package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.MasterKanriIchiran;

/**
 * マスター管理一覧に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class MasterKanriIchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected MasterKanriIchiran mapToDto(GMap map){
		return map == null ? null : new MasterKanriIchiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<MasterKanriIchiran> mapToDto(List<GMap> mapList){
		List<MasterKanriIchiran> dtoList = new ArrayList<MasterKanriIchiran>();
		for (var map : mapList) {
			dtoList.add(new MasterKanriIchiran(map));
		}
		return dtoList;
	}
	
	/**
	 * マスター管理一覧のレコード有無を判定
	 * @param masterId マスターID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String masterId) {
		return this.find(masterId) == null ? false : true;
	}
	
	/**
	 * マスター管理一覧から主キー指定でレコードを取得
	 * @param masterId マスターID
	 * @return マスター管理一覧DTO
	 */
	public MasterKanriIchiran find(String masterId) {
		final String sql = "SELECT * FROM master_kanri_ichiran WHERE master_id = ?";
		return mapToDto(connection.find(sql, masterId));
	}
	
	/**
	 * マスター管理一覧からレコードを全件取得 ※大量データ取得に注意
	 * @return List<マスター管理一覧DTO>
	 */
	public List<MasterKanriIchiran> load() {
		final String sql = "SELECT * FROM master_kanri_ichiran ORDER BY master_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* マスター管理一覧登録
	* @param dto マスター管理一覧
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		MasterKanriIchiran dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO master_kanri_ichiran "
			+ "VALUES(?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.masterId, dto.masterName, dto.henkouKahiFlg, koushinUserId, koushinUserId
					);
	}

	/**
	* マスター管理一覧の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したMasterKanriIchiranの使用を前提
	* @param dto マスター管理一覧
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		MasterKanriIchiran dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE master_kanri_ichiran "
		    + "SET master_name = ?, henkou_kahi_flg = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND master_id = ?";
			return connection.update(sql,
				dto.masterName, dto.henkouKahiFlg, koushinUserId
				,dto.koushinTime, dto.masterId);
    }

	/**
	* マスター管理一覧登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto マスター管理一覧
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		MasterKanriIchiran dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO master_kanri_ichiran "
			+ "VALUES(?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT master_kanri_ichiran_pkey "
			+ "DO UPDATE SET master_name = ?, henkou_kahi_flg = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.masterId, dto.masterName, dto.henkouKahiFlg, koushinUserId, koushinUserId
				, dto.masterName, dto.henkouKahiFlg, koushinUserId
				);
    }
	
	/**
	 * マスター管理一覧から主キー指定でレコードを削除
	 * @param masterId マスターID
	 * @return 削除件数
	 */
	public int delete(String masterId){
		final String sql = "DELETE FROM master_kanri_ichiran WHERE master_id = ?";
		return connection.update(sql, masterId);
	}
}