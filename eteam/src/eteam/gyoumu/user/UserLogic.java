package eteam.gyoumu.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import eteam.base.GMap;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.EteamConst.UserId;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.userjouhoukanri.UserJouhouKanriLogic;

/**
 * ユーザ管理機能ロジック
 */
public class UserLogic extends EteamAbstractLogic {

	/**
	 * ユーザ情報を取得する。
	 * @param userId ユーザID または メールアドレス
	 * @return  ユーザ情報。該当ユーザ情報がなければnull。
	 */
	public User makeSessionUser(String userId){
		BumonUserKanriCategoryLogic selectLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

		//ユーザー情報を取得する
		GMap userMap = selectLogic.selectValidUserInfo(userId);
		if (null == userMap) {
			return null;
		}
		User user = new User();
		user.loginUserId = (String)userMap.get("user_id");
		user.loginUserSei = (String)userMap.get("user_sei");
		user.loginUserMei = (String)userMap.get("user_mei");
		user.loginShainNo = (String)userMap.get("shain_no");
		user.loginYakushokuCd = (String)userMap.get("yakushoku_cd");

		//ユーザーに紐付く所属部門情報を取得する
		List<GMap> bumonRoleList = selectLogic.selectValidShozokuBumonRole(userId);
		user.loginBumonCd = new String[bumonRoleList.size()];
		user.loginBumonName = new String[bumonRoleList.size()];
		user.loginBumonFullName = new String[bumonRoleList.size()];
		user.loginBumonRoleId = new String[bumonRoleList.size()];
		user.loginBumonRoleName = new String[bumonRoleList.size()];
		user.loginDaihyouFutanBumonCd = new String[bumonRoleList.size()];
		int i = 0;
		for(GMap bumonRole : bumonRoleList) {
			user.loginBumonCd[i] = (String)bumonRole.get("bumon_cd");
			user.loginBumonName[i] = (String)bumonRole.get("bumon_name");
			user.loginBumonFullName[i] = EteamCommon.connectBumonName(connection, user.loginBumonCd[i]);
			user.loginBumonRoleId[i] = (String)bumonRole.get("bumon_role_id");
			user.loginBumonRoleName[i] = (String)bumonRole.get("bumon_role_name");
			user.loginDaihyouFutanBumonCd[i] = (String)bumonRole.get("daihyou_futan_bumon_cd");
			i++;
		}

		//ユーザーに紐付く業務ロール情報を取得する
		List<GMap> gyoumuRoleListTmp = selectLogic.selectValidGyoumuRole(userId);
		
		//業務ロールIDごとの部門コードを全て取得
		Map<String,GMap> roleIdMap = new LinkedHashMap<>();
		for(GMap gyoumuRole : gyoumuRoleListTmp) {
			String roleId = (String)gyoumuRole.get("gyoumu_role_id");
			String bumonCd = (String)gyoumuRole.get("shori_bumon_cd");
			if( !(roleIdMap.containsKey(roleId)) ){
				//業務ロールID新規追加
				roleIdMap.put(roleId, gyoumuRole);
			}else{
				//業務ロールID毎の部門コード追加
				GMap tmpMap = roleIdMap.get(roleId);
				String tmpBumon = (String)tmpMap.get("shori_bumon_cd");
				tmpBumon = tmpBumon + "," + bumonCd;
				tmpMap.put("shori_bumon_cd", tmpBumon);
				roleIdMap.put(roleId, tmpMap);
			}
		}
		List<GMap> gyoumuRoleList = new ArrayList<>();
		//取得される順序はキーが追加された順になるはず
		for (GMap roleMap : roleIdMap.values()) {
		    gyoumuRoleList.add(roleMap);
		}
		
		user.gyoumuRoleIdKouho = new String[gyoumuRoleList.size()];
		user.gyoumuRoleNameKouho = new String[gyoumuRoleList.size()];
		user.gyoumuRoleShozokuBumonCdKouho = new String[gyoumuRoleList.size()];
		i = 0;
		for(GMap gyoumuRole : gyoumuRoleList) {
			user.gyoumuRoleIdKouho[i] = (String)gyoumuRole.get("gyoumu_role_id");
			user.gyoumuRoleNameKouho[i] = (String)gyoumuRole.get("gyoumu_role_name");
			user.gyoumuRoleShozokuBumonCdKouho[i] = (String)gyoumuRole.get("shori_bumon_cd");
			i++;
		}

		//業務ロールを持つなら、ロール選択可能
		user.enableRoleSentaku = (0 < gyoumuRoleList.size());
		
		
		//マル秘設定権限フラグの取得
		user.maruhiSetteiFlg = "1".equals(userMap.get("maruhi_kengen_flg"));
		
		//マル秘解除権限フラグの取得
		user.maruhiKaijyoFlg = "1".equals(userMap.get("maruhi_kaijyo_flg"));
		
		//マル秘参照権限フラグの取得
		user.maruhiKengenFlg = user.maruhiSetteiFlg || user.maruhiKaijyoFlg;

		
		//承認ルート変更権限レベルの取得
		user.shouninRouteHenkouLevel = (String)userMap.get("shounin_route_henkou_level");

		//ADMINユーザーは、一般ユーザー禁止で強制業務ロール
		if (UserId.ADMIN.equals(userId)) {
			changeGyoumuRole(user, GyoumuRoleId.SYSTEM_KANRI);
			user.enableRoleSentaku = false;
		}
		return user;
	}

