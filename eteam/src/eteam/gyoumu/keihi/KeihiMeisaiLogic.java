package eteam.gyoumu.keihi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.MasterKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 集計部門・集計科目に関するロジック
 */
public class KeihiMeisaiLogic extends EteamAbstractLogic {
	
	/** null比較用文字列 */
	final static String NULL_STRING = "NullStringForDiff";

	/**
	 * 内部決算期(KESN)を取得
	 * @param date 日付
	 * @return 内部決算期
	 */
	public int findKesn(Date date) {
		final String sql = "SELECT kesn FROM ki_kesn WHERE ? BETWEEN from_date AND to_date";
		GMap record = connection.find(sql, date);
		return (record == null) ? 0 : (int)record.get("kesn");
	}

	
	/**
	 * 経費明細情報（集計部門単位）
	 */
	@Getter @Setter @ToString
	public class KeihiSyuukeiBumon {
		/** 部門コード */ String cd;
		/** 部門名 */ String name;
		/** 部門リスト */ List<KeihiBumon> bumonList = new ArrayList<>();

		/**
		 * コンストラクタ
		 * @param map 経費明細
		 */
		public KeihiSyuukeiBumon(GMap map) {
			this.cd = (String)map.get("oya_syuukei_bumon_cd");
			this.name = (String)map.get("oya_syuukei_bumon_name");
		}
	}
	
	/**
	 * 経費明細情報（部門単位）
	 */
	@Getter @Setter @ToString
	public class KeihiBumon {
		/** 部門コード */ String cd;
		/** 部門名 */ String name;
		/** 科目リスト */ List<KeihiKamoku> kamokuList = new ArrayList<>();

		/**
		 * コンストラクタ
		 * @param map 経費明細
		 */
		public KeihiBumon(GMap map) {
			this.cd = (String)map.get("futan_bumon_cd");
			this.name = (String)map.get("futan_bumon_name");
		}
	}

	/**
	 * 経費明細情報（科目単位）
	 */
	@Getter @Setter @ToString
	public class KeihiKamoku {
		/** 科目コード */ String  cd;
		/** 科目名 */ String  name;
		/** 枝番リスト */ List<KeihiEdaban> edabanList = new ArrayList<>();

		/**
		 * コンストラクタ
		 * @param map 経費明細
		 */
		public KeihiKamoku(GMap map) {
			this.cd = (String)map.get("kamoku_naibu_cd");
			this.name = (String)map.get("kamoku_name_ryakushiki");
		}
	}
	
	/**
	 * 経費明細情報（科目単位）
	 */
	@Getter @Setter @ToString
	public class KeihiEdaban {
		/** 枝番コード */ String  cd;
		/** 枝番名 */ String  name;
		/** 仕訳リスト */ List<GMap> shiwakeList = new ArrayList<>();

		/**
		 * コンストラクタ
		 * @param map 経費明細
		 */
		public KeihiEdaban(GMap map) {
			this.cd = (String)map.get("eda");
			this.name = (String)map.get("edaban_name");
		}
	}

	/**
	 * 「集計部門」のデータを全て取得する。
	 * keihi.kogamen.SyuukeiBumonSentakuActionの集計部門選択の場合に呼び出されるメソッドだが、現在未使用。
	 * @param kesn 内部決算期
	 * @param sptnList セキュリティパターンリスト
	 * @return 検索結果
	 */
	public List<GMap> loadSyuukeiBumon(int kesn, List<Integer> sptnList) {
		
		String whereSpStr = "";
		if(!sptnList.isEmpty()){
			String sptnStr = makeSptnString(sptnList);
			whereSpStr = " AND sec.sptn in (" + sptnStr + ") ";
		}
		
		String sql =
				  "SELECT DISTINCT "
				+ "  sb.syuukei_bumon_cd AS cd, "
				+ "  sb.syuukei_bumon_name AS name "
				+ "FROM ki_syuukei_bumon sb "
				+ "LEFT OUTER JOIN ki_bumon_security sec ON "
				+ "  (sb.kesn, sb.futan_bumon_cd) = (sec.kesn, sec.futan_bumon_cd) "
				+ "INNER JOIN syuukei_bumon s ON "
				+ "  sb.syuukei_bumon_cd = s.syuukei_bumon_cd "
				+ "WHERE "
				+ "  sb.kesn = ? "
				+ whereSpStr
				+ "ORDER BY "
				+ "  sb.syuukei_bumon_cd ";
		return connection.load(sql, kesn);
	}

