package eteam.common.select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;

/**
 * ユーザー定義届書内のSelect文を集約したLogic
 */
public class KaniTodokeCategoryLogic extends EteamAbstractLogic {
	/** レイアウト取得時に固定枠不要 */
	public boolean layoutKoteiNashi = false;

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* ユーザー定義届書一覧(kani_todoke_ichiran) */
	/**
	 * 有効な伝票種別を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadKaniTodokeIchiran() {
		
		final String sql = "SELECT *"
				 + "  FROM kani_todoke_meta meta"
				 + " INNER JOIN kani_todoke_version ver"
				 + "    ON meta.denpyou_kbn = ver.denpyou_kbn"
				 + " INNER JOIN (SELECT denpyou_kbn"
				 + "                   ,MAX(version) AS version"
				 + "               FROM kani_todoke_version"
				 + "              GROUP BY denpyou_kbn"
				 + "            ) tmp"
				 + "    ON ver.denpyou_kbn = tmp.denpyou_kbn"
				 + "   AND ver.version     = tmp.version"
				 + " ORDER BY meta.denpyou_kbn";
		
		return connection.load(sql);
	}
	
	/** ユーザー定義届書レイアウトの取得
	 * @param areaKbn エリア区分
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @return ユーザー定義届書レイアウト
	 */
	public List<GMap> loadLayout(String areaKbn, String denpyouKbn, String version) {

		final String sql = " SELECT kani_todoke_koumoku.denpyou_kbn"
		 + "       ,kani_todoke_koumoku.area_kbn"
		 + "       ,kani_todoke_koumoku.hyouji_jun"
		 + "       ,kani_todoke_koumoku.buhin_type"
		 + "       ,kani_todoke_koumoku.item_name"
		 + "       ,meisai.label_name"
		 + "       ,meisai.hissu_flg"
		 + "       ,meisai.max_length"
		 + "       ,meisai.min_value"
		 + "       ,meisai.max_value"
		 + "       ,meisai.buhin_format"
		 + "       ,meisai.buhin_width"
		 + "       ,meisai.buhin_height"
		 + "       ,meisai.checkbox_label_name"
		 + "       ,meisai.master_kbn"
		 + "       ,kani_todoke_koumoku.yosan_shikkou_koumoku_id"
		 + "       ,meisai.decimal_point"
		 + "       ,meisai.kotei_hyouji"
		 + "   FROM kani_todoke_koumoku"
		 + "  INNER JOIN (SELECT denpyou_kbn"
		 + "                    ,version"
		 + "                    ,area_kbn"
		 + "                    ,item_name"
		 + "                    ,label_name"
		 + "                    ,hissu_flg"
		 + "                    ,max_length"
		 + "                    ,min_value"
		 + "                    ,max_value"
		 + "                    ,buhin_format"
		 + "                    ,buhin_width"
		 + "                    ,null AS buhin_height"
		 + "                    ,null AS checkbox_label_name"
		 + "                    ,null AS master_kbn"
		 + "                    ,decimal_point"
		 + "                    ,kotei_hyouji"
		 + "                FROM kani_todoke_text"
		 + "               UNION"
		 + "              SELECT denpyou_kbn"
		 + "                    ,version"
		 + "                    ,area_kbn"
		 + "                    ,item_name"
		 + "                    ,label_name"
		 + "                    ,hissu_flg"
		 + "                    ,max_length"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,buhin_width"
		 + "                    ,buhin_height"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,kotei_hyouji"
		 + "                FROM kani_todoke_textarea"
		 + "               UNION"
		 + "              SELECT denpyou_kbn"
		 + "                    ,version"
		 + "                    ,area_kbn"
		 + "                    ,item_name"
		 + "                    ,label_name"
		 + "                    ,hissu_flg"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,checkbox_label_name"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                FROM kani_todoke_checkbox"
		 + "               UNION"
		 + "              SELECT denpyou_kbn"
		 + "                    ,version"
		 + "                    ,area_kbn"
		 + "                    ,item_name"
		 + "                    ,label_name"
		 + "                    ,hissu_flg"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                FROM kani_todoke_list_oya"
		 + "               UNION"
		 + "              SELECT denpyou_kbn"
		 + "                    ,version"
		 + "                    ,area_kbn"
		 + "                    ,item_name"
		 + "                    ,label_name"
		 + "                    ,hissu_flg"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,null"
		 + "                    ,master_kbn"
		 + "                    ,null"
		 + "                    ,null"
		 + "                FROM kani_todoke_master"
		 + "             ) meisai"
		 + "     ON kani_todoke_koumoku.denpyou_kbn = meisai.denpyou_kbn"
		 + "    AND kani_todoke_koumoku.version     = meisai.version"
		 + "    AND kani_todoke_koumoku.area_kbn    = meisai.area_kbn"
		 + "    AND kani_todoke_koumoku.item_name   = meisai.item_name"
		 + "  WHERE kani_todoke_koumoku.area_kbn    = ?"
		 + "    AND kani_todoke_koumoku.denpyou_kbn = ?"
		 + "    AND kani_todoke_koumoku.version     = ?"
		 + "  ORDER BY kani_todoke_koumoku.hyouji_jun";
		List<GMap> list =  connection.load(sql, areaKbn, denpyouKbn, Integer.parseInt(version));
		
		//固定表示は表示対象外なので消す
		if(layoutKoteiNashi){
			Iterator<GMap> itr = list.iterator();
			while(itr.hasNext()){
				GMap mp = itr.next();
				if("1".equals(mp.get("kotei_hyouji")) ){
					itr.remove();
				}
			}
		}
		return list;
	}
	
