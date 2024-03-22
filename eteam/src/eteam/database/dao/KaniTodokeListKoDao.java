package eteam.database.dao;

import java.util.List;

import eteam.base.GMap;
import eteam.database.abstractdao.KaniTodokeListKoAbstractDao;

/**
 * 届出ジェネレータ項目リスト子に対するデータ操作クラス
 */
public class KaniTodokeListKoDao extends KaniTodokeListKoAbstractDao {

	/**
	 * select_itemが簡易届の選択部品内で使用されているか ※VERSIONが最新のもの以外は無視
	 * @param selectItem 選択項目
	 * @return 選択項目を使用している伝票区分とそのMAX(VERSION)
	 */
	public List<GMap> loadDenpyouKbnUsingSelectItem(String selectItem) {
		final String sql =
				"WITH "
			+ "  kb AS( "
			+ "    SELECT "
			+ "      denpyou_kbn, "
			+ "      max(version) AS version "
			+ "    FROM kani_todoke_version "
			+ "    GROUP BY denpyou_kbn) "
			+ "SELECT"
			+ "  kb.denpyou_kbn, "
			+ "  kb.version, "
			+ "  ds.denpyou_shubetsu "
			+ "FROM kb "
			+ "JOIN denpyou_shubetsu_ichiran ds USING(denpyou_kbn)"
			+ "WHERE "
			+ "  EXISTS(SELECT * FROM kani_todoke_list_ko WHERE (denpyou_kbn, version) = (kb.denpyou_kbn, kb.version) AND select_item = ?) "
			+ "ORDER BY denpyou_kbn ";
		return connection.load(sql, selectItem);
	}
	
	/**
	 * 届出ジェネレータ項目リスト子から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param selectItem 選択項目
	 * @return List<届出ジェネレータ項目リスト子>DTO
	 */
	public List<GMap> loadBySelectItem(String denpyouKbn, int version, String selectItem) {
		final String sql = "SELECT * FROM (SELECT DISTINCT denpyou_kbn, version, area_kbn, item_name FROM kani_todoke_list_ko WHERE denpyou_kbn = ?  AND version = ? AND select_item = ?) tmp "
							+ "ORDER BY denpyou_kbn, version, area_kbn, item_name ";
		return connection.load(sql, denpyouKbn, version, selectItem);
	}

	/**
	 * 指定項目の値ブランクを除いて削除
	 * @param denpyouKbn 伝票区分
	 * @param version ヴァージョン
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 */
	public void deleteWithoutBlank(String denpyouKbn, int version, String areaKbn, String itemName) {
		final String sql = "DELETE FROM kani_todoke_list_ko WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ? AND value <> ''";
		connection.update(sql, denpyouKbn, version, areaKbn, itemName);
	}
}
