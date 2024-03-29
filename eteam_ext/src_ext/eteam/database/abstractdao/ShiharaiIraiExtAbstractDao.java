package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ShiharaiIraiExt;
/**
 * 支払依頼Extに対する標準データ操作クラス
 * 今後のカスタマイズ含め作成しておく
 */
public abstract class ShiharaiIraiExtAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO shiharai_irai_ext (denpyou_id, furikomi_ginkou_id) VALUES "
			+ "(?, ?)";
	/**
	 * 主キー条件定型部
	 */
	protected final String whereSql = " WHERE denpyou_id = ?";

	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected ShiharaiIraiExt mapToDto(GMap map) {
		return map == null ? null : new ShiharaiIraiExt(map);
	}

	/**
	 * @param mapList 検索結果GMap
	 * @return dtoList
	 */
	protected List<ShiharaiIraiExt> mapToDto(List<GMap> mapList) {
		List<ShiharaiIraiExt> dtoList = new ArrayList<ShiharaiIraiExt>();
		for (var map : mapList) {
			dtoList.add(new ShiharaiIraiExt(map));
		}
		return dtoList;
	}

	/**
	 * 支払依頼のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId) {
		return this.find(denpyouId) != null;
	}

	/**
	 * 支払依頼から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return 支払依頼DTO
	 */
	public ShiharaiIraiExt find(String denpyouId) {
		final String sql = "SELECT * FROM shiharai_irai_ext " + this.whereSql;
		return mapToDto(connection.find(sql, denpyouId));
	}

	/**
	 * 支払依頼からレコードを全件取得 ※大量データ取得に注意
	 * @return List<支払依頼DTO>
	 */
	public List<ShiharaiIraiExt> load() {
		final String sql = "SELECT * FROM shiharai_irai_ext  ORDER BY denpyou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	 * 支払依頼登録
	 * @param dto 支払依頼
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int insert(ShiharaiIraiExt dto) {
		return connection.update(this.insertSql, dto.denpyouId, dto.furikomiGinkouId);
	}

	/**
	 * 支払依頼更新
	 * @param dto 支払依頼
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int update(ShiharaiIraiExt dto) {
		final String sql = "UPDATE shiharai_irai_ext SET  furikomi_ginkou_id = ? " + this.whereSql;
		return connection.update(sql, dto.furikomiGinkouId,dto.denpyouId);
	}

	/**
	 * 支払依頼登録or更新
	 * 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	 * @param dto 支払依頼
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int upsert(ShiharaiIraiExt dto, String koushinUserId) {
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT shiharai_irai_ext_pkey "
			+ "DO UPDATE SET  furikomi_ginkou_id = ? ";
		return connection.update(sql, dto.furikomiGinkouId);
				}

	/**
	 * 支払依頼から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM shiharai_irai_ext " + this.whereSql;
		return connection.update(sql, denpyouId);
	}

}
