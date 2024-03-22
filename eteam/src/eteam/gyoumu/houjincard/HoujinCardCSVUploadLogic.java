package eteam.gyoumu.houjincard;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamNaibuCodeSetting.HOUJIN_CARD_SHUBETSU;

/**
 * 法人カード管理用Logic
 */
public abstract class HoujinCardCSVUploadLogic extends EteamAbstractLogic {

	/** ファイル名(HoujinCcardCSVUploadActionから渡す) */
	String uploadFileFileName = "";

	/** CSVファイル情報（法人カード使用履歴）表示用データリスト */
	List<String[]> hyoujiDataList;
	/** CSVファイル情報（法人カード使用履歴）登録用データリスト */
	List<HoujinCardCSVUploadInfoBase> seigyoDataList;

	/** 法人カード情報用ロジック */
	HoujinCardLogic hcLogic;

	/**
	 * 指定されたカード毎のインスタンス生成。
	 * @param cardShubetsu カード種別
	 * @param connection   コネクション
	 * @return 各カード種別向けクラス
	 */
	public static HoujinCardCSVUploadLogic getInstance(String cardShubetsu, EteamConnection connection){
    	if(HOUJIN_CARD_SHUBETSU.JCB.equals(cardShubetsu)) return EteamContainer.getComponent(HoujinCardCSVUploadJCBLogic.class, connection);
    	if(HOUJIN_CARD_SHUBETSU.VISA.equals(cardShubetsu)) return EteamContainer.getComponent(HoujinCardCSVUploadVISALogic.class, connection);
    	if(HOUJIN_CARD_SHUBETSU.NICOS.equals(cardShubetsu)) return EteamContainer.getComponent(HoujinCardCSVUploadNICOSLogic.class, connection);
    	if(HOUJIN_CARD_SHUBETSU.SAISON.equals(cardShubetsu)) return EteamContainer.getComponent(HoujinCardCSVUploadSAISONLogic.class, connection);
    	
// Ver22.06.06.00 Bizプリカ追加対応 *-    	
    	if(HOUJIN_CARD_SHUBETSU.BIZPURICA.equals(cardShubetsu)) return EteamContainer.getComponent(HoujinCardCSVUploadBIZPURICALogic.class, connection);
// -*

    	return null;
	}

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, _connection);
	}

	/**
	 * 表示用ヘッダリストの返却
	 * @return 表示用ヘッダリスト
	 */
	public abstract List<String> getHyoujiHeader();

	/**
	 * ファイルからデータ取得し、メモリに格納
	 * フォーマット等にエラーある場合はデータ格納はせずエラーリスト設定
	 * @param readfile アップロードファイル
	 * @param errorList エラーリスト
	 */
	public abstract void read(File readfile, List<String> errorList);

	/**
	 * 表示用データリストの返却
	 * @return 表示用データリスト
	 */
	public List<String[]> getHyoujiData() {
		return hyoujiDataList;
	}
	/**
	 * 登録用データリストの返却
	 * @return 登録処理用データリスト
	 */
	public List<HoujinCardCSVUploadInfoBase> getSeigyoData() {
		return seigyoDataList;
	}

    /**
     * ファイルをバイト列へ。ファイルなしや0サイズならnullへ。
     * @param f 変換前
     * @return 変換後
     */
    public byte[] toByte(File f) {
        if (! (null != f && f.exists() && 0 < f.length())) {
            return null;
        }
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            FileUtils.copyFile(f, buf);
            return buf.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CSVファイルの重複レコード確認メソッド
     * @param dataList CSVファイルレコード情報
     * @param rowData 履歴データ作成行
     * @param callingCount 呼出元ループのカウント
     * @return 重複判定結果	重複あり：true 重複なし：false
     */
    protected boolean duplicateCheck(List<String[]> dataList, String[] rowData, int callingCount) {
    	// 履歴データ作成行より前の行とデータを照らし合わせ判定する(重複レコード扱いするのは、2つ目以降のレコードのため)
    	for(int count = 0 ; count < callingCount ; count++) {
    		String[] rowDataTmp = dataList.get(count);
    		if (Arrays.equals(rowDataTmp, rowData))
		{
			return true;
		}
    	}
    	return false;
    }

    /**
     * yyyyMMdd形式日付の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param yyyymmdd 入力
     * @return 変換可能か
     */
    protected boolean checkYYYYMD(String yyyymmdd) {
    	//GenericValidator.isDate(yyyymd, "yyyy/M/d", true)はうまく機能しない？
    	//2月31日などの存在しない日付が弾けないので、else節内で考慮
		if(isEmpty(yyyymmdd) || ! yyyymmdd.matches("[1-9][0-9]{3}(0[1-9]|10|11|12)([0][1-9]|[12][0-9]|3[01])")){
			return false;
		} else {
			int ymd = Integer.parseInt(yyyymmdd);
			int date = ymd % 100;
			int month = ymd % 10000 / 100;
			int year = ymd / 10000;
			return date <= 28 // 28日までは無条件にOK
					|| (date <= 30 && (month != 2)) // 2月以外は30日までOK
					|| (month <= 7 && month % 2 == 1) // 31日あるのは7月以下の奇数月と
					|| (month >= 8 && month % 2 == 0) // 8月以上の偶数月
					|| (date == 29 && (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))); // 残るは2月29日の扱い（グレゴリオ暦準拠）
		}
    }
}