	/** ユーザー定義届書データを取得する
	 * @param areaKbn エリア区分
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param denpyouId 伝票ID
	 * @return ユーザー定義届書データ
	 */
	public List<GMap> loadData(String areaKbn, String denpyouKbn, String version, String denpyouId) {

		StringBuilder sbSql = new StringBuilder();
		
		sbSql.append(" SELECT ");

		if (denpyouId.equals("")) {
			sbSql.append("        kani_todoke_koumoku.item_name");
			sbSql.append("       ,1 AS denpyou_edano");
			sbSql.append("       ,kani_todoke_koumoku.default_value1 AS value1");
			sbSql.append("       ,kani_todoke_koumoku.default_value2 AS value2");
		} else {
			sbSql.append("        denpyou.item_name");
			sbSql.append("       ,denpyou.denpyou_edano");
			sbSql.append("       ,denpyou.value1");
			sbSql.append("       ,denpyou.value2");
		}

		sbSql.append("   FROM kani_todoke_koumoku");
		sbSql.append("   LEFT OUTER JOIN (");
		
		if (areaKbn.equals(AREA_KBN.SHINSEI)) {
			sbSql.append("                    SELECT kani_todoke.denpyou_kbn");
			sbSql.append("                          ,kani_todoke.version");
			sbSql.append("                          ,1 AS denpyou_edano");
			sbSql.append("                          ,kani_todoke.item_name");
			sbSql.append("                          ,kani_todoke.value1");
			sbSql.append("                          ,kani_todoke.value2");
			sbSql.append("                      FROM kani_todoke");
			sbSql.append("                     WHERE kani_todoke.denpyou_id = ?");
		} else if(areaKbn.equals(AREA_KBN.MEISAI)) {
			sbSql.append("                    SELECT DISTINCT");
			sbSql.append("                           kani_todoke.denpyou_kbn");
			sbSql.append("                          ,kani_todoke.version");
			sbSql.append("                          ,kani_todoke_meisai.denpyou_edano");
			sbSql.append("                          ,kani_todoke_meisai.item_name");
			sbSql.append("                          ,kani_todoke_meisai.value1");
			sbSql.append("                          ,kani_todoke_meisai.value2");
			sbSql.append("                      FROM kani_todoke");
			sbSql.append("                     INNER JOIN kani_todoke_meisai");
			sbSql.append("                        ON kani_todoke.denpyou_id = kani_todoke_meisai.denpyou_id");
			sbSql.append("                     WHERE kani_todoke.denpyou_id = ?");
		}

		sbSql.append("                   ) denpyou");
		sbSql.append("     ON kani_todoke_koumoku.denpyou_kbn = denpyou.denpyou_kbn");
		sbSql.append("    AND kani_todoke_koumoku.version     = denpyou.version");
		sbSql.append("    AND kani_todoke_koumoku.item_name   = denpyou.item_name");
		sbSql.append("  WHERE kani_todoke_koumoku.area_kbn    = ?");
		sbSql.append("    AND kani_todoke_koumoku.denpyou_kbn = ?");
		sbSql.append("    AND kani_todoke_koumoku.version     = ?");
		sbSql.append("  ORDER BY denpyou_edano");
		sbSql.append("          ,kani_todoke_koumoku.hyouji_jun");
		
		return connection.load(sbSql.toString(), denpyouId, areaKbn, denpyouKbn, Integer.parseInt(version));
	}
	
