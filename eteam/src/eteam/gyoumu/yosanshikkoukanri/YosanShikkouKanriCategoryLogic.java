package eteam.gyoumu.yosanshikkoukanri;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.SettingInfoAbstractDao;
import eteam.database.dao.SettingInfoDao;
import lombok.Getter;


/**
 * 予算執行管理の共通Ligic
 */
public class YosanShikkouKanriCategoryLogic extends EteamAbstractLogic {
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(YosanShikkouKanriCategoryLogic.class);

	/**
	 * 画面表示用マップのキー項目定義
	 */
	public class MAP_ID{
		/** 集計部門コード */
		public static final String SYUUKEI_BUMON_CD = "syuukeiBumonCd";
		/** 集計部門名称 */
		public static final String SYUUKEI_BUMON_NAME = "syuukeiBumonName";
		/** 明細部門コード */
		public static final String MEISAI_BUMON_CD = "meisaiBumonCd";
		/** 明細部門名称 */
		public static final String MEISAI_BUMON_NAME = "meisaiBumonName";
		/** 科目コード */
		public static final String KAMOKU_CD = "kamokuCd";
		/** 科目名称 */
		public static final String KAMOKU_NAME = "kamokuName";
		/** 科目枝番コード */
		public static final String KAMOKU_EDABAN_CD = "kamokuEdabanCd";
		/** 科目枝番名称 */
		public static final String KAMOKU_EDABAN_NAME = "kamokuEdabanName";
		/** 予算／起案額 */
		public static final String KIANGAKU = "kiangaku";
		/** 累計実績／支出依頼済み額 */
		public static final String JISSEKIGAKU = "jissekigaku";
		/** 申請額 */
		public static final String SHINSEIGAKU = "shinseigaku";
		/** 予算残／起案残高 */
		public static final String ZANDAKA = "zandaka";
		/** 判定コメント */
		public static final String JUDGE_COMMENT = "judgeComment";
		/** 超過判定比率 */
		public static final String RATE = "rate";
		/** 超過判定結果（"0":範囲内 "1":超過） */
		public static final String JUDGE = "judge";
	}

	/** 予算チェック粒度フラグ */
	@Getter
	protected boolean isKamokuTani = EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(setting.yosanCheckTani());
	
