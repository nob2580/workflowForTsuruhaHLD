package eteam.gyoumu.kihyounavi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.select.KihyouNaviCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ガイダンスメンテナンス（見出し設定）画面Action
 */
@Getter @Setter @ToString
public class GuidanceMaintenanceMidashiHensyuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 見出しID(表示キー) */
	String midashiId;
	/** 見出し名 */
	String midashiName;
	/** 事象ID */
	String[] jishouId;
	/** 事象名 */
	String[] jishouName;
	/** 削除対象 */
	String[] delChecked;
	
//＜画面入力以外＞
	
//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck(){
			checkNumber(midashiId, 1, 19, "見出しID", true);
			checkString(midashiName, 1, 20, "見出し名", false);
		if (jishouId != null)
		{
			for(int i = 0; i < jishouId.length ; i++) {
				checkNumber(jishouId[i], 1, 19, "事象ID：" + (i+1) + "行目", false);
			}
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list1 = {
			//項目						EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{midashiId,   "見出しID"	,"2", "2", },
			{midashiName, "見出し名"	,"0", "1", },
		};
		hissuCheckCommon(list1, eventNum);
	}
	

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		
		// 1.入力チェック
		formatCheck();
		hissuCheck(1);
		
		try(EteamConnection connection = EteamConnection.connect()){
			

			// 2.データ存在チェック
			
			//見出し + 見出しに紐付く事象取得。見出しがなければDataNotFound。
			this.displaySeigyo(connection, true);

			// 3.アクセス可能チェック なし
			// 4.処理 2で済んでる

			// 5. 戻り値を返す
			return "success";
		}
	}

	/**
	 * 更新イベント
	 * @return ResultName
	 */
	public String koushin() {
		try(EteamConnection connection = EteamConnection.connect()){
			GuidanceMaintenanceLogic lc2 = EteamContainer.getComponent(GuidanceMaintenanceLogic.class, connection);
			
			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(! errorList.isEmpty()) {
				this.displaySeigyo(connection, false);
				return "error";
			}

			// 2.データ存在チェック

			//見出し + 見出しに紐付く事象取得。見出しがなければDataNotFound。
			this.displaySeigyo(connection, false);

			// 3.アクセス可能チェック なし

			// 4.処理 

			//見出しを更新する。
			lc2.updateMidashi(Long.parseLong(midashiId), midashiName, getUser().getTourokuOrKoushinUserId());

			//チェックを付けた事象に関して、事象テーブル、事象伝票区分関連テーブルから事象に紐付くものを全部削除
			if (jishouId != null)
			{
				for (int i = 0;  i < jishouId.length; i++){
					if(delChecked[i].equals("1")){
						long tmpJishouId = toLong(jishouId[i]);
						lc2.deleteJishouDpkbnKanren(tmpJishouId);
						lc2.deleteJishou(tmpJishouId);
					}
				}
			}

			//表示順の更新 削除対象を飛ばして画面から送信された順番にする
			int hyoujiJun = 1;
			if (jishouId != null)
			{
				for (int i = 0; i < jishouId.length; i++){
					if(!delChecked[i].equals("1")){
						long tmpJishouId = toLong(jishouId[i]);
						lc2.updateJishouSort(tmpJishouId, hyoujiJun++, getUser().getTourokuOrKoushinUserId());
					}
				}
			}
			
			connection.commit();
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 表示情報の取得（初期表示＋エラー用）
	 * @param connection コネクション
	 * @param midashiNameRead 見出し名を読み込む（初期表示true）
	 * @Exception EteamDataNotFoundException 見出し/事象レコードがない場合
	 */
	protected void displaySeigyo(EteamConnection connection, boolean midashiNameRead) {
		KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		//見出しデータの取得
		GMap midashiMap = lc.findMidashi(Long.parseLong(midashiId));
		if (null == midashiMap) throw new EteamDataNotFoundException();
		if (midashiNameRead) {
			midashiName = (String)midashiMap.get("midashi_name");

			//事象データの取得
			List<GMap> list = lc.loadJishou(Long.parseLong(midashiId));
			jishouId = EteamCommon.mapList2Arr(list, "jishou_id");
			jishouName = EteamCommon.mapList2Arr(list, "jishou_name");
			delChecked = new String[list.size()];
			for(int i = 0; i < delChecked.length; i++) delChecked[i] = "0";
		}
	}
}
