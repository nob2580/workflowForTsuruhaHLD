package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;


@Deprecated
/**
 * 振替伝票Logic (InsertとUpdateしかしていないので、今後はDaoを使って：とりあえずカスタマイズ対策として一旦残す）
 */
public class FurikaeDenpyouLogic extends EteamAbstractLogic {
	
	/**登録	
	 * @param denpyouId 伝票ID
	 * @param denpyouDate 伝票日
	 * @param shouhyouShoruiFlg 証憑書類フラグ
	 * @param kingaku 金額
	 * @param hontaiKingaku 本体金額
	 * @param shouhizeigaku 消費税額
	 * @param zeiritsu 消費税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param tekiyou 摘要
	 * @param hf1Cd ヘッダーフィールド１コード
	 * @param hf1Name ヘッダーフィールド１名
	 * @param hf2Cd ヘッダーフィールド２コード
	 * @param hf2Name ヘッダーフィールド２名
	 * @param hf3Cd ヘッダーフィールド３コード
	 * @param hf3Name ヘッダーフィールド３名
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
	 * @param bikou 備考
	 
	 * @param kariFutanBumonCd 借方負担部門コード
	 * @param kariFutanBumonName 借方負担部門名
	 * @param kariKamokuCd 借方勘定科目コード
	 * @param kariKamokuName 借方勘定科目名
	 * @param kariKamokuEdabanCd 借方勘定科目枝番コード
	 * @param kariKamokuEdabanName 借方勘定科目枝番名
	 * @param kariKazeiKbn 借方課税区分
	 * @param kariTorihikisakiCd 借方取引先コード
	 * @param kariTorihikisakiName 借方取引先名
	 * 
	 * @param kashiFutanBumonCd 貸方負担部門コード
	 * @param kashiFutanBumonName 貸方負担部門名
	 * @param kashiKamokuCd 貸方勘定科目コード
	 * @param kashiKamokuName 貸方勘定科目名
	 * @param kashiKamokuEdabanCd 貸方勘定科目枝番コード
	 * @param kashiKamokuEdabanName 貸方勘定科目枝番名
	 * @param kashiKazeiKbn 貸方課税区分
	 * @param kashiTorihikisakiCd 貸方取引先コード
	 * @param kashiTorihikisakiName 貸方取引先名
	 * 
	 * @param kariUf1Cd 借方ユニバーサルフィールド1コード
	 * @param kariUf1Name 借方ユニバーサルフィールド1名
	 * @param kariUf2Cd 借方ユニバーサルフィールド2コード
	 * @param kariUf2Name 借方ユニバーサルフィールド2名
	 * @param kariUf3Cd 借方ユニバーサルフィールド3コード
	 * @param kariUf3Name 借方ユニバーサルフィールド3名
	 * @param kariUf4Cd 借方ユニバーサルフィールド4コード
	 * @param kariUf4Name 借方ユニバーサルフィールド4名
	 * @param kariUf5Cd 借方ユニバーサルフィールド5コード
	 * @param kariUf5Name 借方ユニバーサルフィールド5名
	 * @param kariUf6Cd 借方ユニバーサルフィールド6コード
	 * @param kariUf6Name 借方ユニバーサルフィール6名
	 * @param kariUf7Cd 借方ユニバーサルフィールド7コード
	 * @param kariUf7Name 借方ユニバーサルフィールド7名
	 * @param kariUf8Cd 借方ユニバーサルフィールド8コード
	 * @param kariUf8Name 借方ユニバーサルフィールド8名
	 * @param kariUf9Cd 借方ユニバーサルフィールド9コード
	 * @param kariUf9Name 借方ユニバーサルフィールド9名
	 * @param kariUf10Cd 借方ユニバーサルフィールド10コード
	 * @param kariUf10Name 借方ユニバーサルフィールド10名
	 * @param kashiUf1Cd 貸方ユニバーサルフィールド1コード
	 * @param kashiUf1Name 貸方ユニバーサルフィールド1名
	 * @param kashiUf2Cd 貸方ユニバーサルフィールド2コード
	 * @param kashiUf2Name 貸方ユニバーサルフィールド2名
	 * @param kashiUf3Cd 貸方ユニバーサルフィールド3コード
	 * @param kashiUf3Name 貸方ユニバーサルフィールド3名
	 * @param kashiUf4Cd 貸方ユニバーサルフィールド4コード
	 * @param kashiUf4Name 貸方ユニバーサルフィールド4名
	 * @param kashiUf5Cd 貸方ユニバーサルフィールド5コード
	 * @param kashiUf5Name 貸方ユニバーサルフィールド5名
	 * @param kashiUf6Cd 貸方ユニバーサルフィールド6コード
	 * @param kashiUf6Name 貸方ユニバーサルフィール6名
	 * @param kashiUf7Cd 貸方ユニバーサルフィールド7コード
	 * @param kashiUf7Name 貸方ユニバーサルフィールド7名
	 * @param kashiUf8Cd 貸方ユニバーサルフィールド8コード
	 * @param kashiUf8Name 貸方ユニバーサルフィールド8名
	 * @param kashiUf9Cd 貸方ユニバーサルフィールド9コード
	 * @param kashiUf9Name 貸方ユニバーサルフィールド9名
	 * @param kashiUf10Cd 貸方ユニバーサルフィールド10コード
	 * @param kashiUf10Name 貸方ユニバーサルフィールド10名
	 * @param kariProjectCd 借方プロジェクトコード
	 * @param kariProjectName 借方プロジェクト名
	 * @param kariSegmentCd 借方セグメントコード
	 * @param kariSegmentName 借方セグメント名
	 * @param kashiProjectCd 貸方プロジェクトコード
	 * @param kashiProjectName 貸方プロジェクト名
	 * @param kashiSegmentCd 貸方セグメントコード
	 * @param kashiSegmentName 貸方セグメント名
	 * 
	 * @param userId ユーザーID
	 * @return 件数
	 */
	
