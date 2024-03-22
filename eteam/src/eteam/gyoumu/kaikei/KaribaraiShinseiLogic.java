package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;

/**
 * 仮払申請Logic
 */
public class KaribaraiShinseiLogic extends EteamAbstractLogic {
	
	/**
	 * 仮払テーブルに新規登録する。
	 * 
	 * @param denpyouId 伝票ID
	 * @param seisanYoteibi 精算予定日
	 * @param seisanKanryoubi 精算完了日
	 * @param shiharaibi 支払日
	 * @param karibaraiOn     仮払使用フラグ
	 * @param shiharaiKiboubi 支払希望日
	 * @param shiharaihouhou 支払方法
	 * @param tekiyou 摘要
	 * @param shinseiKingaku 申請金額
	 * @param karibaraiKingaku 仮払金額
	 * @param hf1Cd HF1コード
	 * @param hf1NameRyakushiki HF1名（略式）
	 * @param hf2Cd HF2コード
	 * @param hf2NameRyakushiki HF2名（略式）
	 * @param hf3Cd HF3コード
	 * @param hf3NameRyakushiki HF3名（略式）
	 * @param hf4Cd HF4コード
	 * @param hf4NameRyakushiki HF4名（略式）
	 * @param hf5Cd HF5コード
	 * @param hf5NameRyakushiki HF5名（略式）
	 * @param hf6Cd HF6コード
	 * @param hf6NameRyakushiki HF6名（略式）
	 * @param hf7Cd HF7コード
	 * @param hf7NameRyakushiki HF7名（略式）
	 * @param hf8Cd HF8コード
	 * @param hf8NameRyakushiki HF8名（略式）
	 * @param hf9Cd HF9コード
	 * @param hf9NameRyakushiki HF9名（略式）
	 * @param hf10Cd HF10コード
	 * @param hf10NameRyakushiki HF10名（略式）
	 * @param hosoku 補足
	 * @param shiwakeEdano 仕訳枝番号
	 * @param torihikiName 取引名
	 * @param kariFutanBumonCd 借方負担部門コード
	 * @param kariFutanBumonName 借方負担部門名
	 * @param torihikisakiCd             取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param kariKamokuCd 借方科目コード
	 * @param kariKamokuName 借方科目名
	 * @param kariKamokuEdabanCd 借方科目枝番コード
	 * @param kariKamokuEdabanName 借方科目枝番名
	 * @param kariKazeiKbn 借方課税区分
	 * @param kashiFutanBumonCd 貸方負担部門コード
	 * @param kashiFutanBumonName 貸方負担部門名
	 * @param kashiKamokuCd 貸方科目コード
	 * @param kashiKamokuName 貸方科目名
	 * @param kashiKamokuEdabanCd 貸方科目枝番コード
	 * @param kashiKamokuEdabanName 貸方科目枝番名
	 * @param kashiKazeiKbn 貸方課税区分
	 * @param uf1Cd UF1コード
	 * @param uf1NameRyakushiki UF1名（略式）
	 * @param uf2Cd UF2コード
	 * @param uf2NameRyakushiki UF2名（略式）
	 * @param uf3Cd UF3コード
	 * @param uf3NameRyakushiki UF3名（略式）
	 * @param uf4Cd UF4コード
	 * @param uf4NameRyakushiki UF4名（略式）
	 * @param uf5Cd UF5コード
	 * @param uf5NameRyakushiki UF5名（略式）
	 * @param uf6Cd UF6コード
	 * @param uf6NameRyakushiki UF6名（略式）
	 * @param uf7Cd UF7コード
	 * @param uf7NameRyakushiki UF7名（略式）
	 * @param uf8Cd UF8コード
	 * @param uf8NameRyakushiki UF8名（略式）
	 * @param uf9Cd UF9コード
	 * @param uf9NameRyakushiki UF9名（略式）
	 * @param uf10Cd UF10コード
	 * @param uf10NameRyakushiki UF10名（略式）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd 摘要コード
	 * @param userId ユーザーID
	 * @return 処理結果
	 */
	public int insert(
		String denpyouId,
		Date seisanYoteibi,
		Date seisanKanryoubi,
		Date shiharaibi,
		String karibaraiOn,
		Date shiharaiKiboubi,
		String shiharaihouhou,
		String tekiyou,
		BigDecimal shinseiKingaku,
		BigDecimal karibaraiKingaku,
		String hf1Cd,
		String hf1NameRyakushiki,
		String hf2Cd,
		String hf2NameRyakushiki,
		String hf3Cd,
		String hf3NameRyakushiki,
		String hf4Cd,
		String hf4NameRyakushiki,
		String hf5Cd,
		String hf5NameRyakushiki,
		String hf6Cd,
		String hf6NameRyakushiki,
		String hf7Cd,
		String hf7NameRyakushiki,
		String hf8Cd,
		String hf8NameRyakushiki,
		String hf9Cd,
		String hf9NameRyakushiki,
		String hf10Cd,
		String hf10NameRyakushiki,
		String hosoku,
		Integer shiwakeEdano,
		String torihikiName,
		String kariFutanBumonCd,
		String kariFutanBumonName,
		String torihikisakiCd,
		String torihikisakiNameRyakushiki,
		String kariKamokuCd,
		String kariKamokuName,
		String kariKamokuEdabanCd,
		String kariKamokuEdabanName,
		String kariKazeiKbn,
		String kashiFutanBumonCd,
		String kashiFutanBumonName,
		String kashiKamokuCd,
		String kashiKamokuName,
		String kashiKamokuEdabanCd,
		String kashiKamokuEdabanName,
		String kashiKazeiKbn,
		String uf1Cd,
		String uf1NameRyakushiki,
		String uf2Cd,
		String uf2NameRyakushiki,
		String uf3Cd,
		String uf3NameRyakushiki,
		String uf4Cd,
		String uf4NameRyakushiki,
		String uf5Cd,
		String uf5NameRyakushiki,
		String uf6Cd,
		String uf6NameRyakushiki,
		String uf7Cd,
		String uf7NameRyakushiki,
		String uf8Cd,
		String uf8NameRyakushiki,
		String uf9Cd,
		String uf9NameRyakushiki,
		String uf10Cd,
		String uf10NameRyakushiki,
		String projectCd,
		String projectName,
		String segmentCd,
		String segmentNameRyakushiki,
		String tekiyouCd,
		String userId
	) {
		final String sql =
				"INSERT INTO karibarai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(sql,
			denpyouId, seisanYoteibi, seisanKanryoubi, shiharaibi, karibaraiOn, shiharaiKiboubi, shiharaihouhou, tekiyou, shinseiKingaku, karibaraiKingaku, hf1Cd, hf1NameRyakushiki, hf2Cd, hf2NameRyakushiki, hf3Cd, hf3NameRyakushiki, hf4Cd, hf4NameRyakushiki, hf5Cd, hf5NameRyakushiki, hf6Cd, hf6NameRyakushiki, hf7Cd, hf7NameRyakushiki, hf8Cd, hf8NameRyakushiki, hf9Cd, hf9NameRyakushiki, hf10Cd, hf10NameRyakushiki, hosoku, shiwakeEdano, torihikiName, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki,kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn,
			uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd,
			userId, userId);
	}
	
	/**
	 * 仮払テーブルに更新する。
	 * @param denpyouId 伝票ID
	 * @param seisanYoteibi 精算予定日
	 * @param seisanKanryoubi 精算完了日
	 * @param shiharaibi 支払日
	 * @param karibaraiOn     仮払使用フラグ
	 * @param shiharaiKiboubi 支払希望日
	 * @param shiharaihouhou 支払方法
	 * @param tekiyou 摘要
	 * @param shinseiKingaku    申請金額
	 * @param karibaraiKingaku  仮払金額
	 * @param hf1Cd HF1コード
	 * @param hf1NameRyakushiki HF1名（略式）
	 * @param hf2Cd HF2コード
	 * @param hf2NameRyakushiki HF2名（略式）
	 * @param hf3Cd HF3コード
	 * @param hf3NameRyakushiki HF3名（略式）
	 * @param hf4Cd HF4コード
	 * @param hf4NameRyakushiki HF4名（略式）
	 * @param hf5Cd HF5コード
	 * @param hf5NameRyakushiki HF5名（略式）
	 * @param hf6Cd HF6コード
	 * @param hf6NameRyakushiki HF6名（略式）
	 * @param hf7Cd HF7コード
	 * @param hf7NameRyakushiki HF7名（略式）
	 * @param hf8Cd HF8コード
	 * @param hf8NameRyakushiki HF8名（略式）
	 * @param hf9Cd HF9コード
	 * @param hf9NameRyakushiki HF9名（略式）
	 * @param hf10Cd HF10コード
	 * @param hf10NameRyakushiki HF10名（略式）
	 * @param hosoku 補足
	 * @param shiwakeEdano 仕訳枝番号
	 * @param torihikiName 取引名
	 * @param kariFutanBumonCd 借方負担部門コード
	 * @param kariFutanBumonName 借方負担部門名
	 * @param torihikisakiCd 取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param kariKamokuCd 借方科目コード
	 * @param kariKamokuName 借方科目名
	 * @param kariKamokuEdabanCd 借方科目枝番コード
	 * @param kariKamokuEdabanName 借方科目枝番名
	 * @param kariKazeiKbn 借方課税区分
	 * @param kashiFutanBumonCd 貸方負担部門コード
	 * @param kashiFutanBumonName 貸方負担部門名
	 * @param kashiKamokuCd 貸方科目コード
	 * @param kashiKamokuName 貸方科目名
	 * @param kashiKamokuEdabanCd 貸方科目枝番コード
	 * @param kashiKamokuEdabanName 貸方科目枝番名
	 * @param kashiKazeiKbn 貸方課税区分
	 * @param uf1Cd UF1コード
	 * @param uf1NameRyakushiki UF1名（略式）
	 * @param uf2Cd UF2コード
	 * @param uf2NameRyakushiki UF2名（略式）
	 * @param uf3Cd UF3コード
	 * @param uf3NameRyakushiki UF3名（略式）
	 * @param uf4Cd UF4コード
	 * @param uf4NameRyakushiki UF4名（略式）
	 * @param uf5Cd UF5コード
	 * @param uf5NameRyakushiki UF5名（略式）
	 * @param uf6Cd UF6コード
	 * @param uf6NameRyakushiki UF6名（略式）
	 * @param uf7Cd UF7コード
	 * @param uf7NameRyakushiki UF7名（略式）
	 * @param uf8Cd UF8コード
	 * @param uf8NameRyakushiki UF8名（略式）
	 * @param uf9Cd UF9コード
	 * @param uf9NameRyakushiki UF9名（略式）
	 * @param uf10Cd UF10コード
	 * @param uf10NameRyakushiki UF10名（略式）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd 摘要コード
	 * @param userId ユーザーID
	 * @return 処理結果
	 */
	public int update(
		String denpyouId,
		Date seisanYoteibi,
		Date seisanKanryoubi,
		Date shiharaibi,
		String karibaraiOn,
		Date shiharaiKiboubi,
		String shiharaihouhou,
		String tekiyou,
		BigDecimal shinseiKingaku,
		BigDecimal karibaraiKingaku,
		String hf1Cd,
		String hf1NameRyakushiki,
		String hf2Cd,
		String hf2NameRyakushiki,
		String hf3Cd,
		String hf3NameRyakushiki,
		String hf4Cd,
		String hf4NameRyakushiki,
		String hf5Cd,
		String hf5NameRyakushiki,
		String hf6Cd,
		String hf6NameRyakushiki,
		String hf7Cd,
		String hf7NameRyakushiki,
		String hf8Cd,
		String hf8NameRyakushiki,
		String hf9Cd,
		String hf9NameRyakushiki,
		String hf10Cd,
		String hf10NameRyakushiki,
		String hosoku,
		Integer shiwakeEdano,
		String torihikiName,
		String kariFutanBumonCd,
		String kariFutanBumonName,
		String torihikisakiCd,
		String torihikisakiNameRyakushiki,
		String kariKamokuCd,
		String kariKamokuName,
		String kariKamokuEdabanCd,
		String kariKamokuEdabanName,
		String kariKazeiKbn,
		String kashiFutanBumonCd,
		String kashiFutanBumonName,
		String kashiKamokuCd,
		String kashiKamokuName,
		String kashiKamokuEdabanCd,
		String kashiKamokuEdabanName,
		String kashiKazeiKbn,
		String uf1Cd,
		String uf1NameRyakushiki,
		String uf2Cd,
		String uf2NameRyakushiki,
		String uf3Cd,
		String uf3NameRyakushiki,
		String uf4Cd,
		String uf4NameRyakushiki,
		String uf5Cd,
		String uf5NameRyakushiki,
		String uf6Cd,
		String uf6NameRyakushiki,
		String uf7Cd,
		String uf7NameRyakushiki,
		String uf8Cd,
		String uf8NameRyakushiki,
		String uf9Cd,
		String uf9NameRyakushiki,
		String uf10Cd,
		String uf10NameRyakushiki,
		String projectCd,
		String projectName,
		String segmentCd,
		String segmentNameRyakushiki,
		String tekiyouCd,
		String userId
	) {
		final String sql =
				"UPDATE karibarai SET "
			+ "seisan_yoteibi = ?, seisan_kanryoubi = ?, shiharaibi = ?, karibarai_on = ?, shiharaikiboubi=?, shiharaihouhou = ?, tekiyou = ?, kingaku = ?, karibarai_kingaku = ? , hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, "
			+ "hosoku = ?, shiwake_edano = ?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?," 
			+ "project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "WHERE denpyou_id = ? ";
		return connection.update(sql,
				seisanYoteibi, seisanKanryoubi, shiharaibi, karibaraiOn, shiharaiKiboubi, shiharaihouhou, tekiyou, shinseiKingaku, karibaraiKingaku, hf1Cd, hf1NameRyakushiki, hf2Cd, hf2NameRyakushiki, hf3Cd, hf3NameRyakushiki, hf4Cd, hf4NameRyakushiki, hf5Cd, hf5NameRyakushiki, hf6Cd, hf6NameRyakushiki, hf7Cd, hf7NameRyakushiki, hf8Cd, hf8NameRyakushiki, hf9Cd, hf9NameRyakushiki, hf10Cd, hf10NameRyakushiki,
				hosoku, shiwakeEdano, torihikiName, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, 
				kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
				projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd,
				userId,
				denpyouId);
	}
	
	/**
	 * 支払日を更新する。
	 * @param denpyouId       伝票ID
	 * @param shiharaibi      支払日
	 * @param userId          登録ユーザーID
	 * @return                処理結果
	 */
	public int updateShiharaibi(String denpyouId, Date shiharaibi, String userId) {
		final String sql =
				"UPDATE karibarai SET "
			+ "shiharaibi = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "WHERE denpyou_id = ? ";
		return connection.update(sql, shiharaibi, userId, denpyouId);
	}

}
