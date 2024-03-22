package eteam.database.dao;

import eteam.database.abstractdao.KoutsuuShudanMasterAbstractDao;
import eteam.database.dto.KoutsuuShudanMaster;

/**
 * 国内用交通手段マスターに対するデータ操作クラス
 */
public class KoutsuuShudanMasterDao extends KoutsuuShudanMasterAbstractDao {

	/**
	 * 国内用交通手段マスターから主キー指定でレコードを取得
	 * @param koutsuuShudan 交通手段
	 * @return 国内用交通手段マスターDTO
	 */
	public KoutsuuShudanMaster find(String koutsuuShudan) {
		final String sql = "SELECT * FROM koutsuu_shudan_master WHERE koutsuu_shudan = ?";
		return mapToDto(connection.find(sql, koutsuuShudan));
	}
}
