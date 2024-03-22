package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;

/**
 * 経費精算Logic
 */
public class KeihiTatekaeSeisanLogic extends EteamAbstractLogic {
	
	/**
	 * 経費精算テーブルに新規登録する。
	 * @param denpyouId                  伝票ID
	 * @param karibaraiDenpyouId         仮払伝票ID
	 * @param karibaraiOn              仮払ありなし
	 * @param karibaraiMishiyouFlg       仮払未使用フラグ
	 * @param dairiFlg                   代理起票フラグ
	 * @param keijoubi                   計上日
	 * @param shiharaibi                 支払日
	 * @param shiharaiKiboubi            支払希望日
	 * @param shiharaihouhou             支払方法
	 * @param hontaiKingakuGoukei        本体金額合計
	 * @param shouhizeigakuGoukei        消費税額合計
	 * @param shiharaiKingakuGoukei      支払金額合計
	 * @param houjinCardRiyouKingaku     内法人カード利用合計
	 * @param kaishaTehaiKingaku         会社手配合計
	 * @param sashihikiShikyuuKingaku    差引支給金額
	 * @param hf1Cd                      ヘッダーフィールド１コード
	 * @param hf1Name                    ヘッダーフィールド１名
	 * @param hf2Cd                      ヘッダーフィールド２コード
	 * @param hf2Name                    ヘッダーフィールド２名
	 * @param hf3Cd                      ヘッダーフィールド３コード
	 * @param hf3Name                    ヘッダーフィールド３名
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
	 * @param hosoku                     補足
	 * @param userId                     ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertShinsei(
		String denpyouId,
		String karibaraiDenpyouId,
		String karibaraiOn,
		String karibaraiMishiyouFlg,
		String dairiFlg,
		Date   keijoubi,
		Date   shiharaibi,
		Date   shiharaiKiboubi,
		String shiharaihouhou,
		BigDecimal hontaiKingakuGoukei,
		BigDecimal shouhizeigakuGoukei,
		BigDecimal shiharaiKingakuGoukei,
		BigDecimal houjinCardRiyouKingaku,
		BigDecimal kaishaTehaiKingaku,
		BigDecimal sashihikiShikyuuKingaku,
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
				"INSERT INTO keihiseisan "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, karibaraiDenpyouId, karibaraiOn, karibaraiMishiyouFlg, dairiFlg, keijoubi, shiharaibi, shiharaiKiboubi, shiharaihouhou, hontaiKingakuGoukei, shouhizeigakuGoukei, shiharaiKingakuGoukei, houjinCardRiyouKingaku, kaishaTehaiKingaku, sashihikiShikyuuKingaku, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name, hf5Cd, hf5Name, hf6Cd, hf6Name, hf7Cd, hf7Name, hf8Cd, hf8Name, hf9Cd, hf9Name, hf10Cd, hf10Name, hosoku,
			userId, userId);
	}
	
	/**
	 * 経費精算テーブルを更新する。
	 * @param denpyouId                  伝票ID
	 * @param karibaraiDenpyouId         仮払伝票ID
	 * @param karibaraiOn                仮払ありなし
	 * @param karibaraiMishiyouFlg       仮払未使用フラグ
	 * @param keijoubi                   計上日
	 * @param shiharaibi                 支払日
	 * @param shiharaiKiboubi            支払希望日
	 * @param shiharaihouhou             支払方法
	 * @param hontaiKingakuGoukei        本体金額合計
	 * @param shouhizeigakuGoukei        消費税額合計
	 * @param shiharaiKingakuGoukei      支払金額合計
	 * @param houjinCardRiyouKingaku     内法人カード利用合計
	 * @param kaishaTehaiKingaku         会社手配合計
	 * @param sashihikiShikyuuKingaku    差引支給金額
	 * @param hf1Cd                      ヘッダーフィールド１コード
	 * @param hf1Name                    ヘッダーフィールド１名
	 * @param hf2Cd                      ヘッダーフィールド２コード
	 * @param hf2Name                    ヘッダーフィールド２名
	 * @param hf3Cd                      ヘッダーフィールド３コード
	 * @param hf3Name                    ヘッダーフィールド３名
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
	 * @param hosoku                     補足
	 * @param userId                     ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int updateShinsei(
			String denpyouId,
			String karibaraiDenpyouId,
			String karibaraiOn,
			String karibaraiMishiyouFlg,
			Date   keijoubi,
			Date   shiharaibi,
			Date   shiharaiKiboubi,
			String shiharaihouhou,
			BigDecimal hontaiKingakuGoukei,
			BigDecimal shouhizeigakuGoukei,
			BigDecimal shiharaiKingakuGoukei,
			BigDecimal houjinCardRiyouKingaku,
			BigDecimal kaishaTehaiKingaku,
			BigDecimal sashihikiShikyuuKingaku,
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
				"UPDATE keihiseisan "
			+ "SET karibarai_denpyou_id = ?, karibarai_on = ?, karibarai_mishiyou_flg = ?, keijoubi = ?, shiharaibi = ?, shiharaikiboubi=?, shiharaihouhou = ?, hontai_kingaku_goukei = ?, shouhizeigaku_goukei = ?, shiharai_kingaku_goukei = ?, houjin_card_riyou_kingaku = ?, kaisha_tehai_kingaku = ?, sashihiki_shikyuu_kingaku=?, hf1_cd = ?, hf1_name_ryakushiki = ?, hf2_cd = ?, hf2_name_ryakushiki = ?, hf3_cd = ?, hf3_name_ryakushiki = ?, hf4_cd = ?, hf4_name_ryakushiki = ?, hf5_cd = ?, hf5_name_ryakushiki = ?, hf6_cd = ?, hf6_name_ryakushiki = ?, hf7_cd = ?, hf7_name_ryakushiki = ?, hf8_cd = ?, hf8_name_ryakushiki = ?, hf9_cd = ?, hf9_name_ryakushiki = ?, hf10_cd = ?, hf10_name_ryakushiki = ?, hosoku = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(
			sql,
			karibaraiDenpyouId, karibaraiOn, karibaraiMishiyouFlg, keijoubi, shiharaibi, shiharaiKiboubi, shiharaihouhou, hontaiKingakuGoukei, shouhizeigakuGoukei, shiharaiKingakuGoukei, houjinCardRiyouKingaku, kaishaTehaiKingaku, sashihikiShikyuuKingaku, hf1Cd, hf1Name, hf2Cd, hf2Name, hf3Cd, hf3Name, hf4Cd, hf4Name , hf5Cd, hf5Name , hf6Cd, hf6Name , hf7Cd, hf7Name , hf8Cd, hf8Name , hf9Cd, hf9Name , hf10Cd, hf10Name ,hosoku,
			userId,
			denpyouId);
	}
	
	/** 
	 *
	 * 経費精算明細の登録
	 * @param denpyouId                伝票ID
	 * @param denpyouEdano             伝票枝番号
	 * @param shiwakeEdano             仕訳枝番号
	 * @param siyousha_userId          使用者ID
	 * @param siyousha_shainNo         社員番号
	 * @param siyousha_userSei         使用者姓
	 * @param siyousha_userMei         使用者名
	 * @param shiyoubi                 使用日
	 * @param shouhyouShoruiFlg        証憑書類フラグ
	 * @param torihikiName             取引名
	 * @param tekiyou                  摘要
	 * @param zeiritsu                 税率
	 * @param keigenZeiritsuKbn        軽減税率区分
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
	 * @param himodukeCardMeisai       紐付元カード明細
	 * @param userId                   ユーザーID
	 * @return 件数
	 */
	@Deprecated
	public int insertMeisai(
		String denpyouId,
		int denpyouEdano,
		int shiwakeEdano,
		String siyousha_userId,
		String siyousha_shainNo,
		String siyousha_userSei,
		String siyousha_userMei,
		Date shiyoubi,
		String shouhyouShoruiFlg,
		String torihikiName,
		String tekiyou,
		BigDecimal zeiritsu,
		String keigenZeiritsuKbn,
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
				"INSERT INTO keihiseisan_meisai "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(
			sql,
			denpyouId, denpyouEdano, shiwakeEdano, siyousha_userId, siyousha_shainNo, siyousha_userSei, siyousha_userMei, shiyoubi, shouhyouShoruiFlg, torihikiName, tekiyou, zeiritsu, keigenZeiritsuKbn, shiharaiKingaku, hontaiKingaku, shouhizeigaku, houjinCardRiyouFlg, kaishaTehaiFlg, kousaihiShousaiHyoujiFlg, ninzuuRiyouFlg, kousaihiShousai, kousaihiNinzuu, kousaihiHitoriKingaku, kariFutanBumonCd, kariFutanBumonName, torihikisakiCd, torihikisakiNameRyakushiki, kariKamokuCd, kariKamokuName, kariKamokuEdabanCd, kariKamokuEdabanName, kariKazeiKbn, kashiFutanBumonCd, kashiFutanBumonName, kashiKamokuCd, kashiKamokuName, kashiKamokuEdabanCd, kashiKamokuEdabanName, kashiKazeiKbn, uf1Cd, uf1NameRyakushiki, uf2Cd, uf2NameRyakushiki, uf3Cd, uf3NameRyakushiki,
			uf4Cd, uf4NameRyakushiki, uf5Cd, uf5NameRyakushiki, uf6Cd, uf6NameRyakushiki, uf7Cd, uf7NameRyakushiki, uf8Cd, uf8NameRyakushiki, uf9Cd, uf9NameRyakushiki, uf10Cd, uf10NameRyakushiki,
			projectCd, projectName, segmentCd, segmentNameRyakushiki,tekiyouCd, himodukeCardMeisai, userId, userId);
	}
	
