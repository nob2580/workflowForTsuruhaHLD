package eteam.database.abstractdao;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.InvoiceStart;

/**
 * インボイス開始に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class InvoiceStartAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO invoice_start (invoice_flg, touroku_user_id, touroku_time) VALUES "
			+ "('1', ?, current_timestamp)";

	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected InvoiceStart mapToDto(GMap map) {
		return map == null ? null : new InvoiceStart(map);
	}

	/**
	 * インボイス開始のレコード有無を判定
	 * @return true:exist false:not exist
	 */
	public boolean exists() {
		return this.find() != null;
	}

	/**
	 * インボイス開始から主キー指定でレコードを取得
	 * @return インボイス開始DTO
	 */
	public InvoiceStart find() {
		final String sql = "SELECT * FROM invoice_start";
		return mapToDto(connection.find(sql));
	}

	/**
	 * インボイス開始登録
	 * @param userId ユーザーid
	 * @return 件数
	 */
	public int insert(String userId) {
		return connection.update(this.insertSql, userId);
	}

	/**
	 * インボイス開始更新
	 * @param userId ユーザーid
	 * @return 件数
	 */
	public int update(String userId) {
		final String sql = "UPDATE invoice_start SET invoice_flg = '1', touroku_user_id = ?, touroku_time = current_timestamp";
		return connection.update(sql, userId);
	}
	
	/**
	 * インボイスが開始されているか判定。必要な時に毎回nullチェックするのも面倒なのでdaoに埋め込んでしまう。
	 * @return インボイスが開始ならtrue
	 */
	public boolean isInvoiceStarted()
	{
		var dto = this.find();
		return dto != null && dto.invoiceFlg.equals("1");
	}
	
	/* 20230331
	 * インボイス解除(中止)は社内ツール的な別オブジェクトでの処理想定のためdeleteメソッドはなし*/
}