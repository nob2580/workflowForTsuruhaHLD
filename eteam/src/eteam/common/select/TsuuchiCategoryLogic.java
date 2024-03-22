
package eteam.common.select;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.BUMON_CD;
import eteam.common.EteamConst.buhinType;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.HOUJIN_MEISAI_SORT_COLUMN;
import eteam.common.EteamNaibuCodeSetting.KIAN_BANGOU_END;
import eteam.common.EteamNaibuCodeSetting.KIAN_BANGOU_INPUT;
import eteam.common.EteamNaibuCodeSetting.KIAN_BANGOU_UNYOU;
import eteam.common.EteamNaibuCodeSetting.KIDOKU_FLG;
import eteam.common.EteamNaibuCodeSetting.SHIKYU_ICHIRAN_SORT_COLUMN;
import eteam.common.EteamNaibuCodeSetting.SHIKYU_ICHIRAN_SORT_COLUMN_DATE;
import eteam.common.EteamNaibuCodeSetting.SORT_KBN;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
import eteam.common.RegAccess;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.gyoumu.tsuuchi.DenpyouKensakuQueryParameter;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import lombok.Getter;;

/**
 * 通知カテゴリー内のSelect文を集約したLogic
 */
public class TsuuchiCategoryLogic extends EteamAbstractLogic {

    /** 処理便宜上の伝票区分：ユーザー定義届書 */
    protected static final String DUMMY_DENPYOU_KBN_KANI_TODOKE = "B000";

    /** ロガー */
    protected static EteamLogger log = EteamLogger.getLogger(TsuuchiCategoryLogic.class);
    /** WHERE句(伝票・共通) */
    protected static String WHERE_DENPYOU_COMMON = "<DENPYOU_COMMON>";
    /** WHERE句(伝票・個別) */
    protected static String WHERE_DENPYOU_INDI = "<DENPYOU_INDI>";
    /** WHERE句(支給一覧・共通) */
    protected static String WHERE_SHIKYUU_COMMON = "<SHIKYUU_COMMON>";
    /** WHERE句(支給一覧・個別) */
    protected static String WHERE_SHIKYUU_INDI = "<SHIKYUU_INDI>";
    /** WHERE句(法人明細・共通) */
    protected static String WHERE_HOUJIN_COMMON = "<HOUJIN_COMMON>";
    /** WHERE句(法人明細・個別) */
    protected static String WHERE_HOUJIN_INDI = "<HOUJIN_INDI>";
    /** 起案番号がNULLの場合の置換用文字列（起案番号 範囲指定で使用） */
    protected static String KIAN_BANGOU_NULL = "@ ９９９９９９９号";

    /** ソート区分（ユーザー定義届書の集計-起票部門、起票者）**/
    public static final String SORT_KBN_ALL = "s0";
    /** ソート区分（ユーザー定義届書の集計-起票部門）**/
    public static final String SORT_KBN_BUMON = "s1";
    /** ソート区分（ユーザー定義届書の集計-起票者）**/
    public static final String SORT_KBN_USER = "s2";

    /** 消費税関係 */
	protected final Map<String, String> zeiritsuTableNameMap = new HashMap<String, String>()
	{
		{
			put("A001", "keihiseisan_meisai");
			put("A003", "seikyuushobarai_meisai");
			put("A004", "ryohiseisan");
			put("A005", "ryohi_karibarai_keihi_meisai");
			put("A006", "tsuukinteiki");
			put("A007", "furikae");
			put("A008", "tsukekae");
			put("A009", "jidouhikiotoshi_meisai");
			put("A010", "koutsuuhiseisan");
			put("A011", "kaigai_ryohiseisan");
			put("A012", "kaigai_ryohi_karibarai_keihi_meisai");
			put("A013", "shiharai_irai_meisai");
		}
	};
    
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* 通知(tsuuchi) */

    /**
     * 通知テーブルのデータ件数を取得する。データがなければサイズ0。
     * @param userId    ユーザーID
     * @param kidokuFlg 既読フラグ  0:未読 1:既読 2:全て
     * @return 検索結果件数
     */
    public long findTsuuchiCount(String userId, String kidokuFlg){
        StringBuffer sql = new StringBuffer();
        sql.append("      SELECT t1.datacount AS datacount");
        // 通常の伝票種別に関する通知の件数を取得
        sql.append( "     FROM (");
        sql.append( "         SELECT COUNT(t.serial_no) AS datacount ");
        sql.append( "           FROM tsuuchi t ");
        sql.append( "     INNER JOIN shounin_joukyou sj ");
        sql.append( "             ON (t.denpyou_id = sj.denpyou_id AND t.edano = sj.edano) ");
        sql.append( "     INNER JOIN denpyou den ");
        sql.append( "             ON (t.denpyou_id = den.denpyou_id) ");
        sql.append( "     INNER JOIN denpyou_shubetsu_ichiran dsi ");
        sql.append( "             ON (den.denpyou_kbn = dsi.denpyou_kbn) ");
        sql.append( "	       WHERE t.user_id = ? ");
        // 既読フラグ
        if (! KIDOKU_FLG.SUBETE.equals(kidokuFlg)) {
            sql.append("AND kidoku_flg = ?");
        }
		sql.append( "         ) t1");

        GMap datacount = new GMap();
        if (!KIDOKU_FLG.SUBETE.equals(kidokuFlg)) {
        	datacount = connection.find(sql.toString(), userId, kidokuFlg);
        }else {
        	datacount = connection.find(sql.toString(), userId);
        }

        long count = (long)datacount.get("datacount");
        return count;
    }

    /**
     * 通知一覧を取得する
     * @param loginUserId  ユーザーID
     * @param statusSelect ステータス
     * @param sortKbn      ソート区分
     * @param pageNo       ページ番号
     * @param pageMax      １ページ最大表示件数
     * @return 通知一覧リスト
     */
    public List<GMap> loadTsuuchiIchiran(String loginUserId, String statusSelect, String sortKbn, int pageNo, int pageMax) {

        StringBuffer sql = new StringBuffer();
        // 通常の伝票種別に関する通知を取得
        sql.append( "         WITH t1 AS (");
        sql.append( "         SELECT t.serial_no, t.user_id, t.kidoku_flg, sj.joukyou, sj.comment, t.denpyou_id, dsi.denpyou_shubetsu_url, dsi.denpyou_shubetsu, den.denpyou_kbn, t.edano, t.touroku_time, COALESCE(kt.version, 0) AS version, NULL ::text AS zaimu_kyoten_nyuryoku_pattern_no");
        sql.append( "           FROM tsuuchi t ");
        sql.append( "     INNER JOIN shounin_joukyou sj ");
        sql.append( "             ON (t.denpyou_id = sj.denpyou_id AND t.edano = sj.edano) ");
        sql.append( "     INNER JOIN denpyou den ");
        sql.append( "             ON (t.denpyou_id = den.denpyou_id) ");
        sql.append( "     INNER JOIN denpyou_shubetsu_ichiran dsi ");
        sql.append( "             ON (den.denpyou_kbn = dsi.denpyou_kbn) ");
        sql.append( "LEFT OUTER JOIN (SELECT DISTINCT denpyou_id, version FROM kani_todoke) kt ");
        sql.append( "             ON (t.denpyou_id = kt.denpyou_id) ");
        sql.append( "	       WHERE t.user_id = ? ");
        // 既読フラグ
        if (!KIDOKU_FLG.SUBETE.equals(statusSelect)) {
            sql.append( " AND t.kidoku_flg = ? ");
        }
		sql.append( "         )");

        // 上記副問い合わせの結果を結合して並び替え
		sql.append( "  SELECT * FROM t1 ");

        log.debug("ソート番号" + sortKbn);

        /* 条件①：ソート区分 */
        if (sortKbn == null || sortKbn.isEmpty()) {
            // デフォルトは通知日降順でソート
            sql.append(" ORDER BY touroku_time DESC ");
        } else if ("1".equals(sortKbn)) {
            // ソート区分"1":通知件名昇順
            sql.append(" ORDER BY joukyou ASC, denpyou_id DESC, edano DESC ");
        } else if ("2".equals(sortKbn)) {
            // ソート区分"2":通知件名降順
            sql.append(" ORDER BY joukyou DESC, denpyou_id DESC, edano DESC ");
        } else if ("3".equals(sortKbn)) {
            // ソート区分"3":伝票ID昇順
            sql.append(" ORDER BY denpyou_id ASC, edano DESC ");
        } else if ("4".equals(sortKbn)) {
            // ソート区分"4":伝票ID降順
            sql.append(" ORDER BY denpyou_id DESC, edano DESC ");
        } else if ("5".equals(sortKbn)) {
            // ソート区分"5":通知日昇順
            sql.append(" ORDER BY touroku_time ASC ");
        } else if ("6".equals(sortKbn)) {
            // ソート区分"6":通知日降順
            sql.append(" ORDER BY touroku_time DESC ");
        }

        /* 条件②：ページ番号（取得件数）*/
        sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));

        if (!KIDOKU_FLG.SUBETE.equals(statusSelect)) {
            return connection.load(sql.toString(), loginUserId, statusSelect);
        } else {
            return connection.load(sql.toString(), loginUserId);
        }
    }


/* メール通知設定(mail_tsuuchi) */
    /**
     * メール通知設定テーブルのデータを取得します。
     * @param userId  ユーザーID
     * @return 検索結果
     */
    @Deprecated
    public List<GMap> loadMailTsuuchiSettei(String userId) {
        final String sql = "SELECT tsuuchi_kbn, soushinumu FROM mail_tsuuchi WHERE user_id = ?";
        return connection.load(sql, userId);
    }

    /**
     * メール通知設定より指定したユーザー、通知区分のデータを取得。
     * @param userId      ユーザーID
     * @param tsuuchiKbn  通知区分
     * @return 検索結果 true:送信する
     */
    @Deprecated
    public boolean findMailTsuuchiSettei(String userId , String tsuuchiKbn) {
        final String sql = "SELECT * FROM mail_tsuuchi WHERE user_id = ? AND tsuuchi_kbn = ?";
        GMap record = connection.find(sql, userId, tsuuchiKbn);
        return (null != record && "1".equals(record.get("soushinumu")));
    }

