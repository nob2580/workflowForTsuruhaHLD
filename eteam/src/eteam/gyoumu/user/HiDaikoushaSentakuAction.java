package eteam.gyoumu.user;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamConst.UserId;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 被代行者選択画面Action
 */
@Getter @Setter @ToString
public class HiDaikoushaSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** ユーザーID */
	String selectedUserId;

//＜画面入力以外＞
	/** 被代行ユーザーリスト */
	List<GMap> hiDaikouUserList;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(selectedUserId, 1, 30, "ユーザーID", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//item E1～の必須(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{selectedUserId, "ユーザーID"		,"0", "0", },
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
		
		try(EteamConnection connection = EteamConnection.connect()) {
			//4.処理
			
			//被代行者リスト取得
			makeOutput(connection);
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * E2:選択イベント
	 * @return 結果
	 */
	public String sentaku(){
		User user = super.getUser();
		try(EteamConnection connection = EteamConnection.connect()) {
			UserLogic userLogic  = EteamContainer.getComponent(UserLogic.class, connection);
			BumonUserKanriCategoryLogic bumonSelLogic  = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			UserSessionLogic usLg  = EteamContainer.getComponent(UserSessionLogic.class, connection);

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
			usLg.updateUserSession(user);

			//5.戻り値を返す
			connection.commit();
			return "success";
		}
	}

	/**
	 * 表示内容設定共通処理
	 * @param connection コネクション
	 */
	protected void makeOutput(EteamConnection connection) {
		User user = super.getUser();
		BumonUserKanriCategoryLogic bumonSelLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

		//ADMINの時は全員、一般なら代行登録されたユーザーのみ
		if (user.getLoginUserId().equals(UserId.ADMIN)) {
			hiDaikouUserList = bumonSelLogic.selectAllValidUser();
		} else {
			hiDaikouUserList = bumonSelLogic.selectValidHiDaikouUser(user.loginUserId);
		}

		//被代行者に所属部門・部門ロールを紐つけ
		for (GMap hiDaikouUser: hiDaikouUserList) {
			String userId = (String)hiDaikouUser.get("user_id");
			List<GMap> bumonRoleList = bumonSelLogic.selectValidShozokuBumonRole(userId);
			//部門名はフルで
			for (GMap bumonRole : bumonRoleList) {
				bumonRole.put("bumon_full_name", EteamCommon.connectBumonName(connection, (String)bumonRole.get("bumon_cd")));
			}
			hiDaikouUser.put("bumonRoleList", bumonRoleList);
		}
	}
}
