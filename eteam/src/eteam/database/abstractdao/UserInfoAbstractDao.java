package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.UserInfo;

/**
 * ユーザー情報に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class UserInfoAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected UserInfo mapToDto(GMap map){
		return map == null ? null : new UserInfo(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<UserInfo> mapToDto(List<GMap> mapList){
		List<UserInfo> dtoList = new ArrayList<UserInfo>();
		for (var map : mapList) {
			dtoList.add(new UserInfo(map));
		}
		return dtoList;
	}
	
	/**
	 * ユーザー情報のレコード有無を判定
	 * @param userId ユーザーID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String userId) {
		return this.find(userId) == null ? false : true;
	}
	
	/**
	 * ユーザー情報から主キー指定でレコードを取得
	 * @param userId ユーザーID
	 * @return ユーザー情報DTO
	 */
	public UserInfo find(String userId) {
		final String sql = "SELECT * FROM user_info WHERE user_id = ?";
		return mapToDto(connection.find(sql, userId));
	}
	
	/**
	 * ユーザー情報からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ユーザー情報DTO>
	 */
	public List<UserInfo> load() {
		final String sql = "SELECT * FROM user_info ORDER BY user_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* ユーザー情報登録
	* @param dto ユーザー情報
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		UserInfo dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO user_info "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.mailAddress, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId, koushinUserId, dto.passKoushinDate, dto.passFailureCount, dto.passFailureTime, dto.tmpLockFlg, dto.tmpLockTime, dto.lockFlg, dto.lockTime, dto.dairikihyouFlg, dto.houjinCardRiyouFlag, dto.houjinCardShikibetsuyouNum, dto.securityPattern, dto.securityWfonlyFlg, dto.shouninRouteHenkouLevel, dto.maruhiKengenFlg, dto.maruhiKaijyoFlg, dto.zaimuKyotenNyuryokuOnlyFlg
					);
	}

	/**
	* ユーザー情報の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したUserInfoの使用を前提
	* @param dto ユーザー情報
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		UserInfo dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE user_info "
		    + "SET shain_no = ?, user_sei = ?, user_mei = ?, mail_address = ?, yuukou_kigen_from = ?, yuukou_kigen_to = ?, koushin_user_id = ?, koushin_time = current_timestamp, pass_koushin_date = ?, pass_failure_count = ?, pass_failure_time = ?, tmp_lock_flg = ?, tmp_lock_time = ?, lock_flg = ?, lock_time = ?, dairikihyou_flg = ?, houjin_card_riyou_flag = ?, houjin_card_shikibetsuyou_num = ?, security_pattern = ?, security_wfonly_flg = ?, shounin_route_henkou_level = ?, maruhi_kengen_flg = ?, maruhi_kaijyo_flg = ?, zaimu_kyoten_nyuryoku_only_flg = ? "
	 		+ "WHERE koushin_time = ? AND user_id = ?";
			return connection.update(sql,
				dto.shainNo, dto.userSei, dto.userMei, dto.mailAddress, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId, dto.passKoushinDate, dto.passFailureCount, dto.passFailureTime, dto.tmpLockFlg, dto.tmpLockTime, dto.lockFlg, dto.lockTime, dto.dairikihyouFlg, dto.houjinCardRiyouFlag, dto.houjinCardShikibetsuyouNum, dto.securityPattern, dto.securityWfonlyFlg, dto.shouninRouteHenkouLevel, dto.maruhiKengenFlg, dto.maruhiKaijyoFlg, dto.zaimuKyotenNyuryokuOnlyFlg
				,dto.koushinTime, dto.userId);
    }

	/**
	* ユーザー情報登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ユーザー情報
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		UserInfo dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO user_info "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT user_info_pkey "
			+ "DO UPDATE SET shain_no = ?, user_sei = ?, user_mei = ?, mail_address = ?, yuukou_kigen_from = ?, yuukou_kigen_to = ?, koushin_user_id = ?, koushin_time = current_timestamp, pass_koushin_date = ?, pass_failure_count = ?, pass_failure_time = ?, tmp_lock_flg = ?, tmp_lock_time = ?, lock_flg = ?, lock_time = ?, dairikihyou_flg = ?, houjin_card_riyou_flag = ?, houjin_card_shikibetsuyou_num = ?, security_pattern = ?, security_wfonly_flg = ?, shounin_route_henkou_level = ?, maruhi_kengen_flg = ?, maruhi_kaijyo_flg = ?, zaimu_kyoten_nyuryoku_only_flg = ? "
			+ "";
			return connection.update(sql,
				dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.mailAddress, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId, koushinUserId, dto.passKoushinDate, dto.passFailureCount, dto.passFailureTime, dto.tmpLockFlg, dto.tmpLockTime, dto.lockFlg, dto.lockTime, dto.dairikihyouFlg, dto.houjinCardRiyouFlag, dto.houjinCardShikibetsuyouNum, dto.securityPattern, dto.securityWfonlyFlg, dto.shouninRouteHenkouLevel, dto.maruhiKengenFlg, dto.maruhiKaijyoFlg, dto.zaimuKyotenNyuryokuOnlyFlg
				, dto.shainNo, dto.userSei, dto.userMei, dto.mailAddress, dto.yuukouKigenFrom, dto.yuukouKigenTo, koushinUserId, dto.passKoushinDate, dto.passFailureCount, dto.passFailureTime, dto.tmpLockFlg, dto.tmpLockTime, dto.lockFlg, dto.lockTime, dto.dairikihyouFlg, dto.houjinCardRiyouFlag, dto.houjinCardShikibetsuyouNum, dto.securityPattern, dto.securityWfonlyFlg, dto.shouninRouteHenkouLevel, dto.maruhiKengenFlg, dto.maruhiKaijyoFlg, dto.zaimuKyotenNyuryokuOnlyFlg
				);
    }
	
	/**
	 * ユーザー情報から主キー指定でレコードを削除
	 * @param userId ユーザーID
	 * @return 削除件数
	 */
	public int delete(String userId){
		final String sql = "DELETE FROM user_info WHERE user_id = ?";
		return connection.update(sql, userId);
	}
}
