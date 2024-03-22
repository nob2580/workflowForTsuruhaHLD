package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.MasterTorikomiTermIchiranMk2;

/**
 * マスター取込期間一覧(SIAS_mk2)に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class MasterTorikomiTermIchiranMk2AbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected MasterTorikomiTermIchiranMk2 mapToDto(GMap map){
		return map == null ? null : new MasterTorikomiTermIchiranMk2(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<MasterTorikomiTermIchiranMk2> mapToDto(List<GMap> mapList){
		List<MasterTorikomiTermIchiranMk2> dtoList = new ArrayList<MasterTorikomiTermIchiranMk2>();
		for (var map : mapList) {
			dtoList.add(new MasterTorikomiTermIchiranMk2(map));
		}
		return dtoList;
	}
	
	/**
	 * マスター取込期間一覧(SIAS_mk2)のレコード有無を判定
	 * @param masterId マスターID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String masterId) {
		return this.find(masterId) == null ? false : true;
	}
	
	/**
	 * マスター取込期間一覧(SIAS_mk2)から主キー指定でレコードを取得
	 * @param masterId マスターID
	 * @return マスター取込期間一覧(SIAS_mk2)DTO
	 */
	public MasterTorikomiTermIchiranMk2 find(String masterId) {
		final String sql = "SELECT * FROM master_torikomi_term_ichiran_mk2 WHERE master_id = ?";
		return mapToDto(connection.find(sql, masterId));
	}
	
	/**
	 * マスター取込期間一覧(SIAS_mk2)からレコードを全件取得 ※大量データ取得に注意
	 * @return List<マスター取込期間一覧(SIAS_mk2)DTO>
	 */
	public List<MasterTorikomiTermIchiranMk2> load() {
		final String sql = "SELECT * FROM master_torikomi_term_ichiran_mk2 ORDER BY master_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* マスター取込期間一覧(SIAS_mk2)登録
	* @param dto マスター取込期間一覧(SIAS_mk2)
	* @return 件数
	*/
	public int insert(
		MasterTorikomiTermIchiranMk2 dto
	){
		final String sql =
				"INSERT INTO master_torikomi_term_ichiran_mk2 "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.masterId, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
					);
	}

	/**
	* マスター取込期間一覧(SIAS_mk2)登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto マスター取込期間一覧(SIAS_mk2)
	* @return 件数
	*/
	public int upsert(
		MasterTorikomiTermIchiranMk2 dto
		 ){
		final String sql =
				"INSERT INTO master_torikomi_term_ichiran_mk2 "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT master_torikomi_term_ichiran_mk2_pkey "
			+ "DO UPDATE SET master_name = ?, op_master_id = ?, op_master_name = ?, torikomi_kahi_flg = ? "
			+ "";
			return connection.update(sql,
				dto.masterId, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
				, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
				);
    }
	
	/**
	 * マスター取込期間一覧(SIAS_mk2)から主キー指定でレコードを削除
	 * @param masterId マスターID
	 * @return 削除件数
	 */
	public int delete(String masterId){
		final String sql = "DELETE FROM master_torikomi_term_ichiran_mk2 WHERE master_id = ?";
		return connection.update(sql, masterId);
	}
}