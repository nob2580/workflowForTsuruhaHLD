package eteam.common.select;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 合議部署一覧機能Logic
 */
public class GougiBushoCategoryLogic extends EteamAbstractLogic {

	/**
	 * 有効期限内の合議部署一覧を取得する
	 * @return 検索結果
	 */
	public List<GMap> load() {
		
		final String sql = "SELECT gpo.gougi_pattern_no, " +
								  "gpo.gougi_name, " +
								  "CASE WHEN gpk.shounin_ninzuu_cd = '1' THEN '全員' " +
								  "     WHEN gpk.shounin_ninzuu_cd = '2' THEN 'いずれか１人' " +
								  "     WHEN gpk.shounin_ninzuu_cd = '3' THEN '比率' || gpk.shounin_ninzuu_hiritsu || '％' " +
								  "ELSE '　' END as hiritsu," +
								  "gpk.bumon_cd, " +
								  "CASE WHEN sb.bumon_name IS NULL THEN '(削除)' ELSE sb.bumon_name END, " +
								  "gpk.bumon_role_id, " +
								  "CASE WHEN br.bumon_role_name IS NULL THEN '(削除)' ELSE br.bumon_role_name END, " +
								  "gpk.shounin_shori_kengen_no, " +
								  "CASE WHEN ssk.shounin_shori_kengen_name IS NULL THEN '(削除)' ELSE ssk.shounin_shori_kengen_name END" +
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
				         "ORDER BY gpo.hyouji_jun, gpk.edano";
		return connection.load(sql);
	}
	
	/**
	 * 表示順を修正する
	 * @param crrentLocation 現在位置
	 * @param flg 上下に移動するフラグ
	 */
	public void updateJunjo (int crrentLocation, boolean flg) {
		int location = crrentLocation;
		if (flg) {location--;} else {location++;}
		GMap map = connection.find("SELECT * FROM gougi_pattern_oya WHERE hyouji_jun = ?", location);
		int gougiNo = Integer.parseInt(map.getString("gougi_pattern_no"));
		connection.update("UPDATE gougi_pattern_oya SET hyouji_jun = ? WHERE hyouji_jun = ?", location, crrentLocation);
		connection.update("UPDATE gougi_pattern_oya SET hyouji_jun = ? WHERE gougi_pattern_no = ?", crrentLocation, gougiNo);
	}
}