	/**
	 * 「部門」のデータを全て取得する。
	 * @param kesn 内部決算期
	 * @param sptnList セキュリティパターンリスト
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 検索結果 リスト
	 */
	public List<GMap> loadFutanBumon(int kesn, List<Integer> sptnList, String syuukeiBumonCd) {
		
		String whereSpStr = "";
		if(!sptnList.isEmpty()){
			String sptnStr = makeSptnString(sptnList);
			whereSpStr = " AND sec.sptn in (" + sptnStr + ") ";
		}
	
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT ");
		sql.append("  sb.futan_bumon_cd AS cd, ");
		sql.append("  sb.futan_bumon_name AS name ");
		sql.append("FROM ki_syuukei_bumon sb ");
		sql.append("LEFT OUTER JOIN ki_bumon_security sec ON ");
		sql.append("  (sb.kesn, sb.futan_bumon_cd) = (sec.kesn, sec.futan_bumon_cd)  ");
		sql.append("WHERE ");
		sql.append("  sb.kesn = ? AND ");
		sql.append("  sb.syuukei_bumon_cd = ? ");
		sql.append(whereSpStr);
		sql.append("ORDER BY ");
		sql.append("  sb.futan_bumon_cd ");
		params.add(kesn);
		params.add(syuukeiBumonCd);
		return connection.load(sql.toString(), params.toArray());
	}
	/**
	 * 「集計科目」のデータを全て取得する。
	 * @param ki 決算期
	 * @param sptnList セキュリティパターンリスト
	 * @return 検索結果 リスト
	 */
	public List<GMap> loadKamoku(int ki, List<Integer> sptnList) {
		
		String whereSpStr = "";
		if(!sptnList.isEmpty()){
			String sptnStr = makeSptnString(sptnList);
			whereSpStr = " AND sec.sptn in (" + sptnStr + ") ";
		}
		
		String sql =
				  "select DISTINCT "
				+ "  k.kamoku_gaibu_cd AS cd, "
				+ "  k.kamoku_name_ryakushiki AS name "
				+ "FROM ki_kamoku k "
				+ "LEFT OUTER JOIN ki_kamoku_security sec ON "	// 連絡票No.0995 adminユーザーはセキュリティパターンに関わらず全科目表示させるようにする
				+ "  (k.kesn, k.kamoku_naibu_cd) = (sec.kesn, sec.kamoku_naibu_cd) "
				+ "WHERE "
				+ "  k.kesn = ? AND "
				+ "  k.kamoku_gaibu_cd IS NOT NULL AND k.kamoku_gaibu_cd <> '' "
				+ whereSpStr
				+ "ORDER BY "
				+ "  k.kamoku_gaibu_cd ";
		return connection.load(sql, ki);
	}
	
