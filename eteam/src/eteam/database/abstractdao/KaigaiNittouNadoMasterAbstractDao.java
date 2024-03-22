package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KaigaiNittouNadoMaster;

/**
 * 海外用日当等マスターに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KaigaiNittouNadoMasterAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KaigaiNittouNadoMaster mapToDto(GMap map){
		return map == null ? null : new KaigaiNittouNadoMaster(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KaigaiNittouNadoMaster> mapToDto(List<GMap> mapList){
		List<KaigaiNittouNadoMaster> dtoList = new ArrayList<KaigaiNittouNadoMaster>();
		for (var map : mapList) {
			dtoList.add(new KaigaiNittouNadoMaster(map));
		}
		return dtoList;
	}
	
	/**
	 * 海外用日当等マスターのレコード有無を判定
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @param yakushokuCd 役職コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String shubetsu1, String shubetsu2, String yakushokuCd) {
		return this.find(shubetsu1, shubetsu2, yakushokuCd) == null ? false : true;
	}
	
	/**
	 * 海外用日当等マスターから主キー指定でレコードを取得
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @param yakushokuCd 役職コード
	 * @return 海外用日当等マスターDTO
	 */
	public KaigaiNittouNadoMaster find(String shubetsu1, String shubetsu2, String yakushokuCd) {
		final String sql = "SELECT * FROM kaigai_nittou_nado_master WHERE shubetsu1 = ? AND shubetsu2 = ? AND yakushoku_cd = ?";
		return mapToDto(connection.find(sql, shubetsu1, shubetsu2, yakushokuCd));
	}
	
	/**
	 * 海外用日当等マスターからレコードを全件取得 ※大量データ取得に注意
	 * @return List<海外用日当等マスターDTO>
	 */
	public List<KaigaiNittouNadoMaster> load() {
		final String sql = "SELECT * FROM kaigai_nittou_nado_master ORDER BY shubetsu1, shubetsu2, yakushoku_cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 海外用日当等マスターから一部キー指定でレコードを取得
	 * @param shubetsu1 種別１
	 * @return List<海外用日当等マスター>DTO
	 */
	public List<KaigaiNittouNadoMaster> load(String shubetsu1) {
		final String sql = "SELECT * FROM kaigai_nittou_nado_master WHERE shubetsu1 = ? "
							+ "ORDER BY shubetsu1, shubetsu2, yakushoku_cd";
		return mapToDto(connection.load(sql, shubetsu1));
	}
	
	/**
	 * 海外用日当等マスターから一部キー指定でレコードを取得
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @return List<海外用日当等マスター>DTO
	 */
	public List<KaigaiNittouNadoMaster> load(String shubetsu1, String shubetsu2) {
		final String sql = "SELECT * FROM kaigai_nittou_nado_master WHERE shubetsu1 = ?  AND shubetsu2 = ? "
							+ "ORDER BY shubetsu1, shubetsu2, yakushoku_cd";
		return mapToDto(connection.load(sql, shubetsu1, shubetsu2));
	}

	/**
	* 海外用日当等マスター登録
	* @param dto 海外用日当等マスター
	* @return 件数
	*/
	public int insert(
		KaigaiNittouNadoMaster dto
	){
		final String sql =
				"INSERT INTO kaigai_nittou_nado_master "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.shubetsu1, dto.shubetsu2, dto.yakushokuCd, dto.tanka, dto.heishuCd, dto.currencyUnit, dto.tankaGaika, dto.shouhyouShoruiHissuFlg, dto.nittouShukuhakuhiFlg, dto.zeiKubun, dto.edaban
					);
	}

	/**
	* 海外用日当等マスター登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 海外用日当等マスター
	* @return 件数
	*/
	public int upsert(
		KaigaiNittouNadoMaster dto
		 ){
		final String sql =
				"INSERT INTO kaigai_nittou_nado_master "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT kaigai_nittou_nado_master_pkey "
			+ "DO UPDATE SET tanka = ?, heishu_cd = ?, currency_unit = ?, tanka_gaika = ?, shouhyou_shorui_hissu_flg = ?, nittou_shukuhakuhi_flg = ?, zei_kubun = ?, edaban = ? "
			+ "";
			return connection.update(sql,
				dto.shubetsu1, dto.shubetsu2, dto.yakushokuCd, dto.tanka, dto.heishuCd, dto.currencyUnit, dto.tankaGaika, dto.shouhyouShoruiHissuFlg, dto.nittouShukuhakuhiFlg, dto.zeiKubun, dto.edaban
				, dto.tanka, dto.heishuCd, dto.currencyUnit, dto.tankaGaika, dto.shouhyouShoruiHissuFlg, dto.nittouShukuhakuhiFlg, dto.zeiKubun, dto.edaban
				);
    }
	
	/**
	 * 海外用日当等マスターから主キー指定でレコードを削除
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @param yakushokuCd 役職コード
	 * @return 削除件数
	 */
	public int delete(String shubetsu1, String shubetsu2, String yakushokuCd){
		final String sql = "DELETE FROM kaigai_nittou_nado_master WHERE shubetsu1 = ? AND shubetsu2 = ? AND yakushoku_cd = ?";
		return connection.update(sql, shubetsu1, shubetsu2, yakushokuCd);
	}
	
	/**
	 * 海外用日当等マスターから一部キー指定でレコードを削除
	 * @param shubetsu1 種別１
	 * @return 削除件数
	 */
	public int delete(String shubetsu1) {
		final String sql = "DELETE FROM kaigai_nittou_nado_master WHERE shubetsu1 = ? ";
		return connection.update(sql, shubetsu1);
	}
	
	/**
	 * 海外用日当等マスターから一部キー指定でレコードを削除
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @return 削除件数
	 */
	public int delete(String shubetsu1, String shubetsu2) {
		final String sql = "DELETE FROM kaigai_nittou_nado_master WHERE shubetsu1 = ?  AND shubetsu2 = ? ";
		return connection.update(sql, shubetsu1, shubetsu2);
	}
}