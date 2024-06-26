// AbstractDaoクラスのコードを自動生成します。importなども基本的なものは含めています。不要なら適宜除去してください。
// 以上のコメントは業務には不要なメモなので適宜除去してください。

package eteam.database.abstractdao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.KaigaiRyohiseisan;

/**
 * 海外旅費精算に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class KaigaiRyohiseisanAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO kaigai_ryohiseisan (denpyou_id, karibarai_denpyou_id, karibarai_on, karibarai_mishiyou_flg, shucchou_chuushi_flg, dairiflg, user_id, shain_no, user_sei, user_mei, houmonsaki, mokuteki, seisankikan_from, seisankikan_from_hour, seisankikan_from_min, seisankikan_to, seisankikan_to_hour, seisankikan_to_min, keijoubi, shiharaibi, shiharaikiboubi, shiharaihouhou, tekiyou, kaigai_tekiyou, zeiritsu, keigen_zeiritsu_kbn, goukei_kingaku, houjin_card_riyou_kingaku, kaisha_tehai_kingaku, sashihiki_shikyuu_kingaku, sashihiki_num, sashihiki_tanka, sashihiki_num_kaigai, sashihiki_tanka_kaigai, sashihiki_heishu_cd_kaigai, sashihiki_rate_kaigai, sashihiki_tanka_kaigai_gaika, hf1_cd, hf1_name_ryakushiki, hf2_cd, hf2_name_ryakushiki, hf3_cd, hf3_name_ryakushiki, hf4_cd, hf4_name_ryakushiki, hf5_cd, hf5_name_ryakushiki, hf6_cd, hf6_name_ryakushiki, hf7_cd, hf7_name_ryakushiki, hf8_cd, hf8_name_ryakushiki, hf9_cd, hf9_name_ryakushiki, hf10_cd, hf10_name_ryakushiki, hosoku, shiwake_edano, torihiki_name, kari_futan_bumon_cd, kari_futan_bumon_name, torihikisaki_cd, torihikisaki_name_ryakushiki, kari_kamoku_cd, kari_kamoku_name, kari_kamoku_edaban_cd, kari_kamoku_edaban_name, kari_kazei_kbn, ryohi_kazei_flg, kashi_futan_bumon_cd, kashi_futan_bumon_name, kashi_kamoku_cd, kashi_kamoku_name, kashi_kamoku_edaban_cd, kashi_kamoku_edaban_name, kashi_kazei_kbn, uf1_cd, uf1_name_ryakushiki, uf2_cd, uf2_name_ryakushiki, uf3_cd, uf3_name_ryakushiki, uf4_cd, uf4_name_ryakushiki, uf5_cd, uf5_name_ryakushiki, uf6_cd, uf6_name_ryakushiki, uf7_cd, uf7_name_ryakushiki, uf8_cd, uf8_name_ryakushiki, uf9_cd, uf9_name_ryakushiki, uf10_cd, uf10_name_ryakushiki, project_cd, project_name, segment_cd, segment_name_ryakushiki, tekiyou_cd, kaigai_shiwake_edano, kaigai_torihiki_name, kaigai_kari_futan_bumon_cd, kaigai_kari_futan_bumon_name, kaigai_torihikisaki_cd, kaigai_torihikisaki_name_ryakushiki, kaigai_kari_kamoku_cd, kaigai_kari_kamoku_name, kaigai_kari_kamoku_edaban_cd, kaigai_kari_kamoku_edaban_name, kaigai_kari_kazei_kbn, kaigai_kazei_flg, kaigai_kashi_futan_bumon_cd, kaigai_kashi_futan_bumon_name, kaigai_kashi_kamoku_cd, kaigai_kashi_kamoku_name, kaigai_kashi_kamoku_edaban_cd, kaigai_kashi_kamoku_edaban_name, kaigai_kashi_kazei_kbn, kaigai_uf1_cd, kaigai_uf1_name_ryakushiki, kaigai_uf2_cd, kaigai_uf2_name_ryakushiki, kaigai_uf3_cd, kaigai_uf3_name_ryakushiki, kaigai_uf4_cd, kaigai_uf4_name_ryakushiki, kaigai_uf5_cd, kaigai_uf5_name_ryakushiki, kaigai_uf6_cd, kaigai_uf6_name_ryakushiki, kaigai_uf7_cd, kaigai_uf7_name_ryakushiki, kaigai_uf8_cd, kaigai_uf8_name_ryakushiki, kaigai_uf9_cd, kaigai_uf9_name_ryakushiki, kaigai_uf10_cd, kaigai_uf10_name_ryakushiki, kaigai_project_cd, kaigai_project_name, kaigai_segment_cd, kaigai_segment_name_ryakushiki, kaigai_tekiyou_cd, touroku_user_id, touroku_time, koushin_user_id, koushin_time, bunri_kbn, kari_shiire_kbn, kashi_shiire_kbn, kaigai_bunri_kbn, kaigai_kari_shiire_kbn, kaigai_kashi_shiire_kbn, invoice_denpyou) VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 主キー条件定型部
	 */
	protected final String whereSql = " WHERE denpyou_id = ?";

	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected KaigaiRyohiseisan mapToDto(GMap map) {
		return map == null ? null : new KaigaiRyohiseisan(map);
	}

	/**
	 * @param mapList 検索結果GMap
	 * @return dtoList
	 */
	protected List<KaigaiRyohiseisan> mapToDto(List<GMap> mapList) {
		List<KaigaiRyohiseisan> dtoList = new ArrayList<KaigaiRyohiseisan>();
		for (var map : mapList) {
			dtoList.add(new KaigaiRyohiseisan(map));
		}
		return dtoList;
	}

	/**
	 * 海外旅費精算のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId) {
		return this.find(denpyouId) != null;
	}

	/**
	 * 海外旅費精算から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return 海外旅費精算DTO
	 */
	public KaigaiRyohiseisan find(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohiseisan " + this.whereSql;
		return mapToDto(connection.find(sql, denpyouId));
	}

	/**
	 * 海外旅費精算からレコードを全件取得 ※大量データ取得に注意
	 * @return List<海外旅費精算DTO>
	 */
	public List<KaigaiRyohiseisan> load() {
		final String sql = "SELECT * FROM kaigai_ryohiseisan  ORDER BY denpyou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	 * 海外旅費精算登録
	 * @param dto 海外旅費精算
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int insert(KaigaiRyohiseisan dto, String koushinUserId) {
		return connection.update(this.insertSql, dto.denpyouId, dto.karibaraiDenpyouId, dto.karibaraiOn, dto.karibaraiMishiyouFlg, dto.shucchouChuushiFlg, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.keijoubi, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kaigaiTekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.goukeiKingaku, dto.houjinCardRiyouKingaku, dto.kaishaTehaiKingaku, dto.sashihikiShikyuuKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.ryohiKazeiFlg, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.kaigaiShiwakeEdano, dto.kaigaiTorihikiName, dto.kaigaiKariFutanBumonCd, dto.kaigaiKariFutanBumonName, dto.kaigaiTorihikisakiCd, dto.kaigaiTorihikisakiNameRyakushiki, dto.kaigaiKariKamokuCd, dto.kaigaiKariKamokuName, dto.kaigaiKariKamokuEdabanCd, dto.kaigaiKariKamokuEdabanName, dto.kaigaiKariKazeiKbn, dto.kaigaiKazeiFlg, dto.kaigaiKashiFutanBumonCd, dto.kaigaiKashiFutanBumonName, dto.kaigaiKashiKamokuCd, dto.kaigaiKashiKamokuName, dto.kaigaiKashiKamokuEdabanCd, dto.kaigaiKashiKamokuEdabanName, dto.kaigaiKashiKazeiKbn, dto.kaigaiUf1Cd, dto.kaigaiUf1NameRyakushiki, dto.kaigaiUf2Cd, dto.kaigaiUf2NameRyakushiki, dto.kaigaiUf3Cd, dto.kaigaiUf3NameRyakushiki, dto.kaigaiUf4Cd, dto.kaigaiUf4NameRyakushiki, dto.kaigaiUf5Cd, dto.kaigaiUf5NameRyakushiki, dto.kaigaiUf6Cd, dto.kaigaiUf6NameRyakushiki, dto.kaigaiUf7Cd, dto.kaigaiUf7NameRyakushiki, dto.kaigaiUf8Cd, dto.kaigaiUf8NameRyakushiki, dto.kaigaiUf9Cd, dto.kaigaiUf9NameRyakushiki, dto.kaigaiUf10Cd, dto.kaigaiUf10NameRyakushiki, dto.kaigaiProjectCd, dto.kaigaiProjectName, dto.kaigaiSegmentCd, dto.kaigaiSegmentNameRyakushiki, dto.kaigaiTekiyouCd, koushinUserId, koushinUserId, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.kaigaiBunriKbn, dto.kaigaiKariShiireKbn, dto.kaigaiKashiShiireKbn, dto.invoiceDenpyou);
	}

	/**
	 * 海外旅費精算更新
	 * @param dto 海外旅費精算
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int update(KaigaiRyohiseisan dto, String koushinUserId) {
		final String sql = "UPDATE kaigai_ryohiseisan SET  karibarai_denpyou_id = ?, karibarai_on = ?, karibarai_mishiyou_flg = ?, shucchou_chuushi_flg = ?, dairiflg = ?, user_id = ?, shain_no = ?, user_sei = ?, user_mei = ?, houmonsaki = ?, mokuteki = ?, seisankikan_from = ?, seisankikan_from_hour = ?, seisankikan_from_min = ?, seisankikan_to = ?, seisankikan_to_hour = ?, seisankikan_to_min = ?, keijoubi = ?, shiharaibi = ?, shiharaikiboubi = ?, shiharaihouhou = ?, tekiyou = ?, kaigai_tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, goukei_kingaku = ?, houjin_card_riyou_kingaku = ?, kaisha_tehai_kingaku = ?, sashihiki_shikyuu_kingaku = ?, sashihiki_num = ?, sashihiki_tanka = ?, sashihiki_num_kaigai = ?, sashihiki_tanka_kaigai = ?, sashihiki_heishu_cd_kaigai = ?, sashihiki_rate_kaigai = ?, sashihiki_tanka_kaigai_gaika = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, ryohi_kazei_flg = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, kaigai_shiwake_edano = ?, kaigai_torihiki_name = ?, kaigai_kari_futan_bumon_cd = ?, kaigai_kari_futan_bumon_name = ?, kaigai_torihikisaki_cd = ?, kaigai_torihikisaki_name_ryakushiki = ?, kaigai_kari_kamoku_cd = ?, kaigai_kari_kamoku_name = ?, kaigai_kari_kamoku_edaban_cd = ?, kaigai_kari_kamoku_edaban_name = ?, kaigai_kari_kazei_kbn = ?, kaigai_kazei_flg = ?, kaigai_kashi_futan_bumon_cd = ?, kaigai_kashi_futan_bumon_name = ?, kaigai_kashi_kamoku_cd = ?, kaigai_kashi_kamoku_name = ?, kaigai_kashi_kamoku_edaban_cd = ?, kaigai_kashi_kamoku_edaban_name = ?, kaigai_kashi_kazei_kbn = ?, kaigai_uf1_cd = ?, kaigai_uf1_name_ryakushiki = ?, kaigai_uf2_cd = ?, kaigai_uf2_name_ryakushiki = ?, kaigai_uf3_cd = ?, kaigai_uf3_name_ryakushiki = ?, kaigai_uf4_cd = ?, kaigai_uf4_name_ryakushiki = ?, kaigai_uf5_cd = ?, kaigai_uf5_name_ryakushiki = ?, kaigai_uf6_cd = ?, kaigai_uf6_name_ryakushiki = ?, kaigai_uf7_cd = ?, kaigai_uf7_name_ryakushiki = ?, kaigai_uf8_cd = ?, kaigai_uf8_name_ryakushiki = ?, kaigai_uf9_cd = ?, kaigai_uf9_name_ryakushiki = ?, kaigai_uf10_cd = ?, kaigai_uf10_name_ryakushiki = ?, kaigai_project_cd = ?, kaigai_project_name = ?, kaigai_segment_cd = ?, kaigai_segment_name_ryakushiki = ?, kaigai_tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp, bunri_kbn = ?, kari_shiire_kbn = ?, kashi_shiire_kbn = ?, kaigai_bunri_kbn = ?, kaigai_kari_shiire_kbn = ?, kaigai_kashi_shiire_kbn = ?, invoice_denpyou = ? " + this.whereSql;
		return connection.update(sql, dto.karibaraiDenpyouId, dto.karibaraiOn, dto.karibaraiMishiyouFlg, dto.shucchouChuushiFlg, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.keijoubi, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kaigaiTekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.goukeiKingaku, dto.houjinCardRiyouKingaku, dto.kaishaTehaiKingaku, dto.sashihikiShikyuuKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.ryohiKazeiFlg, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.kaigaiShiwakeEdano, dto.kaigaiTorihikiName, dto.kaigaiKariFutanBumonCd, dto.kaigaiKariFutanBumonName, dto.kaigaiTorihikisakiCd, dto.kaigaiTorihikisakiNameRyakushiki, dto.kaigaiKariKamokuCd, dto.kaigaiKariKamokuName, dto.kaigaiKariKamokuEdabanCd, dto.kaigaiKariKamokuEdabanName, dto.kaigaiKariKazeiKbn, dto.kaigaiKazeiFlg, dto.kaigaiKashiFutanBumonCd, dto.kaigaiKashiFutanBumonName, dto.kaigaiKashiKamokuCd, dto.kaigaiKashiKamokuName, dto.kaigaiKashiKamokuEdabanCd, dto.kaigaiKashiKamokuEdabanName, dto.kaigaiKashiKazeiKbn, dto.kaigaiUf1Cd, dto.kaigaiUf1NameRyakushiki, dto.kaigaiUf2Cd, dto.kaigaiUf2NameRyakushiki, dto.kaigaiUf3Cd, dto.kaigaiUf3NameRyakushiki, dto.kaigaiUf4Cd, dto.kaigaiUf4NameRyakushiki, dto.kaigaiUf5Cd, dto.kaigaiUf5NameRyakushiki, dto.kaigaiUf6Cd, dto.kaigaiUf6NameRyakushiki, dto.kaigaiUf7Cd, dto.kaigaiUf7NameRyakushiki, dto.kaigaiUf8Cd, dto.kaigaiUf8NameRyakushiki, dto.kaigaiUf9Cd, dto.kaigaiUf9NameRyakushiki, dto.kaigaiUf10Cd, dto.kaigaiUf10NameRyakushiki, dto.kaigaiProjectCd, dto.kaigaiProjectName, dto.kaigaiSegmentCd, dto.kaigaiSegmentNameRyakushiki, dto.kaigaiTekiyouCd, koushinUserId, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.kaigaiBunriKbn, dto.kaigaiKariShiireKbn, dto.kaigaiKashiShiireKbn, dto.invoiceDenpyou, dto.denpyouId);
	}

	/**
	 * 海外旅費精算登録or更新
	 * 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	 * @param dto 海外旅費精算
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int upsert(KaigaiRyohiseisan dto, String koushinUserId) {
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT kaigai_ryohiseisan_pkey "
			+ "DO UPDATE SET  karibarai_denpyou_id = ?, karibarai_on = ?, karibarai_mishiyou_flg = ?, shucchou_chuushi_flg = ?, dairiflg = ?, user_id = ?, shain_no = ?, user_sei = ?, user_mei = ?, houmonsaki = ?, mokuteki = ?, seisankikan_from = ?, seisankikan_from_hour = ?, seisankikan_from_min = ?, seisankikan_to = ?, seisankikan_to_hour = ?, seisankikan_to_min = ?, keijoubi = ?, shiharaibi = ?, shiharaikiboubi = ?, shiharaihouhou = ?, tekiyou = ?, kaigai_tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, goukei_kingaku = ?, houjin_card_riyou_kingaku = ?, kaisha_tehai_kingaku = ?, sashihiki_shikyuu_kingaku = ?, sashihiki_num = ?, sashihiki_tanka = ?, sashihiki_num_kaigai = ?, sashihiki_tanka_kaigai = ?, sashihiki_heishu_cd_kaigai = ?, sashihiki_rate_kaigai = ?, sashihiki_tanka_kaigai_gaika = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, ryohi_kazei_flg = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, kaigai_shiwake_edano = ?, kaigai_torihiki_name = ?, kaigai_kari_futan_bumon_cd = ?, kaigai_kari_futan_bumon_name = ?, kaigai_torihikisaki_cd = ?, kaigai_torihikisaki_name_ryakushiki = ?, kaigai_kari_kamoku_cd = ?, kaigai_kari_kamoku_name = ?, kaigai_kari_kamoku_edaban_cd = ?, kaigai_kari_kamoku_edaban_name = ?, kaigai_kari_kazei_kbn = ?, kaigai_kazei_flg = ?, kaigai_kashi_futan_bumon_cd = ?, kaigai_kashi_futan_bumon_name = ?, kaigai_kashi_kamoku_cd = ?, kaigai_kashi_kamoku_name = ?, kaigai_kashi_kamoku_edaban_cd = ?, kaigai_kashi_kamoku_edaban_name = ?, kaigai_kashi_kazei_kbn = ?, kaigai_uf1_cd = ?, kaigai_uf1_name_ryakushiki = ?, kaigai_uf2_cd = ?, kaigai_uf2_name_ryakushiki = ?, kaigai_uf3_cd = ?, kaigai_uf3_name_ryakushiki = ?, kaigai_uf4_cd = ?, kaigai_uf4_name_ryakushiki = ?, kaigai_uf5_cd = ?, kaigai_uf5_name_ryakushiki = ?, kaigai_uf6_cd = ?, kaigai_uf6_name_ryakushiki = ?, kaigai_uf7_cd = ?, kaigai_uf7_name_ryakushiki = ?, kaigai_uf8_cd = ?, kaigai_uf8_name_ryakushiki = ?, kaigai_uf9_cd = ?, kaigai_uf9_name_ryakushiki = ?, kaigai_uf10_cd = ?, kaigai_uf10_name_ryakushiki = ?, kaigai_project_cd = ?, kaigai_project_name = ?, kaigai_segment_cd = ?, kaigai_segment_name_ryakushiki = ?, kaigai_tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp, bunri_kbn = ?, kari_shiire_kbn = ?, kashi_shiire_kbn = ?, kaigai_bunri_kbn = ?, kaigai_kari_shiire_kbn = ?, kaigai_kashi_shiire_kbn = ?, invoice_denpyou = ? ";
		return connection.update(sql, dto.denpyouId, dto.karibaraiDenpyouId, dto.karibaraiOn, dto.karibaraiMishiyouFlg, dto.shucchouChuushiFlg, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.keijoubi, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kaigaiTekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.goukeiKingaku, dto.houjinCardRiyouKingaku, dto.kaishaTehaiKingaku, dto.sashihikiShikyuuKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.ryohiKazeiFlg, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.kaigaiShiwakeEdano, dto.kaigaiTorihikiName, dto.kaigaiKariFutanBumonCd, dto.kaigaiKariFutanBumonName, dto.kaigaiTorihikisakiCd, dto.kaigaiTorihikisakiNameRyakushiki, dto.kaigaiKariKamokuCd, dto.kaigaiKariKamokuName, dto.kaigaiKariKamokuEdabanCd, dto.kaigaiKariKamokuEdabanName, dto.kaigaiKariKazeiKbn, dto.kaigaiKazeiFlg, dto.kaigaiKashiFutanBumonCd, dto.kaigaiKashiFutanBumonName, dto.kaigaiKashiKamokuCd, dto.kaigaiKashiKamokuName, dto.kaigaiKashiKamokuEdabanCd, dto.kaigaiKashiKamokuEdabanName, dto.kaigaiKashiKazeiKbn, dto.kaigaiUf1Cd, dto.kaigaiUf1NameRyakushiki, dto.kaigaiUf2Cd, dto.kaigaiUf2NameRyakushiki, dto.kaigaiUf3Cd, dto.kaigaiUf3NameRyakushiki, dto.kaigaiUf4Cd, dto.kaigaiUf4NameRyakushiki, dto.kaigaiUf5Cd, dto.kaigaiUf5NameRyakushiki, dto.kaigaiUf6Cd, dto.kaigaiUf6NameRyakushiki, dto.kaigaiUf7Cd, dto.kaigaiUf7NameRyakushiki, dto.kaigaiUf8Cd, dto.kaigaiUf8NameRyakushiki, dto.kaigaiUf9Cd, dto.kaigaiUf9NameRyakushiki, dto.kaigaiUf10Cd, dto.kaigaiUf10NameRyakushiki, dto.kaigaiProjectCd, dto.kaigaiProjectName, dto.kaigaiSegmentCd, dto.kaigaiSegmentNameRyakushiki, dto.kaigaiTekiyouCd, koushinUserId, koushinUserId, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.kaigaiBunriKbn, dto.kaigaiKariShiireKbn, dto.kaigaiKashiShiireKbn, dto.invoiceDenpyou, dto.karibaraiDenpyouId, dto.karibaraiOn, dto.karibaraiMishiyouFlg, dto.shucchouChuushiFlg, dto.dairiflg, dto.userId, dto.shainNo, dto.userSei, dto.userMei, dto.houmonsaki, dto.mokuteki, dto.seisankikanFrom, dto.seisankikanFromHour, dto.seisankikanFromMin, dto.seisankikanTo, dto.seisankikanToHour, dto.seisankikanToMin, dto.keijoubi, dto.shiharaibi, dto.shiharaikiboubi, dto.shiharaihouhou, dto.tekiyou, dto.kaigaiTekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.goukeiKingaku, dto.houjinCardRiyouKingaku, dto.kaishaTehaiKingaku, dto.sashihikiShikyuuKingaku, dto.sashihikiNum, dto.sashihikiTanka, dto.sashihikiNumKaigai, dto.sashihikiTankaKaigai, dto.sashihikiHeishuCdKaigai, dto.sashihikiRateKaigai, dto.sashihikiTankaKaigaiGaika, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.hosoku, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.ryohiKazeiFlg, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, dto.kaigaiShiwakeEdano, dto.kaigaiTorihikiName, dto.kaigaiKariFutanBumonCd, dto.kaigaiKariFutanBumonName, dto.kaigaiTorihikisakiCd, dto.kaigaiTorihikisakiNameRyakushiki, dto.kaigaiKariKamokuCd, dto.kaigaiKariKamokuName, dto.kaigaiKariKamokuEdabanCd, dto.kaigaiKariKamokuEdabanName, dto.kaigaiKariKazeiKbn, dto.kaigaiKazeiFlg, dto.kaigaiKashiFutanBumonCd, dto.kaigaiKashiFutanBumonName, dto.kaigaiKashiKamokuCd, dto.kaigaiKashiKamokuName, dto.kaigaiKashiKamokuEdabanCd, dto.kaigaiKashiKamokuEdabanName, dto.kaigaiKashiKazeiKbn, dto.kaigaiUf1Cd, dto.kaigaiUf1NameRyakushiki, dto.kaigaiUf2Cd, dto.kaigaiUf2NameRyakushiki, dto.kaigaiUf3Cd, dto.kaigaiUf3NameRyakushiki, dto.kaigaiUf4Cd, dto.kaigaiUf4NameRyakushiki, dto.kaigaiUf5Cd, dto.kaigaiUf5NameRyakushiki, dto.kaigaiUf6Cd, dto.kaigaiUf6NameRyakushiki, dto.kaigaiUf7Cd, dto.kaigaiUf7NameRyakushiki, dto.kaigaiUf8Cd, dto.kaigaiUf8NameRyakushiki, dto.kaigaiUf9Cd, dto.kaigaiUf9NameRyakushiki, dto.kaigaiUf10Cd, dto.kaigaiUf10NameRyakushiki, dto.kaigaiProjectCd, dto.kaigaiProjectName, dto.kaigaiSegmentCd, dto.kaigaiSegmentNameRyakushiki, dto.kaigaiTekiyouCd, koushinUserId, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.kaigaiBunriKbn, dto.kaigaiKariShiireKbn, dto.kaigaiKashiShiireKbn, dto.invoiceDenpyou);
	}

	/**
	 * 海外旅費精算から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM kaigai_ryohiseisan " + this.whereSql;
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 旅費精算の計上日を更新する。nullなら今日。
	 * @param denpyouId  伝票ID
	 * @param keijoubi   計上日
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	public int updateKeijoubi(
			String denpyouId,
			Date keijoubi,
			String userId
	) {
		final String sql =
				"UPDATE kaigai_ryohiseisan "
			+   "SET keijoubi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, keijoubi, userId, denpyouId);
	}
	
	/**
	 * 旅費精算の計上日がnullなら計上日を本日の日付にする。
	 * @param denpyouId  伝票ID
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	public int updateKeijoubiTodayIfNull(
			String denpyouId,
			String userId
	) {
		String sql = "SELECT * FROM kaigai_ryohiseisan WHERE denpyou_id=?";
		GMap denpyouRecord = connection.find(sql,denpyouId);
		if (denpyouRecord.get("keijoubi") != null ) return 0;
		sql =
			"UPDATE kaigai_ryohiseisan "
			+   "SET keijoubi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, new java.sql.Date(System.currentTimeMillis()), userId, denpyouId);
	} 
	
	/**
	 * 旅費精算の支払日を更新する。
	 * @param denpyouId  伝票ID
	 * @param shiharaibi 支払日
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	public int updateShiharaibi(
			String denpyouId,
			Date shiharaibi,
			String userId
	) {
		final String sql =
				"UPDATE kaigai_ryohiseisan "
			+   "SET shiharaibi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, shiharaibi, userId, denpyouId);
	}
	
	/**
	 * 指定した海外旅費仮払伝票の精算完了日を更新
	 * @param karibaraiDenpyouId 伝票ID
	 * @param userId ユーザーID
	 * @return 処理件数
	 */
	public int updateKaribaraiSeisanbi(String karibaraiDenpyouId, String userId) {
		final String sql =
				"UPDATE kaigai_ryohi_karibarai "
			+   "SET seisan_kanryoubi = current_date, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, userId, karibaraiDenpyouId);
		
	}
}