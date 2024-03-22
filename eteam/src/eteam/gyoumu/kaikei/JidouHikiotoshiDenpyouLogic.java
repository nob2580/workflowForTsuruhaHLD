package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.common.select.MasterKanriCategoryLogic;

/**
 * 自動引落伝票画面Logic
 */
public class JidouHikiotoshiDenpyouLogic extends EteamAbstractLogic {

	/**
	 * 新規登録する。
	 * @param denpyouId 伝票ID
	 * @param keijoubi 計上日
	 * @param hikiotoshibi 引落日
	 * @param shouhyouShoruiFlg 証憑書類フラグ
	 * @param hontaiKingakuGoukei 本体金額合計
	 * @param shouhizeigakuGoukei 消費税額合計
	 * @param shiharaiKingakuGoukei 支払金額合計
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
	 * @param hosoku 補足
	 * @param userId ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertShinsei(
		String denpyouId,
		Date keijoubi,
		Date hikiotoshibi,
		String shouhyouShoruiFlg,
		BigDecimal hontaiKingakuGoukei,
		BigDecimal shouhizeigakuGoukei,
		BigDecimal shiharaiKingakuGoukei,
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
		String userId
	) {
		final String sql =
				"INSERT INTO jidouhikiotoshi "
			+ "(denpyou_id, keijoubi, hikiotoshibi, shouhyou_shorui_flg, hontai_kingaku_goukei, shouhizeigaku_goukei, shiharai_kingaku_goukei, hf1_cd, hf1_name_ryakushiki, hf2_cd, hf2_name_ryakushiki, hf3_cd, hf3_name_ryakushiki, hf4_cd, hf4_name_ryakushiki, hf5_cd, hf5_name_ryakushiki, hf6_cd, hf6_name_ryakushiki, hf7_cd, hf7_name_ryakushiki, hf8_cd, hf8_name_ryakushiki, hf9_cd, hf9_name_ryakushiki, hf10_cd, hf10_name_ryakushiki, hosoku, touroku_user_id, touroku_time, koushin_user_id, koushin_time)"
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, keijoubi, hikiotoshibi, shouhyouShoruiFlg, hontaiKingakuGoukei, shouhizeigakuGoukei, shiharaiKingakuGoukei, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name,
			hosoku,userId, userId);
	}

	/**
	 * 更新する。
	 * @param denpyouId 伝票ID
	 * @param keijoubi 計上日
	 * @param hikiotoshibi 引落日
	 * @param shouhyouShoruiFlg 証憑書類フラグ
	 * @param hontaiKingakuGoukei 本体金額合計
	 * @param shouhizeigakuGoukei 消費税額合計
	 * @param shiharaiKingakuGoukei 支払金額合計
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
	 * @param hosoku 補足
	 * @param userId ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int updateShinsei(
		String denpyouId,
		Date keijoubi,
		Date hikiotoshibi,
		String shouhyouShoruiFlg,
		BigDecimal hontaiKingakuGoukei,
		BigDecimal shouhizeigakuGoukei,
		BigDecimal shiharaiKingakuGoukei,
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
		String userId
	) {
		final String sql =
				"UPDATE jidouhikiotoshi "
			+ "SET hikiotoshibi = ?, keijoubi = ?, shouhyou_shorui_flg = ?, hontai_kingaku_goukei = ?, shouhizeigaku_goukei = ?, shiharai_kingaku_goukei = ?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(
			sql,
			hikiotoshibi, keijoubi, shouhyouShoruiFlg, hontaiKingakuGoukei, shouhizeigakuGoukei, shiharaiKingakuGoukei, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name , hf5Cd, hf5Name , hf6Cd, hf6Name , hf7Cd, hf7Name , hf8Cd, hf8Name , hf9Cd, hf9Name , hf10Cd, hf10Name ,hosoku,
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
	 * @param hontaiKingaku            本体金額
	 * @param shouhizeigaku            消費税額
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
	 * @param userId ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertMeisai(
		String denpyouId,
		int denpyouEdano,
		int shiwakeEdano,
		String torihikiName,
		String tekiyou,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
		BigDecimal shiharaiKingaku,
		BigDecimal hontaiKingaku,
		BigDecimal shouhizeigaku,
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
				"INSERT INTO jidouhikiotoshi_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouEdano, shiwakeEdano, torihikiName, tekiyou, zeiritsu, keigenZeiritsuKbn, shiharaiKingaku, hontaiKingaku, shouhizeigaku, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki, uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
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
				"DELETE FROM jidouhikiotoshi_meisai "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}

	/**
	 * 計上日プルダウン入力時、引落日に一番近い過去日付(同日含む)を計上日リストから取得する（これは純粋ロジックなので残す）。
	 * @param denpyouKbn 伝票ID
	 * @param hikiotoshibi 引落日
	 * @param errorList エラーリスト
	 * @return 引落日に一番近い過去日付
	 */
	public String getkeijoubiPulldown(String denpyouKbn, String hikiotoshibi, List<String> errorList){
		String keijoubi = null;
		MasterKanriCategoryLogic masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);

		//計上日(過去日付)取得
		List<String> keijoubiList = masterKanriLogic.loadKeijoubiList(denpyouKbn);
		for(String keijoobiTmp:keijoubiList) {
			//引落日=>計上日リスト日付の場合
			if (hikiotoshibi.compareTo(keijoobiTmp) >= 0)
			{
				keijoubi = keijoobiTmp;
			}
		}
		//過去日付が取得できない場合(恒常的にプルダウン入力で運用していれば起こりえないはずではあるが)
		if(keijoubi == null) errorList.add("計上日に指定できる日付が存在しません。");
		return keijoubi;
	}
}
