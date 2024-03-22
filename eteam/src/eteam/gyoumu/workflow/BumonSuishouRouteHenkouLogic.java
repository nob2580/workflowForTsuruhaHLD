package eteam.gyoumu.workflow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;

/**
 * 部門推奨ルート追加機能Logic
 */
public class BumonSuishouRouteHenkouLogic extends EteamAbstractLogic {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(BumonSuishouRouteHenkouLogic.class);
	
	
	/**
	 * 部門推奨ルート情報を取得する
	 * @param denpyouKbn       伝票区分
	 * @param bumonCd          部門コード
	 * @param edano            枝番号
	 * @return 検索結果
	 */
	public List<GMap> getBumonSuishouRouteList(String denpyouKbn, String bumonCd, int edano) {
		
		final String sql = "SELECT dsi.denpyou_shubetsu, " +
								 " bsro.denpyou_kbn, " +
								 " dsi.version, " +
								 " CASE WHEN sb.bumon_name IS NULL THEN '(削除)' ELSE sb.bumon_name END, " +
								 " bsro.bumon_cd, " +
								 " bsro.edano, " +
								 " bsro.default_flg, " +
								 " bsro.kingaku_from, " +
								 " bsro.kingaku_to, " +
								 " CASE WHEN br.bumon_role_name IS NULL THEN '(削除)' ELSE br.bumon_role_name END, " +
								 " bsrk.bumon_role_id, " +
								 " bsrk.shounin_shori_kengen_no, " +
								 " CASE WHEN gpo.gougi_name IS NULL THEN '(削除)' ELSE gpo.gougi_name END, " +
								 " bsrk.gougi_pattern_no, " +
								 " bsrk.gougi_edano, " +
								 " bsro.yuukou_kigen_from, " +
								 " bsro.yuukou_kigen_to " +
							" FROM bumon_suishou_route_oya AS bsro " +
					  " INNER JOIN bumon_suishou_route_ko AS bsrk " +
					  		 " ON (bsro.denpyou_kbn = bsrk.denpyou_kbn AND bsro.bumon_cd = bsrk.bumon_cd AND bsro.edano = bsrk.edano) " + 
					  " INNER JOIN denpyou_shubetsu_ichiran AS dsi " +
					  		 " ON (bsro.denpyou_kbn = dsi.denpyou_kbn) " + 
				 "LEFT OUTER JOIN shozoku_bumon AS sb " +
				              "ON (bsro.bumon_cd = sb.bumon_cd) " +
				              "AND ( bsro.yuukou_kigen_to BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to ) " +
				 " LEFT OUTER JOIN bumon_role AS br " +
				 			 " ON (bsrk.bumon_role_id = br.bumon_role_id) " +
				 " LEFT OUTER JOIN gougi_pattern_oya AS gpo " +
				 			 " ON (bsrk.gougi_pattern_no = gpo.gougi_pattern_no) " +
				 		   " WHERE bsro.denpyou_kbn = ? " +
				 		     " AND bsro.bumon_cd = ? " +
				 		     " AND bsro.edano = ? " +
				 	    " ORDER BY bsro.denpyou_kbn, bsro.bumon_cd, bsro.edano, bsrk.edaedano, bsro.kingaku_from, bsro.kingaku_to, bsro.yuukou_kigen_from, bsro.yuukou_kigen_to ASC ";
		return connection.load(sql, denpyouKbn, bumonCd, edano);
	}
	
