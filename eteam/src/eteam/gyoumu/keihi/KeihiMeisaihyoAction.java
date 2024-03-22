package eteam.gyoumu.keihi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamFileLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiBumon;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiEdaban;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiKamoku;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiSyuukeiBumon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 経費明細一覧Actionｓ
 */
@Getter @Setter @ToString
public class KeihiMeisaihyoAction extends EteamAbstractAction {

//＜定数＞
	/** CSVファイル名 */
	static final String CSV_FILENAME = "keihiMeisaiIchiran.csv";
	/** EXCELファイル名 */
	static final String EXCEL_FILENAME = "keihiMeisaiIchiran_";

//＜画面入力＞
	/** 対象月 */
	protected String targetDate;
	/** 集計部門コード */
	protected String syuukeiBumonCd;
	/** 集計部門名 */
	protected String syuukeiBumonName;
	/** 部門コードfrom */
	protected String fromBumonCd;
	/** 部門名from */
	protected String fromBumonName;
	/** 部門コードto */
	protected String toBumonCd;
	/** 部門名to */
	protected String toBumonName;
	/** 科目コードfrom */
	protected String fromKamokuCd;
	/** 科目名from */
	protected String fromKamokuName;
	/** 科目コードto */
	protected String toKamokuCd;
	/** 科目名to */
	protected String toKamokuName;

//＜画面入力以外＞
	/** 表示データ(部門-科目-仕訳 */
	protected List<KeihiSyuukeiBumon> syuukeiBumonList;
	/** 選択月リスト */
	protected List<Map<String, String>> monthList;

	//ページング用
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** ページ番号 */
	String pageNo = "1";
	/** ページング処理 link押下時のURL */
	String pagingLink;

	//CSV、EXCEL出力用
	/** ダウンロードファイル用 */
	InputStream inputStream;
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** EXCELファイルの長さ */
	long contentLength;
	
	/** 経費明細部門表示フラグ */
	boolean showBumon = "1".equals(setting.keihimeisaiBumon());
	/** 経費明細枝番表示フラグ */
	boolean showEdaban = "1".equals(setting.keihimeisaiEdano());

//＜部品＞
	/** 集計部門ロジック */
	protected KeihiMeisaiLogic myLogic;
	/** 経費明細表ロジック */
	protected KeihiMeisaihyoXlsLogic kmLogic;
	/** 部門ユーザロジック */
	protected BumonUserKanriCategoryLogic buLogic;
	/** 日付フォーマット(MM/dd) */
	protected SimpleDateFormat dfm = new SimpleDateFormat("MM/dd");

