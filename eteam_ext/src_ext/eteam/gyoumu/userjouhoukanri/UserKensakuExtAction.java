package eteam.gyoumu.userjouhoukanri;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryExtLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;

public class UserKensakuExtAction extends UserKensakuAction {
	/**
	 * 検索イベント
	 * @return ResultName
	 */
	public String kensaku(){
		//1.入力チェック
		hissuCheck(2);
		formatCheck();
		if(0 < errorList.size()) {
			return "error";
		}
		
		// セッションからログインユーザー情報を取得します。
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			//3.アクセス可能チェック（権限チェック）
			// 権限レベルを判定します。
			accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, loginUserInfo.getGyoumuRoleId(), "CO");
			if(accessAuthority.length() == 0) {
				throw new EteamAccessDeniedException(); // 不正アクセス
			}
			
			//4.処理（初期表示）
			
			// ユーザー情報の全データを取得します。
			boolean isAccountTmpLock = "1".equals(accountTmpLockFlag);
			boolean isAccountLock = "1".equals(accountLockFlag);
			boolean isDairiKihyouFlg = "1".equals(dairiKihyouFlag);
			//カスタマイズ
			//userList = bumonUserLogic.userSerach(replaceSpaceDelete(n2b(userName)), replaceSpaceDelete(n2b(userId)), replaceSpaceDelete(n2b(shainNo)), n2b(bumonCd), n2b(bumonRoleId), n2b(gyoumuRoleId), false, isAccountTmpLock, isAccountLock, isDairiKihyouFlg);
			userList = ((BumonUserKanriCategoryExtLogic)bumonUserLogic).userSerach(replaceSpaceDelete(n2b(userName))
					,replaceSpaceDelete(n2b(userId))
					,replaceSpaceDelete(n2b(shainNo))
					,n2b(bumonCd)
					,n2b(bumonRoleId)
					,n2b(gyoumuRoleId)
					,false
					,isAccountTmpLock
					,isAccountLock
					,isDairiKihyouFlg
					,false);
if(userList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			// 表示用にデータを変更します。
			for(GMap map : userList) {
				if (null != map.get("bumon_cd") && isNotEmpty(map.get("bumon_cd").toString())) {
					// 部門フル名の設定 有効期限終了日時点の名称データを取得
					String bFull = EteamCommon.connectBumonName(connection, map.getString("bumon_cd"), toDate(formatDate(map.get("bumon_wariate_kigen_to"))) );
					if(bFull.length() == 0) {bFull = "(削除)";}
					map.put("bumon_full_name", bFull );
				}
				
				// 日付を表示用に編集
				map.put("bumon_wariate_kigen_from", formatDate(map.get("bumon_wariate_kigen_from")));
				map.put("bumon_wariate_kigen_to", formatDate(map.get("bumon_wariate_kigen_to")));
				
				// 背景色の設定
				map.put("bg_color", EteamCommon.bkColorSettei(map.get("bumon_wariate_kigen_from").toString(), map.get("bumon_wariate_kigen_to").toString()));
				if(map.get("bumon_full_name") == null || map.get("bumon_role_name") == null) {
					map.put("bg_color", "disabled-bgcolor");
				}

				// アカウント一時ロック日時の設定
				if ("1".equals(map.get("tmp_lock_flg"))) {
					map.put("tmpLockTime", formatTimeSS(map.get("tmp_lock_time")));
				} else {
					map.put("tmpLockTime", "-");
				}
				// アカウント永続ロック日時の設定
				if ("1".equals(map.get("lock_flg"))) {
					map.put("lockTime", formatTimeSS(map.get("lock_time")));
				} else {
					map.put("lockTime", "-");
				}
				
				// 代理起票可能
				if ("1".equals(map.get("dairikihyou_flg"))) {
					map.put("dairiKihyou", "1");
				} else {
					map.put("dairiKihyou", "0");
				}
				
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
}