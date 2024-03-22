package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ShouninRouteGougiOya;

/**
 * 承認ルート合議親に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ShouninRouteGougiOyaAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ShouninRouteGougiOya mapToDto(GMap map){
		return map == null ? null : new ShouninRouteGougiOya(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ShouninRouteGougiOya> mapToDto(List<GMap> mapList){
		List<ShouninRouteGougiOya> dtoList = new ArrayList<ShouninRouteGougiOya>();
		for (var map : mapList) {
			dtoList.add(new ShouninRouteGougiOya(map));
		}
		return dtoList;
	}
	
	/**
	 * 承認ルート合議親のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @param gougiEdano 合議枝番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId, int edano, int gougiEdano) {
		return this.find(denpyouId, edano, gougiEdano) == null ? false : true;
	}
	
	/**
	 * 承認ルート合議親から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @param gougiEdano 合議枝番号
	 * @return 承認ルート合議親DTO
	 */
	public ShouninRouteGougiOya find(String denpyouId, int edano, int gougiEdano) {
		final String sql = "SELECT * FROM shounin_route_gougi_oya WHERE denpyou_id = ? AND edano = ? AND gougi_edano = ?";
		return mapToDto(connection.find(sql, denpyouId, edano, gougiEdano));
	}
	
	/**
	 * 承認ルート合議親からレコードを全件取得 ※大量データ取得に注意
	 * @return List<承認ルート合議親DTO>
	 */
	public List<ShouninRouteGougiOya> load() {
		final String sql = "SELECT * FROM shounin_route_gougi_oya ORDER BY denpyou_id, edano, gougi_edano";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 承認ルート合議親から一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return List<承認ルート合議親>DTO
	 */
	public List<ShouninRouteGougiOya> load(String denpyouId) {
		final String sql = "SELECT * FROM shounin_route_gougi_oya WHERE denpyou_id = ? "
							+ "ORDER BY denpyou_id, edano, gougi_edano";
		return mapToDto(connection.load(sql, denpyouId));
	}
	
	/**
	 * 承認ルート合議親から一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @return List<承認ルート合議親>DTO
	 */
	public List<ShouninRouteGougiOya> load(String denpyouId, int edano) {
		final String sql = "SELECT * FROM shounin_route_gougi_oya WHERE denpyou_id = ?  AND edano = ? "
							+ "ORDER BY denpyou_id, edano, gougi_edano";
		return mapToDto(connection.load(sql, denpyouId, edano));
	}

	/**
	* 承認ルート合議親登録
	* @param dto 承認ルート合議親
	* @return 件数
	*/
	public int insert(
		ShouninRouteGougiOya dto
	){
		final String sql =
				"INSERT INTO shounin_route_gougi_oya "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.edano, dto.gougiEdano, dto.gougiPatternNo, dto.gougiName, dto.syoriCd
					);
	}

	/**
	* 承認ルート合議親登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 承認ルート合議親
	* @return 件数
	*/
	public int upsert(
		ShouninRouteGougiOya dto
		 ){
		final String sql =
				"INSERT INTO shounin_route_gougi_oya "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT shounin_route_gougi_oya_pkey "
			+ "DO UPDATE SET gougi_pattern_no = ?, gougi_name = ?, syori_cd = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.edano, dto.gougiEdano, dto.gougiPatternNo, dto.gougiName, dto.syoriCd
				, dto.gougiPatternNo, dto.gougiName, dto.syoriCd
				);
    }
	
	/**
	 * 承認ルート合議親から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @param gougiEdano 合議枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int edano, int gougiEdano){
		final String sql = "DELETE FROM shounin_route_gougi_oya WHERE denpyou_id = ? AND edano = ? AND gougi_edano = ?";
		return connection.update(sql, denpyouId, edano, gougiEdano);
	}
	
	/**
	 * 承認ルート合議親から一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM shounin_route_gougi_oya WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 承認ルート合議親から一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int edano) {
		final String sql = "DELETE FROM shounin_route_gougi_oya WHERE denpyou_id = ?  AND edano = ? ";
		return connection.update(sql, denpyouId, edano);
	}
}
