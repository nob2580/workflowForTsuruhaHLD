package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KiSyuukeiBumon;

/**
 * （期別）集計部門に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KiSyuukeiBumonAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KiSyuukeiBumon mapToDto(GMap map){
		return map == null ? null : new KiSyuukeiBumon(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KiSyuukeiBumon> mapToDto(List<GMap> mapList){
		List<KiSyuukeiBumon> dtoList = new ArrayList<KiSyuukeiBumon>();
		for (var map : mapList) {
			dtoList.add(new KiSyuukeiBumon(map));
		}
		return dtoList;
	}
	
	/**
	 * （期別）集計部門のレコード有無を判定
	 * @param kesn 内部決算期
	 * @param syuukeiBumonCd 集計部門コード
	 * @param futanBumonCd 負担部門コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(int kesn, String syuukeiBumonCd, String futanBumonCd) {
		return this.find(kesn, syuukeiBumonCd, futanBumonCd) == null ? false : true;
	}
	
	/**
	 * （期別）集計部門から主キー指定でレコードを取得
	 * @param kesn 内部決算期
	 * @param syuukeiBumonCd 集計部門コード
	 * @param futanBumonCd 負担部門コード
	 * @return （期別）集計部門DTO
	 */
	public KiSyuukeiBumon find(int kesn, String syuukeiBumonCd, String futanBumonCd) {
		final String sql = "SELECT * FROM ki_syuukei_bumon WHERE kesn = ? AND syuukei_bumon_cd = ? AND futan_bumon_cd = ?";
		return mapToDto(connection.find(sql, kesn, syuukeiBumonCd, futanBumonCd));
	}
	
	/**
	 * （期別）集計部門からレコードを全件取得 ※大量データ取得に注意
	 * @return List<（期別）集計部門DTO>
	 */
	public List<KiSyuukeiBumon> load() {
		final String sql = "SELECT * FROM ki_syuukei_bumon ORDER BY kesn, syuukei_bumon_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * （期別）集計部門から一部キー指定でレコードを取得
	 * @param kesn 内部決算期
	 * @return List<（期別）集計部門>DTO
	 */
	public List<KiSyuukeiBumon> load(int kesn) {
		final String sql = "SELECT * FROM ki_syuukei_bumon WHERE kesn = ? "
							+ "ORDER BY kesn, syuukei_bumon_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql, kesn));
	}
	
	/**
	 * （期別）集計部門から一部キー指定でレコードを取得
	 * @param kesn 内部決算期
	 * @param syuukeiBumonCd 集計部門コード
	 * @return List<（期別）集計部門>DTO
	 */
	public List<KiSyuukeiBumon> load(int kesn, String syuukeiBumonCd) {
		final String sql = "SELECT * FROM ki_syuukei_bumon WHERE kesn = ?  AND syuukei_bumon_cd = ? "
							+ "ORDER BY kesn, syuukei_bumon_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql, kesn, syuukeiBumonCd));
	}

	/**
	* （期別）集計部門登録
	* @param dto （期別）集計部門
	* @return 件数
	*/
	public int insert(
		KiSyuukeiBumon dto
	){
		final String sql =
				"INSERT INTO ki_syuukei_bumon "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.kesn, dto.syuukeiBumonCd, dto.futanBumonCd, dto.syuukeiBumonName, dto.futanBumonName
					);
	}

	/**
	* （期別）集計部門登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto （期別）集計部門
	* @return 件数
	*/
	public int upsert(
		KiSyuukeiBumon dto
		 ){
		final String sql =
				"INSERT INTO ki_syuukei_bumon "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT ki_syuukei_bumon_pkey "
			+ "DO UPDATE SET syuukei_bumon_name = ?, futan_bumon_name = ? "
			+ "";
			return connection.update(sql,
				dto.kesn, dto.syuukeiBumonCd, dto.futanBumonCd, dto.syuukeiBumonName, dto.futanBumonName
				, dto.syuukeiBumonName, dto.futanBumonName
				);
    }
	
	/**
	 * （期別）集計部門から主キー指定でレコードを削除
	 * @param kesn 内部決算期
	 * @param syuukeiBumonCd 集計部門コード
	 * @param futanBumonCd 負担部門コード
	 * @return 削除件数
	 */
	public int delete(int kesn, String syuukeiBumonCd, String futanBumonCd){
		final String sql = "DELETE FROM ki_syuukei_bumon WHERE kesn = ? AND syuukei_bumon_cd = ? AND futan_bumon_cd = ?";
		return connection.update(sql, kesn, syuukeiBumonCd, futanBumonCd);
	}
	
	/**
	 * （期別）集計部門から一部キー指定でレコードを削除
	 * @param kesn 内部決算期
	 * @return 削除件数
	 */
	public int delete(int kesn) {
		final String sql = "DELETE FROM ki_syuukei_bumon WHERE kesn = ? ";
		return connection.update(sql, kesn);
	}
	
	/**
	 * （期別）集計部門から一部キー指定でレコードを削除
	 * @param kesn 内部決算期
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 削除件数
	 */
	public int delete(int kesn, String syuukeiBumonCd) {
		final String sql = "DELETE FROM ki_syuukei_bumon WHERE kesn = ?  AND syuukei_bumon_cd = ? ";
		return connection.update(sql, kesn, syuukeiBumonCd);
	}
}