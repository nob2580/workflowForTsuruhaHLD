package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;

/**
 * 旅費仮払精算Logic
 */
public class RyohiKaribaraiShinseiLogic extends EteamAbstractLogic {
	
	/**
	 * 旅費仮払精算テーブルに新規登録する。
	 * @param denpyouId                伝票ID
	 * @param siyousha_userId          使用者ID
	 * @param siyousha_shainNo         社員番号
	 * @param siyousha_userSei         使用者姓
	 * @param siyousha_userMei         使用者名
	 * @param karibaraiOn              仮払使用フラグ
	 * @param dairiFlg                 代理起票フラグ
	 * @param houmonsaki               訪問先
	 * @param mokuteki                 目的
	 * @param seisankikanFrom          精算期間開始日
	 * @param seisankikanFromHour      精算期間開始時刻（時）
	 * @param seisankikanFromMin       精算期間開始時刻（分）
	 * @param seisankikanTo            精算期間終了日
	 * @param seisankikanToHour        精算期間終了時刻（時）
	 * @param seisankikanToMin         精算期間終了時刻（分）
	 * @param shiharaibi               支払日
	 * @param shiharaiKiboubi          支払希望日
	 * @param shiharaihouhou           支払方法
	 * @param tekiyou                  摘要
	 * @param shinseiKingaku           申請金額
	 * @param karibaraiKingaku         仮払金額
	 * @param sashihikiNum             差引回数
	 * @param sashihikiTanka           差引単価
	 * @param hf1Cd                    ヘッダーフィールド１コード
	 * @param hf1Name                  ヘッダーフィールド１名
	 * @param hf2Cd                    ヘッダーフィールド２コード
	 * @param hf2Name                  ヘッダーフィールド２名
	 * @param hf3Cd                    ヘッダーフィールド３コード
	 * @param hf3Name                  ヘッダーフィールド３名
	 * @param hf4Cd ヘッダーフィールド４コード
	 * @param hf4Name ヘッダーフィールド４名
	 * @param hf5Cd ヘッダーフィールド５コード
	 * @param hf5Name ヘッダーフィールド５名
	 * @param hf6Cd ヘッダーフィールド６コード
	 * @param hf6Name ヘッダーフィールド６名
	 * @param hf7Cd ヘッダーフィールド７コード
	 * @param hf7Name ヘッダーフィールド７名
	 * @param hf8Cd ヘッダーフィールド８コード
	 * @param hf8Name ヘッダーフィールド８名
	 * @param hf9Cd ヘッダーフィールド９コード
	 * @param hf9Name ヘッダーフィールド９名
	 * @param hf10Cd ヘッダーフィールド１０コード
	 * @param hf10Name ヘッダーフィールド１０名
	 * @param hosoku                   補足
	 * @param shiwakeEdaNo             仕訳枝番号
	 * @param torihikiName             取引名
	 * @param kariFutanBumonCd         借方負担部門コード
	 * @param kariFutanBumonName       借方負担部門名
	 * @param torihikisakiCd             取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param kariKamokuCd             借方科目コード
	 * @param kariKamokuName           借方科目名
	 * @param kariKamokuEdabanCd       借方科目枝番コード
	 * @param kariKamokuEdabanName     借方科目枝番名
	 * @param kariKazeiKbn             借方課税区分
	 * @param kashiFutanBumonCd        貸方負担部門コード
	 * @param kashiFutanBumonName      貸方負担部門名
	 * @param kashiKamokuCd            貸方科目コード
	 * @param kashiKamokuName          貸方科目名
	 * @param kashiKamokuEdabanCd      貸方科目枝番コード
	 * @param kashiKamokuEdabanName    貸方科目枝番名
	 * @param kashiKazeiKbn            貸方課税区分
	 * @param uf1Cd                    UF1コード
	 * @param uf1NameRyakushiki        UF1名（略式）
	 * @param uf2Cd                    UF2コード
	 * @param uf2NameRyakushiki        UF2名（略式）
	 * @param uf3Cd                    UF3コード
	 * @param uf3NameRyakushiki        UF3名（略式）
	 * @param uf4Cd                    UF4コード
	 * @param uf4NameRyakushiki        UF4名（略式）
	 * @param uf5Cd                    UF5コード
	 * @param uf5NameRyakushiki        UF5名（略式）
	 * @param uf6Cd                    UF6コード
	 * @param uf6NameRyakushiki        UF6名（略式）
	 * @param uf7Cd                    UF7コード
	 * @param uf7NameRyakushiki        UF7名（略式）
	 * @param uf8Cd                    UF8コード
	 * @param uf8NameRyakushiki        UF8名（略式）
	 * @param uf9Cd                    UF9コード
	 * @param uf9NameRyakushiki        UF9名（略式）
	 * @param uf10Cd                    UF10コード
	 * @param uf10NameRyakushiki        UF10名（略式）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd                摘要コード
	 * @param seisanKanryoubi          精算完了日
	 * @param userId ユーザーID
	 * @return 件数
	 */
	public int insertShinsei(
		String denpyouId,
		String siyousha_userId,
		String siyousha_shainNo,
		String siyousha_userSei,
		String siyousha_userMei,
		String karibaraiOn,
		String dairiFlg,
		String houmonsaki,
		String mokuteki,
		Date seisankikanFrom,
		String seisankikanFromHour,
		String seisankikanFromMin,
		Date seisankikanTo,
		String seisankikanToHour,
		String seisankikanToMin,
		Date shiharaibi,
		Date shiharaiKiboubi,
		String shiharaihouhou,
		String tekiyou,
		BigDecimal shinseiKingaku,
		BigDecimal karibaraiKingaku,
		BigDecimal sashihikiNum,
		BigDecimal sashihikiTanka,
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
		Integer shiwakeEdaNo,
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
		Date seisanKanryoubi,
		String userId
	) {
		final String sql =
				"INSERT INTO ryohi_karibarai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, karibaraiOn, dairiFlg, siyousha_userId, siyousha_shainNo, siyousha_userSei, siyousha_userMei, houmonsaki, mokuteki, seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin, shiharaibi, shiharaiKiboubi, shiharaihouhou, tekiyou, shinseiKingaku, karibaraiKingaku, sashihikiNum, sashihikiTanka, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			hosoku, shiwakeEdaNo, torihikiName, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn,
			uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName,segmentCd, segmentNameRyakushiki, tekiyouCd, seisanKanryoubi,
			userId, userId);
	}
	
	/**
	 * 旅費精算テーブルを更新する。
	 * @param denpyouId                伝票ID
	 * @param siyousha_userId          使用者ID
	 * @param siyousha_shainNo         社員番号
	 * @param siyousha_userSei         使用者姓
	 * @param siyousha_userMei         使用者名
	 * @param karibaraiOn              仮払使用フラグ
	 * @param houmonsaki               訪問先
	 * @param mokuteki                 目的
	 * @param seisankikanFrom          精算期間開始日
	 * @param seisankikanFromHour      精算期間開始時刻（時）
	 * @param seisankikanFromMin       精算期間開始時刻（分）
	 * @param seisankikanTo            精算期間終了日
	 * @param seisankikanToHour        精算期間終了時刻（時）
	 * @param seisankikanToMin         精算期間終了時刻（分）
	 * @param shiharaibi               支払日
	 * @param shiharaiKiboubi          支払希望日
	 * @param shiharaihouhou           支払方法
	 * @param tekiyou                  摘要
	 * @param shinseiKingaku           申請金額
	 * @param karibaraiKingaku         仮払金額
	 * @param sashihikiNum             差引回数
	 * @param sashihikiTanka           差引単価
	 * @param hf1Cd                    ヘッダーフィールド１コード
	 * @param hf1Name                  ヘッダーフィールド１名
	 * @param hf2Cd                    ヘッダーフィールド２コード
	 * @param hf2Name                  ヘッダーフィールド２名
	 * @param hf3Cd                    ヘッダーフィールド３コード
	 * @param hf3Name                  ヘッダーフィールド３名
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
	 * @param hosoku                   補足
	 * @param shiwakeEdaNo             仕訳枝番号
	 * @param torihikiName             取引名
	 * @param kariFutanBumonCd         借方負担部門コード
	 * @param kariFutanBumonName       借方負担部門名
	 * @param torihikisakiCd             取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param kariKamokuCd             借方科目コード
	 * @param kariKamokuName           借方科目名
	 * @param kariKamokuEdabanCd       借方科目枝番コード
	 * @param kariKamokuEdabanName     借方科目枝番名
	 * @param kariKazeiKbn             借方課税区分
	 * @param kashiFutanBumonCd        貸方負担部門コード
	 * @param kashiFutanBumonName      貸方負担部門名
	 * @param kashiKamokuCd            貸方科目コード
	 * @param kashiKamokuName          貸方科目名
	 * @param kashiKamokuEdabanCd      貸方科目枝番コード
	 * @param kashiKamokuEdabanName    貸方科目枝番名
	 * @param kashiKazeiKbn            貸方課税区分
	 * @param uf1Cd                    UF1コード
	 * @param uf1NameRyakushiki        UF1名（略式）
	 * @param uf2Cd                    UF2コード
	 * @param uf2NameRyakushiki        UF2名（略式）
	 * @param uf3Cd                    UF3コード
	 * @param uf3NameRyakushiki        UF3名（略式）
	 * @param uf4Cd                    UF4コード
	 * @param uf4NameRyakushiki        UF4名（略式）
	 * @param uf5Cd                    UF5コード
	 * @param uf5NameRyakushiki        UF5名（略式）
	 * @param uf6Cd                    UF6コード
	 * @param uf6NameRyakushiki        UF6名（略式）
	 * @param uf7Cd                    UF7コード
	 * @param uf7NameRyakushiki        UF7名（略式）
	 * @param uf8Cd                    UF8コード
	 * @param uf8NameRyakushiki        UF8名（略式）
	 * @param uf9Cd                    UF9コード
	 * @param uf9NameRyakushiki        UF9名（略式）
	 * @param uf10Cd                    UF10コード
	 * @param uf10NameRyakushiki        UF10名（略式）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd                摘要コード
	 * @param seisanKanryoubi          精算完了日
	 * @param userId                   ユーザーID
	 * @return 件数
	 */
	public int updateShinsei(
			String denpyouId,
			String siyousha_userId,
			String siyousha_shainNo,
			String siyousha_userSei,
			String siyousha_userMei,
			String karibaraiOn,
			String houmonsaki,
			String mokuteki,
			Date seisankikanFrom,
			String seisankikanFromHour,
			String seisankikanFromMin,
			Date seisankikanTo,
			String seisankikanToHour,
			String seisankikanToMin,
			Date shiharaibi,
			Date shiharaiKiboubi,
			String shiharaihouhou,
			String tekiyou,
			BigDecimal shinseiKingaku,
			BigDecimal karibaraiKingaku,
			BigDecimal sashihikiNum,
			BigDecimal sashihikiTanka,
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
			Integer shiwakeEdaNo,
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
			Date seisanKanryoubi,
			String userId
	) {
		final String sql =
				"UPDATE ryohi_karibarai "
			+ "SET karibarai_on = ?, user_id = ?, shain_no = ?, user_sei = ?, user_mei = ?, houmonsaki = ?, mokuteki = ?, seisankikan_from = ?, seisankikan_from_hour = ?, seisankikan_from_min = ?, seisankikan_to = ?, seisankikan_to_hour = ?, seisankikan_to_min = ?, shiharaibi = ?, shiharaikiboubi = ?, shiharaihouhou = ?, tekiyou = ?, kingaku = ?, karibarai_kingaku = ?, sashihiki_num = ?, sashihiki_tanka = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, "
			+ "hosoku = ?, shiwake_edano=?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, "
			+ "project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, seisan_kanryoubi = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "WHERE denpyou_id = ? ";
		return connection.update(
			sql,
			karibaraiOn, siyousha_userId, siyousha_shainNo, siyousha_userSei, siyousha_userMei, houmonsaki, mokuteki, seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin, shiharaibi, shiharaiKiboubi, shiharaihouhou, tekiyou, shinseiKingaku, karibaraiKingaku, sashihikiNum, sashihikiTanka, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			hosoku, shiwakeEdaNo, torihikiName, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, 
			kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName,segmentCd, segmentNameRyakushiki, tekiyouCd, seisanKanryoubi,
			userId,
			denpyouId);
	}
	
	/** 
	 * 経費精算明細の登録
	 * @param denpyouId                伝票ID
	 * @param denpyouEdano             伝票枝番号
	 * @param shiwakeEdano             仕訳枝番号
	 * @param shiyoubi                 使用日
	 * @param shouhyouShoruiFlg        証憑書類フラグ
	 * @param torihikiName             取引名
	 * @param tekiyou                  摘要
	 * @param zeiritsu                 税率
	 * @param keigenZeiritsuKbn        軽減税率区分
	 * @param shiharaiKingaku          支払金額
	 * @param hontaiKingaku            本体金額
	 * @param shouhizeigaku            消費税額
	 * @param kousaihiShousaiHyoujiFlg 交際費詳細表示フラグ
	 * @param ninzuuRiyouFlg           人数項目表示フラグ
	 * @param kousaihiShousai          交際費詳細
	 * @param kousaihiNinzuu           交際費人数
	 * @param kousaihiHitoriKingaku    交際費一人あたり金額
	 * @param kariFutanBumonCd         借方負担部門コード
	 * @param kariFutanBumonName       借方負担部門名
	 * @param torihikisakiCd             取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param kariKamokuCd             借方科目コード
	 * @param kariKamokuName           借方科目名
	 * @param kariKamokuEdabanCd       借方科目枝番コード
	 * @param kariKamokuEdabanName     借方科目枝番名
	 * @param kariKazeiKbn             借方課税区分
	 * @param kashiFutanBumonCd        貸方負担部門コード
	 * @param kashiFutanBumonName      貸方負担部門名
	 * @param kashiKamokuCd            貸方科目コード
	 * @param kashiKamokuName          貸方科目名
	 * @param kashiKamokuEdabanCd      貸方科目枝番コード
	 * @param kashiKamokuEdabanName    貸方科目枝番名
	 * @param kashiKazeiKbn            貸方課税区分
	 * @param uf1Cd                    UF1コード
	 * @param uf1NameRyakushiki        UF1名（略式）
	 * @param uf2Cd                    UF2コード
	 * @param uf2NameRyakushiki        UF2名（略式）
	 * @param uf3Cd                    UF3コード
	 * @param uf3NameRyakushiki        UF3名（略式）
	 * @param uf4Cd                    UF4コード
	 * @param uf4NameRyakushiki        UF4名（略式）
	 * @param uf5Cd                    UF5コード
	 * @param uf5NameRyakushiki        UF5名（略式）
	 * @param uf6Cd                    UF6コード
	 * @param uf6NameRyakushiki        UF6名（略式）
	 * @param uf7Cd                    UF7コード
	 * @param uf7NameRyakushiki        UF7名（略式）
	 * @param uf8Cd                    UF8コード
	 * @param uf8NameRyakushiki        UF8名（略式）
	 * @param uf9Cd                    UF9コード
	 * @param uf9NameRyakushiki        UF9名（略式）
	 * @param uf10Cd                    UF10コード
	 * @param uf10NameRyakushiki        UF10名（略式）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd                摘要コード
	 * @param userId                   ユーザーID
	 * @return 件数
	 */
	public int insertKeihiMeisai(
		String denpyouId,
		int denpyouEdano,
		int shiwakeEdano,
		Date shiyoubi,
		String shouhyouShoruiFlg,
		String torihikiName,
		String tekiyou,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		BigDecimal shiharaiKingaku,
		BigDecimal hontaiKingaku,
		BigDecimal shouhizeigaku,
		String kousaihiShousaiHyoujiFlg,
		String ninzuuRiyouFlg,
		String kousaihiShousai,
		Integer kousaihiNinzuu,
		BigDecimal kousaihiHitoriKingaku,
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
				"INSERT INTO ryohi_karibarai_keihi_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouEdano, shiwakeEdano, shiyoubi, shouhyouShoruiFlg, torihikiName, tekiyou, zeiritsu, keigenZeiritsuKbn, shiharaiKingaku, hontaiKingaku, shouhizeigaku, kousaihiShousaiHyoujiFlg, ninzuuRiyouFlg, kousaihiShousai, kousaihiNinzuu, kousaihiHitoriKingaku, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki,uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd, userId, userId);
	}
	
	/**
	 * 伝票配下の経費精算明細レコードを全て削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	public int deleteKeihiMeisai(String denpyouId) {
		final String sql =
				"DELETE FROM ryohi_karibarai_keihi_meisai "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
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
				"UPDATE ryohi_karibarai "
			+   "SET shiharaibi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, shiharaibi, userId, denpyouId);
	}
	/**
	 * カスタマイズ用
	 * @param denpyouId         伝票ID
	 * @param sasihikiNum       差引回数
	 * @param sasihihikiKingaku 差引金額
	 * @return 処理件数
	 */
	protected int updateSashihikiExt(
			String denpyouId,
			BigDecimal sasihikiNum,
			BigDecimal sasihihikiKingaku){
		return Integer.parseInt("0");
	}
}
