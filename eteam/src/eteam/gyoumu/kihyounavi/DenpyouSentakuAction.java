package eteam.gyoumu.kihyounavi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KihyouNaviCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門検索ダイアログAction
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class DenpyouSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** 一覧 */
	List<GMap> list;
	
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
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

			// 2.データ存在チェック
			// 検索のためなし

			// 3.アクセス可能チェック なし

			// 4.処理 
			list = lc.loadShinkiKihyouIchiran();
			
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			for (GMap map : list) {
				map.put("gyoumuShubetsu", map.get("gyoumu_shubetsu")); // 業務種別
				map.put("denpyouShubetsu", map.get("denpyou_shubetsu"));// 伝票種別
				map.put("setsumei", map.get("naiyou")); // 内容
				map.put("denpyouKbn", map.get("denpyou_kbn")); // 伝票区分
			}

			//5.戻り値を返す
			return "success";
		}
	}
}
