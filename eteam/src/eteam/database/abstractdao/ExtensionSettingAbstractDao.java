package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ExtensionSetting;

/**
 * 拡張子設定に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ExtensionSettingAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ExtensionSetting mapToDto(GMap map){
		return map == null ? null : new ExtensionSetting(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ExtensionSetting> mapToDto(List<GMap> mapList){
		List<ExtensionSetting> dtoList = new ArrayList<ExtensionSetting>();
		for (var map : mapList) {
			dtoList.add(new ExtensionSetting(map));
		}
		return dtoList;
	}
	
	/**
	 * 拡張子設定のレコード有無を判定
	 * @param extensionCd 拡張子コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String extensionCd) {
		return this.find(extensionCd) == null ? false : true;
	}
	
	/**
	 * 拡張子設定から主キー指定でレコードを取得
	 * @param extensionCd 拡張子コード
	 * @return 拡張子設定DTO
	 */
	public ExtensionSetting find(String extensionCd) {
		final String sql = "SELECT * FROM extension_setting WHERE extension_cd = ?";
		return mapToDto(connection.find(sql, extensionCd));
	}
	
	/**
	 * 拡張子設定からレコードを全件取得 ※大量データ取得に注意
	 * @return List<拡張子設定DTO>
	 */
	public List<ExtensionSetting> load() {
		final String sql = "SELECT * FROM extension_setting ORDER BY extension_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* 拡張子設定登録
	* @param dto 拡張子設定
	* @return 件数
	*/
	public int insert(
		ExtensionSetting dto
	){
		final String sql =
				"INSERT INTO extension_setting "
			+ "VALUES(?, ? "
			+ ")";
			return connection.update(sql,
					dto.extensionCd, dto.extensionFlg
					);
	}

	/**
	* 拡張子設定登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 拡張子設定
	* @return 件数
	*/
	public int upsert(
		ExtensionSetting dto
		 ){
		final String sql =
				"INSERT INTO extension_setting "
			+ "VALUES(?, ? "
			+ ") ON CONFLICT ON CONSTRAINT extension_setting_pkey "
			+ "DO UPDATE SET extension_flg = ? "
			+ "";
			return connection.update(sql,
				dto.extensionCd, dto.extensionFlg
				, dto.extensionFlg
				);
    }
	
	/**
	 * 拡張子設定から主キー指定でレコードを削除
	 * @param extensionCd 拡張子コード
	 * @return 削除件数
	 */
	public int delete(String extensionCd){
		final String sql = "DELETE FROM extension_setting WHERE extension_cd = ?";
		return connection.update(sql, extensionCd);
	}
}
