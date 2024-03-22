package eteam.gyoumu.gyoumurolekanri;

import eteam.base.EteamAbstractLogic;

/**
 * 業務ロール管理機能Logic
 */
public class GyoumuRoleKanriLogic extends EteamAbstractLogic {
	
	/**
	 * 業務ロールを追加する
	 * @param gyoumu_role_id   業務ロールID
	 * @param gyoumu_role_name 業務ロール名
	 * @param user_id          登録ユーザーID
	 * @return 結果
	 */
	public int insertGyoumuRole(String gyoumu_role_id, String gyoumu_role_name, String user_id) {
		final String sql = "INSERT INTO gyoumu_role "
						+  "VALUES(?, ?, (SELECT MAX(hyouji_jun + 1) FROM gyoumu_role), ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, gyoumu_role_id, gyoumu_role_name, user_id, user_id);
	}
	
	/**
	 * 業務ロール機能制御を追加する
	 * @param gyoumu_role_id                業務ロールID
	 * @param gyoumu_role_kinou_seigyo_cd   業務ロール機能制御コード
	 * @param gyoumu_role_kinou_seigyo_kbn  業務ロール機能制御区分
	 * @param user_id                       登録ユーザーID
	 * @return 結果
	 */
	public int insertGyoumuRoleKinouSeigyo(String gyoumu_role_id, String gyoumu_role_kinou_seigyo_cd, String gyoumu_role_kinou_seigyo_kbn, String user_id) {
		final String sql = "INSERT INTO gyoumu_role_kinou_seigyo VALUES(?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, gyoumu_role_id, gyoumu_role_kinou_seigyo_cd, gyoumu_role_kinou_seigyo_kbn, user_id, user_id);
	}
	
	/**
	 * 業務ロールの表示順を変更する
	 * @param gyoumu_role_id  業務ロールID
	 * @param hyouji_jun      表示順
	 * @param user_id         更新ユーザーID
	 * @return 結果
	 */
	public int updateHyoujiJunActive(String gyoumu_role_id, int hyouji_jun, String user_id) {
		final String sql = "UPDATE gyoumu_role "
						+  "SET    hyouji_jun = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  gyoumu_role_id = ?";
		return connection.update(sql, hyouji_jun, user_id, gyoumu_role_id);
	}
	
	/**
	 * 業務ロールの更新
	 * @param gyoumu_role_id    業務ロールID
	 * @param gyoumu_role_name  業務ロール名
	 * @param user_id           更新ユーザーID
	 * @return 結果
	 */
	public int updateGyoumuRole(String gyoumu_role_id, String gyoumu_role_name, String user_id) {
		final String sql = "UPDATE gyoumu_role "
						+  "SET    gyoumu_role_name = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  gyoumu_role_id = ?";
		return connection.update(sql, gyoumu_role_name, user_id, gyoumu_role_id);
	}
	
	/**
	 * 業務ロール機能制御を更新する
	 * @param gyoumu_role_id                業務ロールID
	 * @param gyoumu_role_kinou_seigyo_cd   業務ロール機能制御コード
	 * @param gyoumu_role_kinou_seigyo_kbn  業務ロール機能制御区分
	 * @param user_id                       更新ユーザーID
	 * @return 結果
	 */
	public int updateGyoumuRoleKinouSeigyo(String gyoumu_role_id, String gyoumu_role_kinou_seigyo_cd, String gyoumu_role_kinou_seigyo_kbn, String user_id) {
		final String sql = "UPDATE gyoumu_role_kinou_seigyo "
						+  "SET    gyoumu_role_kinou_seigyo_kbn = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  gyoumu_role_id = ? AND gyoumu_role_kinou_seigyo_cd = ?";
		return connection.update(sql, gyoumu_role_kinou_seigyo_kbn, user_id, gyoumu_role_id, gyoumu_role_kinou_seigyo_cd);
	}

	/**
	 * 業務ロールを削除する
	 * @param gyoumu_role_id 業務ロールID
	 * @return 結果
	 */
	public int delete(String gyoumu_role_id) {
		String sql = "DELETE FROM gyoumu_role WHERE gyoumu_role_id = ? ";
		return connection.update(sql, gyoumu_role_id);
	}

	/**
	 * 業務ロール機能制御を削除する
	 * @param gyoumu_role_id 業務ロールID
	 * @return 結果
	 */
	public int deleteKinouSeigyo(String gyoumu_role_id) {
		String sql = "DELETE FROM gyoumu_role_kinou_seigyo WHERE gyoumu_role_id = ? ";
		return connection.update(sql, gyoumu_role_id);
	}
}
