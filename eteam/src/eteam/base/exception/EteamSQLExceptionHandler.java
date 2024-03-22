package eteam.base.exception;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * SQLエラーをシステム開発標準の例外にラップする
 */
public class EteamSQLExceptionHandler {

	/**
	 * SQLエラーをシステム開発標準の例外にラップする
	 * @param e 根本エラー
	 * @return ラップエラー
	 */
	public static EteamSQLException makeException(SQLException e) {
		if (e instanceof SQLIntegrityConstraintViolationException ||"23505".equals(e.getSQLState())) {
			//重複
			return new EteamSQLDuplicateException(e);
		} else if ("42P01".equals(e.getSQLState())){
			//テーブル存在しない
			return new EteamSQLTableNotFoundException(e);
		}
		//他
		return new EteamSQLException(e);
	}
}
