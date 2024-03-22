package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.SaishuuSyouninRouteOya;

/**
 * 最終承認ルート親に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class SaishuuSyouninRouteOyaAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected SaishuuSyouninRouteOya mapToDto(GMap map){
		return map == null ? null : new SaishuuSyouninRouteOya(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<SaishuuSyouninRouteOya> mapToDto(List<GMap> mapList){
		List<SaishuuSyouninRouteOya> dtoList = new ArrayList<SaishuuSyouninRouteOya>();
		for (var map : mapList) {
			dtoList.add(new SaishuuSyouninRouteOya(map));
		}
		return dtoList;
	}
	
	/**
	 * 最終承認ルート親のレコード有無を判定
	 * @param denpyouKbn 伝票区分
	 * @param edano 枝番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouKbn, int edano) {
		return this.find(denpyouKbn, edano) == null ? false : true;
	}
	
	/**
	 * 最終承認ルート親から主キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param edano 枝番号
	 * @return 最終承認ルート親DTO
	 */
	public SaishuuSyouninRouteOya find(String denpyouKbn, int edano) {
		final String sql = "SELECT * FROM saishuu_syounin_route_oya WHERE denpyou_kbn = ? AND edano = ?";
		return mapToDto(connection.find(sql, denpyouKbn, edano));
	}
	
	/**
	 * 最終承認ルート親からレコードを全件取得 ※大量データ取得に注意
	 * @return List<最終承認ルート親DTO>
	 */
	public List<SaishuuSyouninRouteOya> load() {
		final String sql = "SELECT * FROM saishuu_syounin_route_oya ORDER BY denpyou_kbn, edano";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 最終承認ルート親から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @return List<最終承認ルート親>DTO
	 */
	public List<SaishuuSyouninRouteOya> load(String denpyouKbn) {
		final String sql = "SELECT * FROM saishuu_syounin_route_oya WHERE denpyou_kbn = ? "
							+ "ORDER BY denpyou_kbn, edano";
		return mapToDto(connection.load(sql, denpyouKbn));
	}

	/**
	* 最終承認ルート親登録
	* @param dto 最終承認ルート親
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		SaishuuSyouninRouteOya dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO saishuu_syounin_route_oya "
			+ "VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.denpyouKbn, dto.edano, dto.chuukiMongon, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId, koushinUserId
					);
	}

	/**
	* 最終承認ルート親の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したSaishuuSyouninRouteOyaの使用を前提
	* @param dto 最終承認ルート親
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		SaishuuSyouninRouteOya dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE saishuu_syounin_route_oya "
		    + "SET chuuki_mongon = ?, yuukou_kigen_from = ?, yuukou_kigen_to = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND denpyou_kbn = ? AND edano = ?";
			return connection.update(sql,
				dto.chuukiMongon, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId
				,dto.koushinTime, dto.denpyouKbn, dto.edano);
    }

	/**
	* 最終承認ルート親登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 最終承認ルート親
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		SaishuuSyouninRouteOya dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO saishuu_syounin_route_oya "
			+ "VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT saishuu_syounin_route_oya_pkey "
			+ "DO UPDATE SET chuuki_mongon = ?, yuukou_kigen_from = ?, yuukou_kigen_to = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.denpyouKbn, dto.edano, dto.chuukiMongon, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId, koushinUserId
				, dto.chuukiMongon, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId
				);
    }
	
	/**
	 * 最終承認ルート親から主キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param edano 枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, int edano){
		final String sql = "DELETE FROM saishuu_syounin_route_oya WHERE denpyou_kbn = ? AND edano = ?";
		return connection.update(sql, denpyouKbn, edano);
	}
	
	/**
	 * 最終承認ルート親から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn) {
		final String sql = "DELETE FROM saishuu_syounin_route_oya WHERE denpyou_kbn = ? ";
		return connection.update(sql, denpyouKbn);
	}
}