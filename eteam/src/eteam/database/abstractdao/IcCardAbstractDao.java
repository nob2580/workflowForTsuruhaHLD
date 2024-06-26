package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.IcCard;

/**
 * ICカードに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class IcCardAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected IcCard mapToDto(GMap map){
		return map == null ? null : new IcCard(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<IcCard> mapToDto(List<GMap> mapList){
		List<IcCard> dtoList = new ArrayList<IcCard>();
		for (var map : mapList) {
			dtoList.add(new IcCard(map));
		}
		return dtoList;
	}
	
	/**
	 * ICカードのレコード有無を判定
	 * @param icCardNo ICカード番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String icCardNo) {
		return this.find(icCardNo) == null ? false : true;
	}
	
	/**
	 * ICカードから主キー指定でレコードを取得
	 * @param icCardNo ICカード番号
	 * @return ICカードDTO
	 */
	public IcCard find(String icCardNo) {
		final String sql = "SELECT * FROM ic_card WHERE ic_card_no = ?";
		return mapToDto(connection.find(sql, icCardNo));
	}
	
	/**
	 * ICカードからレコードを全件取得 ※大量データ取得に注意
	 * @return List<ICカードDTO>
	 */
	public List<IcCard> load() {
		final String sql = "SELECT * FROM ic_card ORDER BY ic_card_no";
		return mapToDto(connection.load(sql));
	}

	/**
	* ICカード登録
	* @param dto ICカード
	* @return 件数
	*/
	public int insert(
		IcCard dto
	){
		final String sql =
				"INSERT INTO ic_card "
			+ "VALUES(?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.icCardNo, dto.token, dto.userId
					);
	}

	/**
	* ICカード登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto ICカード
	* @return 件数
	*/
	public int upsert(
		IcCard dto
		 ){
		final String sql =
				"INSERT INTO ic_card "
			+ "VALUES(?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT ic_card_pkey "
			+ "DO UPDATE SET token = ?, user_id = ? "
			+ "";
			return connection.update(sql,
				dto.icCardNo, dto.token, dto.userId
				, dto.token, dto.userId
				);
    }
	
	/**
	 * ICカードから主キー指定でレコードを削除
	 * @param icCardNo ICカード番号
	 * @return 削除件数
	 */
	public int delete(String icCardNo){
		final String sql = "DELETE FROM ic_card WHERE ic_card_no = ?";
		return connection.update(sql, icCardNo);
	}
}
