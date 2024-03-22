package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.SHIWAKE_SAKUSEI_TYPE;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
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
 * 経費立替精算抽出ロジック
 */
public class KeihiTatekaeSeisanChuushutsuLogic extends EteamAbstractLogic  {

//＜部品＞
	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	RyohiSeisanChuushutsuLogic gyoumuLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;
	/** マスターcommonLogic */
	MasterKanriCategoryLogic masterCommonLogic;
	/** 科目マスタDao */
	KamokuMasterDao kamokuMasterDao;
//＜定数＞
	/** 100：対象外　(消費税仕訳の課税区分) */
	static final String KAZEI_KBN_SHOUHIZEI_SHIWAKE = "100";
	// この定数をメンテする際は EteamAbstractKaikeiBat の定数もメンテすること

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(RyohiSeisanChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}

	/**
	 * 抽出対象の伝票を取得
	 * @param denpyouId 伝票ID
	 * @return 抽出対象の伝票
	 */
	public GMap findDenpyou(String denpyouId) {
		String sql =
				"SELECT * " 
			+ "  ,k.shiharai_kingaku_goukei AS goukei_kingaku "
			+ "FROM denpyou d "
			+ "INNER JOIN keihiseisan k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_id = ? ";
		GMap map = connection.find(sql, denpyouId);
		if(map.get("keijoubi") == null) map.put("keijoubi", EteamCommon.today());
		if(map.get("shiharaibi") == null) map.put("shiharaibi", EteamCommon.tomorrow());
		return map;
	}

	/**
	 * 抽出対象の伝票を取得
	 * @return 抽出対象の伝票
	 */
	public List<GMap> loadDenpyou() {
		String sql =
				"SELECT * " 
			+ "  ,k.shiharai_kingaku_goukei AS goukei_kingaku "
			+ "FROM denpyou d "
			+ "INNER JOIN keihiseisan k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? "
			+ "  AND d.denpyou_joutai = ? "
			+ "  AND d.chuushutsu_zumi_flg = '0' "
			+ "  AND NOT EXISTS (SELECT * FROM :SHIWAKE s WHERE d.denpyou_id = s.denpyou_id) "
			+ "ORDER BY "
			+ "  d.denpyou_id ASC";
		
		if (Open21Env.getVersion() == Version.DE3 ) {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_de3");
		} else {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_sias");
		}
		
		return connection.load(sql, DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}

	/**
	 * 承認済仮払伝票をSELECTする
	 * @param denpyouId 伝票ID
	 * @return 承認済仮払伝票データ
	 */
	public List<GMap> loadMeisai(String denpyouId) {
		final String sql =
				"SELECT "
			+ "  * "
			+ "  ,'A001' AS denpyou_kbn "
			+ "FROM keihiseisan_meisai m "
			+ "WHERE "
			+ "  m.denpyou_id = ? "
			+ "ORDER BY "
			+ "  m.denpyou_edano ASC";
		
		return connection.load(sql, denpyouId);
	}

	/**
	 * 明細金額を仮払分と立替分に分ける
	 * @param meisaiList 明細
	 * @param karibaraiKingaku 仮払金額
	 * @param denpyouKbn 伝票区分
	 * @param denpyou 伝票
	 */
	public void calcKariTate(List<GMap> meisaiList, BigDecimal karibaraiKingaku, String denpyouKbn, GMap denpyou) {
		double karizan = karibaraiKingaku == null ? 0 : karibaraiKingaku.doubleValue();
		// 複合では　諸口/未払金　の時、税込金額合計-仮払金額（合計は集約した結果）なので必ず税込金額
		Date keijoubi = (Date)denpyou.get("keijoubi");
		Date shiharaibi = (Date)denpyou.get("shiharaibi");
		String sakuseiHouhou = Key.SHIWAKE_SAKUSEI_HOUHOU_A001.replace(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, denpyouKbn);
		String keisiki = Key.OP21MPARAM_DENPYOU_KEISHIKI_A001.replace(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, denpyouKbn);
		Map<String, Boolean> sakuseiKeishikiInfo = common.getShiwakeSakuseiKeishikiInfoMap(sakuseiHouhou, shiharaibi, keijoubi, keisiki);
		boolean fukugou = sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.FUKUGOU);
		for(GMap meisai : meisaiList){
			if (meisai.get("houjin_card_riyou_flg").equals("1") || meisai.get("kaisha_tehai_flg").equals("1"))
			{
				continue;
			}
			boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
			String kingakuStr = "shiharai_kingaku";
			if (isZeinuki && !fukugou)
			{
				kingakuStr = "hontai_kingaku";
			}
			double meisaiKingaku = meisai.get("shiharai_kingaku") == null ? 0 : ((BigDecimal)meisai.get("shiharai_kingaku")).doubleValue();
			double meisaiKingakuZeinuki = meisai.get("hontai_kingaku") == null ? 0 : ((BigDecimal)meisai.get("hontai_kingaku")).doubleValue();
			if(karizan >= meisaiKingaku){
				meisai.put("karibarai2", new BigDecimal((isZeinuki && !fukugou) ? meisaiKingakuZeinuki:meisaiKingaku));
				meisai.put("genyokin2", new BigDecimal(0));
				karizan -= meisaiKingaku;
				//2023/12/29　この処理↓を入れている理由が不明（単一形式のためのものだと思われるが定かではない）
				//　複合形式の場合にココが原因で貸借不一致が発生するため、とにかく複合の場合回避するように修正
				if(isZeinuki && !fukugou && karizan == meisaiKingaku) {
					meisai.put("karibarai2", new BigDecimal(meisaiKingakuZeinuki));
				}
			}else if(karizan > 0){
				meisai.put("karibarai2", new BigDecimal(karizan));
				double genyokin2;
				if(!fukugou && isZeinuki) {
					//税抜の単一
					if(karizan > meisaiKingakuZeinuki) {
						meisai.put("karibarai2", new BigDecimal(meisaiKingakuZeinuki));
						genyokin2 = 0;
					}else {
						genyokin2 = meisaiKingakuZeinuki - karizan;
					}
				}else {
					//税込の単一 と 複合すべて
					genyokin2 = meisaiKingaku - karizan;
				}
				meisai.put("genyokin2", new BigDecimal(genyokin2));
				
				double zeikari = karizan - meisaiKingakuZeinuki;
				double shouhizeigaku = meisai.get("shouhizeigaku") == null ? 0 : ((BigDecimal)meisai.get("shouhizeigaku")).doubleValue();
				meisai.put("zeikari", new BigDecimal(zeikari));
				meisai.put("zeimi", new BigDecimal(shouhizeigaku - zeikari));
				karizan = 0;
			}else{
				meisai.put("karibarai2", new BigDecimal(0));
				double genyokin2 = (isZeinuki && !fukugou) ? meisaiKingakuZeinuki : meisaiKingaku;
				meisai.put("genyokin2", new BigDecimal(genyokin2));
			}
		}
		//税込＞仮払い＞消費税額の時（220＞210＞20）
		//　経費/仮払金200
		//　消費税/仮払金10
		//　消費税/未払金10
		//仮払いが税込価格より大きいとき
		//genyokin2は税抜金額
		//仮払いは仮払
		//消費税仮は仮払-税抜
		//消費税未は消費税-消費税仮
	}

