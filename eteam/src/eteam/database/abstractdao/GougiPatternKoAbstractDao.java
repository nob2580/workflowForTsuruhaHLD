package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.GougiPatternKo;

/**
 * 合議パターン子に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class GougiPatternKoAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected GougiPatternKo mapToDto(GMap map){
		return map == null ? null : new GougiPatternKo(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<GougiPatternKo> mapToDto(List<GMap> mapList){
		List<GougiPatternKo> dtoList = new ArrayList<GougiPatternKo>();
		for (var map : mapList) {
			dtoList.add(new GougiPatternKo(map));
		}
		return dtoList;
	}
	
	/**
	 * 合議パターン子のレコード有無を判定
	 * @param gougiPatternNo 合議パターン番号
	 * @param edano 枝番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(long gougiPatternNo, int edano) {
		return this.find(gougiPatternNo, edano) == null ? false : true;
	}
	
	/**
	 * 合議パターン子から主キー指定でレコードを取得
	 * @param gougiPatternNo 合議パターン番号
	 * @param edano 枝番号
	 * @return 合議パターン子DTO
	 */
	public GougiPatternKo find(long gougiPatternNo, int edano) {
		final String sql = "SELECT * FROM gougi_pattern_ko WHERE gougi_pattern_no = ? AND edano = ?";
		return mapToDto(connection.find(sql, gougiPatternNo, edano));
	}
	
	/**
	 * 合議パターン子からレコードを全件取得 ※大量データ取得に注意
	 * @return List<合議パターン子DTO>
	 */
	public List<GougiPatternKo> load() {
		final String sql = "SELECT * FROM gougi_pattern_ko ORDER BY gougi_pattern_no, edano";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 合議パターン子から一部キー指定でレコードを取得
	 * @param gougiPatternNo 合議パターン番号
	 * @return List<合議パターン子>DTO
	 */
	public List<GougiPatternKo> load(long gougiPatternNo) {
		final String sql = "SELECT * FROM gougi_pattern_ko WHERE gougi_pattern_no = ? "
							+ "ORDER BY gougi_pattern_no, edano";
		return mapToDto(connection.load(sql, gougiPatternNo));
	}

	/**
	* 合議パターン子登録
	* @param dto 合議パターン子
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		GougiPatternKo dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO gougi_pattern_ko "
			+ "( gougi_pattern_no, edano, bumon_cd, bumon_role_id, shounin_ninzuu_cd, shounin_ninzuu_hiritsu, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.gougiPatternNo, dto.edano, dto.bumonCd, dto.bumonRoleId, dto.shouninNinzuuCd, dto.shouninNinzuuHiritsu, koushinUserId, koushinUserId
					);
	}

	/**
	* 合議パターン子の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したGougiPatternKoの使用を前提
	* @param dto 合議パターン子
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		GougiPatternKo dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE gougi_pattern_ko "
		    + "SET bumon_cd = ?, bumon_role_id = ?, shounin_shori_kengen_no = ?, shounin_ninzuu_cd = ?, shounin_ninzuu_hiritsu = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND gougi_pattern_no = ? AND edano = ?";
			return connection.update(sql,
				dto.bumonCd, dto.bumonRoleId, dto.shouninShoriKengenNo, dto.shouninNinzuuCd, dto.shouninNinzuuHiritsu, koushinUserId
				,dto.koushinTime, dto.gougiPatternNo, dto.edano);
    }

	/**
	* 合議パターン子登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 合議パターン子
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		GougiPatternKo dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO gougi_pattern_ko "
			+ "( gougi_pattern_no, edano, bumon_cd, bumon_role_id, shounin_ninzuu_cd, shounin_ninzuu_hiritsu, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT gougi_pattern_ko_pkey "
			+ "DO UPDATE SET bumon_cd = ?, bumon_role_id = ?, shounin_shori_kengen_no = ?, shounin_ninzuu_cd = ?, shounin_ninzuu_hiritsu = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.gougiPatternNo, dto.edano, dto.bumonCd, dto.bumonRoleId, dto.shouninNinzuuCd, dto.shouninNinzuuHiritsu, koushinUserId, koushinUserId
				, dto.bumonCd, dto.bumonRoleId, dto.shouninShoriKengenNo, dto.shouninNinzuuCd, dto.shouninNinzuuHiritsu, koushinUserId
				);
    }
	
	/**
	 * 合議パターン子から主キー指定でレコードを削除
	 * @param gougiPatternNo 合議パターン番号
	 * @param edano 枝番号
	 * @return 削除件数
	 */
	public int delete(long gougiPatternNo, int edano){
		final String sql = "DELETE FROM gougi_pattern_ko WHERE gougi_pattern_no = ? AND edano = ?";
		return connection.update(sql, gougiPatternNo, edano);
	}
	
	/**
	 * 合議パターン子から一部キー指定でレコードを削除
	 * @param gougiPatternNo 合議パターン番号
	 * @return 削除件数
	 */
	public int delete(long gougiPatternNo) {
		final String sql = "DELETE FROM gougi_pattern_ko WHERE gougi_pattern_no = ? ";
		return connection.update(sql, gougiPatternNo);
	}
}