	/**
	 * セキュリティパターンリストをカンマ区切りの文字列に変換して返却
	 * @param sptnList セキュリティパターンリスト
	 * @return カンマ区切りセキュリティパターン文字列
	 */
	public String makeSptnString(List<Integer> sptnList){
		
		String tmpStr = "";
		if( sptnList != null && !sptnList.isEmpty() ){
			for(Integer sp : sptnList){
				tmpStr = tmpStr + sp.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
		}
		return tmpStr;
		
	}
	
	/** 経費明細件数・一覧のSQL共通部分 */
	static final String KEIHI_SQL_FROM_WHERE =
			//貸借別でSELECTしてUNIONする
			  "FROM "
			     //借方
			+ "  (SELECT "
			+ "    0 AS taishaku_zokusei, "
			+ "    swk.kesn, "
			+ "    swk.dkei, "
			+ "    swk.dseq, "
			+ "    swk.sseq, "
			+ "    swk.dymd, "
			+ "    swk.valu, "
			+ "    swk.rtky tky, "
			+ "    swk.rkmk kmk, "
			+ "    swk.reda eda, "
			+ "    swk.rbmn bmn, "
			+ "    swk.rtor tor, "
			+ "    swk.fway "
			+ "  FROM ki_shiwake swk "
			+ "  WHERE "
			+ "	swk.dymd between ? AND ? "												//① 指定月のFROM-TO
			+ "  UNION "
			     //借方
			+ "  SELECT "
			+ "    1 AS taishaku_zokusei, "
			+ "    swk.kesn, "
			+ "    swk.dkei, "
			+ "    swk.dseq, "
			+ "    swk.sseq, "
			+ "    swk.dymd, "
			+ "    swk.valu, "
			+ "    swk.stky tky, "
			+ "    swk.skmk kmk, "
			+ "    swk.seda eda, "
			+ "    swk.sbmn bmn, "
			+ "    swk.stor tor, "
			+ "    swk.fway "
			+ "  FROM ki_shiwake swk "
			+ "  WHERE "
			+ "	swk.dymd between ? AND ? "												//② 指定月のFROM-TO
			+ "  ) swk "
			
			//科目 JOIN※セキュリティパターンが紐付くものだけ
			+ "INNER JOIN ki_kamoku kmk ON "
			+ "  (swk.kesn, swk.kmk) = (kmk.kesn, kmk.kamoku_naibu_cd) "
			+ "  :SPTNSTR_K "														//③ セキュリティパターン
			
			//部門 JOIN※セキュリティパターンが紐付くものだけ
			+ "INNER JOIN ki_syuukei_bumon bmn ON "
			+ "  bmn.syuukei_bumon_cd = ? AND "										//④ 集計部門
			+ "  (swk.kesn, swk.bmn) = (bmn.kesn, bmn.futan_bumon_cd) "
			+ "  :SPTNSTR_B "														//⑤ セキュリティパターン
			
			//取引先 JOIN
			+ "LEFT OUTER JOIN torihikisaki_master tor ON "
			+ "  swk.tor = tor.torihikisaki_cd "
			
			//(期別)科目枝番 JOIN
			+ "LEFT OUTER JOIN ki_kamoku_edaban ked ON "
			+ "  (swk.kesn, swk.kmk, swk.eda) = (ked.kesn, ked.kamoku_naibu_cd, ked.kamoku_edaban_cd) "
			
			//(期別)部門 直属の親集計部門取得用JOIN
			+ "LEFT OUTER JOIN (SELECT DISTINCT ki_bumon.futan_bumon_cd, ki_bumon.kesn, ki_bumon.oya_syuukei_bumon_cd, ki_syuukei_bumon.syuukei_bumon_name AS oya_syuukei_bumon_name"
			+ " FROM ki_bumon INNER JOIN ki_syuukei_bumon ON (ki_bumon.kesn, ki_bumon.oya_syuukei_bumon_cd) = (ki_syuukei_bumon.kesn, ki_syuukei_bumon.syuukei_bumon_cd) ) kbm ON "
			+ "  (swk.kesn, swk.bmn) = (kbm.kesn, kbm.futan_bumon_cd) "

			//引数指定の集計部門配下の部門のみを取得
			+ "WHERE "
			+ "  1 = 1 "
			+ "  :FROM_KAMOKU "															//⑥swk.kmk >= 科目内部コード
			+ "  :TO_KAMOKU "															//⑦swk.kmk <= 科目内部コード
			+ "  :FROM_FUTAN_BUMON "													//⑧swk.bmn >= 負担部門コード
			+ "  :TO_FUTAN_BUMON "														//⑨swk.bmn <= 負担部門コード
			+ "  :FWAY "; //⑩swk.fway IN (8041, 8042)
	/**
	 * 経費明細SQLの置換を行う。検索条件でSQLを置換する。
	 * 
	 * @param sql 置換前 SQL
	 * @param params SQLパラメータ
	 * @param sptnList セキュリティパターンリスト
	 * @param wfOnly WF限定
	 * @param syuukeiBumonCd 集計部門コード
	 * @param fromFutanBumonCd 負担部門コード(From)
	 * @param toFutanBumonCd 負担部門コード(To)
	 * @param fromKamokuCd 科目コード(FROM)
	 * @param toKamokuCd 科目コード(TO)
	 * @param targetDate 対象日付
	 * @return 置換後 SQL
	 */
	protected String replaceKeihiMeisaiSQLFromWhere(
			String sql,
			List<Object> params,
			List<Integer> sptnList,
			boolean wfOnly,
			String syuukeiBumonCd,
			String fromFutanBumonCd,
			String toFutanBumonCd,
			String fromKamokuCd,
			String toKamokuCd,
			Date targetDate
	) {
		MasterKanriCategoryLogic masterKanriLogic = (MasterKanriCategoryLogic)EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		GMap kesn = masterKanriLogic.findKiKesn(targetDate);
		
		params.add(kesn.get("from_date")); params.add(kesn.get("to_date")); //①
		params.add(kesn.get("from_date")); params.add(kesn.get("to_date")); //②
		//params.add(sptn); //③
		params.add(syuukeiBumonCd); //④
		//params.add(sptn); //⑤
		
		//セキュリティパターンリストが無い場合はセキュリティパターンによる絞り込みをしない(管理者ユーザを想定)
		if( ! sptnList.isEmpty() ){
			String sptnStr = makeSptnString(sptnList);
			sql = sql.replace(":SPTNSTR_K", "INNER JOIN ( SELECT DISTINCT kesn,kamoku_naibu_cd FROM ki_kamoku_security WHERE sptn in ("+ sptnStr +") ) ksec ON " //③
											+ "  (swk.kesn, swk.kmk) = (ksec.kesn, ksec.kamoku_naibu_cd) " );
			
			String setStr = setting.keihimeisaiSecurityPsettei();
			if("1".equals(setStr)){
				sql = sql.replace(":SPTNSTR_B", "INNER JOIN ( SELECT DISTINCT kesn,futan_bumon_cd FROM ki_bumon_security WHERE sptn in ("+ sptnStr +") ) bsec ON "	 //⑤
						+ "  (swk.kesn, swk.bmn) = (bsec.kesn, bsec.futan_bumon_cd) ");
			}else{
				// 経費明細一覧セキュリティパターンが「部門毎」の場合
				// 予算執行一覧と平仄とるため、セキュリティパターンによる部門の絞り込みは行わない
				sql = sql.replace(":SPTNSTR_B", "" );
			}
		}else{
			sql = sql.replace(":SPTNSTR_K", "" );
			sql = sql.replace(":SPTNSTR_B", "" );
		}
		
		if (! isEmpty(fromKamokuCd)){
			sql = sql.replace(":FROM_KAMOKU", "AND kmk.kamoku_gaibu_cd >= ? "); //⑥
			params.add(fromKamokuCd);
		} else {
			sql = sql.replace(":FROM_KAMOKU", "");
		}
		if (! isEmpty(toKamokuCd) ){
			sql = sql.replace(":TO_KAMOKU", "AND kmk.kamoku_gaibu_cd <= ? "); //⑥
			params.add(toKamokuCd);
		} else {
			sql = sql.replace(":TO_KAMOKU", "");
		}
		if (! isEmpty(fromFutanBumonCd) ){
			sql = sql.replace(":FROM_FUTAN_BUMON", "AND swk.bmn >= ? "); //⑦
			params.add(fromFutanBumonCd);
		} else {
			sql = sql.replace(":FROM_FUTAN_BUMON", "");
		}
		if (! isEmpty(toFutanBumonCd) ){
			sql = sql.replace(":TO_FUTAN_BUMON", "AND swk.bmn <= ? "); //⑧
			params.add(toFutanBumonCd);
		} else {
			sql = sql.replace(":TO_FUTAN_BUMON", ""); //⑨
		}
		if (wfOnly) {
			sql = sql.replace(":FWAY", "AND swk.fway IN (8041, 8042)"); //⑩
		} else {
			sql = sql.replace(":FWAY", "");
		}
		return sql;
	}
	
	/**
	 * 経費明細表情報を検索します。
	 * @param sptnList セキュリティパターンリスト
	 * @param wfOnly WF限定
	 * @param syuukeiBumonCd 集計部門コード
	 * @param fromFutanBumonCd 負担部門コード(From)
	 * @param toFutanBumonCd 負担部門コード(To)
	 * @param fromKamokuCd 科目コード(FROM)
	 * @param toKamokuCd 科目コード(TO)
	 * @param targetDate 対象日付
	 * @return 結果
	 */
	public long countKeihiMeisai(
			List<Integer> sptnList,
			boolean wfOnly,
			String syuukeiBumonCd,
			String fromFutanBumonCd,
			String toFutanBumonCd,
			String fromKamokuCd,
			String toKamokuCd,
			Date targetDate
	) {
		
		String sql =
				  "SELECT COUNT(*) count "
				+ KEIHI_SQL_FROM_WHERE;
		List<Object> params = new ArrayList<>();
		sql = replaceKeihiMeisaiSQLFromWhere(sql, params,
				sptnList,
				wfOnly,
				syuukeiBumonCd,
				fromFutanBumonCd,
				toFutanBumonCd,
				fromKamokuCd,
				toKamokuCd,
				targetDate);
		return (long)connection.find(sql, params.toArray()).get("count");
	}
	
	/**
	 * 経費明細表情報を検索します。
	 * @param sptnList セキュリティパターンリスト
	 * @param wfOnly WF限定
	 * @param syuukeiBumonCd 集計部門コード
	 * @param fromFutanBumonCd 負担部門コード(From)
	 * @param toFutanBumonCd 負担部門コード(To)
	 * @param fromKamokuCd 科目コード(FROM)
	 * @param toKamokuCd 科目コード(TO)
	 * @param targetDate 対象日付
	 * @param pageNo ページ番号
	 * @param pageMax 1ページ最大表示件数
	 * @param showBumon 経費明細部門表示フラグ
	 * @param showEdaban 経費明細枝番表示フラグ
	 * @return 結果
	 */
	public List<GMap> loadKiehiMeisai(
			List<Integer> sptnList,
			boolean wfOnly,
			String syuukeiBumonCd,
			String fromFutanBumonCd,
			String toFutanBumonCd,
			String fromKamokuCd,
			String toKamokuCd,
			Date targetDate,
			int pageNo,
			int pageMax,
			boolean showBumon,
			boolean showEdaban
	) {
		String sql =
				  "SELECT "
				+ "  swk.kesn, "
				+ "  swk.dkei, "
				+ "  swk.dseq, "
				+ "  swk.sseq, "
				+ "  swk.dymd, "
				+ "  swk.bmn, "
				+ "  swk.eda, "
				+ "  CASE WHEN (swk.taishaku_zokusei = kmk.taishaku_zokusei) THEN valu ELSE -valu END valu, "
				+ "  :TOTALBMN "	// "  SUM(CASE WHEN (swk.taishaku_zokusei = kmk.taishaku_zokusei) THEN valu ELSE -valu END) OVER (PARTITION BY swk.bmn ) total_bmn, "
				+ "  SUM(CASE WHEN (swk.taishaku_zokusei = kmk.taishaku_zokusei) THEN valu ELSE -valu END) OVER (PARTITION BY kbm.oya_syuukei_bumon_cd, :PTT1 kmk.kamoku_gaibu_cd ) total_kmk, "
				+ "  :TOTALEDA "	// "  SUM(CASE WHEN (swk.taishaku_zokusei = kmk.taishaku_zokusei) THEN valu ELSE -valu END) OVER (PARTITION BY :PTT1 kmk.kamoku_gaibu_cd, swk.eda ) total_eda, "
				+ "  swk.tky, "
				+ "  kmk.kamoku_naibu_cd, "
				+ "  kmk.kamoku_gaibu_cd, "
				+ "  kmk.kamoku_name_ryakushiki, "
				+ "  ked.edaban_name, "
				+ "  bmn.syuukei_bumon_cd, "
				+ "  bmn.syuukei_bumon_name, "
				+ "  bmn.futan_bumon_cd, "
				+ "  bmn.futan_bumon_name, "
				+ "  kbm.oya_syuukei_bumon_cd, "
				+ "  kbm.oya_syuukei_bumon_name, "
				+ "  tor.torihikisaki_cd, "
				+ "  tor.torihikisaki_name_ryakushiki "
				+ KEIHI_SQL_FROM_WHERE
				+ "ORDER BY "
				+ "  kbm.oya_syuukei_bumon_cd, :PTT1 kmk.kamoku_gaibu_cd, :PTT2 swk.dymd, swk.dkei,swk.dseq,swk.sseq,swk.taishaku_zokusei "
				+ ":LIMIT ";

		List<Object> params = new ArrayList<>();
		sql = replaceKeihiMeisaiSQLFromWhere(sql, params,
				sptnList,
				wfOnly,
				syuukeiBumonCd,
				fromFutanBumonCd,
				toFutanBumonCd,
				fromKamokuCd,
				toKamokuCd,
				targetDate);
		if (pageMax > 0) {
			sql = sql.replace(":LIMIT", EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax)); //LIMIT 最大件数 offset 何件目から
		} else {
			sql = sql.replace(":LIMIT", "");
		}
		
		
		
		//会社設定「経費明細一覧に部門を出力する」「経費明細一覧に枝番を出力する」を参照してリスト項目を入替
		//":PTT1"変換のため先に枝番から文字列入れ替え
		if(showEdaban){
			sql = sql.replace(":TOTALEDA", "  SUM(CASE WHEN (swk.taishaku_zokusei = kmk.taishaku_zokusei) THEN valu ELSE -valu END) OVER (PARTITION BY kbm.oya_syuukei_bumon_cd, :PTT1 kmk.kamoku_gaibu_cd, swk.eda ) total_eda, ");
			sql = sql.replace(":PTT2", "swk.eda,");
		}else{
			sql = sql.replace(":TOTALEDA", "");
			sql = sql.replace(":PTT2", "");
		}

		//部門文字列入れ替え
		if(showBumon){
			sql = sql.replace(":TOTALBMN", "  SUM(CASE WHEN (swk.taishaku_zokusei = kmk.taishaku_zokusei) THEN valu ELSE -valu END) OVER (PARTITION BY kbm.oya_syuukei_bumon_cd, swk.bmn ) total_bmn, ");
			sql = sql.replace(":PTT1", "swk.bmn,");
		}else{
			sql = sql.replace(":TOTALBMN", "");
			sql = sql.replace(":PTT1", "");
		}
		
		return connection.load(sql, params.toArray());
	}

