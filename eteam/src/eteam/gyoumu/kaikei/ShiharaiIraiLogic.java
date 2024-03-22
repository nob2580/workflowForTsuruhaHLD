package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;

/**
 * 通勤定期申請Logic
 */
public class ShiharaiIraiLogic extends EteamAbstractLogic {
	
	/**
	 * 新規登録する。
	 * @param denpyouId 伝票ID
	 * @param keijoubi 計上日
	 * @param yoteiBi 予定日
	 * @param shiharaibi 支払日
	 * @param shiharaiKijitsu 支払期日
	 * @param torihikisakiCd 取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param ichigensakiTorihikisakiName 一見先名
	 * @param edi EDI
	 * @param shiharaiGoukei 支払合計
	 * @param sousaiGoukei 相殺合計
	 * @param sashihikiShiharai 差引支払額
	 * @param manekinGensen マネキン源泉
	 * @param shouhyouShoruiFlg 領収書・請求書等
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
	 * @param shiharaiHouhou 支払方法
	 * @param shiharaiShubetsu 支払種別
	 * @param furikomiGinkouCd 振込銀行コード
	 * @param furikomiGinkouName 振込銀行名
	 * @param furikomiGinkouShitenCd 振込支店コード
	 * @param furikomiGinkouShitenName 振込支店名
	 * @param kouzaShubetsu 口座種別
	 * @param kouzaBangou 口座番号
	 * @param kouzaMeiginin 口座名義人
	 * @param tesuuryou 手数料
	 * @param hosoku 補足
	 * @param userId ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertShinsei(
		String denpyouId,
		Date keijoubi,
		Date yoteiBi,
		Date shiharaibi,
		Date shiharaiKijitsu,
		String torihikisakiCd,
		String torihikisakiNameRyakushiki,
		String ichigensakiTorihikisakiName,
		String edi,
		BigDecimal shiharaiGoukei,
		BigDecimal sousaiGoukei,
		BigDecimal sashihikiShiharai,
		BigDecimal manekinGensen,
		String shouhyouShoruiFlg,
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
		String shiharaiHouhou,
		String shiharaiShubetsu,
		String furikomiGinkouCd,
		String furikomiGinkouName,
		String furikomiGinkouShitenCd,
		String furikomiGinkouShitenName,
		String kouzaShubetsu,
		String kouzaBangou,
		String kouzaMeiginin,
		String tesuuryou,
		String hosoku,
		String userId
	) {
		final String sql =
				"INSERT INTO shiharai_irai "
			+ "VALUES ("
			+ "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
			+ "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ "  '0', '0', '0', '経費', '0', '0', "
			+ "  ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, keijoubi, yoteiBi, shiharaibi, shiharaiKijitsu, torihikisakiCd, torihikisakiNameRyakushiki, ichigensakiTorihikisakiName, edi, shiharaiGoukei, sousaiGoukei, sashihikiShiharai, manekinGensen, 
			shouhyouShoruiFlg, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name, 
			shiharaiHouhou, shiharaiShubetsu, furikomiGinkouCd, furikomiGinkouName, furikomiGinkouShitenCd, furikomiGinkouShitenName, kouzaShubetsu, kouzaBangou, kouzaMeiginin, tesuuryou, hosoku,
			userId, userId);
	}
	
	/**
	 * 更新する。
	 * @param denpyouId 伝票ID
	 * @param keijoubi 計上日
	 * @param yoteiBi 予定日
	 * @param shiharaibi 支払日
	 * @param shiharaiKijitsu 支払期日
	 * @param torihikisakiCd 取引先コード
	 * @param torihikisakiNameRyakushiki 取引先名（略式）
	 * @param ichigensakiTorihikisakiName 一見先名
	 * @param edi EDI
	 * @param shiharaiGoukei 支払合計
	 * @param sousaiGoukei 相殺合計
	 * @param sashihikiShiharai 差引支払額
	 * @param manekinGensen マネキン源泉
	 * @param shouhyouShoruiFlg 領収書・請求書等
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
	 * @param shiharaiHouhou 支払方法
	 * @param shiharaiShubetsu 支払種別
	 * @param furikomiGinkouCd 振込銀行コード
	 * @param furikomiGinkouName 振込銀行名
	 * @param furikomiGinkouShitenCd 振込支店コード
	 * @param furikomiGinkouShitenName 振込支店名
	 * @param kouzaShubetsu 口座種別
	 * @param kouzaBangou 口座番号
	 * @param kouzaMeiginin 口座名義人
	 * @param tesuuryou 手数料
	 * @param hosoku 補足
	 * @param userId ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int updateShinsei(
		String denpyouId,
		Date keijoubi,
		Date yoteiBi,
		Date shiharaibi,
		Date shiharaiKijitsu,
		String torihikisakiCd,
		String torihikisakiNameRyakushiki,
		String ichigensakiTorihikisakiName,
		String edi,
		BigDecimal shiharaiGoukei,
		BigDecimal sousaiGoukei,
		BigDecimal sashihikiShiharai,
		BigDecimal manekinGensen,
		String shouhyouShoruiFlg,
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
		String shiharaiHouhou,
		String shiharaiShubetsu,
		String furikomiGinkouCd,
		String furikomiGinkouName,
		String furikomiGinkouShitenCd,
		String furikomiGinkouShitenName,
		String kouzaShubetsu,
		String kouzaBangou,
		String kouzaMeiginin,
		String tesuuryou,
		String hosoku,
		String userId
	) {
		final String sql =
			"UPDATE shiharai_irai "
			+ "SET "
			+ " keijoubi = ? "
			+ ", yoteibi = ? "
			+ ", shiharaibi = ? "
			+ ", shiharai_kijitsu = ? "
			+ ", torihikisaki_cd = ? "
			+ ", torihikisaki_name_ryakushiki = ? "
			+ ", ichigensaki_torihikisaki_name = ? "
			+ ", edi = ? "
			+ ", shiharai_goukei = ? "
			+ ", sousai_goukei = ? "
			+ ", sashihiki_shiharai = ? "
			+ ", manekin_gensen = ? "
			+ ", shouhyou_shorui_flg = ? "
			+ ", hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ? "
			+ ", shiharai_houhou = ? "
			+ ", shiharai_shubetsu = ? "
			+ ", furikomi_ginkou_cd = ? "
			+ ", furikomi_ginkou_name = ? "
			+ ", furikomi_ginkou_shiten_cd = ? "
			+ ", furikomi_ginkou_shiten_name = ? "
			+ ", yokin_shubetsu = ? "
			+ ", kouza_bangou = ? "
			+ ", kouza_meiginin = ? "
			+ ", tesuuryou = ? "
			+ ", hosoku = ? "
			+ ", koushin_user_id = ? "
			+ ", koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(
			sql,
			keijoubi,
			yoteiBi,
			shiharaibi,
			shiharaiKijitsu,
			torihikisakiCd,
			torihikisakiNameRyakushiki,
			ichigensakiTorihikisakiName,
			edi,
			shiharaiGoukei,
			sousaiGoukei,
			sashihikiShiharai,
			manekinGensen,
			shouhyouShoruiFlg, 
			hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			shiharaiHouhou,
			shiharaiShubetsu,
			furikomiGinkouCd,
			furikomiGinkouName,
			furikomiGinkouShitenCd,
			furikomiGinkouShitenName,
			kouzaShubetsu,
			kouzaBangou,
			kouzaMeiginin,
			tesuuryou,
			hosoku,
			userId,
			denpyouId);
	}
	
	/**
	 * 明細の登録
	 * @param denpyouId                伝票ID
	 * @param denpyouEdano             伝票枝番号
	 * @param shiwakeEdano             仕訳枝番号
	 * @param torihikiName             取引名
	 * @param tekiyou                  摘要
	 * @param zeiritsu                 税率
	 * @param keigenZeiritsuKbn        軽減税率区分
	 * @param shiharaiKingaku          支払金額
	 * @param kariFutanBumonCd         借方負担部門コード
	 * @param kariFutanBumonName       借方負担部門名
	 * @param kariKamokuCd             借方科目コード
	 * @param kariKamokuName           借方科目名
	 * @param kariKamokuEdabanCd       借方科目枝番コード
	 * @param kariKamokuEdabanName     借方科目枝番名
	 * @param kariKazeiKbn             借方課税区分
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
	 * @param uf10Cd                   UF10コード
	 * @param uf10NameRyakushiki       UF10名（略式）
	 * @param projectCd                プロジェクトコード
	 * @param projectName              プロジェクト名
	 * @param segmentCd                セグメントコード
	 * @param segmentNameRyakushiki    セグメント名（略式）
	 * @param tekiyouCd                摘要コード
	 * @param userId                   ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertMeisai(
		String denpyouId,
		int denpyouEdano,
		int shiwakeEdano,
		String torihikiName,
		String tekiyou,
		BigDecimal shiharaiKingaku,
		String kariFutanBumonCd,
		String kariFutanBumonName,
		String kariKamokuCd,
		String kariKamokuName,
		String kariKamokuEdabanCd,
		String kariKamokuEdabanName,
		String kariKazeiKbn,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
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
				"INSERT INTO shiharai_irai_meisai "
			+ "VALUES("
			+ "  ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
			+ "  ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouEdano, shiwakeEdano, torihikiName, tekiyou, shiharaiKingaku, kariFutanBumonCd, kariFutanBumonName, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, zeiritsu, keigenZeiritsuKbn, 
			uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki,
			uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName, segmentCd, segmentNameRyakushiki, tekiyouCd, 
			userId, userId);
	}

	/**
	 * 伝票配下の明細レコードを全て削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	@Deprecated
	public int deleteMeisai(String denpyouId) {
		final String sql =
				"DELETE FROM shiharai_irai_meisai "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}

	/**
	 * CSVアップロードフラグを1にする
	 * @param denpyouId 伝票ID
	 * @param hasseiShubetsu 発生種別
	 */
	@Deprecated
	public void updateCSVUploadFlg(String denpyouId, String hasseiShubetsu) {
		final String sql = "UPDATE shiharai_irai SET csv_upload_flg = '1', hassei_shubetsu = ? WHERE denpyou_id = ?";
		connection.update(sql, hasseiShubetsu, denpyouId);
	}
}
