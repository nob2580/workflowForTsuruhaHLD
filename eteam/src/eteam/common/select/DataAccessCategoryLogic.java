package eteam.common.select;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamCommon;

/**
 * データアクセス
 *
 */
public class DataAccessCategoryLogic extends EteamAbstractLogic {

    
    /**
     * テーブルの物理名、和名の一覧を取得する
     * @param schemaName スキーマ名
     * @return 検索結果
     */
    public List<GMap> loadTableList(String schemaName) {
        
        final String sql = "   SELECT psut.relname as table_name, "
                        + "           pd.description as table_comment "
                        + "      FROM pg_stat_user_tables psut "
                        + "INNER JOIN pg_description pd "
                        + "        ON (psut.relid = pd.objoid) "
                        + "     WHERE psut.schemaname = ? "
                        + "       AND psut.relname NOT IN ('password') "
                        + "	      AND pd.objsubid = 0 "
                        + "  ORDER BY psut.relname asc";
        //DataAccessAction#formatCheckの不正入力チェックと同期
        return connection.load(sql, schemaName);
    }

    /**
     * テーブルの和名を取得する
     * @param tableName テーブル物理名
     * @param schemaName スキーマ名
     * @return 検索結果
     */
    public GMap findTableComment(String tableName, String schemaName) {
        
        final String sql = "    SELECT psut.relname as table_name, "
                        + "            pd.description as table_comment "
                        + "       FROM pg_stat_user_tables psut "
                        + " INNER JOIN pg_description pd "
                        + "         ON (psut.relid = pd.objoid) "
                        + "      WHERE psut.relname= ? "
                        + "        AND psut.schemaname = ? "
                        + "	       AND pd.objsubid = 0 ";
        return connection.find(sql, tableName, schemaName);
    }

    /**
     * テーブルに属するカラム名(物理),カラム名(和名)を取得する
     * @param tableName テーブル物理名
     * @param schemaName スキーマ名
     * @return 検索結果
     */
    public List<GMap> loadTableColumnName(String tableName, String schemaName) {
        return loadTableColumnName(tableName, schemaName, null);
    }
    
    /**
     * テーブルに属するカラム名(物理),カラム名(和名)を取得する
     * @param tableName テーブル物理名
     * @param schemaName スキーマ名
     * @return 検索結果
     */
    public List<String> loadTableColumnNameStr(String tableName, String schemaName) {
        List<GMap> list = loadTableColumnName(tableName, schemaName, null);
        List<String> ret = new ArrayList<>();
        for (GMap map : list) {
            ret.add((String)map.get("column_name"));
        }
        return ret;
    }
    
    /**
     * テーブルに属するカラム名(物理),カラム名(和名)を取得する
     * @param tableName テーブル物理名
     * @param schemaName スキーマ名
     * @param columnName カラム名
     * @return 検索結果
     */
    public List<GMap> loadTableColumnName(String tableName, String schemaName, String columnName) {
        
        StringBuilder sql = new StringBuilder();
        
        sql.append("   SELECT ns.nspname as schema_name, ");
        sql.append("          pgclass.relname as table_name, ");
        sql.append("          attr.attname as column_name, ");
        sql.append("          des.description as column_comment, ");
        sql.append("          CASE WHEN (cols.data_type = 'integer' ");
        sql.append("                 OR  cols.data_type = 'bigint' ");
        sql.append("                 OR  cols.data_type = 'numeric' ");
        sql.append("                 OR  cols.data_type = 'smallint') ");
        sql.append("               THEN 'number' ");
        sql.append("               WHEN (cols.data_type = 'date' ");
        sql.append("                 OR  cols.data_type = 'timestamp' ");
        sql.append("                 OR  cols.data_type = 'timestamp without time zone') ");
        sql.append("               THEN 'datepicker' ");
        sql.append("               WHEN (cols.data_type = 'bytea') ");
        sql.append("               THEN 'error' ");
        sql.append("               WHEN (cols.data_type = 'ARRAY') ");
        sql.append("               THEN (CASE WHEN (elet.data_type = 'integer' ");
        sql.append("                           OR  elet.data_type = 'bigint' ");
        sql.append("                           OR  elet.data_type = 'numeric' ");
        sql.append("                           OR  elet.data_type = 'smallint') ");
        sql.append("                         THEN 'number' ");
        sql.append("                         WHEN (elet.data_type = 'date' ");
        sql.append("                           OR  elet.data_type = 'timestamp' ");
        sql.append("                           OR  elet.data_type = 'timestamp without time zone') ");
        sql.append("                         THEN 'datepicker' ");
        sql.append("                         WHEN (elet.data_type = 'bytea') ");
        sql.append("                         THEN 'error' ");
        sql.append("                         ELSE 'string' ");
        sql.append("                         END)");
        sql.append("               ELSE 'string' ");
        sql.append("                END AS data_format, ");
        sql.append("          CASE WHEN (cols.data_type = 'ARRAY') ");
        sql.append("               THEN elet.data_type ");
        sql.append("               ELSE cols.data_type ");
        sql.append("                 END AS data_type, ");
        sql.append("          CASE WHEN (cols.data_type = 'ARRAY') ");
        sql.append("               THEN true ");
        sql.append("               ELSE false ");
        sql.append("                END AS is_array, ");
        sql.append("           cols.character_maximum_length, ");
        sql.append("           cols.numeric_precision ");
        sql.append("      FROM pg_catalog.pg_class pgclass ");
        sql.append("INNER JOIN pg_catalog.pg_namespace ns ");
        sql.append("        ON (pgclass.relnamespace = ns.oid) ");
        sql.append("INNER JOIN pg_catalog.pg_attribute attr ");
        sql.append("        ON (pgclass.oid = attr.attrelid AND attr.attnum > 0) ");
        sql.append(" LEFT JOIN pg_catalog.pg_description des ");
        sql.append("        ON (pgclass.oid = des.objoid AND attr.attnum = des.objsubid) ");
        sql.append(" LEFT JOIN information_schema.columns cols ");
        sql.append("        ON (ns.nspname = cols.table_schema AND pgclass.relname = cols.table_name AND attr.attname = cols.column_name) ");
        sql.append(" LEFT JOIN information_schema.element_types elet ");
        sql.append("        ON (cols.table_schema = elet.object_schema AND cols.table_name = elet.object_name AND cols.dtd_identifier = elet.collection_type_identifier AND elet.object_type = 'TABLE') ");
        sql.append("     WHERE ns.nspname = ? ");
        sql.append("       AND pgclass.relkind = 'r' "); // r = テーブル
        sql.append("       AND pgclass.relname = ? ");
        sql.append("       AND attr.attname not like '%pg.dropped%' "); // 削除カラムを含まない
        
        if (columnName == null) {
            sql.append("  ORDER BY ns.oid, pgclass.oid, attr.attnum ");
            return connection.load(sql.toString(), schemaName, tableName);
        } else {
            sql.append("       AND attr.attname = ? ");
            return connection.load(sql.toString(), schemaName, tableName, columnName);
        }
    }

