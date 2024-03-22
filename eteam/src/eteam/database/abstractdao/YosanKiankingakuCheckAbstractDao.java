package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.YosanKiankingakuCheck;

/**
 * 予算・起案金額チェックに対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class YosanKiankingakuCheckAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected YosanKiankingakuCheck mapToDto(GMap map){
		return map == null ? null : new YosanKiankingakuCheck(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<YosanKiankingakuCheck> mapToDto(List<GMap> mapList){
		List<YosanKiankingakuCheck> dtoList = new ArrayList<YosanKiankingakuCheck>();
		for (var map : mapList) {
			dtoList.add(new YosanKiankingakuCheck(map));
		}
		return dtoList;
	}
	
	/**
	 * 予算・起案金額チェックのレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @param futanBumonCd 負担部門コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd, String kamokuEdabanCd, String futanBumonCd) {
		return this.find(denpyouId, syuukeiBumonCd, kamokuGaibuCd, kamokuEdabanCd, futanBumonCd) == null ? false : true;
	}
	
	/**
	 * 予算・起案金額チェックから主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @param futanBumonCd 負担部門コード
	 * @return 予算・起案金額チェックDTO
	 */
	public YosanKiankingakuCheck find(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd, String kamokuEdabanCd, String futanBumonCd) {
		final String sql = "SELECT * FROM yosan_kiankingaku_check WHERE denpyou_id = ? AND syuukei_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kamoku_edaban_cd = ? AND futan_bumon_cd = ?";
		return mapToDto(connection.find(sql, denpyouId, syuukeiBumonCd, kamokuGaibuCd, kamokuEdabanCd, futanBumonCd));
	}
	
	/**
	 * 予算・起案金額チェックからレコードを全件取得 ※大量データ取得に注意
	 * @return List<予算・起案金額チェックDTO>
	 */
	public List<YosanKiankingakuCheck> load() {
		final String sql = "SELECT * FROM yosan_kiankingaku_check ORDER BY denpyou_id, syuukei_bumon_cd, kamoku_gaibu_cd, kamoku_edaban_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return List<予算・起案金額チェック>DTO
	 */
	public List<YosanKiankingakuCheck> load(String denpyouId) {
		final String sql = "SELECT * FROM yosan_kiankingaku_check WHERE denpyou_id = ? "
							+ "ORDER BY denpyou_id, syuukei_bumon_cd, kamoku_gaibu_cd, kamoku_edaban_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql, denpyouId));
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @return List<予算・起案金額チェック>DTO
	 */
	public List<YosanKiankingakuCheck> load(String denpyouId, String syuukeiBumonCd) {
		final String sql = "SELECT * FROM yosan_kiankingaku_check WHERE denpyou_id = ?  AND syuukei_bumon_cd = ? "
							+ "ORDER BY denpyou_id, syuukei_bumon_cd, kamoku_gaibu_cd, kamoku_edaban_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql, denpyouId, syuukeiBumonCd));
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return List<予算・起案金額チェック>DTO
	 */
	public List<YosanKiankingakuCheck> load(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd) {
		final String sql = "SELECT * FROM yosan_kiankingaku_check WHERE denpyou_id = ?  AND syuukei_bumon_cd = ?  AND kamoku_gaibu_cd = ? "
							+ "ORDER BY denpyou_id, syuukei_bumon_cd, kamoku_gaibu_cd, kamoku_edaban_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql, denpyouId, syuukeiBumonCd, kamokuGaibuCd));
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @return List<予算・起案金額チェック>DTO
	 */
	public List<YosanKiankingakuCheck> load(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd, String kamokuEdabanCd) {
		final String sql = "SELECT * FROM yosan_kiankingaku_check WHERE denpyou_id = ?  AND syuukei_bumon_cd = ?  AND kamoku_gaibu_cd = ?  AND kamoku_edaban_cd = ? "
							+ "ORDER BY denpyou_id, syuukei_bumon_cd, kamoku_gaibu_cd, kamoku_edaban_cd, futan_bumon_cd";
		return mapToDto(connection.load(sql, denpyouId, syuukeiBumonCd, kamokuGaibuCd, kamokuEdabanCd));
	}

	/**
	* 予算・起案金額チェック登録
	* @param dto 予算・起案金額チェック
	* @return 件数
	*/
	public int insert(
		YosanKiankingakuCheck dto
	){
		final String sql =
				"INSERT INTO yosan_kiankingaku_check "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.syuukeiBumonCd, dto.kamokuGaibuCd, dto.kamokuEdabanCd, dto.futanBumonCd, dto.syuukeiBumonName, dto.kamokuNameRyakushiki, dto.edabanName, dto.futanBumonName, dto.kijunKingaku, dto.jissekigaku, dto.shinseiKingaku
					);
	}

	/**
	* 予算・起案金額チェック登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 予算・起案金額チェック
	* @return 件数
	*/
	public int upsert(
		YosanKiankingakuCheck dto
		 ){
		final String sql =
				"INSERT INTO yosan_kiankingaku_check "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT yosan_kiankingaku_check_pkey "
			+ "DO UPDATE SET syuukei_bumon_name = ?, kamoku_name_ryakushiki = ?, edaban_name = ?, futan_bumon_name = ?, kijun_kingaku = ?, jissekigaku = ?, shinsei_kingaku = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.syuukeiBumonCd, dto.kamokuGaibuCd, dto.kamokuEdabanCd, dto.futanBumonCd, dto.syuukeiBumonName, dto.kamokuNameRyakushiki, dto.edabanName, dto.futanBumonName, dto.kijunKingaku, dto.jissekigaku, dto.shinseiKingaku
				, dto.syuukeiBumonName, dto.kamokuNameRyakushiki, dto.edabanName, dto.futanBumonName, dto.kijunKingaku, dto.jissekigaku, dto.shinseiKingaku
				);
    }
	
	/**
	 * 予算・起案金額チェックから主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @param futanBumonCd 負担部門コード
	 * @return 削除件数
	 */
	public int delete(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd, String kamokuEdabanCd, String futanBumonCd){
		final String sql = "DELETE FROM yosan_kiankingaku_check WHERE denpyou_id = ? AND syuukei_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kamoku_edaban_cd = ? AND futan_bumon_cd = ?";
		return connection.update(sql, denpyouId, syuukeiBumonCd, kamokuGaibuCd, kamokuEdabanCd, futanBumonCd);
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM yosan_kiankingaku_check WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 削除件数
	 */
	public int delete(String denpyouId, String syuukeiBumonCd) {
		final String sql = "DELETE FROM yosan_kiankingaku_check WHERE denpyou_id = ?  AND syuukei_bumon_cd = ? ";
		return connection.update(sql, denpyouId, syuukeiBumonCd);
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return 削除件数
	 */
	public int delete(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd) {
		final String sql = "DELETE FROM yosan_kiankingaku_check WHERE denpyou_id = ?  AND syuukei_bumon_cd = ?  AND kamoku_gaibu_cd = ? ";
		return connection.update(sql, denpyouId, syuukeiBumonCd, kamokuGaibuCd);
	}
	
	/**
	 * 予算・起案金額チェックから一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param syuukeiBumonCd 集計部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @return 削除件数
	 */
	public int delete(String denpyouId, String syuukeiBumonCd, String kamokuGaibuCd, String kamokuEdabanCd) {
		final String sql = "DELETE FROM yosan_kiankingaku_check WHERE denpyou_id = ?  AND syuukei_bumon_cd = ?  AND kamoku_gaibu_cd = ?  AND kamoku_edaban_cd = ? ";
		return connection.update(sql, denpyouId, syuukeiBumonCd, kamokuGaibuCd, kamokuEdabanCd);
	}
}