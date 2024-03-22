package eteam.base;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * DBUtilsのSELECT結果ハンドラ
 */
public class EteamMapResultSetHandler implements ResultSetHandler<GMap> {

	/**
	 * 単レコードハンドル
	 * @param rs SELECT結果
	 * @return 結果リスト
	 */
	@Override
	public GMap handle(ResultSet rs) throws SQLException {
		while (rs.next()) {
			return handleRow(rs);
		}
		return null;
	}
	
	/**
	 * 単レコードハンドル
	 * @param rs SELECT結果
	 * @return 結果マップ
	 * @exception SQLException SQLエラー
	 */
	public GMap handleRow(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		GMap result = new GMap(cols * 4 / 3);

		for (int i = 1; i <= cols; i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(i);
			}
			result.put(columnName, rs.getObject(i));
		}
		return result;
	}
}
