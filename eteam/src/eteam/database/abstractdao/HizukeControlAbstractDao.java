package eteam.database.abstractdao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.HizukeControl;

/**
 * 日付コントロールに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class HizukeControlAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected HizukeControl mapToDto(GMap map){
		return map == null ? null : new HizukeControl(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<HizukeControl> mapToDto(List<GMap> mapList){
		List<HizukeControl> dtoList = new ArrayList<HizukeControl>();
		for (var map : mapList) {
			dtoList.add(new HizukeControl(map));
		}
		return dtoList;
	}
	
	/**
	 * 日付コントロールのレコード有無を判定
	 * @param systemKanriDate システム管理日付
	 * @return true:exist false:not exist
	 */
	public boolean exists(Date systemKanriDate) {
		return this.find(systemKanriDate) == null ? false : true;
	}
	
	/**
	 * 日付コントロールから主キー指定でレコードを取得
	 * @param systemKanriDate システム管理日付
	 * @return 日付コントロールDTO
	 */
	public HizukeControl find(Date systemKanriDate) {
		final String sql = "SELECT * FROM hizuke_control WHERE system_kanri_date = ?";
		return mapToDto(connection.find(sql, systemKanriDate));
	}
	
	/**
	 * 日付コントロールからレコードを全件取得 ※大量データ取得に注意
	 * @return List<日付コントロールDTO>
	 */
	public List<HizukeControl> load() {
		final String sql = "SELECT * FROM hizuke_control ORDER BY system_kanri_date";
		return mapToDto(connection.load(sql));
	}

	/**
	* 日付コントロール登録
	* @param dto 日付コントロール
	* @return 件数
	*/
	public int insert(
		HizukeControl dto
	){
		final String sql =
				"INSERT INTO hizuke_control "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.systemKanriDate, dto.fbDataSakuseibiFlg, dto.kojinseisanShiharaibi, dto.kinyuukikanEigyoubiFlg, dto.toujitsuKbnFlg, dto.keijoubiFlg
					);
	}

	/**
	* 日付コントロール登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 日付コントロール
	* @return 件数
	*/
	public int upsert(
		HizukeControl dto
		 ){
		final String sql =
				"INSERT INTO hizuke_control "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT hizuke_control_pkey "
			+ "DO UPDATE SET fb_data_sakuseibi_flg = ?, kojinseisan_shiharaibi = ?, kinyuukikan_eigyoubi_flg = ?, toujitsu_kbn_flg = ?, keijoubi_flg = ? "
			+ "";
			return connection.update(sql,
				dto.systemKanriDate, dto.fbDataSakuseibiFlg, dto.kojinseisanShiharaibi, dto.kinyuukikanEigyoubiFlg, dto.toujitsuKbnFlg, dto.keijoubiFlg
				, dto.fbDataSakuseibiFlg, dto.kojinseisanShiharaibi, dto.kinyuukikanEigyoubiFlg, dto.toujitsuKbnFlg, dto.keijoubiFlg
				);
    }
	
	/**
	 * 日付コントロールから主キー指定でレコードを削除
	 * @param systemKanriDate システム管理日付
	 * @return 削除件数
	 */
	public int delete(Date systemKanriDate){
		final String sql = "DELETE FROM hizuke_control WHERE system_kanri_date = ?";
		return connection.update(sql, systemKanriDate);
	}
}