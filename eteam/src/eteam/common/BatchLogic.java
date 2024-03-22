package eteam.common;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * バッチ排他Logic
 */
public class BatchLogic extends EteamAbstractLogic {

	/**
	 * 排他を掛ける
	 */
	public void lock() {
		String sql = "INSERT INTO batch_haita_seigyo values('1')";
		connection.update(sql);
	}

	/**
	 * 排他を解く
	 */
	public void unlock() {
		String sql = "DELETE FROM batch_haita_seigyo";
		connection.update(sql);
	}

	/**
	 * バッチログのINSERT
	 * @param batchName バッチ名
	 * @param countName 件数名
	 * @return インクリメントされたシリアル番号
	 */
	public long startLog(String batchName, String countName) {
		final String sql = "INSERT INTO batch_log(start_time, batch_name, batch_status, count_name, batch_kbn) values(current_timestamp, ?, ?, ?, '1') RETURNING serial_no;";
		GMap ret = connection.find(sql, batchName, EteamNaibuCodeSetting.BATCH_STATUS.SHORICHUU, countName);
		return (long)ret.get("serial_no");
	}

	/**
	 * バッチログのUPDATE（ステータス等）
	 * @param serialNo シリアル番号
	 * @param batchStatus バッチステータス
	 * @param count 件数
	 */
	public void endLog(long serialNo, String batchStatus, int count) {
		final String sql = "UPDATE batch_log set end_time = current_timestamp, batch_status = ?, count = ? where serial_no = ?";
		connection.update(sql, batchStatus, count, serialNo);
	}
}
