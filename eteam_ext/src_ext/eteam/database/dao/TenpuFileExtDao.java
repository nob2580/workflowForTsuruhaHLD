package eteam.database.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import eteam.base.GMap;

public class TenpuFileExtDao extends TenpuFileDao {
	/**
	 * 添付ファイルテーブルへの登録を行う。
	 * @param denpyouId 伝票ID
	 * @param uploadFile ファイルデータ
	 * @param uploadFileContentType コンテントタイプ
	 * @param uploadFileFileName ファイル名
	 * @param userId ユーザーID
	 */
	public void tenpuFileEntry(String denpyouId, File[] uploadFile, String[] uploadFileContentType, String[] uploadFileFileName, String userId) {
		
		
		if (uploadFile == null || uploadFile.length == 0)
		{
			urlDelete(denpyouId);
			urlInsert(denpyouId, userId);
			return;
		}
		//削除してから
		urlDelete(denpyouId);
		//登録
		final String sql = "INSERT INTO tenpu_file VALUES(?, (SELECT COALESCE(MAX(edano + 1), 1) FROM tenpu_file WHERE denpyou_id = ?), ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		for(int i = 0; i < uploadFile.length; i++) {
			long fileSize = uploadFile[i].length();
			byte[] data = new byte[(int) fileSize];
			try {
				FileInputStream inputStream = new FileInputStream(uploadFile[i]);
				inputStream.read(data);
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			connection.update(sql, denpyouId, denpyouId, uploadFileFileName[i], fileSize, uploadFileContentType[i], data, userId, userId);
		}
		
		//▼カスタマイズ　URL情報を追加で登録
		urlInsert(denpyouId, userId);
		//▲カスタマイズ
	}
	/**
	 * 添付ファイルのテーブルにURL情報を挿入
	 * @param denpyouId
	 * @param userId
	 */
	protected void urlInsert(String denpyouId, String userId) {
		
		//▼カスタマイズ　URL情報を追加で登録
		final String sql2 ="SELECT * FROM url_info WHERE denpyou_id = ? ORDER BY edano";
		List<GMap> result = connection.load(sql2, denpyouId);
		if(!result.isEmpty()) {
			for(GMap map : result) {
				final String sql3 = "INSERT INTO tenpu_file VALUES(?, (SELECT COALESCE(MAX(edano + 1), 1) FROM tenpu_file WHERE denpyou_id = ?), ?, 0, '', '', ?, current_timestamp, ?, current_timestamp)";
				connection.update(sql3, denpyouId, denpyouId, map.get("url"),  userId, userId);
				//▲カスタマイズ
				
			}
		}
	}
	/**
	 * 	添付ファイルテーブルからURL情報を削除
	 * @param denpyouId
	 */
	protected void urlDelete(String denpyouId) {
		//一旦削除
		final String sql1 = "DELETE  FROM tenpu_file WHERE file_size = 0 AND denpyou_id = ?";
		connection.update(sql1,denpyouId);
	}
	
}
