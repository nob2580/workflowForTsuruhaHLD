package eteam.database.dao;

import java.util.List;

import eteam.database.abstractdao.HeishuMasterAbstractDao;
import eteam.database.dto.HeishuMaster;

/**
 * 幣種マスターに対するデータ操作クラス
 */
public class HeishuMasterDao extends HeishuMasterAbstractDao {

	/**
	 * 初期値使用可能なレコードを取得
	 * @return List<レートマスターDTO>
	 */
	public List<HeishuMaster> loadAvailabilityFlgOn() {
		final String sql = "SELECT hm.*, (CASE ra.availability_flg WHEN 1 THEN trunc((ra.rate / hm.conversion_unit), 5) ELSE NULL END) as rate "
						+ "   FROM heishu_master hm "
						+ "   LEFT OUTER JOIN rate_master ra ON hm.heishu_cd = ra.heishu_cd AND ra.start_date <= current_date"
						+ "   WHERE hm.availability_flg = '1' ORDER BY hm.display_order";
		return mapToDto(connection.load(sql));
	}}
