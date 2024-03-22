package eteam.gyoumu.user;

import org.apache.struts2.dispatcher.SessionMap;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ログアウトAction
 */
@Getter @Setter @ToString
public class LogoutAction extends EteamAbstractAction {

//＜定数＞
//＜画面入力＞
//＜画面入力以外＞

//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}

//＜イベント＞
	/**
	 * ログアウトイベント。
	 * @return 処理結果
	 */
	public String logout() {

		//1.入力チェック
		//2.データ存在チェック
		//3.アクセス可能チェック
		//なし

		try(EteamConnection connection = EteamConnection.connect()) {
			UserSessionLogic usLg = EteamContainer.getComponent(UserSessionLogic.class, connection);
			
			//4.処理
			usLg.deleteUserSession();
			((SessionMap<String, Object>)session).invalidate();
	
			//5.戻り値を返す
			connection.commit();
			return "success";
		}
	}
}
