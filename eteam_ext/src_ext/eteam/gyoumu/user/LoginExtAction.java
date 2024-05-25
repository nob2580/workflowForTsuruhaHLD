package eteam.gyoumu.user;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamCommon;
import eteam.common.EteamExtConst.Sessionkey;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LoginExtAction extends LoginAction {
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
			User user = ((UserExtLogic)lc).makeSessionUser(userId);
			session.put(Sessionkey.USER, user);
			//▼▼カスタマイズ　会社切り替えの為
			//sesLg.updateUserSession(user);
			session.put(Sessionkey.SCHEMA_NAME, EteamCommon.getContextSchemaName());
			//▲▲カスタマイズ

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