	/**
	 * 予算・起案金額チェックテーブルのレコードを取得する
	 * @param denpyouId 伝票ID
	 * @return 予算・起案金額チェック保存済みデータ
	 */
	public List<GMap> loadData(String denpyouId){
		String sql = 
				"SELECT "
			+ "  syuukei_bumon_cd       AS syuukeiBumonCd,"
			+ "  TRIM(kamoku_gaibu_cd)  AS kamokuCd,"
			+ "  TRIM(kamoku_edaban_cd) AS kamokuEdabanCd,"
			+ "  TRIM(futan_bumon_cd)   AS meisaiBumonCd,"
			+ "  syuukei_bumon_name     AS syuukeiBumonName,"
			+ "  kamoku_name_ryakushiki AS kamokuName,"
			+ "  edaban_name            AS kamokuEdabanName,"
			+ "  futan_bumon_name       AS meisaiBumonName,"
			+ "  kijun_kingaku          AS kiangaku,"
			+ "  jissekigaku,"
			+ "  shinsei_kingaku        AS shinseigaku,"
			+ "  kijun_kingaku - (jissekigaku + shinsei_kingaku) AS zandaka,"
			+ "  CASE WHEN kijun_kingaku = '0' THEN 101"
			+ "  ELSE trunc(((jissekigaku + shinsei_kingaku) / kijun_kingaku * 100), 0) END AS rate"
			+ " FROM yosan_kiankingaku_check"
			+ " WHERE denpyou_id = ?"
			+ " ORDER BY syuukei_bumon_cd, kamoku_gaibu_cd, kamoku_edaban_cd, futan_bumon_cd";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 予算・起案金額チェックテーブルにレコードを新規保存する
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuCd 科目コード
	 * @param edabanCd 科目枝番コード
	 * @param meisaiBumonCd 明細部門コード
	 * @param syuukeiBumonName 集計部門名称
	 * @param kamokuName 科目名称
	 * @param edabanName 科目枝番
	 * @param meisaiBumonName 明細部門名称
	 * @param kijunKingaku 基準金額
	 * @param jissekigaku 累計金額
	 * @param shinseigaku 申請金額
	 * @return 処理件数
	 */
	@Deprecated
	public int insertData(
			String denpyouId,
			String syuukeiBumonCd,
			String kamokuCd,
			String edabanCd,
			String meisaiBumonCd,
			String syuukeiBumonName,
			String kamokuName,
			String edabanName,
			String meisaiBumonName,
			BigDecimal kijunKingaku,
			BigDecimal jissekigaku,
			BigDecimal shinseigaku){
		if ("".equals(kamokuCd))
		{
			kamokuCd = "        ";
		}
		if ("".equals(edabanCd))
		{
			edabanCd = "            ";
		}
		if ("".equals(meisaiBumonCd))
		{
			meisaiBumonCd = "        ";
		}
		String sql =
				"INSERT INTO yosan_kiankingaku_check "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return connection.update(
				sql, denpyouId, syuukeiBumonCd, kamokuCd, edabanCd, meisaiBumonCd, syuukeiBumonName, kamokuName, edabanName, meisaiBumonName, kijunKingaku, jissekigaku, shinseigaku);
	}
	
	/**
	 * 予算・起案金額チェックテーブルのレコードを削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	@Deprecated
	public int deleteData(String denpyouId){
		String sql = "DELETE FROM yosan_kiankingaku_check WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}

	/**
	 * 予算・起案金額チェックコメントを取得する
	 * @param denpyouId 伝票ID
	 * @return 金額チェックコメント
	 */
	@Deprecated
	public String findComment(String denpyouId){
		String sql = "SELECT comment FROM yosan_kiankingaku_check_comment WHERE denpyou_id = ?";
		GMap map = connection.find(sql, denpyouId);
		return (null == map)? "" : map.get("comment").toString();
	}
	
	/**
	 * 予算・起案金額チェックコメントを登録する
	 * @param denpyouId 伝票ID
	 * @param comment チェックコメント
	 * @return 処理件数
	 */
	@Deprecated
	public int insertComment(String denpyouId, String comment){
		String sql = "INSERT INTO yosan_kiankingaku_check_comment VALUES(?, ?)";
		return connection.update(sql, denpyouId, comment);
	}
	
	/**
	 * 予算・起案金額チェックコメントテーブルのレコードを削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	@Deprecated
	public int deleteComment(String denpyouId){
		String sql = "DELETE FROM yosan_kiankingaku_check_comment WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 予算・起案金額チェックの金額項目の合計を算出する
	 * @param dataList 予算・起案金額チェックデータ
	 * @return 合計データマップ
	 */
	public GMap getGoukei(List<GMap> dataList){
		BigDecimal sumKingaku = new BigDecimal(0);
		BigDecimal sumJissekigaku = new BigDecimal(0);
		BigDecimal sumShinseigaku = new BigDecimal(0);
		BigDecimal sumZandaka = new BigDecimal(0);
		
		for(GMap map : dataList){
			if("".equals(map.get("meisaiBumonCd"))){
				sumKingaku = sumKingaku.add(new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU).toString()));
				sumJissekigaku = sumJissekigaku.add(new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU).toString()));
				sumShinseigaku = sumShinseigaku.add(new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU).toString()));
				sumZandaka = sumZandaka.add(new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA).toString()));
			}
		}
		GMap ret = new GMap();
		ret.put("syuukeiBumonCd", "");
		ret.put(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU, sumKingaku);
		ret.put(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU, sumJissekigaku);
		ret.put(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU, sumShinseigaku);
		ret.put(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA, sumZandaka);
		
		//執行率
		BigDecimal oneHundred = BigDecimal.valueOf(100);
		int rate = oneHundred.intValue() + 1;
		if(!sumKingaku.equals(BigDecimal.valueOf(0))){
			rate = sumJissekigaku.add(sumShinseigaku).divide(sumKingaku, 2, RoundingMode.UP).multiply(oneHundred).intValue();
		}
		ret.put(YosanShikkouKanriCategoryLogic.MAP_ID.RATE, rate);

		return ret;
	}
	
	/**
	 * 予算・起案金額チェックの超過判定結果を設定する
	 * @param dataList 予算・起案金額チェックデータ
	 * @param choukaKijun 超過基準％
	 */
	public void setJudgeCumment(List<GMap> dataList, int choukaKijun){
		//超過基準による判定
		for(GMap map : dataList){
			if(map.containsKey(YosanShikkouKanriCategoryLogic.MAP_ID.RATE)){
				String judge;
				if (0 == choukaKijun){
					// 超過基準％が 0 の場合は「超過判定なし」のため一律 0:予算内 とする。
					judge = "0";
				}else{
					judge = Integer.parseInt(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.RATE).toString()) > choukaKijun ? "1" : "0";
				}
				map.put(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE, judge);
			}
		}
	}

	/**
	 * 経費精算のSQLを組み立てる
	 * @return sql
	 */
	public String getKeiseisanSql(){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("  kari_futan_bumon_cd     AS futan_bumon_cd,");
			if(isKamokuTani){
				sb.append("  kari_kamoku_cd          AS kamoku_gaibu_cd,");
				sb.append("  kari_kamoku_edaban_cd   AS edaban_code,");
				sb.append("  kari_kamoku_name        AS kamoku_name,");
				sb.append("  kari_kamoku_edaban_name AS kamoku_edaban_name,");
			}
			sb.append("  SUM(" + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + ")   AS kingaku");
			sb.append(" FROM keihiseisan_meisai AS shinsei");
			sb.append(" :JOIN_KAMOKU_SECURITY ");
			sb.append(" WHERE kari_futan_bumon_cd <> ''");
			sb.append("   AND denpyou_id = ?");
			sb.append(" GROUP BY kari_futan_bumon_cd");
			if(isKamokuTani){
				sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
			}
			sb.append(" ORDER BY");
			if(isKamokuTani){
				sb.append("          kari_kamoku_cd, kari_kamoku_edaban_cd, ");
			}
			sb.append("          kari_futan_bumon_cd");
		return sb.toString();
	}
	
	/**
	 * 仮払申請のSQLを組み立てる
	 * @return sql
	 */
	public String getKaribaraiSql(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  kari_futan_bumon_cd AS futan_bumon_cd,");
		if(isKamokuTani){
			sb.append("  kari_kamoku_cd AS kamoku_gaibu_cd,");
			sb.append("  kari_kamoku_edaban_cd AS edaban_code,");
			sb.append("  kari_kamoku_name AS kamoku_name,");
			sb.append("  kari_kamoku_edaban_name AS kamoku_edaban_name,");
		}
		sb.append("  CASE karibarai_on WHEN '1' THEN karibarai_kingaku ELSE kingaku END AS kingaku FROM karibarai");
		sb.append(" WHERE kari_futan_bumon_cd <> ''");
		sb.append("   AND denpyou_id = ?");
		return sb.toString();
	}

	/**
	 * 請求書払い申請のSQLを組み立てる
	 * @return sql
	 */
	public String getSeikyuushobaraiSql(){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  kari_futan_bumon_cd     AS futan_bumon_cd,");
		if(isKamokuTani){
			sb.append("  kari_kamoku_cd          AS kamoku_gaibu_cd,");
			sb.append("  kari_kamoku_edaban_cd   AS edaban_code,");
			sb.append("  kari_kamoku_name        AS kamoku_name,");
			sb.append("  kari_kamoku_edaban_name AS kamoku_edaban_name,");
		}
		sb.append("  SUM(" + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + ") AS kingaku");
		sb.append(" FROM seikyuushobarai_meisai AS shinsei");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		sb.append(" GROUP BY kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
		}
		sb.append(" ORDER BY");
		if(isKamokuTani){
			sb.append("          kari_kamoku_cd, kari_kamoku_edaban_cd, ");
		}
		sb.append("          kari_futan_bumon_cd");
		return sb.toString();
	}
	
	/**
	 * 出張旅費精算のSQLを組み立てる
	 * @return sql
	 */
	public String getRyohiseisanSql(){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("   kari_futan_bumon_cd     AS futan_bumon_cd");
		if(isKamokuTani){
			sb.append("  ,kari_kamoku_cd          AS kamoku_gaibu_cd");
			sb.append("  ,kari_kamoku_edaban_cd   AS edaban_code");
			sb.append("  ,kari_kamoku_name        AS kamoku_name");
			sb.append("  ,kari_kamoku_edaban_name AS kamoku_edaban_name");
		}
		sb.append("  ,SUM(kingaku)            AS kingaku");
		sb.append(" FROM (");
		// 旅費明細金額
		sb.append("  SELECT");
		sb.append("     r.kari_futan_bumon_cd");
		sb.append("    ,r.kari_kamoku_cd");
		sb.append("    ,r.kari_kamoku_edaban_cd");
		sb.append("    ,r.kari_kamoku_name");
		sb.append("    ,r.kari_kamoku_edaban_name");
		sb.append("    ,rm." + (isZeinuki ? "zeinuki_kingaku" :  "meisai_kingaku") + " AS kingaku");
		sb.append("  FROM ryohiseisan AS r");
		sb.append("  INNER JOIN ryohiseisan_meisai AS rm ON r.denpyou_id = rm.denpyou_id");
		sb.append("  WHERE r.kari_futan_bumon_cd <> ''");
		sb.append("    AND r.denpyou_id = ?");
		// 経費明細-支払金額
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kari_futan_bumon_cd");
		sb.append("    ,kari_kamoku_cd");
		sb.append("    ,kari_kamoku_edaban_cd");
		sb.append("    ,kari_kamoku_name");
		sb.append("    ,kari_kamoku_edaban_name");
		sb.append("    ," + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + " AS kingaku");
		sb.append("  FROM ryohiseisan_keihi_meisai");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		// 差引金額
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kari_futan_bumon_cd");
		sb.append("    ,kari_kamoku_cd");
		sb.append("    ,kari_kamoku_edaban_cd");
		sb.append("    ,kari_kamoku_name");
		sb.append("    ,kari_kamoku_edaban_name");
		sb.append("    ,0-(COALESCE(sashihiki_num, 0) * COALESCE(sashihiki_tanka, 0)) AS kingaku");
		sb.append("  FROM ryohiseisan");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		sb.append(" ) AS shinsei");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append(" GROUP BY kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
		}
		sb.append(" ORDER BY");
		if(isKamokuTani){
			sb.append("          kari_kamoku_cd, kari_kamoku_edaban_cd, ");
		}
		sb.append("          kari_futan_bumon_cd");
		return sb.toString();
	}
	/**
	 * 出張旅費仮払申請のSQLを組み立てる
	 * @return sql
	 */
	public String getRyohiKaribaraiSql(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("   kari_futan_bumon_cd     AS futan_bumon_cd");
		if(isKamokuTani){
			sb.append("  ,kari_kamoku_cd          AS kamoku_gaibu_cd");
			sb.append("  ,kari_kamoku_edaban_cd   AS edaban_code");
			sb.append("  ,kari_kamoku_name        AS kamoku_name");
			sb.append("  ,kari_kamoku_edaban_name AS kamoku_edaban_name");
		}
		sb.append("  ,SUM(kingaku)            AS kingaku");
		sb.append(" FROM (");
		// 旅費明細金額
		sb.append("   SELECT");
		sb.append("      rk.kari_futan_bumon_cd");
		sb.append("     ,rk.kari_kamoku_cd");
		sb.append("     ,rk.kari_kamoku_edaban_cd");
		sb.append("     ,rk.kari_kamoku_name");
		sb.append("     ,rk.kari_kamoku_edaban_name");
		sb.append("     ,COALESCE(rkm.meisai_kingaku, 0) AS kingaku");
		sb.append("   FROM ryohi_karibarai AS rk");
		sb.append("   LEFT OUTER JOIN ryohi_karibarai_meisai AS rkm ON rk.denpyou_id = rkm.denpyou_id");
		sb.append("   WHERE rk.kari_futan_bumon_cd <> ''");
		sb.append("     AND rk.denpyou_id = ?");
		// 経費明細-支払金額
		sb.append("   UNION ALL");
		sb.append("   SELECT");
		sb.append("      kari_futan_bumon_cd");
		sb.append("     ,kari_kamoku_cd");
		sb.append("     ,kari_kamoku_edaban_cd");
		sb.append("     ,kari_kamoku_name");
		sb.append("     ,kari_kamoku_edaban_name");
		sb.append("     ,shiharai_kingaku AS kingaku");
		sb.append("   FROM ryohi_karibarai_keihi_meisai");
		sb.append("   WHERE kari_futan_bumon_cd <> ''");
		sb.append("     AND denpyou_id = ?");
		// 差引金額
		sb.append("   UNION ALL");
		sb.append("   SELECT");
		sb.append("      kari_futan_bumon_cd");
		sb.append("     ,kari_kamoku_cd");
		sb.append("     ,kari_kamoku_edaban_cd");
		sb.append("     ,kari_kamoku_name");
		sb.append("     ,kari_kamoku_edaban_name");
		sb.append("     ,0-(COALESCE(sashihiki_num, 0) * COALESCE(sashihiki_tanka, 0)) AS kingaku");
		sb.append("   FROM ryohi_karibarai");
		sb.append("   WHERE kari_futan_bumon_cd <> ''");
		sb.append("     AND denpyou_id = ?");
		sb.append(" ) AS shinsei");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append(" GROUP BY kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
		}
		sb.append(" ORDER BY");
		if(isKamokuTani){
			sb.append("          kari_kamoku_cd, kari_kamoku_edaban_cd, ");
		}
		sb.append("          kari_futan_bumon_cd");
	return sb.toString();
	}

	/**
	 * 海外出張旅費精算のSQLを組み立てる
	 * @return sql
	 */
	public String getKaigaiRyohiseisanSql(){
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("   kari_futan_bumon_cd     AS futan_bumon_cd");
		if(isKamokuTani){
			sb.append("  ,kari_kamoku_cd          AS kamoku_gaibu_cd");
			sb.append("  ,kari_kamoku_edaban_cd   AS edaban_code");
			sb.append("  ,kari_kamoku_name        AS kamoku_name");
			sb.append("  ,kari_kamoku_edaban_name AS kamoku_edaban_name");
		}
		sb.append("  ,SUM(kingaku)            AS kingaku");
		sb.append(" FROM (");
		// 旅費明細金額（海外）
		sb.append("  SELECT");
		sb.append("     kr.kaigai_kari_futan_bumon_cd     AS kari_futan_bumon_cd");
		sb.append("    ,kr.kaigai_kari_kamoku_cd          AS kari_kamoku_cd");
		sb.append("    ,kr.kaigai_kari_kamoku_edaban_cd   AS kari_kamoku_edaban_cd");
		sb.append("    ,kr.kaigai_kari_kamoku_name        AS kari_kamoku_name");
		sb.append("    ,kr.kaigai_kari_kamoku_edaban_name AS kari_kamoku_edaban_name");
		sb.append("    ,krm." + (isZeinuki ? "zeinuki_kingaku" :  "meisai_kingaku") + "                AS kingaku");
		sb.append("  FROM kaigai_ryohiseisan AS kr");
		sb.append("  INNER JOIN kaigai_ryohiseisan_meisai AS krm ON kr.denpyou_id = krm.denpyou_id");
		sb.append("  WHERE kr.kaigai_kari_futan_bumon_cd <> ''");
		sb.append("    AND krm.kaigai_flg = '1'");
		sb.append("    AND kr.denpyou_id = ?");
		// 旅費明細金額（国内）
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kr.kari_futan_bumon_cd");
		sb.append("    ,kr.kari_kamoku_cd");
		sb.append("    ,kr.kari_kamoku_edaban_cd");
		sb.append("    ,kr.kari_kamoku_name");
		sb.append("    ,kr.kari_kamoku_edaban_name");
		sb.append("    ,krm." + (isZeinuki ? "zeinuki_kingaku" :  "meisai_kingaku") + " AS kingaku");
		sb.append("  FROM kaigai_ryohiseisan AS kr");
		sb.append("  INNER JOIN kaigai_ryohiseisan_meisai AS krm ON kr.denpyou_id = krm.denpyou_id");
		sb.append("  WHERE kr.kari_futan_bumon_cd <> ''");
		sb.append("    AND krm.kaigai_flg = '0'");
		sb.append("    AND kr.denpyou_id = ?");
		// 経費明細-支払金額
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kari_futan_bumon_cd");
		sb.append("    ,kari_kamoku_cd");
		sb.append("    ,kari_kamoku_edaban_cd");
		sb.append("    ,kari_kamoku_name");
		sb.append("    ,kari_kamoku_edaban_name");
		sb.append("    ," + (isZeinuki ? "hontai_kingaku" :  "shiharai_kingaku") + " AS kingaku");
		sb.append("  FROM kaigai_ryohiseisan_keihi_meisai");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		// 差引金額（海外）
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kaigai_kari_futan_bumon_cd     AS kari_futan_bumon_cd");
		sb.append("    ,kaigai_kari_kamoku_cd          AS kari_kamoku_cd");
		sb.append("    ,kaigai_kari_kamoku_edaban_cd   AS kari_kamoku_edaban_cd");
		sb.append("    ,kaigai_kari_kamoku_name        AS kari_kamoku_name");
		sb.append("    ,kaigai_kari_kamoku_edaban_name AS kari_kamoku_edaban_name");
		sb.append("    ,0-(COALESCE(sashihiki_num_kaigai, 0) * COALESCE(sashihiki_tanka_kaigai, 0)) AS kingaku");
		sb.append("  FROM kaigai_ryohiseisan");
		sb.append("  WHERE kaigai_kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		// 差引金額（国内）
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kari_futan_bumon_cd");
		sb.append("    ,kari_kamoku_cd");
		sb.append("    ,kari_kamoku_edaban_cd");
		sb.append("    ,kari_kamoku_name");
		sb.append("    ,kari_kamoku_edaban_name");
		sb.append("    ,0-(COALESCE(sashihiki_num, 0) * COALESCE(sashihiki_tanka, 0)) AS kingaku");
		sb.append("  FROM kaigai_ryohiseisan");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		sb.append(" ) AS shinsei");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append(" GROUP BY kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("         ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
		}
		sb.append(" ORDER BY");
		if(isKamokuTani){
			sb.append("         kari_kamoku_cd, kari_kamoku_edaban_cd, ");
		}
		sb.append("         kari_futan_bumon_cd");
		return sb.toString();
	}
	
	/**
	 *	支払依頼申請のSQLを組み立てる
	 * @return sql
	 */
	public String getShiharaiIraiSql() {
		SettingInfoAbstractDao settingInfoDao = EteamContainer.getComponent(SettingInfoDao.class, connection);
		var isZeinuki = settingInfoDao.find("minashi_shukei_houhou").settingVal.equals("1");
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  kari_futan_bumon_cd     AS futan_bumon_cd,");
		if(isKamokuTani){
			sb.append("  kari_kamoku_cd          AS kamoku_gaibu_cd,");
			sb.append("  kari_kamoku_edaban_cd   AS edaban_code,");
			sb.append("  kari_kamoku_name        AS kamoku_name,");
			sb.append("  kari_kamoku_edaban_name AS kamoku_edaban_name,");
		}
		sb.append("  SUM(" + (isZeinuki ? "zeinuki_kingaku" :  "shiharai_kingaku") + ")   AS kingaku");
		sb.append(" FROM shiharai_irai_meisai AS shinsei");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		sb.append(" GROUP BY kari_futan_bumon_cd");
	if(isKamokuTani){
		sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
	}
		sb.append(" ORDER BY");
	if(isKamokuTani){
		sb.append("          kari_kamoku_cd, kari_kamoku_edaban_cd, ");
	}
		sb.append("          kari_futan_bumon_cd");
		return sb.toString();
	}
	
	/**
	 * 海外出張旅費仮払申請のSQLを組み立てる<br>
	 * SQL構造は出張旅費仮払申請と同じ。テーブルIDを変更している。
	 * @return sql
	 */
	public String getKaigaiRyohiKaribaraiSql(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("   kari_futan_bumon_cd     AS futan_bumon_cd");
		if(isKamokuTani){
			sb.append("  ,kari_kamoku_cd          AS kamoku_gaibu_cd");
			sb.append("  ,kari_kamoku_edaban_cd   AS edaban_code");
			sb.append("  ,kari_kamoku_name        AS kamoku_name");
			sb.append("  ,kari_kamoku_edaban_name AS kamoku_edaban_name");
		}
		sb.append("  ,SUM(kingaku)            AS kingaku");
		sb.append(" FROM (");
		// 旅費明細金額
		sb.append("   SELECT");
		sb.append("      rk.kari_futan_bumon_cd");
		sb.append("     ,rk.kari_kamoku_cd");
		sb.append("     ,rk.kari_kamoku_edaban_cd");
		sb.append("     ,rk.kari_kamoku_name");
		sb.append("     ,rk.kari_kamoku_edaban_name");
		sb.append("     ,COALESCE(rkm.meisai_kingaku, 0) AS kingaku");
		sb.append("   FROM kaigai_ryohi_karibarai AS rk");
		sb.append("   LEFT OUTER JOIN kaigai_ryohi_karibarai_meisai AS rkm ON rk.denpyou_id = rkm.denpyou_id");
		sb.append("   WHERE rk.kari_futan_bumon_cd <> ''");
		sb.append("     AND rk.denpyou_id = ?");
		// 経費明細-支払金額
		sb.append("   UNION ALL");
		sb.append("   SELECT");
		sb.append("      kari_futan_bumon_cd");
		sb.append("     ,kari_kamoku_cd");
		sb.append("     ,kari_kamoku_edaban_cd");
		sb.append("     ,kari_kamoku_name");
		sb.append("     ,kari_kamoku_edaban_name");
		sb.append("     ,shiharai_kingaku AS kingaku");
		sb.append("   FROM kaigai_ryohi_karibarai_keihi_meisai");
		sb.append("   WHERE kari_futan_bumon_cd <> ''");
		sb.append("     AND denpyou_id = ?");
		// 差引金額（海外+国内）
		sb.append("  UNION ALL");
		sb.append("  SELECT");
		sb.append("     kari_futan_bumon_cd");
		sb.append("    ,kari_kamoku_cd");
		sb.append("    ,kari_kamoku_edaban_cd");
		sb.append("    ,kari_kamoku_name");
		sb.append("    ,kari_kamoku_edaban_name");
		sb.append("    ,0-(COALESCE(sashihiki_num_kaigai, 0) * COALESCE(sashihiki_tanka_kaigai, 0))-(COALESCE(sashihiki_num, 0) * COALESCE(sashihiki_tanka, 0)) AS kingaku");
		sb.append("  FROM kaigai_ryohi_karibarai");
		sb.append("  WHERE kari_futan_bumon_cd <> ''");
		sb.append("    AND denpyou_id = ?");
		sb.append(" ) AS shinsei");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append(" GROUP BY kari_futan_bumon_cd");
		if(isKamokuTani){
			sb.append("     ,kari_kamoku_cd, kari_kamoku_edaban_cd, kari_kamoku_name, kari_kamoku_edaban_name");
		}
		sb.append(" ORDER BY");
		if(isKamokuTani){
			sb.append("          kari_kamoku_cd, kari_kamoku_edaban_cd, ");
		}
		sb.append("          kari_futan_bumon_cd");
		return sb.toString();
	}

	/**
	 * ユーザー定義届書申請のSQLを組み立てる
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return sql
	 */
	public String getKanitodokeSql(String denpyouKbn, String denpyouId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("   b.futan_bumon_cd");
		if(isKamokuTani){
	// // 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
			sb.append("  ,COALESCE(kamoku.kamoku_cd,   '') AS kamoku_gaibu_cd");
			sb.append("  ,COALESCE(kamoku.kamoku_name, '') AS kamoku_name");
			sb.append("  ,COALESCE(eda.eda_cd,         '') AS edaban_code");
			sb.append("  ,COALESCE(eda.eda_name,       '') AS kamoku_edaban_name");
		}else{
			sb.append("  ,'' AS kamoku_gaibu_cd");
			sb.append("  ,'' AS kamoku_name");
			sb.append("  ,'' AS edaban_code");
			sb.append("  ,'' AS kamoku_edaban_name");
		}
		sb.append("  ,SUM(k.kingaku) AS kingaku");
		sb.append(" FROM");
		sb.append("  (SELECT");
		sb.append("     denpyou_edano");
		sb.append("    ,value1 AS futan_bumon_cd");
		sb.append("   FROM kani_todoke_meisai");
		sb.append("   WHERE denpyou_id = ?");
		sb.append("     AND item_name = :SHISHUTSU_BUMON) AS b");
		sb.append(" LEFT OUTER JOIN");
		sb.append("  (SELECT");
		sb.append("     denpyou_edano");
		sb.append("    ,value1 AS kamoku_cd");
		sb.append("    ,value2 AS kamoku_name");
		sb.append("   FROM kani_todoke_meisai");
		sb.append("   WHERE denpyou_id = ?");
		sb.append("   AND item_name = :SHISHUTSU_KAMOKU) AS kamoku ON b.denpyou_edano = kamoku.denpyou_edano");
		sb.append(" LEFT OUTER JOIN");
		sb.append("  (SELECT");
		sb.append("     denpyou_edano");
		sb.append("    ,value1 AS eda_cd");
		sb.append("    ,value2 AS eda_name");
		sb.append("   FROM kani_todoke_meisai");
		sb.append("   WHERE denpyou_id = ?");
		sb.append("   AND item_name = :SHISHUTSU_EDA) AS eda ON b.denpyou_edano = eda.denpyou_edano");
		sb.append(" INNER JOIN");
		sb.append("  (SELECT");
		sb.append("     denpyou_edano");
		sb.append("    ,CASE value1 WHEN '' THEN 0 ELSE cast(replace(value1, ',', '') AS BIGINT) END AS kingaku");
		sb.append("   FROM kani_todoke_meisai");
		sb.append("   WHERE denpyou_id = ?");
		sb.append("   AND item_name = :SHISHUTSU_KINGAKU) AS k ON b.denpyou_edano = k.denpyou_edano");
		sb.append(" :JOIN_KAMOKU_SECURITY ");
		sb.append(" WHERE b.futan_bumon_cd <> ''");
		sb.append(" GROUP BY b.futan_bumon_cd");
		if(isKamokuTani){
			// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
			sb.append("        , kamoku.kamoku_cd, kamoku.kamoku_name, eda.eda_cd, eda.eda_name");
		}
		sb.append(" ORDER BY");
		if(isKamokuTani){
			// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
			sb.append("          kamoku.kamoku_cd, eda.eda_cd, ");
		}
		sb.append("          b.futan_bumon_cd");

		String shishutsuBumon   = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_BUMON, denpyouId);
		String shishutsuKingaku = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KINGAKU, denpyouId);

		String sql = sb.toString();
		sql = sql.replace(":SHISHUTSU_BUMON",   "'" + shishutsuBumon + "'");
		sql = sql.replace(":SHISHUTSU_KINGAKU", "'" + shishutsuKingaku + "'");

		String shishutsuKamoku   = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU, denpyouId);
		String shishutsuEda = getkanitodokeItemName(denpyouKbn, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA, denpyouId);
		sql = sql.replace(":SHISHUTSU_KAMOKU",   "'" + shishutsuKamoku + "'");
		sql = sql.replace(":SHISHUTSU_EDA", "'" + shishutsuEda + "'");

		return sql;
	}

	/**
	 * ユーザー定義届書申請のルート判定金額に指定された項目を取得するSQLを組み立てる
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return sql
	 */
	public String getKanitodokeKingakuSql(String denpyouKbn, String denpyouId){
		String koumokuId = null;
		String koumokuId2 = null;
		KihyouNaviCategoryLogic kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(denpyouKbn);
		String routeHanteiKingakuCd = denpyouShubetsuMap.get("route_hantei_kingaku").toString();
		switch(routeHanteiKingakuCd){
		case ROUTE_HANTEI_KINGAKU.RINGI_KINGAKU:
			koumokuId = YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU;
			break;
		case ROUTE_HANTEI_KINGAKU.SHISHUTSU_GOUKEI:
			koumokuId = YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case ROUTE_HANTEI_KINGAKU.SHISHUTSU_SHUUNYUU:
			koumokuId = YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			koumokuId2 = YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			break;
		case ROUTE_HANTEI_KINGAKU.SHUUNYUU_GOUKEI:
			koumokuId = YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			break;
		default:
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		if(null != koumokuId2){
			sb.append("  CASE WHEN COALESCE(k.kingaku,0) > COALESCE(k2.kingaku,0) THEN COALESCE(k.kingaku,0) ELSE COALESCE(k2.kingaku,0) END AS kingaku");
		}else{
			sb.append("  k.kingaku AS kingaku");
		}
		sb.append(" FROM");
		sb.append("  (SELECT");
		sb.append("     denpyou_id");
		sb.append("    ,CASE value1 WHEN '' THEN 0 ELSE cast(replace(value1, ',', '') AS BIGINT) END AS kingaku");
		sb.append("   FROM kani_todoke");
		sb.append("   WHERE item_name = :ROUTE_HANTEI_KINGAKU) AS k");
		if(null != koumokuId2){
			sb.append(" FULL OUTER JOIN");
			sb.append("  (SELECT");
			sb.append("     denpyou_id");
			sb.append("    ,CASE value1 WHEN '' THEN 0 ELSE cast(replace(value1, ',', '') AS BIGINT) END AS kingaku");
			sb.append("   FROM kani_todoke");
			sb.append("   WHERE item_name = :ROUTE_HANTEI_KINGAKU2) AS k2 ON k2.denpyou_id = k.denpyou_id");
		}
		sb.append(" WHERE k.denpyou_id = ?");
		
		String sql = sb.toString();
		if(null != koumokuId2){
			sql = sql.replace(":ROUTE_HANTEI_KINGAKU2", "'" + getkanitodokeItemName(denpyouKbn, koumokuId2, denpyouId) + "'");
		}
		sql = sql.replace(":ROUTE_HANTEI_KINGAKU", "'" + getkanitodokeItemName(denpyouKbn, koumokuId, denpyouId) + "'");

		return sql;
	}

	/**
	 * 予算執行項目が登録された項目名を取得する。
	 * @param denpyouKbn 伝票区分
	 * @param koumokuId 予算執行項目ID
	 * @param denpyouId 伝票ID
	 * @return 項目名
	 */
	public String getkanitodokeItemName(String denpyouKbn, String koumokuId, String denpyouId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.item_name");
		sb.append(" FROM kani_todoke_koumoku AS A");
		sb.append(" INNER JOIN kani_todoke AS B ON B.denpyou_kbn = A.denpyou_kbn AND B.version = A.version");
		sb.append(" WHERE A.denpyou_kbn = ? AND A.yosan_shikkou_koumoku_id = ?");
		sb.append("   AND B.denpyou_id = ?");
		sb.append(" GROUP BY A.item_name");
		List<Object> params = new ArrayList<>();
		params.add(denpyouKbn);
		params.add(koumokuId);
		params.add(denpyouId);
		GMap record = connection.find(sb.toString(), params.toArray());
		return (record == null) ? "" : (String)record.get("item_name");
	}

	/**
	 * 期首から対象日付を含む年月までの経過月を取得する
	 * @param kesn 決算期
	 * @param targetDate 対象日付
	 * @return 期首～対象日付を含む年月までの経過月数
	 */
	public int getCountKeikaTsuki(int kesn, Date targetDate){
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
	public List<GMap> getYosanKnariTaishou(){
		final String sql = "SELECT denpyou_kbn FROM denpyou_shubetsu_ichiran WHERE yosan_shikkou_taishou = 'A' OR yosan_shikkou_taishou = 'C'";
		return connection.load(sql);
	}

	/**
	 * 集計部門取得<br>
	 * 負担部門に対する集計部門を取得する。<br>
	 * 
	 * @param lstFutanBumonCd 負担部門コードのリスト
	 * @param nendo 年度
	 * @return 集計部門情報リスト
	 */
	public List<GMap> getShuukeiBumon(List<Map<String, String>> lstFutanBumonCd, String nendo){
		MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		
		// 検索SQL
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT A.kesn, B.syuukei_bumon_cd, B.syuukei_bumon_name");
		sbSql.append(" FROM ki_kesn A");
		sbSql.append(" INNER JOIN ki_syuukei_bumon B ON B.kesn = A.kesn");
		sbSql.append(" INNER JOIN ki_bumon_security C ON C.kesn = A.kesn AND C.futan_bumon_cd = B.syuukei_bumon_cd");
		sbSql.append(" INNER JOIN syuukei_bumon D ON D.syuukei_bumon_cd = B.syuukei_bumon_cd");
		sbSql.append(" WHERE B.futan_bumon_cd = ?");
		sbSql.append("   AND ? BETWEEN A.from_date AND A.to_date");
		sbSql.append("   AND C.sptn = ?");
		sbSql.append(" GROUP BY A.kesn, B.syuukei_bumon_cd, B.syuukei_bumon_name");
		sbSql.append(" ORDER BY B.syuukei_bumon_cd");

		List<String> lstDuplicate = new ArrayList<String>();
		List<GMap> lstResult = new ArrayList<GMap>(); 
		String fromDate = masterLogic.findKesnFromMMDD(); //年度開始日
		for (Map<String, String> aMap : lstFutanBumonCd){
			String futanBumonCd = aMap.get("futan_bumon_cd");
			Date tmpDate = Date.valueOf(nendo + "-" + fromDate);
			int sptn = Integer.parseInt(setting.yosanSecurityPattern());
			List<GMap> lstLoad = connection.load(sbSql.toString(), futanBumonCd, tmpDate, sptn);
			for (GMap mapLoad : lstLoad){
				// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
				// 科目と科目枝番を集計部門に紐付ける。
				if(this.isKamokuTani()){
					mapLoad.put("kamoku_gaibu_cd", aMap.get("kamoku_gaibu_cd"));
					mapLoad.put("edaban_code", aMap.get("edaban_code"));
					mapLoad.put("kamoku_name", aMap.get("kamoku_name"));
					mapLoad.put("kamoku_edaban_name", aMap.get("kamoku_edaban_name"));
				}

				// 負担部門から集計部門を求めると重複するデータが発生することがあるため
				// 重複は排除する
				StringBuilder sb = new StringBuilder();
				sb.append(mapLoad.get("syuukei_bumon_cd").toString());
				if(this.isKamokuTani()){
					sb.append(mapLoad.get("kamoku_gaibu_cd").toString());
					sb.append(mapLoad.get("edaban_code").toString());
				}
				// データに重複がなければ取得対象とする。
				if (!lstDuplicate.contains(sb.toString())){
					lstDuplicate.add(sb.toString());
					this.log.debug("■■■集計部門リスト:" + mapLoad.toString());
					lstResult.add(mapLoad);
				}
			}
		}
		// 集計部門の並び替えを実施
		lstResult.sort(new SortShuukeiBumon());

		return lstResult;
	}

	/**
	 * 明細部門取得<br>
	 * 集計部門に紐づく明細部門を取得する。<br>
	 * 
	 * @param shuukeiBumonCd 集計部門コード
	 * @param kesn 内部決算期
	 * @return 明細部門情報リスト
	 */
	public List<GMap> getMeisaiBumon(String shuukeiBumonCd, int kesn){
		// 検索SQL
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT futan_bumon_cd, futan_bumon_name");
		sbSql.append(" FROM ki_syuukei_bumon");
		sbSql.append(" WHERE kesn = ? AND syuukei_bumon_cd = ?");
		sbSql.append(" ORDER BY futan_bumon_cd");
		// SQLの実行
		return connection.load(sbSql.toString(), kesn, shuukeiBumonCd);
	}

	/**
	 * 集計部門ソート<br>
	 */
	public class SortShuukeiBumon implements Comparator<GMap> {
		@Override
		public int compare(GMap map1, GMap map2) {
			String bumonCd1 = (null == map1.get("syuukei_bumon_cd")) ? "" : map1.get("syuukei_bumon_cd").toString();
			String bumonCd2 = (null == map2.get("syuukei_bumon_cd")) ? "" : map2.get("syuukei_bumon_cd").toString();
			String kamokuCd1 = (null == map1.get("kamoku_gaibu_cd")) ? "" : map1.get("kamoku_gaibu_cd").toString();
			String kamokuCd2 = (null == map2.get("kamoku_gaibu_cd")) ? "" : map2.get("kamoku_gaibu_cd").toString();
			String edaCd1 = (null == map1.get("edaban_code")) ? "" : map1.get("edaban_code").toString();
			String edaCd2 = (null == map2.get("edaban_code")) ? "" : map2.get("edaban_code").toString();

			int ret = 0;
			if (bumonCd1.equals(bumonCd2)){
				if (kamokuCd1.equals(kamokuCd2)){
					ret = edaCd1.compareTo(edaCd2);
				}else{
					ret = kamokuCd1.compareTo(kamokuCd2);
				}
			}else{
				ret = bumonCd1.compareTo(bumonCd2);
			}
			return ret;
		}
	}
	
	/**
	 * セキュリティパターン一致する科目が存在するか判定する
	 * @param kesn 決算期
	 * @param sptn セキュリティパターン
	 * @param kamokuCd 科目コード
	 * @return 引数として与えた条件と一致するレコードがあればtrue
	 */
	public boolean existKamokuSecurity(int kesn, int sptn, String kamokuCd){
		String sql = "SELECT km.kamoku_gaibu_cd"
				+ "     FROM kamoku_master km"
				+ "    INNER JOIN ki_kamoku_security kks ON kks.kamoku_naibu_cd = km.kamoku_naibu_cd"
				+ "    WHERE kks.kesn = ?"
				+ "      AND kks.sptn = ?"
				+ "      AND km.kamoku_gaibu_cd = ?";
		Map<String, Object> res = connection.find(sql, kesn, sptn, kamokuCd);
		return (res == null)? false : true;
		
	}

	/**
	 * セキュリティパターン一致する集計部門が存在するか判定する
	 * @param kesn 決算期
	 * @param sptn セキュリティパターン
	 * @param futanBumonCd 部門コード
	 * @return 引数として与えた条件と一致するレコードがあればtrue
	 */
	public boolean existBumonSecurity(int kesn, int sptn, String futanBumonCd){
		String sql = "SELECT bm.futan_bumon_cd"
				+ "     FROM bumon_master bm"
				+ "    INNER JOIN ki_syuukei_bumon ksb ON ksb.futan_bumon_cd = bm.futan_bumon_cd"
				+ "    INNER JOIN ki_bumon_security kbsec ON kbsec.kesn = ksb.kesn AND kbsec.futan_bumon_cd = ksb.syuukei_bumon_cd"
				+ "    WHERE kbsec.kesn = ?"
				+ "      AND kbsec.sptn = ?"
				+ "      AND bm.futan_bumon_cd = ?";
		Map<String, Object> res = connection.find(sql, kesn, sptn, futanBumonCd);
		return (res == null)? false : true;
		
	}
	
	/**
	 * ターゲット日付を取得する
	 * @param jisshiNendo 実施年度
	 * @return 日付
	 */
	public Date retTargetDate(String jisshiNendo){
		
		MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		
		Date sysday = new java.sql.Date(System.currentTimeMillis());
		
		//実施年度がブランクの場合は、操作日をreturnする
		if (jisshiNendo.isEmpty())
		{
			 return  sysday;
		}
		
		// 実施年度がブランクでなければ、実施年度＋年度開始日
		String fromDate = masterLogic.findKesnFromMMDD(); //年度開始日
		return Date.valueOf(jisshiNendo + "-" + fromDate);
		
	}
	
	/**
	 * 予算執行対象月の年度を求める
	 * @param yosanCheckNengetsu 予算執行対象年月
	 * @return 予算執行対象月を含むの年度開始日
	 */
	public Date retYosanCheckNengetsuFromDate(String yosanCheckNengetsu) {
		MasterKanriCategoryLogic masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		return masterKanriLogic.findFromDate(masterKanriLogic.findKesn(retYosanCheckNengetsuAddDD(yosanCheckNengetsu)));
	}
	
	/**
	 * 予算執行対象年月の月初を返却
	 * @param yosanCheckNengetsu 予算執行対象年月
	 * @return 予算執行対象年月の月初
	 */
	public Date retYosanCheckNengetsuAddDD(String yosanCheckNengetsu) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			return StringUtils.isEmpty(yosanCheckNengetsu) ?
					null :
					new Date(format.parse(yosanCheckNengetsu.concat("01")).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
