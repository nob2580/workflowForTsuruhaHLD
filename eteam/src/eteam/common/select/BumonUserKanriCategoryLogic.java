package eteam.common.select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.EteamConst.UserId;
import eteam.gyoumu.user.User;

/**
 * 部門・ユーザー管理カテゴリー内のSelect文を集約したLogic
 */
public class BumonUserKanriCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* ユーザー情報(user_info) */

	/**
	 * ユーザーIDをキーに「ユーザー情報」「パスワード」からデータを取得する。データがなければnull。
	 * @param user_id String ユーザーID
	 * @return 検索結果 1件データ
	 */
	public GMap selectUserJouhou(String user_id){
		int passwordValidTerm = Integer.parseInt(setting.passValidTerm());
		int tmpLockTerm = Integer.parseInt(setting.passLockTerm());
		final String sql = "SELECT u.user_id, u.shain_no, u.user_sei, u.user_mei, u.yuukou_kigen_from, u.yuukou_kigen_to, u.mail_address, u.security_pattern, u.security_wfonly_flg, public.pgp_sym_decrypt(cast(p.password as bytea), cast('eteam' as text)) as password, u.lock_flg, u.dairikihyou_flg, u.houjin_card_riyou_flag, u.houjin_card_shikibetsuyou_num, u.shounin_route_henkou_level, u.maruhi_kengen_flg, u.maruhi_kaijyo_flg, "
				+  "  case when pass_koushin_date is not NULL and pass_koushin_date + cast(? as integer) >= current_date then pass_koushin_date + cast(? as integer) end as pass_koushin_date, "
				+  "  case when pass_koushin_date is not NULL then pass_koushin_date + cast(? as integer) end as pass_koushin_date2, "
				+  "  case "
				+  "    when 0 < ? and u.tmp_lock_flg = '1' and u.tmp_lock_time is null then "
				+  "      '0' "
				+  "    when 0 < ? and u.tmp_lock_flg = '1' and current_timestamp >= u.tmp_lock_time + (interval '1 min' * cast(? as integer)) then "
				+  "      '0' "
				+  "    else "
				+  "      u.tmp_lock_flg "
				+  "  end as tmp_lock_flg, "
				+  "  u.zaimu_kyoten_nyuryoku_only_flg "
				+  "FROM   user_info u, password p "
				+  "WHERE  u.user_id = p.user_id AND u.user_id = ?;";
		return connection.find(sql, Integer.valueOf(passwordValidTerm-1), Integer.valueOf(passwordValidTerm-1), Integer.valueOf(passwordValidTerm-1), Integer.valueOf(tmpLockTerm), Integer.valueOf(tmpLockTerm), Integer.valueOf(tmpLockTerm), user_id);
	}

	/**
	 * ユーザーIDをキーに「ユーザー情報」を取得する。データがなければnull。
	 * @param user_id ユーザーID
	 * @return 検索結果 1件データ
	 */
	public GMap selectUserInfo(String user_id){
		final String sql = "SELECT user_id, shain_no, user_sei, user_mei, mail_address, yuukou_kigen_from, yuukou_kigen_to, houjin_card_riyou_flag, houjin_card_shikibetsuyou_num, shounin_route_henkou_level, maruhi_kengen_flg, zaimu_kyoten_nyuryoku_only_flg FROM user_info WHERE user_id = ?";
		return connection.find(sql, user_id);
	}


	/**
	 * 指定ユーザーのセキュリティパターンを取得
	 * @param  userId ユーザーID
	 * @return 検索結果	セキュリティパターン、未設定ならnull
	 */
	public Integer findSecurityPatternFromUserId(String userId){
		GMap record = selectUserJouhou(userId);
		if (record == null)
		{
			return null;
		}
		String secPtn = (String)record.get("security_pattern");
		return (StringUtils.isEmpty(secPtn)) ? null : Integer.parseInt(secPtn);
	}

	/**
	 * 指定ユーザーが利用できるセキュリティパターンリストを取得
	 * @param userId ユーザーID
	 * @return 検索結果	セキュリティパターンリスト
	 */
	public List<Integer> selectSecurityPatternListUser(String userId){
		List<GMap> shozokuBumonList = selectValidShozokuBumonRole(userId);
		List<Integer> spList = new ArrayList<Integer>();
		for(GMap map : shozokuBumonList){

			//各部門のセキュリティパターン取得
			String bumonCd = (String)map.get("bumon_cd");
			Integer secPtn = findSecurityPatternBumon(bumonCd);
			if (secPtn != null){
				spList.add(secPtn);
			}
		}
		return spList;
	}

	/**
	 * システム日付時点での指定部門のセキュリティパターンを取得
	 * @param  bumonCd 部門コード
	 * @return 検索結果	セキュリティパターン、未設定ならnull
	 */
	public Integer findSecurityPatternBumon(String bumonCd){
		return findSecurityPatternBumon(bumonCd, new Date(System.currentTimeMillis()));
	}

	/**
	 * 指定基準日時点の指定部門のセキュリティパターンを取得
	 * @param  bumonCd 部門コード
	 * @param  kijunBi 基準日
	 * @return 検索結果	セキュリティパターン、未設定ならnull
	 */
	public Integer findSecurityPatternBumon(String bumonCd,Date kijunBi){
		GMap record = selectValidShozokuBumon(bumonCd, kijunBi);
		if (record == null)
		{
			return null;
		}
		String secPtn = (String)record.get("security_pattern");
		return (StringUtils.isEmpty(secPtn)) ? null : Integer.parseInt(secPtn);
	}


	/**
	 * 指定ユーザがのセキュリティパターンを取得
	 * @param  userId 対象ユーザーID(操作者ID、または伝票起票者ID)
	 * @param  kihyouBumonCd 起票部門コード
	 * @return 検索結果			セキュリティパターン、未設定ならnull
	 */
	public List<Integer> selectSecurityPatternList(String userId, String kihyouBumonCd) {

		Integer sptn = null;
		List<Integer> spList = new ArrayList<Integer>();

		//会社設定によりユーザと所属部門どちらのセキュリティパターンを取得するか分岐
		//TODO 伝票起票前後で該当設定が変更されている場合のパターンは考慮しなくてよい？
		String setStr = setting.keihimeisaiSecurityPsettei();
		if("1".equals(setStr)){
			//ユーザのセキュリティパターン取得
			sptn = this.findSecurityPatternFromUserId(userId);
			if(sptn != null) spList.add(sptn);
		}else if("2".equals(setStr)){
			//起票部門コードが指定されている(＝伝票画面からの呼び出し)か
			if(kihyouBumonCd == null || kihyouBumonCd.isEmpty()){
				//ユーザー所属部門の全セキュリティパターン取得
				spList = this.selectSecurityPatternListUser(userId);
			}else{
				//部門のセキュリティパターン取得
				sptn = this.findSecurityPatternBumon(kihyouBumonCd);
				if(sptn != null) spList.add(sptn);
			}
		}
		return spList;
	}


	/**
	 * ユーザーIDをキーに「代表負担部門コード」と「代表負担部門名」を取得する。データがなければnull。
	 * @param user_id ユーザーID
	 * @return 検索結果1件データ
	 */
	public GMap shokiDaihyouFutanBumon(String user_id){
		final String sql  = "SELECT sh.daihyou_futan_bumon_cd, bm.futan_bumon_name "
							+ "FROM user_info ui, shain sh, bumon_master bm "
							+ "WHERE ui.shain_no = sh.shain_no AND sh.daihyou_futan_bumon_cd = bm.futan_bumon_cd AND  ui.user_id = ?;";
		return connection.find(sql, user_id);
	}

	/**
	 * 社員番号をキーに「代表負担部門コード」と「代表負担部門名」を取得する。データがなければnull。
	 * @param shainNo 社員番号
	 * @return 検索結果1件データ
	 */
	public GMap findShainDaihyouFutanBumonName(String shainNo){
		final String sql  = "SELECT sh.daihyou_futan_bumon_cd, bm.futan_bumon_name "
						+ "FROM shain sh, bumon_master bm "
						+ "WHERE sh.daihyou_futan_bumon_cd = bm.futan_bumon_cd AND  sh.shain_no = ?;";
		return connection.find(sql, shainNo);
	}

	/**
	 * 社員番号をキーに「ユーザー情報」を取得する。データがなければnull。
	 * @param shain_no 社員番号
	 * @return 検索結果 1件データ
	 */
	public GMap selectShainNo(String shain_no){
		final String sql = "SELECT user_id, shain_no, user_sei, user_mei, houjin_card_riyou_flag, houjin_card_shikibetsuyou_num, shounin_route_henkou_level, maruhi_kengen_flg, yuukou_kigen_from, yuukou_kigen_to FROM user_info WHERE shain_no = ?";
		return connection.find(sql, shain_no);
	}

	/**
	 * メールアドレスをキーに「ユーザー情報」を取得する。データがなければnull。
	 * @param mail_address メールアドレス
	 * @return 検索結果 1件データ
	 */
	public GMap selectMailAddress(String mail_address){
		final String sql = "SELECT user_id, mail_address, yuukou_kigen_from, yuukou_kigen_to FROM user_info WHERE mail_address = ?";
		return connection.find(sql, mail_address);
	}

	/**
	 * ユーザーIDをキーに「ユーザー情報」を取得する。データがなければnull。
	 * @param user_id ユーザーID
	 * @return 検索結果 1件データ
	 */
	public GMap selectValidUserInfo(String user_id){
		final String sql =
				"SELECT user_info.*, shain.yakushoku_cd "
			+ "FROM user_info "
			+ "LEFT OUTER JOIN shain ON "
			+ "  user_info.shain_no = shain.shain_no "
			+ "WHERE "
			+ "  user_id = ? "
			+ "  AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to ";
		return connection.find(sql, user_id);
	}

	/**
	 * ID(メールアドレス)が一致するレコードがあるか調べる。
	 * @param userIdOrMailAddress ID
	 * @return 存在すれば該当ユーザーのuser_id
	 */
	public String selectUserForIdOrAddress(String userIdOrMailAddress){
		final String sql =
				"SELECT "
			+ "  user_info.user_id "
			+ "FROM user_info "
			+ "WHERE "
			+ "  (user_info.user_id = ? or user_info.mail_address = ?)"
			+ "  AND current_date BETWEEN user_info.yuukou_kigen_from AND user_info.yuukou_kigen_to ";
		GMap record = connection.find(sql, userIdOrMailAddress, userIdOrMailAddress);
		if (null == record) {
			return null;
		}
		return (String)record.get("user_id");
	}

	/**
	 * ユーザー検索
	 * ユーザー名、部門コード、部門ロールID（役割）、業務ロールIDをキーに「ユーザー情報」「所属部門割り当て」「所属部門」「部門ロール」「業務ロール割り当て」「業務ロール」を結合し検索を行う。
	 * ※検索結果は所属別に取得されるので、ユーザーは重複する。
	 * @param user_name String ユーザー名
	 * @param bumon_cd String 部門コード
	 * @param bumon_role_id String 部門ロールID
	 * @param gyoumu_role_id String 業務ロールID
	 * @param isYuukoukigennai 有効期限内のデータに絞り込むフラグ：無効になったユーザーや所属も出す場合はfalse、有効なもののみならばtrue。
	 * @return 検索結果 リスト
	 */
	public List<GMap> userSerach(String user_name, String bumon_cd, String bumon_role_id, String gyoumu_role_id, boolean isYuukoukigennai) {
		return userSerach(user_name, "", "", bumon_cd, bumon_role_id, gyoumu_role_id, isYuukoukigennai, false, false, false);
	}

	/**
	 * ユーザー検索
	 * ユーザー名、部門コード、部門ロールID（役割）、業務ロールIDをキーに「ユーザー情報」「所属部門割り当て」「所属部門」「部門ロール」「業務ロール割り当て」「業務ロール」を結合し検索を行う。
	 * ※検索結果は所属別に取得されるので、ユーザーは重複する。
	 * @param user_name String ユーザー名
	 * @param user_id String ユーザーID
	 * @param shain_no String 社員番号
	 * @param bumon_cd String 部門コード
	 * @param bumon_role_id String 部門ロールID
	 * @param gyoumu_role_id String 業務ロールID
	 * @param isYuukoukigennai 有効期限内のデータに絞り込むフラグ：無効になったユーザーや所属も出す場合はfalse、有効なもののみならばtrue。
	 * @param isAccountTmpLock 一時アカウントロックされたもののみを検索するかどうか。falseの場合すべて。
	 * @param isAccountLock 永続アカウントロックされたもののみを検索するかどうか。falseの場合すべて。
	 * @param isDairiKihyouFlg 代理起票可能のもののみを検索するかどうか。falseの場合すべて。
	 * @return 検索結果 リスト
	 */
	public List<GMap> userSerach(String user_name, String user_id, String shain_no, String bumon_cd, String bumon_role_id, String gyoumu_role_id, boolean isYuukoukigennai, boolean isAccountTmpLock, boolean isAccountLock, boolean isDairiKihyouFlg) {
		Date kijunDate = null;
		if(isYuukoukigennai == true){
			kijunDate = new Date(System.currentTimeMillis());
		}
		return userSerach(user_name, user_id, shain_no, bumon_cd, bumon_role_id, gyoumu_role_id, kijunDate, isAccountTmpLock, isAccountLock, isDairiKihyouFlg, "");
	}

	/**
	 * ユーザー検索
	 * ユーザー名、部門コード、部門ロールID（役割）、業務ロールIDをキーに「ユーザー情報」「所属部門割り当て」「所属部門」「部門ロール」「業務ロール割り当て」「業務ロール」を結合し検索を行う。
	 * ※検索結果は所属別に取得されるので、ユーザーは重複する。
	 * @param user_name String ユーザー名
	 * @param user_id String ユーザーID
	 * @param shain_no String 社員番号
	 * @param bumon_cd String 部門コード
	 * @param bumon_role_id String 部門ロールID
	 * @param gyoumu_role_id String 業務ロールID
	 * @param kijunDate 基準日
	 * @param isAccountTmpLock 一時アカウントロックされたもののみを検索するかどうか。falseの場合すべて。
	 * @param isAccountLock 永続アカウントロックされたもののみを検索するかどうか。falseの場合すべて。
	 * @param isDairiKihyouFlg 代理起票可能のもののみを検索するかどうか。falseの場合すべて。
	 * @param houjinCardShikibetsuNum 法人カード識別用番号
	 * @return 検索結果 リスト
	 */
	public List<GMap> userSerach(String user_name, String user_id, String shain_no, String bumon_cd, String bumon_role_id, String gyoumu_role_id, Date kijunDate, boolean isAccountTmpLock, boolean isAccountLock, boolean isDairiKihyouFlg, String houjinCardShikibetsuNum) {
		final StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT ");
		sql.append("DISTINCT ");
		sql.append(    "ui.user_id, ");            /* ユーザーID */
		sql.append(    "ui.shain_no, ");           /* 社員番号 */
		sql.append(    "ui.user_sei, ");           /* ユーザー姓 */
		sql.append(    "ui.user_mei, ");           /* ユーザー名 */
		sql.append(    "ui.yuukou_kigen_from, ");  /* 有効期限開始日(ユーザー情報) */
		sql.append(    "ui.yuukou_kigen_to, ");    /* 有効期限終了日(ユーザー情報) */
		sql.append(    "sbw.bumon_cd, ");          /* 部門コード */
		sql.append(    "sb.bumon_name, ");         /* 部門名 */
		sql.append(    "sbw.bumon_role_id, ");     /* 部門ロールID */
		sql.append(    "br.bumon_role_name, ");    /* 部門ロール名 */
		sql.append(    "br.hyouji_jun, " );        /* 表示順 */
		sql.append(    "ui.touroku_time, ");       /* 登録日時(ユーザー情報) */
		sql.append(    "sbw.yuukou_kigen_from AS bumon_wariate_kigen_from, "); /* 有効期限開始日(所属部門割り当て) */
		sql.append(    "sbw.yuukou_kigen_to   AS bumon_wariate_kigen_to,  ");  /* 有効期限終了日(所属部門割り当て) */
		sql.append(    "case ");
		sql.append(    "  when 0 < ? and ui.tmp_lock_flg = '1' and ui.tmp_lock_time is null then ");
		sql.append(    "    '0' ");
		sql.append(    "  when 0 < ? and ui.tmp_lock_flg = '1' and current_timestamp >= ui.tmp_lock_time + (interval '1 min' * cast(? as integer)) then ");
		sql.append(    "    '0' ");
		sql.append(    "  else ");
		sql.append(    "    ui.tmp_lock_flg ");
		sql.append(    "end as tmp_lock_flg, ");   /* アカウント一時ロックフラグ */
		sql.append(    "ui.tmp_lock_time, ");      /* アカウント一時ロック日時 */
		sql.append(    "ui.lock_flg, ");           /* アカウント永続ロックフラグ */
		sql.append(    "ui.lock_time, ");          /* アカウント永続ロック日時 */
		sql.append(    "ui.dairikihyou_flg, ");     /* 代理起票可能フラグ */
		sql.append(    "ui.houjin_card_riyou_flag, ");
		sql.append(    "ui.houjin_card_shikibetsuyou_num ");/* 法人カード識別用番号 */
		sql.append("FROM ");
					/* ユーザー情報と所属部門割り当てをユーザーIDで結合 */
					/* 所属部門割り当てと所属部門を部門コードで結合 */
					/* 所属部門割り当てと部門ロールを部門ロールIDで結合 */
					/* ユーザー情報と業務ロール割り当てをユーザーIDで結合 */
					/* 業務ロール割り当てと業務ロールを業務ロールIDで結合 */
		sql.append(    "user_info ui LEFT OUTER JOIN shozoku_bumon_wariate sbw ON ui.user_id = sbw.user_id ");
		sql.append(                 "LEFT OUTER JOIN shozoku_bumon sb ON sbw.bumon_cd = sb.bumon_cd ");
		sql.append("                                 AND ? BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to ");
		sql.append(                 "LEFT OUTER JOIN bumon_role br    ON sbw.bumon_role_id = br.bumon_role_id ");
		sql.append(                 "LEFT OUTER JOIN gyoumu_role_wariate grw ON ui.user_id = grw.user_id ");
		sql.append(                 "LEFT OUTER JOIN gyoumu_role gr   ON grw.gyoumu_role_id = gr.gyoumu_role_id ");

		// 表示用のパラメータ追加
		int tmpLockTerm = Integer.parseInt(setting.passLockTerm());
		params.add(Integer.valueOf(tmpLockTerm));
		params.add(Integer.valueOf(tmpLockTerm));
		params.add(Integer.valueOf(tmpLockTerm));

		// 取得データとして基準日(無ければ現在日)を指定
		Date tmpDt = kijunDate != null ? kijunDate : new Date(System.currentTimeMillis());
		params.add(tmpDt);

		// Adminは検索結果に出力しないようにする。
		sql.append(" WHERE ui.user_id NOT IN('admin') ");

		// 有効期限内に絞り込む場合、ユーザー自体と所属の有効期限で絞り込む
		if (kijunDate != null) {
			sql.append(" AND ? BETWEEN ui.yuukou_kigen_from AND ui.yuukou_kigen_to ");
			sql.append(" AND ? BETWEEN sbw.yuukou_kigen_from AND sbw.yuukou_kigen_to ");
			params.add(kijunDate);
			params.add(kijunDate);
		}

		// 検索条件によってSQLを変更します。
		if (!user_name.isEmpty()) {
			// ユーザー姓、ユーザー名を連結しあいまい検索
			sql.append(" AND unify_kana_width(CONCAT(ui.user_sei, ui.user_mei)) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(user_name).append("%").toString());
		}
		if (!user_id.isEmpty()) {
			// ユーザーID
			sql.append(" AND ui.user_id LIKE ? ");
			params.add(new StringBuilder().append("%").append(user_id).append("%").toString());
		}
		if (!shain_no.isEmpty()) {
			// 社員番号
			sql.append(" AND ui.shain_no LIKE ? ");
			params.add(new StringBuilder().append("%").append(shain_no).append("%").toString());
		}
		if (!bumon_cd.isEmpty()) {
			//部門コード
			sql.append(" AND sbw.bumon_cd = ? ");
			params.add(bumon_cd);
		}
		if (!bumon_role_id.isEmpty()) {
			//部門ロールID
			sql.append(" AND br.bumon_role_id = ? ");
			params.add(bumon_role_id);
		}
		if (!gyoumu_role_id.isEmpty()) {
			//業務ロールID
			sql.append(" AND gr.gyoumu_role_id = ? ");
			params.add(gyoumu_role_id);
		}
		if (isAccountTmpLock) {
			sql.append(" AND ui.tmp_lock_flg = ? ");
			params.add("1");
			if (0 < tmpLockTerm) {
				sql.append(" AND ui.tmp_lock_time is not null ");
				sql.append(" AND current_timestamp < ui.tmp_lock_time + (interval '1 min' * cast(? as integer)) ");
				params.add(Integer.valueOf(tmpLockTerm));
			}
		}
		if (isAccountLock) {
			sql.append(" AND ui.lock_flg = ? ");
			params.add("1");
		}
		if (isDairiKihyouFlg) {
			sql.append(" AND ui.dairikihyou_flg = ? ");
			params.add("1");
		}

		if (!houjinCardShikibetsuNum.isEmpty()) {
			sql.append(" AND ui.houjin_card_riyou_flag = ? ");
			sql.append(" AND ui.houjin_card_shikibetsuyou_num = ? ");
			params.add("1");
			params.add(houjinCardShikibetsuNum);
		}

		// 所属部門.部門コード、部門ロール.表示順、ユーザー情報.ユーザー姓名でソートします。
		sql.append(" ORDER BY sbw.bumon_cd, br.hyouji_jun, ui.touroku_time ");

		return connection.load(sql.toString(), params.toArray());
	}


	/**
	 * 有効なユーザーのリスト取得
	 * @return 有効なユーザーのリスト
	 */
	public List<GMap> selectAllValidUser() {
		final String sql =
				"SELECT "
			+ "  user_info.user_id "
			+ "  ,user_info.shain_no "
			+ "  ,user_info.user_sei "
			+ "  ,user_info.user_mei "
			+ "FROM user_info "
			+ "WHERE "
			+ "  current_date BETWEEN user_info.yuukou_kigen_from AND user_info.yuukou_kigen_to "
			+ "  AND user_id <> ? "
			+ "ORDER BY "
			+ "  user_info.touroku_time ";
		return connection.load(sql, UserId.ADMIN);
	}

	/**
	 * 承認ユーザー検索
	 * ユーザー名、部門コードをキーに「ユーザー情報」「所属部門割り当て」「所属部門」「部門ロール」を結合し検索を行う。
	 * @param bumon_cd       部門コード
	 * @param bumon_role_id  部門ロールID
	 * @param user_id        ユーザーID
	 * @param date        ユーザーID
	 * @return 検索結果 リスト
	 */
	public List<GMap> shouninUserSerach(String bumon_cd, String bumon_role_id, String user_id, Date date) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT ui.user_id, ui.user_sei, ui.user_mei, sbw.bumon_cd, sb.bumon_name, sbw.bumon_role_id, br.bumon_role_name ");
		sql.append("FROM user_info ui LEFT OUTER JOIN shozoku_bumon_wariate sbw ON ui.user_id = sbw.user_id ");
		sql.append(                  "LEFT OUTER JOIN shozoku_bumon sb ON sbw.bumon_cd = sb.bumon_cd ");
		sql.append(                  "LEFT OUTER JOIN bumon_role br    ON sbw.bumon_role_id = br.bumon_role_id ");
		sql.append("WHERE ui.user_id <> ? AND sbw.bumon_cd = ? AND sbw.bumon_role_id = ? ");
		sql.append(" AND ? BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to ");
		sql.append(" AND ? BETWEEN sbw.yuukou_kigen_from AND sbw.yuukou_kigen_to ");
		sql.append(" ORDER BY sbw.yuukou_kigen_from DESC, sbw.touroku_time DESC");
		return connection.load(sql.toString(), user_id, bumon_cd, bumon_role_id, date, date);
	}

	/**
	 * 滞留設定がONになっているメールアドレスとユーザーIDと取得
	 * @return 検索結果 リスト
	 */
	public List<GMap> loadTairyuUser() {
		final String sql = "SELECT a.mail_address, a.user_id "
						 + "FROM user_info a "
						 + "INNER JOIN mail_tsuuchi b ON "
						 + "      a.user_id = b.user_id "
						 + "  AND b.tsuuchi_kbn = 'TTT' "
						 + "WHERE "
						 + "      a.mail_address != '' "
						 + "  AND current_date BETWEEN a.yuukou_kigen_from AND a.yuukou_kigen_to "
						 +   "AND b.soushinumu = '1' ";
		return connection.load(sql);
	}

	/**
	 * 登録済ユーザー数を取得する
	 * 「admin」以外のユーザー数とし、ユーザーの有効期限は問わない。
	 * @return 登録済ユーザー数
	 */
	public long findUserCount()  {
		final String sql = "SELECT count(*) AS count "
				+  "FROM   user_info "
				+  "WHERE  user_id <> 'admin'";
		GMap record = connection.find(sql);
		return (long)record.get("count");
	}

	/**
	 * 指定した法人カード使用履歴情報IDが持つカード番号と、ユーザーの法人カード識別用番号が一致するユーザーリストを取得
	 * @param houjinRirekiNo 法人カード使用履歴情報ID
	 * @return リスト(カード履歴に一致するデータが無い場合は空のリスト)
	 */
	public List<GMap> userSerachHoujinCardRireki(String houjinRirekiNo){

		final String sql =
				"SELECT "
			+ "  houjin_card_jouhou.card_shubetsu, "
			+ "  houjin_card_jouhou.shain_bangou, "
			+ "  houjin_card_jouhou.card_bangou "
			+ "FROM houjin_card_jouhou "
			+ "WHERE "
			+ "  houjin_card_jouhou.card_jouhou_id = ? ";
		GMap record = connection.find(sql, Integer.parseInt(houjinRirekiNo));
		if (null == record) {
			return new ArrayList<GMap>(); //空リスト返却
		}

		String houjinCardShikibetsuNum = "";
		//カード種別により識別用コード分岐
		switch((String)record.get("card_shubetsu")){
			case "1":houjinCardShikibetsuNum = (String)record.get("shain_bangou");break; //JCB
			case "2":houjinCardShikibetsuNum = (String)record.get("card_bangou");break; //VISA
			case "3":houjinCardShikibetsuNum = (String)record.get("card_bangou");break; //NICOS
			case "4":houjinCardShikibetsuNum = (String)record.get("shain_bangou");break; //SAISON
			default:return new ArrayList<GMap>(); //空リスト返却
		}

		return userSerach("", "", "", "", "", "", null, false, false, false, houjinCardShikibetsuNum);
	}