	/**
	 * 部門推奨ルート情報を取得する
	 * @param denpyouKbn       伝票区分
	 * @param bumonCd          部門コード
	 * @param edano            枝番号
	 * @return 検索結果
	 */
	public List<GMap> getBumonSuishouRouteTorihikiList(String denpyouKbn, String bumonCd, int edano){
		final String sql = "SELECT unnest(shiwake_edano) AS shiwake_edano"
						 + " FROM bumon_suishou_route_oya"
						 + " WHERE denpyou_kbn = ?"
						 + "   AND bumon_cd = ?"
						 + "   AND edano = ?";
		return connection.load(sql, denpyouKbn, bumonCd, edano);
	}
	
	
	/**
	 * 部門推奨ルートを登録する。
	 * @param denpyouKbn        伝票区分
	 * @param bumonCd           部門コード
	 * @param edaNo             枝番号
	 * @param defaultFlg        デフォルト設定フラグ
	 * @param shiwakeEdaNo      仕訳枝番
	 * @param kingakuFrom       金額FROM
	 * @param kingakuTo         金額TO
	 * @param bumonRole         承認者
	 * @param shoriKengen       処理権限
	 * @param gougiCd           合議コード
	 * @param gougiEdano        合議枝番
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @param userId            登録ユーザーID
	 * @return 登録件数
	 */
	public int updateBumonSuishoRoute(String denpyouKbn, String bumonCd, int edaNo, String defaultFlg, Object[] shiwakeEdaNo,
			BigDecimal kingakuFrom, BigDecimal kingakuTo, String[] bumonRole, String[] shoriKengen, String[] gougiCd, String[] gougiEdano,
			Date yuukouKigenFrom, Date yuukouKigenTo, String userId) {

		
		final String insOyaSql = "UPDATE bumon_suishou_route_oya " + 
								 "   SET default_flg = ?, " +
								 "       shiwake_edano = ?, " +
								 "       kingaku_from = ?, " +
								 "       kingaku_to = ?, " +
								 "       yuukou_kigen_from = ?, " +
								 "       yuukou_kigen_to = ?, " +
								 "       koushin_user_id = ?, " +
								 "       koushin_time = current_timestamp " +
								 " WHERE denpyou_kbn = ? " +
								 "   AND bumon_cd = ? " +
								 "   AND edano = ? ";
		
		int retOya = connection.update(insOyaSql, defaultFlg, (null == shiwakeEdaNo)? null:connection.getIntegerArray(shiwakeEdaNo), kingakuFrom, kingakuTo, yuukouKigenFrom, yuukouKigenTo, userId, denpyouKbn, bumonCd, edaNo);
		
		log.info("登録件数(部門推奨ルート親:" + retOya + "件");
		
		final String delKoSql = "DELETE " +
								"  FROM bumon_suishou_route_ko " +
								 " WHERE denpyou_kbn = ? " +
								 "   AND bumon_cd = ? " +
								 "   AND edano = ? ";
		
		int retDelKo = connection.update(delKoSql, denpyouKbn, bumonCd, edaNo);
		log.info("削除件数(部門推奨ルート子:" + retDelKo + "件");
		
		
		final String insKoSql = "INSERT INTO bumon_suishou_route_ko " +
								"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		// 枝枝番号
		int edaedaNo = 0;
		
		int retKo = 0;
		for(int i = 0; i < bumonRole.length; i++) {
			edaedaNo++;
			retKo = retKo + connection.update(insKoSql, denpyouKbn, bumonCd, edaNo, edaedaNo, bumonRole[i], 
											  !shoriKengen[i].equals("") ? Integer.parseInt(shoriKengen[i]) : null, 
											  !gougiCd[i].equals("") ? Integer.parseInt(gougiCd[i]) : null, 
											  !gougiEdano[i].equals("") ? Integer.parseInt(gougiEdano[i]) : null, 
											  userId, userId);
		}

		log.info("登録件数(部門推奨ルート子:" + retKo + "件");

		return retOya + retKo;
	}
	
	/**
	 * 部門推奨ルート情報を削除する
	 * @param denpyouKbn        伝票区分
	 * @param bumonCd           部門コード
	 * @param edaNo             枝番号
	 * @return 削除件数
	 */
	public int deleteBumonSuishoRoute(String denpyouKbn, String bumonCd, int edaNo) {
		
		final String delOyasql = "DELETE " +
				"  FROM bumon_suishou_route_oya " +
				 " WHERE denpyou_kbn = ? " +
				 "   AND bumon_cd = ? " +
				 "   AND edano = ? ";
		
		int retDelOya = connection.update(delOyasql, denpyouKbn, bumonCd, edaNo);
		log.info("削除件数(部門推奨ルート親:" + retDelOya + "件");
		
		final String delKoSql = "DELETE " +
				"  FROM bumon_suishou_route_ko " +
				 " WHERE denpyou_kbn = ? " +
				 "   AND bumon_cd = ? " +
				 "   AND edano = ? ";

		int retDelKo = connection.update(delKoSql, denpyouKbn, bumonCd, edaNo);
		log.info("削除件数(部門推奨ルート子:" + retDelKo + "件");
		
		return retDelOya + retDelKo;
	}
	
	
	/**
	 * 部門推奨ルート情報を全削除する
	 * @return 削除件数
	 */
	public int deleteAllBumonSuishoRoute() {
		
		final String delOyasql = "DELETE FROM bumon_suishou_route_oya ";
		int retDelOya = connection.update(delOyasql);
		log.info("削除件数(部門推奨ルート親:" + retDelOya + "件");
		
		final String delKoSql = "DELETE FROM bumon_suishou_route_ko ";
		int retDelKo = connection.update(delKoSql);
		log.info("削除件数(部門推奨ルート子:" + retDelKo + "件");
		
		return retDelOya + retDelKo;
	}
	
	/**
	 * TODO:承認処理権限取得（移動予定）
	 * @return 処理権限リスト
	 */
	public List<GMap> loadShouninShoriKengen() {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.HYOUJUN + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "' "
							+ "ORDER BY hyouji_jun";
		
		return connection.load(sql);
	}
	
	/**
	 * TODO:承認処理権限Noによる、承認処理権限取得（移動予定）
	 * @param shoriNo 処理権限No
	 * @return 処理権限
	 */
	public GMap findShouninShoriKengen(String shoriNo) {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE (hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.HYOUJUN + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "') "
							+ "AND shounin_shori_kengen_no = ?";
		
		return connection.find(sql, Integer.parseInt(shoriNo));
	}

	/**
	 * @param routeTrihikiflg ルート取引毎設定フラグ
	 * @return true：取引入力可、false:取引入力不可
	 */
	public boolean judgeTorihikiInput(String routeTrihikiflg){
		
		if(EteamConst.routeTorihiki.OK.equals(routeTrihikiflg)){
			return true;
		}else{
			return false;
		}
	}
}
