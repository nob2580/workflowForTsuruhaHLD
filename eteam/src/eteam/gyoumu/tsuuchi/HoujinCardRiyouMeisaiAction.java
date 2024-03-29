package eteam.gyoumu.tsuuchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.SHIKYU_ICHIRAN_SORT_COLUMN;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 法人カード利用明細Action
 */
@Getter @Setter @ToString
public class HoujinCardRiyouMeisaiAction extends EteamAbstractAction {

//＜定数＞
	/** 伝票状態ID（コード一覧より）*/
	static final String DENPYOU_JOUTAI_ID = "denpyou_jyoutai";
	/** ソートカラム名（コード一覧より）*/
	static final String SORT_COLUMN_NM = "houjin_meisai_sort_column";
	/** ソート順（コード一覧より）*/
	static final String SORT_KBN_NM = "sort_kbn";
	/** CSVファイル名 */
	static final String CSV_FILENAME = "法人カード利用明細";
	/** PDFファイル名 */
	static final String PDF_FILENAME = "法人カード利用明細";
	
//＜画面入力＞
	/** 利用日From */
	String riyouBiFrom="";
	/** 利用日To */
	String riyouBiTo="";
	/** 起票日付From */
	String kihyouBiFrom="";
	/** 起票日付To */
	String kihyouBiTo="";
	/** 起票者所属部門名 */
	String kihyouShozokuBumonCd="";
	/** 起票者所属部門名 */
	String kihyouShozokuBumonName="";
	/** 起票者社員番号 */
	String kihyouShainNo="";
	/** 起票者カード番号 */
	String kihyouCardNum="";
	/** 起票者名 */
	String kihyouUserName="";
	/** 起票者ユーザーID */
	String kensakuKihyouShozokuUserId="";
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
	/** 並べ替え順序DropDownList① */
	List<GMap> sortList1;
	/** 並べ替え順序DropDownList② */
	List<GMap> sortList2;
	/** カラム名DropDownListドメイン */
	String[] sortColumnListDomain;
	/** 順序DropDownListドメイン */
	String[] sortListDomain;
	/** ソート②：カラムデフォルト値 */
	String defaultSortColumn2 = SHIKYU_ICHIRAN_SORT_COLUMN.BUMON;

	/** 権限レベル */
	protected boolean IsKanri;

	//表示
	/** 検索結果リスト(合計) */
	List<GMap> goukeiList = new ArrayList<GMap>();
	/** 検索結果リスト(明細) */
	List<GMap> meisaiList = new ArrayList<GMap>();

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
	protected HoujinCardRiyouMeisaiLogic houjinCardLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(HoujinCardRiyouMeisaiAction.class);

	//＜入力チェック＞
	@Override protected void formatCheck() {
		checkDate(riyouBiFrom, "利用日From", false);
		checkDate(riyouBiTo, "利用日To", false);
		checkDate(kihyouBiFrom, "起票日付From", false);
		checkDate(kihyouBiTo, "起票日付To", false);
		checkString(kihyouShozokuBumonName, 1,20, "起票者所属部門名", false);
		checkString(kihyouShainNo, 1,15, "起票者社員番号", false);
		checkString(kihyouCardNum, 1,16, "起票者ｶｰﾄﾞ番号", false);
		checkString(kihyouUserName, 1,21, "起票者名", false);
		checkCheckbox(kihyouchuu, "起票中", false);
		checkCheckbox(shinseichuu, "申請中", false);
		checkCheckbox(syouninzumi, "承認済", false);
		checkCheckbox(hininzumi, "否認済", false);
		checkCheckbox(torisagezumi, "取下済", false);
		checkDomain(sortColumn1, sortColumnListDomain, "並べ替え①項目名", false);
		checkDomain(sort1,  sortListDomain, "並べ替え①順序", false);
	}

	@Override protected void hissuCheck(int eventNum) {
		String[][] list = {
		//項目				項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
		{riyouBiFrom, "利用日From", "0", "1"},
		{riyouBiTo, "利用日To", "0", "1"},
		};
		hissuCheckCommon(list, eventNum);
	}


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
			hissuCheck(2);
			if(! errorList.isEmpty()){
				return "error";
			}
			
			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理 
			meisaiList = tsuuchiLogic.loadHoujinCardRiyouMeisaiKensaku(
					  getUser()
					, toDate(kihyouBiFrom)
					, toDate(kihyouBiTo)
					, kihyouShainNo
					, kihyouCardNum
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, replaceSpaceDelete(n2b(kihyouUserName))
					, toDate(riyouBiFrom)
					, toDate(riyouBiTo)
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1);
		
			if(meisaiList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//ContentType判定
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());