	public int insertShinsei(
		
		String denpyouId,
		Date denpyouDate,
		String shouhyouShoruiFlg,
		BigDecimal kingaku,
		BigDecimal hontaiKingaku,
		BigDecimal shouhizeigaku,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		String tekiyou,
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
		String bikou,
		
		String kariFutanBumonCd,
		String kariFutanBumonName,
		String kariKamokuCd,
		String kariKamokuName,
		String kariKamokuEdabanCd,
		String kariKamokuEdabanName,
		String kariKazeiKbn,
		String kariTorihikisakiCd,
		String kariTorihikisakiName,
		
		String kashiFutanBumonCd,
		String kashiFutanBumonName,
		String kashiKamokuCd,
		String kashiKamokuName,
		String kashiKamokuEdabanCd,
		String kashiKamokuEdabanName,
		String kashiKazeiKbn,
		String kashiTorihikisakiCd,
		String kashiTorihikisakiName,

		String kariUf1Cd,
		String kariUf1Name,
		String kariUf2Cd,
		String kariUf2Name,
		String kariUf3Cd,
		String kariUf3Name,
		String kariUf4Cd,
		String kariUf4Name,
		String kariUf5Cd,
		String kariUf5Name,
		String kariUf6Cd,
		String kariUf6Name,
		String kariUf7Cd,
		String kariUf7Name,
		String kariUf8Cd,
		String kariUf8Name,
		String kariUf9Cd,
		String kariUf9Name,
		String kariUf10Cd,
		String kariUf10Name,

		String kashiUf1Cd,
		String kashiUf1Name,
		String kashiUf2Cd,
		String kashiUf2Name,
		String kashiUf3Cd,
		String kashiUf3Name,
		String kashiUf4Cd,
		String kashiUf4Name,
		String kashiUf5Cd,
		String kashiUf5Name,
		String kashiUf6Cd,
		String kashiUf6Name,
		String kashiUf7Cd,
		String kashiUf7Name,
		String kashiUf8Cd,
		String kashiUf8Name,
		String kashiUf9Cd,
		String kashiUf9Name,
		String kashiUf10Cd,
		String kashiUf10Name,
		
		String kariProjectCd,
		String kariProjectName,
		String kariSegmentCd,
		String kariSegmentName,
		String kashiProjectCd,
		String kashiProjectName,
		String kashiSegmentCd,
		String kashiSegmentName,
		
		String userId
	){
		final String sql =
				"INSERT INTO furikae "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, current_timestamp, ?, current_timestamp)";
			return connection.update(sql,denpyouId,denpyouDate,shouhyouShoruiFlg,kingaku,hontaiKingaku,shouhizeigaku,zeiritsu,keigenZeiritsuKbn,tekiyou, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name,hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
					bikou,kariFutanBumonCd,kariFutanBumonName,kariKamokuCd,kariKamokuName,kariKamokuEdabanCd,kariKamokuEdabanName,kariKazeiKbn,kariTorihikisakiCd,kariTorihikisakiName,kashiFutanBumonCd,kashiFutanBumonName,kashiKamokuCd,kashiKamokuName,kashiKamokuEdabanCd,kashiKamokuEdabanName,kashiKazeiKbn,kashiTorihikisakiCd, kashiTorihikisakiName,
					kariUf1Cd,kariUf1Name,kariUf2Cd,kariUf2Name,kariUf3Cd,kariUf3Name,kariUf4Cd, kariUf4Name, kariUf5Cd, kariUf5Name, kariUf6Cd, kariUf6Name, kariUf7Cd, kariUf7Name, kariUf8Cd, kariUf8Name, kariUf9Cd, kariUf9Name, kariUf10Cd, kariUf10Name,
					kashiUf1Cd,kashiUf1Name,kashiUf2Cd,kashiUf2Name,kashiUf3Cd,kashiUf3Name,kashiUf4Cd, kashiUf4Name, kashiUf5Cd, kashiUf5Name, kashiUf6Cd, kashiUf6Name, kashiUf7Cd, kashiUf7Name, kashiUf8Cd, kashiUf8Name, kashiUf9Cd, kashiUf9Name, kashiUf10Cd, kashiUf10Name,
					kariProjectCd,kariProjectName,kariSegmentCd,kariSegmentName,kashiProjectCd,kashiProjectName,kashiSegmentCd,kashiSegmentName,
					userId,userId);
	}
	
	/**
	 * 更新
	 * * @param denpyouId 伝票ID
	 * @param denpyouDate 伝票日
	 * @param shouhyouShoruiFlg 証憑書類フラグ
	 * @param kingaku 金額
	 * @param hontaiKingaku 本体金額
	 * @param shouhizeigaku 消費税額
	 * @param zeiritsu 消費税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param tekiyou 摘要
	 * @param hf1Cd ヘッダーフィールド１コード
	 * @param hf1Name ヘッダーフィールド１名
	 * @param hf2Cd ヘッダーフィールド２コード
	 * @param hf2Name ヘッダーフィールド２名
	 * @param hf3Cd ヘッダーフィールド３コード
	 * @param hf3Name ヘッダーフィールド３名
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
	 * @param bikou 備考
	 
	 * @param kariFutanBumonCd 借方負担部門コード
	 * @param kariFutanBumonName 借方負担部門名
	 * @param kariKamokuCd 借方勘定科目コード
	 * @param kariKamokuName 借方勘定科目名
	 * @param kariKamokuEdabanCd 借方勘定科目枝番コード
	 * @param kariKamokuEdabanName 借方勘定科目枝番名
	 * @param kariKazeiKbn 借方課税区分
	 * @param kariTorihikisakiCd 借方取引先コード
	 * @param kariTorihikisakiName 借方取引先名
	 * 
	 * @param kashiFutanBumonCd 貸方負担部門コード
	 * @param kashiFutanBumonName 貸方負担部門名
	 * @param kashiKamokuCd 貸方勘定科目コード
	 * @param kashiKamokuName 貸方勘定科目名
	 * @param kashiKamokuEdabanCd 貸方勘定科目枝番コード
	 * @param kashiKamokuEdabanName 貸方勘定科目枝番名
	 * @param kashiKazeiKbn 貸方課税区分
	 * @param kashiTorihikisakiCd 貸方取引先コード
	 * @param kashiTorihikisakiName 貸方取引先名
	 * 
	 * @param kariUf1Cd 借方ユニバーサルフィールド1コード
	 * @param kariUf1Name 借方ユニバーサルフィールド1名
	 * @param kariUf2Cd 借方ユニバーサルフィールド2コード
	 * @param kariUf2Name 借方ユニバーサルフィールド2名
	 * @param kariUf3Cd 借方ユニバーサルフィールド3コード
	 * @param kariUf3Name 借方ユニバーサルフィールド3名
	 * @param kariUf4Cd 借方ユニバーサルフィールド4コード
	 * @param kariUf4Name 借方ユニバーサルフィールド4名
	 * @param kariUf5Cd 借方ユニバーサルフィールド5コード
	 * @param kariUf5Name 借方ユニバーサルフィールド5名
	 * @param kariUf6Cd 借方ユニバーサルフィールド6コード
	 * @param kariUf6Name 借方ユニバーサルフィール6名
	 * @param kariUf7Cd 借方ユニバーサルフィールド7コード
	 * @param kariUf7Name 借方ユニバーサルフィールド7名
	 * @param kariUf8Cd 借方ユニバーサルフィールド8コード
	 * @param kariUf8Name 借方ユニバーサルフィールド8名
	 * @param kariUf9Cd 借方ユニバーサルフィールド9コード
	 * @param kariUf9Name 借方ユニバーサルフィールド9名
	 * @param kariUf10Cd 借方ユニバーサルフィールド10コード
	 * @param kariUf10Name 借方ユニバーサルフィールド10名
	 * @param kashiUf1Cd 貸方ユニバーサルフィールド1コード
	 * @param kashiUf1Name 貸方ユニバーサルフィールド1名
	 * @param kashiUf2Cd 貸方ユニバーサルフィールド2コード
	 * @param kashiUf2Name 貸方ユニバーサルフィールド2名
	 * @param kashiUf3Cd 貸方ユニバーサルフィールド3コード
	 * @param kashiUf3Name 貸方ユニバーサルフィールド3名
	 * @param kashiUf4Cd 貸方ユニバーサルフィールド4コード
	 * @param kashiUf4Name 貸方ユニバーサルフィールド4名
	 * @param kashiUf5Cd 貸方ユニバーサルフィールド5コード
	 * @param kashiUf5Name 貸方ユニバーサルフィールド5名
	 * @param kashiUf6Cd 貸方ユニバーサルフィールド6コード
	 * @param kashiUf6Name 貸方ユニバーサルフィール6名
	 * @param kashiUf7Cd 貸方ユニバーサルフィールド7コード
	 * @param kashiUf7Name 貸方ユニバーサルフィールド7名
	 * @param kashiUf8Cd 貸方ユニバーサルフィールド8コード
	 * @param kashiUf8Name 貸方ユニバーサルフィールド8名
	 * @param kashiUf9Cd 貸方ユニバーサルフィールド9コード
	 * @param kashiUf9Name 貸方ユニバーサルフィールド9名
	 * @param kashiUf10Cd 貸方ユニバーサルフィールド10コード
	 * @param kashiUf10Name 貸方ユニバーサルフィールド10名
	 * 
	 * @param kariProjectCd 借方プロジェクトコード
	 * @param kariProjectName 借方プロジェクト名
	 * @param kariSegmentCd 借方セグメントコード
	 * @param kariSegmentName 借方セグメント名
	 * @param kashiProjectCd 貸方プロジェクトコード
	 * @param kashiProjectName 貸方プロジェクト名
	 * @param kashiSegmentCd 貸方セグメントコード
	 * @param kashiSegmentName 貸方セグメント名
	 * 
	 * @param userId ユーザーID
	 * @return 件数	
	 */
	
	 public int updateShinsei(
		
		String denpyouId,
		Date denpyouDate,
		String shouhyouShoruiFlg,
		BigDecimal kingaku,
		BigDecimal hontaiKingaku,
		BigDecimal shouhizeigaku,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		String tekiyou,
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
		String bikou,
		
		String kariFutanBumonCd,
		String kariFutanBumonName,
		String kariKamokuCd,
		String kariKamokuName,
		String kariKamokuEdabanCd,
		String kariKamokuEdabanName,
		String kariKazeiKbn,
		String kariTorihikisakiCd,
		String kariTorihikisakiName,
		
		String kashiFutanBumonCd,
		String kashiFutanBumonName,
		String kashiKamokuCd,
		String kashiKamokuName,
		String kashiKamokuEdabanCd,
		String kashiKamokuEdabanName,
		String kashiKazeiKbn,
		String kashiTorihikisakiCd,
		String kashiTorihikisakiName,

		String kariUf1Cd,
		String kariUf1Name,
		String kariUf2Cd,
		String kariUf2Name,
		String kariUf3Cd,
		String kariUf3Name,
		String kariUf4Cd,
		String kariUf4Name,
		String kariUf5Cd,
		String kariUf5Name,
		String kariUf6Cd,
		String kariUf6Name,
		String kariUf7Cd,
		String kariUf7Name,
		String kariUf8Cd,
		String kariUf8Name,
		String kariUf9Cd,
		String kariUf9Name,
		String kariUf10Cd,
		String kariUf10Name,
		
		String kashiUf1Cd,
		String kashiUf1Name,
		String kashiUf2Cd,
		String kashiUf2Name,
		String kashiUf3Cd,
		String kashiUf3Name,
		String kashiUf4Cd,
		String kashiUf4Name,
		String kashiUf5Cd,
		String kashiUf5Name,
		String kashiUf6Cd,
		String kashiUf6Name,
		String kashiUf7Cd,
		String kashiUf7Name,
		String kashiUf8Cd,
		String kashiUf8Name,
		String kashiUf9Cd,
		String kashiUf9Name,
		String kashiUf10Cd,
		String kashiUf10Name,
		
		String kariProjectCd,
		String kariProjectName,
		String kariSegmentCd,
		String kariSegmentName,
		String kashiProjectCd,
		String kashiProjectName,
		String kashiSegmentCd,
		String kashiSegmentName,
		
		String userId
	 ){
	 	final String sql =
				"UPDATE furikae "
	 		+ "SET denpyou_date = ?, shouhyou_shorui_flg = ?,kingaku = ?,hontai_kingaku = ?,shouhizeigaku = ?,zeiritsu = ?,keigen_zeiritsu_kbn = ?,tekiyou = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, "
			+ "      bikou = ?,kari_futan_bumon_cd = ?,kari_futan_bumon_name = ?,kari_kamoku_cd = ?,kari_kamoku_name = ?,kari_kamoku_edaban_cd = ?,kari_kamoku_edaban_name = ?,kari_kazei_kbn = ?,kari_torihikisaki_cd = ?,kari_torihikisaki_name_ryakushiki = ?,kashi_futan_bumon_cd = ?,kashi_futan_bumon_name = ?,kashi_kamoku_cd = ?,kashi_kamoku_name = ?,kashi_kamoku_edaban_cd = ?,kashi_kamoku_edaban_name = ?,kashi_kazei_kbn = ?,kashi_torihikisaki_cd = ?,kashi_torihikisaki_name_ryakushiki = ?,kari_uf1_cd = ?,kari_uf1_name_ryakushiki = ?,kari_uf2_cd = ?,kari_uf2_name_ryakushiki = ?,kari_uf3_cd = ?,kari_uf3_name_ryakushiki = ?,kari_uf4_cd = ?, kari_uf4_name_ryakushiki = ?, kari_uf5_cd = ?, kari_uf5_name_ryakushiki = ?, kari_uf6_cd = ?, kari_uf6_name_ryakushiki = ?, kari_uf7_cd = ?, kari_uf7_name_ryakushiki = ?, kari_uf8_cd = ?, kari_uf8_name_ryakushiki = ?, kari_uf9_cd = ?, kari_uf9_name_ryakushiki = ?, kari_uf10_cd = ?, kari_uf10_name_ryakushiki = ?, "
			+ "      kashi_uf1_cd = ?,kashi_uf1_name_ryakushiki = ?,kashi_uf2_cd = ?,kashi_uf2_name_ryakushiki = ?,kashi_uf3_cd = ?,kashi_uf3_name_ryakushiki = ?, kashi_uf4_cd = ?, kashi_uf4_name_ryakushiki = ?, kashi_uf5_cd = ?, kashi_uf5_name_ryakushiki = ?, kashi_uf6_cd = ?, kashi_uf6_name_ryakushiki = ?, kashi_uf7_cd = ?, kashi_uf7_name_ryakushiki = ?, kashi_uf8_cd = ?, kashi_uf8_name_ryakushiki = ?, kashi_uf9_cd = ?, kashi_uf9_name_ryakushiki = ?, kashi_uf10_cd = ?, kashi_uf10_name_ryakushiki = ?, kari_project_cd = ?, kari_project_name = ?, kari_segment_cd = ?, kari_segment_name_ryakushiki = ?, kashi_project_cd = ?, kashi_project_name = ?, kashi_segment_cd = ?, kashi_segment_name_ryakushiki = ?,"
	 		+ "      koushin_user_id = ?, koushin_time = current_timestamp "
	 		+ "WHERE denpyou_id = ?";
		return connection.update(
		 		sql, denpyouDate,shouhyouShoruiFlg,kingaku,hontaiKingaku,shouhizeigaku, zeiritsu,keigenZeiritsuKbn,tekiyou, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
		 		bikou,kariFutanBumonCd,kariFutanBumonName,kariKamokuCd,kariKamokuName,kariKamokuEdabanCd, kariKamokuEdabanName,kariKazeiKbn, kariTorihikisakiCd,kariTorihikisakiName,kashiFutanBumonCd,kashiFutanBumonName,kashiKamokuCd,kashiKamokuName, kashiKamokuEdabanCd,kashiKamokuEdabanName,kashiKazeiKbn,kashiTorihikisakiCd,kashiTorihikisakiName,kariUf1Cd,kariUf1Name,kariUf2Cd,kariUf2Name,kariUf3Cd,kariUf3Name,kariUf4Cd, kariUf4Name, kariUf5Cd, kariUf5Name, kariUf6Cd, kariUf6Name, kariUf7Cd, kariUf7Name, kariUf8Cd, kariUf8Name, kariUf9Cd, kariUf9Name, kariUf10Cd, kariUf10Name,
		 		kashiUf1Cd,kashiUf1Name,kashiUf2Cd,kashiUf2Name,kashiUf3Cd,kashiUf3Name,kashiUf4Cd, kashiUf4Name, kashiUf5Cd, kashiUf5Name, kashiUf6Cd, kashiUf6Name, kashiUf7Cd, kashiUf7Name, kashiUf8Cd, kashiUf8Name, kashiUf9Cd, kashiUf9Name, kashiUf10Cd, kashiUf10Name,
		 		kariProjectCd, kariProjectName,kariSegmentCd, kariSegmentName,kashiProjectCd,kashiProjectName,kashiSegmentCd,kashiSegmentName, userId,denpyouId);
	 }
}
