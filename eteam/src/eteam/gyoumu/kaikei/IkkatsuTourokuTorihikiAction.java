package eteam.gyoumu.kaikei;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShiwakePatternVarSettingAbstractDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShiwakePatternVarSettingDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternVarSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 取引（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuTorihikiAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数(de3)*/
	static final int DATACOLUMN_NUM_DE3 = 83 + 12; // +12（=2×6）はインボイス対応分。旧区分の追加も今後あるはずなので、一旦元の数が分かるように足し算で
	/** データ列数(SIAS)*/
	static final int DATACOLUMN_NUM_SIAS = 185 + 12;
	/** CSVファイル名 */
	static final String CSV_FILENAME = "shiwake_pattern_master.csv";

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
	/** 会計SELECTロジック */
	KaikeiCategoryLogic kcLogic;
	/** ワークフローSELECTロジック */
	WorkflowCategoryLogic wfLogic;
	/** システム管理カテゴリロジック */
	SystemKanriCategoryLogic systemLogic;
	/** 仕訳パターンマスターDAO */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 仕訳パターンVar設定Dao */
	ShiwakePatternVarSettingAbstractDao shiwakePatternVarSettingDao;

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
		int datacolumnNum;
		if(Open21Env.getVersion() == Version.DE3){
			datacolumnNum = DATACOLUMN_NUM_DE3;
		}else{
			datacolumnNum = DATACOLUMN_NUM_SIAS;
		} //データ行の項目数のチェック
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
	 * @param trList CSVファイル情報
	 */
	protected void fileContentCheck(List<IkkatsuTourokuTorihikiCSVUploadInfo> trList) {
		
		//ここでは最低限必要な項目が入力されているかのチェックにとどめ、詳細なチェックは取引登録Action側で実行させる
		
		//必須項目チェック
		for (int i = 0; i < trList.size(); i++) {
			IkkatsuTourokuTorihikiCSVUploadInfo torihiki = trList.get(i);
			String ip = "#" + torihiki.getNumber() +":";
			checkHissu(torihiki.getDenpyouKbn(), ip + "伝票区分");
			checkHissu(torihiki.getShiwakeEdano(), ip + "仕訳枝番号");
			checkHissu(torihiki.getTorihikiName(), ip + "取引名");
			checkHissu(torihiki.getDefaultHyoujiFlg(), ip + "デフォルト表示フラグ");
			checkHissu(torihiki.getHyoujiJun(), ip + "表示順");
			checkHissu(torihiki.getYuukouKigenFrom(), ip + "有効期限開始日");
			checkHissu(torihiki.getYuukouKigenTo(), ip + "有効期限終了日");
			checkHissu(torihiki.getKariKamokuCd(), ip + "借方科目コード");
			checkHissu(torihiki.getKashiKamokuCd1(), ip + "貸方科目コード１");
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < trList.size(); i++) {
			IkkatsuTourokuTorihikiCSVUploadInfo torihiki = trList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			
			//課税区分用
			List<GMap> kazeiKbnList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("kazei_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
			if(DENPYOU_KBN.SIHARAIIRAI.equals(torihiki.getDenpyouKbn())) {
				kazeiKbnList.addAll(this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("kazei_kbn_shiharaiirai").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList()));
			}
			String[] kazeiKbnDomain = EteamCommon.mapList2Arr(kazeiKbnList, "naibu_cd");
			
			checkNumber(torihiki.getShiwakeEdano(), 1, 8, ip + "仕訳枝番号"	, false);
			checkDate(torihiki.getYuukouKigenFrom(), ip + "有効期限開始日"	, false);
			checkDate(torihiki.getYuukouKigenTo(), ip + "有効期限終了日"	, false);
			checkDomain(torihiki.getKariKazeiKbn(), kazeiKbnDomain, ip + "借方課税区分（仕訳パターン）", false);
		}
		if (0 < errorList.size())
		{
			return;
		}
		
		
		//伝票区分の存在チェック
		List<GMap> infoDenpyouShubetsuList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiwake_pattern_denpyou_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		String[] denpyouShubetsuDomain = EteamCommon.mapList2Arr(infoDenpyouShubetsuList, "naibu_cd");
		for (int i = 0; i < trList.size(); i++) {
			IkkatsuTourokuTorihikiCSVUploadInfo torihiki = trList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkDomain(torihiki.getDenpyouKbn(), denpyouShubetsuDomain, ip + "伝票区分"	, false);
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
		session.remove(Sessionkey.TORIHIKI_CSV);
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
			List<IkkatsuTourokuTorihikiCSVUploadInfo> bumonList = makeCsvFileInfo(dataList);

			//CSVファイル情報をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheck(bumonList);

			//処理結果をセッションに保存します。
			IkkatsuTourokuTorihikiCSVUploadSessionInfo sessionInfo = new IkkatsuTourokuTorihikiCSVUploadSessionInfo(uploadFileFileName, bumonList, errorList);
			session.put(Sessionkey.TORIHIKI_CSV, sessionInfo);
			
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
			
			// 既存の取引を全て削除します。
			this.shiwakePatternMasterDao.deleteAllShiwakePatternMaster();
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
			List<GMap> trList;
			trList = kcLogic.loadShiwakePatternForCSV();
			
			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();
			
			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("伝票区分");
			colRecord.add("仕訳枝番号");
			colRecord.add("削除フラグ");
			colRecord.add("有効期限開始日");
			colRecord.add("有効期限終了日");
			colRecord.add("分類1");
			colRecord.add("分類2");
			colRecord.add("分類3");
			colRecord.add("取引名");
			colRecord.add("摘要フラグ");
			colRecord.add("摘要");
			colRecord.add("デフォルト表示フラグ");
			colRecord.add("交際費表示フラグ");
			colRecord.add("人数項目表示フラグ");
			colRecord.add("交際費基準額（税込）");
			colRecord.add("交際費チェック方法");
			colRecord.add("交際費チェック後登録許可");
			colRecord.add("掛けフラグ");
			colRecord.add("表示順");
			colRecord.add("社員コード連携フラグ");
			colRecord.add("財務枝番コード連携フラグ");
			
			String[] taishakuArray = { "借方", "貸方１", "貸方２", "貸方３", "貸方４", "貸方５" };
			
			boolean isSIAS = Open21Env.getVersion() == Version.SIAS;
			
			for(var taishaku : taishakuArray)
			{
				colRecord.add(taishaku + "負担部門コード");
				colRecord.add(taishaku + "科目コード");
				colRecord.add(taishaku + "科目枝番コード");
				colRecord.add(taishaku + "取引先コード");
				colRecord.add(taishaku + "プロジェクトコード");
				colRecord.add(taishaku + "セグメントコード");
				
				for(int i = 1; i <= (isSIAS ? 20 : 3); i++)
				{
					colRecord.add(taishaku + "UFコード" + (i > 10 ? "(固定)" : "") + (i % 10));
				}
				colRecord.add(taishaku + "課税区分");
				if(taishaku.contains("借方"))
				{
					colRecord.add(taishaku + "消費税率");
					colRecord.add(taishaku + "軽減税率区分");
				}
				colRecord.add(taishaku + "分離区分");
				colRecord.add(taishaku + "仕入区分");
			}
			
			csvRecords.add(colRecord);
			
			// データ
			for (GMap map : trList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("denpyou_kbn"));
				dataRecord.add(map.get("shiwake_edano"));
				dataRecord.add(map.get("delete_flg"));
				dataRecord.add(map.get("yuukou_kigen_from"));
				dataRecord.add(map.get("yuukou_kigen_to"));
				dataRecord.add(map.get("bunrui1"));
				dataRecord.add(map.get("bunrui2"));
				dataRecord.add(map.get("bunrui3"));
				dataRecord.add(map.get("torihiki_name"));
				dataRecord.add(map.get("tekiyou_flg"));
				dataRecord.add(map.get("tekiyou"));
				dataRecord.add(map.get("default_hyouji_flg"));
				dataRecord.add(map.get("kousaihi_hyouji_flg"));
				dataRecord.add(map.get("kousaihi_ninzuu_riyou_flg"));
				dataRecord.add(map.get("kousaihi_kijun_gaku") == null ? "" : ((BigDecimal)map.get("kousaihi_kijun_gaku")).toString()); //CSV出力時にカンマ付きにならないよう調整
				dataRecord.add(map.get("kousaihi_check_houhou"));
				dataRecord.add(map.get("kousaihi_check_result"));
				dataRecord.add(map.get("kake_flg"));
				dataRecord.add(map.get("hyouji_jun"));
				dataRecord.add(map.get("shain_cd_renkei_flg"));
				dataRecord.add(map.get("edaban_renkei_flg"));
				
				String[] taishakuKeyArray = { "kari", "kashi", "kashi", "kashi", "kashi", "kashi" };
				
				int side = 0;
				for(var taishakuKey : taishakuKeyArray)
				{
					String suffix = side == 0 ? "" : String.valueOf(side);
					
					dataRecord.add(map.get(taishakuKey + "_futan_bumon_cd" + suffix));
					dataRecord.add(map.get(taishakuKey + "_kamoku_cd" + suffix));
					dataRecord.add(map.get(taishakuKey + "_kamoku_edaban_cd" + suffix));
					dataRecord.add(map.get(taishakuKey + "_torihikisaki_cd" + suffix));
					dataRecord.add(map.get(taishakuKey + "_project_cd" + suffix));
					dataRecord.add(map.get(taishakuKey + "_segment_cd" + suffix));
					
					for(int i = 1; i <= (isSIAS ? 20 : 3); i++)
					{
						dataRecord.add(map.get(taishakuKey + "_uf" + (i > 10 ? "_kotei" : "" + suffix) + (i % 10) + "_cd" + suffix));
					}
					
					dataRecord.add(map.get(taishakuKey + "_kazei_kbn" + suffix));
					if(taishakuKey.contains("kari"))
					{
						dataRecord.add(map.get(taishakuKey + "_zeiritsu" + suffix));
						dataRecord.add(map.get(taishakuKey + "_keigen_zeiritsu_kbn" + suffix));
					}
					dataRecord.add(map.get(taishakuKey + "_bunri_kbn" + suffix));
					dataRecord.add(map.get(taishakuKey + "_shiire_kbn" + suffix));
					
					side++;
				}
				
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
		kcLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.shiwakePatternVarSettingDao = EteamContainer.getComponent(ShiwakePatternVarSettingDao.class, connection);
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
	protected List<IkkatsuTourokuTorihikiCSVUploadInfo> makeCsvFileInfo(List<String[]> dataList) {
		List<IkkatsuTourokuTorihikiCSVUploadInfo> trList = new ArrayList<>();
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（取引）の設定
			IkkatsuTourokuTorihikiCSVUploadInfo torihiki = new IkkatsuTourokuTorihikiCSVUploadInfo();
			trList.add(torihiki);
			int m = 0;
			boolean isSIAS = Open21Env.getVersion() == Version.SIAS;
			
			// SIASでない場合はUF4～UF固定10が空
			torihiki.setNumber(Integer.toString(i));
			torihiki.setDenpyouKbn(rowData[m++]);
			torihiki.setShiwakeEdano(rowData[m++]);
			torihiki.setDeleteFlg(rowData[m++]);
			torihiki.setYuukouKigenFrom(rowData[m++]);
			torihiki.setYuukouKigenTo(rowData[m++]);
			torihiki.setBunrui1(rowData[m++]);
			torihiki.setBunrui2(rowData[m++]);
			torihiki.setBunrui3(rowData[m++]);
			torihiki.setTorihikiName(rowData[m++]);
			torihiki.setTekiyouFlg(rowData[m++]);
			torihiki.setTekiyou(rowData[m++]);
			torihiki.setDefaultHyoujiFlg(rowData[m++]);
			torihiki.setKousaihiHyoujiFlg(rowData[m++]);
			torihiki.setKousaihiNinzuuRiyouFlg(rowData[m++]);
			torihiki.setKousaihiKijungaku(rowData[m++]);
			torihiki.setKousaihiCheckHouhou(rowData[m++]);
			torihiki.setKousaihiCheckResult(rowData[m++]);
			torihiki.setKakeFlg(rowData[m++]);
			torihiki.setHyoujiJun(rowData[m++]);
			torihiki.setShainCdRenkeiFlg(rowData[m++]);
			torihiki.setZaimuEdabanRenkeiFlg(rowData[m++]);
			torihiki.setKariFutanBumonCd(convConst(rowData[m++]));
			torihiki.setKariKamokuCd(convConst(rowData[m++]));
			torihiki.setKariKamokuEdabanCd(convConst(rowData[m++]));
			torihiki.setKariTorihikisakiCd(convConst(rowData[m++]));
			torihiki.setKariProjectCd(convConst(rowData[m++]));
			torihiki.setKariSegmentCd(convConst(rowData[m++]));
			torihiki.setKariUf1Cd(convConst(rowData[m++]));
			torihiki.setKariUf2Cd(convConst(rowData[m++]));
			torihiki.setKariUf3Cd(convConst(rowData[m++]));
			torihiki.setKariUf4Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUf5Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUf6Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUf7Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUf8Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUf9Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUf10Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei1Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei2Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei3Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei4Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei5Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei6Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei7Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei8Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei9Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariUfKotei10Cd(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKariKazeiKbn(rowData[m++]);
			torihiki.setKariZeiritsu(convConst(rowData[m++]));
			torihiki.setKariKeigenZeiritsuKbn(rowData[m++]);
			torihiki.setKariBunriKbn(rowData[m++]);
			torihiki.setKariShiireKbn(rowData[m++]);
			torihiki.setKashiFutanBumonCd1(convConst(rowData[m++]));
			torihiki.setKashiKamokuCd1(convConst(rowData[m++]));
			torihiki.setKashiKamokuEdabanCd1(convConst(rowData[m++]));
			torihiki.setKashiTorihikisakiCd1(convConst(rowData[m++]));
			torihiki.setKashiProjectCd1(convConst(rowData[m++]));
			torihiki.setKashiSegmentCd1(convConst(rowData[m++]));
			torihiki.setKashiUf1Cd1(convConst(rowData[m++]));
			torihiki.setKashiUf2Cd1(convConst(rowData[m++]));
			torihiki.setKashiUf3Cd1(convConst(rowData[m++]));
			torihiki.setKashiUf4Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf5Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf6Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf7Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf8Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf9Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf10Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei1Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei2Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei3Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei4Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei5Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei6Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei7Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei8Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei9Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei10Cd1(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiKazeiKbn1(rowData[m++]);
			torihiki.setKashiBunriKbn1(rowData[m++]);
			torihiki.setKashiShiireKbn1(rowData[m++]);
			torihiki.setKashiFutanBumonCd2(convConst(rowData[m++]));
			torihiki.setKashiKamokuCd2(convConst(rowData[m++]));
			torihiki.setKashiKamokuEdabanCd2(convConst(rowData[m++]));
			torihiki.setKashiTorihikisakiCd2(convConst(rowData[m++]));
			torihiki.setKashiProjectCd2(convConst(rowData[m++]));
			torihiki.setKashiSegmentCd2(convConst(rowData[m++]));
			torihiki.setKashiUf1Cd2(convConst(rowData[m++]));
			torihiki.setKashiUf2Cd2(convConst(rowData[m++]));
			torihiki.setKashiUf3Cd2(convConst(rowData[m++]));
			torihiki.setKashiUf4Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf5Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf6Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf7Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf8Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf9Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf10Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei1Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei2Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei3Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei4Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei5Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei6Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei7Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei8Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei9Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei10Cd2(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiKazeiKbn2(rowData[m++]);
			torihiki.setKashiBunriKbn2(rowData[m++]);
			torihiki.setKashiShiireKbn2(rowData[m++]);
			torihiki.setKashiFutanBumonCd3(convConst(rowData[m++]));
			torihiki.setKashiKamokuCd3(convConst(rowData[m++]));
			torihiki.setKashiKamokuEdabanCd3(convConst(rowData[m++]));
			torihiki.setKashiTorihikisakiCd3(convConst(rowData[m++]));
			torihiki.setKashiProjectCd3(convConst(rowData[m++]));
			torihiki.setKashiSegmentCd3(convConst(rowData[m++]));
			torihiki.setKashiUf1Cd3(convConst(rowData[m++]));
			torihiki.setKashiUf2Cd3(convConst(rowData[m++]));
			torihiki.setKashiUf3Cd3(convConst(rowData[m++]));
			torihiki.setKashiUf4Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf5Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf6Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf7Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf8Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf9Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf10Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei1Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei2Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei3Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei4Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei5Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei6Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei7Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei8Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei9Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei10Cd3(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiKazeiKbn3(rowData[m++]);
			torihiki.setKashiBunriKbn3(rowData[m++]);
			torihiki.setKashiShiireKbn3(rowData[m++]);
			torihiki.setKashiFutanBumonCd4(convConst(rowData[m++]));
			torihiki.setKashiKamokuCd4(convConst(rowData[m++]));
			torihiki.setKashiKamokuEdabanCd4(convConst(rowData[m++]));
			torihiki.setKashiTorihikisakiCd4(convConst(rowData[m++]));
			torihiki.setKashiProjectCd4(convConst(rowData[m++]));
			torihiki.setKashiSegmentCd4(convConst(rowData[m++]));
			torihiki.setKashiUf1Cd4(convConst(rowData[m++]));
			torihiki.setKashiUf2Cd4(convConst(rowData[m++]));
			torihiki.setKashiUf3Cd4(convConst(rowData[m++]));
			torihiki.setKashiUf4Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf5Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf6Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf7Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf8Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf9Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf10Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei1Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei2Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei3Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei4Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei5Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei6Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei7Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei8Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei9Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei10Cd4(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiKazeiKbn4(rowData[m++]);
			torihiki.setKashiBunriKbn4(rowData[m++]);
			torihiki.setKashiShiireKbn4(rowData[m++]);
			torihiki.setKashiFutanBumonCd5(convConst(rowData[m++]));
			torihiki.setKashiKamokuCd5(convConst(rowData[m++]));
			torihiki.setKashiKamokuEdabanCd5(convConst(rowData[m++]));
			torihiki.setKashiTorihikisakiCd5(convConst(rowData[m++]));
			torihiki.setKashiProjectCd5(convConst(rowData[m++]));
			torihiki.setKashiSegmentCd5(convConst(rowData[m++]));
			torihiki.setKashiUf1Cd5(convConst(rowData[m++]));
			torihiki.setKashiUf2Cd5(convConst(rowData[m++]));
			torihiki.setKashiUf3Cd5(convConst(rowData[m++]));
			torihiki.setKashiUf4Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf5Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf6Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf7Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf8Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf9Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUf10Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei1Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei2Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei3Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei4Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei5Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei6Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei7Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei8Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei9Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiUfKotei10Cd5(isSIAS ? convConst(rowData[m++]) : "");
			torihiki.setKashiKazeiKbn5(rowData[m++]);
			torihiki.setKashiBunriKbn5(rowData[m++]);
			torihiki.setKashiShiireKbn5(rowData[m++]);
		}
		return trList;
	}
	
	/**
	 * shiwake_pattern_var_settingの値を参照し、定数(「<FUTAN>」など)に該当する場合は該当の定数名(「任意部門」など)に変換
	 * @param convStr 変換前文字列
	 * @return 定数でなければ引数そのまま、定数ならshiwake_pattern_var_nameへの変換値
	 */
	protected String convConst(String convStr){
		List<ShiwakePatternVarSetting> constList = shiwakePatternVarSettingDao.load();
		if ( constList != null && !(constList.isEmpty()) ){
			for(var dto : constList){
				if(convStr.equals(dto.shiwakePatternVar) ){
					return  dto.shiwakePatternVarName;
				}
			}
		}
		return convStr;
	}
}
