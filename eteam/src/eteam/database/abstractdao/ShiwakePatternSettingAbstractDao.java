package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ShiwakePatternSetting;

/**
 * 仕訳パターン設定に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ShiwakePatternSettingAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ShiwakePatternSetting mapToDto(GMap map){
		return map == null ? null : new ShiwakePatternSetting(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ShiwakePatternSetting> mapToDto(List<GMap> mapList){
		List<ShiwakePatternSetting> dtoList = new ArrayList<ShiwakePatternSetting>();
		for (var map : mapList) {
			dtoList.add(new ShiwakePatternSetting(map));
		}
		return dtoList;
	}
	
	/**
	 * 仕訳パターン設定のレコード有無を判定
	 * @param denpyouKbn 伝票区分
	 * @param settingKbn 設定区分
	 * @param settingItem 設定項目
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouKbn, String settingKbn, String settingItem) {
		return this.find(denpyouKbn, settingKbn, settingItem) == null ? false : true;
	}
	
	/**
	 * 仕訳パターン設定から主キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param settingKbn 設定区分
	 * @param settingItem 設定項目
	 * @return 仕訳パターン設定DTO
	 */
	public ShiwakePatternSetting find(String denpyouKbn, String settingKbn, String settingItem) {
		final String sql = "SELECT * FROM shiwake_pattern_setting WHERE denpyou_kbn = ? AND setting_kbn = ? AND setting_item = ?";
		return mapToDto(connection.find(sql, denpyouKbn, settingKbn, settingItem));
	}
	
	/**
	 * 仕訳パターン設定からレコードを全件取得 ※大量データ取得に注意
	 * @return List<仕訳パターン設定DTO>
	 */
	public List<ShiwakePatternSetting> load() {
		final String sql = "SELECT * FROM shiwake_pattern_setting ORDER BY denpyou_kbn, setting_kbn, setting_item";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 仕訳パターン設定から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @return List<仕訳パターン設定>DTO
	 */
	public List<ShiwakePatternSetting> load(String denpyouKbn) {
		final String sql = "SELECT * FROM shiwake_pattern_setting WHERE denpyou_kbn = ? "
							+ "ORDER BY denpyou_kbn, setting_kbn, setting_item";
		return mapToDto(connection.load(sql, denpyouKbn));
	}
	
	/**
	 * 仕訳パターン設定から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param settingKbn 設定区分
	 * @return List<仕訳パターン設定>DTO
	 */
	public List<ShiwakePatternSetting> load(String denpyouKbn, String settingKbn) {
		final String sql = "SELECT * FROM shiwake_pattern_setting WHERE denpyou_kbn = ?  AND setting_kbn = ? "
							+ "ORDER BY denpyou_kbn, setting_kbn, setting_item";
		return mapToDto(connection.load(sql, denpyouKbn, settingKbn));
	}

	/**
	* 仕訳パターン設定登録
	* @param dto 仕訳パターン設定
	* @return 件数
	*/
	public int insert(
		ShiwakePatternSetting dto
	){
		final String sql =
				"INSERT INTO shiwake_pattern_setting "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouKbn, dto.settingKbn, dto.settingItem, dto.defaultValue, dto.hyoujiFlg, dto.shiwakePatternVar1, dto.shiwakePatternVar2, dto.shiwakePatternVar3, dto.shiwakePatternVar4, dto.shiwakePatternVar5
					);
	}

	/**
	* 仕訳パターン設定登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 仕訳パターン設定
	* @return 件数
	*/
	public int upsert(
		ShiwakePatternSetting dto
		 ){
		final String sql =
				"INSERT INTO shiwake_pattern_setting "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT shiwake_pattern_setting_pkey "
			+ "DO UPDATE SET default_value = ?, hyouji_flg = ?, shiwake_pattern_var1 = ?, shiwake_pattern_var2 = ?, shiwake_pattern_var3 = ?, shiwake_pattern_var4 = ?, shiwake_pattern_var5 = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouKbn, dto.settingKbn, dto.settingItem, dto.defaultValue, dto.hyoujiFlg, dto.shiwakePatternVar1, dto.shiwakePatternVar2, dto.shiwakePatternVar3, dto.shiwakePatternVar4, dto.shiwakePatternVar5
				, dto.defaultValue, dto.hyoujiFlg, dto.shiwakePatternVar1, dto.shiwakePatternVar2, dto.shiwakePatternVar3, dto.shiwakePatternVar4, dto.shiwakePatternVar5
				);
    }
	
	/**
	 * 仕訳パターン設定から主キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param settingKbn 設定区分
	 * @param settingItem 設定項目
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, String settingKbn, String settingItem){
		final String sql = "DELETE FROM shiwake_pattern_setting WHERE denpyou_kbn = ? AND setting_kbn = ? AND setting_item = ?";
		return connection.update(sql, denpyouKbn, settingKbn, settingItem);
	}
	
	/**
	 * 仕訳パターン設定から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn) {
		final String sql = "DELETE FROM shiwake_pattern_setting WHERE denpyou_kbn = ? ";
		return connection.update(sql, denpyouKbn);
	}
	
	/**
	 * 仕訳パターン設定から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param settingKbn 設定区分
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, String settingKbn) {
		final String sql = "DELETE FROM shiwake_pattern_setting WHERE denpyou_kbn = ?  AND setting_kbn = ? ";
		return connection.update(sql, denpyouKbn, settingKbn);
	}
	
	/**
	 * 仕訳パターン設定テーブルから仕訳パターン設定情報を取得する。
	 * @param denpyouKbn String 伝票区分
	 * @param settingKbn String 設定区分
	 * @param settingItem String 設定項目
	 * @return 検索結果
	 */
	public String findDefaultValue(String denpyouKbn, String settingKbn, String settingItem){
		var record = this.find(denpyouKbn, settingKbn, settingItem);
		return (null == record) ? "" : record.defaultValue;
	}
}
