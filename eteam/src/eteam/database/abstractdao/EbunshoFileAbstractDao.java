package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.EbunshoFile;

/**
 * e文書ファイルに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class EbunshoFileAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected EbunshoFile mapToDto(GMap map){
		return map == null ? null : new EbunshoFile(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<EbunshoFile> mapToDto(List<GMap> mapList){
		List<EbunshoFile> dtoList = new ArrayList<EbunshoFile>();
		for (var map : mapList) {
			dtoList.add(new EbunshoFile(map));
		}
		return dtoList;
	}
	
	/**
	 * e文書ファイルのレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId, int edano) {
		return this.find(denpyouId, edano) == null ? false : true;
	}
	
	/**
	 * e文書ファイルから主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @return e文書ファイルDTO
	 */
	public EbunshoFile find(String denpyouId, int edano) {
		final String sql = "SELECT * FROM ebunsho_file WHERE denpyou_id = ? AND edano = ?";
		return mapToDto(connection.find(sql, denpyouId, edano));
	}
	
	/**
	 * e文書ファイルからレコードを全件取得 ※大量データ取得に注意
	 * @return List<e文書ファイルDTO>
	 */
	public List<EbunshoFile> load() {
		final String sql = "SELECT * FROM ebunsho_file ORDER BY denpyou_id, edano";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * e文書ファイルから一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return List<e文書ファイル>DTO
	 */
	public List<EbunshoFile> load(String denpyouId) {
		final String sql = "SELECT * FROM ebunsho_file WHERE denpyou_id = ? "
							+ "ORDER BY denpyou_id, edano";
		return mapToDto(connection.load(sql, denpyouId));
	}

	/**
	* e文書ファイル登録
	* @param dto e文書ファイル
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		EbunshoFile dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO ebunsho_file "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.edano, dto.ebunshoNo, dto.binaryData, dto.denshitorihikiFlg, dto.tsfuyoFlg, koushinUserId
					);
	}

	/**
	* e文書ファイル登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto e文書ファイル
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		EbunshoFile dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO ebunsho_file "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT ebunsho_file_pkey "
			+ "DO UPDATE SET ebunsho_no = ?, binary_data = ?, denshitorihiki_flg = ?, tsfuyo_flg = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.edano, dto.ebunshoNo, dto.binaryData, dto.denshitorihikiFlg, dto.tsfuyoFlg, koushinUserId
				, dto.ebunshoNo, dto.binaryData, dto.denshitorihikiFlg, dto.tsfuyoFlg
				);
    }
	
	/**
	 * e文書ファイルから主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int edano){
		final String sql = "DELETE FROM ebunsho_file WHERE denpyou_id = ? AND edano = ?";
		return connection.update(sql, denpyouId, edano);
	}
	
	/**
	 * e文書ファイルから一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM ebunsho_file WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
}
