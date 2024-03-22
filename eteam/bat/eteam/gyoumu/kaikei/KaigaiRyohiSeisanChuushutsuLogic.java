package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;

/**
 * 海外出張旅費精算抽出ロジック
 */
public class KaigaiRyohiSeisanChuushutsuLogic extends EteamAbstractLogic  {

	/**
	 * 抽出対象の伝票を取得
	 * @param denpyouId 伝票ID
	 * @return 抽出対象の伝票
	 */
	public GMap findDenpyou(String denpyouId) {
		String sql =
				"SELECT * "
			+ "  ,(COALESCE((SELECT SUM(meisai_kingaku) FROM kaigai_ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.houjin_card_riyou_flg <> '1' AND m.kaisha_tehai_flg <> '1'), 0) "
			+ "    - COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0) - COALESCE(k.sashihiki_num_kaigai, 0) * COALESCE(k.sashihiki_tanka_kaigai, 0)) ryohi_kingaku "
			+   "  ,COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0) sashihiki_kingaku "
			+   "  ,COALESCE(k.sashihiki_num_kaigai, 0) * COALESCE(k.sashihiki_tanka_kaigai, 0) sashihiki_kingaku_kaigai "
			+ "FROM denpyou d "
			+ "INNER JOIN kaigai_ryohiseisan k ON "
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
			+ "  ,(COALESCE((SELECT SUM(meisai_kingaku) FROM kaigai_ryohiseisan_meisai m WHERE m.denpyou_id = d.denpyou_id AND m.houjin_card_riyou_flg <> '1' AND m.kaisha_tehai_flg <> '1'), 0) "
			+ "    - COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0) - COALESCE(k.sashihiki_num_kaigai, 0) * COALESCE(k.sashihiki_tanka_kaigai, 0)) ryohi_kingaku "
			+   "  ,COALESCE(k.sashihiki_num, 0) * COALESCE(k.sashihiki_tanka, 0) sashihiki_kingaku "
			+   "  ,COALESCE(k.sashihiki_num_kaigai, 0) * COALESCE(k.sashihiki_tanka_kaigai, 0) sashihiki_kingaku_kaigai "
			+ "FROM denpyou d "
			+ "INNER JOIN kaigai_ryohiseisan k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? "
			+ "  AND d.denpyou_joutai = ? "
			+ "  AND d.chuushutsu_zumi_flg = '0' "
			+ "  AND NOT EXISTS (SELECT * FROM :SHIWAKE s WHERE d.denpyou_id = s.denpyou_id) "
// +	"  AND d.denpyou_id='170213-A004-0000003' " //UT
			+ "ORDER BY "
			+ "  d.denpyou_id ASC";
		
		if (Open21Env.getVersion() == Version.DE3 ) {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_de3");
		} else {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_sias");
		}
		
		return connection.load(sql, DENPYOU_KBN.KAIGAI_RYOHI_SEISAN, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}

	/**
	 * 海外出張旅費精算の旅費明細レコードをSELECTする
	 * @param denpyouId 伝票ID
	 * @return 承認済仮払伝票データ
	 */
	public List<GMap> loadRyohiMeisai(String denpyouId) {
		final String sql =
				"SELECT * "
			+ "  ,(CASE WHEN kaigai_flg = '1' THEN 'A011' ELSE 'A004' END) AS denpyou_kbn "
			+ "FROM kaigai_ryohiseisan_meisai m "
			+ "WHERE "
			+ "  m.denpyou_id = ? "
			+ "ORDER BY "
			+ "  m.kaigai_flg ASC, m.denpyou_edano ASC";
		
		return connection.load(sql, denpyouId);
	}

	
	/**
	 * 海外出張旅費精算の経費明細レコードをSELECTする
	 * @param denpyouId 伝票ID
	 * @return 承認済仮払伝票データ
	 */
	public List<GMap> loadKeihiMeisai(String denpyouId) {
		final String sql =
				"SELECT * "
			+ "  ,(CASE WHEN kaigai_flg = '1' THEN 'A901' ELSE 'A001' END) AS denpyou_kbn "
			+ "FROM kaigai_ryohiseisan_keihi_meisai m "
			+ "WHERE "
			+ "  m.denpyou_id = ? "
			+ "ORDER BY "
			+ "  m.denpyou_edano ASC";
		
		return connection.load(sql, denpyouId);
	}

	/**
	 * kaigai_hogeをhogeとしてセットする。例)kaigai_kari_kamoku_cd→kari_kamoku_cd
	 * @param meisai 明細
	 */
	public void trimKaigai(GMap meisai) {
		Iterator<String> ite = meisai.keySet().iterator();
		List<String> kaigaiKeyList = new ArrayList<>();
		while(ite.hasNext()){
			String key = ite.next();
			if(key.startsWith("kaigai_")){
				kaigaiKeyList.add(key);
			}
		}
		for(String kaigaiKey : kaigaiKeyList){
			meisai.put(kaigaiKey.substring("kaigai_".length()), meisai.get(kaigaiKey));
		}
	}
}