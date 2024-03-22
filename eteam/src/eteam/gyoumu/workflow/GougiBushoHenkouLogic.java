package eteam.gyoumu.workflow;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;

/**
 * 合議部署追加機能Logic
 */
public class GougiBushoHenkouLogic extends EteamAbstractLogic {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(GougiBushoHenkouLogic.class);
	
	
	/**
	 * 合議部署情報を取得する
	 * @param gougiNo        合議パターン番号
	 * @return 検索結果
	 */
	public List<GMap> getGougiBushoList(int gougiNo) {
		
		final String sql = "SELECT gpo.gougi_pattern_no, " +
								  "gpo.gougi_name, " +
								  "gpk.shounin_ninzuu_cd," +
								  "gpk.shounin_ninzuu_hiritsu," +
								  "gpk.bumon_cd, " +
								  "CASE WHEN sb.bumon_name IS NULL THEN '(削除)' ELSE sb.bumon_name END, " +
								  "gpk.bumon_role_id, " +
								  "CASE WHEN br.bumon_role_name IS NULL THEN '(削除)' ELSE br.bumon_role_name END, " +
								  "gpk.shounin_shori_kengen_no, " +
								  "CASE WHEN ssk.shounin_shori_kengen_name IS NULL THEN '(削除)' ELSE ssk.shounin_shori_kengen_name END " +
						    " FROM gougi_pattern_oya AS gpo " +
						       "INNER JOIN gougi_pattern_ko AS gpk " +
						               "ON (gpo.gougi_pattern_no = gpk.gougi_pattern_no)  " +
						  "LEFT OUTER JOIN bumon_role AS br " +
						               "ON (gpk.bumon_role_id = br.bumon_role_id) " +
						  "LEFT OUTER JOIN shozoku_bumon AS sb " +
						               "ON (gpk.bumon_cd = sb.bumon_cd) " +
						               "AND ( current_date BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to) " +
						  "LEFT OUTER JOIN shounin_shori_kengen AS ssk " +
						               "ON (ssk.shounin_shori_kengen_no = gpk.shounin_shori_kengen_no) " +
						            "WHERE gpo.gougi_pattern_no = ? " +
						         "ORDER BY gpk.edano";
		return connection.load(sql, gougiNo);
	}

	/**
	 * 
	 * @param gougiNo 合議パターン番号
	 * @param gougiName 合議名
	 * @param bumonCd 部門コード
	 * @param bumonRole 部門ロールコード
	 * @param shoriKengenNo 処理権限番号
	 * @param hitsuyouCd 承認比率コード
	 * @param hitsuyouHiritsu 承認比率
	 * @param userId ユーザーID
	 * @return 処理件数
	 */
	public int updateGougiBusho(int gougiNo, String gougiName, String[] bumonCd, String[] bumonRole, String[] shoriKengenNo, String[] hitsuyouCd, String[] hitsuyouHiritsu, String userId) {
		
		final String insOyaSql = "UPDATE gougi_pattern_oya " + 
								 "   SET gougi_name = ?, " +
								 "       koushin_user_id = ?, " +
								 "       koushin_time = current_timestamp " +
								 " WHERE gougi_pattern_no = ? ";
		
		int retOya = connection.update(insOyaSql, gougiName, userId, gougiNo);
		
		log.info("登録件数(合議部署親:" + retOya + "件");
		
		final String delKoSql = "DELETE " +
								"  FROM gougi_pattern_ko " +
								" WHERE gougi_pattern_no = ? ";
		
		int retDelKo = connection.update(delKoSql, gougiNo);
		log.info("削除件数(合議部署子:" + retDelKo + "件");
		
		final String insKoSql = "INSERT INTO gougi_pattern_ko " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";

		// 枝枝番号
		int edaNo = 0;
		int retKo = 0;
		for (int i = 0; i < bumonCd.length; i++) {
		edaNo++;
		retKo = retKo + connection.update(insKoSql, gougiNo, edaNo, bumonCd[i], bumonRole[i], Integer.parseInt(shoriKengenNo[i]), hitsuyouCd[i], hitsuyouHiritsu[i].equals("") ? null : Integer.parseInt(hitsuyouHiritsu[i]), userId, userId);
		}
		
		log.info("登録件数(合議部署子:" + retKo + "件");
		
		return retKo++;
	}
	
	/**
	 * 合議部署情報を削除する
	 * @param gougiNo 合議パターン番号
	 * @return 表示順
	 */
	public int deleteGougiBusho(int gougiNo) {
		
		final String delOyasql = "DELETE " +
				"  FROM gougi_pattern_oya " +
				 " WHERE gougi_pattern_no = ? RETURNING hyouji_jun";
		
		GMap ret = connection.find(delOyasql, gougiNo);
		log.info("削除件数(合議部署親:1件");
		
		final String delKoSql = "DELETE " +
				"  FROM gougi_pattern_ko " +
				 " WHERE gougi_pattern_no = ? ";
		
		int retDelKo = connection.update(delKoSql, gougiNo);
		log.info("削除件数(合議部署子:" + retDelKo + "件");
		
		return Integer.parseInt(ret.getString("hyouji_jun"));
	}

	/**
	 * 表示順を修正する
	 * @param hyoujiJun 表示順
	 * @return 削除件数
	 */
	public int updateHyoujiJun(int hyoujiJun) {
		final String sql = "UPDATE gougi_pattern_oya SET hyouji_jun = hyouji_jun - 1 WHERE hyouji_jun > ? ";
		int retDelOya = connection.update(sql, hyoujiJun);
		return retDelOya;
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
	
	/**
	 * 部門推奨ルートに対象の合議Noがあるか調べる
	 * @param gougiNo 合議パターン番号
	 * @return 部門推奨ルート
	 */
	public List<GMap> loadBumonSuishouRoute(String gougiNo) {
		final String sql = "SELECT * FROM bumon_suishou_route_ko bsrk WHERE bsrk.gougi_pattern_no = ? ";
		return connection.load(sql, Integer.parseInt(gougiNo));
	}
	
	/**
	 * 伝票テーブルに対象の合議Noがあるか調べる
	 * @param gougiNo 合議パターン番号
	 * @return 伝票テーブル
	 */
	public List<GMap> loadGougiDenpyouJoutai(String gougiNo) {
		final String sql = "SELECT * FROM shounin_route_gougi_oya srgo "
						 + "INNER JOIN denpyou d ON srgo.denpyou_id = d.denpyou_id "
						 + "WHERE "
						 + "    srgo.gougi_pattern_no = ? "
						 + "AND d.denpyou_joutai NOT IN ('" + EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "','" 
						 									+ EteamNaibuCodeSetting.DENPYOU_JYOUTAI.HININ_ZUMI + "','" 
						 									+ EteamNaibuCodeSetting.DENPYOU_JYOUTAI.TORISAGE_ZUMI + "')";
		return connection.load(sql, Integer.parseInt(gougiNo));
	}
}
