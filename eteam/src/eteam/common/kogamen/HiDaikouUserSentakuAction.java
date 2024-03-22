package eteam.common.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 被代行者選択ダイアログAction
 */
@Getter @Setter @ToString
public class HiDaikouUserSentakuAction extends EteamAbstractAction {

	//＜画面入力＞
		/** ユーザー名 */
		String userName;
		/** 部門コード */
		String bumonCd;
		/** 部門名 */
		String bumonName;
		/** 部門ロールID */
		String bmnRoleId;
		/** 役割名 */
		String bmnRoleName;
		/** 業務ロールID */
		String gyoumuRoleId;
		/** 業務ロール名 */
		String gyoumuRoleName;

	//＜画面入力以外＞
		/** ユーザー一覧 */
		List<GMap> userList;
		

	//＜入力チェック＞
		@Override 
		protected void formatCheck() {
			checkString(userName,   1, 20, "ユーザー名", false);
			checkString(bumonCd,    1, 8 , "部門コード", false);
			checkString(bumonName,  1, 20, "部門名",     false);
			checkString(bmnRoleId,    1, 5,   "部門ロールID", false);
			checkString(bmnRoleName,  1, 20 , "役割名",         false);
			checkString(gyoumuRoleId,   1, 5,   "業務ロールID", false);
			checkString(gyoumuRoleName, 1, 20 , "業務ロール名", false);
		}
		@Override 
		protected void hissuCheck(int eventNum) {
		}

	//＜イベント＞
		/**
		 * 検索イベント
		 * @return ResultName
		 */
		public String kensaku(){
			//1.入力チェック
			formatCheck();
			if(0 < errorList.size()) {
				return "error";
			}
			
			try(EteamConnection connection = EteamConnection.connect()){
				BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
				
				//2.データ存在チェック
				
				//4.処理（初期表示）
				
				// ユーザー情報の全データを取得します。
				userList = bumonUserLogic.userSerach(replaceSpaceDelete(n2b(userName)), "", "", n2b(bumonCd), n2b(bmnRoleId), n2b(gyoumuRoleId), true, false, false, false);
				if(userList.isEmpty()){
					errorList.add("検索結果が0件でした。");
					return "error";
				}
				
				// 表示用にデータを変更します。
				for(GMap map : userList) {
					if (null != map.get("bumon_cd") && isNotEmpty(map.get("bumon_cd").toString())) {
						// 部門フル名の設定
						map.put("bumon_full_name", EteamCommon.connectBumonName(connection, map.get("bumon_cd").toString()));
					}
					
					// 日付を表示用に編集
					map.put("bumon_wariate_kigen_from", formatDate(map.get("bumon_wariate_kigen_from")));
					map.put("bumon_wariate_kigen_to", formatDate(map.get("bumon_wariate_kigen_to")));
					
				}
				
				//5.戻り値を返す
				return "success";
			}
		}
}
