package eteam.gyoumu.masterkanri;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamConst.Encoding;

/**
 * マスター取込バッチ共通ロジック
 */
public class MasterTorikomiLogic extends EteamAbstractLogic {
	
	/** CSV作成用一時テーブルからの一括取得行サイズ */
	protected static final int LIST_GET_SIZE = 1000;
	
	/** byte・String変換用エンコード用文字セット */
	protected static final String ENCODE_CHARSET = Encoding.MS932;
	
	/**
	 * 各テーブルへデータを登録します。
	 * @param sql    登録SQL
	 * @param param  パラメータ
	 */
	public void insert(String sql, Object[] param) {
		connection.update(sql, param);
	}
	
	/**
	 * 各テーブルのデータを削除します。
	 * @param table_name テーブル名
	 */
	public void delete(String table_name) {
		connection.update("DELETE FROM " + table_name);
	}
	
	/**
	 * 各テーブルのデータを削除します。(TERM系)
	 * @param table_name テーブル名
	 * @param targetDateFrom 日付(FROM)
	 * @param targetDateTo 日付(TO)
	 */
	public void deleteTerm(String table_name, Date targetDateFrom, Date targetDateTo) {
		connection.update("DELETE FROM " + table_name + " WHERE dymd BETWEEN ? AND ?", targetDateFrom, targetDateTo);
	}
	
	/**
	 * マスター管理版数へファイル情報を更新します。
	 * @Deprecated バイナリーデータに巨大データが代入された際にメモリ不足が発生する可能性あり
	 * 
	 * @param master_id         マスターID
	 * @param fileName          ファイル名
	 * @param fileSize          ファイルサイズ
	 * @param contentType       コンテンツタイプ
	 * @param date              バイナリーデータ
	 * @return 結果
	 */
	@Deprecated
	public int updateMasterKanriHansuu(String master_id, String fileName, long fileSize, String contentType, byte[] date) {
		final String sql = "UPDATE master_kanri_hansuu "
						+  "SET    file_name = ?, file_size = ?, content_type = ?, binary_data = ?, "
						+ "        koushin_user_id = 'batch', koushin_time = current_timestamp "
						+  "WHERE  master_id = ? AND version = (SELECT MAX(version) FROM master_kanri_hansuu WHERE  master_id = ?)";
		return connection.update(sql, fileName, fileSize, contentType, date, master_id, master_id);
	}
	
	/**
	 * CSV作成用一時テーブルにデータを追加します。
	 * @param addStr 追加文字列データ
	 * @param intCnt 追加文字列行番号
	 * @throws IOException getBytes変換
	 */
	public void insertTempCsvTable(String addStr,int intCnt) throws IOException{
		
		//temp_csvmakeの指定された行番号にbyteデータを登録
		byte[] addByte = addStr.getBytes(ENCODE_CHARSET);
		final String sqlAdd = "INSERT INTO temp_csvmake VALUES (?, ?)";
		connection.update(sqlAdd, intCnt, addByte );
	}
	
	/**
	 * CSV作成用一時テーブルのデータを用いて、マスター管理版数のファイル情報を更新します。
	 * @param master_id         マスターID
	 * @param fileName          ファイル名
	 * @param contentType       コンテンツタイプ
	 * @throws IOException getBytes変換
	 * @return 結果
	 */
	public int updateMasterKanriCsv(String master_id, String fileName, String contentType) throws IOException{

		//該当マスターIDデータの最新バージョン取得
		final String sqlVer = "SELECT MAX(version) as maxver FROM master_kanri_hansuu WHERE master_id = ?";
		int maxver = (int)connection.find(sqlVer, master_id).get("maxver");
		
		//master_kanri_hansuu側のデータは空白にする
		final String sqlClr = "UPDATE master_kanri_hansuu SET binary_data = CAST('' AS BYTEA) "
							+ " WHERE master_id = ? AND version = ? ";
		connection.update(sqlClr, master_id, maxver);
		long cnt = 1;
		while(true){
			//LIST_GET_SIZE行分temp_csvmakeのデータ取得
			final String sqlGet = "SELECT row_num,data_rowbinary FROM temp_csvmake WHERE row_num >= ? AND row_num < ? ORDER BY row_num";
			List<GMap> tmpList;
			tmpList = connection.load(sqlGet, cnt, (cnt + LIST_GET_SIZE));
			cnt = cnt + LIST_GET_SIZE;
			
			//temp_csvmakeのデータがある限り該当データ文字列を
			//master_kanri_hansuuのdata_rowbinaryに加算する上書きを繰りかえす
			if (tmpList.isEmpty() == true)
			{
				break;
			}
			String tmpStr = "";
			for(GMap tmpMap : tmpList){
				//取得したbyte列を一旦String化して連結
				byte[] dataBinTmp = (byte[])tmpMap.get("data_rowbinary");
				String binaryStr = new String(dataBinTmp, ENCODE_CHARSET);
				tmpStr = tmpStr + binaryStr;
			}
			byte[] dataBin;
			dataBin = tmpStr.getBytes(ENCODE_CHARSET);
			String sqlUpd = "UPDATE master_kanri_hansuu SET binary_data = (t.binary_data || ?) "
							+ " FROM (SELECT binary_data FROM master_kanri_hansuu WHERE master_id = ? AND version = ?) t "
							+ " WHERE master_id = ? AND version = ? ";
			connection.update(sqlUpd, dataBin, master_id, maxver, master_id, maxver);
		}
		
		//データを全て入れ終わったらkoushin_time,koushin_user_idも追加して最後に更新
		//file_sizeは0固定でよい
		final String sqlEnd = "UPDATE master_kanri_hansuu "
				+  "SET    file_name = ?, file_size = 0, content_type = ?, "
				+ "        koushin_user_id = 'batch', koushin_time = current_timestamp "
				+ " WHERE master_id = ? AND version = ? ";
		connection.update(sqlEnd, fileName, contentType, master_id, maxver);
		
		return 1;
	}
	
	/**
	 * 仕訳パターンマスターのプロジェクトコードを更新します。
	 * @return 結果
	 */
	public int updateshiwakePForPjCd() {
		final String sql = "UPDATE shiwake_pattern_master "
						+  "SET    kari_project_cd = '', "
						+  "       kashi_project_cd1 = '', "
						+  "       kashi_project_cd2 = '', "
						+  "       kashi_project_cd3 = '', "
						+  "       kashi_project_cd4 = '', "
						+  "       kashi_project_cd5 = '', "
						+ "        koushin_user_id = 'batch', koushin_time = current_timestamp "
						+  "WHERE  delete_flg = '0' ";
		return connection.update(sql);
	}
	
	/**
	 * 仕訳パターンマスターのセグメントコードを更新します。
	 * @return 結果
	 */
	public int updateshiwakePForSgCd() {
		final String sql = "UPDATE shiwake_pattern_master "
						+  "SET    kari_segment_cd = '', "
						+  "       kashi_segment_cd1 = '', "
						+  "       kashi_segment_cd2 = '', "
						+  "       kashi_segment_cd3 = '', "
						+  "       kashi_segment_cd4 = '', "
						+  "       kashi_segment_cd5 = '', "
						+ "        koushin_user_id = 'batch', koushin_time = current_timestamp "
						+  "WHERE  delete_flg = '0' ";
		return connection.update(sql);
	}

	/**
	 * マスター更新後処理
	 */
	public void afterUpdate() {
	}
}