/* パスワード(password) */

/* 社員口座(shain_kouza) */

/* 所属部門(shozoku_bumon) */

	/**
	 * 部門コードをキーに期限Fromが最新の「所属部門」を取得する。データがなければnull。
	 * @param bumon_cd 部門コード
	 * @return 検索結果 1件データ
	 */
	public GMap findShozokuBumonLatest(String bumon_cd){
		final String sql = "SELECT bumon_cd, bumon_name, oya_bumon_cd, yuukou_kigen_from, yuukou_kigen_to "
						+  "FROM shozoku_bumon "
						+  "WHERE bumon_cd = ? "
						+  "ORDER BY yuukou_kigen_from DESC LIMIT 1 ";
		return connection.find(sql, bumon_cd);
	}

	/**
	 * 部門コードをキーに全期間の「所属部門」を取得する。
	 * @param bumon_cd 部門コード
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectShozokuBumonAll(String bumon_cd){
		final String sql = "SELECT bumon_cd, bumon_name, oya_bumon_cd, yuukou_kigen_from, yuukou_kigen_to, security_pattern FROM shozoku_bumon WHERE bumon_cd = ? ORDER BY yuukou_kigen_from";
		return connection.load(sql, bumon_cd);
	}

	/**
	 * 部門コードをキーに現在日で有効期限内の「所属部門」を取得する。データがなければnull。
	 * @param bumon_cd 部門コード
	 * @return 検索結果 1件データ
	 */
	public GMap selectValidShozokuBumon(String bumon_cd){
		return selectValidShozokuBumon(bumon_cd, new Date(System.currentTimeMillis()));
	}

	/**
	 * 部門コードをキーに指定基準日で有効期限内の「所属部門」を取得する。データがなければnull。
	 * @param bumon_cd 部門コード
	 * @param kijunBi  基準日
	 * @return 検索結果 1件データ
	 */
	public GMap selectValidShozokuBumon(String bumon_cd, Date kijunBi){
		final String sql = "SELECT bumon_cd, bumon_name, oya_bumon_cd, yuukou_kigen_from, yuukou_kigen_to, security_pattern "
						+  "FROM shozoku_bumon "
						+  "WHERE bumon_cd = ? AND ? BETWEEN yuukou_kigen_from AND yuukou_kigen_to "
						+  "ORDER BY yuukou_kigen_from DESC LIMIT 1 ";
		return connection.find(sql, bumon_cd, kijunBi);
	}

	/**
	 * 親部門コード配下の階層を取得する。SQLで再帰処理を行い階層ごとにデータを取得する。
	 * @param oya_bumon_cd 親部門コード（空「""」の場合はROOTから検索する。）
	 * @param isYuukoukigennai 有効期限内のデータに絞り込むフラグ
	 * @return 検索結果
	 */
	public List<GMap> selectBumonTreeStructure(String oya_bumon_cd, boolean isYuukoukigennai){
		final StringBuilder sql = new StringBuilder();
		sql.append("WITH RECURSIVE rec_bumon( " /* 一時テーブル */
				+      "level, "                /* 階層(TEMPデータ) */
				+      "bumon_cd, "             /* 部門コード */
				+      "bumon_name, "           /* 部門名 */
				+      "oya_bumon_cd, "         /* 親部門コード */
				+      "yuukou_kigen_from, "    /* 有効期限開始日 */
				+      "yuukou_kigen_to "       /* 有効期限終了日 */
				+  ") AS ("
				        /* 初回の検索条件は受け取った親部門コードをキーに検索します。*/
				+      "SELECT 1, bumon_cd, bumon_name, oya_bumon_cd, yuukou_kigen_from, yuukou_kigen_to "
				+      "FROM shozoku_bumon "
				+      "WHERE oya_bumon_cd = ? ");
		if(isYuukoukigennai) {
			// 有効期限で絞り込みをします。
			sql.append(" AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to ");
		}
		sql.append("UNION ALL "
				        /* 再帰的に処理する条件は一時テーブルの部門コードをキーに所属部門の親部門コードを検索します。*/
				        /* 検索結果は一時テーブルに蓄積されます。*/
				+      "SELECT r.level + 1, s.bumon_cd, s.bumon_name, s.oya_bumon_cd, s.yuukou_kigen_from, s.yuukou_kigen_to "
				+      "FROM shozoku_bumon s, rec_bumon r ");
// 有効期限が異なる同じ部門コードが存在すると無限ループする対応  Ver22.12.02.11 *-
// +      "WHERE s.oya_bumon_cd = r.bumon_cd "); 
		if(isYuukoukigennai) {
			// 有効期限で絞り込みをします。
			sql.append("WHERE s.oya_bumon_cd = r.bumon_cd "
				+    "  AND current_date BETWEEN s.yuukou_kigen_from AND s.yuukou_kigen_to ");
		} else {
			sql.append("WHERE s.oya_bumon_cd = r.bumon_cd AND s.yuukou_kigen_from = r.yuukou_kigen_from ");
		}
// -*
		
		sql.append(")"
				    /* 一時テーブルの内容を表示します。*/
				+  "SELECT * FROM rec_bumon ORDER BY level, bumon_cd;");
		return connection.load(sql.toString(), oya_bumon_cd);
	}

	/**
	 * 親部門コード配下の階層を取得する。SQLで再帰処理を行い階層ごとにデータを取得する。
	 * @param oya_bumon_cd 親部門コード（空「""」の場合はROOTから検索する。）
	 * @param kijunBi 有効期限内のデータに絞り込むフラグ
	 * @return 検索結果
	 */
	public List<GMap> selectBumonTreeWithKijunbi(String oya_bumon_cd, Date kijunBi){
		final StringBuilder sql = new StringBuilder();
		sql.append("WITH RECURSIVE rec_bumon( " /* 一時テーブル */
				+      "level, "                /* 階層(TEMPデータ) */
				+      "bumon_cd, "             /* 部門コード */
				+      "bumon_name, "           /* 部門名 */
				+      "oya_bumon_cd, "         /* 親部門コード */
				+      "yuukou_kigen_from, "    /* 有効期限開始日 */
				+      "yuukou_kigen_to "       /* 有効期限終了日 */
				+  ") AS ("
				        /* 初回の検索条件は受け取った親部門コードをキーに検索します。*/
				+      "SELECT 1, bumon_cd, bumon_name, oya_bumon_cd, yuukou_kigen_from, yuukou_kigen_to "
				+      "FROM shozoku_bumon "
				+      "WHERE oya_bumon_cd = ? ");
		// 有効期限で絞り込みをします。
		sql.append(" AND ? BETWEEN yuukou_kigen_from AND yuukou_kigen_to ");
		sql.append("UNION ALL "
				        /* 再帰的に処理する条件は一時テーブルの部門コードをキーに所属部門の親部門コードを検索します。*/
				        /* 検索結果は一時テーブルに蓄積されます。*/
				+      "SELECT r.level + 1, s.bumon_cd, s.bumon_name, s.oya_bumon_cd, s.yuukou_kigen_from, s.yuukou_kigen_to "
				+      "FROM shozoku_bumon s, rec_bumon r "
				+      "WHERE s.oya_bumon_cd = r.bumon_cd ");
		// 有効期限で絞り込みをします。
		sql.append(" AND ? BETWEEN s.yuukou_kigen_from AND s.yuukou_kigen_to ");

		sql.append(")"
				    /* 一時テーブルの内容を表示します。*/
				+  "SELECT * FROM rec_bumon ORDER BY level, bumon_cd;");
		return connection.load(sql.toString(), oya_bumon_cd, kijunBi, kijunBi);
	}

	/**
	 * 部門コードから親部門を検索し、ルート（全社）までのデータをリストで返却します。
	 * @param bumon_cd String 部門コード
	 * @param kijunBi  Date   基準日
	 * @return 検索結果（データの並び順は「全社⇒子部門⇒孫部門⇒…⇒パラメータの部門」）
	 */
	public List<GMap> selectParentBumon(String bumon_cd, Date kijunBi){
		final String sql = "WITH RECURSIVE rec_bumon( " /* 一時テーブル */
						+      "level, "                /* 階層(TEMPデータ) */
						+      "bumon_cd, "             /* 部門コード */
						+      "bumon_name, "           /* 部門名 */
						+      "oya_bumon_cd "          /* 親部門コード */
						+  ") AS ("
						        /* 初回の検索条件は受け取った部門コードをキーに検索します。*/
						+      "SELECT 1, bumon_cd, bumon_name, oya_bumon_cd "
						+      "FROM   shozoku_bumon "
						+      "WHERE  bumon_cd = ? "
						+      "AND ? BETWEEN yuukou_kigen_from AND yuukou_kigen_to "
						+  "UNION ALL "
						        /* 再帰的に処理する条件は一時テーブルの親部門コードをキーに所属部門の部門コードを検索します。*/
						        /* 検索結果は一時テーブルに蓄積されます。*/
						+      "SELECT r.level + 1, s.bumon_cd, s.bumon_name, s.oya_bumon_cd "
						+      "FROM   shozoku_bumon s, rec_bumon r "
						+      "WHERE  s.bumon_cd = r.oya_bumon_cd "
						+      "AND ? BETWEEN s.yuukou_kigen_from AND s.yuukou_kigen_to "
						+  ")"
						    /* 一時テーブルの内容を表示します。*/
						+  "SELECT * FROM rec_bumon ORDER BY level DESC;";
		return connection.load(sql, bumon_cd, kijunBi, kijunBi);
	}

	/**
	 * 全期間の全「所属部門コード」を取得する。
	 * @return 検索結果 リスト
	 */
	public List<String> selectAllBumonCd(){
		final String sql = "SELECT DISTINCT bumon_cd FROM shozoku_bumon";

		List<GMap> list = connection.load(sql);
		List<String> retList = new ArrayList<String>();
		for(GMap map : list){
			if(map.get("bumon_cd") != null){
				String tmp = (String)map.get("bumon_cd");
				retList.add(tmp);
			}
		}

		return retList;
	}

