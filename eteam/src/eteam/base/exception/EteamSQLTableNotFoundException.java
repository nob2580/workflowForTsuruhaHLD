package eteam.base.exception;

import java.sql.SQLException;

/**
 * DB操作でのエラー（テーブルが存在しない）
 */
public class EteamSQLTableNotFoundException extends EteamSQLException {
	
	/**
	 * コンストラクタ
	 * @param e 根本エラー
	 */
	public EteamSQLTableNotFoundException(SQLException e) {
		super(e); 
	}
}
