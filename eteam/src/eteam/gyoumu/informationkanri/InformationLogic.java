package eteam.gyoumu.informationkanri;

import java.sql.Date;

import eteam.base.EteamAbstractLogic;

/**
 * インフォメーション管理Logic
 */
public class InformationLogic extends EteamAbstractLogic {

	/**
	 * 削除する。
	 * @param infoId インフォメーションID
	 * @return 処理結果
	 */
	public int delete(String infoId) {
		final String sql = "DELETE FROM information WHERE info_id=?";
		return connection.update(sql, infoId);
	}

	/**
	 * 更新する。
	 * @param infoId         インフォメーションID
	 * @param keijiKikanFrom 掲示期間開始日
	 * @param keijiKikanTo   掲示期間終了日
	 * @param tsuuchinaiyou  通知内容
	 * @param userId         更新ユーザーID
	 * @return 処理結果
	 */
	public int update(String infoId, Date keijiKikanFrom, Date keijiKikanTo, String tsuuchinaiyou, String userId) {
		final String sql = "UPDATE information "
							+ "SET tsuuchi_kikan_from=?, tsuuchi_kikan_to=?, tsuuchi_naiyou=?, koushin_user_id=?, koushin_time=current_timestamp "
							+ "WHERE info_id=?";
		return connection.update(sql, keijiKikanFrom, keijiKikanTo, tsuuchinaiyou, userId, infoId);
	}

	/**
	 * 新規登録する。
	 * @param informationId  インフォメーションID
	 * @param keijiKikanFrom 掲示期間開始日
	 * @param keijiKikanTo   掲示期間終了日
	 * @param tsuuchinaiyou  通知内容
	 * @param userId         登録ユーザーID
	 * @return 処理結果
	 */
	public int insert(String informationId, Date keijiKikanFrom, Date keijiKikanTo, String tsuuchinaiyou, String userId) {

		final String sql = "INSERT INTO information VALUES(?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, informationId, keijiKikanFrom, keijiKikanTo, tsuuchinaiyou, userId, userId);
	}

	/**
	 * ソート順の削除
	 * @return 処理結果
	 */
	public int deleteSort() {
		final String sql = "DELETE FROM information_sort";
		return connection.update(sql);
	}
	
	/**
	 * ソート順の追加
	 * @param informationId インフォメーションID
	 * @param hyoujiJun 表示順
	 * @return 処理結果
	 */
	public int insertSort(String informationId, int hyoujiJun) {

		final String sql = "INSERT INTO information_sort VALUES(?, ?)";
		return connection.update(sql, informationId, hyoujiJun);
	}

}
