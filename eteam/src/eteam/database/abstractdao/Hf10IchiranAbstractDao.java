package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Hf10Ichiran;

/**
 * ヘッダフィールド１０一覧に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class Hf10IchiranAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Hf10Ichiran mapToDto(GMap map){
		return map == null ? null : new Hf10Ichiran(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Hf10Ichiran> mapToDto(List<GMap> mapList){
		List<Hf10Ichiran> dtoList = new ArrayList<Hf10Ichiran>();
		for (var map : mapList) {
			dtoList.add(new Hf10Ichiran(map));
		}
		return dtoList;
	}
	
	/**
	 * ヘッダフィールド１０一覧のレコード有無を判定
	 * @param hf10Cd HF10コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String hf10Cd) {
		return this.find(hf10Cd) == null ? false : true;
	}
	
	/**
	 * ヘッダフィールド１０一覧から主キー指定でレコードを取得
	 * @param hf10Cd HF10コード
	 * @return ヘッダフィールド１０一覧DTO
	 */
	public Hf10Ichiran find(String hf10Cd) {
		final String sql = "SELECT * FROM hf10_ichiran WHERE hf10_cd = ?";
		return mapToDto(connection.find(sql, hf10Cd));
	}
	
	/**
	 * ヘッダフィールド１０一覧からレコードを全件取得 ※大量データ取得に注意
	 * @return List<ヘッダフィールド１０一覧DTO>
	 */
	public List<Hf10Ichiran> load() {
		final String sql = "SELECT * FROM hf10_ichiran ORDER BY hf10_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* ヘッダフィールド１０一覧登録
	* @param dto ヘッダフィールド１０一覧
	* @return 件数
	*/
	public int insert(
		Hf10Ichiran dto
	){
		final String sql =
				"INSERT INTO hf10_ichiran "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.hf10Cd, dto.hf10NameRyakushiki, dto.kessankiBangou
					);
	}

	/**
	* ヘッダフィールド１０一覧登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ヘッダフィールド１０一覧
	* @return 件数
	*/
	public int upsert(
		Hf10Ichiran dto
		 ){
		final String sql =
				"INSERT INTO hf10_ichiran "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT hf10_ichiran_pkey "
			+ "DO UPDATE SET hf10_name_ryakushiki = ?, kessanki_bangou = ? "
			+ "";
			return connection.update(sql,
				dto.hf10Cd, dto.hf10NameRyakushiki, dto.kessankiBangou
				, dto.hf10NameRyakushiki, dto.kessankiBangou
				);
    }
	
	/**
	 * ヘッダフィールド１０一覧から主キー指定でレコードを削除
	 * @param hf10Cd HF10コード
	 * @return 削除件数
	 */
	public int delete(String hf10Cd){
		final String sql = "DELETE FROM hf10_ichiran WHERE hf10_cd = ?";
		return connection.update(sql, hf10Cd);
	}
}
