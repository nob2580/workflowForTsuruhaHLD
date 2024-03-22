package eteam.database.abstractdao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Tsuukinteiki;

/**
 * 通勤定期に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class TsuukinteikiAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO tsuukinteiki (denpyou_id, shiyou_kikan_kbn, shiyou_kaishibi, shiyou_shuuryoubi, jyousha_kukan, teiki_serialize_data, shiharaibi, tekiyou, zeiritsu, keigen_zeiritsu_kbn, kingaku, tenyuuryoku_flg, hf1_cd, hf1_name_ryakushiki, hf2_cd, hf2_name_ryakushiki, hf3_cd, hf3_name_ryakushiki, hf4_cd, hf4_name_ryakushiki, hf5_cd, hf5_name_ryakushiki, hf6_cd, hf6_name_ryakushiki, hf7_cd, hf7_name_ryakushiki, hf8_cd, hf8_name_ryakushiki, hf9_cd, hf9_name_ryakushiki, hf10_cd, hf10_name_ryakushiki, shiwake_edano, torihiki_name, kari_futan_bumon_cd, kari_futan_bumon_name, torihikisaki_cd, torihikisaki_name_ryakushiki, kari_kamoku_cd, kari_kamoku_name, kari_kamoku_edaban_cd, kari_kamoku_edaban_name, kari_kazei_kbn, kashi_futan_bumon_cd, kashi_futan_bumon_name, kashi_kamoku_cd, kashi_kamoku_name, kashi_kamoku_edaban_cd, kashi_kamoku_edaban_name, kashi_kazei_kbn, uf1_cd, uf1_name_ryakushiki, uf2_cd, uf2_name_ryakushiki, uf3_cd, uf3_name_ryakushiki, uf4_cd, uf4_name_ryakushiki, uf5_cd, uf5_name_ryakushiki, uf6_cd, uf6_name_ryakushiki, uf7_cd, uf7_name_ryakushiki, uf8_cd, uf8_name_ryakushiki, uf9_cd, uf9_name_ryakushiki, uf10_cd, uf10_name_ryakushiki, project_cd, project_name, segment_cd, segment_name_ryakushiki, tekiyou_cd, touroku_user_id, touroku_time, koushin_user_id, koushin_time, zeinuki_kingaku, shouhizeigaku, shiharaisaki_name, jigyousha_kbn, bunri_kbn, kari_shiire_kbn, kashi_shiire_kbn, invoice_denpyou) VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * 主キー条件定型部
	 */
	protected final String whereSql = " WHERE denpyou_id = ?";

	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected Tsuukinteiki mapToDto(GMap map) {
		return map == null ? null : new Tsuukinteiki(map);
	}

	/**
	 * @param mapList 検索結果GMap
	 * @return dtoList
	 */
	protected List<Tsuukinteiki> mapToDto(List<GMap> mapList) {
		List<Tsuukinteiki> dtoList = new ArrayList<Tsuukinteiki>();
		for (var map : mapList) {
			dtoList.add(new Tsuukinteiki(map));
		}
		return dtoList;
	}

	/**
	 * 通勤定期のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId) {
		return this.find(denpyouId) != null;
	}

	/**
	 * 通勤定期から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return 通勤定期DTO
	 */
	public Tsuukinteiki find(String denpyouId) {
		final String sql = "SELECT * FROM tsuukinteiki " + this.whereSql;
		return mapToDto(connection.find(sql, denpyouId));
	}

	/**
	 * 通勤定期からレコードを全件取得 ※大量データ取得に注意
	 * @return List<通勤定期DTO>
	 */
	public List<Tsuukinteiki> load() {
		final String sql = "SELECT * FROM tsuukinteiki  ORDER BY denpyou_id";
		return mapToDto(connection.load(sql));
	}

	/**
	 * 通勤定期登録
	 * @param dto 通勤定期
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int insert(Tsuukinteiki dto, String koushinUserId) {
		return connection.update(this.insertSql, dto.denpyouId, dto.shiyouKikanKbn, dto.shiyouKaishibi, dto.shiyouShuuryoubi, dto.jyoushaKukan, dto.teikiSerializeData, dto.shiharaibi, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kingaku, dto.tenyuuryokuFlg, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId, koushinUserId, dto.zeinukiKingaku, dto.shouhizeigaku, dto.shiharaisakiName, dto.jigyoushaKbn, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.invoiceDenpyou);
	}

	/**
	 * 通勤定期更新
	 * @param dto 通勤定期
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int update(Tsuukinteiki dto, String koushinUserId) {
		final String sql = "UPDATE tsuukinteiki SET  shiyou_kikan_kbn = ?, shiyou_kaishibi = ?, shiyou_shuuryoubi = ?, jyousha_kukan = ?, teiki_serialize_data = ?, shiharaibi = ?, tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, kingaku = ?, tenyuuryoku_flg = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp, zeinuki_kingaku = ?, shouhizeigaku = ?, shiharaisaki_name = ?, jigyousha_kbn = ?, bunri_kbn = ?, kari_shiire_kbn = ?, kashi_shiire_kbn = ?, invoice_denpyou = ? " + this.whereSql;
		return connection.update(sql, dto.shiyouKikanKbn, dto.shiyouKaishibi, dto.shiyouShuuryoubi, dto.jyoushaKukan, dto.teikiSerializeData, dto.shiharaibi, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kingaku, dto.tenyuuryokuFlg, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId, dto.zeinukiKingaku, dto.shouhizeigaku, dto.shiharaisakiName, dto.jigyoushaKbn, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.invoiceDenpyou, dto.denpyouId);
	}

	/**
	 * 通勤定期登録or更新
	 * 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	 * @param dto 通勤定期
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int upsert(Tsuukinteiki dto, String koushinUserId) {
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT tsuukinteiki_pkey "
			+ "DO UPDATE SET  shiyou_kikan_kbn = ?, shiyou_kaishibi = ?, shiyou_shuuryoubi = ?, jyousha_kukan = ?, teiki_serialize_data = ?, shiharaibi = ?, tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, kingaku = ?, tenyuuryoku_flg = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp, zeinuki_kingaku = ?, shouhizeigaku = ?, shiharaisaki_name = ?, jigyousha_kbn = ?, bunri_kbn = ?, kari_shiire_kbn = ?, kashi_shiire_kbn = ?, invoice_denpyou = ? ";
		return connection.update(sql, dto.denpyouId, dto.shiyouKikanKbn, dto.shiyouKaishibi, dto.shiyouShuuryoubi, dto.jyoushaKukan, dto.teikiSerializeData, dto.shiharaibi, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kingaku, dto.tenyuuryokuFlg, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId, koushinUserId, dto.zeinukiKingaku, dto.shouhizeigaku, dto.shiharaisakiName, dto.jigyoushaKbn, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.invoiceDenpyou, dto.shiyouKikanKbn, dto.shiyouKaishibi, dto.shiyouShuuryoubi, dto.jyoushaKukan, dto.teikiSerializeData, dto.shiharaibi, dto.tekiyou, dto.zeiritsu, dto.keigenZeiritsuKbn, dto.kingaku, dto.tenyuuryokuFlg, dto.hf1Cd, dto.hf1NameRyakushiki, dto.hf2Cd, dto.hf2NameRyakushiki, dto.hf3Cd, dto.hf3NameRyakushiki, dto.hf4Cd, dto.hf4NameRyakushiki, dto.hf5Cd, dto.hf5NameRyakushiki, dto.hf6Cd, dto.hf6NameRyakushiki, dto.hf7Cd, dto.hf7NameRyakushiki, dto.hf8Cd, dto.hf8NameRyakushiki, dto.hf9Cd, dto.hf9NameRyakushiki, dto.hf10Cd, dto.hf10NameRyakushiki, dto.shiwakeEdano, dto.torihikiName, dto.kariFutanBumonCd, dto.kariFutanBumonName, dto.torihikisakiCd, dto.torihikisakiNameRyakushiki, dto.kariKamokuCd, dto.kariKamokuName, dto.kariKamokuEdabanCd, dto.kariKamokuEdabanName, dto.kariKazeiKbn, dto.kashiFutanBumonCd, dto.kashiFutanBumonName, dto.kashiKamokuCd, dto.kashiKamokuName, dto.kashiKamokuEdabanCd, dto.kashiKamokuEdabanName, dto.kashiKazeiKbn, dto.uf1Cd, dto.uf1NameRyakushiki, dto.uf2Cd, dto.uf2NameRyakushiki, dto.uf3Cd, dto.uf3NameRyakushiki, dto.uf4Cd, dto.uf4NameRyakushiki, dto.uf5Cd, dto.uf5NameRyakushiki, dto.uf6Cd, dto.uf6NameRyakushiki, dto.uf7Cd, dto.uf7NameRyakushiki, dto.uf8Cd, dto.uf8NameRyakushiki, dto.uf9Cd, dto.uf9NameRyakushiki, dto.uf10Cd, dto.uf10NameRyakushiki, dto.projectCd, dto.projectName, dto.segmentCd, dto.segmentNameRyakushiki, dto.tekiyouCd, koushinUserId, dto.zeinukiKingaku, dto.shouhizeigaku, dto.shiharaisakiName, dto.jigyoushaKbn, dto.bunriKbn, dto.kariShiireKbn, dto.kashiShiireKbn, dto.invoiceDenpyou);
	}

	/**
	 * 通勤定期から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int delete(String denpyouId) {
		final String sql = "DELETE FROM tsuukinteiki " + this.whereSql;
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 支払日を更新する。
	 * @param denpyouId       伝票ID
	 * @param shiharaibi      支払日
	 * @param userId          登録ユーザーID
	 * @return                処理結果
	 */
	public int updateShiharaibi(String denpyouId, Date shiharaibi, String userId) {

		final String sql = "UPDATE tsuukinteiki "
						+ "SET shiharaibi=?, koushin_user_id=?, koushin_time=current_timestamp "
						+ "WHERE denpyou_id=?";
		return connection.update(sql, shiharaibi, userId, denpyouId);
	}
}