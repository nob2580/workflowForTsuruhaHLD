package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Uf6Ichiran;

/**
 * ユニバーサルフィールド６一覧に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class Uf6IchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Uf6Ichiran mapToDto(GMap map){
		return map == null ? null : new Uf6Ichiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Uf6Ichiran> mapToDto(List<GMap> mapList){
		List<Uf6Ichiran> dtoList = new ArrayList<Uf6Ichiran>();
		for (var map : mapList) {
			dtoList.add(new Uf6Ichiran(map));
		}
		return dtoList;
	}
	
	/**
	 * ユニバーサルフィールド６一覧のレコード有無を判定
	 * @param uf6Cd UF6コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String uf6Cd) {
		return this.find(uf6Cd) == null ? false : true;
	}
	
	/**
	 * ユニバーサルフィールド６一覧から主キー指定でレコードを取得
	 * @param uf6Cd UF6コード
	 * @return ユニバーサルフィールド６一覧DTO
	 */
	public Uf6Ichiran find(String uf6Cd) {
		final String sql = "SELECT * FROM uf6_ichiran WHERE uf6_cd = ?";
		return mapToDto(connection.find(sql, uf6Cd));
	}
	
	/**
	 * ユニバーサルフィールド６一覧からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ユニバーサルフィールド６一覧DTO>
	 */
	public List<Uf6Ichiran> load() {
		final String sql = "SELECT * FROM uf6_ichiran ORDER BY uf6_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* ユニバーサルフィールド６一覧登録
	* @param dto ユニバーサルフィールド６一覧
	* @return 件数
	*/
	public int insert(
		Uf6Ichiran dto
	){
		final String sql =
				"INSERT INTO uf6_ichiran "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.uf6Cd, dto.uf6NameRyakushiki, dto.kessankiBangou
					);
	}

	/**
	* ユニバーサルフィールド６一覧登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ユニバーサルフィールド６一覧
	* @return 件数
	*/
	public int upsert(
		Uf6Ichiran dto
		 ){
		final String sql =
				"INSERT INTO uf6_ichiran "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT uf6_ichiran_pkey "
			+ "DO UPDATE SET uf6_name_ryakushiki = ?, kessanki_bangou = ? "
			+ "";
			return connection.update(sql,
				dto.uf6Cd, dto.uf6NameRyakushiki, dto.kessankiBangou
				, dto.uf6NameRyakushiki, dto.kessankiBangou
				);
    }
	
	/**
	 * ユニバーサルフィールド６一覧から主キー指定でレコードを削除
	 * @param uf6Cd UF6コード
	 * @return 削除件数
	 */
	public int delete(String uf6Cd){
		final String sql = "DELETE FROM uf6_ichiran WHERE uf6_cd = ?";
		return connection.update(sql, uf6Cd);
	}
}