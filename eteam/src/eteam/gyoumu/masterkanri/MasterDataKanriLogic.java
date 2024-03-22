package eteam.gyoumu.masterkanri;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;

/**
 * マスターデータ管理機能Logic
 */
public class MasterDataKanriLogic extends EteamAbstractLogic {
	
	/**
	 * マスター管理版数へ登録します。
	 * @param master_id         マスターID
	 * @param delete_flg        削除フラグ
	 * @param fileName          ファイル名
	 * @param fileSize          ファイルサイズ
	 * @param contentType       コンテンツタイプ
	 * @param date              バイナリーデータ
	 * @param user_id           ユーザーID
	 * @return 結果
	 */
	public int insertMasterKanriHansuu(String master_id, String delete_flg, 
			String fileName, long fileSize, String contentType, byte[] date, String user_id) {
		final String sql = "INSERT INTO master_kanri_hansuu "
						+  "VALUES(?, "
						+  "  (SELECT COALESCE(MAX(version + 1), 1) FROM master_kanri_hansuu WHERE master_id = ?), "
						+  "  ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, master_id, master_id, 
										delete_flg, fileName, fileSize, contentType, date, user_id, user_id);
	}
	
	/**
	 * マスター管理版数の対象データに削除フラグを立てます。
	 * @param master_id マスターID
	 * @param user_id   ユーザーID
	 * @return 結果
	 */
	public int updateMasterKanriHansuu(String master_id, String user_id) {
		final String sql = "UPDATE master_kanri_hansuu "
						+  "SET    delete_flg  = '1', koushin_user_id = ? "
						+  "WHERE  master_id = ? AND delete_flg  = '0'";
		return connection.update(sql, user_id, master_id);
	}
	
	/**
	 * 対象のテーブルをロックします。
	 * @param tableName テーブル名
	 * @return 処理結果
	 */
	public int tableLock(String tableName) {
		return connection.update("LOCK " + tableName);
	}
	
	/**
	 * データを登録します。
	 * @param physicalTableName テーブル物理名
	 * @param recodeList        レコードリスト
	 */
	public void insertData(String physicalTableName, List<List<Object>> recodeList) {
		StringBuilder sqlBuilder;
		List<Object> dataRecode;
		for(List<Object> tmpDate : recodeList) {
			sqlBuilder = new StringBuilder("INSERT INTO ").append(physicalTableName).append(" VALUES(");
			
			dataRecode = new ArrayList<Object>();
			for(int i = 0; i < tmpDate.size(); i++) {
				if (tmpDate.get(i) != null && "CURRENT_TIMESTAMP".equals(tmpDate.get(i).toString().toUpperCase())) {
					sqlBuilder.append(tmpDate.get(i)).append(",");
				} else {
					sqlBuilder.append("?,");
					dataRecode.add(tmpDate.get(i));
				}
			}
			sqlBuilder.deleteCharAt(sqlBuilder.length() -1);
			sqlBuilder.append(")");
			connection.update(sqlBuilder.toString(), dataRecode.toArray());
		}
	}
	
	/**
	 * データを取得します。
	 * @param table_name        テーブル名
	 * @return 結果
	 */
	public List<GMap> selectData(String table_name) {
		
		//プライマリキーでソート
		PostgreSQLSystemCatalogsLogic dbLogic = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class, connection);
		
		MasterColumnsInfo masterColumnsInfo = dbLogic.getMasterColumnsInfo(table_name);
		List<String> pkList = masterColumnsInfo.getPkList();
		String sql = " SELECT * FROM " + table_name;
		if(pkList != null){
			if(!(pkList.isEmpty())){
				String addSql = " ORDER BY " + String.join(",",pkList);
				sql = sql + addSql;
			}
		}
		
		return connection.load(sql);
	}
	
	/**
	 * 対象のテーブルのデータを全て削除します。
	 * @param table_name テーブル名
	 * @return 結果
	 */
	public int delete(String table_name) {
		return connection.update("DELETE FROM " + table_name);
	}

	/**
	 * マスター更新後処理
	 * @param masterId マスターのテーブル名
	 */
	public void afterUpdate(String masterId) {
		//今の所なにもなし・・・パッケージでなにかする
	}
}