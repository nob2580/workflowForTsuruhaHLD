package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.TorihikisakiHojo;

/**
 * 取引先補助に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class TorihikisakiHojoAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected TorihikisakiHojo mapToDto(GMap map){
		return map == null ? null : new TorihikisakiHojo(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<TorihikisakiHojo> mapToDto(List<GMap> mapList){
		List<TorihikisakiHojo> dtoList = new ArrayList<TorihikisakiHojo>();
		for (var map : mapList) {
			dtoList.add(new TorihikisakiHojo(map));
		}
		return dtoList;
	}
	
	/**
	 * 取引先補助のレコード有無を判定
	 * @param torihikisakiCd 取引先コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String torihikisakiCd) {
		return this.find(torihikisakiCd) == null ? false : true;
	}
	
	/**
	 * 取引先補助から主キー指定でレコードを取得
	 * @param torihikisakiCd 取引先コード
	 * @return 取引先補助DTO
	 */
	public TorihikisakiHojo find(String torihikisakiCd) {
		final String sql = "SELECT * FROM torihikisaki_hojo WHERE torihikisaki_cd = ?";
		return mapToDto(connection.find(sql, torihikisakiCd));
	}
	
	/**
	 * 取引先補助からレコードを全件取得 ※大量データ取得に注意
	 * @return List<取引先補助DTO>
	 */
	public List<TorihikisakiHojo> load() {
		final String sql = "SELECT * FROM torihikisaki_hojo ORDER BY torihikisaki_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* 取引先補助登録
	* @param dto 取引先補助
	* @return 件数
	*/
	public int insert(
		TorihikisakiHojo dto
	){
		final String sql =
				"INSERT INTO torihikisaki_hojo "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.torihikisakiCd, dto.dm1, dto.dm2, dto.dm3, dto.stflg
					);
	}

	/**
	* 取引先補助登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 取引先補助
	* @return 件数
	*/
	public int upsert(
		TorihikisakiHojo dto
		 ){
		final String sql =
				"INSERT INTO torihikisaki_hojo "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT torihikisaki_hojo_pkey "
			+ "DO UPDATE SET dm1 = ?, dm2 = ?, dm3 = ?, stflg = ? "
			+ "";
			return connection.update(sql,
				dto.torihikisakiCd, dto.dm1, dto.dm2, dto.dm3, dto.stflg
				, dto.dm1, dto.dm2, dto.dm3, dto.stflg
				);
    }
	
	/**
	 * 取引先補助から主キー指定でレコードを削除
	 * @param torihikisakiCd 取引先コード
	 * @return 削除件数
	 */
	public int delete(String torihikisakiCd){
		final String sql = "DELETE FROM torihikisaki_hojo WHERE torihikisaki_cd = ?";
		return connection.update(sql, torihikisakiCd);
	}
}
