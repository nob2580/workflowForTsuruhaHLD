package eteam.gyoumu.workflow;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;

/**
 * 合議部署追加機能Logic
 */
public class GougiBushoTsuikaLogic extends EteamAbstractLogic {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(GougiBushoTsuikaLogic.class);
	
	/**
	 * 合議部署を登録する。
	 * @param gougiName         合議部署名
	 * @param bumonCd           部門コード
	 * @param bumonRole         承認者
	 * @param shoriKengenNo     承認処理権限番号
	 * @param hitsuyouCd        承認必要人数コード
	 * @param hitsuyouHiritsu   承認必要人数比率
	 * @param userId            登録ユーザーID
	 * @return 登録件数
	 */
	public int insertGougiBusho(String gougiName, String[] bumonCd, String[] bumonRole, String[] shoriKengenNo, String[] hitsuyouCd, String[] hitsuyouHiritsu, String userId) {
		final String insOyaSql = "INSERT INTO gougi_pattern_oya " +
								 " (gougi_name, hyouji_jun, touroku_user_id, touroku_time, koushin_user_id, koushin_time) " + 
								 "VALUES (?, (SELECT COALESCE(MAX(hyouji_jun)+1, 1) FROM gougi_pattern_oya), ?, current_timestamp, ?, current_timestamp) RETURNING gougi_pattern_no";
		GMap ret = connection.find(insOyaSql, gougiName, userId, userId);
		
		log.info("登録件数(合議部署親:1件");
		
		final String insKoSql = "INSERT INTO gougi_pattern_ko " +
								"VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		// 枝枝番号
		int edaNo = 0;
		int retKo = 0;
		for (int i = 0; i < bumonCd.length; i++) {
			edaNo++;
			retKo = retKo + connection.update(insKoSql, ret.get("gougi_pattern_no"), edaNo, bumonCd[i], bumonRole[i], Integer.parseInt(shoriKengenNo[i]), hitsuyouCd[i], hitsuyouHiritsu[i].equals("") ? null : Integer.parseInt(hitsuyouHiritsu[i]), userId, userId);
		}
		
		log.info("登録件数(合議部署子:" + retKo + "件");

		return retKo++;
	}
	
	/**
	 * TODO:承認処理権限取得（移動予定）
	 * @return 処理権限リスト
	 */
	public List<GMap> loadShouninShoriKengen() {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.GOUGI + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "' "
							+ "ORDER BY hyouji_jun";
		
		return connection.load(sql);
	}
	
	/**
	 * 処理権限Noから承認処理権限取得
	 * @param shorikengenNo 処理権限No
	 * @return 処理権限リスト
	 */
	public GMap loadShouninShoriKengen(int shorikengenNo) {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE (hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.GOUGI + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "' ) "
							+ "AND shounin_shori_kengen_no = ? "
							+ "ORDER BY hyouji_jun";
		
		return connection.find(sql, shorikengenNo);
	}
}
