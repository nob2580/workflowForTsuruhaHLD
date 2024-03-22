package eteam.base.exception;

import java.sql.SQLException;

/**
 * DB操作でのエラー（更新時重複発生）
 */
public class EteamSQLDuplicateException extends EteamSQLException {
	
	/**
	 * コンストラクタ
	 * @param e 根本エラー
	 */
	public EteamSQLDuplicateException(SQLException e) {
		super(e); 
	}
}
