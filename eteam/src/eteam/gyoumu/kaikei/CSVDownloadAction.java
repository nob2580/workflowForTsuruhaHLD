package eteam.gyoumu.kaikei;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamNaibuCodeSetting.FB_STATUS;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CSVダウンロード画面Action
 */
@Getter @Setter @ToString
public class CSVDownloadAction extends EteamAbstractAction {

//＜定数＞
	/** プルダウン用コードキー */
	static final String NAIBU_CODE_KEY = "naibu_cd";
	/** プルダウン用表示名キー */
	static final String NAIBU_NAME_KEY = "name";

//＜画面入力＞
	/** 年月 */
	String nengetsu;
	/** 対象ファイル */
	String taishouFile;

//＜画面入力以外＞
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** ダウンロードファイル用 */
	protected InputStream inputStream;

// /** 種別のDropDownList */
// List<GMap> syubetsuList;
	/** 年月のDropDownList */
	List<GMap> nengetsuList;
	/** 対象ファイルのDropDownList */
	List<GMap> taishouFileList;

//＜部品＞
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** システム管理SELECT */
	SystemKanriCategoryLogic systemLogic;

	
//＜入力チェック＞
	@Override 
	protected void formatCheck() {}

	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{nengetsu, "年月", "0", "0", "1", },
				{taishouFile, "対象ファイル", "0", "0", "1", },
			};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			// 各DropDownリスト作成
			makeDropDownListData();
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * ファイルリストイベント
	 * 対象ファイルリストを取得・設定する
	 * @return 処理結果
	 */
	public String filelist(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			// DropDownリスト作成
			makeDropDownListData();
			
			// 入力チェック
			hissuCheck(2);
			formatCheck();
			if (0 < errorList.size()) {
				return "error";
			}

			// 対象ファイルが0件の場合
			if (StringUtils.isNotEmpty(nengetsu) && taishouFileList.isEmpty()) {
				errorList.add("ダウンロードする対象ファイルが存在しません。");
				return "error";
			}

			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * ダウンロードイベント
	 * CSVファイルをダウンロードする
	 * @return ResultName
	 */
	public String download() {

		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			//（1）各DropDownリスト作成
			makeDropDownListData();
			
			//（2）入力チェック
			hissuCheck(3);
			formatCheck();
			if (0 < errorList.size()) {
				return "error";
			}

			//（3）検索条件を設定する
			// 対象ファイルの振込日
			java.sql.Date furikomiDate = strToSqlDate(taishouFile);
// // セッションの業務ロールID
// String gyoumuRoleId = getUser().getGyoumuRoleId();
			
			//（4）FB抽出データの取得
			List<GMap> fbDataList = kaikeiLogic.loadFbDataList(
					FB_STATUS.FB_DATA_SAKUSEIZUMI, getUser().getGyoumuRoleShozokuBumonCd(), furikomiDate);
			
			//（5）対象ファイルが0件の場合
			if (fbDataList.isEmpty()) {
				errorList.add("出力のFBデータが存在しません。");
				return "error";
			}

			//（6）CSVファイル情報を生成する。
			// 出力のCSVファイル名を作成　振込明細表yyyyMMdd.csv
			String fileName = taishouFile + ".csv";
			fileName = "振込明細表" + fileName;

			contentType = ContentType.EXCEL;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);

			try {
				// 出力ストリームの設定
				ByteArrayOutputStream objBos = new ByteArrayOutputStream();
				OutputStreamWriter objOsw = new OutputStreamWriter(objBos, Encoding.MS932);
				
				// 振込明細表
				makeSeisahyouData(objOsw, fbDataList);
				objOsw.flush();
				objOsw.close();
				
				this.inputStream = new ByteArrayInputStream(objBos.toByteArray());
				
			} catch (IOException e) {
			    throw new RuntimeException(e);
			}
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 振込精査表データを作成
	 * @param objOsw OutputStreamWriter
	 * @param fbDataList FB抽出リスト
	 * @throws IOException IOException
	 */
	protected void makeSeisahyouData(OutputStreamWriter objOsw, List<GMap> fbDataList) throws IOException {

		objOsw.write("支払日,振込元金融機関コード,振込元金融機関名（半角カナ）,振込元金融機関支店コード,振込元金融機関支店名（半角カナ）,"
				+ "振込元預金種目コード,振込元口座番号,伝票ID,所属部門コード,所属部門名,社員番号,"
				+ "社員名（半角カナ）,社員名,振込先金融機関コード,振込先金融機関名（半角カナ）,振込先金融機関支店コード,振込先金融機関支店名（半角カナ）,"
				+ "振込先預金種目コード,振込先口座番号,金額");

		String furikomiDate = taishouFile.substring(0,4) + "/" +  taishouFile.substring(4,6) + "/" + taishouFile.substring(6);
		for (GMap map : fbDataList) {
			objOsw.write(System.getProperty("line.separator"));
			objOsw.write("\"");
			objOsw.write(furikomiDate); //支払日(YYYY/MM/DD)=FB抽出．振込日
			objOsw.write("\",\"");
			objOsw.write((String)map.get("moto_kinyuukikan_cd")); //振込元銀行=FB抽出．振込元金融機関コード
			objOsw.write("\",\"");
			objOsw.write((String)map.get("moto_kinyuukikan_name_hankana")); //振込元銀行名=FB抽出．振込元金融機関名（半角カナ）
			objOsw.write("\",\"");
			objOsw.write((String)map.get("moto_kinyuukikan_shiten_cd")); //振込元支店コード=FB抽出．振込元金融機関支店コード
			objOsw.write("\",\"");
			objOsw.write((String)map.get("moto_kinyuukikan_shiten_name_hankana")); //振込元支店名=FB抽出．振込元金融機関支店名（半角カナ）
			objOsw.write("\",\"");
			objOsw.write((String)map.get("moto_yokin_shumoku_cd")); //振込元預金種目=FB抽出．振込元預金種目コード
			objOsw.write("\",\"");
			objOsw.write((String)map.get("moto_kouza_bangou")); //振込元口座番号=FB抽出．振込元口座番号
			objOsw.write("\",\"");
			objOsw.write((String)map.get("denpyou_id")); //調書ＩＤ=FB抽出．伝票ID
			objOsw.write("\",\"");
			objOsw.write(formatString(map.get("bumon_cd"))); //所属部署コード=承認ルート．部門コード
			objOsw.write("\",\"");
			objOsw.write(formatString(map.get("bumon_name"))); //所属部署名=所属部門．部門名
			objOsw.write("\",\"");
			objOsw.write((String)map.get("kokyaku_cd1")); //社員コード=FB抽出．顧客コード１
			objOsw.write("\",\"");
			objOsw.write(formatString(map.get("saki_kouza_meigi_kana"))); //社員名=FB抽出．振込先口座名義（カナ）
			objOsw.write("\",\"");
			objOsw.write(formatString(map.get("saki_kouza_meigi_kanji"))); //社員名漢字=FB抽出．振込先口座名義漢字
			objOsw.write("\",\"");
			objOsw.write((String)map.get("saki_kinyuukikan_cd")); //振込先銀行=FB抽出．振込先金融機関銀行コード
			objOsw.write("\",\"");
			objOsw.write((String)map.get("saki_kinyuukikan_name_hankana")); //振込先銀行名=FB抽出．振込先金融機関名（半角カナ）
			objOsw.write("\",\"");
			objOsw.write((String)map.get("saki_kinyuukikan_shiten_cd")); //振込先支店コード=FB抽出．振込先金融機関支店コード
			objOsw.write("\",\"");
			objOsw.write((String)map.get("saki_kinyuukikan_shiten_name_hankana")); //振込先支店名=FB抽出．振込先金融機関支店名（半角カナ）
			objOsw.write("\",\"");
			objOsw.write((String)map.get("saki_yokin_shumoku_cd")); //振込先預金種目=FB抽出．振込先預金種目コード
			objOsw.write("\",\"");
			objOsw.write((String)map.get("saki_kouza_bangou")); //振込先口座番号=FB抽出．振込先口座番号
			objOsw.write("\",\"");
			objOsw.write(map.getString("kingaku")); //金額=FB抽出．金額
			objOsw.write("\"");
		}

	}
	
	/**
	 * 画面側のDropDownリストデータを作成
	 */
	protected void makeDropDownListData() {

		/* 年月DropDownリスト作成 */
		makeNengetsuList();
		
		/* 対象ファイルDropDownリスト作成 */
		makeTaishouFileList();
	}
	
	/**
	 * 年月プルダウンリストの作成
	 */
	protected void makeNengetsuList() {
		nengetsuList = new ArrayList<>();

        Date nowDate = new Date(System.currentTimeMillis());
        // ①当月元素の作成
		GMap data = new GMap();
		String codeVal = formatDateToKey(nowDate);
		String nameVal = formatDateToVal(nowDate, "1");
		data.put(NAIBU_CODE_KEY, codeVal);
		data.put(NAIBU_NAME_KEY, nameVal);
		nengetsuList.add(data);

        // ②前月元素の作成
		// 前月Dateを取得
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(nowDate);
        // 日付－１个月
        rightNow.add(Calendar.MONTH, -1);
        Date beforeDate = rightNow.getTime();

        // 前月元素を追加
		data = new GMap();
		codeVal = formatDateToKey(beforeDate);
		nameVal = formatDateToVal(beforeDate, "1");
		data.put(NAIBU_CODE_KEY, codeVal);
		data.put(NAIBU_NAME_KEY, nameVal);
		nengetsuList.add(data);
	}

	/**
	 * 対象ファイルプルダウンリストの作成
	 */
	protected void makeTaishouFileList() {
		taishouFileList = new ArrayList<>();
		
		// 対象ファイルプルダウンにリストを追加
		if (!StringUtils.isEmpty(nengetsu)) {
			// 検索条件を設定する
			// ダウンロード条件．年月の月初日
			java.sql.Date strMinDate = strToSqlDate(getMinDayOfMonthDate(nengetsu));
			// ダウンロード条件．年月の月末日
			java.sql.Date strMaxDate = strToSqlDate(getMaxDayOfMonthDate(nengetsu));
// // 業務ロールID
// String gyoumuRoleId = getUser().getGyoumuRoleId();

			// 対象ファイルの支払日(振込日)データを取得
			List<GMap> furikomiDateList = kaikeiLogic.loadTaishouFileList(
					FB_STATUS.FB_DATA_SAKUSEIZUMI, strMinDate, strMaxDate);
			
			// 対象ファイルが0件の場合
			if (furikomiDateList.isEmpty()) {
				return;
			}

			// 対象ファイルをプルダウンListに設定する
			for (GMap furikomiDateMap: furikomiDateList) {
				GMap data = new GMap();
				String codeVal = formatDateToKey(furikomiDateMap.get("furikomi_date"));
				String nameVal = formatDateToVal(furikomiDateMap.get("furikomi_date"), "2");
				data.put(NAIBU_CODE_KEY, codeVal);
				data.put(NAIBU_NAME_KEY, nameVal + " 支払分");
				
				taishouFileList.add(data);
			}
		}
	}
	
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}

	/**
	 * 年月の月初日日付の取得
	 * @param strDate 該当年月の日付
	 * @return String 月初日日付
	 */
	protected String getMinDayOfMonthDate(String strDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToDate(strDate));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return formatDateToKey(calendar.getTime());
	}

	/**
	 * 年月の月末日日付の取得
	 * @param strDate 該当年月の日付
	 * @return String 月末日日付
	 */
	protected String getMaxDayOfMonthDate(String strDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToDate(strDate));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return formatDateToKey(calendar.getTime());
	}

	/**
	 * 日付型を文字列(yyyyMMdd)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	protected String formatDateToKey(Object d){
		if (null == d) {
			return "";
		} else {
			return new SimpleDateFormat("yyyyMMdd").format(d);
		} 
	}

	/**
	 * 日付型を文字列(yyyy年MM月dd日)に変換。
	 * @param d 変換前
	 * @param format フォーマット(1: yyyy年MM月、2: yyyy年MM月dd日)
	 * @return 変換後
	 */
	protected String formatDateToVal(Object d, String format){
		if (null == d) {
			return "";
		} else if ("1".equals(format)) {
			return new SimpleDateFormat("yyyy年MM月").format(d);
		} else {
			return new SimpleDateFormat("yyyy年MM月dd日").format(d);
		}
	}

	/**
	 * yyyyMMddからDateへ。nullやブランクはnullへ。
	 * @param strDate 変換前
	 * @return 変換後
	 */
	protected Date strToDate(String strDate) {
		try {
			return isEmpty(strDate) ? null : new Date(new SimpleDateFormat(
					"yyyyMMdd").parse(strDate).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * yyyyMMddからDateへ。nullやブランクはnullへ。
	 * @param strDate 変換前
	 * @return 変換後
	 */
	protected java.sql.Date strToSqlDate(String strDate) {
		try {
			return isEmpty(strDate) ? null : new java.sql.Date(new SimpleDateFormat(
					"yyyyMMdd").parse(strDate).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
