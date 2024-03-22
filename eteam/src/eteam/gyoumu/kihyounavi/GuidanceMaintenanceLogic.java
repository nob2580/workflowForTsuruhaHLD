package eteam.gyoumu.kihyounavi;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ガイダンスメンテナンスLogic
 */
@Getter @Setter @ToString
public class GuidanceMaintenanceLogic extends EteamAbstractLogic {
	
/* 見出し(midashi) */
	/**
	 * 見出しを追加する。
	 * @param midashiName 見出し名
	 * @param userId ユーザID
	 * @return 自動採番された見出しID
	 */
	public long insertMidashi(String midashiName, String userId) {
		final String sql = "INSERT INTO midashi(midashi_name, hyouji_jun, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
						 + "VALUES(?, (SELECT COALESCE(MAX(hyouji_jun) + 1, 1) FROM midashi), ?, current_timestamp, ?, current_timestamp) "
						 + "RETURNING midashi_id ";
		GMap ret = connection.find(sql, midashiName, userId, userId);
		return (long)ret.get("midashi_id");
	}

	/**
	 * 見出しを更新する。
	 * @param midashiId 見出しID
	 * @param midashiName 見出し名
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int updateMidashi(Long midashiId, String midashiName, String userId) {
		final String sql = "UPDATE midashi SET midashi_name = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE midashi_id = ?";
		return connection.update(sql, midashiName, userId, midashiId);
	}

	/**
	 * 見出しの表示順を更新する。
	 * @param midashiId 見出しID
	 * @param hyoujiJun 表示順
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int updateMidashiSort(Long midashiId, int hyoujiJun, String userId) {
		final String sql = "UPDATE midashi SET hyouji_jun=?, koushin_user_id = ?, koushin_time = current_timestamp WHERE midashi_id=?";
		return connection.update(sql, hyoujiJun, userId, midashiId);
	}
	
	/**
	 * 見出しを削除する。見出しに紐付く事象と事象伝票区分関連も削除
	 * @param midashiId 見出しID
	 * @return 削除件数
	 */
	public int deleteMidashi(Long midashiId) {
		final String sql = "DELETE FROM midashi WHERE midashi_id=?";
		return connection.update(sql, midashiId);
	}
	
/* 事象(jishou) */
	/**
	 * 事象を追加する。
	 * @param midashiId 見出しID
	 * @param jishouName 事象名
	 * @param userId ユーザID
	 * @return 自動採番された事象ID
	 */
	public long insertJishou(Long midashiId, String jishouName, String userId) {
		final String sql = "INSERT INTO jishou(midashi_id, jishou_name, hyouji_jun, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
						 + "VALUES(?, ?, (SELECT COALESCE(MAX(hyouji_jun) + 1, 1) FROM jishou WHERE midashi_id = ?), ?, current_timestamp, ?, current_timestamp) "
						 + "RETURNING jishou_id ";
		GMap ret = connection.find(sql, midashiId, jishouName, midashiId, userId,userId);
		return (long)ret.get("jishou_id");
	}

	/**
	 * 事象を更新する。
	 * @param jishouId 事象ID
	 * @param jishouName 事象名
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int updateJishou(Long jishouId, String jishouName, String userId) {
		final String sql = "UPDATE jishou SET jishou_name = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE jishou_id = ?";
		return connection.update(sql, jishouName, userId, jishouId);
	}

	/**
	 * 事象の表示順を更新する。
	 * @param jishouId 事象ID
	 * @param hyoujiJun 表示順
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int updateJishouSort(Long jishouId, int hyoujiJun, String userId) {
		final String sql = "UPDATE jishou SET hyouji_jun = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE jishou_id = ?";
		return connection.update(sql, hyoujiJun, userId, jishouId);
	}

	/**
	 * 事象を削除する。
	 * @param jishouId 事象ID
	 * @return 削除件数
	 */
	public int deleteJishou(Long jishouId) {
		final String sql = "DELETE FROM jishou WHERE jishou_id=?";
		return connection.update(sql, jishouId);
	}

	/**
	 * 見出に紐付く事象を削除する。
	 * @param midashiId 見出ID
	 * @return 削除件数
	 */
	public int deleteJishouByMidashi(Long midashiId) {
		final String sql = "DELETE FROM jishou WHERE midashi_id = ?";
		return connection.update(sql, midashiId);
	}
	
/* 事象伝票区分関連(jishou_dpkbn_kanren) */
	/**
	 * 事象伝票区分関連にレコードを追加する。
	 * @param jishouId 事象ID
	 * @param denpyouKbn 伝票区分
	 * @param hyoujiJun 表示順
	 * @param userId ユーザID
	 * @return 処理結果
	 */
	public int insertJishouDpkbnKanren(Long jishouId, String denpyouKbn, int hyoujiJun, String userId) {
		final String sql = "INSERT INTO jishou_dpkbn_kanren(jishou_id, denpyou_kbn, hyouji_jun, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
						 + "VALUES(?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		 return connection.update(sql, jishouId, denpyouKbn, hyoujiJun, userId,userId);
	}

	/**
	 * 事象伝票区分関連を削除する。
	 * @param jishouId 事象ID
	 * @return 削除件数
	 */
	public int deleteJishouDpkbnKanren(Long jishouId) {
		final String sql = "DELETE FROM jishou_dpkbn_kanren WHERE jishou_id = ?";
		return connection.update(sql, jishouId);
	}

	/**
	 * 見出しに紐付く事象伝票区分関連を削除する。
	 * @param midashiId 事象ID
	 * @return 削除件数
	 */
	public int deleteJishouDpkbnKanrenByMidashi(Long midashiId) {
		final String sql = "DELETE FROM jishou_dpkbn_kanren WHERE jishou_id in (SELECT jishou_id FROM jishou WHERE midashi_id = ?)";
		return connection.update(sql, midashiId);
	}

/* お気に入り起票(bookmark) */
	/**
	 * ブックマールテーブルからユーザIDの一致するレコードを削除する。
	 * @param userId ユーザーID
	 * @return 削除件数
	 */
	public int deleteOkiniiri(String userId){
		final String sql = "DELETE FROM bookmark WHERE user_id=?";
		return connection.update(sql,userId);
	}

	/**
	 * ブックマールテーブルにレコードを追加する。
	 * @param seigyoUserId 制御ユーザーID
	 * @param denpyouKbn 伝票区分
	 * @param hyoujiJun 表示順
	 * @param memo メモ
	 * @param userId ユーザーID
	 * @return 処理結果
	 */
	public int insertOkiniiri(String seigyoUserId, String denpyouKbn, int hyoujiJun, String memo, String userId) {
		final String sql = "INSERT INTO bookmark(user_id, denpyou_kbn, hyouji_jun, memo, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
						 + "VALUES(?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		 return connection.update(sql, seigyoUserId, denpyouKbn, hyoujiJun, memo, userId, userId);
	}

}
