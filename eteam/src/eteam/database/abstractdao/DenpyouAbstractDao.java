package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Denpyou;

/**
 * 伝票に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class DenpyouAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Denpyou mapToDto(GMap map){
		return map == null ? null : new Denpyou(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Denpyou> mapToDto(List<GMap> mapList){
		List<Denpyou> dtoList = new ArrayList<Denpyou>();
		for (var map : mapList) {
			dtoList.add(new Denpyou(map));
		}
		return dtoList;
	}
	
	/**
	 * 伝票のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId) {
		return this.find(denpyouId) == null ? false : true;
	}
	
	/**
	 * 伝票から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return 伝票DTO
	 */
	public Denpyou find(String denpyouId) {
		final String sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		return mapToDto(connection.find(sql, denpyouId));
	}
	
	/**
	 * 伝票からレコードを全件取得 ※大量データ取得に注意
	 * @return List<伝票DTO>
	 */
	public List<Denpyou> load() {
		final String sql = "SELECT * FROM denpyou ORDER BY denpyou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* 伝票登録
	* @param dto 伝票
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		Denpyou dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO denpyou "
			+ "VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.denpyouKbn, dto.denpyouJoutai, dto.sanshouDenpyouId, dto.daihyouFutanBumonCd, koushinUserId, koushinUserId, dto.serialNo, dto.chuushutsuZumiFlg, dto.shouninRouteHenkouFlg, dto.maruhiFlg, dto.yosanCheckNengetsu
					);
	}

	/**
	* 伝票の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したDenpyouの使用を前提
	* @param dto 伝票
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		Denpyou dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE denpyou "
		    + "SET denpyou_kbn = ?, denpyou_joutai = ?, sanshou_denpyou_id = ?, daihyou_futan_bumon_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp, serial_no = ?, chuushutsu_zumi_flg = ?, shounin_route_henkou_flg = ?, maruhi_flg = ?, yosan_check_nengetsu = ? "
	 		+ "WHERE koushin_time = ? AND denpyou_id = ?";
			return connection.update(sql,
				dto.denpyouKbn, dto.denpyouJoutai, dto.sanshouDenpyouId, dto.daihyouFutanBumonCd, koushinUserId, dto.serialNo, dto.chuushutsuZumiFlg, dto.shouninRouteHenkouFlg, dto.maruhiFlg, dto.yosanCheckNengetsu
				,dto.koushinTime, dto.denpyouId);
    }

	/**
	* 伝票登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 伝票
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		Denpyou dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO denpyou "
			+ "VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT denpyou_pkey "
			+ "DO UPDATE SET denpyou_kbn = ?, denpyou_joutai = ?, sanshou_denpyou_id = ?, daihyou_futan_bumon_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp, serial_no = ?, chuushutsu_zumi_flg = ?, shounin_route_henkou_flg = ?, maruhi_flg = ?, yosan_check_nengetsu = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.denpyouKbn, dto.denpyouJoutai, dto.sanshouDenpyouId, dto.daihyouFutanBumonCd, koushinUserId, koushinUserId, dto.serialNo, dto.chuushutsuZumiFlg, dto.shouninRouteHenkouFlg, dto.maruhiFlg, dto.yosanCheckNengetsu
				, dto.denpyouKbn, dto.denpyouJoutai, dto.sanshouDenpyouId, dto.daihyouFutanBumonCd, koushinUserId, dto.serialNo, dto.chuushutsuZumiFlg, dto.shouninRouteHenkouFlg, dto.maruhiFlg, dto.yosanCheckNengetsu
				);
    }
	
	/**
	 * 伝票から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId){
		final String sql = "DELETE FROM denpyou WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}
}
