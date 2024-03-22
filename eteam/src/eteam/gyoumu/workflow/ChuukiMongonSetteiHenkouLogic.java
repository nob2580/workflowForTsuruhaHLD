package eteam.gyoumu.workflow;

import java.util.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import eteam.base.GMap;

/**
 * 最終承認ルート変更機能Logic
 */
public class ChuukiMongonSetteiHenkouLogic extends EteamAbstractLogic {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(ChuukiMongonSetteiHenkouLogic.class);
	
	
	/**
	 * 最終承認ルート情報を取得する
	 * @param denpyouKbn       伝票区分
	 * @param edaNo            枝番号
	 * @return 検索結果
	 */
	public List<GMap> saishuShouninRouteList(String denpyouKbn, int edaNo) {
		
		final String sql = "          SELECT dsi.denpyou_shubetsu, ssro.denpyou_kbn, ssro.edano, gr.gyoumu_role_name, gr.gyoumu_role_id,  " +
						   "                 ssrk.saishuu_shounin_shori_kengen_name, ssro.chuuki_mongon, ssro.yuukou_kigen_from, ssro.yuukou_kigen_to " +
						   "            FROM saishuu_syounin_route_oya ssro " +
						   " LEFT OUTER JOIN saishuu_syounin_route_ko ssrk " +
						   "              ON (ssro.denpyou_kbn = ssrk.denpyou_kbn AND ssro.edano = ssrk.edano) " +
						   "      INNER JOIN denpyou_shubetsu_ichiran AS dsi " +
						   "              ON (ssro.denpyou_kbn = dsi.denpyou_kbn) " +
						   " LEFT OUTER JOIN gyoumu_role gr " +
						   "              ON (ssrk.gyoumu_role_id = gr.gyoumu_role_id) " +
						   "           WHERE ssro.denpyou_kbn = ? " +
						   "             AND ssro.edano = ? " +
						   "        ORDER BY ssro.denpyou_kbn, ssro.edano, ssrk.edaedano ASC ";
		
		return connection.load(sql, denpyouKbn, edaNo);
	}

	/**
	 * 最終承認ルートに重複レコードがないかチェックする
	 * @param denpyouKbn        伝票区分
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @return 検索結果
	 */
	public List<GMap> checkDuplicate(String denpyouKbn, Date yuukouKigenFrom, Date yuukouKigenTo) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT denpyou_kbn, edano ");
		sql.append("  FROM saishuu_syounin_route_oya ");
		sql.append(" WHERE denpyou_kbn = ? ");
		sql.append("   AND (NOT(? < yuukou_kigen_from OR yuukou_kigen_to < ? )) ");

		return connection.load(sql.toString(), denpyouKbn, yuukouKigenTo, yuukouKigenFrom);
	}
	
	/**
	 * 最終承認ルート情報を登録する。
	 * @param denpyouKbn        伝票区分
	 * @param edaNo             枝番号
	 * @param gyoumuRoleId      承認者
	 * @param shorikengen       最終処理権限名
	 * @param chuukiMongon      注記文言
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @param userId            登録ユーザーID
	 * @return 登録件数
	 */
	public int updateSaishuShouninRoute(String denpyouKbn, int edaNo, String[] gyoumuRoleId, String[] shorikengen, String chuukiMongon,
			Date yuukouKigenFrom, Date yuukouKigenTo, String userId) {

		
		final String insOyaSql = "UPDATE saishuu_syounin_route_oya " + 
								 "   SET chuuki_mongon = ?, " +
								 "       yuukou_kigen_from = ?, " +
								 "       yuukou_kigen_to = ?, " +
								 "       koushin_user_id = ?, " +
								 "       koushin_time = current_timestamp " +
								 " WHERE denpyou_kbn = ? " +
								 "   AND edano = ? ";
		
		int retOya = connection.update(insOyaSql, chuukiMongon, yuukouKigenFrom, yuukouKigenTo, userId, denpyouKbn, edaNo);
		
		log.info("登録件数(最終承認ルート親:" + retOya + "件");
		
		final String delKoSql = "DELETE " +
								"  FROM saishuu_syounin_route_ko " +
								" WHERE denpyou_kbn = ? " + 
								"   AND edano = ? ";
		
		int retDelKo = connection.update(delKoSql, denpyouKbn, edaNo);
		log.info("削除件数(最終承認ルート子:" + retDelKo + "件");
		
		
		final String insKoSql = "INSERT INTO saishuu_syounin_route_ko " +
				"VALUES (?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";

		// 枝番号
		int edaEdaNo = 0;

		int retKo = 0;
		for(int i = 0; i < gyoumuRoleId.length ; i++) {
			String id = gyoumuRoleId[i];

			if (!id.isEmpty()) {

				edaEdaNo++;
				retKo = retKo
						+ connection.update(insKoSql, denpyouKbn, edaNo, edaEdaNo, id, shorikengen[i],
								userId, userId);
			}
		}
		log.info("登録件数(最終承認ルート子:" + retKo + "件");

		return retOya + retKo;
	}
	
	/**
	 * 最終承認ルート情報を削除する
	 * @param denpyouKbn        伝票区分
	 * @param edaNo             枝番号
	 * @return 削除件数
	 */
	public int deleteSaishuShouninRoute(String denpyouKbn, int edaNo) {
		
		final String delOyasql = "DELETE " +
				"  FROM saishuu_syounin_route_oya " +
				" WHERE denpyou_kbn = ? " +
				"   AND edano = ? ";
		
		int retDelOya = connection.update(delOyasql, denpyouKbn, edaNo);
		log.info("削除件数(最終承認ルート親:" + retDelOya + "件");
		
		final String delKoSql = "DELETE " +
				"  FROM saishuu_syounin_route_ko " +
				" WHERE denpyou_kbn = ? " +
				"   AND edano = ? ";

		int retDelKo = connection.update(delKoSql, denpyouKbn, edaNo);
		log.info("削除件数(最終承認ルート子:" + retDelKo + "件");
		
		return retDelOya + retDelKo;
	}
}
