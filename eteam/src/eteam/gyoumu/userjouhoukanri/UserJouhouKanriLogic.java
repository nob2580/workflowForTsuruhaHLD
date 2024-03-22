package eteam.gyoumu.userjouhoukanri;

import java.sql.Date;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamConst.PasswordValidTerm;
import eteam.common.EteamConst.UserId;

/**
 * ユーザー情報管理機能Logic
 */
public class UserJouhouKanriLogic extends EteamAbstractLogic {

	/**
	 * ユーザー情報を登録する。
	 * @param user_id       ユーザーID
	 * @param shain_no      社員番号
	 * @param user_sei      ユーザー姓
	 * @param user_mei      ユーザー名
	 * @param mail_address  メールアドレス
	 * @param kigen_from    有効期限開始日
	 * @param kigen_to      有効期限終了日
	 * @param login_user_id ログインユーザーID
	 * @param isDairiKihyouFlag 代理起票フラグ
	 * @param isHoujinCardRiyouFlag 法人カード利用フラグ
	 * @param houjinCardShikibetsuyouNum 法人カード識別用番号
	 * @param securityPattern セキュリティパターン
	 * @param securityWfonlyFlg セキュリティワークフロー限定フラグ
	 * @param shouninRouteHenkouLevel 承認ルート変更権限レベル
	 * @param maruhiKengenFlag マル秘設定権限
	 * @param maruhiKaijyoFlag マル秘解除権限
	 * @param zaimuKyotenNyuryokuOnlyFlg 財務拠点のみ使用
	 * @return 結果
	 */
	public int insertUserInfo(String user_id, String shain_no, String user_sei, String user_mei, String mail_address, Date kigen_from, Date kigen_to, String login_user_id, boolean isDairiKihyouFlag, boolean isHoujinCardRiyouFlag, String houjinCardShikibetsuyouNum, String securityPattern, String securityWfonlyFlg, String shouninRouteHenkouLevel, String maruhiKengenFlag, String maruhiKaijyoFlag, String zaimuKyotenNyuryokuOnlyFlg) {
		String DairiKihyouFlag = isDairiKihyouFlag ? "1" : "0";
		String houjinCardRiyouFlag = isHoujinCardRiyouFlag ? "1" : "0";
		final String sql = "INSERT INTO user_info VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, NULL, 0, NULL, '0', NULL, '0', NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return connection.update(sql, user_id, shain_no, user_sei, user_mei, mail_address, kigen_from, kigen_to, login_user_id, login_user_id, DairiKihyouFlag, houjinCardRiyouFlag, houjinCardShikibetsuyouNum, securityPattern, securityWfonlyFlg, shouninRouteHenkouLevel, maruhiKengenFlag, maruhiKaijyoFlag, zaimuKyotenNyuryokuOnlyFlg);
	}
	
	/**
	 * パスワードを登録する
	 * @param user_id   ユーザーID
	 * @param password  パスワード
	 * @return 結果
	 */
	public int insertPassword(String user_id, String password) {
		return connection.update("INSERT INTO password VALUES (?, public.pgp_sym_encrypt(cast(? as text), cast('eteam' as text)) )", user_id, password);
	}
	
	/**
	 * 所属部門割り当てを登録する
	 * @param bumon_cd       部門コード
	 * @param bumon_role_id  部門ロールID
	 * @param user_id        ユーザーID
	 * @param daihyou_futan_bumon_cd 代表負担部門コード
	 * @param kigen_from     有効期限開始日
	 * @param kigen_to       有効期限終了日
	 * @param hyouji_jun     表示順
	 * @param login_user_id  ログインユーザーID
	 * @return 結果
	 */
	public int insertShozokuBumonWariate(String bumon_cd, String bumon_role_id, String user_id, String daihyou_futan_bumon_cd, Date kigen_from, Date kigen_to, int hyouji_jun, String login_user_id) {
		final String sql = "INSERT INTO shozoku_bumon_wariate VALUES(?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, bumon_cd, bumon_role_id, user_id, daihyou_futan_bumon_cd, kigen_from, kigen_to, hyouji_jun, login_user_id, login_user_id);
	}
	
