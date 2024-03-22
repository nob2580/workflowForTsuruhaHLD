package eteam.common;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * ユーザー単位の設定値クラス
 */
public class EteamUserSettingLogic extends EteamAbstractLogic{
	
	/**
	 * ユーザーデフォルト値区分
	 * =user_default_value.kbn
	 */
	public enum UserDefaultValueKbn {
		/**
		 * 付替区分
		 */
		tsukekaeKbn,
		/**
		 * 伝票一覧画面自動更新区分
		 */
		ichiranReloadKbn,
		/**
		 * 取下済・否認済　表示区分
		 */
		torisageHininHyoujiKbn
		
        // 区分はココに追加してください
    };
    
    /**
     *  ユーザーデフォルト値を取得。設定がない場合はブランク
     * @param kbn ユーザーデフォルト値区分
	 * @param userId ユーザーID
	 * @return ユーザーデフォルト値情報マップ
	 */
	public GMap selectUserDefaultValue(UserDefaultValueKbn kbn, String userId) {
		final String sql = "SELECT * FROM user_default_value WHERE kbn = ? AND user_id = ?";
		return connection.find(sql, kbn.toString(), userId);
	}
	
	/**
	 * ユーザーデフォルト値情報テーブルの更新
	 * ※upsertはPostgreSQL 9.5から。。
	 * @param kbn ユーザーデフォルト値区分
	 * @param userId ユーザーID
	 * @param defaultValue デフォルト値
	 * @return 結果
	 */
	public int upsertUserDefaultValue(UserDefaultValueKbn kbn, String userId, String defaultValue) {
		if(this.existUserDefaultValue(kbn, userId)) {
			return updateUserDefaultValue(kbn, userId, defaultValue);
		}else {
			return insertUserDefaultValue(kbn, userId, defaultValue);
		}
	}
	
	/**
	 * ユーザーデフォルト値情報テーブルにレコードを追加
	 * @param kbn ユーザーデフォルト値区分
	 * @param userId ユーザーID
	 * @param defaultValue 値
	 * @return 結果
	 */
	protected int insertUserDefaultValue(UserDefaultValueKbn kbn, String userId, String defaultValue) {
		final String sql = "INSERT INTO user_default_value VALUES(?, ?, ?)";
		return connection.update(sql, kbn.toString(), userId, defaultValue);
	}
	
	/**
	 * ユーザーデフォルト値情報テーブルのレコードを更新
	 * @param kbn ユーザーデフォルト値区分
	 * @param userId ユーザーID
	 * @param defaultValue デフォルト値
	 * @return 結果
	 */
	protected int updateUserDefaultValue(UserDefaultValueKbn kbn, String userId, String defaultValue) {
		final String sql = "UPDATE user_default_value SET default_value = ? WHERE kbn = ? AND user_id = ?";
		return connection.update(sql, defaultValue, kbn.toString(), userId);
	}
	
	/**
	 * ユーザーデフォルト値情報テーブルのレコード有無を確認
	 * @param kbn ユーザーデフォルト値区分
	 * @param userId ユーザーID
	 * @return レコードあればtrue
	 */
	protected boolean existUserDefaultValue(UserDefaultValueKbn kbn, String userId) {
		final String sql = "SELECT count(*) as cnt FROM user_default_value WHERE kbn = ? AND user_id = ?";
		GMap result = connection.find(sql, kbn.toString(), userId);
		return (Long)result.get("cnt") > 0 ? true : false;
	}
}
