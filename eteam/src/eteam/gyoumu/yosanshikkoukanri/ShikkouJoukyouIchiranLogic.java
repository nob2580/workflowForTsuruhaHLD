package eteam.gyoumu.yosanshikkoukanri;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.SettingInfoAbstractDao;
import eteam.database.dao.SettingInfoDao;


/**
 * 執行状況一覧に関するロジック
 *
 */
public class ShikkouJoukyouIchiranLogic extends EteamAbstractLogic {
	
	/** マスター管理カテゴリーロジック */
	protected MasterKanriCategoryLogic masterLogic ;
	/** 予算執行管理カテゴリーロジック */
	protected YosanShikkouKanriCategoryLogic commonLogic ;
	
	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, _connection);
		commonLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, _connection);
	}
	
	/**
	 * 執行状況情報を検索します。
	 * @param isKamokuTani チェック粒度
	 * @param isRuikei 予算単位
	 * @param sptn セキュリティパターン
	 * @param syuukeiBumonCd 集計部門コード
	 * @param fromFutanBumonCd 負担部門コード(From)
	 * @param toFutanBumonCd 負担部門コード(To)
	 * @param fromKamokuCd 科目コード(From)
	 * @param toKamokuCd 科目コード(To)
	 * @param yosanTani 予算単位
	 * @param targetDate 対象日付
	 * @return 結果
	 */
	public long countShikkouJoukyou(
			boolean isKamokuTani,
			boolean isRuikei,
			int sptn,
			String syuukeiBumonCd,
			String fromFutanBumonCd,
			String toFutanBumonCd,
			String fromKamokuCd,
			String toKamokuCd,
			String yosanTani,
			Date targetDate
	) {
		
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<>();
		
		//WITH句
		sb.append(shikkouJyoukyouWith(isKamokuTani, isRuikei, params, sptn, targetDate));
		
		//▼▼SQL本体
		sb.append("SELECT COUNT(*) count ");
		sb.append(shikkouJyoukyouSqlFromWhere(isKamokuTani, params, targetDate));
		String sql = replaceShikkouJoukyouSQLFromWhere(
						 sb.toString()
						,syuukeiBumonCd
						,fromFutanBumonCd
						,toFutanBumonCd
						,fromKamokuCd
						,toKamokuCd
						,yosanTani
						,targetDate);
				
		return (long)connection.find(sql, params.toArray()).get("count");
	}
	
	/**
	 * @param isKamokuTani チェック粒度
	 * @param isRuikei 予算単位
	 * @param sptn セキュリティパターン
	 * @param syuukeiBumonCd 集計部門コード
	 * @param fromFutanBumonCd 負担部門コード(From)
	 * @param toFutanBumonCd 負担部門コード(To)
	 * @param fromKamokuCd 科目コード(From)
	 * @param toKamokuCd 科目コード(To)
	 * @param yosanTani 予算単位
	 * @param targetDate 対象日付
	 * @param pageNo ページ番号
	 * @param pageMax 1ページ最大表示件数
	 * @return 結果
	 */
	public List<GMap> loadShikkouJoukyou(
			boolean isKamokuTani,
			boolean isRuikei,
			int sptn,
			String syuukeiBumonCd,
			String fromFutanBumonCd,
			String toFutanBumonCd,
			String fromKamokuCd,
			String toKamokuCd,
			String yosanTani,
			Date targetDate,
			int pageNo,
			int pageMax
	){
		List<Object> params = new ArrayList<>();
		StringBuffer sb = new StringBuffer();

		//WITH句
		sb.append(shikkouJyoukyouWith(isKamokuTani, isRuikei, params, sptn, targetDate));
		
		//▼▼SQL本体
		sb.append("SELECT");
		//集計部門コード
		sb.append("  SB.syuukei_bumon_cd AS syuukei_bumon_cd,");
		//集計部門名
		sb.append("  SB.syuukei_bumon_name AS syuukei_bumon_name,");
		//明細部門コード
		sb.append("  SB.meisai_bumon_cd AS meisai_bumon_cd, ");
		//明細部門名
		sb.append("  SB.meisai_bumon_name AS meisai_bumon_name,");
		if(isKamokuTani) {
			//部門科目単位で集計する場合のみ
			//科目コード
			sb.append("  KM.kamoku_gaibu_cd,");
			//科目名称
			sb.append("  KM.kamoku_name_ryakushiki,");
		}
		//当年度予算
		sb.append("  COALESCE(YR.yosan, '0') AS yosan,");
		//累計執行高
		sb.append("  COALESCE(YR.ruikei_shikkoudaka, '0') AS ruikei_shikkoudaka,");
		//みなし実績
		sb.append("  COALESCE(MJ.kingaku, '0') AS minasi_shikkoudaka,");
		//執行高合計
		sb.append("  COALESCE(YR.ruikei_shikkoudaka, '0') + COALESCE(MJ.kingaku, '0') AS shikkoudaka_goukei,");
		//予算残
		sb.append("  COALESCE(YR.yosan, '0') - (COALESCE(YR.ruikei_shikkoudaka, '0') + COALESCE(MJ.kingaku, '0')) AS yosanzan,");
		sb.append("  CASE WHEN COALESCE(YR.yosan, '0') = '0' THEN 100.00");
		//執行率
		sb.append("  ELSE trunc(((COALESCE(YR.ruikei_shikkoudaka, '0') + COALESCE(MJ.kingaku, '0')) / YR.yosan * 100), 2) END AS rate");
		
		//FROM句-WHERE句
		sb.append(shikkouJyoukyouSqlFromWhere(isKamokuTani, params, targetDate));
		
		sb.append(" ORDER BY ");
		sb.append("   syuukei_bumon_cd, meisai_bumon_cd");
		if(isKamokuTani) //部門科目単位で集計する場合のみ、科目外部コードも
		sb.append("                                    , kamoku_gaibu_cd");
		sb.append(":LIMIT ");
		
		String  sql = replaceShikkouJoukyouSQLFromWhere(
						 sb.toString()
						,syuukeiBumonCd
						,fromFutanBumonCd
						,toFutanBumonCd
						,fromKamokuCd
						,toKamokuCd
						,yosanTani
						,targetDate);

		if (pageMax > 0) {
			sql = sql.replace(":LIMIT", EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax)); //LIMIT 最大件数 offset 何件目から
		} else {
			sql = sql.replace(":LIMIT", "");
		}
		
		return connection.load(sql, params.toArray());
	}
	
	/**
	 * 執行状況一覧の検索結果取得用WITH句
	 * @param isKamokuTani チェック粒度
	 * @param isRuikei 累計予算フラグ
	 * @param params SQLパラメータ
	 * @param sptn   		セキュリティパターン
	 * @param targetDate 対象日付
	 * @return 執行状況一覧の検索結果取得用WITH句
	 */
	protected String shikkouJyoukyouWith(boolean isKamokuTani, boolean isRuikei, List<Object> params, int sptn, Date targetDate) {
		
		int kesn = masterLogic.findKesn(targetDate);
		int kessankiBangou = masterLogic.findKessankiBangou(targetDate);
		Date fromDate = masterLogic.findFromDate(kesn);
		Date toDate = masterLogic.findToDate(kesn, targetDate);
		StringBuffer sb = new StringBuffer();
		
		// 起案終了フラグ
		sb.append("WITH RECURSIVE kiansyuryo AS (");
		sb.append("SELECT denpyou_id, kian_denpyou, kian_syuryo_flg FROM denpyou_kian_himozuke");
		sb.append(" UNION ALL");
		sb.append("  SELECT kiansyuryo.denpyou_id, kiantnpu.kian_denpyou, kiantnpu.kian_syuryo_flg FROM denpyou_kian_himozuke kiantnpu, kiansyuryo");
		sb.append("   WHERE kiantnpu.denpyou_id = kiansyuryo.kian_denpyou)");
		
		//部署入出力仕訳の部門・科目・金額の貸借を共通項目化
		sb.append("    ,base_busho_shiwake AS (" + getBaseBushoShiwake() + ") ");
		params.add(fromDate); params.add(toDate);
		params.add(fromDate); params.add(toDate);
		
		//予算
		sb.append("    ,main_yosan AS (" + getYosanSql(kesn, targetDate, isKamokuTani, isRuikei) + ") ");
		params.add(kessankiBangou);
		params.add(kessankiBangou);
		params.add(kessankiBangou);
		params.add(kesn);
		params.add(sptn);
		
		//累計
		sb.append("    ,main_ruikei AS (" + getRuikeijissekiSql(kesn, targetDate, isKamokuTani) + ") ");
		params.add(kesn);
		params.add(sptn);
		params.add(kessankiBangou);
		params.add(kesn);
		params.add(sptn);
		
		//伝票毎の承認状況の最新更新日
		sb.append("     ,max_shounin_jyoukyou_time AS (" + getMaxShouninJyoukyouTimeSql() + ") ");
		
		if(isRuikei) {
			//伝票毎の基準日
			sb.append("     ,kijunbi AS (" + getKijunbi() + ") ");
		}
		
		//仕訳抽出（財務転記済）
		sb.append("     ,shiwake_zaimu_renkeizumi AS (" + getShiwakeZaimuRenkeizumiSql() + ")");
		
		//WFみなし実績
		sb.append("    ,base_minashi  AS (" + getMinashiJissekiSql(kesn, sptn, targetDate ,isKamokuTani, isRuikei) + ") ");
		SimpleDateFormat format = new SimpleDateFormat("YYYYMM");
		Date startToDate = masterLogic.findToDate(kesn, fromDate);
		// 起案添付なし支出依頼ここから
		params.add(kesn);
		params.add(targetDate);
		if(isRuikei) {
			params.add(format.format(startToDate)); // 期首年月
			params.add(format.format(targetDate)); // 対象年月
		}
		// ここまで
		// 実施起案ここから
		params.add(kesn);
		params.add(targetDate);
		if(isRuikei) {
			params.add(format.format(startToDate)); // 期首年月
			params.add(format.format(targetDate)); // 対象年月
		}
		params.add(kesn);
		// ここまで
		// 起案添付あり支出依頼ここから
		params.add(kesn);
		params.add(targetDate);
		params.add(kesn);
		// ここまで
		
		//WFみなし実績
		sb.append("    ,main_minashi  AS (");
		sb.append("                     SELECT");
		sb.append("                       futan_bumon_cd,");
		if(isKamokuTani) sb.append("   kamoku_gaibu_cd,");
		sb.append("                       SUM(kingaku) AS kingaku");
		sb.append("                      FROM");
		sb.append("                       base_minashi");
		sb.append("                      GROUP BY futan_bumon_cd");
		if(isKamokuTani) sb.append("          ,kamoku_gaibu_cd");
		sb.append("                                         ) ");
		
		
		//集計部門と明細部門
		sb.append("    ,main_bumon AS (");
		sb.append("                     SELECT");
		sb.append("                       ksb.syuukei_bumon_cd AS syuukei_bumon_cd,");
		sb.append("                       ksb.syuukei_bumon_name AS syuukei_bumon_name,");
		sb.append("                       ksb.futan_bumon_cd AS meisai_bumon_cd,");
		sb.append("                       ksb.futan_bumon_name AS meisai_bumon_name");
		sb.append("                      FROM");
		sb.append("                       ki_syuukei_bumon ksb");
										//集計部門が予算執行対象のもののみに絞り込み（明細部門が予算執行対象かどうかは不問）
		sb.append("                      INNER JOIN ki_bumon_security kbs ON kbs.kesn = ksb.kesn AND kbs.futan_bumon_cd = ksb.syuukei_bumon_cd");
		sb.append("                      INNER JOIN syuukei_bumon sbm ON sbm.syuukei_bumon_cd = ksb.syuukei_bumon_cd");
		sb.append("                      WHERE kbs.kesn = ?");
		params.add(kesn);
		sb.append("                        AND kbs.sptn = ?");
		params.add(sptn);
		sb.append("                    ) ");
		
		return sb.toString();
	}

	/**
	 * 執行状況一覧の検索結果取得用FROM句-WHERE句
	 * @param isKamokuTani チェック粒度
	 * @param params SQLパラメータ
	 * @param targetDate 対象日付
	 * @return 執行状況一覧の検索結果取得用FROM句-WHERE句
	 */
	protected String shikkouJyoukyouSqlFromWhere(boolean isKamokuTani, List<Object> params, Date targetDate) {
		
		int kesn = masterLogic.findKesn(targetDate);
		int kessankiBangou = masterLogic.findKessankiBangou(targetDate);
		StringBuffer sb = new StringBuffer();
		
		sb.append(" FROM");
		sb.append("     (SELECT");
		sb.append("        CASE WHEN Y.futan_bumon_cd IS NOT NULL THEN Y.futan_bumon_cd ELSE R.meisai_bumon_cd END AS futan_bumon_cd,");
		if(isKamokuTani) //部門科目単位で集計する場合のみ科目外部コード
		sb.append("        CASE WHEN Y.kamoku_gaibu_cd IS NOT NULL THEN Y.kamoku_gaibu_cd ELSE R.kamoku_gaibu_cd END AS kamoku_gaibu_cd,");
		sb.append("        Y.yosan AS yosan,");
		sb.append("        R.ruikei_shikkoudaka AS ruikei_shikkoudaka");
		
		if(isKamokuTani){
			//予算と累計実績の集計結果
			sb.append("       FROM");
			sb.append("         main_yosan Y");
			sb.append("        FULL OUTER JOIN main_ruikei R ON R.meisai_bumon_cd = Y.futan_bumon_cd AND R.kamoku_gaibu_cd = Y.kamoku_gaibu_cd) YR");
			//みなし実績の集計結果
			sb.append("     FULL OUTER JOIN main_minashi MJ ON MJ.futan_bumon_cd = YR.futan_bumon_cd AND MJ.kamoku_gaibu_cd = YR.kamoku_gaibu_cd");
			//集計部門と明細部門の紐づけ
			sb.append("     INNER JOIN main_bumon SB ON SB.meisai_bumon_cd = YR.futan_bumon_cd OR SB.meisai_bumon_cd = MJ.futan_bumon_cd");
			//科目マスター
			sb.append("     INNER JOIN kamoku_master KM ON KM.kamoku_gaibu_cd = YR.kamoku_gaibu_cd OR KM.kamoku_gaibu_cd = MJ.kamoku_gaibu_cd");
			sb.append("     WHERE (SB.meisai_bumon_cd, KM.kamoku_gaibu_cd) IN (SELECT DISTINCT futan_bumon_cd, kamoku_gaibu_cd FROM bumon_kamoku_yosan  WHERE kessanki_bangou = ?)");
			params.add(kessankiBangou);
			sb.append("        OR (SB.meisai_bumon_cd, KM.kamoku_gaibu_cd) IN (SELECT DISTINCT futan_bumon_cd, kamoku_gaibu_cd FROM bumon_kamoku_edaban_yosan  WHERE kessanki_bangou = ?)");
			params.add(kessankiBangou);
			sb.append("        OR (SB.meisai_bumon_cd, KM.kamoku_gaibu_cd) IN (SELECT DISTINCT futan_bumon_cd, kamoku_gaibu_cd FROM bumon_kamoku_zandaka  WHERE kessanki_bangou = ?)");
			params.add(kessankiBangou);
			sb.append("        OR (SB.meisai_bumon_cd, KM.kamoku_naibu_cd) IN (SELECT DISTINCT futan_bumon_cd, kamoku_naibu_cd FROM base_busho_shiwake BS WHERE kesn = ?)");
			params.add(kesn);
		}else {
			sb.append("       FROM");
			sb.append("        main_yosan Y");
			//予算と累計実績の集計結果
			sb.append("        FULL OUTER JOIN main_ruikei R ON R.meisai_bumon_cd = Y.futan_bumon_cd) YR");
			//みなし実績の集計結果
			sb.append("     FULL OUTER JOIN main_minashi MJ ON MJ.futan_bumon_cd = YR.futan_bumon_cd");
			//集計部門と明細部門の紐づけ
			sb.append("     INNER JOIN main_bumon SB ON SB.meisai_bumon_cd = YR.futan_bumon_cd OR SB.meisai_bumon_cd = MJ.futan_bumon_cd");
			sb.append("     WHERE SB.meisai_bumon_cd IN (SELECT DISTINCT futan_bumon_cd FROM bumon_kamoku_yosan  WHERE kessanki_bangou = ?)");
			params.add(kessankiBangou);
			sb.append("        OR SB.meisai_bumon_cd IN (SELECT DISTINCT futan_bumon_cd FROM bumon_kamoku_edaban_yosan  WHERE kessanki_bangou = ?)");
			params.add(kessankiBangou);
			sb.append("        OR SB.meisai_bumon_cd IN (SELECT DISTINCT futan_bumon_cd FROM bumon_kamoku_zandaka  WHERE kessanki_bangou = ?)");
			params.add(kessankiBangou);
			sb.append("        OR SB.meisai_bumon_cd IN (SELECT DISTINCT futan_bumon_cd FROM base_busho_shiwake BS WHERE kesn = ?)");
			params.add(kesn);
		}
		
		return sb.toString();
	}
	
	/**
	 * 検索条件でSQLを置換する。
	 * 
	 * @param sql 置換前 SQL
	 * @param syuukeiBumonCd 集計部門コード
	 * @param fromFutanBumonCd 負担部門コード(From)
	 * @param toFutanBumonCd 負担部門コード(To)
	 * @param fromKamokuCd 科目コード(From)
	 * @param toKamokuCd 科目コード(To)
	 * @param yosanTani 予算単位
	 * @param targetDate 対象日付
	 * @return 置換後 SQL
	 */
	protected String replaceShikkouJoukyouSQLFromWhere(
			String sql,
			String syuukeiBumonCd,
			String fromFutanBumonCd,
			String toFutanBumonCd,
			String fromKamokuCd,
			String toKamokuCd,
			String yosanTani,
			Date targetDate
	) {
		
		int kesn = masterLogic.findKesn(targetDate);
		
		//集計部門に属する明細部門を取得
		List<GMap> meisaiBumonList = getMeisaiBumon(kesn, syuukeiBumonCd);
		StringBuffer sb = new StringBuffer();
		for(GMap map :meisaiBumonList){
			String meisaiBumon = (String)map.get("futan_bumon_cd");
			if(sb.length() != 0) sb.append(",");
			sb.append("'");
			sb.append(meisaiBumon);
			sb.append("'");
		}
		//集計部門指定
		String andSyuukeiBumon     = " AND futan_bumon_cd IN (" + sb.toString() + ")";
		if(sb.length() == 0){
			sql = sql.replace(":AND_SYUUKEI_BUMON",      "AND 1 <> 1 ");
		}else{
			sql = sql.replace(":AND_SYUUKEI_BUMON",      andSyuukeiBumon);
		}
		
		//開始負担部門指定
		String andFromFutanBumon     = " AND futan_bumon_cd >= '" + fromFutanBumonCd + "'";
		if (! isEmpty(fromFutanBumonCd) ){
			sql = sql.replace(":AND_FROM_FUTAN_BUMON",      andFromFutanBumon);
		} else {
			sql = sql.replace(":AND_FROM_FUTAN_BUMON",      "");
		}
		
		//終了負担部門指定
		String andToFutanBumon       = " AND futan_bumon_cd <= '" + toFutanBumonCd + "'";
		if (! isEmpty(toFutanBumonCd) ){
			sql = sql.replace(":AND_TO_FUTAN_BUMON",      andToFutanBumon);
		} else {
			sql = sql.replace(":AND_TO_FUTAN_BUMON",      "");
		}
		
		//開始科目指定
		if(! isEmpty(fromKamokuCd)) {
			sql = sql.replace(":AND_FROM_KAMOKU", " AND kamoku_gaibu_cd >= '" + fromKamokuCd + "'");
			sql = sql.replace(":AND_FROM_KM_KAMOKU", " AND km.kamoku_gaibu_cd >= '" + fromKamokuCd + "'");
		}else {
			sql = sql.replace(":AND_FROM_KAMOKU", "");
			sql = sql.replace(":AND_FROM_KM_KAMOKU", "");
		}
		
		//終了科目指定
		//開始科目指定
		if(! isEmpty(toKamokuCd)) {
			sql = sql.replace(":AND_TO_KAMOKU", " AND kamoku_gaibu_cd <= '" + toKamokuCd + "'");
			sql = sql.replace(":AND_TO_KM_KAMOKU", " AND km.kamoku_gaibu_cd <= '" + toKamokuCd + "'");
		}else {
			sql = sql.replace(":AND_TO_KAMOKU", "");
			sql = sql.replace(":AND_TO_KM_KAMOKU", "");
		}
		
		
		return sql;
	}
	
	/**
	 * 集計部門と決算期を指定して、（期別）集計部門テーブルから明細部門コードを取得する
	 * @param kesn 決算期
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 明細部門コードリスト
	 */
	protected List<GMap> getMeisaiBumon(int kesn, String syuukeiBumonCd){
		final String sql = "SELECT futan_bumon_cd FROM ki_syuukei_bumon WHERE kesn = ? AND syuukei_bumon_cd = ?";
		List<Object> params = new ArrayList<>();
		params.add(kesn);
		params.add(syuukeiBumonCd);
		return connection.load(sql, params.toArray());
	}
	
	/**
	 * 期首から対象日付を含む年月までの経過月を取得する
	 * @param kesn 決算期
	 * @param targetDate 対象日付
	 * @return 期首～対象日付を含む年月までの経過月数
	 */
	protected int getCountKeikaTsuki(int kesn, Date targetDate){
		final String sql = "SELECT count(*) AS count FROM ki_kesn WHERE kesn = ? AND from_date <= ?";
		List<Object> params = new ArrayList<>();
		params.add(kesn);
		params.add(targetDate);
		GMap record = connection.find(sql, params.toArray());
		return (record == null) ? 0 : Integer.parseInt((record.get("count")).toString());
	}
	
	/**
	 * 実施起案または支出依頼として登録された伝票区分リストを取得する
	 * @return 伝票区分リスト
	 */
	protected List<GMap> getYosanKnariTaishou(){
		final String sql = "SELECT denpyou_kbn FROM denpyou_shubetsu_ichiran WHERE yosan_shikkou_taishou = 'A' OR yosan_shikkou_taishou = 'C'";
		return connection.load(sql);
	}
	
	/**
	 * 予算執行項目が登録された項目名を取得する。
	 * @param denpyouKbn 伝票区分
	 * @param koumokuId 予算執行項目ID
	 * @return 項目名
	 */
	protected List<GMap> getkanitodokeItemName(String denpyouKbn, String koumokuId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.version,");
		sb.append("       A.item_name");
		sb.append(" FROM kani_todoke_koumoku AS A");
		sb.append(" INNER JOIN kani_todoke AS B ON B.denpyou_kbn = A.denpyou_kbn AND B.version = A.version");
		sb.append(" WHERE A.denpyou_kbn = ? AND A.yosan_shikkou_koumoku_id = ?");
		sb.append(" GROUP BY A.version, A.item_name");
		List<Object> params = new ArrayList<>();
		params.add(denpyouKbn);
		params.add(koumokuId);
		return  connection.load(sb.toString(), params.toArray());
	}
	
	/**
	 * 予算執行項目が登録された項目名を取得する。
	 * @param denpyouKbn 伝票区分
	 * @param koumokuId 予算執行項目ID
	 * @param version ユーザー定義届書ヴァージョン
	 * @return 項目名
	 */
	protected String getkanitodokeItemName(String denpyouKbn, String koumokuId, int version){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.item_name");
		sb.append(" FROM kani_todoke_koumoku AS A");
		sb.append(" INNER JOIN kani_todoke AS B ON B.denpyou_kbn = A.denpyou_kbn AND B.version = A.version");
		sb.append(" WHERE A.denpyou_kbn = ? AND A.yosan_shikkou_koumoku_id = ?");
		sb.append("   AND A.version = ?");
		sb.append(" GROUP BY A.item_name");
		List<Object> params = new ArrayList<>();
		params.add(denpyouKbn);
		params.add(koumokuId);
		params.add(version);
		GMap record =   connection.find(sb.toString(), params.toArray());
		return (record == null) ? "" : (record.get("item_name")).toString();
	}
	
	
	/**
	 * 予算取得用のSQLを組み立てる
	 * @param kesn 決算期
	 * @param targetDate 対象日付
	 * @param isKamokuTani チェック粒度
	 * @param isRuikei 予算単位
	 * @return SQL
	 */
	protected String getYosanSql(int kesn, Date targetDate, boolean isKamokuTani, boolean isRuikei){
		
		// 予算の計算式を組み立てる
		StringBuffer sumYosan = new StringBuffer("0");
		int keikaTsuki = 12;
		// 予算単位が『累計予算』の場合
		// 対象月から求めた経過月で上書き
		if(isRuikei) {
			keikaTsuki = commonLogic.getCountKeikaTsuki(kesn, targetDate);
		}
		for(int i = 0 ; i < keikaTsuki ;){
			i++;
			sumYosan.append(" + COALESCE(SUM(yosan_" + String.format("%02d", i) + "), '0')");
			if(0 == i % 3) {
				sumYosan.append(" + COALESCE(SUM(yosan_" + String.format("%02d", i) + "_shu), '0')");
			}
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  futan_bumon_cd,");
		if(isKamokuTani)
		sb.append("  yosan.kamoku_gaibu_cd,"); // 部門科目単位で集計する場合
		sb.append("  SUM(yosan) AS yosan");
		sb.append(" FROM");
					//部門科目予算
		sb.append("  (SELECT");
		sb.append("     futan_bumon_cd,");
		sb.append("     kamoku_gaibu_cd,");
		sb.append("     '' AS edaban_code,");
		sb.append("     :SUM_YOSAN AS yosan"); // 計算式で置き換える
		sb.append("    FROM");
		sb.append("     bumon_kamoku_yosan");
		sb.append("    WHERE");
		sb.append("         kessanki_bangou = ?");
		sb.append("     :AND_SYUUKEI_BUMON");
		sb.append("     :AND_FROM_FUTAN_BUMON");
		sb.append("     :AND_TO_FUTAN_BUMON");
		sb.append("     :AND_FROM_KAMOKU");
		sb.append("     :AND_TO_KAMOKU");
						//部門科目予算から部門科目枝番予算の重複分を除外
		sb.append("     AND (futan_bumon_cd, kamoku_gaibu_cd) NOT IN ");
		sb.append("      (SELECT");
		sb.append("          futan_bumon_cd,");
		sb.append("         kamoku_gaibu_cd");
		sb.append("        FROM bumon_kamoku_edaban_yosan");
		sb.append("        WHERE");
		sb.append("             kessanki_bangou = ?");
		sb.append("         :AND_SYUUKEI_BUMON");
		sb.append("         :AND_FROM_FUTAN_BUMON");
		sb.append("         :AND_TO_FUTAN_BUMON");
		sb.append("         :AND_FROM_KAMOKU");
		sb.append("         :AND_TO_KAMOKU");
		sb.append("        GROUP BY futan_bumon_cd, kamoku_gaibu_cd)");
		sb.append("     GROUP BY futan_bumon_cd, kamoku_gaibu_cd");
		sb.append("   UNION ALL");
					//部門科目枝番予算
		sb.append("   SELECT");
		sb.append("     futan_bumon_cd,");
		sb.append("     kamoku_gaibu_cd,");
		sb.append("     edaban_code,");
		sb.append("     :SUM_YOSAN AS yosan"); // 計算式で置き換える
		sb.append("    FROM");
		sb.append("     bumon_kamoku_edaban_yosan");
		sb.append("    WHERE");
		sb.append("         kessanki_bangou = ?");
		sb.append("     :AND_SYUUKEI_BUMON");
		sb.append("     :AND_FROM_FUTAN_BUMON");
		sb.append("     :AND_TO_FUTAN_BUMON");
		sb.append("     :AND_FROM_KAMOKU");
		sb.append("     :AND_TO_KAMOKU");
		sb.append("    GROUP BY futan_bumon_cd, kamoku_gaibu_cd, edaban_code");
		sb.append("   ) yosan");
					//予算執行対象外科目は除外
		sb.append(" INNER JOIN kamoku_master km ON yosan.kamoku_gaibu_cd = km.kamoku_gaibu_cd");
		sb.append(" INNER JOIN ki_kamoku_security ksec ON km.kamoku_naibu_cd = ksec.kamoku_naibu_cd AND ksec.kesn = ?  AND ksec.sptn = ?");
		sb.append(" GROUP BY futan_bumon_cd");
		if(isKamokuTani)
		sb.append("         ,yosan.kamoku_gaibu_cd"); // 部門科目単位で集計する場合
		
		return sb.toString().replace(":SUM_YOSAN", sumYosan.toString());
	}
	
	/**
	 * 部署入出力仕訳の部門・科目・金額の貸借を共通項目化するSQL
	 * @return sql
	 */
	public String getBaseBushoShiwake() {
		StringBuffer sb = new StringBuffer();
		//借方
		sb.append(" SELECT");
		sb.append("   kesn");
		sb.append("  ,SUM(valu) AS valu");
		sb.append("  ,rkmk AS kamoku_naibu_cd");
		sb.append("  ,reda AS edaban_code");
		sb.append("  ,rbmn AS futan_bumon_cd");
		sb.append(" FROM ki_busho_shiwake");
		sb.append(" WHERE");
		sb.append("       dymd BETWEEN ? AND ?");
		sb.append(" GROUP BY kesn, rkmk, reda, rbmn");
		sb.append(" UNION ALL");
		//貸方
		sb.append(" SELECT");
		sb.append("   kesn");
		sb.append("  ,0 - SUM(valu) AS valu");
		sb.append("  ,skmk AS kamoku_naibu_cd");
		sb.append("  ,seda AS edaban_code");
		sb.append("                          ,sbmn AS futan_bumon_cd");
		sb.append(" FROM ki_busho_shiwake");
		sb.append(" WHERE");
		sb.append("       dymd BETWEEN ? AND ?");
		sb.append(" GROUP BY kesn, skmk, seda, sbmn");
		return sb.toString();
	}
	
	/**
	 * 累計実績取得用SQLを組み立てる
	 * @param kesn 決算期
	 * @param targetDate 対象日付
	 * @param isKamokuTani チェック粒度
	 * @return SQL
	 */
	protected String getRuikeijissekiSql(int kesn, Date targetDate, boolean isKamokuTani){
		
		//残高の計算式を作成
		int keikaTsuki = getCountKeikaTsuki(kesn, targetDate);
		StringBuffer sumZandaka = new StringBuffer("COALESCE(SUM(zandaka0), '0')");
		for(int i = 0 ; i < keikaTsuki ; i++ ){
			sumZandaka.append(" + ");
			sumZandaka.append("COALESCE(SUM(zandaka" + (i+1) + "), '0')");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  meisai_bumon_cd,");
		if(isKamokuTani)
		sb.append("  kamoku_gaibu_cd_tmp AS kamoku_gaibu_cd,"); // 部門科目単位で集計する場合
					//対象年月までの残高と部署入出仕訳額の合計
		sb.append("  " + sumZandaka.toString()  + " + COALESCE(SUM(ruikei_kingaku_shiwake), '0') AS ruikei_shikkoudaka");
		sb.append(" FROM");
					
		sb.append(" (SELECT");
		sb.append("    CASE WHEN bkz.futan_bumon_cd IS NOT NULL THEN bkz.futan_bumon_cd ELSE ks.futan_bumon_cd END AS meisai_bumon_cd,");
		sb.append("    CASE WHEN bkz.kamoku_gaibu_cd IS NOT NULL THEN bkz.kamoku_gaibu_cd ELSE ks.kamoku_gaibu_cd END AS kamoku_gaibu_cd_tmp,");
		sb.append("    bkz.*,");
		sb.append("    ks.*");
		sb.append("  FROM");
					//部門科目残高
		sb.append("   (SELECT");
		sb.append("      futan_bumon_cd,");
		sb.append("      bkz_tmp.kamoku_gaibu_cd,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari00 - zandaka_kashi00 ELSE zandaka_kashi00 - zandaka_kari00 END) AS zandaka0,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari01 - zandaka_kashi01 ELSE zandaka_kashi01 - zandaka_kari01 END) AS zandaka1,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari02 - zandaka_kashi02 ELSE zandaka_kashi02 - zandaka_kari02 END) AS zandaka2,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari03 + zandaka_kari03_shu - zandaka_kashi03 - zandaka_kashi03_shu ELSE zandaka_kashi03 + zandaka_kashi03_shu - zandaka_kari03 - zandaka_kari03_shu  END) AS zandaka3,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari04 - zandaka_kashi04 ELSE zandaka_kashi04 - zandaka_kari04 END) AS zandaka4,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari05 - zandaka_kashi05 ELSE zandaka_kashi05 - zandaka_kari05 END) AS zandaka5,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari06 + zandaka_kari06_shu - zandaka_kashi06 - zandaka_kashi06_shu ELSE zandaka_kashi06 + zandaka_kashi06_shu - zandaka_kari06 - zandaka_kari06_shu  END) AS zandaka6,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari07 - zandaka_kashi07 ELSE zandaka_kashi07 - zandaka_kari07 END) AS zandaka7,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari08 - zandaka_kashi08 ELSE zandaka_kashi08 - zandaka_kari08 END) AS zandaka8,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari09 + zandaka_kari09_shu - zandaka_kashi09 - zandaka_kashi09_shu ELSE zandaka_kashi09 + zandaka_kashi09_shu - zandaka_kari09 - zandaka_kari09_shu  END) AS zandaka9,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari10 - zandaka_kashi10 ELSE zandaka_kashi10 - zandaka_kari10 END) AS zandaka10,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari11 - zandaka_kashi11 ELSE zandaka_kashi11 - zandaka_kari11 END) AS zandaka11,");
		sb.append("      SUM(CASE WHEN bkz_tmp.taishaku_zokusei = '0' THEN zandaka_kari12 + zandaka_kari12_shu - zandaka_kashi12 - zandaka_kashi12_shu ELSE zandaka_kashi12 + zandaka_kashi12_shu - zandaka_kari12 - zandaka_kari12_shu  END) AS zandaka12");
		sb.append("    FROM ");
		sb.append("      bumon_kamoku_zandaka bkz_tmp");
					//予算執行対象外科目は除外
		sb.append("    INNER JOIN kamoku_master km ON bkz_tmp.kamoku_gaibu_cd = km.kamoku_gaibu_cd");
		sb.append("    INNER JOIN ki_kamoku_security ksec ON km.kamoku_naibu_cd = ksec.kamoku_naibu_cd AND ksec.kesn = ?  AND ksec.sptn = ?");
		sb.append("    WHERE");
		sb.append("          bkz_tmp.kessanki_bangou = ?");
		sb.append("      :AND_SYUUKEI_BUMON");
		sb.append("      :AND_FROM_FUTAN_BUMON");
		sb.append("      :AND_TO_FUTAN_BUMON");
		sb.append("      :AND_FROM_KM_KAMOKU");
		sb.append("      :AND_TO_KM_KAMOKU");
		sb.append("    GROUP BY futan_bumon_cd, bkz_tmp.kamoku_gaibu_cd");
		sb.append("    ) bkz");
		sb.append("  FULL OUTER JOIN");
					//部署入出力仕訳
		sb.append("   (");
		sb.append("    SELECT");
		sb.append("       futan_bumon_cd");
		sb.append("      ,kamoku_gaibu_cd");
		sb.append("      ,SUM(valu) AS ruikei_kingaku_shiwake");
		sb.append("     FROM");
		sb.append("       base_busho_shiwake kbs");
						//予算執行対象外科目は除外
		sb.append("     INNER JOIN ki_kamoku_security kks ON kks.kesn = kbs.kesn AND kks.kamoku_naibu_cd = kbs.kamoku_naibu_cd");
		sb.append("     INNER JOIN kamoku_master km ON km.kamoku_naibu_cd = kks.kamoku_naibu_cd");
		sb.append("    WHERE");
		sb.append("          kbs.kesn = ?");
		sb.append("       AND kks.sptn = ?");
		sb.append("      :AND_SYUUKEI_BUMON");
		sb.append("      :AND_FROM_FUTAN_BUMON");
		sb.append("      :AND_TO_FUTAN_BUMON");
		sb.append("      :AND_FROM_KM_KAMOKU");
		sb.append("      :AND_TO_KM_KAMOKU");
		sb.append("     GROUP BY futan_bumon_cd, kamoku_gaibu_cd");
		sb.append("    ) ks ON ks.futan_bumon_cd = bkz.futan_bumon_cd AND ks.kamoku_gaibu_cd = bkz.kamoku_gaibu_cd");
		sb.append("  ) ruikei");
		sb.append(" GROUP BY meisai_bumon_cd");
		if(isKamokuTani)
		sb.append("             ,kamoku_gaibu_cd_tmp"); // 部門科目単位で集計する場合
		
		return sb.toString();
	}
	
	
	/**
	 * WFみなし実績の集計対象月を決めるための基準日を取得
	 * @return sql
	 */
	public String getKijunbi() {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbAnd = new StringBuffer();
		
		// 申請日から取るのが基本
		sb.append("     SELECT denpyou_id, touroku_time AS kijunbi");
		sb.append("     FROM max_shounin_jyoukyou_time");
		sb.append("     WHERE 1 = 1");
		sb.append("     :AND");
		
		// 経費立替精算
		if("3".equals(setting.keijoubiDefaultA001())) {
			sbAnd.append("AND denpyou_id NOT LIKE '%" + DENPYOU_KBN.KEIHI_TATEKAE_SEISAN + "%'");
			sb.append(" UNION ALL");
			sb.append(" SELECT denpyou_id, keijoubi AS kijunbi FROM keihiseisan");
		}
		// 請求書払い申請
		if("1".equals(setting.seikyuuKeijouNyuurohyku())) {
			sbAnd.append("AND denpyou_id NOT LIKE '%" + DENPYOU_KBN.SEIKYUUSHO_BARAI + "%'");
			sb.append(" UNION ALL");
			sb.append(" SELECT denpyou_id, keijoubi AS kijunbi FROM seikyuushobarai");
		}
		// 旅費精算
		if("3".equals(setting.keijoubiDefaultA004())) {
			sbAnd.append("AND denpyou_id NOT LIKE '%" + DENPYOU_KBN.RYOHI_SEISAN + "%'");
			sb.append(" UNION ALL");
			sb.append(" SELECT denpyou_id, keijoubi AS kijunbi FROM ryohiseisan");
		}
		// 海外旅費精算
		if("3".equals(setting.keijoubiDefaultA011())) {
			sbAnd.append("AND denpyou_id NOT LIKE '%" + DENPYOU_KBN.KAIGAI_RYOHI_SEISAN + "%'");
			sb.append(" UNION ALL");
			sb.append(" SELECT denpyou_id, keijoubi AS kijunbi FROM kaigai_ryohiseisan");
		}
		// 支払依頼申請
		sbAnd.append("AND denpyou_id NOT LIKE '%" + DENPYOU_KBN.SIHARAIIRAI + "%'");
		sb.append("     UNION ALL");
		sb.append("     SELECT denpyou_id, keijoubi AS kijunbi FROM shiharai_irai");
		
		return sb.toString().replace(":AND", sbAnd.toString());
	}
	
	/**
	 * 伝票毎の承認状況の最新更新日を取得するSQL
	 * @return sql
	 */
	public String getMaxShouninJyoukyouTimeSql() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT denpyou_id, MAX(touroku_time) AS touroku_time ");
		sb.append(" FROM shounin_joukyou WHERE joukyou_cd = '1' GROUP BY denpyou_id");
		return sb.toString();
	}
	
	/**
	 * 財務転記済み仕訳の伝票IDを取得するSQL
	 * @return sql
	 */
	public String getShiwakeZaimuRenkeizumiSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT denpyou_id FROM shiwake_de3 WHERE shiwake_status = '1' ");
		sb.append("UNION ALL SELECT DISTINCT denpyou_id FROM shiwake_sias WHERE shiwake_status = '1'");
		return sb.toString();
	}
	
	/**
	 * みなし実績取得用のSQLを組み立てる
	 * @param kesn 決算期
	 * @param sptn セキュリティパターン
	 * @param targetDate 対象日付
	 * @param isKamokuTani 集計単位フラグ
	 * @param isRuikei 予算単位
	 * @return sql
	 */
	public String getMinashiJissekiSql(int kesn, int sptn, Date targetDate, boolean isKamokuTani, boolean isRuikei){

		//業務テーブルを結合させる
		List<GMap> yosanShikkouTaishouList = getYosanKnariTaishou();
		StringBuffer gyoumuJKian = new StringBuffer();
		StringBuffer gyoumuSIrai = new StringBuffer();
		
		// 実施起案または支出依頼の場合
		for(GMap map : yosanShikkouTaishouList){
			
			String denpyouKbn = (String)map.get("denpyou_kbn");
			
			// 経費精算
			if(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN.equals(denpyouKbn)){
				if (gyoumuSIrai.length() > 0) gyoumuSIrai.append(" UNION ALL ");
				gyoumuSIrai.append(getKeiseisanSql(isKamokuTani));

			// 仮払申請
			}else if(DENPYOU_KBN.KARIBARAI_SHINSEI.equals(denpyouKbn)){
				if (gyoumuJKian.length() > 0) gyoumuJKian.append(" UNION ALL ");
				gyoumuJKian.append(getKaribaraiSql(isKamokuTani));

			//請求書払い申請
			}else if(DENPYOU_KBN.SEIKYUUSHO_BARAI.equals(denpyouKbn)){
				if (gyoumuSIrai.length() > 0) gyoumuSIrai.append(" UNION ALL ");
				gyoumuSIrai.append(getSeikyuushobaraiSql(isKamokuTani));

			//旅費精算
			}else if(DENPYOU_KBN.RYOHI_SEISAN.equals(denpyouKbn)){
				if (gyoumuSIrai.length() > 0) gyoumuSIrai.append(" UNION ALL ");
				gyoumuSIrai.append(getRyohiseisanSql(isKamokuTani));

			//旅費仮払い申請
			}else if(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI.equals(denpyouKbn)){
				if (gyoumuJKian.length() > 0) gyoumuJKian.append(" UNION ALL ");
				gyoumuJKian.append(getRyohiKaribaraiSql(isKamokuTani));

			//海外出張旅費精算
			}else if(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN.equals(denpyouKbn)){
				if (gyoumuSIrai.length() > 0) gyoumuSIrai.append(" UNION ALL ");
				gyoumuSIrai.append(getKaigaiRyohiseisanSql(isKamokuTani));

			//海外出張旅費仮払い申請
			}else if(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI.equals(denpyouKbn)){
				if (gyoumuJKian.length() > 0) gyoumuJKian.append(" UNION ALL ");
				gyoumuJKian.append(getKaigaiRyohiKaribaraiSql(isKamokuTani));

			//支出依頼書
			}else if(DENPYOU_KBN.SIHARAIIRAI.equals(denpyouKbn)){
				if (gyoumuSIrai.length() > 0) gyoumuSIrai.append(" UNION ALL ");
				gyoumuSIrai.append(getShiharaiIraiSql(isKamokuTani));

			//ユーザー定義届書
			}else if(denpyouKbn.startsWith("B")){
				// 対象SQLを取得する。
				String tmpsSql = this.getKanitodokeSql(denpyouKbn, isKamokuTani);
				// 対象SQLが取得できた場合に
				if (!super.isEmpty(tmpsSql)){
					if (gyoumuJKian.length() > 0) gyoumuJKian.append(" UNION ALL ");
					gyoumuJKian.append(tmpsSql);
				}
			}else{
				// カスタマイズ用
				gyoumuJKian.append(getExtSql(denpyouKbn, gyoumuJKian, isKamokuTani));
// gyoumuSIrai.append(getExtSql(denpyouKbn, gyoumuSIrai, isKamokuTani));
			}
		}
		
		//実施起案の伝票がない場合、ダミーの空テーブルをセット
		if(gyoumuJKian.length() == 0){
			gyoumuJKian.append("SELECT");
			gyoumuJKian.append(" null::text AS denpyou_id,");
			gyoumuJKian.append(" null::text AS futan_bumon_cd,");
		if(isKamokuTani){
			gyoumuJKian.append(" null::text AS kamoku_gaibu_cd,");
			gyoumuJKian.append(" null::text AS edaban_code,");
		}
		gyoumuJKian.append(" null::INTEGER AS kingaku");
		}
		String gyoumuJKianSql = gyoumuJKian.toString();
		
		//支出依頼の伝票がない場合、ダミーの空テーブルをセット
		if(gyoumuSIrai.length() == 0){
			gyoumuSIrai.append("SELECT");
			gyoumuSIrai.append(" null::text AS denpyou_id,");
			gyoumuSIrai.append(" null::text AS futan_bumon_cd,");
		if(isKamokuTani){
			gyoumuSIrai.append(" null::text AS kamoku_gaibu_cd,");
			gyoumuSIrai.append(" null::text AS edaban_code,");
		}
		gyoumuSIrai.append(" null::INTEGER AS kingaku");
		}
		//支出対象科目判定用SQLの置換（支出依頼のみ）
		final String SHISHUTSU_KAMOKU = "SELECT kamoku_gaibu_cd"
										+ " FROM kamoku_master km"
										+ " INNER JOIN ki_kamoku_security kks ON kks.kamoku_naibu_cd = km.kamoku_naibu_cd"
										+ " WHERE kks.kesn = :KESN AND kks.sptn = :SPTN";
		String gyoumuSIraiSql = gyoumuSIrai.toString().replace(":SHISHUTSU_KAMOKU", SHISHUTSU_KAMOKU);
		gyoumuSIraiSql = gyoumuSIraiSql.replace(":KESN", String.valueOf(kesn));
		gyoumuSIraiSql = gyoumuSIraiSql.replace(":SPTN", String.valueOf(sptn));
		
		
		StringBuffer sbKiannoNashiDenpyouId = new StringBuffer();
									//集計対象伝票ID(起案番号なし支出依頼)
		sbKiannoNashiDenpyouId.append("SELECT");
		sbKiannoNashiDenpyouId.append("  d.denpyou_id AS denpyou_id");
		sbKiannoNashiDenpyouId.append(" FROM");
		sbKiannoNashiDenpyouId.append("  denpyou d");
		sbKiannoNashiDenpyouId.append("  INNER JOIN denpyou_shubetsu_ichiran dsi ON d.denpyou_kbn = dsi.denpyou_kbn");
		sbKiannoNashiDenpyouId.append("  LEFT OUTER JOIN denpyou_kian_himozuke dkh ON d.denpyou_id = dkh.denpyou_id ");
		sbKiannoNashiDenpyouId.append("  INNER JOIN " + ((isRuikei)? "kijunbi" : "max_shounin_jyoukyou_time") + " sj ON d.denpyou_id = sj.denpyou_id");
		sbKiannoNashiDenpyouId.append(" WHERE dsi.yosan_shikkou_taishou = '"  + YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI + "'");
		sbKiannoNashiDenpyouId.append("  AND d.denpyou_id NOT IN (SELECT denpyou_id FROM shiwake_zaimu_renkeizumi)"); // 財務転記未済
		sbKiannoNashiDenpyouId.append("  AND d.denpyou_joutai IN ('" + DENPYOU_JYOUTAI.SHINSEI_CHUU + "','" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI +"')");
		if(isRuikei) {
			sbKiannoNashiDenpyouId.append("  AND (");
			sbKiannoNashiDenpyouId.append("   (");
												// 予算執行対象が空なら申請日が期首～対象月まで
			sbKiannoNashiDenpyouId.append("        d.yosan_check_nengetsu = ''");
			sbKiannoNashiDenpyouId.append("    AND sj.kijunbi < (SELECT to_date + 1 FROM ki_kesn WHERE kesn = ? AND ? BETWEEN from_date AND to_date)");
			sbKiannoNashiDenpyouId.append("    )");
												// 予算執行対象月に登録されていれば予算執行対象月が期首～対象月まで
			sbKiannoNashiDenpyouId.append("     OR d.yosan_check_nengetsu BETWEEN ? AND ?");
			sbKiannoNashiDenpyouId.append("   )");
		}else {
			sbKiannoNashiDenpyouId.append("  AND sj.touroku_time < (SELECT to_date + 1 FROM ki_kesn WHERE kesn = ? AND ? BETWEEN from_date AND to_date)");
		}
		sbKiannoNashiDenpyouId.append("  AND dkh.jisshi_kian_bangou IS NULL");
		
		StringBuffer sbKiannoAriShishutsuIraiDenpyouId = new StringBuffer();
										//集計対象伝票ID(起案番号ありの支出依頼)
		sbKiannoAriShishutsuIraiDenpyouId.append("SELECT");
		sbKiannoAriShishutsuIraiDenpyouId.append("  d.denpyou_id AS denpyou_id");
		sbKiannoAriShishutsuIraiDenpyouId.append(" FROM");
		sbKiannoAriShishutsuIraiDenpyouId.append("  denpyou d");
		sbKiannoAriShishutsuIraiDenpyouId.append("  INNER JOIN denpyou_shubetsu_ichiran dsi ON d.denpyou_kbn = dsi.denpyou_kbn");
		sbKiannoAriShishutsuIraiDenpyouId.append("  LEFT OUTER JOIN denpyou_kian_himozuke dkh ON d.denpyou_id = dkh.denpyou_id ");
		sbKiannoAriShishutsuIraiDenpyouId.append("  INNER JOIN " + ((isRuikei)? "kijunbi" : "max_shounin_jyoukyou_time") + " sj ON d.denpyou_id = sj.denpyou_id");
		sbKiannoAriShishutsuIraiDenpyouId.append(" WHERE dsi.yosan_shikkou_taishou = '"  + YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI + "'");
		sbKiannoAriShishutsuIraiDenpyouId.append("  AND d.denpyou_joutai IN ('" + DENPYOU_JYOUTAI.SHINSEI_CHUU + "','" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI +"')");
		sbKiannoAriShishutsuIraiDenpyouId.append("  AND " + ((isRuikei)? "sj.kijunbi" : "sj.touroku_time") + " < (SELECT to_date + 1 FROM ki_kesn WHERE kesn = ? AND ? BETWEEN from_date AND to_date)");
		sbKiannoAriShishutsuIraiDenpyouId.append("  AND dkh.jisshi_nendo = (SELECT TO_CHAR(to_date, 'YYYY') FROM ki_kesn WHERE kesn = ? ORDER BY to_date ASC LIMIT 1)");
		sbKiannoAriShishutsuIraiDenpyouId.append("  AND dkh.jisshi_kian_bangou IS NOT NULL");
												// 支出依頼は自分のレコードみても起案終了の判断できない。だから添付された起案をさかのぼって起案終了済かどうか確認しないいけない。
		sbKiannoAriShishutsuIraiDenpyouId.append("  AND d.denpyou_id NOT IN (SELECT DISTINCT denpyou_id FROM kiansyuryo WHERE kian_syuryo_flg = '1')");
		
		StringBuffer sbKiannoAriJisshiKianDenpyouId = new StringBuffer();
									//集計対象伝票ID(実施起案)
		sbKiannoAriJisshiKianDenpyouId.append("SELECT");
		sbKiannoAriJisshiKianDenpyouId.append("  d.denpyou_id AS denpyou_id");
		sbKiannoAriJisshiKianDenpyouId.append(" FROM");
		sbKiannoAriJisshiKianDenpyouId.append("     denpyou d");
		sbKiannoAriJisshiKianDenpyouId.append("     INNER JOIN denpyou_shubetsu_ichiran dsi ON d.denpyou_kbn = dsi.denpyou_kbn");
		sbKiannoAriJisshiKianDenpyouId.append("     LEFT OUTER JOIN denpyou_kian_himozuke dkh ON d.denpyou_id = dkh.denpyou_id ");
		sbKiannoAriJisshiKianDenpyouId.append("  INNER JOIN " + ((isRuikei)? "kijunbi" : "max_shounin_jyoukyou_time") + " sj ON d.denpyou_id = sj.denpyou_id");
		sbKiannoAriJisshiKianDenpyouId.append(" WHERE dsi.yosan_shikkou_taishou = '"  + YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN + "' ");
		sbKiannoAriJisshiKianDenpyouId.append("  AND d.denpyou_joutai IN ('" + DENPYOU_JYOUTAI.SHINSEI_CHUU + "','" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI +"')");
		sbKiannoAriJisshiKianDenpyouId.append("  AND dkh.kian_syuryo_flg <> '1'"); //実施起案は自分のレコードだけみて起案終了判断できる
		if(isRuikei) {
			sbKiannoAriJisshiKianDenpyouId.append("  AND (");
			sbKiannoAriJisshiKianDenpyouId.append("   (");
												// 予算執行対象が空なら申請日が期首～対象月まで
			sbKiannoAriJisshiKianDenpyouId.append("        d.yosan_check_nengetsu = ''");
			sbKiannoAriJisshiKianDenpyouId.append("    AND sj.kijunbi < (SELECT to_date + 1 FROM ki_kesn WHERE kesn = ? AND ? BETWEEN from_date AND to_date)");
			sbKiannoAriJisshiKianDenpyouId.append("    )");
												// 予算執行対象月に登録されていれば予算執行対象月が期首～対象月まで
			sbKiannoAriJisshiKianDenpyouId.append("     OR d.yosan_check_nengetsu BETWEEN ? AND ?");
			sbKiannoAriJisshiKianDenpyouId.append("   )");
		}else {
			sbKiannoAriJisshiKianDenpyouId.append("  AND sj.touroku_time < (SELECT to_date + 1 FROM ki_kesn WHERE kesn = ? AND ? BETWEEN from_date AND to_date)");
		}
		sbKiannoAriJisshiKianDenpyouId.append("  AND dkh.jisshi_nendo = (SELECT TO_CHAR(to_date, 'YYYY') FROM ki_kesn WHERE kesn = ? ORDER BY to_date ASC LIMIT 1)");
		sbKiannoAriJisshiKianDenpyouId.append("  AND dkh.jisshi_kian_bangou IS NOT NULL");
		
		
		StringBuffer sb = new StringBuffer();
					//起案番号なしの支出依頼を集約
			sb.append("SELECT");
			sb.append("     futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kamoku_gaibu_cd,");
				sb.append("     edaban_code,");
			}
			sb.append("     SUM(kingaku) AS kingaku");
			sb.append("    FROM");
							//各伝票テーブルから部門・科目・金額を取得
			sb.append("      ("  + gyoumuSIraiSql + ") g1");
						//集計対象とする伝票を共通条件で絞り込み
			sb.append("    WHERE denpyou_id IN (" + sbKiannoNashiDenpyouId + ")");
			sb.append("     :AND_SYUUKEI_BUMON");
			sb.append("     :AND_FROM_FUTAN_BUMON");
			sb.append("     :AND_TO_FUTAN_BUMON");
			sb.append("     :AND_FROM_KAMOKU");
			sb.append("     :AND_TO_KAMOKU");
			sb.append("    GROUP BY futan_bumon_cd");
			if(isKamokuTani){
				sb.append("            ,kamoku_gaibu_cd, edaban_code");
			}
			sb.append("   UNION ALL");
					//起案番号が採番された実施起案・実施起案に紐付く支出依頼を集約
			sb.append("   SELECT");
			sb.append("     futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kamoku_gaibu_cd,");
				sb.append("  edaban_code,");
			}
			sb.append("     SUM(kingaku) AS kingaku");
			sb.append("    FROM");
			sb.append("      (SELECT");
			sb.append("         CASE WHEN jkian.kian_bangou IS NULL THEN sirai.kian_bangou ELSE jkian.kian_bangou END AS kian_bangou,");
			sb.append("         CASE WHEN jkian.kian_bangou IS NULL THEN sirai.futan_bumon_cd ELSE jkian.futan_bumon_cd END AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("         CASE WHEN jkian.kian_bangou IS NULL THEN sirai.kamoku_gaibu_cd ELSE jkian.kamoku_gaibu_cd END AS kamoku_gaibu_cd,");
				sb.append("         CASE WHEN jkian.kian_bangou IS NULL THEN sirai.edaban_code ELSE jkian.edaban_code END AS edaban_code,");
			}
					// 起案金額 >= 支出依頼の合計額→起案金額-財務転記額合計
					// 起案金額 <  支出依頼の合計額→支出依頼の合計額-財務転記額合計
			sb.append("         CASE WHEN COALESCE(jkian.kingaku, 0) >= COALESCE(sirai.kingaku, 0) THEN COALESCE(jkian.kingaku, 0) - COALESCE(tenkizumi_kingaku, 0) ELSE sirai.kingaku - COALESCE(tenkizumi_kingaku, 0) END AS kingaku");
			sb.append("       FROM");
								//実施起案
			sb.append("         (SELECT");
			sb.append("            dkh.jisshi_kian_bangou AS kian_bangou,");
			sb.append("            g2.futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("            g2.kamoku_gaibu_cd AS kamoku_gaibu_cd,");
				sb.append("            g2.edaban_code AS edaban_code,");
			}
			sb.append("            SUM(g2.kingaku) AS kingaku ");
			sb.append("          FROM");
									//各伝票テーブルから部門・科目・金額を取得
			sb.append("            ("  + gyoumuJKianSql + ") g2");
			sb.append("            INNER JOIN denpyou_kian_himozuke dkh ON dkh.denpyou_id = g2.denpyou_id");
								//集計対象とする伝票を共通条件で絞り込み
			sb.append("          WHERE g2.denpyou_id IN (" + sbKiannoAriJisshiKianDenpyouId + ")"); // 実施起案の抽出条件
			sb.append("          GROUP BY dkh.jisshi_kian_bangou, g2.futan_bumon_cd"); // 別に伝票IDが先頭にあっても問題ないけどなんとなく支出依頼に合わせとく
			if(isKamokuTani){
				sb.append("                  ,g2.kamoku_gaibu_cd, g2.edaban_code");
			}
			sb.append("          ) jkian");
								//実施起案に紐付く支出依頼を実施起案の抽出結果に結合させる
			sb.append("         FULL OUTER JOIN");
			sb.append("         (SELECT");
			sb.append("            jisshi_kian_bangou AS kian_bangou,");
			sb.append("            futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("            kamoku_gaibu_cd,");
				sb.append("            edaban_code,");
			}
			sb.append("            SUM(kingaku) AS kingaku,");
			sb.append("            SUM(tenkizumi_kingaku) AS tenkizumi_kingaku ");
			sb.append("           FROM");
			sb.append("           (SELECT");
			sb.append("              g2.denpyou_id,");
			sb.append("              dkh.jisshi_kian_bangou,");
			sb.append("              g2.futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("              g2.kamoku_gaibu_cd AS kamoku_gaibu_cd,");
				sb.append("              g2.edaban_code AS edaban_code,");
			}
									//申請額（財務転記済・未済に関わらない）
			sb.append("              g2.kingaku,");
									// 財務転記済なら申請額を転記済み金額として持っておく
			sb.append("              CASE WHEN s.denpyou_id IS NULL THEN 0 ELSE g2.kingaku END AS tenkizumi_kingaku ");
			sb.append("            FROM");
									//各伝票テーブルから部門・科目・金額を取得
			sb.append("              ("  + gyoumuSIraiSql + ") g2");
			sb.append("              INNER JOIN denpyou_kian_himozuke dkh ON dkh.denpyou_id = g2.denpyou_id");
			sb.append("              LEFT OUTER JOIN shiwake_zaimu_renkeizumi s ON s.denpyou_id = g2.denpyou_id"); // 財務転記済
									//集計対象とする伝票を共通条件で絞り込み
			sb.append("            WHERE g2.denpyou_id IN (" + sbKiannoAriShishutsuIraiDenpyouId + ")"); // 実施起案添付あり支出依頼の抽出条件
			sb.append("            ) sirai_base");
			sb.append("          GROUP BY jisshi_kian_bangou, futan_bumon_cd");
			if(isKamokuTani){
				sb.append("                  ,kamoku_gaibu_cd, edaban_code");
			}
			sb.append("          ) sirai ON jkian.kian_bangou = sirai.kian_bangou AND jkian.futan_bumon_cd = sirai.futan_bumon_cd");
			if(isKamokuTani){
				sb.append("                 AND jkian.kamoku_gaibu_cd = sirai.kamoku_gaibu_cd AND jkian.edaban_code = sirai.edaban_code");
			}
			sb.append("       ) m1");
			sb.append("     WHERE 1 = 1");
			sb.append("     :AND_SYUUKEI_BUMON");
			sb.append("     :AND_FROM_FUTAN_BUMON");
			sb.append("     :AND_TO_FUTAN_BUMON");
			sb.append("     :AND_FROM_KAMOKU");
			sb.append("     :AND_TO_KAMOKU");
			sb.append("    GROUP BY futan_bumon_cd");
			if(isKamokuTani){
				sb.append("            ,kamoku_gaibu_cd, edaban_code");
			}
		
		return sb.toString();
	}
	
	/**
	 * カスタマイズで対象伝票増やす時の拡張用
	 * @param denpyouKbn 伝票区分
	 * @param sb SQL格納用
	 * @param isKamokuTani チェック粒度科目判定
	 * @return sql
	 */
	public String getExtSql(String denpyouKbn, StringBuffer sb, boolean isKamokuTani) {
		return "";
	}
	
	/**
	 * 支払依頼申請のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected Object getShiharaiIraiSql(boolean isKamokuTani) {
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  denpyou_id,");
		sb.append("  kari_futan_bumon_cd AS futan_bumon_cd,");
		if(isKamokuTani){
			sb.append("  kari_kamoku_cd AS kamoku_gaibu_cd,");
			sb.append("  kari_kamoku_edaban_cd AS edaban_code,");
		}
		sb.append("  SUM(" + (isZeinuki ? "zeinuki_kingaku" :  "shiharai_kingaku") + ") AS kingaku");
		sb.append(" FROM shiharai_irai_meisai");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
		sb.append(" GROUP BY denpyou_id, kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd");
		}
	return sb.toString();
	}

	/**
	 * 経費精算のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getKeiseisanSql(boolean isKamokuTani){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  denpyou_id,");
			sb.append("  kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("  kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("  SUM(" + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + ") AS kingaku");
			sb.append(" FROM keihiseisan_meisai");
			sb.append(" WHERE kari_futan_bumon_cd <> ''");
			sb.append("   AND kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append(" GROUP BY denpyou_id, kari_futan_bumon_cd");
			if(isKamokuTani){
				sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd");
			}
		return sb.toString();
	}
	
	/**
	 * 仮払申請のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getKaribaraiSql(boolean isKamokuTani){
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append(" denpyou_id,");
			sb.append("  kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("  kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("  CASE karibarai_on WHEN '1' THEN karibarai_kingaku ELSE kingaku END AS kingaku FROM karibarai");
			sb.append(" WHERE kari_futan_bumon_cd <> ''");
		return sb.toString();
	}

	/**
	 * 請求書払い申請のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getSeikyuushobaraiSql(boolean isKamokuTani){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  denpyou_id,");
			sb.append("  kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("  kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("  SUM(" + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + ") AS kingaku");
			sb.append(" FROM seikyuushobarai_meisai");
			sb.append("  WHERE kari_futan_bumon_cd <> ''");
			sb.append("    AND kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append(" GROUP BY denpyou_id, kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd");
		}
		return sb.toString();
	}
	
	/**
	 * 出張旅費精算のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getRyohiseisanSql(boolean isKamokuTani){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  denpyou_id,");
			sb.append("  futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kamoku_gaibu_cd,");
				sb.append("  edaban_code,");
			}
			sb.append("  SUM(kingaku) AS kingaku");
			sb.append(" FROM");
			// 旅費明細金額
			sb.append("  (SELECT");
			sb.append("     r.denpyou_id AS denpyou_id,");
			sb.append("     r.kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     r.kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     r.kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("    SUM(rm." + (isZeinuki ? "zeinuki_kingaku" :  "meisai_kingaku") + ") AS kingaku");
			sb.append("    FROM ryohiseisan r");
			sb.append("    INNER JOIN ryohiseisan_meisai rm ON r.denpyou_id = rm.denpyou_id");
			sb.append("    WHERE r.kari_futan_bumon_cd <> ''");
			sb.append("     AND r.kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append("    GROUP BY r.denpyou_id");
			// 経費明細金額
			sb.append("   UNION ALL");
			sb.append("   SELECT");
			sb.append("     denpyou_id,");
			sb.append("     kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     " + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + " AS kingaku");
			sb.append("    FROM ryohiseisan_keihi_meisai");
			sb.append("    WHERE kari_futan_bumon_cd <> ''");
			sb.append("     AND kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append("   ) ryohi");
			sb.append(" GROUP BY ryohi.denpyou_id, ryohi.futan_bumon_cd");
			if(isKamokuTani){
				sb.append("         ,ryohi.kamoku_gaibu_cd, ryohi.edaban_code");
			}
		return sb.toString();
	}
	/**
	 * 出張旅費仮払申請のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getRyohiKaribaraiSql(boolean isKamokuTani){
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  denpyou_id,");
			sb.append("  futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kamoku_gaibu_cd,");
				sb.append("  edaban_code,");
			}
			sb.append("  SUM(kingaku) AS kingaku");
			sb.append(" FROM");
			// 旅費明細金額
			sb.append("  (SELECT");
			sb.append("     rk.denpyou_id AS denpyou_id,");
			sb.append("     rk.kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     rk.kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     rk.kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     rk.kingaku - SUM(COALESCE(rkkm.shiharai_kingaku, 0)) AS kingaku");
			sb.append("    FROM ryohi_karibarai rk");
			sb.append("    LEFT OUTER JOIN ryohi_karibarai_keihi_meisai rkkm ON rk.denpyou_id = rkkm.denpyou_id");
			sb.append("    WHERE rk.kari_futan_bumon_cd <> ''");
			sb.append("    GROUP BY rk.denpyou_id");
			// 経費明細金額
			sb.append("   UNION ALL");
			sb.append("   SELECT");
			sb.append("     denpyou_id,");
			sb.append("     kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     shiharai_kingaku AS kingaku");
			sb.append("    FROM ryohi_karibarai_keihi_meisai");
			sb.append("    WHERE kari_futan_bumon_cd <> ''");
			sb.append("   ) rkari");
			sb.append(" GROUP BY rkari.denpyou_id, rkari.futan_bumon_cd");
			if(isKamokuTani){
				sb.append("         ,rkari.kamoku_gaibu_cd, rkari.edaban_code");
			}
		return sb.toString();
	}
	/**
	 * 海外出張旅費精算のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getKaigaiRyohiseisanSql(boolean isKamokuTani){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  denpyou_id,");
			sb.append("  futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kamoku_gaibu_cd,");
				sb.append("  edaban_code,");
			}
			sb.append("  SUM(kingaku) AS kingaku");
			sb.append(" FROM");
			// 旅費明細金額（海外）-差引金額（海外）
			sb.append("  (SELECT");
			sb.append("     kr.denpyou_id AS denpyou_id,");
			sb.append("     kr.kaigai_kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kr.kaigai_kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     kr.kaigai_kari_kamoku_edaban_cd AS edaban_code,");
			}
				sb.append("     SUM(krm." + (isZeinuki ? "zeinuki_kingaku" :  "meisai_kingaku") + ")-(COALESCE(kr.sashihiki_num_kaigai, 0) * COALESCE(kr.sashihiki_tanka_kaigai, 0)) AS kingaku");
			sb.append("    FROM kaigai_ryohiseisan kr");
			sb.append("    INNER JOIN kaigai_ryohiseisan_meisai krm ON kr.denpyou_id = krm.denpyou_id");
			sb.append("    WHERE kr.kaigai_kari_futan_bumon_cd <> ''");
			sb.append("      AND kr.kaigai_kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append("      AND krm.kaigai_flg = '1'");
			sb.append("    GROUP BY kr.denpyou_id");
			// 旅費明細金額（国内）-差引金額（国内）
			sb.append("   UNION ALL");
			sb.append("   SELECT");
			sb.append("     kr.denpyou_id AS denpyou_id,");
			sb.append("     kr.kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kr.kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     kr.kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     SUM(" + (isZeinuki ? "zeinuki_kingaku" :  "meisai_kingaku") + ")-(COALESCE(kr.sashihiki_num, 0) * COALESCE(kr.sashihiki_tanka, 0)) AS kingaku");
			sb.append("    FROM kaigai_ryohiseisan kr");
			sb.append("    INNER JOIN kaigai_ryohiseisan_meisai krm ON kr.denpyou_id = krm.denpyou_id");
			sb.append("    WHERE kr.kari_futan_bumon_cd <> ''");
			sb.append("      AND kr.kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append("      AND krm.kaigai_flg = '0'");
			sb.append("    GROUP BY kr.denpyou_id");
			// 経費明細金額
			sb.append("   UNION ALL");
			sb.append("   SELECT");
			sb.append("     denpyou_id,");
			sb.append("     kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     " + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + " AS kingaku");
			sb.append("    FROM kaigai_ryohiseisan_keihi_meisai");
			sb.append("    WHERE kari_futan_bumon_cd <> ''");
			sb.append("    AND kari_kamoku_cd IN (:SHISHUTSU_KAMOKU)");
			sb.append("   ) kryohi");
			sb.append(" GROUP BY kryohi.denpyou_id, kryohi.futan_bumon_cd");
			if(isKamokuTani){
				sb.append("         ,kryohi.kamoku_gaibu_cd, kryohi.edaban_code");
			}
		return sb.toString();
	}
	/**
	 * 海外出張旅費仮払申請のSQLを組み立てる
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getKaigaiRyohiKaribaraiSql(boolean isKamokuTani){
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  denpyou_id,");
			sb.append("  futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kamoku_gaibu_cd,");
				sb.append("  edaban_code,");
			}
			sb.append("  SUM(kingaku) AS kingaku");
			sb.append(" FROM");
			// 旅費明細金額
			sb.append("  (SELECT");
			sb.append("     krk.denpyou_id AS denpyou_id,");
			sb.append("     krk.kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     krk.kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     krk.kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     krk.kingaku - SUM(COALESCE(krkkm.shiharai_kingaku, 0)) AS kingaku ");
			sb.append("    FROM kaigai_ryohi_karibarai krk");
			sb.append("    LEFT OUTER JOIN kaigai_ryohi_karibarai_keihi_meisai krkkm ON krk.denpyou_id = krkkm.denpyou_id");
			sb.append("    WHERE krk.kari_futan_bumon_cd <> ''");
			sb.append("    GROUP BY krk.denpyou_id");
			// 経費明細金額
			sb.append("   UNION ALL");
			sb.append("   SELECT");
			sb.append("     denpyou_id,");
			sb.append("     kari_futan_bumon_cd AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("     kari_kamoku_cd AS kamoku_gaibu_cd,");
				sb.append("     kari_kamoku_edaban_cd AS edaban_code,");
			}
			sb.append("     shiharai_kingaku AS kingaku");
			sb.append("   FROM kaigai_ryohi_karibarai_keihi_meisai");
			sb.append("   WHERE kari_futan_bumon_cd <> ''");
			sb.append("  ) krkari");
			sb.append(" GROUP BY krkari.denpyou_id, krkari.futan_bumon_cd");
			if(isKamokuTani){
				sb.append("         ,krkari.kamoku_gaibu_cd, krkari.edaban_code");
			}
		return sb.toString();
	}
	/**
	 * ユーザー定義届書申請のSQLを組み立てる
	 * @param denpyouKbn 伝票区分
	 * @param isKamokuTani 集計単位フラグ
	 * @return sql
	 */
	protected String getKanitodokeSql(String denpyouKbn, boolean isKamokuTani){
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  b.denpyou_id,");
			sb.append("  b.futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  COALESCE(km.kamoku_gaibu_cd, '') AS kamoku_gaibu_cd,");
				sb.append("  COALESCE(e.edaban_code, '') AS edaban_code,");
			}