	/**
	 * 業務ロール割り当てを登録する
	 * @param user_id         ユーザーID
	 * @param gyoumu_role_id  業務ロールID
	 * @param kigen_from      有効期限開始日
	 * @param kigen_to        有効期限終了日
	 * @param bumon_cd        処理部門コード
	 * @param login_user_id   ログインユーザーID
	 * @return 結果
	 */
	public int insertGyoumuRoleWariate(String user_id, String gyoumu_role_id, Date kigen_from, Date kigen_to, String bumon_cd, String login_user_id) {
		final String sql = "INSERT INTO gyoumu_role_wariate VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, user_id, gyoumu_role_id, kigen_from, kigen_to, bumon_cd, login_user_id, login_user_id);
	}
	
	/**
	 * ユーザー情報を更新する。
	 * @param user_id       ユーザーID
	 * @param mail_address  メールアドレス
	 * @param login_user_id ログインユーザーID
	 * @return 結果
	 */
	public int updataUserInfoMail(String user_id, String mail_address, String login_user_id) {
		final String sql = "UPDATE user_info "
						+  "SET    mail_address = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  user_id = ?";
		return connection.update(sql, mail_address, login_user_id, user_id);
	}
	/**
	 * ユーザー情報を更新する。
	 * @param user_id       ユーザーID
	 * @param shain_no      社員番号
	 * @param user_sei      ユーザー姓
	 * @param user_mei      ユーザー名
	 * @param login_user_id ログインユーザーID
	 * @return 結果
	 */
	public int updataUserInfoNoMei(String user_id, String shain_no, String user_sei, String user_mei, String login_user_id) {
		final String sql = "UPDATE user_info "
						+  "SET    user_sei = ?, shain_no = ?, user_mei = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  user_id = ?";
		return connection.update(sql, user_sei, shain_no, user_mei, login_user_id, user_id);
	}
	/**
	 * ユーザー情報を更新する。
	 * @param user_id       ユーザーID
	 * @param kigen_from    有効期限開始日
	 * @param kigen_to      有効期限終了日
	 * @param login_user_id ログインユーザーID
	 * @return 結果
	 */
	public int updataUserInfoKigen(String user_id, Date kigen_from, Date kigen_to, String login_user_id) {
		final String sql = "UPDATE user_info "
						+  "SET    yuukou_kigen_from = ?, yuukou_kigen_to = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  user_id = ?";
		return connection.update(sql, kigen_from, kigen_to, login_user_id, user_id);
	}

	/**
	 * パスワードを更新し、有効期限を更新します。
	 * パスワード誤り回数もリセットします。
	 * @param login_user_id ログインユーザーID
	 * @param user_id   ユーザーID
	 * @param password  パスワード
	 * @param type 有効期限種別。0:無期限、1:通常、2:失効
	 * @return 結果
	 */
	public int updataPassword(String login_user_id, String user_id, String password, int type) {
		resetPasswordFailureCount(user_id, false);

		// 無期限更新
		String sql = null;
		switch (type) {
		case PasswordValidTerm.INFI:
			sql = ""
				+ "UPDATE user_info "
				+ "    SET    pass_koushin_date = '9999-12-31', koushin_user_id = ?, koushin_time = current_timestamp "
				+ "    WHERE  user_id = ?";
			break;
		case PasswordValidTerm.NORMAL:
			sql = ""
				+ "UPDATE user_info "
				+ "    SET    pass_koushin_date = current_date, koushin_user_id = ?, koushin_time = current_timestamp "
				+ "    WHERE  user_id = ?";
			break;
		case PasswordValidTerm.NONE:
		default:
			sql = ""
				+ "UPDATE user_info "
				+ "    SET    pass_koushin_date = NULL, koushin_user_id = ?, koushin_time = current_timestamp "
				+ "    WHERE  user_id = ?";
			break;
		}
		connection.update(sql, login_user_id, user_id);

		return connection.update("UPDATE password SET password = public.pgp_sym_encrypt(cast(? as text), cast('eteam' as text)) WHERE user_id = ?", password, user_id);
	}

