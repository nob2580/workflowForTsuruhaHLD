package eteam.gyoumu.userjouhoukanri;

import java.sql.Date;

public class UserJouhouKanriExtLogic extends UserJouhouKanriLogic {
	/**
	 * 会社切替設定削除<br>
	 * ユーザIDに紐づく会社切替設定レコードを削除する。
	 *
	 * @param user_id ユーザーID
	 * @return 更新件数
	 */
	public int deleteKaishaKirikaeSettei(String user_id) {
		return connection.update("DELETE FROM kaisha_kirikae_settei WHERE user_id = ?", user_id);
	}
	/**
	 * 会社切替設定登録<br>
	 * ユーザIDに対して会社切替を割り当てる。
	 *
	 * @param user_id        ユーザーID
	 * @param scheme_cd      スキーマコード
	 * @param kigen_from     有効期限開始日
	 * @param kigen_to       有効期限終了日
	 * @param hyouji_jun     表示順
	 * @param login_user_id  ログインユーザーID
	 * @return 更新件数
	 */
	public int insertKaishaKirikaeSettei(String user_id, String scheme_cd, Date kigen_from, Date kigen_to, int hyouji_jun, String login_user_id) {
		final String sql = "INSERT INTO kaisha_kirikae_settei VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, user_id, scheme_cd, kigen_from, kigen_to, hyouji_jun, login_user_id, login_user_id);
	}
//カスタマイズここまで
}
