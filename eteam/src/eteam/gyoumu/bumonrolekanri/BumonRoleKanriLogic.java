package eteam.gyoumu.bumonrolekanri;

import eteam.base.EteamAbstractLogic;

/**
 * 部門ロール管理機能Logic
 */
public class BumonRoleKanriLogic extends EteamAbstractLogic {
	
	/**
	 * 部門ロールを追加する
	 * @param bumon_role_id   部門ロールID
	 * @param bumon_role_name 部門ロール名
	 * @param user_id          登録ユーザーID
	 * @return 結果
	 */
	public int insert(String bumon_role_id, String bumon_role_name, String user_id) {
		final String sql = "INSERT INTO bumon_role "
						+  "VALUES(?, ?, (SELECT COALESCE(MAX(hyouji_jun + 1), 1) FROM bumon_role), ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, bumon_role_id, bumon_role_name, user_id, user_id);
	}
	
	/**
	 * 部門ロールの表示順を更新する
	 * @param bumon_role_id  部門ロールID
	 * @param hyouji_jun      表示順
	 * @param user_id         更新ユーザーID
	 * @return 結果
	 */
	public int updateHyoujiJunActive(String bumon_role_id, int hyouji_jun, String user_id) {
		final String sql = "UPDATE bumon_role "
						+  "SET    hyouji_jun = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  bumon_role_id = ?";
		return connection.update(sql, hyouji_jun, user_id, bumon_role_id);
	}
	
	/**
	 * 部門ロールの更新
	 * @param bumon_role_id    部門ロールID
	 * @param bumon_role_name  部門ロール名
	 * @param user_id           更新ユーザーID
	 * @return 結果
	 */
	public int update(String bumon_role_id, String bumon_role_name, String user_id) {
		final String sql = "UPDATE bumon_role "
						+  "SET    bumon_role_name = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  bumon_role_id = ?";
		return connection.update(sql, bumon_role_name, user_id, bumon_role_id);
	}

	/**
	 * 部門ロールを削除する
	 * @param bumon_role_id 部門ロールID
	 * @return 結果
	 */
	public int delete(String bumon_role_id) {
		String sql = "DELETE FROM bumon_role WHERE bumon_role_id = ? ";
		return connection.update(sql, bumon_role_id);
	}
}