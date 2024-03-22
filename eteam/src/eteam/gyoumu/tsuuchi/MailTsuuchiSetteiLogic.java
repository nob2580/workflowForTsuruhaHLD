package eteam.gyoumu.tsuuchi;

import eteam.base.EteamAbstractLogic;

/**
 * メール通知一覧画面Logic
 */
public class MailTsuuchiSetteiLogic extends EteamAbstractLogic {
	
	/**
	 * メール通知テーブルにデータを登録する
	 * @param userId セッション情報.ログインユーザーID
	 * @param tsuuchiKbn コード定義の各内部コード値
	 * @param soushinumu 送信有無(チェックがあれば '1' なければ '0')
	 */
	public void insert(String userId, String tsuuchiKbn, String soushinumu) {
		
		final String sql = "INSERT INTO mail_tsuuchi "
				 			+ " (user_id, tsuuchi_kbn, soushinumu) VALUES "
				 			+ " (?, ?, ?) ";
		connection.update(sql, userId, tsuuchiKbn, soushinumu);
		
	}
	
	/**
	 * メール通知テーブルのデータを更新する
	 * @param userId セッション情報.ログインユーザーID
	 * @param tsuuchiKbn コード定義の各内部コード値
	 * @param soushinumu 送信有無(チェックがあれば '1' なければ '0')
	 */
	public void update(String userId, String tsuuchiKbn, String soushinumu) {
		
		final String sql = "UPDATE mail_tsuuchi "
				 			+ " SET soushinumu = ? "
				 			+ " WHERE user_id = ? "
				 			+ " AND tsuuchi_kbn = ? ";
		connection.update(sql, soushinumu, userId, tsuuchiKbn);
		
	}
}
