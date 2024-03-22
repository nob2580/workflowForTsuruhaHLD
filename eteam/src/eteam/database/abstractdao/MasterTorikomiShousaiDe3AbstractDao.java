package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.MasterTorikomiShousaiDe3;

/**
 * マスター取込詳細(de3)に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class MasterTorikomiShousaiDe3AbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected MasterTorikomiShousaiDe3 mapToDto(GMap map){
		return map == null ? null : new MasterTorikomiShousaiDe3(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<MasterTorikomiShousaiDe3> mapToDto(List<GMap> mapList){
		List<MasterTorikomiShousaiDe3> dtoList = new ArrayList<MasterTorikomiShousaiDe3>();
		for (var map : mapList) {
			dtoList.add(new MasterTorikomiShousaiDe3(map));
		}
		return dtoList;
	}
	
	/**
	 * マスター取込詳細(de3)のレコード有無を判定
	 * @param masterId マスターID
	 * @param etColumnId eTeamカラムID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String masterId, String etColumnId) {
		return this.find(masterId, etColumnId) == null ? false : true;
	}
	
	/**
	 * マスター取込詳細(de3)から主キー指定でレコードを取得
	 * @param masterId マスターID
	 * @param etColumnId eTeamカラムID
	 * @return マスター取込詳細(de3)DTO
	 */
	public MasterTorikomiShousaiDe3 find(String masterId, String etColumnId) {
		final String sql = "SELECT * FROM master_torikomi_shousai_de3 WHERE master_id = ? AND et_column_id = ?";
		return mapToDto(connection.find(sql, masterId, etColumnId));
	}
	
	/**
	 * マスター取込詳細(de3)からレコードを全件取得 ※大量データ取得に注意
	 * @return List<マスター取込詳細(de3)DTO>
	 */
	public List<MasterTorikomiShousaiDe3> load() {
		final String sql = "SELECT * FROM master_torikomi_shousai_de3 ORDER BY master_id, et_column_id";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * マスター取込詳細(de3)から一部キー指定でレコードを取得
	 * @param masterId マスターID
	 * @return List<マスター取込詳細(de3)>DTO
	 */
	public List<MasterTorikomiShousaiDe3> load(String masterId) {
		final String sql = "SELECT * FROM master_torikomi_shousai_de3 WHERE master_id = ? "
							+ "ORDER BY master_id, et_column_id";
		return mapToDto(connection.load(sql, masterId));
	}

	/**
	* マスター取込詳細(de3)登録
	* @param dto マスター取込詳細(de3)
	* @return 件数
	*/
	public int insert(
		MasterTorikomiShousaiDe3 dto
	){
		final String sql =
				"INSERT INTO master_torikomi_shousai_de3 "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.masterId, dto.etColumnId, dto.etColumnName, dto.etDataType, dto.opColumeId, dto.opColumnName, dto.opDataType, dto.entryOrder, dto.pkFlg
					);
	}

	/**
	* マスター取込詳細(de3)登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto マスター取込詳細(de3)
	* @return 件数
	*/
	public int upsert(
		MasterTorikomiShousaiDe3 dto
		 ){
		final String sql =
				"INSERT INTO master_torikomi_shousai_de3 "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT master_torikomi_shousai_de3_pkey "
			+ "DO UPDATE SET et_column_name = ?, et_data_type = ?, op_colume_id = ?, op_column_name = ?, op_data_type = ?, entry_order = ?, pk_flg = ? "
			+ "";
			return connection.update(sql,
				dto.masterId, dto.etColumnId, dto.etColumnName, dto.etDataType, dto.opColumeId, dto.opColumnName, dto.opDataType, dto.entryOrder, dto.pkFlg
				, dto.etColumnName, dto.etDataType, dto.opColumeId, dto.opColumnName, dto.opDataType, dto.entryOrder, dto.pkFlg
				);
    }
	
	/**
	 * マスター取込詳細(de3)から主キー指定でレコードを削除
	 * @param masterId マスターID
	 * @param etColumnId eTeamカラムID
	 * @return 削除件数
	 */
	public int delete(String masterId, String etColumnId){
		final String sql = "DELETE FROM master_torikomi_shousai_de3 WHERE master_id = ? AND et_column_id = ?";
		return connection.update(sql, masterId, etColumnId);
	}
	
	/**
	 * マスター取込詳細(de3)から一部キー指定でレコードを削除
	 * @param masterId マスターID
	 * @return 削除件数
	 */
	public int delete(String masterId) {
		final String sql = "DELETE FROM master_torikomi_shousai_de3 WHERE master_id = ? ";
		return connection.update(sql, masterId);
	}
}
