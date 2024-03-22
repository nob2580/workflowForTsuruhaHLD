package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;

/**
 * 旅費精算抽出ロジック
 */
public class RyohiSeisanChuushutsuLogic extends EteamAbstractLogic  {

	/**
	 * 初期化
	 * @param _connection コネクション
	 */
	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
	}

	/**
	 * 抽出対象の伝票を取得
	 * @param denpyouId 伝票ID
	 * @return 抽出対象の伝票
	 */
	public GMap findDenpyou(String denpyouId) {
		String sql =
				"SELECT * "
			+ "  ,(COALESCE((SELECT SUM(meisai_kingaku) FROM ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.houjin_card_riyou_flg <> '1' AND m.kaisha_tehai_flg <> '1'), 0) "
			+ "    - COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0)) ryohi_kingaku "
			+ "  ,COALESCE((SELECT SUM(meisai_kingaku) FROM ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.houjin_card_riyou_flg = '1'), 0) ryohi_kingaku_houjin "
			+ "  ,COALESCE((SELECT SUM(meisai_kingaku) FROM ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.kaisha_tehai_flg = '1'), 0) ryohi_kingaku_kaisha_tehai "
			+   "  ,COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0) sashihiki_kingaku "
			+ "FROM denpyou d "
			+ "INNER JOIN ryohiseisan k ON "
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
			+ "  ,(COALESCE((SELECT SUM(meisai_kingaku) FROM ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.houjin_card_riyou_flg <> '1' AND m.kaisha_tehai_flg <> '1'), 0) "
			+ "    - COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0)) ryohi_kingaku "
			+ "  ,COALESCE((SELECT SUM(meisai_kingaku) FROM ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.houjin_card_riyou_flg = '1'), 0) ryohi_kingaku_houjin "
			+ "  ,COALESCE((SELECT SUM(meisai_kingaku) FROM ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.kaisha_tehai_flg = '1'), 0) ryohi_kingaku_kaisha_tehai "
			+   "  ,COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0) sashihiki_kingaku "
			+ "FROM denpyou d "
			+ "INNER JOIN ryohiseisan k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? "
			+ "  AND d.denpyou_joutai = ? "
			+ "  AND d.chuushutsu_zumi_flg = '0' "
			+ "  AND NOT EXISTS (SELECT * FROM :SHIWAKE s WHERE d.denpyou_id = s.denpyou_id) "
// +	"  AND d.denpyou_id='180328-A004-0000002' " //UT
			+ "ORDER BY "
			+ "  d.denpyou_id ASC";
		
		if (Open21Env.getVersion() == Version.DE3 ) {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_de3");
		} else {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_sias");
		}
		
		return connection.load(sql, DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}
	
	/**
	 * 出張旅費精算の旅費明細レコードをSELECTする
	 * @param denpyouId 伝票ID
	 * @return 承認済出張旅費明細データ
	 */
	public List<GMap> loadRyohiMeisai(String denpyouId) {
		final String sql =
				"SELECT * "
			+ "  ,'A004' AS denpyou_kbn "
			+ "FROM ryohiseisan_meisai m "
			+ "WHERE "
			+ "  m.denpyou_id = ? "
			+ "ORDER BY "
			+ "  m.denpyou_edano ASC";
		
		return connection.load(sql, denpyouId);
	}

	/**
	 * 出張旅費精算の経費明細レコードをSELECTする
	 * @param denpyouId 伝票ID
	 * @return 承認済出張経費明細データ
	 */
	public List<GMap> loadKeihiMeisai(String denpyouId) {
		final String sql =
				"SELECT * "
			+ "  ,'A001' AS denpyou_kbn "
			+ "FROM ryohiseisan_keihi_meisai m "
			+ "WHERE "
			+ "  m.denpyou_id = ? "
			+ "ORDER BY "
			+ "  m.denpyou_edano ASC";
		
		return connection.load(sql, denpyouId);
	}

	/**
	 * 明細から差引金額を引いて、meisai_kingakuをセットしなおす
	 * @param ryohiMeisaiList 交通費・日当等の明細
	 * @param sashihikiKingaku 差引金額
	 * @param yakushokuCd 役職コード
	 * @param isZeinuki 税抜であるか？
	 * @param zeiritsu 税率
	 * @return 差引税額
	 */
	public BigDecimal meisaiSashihikiAndGetSashihikiZeigaku(List<GMap> ryohiMeisaiList, BigDecimal sashihikiKingaku, String yakushokuCd, boolean isZeinuki, BigDecimal zeiritsu) {
		
		// 0の時は意味がないので終わり
		if(sashihikiKingaku.compareTo(BigDecimal.ZERO) <= 0){
			return BigDecimal.ZERO;
		}
		
		BatchKaikeiCommonLogic batchKaikeiCommonLogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		WorkflowEventControlLogic workflowEventControlLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		BigDecimal sashihikiZeigaku = kaikeiCommonLogic.processShouhizeiFraction(workflowEventControlLogic.judgeHasuuKeisan(), sashihikiKingaku.multiply(zeiritsu).divide(new BigDecimal("100").add(zeiritsu), 3, RoundingMode.HALF_UP), true);

		//差引優先度（日当、宿泊費、その他、交通費、明細番号）の順にソートする
		ryohiMeisaiList.sort(new Comparator<GMap>() {
			public int compare(GMap a, GMap b) {
				return RyohiMeisaiComparer.compareRyohiMeisaiForSashihiki(a, b, batchKaikeiCommonLogic, yakushokuCd);
			}
		});

		//差引金額を明細の金額から引いていく※0円になった場合は後でしょりされない
		for(GMap ryohiMeisai : ryohiMeisaiList) {
			if((!"1".equals(ryohiMeisai.get("houjin_card_riyou_flg"))) && (!"1".equals(ryohiMeisai.get("kaisha_tehai_flg")))){
				List<BigDecimal> kingakuList = batchKaikeiCommonLogic.subtractSasihikiKingaku(ryohiMeisai.get("meisai_kingaku"), sashihikiKingaku);
				ryohiMeisai.put("meisai_kingaku", kingakuList.get(0));
				sashihikiKingaku = kingakuList.get(1);
				// 税抜の場合は、税額も減算
				if(isZeinuki) {
					List<BigDecimal> zeigakuList = batchKaikeiCommonLogic.subtractSasihikiKingaku(ryohiMeisai.get("shouhizeigaku"), sashihikiZeigaku);
					ryohiMeisai.put("shouhizeigaku", zeigakuList.get(0));
					ryohiMeisai.put("zeinuki_kingaku", kingakuList.get(0).subtract(zeigakuList.get(0)));
					sashihikiZeigaku = zeigakuList.get(1);
				}
				// 金額及び税抜の場合の税額の差引が終わったらbreak
				if (sashihikiKingaku.doubleValue() <= 0 && (!isZeinuki || sashihikiZeigaku.doubleValue() <= 0))
				{
					break;
				}
			}
		}

		//明細番号の順にソートしなおす
		ryohiMeisaiList.sort(new Comparator<GMap>() {
			public int compare(GMap a, GMap b) {
				return RyohiMeisaiComparer.compareByEdano(a, b);
			}
		});
		
		return sashihikiZeigaku;
	}
}
	
