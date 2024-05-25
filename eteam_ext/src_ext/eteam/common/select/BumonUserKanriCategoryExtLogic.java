package eteam.common.select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamContainer;
import eteam.base.GMap;

public class BumonUserKanriCategoryExtLogic extends BumonUserKanriCategoryLogic {
	/**
	 * ユーザー検索<br>
	 * ユーザー名、部門コード、部門ロールID（役割）、業務ロールIDをキーに<br>
	 * 「ユーザー情報」「所属部門割り当て」「所属部門」「部門ロール」「業務ロール割り当て」「業務ロール」を結合し検索を行う。<br>
	 * ※検索結果は所属別に取得されるので、ユーザーは重複する。
	 *
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
	 * @param isKaishaKirikae 会社切替設定されているユーザを検索するかどうか
	 * @return 検索結果 リスト
	 */
	//カスタマイズここから
	//public List<GMap> userSerach(String user_name, String user_id, String shain_no, String bumon_cd, String bumon_role_id, String gyoumu_role_id, Date kijunDate, boolean isAccountTmpLock, boolean isAccountLock, boolean isDairiKihyouFlg, String houjinCardShikibetsuNum) {
		public List<GMap> userSerach(String user_name
				   ,String user_id
				   ,String shain_no
				   ,String bumon_cd
				   ,String bumon_role_id
				   ,String gyoumu_role_id
				   ,Date kijunDate
				   ,boolean isAccountTmpLock
				   ,boolean isAccountLock
				   ,boolean isDairiKihyouFlg
				   ,String houjinCardShikibetsuNum
				   ,boolean isKaishaKirikae) {
	//カスタマイズここまで
		final StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		//カスタマイズここから
		sql.append("SELECT DISTINCT ");
		sql.append(   "ui.user_id ");											/* ユーザーID */
		sql.append(   ",ui.shain_no ");											/* 社員番号 */
		sql.append(   ",ui.user_sei ");											/* ユーザー姓 */
		sql.append(   ",ui.user_mei ");											/* ユーザー名 */
		sql.append(   ",ui.yuukou_kigen_from ");								/* 有効期限開始日(ユーザー情報) */
		sql.append(   ",ui.yuukou_kigen_to ");									/* 有効期限終了日(ユーザー情報) */
		sql.append(   ",sbw.bumon_cd ");										/* 部門コード */
		sql.append(   ",sb.bumon_name ");										/* 部門名 */
		sql.append(   ",sbw.bumon_role_id ");									/* 部門ロールID */
		sql.append(   ",br.bumon_role_name ");									/* 部門ロール名 */
		sql.append(   ",br.hyouji_jun " );										/* 表示順 */
		sql.append(   ",ui.touroku_time ");										/* 登録日時(ユーザー情報) */
		sql.append(   ",sbw.yuukou_kigen_from AS bumon_wariate_kigen_from ");	/* 有効期限開始日(所属部門割り当て) */
		sql.append(   ",sbw.yuukou_kigen_to   AS bumon_wariate_kigen_to  ");	/* 有効期限終了日(所属部門割り当て) */
		sql.append(   ",case ");
		sql.append(   "  when 0 < ? and ui.tmp_lock_flg = '1' and ui.tmp_lock_time is null then ");
		sql.append(   "    '0' ");
		sql.append(   "  when 0 < ? and ui.tmp_lock_flg = '1' and current_timestamp >= ui.tmp_lock_time + (interval '1 min' * cast(? as integer)) then ");
		sql.append(   "    '0' ");
		sql.append(   "  else ");
		sql.append(   "    ui.tmp_lock_flg ");
		sql.append(   " end as tmp_lock_flg ");									/* アカウント一時ロックフラグ */
		sql.append(   ",ui.tmp_lock_time ");									/* アカウント一時ロック日時 */
		sql.append(   ",ui.lock_flg ");											/* アカウント永続ロックフラグ */
		sql.append(   ",ui.lock_time ");										/* アカウント永続ロック日時 */
		sql.append(   ",ui.dairikihyou_flg ");									/* 代理起票可能フラグ */
		sql.append(   ",ui.houjin_card_riyou_flag ");
		sql.append(   ",ui.houjin_card_shikibetsuyou_num ");					/* 法人カード識別用番号 */
		sql.append("FROM user_info ui ");
if (isKaishaKirikae){
		// 全社承認の検索時は会社切替設定と結合する
		sql.append("INNER JOIN kaisha_kirikae_settei AS kks ON kks.user_id = ui.user_id ");
}
		/* ユーザー情報と所属部門割り当てをユーザーIDで結合 */
		sql.append("LEFT OUTER JOIN shozoku_bumon_wariate sbw ON ui.user_id = sbw.user_id ");
		/* 所属部門割り当てと所属部門を部門コードで結合 */
		sql.append("LEFT OUTER JOIN shozoku_bumon sb ON sbw.bumon_cd = sb.bumon_cd ");
		sql.append("                                 AND ? BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to ");
		/* 所属部門割り当てと部門ロールを部門ロールIDで結合 */
		sql.append("LEFT OUTER JOIN bumon_role br    ON sbw.bumon_role_id = br.bumon_role_id ");
		/* ユーザー情報と業務ロール割り当てをユーザーIDで結合 */
		sql.append("LEFT OUTER JOIN gyoumu_role_wariate grw ON ui.user_id = grw.user_id ");
		/* 業務ロール割り当てと業務ロールを業務ロールIDで結合 */
		sql.append("LEFT OUTER JOIN gyoumu_role gr   ON grw.gyoumu_role_id = gr.gyoumu_role_id ");

//		sql.append("SELECT ");
//		sql.append("DISTINCT ");
//		sql.append(    "ui.user_id, ");            /* ユーザーID */
//		sql.append(    "ui.shain_no, ");           /* 社員番号 */
//		sql.append(    "ui.user_sei, ");           /* ユーザー姓 */
//		sql.append(    "ui.user_mei, ");           /* ユーザー名 */
//		sql.append(    "ui.yuukou_kigen_from, ");  /* 有効期限開始日(ユーザー情報) */
//		sql.append(    "ui.yuukou_kigen_to, ");    /* 有効期限終了日(ユーザー情報) */
//		sql.append(    "sbw.bumon_cd, ");          /* 部門コード */
//		sql.append(    "sb.bumon_name, ");         /* 部門名 */
//		sql.append(    "sbw.bumon_role_id, ");     /* 部門ロールID */
//		sql.append(    "br.bumon_role_name, ");    /* 部門ロール名 */
//		sql.append(    "br.hyouji_jun, " );        /* 表示順 */
//		sql.append(    "ui.touroku_time, ");       /* 登録日時(ユーザー情報) */
//		sql.append(    "sbw.yuukou_kigen_from AS bumon_wariate_kigen_from, "); /* 有効期限開始日(所属部門割り当て) */
//		sql.append(    "sbw.yuukou_kigen_to   AS bumon_wariate_kigen_to,  ");  /* 有効期限終了日(所属部門割り当て) */
//		sql.append(    "case ");
//		sql.append(    "  when 0 < ? and ui.tmp_lock_flg = '1' and ui.tmp_lock_time is null then ");
//		sql.append(    "    '0' ");
//		sql.append(    "  when 0 < ? and ui.tmp_lock_flg = '1' and current_timestamp >= ui.tmp_lock_time + (interval '1 min' * cast(? as integer)) then ");
//		sql.append(    "    '0' ");
//		sql.append(    "  else ");
//		sql.append(    "    ui.tmp_lock_flg ");
//		sql.append(    "end as tmp_lock_flg, ");   /* アカウント一時ロックフラグ */
//		sql.append(    "ui.tmp_lock_time, ");      /* アカウント一時ロック日時 */
//		sql.append(    "ui.lock_flg, ");           /* アカウント永続ロックフラグ */
//		sql.append(    "ui.lock_time, ");          /* アカウント永続ロック日時 */
//		sql.append(    "ui.dairikihyou_flg, ");     /* 代理起票可能フラグ */
//		sql.append(    "ui.houjin_card_riyou_flag, ");
//		sql.append(    "ui.houjin_card_shikibetsuyou_num ");/* 法人カード識別用番号 */
//		sql.append("FROM ");
//					/* ユーザー情報と所属部門割り当てをユーザーIDで結合 */
//					/* 所属部門割り当てと所属部門を部門コードで結合 */
//					/* 所属部門割り当てと部門ロールを部門ロールIDで結合 */
//					/* ユーザー情報と業務ロール割り当てをユーザーIDで結合 */
//					/* 業務ロール割り当てと業務ロールを業務ロールIDで結合 */
//		sql.append(    "user_info ui LEFT OUTER JOIN shozoku_bumon_wariate sbw ON ui.user_id = sbw.user_id ");
//		sql.append(                 "LEFT OUTER JOIN shozoku_bumon sb ON sbw.bumon_cd = sb.bumon_cd ");
//		sql.append("                                 AND ? BETWEEN sb.yuukou_kigen_from AND sb.yuukou_kigen_to ");
//		sql.append(                 "LEFT OUTER JOIN bumon_role br    ON sbw.bumon_role_id = br.bumon_role_id ");
//		sql.append(                 "LEFT OUTER JOIN gyoumu_role_wariate grw ON ui.user_id = grw.user_id ");
//		sql.append(                 "LEFT OUTER JOIN gyoumu_role gr   ON grw.gyoumu_role_id = gr.gyoumu_role_id ");
		//カスタマイズここまで

		// 表示用のパラメータ追加
		int tmpLockTerm = Integer.parseInt(setting.passLockTerm());
		params.add(Integer.valueOf(tmpLockTerm));
		params.add(Integer.valueOf(tmpLockTerm));
		params.add(Integer.valueOf(tmpLockTerm));

		// カスタマイズ
		// 取得データとして基準日(無ければ現在日)を指定
		Date tmpDt = kijunDate != null ? kijunDate : new Date(System.currentTimeMillis());
		params.add(tmpDt);

		// Adminは検索結果に出力しないようにする。
		sql.append(" WHERE ui.user_id NOT IN('admin') ");

		// 有効期限内に絞り込む場合、ユーザー自体と所属の有効期限で絞り込む
		if (kijunDate != null) {
			//カスタマイズここから
			if (isKaishaKirikae){
				// 全社承認の検索時は有効期限を条件に含める
				sql.append(" AND ? BETWEEN kks.yuukou_kigen_from AND kks.yuukou_kigen_to ");
				params.add(kijunDate);
			}
			//カスタマイズここまで
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

	//カスタマイズここから
		/* 会社切替設定(kaisha_kirikae_settei) */

		/**
		 * 会社切替設定情報取得<br>
		 * 指定されたユーザーに設定された会社切替可能なスキーマ情報を取得する。
		 *
		 * @param user_id String ユーザーID
		 * @return 会社切替設定情報リスト
		 */
		public List<GMap> selectKaishaKirikaeSettei(String user_id){
			StringBuilder sbSql = new StringBuilder();
			sbSql.append("SELECT");
			sbSql.append("   A.scheme_cd");
			sbSql.append("  ,A.yuukou_kigen_from");
			sbSql.append("  ,A.yuukou_kigen_to");
			sbSql.append("  ,A.hyouji_jun");
			sbSql.append(" FROM kaisha_kirikae_settei AS A");
			sbSql.append(" WHERE A.user_id = ?");
			sbSql.append("   AND current_date BETWEEN A.yuukou_kigen_from AND A.yuukou_kigen_to");
			sbSql.append(" ORDER BY A.hyouji_jun ASC");
			List<GMap> list = connection.load(sbSql.toString(), user_id);
			PostgreSQLSystemCatalogsLogic pgLog = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class);
			pgLog.init(connection);
			for (Map<String, Object>map : list) {
				String schemeName = ((PostgreSQLSystemCatalogsExtLogic)pgLog).findSchemeName((String)map.get("scheme_cd"));
				map.put("scheme_name", schemeName);
			}
			return list;
		}

		/**
		 * ユーザー検索<br>
		 * 会社切替設定されているユーザを検索する。<br>
		 *
		 * @param kijunDate 基準日
		 * @return 検索結果 リスト
		 */
		public  List<GMap>  selectZenshaShounin(Date kijunDate) {
			return this.userSerach("", "", "", "", "", "", kijunDate, false, false, false,"", true);
		}
	//カスタマイズここまで
		//カスタマイズここから
		/**
		 * 他スキーマの業務ロールIDをキーに、ログインしている業務ロールの「業務ロール機能制御区分」を取得する。
		 * @param gyoumu_role_id 業務ロールID
		 * @param schema スキーマ
		 * @return 検索結果 リスト
		 */
		public List<GMap> selectGyoumuRoleKinouSeigyoKbnOtherSchema(String gyoumu_role_id, String schema) {
			final String sql = "SELECT gyoumu_role_kinou_seigyo_cd, gyoumu_role_kinou_seigyo_kbn "
							+  "FROM   " + schema + ".gyoumu_role_kinou_seigyo "
							+  "WHERE  gyoumu_role_id = ?;";
			return connection.load(sql, gyoumu_role_id);
		}
		/**
		 * 他スキーマの業務ロールを制御コードにより判別する
		 * @param wf WFの値
		 * @param kr KRの値
		 * @param co COの値
		 * @param userId ユーザーID
		 * @param schema スキーマ
		 * @return 業務ロールMAP
		 */
		public Map<String, Object> selectGyoumuRoleOtherSchema(String wf, String kr, String co, String userId, String schema) {
			final String sql ="SELECT "
							+ "    gyoumu.gyoumu_role_id "
							+ "  , gyoumu.gyoumu_role_name "
							+ "FROM "
							+ "  ( "
							+ "    SELECT "
							+ "        tmp.gyoumu_role_id "
							+ "      , gr.gyoumu_role_name "
							+ "    FROM "
							+ "      ( "
							+ "        SELECT "
							+ "            a.gyoumu_role_id "
							+ "        FROM "
							+ "          " + schema + ".gyoumu_role_kinou_seigyo a "
							+ "          INNER JOIN ( "
							+ "            SELECT "
							+ "                b.gyoumu_role_id "
							+ "            FROM "
							+ "              " + schema + ".gyoumu_role_kinou_seigyo b "
							+ "              INNER JOIN ( "
							+ "                SELECT "
							+ "                    gyoumu_role_id "
							+ "                FROM "
							+ "                  " + schema + ".gyoumu_role_kinou_seigyo "
							+ "                WHERE "
							+ "                  gyoumu_role_kinou_seigyo_cd = 'WF' "
							+ "                  AND gyoumu_role_kinou_seigyo_kbn = ? "
							+ "              ) c "
							+ "                ON b.gyoumu_role_id = c.gyoumu_role_id "
							+ "            WHERE "
							+ "              b.gyoumu_role_kinou_seigyo_cd = 'KR' "
							+ "              AND b.gyoumu_role_kinou_seigyo_kbn = ? "
							+ "          ) d "
							+ "            ON a.gyoumu_role_id = d.gyoumu_role_id "
							+ "        WHERE "
							+ "          a.gyoumu_role_kinou_seigyo_cd = 'CO' "
							+ "          AND a.gyoumu_role_kinou_seigyo_kbn = ? "
							+ "      ) tmp "
							+ "      INNER JOIN " + schema + ".gyoumu_role gr "
							+ "        ON tmp.gyoumu_role_id = gr.gyoumu_role_id "
							+ "  ) gyoumu "
							+ "  INNER JOIN " + schema + ".gyoumu_role_wariate grw "
							+ "    ON gyoumu.gyoumu_role_id = grw.gyoumu_role_id "
							+ "WHERE "
							+ "  grw.user_id = ?; ";


			return connection.find(sql, wf, kr, co, userId);
		}
		
		/**
		 * ユーザー検索<br>
		 * ユーザー名、部門コード、部門ロールID（役割）、業務ロールIDをキーに<br>
		 * 「ユーザー情報」「所属部門割り当て」「所属部門」「部門ロール」「業務ロール割り当て」「業務ロール」を結合し検索を行う。<br>
		 * ※検索結果は所属別に取得されるので、ユーザーは重複する。
		 *
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
		 * @param isKaishaKirikae 会社切替設定されているユーザを検索するかどうか
		 * @return 検索結果 リスト
		 */
		//カスタマイズここから
		//public List<GMap> userSerach(String user_name, String user_id, String shain_no, String bumon_cd, String bumon_role_id, String gyoumu_role_id, boolean isYuukoukigennai, boolean isAccountTmpLock, boolean isAccountLock, boolean isDairiKihyouFlg) {
		public List<GMap> userSerach(String user_name
				   ,String user_id
				   ,String shain_no
				   ,String bumon_cd
				   ,String bumon_role_id
				   ,String gyoumu_role_id
				   ,boolean isYuukoukigennai
				   ,boolean isAccountTmpLock
				   ,boolean isAccountLock
				   ,boolean isDairiKihyouFlg
				   ,boolean isKaishaKirikae) {
		//カスタマイズここまで

			Date kijunDate = null;
			if(isYuukoukigennai == true){
				kijunDate = new Date(System.currentTimeMillis());
			}
			//カスタマイズここから
			//return userSerach(user_name, user_id, shain_no, bumon_cd, bumon_role_id, gyoumu_role_id, kijunDate, isAccountTmpLock, isAccountLock, isDairiKihyouFlg, "");
			return userSerach(user_name, user_id, shain_no, bumon_cd, bumon_role_id, gyoumu_role_id, kijunDate, isAccountTmpLock, isAccountLock, isDairiKihyouFlg, "", isKaishaKirikae);
			//カスタマイズここまで
		}
		/**
		 * 会社切替一覧を取得する。
		 * @return 検索結果（adminを除く）
		 */
		public List<GMap> loadKaishaKirikaeIchiran(){
			final String sql = "SELECT user_id, scheme_cd, yuukou_kigen_from, yuukou_kigen_to, hyouji_jun FROM kaisha_kirikae_settei WHERE user_id <> 'admin' ORDER BY user_id, scheme_cd;";
			return connection.load(sql);
		}
}
