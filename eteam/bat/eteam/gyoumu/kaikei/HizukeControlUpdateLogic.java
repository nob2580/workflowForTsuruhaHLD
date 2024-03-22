package eteam.gyoumu.kaikei;

import java.sql.Date;

import eteam.base.EteamAbstractLogic;

/**
 * 日付コントロール更新ロジック
 */
public class HizukeControlUpdateLogic extends EteamAbstractLogic {

	/**
	 * 当日区分フラグ = 1のレコードに対してフラグを0に更新する。
	 */
	public void updateToujitsuKbnFlgOff() {
		final String sql = "UPDATE hizuke_control SET toujitsu_kbn_flg = '0' WHERE toujitsu_kbn_flg = '1'";
		connection.update(sql);
	}

	/**
	 * システム管理日付 = システム日付のレコードに対して当日区分フラグ = 1にする。
	 * @return 更新件数
	 */
	public int updateToujitsuKbnFlgOn() {
		final String sql = "UPDATE hizuke_control SET toujitsu_kbn_flg = '1' WHERE system_kanri_date = ?";
		return connection.update(sql, new Date(System.currentTimeMillis()));
	}
}