	/**
	 * 伝票配下の経費精算明細レコードを全て削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	@Deprecated
	public int deleteMeisai(String denpyouId) {
		final String sql =
				"DELETE FROM keihiseisan_meisai "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}
	
	/**
	 * 経費精算の計上日を更新する。
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
				"UPDATE keihiseisan "
			+   "SET keijoubi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, keijoubi, userId, denpyouId);
	}
	
	/**
	 * 経費精算の計上日がnullなら計上日を本日の日付にする。
	 * @param denpyouId  伝票ID
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
	public int updateKeijoubiTodayIfNull(
			String denpyouId,
			String userId
	) {
		String sql = "SELECT * FROM keihiseisan WHERE denpyou_id=?";
		GMap denpyouRecord = connection.find(sql,denpyouId);
		if (denpyouRecord.get("keijoubi") != null ) return 0;
		sql =
			"UPDATE keihiseisan "
			+   "SET keijoubi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, new java.sql.Date(System.currentTimeMillis()), userId, denpyouId);
	} 
	
	/**
	 * 経費精算の支払日を更新する。
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
				"UPDATE keihiseisan "
			+   "SET shiharaibi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, shiharaibi, userId, denpyouId);
	}

	/**
	 * 全明細を参照可能かどうか判定
	 * @param denpyouId 伝票ID
	 * @param userInfo ユーザー
	 * @param dairiFlg 代理フラグ
	 * @return 全表示可能ならtrue
	 */
	public boolean judgeAllMeisaiView(String denpyouId, User userInfo, String dairiFlg) {
		WorkflowCategoryLogic wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		WorkflowEventControlLogic wfCtr = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);

