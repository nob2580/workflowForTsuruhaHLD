package eteam.database.abstractdao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ShukujitsuMaster;

/**
 * 祝日マスターに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ShukujitsuMasterAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ShukujitsuMaster mapToDto(GMap map){
		return map == null ? null : new ShukujitsuMaster(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ShukujitsuMaster> mapToDto(List<GMap> mapList){
		List<ShukujitsuMaster> dtoList = new ArrayList<ShukujitsuMaster>();
		for (var map : mapList) {
			dtoList.add(new ShukujitsuMaster(map));
		}
		return dtoList;
	}
	
	/**
	 * 祝日マスターのレコード有無を判定
	 * @param shukujitsu 祝日
	 * @return true:exist false:not exist
	 */
	public boolean exists(Date shukujitsu) {
		return this.find(shukujitsu) == null ? false : true;
	}
	
	/**
	 * 祝日マスターから主キー指定でレコードを取得
	 * @param shukujitsu 祝日
	 * @return 祝日マスターDTO
	 */
	public ShukujitsuMaster find(Date shukujitsu) {
		final String sql = "SELECT * FROM shukujitsu_master WHERE shukujitsu = ?";
		return mapToDto(connection.find(sql, shukujitsu));
	}
	
	/**
	 * 祝日マスターからレコードを全件取得 ※大量データ取得に注意
	 * @return List<祝日マスターDTO>
	 */
	public List<ShukujitsuMaster> load() {
		final String sql = "SELECT * FROM shukujitsu_master ORDER BY shukujitsu";
		return mapToDto(connection.load(sql));
	}

	/**
	* 祝日マスター登録
	* @param dto 祝日マスター
	* @return 件数
	*/
	public int insert(
		ShukujitsuMaster dto
	){
		final String sql =
				"INSERT INTO shukujitsu_master "
			+ "VALUES(?, ? "
			+ ")";
			return connection.update(sql,
					dto.shukujitsu, dto.shukujitsuName
					);
	}

	/**
	* 祝日マスター登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 祝日マスター
	* @return 件数
	*/
	public int upsert(
		ShukujitsuMaster dto
		 ){
		final String sql =
				"INSERT INTO shukujitsu_master "
			+ "VALUES(?, ? "
			+ ") ON CONFLICT ON CONSTRAINT shukujitsu_master_pkey "
			+ "DO UPDATE SET shukujitsu_name = ? "
			+ "";
			return connection.update(sql,
				dto.shukujitsu, dto.shukujitsuName
				, dto.shukujitsuName
				);
    }
	
	/**
	 * 祝日マスターから主キー指定でレコードを削除
	 * @param shukujitsu 祝日
	 * @return 削除件数
	 */
	public int delete(Date shukujitsu){
		final String sql = "DELETE FROM shukujitsu_master WHERE shukujitsu = ?";
		return connection.update(sql, shukujitsu);
	}
}