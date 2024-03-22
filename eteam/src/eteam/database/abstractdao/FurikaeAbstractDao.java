package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Furikae;

/**
 * 振替に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class FurikaeAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO furikae (denpyou_id, denpyou_date, shouhyou_shorui_flg, kingaku, hontai_kingaku, shouhizeigaku, kari_zeiritsu, kari_keigen_zeiritsu_kbn, tekiyou, hf1_cd, hf1_name_ryakushiki, hf2_cd, hf2_name_ryakushiki, hf3_cd, hf3_name_ryakushiki, hf4_cd, hf4_name_ryakushiki, hf5_cd, hf5_name_ryakushiki, hf6_cd, hf6_name_ryakushiki, hf7_cd, hf7_name_ryakushiki, hf8_cd, hf8_name_ryakushiki, hf9_cd, hf9_name_ryakushiki, hf10_cd, hf10_name_ryakushiki, bikou, kari_futan_bumon_cd, kari_futan_bumon_name, kari_kamoku_cd, kari_kamoku_name, kari_kamoku_edaban_cd, kari_kamoku_edaban_name, kari_kazei_kbn, kari_torihikisaki_cd, kari_torihikisaki_name_ryakushiki, kashi_futan_bumon_cd, kashi_futan_bumon_name, kashi_kamoku_cd, kashi_kamoku_name, kashi_kamoku_edaban_cd, kashi_kamoku_edaban_name, kashi_kazei_kbn, kashi_torihikisaki_cd, kashi_torihikisaki_name_ryakushiki, kari_uf1_cd, kari_uf1_name_ryakushiki, kari_uf2_cd, kari_uf2_name_ryakushiki, kari_uf3_cd, kari_uf3_name_ryakushiki, kari_uf4_cd, kari_uf4_name_ryakushiki, kari_uf5_cd, kari_uf5_name_ryakushiki, kari_uf6_cd, kari_uf6_name_ryakushiki, kari_uf7_cd, kari_uf7_name_ryakushiki, kari_uf8_cd, kari_uf8_name_ryakushiki, kari_uf9_cd, kari_uf9_name_ryakushiki, kari_uf10_cd, kari_uf10_name_ryakushiki, kashi_uf1_cd, kashi_uf1_name_ryakushiki, kashi_uf2_cd, kashi_uf2_name_ryakushiki, kashi_uf3_cd, kashi_uf3_name_ryakushiki, kashi_uf4_cd, kashi_uf4_name_ryakushiki, kashi_uf5_cd, kashi_uf5_name_ryakushiki, kashi_uf6_cd, kashi_uf6_name_ryakushiki, kashi_uf7_cd, kashi_uf7_name_ryakushiki, kashi_uf8_cd, kashi_uf8_name_ryakushiki, kashi_uf9_cd, kashi_uf9_name_ryakushiki, kashi_uf10_cd, kashi_uf10_name_ryakushiki, kari_project_cd, kari_project_name, kari_segment_cd, kari_segment_name_ryakushiki, kashi_project_cd, kashi_project_name, kashi_segment_cd, kashi_segment_name_ryakushiki, touroku_user_id, touroku_time, koushin_user_id, koushin_time, kari_bunri_kbn, kashi_bunri_kbn, kari_jigyousha_kbn, kari_shiire_kbn, kari_zeigaku_houshiki, kashi_jigyousha_kbn, kashi_shiire_kbn, kashi_zeigaku_houshiki, invoice_denpyou, kashi_zeiritsu, kashi_keigen_zeiritsu_kbn) VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 主キー条件定型部
	 */
	protected final String whereSql = " WHERE denpyou_id = ?";

	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected Furikae mapToDto(GMap map) {
		return map == null ? null : new Furikae(map);
	}

	/**
	 * @param mapList 検索結果GMap
	 * @return dtoList
	 */
	protected List<Furikae> mapToDto(List<GMap> mapList) {
		List<Furikae> dtoList = new ArrayList<Furikae>();
		for (var map : mapList) {
			dtoList.add(new Furikae(map));
		}
		return dtoList;
	}

	/**
	 * 振替のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId) {
		return this.find(denpyouId) != null;
	}

	/**
	 * 振替から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return 振替DTO
	 */
	public Furikae find(String denpyouId) {
		final String sql = "SELECT * FROM furikae " + this.whereSql;
		return mapToDto(connection.find(sql, denpyouId));
	}

	/**
	 * 振替からレコードを全件取得 ※大量データ取得に注意
	 * @return List<振替DTO>
	 */
	public List<Furikae> load() {
		final String sql = "SELECT * FROM furikae  ORDER BY denpyou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	 * 振替登録
	 * @param dto 振替
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int insert(Furikae dto, String koushinUserId) {
		return connection.update(this.insertSql, dto.denpyouId, dto.denpyouDate, dto.shouhyouShoruiFlg, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kariZeiritsu, dto.kariKeigenZeiritsuKbn, dto.tekiyou, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.bikou, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kariTorihikisakiCd, dto.kariTorihikisakiNameRyakushiki, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.kashiTorihikisakiCd, dto.kashiTorihikisakiNameRyakushiki, dto.kariUf1Cd, dto.kariUf1NameRyakushiki, dto.kariUf2Cd, dto.kariUf2NameRyakushiki, dto.kariUf3Cd, dto.kariUf3NameRyakushiki, dto.kariUf4Cd, dto.kariUf4NameRyakushiki, dto.kariUf5Cd, dto.kariUf5NameRyakushiki, dto.kariUf6Cd, dto.kariUf6NameRyakushiki, dto.kariUf7Cd, dto.kariUf7NameRyakushiki, dto.kariUf8Cd, dto.kariUf8NameRyakushiki, dto.kariUf9Cd, dto.kariUf9NameRyakushiki, dto.kariUf10Cd, dto.kariUf10NameRyakushiki, dto.kashiUf1Cd, dto.kashiUf1NameRyakushiki, dto.kashiUf2Cd, dto.kashiUf2NameRyakushiki, dto.kashiUf3Cd, dto.kashiUf3NameRyakushiki, dto.kashiUf4Cd, dto.kashiUf4NameRyakushiki, dto.kashiUf5Cd, dto.kashiUf5NameRyakushiki, dto.kashiUf6Cd, dto.kashiUf6NameRyakushiki, dto.kashiUf7Cd, dto.kashiUf7NameRyakushiki, dto.kashiUf8Cd, dto.kashiUf8NameRyakushiki, dto.kashiUf9Cd, dto.kashiUf9NameRyakushiki, dto.kashiUf10Cd, dto.kashiUf10NameRyakushiki, dto.kariProjectCd, dto.kariProjectName, dto.kariSegmentCd, dto.kariSegmentNameRyakushiki, dto.kashiProjectCd, dto.kashiProjectName, dto.kashiSegmentCd, dto.kashiSegmentNameRyakushiki, koushinUserId, koushinUserId, dto.kariBunriKbn, dto.kashiBunriKbn,  dto.kariJigyoushaKbn, dto.kariShiireKbn, dto.kariZeigakuHoushiki, dto.kashiJigyoushaKbn, dto.kashiShiireKbn, dto.kashiZeigakuHoushiki, dto.invoiceDenpyou, dto.kashiZeiritsu, dto.kashiKeigenZeiritsuKbn);
	}

	/**
	 * 振替更新
	 * @param dto 振替
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int update(Furikae dto, String koushinUserId) {
		final String sql = "UPDATE furikae SET  denpyou_date = ?, shouhyou_shorui_flg = ?, kingaku = ?, hontai_kingaku = ?, shouhizeigaku = ?, kari_zeiritsu = ?, kari_keigen_zeiritsu_kbn = ?, tekiyou = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, bikou = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kari_torihikisaki_cd = ?, kari_torihikisaki_name_ryakushiki = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, kashi_torihikisaki_cd = ?, kashi_torihikisaki_name_ryakushiki = ?, kari_uf1_cd = ?, kari_uf1_name_ryakushiki = ?, kari_uf2_cd = ?, kari_uf2_name_ryakushiki = ?, kari_uf3_cd = ?, kari_uf3_name_ryakushiki = ?, kari_uf4_cd = ?, kari_uf4_name_ryakushiki = ?, kari_uf5_cd = ?, kari_uf5_name_ryakushiki = ?, kari_uf6_cd = ?, kari_uf6_name_ryakushiki = ?, kari_uf7_cd = ?, kari_uf7_name_ryakushiki = ?, kari_uf8_cd = ?, kari_uf8_name_ryakushiki = ?, kari_uf9_cd = ?, kari_uf9_name_ryakushiki = ?, kari_uf10_cd = ?, kari_uf10_name_ryakushiki = ?, kashi_uf1_cd = ?, kashi_uf1_name_ryakushiki = ?, kashi_uf2_cd = ?, kashi_uf2_name_ryakushiki = ?, kashi_uf3_cd = ?, kashi_uf3_name_ryakushiki = ?, kashi_uf4_cd = ?, kashi_uf4_name_ryakushiki = ?, kashi_uf5_cd = ?, kashi_uf5_name_ryakushiki = ?, kashi_uf6_cd = ?, kashi_uf6_name_ryakushiki = ?, kashi_uf7_cd = ?, kashi_uf7_name_ryakushiki = ?, kashi_uf8_cd = ?, kashi_uf8_name_ryakushiki = ?, kashi_uf9_cd = ?, kashi_uf9_name_ryakushiki = ?, kashi_uf10_cd = ?, kashi_uf10_name_ryakushiki = ?, kari_project_cd = ?, kari_project_name = ?, kari_segment_cd = ?, kari_segment_name_ryakushiki = ?, kashi_project_cd = ?, kashi_project_name = ?, kashi_segment_cd = ?, kashi_segment_name_ryakushiki = ?, koushin_user_id = ?, koushin_time = current_timestamp, kari_bunri_kbn = ?, kashi_bunri_kbn = ?, kari_jigyousha_kbn = ?, kari_shiire_kbn = ?, kari_zeigaku_houshiki = ?, kashi_jigyousha_kbn = ?, kashi_shiire_kbn = ?, kashi_zeigaku_houshiki = ?, invoice_denpyou = ?, kashi_zeiritsu = ?, kashi_keigen_zeiritsu_kbn = ? " + this.whereSql;
		return connection.update(sql, dto.denpyouDate, dto.shouhyouShoruiFlg, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kariZeiritsu, dto.kariKeigenZeiritsuKbn, dto.tekiyou, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.bikou, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kariTorihikisakiCd, dto.kariTorihikisakiNameRyakushiki, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.kashiTorihikisakiCd, dto.kashiTorihikisakiNameRyakushiki, dto.kariUf1Cd, dto.kariUf1NameRyakushiki, dto.kariUf2Cd, dto.kariUf2NameRyakushiki, dto.kariUf3Cd, dto.kariUf3NameRyakushiki, dto.kariUf4Cd, dto.kariUf4NameRyakushiki, dto.kariUf5Cd, dto.kariUf5NameRyakushiki, dto.kariUf6Cd, dto.kariUf6NameRyakushiki, dto.kariUf7Cd, dto.kariUf7NameRyakushiki, dto.kariUf8Cd, dto.kariUf8NameRyakushiki, dto.kariUf9Cd, dto.kariUf9NameRyakushiki, dto.kariUf10Cd, dto.kariUf10NameRyakushiki, dto.kashiUf1Cd, dto.kashiUf1NameRyakushiki, dto.kashiUf2Cd, dto.kashiUf2NameRyakushiki, dto.kashiUf3Cd, dto.kashiUf3NameRyakushiki, dto.kashiUf4Cd, dto.kashiUf4NameRyakushiki, dto.kashiUf5Cd, dto.kashiUf5NameRyakushiki, dto.kashiUf6Cd, dto.kashiUf6NameRyakushiki, dto.kashiUf7Cd, dto.kashiUf7NameRyakushiki, dto.kashiUf8Cd, dto.kashiUf8NameRyakushiki, dto.kashiUf9Cd, dto.kashiUf9NameRyakushiki, dto.kashiUf10Cd, dto.kashiUf10NameRyakushiki, dto.kariProjectCd, dto.kariProjectName, dto.kariSegmentCd, dto.kariSegmentNameRyakushiki, dto.kashiProjectCd, dto.kashiProjectName, dto.kashiSegmentCd, dto.kashiSegmentNameRyakushiki, koushinUserId, dto.kariBunriKbn, dto.kashiBunriKbn, dto.kariJigyoushaKbn, dto.kariShiireKbn, dto.kariZeigakuHoushiki, dto.kashiJigyoushaKbn, dto.kashiShiireKbn, dto.kashiZeigakuHoushiki, dto.invoiceDenpyou, dto.kashiZeiritsu, dto.kashiKeigenZeiritsuKbn, dto.denpyouId);
	}

	/**
	 * 振替登録or更新
	 * 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	 * @param dto 振替
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int upsert(Furikae dto, String koushinUserId) {
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT furikae_pkey "
			+ "DO UPDATE SET  denpyou_date = ?, shouhyou_shorui_flg = ?, kingaku = ?, hontai_kingaku = ?, shouhizeigaku = ?, kari_zeiritsu = ?, kari_keigen_zeiritsu_kbn = ?, tekiyou = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, bikou = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kari_torihikisaki_cd = ?, kari_torihikisaki_name_ryakushiki = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, kashi_torihikisaki_cd = ?, kashi_torihikisaki_name_ryakushiki = ?, kari_uf1_cd = ?, kari_uf1_name_ryakushiki = ?, kari_uf2_cd = ?, kari_uf2_name_ryakushiki = ?, kari_uf3_cd = ?, kari_uf3_name_ryakushiki = ?, kari_uf4_cd = ?, kari_uf4_name_ryakushiki = ?, kari_uf5_cd = ?, kari_uf5_name_ryakushiki = ?, kari_uf6_cd = ?, kari_uf6_name_ryakushiki = ?, kari_uf7_cd = ?, kari_uf7_name_ryakushiki = ?, kari_uf8_cd = ?, kari_uf8_name_ryakushiki = ?, kari_uf9_cd = ?, kari_uf9_name_ryakushiki = ?, kari_uf10_cd = ?, kari_uf10_name_ryakushiki = ?, kashi_uf1_cd = ?, kashi_uf1_name_ryakushiki = ?, kashi_uf2_cd = ?, kashi_uf2_name_ryakushiki = ?, kashi_uf3_cd = ?, kashi_uf3_name_ryakushiki = ?, kashi_uf4_cd = ?, kashi_uf4_name_ryakushiki = ?, kashi_uf5_cd = ?, kashi_uf5_name_ryakushiki = ?, kashi_uf6_cd = ?, kashi_uf6_name_ryakushiki = ?, kashi_uf7_cd = ?, kashi_uf7_name_ryakushiki = ?, kashi_uf8_cd = ?, kashi_uf8_name_ryakushiki = ?, kashi_uf9_cd = ?, kashi_uf9_name_ryakushiki = ?, kashi_uf10_cd = ?, kashi_uf10_name_ryakushiki = ?, kari_project_cd = ?, kari_project_name = ?, kari_segment_cd = ?, kari_segment_name_ryakushiki = ?, kashi_project_cd = ?, kashi_project_name = ?, kashi_segment_cd = ?, kashi_segment_name_ryakushiki = ?, koushin_user_id = ?, koushin_time = current_timestamp, kari_bunri_kbn = ?, kashi_bunri_kbn = ?, kari_jigyousha_kbn = ?, kari_shiire_kbn = ?, kari_zeigaku_houshiki = ?, kashi_jigyousha_kbn = ?, kashi_shiire_kbn = ?, kashi_zeigaku_houshiki = ?, invoice_denpyou = ?, kashi_zeiritsu = ?, kashi_keigen_zeiritsu_kbn = ? ";
		return connection.update(sql, dto.denpyouId, dto.denpyouDate, dto.shouhyouShoruiFlg, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kariZeiritsu, dto.kariKeigenZeiritsuKbn, dto.tekiyou, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.bikou, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kariTorihikisakiCd, dto.kariTorihikisakiNameRyakushiki, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.kashiTorihikisakiCd, dto.kashiTorihikisakiNameRyakushiki, dto.kariUf1Cd, dto.kariUf1NameRyakushiki, dto.kariUf2Cd, dto.kariUf2NameRyakushiki, dto.kariUf3Cd, dto.kariUf3NameRyakushiki, dto.kariUf4Cd, dto.kariUf4NameRyakushiki, dto.kariUf5Cd, dto.kariUf5NameRyakushiki, dto.kariUf6Cd, dto.kariUf6NameRyakushiki, dto.kariUf7Cd, dto.kariUf7NameRyakushiki, dto.kariUf8Cd, dto.kariUf8NameRyakushiki, dto.kariUf9Cd, dto.kariUf9NameRyakushiki, dto.kariUf10Cd, dto.kariUf10NameRyakushiki, dto.kashiUf1Cd, dto.kashiUf1NameRyakushiki, dto.kashiUf2Cd, dto.kashiUf2NameRyakushiki, dto.kashiUf3Cd, dto.kashiUf3NameRyakushiki, dto.kashiUf4Cd, dto.kashiUf4NameRyakushiki, dto.kashiUf5Cd, dto.kashiUf5NameRyakushiki, dto.kashiUf6Cd, dto.kashiUf6NameRyakushiki, dto.kashiUf7Cd, dto.kashiUf7NameRyakushiki, dto.kashiUf8Cd, dto.kashiUf8NameRyakushiki, dto.kashiUf9Cd, dto.kashiUf9NameRyakushiki, dto.kashiUf10Cd, dto.kashiUf10NameRyakushiki, dto.kariProjectCd, dto.kariProjectName, dto.kariSegmentCd, dto.kariSegmentNameRyakushiki, dto.kashiProjectCd, dto.kashiProjectName, dto.kashiSegmentCd, dto.kashiSegmentNameRyakushiki, koushinUserId, koushinUserId, dto.kariBunriKbn, dto.kashiBunriKbn, dto.kariJigyoushaKbn, dto.kariShiireKbn, dto.kariZeigakuHoushiki, dto.kashiJigyoushaKbn, dto.kashiShiireKbn, dto.kashiZeigakuHoushiki, dto.invoiceDenpyou, dto.denpyouDate, dto.shouhyouShoruiFlg, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.kashiZeiritsu, dto.kashiKeigenZeiritsuKbn, dto.tekiyou, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.bikou, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kariTorihikisakiCd, dto.kariTorihikisakiNameRyakushiki, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.kashiTorihikisakiCd, dto.kashiTorihikisakiNameRyakushiki, dto.kariUf1Cd, dto.kariUf1NameRyakushiki, dto.kariUf2Cd, dto.kariUf2NameRyakushiki, dto.kariUf3Cd, dto.kariUf3NameRyakushiki, dto.kariUf4Cd, dto.kariUf4NameRyakushiki, dto.kariUf5Cd, dto.kariUf5NameRyakushiki, dto.kariUf6Cd, dto.kariUf6NameRyakushiki, dto.kariUf7Cd, dto.kariUf7NameRyakushiki, dto.kariUf8Cd, dto.kariUf8NameRyakushiki, dto.kariUf9Cd, dto.kariUf9NameRyakushiki, dto.kariUf10Cd, dto.kariUf10NameRyakushiki, dto.kashiUf1Cd, dto.kashiUf1NameRyakushiki, dto.kashiUf2Cd, dto.kashiUf2NameRyakushiki, dto.kashiUf3Cd, dto.kashiUf3NameRyakushiki, dto.kashiUf4Cd, dto.kashiUf4NameRyakushiki, dto.kashiUf5Cd, dto.kashiUf5NameRyakushiki, dto.kashiUf6Cd, dto.kashiUf6NameRyakushiki, dto.kashiUf7Cd, dto.kashiUf7NameRyakushiki, dto.kashiUf8Cd, dto.kashiUf8NameRyakushiki, dto.kashiUf9Cd, dto.kashiUf9NameRyakushiki, dto.kashiUf10Cd, dto.kashiUf10NameRyakushiki, dto.kariProjectCd, dto.kariProjectName, dto.kariSegmentCd, dto.kariSegmentNameRyakushiki, dto.kashiProjectCd, dto.kashiProjectName, dto.kashiSegmentCd, dto.kashiSegmentNameRyakushiki, koushinUserId, dto.kariBunriKbn, dto.kashiBunriKbn, dto.kariJigyoushaKbn, dto.kariShiireKbn, dto.kariZeigakuHoushiki, dto.kashiJigyoushaKbn, dto.kashiShiireKbn, dto.kashiZeigakuHoushiki, dto.invoiceDenpyou, dto.kashiZeiritsu, dto.kashiKeigenZeiritsuKbn);
	}

	/**
	 * 振替から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM furikae " + this.whereSql;
		return connection.update(sql, denpyouId);
	}
}