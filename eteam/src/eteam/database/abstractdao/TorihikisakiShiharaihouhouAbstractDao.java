package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.TorihikisakiShiharaihouhou;

/**
 * 取引先支払方法に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class TorihikisakiShiharaihouhouAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected TorihikisakiShiharaihouhou mapToDto(GMap map){
		return map == null ? null : new TorihikisakiShiharaihouhou(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<TorihikisakiShiharaihouhou> mapToDto(List<GMap> mapList){
		List<TorihikisakiShiharaihouhou> dtoList = new ArrayList<TorihikisakiShiharaihouhou>();
		for (var map : mapList) {
			dtoList.add(new TorihikisakiShiharaihouhou(map));
		}
		return dtoList;
	}
	
	/**
	 * 取引先支払方法のレコード有無を判定
	 * @param torihikisakiCd 取引先コード
	 * @param shiharaiId 支払ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String torihikisakiCd, int shiharaiId) {
		return this.find(torihikisakiCd, shiharaiId) == null ? false : true;
	}
	
	/**
	 * 取引先支払方法から主キー指定でレコードを取得
	 * @param torihikisakiCd 取引先コード
	 * @param shiharaiId 支払ID
	 * @return 取引先支払方法DTO
	 */
	public TorihikisakiShiharaihouhou find(String torihikisakiCd, int shiharaiId) {
		final String sql = "SELECT * FROM torihikisaki_shiharaihouhou WHERE torihikisaki_cd = ? AND shiharai_id = ?";
		return mapToDto(connection.find(sql, torihikisakiCd, shiharaiId));
	}
	
	/**
	 * 取引先支払方法からレコードを全件取得 ※大量データ取得に注意
	 * @return List<取引先支払方法DTO>
	 */
	public List<TorihikisakiShiharaihouhou> load() {
		final String sql = "SELECT * FROM torihikisaki_shiharaihouhou ORDER BY torihikisaki_cd, shiharai_id";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 取引先支払方法から一部キー指定でレコードを取得
	 * @param torihikisakiCd 取引先コード
	 * @return List<取引先支払方法>DTO
	 */
	public List<TorihikisakiShiharaihouhou> load(String torihikisakiCd) {
		final String sql = "SELECT * FROM torihikisaki_shiharaihouhou WHERE torihikisaki_cd = ? "
							+ "ORDER BY torihikisaki_cd, shiharai_id";
		return mapToDto(connection.load(sql, torihikisakiCd));
	}

	/**
	* 取引先支払方法登録
	* @param dto 取引先支払方法
	* @return 件数
	*/
	public int insert(
		TorihikisakiShiharaihouhou dto
	){
		final String sql =
				"INSERT INTO torihikisaki_shiharaihouhou "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.torihikisakiCd, dto.shiharaiId, dto.shimebi, dto.shiharaibiMm, dto.shiharaibiDd, dto.shiharaiKbn, dto.shiharaikijitsuMm, dto.shiharaikijitsuDd, dto.haraiH, dto.kijituH, dto.shiharaiHouhou
					);
	}

	/**
	* 取引先支払方法登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 取引先支払方法
	* @return 件数
	*/
	public int upsert(
		TorihikisakiShiharaihouhou dto
		 ){
		final String sql =
				"INSERT INTO torihikisaki_shiharaihouhou "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT torihikisaki_shiharaihouhou_pkey "
			+ "DO UPDATE SET shimebi = ?, shiharaibi_mm = ?, shiharaibi_dd = ?, shiharai_kbn = ?, shiharaikijitsu_mm = ?, shiharaikijitsu_dd = ?, harai_h = ?, kijitu_h = ?, shiharai_houhou = ? "
			+ "";
			return connection.update(sql,
				dto.torihikisakiCd, dto.shiharaiId, dto.shimebi, dto.shiharaibiMm, dto.shiharaibiDd, dto.shiharaiKbn, dto.shiharaikijitsuMm, dto.shiharaikijitsuDd, dto.haraiH, dto.kijituH, dto.shiharaiHouhou
				, dto.shimebi, dto.shiharaibiMm, dto.shiharaibiDd, dto.shiharaiKbn, dto.shiharaikijitsuMm, dto.shiharaikijitsuDd, dto.haraiH, dto.kijituH, dto.shiharaiHouhou
				);
    }
	
	/**
	 * 取引先支払方法から主キー指定でレコードを削除
	 * @param torihikisakiCd 取引先コード
	 * @param shiharaiId 支払ID
	 * @return 削除件数
	 */
	public int delete(String torihikisakiCd, int shiharaiId){
		final String sql = "DELETE FROM torihikisaki_shiharaihouhou WHERE torihikisaki_cd = ? AND shiharai_id = ?";
		return connection.update(sql, torihikisakiCd, shiharaiId);
	}
	
	/**
	 * 取引先支払方法から一部キー指定でレコードを削除
	 * @param torihikisakiCd 取引先コード
	 * @return 削除件数
	 */
	public int delete(String torihikisakiCd) {
		final String sql = "DELETE FROM torihikisaki_shiharaihouhou WHERE torihikisaki_cd = ? ";
		return connection.update(sql, torihikisakiCd);
	}
}
