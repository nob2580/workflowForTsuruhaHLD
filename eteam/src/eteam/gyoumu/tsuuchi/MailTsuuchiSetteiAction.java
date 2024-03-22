package eteam.gyoumu.tsuuchi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.TsuuchiCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * メール通知設定画面Action
 */
@Getter @Setter @ToString
public class MailTsuuchiSetteiAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 滞留通知 */
	String tairyuuTsuuchi;
	
	/** リアルタイム通知 */
	/** 承認待ち */
	String realTimeMyTurnShounin;
	/** 自伝票差戻し */
	String realTimeMyTurnSashimodoshi;
	/** 合議部署承認待ち */
	String realTimeMyTurnGougiShounin;
	/** 自伝票最終確認 */
	String realTimeMyDenpyouSaisyuushounin;
	/** 自伝票否認 */
	String realTimeMyDenpyouHinin;
	/** 関連伝票最終承認 */
	String realTimeSaisyuuShounin;
	/** 関連伝票否認 */
	String realTimeHinin;
	
//＜画面入力以外＞

//＜入力チェック＞
	@Override protected void formatCheck() {
		checkCheckbox(tairyuuTsuuchi, "滞留通知", false);
		checkCheckbox(realTimeMyTurnShounin, "承認待ち", false);
		checkCheckbox(realTimeMyTurnSashimodoshi, "自伝票差戻し", false);
		checkCheckbox(realTimeMyTurnGougiShounin, "合議部署承認待ち", false);
		checkCheckbox(realTimeMyDenpyouSaisyuushounin, "自伝票最終確認", false);
		checkCheckbox(realTimeMyDenpyouHinin, "自伝票否認", false);
		checkCheckbox(realTimeSaisyuuShounin, "関連伝票最終承認", false);
		checkCheckbox(realTimeHinin, "関連伝票否認", false);
	}
	@Override protected void hissuCheck(int eventNum) {
		String[][] list = {
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {

		//1.入力チェック なし
		//2.データ存在チェック なし
		//3.アクセス可能チェック なし
		
		try(EteamConnection connection = EteamConnection.connect()) {
			//4.処理
			TsuuchiCategoryLogic tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
			
			// メール通知設定情報の取得
			List<GMap> mailTsuuchiSetteiList = tsuuchiLogic.loadMailTsuuchiSettei(getUser().getTourokuOrKoushinUserId());
			
			// 取得情報のデータが存在チェック
			if(mailTsuuchiSetteiList.isEmpty()) {
				// 取得情報のデータが存在しない場合、送信有無 = '0'に設定
				tairyuuTsuuchi                  = "0";
				realTimeMyTurnShounin           = "0";
				realTimeMyTurnSashimodoshi      = "0";
				realTimeMyTurnGougiShounin      = "0";
				realTimeMyDenpyouSaisyuushounin = "0";
				realTimeMyDenpyouHinin          = "0";
				realTimeSaisyuuShounin          = "0";
				realTimeHinin                   = "0";
			} else {
				// 取得情報のデータが存在する場合、それぞれの送信有無の値を設定
				for (GMap map : mailTsuuchiSetteiList) {
					switch(map.get("tsuuchi_kbn").toString()) {
						// 滞留通知
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.TAIRYUU:
							tairyuuTsuuchi = map.get("soushinumu").toString();
							break;
						// 承認待ち
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.SHOUNIN_MACHI:
							realTimeMyTurnShounin = map.get("soushinumu").toString();
							break;
						// 自伝票差戻し
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_SASHIMODOSHI:
							realTimeMyTurnSashimodoshi = map.get("soushinumu").toString();
							break;
						// 合議部署承認待ち
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI:
							realTimeMyTurnGougiShounin = map.get("soushinumu").toString();
							break;
						// 自伝票承認
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_SHOUNIN:
							realTimeMyDenpyouSaisyuushounin = map.get("soushinumu").toString();
							break;
						// 自伝票否認
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_HININ:
							realTimeMyDenpyouHinin = map.get("soushinumu").toString();
							break;
						// 関連伝票承認
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.KANREN_DENPYOU_SHOUNIN:
							realTimeSaisyuuShounin = map.get("soushinumu").toString();
							break;
						// 関連伝票否認
						case EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.KANREN_DENPYOU_HININ:
							realTimeHinin = map.get("soushinumu").toString();
							break;
					}
				}
			}
			// 5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 登録イベント
	 * @return 処理結果
	 */
	public String touroku() {

		//1.入力チェック
		formatCheck();
		
		//2.データ存在チェック なし
		//3.アクセス可能チェック なし
		
		try(EteamConnection connection = EteamConnection.connect()) {
			//4.処理
			TsuuchiCategoryLogic tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
			MailTsuuchiSetteiLogic mailLogic = EteamContainer.getComponent(MailTsuuchiSetteiLogic.class, connection);
			
			// メール通知設定情報テーブルよりデータ件数の取得
			List<GMap> mailTsuuchiSetteiList = tsuuchiLogic.loadMailTsuuchiSettei(getUser().getTourokuOrKoushinUserId());
			
			// メール通知設定情報のデータ件数チェック
			if(mailTsuuchiSetteiList.isEmpty()) {
				// 取得情報のデータ件数が0件だった場合、メール通知情報テーブルにデータを登録
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.TAIRYUU, tairyuuTsuuchi                  == null ? "0" : tairyuuTsuuchi);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.SHOUNIN_MACHI, realTimeMyTurnShounin           == null ? "0" : realTimeMyTurnShounin);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_SASHIMODOSHI, realTimeMyTurnSashimodoshi      == null ? "0" : realTimeMyTurnSashimodoshi);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI, realTimeMyTurnGougiShounin      == null ? "0" : realTimeMyTurnGougiShounin);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_SHOUNIN, realTimeMyDenpyouSaisyuushounin == null ? "0" : realTimeMyDenpyouSaisyuushounin);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_HININ, realTimeMyDenpyouHinin          == null ? "0" : realTimeMyDenpyouHinin);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.KANREN_DENPYOU_SHOUNIN, realTimeSaisyuuShounin          == null ? "0" : realTimeSaisyuuShounin);
				mailLogic.insert(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.KANREN_DENPYOU_HININ, realTimeHinin                   == null ? "0" : realTimeHinin);
			} else {
				// 取得情報のデータ件数が0件以外の場合、メール通知情報テーブルのデータを更新
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.TAIRYUU, tairyuuTsuuchi                  == null ? "0" : tairyuuTsuuchi);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.SHOUNIN_MACHI, realTimeMyTurnShounin           == null ? "0" : realTimeMyTurnShounin);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_SASHIMODOSHI, realTimeMyTurnSashimodoshi      == null ? "0" : realTimeMyTurnSashimodoshi);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI, realTimeMyTurnGougiShounin      == null ? "0" : realTimeMyTurnGougiShounin);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_SHOUNIN, realTimeMyDenpyouSaisyuushounin == null ? "0" : realTimeMyDenpyouSaisyuushounin);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.JI_DENPYOU_HININ, realTimeMyDenpyouHinin          == null ? "0" : realTimeMyDenpyouHinin);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.KANREN_DENPYOU_SHOUNIN, realTimeSaisyuuShounin          == null ? "0" : realTimeSaisyuuShounin);
				mailLogic.update(getUser().getTourokuOrKoushinUserId(), EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN.KANREN_DENPYOU_HININ, realTimeHinin                   == null ? "0" : realTimeHinin);
			}
			
			connection.commit();
			
			// 5.戻り値を返す
			return "success";
		}
	}
}
