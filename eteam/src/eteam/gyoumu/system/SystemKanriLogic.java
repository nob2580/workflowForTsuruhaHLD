package eteam.gyoumu.system;

import eteam.base.EteamAbstractLogic;

/**
 * システム管理機能Logic
 */
public class SystemKanriLogic extends EteamAbstractLogic {
	
	/**
	 * システム管理の機能制御を更新する
	 * @param kinouSeigyoCd            機能制御コード
	 * @param kinouSeigyoKbn           機能制御区分
	 * @return 結果
	 */
	public int updateKinouSeigyo(String kinouSeigyoCd, String kinouSeigyoKbn) {
		final String sql = "UPDATE kinou_seigyo SET kinou_seigyo_kbn = ? WHERE kinou_seigyo_cd = ?";
		return connection.update(sql, kinouSeigyoKbn, kinouSeigyoCd);
	}
	
	/**
	 * システム管理のメール設定を更新する（メールパスワードを更新しない）
	 * @param smtpServerName           SMTPサーバ名
	 * @param portNo                   ポート番号
	 * @param ninshouHouhou            認証方法
	 * @param angoukaHouhou            暗号化方法
	 * @param mailAddress              メールアドレス
	 * @param mailId                   メールID
	 * @return 結果
	 */
	public int updateMailSettei(String smtpServerName, String portNo, String ninshouHouhou, String angoukaHouhou, String mailAddress, String mailId) {
		final String sql = "UPDATE mail_settei SET smtp_server_name = ?, port_no = ?, ninshou_houhou = ?, angouka_houhou = ?, mail_address = ?, mail_id = ?";
		return connection.update(sql, smtpServerName, portNo, ninshouHouhou, angoukaHouhou, mailAddress, mailId);
	}
	/**
	 * システム管理のメール設定を更新する（メールパスワードを更新する）
	 * @param smtpServerName           SMTPサーバ名
	 * @param portNo                   ポート番号
	 * @param ninshouHouhou            認証方法
	 * @param angoukaHouhou            暗号化方法
	 * @param mailAddress              メールアドレス
	 * @param mailId                   メールID
	 * @param mailPassword             メールパスワード
	 * @return 結果
	 */
	public int updateMailSettei(String smtpServerName, String portNo, String ninshouHouhou, String angoukaHouhou, String mailAddress, String mailId, String mailPassword) {
		final String sql = "UPDATE mail_settei SET smtp_server_name = ?, port_no = ?, ninshou_houhou = ?, angouka_houhou = ?, mail_address = ?, mail_id = ?, mail_password = ?";
		return connection.update(sql, smtpServerName, portNo, ninshouHouhou, angoukaHouhou, mailAddress, mailId, mailPassword);
	}

	/**
	 * 設定情報を更新する
	 * @param settingName 設定名
	 * @param settingVal 設定値
	 */
	public void updateSettingInfo(String settingName, String settingVal) {
		connection.update("UPDATE setting_info SET setting_val = ? WHERE setting_name = ?", settingVal, settingName);
	}

	/**
	 * 仕訳パターンマスターの社員コード連携フラグを一律更新する
	 * @param  shainCdRenkeiFlgAf 更新後社員コード連携フラグ
	 * @param  shainCdRenkeiFlgBf 更新前社員コード連携フラグ
	 * @param  userId           ユーザーID
	 * @return 結果
	 */
	public int updateshiwakePForshainRenkei(String shainCdRenkeiFlgAf, String shainCdRenkeiFlgBf, String userId) {
		final String sql = "UPDATE shiwake_pattern_master SET shain_cd_renkei_flg = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE shain_cd_renkei_flg = ? AND delete_flg = '0'";
		return connection.update(sql, shainCdRenkeiFlgAf, userId, shainCdRenkeiFlgBf);
	}

	/**
	 * 伝票番号採番を更新する
	 * @param  serialNoStart 開始番号(伝票番号)
	 * @param  serialNoEnd   終了番号(伝票番号)
	 * @return 結果
	 */
	public int updateDenpyouSerialNoSaiban(int serialNoStart, int serialNoEnd) {
		final String sql = "UPDATE denpyou_serial_no_saiban "
							+ "SET sequence_val = (CASE WHEN sequence_val < ? THEN ? -2 ELSE sequence_val END) "
							+ "   ,max_value = ? -1"
							+ "   ,min_value = ?";
		return connection.update(sql, serialNoStart, serialNoStart, serialNoEnd, serialNoStart);
	}
	
	/**
	 * 伝票番号採番を更新する
	 * @param  denpyouKbn 伝票区分
	 * @param  serialNoStart 開始番号(伝票番号)
	 * @param  serialNoEnd   終了番号(伝票番号)
	 * @return 結果
	 */
	public int updateDenpyouSerialNoSaibanKyoten(String denpyouKbn, int serialNoStart, int serialNoEnd) {
		final String sql = "UPDATE denpyou_serial_no_saiban_kyoten "
							+ " SET sequence_val = (CASE WHEN sequence_val < ? THEN ? -1 ELSE sequence_val END) "
							+ "   ,max_value = ?"
							+ "   ,min_value = ?"
							+ " WHERE denpyou_kbn = ?";
		return connection.update(sql, serialNoStart, serialNoStart, serialNoEnd, serialNoStart, denpyouKbn);
	}
}
