package eteam.gyoumu.kaikei;

import java.sql.Date;

import eteam.base.EteamAbstractLogic;
import eteam.base.exception.EteamSQLDuplicateException;

/**
 * 請求書払い申請締画面Logic
 */
public class ShimeLogic extends EteamAbstractLogic {

	/**
	 * 締日を登録する
	 * @param denpyouKbn 伝票区分
	 * @param shimebi 締日
	 * @param userId ユーザーID
	 */
	public void insertShinsei(String denpyouKbn, Date shimebi, String userId) {
		final String sql = "INSERT INTO keijoubi_shimebi VALUES(?, ?, ?, current_timestamp, ?, current_timestamp)";
		try {
			connection.update(sql, denpyouKbn, shimebi, userId, userId);
		} catch (EteamSQLDuplicateException e) {
			//重複登録は無視}
		}
	}

	/**
	 * 締日を削除する
	 * @param denpyouKbn 伝票区分
	 * @param shimebi 締日
	 */
	public void deleteShinsei(String denpyouKbn, Date shimebi) {
		final String sql = "DELETE FROM keijoubi_shimebi WHERE denpyou_kbn = ? AND shimebi = ?";
		connection.update(sql, denpyouKbn, shimebi);
	}
}