	/**
	 * セッション・ユーザーの被代行者者属性を変更する。
	 * 代行を解除する時は、hiDaikouUserIdにnullを渡す。
	 * 代行する時は、hiDaikouUserIdに被代行ユーザーIDを渡す。
	 * 
	 * @param user セッション・ユーザー情報
	 * @param hiDaikouUserId 被代行ユーザーID
	 */
	public void changeHiDaikouUserInfo(User user, String hiDaikouUserId) {
		BumonUserKanriCategoryLogic selectLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

		//本人に戻るなら、被代行者属性を解除
		if (null == hiDaikouUserId) {
			user.hiDaikouUserId = null;
			user.hiDaikouUserSei = null;
			user.hiDaikouUserMei = null;
			user.hiDaikouShainNo = null;
			user.hiDaikouDaihyouFutanBumonCd = null;
			user.hiDaikouYakushokuCd = null;
			user.hiDaikouBumonCd = null;
			user.hiDaikouBumonName = null;
			user.hiDaikouBumonFullName = null;
			user.hiDaikouBumonRoleId = null;
			user.hiDaikouBumonRoleName = null;
			
		//代行するなら、被代行者属性をセット
		} else {
			//被代行者自体の情報
			GMap hiDaikouUserMap = selectLogic.selectValidUserInfo(hiDaikouUserId);
			user.hiDaikouUserId = (String)hiDaikouUserMap.get("user_id");
			user.hiDaikouUserSei = (String)hiDaikouUserMap.get("user_sei");
			user.hiDaikouUserMei = (String)hiDaikouUserMap.get("user_mei");
			user.hiDaikouShainNo = (String)hiDaikouUserMap.get("shain_no");
			user.hiDaikouYakushokuCd = (String)hiDaikouUserMap.get("yakushoku_cd");

			//被代行者の所属情報
			List<GMap> bumonRoleList = selectLogic.selectValidShozokuBumonRole(hiDaikouUserId);
			user.hiDaikouBumonCd = new String[bumonRoleList.size()];
			user.hiDaikouBumonName = new String[bumonRoleList.size()];
			user.hiDaikouBumonFullName = new String[bumonRoleList.size()];
			user.hiDaikouBumonRoleId = new String[bumonRoleList.size()];
			user.hiDaikouBumonRoleName = new String[bumonRoleList.size()];
			user.hiDaikouDaihyouFutanBumonCd = new String[bumonRoleList.size()];
			int i = 0;
			for(GMap bumonRole : bumonRoleList) {
				user.hiDaikouBumonCd[i] = (String)bumonRole.get("bumon_cd");
				user.hiDaikouBumonName[i] = (String)bumonRole.get("bumon_name");
				user.hiDaikouBumonFullName[i] = EteamCommon.connectBumonName(connection, user.hiDaikouBumonCd[i]);
				user.hiDaikouBumonRoleId[i] = (String)bumonRole.get("bumon_role_id");
				user.hiDaikouBumonRoleName[i] = (String)bumonRole.get("bumon_role_name");
				user.hiDaikouDaihyouFutanBumonCd[i] = (String)bumonRole.get("daihyou_futan_bumon_cd");
				i++;
			}
		}
	}

