package eteam.gyoumu.bumonkanri;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamFileLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 部門（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数*/
	static final int DATACOLUMN_NUM = 6;
	/** CSVファイル名 */
	static final String CSV_FILENAME = "shozoku_bumon.csv";

//＜画面入力＞
	/** ファイルオブジェクト */
	protected File uploadFile;
	/** ファイル名(uploadFileに付随) */
	protected String uploadFileFileName;
	
	//CSV出力用
	/** ダウンロードファイル用 */
	InputStream inputStream;
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** EXCELファイルの長さ */
	long contentLength;
	
//＜部品＞
	/** マスターデータ管理ロジック */
	BumonKanriLogic myLogic;
	/** 部門ユーザ管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {
		super.checkHissu(uploadFile, "CSVファイル");
	}

	/**
	 * ファイルのフォーマットチェックを行います。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheck(List<String[]> dataList) {
		int datacolumnNum = DATACOLUMN_NUM;
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：データの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}

	/**
	 * CSVファイル情報をチェックします。
	 * @param bumonList CSVファイル情報
	 */
	protected void fileContentCheck(List<IkkatsuTourokuBumonCSVUploadInfo> bumonList) {
		
		//必須項目チェック
		for (int i = 0; i < bumonList.size(); i++) {
			IkkatsuTourokuBumonCSVUploadInfo bumon = bumonList.get(i);
			String ip = "#" + bumon.getNumber() +":";
			checkHissu(bumon.getBumonCd(), ip + "部門コード");
			checkHissu(bumon.getBumonName(), ip + "部門名");
			checkHissu(bumon.getOyaBumonCd(), ip + "親部門コード");
			checkHissu(bumon.getYuukouKigenFrom(), ip + "有効期限開始日");
			checkHissu(bumon.getYuukouKigenTo(), ip + "有効期限終了日");
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < bumonList.size(); i++) {
			IkkatsuTourokuBumonCSVUploadInfo bumon = bumonList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			int bumonCdMaxLength = Integer.parseInt(setting.bumonCdKetasuu());
			checkString(bumon.getBumonCd(),       1, bumonCdMaxLength,  ip + "部門コード",  false);
			checkDate(bumon.getYuukouKigenFrom(), ip + "有効期限開始日"		, false);
			checkDate(bumon.getYuukouKigenTo(), ip + "有効期限終了日"		, false);
		}
		if (0 < errorList.size())
		{
			return;
		}
		
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		session.remove(Sessionkey.BUMON_CSV);
		return "success";
	}

	/**
	 * アップロードイベント
	 * @return ResultName
	 */
	public String upload(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);
			// 入力チェック
			hissuCheck(2);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			// ファイルサイズエラーチェック
			EteamCommon.uploadFileSizeCheck();
			//ファイルをテキストに分解
			List<String[]> dataList = fileDataRead();
			
			//ファイル全体のチェック。列数が規定数である。
			fileFormatCheck(dataList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			//CSVファイル情報を作成します。
			List<IkkatsuTourokuBumonCSVUploadInfo> bumonList = makeCsvFileInfo(dataList);

			//CSVファイル情報をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheck(bumonList);

			//処理結果をセションに保存します。
			IkkatsuTourokuBumonCSVUploadSessionInfo sessionInfo = new IkkatsuTourokuBumonCSVUploadSessionInfo(uploadFileFileName, bumonList, errorList);
			session.put(Sessionkey.BUMON_CSV, sessionInfo);
			
			// 戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 一括削除イベント
	 * @return ResultName
	 */
	public String sakujo(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);
			
			// デフォルト部門以外（0000:全社以外）の所属部門を全て削除します。
			myLogic.shozokuBumonDelete();
			connection.commit();
			
			// 戻り値を返す
			return "success";
		}
	}
	
	/**
	 * ダウンロードイベント
	 * @return ResultName
	 */
	public String download(){
		try(EteamConnection connection = EteamConnection.connect();){
			setConnection(connection);
			
			//--------------------
			//データ取得
			//--------------------
			List<GMap> bumonList;
			bumonList = bumonUserLogic.loadBumonIchiran();
			
			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();
			
			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("部門コード");
			colRecord.add("部門名");
			colRecord.add("親所属部門コード");
			colRecord.add("有効期限開始日");
			colRecord.add("有効期限終了日");
			colRecord.add("セキュリティパターン");
			csvRecords.add(colRecord);
			
			// データ
			for (GMap map : bumonList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("bumon_cd"));
				dataRecord.add(map.get("bumon_name"));
				dataRecord.add(map.get("oya_bumon_cd"));
				dataRecord.add(formatDate(map.get("yuukou_kigen_from")));
				dataRecord.add(formatDate(map.get("yuukou_kigen_to")));
				dataRecord.add(map.get("security_pattern"));
				csvRecords.add(dataRecord);
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
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(BumonKanriLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
	}
	
	/**
	 * ファイルデータを読み込みます。
	 * @return CSVの１行ずつのテキスト
	 */
	protected List<String[]> fileDataRead() {
		List<String[]> dataList = new ArrayList<>();
		BufferedReader inFile = null;
		try {
			// ファイルデータを読み込みます。
			inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(uploadFile)), Encoding.MS932));
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("部門CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("部門CSVファイルクローズでエラー", e);
				}
			}
		}
		return dataList;
	}

	/**
	 * CSVファイル情報を作成します。
	 * @param dataList CSV１行ずつのテキスト
	 * @return 伝票リスト
	 */
	protected List<IkkatsuTourokuBumonCSVUploadInfo> makeCsvFileInfo(List<String[]> dataList) {
		List<IkkatsuTourokuBumonCSVUploadInfo> bumonList = new ArrayList<>();
		IkkatsuTourokuBumonCSVUploadInfo bumon = null;
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（部門）の設定
			bumon = new IkkatsuTourokuBumonCSVUploadInfo();
			bumonList.add(bumon);
			bumon.setNumber (Integer.toString(i));
			bumon.setBumonCd(rowData[0]);
			bumon.setBumonName(rowData[1]);
			bumon.setOyaBumonCd(rowData[2]);
			bumon.setYuukouKigenFrom(rowData[3]);
			bumon.setYuukouKigenTo(rowData[4]);
			bumon.setSecurityPattern(rowData[5]);
		}
		return bumonList;
	}
}
