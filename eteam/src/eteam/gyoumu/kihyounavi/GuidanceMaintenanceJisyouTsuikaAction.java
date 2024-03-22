package eteam.gyoumu.kihyounavi;

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
 * ガイダンスメンテナンス（事象追加）画面Action
 */
@Getter @Setter @ToString
public class GuidanceMaintenanceJisyouTsuikaAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 見出しID */
	String midashiId;
	/** 事象名 */
	String jishouName;

//＜画面入力以外＞
	//表示
	/** 見出し名 */
	String midashiName;
	//リダイレクト用
	/** 事象ID */
	String jishouId;
	
//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck(){
		checkNumber(midashiId, 1, 19, "見出しID", true);
		checkString(jishouName, 1, 20, "事象名", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list1 = {
			//項目						EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{midashiId , "見出しID"	,"2", "2", },
			{jishouName , "事象名"		,"0", "1", },
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

			// 2.データ存在チェック

			//見出し取得。見出しがなければDataNotFound。
			displaySeigyo(connection);

			// 3.アクセス可能チェック なし

			// 4.処理 2で済んでいる
			
			// 5.戻り値を返す
			return "success";
			
		}
	}

	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){
		try(EteamConnection connection = EteamConnection.connect()){
			GuidanceMaintenanceLogic lc = EteamContainer.getComponent(GuidanceMaintenanceLogic.class, connection);

			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(! errorList.isEmpty()){
				displaySeigyo(connection);
				return "error";
			}

			// 2.データ存在チェック

			//見出し取得。見出しがなければDataNotFound。
			displaySeigyo(connection);

			// 3.アクセス可能チェック なし

			// 4.処理
			
			// データ登録
			jishouId = Long.toString(lc.insertJishou(Long.parseLong(midashiId), jishouName, getUser().getTourokuOrKoushinUserId()));
			
			connection.commit();
			
			// 5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 表示情報の取得（初期表示＋エラー用）
	 * @param connection コネクション
	 * @Exception EteamDataNotFoundException 見出し/事象レコードがない場合
	 */
	protected void displaySeigyo(EteamConnection connection) {
		KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

		//見出しデータの取得
		GMap midashiMap = lc.findMidashi(Long.parseLong(midashiId));
		if (null == midashiMap) throw new EteamDataNotFoundException();
		midashiName = (String)midashiMap.get("midashi_name");
	}
}
