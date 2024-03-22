package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.GamenKoumokuSeigyo;

/**
 * 画面項目制御に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class GamenKoumokuSeigyoAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected GamenKoumokuSeigyo mapToDto(GMap map){
		return map == null ? null : new GamenKoumokuSeigyo(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<GamenKoumokuSeigyo> mapToDto(List<GMap> mapList){
		List<GamenKoumokuSeigyo> dtoList = new ArrayList<GamenKoumokuSeigyo>();
		for (var map : mapList) {
			dtoList.add(new GamenKoumokuSeigyo(map));
		}
		return dtoList;
	}
	
	/**
	 * 画面項目制御のレコード有無を判定
	 * @param denpyouKbn 伝票区分
	 * @param koumokuId 項目ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouKbn, String koumokuId) {
		return this.find(denpyouKbn, koumokuId) == null ? false : true;
	}
	
	/**
	 * 画面項目制御から主キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @param koumokuId 項目ID
	 * @return 画面項目制御DTO
	 */
	public GamenKoumokuSeigyo find(String denpyouKbn, String koumokuId) {
		final String sql = "SELECT * FROM gamen_koumoku_seigyo WHERE denpyou_kbn = ? AND koumoku_id = ?";
		return mapToDto(connection.find(sql, denpyouKbn, koumokuId));
	}
	
	/**
	 * 画面項目制御からレコードを全件取得 ※大量データ取得に注意
	 * @return List<画面項目制御DTO>
	 */
	public List<GamenKoumokuSeigyo> load() {
		final String sql = "SELECT * FROM gamen_koumoku_seigyo ORDER BY denpyou_kbn, koumoku_id";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 画面項目制御から一部キー指定でレコードを取得
	 * @param denpyouKbn 伝票区分
	 * @return List<画面項目制御>DTO
	 */
	public List<GamenKoumokuSeigyo> load(String denpyouKbn) {
		final String sql = "SELECT * FROM gamen_koumoku_seigyo WHERE denpyou_kbn = ? "
							+ "ORDER BY denpyou_kbn, koumoku_id";
		return mapToDto(connection.load(sql, denpyouKbn));
	}

	/**
	* 画面項目制御登録
	* @param dto 画面項目制御
	* @return 件数
	*/
	public int insert(
		GamenKoumokuSeigyo dto
	){
		final String sql =
				"INSERT INTO gamen_koumoku_seigyo "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouKbn, dto.koumokuId, dto.defaultKoumokuName, dto.koumokuName, dto.hyoujiFlg, dto.hissuFlg, dto.hyoujiSeigyoFlg, dto.pdfHyoujiFlg, dto.pdfHyoujiSeigyoFlg, dto.codeOutputFlg, dto.codeOutputSeigyoFlg, dto.hyoujiJun
					);
	}

	/**
	* 画面項目制御登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 画面項目制御
	* @return 件数
	*/
	public int upsert(
		GamenKoumokuSeigyo dto
		 ){
		final String sql =
				"INSERT INTO gamen_koumoku_seigyo "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT gamen_koumoku_seigyo_pkey "
			+ "DO UPDATE SET default_koumoku_name = ?, koumoku_name = ?, hyouji_flg = ?, hissu_flg = ?, hyouji_seigyo_flg = ?, pdf_hyouji_flg = ?, pdf_hyouji_seigyo_flg = ?, code_output_flg = ?, code_output_seigyo_flg = ?, hyouji_jun = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouKbn, dto.koumokuId, dto.defaultKoumokuName, dto.koumokuName, dto.hyoujiFlg, dto.hissuFlg, dto.hyoujiSeigyoFlg, dto.pdfHyoujiFlg, dto.pdfHyoujiSeigyoFlg, dto.codeOutputFlg, dto.codeOutputSeigyoFlg, dto.hyoujiJun
				, dto.defaultKoumokuName, dto.koumokuName, dto.hyoujiFlg, dto.hissuFlg, dto.hyoujiSeigyoFlg, dto.pdfHyoujiFlg, dto.pdfHyoujiSeigyoFlg, dto.codeOutputFlg, dto.codeOutputSeigyoFlg, dto.hyoujiJun
				);
    }
	
	/**
	 * 画面項目制御から主キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @param koumokuId 項目ID
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn, String koumokuId){
		final String sql = "DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn = ? AND koumoku_id = ?";
		return connection.update(sql, denpyouKbn, koumokuId);
	}
	
	/**
	 * 画面項目制御から一部キー指定でレコードを削除
	 * @param denpyouKbn 伝票区分
	 * @return 削除件数
	 */
	public int delete(String denpyouKbn) {
		final String sql = "DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn = ? ";
		return connection.update(sql, denpyouKbn);
	}
}
