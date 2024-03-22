package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KaniTodokeKoumoku;

/**
 * 届出ジェネレータ項目定義に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KaniTodokeKoumokuAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KaniTodokeKoumoku mapToDto(GMap map){
		return map == null ? null : new KaniTodokeKoumoku(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KaniTodokeKoumoku> mapToDto(List<GMap> mapList){
		List<KaniTodokeKoumoku> dtoList = new ArrayList<KaniTodokeKoumoku>();
		for (var map : mapList) {
			dtoList.add(new KaniTodokeKoumoku(map));
		}
		return dtoList;
	}
	
	/**
	 * 届出ジェネレータ項目定義のレコード有無を判定
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouKbn, int version, String areaKbn, String itemName) {
		return this.find(denpyouKbn, version, areaKbn, itemName) == null ? false : true;
	}
	
	/**
	 * 届出ジェネレータ項目定義から主キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 * @return 届出ジェネレータ項目定義DTO
	 */
	public KaniTodokeKoumoku find(String denpyouKbn, int version, String areaKbn, String itemName) {
		final String sql = "SELECT * FROM kani_todoke_koumoku WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ?";
		return mapToDto(connection.find(sql, denpyouKbn, version, areaKbn, itemName));
	}
	
	/**
	 * 届出ジェネレータ項目定義からレコードを全件取得 ※大量データ取得に注意
	 * @return List<届出ジェネレータ項目定義DTO>
	 */
	public List<KaniTodokeKoumoku> load() {
		final String sql = "SELECT * FROM kani_todoke_koumoku ORDER BY denpyou_kbn, version, area_kbn, item_name";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 届出ジェネレータ項目定義から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @return List<届出ジェネレータ項目定義>DTO
	 */
	public List<KaniTodokeKoumoku> load(String denpyouKbn) {
		final String sql = "SELECT * FROM kani_todoke_koumoku WHERE denpyou_kbn = ? "
							+ "ORDER BY denpyou_kbn, version, area_kbn, item_name";
		return mapToDto(connection.load(sql, denpyouKbn));
	}
	
	/**
	 * 届出ジェネレータ項目定義から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @return List<届出ジェネレータ項目定義>DTO
	 */
	public List<KaniTodokeKoumoku> load(String denpyouKbn, int version) {
		final String sql = "SELECT * FROM kani_todoke_koumoku WHERE denpyou_kbn = ?  AND version = ? "
							+ "ORDER BY denpyou_kbn, version, area_kbn, item_name";
		return mapToDto(connection.load(sql, denpyouKbn, version));
	}
	
	/**
	 * 届出ジェネレータ項目定義から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @return List<届出ジェネレータ項目定義>DTO
	 */
	public List<KaniTodokeKoumoku> load(String denpyouKbn, int version, String areaKbn) {
		final String sql = "SELECT * FROM kani_todoke_koumoku WHERE denpyou_kbn = ?  AND version = ?  AND area_kbn = ? "
							+ "ORDER BY denpyou_kbn, version, area_kbn, item_name";
		return mapToDto(connection.load(sql, denpyouKbn, version, areaKbn));
	}

	/**
	* 届出ジェネレータ項目定義登録
	* @param dto 届出ジェネレータ項目定義
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		KaniTodokeKoumoku dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO kani_todoke_koumoku "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.denpyouKbn, dto.version, dto.areaKbn, dto.itemName, dto.hyoujiJun, dto.buhinType, dto.defaultValue1, dto.defaultValue2, dto.yosanShikkouKoumokuId, koushinUserId, koushinUserId
					);
	}

	/**
	* 届出ジェネレータ項目定義の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したKaniTodokeKoumokuの使用を前提
	* @param dto 届出ジェネレータ項目定義
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		KaniTodokeKoumoku dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE kani_todoke_koumoku "
		    + "SET hyouji_jun = ?, buhin_type = ?, default_value1 = ?, default_value2 = ?, yosan_shikkou_koumoku_id = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ?";
			return connection.update(sql,
				dto.hyoujiJun, dto.buhinType, dto.defaultValue1, dto.defaultValue2, dto.yosanShikkouKoumokuId, koushinUserId
				,dto.koushinTime, dto.denpyouKbn, dto.version, dto.areaKbn, dto.itemName);
    }

	/**
	* 届出ジェネレータ項目定義登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 届出ジェネレータ項目定義
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		KaniTodokeKoumoku dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO kani_todoke_koumoku "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT kani_todoke_koumoku_pkey "
			+ "DO UPDATE SET hyouji_jun = ?, buhin_type = ?, default_value1 = ?, default_value2 = ?, yosan_shikkou_koumoku_id = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.denpyouKbn, dto.version, dto.areaKbn, dto.itemName, dto.hyoujiJun, dto.buhinType, dto.defaultValue1, dto.defaultValue2, dto.yosanShikkouKoumokuId, koushinUserId, koushinUserId
				, dto.hyoujiJun, dto.buhinType, dto.defaultValue1, dto.defaultValue2, dto.yosanShikkouKoumokuId, koushinUserId
				);
    }
	
	/**
	 * 届出ジェネレータ項目定義から主キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, int version, String areaKbn, String itemName){
		final String sql = "DELETE FROM kani_todoke_koumoku WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ?";
		return connection.update(sql, denpyouKbn, version, areaKbn, itemName);
	}
	
	/**
	 * 届出ジェネレータ項目定義から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn) {
		final String sql = "DELETE FROM kani_todoke_koumoku WHERE denpyou_kbn = ? ";
		return connection.update(sql, denpyouKbn);
	}
	
	/**
	 * 届出ジェネレータ項目定義から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, int version) {
		final String sql = "DELETE FROM kani_todoke_koumoku WHERE denpyou_kbn = ?  AND version = ? ";
		return connection.update(sql, denpyouKbn, version);
	}
	
	/**
	 * 届出ジェネレータ項目定義から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, int version, String areaKbn) {
		final String sql = "DELETE FROM kani_todoke_koumoku WHERE denpyou_kbn = ?  AND version = ?  AND area_kbn = ? ";
		return connection.update(sql, denpyouKbn, version, areaKbn);
	}
}