	/**
	 * ユーザーのパスワードが有効期限内であるかどうかを確認します。
	 * @param userId ユーザーID
	 * @return 有効期間内の場合true
	 */
	public boolean isValidPasswordPeriod(String userId) {
		if (UserId.ADMIN.equals(userId)) {
			// Adminユーザーは有効期限制御なし
			return true;
		}

		int passwordValidTerm = Integer.parseInt(setting.passValidTerm());
		if (0 >= passwordValidTerm) {
			final String sql =
					"SELECT "
				+ "  user_info.user_id, "
				+ "  user_info.pass_koushin_date "
				+ "FROM user_info "
				+ "WHERE "
				+ "  user_info.user_id = ? "
				+ "  AND current_date BETWEEN user_info.yuukou_kigen_from AND user_info.yuukou_kigen_to "
				+ "  AND user_info.pass_koushin_date is NOT NULL";
			GMap record = connection.find(sql, userId);
			return null != record;
		}
		final String sql =
				"SELECT "
			+ "  user_info.user_id, "
			+ "  user_info.pass_koushin_date "
			+ "FROM user_info "
			+ "WHERE "
			+ "  user_info.user_id = ? "
			+ "  AND current_date BETWEEN user_info.yuukou_kigen_from AND user_info.yuukou_kigen_to "
			+ "  AND user_info.pass_koushin_date is NOT NULL"
			+ "  AND user_info.pass_koushin_date + cast(? as integer) >= current_date ";
		GMap record = connection.find(sql, userId, passwordValidTerm - 1);
		return null != record;
	}

	/**
	 * ユーザー情報を削除する。
	 * @param user_id       ユーザーID
	 * @return 結果
	 */
	public int deleteUserInfo(String user_id) {
		return connection.update("DELETE FROM user_info WHERE user_id = ?", user_id);
	}
	
	/**
	 * パスワードを削除する
	 * @param user_id   ユーザーID
	 * @return 結果
	 */
	public int deletePassword(String user_id) {
		return connection.update("DELETE FROM password WHERE user_id = ?", user_id);
	}
	
	/**
	 * 所属部門割り当てを削除する
	 * @param user_id        ユーザーID
	 * @return 結果
	 */
	public int deleteShozokuBumonWariate(String user_id) {
		return connection.update("DELETE FROM shozoku_bumon_wariate WHERE user_id = ?", user_id);
	}

	/**
	 * 業務ロール割り当てを削除する
	 * @param user_id         ユーザーID
	 * @return 結果
	 */
	public int deleteGyoumuRoleWariate(String user_id) {
		return connection.update("DELETE FROM gyoumu_role_wariate WHERE user_id = ?", user_id);
	}

	/**
	 * ユーザーがログイン可能かどうかを判定します。
	 * @param user_id ユーザーID
	 * @param inputPassword パスワード
	 * @return 0:ログイン可能、1:パスワード誤り、2:アカウント一時ロック、3:アカウント永続ロック、4:存在しないアカウント
	 */
	public int checkUserStatusAndPassword(String user_id, String inputPassword) {
		if (null == user_id) {
			// アカウントなし
			return 4;
		}

		final String sql =
				"SELECT "
			+ "  user_info.user_id, "
			+ "  user_info.lock_flg, "
			+ "  user_info.tmp_lock_flg, "
			+ "  case "
			+ "    when user_info.tmp_lock_flg = '0' then "
			+ "      '0' "		// 一時ロックされていない
			+ "    when user_info.tmp_lock_flg = '1' and user_info.tmp_lock_time is null then "
			+ "      '1' "		// 一時ロックされているが期間あけ
			+ "    when user_info.tmp_lock_flg = '1' and current_timestamp >= user_info.tmp_lock_time + (interval '1 min' * cast(? as integer)) then "
			+ "      '1' "		// 一時ロックされているが期間あけ
			+ "    else "
			+ "      '2' "		// 一時ロック中
			+ "  end as  tmpLockState, "
			+ "  public.pgp_sym_decrypt(cast(password.password as bytea), cast('eteam' as text)) as password "
			+ "FROM user_info "
			+ "INNER JOIN password on "
			+ "  password.user_id = user_info.user_id "
			+ "WHERE "
			+ "  user_info.user_id = ? "
			+ "  ";
		int tmpLockTerm = Integer.parseInt(setting.passLockTerm());
		
		GMap record = connection.find(sql, tmpLockTerm, user_id);
		if (null == record) {
			// アカウントなし
			return 4;
		}

		String lockFlg = (String)record.get("lock_flg");
		String tmpLockState = (String)record.get("tmpLockState");
		String password = (String)record.get("password");

		// アカウント永続ロック判定
		if ("1".equals(lockFlg)) {
			return 3;
		}

		// アカウント一時ロック判定
		if (!"0".equals(tmpLockState)) {
			if (0 >= tmpLockTerm) {
				return 2;
			}
			if ("2".equals(tmpLockState)) {
				return 2;
			}
		}

		// パスワード判定
		if (!StringUtils.equals(password, inputPassword)) {
			return 1;
		}
		return 0;
	}