/**
 * JCBフォーマット
 */
class HoujinCardCSVUploadJCBLogic extends HoujinCardCSVUploadLogic{
	/** データ列数（JCB）*/
	static final int DATACOLUMN_NUM_JCB = 21;

	@Override
	public List<String> getHyoujiHeader() {
		List<String> hyoujiHeaderList = new ArrayList<String>();
		hyoujiHeaderList.add("カード番号");
		hyoujiHeaderList.add("利用者氏名");
		hyoujiHeaderList.add("社員番号");
		hyoujiHeaderList.add("組織番号");
		hyoujiHeaderList.add("組織名称");
		hyoujiHeaderList.add("ご利用日");
		hyoujiHeaderList.add("ご利用先など(漢字)");
		hyoujiHeaderList.add("ご利用先など(ｶﾅ)");
		hyoujiHeaderList.add("ご利用先など(ALPHA)");
		hyoujiHeaderList.add("ご利用金額(円)");
		hyoujiHeaderList.add("支払区分");
		hyoujiHeaderList.add("売上計上場所業態コード");
		hyoujiHeaderList.add("通貨");
		hyoujiHeaderList.add("現地通貨利用金額");
		hyoujiHeaderList.add("円換算レート");
		hyoujiHeaderList.add("円換算日");
		hyoujiHeaderList.add("相手先組織コード");
		hyoujiHeaderList.add("タクシーチケット番号");
		hyoujiHeaderList.add("タクシーチケット費目コード");
		hyoujiHeaderList.add("伝票区分");
		hyoujiHeaderList.add("摘要");
		return hyoujiHeaderList;
	}

