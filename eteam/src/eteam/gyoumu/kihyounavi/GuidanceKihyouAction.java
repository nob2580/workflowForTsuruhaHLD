	package eteam.gyoumu.kihyounavi;

import java.util.ArrayList;
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
 * ガイダンス起票画面Action
 */
@Getter @Setter @ToString
public class GuidanceKihyouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** ガイダンスデータリスト */
	List<MidashiData> midashiList = new ArrayList<MidashiData>();

	/** 見出しデータクラス */
	@Getter @Setter @ToString
	public class MidashiData{
		/** 見出し名 */
		String midashiName;
		/** 見出しID */
		String midashiId;
		/** 事象リスト */
		ArrayList<JishouData> jishouList;
	}

	/** 事象データクラス */
	@Getter @Setter @ToString
	public class JishouData{
		/** 事象名 */
		String jishouName;
		/** 事象ID */
		String jishouId;
	}
	
//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

			// 1.入力チェック なし
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理 

			//ガイダンス起票データを取得する。
			 List<GMap> list = lc.loadGuidanceKihyouIchiran();
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//表示用データを作成する。
			MidashiData midashiData = null;
			String lastMidashiId = null;
			for (GMap map : list) {

				//事象データを生成する。
				JishouData jishouData = new JishouData();
				jishouData.setJishouId(map.getString("jishou_id"));
				jishouData.setJishouName((String)map.get("jishou_name"));

				//見出しIDが変わった場合は生成する。
				if (! map.getString("midashi_id").equals(lastMidashiId)) {
					midashiData = new MidashiData(); 
					midashiData.setMidashiId(map.getString("midashi_id"));
					midashiData.setMidashiName((String)(map.get("midashi_name")));
					midashiData.setJishouList(new ArrayList<JishouData>());
					lastMidashiId = map.getString("midashi_id");
					midashiList.add(midashiData);
				}
				midashiData.getJishouList().add(jishouData);
			}
			
			//5.戻り値を返す
			return "success";
			
		}
	}

}