		//代理起票でなければそもそも明細単位の制御はなし
		if(dairiFlg.equals("0")) {
			return true;
		//代理起票で
		}else {
			//未登録なら全部見れてOK（代理起票者そのもの）
			if (isEmpty(denpyouId))
			{
				return true;
			}

			//ユーザ情報なしなら全部見れてOK（SIAS連携用PDF作成と思われる）
			if (userInfo == null)
			{
				return true;
			}

			//代理起票であっても、同部門参照権限があるなら全部見れることとする　※被代理者であっても
			if(setting.shozokuBumonDataKyoyu().equals("1")) {
				if(wfCtr.judgeShozokuBumonKyoyu(userInfo, denpyouId)) {
					return true;
				}
			}

			//代理起票であり、同部門参照権限による参照でもない場合の判定
			//一般ユーザーで開いていて承認ルートにいないなら被代理者なので、一部表示。それ以外なら、全表示。
			if (userInfo.getGyoumuRoleId() != null)
			{
				return true;
			}
			if (0 < wfLogic.countExistShouninsha(denpyouId, userInfo) + wfLogic.countExistShouninshaGougiKo(denpyouId, userInfo))
			{
				return true;
			}
			return false;
		}
	}

	/**
	 * 指定した仮払伝票の精算完了日を更新
	 * @param karibaraiDenpyouId 伝票ID
	 * @param userId ユーザーID
	 * @return 処理件数
	 */
	@Deprecated
	public int updateKaribaraiSeisanbi(String karibaraiDenpyouId, String userId) {
		final String sql =
				"UPDATE karibarai "
			+   "SET seisan_kanryoubi = current_date, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, userId, karibaraiDenpyouId);
		
	}
}