	@Override
	protected void formatCheck() {
		checkDate(targetDate, "対象月", false);
		checkString(syuukeiBumonCd, 1, 8, "集計部門コード", false);
		checkString(fromBumonCd, 1, 8, "部門コードFrom", false);
		checkString(toBumonCd, 1, 8, "部門コードTo", false);
// checkString(fromKamokuCd, 1, 8, "科目コードFrom", false);//科目FROM-TOは普通に科目外部コードなので8桁だよねって思ったのだけどまあマスタ選択なのでチェックしないでもいいことにしておくよ
// checkString(toKamokuCd, 1, 8, "科目コードTo", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目				項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{targetDate, "対象月", "0", "2"},
				{syuukeiBumonCd, "集計部門", "0", "1"},
		};
		hissuCheckCommon(list, eventNum);
	}
	
	/**
	 * セキュリティパターンがないならエラー※メニューで非表示にするけどURLで来るかもしれないので
	 */
	protected void accessCheck() {

		//adminユーザなら無条件で許可
		if(!(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId()))) {
			
			List<Integer> spList = buLogic.selectSecurityPatternList(getUser().getSeigyoUserId(),null);
			
			//セキュリティパターンが取得できていなければエラー
			if (spList.isEmpty()) {
				throw new EteamAccessDeniedException("セキュリティパターンがないので経費明細一覧は参照できません。");
			}
		}

	}

	/**
	 * 初期化処理です。
	 * @param connection コネクション
	 */
	public void initialize(EteamConnection connection) {
		monthList = EteamCommon.createMonthList();
		myLogic = EteamContainer.getComponent(KeihiMeisaiLogic.class, connection);
		kmLogic = EteamContainer.getComponent(KeihiMeisaihyoXlsLogic.class, connection);
		buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
	}

	/**
	 * 表示処理
	 * @return 結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			accessCheck();
			
			totalCount = 0;
			pageNo = "0";
			pagingLink = "";
		}
		
		return "success";
	}

	/**
	 * 検索処理
	 * @return 結果
	 */
	public String search() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			accessCheck();
			
			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}
			
			//総件数取得
			totalCount = countMeisai();
			
			//1ページ最大表示件数をテーブルから取得
			int pageMax = Integer.parseInt(setting.recordNumPerPage());

			// 総ページ数の計算
			totalPage = EteamCommon.calcTotalPageNum(pageMax, totalCount);
			if(totalPage == 0){
				totalPage = 1;
			}
			
			// 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
			if(isEmpty(pageNo) || "0".equals(pageNo)){
				pageNo = "1";
			} else if(Integer.parseInt(pageNo) > totalPage) {
				pageNo = String.valueOf(totalPage);
			}
			
			// ページングリンクURLを設定
			pagingLink = EteamCommon.getParmeterString(
					this,
					"keihi_data_meisai_search",
					new HashSet<String>(Arrays.asList(new String[] {
						"targetDate",
						"syuukeiBumonCd",
						"syuukeiBumonName",
						"fromBumonCd",
						"fromBumonName",
						"toBumonCd",
						"toBumonName",
						"fromKamokuCd",
						"fromKamokuName",
						"toKamokuCd",
						"toKamokuName",
					}))
				).toString();
			
			// データを取得
			List<GMap> list = loadMeisai(pageMax);
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//表示書式セット
			for (GMap map : list) {
				map.put("dymd", dfm.format(map.get("dymd")).toString());
				map.put("valu", formatMoney(map.get("valu")));
				if(showBumon) map.put("total_bmn", formatMoney(map.get("total_bmn")));
				map.put("total_kmk", formatMoney(map.get("total_kmk")));
				if(showEdaban) map.put("total_eda", formatMoney(map.get("total_eda")));
			}

			// 集計部門-部門-科目-枝番別にする
			syuukeiBumonList = myLogic.toBumonList(list, showBumon, showEdaban);
		}
		return "success";
	}

	/**
	 * CSV出力イベント
	 * @return 結果
	 */
	public String csvOutput() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			accessCheck();
			
			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}

			// データ取得
			List<GMap> list = loadMeisai(0);
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 集計部門-部門-科目-枝番別にする
			syuukeiBumonList = myLogic.toBumonList(list, showBumon, showEdaban);

			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// ヘッダー部(集計部門と対象月)を出力
			List<Object> headerRecord = new ArrayList<Object>();
			headerRecord.add(syuukeiBumonName);
			headerRecord.add(targetDate.substring(0, 4) + "年" + targetDate.substring(5, 7) + "月度");
			csvRecords.add(headerRecord);

			// データ部を出力
			for(KeihiSyuukeiBumon syuukeiBumon : syuukeiBumonList) {
				
				
				// 集計部門名
				List<Object> syuukeiBumonRecord = new ArrayList<Object>();
				syuukeiBumonRecord.add(syuukeiBumon.getName());
				csvRecords.add(syuukeiBumonRecord);
				
				for(KeihiBumon bumon : syuukeiBumon.getBumonList()) {
				
					// カラム名
					List<Object> colRecord = new ArrayList<Object>();
					if(showBumon) colRecord.add("部門名称");
					colRecord.add("科目名");
					if(showEdaban) colRecord.add("枝番名称");
					colRecord.add("日付");
					colRecord.add("相手先");
					colRecord.add("摘要");
					colRecord.add("金額");
					csvRecords.add(colRecord);
	
					
					for (KeihiKamoku kamoku : bumon.getKamokuList()) {
						// 仕訳を出力
						for (KeihiEdaban edaban : kamoku.getEdabanList()) {
							for (GMap map : edaban.getShiwakeList()) {
								List<Object> dataRecord = new ArrayList<Object>();
								if(showBumon) dataRecord.add(map.get("futan_bumon_name"));
								dataRecord.add(map.get("kamoku_name_ryakushiki"));
								if(showEdaban) dataRecord.add(map.get("edaban_name"));
								dataRecord.add(dfm.format(map.get("dymd")));
								dataRecord.add(n2b((String)map.get("torihikisaki_name_ryakushiki")));
								dataRecord.add(map.get("tky"));
								dataRecord.add(map.get("valu"));
								csvRecords.add(dataRecord);
							}
							
							// 合計行(枝番小計)を出力
							if(showEdaban){
								GMap shiwakeEda = edaban.getShiwakeList().get(0);
								List<Object> edabanTotal = new ArrayList<Object>();
								if(showBumon) edabanTotal.add("");
								edabanTotal.add("");
								if(showEdaban) edabanTotal.add("小計");
								edabanTotal.add("");
								edabanTotal.add("");
								edabanTotal.add("");
								edabanTotal.add(shiwakeEda.get("total_eda"));
								csvRecords.add(edabanTotal);
							}
						}
						
						// 合計行(科目合計)を出力
						GMap shiwakeKmk = kamoku.getEdabanList().get(0).getShiwakeList().get(0);
						List<Object> kamokuTotal = new ArrayList<Object>();
						if(showBumon) kamokuTotal.add("");
						kamokuTotal.add("合計");
						if(showEdaban) kamokuTotal.add("");
						kamokuTotal.add("");
						kamokuTotal.add("");
						kamokuTotal.add("");
						kamokuTotal.add(shiwakeKmk.get("total_kmk"));
						csvRecords.add(kamokuTotal);
					}
					
					// 合計行(部門総合計)を出力
					if(showBumon){
						GMap shiwakeBmn = bumon.getKamokuList().get(0).getEdabanList().get(0).getShiwakeList().get(0);
						List<Object> bumonTotal = new ArrayList<Object>();
						if(showBumon) bumonTotal.add("総合計");
						bumonTotal.add("");
						if(showEdaban) bumonTotal.add("");
						bumonTotal.add("");
						bumonTotal.add("");
						bumonTotal.add("");
						bumonTotal.add(shiwakeBmn.get("total_bmn"));
						csvRecords.add(bumonTotal);
					}
				}
			}

			// CSVファイルデータを作る
			try {
				//CSVデータ作成
				ByteArrayOutputStream objBos = new ByteArrayOutputStream();
				EteamFileLogic.outputCsv(objBos, csvRecords);

				//コンテンツタイプ設定
				contentType = ContentType.EXCEL;
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME);
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();
			} catch (IOException e) {
			    throw new RuntimeException(e);
			}
		}
		return "success";
	}

	/**
	 * EXCEL帳票出力
	 * @return 結果
	 */
	public String excelOutput() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			formatCheck();
			hissuCheck(2);
			if (!errorList.isEmpty()) {
				return "error";
			}
			
			//データ取得
			List<GMap> list = loadMeisai(0);
			
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 集計部門-部門-科目-枝番別にする
			syuukeiBumonList = myLogic.toBumonList(list, showBumon, showEdaban);

			//Excelファイル作成
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			kmLogic.makeExcel(syuukeiBumonList, toDate(targetDate), syuukeiBumonName, excelOut);

			//コンテンツタイプ設定
			String fileName = (EXCEL_FILENAME + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls");
			contentType = ContentType.EXCEL;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);
			inputStream = new ByteArrayInputStream(excelOut.toByteArray());
			contentLength = excelOut.size();
		}

		return "success";
	}

	/**
	 * 経費明細データ件数
	 * @return 件数
	 */
	protected long countMeisai() {
		GMap user = buLogic.selectUserJouhou(getUser().getSeigyoUserId());
		List<Integer> spList = buLogic.selectSecurityPatternList(getUser().getSeigyoUserId(),null);
		//管理者ユーザなら全表示させるためセキュリティパターンリストクリア
		if(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId())) {spList.clear();}
		boolean wfOnly = ((String)user.get("security_wfonly_flg")).equals("1");
		
		
		return myLogic.countKeihiMeisai(
			spList,
			wfOnly,
			syuukeiBumonCd,
			fromBumonCd,
			toBumonCd,
			fromKamokuCd,
			toKamokuCd,
			toDate(targetDate));
	}

	/**
	 * 経費明細データ読み込み
	 * @param pageMax 1ページ最大表示件数
	 * @return 経費明細
	 */
	protected List<GMap> loadMeisai(int pageMax) {
		GMap user = buLogic.selectUserJouhou(getUser().getSeigyoUserId());
		List<Integer> spList = buLogic.selectSecurityPatternList(getUser().getSeigyoUserId(),null);
		//管理者ユーザなら全表示させるためセキュリティパターンリストクリア
		if(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId())) {spList.clear();}
		boolean wfOnly = ((String)user.get("security_wfonly_flg")).equals("1");
		
		
		return myLogic.loadKiehiMeisai(
			spList,
			wfOnly,
			syuukeiBumonCd,
			fromBumonCd,
			toBumonCd,
			fromKamokuCd,
			toKamokuCd,
			toDate(targetDate),
			Integer.parseInt(pageNo),
			pageMax,
			showBumon,
			showEdaban);
	}

}
