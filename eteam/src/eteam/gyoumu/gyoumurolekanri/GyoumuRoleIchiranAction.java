package eteam.gyoumu.gyoumurolekanri;

import java.util.List;

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
 * 業務ロール一覧画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class GyoumuRoleIchiranAction extends EteamAbstractAction {


//＜画面入力＞
	/** 業務ロールID */
	String[] gyoumuRoleId;
	
//＜画面入力以外＞
	/** 部門ロール一覧 */
	List<GMap> gyoumuRoleList;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		if (gyoumuRoleId != null)
			for (int i = 0; i < gyoumuRoleId.length; i++) {
				checkString(gyoumuRoleId[i],       1, 5,     "業務ロールID",   true); // KEY
		}
		}

	@Override
	protected void hissuCheck(int eventNum) {
		if (gyoumuRoleId != null)
			for (int i = 0; i < gyoumuRoleId.length; i++) {

				String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{gyoumuRoleId[i] ,"業務ロールID"		,"0", "2", }, // KEY
				};
				hissuCheckCommon(list, eventNum);
		}
		}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		//1.入力チェック
		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理（初期表示）
			// 業務ロールの全データを取得します。
			gyoumuRoleList = bumonUserLogic.selectGyoumuRole();
			if(gyoumuRoleList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 表示順更新イベント
	 * @return ResultName
	 */
	public String hyoujijunKoushin(){
		//1.入力チェック
		try(EteamConnection connection = EteamConnection.connect();){
			GyoumuRoleKanriLogic myLogic = EteamContainer.getComponent(GyoumuRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			if (gyoumuRoleId != null)for (int i = 0; i < gyoumuRoleId.length; i++) {
	
				//2.データ存在チェック
				//業務ロールIDをキーに業務ロールからデータを取得する。
				GMap gyoumuRoleMap = bumonUserLogic.selectGyoumuRole(gyoumuRoleId[i]);
				if (gyoumuRoleMap == null) {
					throw new EteamDataNotFoundException();
				}
				
				//3.アクセス可能チェック
				//4.処理（初期表示）
	
				myLogic.updateHyoujiJunActive(gyoumuRoleId[i], i+1, getUser().getTourokuOrKoushinUserId());
				connection.commit();
				}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
}
