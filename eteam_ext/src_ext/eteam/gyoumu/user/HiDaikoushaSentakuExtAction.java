package eteam.gyoumu.user;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.EteamConst.UserId;
import eteam.common.EteamExtConst.Sessionkey;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class HiDaikoushaSentakuExtAction extends HiDaikoushaSentakuAction {
	/**
	 * E2:選択イベント
	 * @return 結果
	 */
	public String sentaku(){
		User user = super.getUser();
		try(EteamConnection connection = EteamConnection.connect()) {
			UserLogic					userLogic		 = EteamContainer.getComponent(UserLogic.class, connection);
			BumonUserKanriCategoryLogic	bumonSelLogic	 = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			UserSessionLogic			usLg			 = EteamContainer.getComponent(UserSessionLogic.class, connection);

			//1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(0 < errorList.size()){
				makeOutput(connection);
				return "error";
			}
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			//2～4まとめて
			
			//代行する時（代行解除ではなく）
			if (isNotEmpty(selectedUserId)) {
				//adminの代行はNG
				if (selectedUserId.equals(UserId.ADMIN)) {
					throw new EteamAccessDeniedException();
				}
				
				//部門所属ユーザーとしてログインしているなら、代行指定されていることをチェック
				if (! user.getLoginUserId().equals(UserId.ADMIN)) {
					if (0 == bumonSelLogic.selectDaikouCount(user.loginUserId, selectedUserId)){
						throw new EteamAccessDeniedException();
					}
				}
				
				//被代行ユーザーが有効
				if (null == bumonSelLogic.selectValidUserInfo(selectedUserId)) {
					throw new EteamAccessDeniedException();
				}
			}
			
			//セッション・ユーザー情報更新。本人に戻す(null) or 被代行者設定(selectedUserId)
			userLogic.changeHiDaikouUserInfo(
				user,
				isEmpty(selectedUserId) ? null : selectedUserId
			);

			//ADMINによる代行解除 → ADMINモードに
			if (user.getLoginUserId().equals(UserId.ADMIN) && isEmpty(selectedUserId)) {
				userLogic.changeGyoumuRole(user, GyoumuRoleId.SYSTEM_KANRI);
			//ADMINによる代行、ADMIN以外による代行/代行解除 → 一般モードに
			} else {
				userLogic.changeGyoumuRole(user, null);
			}

			//セッション共有
			session.put(Sessionkey.USER, user);
//			usLg.updateUserSession(user);	//◀◀カスタマイズ

			//5.戻り値を返す
			connection.commit();
			return "success";
		}
	}
}
