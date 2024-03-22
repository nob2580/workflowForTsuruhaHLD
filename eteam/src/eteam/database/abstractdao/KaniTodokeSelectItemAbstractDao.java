package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KaniTodokeSelectItem;

/**
 * 届出ジェネレータ選択項目に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KaniTodokeSelectItemAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KaniTodokeSelectItem mapToDto(GMap map){
		return map == null ? null : new KaniTodokeSelectItem(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KaniTodokeSelectItem> mapToDto(List<GMap> mapList){
		List<KaniTodokeSelectItem> dtoList = new ArrayList<KaniTodokeSelectItem>();
		for (var map : mapList) {
			dtoList.add(new KaniTodokeSelectItem(map));
		}
		return dtoList;
	}
	
	/**
	 * 届出ジェネレータ選択項目のレコード有無を判定
	 * @param selectItem 選択項目
	 * @param cd コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String selectItem, String cd) {
		return this.find(selectItem, cd) == null ? false : true;
	}
	
	/**
	 * 届出ジェネレータ選択項目から主キー指定でレコードを取得
	 * @param selectItem 選択項目
	 * @param cd コード
	 * @return 届出ジェネレータ選択項目DTO
	 */
	public KaniTodokeSelectItem find(String selectItem, String cd) {
		final String sql = "SELECT * FROM kani_todoke_select_item WHERE select_item = ? AND cd = ?";
		return mapToDto(connection.find(sql, selectItem, cd));
	}
	
	/**
	 * 届出ジェネレータ選択項目からレコードを全件取得 ※大量データ取得に注意
	 * @return List<届出ジェネレータ選択項目DTO>
	 */
	public List<KaniTodokeSelectItem> load() {
		final String sql = "SELECT * FROM kani_todoke_select_item ORDER BY select_item, cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 届出ジェネレータ選択項目から一部キー指定でレコードを取得
	 * @param selectItem 選択項目
	 * @return List<届出ジェネレータ選択項目>DTO
	 */
	public List<KaniTodokeSelectItem> load(String selectItem) {
		final String sql = "SELECT * FROM kani_todoke_select_item WHERE select_item = ? "
							+ "ORDER BY select_item, cd";
		return mapToDto(connection.load(sql, selectItem));
	}

	/**
	* 届出ジェネレータ選択項目登録
	* @param dto 届出ジェネレータ選択項目
	* @return 件数
	*/
	public int insert(
		KaniTodokeSelectItem dto
	){
		final String sql =
				"INSERT INTO kani_todoke_select_item "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.selectItem, dto.cd, dto.name
					);
	}

	/**
	* 届出ジェネレータ選択項目登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 届出ジェネレータ選択項目
	* @return 件数
	*/
	public int upsert(
		KaniTodokeSelectItem dto
		 ){
		final String sql =
				"INSERT INTO kani_todoke_select_item "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT kani_todoke_select_item_pkey "
			+ "DO UPDATE SET name = ? "
			+ "";
			return connection.update(sql,
				dto.selectItem, dto.cd, dto.name
				, dto.name
				);
    }
	
	/**
	 * 届出ジェネレータ選択項目から主キー指定でレコードを削除
	 * @param selectItem 選択項目
	 * @param cd コード
	 * @return 削除件数
	 */
	public int delete(String selectItem, String cd){
		final String sql = "DELETE FROM kani_todoke_select_item WHERE select_item = ? AND cd = ?";
		return connection.update(sql, selectItem, cd);
	}
	
	/**
	 * 届出ジェネレータ選択項目から一部キー指定でレコードを削除
	 * @param selectItem 選択項目
	 * @return 削除件数
	 */
	public int delete(String selectItem) {
		final String sql = "DELETE FROM kani_todoke_select_item WHERE select_item = ? ";
		return connection.update(sql, selectItem);
	}
}
