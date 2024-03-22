package eteam.common;

import java.sql.Date;
import java.util.Calendar;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.gyoumu.user.User;


/**
 * 駅すぱあと共通Logic
 */
public class EteamEkispertCommonLogic extends EteamAbstractLogic {

	/**
	 * 定期券情報を取得する
	 * @param userId ユーザーID
	 * @param date 日付
	 * @return 定期券情報
	 */
	public GMap loadTeikiJouhou(String userId, Date date) {
		final String sql = "SELECT * "
							+ "FROM teiki_jouhou "
							+ "WHERE "
							+ "  user_id = ? "
							+ "  AND ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi ";
		GMap ret = connection.find(sql, userId, date);
		return ret;
	}

	/**
	 * 定期券情報を登録する
	 * @param route 路線区間（駅すぱあと検索結果）
	 * @param teikiSerializeData 定期シリアライズデータ
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 使用終了日
	 * @param entryUserId 起票ユーザーID
	 * @param loginUser ログインユーザー
	 * @return 処理件数
	 */
	public int updateTeikiJouhou(String route, String teikiSerializeData, Date shiyouKaishibi, Date shiyouShuuryoubi, String entryUserId, User loginUser){
		
		// 使用開始日の前日
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(shiyouKaishibi);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date bfShiyouKaishibi = new Date(calendar.getTimeInMillis());
		
		// 駅すぱあとのイントラフラグを取得する
		String intraFlg = setting.intraFlg();
		
		// イントラ版はハイフン繋ぎ
		if ("1".equals(intraFlg)) {
			String cnvRoute = route.replaceAll("－", "＝");
			// 定期券情報を登録する
			return updateTeikiJouhou(entryUserId, shiyouKaishibi, shiyouShuuryoubi, cnvRoute, teikiSerializeData, null, "", bfShiyouKaishibi, loginUser.getSeigyoUserId());

		} else {
			// Webサービス番は
			// 駅名:路線名:駅名:路線名:駅名の形式
			String cnvRoute = route.replaceAll("＝", ":");
			
			// 定期券情報を登録する
			return updateTeikiJouhou(entryUserId, shiyouKaishibi, shiyouShuuryoubi, null, "", cnvRoute, teikiSerializeData, bfShiyouKaishibi, loginUser.getSeigyoUserId());
		}
	}
	
	/**
	 * 定期券情報を登録する
	 * @param userId ユーザーID
	 * @param shiyouKaishibi 使用開始日
	 * @param shiyouShuuryoubi 仕様終了日
	 * @param intraTeikiKukan イントラ版定期区間
	 * @param intraRestoreroute イントラ版方向性付き定期経路文字列
	 * @param webTeikiKukan Webサービス版定期区間情報
	 * @param webTeikiSerializeData Webサービス版定期区間シリアライズデータ
	 * @param bfShiyouKaishibi 使用開始日の前日
	 * @param tourokuUserId ログインユーザーID
	 * @return 処理件数
	 */
	protected int updateTeikiJouhou(String userId, Date shiyouKaishibi, Date shiyouShuuryoubi, String intraTeikiKukan,
			String intraRestoreroute, String webTeikiKukan, String webTeikiSerializeData, Date bfShiyouKaishibi, String tourokuUserId) {

		// 期間重複していた場合、既存レコードを削除する
		final String chkSql = "DELETE "
							+ "FROM teiki_jouhou "
							+ "WHERE "
							+ "  user_id = ? "
							+ "  AND (   (( ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi ) OR ( ? BETWEEN shiyou_kaishibi AND shiyou_shuuryoubi )) "
							+ "       OR (shiyou_kaishibi BETWEEN ? AND ? OR shiyou_shuuryoubi BETWEEN ? AND ?) )";
		connection.update(chkSql, userId, shiyouKaishibi, shiyouShuuryoubi, shiyouKaishibi, shiyouShuuryoubi, shiyouKaishibi, shiyouShuuryoubi);

		// 新規定期情報を登録する
		final String insSql = "INSERT INTO teiki_jouhou "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(insSql, userId, shiyouKaishibi, shiyouShuuryoubi, intraTeikiKukan, intraRestoreroute, webTeikiKukan, webTeikiSerializeData, tourokuUserId, tourokuUserId);
	}
	

	
}
