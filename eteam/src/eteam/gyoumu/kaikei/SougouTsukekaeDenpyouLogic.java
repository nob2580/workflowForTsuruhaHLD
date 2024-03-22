package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;

/**
 * 総合付替伝票画面Logic
 */
public class SougouTsukekaeDenpyouLogic extends EteamAbstractLogic {
	
	/**
	 * 新規登録する。
	 * @param denpyouId 伝票ID
	 * @param denpyouDate 伝票日付
	 * @param shouhyouShoruiFlg 証憑書類フラグ
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param kingakuGoukei 金額合計
	 * @param hf1Cd ヘッダーフィールド１コード
	 * @param hf1Name ヘッダーフィールド１名
	 * @param hf2Cd ヘッダーフィールド２コード
	 * @param hf2Name ヘッダーフィールド２名
	 * @param hf3Cd ヘッダーフィールド３コード
	 * @param hf3Name ヘッダーフィールド３名	
	 * @param hf4Cd                      ヘッダーフィールド４コード
	 * @param hf4Name                    ヘッダーフィールド４名
	 * @param hf5Cd                      ヘッダーフィールド５コード
	 * @param hf5Name                    ヘッダーフィールド５名
	 * @param hf6Cd                      ヘッダーフィールド６コード
	 * @param hf6Name                    ヘッダーフィールド６名
	 * @param hf7Cd                      ヘッダーフィールド７コード
	 * @param hf7Name                    ヘッダーフィールド７名
	 * @param hf8Cd                      ヘッダーフィールド８コード
	 * @param hf8Name                    ヘッダーフィールド８名
	 * @param hf9Cd                      ヘッダーフィールド９コード
	 * @param hf9Name                    ヘッダーフィールド９名
	 * @param hf10Cd                      ヘッダーフィールド１０コード
	 * @param hf10Name                    ヘッダーフィールド１０名
	 * @param hosoku 補足
	 * @param tsukekaeKbn 付替区分
	 * @param motoKamokuCd 付替元科目コード
	 * @param motoKamokuName 付替元科目名
	 * @param motoKamokuEdabanCd 付替元科目枝番コード
	 * @param motoKamokuEdabanName 付替元科目枝番名
	 * @param motoFutanBumonCd 付替元負担部門コード
	 * @param motoFutanBumonName 付替元負担部門名
	 * @param motoTorihikisakiCd 付替元取引先コード
	 * @param motoTorihikisakiNameRyakushiki 付替元取引先名（略式）
	 * @param motoKazeiKbn 付替元課税区分
	 * @param motoUf1Cd 付替元UF1コード
	 * @param motoUf1NameRyakushiki 付替元UF1名（略式）
	 * @param motoUf2Cd 付替元UF2コード
	 * @param motoUf2NameRyakushiki 付替元UF2名（略式）
	 * @param motoUf3Cd 付替元UF3コード
	 * @param motoUf3NameRyakushiki 付替元UF3名（略式）
	 * @param motoUf4Cd       付替元UF4コード
	 * @param motoUf4NameRyakushiki    付替元UF4名（略式）
	 * @param motoUf5Cd       付替元UF5コード
	 * @param motoUf5NameRyakushiki    付替元UF5名（略式）
	 * @param motoUf6Cd       付替元UF6コード
	 * @param motoUf6NameRyakushiki    付替元UF6名（略式）
	 * @param motoUf7Cd       付替元UF7コード
	 * @param motoUf7NameRyakushiki    付替元UF7名（略式）
	 * @param motoUf8Cd       付替元UF8コード
	 * @param motoUf8NameRyakushiki    付替元UF8名（略式）
	 * @param motoUf9Cd       付替元UF9コード
	 * @param motoUf9NameRyakushiki    付替元UF9名（略式）
	 * @param motoUf10Cd       付替元UF10コード
	 * @param motoUf10NameRyakushiki    付替元UF10名（略式）
	 * @param motoProjectCd 付替元プロジェクトコード
	 * @param motoProjectName 付替元プロジェクト名
	 * @param motoSegmentCd 付替元セグメントコード
	 * @param motoSegmentName 付替元セグメント名
	 * @param userId ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
	public int insertShinsei(
		String denpyouId,
		Date denpyouDate,
		String shouhyouShoruiFlg,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		BigDecimal kingakuGoukei,
		String hf1Cd,
		String hf1Name,
		String hf2Cd,
		String hf2Name,
		String hf3Cd,
		String hf3Name,
		String hf4Cd,
		String hf4Name,
		String hf5Cd,
		String hf5Name,
		String hf6Cd,
		String hf6Name,
		String hf7Cd,
		String hf7Name,
		String hf8Cd,
		String hf8Name,
		String hf9Cd,
		String hf9Name,
		String hf10Cd,
		String hf10Name,
		String hosoku,
		String tsukekaeKbn,
		String motoKamokuCd,
		String motoKamokuName,
		String motoKamokuEdabanCd,
		String motoKamokuEdabanName,
		String motoFutanBumonCd,
		String motoFutanBumonName,
		String motoTorihikisakiCd,
		String motoTorihikisakiNameRyakushiki,
		String motoKazeiKbn,
		String motoUf1Cd,
		String motoUf1NameRyakushiki,
		String motoUf2Cd,
		String motoUf2NameRyakushiki,
		String motoUf3Cd,
		String motoUf3NameRyakushiki,
		String  motoUf4Cd,
		String  motoUf4NameRyakushiki,
		String  motoUf5Cd,
		String  motoUf5NameRyakushiki,
		String  motoUf6Cd,
		String  motoUf6NameRyakushiki,
		String  motoUf7Cd,
		String  motoUf7NameRyakushiki,
		String  motoUf8Cd,
		String  motoUf8NameRyakushiki,
		String  motoUf9Cd,
		String  motoUf9NameRyakushiki,
		String  motoUf10Cd,
		String  motoUf10NameRyakushiki,
		String motoProjectCd,
		String motoProjectName,
		String motoSegmentCd,
		String motoSegmentName,
		String userId
	) {
		final String sql =
				"INSERT INTO tsukekae "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouDate, shouhyouShoruiFlg, zeiritsu, keigenZeiritsuKbn, kingakuGoukei, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			hosoku, tsukekaeKbn, motoKamokuCd, motoKamokuName, motoKamokuEdabanCd, motoKamokuEdabanName, motoFutanBumonCd, motoFutanBumonName, motoTorihikisakiCd, motoTorihikisakiNameRyakushiki, motoKazeiKbn, motoUf1Cd, motoUf1NameRyakushiki, motoUf2Cd, motoUf2NameRyakushiki, motoUf3Cd, motoUf3NameRyakushiki, motoUf4Cd, motoUf4NameRyakushiki, motoUf5Cd, motoUf5NameRyakushiki, motoUf6Cd, motoUf6NameRyakushiki, motoUf7Cd, motoUf7NameRyakushiki, motoUf8Cd, motoUf8NameRyakushiki, motoUf9Cd, motoUf9NameRyakushiki, motoUf10Cd, motoUf10NameRyakushiki,
			motoProjectCd, motoProjectName, motoSegmentCd, motoSegmentName, 
			userId, userId);
	}
	
	/**
	 * 更新する。
	 * @param denpyouId 伝票ID
	 * @param denpyouDate 伝票日付
	 * @param shouhyouShoruiFlg 証憑書類フラグ
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param kingakuGoukei 金額合計
	 * @param hf1Cd ヘッダーフィールド１コード
	 * @param hf1Name ヘッダーフィールド１名
	 * @param hf2Cd ヘッダーフィールド２コード
	 * @param hf2Name ヘッダーフィールド２名
	 * @param hf3Cd ヘッダーフィールド３コード
	 * @param hf3Name ヘッダーフィールド３名	
	 * @param hf4Cd                      ヘッダーフィールド４コード
	 * @param hf4Name                    ヘッダーフィールド４名
	 * @param hf5Cd                      ヘッダーフィールド５コード
	 * @param hf5Name                    ヘッダーフィールド５名
	 * @param hf6Cd                      ヘッダーフィールド６コード
	 * @param hf6Name                    ヘッダーフィールド６名
	 * @param hf7Cd                      ヘッダーフィールド７コード
	 * @param hf7Name                    ヘッダーフィールド７名
	 * @param hf8Cd                      ヘッダーフィールド８コード
	 * @param hf8Name                    ヘッダーフィールド８名
	 * @param hf9Cd                      ヘッダーフィールド９コード
	 * @param hf9Name                    ヘッダーフィールド９名
	 * @param hf10Cd                      ヘッダーフィールド１０コード
	 * @param hf10Name                    ヘッダーフィールド１０名
	 * @param hosoku 補足
	 * @param tsukekaeKbn 付替区分
	 * @param motoKamokuCd 付替元科目コード
	 * @param motoKamokuName 付替元科目名
	 * @param motoKamokuEdabanCd 付替元科目枝番コード
	 * @param motoKamokuEdabanName 付替元科目枝番名
	 * @param motoFutanBumonCd 付替元負担部門コード
	 * @param motoFutanBumonName 付替元負担部門名
	 * @param motoTorihikisakiCd 付替元取引先コード
	 * @param motoTorihikisakiNameRyakushiki 付替元取引先名（略式）
	 * @param motoKazeiKbn 付替元課税区分
	 * @param motoUf1Cd 付替元UF1コード
	 * @param motoUf1NameRyakushiki 付替元UF1名（略式）
	 * @param motoUf2Cd 付替元UF2コード
	 * @param motoUf2NameRyakushiki 付替元UF2名（略式）
	 * @param motoUf3Cd 付替元UF3コード
	 * @param motoUf3NameRyakushiki 付替元UF3名（略式）
	 * @param motoUf4Cd       付替元UF4コード
	 * @param motoUf4NameRyakushiki    付替元UF4名（略式）
	 * @param motoUf5Cd       付替元UF5コード
	 * @param motoUf5NameRyakushiki    付替元UF5名（略式）
	 * @param motoUf6Cd       付替元UF6コード
	 * @param motoUf6NameRyakushiki    付替元UF6名（略式）
	 * @param motoUf7Cd       付替元UF7コード
	 * @param motoUf7NameRyakushiki    付替元UF7名（略式）
	 * @param motoUf8Cd       付替元UF8コード
	 * @param motoUf8NameRyakushiki    付替元UF8名（略式）
	 * @param motoUf9Cd       付替元UF9コード
	 * @param motoUf9NameRyakushiki    付替元UF9名（略式）
	 * @param motoUf10Cd       付替元UF10コード
	 * @param motoUf10NameRyakushiki    付替元UF10名（略式）
	 * @param motoProjectCd 付替元プロジェクトコード
	 * @param motoProjectName 付替元プロジェクト名
	 * @param motoSegmentCd 付替元セグメントコード
	 * @param motoSegmentName 付替元セグメント名
	 * @param userId ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
	public int updateShinsei(
		String denpyouId,
		Date denpyouDate,
		String shouhyouShoruiFlg,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		BigDecimal kingakuGoukei,
		String hf1Cd,
		String hf1Name,
		String hf2Cd,
		String hf2Name,
		String hf3Cd,
		String hf3Name,
		String hf4Cd,
		String hf4Name,
		String hf5Cd,
		String hf5Name,
		String hf6Cd,
		String hf6Name,
		String hf7Cd,
		String hf7Name,
		String hf8Cd,
		String hf8Name,
		String hf9Cd,
		String hf9Name,
		String hf10Cd,
		String hf10Name,
		String hosoku,
		String tsukekaeKbn,
		String motoKamokuCd,
		String motoKamokuName,
		String motoKamokuEdabanCd,
		String motoKamokuEdabanName,
		String motoFutanBumonCd,
		String motoFutanBumonName,
		String motoTorihikisakiCd,
		String motoTorihikisakiNameRyakushiki,
		String motoKazeiKbn,
		String motoUf1Cd,
		String motoUf1NameRyakushiki,
		String motoUf2Cd,
		String motoUf2NameRyakushiki,
		String motoUf3Cd,
		String motoUf3NameRyakushiki,
		String  motoUf4Cd,
		String  motoUf4NameRyakushiki,
		String  motoUf5Cd,
		String  motoUf5NameRyakushiki,
		String  motoUf6Cd,
		String  motoUf6NameRyakushiki,
		String  motoUf7Cd,
		String  motoUf7NameRyakushiki,
		String  motoUf8Cd,
		String  motoUf8NameRyakushiki,
		String  motoUf9Cd,
		String  motoUf9NameRyakushiki,
		String  motoUf10Cd,
		String  motoUf10NameRyakushiki,
		String motoProjectCd,
		String motoProjectName,
		String motoSegmentCd,
		String motoSegmentName,
		String userId
	) {
		final String sql =
				"UPDATE tsukekae SET "
			+ "denpyou_date = ?, shouhyou_shorui_flg = ?, zeiritsu = ?, keigen_Zeiritsu_Kbn = ?, kingaku_goukei = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, "
			+ "hosoku = ?, tsukekae_kbn = ?, moto_kamoku_cd = ?, moto_kamoku_name = ?, moto_kamoku_edaban_cd = ?, moto_kamoku_edaban_name = ?, moto_futan_bumon_cd = ?, moto_futan_bumon_name = ?, moto_torihikisaki_cd = ?, moto_torihikisaki_name_ryakushiki = ?, moto_kazei_kbn = ?, moto_uf1_cd = ?, moto_uf1_name_ryakushiki = ?, moto_uf2_cd = ?, moto_uf2_name_ryakushiki = ?, moto_uf3_cd = ?, moto_uf3_name_ryakushiki = ?, moto_uf4_cd = ?, moto_uf4_name_ryakushiki = ?, moto_uf5_cd = ?, moto_uf5_name_ryakushiki = ?, moto_uf6_cd = ?, moto_uf6_name_ryakushiki = ?, moto_uf7_cd = ?, moto_uf7_name_ryakushiki = ?, moto_uf8_cd = ?, moto_uf8_name_ryakushiki = ?, moto_uf9_cd = ?, moto_uf9_name_ryakushiki = ?, moto_uf10_cd = ?, moto_uf10_name_ryakushiki = ?, "
			+ "moto_project_cd = ?, moto_project_name = ?, moto_segment_cd = ?, moto_segment_name_ryakushiki = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "WHERE denpyou_id = ? ";
		return connection.update(
			sql,
			denpyouDate, shouhyouShoruiFlg, zeiritsu, keigenZeiritsuKbn, kingakuGoukei, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			hosoku, tsukekaeKbn, motoKamokuCd, motoKamokuName, motoKamokuEdabanCd, motoKamokuEdabanName, motoFutanBumonCd, motoFutanBumonName, motoTorihikisakiCd, motoTorihikisakiNameRyakushiki, motoKazeiKbn, motoUf1Cd, motoUf1NameRyakushiki, motoUf2Cd, motoUf2NameRyakushiki, motoUf3Cd, motoUf3NameRyakushiki, motoUf4Cd, motoUf4NameRyakushiki, motoUf5Cd, motoUf5NameRyakushiki, motoUf6Cd, motoUf6NameRyakushiki, motoUf7Cd, motoUf7NameRyakushiki, motoUf8Cd, motoUf8NameRyakushiki, motoUf9Cd, motoUf9NameRyakushiki, motoUf10Cd, motoUf10NameRyakushiki,
			motoProjectCd, motoProjectName, motoSegmentCd, motoSegmentName,
			userId,
			denpyouId);
	}

	/**
	 * 明細の登録
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @param tekiyou 摘要
	 * @param kingaku 金額
	 * @param hontaiKingaku 本体金額
	 * @param shouhizeigaku 消費税額
	 * @param bikou 備考（会計伝票）
	 * @param sakiKamokuCd 付替先科目コード
	 * @param sakiKamokuName 付替先科目名
	 * @param sakiKamokuEdabanCd 付替先科目枝番コード
	 * @param sakiKamokuEdabanName 付替先科目枝番名
	 * @param sakiFutanBumonCd 付替先負担部門コード
	 * @param sakiFutanBumonName 付替先負担部門名
	 * @param sakiTorihikisakiCd 付替先取引先コード
	 * @param sakiTorihikisakiNameRyakushiki 付替先取引先名（略式）
	 * @param sakiKazeiKbn 付替先課税区分
	 * @param sakiUf1Cd 付替先UF1コード
	 * @param sakiUf1NameRyakushiki 付替先UF1名（略式）
	 * @param sakiUf2Cd 付替先UF2コード
	 * @param sakiUf2NameRyakushiki 付替先UF2名（略式）
	 * @param sakiUf3Cd 付替先UF3コード
	 * @param sakiUf3NameRyakushiki 付替先UF3名（略式）
	 * @param sakiUf4Cd       付替先UF4コード
	 * @param sakiUf4NameRyakushiki    付替先UF4名（略式）
	 * @param sakiUf5Cd       付替先UF5コード
	 * @param sakiUf5NameRyakushiki    付替先UF5名（略式）
	 * @param sakiUf6Cd       付替先UF6コード
	 * @param sakiUf6NameRyakushiki    付替先UF6名（略式）
	 * @param sakiUf7Cd       付替先UF7コード
	 * @param sakiUf7NameRyakushiki    付替先UF7名（略式）
	 * @param sakiUf8Cd       付替先UF8コード
	 * @param sakiUf8NameRyakushiki    付替先UF8名（略式）
	 * @param sakiUf9Cd       付替先UF9コード
	 * @param sakiUf9NameRyakushiki    付替先UF9名（略式）
	 * @param sakiUf10Cd       付替先UF10コード
	 * @param sakiUf10NameRyakushiki    付替先UF10名（略式）
	 * @param sakiProjectCd 付替先プロジェクトコード
	 * @param sakiProjectName 付替先プロジェクト名
	 * @param sakiSegmentCd 付替先セグメントコード
	 * @param sakiSegmentName 付替先セグメント名
	 * @param userId 登録ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
	public int insertMeisai(
		String denpyouId,
		int denpyouEdano,
		String tekiyou,
		BigDecimal kingaku,
		BigDecimal hontaiKingaku,
		BigDecimal shouhizeigaku,
		String bikou,
		String sakiKamokuCd,
		String sakiKamokuName,
		String sakiKamokuEdabanCd,
		String sakiKamokuEdabanName,
		String sakiFutanBumonCd,
		String sakiFutanBumonName,
		String sakiTorihikisakiCd,
		String sakiTorihikisakiNameRyakushiki,
		String sakiKazeiKbn,
		String sakiUf1Cd,
		String sakiUf1NameRyakushiki,
		String sakiUf2Cd,
		String sakiUf2NameRyakushiki,
		String sakiUf3Cd,
		String sakiUf3NameRyakushiki,
		String  sakiUf4Cd,
		String  sakiUf4NameRyakushiki,
		String  sakiUf5Cd,
		String  sakiUf5NameRyakushiki,
		String  sakiUf6Cd,
		String  sakiUf6NameRyakushiki,
		String  sakiUf7Cd,
		String  sakiUf7NameRyakushiki,
		String  sakiUf8Cd,
		String  sakiUf8NameRyakushiki,
		String  sakiUf9Cd,
		String  sakiUf9NameRyakushiki,
		String  sakiUf10Cd,
		String  sakiUf10NameRyakushiki,
		String sakiProjectCd,
		String sakiProjectName,
		String sakiSegmentCd,
		String sakiSegmentName,
		String userId
	) {
		final String sql =
				"INSERT INTO tsukekae_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouEdano, tekiyou, kingaku, hontaiKingaku, shouhizeigaku, bikou, sakiKamokuCd, sakiKamokuName, sakiKamokuEdabanCd, sakiKamokuEdabanName, sakiFutanBumonCd, sakiFutanBumonName, sakiTorihikisakiCd, sakiTorihikisakiNameRyakushiki, sakiKazeiKbn, sakiUf1Cd, sakiUf1NameRyakushiki, sakiUf2Cd, sakiUf2NameRyakushiki, sakiUf3Cd, sakiUf3NameRyakushiki, sakiUf4Cd, sakiUf4NameRyakushiki, sakiUf5Cd, sakiUf5NameRyakushiki, sakiUf6Cd, sakiUf6NameRyakushiki, sakiUf7Cd, sakiUf7NameRyakushiki, sakiUf8Cd, sakiUf8NameRyakushiki, sakiUf9Cd, sakiUf9NameRyakushiki, sakiUf10Cd, sakiUf10NameRyakushiki,
			sakiProjectCd, sakiProjectName, sakiSegmentCd, sakiSegmentName,
			userId, userId);
	}
	
	/**
	 * 明細の登録(東商カスタマイズ用)
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @param sakiZeiritsu 付替先税率
	 * @param sakiKeigenZeiritsuKbn 付替先軽減税率区分
	 * @return 処理件数
	 */
	// TODO daoへの移行
	public int insertMeisaiExt(
		String denpyouId,
		int denpyouEdano,
		BigDecimal sakiZeiritsu,
		String sakiKeigenZeiritsuKbn
	) {
		return 0;
	}

	/**
	 * 伝票配下の明細レコードを全て削除する
	 * @param denpyouId  伝票ID
	 * @return 処理件数
	 */
	@Deprecated
	public int deleteMeisai(String denpyouId) {
		String sql = "DELETE FROM tsukekae_meisai WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 伝票配下の明細レコードを全て削除する(カスタマイズ用)
	 * @param denpyouId  伝票ID
	 * @return 処理件数
	 */
	// TODO daoへの移行
	public int deleteMeisaiExt(String denpyouId) {
		return 0;
	}
}