    /**
     * プライマリキーが設定されているカラムを取得する
     * @param tableName テーブル物理名
     * @param schemaName スキーマ名
     * @return 検索結果
     */
    public List<GMap> loadPrymaryKeyColumn(String tableName, String schemaName) {
        
        final String sql = "    SELECT c.table_name, "
                        + "            c.column_name, "
                        + "            c.data_type, "
                        + "            c.ordinal_position "
                        + "       FROM information_schema.columns c "
                        + " INNER JOIN information_schema.constraint_column_usage ccu "
                        + "         ON (c.table_schema = ccu.table_schema AND c.table_name = ccu.table_name AND c.column_name = ccu.column_name) "
                        + " INNER JOIN information_schema.table_constraints tc "
                        + "         ON (tc.table_schema = c.table_schema AND tc.table_catalog = c.table_catalog "
                        + "        AND tc.table_name = c.table_name AND tc.constraint_name = ccu.constraint_name) "
                        + "      WHERE tc.constraint_type = 'PRIMARY KEY' "
                        + "        AND c.table_schema = ? "
                        + "        AND c.table_name = ? "
                        + "   ORDER BY c.table_name, "
                        + "            c.ordinal_position ";
        return connection.load(sql, schemaName, tableName);
    }
    
    /**
     * 指定のテーブルのデータを全て取得する
     * @param tableName テーブル名
     * @param columnData 取得カラム名文字列(カンマ区切り)
     * @param pkColumnData プライマリキーカラム名文字列(カンマ区切り)
     * @param pageNo ページ番号
     * @param pageMax １ページ最大表示件数
     * @param where 条件句
     * @param whereParam 条件値
     * @return 検索結果
     */
    public List<GMap> loadTableData(String tableName, String columnData, String pkColumnData, int pageNo, int pageMax, String where, Object[] whereParam) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT " + columnData);
        sql.append("  FROM " + tableName);
        
        if(where != null && where.length() > 0) {
            sql.append(" WHERE 1 = 1" + where);
        }
        
        if (pkColumnData != null && !"".equals(pkColumnData)) {
            sql.append(" ORDER BY " + pkColumnData);
        }
        
        /* 条件：ページ番号（取得件数）*/
        sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));
        
        if(where != null && where.length() > 0) {
            return connection.load(sql.toString(), whereParam);
        } else {
            return connection.load(sql.toString());
        }
    }
    
    /**
     * 指定のテーブルの件数を取得する。データがなければサイズ0
     * @param tableName テーブル名
     * @param where 条件句
     * @param whereParam 条件値
     * @return 検索結果件数
     */
    public long findDataCount(String tableName, String where, Object[] whereParam) {
        
        GMap datacount = new GMap();
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(*) AS datacount FROM " + tableName);
        
        if(where != null && where.length() > 0) {
            sql.append(" WHERE 1 = 1" + where);
            datacount = connection.find(sql.toString(), whereParam);
        } else {
            datacount = connection.find(sql.toString());
        }

        return (long)datacount.get("datacount");
        
    }
}


