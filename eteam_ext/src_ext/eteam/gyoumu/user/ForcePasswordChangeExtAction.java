package eteam.gyoumu.user;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.PasswordValidTerm;
import eteam.common.EteamExtConst.Sessionkey;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.userjouhoukanri.UserJouhouKanriLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ForcePasswordChangeExtAction extends ForcePasswordChangeAction {
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
		session.put(Sessionkey.SCHEMA_NAME, EteamCommon.getContextSchemaName());	//◀◀◀カスタマイズ 会社切り替え為
		originalURL = (String)session.get(Sessionkey.ORIGINAL_URL2);
		if (null != originalURL) {
			session.remove(Sessionkey.ORIGINAL_URL2);
			return "loginBack";
		}

		return "success";
	}
}
