package eteam.common.kogamen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamEkispertCommonWeb;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 路線図ダイアログAction
 */
@Getter @Setter @ToString
public class RailMapSentakuAction extends EteamAbstractAction {

//＜定数＞
	/** 都道府県コード取得キー */
	public static final String TODOUHUKEN_CD = "todouhuken_cd";
//＜画面入力＞
	/** 都道府県コード */
	protected String todouhukenCd;
	/** 路線図ID */
	protected String id;
	/** 路線図地区 */
	protected String railMapStName;
	/** 基点駅名 */
	protected String stationName;
	/** 路線図表示フラグ */
	protected String imageFlg;
	/** 画像ファイル名フルパス(RailMapSub2.jspの項目) */
	protected String imageDir;
	/** x座標(RailMapSub2.jspの項目) */
	protected String x;
	/** y座標(RailMapSub2.jspの項目) */
	protected String y;
	/** 前回x座標 */
	protected String tempX;
	/** 前回y座標 */
	protected String tempY;
	/** 画像クリックx座標 */
	protected String clickX;
	/** 画像クリックy座標 */
	protected String clickY;
	/** 選択駅名１(RailMapSub1.jspの項目) */
	protected String selStName1;
	/** 選択駅名２(RailMapSub1.jspの項目) */
	protected String selStName2;
	/** 選択駅名３(RailMapSub1.jspの項目) */
	protected String selStName3;
	/** 選択駅名４(RailMapSub1.jspの項目) */
	protected String selStName4;
	/** 選択駅名５(RailMapSub1.jspの項目) */
	protected String selStName5;

	//＜画面入力以外＞
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(RailMapSentakuAction.class);
	/** 路線種別リスト */
	protected List<GMap> list;
	/** 地区リスト */
	protected List<GMap>todouhuken;
	
//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞

	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {

		try(EteamConnection connection = EteamConnection.connect()) {
			
			SystemKanriCategoryLogic syslc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			// 都道府県リストを取得する
			todouhuken = syslc.loadNaibuCdSetting(TODOUHUKEN_CD);
			
			//4.処理（初期表示）
			list = eecw.createRailMapList(todouhuken.get(0).getString("naibu_cd"));
	
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			imageFlg = "0";
			//5.戻り値を返す
			return "success";
		
		} catch (Exception e) {
			log.error(e);
			errorList.add("WEB APIでエラーが発生しました。");
			return "error";
			
	
		}
	}
	
	/**
	 * 地区選択時イベント
	 * @return 処理結果
	 */
	public String select() {

		try(EteamConnection connection = EteamConnection.connect()) {
			
			SystemKanriCategoryLogic syslc = EteamContainer.getComponent(SystemKanriCategoryLogic.class);
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class, connection);
			// 都道府県リストを取得する
			todouhuken = syslc.loadNaibuCdSetting(TODOUHUKEN_CD);
			
			//4.処理
			list = eecw.createRailMapList(todouhukenCd);
	
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			imageFlg = "0";
			//5.戻り値を返す
			return "success";
		
		} catch (Exception e) {
			log.error(e);
			errorList.add("WEB APIでエラーが発生しました。");
			return "error";
			
	
		}
	}
	
	/**
	 * 路線表示ボタンイベント
	 * @return 処理結果
	 */
	public String search() {
		
		try {
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			
			// 路線図駅情報を取得
			Map<String, String> stationInfoMap;
			
			// 基点駅名未入力時
			if (stationName.isEmpty()) {
				stationInfoMap = new HashMap<String, String>();
				
				x = "1";
				y = "1";
				stationInfoMap.put("x", x);
				stationInfoMap.put("y", y);
			} else {
				stationInfoMap = eecw.railMapStation(id, stationName);
				x = stationInfoMap.get("x");
				y = stationInfoMap.get("y");
	
			}
			
			imageDir =  "data:image/png;base64," + eecw.railMapImage(id, stationInfoMap);
			imageFlg = "1";
		
		} catch (Exception e) {
			log.error(e);
			errorList.add("WEB APIでエラーが発生しました。");
			return "error";
		}
		return "success";
	}
	
	/**
	 * 路線図移動ボタン押下時イベント
	 * @return 処理結果
	 */
	public String move() {
		
		try {
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("x", x);
			map.put("y", y);
			
			imageDir = "data:image/png;base64," + eecw.railMapImage(id, map);
			imageFlg = "1";
			
		} catch (Exception e) {
			
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("x", tempX);
				map.put("y", tempY);
	
				imageDir = "data:image/png;base64," + eecw.railMapImage(id, map);
				
				x = tempX;
				y = tempY;
				imageFlg = "1";
			} catch (Exception e2) {
				log.error(e2);
				errorList.add("WEB APIでエラーが発生しました。");
				return "error";
			}

		} 
		return "success";
	}
	
	/**
	 * 路線図駅名クリック時イベント
	 * @return 処理結果
	 */
	public String click() {

		try {
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			
			// 駅名を取得
			Map<String, String> clickMap = new HashMap<String, String>();
			clickMap.put("x", clickX);
			clickMap.put("y", clickY);
			
			String clickStName = eecw.railMapStationName(id, clickMap);
			
			// 未入力のテキストに設定する
			if (selStName1.isEmpty()) {
				selStName1 = clickStName;
			} else if (selStName2.isEmpty()) {
				selStName2 = clickStName;
			} else if (selStName3.isEmpty()) {
				selStName3 = clickStName;
			} else if (selStName4.isEmpty()){
				selStName4 = clickStName;
			} else {
				selStName5 = clickStName;
			}
	
			imageFlg = "1";
		
		} catch (Exception e2) {
			log.error(e2);
			errorList.add("WEB APIでエラーが発生しました。");
			return "error";
		}
		return "success";
	}
}
