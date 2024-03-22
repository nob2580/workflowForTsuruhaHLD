package eteam.gyoumu.user;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.PasswordValidTerm;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.userjouhoukanri.UserJouhouKanriLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 強制パスワード変更Action
 */
@Getter @Setter @ToString
public class ForcePasswordChangeAction extends EteamAbstractAction {
//＜定数＞

//＜画面入力＞
	/** ID または メールアドレス */
	String userId;
	/** パスワード */
	String password;

//＜画面入力以外＞
	/** ログイン前のURL */
	String originalURL;

	/** ログインユーザー情報 */
	User loginUserInfo;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		int passLenMin = Integer.parseInt(setting.passLenMin());
		checkString(userId, 1, 30, "ユーザーID", true); // KEY
		checkString(password, passLenMin, 32, "パスワード", false);
		errorList.addAll(EteamCommon.passwordCheck(password));
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//item  				E1～の必須(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{userId,              "ユーザーID"			,"0", "2", }, // KEY
			{password,            "パスワード"			,"0", "1", },
		};
		hissuCheckCommon(list, eventNum);
	}

	/**
	 * E1:初期表示イベント
	 * @return 結果
	 */
	public String initForcePassword() {

		/* セッションからログインユーザー情報を取得します。 */
		loginUserInfo = getUser();
		userId = loginUserInfo.getLoginUserId();

		try(EteamConnection connection = EteamConnection.connect()) {
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			// データの取得
			GMap userJouhouMap = bumonUserLogic.selectUserJouhou(userId);

			//2.データ存在チェック
			// nullになることはあり得ない。
			if (userJouhouMap == null) {
				throw new EteamDataNotFoundException();
			}

			//3.アクセス可能チェック（権限チェック）
			// このイベントにアクセスできない人はいません。

			//4.処理（初期表示）
			password    = userJouhouMap.get("password").toString();
		}
		
		// 強制表示画面であるフラグを設定
		ServletActionContext.getRequest().setAttribute("forceGamen", true);

		errorList.add("パスワードの有効期限切れです。パスワードを変更してください。");

		//5.戻り値を返す
		return "success";
	}

	/**
	 * E2:ログインイベント
	 * @return 結果
	 */
	public String updateForcePassword() {

		//1.入力チェック
		hissuCheck(2);
		formatCheck();
		if (!errorList.isEmpty()) {
			// 強制表示画面であるフラグを設定
			ServletActionContext.getRequest().setAttribute("forceGamen", true);
			return "error";
		}

		/* セッションからログインユーザー情報を取得します。 */
		loginUserInfo = getUser();
		if (!loginUserInfo.getLoginUserId().equals(userId)) {
			// 不正アクセス
			throw new EteamDataNotFoundException();
		}

		try(EteamConnection connection = EteamConnection.connect()) {
			SecurityLogLogic logLogic = EteamContainer.getComponent(SecurityLogLogic.class, connection);
			UserJouhouKanriLogic myLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			// nullになることはあり得ない。
			GMap userJouhouMap = bumonUserLogic.selectUserJouhou(userId);
			if (userJouhouMap == null) {
				throw new EteamDataNotFoundException();
			}

			String registedPassword = userJouhouMap.get("password").toString();
			if (password.equals(registedPassword)) {
				errorList.add("現在のパスワードは使用できません。新しいパスワードを入力してください。");

				// 強制表示画面であるフラグを設定
				ServletActionContext.getRequest().setAttribute("forceGamen", true);
				return "error";
			}

			//3.アクセス可能チェック
			// このイベントにアクセスできない人はいません。

			//4.処理
			// パスワード更新
			myLogic.updataPassword(userId, userId, password, PasswordValidTerm.NORMAL);
			logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_PASSCHG, LogDetail.PASSCHG_FORCE_OWN);

			connection.commit();
		}

		//5.戻り値を返す
		originalURL = (String)session.get(Sessionkey.ORIGINAL_URL2);
		if (null != originalURL) {
			session.remove(Sessionkey.ORIGINAL_URL2);
			return "loginBack";
		}

		return "success";
	}
}
