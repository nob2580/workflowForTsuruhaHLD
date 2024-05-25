package eteam.gyoumu.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.EteamConst.UserId;
import eteam.common.select.BumonUserKanriCategoryExtLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;

public class UserExtLogic extends UserLogic{
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
		UserExt user = new UserExt();
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

		//カスタマイズここから
		//会社切替設定の取得
		List<GMap> lstKaishaKirikae = ((BumonUserKanriCategoryExtLogic)selectLogic).selectKaishaKirikaeSettei(userId);
		int lstCnt = lstKaishaKirikae.size();
		if (0 < lstCnt){
			user.kaishaKirikaeCd = new String[lstCnt];
			user.kaishaKirikaeName = new String[lstCnt];
			i = 0;
			for(Map<String, Object> aKaishaKirikae : lstKaishaKirikae) {
				user.kaishaKirikaeCd[i] = aKaishaKirikae.get("scheme_cd").toString();
				user.kaishaKirikaeName[i] = aKaishaKirikae.get("scheme_name").toString();
				i++;
			}
		}
		//カスタマイズここまで
		//承認ルート変更権限レベルの取得
		user.shouninRouteHenkouLevel = (String)userMap.get("shounin_route_henkou_level");

		//ADMINユーザーは、一般ユーザー禁止で強制業務ロール
		if (UserId.ADMIN.equals(userId)) {
			changeGyoumuRole(user, GyoumuRoleId.SYSTEM_KANRI);
			user.enableRoleSentaku = false;
		}
		return user;
	}

}
