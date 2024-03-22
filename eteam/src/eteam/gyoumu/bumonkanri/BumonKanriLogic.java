package eteam.gyoumu.bumonkanri;

import java.sql.Date;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 部門管理機能Logic
 */
public class BumonKanriLogic extends EteamAbstractLogic {
	
	/**
	 * 所属部門を追加する
	 * @param bumon_cd      部門コード
	 * @param bumon_name    部門名
	 * @param oya_bumon_cd  親部門コード
	 * @param kigen_from    有効期限開始日
	 * @param kigen_to      有効期限終了日
	 * @param security_pattern セキュリティパターン
	 * @param user_id       登録ユーザーID
	 * @return 結果
	 */
	public int insert(String bumon_cd, String bumon_name, String oya_bumon_cd, Date kigen_from, Date kigen_to, String security_pattern, String user_id) {
		final String sql = "INSERT INTO shozoku_bumon VALUES(?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, bumon_cd, bumon_name, oya_bumon_cd, kigen_from, kigen_to, security_pattern, user_id, user_id);
	}
	
	/**
	 * 所属部門を削除する
	 * @param bumon_cd      部門コード
	 * @return 結果
	 */
	public int delete(String bumon_cd) {
		final StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM shozoku_bumon WHERE bumon_cd = ?");
		return connection.update(sql.toString(), bumon_cd);
	}

	/**
	 * 所属部門テーブルのデータをデフォルト部門以外（0000:全社以外）全て削除します。
	 * @return 結果
	 */
	public int shozokuBumonDelete() {
		return connection.update("DELETE FROM shozoku_bumon WHERE bumon_cd <> '0000'");
	}
	
	/**
	 * 指定部門に子部門が存在するかチェック
	 * @param bumon_cd      部門コード
	 * @return 結果
	 */
	boolean isKobumonExist(String bumon_cd) {
		final String sql = "SELECT COUNT(*) AS cnt FROM shozoku_bumon WHERE oya_bumon_cd = ?";
		GMap mp = connection.find(sql, bumon_cd);
		long cnt = (long)mp.get("cnt");
		return cnt > 0 ? true:false;
	}
}
