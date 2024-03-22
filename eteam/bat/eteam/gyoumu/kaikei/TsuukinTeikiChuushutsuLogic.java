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
 * 通勤定期抽出ロジック
 */
public class TsuukinTeikiChuushutsuLogic extends EteamAbstractLogic  {

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
			+ "FROM denpyou d "
			+ "INNER JOIN tsuukinteiki k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_id = ? ";
		GMap map = connection.find(sql, denpyouId);
		if(map.get("shiharaibi") == null) map.put("shiharaibi", EteamCommon.tomorrow());
		return map;
	}
	
	/**
	 * 承認済通勤定期伝票をSELECTする
	 * @return 承認済通勤定期伝票データ
	 */
	public List<GMap> loadDenpyou() {
		String sql =
				"SELECT * "
			+ "FROM denpyou d "
			+ "INNER JOIN tsuukinteiki k ON "
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
		
		return connection.load(sql, DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}
}