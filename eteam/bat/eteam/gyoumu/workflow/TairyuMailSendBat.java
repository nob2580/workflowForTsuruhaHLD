package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamNaibuCodeSetting.KANREN_DENPYOU;
import eteam.common.EteamNaibuCodeSetting.KIDOKU_FLG;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.EteamSendMailLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.user.UserLogic;

/**
 * 滞留メール配信バッチ
 */


public class TairyuMailSendBat extends EteamAbstractBat {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(TairyuMailSendBat.class);
	
	/**
	 * バッチ処理メイン
	 * @param argv 0:スキーマ名
	 */
	public static void main(String[] argv) {
		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		//スキーマ指定
		if (1 != argv.length) {
			throw new IllegalArgumentException("パラメータにスキーマ名が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);

		//実行
		TairyuMailSendBat bat = EteamContainer.getComponent(TairyuMailSendBat.class);
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "滞留メール配信";
	}

	@Override
	public String getCountName() {
		return "配信メール数";
	}

	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {

			TsuuchiCategoryLogic tcl = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
			BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			EteamSendMailLogic esml = EteamContainer.getComponent(EteamSendMailLogic.class, connection);
			SystemKanriCategoryLogic sysl = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			UserLogic usl = EteamContainer.getComponent(UserLogic.class, connection);

			//機能OFFであれば何もせず終わり
			GMap kinouSeigyo = sysl.findKinouSeigyo(KINOU_SEIGYO_CD.MAIL_HAISHIN);
			if(! KINOU_SEIGYO_KBN.YUUKOU.equals(kinouSeigyo.get("kinou_seigyo_kbn"))) {
				log.info("メール配信機能がOFFなので終了します。");
				return 0;
			}

			//滞留設定がONになっているユーザーリストを取得
			List<GMap> userList = bukc.loadTairyuUser();

			//ユーザー単位で処理を繰り返す
			int count = 0;
			for(GMap user : userList) {
				
				String userId = (String) user.get("user_id");
				User userObj = usl.makeSessionUser(userId);
				if (userObj == null)
				{
					continue;
				}

				//承認待ちの伝票件数、未読の通知件数を取得→両方0ならそのユーザーは飛ばす
				long shouninmachiCount = tcl.findDenpyouIchiranKensakuCount(userObj, KANREN_DENPYOU.SHOUNIN_MACHI);
				long tsuuchiIchiranCount = tcl.findTsuuchiCount(userId,KIDOKU_FLG.MIDOKU);
				if (0 == shouninmachiCount && 0 == tsuuchiIchiranCount)
				{
					continue;
				}

				//メール内容
				String subject = "【" + setting.mailTsuuchiSystemName() + "】滞留通知";
				StringBuffer honbun = new StringBuffer();
				honbun.append("お疲れ様です。\r\n\r\n");
				if (0 < shouninmachiCount) {
					honbun.append("承認待ちの伝票が" + shouninmachiCount + "件あります。\r\n");
					honbun.append(setting.shotodokeUrl() + "denpyou_ichiran_kensaku?kanrenDenpyou=020\r\n\r\n");
				}
				if (0 < tsuuchiIchiranCount) {
					honbun.append("未確認の通知が" + tsuuchiIchiranCount + "件あります。\r\n");
					honbun.append(setting.shotodokeUrl() + "tsuuchi_ichiran\r\n\r\n");
				}
				honbun.append("以上です。");
				List<String> to = new ArrayList<>();
				to.add(user.get("mail_address").toString());

				// メール送信
				boolean ret = esml.execute(subject, honbun.toString(), to, null, null);
				if (! ret) log.warn("滞留メール配信でエラー。EteamSendMailLogic#execute():false");
				count++;
			}
			setCount(count);
			return 0;
		}catch(Throwable e){
			log.error("エラー発生" ,e);
			return 1;
		}
	}
}
