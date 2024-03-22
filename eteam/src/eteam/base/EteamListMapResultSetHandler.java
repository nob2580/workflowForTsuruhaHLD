package eteam.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * DBUtilsのSELECT結果ハンドラ
 */
public class EteamListMapResultSetHandler implements ResultSetHandler<List<GMap>> {

	/**
	 * 複数リストハンドル
	 * @param rs SELECT結果
	 * @return 結果リスト
	 */
	@Override
	public List<GMap> handle(ResultSet rs) throws SQLException {
		List<GMap> rows = new ArrayList<>(rs.getFetchSize());
		while (rs.next()) {
			var test = new EteamMapResultSetHandler();
			rows.add(test.handleRow(rs));
		}
		return rows;
	}
}