	/**
	 * 
	 * @param loginUserId ログインユーザーId
	 * @param userId ユーザーID
	 * @param securityPattern セキュリティパターン
	 * @param wfonly ワークフローのみ
	 * @return 結果
	 */
	public int updateSecurityPatterm(String loginUserId, String userId, String securityPattern, boolean wfonly) {
		String wfonlyFlg = wfonly ? "1" : "0";
		return connection.update("UPDATE user_info SET security_pattern = ?, security_wfonly_flg = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", securityPattern, wfonlyFlg, loginUserId, userId);
	}

	/**
	 * ユーザーアカウントのロック状態を変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param isLock ロック状態
	 * @return 結果
	 */
	public int updateUserLockFlag(String loginUserId, String user_id, boolean isLock) {
		String lockFlg = isLock ? "1" : "0";
		return connection.update("UPDATE user_info SET lock_flg = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", lockFlg, loginUserId, user_id);
	}
	
	/**
	 * 代理起票可能フラグの状態を変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param isDairiKihyouFlag 代理起票可能フラグ
	 * @return 結果
	 */
	public int updateDairiKihyouFlag(String loginUserId, String user_id, boolean isDairiKihyouFlag) {
		String DairiKihyouFlag = isDairiKihyouFlag ? "1" : "0";
		return connection.update("UPDATE user_info SET dairikihyou_flg = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", DairiKihyouFlag, loginUserId, user_id);
	}
	
	/**
	 * 法人カード利用フラグの状態を変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param isHoujinCardRiyouFlag 法人カード利用フラグ
	 * @return 結果
	 */
	public int updateHoujinCardRiyouFlag(String loginUserId, String user_id, boolean isHoujinCardRiyouFlag) {
		String houjinCardRiyouFlag = isHoujinCardRiyouFlag ? "1" : "0";
		return connection.update("UPDATE user_info SET houjin_card_riyou_flag = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", houjinCardRiyouFlag, loginUserId, user_id);
	}
	
	/**
	 * 法人カード識別用番号を変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param houjinCardShikibetsuyouNum 法人カード識別用番号
	 * @return 結果
	 */
	public int updateHoujinCardShikibetsuyouNum(String loginUserId, String user_id, String houjinCardShikibetsuyouNum) {
		return connection.update("UPDATE user_info SET houjin_card_shikibetsuyou_num = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", houjinCardShikibetsuyouNum, loginUserId, user_id);
	}
	
	/**
	 * 承認ルート変更権限レベルを変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param shouninRouteHenkouLevel 承認ルート変更権限レベル
	 * @return 結果
	 */
	public int updateShouninRouteHenkouLevel(String loginUserId, String user_id, String shouninRouteHenkouLevel) {
		return connection.update("UPDATE user_info SET shounin_route_henkou_level = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", shouninRouteHenkouLevel, loginUserId, user_id);
	}
	
	/**
	 * マル秘設定権限を変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param isMaruhiKengenFlag マル秘設定権限
	 * @return 結果
	 */
	public int updateMaruhiKengenFlag(String loginUserId, String user_id, boolean isMaruhiKengenFlag) {
		String maruhiKengenFlag = isMaruhiKengenFlag ? "1" : "0";
		return connection.update("UPDATE user_info SET maruhi_kengen_flg = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", maruhiKengenFlag, loginUserId, user_id);
	}
	
	/**
	 * マル秘解除権限を変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param isMaruhiKaijyoFlag マル秘解除権限
	 * @return 結果
	 */
	public int updateMaruhiKaijyoFlag(String loginUserId, String user_id, boolean isMaruhiKaijyoFlag) {
		String maruhiKaijyoFlag = isMaruhiKaijyoFlag ? "1" : "0";
		return connection.update("UPDATE user_info SET maruhi_kaijyo_flg = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", maruhiKaijyoFlag, loginUserId, user_id);
	}
	
