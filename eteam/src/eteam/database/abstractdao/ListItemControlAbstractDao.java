package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ListItemControl;

/**
 * 一覧表示項目制御に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ListItemControlAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ListItemControl mapToDto(GMap map){
		return map == null ? null : new ListItemControl(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ListItemControl> mapToDto(List<GMap> mapList){
		List<ListItemControl> dtoList = new ArrayList<ListItemControl>();
		for (var map : mapList) {
			dtoList.add(new ListItemControl(map));
		}
		return dtoList;
	}
	
	/**
	 * 一覧表示項目制御のレコード有無を判定
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @param index 項番
	 * @return true:exist false:not exist
	 */
	public boolean exists(String kbn, String userId, String gyoumuRoleId, int index) {
		return this.find(kbn, userId, gyoumuRoleId, index) == null ? false : true;
	}
	
	/**
	 * 一覧表示項目制御から主キー指定でレコードを取得
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @param index 項番
	 * @return 一覧表示項目制御DTO
	 */
	public ListItemControl find(String kbn, String userId, String gyoumuRoleId, int index) {
		final String sql = "SELECT * FROM list_item_control WHERE kbn = ? AND user_id = ? AND gyoumu_role_id = ? AND index = ?";
		return mapToDto(connection.find(sql, kbn, userId, gyoumuRoleId, index));
	}
	
	/**
	 * 一覧表示項目制御からレコードを全件取得 ※大量データ取得に注意
	 * @return List<一覧表示項目制御DTO>
	 */
	public List<ListItemControl> load() {
		final String sql = "SELECT * FROM list_item_control ORDER BY kbn, user_id, gyoumu_role_id, index";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 一覧表示項目制御から一部キー指定でレコードを取得
	 * @param kbn 表示項目区分
	 * @return List<一覧表示項目制御>DTO
	 */
	public List<ListItemControl> load(String kbn) {
		final String sql = "SELECT * FROM list_item_control WHERE kbn = ? "
							+ "ORDER BY kbn, user_id, gyoumu_role_id, index";
		return mapToDto(connection.load(sql, kbn));
	}
	
	/**
	 * 一覧表示項目制御から一部キー指定でレコードを取得
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @return List<一覧表示項目制御>DTO
	 */
	public List<ListItemControl> load(String kbn, String userId) {
		final String sql = "SELECT * FROM list_item_control WHERE kbn = ?  AND user_id = ? "
							+ "ORDER BY kbn, user_id, gyoumu_role_id, index";
		return mapToDto(connection.load(sql, kbn, userId));
	}
	
	/**
	 * 一覧表示項目制御から一部キー指定でレコードを取得
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @return List<一覧表示項目制御>DTO
	 */
	public List<ListItemControl> load(String kbn, String userId, String gyoumuRoleId) {
		final String sql = "SELECT * FROM list_item_control WHERE kbn = ?  AND user_id = ?  AND gyoumu_role_id = ? "
							+ "ORDER BY kbn, user_id, gyoumu_role_id, index";
		return mapToDto(connection.load(sql, kbn, userId, gyoumuRoleId));
	}

	/**
	* 一覧表示項目制御登録
	* @param dto 一覧表示項目制御
	* @return 件数
	*/
	public int insert(
		ListItemControl dto
	){
		final String sql =
				"INSERT INTO list_item_control "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.kbn, dto.userId, dto.gyoumuRoleId, dto.index, dto.name, dto.displayFlg
					);
	}

	/**
	* 一覧表示項目制御登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 一覧表示項目制御
	* @return 件数
	*/
	public int upsert(
		ListItemControl dto
		 ){
		final String sql =
				"INSERT INTO list_item_control "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT list_item_control_pkey "
			+ "DO UPDATE SET name = ?, display_flg = ? "
			+ "";
			return connection.update(sql,
				dto.kbn, dto.userId, dto.gyoumuRoleId, dto.index, dto.name, dto.displayFlg
				, dto.name, dto.displayFlg
				);
    }
	
	/**
	 * 一覧表示項目制御から主キー指定でレコードを削除
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @param index 項番
	 * @return 削除件数
	 */
	public int delete(String kbn, String userId, String gyoumuRoleId, int index){
		final String sql = "DELETE FROM list_item_control WHERE kbn = ? AND user_id = ? AND gyoumu_role_id = ? AND index = ?";
		return connection.update(sql, kbn, userId, gyoumuRoleId, index);
	}
	
	/**
	 * 一覧表示項目制御から一部キー指定でレコードを削除
	 * @param kbn 表示項目区分
	 * @return 削除件数
	 */
	public int delete(String kbn) {
		final String sql = "DELETE FROM list_item_control WHERE kbn = ? ";
		return connection.update(sql, kbn);
	}
	
	/**
	 * 一覧表示項目制御から一部キー指定でレコードを削除
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @return 削除件数
	 */
	public int delete(String kbn, String userId) {
		final String sql = "DELETE FROM list_item_control WHERE kbn = ?  AND user_id = ? ";
		return connection.update(sql, kbn, userId);
	}
	
	/**
	 * 一覧表示項目制御から一部キー指定でレコードを削除
	 * @param kbn 表示項目区分
	 * @param userId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @return 削除件数
	 */
	public int delete(String kbn, String userId, String gyoumuRoleId) {
		final String sql = "DELETE FROM list_item_control WHERE kbn = ?  AND user_id = ?  AND gyoumu_role_id = ? ";
		return connection.update(sql, kbn, userId, gyoumuRoleId);
	}
}