/**
 * ソート部が長いので別クラスに切り出す
 */
class RyohiMeisaiComparer
{
	/**
	 * @param a 明細A
	 * @param b 明細B
	 * @param common BatchKaikeiCommonLogic
	 * @param yakushokuCd 役職コード
	 * @return 大小
	 */
	protected static int compareRyohiMeisaiForSashihiki(GMap a, GMap b, BatchKaikeiCommonLogic common, String yakushokuCd) {
		int no_a  = a.get("denpyou_edano");
		String shubetsuCd_a  = a.get("shubetsu_cd");
		String shubetsu1_a  = a.get("shubetsu1");
		String shubetsu2_a  = a.get("shubetsu2");
		int no_b  = b.get("denpyou_edano");
		String shubetsuCd_b  = b.get("shubetsu_cd");
		String shubetsu1_b  = b.get("shubetsu1");
		String shubetsu2_b  = b.get("shubetsu2");

		//交通費同士なら明細番号の順
		if(shubetsuCd_a.equals("1") && shubetsuCd_b.equals("1")) {
			return (no_a < no_b) ? -1 : 1;

		//日当同士なら日当(2)、宿泊費(1)、その他(0)の順、同類なら明細番号の順
		} else if(shubetsuCd_a.equals("2") && shubetsuCd_b.equals("2")) {
			String flg_a = common.findNittouShukuhakuhiFlg(false, shubetsu1_a, shubetsu2_a, yakushokuCd);
			String flg_b = common.findNittouShukuhakuhiFlg(false, shubetsu1_b, shubetsu2_b, yakushokuCd);
			if(flg_a.equals(flg_b)) {
				return (no_a < no_b) ? -1 : 1;
			}else {
				return - flg_a.compareTo(flg_b);
			}

		//交通費と日当等なら日当等が先
		} else if(shubetsuCd_a.equals("1") && shubetsuCd_b.equals("2")) {
			return 1;
		} else if(shubetsuCd_a.equals("2") && shubetsuCd_b.equals("1")) {
			return -1;
		}
		return 0;
	}
	
	/**
	 * @param a 明細A
	 * @param b 明細B
	 * @return 大小
	 */
	public static int compareByEdano(GMap a, GMap b) {
		int no_a  = a.get("denpyou_edano");
		int no_b  = b.get("denpyou_edano");
		return (no_a < no_b) ? -1 : 1;
	}
}