	@Override
	public void read(File readfile, List<String> errorList) {
		String extension = FilenameUtils.getExtension(uploadFileFileName);
		if("csv".equalsIgnoreCase(extension) == false){
			errorList.add("csvファイル以外はアップロード不可です。");
		}
		hyoujiDataList = new ArrayList<String[]>();
		seigyoDataList = new ArrayList<HoujinCardCSVUploadInfoBase>();
		List<String[]> dataList = new ArrayList<>();

		try(BufferedReader inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(readfile)), Encoding.MS932))) {
			//データリスト作成
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray;
				columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}

			//データ項目数チェック
			for(int i = 0; i < dataList.size() ; i++){
				String[] rowData = dataList.get(i);
				if(rowData[0].length() > 0 && "*".equals(rowData[0].substring(0,1))){
					if(rowData.length != DATACOLUMN_NUM_JCB){
						errorList.add((i + 1) + "行目：インポートファイルの項目数が正しくありません。(正常:" + DATACOLUMN_NUM_JCB +"項目、現在:" + rowData.length + "項目)");
					}
				}
			}
			if (!errorList.isEmpty())
			{
				return;
			}

			//履歴データ作成
			for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
				String[] rowData = dataList.get(i);
				//先頭文字が「*」以外ならスキップ
				if ( rowData[0].length() < 1 || !("*".equals(rowData[0].substring(0,1))) )
				{
					continue;
				}

				//データファイルからの加工
				rowData[9] = (rowData[19].equals("1") || rowData[19].equals("2") ? "-" : "") + rowData[9];

				HoujinCardCSVUploadInfoBase rireki = null;
				rireki = new HoujinCardCSVUploadInfoBase();
				seigyoDataList.add(rireki);
				rireki.setNumber (Integer.toString(i));
				rireki.setCardShubetsu ("1");
				rireki.setCardBangou(rowData[0].replace("-","")); //ハイフンは除外
				rireki.setShiyousha(rowData[1]);
				rireki.setShainNo(rowData[2]);
				rireki.setBushoCd(rowData[3]);
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				//元がyyyy/m/dならyyyymmdd型に変換して入力 できないならそのまま登録(後でエラー出力)
				if(checkYYYYMD(rowData[5])) {
					rireki.setRiyoubi(fmt.format(hcLogic.toDateyyyyMd(rowData[5]))); // yyyy/m/dをyyyymmdd形式に変更
				}else {
					rireki.setRiyoubi(rowData[5]);
					errorList.add((i + 1) + "行目：日付はyyyy/m/d形式の有効な日付を入力してください。");
				}
				rireki.setKameiten(rowData[7]);
				rireki.setKingaku(rowData[9]);
				rireki.setGyoushuCd(rowData[11]);

				//重複レコード確認
				rireki.setDuplicateFlg(duplicateCheck(dataList, rowData, i));

				//表示用データリストに登録
				String[] dataStr = new String[21];
				for(int k = 0; k < 21 ; k++) {
					dataStr[k] = n2b(rowData[k]);
				}
				hyoujiDataList.add(dataStr);
			}
		} catch (IOException e) {
			throw new RuntimeException("法人カード使用データインポートファイル読込でエラー", e);
		}
	}

    /**
     * yyyyMMdd形式日付の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param yyyymd 入力
     * @return 変換可能か
     */
	@Override
    protected boolean checkYYYYMD(String yyyymd) {
    	//GenericValidator.isDate(yyyymd, "yyyy/M/d", true)はうまく機能しない？
    	//2月31日などの存在しない日付が弾けないが、一旦保留
		if(isEmpty(yyyymd) || ! yyyymd.matches("[1-9][0-9]{3}/([1-9]|10|11|12)/([1-2]{0,1}[1-9]|10|20|30|31)")){
			return false;
		} else {
			String[] ymd = yyyymd.split("/");
			int year = Integer.parseInt(ymd[0]);
			int month = Integer.parseInt(ymd[1]);
			int date = Integer.parseInt(ymd[2]);
			return date <= 28 // 28日までは無条件にOK
					|| (date <= 30 && (month != 2)) // 2月以外は30日までOK
					|| (month <= 7 && month % 2 == 1) // 31日あるのは7月以下の奇数月と
					|| (month >= 8 && month % 2 == 0) // 8月以上の偶数月
					|| (date == 29 && (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))); // 残るは2月29日の扱い（グレゴリオ暦準拠）
		}
    }
}

/**
 * VISAフォーマット
 */
class HoujinCardCSVUploadVISALogic extends HoujinCardCSVUploadLogic{
	/** データ列長（VIZA）*/
	static final int DATA_BYTELENGTH_VISA = 256;

	@Override
	public List<String> getHyoujiHeader() {
		List<String> hyoujiHeaderList = new ArrayList<String>();
		hyoujiHeaderList.add("TRコード");
		hyoujiHeaderList.add("契約組織番号");
		hyoujiHeaderList.add("契約組織名");
		hyoujiHeaderList.add("申込組織番号");
		hyoujiHeaderList.add("申込組織名");
		hyoujiHeaderList.add("使用者所属組織番号");
		hyoujiHeaderList.add("使用者所属組織名");
		hyoujiHeaderList.add("社員番号");
		hyoujiHeaderList.add("利用会員番号");
		hyoujiHeaderList.add("利用会員名");
		hyoujiHeaderList.add("利用区分");
		hyoujiHeaderList.add("利用日");
		hyoujiHeaderList.add("利用金額");
		hyoujiHeaderList.add("利用加盟店名（カナ）");
		hyoujiHeaderList.add("加盟店番号");
		hyoujiHeaderList.add("SO業種コード");
		hyoujiHeaderList.add("利用区分");
		hyoujiHeaderList.add("加盟店名/課金通話/高速利用状況");
		hyoujiHeaderList.add("現地通貨");
		hyoujiHeaderList.add("海外換算レート");
		hyoujiHeaderList.add("外貨換算日");
		hyoujiHeaderList.add("外貨略称");
		hyoujiHeaderList.add("加盟店所在地");
		hyoujiHeaderList.add("その他");
		hyoujiHeaderList.add("タクチケ番号/クーポン番号/利用ETC番号");
		hyoujiHeaderList.add("タクチケ乗車区間（自）");
		hyoujiHeaderList.add("タクチケ乗車区間（至）");
		hyoujiHeaderList.add("タクチケ法人使用欄");
		hyoujiHeaderList.add("割引元金情報");
		hyoujiHeaderList.add("文字区分");
		hyoujiHeaderList.add("航空券情報");
		hyoujiHeaderList.add("トリアツカイカショ");
		hyoujiHeaderList.add("取扱個所");
		hyoujiHeaderList.add("会員決済日");
		hyoujiHeaderList.add("一連NO");
		return hyoujiHeaderList;
	}

