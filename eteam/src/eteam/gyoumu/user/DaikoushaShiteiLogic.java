package eteam.gyoumu.user;

import eteam.base.EteamAbstractLogic;

/**
 * 代行者指定ロジック
 */
public class DaikoushaShiteiLogic extends EteamAbstractLogic {

	/**
	 * 代行者を登録する
	 * @param userId 代行者ユーザーID配列
	 * @param hiDaikouUserId 被代行ユーザーID
	 * @param loginUserId ログインユーザーID
	 * @return 登録件数
	 */
	public int insertDaikousha(String[] userId, String hiDaikouUserId, String loginUserId) {
		
		// 前回登録済レコードを削除する
		final String delSql = " DELETE " +
							  "   FROM daikou_shitei " +
							  "  WHERE hi_daikou_user_id = ? ";
		
		connection.update(delSql, hiDaikouUserId);
		
		// 登録処理
		final String insSql = " INSERT INTO daikou_shitei " +
							  " VALUES (?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		int insCnt = 0;
		if (null != userId) for (String id: userId) {
			insCnt = insCnt + connection.update(insSql, id, hiDaikouUserId, loginUserId, loginUserId);
		}
		return insCnt;
		
	}
}