/* 伝票種別一覧(denpyou_shubetsu_ichiran) */
    /**
     * 伝票種別の一覧を表示順で取得します。
     * @return 検索結果
     */
    @Deprecated
    public List<GMap> loadDenpyouShubetsu() {
        final String sql = "SELECT denpyou_kbn, denpyou_shubetsu FROM denpyou_shubetsu_ichiran ORDER BY hyouji_jun";
        return connection.load(sql);
    }

    /**
     * 業務種別の一覧を取得します。
     * * @return 検索結果
     */
    @Deprecated
    public List<GMap> loadGyoumuShubetsu() {
        final String sql = "SELECT gyoumu_shubetsu, MIN(hyouji_jun) AS hyouji_jun FROM denpyou_shubetsu_ichiran GROUP BY gyoumu_shubetsu ORDER BY hyouji_jun";
        return connection.load(sql);
    }

    /**
     * 伝票一覧の件数を取得する。（メニュー用）
     * @param user ユーザ
     * @param kanrenDenpyou 関連伝票
     * @return 検索結果件数
     */
    public long findDenpyouIchiranKensakuCount(User user, String kanrenDenpyou){
    	var queryParameter = new DenpyouKensakuQueryParameter();
    	queryParameter.user = user;
    	queryParameter.kanrenDenpyou = kanrenDenpyou;
    	queryParameter.kianBangouInput = KIAN_BANGOU_INPUT.MISHITEI;
    	queryParameter.kianBangouEnd = KIAN_BANGOU_END.MISHITEI;
    	queryParameter.torisageHininHyoujiFlg = "1"; // メニューではそもそも関係ないので無視
    	
        // WhereSQL追加(伝票共通)
        long datacount = findDenpyouIchiranKensakuCount(queryParameter);
        return datacount;
    }


    /**
     * 伝票一覧の件数を取得する。（検索用）
     * @param queryParameter クエリパラメーター
     * @return 検索結果件数
     */
    public long findDenpyouIchiranKensakuCount(DenpyouKensakuQueryParameter queryParameter){

        // パラメータ
        List<Object> params = new ArrayList<Object>();
        // MainSQL
        String sqlMain = this.getDenpyouIchiranTableSql(params,queryParameter.user);


        sqlMain = addWhereSqlDenpyouIchiranTable(
                sqlMain
                , params
                , queryParameter
                );

        GMap datacount = connection.find("SELECT COUNT(denpyou_id) AS datacount FROM (" + sqlMain + ") rec", params.toArray());
        long count = (long)datacount.get("datacount");

        return count;
    }


    /**
     * 伝票一覧を取得する。（検索用テーブルから取得）
     * @param queryParameter クエリパラメーター
     * @param pageNo  ページ番号 ※0の場合、全件検索する。
     * @param pageMax 1ページ最大表示件数
     * @param sort ソート条件 ※ ""の場合標準ソート
     * @return 検索結果
     */
    public List<GMap> loadDenpyouIchiranKensaku(
    		DenpyouKensakuQueryParameter queryParameter
            , int pageNo
            , int pageMax
            , String sort){


        // パラメータ
        List<Object> params = new ArrayList<Object>();
        // MainSQL
        String sqlMain = this.getDenpyouIchiranTableSql(params, queryParameter.user);


        sqlMain = addWhereSqlDenpyouIchiranTable(
                sqlMain
                , params
                , queryParameter
                );

        // ソート指定
        String order = makeOrder(sort);

        StringBuffer sql = new StringBuffer("SELECT * FROM (" + sqlMain + ") rec").append(" ORDER BY ").append(order).append(",denpyou_id DESC");

        // ページ番号（取得件数）
        if(pageNo != 0){
            sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));
        }

        // ロード結果に補足と税率を追加したものを返す
        return this.addZeiritsu(this.addHosoku(connection.load(sql.toString(),params.toArray())));
    }

    /**
     * 既に長大になっているSQLをいじるのはリスキーなので、個別に補足の追加検索をかける
     * 20231024 インボイス前後フラグもここで取得
     * @param map マップ
     * @return 結果
     */
    protected List<GMap> addHosoku(List<GMap> map)
    {
    	List<String> tableNameList = new ArrayList<String>();

    	Map<String, String> tableNameMap = new HashMap<String, String>()
    	{
    		{
    			put("A001", "keihiseisan");
    			put("A002", "karibarai");
    			put("A003", "seikyuushobarai");
    			put("A004", "ryohiseisan");
    			put("A005", "ryohi_karibarai");
    			put("A006", "tsuukinteiki");
    			put("A007", "furikae");
    			put("A008", "tsukekae");
    			put("A009", "jidouhikiotoshi");
    			put("A010", "koutsuuhiseisan");
    			put("A011", "kaigai_ryohiseisan");
    			put("A012", "kaigai_ryohi_karibarai");
    			put("A013", "shiharai_irai");
    		}
    	};

    	List<GMap> queryTargetMapList = this.createQueryTargetMapList(map, tableNameList, tableNameMap, "hosoku");

    	Map<String, List<GMap>> groupedTargetMapList = queryTargetMapList.stream().collect(
                Collectors.groupingBy(item -> item.get("denpyou_kbn")));

    	// テーブル種別ごとに検索をかける
    	for(var denpyouKbn : groupedTargetMapList.entrySet())
    	{
    		List<GMap> groupedMap = denpyouKbn.getValue();
    		List<List<GMap>> splitList = new ArrayList<List<GMap>>();

    		// 検索条件が不規則的な時は100件ごとに検索すると高速（開発部で発見されたHACK）
    		for(int i = 0; i < groupedMap.size(); i += 100)
    		{
    			splitList.add(groupedMap.subList(i, Math.min(i + 100, groupedMap.size())));
    		}

    		// 共通ヘッダーは別変数として定義しておく（気付かずにループ内で永遠に文字が追加される仕様になっていたので）
    		String initialSql = "SELECT denpyou_id";
    		//振替、通勤定期以外は補足を取得
    		if(!List.of("A006","A007").contains(denpyouKbn.getKey())) {
    			initialSql += ", hosoku";
    		}
    		//仮払い系以外はインボイス前後フラグも取得
    		if(!List.of("A002","A005","A012").contains(denpyouKbn.getKey())) {
    			initialSql += ", invoice_denpyou";
    		}
    		initialSql += " FROM " + tableNameMap.get(denpyouKbn.getKey()) + " WHERE denpyou_id IN (";

    		for(var splitItem : splitList)
    		{
    			// SQLのスコープをループ内に制限
    			String sql = initialSql + "'" + splitItem.stream().map(denpyou -> denpyou.getString("denpyou_id")).collect(Collectors.joining("', '")) + "')";

    			var hosokuMap = this.connection.load(sql, (Object[])null);

    			// 紐づけるためにマップを作っておく
    			Map<String, GMap> splitMap = splitItem.stream().collect(Collectors.toMap(denpyou -> denpyou.get("denpyou_id"), denpyou -> denpyou));

    			// 紐づけ
    			// GMapは参照型なのでここでの代入はmapにも反映される
    			for(GMap hosokuItem : hosokuMap)
    			{
    				splitMap.get(hosokuItem.get("denpyou_id")).put("hosoku", hosokuItem.get("hosoku"));
    				splitMap.get(hosokuItem.get("denpyou_id")).put("invoice_denpyou", hosokuItem.get("invoice_denpyou"));
    			}
    		}
    	}
    	
    	// 補足はソート対象外になったので戻す
    	return map;
    }

    /**
     * 既に長大になっているSQLをいじるのはリスキーなので、個別に税率の追加検索をかける
     * @param map マップ
     * @return 結果
     */
    protected List<GMap> addZeiritsu(List<GMap> map)
    {
    	List<String> tableNameList = new ArrayList<String>();

    	List<GMap> queryTargetMapList = this.createQueryTargetMapList(map, tableNameList, this.zeiritsuTableNameMap, "zeiritsu");
    	
    	var systemZeiritsuList = this.connection.load("SELECT sort_jun, zeiritsu, keigen_zeiritsu_kbn FROM shouhizeiritsu ORDER BY sort_jun", (Object[])null);

    	Map<String, List<GMap>> groupedTargetMapList = queryTargetMapList.stream().collect(
                Collectors.groupingBy(item -> item.get("denpyou_kbn")));

    	// テーブル種別ごとに検索をかける
    	for(var denpyouKbn : groupedTargetMapList.entrySet())
    	{
    		List<GMap> groupedMap = denpyouKbn.getValue();
    		List<List<GMap>> splitList = new ArrayList<List<GMap>>();

    		// 検索条件が不規則的な時は100件ごとに検索すると高速（開発部で発見されたHACK）
    		for(int i = 0; i < groupedMap.size(); i += 100)
    		{
    			splitList.add(groupedMap.subList(i, Math.min(i + 100, groupedMap.size())));
    		}

    		String tableName = this.zeiritsuTableNameMap.get(denpyouKbn.getKey());
    		// 明細を含む場合とでロジックを切り分け
    		boolean isMeisai = tableName.contains("meisai");
    		boolean isRyohiSeisan = tableName.contains("ryohiseisan");
    		String initialSql;
    		// 共通ヘッダーは別変数として定義しておく
    		if(tableName.equals("furikae")) {
    			initialSql = "SELECT denpyou_id, t.kari_zeiritsu, t.kari_keigen_zeiritsu_kbn FROM " + tableName + " t"
    					+ (isMeisai ? " INNER JOIN shouhizeiritsu s ON t.kari_zeiritsu = s.zeiritsu AND t.kari_keigen_zeiritsu_kbn = s.keigen_zeiritsu_kbn" : "") + " WHERE denpyou_id IN (";
    		}else {
    			initialSql = "SELECT denpyou_id, t.zeiritsu, t.keigen_zeiritsu_kbn FROM " + tableName + " t"
    					+ (isMeisai ? " INNER JOIN shouhizeiritsu s ON t.zeiritsu = s.zeiritsu AND t.keigen_zeiritsu_kbn = s.keigen_zeiritsu_kbn" : "") + " WHERE denpyou_id IN (";
    		}
    		String sqlSuffixForMeisai = " ORDER BY s.sort_jun";

    		for(var splitItem : splitList)
    		{
    			// SQLのスコープをループ内に制限
    			String sql = initialSql + "'" + splitItem.stream().map(denpyou -> denpyou.getString("denpyou_id")).collect(Collectors.joining("', '")) + "')"
    					+ (isMeisai ? sqlSuffixForMeisai : "");

    			var zeiritsuMap = this.connection.load(sql, (Object[])null);
    			
    			// 旅費精算系2種では旅費精算経費明細を考慮
    			if(isRyohiSeisan)
    			{
    				zeiritsuMap.addAll(
    					this.connection.load(
    						sql.replace("ryohiseisan t", "ryohiseisan_keihi_meisai t INNER JOIN shouhizeiritsu s ON t.zeiritsu = s.zeiritsu AND t.keigen_zeiritsu_kbn = s.keigen_zeiritsu_kbn") + sqlSuffixForMeisai, (Object[])null));
    			}
    			
    			// 税率で重複を除去してソートする
    			zeiritsuMap = zeiritsuMap.stream().map(item -> new ZeiritsuMap(item)).distinct().sorted(Comparator.comparing(item -> systemZeiritsuList.indexOf(
    		        	systemZeiritsuList.stream().filter(ritsu ->
    		        		ritsu.get("zeiritsu").equals(item.get("zeiritsu")) && ritsu.get("keigen_zeiritsu_kbn").equals(item.get("keigen_zeiritsu_kbn")))
    		        			.findFirst().orElse(null)))).map(item -> (GMap)item).collect(Collectors.toList());

    			// 紐づけるためにマップを作っておく
    			Map<String, GMap> splitMap = splitItem.stream().collect(Collectors.toMap(denpyou -> denpyou.get("denpyou_id"), denpyou -> denpyou));

    			// 紐づけ
    			// GMapは参照型なのでここでの代入はmapにも反映される
    			// CSVに合わせて、デフォルトはカンマ区切りで連結
    			for(GMap zeiritsuItem : zeiritsuMap)
    			{
    				var denpyouId = zeiritsuItem.get("denpyou_id");
    				var currentZeiritsu = splitMap.get(denpyouId).get("zeiritsu");
    				if(((String) denpyouId).contains("A007")) {
    					splitMap.get(denpyouId).put("kari_zeiritsu", (isEmpty(currentZeiritsu) ? "" : currentZeiritsu + ",") + (zeiritsuItem.get("kari_keigen_zeiritsu_kbn").equals("1") ? "*" : "") + zeiritsuItem.get("kari_zeiritsu"));
    				}else {
    					splitMap.get(denpyouId).put("zeiritsu", (isEmpty(currentZeiritsu) ? "" : currentZeiritsu + ",") + (zeiritsuItem.get("keigen_zeiritsu_kbn").equals("1") ? "*" : "") + zeiritsuItem.get("zeiritsu"));
    				}
    			}	
    		}
    	}
    	
    	// 税率はソート対象外なのでそのまま戻す
    	return map;
    }
    
    /**
     * @param map データマップ
     * @param tableNameList テーブル名リスト
     * @param tableNameMap テーブル名と区分のマップ
     * @param keyName キー名称
     * @return クエリ対象データのリスト
     */
    protected List<GMap> createQueryTargetMapList(List<GMap> map, List<String> tableNameList, Map<String, String> tableNameMap, String keyName)
    {
    	List<GMap> queryTargetMapList = new ArrayList<GMap>();
    	for(GMap item : map)
    	{
    		String denpyouKbn = item.get("denpyou_kbn");
    		tableNameList.add(denpyouKbn);

    		// 対象データが存在しない場合と、存在する場合で分けておく
    		if(tableNameMap.containsKey(denpyouKbn))
    		{
    			queryTargetMapList.add(item);
    		}
    		else
	    	{
	    		item.put(keyName, "");
	    	}
    	}
    	
    	return queryTargetMapList;
    }
    
    /**
     * @author j_matsumoto
     * 税率をキーでdistinctするために作成
     */
    protected class ZeiritsuMap extends GMap
    {
    	/**
    	 * @param map 通常のマップ
    	 */
    	public ZeiritsuMap(GMap map)
    	{
    		for(String key : map.keySet())
    		{
    			this.put(key, map.get(key));
    		}
    	}
    	
    	@Override
    	public boolean equals(Object map) 
    	{
    		return ZeiritsuMap.class.isInstance(map)
    			&& this.get("denpyou_id").equals(((GMap) map).get("denpyou_id"))
    			&& this.get("zeiritsu").equals(((GMap) map).get("zeiritsu"))
    			&& this.get("keigen_zeiritsu_kbn").equals(((GMap) map).get("keigen_zeiritsu_kbn"));
    	}
    }
    
    /**
     * 伝票一覧の合計金額を取得する。（ユーザー定義届書は除く:検索用）
     * @param queryParameter クエリパラメーター
     * @return 合計金額
     */
    public BigDecimal findDenpyouIchiranKensakuGoukeiKingaku(DenpyouKensakuQueryParameter queryParameter){


        // パラメータ
        List<Object> params = new ArrayList<Object>();
        // MainSQL
        String sqlMain = this.getDenpyouIchiranTableSql(params, queryParameter.user);


        sqlMain = addWhereSqlDenpyouIchiranTable(
                sqlMain
                , params
                , queryParameter
                );

        GMap dataGokeiKingaku = connection.find("SELECT SUM(rec.kingaku) AS datagoukeikingaku FROM (" + sqlMain + " AND SUBSTR(di.denpyou_kbn,1,1)  != 'B' ) rec", params.toArray());

        return (BigDecimal)dataGokeiKingaku.get("datagoukeikingaku");
    }

    /**
     * 伝票一覧情報保持テーブルからのデータ取得用SQLを返却
     * @param params パラメータ
     * @param user ログインユーザ
     * @return SQL
     */
    protected String getDenpyouIchiranTableSql(List<Object> params, User user){
        //ユーザー参照可能伝票IDテーブルで絞込み
        String sql =
                  " SELECT "
                + "   di.*,ds.denpyou_shubetsu,ds.gyoumu_shubetsu,sh.bumon_cd as kihyouBumonCd,ui.shain_no as uiShainNo, uni.invoice_denpyou"
                + " FROM denpyou_ichiran di "
                + " INNER JOIN shounin_route sh ON "
                + "       di.denpyou_id = sh.denpyou_id "
                + "   AND sh.edano = 1 "
                + " LEFT OUTER JOIN user_info ui ON "
                + "       di.user_id = ui.user_id "
                + " LEFT OUTER JOIN (SELECT denpyou_id, invoice_denpyou FROM keihiseisan"
    			+ " UNION SELECT denpyou_id, invoice_denpyou FROM seikyuushobarai"
        		+ " UNION SELECT denpyou_id, invoice_denpyou FROM ryohiseisan"
        		+ " UNION SELECT denpyou_id, invoice_denpyou FROM tsuukinteiki"
        		+ " UNION SELECT denpyou_id, invoice_denpyou FROM furikae"
				+ " UNION SELECT denpyou_id, invoice_denpyou FROM tsukekae"
				+ " UNION SELECT denpyou_id, invoice_denpyou FROM jidouhikiotoshi"
				+ " UNION SELECT denpyou_id, invoice_denpyou FROM koutsuuhiseisan"
        		+ " UNION SELECT denpyou_id, invoice_denpyou FROM kaigai_ryohiseisan"
				+ " UNION SELECT denpyou_id, invoice_denpyou FROM shiharai_irai) uni"
				+ " ON di.denpyou_id = uni.denpyou_id "
                + " INNER JOIN denpyou_shubetsu_ichiran ds ON "
                + "       di.denpyou_kbn = ds.denpyou_kbn "
                + " WHERE 1 = 1 ";
        

        StringBuffer sqlBuf = new StringBuffer("");
        sqlBuf.append(makeAccessWhere(params, user, "di", "sh", true));

        return sql + sqlBuf.toString();
    }


    /** 検索条件SQLを付加する
     * @param sql メインSQL
     * @param params パラメータ
     * @param queryParameter クエリパラメーター
     * @return メインSQL(+伝票共通SQL)
     */
    protected String addWhereSqlDenpyouIchiranTable(
              String sql
            , List<Object> params
            , DenpyouKensakuQueryParameter queryParameter){

        StringBuffer sqlBuf = new StringBuffer("");

        //以下、伝票共通用
        boolean bumonShozokuUserFlag = queryParameter.user.getGyoumuRoleId() == null;

        // 関連伝票
        if(queryParameter.kanrenDenpyou != null)
        {
            String column = "";
            String value = "";

            if(bumonShozokuUserFlag){

                //部門所属ユーザ
                column = "user_id";
                value = queryParameter.user.getSeigyoUserId();
            }
            else
            {
                //業務ロール
                column = "gyoumu_role_id";
                value = queryParameter.user.getGyoumuRoleId();
            }

            switch (queryParameter.kanrenDenpyou) {
                case EteamNaibuCodeSetting.KANREN_DENPYOU.YOUSHORI_ALL:
                    // 要処理伝票全て(000)
                    sqlBuf.append("  AND ( ");
                    if(bumonShozokuUserFlag){
                        sqlBuf.append("  ( di.denpyou_joutai = ? AND di."+column+" = ? ) OR ");
                        params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU);
                        params.add(value);
                    }
                    sqlBuf.append("   ( di.denpyou_joutai = ? AND ? = ANY(string_to_array(di.gen_"+column+",'╂')) ) ");
                    sqlBuf.append("  ) ");
                    params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU);
                    params.add(value);
                    break;
                case EteamNaibuCodeSetting.KANREN_DENPYOU.KIHYOU_CHUU:
                    // 起票中(010)
                    if(bumonShozokuUserFlag){
                        sqlBuf.append("  AND ( di.denpyou_joutai = ? AND di."+column+" = ? )");
                        params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU);
                        params.add(value);
                    }else{
                        sqlBuf.append("  AND 1 <> 1 ");
                    }
                    break;
                case EteamNaibuCodeSetting.KANREN_DENPYOU.SHOUNIN_MACHI:
                    // 承認待ち(020)
                    //ここも承認ルートと結びつける(後伺い考慮のため)
                    sqlBuf.append("  AND(");
                    sqlBuf.append("        di.denpyou_joutai = ?");
                    sqlBuf.append("    AND (  EXISTS(SELECT * FROM shounin_route tmp WHERE ");
                    sqlBuf.append("                 tmp.denpyou_id = di.denpyou_id");
                    sqlBuf.append("             AND tmp." + column + " = ?");
                    sqlBuf.append("             AND tmp.joukyou_edano IS NULL");
                    sqlBuf.append("             AND (EXISTS(SELECT * FROM shounin_route t2 WHERE t2.denpyou_id = tmp.denpyou_id AND t2.edano >= tmp.edano AND t2.genzai_flg = '1') OR NOT EXISTS(SELECT * FROM shounin_route t2 WHERE t2.denpyou_id = tmp.denpyou_id AND t2.genzai_flg='1')))");
                    if(bumonShozokuUserFlag){
                    sqlBuf.append("        OR EXISTS(SELECT * FROM shounin_route_gougi_ko tmp WHERE ");
                    sqlBuf.append("                 tmp.denpyou_id = di.denpyou_id");
                    sqlBuf.append("             AND tmp." + column + " = ?");
                    sqlBuf.append("             AND tmp.joukyou_edano IS NULL"); //未承認
                    sqlBuf.append("             AND (tmp.gougi_genzai_flg = '1' "); //該当ユーザが現在承認者
                    sqlBuf.append("                  OR (tmp.gougi_genzai_flg = '0' "); //後伺いを考慮
                    sqlBuf.append("                    AND (EXISTS(SELECT * FROM shounin_route_gougi_ko t2 "); //該当ユーザ以降の合議内に現在承認者がいるなら対象とする
                    sqlBuf.append("                               WHERE t2.denpyou_id = tmp.denpyou_id ");
                    sqlBuf.append("                                 AND t2.edano = tmp.edano ");
                    sqlBuf.append("                                 AND t2.gougi_edano = tmp.gougi_edano ");
                    sqlBuf.append("                                 AND t2.bumon_role_id <> tmp.bumon_role_id ");
                    sqlBuf.append("                                 AND t2.gougi_edaedano > tmp.gougi_edaedano ");
                    sqlBuf.append("                                 AND t2.gougi_genzai_flg = '1') ");
                    sqlBuf.append("                         OR EXISTS(SELECT * FROM shounin_route t3 "); //該当ユーザ以降の承認ルートに現在承認者がいるなら対象とする
                    sqlBuf.append("                               WHERE t3.denpyou_id = tmp.denpyou_id ");
                    sqlBuf.append("                                 AND t3.edano > tmp.edano ");
                    sqlBuf.append("                                 AND t3.genzai_flg = '1') ");
                    sqlBuf.append("                         OR NOT EXISTS(SELECT * FROM shounin_route t4 "); //現在承認者なしで申請中の場合
                    sqlBuf.append("                               WHERE t4.denpyou_id = tmp.denpyou_id ");
                    sqlBuf.append("                                 AND t4.genzai_flg = '1') ");
                    sqlBuf.append("                         ) ");
                    sqlBuf.append("                     ) ");
                    sqlBuf.append("                 ) ");
                    sqlBuf.append("             ) ");
                    }
                    sqlBuf.append("    ))");
                    params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU);
                    params.add(value);
                    if(bumonShozokuUserFlag){
                        params.add(value);
                    }
                    break;
                case EteamNaibuCodeSetting.KANREN_DENPYOU.KAI_MISHOUNIN:
                    // 下位未承認伝票のみ(020)
                    //ここは承認ルートと結びつける
                    sqlBuf.append("  AND (di.denpyou_joutai = ? AND EXISTS (SELECT * FROM shounin_route tmp WHERE tmp.denpyou_id = di.denpyou_id AND tmp." + column + " = ? AND tmp.edano > (SELECT edano FROM shounin_route tmpsub WHERE tmpsub.denpyou_id = tmp.denpyou_id AND tmpsub.genzai_flg = '1')))");
                    params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU);
                    params.add(value);
                    break;
                case EteamNaibuCodeSetting.KANREN_DENPYOU.KIHYOU_ALL:
                    // 起票伝票全て(100)
                    if(bumonShozokuUserFlag){
                        sqlBuf.append("  AND di." + column + " = ?");
                        params.add(value);
                    }else{
                        sqlBuf.append("  AND 1 <> 1 ");
                    }
                    break;
                case EteamNaibuCodeSetting.KANREN_DENPYOU.SHINSEI_CHUU:
                    // 申請中（120）
                    if(bumonShozokuUserFlag){
                        sqlBuf.append("  AND (di.denpyou_joutai = ? AND di." + column + " = ?)");
                        params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU);
                        params.add(value);
                    }else{
                        sqlBuf.append("  AND 1 <> 1 ");
                    }
                    break;
                case EteamNaibuCodeSetting.KANREN_DENPYOU.KIHYOU_OR_SHINSEI:
                    // 起票中 or 申請中（121）
                    if(bumonShozokuUserFlag){
                        sqlBuf.append("  AND ((di.denpyou_joutai = ? AND di." + column + " = ?) OR (di.denpyou_joutai = ? AND di." + column + " = ?))");
                        params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU);
                        params.add(value);
                        params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU);
                        params.add(value);
                    }else{
                        sqlBuf.append("  AND 1 <> 1 ");
                    }
                    break;
            }
        }
        //伝票ID
        super.appendIfNotEmpty(sqlBuf, "  AND di.denpyou_id=?", params, queryParameter.denpyouId);
        //シリアル番号
        super.appendIfNotEmpty(sqlBuf, super.isEmpty(queryParameter.serialNo) ? "" : createSerialNoCondition(queryParameter.serialNo), null, queryParameter.serialNo);

        //伝票状態
        if(!super.isEmpty(queryParameter.denpyouJoutai)){
            if (queryParameter.denpyouJoutai.equals("10_20")) {
                sqlBuf.append("  AND di.denpyou_joutai IN ('10','20')");
            } else {
                sqlBuf.append("  AND di.denpyou_joutai=?");
                params.add(queryParameter.denpyouJoutai);
            }
        }
        //業務種別
        super.appendIfNotEmpty(sqlBuf, "  AND ds.gyoumu_shubetsu=?", params, queryParameter.gyoumuShubetsu);
        
        //伝票種別
        super.appendIfNotEmpty(sqlBuf, "  AND di.denpyou_kbn=?", params, queryParameter.denpyouShubetsu);
        
        if(RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())){
            // 起案番号：起案年度
        	this.appendIf(!super.isEmpty(queryParameter.kianBangouNendo), sqlBuf, "  AND (di.jisshi_nendo = ? OR di.shishutsu_nendo = ?)", params, queryParameter.kianBangouNendo, queryParameter.kianBangouNendo);
            // 起案番号：起案略号
        	this.appendWhereKianBangou(queryParameter.kianBangouRyakugou, sqlBuf, params, "%", "%");
            // 起案番号：起案番号（検索は後方一致を行うため kianBangou は全角の必要あり：呼び元で要変換）
        	this.appendWhereKianBangou(queryParameter.kianBangou, sqlBuf, params, "%", "号");
            // 起案番号 範囲指定：起案番号略号
        	this.appendWhereKianBangou(queryParameter.kianBangouRyakugouFromTo, sqlBuf, params, "%", "%");
        	
            // 起案番号 範囲指定：起案番号From-To
            // 0. 開始は空なら1、終了は空なら起案番号最大値で固定
            //   1.NULLIFで起案番号がブランクならNULLが返却される
            //   2.COALESCEで起案番号がNULLなら起案番号を'@ ９９９９９９９号'とする（起案番号は６桁なので検索対象外となる）
            //   3.SUBSTRで番号部分を取得（スペースの位置＋１から、号の位置－スペースの位置＋１の文字数）
            //   4.TRANSLATEで番号部分を全角文字から半角文字に変換
            //   5.CASTで番号部分を数値に変換して範囲指定条件を付ける
            if (!super.isEmpty(queryParameter.kianBangouFrom) || !super.isEmpty(queryParameter.kianBangouTo)){
            	var actualKianBangouFrom = super.isEmpty(queryParameter.kianBangouFrom) ? "1" : queryParameter.kianBangouFrom;
            	var actualKianBangouTo = super.isEmpty(queryParameter.kianBangouTo) ? String.valueOf(EteamConst.kianBangou.MAX_VALUE) : queryParameter.kianBangouTo;
            	
                sqlBuf.append("  AND (CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(di.jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sqlBuf.append("                            ,STRPOS(COALESCE(NULLIF(di.jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sqlBuf.append("                            ,STRPOS(COALESCE(NULLIF(di.jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(di.jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sqlBuf.append("                     , '０１２３４５６７８９', '0123456789')");
                sqlBuf.append("            AS INTEGER)");
                sqlBuf.append("       BETWEEN ").append(actualKianBangouFrom).append(" AND ").append(actualKianBangouTo);
                sqlBuf.append("       OR");
                sqlBuf.append("       CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(di.shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sqlBuf.append("                            ,STRPOS(COALESCE(NULLIF(di.shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sqlBuf.append("                            ,STRPOS(COALESCE(NULLIF(di.shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(di.shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sqlBuf.append("                     , '０１２３４５６７８９', '0123456789')");
                sqlBuf.append("            AS INTEGER)");
                sqlBuf.append("       BETWEEN ").append(actualKianBangouFrom).append(" AND ").append(actualKianBangouTo);
                sqlBuf.append("      )");
            }
            //起案番号入力
            if(queryParameter.kianBangouInput != null){
                if(KIAN_BANGOU_INPUT.ARI.equals(queryParameter.kianBangouInput)){
                    sqlBuf.append("  AND di.kian_bangou_input = '1' ");
                }else if(KIAN_BANGOU_INPUT.NASHI.equals(queryParameter.kianBangouInput)){
                    sqlBuf.append("  AND di.kian_bangou_input <> '1' ");
                }
            }
            //起案番号終了
            if(queryParameter.kianBangouEnd != null){
                if(KIAN_BANGOU_END.YES.equals(queryParameter.kianBangouEnd)){
                    sqlBuf.append("  AND di.kian_syuryou_flg = '1'");
                }else if(KIAN_BANGOU_END.NO.equals(queryParameter.kianBangouEnd)){
                    sqlBuf.append("  AND di.kian_syuryou_flg <> '1'");
                }
            }
            //起案番号運用
            if(queryParameter.kianBangouUnyou != null){
                if(KIAN_BANGOU_UNYOU.ARI.equals(queryParameter.kianBangouUnyou)){
                    sqlBuf.append("  AND di.kian_bangou_unyou_flg = '1'");
                }else if(KIAN_BANGOU_UNYOU.NASHI.equals(queryParameter.kianBangouUnyou)){
                    sqlBuf.append("  AND di.kian_bangou_unyou_flg <> '1'");
                }
            }
            //起案番号あり且つ支出依頼なし分抽出
            if(!super.isEmpty(queryParameter.kianBangouNoShishutsuIrai)){
                // 1.AA+AB+AC で実施起案伝票で実施起案番号を持っている伝票のリスト
                // 2.AD1+AD2+AD3 で支出依頼伝票で実施起案番号を持っている伝票のリスト
                // 3.(AA+AB+AC)と(AD1+AD2+AD3)をLEFT OUTER JOINの実施起案番号で結合して支出依頼伝票を持たない伝票IDを返す
                sqlBuf.append("  AND di.denpyou_id IN (");
                sqlBuf.append("        SELECT AA.denpyou_id");
                sqlBuf.append("        FROM denpyou AS AA");
                sqlBuf.append("        INNER JOIN denpyou_shubetsu_ichiran AS AB ON AB.denpyou_kbn = AA.denpyou_kbn");
                sqlBuf.append("        INNER JOIN denpyou_kian_himozuke AS AC ON AC.denpyou_id = AA.denpyou_id");
                sqlBuf.append("        LEFT OUTER JOIN (");
                sqlBuf.append("         SELECT AD1.denpyou_id, AD2.yosan_shikkou_taishou, AD3.jisshi_nendo, AD3.jisshi_kian_bangou");
                sqlBuf.append("         FROM denpyou AS AD1");
                sqlBuf.append("         INNER JOIN denpyou_shubetsu_ichiran AS AD2 ON AD2.denpyou_kbn = AD1.denpyou_kbn");
                sqlBuf.append("         INNER JOIN denpyou_kian_himozuke AS AD3 ON AD3.denpyou_id = AD1.denpyou_id");
                sqlBuf.append("         WHERE AD2.yosan_shikkou_taishou = 'C' AND AD3.jisshi_kian_bangou IS NOT NULL");
                sqlBuf.append("        ) AS AD ON AD.jisshi_nendo = AC.jisshi_nendo");
                sqlBuf.append("               AND AD.jisshi_kian_bangou = AC.jisshi_kian_bangou");
                sqlBuf.append("        WHERE AB.yosan_shikkou_taishou = 'A'");
                sqlBuf.append("          AND AC.jisshi_kian_bangou IS NOT NULL");
                sqlBuf.append("          AND AD.denpyou_id IS NULL");
                // 1.AA+AB+AC で支出起案伝票で実施起案番号を持っている伝票のリスト
                // 2.AD1+AD2+AD3 で支出依頼伝票で実施起案番号を持っている伝票のリスト
                // 3.(AA+AB+AC)と(AD1+AD2+AD3)をLEFT OUTER JOINの実施起案番号と支出起案番号で結合して支出依頼伝票を持たない伝票IDを返す
                sqlBuf.append("        UNION");
                sqlBuf.append("        SELECT AA.denpyou_id");
                sqlBuf.append("        FROM denpyou AS AA");
                sqlBuf.append("        INNER JOIN denpyou_shubetsu_ichiran AS AB ON AB.denpyou_kbn = AA.denpyou_kbn");
                sqlBuf.append("        INNER JOIN denpyou_kian_himozuke AS AC ON AC.denpyou_id = AA.denpyou_id");
                sqlBuf.append("        LEFT OUTER JOIN (");
                sqlBuf.append("         SELECT AD1.denpyou_id, AD2.yosan_shikkou_taishou, AD3.jisshi_nendo, AD3.jisshi_kian_bangou, AD3.shishutsu_nendo , AD3.shishutsu_kian_bangou");
                sqlBuf.append("         FROM denpyou AS AD1");
                sqlBuf.append("         INNER JOIN denpyou_shubetsu_ichiran AS AD2 ON AD2.denpyou_kbn = AD1.denpyou_kbn");
                sqlBuf.append("         INNER JOIN denpyou_kian_himozuke AS AD3 ON AD3.denpyou_id = AD1.denpyou_id");
                sqlBuf.append("         WHERE AD2.yosan_shikkou_taishou = 'C' AND AD3.jisshi_kian_bangou IS NOT NULL");
                sqlBuf.append("        ) AS AD ON AD.jisshi_nendo = AC.jisshi_nendo");
                sqlBuf.append("               AND AD.jisshi_kian_bangou = AC.jisshi_kian_bangou");
                sqlBuf.append("               AND AD.shishutsu_nendo = AC.shishutsu_nendo");
                sqlBuf.append("               AND AD.shishutsu_kian_bangou = AC.shishutsu_kian_bangou");
                sqlBuf.append("        WHERE AB.yosan_shikkou_taishou = 'B'");
                sqlBuf.append("          AND AC.shishutsu_kian_bangou IS NOT NULL");
                sqlBuf.append("          AND AD.denpyou_id IS NULL");
                sqlBuf.append("      )");
            }
            //予算執行伝票種別
            if(queryParameter.yosanshikkouShubetsu != null
            		&& Arrays.stream(queryParameter.yosanshikkouShubetsu).anyMatch(item -> !super.isEmpty(item)))
            {
                //予算執行対象が1つ以上選択されている場合
            	// 以前のコードがURLパラメーターのスペルミスで結果的に正しく動いていたところ、URLパラメーターのスペルが正しくても動くように修正
            	var yosanshikkouShubetsuWithData = Arrays.stream(queryParameter.yosanshikkouShubetsu).filter(item -> !super.isEmpty(item)).toArray(String[]::new);
                for(int i=0 ; i < yosanshikkouShubetsuWithData.length ; i++){
                    if(0 == i){
                        sqlBuf.append("  AND (");
                    }else{
                        sqlBuf.append("       OR");
                    }
                    sqlBuf.append("           di.yosan_shikkou_taishou_cd = ?");
                    params.add(yosanshikkouShubetsuWithData[i]);
                }
                sqlBuf.append(")");
            }
        }
        //予算執行対象月
       super.appendIf(!super.isEmpty(queryParameter.yosanCheckNengetsuFrom) || !super.isEmpty(queryParameter.yosanCheckNengetsuTo), sqlBuf, "  AND di.yosan_check_nengetsu <> ''", null);
       super.appendIfNotEmpty(sqlBuf, "  AND di.yosan_check_nengetsu >= ?", params, queryParameter.yosanCheckNengetsuFrom);
       super.appendIfNotEmpty(sqlBuf, "  AND di.yosan_check_nengetsu <= ?", params, queryParameter.yosanCheckNengetsuTo);
       
        //起票日付
        this.appendWhereDate(queryParameter.kihyouBiFrom, queryParameter.kihyouBiTo, sqlBuf, "di.touroku_time", params);
        //起票者所属部門コード
        super.appendIfNotEmpty(sqlBuf, "  AND di.bumon_cd = ?", params, queryParameter.kihyouShozokuBumonCd);
        //起票者所属部門名
        super.appendIfNotEmptyForLikeQuery(sqlBuf, "di.bumon_full_name", params, queryParameter.kihyouShozokuBumonName);
        
        //起票者名
        if(!super.isEmpty(queryParameter.kihyouShozokuUserName)){
            appenLikeUserName(sqlBuf, params, "di.", queryParameter.kihyouShozokuUserName);
        }
        //起票者社員番号
        super.appendIfNotEmpty(sqlBuf, "  AND di.user_id = (SELECT user_id FROM user_info WHERE shain_no = ? ) ", params, queryParameter.kihyouShozokuUserShainNo);

        //承認日付や承認者関連項目が一つでも指定されている場合には条件を追加する
        if(queryParameter.shouninBiFrom != null || queryParameter.shouninBiTo != null
        		|| !super.isEmpty(queryParameter.shouninsyaShozokuBumonCd) || !super.isEmpty(queryParameter.shouninsyaShozokuBumonName)
        		|| !super.isEmpty(queryParameter.shouninsyaShozokuUserName) || !super.isEmpty(queryParameter.shouninsyaShozokuUserShainNo)){
            sqlBuf.append("  AND (di.denpyou_joutai IN (?, ?) AND EXISTS (SELECT * FROM shounin_joukyou sj WHERE sj.denpyou_id = di.denpyou_id AND sj.edano > 1 AND sj.joukyou_cd = '4'");
            params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU);
            params.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
            
            // 承認日
            this.appendWhereDate(queryParameter.shouninBiFrom, queryParameter.shouninBiTo, sqlBuf, "sj.touroku_time", params);
            //承認者所属部門コード
            super.appendIfNotEmpty(sqlBuf, "  AND sj.bumon_cd = ?", params, queryParameter.shouninsyaShozokuBumonCd);
            //承認者所属部門名
            super.appendIfNotEmptyForLikeQuery(sqlBuf, "sj.bumon_full_name", params, queryParameter.shouninsyaShozokuBumonName);
            
            //承認者名
            if(!super.isEmpty(queryParameter.shouninsyaShozokuUserName)){
                appenLikeUserName(sqlBuf, params, "sj.", queryParameter.shouninsyaShozokuUserName);
            }
            //承認者社員番号
            super.appendIfNotEmpty(sqlBuf, "  AND sj.user_id = (SELECT user_id FROM user_info WHERE shain_no = ? ) ", params, queryParameter.shouninsyaShozokuUserShainNo);

            sqlBuf.append("))");
        }
        // 所有者
        //所有者所属部門コード
        // gen_bumon_cdがないため、過去分や影響範囲を鑑み、カラム追加ではなく承認ルートの現在フラグを取ってこさせる
        // gougiの場合はgougi_koの部門コード
        super.appendIf(!super.isEmpty(queryParameter.shoyuushaShozokuBumonCd), sqlBuf,
        		"  AND EXISTS(SELECT target.bumon_cd, target.genzai_flg FROM shounin_route target LEFT JOIN shounin_route_gougi_ko target_gougi ON target.denpyou_id = target_gougi.denpyou_id AND target.edano = target_gougi.edano"
        		+ " WHERE di.denpyou_id = target.denpyou_id AND (target.bumon_cd = ? OR target_gougi.bumon_cd = ?) AND target.genzai_flg = '1')",
        		params, queryParameter.shoyuushaShozokuBumonCd, queryParameter.shoyuushaShozokuBumonCd);
        //所有者所属部門名
        super.appendIfNotEmptyForLikeQuery(sqlBuf, "di.gen_bumon_full_name", params, queryParameter.shoyuushaShozokuBumonName);
        
        //所有者名
        super.appendIfNotEmptyForLikeQuery(sqlBuf, "di.gen_user_full_name", params, queryParameter.shoyuushaShozokuUserName);
        
        //所有者社員番号
        String nameCondition = super.isEmpty(queryParameter.shoyuushaShozokuUserName) ? "" : "AND unify_kana_width(CONCAT(ui_tmp.user_sei, ui_tmp.user_mei)) LIKE unify_kana_width('%" + queryParameter.shoyuushaShozokuUserName.replaceAll("[ 　]", "") + "%')"; // 名称非空欄なら名称一致もチェックする
        super.appendIfNotEmpty(sqlBuf, "  AND di.gen_user_id ~*('(^|╂)' || (SELECT user_id FROM user_info ui_tmp WHERE shain_no = ? " + nameCondition + ") || '(╂|$)') ", params, queryParameter.shoyuushaShozokuUserShainNo);
        
        //金額
        super.appendIfNotEmpty(sqlBuf, " AND di.kingaku >= ? ", params, queryParameter.kingakuFrom);
        super.appendIfNotEmpty(sqlBuf, " AND di.kingaku <= ? ", params, queryParameter.kingakuTo);

        //明細金額
        this.appendWhereUnnestedRange(queryParameter.meisaiKingakuFrom, queryParameter.meisaiKingakuTo, sqlBuf, "meisai_kingaku", params);

        // 消費税率
        if(!super.isEmpty(queryParameter.queryZeiritsu))
        {
        	var sqlPrefix = " OR (di.denpyou_kbn = '";
        	
        	sqlBuf.append(" AND (1=0");
        	for(var key : this.zeiritsuTableNameMap.keySet())
        	{
        		var furikae = key.contains("A007") ? "kari_" : ""; 
        		var sqlMiddle = "' AND EXISTS(SELECT target." + furikae +"zeiritsu, target." + furikae +"keigen_zeiritsu_kbn FROM ";
        		var sqlSuffix = " target WHERE di.denpyou_id = target.denpyou_id AND target." + furikae +"zeiritsu = ? AND target." + furikae +"keigen_zeiritsu_kbn = ?))";
        		var value = this.zeiritsuTableNameMap.get(key);
        		var additionalSql = sqlPrefix + key + sqlMiddle + value + sqlSuffix;
        		sqlBuf.append(additionalSql);
        		params.add(Double.parseDouble(queryParameter.queryZeiritsu));
        		params.add(queryParameter.keigenZeiritsuKbn);
        		
        		// 旅費精算系では経費明細も考慮する
        		if(key.contains("ryohiseisan"))
        		{
            		sqlBuf.append(additionalSql.replace("ryohiseisan", "ryohiseisan_keihi_meisai"));
	        		params.add(Double.parseDouble(queryParameter.queryZeiritsu));
	        		params.add(queryParameter.keigenZeiritsuKbn);
        		}
        	}
        	sqlBuf.append(")");
        }
        
        //支払日
        this.appendWhereDate(queryParameter.shiharaiBiFrom, queryParameter.shiharaiBiTo, sqlBuf, "di.shiharaibi", params);
        // 支払希望日
        this.appendWhereDate(queryParameter.shiharaiKiboubiFrom, queryParameter.shiharaiKiboubiTo, sqlBuf, "di.shiharaikiboubi", params);
        // 計上日
        this.appendWhereDate(queryParameter.keijoubiFrom, queryParameter.keijoubiTo, sqlBuf, "di.keijoubi", params);

        // 仕訳計上日
        this.appendWhereDate(queryParameter.shiwakeKeijoubiFrom, queryParameter.shiwakeKeijoubiTo, sqlBuf, "di.shiwakekeijoubi", params);
        if(queryParameter.shiwakeKeijoubiFrom != null || queryParameter.shiwakeKeijoubiTo != null) {
            List<String> denpyouShubetsuList = new ArrayList<>();
            if("3".equals(setting.shiwakeSakuseiHouhouA001())) {
                denpyouShubetsuList.add( DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
            }
            if("3".equals(setting.shiwakeSakuseiHouhouA009())) {
                denpyouShubetsuList.add(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU);
            }
            if("3".equals(setting.shiwakeSakuseiHouhouA004())) {
                denpyouShubetsuList.add(DENPYOU_KBN.RYOHI_SEISAN);
            }
            if("3".equals(setting.shiwakeSakuseiHouhouA011())) {
                denpyouShubetsuList.add(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN);
            }
            if("3".equals(setting.shiwakeSakuseiHouhouA010())) {
                denpyouShubetsuList.add(DENPYOU_KBN.KOUTSUUHI_SEISAN);
            }
            if(!denpyouShubetsuList.isEmpty()) {
                String denpyouShubetsuListMakeIn = EteamCommon.makeIn(denpyouShubetsuList);
                sqlBuf.append(" AND di.denpyou_kbn not in (" + denpyouShubetsuListMakeIn + ")") ;
            }
        }

        // 添付ファイル・e文書
        boolean isWithTenpuFile = "1".equals(queryParameter.tenpuFileFlg);
        super.appendIfNotEmpty(sqlBuf
    		, " AND " + (isWithTenpuFile ? "" : "NOT ") + "EXISTS(SELECT target.denpyou_id FROM tenpu_file target WHERE target.denpyou_id = di.denpyou_id)", null, queryParameter.tenpuFileFlg);
        
        if(isWithTenpuFile)
        {
        	// e文書の必要箇所を検索する共通SQL（重くなるbinary_dataや、他の検索で使わない列はカット）
        	var baseSqlForEbunsho = "EXISTS(SELECT f.denpyou_id, f.ebunsho_no, denshitorihiki_flg, tsfuyo_flg, ebunsho_shubetsu, ebunsho_nengappi, ebunsho_kingaku, ebunsho_hakkousha "
        			+ "	FROM ebunsho_file f INNER JOIN ebunsho_data d on f.ebunsho_no = d.ebunsho_no WHERE f.denpyou_id = di.denpyou_id ";
        	
        	// 指定添付ファイル種別処理
        	var shubetsuList =List.of(queryParameter.tenpuFileShubetsu);
        	if(shubetsuList.stream().anyMatch(item -> !super.isEmpty(item) && Integer.parseInt(item) >= 0))
        	{
        		sqlBuf.append(" AND (1=0 ");
        		// 添付ファイルかつe文書でないものが存在⇔tenpu_fileとebunsho_fileの行数が異なる
        		super.appendIf(shubetsuList.contains("0"), sqlBuf, 
        				" OR ( SELECT COUNT(*) FROM tenpu_file t WHERE t.denpyou_id = di.denpyou_id ) != ( SELECT COUNT(*) FROM ebunsho_file e WHERE e.denpyou_id = di.denpyou_id )", null);
        		
        		if(shubetsuList.stream().anyMatch(item -> List.of("1", "2", "3").contains(item)))
        		{
        			sqlBuf.append(" OR " + baseSqlForEbunsho + " AND (1=0 ");
        			// もう少しスマートに整理できそうな気がするが、一旦そのままで
        			super.appendIf(shubetsuList.contains("1"), sqlBuf, " OR (denshitorihiki_flg = '0' AND tsfuyo_flg = '0')", null); // スキャン
        			super.appendIf(shubetsuList.contains("2"), sqlBuf, " OR (denshitorihiki_flg = '1' AND tsfuyo_flg = '1')", null); // タイムスタンプあり・電子取引
        			super.appendIf(shubetsuList.contains("3"), sqlBuf, " OR (denshitorihiki_flg = '1' AND tsfuyo_flg = '0')", null); // タイムスタンプ対象外電子取引
        			sqlBuf.append("))");
        		}
        		
        		sqlBuf.append(")");
        	}
        	
        	// 各入力項目が空ではないのなら追加条件検索する
        	if(Arrays.stream(new Object[] {queryParameter.ebunshoShubetsu, queryParameter.ebunshoNengappiFrom, queryParameter.ebunshoNengappiTo, queryParameter.ebunshoNengappiMinyuuryokuFlg,
    			queryParameter.ebunshoKingakuFrom, queryParameter.ebunshoKingakuTo, queryParameter.ebunshoKingakuMinyuuryokuFlg, queryParameter.ebunshoHakkousha, queryParameter.ebunshoHakkoushaMinyuuryokuFlg})
    			.anyMatch(item -> !super.isEmpty(item)))
        	{
        		sqlBuf.append(" AND " + baseSqlForEbunsho);
        		// 種別
        		super.appendIfNotEmpty(sqlBuf, " AND ebunsho_shubetsu = ? ", params, super.isEmpty(queryParameter.ebunshoShubetsu) ? null : Integer.parseInt(queryParameter.ebunshoShubetsu));
        		
        		// 年月日
        		super.appendWhereDate(queryParameter.ebunshoNengappiFrom, queryParameter.ebunshoNengappiTo, sqlBuf, "ebunsho_nengappi", params);
        		super.appendIfNotEmpty(sqlBuf, " AND ebunsho_nengappi IS NULL ", null, queryParameter.ebunshoNengappiMinyuuryokuFlg);
        		
        		// 金額
                super.appendIfNotEmpty(sqlBuf, " AND ebunsho_kingaku >= ? ", params, queryParameter.ebunshoKingakuFrom);
                super.appendIfNotEmpty(sqlBuf, " AND ebunsho_kingaku <= ? ", params, queryParameter.ebunshoKingakuTo);
        		super.appendIfNotEmpty(sqlBuf, " AND ebunsho_kingaku IS NULL ", null, queryParameter.ebunshoKingakuMinyuuryokuFlg);
        		
        		// 発行者
                super.appendIfNotEmptyForLikeQuery(sqlBuf, "ebunsho_hakkousha", params, queryParameter.ebunshoHakkousha);
        		super.appendIfNotEmpty(sqlBuf, " AND (ebunsho_hakkousha IS NULL OR ebunsho_hakkousha = '') ", null, queryParameter.ebunshoHakkoushaMinyuuryokuFlg); // 文字列の場合は空のケースも考慮が必要
        		sqlBuf.append(")");
        	}
        }
        
        //仕訳抽出状況
        if(!super.isEmpty(queryParameter.shiwakeStatus)){
            // 仕訳のバージョン判定
            sqlBuf.append("  AND di.denpyou_kbn NOT LIKE 'B%'");
            switch(queryParameter.shiwakeStatus) {
            case "N":
                //未抽出(N)
                sqlBuf.append("  AND di.shiwake_status = '' ");
                break;
            case "0":
            case "1":
                //抽出済(0)とOPEN21転記済(1)
                sqlBuf.append("  AND di.shiwake_status LIKE '%"+ queryParameter.shiwakeStatus +"%' ");
                break;
            }
        }

        //マル秘文書フラグ
        if( !super.isEmpty(queryParameter.maruhi) ){
            sqlBuf.append(" AND di.maruhi_flg = '1'");
        }

        //以下、伝票個別用
        boolean kariBumonCdFromInputted = (!super.isEmpty(queryParameter.karikataBumonCdFrom));
        boolean kariBumonCdToInputted = (!super.isEmpty(queryParameter.karikataBumonCdTo));
        boolean kashiBumonCdFromInputted = (!super.isEmpty(queryParameter.kashikataBumonCdFrom));
        boolean kashiBumonCdToInputted = (!super.isEmpty(queryParameter.kashikataBumonCdTo));
        boolean miseisanKaribaraiInputted = (!super.isEmpty(queryParameter.miseisanKaribarai));
        boolean miseisanUkagaiInputted = (!super.isEmpty(queryParameter.miseisanUkagai));
        boolean fbStatusInputted = (!super.isEmpty(queryParameter.fbStatus));
        boolean kanitodokeKenmeiInputted = (!super.isEmpty(queryParameter.kanitodokeKenmei));
        boolean kanitodokeNaiyouInputted = (!super.isEmpty(queryParameter.kanitodokeNaiyou));

        // 摘要
        super.appendIfNotEmptyForLikeQuery(sqlBuf, "di.tekiyou", params, queryParameter.tekiyou);
        // 領収書
        super.appendIfNotEmpty(sqlBuf, " AND ? = ANY(string_to_array(di.ryoushuusho_exist,'╂'))", params, queryParameter.ryoushuushoSeikyuushoTou);

        // 借方
        // 集計部門の場合
        if ("1".equals(queryParameter.bumonSentakuFlag)) {
            if(kariBumonCdFromInputted || kariBumonCdToInputted) {
                List<String> futanBumonList = loadFutanBumonCd(queryParameter.karikataBumonCdFrom, queryParameter.karikataBumonCdTo, this.connection);
                if(futanBumonList.isEmpty()) {
                    sqlBuf.append(" AND 1=0 ");
                }else {
                    sqlBuf.append(" AND EXISTS(SELECT * FROM (SELECT UNNEST(kari_futan_bumon_cd) a FROM denpyou_ichiran hoge WHERE hoge.denpyou_id=di.denpyou_id) hoge WHERE hoge.a IN (" + EteamCommon.makeIn(futanBumonList) + ")) ");
                }
            }
        // 部門の場合
        } else {
            this.appendWhereUnnestedRange(queryParameter.karikataBumonCdFrom, queryParameter.karikataBumonCdTo, sqlBuf, "kari_futan_bumon_cd", params);
        }
        
        // 科目コード
        this.appendWhereUnnestedRange(queryParameter.karikataKamokuCdFrom, queryParameter.karikataKamokuCdTo, sqlBuf, "kari_kamoku_cd", params);
        // 科目枝番
        this.appendWhereUnnestedRange(queryParameter.karikataKamokuEdanoCdFrom, queryParameter.karikataKamokuEdanoCdTo, sqlBuf, "kari_kamoku_edaban_cd", params);
        // 取引先
        this.appendWhereUnnestedRange(queryParameter.karikataTorihikisakiCdFrom, queryParameter.karikataTorihikisakiCdTo, sqlBuf, "kari_torihikisaki_cd", params);

        // 貸方
        // 集計部門の場合
        if ("1".equals(queryParameter.bumonSentakuFlag)) {
            if(kashiBumonCdFromInputted || kashiBumonCdToInputted) {
                List<String> futanBumonList = loadFutanBumonCd(queryParameter.kashikataBumonCdFrom, queryParameter.kashikataBumonCdTo, this.connection);
                if(futanBumonList.isEmpty()) {
                    sqlBuf.append(" AND 1=0 ");
                }else {
                    sqlBuf.append(" AND EXISTS(SELECT * FROM (SELECT UNNEST(kashi_futan_bumon_cd) a FROM denpyou_ichiran hoge WHERE hoge.denpyou_id=di.denpyou_id) hoge WHERE hoge.a IN (" + EteamCommon.makeIn(futanBumonList) + ")) ");
                }
            }
        // 部門の場合
        } else {
            this.appendWhereUnnestedRange(queryParameter.kashikataBumonCdFrom, queryParameter.kashikataBumonCdTo, sqlBuf, "kashi_futan_bumon_cd", params);
        }
        
        // 科目コード
        this.appendWhereUnnestedRange(queryParameter.kashikataKamokuCdFrom, queryParameter.kashikataKamokuCdTo, sqlBuf, "kashi_kamoku_cd", params);
        // 科目枝番
        this.appendWhereUnnestedRange(queryParameter.kashikataKamokuEdanoCdFrom, queryParameter.kashikataKamokuEdanoCdTo, sqlBuf, "kashi_kamoku_edaban_cd", params);
        // 取引先
        this.appendWhereUnnestedRange(queryParameter.kashikataTorihikisakiCdFrom, queryParameter.kashikataTorihikisakiCdTo, sqlBuf, "kashi_torihikisaki_cd", params);

        //法人カード
        this.appendIfNotEmpty(sqlBuf, " AND di.houjin_card_use = '1'", null, queryParameter.houjinCardRiyou);
        // 会社手配
        this.appendIfNotEmpty(sqlBuf, " AND di.kaisha_tehai_use = '1'", null, queryParameter.kaishaTehai);
        
        // 未精算仮払・伺い
        this.appendIf(miseisanKaribaraiInputted && miseisanUkagaiInputted, sqlBuf, " AND (di.miseisan_karibarai_exist = '1' OR di.miseisan_ukagai_exist = '1')", null);
        this.appendIfNotEmpty(sqlBuf, " AND di.miseisan_karibarai_exist = '1'", null, queryParameter.miseisanKaribarai);
        this.appendIfNotEmpty(sqlBuf, " AND di.miseisan_ukagai_exist = '1'", null, queryParameter.miseisanUkagai);
        
        if(fbStatusInputted){
            switch(queryParameter.fbStatus) {
            case "N":
                //未作成(N)
                sqlBuf.append("  AND di.fb_status = '0' ");
                break;
            case "1":
                //作成済(1)
                sqlBuf.append("  AND di.fb_status = '1' ");
                break;
            }
        }

        // 負担部門
        this.appendWhereUnnestedRange(queryParameter.bumonCdFrom, queryParameter.bumonCdTo, sqlBuf, "futan_bumon_cd", params);

        // 簡易届件名・内容
        super.appendIfNotEmptyForLikeQuery(sqlBuf, "di.kenmei", params, queryParameter.kanitodokeKenmei);
        super.appendIfNotEmptyForLikeQuery(sqlBuf, "di.naiyou", params, queryParameter.kanitodokeNaiyou);

        //どちらかが検索項目になってるとき（ログインユーザーのマル秘参照権限は無視）
        if(setting.maruhiHyoujiSeigyoFlg().equals("0") && (kanitodokeKenmeiInputted || kanitodokeNaiyouInputted)) {sqlBuf.append(" AND di.maruhi_flg != '1'");}

        // 取下済・否認済の除外
        super.appendIf(queryParameter.torisageHininHyoujiFlg.equals("0"), sqlBuf, " AND di.name NOT IN ('取下済', '否認済') ", null);
        
        //事業者区分
        this.appendWhereUnnestedStringArray(sqlBuf, "di.jigyousha_kbn", params, queryParameter.jigyoushaKbn);
        
        //支払先名
        this.appendWhereUnnestedStringArray(sqlBuf, "shiharai_name", params, queryParameter.shiharaiName);
        
        //税抜明細金額
        this.appendWhereUnnestedRange(queryParameter.zeinukiMeisaiKingakuFrom, queryParameter.zeinukiMeisaiKingakuTo, sqlBuf, "zeinuki_meisai_kingaku", params);
        
        //税抜金額
        super.appendIfNotEmpty(sqlBuf, " AND di.zeinuki_kingaku >= ? ", params, queryParameter.zeinukiKingakuFrom);
        super.appendIfNotEmpty(sqlBuf, " AND di.zeinuki_kingaku <= ? ", params, queryParameter.zeinukiKingakuTo);
        
        //事業者区分、明細税抜金額、明細税抜金額合計に検索の値が指定されている場合、インボイス対応前伝票は検索結果から除外する
		if(queryParameter.jigyoushaKbn != null) {
			if(!isNull(queryParameter.zeinukiMeisaiKingakuFrom, queryParameter.zeinukiMeisaiKingakuTo,
					queryParameter.zeinukiKingakuFrom,queryParameter.zeinukiKingakuTo) || queryParameter.jigyoushaKbn.length() == 1) {
				sqlBuf.append("  AND uni.invoice_denpyou = '0' ");
			}
		}
        return sql + " " + sqlBuf.toString();
    }
    
    /**
     * UNNESTなんとか/ANYなんとかによる範囲指定SQL文を生成。
     * @param start 開始
     * @param end 終了
     * @param buffer バッファー
     * @param columnName DBのカラム名（「di.」は不要）
     * @param params SQL分のパラメーターリスト
     */
    protected void appendWhereUnnestedRange(Object start, Object end, StringBuffer buffer, String columnName, List<Object> params)
    {
    	// 両方とも空ではない場合
    	if(!super.isEmpty(start) && !super.isEmpty(end))
    	{
	    	super.appendIf(true, buffer, 
				" AND EXISTS(SELECT * FROM (SELECT UNNEST("+ columnName + ") a FROM denpyou_ichiran hoge WHERE hoge.denpyou_id=di.denpyou_id) hoge WHERE hoge.a>=? AND hoge.a<=?) ",
				params, start, end);
	    	return;
    	}
    	
    	// それ以外
    	super.appendIfNotEmpty(buffer, " AND ? <= ANY(di." + columnName + ")", params, start);
    	super.appendIfNotEmpty(buffer, " AND ? >= ANY(di." + columnName + ")", params, end);
    }
    /**
     * 配列内文字列あいまい検索SQL文を生成。
     * @param buffer バッファー
     * @param columnName DBのカラム名（「di.」は不要）
     * @param params SQL分のパラメーターリスト
     * @param additionalParam 追加パラメーター
     */
    protected void appendWhereUnnestedStringArray(StringBuffer buffer, String columnName, List<Object> params, Object additionalParam)
    {
    	// 空でない場合
    	if(!super.isEmpty(additionalParam))
    	{
    		//TODO ひらがなカタカナの半角/全角・アルファベットの大文字/小文字のあいまい検索に対応していない
    		//　配列用のunify_kana_width()が必要になる　難しいため後回しorお願いする
	    	super.appendIf(true, buffer, 
	    			" AND EXISTS (SELECT * FROM (SELECT UNNEST("+ columnName +") a FROM denpyou_ichiran hoge WHERE hoge.denpyou_id=di.denpyou_id) hoge WHERE a LIKE ?) ",
				params, "%"+additionalParam+"%");
	    	return;
    	}
    }
    
    /**
     * 起案番号系の基本的なWhere文を生成
     * @param target ターゲットパラメーター
     * @param buffer SQL用バッファー
     * @param params パラメーター
     * @param prefix LIKE接頭辞
     * @param suffix LIKE接尾辞
     */
    protected void appendWhereKianBangou(Object target, StringBuffer buffer, List<Object> params, String prefix, String suffix)
    {
    	var formattedTarget = prefix + target + suffix;
    	super.appendIf(!super.isEmpty(target), buffer, "  AND (di.jisshi_kian_bangou LIKE ? OR di.shishutsu_kian_bangou LIKE ?)", params, formattedTarget, formattedTarget);
    }
    
    /**
     * 伝票番号検索条件作成
     * @param serialNo 伝票番号
     * @return 伝票番号検索条件
     */
    protected String createSerialNoCondition(Long serialNo) {

        final String CONST_SERIAL_NO = "<SERIALNO>";

        var sqlBuf = new StringBuffer();
        sqlBuf.append(" AND (");
        // 伝票番号で仕訳後のテーブルを検索。支払仕訳も対象になる。
        if (Open21Env.getVersion() == Version.DE3 ) {
            sqlBuf.append("  (di.denpyou_id in (select denpyou_id from shiwake_de3 where CAST(dcno AS Integer) ='<SERIALNO>'))");
        } else {
            sqlBuf.append("  (di.denpyou_id in (select denpyou_id from shiwake_sias where CAST(dcno AS Integer) ='<SERIALNO>'))");
        }

        // 作成単位が伝票の種別はdenpyou_ichiranも検索し、仕訳抽出前でも検索可
        if (setting.denpyouSakuseiTaniA001().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A001' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA002().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A002' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA003().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A003' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA004().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A004' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA005().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A005' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA006().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A006' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA007().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A007' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA008().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A008' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA009().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A009' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA010().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A010' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA011().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A011' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA012().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A012' AND di.serial_no = '<SERIALNO>')");
        }
        if (setting.denpyouSakuseiTaniA013().equals(EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID)) {
            sqlBuf.append(" OR (di.denpyou_kbn = 'A013' AND di.serial_no = '<SERIALNO>')");
        }
        sqlBuf.append(" OR (di.denpyou_kbn LIKE 'B%' AND di.serial_no = '<SERIALNO>')");
        sqlBuf.append(")");
        String ret = sqlBuf.toString().replace(CONST_SERIAL_NO, serialNo.toString());
        return ret;
    }

    /**
     * 集計部門配下で最小の負担部門コードを取得する
     * @param bumonCdFrom 集計部門コードFrom
     * @param bumonCdTo 集計部門コードTo
     * @param c コネクション
     * @return 最小or最大の負担部門コード
     */
    public List<String> loadFutanBumonCd(String bumonCdFrom, String bumonCdTo, EteamConnection c) {
        MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, c);
        //決算期番号リスト
        List<Integer> kbList = new ArrayList<Integer>();
        kbList.add(0);
        kbList.add(1);
        List<GMap> bumonList = masterLogic.loadFutanBumonFromSptn(new ArrayList<Integer>(), kbList, bumonCdFrom, bumonCdTo);
        if (bumonList.isEmpty()) return new ArrayList<>();

        List<String> ret = new ArrayList<>();
        for (GMap map : bumonList) {
            if(! ret.contains(map.getString("futan_bumon_cd"))) ret.add(map.getString("futan_bumon_cd"));
        }
        return ret;
    }

    /**
     * ORDER 句を作る
     * @param sort 画面から渡されたソート値
     * @return 「ORDER BY ○○」の○○部分
     */
    protected String makeOrder(String sort) {
        String order;
        switch(sort) {
            case "1"	: order = "denpyou_joutai, zan_cnt desc, all_cnt"; break;
            case "2"	: order = "denpyou_joutai DESC, zan_cnt, all_cnt DESC"; break;
            case "3"	: order = "denpyou_id"; break;
            case "4"	: order = "denpyou_id DESC"; break;
            case "5"	: order = "denpyou_shubetsu"; break;
            case "6"	: order = "denpyou_shubetsu DESC "; break;
            case "7"	: order = "(kingaku IS null), kingaku"; break;
            case "8"	: order = "(kingaku IS null), kingaku DESC"; break;
            case "9"	: order = "(torihikisaki1  IS NULL or torihikisaki1 = ''), torihikisaki1"; break;
            case "10"	: order = "(torihikisaki1  IS NULL or torihikisaki1 = ''), torihikisaki1 DESC"; break;
            case "11"	: order = "touroku_time"; break;
            case "12"	: order = "touroku_time DESC"; break;
            case "13"	: order = "bumon_full_name"; break;
            case "14"	: order = "bumon_full_name DESC"; break;
            case "15"	: order = "(user_full_name IS NULL OR user_full_name = ''), user_full_name"; break;
            case "16"	: order = "(user_full_name IS NULL OR user_full_name = ''), user_full_name DESC"; break;
            case "17"	: order = "(shiharaikiboubi IS NULL), shiharaikiboubi"; break;
            case "18"	: order = "(shiharaikiboubi IS NULL), shiharaikiboubi DESC"; break;
            case "19"	: order = "(shiharaibi IS NULL), shiharaibi"; break;
            case "20"	: order = "(shiharaibi IS NULL), shiharaibi DESC"; break;
            case "21"	: order = "(gen_bumon_full_name IS NULL OR gen_bumon_full_name = ''), gen_bumon_full_name"; break;
            case "22"	: order = "(gen_bumon_full_name IS NULL OR gen_bumon_full_name = ''), gen_bumon_full_name DESC"; break;
            case "23"	: order = "(gen_user_full_name IS NULL OR gen_user_full_name = ''), gen_user_full_name"; break;
            case "24"	: order = "(gen_user_full_name IS NULL OR gen_user_full_name = ''), gen_user_full_name DESC"; break;
            case "25"	: order = "(gen_gyoumu_role_name IS NULL OR gen_gyoumu_role_name = ''), gen_gyoumu_role_name"; break;
            case "26"	: order = "(gen_gyoumu_role_name IS NULL OR gen_gyoumu_role_name = ''), gen_gyoumu_role_name DESC"; break;
            case "27"	: order = "(gen_name IS NULL OR gen_name = ''), gen_name"; break;
            case "28"	: order = "(gen_name IS NULL OR gen_name = ''), gen_name DESC"; break;
            case "29"	: order = "(shiharaihouhou IS NULL OR shiharaihouhou = ''), shiharaihouhou"; break;
            case "30"	: order = "(shiharaihouhou IS NULL OR shiharaihouhou = ''), shiharaihouhou DESC"; break;
            case "31"	: order = "(sashihiki_shikyuu_kingaku IS NULL), sashihiki_shikyuu_kingaku"; break;
            case "32"	: order = "(sashihiki_shikyuu_kingaku IS NULL), sashihiki_shikyuu_kingaku DESC"; break;
            case "33"	: order = "(keijoubi IS NULL), keijoubi"; break;
            case "34"	: order = "(keijoubi IS NULL), keijoubi DESC"; break;
            case "35"	: order = "(seisan_yoteibi IS NULL), seisan_yoteibi"; break;
            case "36"	: order = "(seisan_yoteibi IS NULL), seisan_yoteibi DESC"; break;
            case "37"	: order = "(karibarai_denpyou_id IS NULL OR karibarai_denpyou_id = ''), karibarai_denpyou_id"; break;
            case "38"	: order = "(karibarai_denpyou_id IS NULL OR karibarai_denpyou_id = ''), karibarai_denpyou_id DESC"; break;
            case "39"	: order = "(shiwakeKeijoubi IS NULL), shiwakeKeijoubi"; break;
            case "40"	: order = "(shiwakeKeijoubi IS NULL), shiwakeKeijoubi DESC"; break;
            case "41"	: order = "(shouninbi IS NULL), shouninbi"; break;
            case "42"	: order = "(shouninbi IS NULL), shouninbi DESC"; break;
            case "43"	: order = "serial_no"; break;
            case "44"	: order = "serial_no DESC"; break;
            case "45"	: order = "(houmonsaki IS NULL OR houmonsaki = ''), houmonsaki"; break;
            case "46"	: order = "(houmonsaki IS NULL OR houmonsaki = ''), houmonsaki DESC"; break;
            case "47"	: order = "(mokuteki IS NULL OR mokuteki = ''), mokuteki"; break;
            case "48"	: order = "(mokuteki IS NULL OR mokuteki = ''), mokuteki DESC"; break;
            case "49"	: order = "(houjin_kingaku IS NULL), houjin_kingaku"; break;
            case "50"	: order = "(houjin_kingaku IS NULL), houjin_kingaku DESC"; break;
            case "51"	: order = "(jisshi_kian_bangou IS NULL OR jisshi_kian_bangou = ''), jisshi_kian_bangou"; break;
            case "52"	: order = "(jisshi_kian_bangou IS NULL OR jisshi_kian_bangou = ''), jisshi_kian_bangou DESC"; break;
            case "53"	: order = "(shishutsu_kian_bangou IS NULL OR shishutsu_kian_bangou = ''), shishutsu_kian_bangou"; break;
            case "54"	: order = "(shishutsu_kian_bangou IS NULL OR shishutsu_kian_bangou = ''), shishutsu_kian_bangou DESC"; break;
            case "55"	: order = "(yosan_shikkou_taishou IS NULL), yosan_shikkou_taishou"; break;
            case "56"	: order = "(yosan_shikkou_taishou IS NULL), yosan_shikkou_taishou DESC"; break;
            case "57"	: order = "(kenmei IS NULL OR kenmei = ''), kenmei"; break;
            case "58"	: order = "(kenmei IS NULL OR kenmei = ''), kenmei DESC"; break;
            case "59"	: order = "(naiyou IS NULL OR naiyou = ''), naiyou"; break;
            case "60"	: order = "(naiyou IS NULL OR naiyou = ''), naiyou DESC"; break;
            case "61"	: order = "(user_sei IS NULL OR user_sei = ''), user_sei, (user_mei IS NULL OR user_mei = ''), user_mei"; break;
            case "62"	: order = "(user_sei IS NULL OR user_sei = ''), user_sei DESC, (user_mei IS NULL OR user_mei = ''), user_mei DESC"; break;
            case "63"	: order = "(seisankikan_from IS NULL), seisankikan_from, (seisankikan_to IS NULL), seisankikan_to"; break;
            case "64"	: order = "(seisankikan_from IS NULL), seisankikan_from DESC, (seisankikan_to IS NULL), seisankikan_to DESC"; break;
            case "65"	: order = "(yosan_check_nengetsu IS NULL OR yosan_check_nengetsu = ''), yosan_check_nengetsu"; break;
            case "66"	: order = "(yosan_check_nengetsu IS NULL OR yosan_check_nengetsu = ''), yosan_check_nengetsu DESC"; break;
            case "67"	: order = "uiShainNo"; break;
            case "68"	: order = "uiShainNo DESC"; break;
            default : order = "touroku_time DESC"; break;
        }
        return order;
    }

    /**
     * 簡易届の条件
     */
    @Getter
    public class KaniJouken{
        /** カラム名リスト　*/ public List<String> columnList;
        /** typeリスト　*/ public List<String> typeList;
        /** formatリスト　*/ public List<String> formatList;
        /** 本体の01 */ public String[] shinsei01;
        /** 本体の02 */ public String[] shinsei02;
        /** 本体の03 */ public String[] shinsei03;
        /** 本体の04 */ public String[] shinsei04;
        /** 本体の05 */ public String[] shinsei05;
        /** 本体の06 */ public String[] shinsei06;
        /** 本体の07 */ public String[] shinsei07;
        /** 本体の08 */ public String[] shinsei08;
        /** 本体の09 */ public String[] shinsei09;
        /** 本体の10 */ public String[] shinsei10;
        /** 本体の11 */ public String[] shinsei11;
        /** 本体の12 */ public String[] shinsei12;
        /** 本体の13 */ public String[] shinsei13;
        /** 本体の14 */ public String[] shinsei14;
        /** 本体の15 */ public String[] shinsei15;
        /** 本体の16 */ public String[] shinsei16;
        /** 本体の17 */ public String[] shinsei17;
        /** 本体の18 */ public String[] shinsei18;
        /** 本体の19 */ public String[] shinsei19;
        /** 本体の20 */ public String[] shinsei20;
        /** 本体の21 */ public String[] shinsei21;
        /** 本体の22 */ public String[] shinsei22;
        /** 本体の23 */ public String[] shinsei23;
        /** 本体の24 */ public String[] shinsei24;
        /** 本体の25 */ public String[] shinsei25;
        /** 本体の26 */ public String[] shinsei26;
        /** 本体の27 */ public String[] shinsei27;
        /** 本体の28 */ public String[] shinsei28;
        /** 本体の29 */ public String[] shinsei29;
        /** 本体の30 */ public String[] shinsei30;
        /** 明細の01 */ public String[] meisai01;
        /** 明細の02 */ public String[] meisai02;
        /** 明細の03 */ public String[] meisai03;
        /** 明細の04 */ public String[] meisai04;
        /** 明細の05 */ public String[] meisai05;
        /** 明細の06 */ public String[] meisai06;
        /** 明細の07 */ public String[] meisai07;
        /** 明細の08 */ public String[] meisai08;
        /** 明細の09 */ public String[] meisai09;
        /** 明細の10 */ public String[] meisai10;
        /** 明細の11 */ public String[] meisai11;
        /** 明細の12 */ public String[] meisai12;
        /** 明細の13 */ public String[] meisai13;
        /** 明細の14 */ public String[] meisai14;
        /** 明細の15 */ public String[] meisai15;
        /** 明細の16 */ public String[] meisai16;
        /** 明細の17 */ public String[] meisai17;
        /** 明細の18 */ public String[] meisai18;
        /** 明細の19 */ public String[] meisai19;
        /** 明細の20 */ public String[] meisai20;
        /** 明細の21 */ public String[] meisai21;
        /** 明細の22 */ public String[] meisai22;
        /** 明細の23 */ public String[] meisai23;
        /** 明細の24 */ public String[] meisai24;
        /** 明細の25 */ public String[] meisai25;
        /** 明細の26 */ public String[] meisai26;
        /** 明細の27 */ public String[] meisai27;
        /** 明細の28 */ public String[] meisai28;
        /** 明細の29 */ public String[] meisai29;
        /** 明細の30 */ public String[] meisai30;

        /** typeMap */ Map<String, String> typeMap;

        /** formatMap */ Map<String, String> formatMap;
        
        /** ルート判定金額 */ public String routeKingakuItem;
        /** 件名 */ public String kenmeiItem;
        /** 内容 */ public String naiyouShinseiItem;
        
        /**
         * 明細項目のFROMを返す
         * @param no shinsei##の##
         * @return FROM。該当項目なければnull。
         */
        public String getShinseiFrom(int no){
            try {
                Method method = this.getClass().getMethod("getShinsei" + String.format("%1$02d", no));
                String[] meisai = (String[])method.invoke(this);
                if(meisai != null && meisai.length >= 1){
                    return meisai[0];
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * 明細項目のTOを返す
         * @param no shinsei##の##
         * @return TO。該当項目なければnull。
         */
        public String getShinseiTo(int no){
            try {
                Method method = this.getClass().getMethod("getShinsei" + String.format("%1$02d", no));
                String[] meisai = (String[])method.invoke(this);
                if(meisai != null && meisai.length >= 2){
                    return meisai[1];
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * 明細項目のFROMを返す
         * @param no shinsei##の##
         * @return FROM。該当項目なければnull。
         */
        public String getMeisaiFrom(int no){
            try {
                Method method = this.getClass().getMethod("getMeisai" + String.format("%1$02d", no));
                String[] meisai = (String[])method.invoke(this);
                if(meisai != null && meisai.length >= 1){
                    return meisai[0];
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * 明細項目のTOを返す
         * @param no shinsei##の##
         * @return TO。該当項目なければnull。
         */
        public String getMeisaiTo(int no){
            try {
                Method method = this.getClass().getMethod("getMeisai" + String.format("%1$02d", no));
                String[] meisai = (String[])method.invoke(this);
                if(meisai != null && meisai.length >= 2){
                    return meisai[1];
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * オブジェクト初期化
         */
        protected void init(){
            if(formatMap == null){
                typeMap = new HashMap<>();
                for(int i = 0; i < columnList.size(); i++){
                    typeMap.put(columnList.get(i), typeList.get(i));
                }
                formatMap = new HashMap<>();
                for(int i = 0; i < columnList.size(); i++){
                    formatMap.put(columnList.get(i), formatList.get(i));
                }
            }
        }
        /**
         * 本体のTYPEを返す
         * @param no shinsei##の##
         * @return TYPE
         */
        public String getShinseiType(int no){
            init();
            return typeMap.get("shinsei" + String.format("%1$02d", no));
        }
        /**
         * 明細のTYPEを返す
         * @param no shinsei##の##
         * @return TYPE
         */
        public String getMeisaiType(int no){
            init();
            return typeMap.get("meisai" + String.format("%1$02d", no));
        }
        /**
         * 本体のフォーマットを返す
         * @param no shinsei##の##
         * @return フォーマット
         */
        public String getShinseiFormat(int no){
            init();
            return formatMap.get("shinsei" + String.format("%1$02d", no));
        }
        /**
         * 明細のフォーマットを返す
         * @param no shinsei##の##
         * @return フォーマット
         */
        public String getMeisaiFormat(int no){
            init();
            return formatMap.get("meisai" + String.format("%1$02d", no));
        }
    }
    /**
     * 伝票一覧の件数を取得する。（簡易届専用）
     * @param queryParameter クエリパラメーター
     * @param kaniJ 簡易届専用条件
     * @return 検索結果件数
     */
    public long findDenpyouIchiranKensakuCountKani(DenpyouKensakuQueryParameter queryParameter, KaniJouken kaniJ){
        List<Object> params = new ArrayList<Object>();

        //WHERE区ないSELECT
        String sqlMain = this.getDenpyouIchiranTableSqlKani(false, params, queryParameter.user);
        //WHERE区つける
        sqlMain = addWhereSqlDenpyouIchiranTableKani(
                sqlMain
                , params
                , queryParameter
                , kaniJ
                );

        GMap datacount = connection.find("SELECT COUNT(DISTINCT denpyou_id) AS datacount FROM (" + sqlMain + ") rec", params.toArray());
        long count = (long)datacount.get("datacount");
        return count;
    }


    /**
     * 伝票一覧を取得する。（検索用テーブルから取得）（簡易届専用）
     * @param csv CSV出力ならtrue
     * @param queryParameter クエリパラメーター
     * @param kaniJ 簡易届専用条件
     * @param pageNo  ページ番号 ※0の場合、全件検索する。
     * @param pageMax 1ページ最大表示件数
     * @param sort ソート条件 ※ ""の場合標準ソート
     * @return 検索結果
     */
    public List<GMap> loadDenpyouIchiranKensakuKani(
              boolean csv
            , DenpyouKensakuQueryParameter queryParameter
            , KaniJouken kaniJ
            , int pageNo
            , int pageMax
            , String sort){
        KaniTodokeCategoryLogic kaniLg = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
        List<String> columnList = new ArrayList<>();
        List<String> wameiList = new ArrayList<>();
        List<String> formatList = new ArrayList<>();

        //---------------------------------
        //出力項目(簡易届可変)を確定させる
        //---------------------------------

        //該当伝票区分(簡易届)の和名と物理名を表示順に取る
        String version = Integer.toString(kaniLg.findMaxVersion(queryParameter.denpyouShubetsu));
        List<GMap> shinseiLayoutList = kaniLg.loadLayout(AREA_KBN.SHINSEI, queryParameter.denpyouShubetsu, version);
        List<GMap> meisaiLayoutList = kaniLg.loadLayout(AREA_KBN.MEISAI, queryParameter.denpyouShubetsu, version);

        for(GMap shinseiLayout : shinseiLayoutList){
            columnList.add((String)shinseiLayout.get("item_name"));
            wameiList.add((String)shinseiLayout.get("label_name"));
            formatList.add((String)shinseiLayout.get("buhin_format"));
        }
        for(GMap meisaiLayout : meisaiLayoutList){
            columnList.add((String)meisaiLayout.get("item_name"));
            wameiList.add((String)meisaiLayout.get("label_name"));
            formatList.add((String)meisaiLayout.get("buhin_format"));
        }

        //---------------------------------
        //検索する
        //---------------------------------
        List<Object> params = new ArrayList<Object>();

        //WHERE区ないSELECT
        String sqlMain = this.getDenpyouIchiranTableSqlKani(csv, params, queryParameter.user);
        //WHERE区つける
        sqlMain = addWhereSqlDenpyouIchiranTableKani(
              sqlMain
            , params
            , queryParameter
            , kaniJ
            );

        // ソート指定
        String order;
        if(sort.equals(SORT_KBN_ALL)){
            order = "kihyouBumonCd, shain_no"; //部門＋ユーザ集計
        }else if(sort.equals(SORT_KBN_BUMON)){
            order = "kihyouBumonCd"; //部門集計
        }else if(sort.equals(SORT_KBN_USER)){
            order = "shain_no"; //ユーザ集計
        }else if(sort.startsWith("shinsei")){
            order = sort; //簡易届の申請項目でソート(shinsei01 descみたいな形で画面から送られてくる)
        }else if(sort.startsWith("meisai")){
            order = sort; //簡易届の明細項目でソート(meisai01 descみたいな形で画面から送られてくる)
        }else{
            order = makeOrder(sort); //その他は通常検索と同じ
        }

// StringBuffer sql = new StringBuffer("SELECT * FROM (" + sqlMain + ") rec").append(" ORDER BY ").append(order).append(",denpyou_id DESC,denpyou_edano");
        StringBuffer sql = null;
        if(csv){
            sql = new StringBuffer("SELECT * FROM (" + sqlMain + ") rec ORDER BY " + order + ",denpyou_id DESC,denpyou_edano");
        }else{
            sql = new StringBuffer("SELECT * FROM (" + NOWHERE_SQL_KANI + "WHERE di.denpyou_id IN(SELECT denpyou_id FROM(" + sqlMain + ") rec) AND (kani_m.denpyou_edano IS NULL OR kani_m.denpyou_edano=1)) rec2 ORDER BY " + order + ",denpyou_id DESC");
        }

        // ページ番号（取得件数）
        if(pageNo != 0){
            sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));
        }

        return connection.load(sql.toString(),params.toArray());
    }

    /** 簡易届一覧用のSQL・・・WHERE句なしで必要なカラム全部取ってくるやつ */
    static final String NOWHERE_SQL_KANI =
              " SELECT "
            + "    di.*,ds.denpyou_shubetsu,ds.gyoumu_shubetsu,sh.bumon_cd as kihyouBumonCd,ui.shain_no "
            + "   ,kani_s.shinsei01,kani_s.shinsei02,kani_s.shinsei03,kani_s.shinsei04,kani_s.shinsei05,kani_s.shinsei06,kani_s.shinsei07,kani_s.shinsei08,kani_s.shinsei09,kani_s.shinsei10,kani_s.shinsei11,kani_s.shinsei12,kani_s.shinsei13,kani_s.shinsei14,kani_s.shinsei15,kani_s.shinsei16,kani_s.shinsei17,kani_s.shinsei18,kani_s.shinsei19,kani_s.shinsei20,kani_s.shinsei21,kani_s.shinsei22,kani_s.shinsei23,kani_s.shinsei24,kani_s.shinsei25,kani_s.shinsei26,kani_s.shinsei27,kani_s.shinsei28,kani_s.shinsei29,kani_s.shinsei30 "
            + "   ,kani_m.denpyou_edano,kani_m.meisai01,kani_m.meisai02,kani_m.meisai03,kani_m.meisai04,kani_m.meisai05,kani_m.meisai06,kani_m.meisai07,kani_m.meisai08,kani_m.meisai09,kani_m.meisai10,kani_m.meisai11,kani_m.meisai12,kani_m.meisai13,kani_m.meisai14,kani_m.meisai15,kani_m.meisai16,kani_m.meisai17,kani_m.meisai18,kani_m.meisai19,kani_m.meisai20,kani_m.meisai21,kani_m.meisai22,kani_m.meisai23,kani_m.meisai24,kani_m.meisai25,kani_m.meisai26,kani_m.meisai27,kani_m.meisai28,kani_m.meisai29,kani_m.meisai30 "
            + " FROM denpyou_ichiran di "
            + " INNER JOIN shounin_route sh ON "
            + "       di.denpyou_id = sh.denpyou_id "
            + "   AND sh.edano = 1 "
            + " LEFT OUTER JOIN user_info ui ON "
            + "       di.user_id = ui.user_id "
            + " INNER JOIN denpyou_shubetsu_ichiran ds ON "
            + "       di.denpyou_kbn = ds.denpyou_kbn "
            + " LEFT OUTER JOIN kani_todoke_ichiran kani_s ON "	//↓簡易届専用
            + "       di.denpyou_id = kani_s.denpyou_id "
            + " LEFT OUTER JOIN kani_todoke_meisai_ichiran kani_m ON "
            + "       di.denpyou_id = kani_m.denpyou_id ";
    /**
     * 伝票一覧情報保持テーブルからのデータ取得用SQLを返却
     * @param csv CSVである
     * @param params パラメータ
     * @param user ログインユーザ
     * @return SQL
     */
    protected String getDenpyouIchiranTableSqlKani(boolean csv, List<Object> params, User user){
        //ユーザー参照可能伝票IDテーブルで絞込み
        String sql = NOWHERE_SQL_KANI + " WHERE 1 = 1 ";

        StringBuffer sqlBuf = new StringBuffer("");
        sqlBuf.append(makeAccessWhere(params, user, "di", "sh", true));

        return sql + sqlBuf.toString();
    }


    /** 検索条件SQLを付加する
     * @param sql メインSQL
     * @param params パラメータ
     * @param queryParameter クエリパラメーター
     * @param kaniJ 簡易届専用条件
     * @return メインSQL(+伝票共通SQL)
     */
    protected String addWhereSqlDenpyouIchiranTableKani(
              String sql
            , List<Object> params
            , DenpyouKensakuQueryParameter queryParameter
            , KaniJouken kaniJ
    ){
        //共通部の条件
        sql = addWhereSqlDenpyouIchiranTable(
              sql
            , params
            , queryParameter
            );
        StringBuffer sqlBuf = new StringBuffer(sql);

        //簡易届の条件
        boolean maruhiHideFlg = false;
        for(int kbn = 1; kbn <= 2; kbn++){
            for(int i = 1; i <= 30; i++){
                String column = kbn == 1 ? "shinsei" + String.format("%1$02d", i) : "meisai" + String.format("%1$02d", i);
                String from = kbn == 1 ? kaniJ.getShinseiFrom(i) : kaniJ.getMeisaiFrom(i);
                String to = kbn == 1 ? kaniJ.getShinseiTo(i) : kaniJ.getMeisaiTo(i);
                String type = kbn == 1 ? kaniJ.getShinseiType(i) : kaniJ.getMeisaiType(i);
                String format = kbn == 1 ? kaniJ.getShinseiFormat(i) : kaniJ.getMeisaiFormat(i);
                if(type == null) continue;
                
                //FROMとTOが条件で入力された内容
                //ルート判定金額・件名・内容に一致しないカラムでの検索がある場合はマル秘を除く
                if(!isEmpty(from)||!isEmpty(to)) {
                    if(!column.equals(kaniJ.routeKingakuItem)) {
                    	if(setting.maruhiHyoujiSeigyoFlg().equals("0") || (!column.equals(kaniJ.kenmeiItem) && !column.equals(kaniJ.naiyouShinseiItem))) {
                    		maruhiHideFlg = true;
                    	}
                    }
                }

                switch(type){
                    case buhinType.TEXT:
                        switch(format){
                            case BUHIN_FORMAT.NUMBER:
                            case BUHIN_FORMAT.MONEY:
                                if(!isEmpty(from)){
                                    sqlBuf.append("AND CAST(REPLACE(CASE WHEN " + column + "='' THEN '-999999999999999' ELSE " + column + " END, ',', '') AS NUMERIC) >= CAST (REPLACE(?,',','') AS NUMERIC) ");
                                    params.add(from);
                                }
                                if(!isEmpty(to)){
                                    sqlBuf.append("AND CAST(REPLACE(CASE WHEN " + column + "='' THEN  '999999999999999' ELSE " + column + " END, ',', '') AS NUMERIC) <= CAST (REPLACE(?,',','') AS NUMERIC) ");
                                    params.add(to);
                                }
                                break;
                            case BUHIN_FORMAT.DATE:
                            case BUHIN_FORMAT.TIME:
                                if(!isEmpty(from)){
                                    sqlBuf.append("AND " + column + " >= ? ");
                                    params.add(from);
                                }
                                if(!isEmpty(to)){
                                    sqlBuf.append("AND " + column + " <= ? ");
                                    params.add(to);
                                }
                                break;
                            default:
                                if(!isEmpty(from)){
                                    sqlBuf.append("AND " + column + " LIKE ? ");
                                    params.add("%" + from + "%");
                                }
                                break;
                        }
                        break;
                    case buhinType.TEXTAREA:
                        if(!isEmpty(from)){
                            sqlBuf.append("AND " + column + " LIKE ? ");
                            params.add("%" + from + "%");
                        }
                        break;
                    case buhinType.RADIO:
                    case buhinType.PULLDOWN:
                        if(!isEmpty(from)){
                            sqlBuf.append("AND " + column + " = ? ");
                            params.add(from);
                        }
                        break;
                    case buhinType.CHECKBOX:
                        if(!isEmpty(from)){
                            sqlBuf.append("AND " + column + " <> '' ");
                        }
                        break;
                    case buhinType.MASTER:
                        if(!isEmpty(to)){
                            sqlBuf.append("AND " + column + " = ? ");
                            params.add(to);
                        }else if(!isEmpty(from)){
                            sqlBuf.append("AND " + column + " = ? ");
                            params.add(from);
                        }
                        break;
                }
            }
        }
        if(maruhiHideFlg) {sqlBuf.append(" AND di.maruhi_flg != '1'");}
        return sqlBuf.toString();
    }

    /**
     * 指定した伝票IDの伝票レコードを取得する。
     * @param denpyouId 伝票IDリスト
     * @return 検索結果
     */
    @Deprecated
    public GMap findDenpyou(String denpyouId){
        String sql = "SELECT * FROM denpyou WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 経費精算(keihiseisan) */
    /**
     * (A001)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findKeihiSeisanKeiri(String denpyouId){
        String sql = "SELECT * FROM keihiseisan WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 仮払(karibarai) */
    /**
     * (A002)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findKaribaraKeiri(String denpyouId){
        String sql = "SELECT * FROM karibarai WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 請求書払い(seikyuushobarai) */
    /**
     * (A003)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findSeikyuushoBarai(String denpyouId){
        String sql = "SELECT * FROM seikyuushobarai WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 出張旅費精算(ryohiseisan) */
    /**
     * (A004)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findRyohiSeisanKeiri(String denpyouId){
        String sql = "SELECT * FROM ryohiseisan WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 出張旅費仮払(ryohi_karibarai) */
    /**
     * (A005)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findRyohiKaribaraiKeiri(String denpyouId){
        String sql = "SELECT * FROM ryohi_karibarai WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 海外出張旅費精算(kaigai_ryohiseisan) */
    /**
     * (A011)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findKaigaiRyohiSeisanKeiri(String denpyouId){
        String sql = "SELECT * FROM kaigai_ryohiseisan WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 海外出張旅費仮払(kaigai_ryohi_karibarai) */
    /**
     * (A012)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findKaigaiRyohiKaribaraiKeiri(String denpyouId){
        String sql = "SELECT * FROM kaigai_ryohi_karibarai WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 通勤定期(tsuukinteiki) */
    /**
     * (A005)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findTsuukinTeiki(String denpyouId){
        String sql = "SELECT * FROM tsuukinteiki WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 交通費精算(koutsuuhiseisan) */
    /**
     * (A010)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    public GMap findKoutsuuhiSeisanKeiri(String denpyouId){
        String sql = "SELECT * FROM koutsuuhiseisan WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

/* 自動引落(jidouhikiotoshi) */
    /**
     * (A009)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findJidouhikiotoshi(String denpyouId){
        String sql = "SELECT * FROM jidouhikiotoshi WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

    /* 支払依頼(shiharaiirai) */
    /**
     * (A013)指定した伝票IDのレコードを取得する。
     * @param denpyouId 伝票ID
     * @return 検索結果
     */
    @Deprecated
    public GMap findShiharaiirai(String denpyouId){
        String sql = "SELECT * FROM shiharai_irai WHERE denpyou_id=?";
        return connection.find(sql,denpyouId);
    }

    /**
     * 社員別支給金額一覧を取得する。
     * @param user ユーザ
     * @param kihyouBiFrom 起票日付From
     * @param kihyouBiTo 起票日付To
     * @param kensakuShainNo 起票者社員番号
     * @param kihyouShozokuBumonCd 起票者所属部門コード
     * @param kihyouShozokuBumonName 起票者所属部門名
     * @param kihyouShozokuUserName 起票者名
     * @param shiharaiBiFrom 支払日From
     * @param shiharaiBiTo 支払日To
     * @param shiyouBiFrom 使用日From
     * @param shiyouBiTo 使用日To
     * @param kihyouchuu 伝票状態：起票中
     * @param shinseichuu 伝票状態：申請中
     * @param syouninzumi 伝票状態：承認済
     * @param hininzumi   伝票状態：否認済
     * @param torisagezumi 伝票状態：取下済
     * @param sortColumn1 ソートカラム①
     * @param sort1       ソート順①
     * @param sortColumn2 ソートカラム②
     * @param sort2       ソート順②
     * @param sortColumn3 ソートカラム③
     * @param sort3       ソート順③
     * @param isShainBetsuSummary 社員別サマリ
     * @return 検索結果
     */
    public List<GMap> loadShikyuuKingakuIchiranKensaku(
              User user
            , Date kihyouBiFrom
            , Date kihyouBiTo
            , String kensakuShainNo
            , String kihyouShozokuBumonCd
            , String kihyouShozokuBumonName
            , String kihyouShozokuUserName
            , Date shiharaiBiFrom
            , Date shiharaiBiTo
            , Date shiyouBiFrom
            , Date shiyouBiTo
            , String kihyouchuu
            , String shinseichuu
            , String syouninzumi
            , String hininzumi
            , String torisagezumi
            , String sortColumn1
            , String sort1
            , String sortColumn2
            , String sort2
            , String sortColumn3
            , String sort3
            , Boolean isShainBetsuSummary){

        // MainSQL
        String sqlMain = this.getShikyuuKingakuMainSql();

        // パラメータ
        List<Object> params = new ArrayList<Object>();

        // WhereSQL追加(伝票共通)
        sqlMain = this.addWhereSqlShikyuuCommon(
                  sqlMain
                , params
                , user
                , kihyouBiFrom
                , kihyouBiTo
                , kihyouShozokuBumonCd
                , kihyouShozokuBumonName
                , kihyouchuu
                , shinseichuu
                , syouninzumi
                , hininzumi
                , torisagezumi);

        // WhereSQL追加(個別伝票テーブル)
        sqlMain = this.addWhereSqlShikyuuIndi(
                  sqlMain
                , params
                , user
                , kensakuShainNo
                , kihyouShozokuUserName
                , shiharaiBiFrom
                , shiharaiBiTo
                , shiyouBiFrom
                , shiyouBiTo);

        // ソート指定
        String column1  = SHIKYU_ICHIRAN_SORT_COLUMN.BUMON.equals(sortColumn1) ? " bumon_full_name " : " shain_no ";
        String order1  = SORT_KBN.KOU_JUN.equals(sort1) ? " DESC " : "";
        String column2  = SHIKYU_ICHIRAN_SORT_COLUMN.BUMON.equals(sortColumn2) ? " bumon_full_name " : " shain_no ";
        String order2  = SORT_KBN.KOU_JUN.equals(sort2) ? "DESC" : "";
        String column3Null = SHIKYU_ICHIRAN_SORT_COLUMN_DATE.SHIYOUBI.equals(sortColumn3) && !isShainBetsuSummary ? " (shiyoubi IS NULL), " : "";
        String column3  = SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(sortColumn3) ? " touroku_time " : " shiyoubi ";
        String order3  = SORT_KBN.KOU_JUN.equals(sort3) ? "DESC" : "";

        String order = column1 + order1 + "," + column2 + order2 + "," + column3Null + column3 + order3;

        StringBuffer sql = new StringBuffer();

        if (isShainBetsuSummary) {
            // 社員別サマリあり
            // Select句
            StringBuffer select = new StringBuffer();
            select.append("  shain_no");
            select.append(" ,user_id");
            select.append(" ,user_full_name");
            select.append(" ,string_agg(distinct bumon_full_name, ',') as bumon_full_name"); // 複数の部門名をカンマで区切って１行にする
            select.append(",coalesce(");
            select.append(" case when min(touroku_time) = max(touroku_time) then to_char(min(touroku_time), 'yyyy/mm/dd')");
            select.append("      else to_char(min(touroku_time), 'yyyy/mm/dd') || '～' || to_char(max(touroku_time), 'yyyy/mm/dd')");
            select.append(" end, '') as touroku_time");
            select.append(",coalesce(");
            select.append(" case when min(shiyoubi) = max(shiyoubi) then to_char(min(shiyoubi), 'yyyy/mm/dd')");
            select.append("      else to_char(min(shiyoubi), 'yyyy/mm/dd') || '～' || to_char(max(shiyoubi), 'yyyy/mm/dd')");
            select.append(" end, '') as shiyoubi");
            select.append(",coalesce(");
            select.append(" case when min(shiharaibi) = max(shiharaibi) then to_char(min(shiharaibi), 'yyyy/mm/dd')");
            select.append("      else to_char(min(shiharaibi), 'yyyy/mm/dd') || '～' || to_char(max(shiharaibi), 'yyyy/mm/dd')");
            select.append(" end, '') as shiharaibi");
            //shiharaihouhou:1(現金)、2(振込)
            select.append(" ,sum(CASE shiharaihouhou WHEN '1' THEN kingaku ELSE 0 END) as genkin_shiharai");
            select.append(" ,sum(CASE shiharaihouhou WHEN '2' THEN kingaku ELSE 0 END) as furikomi_shiharai");
            select.append(" ,sum(houjin_shiharai) as houjin_shiharai");
            select.append(" ,sum(tehai_shiharai) as tehai_shiharai");
            select.append(" ,sum(karibarai_kingaku) as karibarai_kingaku");
            select.append(" ,sum(CASE WHEN shiharaihouhou='1' OR  sashihiki_shikyuu_kingaku <  0 THEN sashihiki_shikyuu_kingaku ELSE 0 END) as genkin_sashihiki");
            select.append(" ,sum(CASE WHEN shiharaihouhou='2' AND sashihiki_shikyuu_kingaku >= 0 THEN sashihiki_shikyuu_kingaku ELSE 0 END) as furikomi_sashihiki");

            // Group句
            StringBuffer group = new StringBuffer();
            group.append("  shain_no");
            group.append(" ,user_id");
            group.append(" ,user_full_name");

            // SQL
            sql.append("SELECT ").append(select);
            sql.append(" FROM (" + sqlMain + ") rec");
            sql.append(" GROUP BY ").append(group);
            sql.append(" ORDER BY ").append(order);

        } else {
            // 社員別サマリなし
            sql = new StringBuffer();
            sql.append("SELECT * ");
            //shiharaihouhou:1(現金)、2(振込)
            sql.append(" ,(CASE shiharaihouhou WHEN '1' THEN kingaku ELSE 0 END) as genkin_shiharai");
            sql.append(" ,(CASE shiharaihouhou WHEN '2' THEN kingaku ELSE 0 END) as furikomi_shiharai");
            //houjin_shiharai
            //tehai_shiharai
            //karibarai_kingaku は*に入っているまんまでOK
            sql.append(" ,(CASE WHEN shiharaihouhou='1' OR  sashihiki_shikyuu_kingaku <  0 THEN sashihiki_shikyuu_kingaku ELSE 0 END) as genkin_sashihiki");
            sql.append(" ,(CASE WHEN shiharaihouhou='2' AND sashihiki_shikyuu_kingaku >= 0 THEN sashihiki_shikyuu_kingaku ELSE 0 END) as furikomi_sashihiki");
            sql.append(" FROM (" + sqlMain + ") rec");
            sql.append(" ORDER BY ").append(order).append(",denpyou_id DESC");
        }

        return connection.load(sql.toString(),params.toArray());
    }

    /** 社員別支給金額一覧メインSQLの取得
     * @return 一覧メインSQL
     */
    protected String getShikyuuKingakuMainSql(){

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT ");
        sql.append("  L.denpyou_id");
        sql.append("  ,L.denpyou_shubetsu");
        sql.append("  ,L.touroku_time ::date");
        sql.append("  ,L.bumon_full_name");
        sql.append("  ,COALESCE(R.user_id, L.user_id) as user_id");
        sql.append("  ,COALESCE(R.shain_no,L.shain_no) as shain_no");
        sql.append("  ,case ");
        sql.append("    when R.user_id is not null then ");
        sql.append("       R.user_full_name");
        sql.append("    when strpos(L.user_full_name,'(代行者:') > 0 then ");
        sql.append("       substr(L.user_full_name, 1, strpos(L.user_full_name, '(代行者:') - 1) ");
        sql.append("    else ");
        sql.append("       L.user_full_name");
        sql.append("  end as user_full_name");
        sql.append("  ,L.name");
        sql.append("  ,cnt.cnt                  as all_cnt ");
        sql.append("  ,case ");
        sql.append("    when shec.gen_cnt is null then ");
        sql.append("      0 ");
        sql.append("    else ");
        sql.append("      shec.gen_cnt ");
        sql.append("  end as cur_cnt ");
        sql.append("  ,R.shiharaibi ");
        sql.append("  ,R.shiyoubi ");
        sql.append("  ,R.shiharaihouhou ");
        sql.append("  ,R.kingaku ");
        sql.append("  ,R.houjin_shiharai ");
        sql.append("  ,R.tehai_shiharai ");
        sql.append("  ,R.sashihiki_shikyuu_kingaku");
        sql.append("  ,R.karibarai_kingaku ");

        //中間テーブル L（伝票共通部分）
        sql.append("FROM ( ");
        sql.append("  SELECT ");
        sql.append("    a.denpyou_id");
        sql.append("    ,b.denpyou_shubetsu");
        sql.append("    ,a.denpyou_joutai");
        sql.append("    ,c.touroku_time");
        sql.append("    ,c.bumon_full_name");
        sql.append("    ,c.user_id");
        sql.append("    ,e.shain_no");
        sql.append("    ,c.user_full_name");
        sql.append("    ,d.name");
        sql.append("  FROM denpyou a");
        sql.append("  INNER JOIN denpyou_shubetsu_ichiran b ON ");
        sql.append("     a.denpyou_kbn = b.denpyou_kbn ");
        sql.append("  INNER JOIN shounin_route c ON ");
        sql.append("     a.denpyou_id = c.denpyou_id ");
        sql.append("     AND c.edano = 1");
        sql.append("  INNER JOIN naibu_cd_setting d ON ");
        sql.append("     d.naibu_cd_name  = 'denpyou_jyoutai'");
        sql.append("     AND a.denpyou_joutai = d.naibu_cd");
        sql.append("  INNER JOIN user_info e ON "); //ユーザーが消えて見えなくなるのはポリシーに反するのだが
        sql.append("     c.user_id = e.user_id ");
        sql.append("  WHERE 1 = 1 " + WHERE_SHIKYUU_COMMON).append(" ");
        sql.append(") L ");

        // 進捗部分
        //該当伝票の承認対象ユーザー数を取得(合議内ユーザーもそれぞれ1人とカウント)
        sql.append("INNER JOIN ( ");
        sql.append("SELECT sh.denpyou_id, count(sh.denpyou_id) as cnt ");
        sql.append(" FROM shounin_route sh ");
        sql.append(" LEFT OUTER JOIN shounin_route_gougi_ko sk ");
        sql.append(" ON sh.denpyou_id = sk.denpyou_id AND sh.edano = sk.edano ");
        sql.append(" WHERE (sh.kihon_model_cd <> '2' AND sh.kihon_model_cd <> '3' AND sk.gougi_edano IS NULL) "); //基本モデルが「閲覧者」「後伺い」以外
        sql.append("    OR (sk.kihon_model_cd <> '2' AND sk.kihon_model_cd <> '3' AND sk.gougi_edano IS NOT NULL) ");
        sql.append(" GROUP BY sh.denpyou_id ");
        sql.append(") cnt ");
        sql.append("  ON L.denpyou_id = cnt.denpyou_id ");

        sql.append("LEFT OUTER JOIN shounin_route gen ");
        sql.append("  ON cnt.denpyou_id = gen.denpyou_id and gen.genzai_flg = '1' ");


        //該当伝票で承認処理完了したユーザー数を取得(合議内ユーザーもそれぞれ1人とカウント、承認人数比率によりスキップされたメンバーも含む)
        sql.append("LEFT OUTER JOIN ( ");
        sql.append(" SELECT sh.denpyou_id,count(sh.denpyou_id) as gen_cnt");
        sql.append(" FROM shounin_route sh ");
        sql.append(" LEFT OUTER JOIN shounin_route_gougi_ko sk ");
        sql.append("  ON sh.denpyou_id = sk.denpyou_id AND sh.edano = sk.edano ");
        sql.append(" LEFT OUTER JOIN "); //枝番・合議枝番・部門コード・部門ロールごとに現在フラグONの最小の合議枝枝番を取得
        sql.append("  (SELECT denpyou_id, edano,gougi_edano, bumon_cd as gen_bcd, bumon_role_id as gen_bri, min(gougi_edaedano) as minedaedano FROM shounin_route_gougi_ko ");
        sql.append("  WHERE gougi_genzai_flg = '1' GROUP BY denpyou_id, edano, gougi_edano, gen_bcd, gen_bri) skg ");
        sql.append("  ON sk.denpyou_id = skg.denpyou_id AND sk.edano = skg.edano AND sk.gougi_edano = skg.gougi_edano ");
        sql.append(" LEFT OUTER JOIN "); //枝番・合議枝番・部門コード・部門ロールごとに状況枝番が記録済(承認処理済)のデータがあるかチェック
        sql.append(" (SELECT denpyou_id, edano, gougi_edano, bumon_cd, bumon_role_id, min(joukyou_edano) as minjoukyou FROM shounin_route_gougi_ko GROUP BY denpyou_id, edano, gougi_edano, bumon_cd, bumon_role_id) skj ");
        sql.append("  ON sk.denpyou_id = skj.denpyou_id AND sk.edano = skj.edano AND sk.gougi_edano = skj.gougi_edano AND sk.bumon_cd = skj.bumon_cd AND sk.bumon_role_id = skj.bumon_role_id ");
        sql.append(" LEFT OUTER JOIN shounin_route gen "); //大本の承認ルートで現在フラグを持つデータの枝番を取得
        sql.append("  ON sh.denpyou_id = gen.denpyou_id and gen.genzai_flg = '1' ");
        sql.append(" WHERE ( "); //該当伝票で承認処理済の全データを取得
        sql.append("         (gen.edano is null OR sh.edano < gen.edano ) "); //伝票が承認済か、大元の現在フラグが該当者より後
        sql.append("         OR (sh.edano = gen.edano AND sk.gougi_edano IS NOT NULL AND sk.gougi_genzai_flg = '0' ");
        sql.append("            AND (   "); //合議内で、現在フラグが0の
        sql.append("                      (sk.bumon_cd = skg.gen_bcd AND sk.bumon_role_id = skg.gen_bri AND sk.joukyou_edano IS NOT NULL ) "); //現在承認者が同一部門・同一部門ロール(承認実行したユーザ)
        sql.append("                 OR ( (sk.bumon_cd <> skg.gen_bcd OR sk.bumon_role_id <> skg.gen_bri) AND skj.minjoukyou IS NOT NULL ) "); //現在承認者が別部門・部門ロールだがminjoukyouが記録済み(割合でスキップされたユーザ)
        sql.append("                 OR ( skg.gen_bri IS NULL AND (sk.joukyou_edano IS NOT NULL OR skj.minjoukyou IS NOT NULL) ) "); //自分の属する合議に現在承認者がないがjoukyou_edanoが記録済みか、minjoukyouが記録済み(該当合議承認済みユーザ、割合でスキップされたユーザ)
        sql.append("                 ) ");
        sql.append("            ) ");
        sql.append("       ) ");
        sql.append(" AND ( (sh.kihon_model_cd <> '2' AND sh.kihon_model_cd <> '3' AND sk.gougi_edano IS NULL) "); //基本モデルが「閲覧者」「後伺い」以外
        sql.append("    OR (sk.kihon_model_cd <> '2' AND sk.kihon_model_cd <> '3' AND sk.gougi_edano IS NOT NULL) ) ");
        sql.append("GROUP BY sh.denpyou_id) shec ");
        sql.append("  ON L.denpyou_id = shec.denpyou_id ");

        //中間テーブル R（会計個別部分）
        sql.append("INNER JOIN ( ");
        //経費立替精算(普通)
        sql.append("  SELECT ");
        sql.append("    ke.denpyou_id ");
        sql.append("    ,ke.shiharai_kingaku_goukei - ke.houjin_card_riyou_kingaku - ke.kaisha_tehai_kingaku as kingaku "); //支払金額(現金 or 振込)
        sql.append("    ,ke.houjin_card_riyou_kingaku                              as houjin_shiharai "); //支払金額(法人)
        sql.append("    ,ke.kaisha_tehai_kingaku                                   as tehai_shiharai "); //支払金額(会社手配)
        sql.append("    ,ka.karibarai_kingaku                                      as karibarai_kingaku "); //仮払金額
        sql.append("    ,ke.sashihiki_shikyuu_kingaku                              as sashihiki_shikyuu_kingaku "); //差引支給金額(現金 or 振込)
        sql.append("    ,ke.shiharaibi                                             as shiharaibi ");
        sql.append("    ,kem.shiyoubi                                              as shiyoubi ");
        sql.append("    ,ke.shiharaihouhou                                         as shiharaihouhou ");
        sql.append("    ,null as user_id");
        sql.append("    ,null as shain_no");
        sql.append("    ,null as user_full_name");
        sql.append("  FROM keihiseisan ke ");
        sql.append("  LEFT OUTER JOIN keihiseisan_meisai kem ");
        sql.append("     ON ke.denpyou_id = kem.denpyou_id AND kem.denpyou_edano = 1");
        sql.append("  LEFT OUTER JOIN karibarai ka ");
        sql.append("    ON ke.karibarai_denpyou_id = ka.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    ke.dairiflg = '0' ");
        sql.append("  UNION ALL");
        //経費立替精算(代理起票)
        sql.append("  SELECT ");
        sql.append("    ke.denpyou_id ");
        sql.append("    ,sum(case when kem.houjin_card_riyou_flg='0' AND kem.kaisha_tehai_flg='0' then kem.shiharai_kingaku else 0 end) as kingaku");
        sql.append("    ,sum(case when kem.houjin_card_riyou_flg='1'                              then kem.shiharai_kingaku else 0 end) as houjin_shiharai");
        sql.append("    ,sum(case when kem.kaisha_tehai_flg='1'                                   then kem.shiharai_kingaku else 0 end) as tehai_shiharai");
        sql.append("    ,null                                    as karibarai_kingaku");
        sql.append("    ,sum(kem.shiharai_kingaku)               as sashihiki_shikyuu_kingaku");
        sql.append("    ,ke.shiharaibi                           as shiharaibi ");
        sql.append("    ,min(kem.shiyoubi)                       as shiyoubi");
        sql.append("    ,ke.shiharaihouhou                       as shiharaihouhou");
        sql.append("    ,kem.user_id                             as user_id");
        sql.append("    ,kem.shain_no                            as shain_no");
        sql.append("    ,(kem.user_sei || '　' || kem.user_mei)  as user_full_name");
        sql.append("  FROM keihiseisan ke");
        sql.append("  INNER JOIN keihiseisan_meisai kem ");
        sql.append("     ON ke.denpyou_id = kem.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    ke.dairiflg = '1' ");
        sql.append("  GROUP BY ke.denpyou_id, kem.user_id, kem.user_sei, kem.user_mei, kem.shain_no ");
        sql.append("  UNION ALL");
        //仮払申請
        sql.append("  SELECT ");
        sql.append("    denpyou_id ");
        sql.append("    ,karibarai_kingaku  as kingaku ");
        sql.append("    ,0               as houjin_shiharai ");
        sql.append("    ,0               as tehai_shiharai ");
        sql.append("    ,null            as karibarai_kingaku ");
        sql.append("    ,karibarai_kingaku  as sashihiki_shikyuu_kingaku ");
        sql.append("    ,shiharaibi      as shiharaibi ");
        sql.append("    ,null::DATE      as shiyoubi ");
        sql.append("    ,shiharaihouhou  as shiharaihouhou ");
        sql.append("    ,null            as user_id");
        sql.append("    ,null            as shain_no");
        sql.append("    ,null            as user_full_name");
        sql.append("  FROM karibarai k ");
        sql.append("  WHERE ");
        sql.append("    karibarai_on = '1' ");// 仮払ありのみ取得
        sql.append("  UNION ALL");
        //出張旅費精算
        sql.append("  SELECT ");
        sql.append("    ry.denpyou_id ");
        sql.append("    ,ry.goukei_kingaku - ry.houjin_card_riyou_kingaku - ry.kaisha_tehai_kingaku as kingaku ");
        sql.append("    ,ry.houjin_card_riyou_kingaku         as houjin_shiharai ");
        sql.append("    ,ry.kaisha_tehai_kingaku              as tehai_shiharai ");
        sql.append("    ,rk.karibarai_kingaku                 as karibarai_kingaku ");
        sql.append("    ,ry.sashihiki_shikyuu_kingaku         as sashihiki_shikyuu_kingaku ");
        sql.append("    ,ry.shiharaibi                        as shiharaibi ");
        sql.append("    ,ry.seisankikan_to                    as shiyoubi ");
        sql.append("    ,ry.shiharaihouhou                    as shiharaihouhou ");
        sql.append("    ,ry.user_id                           as user_id");
        sql.append("    ,ry.shain_no                          as shain_no");
        sql.append("    ,(ry.user_sei || '　' || ry.user_mei) as user_full_name");
        sql.append("  FROM ryohiseisan ry ");
        sql.append("  LEFT OUTER JOIN ryohi_karibarai rk ");
        sql.append("    ON ry.karibarai_denpyou_id = rk.denpyou_id ");
        sql.append("  UNION ALL");
        //出張旅費仮払
        sql.append("  SELECT ");
        sql.append("    denpyou_id ");
        sql.append("    ,karibarai_kingaku              as kingaku ");
        sql.append("    ,0                              as houjin_shiharai ");
        sql.append("    ,0                              as tehai_shiharai ");
        sql.append("    ,null                           as karibarai_kingaku ");
        sql.append("    ,karibarai_kingaku              as sashihiki_shikyuu_kingaku ");
        sql.append("    ,shiharaibi                     as shiharaibi ");
        sql.append("    ,null::DATE                     as shiyoubi ");
        sql.append("    ,shiharaihouhou                 as shiharaihouhou ");
        sql.append("    ,user_id                        as user_id");
        sql.append("    ,shain_no                       as shain_no");
        sql.append("    ,(user_sei || '　' || user_mei) as user_full_name");
        sql.append("  FROM ryohi_karibarai rk ");
        sql.append("  WHERE ");
        sql.append("    karibarai_on = '1' ");// 仮払ありのみ取得
        sql.append("  UNION ALL");
        //海外出張旅費精算
        sql.append("  SELECT ");
        sql.append("    ry.denpyou_id ");
        sql.append("    ,ry.goukei_kingaku - ry.houjin_card_riyou_kingaku - ry.kaisha_tehai_kingaku as kingaku ");
        sql.append("    ,ry.houjin_card_riyou_kingaku         as houjin_shiharai ");
        sql.append("    ,ry.kaisha_tehai_kingaku              as tehai_shiharai ");
        sql.append("    ,rk.karibarai_kingaku                 as karibarai_kingaku ");
        sql.append("    ,ry.sashihiki_shikyuu_kingaku         as sashihiki_shikyuu_kingaku ");
        sql.append("    ,ry.shiharaibi                        as shiharaibi ");
        sql.append("    ,ry.seisankikan_to                    as shiyoubi ");
        sql.append("    ,ry.shiharaihouhou                    as shiharaihouhou ");
        sql.append("    ,ry.user_id                           as user_id");
        sql.append("    ,ry.shain_no                          as shain_no");
        sql.append("    ,(ry.user_sei || '　' || ry.user_mei) as user_full_name");
        sql.append("  FROM kaigai_ryohiseisan ry ");
        sql.append("  LEFT OUTER JOIN kaigai_ryohi_karibarai rk ");
        sql.append("    ON ry.karibarai_denpyou_id = rk.denpyou_id ");
        sql.append("  UNION ALL");
        //海外出張旅費仮払
        sql.append("  SELECT ");
        sql.append("    denpyou_id ");
        sql.append("    ,karibarai_kingaku              as kingaku ");
        sql.append("    ,0                              as houjin_shiharai ");
        sql.append("    ,0                              as tehai_shiharai ");
        sql.append("    ,null                           as karibarai_kingaku ");
        sql.append("    ,karibarai_kingaku              as sashihiki_shikyuu_kingaku ");
        sql.append("    ,shiharaibi                     as shiharaibi ");
        sql.append("    ,null::DATE                     as shiyoubi ");
        sql.append("    ,shiharaihouhou                 as shiharaihouhou ");
        sql.append("    ,user_id                        as user_id");
        sql.append("    ,shain_no                       as shain_no");
        sql.append("    ,(user_sei || '　' || user_mei) as user_full_name");
        sql.append("  FROM kaigai_ryohi_karibarai rk ");
        sql.append("  WHERE ");
        sql.append("    karibarai_on = '1' ");// 仮払ありのみ取得
        sql.append("  UNION ALL");
if(setting.tsuukinteikiShiwakeSakuseiUmu().equals("1")){
        //通勤定期申請
        sql.append("  SELECT ");
        sql.append("    denpyou_id ");
        sql.append("    ,kingaku           as kingaku ");
        sql.append("    ,0                 as houjin_shiharai ");
        sql.append("    ,0                 as tehai_shiharai ");
        sql.append("    ,null              as karibarai_kingaku ");
        sql.append("    ,kingaku           as sashihiki_shikyuu_kingaku ");
        sql.append("    ,shiharaibi        as shiharaibi ");
        sql.append("    ,null::DATE        as shiyoubi ");
        sql.append("    ,'2'               as shiharaihouhou ");
        sql.append("    ,null              as user_id");
        sql.append("    ,null              as shain_no");
        sql.append("    ,null              as user_full_name");
        sql.append("  FROM tsuukinteiki t ");
        sql.append("  UNION ALL");
}
        //交通費精算
        sql.append("  SELECT ");
        sql.append("    denpyou_id ");
        sql.append("    ,goukei_kingaku - houjin_card_riyou_kingaku - kaisha_tehai_kingaku as kingaku ");
        sql.append("    ,houjin_card_riyou_kingaku as houjin_shiharai ");
        sql.append("    ,kaisha_tehai_kingaku      as tehai_shiharai ");
        sql.append("    ,null                      as karibarai_kingaku ");
        sql.append("    ,sashihiki_shikyuu_kingaku as sashihiki_shikyuu_kingaku ");
        sql.append("    ,shiharaibi                as shiharaibi ");
        sql.append("    ,seisankikan_to            as shiyoubi ");
        sql.append("    ,shiharaihouhou            as shiharaihouhou ");
        sql.append("    ,null                      as user_id");
        sql.append("    ,null                      as shain_no");
        sql.append("    ,null                      as user_full_name");
        sql.append("  FROM koutsuuhiseisan kou ");

        //カスタマイズ用
        getShikyuuKingakuMainSql_Ext(sql);

        sql.append(") R ON ");
        sql.append("  L.denpyou_id = R.denpyou_id ");
        sql.append("WHERE 1 = 1 " + WHERE_SHIKYUU_INDI).append(" ");
        return sql.toString();
    }

    /** 社員別支給金額一覧SQL（伝票共通部分）を付加する
     * @param sql メインSQL
     * @param params パラメータ
     * @param user ユーザ
     * @param kihyouBiFrom 起票日付From
     * @param kihyouBiTo 起票日付To
     * @param kihyouShozokuBumonCd 起票者所属部門コード
     * @param kihyouShozokuBumonName 起票者所属部門名
     * @param kihyouchuu   伝票状態：起票中
     * @param shinseichuu  伝票状態：申請中
     * @param syouninzumi  伝票状態：承認済
     * @param hininzumi    伝票状態：否認済
     * @param torisagezumi 伝票状態：取下済
     * @return メインSQL(+伝票共通SQL)
     */
    protected String addWhereSqlShikyuuCommon(
              String sql
            , List<Object> params
            , User user
            , Date kihyouBiFrom
            , Date kihyouBiTo
            , String kihyouShozokuBumonCd
            , String kihyouShozokuBumonName
            , String kihyouchuu
            , String shinseichuu
            , String syouninzumi
            , String hininzumi
            , String torisagezumi
            ){
        StringBuffer sqlBuf = new StringBuffer("");
        StringBuffer sqlJoutaiBuf = new StringBuffer("");

        //アクセス制御
        sqlBuf.append(makeAccessWhere(params, user, "a", "c", true));

        // 伝票状態：起票中
        if(kihyouchuu != null && !kihyouchuu.isEmpty()){
            sqlJoutaiBuf.append("  AND  ( a.denpyou_joutai=?");
            params.add(DENPYOU_JYOUTAI.KIHYOU_CHUU);
        }
        // 伝票状態：申請中
        if(shinseichuu != null && !shinseichuu.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND (a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.SHINSEI_CHUU);
        }
        // 伝票状態：承認済
        if(syouninzumi != null && !syouninzumi.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND (a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
        }
        // 伝票状態：否認済
        if(hininzumi != null && !hininzumi.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND (a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.HININ_ZUMI);
        }
        // 伝票状態：取下済
        if(torisagezumi != null && !torisagezumi.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND (a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.TORISAGE_ZUMI);
        }
        if(sqlJoutaiBuf.length() != 0) {
            sqlJoutaiBuf.append(" ) ");
            sqlBuf.append(sqlJoutaiBuf);
        }

        //起票日付
        if(kihyouBiFrom != null){
            sqlBuf.append("  AND c.touroku_time >= ?");
            params.add(kihyouBiFrom);
        }
        if(kihyouBiTo != null){
            sqlBuf.append("  AND c.touroku_time < (CAST(? AS DATE) + 1)");
            params.add(kihyouBiTo);
        }
        //起票者所属部門コード
        if(kihyouShozokuBumonCd != null && !kihyouShozokuBumonCd.isEmpty()){
            sqlBuf.append("  AND c.bumon_cd = ?");
            params.add(kihyouShozokuBumonCd);
        }
        //起票者所属部門名
        if(kihyouShozokuBumonName != null && !kihyouShozokuBumonName.isEmpty()){
            sqlBuf.append("  AND unify_kana_width(c.bumon_full_name) LIKE unify_kana_width(?)");
            params.add("%" + kihyouShozokuBumonName + "%");
        }
        // Where句追加
        return sql.replace(WHERE_SHIKYUU_COMMON, sqlBuf.toString());
    }

    /** 社員別支給金額一覧SQL（個別伝票部分）を付加する
     * @param sql メインSQL
     * @param params パラメータ
     * @param user ユーザ
     * @param kensakuShainNo 起票者社員番号
     * @param kihyouShozokuUserName 起票者名
     * @param shiharaiBiFrom 支払日From
     * @param shiharaiBiTo 支払日To
     * @param shiyouBiFrom 使用日From
     * @param shiyouBiTo 使用日To
     * @return メインSQL(+伝票個別SQL)
     */
    protected String addWhereSqlShikyuuIndi(
              String sql
            , List<Object> params
            , User user
            , String kensakuShainNo
            , String kihyouShozokuUserName
            , Date shiharaiBiFrom
            , Date shiharaiBiTo
            , Date shiyouBiFrom
            , Date shiyouBiTo
            ) {
        boolean bumonShozokuUserFlag = user.getGyoumuRoleId() == null;
        String sqlRet = sql;
        StringBuffer sqlBuf = new StringBuffer("");

        //部門所属ユーザの場合の条件 代理申請以外ならここでの絞りなし 代理申請なら承認ルートにいるか使用者がユーザーと一致するものの絞る
        if(bumonShozokuUserFlag){
            sqlBuf.append("  AND ((R.user_id is null) OR (R.user_id is not null AND (EXISTS(SELECT * FROM shounin_route WHERE denpyou_id = L.denpyou_id AND user_id = ?) OR R.user_id = ?))) ");
            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());
        }

        //支払日
        if(shiharaiBiFrom != null){
            sqlBuf.append("  AND R.shiharaibi >= ?");
            params.add(shiharaiBiFrom);
        }
        if(shiharaiBiTo != null){
            sqlBuf.append("  AND R.shiharaibi < (CAST(? AS DATE) + 1)");
            params.add(shiharaiBiTo);
        }

        //使用日
        if(shiyouBiFrom != null){
            sqlBuf.append("  AND R.shiyoubi >= ?");
            params.add(shiyouBiFrom);
        }
        if(shiyouBiTo != null){
            sqlBuf.append("  AND R.shiyoubi < (CAST(? AS DATE) + 1)");
            params.add(shiyouBiTo);
        }

        //起票者名
        if(kihyouShozokuUserName != null && !kihyouShozokuUserName.isEmpty()){
            sqlBuf.append("  AND(");
            sqlBuf.append("    (R.user_id is null ");
            appenLikeUserName2(sqlBuf, params, "L.", kihyouShozokuUserName);
            sqlBuf.append("    )");
            sqlBuf.append("    OR (R.user_id is not null ");
            appenLikeUserName2(sqlBuf, params, "R.", kihyouShozokuUserName);
            sqlBuf.append("    )");
            sqlBuf.append("  )");
        }
        //起票者社員番号
        if(kensakuShainNo != null && !kensakuShainNo.isEmpty()){
            sqlBuf.append("  AND ((R.user_id is null AND L.shain_no LIKE ?) OR (R.user_id is not null AND R.shain_no LIKE ?))");
            params.add("%" + kensakuShainNo + "%");
            params.add("%" + kensakuShainNo + "%");
        }

        // Where句追加
        sqlRet = sqlRet.replace(WHERE_SHIKYUU_INDI, sqlBuf.toString());

        return sqlRet;
    }

    /**
     * 法人カード利用明細を取得する。
     * @param user ログインユーザ
     * @param kihyouBiFrom 起票日付From
     * @param kihyouBiTo 起票日付To
     * @param kensakuShainNo 起票者社員番号
     * @param kihyouCardNum 起票者ｶｰﾄﾞ番号
     * @param kihyouShozokuBumonCd 起票者所属部門コード
     * @param kihyouShozokuBumonName 起票者所属部門名
     * @param kihyouShozokuUserName 起票者名
     * @param riyouBiFrom 利用日From
     * @param riyouBiTo 利用日To
     * @param kihyouchuu 伝票状態：起票中
     * @param shinseichuu 伝票状態：申請中
     * @param syouninzumi 伝票状態：承認済
     * @param hininzumi   伝票状態：否認済
     * @param torisagezumi 伝票状態：取下済
     * @param sortColumn1 ソートカラム①
     * @param sort1       ソート順①
     * @return 検索結果
     */
    public List<GMap> loadHoujinCardRiyouMeisaiKensaku(
              User user
            , Date kihyouBiFrom
            , Date kihyouBiTo
            , String kensakuShainNo
            , String kihyouCardNum
            , String kihyouShozokuBumonCd
            , String kihyouShozokuBumonName
            , String kihyouShozokuUserName
            , Date riyouBiFrom
            , Date riyouBiTo
            , String kihyouchuu
            , String shinseichuu
            , String syouninzumi
            , String hininzumi
            , String torisagezumi
            , String sortColumn1
            , String sort1){

        // MainSQL
        String sqlMain = this.getHoujinCardMainSql();

        // パラメータ
        List<Object> params = new ArrayList<Object>();

        // WhereSQL追加(伝票共通)
        sqlMain = this.addWhereSqlHoujinCommon(
                  sqlMain
                , params
                , user
                , kihyouBiFrom
                , kihyouBiTo
                , kihyouShozokuBumonCd
                , kihyouShozokuBumonName
                , kihyouchuu
                , shinseichuu
                , syouninzumi
                , hininzumi
                , torisagezumi);

        // 個別伝票テーブルへの検索条件
        sqlMain = this.addWhereSqlHoujinIndi(
                  sqlMain
                , params
                , riyouBiFrom
                , riyouBiTo
                , kensakuShainNo
                , kihyouCardNum
                , kihyouShozokuUserName);

        // ソート指定
        String column1  = HOUJIN_MEISAI_SORT_COLUMN.BUMON.equals(sortColumn1) ? " bumon_full_name " : " shain_no ";
        String order1  = SORT_KBN.KOU_JUN.equals(sort1) ? " DESC " : "";

        String order = column1 + order1;

        StringBuffer sql = new StringBuffer();
        sql = new StringBuffer("SELECT * FROM (" + sqlMain + ") rec").append(" ORDER BY ").append(order).append(",denpyou_id ,denpyou_edano");

        return connection.load(sql.toString(),params.toArray());
    }

    /** 法人カード利用明細メインSQLの取得
     * @return 一覧メインSQL
     */
    protected String getHoujinCardMainSql(){

        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT ");
        sql.append("  L.denpyou_id");
        sql.append("  ,L.denpyou_kbn");
        sql.append("  ,L.denpyou_shubetsu");
        sql.append("  ,L.denpyou_joutai");
        sql.append("  ,L.touroku_time ::date");
        sql.append("  ,L.bumon_full_name");
        sql.append("  ,COALESCE(R.user_id,L.user_id) as user_id");
        sql.append("  ,COALESCE(R.shain_no,L.shain_no) as shain_no");
        sql.append("  ,case ");
        sql.append("    when R.user_id is not null then ");
        sql.append("       R.user_full_name");
        sql.append("    when strpos(L.user_full_name,'(代行者:') > 0 then ");
        sql.append("       substr(L.user_full_name, 1, strpos(L.user_full_name, '(代行者:') - 1) ");
        sql.append("    else ");
        sql.append("       L.user_full_name");
        sql.append("  end as user_full_name");
        sql.append("  ,COALESCE(RUSER.houjin_card_shikibetsuyou_num,L.houjin_card_shikibetsuyou_num) as houjin_card_shikibetsuyou_num");

        sql.append("  ,(R.naiyou || R.bikou || R.tekiyou) as naiyou_bikou_tekiyou");
        sql.append("  ,R.riyou_kingaku ");
        sql.append("  ,R.riyoubi ");
        sql.append("  ,R.denpyou_edano ");

        //中間テーブル L（伝票共通部分）
        sql.append("FROM ( ");
        sql.append("  SELECT ");
        sql.append("    a.denpyou_id");
        sql.append("    ,a.denpyou_kbn");
        sql.append("    ,b.denpyou_shubetsu");
        sql.append("    ,a.denpyou_joutai");

        sql.append("    ,c.touroku_time");
        sql.append("    ,c.bumon_full_name");
        sql.append("    ,c.user_id");
        sql.append("    ,e.shain_no");
        sql.append("    ,c.user_full_name");
        sql.append("    ,e.houjin_card_shikibetsuyou_num");
        sql.append("  FROM denpyou a");
        sql.append("  INNER JOIN denpyou_shubetsu_ichiran b ON ");
        sql.append("     a.denpyou_kbn = b.denpyou_kbn ");
        sql.append("  INNER JOIN shounin_route c ON ");
        sql.append("     a.denpyou_id = c.denpyou_id ");
        sql.append("     AND c.edano = 1");
        sql.append("  INNER JOIN user_info e ON "); //ユーザーが消えて見えなくなるのはポリシーに反するのだが
        sql.append("     c.user_id = e.user_id ");
        sql.append("  WHERE 1 = 1 " + WHERE_HOUJIN_COMMON).append(" ");
        sql.append(") L ");

        //中間テーブル R（会計個別部分）
        sql.append("INNER JOIN ( ");
        //経費立替精算
        sql.append("  SELECT ");
        sql.append("    denpyou_id ");
        sql.append("    , denpyou_edano ");
        sql.append("    , ''                  as naiyou ");
        sql.append("    , ''                  as bikou ");
        sql.append("    , tekiyou             as tekiyou ");
        sql.append("    , shiharai_kingaku    as riyou_kingaku ");
        sql.append("    , shiyoubi            as riyoubi ");
        sql.append("    , user_id                            as user_id");
        sql.append("    , shain_no                           as shain_no");
        sql.append("    , (user_sei || '　' || user_mei)     as user_full_name");
        sql.append("  FROM ");
        sql.append("    keihiseisan_meisai ");
        sql.append("  WHERE ");
        sql.append("    houjin_card_riyou_flg = '1' ");
        sql.append("  UNION ALL");
        //出張旅費精算（交通費、日当）
        sql.append("  SELECT ");
        sql.append("    m.denpyou_id ");
        sql.append("    , denpyou_edano ");
        sql.append("    , m.naiyou || '/'   as naiyou ");
        sql.append("    , m.bikou || '/'    as bikou ");
        sql.append("    , h.tekiyou         as tekiyou ");
        sql.append("    , m.meisai_kingaku  as riyou_kingaku ");
        sql.append("    , m.kikan_from      as riyoubi ");
        sql.append("    , h.user_id                          as user_id");
        sql.append("    , h.shain_no                         as shain_no");
        sql.append("    , (h.user_sei || '　' || h.user_mei) as user_full_name");
        sql.append("  FROM ");
        sql.append("    ryohiseisan_meisai m ");
        sql.append("  INNER JOIN ryohiseisan h ");
        sql.append("    ON m.denpyou_id = h.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    houjin_card_riyou_flg = '1' ");
        sql.append("  UNION ALL");
        //出張旅費精算（その他経費）
        sql.append("  SELECT ");
        sql.append("    m.denpyou_id ");
        sql.append("    , m.denpyou_edano+5000 ");
        sql.append("    , ''                  as naiyou ");
        sql.append("    , ''                  as bikou ");
        sql.append("    , m.tekiyou           as tekiyou ");
        sql.append("    , m.shiharai_kingaku  as riyou_kingaku ");
        sql.append("    , m.shiyoubi          as riyoubi ");
        sql.append("    , h.user_id                          as user_id");
        sql.append("    , h.shain_no                         as shain_no");
        sql.append("    , (h.user_sei || '　' || h.user_mei) as user_full_name");
        sql.append("  FROM ");
        sql.append("    ryohiseisan_keihi_meisai m ");
        sql.append("  INNER JOIN ryohiseisan h ");
        sql.append("    ON m.denpyou_id = h.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    houjin_card_riyou_flg = '1' ");
        sql.append("  UNION ALL");
        //海外出張旅費精算（交通費、日当）
        sql.append("  SELECT ");
        sql.append("    m.denpyou_id ");
        sql.append("    , m.denpyou_edano ");
        sql.append("    , m.naiyou || '/'   as naiyou ");
        sql.append("    , m.bikou || '/'    as bikou ");
        sql.append("    , (CASE m.kaigai_flg WHEN '1' THEN h.kaigai_tekiyou ELSE h.tekiyou END) as tekiyou ");
        sql.append("    , m.meisai_kingaku  as riyou_kingaku ");
        sql.append("    , m.kikan_from      as riyoubi ");
        sql.append("    , h.user_id                          as user_id");
        sql.append("    , h.shain_no                         as shain_no");
        sql.append("    , (h.user_sei || '　' || h.user_mei) as user_full_name");
        sql.append("  FROM ");
        sql.append("    kaigai_ryohiseisan_meisai m ");
        sql.append("  INNER JOIN kaigai_ryohiseisan h ");
        sql.append("    ON m.denpyou_id = h.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    houjin_card_riyou_flg = '1' ");
        sql.append("  UNION ALL");
        //海外出張旅費精算（その他経費）
        sql.append("  SELECT ");
        sql.append("    m.denpyou_id ");
        sql.append("    , m.denpyou_edano+5000 ");
        sql.append("    , ''                  as naiyou ");
        sql.append("    , ''                  as bikou ");
        sql.append("    , m.tekiyou           as tekiyou ");
        sql.append("    , m.shiharai_kingaku  as riyou_kingaku ");
        sql.append("    , m.shiyoubi          as riyoubi ");
        sql.append("    , h.user_id                          as user_id");
        sql.append("    , h.shain_no                         as shain_no");
        sql.append("    , (h.user_sei || '　' || h.user_mei) as user_full_name");
        sql.append("  FROM ");
        sql.append("    kaigai_ryohiseisan_keihi_meisai m ");
        sql.append("  INNER JOIN kaigai_ryohiseisan h ");
        sql.append("    ON m.denpyou_id = h.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    houjin_card_riyou_flg = '1' ");
        sql.append("  UNION ALL");
        //交通費精算
        sql.append("  SELECT ");
        sql.append("    m.denpyou_id ");
        sql.append("    , m.denpyou_edano ");
        sql.append("    , m.naiyou || '/'     as naiyou ");
        sql.append("    , m.bikou || '/'      as bikou ");
        sql.append("    , h.tekiyou           as tekiyou ");
        sql.append("    , m.meisai_kingaku    as riyou_kingaku ");
        sql.append("    , m.kikan_from        as riyoubi ");
        sql.append("    , null                as user_id");
        sql.append("    , null                as shain_no");
        sql.append("    , null                as user_full_name");
        sql.append("  FROM ");
        sql.append("    koutsuuhiseisan_meisai m ");
        sql.append("  INNER JOIN koutsuuhiseisan h ");
        sql.append("    ON m.denpyou_id = h.denpyou_id ");
        sql.append("  WHERE ");
        sql.append("    houjin_card_riyou_flg = '1' ");
        sql.append(") R ON ");
        sql.append("  L.denpyou_id = R.denpyou_id ");

        //ユーザー(法人カード番号)
        sql.append("LEFT OUTER JOIN user_info RUSER ON ");
        sql.append("  R.user_id = RUSER.user_id ");

        sql.append("WHERE 1 = 1 " + WHERE_HOUJIN_INDI).append(" ");
        return sql.toString();
    }

    /** 法人カード利用明細SQL（伝票共通部分）を付加する
     * @param sql メインSQL
     * @param params パラメータ
     * @param user ログインユーザ
     * @param kihyouBiFrom 起票日付From
     * @param kihyouBiTo 起票日付To
     * @param kihyouShozokuBumonCd 起票者所属部門コード
     * @param kihyouShozokuBumonName 起票者所属部門名
     * @param kihyouchuu   伝票状態：起票中
     * @param shinseichuu  伝票状態：申請中
     * @param syouninzumi  伝票状態：承認済
     * @param hininzumi    伝票状態：否認済
     * @param torisagezumi 伝票状態：取下済
     * @return メインSQL(+伝票共通SQL)
     */
    protected String addWhereSqlHoujinCommon(
              String sql
            , List<Object> params
            , User user
            , Date kihyouBiFrom
            , Date kihyouBiTo
            , String kihyouShozokuBumonCd
            , String kihyouShozokuBumonName
            , String kihyouchuu
            , String shinseichuu
            , String syouninzumi
            , String hininzumi
            , String torisagezumi
            ){
        StringBuffer sqlBuf = new StringBuffer("");
        StringBuffer sqlJoutaiBuf = new StringBuffer("");

        //アクセス制御
        sqlBuf.append(makeAccessWhere(params, user, "a", "c", true));

        // 伝票状態：起票中
        if(kihyouchuu != null && !kihyouchuu.isEmpty()){
            sqlJoutaiBuf.append("  AND  ( a.denpyou_joutai=?");
            params.add(DENPYOU_JYOUTAI.KIHYOU_CHUU);
        }
        // 伝票状態：申請中
        if(shinseichuu != null && !shinseichuu.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND ( a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.SHINSEI_CHUU);
        }
        // 伝票状態：承認済
        if(syouninzumi != null && !syouninzumi.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND ( a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
        }
        // 伝票状態：否認済
        if(hininzumi != null && !hininzumi.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND ( a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.HININ_ZUMI);
        }
        // 伝票状態：取下済
        if(torisagezumi != null && !torisagezumi.isEmpty()){
            if(sqlJoutaiBuf.length() != 0) {
                sqlJoutaiBuf.append("  OR  a.denpyou_joutai=?");
            } else {
                sqlJoutaiBuf.append("  AND ( a.denpyou_joutai=?");
            }
            params.add(DENPYOU_JYOUTAI.TORISAGE_ZUMI);
        }
        if(sqlJoutaiBuf.length() != 0) {
            sqlJoutaiBuf.append(" ) ");
            sqlBuf.append(sqlJoutaiBuf);
        }

        //起票日付
        if(kihyouBiFrom != null){
            sqlBuf.append("  AND c.touroku_time >= ?");
            params.add(kihyouBiFrom);
        }
        if(kihyouBiTo != null){
            sqlBuf.append("  AND c.touroku_time < (CAST(? AS DATE) + 1)");
            params.add(kihyouBiTo);
        }
        //起票者所属部門コード
        if(kihyouShozokuBumonCd != null && !kihyouShozokuBumonCd.isEmpty()){
            sqlBuf.append("  AND c.bumon_cd = ?");
            params.add(kihyouShozokuBumonCd);
        }
        //起票者所属部門名
        if(kihyouShozokuBumonName != null && !kihyouShozokuBumonName.isEmpty()){
            sqlBuf.append("  AND unify_kana_width(c.bumon_full_name) LIKE unify_kana_width(?)");
            params.add("%" + kihyouShozokuBumonName + "%");
        }

        // Where句追加
        return sql.replace(WHERE_HOUJIN_COMMON, sqlBuf.toString());
    }

    /** 法人カード利用明細SQL（個別伝票部分）を付加する
     * @param sql メインSQL
     * @param params パラメータ
     * @param riyouBiFrom 利用日From
     * @param riyouBiTo 利用日To
     * @param kensakuShainNo 起票者社員番号
     * @param kihyouCardNum 起票者ｶｰﾄﾞ番号
     * @param kihyouShozokuUserName 起票者名
     * @return メインSQL(+伝票個別SQL)
     */
    protected String addWhereSqlHoujinIndi(
              String sql
            , List<Object> params
            , Date riyouBiFrom
            , Date riyouBiTo
            , String kensakuShainNo
            , String kihyouCardNum
            , String kihyouShozokuUserName
            ) {

        String sqlRet = sql;
        StringBuffer sqlBuf = new StringBuffer("");

        //利用日
        if(riyouBiFrom != null){
            sqlBuf.append("  AND R.riyoubi >= ?");
            params.add(riyouBiFrom);
        }
        if(riyouBiTo != null){
            sqlBuf.append("  AND R.riyoubi < (CAST(? AS DATE) + 1)");
            params.add(riyouBiTo);
        }
        //起票者名
        if(kihyouShozokuUserName != null && !kihyouShozokuUserName.isEmpty()){
            sqlBuf.append("  AND(");
            sqlBuf.append("    (R.user_id is null ");
            appenLikeUserName2(sqlBuf, params, "L.", kihyouShozokuUserName);
            sqlBuf.append("    )");
            sqlBuf.append("    OR (R.user_id is not null ");
            appenLikeUserName2(sqlBuf, params, "R.", kihyouShozokuUserName);
            sqlBuf.append("    )");
            sqlBuf.append("  )");
        }
        //起票者社員番号
        if(kensakuShainNo != null && !kensakuShainNo.isEmpty()){
            sqlBuf.append("  AND ((R.user_id is null AND L.shain_no LIKE ?) OR (R.user_id is not null AND R.shain_no LIKE ?))");
            params.add("%" + kensakuShainNo + "%");
            params.add("%" + kensakuShainNo + "%");
        }
        //起票者ｶｰﾄﾞ番号
        if(kihyouCardNum != null && !kihyouCardNum.isEmpty()){
            sqlBuf.append("  AND ((R.user_id is null AND L.houjin_card_shikibetsuyou_num LIKE ?) OR (R.user_id is not null AND RUSER.houjin_card_shikibetsuyou_num LIKE ?))");
            params.add("%" + kihyouCardNum + "%");
            params.add("%" + kihyouCardNum + "%");
        }

        // Where句追加
        sqlRet = sqlRet.replace(WHERE_HOUJIN_INDI, sqlBuf.toString());

        return sqlRet;
    }

    /**
     * ユーザー名のあいまい検索からユーザーIDを検索して、SQLにユーザーIDのIN検索条件を追加する。
     *
     * tblPrefix = Rとして以下のSQL断片を生成
     * 検索結果(user_id)が"a", "b"の時に追記する条件： AND R.user_id IN ('a', 'b')
     * 検索結果(user_id)がなしの時に追記する条件： AND R.user_id = null ※絶対に検索結果0になる
     *
     * @param sqlBuf 作成中SQL
     * @param params 作成中SQL-PARAMS
     * @param tblPrefix テーブル名接頭語。SQLに複数user_idが検出される時、識別の為につける。
     * @param userName ユーザー名（こいつからユーザーIDを検索する）
     */
    protected void appenLikeUserName(StringBuffer sqlBuf, List<Object> params, String tblPrefix, String userName) {
        userName = n2b(userName).replaceAll("[ 　]", ""); //null → ブランク → 半全スペーストリム
        BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
        // 起票者名からユーザー情報を抜き出す
        List<GMap> userList = bumonUserLogic.userSerach(userName, "", "", "", false);

        if (!userList.isEmpty()) {
            sqlBuf.append("  AND " + tblPrefix + "user_id IN ( ");
            for (int i = 0; i < userList.size(); i++) {
                if (userList.size() - 1 != i) {
                    sqlBuf.append("?, ");
                } else {
                    sqlBuf.append("? ) ");
                }
                params.add(userList.get(i).get("user_id"));
            }
        } else {
            sqlBuf.append("  AND " + tblPrefix + "user_id = null ");
        }
    }

    //※※※ elseのc.user_id = nullというのが修正後の社員別支給一覧と合ってない・・・汚いのだが、時間がないのでこれでいく
    /**
     * ユーザー名のあいまい検索からユーザーIDを検索して、SQLにユーザーIDのIN検索条件を追加する。
     *
     * tblPrefix = cとして以下のSQL断片を生成
     * 検索結果(user_id)が"a", "b"の時に追記する条件： AND c.user_id IN ('a', 'b')
     * 検索結果(user_id)がなしの時に追記する条件： AND c.user_id = null ※絶対に検索結果0になる
     *
     * @param sqlBuf 作成中SQL
     * @param params 作成中SQL-PARAMS
     * @param tblPrefix テーブル名接頭語。SQLに複数user_idが検出される時、識別の為につける。
     * @param userName ユーザー名（こいつからユーザーIDを検索する）
     */
    protected void appenLikeUserName2(StringBuffer sqlBuf, List<Object> params, String tblPrefix, String userName) {
        userName = n2b(userName).replaceAll("[ 　]", ""); //null → ブランク → 半全スペーストリム
        BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
        // 起票者名からユーザー情報を抜き出す
        List<GMap> userList = bumonUserLogic.userSerach(userName, "", "", "", false);

        if (!userList.isEmpty()) {
            sqlBuf.append("  AND " + tblPrefix + "user_id IN ( ");
            for (int i = 0; i < userList.size(); i++) {
                if (userList.size() - 1 != i) {
                    sqlBuf.append("?, ");
                } else {
                    sqlBuf.append("? ) ");
                }
                params.add(userList.get(i).get("user_id"));
            }
        } else {
            sqlBuf.append("  AND "  + tblPrefix + "user_id = null ");
        }
    }

    /**
     * カスタマイズ用 getDenpyouIchiranMainSql
     * @param sql SQL
     */
    protected void getDenpyouIchiranMainSql_Ext(StringBuffer sql) {}

    /**
     * カスタマイズ用 addWhereSqlDenpyouIndi
     *
     * @param denpyouKbn 伝票区分
     * @param sqlBuf SQL
     * @param tekiyouInputted 摘要入力チェック
     * @param kariBumonCdInputted 借方部門コード入力チェック
     * @param kariBumonNameInputted 借方部門名入力チェック
     * @param kariKamokuCdInputted 借方科目コード入力チェック
     * @param kariKamokuNameInputted 借方科目名入力チェック
     * @param kariToriCdInputted 借方取引コード入力チェック
     * @param kariToriNameInputted 借方取引名入力チェック
     * @param kashiBumonCdInputted 貸方負担部門コード入力チェック
     * @param kashiBumonNameInputted 貸方負担部門名入力チェック
     * @param kashiKamokuCdInputted 貸方科目コード入力チェック
     * @param kashiKamokuNameInputted 貸方科目名入力チェック
     * @param kashiToriCdInputted 貸方取引コード入力チェック
     * @param kashiToriNameInputted 貸方取引名入力チェック
     * @param miseisanInputted 未精算入力チェック
     * @param fbStatusInputted FBステータス入力チェック
     * @param sqlRet 結果SQL
     * @param fbStatus FBステータス
     * @param denpyouShubetsu 伝票種別
     * @param params パラメータ
     * @return 結果SQL
     */
    protected String addWhereSqlDenpyouIndi_Ext(
            String denpyouKbn,
            StringBuffer sqlBuf,
            boolean tekiyouInputted,
            boolean kariBumonCdInputted,
            boolean kariBumonNameInputted,
            boolean kariKamokuCdInputted,
            boolean kariKamokuNameInputted,
            boolean kariToriCdInputted,
            boolean kariToriNameInputted,
            boolean kashiBumonCdInputted,
            boolean kashiBumonNameInputted,
            boolean kashiKamokuCdInputted,
            boolean kashiKamokuNameInputted,
            boolean kashiToriCdInputted,
            boolean kashiToriNameInputted,
            boolean miseisanInputted,
            boolean fbStatusInputted,
            String sqlRet,
            String fbStatus,
            String denpyouShubetsu,
            List<Object> params) {
        return sqlRet;
    }

    /**
     * カスタマイズ用 getShikyuuKingakuMainSql
     * @param sql SQL
     */
    protected void getShikyuuKingakuMainSql_Ext(StringBuffer sql) {

    }




    /**
     * 一覧検索の共通アクセス制御
     * @param params SQLパラメータ
     * @param user ユーザー
     * @param m メインテーブルの別名
     * @param r 承認ルートテーブルの別名
     * @param isGougiSeigen 合議制限
     * @return WHERE句一部
     */
    public String makeAccessWhere(List<Object> params, User user, String m, String r, boolean isGougiSeigen) {
        BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
        String sql = "";

        //閲覧制限 未完了 OR 一定日数内 OR 基本モデル以外→OK
        int etsuranshaNissuu = Integer.parseInt(setting.etsuranSeigenEtsuransha());
        String etsuranshaSeigen = "";
        if(etsuranshaNissuu != 0){
            etsuranshaSeigen =" AND("
                                +    m + ".denpyou_joutai<'30'"
                                +"   OR CURRENT_DATE<DATE((SELECT MAX(koushin_time) FROM shounin_route ls WHERE ls.denpyou_id=" + m + ".denpyou_id)) + interval '" + etsuranshaNissuu + " months'"
                                +"   OR kihon_model_cd<>'2'"
                                +" ) ";
        }

        //合議制限 未完了 AND (最終承認者以外の承認者が残っている OR 合議が未CLOSE) 合議が未CLOSE = 同じ合議内で承認権なしで未処理がいる OR 現在フラグ=1の承認ルートがその合議以前である
        String gougishaSeigen = "";
        if(isGougiSeigen && setting.etsuranSeigenGougi().equals("0")){
            gougishaSeigen =" AND( "
                                +"   (" + m + ".denpyou_joutai<'30')"
                                +"   AND("
                                +"     EXISTS(SElECT * FROM shounin_route WHERE"
                                +"       denpyou_id=" + m + ".denpyou_id"
                                 +"       AND edano>=(SELECT MAX(edano) FROM shounin_route WHERE denpyou_id=" + m + ".denpyou_id AND genzai_flg='1')"
                                 +"       AND saishu_shounin_flg<>'1')"
                                 +"     OR"
                                 +"     ("
                                 +"       EXISTS(SELECT * FROM shounin_route_gougi_ko WHERE"
                                 +"         (denpyou_id,gougi_edano)=(tmp_gk.denpyou_id,tmp_gk.gougi_edano)"
                                 +"         AND shounin_ken_flg='0'"
                                 +"         AND joukyou_edano IS NULL)"
                                 +"       OR"
                                 +"       tmp_gk.edano>=(SELECT MAX(edano) FROM shounin_route WHERE denpyou_id=" + m + ".denpyou_id AND genzai_flg='1')"
                                 +"     )"
                                 +"   )"
                                 +" ) ";
        }

        //部門所属ユーザの場合の条件
        if(user.getGyoumuRoleId() == null){


            //承認ルートに自分がいる または 自分が被代理起票者
            sql += (" AND(");
            sql += (" ( ");
            sql += ("       (EXISTS (SELECT * FROM shounin_route tmp_r WHERE  tmp_r.denpyou_id = " + m + ".denpyou_id AND  tmp_r.user_id=?" + etsuranshaSeigen + "))");
            sql += ("    OR (EXISTS (SELECT * FROM shounin_route_gougi_ko tmp_gk WHERE tmp_gk.denpyou_id = " + m + ".denpyou_id AND tmp_gk.user_id=?" + etsuranshaSeigen + gougishaSeigen + "))");
            sql += ("    OR (" + m + ".denpyou_kbn = 'A001' AND EXISTS (SELECT * FROM keihiseisan_meisai        WHERE denpyou_id = " + m + ".denpyou_id AND user_id = ?))");
            sql += ("    OR (" + m + ".denpyou_kbn = 'A004' AND EXISTS (SELECT * FROM ryohiseisan               WHERE denpyou_id = " + m + ".denpyou_id AND user_id = ?))");
            sql += ("    OR (" + m + ".denpyou_kbn = 'A005' AND EXISTS (SELECT * FROM ryohi_karibarai           WHERE denpyou_id = " + m + ".denpyou_id AND user_id = ?))");
            sql += ("    OR (" + m + ".denpyou_kbn = 'A011' AND EXISTS (SELECT * FROM kaigai_ryohiseisan        WHERE denpyou_id = " + m + ".denpyou_id AND user_id = ?))");
            sql += ("    OR (" + m + ".denpyou_kbn = 'A012' AND EXISTS (SELECT * FROM kaigai_ryohi_karibarai    WHERE denpyou_id = " + m + ".denpyou_id AND user_id = ?))");
            sql += (" ) :bumon_access_append ");
            sql += (" ) ");

            int bumonDataKyoyu = Integer.parseInt(setting.shozokuBumonDataKyoyu());
            if(bumonDataKyoyu == 1){
                //「所属部門＆配下部門のデータ共有する・しない」オプション設定値が1(する)の場合
                //該当ユーザの所属部門・配下部門により起票された伝票は
                //該当ユーザが承認ルート上に存在しない場合も参照可能とする
                StringBuilder sbBumon = new StringBuilder();
                List<String> lstBumon = new ArrayList<String>();

                // ログインユーザに紐づく所属部門の件数分、配下部門のリストを取得する。
                for (String aBumonCd : user.getBumonCd()){
                    // まず、自所属部門をリストに追加
                    lstBumon.add(aBumonCd);
                    // 自所属部門を親とする子部門を取得して、リストに追加
                    List<GMap> lstNestBumon = bumonUserLogic.selectBumonTreeStructure(aBumonCd, true);
                    for (GMap aMap : lstNestBumon){
                        lstBumon.add(aMap.get("bumon_cd").toString());
                    }
                }
                // 取得した部門リストを絞り込みSQLのパラメータに編集する。
                for (String aBumon : lstBumon){
                    if (0 < sbBumon.length()){
                        sbBumon.append(",");
                    }
                    sbBumon.append("'").append(aBumon).append("'");
                }
                // 有効部門が存在しない場合にSQLエラーとなるため制御追加
                String addSql;
                if (0 < sbBumon.length()){
                    addSql = " OR (" + r + ".bumon_cd IN( " + sbBumon.toString() + " )  ) ";
                }else{
                    addSql = "";
                }
                sql = sql.replace(":bumon_access_append", addSql);
            }else{
                sql = sql.replace(":bumon_access_append", "");
            }

            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());
            params.add(user.getSeigyoUserId());



        //業務ロールユーザの場合の条件
        }else if(user.getGyoumuRoleId() != null && !(Arrays.asList(user.getGyoumuRoleShozokuBumonCd()).contains(BUMON_CD.ZENSHA)) ){
            String searchGyoumuRoleShozokuBumonCd = bumonUserLogic.getMergeGyoumuRoleShozokuBumonCd(user.getGyoumuRoleShozokuBumonCd(), true);
            sql += (" AND " + r + ".bumon_cd IN( " + searchGyoumuRoleShozokuBumonCd + " ) ");
        }
        return sql;
    }


    /**
     * 起案一覧CSV出力<br>
     *
     * @param kianMap 起案番号Map(予めloadKianListにて取得)
     * @param kihyouBiFrom 起票日From
     * @param kihyouBiTo 起票日To
     * @param shouninBiFrom 承認日From
     * @param shouninBiTo 承認日To
     * @param kingakuFrom 金額From
     * @param kingakuTo 金額To
     * @param tekiyou 摘要
     * @param bumonCdFrom 部門コードFrom
     * @param bumonCdTo 部門コードTo
     * @param kanitodokeKenmei 簡易届の件名
     * @return 起案一覧CSVデータリスト
     */
    public List<GMap> loadKianIchiranCsvDataWithMap(
                                          GMap kianMap
                                        , Date kihyouBiFrom
                                        , Date kihyouBiTo
                                        , Date shouninBiFrom
                                        , Date shouninBiTo
                                        , BigDecimal kingakuFrom
                                        , BigDecimal kingakuTo
                                        , String tekiyou
                                        , String bumonCdFrom
                                        , String bumonCdTo
                                        , String kanitodokeKenmei
                                        ) {
        List<GMap> lstResult = new ArrayList<GMap>();

        // 指定した起案番号Mapに対して、以下の処理を実施

        GMap mapCsv = new GMap();
        BigDecimal kianGoukei = new BigDecimal(0);
        BigDecimal iraiGoukei = new BigDecimal(0);

        String yosanShikkouTaishou = kianMap.get("yosan_shikkou_taishou").toString();
        String denpyouId = kianMap.get("denpyou_id").toString();
        String denpyouKbn = kianMap.get("denpyou_kbn").toString();
        String jisshiNendo = kianMap.get("jisshi_nendo").toString();
        String jisshiKianBangou = kianMap.get("jisshi_kian_bangou").toString();
        String shishutsuNendo = (null != kianMap.get("shishutsu_nendo"))? kianMap.get("shishutsu_nendo").toString() : "";
        String shishutsuKianBangou = (null != kianMap.get("shishutsu_kian_bangou"))? kianMap.get("shishutsu_kian_bangou").toString() : "";
        log.debug("■■■jisshi_nendo=[" + jisshiNendo + "] jisshi_kian_bangou=[" + jisshiKianBangou + "]");
        log.debug("■■■shishutsu_nendo=[" + shishutsuNendo + "] shishutsu_kian_bangou=[" + shishutsuKianBangou + "]");

        // 取得した起案の情報を取得する（ここは１件のみ取得となる）。
        List<GMap> lstInfo = this.loadKianDenpyouList(
                                                      yosanShikkouTaishou
                                                    , jisshiNendo
                                                    , jisshiKianBangou
                                                    , denpyouId
                                                    , denpyouKbn
                                                    , kihyouBiFrom
                                                    , kihyouBiTo
                                                    , shouninBiFrom
                                                    , shouninBiTo
                                                    , tekiyou
                                                    , bumonCdFrom
                                                    , bumonCdTo
                                                    , kanitodokeKenmei
                                                    );
        // 起案の情報が取得できない場合、空リスト返却(次の起案番号の処理へ)
        if(null == lstInfo || 0 == lstInfo.size()){ return lstResult; }

        for (GMap mapInfo : lstInfo){
            String jDenpyouKbn = mapInfo.get("denpyou_kbn").toString();
// String jVersion = mapInfo.get("version").toString();
            String jDenpyouId = mapInfo.get("denpyou_id").toString();
            String jKianDenyouId = mapInfo.get("kian_denpyou").toString();
            String jKenmei = super.n2b(mapInfo.get("kenmei"));
            String jTekiyou = super.n2b(mapInfo.get("tekiyou"));
            String jDenpyouShubetsu = super.n2b(mapInfo.get("denpyou_shubetsu"));
            String jYosanShikkouTaishou = super.n2b(mapInfo.get("yosan_shikkou_taishou"));
            String jKianShinseiBi = this.formatDate(mapInfo.get("kian_shinsei_bi"));
            String jKianShouninBi = this.formatDate(mapInfo.get("kian_shounin_bi"));
            String jKianSyuryoBi = this.formatDate(mapInfo.get("kian_syuryo_bi"));
            log.debug("■■■<起案>kenmei=[" + jKenmei + "] kian_shinsei_bi=[" + jKianShinseiBi + "] kian_shounin_bi=[" + jKianShouninBi + "] kian_syuryo_bi=[" + jKianSyuryoBi + "]");

            // 起案・支出合計に起案の金額を加算する。
            BigDecimal jisshiKianKingaku = this.getKingaku(jDenpyouKbn, jDenpyouId);
            kianGoukei = kianGoukei.add(jisshiKianKingaku);
            log.debug("■■■<起案>kianGoukei=[" + jisshiKianKingaku + "]");

            // 金額合計 < 金額From または 金額合計 > 金額Toの場合
            if((null != kingakuFrom && kianGoukei.compareTo(kingakuFrom) == -1) || (null != kingakuTo && kianGoukei.compareTo(kingakuTo) == 1)){
                // 処理中断。次の起案番号の処理へ。
                continue;
            }

            // 起案に紐づく支出依頼の情報を取得する。
            String iraiShinseiBi = "";
            String iraiShouninBi = "";
            String iraiCnt = "";
            GMap mapIraiInfo = this.getIraiDenpyouInfo(yosanShikkouTaishou, jisshiNendo, jisshiKianBangou, jDenpyouId);
            if(null != mapIraiInfo){
                iraiShinseiBi = this.formatDate(mapIraiInfo.get("irai_shinsei_bi"));
                iraiShouninBi = this.formatDate(mapIraiInfo.get("irai_shounin_bi"));
                iraiCnt = Long.valueOf((long)mapIraiInfo.get("irai_cnt")).toString();
            }
            log.debug("■■■<支出依頼>irai_shinsei_bi=[" + iraiShinseiBi + "] irai_shounin_bi=[" + iraiShouninBi + "] irai_cnt=[" + iraiCnt + "]");

            // CSV出力データに格納
            // ・起案番号
            mapCsv.put("kianbangou", (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou))? shishutsuKianBangou : jisshiKianBangou);
            // ・件名
            WorkflowEventControlLogic wfecLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
            if (wfecLogic.isKaniTodoke(denpyouKbn)){
                mapCsv.put("kenmei", jKenmei);
            }else{
                mapCsv.put("kenmei", jTekiyou);
            }
            // ・伝票種別
            mapCsv.put("denpyou_shubetsu", jDenpyouShubetsu);
            // ・予算執行対象
            mapCsv.put("yosan_shikkou_taishou", jYosanShikkouTaishou);
            // ・起案・申請日
            mapCsv.put("kian_shinseibi", jKianShinseiBi);
            // ・起案・決済日
            mapCsv.put("kian_kessaibi", jKianShouninBi);
            // ・起案終了日
            mapCsv.put("kian_shuuryoubi", jKianSyuryoBi);
            // ・支出依頼・申請日
            mapCsv.put("irai_shinseibi", iraiShinseiBi);
            // ・支出依頼・決済日
            mapCsv.put("irai_kessaibi", iraiShouninBi);
            // ・支出依頼申請書数
            mapCsv.put("irai_kensuu", iraiCnt);

            //関連起案情報リストを取得する。
            List<GMap> lstKanren ;
            if(!EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishou)){
                lstKanren = this.loadKanrenKianDenpyouList((EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou))? YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN :YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN
                                                           ,jisshiNendo, jisshiKianBangou
                                                           ,(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou))? jKianDenyouId : jDenpyouId);
                // 取得した関連起案の件数分、以下の処理を繰り返す
                for (GMap mapKanren : lstKanren){
                    // 取得した支出起案の情報を取得する。

                    // CSV出力データに格納
                    // ・関連起案番号
                    if(null == mapCsv.get("kanren_kian_bangou")){
                        mapCsv.put("kanren_kian_bangou", mapKanren.get("kanren_kian_bangou"));

                    }else{
                        mapCsv.put("kanren_kian_bangou", mapCsv.get("kanren_kian_bangou") + "・" + mapKanren.get("kanren_kian_bangou"));
                    }

                }
            }else{
                mapCsv.put("kanren_kian_bangou", "");
            }

        }

        // 起案に紐づく支出依頼の金額を取得する。
        if(!mapCsv.isEmpty()){
            List<GMap> lstIrai = this.loadShishutsuIraiList(yosanShikkouTaishou, jisshiNendo, jisshiKianBangou, denpyouId);
            for (GMap mapIrai : lstIrai){
                String iDenpyouKbn = mapIrai.get("denpyou_kbn").toString();
// String iVersion = mapIrai.get("version").toString();
                String iDenpyouId = mapIrai.get("denpyou_id").toString();
                BigDecimal shishutsuIraiKingaku = this.getKingaku(iDenpyouKbn, iDenpyouId);
                iraiGoukei = iraiGoukei.add(shishutsuIraiKingaku);
                log.debug("■■■<支出依頼>iraiGoukei=[" + shishutsuIraiKingaku + "]");
            }

            // CSV出力データに格納
            // ・起案・支出合計の金額
            mapCsv.put("kian_shishutsu_goukei", this.formatMoney(kianGoukei));
            // ・支出依頼・金額合計
            mapCsv.put("shishutsu_irai_goukei", this.formatMoney(iraiGoukei));
            // ・起案－支出依頼
            mapCsv.put("sagaku", this.formatMoney(kianGoukei.subtract(iraiGoukei)));

            lstResult.add(mapCsv);
        }


        return lstResult;
    }

    /**
     * 起案伝票情報リスト取得<br>
     * 起案一覧CSV出力用に指定された起案伝票の関連伝票情報を取得する。<br>
     *
     * @param yosanShikkouTaishou 予算執行対象
     * @param jisshiNendo 実施起案年度
     * @param kianBangou 実施起案番号
     * @param denpyouId 伝票番号（実施起案：自身の伝票番号 支出起案：起案追加された伝票番号）
     * @return 実施起案番号リスト
     */
    protected List<GMap> loadKanrenKianDenpyouList(String yosanShikkouTaishou, String jisshiNendo, String kianBangou, String denpyouId){
        StringBuilder sbSql = new StringBuilder();
        // SQL本体
        sbSql.append(" SELECT");
        if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(yosanShikkouTaishou)){
            sbSql.append("   C.jisshi_kian_bangou AS kanren_kian_bangou");
        }else{
            sbSql.append("   C.shishutsu_kian_bangou AS kanren_kian_bangou");
        }
        sbSql.append("   ,C.*");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn ");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id ");
        sbSql.append("  INNER JOIN naibu_cd_setting g ON G.naibu_cd_name  = 'yosan_shikkou_taishou' AND A.yosan_shikkou_taishou = G.naibu_cd");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = ?");
        sbSql.append("    AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append("    AND C.jisshi_nendo = ?");
        sbSql.append("    AND C.jisshi_kian_bangou = ?");
        if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(yosanShikkouTaishou)){
            sbSql.append("    AND C.denpyou_id = '").append(denpyouId).append("'");
        }else{
            sbSql.append("    AND C.kian_denpyou = '").append(denpyouId).append("'");
        }
        sbSql.append("  ORDER BY C.ryakugou, C.kian_bangou, C.nendo, C.bumon_cd");
        return this.connection.load(sbSql.toString(), yosanShikkouTaishou, jisshiNendo, kianBangou);
    }


    /**
     * 実施起案番号リストの件数取得<br>
     *
     * @param usr ログインユーザ情報
     * @param gyoumuShubetsu 業務種別
     * @param denpyouShubetsu 伝票種別
     * @param kianBangouNendo 起案番号年度
     * @param kianBangouRyakugou 起案番号略号
     * @param kianBangou 起案番号
     * @param kianBangouRyakugouFromTo 起案番号略号FromTo
     * @param kianBangouFrom 起案番号From
     * @param kianBangouTo 起案番号To
     * @param kianBangouEnd 起案番号終了
     * @param kianBangouNoShishutsuIrai 起案番号あり且つ支出依頼なし
     * @param yosanshikkouShubetsu 予算執行種別
     * @param yosanCheckNengetsuFrom 予算執行対象月From
     * @param yosanCheckNengetsuTo 予算執行対象月To
     * @return 起案一覧CSV出力用に一覧の先頭となる実施起案伝票の一覧の件数
     */
    public long findKianListCount(
                                      User usr
                                    , String gyoumuShubetsu
                                    , String denpyouShubetsu
                                    , String kianBangouNendo
                                    , String kianBangouRyakugou
                                    , String kianBangou
                                    , String kianBangouRyakugouFromTo
                                    , String kianBangouFrom
                                    , String kianBangouTo
                                    , String kianBangouEnd
                                    , String kianBangouNoShishutsuIrai
                                    , String[] yosanshikkouShubetsu
                                    , String yosanCheckNengetsuFrom
                                    , String yosanCheckNengetsuTo
            ) {

        //基本のSQL
        StringBuilder sbSql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        sbSql.append("SELECT count(B.denpyou_id) AS datacount");
        sbSql.append(" FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append(" INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn");
        sbSql.append(" INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id");
        sbSql.append(" WHERE A.yosan_shikkou_taishou <> '" + EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI+ "'");
        sbSql.append("   AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append("   AND LENGTH(COALESCE(C.jisshi_kian_bangou, '')) > 0");

        //検索条件を付加
        sbSql.append(addWhereSqlKianList(
                  usr
                , gyoumuShubetsu
                , denpyouShubetsu
                , kianBangouNendo
                , kianBangouRyakugou
                , kianBangou
                , kianBangouRyakugouFromTo
                , kianBangouFrom
                , kianBangouTo
                , kianBangouEnd
                , kianBangouNoShishutsuIrai
                , yosanshikkouShubetsu
                , yosanCheckNengetsuFrom
                , yosanCheckNengetsuTo
                , params));

        GMap datacount = connection.find(sbSql.toString(), params.toArray());
        long count = (long)datacount.get("datacount");
        return count;
    }

    /**
     * 実施起案番号リスト取得<br>
     * 起案一覧CSV出力用に一覧の先頭となる実施起案伝票の一覧を取得する。<br>
     *
     * @param usr ログインユーザ情報
     * @param gyoumuShubetsu 業務種別
     * @param denpyouShubetsu 伝票種別
     * @param kianBangouNendo 起案番号年度
     * @param kianBangouRyakugou 起案番号略号
     * @param kianBangou 起案番号
     * @param kianBangouRyakugouFromTo 起案番号略号FromTo
     * @param kianBangouFrom 起案番号From
     * @param kianBangouTo 起案番号To
     * @param kianBangouEnd 起案番号終了
     * @param kianBangouNoShishutsuIrai 起案番号あり且つ支出依頼なし
     * @param yosanshikkouShubetsu 予算執行種別
     * @param yosanCheckNengetsuFrom 予算執行対象月From
     * @param yosanCheckNengetsuTo 予算執行対象月To
     * @param pageNo  ページ番号 ※0の場合、全件検索する。
     * @param pageMax 1ページ最大表示件数
     * @return 実施起案番号リスト
     */
    public List<GMap> loadKianList(
                                          User usr
                                        , String gyoumuShubetsu
                                        , String denpyouShubetsu
                                        , String kianBangouNendo
                                        , String kianBangouRyakugou
                                        , String kianBangou
                                        , String kianBangouRyakugouFromTo
                                        , String kianBangouFrom
                                        , String kianBangouTo
                                        , String kianBangouEnd
                                        , String kianBangouNoShishutsuIrai
                                        , String[] yosanshikkouShubetsu
                                        , String yosanCheckNengetsuFrom
                                        , String yosanCheckNengetsuTo
                                        , int pageNo, int pageMax) {

        //基本のSQL
        StringBuilder sbSql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        sbSql.append("SELECT A.yosan_shikkou_taishou, B.denpyou_id, B.denpyou_kbn, C.jisshi_nendo, C.jisshi_kian_bangou, C.shishutsu_nendo, C.shishutsu_kian_bangou");
        sbSql.append(" FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append(" INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn");
        sbSql.append(" INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id");
        sbSql.append(" WHERE A.yosan_shikkou_taishou <> '" + EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI+ "'");
        sbSql.append("   AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append("   AND LENGTH(COALESCE(C.jisshi_kian_bangou, '')) > 0");

        //検索条件を付加
        sbSql.append(addWhereSqlKianList(
                  usr
                , gyoumuShubetsu
                , denpyouShubetsu
                , kianBangouNendo
                , kianBangouRyakugou
                , kianBangou
                , kianBangouRyakugouFromTo
                , kianBangouFrom
                , kianBangouTo
                , kianBangouEnd
                , kianBangouNoShishutsuIrai
                , yosanshikkouShubetsu
                , yosanCheckNengetsuFrom
                , yosanCheckNengetsuTo
                , params));



        //ソート順
        sbSql.append(" ORDER BY C.ryakugou, C.kian_bangou, C.nendo, C.bumon_cd");

        // ページ番号（取得件数）
        if(pageNo != 0){
            sbSql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));
        }

        return this.connection.load(sbSql.toString(), params.toArray());
    }

    /**
     * 実施起案番号リスト取得<br>
     * 起案一覧CSV出力用に一覧の先頭となる実施起案伝票の一覧を取得する。<br>
     *
     * @param usr ログインユーザ情報
     * @param gyoumuShubetsu 業務種別
     * @param denpyouShubetsu 伝票種別
     * @param kianBangouNendo 起案番号年度
     * @param kianBangouRyakugou 起案番号略号
     * @param kianBangou 起案番号
     * @param kianBangouRyakugouFromTo 起案番号略号FromTo
     * @param kianBangouFrom 起案番号From
     * @param kianBangouTo 起案番号To
     * @param kianBangouEnd 起案番号終了
     * @param kianBangouNoShishutsuIrai 起案番号あり且つ支出依頼なし
     * @param yosanshikkouShubetsu 予算執行種別
     * @param yosanCheckNengetsuFrom 予算執行対象月From
     * @param yosanCheckNengetsuTo 予算執行対象月To
     * @param params パラメータ
     * @return 検索条件（AND句）
     */
    protected String addWhereSqlKianList(
                                          User usr
                                        , String gyoumuShubetsu
                                        , String denpyouShubetsu
                                        , String kianBangouNendo
                                        , String kianBangouRyakugou
                                        , String kianBangou
                                        , String kianBangouRyakugouFromTo
                                        , String kianBangouFrom
                                        , String kianBangouTo
                                        , String kianBangouEnd
                                        , String kianBangouNoShishutsuIrai
                                        , String[] yosanshikkouShubetsu
                                        , String yosanCheckNengetsuFrom
                                        , String yosanCheckNengetsuTo
                                        , List<Object> params
                                    ) {
        StringBuilder sbSql = new StringBuilder();
        /*
         * ログインユーザが所属する部門および配下部門までを表示するための部門リストを取得する。
         * ただし、業務ロールでログインしている場合は部門リストを利用しない
         */
        StringBuilder sbBumon = new StringBuilder();
        if (isEmpty(usr.getGyoumuRoleId())){
            // 業務ロールでログインしていない
            List<String> lstBumon = new ArrayList<String>();

            // ログインユーザに紐づく所属部門の件数分、配下部門のリストを取得する。
            BumonUserKanriCategoryLogic bumonUserKanriCategoryLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, super.connection);
            for (String aBumonCd : usr.getBumonCd()){
                // まず、自所属部門をリストに追加
                lstBumon.add(aBumonCd);
                // 自所属部門を親とする子部門を取得して、リストに追加
                List<GMap> lstNestBumon = bumonUserKanriCategoryLogic.selectBumonTreeStructure(aBumonCd, true);
                for (GMap aMap : lstNestBumon){
                    lstBumon.add(aMap.get("bumon_cd").toString());
                }
            }
            // 取得した部門リストを絞り込みSQLのパラメータに編集する。
            for (String aBumon : lstBumon){
                if (0 < sbBumon.length()){
                    sbBumon.append(",");
                }
                sbBumon.append("'").append(aBumon).append("'");
            }
        }

        // ログインユーザの所属部門縛りがある場合、自部門と配下部門だけを参照する条件を付加する（業務ロールログインが除外）
        if (0 < sbBumon.length()){
            sbSql.append("   AND C.bumon_cd IN (").append(sbBumon.toString()).append(")");
        }
        //業務種別
        if(gyoumuShubetsu != null && !gyoumuShubetsu.isEmpty()){
            sbSql.append("  AND A.gyoumu_shubetsu=?");
            params.add(gyoumuShubetsu);
        }
        //伝票種別
        if(denpyouShubetsu != null && !denpyouShubetsu.isEmpty()){
            sbSql.append("  AND B.denpyou_kbn=?");
            params.add(denpyouShubetsu);
        }
        // 起案番号：起案年度
        if (!super.isEmpty(kianBangouNendo)){
            sbSql.append("  AND (C.jisshi_nendo = ? OR C.shishutsu_nendo = ?)");
            params.add(kianBangouNendo);
            params.add(kianBangouNendo);
        }
        // 起案番号：起案略号
        if (!super.isEmpty(kianBangouRyakugou)){
            sbSql.append("  AND (C.jisshi_kian_bangou LIKE ? OR C.shishutsu_kian_bangou LIKE ?)");
            params.add("%" + kianBangouRyakugou + "%");
            params.add("%" + kianBangouRyakugou + "%");
        }
        // 起案番号：起案番号（検索は後方一致を行うため kianBangou は全角の必要あり：呼び元で要変換）
        if (!super.isEmpty(kianBangou)){
            sbSql.append("  AND (C.jisshi_kian_bangou LIKE ? OR C.shishutsu_kian_bangou LIKE ?)");
            params.add("% " + kianBangou + "号");
            params.add("% " + kianBangou + "号");
        }
        // 起案番号 範囲指定：起案番号略号
        if (!super.isEmpty(kianBangouRyakugouFromTo)){
            sbSql.append("  AND (C.jisshi_kian_bangou LIKE ? OR C.shishutsu_kian_bangou LIKE ?)");
            params.add("%" + kianBangouRyakugouFromTo + "%");
            params.add("%" + kianBangouRyakugouFromTo + "%");
        }
        // 起案番号 範囲指定：起案番号From-To
        //   1.NULLIFで起案番号がブランクならNULLが返却される
        //   2.COALESCEで起案番号がNULLなら起案番号を'@ ９９９９９９９号'とする（起案番号は６桁なので検索対象外となる）
        //   3.SUBSTRで番号部分を取得（スペースの位置＋１から、号の位置－スペースの位置＋１の文字数）
        //   4.TRANSLATEで番号部分を全角文字から半角文字に変換
        //   5.CASTで番号部分を数値に変換して範囲指定条件を付ける
        if (!super.isEmpty(kianBangouFrom)){
            // 起案番号Fromに指定あり
            if (!super.isEmpty(kianBangouTo)){
                // 起案番号Toに指定あり
                sbSql.append("  AND (CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sbSql.append("                     , '０１２３４５６７８９', '0123456789')");
                sbSql.append("            AS INTEGER)");
                sbSql.append("       BETWEEN ").append(kianBangouFrom).append(" AND ").append(kianBangouTo);
                sbSql.append("       OR");
                sbSql.append("       CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sbSql.append("                     , '０１２３４５６７８９', '0123456789')");
                sbSql.append("            AS INTEGER)");
                sbSql.append("       BETWEEN ").append(kianBangouFrom).append(" AND ").append(kianBangouTo);
                sbSql.append("      )");
            }else{
                // 起案番号Toに指定なし
                sbSql.append("  AND (CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sbSql.append("                     , '０１２３４５６７８９', '0123456789')");
                sbSql.append("            AS INTEGER)");
                sbSql.append("       BETWEEN ").append(kianBangouFrom).append(" AND " + String.valueOf(EteamConst.kianBangou.MAX_VALUE));
                sbSql.append("       OR");
                sbSql.append("       CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sbSql.append("                     , '０１２３４５６７８９', '0123456789')");
                sbSql.append("            AS INTEGER)");
                sbSql.append("       BETWEEN ").append(kianBangouFrom).append(" AND " + String.valueOf(EteamConst.kianBangou.MAX_VALUE));
                sbSql.append("      )");
            }
        }else{
            // 起案番号Fromに指定なし
            if (!super.isEmpty(kianBangouTo)){
                // 起案番号Toに指定あり
                sbSql.append("  AND (CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(jisshi_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sbSql.append("                     , '０１２３４５６７８９', '0123456789')");
                sbSql.append("            AS INTEGER)");
                sbSql.append("       BETWEEN 1 AND ").append(kianBangouTo);
                sbSql.append("       OR");
                sbSql.append("       CAST(TRANSLATE(SUBSTR(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "')");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1");
                sbSql.append("                            ,STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), '号') - (STRPOS(COALESCE(NULLIF(shishutsu_kian_bangou, ''), '" + KIAN_BANGOU_NULL + "'), ' ') + 1))");
                sbSql.append("                     , '０１２３４５６７８９', '0123456789')");
                sbSql.append("            AS INTEGER)");
                sbSql.append("       BETWEEN 1 AND ").append(kianBangouTo);
                sbSql.append("      )");
            }
        }
        //起案番号終了
        if(kianBangouEnd != null){
            if(KIAN_BANGOU_END.YES.equals(kianBangouEnd)){
                sbSql.append("  AND C.kian_syuryo_flg = '1'");
            }else if(KIAN_BANGOU_END.NO.equals(kianBangouEnd)){
                sbSql.append("  AND C.kian_syuryo_flg <> '1'");
            }
        }
        //起案番号あり且つ支出依頼なし分抽出
        if(kianBangouNoShishutsuIrai != null && !kianBangouNoShishutsuIrai.isEmpty()){
            // 1.AA+AB+AC で実施起案伝票で実施起案番号を持っている伝票のリスト
            // 2.AD1+AD2+AD3 で支出依頼伝票で実施起案番号を持っている伝票のリスト
            // 3.(AA+AB+AC)と(AD1+AD2+AD3)をLEFT OUTER JOINの実施起案番号で結合して支出依頼伝票を持たない伝票IDを返す
            sbSql.append("  AND B.denpyou_id IN (");
            sbSql.append("        SELECT AA.denpyou_id");
            sbSql.append("        FROM denpyou AS AA");
            sbSql.append("        INNER JOIN denpyou_shubetsu_ichiran AS AB ON AB.denpyou_kbn = AA.denpyou_kbn");
            sbSql.append("        INNER JOIN denpyou_kian_himozuke AS AC ON AC.denpyou_id = AA.denpyou_id");
            sbSql.append("        LEFT OUTER JOIN (");
            sbSql.append("         SELECT AD1.denpyou_id, AD2.yosan_shikkou_taishou, AD3.jisshi_nendo, AD3.jisshi_kian_bangou");
            sbSql.append("         FROM denpyou AS AD1");
            sbSql.append("         INNER JOIN denpyou_shubetsu_ichiran AS AD2 ON AD2.denpyou_kbn = AD1.denpyou_kbn");
            sbSql.append("         INNER JOIN denpyou_kian_himozuke AS AD3 ON AD3.denpyou_id = AD1.denpyou_id");
            sbSql.append("         WHERE AD2.yosan_shikkou_taishou = 'C' AND AD3.jisshi_kian_bangou IS NOT NULL");
            sbSql.append("        ) AS AD ON AD.jisshi_nendo = AC.jisshi_nendo");
            sbSql.append("               AND AD.jisshi_kian_bangou = AC.jisshi_kian_bangou");
            sbSql.append("        WHERE AB.yosan_shikkou_taishou = 'A'");
            sbSql.append("          AND AC.jisshi_kian_bangou IS NOT NULL");
            sbSql.append("          AND AD.denpyou_id IS NULL");
            // 1.AA+AB+AC で支出起案伝票で実施起案番号を持っている伝票のリスト
            // 2.AD1+AD2+AD3 で支出依頼伝票で実施起案番号と支出起案番号を持っている伝票のリスト
            // 3.(AA+AB+AC)と(AD1+AD2+AD3)をLEFT OUTER JOINの実施起案番号と支出起案番号で結合して支出依頼伝票を持たない伝票IDを返す
            sbSql.append("        UNION");
            sbSql.append("        SELECT AA.denpyou_id");
            sbSql.append("        FROM denpyou AS AA");
            sbSql.append("        INNER JOIN denpyou_shubetsu_ichiran AS AB ON AB.denpyou_kbn = AA.denpyou_kbn");
            sbSql.append("        INNER JOIN denpyou_kian_himozuke AS AC ON AC.denpyou_id = AA.denpyou_id");
            sbSql.append("        LEFT OUTER JOIN (");
            sbSql.append("         SELECT AD1.denpyou_id, AD2.yosan_shikkou_taishou, AD3.jisshi_nendo, AD3.jisshi_kian_bangou, AD3.shishutsu_nendo , AD3.shishutsu_kian_bangou");
            sbSql.append("         FROM denpyou AS AD1");
            sbSql.append("         INNER JOIN denpyou_shubetsu_ichiran AS AD2 ON AD2.denpyou_kbn = AD1.denpyou_kbn");
            sbSql.append("         INNER JOIN denpyou_kian_himozuke AS AD3 ON AD3.denpyou_id = AD1.denpyou_id");
            sbSql.append("         WHERE AD2.yosan_shikkou_taishou = 'C' AND AD3.jisshi_kian_bangou IS NOT NULL");
            sbSql.append("        ) AS AD ON AD.jisshi_nendo = AC.jisshi_nendo");
            sbSql.append("               AND AD.jisshi_kian_bangou = AC.jisshi_kian_bangou");
            sbSql.append("               AND AD.shishutsu_nendo = AC.shishutsu_nendo");
            sbSql.append("               AND AD.shishutsu_kian_bangou = AC.shishutsu_kian_bangou");
            sbSql.append("        WHERE AB.yosan_shikkou_taishou = 'B'");
            sbSql.append("          AND AC.shishutsu_kian_bangou IS NOT NULL");
            sbSql.append("          AND AD.denpyou_id IS NULL");
            sbSql.append("      )");
        }
        //予算執行伝票種別
        if(yosanshikkouShubetsu != null){
            //予算執行対象が1つ以上選択されている場合
            if(0 < yosanshikkouShubetsu.length){
                StringBuffer sbYosanShikkou = new StringBuffer();
                for(int i=0 ; i < yosanshikkouShubetsu.length ; i++){

                    switch(yosanshikkouShubetsu[i]){
                    case YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN :		//実施起案
                    case YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN :		//支出起案

                        if(0 == sbYosanShikkou.length()){
                            sbYosanShikkou.append("  AND (");
                        }else{
                            sbYosanShikkou.append("       OR");
                        }
                        sbYosanShikkou.append("           A.yosan_shikkou_taishou = ?");
                        params.add(yosanshikkouShubetsu[i]);

                        break;
                    default:
                        break;
                    }
                }
                if(0 != sbYosanShikkou.length()){
                    sbYosanShikkou.append(")");
                }
                sbSql.append(sbYosanShikkou.toString());
            }
        }

        //予算執行対象月
        if(!yosanCheckNengetsuFrom.isEmpty() || !yosanCheckNengetsuTo.isEmpty()) {
            sbSql.append("  AND B.yosan_check_nengetsu <> ''");
        }
        if(!yosanCheckNengetsuFrom.isEmpty()) {
            sbSql.append("  AND B.yosan_check_nengetsu >= ?");
            params.add(yosanCheckNengetsuFrom);
        }
        if(!yosanCheckNengetsuTo.isEmpty()) {
            sbSql.append("  AND B.yosan_check_nengetsu <= ?");
            params.add(yosanCheckNengetsuTo);
        }

        return sbSql.toString();
    }

    /**
     * 起案伝票情報リスト取得<br>
     * 起案一覧CSV出力用に指定された起案伝票の情報を取得する。<br>
     *
     * @param yosanShikkouTaishou 予算執行対象
     * @param jisshiNendo 実施起案年度
     * @param kianBangou 実施起案番号
     * @param denpyouId 伝票番号（実施起案：自身の伝票番号 支出起案：起案追加された伝票番号）
     * @param denpyouKbn 伝票区分
     * @param kihyouBiFrom 起票日From
     * @param kihyouBiTo 起票日To
     * @param shouninBiFrom 承認日From
     * @param shouninBiTo 承認日To
     * @param tekiyou 摘要
     * @param bumonCdFrom 部門コードFrom
     * @param bumonCdTo 部門コードTo
     * @param kanitodokeKenmei 簡易届件名
     * @return 実施起案番号リスト
     */
    public List<GMap> loadKianDenpyouList(
                                          String yosanShikkouTaishou
                                        , String jisshiNendo
                                        , String kianBangou
                                        , String denpyouId
                                        , String denpyouKbn
                                        , Date kihyouBiFrom
                                        , Date kihyouBiTo
                                        , Date shouninBiFrom
                                        , Date shouninBiTo
                                        , String tekiyou
                                        , String bumonCdFrom
                                        , String bumonCdTo
                                        , String kanitodokeKenmei
                                        ) {
        StringBuilder sbSql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        sbSql.append("WITH");
        // 伝票の最終申請日の枝番
        sbSql.append(" MAX_SHINSEIBI AS (SELECT denpyou_id, MAX(edano) AS edano FROM shounin_joukyou WHERE joukyou_cd = '1' GROUP BY denpyou_id)");
        // 伝票の最終承認日の枝番
        sbSql.append(" ,MAX_SHOUNINBI AS (SELECT denpyou_id, MAX(edano) AS edano FROM shounin_joukyou WHERE joukyou_cd = '4' GROUP BY denpyou_id)");
        // 伝票の申請日
        sbSql.append(" ,SHINSEIBI AS (");
        sbSql.append("  SELECT A.denpyou_id, A.edano, A.touroku_time");
        sbSql.append("  FROM shounin_joukyou AS A");
        sbSql.append("  INNER JOIN MAX_SHINSEIBI AS B ON B.denpyou_id = A.denpyou_id AND B.edano = A.edano");
        sbSql.append("  INNER JOIN denpyou AS C ON C.denpyou_id = A.denpyou_id");
        sbSql.append("  WHERE C.denpyou_joutai IN ('20', '30')");
        sbSql.append(" )");
        // 伝票の承認日
        sbSql.append(" ,SHOUNINBI AS (");
        sbSql.append("  SELECT A.denpyou_id, A.edano, A.touroku_time");
        sbSql.append("  FROM shounin_joukyou AS A");
        sbSql.append("  INNER JOIN MAX_SHOUNINBI AS B ON B.denpyou_id = A.denpyou_id AND B.edano = A.edano");
        sbSql.append("  INNER JOIN denpyou AS C ON C.denpyou_id = A.denpyou_id");
        sbSql.append("  WHERE C.denpyou_joutai IN ('20', '30')");
        sbSql.append(" )");
        // ユーザー定義届書伝票から件名を取得
        sbSql.append(" ,KANI_KENMEI AS (");
        sbSql.append("  SELECT A.denpyou_id, A.value1 AS kenmei");
        sbSql.append("  FROM kani_todoke AS A");
        sbSql.append("  INNER JOIN kani_todoke_koumoku AS B ON B.denpyou_kbn = A.denpyou_kbn AND B.version = A.version AND B.item_name = A.item_name");
        sbSql.append("  WHERE B.yosan_shikkou_koumoku_id = 'kenmei'");
        sbSql.append(" )");
        // 摘要
        sbSql.append(" ,TEKIYOU AS (");
        switch(denpyouKbn){
        case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN :		//経費精算
            sbSql.append("  SELECT A.denpyou_id, A.tekiyou");
            sbSql.append("  FROM (SELECT denpyou_id, tekiyou FROM keihiseisan_meisai WHERE denpyou_edano = 1) AS A");
            break;
        case DENPYOU_KBN.KARIBARAI_SHINSEI :		//仮払申請
            sbSql.append("  SELECT denpyou_id, tekiyou FROM karibarai");
            break;
        case DENPYOU_KBN.SEIKYUUSHO_BARAI :			//請求書払い申請
            sbSql.append("  SELECT A.denpyou_id, A.tekiyou");
            sbSql.append("  FROM (SELECT denpyou_id, tekiyou FROM seikyuushobarai_meisai WHERE denpyou_edano = 1) AS A");
            break;
        case DENPYOU_KBN.RYOHI_SEISAN :				//旅費精算
            sbSql.append("  SELECT denpyou_id, tekiyou FROM ryohiseisan");
            break;
        case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI :	//旅費仮払申請
            sbSql.append("  SELECT denpyou_id, tekiyou FROM ryohi_karibarai");
            break;
        case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN :				//海外旅費精算
            sbSql.append("  SELECT denpyou_id, kaigai_tekiyou AS tekiyou FROM kaigai_ryohiseisan");
            break;
        case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI :	//海外旅費仮払申請
            sbSql.append("  SELECT denpyou_id, tekiyou FROM kaigai_ryohi_karibarai");
            break;
        case DENPYOU_KBN.SIHARAIIRAI :			//支払依頼申請
            sbSql.append("  SELECT A.denpyou_id, A.tekiyou");
            sbSql.append("  FROM (SELECT denpyou_id, tekiyou FROM shiharai_irai_meisai WHERE denpyou_edano = 1) AS A");
            break;
        default:
            sbSql.append("  SELECT null::text AS denpyou_id, null::text AS tekiyou");
            break;
        }
        sbSql.append(" )");
        // SQL本体
        sbSql.append(" SELECT");
        sbSql.append("    A.denpyou_kbn, A.version");
        sbSql.append("   ,A.denpyou_shubetsu");
        sbSql.append("   ,G.name AS yosan_shikkou_taishou");
        sbSql.append("   ,B.denpyou_id");
        sbSql.append("   ,C.kian_denpyou");
        sbSql.append("   ,C.shishutsu_kian_bangou");
        sbSql.append("   ,D.kenmei");
        sbSql.append("   ,E.touroku_time AS kian_shinsei_bi");
        sbSql.append("   ,F.touroku_time AS kian_shounin_bi");
        sbSql.append("   ,H.tekiyou");
        sbSql.append("   ,C.kian_syuryo_bi");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn ");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id ");
        sbSql.append("  INNER JOIN naibu_cd_setting g ON G.naibu_cd_name  = 'yosan_shikkou_taishou' AND A.yosan_shikkou_taishou = G.naibu_cd");
        sbSql.append("  LEFT OUTER JOIN KANI_KENMEI AS D ON D.denpyou_id = B.denpyou_id");
        sbSql.append("  LEFT OUTER JOIN SHINSEIBI AS E ON E.denpyou_id = B.denpyou_id");
        sbSql.append("  LEFT OUTER JOIN SHOUNINBI AS F ON F.denpyou_id = B.denpyou_id");
        sbSql.append("  LEFT OUTER JOIN TEKIYOU AS H ON H.denpyou_id = B.denpyou_id");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = ?");
        params.add(yosanShikkouTaishou);
        sbSql.append("    AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append("    AND C.jisshi_nendo = ?");
        params.add(jisshiNendo);
        sbSql.append("    AND C.jisshi_kian_bangou = ?");
        params.add(kianBangou);
        sbSql.append("    AND C.denpyou_id = ?");
        params.add(denpyouId);

        //起票日付
        if(kihyouBiFrom != null){
            sbSql.append("  AND E.touroku_time >= ?");
            params.add(kihyouBiFrom);
        }
        if(kihyouBiTo != null){
            sbSql.append("  AND E.touroku_time < (CAST(? AS DATE) + 1)");
            params.add(kihyouBiTo);
        }
        //承認日付
        if(shouninBiFrom != null){
            sbSql.append("  AND F.touroku_time >= ?");
            params.add(shouninBiFrom);
        }
        if(shouninBiTo != null){
            sbSql.append("  AND F.touroku_time < (CAST(? AS DATE) + 1)");
            params.add(shouninBiTo);
        }

        boolean tekiyouInputted = (tekiyou != null && !tekiyou.isEmpty());
        boolean bumonCdFromInputted = (bumonCdFrom != null && !bumonCdFrom.isEmpty());
        boolean bumonCdToInputted = (bumonCdTo != null && !bumonCdTo.isEmpty());
        boolean kanitodokeKenmeiInputted = (kanitodokeKenmei != null && !kanitodokeKenmei.isEmpty());

        WorkflowEventControlLogic wfecLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
        if (wfecLogic.isKaniTodoke(denpyouKbn)){
            if(kanitodokeKenmeiInputted){
                sbSql.append(" AND unify_kana_width(D.kenmei) LIKE unify_kana_width(?)");
                params.add("%" + kanitodokeKenmei + "%");
            }
            if(tekiyouInputted){
                sbSql.append(" AND 1=0");
            }
            if (bumonCdFromInputted){
                if (bumonCdToInputted){
                    // From-Toで条件を生成する。
                    sbSql.append(" AND EXISTS");
                    sbSql.append("  (SELECT * FROM (SELECT DISTINCT denpyou_id, denpyou_kbn, version FROM kani_todoke WHERE denpyou_id = B.denpyou_id) h ");
                    sbSql.append("            INNER JOIN kani_todoke_meisai m ON m.denpyou_id = h.denpyou_id ");
                    sbSql.append("            INNER JOIN kani_todoke_koumoku k ON k.denpyou_kbn = h.denpyou_kbn AND k.version = h.version AND k.item_name = m.item_name");
                    sbSql.append(" WHERE k.area_kbn = 'meisai'");
                    sbSql.append(" AND (k.yosan_shikkou_koumoku_id = 'shishutsu_bumon' OR k.yosan_shikkou_koumoku_id = 'syuunyuu_bumon')");
                    sbSql.append(" AND m.value1 BETWEEN ? AND ?)");
                    params.add(bumonCdFrom);
                    params.add(bumonCdTo);
                }else{
                    // Fromだけで条件を生成する。
                    sbSql.append(" AND EXISTS");
                    sbSql.append("  (SELECT * FROM (SELECT DISTINCT denpyou_id, denpyou_kbn, version FROM kani_todoke WHERE denpyou_id = B.denpyou_id) h ");
                    sbSql.append("            INNER JOIN kani_todoke_meisai m ON m.denpyou_id = h.denpyou_id ");
                    sbSql.append("            INNER JOIN kani_todoke_koumoku k ON k.denpyou_kbn = h.denpyou_kbn AND k.version = h.version AND k.item_name = m.item_name");
                    sbSql.append(" AND k.area_kbn = 'meisai'");
                    sbSql.append(" AND (k.yosan_shikkou_koumoku_id = 'shishutsu_bumon' OR k.yosan_shikkou_koumoku_id = 'syuunyuu_bumon')");
                    sbSql.append(" AND m.value1 >= ?)");
                    params.add(bumonCdFrom);
                }
            }else{
                if (bumonCdToInputted){
                    // Toだけで条件を生成する。
                    sbSql.append(" AND EXISTS");
                    sbSql.append("  (SELECT * FROM (SELECT DISTINCT denpyou_id, denpyou_kbn, version FROM kani_todoke WHERE denpyou_id = B.denpyou_id) h ");
                    sbSql.append("            INNER JOIN kani_todoke_meisai m ON m.denpyou_id = h.denpyou_id ");
                    sbSql.append("            INNER JOIN kani_todoke_koumoku k ON k.denpyou_kbn = h.denpyou_kbn AND k.version = h.version AND k.item_name = m.item_name");
                    sbSql.append(" AND k.area_kbn = 'meisai'");
                    sbSql.append(" AND (k.yosan_shikkou_koumoku_id = 'shishutsu_bumon' OR k.yosan_shikkou_koumoku_id = 'syuunyuu_bumon')");
                    sbSql.append(" AND m.value1 <= ?)");
                    params.add(bumonCdTo);
                }
            }
        }else{
            if(kanitodokeKenmeiInputted){
                sbSql.append(" AND 1=0");
            }
            switch(denpyouKbn){
            case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN :		//経費精算
                if(tekiyouInputted){
                    sbSql.append(" AND EXISTS (SELECT * FROM keihiseisan_meisai m WHERE m.denpyou_id = B.denpyou_id AND unify_kana_width(m.tekiyou) LIKE unify_kana_width(?))");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM keihiseisan_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM keihiseisan_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM keihiseisan_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.KARIBARAI_SHINSEI :		//仮払申請
                if(tekiyouInputted){
                    sbSql.append(" AND unify_kana_width(H.tekiyou) LIKE unify_kana_width(?)");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.SEIKYUUSHO_BARAI :			//請求書払い申請
                if(tekiyouInputted){
                    sbSql.append(" AND EXISTS (SELECT * FROM seikyuushobarai_meisai m WHERE m.denpyou_id = B.denpyou_id AND unify_kana_width(m.tekiyou) LIKE unify_kana_width(?))");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM seikyuushobarai_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM seikyuushobarai_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM seikyuushobarai_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.RYOHI_SEISAN :				//旅費精算
                if(tekiyouInputted){
                    sbSql.append(" AND unify_kana_width(H.tekiyou) LIKE unify_kana_width(?)");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI :	//旅費仮払申請
                if(tekiyouInputted){
                    sbSql.append(" AND unify_kana_width(H.tekiyou) LIKE unify_kana_width(?)");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM ryohi_karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM ryohi_karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM ryohi_karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN :				//海外旅費精算
                if(tekiyouInputted){
                    sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND (unify_kana_width(m.tekiyou) LIKE unify_kana_width(?) OR unify_kana_width(m.kaigai_tekiyou) LIKE unify_kana_width(?)))");
                    params.add("%" + tekiyou + "%");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ? OR m.kaigai_kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ? OR m.kaigai_kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohiseisan m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ? OR m.kaigai_kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI :	//海外旅費仮払申請
                if(tekiyouInputted){
                    sbSql.append(" AND unify_kana_width(H.tekiyou) LIKE unify_kana_width(?)");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohi_karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohi_karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM kaigai_ryohi_karibarai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            case DENPYOU_KBN.SIHARAIIRAI :			//支払依頼申請
                if(tekiyouInputted){
                    sbSql.append(" AND EXISTS (SELECT * FROM shiharai_irai_meisai m WHERE m.denpyou_id = B.denpyou_id AND unify_kana_width(m.tekiyou) LIKE unify_kana_width(?))");
                    params.add("%" + tekiyou + "%");
                }
                if (bumonCdFromInputted){
                    if (bumonCdToInputted){
                        // From-Toで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM shiharai_irai_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd BETWEEN ? AND ?)");
                        params.add(bumonCdFrom);
                        params.add(bumonCdTo);
                    }else{
                        // Fromだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM shiharai_irai_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd >= ?)");
                        params.add(bumonCdFrom);
                    }
                }else{
                    if (bumonCdToInputted){
                        // Toだけで条件を生成する。
                        sbSql.append(" AND EXISTS (SELECT * FROM shiharai_irai_meisai m WHERE m.denpyou_id = B.denpyou_id AND m.kari_futan_bumon_cd <= ?)");
                        params.add(bumonCdTo);
                    }
                }
                break;
            default:
                break;
            }
        }

        //ソート条件
        sbSql.append("  ORDER BY C.ryakugou, C.kian_bangou, C.nendo, C.bumon_cd");

        return this.connection.load(sbSql.toString(), params.toArray());
    }

    /**
     * 支出依頼情報取得<br>
     * 起案一覧CSV出力用に指定された起案伝票に紐づく支出依頼の情報を取得する。<br>
     *
     * @param yosanShikkouTaishou 予算執行対象
     * @param jisshiNendo 実施起案年度
     * @param kianBangou 実施起案番号
     * @param denpyouId 起案の伝票番号
     * @return 支出依頼情報マップ
     */
    protected GMap getIraiDenpyouInfo(String yosanShikkouTaishou, String jisshiNendo, String kianBangou, String denpyouId) {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("WITH");
        // 伝票の最終申請日の枝番
        sbSql.append(" MAX_SHINSEIBI AS (SELECT denpyou_id, MAX(edano) AS edano FROM shounin_joukyou WHERE joukyou_cd = '1' GROUP BY denpyou_id)");
        // 伝票の最終承認日の枝番
        sbSql.append(" ,MAX_SHOUNINBI AS (SELECT denpyou_id, MAX(edano) AS edano FROM shounin_joukyou WHERE joukyou_cd = '4' GROUP BY denpyou_id)");
        // 伝票の申請日
        sbSql.append(" ,SHINSEIBI AS (");
        sbSql.append("  SELECT A.denpyou_id, A.edano, A.touroku_time");
        sbSql.append("  FROM shounin_joukyou AS A");
        sbSql.append("  INNER JOIN MAX_SHINSEIBI AS B ON B.denpyou_id = A.denpyou_id AND B.edano = A.edano");
        sbSql.append("  INNER JOIN denpyou AS C ON C.denpyou_id = A.denpyou_id");
        sbSql.append("  WHERE C.denpyou_joutai IN ('20', '30')");
        sbSql.append(" )");
        // 伝票の承認日
        sbSql.append(" ,SHOUNINBI AS (");
        sbSql.append("  SELECT A.denpyou_id, A.edano, A.touroku_time");
        sbSql.append("  FROM shounin_joukyou AS A");
        sbSql.append("  INNER JOIN MAX_SHOUNINBI AS B ON B.denpyou_id = A.denpyou_id AND B.edano = A.edano");
        sbSql.append("  INNER JOIN denpyou AS C ON C.denpyou_id = A.denpyou_id");
        sbSql.append("  WHERE C.denpyou_joutai IN ('20', '30')");
        sbSql.append(" )");
        // 起案中、申請中、申請済で登録日が最新の支出依頼伝票
        sbSql.append(" ,SHISHUTSU_IRAI AS (");
        sbSql.append("  SELECT");
        sbSql.append("    B.denpyou_kbn, B.denpyou_id, B.touroku_time");
        sbSql.append("   ,C.kian_denpyou, C.jisshi_kian_bangou, C.shishutsu_kian_bangou");
        sbSql.append("   ,D.touroku_time AS shinsei_bi");
        sbSql.append("   ,E.touroku_time AS shounin_bi");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn ");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id ");
        sbSql.append("  LEFT OUTER JOIN SHINSEIBI AS D ON D.denpyou_id = B.denpyou_id");
        sbSql.append("  LEFT OUTER JOIN SHOUNINBI AS E ON E.denpyou_id = B.denpyou_id");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = 'C'");
        sbSql.append("    AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append(" )");
        // 実施起案の伝票を起案追加して申請済の支出起案伝票
        sbSql.append(" ,SHISHUTSU_KIAN AS (");
        sbSql.append("  SELECT");
        sbSql.append("    B.denpyou_kbn, B.denpyou_id");
        sbSql.append("   ,C.kian_denpyou, C.jisshi_nendo, C.jisshi_kian_bangou");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = 'B'");
        sbSql.append("    AND B.denpyou_joutai IN ('30')");
        sbSql.append("    AND C.kian_denpyou = ':DENPYOU_ID'");
        sbSql.append(" )");
        // SQL本体
        sbSql.append(" SELECT");
        sbSql.append("    G.shinsei_bi AS irai_shinsei_bi");
        sbSql.append("   ,G.shounin_bi AS irai_shounin_bi");
        sbSql.append("   ,CASE WHEN H.irai_cnt IS NULL THEN 0 ELSE H.irai_cnt END AS irai_cnt");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn ");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id ");
        sbSql.append("  LEFT OUTER JOIN (");
        sbSql.append("   SELECT G1.jisshi_kian_bangou, G1.shinsei_bi, G1.shounin_bi");
        sbSql.append("   FROM SHISHUTSU_IRAI AS G1");
        sbSql.append("   INNER JOIN (");
        sbSql.append("    SELECT jisshi_kian_bangou, MAX(touroku_time) AS touroku_time");
        sbSql.append("    FROM SHISHUTSU_IRAI");
        sbSql.append("    WHERE kian_denpyou = ':DENPYOU_ID'");
        sbSql.append("       OR kian_denpyou IN (SELECT denpyou_id FROM SHISHUTSU_KIAN WHERE kian_denpyou = ':DENPYOU_ID')");
        sbSql.append("    GROUP BY jisshi_kian_bangou");
        sbSql.append("   ) AS G2 ON G2.jisshi_kian_bangou = G1.jisshi_kian_bangou AND G2.touroku_time = G1.touroku_time");
        sbSql.append("  ) AS G ON G.jisshi_kian_bangou = C.jisshi_kian_bangou");
        sbSql.append("  LEFT OUTER JOIN (");
        sbSql.append("   SELECT jisshi_kian_bangou, COUNT(1) AS irai_cnt");
        sbSql.append("   FROM SHISHUTSU_IRAI");
        sbSql.append("   WHERE kian_denpyou = ':DENPYOU_ID'");
        sbSql.append("      OR kian_denpyou IN (SELECT denpyou_id FROM SHISHUTSU_KIAN WHERE kian_denpyou = ':DENPYOU_ID')");
        sbSql.append("   GROUP BY jisshi_kian_bangou");
        sbSql.append("  ) AS H ON H.jisshi_kian_bangou = C.jisshi_kian_bangou");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = 'A'");
        sbSql.append("    AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append("    AND C.jisshi_nendo = ?");
        sbSql.append("    AND C.jisshi_kian_bangou = ?");

        String sql = sbSql.toString().replaceAll(":DENPYOU_ID", denpyouId);

        return this.connection.find(sql, jisshiNendo, kianBangou);
    }

    /**
     * 伝票金額取得<br>
     * 指定された伝票から金額を取得して、合計値を返却する。<br>
     *
     * @param denpyouKbn 伝票区分
     * @param denpyouId 伝票ID
     * @return 合計金額
     */
    public BigDecimal getKingaku(String denpyouKbn, String denpyouId){
        BigDecimal result = new BigDecimal(0);

        WorkflowEventControlLogic wfecLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
        YosanShikkouKanriCategoryLogic yskcLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);

        List<GMap> lstDenpyouInfo = null;
        if (wfecLogic.isKaniTodoke(denpyouKbn)){
            // ユーザー定義届書の場合
            String sqlStr = yskcLogic.getKanitodokeKingakuSql(denpyouKbn, denpyouId);
            if(sqlStr != null){
                List<String> lstParam = new ArrayList<String>();
                lstParam.add(denpyouId);
                lstDenpyouInfo = connection.load(sqlStr, lstParam.toArray());
            }
        }else{
            String sqlStr = null;
            switch (denpyouKbn){
            case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
                // 経費立替精算
                sqlStr = yskcLogic.getKeiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
                // 仮払申請
                sqlStr = yskcLogic.getKaribaraiSql();
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
                // 請求書払い申請
                sqlStr = yskcLogic.getSeikyuushobaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
                // 出張旅費精算（仮払精算）
                sqlStr = yskcLogic.getRyohiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId, denpyouId, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
                // 出張伺い申請（仮払申請）
                sqlStr = yskcLogic.getRyohiKaribaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId, denpyouId, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
                // 海外出張旅費精算（仮払精算）
                sqlStr = yskcLogic.getKaigaiRyohiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId, denpyouId, denpyouId, denpyouId, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
                // 海外出張伺い申請（仮払申請）
                sqlStr = yskcLogic.getKaigaiRyohiKaribaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId, denpyouId, denpyouId);
                break;
            case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
                // 支払依頼申請
                sqlStr = yskcLogic.getShiharaiIraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
                lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
                break;
            default:
            	lstDenpyouInfo = loadExtDenpyouInfo(denpyouKbn, denpyouId);
                break;
            }
        }


        // 金額リストがNULLの場合は0を返却し終了
        if(null == lstDenpyouInfo) return BigDecimal.ZERO;

        // 金額を合計する。
        for (GMap aMap : lstDenpyouInfo){
            result = result.add(new BigDecimal(aMap.get("kingaku").toString()));
        }

        return result;
    }

    /**
     * カスタマイズ用
     * @param denpyouKbn 伝票区分
     * @param denpyouId 伝票ID
     * @return 伝票情報リスト
     */
    public List<GMap> loadExtDenpyouInfo(String denpyouKbn, String denpyouId){
    	return null;
    }

    /**
     * 支出依頼伝票リスト取得<br>
     * 起案一覧CSV出力用に指定された実施起案番号を持つ支出依頼伝票の一覧を取得する。<br>
     *
     * @param yosanShikkouTaishou 予算執行対象
     * @param jisshiNendo 実施起案年度
     * @param kianBangou 実施起案番号
     * @param denpyouId 起案の伝票番号
     * @return 支出依頼伝票リスト
     */
    protected List<GMap> loadShishutsuIraiList(String yosanShikkouTaishou, String jisshiNendo, String kianBangou, String denpyouId) {
        StringBuilder sbSql = new StringBuilder();
        // 実施起案の伝票を起案追加して申請済の支出起案伝票
        sbSql.append("WITH");
        sbSql.append(" SHISHUTSU_KIAN AS (");
        sbSql.append("  SELECT");
        sbSql.append("    B.denpyou_kbn, B.denpyou_id");
        sbSql.append("   ,C.kian_denpyou, C.jisshi_nendo, C.jisshi_kian_bangou");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = 'B'");
        sbSql.append("    AND B.denpyou_joutai IN ('30')");
        sbSql.append("    AND C.kian_denpyou = ':DENPYOU_ID'");
        sbSql.append(" )");
        sbSql.append(" SELECT");
        sbSql.append("     A.denpyou_kbn, A.version");
        sbSql.append("    ,B.denpyou_id");
        sbSql.append("  FROM denpyou_shubetsu_ichiran AS A");
        sbSql.append("  INNER JOIN denpyou AS B ON B.denpyou_kbn = A.denpyou_kbn");
        sbSql.append("  INNER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = B.denpyou_id");
        sbSql.append("  WHERE A.yosan_shikkou_taishou = '" + EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI + "'");
        sbSql.append("    AND B.denpyou_joutai IN ('10', '20', '30')");
        sbSql.append("    AND C.jisshi_nendo = ?");
        sbSql.append("    AND C.jisshi_kian_bangou = ?");
        sbSql.append("    AND (C.kian_denpyou = ':DENPYOU_ID' OR");
        sbSql.append("         C.kian_denpyou IN (SELECT denpyou_id FROM SHISHUTSU_KIAN WHERE kian_denpyou = ':DENPYOU_ID'))");

        String sql = sbSql.toString().replaceAll(":DENPYOU_ID", denpyouId);

        return this.connection.load(sql, jisshiNendo, kianBangou);
    }

    /**
     * 日付型を文字列(yyyy/MM/dd)に変換。
     * @param d 変換前
     * @return 変換後
     */
    protected String formatDate(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof java.util.Date) {
            return new SimpleDateFormat("yyyy/MM/dd").format(d);
        } else {
            throw new InvalidParameterException("Date以外禁止.d:" + d);
        }
    }

    /**
     * BigDecimal型を文字列(カンマ区切り数値)に変換。
     * @param d 変換前
     * @return 変換後
     */
    protected String formatMoney(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            return new DecimalFormat("#,###").format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }
    
    /**
     * 複数NULLチェック 1つでもNULLでないものがあればtrue
     * @param params チェック対象項目
     * @return 変換後
     */
    protected static boolean isNull(Object... params) {
        for(Object param : params) {
            if(param != null) {
                return false;
            }
        }
        return true;
    }
}