	/**
	 * 経費明細のレコードを集計部門-部門-科目-枝番別にする。
	 * 以下の様な構造にする。
	 * ----------------------------
	 * 集計部門１
	 * ┣部門１
	 * ┃ ┣科目１
	 * ┃ ┃ ┣枝番１
	 * ┃ ┃ ┃  ┣項目１
	 * ┃ ┃ ┃  ┗項目N
	 * ┃ ┃ ┗枝番N
	 * ┃ ┃     ┣項目１
	 * ┃ ┃     ┗項目N
	 * ┃ ┗科目N
	 * ┃    ┣枝番１
	 * ┃    ┃  ┣項目１
	 * ┃    ┃  ┗項目N
	 * ┃    ┗枝番N
	 * ┃        ┣項目１
	 * ┃        ┗項目N
	 * ┗部門２
	 *  略
	 * ----------------------------
	 * @param list 経費明細リスト
	 * @param showBumon 経費明細部門表示フラグ
	 * @param showEdaban 経費明細枝番表示フラグ
	 * @return 部門-科目-枝番別にしたもの
	 */
	public List<KeihiSyuukeiBumon> toBumonList(List<GMap> list, boolean showBumon, boolean showEdaban) {

		List<KeihiSyuukeiBumon> ret = new ArrayList<>();
		KeihiSyuukeiBumon syuukeiBumon = null;
		KeihiBumon bumon = null;
		KeihiKamoku kamoku = null;
		KeihiEdaban edaban = null;
		
		GMap bfMap = null;
		for (GMap map : list) {
			if(! sameSyuukeiBumon(bfMap, map) || ret.isEmpty() ){
				syuukeiBumon = new KeihiSyuukeiBumon(map);
				ret.add(syuukeiBumon);
				bumon = new KeihiBumon(map);
				syuukeiBumon.getBumonList().add(bumon);
				kamoku = new KeihiKamoku(map);
				bumon.getKamokuList().add(kamoku);
				edaban = new KeihiEdaban(map);
				kamoku.getEdabanList().add(edaban);
			} else if ( (showBumon && ! sameBumon(bfMap, map))) {
				bumon = new KeihiBumon(map);
				syuukeiBumon.getBumonList().add(bumon);
				kamoku = new KeihiKamoku(map);
				bumon.getKamokuList().add(kamoku);
				edaban = new KeihiEdaban(map);
				kamoku.getEdabanList().add(edaban);
			} else if(! sameKamoku(bfMap, map)) {
				kamoku = new KeihiKamoku(map);
				bumon.getKamokuList().add(kamoku);
				edaban = new KeihiEdaban(map);
				kamoku.getEdabanList().add(edaban);
			} else if(showEdaban && ! sameEdaban(bfMap, map)) {
				edaban = new KeihiEdaban(map);
				kamoku.getEdabanList().add(edaban);
			}
			
			edaban.getShiwakeList().add(map);
			bfMap = map;
		}
		return ret;
	}
	
