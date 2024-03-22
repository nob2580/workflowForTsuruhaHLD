package eteam.common.select;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 代行者カテゴリー内のSelect文を集約したLogic
 */
public class DaikoushaCategoryLogic  extends EteamAbstractLogic {

	/**
	 * ログインユーザの代行者情報を検索する
	 * @param userId ログインユーザーID
	 * @return 検索結果リスト
	 */
	public List<GMap> daikoushaSerach(String userId) {
		final String sql = "          SELECT ui.user_id daikou_user_id, "
						 + "                 ui.user_id, "
						 + "                 ui.shain_no, "
						 + "                 ui.user_sei, "
						 + "                 ui.user_mei, "
						 + "                 sbw.bumon_cd, "
						 + "                 sbw.yuukou_kigen_from, "
						 + "                 sb.bumon_name, "
						 + "                 sbw.bumon_role_id, "
						 + "                 br.bumon_role_name "
						 + "            FROM daikou_shitei ds "
						 + "      INNER JOIN user_info ui "
						 + "              ON (ds.daikou_user_id = ui.user_id) "
						 + " LEFT OUTER JOIN shozoku_bumon_wariate sbw "
						 + "              ON (ds.daikou_user_id = sbw.user_id) "
						 + "             AND current_date BETWEEN sbw.yuukou_kigen_from AND sbw.yuukou_kigen_to "
						 + " LEFT OUTER JOIN shozoku_bumon sb "
						 + "              ON sbw.bumon_cd = sb.bumon_cd "
						 + "             AND current_date BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to "
						 + "             AND sbw.yuukou_kigen_to BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to "
						 + " LEFT OUTER JOIN bumon_role br "
						 + "              ON sbw.bumon_role_id = br.bumon_role_id "
						 + "           WHERE ds.hi_daikou_user_id = ? "
						 + "        ORDER BY daikou_user_id, ui.user_sei, ui.user_mei, sbw.bumon_cd DESC, sbw.yuukou_kigen_from, sbw.bumon_role_id DESC ";

		return connection.load(sql, userId);
	}
}
