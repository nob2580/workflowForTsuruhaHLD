package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Jishou;

/**
 * 事象に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class JishouAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Jishou mapToDto(GMap map){
		return map == null ? null : new Jishou(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Jishou> mapToDto(List<GMap> mapList){
		List<Jishou> dtoList = new ArrayList<Jishou>();
		for (var map : mapList) {
			dtoList.add(new Jishou(map));
		}
		return dtoList;
	}
	
	/**
	 * 事象のレコード有無を判定
	 * @param jishouId 事象ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(long jishouId) {
		return this.find(jishouId) == null ? false : true;
	}
	
	/**
	 * 事象から主キー指定でレコードを取得
	 * @param jishouId 事象ID
	 * @return 事象DTO
	 */
	public Jishou find(long jishouId) {
		final String sql = "SELECT * FROM jishou WHERE jishou_id = ?";
		return mapToDto(connection.find(sql, jishouId));
	}
	
	/**
	 * 事象からレコードを全件取得 ※大量データ取得に注意
	 * @return List<事象DTO>
	 */
	public List<Jishou> load() {
		final String sql = "SELECT * FROM jishou ORDER BY jishou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* 事象登録
	* @param dto 事象
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		Jishou dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO jishou "
			+ "( midashi_id, jishou_name, hyouji_jun, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
			+ "VALUES(?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.midashiId, dto.jishouName, dto.hyoujiJun, koushinUserId, koushinUserId
					);
	}

	/**
	* 事象の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したJishouの使用を前提
	* @param dto 事象
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		Jishou dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE jishou "
		    + "SET midashi_id = ?, jishou_name = ?, hyouji_jun = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND jishou_id = ?";
			return connection.update(sql,
				dto.midashiId, dto.jishouName, dto.hyoujiJun, koushinUserId
				,dto.koushinTime, dto.jishouId);
    }

	/**
	* 事象登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 事象
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		Jishou dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO jishou "
			+ "( midashi_id, jishou_name, hyouji_jun, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
			+ "VALUES(?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT jishou_pkey "
			+ "DO UPDATE SET midashi_id = ?, jishou_name = ?, hyouji_jun = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.midashiId, dto.jishouName, dto.hyoujiJun, koushinUserId, koushinUserId
				, dto.midashiId, dto.jishouName, dto.hyoujiJun, koushinUserId
				);
    }
	
	/**
	 * 事象から主キー指定でレコードを削除
	 * @param jishouId 事象ID
	 * @return 削除件数
	 */
	public int delete(long jishouId){
		final String sql = "DELETE FROM jishou WHERE jishou_id = ?";
		return connection.update(sql, jishouId);
	}
}