/* 部門ロール(bumon_role) */

	/**
	 * 「部門ロール」のデータを全て取得する。
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectBumonRole(){
		final String sql = "SELECT bumon_role_id, bumon_role_name "
						+  "FROM bumon_role "
		 				+  "ORDER BY hyouji_jun ";
		return connection.load(sql);
	}

	/**
	 * 部門ロールIDをキーに「部門ロール」を取得する。データがなければnull。
	 * @param bumon_role_id 部門ロールID
	 * @return 検索結果 1件データ
	 */
	public GMap selectBumonRole(String bumon_role_id){
		final String sql = "SELECT bumon_role_id, bumon_role_name, hyouji_jun FROM bumon_role WHERE bumon_role_id = ?";
		return connection.find(sql, bumon_role_id);
	}

/* 所属部門割り当て(shozoku_bumon_wariate) */

	/**
	 * ユーザーIDをキーに「所属部門割り当て」「所属部門」「部門ロール」「負担部門」からデータを取得する。
	 * @param user_id String ユーザーID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectShozokuBumonKanri(String user_id){
		final String sql = "SELECT sbw.bumon_cd, sb.bumon_name, sb.oya_bumon_cd, sbw.bumon_role_id, br.bumon_role_name, sbw.user_id,sbw.daihyou_futan_bumon_cd,bm.futan_bumon_name,sbw.yuukou_kigen_from, sbw.yuukou_kigen_to, sbw.hyouji_jun "
						 + "FROM   shozoku_bumon_wariate sbw LEFT OUTER JOIN shozoku_bumon sb ON sbw.bumon_cd      = sb.bumon_cd "
						 + " AND sbw.yuukou_kigen_to BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to "
						 +                                  "LEFT OUTER JOIN bumon_role br    ON sbw.bumon_role_id = br.bumon_role_id "
						 + "LEFT OUTER JOIN bumon_master bm  ON sbw.daihyou_futan_bumon_cd = bm.futan_bumon_cd "
						 + "WHERE sbw.user_id = ? "
						 + "ORDER BY sbw.hyouji_jun ASC";
		return connection.load(sql, user_id);
	}

	/**
	 * ユーザーに紐付く所属部門ロール情報を取得
	 * @param user_id ユーザーID
	 * @return 所属部門ロール情報
	 */
	public List<GMap> selectValidShozokuBumonRole(String user_id){
		final String sql =
				"SELECT "
			+ "  shozoku_bumon.bumon_cd "
			+ "  ,shozoku_bumon.bumon_name "
			+ "  ,bumon_role.bumon_role_id "
			+ "  ,bumon_role.bumon_role_name "
			+ "  ,shozoku_bumon_wariate.daihyou_futan_bumon_cd "
			+ "FROM shozoku_bumon_wariate "
			+ "INNER JOIN shozoku_bumon ON "
			+ "  shozoku_bumon.bumon_cd = shozoku_bumon_wariate.bumon_cd "
			+ "  and current_date BETWEEN shozoku_bumon.yuukou_kigen_from and shozoku_bumon.yuukou_kigen_to "
			+ "INNER JOIN bumon_role ON "
			+ "  bumon_role.bumon_role_id = shozoku_bumon_wariate.bumon_role_id "
			+ "WHERE "
			+ "  shozoku_bumon_wariate.user_id = ? "
			+ "  AND current_date BETWEEN shozoku_bumon_wariate.yuukou_kigen_from AND shozoku_bumon_wariate.yuukou_kigen_to "
			+ "ORDER BY shozoku_bumon_wariate.hyouji_jun ASC";
		return connection.load(sql, user_id);
	}

	/**
	 * ユーザーに紐付く代表負担部門コード（先頭）を取得。現在有効なのがなければブランク。
	 * @param userId ユーザーID
	 * @return 代表負担部門コード
	 */
	public String findFirstDaihyouFutanBumonCd(String userId){
		List<GMap> shozokuBumonList = selectValidShozokuBumonRole(userId);
		return (shozokuBumonList.isEmpty()) ? "" : (String)shozokuBumonList.get(0).get("daihyou_futan_bumon_cd");
	}