	/**
	 * 精算の仕訳作成共通
	 * @param seisanDenKbn 精算の伝票区分
	 * @param kariDenKbn 仮払の伝票区分
	 * @param denpyou 伝票 + 申請本体
	 * @param meisaiList 明細リスト
	 * @param karibaraiDenpyou 仮払の伝票 + 申請本体
	 * @return 仕訳リスト
	 */
	public List<ShiwakeData> makeSeisanCommon(String seisanDenKbn, String kariDenKbn, GMap denpyou,
			List<GMap> meisaiList, GMap karibaraiDenpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String) denpyou.get("denpyou_id");
		String kariBaraiDenpyouId = (String) denpyou.get("karibarai_denpyou_id");
		String karibaraiMishiyouFlg = (String) denpyou.get("karibarai_mishiyou_flg");
		Date keijoubi = (Date) denpyou.get("keijoubi");
		Date shiharaibi = (Date) denpyou.get("shiharaibi");
		BigDecimal keihiKingaku = (BigDecimal) denpyou.get("goukei_kingaku");
		BigDecimal houjinKingaku = (BigDecimal) denpyou.get("houjin_card_riyou_kingaku");
		BigDecimal kaishaTehaiKingaku = (BigDecimal) denpyou.get("kaisha_tehai_kingaku");
		BigDecimal tatekaeKingaku = keihiKingaku.subtract(houjinKingaku).subtract(kaishaTehaiKingaku);

		// 仮払の取得
		BigDecimal karibaraiKingaku = null;
		BigDecimal henkinKingaku = BigDecimal.valueOf(0);
		if (StringUtils.isNotEmpty(kariBaraiDenpyouId)) {
			if (karibaraiDenpyou != null) {
				// 仮払金額の取得
				karibaraiKingaku = (BigDecimal) karibaraiDenpyou.get("karibarai_kingaku");
				if (karibaraiKingaku == null || karibaraiKingaku.doubleValue() == 0) {
					karibaraiDenpyou = null;
					karibaraiKingaku = null;
				} else {
					// 返金額の計算（複合形式時の仮払返金時に使用）
					henkinKingaku = BigDecimal.valueOf(karibaraiKingaku.doubleValue() - tatekaeKingaku.doubleValue());
				}
			}
		}

		// 差引支給金額
		BigDecimal shikyuuKingaku = (BigDecimal) denpyou.get("sashihiki_shikyuu_kingaku");

