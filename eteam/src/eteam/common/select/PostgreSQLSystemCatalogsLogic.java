package eteam.common.select;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.gyoumu.masterkanri.MasterColumnInfo;
import eteam.gyoumu.masterkanri.MasterColumnsInfo;

/**
 * ポスグレのシステム情報にアクセスするSelect文を集約したLogic
 */
public class PostgreSQLSystemCatalogsLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

	/**
	 * マスターの列情報を取得します
	 * @param masterId マスター名
	 * @return 結果
	 */
	public MasterColumnsInfo getMasterColumnsInfo(String masterId) {
		String schemaName = EteamCommon.getContextSchemaName();
		List<GMap> colList = selectColumnInfo(schemaName, masterId);
		List<String> pkList = selectPKColumn(schemaName, masterId);

		LinkedHashMap<String, MasterColumnInfo> columnMap = new LinkedHashMap<String, MasterColumnInfo>(colList.size());
		int index = 0;
		for (GMap data : colList) {
			MasterColumnInfo info = new MasterColumnInfo(masterId, index, data);
			columnMap.put(info.getName(), info);
			index++;
		}
		for (String pkName : pkList) {
			columnMap.get(pkName.toLowerCase()).setPk(true);
		}

		return new MasterColumnsInfo(masterId, pkList, columnMap);
	}

	/**
	 * テーブルの存在チェックをします。
	 * @param tableName テーブル名
	 * @return true:存在する。 false:存在しない
	 */
	public boolean tableSonzaiCheck(String tableName) {
		String sql = "SELECT relname FROM pg_class WHERE relkind = 'r' AND relname = ? ";
		return connection.find(sql, tableName) == null ? false : true;
	}
	
	/**
	 * カラム情報を取得します。
	 * @param schemaName スキーマ名
	 * @param table_name テーブル名
	 * @return リスト
	 */
	public List<GMap> selectColumnInfo(String schemaName, String table_name) {
		final String sql = "SELECT "
							+ "   nsp.oid         AS schema_id "
							+ "  ,atr.attname     AS column_name "
							+ "  ,des.description AS column_comment "
							+ "  ,typ.typname     AS column_type "
							+ "  ,atr.attnotnull  AS column_notnull "
							+ "  ,CASE typ.typname "
							+ "        WHEN 'varchar'   THEN atr.atttypmod-4 "
							+ "        WHEN 'bpchar'    THEN atr.atttypmod-4 "
							+ "        WHEN 'numeric'   THEN atr.atttypmod/65536 "
							+ "        WHEN 'date'      THEN null "
							+ "        WHEN 'timestamp' THEN null "
							+ "        ELSE null "
							+ "   END AS column_length "
							+ "  ,CASE typ.typname "
							+ "        WHEN 'numeric'   THEN (atr.atttypmod-4)%65536 "
							+ "        ELSE null "
							+ "   END AS column_decimal_length "
							+ "FROM "
							+ "             pg_catalog.pg_class       cls "
							+ "  INNER JOIN pg_catalog.pg_namespace   nsp ON cls.relnamespace = nsp.oid"
							+ "  INNER JOIN pg_catalog.pg_attribute   atr ON cls.oid = atr.attrelid AND atr.attnum > 0 "
							+ "  LEFT  JOIN pg_catalog.pg_description des ON cls.oid = des.objoid   AND atr.attnum = des.objsubid "
							+ "  INNER JOIN pg_catalog.pg_type        typ ON atr.atttypid = typ.oid "
							+ "WHERE "
							+ "      cls.relkind = 'r'"
							+ "  AND cls.relname = ? "
							+ "  AND des.description IS NOT NULL "
							+ "  AND nsp.nspname = ? "
							+ "ORDER BY  "
							+ "  cls.oid, atr.attnum ";
		return connection.load(sql, table_name.toLowerCase(), schemaName.toLowerCase());
	}
	
	/**
	 * テーブルのプライマリーキーのカラム名を取得します。
	 * @param schemaName スキーマ名
	 * @param table_name テーブル名
	 * @return リスト
	 */
	public List<String> selectPKColumn(String schemaName, String table_name) {
		final String sql = " SELECT "
							+ "   ccu.column_name AS pk_column_name "
							+ " FROM "
							+ "   information_schema.table_constraints tc "
							+ " INNER JOIN "
							+ "   information_schema.constraint_column_usage ccu ON "
							+ "       tc.table_catalog   = ccu.table_catalog "
							+ "   AND tc.table_schema    = ccu.table_schema "
							+ "   AND tc.table_name      = ccu.table_name "
							+ "   AND tc.constraint_name = ccu.constraint_name "
							+ " WHERE "
							+ "       tc.table_schema    = ? "
							+ "   AND tc.table_name      = ? "
							+ "   AND tc.constraint_type = 'PRIMARY KEY' ";
		List<GMap> mapList = connection.load(sql, schemaName, table_name.toLowerCase());
		List<String> resultList = new ArrayList<String>();
		for(GMap map : mapList) {
			resultList.add(map.get("pk_column_name").toString());
		}
		
		return resultList;
	}
	
	/**
	 * 全スキーマ名を取得する。
	 * @return リスト
	 */
	public List<String> loadAllSchemaName() {
		final String sql = " SELECT "
							+ "   nspname "
							+ " FROM "
							+ "   pg_catalog.pg_namespace "
							+ " WHERE "
							+ "       nspname NOT LIKE 'pg_%%' "
							+ "   AND nspname NOT LIKE 'information_%%' "
							+ "   AND nspname <> 'public' ";
		List<GMap> mapList = connection.load(sql);
		List<String> resultList = new ArrayList<String>();
		for(GMap map : mapList) {
			resultList.add(map.get("nspname").toString());
		}
		return resultList;
	}
	
}
