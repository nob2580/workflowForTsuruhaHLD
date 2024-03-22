package eteam.gyoumu.gyoumurolekanri;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 業務ロール追加画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class GyoumuRoleTsuikaAction extends EteamAbstractAction {

//＜定数＞
	/** 業務ロールID */
	protected static final String GYOUMU_ROLE_ID = "gyoumu_role_id";

//＜画面入力＞
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
		checkString(gyoumuRoleName,  1, 20, "業務ロール名",   false);
		checkCheckbox(workflow,             "ワークフロー",   false);
		checkCheckbox(kaishaSettei,         "会社設定",       false);
		checkCheckbox(keiriShori,           "経理処理",       false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{gyoumuRoleName ,"業務ロール名"		,"0", "1", },
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
		formatCheck();
		hissuCheck(2);
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			GyoumuRoleKanriLogic myLogic = EteamContainer.getComponent(GyoumuRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			
			// チェックボックスが入っていない場合エラーとする。
			if (workflow == null && kaishaSettei == null && keiriShori == null) {
				errorList.add("可能操作のいずれかにチェックを入れて下さい。");
				return "error";
			}
			
			// ログインユーザーIDを取得する。
			String loginUserId = getUser().getTourokuOrKoushinUserId();
			// 業務ロールIDのシーケンス番号を取得する。
			int tempId = bumonUserLogic.selectSaibanKanri(GYOUMU_ROLE_ID);
			// ０埋めしStringへ変換する。
			String gyoumuRoleId = String.format("%1$05d", tempId);
			
			// 業務ロールへ登録する。
			myLogic.insertGyoumuRole(gyoumuRoleId, gyoumuRoleName, loginUserId);
			
			// 業務ロール機能制御へ登録する。
			myLogic.insertGyoumuRoleKinouSeigyo(gyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.WORKFLOW,      workflow     == null ? "0" : workflow,     loginUserId);
			myLogic.insertGyoumuRoleKinouSeigyo(gyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.KAISHA_SETTEI, kaishaSettei == null ? "0" : kaishaSettei, loginUserId);
			myLogic.insertGyoumuRoleKinouSeigyo(gyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI,   keiriShori   == null ? "0" : keiriShori,   loginUserId);
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
}
