package eteam.gyoumu.user;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ロール選択画面Action
 */
@Getter @Setter @ToString
public class RoleSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 業務ロールID */
	String selectedGyoumuRoleId;

//＜画面入力以外＞
	/** ログイン前のURL */
	String originalURL;
	/** 業務ロールID */
	String[] gyoumuRoleId;
	/** 業務ロール名 */
	String[] gyoumuRoleName;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(selectedGyoumuRoleId, 5, 5, "業務ロールID", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//item  				E1～の必須(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{selectedGyoumuRoleId,"業務ロールID"				,"0", "0", },
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
		//なし

		//4.処理
		makeOutput();

		//5.戻り値を返す
		return "success";
	}

	/**
	 * E2:選択イベント
	 * @return 結果
	 */
	public String sentaku(){
		User user = super.getUser();

		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		if(0 < errorList.size()){
			makeOutput();
			return "error";
		}

		try(EteamConnection connection = EteamConnection.connect()) {
			UserLogic lc  = EteamContainer.getComponent(UserLogic.class, connection);
			SecurityLogLogic logLogic = EteamContainer.getComponent(SecurityLogLogic.class, connection);
			UserSessionLogic usLg  = EteamContainer.getComponent(UserSessionLogic.class, connection);

			String oldGyoumuRollId = user.getGyoumuRoleId();

			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			//まとめて

			//部門所属ユーザーとして」リンククリック → 業務ロール選択解除
			if (isEmpty(selectedGyoumuRoleId)) {
				lc.changeGyoumuRole(user, null);
				
			//業務ロールリンククリック → 業務ロールを設定（そもそも選べないのならエラー）
			} else {
				int gyoumuRoleIndex = -1;
				for (int i = 0; i < user.gyoumuRoleIdKouho.length; i++) {
					if (selectedGyoumuRoleId.equals(user.gyoumuRoleIdKouho[i])) {
						gyoumuRoleIndex = i;
					}
				}
				if (-1 == gyoumuRoleIndex) {
					throw new EteamAccessDeniedException();
				}
				lc.changeGyoumuRole(user, selectedGyoumuRoleId);
			}

			//代行解除　本人所属に戻す
			lc.changeHiDaikouUserInfo(user, null);

			//セッション共有
			session.put(Sessionkey.USER, user);
			usLg.updateUserSession(user);

			logLogic.insertLog(user.getLoginUserId(), oldGyoumuRollId, user.getGyoumuRoleId(), LogType.TYPE_LOGIN, LogDetail.LOGIN_ROLE_CHG);
			connection.commit();

			//5.戻り値を返す
			//他URLからの強制ログインなら他URLに戻る。そうでなければメニューへ。
			originalURL = (String)session.get(EteamConst.Sessionkey.ORIGINAL_URL);
			session.remove(EteamConst.Sessionkey.ORIGINAL_URL);
			return (null != originalURL) ? "loginBack" : "success";
		}
	}

	/**
	 * 出力内容生成
	 */
	protected void makeOutput() {
		User user = getUser();
		gyoumuRoleId = user.gyoumuRoleIdKouho;
		gyoumuRoleName = user.gyoumuRoleNameKouho;
	}
}
