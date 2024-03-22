package eteam.gyoumu.kihyounavi;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ガイダンスメンテナンス（見出し追加）画面Action
 */
@Getter @Setter @ToString
public class GuidanceMaintenanceMidashiTsuikaAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 見出し名 */
	String midashiName;
	
//＜画面入力以外＞
	//登録後のリダイレクトパラメータ
	/** 見出しID */
	String midashiId;

//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck(){
		checkString(midashiName, 1, 20, "見出し名", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list1 = {
			//項目						EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
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
		try(EteamConnection connection = EteamConnection.connect()){
			// 1.入力チェック なし
			// 2.データ存在チェック
			// 3.アクセス可能チェック なし
			// 4.処理 なし
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){
		
		// 1.入力チェック
		formatCheck();
		hissuCheck(2);
		if (! errorList.isEmpty())
		{
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			GuidanceMaintenanceLogic lc = EteamContainer.getComponent(GuidanceMaintenanceLogic.class, connection);

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし
			// 4.処理 

			// データ登録処理
			midashiId = Long.toString(lc.insertMidashi(midashiName, getUser().getTourokuOrKoushinUserId()));

			connection.commit();
			
			// 5.戻り値を返す
			return "success";
		}
	}
}