		// 旅費系差引税額の残額考慮有無
		var shouldConsiderSashihikiZeigaku = seisanDenKbn.equals(DENPYOU_KBN.RYOHI_SEISAN)
				&& List.of("002", "013", "014").contains(denpyou.get("kari_kazei_kbn"))
				&& ((BigDecimal) denpyou.get("sashihiki_zeigaku")).compareTo(BigDecimal.ZERO) > 0;

		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser((String) denpyou.get("denpyou_id"));
		String shainNo = (String) kihyouUser.get("shain_no");
		if (denpyou.containsKey("shain_no")) {
			shainNo = (String) denpyou.get("shain_no"); //出張の場合、仮払及び精算の代理申請であれば代理者ではなく被代理者の方
		}
		String shikibetsuBangou = (String) kihyouUser.get("houjin_card_shikibetsuyou_num");

		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);

		// 仕訳作成のための形式等を決定
		String sakuseiHouhou = Key.SHIWAKE_SAKUSEI_HOUHOU_A001.replace(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, seisanDenKbn);
		String keisiki = Key.OP21MPARAM_DENPYOU_KEISHIKI_A001.replace(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, seisanDenKbn);
		String zankinSakusei = Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A001.replace(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,
				seisanDenKbn);
		Map<String, Boolean> sakuseiKeishikiInfo = common.getShiwakeSakuseiKeishikiInfoMap(sakuseiHouhou, shiharaibi,
				keijoubi, keisiki);
		boolean isHassei = sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.HASSEI);
		boolean isFukugou = sakuseiKeishikiInfo.get(SHIWAKE_SAKUSEI_TYPE.FUKUGOU);
		boolean shouldSakuseiZankin = EteamSettingInfo.getSettingInfo(zankinSakusei).equals("1");
		String zankinSakuseibi = EteamSettingInfo.getSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_HI_A001.replace(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, seisanDenKbn));

		//代表明細:経費精算なら明細１、国内出張精算なら国内交通費、海外出張精算なら海外交通費
		GMap meisai1 = meisaiList.size() == 0 ? null : comChu.merge(denpyou, meisaiList.get(0));

		//======================================
		//作成
		//======================================
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);
		List<ShiwakeData> shiwakes = new ArrayList<>();

		// ★シート：0:精算－仮払金全額未使用
		if (kariBaraiDenpyouId != null && karibaraiMishiyouFlg.equals("1")) {
			// ①　計上日　現金/仮払金　仮払金額 (現金主義なら計上日じゃなくて支払日)
			ShiwakeData shiwake = new ShiwakeData();
			shiwakes.add(shiwake);
			shiwake.setMain(main);
			shiwake.setKari(comChu.makeKashikata(karibaraiDenpyou, 2, kariDenKbn), null, null);
			shiwake.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId, comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

			ShiwakeDataCom sc = shiwake.getCom();
			sc.setDYMD(isHassei ? keijoubi : shiharaibi);
			sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
			sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, null, "2")));
			sc.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
			sc.setVALU((BigDecimal) karibaraiDenpyou.get("karibarai_kingaku"));

			// 単一形式のとき
		} else if (!isFukugou) {
			//こいつだけ仕訳最後にしたいので一旦別リストにしておいて最後に結合する
			List<ShiwakeData> swkMibaraiKeshikomi = new ArrayList<>();

			// ★シート：5:精算－単一形式・発生主義・仮払相殺あり(支払日)
			if (isHassei && shouldSakuseiZankin && zankinSakuseibi.equals("2") && null != karibaraiDenpyou
					&& shikyuuKingaku.doubleValue() < 0) {
				// ①　支払日　現預金　仮払金　仮払金額　（伝票単位）
				ShiwakeData shiwake = new ShiwakeData();
				shiwakes.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(comChu.makeKashikata(karibaraiDenpyou,
						karibaraiDenpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, kariDenKbn),
						null, null);
				shiwake.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
						comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

				ShiwakeDataCom sc = shiwake.getCom();
				sc.setDYMD(shiharaibi);
				sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "1")));
				sc.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
				sc.setVALU((BigDecimal) karibaraiDenpyou.get("karibarai_kingaku"));

				// （明細単位）
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					String meisaiShainNo = comChu.getShainCdRenkeiFlg(meisai, (String) meisai.get("denpyou_kbn"))
							? (String) meisai.get("shain_no")
							: null;

					//課税区分取得
					//課税区分：税抜なら、税抜で明細作って消費税額合計した行作成する
					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) || "014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki) {
						kingakuStr = "hontai_kingaku";
					}

					// ③　計上日　経費　法人ｶｰﾄﾞ　明細金額　（経費明細単位）
					if ("1".equals(meisai.get("houjin_card_riyou_flg"))) {
						//計上日(支払日)　経費/法人貸方　明細金額
						shiwake = new ShiwakeData();
						shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
						shiwake.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null, null, shikibetsuBangou);

						sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						sc.setTNO((String) meisai.get("tekiyou_cd"));
						// sc.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));
						sc.setVALU((BigDecimal) meisai.get(kingakuStr));
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

						if (isZeinuki) {
							ShiwakeData shiwakeZei = new ShiwakeData();
							shiwakes.add(shiwakeZei);
							shiwakeZei.setMain(main);
							//消費税科目が必要
							shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
							shiwakeZei.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null, null, shikibetsuBangou);

							ShiwakeDataRS r = shiwakeZei.getR();
							r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

							r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
							r.setSRE("");
							//消費税仕訳　消費税設定により空にする
							common.setZeitaishouKoumoku(r, dto);

							//消費税対象科目
							//TODO セットに必要な値の取得元が伝票毎に違うため、今すぐにはメソッド化しない　　統一してメソッド化は可能だと思うけど
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
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
							scZei.setTNO((String) meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//							scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
						}

						// ④　計上日　経費　会社手配　明細金額　（経費明細単位）
					} else if ("1".equals(meisai.get("kaisha_tehai_flg"))) {
						//計上日(支払日)　経費/手配貸方　明細金額
						shiwake = new ShiwakeData();
						shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
						shiwake.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null, null, null);

						sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						sc.setTNO((String) meisai.get("tekiyou_cd"));
						// sc.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));
						sc.setVALU((BigDecimal) meisai.get(kingakuStr));
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
						if (isZeinuki) {
							ShiwakeData shiwakeZei = new ShiwakeData();
							shiwakes.add(shiwakeZei);
							shiwakeZei.setMain(main);
							//消費税科目が必要
							shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
							shiwakeZei.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null, null, null);

							ShiwakeDataRS r = shiwakeZei.getR();
							r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

							r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
							r.setSRE("");
							//消費税仕訳　消費税設定により空にする
							common.setZeitaishouKoumoku(r, dto);

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
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
							scZei.setTNO((String) meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//							sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
						}

					} else {
						// ②　計上日　経費　未払金　明細金額　（経費明細単位）
						shiwake = new ShiwakeData();
						shiwakes.add(shiwake);
						shiwake.setMain(main);
						//消費税科目が必要
						shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
						shiwake.setKashi(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null, null);

						sc = shiwake.getCom();
						sc.setDYMD(keijoubi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						sc.setTNO((String) meisai.get("tekiyou_cd"));
						// sc.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));
						sc.setVALU((BigDecimal) meisai.get(kingakuStr));
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

						if (isZeinuki) {
							ShiwakeData shiwakeZei = new ShiwakeData();
							shiwakes.add(shiwakeZei);
							shiwakeZei.setMain(main);
							shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
							shiwakeZei.setKashi(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null, null);

							ShiwakeDataRS r = shiwakeZei.getR();
							r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

							r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
							r.setSRE("");
							//消費税仕訳　消費税設定により空にする
							common.setZeitaishouKoumoku(r, dto);

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
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
							scZei.setTNO((String) meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//							sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
						}

						// ⑤支払日　未払金　現預金　②と同額　（経費明細単位）
						shiwake = new ShiwakeData();
						swkMibaraiKeshikomi.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null);
						shiwake.setKashi(comChu.makeKashikata(meisai, denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, (String) meisai.get("denpyou_kbn")), null, null, null);

						sc = shiwake.getCom();
						sc.setDYMD(shiharaibi);
						sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						sc.setTNO((String) meisai.get("tekiyou_cd"));
						sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
//						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					}
				}

				// 差引税額の考慮
				if (shouldConsiderSashihikiZeigaku) {
					this.makeSashihikiZeiShiwake(denpyou, dto, shiwakes, isFukugou, isHassei, shainNo, main);
				}

				// ★シート：
				//    1:精算－単一形式・現金主義・仮払残金仕訳なし
				//    2:精算－単一形式・現金主義・仮払残金仕訳あり
				//    3:精算－単一形式・発生主義・仮払残金仕訳なし
				//    4:精算－単一形式・発生主義・仮払残金仕訳あり(計上日)
			} else {
				// 仮払情報がある場合、仮払相殺仕訳の作成
				// ①計上日(支払日)　現預金(仮払貸方)/仮払金　仮払金額
				if (null != karibaraiDenpyou && shouldSakuseiZankin) {
					ShiwakeData shiwake = new ShiwakeData();
					shiwakes.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKashikata(karibaraiDenpyou,
							karibaraiDenpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2,
							kariDenKbn), null, null);
					shiwake.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
							comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(isHassei ? keijoubi : shiharaibi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "1")));
					sc.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) karibaraiDenpyou.get("karibarai_kingaku"));
				}

				// （明細単位）
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					String meisaiShainNo = comChu.getShainCdRenkeiFlg(meisai, (String) meisai.get("denpyou_kbn"))
							? (String) meisai.get("shain_no")
							: null;

					//課税区分取得
					//課税区分：税抜なら、税抜で明細作って消費税額行も作成する
					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) || "014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki) {
						kingakuStr = "hontai_kingaku";
					}

					if ("1".equals(meisai.get("houjin_card_riyou_flg"))) {
						//計上日(支払日)　経費/法人貸方　明細金額
						ShiwakeData shiwake = new ShiwakeData();
						shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
						shiwake.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null, null, shikibetsuBangou);

						ShiwakeDataCom sc = shiwake.getCom();
						sc.setDYMD(isHassei ? keijoubi : shiharaibi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						sc.setTNO((String) meisai.get("tekiyou_cd"));
						sc.setVALU((BigDecimal) meisai.get(kingakuStr));
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

						if (isZeinuki) {
							ShiwakeData shiwakeZei = new ShiwakeData();
							shiwakes.add(shiwakeZei);
							shiwakeZei.setMain(main);
							shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
							shiwakeZei.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null, null, shikibetsuBangou);

							ShiwakeDataRS r = shiwakeZei.getR();
							r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

							r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
							r.setSRE("");
							//消費税仕訳　消費税設定により空にする
							common.setZeitaishouKoumoku(r, dto);

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
							scZei.setDYMD(isHassei ? keijoubi : shiharaibi);
							scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
							scZei.setTNO((String) meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//							scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
						}

					} else if ("1".equals(meisai.get("kaisha_tehai_flg"))) {
						//計上日(支払日)　経費/手配貸方　明細金額
						ShiwakeData shiwake = new ShiwakeData();
						shiwakes.add(shiwake);
						shiwake.setMain(main);
						shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,meisaiShainNo);
						shiwake.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null,null, null);

						ShiwakeDataCom sc = shiwake.getCom();
						sc.setDYMD(isHassei ? keijoubi : shiharaibi);
						sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
						sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						sc.setTNO((String) meisai.get("tekiyou_cd"));
						sc.setVALU((BigDecimal) meisai.get(kingakuStr));
						sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

						if (isZeinuki) {
							ShiwakeData shiwakeZei = new ShiwakeData();
							shiwakes.add(shiwakeZei);
							shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
							shiwakeZei.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null, null, null);

							ShiwakeDataRS r = shiwakeZei.getR();
							r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

							r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
							r.setSRE("");
							//消費税仕訳　消費税設定により空にする
							common.setZeitaishouKoumoku(r, dto);

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
							scZei.setDYMD(isHassei ? keijoubi : shiharaibi);
							scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
							scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
							scZei.setTNO((String) meisai.get("tekiyou_cd"));
							scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//							scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
						}

					} else {
						BigDecimal karibarai2 = (BigDecimal) meisai.get("karibarai2");
						BigDecimal genyokin2 = (BigDecimal) meisai.get("genyokin2");
						BigDecimal zeikari = meisai.get("zeikari") == null ? BigDecimal.ZERO : (BigDecimal) meisai.get("zeikari");
						BigDecimal zeimi = meisai.get("zeimi") == null ? BigDecimal.ZERO : (BigDecimal) meisai.get("zeimi");

						//経費の税込金額　＞　仮払金額　＞　経費の税抜金額 の場合
						//　経費/仮払金200
						//　消費税/仮払金10
						//　消費税/未払金10

						if (karibarai2.doubleValue() > 0) {
							//計上日(支払日)　経費/仮払金　明細金額→仮払分
							ShiwakeData shiwake = new ShiwakeData();
							shiwakes.add(shiwake);
							shiwake.setMain(main);
							shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,meisaiShainNo);
							if (shouldSakuseiZankin) {
								shiwake.setKashi(comChu.makeKashikata(karibaraiDenpyou,karibaraiDenpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2,kariDenKbn), null, null, null);
							} else {
								shiwake.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId, comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);
							}

							ShiwakeDataCom sc = shiwake.getCom();
							sc.setDYMD(isHassei ? keijoubi : shiharaibi);
							sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
							sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
							sc.setTNO((String) meisai.get("tekiyou_cd"));
							sc.setVALU(karibarai2);
							sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

							if (isZeinuki) {
								//消費税科目が必要
								//消費税／仮払金　消費税が仮払金から出せた分があった場合 または 明細金額が全額仮払金から出た場合
								if((genyokin2.doubleValue() < 1 && zeikari.doubleValue() > 0) || meisai.get("zeimi") == null) {
									ShiwakeData shiwakeZei = new ShiwakeData();
									shiwakes.add(shiwakeZei);
									shiwakeZei.setMain(main);
									shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
									if (shouldSakuseiZankin) {
										shiwakeZei.setKashi(comChu.makeKashikata(karibaraiDenpyou,karibaraiDenpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2,kariDenKbn),null, null, null);
									} else {
										shiwakeZei.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);
									}

									ShiwakeDataRS r = shiwakeZei.getR();
									r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

									r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
									r.setSRE("");
									//消費税仕訳　消費税設定により空にする
									common.setZeitaishouKoumoku(r, dto);

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
									scZei.setDYMD(isHassei ? keijoubi : shiharaibi);
									scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
									scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"),denpyou, meisai, "0")));
									scZei.setTNO((String) meisai.get("tekiyou_cd"));
									scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
									if (genyokin2.doubleValue() < 1 && zeikari.doubleValue() > 0) {
										scZei.setVALU((BigDecimal) meisai.get("zeikari")); //消費税/仮払金（税抜金額は全部仮払金から　消費税が仮払金から出た）
									}
//									scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
								}

								if (genyokin2.doubleValue() < 1 && zeimi.doubleValue() > 0) {
									//消費税／未払金　仮払金から出せなかった消費税額がある場合
									Date dymd;
									int kashiNo;
									if (!isHassei) {
										dymd = shiharaibi;
										kashiNo = denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2;
									}else {
										dymd = keijoubi;
										kashiNo = 3;
									}

									ShiwakeData shiwakeZei2 = new ShiwakeData();
									shiwakes.add(shiwakeZei2);
									shiwakeZei2.setMain(main);
									shiwakeZei2.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
									shiwakeZei2.setKashi(comChu.makeKashikata(meisai, kashiNo, (String) meisai.get("denpyou_kbn")), null, null, null);

									ShiwakeDataRS r2 = shiwakeZei2.getR();
									r2.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

									r2.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
									r2.setSRE("");
									//消費税仕訳　消費税設定により空にする
									common.setZeitaishouKoumoku(r2, dto);

									//消費税対象科目
									shiwakeZei2.setZKMK(shiwake.getR().getKMK());
									shiwakeZei2.setZRIT(shiwake.getR().getRIT());
									shiwakeZei2.setZKEIGEN(shiwake.getR().getKEIGEN());
									shiwakeZei2.setZZKB(shiwake.getR().getZKB());
									shiwakeZei2.setZGYO(shiwakeZei2.getRGYO());
									shiwakeZei2.setZSRE(shiwake.getR().getSRE());
									shiwakeZei2.setZURIZEIKEISAN(shiwake.getR().getURIZEIKEISAN());
									shiwakeZei2.setZMENZEIKEIKA(shiwake.getR().getMENZEIKEIKA());

									ShiwakeDataCom scZei2 = shiwakeZei2.getCom();
									scZei2.setDYMD(dymd);
									scZei2.setDCNO(common.serialNo(denpyou.get("serial_no")));
									scZei2.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
									scZei2.setTNO((String) meisai.get("tekiyou_cd"));
									scZei2.setVALU((BigDecimal) meisai.get("zeimi"));
//									scZei2.setBUNRI(common.getBunriKbnForShiwake(meisai));
								}

							}
						}
						if (genyokin2.doubleValue() > 0) {
							if (!isHassei) {
								//支払日　経費/現預金　明細金額→仮払超過分
								ShiwakeData shiwake = new ShiwakeData();
								shiwakes.add(shiwake);
								shiwake.setMain(main);
								shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
								shiwake.setKashi(comChu.makeKashikata(meisai, denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, (String) meisai.get("denpyou_kbn")), null, null, null);

								ShiwakeDataCom sc = shiwake.getCom();
								sc.setDYMD(shiharaibi);
								sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
								sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
								sc.setTNO((String) meisai.get("tekiyou_cd"));
								sc.setVALU(genyokin2);
								sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
								if (isZeinuki) {
									//消費税科目が必要
									ShiwakeData shiwakeZei = new ShiwakeData();
									shiwakes.add(shiwakeZei);
									shiwakeZei.setMain(main);
									shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
									shiwakeZei.setKashi(comChu.makeKashikata(meisai,denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, (String) meisai.get("denpyou_kbn")), null, null, null);

									ShiwakeDataRS r = shiwakeZei.getR();
									r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

									r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
									r.setSRE("");
									//消費税仕訳　消費税設定により空にする
									common.setZeitaishouKoumoku(r, dto);

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
									scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
									scZei.setTNO((String) meisai.get("tekiyou_cd"));
									scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//									scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));

								}

							} else {
								//計上日　経費/未払金　明細金額→仮払超過分
								ShiwakeData shiwake = new ShiwakeData();
								shiwakes.add(shiwake);
								shiwake.setMain(main);
								shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
								shiwake.setKashi(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null, null);

								ShiwakeDataCom sc = shiwake.getCom();
								sc.setDYMD(keijoubi);
								sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
								sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
								sc.setTNO((String) meisai.get("tekiyou_cd"));
								sc.setVALU(genyokin2);
								sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

								if (isZeinuki) {
									ShiwakeData shiwakeZei = new ShiwakeData();
									shiwakes.add(shiwakeZei);
									shiwakeZei.setMain(main);
									shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId, meisaiShainNo);
									shiwakeZei.setKashi(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null, null);

									ShiwakeDataRS r = shiwakeZei.getR();
									r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

									r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
									r.setSRE("");
									//消費税仕訳　消費税設定により空にする
									common.setZeitaishouKoumoku(r, dto);

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
									scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
									scZei.setTNO((String) meisai.get("tekiyou_cd"));
									scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//									scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
								}

								//支払日　未払金/現預金　明細金額→仮払超過分
								shiwake = new ShiwakeData();
								swkMibaraiKeshikomi.add(shiwake);
								shiwake.setMain(main);
								shiwake.setKari(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")),
										null, null);
								shiwake.setKashi(comChu.makeKashikata(meisai,
										denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2,
										(String) meisai.get("denpyou_kbn")), null, null, null);

								sc = shiwake.getCom();
								sc.setDYMD(shiharaibi);
								sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
								sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"),
										denpyou, meisai, "0")));
								sc.setTNO((String) meisai.get("tekiyou_cd"));
								BigDecimal mibaraiGenyokin = isZeinuki
										? genyokin2.add((BigDecimal) meisai.get("shouhizeigaku"))
										: genyokin2;