	@Override
	public void read(File readfile, List<String> errorList) {
		//拡張子チェック
		String extension = FilenameUtils.getExtension(uploadFileFileName);
		if("txt".equalsIgnoreCase(extension) == false){
			errorList.add("txtファイル以外はアップロード不可です。");
		}
		hyoujiDataList = new ArrayList<String[]>();
		seigyoDataList = new ArrayList<HoujinCardCSVUploadInfoBase>();
		List<String[]> dataList = new ArrayList<>();

		try(BufferedReader inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(readfile)), Encoding.MS932));) {
			//データリスト作成
			String line;
			while ((line = inFile.readLine()) != null) {
				//VISAフォーマットのみカンマ区切りせずに登録
				String[] columnArray;
				columnArray = new String[1];
				columnArray[0] = line;
				dataList.add(columnArray);
			}

			//データ項目数チェック
			for(int i = 0; i < dataList.size() ; i++){
				String[] rowData = dataList.get(i);
				String row = rowData[0];
				if( row.length() >= 1 && ("2".equals(row.substring(0,1))) ){
					if(EteamCommon.getByteLength(row) != DATA_BYTELENGTH_VISA){
						errorList.add((i + 1) + "行目：インポートファイルのバイト長が正しくありません。(正常:" + DATA_BYTELENGTH_VISA +"バイト、現在:" + EteamCommon.getByteLength(row) + "バイト)");
					}
				}
			}
			if (!errorList.isEmpty())
			{
				return;
			}


			//履歴データ作成
			for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
				String[] rowData = dataList.get(i);
				String row = rowData[0];

				//先頭文字が「2」以外ならスキップ
				if ( row.length() < 1 || !("2".equals(row.substring(0,1))) )
				{
					continue;
				}

				HoujinCardCSVUploadInfoBase rireki = null;
				rireki = new HoujinCardCSVUploadInfoBase();
				seigyoDataList.add(rireki);
				rireki.setNumber (Integer.toString(i));
				rireki.setCardShubetsu ("2");
				rireki.setBushoCd(subStringByte(row,51,55,true));
				rireki.setShainNo(subStringByte(row,75,91,true));
				rireki.setCardBangou(subStringByte(row,91,107,true));
				rireki.setShiyousha(subStringByte(row,107,127,true));

				String yyyymmdd = subStringByte(row,128,136,true);

				//有効な日付かチェックしてそのまま登録(後でエラー出力)
				if(checkYYYYMD(yyyymmdd)) {
					rireki.setRiyoubi(yyyymmdd); // yyyymmddで入力されることになっている
				}else {
					rireki.setRiyoubi(yyyymmdd);
					errorList.add((i + 1) + "行目：利用日はyyyymmdd形式の有効な年月日を入力してください。");
				}

				rireki.setRiyoubi(subStringByte(row,128,136,true));
				if("0".equals(subStringByte(row,136,137,true))){ //金額部先頭が1ならマイナスとみなす
					rireki.setKingaku(subStringByte(row,137,146,true));
				}else if("1".equals(subStringByte(row,136,137,true))){
					rireki.setKingaku("-" + subStringByte(row,137,146,true));
				}
				rireki.setKameiten(subStringByte(row,146,166,true));
				rireki.setGyoushuCd(subStringByte(row,175,179,true));

				//重複レコード確認
				rireki.setDuplicateFlg(duplicateCheck(dataList, rowData, i));

				//表示用データリストに登録
				String[] dataStr = new String[35];
				dataStr[0] = n2b(subStringByte(row,1,3,true)); //TRコード
				dataStr[1] = n2b(subStringByte(row,3,7,true)); //契約組織番号
				dataStr[2] = n2b(subStringByte(row,7,27,true)); //契約組織名
				dataStr[3] = n2b(subStringByte(row,27,31,true)); //申込組織番号
				dataStr[4] = n2b(subStringByte(row,31,51,true)); //申込組織名
				dataStr[5] = n2b(subStringByte(row,51,55,true)); //使用者所属組織番号
				dataStr[6] = n2b(subStringByte(row,55,75,true)); //使用者所属組織名
				dataStr[7] = n2b(subStringByte(row,75,91,true)); //社員番号
				dataStr[8] = n2b(subStringByte(row,91,107,true)); //利用会員番号
				dataStr[9] = n2b(subStringByte(row,107,127,true)); //利用会員名
				dataStr[10] = n2b(subStringByte(row,127,128,true)); //利用区分
				dataStr[11] = n2b(subStringByte(row,128,136,true)); //利用日
				dataStr[12] = n2b(rireki.getKingaku()); //利用金額
				dataStr[13] = n2b(subStringByte(row,146,166,true)); //利用加盟店名（カナ）
				dataStr[14] = n2b(subStringByte(row,166,175,true)); //加盟店番号
				dataStr[15] = n2b(subStringByte(row,175,179,true)); //SO業種コード
				dataStr[16] = n2b(subStringByte(row,179,180,true)); //利用区分
				dataStr[17] = n2b(subStringByte(row,180,240,true)); //加盟店名/課金通話/高速利用状況
				dataStr[18] = ""; //現地通貨
				dataStr[19] = ""; //海外換算レート
				dataStr[20] = ""; //外貨換算日
				dataStr[21] = ""; //外貨略称
				dataStr[22] = ""; //加盟店所在地
				dataStr[23] = ""; //その他
				dataStr[24] = ""; //タクチケ番号/クーポン番号/利用ETC番号
				dataStr[25] = ""; //タクチケ乗車区間（自）
				dataStr[26] = ""; //タクチケ乗車区間（至）
				dataStr[27] = ""; //タクチケ法人使用欄
				dataStr[28] = ""; //割引元金情報
				dataStr[29] = ""; //文字区分
				dataStr[30] = ""; //航空券情報
				dataStr[31] = ""; //トリアツカイカショ
				dataStr[32] = ""; //取扱個所
				dataStr[33] = n2b(subStringByte(row,240,248,true)); //会員決済日
				dataStr[34] = n2b(subStringByte(row,248,253,true)); //一連NO
				hyoujiDataList.add(dataStr);
			}
		} catch (IOException e) {
			throw new RuntimeException("法人カード使用データインポートファイル読込でエラー", e);
		}
	}

	/**
	 * バイト単位でのsubstring実行
	 * @param data 取得文字列(SJISのString)
	 * @param from 開始位置
	 * @param to   終了位置
	 * @param trim 文字列トリミングするか
	 * @return     部分文字列
	 */
	String subStringByte(String data, int from, int to, boolean trim){
		String str;
		try {
			byte[] bArr = data.getBytes("MS932");
			str = new String(bArr,from,(to-from),"MS932");
			if(trim){
				str = str.trim();
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("エンコード不正", e);
		}
		return str;
	}
}

