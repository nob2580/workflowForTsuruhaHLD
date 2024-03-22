package eteam.database.dao;

import java.sql.Date;
import java.util.List;

import eteam.base.GMap;
import eteam.database.abstractdao.KiHeishuMasterKyotenAbstractDao;
import eteam.database.dto.KiHeishuMasterKyoten;

/**
 * （期別）幣種マスター(拠点)に対するデータ操作クラス
 */
public class KiHeishuMasterKyotenDao extends KiHeishuMasterKyotenAbstractDao {
	
	/**
	 * （期別）幣種マスター(拠点)からすべての幣種コードを取得する
	 * @return List<GMap>
	 */
	public List<GMap> loadDistinct() {
		final String sql = "SELECT DISTINCT heishu_cd, currency_unit FROM ki_heishu_master_kyoten ORDER BY heishu_cd";
		return connection.load(sql);
	}
	
	/**
	 * 基準日で有効な幣種コードをすべて取得
	 * @param kijunbi 基準日
	 * @return List<KiHeishuMasterKyoten>
	 *
	 */
	public List<KiHeishuMasterKyoten> loadYuukou(Date kijunbi) {
		final String sql = "SELECT * FROM ki_heishu_master_kyoten "
						  + " INNER JOIN ki_kesn USING(kesn) "
						  + " WHERE ? BETWEEN from_date AND to_date";
		return mapToDto(connection.load(sql, kijunbi));
	}
}
