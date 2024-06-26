package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.UfKotei9Zandaka;

/**
 * ユニバーサルフィールド９残高（固定値）に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class UfKotei9ZandakaAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected UfKotei9Zandaka mapToDto(GMap map){
		return map == null ? null : new UfKotei9Zandaka(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<UfKotei9Zandaka> mapToDto(List<GMap> mapList){
		List<UfKotei9Zandaka> dtoList = new ArrayList<UfKotei9Zandaka>();
		for (var map : mapList) {
			dtoList.add(new UfKotei9Zandaka(map));
		}
		return dtoList;
	}
	
	/**
	 * ユニバーサルフィールド９残高（固定値）のレコード有無を判定
	 * @param ufKotei9Cd UF9コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String ufKotei9Cd, String kamokuGaibuCd) {
		return this.find(ufKotei9Cd, kamokuGaibuCd) == null ? false : true;
	}
	
	/**
	 * ユニバーサルフィールド９残高（固定値）から主キー指定でレコードを取得
	 * @param ufKotei9Cd UF9コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return ユニバーサルフィールド９残高（固定値）DTO
	 */
	public UfKotei9Zandaka find(String ufKotei9Cd, String kamokuGaibuCd) {
		final String sql = "SELECT * FROM uf_kotei9_zandaka WHERE uf_kotei9_cd = ? AND kamoku_gaibu_cd = ?";
		return mapToDto(connection.find(sql, ufKotei9Cd, kamokuGaibuCd));
	}
	
	/**
	 * ユニバーサルフィールド９残高（固定値）からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ユニバーサルフィールド９残高（固定値）DTO>
	 */
	public List<UfKotei9Zandaka> load() {
		final String sql = "SELECT * FROM uf_kotei9_zandaka ORDER BY uf_kotei9_cd, kamoku_gaibu_cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * ユニバーサルフィールド９残高（固定値）から一部キー指定でレコードを取得
	 * @param ufKotei9Cd UF9コード
	 * @return List<ユニバーサルフィールド９残高（固定値）>DTO
	 */
	public List<UfKotei9Zandaka> load(String ufKotei9Cd) {
		final String sql = "SELECT * FROM uf_kotei9_zandaka WHERE uf_kotei9_cd = ? "
							+ "ORDER BY uf_kotei9_cd, kamoku_gaibu_cd";
		return mapToDto(connection.load(sql, ufKotei9Cd));
	}

	/**
	* ユニバーサルフィールド９残高（固定値）登録
	* @param dto ユニバーサルフィールド９残高（固定値）
	* @return 件数
	*/
	public int insert(
		UfKotei9Zandaka dto
	){
		final String sql =
				"INSERT INTO uf_kotei9_zandaka "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.ufKotei9Cd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.ufKotei9NameRyakushiki, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.kishuZandaka
					);
	}

	/**
	* ユニバーサルフィールド９残高（固定値）登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ユニバーサルフィールド９残高（固定値）
	* @return 件数
	*/
	public int upsert(
		UfKotei9Zandaka dto
		 ){
		final String sql =
				"INSERT INTO uf_kotei9_zandaka "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT uf_kotei9_zandaka_pkey "
			+ "DO UPDATE SET kessanki_bangou = ?, kamoku_naibu_cd = ?, uf_kotei9_name_ryakushiki = ?, chouhyou_shaturyoku_no = ?, kamoku_name_ryakushiki = ?, kamoku_name_seishiki = ?, taishaku_zokusei = ?, kishu_zandaka = ? "
			+ "";
			return connection.update(sql,
				dto.ufKotei9Cd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.ufKotei9NameRyakushiki, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.kishuZandaka
				, dto.kessankiBangou, dto.kamokuNaibuCd, dto.ufKotei9NameRyakushiki, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.kishuZandaka
				);
    }
	
	/**
	 * ユニバーサルフィールド９残高（固定値）から主キー指定でレコードを削除
	 * @param ufKotei9Cd UF9コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return 削除件数
	 */
	public int delete(String ufKotei9Cd, String kamokuGaibuCd){
		final String sql = "DELETE FROM uf_kotei9_zandaka WHERE uf_kotei9_cd = ? AND kamoku_gaibu_cd = ?";
		return connection.update(sql, ufKotei9Cd, kamokuGaibuCd);
	}
	
	/**
	 * ユニバーサルフィールド９残高（固定値）から一部キー指定でレコードを削除
	 * @param ufKotei9Cd UF9コード
	 * @return 削除件数
	 */
	public int delete(String ufKotei9Cd) {
		final String sql = "DELETE FROM uf_kotei9_zandaka WHERE uf_kotei9_cd = ? ";
		return connection.update(sql, ufKotei9Cd);
	}
}
