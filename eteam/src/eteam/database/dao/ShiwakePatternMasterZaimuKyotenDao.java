package eteam.database.dao;

import java.util.List;

import eteam.base.GMap;
import eteam.database.abstractdao.ShiwakePatternMasterZaimuKyotenAbstractDao;

/**
 * 仕訳パターンマスター（財務拠点入力）に対するデータ操作クラス
 */
public class ShiwakePatternMasterZaimuKyotenDao extends ShiwakePatternMasterZaimuKyotenAbstractDao {
	
	/**
	 * 仕訳パターンマスターで最新の摘要フラグを取得する
	 * @return 摘要フラグ
	 */
	public String selLatestShiwakePatternMaster() {
		final String sql ="SELECT "
						+ "  tekiyou_flg "
						+ "FROM "
						+ "  shiwake_pattern_master_zaimu_kyoten "
						+ "ORDER BY "
						+ "  koushin_time DESC "
						+ "LIMIT "
						+ "  1 ";
		GMap ret = connection.find(sql);
		if (ret == null || ret.isEmpty()) {
			return "0";
		} else {
			return (String)ret.get("tekiyou_flg");
		}
	}
	
	/**
	 * UF欄をブランクにする
	 * @param no UFの番号(1～10)
	 * @param denpyouKbn 伝票区分
	 * @param userId ログインユーザーID
	 */
	public void clearUf(Integer no, String denpyouKbn, String userId){
		String sql =	"UPDATE shiwake_pattern_master_zaimu_kyoten SET "
					+ "  header_uf:NO_cd='', kari_uf:NO_cd='', kashi_uf:NO_cd='', "
					+ "  koushin_user_id=?, koushin_time = current_timestamp ";
		sql = sql.replaceAll(":NO", Integer.toString(no));
		if(!isEmpty(denpyouKbn)){
			sql = sql + "WHERE denpyou_kbn='" + denpyouKbn + "' ";
		}
		connection.update(sql, userId);
	}
	
	/**
	 * UF欄(固定値)をブランクにする
	 * @param no UFの番号(1～10)
	 * @param denpyouKbn 伝票区分
	 * @param userId ログインユーザーID
	 */
	public void clearUfKotei(Integer no, String denpyouKbn, String userId){
		String sql =	"UPDATE shiwake_pattern_master_zaimu_kyoten SET "
					+ "  header_uf_kotei:NO_cd='', kari_uf_kotei:NO_cd='', kashi_uf_kotei:NO_cd='', "
					+ "  koushin_user_id=?, koushin_time = current_timestamp ";
		sql = sql.replaceAll(":NO", Integer.toString(no));
		if(!isEmpty(denpyouKbn)){
			sql = sql + "WHERE denpyou_kbn='" + denpyouKbn + "' ";
		}
		connection.update(sql, userId);
	}
	
	/**
	 * 仕訳パターンを取得する。
	 * @param denpyouKbn 伝票区分
	 * @param zaimuKyotenNyuryokuPatternNo 財務拠点入力パターンNo
	 * @param shiwakeEdano 仕訳枝番号
	 * @return 検索結果
	 */
	public GMap findShiwakePattern(String denpyouKbn, String zaimuKyotenNyuryokuPatternNo, int shiwakeEdano) {
		final String sql = "SELECT * FROM shiwake_pattern_master_zaimu_kyoten where denpyou_kbn = ? and zaimu_kyoten_nyuryoku_pattern_no = ? and shiwake_edano = ?";
		return connection.find(sql, denpyouKbn, zaimuKyotenNyuryokuPatternNo, shiwakeEdano);
	}
	
