package eteam.common;

import java.sql.Date;
import java.text.SimpleDateFormat;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.GMap;
import eteam.base.exception.EteamSQLDuplicateException;

/**
 * 採番Logic
 */
public class SaibanLogic extends EteamAbstractLogic {

	/**
	 * インフォメーションIDを採番する。
	 * @return インフォメーションID "yyMMdd-XXXXXXX" (登録日-シーケンス値)
	 */
	public String saibanInfoId(){
		final String insertSql = "INSERT INTO info_id_saiban SELECT ?, 1 WHERE (SELECT COUNT(*) FROM info_id_saiban WHERE touroku_date = ?) = 0;";
		final String updateSql = "UPDATE info_id_saiban SET sequence_val = sequence_val + 1 WHERE touroku_date = ? RETURNING sequence_val;";

		//上位キー
		Date systemDate = new Date(System.currentTimeMillis());

		//シーケンス値を決定
		int sequenceVal = saiban(insertSql, new Object[]{systemDate, systemDate}, updateSql, new Object[]{systemDate});


		//"yyMMdd-XXXXXXX"に変換
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(systemDate) + "-" + String.format("%1$07d", sequenceVal);
	}

	/**
	 * 伝票IDを採番し返却します。
	 * @param denpyouKbn String 伝票区分
	 * @param currentConnection current connection
	 * @return 伝票ID "yymmdd-X###-#######" (登録日-伝票区分-シーケンス値)
	 */
	public String saibanDenpyouId(String denpyouKbn, EteamConnection currentConnection){
		final String insertSql = "INSERT INTO denpyou_id_saiban SELECT ?, ?, 1 WHERE (SELECT COUNT(*) FROM denpyou_id_saiban WHERE touroku_date = ?  AND denpyou_kbn = ?) = 0;";
		final String updateSql = "UPDATE denpyou_id_saiban SET sequence_val = sequence_val + 1 WHERE touroku_date = ? AND denpyou_kbn = ? RETURNING sequence_val";

		//上位キー
		Date systemDate = new Date(System.currentTimeMillis());

		//シーケンス値を取得
		int sequenceVal = saiban(insertSql, new Object[]{systemDate, denpyouKbn, systemDate, denpyouKbn}, updateSql, new Object[]{systemDate, denpyouKbn}, currentConnection);

		//"yyMMdd-X000-XXXXXXXに変換
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(systemDate) + "-" + denpyouKbn + "-" + String.format("%1$07d", sequenceVal);
	}

	/**
	 * 伝票番号を採番し返却します（コミットは現在のconnectionを使って外で実行）。
	 * @param currentConnection current connection
	 * @return 伝票番号
	 */
	public int saibanDenpyouSerialNo(EteamConnection currentConnection){
		final String updateSql = "UPDATE denpyou_serial_no_saiban "
								+ "SET sequence_val = (CASE WHEN sequence_val + 2 > max_value THEN min_value"
													+ "     ELSE sequence_val + 2 END )";
		final String selectSql = "SELECT sequence_val FROM denpyou_serial_no_saiban";

		currentConnection.update(updateSql);
		GMap saiban = currentConnection.find(selectSql);
		return (int)saiban.get("sequence_val");
	}

	/**
	 * 伝票番号を採番し返却します（コミットも実行します）。
	 * @return 伝票番号
	 */
	public int saibanDenpyouSerialNo(){
		int sequenceVal;

		try(EteamConnection saibanConnection = EteamConnection.connect()) {
			sequenceVal =this.saibanDenpyouSerialNo(saibanConnection);
			saibanConnection.commit();
		}

		return sequenceVal;
	}

	/**
	 * シーケンス値を取得する共通のロジック（コミットは現在のconnectionを使って外部で行う）。
	 * 1. 採番テーブルへinsert
	 * 2. insertに失敗したらrollbackして、採番テーブルをupdate
	 * 3. 最大値を取得
	 * @param insertSql シーケンス値1をINSERT
	 * @param insertParams INSERT PARAMS
	 * @param updateSql シーケンス値を+1するUPDATE
	 * @param updateParams UPDATE PARAMS
	 * @param currentConnection current connection
	 * @return シーケンス値
	 */
	protected int saiban(String insertSql, Object[] insertParams, String updateSql, Object[] updateParams, EteamConnection currentConnection) {
		try{
			// insertしてみる
			if (currentConnection.update(insertSql, insertParams) == 1) {
				// insert成功なので、値は必ず"1"
				return 1;
			}
		} catch(EteamSQLDuplicateException e){
			// insertに失敗
			currentConnection.rollback();
		}

		// insertに失敗したのでupdateし、シーケンス値を取得 */
		GMap infosaiban = currentConnection.find(updateSql, updateParams);
		return (int)infosaiban.get("sequence_val");
	}

	/**
	 * シーケンス値を取得する共通のロジック（コミットまで完遂する）。
	 * 1. 採番テーブルへinsert
	 * 2. insertに失敗したらrollbackして、採番テーブルをupdate
	 * 3. 最大値を取得
	 * @param insertSql シーケンス値1をINSERT
	 * @param insertParams INSERT PARAMS
	 * @param updateSql シーケンス値を+1するUPDATE
	 * @param updateParams UPDATE PARAMS
	 * @return シーケンス値
	 */
	protected int saiban(String insertSql, Object[] insertParams, String updateSql, Object[] updateParams) {
		EteamConnection saibanConnection = EteamConnection.connect();
		try{
			// insertしてみる
			if (saibanConnection.update(insertSql, insertParams) == 1) {
				// insert成功なので、値は必ず"1"
				saibanConnection.commit();
				return 1;
			}
		} catch(EteamSQLDuplicateException e){
			// insertに失敗
			saibanConnection.rollback();
		} finally {
			saibanConnection.close();
		}

		// insertに失敗したのでupdateし、シーケンス値を取得 */
		saibanConnection = EteamConnection.connect();
		try {
			GMap infosaiban = saibanConnection.find(updateSql, updateParams);
			saibanConnection.commit();
			return (int)infosaiban.get("sequence_val");
		} finally {
			saibanConnection.close();
		}
	}

}