/**
 * NICOSフォーマット
 */
class HoujinCardCSVUploadNICOSLogic extends HoujinCardCSVUploadLogic{
	/** データ列数（NICOS）*/
	static final int DATACOLUMN_NUM_NICOS = 26;

	@Override
	public List<String> getHyoujiHeader() {
		List<String> hyoujiHeaderList = new ArrayList<String>();
		hyoujiHeaderList.add("集計部署コード");
		hyoujiHeaderList.add("予算単位コード");
		hyoujiHeaderList.add("社員番号");
		hyoujiHeaderList.add("カード番号");
		hyoujiHeaderList.add("カード使用者氏名");
		hyoujiHeaderList.add("利用日(和暦)");
		hyoujiHeaderList.add("利用加盟店名");
		hyoujiHeaderList.add("円金額");
		hyoujiHeaderList.add("現地通貨金額・種類");
		hyoujiHeaderList.add("現地通貨コード");
		hyoujiHeaderList.add("換算レート");
		hyoujiHeaderList.add("返品区分");
		hyoujiHeaderList.add("国内・海外売上区分");
		hyoujiHeaderList.add("加盟店区分");
		hyoujiHeaderList.add("利用日(西暦)");
		hyoujiHeaderList.add("加盟店番号");
		hyoujiHeaderList.add("業種コード");
		hyoujiHeaderList.add("割引率");
		hyoujiHeaderList.add("換算日");
		hyoujiHeaderList.add("タクシーチケットＮＯ");
		hyoujiHeaderList.add("葉番号");
		hyoujiHeaderList.add("タクシー付加内容");
		hyoujiHeaderList.add("利用カナ名");
		return hyoujiHeaderList;
	}

