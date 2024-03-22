package eteam.gyoumu.user;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ログインAction
 */
@Getter @Setter @ToString
public class LoginAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** ID または メールアドレス */
	String userIdOrMailAddress;
	/** パスワード */
	String password;
	/** チェック */
	String ever;
	/** 区分 */
	String kbn;

//＜画面入力以外＞
	/** ログイン前のURL */
	String originalURL;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(userIdOrMailAddress, 1, 50, "ID", false);
		checkString(password, 1, 32, "パスワード", false);
		checkCheckbox(ever, "チェックボックス", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//item  				E1～の必須(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{userIdOrMailAddress,"ID または メールアドレス"		,"0", "1", },
			{password ,"パスワード"					,"0", "1", },
			{ever ,"チェック"						,"0", "0", },
		};
		hissuCheckCommon(list, eventNum);
	}

	/**
	 * E1:初期表示イベント
	 * @return 結果
	 */
	public String init(){

		//1.入力チェック
		//2.データ存在チェック
		//3.アクセス可能チェック
		//4.処理
		//なし

		//5.戻り値を返す
		return "success";
	}

	/**
	 * 強制ログイン
	 * @return 結果
	 */
	public String forceLogin(){
		
		kbn = "1";
		
		//1.入力チェック
		//2.データ存在チェック
		//3.アクセス可能チェック
		//4.処理
		//なし

		//5.戻り値を返す
		return "success";
	}

	/**
	 * E2:ログインイベント
	 * @return 結果
	 */
	public String login(){

		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		if(0 < errorList.size()){
			return "error";
		}

		try(EteamConnection connection = EteamConnection.connect()) {
			SecurityLogLogic logLogic  = EteamContainer.getComponent(SecurityLogLogic.class, connection);
			UserLogic lc  = EteamContainer.getComponent(UserLogic.class, connection);
			UserSessionLogic sesLg  = EteamContainer.getComponent(UserSessionLogic.class, connection);

			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			//まとめて
			StringBuffer errorMessage = new StringBuffer();
			String userId = lc.ninsho(userIdOrMailAddress, password, errorMessage);
			if (userId == null) {
				connection.commit();
				errorList.add(errorMessage.toString());
				return "error";
			}

			//セッション共有
			User user = lc.makeSessionUser(userId);
			session.put(Sessionkey.USER, user);
			sesLg.updateUserSession(user);

			// パスワードチェック済みフラグを落としておく
			ServletActionContext.getRequest().getSession().removeAttribute(Sessionkey.PASSWORD_PERIOD_CHECK);

			logLogic.insertLog(user, userId, LogType.TYPE_LOGIN, LogDetail.LOGIN_SUCCESS);

			connection.commit();

			//5.戻り値を返す
			originalURL = (String)session.get(Sessionkey.ORIGINAL_URL);
			if (user.enableRoleSentaku) {
				return "roleSentaku";
			} else if (null != originalURL) {
				session.remove(Sessionkey.ORIGINAL_URL);
				return "loginBack";
			} else {
				return "success";
			}
		}
	}
}
