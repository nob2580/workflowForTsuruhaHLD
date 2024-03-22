package eteam.gyoumu.kaikei;

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
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.ShozokuBumonShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShozokuBumonShiwakePatternMasterDao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 部門別取引（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonbetsuTorihikiAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数*/
	static final int DATACOLUMN_NUM = 3;
	/** CSVファイル名 */
	static final String CSV_FILENAME = "shozoku_bumon_shiwake_pattern_master.csv";

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
	TorihikiLogic myLogic;
	/** 部門ユーザ管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** ワークフローSELECTロジック */
	WorkflowCategoryLogic wfLogic;
	/** 会計SELECTロジック */
	KaikeiCategoryLogic kcLogic;
	/** システム管理カテゴリロジック */
	SystemKanriCategoryLogic systemLogic;
	/** 所属部門仕訳パターンマスターDAO */
	ShozokuBumonShiwakePatternMasterAbstractDao shozokuBumonShiwakePatternMasterDao;

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
	 * @param bumonTRList CSVファイル情報
	 */
	protected void fileContentCheck(List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> bumonTRList) {
		
		//必須項目チェック
		for (int i = 0; i < bumonTRList.size(); i++) {
			IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo bumonTR = bumonTRList.get(i);
			String ip = "#" + bumonTR.getNumber() +":";
			checkHissu(bumonTR.getBumonCd(), ip + "部門コード");
			checkHissu(bumonTR.getDenpyouKbn(), ip + "伝票区分");
			checkHissu(bumonTR.getShiwakeEdano(), ip + "仕訳枝番号");
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < bumonTRList.size(); i++) {
			IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo bumonTR = bumonTRList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkNumber(bumonTR.getShiwakeEdano(), 1, 8, ip + "仕訳枝番号"	, false);
		}
		if (0 < errorList.size())
		{
			return;
		}
		
		//部門コードの存在チェック
		/*現在有効でない部門を登録可能にするためスキップ*/
		/*for (int i = 0; i < bumonTRList.size(); i++) {
			IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo bumonTR = bumonTRList.get(i);
			GMap bumonTRMap = bumonUserLogic.selectValidShozokuBumon(bumonTR.getBumonCd());
			if(bumonTRMap == null){
				errorList.add( "#" + Integer.toString(i+1) + ":部門コードが存在しません。");
			}
		}*/
		
		//伝票区分の存在チェック
		List<GMap> infoDenpyouShubetsuList = systemLogic.loadNaibuCdSetting("shiwake_pattern_denpyou_kbn");
		String[] denpyouShubetsuDomain = EteamCommon.mapList2Arr(infoDenpyouShubetsuList, "naibu_cd");
		for (int i = 0; i < bumonTRList.size(); i++) {
			IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo bumonTR = bumonTRList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkDomain(bumonTR.getDenpyouKbn(), denpyouShubetsuDomain, ip + "伝票区分"	, false);
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
		session.remove(Sessionkey.BUMONBETSUTORIHIKI_CSV);
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
			List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> bumonList = makeCsvFileInfo(dataList);

			//CSVファイル情報をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheck(bumonList);

			//処理結果をセッションに保存します。
			IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo sessionInfo = new IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo(uploadFileFileName, bumonList, errorList);
			session.put(Sessionkey.BUMONBETSUTORIHIKI_CSV, sessionInfo);
			
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
			
			// 既存の部門別取引を全て削除します。
			this.shozokuBumonShiwakePatternMasterDao.deleteAllShozokuBumonShiwake();
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
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);
			
			//--------------------
			//データ取得
			//--------------------
			List<GMap> bumonList;
			bumonList = kcLogic.loadBumonbetsuTorihikiIchiran();
			
			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();
			
			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("部門コード");
			colRecord.add("伝票区分");
			colRecord.add("仕訳枝番号");
			csvRecords.add(colRecord);
			
			// データ
			for (GMap map : bumonList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("bumon_cd"));
				dataRecord.add(map.get("denpyou_kbn"));
				dataRecord.add(map.get("shiwake_edano"));
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
		myLogic = EteamContainer.getComponent(TorihikiLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		kcLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		this.shozokuBumonShiwakePatternMasterDao = EteamContainer.getComponent(ShozokuBumonShiwakePatternMasterDao.class, connection);
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
			throw new RuntimeException("取引CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("取引CSVファイルクローズでエラー", e);
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
	protected List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> makeCsvFileInfo(List<String[]> dataList) {
		List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> bumonTRList = new ArrayList<>();
		IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo bumonTR = null;
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（部門別取引）の設定
			bumonTR = new IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo();
			bumonTRList.add(bumonTR);
			bumonTR.setNumber (Integer.toString(i));
			bumonTR.setBumonCd(rowData[0]);
			bumonTR.setDenpyouKbn(rowData[1]);
			bumonTR.setShiwakeEdano(rowData[2]);
		}
		return bumonTRList;
	}
}
