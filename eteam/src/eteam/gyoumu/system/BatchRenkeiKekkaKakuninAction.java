package eteam.gyoumu.system;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バッチ連携結果確認画面Action
 */
@Getter @Setter @ToString
public class BatchRenkeiKekkaKakuninAction extends EteamAbstractAction {

//＜定数＞
	/** ソートアイテム */
	static final String[] SOURT_ITEM_DOMAIN = {"start_time", "batch_name", "batch_status"};
	/** ソート順 */
	static final String[] SOURT_ORDER_DOMAIN = {"ASC", "DESC"};

	/** プルダウン用コードキー */
	static final String NAIBU_CODE_KEY = "naibu_cd";
	/** プルダウン用表示名キー */
	static final String NAIBU_NAME_KEY = "name";
	
//＜画面入力＞
	/** 開始日付 */
	String searchKaishiHiduke;
	/** 開始時 */
	String searchKaishiHour;
	/** 開始分 */
	String searchKaishiMin;
	/** 終了日付 */
	String searchSyuuryouHiduke;
	/** 終了時 */
	String searchSyuuryouHour;
	/** 終了分 */
	String searchSyuuryouMin;
	/** バッチ名 */
	String searchBatchName;
	/** ステータス */
	String searchStatus;

//＜非表示項目＞
	/** ソート項目 */
	String sortItem;
	/** ソート順 */
	String sortOrder;
	/** ページ番号 */
	String pageNo;

//＜画面入力以外＞
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** ページング処理 link押下時のURL */
	String pagingLink;