	/** ユーザー定義届書データ(参照用)を取得する
	 * @param areaKbn エリア区分
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param denpyouId 伝票ID
	 * @param isBuhinMatch 一致判定フラグ（true：ラベル名・部品の両方で一致を判断）、false：ラベル名のみで一致を判断）
	 * @return ユーザー定義届書データ
	 */
	public List<GMap> loadSanshouData(String areaKbn, String denpyouKbn, String version, String denpyouId, boolean isBuhinMatch) {

		// 最大伝票枝番号
		int maxDenpyouEdano = this.getMaxDenpyouEdaNo(denpyouId, denpyouKbn, version, areaKbn);
		
		// 新レイアウト情報の取得
		final String sqlLayout = "SELECT item_name"
				+ "      ,label_name"
				+ "      ,buhin_type"
				+ "      ,buhin_format"
				+ "  FROM (SELECT L.item_name"
				+ "              ,R.label_name"
				+ "              ,L.buhin_type"
				+ "              ,R.buhin_format"
				+ "          FROM kani_todoke_koumoku L"
				+ "         INNER JOIN (SELECT denpyou_kbn, version, area_kbn, item_name, label_name, buhin_format FROM kani_todoke_text     UNION ALL"
				+ "                     SELECT denpyou_kbn, version, area_kbn, item_name, label_name, null         FROM kani_todoke_textarea UNION ALL"
				+ "                     SELECT denpyou_kbn, version, area_kbn, item_name, label_name, null         FROM kani_todoke_list_oya UNION ALL"
				+ "                     SELECT denpyou_kbn, version, area_kbn, item_name, label_name, null         FROM kani_todoke_checkbox UNION ALL"
				+ "                     SELECT denpyou_kbn, version, area_kbn, item_name, label_name, null         FROM kani_todoke_master)  R"
				+ "            ON L.denpyou_kbn = R.denpyou_kbn"
				+ "           AND L.version     = R.version"
				+ "           AND L.area_kbn    = R.area_kbn"
				+ "           AND L.item_name   = R.item_name"
				+ "         WHERE L.denpyou_kbn = ?"
				+ "           AND L.version     = ?"
				+ "           AND L.area_kbn    = ?"
				+ "       ) KANI"
				+ " ORDER BY item_name";
		
		List<GMap> listMapLayout = connection.load(sqlLayout, denpyouKbn, Integer.parseInt(version), areaKbn);
		
		// 旧レイアウト情報のデータ取得
		StringBuilder sbSqlData = new StringBuilder();
		
		sbSqlData.append("SELECT M.buhin_type");
		sbSqlData.append("      ,N.buhin_format");
		sbSqlData.append("      ,L.item_name");
		sbSqlData.append("      ,L.value1");
		sbSqlData.append("      ,L.value2");
		
		if (areaKbn.equals(AREA_KBN.SHINSEI)) {
			sbSqlData.append("  FROM kani_todoke L");
		} else if(areaKbn.equals(AREA_KBN.MEISAI)) {
			sbSqlData.append("  FROM (SELECT kani_todoke.denpyou_id");
			sbSqlData.append("              ,kani_todoke.denpyou_kbn");
			sbSqlData.append("              ,kani_todoke.version");
			sbSqlData.append("              ,kani_todoke_meisai.item_name");
			sbSqlData.append("              ,kani_todoke_meisai.denpyou_edano");
			sbSqlData.append("              ,kani_todoke_meisai.value1");
			sbSqlData.append("              ,kani_todoke_meisai.value2");
			sbSqlData.append("          FROM kani_todoke");
			sbSqlData.append("         INNER JOIN kani_todoke_meisai");
			sbSqlData.append("            ON kani_todoke.denpyou_id = kani_todoke_meisai.denpyou_id");
			sbSqlData.append("       ) L");
		}
		
		sbSqlData.append(" INNER JOIN kani_todoke_koumoku M");
		sbSqlData.append("    ON L.denpyou_kbn = M.denpyou_kbn");
		sbSqlData.append("   AND L.version     = M.version");
		sbSqlData.append("   AND L.item_name   = M.item_name");
		sbSqlData.append(" INNER JOIN (SELECT denpyou_kbn, version, item_name, label_name, buhin_format FROM kani_todoke_text     UNION ALL");
		sbSqlData.append("             SELECT denpyou_kbn, version, item_name, label_name, null         FROM kani_todoke_textarea UNION ALL");
		sbSqlData.append("             SELECT denpyou_kbn, version, item_name, label_name, null         FROM kani_todoke_list_oya UNION ALL");
		sbSqlData.append("             SELECT denpyou_kbn, version, item_name, label_name, null         FROM kani_todoke_checkbox UNION ALL");
		sbSqlData.append("             SELECT denpyou_kbn, version, item_name, label_name, null         FROM kani_todoke_master) N");
		sbSqlData.append("    ON L.denpyou_kbn   = N.denpyou_kbn");
		sbSqlData.append("   AND L.version       = N.version");
		sbSqlData.append("   AND L.item_name     = N.item_name");
		sbSqlData.append(" WHERE L.denpyou_id    = ?");
		
		if (areaKbn.equals(AREA_KBN.SHINSEI)) {
			sbSqlData.append("   AND 1               = ?");
		} else if(areaKbn.equals(AREA_KBN.MEISAI)) {
			sbSqlData.append("   AND L.denpyou_edano = ?");
		}
		
		sbSqlData.append("   AND N.label_name    = ?");
		sbSqlData.append(" ORDER BY M.hyouji_jun");
		
		
		// 新レイアウト＋旧データでユーザー定義届書データを作成する
		List<GMap> list = new ArrayList<GMap>();
		
		// 伝票枝番号単位にループ処理する
		for(Integer denpyou_edano = 1; denpyou_edano <= maxDenpyouEdano; denpyou_edano++) {
		
			// 新レイアウトでループ処理する
			for(GMap mapLayout : listMapLayout) {
				
				// 新レイアウト情報
				String item_name = mapLayout.get("item_name") == null ? "" : mapLayout.get("item_name").toString();
				String label_name = mapLayout.get("label_name") == null ? "" : mapLayout.get("label_name").toString();
				String buhin_type = mapLayout.get("buhin_type") == null ? "" : mapLayout.get("buhin_type").toString();
				String buhin_format = mapLayout.get("buhin_format") == null ? "" : mapLayout.get("buhin_format").toString();

				// 新レイアウトのラベル名と同一の旧レイアウト情報のデータを取得する
				List<GMap> listMapData = connection.load(sbSqlData.toString(), denpyouId, denpyou_edano, label_name);
				
				GMap map = new GMap();
				map.put("item_name", item_name);
				map.put("denpyou_edano", denpyou_edano);
				map.put("value1", null);
				map.put("value2", null);
				
				// 同一名のラベル名が複数ある場合、複数件のデータが取得される
				for(GMap mapData : listMapData) {
					
					// 旧レイアウト情報のデータ
					String data_buhin_type = mapData.get("buhin_type") == null ? "" : mapData.get("buhin_type").toString();
					String data_buhin_format = mapData.get("buhin_format") == null ? "" : mapData.get("buhin_format").toString();
					String data_value1 = mapData.get("value1") == null ? "" : mapData.get("value1").toString();
					String data_value2 = mapData.get("value2") == null ? "" : mapData.get("value2").toString();
					
					if(isBuhinMatch){
					// 部品タイプと部品形式が一致している場合、新レイアウトに旧データを適用する
					if (buhin_type.equals(data_buhin_type) && buhin_format.equals(data_buhin_format)) {
						map.put("value1", data_value1);
						map.put("value2", data_value2);
						break;
					}
					}else{
						// 部品タイプと部品形式の一致に係わらず、新レイアウトに旧データを適用する
						// セットする値は一覧に保存する値のみ
						String value = null;
						switch(data_buhin_type){
						case EteamConst.buhinType.CHECKBOX:
							value = ("1".equals(data_value1))? this.findCheckboxLabelName(denpyouId, areaKbn,  mapData.get("item_name").toString()) : "";
							break;
						case EteamConst.buhinType.MASTER:
							value = data_value2;
							break;
						case EteamConst.buhinType.PULLDOWN:
						case EteamConst.buhinType.RADIO:
							value = this.findListText(denpyouId, areaKbn, mapData.get("item_name").toString(), data_value1);
							break;
						case EteamConst.buhinType.TEXT:
						case EteamConst.buhinType.TEXTAREA:
							value = this.isKoteiHyouji(denpyouKbn, Integer.parseInt(version), areaKbn, item_name, data_buhin_type)? "" : data_value1;
							break;
						default:
							value = data_value1;
							break;
				}
						map.put("value", value);
				
					}
				}
				
				list.add(map);
			}
		}

		return list;
	}
	