/* 業務ロール(gyoumu_role) */

	/**
	 * 「業務ロール」のデータを全て取得する。
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectGyoumuRole(){
		final String sql = "SELECT gyoumu_role_id, gyoumu_role_name "
						 + "FROM gyoumu_role "
						 + "WHERE gyoumu_role_id <> ? "
						 + "ORDER BY hyouji_jun ";
		return connection.load(sql, GyoumuRoleId.SYSTEM_KANRI);
	}

	/**
	 * 業務ロールIDをキーに「業務ロール」を取得する。データがなければnull。
	 * @param gyoumu_role_id 業務ロールID
	 * @return 検索結果 1件データ
	 */
	public GMap selectGyoumuRole(String gyoumu_role_id){
		final String sql = "SELECT gyoumu_role_id, gyoumu_role_name, hyouji_jun FROM gyoumu_role WHERE gyoumu_role_id = ?";
		return connection.find(sql, gyoumu_role_id);
	}

/* 業務ロール_機能制御(gyoumu_role_kinou_seigyo) */

	/**
	 * 業務ロール機能制御コードが有効な業務ロールを取得します。
	 * @param gyoumu_role_kinou_seigyo_cd 業務ロール機能制御コード
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectValidGyoumuRoleKinou(String gyoumu_role_kinou_seigyo_cd){
		final String sql = "SELECT gr.gyoumu_role_id, gr.gyoumu_role_name, grks.gyoumu_role_kinou_seigyo_cd, grks.gyoumu_role_kinou_seigyo_kbn "
						+  "FROM   gyoumu_role gr, gyoumu_role_kinou_seigyo grks "
						+  "WHERE  gr.gyoumu_role_id = grks.gyoumu_role_id AND grks.gyoumu_role_kinou_seigyo_cd = ? AND grks.gyoumu_role_kinou_seigyo_kbn = '1' "
						+  "ORDER BY gr.hyouji_jun ";
		return connection.load(sql, gyoumu_role_kinou_seigyo_cd);
	}

	/**
	 * 業務ロールID、業務ロール制御区分をキーに「業務ロール_制御機能」からデータを取得する。データがなければnull。
	 * @param gyoumu_role_id String 業務ロールID
	 * @param seigyoKbn      String 業務ロール制御区分
	 * @return 検索結果 1件データ
	 */
	public GMap selectGyoumuRoleKinouSeigyo(String gyoumu_role_id, String seigyoKbn){
		final String sql = "SELECT gyoumu_role_id, gyoumu_role_kinou_seigyo_cd, gyoumu_role_kinou_seigyo_kbn "
						+  "FROM   gyoumu_role_kinou_seigyo "
						+  "WHERE  gyoumu_role_id = ? AND gyoumu_role_kinou_seigyo_cd = ?;";
		return connection.find(sql, gyoumu_role_id, seigyoKbn);
	}

	/**
	 * 業務ロールIDをキーに、ログインしている業務ロールの「業務ロール機能制御区分」を取得する。
	 * @param gyoumu_role_id 業務ロールID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectGyoumuRoleKinouSeigyoKbn(String gyoumu_role_id) {
		final String sql = "SELECT gyoumu_role_kinou_seigyo_cd, gyoumu_role_kinou_seigyo_kbn "
						+  "FROM   gyoumu_role_kinou_seigyo "
						+  "WHERE  gyoumu_role_id = ?;";
		return connection.load(sql, gyoumu_role_id);
	}

/* 業務ロール割り当て(gyoumu_role_wariate) */

	/**
	 * ユーザーIDをキーに「業務ロール割り当て」「業務ロール」からデータを取得する。
	 * @param user_id String ユーザーID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectGyoumuRoleKanri(String user_id){
		final String sql = "SELECT grw.gyoumu_role_id, gr.gyoumu_role_name, grw.user_id, grw.yuukou_kigen_from, grw.yuukou_kigen_to, grw.shori_bumon_cd, sb.bumon_name "
						 + "FROM   gyoumu_role_wariate grw LEFT OUTER JOIN gyoumu_role gr ON grw.gyoumu_role_id = gr.gyoumu_role_id "
						 +   " LEFT OUTER JOIN shozoku_bumon sb ON grw.shori_bumon_cd     = sb.bumon_cd "
						 +   " AND grw.yuukou_kigen_to BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to "
						 + "WHERE grw.user_id = ? "
						 + "ORDER BY grw.gyoumu_role_id ASC ";
		return connection.load(sql, user_id);
	}

	/**
	 * ユーザーに紐付く業務ロール情報を取得
	 * @param user_id ユーザーID
	 * @return ユーザーに紐付く業務ロール情報
	 */
	public List<GMap> selectValidGyoumuRole(String user_id){
		final String sql =
				"SELECT "
			+ "  gyoumu_role.gyoumu_role_id "
			+ "  ,gyoumu_role.gyoumu_role_name "
			+ "  ,gyoumu_role_wariate.shori_bumon_cd "
			+ "FROM gyoumu_role_wariate "
			+ "INNER JOIN gyoumu_role ON "
			+ "  gyoumu_role_wariate.gyoumu_role_id = gyoumu_role.gyoumu_role_id "
			+ "WHERE "
			+ "  gyoumu_role_wariate.user_id=? "
			+ "  AND current_date BETWEEN gyoumu_role_wariate.yuukou_kigen_from AND gyoumu_role_wariate.yuukou_kigen_to "
			+ "ORDER BY "
			+ "  gyoumu_role.hyouji_jun ";
		return connection.load(sql, user_id);
	}

	/**
	 * 指定ユーザーが指定ロールの権限を持つかチェックする。
	 * 処理呼出時点で指定ロールとしてログインしているかは考慮しない。
	 * @param user ユーザー
	 * @param seigyoCd 業務ロール機能制御コード
	 * @return 指定ユーザーが指定ロールを持っているか
	 */
	public boolean haveGyoumuRole(User user, String seigyoCd){
		final String sql = "SELECT * FROM gyoumu_role_kinou_seigyo "
				+ " WHERE gyoumu_role_kinou_seigyo_cd = ? "
				+ " AND gyoumu_role_kinou_seigyo_kbn = '1' "
				+ " AND gyoumu_role_id IN "
				+ " (SELECT gyoumu_role_id FROM gyoumu_role_wariate WHERE user_id = ? AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to) "
				+ " LIMIT 1";
		GMap res = connection.find(sql, seigyoCd, user.getSeigyoUserId());
		return (res != null);
	}

