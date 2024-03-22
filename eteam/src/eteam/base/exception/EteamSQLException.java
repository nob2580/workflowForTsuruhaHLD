package eteam.base.exception;

import java.sql.SQLException;

/**
 * DB操作でのエラー
 */
public class EteamSQLException extends RuntimeException {
	
	/**
	 * コンストラクタ
	 * @param e 根本エラー
	 */
	public EteamSQLException(SQLException e) {
		super(e.getErrorCode() + ":" + e.getMessage(), e); 
	}
}
