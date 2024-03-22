package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.SettingInfo;

/**
 * 設定情報に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class SettingInfoAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected SettingInfo mapToDto(GMap map){
		return map == null ? null : new SettingInfo(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<SettingInfo> mapToDto(List<GMap> mapList){
		List<SettingInfo> dtoList = new ArrayList<SettingInfo>();
		for (var map : mapList) {
			dtoList.add(new SettingInfo(map));
		}
		return dtoList;
	}
	
	/**
	 * 設定情報のレコード有無を判定
	 * @param settingName 設定名
	 * @return true:exist false:not exist
	 */
	public boolean exists(String settingName) {
		return this.find(settingName) == null ? false : true;
	}
	
	/**
	 * 設定情報から主キー指定でレコードを取得
	 * @param settingName 設定名
	 * @return 設定情報DTO
	 */
	public SettingInfo find(String settingName) {
		final String sql = "SELECT * FROM setting_info WHERE setting_name = ?";
		return mapToDto(connection.find(sql, settingName));
	}
	
	/**
	 * 設定情報からレコードを全件取得 ※大量データ取得に注意
	 * @return List<設定情報DTO>
	 */
	public List<SettingInfo> load() {
		final String sql = "SELECT * FROM setting_info ORDER BY setting_name";
		return mapToDto(connection.load(sql));
	}

	/**
	* 設定情報登録
	* @param dto 設定情報
	* @return 件数
	*/
	public int insert(
		SettingInfo dto
	){
		final String sql =
				"INSERT INTO setting_info "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.settingName, dto.settingNameWa, dto.settingVal, dto.category, dto.hyoujiJun, dto.editableFlg, dto.hissuFlg, dto.formatRegex, dto.description
					);
	}

	/**
	* 設定情報登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 設定情報
	* @return 件数
	*/
	public int upsert(
		SettingInfo dto
		 ){
		final String sql =
				"INSERT INTO setting_info "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT setting_info_pkey "
			+ "DO UPDATE SET setting_name_wa = ?, setting_val = ?, category = ?, hyouji_jun = ?, editable_flg = ?, hissu_flg = ?, format_regex = ?, description = ? "
			+ "";
			return connection.update(sql,
				dto.settingName, dto.settingNameWa, dto.settingVal, dto.category, dto.hyoujiJun, dto.editableFlg, dto.hissuFlg, dto.formatRegex, dto.description
				, dto.settingNameWa, dto.settingVal, dto.category, dto.hyoujiJun, dto.editableFlg, dto.hissuFlg, dto.formatRegex, dto.description
				);
    }
	
	/**
	 * 設定情報から主キー指定でレコードを削除
	 * @param settingName 設定名
	 * @return 削除件数
	 */
	public int delete(String settingName){
		final String sql = "DELETE FROM setting_info WHERE setting_name = ?";
		return connection.update(sql, settingName);
	}
}