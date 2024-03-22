package eteam.gyoumu.userjouhoukanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー検索画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class UserKensakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** ユーザー名 */
	String userName;
	/** ユーザーID */
	String userId;
	/** 社員番号 */
	String shainNo;
	/** 部門コード */
	String bumonCd;
	/** 部門名 */
	String bumonName;
	/** 部門ロールID */
	String bumonRoleId;
	/** 役割名 */
	String bumonRoleName;
	/** 業務ロールID */
	String gyoumuRoleId;
	/** 業務ロール名 */
	String gyoumuRoleName;
	/** 代理起票可能フラグ **/
	String dairiKihyouFlag;
	/** アカウント一時ロック */
	String accountTmpLockFlag;
	/** アカウント永続ロック */
	String accountLockFlag;

//＜画面入力以外＞
	/** ユーザー一覧 */
	List<GMap> userList;
	
	/** ログインユーザー情報 */
	User loginUserInfo;
	
	/** アクセス権限 */
	String accessAuthority;

//＜入力チェック＞
	@Override 
	protected void formatCheck() {
		checkString(userName,       1, 20, "ユーザー名", false);
		checkString(userId,         1, 30, "ユーザーID", false);
		checkString(shainNo,        1, 15, "社員番号",   false);
		checkString(bumonCd,        1, 8 , "部門コード", false);
		checkString(bumonName,      1, 20, "部門名",     false);
		checkString(bumonRoleId,    1, 5,   "部門ロールID", false);
		checkString(bumonRoleName,  1, 20 , "役割名",         false);
		checkString(gyoumuRoleId,   1, 5,   "業務ロールID", false);
		checkString(gyoumuRoleName, 1, 20 , "業務ロール名", false);
		checkCheckbox(dairiKihyouFlag, "代理申請可能", false);
		checkCheckbox(accountTmpLockFlag, "アカウント一時ロック", false);
		checkCheckbox(accountLockFlag, "アカウント永続ロック", false);
		checkHankakuEiSuuUnderbar(userId,  "ユーザーID");
		checkHankakuEiSuu(shainNo, "社員番号");
	}
	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目				 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{userName,   "ユーザー名"	,"0", "0", },
				{userId,     "ユーザーID"	,"0", "0", },
				{shainNo,    "社員番号"		,"0", "0", },
				{bumonCd,    "部門コード"	,"0", "0", },
				{bumonName,  "部門名"		,"0", "0", },
				{bumonRoleId,    "部門ロールID"	,"0", "0", },
				{bumonRoleName,  "役割名"		,"0", "0", },
				{gyoumuRoleId,   "業務ロールID"	,"0", "0", },
				{gyoumuRoleName, "業務ロール名"	,"0", "0", },
			};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		//1.入力チェック
		hissuCheck(1);
		formatCheck();
		
		/* セッションからログインユーザー情報を取得します。 */
		loginUserInfo = getUser();
		
		
		try(EteamConnection connection = EteamConnection.connect()){
			//2.データ存在チェック
			//3.アクセス可能チェック（権限チェック）
			// 権限レベルを判定します。
			accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, loginUserInfo.getGyoumuRoleId(), "CO");
			if(accessAuthority.length() == 0) { // 一般ユーザーの場合
				throw new EteamAccessDeniedException(); // 不正アクセス
			}
			
			//4.処理（初期表示）
			//5.戻り値を返す
			return "success";
		}
	}
	
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
			userList = bumonUserLogic.userSerach(replaceSpaceDelete(n2b(userName)), replaceSpaceDelete(n2b(userId)), replaceSpaceDelete(n2b(shainNo)), n2b(bumonCd), n2b(bumonRoleId), n2b(gyoumuRoleId), false, isAccountTmpLock, isAccountLock, isDairiKihyouFlg);
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