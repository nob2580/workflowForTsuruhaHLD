package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Hf2Ichiran;

/**
 * ヘッダフィールド２一覧に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class Hf2IchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Hf2Ichiran mapToDto(GMap map){
		return map == null ? null : new Hf2Ichiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Hf2Ichiran> mapToDto(List<GMap> mapList){
		List<Hf2Ichiran> dtoList = new ArrayList<Hf2Ichiran>();
		for (var map : mapList) {
			dtoList.add(new Hf2Ichiran(map));
		}
		return dtoList;
	}
	
	/**
	 * ヘッダフィールド２一覧のレコード有無を判定
	 * @param hf2Cd HF2コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String hf2Cd) {
		return this.find(hf2Cd) == null ? false : true;
	}
	
	/**
	 * ヘッダフィールド２一覧から主キー指定でレコードを取得
	 * @param hf2Cd HF2コード
	 * @return ヘッダフィールド２一覧DTO
	 */
	public Hf2Ichiran find(String hf2Cd) {
		final String sql = "SELECT * FROM hf2_ichiran WHERE hf2_cd = ?";
		return mapToDto(connection.find(sql, hf2Cd));
	}
	
	/**
	 * ヘッダフィールド２一覧からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ヘッダフィールド２一覧DTO>
	 */
	public List<Hf2Ichiran> load() {
		final String sql = "SELECT * FROM hf2_ichiran ORDER BY hf2_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* ヘッダフィールド２一覧登録
	* @param dto ヘッダフィールド２一覧
	* @return 件数
	*/
	public int insert(
		Hf2Ichiran dto
	){
		final String sql =
				"INSERT INTO hf2_ichiran "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.hf2Cd, dto.hf2NameRyakushiki, dto.kessankiBangou
					);
	}

	/**
	* ヘッダフィールド２一覧登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ヘッダフィールド２一覧
	* @return 件数
	*/
	public int upsert(
		Hf2Ichiran dto
		 ){
		final String sql =
				"INSERT INTO hf2_ichiran "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT hf2_ichiran_pkey "
			+ "DO UPDATE SET hf2_name_ryakushiki = ?, kessanki_bangou = ? "
			+ "";
			return connection.update(sql,
				dto.hf2Cd, dto.hf2NameRyakushiki, dto.kessankiBangou
				, dto.hf2NameRyakushiki, dto.kessankiBangou
				);
    }
	
	/**
	 * ヘッダフィールド２一覧から主キー指定でレコードを削除
	 * @param hf2Cd HF2コード
	 * @return 削除件数
	 */
	public int delete(String hf2Cd){
		final String sql = "DELETE FROM hf2_ichiran WHERE hf2_cd = ?";
		return connection.update(sql, hf2Cd);
	}
}