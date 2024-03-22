package eteam.gyoumu.workflow;

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
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamFileLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 部門推奨ルート一括登録（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonSuishouRouteAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数（部門推奨ルート(親)）*/
	static final int DATACOLUMN_NUM_OYA_INFO = 9;
	/** データ列数（部門推奨ルート(子)）*/
	static final int DATACOLUMN_NUM_KO_INFO = 8;
	/** CSVファイル名（部門推奨ルート(親)） */
	static final String CSV_FILENAME_OYA_INFO = "bumon_suishou_route_oya.csv";
	/** CSVファイル名（部門推奨ルート(子)） */
	static final String CSV_FILENAME_KO_INFO  = "bumon_suishou_route_ko.csv";

//＜画面入力＞
	/** ファイルオブジェクト（部門推奨ルート(親)） */
	protected File uploadFileOyaInfo;
	/** ファイルオブジェクト（部門推奨ルート(子)） */
	protected File uploadFileKoInfo;
	/** ファイル名(uploadFileに付随：部門推奨ルート(親)) */
	protected String uploadFileOyaInfoFileName;
	/** ファイル名(uploadFileに付随：部門推奨ルート(子)) */
	protected String uploadFileKoInfoFileName;
	
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
	/** 部門推奨ルート変更管理ロジック */
	BumonSuishouRouteHenkouLogic myLogic;
	/** ワークフローSELECT */
	WorkflowCategoryLogic wfLogic;
	/** 部門ユーザ管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** 会計カテゴリーロジック */
	KaikeiCategoryLogic kaikeiLogic;
	/** 起票ナビカテゴリーロジック */
	KihyouNaviCategoryLogic navi;
	
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {
		super.checkHissu(uploadFileOyaInfo, "CSVファイル（部門推奨ルート(親)）");
		super.checkHissu(uploadFileKoInfo, "CSVファイル（部門推奨ルート(子)）");
	}

	/**
	 * ファイル（部門推奨ルート(親)）のフォーマットチェックを行います。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheckOyaInfo(List<String[]> dataList) {
		int datacolumnNum = DATACOLUMN_NUM_OYA_INFO;
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：部門推奨ルート(親)データの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}
	
	/**
	 * ファイル（部門推奨ルート(子)）のフォーマットチェックを行います。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheckKoInfo(List<String[]> dataList) {
		int datacolumnNum = DATACOLUMN_NUM_KO_INFO;
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：部門推奨ルート(子)データの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}
	
	/**
	 * CSVファイル情報（部門推奨ルート(親)）をチェックします。
	 * @param routeOyaList CSVファイル情報
	 */
	protected void fileContentCheckOyaInfo(List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> routeOyaList) {
		
		//必須項目チェック
		for (int i = 0; i < routeOyaList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya oyaInfo = routeOyaList.get(i);
			String ip = "#" + oyaInfo.getNumber() +":";
			checkHissu(oyaInfo.getDenpyouKbn(), "部門推奨ルート(親)：" + ip + "伝票区分");
			checkHissu(oyaInfo.getBumonCd(), "部門推奨ルート(親)：" + ip + "部門コード");
			checkHissu(oyaInfo.getEdaNo(), "部門推奨ルート(親)：" + ip + "枝番号");
			checkHissu(oyaInfo.getDefaultFlg(), "部門推奨ルート(親)：" + ip + "デフォルト設定");
			if(navi.routeBunkiAri(oyaInfo.getDenpyouKbn())){
				checkHissu(oyaInfo.getKingakuFrom(), "部門推奨ルート(親)：" + ip + "金額開始");
				checkHissu(oyaInfo.getKingakuTo(), "部門推奨ルート(親)：" + ip + "金額終了");
			}
			checkHissu(oyaInfo.getYuukouKigenFrom(), "部門推奨ルート(親)：" + ip + "有効期限開始日");
			checkHissu(oyaInfo.getYuukouKigenTo(), "部門推奨ルート(親)：" + ip + "有効期限終了日");
		}
		if (0 < errorList.size())
		{
			return;
		}
		
		
		//形式のチェック
		for (int i = 0; i < routeOyaList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya oyaInfo = routeOyaList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkNumber(oyaInfo.getEdaNo(), 1, 8, "部門推奨ルート(親)：" + ip + "枝番号"	, false);
			checkDomain(oyaInfo.getDefaultFlg(),Domain.FLG, "部門推奨ルート(親)：" + ip + "デフォルト設定", false);
			for(int j = 0; j < oyaInfo.getShiwakeEdaNo().length ; j++){
				checkNumberOver1(oyaInfo.getShiwakeEdaNo()[j], 1 , 8, "部門推奨ルート(親)：" + ip + "取引コード", false);
			}
			checkKingakuOver0 (oyaInfo.getKingakuFrom(), "部門推奨ルート(親)：" + ip + "金額開始", false);
			checkKingakuOver0 (oyaInfo.getKingakuTo(), "部門推奨ルート(親)：" + ip + "金額終了", false);
			checkDate(oyaInfo.getYuukouKigenFrom(), "部門推奨ルート(親)：" + ip + "有効期限開始日"	, false);
			checkDate(oyaInfo.getYuukouKigenTo(), "部門推奨ルート(親)：" + ip + "有効期限終了日"	, false);
		}
		if (0 < errorList.size())
		{
			return;
		}
		
		//存在チェック
		for (int i = 0; i < routeOyaList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya oyaInfo = routeOyaList.get(i);
			//部門コードの存在チェック
			GMap bcMap = bumonUserLogic.selectValidShozokuBumon(oyaInfo.getBumonCd(),toDate(oyaInfo.getYuukouKigenFrom()));
			if(bcMap == null){
				errorList.add( "部門推奨ルート(親)：#" + Integer.toString(i+1) + ":部門コードが存在しません。");
			}
			
			//伝票区分の存在チェック
			if ("A901".equals(oyaInfo.getDenpyouKbn()))
			{
				continue;
			} //海外その他精算は除外
			GMap dkMap = wfLogic.selectDenpyouShubetu(oyaInfo.getDenpyouKbn());
			if(dkMap == null){
				errorList.add( "部門推奨ルート(親)：#" + Integer.toString(i+1) + ":伝票区分が存在しません。");
			}
			
			//仕訳枝番号の存在チェック
			for(int j = 0; j < oyaInfo.getShiwakeEdaNo().length ; j++){
				if(!"".equals(oyaInfo.getShiwakeEdaNo()[j])){
					GMap seMap = kaikeiLogic.findShiwakePattern(oyaInfo.getDenpyouKbn(), Integer.parseInt(oyaInfo.getShiwakeEdaNo()[j]));
					if(seMap == null){
						errorList.add( "部門推奨ルート(親)：#" + Integer.toString(i+1) + ":取引コードが存在しません。");
					}
				}
			}
		}
	}
	
	/**
	 * CSVファイル情報（部門推奨ルート(子)）をチェックします。
	 * @param routeKoList CSVファイル情報
	 */
	protected void fileContentCheckKoInfo(List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> routeKoList) {
		
		//必須項目チェック
		for (int i = 0; i < routeKoList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo = routeKoList.get(i);
			String ip = "#" + koInfo.getNumber() +":";
			checkHissu(koInfo.getDenpyouKbn(), "部門推奨ルート(子)：" + ip + "伝票区分");
			checkHissu(koInfo.getBumonCd(), "部門推奨ルート(子)：" + ip + "部門コード");
			checkHissu(koInfo.getEdaNo(), "部門推奨ルート(子)：" + ip + "枝番号");
			checkHissu(koInfo.getEdaedaNo(), "部門推奨ルート(子)：" + ip + "枝枝番号");
			//checkHissu(koInfo.getBumonRoleId(), "部門推奨ルート(子)：" + ip + "部門ロールID");
			checkHissu(koInfo.getShouninhoriKengenNo(), "部門推奨ルート(子)：" + ip + "承認処理権限番号");
			//checkHissu(koInfo.getGougiPatternNo(), "部門推奨ルート(子)：" + ip + "合議パターン番号");
			//checkHissu(koInfo.getGougiEdano(), "部門推奨ルート(子)：" + ip + "合議枝番");
			
			//部門ロールIDがあるなら合議パターン番号・合議枝番がブランクであること
			if( !isEmpty(koInfo.getBumonRoleId()) ){
				if( !isEmpty(koInfo.getGougiPatternNo()) || !isEmpty(koInfo.getGougiEdano()) ){
					errorList.add( "部門推奨ルート(子)：" + ip + "部門ロールID指定時は、合議パターン番号・合議枝番は設定できません。");
				}
			}else{
				//部門ロールIDがないなら合議パターン番号・合議枝番が両方存在すること
				checkHissu(koInfo.getGougiPatternNo(), "部門推奨ルート(子)：" + ip + "合議パターン番号");
				checkHissu(koInfo.getGougiEdano(), "部門推奨ルート(子)：" + ip + "合議枝番");
			}
		}
		if (0 < errorList.size())
		{
			return;
		}
		
		
		//形式のチェック
		for (int i = 0; i < routeKoList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo = routeKoList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkNumber(koInfo.getEdaNo(), 1, 8, "部門推奨ルート(子)：" + ip + "枝番号"	, false);
			checkNumber(koInfo.getEdaedaNo(), 1, 8, "部門推奨ルート(子)：" + ip + "枝枝番号"	, false);
			checkNumber(koInfo.getShouninhoriKengenNo(), 1, 8, "部門推奨ルート(子)：" + ip + "承認処理権限番号"	, false);
		}
		if (0 < errorList.size())
		{
			return;
		}
		
		//部門コードの存在チェック
		for (int i = 0; i < routeKoList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo = routeKoList.get(i);
			GMap bcMap = bumonUserLogic.selectValidShozokuBumon(koInfo.getBumonCd());
			if(bcMap == null){
				errorList.add( "部門推奨ルート(子)：#" + Integer.toString(i+1) + ":部門コードが存在しません。");
			}
		}
		
		//伝票区分の存在チェック
		for (int i = 0; i < routeKoList.size(); i++) {
			IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo = routeKoList.get(i);
			if ("A901".equals(koInfo.getDenpyouKbn()))
			{
				continue;
			} //海外その他精算は除外
			GMap dkMap = wfLogic.selectDenpyouShubetu(koInfo.getDenpyouKbn());
			if(dkMap == null){
				errorList.add( "部門推奨ルート(子)：#" + Integer.toString(i+1) + ":伝票区分が存在しません。");
			}
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
		session.remove(Sessionkey.ROUTE_CSV);
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
			//ファイル（部門推奨ルート(親)）をテキストに分解
			List<String[]> dataOyaInfoList = fileDataReadOyaInfo();
			
			//ファイル（部門推奨ルート(子)）をテキストに分解
			List<String[]> dataKoInfoList = fileDataReadKoInfo();
			
			//ファイル（部門推奨ルート(親)）全体のチェック。列数が規定数であるか
			fileFormatCheckOyaInfo(dataOyaInfoList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			//ファイル（部門推奨ルート(子)）全体のチェック。列数が規定数であるか
			fileFormatCheckKoInfo(dataKoInfoList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			
			//CSVファイル情報（部門推奨ルート(親)）を作成します。
			List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> oyaInfoList = makeCsvFileInfoBumonSuishouRouteOya(dataOyaInfoList);
			
			//CSVファイル情報（部門推奨ルート(子)）を作成します。
			List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> koInfoList = makeCsvFileInfoBumonSuishouRouteKo(dataKoInfoList);
			
			
			//CSVファイル情報（部門推奨ルート(親)）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckOyaInfo(oyaInfoList);
			
			//CSVファイル情報（部門推奨ルート(子)）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckKoInfo(koInfoList);
			
			//処理結果をセッションに保存します。（部門推奨ルート）
			IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo sessionInfo = new IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo(uploadFileOyaInfoFileName, oyaInfoList, uploadFileKoInfoFileName, koInfoList ,errorList);
			
			session.put(Sessionkey.ROUTE_CSV, sessionInfo);
			
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
			
			// 部門推奨ルート情報を全て削除します。
			myLogic.deleteAllBumonSuishoRoute();
			
			connection.commit();
			
			// 戻り値を返す
			return "success";
		}
	}
	
	/**
	 * ダウンロードイベント（部門推奨ルート(親)）
	 * @return ResultName
	 */
	public String downloadOyaInfo(){
		try(EteamConnection connection = EteamConnection.connect()){
			
			//TODO 部門推奨ルート(親)側のデータを取る
			setConnection(connection);
			
			//--------------------
			//データ取得
			//--------------------
			List<GMap> oyaList;
			oyaList = wfLogic.loadBumonSuishouRouteOya();
			
			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();
			
			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("伝票区分");
			colRecord.add("部門コード");
			colRecord.add("枝番号");
			colRecord.add("デフォルト設定");
			colRecord.add("取引コード");
			colRecord.add("金額開始");
			colRecord.add("金額終了");
			colRecord.add("有効期限開始日");
			colRecord.add("有効期限終了日");
			csvRecords.add(colRecord);
			
			// データ
			for (GMap map : oyaList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("denpyou_kbn"));
				dataRecord.add(map.get("bumon_cd"));
				dataRecord.add(map.get("edano"));
				dataRecord.add(map.get("default_flg"));
				String tmpShiwakeEdaNo = (null==map.get("shiwake_edano"))? "": map.get("shiwake_edano").toString();
				dataRecord.add(tmpShiwakeEdaNo.replace("{", "").replace(",", "|").replace("}", ""));
				dataRecord.add(map.get("kingaku_from"));
				dataRecord.add(map.get("kingaku_to"));
				dataRecord.add(formatDate(map.get("yuukou_kigen_from")));
				dataRecord.add(formatDate(map.get("yuukou_kigen_to")));
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
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME_OYA_INFO);
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();
			} catch (IOException e) {
			   throw new RuntimeException(e);
			}
		}
		return "success";
	}
	
	/**
	 * ダウンロードイベント（部門推奨ルート(子)）
	 * @return ResultName
	 */
	public String downloadKoInfo(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);
			
			//--------------------
			//データ取得
			//--------------------
			List<GMap> koList;
			koList = wfLogic.loadBumonSuishouRouteKo();
			
			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();
			
			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("伝票区分");
			colRecord.add("部門コード");
			colRecord.add("枝番号");
			colRecord.add("枝々番号");
			colRecord.add("部門ロールID");
			colRecord.add("承認処理権限番号");
			colRecord.add("合議パターン番号");
			colRecord.add("合議枝番");
			csvRecords.add(colRecord);
			
			// データ
			for (GMap map : koList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("denpyou_kbn"));
				dataRecord.add(map.get("bumon_cd"));
				dataRecord.add(map.get("edano"));
				dataRecord.add(map.get("edaedano"));
				dataRecord.add(map.get("bumon_role_id"));
				dataRecord.add(map.get("shounin_shori_kengen_no"));
				dataRecord.add(map.get("gougi_pattern_no"));
				dataRecord.add(map.get("gougi_edano"));
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
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME_KO_INFO);
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
		myLogic = EteamContainer.getComponent(BumonSuishouRouteHenkouLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		
	}
	
	/**
	 * ファイルデータ（部門推奨ルート(親)）を読み込みます。
	 * @return 部門推奨ルート(親)情報CSVの１行ずつのテキスト
	 */
	protected List<String[]> fileDataReadOyaInfo() {
		List<String[]> dataList = new ArrayList<>();
		BufferedReader inFile = null;
		try {
			// ファイルデータを読み込みます。
			inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(uploadFileOyaInfo)), Encoding.MS932));
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("部門推奨ルート(親)CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("部門推奨ルート(親)CSVファイルクローズでエラー", e);
				}
			}
		}
		return dataList;
	}
	
	/**
	 * ファイルデータ（部門推奨ルート(子)）を読み込みます。
	 * @return 部門推奨ルート(子)情報CSVの１行ずつのテキスト
	 */
	protected List<String[]> fileDataReadKoInfo() {
		List<String[]> dataList = new ArrayList<>();
		BufferedReader inFile = null;
		try {
			// ファイルデータを読み込みます。
			inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(uploadFileKoInfo)), Encoding.MS932));
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("部門推奨ルート(子)情報CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("部門推奨ルート(子)情報CSVファイルクローズでエラー", e);
				}
			}
		}
		return dataList;
	}
	
	/**
	 * CSVファイル情報（部門推奨ルート(親)）を作成します。
	 * @param dataList CSV１行ずつのテキスト
	 * @return 部門推奨ルート(親)リスト
	 */
	protected List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> makeCsvFileInfoBumonSuishouRouteOya(List<String[]> dataList) {
		List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> BumonSuishouRouteOyaList = new ArrayList<>();
		IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya oyaInfo = null;
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（部門推奨ルート(親)）の設定
			oyaInfo = new IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya();
			BumonSuishouRouteOyaList.add(oyaInfo);
			oyaInfo.setNumber(Integer.toString(i));
			oyaInfo.setDenpyouKbn(rowData[0]);
			oyaInfo.setBumonCd(rowData[1]);
			oyaInfo.setEdaNo(rowData[2]);
			oyaInfo.setDefaultFlg(rowData[3]);
			oyaInfo.setShiwakeEdaNo(rowData[4].split("\\|"));
			oyaInfo.setShiwakeEdaNoHyouji(rowData[4]);
			oyaInfo.setKingakuFrom(rowData[5]);
			oyaInfo.setKingakuTo(rowData[6]);
			oyaInfo.setYuukouKigenFrom(rowData[7]);
			oyaInfo.setYuukouKigenTo(rowData[8]);
		}
		return BumonSuishouRouteOyaList;
	}
	
	/**
	 * CSVファイル情報（部門推奨ルート(子)）を作成します。
	 * @param dataList CSV１行ずつのテキスト
	 * @return 部門推奨ルート(子)リスト
	 */
	protected List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> makeCsvFileInfoBumonSuishouRouteKo(List<String[]> dataList) {
		List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> BumonSuishouRouteKoList = new ArrayList<>();
		IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo = null;
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（所属部門割り当て）の設定
			koInfo = new IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo();
			BumonSuishouRouteKoList.add(koInfo);
			koInfo.setNumber(Integer.toString(i));
			koInfo.setDenpyouKbn(rowData[0]);
			koInfo.setBumonCd(rowData[1]);
			koInfo.setEdaNo(rowData[2]);
			koInfo.setEdaedaNo(rowData[3]);
			koInfo.setBumonRoleId(rowData[4]);
			koInfo.setShouninhoriKengenNo(rowData[5]);
			koInfo.setGougiPatternNo(rowData[6]);
			koInfo.setGougiEdano(rowData[7]);
		}
		return BumonSuishouRouteKoList;
	}
	
}
