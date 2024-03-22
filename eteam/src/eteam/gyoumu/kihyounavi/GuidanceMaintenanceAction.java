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
 * ガイダンスメンテナンス画面Action
 */
@Getter @Setter @ToString
public class GuidanceMaintenanceAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 見出しID（隠しキー） */
	String[] midashiId;
	/** 見出しID（削除対象） */
	String[] deleteMidashiId;

//＜画面入力以外＞
	/** 見出し一覧 */
	List<GMap> list;

	
//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck(){
		if (midashiId != null)
		{
			for(int i = 0; i < midashiId.length ; i++){
				checkNumber(midashiId[i], 1, 19, "見出しID：" + (i+1) + "行目", false);
			}
		}
		if (deleteMidashiId != null)
		{
			for(int i = 0; i < deleteMidashiId.length ; i++){
				checkNumber(deleteMidashiId[i], 1, 19, "（削除対象）見出しID：" + (i+1) + "行目", false);
			}
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

			// 1.入力チェック なし
			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理 
			list = lc.loadMidashi();
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 更新イベント
	 * @return ResultName
	 */
	public String koushin(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			GuidanceMaintenanceLogic lc2 = EteamContainer.getComponent(GuidanceMaintenanceLogic.class, connection);

			// 1.入力チェック
			formatCheck();
			if(! errorList.isEmpty()) {
				list = lc.loadMidashi();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理

			// 処理しやすいほうにListに
			List<Long> midashiIdList = toList(midashiId);
			List<Long> deleteMidashiIdList = toList(deleteMidashiId);

			//チェックを付けた見出に関して、見出テーブル、事象テーブル、事象伝票区分関連テーブルから事象に紐付くものを全部削除
			for (long tmpMidashiId : deleteMidashiIdList) {
				lc2.deleteJishouDpkbnKanrenByMidashi(tmpMidashiId);
				lc2.deleteJishouByMidashi(tmpMidashiId);
				lc2.deleteMidashi(tmpMidashiId);
			}

			//表示順の更新 削除対象を飛ばして画面から送信された順番にする
			int hyoujiJun = 1;
			for (long tmpMidashiId : midashiIdList) {
				if (! deleteMidashiIdList.contains(tmpMidashiId)) {
					lc2.updateMidashiSort(tmpMidashiId, hyoujiJun++, getUser().getTourokuOrKoushinUserId());
				}
			}
			connection.commit();
			
			// 5.戻り値を返す
			return "success";
		}
	}

	/**
	 * String[] から List<Long>へ
	 * @param arr String[]
	 * @return List<Long>
	 */
	protected List<Long> toList(String[] arr) {
		List<Long> ret = new ArrayList<>();
		if (null != arr) for (String s : arr) ret.add(Long.parseLong(s));
		return ret;
	}
}