/* 代行処理指定(daikou_shitei) */

// /**
//  * 「代行処理指定」からデータを取得する。データがなければnull。
//  * @param login_user_id    ログインユーザーID
//  * @return 検索結果 リスト
//  */
// public List<GMap> selectDaikouShiteiUserList(String login_user_id) {
// final String sql = "SELECT daikou_irai_user_id, daikou_user_id "
// +  "FROM daikou_shitei "
// +  "WHERE daikou_user_id = ? AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to ";
// return connection.load(sql, login_user_id);
// }

// /**
//  * 「代行処理指定」からデータを取得する。データがなければnull。
//  * @param login_user_id    ログインユーザーID
//  * @param shounin_user_id  現在作業者ユーザーID
//  * @return 検索結果 1件データ
//  */
// public GMap selectDaikouShiteiUser(String login_user_id, String shounin_user_id) {
// final String sql = "SELECT daikou_irai_user_id, daikou_user_id "
// +  "FROM daikou_shitei "
// +  "WHERE daikou_irai_user_id = ? AND daikou_user_id = ? AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to ";
// return connection.find(sql, shounin_user_id, login_user_id);
// }

	/**
	 * 被代行ユーザーのリスト取得
	 * @param daikou_user_id 代行ユーザーID
	 * @return 被代行ユーザーリスト
	 */
	public List<GMap> selectValidHiDaikouUser(String daikou_user_id) {
		final String sql =
				"SELECT "
			+ "  user_info.user_id "
			+ "  ,user_info.shain_no "
			+ "  ,user_info.user_sei "
			+ "  ,user_info.user_mei "
			+ "FROM daikou_shitei "
			+ "INNER JOIN user_info ON "
			+ "  user_info.user_id = daikou_shitei.hi_daikou_user_id "
			+ "  AND current_date BETWEEN user_info.yuukou_kigen_from AND user_info.yuukou_kigen_to "
			+ "WHERE "
			+ "  daikou_shitei.daikou_user_id = ? "
			+ "ORDER BY "
			+ "  user_info.touroku_time ";
		return connection.load(sql, daikou_user_id);
	}

	/**
	 * 被代行ユーザーのリスト取得
	 * @param daikou_user_id 代行ユーザーID
	 * @param hi_daikou_user_id 被代行ユーザーID
	 * @return 被代行ユーザーリスト
	 */
	public long selectDaikouCount(String daikou_user_id, String hi_daikou_user_id) {
		final String sql =
				"SELECT "
			+ "  count(*) count "
			+ "FROM daikou_shitei "
			+ "WHERE "
			+ "  daikou_shitei.daikou_user_id = ? "
			+ "  AND daikou_shitei.hi_daikou_user_id = ? ";
		GMap record = connection.find(sql, daikou_user_id, hi_daikou_user_id);
		return (long)record.get("count");
	}

