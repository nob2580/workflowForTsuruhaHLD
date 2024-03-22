package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst.SHIWAKE_SAKUSEI_TYPE;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
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
 * 自動引落伝票抽出バッチ
 */
public class JidouHikiotoshiChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(SeikyuushoBaraiChuushutsuBat.class);

	/** 自動引落抽出バッチのロジック */
	protected JidouHikiotoshiChuushutsuLogic gyoumuLogic;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 会計SELECT */
	protected KaikeiCategoryLogic kaikeiLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;
	/** システム管理SELECT */
	SystemKanriCategoryLogic systemLg;
	/** マスターcommonLogic */
	MasterKanriCategoryLogic masterCommonLogic;
	/** 科目マスタDao */
	KamokuMasterDao kamokuMasterDao;


	@Override
	public String getBatchName() {
		return "自動引落伝票抽出";
	}

	@Override
	public String getCountName() {
		return "伝票数";
	}

	@Override
	public int mainProc() {
		try {
			connection = EteamConnection.connect();
			initialize();

			//承認済で仕訳未作成の自動引落を伝票単位で取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("自動引落抽出データ件数：" + denpyouList.size());

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

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A009;
	}

	/**
	 * 伝票単位に仕訳作成
	 * @param denpyou 自動引落データ
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		List<GMap> meisaiList = kaikeiLogic.loadJidouhikiotoshiMeisai(denpyouId);
		Date keijoubi = (Date)denpyou.get("keijoubi");
		Date hikiotoshibi = (Date)denpyou.get("hikiotoshibi");
		Map<String, Boolean> sakuseiKeishikiInfo = common.getShiwakeSakuseiKeishikiInfoMap(EteamSettingInfo.Key.SHIWAKE_SAKUSEI_HOUHOU_A009, hikiotoshibi, keijoubi, EteamSettingInfo.Key.OP21MPARAM_DENPYOU_KEISHIKI_A009);
		boolean fukugou = sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.FUKUGOU);
		
		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser(denpyouId);
		String shainNo = (String)kihyouUser.get("shain_no");
		
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		
		
		//貸方集約するなら仕訳種類(計上・支払）によりリストを分けておいて、後で集約処理を行う。
		//貸方集約しないなら全部同じリストに突っ込んでいく。
		List<ShiwakeData> shiwakes = new ArrayList<>();
		List<ShiwakeData> swkK = null;
		List<ShiwakeData> swkS = null;
		if(fukugou){
			swkK = new ArrayList<>();
			swkS = new ArrayList<>();
		}else{
			swkK = shiwakes;
			swkS = shiwakes;
		}

		//======================================
		//仕訳作成
		//======================================
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);
		for (GMap meisai : meisaiList) {
			meisai = comChu.merge(denpyou, meisai);
			String meisaiShainNo = comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null;
			
			boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
			String kingakuStr = "shiharai_kingaku";
			if (isZeinuki)
			{
				kingakuStr = "hontai_kingaku";
			}
			
			
			//現金主義なら
			if(!sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.HASSEI)){
				//引落日 経費/未払金 明細金額
				ShiwakeData shiwake = new ShiwakeData(); swkK.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, meisaiShainNo);
				shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
	
				ShiwakeDataCom sc = shiwake.getCom();
				sc.setDYMD(hikiotoshibi);
				sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, denpyou, meisai, "0")));
				sc.setTNO((String)meisai.get("tekiyou_cd"));
				sc.setVALU((BigDecimal)meisai.get(kingakuStr));
				sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
				
				if(isZeinuki) {
					ShiwakeData shiwakeZei = new ShiwakeData(); swkK.add(shiwakeZei);
					shiwakeZei.setMain(main);
					shiwakeZei.setKari(comChu.makeKarikata(meisai), denpyouId, meisaiShainNo);
					shiwakeZei.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
					
					ShiwakeDataRS r = shiwakeZei.getR();
					r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));
					
					r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);//消費税仕訳の課税区分は　000対象外　か　100対象外　か　""未設定か
					r.setSRE("");
					//消費税仕訳　消費税設定により空にする
					common.setZeitaishouKoumoku(r, dto);
					
					//消費税対象科目
					shiwakeZei.setZKMK(shiwake.getR().getKMK());
					shiwakeZei.setZRIT(r.getRIT());
					shiwakeZei.setZKEIGEN(r.getKEIGEN());
					shiwakeZei.setZZKB(shiwake.getR().getZKB());
					shiwakeZei.setZGYO(shiwakeZei.getRGYO());
					shiwakeZei.setZSRE(r.getSRE());
					shiwakeZei.setZURIZEIKEISAN(r.getURIZEIKEISAN());
					shiwakeZei.setZMENZEIKEIKA(r.getMENZEIKEIKA());
		
					ShiwakeDataCom scZei = shiwakeZei.getCom();
					scZei.setDYMD(hikiotoshibi);
					scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
					scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, denpyou, meisai, "0")));
					scZei.setTNO((String)meisai.get("tekiyou_cd"));
					scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
//					scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
				}
			
			//発生主義なら
			}else{
				//計上日 経費/未払金 　明細金額
				ShiwakeData shiwake = new ShiwakeData(); swkK.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, meisaiShainNo);
				shiwake.setKashi(comChu.makeKashikata(meisai, 2), null, null, null);
	
				ShiwakeDataCom sc = shiwake.getCom();
				sc.setDYMD(keijoubi);
				sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, denpyou, meisai, "0")));
				sc.setTNO((String)meisai.get("tekiyou_cd"));
				sc.setVALU((BigDecimal)meisai.get(kingakuStr));
				sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
				
				if(isZeinuki) {
					ShiwakeData shiwakeZei = new ShiwakeData(); swkK.add(shiwakeZei);
					shiwakeZei.setMain(main);
					shiwakeZei.setKari(comChu.makeKarikata(meisai), denpyouId, meisaiShainNo);
					shiwakeZei.setKashi(comChu.makeKashikata(meisai, 2), null, null, null);
					
					ShiwakeDataRS r = shiwakeZei.getR();
					r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));
					
					r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
					r.setSRE("");
					//消費税仕訳　消費税設定により空にする
					common.setZeitaishouKoumoku(r, dto);
					
					//消費税対象科目
					shiwakeZei.setZKMK(shiwake.getR().getKMK());
					shiwakeZei.setZRIT(r.getRIT());
					shiwakeZei.setZKEIGEN(r.getKEIGEN());
					shiwakeZei.setZZKB(shiwake.getR().getZKB());
					shiwakeZei.setZGYO(shiwakeZei.getRGYO());
					shiwakeZei.setZSRE(r.getSRE());
					shiwakeZei.setZURIZEIKEISAN(r.getURIZEIKEISAN());
					shiwakeZei.setZMENZEIKEIKA(r.getMENZEIKEIKA());
		
					ShiwakeDataCom scZei = shiwakeZei.getCom();
					scZei.setDYMD(keijoubi);
					scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
					scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, denpyou, meisai, "0")));
					scZei.setTNO((String)meisai.get("tekiyou_cd"));
					scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
//					scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
				}

				//引落日(支払日) 未払金/現預金 明細金額
				shiwake = new ShiwakeData(); swkS.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(comChu.makeKashikata(meisai, 2), null, null);
				shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
	
				sc = shiwake.getCom();
				sc.setDYMD(hikiotoshibi);
				sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, denpyou, meisai, "0")));
				sc.setTNO((String)meisai.get("tekiyou_cd"));
				sc.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));
//				sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
			}
		}
		
		//借方・貸方集約
		if(fukugou){
			swkK = common.mergeShiwake(swkK, false, true); shiwakes.addAll(swkK);
			swkS = common.mergeShiwake(swkS, true, true); shiwakes.addAll(swkS);
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

	/**
	 * 初期化
	 */
	protected void initialize() {
		gyoumuLogic = EteamContainer.getComponent(JidouHikiotoshiChuushutsuLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		systemLg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}
}
