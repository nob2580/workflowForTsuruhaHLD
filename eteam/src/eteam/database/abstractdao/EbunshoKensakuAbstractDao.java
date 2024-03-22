package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.EbunshoKensaku;

/**
 * @author j_matsumoto
 *
 */
public abstract class EbunshoKensakuAbstractDao extends EteamAbstractLogic {

	/**
	 * insert定型部
	 */
	protected final String insertSql = "INSERT INTO ebunsho_kensaku("
			+ "	 doc_type, item_no, nini_flg)"
			+ "	 VALUES (?, ?, ?);";

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected EbunshoKensaku mapToDto(GMap map){
		return map == null ? null : new EbunshoKensaku(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<EbunshoKensaku> mapToDto(List<GMap> mapList){
		List<EbunshoKensaku> dtoList = new ArrayList<EbunshoKensaku>();
		for (var map : mapList) {
			dtoList.add(new EbunshoKensaku(map));
		}
		return dtoList;
	}

	/**
	 * e文書検索設定のレコード有無を判定
	 * @param docType 文書種別
	 * @param itemNo 項目
	 * @return true:exist false:not exist
	 */
	public boolean exists(short docType, short itemNo) {
		return this.find(docType, itemNo) == null ? false : true;
	}

	/**
	 * e文書検索設定から主キー指定でレコードを取得
	 * @param docType 文書種別
	 * @param itemNo 項目
	 * @return e文書検索設定DTO
	 */
	public EbunshoKensaku find(short docType, short itemNo) {
		final String sql = "SELECT * FROM ebunsho_kensaku WHERE doc_type = ? AND item_no = ?";
		return mapToDto(connection.find(sql, docType, itemNo));
	}

	/**
	 * e文書検索設定からレコードを全件取得 ※大量データ取得に注意
	 * @return List<e文書検索設定DTO>
	 */
	public List<EbunshoKensaku> load() {
		final String sql = "SELECT * FROM ebunsho_kensaku ORDER BY doc_type, item_no";
		return mapToDto(connection.load(sql));
	}

	/**
	* e文書検索設定登録
	* @param dto e文書検索設定
	* @return 件数
	*/
	public int insert(
		EbunshoKensaku dto
	){
			return connection.update(this.insertSql,
					dto.docType, dto.itemNo, dto.niniFlg
					);
	}

	/**
	* e文書検索設定登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto e文書検索設定
	* @return 件数
	*/
	public int upsert(
		EbunshoKensaku dto
		 ){
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT ebunsho_kensaku_pkey "
			+ "DO UPDATE SET item_no = ?, nini_flg = ? ";
			return connection.update(sql,
				dto.docType, dto.itemNo, dto.niniFlg
				, dto.itemNo, dto.niniFlg
				);
    }

	/**
	 * e文書検索設定から主キー指定でレコードを削除
	 * @param docType 文書種別
	 * @param itemNo 項目
	 * @return 削除件数
	 */
	public int delete(short docType, short itemNo){
		final String sql = "DELETE FROM ebunsho_kensaku WHERE doc_type = ? AND item_no = ?";
		return connection.update(sql, docType, itemNo);
	}
}
