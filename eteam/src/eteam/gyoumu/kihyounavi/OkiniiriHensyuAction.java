package eteam.gyoumu.kihyounavi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.KihyouNaviCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * お気に入り編集画面Action
 */
@Getter @Setter @ToString
public class OkiniiriHensyuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票区分 */
	String[] denpyouKbn;
	/** 業務種別 */
	String[] gyoumuShubetsu;
	/** 伝票種別 */
	String[] denpyouShubetsu;
	/** メモ */
	String[] memo;
	/** 背景色 */
	String[] bgColor;
	
//＜部品＞
	
//＜入力チェック＞
	@Override
	protected void formatCheck(){
		if (denpyouKbn != null)
		{
			for(int i = 0; i < denpyouKbn.length ; i++) {
				int ip = i + 1;
				checkString(denpyouKbn[i] , 1, 4, "伝票区分：" + ip + "行目", true);
				checkString(gyoumuShubetsu[i] , 1, 20, "業務種別：" + ip + "行目", false);
				checkString(denpyouShubetsu[i] , 1, 20, "伝票種別：" + ip + "行目", false);
				checkString(memo[i] , 1, 160, "メモ：" + ip + "行目", false);
			}
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		if (denpyouKbn != null)
		{
			for(int i = 0; i < denpyouKbn.length ; i++){
				int ip = i + 1;
				String[][] list1 = {
					//項目												EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{denpyouKbn[i] ,"伝票区分：" + ip + "行目"		,"0", "2", },
					{denpyouShubetsu[i] ,"伝票種別：" + ip + "行目"		,"0", "1", },
					{gyoumuShubetsu[i] ,"業務種別：" + ip + "行目"		,"0", "1", },
					{memo[i] ,"メモ：" + ip + "行目"			,"0", "1", },
				};
				hissuCheckCommon(list1, eventNum);
			}
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			KihyouNaviCategoryLogic lc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理 

			//ユーザーのブックマーデータを取得
			List<GMap> list = lc.loadOkiniiri(getUser().getTourokuOrKoushinUserId());
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			// 行番号、伝票種別レコードの初期化
			denpyouKbn = new String[list.size()];
			gyoumuShubetsu = new String[list.size()];
			denpyouShubetsu = new String[list.size()];
			memo = new String[list.size()];
			bgColor = new String[list.size()];
			for (int i = 0; i < denpyouKbn.length; i++) {
				GMap map = list.get(i);
				denpyouKbn[i] = (String)map.get("denpyou_kbn");
				gyoumuShubetsu[i] = (String)map.get("gyoumu_shubetsu");
				denpyouShubetsu[i] = (String)map.get("denpyou_shubetsu");
				memo[i] = (String)map.get("memo");
				String wkYuukouKigenFrom = formatDate(map.get("yuukou_kigen_from"));
				String wkYuukouKigenTo = formatDate(map.get("yuukou_kigen_to"));
				bgColor[i] = EteamCommon.bkColorSettei(wkYuukouKigenFrom,wkYuukouKigenTo); 
			}
			
			//5.戻り値を返す
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

			//ユーザのブックマーデータを全削除
			lc.deleteOkiniiri(getUser().getTourokuOrKoushinUserId());

			//画面入力されたレコードを追加する。
			if (denpyouKbn != null)
			{
				for(int i = 0; i < denpyouKbn.length ; i++){
					lc.insertOkiniiri(getUser().getTourokuOrKoushinUserId(), denpyouKbn[i], i + 1, memo[i], getUser().getTourokuOrKoushinUserId());
				}
			}
			
			connection.commit();
			
			// 戻り値を返す
			return "success";

		}
	}
}
