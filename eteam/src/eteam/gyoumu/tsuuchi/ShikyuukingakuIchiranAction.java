package eteam.gyoumu.tsuuchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamFileLogic;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.SHIKYU_ICHIRAN_SORT_COLUMN;
import eteam.common.EteamNaibuCodeSetting.SHIKYU_ICHIRAN_SORT_COLUMN_DATE;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.tsuuchi.ShikyuukingakuIchiranLogic.GoukeiShikyuuData;
import eteam.gyoumu.tsuuchi.ShikyuukingakuIchiranLogic.ShainGoukeiShikyuuData;
import eteam.gyoumu.tsuuchi.ShikyuukingakuIchiranLogic.ShikyuuData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧画面Action
 */
@Getter @Setter @ToString
public class ShikyuukingakuIchiranAction extends EteamAbstractAction {

//＜定数＞
	/** 伝票状態ID（コード一覧より）*/
	static final String DENPYOU_JOUTAI_ID = "denpyou_jyoutai";
	/** ソートカラム名（コード一覧より）*/
	static final String SORT_COLUMN_NM = "shikyu_ichiran_sort_column";
	/** ソートカラム名(日付)（コード一覧より）*/
	static final String SORT_COLUMN_DT = "shikyu_ichiran_sort_column_date";
	/** ソート順（コード一覧より）*/
	static final String SORT_KBN_NM = "sort_kbn";
	/** CSVファイル名 */
	static final String CSV_FILENAME = "社員別支給金額一覧表";
	/** CSVファイル名 */
	static final String CSV_FILENAME_SUM = "社員別支給金額一覧表（社員別サマリ）";
	/** PDFファイル名 */
	static final String PDF_FILENAME = "社員別支給金額一覧表";
	/** PDFファイル名 */
	static final String PDF_FILENAME_SUM = "社員別支給金額一覧表（社員別サマリ）";
	
//＜画面入力＞
	/** 起票日付From */
	String kihyouBiFrom="";
	/** 起票日付To */
	String kihyouBiTo="";
	/** 起票者所属部門コード */
	String kihyouShozokuBumonCd="";
	/** 起票者所属部門名 */
	String kihyouShozokuBumonName="";
	/** 起票者社員番号 */
	String kihyouShainNo="";
	/** 起票者名 */
	String kihyouUserName="";
	/** 起票者ユーザーID */
	String kensakuKihyouShozokuUserId="";
	/** 支払日From */
	String shiharaiBiFrom="";
	/** 支払日To */
	String shiharaiBiTo="";
	/** 使用日From */
	String shiyouBiFrom="";
	/** 使用日To */
	String shiyouBiTo="";
	/** 状態：起票中 */
	String kihyouchuu = "";
	/** 状態：申請中 */
	String shinseichuu = "";
	/** 状態：承認済 */
	String syouninzumi = "";
	/** 状態：否認済 */
	String hininzumi = "";
	/** 状態：取下済 */
	String torisagezumi = "";
	/** ソート①：カラム */
	String sortColumn1;
	/** ソート①：昇順/降順 */
	String sort1;
	/** ソート②：カラム */
	String sortColumn2 = SHIKYU_ICHIRAN_SORT_COLUMN.BUMON;
	/** ソート②：昇順/降順 */
	String sort2;
	/** ソート③：カラム */
	String sortColumn3;
	/** ソート③：昇順/降順 */
	String sort3;
	/** 社員別サマリ */
	String shainBetsuSummary = "";

//＜画面入力以外＞
	/** 状態：起票中名 */
	String kihyouchuuNm;
	/** 状態：申請中名 */
	String shinseichuuNm;
	/** 状態：承認済名 */
	String syouninzumiNm;
	/** 状態：否認済名 */
	String hininzumiNm;
	/** 状態：取下済名 */
	String torisagezumiNm;

	/** 並べ替えカラム名DropDownList① */
	List<GMap> sortColumnList1;
	/** 並べ替えカラム名DropDownList② */
	List<GMap> sortColumnList2;
	/** 並べ替えカラム名DropDownList③ */
	List<GMap> sortColumnList3;
	/** 並べ替え順序DropDownList① */
	List<GMap> sortList1;
	/** 並べ替え順序DropDownList② */
	List<GMap> sortList2;
	/** 並べ替え順序DropDownList③ */
	List<GMap> sortList3;
	/** カラム名DropDownListドメイン */
	String[] sortColumnListDomain;
	/** カラム名DropDownList(日付)ドメイン */
	String[] sortColumnListDateDomain;
	/** 順序DropDownListドメイン */
	String[] sortListDomain;
	/** ソート②：カラムデフォルト値 */
	String defaultSortColumn2 = SHIKYU_ICHIRAN_SORT_COLUMN.BUMON;
	
