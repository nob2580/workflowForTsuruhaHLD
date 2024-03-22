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
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dto.KamokuMaster;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataMain;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 交通費精算抽出バッチ
 */
public class KoutsuuhiSeisanChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KoutsuuhiSeisanChuushutsuBat.class);

	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	KoutsuuhiSeisanChuushutsuLogic gyoumuLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/**	処理グループ */
	String shoriGroup;
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;

	@Override
	public String getBatchName() {
		return "交通費精算抽出";
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

			//承認済交通費精算伝票を取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("交通費精算抽出データ件数：" + denpyouList.size());

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
	 * @param denpyou 伝票と交通費精算の結合データ
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");

		// 承認ルートから、起票者を取得
		GMap kihyouUser = common.findKihyouUser((String)denpyou.get("denpyou_id")); 
		String userId = (String)kihyouUser.get("user_id");
		String shainNo = comChu.getShainCdRenkeiFlg(denpyou) ? (String)kihyouUser.get("shain_no") : null;
		String shikibetsuBangou = (String)kihyouUser.get("houjin_card_shikibetsuyou_num");
		
		Date keijoubi = (Date)denpyou.get("keijoubi");
		Date shiharaibi = (Date)denpyou.get("shiharaibi");

		// 仕訳作成のための形式等を決定
		Map<String, Boolean> sakuseiKeishikiInfo = common.getShiwakeSakuseiKeishikiInfoMap(EteamSettingInfo.Key.SHIWAKE_SAKUSEI_HOUHOU_A010, shiharaibi, keijoubi, EteamSettingInfo.Key.OP21MPARAM_DENPYOU_KEISHIKI_A010);

		
		// 本体と税仕訳で使うのは支払日か計上日か
		boolean isKeijoubi = sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.HASSEI);
		var dymd = isKeijoubi ? keijoubi : shiharaibi;
		
		boolean isFukugou = sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.FUKUGOU);
		
		BigDecimal goukeiKingaku = (BigDecimal)denpyou.get("goukei_kingaku");
		BigDecimal houjinKingaku = (BigDecimal)denpyou.get("houjin_card_riyou_kingaku");
		BigDecimal kaishaTehaiKingaku = (BigDecimal)denpyou.get("kaisha_tehai_kingaku");
		BigDecimal tatekaeKingaku = goukeiKingaku.subtract(houjinKingaku).subtract(kaishaTehaiKingaku);

		boolean edabanRenkei = comChu.getEdabanRenkeiFlg(denpyou, DENPYOU_KBN.KOUTSUUHI_SEISAN);
		
		boolean isZeinuki = List.of("002", "013", "014").contains(denpyou.get("kari_kazei_kbn"));
		
		// 処理グループ
		String zeiKamokuGaibuCd = isZeinuki ? this.common.getZeiKamokuGaibuCd(denpyou) : denpyou.get("kari_kamoku_cd"); // 税抜にならないと気には使われないはずではある

		//======================================
		//交通費明細
		//======================================

		//DBのまま
		List<GMap> meisaiList = gyoumuLogic.loadRyohiMeisai(denpyouId);
		
		//交通手段・日当等マスターから借方枝番を付与
		if(edabanRenkei) {
			common.setMasterEdaban(meisaiList, null);
		}
		
		//借方枝番と支払方法(CとかKとか)と事業者区分が同じやつで合算
		meisaiList = common.mergeRyohiMeisai(meisaiList);
		
		//貸方集約するなら仕訳種類(経費・未払消込）によりリストを分けておいて、後で集約処理を行う。
		List<ShiwakeData> siwakeKeihiList = new ArrayList<>();
		List<ShiwakeData> siwakeMibaraiKeshikomiList = new ArrayList<>();

		//======================================
		//仕訳作成
		//======================================
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);
		KamokuMaster kmk = kamokuMasterDao.find(denpyou.get("kari_kamoku_cd"));
		if (kmk.shoriGroup != null) {
			shoriGroup = kmk.shoriGroup.toString(); //事業者区分の制御で使用
		}

		for(GMap meisai : meisaiList){
			ShiwakeDataRS kari = comChu.makeKarikata(denpyou);
			if(edabanRenkei) {
				kari.setEDA(meisai.get("kari_kamoku_edaban_cd"));
			}
			String shiharai = "";
			if (meisai.get("houjin_card_riyou_flg").equals("1"))
			{
				shiharai = "C";
			}
			if (meisai.get("kaisha_tehai_flg").equals("1"))
			{
				shiharai = "K";
			}
			//伝票には事業者区分を持っていないため
			//課税区分が非課税等の場合は値を「0」に書き換え
			if(!((List.of("001","011","002","013").contains(denpyou.get("kari_kazei_kbn")) && List.of("2","5","6","7","8","10").contains(shoriGroup)) || shoriGroup.equals("21"))) {
				kari.setMENZEIKEIKA("0");
			}else {
				kari.setMENZEIKEIKA((String)meisai.get("jigyousha_kbn"));
			}

			
			//本体分
			boolean isHoujinCard = shiharai.equals("C");
			boolean isFurikomi = denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI);
			ShiwakeData shiwake = new ShiwakeData();
			shiwake.setMain(main);
			shiwake.setKari(kari, denpyouId, shainNo);
			int kashiNo = isHoujinCard
				? 4
				: shiharai.equals("K")
					? 5
					: isKeijoubi
						? 3
						: isFurikomi
							? 1
							: 2;
			shiwake.setKashi(comChu.makeKashikata(denpyou, kashiNo), null, null, isHoujinCard ? shikibetsuBangou : null);

			ShiwakeDataCom sc = shiwake.getCom();
			sc.setDYMD(dymd);
			sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
			sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.KOUTSUUHI_SEISAN, denpyou, null, "0")));
			sc.setTNO((String)denpyou.get("tekiyou_cd"));
			sc.setVALU((BigDecimal)meisai.get(isZeinuki ? "zeinuki_kingaku" : "meisai_kingaku"));
			sc.setBUNRI(common.getBunriKbnForShiwake(denpyou));
			siwakeKeihiList.add(shiwake);
			
			// 税抜の場合
			if(isZeinuki)
			{
				ShiwakeDataRS kariZeigaku = comChu.makeKarikata(denpyou);
				kariZeigaku.setKMK(zeiKamokuGaibuCd);
				kariZeigaku.setMENZEIKEIKA((String)meisai.get("jigyousha_kbn"));//伝票には事業者区分を持っていないため
				kariZeigaku.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
				kariZeigaku.setSRE("");
				common.setZeitaishouKoumoku(kariZeigaku);
				
				//消費税対象科目
				
				ShiwakeData shiwakeZeigaku = new ShiwakeData();
				shiwakeZeigaku.setMain(main);
				shiwakeZeigaku.setKari(kariZeigaku, denpyouId, shainNo);
				shiwakeZeigaku.setKashi(comChu.makeKashikata(denpyou, kashiNo), null, null, isHoujinCard ? shikibetsuBangou : null);
	
				shiwakeZeigaku.setZKMK(kari.getKMK());
				shiwakeZeigaku.setZRIT(kari.getRIT());
				shiwakeZeigaku.setZKEIGEN(kari.getKEIGEN());
				shiwakeZeigaku.setZZKB(kari.getZKB());
				shiwakeZeigaku.setZGYO(shiwake.getRGYO());
				shiwakeZeigaku.setZSRE(kari.getSRE());
				shiwakeZeigaku.setZURIZEIKEISAN(kari.getURIZEIKEISAN());
				shiwakeZeigaku.setZMENZEIKEIKA(kari.getMENZEIKEIKA());
				
				ShiwakeDataCom scZeigaku = shiwakeZeigaku.getCom();
				scZeigaku.setDYMD(dymd);
				scZeigaku.setDCNO(common.serialNo(denpyou.get("serial_no")));
				scZeigaku.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.KOUTSUUHI_SEISAN, denpyou, null, "0")));
				scZeigaku.setTNO((String)denpyou.get("tekiyou_cd"));
				scZeigaku.setVALU((BigDecimal)meisai.get("shouhizeigaku"));
				siwakeKeihiList.add(shiwakeZeigaku);
			}
			
			//未払消込（貸方3の時専用）
			if (kashiNo == 3){
				//支払日 未払金/現預金 合計金額
				ShiwakeData shiwakeMiharai = new ShiwakeData();
				shiwakeMiharai.setMain(main);
				shiwakeMiharai.setKari(comChu.makeKashikata(denpyou, 3), null, null);
				shiwakeMiharai.setKashi(comChu.makeKashikata(denpyou, isFurikomi ? 1 : 2), null, null, null);
	
				ShiwakeDataCom scMiharai = shiwakeMiharai.getCom();
				scMiharai.setDYMD(shiharaibi);
				scMiharai.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
				scMiharai.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.KOUTSUUHI_SEISAN, denpyou, null, "0")));
				scMiharai.setTNO((String)denpyou.get("tekiyou_cd"));
				scMiharai.setVALU((BigDecimal)meisai.get("meisai_kingaku")); // こっちは課税区分に関係なく税込でOK
				siwakeMibaraiKeshikomiList.add(shiwakeMiharai);
			}
		}
		
		// 複合仕訳形式ならマージ
		if(isFukugou){
			siwakeKeihiList = common.mergeShiwake(siwakeKeihiList, true, true);
			siwakeMibaraiKeshikomiList = common.mergeShiwake(siwakeMibaraiKeshikomiList, true, true);
		}
		
		// 各種仕訳を一つのリストに統合
		List<ShiwakeData> shiwakeList = new ArrayList<>();
		shiwakeList.addAll(siwakeKeihiList);
		shiwakeList.addAll(siwakeMibaraiKeshikomiList);
		if (onMemory)
		{
			return shiwakeList;
		}
		swkLogic.insert(shiwakeList);

		//======================================
		//FBデータ作成
		//======================================
		boolean fbDataMade = false;
		if (tatekaeKingaku.compareTo(BigDecimal.ZERO) > 0 && SHIHARAI_HOUHOU.FURIKOMI.equals(denpyou.get("shiharaihouhou"))) {
			//起票ユーザー口座情報
			GMap shainKouza = common.findKihyouShainKouza(userId); 

			//FBデータ作成
			common.makeFBData((String)denpyou.get("denpyou_id"), (Date)denpyou.get("shiharaibi"), tatekaeKingaku, userId, shainKouza);
			fbDataMade = true;
		}


		//======================================
		//後処理
		//======================================
		// 承認状況登録処理
		common.insertSyouninJoukyou(denpyouId, fbDataMade);

		//済
		common.updateChuushutsuZumi(denpyouId);
		return shiwakeList;
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A010;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(KoutsuuhiSeisanChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}
}
