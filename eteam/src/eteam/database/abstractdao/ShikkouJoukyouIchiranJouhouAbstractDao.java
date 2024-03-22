package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.ShikkouJoukyouIchiranJouhou;

/**
 * 執行状況一覧情報に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class ShikkouJoukyouIchiranJouhouAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected ShikkouJoukyouIchiranJouhou mapToDto(GMap map){
		return map == null ? null : new ShikkouJoukyouIchiranJouhou(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<ShikkouJoukyouIchiranJouhou> mapToDto(List<GMap> mapList){
		List<ShikkouJoukyouIchiranJouhou> dtoList = new ArrayList<ShikkouJoukyouIchiranJouhou>();
		for (var map : mapList) {
			dtoList.add(new ShikkouJoukyouIchiranJouhou(map));
		}
		return dtoList;
	}
	
	/**
	 * 執行状況一覧情報のレコード有無を判定
	 * @param userId ユーザーID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String userId) {
		return this.find(userId) == null ? false : true;
	}
	
	/**
	 * 執行状況一覧情報から主キー指定でレコードを取得
	 * @param userId ユーザーID
	 * @return 執行状況一覧情報DTO
	 */
	public ShikkouJoukyouIchiranJouhou find(String userId) {
		final String sql = "SELECT * FROM shikkou_joukyou_ichiran_jouhou WHERE user_id = ?";
		return mapToDto(connection.find(sql, userId));
	}
	
	/**
	 * 執行状況一覧情報からレコードを全件取得 ※大量データ取得に注意
	 * @return List<執行状況一覧情報DTO>
	 */
	public List<ShikkouJoukyouIchiranJouhou> load() {
		final String sql = "SELECT * FROM shikkou_joukyou_ichiran_jouhou ORDER BY user_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* 執行状況一覧情報登録
	* @param dto 執行状況一覧情報
	* @return 件数
	*/
	public int insert(
		ShikkouJoukyouIchiranJouhou dto
	){
		final String sql =
				"INSERT INTO shikkou_joukyou_ichiran_jouhou "
			+ "VALUES(?, ? "
			+ ")";
			return connection.update(sql,
					dto.userId, dto.yosanTani
					);
	}

	/**
	* 執行状況一覧情報登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 執行状況一覧情報
	* @return 件数
	*/
	public int upsert(
		ShikkouJoukyouIchiranJouhou dto
		 ){
		final String sql =
				"INSERT INTO shikkou_joukyou_ichiran_jouhou "
			+ "VALUES(?, ? "
			+ ") ON CONFLICT ON CONSTRAINT shikkou_joukyou_ichiran_jouhou_pkey "
			+ "DO UPDATE SET yosan_tani = ? "
			+ "";
			return connection.update(sql,
				dto.userId, dto.yosanTani
				, dto.yosanTani
				);
    }
	
	/**
	 * 執行状況一覧情報から主キー指定でレコードを削除
	 * @param userId ユーザーID
	 * @return 削除件数
	 */
	public int delete(String userId){
		final String sql = "DELETE FROM shikkou_joukyou_ichiran_jouhou WHERE user_id = ?";
		return connection.update(sql, userId);
	}
}