	/**
	 * 同じ集計部門かどうか調べる
	 * @param bfMap 経費明細レコード１(nullかも)
	 * @param map 経費明細レコード２
	 * @return 同じ部門ならtrue
	 */
	protected boolean sameSyuukeiBumon(GMap bfMap, GMap map) {
		if (bfMap == null)
		{
			return false;
		}
		String bfBcod = (bfMap.get("oya_syuukei_bumon_cd") == null) ? NULL_STRING : (String)bfMap.get("oya_syuukei_bumon_cd");
		String afBcod = (map.get("oya_syuukei_bumon_cd") == null) ? NULL_STRING : (String)map.get("oya_syuukei_bumon_cd");
		return (bfBcod.equals(afBcod));
	}
	
	/**
	 * 同じ部門かどうか調べる
	 * @param bfMap 経費明細レコード１(nullかも)
	 * @param map 経費明細レコード２
	 * @return 同じ部門ならtrue
	 */
	protected boolean sameBumon(GMap bfMap, GMap map) {
		if (bfMap == null)
		{
			return false;
		}
		String bfBcod = (bfMap.get("futan_bumon_cd") == null) ? NULL_STRING : (String)bfMap.get("futan_bumon_cd");
		String afBcod = (map.get("futan_bumon_cd") == null) ? NULL_STRING : (String)map.get("futan_bumon_cd");
		return (bfBcod.equals(afBcod));
	}