	/**
	 * 固定表示するかどうかを判定
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 * @param BuhinType 部品タイプ
	 * @return true：固定表示する、false：固定表示しない
	 */
	protected boolean isKoteiHyouji(String denpyouKbn, int version, String areaKbn, String itemName, String BuhinType){
		String sql = "SELECT kotei_hyouji FROM TABLE_NAME WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ? ";
		if(EteamConst.buhinType.TEXT.equals(BuhinType)){
			sql = sql.replace("TABLE_NAME", "kani_todoke_text");
		}else{
			sql = sql.replace("TABLE_NAME", "kani_todoke_textarea");
		}
		GMap mp = connection.find(sql, denpyouKbn, version, areaKbn, itemName);
		String koreiHyouji = (null==mp)? "" : mp.get("kotei_hyouji").toString();
		return ("1".equals(koreiHyouji))? true : false;
	}
	
	/**
	 * 指定したコード値の名称を取得する
	 * @param denpyouId 伝票ID
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 * @param value コード値
	 * @return 名称
	 */
	protected String findListText(String denpyouId, String areaKbn, String itemName, String value){
		String denpyouKbn = denpyouId.substring(7, 11); // 伝票区分
		int thisVersion = this.findVersion(denpyouId); // 伝票バージョン
		String sql = "SELECT text FROM kani_todoke_list_ko WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ? AND value = ?";
		GMap mp = connection.find(sql, denpyouKbn, thisVersion, areaKbn, itemName, value.toString());
		return (null==mp)? "" : mp.get("text").toString();
	}
	
