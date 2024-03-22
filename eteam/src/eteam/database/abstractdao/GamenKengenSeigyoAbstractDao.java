package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.GamenKengenSeigyo;

/**
 * 画面権限制御に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class GamenKengenSeigyoAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected GamenKengenSeigyo mapToDto(GMap map){
		return map == null ? null : new GamenKengenSeigyo(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<GamenKengenSeigyo> mapToDto(List<GMap> mapList){
		List<GamenKengenSeigyo> dtoList = new ArrayList<GamenKengenSeigyo>();
		for (var map : mapList) {
			dtoList.add(new GamenKengenSeigyo(map));
		}
		return dtoList;
	}
	
	/**
	 * 画面権限制御のレコード有無を判定
	 * @param gamenId 画面ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String gamenId) {
		return this.find(gamenId) == null ? false : true;
	}
	
	/**
	 * 画面権限制御から主キー指定でレコードを取得
	 * @param gamenId 画面ID
	 * @return 画面権限制御DTO
	 */
	public GamenKengenSeigyo find(String gamenId) {
		final String sql = "SELECT * FROM gamen_kengen_seigyo WHERE gamen_id = ?";
		return mapToDto(connection.find(sql, gamenId));
	}
	
	/**
	 * 画面権限制御からレコードを全件取得 ※大量データ取得に注意
	 * @return List<画面権限制御DTO>
	 */
	public List<GamenKengenSeigyo> load() {
		final String sql = "SELECT * FROM gamen_kengen_seigyo ORDER BY gamen_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* 画面権限制御登録
	* @param dto 画面権限制御
	* @return 件数
	*/
	public int insert(
		GamenKengenSeigyo dto
	){
		final String sql =
				"INSERT INTO gamen_kengen_seigyo "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.gamenId, dto.gamenName, dto.bumonShozokuRiyoukanouFlg, dto.systemKanriRiyoukanouFlg, dto.workflowRiyoukanouFlg, dto.kaishasetteiRiyoukanouFlg, dto.keirishoriRiyoukanouFlg, dto.kinouSeigyoCd
					);
	}

	/**
	* 画面権限制御登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 画面権限制御
	* @return 件数
	*/
	public int upsert(
		GamenKengenSeigyo dto
		 ){
		final String sql =
				"INSERT INTO gamen_kengen_seigyo "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT gamen_kengen_seigyo_pkey "
			+ "DO UPDATE SET gamen_name = ?, bumon_shozoku_riyoukanou_flg = ?, system_kanri_riyoukanou_flg = ?, workflow_riyoukanou_flg = ?, kaishasettei_riyoukanou_flg = ?, keirishori_riyoukanou_flg = ?, kinou_seigyo_cd = ? "
			+ "";
			return connection.update(sql,
				dto.gamenId, dto.gamenName, dto.bumonShozokuRiyoukanouFlg, dto.systemKanriRiyoukanouFlg, dto.workflowRiyoukanouFlg, dto.kaishasetteiRiyoukanouFlg, dto.keirishoriRiyoukanouFlg, dto.kinouSeigyoCd
				, dto.gamenName, dto.bumonShozokuRiyoukanouFlg, dto.systemKanriRiyoukanouFlg, dto.workflowRiyoukanouFlg, dto.kaishasetteiRiyoukanouFlg, dto.keirishoriRiyoukanouFlg, dto.kinouSeigyoCd
				);
    }
	
	/**
	 * 画面権限制御から主キー指定でレコードを削除
	 * @param gamenId 画面ID
	 * @return 削除件数
	 */
	public int delete(String gamenId){
		final String sql = "DELETE FROM gamen_kengen_seigyo WHERE gamen_id = ?";
		return connection.update(sql, gamenId);
	}
}
