package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.UfKotei7Ichiran;

/**
 * ユニバーサルフィールド７一覧（固定値）に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class UfKotei7IchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected UfKotei7Ichiran mapToDto(GMap map){
		return map == null ? null : new UfKotei7Ichiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<UfKotei7Ichiran> mapToDto(List<GMap> mapList){
		List<UfKotei7Ichiran> dtoList = new ArrayList<UfKotei7Ichiran>();
		for (var map : mapList) {
			dtoList.add(new UfKotei7Ichiran(map));
		}
		return dtoList;
	}
	
	/**
	 * ユニバーサルフィールド７一覧（固定値）のレコード有無を判定
	 * @param ufKotei7Cd UF7コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String ufKotei7Cd) {
		return this.find(ufKotei7Cd) == null ? false : true;
	}
	
	/**
	 * ユニバーサルフィールド７一覧（固定値）から主キー指定でレコードを取得
	 * @param ufKotei7Cd UF7コード
	 * @return ユニバーサルフィールド７一覧（固定値）DTO
	 */
	public UfKotei7Ichiran find(String ufKotei7Cd) {
		final String sql = "SELECT * FROM uf_kotei7_ichiran WHERE uf_kotei7_cd = ?";
		return mapToDto(connection.find(sql, ufKotei7Cd));
	}
	
	/**
	 * ユニバーサルフィールド７一覧（固定値）からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ユニバーサルフィールド７一覧（固定値）DTO>
	 */
	public List<UfKotei7Ichiran> load() {
		final String sql = "SELECT * FROM uf_kotei7_ichiran ORDER BY uf_kotei7_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* ユニバーサルフィールド７一覧（固定値）登録
	* @param dto ユニバーサルフィールド７一覧（固定値）
	* @return 件数
	*/
	public int insert(
		UfKotei7Ichiran dto
	){
		final String sql =
				"INSERT INTO uf_kotei7_ichiran "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.ufKotei7Cd, dto.ufKotei7NameRyakushiki, dto.kessankiBangou
					);
	}

	/**
	* ユニバーサルフィールド７一覧（固定値）登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ユニバーサルフィールド７一覧（固定値）
	* @return 件数
	*/
	public int upsert(
		UfKotei7Ichiran dto
		 ){
		final String sql =
				"INSERT INTO uf_kotei7_ichiran "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT uf_kotei7_ichiran_pkey "
			+ "DO UPDATE SET uf_kotei7_name_ryakushiki = ?, kessanki_bangou = ? "
			+ "";
			return connection.update(sql,
				dto.ufKotei7Cd, dto.ufKotei7NameRyakushiki, dto.kessankiBangou
				, dto.ufKotei7NameRyakushiki, dto.kessankiBangou
				);
    }
	
	/**
	 * ユニバーサルフィールド７一覧（固定値）から主キー指定でレコードを削除
	 * @param ufKotei7Cd UF7コード
	 * @return 削除件数
	 */
	public int delete(String ufKotei7Cd){
		final String sql = "DELETE FROM uf_kotei7_ichiran WHERE uf_kotei7_cd = ?";
		return connection.update(sql, ufKotei7Cd);
	}
}