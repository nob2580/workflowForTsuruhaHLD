package eteam.database.dao;

import java.sql.Timestamp;

import eteam.database.abstractdao.EbunshoFileAbstractDao;

/**
 * e文書ファイルに対するデータ操作クラス
 */
public class EbunshoFileDao extends EbunshoFileAbstractDao {
	
	/**
	 * e文書ファイル保持用テーブルebunsho_fileへの登録を行う。
	 * @param denpyouId 伝票ID
	 * @param edano 該当添付ファイルの枝番
	 * @param ebunshoNo e文書番号
	 * @param pdfFileData PDFファイルデータ
	 * @param denshitorihikiFlg 電子取引フラグ
	 * @param tsfuyoFlg タイムスタンプ付与フラグ
	 * @param loginUserId ユーザーID
	 * @param tourokuTime タイムスタンプ付与日(nullならタイムスタンプ未付与)
	 */
	public void insertEbunshoFile(String denpyouId, Integer edano, String ebunshoNo, byte[] pdfFileData, String denshitorihikiFlg, String tsfuyoFlg, String loginUserId, java.util.Date tourokuTime){
		String sql;
		if(tourokuTime != null){
			sql = "INSERT INTO ebunsho_file VALUES(?, ?, ?, ?, ?, ?, ?, ? )";
			connection.update(sql, denpyouId, edano, ebunshoNo, pdfFileData, denshitorihikiFlg, tsfuyoFlg, loginUserId, new Timestamp(tourokuTime.getTime()));
		}else{
			sql = "INSERT INTO ebunsho_file VALUES(?, ?, ?, ?, ?, ?, ?, null)";
			connection.update(sql, denpyouId, edano, ebunshoNo, pdfFileData, denshitorihikiFlg, tsfuyoFlg, loginUserId);
		}
	}
	
	/**
	 * e文書ファイルテーブルの指定したe文書番号データにタイムスタンプ付与済みPDFバイトデータ・登録日時を設定する。(タイムスタンプ付与確認用)
	 * @param ebunshoNo e文書番号
	 * @param pdfFileData PDFファイルデータ
	 * @param tourokuTime タイムスタンプ登録日時
	 */
	public void updateEbunshoTimestamp(String ebunshoNo, byte[] pdfFileData, java.util.Date tourokuTime) {
		final String sql =
					"UPDATE ebunsho_file SET "
				+ "  binary_data = ? , "
				+ "  touroku_time = ? "
				+ "WHERE "
				+ "  ebunsho_no = ? ";
		connection.update(sql, pdfFileData, new Timestamp(tourokuTime.getTime()), ebunshoNo);
	}
	
	/**
	 * e文書ファイルテーブルの指定したe文書番号データのタイムスタンプ付与フラグ・登録日付を変更。
	 * @param ebunshoNo e文書番号
	 * @param tsfuyoFlg タイムスタンプ付与フラグ
	 * @param tourokuTime 登録日付
	 */
	public void updateEbunshoTSfuyoFlg(String ebunshoNo, String tsfuyoFlg, java.util.Date tourokuTime) {
		final String sql =
					"UPDATE ebunsho_file SET "
				+ "  tsfuyo_flg = ? , "
				+ "  touroku_time = ? "
				+ "WHERE "
				+ "  ebunsho_no = ? ";
		connection.update(sql, tsfuyoFlg, new Timestamp(tourokuTime.getTime()), ebunshoNo);
	}
}
