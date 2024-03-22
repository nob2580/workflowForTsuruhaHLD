package eteam.database.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import eteam.database.abstractdao.TenpuFileAbstractDao;

/**
 * 添付ファイルに対するデータ操作クラス
 */
public class TenpuFileDao extends TenpuFileAbstractDao {
	
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
			return;
		}

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
	}
}