	/** バッチ連携結果一覧 */
	List<GMap> kekkaList;
	/** 開始、終了時のDropDownList */
	List<GMap> hourList;
	/** 開始、終了分のDropDownList */
	List<GMap> minList;
	/** バッチ名のDropDownList */
	List<GMap> batchNameList;
	/** ステータスのDropDownList */
	List<GMap> statusList;

//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** システム管理　SELECT */
	SystemKanriCategoryLogic systemLogic;

//＜入力チェック＞
	@Override 
	protected void formatCheck() {
		checkDate(searchKaishiHiduke, "開始日付", false);
		checkNumberRange(searchKaishiHour, 0, 23, "開始時",  			false);
		checkNumberRange(searchKaishiMin, 0, 59, "開始分",  			false);
		checkDate(searchSyuuryouHiduke, "終了日付", false);
		checkNumberRange(searchKaishiHour, 0, 23, "終了時",  			false);
		checkNumberRange(searchKaishiMin, 0, 59, "終了分",  			false);
		checkNumber(pageNo, 1, 10, "ページ番号", false);
		checkDomain(sortItem,  SOURT_ITEM_DOMAIN, "ソートアイテム", false);
		checkDomain(sortOrder, SOURT_ORDER_DOMAIN, "ソート順", false);
	}

	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{searchKaishiHiduke, "開始日付", "0", "1" },
			{searchKaishiHour, "開始時", "0", "1" },
			{searchKaishiMin, "開始分", "0", "1" },
			{searchSyuuryouHiduke, "終了日付", "0", "1" },
			{searchSyuuryouHour, "終了時", "0", "1" },
			{searchSyuuryouMin, "終了分", "0", "1" },
			{pageNo, "ページ番号", "0", "2" },
			{sortItem, "ソート項目", "0", "2" },
			{sortOrder, "ソート順", "0", "2" },
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		// デフォルト値セット
		Date nowDate = new Date(System.currentTimeMillis());
		String strNowDate = formatDate(nowDate);
		searchKaishiHiduke = strNowDate;
		searchKaishiHour = "00";
		searchKaishiMin = "00";
		searchSyuuryouHiduke = strNowDate;
		searchSyuuryouHour = "23";
		searchSyuuryouMin = "59";
		searchBatchName = "";
		searchStatus = "";
		sortItem = "start_time";
		sortOrder = "DESC";
		pageNo = "1";
		return kensaku();
	}

	/**
	 * 検索イベント
	 * 検索結果の表示/各種項目でのソート
	 * @return 処理結果
	 */
	public String kensaku(){
		connection = EteamConnection.connect();
		try{
			initParts();
			displaySeigyo();

			//1.入力チェック
			formatCheck();
			hissuCheck(2);
			if (0 < errorList.size())
			{
				return "error";
			}
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理

			//入力→Time型変換
			Timestamp startTime = strToDate(searchKaishiHiduke + " " + searchKaishiHour + ":" + searchKaishiMin + ":00");
			Timestamp endTime = strToDate(searchSyuuryouHiduke + " " + searchSyuuryouHour + ":" + searchSyuuryouMin + ":59");

			// 件数取得
			totalCount = systemLogic.findBatchInfoCount(startTime, endTime, searchBatchName, searchStatus);
			
			// 設定情報の取得（１ページ表示件数）
			String settingVal = setting.recordNumPerPage();
			int pagemax = Integer.parseInt(settingVal);
			
			// 総ページ数の計算
			totalPage = EteamCommon.calcTotalPageNum(pagemax, totalCount);
			if (totalPage == 0) {
				totalPage = 1;
			}
			
			// 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
			if(isEmpty(pageNo) || "0".equals(pageNo)){
				pageNo = "1";
			} else if(Integer.parseInt(pageNo) > totalPage) {
				pageNo = String.valueOf(totalPage);
			}
			
			// ページングリンクURLを設定
			pagingLink = "batch_renkei_kekka_kakunin_kensaku?searchKaishiHiduke=" + searchKaishiHiduke + "&searchKaishiHour=" + searchKaishiHour 
					+ "&searchKaishiMin=" + searchKaishiMin  + "&searchSyuuryouHiduke=" + searchSyuuryouHiduke  
					+ "&searchSyuuryouHour=" + searchSyuuryouHour + "&searchSyuuryouMin=" + searchSyuuryouMin
					+ "&searchBatchName=" + searchBatchName + "&searchStatus=" + searchStatus + "&sortItem=" + sortItem 
					+ "&sortOrder=" + sortOrder + "&";

			// バッチ連携情報一覧の取得。0件ならエラー
			kekkaList = systemLogic.loadBatchRenkeiKekkaList(startTime, endTime, searchBatchName, searchStatus, sortItem, sortOrder, Integer.parseInt(pageNo), pagemax);
			if (kekkaList.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 発生日時のフォーマット処理
			for (GMap dataMap : kekkaList) {
				dataMap.put("start_time", formatTimeSS(dataMap.get("start_time")));
			}

			//5.戻り値を返す
			return "success";
		}finally{
			connection.close();
		}
	}

	/**
	 * 部品初期化
	 */
	protected void initParts() {
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}

	/**
	 * コンボボックスなどの読込
	 */
	protected void displaySeigyo() {
		batchNameList = systemLogic.loadBatchName();
		statusList = systemLogic.loadNaibuCdSetting("batch_status");
		hourList = makeDropDownList(24);
		minList = makeDropDownList(60);
	}

	/**
	 * プルダウンリストの作成
	 * 
	 * @param length プルダウンサイズ
	 * @return プルダウンリスト
	 */
	protected List<GMap> makeDropDownList(int length) {
		List<GMap> dropDownList = new ArrayList<GMap>();

		// 対象ファイルをプルダウンListに設定する
		for (int i = 0; i < length; i ++) {
			GMap data = new GMap();
			String value = String.format("%1$02d", i);
			data.put(NAIBU_CODE_KEY, value);
			data.put(NAIBU_NAME_KEY, value);
			
			dropDownList.add(data);
		}
		
		return dropDownList;
	}

	/**
	 * yyyy/MM/dd HH:mm:ssからDateへ
	 * @param strDate 変換前
	 * @return 変換後
	 */
	protected Timestamp strToDate(String strDate) {
		try {
			return new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(strDate).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
