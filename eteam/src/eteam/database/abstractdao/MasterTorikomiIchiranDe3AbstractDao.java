package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.MasterTorikomiIchiranDe3;

/**
 * マスター取込一覧(de3)に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class MasterTorikomiIchiranDe3AbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected MasterTorikomiIchiranDe3 mapToDto(GMap map){
		return map == null ? null : new MasterTorikomiIchiranDe3(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<MasterTorikomiIchiranDe3> mapToDto(List<GMap> mapList){
		List<MasterTorikomiIchiranDe3> dtoList = new ArrayList<MasterTorikomiIchiranDe3>();
		for (var map : mapList) {
			dtoList.add(new MasterTorikomiIchiranDe3(map));
		}
		return dtoList;
	}
	
	/**
	 * マスター取込一覧(de3)のレコード有無を判定
	 * @param masterId マスターID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String masterId) {
		return this.find(masterId) == null ? false : true;
	}
	
	/**
	 * マスター取込一覧(de3)から主キー指定でレコードを取得
	 * @param masterId マスターID
	 * @return マスター取込一覧(de3)DTO
	 */
	public MasterTorikomiIchiranDe3 find(String masterId) {
		final String sql = "SELECT * FROM master_torikomi_ichiran_de3 WHERE master_id = ?";
		return mapToDto(connection.find(sql, masterId));
	}
	
	/**
	 * マスター取込一覧(de3)からレコードを全件取得 ※大量データ取得に注意
	 * @return List<マスター取込一覧(de3)DTO>
	 */
	public List<MasterTorikomiIchiranDe3> load() {
		final String sql = "SELECT * FROM master_torikomi_ichiran_de3 ORDER BY master_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* マスター取込一覧(de3)登録
	* @param dto マスター取込一覧(de3)
	* @return 件数
	*/
	public int insert(
		MasterTorikomiIchiranDe3 dto
	){
		final String sql =
				"INSERT INTO master_torikomi_ichiran_de3 "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.masterId, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
					);
	}

	/**
	* マスター取込一覧(de3)登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto マスター取込一覧(de3)
	* @return 件数
	*/
	public int upsert(
		MasterTorikomiIchiranDe3 dto
		 ){
		final String sql =
				"INSERT INTO master_torikomi_ichiran_de3 "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT master_torikomi_ichiran_de3_pkey "
			+ "DO UPDATE SET master_name = ?, op_master_id = ?, op_master_name = ?, torikomi_kahi_flg = ? "
			+ "";
			return connection.update(sql,
				dto.masterId, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
				, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
				);
    }
	
	/**
	 * マスター取込一覧(de3)から主キー指定でレコードを削除
	 * @param masterId マスターID
	 * @return 削除件数
	 */
	public int delete(String masterId){
		final String sql = "DELETE FROM master_torikomi_ichiran_de3 WHERE master_id = ?";
		return connection.update(sql, masterId);
	}
}
