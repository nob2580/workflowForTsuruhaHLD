package eteam.gyoumu.help;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 画像アップロードLogicクラス
 */
public class GazouUploadLogic extends EteamAbstractLogic {
	
	/**
	 * 画像ファイルテーブル検索処理
	 * @return 検索結果
	 */
	public List<GMap> selectGazouFile(){
		final String sql = "SELECT serial_no, file_name, touroku_time, koushin_time FROM gazou_file ORDER BY serial_no";
		
		return connection.load(sql);
	}
	
	/**
	 * 画像ファイルテーブル登録処理
	 * @param fileName ファイル名
	 * @param fileSize ファイルサイズ
	 * @param contentType コンテンツタイプ
	 * @param data データ
	 * @param loginUserId ユーザーID
	 * @return 登録件数
	 */
	public int insertGazouFile(String fileName, long fileSize, String contentType, byte[] data, String loginUserId) {
		final String sql = "INSERT INTO gazou_file VALUES"
				+ "((SELECT COALESCE(MAX(serial_no + 1), 1) FROM gazou_file), ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql, fileName, fileSize, contentType, data, loginUserId, loginUserId);
	}
	
	/**
	 * 画像ファイルテーブル差し替え処理
	 * @param fileName ファイル名
	 * @param fileSize ファイルサイズ
	 * @param contentType コンテンツタイプ
	 * @param data データ
	 * @param loginUserId ユーザーID
	 * @param serialNo シリアル番号
	 * @return 登録件数
	 */
	public int updateGazouFile(String fileName, long fileSize, String contentType, byte[] data, String loginUserId, int serialNo) {
		final String sql = "UPDATE gazou_file"
				+ " SET file_name = ?, "
				+ "     file_size = ?, "
				+ "     content_type = ?, "
				+ "     binary_data = ?, "
				+ "     koushin_user_id = ?, "
				+ "     koushin_time = current_timestamp "
				+ "WHERE serial_no = ?";
		
		return connection.update(sql, fileName, fileSize, contentType, data, loginUserId, serialNo);
	}
	
	/**
	 * ダウンロードファイル情報検索処理
	 * @param serialNo シリアル番号
	 * @return 検索結果(1件)
	 */
	public GMap selectDownloadFile(int serialNo){
		final String sql = "SELECT file_name, file_size, content_type, binary_data FROM gazou_file WHERE serial_no = ?";
		return connection.find(sql, serialNo);
	}
	
	/**
	 * 画像ファイル削除処理
	 * @param serialNo シリアル番号
	 * @return 削除件数
	 */
	public int deleteGazouFile(int serialNo){
		final String sql = "DELETE FROM gazou_file WHERE serial_no = ?";
		return connection.update(sql, serialNo);
	}
	

}
