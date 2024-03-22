package eteam.database.abstractdao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Shouhizeiritsu;

/**
 * 消費税率に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ShouhizeiritsuAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Shouhizeiritsu mapToDto(GMap map){
		return map == null ? null : new Shouhizeiritsu(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Shouhizeiritsu> mapToDto(List<GMap> mapList){
		List<Shouhizeiritsu> dtoList = new ArrayList<Shouhizeiritsu>();
		for (var map : mapList) {
			dtoList.add(new Shouhizeiritsu(map));
		}
		return dtoList;
	}
	
	/**
	 * 消費税率のレコード有無を判定
	 * @param sortJun 並び順
	 * @param zeiritsu 税率
	 * @return true:exist false:not exist
	 */
	public boolean exists(String sortJun, BigDecimal zeiritsu) {
		return this.find(sortJun, zeiritsu) == null ? false : true;
	}
	
	/**
	 * 消費税率から主キー指定でレコードを取得
	 * @param sortJun 並び順
	 * @param zeiritsu 税率
	 * @return 消費税率DTO
	 */
	public Shouhizeiritsu find(String sortJun, BigDecimal zeiritsu) {
		final String sql = "SELECT * FROM shouhizeiritsu WHERE sort_jun = ? AND zeiritsu = ?";
		return mapToDto(connection.find(sql, sortJun, zeiritsu));
	}
	
	/**
	 * 消費税率からレコードを全件取得 ※大量データ取得に注意
	 * @return List<消費税率DTO>
	 */
	public List<Shouhizeiritsu> load() {
		final String sql = "SELECT * FROM shouhizeiritsu ORDER BY sort_jun, zeiritsu";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 消費税率から一部キー指定でレコードを取得
	 * @param sortJun 並び順
	 * @return List<消費税率>DTO
	 */
	public List<Shouhizeiritsu> load(String sortJun) {
		final String sql = "SELECT * FROM shouhizeiritsu WHERE sort_jun = ? "
							+ "ORDER BY sort_jun, zeiritsu";
		return mapToDto(connection.load(sql, sortJun));
	}

	/**
	 * @return 軽減税率以外の税率
	 */
	public List<Shouhizeiritsu> loadNormalZeiritsu(){
		final String sql = "SELECT * FROM shouhizeiritsu WHERE keigen_zeiritsu_kbn = '0' "
				+ "ORDER BY sort_jun, zeiritsu";
		return mapToDto(connection.load(sql));
	}

	/**
	* 消費税率登録
	* @param dto 消費税率
	* @return 件数
	*/
	public int insert(
		Shouhizeiritsu dto
	){
		final String sql =
				"INSERT INTO shouhizeiritsu "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.sortJun, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.hasuuKeisanKbn, dto.yuukouKigenFrom, dto.yuukouKigenTo
					);
	}

	/**
	* 消費税率登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 消費税率
	* @return 件数
	*/
	public int upsert(
		Shouhizeiritsu dto
		 ){
		final String sql =
				"INSERT INTO shouhizeiritsu "
			+ "VALUES(?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT shouhizeiritsu_pkey "
			+ "DO UPDATE SET keigen_zeiritsu_kbn = ?, hasuu_keisan_kbn = ?, yuukou_kigen_from = ?, yuukou_kigen_to = ? "
			+ "";
			return connection.update(sql,
				dto.sortJun, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.hasuuKeisanKbn, dto.yuukouKigenFrom, dto.yuukouKigenTo
				, dto.keigenZeiritsuKbn, dto.hasuuKeisanKbn, dto.yuukouKigenFrom, dto.yuukouKigenTo
				);
    }
	
	/**
	 * 消費税率から主キー指定でレコードを削除
	 * @param sortJun 並び順
	 * @param zeiritsu 税率
	 * @return 削除件数
	 */
	public int delete(String sortJun, BigDecimal zeiritsu){
		final String sql = "DELETE FROM shouhizeiritsu WHERE sort_jun = ? AND zeiritsu = ?";
		return connection.update(sql, sortJun, zeiritsu);
	}
	
	/**
	 * 消費税率から一部キー指定でレコードを削除
	 * @param sortJun 並び順
	 * @return 削除件数
	 */
	public int delete(String sortJun) {
		final String sql = "DELETE FROM shouhizeiritsu WHERE sort_jun = ? ";
		return connection.update(sql, sortJun);
	}
}
