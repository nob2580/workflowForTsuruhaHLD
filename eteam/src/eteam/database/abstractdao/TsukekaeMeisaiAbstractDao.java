package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.TsukekaeMeisai;

/**
 * 付替明細に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class TsukekaeMeisaiAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO tsukekae_meisai (denpyou_id, denpyou_edano, tekiyou, kingaku, hontai_kingaku, shouhizeigaku, bikou, saki_kamoku_cd, saki_kamoku_name, saki_kamoku_edaban_cd, saki_kamoku_edaban_name, saki_futan_bumon_cd, saki_futan_bumon_name, saki_torihikisaki_cd, saki_torihikisaki_name_ryakushiki, saki_kazei_kbn, saki_uf1_cd, saki_uf1_name_ryakushiki, saki_uf2_cd, saki_uf2_name_ryakushiki, saki_uf3_cd, saki_uf3_name_ryakushiki, saki_uf4_cd, saki_uf4_name_ryakushiki, saki_uf5_cd, saki_uf5_name_ryakushiki, saki_uf6_cd, saki_uf6_name_ryakushiki, saki_uf7_cd, saki_uf7_name_ryakushiki, saki_uf8_cd, saki_uf8_name_ryakushiki, saki_uf9_cd, saki_uf9_name_ryakushiki, saki_uf10_cd, saki_uf10_name_ryakushiki, saki_project_cd, saki_project_name, saki_segment_cd, saki_segment_name_ryakushiki, touroku_user_id, touroku_time, koushin_user_id, koushin_time, saki_jigyousha_kbn, saki_bunri_kbn, saki_shiire_kbn, saki_zeigaku_houshiki) VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?)";

	/**
	 * 主キー条件定型部
	 */
	protected final String whereSql = " WHERE denpyou_id = ? AND denpyou_edano = ?";

	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected TsukekaeMeisai mapToDto(GMap map) {
		return map == null ? null : new TsukekaeMeisai(map);
	}

	/**
	 * @param mapList 検索結果GMap
	 * @return dtoList
	 */
	protected List<TsukekaeMeisai> mapToDto(List<GMap> mapList) {
		List<TsukekaeMeisai> dtoList = new ArrayList<TsukekaeMeisai>();
		for (var map : mapList) {
			dtoList.add(new TsukekaeMeisai(map));
		}
		return dtoList;
	}

	/**
	 * 付替明細のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId, int denpyouEdano) {
		return this.find(denpyouId, denpyouEdano) != null;
	}

	/**
	 * 付替明細から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return 付替明細DTO
	 */
	public TsukekaeMeisai find(String denpyouId, int denpyouEdano) {
		final String sql = "SELECT * FROM tsukekae_meisai " + this.whereSql;
		return mapToDto(connection.find(sql, denpyouId, denpyouEdano));
	}

	/**
	 * 付替明細からレコードを全件取得 ※大量データ取得に注意
	 * @return List<付替明細DTO>
	 */
	public List<TsukekaeMeisai> load() {
		final String sql = "SELECT * FROM tsukekae_meisai  ORDER BY denpyou_id, denpyou_edano";
		return mapToDto(connection.load(sql));
	}

	/**
	 * 付替明細から一部キー（伝票ID）指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return List<付替明細>DTO
	 */
	public List<TsukekaeMeisai> loadByDenpyouId(String denpyouId) {
		final String sql = "SELECT * FROM tsukekae_meisai WHERE denpyou_id = ? "
							+ "ORDER BY denpyou_id, denpyou_edano";
		return mapToDto(connection.load(sql, denpyouId));
	}

	/**
	 * 付替明細登録
	 * @param dto 付替明細
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int insert(TsukekaeMeisai dto, String koushinUserId) {
		return connection.update(this.insertSql, dto.denpyouId, dto.denpyouEdano, dto.tekiyou, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.bikou, dto.sakiKamokuCd, dto.sakiKamokuName, dto.sakiKamokuEdabanCd, dto.sakiKamokuEdabanName, dto.sakiFutanBumonCd, dto.sakiFutanBumonName, dto.sakiTorihikisakiCd, dto.sakiTorihikisakiNameRyakushiki, dto.sakiKazeiKbn, dto.sakiUf1Cd, dto.sakiUf1NameRyakushiki, dto.sakiUf2Cd, dto.sakiUf2NameRyakushiki, dto.sakiUf3Cd, dto.sakiUf3NameRyakushiki, dto.sakiUf4Cd, dto.sakiUf4NameRyakushiki, dto.sakiUf5Cd, dto.sakiUf5NameRyakushiki, dto.sakiUf6Cd, dto.sakiUf6NameRyakushiki, dto.sakiUf7Cd, dto.sakiUf7NameRyakushiki, dto.sakiUf8Cd, dto.sakiUf8NameRyakushiki, dto.sakiUf9Cd, dto.sakiUf9NameRyakushiki, dto.sakiUf10Cd, dto.sakiUf10NameRyakushiki, dto.sakiProjectCd, dto.sakiProjectName, dto.sakiSegmentCd, dto.sakiSegmentNameRyakushiki, koushinUserId, koushinUserId, dto.sakiJigyoushaKbn, dto.sakiBunriKbn, dto.sakiShiireKbn, dto.sakiZeigakuHoushiki);
	}

	/**
	 * 付替明細更新
	 * @param dto 付替明細
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int update(TsukekaeMeisai dto, String koushinUserId) {
		final String sql = "UPDATE tsukekae_meisai SET  tekiyou = ?, kingaku = ?, hontai_kingaku = ?, shouhizeigaku = ?, bikou = ?, saki_kamoku_cd = ?, saki_kamoku_name = ?, saki_kamoku_edaban_cd = ?, saki_kamoku_edaban_name = ?, saki_futan_bumon_cd = ?, saki_futan_bumon_name = ?, saki_torihikisaki_cd = ?, saki_torihikisaki_name_ryakushiki = ?, saki_kazei_kbn = ?, saki_uf1_cd = ?, saki_uf1_name_ryakushiki = ?, saki_uf2_cd = ?, saki_uf2_name_ryakushiki = ?, saki_uf3_cd = ?, saki_uf3_name_ryakushiki = ?, saki_uf4_cd = ?, saki_uf4_name_ryakushiki = ?, saki_uf5_cd = ?, saki_uf5_name_ryakushiki = ?, saki_uf6_cd = ?, saki_uf6_name_ryakushiki = ?, saki_uf7_cd = ?, saki_uf7_name_ryakushiki = ?, saki_uf8_cd = ?, saki_uf8_name_ryakushiki = ?, saki_uf9_cd = ?, saki_uf9_name_ryakushiki = ?, saki_uf10_cd = ?, saki_uf10_name_ryakushiki = ?, saki_project_cd = ?, saki_project_name = ?, saki_segment_cd = ?, saki_segment_name_ryakushiki = ?, koushin_user_id = ?, koushin_time = current_timestamp, saki_jigyousha_kbn = ?, saki_bunri_kbn = ?, saki_shiire_kbn = ?, saki_zeigaku_houshiki = ? " + this.whereSql;
		return connection.update(sql, dto.tekiyou, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.bikou, dto.sakiKamokuCd, dto.sakiKamokuName, dto.sakiKamokuEdabanCd, dto.sakiKamokuEdabanName, dto.sakiFutanBumonCd, dto.sakiFutanBumonName, dto.sakiTorihikisakiCd, dto.sakiTorihikisakiNameRyakushiki, dto.sakiKazeiKbn, dto.sakiUf1Cd, dto.sakiUf1NameRyakushiki, dto.sakiUf2Cd, dto.sakiUf2NameRyakushiki, dto.sakiUf3Cd, dto.sakiUf3NameRyakushiki, dto.sakiUf4Cd, dto.sakiUf4NameRyakushiki, dto.sakiUf5Cd, dto.sakiUf5NameRyakushiki, dto.sakiUf6Cd, dto.sakiUf6NameRyakushiki, dto.sakiUf7Cd, dto.sakiUf7NameRyakushiki, dto.sakiUf8Cd, dto.sakiUf8NameRyakushiki, dto.sakiUf9Cd, dto.sakiUf9NameRyakushiki, dto.sakiUf10Cd, dto.sakiUf10NameRyakushiki, dto.sakiProjectCd, dto.sakiProjectName, dto.sakiSegmentCd, dto.sakiSegmentNameRyakushiki, koushinUserId, dto.sakiJigyoushaKbn, dto.sakiBunriKbn, dto.sakiShiireKbn, dto.sakiZeigakuHoushiki, dto.denpyouId, dto.denpyouEdano);
	}

	/**
	 * 付替明細登録or更新
	 * 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	 * @param dto 付替明細
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int upsert(TsukekaeMeisai dto, String koushinUserId) {
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT tsukekae_meisai_pkey "
			+ "DO UPDATE SET  tekiyou = ?, kingaku = ?, hontai_kingaku = ?, shouhizeigaku = ?, bikou = ?, saki_kamoku_cd = ?, saki_kamoku_name = ?, saki_kamoku_edaban_cd = ?, saki_kamoku_edaban_name = ?, saki_futan_bumon_cd = ?, saki_futan_bumon_name = ?, saki_torihikisaki_cd = ?, saki_torihikisaki_name_ryakushiki = ?, saki_kazei_kbn = ?, saki_uf1_cd = ?, saki_uf1_name_ryakushiki = ?, saki_uf2_cd = ?, saki_uf2_name_ryakushiki = ?, saki_uf3_cd = ?, saki_uf3_name_ryakushiki = ?, saki_uf4_cd = ?, saki_uf4_name_ryakushiki = ?, saki_uf5_cd = ?, saki_uf5_name_ryakushiki = ?, saki_uf6_cd = ?, saki_uf6_name_ryakushiki = ?, saki_uf7_cd = ?, saki_uf7_name_ryakushiki = ?, saki_uf8_cd = ?, saki_uf8_name_ryakushiki = ?, saki_uf9_cd = ?, saki_uf9_name_ryakushiki = ?, saki_uf10_cd = ?, saki_uf10_name_ryakushiki = ?, saki_project_cd = ?, saki_project_name = ?, saki_segment_cd = ?, saki_segment_name_ryakushiki = ?, koushin_user_id = ?, koushin_time = current_timestamp, saki_jigyousha_kbn = ?, saki_bunri_kbn = ?, saki_shiire_kbn = ?, saki_zeigaku_houshiki = ? ";
		return connection.update(sql, dto.denpyouId, dto.denpyouEdano, dto.tekiyou, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.bikou, dto.sakiKamokuCd, dto.sakiKamokuName, dto.sakiKamokuEdabanCd, dto.sakiKamokuEdabanName, dto.sakiFutanBumonCd, dto.sakiFutanBumonName, dto.sakiTorihikisakiCd, dto.sakiTorihikisakiNameRyakushiki, dto.sakiKazeiKbn, dto.sakiUf1Cd, dto.sakiUf1NameRyakushiki, dto.sakiUf2Cd, dto.sakiUf2NameRyakushiki, dto.sakiUf3Cd, dto.sakiUf3NameRyakushiki, dto.sakiUf4Cd, dto.sakiUf4NameRyakushiki, dto.sakiUf5Cd, dto.sakiUf5NameRyakushiki, dto.sakiUf6Cd, dto.sakiUf6NameRyakushiki, dto.sakiUf7Cd, dto.sakiUf7NameRyakushiki, dto.sakiUf8Cd, dto.sakiUf8NameRyakushiki, dto.sakiUf9Cd, dto.sakiUf9NameRyakushiki, dto.sakiUf10Cd, dto.sakiUf10NameRyakushiki, dto.sakiProjectCd, dto.sakiProjectName, dto.sakiSegmentCd, dto.sakiSegmentNameRyakushiki, koushinUserId, koushinUserId, dto.sakiJigyoushaKbn, dto.sakiBunriKbn, dto.sakiShiireKbn, dto.sakiZeigakuHoushiki, dto.tekiyou, dto.kingaku, dto.hontaiKingaku, dto.shouhizeigaku, dto.bikou, dto.sakiKamokuCd, dto.sakiKamokuName, dto.sakiKamokuEdabanCd, dto.sakiKamokuEdabanName, dto.sakiFutanBumonCd, dto.sakiFutanBumonName, dto.sakiTorihikisakiCd, dto.sakiTorihikisakiNameRyakushiki, dto.sakiKazeiKbn, dto.sakiUf1Cd, dto.sakiUf1NameRyakushiki, dto.sakiUf2Cd, dto.sakiUf2NameRyakushiki, dto.sakiUf3Cd, dto.sakiUf3NameRyakushiki, dto.sakiUf4Cd, dto.sakiUf4NameRyakushiki, dto.sakiUf5Cd, dto.sakiUf5NameRyakushiki, dto.sakiUf6Cd, dto.sakiUf6NameRyakushiki, dto.sakiUf7Cd, dto.sakiUf7NameRyakushiki, dto.sakiUf8Cd, dto.sakiUf8NameRyakushiki, dto.sakiUf9Cd, dto.sakiUf9NameRyakushiki, dto.sakiUf10Cd, dto.sakiUf10NameRyakushiki, dto.sakiProjectCd, dto.sakiProjectName, dto.sakiSegmentCd, dto.sakiSegmentNameRyakushiki, koushinUserId, dto.sakiJigyoushaKbn, dto.sakiBunriKbn, dto.sakiShiireKbn, dto.sakiZeigakuHoushiki);
	}

	/**
	 * 付替明細から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int denpyouEdano) {
		final String sql = "DELETE FROM tsukekae_meisai " + this.whereSql;
		return connection.update(sql, denpyouId, denpyouEdano);
	}
	
	/**
	 * 付替明細から一部キー（伝票ID）指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int deleteByDenpyouId(String denpyouId) {
		final String sql = "DELETE FROM tsukekae_meisai WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
}