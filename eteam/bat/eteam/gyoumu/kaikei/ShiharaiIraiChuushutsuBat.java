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
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.TESUURYOU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
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
public class ShiharaiIraiChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(SeikyuushoBaraiChuushutsuBat.class);

	/** 支払依頼抽出バッチのロジック */
	protected ShiharaiIraiChuushutsuLogic lg;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 会計カテゴリSELECT */
	protected KaikeiCategoryLogic kaikei;
	/** 仕訳ロジック */
	protected ShiwakeLogic shiwakeLg;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** マスターcommonLogic */
	MasterKanriCategoryLogic masterCommonLogic;
	/** マスターロジック */
	protected DaishoMasterCategoryLogic dmLogic;
	
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;
	/** 科目マスタDao */
	KamokuMasterDao kamokuMasterDao;

	@Override
	public String getBatchName() {
		return "支払依頼申請抽出";
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
			List<GMap> denpyouList = lg.loadDenpyou();
			log.info("支払依頼抽出データ件数：" + denpyouList.size());

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
		
		GMap denpyou = lg.findDenpyou(denpyouId);
		return denpyouChuushutsu(denpyou);
	}
	
	/**
	 * 伝票単位に仕訳作成
	 * @param denpyou 伝票+支払依頼レコード
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		String shiharaiHouhou = (String)denpyou.get("shiharai_houhou");
		String shiharaiShubetsu = (String)denpyou.get("shiharai_shubetsu");
		Date keijoubi = (Date)denpyou.get("keijoubi");
		Date shiharaibi = (Date)denpyou.get("yoteibi");
		boolean tanitsu = EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A013).equals("0");
		String torihikisakiCd = (String)denpyou.get("torihikisaki_cd");
		boolean ichigensaki = torihikisakiCd.equals(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_CD));
		BigDecimal manekinKingaku = (BigDecimal)denpyou.get("manekin_gensen");
//		BigDecimal tesuuryou = denpyou.get("tesuuryou").equals(TESUURYOU.JISHA_FUTAN) ? null : dmLogic.judgeTesuuryou((BigDecimal)denpyou.get("sashihiki_shiharai"), (String)denpyou.get("furikomi_ginkou_cd"));
		BigDecimal tesuuryou = ichigensaki && denpyou.get("tesuuryou").equals(TESUURYOU.SENPOU_FUTAN) ? dmLogic.judgeTesuuryou((BigDecimal)denpyou.get("sashihiki_shiharai"), (String)denpyou.get("furikomi_ginkou_cd")) : null;
		set4Shiwake(denpyou);

		//伝票別に明細を取得
		List<GMap> meisaiList = kaikei.loadShiharaiIraiMeisai(denpyouId);
		
		//手数料仕訳で任意部門の場合にセットする負担部門コードを取得
		String tesuuryouFutanBumonCd = lg.getTesuuryouFutanBumonCd(meisaiList);

		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser(denpyouId);
		String shainNo = (String)kihyouUser.get("shain_no");
		
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		
		//明細の金額を控除金／未払金に分離　手数料相手負担なら未払金を支払分と相手負担手数料分に分離
		meisaiList = lg.calcHaraiManekin(meisaiList, manekinKingaku);
		List<GMap> meisaiList2 = lg.calcTesuuryou(meisaiList, tesuuryou);

		//======================================
		//仕訳作成
		//======================================
		List<ShiwakeData> shiwakes = new ArrayList<>();
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);

		//TODO 有効化する場合、BigDecimalのnullチェックを加えておくこと
		// ※コメントアウト分には消費税仕訳対応していません
		if(shiharaiHouhou.equals(SHIHARAI_IRAI_HOUHOU.JIDOUHIKIOTOSHI)){
			/*
			//引落（単一）・・・支払日　経費／マネキン　明細金額　内マネキン分
			//　　　　　　　　　支払日　経費／預金　　　明細金額　内預金分
			if(tanitsu){
				for (int i = 0; i < meisaiList.size(); i++) {
					Map<String, Object> meisai = comChu.merge(denpyou, meisaiList.get(i));
					BigDecimal manekin2 = (BigDecimal)meisai.get("manekin2");
					BigDecimal genyokin2 = (BigDecimal)meisai.get("genyokin2");
					if(manekin2.doubleValue() != 0){
						ShiwakeData shiwake = new ShiwakeData(); shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
						shiwake.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.MANEKIN_CD));
						shiwake.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.MANEKIN_EDABAN));
						shiwake.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.MANEKIN_BUMON));
			
						ShiwakeDataCom sc = shiwake.getCom();
						sc.setDYMD(shiharaibi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutShiwake(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(manekin2);
					}
					if(genyokin2.doubleValue() != 0){
						ShiwakeData shiwake = new ShiwakeData(); shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
						shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
			
						ShiwakeDataCom sc = shiwake.getCom();
						sc.setDYMD(shiharaibi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutShiwake(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(genyokin2);
					}
				}
			//引落（複合）・・・経費／諸口
			//　　　　　　　　　諸口／マネキン
			//　　　　　　　　　諸口／預金
			}else{
				List<ShiwakeData> swkKeihi = new ArrayList<>();
				List<ShiwakeData> swkManekin = new ArrayList<>();
				List<ShiwakeData> swkYokin = new ArrayList<>();
				
				for (int i = 0; i < meisaiList.size(); i++) {
					Map<String, Object> meisai = comChu.merge(denpyou, meisaiList.get(i));
					BigDecimal manekin2 = (BigDecimal)meisai.get("manekin2");
					BigDecimal genyokin2 = (BigDecimal)meisai.get("genyokin2");

					ShiwakeData shiwake = new ShiwakeData(); swkKeihi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);
					
					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutShiwake(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
					sc.setTNO((String)meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));

					if(manekin2.doubleValue() != 0){
						shiwake = new ShiwakeData(); swkManekin.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeShokuchi(), null, null);
						shiwake.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.MANEKIN_CD));
						shiwake.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.MANEKIN_EDABAN));
						shiwake.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.MANEKIN_BUMON));
						
						sc = shiwake.getCom();
						sc.setDYMD(shiharaibi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutShiwake(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(manekin2);
					}

					if(genyokin2.doubleValue() != 0){
						shiwake = new ShiwakeData(); swkYokin.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeShokuchi(), null, null);
						shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
			
						sc = shiwake.getCom();
						sc.setDYMD(shiharaibi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutShiwake(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(genyokin2);
					}
				}
				shiwakes.addAll(swkKeihi);
				lg.mergeKashi(swkManekin); shiwakes.addAll(swkManekin);
				lg.mergeKashi(swkYokin); shiwakes.addAll(swkYokin);
			}
			*/
		}else{
			int kashiNo = shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI) ? 2 : 3;

			//振込（単一）・・・経費／控除科目(控除金額あり)
			//　　　　　　　　　経費／未払金
			//　　　　　　　　　未払金／預金
			//　　　　　　　　　未払金／手数料科目(相手先手数料負担)
			if(tanitsu){
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					BigDecimal manekin2 = (BigDecimal)meisai.get("manekin2");
					BigDecimal mibarai2 = (BigDecimal)meisai.get("mibarai2");
					BigDecimal zeimanekin = meisai.get("zeimanekin") == null ? BigDecimal.ZERO: (BigDecimal)meisai.get("zeimanekin");
					BigDecimal zeimiabarai = meisai.get("zeimiabarai") == null ? BigDecimal.ZERO: (BigDecimal)meisai.get("zeimiabarai");
					
					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki)
					{
						kingakuStr = "zeinuki_kingaku";
					}
					
					
					if(manekin2 != null && manekin2.doubleValue() != 0){
						//経費／控除科目
						ShiwakeData shiwake = new ShiwakeData(); shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
						shiwake.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.MANEKIN_CD));
						shiwake.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.MANEKIN_EDABAN));
						shiwake.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.MANEKIN_BUMON));
			
						ShiwakeDataCom sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(manekin2);
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
						
						//税込＞控除金額＞税抜
						if(isZeinuki) {
							String zeiKMK = common.getZeiKamokuGaibuCd(meisai, dto);
							ShiwakeData shiwakeZei = new ShiwakeData(); shiwakes.add(shiwakeZei);
							shiwakeZei.setMain(main);
							shiwakeZei.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
							shiwakeZei.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.MANEKIN_CD));
							shiwakeZei.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.MANEKIN_EDABAN));
							shiwakeZei.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.MANEKIN_BUMON));
							
							ShiwakeDataRS r = shiwakeZei.getR();
							r.setKMK(zeiKMK);
							
							//消費税仕訳　課税区分は対象外、その他消費税設定により空にする
							r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
							r.setSRE("");
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
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							scZei.setTNO((String)meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
							if(mibarai2.doubleValue() == 0 && zeimanekin.doubleValue() != 0) scZei.setVALU(zeimanekin); // 消費税/控除金額　（税抜金額は控除金額の中からすべて出た　消費税が控除金額から出た）
//							scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
							
							if(mibarai2.doubleValue() == 0 && zeimiabarai.doubleValue() != 0) { //「0か0以外か」で判定するように
								//消費税/未払金（税抜金額は控除金額からすべて出た　消費税は控除金額からはみ出した分がある）
								ShiwakeData shiwakeZei2 = new ShiwakeData(); shiwakes.add(shiwakeZei2);
								shiwakeZei2.setMain(main);
								shiwakeZei2.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
								shiwakeZei2.setKashi(comChu.makeKashikata(meisai, kashiNo), null, null, null);
								
								ShiwakeDataRS r2 = shiwakeZei2.getR();
								r2.setKMK(zeiKMK);
								
								//消費税仕訳　課税区分は対象外、その他消費税設定により空にする
								r2.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
								r2.setSRE("");
								common.setZeitaishouKoumoku(r2, dto);
								
								//消費税対象科目
								shiwakeZei2.setZKMK(shiwake.getR().getKMK());
								shiwakeZei2.setZRIT(r2.getRIT());
								shiwakeZei2.setZKEIGEN(r.getKEIGEN());
								shiwakeZei2.setZZKB(shiwake.getR().getZKB());
								shiwakeZei2.setZGYO(shiwakeZei2.getRGYO());
								shiwakeZei2.setZSRE(r2.getSRE());
								shiwakeZei2.setZURIZEIKEISAN(r2.getURIZEIKEISAN());
								shiwakeZei2.setZMENZEIKEIKA(r2.getMENZEIKEIKA());
								
								ShiwakeDataCom scZei2 = shiwakeZei2.getCom();
								scZei2.setDYMD(keijoubi);
								scZei2.setDCNO(common.serialNo(denpyou.get("serial_no")));
								scZei2.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
								scZei2.setTNO((String)meisai.get("tekiyou_cd"));
								scZei2.setVALU(zeimiabarai);
//								scZei2.setBUNRI(common.getBunriKbnForShiwake(meisai));
							};
						}
					}
					if(mibarai2 != null && mibarai2.doubleValue() != 0){
						//経費／未払金
						ShiwakeData shiwake = new ShiwakeData(); shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
						shiwake.setKashi(comChu.makeKashikata(meisai, kashiNo), null, null, null);
			
						ShiwakeDataCom sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(mibarai2);
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
						if(isZeinuki) {
							//消費税/未払金
							ShiwakeData shiwakeZei = new ShiwakeData(); shiwakes.add(shiwakeZei);
							shiwakeZei.setMain(main);
							shiwakeZei.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
							shiwakeZei.setKashi(comChu.makeKashikata(meisai, kashiNo), null, null, null);
							
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
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							scZei.setTNO((String)meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
//							scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
						}
					}
					
				}
				if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)){
					for (int i = 0; i < meisaiList.size(); i++) {
						GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
						BigDecimal yokin2 = (BigDecimal)meisai.get("yokin2");
						BigDecimal tesuuryou2 = (BigDecimal)meisai.get("tesuuryou2");
						if(yokin2 != null && yokin2.doubleValue() != 0){
							//未払金／預金
							ShiwakeData shiwake = new ShiwakeData(); shiwakes.add(shiwake);
							shiwake.setMain(main);
							shiwake.setKari(comChu.makeKashikata(meisai, kashiNo), null, null);
							shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
				
							ShiwakeDataCom sc = shiwake.getCom();
							sc.setDYMD(shiharaibi);
							sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
							sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							sc.setTNO((String)meisai.get("tekiyou_cd"));
							sc.setVALU(yokin2);
						}
						if(tesuuryou2 != null && tesuuryou2.doubleValue() != 0){
							//未払金／手数料
							ShiwakeData shiwake = new ShiwakeData(); shiwakes.add(shiwake);
							shiwake.setMain(main);
							shiwake.setKari(comChu.makeKashikata(meisai, kashiNo), null, null);
							shiwake.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_KAMOKU_CD));
							shiwake.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_EDABAN_CD));
							//ICSP連絡票_1012_支払依頼申請：振込手数料仕訳への部門コードセット
							if( EteamConst.ShiwakeConst.FUTAN.equals(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD)) ){ 
								//借方負担部門を指定
								shiwake.getS().setBMN(tesuuryouFutanBumonCd);
							}else if( EteamConst.ShiwakeConst.DAIHYOUBUMON.equals(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD)) ){ 
								//起票者の代表部門を設定
								shiwake.getS().setBMN((String)meisai.get("daihyou_futan_bumon_cd"));
							}else{
								//会社設定の固定値指定
								shiwake.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD));
							} 
				
							ShiwakeDataCom sc = shiwake.getCom();
							sc.setDYMD(shiharaibi);
							sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
							sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							sc.setTNO((String)meisai.get("tekiyou_cd"));
							sc.setVALU(tesuuryou2);
						}
					}
				}
			//振込（複合）・・・経費／諸口
			//　　　　　　　　　諸口／控除科目(控除金額あり)
			//　　　　　　　　　諸口／未払金
			//　　　　　　　　　未払金／諸口
			//　　　　　　　　　諸口／預金
			//　　　　　　　　　諸口／手数料科目(相手先手数料負担)
			}else{
				List<ShiwakeData> swkKeihi = new ArrayList<>();
				List<ShiwakeData> swkManekin = new ArrayList<>();
				List<ShiwakeData> swkMibarai = new ArrayList<>();
				List<ShiwakeData> swkMibaraiKeshi = new ArrayList<>();
				List<ShiwakeData> swkYokin = new ArrayList<>();
				List<ShiwakeData> swkTesuuryou = new ArrayList<>();
				
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					BigDecimal manekin2 = (BigDecimal)meisai.get("manekin2");
					BigDecimal mibarai2 = (BigDecimal)meisai.get("mibarai2");
					
					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki)
					{
						kingakuStr = "zeinuki_kingaku";
					}

					//経費／諸口
					ShiwakeData shiwake = new ShiwakeData(); swkKeihi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);
					
					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(keijoubi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
					sc.setTNO((String)meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal)meisai.get(kingakuStr));
					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					
					if(isZeinuki) {
						//消費税／諸口
						ShiwakeData shiwakeZei = new ShiwakeData(); swkKeihi.add(shiwakeZei);
						shiwakeZei.setMain(main);
						shiwakeZei.setKari(comChu.makeKarikata(meisai), denpyouId, comChu.getShainCdRenkeiFlg(meisai) ? shainNo : null);
						shiwakeZei.setKashi(comChu.makeShokuchi(), null, null, null);
						
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
						scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						scZei.setTNO((String)meisai.get("tekiyou_cd"));
						scZei.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
//						scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
					}

					if(manekin2 != null && manekin2.doubleValue() != 0){
						//諸口／控除項目
						shiwake = new ShiwakeData(); swkManekin.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeShokuchi(), null, null);
						shiwake.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.MANEKIN_CD));
						shiwake.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.MANEKIN_EDABAN));
						shiwake.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.MANEKIN_BUMON));
						
						sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(manekin2);
					}

					if(mibarai2 != null && mibarai2.doubleValue() != 0){
						//諸口／未払金
						shiwake = new ShiwakeData(); swkMibarai.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeShokuchi(), null, null);
						shiwake.setKashi(comChu.makeKashikata(meisai, kashiNo), null, null, null);
			
						sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
						sc.setTNO((String)meisai.get("tekiyou_cd"));
						sc.setVALU(mibarai2);

						if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)){
							//未払金／諸口
							shiwake = new ShiwakeData(); swkMibaraiKeshi.add(shiwake);
							shiwake.setMain(main);
							shiwake.setKari(comChu.makeKashikata(meisai, kashiNo), null, null);
							shiwake.setKashi(comChu.makeShokuchi(), null, null, null);
				
							sc = shiwake.getCom();
							sc.setDYMD(shiharaibi);
							sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
							sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							sc.setTNO((String)meisai.get("tekiyou_cd"));
							sc.setVALU(mibarai2);
						}
					}
				}

				if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)){
					for (int i = meisaiList2.size() - 1; i >= 0; i--) {
						GMap meisai = comChu.merge(denpyou, meisaiList2.get(i));
						BigDecimal yokin2 = (BigDecimal)meisai.get("yokin2");
						BigDecimal tesuuryou2 = (BigDecimal)meisai.get("tesuuryou2");
					
						if(yokin2 != null && yokin2.doubleValue() != 0){
							//諸口／預金
							ShiwakeData shiwake = new ShiwakeData(); swkYokin.add(shiwake);
							shiwake.setMain(main);
							shiwake.setKari(comChu.makeShokuchi(), null, null);
							shiwake.setKashi(comChu.makeKashikata(meisai, 1), null, null, null);
				
							ShiwakeDataCom sc = shiwake.getCom();
							sc.setDYMD(shiharaibi);
							sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
							sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							sc.setTNO((String)meisai.get("tekiyou_cd"));
							sc.setVALU(yokin2);
						}
						if(tesuuryou2 != null && tesuuryou2.doubleValue() != 0){
							//諸口／手数料
							ShiwakeData shiwake = new ShiwakeData(); swkTesuuryou.add(shiwake);
							shiwake.setMain(main);
							shiwake.setKari(comChu.makeShokuchi(), null, null);
							shiwake.getS().setKMK(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_KAMOKU_CD));
							shiwake.getS().setEDA(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_EDABAN_CD));
							
							//ICSP連絡票_1012_支払依頼申請：振込手数料仕訳への部門コードセット
							if( EteamConst.ShiwakeConst.FUTAN.equals(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD)) ){ 
								//借方負担部門を指定
								shiwake.getS().setBMN(tesuuryouFutanBumonCd);
							}else if( EteamConst.ShiwakeConst.DAIHYOUBUMON.equals(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD)) ){ 
								//起票者の代表部門を設定
								shiwake.getS().setBMN((String)meisai.get("daihyou_futan_bumon_cd"));
							}else{
								//会社設定の固定値指定
								shiwake.getS().setBMN(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD));
							}
							
							ShiwakeDataCom sc = shiwake.getCom();
							sc.setDYMD(shiharaibi);
							sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
							sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai, null, "0")));
							sc.setTNO((String)meisai.get("tekiyou_cd"));
							sc.setVALU(tesuuryou2);
						}
					}
				}
				shiwakes.addAll(swkKeihi);
				lg.mergeKashi(swkManekin); shiwakes.addAll(swkManekin);
				lg.mergeKashi(swkMibarai); shiwakes.addAll(swkMibarai);
				lg.mergeKari(swkMibaraiKeshi); shiwakes.addAll(swkMibaraiKeshi);
				lg.mergeKashi(swkYokin); shiwakes.addAll(swkYokin);
				lg.mergeKashi(swkTesuuryou); shiwakes.addAll(swkTesuuryou);
			}
		}
		if (onMemory)
		{
			return shiwakes;
		}
		shiwakeLg.insert(shiwakes);


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
	 * 仕訳作成用に項目の整理
	 * (1)取引先名を一見先名で入れ替える
	 * @param denpyou 支払依頼テーブル
	 */
	protected void set4Shiwake(GMap denpyou) {
		String torihikisakiCd = (String)denpyou.get("torihikisaki_cd");
		if (torihikisakiCd.equals(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_CD))) {
			denpyou.put("torihikisaki_name_ryakushiki", denpyou.get("ichigensaki_torihikisaki_name"));
		}
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A013;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		lg = EteamContainer.getComponent(ShiharaiIraiChuushutsuLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		kaikei = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		shiwakeLg = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		dmLogic = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);
		masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}
}
