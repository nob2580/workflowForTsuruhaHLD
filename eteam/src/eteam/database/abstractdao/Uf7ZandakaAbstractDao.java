package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Uf7Zandaka;

/**
 * ユニバーサルフィールド７残高に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class Uf7ZandakaAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Uf7Zandaka mapToDto(GMap map){
		return map == null ? null : new Uf7Zandaka(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Uf7Zandaka> mapToDto(List<GMap> mapList){
		List<Uf7Zandaka> dtoList = new ArrayList<Uf7Zandaka>();
		for (var map : mapList) {
			dtoList.add(new Uf7Zandaka(map));
		}
		return dtoList;
	}
	
	/**
	 * ユニバーサルフィールド７残高のレコード有無を判定
	 * @param uf7Cd UF7コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String uf7Cd, String kamokuGaibuCd) {
		return this.find(uf7Cd, kamokuGaibuCd) == null ? false : true;
	}
	
	/**
	 * ユニバーサルフィールド７残高から主キー指定でレコードを取得
	 * @param uf7Cd UF7コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return ユニバーサルフィールド７残高DTO
	 */
	public Uf7Zandaka find(String uf7Cd, String kamokuGaibuCd) {
		final String sql = "SELECT * FROM uf7_zandaka WHERE uf7_cd = ? AND kamoku_gaibu_cd = ?";
		return mapToDto(connection.find(sql, uf7Cd, kamokuGaibuCd));
	}
	
	/**
	 * ユニバーサルフィールド７残高からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ユニバーサルフィールド７残高DTO>
	 */
	public List<Uf7Zandaka> load() {
		final String sql = "SELECT * FROM uf7_zandaka ORDER BY uf7_cd, kamoku_gaibu_cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * ユニバーサルフィールド７残高から一部キー指定でレコードを取得
	 * @param uf7Cd UF7コード
	 * @return List<ユニバーサルフィールド７残高>DTO
	 */
	public List<Uf7Zandaka> load(String uf7Cd) {
		final String sql = "SELECT * FROM uf7_zandaka WHERE uf7_cd = ? "
							+ "ORDER BY uf7_cd, kamoku_gaibu_cd";
		return mapToDto(connection.load(sql, uf7Cd));
	}

	/**
	* ユニバーサルフィールド７残高登録
	* @param dto ユニバーサルフィールド７残高
	* @return 件数
	*/
	public int insert(
		Uf7Zandaka dto
	){
		final String sql =
				"INSERT INTO uf7_zandaka "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.uf7Cd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.uf7NameRyakushiki, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.kishuZandaka
					);
	}

	/**
	* ユニバーサルフィールド７残高登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ユニバーサルフィールド７残高
	* @return 件数
	*/
	public int upsert(
		Uf7Zandaka dto
		 ){
		final String sql =
				"INSERT INTO uf7_zandaka "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT uf7_zandaka_pkey "
			+ "DO UPDATE SET kessanki_bangou = ?, kamoku_naibu_cd = ?, uf7_name_ryakushiki = ?, chouhyou_shaturyoku_no = ?, kamoku_name_ryakushiki = ?, kamoku_name_seishiki = ?, taishaku_zokusei = ?, kishu_zandaka = ? "
			+ "";
			return connection.update(sql,
				dto.uf7Cd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.uf7NameRyakushiki, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.kishuZandaka
				, dto.kessankiBangou, dto.kamokuNaibuCd, dto.uf7NameRyakushiki, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.kishuZandaka
				);
    }
	
	/**
	 * ユニバーサルフィールド７残高から主キー指定でレコードを削除
	 * @param uf7Cd UF7コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return 削除件数
	 */
	public int delete(String uf7Cd, String kamokuGaibuCd){
		final String sql = "DELETE FROM uf7_zandaka WHERE uf7_cd = ? AND kamoku_gaibu_cd = ?";
		return connection.update(sql, uf7Cd, kamokuGaibuCd);
	}
	
	/**
	 * ユニバーサルフィールド７残高から一部キー指定でレコードを削除
	 * @param uf7Cd UF7コード
	 * @return 削除件数
	 */
	public int delete(String uf7Cd) {
		final String sql = "DELETE FROM uf7_zandaka WHERE uf7_cd = ? ";
		return connection.update(sql, uf7Cd);
	}
}