	@Override
	public void read(File readfile, List<String> errorList) {
		String extension = FilenameUtils.getExtension(uploadFileFileName);
		if("csv".equalsIgnoreCase(extension) == false){
			errorList.add("csvファイル以外はアップロード不可です。");
		}
		hyoujiDataList = new ArrayList<String[]>();
		seigyoDataList = new ArrayList<HoujinCardCSVUploadInfoBase>();
		List<String[]> dataList = new ArrayList<>();
		EteamCommon.uploadFileSizeCheck();

		try(BufferedReader inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(readfile)), Encoding.MS932))) {
			//データリスト作成
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray;
				columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}

			//データ項目数チェック
			for(int i = 0; i < dataList.size() ; i++){
				String[] rowData = dataList.get(i);
				if("2".equals(rowData[0])){
					if(rowData.length != DATACOLUMN_NUM_NICOS){
						errorList.add((i + 1) + "行目：インポートファイルの項目数が正しくありません。(正常:" + DATACOLUMN_NUM_NICOS +"項目、現在:" + rowData.length + "項目)");
					}
				}
			}
			if (!errorList.isEmpty())
			{
				return;
			}

			//履歴データ作成
			for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
				String[] rowData = dataList.get(i);
				//先頭文字(データ区分)が「2」以外ならスキップ
				if ( rowData[0].length() < 1 || !("2".equals(rowData[0])) )
				{
					continue;
				}

				//データファイルからの加工
				rowData[8] = (rowData[12].equals("1") ? "-" : "") + rowData[8];

				HoujinCardCSVUploadInfoBase rireki = null;
				rireki = new HoujinCardCSVUploadInfoBase();
				seigyoDataList.add(rireki);
				rireki.setNumber (Integer.toString(i));
				rireki.setCardShubetsu ("3");
				rireki.setBushoCd(rowData[1]);
				rireki.setShainNo(rowData[3]);
				rireki.setCardBangou(rowData[4]);
				rireki.setShiyousha(rowData[5]);
				rireki.setKameiten(rowData[7]);
				rireki.setKingaku(rowData[8]);

				//有効な日付かチェックしてそのまま登録(後でエラー出力)
				if(checkYYYYMD(rowData[15])) {
					rireki.setRiyoubi(rowData[15]); // yyyymmddで入力されることになっている
				}else {
					rireki.setRiyoubi(rowData[15]);
					errorList.add((i + 1) + "行目：利用日はyyyymmdd形式の有効な年月日を入力してください。");
				}

				rireki.setGyoushuCd(rowData[17]);

				//重複レコード確認
				rireki.setDuplicateFlg(duplicateCheck(dataList, rowData, i));

				//表示用データリストに登録
				String[] dataStr = new String[23];
				for(int k = 0; k < 23 ; k++) {
					dataStr[k] = n2b(rowData[ (k <= 18 ? k+1 : k+2) ]); //最初の列はデータ区分のためスキップ 21列目にブランク列あるため、タクシーチケットNo以降は1列ずつ読取データずれる
				}
				hyoujiDataList.add(dataStr);
			}
		} catch (IOException e) {
			throw new RuntimeException("法人カード使用データインポートファイル読込でエラー", e);
		}
	}
}


