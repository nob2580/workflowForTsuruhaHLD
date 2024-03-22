package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 海外出張旅費精算抽出バッチ
 */
public class KaigaiRyohiSeisanChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaigaiRyohiSeisanChuushutsuBat.class);

	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommon;
	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	KaigaiRyohiSeisanChuushutsuLogic gyoumuLogic;
	/** 経費立替ロジック */
	protected KeihiTatekaeSeisanChuushutsuLogic tatekaeLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** マスタ管理ロジック */
	MasterKanriCategoryLogic masterCommonLogic;

	@Override
	public String getBatchName() {
		return "海外出張旅費精算（仮払精算）抽出";
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

			//承認済旅費精算伝票を取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("海外旅費精算抽出データ件数：" + denpyouList.size());

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
	 * @param denpyou 伝票と旅費精算の結合データ
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		
		//「出張中止」オンの伝票の場合は仕訳データ作成済フラグのみセットして終了(仕訳作成しない)
		String shucchouChuushiFlg = (String)denpyou.get("shucchou_chuushi_flg");
		if("1".equals(shucchouChuushiFlg)){
			if (onMemory)
			{
				return null;
			}
			common.updateChuushutsuZumi(denpyouId);
			return null;
		}
		
		List<GMap> meisaiList = gyoumuLogic.loadKeihiMeisai(denpyouId);
		List<GMap> ryohiMeisaiList = gyoumuLogic.loadRyohiMeisai(denpyouId);
		
		// 仮払の取得
		GMap karibaraiDenpyou = null;
		BigDecimal karibaraiKingaku = null;
		String kariBaraiDenpyouId = (String)denpyou.get("karibarai_denpyou_id");
		if (StringUtils.isNotEmpty(kariBaraiDenpyouId)) { 
			karibaraiDenpyou = common.findKaigaiRyouhiKaribarai(kariBaraiDenpyouId); 
			if (karibaraiDenpyou != null) {
				karibaraiKingaku = (BigDecimal) karibaraiDenpyou.get("karibarai_kingaku");
			}
		}
		
		// 差引支給金額
		BigDecimal shikyuuKingaku = (BigDecimal) denpyou.get("sashihiki_shikyuu_kingaku");
		boolean isShikyuuKingaku = (shikyuuKingaku != null && shikyuuKingaku.doubleValue() > 0);
		
		// 差引金額
		BigDecimal sashihikiKingaku = (BigDecimal)denpyou.get("sashihiki_kingaku");
		BigDecimal sashihikiKingakuKaigai = (BigDecimal)denpyou.get("sashihiki_kingaku_kaigai");

		String yakushokuCd = kaikeiCommon.getYakushokuCd(denpyou.get("user_id"));


		//======================================
		//交通費をその他経費と同じ形にする（後続処理は交通費とその他経費同じ）
		//======================================
		
		for(int i = ryohiMeisaiList.size() - 1; i >= 0; i--){
			GMap ryohiMeisai = ryohiMeisaiList.get(i);
			GMap meisai = comChu.merge(denpyou); meisaiList.add(0, meisai);
			boolean kaigaiFlg = ryohiMeisai.get("kaigai_flg").equals("1");
			String shubetsu1 = (String)ryohiMeisai.get("shubetsu1");
			String shubetsu2 = (String)ryohiMeisai.get("shubetsu2");
			
			meisai.put("shiharai_kingaku", ryohiMeisai.get("meisai_kingaku"));
			meisai.put("hontai_kingaku", ryohiMeisai.get("zeinuki_kingaku"));
			meisai.put("shouhizeigaku", ryohiMeisai.get("shouhizeigaku"));
			meisai.put("kari_kazei_kbn", denpyou.get("kari_kazei_kbn"));
			meisai.put("houjin_card_riyou_flg", ryohiMeisai.get("houjin_card_riyou_flg"));
			meisai.put("kaisha_tehai_flg", ryohiMeisai.get("kaisha_tehai_flg"));
			meisai.put("denpyou_kbn", ryohiMeisai.get("denpyou_kbn"));
			meisai.put("jigyousha_kbn",ryohiMeisai.get("jigyousha_kbn"));

			//kaigai_hogeをhogeとしてセットする。例)kaigai_kari_kamoku_cd→kari_kamoku_cd
			if(kaigaiFlg){
				gyoumuLogic.trimKaigai(meisai);
			}

			//交通手段・日当等マスターから借方枝番を付与
			boolean edabanRenkei = kaigaiFlg ? comChu.getEdabanRenkeiFlg(meisai,DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) : comChu.getEdabanRenkeiFlg(meisai,DENPYOU_KBN.RYOHI_SEISAN);
			if(edabanRenkei) {
				common.setMasterEdaban(ryohiMeisai, yakushokuCd);
				meisai.put("kari_kamoku_edaban_cd", ryohiMeisai.get("kari_kamoku_edaban_cd"));
			}
			
			// 課税区分を上書き
			String kariKazeiKbn = common.cnvZeiKbn((String)meisai.get("kari_kazei_kbn")
				 	, kaigaiFlg
				 	, (String)ryohiMeisai.get("shubetsu_cd")
				 	, (String)ryohiMeisai.get("koutsuu_shudan")
				 	, shubetsu1
				 	, shubetsu2
				 	, yakushokuCd);
			meisai.put("kari_kazei_kbn", kariKazeiKbn);
			
			// 明細金額を上書き
			if((!"1".equals(meisai.get("houjin_card_riyou_flg"))) && (!"1".equals(meisai.get("kaisha_tehai_flg")))){
				BigDecimal sashihikiKingakuTmp = (kaigaiFlg)? sashihikiKingakuKaigai : sashihikiKingaku;
				if(sashihikiKingakuTmp.compareTo(BigDecimal.ZERO) > 0 
						&& common.isNittou(kaigaiFlg, shubetsu1, shubetsu2, yakushokuCd)){
					List<BigDecimal> kingakuList = common.subtractSasihikiKingaku((BigDecimal)ryohiMeisai.get("meisai_kingaku"), sashihikiKingakuTmp);
					meisai.put("shiharai_kingaku", kingakuList.get(0));
					if(kaigaiFlg){
						sashihikiKingakuKaigai = kingakuList.get(1);
					}else{
						sashihikiKingaku = kingakuList.get(1);
					}
				}
			}
			
		}
		gyoumuLogic.trimKaigai(denpyou);//本体のkaigai_hogeをhogeに変えるkaigai_の方がメインなので


		//======================================
		// 明細金額を仮払分と立替分に分ける
		//======================================
		tatekaeLogic.calcKariTate(meisaiList, karibaraiKingaku, DENPYOU_KBN.KAIGAI_RYOHI_SEISAN, denpyou);


		//======================================
		//ケースごとに仕訳作成
		//======================================
		List<ShiwakeData> shiwakes = tatekaeLogic.makeSeisanCommon(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN, DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, denpyou, meisaiList, karibaraiDenpyou);
		if (onMemory)
		{
			return shiwakes;
		}
		swkLogic.insert(shiwakes);

		//======================================
		//FBデータ作成
		//======================================
		// 旅費精算.支払方法 = 「振込」かつ 旅費精算.差引支給金額 > 0 の場合
		// FBデータ作成
		boolean fbDataMade = false;
		
		if ((SHIHARAI_HOUHOU.FURIKOMI.equals(denpyou.get("shiharaihouhou"))) && isShikyuuKingaku) {
			// 通常起票ならば起票者口座情報、代理起票ならば使用者口座情報を取得
			GMap shainKouza = common.findKihyouShainKouza((String)denpyou.get("user_id"));
			// FBデータを作成
			common.makeFBData(denpyouId, (Date)denpyou.get("shiharaibi"), shikyuuKingaku, (String)denpyou.get("user_id"), shainKouza);
			fbDataMade = true;
		}

		
		//======================================
		//後処理
		//======================================
		// 承認状況登録処理
		common.insertSyouninJoukyou(denpyouId, fbDataMade);

		//済
		common.updateChuushutsuZumi(denpyouId);
		return shiwakes;
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A011;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		kaikeiCommon = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(KaigaiRyohiSeisanChuushutsuLogic.class, connection);
		tatekaeLogic = EteamContainer.getComponent(KeihiTatekaeSeisanChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
	}
}
