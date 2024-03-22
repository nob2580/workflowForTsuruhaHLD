package eteam.gyoumu.bumonrolekanri;

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
 * 役割一覧画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonRoleIchiranAction extends EteamAbstractAction {

//＜画面入力＞
	/** 部門ロールID */
	String[] bumonRoleId;

//＜画面入力以外＞
	/** 部門ロール一覧 */
	List<GMap> bumonRoleList;

//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

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
			bumonRoleList = bumonUserLogic.selectBumonRole();
			if(bumonRoleList.isEmpty()){
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
		try(EteamConnection connection = EteamConnection.connect()){
			BumonRoleKanriLogic myLogic = EteamContainer.getComponent(BumonRoleKanriLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			if(bumonRoleId !=null)for(int i=0; i<bumonRoleId.length; i++){
			
				//2.データ存在チェック
				//業務ロールIDをキーに業務ロールからデータを取得する。
				GMap bumonRoleMap = bumonUserLogic.selectBumonRole(bumonRoleId[i]);
				if (bumonRoleMap == null) {
					throw new EteamDataNotFoundException();
					}
			
				//3.アクセス可能チェック
				//4.処理（初期表示）
				
				myLogic.updateHyoujiJunActive(bumonRoleId[i], i+1, getUser().getTourokuOrKoushinUserId());
				connection.commit();
			
			
				}
			//5.戻り値を返す
			return "success";
		}
	}
}
	