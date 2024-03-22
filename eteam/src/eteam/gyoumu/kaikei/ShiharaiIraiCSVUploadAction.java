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
 * 支払依頼（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class ShiharaiIraiCSVUploadAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数(de3)*/
	static final int DATACOLUMN_NUM_DE3 = 32;//27→32
	/** データ列数(SIAS)*/
	static final int DATACOLUMN_NUM_SIAS = 49;//44→49
	
	//＜画面入力＞
		/** ファイルオブジェクト */
		protected File uploadFile;
		/** ファイル名(uploadFileに付随) */
		protected String uploadFileFileName;

	//＜画面制御情報＞
		/** HF・UF制御クラス */
		HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
		/** 予算執行対象 */
		String yosanShikkouTaishou;
		
		/** 入力方式 */
		String nyuuryokuHoushiki = setting.zeiDefaultA013();
		/** インボイス開始フラグ */
		String invoiceStartFlg;
		/** インボイス設定フラグ */
		String invoiceSetteiFlg = setting.invoiceSetteiFlg();
		
	//＜部品＞
		/** マスター管理ロジック */
		protected MasterKanriCategoryLogic masterLogic;
		/** 会計ロジック */
		protected KaikeiCategoryLogic kLogic;
		/** 会計共通ロジック */
		protected KaikeiCommonLogic kaikeiLogic;
		/** 起票ナビカテゴリーロジック*/
		protected KihyouNaviCategoryLogic kihyouLogic;
		/** 内部コード設定Dao */
		NaibuCdSettingAbstractDao naibuCdSettingDao;
		/** WorkflowEventControlLogic */
		protected WorkflowEventControlLogic wfEventLogic;
		
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
		protected void fileContentCheck(List<ShiharaiIraiCSVUploadDenpyouInfo> denpyouList) {
			List<GMap> zeiritsuList = masterLogic.loadshouhizeiritsu();
			String[] zeiritsuDomain = EteamCommon.mapList2Arr(zeiritsuList, "zeiritsu");
			List<NaibuCdSetting> invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
			String[] invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
			List<GMap> jigyoushaKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
			String[] jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");
			
			//必須項目チェック
			for (int i = 0; i < denpyouList.size(); i++) {
				ShiharaiIraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
				String ip = "#" + denpyou.getNumber();
				checkHissu(denpyou.getDenpyouNo(), "伝票No."		+ ip);
				checkHissu(denpyou.getTorihikisakiCd(), "取引先コード"	+ ip);
				checkHissu(denpyou.getShihiaraiShubetsu(), "支払種別"		+ ip);
				checkHissu(denpyou.getKeijoubi(), "計上日"		+ ip);
				checkHissu(denpyou.getKianDenpyouId(), "起案伝票ID" 	+ ip);

				List<ShiharaiIraiCSVUploadMeisaiInfo> meisaiList = denpyou.getMeisaiList();
				for (int j = 0; j < meisaiList.size(); j++) {
					ShiharaiIraiCSVUploadMeisaiInfo meisai = meisaiList.get(j);
					ip = "#" + meisai.getNumber();
					checkHissu(meisai.getTekiyou(), "摘要"			+ ip);
					if("0".equals(nyuuryokuHoushiki)) {
						checkHissu(meisai.getShiharaiKingaku(),	"支払金額"+ ip);
					}else {
						checkHissu(meisai.getZeinukiKingaku(), "税抜金額"+ ip);
						checkHissu(meisai.getShouhizeigaku(), "消費税額"+ ip);
					}
					checkHissu(meisai.getShiwakeEdaNo(), "仕訳枝番号"	+ ip);
					boolean isKazei = true;
					if(meisai.getShiwakeEdaNo().matches("^[0-9]{1,10}$")){
						GMap mp = kLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(meisai.getShiwakeEdaNo()));
						if(mp != null){
							isKazei = List.of("001", "002", "011", "012", "013", "014").contains(mp.get("kari_kazei_kbn")); 
						}
					}
					if(isKazei) {
						checkHissu(meisai.getZeiritsu(), "消費税率"		+ ip);
						checkHissu(meisai.getKeigenZeiritsuKbn(), "軽減税率区分"	+ ip);
					}
				}
				//入力がなかった場合は0とみなすので必須チェックの必要はなくなった
				//ただし仕様変更など可能性あるので残しておく
//				if("1".equals(invoiceStartFlg) && "1".equals(invoiceSetteiFlg)) {
//					checkHissu(denpyou.getInvoiceDenpyou(),"インボイス対応伝票"+ip);
//				}
			}
			if (0 < errorList.size())
			{
				return;
			}

			//形式のチェック
			for (int i = 0; i < denpyouList.size(); i++) {
				ShiharaiIraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
				String ip = "#" + denpyou.getNumber();
				checkNumber(denpyou.getDenpyouNo(), 1, 3, "伝票No." + ip, false);
				if(!(denpyou.getKianDenpyouId().equals("0"))) {
					checkString(denpyou.getKianDenpyouId(), 19, 19, "起案伝票ID" + ip , false);
				}
				checkDomain(denpyou.getJigyoushaKbn(), jigyoushaKbnDomain,"事業者区分" + ip, false); //インボイス対応
				checkJigyoushaNo (denpyou.getJigyoushaNo(), "適格請求書発行事業者登録番号" + ip); //インボイス対応
				checkDomain(denpyou.getInvoiceDenpyou(), invoiceDenpyouDomain, "インボイス伝票" + ip , false); //インボイス対応

				List<ShiharaiIraiCSVUploadMeisaiInfo> meisaiList = denpyou.getMeisaiList();
				for (int j = 0; j < meisaiList.size(); j++) {
					ShiharaiIraiCSVUploadMeisaiInfo meisai = meisaiList.get(j);
					ip = "#" + meisai.getNumber();
					checkNumber(meisai.getShiwakeEdaNo(), 1, 10, "仕訳枝番号" + ip, false);
					boolean isKazei = true;
					if(meisai.getShiwakeEdaNo().matches("^[0-9]{1,10}$")){
						GMap mp = kLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(meisai.getShiwakeEdaNo()));
						if(mp == null){
							errorList.add("仕訳枝番号" + ip + "は存在しません。");
						}else {
							isKazei = List.of("001", "002", "011", "012", "013", "014").contains(mp.get("kari_kazei_kbn")); 
						}
						
					}
					if(isKazei) {
						checkDomain(meisai.getZeiritsu(), zeiritsuDomain, "消費税率" + ip , false);
						checkDomain(meisai.getKeigenZeiritsuKbn(), Domain.FLG, "軽減税率区分" + ip , false);
					}
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
				ShiharaiIraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
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
			session.remove(Sessionkey.SHIHARAIIRAI_CSV);
			return "success";
		}

		/**
		 * アップロードイベント
		 * @return ResultName
		 */
		public String upload(){
			EteamConnection connection = EteamConnection.connect();
			try{
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
				kLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
				kaikeiLogic = EteamContainer.getComponent(KaikeiCommonLogic.class , connection);
				kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
				naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
				wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);

				//支払依頼申請の予算執行対象を取得
				GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.SIHARAIIRAI);
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
				List<ShiharaiIraiCSVUploadDenpyouInfo> denpyouList = makeCsvFileInfo(dataList);

				//CSVファイル情報をチェックします。
				//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
				fileContentCheck(denpyouList);

				//処理結果をセションに保存します。
				ShiharaiIraiCSVUploadSessionInfo sessionInfo = new ShiharaiIraiCSVUploadSessionInfo(uploadFileFileName, denpyouList, errorList);
				session.put(Sessionkey.SHIHARAIIRAI_CSV, sessionInfo);
				
				// 戻り値を返す
				return "success";
			}finally{
				connection.close();
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
				throw new RuntimeException("支払依頼申請CSVファイル読込でエラー", e);
			} finally {
				if (null != inFile) {
					try {
						inFile.close();
					} catch (IOException e) {
						throw new RuntimeException("支払依頼申請CSVファイルクローズでエラー", e);
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
		protected List<ShiharaiIraiCSVUploadDenpyouInfo> makeCsvFileInfo(List<String[]> dataList) {
			List<ShiharaiIraiCSVUploadDenpyouInfo> denpyouList = new ArrayList<>();
	        String beforeNo = null; 
			int currentEdaNo = -1;
			ShiharaiIraiCSVUploadDenpyouInfo denpyou = null;
			for(int i = 0 ; i < dataList.size() ; i++){
				String[] rowData = dataList.get(i);
				String denpyouNo = rowData[0];

				if(Open21Env.getVersion() == Version.DE3){
					//de3版の場合HFにはブランクを指定
					//伝票No.は昇順前提。同じ伝票No.の先頭行のみを処理→申請本体情報の読込
					//起案伝票IDは予算執行対象が支出依頼でない場合、0を内部的にセットする
					if(! denpyouNo.equals(beforeNo)) {
						//CSVファイル情報（申請内容）の設定
						denpyou = new ShiharaiIraiCSVUploadDenpyouInfo();
						denpyouList.add(denpyou);
						denpyou.setNumber (Integer.toString(i + 1));
						denpyou.setDenpyouNo (denpyouNo);
						denpyou.setHf1Cd ("");
						denpyou.setHf2Cd ("");
						denpyou.setHf3Cd ("");
						denpyou.setHf4Cd ("");
						denpyou.setHf5Cd ("");
						denpyou.setHf6Cd ("");
						denpyou.setHf7Cd ("");
						denpyou.setHf8Cd ("");
						denpyou.setHf9Cd ("");
						denpyou.setHf10Cd ("");
						denpyou.setTorihikisakiCd (rowData[1]);
						denpyou.setIchigensakiTorihikisakiName (rowData[2]);
						denpyou.setShihiaraiShubetsu (rowData[3]);
						denpyou.setKeijoubi (rowData[4]);
						denpyou.setYoteibi (rowData[5]);
						denpyou.setEdi (rowData[6]);
						denpyou.setFurikomiGinkouCd (rowData[7]);
						denpyou.setFurikomiGinkouShitenCd (rowData[8]);
						denpyou.setYokinShubetsu (rowData[9]);
						denpyou.setKouzaBangou (rowData[10]);
						denpyou.setKouzaMeiginin (rowData[11]);
						denpyou.setTesuuryou (rowData[12]);
						denpyou.setManekinGensen (rowData[13]);
						denpyou.setHasseiShubetsu (rowData[14]); if(isEmpty(denpyou.getHasseiShubetsu())) denpyou.setHasseiShubetsu("経費");
						denpyou.setJigyoushaKbn(rowData[29]);
						denpyou.setJigyoushaNo(rowData[30]);
						denpyou.setInvoiceDenpyou(rowData[31]);
						if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
							denpyou.setKianDenpyouId(rowData[DATACOLUMN_NUM_DE3]);
						}else {
							denpyou.setKianDenpyouId("0");
						}
						currentEdaNo = 0;
					}
					
					//全行を処理→申請明細情報の読込
					currentEdaNo++;
					ShiharaiIraiCSVUploadMeisaiInfo meisai = new ShiharaiIraiCSVUploadMeisaiInfo();
					denpyou.meisaiList.add(meisai);
					meisai.setNumber(Integer.toString(i + 1));
					meisai.setDenpyouEdaNo (Integer.toString(currentEdaNo));
					meisai.setTekiyou (rowData[15]);
					meisai.setShiharaiKingaku (rowData[16]);
					meisai.setZeiritsu (rowData[17]);
					meisai.setKeigenZeiritsuKbn (rowData[18]);
					meisai.setShiwakeEdaNo (rowData[19]);
					meisai.setKamokuEdabanCd (rowData[20]);
					meisai.setFutanBumonCd (rowData[21]);
					meisai.setUf1Cd (rowData[22]);
					meisai.setUf2Cd (rowData[23]);
					meisai.setUf3Cd (rowData[24]);
					meisai.setUf4Cd ("");
					meisai.setUf5Cd ("");
					meisai.setUf6Cd ("");
					meisai.setUf7Cd ("");
					meisai.setUf8Cd ("");
					meisai.setUf9Cd ("");
					meisai.setUf10Cd ("");
					meisai.setProjectCd (rowData[25]);
					meisai.setSegmentCd (rowData[26]);
					meisai.setZeinukiKingaku(rowData[27]);
					meisai.setShouhizeigaku(rowData[28]);
				}else{
					//SIAS版の場合HFにそのままCSVの値を指定
					//伝票No.は昇順前提。同じ伝票No.の先頭行のみを処理→申請本体情報の読込
					//起案伝票IDは予算執行対象が支出依頼でない場合、0を内部的にセットする
					if(! denpyouNo.equals(beforeNo)) {
						//CSVファイル情報（申請内容）の設定
						denpyou = new ShiharaiIraiCSVUploadDenpyouInfo();
						denpyouList.add(denpyou);
						denpyou.setNumber (Integer.toString(i + 1));
						denpyou.setDenpyouNo (denpyouNo);
						denpyou.setHf1Cd (rowData[1]);
						denpyou.setHf2Cd (rowData[2]);
						denpyou.setHf3Cd (rowData[3]);
						denpyou.setHf4Cd (rowData[4]);
						denpyou.setHf5Cd (rowData[5]);
						denpyou.setHf6Cd (rowData[6]);
						denpyou.setHf7Cd (rowData[7]);
						denpyou.setHf8Cd (rowData[8]);
						denpyou.setHf9Cd (rowData[9]);
						denpyou.setHf10Cd (rowData[10]);
						denpyou.setTorihikisakiCd (rowData[11]);
						denpyou.setIchigensakiTorihikisakiName (rowData[12]);
						denpyou.setShihiaraiShubetsu (rowData[13]);
						denpyou.setKeijoubi (rowData[14]);
						denpyou.setYoteibi (rowData[15]);
						denpyou.setEdi (rowData[16]);
						denpyou.setFurikomiGinkouCd (rowData[17]);
						denpyou.setFurikomiGinkouShitenCd (rowData[18]);
						denpyou.setYokinShubetsu (rowData[19]);
						denpyou.setKouzaBangou (rowData[20]);
						denpyou.setKouzaMeiginin (rowData[21]);
						denpyou.setTesuuryou (rowData[22]);
						denpyou.setManekinGensen (rowData[23]);
						denpyou.setHasseiShubetsu (rowData[24]); if(isEmpty(denpyou.getHasseiShubetsu())) denpyou.setHasseiShubetsu("経費");
						denpyou.setJigyoushaKbn(rowData[46]);
						denpyou.setJigyoushaNo(rowData[47]);
						denpyou.setInvoiceDenpyou(rowData[48]);
						if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
							denpyou.setKianDenpyouId(rowData[DATACOLUMN_NUM_SIAS]);
						}else {
							denpyou.setKianDenpyouId("0");
						}
						currentEdaNo = 0;
					}
					
					//全行を処理→申請明細情報の読込
					currentEdaNo++;
					ShiharaiIraiCSVUploadMeisaiInfo meisai = new ShiharaiIraiCSVUploadMeisaiInfo();
					denpyou.meisaiList.add(meisai);
					meisai.setNumber(Integer.toString(i + 1));
					meisai.setDenpyouEdaNo(Integer.toString(currentEdaNo));
					meisai.setTekiyou (rowData[25]);
					meisai.setShiharaiKingaku (rowData[26]);
					meisai.setZeiritsu (rowData[27]);
					meisai.setKeigenZeiritsuKbn (rowData[28]);
					meisai.setShiwakeEdaNo (rowData[29]);
					meisai.setKamokuEdabanCd (rowData[30]);
					meisai.setFutanBumonCd (rowData[31]);
					meisai.setUf1Cd (rowData[32]);
					meisai.setUf2Cd (rowData[33]);
					meisai.setUf3Cd (rowData[34]);
					meisai.setUf4Cd (rowData[35]);
					meisai.setUf5Cd (rowData[36]);
					meisai.setUf6Cd (rowData[37]);
					meisai.setUf7Cd (rowData[38]);
					meisai.setUf8Cd (rowData[39]);
					meisai.setUf9Cd (rowData[40]);
					meisai.setUf10Cd (rowData[41]);
					meisai.setProjectCd (rowData[42]);
					meisai.setSegmentCd (rowData[43]);
					meisai.setZeinukiKingaku(rowData[44]);
					meisai.setShouhizeigaku(rowData[45]);
				}

				beforeNo = denpyouNo;
			}
			return denpyouList;
		}
	}