	/**
	 * 財務入力のみ使用かそうでないかを変更します。
	 * @param loginUserId ログインユーザーId
	 * @param user_id ユーザーID
	 * @param iszaimuKyotenNyuryokuOnlyFlg 財務入力のみ使用フラグ
	 * @return 結果
	 */
	public int updatenewzaimuKyotenNyuryokuOnlyFlg(String loginUserId, String user_id, boolean iszaimuKyotenNyuryokuOnlyFlg) {
		String zaimuKyotenNyuryokuOnlyFlg = iszaimuKyotenNyuryokuOnlyFlg ? "1" : "0";
		return connection.update("UPDATE user_info SET zaimu_kyoten_nyuryoku_only_flg = ?, lock_time = current_timestamp, koushin_user_id = ?, koushin_time = current_timestamp WHERE user_id = ?", zaimuKyotenNyuryokuOnlyFlg, loginUserId, user_id);
	}
	
	/**
	 * パスワード誤り回数を増やします。
	 * @param user_id ユーザーID
	 * @return 結果(0:ロック機能無効or対象なし, 1:インクリメントのみ, 2:一時ロックされた)
	 */
	public int incrementPasswordFailureCount(String user_id) {
		int failureCount = Integer.parseInt(setting.passLockNum());
		int failureResetTime = Integer.parseInt(setting.passLockTerm());

		if (0 >= failureCount) {
			// ロックしない設定
			return 0;
		}

		// パスワード誤り回数を時間に応じて設定。
		if (0 < failureResetTime) {
			final String updateSql = 
					  "UPDATE "
					+ "  user_info u "
					+ "SET "
					+ "  pass_failure_time = current_timestamp, "
					+ "  pass_failure_count = lu.logicalFailureCount + 1 "
					+ "FROM "
					+ "  ("
					+ "    SELECT "
					+ "      user_id, "
					+ "      case "
					+ "        when u2.pass_failure_time is NULL then "		// 基本あり得ないはずだけど、念のためチェック。
					+ "          0 "
					+ "        when current_timestamp >= u2.pass_failure_time + (interval '1 min' * cast(? as integer)) then "
					+ "          0 "
					+ "        else "
					+ "          u2.pass_failure_count "
					+ "      end as logicalFailureCount "
					+ "    FROM "
					+ "      user_info u2 "
					+ "  ) lu "
					+ "WHERE "
					+ "  lu.user_id = u.user_id and "
					+ "  u.user_id = ? "
			;
			connection.update(updateSql, failureResetTime, user_id);
		} else {
			final String updateSql = 
					  "UPDATE "
					+ "  user_info u "
					+ "SET "
					+ "  pass_failure_time = current_timestamp, "
					+ "  pass_failure_count = pass_failure_count + 1 "
					+ "WHERE "
					+ "  u.user_id = ? "
			;
			connection.update(updateSql, user_id);
		}

		// 規定数以上の誤りは一時ロックする。
		final String lockSql = 
				  "UPDATE "
				+ "  user_info "
				+ "SET "
				+ "  pass_failure_time = NULL, "
				+ "  pass_failure_count = 0, "
				+ "  tmp_lock_flg = '1', "
				+ "  tmp_lock_time = current_timestamp "
				+ "WHERE "
				+ "  user_id = ? and "
				+ "  pass_failure_count >= ? "
		;
		if (0 < connection.update(lockSql, user_id, failureCount)) {
			return 2;
		}
		return 1;
	}

	/**
	 * パスワード誤り回数をリセットします。
	 * @param user_id ユーザーID
	 * @param isUnlockTmp 一時ロックを解除するかどうか
	 * @return 結果
	 */
	public int resetPasswordFailureCount(String user_id, boolean isUnlockTmp) {
		if (isUnlockTmp) {
			return connection.update("UPDATE user_info SET tmp_lock_flg = '0', tmp_lock_time = current_timestamp, pass_failure_count = 0, pass_failure_time = NULL WHERE user_id = ?", user_id);
		}
		return connection.update("UPDATE user_info SET pass_failure_count = 0, pass_failure_time = NULL WHERE user_id = ?", user_id);
	}
}