	/** 法人カード利用有無 */
	protected boolean houjinRiyou;
	/** 会社手配 */
	protected boolean tehaiRiyou;

	/** 権限レベル */
	protected boolean IsKanri;

	//ダウンロード用の
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** ダウンロードファイル用 */
	protected InputStream inputStream;

	//部品
	/** 通知ロジック */
	protected TsuuchiCategoryLogic tsuuchiLogic;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysLogic;
	/** 部門ユーザー管理カテゴリロジック */
	protected BumonUserKanriCategoryLogic bumonUserLogic;
	/** 社員別支給一覧ロジック */
	protected ShikyuukingakuIchiranLogic shikyuuIchiranLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(ShikyuukingakuIchiranAction.class);

	//＜入力チェック＞
	@Override protected void formatCheck() {
		checkDate(kihyouBiFrom, "起票日付From", false);
		checkDate(kihyouBiTo, "起票日付To", false);
		checkString(kihyouShozokuBumonName, 1,20, "起票者所属部門名", false);
		checkString(kihyouShainNo, 1,15, "起票者社員番号", false);
		checkString(kihyouUserName, 1,21, "起票者名", false);
		checkDate(shiharaiBiFrom, "支払日From", false);
		checkDate(shiharaiBiTo, "支払日To", false);
		checkDate(shiyouBiFrom, "使用日From", false);
		checkDate(shiyouBiTo, "使用日To", false);
		checkCheckbox(kihyouchuu, "起票中", false);
		checkCheckbox(shinseichuu, "申請中", false);
		checkCheckbox(syouninzumi, "承認済", false);
		checkCheckbox(hininzumi, "否認済", false);
		checkCheckbox(torisagezumi, "取下済", false);
		checkDomain(sortColumn1, sortColumnListDomain, "並べ替え①項目名", false);
		checkDomain(sort1,  sortListDomain, "並べ替え①順序", false);
		checkDomain(sortColumn2, sortColumnListDomain, "並べ替え②項目名", false);
		checkDomain(sort2,  sortListDomain, "並べ替え②順序", false);
		checkDomain(sortColumn3, sortColumnListDateDomain, "並べ替え③項目名", false);
		checkDomain(sort3,  sortListDomain, "並べ替え③順序", false);
		checkCheckbox(shainBetsuSummary, "社員別サマリ", false);
	}

	@Override protected void hissuCheck(int eventNum) {}


//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {
		
