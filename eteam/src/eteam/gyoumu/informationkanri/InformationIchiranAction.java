package eteam.gyoumu.informationkanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.INFO_STATUS;
import eteam.common.EteamSettingInfo;
import eteam.common.select.InformationKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * インフォメーション一覧画面Action
 */
@Getter @Setter @ToString
public class InformationIchiranAction extends EteamAbstractAction {

//＜定数＞
	/** インフォメーションステータスコードID（コード一覧より）*/
	static final String INFO_STATUS_CD_ID = "info_status";
	/** メニュー用インフォメーションソート区分 （表示順ソート）*/
	static final String SORT_KBN_MENU = "3";

//＜画面入力＞
	/** ステータス */
	String status = INFO_STATUS.ALL;
	/** 選択したチェックボックスのインフォメーションIDリスト */
	String[] infoSentakuIdList;
	/** 並び順更新用のインフォメーションIDリスト */
	String[] informationIdList;
	/** 並び順更新用のインフォメーションステータス名リスト */
	String[] statusNameList;
	
	/** 掲示期間ソート区分 */
	String sortKbn = SORT_KBN_MENU;
	/** ページ番号 */
	String pageNo = "1";

//＜画面入力以外＞
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** 一覧 */
	List<GMap> ichiranList;
	/** ステータスのDropDownList */
	List<GMap> infoStatusList;
	/** ページング処理 link押下時のURL */
	String pagingLink;
	/** インフォメーションステータスの文言(未掲載) */
	String WaitPeriod;
	/** インフォメーションステータスの文言(掲載終了) */
	String ExpiredPeriod;
	/** インフォメーションステータスの文言(掲載中) */
	String DurindPeriod;
	/** インフォメーションID */
	String informationId;

//＜入力チェック＞
	@Override 
	protected void formatCheck() {
		checkString(status, 0, 1, "status", true);
		checkString(sortKbn, 0, 1, "sortKbn", true);
		checkNumber(pageNo, 0, 10, "pageNo", true);
	}
	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 			EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{status ,"status"				,"0", "2", },
				{sortKbn ,"sortKbn"				,"0", "2", },
				{pageNo ,"pageNo"				,"0", "2", },
			};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 検索イベント
	 * 初期表示/ステータス検索/掲示期間でのソート
	 * @return 処理結果
	 */
	public String kensaku(){

		// 1.入力チェック
		formatCheck();

		try(EteamConnection connection = EteamConnection.connect()){
			InformationKanriCategoryLogic lc = EteamContainer.getComponent(InformationKanriCategoryLogic.class, connection);
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			// 2.データ存在チェック
			// 検索のためなし
			// 3.アクセス可能チェック なし
			
			// 4.処理 
			/* DropDownリスト作成 */
			// naibu_cd_settingテーブルからデータを取得
			infoStatusList = common.loadNaibuCdSetting(INFO_STATUS_CD_ID);
			
			/* 
			 * データの全件数取得 
			 * 件数取得だけなので、パラメータはステータスのみ
			 */
			totalCount = lc.findInfoCount(n2b(status));
			
			/* 1ページ最大表示件数をテーブルから取得 */
			int pagemax = Integer.parseInt(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.RECORD_NUM_PER_PAGE));
			
			/* 総ページ数の計算 */
			totalPage = EteamCommon.calcTotalPageNum(pagemax, totalCount);
			if(totalPage == 0){
				totalPage = 1;
			}
			
			// 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
			if(isEmpty(pageNo) || "0".equals(pageNo)){
				pageNo = "1";
			} else if(Integer.parseInt(pageNo) > totalPage) {
				pageNo = String.valueOf(totalPage);
			}
			
			/* ページングリンクURLを設定 */
			pagingLink ="information_ichiran?sortKbn="+sortKbn+"&status="+status+"&";
			
			/* 
			 * 一覧データ取得
			 * ソート区分によって、掲示期間の昇順/降順
			 * ステータス・ページ番号によって取得するデータを制御する
			 */
			ichiranList = lc.loadInfo(
					n2b(sortKbn),
					n2b(status),
					Integer.parseInt(pageNo),
					(pagemax));
			if(ichiranList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			/* ステータス文言の取得 */
			for (GMap infomap : infoStatusList) {
				if(INFO_STATUS.MI_KEISAI.equals(infomap.get("naibu_cd"))) {
					WaitPeriod = (String) infomap.get("name");
				}
				else if(INFO_STATUS.KEISAI_CHUU.equals(infomap.get("naibu_cd"))) {
					DurindPeriod = (String) infomap.get("name");
				}
				else if(INFO_STATUS.KEISAI_SHUURYOU.equals(infomap.get("naibu_cd"))) {
					ExpiredPeriod = (String) infomap.get("name");
				}
			}
			
			for (GMap map : ichiranList) {
				
				/* 背景色の変更 */
				String bkclrset = EteamCommon.bkColorSettei(formatDate(map.get("tsuuchi_kikan_from")), formatDate(map.get("tsuuchi_kikan_to")));
				map.put("bkclrset", bkclrset);
				
				/* インフォメーションのステータス文言の設定 */
				if("wait-period-bgcolor".equals(bkclrset)){
					map.put("statusName", WaitPeriod);
				}
				else if("disabled-bgcolor".equals(bkclrset)){
					map.put("statusName", ExpiredPeriod);
				}
				else{
					map.put("statusName", DurindPeriod);
				}
				
				map.put("informationId", map.get("info_id")); //インフォメーションID
				map.put("keijikikanFrom", formatDate(map.get("tsuuchi_kikan_from"))); //掲示期間開始日
				map.put("keijikikanTo", formatDate(map.get("tsuuchi_kikan_to"))); //掲示期間終了日
				map.put("tsuuchinaiyou", map.get("tsuuchi_naiyou")); //通知内容
			}

			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 削除イベント
	 * 削除ボタン押下時
	 * 選択した明細を削除する。
	 * @return 処理結果
	 */
	public String sakujo(){

		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		
		// 3.アクセス可能チェック なし
		
		try(EteamConnection connection = EteamConnection.connect()){
			InformationLogic lc = EteamContainer.getComponent(InformationLogic.class, connection);
			InformationKanriCategoryLogic infolc = EteamContainer.getComponent(InformationKanriCategoryLogic.class, connection);
			
			//4.処理
			/* チェックがつけられたインフォメーション分、データ存在チェックと削除を繰り返す。*/
			for (int i = 0; i< infoSentakuIdList.length; i++) {
				informationId = infoSentakuIdList[i];
				//2.データ存在チェック
				GMap record = infolc.findInfo(informationId);
				if(null == record){
					throw new EteamDataNotFoundException();
				}
				
				/* DBからの削除処理 */
				lc.delete(informationId);
			}
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 表示順更新イベント
	 * 掲載終了以外のインフォメーションで表示順テーブルを洗い替える。
	 * @return 処理結果
	 */
	public String orderSave(){

		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		
		// 3.アクセス可能チェック なし
		
		try(EteamConnection connection = EteamConnection.connect()){
			InformationLogic lc = EteamContainer.getComponent(InformationLogic.class, connection);
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			infoStatusList = common.loadNaibuCdSetting(INFO_STATUS_CD_ID);
			String keisaiSyuuryouName = infoStatusList.stream().filter(x -> x.get("naibu_cd").equals(INFO_STATUS.KEISAI_SHUURYOU)).findFirst().orElse(null).get("name");
			lc.deleteSort();
			
			//4.処理
			if(null != informationIdList) {
				int hyoujiJun=0;
				for(int i=0 ; i < informationIdList.length ; i++) {
					String infomationId = informationIdList[i];
					if(!statusNameList[i].equals(keisaiSyuuryouName)){
						// 採番は1から
						lc.insertSort(infomationId, hyoujiJun+1);
						hyoujiJun++;
					}
				}
			}
			this.sortKbn = SORT_KBN_MENU;
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
}
