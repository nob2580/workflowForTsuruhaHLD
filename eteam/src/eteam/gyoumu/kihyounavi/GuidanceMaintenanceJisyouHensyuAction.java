package eteam.gyoumu.kihyounavi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.select.KihyouNaviCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ガイダンスメンテナンス（事象設定）画面Action
 */
@Getter @Setter @ToString
public class GuidanceMaintenanceJisyouHensyuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 見出しID */
	String midashiId;
	/** 事象ID */
	String jishouId;
	/** 伝票区分 */
	String[] denpyouKbn;
	/** 伝票名 */
	String[] denpyouName;

//＜画面入力以外＞
	/** 見出し名 */
	String midashiName;
	/** 事象名 */
	String jishouName;
	
//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck(){
			checkNumber(midashiId, 1, 19, "見出しID", true);
			checkNumber(jishouId, 1, 19, "事象ID", true);
			checkString(jishouName, 1, 20, "事象名", false);
		if (denpyouKbn != null)
		{
			for(int i = 0; i < denpyouKbn.length ; i++){
				checkString(denpyouKbn[i], 1, 19, "伝票区分：" + (i + 1) + "行目", false);
			}
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list1 = {
				{midashiId, "見出しID", "2", "2", },
				{jishouId, "事象ID", "2", "2", },
				{jishouName, "事象名", "0", "1", },
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
		if (! errorList.isEmpty())
		{
			return "error";
		}

		try(EteamConnection connection = EteamConnection.connect()){
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

			// 2.データ存在チェック
			
			//見出し＋事象取得。なければDataNotFound。
			this.displaySeigyo(connection, true);

			// 3.アクセス可能チェック なし

			// 4.処理 なし
			
			//伝票情報の取得
			List<GMap>list = lc.loadDenpyouInfo(Long.parseLong(jishouId));
			denpyouKbn = new String[list.size()];
			denpyouName = new String[list.size()];
			for (int i = 0; i < denpyouKbn.length; i++) {
				GMap record = list.get(i);
				denpyouKbn[i] = (String)record.get("denpyou_kbn");
				denpyouName[i] = (String)record.get("denpyou_shubetsu");
			}
			
			// 5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 更新イベント
	 * @return ResultName
	 */
	public String koushin(){
		try(EteamConnection connection = EteamConnection.connect()){
			GuidanceMaintenanceLogic lc = EteamContainer.getComponent(GuidanceMaintenanceLogic.class, connection);

			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(! errorList.isEmpty()) {
				displaySeigyo(connection, false);
				return "error";
			}
			
			// 2.データ存在チェック
			
			//見出し＋事象取得。なければDataNotFound。
			this.displaySeigyo(connection, false);

			// 3.アクセス可能チェック なし

			// 4.処理 

			//事象を更新する。
			lc.updateJishou(Long.parseLong(jishouId), jishouName, getUser().getTourokuOrKoushinUserId());

			//一旦該当事象に紐付く事象伝票関連をALL-DELETEしてから送信された伝票区分についてINSERT
			lc.deleteJishouDpkbnKanren(toLong(jishouId));
			int hyoujiJun = 1;
			if (null != denpyouKbn) for (String tmpDenpyouKbn : denpyouKbn) {
				lc.insertJishouDpkbnKanren(toLong(jishouId), tmpDenpyouKbn, hyoujiJun++, getUser().getTourokuOrKoushinUserId());
			}
			
			connection.commit();
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 表示情報の取得（初期表示＋エラー用）
	 * @param connection コネクション
	 * @param jishouNameRead 事象名を読み込む（初期表示true）
	 * @Exception EteamDataNotFoundException 見出し/事象レコードがない場合
	 */
	protected void displaySeigyo(EteamConnection connection, boolean jishouNameRead) {
		KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

		//見出しデータの取得
		GMap midashiMap = lc.findMidashi(Long.parseLong(midashiId));
		if (null == midashiMap) throw new EteamDataNotFoundException();
		midashiName = (String)midashiMap.get("midashi_name"); // 見出し名

		//事象データの取得
		GMap jishouMap = lc.findJishou(Long.parseLong(jishouId));
		if (null == jishouMap) throw new EteamDataNotFoundException();
		if (jishouNameRead) jishouName = (String)jishouMap.get("jishou_name");
	}
}