		try(EteamConnection connection = EteamConnection.connect()) {
			
			initConnection(connection);
			
			//画面部品の作成
			makeParts();
			//検索条件領域
			setKihyouUserInfo(connection);
			
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * CSV出力イベント
	 * @return 処理結果
	 */
	public String csvOutput(){

		PrintWriter writer = null;
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			hissuCheck(1);
			if(! errorList.isEmpty()){
				return "error";
			}
			
			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理
			Boolean isShainBetsuSummary = ("1".equals(shainBetsuSummary));
			
			//--------------------
			//データ取得
			//--------------------
			//明細リスト・・・伝票単位 or 起票者単位
			List<ShikyuuData> meisaiList = shikyuuIchiranLogic.loadIchiran(
					  getUser()
					, toDate(kihyouBiFrom)
					, toDate(kihyouBiTo)
					, kihyouShainNo
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, replaceSpaceDelete(n2b(kihyouUserName))
					, toDate(shiharaiBiFrom)
					, toDate(shiharaiBiTo)
					, toDate(shiyouBiFrom)
					, toDate(shiyouBiTo)
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1
					, sortColumn2
					, sort2
					, sortColumn3
					, sort3
					, isShainBetsuSummary);
			if(meisaiList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			//明細リスト（サマリ）・・・合計計算用
			List<ShikyuuData> summaryList = shikyuuIchiranLogic.loadIchiran(
					  getUser()
					, toDate(kihyouBiFrom)
					, toDate(kihyouBiTo)
					, kihyouShainNo
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, replaceSpaceDelete(n2b(kihyouUserName))
					, toDate(shiharaiBiFrom)
					, toDate(shiharaiBiTo)
					, toDate(shiyouBiFrom)
					, toDate(shiyouBiTo)
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1
					, sortColumn2
					, sort2
					, sortColumn3
					, sort3
					, false);
			//合計計算
			GoukeiShikyuuData goukeiData = shikyuuIchiranLogic.calcAllKingaku(summaryList);

			//--------------------
			//CSVを表現した２次元配列作る
			//--------------------
			//ContentType判定
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			String fileName = ("{file_name}_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".csv");
			
			if (isShainBetsuSummary) {
				fileName = fileName.replace("{file_name}", CSV_FILENAME_SUM);
			} else {
				fileName = fileName.replace("{file_name}", CSV_FILENAME);
			}

			// CSVファイルデータを作る
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(ContentType.EXCEL);
		    response.setHeader("Content-Disposition", EteamCommon.contentDisposition(browserCode, true, fileName));
		    response.setCharacterEncoding(Encoding.MS932);
		    writer = response.getWriter();

			// 1行目：カラム名(和名)
			List<Object> colName = new ArrayList<Object>();
			colName.add("社員番号");
			colName.add("社員名");
			colName.add("起票部門");
			if(SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(sortColumn3)){
				colName.add("起票日");
			}else{
				colName.add("使用日");
			}
			if (!isShainBetsuSummary) {
				colName.add("伝票ID");
				colName.add("状態");
				colName.add("伝票種別");
			}
			colName.add("支払日");
			colName.add("現金支払金額");
			colName.add("振込支払金額");
			if (houjinRiyou) colName.add("法人ｶｰﾄﾞ支払金額");
			if (tehaiRiyou)  colName.add("会社手配支払金額");
			colName.add("仮払金額");
			colName.add("現金差引支給金額");
			colName.add("振込差引支給金額");
			EteamFileLogic.outputCsvRecord(writer, colName);
			
			// 2行目以降：データ部
			for (ShikyuuData d : meisaiList) {
				List<Object> csvRecord = new ArrayList<Object>();
				csvRecord.add(d.getShainNo());
				csvRecord.add(d.getUserFullName());
				if (isShainBetsuSummary) {
					String bumonName = String.join(System.getProperty("line.separator"), d.getBumonFullName().split(","));
					csvRecord.add(bumonName);
					if(SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(sortColumn3)){
						csvRecord.add(d.getTourokuTime()); //FROM-TO
					}else{
						csvRecord.add(d.getShiyoubi()); //FROM-TO
					}
					csvRecord.add(d.getShiharaibi()); //FROM-TO
				} else {
					csvRecord.add(d.getBumonFullName());
					if(SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(sortColumn3)){
						csvRecord.add(d.getTourokuTime()); //FROM-TO
					}else{
						csvRecord.add(d.getShiyoubi()); //FROM-TO
					}
					csvRecord.add(d.getDenpyouId());
					csvRecord.add(d.getJoutai());
					csvRecord.add(d.getDenpyouShubetsu());
					csvRecord.add(d.getShiharaibi());
				}
				csvRecord.add(formatMoney(d.getGenkinShiharai()));
				csvRecord.add(formatMoney(d.getFurikomiShiharai()));
				if (houjinRiyou) csvRecord.add(formatMoney(d.getHoujinShiharai()));
				if (tehaiRiyou)  csvRecord.add(formatMoney(d.getTehaiShiharai()));
				csvRecord.add(formatMoney(d.getKaribarai()));
				csvRecord.add(formatMoney(d.getGenkinSashihiki()));
				csvRecord.add(formatMoney(d.getFurikomiSashihiki()));
				EteamFileLogic.outputCsvRecord(writer, csvRecord);

				// 社員別合計
				if (! isShainBetsuSummary && d.getShainGoukei() != null) {
					ShainGoukeiShikyuuData s = d.getShainGoukei();
					csvRecord = new ArrayList<Object>();
					csvRecord.add("");
					csvRecord.add("");
					csvRecord.add("");
					csvRecord.add("");
					csvRecord.add("");
					csvRecord.add("");
					csvRecord.add("合計");
					csvRecord.add("");
					csvRecord.add(formatMoney(s.getSumGenkinShiharai()));
					csvRecord.add(formatMoney(s.getSumFurikomiShiharai()));
					if (houjinRiyou) csvRecord.add(formatMoney(s.getSumHoujinShiharai()));
					if (tehaiRiyou)  csvRecord.add(formatMoney(s.getSumTehaiShiharai()));
					csvRecord.add(formatMoney(s.getSumKaribarai()));
					csvRecord.add(formatMoney(s.getSumGenkinSashihiki()));
					csvRecord.add(formatMoney(s.getSumFurikomiSashihiki()));
					EteamFileLogic.outputCsvRecord(writer, csvRecord);
				}
			}
			
			// 支払金額合計
			List<Object> goukeiRecord = new ArrayList<Object>();
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("支給金額合計");
			goukeiRecord.add("現金");
			goukeiRecord.add(formatMoney(goukeiData.sumGenkinShikyuu));
			goukeiRecord.add("振込");
			goukeiRecord.add(formatMoney(goukeiData.sumFurikomiShikyuu));
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			EteamFileLogic.outputCsvRecord(writer, goukeiRecord);
			
			// 差引マイナス
			goukeiRecord = new ArrayList<Object>();
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("差引マイナス");
			goukeiRecord.add("現金");
			goukeiRecord.add(formatMoney(goukeiData.sumGenkinSashihiki));
			goukeiRecord.add("振込");
			goukeiRecord.add(formatMoney(goukeiData.sumFurikomiSashihiki));
			goukeiRecord.add("");
			goukeiRecord.add("");
			goukeiRecord.add("");
			EteamFileLogic.outputCsvRecord(writer, goukeiRecord);
			
			//法人カード利用
			if (houjinRiyou) {
				goukeiRecord = new ArrayList<Object>();
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("法人カード");
				goukeiRecord.add(formatMoney(goukeiData.sumHoujinKingaku));
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				EteamFileLogic.outputCsvRecord(writer, goukeiRecord);
			}
			if (tehaiRiyou) {
				goukeiRecord = new ArrayList<Object>();
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("会社手配");
				goukeiRecord.add(formatMoney(goukeiData.sumTehaiKingaku));
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				goukeiRecord.add("");
				EteamFileLogic.outputCsvRecord(writer, goukeiRecord);
			}

			
			//5.戻り値を返す
			return "success";
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}finally{
			if(writer != null) {
				writer.close();
			}
		}

	}

	/**
	 * PDF帳票出力
	 * @return 結果
	 */
	public String pdfOutput() {
		
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();
			
			// 1.入力チェック
			formatCheck();
			hissuCheck(1);
			if(! errorList.isEmpty()){
				return "error";
			}
			
			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理
			Boolean isShainBetsuSummary = ("1".equals(shainBetsuSummary));

			//--------------------
			//データ取得
			//--------------------
			//明細リスト・・・伝票単位 or 起票者単位
			List<ShikyuuData> meisaiList = shikyuuIchiranLogic.loadIchiran(
					  getUser()
					, toDate(kihyouBiFrom)
					, toDate(kihyouBiTo)
					, kihyouShainNo
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, replaceSpaceDelete(n2b(kihyouUserName))
					, toDate(shiharaiBiFrom)
					, toDate(shiharaiBiTo)
					, toDate(shiyouBiFrom)
					, toDate(shiyouBiTo)
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1
					, sortColumn2
					, sort2
					, sortColumn3
					, sort3
					, isShainBetsuSummary);
			if(meisaiList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			//明細リスト（サマリ）・・・合計計算用
			List<ShikyuuData> summaryList = shikyuuIchiranLogic.loadIchiran(
					  getUser()
					, toDate(kihyouBiFrom)
					, toDate(kihyouBiTo)
					, kihyouShainNo
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, replaceSpaceDelete(n2b(kihyouUserName))
					, toDate(shiharaiBiFrom)
					, toDate(shiharaiBiTo)
					, toDate(shiyouBiFrom)
					, toDate(shiyouBiTo)
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1
					, sortColumn2
					, sort2
					, sortColumn3
					, sort3
					, false);
			//合計計算
			GoukeiShikyuuData goukeiData = shikyuuIchiranLogic.calcAllKingaku(summaryList);

			//--------------------
			// Excelファイル作成
			//--------------------
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			ShikyuukingakuIchiranXlsLogic shikyuIchiranXlsLogic = EteamContainer.getComponent(ShikyuukingakuIchiranXlsLogic.class, connection);
			shikyuIchiranXlsLogic.makeExcel(isShainBetsuSummary, meisaiList, goukeiData, sortColumn3, excelOut);

			//--------------------
			//PDF変換
			//--------------------
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			Workbook workbook = new Workbook(new ByteArrayInputStream(excelOut.toByteArray()));
			workbook.save(pdfOut, SaveFormat.PDF);
			
			//コンテンツタイプ設定 
			String fileName = ("{file_name}_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".pdf");
			
			if (isShainBetsuSummary) {
				fileName = fileName.replace("{file_name}", PDF_FILENAME_SUM);
			} else {
				fileName = fileName.replace("{file_name}", PDF_FILENAME);
			}
			
			contentType = ContentType.PDF;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);
			inputStream = new ByteArrayInputStream(pdfOut.toByteArray());
			
			return "success";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * DB接続を初期化する。
	 * @param connection コネクション
	 */
	protected void initConnection(EteamConnection connection){
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		shikyuuIchiranLogic = EteamContainer.getComponent(ShikyuukingakuIchiranLogic.class, connection);
	}

	/**
	 * 画面パーツを作成する。
	 */
	protected void makeParts(){
		// 伝票状態
		List<GMap> denpyouJoutaiList = sysLogic.loadNaibuCdSetting(DENPYOU_JOUTAI_ID);
		for (GMap joutai : denpyouJoutaiList) {
			switch (joutai.get("naibu_cd").toString()){
			case DENPYOU_JYOUTAI.KIHYOU_CHUU:
				kihyouchuuNm = joutai.get("name").toString();
				break;
			case DENPYOU_JYOUTAI.SHINSEI_CHUU:
				shinseichuuNm = joutai.get("name").toString();
				break;
			case DENPYOU_JYOUTAI.SYOUNIN_ZUMI:
				syouninzumiNm = joutai.get("name").toString();
				break;
			case DENPYOU_JYOUTAI.HININ_ZUMI:
				hininzumiNm = joutai.get("name").toString();
				break;
			case DENPYOU_JYOUTAI.TORISAGE_ZUMI:
				torisagezumiNm = joutai.get("name").toString();
				break;
				
			}
		}
		
		// 並べ替え
		List<GMap> sortColumnList = sysLogic.loadNaibuCdSetting(SORT_COLUMN_NM);
		List<GMap> sortColumnDateList = sysLogic.loadNaibuCdSetting(SORT_COLUMN_DT);
		List<GMap> sortList = sysLogic.loadNaibuCdSetting(SORT_KBN_NM);
		sortColumnList1 = sortColumnList;
		sortList1 = sortList;
		sortColumnList2 = sortColumnList;
		sortList2 = sortList;
		sortColumnList3 = sortColumnDateList;
		sortList3 = sortList;
		sortColumnListDomain = EteamCommon.mapList2Arr(sortColumnList, "naibu_cd");
		sortColumnListDateDomain = EteamCommon.mapList2Arr(sortColumnDateList, "naibu_cd");
		sortListDomain = EteamCommon.mapList2Arr(sortList, "naibu_cd");
		
		// 権限制御
		houjinRiyou = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		tehaiRiyou  = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
	}

	/**
	 * 権限による検索条件領域の表示設定
	 * @param connection      コネクション
	 */
	protected void setKihyouUserInfo(EteamConnection connection) {

		// 権限判定
		String accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, getUser().getGyoumuRoleId(), "KR");

		// 一般ユーザーの場合
		if(accessAuthority.length() == 0) {
			IsKanri = false;
			kensakuKihyouShozokuUserId = getUser().getSeigyoUserId();
			GMap userInfo = bumonUserLogic.selectUserInfo(kensakuKihyouShozokuUserId);
			kihyouShainNo = (String)userInfo.get("shain_no");
			kihyouUserName = userInfo.getString("user_sei") + "　" + userInfo.getString("user_mei");
		}
		// adminユーザーか経理権限ロールの場合
		if (accessAuthority.equals("SU") || accessAuthority.equals("KR")) {
			IsKanri = true;
		}
	}
}