//								sc.setVALU(genyokin2.add((BigDecimal)meisai.get("shouhizeigaku")));//genyokin2+shouhizeigaku or shiharai_kingaku
								sc.setVALU(mibaraiGenyokin);
//								sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

							}
						}
					}
				}

				// 差引税額の考慮
				if (shouldConsiderSashihikiZeigaku) {
					this.makeSashihikiZeiShiwake(denpyou, dto, shiwakes, isFukugou, isHassei, shainNo, main);
				}
			}
			shiwakes.addAll(swkMibaraiKeshikomi); //★テスト！

			// 複合形式のとき
		} else {
			//仕訳種類によりリストを分けておいて、後で集約処理を行う。
			List<ShiwakeData> swkKeihiShokuchi = new ArrayList<>(); //経費／諸口
			List<ShiwakeData> swkShokuchiMibarai = new ArrayList<>(); //諸口／未払
			List<ShiwakeData> swkKashiShokuchi = new ArrayList<>(); //○○／諸口※返金
			List<ShiwakeData> swkKariShokuchi = new ArrayList<>(); //諸口／○○※支払方法別充当
			List<ShiwakeData> swkMibaraiKeshikomi = new ArrayList<>(); //未払／現預金→後で諸口分割

			// ★シート：10:精算－複合形式・発生主義・仮払相殺あり(支払日)
			if (isHassei && shouldSakuseiZankin && zankinSakuseibi.equals("2") && karibaraiDenpyou != null
					&& shikyuuKingaku.doubleValue() < 0) {

				// ①　計上日　経費/諸口　明細金額　（明細単位）
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					String meisaiShainNo = comChu.getShainCdRenkeiFlg(meisai, (String) meisai.get("denpyou_kbn"))
							? (String) meisai.get("shain_no")
							: null;

					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn"))
							|| "013".equals(meisai.get("kari_kazei_kbn"))
							|| "014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki) {
						kingakuStr = "hontai_kingaku";
					}

					ShiwakeData shiwake = new ShiwakeData();
					swkKeihiShokuchi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,
							meisaiShainNo);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(keijoubi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(
							common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) meisai.get(kingakuStr));
					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

					if (isZeinuki) {
						ShiwakeData shiwakeZei = new ShiwakeData();
						swkKeihiShokuchi.add(shiwakeZei);
						shiwakeZei.setMain(main);
						shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,
								meisaiShainNo);
						shiwakeZei.setKashi(comChu.makeShokuchi(), null, null, null);

						ShiwakeDataRS r = shiwakeZei.getR();
						r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

						r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
						r.setSRE("");
						//消費税仕訳　消費税設定により空にする
						common.setZeitaishouKoumoku(r, dto);

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
						scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						scZei.setTNO((String) meisai.get("tekiyou_cd"));
						scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
						//						scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
					}
				}

				// ②　計上日　諸口/未払金　明細金額　（明細単位→集約）
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));

					ShiwakeData shiwake = new ShiwakeData();
					swkShokuchiMibarai.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeShokuchi(), null, null);
					shiwake.setKashi(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(keijoubi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
//					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));

				}

				// ③　支払日　未払金/諸口　明細金額　（明細金額合計）　経費未払金は諸口から
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));

					ShiwakeData shiwake = new ShiwakeData();
					swkKashiShokuchi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null, null);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
