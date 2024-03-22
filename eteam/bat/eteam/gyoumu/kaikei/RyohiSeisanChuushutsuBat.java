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
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;

/**
 * 旅費精算抽出バッチ
 */
public class RyohiSeisanChuushutsuBat extends EteamAbstractKaikeiBat {

//＜部品＞
	/** ロガー */
	static EteamLogger log = EteamLogger.getLogger(RyohiSeisanChuushutsuBat.class);

	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommon;
	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	RyohiSeisanChuushutsuLogic gyoumuLogic;
	/** 経費立替ロジック */
	KeihiTatekaeSeisanChuushutsuLogic tatekaeLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** WorkflowEventControlLogic（端数処理フラグ取得用） */
	WorkflowEventControlLogic workflowEventControlLogic;

	@Override
	public String getBatchName() {
		return "出張旅費精算（仮払精算）抽出";
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
			log.info("旅費精算抽出データ件数：" + denpyouList.size());

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
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A004;
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
		
		// 仮払の取得
		GMap karibaraiDenpyou = null;
		BigDecimal karibaraiKingaku = null;
		String kariBaraiDenpyouId = (String)denpyou.get("karibarai_denpyou_id");
		if (StringUtils.isNotEmpty(kariBaraiDenpyouId)) { 
			karibaraiDenpyou = common.findRyouhiKaribarai(kariBaraiDenpyouId); 
			if (karibaraiDenpyou != null) {
				karibaraiKingaku = (BigDecimal) karibaraiDenpyou.get("karibarai_kingaku");
			}
		}
		
		// 差引支給金額
		BigDecimal shikyuuKingaku = (BigDecimal) denpyou.get("sashihiki_shikyuu_kingaku");
		boolean isShikyuuKingaku = (shikyuuKingaku != null && shikyuuKingaku.doubleValue() > 0);

		String yakushokuCd = kaikeiCommon.getYakushokuCd(denpyou.get("user_id"));

		boolean edabanRenkei = false;
		if( ! ("1".equals(denpyou.get("karibarai_mishiyou_flg"))) ){
			edabanRenkei = comChu.getEdabanRenkeiFlg(denpyou, DENPYOU_KBN.RYOHI_SEISAN);
		}

		
		//======================================
		//交通費をその他経費と同じ形にする（後続処理は交通費とその他経費同じ）
		//======================================

		//DBのままの交通費と日当等の明細
		List<GMap> ryohiMeisaiList = gyoumuLogic.loadRyohiMeisai(denpyouId);

		//差引金額分を日当明細明細から差し引く
		//例) 差引金額が1000×3 = 3000円だったとする
		//交通費#1 3000円 → 3000円
		//交通費#2 4000円 → 4000円
		//日当等#1 8000円 → 8000円(宿泊費)
		//日当等#2 4000円 → 1000円(日当)
		var isZeinuki = List.of("002", "013", "014").contains(denpyou.get("kari_kazei_kbn"));
		BigDecimal sashihikiKingaku = denpyou.get("sashihiki_kingaku");
		denpyou.put("sashihiki_zeigaku", gyoumuLogic.meisaiSashihikiAndGetSashihikiZeigaku(ryohiMeisaiList, sashihikiKingaku, yakushokuCd, isZeinuki, denpyou.get("zeiritsu")));
		
		log.info("差引税額:" + denpyou.get("sashihiki_zeigaku"));
		
		//金額0の明細は削除
		removeKingakuZeroMeisai(ryohiMeisaiList);

		//交通手段・日当等マスターから借方枝番を付与
		if(edabanRenkei) {
			common.setMasterEdaban(ryohiMeisaiList, yakushokuCd);
		}
		
		//借方枝番と支払方法(CとかKとか)が同じやつで合算
		//交通費#1 3000円 → 7000円
		//交通費#2 4000円 → #1に合算されて消える
		//日当等#1 8000円 → 8000円(宿泊費)
		//日当等#2 1000円 → 1000円(日当)
		ryohiMeisaiList = common.mergeRyohiMeisai(ryohiMeisaiList);
		
		// 交通費・日当等明細(借方枝番と支払方法での合算後)をその他明細と同様に扱えるようにする
		for(int i = ryohiMeisaiList.size() - 1; i >= 0; i--){
			GMap ryohiMeisai = ryohiMeisaiList.get(i);
			String shiharai = "";
			if (ryohiMeisai.get("houjin_card_riyou_flg").equals("1"))
			{
				shiharai = "C";
			}
			if (ryohiMeisai.get("kaisha_tehai_flg").equals("1"))
			{
				shiharai = "K";
			}

			GMap meisai = comChu.merge(denpyou);
//			meisaiList.add(0, meisai);
			if(edabanRenkei) {
				meisai.put("kari_kamoku_edaban_cd", ryohiMeisai.get("kari_kamoku_edaban_cd"));
			}
			meisai.put("shiharai_kingaku", ryohiMeisai.get("meisai_kingaku"));
			meisai.put("hontai_kingaku", ryohiMeisai.get("zeinuki_kingaku"));
			meisai.put("shouhizeigaku", ryohiMeisai.get("shouhizeigaku"));
			meisai.put("kari_kazei_kbn", denpyou.get("kari_kazei_kbn"));
			meisai.put("houjin_card_riyou_flg", "0");
			meisai.put("kaisha_tehai_flg", "0");
			if (shiharai.equals("C")) {
				meisai.put("houjin_card_riyou_flg", "1");
			}
			if (shiharai.equals("K")) {
				meisai.put("kaisha_tehai_flg", "1");
			}
			meisai.put("jigyousha_kbn",ryohiMeisai.get("jigyousha_kbn"));
			
			meisaiList.add(0, meisai);
		}


		//======================================
		// 明細金額を仮払分と立替分に分ける
		//======================================
		tatekaeLogic.calcKariTate(meisaiList, karibaraiKingaku, DENPYOU_KBN.RYOHI_SEISAN, denpyou);


		//======================================
		//ケースごとに仕訳作成
		//======================================
		List<ShiwakeData> shiwakes = tatekaeLogic.makeSeisanCommon(DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI, denpyou, meisaiList, karibaraiDenpyou);
		
		// TODO 差引税額が残っている時の未払金/仮払消費税や諸口/仮払消費税
		
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

	/**
	 * 初期化
	 */
	protected void initialize() {
		kaikeiCommon = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(RyohiSeisanChuushutsuLogic.class, connection);
		tatekaeLogic = EteamContainer.getComponent(KeihiTatekaeSeisanChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		workflowEventControlLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
	}
	
	/**
	 * 金額ゼロの明細を削除したリストを返却
	 * @param orgList 交通費・日当等の明細
	 */
	protected void removeKingakuZeroMeisai(List<GMap> orgList){
		for(int i = orgList.size() - 1 ; i >= 0 ; i-- ) {
			GMap mp = orgList.get(i);
			if( ((BigDecimal)mp.get("meisai_kingaku")).doubleValue() == 0) {orgList.remove(i);}
		}
	}
}
