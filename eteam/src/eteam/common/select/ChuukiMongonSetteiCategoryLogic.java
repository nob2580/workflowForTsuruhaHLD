package eteam.common.select;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 最終承認者・注記文言設定内のSelect文を集約したLogic
 */
public class ChuukiMongonSetteiCategoryLogic extends EteamAbstractLogic {

	/**
	 * 有効期限内の最終承認ルート一覧を取得する
	 * @return 検索結果
	 */
	public List<GMap> load() {
		
		final String sql = "          SELECT dsi.denpyou_shubetsu, ssro.denpyou_kbn, ssro.edano, " +
						   "                 CASE WHEN ssrk.gyoumu_role_id IS NULL THEN '' " +
						   "                      WHEN gr.gyoumu_role_name IS NULL THEN '(削除)' " +
						   "                      ELSE gr.gyoumu_role_name END gyoumu_role_name, " + 
						   "                 ssro.chuuki_mongon, ssro.yuukou_kigen_from, ssro.yuukou_kigen_to " +
						   "            FROM saishuu_syounin_route_oya ssro " +
						   " LEFT OUTER JOIN saishuu_syounin_route_ko ssrk " +
						   "              ON (ssro.denpyou_kbn = ssrk.denpyou_kbn AND ssro.edano = ssrk.edano) " +
						   "      INNER JOIN denpyou_shubetsu_ichiran AS dsi " +
						   "              ON (ssro.denpyou_kbn = dsi.denpyou_kbn) " +
						   " LEFT OUTER JOIN gyoumu_role gr " +
						   "              ON (ssrk.gyoumu_role_id = gr.gyoumu_role_id) " +
						   "        ORDER BY ssro.denpyou_kbn, ssro.edano, ssrk.edaedano ASC ";

		return connection.load(sql);
	}
}
