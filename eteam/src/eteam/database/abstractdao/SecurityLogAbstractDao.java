package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.SecurityLog;

/**
 * セキュリティログに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class SecurityLogAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected SecurityLog mapToDto(GMap map){
		return map == null ? null : new SecurityLog(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<SecurityLog> mapToDto(List<GMap> mapList){
		List<SecurityLog> dtoList = new ArrayList<SecurityLog>();
		for (var map : mapList) {
			dtoList.add(new SecurityLog(map));
		}
		return dtoList;
	}
	
	/**
	 * セキュリティログのレコード有無を判定
	 * @param serialNo シリアル番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(long serialNo) {
		return this.find(serialNo) == null ? false : true;
	}
	
	/**
	 * セキュリティログから主キー指定でレコードを取得
	 * @param serialNo シリアル番号
	 * @return セキュリティログDTO
	 */
	public SecurityLog find(long serialNo) {
		final String sql = "SELECT * FROM security_log WHERE serial_no = ?";
		return mapToDto(connection.find(sql, serialNo));
	}
	
	/**
	 * セキュリティログからレコードを全件取得 ※大量データ取得に注意
	 * @return List<セキュリティログDTO>
	 */
	public List<SecurityLog> load() {
		final String sql = "SELECT * FROM security_log ORDER BY serial_no";
		return mapToDto(connection.load(sql));
	}

	/**
	* セキュリティログ登録
	* @param dto セキュリティログ
	* @return 件数
	*/
	public int insert(
		SecurityLog dto
	){
		final String sql =
				"INSERT INTO security_log "
			+ "( event_time, ip, ip_xforwarded, user_id, gyoumu_role_id, target, type, detail) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.eventTime, dto.ip, dto.ipXforwarded, dto.userId, dto.gyoumuRoleId, dto.target, dto.type, dto.detail
					);
	}

	/**
	* セキュリティログ登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto セキュリティログ
	* @return 件数
	*/
	public int upsert(
		SecurityLog dto
		 ){
		final String sql =
				"INSERT INTO security_log "
			+ "( event_time, ip, ip_xforwarded, user_id, gyoumu_role_id, target, type, detail) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT security_log_pkey "
			+ "DO UPDATE SET event_time = ?, ip = ?, ip_xforwarded = ?, user_id = ?, gyoumu_role_id = ?, target = ?, type = ?, detail = ? "
			+ "";
			return connection.update(sql,
				dto.eventTime, dto.ip, dto.ipXforwarded, dto.userId, dto.gyoumuRoleId, dto.target, dto.type, dto.detail
				, dto.eventTime, dto.ip, dto.ipXforwarded, dto.userId, dto.gyoumuRoleId, dto.target, dto.type, dto.detail
				);
    }
	
	/**
	 * セキュリティログから主キー指定でレコードを削除
	 * @param serialNo シリアル番号
	 * @return 削除件数
	 */
	public int delete(long serialNo){
		final String sql = "DELETE FROM security_log WHERE serial_no = ?";
		return connection.update(sql, serialNo);
	}
}