	/**
	 * 伝票種別をキーに財務拠点入力の取引(仕訳)一覧を取得する。
	 * @param denpyouKbn String 伝票種別
	 * @param sortItem String ソートする項目
	 * @param sortOrder String ソート順
	 * @return 検索結果
	 */
	public List<GMap> selectZaimuKyotenTorihikiIchiran(String denpyouKbn, String sortItem, String sortOrder){

		String sql = "SELECT a.denpyou_kbn, a.zaimu_kyoten_nyuryoku_pattern_no, a.shiwake_edano, a.yuukou_kigen_from, a.yuukou_kigen_to, a.bunrui1, a.bunrui2, a.bunrui3, a.torihiki_name, "
						+  "a.header_kamoku_cd, b.kamoku_name_ryakushiki as header_kamoku_name, "
						+  "a.aite_kamoku_cd, c.kamoku_name_ryakushiki as aite_kamoku_name, "
						+  "f.zaimu_kyoten_nyuryoku_pattern_name "
						+  "FROM (SELECT *, CASE nyushukkin_flg WHEN '0' THEN kari_kamoku_cd ELSE kashi_kamoku_cd END as aite_kamoku_cd FROM shiwake_pattern_master_zaimu_kyoten) a "
						+  "LEFT OUTER JOIN kamoku_master b "
						+  "ON b.kamoku_gaibu_cd = a.header_kamoku_cd "
						+  "LEFT OUTER JOIN kamoku_master c "
						+  "ON c.kamoku_gaibu_cd = a.aite_kamoku_cd "
						+  "LEFT OUTER JOIN (SELECT denpyou_kbn, zaimu_kyoten_nyuryoku_pattern_no, shiwake_edano, CASE kari_kamoku_cd WHEN ''THEN kashi_kamoku_cd ELSE kari_kamoku_cd END as aite_kamoku_cd FROM shiwake_pattern_master_zaimu_kyoten) e "
						+  "ON (a.denpyou_kbn, a.zaimu_kyoten_nyuryoku_pattern_no, a.shiwake_edano) = (e.denpyou_kbn, e.zaimu_kyoten_nyuryoku_pattern_no, e.shiwake_edano) "
						+  "INNER JOIN zaimu_kyoten_nyuryoku_ichiran f "
						+  "ON f.denpyou_kbn = a.denpyou_kbn AND f.zaimu_kyoten_nyuryoku_pattern_no = a.zaimu_kyoten_nyuryoku_pattern_no "
						+  "WHERE  a.denpyou_kbn = ? AND a.delete_flg = '0' ";
		
		StringBuffer sb = new StringBuffer();
		sb.append("ORDER BY (CASE WHEN current_date > a.yuukou_kigen_to THEN 1 ELSE 0 END), ");
		sb.append((sortItem.equals("zaimu_kyoten_nyuryoku_pattern_name")? "f." : "a.")).append(sortItem).append(" " + sortOrder);
		sb.append(", a.shiwake_edano");
		sql = sql + sb.toString();
		
		return connection.load(sql, denpyouKbn);
	}
	
	/**
	 * 伝票種別と財務拠点入力パターンNoをキーに財務拠点入力の取引(仕訳)一覧を取得する。(現預金出納帳仕訳パターン用)
	 * @param denpyouKbn String 伝票種別
	 * @param zaimuKyotenNyuryokuPatternNo コピー元財務拠点入力パターンNo
	 * @return 検索結果
	 */
	public List<GMap> selectCopyMotoZaimuKyotenTorihikiIchiran(String denpyouKbn, String zaimuKyotenNyuryokuPatternNo){

		String sql = "SELECT denpyou_kbn, zaimu_kyoten_nyuryoku_pattern_no, shiwake_edano, torihiki_name, "
						+  "CASE tekiyou_flg WHEN '0' THEN torihiki_name ELSE tekiyou END AS tekiyou, "
						+  "CASE nyushukkin_flg WHEN '0' THEN kari_futan_bumon_cd ELSE kashi_futan_bumon_cd END AS futan_bumon_cd "
						+  "FROM shiwake_pattern_master_zaimu_kyoten "
						+  "WHERE denpyou_kbn = ? "
						+  "  AND zaimu_kyoten_nyuryoku_pattern_no = ? "
						+  "  AND delete_flg = '0' "
						+  "  AND yuukou_kigen_to >= current_date "
						+  "ORDER BY shiwake_edano";
		return connection.load(sql, denpyouKbn, zaimuKyotenNyuryokuPatternNo);
	}
	