//					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
				}

				// ④　支払日　現預金/諸口　仮払金額－明細金額合計(カード、会社手配以外)　（伝票単位）　仮払金額 > 明細金額合計(カード、会社手配以外)であれば、返金
				ShiwakeData shiwake = new ShiwakeData();
				swkKashiShokuchi.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(comChu.makeKashikata(meisai1, 2, (String) meisai1.get("denpyou_kbn")), null, null);
				shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

				ShiwakeDataCom sc = shiwake.getCom();
				sc.setDYMD(shiharaibi);
				sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "3")));
				sc.setTNO("");
				sc.setVALU(shikyuuKingaku.negate());

				// ⑤　支払日　諸口/仮払金　仮払金額　（伝票単位）　仮払金があれば諸口補充
				shiwake = new ShiwakeData();
				swkKariShokuchi.add(shiwake);
				shiwake.setMain(main);
				shiwake.setKari(comChu.makeShokuchi(), null, null);
				shiwake.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
						comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

				sc = shiwake.getCom();
				sc.setDYMD(shiharaibi);
				sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
				sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "1")));
				sc.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
				sc.setVALU((BigDecimal) karibaraiDenpyou.get("karibarai_kingaku"));

				// 法人カード・会社手配分の仕訳作成(最後にmergeShiwakeWithoutBunkatsuで結合)
				// ⑥　支払日　諸口/法人ｶｰﾄﾞ　明細金額合計(カード)　（伝票単位）　　カード利用分の諸口補充
				// ⑦　支払日　諸口/会社手配　明細金額合計(会社手配)　（伝票単位）　　会社手配利用分の諸口補充
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					shiwake = new ShiwakeData();
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeShokuchi(), null, null);

					if ("1".equals(meisai.get("houjin_card_riyou_flg"))) {
						shiwake.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null,
								null, shikibetsuBangou);
					} else if ("1".equals(meisai.get("kaisha_tehai_flg"))) {
						shiwake.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null,
								null, null);
					} else {
						continue;
					}
					sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
					//					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					swkKariShokuchi.add(shiwake);
				}

				// 差引税額の考慮
				if (shouldConsiderSashihikiZeigaku) {
					this.makeSashihikiZeiShiwake(denpyou, dto, shiwakes, isFukugou, isHassei, shainNo, main);
				}
			} else if (!isHassei) {
				// 現金主義
				// 6:精算－複合形式・現金主義・仮払相殺なし
				// 7:精算－複合形式・現金主義・仮払相殺あり
				// 残金を計上する または 返金額がない場合
				// ①支払日  経費 / 諸口   明細金額
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					String meisaiShainNo = comChu.getShainCdRenkeiFlg(meisai, (String) meisai.get("denpyou_kbn"))
							? (String) meisai.get("shain_no")
							: null;

					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn"))
							|| "013".equals(meisai.get("kari_kazei_kbn"))
							|| "014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki) {
						kingakuStr = "hontai_kingaku";
					}

					ShiwakeData shiwake = new ShiwakeData();
					swkKeihiShokuchi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,
							meisaiShainNo);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(
							common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) meisai.get(kingakuStr));
					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					if (isZeinuki) {
						ShiwakeData shiwakeZei = new ShiwakeData();
						swkKeihiShokuchi.add(shiwakeZei);
						shiwakeZei.setMain(main);
						shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,
								meisaiShainNo);
						shiwakeZei.setKashi(comChu.makeShokuchi(), null, null, null);

						ShiwakeDataRS r = shiwakeZei.getR();
						r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

						r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
						r.setSRE("");
						//消費税仕訳　消費税設定により空にする
						common.setZeitaishouKoumoku(r, dto);

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
						scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						scZei.setTNO((String) meisai.get("tekiyou_cd"));
						scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//						scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
					}
				}

				// 残金仕訳をする設定かつ返金額ありの場合、明細1行目の仮払残金仕訳を作成
				// ②支払日　現金(精算の貸方2)/諸口　返金額 (仮払金額－税込金額合計(法カ、手配以外))
				if (shouldSakuseiZankin && henkinKingaku.doubleValue() > 0) {
					// 残金仕訳をする設定かつ返金額ありの場合、明細1行目の仮払残金仕訳を作成
					// 現金主義：支払日  現金(精算の貸方2) / 諸口   返金額
					ShiwakeData shiwake = new ShiwakeData();
					swkKashiShokuchi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKashikata(meisai1, 2, (String) meisai1.get("denpyou_kbn")), null, null);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "3")));
					sc.setTNO("");
					sc.setVALU(henkinKingaku);

					//残金分の諸口/仮払金仕訳も作成
					ShiwakeData shiwakeKb = new ShiwakeData();
					shiwakeKb.setMain(main);
					shiwakeKb.setKari(comChu.makeShokuchi(), null, null);
					shiwakeKb.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
							comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

					ShiwakeDataCom scKb = shiwakeKb.getCom();
					scKb.setDYMD(shiharaibi);
					scKb.setDCNO(common.serialNo(denpyou.get("serial_no")));
					scKb.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "1")));
					scKb.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
					scKb.setVALU(henkinKingaku);
					swkKariShokuchi.add(shiwakeKb);
				}

				// 仮払相殺仕訳・支払仕訳・法人カード・会社手配分の仕訳作成(最後にmergeShiwakeWithoutBunkatsuで結合)
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));

					if ((meisai.get("karibarai2") != null)
							&& ((BigDecimal) meisai.get("karibarai2")).doubleValue() > 0) {
						// ③支払日　諸口/仮払金　　仮払金額
						ShiwakeData shiwakeKb = new ShiwakeData();
						shiwakeKb.setMain(main);
						shiwakeKb.setKari(comChu.makeShokuchi(), null, null);
						shiwakeKb.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
								comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

						ShiwakeDataCom scKb = shiwakeKb.getCom();
						scKb.setDYMD(shiharaibi);
						scKb.setDCNO(common.serialNo(denpyou.get("serial_no")));
						scKb.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai, "1")));
						scKb.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
						scKb.setVALU((BigDecimal) meisai.get("karibarai2"));
						swkKariShokuchi.add(shiwakeKb);
					}
					ShiwakeData shiwake = new ShiwakeData();
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeShokuchi(), null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(shiharaibi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
//					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					if ((meisai.get("genyokin2") != null) && ((BigDecimal) meisai.get("genyokin2")).doubleValue() > 0) {
						// ④支払日　諸口/現預金　　差引支給金額
						shiwake.setKashi(comChu.makeKashikata(meisai,
								denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2,
								(String) meisai.get("denpyou_kbn")), null, null, null);
						sc.setVALU((BigDecimal) meisai.get("genyokin2"));
						swkKariShokuchi.add(shiwake);
					} else if ("1".equals(meisai.get("houjin_card_riyou_flg"))) {
						// ⑤支払日　諸口/法人ｶｰﾄﾞ　法人カード利用金額合計
						shiwake.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null,
								null, shikibetsuBangou);
						sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
						swkKariShokuchi.add(shiwake);
					} else if ("1".equals(meisai.get("kaisha_tehai_flg"))) {
						// ⑥支払日　諸口/会社手配　会社手配利用金額合計
						shiwake.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null,
								null, null);
						sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
						swkKariShokuchi.add(shiwake);
					} else {
						continue;
					}
				}

				// 差引税額の考慮
				if (shouldConsiderSashihikiZeigaku) {
					this.makeSashihikiZeiShiwake(denpyou, dto, shiwakes, isFukugou, isHassei, shainNo, main);
				}
			} else {
				// 発生主義
				// 8:精算－複合形式・発生主義・仮払相殺なし
				// 9:精算－複合形式・発生主義・仮払相殺あり(計上日)
				//シート8,9用
				// 残金を計上する または 返金額がない場合
				// ①計上日  経費 / 諸口   明細金額
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));
					String meisaiShainNo = comChu.getShainCdRenkeiFlg(meisai, (String) meisai.get("denpyou_kbn"))
							? (String) meisai.get("shain_no")
							: null;

					boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn"))
							|| "013".equals(meisai.get("kari_kazei_kbn"))
							|| "014".equals(meisai.get("kari_kazei_kbn")));
					String kingakuStr = "shiharai_kingaku";
					if (isZeinuki) {
						kingakuStr = "hontai_kingaku";
					}

					ShiwakeData shiwake = new ShiwakeData();
					swkKeihiShokuchi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,
							meisaiShainNo);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(keijoubi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					sc.setVALU((BigDecimal) meisai.get(kingakuStr));
					sc.setBUNRI(common.getBunriKbnForShiwake(meisai));
					if (isZeinuki) {
						ShiwakeData shiwakeZei = new ShiwakeData();
						swkKeihiShokuchi.add(shiwakeZei);
						shiwakeZei.setMain(main);
						shiwakeZei.setKari(comChu.makeKarikata(meisai, (String) meisai.get("denpyou_kbn")), denpyouId,
								meisaiShainNo);
						shiwakeZei.setKashi(comChu.makeShokuchi(), null, null, null);

						ShiwakeDataRS r = shiwakeZei.getR();
						r.setKMK(common.getZeiKamokuGaibuCd(meisai, dto));

						r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
						r.setSRE("");
						//消費税仕訳　消費税設定により空にする
						common.setZeitaishouKoumoku(r, dto);

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
						scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) meisai.get("denpyou_kbn"), denpyou, meisai, "0")));
						scZei.setTNO((String) meisai.get("tekiyou_cd"));
						scZei.setVALU((BigDecimal) meisai.get("shouhizeigaku"));
