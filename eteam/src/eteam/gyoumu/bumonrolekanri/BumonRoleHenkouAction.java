package eteam.gyoumu.bumonrolekanri;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 役割変更画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonRoleHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 部門ロール名 */
	String bumonRoleName;

//＜画面入力以外＞
	/** 部門ロールID */
	String bumonRoleId;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(bumonRoleId,       5, 5,     "役割ID",   true); // KEY
		checkString(bumonRoleName,     1, 20,    "役割名",   false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{bumonRoleId ,"役割ID"		,"2", "2", "2", }, // KEY
				{bumonRoleName ,"役割名"		,"0", "1", "0", },
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
		formatCheck();
		hissuCheck(1);
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			
			// 部門ロールに存在するかチェックします。
			GMap bumonRoleMap = bumonUserLogic.selectBumonRole(bumonRoleId);
			if(bumonRoleMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			//4.処理
			// 表示データ取得
			bumonRoleName  = bumonRoleMap.get("bumon_role_name").toString();
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 変更イベント
	 * @return ResultName
	 */
	public String henkou() {
		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect();){
			BumonRoleKanriLogic myLogic = EteamContainer.getComponent(BumonRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			// 部門ロールに存在するかチェックします。
			GMap bumonRoleMap = bumonUserLogic.selectBumonRole(bumonRoleId);
			if(bumonRoleMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			//4.処理
			
			// 部門ロールを更新します。
			myLogic.update(bumonRoleId, bumonRoleName, getUser().getTourokuOrKoushinUserId());
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	
	/**
	 * 削除イベント
	 * @return ResultName
	 */
	public String sakujo() {
		//1.入力チェック
		hissuCheck(3);
		formatCheck();
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonRoleKanriLogic myLogic = EteamContainer.getComponent(BumonRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			// 部門ロールに存在するかチェックします。
			GMap bumonRoleMap = bumonUserLogic.selectBumonRole(bumonRoleId);
			if(bumonRoleMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			//4.処理
			
			// 部門ロールを削除します。
			myLogic.delete(bumonRoleId);
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
}
