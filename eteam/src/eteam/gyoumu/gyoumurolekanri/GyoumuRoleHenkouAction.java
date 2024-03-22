package eteam.gyoumu.gyoumurolekanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 業務ロール変更画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class GyoumuRoleHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 業務ロールID */
	String gyoumuRoleId;
	/** 業務ロール名 */
	String gyoumuRoleName;
	/** ワークフロー */
	String workflow;
	/** 会社設定 */
	String kaishaSettei;
	/** 経理処理 */
	String keiriShori;

//＜画面入力以外＞

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(gyoumuRoleId,    5, 5,  "業務ロールID",   true); // KEY
		checkString(gyoumuRoleName,  1, 20, "業務ロール名",   false);
		checkCheckbox(workflow,             "ワークフロー",   false);
		checkCheckbox(kaishaSettei,         "会社設定",       false);
		checkCheckbox(keiriShori,           "経理処理",       false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{gyoumuRoleId ,"業務ロールID"		,"2", "2", "2", }, // KEY
				{gyoumuRoleName ,"業務ロール名"		,"0", "1", "0", },
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
			
			// 業務ロールに存在するかチェックします。
			GMap gyoumuRoleMap = bumonUserLogic.selectGyoumuRole(gyoumuRoleId);
			if(gyoumuRoleMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			// 業務ロールIDがシステム管理の場合、アクセスエラーとする
			if (gyoumuRoleId.matches("^[0]+$")) {
				throw new EteamAccessDeniedException(); // アクセスエラー
			}
			
			//4.処理
			// 表示データ取得
			gyoumuRoleName  = gyoumuRoleMap.get("gyoumu_role_name").toString();
			
			// 業務ロール機能制御から機能制御区分を取得します。
			List<GMap> kinouSeigyoList = bumonUserLogic.selectGyoumuRoleKinouSeigyoKbn(gyoumuRoleId);
			for(GMap map : kinouSeigyoList) {
				switch (map.get("gyoumu_role_kinou_seigyo_cd").toString()) {
				case GYOUMU_ROLE_KINOU_SEIGYO_CD.WORKFLOW:
					workflow = map.get("gyoumu_role_kinou_seigyo_kbn").toString();
					break;
				case GYOUMU_ROLE_KINOU_SEIGYO_CD.KAISHA_SETTEI:
					kaishaSettei = map.get("gyoumu_role_kinou_seigyo_kbn").toString();
					break;
				case GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI:
					keiriShori = map.get("gyoumu_role_kinou_seigyo_kbn").toString();
					break;
				}
			}
			
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
		
		try(EteamConnection connection = EteamConnection.connect()){
			GyoumuRoleKanriLogic myLogic = EteamContainer.getComponent(GyoumuRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			// 業務ロールに存在するかチェックします。
			GMap gyoumuRoleMap = bumonUserLogic.selectGyoumuRole(gyoumuRoleId);
			if(gyoumuRoleMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			// 業務ロールIDがシステム管理の場合、アクセスエラーとする
			if (gyoumuRoleId.matches("^[0]+$")) {
				throw new EteamAccessDeniedException(); // アクセスエラー
			}
			
			//4.処理
			
			// チェックボックスが入っていない場合エラーとする。
			if (workflow == null && kaishaSettei == null && keiriShori == null) {
				errorList.add("可能操作のいずれかにチェックを入れて下さい。");
				return "error";
			}
			
			// ログインユーザーIDを取得する。
			String loginUserId = getUser().getTourokuOrKoushinUserId();
			
			// 業務ロールを更新します。
			myLogic.updateGyoumuRole(gyoumuRoleId, gyoumuRoleName, loginUserId);
			
			// 業務ロール機能制御を更新します。
			myLogic.updateGyoumuRoleKinouSeigyo(gyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.WORKFLOW,      workflow     == null ? "0" : workflow,     loginUserId);
			myLogic.updateGyoumuRoleKinouSeigyo(gyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.KAISHA_SETTEI, kaishaSettei == null ? "0" : kaishaSettei, loginUserId);
			myLogic.updateGyoumuRoleKinouSeigyo(gyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI,   keiriShori   == null ? "0" : keiriShori,   loginUserId);
			
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
			GyoumuRoleKanriLogic myLogic = EteamContainer.getComponent(GyoumuRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			// 業務ロールに存在するかチェックします。
			GMap gyoumuRoleMap = bumonUserLogic.selectGyoumuRole(gyoumuRoleId);
			if(gyoumuRoleMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			// 業務ロールIDがシステム管理の場合、アクセスエラーとする
			if (gyoumuRoleId.matches("^[0]+$")) {
				throw new EteamAccessDeniedException(); // アクセスエラー
			}
			
			//4.処理
			
			// 業務ロールと業務ロール機能制御を削除します。
			myLogic.delete(gyoumuRoleId);
			myLogic.deleteKinouSeigyo(gyoumuRoleId);
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
}
