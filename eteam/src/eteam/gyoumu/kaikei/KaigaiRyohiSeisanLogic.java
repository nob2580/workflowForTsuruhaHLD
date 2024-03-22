package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 海外旅費精算Logic
 */
public class KaigaiRyohiSeisanLogic extends EteamAbstractLogic {
	
	/**
	 * 海外旅費精算テーブルに新規登録する。
	 * @param denpyouId                伝票ID
	 * @param karibaraiDenpyouId       仮払伝票ID
	 * @param karibaraiOn              仮払ありなし
	 * @param karibaraiMishiyouFlg     仮払未使用フラグ
	 * @param shucchouChuushiFlg       出張中止フラグ
	 * @param dairiFlg                 代理起票フラグ
	 * @param siyousha_userId          使用者ID
	 * @param siyousha_shainNo         社員番号
	 * @param siyousha_userSei         使用者姓
	 * @param siyousha_userMei         使用者名
	 * @param houmonsaki               訪問先
	 * @param mokuteki                 目的
	 * @param seisankikanFrom          精算期間開始日
	 * @param seisankikanFromHour      精算期間開始時刻（時）
	 * @param seisankikanFromMin       精算期間開始時刻（分）
	 * @param seisankikanTo            精算期間終了日
	 * @param seisankikanToHour        精算期間終了時刻（時）
	 * @param seisankikanToMin         精算期間終了時刻（分）
	 * @param keijoubi                 計上日
	 * @param shiharaibi               支払日
	 * @param shiharaiKiboubi          支払希望日
	 * @param shiharaihouhou           支払方法
	 * @param tekiyou                  摘要
	 * @param kaigaiTekiyou            海外摘要
	 * @param zeiritsu                 税率
	 * @param keigenZeiritsuKbn        軽減税率区分
	 * @param goukeiKingaku            合計金額
	 * @param houjinCardRiyouKingaku   内法人カード利用合計
	 * @param kaishaTehaiKingaku       会社手配合計
	 * @param sashihikiShikyuuKingaku  差引支給金額
	 * @param sashihikiNum             差引回数
	 * @param sashihikiTanka           差引単価
	 * @param sashihikiNumKaigai       差引回数（海外）
	 * @param sashihikiTankaKaigai     差引単価（海外）
	 * @param sashihikiHeishuCdKaigai   差引幣種コード（海外）
	 * @param sashihikiRateKaigai       差引レート（海外）
	 * @param sashihikiTankaKaigaiGaika 差引単価（海外）外貨
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
	 * @param kazeiFlgRyohi            課税フラグ
	 * @param kashiFutanBumonCd        貸方負担部門コード
	 * @param kashiFutanBumonName      貸方負担部門名
	 * @param kashiKamokuCd            貸方科目コード
	 * @param kashiKamokuName          貸方科目名
	 * @param kashiKamokuEdabanCd      貸方科目枝番コード
	 * @param kashiKamokuEdabanName    貸方科目枝番名
	 * @param kashiKazeiKbn            貸方課税区分
	 * @param kaigaiShiwakeEdaNo       仕訳枝番号(海外)
	 * @param kaigaiTorihikiName       取引名(海外)
	 * @param kaigaiKariFutanBumonCd   借方負担部門コード(海外)
	 * @param kaigaiKariFutanBumonName 借方負担部門名(海外)
	 * @param kaigaiTorihikisakiCd   借方取引先コード(海外)
	 * @param kaigaiTorihikisakiNameRyakushiki 借方取引先名(海外)
	 * @param kaigaiKariKamokuCd       借方科目コード(海外)
	 * @param kaigaiKariKamokuName     借方科目名(海外)
	 * @param kaigaiKariKamokuEdabanCd 借方科目枝番コード(海外)
	 * @param kaigaiKariKamokuEdabanName 借方科目枝番名(海外)
	 * @param kaigaiKariKazeiKbn       借方課税区分(海外)
	 * @param kazeiFlgRyohiKaigai      課税フラグ(海外)
	 * @param kaigaiKashiFutanBumonCd  貸方負担部門コード(海外)
	 * @param kaigaiKashiFutanBumonName 貸方負担部門名(海外)
	 * @param kaigaiKashiKamokuCd      貸方科目コード(海外)
	 * @param kaigaiKashiKamokuName    貸方科目名(海外)
	 * @param kaigaiKashiKamokuEdabanCd 貸方科目枝番コード(海外)
	 * @param kaigaiKashiKamokuEdabanName 貸方科目枝番名(海外)
	 * @param kaigaiKashiKazeiKbn      貸方課税区分(海外)
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
	 * @param kaigaiUf1CdRyohi         UF1コード(海外)
	 * @param kaigaiUf1NameRyohi       UF1名（略式・海外）
	 * @param kaigaiUf2CdRyohi         UF2コード(海外)
	 * @param kaigaiUf2NameRyohi       UF2名（略式・海外）
	 * @param kaigaiUf3CdRyohi         UF3コード(海外)
	 * @param kaigaiUf3NameRyohi       UF3名（略式・海外）
	 * @param kaigaiUf4CdRyohi         UF4コード(海外)
	 * @param kaigaiUf4NameRyohi       UF4名（略式・海外）
	 * @param kaigaiUf5CdRyohi         UF5コード(海外)
	 * @param kaigaiUf5NameRyohi       UF5名（略式・海外）
	 * @param kaigaiUf6CdRyohi         UF6コード(海外)
	 * @param kaigaiUf6NameRyohi       UF6名（略式・海外）
	 * @param kaigaiUf7CdRyohi         UF7コード(海外)
	 * @param kaigaiUf7NameRyohi       UF7名（略式・海外）
	 * @param kaigaiUf8CdRyohi         UF8コード(海外)
	 * @param kaigaiUf8NameRyohi       UF8名（略式・海外）
	 * @param kaigaiUf9CdRyohi         UF9コード(海外)
	 * @param kaigaiUf9NameRyohi       UF9名（略式・海外）
	 * @param kaigaiUf10CdRyohi         UF10コード(海外)
	 * @param kaigaiUf10NameRyohi       UF10名（略式・海外）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd                摘要コード
	 * @param kaigaiProjectCd          プロジェクトコード(海外)
	 * @param kaigaiProjectName        プロジェクト名(海外)
	 * @param kaigaiSegmentCd          セグメントコード(海外)
	 * @param kaigaiSegmentName        セグメント名(海外)
	 * @param kaigaiTekiyouCd          摘要コード(海外)
	 * @param userId ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertShinsei(
		String denpyouId,
		String karibaraiDenpyouId,
		String karibaraiOn,
		String karibaraiMishiyouFlg,
		String shucchouChuushiFlg,
		String dairiFlg,
		String siyousha_userId,
		String siyousha_shainNo,
		String siyousha_userSei,
		String siyousha_userMei,
		String houmonsaki,
		String mokuteki,
		Date seisankikanFrom,
		String seisankikanFromHour,
		String seisankikanFromMin,
		Date seisankikanTo,
		String seisankikanToHour,
		String seisankikanToMin,
		Date keijoubi,
		Date shiharaibi,
		Date shiharaiKiboubi,
		String shiharaihouhou,
		String tekiyou,
		String kaigaiTekiyou,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		BigDecimal goukeiKingaku,
		BigDecimal houjinCardRiyouKingaku,
		BigDecimal kaishaTehaiKingaku,
		BigDecimal sashihikiShikyuuKingaku,
		BigDecimal sashihikiNum,
		BigDecimal sashihikiTanka,
		BigDecimal sashihikiNumKaigai,
		BigDecimal sashihikiTankaKaigai,
		String sashihikiHeishuCdKaigai,
		BigDecimal sashihikiRateKaigai,
		BigDecimal sashihikiTankaKaigaiGaika,
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
		String kazeiFlgRyohi,
		String kashiFutanBumonCd,
		String kashiFutanBumonName,
		String kashiKamokuCd,
		String kashiKamokuName,
		String kashiKamokuEdabanCd,
		String kashiKamokuEdabanName,
		String kashiKazeiKbn,
		Integer kaigaiShiwakeEdaNo,
		String kaigaiTorihikiName,
		String kaigaiKariFutanBumonCd,
		String kaigaiKariFutanBumonName,
		String kaigaiTorihikisakiCd,
		String kaigaiTorihikisakiNameRyakushiki,
		String kaigaiKariKamokuCd,
		String kaigaiKariKamokuName,
		String kaigaiKariKamokuEdabanCd,
		String kaigaiKariKamokuEdabanName,
		String kaigaiKariKazeiKbn,
		String kazeiFlgRyohiKaigai,
		String kaigaiKashiFutanBumonCd,
		String kaigaiKashiFutanBumonName,
		String kaigaiKashiKamokuCd,
		String kaigaiKashiKamokuName,
		String kaigaiKashiKamokuEdabanCd,
		String kaigaiKashiKamokuEdabanName,
		String kaigaiKashiKazeiKbn,
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
		String kaigaiUf1CdRyohi,
		String kaigaiUf1NameRyohi,
		String kaigaiUf2CdRyohi,
		String kaigaiUf2NameRyohi,
		String kaigaiUf3CdRyohi,
		String kaigaiUf3NameRyohi,
		String  kaigaiUf4CdRyohi,
		String  kaigaiUf4NameRyohi,
		String  kaigaiUf5CdRyohi,
		String  kaigaiUf5NameRyohi,
		String  kaigaiUf6CdRyohi,
		String  kaigaiUf6NameRyohi,
		String  kaigaiUf7CdRyohi,
		String  kaigaiUf7NameRyohi,
		String  kaigaiUf8CdRyohi,
		String  kaigaiUf8NameRyohi,
		String  kaigaiUf9CdRyohi,
		String  kaigaiUf9NameRyohi,
		String  kaigaiUf10CdRyohi,
		String  kaigaiUf10NameRyohi,
		String projectCd,
		String projectName,
		String segmentCd,
		String segmentNameRyakushiki,
		String tekiyouCd,
		String kaigaiProjectCd,
		String kaigaiProjectName,
		String kaigaiSegmentCd,
		String kaigaiSegmentName,
		String kaigaiTekiyouCd,
		String userId
	) {
		final String sql =
				"INSERT INTO kaigai_ryohiseisan "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, karibaraiDenpyouId, karibaraiOn, karibaraiMishiyouFlg, shucchouChuushiFlg, dairiFlg, siyousha_userId, siyousha_shainNo, siyousha_userSei, siyousha_userMei, houmonsaki, mokuteki, seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin, keijoubi, shiharaibi, shiharaiKiboubi, shiharaihouhou, tekiyou, kaigaiTekiyou, zeiritsu, keigenZeiritsuKbn, goukeiKingaku, houjinCardRiyouKingaku, kaishaTehaiKingaku, sashihikiShikyuuKingaku, 
			sashihikiNum, sashihikiTanka, sashihikiNumKaigai, sashihikiTankaKaigai, sashihikiHeishuCdKaigai, sashihikiRateKaigai, sashihikiTankaKaigaiGaika, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			hosoku,shiwakeEdaNo, torihikiName, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kazeiFlgRyohi, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, 
			uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd, 
			kaigaiShiwakeEdaNo, kaigaiTorihikiName, kaigaiKariFutanBumonCd, kaigaiKariFutanBumonName,kaigaiTorihikisakiCd, kaigaiTorihikisakiNameRyakushiki,  kaigaiKariKamokuCd, kaigaiKariKamokuName, kaigaiKariKamokuEdabanCd, kaigaiKariKamokuEdabanName, kaigaiKariKazeiKbn, kazeiFlgRyohiKaigai, kaigaiKashiFutanBumonCd, kaigaiKashiFutanBumonName, kaigaiKashiKamokuCd, kaigaiKashiKamokuName, kaigaiKashiKamokuEdabanCd, kaigaiKashiKamokuEdabanName, kaigaiKashiKazeiKbn, 
			kaigaiUf1CdRyohi, kaigaiUf1NameRyohi, kaigaiUf2CdRyohi, kaigaiUf2NameRyohi, kaigaiUf3CdRyohi, kaigaiUf3NameRyohi, kaigaiUf4CdRyohi, kaigaiUf4NameRyohi, kaigaiUf5CdRyohi, kaigaiUf5NameRyohi, kaigaiUf6CdRyohi, kaigaiUf6NameRyohi, kaigaiUf7CdRyohi, kaigaiUf7NameRyohi, kaigaiUf8CdRyohi, kaigaiUf8NameRyohi, kaigaiUf9CdRyohi, kaigaiUf9NameRyohi, kaigaiUf10CdRyohi, kaigaiUf10NameRyohi, 
			kaigaiProjectCd, kaigaiProjectName, kaigaiSegmentCd, kaigaiSegmentName, kaigaiTekiyouCd,
			userId, userId);
	}
	
	/**
	 * 海外旅費精算テーブルを更新する。
	 * @param denpyouId                伝票ID
	 * @param karibaraiDenpyouId       仮払伝票ID
	 * @param karibaraiOn              仮払ありなし
	 * @param karibaraiMishiyouFlg     仮払未使用フラグ
	 * @param shucchouChuushiFlg       出張中止フラグ
	 * @param siyousha_userId          使用者ID
	 * @param siyousha_shainNo         社員番号
	 * @param siyousha_userSei         使用者姓
	 * @param siyousha_userMei         使用者名
	 * @param houmonsaki               訪問先
	 * @param mokuteki                 目的
	 * @param seisankikanFrom          精算期間開始日
	 * @param seisankikanFromHour      精算期間開始時刻（時）
	 * @param seisankikanFromMin       精算期間開始時刻（分）
	 * @param seisankikanTo            精算期間終了日
	 * @param seisankikanToHour        精算期間終了時刻（時）
	 * @param seisankikanToMin         精算期間終了時刻（分）
	 * @param keijoubi                 計上日
	 * @param shiharaibi               支払日
	 * @param shiharaiKiboubi          支払希望日
	 * @param shiharaihouhou           支払方法
	 * @param tekiyou                  摘要
	 * @param kaigaiTekiyou            海外摘要
	 * @param zeiritsu                 税率
	 * @param keigenZeiritsuKbn         軽減税率区分
	 * @param goukeiKingaku            合計金額
	 * @param houjinCardRiyouKingaku   内法人カード利用合計
	 * @param kaishaTehaiKingaku       会社手配合計
	 * @param sashihikiShikyuuKingaku  差引支給金額
	 * @param sashihikiNum             差引回数
	 * @param sashihikiTanka           差引単価
	 * @param sashihikiNumKaigai       差引回数（海外）
	 * @param sashihikiTankaKaigai     差引単価（海外）
	 * @param sashihikiHeishuCdKaigai   差引幣種コード（海外）
	 * @param sashihikiRateKaigai       差引レート（海外）
	 * @param sashihikiTankaKaigaiGaika 差引単価（海外）外貨
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
	 * @param kazeiFlgRyohi            課税フラグ
	 * @param kashiFutanBumonCd        貸方負担部門コード
	 * @param kashiFutanBumonName      貸方負担部門名
	 * @param kashiKamokuCd            貸方科目コード
	 * @param kashiKamokuName          貸方科目名
	 * @param kashiKamokuEdabanCd      貸方科目枝番コード
	 * @param kashiKamokuEdabanName    貸方科目枝番名
	 * @param kashiKazeiKbn            貸方課税区分
	 * @param kaigaiShiwakeEdaNo       仕訳枝番号(海外)
	 * @param kaigaiTorihikiName       取引名(海外)
	 * @param kaigaiKariFutanBumonCd   借方負担部門コード(海外)
	 * @param kaigaiKariFutanBumonName 借方負担部門名(海外)
	 * @param kaigaiTorihikisakiCd   借方取引先コード(海外)
	 * @param kaigaiTorihikisakiNameRyakushiki 借方取引先名(海外)
	 * @param kaigaiKariKamokuCd       借方科目コード(海外)
	 * @param kaigaiKariKamokuName     借方科目名(海外)
	 * @param kaigaiKariKamokuEdabanCd 借方科目枝番コード(海外)
	 * @param kaigaiKariKamokuEdabanName 借方科目枝番名(海外)
	 * @param kaigaiKariKazeiKbn       借方課税区分(海外)
	 * @param kazeiFlgRyohiKaigai      課税フラグ(海外)
	 * @param kaigaiKashiFutanBumonCd  貸方負担部門コード(海外)
	 * @param kaigaiKashiFutanBumonName 貸方負担部門名(海外)
	 * @param kaigaiKashiKamokuCd      貸方科目コード(海外)
	 * @param kaigaiKashiKamokuName    貸方科目名(海外)
	 * @param kaigaiKashiKamokuEdabanCd 貸方科目枝番コード(海外)
	 * @param kaigaiKashiKamokuEdabanName 貸方科目枝番名(海外)
	 * @param kaigaiKashiKazeiKbn      貸方課税区分(海外)
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
	 * @param kaigaiUf1CdRyohi         UF1コード(海外)
	 * @param kaigaiUf1NameRyohi       UF1名（略式・海外）
	 * @param kaigaiUf2CdRyohi         UF2コード(海外)
	 * @param kaigaiUf2NameRyohi       UF2名（略式・海外）
	 * @param kaigaiUf3CdRyohi         UF3コード(海外)
	 * @param kaigaiUf3NameRyohi       UF3名（略式・海外）
	 * @param kaigaiUf4CdRyohi         UF4コード(海外)
	 * @param kaigaiUf4NameRyohi       UF4名（略式・海外）
	 * @param kaigaiUf5CdRyohi         UF5コード(海外)
	 * @param kaigaiUf5NameRyohi       UF5名（略式・海外）
	 * @param kaigaiUf6CdRyohi         UF6コード(海外)
	 * @param kaigaiUf6NameRyohi       UF6名（略式・海外）
	 * @param kaigaiUf7CdRyohi         UF7コード(海外)
	 * @param kaigaiUf7NameRyohi       UF7名（略式・海外）
	 * @param kaigaiUf8CdRyohi         UF8コード(海外)
	 * @param kaigaiUf8NameRyohi       UF8名（略式・海外）
	 * @param kaigaiUf9CdRyohi         UF9コード(海外)
	 * @param kaigaiUf9NameRyohi       UF9名（略式・海外）
	 * @param kaigaiUf10CdRyohi         UF10コード(海外)
	 * @param kaigaiUf10NameRyohi       UF10名（略式・海外）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param tekiyouCd                摘要コード
	 * @param kaigaiProjectCd          プロジェクトコード(海外)
	 * @param kaigaiProjectName        プロジェクト名(海外)
	 * @param kaigaiSegmentCd          セグメントコード(海外)
	 * @param kaigaiSegmentName        セグメント名(海外)
	 * @param kaigaiTekiyouCd          摘要コード(海外)
	 * @param userId                   ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int updateShinsei(
			String denpyouId,
			String karibaraiDenpyouId,
			String karibaraiOn,
			String karibaraiMishiyouFlg,
			String shucchouChuushiFlg,
			String siyousha_userId,
			String siyousha_shainNo,
			String siyousha_userSei,
			String siyousha_userMei,
			String houmonsaki,
			String mokuteki,
			Date seisankikanFrom,
			String seisankikanFromHour,
			String seisankikanFromMin,
			Date seisankikanTo,
			String seisankikanToHour,
			String seisankikanToMin,
			Date keijoubi,
			Date shiharaibi,
			Date shiharaiKiboubi,
			String shiharaihouhou,
			String tekiyou,
			String kaigaiTekiyou,
			BigDecimal zeiritsu,
			String keigenZeiritsuKbn,
			BigDecimal goukeiKingaku,
			BigDecimal houjinCardRiyouKingaku,
			BigDecimal kaishaTehaiKingaku,
			BigDecimal sashihikiShikyuuKingaku,
			BigDecimal sashihikiNum,
			BigDecimal sashihikiTanka,
			BigDecimal sashihikiNumKaigai,
			BigDecimal sashihikiTankaKaigai,
			String sashihikiHeishuCdKaigai,
			BigDecimal sashihikiRateKaigai,
			BigDecimal sashihikiTankaKaigaiGaika,
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
			String kazeiFlgRyohi,
			String kashiFutanBumonCd,
			String kashiFutanBumonName,
			String kashiKamokuCd,
			String kashiKamokuName,
			String kashiKamokuEdabanCd,
			String kashiKamokuEdabanName,
			String kashiKazeiKbn,
			Integer kaigaiShiwakeEdaNo,
			String kaigaiTorihikiName,
			String kaigaiKariFutanBumonCd,
			String kaigaiKariFutanBumonName,
			String kaigaiTorihikisakiCd,
			String kaigaiTorihikisakiNameRyakushiki,
			String kaigaiKariKamokuCd,
			String kaigaiKariKamokuName,
			String kaigaiKariKamokuEdabanCd,
			String kaigaiKariKamokuEdabanName,
			String kaigaiKariKazeiKbn,
			String kazeiFlgRyohiKaigai,
			String kaigaiKashiFutanBumonCd,
			String kaigaiKashiFutanBumonName,
			String kaigaiKashiKamokuCd,
			String kaigaiKashiKamokuName,
			String kaigaiKashiKamokuEdabanCd,
			String kaigaiKashiKamokuEdabanName,
			String kaigaiKashiKazeiKbn,
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
			String kaigaiUf1CdRyohi,
			String kaigaiUf1NameRyohi,
			String kaigaiUf2CdRyohi,
			String kaigaiUf2NameRyohi,
			String kaigaiUf3CdRyohi,
			String kaigaiUf3NameRyohi,
			String  kaigaiUf4CdRyohi,
			String  kaigaiUf4NameRyohi,
			String  kaigaiUf5CdRyohi,
			String  kaigaiUf5NameRyohi,
			String  kaigaiUf6CdRyohi,
			String  kaigaiUf6NameRyohi,
			String  kaigaiUf7CdRyohi,
			String  kaigaiUf7NameRyohi,
			String  kaigaiUf8CdRyohi,
			String  kaigaiUf8NameRyohi,
			String  kaigaiUf9CdRyohi,
			String  kaigaiUf9NameRyohi,
			String  kaigaiUf10CdRyohi,
			String  kaigaiUf10NameRyohi,
			String projectCd,
			String projectName,
			String segmentCd,
			String segmentNameRyakushiki,
			String tekiyouCd,
			String kaigaiProjectCd,
			String kaigaiProjectName,
			String kaigaiSegmentCd,
			String kaigaiSegmentName,
			String kaigaiTekiyouCd,
			String userId
	) {
		final String sql =
				"UPDATE kaigai_ryohiseisan "
			+ "SET karibarai_denpyou_id = ?, karibarai_on = ?, karibarai_mishiyou_flg = ?, shucchou_chuushi_flg = ?, user_id = ?, shain_no = ?, user_sei = ?, user_mei = ?, houmonsaki = ?, mokuteki = ?, seisankikan_from = ?, seisankikan_from_hour = ?, seisankikan_from_min = ?, seisankikan_to = ?, seisankikan_to_hour = ?, seisankikan_to_min = ?, keijoubi = ?, shiharaibi = ?, shiharaikiboubi = ?, shiharaihouhou = ?, tekiyou = ?, kaigai_tekiyou = ?, zeiritsu = ?, keigen_zeiritsu_kbn = ?, goukei_kingaku = ?, houjin_card_riyou_kingaku = ?, kaisha_tehai_kingaku = ?, sashihiki_shikyuu_kingaku = ?, "
			+ "sashihiki_num = ?, sashihiki_tanka = ?, sashihiki_num_kaigai = ?, sashihiki_tanka_kaigai = ?, sashihiki_heishu_cd_kaigai = ?, sashihiki_rate_kaigai = ?, sashihiki_tanka_kaigai_gaika = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, "
			+ "shiwake_edano=?, torihiki_name = ?, kari_futan_bumon_cd = ?, kari_futan_bumon_name = ?, torihikisaki_cd = ?, torihikisaki_name_ryakushiki = ?, kari_kamoku_cd = ?, kari_kamoku_name = ?, kari_kamoku_edaban_cd = ?, kari_kamoku_edaban_name = ?, kari_kazei_kbn = ?, ryohi_kazei_flg = ?, kashi_futan_bumon_cd = ?, kashi_futan_bumon_name = ?, kashi_kamoku_cd = ?, kashi_kamoku_name = ?, kashi_kamoku_edaban_cd = ?, kashi_kamoku_edaban_name = ?, kashi_kazei_kbn = ?, "
			+ "uf1_cd = ?, uf1_name_ryakushiki = ?, uf2_cd = ?, uf2_name_ryakushiki = ?, uf3_cd = ?, uf3_name_ryakushiki = ?, uf4_cd = ?, uf4_name_ryakushiki = ?, uf5_cd = ?, uf5_name_ryakushiki = ?, uf6_cd = ?, uf6_name_ryakushiki = ?, uf7_cd = ?, uf7_name_ryakushiki = ?, uf8_cd = ?, uf8_name_ryakushiki = ?, uf9_cd = ?, uf9_name_ryakushiki = ?, uf10_cd = ?, uf10_name_ryakushiki = ?, project_cd = ?, project_name = ?, segment_cd = ?, segment_name_ryakushiki = ?, tekiyou_cd = ?, "
			+ "kaigai_shiwake_edano=?, kaigai_torihiki_name = ?, kaigai_kari_futan_bumon_cd = ?, kaigai_kari_futan_bumon_name = ?, kaigai_torihikisaki_cd = ?, kaigai_torihikisaki_name_ryakushiki = ?, kaigai_kari_kamoku_cd = ?, kaigai_kari_kamoku_name = ?, kaigai_kari_kamoku_edaban_cd = ?, kaigai_kari_kamoku_edaban_name = ?, kaigai_kari_kazei_kbn = ?, kaigai_kazei_flg = ?, kaigai_kashi_futan_bumon_cd = ?, kaigai_kashi_futan_bumon_name = ?, kaigai_kashi_kamoku_cd = ?, kaigai_kashi_kamoku_name = ?, kaigai_kashi_kamoku_edaban_cd = ?, kaigai_kashi_kamoku_edaban_name = ?, kaigai_kashi_kazei_kbn = ?, "
			+ "kaigai_uf1_cd = ?, kaigai_uf1_name_ryakushiki = ?, kaigai_uf2_cd = ?, kaigai_uf2_name_ryakushiki = ?, kaigai_uf3_cd = ?, kaigai_uf3_name_ryakushiki = ?, kaigai_uf4_cd = ?, kaigai_uf4_name_ryakushiki = ?, kaigai_uf5_cd = ?, kaigai_uf5_name_ryakushiki = ?, kaigai_uf6_cd = ?, kaigai_uf6_name_ryakushiki = ?, kaigai_uf7_cd = ?, kaigai_uf7_name_ryakushiki = ?, kaigai_uf8_cd = ?, kaigai_uf8_name_ryakushiki = ?, kaigai_uf9_cd = ?, kaigai_uf9_name_ryakushiki = ?, kaigai_uf10_cd = ?, kaigai_uf10_name_ryakushiki = ?, "
			+ "kaigai_project_cd = ?, kaigai_project_name = ?, kaigai_segment_cd = ?, kaigai_segment_name_ryakushiki = ?, kaigai_tekiyou_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp "
			+ "WHERE denpyou_id = ? ";
		return connection.update(
			sql,
			karibaraiDenpyouId, karibaraiOn, karibaraiMishiyouFlg, shucchouChuushiFlg, siyousha_userId, siyousha_shainNo, siyousha_userSei, siyousha_userMei, houmonsaki, mokuteki, seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin, keijoubi, shiharaibi, shiharaiKiboubi, shiharaihouhou, tekiyou, kaigaiTekiyou, zeiritsu, keigenZeiritsuKbn, goukeiKingaku, houjinCardRiyouKingaku, kaishaTehaiKingaku, sashihikiShikyuuKingaku, 
			sashihikiNum, sashihikiTanka, sashihikiNumKaigai, sashihikiTankaKaigai, sashihikiHeishuCdKaigai, sashihikiRateKaigai, sashihikiTankaKaigaiGaika, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,hosoku, 
			shiwakeEdaNo, torihikiName, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kazeiFlgRyohi, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, 
			uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd, 
			kaigaiShiwakeEdaNo, kaigaiTorihikiName, kaigaiKariFutanBumonCd, kaigaiKariFutanBumonName, kaigaiTorihikisakiCd, kaigaiTorihikisakiNameRyakushiki, kaigaiKariKamokuCd, kaigaiKariKamokuName, kaigaiKariKamokuEdabanCd, kaigaiKariKamokuEdabanName, kaigaiKariKazeiKbn, kazeiFlgRyohiKaigai, kaigaiKashiFutanBumonCd, kaigaiKashiFutanBumonName, kaigaiKashiKamokuCd, kaigaiKashiKamokuName, kaigaiKashiKamokuEdabanCd, kaigaiKashiKamokuEdabanName, kaigaiKashiKazeiKbn, 
			kaigaiUf1CdRyohi, kaigaiUf1NameRyohi, kaigaiUf2CdRyohi, kaigaiUf2NameRyohi, kaigaiUf3CdRyohi, kaigaiUf3NameRyohi, kaigaiUf4CdRyohi, kaigaiUf4NameRyohi, kaigaiUf5CdRyohi, kaigaiUf5NameRyohi, kaigaiUf6CdRyohi, kaigaiUf6NameRyohi, kaigaiUf7CdRyohi, kaigaiUf7NameRyohi, kaigaiUf8CdRyohi, kaigaiUf8NameRyohi, kaigaiUf9CdRyohi, kaigaiUf9NameRyohi, kaigaiUf10CdRyohi, kaigaiUf10NameRyohi, 
			kaigaiProjectCd, kaigaiProjectName, kaigaiSegmentCd, kaigaiSegmentName, kaigaiTekiyouCd,
			userId,
			denpyouId);
	}
	
	/** 
	 * 経費精算明細の登録
	 * @param denpyouId                伝票ID
	 * @param denpyouEdano             伝票枝番号
	 * @param kaigaiFlg                海外フラグ
	 * @param shiwakeEdano             仕訳枝番号
	 * @param shiyoubi                 使用日
	 * @param shouhyouShoruiFlg        証憑書類フラグ
	 * @param torihikiName             取引名
	 * @param tekiyou                  摘要
	 * @param zeiritsu                 税率
	 * @param keigenZeiritsuKbn        軽減税率区分
	 * @param kazeiFlg                 課税フラグ
	 * @param shiharaiKingaku          支払金額
	 * @param hontaiKingaku            本体金額
	 * @param shouhizeigaku            消費税額
	 * @param houjinCardRiyouFlg       法人カード利用フラグ
	 * @param kaishaTehaiFlg           会社手配フラグ
	 * @param kousaihiShousaiHyoujiFlg 交際費詳細表示フラグ
	 * @param ninzuuRiyouFlg           人数項目表示フラグ
	 * @param kousaihiShousai          交際費詳細
	 * @param kousaihiNinzuu           交際費人数
	 * @param kousaihiHitoriKingaku    交際費一人あたり金額
	 * @param heishuCd                 幣種コード
	 * @param rate                     レート
	 * @param gaika                    外貨
	 * @param tani                     通貨単位
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
	 * @param tekiyouCd                摘要コード
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki              セグメント名（略式）
	 * @param himodukeCardMeisai    紐付元カード明細
	 * @param userId                   ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertKeihiMeisai(
		String denpyouId,
		int denpyouEdano,
		String kaigaiFlg,
		int shiwakeEdano,
		Date shiyoubi,
		String shouhyouShoruiFlg,
		String torihikiName,
		String tekiyou,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		String kazeiFlg,
		BigDecimal shiharaiKingaku,
		BigDecimal hontaiKingaku,
		BigDecimal shouhizeigaku,
		String houjinCardRiyouFlg,
		String kaishaTehaiFlg,
		String kousaihiShousaiHyoujiFlg,
		String ninzuuRiyouFlg,
		String kousaihiShousai,
		Integer kousaihiNinzuu,
		BigDecimal kousaihiHitoriKingaku,
		String heishuCd,
		BigDecimal rate,
		BigDecimal gaika,
		String tani,
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
		Integer himodukeCardMeisai,
		String userId
	) {
		final String sql =
				"INSERT INTO kaigai_ryohiseisan_keihi_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouEdano, kaigaiFlg, shiwakeEdano, shiyoubi, shouhyouShoruiFlg, torihikiName, tekiyou, zeiritsu, keigenZeiritsuKbn, kazeiFlg, shiharaiKingaku, hontaiKingaku, shouhizeigaku, houjinCardRiyouFlg, kaishaTehaiFlg, kousaihiShousaiHyoujiFlg, ninzuuRiyouFlg, kousaihiShousai, kousaihiNinzuu, kousaihiHitoriKingaku, heishuCd, rate, gaika, tani, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki,uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd, himodukeCardMeisai, userId, userId);
	}
	
	/**
	 * 伝票配下の経費精算明細レコードを全て削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	@Deprecated
	public int deleteKeihiMeisai(String denpyouId) {
		final String sql =
				"DELETE FROM kaigai_ryohiseisan_keihi_meisai "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 旅費精算の計上日を更新する。
	 * @param denpyouId  伝票ID
	 * @param keijoubi   計上日
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	
	/**
	 * 指定した海外旅費仮払伝票の精算完了日を更新
	 * @param karibaraiDenpyouId 伝票ID
	 * @param userId ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
	public int updateKaribaraiSeisanbi(String karibaraiDenpyouId, String userId) {
		final String sql =
				"UPDATE kaigai_ryohi_karibarai "
			+   "SET seisan_kanryoubi = current_date, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, userId, karibaraiDenpyouId);
		
	}
}
