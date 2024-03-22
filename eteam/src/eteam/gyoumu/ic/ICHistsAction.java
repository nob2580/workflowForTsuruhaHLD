package eteam.gyoumu.ic;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.EteamConst.UserId;
import eteam.common.EteamIO;
import eteam.common.SecurityLogLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.user.UserLogic;
import eteam.gyoumu.userjouhoukanri.UserJouhouKanriLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * インフォメーション変更画面Action
 */
@Getter @Setter @ToString
public class ICHistsAction extends EteamAbstractAction {
	/** 部品 */ EteamConnection connection;
	/** 部品 */ SecurityLogLogic logLogic;
	/** 部品 */ UserJouhouKanriLogic userLogic;
	/** 部品 */ BumonUserKanriCategoryLogic selectLogic;
	/** 部品 */ UserLogic lc;
	/** 部品 */ ICLogic icLogic;
	/** 部品 */ EteamLogger log = EteamLogger.getLogger(getClass());

	/** バス端末種別定数 */ final static String IC_CTYPE_BUS = "5";

	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String execute(){
		PrintWriter out = null;
		String stat = "500";
		try{
			//リクエスト読み取る
			HttpServletRequest request = ServletActionContext.getRequest();
			String jsonText = EteamIO.read(request.getReader());
			Gson gson = new Gson();
			ICHistsJson jsonObj = gson.fromJson(jsonText, ICHistsJson.class);

			//レスポンス準備
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
			out = response.getWriter();

			//パラメータ不正チェック、だめならエラー返して終わり
			if(jsonObj == null || isEmpty(jsonObj.cardid) || isEmpty(jsonObj.token) || isEmpty(jsonObj.userid)){
				stat = "500";
				throw new Exception("パラメータ不正：" + jsonObj);
			}

			//接続
			connection = EteamConnection.connect();
			initialize();

			//ICカードは登録済？
			String userId = selectLogic.selectUserForIdOrAddress(jsonObj.userid);
			if(userId == null || userId.equals(UserId.ADMIN)){
				throw new Exception("対応ユーザーID取得失敗");
			}
			boolean ninshouZumi = icLogic.findIcCard(jsonObj.cardid, jsonObj.token, userId);

			//認証済じゃないのでエラー
			if(!ninshouZumi){
				stat = "401";
				throw new Exception("認証未済");
			}

			//ICカード履歴登録
			if(jsonObj.trns != null) for(ICHistsTrnsJson tr : jsonObj.trns){
				if(tr != null && isNotEmpty(tr.date) && isNotEmpty(tr.seq)){
					//電車等かバスかで登録処理分岐
					if(!IC_CTYPE_BUS.equals(tr.ctype)) {
						icLogic.insertTrain(jsonObj.cardid, tr, userId, "0"); // 物販など含め、バス以外こっち
					}else {
						icLogic.insertBus(jsonObj.cardid, tr, userId, "0"); // バス専用仕様、とりあえず維持
					}
				}
			}
			out(out, "200", jsonObj.trns == null ? 0 : jsonObj.trns.size());
		}catch(Exception e){
			log.error("ICカード履歴登録でエラー", e);
			out(out, stat, 0);
		}finally{
			if(connection != null){
				connection.commit();
				connection.close();
			}
			if(out != null){
				out.close();
			}
		}
		return "success";
	}

	/**
	 * 部品初期化
	 */
	protected void initialize(){
		logLogic = EteamContainer.getComponent(SecurityLogLogic.class, connection);
		userLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
		selectLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		lc = EteamContainer.getComponent(UserLogic.class, connection);
		icLogic = EteamContainer.getComponent(ICLogic.class, connection);
	}

	/**
	 * レスポンスJSON書き込み
	 * @param out レスポンス
	 * @param stat ステータス
	 * @param cnt 件数
	 */
	protected void out(PrintWriter out, String stat, int cnt) {
		out.write("{ \"stat\" : \"" + stat + "\" }");
		out.write("{ \"cnt\" : \"" + cnt + "\" }");
	}

	/** ICカード履歴JSON */
	@Getter @Setter @ToString
	public class ICHistsJson{
		/** カードID */ String cardid;
		/** トークン */ String token;
		/** 履歴 */ List<ICHistsTrnsJson> trns;
		/** ユーザーID */ String userid;
	}
	/** ICカード履歴JSON(明細) */
	@Getter @Setter @ToString
	public class ICHistsTrnsJson{
		/** 日付 */ String date;
		/** 通番 */ String seq;
		/** 駅(FROM) */ ICHistsStation from;
		/** 駅(TO) */ ICHistsStation to;
		/** 運賃 */ String fare;
		/** 端末種別 */ String ctype;
		/** 処理内容 */ String process;
		/** 残高 */ String balance;
		/** 全バイト配列 */ String bytecd;
	}
	/** ICカード履歴JSON(駅) */
	@Getter @Setter @ToString
	public class ICHistsStation{
		/** 路線コード */ String linecode;
		/** 地域 */ String region;
		/** 駅コード */ String station;
	}
}
