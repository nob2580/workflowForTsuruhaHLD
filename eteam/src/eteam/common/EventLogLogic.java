package eteam.common;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * イベントログLogic
 * 各種アクションの前後で自動実行するので、エラーが出ても流して続けられたし。
 */
public class EventLogLogic extends EteamAbstractLogic {

	/**
	 * 利用履歴のINSERT（開始時刻）
	 * @param userId ログインユーザーのユーザーID(未ログインならブランク)
	 * @param gamenId 画面ID
	 * @param eventId イベントID
	 * @return インクリメントされたシリアル番号
	 */
	public long startLog(String userId, String gamenId, String eventId) {
		final String sql = "INSERT INTO event_log(start_time, end_time, user_id, gamen_id, event_id, result) values(current_timestamp, null, ?, ?, ?, '') RETURNING serial_no;";
		GMap ret = connection.find(sql, userId, gamenId, eventId);
		return (long)ret.get("serial_no");
	}

	/**
	 * 利用履歴のINSERT（開始時刻）
	 * @param serialNo シリアル番号
	 * @param result 処理結果
	 */
	public void endLog(long serialNo, String result) {
		final String sql = "UPDATE event_log set end_time = current_timestamp, result = ? where serial_no = ?";
		connection.update(sql, result, serialNo);
	}
}