/* 採番管理(saiban_kanri) */

	/**
	 * 採番区分をキーに採番管理よりシーケンス番号を取得する。
	 * @param saiban_kbn 採番区分
	 * @return シーケンス番号
	 */
	public int selectSaibanKanri(String saiban_kbn) {
		// テーブルロックしつつシーケンス番号の取得
		final String selectSql = "SELECT sequence_val FROM saiban_kanri WHERE saiban_kbn = ? FOR UPDATE";
		GMap map = connection.find(selectSql, saiban_kbn);
		int result = (int) map.get("sequence_val");

		// シーケンス番号の更新
		final String updateSql = "UPDATE saiban_kanri SET sequence_val = ? WHERE saiban_kbn = ? ";
		connection.update(updateSql, result + 1, saiban_kbn);

		return result;
	}

	/**
	 * 自部門を含めた所属部門配下の部門コードを連結した文字列を取得する。
	 * @param gyoumuRoleShozokuBumonCd 業務ロール所属部門コード
	 * @param isYuukoukigennai 有効期限内のデータに絞り込むフラグ
	 * @return 連結した所属部門コード
	 */
	public String getMergeGyoumuRoleShozokuBumonCd(String[] gyoumuRoleShozokuBumonCd, boolean isYuukoukigennai) {

		// 指定部門配下の部門コードリストを取得
		Set<String> shozokuBumonSet = new HashSet<>();
		for(String bumonCdTmp : gyoumuRoleShozokuBumonCd){
			// 業務ロール所属部門コードの存在チェック
			if(!EteamCommon.dataSonzaiCheck(connection, bumonCdTmp, EteamCommon.CheckTable.SHOZOKU_BUMON_KEY_CD_ALL_DATE)) {
				continue;
			}
			shozokuBumonSet.add(bumonCdTmp);
			List<GMap>bumonList = selectBumonTreeStructure(bumonCdTmp, isYuukoukigennai);
			for (GMap map : bumonList) {
				String bumonCd =  map.get("bumon_cd").toString();
				shozokuBumonSet.add(bumonCd);
			}
		}

		// SQLで使用できる形式に変換して返却
		if (shozokuBumonSet.size() <= 0)
		{
			return "''";
		}
		String searchGyoumuRoleShozokuBumonCd = "";
		for (String bumonCd : shozokuBumonSet) {
			if(searchGyoumuRoleShozokuBumonCd.isEmpty()){
				searchGyoumuRoleShozokuBumonCd = "'" + bumonCd + "'";
			}else{
				searchGyoumuRoleShozokuBumonCd += ",'" + bumonCd + "'";
			}
		}
		return searchGyoumuRoleShozokuBumonCd;
	}

	/**
	 * 部門コードを連結した文字列に該当の部門が含まれるかチェックする。
	 * @param mergeGyoumuRoleShozokuBumonCd 業務ロール所属部門コード
	 * @param shozokuBumonCd 部門コード
	 * @return 該当部門の有無
	 */
	public boolean checkGyoumuRoleShozokuBumonCd(String mergeGyoumuRoleShozokuBumonCd, String shozokuBumonCd) {
		String[] bumonList = mergeGyoumuRoleShozokuBumonCd.split(",", 0);
		String bumonCd;
		for (int i=0; i<bumonList.length; i++){
			bumonCd = bumonList[i].replaceAll("'", "");
			if(bumonCd.equals(shozokuBumonCd)) return true;
		}
		return false;
	}