	/**
	 * 指定したチェックボックスのラベル名称を取得する
	 * @param denpyouId 伝票ID
	 * @param areaKbn エリア区分
	 * @param itemName 項目名
	 * @return チェックボックスラベル名称
	 */
	protected String findCheckboxLabelName(String denpyouId, String areaKbn, String itemName){
		String denpyouKbn = denpyouId.substring(7, 11); // 伝票区分
		int thisVersion = this.findVersion(denpyouId); // 伝票バージョン
		String sql = "SELECT checkbox_label_name FROM kani_todoke_checkbox WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ? AND item_name = ?";
		String checkLabelName = (String)connection.find(sql, denpyouKbn, thisVersion, areaKbn, itemName).get("checkbox_label_name");
		return ("".equals(checkLabelName))? "チェック有" : checkLabelName;
	}
	
	/**
	 * オプションリストの取得
	 * @param areaKbn エリア区分
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @return オプションリストの取得
	 */
	public List<GMap> loadOption(String areaKbn, String denpyouKbn, String version) {
		
		final String sql = " SELECT area_kbn"
		 + "       ,item_name"
		 + "       ,hyouji_jun"
		 + "       ,text"
		 + "       ,value"
		 + "       ,select_item"
		 + "   FROM kani_todoke_list_ko"
		 + "  WHERE denpyou_kbn = ?"
		 + "    AND version     = ?"
		 + "    AND area_kbn    = ?"
		 + "  ORDER BY item_name, hyouji_jun";
		
		return connection.load(sql, denpyouKbn, Integer.parseInt(version), areaKbn);
	}
	
