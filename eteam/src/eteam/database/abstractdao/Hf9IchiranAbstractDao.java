package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Hf9Ichiran;

/**
 * ヘッダフィールド９一覧に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class Hf9IchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Hf9Ichiran mapToDto(GMap map){
		return map == null ? null : new Hf9Ichiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Hf9Ichiran> mapToDto(List<GMap> mapList){
		List<Hf9Ichiran> dtoList = new ArrayList<Hf9Ichiran>();
		for (var map : mapList) {
			dtoList.add(new Hf9Ichiran(map));
		}
		return dtoList;
	}
	
	/**
	 * ヘッダフィールド９一覧のレコード有無を判定
	 * @param hf9Cd HF9コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String hf9Cd) {
		return this.find(hf9Cd) == null ? false : true;
	}
	
	/**
	 * ヘッダフィールド９一覧から主キー指定でレコードを取得
	 * @param hf9Cd HF9コード
	 * @return ヘッダフィールド９一覧DTO
	 */
	public Hf9Ichiran find(String hf9Cd) {
		final String sql = "SELECT * FROM hf9_ichiran WHERE hf9_cd = ?";
		return mapToDto(connection.find(sql, hf9Cd));
	}
	
	/**
	 * ヘッダフィールド９一覧からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ヘッダフィールド９一覧DTO>
	 */
	public List<Hf9Ichiran> load() {
		final String sql = "SELECT * FROM hf9_ichiran ORDER BY hf9_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* ヘッダフィールド９一覧登録
	* @param dto ヘッダフィールド９一覧
	* @return 件数
	*/
	public int insert(
		Hf9Ichiran dto
	){
		final String sql =
				"INSERT INTO hf9_ichiran "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.hf9Cd, dto.hf9NameRyakushiki, dto.kessankiBangou
					);
	}

	/**
	* ヘッダフィールド９一覧登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ヘッダフィールド９一覧
	* @return 件数
	*/
	public int upsert(
		Hf9Ichiran dto
		 ){
		final String sql =
				"INSERT INTO hf9_ichiran "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT hf9_ichiran_pkey "
			+ "DO UPDATE SET hf9_name_ryakushiki = ?, kessanki_bangou = ? "
			+ "";
			return connection.update(sql,
				dto.hf9Cd, dto.hf9NameRyakushiki, dto.kessankiBangou
				, dto.hf9NameRyakushiki, dto.kessankiBangou
				);
    }
	
	/**
	 * ヘッダフィールド９一覧から主キー指定でレコードを削除
	 * @param hf9Cd HF9コード
	 * @return 削除件数
	 */
	public int delete(String hf9Cd){
		final String sql = "DELETE FROM hf9_ichiran WHERE hf9_cd = ?";
		return connection.update(sql, hf9Cd);
	}
}