//						scZei.setBUNRI(common.getBunriKbnForShiwake(meisai));
					}
				}

				// 残金仕訳をする設定かつ返金額ありの場合、明細1行目の仮払残金仕訳を作成
				// ②計上日　現金(精算の貸方2)/諸口　返金額 (仮払金額－税込金額合計(法カ、手配以外))
				if (shouldSakuseiZankin && henkinKingaku.doubleValue() > 0) {
					// 残金仕訳をする設定かつ返金額ありの場合、明細1行目の仮払残金仕訳を作成
					// 発生主義：計上日  現金(精算の貸方2) / 諸口   返金額
					ShiwakeData shiwake = new ShiwakeData();
					swkKashiShokuchi.add(shiwake);
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeKashikata(meisai1, 2, (String) meisai1.get("denpyou_kbn")), null, null);
					shiwake.setKashi(comChu.makeShokuchi(), null, null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(keijoubi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "3")));
					sc.setTNO("");
					sc.setVALU(henkinKingaku);

					//残金分の諸口/仮払金仕訳も作成
					ShiwakeData shiwakeKb = new ShiwakeData();
					shiwakeKb.setMain(main);
					shiwakeKb.setKari(comChu.makeShokuchi(), null, null);
					shiwakeKb.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
							comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

					ShiwakeDataCom scKb = shiwakeKb.getCom();
					scKb.setDYMD(keijoubi);
					scKb.setDCNO(common.serialNo(denpyou.get("serial_no")));
					scKb.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai1, "1")));
					scKb.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
					scKb.setVALU(henkinKingaku);
					swkKariShokuchi.add(shiwakeKb);
				}

				// 仮払相殺仕訳・未払仕訳・法人カード・会社手配分の仕訳作成(最後にmergeShiwakeWithoutBunkatsuで結合)
				for (int i = 0; i < meisaiList.size(); i++) {
					GMap meisai = comChu.merge(denpyou, meisaiList.get(i));

					if ((meisai.get("karibarai2") != null)
							&& ((BigDecimal) meisai.get("karibarai2")).doubleValue() > 0) {
						// ③計上日　諸口/仮払金　　仮払金額
						ShiwakeData shiwakeKb = new ShiwakeData();
						shiwakeKb.setMain(main);
						shiwakeKb.setKari(comChu.makeShokuchi(), null, null);
						shiwakeKb.setKashi(comChu.makeKarikata(karibaraiDenpyou), kariBaraiDenpyouId,
								comChu.getShainCdRenkeiFlg(karibaraiDenpyou) ? shainNo : null, null);

						ShiwakeDataCom scKb = shiwakeKb.getCom();
						scKb.setDYMD(keijoubi);
						scKb.setDCNO(common.serialNo(denpyou.get("serial_no")));
						scKb.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai, "1")));
						scKb.setTNO((String) karibaraiDenpyou.get("tekiyou_cd"));
						scKb.setVALU((BigDecimal) meisai.get("karibarai2")); //複合
						swkKariShokuchi.add(shiwakeKb);
					}

					ShiwakeData shiwake = new ShiwakeData();
					shiwake.setMain(main);
					shiwake.setKari(comChu.makeShokuchi(), null, null);

					ShiwakeDataCom sc = shiwake.getCom();
					sc.setDYMD(keijoubi);
					sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
					sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai, "0")));
					sc.setTNO((String) meisai.get("tekiyou_cd"));
					if ((meisai.get("genyokin2") != null) && ((BigDecimal) meisai.get("genyokin2")).doubleValue() > 0) {
						// ④計上日　諸口/未払金　　差引支給金額
						shiwake.setKashi(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null,
								null, null);
						sc.setVALU((BigDecimal) meisai.get("genyokin2"));
						swkKariShokuchi.add(shiwake);

						// ⑦⑧支払日  未払金 / 現預金   差引支給金額(mergeShiwakeで諸口分割)
						ShiwakeData shiwakeMk = new ShiwakeData();
						shiwakeMk.setMain(main);
						shiwakeMk.setKari(comChu.makeKashikata(meisai, 3, (String) meisai.get("denpyou_kbn")), null,
								null);
						shiwakeMk.setKashi(comChu.makeKashikata(meisai,
								denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2,
								(String) meisai.get("denpyou_kbn")), null, null, null);

						ShiwakeDataCom scMk = shiwakeMk.getCom();
						scMk.setDYMD(shiharaibi);
						scMk.setDCNO(common.serialNoPlus(denpyou.get("serial_no")));
						scMk.setTKY(common.cutTekiyou(common.shiwakeTekiyou(seisanDenKbn, denpyou, meisai, "0")));
						scMk.setTNO((String) meisai.get("tekiyou_cd"));
						scMk.setVALU((BigDecimal) meisai.get("genyokin2"));
						swkMibaraiKeshikomi.add(shiwakeMk);

					} else if ("1".equals(meisai.get("houjin_card_riyou_flg"))) {
						// ⑤計上日　諸口/法人ｶｰﾄﾞ　法人カード利用金額合計
						shiwake.setKashi(comChu.makeKashikata(meisai, 4, (String) meisai.get("denpyou_kbn")), null,
								null, shikibetsuBangou);
						sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
						swkKariShokuchi.add(shiwake);
					} else if ("1".equals(meisai.get("kaisha_tehai_flg"))) {
						// ⑥計上日　諸口/会社手配　会社手配利用金額合計
						shiwake.setKashi(comChu.makeKashikata(meisai, 5, (String) meisai.get("denpyou_kbn")), null,
								null, null);
						sc.setVALU((BigDecimal) meisai.get("shiharai_kingaku"));
						swkKariShokuchi.add(shiwake);
					} else {
						continue;
					}
				}

				// 差引税額の考慮
				if (shouldConsiderSashihikiZeigaku) {
					this.makeSashihikiZeiShiwake(denpyou, dto, shiwakes, isFukugou, isHassei, shainNo, main);
				}
			}

			//国内出張の交通費・日当等は本来借方１つなのだけど、便宜上一旦明細に分割されて処理するので、ここで１つに統合
			if (seisanDenKbn.equals(DENPYOU_KBN.RYOHI_SEISAN)) {
				List<ShiwakeData> swkKeihiShokuchiKoutsu = new ArrayList<>();
				List<ShiwakeData> swkKeihiShokuchiSonota = new ArrayList<>();
				for (ShiwakeData swk : swkKeihiShokuchi) {
					if (swk.getR().getDenpyouKbn().equals(DENPYOU_KBN.RYOHI_SEISAN)) {
						swkKeihiShokuchiKoutsu.add(swk);
					} else { //DENPYOU_KBN.KEIHI_TATEKAE_SEISAN
						swkKeihiShokuchiSonota.add(swk);
					}
				}
				swkKeihiShokuchiKoutsu = common.mergeShiwakeWithoutBunkatsu(swkKeihiShokuchiKoutsu, true, false);
				swkKeihiShokuchi.clear();
				swkKeihiShokuchi.addAll(swkKeihiShokuchiKoutsu);
				swkKeihiShokuchi.addAll(swkKeihiShokuchiSonota);
			}
			shiwakes.addAll(swkKeihiShokuchi);
			//借方集約
			shiwakes.addAll(common.mergeShiwakeWithoutBunkatsu(swkShokuchiMibarai, false, true));
			shiwakes.addAll(common.mergeShiwakeWithoutBunkatsu(swkKashiShokuchi, true, false));
			shiwakes.addAll(common.mergeShiwakeWithoutBunkatsu(swkKariShokuchi, false, true));
			shiwakes.addAll(common.mergeShiwake(swkMibaraiKeshikomi, true, true));
		}
		return shiwakes;
	}
	
	/**
	 * 差引税額分の仕訳の作成。これまであちこちにクローンしたら悲惨になるのでメソッドに分離して作る
	 * @param denpyou 伝票
	 * @param kiShouhizeiSettingDto 期消費税設定
	 * @param shiwakes 仕訳データリスト
	 * @param isFukugou 複合か
	 * @param isHassei 発生か
	 * @param main データ抽出のメイン部分
	 */
	protected void makeSashihikiZeiShiwake(GMap denpyou, KiShouhizeiSetting kiShouhizeiSettingDto, List<ShiwakeData> shiwakes, boolean isFukugou, boolean isHassei, String shainNo, ShiwakeDataMain main) {
		// 重要なのは相手科目
		// 複合なら借方・諸口
		// 単一なら借方・未払金
		// 差し引いて生まれるものなので、仮払の影響はない…
		String denpyouShainNo = comChu.getShainCdRenkeiFlg(denpyou)
				? (String) denpyou.get("shain_no")
				: null;
		ShiwakeData shiwakeZei = new ShiwakeData();
		shiwakes.add(shiwakeZei);
		shiwakeZei.setMain(main);
		shiwakeZei.setKari(isFukugou ? comChu.makeShokuchi() : comChu.makeKashikata(denpyou, 3),
				null, null);
		shiwakeZei.setKashi(comChu.makeKarikata(denpyou, (String) denpyou.get("denpyou_kbn")),
				denpyou.get("denpyou_id"), shainNo, null);
		ShiwakeDataRS shiwakeZeiKashikata = shiwakeZei.getS();
		shiwakeZeiKashikata.setKMK(common.getZeiKamokuGaibuCd(denpyou, kiShouhizeiSettingDto));

		shiwakeZeiKashikata.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
		shiwakeZeiKashikata.setSRE("");
		//消費税仕訳　消費税設定により空にする
		common.setZeitaishouKoumoku(shiwakeZeiKashikata,kiShouhizeiSettingDto);

		ShiwakeDataRS hontaiShiwakeKarikata = comChu.makeKarikata(denpyou);
		//消費税対象科目
		shiwakeZei.setZKMK(hontaiShiwakeKarikata.getKMK());
		shiwakeZei.setZRIT(hontaiShiwakeKarikata.getRIT());
		shiwakeZei.setZKEIGEN(hontaiShiwakeKarikata.getKEIGEN());
		shiwakeZei.setZZKB(hontaiShiwakeKarikata.getZKB());
		shiwakeZei.setZGYO(shiwakeZei.getRGYO());
		shiwakeZei.setZSRE(hontaiShiwakeKarikata.getSRE());
		shiwakeZei.setZURIZEIKEISAN("0");
		shiwakeZei.setZMENZEIKEIKA("0");

		ShiwakeDataCom shiwakeDataComZei = shiwakeZei.getCom();
		shiwakeDataComZei.setDYMD(isHassei ? (Date)denpyou.get("keijoubi") : (Date)denpyou.get("shiharaibi"));
		shiwakeDataComZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
		shiwakeDataComZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou((String) denpyou.get("denpyou_kbn"),
				denpyou, denpyou, "0")));
		shiwakeDataComZei.setTNO((String) denpyou.get("tekiyou_cd"));
		shiwakeDataComZei.setVALU((BigDecimal) denpyou.get("sashihiki_zeigaku"));
		//								scZei.setBUNRI(common.getBunriKbnForShiwake(denpyou));
	}
}