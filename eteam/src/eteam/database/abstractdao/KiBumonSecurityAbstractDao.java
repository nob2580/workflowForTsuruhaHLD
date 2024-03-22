package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KiBumonSecurity;

/**
 * （期別）部門セキュリティに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KiBumonSecurityAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KiBumonSecurity mapToDto(GMap map){
		return map == null ? null : new KiBumonSecurity(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KiBumonSecurity> mapToDto(List<GMap> mapList){
		List<KiBumonSecurity> dtoList = new ArrayList<KiBumonSecurity>();
		for (var map : mapList) {
			dtoList.add(new KiBumonSecurity(map));
		}
		return dtoList;
	}
	
	/**
	 * （期別）部門セキュリティのレコード有無を判定
	 * @param kesn 内部決算期
	 * @param sptn セキュリティパターン
	 * @param futanBumonCd 負担部門コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(int kesn, int sptn, String futanBumonCd) {
		return this.find(kesn, sptn, futanBumonCd) == null ? false : true;
	}
	
	/**
	 * （期別）部門セキュリティから主キー指定でレコードを取得
	 * @param kesn 内部決算期
	 * @param sptn セキュリティパターン
	 * @param futanBumonCd 負担部門コード
	 * @return （期別）部門セキュリティDTO
	 */
	public KiBumonSecurity find(int kesn, int sptn, String futanBumonCd) {
		final String sql = "SELECT * FROM ki_bumon_security WHERE kesn = ? AND sptn = ? AND futan_bumon_cd = ?";
		return mapToDto(connection.find(sql, kesn, sptn, futanBumonCd));
	}
	
	/**
	 * （期別）部門セキュリティからレコードを全件取得 ※大量データ取得に注意
	 * @return List<（期別）部門セキュリティDTO>
	 */
	public List<KiBumonSecurity> load() {
		final String sql = "SELECT * FROM ki_bumon_security ORDER BY kesn, sptn, futan_bumon_cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * （期別）部門セキュリティから一部キー指定でレコードを取得
	 * @param kesn 内部決算期
	 * @return List<（期別）部門セキュリティ>DTO
	 */
	public List<KiBumonSecurity> load(int kesn) {
		final String sql = "SELECT * FROM ki_bumon_security WHERE kesn = ? "
							+ "ORDER BY kesn, sptn, futan_bumon_cd";
		return mapToDto(connection.load(sql, kesn));
	}
	
	/**
	 * （期別）部門セキュリティから一部キー指定でレコードを取得
	 * @param kesn 内部決算期
	 * @param sptn セキュリティパターン
	 * @return List<（期別）部門セキュリティ>DTO
	 */
	public List<KiBumonSecurity> load(int kesn, int sptn) {
		final String sql = "SELECT * FROM ki_bumon_security WHERE kesn = ?  AND sptn = ? "
							+ "ORDER BY kesn, sptn, futan_bumon_cd";
		return mapToDto(connection.load(sql, kesn, sptn));
	}

	/**
	* （期別）部門セキュリティ登録
	* @param dto （期別）部門セキュリティ
	* @return 件数
	*/
	public int insert(
		KiBumonSecurity dto
	){
		final String sql =
				"INSERT INTO ki_bumon_security "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.kesn, dto.sptn, dto.futanBumonCd
					);
	}
	
	/**
	 * （期別）部門セキュリティから主キー指定でレコードを削除
	 * @param kesn 内部決算期
	 * @param sptn セキュリティパターン
	 * @param futanBumonCd 負担部門コード
	 * @return 削除件数
	 */
	public int delete(int kesn, int sptn, String futanBumonCd){
		final String sql = "DELETE FROM ki_bumon_security WHERE kesn = ? AND sptn = ? AND futan_bumon_cd = ?";
		return connection.update(sql, kesn, sptn, futanBumonCd);
	}
	
	/**
	 * （期別）部門セキュリティから一部キー指定でレコードを削除
	 * @param kesn 内部決算期
	 * @return 削除件数
	 */
	public int delete(int kesn) {
		final String sql = "DELETE FROM ki_bumon_security WHERE kesn = ? ";
		return connection.update(sql, kesn);
	}
	
	/**
	 * （期別）部門セキュリティから一部キー指定でレコードを削除
	 * @param kesn 内部決算期
	 * @param sptn セキュリティパターン
	 * @return 削除件数
	 */
	public int delete(int kesn, int sptn) {
		final String sql = "DELETE FROM ki_bumon_security WHERE kesn = ?  AND sptn = ? ";
		return connection.update(sql, kesn, sptn);
	}
}
