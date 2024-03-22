package eteam.common.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門ロール選択ダイアログAction
 */
@Getter @Setter @ToString
public class BumonRoleSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** 部門ロール一覧 */
	List<GMap> list;
	
//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){

		//1.入力チェック

		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			//3.アクセス可能チェック

			//4.処理（初期表示）
			list = bukc.selectBumonRole();
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
}