	/**
	 * 同じ科目かどうか調べる
	 * @param bfMap 経費明細レコード１(nullかも)
	 * @param map 経費明細レコード２
	 * @return 同じ科目ならtrue
	 */
	protected boolean sameKamoku(GMap bfMap, GMap map) {
		if (bfMap == null)
		{
			return false;
		}
		String bfKicd = (bfMap.get("kamoku_naibu_cd") == null) ? NULL_STRING : (String)bfMap.get("kamoku_naibu_cd");
		String afKicd = (map.get("kamoku_naibu_cd") == null) ? NULL_STRING : (String)map.get("kamoku_naibu_cd");
		return (bfKicd.equals(afKicd));
	}
	
	/**
	 * 同じ枝番かどうか調べる
	 * @param bfMap 経費明細レコード１(nullかも)
	 * @param map 経費明細レコード２
	 * @return 同じ枝番ならtrue
	 */
	protected boolean sameEdaban(GMap bfMap, GMap map) {
		if (bfMap == null)
		{
			return false;
		}
		String bfKicd = (bfMap.get("eda") == null) ? NULL_STRING : (String)bfMap.get("eda");
		String afKicd = (map.get("eda") == null) ? NULL_STRING : (String)map.get("eda");
		return (bfKicd.equals(afKicd));
	}
}
