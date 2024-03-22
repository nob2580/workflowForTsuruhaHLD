package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.MasterTorikomiTermIchiranSias;

/**
 * マスター取込期間一覧(SIAS)に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class MasterTorikomiTermIchiranSiasAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected MasterTorikomiTermIchiranSias mapToDto(GMap map){
		return map == null ? null : new MasterTorikomiTermIchiranSias(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<MasterTorikomiTermIchiranSias> mapToDto(List<GMap> mapList){
		List<MasterTorikomiTermIchiranSias> dtoList = new ArrayList<MasterTorikomiTermIchiranSias>();
		for (var map : mapList) {
			dtoList.add(new MasterTorikomiTermIchiranSias(map));
		}
		return dtoList;
	}
	
	/**
	 * マスター取込期間一覧(SIAS)のレコード有無を判定
	 * @param masterId マスターID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String masterId) {
		return this.find(masterId) == null ? false : true;
	}
	
	/**
	 * マスター取込期間一覧(SIAS)から主キー指定でレコードを取得
	 * @param masterId マスターID
	 * @return マスター取込期間一覧(SIAS)DTO
	 */
	public MasterTorikomiTermIchiranSias find(String masterId) {
		final String sql = "SELECT * FROM master_torikomi_term_ichiran_sias WHERE master_id = ?";
		return mapToDto(connection.find(sql, masterId));
	}
	
	/**
	 * マスター取込期間一覧(SIAS)からレコードを全件取得 ※大量データ取得に注意
	 * @return List<マスター取込期間一覧(SIAS)DTO>
	 */
	public List<MasterTorikomiTermIchiranSias> load() {
		final String sql = "SELECT * FROM master_torikomi_term_ichiran_sias ORDER BY master_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* マスター取込期間一覧(SIAS)登録
	* @param dto マスター取込期間一覧(SIAS)
	* @return 件数
	*/
	public int insert(
		MasterTorikomiTermIchiranSias dto
	){
		final String sql =
				"INSERT INTO master_torikomi_term_ichiran_sias "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.masterId, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
					);
	}

	/**
	* マスター取込期間一覧(SIAS)登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto マスター取込期間一覧(SIAS)
	* @return 件数
	*/
	public int upsert(
		MasterTorikomiTermIchiranSias dto
		 ){
		final String sql =
				"INSERT INTO master_torikomi_term_ichiran_sias "
			+ "VALUES(?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT master_torikomi_term_ichiran_sias_pkey "
			+ "DO UPDATE SET master_name = ?, op_master_id = ?, op_master_name = ?, torikomi_kahi_flg = ? "
			+ "";
			return connection.update(sql,
				dto.masterId, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
				, dto.masterName, dto.opMasterId, dto.opMasterName, dto.torikomiKahiFlg
				);
    }
	
	/**
	 * マスター取込期間一覧(SIAS)から主キー指定でレコードを削除
	 * @param masterId マスターID
	 * @return 削除件数
	 */
	public int delete(String masterId){
		final String sql = "DELETE FROM master_torikomi_term_ichiran_sias WHERE master_id = ?";
		return connection.update(sql, masterId);
	}
}
