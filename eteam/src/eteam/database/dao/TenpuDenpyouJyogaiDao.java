package eteam.database.dao;

import eteam.database.abstractdao.TenpuDenpyouJyogaiAbstractDao;
import eteam.database.dto.TenpuDenpyouJyogai;

/**
 * 添付伝票除外対象テーブルに対するデータ操作クラス
 */
public class TenpuDenpyouJyogaiDao extends TenpuDenpyouJyogaiAbstractDao {

	/**
	* 添付伝票除外対象テーブルにデータ登録(重複時はスキップ)
	* @param dto 添付伝票除外対象テーブルdto
	* @return 件数
	*/
	public int upsert(
		TenpuDenpyouJyogai dto
		 ){
		final String sql =
				" INSERT INTO tenpu_denpyou_jyogai VALUES (?) "
		    + "  ON CONFLICT DO NOTHING";
			return connection.update(sql,dto.denpyouId);
    }
	
}