	/**
	 * 伝票名と伝票内容を取得する
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @return 検索結果
	 */
	public GMap findKaniTodokeName(String denpyouKbn, String version) {
		
		final String sql = "SELECT denpyou_shubetsu, naiyou "
						+ "   FROM kani_todoke_version "
						+ "  WHERE denpyou_kbn = ? "
						+ "    AND version = ? ";
		
		return connection.find(sql, denpyouKbn, Integer.parseInt(version));
	}

	/**
	 * 最大伝票枝番号を取得する
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param areaKbn エリア区分
	 * @return 最大伝票枝番号
	 */
	public Integer getMaxDenpyouEdaNo(String denpyouId, String denpyouKbn, String version, String areaKbn) {
		
		// 最大伝票枝番号
		int maxDenpyouEdano = 1;
		
		if (areaKbn.equals(AREA_KBN.MEISAI)) {
			
			GMap mapMaxDenpyouEdano = connection.find("SELECT COALESCE(MAX(denpyou_edano), 0) AS denpyou_edano FROM kani_todoke_meisai WHERE denpyou_id  = ?", denpyouId);
			maxDenpyouEdano = Integer.parseInt(mapMaxDenpyouEdano.get("denpyou_edano").toString());
			
			if (maxDenpyouEdano == 0) {
			
				GMap mapMeisaiCount = connection.find("SELECT count(*) AS cnt FROM kani_todoke_koumoku WHERE denpyou_kbn = ? AND version = ? AND area_kbn = ?", denpyouKbn, Integer.parseInt(version), areaKbn);
				Integer meisaiCount = Integer.parseInt(mapMeisaiCount.get("cnt").toString());

				// レイアウトに明細の登録が必要な場合、最大枝番号を１に設定する
				if(meisaiCount > 0) {
					maxDenpyouEdano = 1;
				}
			}
		}
		
		return maxDenpyouEdano;
	}
	
	/**
	 * 新しい伝票区分を取得する
	 * @return 伝票区分
	 */
	public String getNewDenpyouKbn() {

		GMap map = connection.find("SELECT MAX(denpyou_kbn) AS denpyou_kbn FROM kani_todoke_meta");

		String denpyouKbn = "";
		
		if (map.get("denpyou_kbn") != null) {
			denpyouKbn = map.get("denpyou_kbn").toString();
		}

		int seq = 0;
		
		if (!denpyouKbn.isEmpty()) {
			seq = Integer.parseInt(denpyouKbn.replace("B", ""));
		}

		return "B" + valuePad(String.valueOf(seq + 1), 3);
	}

	/**
	 * 指定の桁数で0埋めをする
	 * 
	 * @param str 文字列
	 * @param size 最大桁数
	 * @return 変換後桁数
	 */
	protected String valuePad(String str, int size) {

		while (str.length() < size) {
			str = "0" + str;
		}

		if (str.length() > size) {
			throw new RuntimeException("伝票区分の採番ができませんでした。");
		}
		return str;
	}
	
	/**
	 * ユーザー定義届書コード設定取得
	 * kaniTodokeCdName(ユーザー定義届書コード名称)をキーに、kani_todoke_cd_setting(ユーザー定義届書コード設定)よりデータを取得します。
	 * @return 検索結果
	 */
	public List<GMap> loadKaniTodokeSelectItem() {
		return connection.load("SELECT DISTINCT select_item FROM kani_todoke_select_item ORDER BY select_item");
	}
	
	/** ユーザー定義届書コード設定オプション取得
	 * @return 検索結果
	 */
	public List<GMap> loadKaniTodokeSelectItemOption() {
		return connection.load("SELECT * FROM kani_todoke_select_item ORDER BY select_item, cd");
	}
	
	/**
	 * 新しいバージョンを取得する
	 * @param denpyouKbn 伝票区分
	 * @return バージョン
	 */
	public int findNewVersion(String denpyouKbn) {

		final String sql = "SELECT CASE WHEN max(version) IS NULL THEN 0 ELSE max(version) END AS version "
				+ "     FROM kani_todoke_version "
				+ "    WHERE denpyou_kbn = ? ";

		GMap map = connection.find(sql, denpyouKbn);

		return Integer.parseInt(map.get("version").toString()) + 1;
	}
	
