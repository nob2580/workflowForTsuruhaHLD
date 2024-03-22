package eteam.gyoumu.teikijouhou;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 定期情報管理機能Logic
 */
public class TeikiJouhouLogic extends EteamAbstractLogic {
	
	/**
	 * 検索条件に合致する定期情報データリストを取得します。
	 * @param userId ユーザーID
	 * @param shiyoubiFrom 使用日From
	 * @param shiyoubiTo 使用日To
	 * @return 表示用リスト
	 */
	public List<GMap> selectTeikiJouhouList(String userId, Date shiyoubiFrom, Date shiyoubiTo) {
		String sql  = " SELECT a.user_id, "
					+ "        b.shain_no, "
					+ "        (b.user_sei || '　' || b.user_mei) as user_full_name, "
					+ "        a.shiyou_kaishibi, "
					+ "        a.shiyou_shuuryoubi, "
					+ "        a.intra_teiki_kukan, "
					+ "        a.web_teiki_kukan "
					+ " FROM teiki_jouhou AS a "
					+ " LEFT OUTER JOIN user_info AS b ON a.user_id = b.user_id ";
		List<Object> params = new ArrayList<>();
		
		String whereSql = " WHERE 1 = 1 ";
		
		if(!isEmpty(userId)){
			whereSql += " AND a.user_id =  ?";
			params.add(userId);
		}
		
		if(shiyoubiFrom != null){
			whereSql += " AND ? <= a.shiyou_shuuryoubi ";
			params.add(shiyoubiFrom);
		}
		if(shiyoubiTo != null){
			whereSql += " AND a.shiyou_kaishibi <= ? ";
			params.add(shiyoubiTo);
		}
		sql += whereSql;
		sql += " ORDER BY shiyou_shuuryoubi ASC, shain_no ASC ";
		
		return connection.load(sql, params.toArray());
	}
	
	/**
	 * 定期情報データ1件を取得します。
	 * @param userId ユーザーID
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 使用終了日
	 * @return 1件データ、見つからない場合はnull
	 */
	public GMap findTeikiJouhou(String userId, Date shiyouKaishibi, Date shiyouShuuryoubi){
		final String sql = " SELECT * FROM teiki_jouhou WHERE user_id = ? AND shiyou_kaishibi = ? AND shiyou_shuuryoubi = ? ";
		return connection.find(sql, userId, shiyouKaishibi, shiyouShuuryoubi);
	}
	
	/**
	 * 指定ユーザー・指定期間の範囲で、既存の期間が重複している定期券情報レコード数を取得します。
	 * @param userId ユーザーID
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 使用終了日
	 * @return 重複レコード数
	 */
	public long countTeikiJouhouDuplicate(String userId, Date shiyouKaishibi, Date shiyouShuuryoubi){
		final String chkSql = " SELECT COUNT(*) as cnt "
							+ " FROM teiki_jouhou "
							+ " WHERE "
							+ " user_id = ? "
							+ "  AND (   (( ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi ) OR ( ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi )) "
							+ "       OR (shiyou_kaishibi BETWEEN ? AND ? OR shiyou_shuuryoubi BETWEEN ? AND ?) )";
		return (long)connection.find(chkSql, userId, shiyouKaishibi, shiyouShuuryoubi, shiyouKaishibi, shiyouShuuryoubi, shiyouKaishibi, shiyouShuuryoubi).get("cnt");
	}
	
	/**
	 * 指定ユーザー・指定期間の範囲で、既存の期間が重複している定期券情報レコード本体を取得します。
	 * @param userId ユーザーID
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 使用終了日
	 * @return 重複レコード本体
	 */
	public GMap findTeikiJouhouDuplicate(String userId, Date shiyouKaishibi, Date shiyouShuuryoubi){
		final String chkSql = " SELECT * "
				+ " FROM teiki_jouhou "
				+ " WHERE "
				+ " user_id = ? "
				+ "  AND (   (( ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi ) OR ( ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi )) "
				+ "       OR (shiyou_kaishibi BETWEEN ? AND ? OR shiyou_shuuryoubi BETWEEN ? AND ?) )";
		return connection.find(chkSql, userId, shiyouKaishibi, shiyouShuuryoubi, shiyouKaishibi, shiyouShuuryoubi, shiyouKaishibi, shiyouShuuryoubi);
	}
	
	/**
	 * 指定ユーザーID・使用開始日・使用終了日の定期券情報レコードを削除します。
	 * @param userId ユーザーID
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 使用終了日
	 * @return 処理レコード数
	 */
	public int deleteTeikiJouhou(String userId, Date shiyouKaishibi, Date shiyouShuuryoubi){
		final String sql = " DELETE FROM teiki_jouhou WHERE user_id = ? AND shiyou_kaishibi = ? AND shiyou_shuuryoubi = ? ";
		return connection.update(sql, userId, shiyouKaishibi, shiyouShuuryoubi);
	}
	
	/**
	 * 指定ユーザーID・使用開始日・使用終了日の定期券情報レコードについて、使用終了日を9999/12/31に変更します。
	 * @param userId ユーザーID
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 使用終了日
	 * @return 処理レコード数
	 */
	public int mukigenEnchou(String userId, Date shiyouKaishibi, Date shiyouShuuryoubi){
		final String sql = " UPDATE teiki_jouhou SET shiyou_shuuryoubi = TO_DATE('9999/12/31', 'YYYY/MM/DD') WHERE user_id = ? AND shiyou_kaishibi = ? AND shiyou_shuuryoubi = ? ";
		return connection.update(sql, userId, shiyouKaishibi, shiyouShuuryoubi);
	}
}
