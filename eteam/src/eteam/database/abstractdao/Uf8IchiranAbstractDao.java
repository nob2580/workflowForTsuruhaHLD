package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Uf8Ichiran;

/**
 * ユニバーサルフィールド８一覧に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class Uf8IchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Uf8Ichiran mapToDto(GMap map){
		return map == null ? null : new Uf8Ichiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Uf8Ichiran> mapToDto(List<GMap> mapList){
		List<Uf8Ichiran> dtoList = new ArrayList<Uf8Ichiran>();
		for (var map : mapList) {
			dtoList.add(new Uf8Ichiran(map));
		}
		return dtoList;
	}
	
	/**
	 * ユニバーサルフィールド８一覧のレコード有無を判定
	 * @param uf8Cd UF8コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String uf8Cd) {
		return this.find(uf8Cd) == null ? false : true;
	}
	
	/**
	 * ユニバーサルフィールド８一覧から主キー指定でレコードを取得
	 * @param uf8Cd UF8コード
	 * @return ユニバーサルフィールド８一覧DTO
	 */
	public Uf8Ichiran find(String uf8Cd) {
		final String sql = "SELECT * FROM uf8_ichiran WHERE uf8_cd = ?";
		return mapToDto(connection.find(sql, uf8Cd));
	}
	
	/**
	 * ユニバーサルフィールド８一覧からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ユニバーサルフィールド８一覧DTO>
	 */
	public List<Uf8Ichiran> load() {
		final String sql = "SELECT * FROM uf8_ichiran ORDER BY uf8_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* ユニバーサルフィールド８一覧登録
	* @param dto ユニバーサルフィールド８一覧
	* @return 件数
	*/
	public int insert(
		Uf8Ichiran dto
	){
		final String sql =
				"INSERT INTO uf8_ichiran "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.uf8Cd, dto.uf8NameRyakushiki, dto.kessankiBangou
					);
	}

	/**
	* ユニバーサルフィールド８一覧登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ユニバーサルフィールド８一覧
	* @return 件数
	*/
	public int upsert(
		Uf8Ichiran dto
		 ){
		final String sql =
				"INSERT INTO uf8_ichiran "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT uf8_ichiran_pkey "
			+ "DO UPDATE SET uf8_name_ryakushiki = ?, kessanki_bangou = ? "
			+ "";
			return connection.update(sql,
				dto.uf8Cd, dto.uf8NameRyakushiki, dto.kessankiBangou
				, dto.uf8NameRyakushiki, dto.kessankiBangou
				);
    }
	
	/**
	 * ユニバーサルフィールド８一覧から主キー指定でレコードを削除
	 * @param uf8Cd UF8コード
	 * @return 削除件数
	 */
	public int delete(String uf8Cd){
		final String sql = "DELETE FROM uf8_ichiran WHERE uf8_cd = ?";
		return connection.update(sql, uf8Cd);
	}
}
