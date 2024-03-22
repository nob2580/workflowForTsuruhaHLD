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
 * 新規起票画面Action
 */
@Getter @Setter @ToString
public class ShinkiKihyouAction extends EteamAbstractAction {

//＜定数＞
	
//＜画面入力＞
	/** 事象ID(ガイダンス起票から遷移の場合) */
	String jishouId;

//＜画面入力以外＞
	/** 伝票区分 */
	String denpyouKbn;
	/** 一覧 */
	List<GMap> list;

//＜入力チェック＞
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * 表示パターン：新規起票 or ガイダンス起票
	 * @return 処理結果
	 */
	public String init(){

		// 1.入力チェック
		//ユーザーによる手入力がないため、チェックなし

		try(EteamConnection connection = EteamConnection.connect()){
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

			// 2.データ存在チェック
			// 検索のためなし

			// 3.アクセス可能チェック なし

			// 4.処理 

			if(jishouId==null || jishouId.isEmpty()){ //事象ID指定なし
				list = lc.loadShinkiKihyouIchiran();
			}else{//事象ID指定あり
				list = lc.loadShinkiKihyouIchiran(jishouId);
			}
			
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			// 行No・業務種別tmpの初期化
			int gyouNo = 0;
			
			for (GMap map : list) {
				
				gyouNo = gyouNo + 1;
				map.put("gyouNo", gyouNo); // 行No
				map.put("gyoumuShubetsu", map.get("gyoumu_shubetsu")); // 業務種別(空白化はJSP側で制御)
				map.put("denpyouShubetsu", map.get("denpyou_shubetsu")); // 伝票種別
				map.put("naiyou", map.get("naiyou")); // 内容
				map.put("denpyouKbn", map.get("denpyou_kbn")); // 伝票区分
				map.put("version", map.get("version")); // バージョン
				map.put("denpyouShubetsuUrl", map.get("denpyou_shubetsu_url")); // 遷移先URL
			}

			//5.戻り値を返す
			return "success";
		}
	}

}
