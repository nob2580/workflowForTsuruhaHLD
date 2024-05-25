package eteam.gyoumu.userjouhoukanri;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamFileLogic;
import eteam.common.select.BumonUserKanriCategoryExtLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuUserExtAction extends IkkatsuTourokuUserAction {
	
//＜定数＞
	/** データ列数（会社切替）*/
	static final int DATACOLUMN_NUM_KAISHA_KIRIKAE = 5;
	/** CSVファイル名（会社切替） */
	static final String CSV_FILENAME_UKISHA_KIRIKAE = "kaisha_kirikae.csv";
	
//＜画面入力＞
	/** ファイルオブジェクト（会社切替） */
	protected File uploadFileKaishaKirikae;
	/** ファイル名(uploadFileに付随：会社切替) */
	protected String uploadFileKaishaKirikaeFileName;
	
	@Override
	protected void hissuCheck(int eventNum) {
		super.hissuCheck(eventNum);
		super.checkHissu(uploadFileKaishaKirikae, "CSVファイル（会社切替）");		
	}
	
	/**
	 * ファイル（会社切替）のフォーマットチェックを行います。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheckKaishaKirikae(List<String[]> dataList) {
		int datacolumnNum = DATACOLUMN_NUM_KAISHA_KIRIKAE;
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：会社切替データの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}
	/**
	 * CSVファイル情報（会社切替）をチェックします。
	 * @param shozokuBumonWariateList CSVファイル情報
	 */
	protected void fileContentCheckKaishaKirikae(List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> kaishaKirikaeList) {

		//必須項目チェック
		for (int i = 0; i < kaishaKirikaeList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoKaishaKirikae kaishaKirikae = kaishaKirikaeList.get(i);
			String ip = "#" + kaishaKirikae.getNumber() +":";
			checkHissu(kaishaKirikae.getUserId(), "会社切替：" + ip + "ユーザーID");
			checkHissu(kaishaKirikae.getSchemeCd(), "会社切替：" + ip + "スキーマコード");

			checkHissu(kaishaKirikae.getYuukouKigenFrom(), "所属部門割り当て：" + ip + "有効期限開始日");
			checkHissu(kaishaKirikae.getYuukouKigenTo(), "所属部門割り当て：" + ip + "有効期限終了日");
			checkHissu(kaishaKirikae.getHyoujiJun(), "所属部門割り当て：" + ip + "表示順");
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < kaishaKirikaeList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoKaishaKirikae kaishaKirikae = kaishaKirikaeList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkDate(kaishaKirikae.getYuukouKigenFrom(), "所属部門割り当て：" + ip + "有効期限開始日"		, false);
			checkDate(kaishaKirikae.getYuukouKigenTo(), "所属部門割り当て：" + ip + "有効期限終了日"		, false);
		}
		if (0 < errorList.size())
		{
			return;
		}
	}
	/**
	 * ダウンロードイベント（会社切替）
	 * @return ResultName
	 */
	public String downloadKaishaKirikae(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);

			//--------------------
			//データ取得
			//--------------------
			List<GMap> kaishaKirikaeList;
			kaishaKirikaeList = ((BumonUserKanriCategoryExtLogic)bumonUserLogic).loadKaishaKirikaeIchiran();

			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("ユーザーID");
			colRecord.add("スキーマコード");
			colRecord.add("有効期限開始日");
			colRecord.add("有効期限終了日");
			colRecord.add("表示順");
			csvRecords.add(colRecord);

			// データ
			for (GMap map : kaishaKirikaeList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("user_id"));
				dataRecord.add(map.get("scheme_cd"));
				dataRecord.add(formatDate(map.get("yuukou_kigen_from")));
				dataRecord.add(formatDate(map.get("yuukou_kigen_to")));
				dataRecord.add(map.get("hyouji_jun"));
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
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME_UKISHA_KIRIKAE);
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();
			} catch (IOException e) {
			   throw new RuntimeException(e);
			}
		}
		return "success";
	}
	/**
	 * アップロードイベント
	 * @return ResultName
	 */
	public String upload(){
		try(EteamConnection connection = EteamConnection.connect()){
			// 入力チェック
			hissuCheck(2);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			// ファイルサイズエラーチェック
			EteamCommon.uploadFileSizeCheck();
			//ファイル（ユーザ情報）をテキストに分解
			List<String[]> dataUserInfoList = fileDataReadUserInfo();

			//ファイル（ユーザ情報）をテキストに分解
			List<String[]> dataShozokuBumonWariateList = fileDataReadShozokuBumonWariate();
			
			//▼カスタマイズ
			//ファイル（会社切替情報）をテキストに分解
			List<String[]> dataKaishaKirikaeList = fileDataReadKaishaKirikae();
			
			//ファイル（ユーザー情報）全体のチェック。列数が規定数であるか
			fileFormatCheckUserInfo(dataUserInfoList);
			if (! errorList.isEmpty())
			{
				return "error";
			}

			//ファイル（所属部門割り当て）全体のチェック。列数が規定数であるか
			fileFormatCheckShozokuBumonWariate(dataShozokuBumonWariateList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			//▼カスタマイズ
			//ファイル（会社切替）全体のチェック。列数が規定数であるか
			fileFormatCheckKaishaKirikae(dataKaishaKirikaeList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			//CSVファイル情報（ユーザー情報）を作成します。
			List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList = makeCsvFileInfoUserInfo(dataUserInfoList);

			//CSVファイル情報（所属部門割り当て）を作成します。
			List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList = makeCsvFileInfoShozokuBumonWariate(dataShozokuBumonWariateList);
			
			//▼カスタマイズ　CSVファイル情報（会社切替）を作成します。
			List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> kaishaKirikaeList = makeCsvFileInfoKaishaKirikae(dataKaishaKirikaeList);
			
			//CSVファイル情報（ユーザー情報）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckUserInfo(userInfoList);

			//CSVファイル情報（所属部門割り当て）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckShozokuBumonWariate(shozokuBumonWariateList);
			
			//▼カスタマイズ
			//CSVファイル情報（会社切替）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckKaishaKirikae(kaishaKirikaeList);

			//▼カスタマイズ　会社切替の情報も追加
			//処理結果をセションに保存します。（ユーザー情報）
			IkkatsuTourokuUserCSVUploadSessionInfoExt sessionInfo = new IkkatsuTourokuUserCSVUploadSessionInfoExt(uploadFileUserInfoFileName, userInfoList, uploadFileShozokuBumonWariateFileName, shozokuBumonWariateList ,uploadFileKaishaKirikaeFileName, kaishaKirikaeList,errorList);

			session.put(Sessionkey.USER_CSV, sessionInfo);

			// 戻り値を返す
			return "success";
		}
	}
	
/**
 * ファイルデータ（所属部門割り当て）を読み込みます。
 * @return 所属部門割り当てCSVの１行ずつのテキスト
 */
protected List<String[]> fileDataReadKaishaKirikae() {
	List<String[]> dataList = new ArrayList<>();
	BufferedReader inFile = null;
	try {
		// ファイルデータを読み込みます。
		inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(uploadFileKaishaKirikae)), Encoding.MS932));
		String line;
		while ((line = inFile.readLine()) != null) {
			String[] columnArray = EteamCommon.splitLineWithComma(line);
			dataList.add(columnArray);
		}
	} catch (IOException e) {
		throw new RuntimeException("会社切替CSVファイル読込でエラー", e);
	} finally {
		if (null != inFile) {
			try {
				inFile.close();
			} catch (IOException e) {
				throw new RuntimeException("会社切替CSVファイルクローズでエラー", e);
			}
		}
	}
	return dataList;
}
/**
 * CSVファイル情報（会社切替）を作成します。
 * @param dataList CSV１行ずつのテキスト
 * @return ユーザー情報リスト
 */
protected List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> makeCsvFileInfoKaishaKirikae(List<String[]> dataList) {
	List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> kaishaKirikaeList = new ArrayList<>();
	IkkatsuTourokuUserCSVUploadInfoKaishaKirikae kaishaKirikae = null;
	for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
		String[] rowData = dataList.get(i);
		//CSVファイル情報（所属部門割り当て）の設定
		kaishaKirikae = new IkkatsuTourokuUserCSVUploadInfoKaishaKirikae();
		kaishaKirikaeList.add(kaishaKirikae);
		kaishaKirikae.setNumber(Integer.toString(i));
		kaishaKirikae.setUserId(rowData[0]);
		kaishaKirikae.setSchemeCd(rowData[1]);
		kaishaKirikae.setYuukouKigenFrom(rowData[2]);
		kaishaKirikae.setYuukouKigenTo(rowData[3]);
		kaishaKirikae.setHyoujiJun(rowData[4]);
	}
	return kaishaKirikaeList;
}
}