			// CSVファイルデータを作る
			String fileName = ("{file_name}_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".csv");
			fileName = fileName.replace("{file_name}", CSV_FILENAME);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(ContentType.EXCEL);
		    response.setHeader("Content-Disposition", EteamCommon.contentDisposition(browserCode, true, fileName));
		    response.setCharacterEncoding(Encoding.MS932);
		    writer = response.getWriter();
			
			// 1行目：利用日の挿入
			List<Object> colRiyoubi = new ArrayList<Object>();
			colRiyoubi.add("利用日");
			if (riyouBiFrom.equals("") && riyouBiTo.equals("")) {
				colRiyoubi.add("");
			} else if (riyouBiFrom.equals("")) {
				colRiyoubi.add(" ～ " + riyouBiTo);
			} else if (riyouBiTo.equals("")) {
				colRiyoubi.add(riyouBiFrom + " ～ ");
			} else {
				colRiyoubi.add(riyouBiFrom + " ～ " + riyouBiTo);
			}
			EteamFileLogic.outputCsvRecord(writer, colRiyoubi);
			
			// 2行目：カラム名(和名)のリストを作る
			List<Object> colName = new ArrayList<Object>();
			colName.add("社員番号");
			colName.add("社員名");
			colName.add("社員ｶｰﾄﾞ番号");
			colName.add("利用日");
			colName.add("伝票ID");
			colName.add("伝票種別");
			colName.add("内容／備考／摘要");
			colName.add("利用金額");
			EteamFileLogic.outputCsvRecord(writer, colName);
			
			// 3行目以降：データ部のリストを作る
			/* データ編集 */
			List<GMap> dataList = houjinCardLogic.makeIchiranData(meisaiList, false);
			/* データ書込み */
			for (GMap map : dataList) {
				List<Object> csvRecord = makeIchiranForCSV(map);
				EteamFileLogic.outputCsvRecord(writer, csvRecord);
			}
			// 総合計の書込み
			EteamFileLogic.outputCsvRecord(writer, makeSaisyuuGoukeiRowForCSV(false , houjinCardLogic.sumAllHoujinCardRiyou));
			
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
			hissuCheck(2);
			if(! errorList.isEmpty()){
				return "error";
			}
			
			// データ取得
			meisaiList = tsuuchiLogic.loadHoujinCardRiyouMeisaiKensaku(
					  getUser()
					, toDate(kihyouBiFrom)
					, toDate(kihyouBiTo)
					, kihyouShainNo
					, kihyouCardNum
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, replaceSpaceDelete(n2b(kihyouUserName))
					, toDate(riyouBiFrom)
					, toDate(riyouBiTo)
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1);
			
			if(meisaiList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			// Excelファイル作成
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			HoujinCardRiyouMeisaiXlsLogic houjinCardXlsLogic = EteamContainer.getComponent(HoujinCardRiyouMeisaiXlsLogic.class, connection);
			houjinCardXlsLogic.makeExcel(meisaiList, riyouBiFrom, riyouBiTo, excelOut);
			
			//PDF変換
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			Workbook workbook = new Workbook(new ByteArrayInputStream(excelOut.toByteArray()));
			workbook.save(pdfOut, SaveFormat.PDF);
			
			//コンテンツタイプ設定 
			String fileName = ("{file_name}_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".pdf");
			
			fileName = fileName.replace("{file_name}", PDF_FILENAME);
			
			
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
		houjinCardLogic = EteamContainer.getComponent(HoujinCardRiyouMeisaiLogic.class);
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
		List<GMap> sortList = sysLogic.loadNaibuCdSetting(SORT_KBN_NM);
		sortColumnList1 = sortColumnList;
		sortList1 = sortList;
		sortColumnList2 = sortColumnList;
		sortList2 = sortList;
		sortColumnListDomain = EteamCommon.mapList2Arr(sortColumnList, "naibu_cd");
		sortListDomain = EteamCommon.mapList2Arr(sortList, "naibu_cd");
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
			kensakuKihyouShozokuUserId = getUser().getTourokuOrKoushinUserId();
			GMap userInfo = bumonUserLogic.selectUserInfo(kensakuKihyouShozokuUserId);
			kihyouShainNo = (String)userInfo.get("shain_no");
			kihyouCardNum = (String)userInfo.get("houjin_card_shikibetsuyou_num");
			kihyouUserName = userInfo.getString("user_sei") + "　" + userInfo.getString("user_mei");
		}
		// adminユーザーか経理権限ロールの場合
		if (accessAuthority.equals("SU") || accessAuthority.equals("KR")) {
			IsKanri = true;
		}
	}

	/**
	 * 一覧情報の編集(CSV)
	 * @param kensakuListMap      一覧情報
	 * @return 編集結果
	 */
	protected List<Object>  makeIchiranForCSV(GMap kensakuListMap) {

		List<Object> csvRecord = new ArrayList<Object>();
		csvRecord.add(kensakuListMap.get("shain_no"));
		csvRecord.add(kensakuListMap.get("user_full_name"));
		csvRecord.add(kensakuListMap.get("houjin_card_shikibetsuyou_num"));
		csvRecord.add(kensakuListMap.get("riyoubi"));
		csvRecord.add(kensakuListMap.get("denpyou_id"));
		csvRecord.add(kensakuListMap.get("denpyou_shubetsu"));
		csvRecord.add(kensakuListMap.get("naiyou_bikou_tekiyou"));
		csvRecord.add(kensakuListMap.get("riyou_kingaku"));

		return csvRecord;
	}

	/**
	 * 最終合計行の編集(CSV出力用)
	 * @param shikyuuGoukeiFlg 合計ならtrue/総合計ならfalse
	 * @param kingakuData      金額情報
	 * @return 編集結果
	 */
	protected List<Object> makeSaisyuuGoukeiRowForCSV(boolean shikyuuGoukeiFlg ,BigDecimal kingakuData) {

		List<Object> goukeList = new ArrayList<Object>();
		goukeList.add("");
		goukeList.add("");
		goukeList.add("");
		goukeList.add("");
		goukeList.add("");
		goukeList.add("");
		goukeList.add("総合計");
		goukeList.add(kingakuData);
		
		return goukeList;
	}
}
