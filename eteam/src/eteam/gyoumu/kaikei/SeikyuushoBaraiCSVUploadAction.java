package eteam.gyoumu.kaikei;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 請求書払い（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class SeikyuushoBaraiCSVUploadAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数(de3)*/
	static final int DATACOLUMN_NUM_DE3 = 20;//16→20
	/** データ列数(SIAS)*/
	static final int DATACOLUMN_NUM_SIAS = 37;//33→35
	
//＜画面入力＞
	/** ファイルオブジェクト */
	protected File uploadFile;
	/** ファイル名(uploadFileに付随) */
	protected String uploadFileFileName;

//＜画面制御情報＞
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SEIKYUUSHO_BARAI);
	/** 予算執行対象 */
	String yosanShikkouTaishou;
	
	/** 入力方式 */
	String nyuuryokuHoushiki = setting.zeiDefaultA003();
	
	/** インボイス開始フラグ */
	String invoiceStartFlg;
	/** インボイス設定フラグ */
	String invoiceSetteiFlg = setting.invoiceSetteiFlg();
	
//＜部品＞
	/** マスター管理ロジック */
	protected MasterKanriCategoryLogic masterLogic;
	/** 会計共通ロジック */
	protected KaikeiCommonLogic kaikeiLogic;
	/** 起票ナビカテゴリーロジック*/
	protected KihyouNaviCategoryLogic kihyouLogic;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** WorkflowEventControlLogic */
	protected WorkflowEventControlLogic wfEventLogic;
	/** 会計ロジック */
	protected KaikeiCategoryLogic kLogic;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {
		super.checkHissu(uploadFile, "CSVファイル");
	}

	/**
	 * ファイルのフォーマット、伝票IDの昇順チェックを行います。
	 * 予算執行対象が支出依頼の場合、CSVのデータ列数が1列追加されます。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheck(List<String[]> dataList) {
		int datacolumnNum;
		if(Open21Env.getVersion() == Version.DE3){
			datacolumnNum = DATACOLUMN_NUM_DE3;
			if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
				datacolumnNum = datacolumnNum + 1;
			}
		}else{
			datacolumnNum = DATACOLUMN_NUM_SIAS;
			if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
				datacolumnNum = datacolumnNum + 1;
			}
		}
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
	 * @param denpyouList CSVファイル情報
	 */
	protected void fileContentCheck(List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList) {
		List<GMap> zeiritsuList = masterLogic.loadshouhizeiritsu();
		String[] zeiritsuDomain = EteamCommon.mapList2Arr(zeiritsuList, "zeiritsu");
		List<GMap> jigyoushaKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		String[] jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");
		List<GMap> bunriKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		String[] bunriKbnDomain = EteamCommon.mapList2Arr(bunriKbnList, "naibu_cd");
		List<GMap> shiireKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		String[] shiireKbnDomain = EteamCommon.mapList2Arr(shiireKbnList, "naibu_cd");
		List<GMap> nyuryokuHoushikiList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("nyuryoku_flg").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		String[] nyuryokuHoushikiDomain = EteamCommon.mapList2Arr(nyuryokuHoushikiList, "naibu_cd");
		List<NaibuCdSetting> invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		String[] invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		
		//必須項目チェック
		for (int i = 0; i < denpyouList.size(); i++) {
			SeikyuushoBaraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
			String ip = "#" + denpyou.getNumber();
			checkHissu(denpyou.getDenpyouNo(), "伝票No."		+ ip);
			if("1".equals(ks.shiharaiKigen.getHissuFlgS())){
				checkHissu(denpyou.getShiharaiKigen(), "支払期限"		+ ip);
			}
			checkHissu(denpyou.getKianDenpyouId(), "起案伝票ID" + ip);
			List<SeikyuushoBaraiCSVUploadMeisaiInfo> meisaiList = denpyou.getMeisaiList();
			for (int j = 0; j < meisaiList.size(); j++) {
				SeikyuushoBaraiCSVUploadMeisaiInfo meisai = meisaiList.get(j);
				ip = "#" + meisai.getNumber();
				checkHissu(meisai.getShiwakeEdaNo(), "仕訳枝番号"	+ ip);
				checkHissu(meisai.getTekiyou(), "摘要"			+ ip);
				if("0".equals(nyuuryokuHoushiki)) {
					checkHissu(meisai.getShiharaiKingaku(),	"支払金額"+ ip);
				}else {
					checkHissu(meisai.getZeinukiKingaku(), "税抜金額"+ ip);
					checkHissu(meisai.getShouhizeigaku(), "消費税額"+ ip);
				}
				boolean isKazei = true;
				if(meisai.getShiwakeEdaNo().matches("^[0-9]{1,10}$")){
					GMap mp = kLogic.findShiwakePattern(DENPYOU_KBN.SEIKYUUSHO_BARAI, Integer.parseInt(meisai.getShiwakeEdaNo()));
					if(mp != null){
						isKazei = List.of("001", "002", "011", "012", "013", "014").contains(mp.get("kari_kazei_kbn"));
					}
				}
				if(isKazei) {
					checkHissu(meisai.getZeiritsu(), "消費税率"        + ip);
					checkHissu(meisai.getKeigenZeiritsuKbn(), "軽減税率区分"    + ip);
				}
			}
//			if("1".equals(invoiceStartFlg) && "1".equals(invoiceSetteiFlg)) {
//				checkHissu(denpyou.getInvoiceDenpyou(),"インボイス対応伝票"+ip);
//			}
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < denpyouList.size(); i++) {
			SeikyuushoBaraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
			String ip = "#" + denpyou.getNumber();
			// 項目									//項目名								//キー項目？
			checkNumber (denpyou.getDenpyouNo(), 1, 3, "伝票No."						+ ip, false);//必須チェック
			if(Open21Env.getVersion() == Version.SIAS){
				checkString(denpyou.getHf1Cd(), 1, 20, hfUfSeigyo.getHf1Name() + ip, false);
				checkString(denpyou.getHf2Cd(), 1, 20, hfUfSeigyo.getHf2Name() + ip, false);
				checkString(denpyou.getHf3Cd(), 1, 20, hfUfSeigyo.getHf3Name() + ip, false);
				checkString(denpyou.getHf4Cd(), 1, 20, hfUfSeigyo.getHf4Name() + ip, false);
				checkString(denpyou.getHf5Cd(), 1, 20, hfUfSeigyo.getHf5Name() + ip, false);
				checkString(denpyou.getHf6Cd(), 1, 20, hfUfSeigyo.getHf6Name() + ip, false);
				checkString(denpyou.getHf7Cd(), 1, 20, hfUfSeigyo.getHf7Name() + ip, false);
				checkString(denpyou.getHf8Cd(), 1, 20, hfUfSeigyo.getHf8Name() + ip, false);
				checkString(denpyou.getHf9Cd(), 1, 20, hfUfSeigyo.getHf9Name() + ip, false);
				checkString(denpyou.getHf10Cd(), 1, 20, hfUfSeigyo.getHf10Name() + ip, false);
			}
			checkDate (denpyou.getShiharaiKigen(), "支払期限"						+ ip, false);
			if(!(denpyou.getKianDenpyouId().equals("0"))) {
				checkString(denpyou.getKianDenpyouId(), 19, 19, "起案伝票ID" + ip , false);
			}
			checkDomain(denpyou.getInvoiceDenpyou(), invoiceDenpyouDomain, "インボイス伝票" + ip , false); //インボイス対応

			List<SeikyuushoBaraiCSVUploadMeisaiInfo> meisaiList = denpyou.getMeisaiList();
			for (int j = 0; j < meisaiList.size(); j++) {
				SeikyuushoBaraiCSVUploadMeisaiInfo meisai = meisaiList.get(j);
				ip = "#" + meisai.getNumber();
				// 項目									//項目名							//キー項目？
				checkNumber(meisai.getShiwakeEdaNo(), 1, 10, "仕訳枝番号" + ip, false);
				boolean isKazei = true;
				if(meisai.getShiwakeEdaNo().matches("^[0-9]{1,10}$")){
					GMap mp = kLogic.findShiwakePattern(DENPYOU_KBN.SEIKYUUSHO_BARAI, Integer.parseInt(meisai.getShiwakeEdaNo()));
					if(mp == null){
						errorList.add("仕訳枝番号" + ip + "は存在しません。");
					}else {
						isKazei = List.of("001", "002", "011", "012", "013", "014").contains(mp.get("kari_kazei_kbn")); 
					}
					
				}
				checkString(meisai.getKamokuEdabanCd(), 1, 12, "勘定科目枝番コード" + ip, false);
				checkString(meisai.getFutanBumonCd(), 1, 8, "負担部門コード" + ip, false);
				checkString(meisai.getTorihikisakiCd(), 1, 12, "取引先コード" + ip, false);
				checkString(meisai.getTekiyou(), 1, 60, "摘要" + ip, false);
				checkKingakuOver1(meisai.getShiharaiKingaku(), "支払金額" + ip, false);
				checkString(meisai.getUf1Cd(), 1, 20, hfUfSeigyo.getUf1Name() + ip, false);
				checkString(meisai.getUf2Cd(), 1, 20, hfUfSeigyo.getUf2Name() + ip, false);
				checkString(meisai.getUf3Cd(), 1, 20, hfUfSeigyo.getUf3Name() + ip, false);
				if(Open21Env.getVersion() == Version.SIAS){
					checkString(meisai.getUf4Cd(), 1, 20, hfUfSeigyo.getUf4Name() + ip, false);
					checkString(meisai.getUf5Cd(), 1, 20, hfUfSeigyo.getUf5Name() + ip, false);
					checkString(meisai.getUf6Cd(), 1, 20, hfUfSeigyo.getUf6Name() + ip, false);
					checkString(meisai.getUf7Cd(), 1, 20, hfUfSeigyo.getUf7Name() + ip, false);
					checkString(meisai.getUf8Cd(), 1, 20, hfUfSeigyo.getUf8Name() + ip, false);
					checkString(meisai.getUf9Cd(), 1, 20, hfUfSeigyo.getUf9Name() + ip, false);
					checkString(meisai.getUf10Cd(), 1, 20, hfUfSeigyo.getUf10Name() + ip, false);
				}
				checkString(meisai.getProjectCd(), 1, 12, "プロジェクトコード" + ip, false);
				checkString(meisai.getSegmentCd(), 1, 8, "セグメントコード" + ip, false);
				if(isKazei) {
					checkDomain(meisai.getZeiritsu(), zeiritsuDomain, "消費税率" + ip , false);
					checkDomain(meisai.getKeigenZeiritsuKbn(), Domain.FLG, "軽減税率区分" + ip , false);
				}
				checkNumberOver1(meisai.getKousaihiNinzuu(), 1, 6, "交際費人数" + ip, false);
				checkDomain(meisai.getJigyoushaKbn(), jigyoushaKbnDomain,"事業者区分" + ip, false); //インボイス対応
			}
		}
		if (0 < errorList.size())
		{
			return;
		}

		//伝票No.の昇順チェック
        int beforeNo = -1; 
        int currentNo = -1;
		for(int i = 0 ; i < denpyouList.size() ; i++){
			SeikyuushoBaraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
			String ip = "#" + denpyou.getNumber();
			currentNo = Integer.parseInt(denpyou.getDenpyouNo());
			if(currentNo < beforeNo){
				errorList.add("伝票No." + ip + "が昇順にソートされていません。");
				return;
			}
			beforeNo = currentNo;
		}
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		session.remove(Sessionkey.SEIKYUUSHO_CSV);
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

			// 部品 new
			masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			kaikeiLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
			wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
			kLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);

			//請求書払い申請の予算執行対象を取得
			GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.SEIKYUUSHO_BARAI);
			yosanShikkouTaishou = denpyouShubetsuMap.get("yosan_shikkou_taishou").toString();
			invoiceStartFlg = wfEventLogic.judgeinvoiceStart();
			
			//ファイルをテキストに分解
			List<String[]> dataList = fileDataRead();
			
			//ファイル全体のチェック。列数が規定数であるか、キーとなる伝票No.が昇順であるかのみ。
			fileFormatCheck(dataList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			//CSVファイル情報を作成します。
			List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList = makeCsvFileInfo(dataList);

			//CSVファイル情報をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheck(denpyouList);

			//処理結果をセションに保存します。
			SeikyuushoBaraiCSVUploadSessionInfo sessionInfo = new SeikyuushoBaraiCSVUploadSessionInfo(uploadFileFileName, denpyouList, errorList);
			session.put(Sessionkey.SEIKYUUSHO_CSV, sessionInfo);
			
			// 戻り値を返す
			return "success";
		}
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
				String[] columnArray = line.split(",",-1);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("請求書払い申請CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("請求書払い申請CSVファイルクローズでエラー", e);
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
	protected List<SeikyuushoBaraiCSVUploadDenpyouInfo> makeCsvFileInfo(List<String[]> dataList) {
		List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList = new ArrayList<>();
        String beforeNo = ""; 
		int currentEdaNo = -1;
		SeikyuushoBaraiCSVUploadDenpyouInfo denpyou = null;
		for(int i = 0 ; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			String denpyouNo = rowData[0];

			if(Open21Env.getVersion() == Version.DE3){
				//de3版の場合HFにはブランクを指定
				//伝票No.は昇順前提。同じ伝票No.の先頭行のみを処理→申請本体情報の読込
				//起案伝票IDは予算執行対象が支出依頼でない場合、0を内部的にセットする
				if(! denpyouNo.equals(beforeNo)) {
					//CSVファイル情報（申請内容）の設定
					denpyou = new SeikyuushoBaraiCSVUploadDenpyouInfo();
					denpyouList.add(denpyou);
					denpyou.setNumber (Integer.toString(i + 1));
					denpyou.setDenpyouNo (denpyouNo);
					denpyou.setHf1Cd("");
					denpyou.setHf2Cd("");
					denpyou.setHf3Cd("");
					denpyou.setHf4Cd("");
					denpyou.setHf5Cd("");
					denpyou.setHf6Cd("");
					denpyou.setHf7Cd("");
					denpyou.setHf8Cd("");
					denpyou.setHf9Cd("");
					denpyou.setHf10Cd("");
					denpyou.setShiharaiKigen(rowData[2]);
					denpyou.setInvoiceDenpyou(rowData[19]);
					if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
						denpyou.setKianDenpyouId(rowData[DATACOLUMN_NUM_DE3]);
					}else {
						denpyou.setKianDenpyouId("0");
					}
					currentEdaNo = 0;
				}
				
				//全行を処理→申請明細情報の読込
				currentEdaNo++;
				SeikyuushoBaraiCSVUploadMeisaiInfo meisai = new SeikyuushoBaraiCSVUploadMeisaiInfo();
				denpyou.meisaiList.add(meisai);
				meisai.setNumber(Integer.toString(i + 1));
				meisai.setDenpyouEdaNo(Integer.toString(currentEdaNo));
				meisai.setShiwakeEdaNo(rowData[3]);
				meisai.setKamokuEdabanCd(rowData[4]);
				meisai.setFutanBumonCd(rowData[5]);
				meisai.setTorihikisakiCd(rowData[1]);
				meisai.setTekiyou(rowData[6]);
				meisai.setShiharaiKingaku(rowData[7]);
				meisai.setUf1Cd(rowData[8]);
				meisai.setUf2Cd(rowData[9]);
				meisai.setUf3Cd(rowData[10]);
				meisai.setUf4Cd("");
				meisai.setUf5Cd("");
				meisai.setUf6Cd("");
				meisai.setUf7Cd("");
				meisai.setUf8Cd("");
				meisai.setUf9Cd("");
				meisai.setUf10Cd("");
				meisai.setProjectCd(rowData[11]);
				meisai.setSegmentCd(rowData[12]);
				meisai.setZeiritsu(rowData[13]);
				meisai.setKeigenZeiritsuKbn(rowData[14]);
				meisai.setKousaihiNinzuu(rowData[15]);
				meisai.setZeinukiKingaku(rowData[16]);
				meisai.setShouhizeigaku(rowData[17]);
				meisai.setJigyoushaKbn(rowData[18]);
			}else{
				//SIAS版の場合HFにそのままCSVの値を指定
				//伝票No.は昇順前提。同じ伝票No.の先頭行のみを処理→申請本体情報の読込
				//起案伝票IDは予算執行対象が支出依頼でない場合、0を内部的にセットする
				if(! denpyouNo.equals(beforeNo)) {
					//CSVファイル情報（申請内容）の設定
					denpyou = new SeikyuushoBaraiCSVUploadDenpyouInfo();
					denpyouList.add(denpyou);
					denpyou.setNumber(Integer.toString(i + 1));
					denpyou.setDenpyouNo(denpyouNo);
					denpyou.setHf1Cd(rowData[1]);
					denpyou.setHf2Cd(rowData[2]);
					denpyou.setHf3Cd(rowData[3]);
					denpyou.setHf4Cd(rowData[4]);
					denpyou.setHf5Cd(rowData[5]);
					denpyou.setHf6Cd(rowData[6]);
					denpyou.setHf7Cd(rowData[7]);
					denpyou.setHf8Cd(rowData[8]);
					denpyou.setHf9Cd(rowData[9]);
					denpyou.setHf10Cd(rowData[10]);
					denpyou.setShiharaiKigen(rowData[12]);
					denpyou.setInvoiceDenpyou(rowData[36]);
					currentEdaNo = 0;
					if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
						denpyou.setKianDenpyouId(rowData[DATACOLUMN_NUM_SIAS]);
					}else {
						denpyou.setKianDenpyouId("0");
					}
				}
				
				//全行を処理→申請明細情報の読込
				currentEdaNo++;
				SeikyuushoBaraiCSVUploadMeisaiInfo meisai = new SeikyuushoBaraiCSVUploadMeisaiInfo();
				denpyou.meisaiList.add(meisai);
				meisai.setNumber(Integer.toString(i + 1));
				meisai.setDenpyouEdaNo(Integer.toString(currentEdaNo));
				meisai.setShiwakeEdaNo(rowData[13]);
				meisai.setKamokuEdabanCd(rowData[14]);
				meisai.setFutanBumonCd(rowData[15]);
				meisai.setTorihikisakiCd(rowData[11]);
				meisai.setTekiyou(rowData[16]);
				meisai.setShiharaiKingaku(rowData[17]);
				meisai.setUf1Cd(rowData[18]);
				meisai.setUf2Cd(rowData[19]);
				meisai.setUf3Cd(rowData[20]);
				meisai.setUf4Cd(rowData[21]);
				meisai.setUf5Cd(rowData[22]);
				meisai.setUf6Cd(rowData[23]);
				meisai.setUf7Cd(rowData[24]);
				meisai.setUf8Cd(rowData[25]);
				meisai.setUf9Cd(rowData[26]);
				meisai.setUf10Cd(rowData[27]);
				meisai.setProjectCd(rowData[28]);
				meisai.setSegmentCd(rowData[29]);
				meisai.setZeiritsu(rowData[30]);
				meisai.setKeigenZeiritsuKbn(rowData[31]);
				meisai.setKousaihiNinzuu(rowData[32]);
				meisai.setZeinukiKingaku(rowData[33]);
				meisai.setShouhizeigaku(rowData[34]);
				meisai.setJigyoushaKbn(rowData[35]);
			}

			beforeNo = denpyouNo;
		}
		return denpyouList;
	}
}