	/**
	 * 財務拠点入力の仕訳パターンの削除フラグを1にして、削除済みパターン扱いにする（実際にDBから削除するわけではない）。
	 * @param userId ユーザーID
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdano 仕訳枝番号
	 * @return 処理件数
	 */
	public int updateDeleteFlgAsTrue(String userId, String denpyouKbn, int shiwakeEdano) {

		final String sql1 = "UPDATE shiwake_pattern_master_zaimu_kyoten SET delete_flg = '1', koushin_user_id = ?, koushin_time = current_timestamp WHERE denpyou_kbn = ? and shiwake_edano = ?";
		return connection.update(sql1, userId, denpyouKbn, shiwakeEdano);
	}
	
	/**
	 * 伝票種別・財務拠点入力パターンNoをキーに財務拠点入力の取引(仕訳)一覧を取得する。(仕訳選択ダイアログ用)
	 * @param denpyouKbn String 伝票種別
	 * @param zaimuKyotenNyuryokuPatternNo String 財務拠点入力パターンNo
	 * @param sortItem String ソートする項目
	 * @param sortOrder String ソート順
	 * @param isYuukouOnly 現時点で有効な取引のみ表示するか(有効な取引のみ選択する・・・"1" 全取引表示する・・・“0”)
	 * @return 検索結果
	 */
	public List<GMap> selectZaimuKyotenTorihikiIchiranPatternNo(String denpyouKbn, String zaimuKyotenNyuryokuPatternNo, String sortItem, String sortOrder, String isYuukouOnly){

		String sql = "SELECT a.denpyou_kbn, a.zaimu_kyoten_nyuryoku_pattern_no, a.shiwake_edano, a.yuukou_kigen_from, a.yuukou_kigen_to, a.bunrui1, a.bunrui2, a.bunrui3, a.torihiki_name, "
						+  "a.header_kamoku_cd, b.kamoku_name_ryakushiki as header_kamoku_name, "
						+  "a.kari_kamoku_cd, c.kamoku_name_ryakushiki as kari_kamoku_name, "
						+  "a.kashi_kamoku_cd, d.kamoku_name_ryakushiki as kashi_kamoku_name, "
						+  "a.tekiyou_flg, "
						+  "a.tekiyou, "
						+  "a.nyushukkin_flg, "
						+  "a.fusen_flg, "
						+  "a.fusen_color, "
						+  "a.kari_kazei_kbn, "
						+  "a.kari_zeiritsu, "
						+  "a.kari_keigen_zeiritsu_kbn, "
						+  "a.kashi_kazei_kbn, "
						+  "a.kashi_zeiritsu, "
						+  "a.kashi_keigen_zeiritsu_kbn, "
						+  "a.tenpu_file_hissu_flg, "
						+  "a.shiwake_taishougai_flg "
						+  "FROM shiwake_pattern_master_zaimu_kyoten a "
						+  "LEFT OUTER JOIN kamoku_master b "
						+  "ON b.kamoku_gaibu_cd = a.header_kamoku_cd "
						+  "LEFT OUTER JOIN kamoku_master c "
						+  "ON c.kamoku_gaibu_cd = a.kari_kamoku_cd "
						+  "LEFT OUTER JOIN kamoku_master d "
						+  "ON d.kamoku_gaibu_cd = a.kashi_kamoku_cd "
						+  "LEFT OUTER JOIN (SELECT denpyou_kbn, zaimu_kyoten_nyuryoku_pattern_no, shiwake_edano, CASE kari_kamoku_cd WHEN ''THEN kashi_kamoku_cd ELSE kari_kamoku_cd END as aite_kamoku_cd FROM shiwake_pattern_master_zaimu_kyoten) e "
						+  "ON (a.denpyou_kbn, a.zaimu_kyoten_nyuryoku_pattern_no, a.shiwake_edano) = (e.denpyou_kbn, e.zaimu_kyoten_nyuryoku_pattern_no, e.shiwake_edano) "
						+  "WHERE  a.denpyou_kbn = ? AND a.zaimu_kyoten_nyuryoku_pattern_no = ? AND a.delete_flg = '0' ";

		if (isYuukouOnly.equals("1"))
		{
			sql = sql + " AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to ";
		}
		sql = sql + "ORDER BY (CASE WHEN current_date > a.yuukou_kigen_to THEN 1 ELSE 0 END), " + "a." + sortItem + " " + sortOrder + ", a.shiwake_edano";
		return connection.load(sql, denpyouKbn, zaimuKyotenNyuryokuPatternNo);
	}
}