// sb.append("  SUM(k.kingaku) AS kingaku");
        	// SUMする際に、ブランク→0への変換と,の除去をする
			sb.append("  SUM(CASE k.kingaku WHEN '' THEN 0 ELSE cast (replace (k.kingaku, ',', '') AS BIGINT) END) AS kingaku");
			sb.append(" FROM");
			sb.append(" (SELECT DISTINCT denpyou_id, version FROM kani_todoke WHERE version = :VERSION) kt");
			sb.append(" LEFT OUTER JOIN");
			sb.append("  (SELECT");
			sb.append("    denpyou_id,");
			sb.append("    denpyou_edano,");
			sb.append("    value1 AS futan_bumon_cd");
			sb.append("   FROM kani_todoke_meisai");
			sb.append("   WHERE denpyou_id LIKE :DENPYOU_ID");
			sb.append("     AND item_name = :SHISHUTSU_BUMON) b ON kt.denpyou_id = b.denpyou_id");
			sb.append(" LEFT OUTER JOIN");
			sb.append("  (SELECT");
			sb.append("    denpyou_id,");
			sb.append("    denpyou_edano,");
// sb.append("    CASE value1 WHEN '' THEN 0 ELSE cast(replace(value1, ',', '') AS BIGINT) END AS kingaku");
 			sb.append("    value1 AS kingaku"); // 結合前は変換なしでDB値をそのまま取得する。
			sb.append("   FROM kani_todoke_meisai");
			sb.append("   WHERE denpyou_id LIKE :DENPYOU_ID");
			sb.append("   AND item_name = :SHISHUTSU_KINGAKU) k ON kt.denpyou_id = k.denpyou_id AND b.denpyou_edano = k.denpyou_edano");
			if(isKamokuTani){
				sb.append(" LEFT OUTER JOIN");
				sb.append("  (SELECT");
				sb.append("    denpyou_id,");
				sb.append("    denpyou_edano,");
				sb.append("    value1 AS kamoku_gaibu_cd");
				sb.append("   FROM kani_todoke_meisai");
				sb.append("   WHERE denpyou_id LIKE :DENPYOU_ID");
				sb.append("   AND item_name = :SHISHUTSU_KAMOKU) km ON kt.denpyou_id = km.denpyou_id AND b.denpyou_edano = km.denpyou_edano");
				sb.append(" LEFT OUTER JOIN");
				sb.append("  (SELECT");
				sb.append("    denpyou_id,");
				sb.append("    denpyou_edano,");
				sb.append("    value1 AS edaban_code");
				sb.append("   FROM kani_todoke_meisai");
				sb.append("   WHERE denpyou_id LIKE :DENPYOU_ID");
				sb.append("   AND item_name = :SHISHUTSU_EDABAN) e ON kt.denpyou_id = e.denpyou_id AND b.denpyou_edano = e.denpyou_edano");
			}
			sb.append(" WHERE b.futan_bumon_cd <> ''");
			sb.append(" GROUP BY b.denpyou_id, b.futan_bumon_cd");
			if(isKamokuTani){
				sb.append("         ,km.kamoku_gaibu_cd, e.edaban_code");
			}
		
		
		StringBuffer sbRet = new StringBuffer();
		List<GMap> shishutsuBumonList   = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_BUMON);
		for(GMap map : shishutsuBumonList){
			int version = Integer.parseInt(map.get("version").toString());
			String shishutsuBumon = map.get("item_name").toString();
			String shishutsuKingaku = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KINGAKU, version);
			
			String sql = sb.toString();
			sql = sql.replace(":DENPYOU_ID", "      '%"+ denpyouKbn + "%'");
			sql = sql.replace(":VERSION", Integer.valueOf(version).toString());
			sql = sql.replace(":SHISHUTSU_BUMON",   "'" + shishutsuBumon + "'");
			sql = sql.replace(":SHISHUTSU_KINGAKU", "'" + shishutsuKingaku + "'");

			if(isKamokuTani){
				String shishutsuKamoku = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU, version);
				String shishutsuEdaban = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA, version);
				sql = sql.replace(":SHISHUTSU_KAMOKU", "'" + shishutsuKamoku + "'");
				sql = sql.replace(":SHISHUTSU_EDABAN", "'" + shishutsuEdaban + "'");
			}
			if(0 < sbRet.length()){
				sbRet.append(" UNION ALL ");
			}
			sbRet.append(sql);
		}
		return sbRet.toString();
	}
	
	/**
	 * 執行状況一覧情報テーブルのレコードを取得
	 * @param userId ユーザーID
	 * @return 執行状況一覧情報マップ
	 */
	public GMap selectShikkouJoukyouIchiranJouhou(String userId) {
		final String sql = "SELECT * FROM shikkou_joukyou_ichiran_jouhou WHERE user_id = ?";
		return connection.find(sql, userId);
	}
	
	/**
	 * 執行状況一覧情報テーブルにレコードを追加
	 * @param userId ユーザーID
	 * @param yosanTani 予算単位
	 * @return 結果
	 */
	public int insertShikkouJoukyouIchiranJouhou(String userId, String yosanTani) {
		final String sql = "INSERT INTO shikkou_joukyou_ichiran_jouhou VALUES(?, ?)";
		return connection.update(sql, userId, yosanTani);
	}
	
	/**
	 * 執行状況一覧情報テーブルのレコードを更新
	 * @param userId ユーザーID
	 * @param yosanTani 予算単位
	 * @return 結果
	 */
	public int updateShikkouJoukyouIchiranJouhou(String userId, String yosanTani) {
		final String sql = "UPDATE shikkou_joukyou_ichiran_jouhou SET yosan_tani = ? WHERE user_id = ?";
		return connection.update(sql, yosanTani, userId);
	}
	
	/**
	 * 執行状況一覧情報テーブルのレコード有無を確認
	 * @param userId ユーザーID
	 * @return レコードあればtrue
	 */
	public boolean existShikkouJoukyouIchiranJouhou(String userId) {
		final String sql = "SELECT count(*) as cnt FROM shikkou_joukyou_ichiran_jouhou WHERE user_id = ?";
		GMap result = connection.find(sql, userId);
		return (Long)result.get("cnt") > 0 ? true : false;
	}
}
