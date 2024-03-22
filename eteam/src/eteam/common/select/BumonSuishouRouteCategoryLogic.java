package eteam.common.select;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 部門推奨ルート一覧機能Logic
 */
public class BumonSuishouRouteCategoryLogic extends EteamAbstractLogic {

	/**
	 * 有効期限内の部門推奨ルート一覧を取得する
	 * @return 検索結果
	 */
	public List<GMap> load() {
		
		final String sql = "SELECT dsi.denpyou_shubetsu, " +
								  "bsro.denpyou_kbn, " +
								  "CASE WHEN sb.bumon_name IS NULL THEN '(削除)' ELSE sb.bumon_name END, " +
								  "bsro.bumon_cd, " +
								  "bsro.edano, " +
								  "bsro.kingaku_from, " + 
								  "bsro.kingaku_to, " +
								  "CASE WHEN br.bumon_role_name IS NULL THEN '(削除)' ELSE br.bumon_role_name END, " +
								  "bsrk.bumon_role_id,  " +
								  "CASE WHEN ssk.shounin_shori_kengen_name IS NULL THEN '(削除)' ELSE ssk.shounin_shori_kengen_name END, " +
								  "bsrk.shounin_shori_kengen_no, " +
								  "CASE WHEN gpo.gougi_name IS NULL THEN '(削除)' ELSE gpo.gougi_name END, " +
								  "bsrk.gougi_pattern_no, " +
								  "bsrk.gougi_edano, " +
								  "bsro.yuukou_kigen_from, " +
								  "bsro.yuukou_kigen_to " +
						    " FROM bumon_suishou_route_oya AS bsro " +
					   "INNER JOIN denpyou_shubetsu_ichiran AS dsi " +
					           "ON (bsro.denpyou_kbn = dsi.denpyou_kbn)  " +
				  "LEFT OUTER JOIN shozoku_bumon AS sb " +
				               "ON (bsro.bumon_cd = sb.bumon_cd) " +
				               "AND ( bsro.yuukou_kigen_to BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to) " +
				       "INNER JOIN bumon_suishou_route_ko AS bsrk " +
				               "ON (bsro.denpyou_kbn = bsrk.denpyou_kbn AND bsro.bumon_cd = bsrk.bumon_cd AND bsro.edano = bsrk.edano)  " +
				  "LEFT OUTER JOIN bumon_role AS br " +
				               "ON (bsrk.bumon_role_id = br.bumon_role_id) " +
				  "LEFT OUTER JOIN shounin_shori_kengen AS ssk " +
				               "ON (bsrk.shounin_shori_kengen_no = ssk.shounin_shori_kengen_no) " +
				  "LEFT OUTER JOIN gougi_pattern_oya AS gpo " +
				 			  " ON (bsrk.gougi_pattern_no = gpo.gougi_pattern_no) " +
				         "ORDER BY bsro.denpyou_kbn, bsro.bumon_cd, bsro.edano, bsrk.edaedano, bsro.kingaku_from, bsro.kingaku_to, bsro.yuukou_kigen_from, bsro.yuukou_kigen_to ASC ";
		return connection.load(sql);
	}
	
	/**
	 * 部門推奨ルートに重複レコードがないかチェックする
	 * @param denpyouKbn        伝票区分
	 * @param bumonCd           部門コード
	 * @param defaultFlg        デフォルト設定フラグ
	 * @param shiwakeEdaNo      仕訳枝番号（配列）
	 * @param kingakuFrom       金額FROM
	 * @param kingakuTo         金額TO
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @return 検索結果
	 */
	public List<GMap> checkDuplicate(String denpyouKbn,
			String bumonCd, String defaultFlg, String[] shiwakeEdaNo, BigDecimal kingakuFrom, BigDecimal kingakuTo,
			Date yuukouKigenFrom, Date yuukouKigenTo) {
		
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT denpyou_kbn, bumon_cd, edano");
		sql.append("  FROM bumon_suishou_route_oya ");
		sql.append(" WHERE denpyou_kbn = ? ");
		sql.append("   AND bumon_cd = ? ");
		sql.append("   AND default_flg = ? ");
		params.add(denpyouKbn);
		params.add(bumonCd);
		params.add(defaultFlg);
		
		if(null != shiwakeEdaNo){
			boolean isAnd = true;
			for(int i=0 ; i < shiwakeEdaNo.length ; i++){
				if(!"".equals(shiwakeEdaNo[i])){
					if(isAnd){
						sql.append("   AND (? = ANY(shiwake_edano)");
						isAnd = false;
					}else{
						sql.append("        OR ? = ANY(shiwake_edano)");
					}
					params.add(Integer.parseInt(shiwakeEdaNo[i]));
				}
				if(i==(shiwakeEdaNo.length - 1)){
					sql.append("       )");
				}
			}
		}
		
		sql.append("   AND (NOT(? < kingaku_from OR kingaku_to < ? ) ");
		if (kingakuFrom == null && kingakuTo != null) {
			sql.append("OR (kingaku_from IS NULL AND kingaku_to IS NOT NULL)");
		} else if (kingakuFrom != null && kingakuTo == null) {
			sql.append("OR (kingaku_from IS NOT NULL AND kingaku_to IS NULL)");
			
		} else if (kingakuFrom == null && kingakuTo == null) {
			sql.append("OR (kingaku_from IS NULL AND kingaku_to IS NULL)");
			
		}
		sql.append(")");
		params.add(kingakuTo);
		params.add(kingakuFrom);
		
		sql.append("   AND (NOT(? < yuukou_kigen_from OR yuukou_kigen_to < ? )) ");
		params.add(yuukouKigenTo);
		params.add(yuukouKigenFrom);

		return connection.load(sql.toString(), params.toArray());
	}
}
