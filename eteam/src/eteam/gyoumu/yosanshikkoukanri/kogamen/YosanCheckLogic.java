package eteam.gyoumu.yosanshikkoukanri.kogamen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.EteamConst.yosanCheckTani;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import eteam.gyoumu.yosanshikkoukanri.ShikkouJoukyouIchiranLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;

/**
 * 予算チェックイベントのLogic
 */
public class YosanCheckLogic extends EteamAbstractLogic {
	
	/** 予算チェック粒度フラグ */
	public boolean isKamokuTani = yosanCheckTani.BUMON_KAMOKU.equals(setting.yosanCheckTani());
	/** 予算チェック期間フラグ */
	protected boolean isRuikei     = yosanCheckKikan.TO_TAISHOUZUKI.equals(setting.yosanCheckKikan());
	/** 予算チェック:判定コメント・超過 */
	public final String cmntChouka = setting.yosanCommentChouka();
	
	/** ワークフローLogic */
	protected WorkflowEventControlLogic wfEvntLogic ;
	/** マスター管理カテゴリーロジック */
	protected MasterKanriCategoryLogic masterLogic ;
	/** 予算執行管理カテゴリーロジック */
	public YosanShikkouKanriCategoryLogic commonLogic ;
	/** 通知カテゴリーロジック */
	protected TsuuchiCategoryLogic tsuuchiLogic ;
	/** 執行状況一覧ロジック */
	protected ShikkouJoukyouIchiranLogic ichiranLogic;
	
	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		wfEvntLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, _connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, _connection);
		commonLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, _connection);
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, _connection);
		ichiranLogic = EteamContainer.getComponent(ShikkouJoukyouIchiranLogic.class, _connection);
	}
	
	
	/**
	 * 予算チェックの初期表示用データを取得する
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param nendo 起案年度
	 * @return 表示用データ
	 */
	public List<GMap> loadData(String denpyouId, String denpyouKbn, String nendo){
		
		//検索用の変数取得
		int sptn = Integer.parseInt(setting.yosanSecurityPattern());
		GMap mapHimozuke = wfEvntLogic.selectDenpyouKianHimozuke(denpyouId);
		String jisshiNendo = nendo;
		if(null==nendo || nendo.isEmpty()){
			jisshiNendo = null==mapHimozuke.get("jisshi_nendo")? "" : mapHimozuke.get("jisshi_nendo").toString();
		}
		Date tmpDate = commonLogic.retTargetDate(jisshiNendo); //実施起案年度に基づくターゲット日付

		// 予算チェック期間が『累計』の場合
		// 予算執行対象月から求めた決算開始日で上書き
		String yosanCheckNengetsu = tsuuchiLogic.findDenpyou(denpyouId).getString("yosan_check_nengetsu");
		if(isRuikei) {
			tmpDate = commonLogic.retYosanCheckNengetsuFromDate(yosanCheckNengetsu);
		}
		
		// 検索用引数
		int kesn = masterLogic.findKesn(tmpDate);
		int kessankiBangou = masterLogic.findKessankiBangou(tmpDate);
		
		// 伝票から負担部門と金額を取得する。
		String sql = getDenpyouInfo(denpyouKbn, denpyouId);
		List<Object> params = new ArrayList<>();
		params = setDenpyouId(params, denpyouKbn, denpyouId, kesn, sptn);
		List<GMap> lstDenpyouInfo;
		lstDenpyouInfo = connection.load(sql, params.toArray());
		
		// 伝票からデータが取得できない（対象外伝票も含む）場合は処理を終了する。
		if (null == lstDenpyouInfo){
			return lstDenpyouInfo;
		}
		
		// 部門情報を取得する
		StringBuffer sbSB = new StringBuffer();
		params = new ArrayList<>(); //パラメータの初期化
		
		// ▼▼WITH句（部門情報取得）
		sbSB.append("WITH denpyou_info AS(" + getDenpyouInfo(denpyouKbn, denpyouId) + ") ");
		params = setDenpyouId(params, denpyouKbn, denpyouId, kesn, sptn);
		// ▼本体（部門情報取得）
		sbSB.append(getBumonInfo());
		params.add(kesn);
		params.add(sptn);
		
		List<GMap> lstMeisaiBumon = connection.load(sbSB.toString(), params.toArray());
		// 部門情報が取得できない場合は処理を終了する。
		if (null == lstMeisaiBumon){
			return lstMeisaiBumon;
		}
		
		// 伝票内容によって可変な部分を成形
		StringBuffer sb = new StringBuffer();
		StringBuffer sbNonEda = new StringBuffer();
		StringBuffer sbNonEda2 = new StringBuffer();
		for(GMap map :lstMeisaiBumon){
			String futanBumonCd = (String)map.get("futan_bumon_cd");
			String kamokuCd = (String)map.get("kamoku_gaibu_cd");
			String kamokuEdabanCd = (String)map.get("edaban_code");
			if(sb.length() != 0){
				sb.append(",");
				sbNonEda.append(",");
			}
			if(isKamokuTani){
				sb.append("(");
				sbNonEda.append("(");
			}
			sb.append("'").append(futanBumonCd).append("'");
			sbNonEda.append("'").append(futanBumonCd).append("'");
			if(isKamokuTani){
				sb.append(" , '").append(kamokuCd).append("'");
				sb.append(" , '").append(kamokuEdabanCd).append("')");
				sbNonEda.append(" , '").append(kamokuCd).append("')");
			}
			//部門科目単位かつ科目枝番が未入力の場合
			if(isKamokuTani){
				if(kamokuEdabanCd.isEmpty()){
					if(sbNonEda2.length() != 0){
						sbNonEda2.append(",");
					}
					sbNonEda2.append("(");
					sbNonEda2.append("'").append(futanBumonCd).append("'");
					sbNonEda2.append(" , '").append(kamokuCd).append("')");
				}
			}
		}
		
		if(0 == sb.toString().length()){
			return null;
		}
		
		
		//明細行-集計行の共通FROM句
		StringBuffer sbFrom = new StringBuffer();
		sbFrom.append("                       FROM");
		sbFrom.append("                         bumon_info bi");
		sbFrom.append("                       LEFT OUTER JOIN denpyou_info di");
		sbFrom.append("                        ON bi.futan_bumon_cd = di.futan_bumon_cd");
		if(this.isKamokuTani) sbFrom.append(" AND bi.kamoku_gaibu_cd = di.kamoku_gaibu_cd AND bi.edaban_code = di.edaban_code");
		sbFrom.append("                       LEFT OUTER JOIN yosan y");
		sbFrom.append("                        ON bi.futan_bumon_cd = y.futan_bumon_cd");
		if(this.isKamokuTani) sbFrom.append(" AND bi.kamoku_gaibu_cd = y.kamoku_gaibu_cd  AND bi.edaban_code = y.edaban_code");
		sbFrom.append("                       LEFT OUTER JOIN jisseki j");
		sbFrom.append("                        ON bi.futan_bumon_cd = j.futan_bumon_cd");
		if(this.isKamokuTani) sbFrom.append(" AND bi.kamoku_gaibu_cd = j.kamoku_gaibu_cd  AND bi.edaban_code = j.edaban_code");
		String sqlFrom = sbFrom.toString();
		
		//サマリー取得用SQL
		StringBuffer sbMain = new StringBuffer();
		//パラメータの初期化
		params = new ArrayList<>();
		
		//▼▼WITH句
		sbMain.append(yosanCheckSqlWith(params, denpyouKbn, denpyouId, kesn, kessankiBangou, sptn, yosanCheckNengetsu, sbNonEda2.toString()));
		//▼▼本体
		sbMain.append("SELECT ");
		sbMain.append("      syuukeiBumonCd,");
		sbMain.append("      kamokuCd,");
		sbMain.append("      kamokuEdabanCd,");
		sbMain.append("      meisaiBumonCd,");
		sbMain.append("      sb.syuukei_bumon_name                   AS syuukeiBumonName,");
		sbMain.append("      COALESCE(bm.futan_bumon_name, '')       AS meisaiBumonName,");
		if(this.isKamokuTani){
			sbMain.append("  COALESCE(km.kamoku_name_ryakushiki, '') AS kamokuName,");
			sbMain.append("  COALESCE(kez.edaban_name, '')           AS kamokuEdabanName,");
		}else{
			sbMain.append("  '' AS kamokuName,");
			sbMain.append("  '' AS kamokuEdabanName,");
		}
		sbMain.append("      kijunKingaku AS kiangaku,");
		sbMain.append("      jissekigaku,");
		
		sbMain.append("      shinseigaku,");
		sbMain.append("      zandaka");
		sbMain.append(" FROM ");
			
		// 明細行と集計行の集合
		sbMain.append("      (");
		// 明細行を取得
		sbMain.append("                 SELECT");
		sbMain.append("                   bi.syuukei_bumon_cd   AS syuukeiBumonCd,");
		sbMain.append("                   bi.futan_bumon_cd     AS meisaiBumonCd,");
		if(this.isKamokuTani){
			sbMain.append("               bi.kamoku_gaibu_cd        AS kamokuCd,");
			sbMain.append("               bi.edaban_code            AS kamokuEdabanCd,");
		}else{
			sbMain.append("               ''                        AS kamokuCd,");
			sbMain.append("               ''                        AS kamokuEdabanCd,");
		}
		sbMain.append("                   COALESCE(SUM(y.yosan), '0')              AS kijunKingaku,");
		sbMain.append("                   COALESCE(SUM(j.ruikei_shikkoudaka), '0') AS jissekigaku,");
		sbMain.append("                   COALESCE(SUM(di.kingaku), '0')           AS shinseigaku,");
		sbMain.append("                   COALESCE(SUM(y.yosan), '0') - (COALESCE(SUM(j.ruikei_shikkoudaka), '0') + COALESCE(SUM(di.kingaku), '0')) AS zandaka");
		sbMain.append(sqlFrom); // FROM句は明細行-集計行で共通
		sbMain.append("                 GROUP BY bi.syuukei_bumon_cd, bi.futan_bumon_cd");
		if(this.isKamokuTani) sbMain.append(" , bi.kamoku_gaibu_cd, bi.edaban_code");
		
		sbMain.append("        UNION ALL ");
		
		// 集計行
		sbMain.append("                 SELECT");
		sbMain.append("                   bi.syuukei_bumon_cd   AS syuukeiBumonCd,");
		sbMain.append("                   ''                    AS meisaiBumonCd,");
		if(this.isKamokuTani){
			sbMain.append("               bi.kamoku_gaibu_cd        AS kamokuCd,");
			sbMain.append("               bi.edaban_code            AS kamokuEdabanCd,");
		}else{
			sbMain.append("               ''                        AS kamokuCd,");
			sbMain.append("               ''                        AS kamokuEdabanCd,");
		}
		sbMain.append("                   COALESCE(SUM(y.yosan), '0')              AS kijunKingaku,");
		sbMain.append("                   COALESCE(SUM(j.ruikei_shikkoudaka), '0') AS jissekigaku,");
		sbMain.append("                   COALESCE(SUM(di.kingaku), '0')           AS shinseigaku,");
		sbMain.append("                   COALESCE(SUM(y.yosan), '0') - (COALESCE(SUM(j.ruikei_shikkoudaka), '0') + COALESCE(SUM(di.kingaku), '0')) AS zandaka");
		sbMain.append(sqlFrom); // FROM句は明細行-集計行で共通
		sbMain.append("                 GROUP BY bi.syuukei_bumon_cd");
		if(this.isKamokuTani) sbMain.append(" , bi.kamoku_gaibu_cd, bi.edaban_code");
		
		sbMain.append("       ) yc");
		// 集計部門を結合
		sbMain.append("     INNER JOIN syuukei_bumon sb ON yc.syuukeiBumonCd = sb.syuukei_bumon_cd");
		// 部門マスターを結合
		sbMain.append("     LEFT OUTER JOIN bumon_master bm ON yc.meisaiBumonCd = bm.futan_bumon_cd");
		
		if(this.isKamokuTani){
			//科目マスターを結合
			sbMain.append(" LEFT OUTER JOIN kamoku_master km ON yc.kamokuCd = km.kamoku_gaibu_cd");
			//科目枝番残高を結合（枝番名称の取得用）
			sbMain.append(" LEFT OUTER JOIN kamoku_edaban_zandaka kez ON yc.kamokuCd = kez.kamoku_gaibu_cd AND yc.kamokuEdabanCd = kez.kamoku_edaban_cd");
		}
			
		// ▼▼ソート
			sbMain.append(" ORDER BY syuukeiBumonCd, kamokuCd, kamokuEdabanCd, meisaiBumonCd");
		String sqlMain = sbMain.toString();
		
		
		//負担部門、科目、科目枝番による絞り込み条件の置換
		String andfutanBumonNonEda = isKamokuTani ?
				  " AND (futan_bumon_cd, kamoku_gaibu_cd) IN (" + sbNonEda.toString() + ")"
				: " AND futan_bumon_cd IN (" + sbNonEda.toString() + ")";
		String andfutanBumon = isKamokuTani ?
				  " AND (futan_bumon_cd, kamoku_gaibu_cd, edaban_code) IN (" + sb.toString() + ")"
				: " AND futan_bumon_cd IN (" + sb.toString() + ")";
		sqlMain = sqlMain.replace(":AND_FUTAN_BUMON_NON_EDA", andfutanBumonNonEda);
		sqlMain = sqlMain.replace(":AND_FUTAN_BUMON", andfutanBumon);
		
		
		List<GMap> ret = connection.load(sqlMain, params.toArray());
		
		//執行率
		BigDecimal oneHundred = BigDecimal.valueOf(100);
		for(GMap map :ret){
			
			int rate = oneHundred.intValue() + 1;
			BigDecimal yosan = new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU).toString());
			BigDecimal jissekigaku = new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU).toString());
			BigDecimal shinseigaku = new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU).toString());
			if(!yosan.equals(BigDecimal.valueOf(0))){
				rate = jissekigaku.add(shinseigaku).divide(yosan, 2, RoundingMode.UP).multiply(oneHundred).intValue();
			}else if(0==jissekigaku.add(shinseigaku).compareTo(BigDecimal.valueOf(0))){
				rate = 0;
			}
			map.put(YosanShikkouKanriCategoryLogic.MAP_ID.RATE, rate);

		}
		
		return ret;
	}
	
	/**
	 * 予算チェックの検索結果取得用WITH句
	 * @param params SQLパラメータ
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param kesn 決算期
	 * @param kessankiBangou 決算期番号
	 * @param sptn セキュリティパターン
	 * @param yosanCheckNengetsu 予算執行対象月
	 * @param nonEda2 検索条件文字列（枝番号指定ない負担部門-科目）
	 * @return 執行状況一覧の検索結果取得用WITH句
	 */
	protected String yosanCheckSqlWith(List<Object> params, String denpyouKbn, String denpyouId, int kesn, int kessankiBangou, int sptn, String yosanCheckNengetsu, String nonEda2) {
		
		StringBuffer sb = new StringBuffer();
		Date currentDate = new java.sql.Date(System.currentTimeMillis());
		Date fromDate = masterLogic.findFromDate(kesn);
		// 予算チェック期間が予算執行対象月までなら、部署仕訳の抽出範囲も指定の予算執行対象月まで
		Date toDate   = isRuikei? masterLogic.findToDate(kesn, commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu)) : masterLogic.findToDate(kesn);
		
		// 起案終了フラグ
		sb.append("WITH RECURSIVE kiansyuryo AS (");
		sb.append("SELECT denpyou_id, kian_denpyou, kian_syuryo_flg FROM denpyou_kian_himozuke");
		sb.append(" UNION ALL");
		sb.append("  SELECT kiansyuryo.denpyou_id, kiantnpu.kian_denpyou, kiantnpu.kian_syuryo_flg FROM denpyou_kian_himozuke kiantnpu, kiansyuryo");
		sb.append("   WHERE kiantnpu.denpyou_id = kiansyuryo.kian_denpyou)");
		
		//部署入出力仕訳の部門・科目・金額の貸借を共通項目化
		
		sb.append("     ,base_busho_shiwake AS (" + ichiranLogic.getBaseBushoShiwake() + ") ");
		params.add(fromDate); params.add(toDate);
		params.add(fromDate); params.add(toDate);
		
		//伝票情報
		sb.append("     ,denpyou_info AS(" + getDenpyouInfo(denpyouKbn, denpyouId) + ") ");
		params = setDenpyouId(params, denpyouKbn, denpyouId, kesn, sptn);

		//部門情報
		sb.append("     ,bumon_info AS(" + getBumonInfo() + ") ");
		params.add(kesn);
		params.add(sptn);
		
		//予算
		sb.append("     ,yosan AS(" + getYosanSql(kesn, kessankiBangou, yosanCheckNengetsu, nonEda2) + ") ");
		params.add(kessankiBangou);
		params.add(kessankiBangou);
		params.add(kessankiBangou);
		params.add(kesn);
		params.add(sptn);

		//伝票毎の承認状況の最新更新日
		sb.append("     ,max_shounin_jyoukyou_time AS (" + ichiranLogic.getMaxShouninJyoukyouTimeSql() + ") ");
		
		if(isRuikei) {
			//伝票毎の基準日
			sb.append("     ,kijunbi AS (" + ichiranLogic.getKijunbi() + ") ");
		}
		
		//仕訳抽出（財務転記済）
		sb.append("     ,shiwake_zaimu_renkeizumi AS (" + ichiranLogic.getShiwakeZaimuRenkeizumiSql() + ")");
		
		//WFみなし実績
		sb.append("    ,base_minashi  AS (" + ichiranLogic.getMinashiJissekiSql(kesn, sptn, commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu), isKamokuTani, isRuikei) + ") ");
		SimpleDateFormat format = new SimpleDateFormat("YYYYMM");
		Date startToDate = masterLogic.findToDate(kesn, fromDate);
		// 支出依頼（起案添付なし）ここから
		params.add(kesn);
		if(isRuikei) {
			params.add(commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu)); // チェック日じゃなくて指定月の月初をターゲット日とする
			params.add(format.format(startToDate)); // 期首年月
			params.add(yosanCheckNengetsu); // 対象年月
		}else {
			params.add(currentDate);
		}
		// ここまで
		// 実施起案ここから
		params.add(kesn);
		if(isRuikei) {
			params.add(commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu)); // チェック日じゃなくて指定月の月初をターゲット日とする
			params.add(format.format(startToDate)); // 期首年月
			params.add(yosanCheckNengetsu); // 対象年月
		}else {
			params.add(currentDate);
		}
		params.add(kesn);
		// ここまで
		// 支出依頼（起案添付あり）ここから
		params.add(kesn);
		if(isRuikei) {
			params.add(commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu)); // チェック日じゃなくて指定月の月初をターゲット日とする
		}else {
			params.add(currentDate);
		}
		params.add(kesn);
		// ここまで
		
		//WFみなし実績
		sb.append("    ,main_minashi  AS (");
		sb.append("                     SELECT");
		sb.append("                       futan_bumon_cd,");
		if(isKamokuTani) {
			sb.append("                   kamoku_gaibu_cd,");
			sb.append("                   edaban_code,");
		}
		sb.append("                       SUM(kingaku) AS kingaku");
		sb.append("                      FROM");
		sb.append("                       base_minashi");
		sb.append("                      GROUP BY futan_bumon_cd");
		if(isKamokuTani) sb.append("           ,kamoku_gaibu_cd, edaban_code");
		sb.append("                                         ) ");
		
		// 累計残高
		sb.append("     ,jisseki AS(" + getRuikeijissekiSql(kesn, kessankiBangou, currentDate, yosanCheckNengetsu, nonEda2) + ") ");
		params.add(kessankiBangou);
		params.add(kessankiBangou);
		params.add(kessankiBangou);
		params.add(kesn);
		params.add(sptn);
		params.add(kesn);
		params.add(sptn);
		
		//予算チェックでは集計部門や明細部門、科目による絞り込みは不要なのでブランクで置換
		return sb.toString().replace(":AND_SYUUKEI_BUMON",      "")
							.replace(":AND_FROM_FUTAN_BUMON",   "")
							.replace(":AND_TO_FUTAN_BUMON",     "")
							.replace(":AND_FROM_KAMOKU",    "")
							.replace(":AND_TO_KAMOKU",     "");
	}
	
	
	/**
	 * 伝票情報取得用のパラメータをセットする
	 * @param params パラメータ格納用リスト
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param kesn 決算期
	 * @param sptn セキュリティパターン
	 * @return パラメータ格納用リスト
	 */
	public List<Object> setDenpyouId(List<Object> params, String denpyouKbn, String denpyouId, int kesn, int sptn){
		
		if(denpyouKbn.startsWith("B")) {
			// ユーザー定義届書
			params.add(denpyouId);
			params.add(denpyouId);
			params.add(denpyouId);
			params.add(denpyouId);
			if(isKamokuTani) {
				params.add(kesn);
				params.add(sptn);
			}
		}else {
			switch(denpyouKbn){
			case DENPYOU_KBN.KARIBARAI_SHINSEI:			// 仮払申請
				params.add(denpyouId);
				break;
				
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:		// 経費立替精算
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:			// 請求書払い申請
			case DENPYOU_KBN.SIHARAIIRAI:				// 支払依頼申請
				params.add(kesn);
				params.add(sptn);
				params.add(denpyouId);
				break;
				
			case DENPYOU_KBN.RYOHI_SEISAN:						// 出張旅費
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:			// 出張仮払
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:	// 海外出張仮払
				params.add(denpyouId);
				params.add(denpyouId);
				params.add(denpyouId);
				params.add(kesn);
				params.add(sptn);
				break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:				// 海外出張旅費
				params.add(denpyouId);
				params.add(denpyouId);
				params.add(denpyouId);
				params.add(denpyouId);
				params.add(denpyouId);
				params.add(kesn);
				params.add(sptn);
				break;
			default:
				setExtDenpyouId(params, denpyouKbn, denpyouId, kesn, sptn);
				break;
			
			}
		}
		
		return params;
	}
	
	/**
	 * 伝票情報取得用のパラメータをセットする（拡張用）
	 * @param params パラメータ格納用リスト
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param kesn 決算期
	 * @param sptn セキュリティパターン
	 */
	public void setExtDenpyouId(List<Object> params, String denpyouKbn, String denpyouId, int kesn, int sptn){
		return;
	}
	
	/**
	 * 集計部門取得用SQL
	 * @return sql
	 */
	protected String getBumonInfo(){
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  A.syuukei_bumon_cd AS syuukei_bumon_cd");
			sb.append(" ,A.syuukei_bumon_name AS syuukei_bumon_name");
			sb.append(" ,A.futan_bumon_cd AS futan_bumon_cd");
			sb.append(" ,A.futan_bumon_name AS futan_bumon_name");
		if(isKamokuTani){
			sb.append(" ,B.kamoku_gaibu_cd AS kamoku_gaibu_cd");
			sb.append(" ,B.edaban_code AS edaban_code");
		}
			sb.append(" FROM");
			sb.append("  ki_syuukei_bumon A");
			sb.append("  INNER JOIN (");
			sb.append("              SELECT DISTINCT");
			sb.append("                 ksb.syuukei_bumon_cd AS syuukei_bumon_cd");
			sb.append("                ,ksb.kesn AS kesn");
		if(isKamokuTani){
			sb.append("                ,di.kamoku_gaibu_cd AS kamoku_gaibu_cd");
			sb.append("                ,di.edaban_code AS edaban_code");
		}
			sb.append("               FROM");
			sb.append("                ki_syuukei_bumon ksb");
			sb.append("                INNER JOIN ki_bumon_security kbs ON kbs.futan_bumon_cd = ksb.syuukei_bumon_cd AND kbs.kesn = ksb.kesn");
			sb.append("                INNER JOIN syuukei_bumon sbm ON sbm.syuukei_bumon_cd = ksb.syuukei_bumon_cd");
			sb.append("                INNER JOIN denpyou_info di ON di.futan_bumon_cd = ksb.futan_bumon_cd");
			sb.append("               WHERE ksb.kesn = ?");
			sb.append("                 AND kbs.sptn = ?");
			sb.append("              ) B ON B.syuukei_bumon_cd = A.syuukei_bumon_cd AND B.kesn = A.kesn");
		return sb.toString();
	}
	
	/**
	 * 伝票毎の負担部門、金額を取得するためのSQL
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return sql
	 */
	public String getDenpyouInfo(String denpyouKbn, String denpyouId){
		
		// セキュリティ絞り込み条件
		final String joinKamokuSecurity = 
						  " INNER JOIN kamoku_master km ON km.kamoku_gaibu_cd = shinsei.kari_kamoku_cd"
						+ " INNER JOIN ki_kamoku_security kks ON kks.kamoku_naibu_cd = km.kamoku_naibu_cd AND kks.kesn = ? AND kks.sptn = ?";
		
		if(denpyouKbn.startsWith("B")) {
			// ユーザー定義届書
			String kaniSql = commonLogic.getKanitodokeSql(denpyouKbn, denpyouId);
			
			if(isKamokuTani){
				kaniSql = kaniSql.replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity)
								 .replace("shinsei.kari_kamoku_cd", "kamoku.kamoku_cd");
			}else{
				kaniSql = kaniSql.replace(":JOIN_KAMOKU_SECURITY", "");
			}
			StringBuffer sb = new StringBuffer();
				sb.append("SELECT");
				sb.append("  kani.futan_bumon_cd  AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kani.kamoku_gaibu_cd AS kamoku_gaibu_cd,");
				sb.append("  kani.edaban_code     AS edaban_code,");
			}
				sb.append("  kani.kingaku         AS kingaku");
				sb.append(" FROM ( " + kaniSql + ") kani");
			return sb.toString();
			
		}else {
			switch (denpyouKbn){
			//経費精算
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				return commonLogic.getKeiseisanSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
				
			// 仮払申請
			case DENPYOU_KBN.KARIBARAI_SHINSEI:
				return commonLogic.getKaribaraiSql();
					// 仮払申請は科目が混在することはないため科目セキュリティによる絞り込み不要
				
			//請求書払い申請
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
				return commonLogic.getSeikyuushobaraiSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
					
			//旅費精算
			case DENPYOU_KBN.RYOHI_SEISAN:
				return commonLogic.getRyohiseisanSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
					
			//旅費仮払い申請
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				return commonLogic.getRyohiKaribaraiSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
					
			//海外出張旅費精算
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				return commonLogic.getKaigaiRyohiseisanSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
					
			//海外出張旅費仮払い申請
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				return commonLogic.getKaigaiRyohiKaribaraiSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
					
			//支払依頼申請
			case DENPYOU_KBN.SIHARAIIRAI:
				return commonLogic.getShiharaiIraiSql()
								  .replace(":JOIN_KAMOKU_SECURITY", joinKamokuSecurity);
				
			default:
				return getExtDenpyouInfo(denpyouKbn, denpyouId);
			}
		}
	}
	
	/**
	 * 伝票毎の負担部門、金額を取得するためのSQL（拡張用）
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return sql
	 */
	public String getExtDenpyouInfo(String denpyouKbn, String denpyouId) {
		return "";
	}
	
	/**
	 * 予算取得用のSQLを組み立てる
	 * @param kesn 決算期
	 * @param kessankiBangou 決算期番号
	 * @param yosanCheckNengetsu 予算執行対象月
	 * @param nonEdaBumonKamoku nonEdaBumonKamoku
	 * @return SQL
	 */
	protected String getYosanSql(int kesn, int kessankiBangou, String yosanCheckNengetsu, String nonEdaBumonKamoku){

		// 予算の計算式を組み立てる
		StringBuffer sumYosan = new StringBuffer("0");
		int keikaTsuki = 12;
		// 予算チェック期間が『累計』の場合
		// 予算執行対象月から求めた経過月で上書き
		if(isRuikei) {
			Date targetDate = commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu);
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
		sb.append("      futan_bumon_cd,");
		if(isKamokuTani){
			sb.append("  yosan.kamoku_gaibu_cd,");
			sb.append("  edaban_code,");
		}
		sb.append("      SUM(yosan) AS yosan");
		sb.append(" FROM");
		sb.append("  (");
		
					// 部門科目予算（枝番予算に登録がある科目予算は取らない。枝番予算の登録内容を優先するため）
		sb.append("   SELECT");
		sb.append("     futan_bumon_cd,");
		sb.append("     kamoku_gaibu_cd,");
		sb.append("     '' AS edaban_code,");
		sb.append("     :SUM_YOSAN AS yosan"); // 計算式で置き換える
		sb.append("    FROM");
		sb.append("     bumon_kamoku_yosan");
		sb.append("    WHERE");
		sb.append("          kessanki_bangou = ?");
		sb.append("     :AND_FUTAN_BUMON_NON_EDA");
		sb.append("     AND (futan_bumon_cd, kamoku_gaibu_cd) NOT IN ");
		sb.append("      (SELECT");
		sb.append("         futan_bumon_cd,");
		sb.append("         kamoku_gaibu_cd");
		sb.append("       FROM bumon_kamoku_edaban_yosan");
		sb.append("        WHERE");
		sb.append("              kessanki_bangou = ?");
		sb.append("         :AND_FUTAN_BUMON_NON_EDA");
		sb.append("        GROUP BY futan_bumon_cd, kamoku_gaibu_cd)");
		sb.append("     GROUP BY futan_bumon_cd, kamoku_gaibu_cd");
		
		sb.append("   UNION ALL");
		
					// 部門科目枝番予算
		sb.append("   SELECT");
		sb.append("     futan_bumon_cd,");
		sb.append("     kamoku_gaibu_cd,");
		sb.append("     edaban_code,");
		sb.append("     :SUM_YOSAN AS yosan"); // 計算式で置き換える
		sb.append("    FROM");
		sb.append("     bumon_kamoku_edaban_yosan");
		sb.append("    WHERE");
		sb.append("         kessanki_bangou = ?");
		sb.append("    :AND_FUTAN_BUMON");
		sb.append("   GROUP BY futan_bumon_cd, kamoku_gaibu_cd, edaban_code");
		sb.append("   :UNION_SELECT_YOSAN_NON_KAMOKU_EDABAN"); // 科目枝番が未入力の場合を考慮した置き換え用文字列
		sb.append("   ) yosan");
		sb.append(" INNER JOIN kamoku_master km ON yosan.kamoku_gaibu_cd = km.kamoku_gaibu_cd");
		sb.append(" INNER JOIN ki_kamoku_security ksec ON km.kamoku_naibu_cd = ksec.kamoku_naibu_cd AND ksec.kesn = ?  AND ksec.sptn = ?");
		sb.append(" GROUP BY futan_bumon_cd");
		if(isKamokuTani) sb.append("      , yosan.kamoku_gaibu_cd, edaban_code");
		
		
		String unionSelectYosanSql = "";
		//部門科目単位かつ科目枝番が未入力の場合
		if(nonEdaBumonKamoku.length() != 0){
			unionSelectYosanSql = " UNION ALL"
								+ " SELECT"
								+ "   futan_bumon_cd"
								+ "   , kamoku_gaibu_cd"
								+ "   , '' AS edaban_code"
								+ "   , :SUM_YOSAN AS yosan "
								+ " FROM"
								+ "   bumon_kamoku_edaban_yosan"
								+ " WHERE"
								+ "       kessanki_bangou = " + Integer.toString(kessankiBangou)
								+ " AND (futan_bumon_cd, kamoku_gaibu_cd) IN (" + nonEdaBumonKamoku + ")"
								+ " GROUP BY"
								+ "   futan_bumon_cd"
								+ "   , kamoku_gaibu_cd";
		}
		
		return sb.toString().replace(":UNION_SELECT_YOSAN_NON_KAMOKU_EDABAN", unionSelectYosanSql).replace(":SUM_YOSAN", sumYosan.toString());
	}
	
	/**
	 * 累計実績取得用SQLを組み立てる
	 * @param kesn 決算期
	 * @param kessankiBangou 決算期番号
	 * @param targetDate 対象日付
	 * @param yosanCheckNengetsu 予算執行対象月
	 * @param nonEdaBumonKamoku nonEdaBumonKamoku
	 * @return SQL
	 */
	protected String getRuikeijissekiSql(int kesn, int kessankiBangou, Date targetDate, String yosanCheckNengetsu, String nonEdaBumonKamoku){

		//残高の計算式を組み立てる
		StringBuffer sumZandaka = new StringBuffer("COALESCE(SUM(zandaka0), '0')");
		int keikaTsuki = commonLogic.getCountKeikaTsuki(kesn, targetDate);
		// 予算チェック期間が『累計』の場合
		// 予算執行対象月から求めた経過月で上書き
		if(isRuikei) {
			keikaTsuki = commonLogic.getCountKeikaTsuki(kesn, commonLogic.retYosanCheckNengetsuAddDD(yosanCheckNengetsu));
		}
		for(int i = 0 ; i < keikaTsuki ; i++ ){
			sumZandaka.append(" + COALESCE(SUM(zandaka" + (i+1) + "), '0')");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("      futan_bumon_cd_tmp AS futan_bumon_cd,");
		if(isKamokuTani){
			
			sb.append("  kamoku_gaibu_cd_tmp AS kamoku_gaibu_cd,");
			sb.append("  edaban_code_tmp AS edaban_code,");
		}
		sb.append("      :SUM_ZANDAKA  + COALESCE(SUM(ruikei_kingaku_shiwake), '0') + COALESCE(SUM(kingaku), '0') AS ruikei_shikkoudaka");
		sb.append(" FROM");
		sb.append("     (SELECT");
		sb.append("        CASE WHEN bkz.futan_bumon_cd IS NOT NULL THEN bkz.futan_bumon_cd ");
		sb.append("         WHEN ks.futan_bumon_cd IS NOT NULL THEN ks.futan_bumon_cd ");
		sb.append("         ELSE mj.futan_bumon_cd END AS futan_bumon_cd_tmp,");
		if(isKamokuTani){
			sb.append("    CASE WHEN bkz.kamoku_gaibu_cd IS NOT NULL THEN bkz.kamoku_gaibu_cd ");
			sb.append("         WHEN ks.kamoku_gaibu_cd IS NOT NULL THEN ks.kamoku_gaibu_cd ");
			sb.append("         ELSE mj.kamoku_gaibu_cd END AS kamoku_gaibu_cd_tmp,");
			sb.append("    CASE WHEN bkz.edaban_code IS NOT NULL THEN bkz.edaban_code ");
			sb.append("         WHEN ks.edaban_code IS NOT NULL THEN ks.edaban_code ");
			sb.append("         ELSE mj.edaban_code END AS edaban_code_tmp,");
		}
		sb.append("        bkz.*,");
		sb.append("        ks.*,");
		sb.append("        mj.*");
		sb.append("  FROM");
		
						//部門科目残高+部門科目枝番残高
		sb.append("       (SELECT");
		sb.append("          futan_bumon_cd,");
		if(isKamokuTani){
			sb.append("      bkz_tmp.kamoku_gaibu_cd,");
			sb.append("      edaban_code,");
		}
		sb.append("          SUM(zandaka0) AS zandaka0,");
		sb.append("          SUM(zandaka1) AS zandaka1,");
		sb.append("          SUM(zandaka2) AS zandaka2,");
		sb.append("          SUM(zandaka3) AS zandaka3,");
		sb.append("          SUM(zandaka4) AS zandaka4,");
		sb.append("          SUM(zandaka5) AS zandaka5,");
		sb.append("          SUM(zandaka6) AS zandaka6,");
		sb.append("          SUM(zandaka7) AS zandaka7,");
		sb.append("          SUM(zandaka8) AS zandaka8,");
		sb.append("          SUM(zandaka9) AS zandaka9,");
		sb.append("          SUM(zandaka10) AS zandaka10,");
		sb.append("          SUM(zandaka11) AS zandaka11,");
		sb.append("          SUM(zandaka12) AS zandaka12");
		sb.append("    FROM ");
		
							//部門科目残高（枝番残高が登録されている科目は取らない。枝番残高の登録内容を優先するため）
		sb.append("         (SELECT");
		sb.append("            futan_bumon_cd,");
		sb.append("            kamoku_gaibu_cd,");
		sb.append("            '' AS edaban_code,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari00 - zandaka_kashi00 ELSE zandaka_kashi00 - zandaka_kari00 END AS zandaka0,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari01 - zandaka_kashi01 ELSE zandaka_kashi01 - zandaka_kari01 END AS zandaka1,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari02 - zandaka_kashi02 ELSE zandaka_kashi02 - zandaka_kari02 END AS zandaka2,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari03 + zandaka_kari03_shu - zandaka_kashi03 - zandaka_kashi03_shu ELSE zandaka_kashi03 + zandaka_kashi03_shu - zandaka_kari03 - zandaka_kari03_shu  END AS zandaka3,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari04 - zandaka_kashi04 ELSE zandaka_kashi04 - zandaka_kari04 END AS zandaka4,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari05 - zandaka_kashi05 ELSE zandaka_kashi05 - zandaka_kari05 END AS zandaka5,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari06 + zandaka_kari06_shu - zandaka_kashi06 - zandaka_kashi06_shu ELSE zandaka_kashi06 + zandaka_kashi06_shu - zandaka_kari06 - zandaka_kari06_shu  END AS zandaka6,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari07 - zandaka_kashi07 ELSE zandaka_kashi07 - zandaka_kari07 END AS zandaka7,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari08 - zandaka_kashi08 ELSE zandaka_kashi08 - zandaka_kari08 END AS zandaka8,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari09 + zandaka_kari09_shu - zandaka_kashi09 - zandaka_kashi09_shu ELSE zandaka_kashi09 + zandaka_kashi09_shu - zandaka_kari09 - zandaka_kari09_shu  END AS zandaka9,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari10 - zandaka_kashi10 ELSE zandaka_kashi10 - zandaka_kari10 END AS zandaka10,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari11 - zandaka_kashi11 ELSE zandaka_kashi11 - zandaka_kari11 END AS zandaka11,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari12 + zandaka_kari12_shu - zandaka_kashi12 - zandaka_kashi12_shu ELSE zandaka_kashi12 + zandaka_kashi12_shu - zandaka_kari12 - zandaka_kari12_shu  END AS zandaka12");
		sb.append("          FROM ");
		sb.append("            bumon_kamoku_zandaka");
		sb.append("          WHERE");
		sb.append("                kessanki_bangou = ?");
		sb.append("            :AND_FUTAN_BUMON_NON_EDA");
		
						//部門科目枝番残高が存在する部門・科目は除外
		sb.append("            AND (futan_bumon_cd, kamoku_gaibu_cd) NOT IN ");
		sb.append("            (SELECT");
		sb.append("               futan_bumon_cd,");
		sb.append("               kamoku_gaibu_cd");
		sb.append("             FROM bumon_kamoku_edaban_zandaka");
		sb.append("              WHERE");
		sb.append("                    kessanki_bangou = ?");
		sb.append("               :AND_FUTAN_BUMON_NON_EDA");
		sb.append("              GROUP BY futan_bumon_cd, kamoku_gaibu_cd)");
		
		sb.append("         UNION ALL");
		
							//部門科目枝番残高
		sb.append("          SELECT");
		sb.append("            futan_bumon_cd,");
		sb.append("            kamoku_gaibu_cd,");
		sb.append("            edaban_code,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari00 - zandaka_kashi00 ELSE zandaka_kashi00 - zandaka_kari00 END AS zandaka0,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari01 - zandaka_kashi01 ELSE zandaka_kashi01 - zandaka_kari01 END AS zandaka1,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari02 - zandaka_kashi02 ELSE zandaka_kashi02 - zandaka_kari02 END AS zandaka2,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari03 + zandaka_kari03_shu - zandaka_kashi03 - zandaka_kashi03_shu ELSE zandaka_kashi03 + zandaka_kashi03_shu - zandaka_kari03 - zandaka_kari03_shu  END AS zandaka3,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari04 - zandaka_kashi04 ELSE zandaka_kashi04 - zandaka_kari04 END AS zandaka4,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari05 - zandaka_kashi05 ELSE zandaka_kashi05 - zandaka_kari05 END AS zandaka5,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari06 + zandaka_kari06_shu - zandaka_kashi06 - zandaka_kashi06_shu ELSE zandaka_kashi06 + zandaka_kashi06_shu - zandaka_kari06 - zandaka_kari06_shu  END AS zandaka6,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari07 - zandaka_kashi07 ELSE zandaka_kashi07 - zandaka_kari07 END AS zandaka7,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari08 - zandaka_kashi08 ELSE zandaka_kashi08 - zandaka_kari08 END AS zandaka8,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari09 + zandaka_kari09_shu - zandaka_kashi09 - zandaka_kashi09_shu ELSE zandaka_kashi09 + zandaka_kashi09_shu - zandaka_kari09 - zandaka_kari09_shu  END AS zandaka9,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari10 - zandaka_kashi10 ELSE zandaka_kashi10 - zandaka_kari10 END AS zandaka10,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari11 - zandaka_kashi11 ELSE zandaka_kashi11 - zandaka_kari11 END AS zandaka11,");
		sb.append("            CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari12 + zandaka_kari12_shu - zandaka_kashi12 - zandaka_kashi12_shu ELSE zandaka_kashi12 + zandaka_kashi12_shu - zandaka_kari12 - zandaka_kari12_shu  END AS zandaka12");
		sb.append("          FROM ");
		sb.append("            bumon_kamoku_edaban_zandaka");
		sb.append("          WHERE");
		sb.append("                kessanki_bangou = ?");
		sb.append("            :AND_FUTAN_BUMON");
		sb.append("         :UNION_SELECT_ZANDAKA_NON_KAMOKU_EDABAN"); // 科目枝番未入力の場合の置き換え用文字列
		sb.append("         ) bkz_tmp");
		
							//予算執行対象外科目は除外
		sb.append("         INNER JOIN kamoku_master km ON bkz_tmp.kamoku_gaibu_cd = km.kamoku_gaibu_cd");
		sb.append("         INNER JOIN ki_kamoku_security ksec ON km.kamoku_naibu_cd = ksec.kamoku_naibu_cd AND ksec.kesn = ?  AND ksec.sptn = ?");
		sb.append("         GROUP BY  futan_bumon_cd");
		if(isKamokuTani) sb.append(", bkz_tmp.kamoku_gaibu_cd, edaban_code");
		sb.append("    ) bkz");
		
		
					//部署入出力仕訳を結合
		sb.append("      FULL OUTER JOIN");
		sb.append("       (SELECT");
		sb.append("          futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kamoku_gaibu_cd");
			sb.append("     ,edaban_code");
		}
		sb.append("         ,SUM(valu) AS ruikei_kingaku_shiwake");
		sb.append("        FROM");
		sb.append("         base_busho_shiwake kbs");
						//予算執行対象外科目は除外
		sb.append("        INNER JOIN ki_kamoku_security kks ON kks.kesn = kbs.kesn AND kks.kamoku_naibu_cd = kbs.kamoku_naibu_cd");
		sb.append("        INNER JOIN kamoku_master km ON km.kamoku_naibu_cd = kks.kamoku_naibu_cd");
		sb.append("        WHERE");
		sb.append("              kbs.kesn = ?");
		sb.append("          AND kks.sptn = ?");
		sb.append("         :AND_FUTAN_BUMON");
		sb.append("         GROUP BY  futan_bumon_cd");
		if(isKamokuTani) sb.append(", kamoku_gaibu_cd, edaban_code");
		sb.append("    ) ks");
		sb.append("                   ON ks.futan_bumon_cd = bkz.futan_bumon_cd");
		if(isKamokuTani) sb.append(" AND ks.kamoku_gaibu_cd = bkz.kamoku_gaibu_cd AND ks.edaban_code = bkz.edaban_code");
		
		
					//みなし実績を外部結合
		sb.append("  FULL OUTER JOIN");
		sb.append("   main_minashi mj");
		sb.append("                   ON mj.futan_bumon_cd = bkz.futan_bumon_cd");
		if(isKamokuTani) sb.append(" AND mj.kamoku_gaibu_cd = bkz.kamoku_gaibu_cd AND mj.edaban_code = bkz.edaban_code");
		
		
		sb.append("  ) ruikei");
		sb.append("          GROUP BY futan_bumon_cd_tmp");
		if(isKamokuTani) sb.append(", kamoku_gaibu_cd_tmp, edaban_code_tmp");
		
		
		String unionSelectZandakaSql = "";
		//部門科目単位かつ科目枝番が未入力の場合
		if(nonEdaBumonKamoku.length() != 0){
			unionSelectZandakaSql = " UNION ALL"
								  + " SELECT"
								  + "   futan_bumon_cd"
								  + "   ,kamoku_gaibu_cd"
								  + "   ,'' AS edaban_code"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari00 - zandaka_kashi00 ELSE zandaka_kashi00 - zandaka_kari00 END AS zandaka0"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari01 - zandaka_kashi01 ELSE zandaka_kashi01 - zandaka_kari01 END AS zandaka1"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari02 - zandaka_kashi02 ELSE zandaka_kashi02 - zandaka_kari02 END AS zandaka2"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari03 + zandaka_kari03_shu - zandaka_kashi03 - zandaka_kashi03_shu ELSE zandaka_kashi03 + zandaka_kashi03_shu - zandaka_kari03 - zandaka_kari03_shu  END AS zandaka3"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari04 - zandaka_kashi04 ELSE zandaka_kashi04 - zandaka_kari04 END AS zandaka4"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari05 - zandaka_kashi05 ELSE zandaka_kashi05 - zandaka_kari05 END AS zandaka5"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari06 + zandaka_kari06_shu - zandaka_kashi06 - zandaka_kashi06_shu ELSE zandaka_kashi06 + zandaka_kashi06_shu - zandaka_kari06 - zandaka_kari06_shu  END AS zandaka6"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari07 - zandaka_kashi07 ELSE zandaka_kashi07 - zandaka_kari07 END AS zandaka7"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari08 - zandaka_kashi08 ELSE zandaka_kashi08 - zandaka_kari08 END AS zandaka8"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari09 + zandaka_kari09_shu - zandaka_kashi09 - zandaka_kashi09_shu ELSE zandaka_kashi09 + zandaka_kashi09_shu - zandaka_kari09 - zandaka_kari09_shu  END AS zandaka9"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari10 - zandaka_kashi10 ELSE zandaka_kashi10 - zandaka_kari10 END AS zandaka10"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari11 - zandaka_kashi11 ELSE zandaka_kashi11 - zandaka_kari11 END AS zandaka11"
								  + "   ,CASE WHEN taishaku_zokusei = '0' THEN zandaka_kari12 + zandaka_kari12_shu - zandaka_kashi12 - zandaka_kashi12_shu ELSE zandaka_kashi12 + zandaka_kashi12_shu - zandaka_kari12 - zandaka_kari12_shu  END AS zandaka12"
								  + " FROM "
								  + "   bumon_kamoku_edaban_zandaka"
								  + " WHERE"
								  + "       kessanki_bangou = " + Integer.toString(kessankiBangou)
								  + " AND (futan_bumon_cd, kamoku_gaibu_cd) IN (" + nonEdaBumonKamoku + ")";
		}
		
		return sb.toString().replace(":UNION_SELECT_ZANDAKA_NON_KAMOKU_EDABAN", unionSelectZandakaSql).replace(":SUM_ZANDAKA", sumZandaka.toString());
	}

}
