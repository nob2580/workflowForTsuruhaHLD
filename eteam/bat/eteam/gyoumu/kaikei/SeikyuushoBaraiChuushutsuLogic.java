package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;

/**
 * 請求書払い抽出ロジック
 */
public class SeikyuushoBaraiChuushutsuLogic extends EteamAbstractLogic  {

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
			+ "FROM denpyou d "
			+ "INNER JOIN seikyuushobarai k ON "
			+ "  d.denpyou_id = k.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_id = ? ";
		GMap map = connection.find(sql, denpyouId);
		if(map.get("keijoubi") == null) map.put("keijoubi", EteamCommon.today());
		if(map.get("masref_flg").equals("0") && map.get("shiharaibi") == null) map.put("shiharaibi", EteamCommon.tomorrow());
		return map;
	}

	/**
	 * 抽出対象の伝票を取得
	 * @return 仕訳対象のデータ
	 */
	public List<GMap> loadDenpyou() {
		String sql =
				"SELECT * "
			+ "FROM denpyou d "
			+ "INNER JOIN seikyuushobarai k ON "
			+ "  d.denpyou_id = k.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? "
			+ "  AND d.denpyou_joutai = ? "
			+ "  AND d.chuushutsu_zumi_flg = '0' "
			+ "  AND NOT EXISTS (SELECT * FROM :SHIWAKE s WHERE d.denpyou_id = s.denpyou_id) "
// +	"  AND d.denpyou_id='180814-A003-0000001' "
			+ "ORDER BY "
			+ "  d.denpyou_id ASC";
		
		if (Open21Env.getVersion() == Version.DE3 ) {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_de3");
		} else {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_sias");
		}
		
		return connection.load(sql, DENPYOU_KBN.SEIKYUUSHO_BARAI, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}

}
