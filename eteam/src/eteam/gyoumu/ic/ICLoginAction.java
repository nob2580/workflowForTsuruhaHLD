package eteam.gyoumu.ic;

import java.io.PrintWriter;
import java.util.UUID;

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
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
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
public class ICLoginAction extends EteamAbstractAction {
	/** 部品 */ EteamConnection connection;
	/** 部品 */ SecurityLogLogic logLogic;
	/** 部品 */ UserJouhouKanriLogic userLogic;
	/** 部品 */ BumonUserKanriCategoryLogic selectLogic;
	/** 部品 */ UserLogic lc;
	/** 部品 */ ICLogic icLogic;
	/** 部品 */ EteamLogger log = EteamLogger.getLogger(getClass());

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
		try{
			//リクエスト読み取る
			HttpServletRequest request = ServletActionContext.getRequest();
			String jsonText = EteamIO.read(request.getReader());
			Gson gson = new Gson();
			ICLoginJson jsonObj = gson.fromJson(jsonText, ICLoginJson.class);

			//レスポンス準備
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
			out = response.getWriter();

			//パラメータ不正チェック、だめならエラー返して終わり
			if(jsonObj == null || isEmpty(jsonObj.userid) || isEmpty(jsonObj.password) || isEmpty(jsonObj.cardid)){
				throw new Exception("パラメータ不正：" + jsonObj);
			}

			//接続
			connection = EteamConnection.connect();
			initialize();

			//認証、だめならエラー返して終わり
			StringBuffer errorMessage = new StringBuffer();
			String userId = lc.ninsho(jsonObj.userid, jsonObj.password, errorMessage);
			if(userId == null || userId.equals(UserId.ADMIN)){
				throw new Exception("認証エラー：" + errorMessage.toString());
			}

			//認証成功なので、トークンを発行、トークンを返して終わり
			String token = UUID.randomUUID().toString();
			icLogic.insertUserToken(jsonObj.cardid, token, userId);
			logLogic.insertLog(null, userId, LogType.TYPE_LOGIN, LogDetail.LOGIN_SUCCESS);
			out(out, token);

		}catch(Exception e){
			log.error("ICカード認証でエラー", e);
			out(out, "");
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
	 * @param token トークン（認証エラーなら空）
	 */
	protected void out(PrintWriter out, String token) {
		out.write("{ \"token\" : \"" + token + "\" }");
	}

	/**
	 * ICカード認証のリクエストJSON
	 */
	@Getter @Setter @ToString
	public class ICLoginJson{
		/** ユーザーID */ String userid;
		/** パスワード */ String password;
		/** カードID */ String cardid;
	}
}