/**
 * SAISONフォーマット
 */
class HoujinCardCSVUploadSAISONLogic extends HoujinCardCSVUploadLogic{
	/** データ列数（SAISON）*/
	static final int DATACOLUMN_NUM_SAISON = 9;

	@Override
	public List<String> getHyoujiHeader() {
		List<String> hyoujiHeaderList = new ArrayList<String>();
		hyoujiHeaderList.add("法人コード");
		hyoujiHeaderList.add("社員番号");
		hyoujiHeaderList.add("計上日");
		hyoujiHeaderList.add("カード番号");
		hyoujiHeaderList.add("名前（名）");
		hyoujiHeaderList.add("名前（姓）");
		hyoujiHeaderList.add("対象売上の加盟店");
		hyoujiHeaderList.add("利用日");
		hyoujiHeaderList.add("請求金額");
		return hyoujiHeaderList;
	}

	@Override
	public void read(File readfile, List<String> errorList) {
		String extension = FilenameUtils.getExtension(uploadFileFileName);
		if("csv".equalsIgnoreCase(extension) == false){
			errorList.add("csvファイル以外はアップロード不可です。");
		}
		hyoujiDataList = new ArrayList<String[]>();
		seigyoDataList = new ArrayList<HoujinCardCSVUploadInfoBase>();
		List<String[]> dataList = new ArrayList<>();

		try(BufferedReader inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(readfile)), Encoding.UTF8))) {
			//データリスト作成
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray;
				columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}

			//データ項目数チェック
			for(int i = 0; i < dataList.size() ; i++){
				String[] rowData = dataList.get(i);
// 2022/03/18 Ver22.03.09.02 項目数チェック条件変更				
// if(rowData[0].length() > 0 && "*".equals(rowData[0].substring(0,1))){
				if(rowData[0].length() > 0){
// 
					if(rowData.length != DATACOLUMN_NUM_SAISON){
						errorList.add((i + 1) + "行目：インポートファイルの項目数が正しくありません。(正常:" + DATACOLUMN_NUM_SAISON +"項目、現在:" + rowData.length + "項目)");
					}
				}
			}
			if (!errorList.isEmpty())
			{
				return;
			}

			//履歴データ作成
// 2022/02/18 Ver22.03.09.02 ヘッダ読み飛ばしなし			
// for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			for(int i = 0 ; i < dataList.size() ; i++){ 
// 
				String[] rowData = dataList.get(i);
				//空データならスキップ
				if ( rowData[0].length() < 1)
				{
					continue;
				}

				HoujinCardCSVUploadInfoBase rireki = null;
				rireki = new HoujinCardCSVUploadInfoBase();
				seigyoDataList.add(rireki);
				rireki.setNumber (Integer.toString(i + 1));
				rireki.setCardShubetsu ("4");
				rireki.setShainNo(rowData[1]);
				rireki.setCardBangou(rowData[3].replace("-","")); //ハイフンは除外
				rireki.setShiyousha(rowData[5] + rowData[4]);
				rireki.setKameiten(rowData[6]);

				//有効な日付かチェックしてそのまま登録(後でエラー出力)
				if(checkYYYYMD(rowData[7])) {
					rireki.setRiyoubi(rowData[7]); // yyyymmddで入力されることになっている
				}else {
					rireki.setRiyoubi(rowData[7]);
					errorList.add((i + 1) + "行目：利用日はyyyymmdd形式の有効な年月日を入力してください。");
				}

				// 金額の加工
				rowData[8] =String.valueOf((int)Double.parseDouble(rowData[8]));
				rireki.setKingaku(rowData[8]);

				//csvにはない必須項目を設定（ダミー）
				rireki.setBushoCd("0");
				rireki.setGyoushuCd("0");

				//重複レコード確認
				rireki.setDuplicateFlg(duplicateCheck(dataList, rowData, i));

				//表示用データリストに登録
				String[] dataStr = new String[9];
				for(int k = 0; k < 9 ; k++) {
					dataStr[k] = n2b(rowData[k]);
				}
				hyoujiDataList.add(dataStr);
			}
		} catch (IOException e) {
			throw new RuntimeException("法人カード使用データインポートファイル読込でエラー", e);
		}
	}
} 
	

// Ver22.06.06.00 Bizプリカ追加対応 *-	
	/**
	 * BIZPURICAフォーマット
	 */
