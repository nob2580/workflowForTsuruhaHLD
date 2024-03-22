package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KaigaiRyohiKaribarai;

/**
 * 海外旅費仮払に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KaigaiRyohiKaribaraiAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected KaigaiRyohiKaribarai mapToDto(GMap map){
		return map == null ? null : new KaigaiRyohiKaribarai(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<KaigaiRyohiKaribarai> mapToDto(List<GMap> mapList){
		List<KaigaiRyohiKaribarai> dtoList = new ArrayList<KaigaiRyohiKaribarai>();
		for (var map : mapList) {
			dtoList.add(new KaigaiRyohiKaribarai(map));
		}
		return dtoList;
	}
	
	/**
	 * 海外旅費仮払のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId) {
		return this.find(denpyouId) == null ? false : true;
	}
	
	/**
	 * 海外旅費仮払から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return 海外旅費仮払DTO
	 */
	public KaigaiRyohiKaribarai find(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai WHERE denpyou_id = ?";
		return mapToDto(connection.find(sql, denpyouId));
	}
	
	/**
	 * 海外旅費仮払からレコードを全件取得 ※大量データ取得に注意
	 * @return List<海外旅費仮払DTO>
	 */
	public List<KaigaiRyohiKaribarai> load() {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai ORDER BY denpyou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	* 海外旅費仮払登録
	* @param dto 海外旅費仮払
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int insert(
		KaigaiRyohiKaribarai dto
		,String koushinUserId
	){
		final String sql =
				"INSERT INTO kaigai_ryohi_karibarai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.karibaraiOn, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kingaku, dto.karibaraiKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.seisanKanryoubi, koushinUserId, koushinUserId
					);
	}

	/**
	* 海外旅費仮払の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したKaigaiRyohiKaribaraiの使用を前提
	* @param dto 海外旅費仮払
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int update(
		KaigaiRyohiKaribarai dto
		,String koushinUserId
		 ){
		final String sql =
				"UPDATE kaigai_ryohi_karibarai "
		    + "SET karibarai_on = ?, dairiflg = ?, user_id = ?, shain_no = ?, user_sei = ?, user_mei = ?, houmonsaki = ?, mokuteki = ?, seisankikan_from = ?, seisankikan_from_hour = ?, seisankikan_from_min = ?, seisankikan_to = ?, seisankikan_to_hour = ?, seisankikan_to_min = ?, shiharaibi = ?, shiharaikiboubi = ?, shiharaihouhou = ?, tekiyou = ?, kingaku = ?, karibarai_kingaku = ?, sashihiki_num = ?, sashihiki_tanka = ?, sashihiki_num_kaigai = ?, sashihiki_tanka_kaigai = ?, sashihiki_heishu_cd_kaigai = ?, sashihiki_rate_kaigai = ?, sashihiki_tanka_kaigai_gaika = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, seisan_kanryoubi = ?, koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE koushin_time = ? AND denpyou_id = ?";
			return connection.update(sql,
				dto.karibaraiOn, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kingaku, dto.karibaraiKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.seisanKanryoubi, koushinUserId
				,dto.koushinTime, dto.denpyouId);
    }

	/**
	* 海外旅費仮払登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 海外旅費仮払
	* @param koushinUserId 更新ユーザーID
	* @return 件数
	*/
	public int upsert(
		KaigaiRyohiKaribarai dto
		,String koushinUserId
		 ){
		final String sql =
				"INSERT INTO kaigai_ryohi_karibarai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp "
			+ ") ON CONFLICT ON CONSTRAINT kaigai_ryohi_karibarai_pkey "
			+ "DO UPDATE SET karibarai_on = ?, dairiflg = ?, user_id = ?, shain_no = ?, user_sei = ?, user_mei = ?, houmonsaki = ?, mokuteki = ?, seisankikan_from = ?, seisankikan_from_hour = ?, seisankikan_from_min = ?, seisankikan_to = ?, seisankikan_to_hour = ?, seisankikan_to_min = ?, shiharaibi = ?, shiharaikiboubi = ?, shiharaihouhou = ?, tekiyou = ?, kingaku = ?, karibarai_kingaku = ?, sashihiki_num = ?, sashihiki_tanka = ?, sashihiki_num_kaigai = ?, sashihiki_tanka_kaigai = ?, sashihiki_heishu_cd_kaigai = ?, sashihiki_rate_kaigai = ?, sashihiki_tanka_kaigai_gaika = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, seisan_kanryoubi = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.karibaraiOn, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kingaku, dto.karibaraiKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.seisanKanryoubi, koushinUserId, koushinUserId
				, dto.karibaraiOn, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kingaku, dto.karibaraiKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.seisanKanryoubi, koushinUserId
				);
    }
	
	/**
	 * 海外旅費仮払から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId){
		final String sql = "DELETE FROM kaigai_ryohi_karibarai WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}
}