	/**
	 * 最大バージョンを取得する
	 * @param denpyouKbn 伝票区分
	 * @return バージョン
	 */
	public int findMaxVersion(String denpyouKbn) {

		final String sql = "SELECT CASE WHEN max(version) IS NULL THEN 0 ELSE max(version) END AS version "
				+ "     FROM kani_todoke_version "
				+ "    WHERE denpyou_kbn = ? ";

		GMap map = connection.find(sql, denpyouKbn);

		return Integer.parseInt(map.get("version").toString());
	}
	
	/**
	 * バージョンを取得する
	 * @param denpyouId 伝票ID
	 * @return バージョン
	 */
	public int findVersion(String denpyouId) {
		final String sql = "SELECT DISTINCT version FROM kani_todoke WHERE denpyou_id = ? ";
		GMap map = connection.find(sql, denpyouId);
		return (int)map.get("version");
	}
	
	/** ユーザー定義届書伝票の件数を取得する
	 * @param denpyouKbn 伝票区分
	 * @return 件数
	 */
	public int findKaniTodokeDenpyouCnt(String denpyouKbn) {
		GMap map = connection.find("SELECT count(*) AS cnt FROM denpyou WHERE denpyou_kbn = ?", denpyouKbn);
		return Integer.parseInt(map.get("cnt").toString());
	}

	/**
	 * 予算執行項目入力部品件数取得<br>
	 * ユーザー定義届書レイアウトから指定された予算執行項目IDの入力部品の件数を取得する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param yosanShikkouKoumokuId 予算執行項目ID
	 * @return 件数
	 */
	public int findYosanShikkouKoumokuCnt(String denpyouKbn, String version, String yosanShikkouKoumokuId) {
		final String sql = "SELECT COUNT(1) AS cnt FROM kani_todoke_koumoku WHERE denpyou_kbn=? AND version=? AND yosan_shikkou_koumoku_id=?";
		GMap map = connection.find(sql, denpyouKbn, Integer.parseInt(version), yosanShikkouKoumokuId);
		return Integer.parseInt(map.get("cnt").toString());
	}

	/**
	 * レイアウト情報取得
	 * @param denpyouKbn 伝票区分
	 * @return [0]簡易届の項目名List<String> [1]簡易届の和名List<String> [3]簡易届のフォーマットList<String>
	 */
	public List<String>[] getLayoutInfo(String denpyouKbn){
		List<String> columnList = new ArrayList<>();
		List<String> wameiList = new ArrayList<>();
		List<String> typeList = new ArrayList<>();
		List<String> formatList = new ArrayList<>();
		List<String> decPointList = new ArrayList<>();

		//---------------------------------
		//出力項目(簡易届可変)を確定させる
		//---------------------------------		

		//該当伝票区分(簡易届)の和名と物理名を表示順に取る
		String version = Integer.toString(findMaxVersion(denpyouKbn));
		List<GMap> shinseiLayoutList = loadLayout(AREA_KBN.SHINSEI, denpyouKbn, version);
		List<GMap> meisaiLayoutList = loadLayout(AREA_KBN.MEISAI, denpyouKbn, version);
		
		for(GMap shinseiLayout : shinseiLayoutList){
			columnList.add((String)shinseiLayout.get("item_name"));
			wameiList.add((String)shinseiLayout.get("label_name"));
			typeList.add((String)shinseiLayout.get("buhin_type"));
			formatList.add((String)shinseiLayout.get("buhin_format"));
			decPointList.add((String)shinseiLayout.get("decimal_point"));
		}
		for(GMap meisaiLayout : meisaiLayoutList){
			columnList.add((String)meisaiLayout.get("item_name"));
			wameiList.add((String)meisaiLayout.get("label_name"));
			typeList.add((String)meisaiLayout.get("buhin_type"));
			formatList.add((String)meisaiLayout.get("buhin_format"));
			decPointList.add((String)meisaiLayout.get("decimal_point"));
		}
		return new List[]{columnList, wameiList, formatList, typeList, decPointList};
	}
}