/* 合議部署一覧 */
	/**
	 * 合議部署を取得する
	 * @return 合議部署リスト
	 */
	public List<GMap> selectGougiBusho() {
		final String sql = "SELECT * FROM gougi_pattern_oya oya ORDER BY oya.hyouji_jun";
		return connection.load(sql);
	}

	/**
	 * 合議部署Noによる、合議部署を取得する
	 * @param gougiNo 合議部署No
	 * @return 合議部署
	 */
	public GMap findGougiBusho(String gougiNo) {
		final String sql = "SELECT * FROM gougi_pattern_oya oya WHERE gougi_pattern_no = ?";
		return connection.find(sql, Integer.parseInt(gougiNo));
	}
	/**
	 * 部門一覧を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadBumonIchiran(){
		final String sql = "SELECT bumon_cd, bumon_name, oya_bumon_cd, yuukou_kigen_from, yuukou_kigen_to, security_pattern FROM shozoku_bumon WHERE bumon_cd <> '0000' ORDER BY bumon_cd;";
		return connection.load(sql);
	}
	/**
	 * ユーザー一覧を取得する。
	 * @return 検索結果（adminを除く）
	 */
	public List<GMap> loadUserIchiran(){
		final String sql = "SELECT user_id ,shain_no ,user_sei ,user_mei ,mail_address ,yuukou_kigen_from ,yuukou_kigen_to ,dairikihyou_flg ,houjin_card_riyou_flag ,houjin_card_shikibetsuyou_num, security_pattern ,security_wfonly_flg ,shounin_route_henkou_level ,maruhi_kengen_flg ,maruhi_kaijyo_flg ,zaimu_kyoten_nyuryoku_only_flg FROM user_info WHERE user_id <> 'admin' ORDER BY shain_no;";
		return connection.load(sql);
	}
	/**
	 * 所属部門割り当て一覧を取得する。
	 * @return 検索結果（adminを除く）
	 */
	public List<GMap> loadShozokuBumonWariateIchiran(){
		final String sql = "SELECT bumon_cd, bumon_role_id, user_id, daihyou_futan_bumon_cd, yuukou_kigen_from, yuukou_kigen_to, hyouji_jun FROM shozoku_bumon_wariate WHERE user_id <> 'admin' ORDER BY bumon_cd, user_id;";
		return connection.load(sql);
	}
}
