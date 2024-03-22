package eteam.database.dao;

import eteam.database.abstractdao.NiniCommentAbstractDao;

/**
 * 任意メモに対するデータ操作クラス
 */
public class NiniCommentDao extends NiniCommentAbstractDao {

	/**
	 * 任意メモを追加する
	 * @param denpyouId      伝票ID
	 * @param userId         ユーザーID
	 * @param userFullName  ユーザーフル名
	 * @param comment         コメント
	 * @return 結果
	 */
	public int insertNinimemo(String denpyouId, String userId, String userFullName, String comment) {
		final String sql = "INSERT INTO nini_comment VALUES"
				+ "(?, (SELECT COALESCE(MAX(edano + 1), 1) FROM nini_comment WHERE denpyou_id = ?), ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, denpyouId, denpyouId, userId, userFullName, comment, userId, userId);
	}
}