class HoujinCardCSVUploadBIZPURICALogic extends HoujinCardCSVUploadLogic{
	/** データ列数（SAISON）*/
	static final int DATACOLUMN_NUM_BIZPURICA = 13;

	@Override
	public List<String> getHyoujiHeader() {
		List<String> hyoujiHeaderList = new ArrayList<String>();
		hyoujiHeaderList.add("取引番号");
		hyoujiHeaderList.add("企業さいふ名");
		hyoujiHeaderList.add("ユーザー姓");
		hyoujiHeaderList.add("ユーザー名");
		hyoujiHeaderList.add("社員従業員番号");
		hyoujiHeaderList.add("取引種別");
		hyoujiHeaderList.add("確定金額");
		hyoujiHeaderList.add("カード番号");
		hyoujiHeaderList.add("国内利用/海外利用");
		hyoujiHeaderList.add("(海外)現地通貨額");
		hyoujiHeaderList.add("加盟店名");
		hyoujiHeaderList.add("利用日");
		hyoujiHeaderList.add("確定日");
		return hyoujiHeaderList;
	}

	@Override
	public void read(File readfile, List<String> errorList) {
		String extension = FilenameUtils.getExtension(uploadFileFileName);
		if("csv".equalsIgnoreCase(extension) == false){
			errorList.add("csvファイル以外はアップロード不可です。");
		}
		hyoujiDataList = new ArrayList<String[]>();
		seigyoDataList = new ArrayList<HoujinCardCSVUploadInfoBase>();
		List<String[]> dataList = new ArrayList<>();

		try(BufferedReader inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(readfile)), Encoding.MS932))) {
			//データリスト作成
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray;
				columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}

			//データ項目数チェック
			for(int i = 0; i < dataList.size() ; i++){
				String[] rowData = dataList.get(i);
				if(rowData[0].length() > 0){
					if(rowData.length != DATACOLUMN_NUM_BIZPURICA){
						errorList.add((i) + "行目：インポートファイルの項目数が正しくありません。(正常:" + DATACOLUMN_NUM_BIZPURICA +"項目、現在:" + rowData.length + "項目)");
					}
				}
			}
			if (!errorList.isEmpty())
			{
				return;
			}

			//履歴データ作成
			for(int i = 1 ; i < dataList.size() ; i++){  //ヘッダ読み飛ばし
				String[] rowData = dataList.get(i);
				//空データならスキップ
				if ( rowData[0].length() < 1)
				{
					continue;
				}

				HoujinCardCSVUploadInfoBase rireki = null;
				rireki = new HoujinCardCSVUploadInfoBase();
				seigyoDataList.add(rireki);
				rireki.setNumber (Integer.toString(i));
				rireki.setCardShubetsu ("51");
				rireki.setShainNo (rowData[4]);
				rireki.setCardBangou (rowData[7].replace("-","")); // ハイフンは除外
				rireki.setShiyousha ((rowData[2] + rowData[3]));
				rireki.setKameiten (rowData[10].trim()); // 前後のスペースは除外

				//有効な日付かチェックしてそのまま登録(後でエラー出力)
				String riyoubi = rowData[11].replace("/", "");
				if(checkYYYYMD(riyoubi)) {
					rireki.setRiyoubi(riyoubi);
				} else {
					rireki.setRiyoubi(riyoubi);
					errorList.add((i) + "行目：利用日はyyyymmdd形式の有効な年月日を入力してください。");
				}

				// 金額の加工
				rowData[6] =String.valueOf((int)Double.parseDouble(rowData[6]));
				rireki.setKingaku(rowData[6]);

				//csvにはない必須項目を設定（ダミー）
				rireki.setBushoCd("0");
				rireki.setGyoushuCd("0");

				//重複レコード確認
				rireki.setDuplicateFlg(duplicateCheck(dataList, rowData, i));

				//表示用データリストに登録
				String[] dataStr = new String[13];
				for(int k = 0; k < 13 ; k++) {
					dataStr[k] = n2b(rowData[k]);
				}
				hyoujiDataList.add(dataStr);
			}
		} catch (IOException e) {
			throw new RuntimeException("法人カード使用データインポートファイル読込でエラー", e);
		}
	} 
}
// -*	