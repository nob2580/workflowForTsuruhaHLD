package eteam.base.exception;

import java.sql.SQLException;

/**
 * DB接続でのエラー
 */
public class EteamDBConnectException extends RuntimeException {
	
	/**
	 * コンストラクタ
	 * @param e 根本エラー
	 */
	public EteamDBConnectException(SQLException e) {
		super(e); 
	}
}
