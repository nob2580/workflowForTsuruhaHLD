package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KaigaiRyohiKaribaraiKeihiMeisai;

/**
 * 海外旅費仮払経費明細に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KaigaiRyohiKaribaraiKeihiMeisaiAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KaigaiRyohiKaribaraiKeihiMeisai mapToDto(GMap map){
		return map == null ? null : new KaigaiRyohiKaribaraiKeihiMeisai(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KaigaiRyohiKaribaraiKeihiMeisai> mapToDto(List<GMap> mapList){
		List<KaigaiRyohiKaribaraiKeihiMeisai> dtoList = new ArrayList<KaigaiRyohiKaribaraiKeihiMeisai>();
		for (var map : mapList) {
			dtoList.add(new KaigaiRyohiKaribaraiKeihiMeisai(map));
		}
		return dtoList;
	}
	
	/**
	 * 海外旅費仮払経費明細のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @param kaigaiFlg 海外フラグ
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId, int denpyouEdano, String kaigaiFlg) {
		return this.find(denpyouId, denpyouEdano, kaigaiFlg) == null ? false : true;
	}
	
	/**
	 * 海外旅費仮払経費明細から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @param kaigaiFlg 海外フラグ
	 * @return 海外旅費仮払経費明細DTO
	 */
	public KaigaiRyohiKaribaraiKeihiMeisai find(String denpyouId, int denpyouEdano, String kaigaiFlg) {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? AND denpyou_edano = ? AND kaigai_flg = ?";
		return mapToDto(connection.find(sql, denpyouId, denpyouEdano, kaigaiFlg));
	}
	
	/**
	 * 海外旅費仮払経費明細からレコードを全件取得 ※大量データ取得に注意
	 * @return List<海外旅費仮払経費明細DTO>
	 */
	public List<KaigaiRyohiKaribaraiKeihiMeisai> load() {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai_keihi_meisai ORDER BY denpyou_id, denpyou_edano, kaigai_flg";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 海外旅費仮払経費明細から一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return List<海外旅費仮払経費明細>DTO
	 */
	public List<KaigaiRyohiKaribaraiKeihiMeisai> load(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? "
							+ "ORDER BY denpyou_id, denpyou_edano, kaigai_flg";
		return mapToDto(connection.load(sql, denpyouId));
	}
	
	/**
	 * 海外旅費仮払経費明細から一部キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return List<海外旅費仮払経費明細>DTO
	 */
	public List<KaigaiRyohiKaribaraiKeihiMeisai> load(String denpyouId, int denpyouEdano) {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ?  AND denpyou_edano = ? "
							+ "ORDER BY denpyou_id, denpyou_edano, kaigai_flg";
		return mapToDto(connection.load(sql, denpyouId, denpyouEdano));
	}

	/**
	* 海外旅費仮払経費明細登録
	* @param dto 海外旅費仮払経費明細
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		KaigaiRyohiKaribaraiKeihiMeisai dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO kaigai_ryohi_karibarai_keihi_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.denpyouEdano, dto.kaigaiFlg, dto.shiwakeEdano, dto.shiyoubi, dto.shouhyouShoruiFlg, dto.torihikiName, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kazeiFlg, dto.shiharaiKingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kousaihiShousaiHyoujiFlg, dto.kousaihiNinzuuRiyouFlg, dto.kousaihiShousai, dto.kousaihiNinzuu, dto.kousaihiHitoriKingaku, dto.heishuCd, dto.rate, dto.gaika, dto.currencyUnit, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId, koushinUserId
					);
	}

	/**
	* 海外旅費仮払経費明細の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したKaigaiRyohiKaribaraiKeihiMeisaiの使用を前提
	* @param dto 海外旅費仮払経費明細
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		KaigaiRyohiKaribaraiKeihiMeisai dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE kaigai_ryohi_karibarai_keihi_meisai "
		    + "SET shiwake_edano = ?, shiyoubi = ?, shouhyou_shorui_flg = ?, torihiki_name = ?, tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, kazei_flg = ?, shiharai_kingaku = ?, hontai_kingaku = ?, shouhizeigaku = ?, kousaihi_shousai_hyouji_flg = ?, kousaihi_ninzuu_riyou_flg = ?, kousaihi_shousai = ?, kousaihi_ninzuu = ?, kousaihi_hitori_kingaku = ?, heishu_cd = ?, rate = ?, gaika = ?, currency_unit = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND denpyou_id = ? AND denpyou_edano = ? AND kaigai_flg = ?";
			return connection.update(sql,
				dto.shiwakeEdano, dto.shiyoubi, dto.shouhyouShoruiFlg, dto.torihikiName, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kazeiFlg, dto.shiharaiKingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kousaihiShousaiHyoujiFlg, dto.kousaihiNinzuuRiyouFlg, dto.kousaihiShousai, dto.kousaihiNinzuu, dto.kousaihiHitoriKingaku, dto.heishuCd, dto.rate, dto.gaika, dto.currencyUnit, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId
				,dto.koushinTime, dto.denpyouId, dto.denpyouEdano, dto.kaigaiFlg);
    }

	/**
	* 海外旅費仮払経費明細登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 海外旅費仮払経費明細
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		KaigaiRyohiKaribaraiKeihiMeisai dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO kaigai_ryohi_karibarai_keihi_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT kaigai_ryohi_karibarai_keihi_meisai_pkey "
			+ "DO UPDATE SET shiwake_edano = ?, shiyoubi = ?, shouhyou_shorui_flg = ?, torihiki_name = ?, tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, kazei_flg = ?, shiharai_kingaku = ?, hontai_kingaku = ?, shouhizeigaku = ?, kousaihi_shousai_hyouji_flg = ?, kousaihi_ninzuu_riyou_flg = ?, kousaihi_shousai = ?, kousaihi_ninzuu = ?, kousaihi_hitori_kingaku = ?, heishu_cd = ?, rate = ?, gaika = ?, currency_unit = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.denpyouEdano, dto.kaigaiFlg, dto.shiwakeEdano, dto.shiyoubi, dto.shouhyouShoruiFlg, dto.torihikiName, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kazeiFlg, dto.shiharaiKingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kousaihiShousaiHyoujiFlg, dto.kousaihiNinzuuRiyouFlg, dto.kousaihiShousai, dto.kousaihiNinzuu, dto.kousaihiHitoriKingaku, dto.heishuCd, dto.rate, dto.gaika, dto.currencyUnit, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId, koushinUserId
				, dto.shiwakeEdano, dto.shiyoubi, dto.shouhyouShoruiFlg, dto.torihikiName, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kazeiFlg, dto.shiharaiKingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kousaihiShousaiHyoujiFlg, dto.kousaihiNinzuuRiyouFlg, dto.kousaihiShousai, dto.kousaihiNinzuu, dto.kousaihiHitoriKingaku, dto.heishuCd, dto.rate, dto.gaika, dto.currencyUnit, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId
				);
    }
	
	/**
	 * 海外旅費仮払経費明細から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @param kaigaiFlg 海外フラグ
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int denpyouEdano, String kaigaiFlg){
		final String sql = "DELETE FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? AND denpyou_edano = ? AND kaigai_flg = ?";
		return connection.update(sql, denpyouId, denpyouEdano, kaigaiFlg);
	}
	
	/**
	 * 海外旅費仮払経費明細から一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 海外旅費仮払経費明細から一部キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int denpyouEdano) {
		final String sql = "DELETE FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ?  AND denpyou_edano = ? ";
		return connection.update(sql, denpyouId, denpyouEdano);
	}
}
