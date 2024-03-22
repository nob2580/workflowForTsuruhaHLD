package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataMain;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 請求書払い抽出バッチ
 */
public class SeikyuushoBaraiChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(SeikyuushoBaraiChuushutsuBat.class);

	/** 請求書払い抽出バッチのロジック */
	protected SeikyuushoBaraiChuushutsuLogic gyoumuLogic;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 会計カテゴリSELECT */
	protected KaikeiCategoryLogic kaikei;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;
	/** 科目マスタDao */
	KamokuMasterDao kamokuMasterDao;

	@Override
	public String getBatchName() {
		return "請求書払い申請抽出";
	}

	@Override
	public String getCountName() {
		return "伝票数";
	}

	@Override
	public int mainProc() {
		connection = null;
		try {
			connection = EteamConnection.connect();
			initialize();

			//承認済で仕訳未作成の請求書払いを伝票単位で取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("請求書払い抽出データ件数：" + denpyouList.size());

			return chuushutsuShiwake(denpyouList);
		} catch (Throwable e) {
			log.error("エラー発生", e);
			return 1;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	@Override
	public List<ShiwakeData> makeShiwake(String denpyouId){
		initialize();
		
		GMap denpyou = gyoumuLogic.findDenpyou(denpyouId);
		return denpyouChuushutsu(denpyou);
	}

	/**
	 * 伝票単位に仕訳作成
	 * @param denpyou 請求書データ
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		String kakeFlg = (String)denpyou.get("kake_flg");
		//String nyuryokuHoushiki = (String)denpyou.get("nyuryoku_houshiki");
		//String invoiceDenpyou = (String)denpyou.get("invoiceDenpyou");
		Date keijoubi = (Date)denpyou.get("keijoubi");
		Date shiharaibi = (Date)denpyou.get("shiharaibi");
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		

		//伝票 + 掛け別に明細を取得
		//変数名は1:掛けなし 2:掛けあり
		List<GMap> meisaiList = kaikei.loadSeikyuushobaraiMeisai(denpyouId);
		
		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser(denpyouId);
		String shainNo = (String)kihyouUser.get("shain_no");


		//======================================
		//仕訳作成
		//====================================== 
		List<ShiwakeData> shiwakes = new ArrayList<>();
		List<ShiwakeData> swkKakeNashi = null;
		List<ShiwakeData> swkKakeAriK = null;
		List<ShiwakeData> swkKakeAriS = null;
		//貸方集約するなら仕訳種類(掛けなし・掛けあり計上・掛けあり支払）によりリストを分けておいて、後で集約処理を行う。
		//貸方集約しないなら全部同じリストに突っ込んでいく。
		if(!setting.op21MparamDenpyouKeishikiA003().equals("0")){
			swkKakeNashi = new ArrayList<>();
			swkKakeAriK = new ArrayList<>();
			swkKakeAriS = new ArrayList<>();
		}else{
			swkKakeNashi = shiwakes;
			swkKakeAriK = shiwakes;
			swkKakeAriS = shiwakes; 
		}
		
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);
		for (GMap meisai : meisaiList) {
			meisai = comChu.merge(denpyou, meisai);
			ShiwakeDataRS kari = comChu.makeKarikata(meisai);
			//課税区分取得
			//課税区分：税抜なら、税抜で明細作って最後に消費税額合計した行作成する
			boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
			String kingakuStr = "shiharai_kingaku";
			if (isZeinuki)
			{
				kingakuStr = "hontai_kingaku";
			}

			//掛けなしに対する処理
			if (kakeFlg.equals("0")) {
				if (setting.kakeNashiShiharaiShiwakeSakusei().equals("1")){

					//支払日(=計上日) 経費/預金
					ShiwakeData shiwake = new ShiwakeData(); swkKakeNashi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(kari, denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
					shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
		
					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SEIKYUUSHO_BARAI, meisai, null, "0")));
					sc.setTNO((String)meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal)meisai.get(kingakuStr));
					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					
					if(isZeinuki) {
						ShiwakeData shiwakeZei = new ShiwakeData(); swkKakeNashi.add(shiwakeZei);
						shiwakeZei.setMain(main);
						ShiwakeDataRS kariZei = comChu.makeKarikata(meisai);//なぜか本体仕訳の借方が連動して科目など変わってしまうので新しく作成
						shiwakeZei.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
						
						kariZei.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));
						kariZei.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
						kariZei.setSRE("");
						//消費税仕訳　消費税設定により空にする
						common.setZeitaishouKoumoku(kariZei, dto);
						
						shiwakeZei.setKari(kariZei, denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
						
						//消費税対象科目
						shiwakeZei.setZKMK(shiwake.getR().getKMK());
						shiwakeZei.setZRIT(shiwake.getR().getRIT());
						shiwakeZei.setZKEIGEN(shiwake.getR().getKEIGEN());
						shiwakeZei.setZZKB(shiwake.getR().getZKB());
						shiwakeZei.setZGYO(shiwakeZei.getRGYO());
						shiwakeZei.setZSRE(shiwake.getR().getSRE());
						shiwakeZei.setZURIZEIKEISAN(shiwake.getR().getURIZEIKEISAN());
						shiwakeZei.setZMENZEIKEIKA(shiwake.getR().getMENZEIKEIKA());
						
						ShiwakeDataCom scZei = shiwakeZei.getCom();
						scZei.setDYMD(shiharaibi);
						scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
						scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String)meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						scZei.setTNO((String)meisai.get("tekiyou_cd"));
						scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
//						scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
					}
				}
			//掛けありに対する処理
			} else {
				//計上日 経費/未払金
				ShiwakeData shiwake = new ShiwakeData(); swkKakeAriK.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(kari, denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
				shiwake.setKashi(comChu.makeKashikata(meisai, 2), null, null, null);
	
				ShiwakeDataCom sc = shiwake.getCom();
				sc.setDYMD(keijoubi);
				sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SEIKYUUSHO_BARAI, meisai, null, "0")));
				sc.setTNO((String)meisai.get("tekiyou_cd"));
				sc.setVALU((BigDecimal)meisai.get(kingakuStr));
				sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
				if(shiharaibi != null){
					sc.setSYMD(shiharaibi);
				}
				
				if(isZeinuki) {
					ShiwakeData shiwakeZei = new ShiwakeData(); swkKakeAriK.add(shiwakeZei);
					shiwakeZei.setMain(main);
					ShiwakeDataRS kariZei = comChu.makeKarikata(meisai);//なぜか本体仕訳の借方が連動して科目など変わってしまうので新しく作成
					shiwakeZei.setKashi(comChu.makeKashikata(meisai, 2), null, null, null);
					
					kariZei.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));
					kariZei.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
					kariZei.setSRE("");
					//消費税仕訳　消費税設定により空にする
					common.setZeitaishouKoumoku(kariZei, dto);
					
					shiwakeZei.setKari(kariZei, denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
					
					//消費税対象科目
					shiwakeZei.setZKMK(shiwake.getR().getKMK());
					shiwakeZei.setZRIT(shiwake.getR().getRIT());
					shiwakeZei.setZKEIGEN(shiwake.getR().getKEIGEN());
					shiwakeZei.setZZKB(shiwake.getR().getZKB());
					shiwakeZei.setZGYO(shiwakeZei.getRGYO());
					shiwakeZei.setZSRE(shiwake.getR().getSRE());
					shiwakeZei.setZURIZEIKEISAN(shiwake.getR().getURIZEIKEISAN());
					shiwakeZei.setZMENZEIKEIKA(shiwake.getR().getMENZEIKEIKA());
					
					ShiwakeDataCom scZei = shiwakeZei.getCom();
					scZei.setDYMD(keijoubi);
					scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
					scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String)meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
					scZei.setTNO((String)meisai.get("tekiyou_cd"));
					scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
//					scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
					if(shiharaibi != null){
						scZei.setSYMD(shiharaibi);
					}
				}

				//支払依頼仕訳を作成：作成する設定で、支払日の入力がある場合のみ
				if (setting.kakeAriShiharaiShiwakeSakusei().equals("1") && shiharaibi != null){
					//支払日 未払金/預金
					shiwake = new ShiwakeData(); swkKakeAriS.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKashikata(meisai, 2), null, null);
					shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
		
					sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SEIKYUUSHO_BARAI, meisai, null, "0")));
					sc.setTNO("");
					sc.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));
//					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
				}
			}
			
		}
		
		//貸方集約
		if(!setting.op21MparamDenpyouKeishikiA003().equals("0")){
			
			swkKakeNashi = common.mergeShiwake(swkKakeNashi, false, true); shiwakes.addAll(swkKakeNashi);
			swkKakeAriK = common.mergeShiwake(swkKakeAriK, false, true); shiwakes.addAll(swkKakeAriK);
			swkKakeAriS = common.mergeShiwake(swkKakeAriS, true, true); shiwakes.addAll(swkKakeAriS);
			
		}
		if (onMemory)
		{
			return shiwakes;
		}
		swkLogic.insert(shiwakes);


		//======================================
		//後処理
		//======================================

		// 承認状況登録処理
		common.insertSyouninJoukyou(denpyouId, false);

		//済
		common.updateChuushutsuZumi(denpyouId);
		return shiwakes;
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A003;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		gyoumuLogic = EteamContainer.getComponent(SeikyuushoBaraiChuushutsuLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		kaikei = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}
}
