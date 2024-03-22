package eteam.gyoumu.bumonrolekanri;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 役割追加画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonRoleTsuikaAction extends EteamAbstractAction {

//＜定数＞
	/** 部門ロールID */
	protected static final String BUMON_ROLE_ID = "bumon_role_id";
	
//＜画面入力＞
	/** 部門ロール名 */
	String bumonRoleName;

//＜画面入力以外＞

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(bumonRoleName,     1, 20,    "役割名",   false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{bumonRoleName ,"役割名"		,"0", "1", },
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
		//2.データ存在チェック
		//3.アクセス可能チェック
		//4.処理
		//5.戻り値を返す
		return "success";
	}
	
	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku() {
		//1.入力チェック
		hissuCheck(2);
		formatCheck();
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonRoleKanriLogic myLogic = EteamContainer.getComponent(BumonRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			
			// 部門ロールIDのシーケンス番号を取得する。
			int tempId = bumonUserLogic.selectSaibanKanri(BUMON_ROLE_ID);
			// ０埋めしStringへ変換する。
			String bumonRoleId = String.format("%1$05d", tempId);
			
			// 部門ロールへ登録する。
			myLogic.insert(bumonRoleId, bumonRoleName, getUser().getTourokuOrKoushinUserId());
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
}