	/**
	 * セッション・ユーザーの業務ロール属性を変更する。
	 * 部門所属ユーザーとしてログインする時は、gyoumuRoleIdにnullを渡す。
	 * 業務ロールでログインする時は、gyoumuRoleIdに選択した業務ロールIDを渡す。
	 * 
	 * @param user セッション・ユーザー
	 * @param gyoumuRoleId 業務ロールID
	 */
	public void changeGyoumuRole(User user, String gyoumuRoleId) {

		//部門所属ユーザーとしてログインする時
		if (null == gyoumuRoleId) {
			user.gyoumuRoleId = null;
			user.gyoumuRoleName = null;
			user.gyoumuRoleShozokuBumonCd = null;

		//業務ロールでログインする時
		} else {
			user.gyoumuRoleId = gyoumuRoleId;
			for (int i = 0; i < user.gyoumuRoleIdKouho.length; i++) {
				if (gyoumuRoleId.equals(user.gyoumuRoleIdKouho[i])) {
					user.gyoumuRoleName = user.gyoumuRoleNameKouho[i];
					user.gyoumuRoleShozokuBumonCd = user.gyoumuRoleShozokuBumonCdKouho[i].split(",");
				}
			}
		}
	}

	/**
	 * 認証
	 * @param userIdOrMailAddress ユーザーIDまたはメールアドレス
	 * @param password パスワード
	 * @param error エラーメッセージ
	 * @return 認証成功ならユーザーID、認証失敗ならnull
	 */
	public String ninsho(String userIdOrMailAddress, String password, StringBuffer error) {
		BumonUserKanriCategoryLogic selectLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		SecurityLogLogic logLogic = EteamContainer.getComponent(SecurityLogLogic.class, connection);
		UserJouhouKanriLogic userLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
		
		// userIdの取得
		String ret = null;
		String userId = selectLogic.selectUserForIdOrAddress(userIdOrMailAddress);
		int status = userLogic.checkUserStatusAndPassword(userId, password);
		switch (status) {
		default:
		case 4:
			// アカウントが存在しない
			error.append("ID(メールアドレス)またはパスワードが一致しません。");
			logLogic.insertLog(null, userIdOrMailAddress, LogType.TYPE_LOGIN, LogDetail.LOGIN_FAIL_USERID);
			break;
		case 3:
			// アカウントロック
			error.append("アカウントがロックされています。管理者にお問い合わせください。");
			logLogic.insertLog(null, userId, LogType.TYPE_LOGIN, LogDetail.LOGIN_FAIL_LOCK);
			break;
		case 2:
			// 一時ロック
			error.append("アカウントがロックされています。管理者にお問い合わせください。");
			logLogic.insertLog(null, userId, LogType.TYPE_LOGIN, LogDetail.LOGIN_FAIL_TMP_LOCK);
			break;
		case 1:
			// パスワード間違い
			error.append("ID(メールアドレス)またはパスワードが一致しません。");
			logLogic.insertLog(null, userId, LogType.TYPE_LOGIN, LogDetail.LOGIN_FAIL_PASS);
			if (2 == userLogic.incrementPasswordFailureCount(userId)) {
				// パスワード間違いandロック
				logLogic.insertLog(null, userId, LogType.TYPE_ACCOUNT, LogDetail.ACCOUNT_TMP_LOCK);
			}
			break;
		case 0:
			// 成功：ロック回数リセット
			userLogic.resetPasswordFailureCount(userId, true);
			ret = userId;
			break;
		}
		return ret;
	}
}
