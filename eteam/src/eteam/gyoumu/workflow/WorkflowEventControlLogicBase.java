package eteam.gyoumu.workflow;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.DenpyouItemInfo;
import eteam.common.DenpyouItemInfoLogic;
import eteam.common.EbunshoLogic;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamIO;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID;
import eteam.common.EteamSendMailLogic;
import eteam.common.EteamSettingInfo;
import eteam.common.RegAccess;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.DataAccessCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.InvoiceStartDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.yosanshikkoukanri.KianBangouBoHenkouLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import eteam.gyoumu.yosanshikkoukanri.kogamen.KianKingakuCheckLogic;
import eteam.gyoumu.yosanshikkoukanri.kogamen.KianTsuikaLogic;
import eteam.gyoumu.yosanshikkoukanri.kogamen.YosanCheckLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ワークフローイベント制御Logic
 */
abstract class WorkflowEventControlLogicBase extends EteamAbstractLogic {
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(WorkflowEventControlLogic.class);

	/** 改行コード */
	protected static final String BR = System.getProperty("line.separator");

	/** 支払日コード */
	protected static final String ITEM_SHIHARAIBI = "shiharaibi";

	/** 計上日コード */
	protected static final String ITEM_KEIJOUBI = "keijoubi";

	/** 合議判別部門コード */
	protected static final String GOUGI_BUMONCD = "GOUGI";

	/** システム管理 */ SystemKanriCategoryLogic systemLogic;
	/** WF */ WorkflowCategoryLogic wfLogic;
	/** 部門ユーザー管理 */ BumonUserKanriCategoryLogic bumonUserLogic;
	/** データアクセス */ DataAccessCategoryLogic dataAccessCategoryLogic;
	/** 伝票項目情報 */ DenpyouItemInfoLogic denpyouItemInfoLogic;
	/** 通知 */ TsuuchiCategoryLogic tsuuchiLogic;
	/** メール */ EteamSendMailLogic mailLogic;
	/** 会計 */ KaikeiCategoryLogic kaikeiLogic;
	/** E文書 */ EbunshoLogic eLogic;
	/** 承認ルート */ ShouninRouteTourokuLogic routeLogic;
	/** 起案追加 */ KianTsuikaLogic kianTsuikaLogic;
	/** 起票ナビカテゴリー */ KihyouNaviCategoryLogic kihyouLogic;
	/** 伝票管理 */ DenpyouKanriLogic denpyouKanriLogic;
	/** インボイス開始Dao */ InvoiceStartAbstractDao invoiceStartDao;
	/** 消費税設定Dao */ KiShouhizeiSettingDao kiShouhizeiSettingDao;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		dataAccessCategoryLogic = EteamContainer.getComponent(DataAccessCategoryLogic.class, connection);
		denpyouItemInfoLogic = EteamContainer.getComponent(DenpyouItemInfoLogic.class, connection);
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		mailLogic = EteamContainer.getComponent(EteamSendMailLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		eLogic = EteamContainer.getComponent(EbunshoLogic.class);
		routeLogic = EteamContainer.getComponent(ShouninRouteTourokuLogic.class, connection);
		kianTsuikaLogic = EteamContainer.getComponent(KianTsuikaLogic.class, connection);
		kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		this.invoiceStartDao = EteamContainer.getComponent(InvoiceStartDao.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
	}

	/**
	 * 伝票を追加する
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param denpyouJoutai 伝票状態
	 * @param sanshouDenpyouId 承認伝票ID
	 * @param daihyouFutanBumonCd 代表負担部門コード
	 * @param loginUserId ログインユーザーID
	 * @param serialNo シリアル番号
	 */
	public void insertDenpyou(String denpyouId, String denpyouKbn, String denpyouJoutai, String sanshouDenpyouId, String daihyouFutanBumonCd, String loginUserId, int serialNo) {
		final String sql = "INSERT INTO denpyou VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?)";
		connection.update(sql, denpyouId, denpyouKbn, denpyouJoutai, sanshouDenpyouId, daihyouFutanBumonCd, loginUserId, loginUserId, serialNo);
	}

	/**
	 * 伝票を更新する
	 * @param denpyouId 伝票ID
	 * @param denpyouJoutai 伝票状態
	 * @param loginUserId ユーザーID
	 * @return 結果
	 */
	public int updateDenpyou(String denpyouId, String denpyouJoutai, String loginUserId) {
		final String sql = "UPDATE denpyou "
						+  "SET denpyou_joutai = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouJoutai, loginUserId, denpyouId);
	}

	/**
	 * 伝票を更新する(代表負担部門)
	 * @param denpyouId 伝票ID
	 * @param daihyouFutanBumonCd 代表負担部門コード
	 * @param loginUserId ユーザーID
	 * @return 結果
	 */
	public int updateDenpyouDaihyouFutanBumon(String denpyouId, String daihyouFutanBumonCd, String loginUserId) {
		final String sql = "UPDATE denpyou "
						+  "SET daihyou_futan_bumon_cd = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE denpyou_id = ? ";
		return connection.update(sql, daihyouFutanBumonCd, loginUserId, denpyouId);
	}

	/**
	 * 伝票を更新する（予算執行対象月）
	 * @param denpyouId 伝票ID
	 * @param yosanCheckNengetsu 予算執行対象月
	 * @param loginUserId ユーザーID
	 * @return 結果
	 */
	public int updateDenpyouYosanCheckNengetsu(String denpyouId, String yosanCheckNengetsu, String loginUserId) {
		final String sql = "UPDATE denpyou "
						+  "SET yosan_check_nengetsu = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE denpyou_id = ? ";
		return connection.update(sql, yosanCheckNengetsu, loginUserId, denpyouId);
	}


	/**
	 * 承認ルートを削除する
	 * @param denpyouId         伝票ID
	 */
	protected void deleteShouninRoute(String denpyouId) {
		final String sql = "DELETE FROM shounin_route WHERE denpyou_id = ?";
		connection.update(sql, denpyouId);
	}

	/**
	 * デフォルトの承認ルートを初期登録する。以下を連結
	 * ①枝番1に、起票ユーザーを登録
	 * ②新規起票なら部門推奨ルートに基づいたユーザー、参照起票なら参照元の承認ルートのコピーを登録
	 * ③最終承認者に登録されたユーザーを登録
	 *
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param kingaku 金額
	 * @param sanshouDenpyouId 参照起票伝票ID(あれば)
	 * @param kihyouUserId ユーザーID(制御)
	 * @param kihyouUserName ユーザー名
	 * @param kihyouBumonCd 起票部門コード
	 * @param kihyouBumon 起票部門名
	 * @param bumonRoleId 起票部門ロールID
	 * @param bumonRoleName 起票部門ロール名
	 * @param loginUserId ユーザーID(本人)
	 * @param kijunbi 基準日
	 * @param shiwakeEdaNo 仕訳枝番号
	 */
	public void insertDefaultShouninRoute(String denpyouId, String denpyouKbn, BigDecimal kingaku, String sanshouDenpyouId,
			String kihyouUserId, String kihyouUserName, String kihyouBumonCd, String kihyouBumon, String bumonRoleId, String bumonRoleName,
			String loginUserId, Date kijunbi, String shiwakeEdaNo) {

		//------------------------------------
		//参照起票だとしても参照元がデフォルトのままならそれを継承する必要なし
		//------------------------------------
		if(! StringUtils.isEmpty(sanshouDenpyouId)) {
			GMap sanshouMoto = wfLogic.selectDenpyou(sanshouDenpyouId);
			if (sanshouMoto.get("shounin_route_henkou_flg").equals("0")) {
				sanshouDenpyouId = null;
			}
		}

		//------------------------------------
		//一回削除
		//------------------------------------
		deleteShouninRoute(denpyouId);
		routeLogic.deleteShouninRouteGougiOya(denpyouId, 0);
		routeLogic.deleteShouninRouteGougiKo(denpyouId, 0);

		//------------------------------------
		// 起票者を承認ルートに登録（枝番1）
		//------------------------------------
		routeLogic.insertShouninRoute(denpyouId, 1, kihyouUserId, kihyouUserName, kihyouBumonCd, kihyouBumon, bumonRoleId, bumonRoleName, "", "", "1", "0", null, loginUserId);
		String shinseiShorikengen = kihyouLogic.findDenpyouKanri(denpyouKbn).get("shinsei_shori_kengen_name").toString();
		if(shinseiShorikengen.isEmpty() == false){
			routeLogic.updateShouninRouteShorikengenName(denpyouId, 1, shinseiShorikengen);
		}

		//----------------------------------
		// 最終承認者（変更不可部分）を除いて登録
		//----------------------------------

		//通常は部門推奨ルートから
		if(StringUtils.isEmpty(sanshouDenpyouId)) {

			//起票者の所属部門から再帰的に親部門まで遡っていきながら、所属部門に紐付く部門推奨ルートを探す
			List<GMap> bumonSuishouRouteUserList = wfLogic.loadBumonSuishouRouteUser(denpyouKbn, kihyouBumonCd, kingaku, kihyouUserId, kijunbi, shiwakeEdaNo);

			// 部門推奨ルートを承認ルートへ登録
			int edano = 1;
			int gougiEdano = 0;
			int gougiEdaEdano = 0;
			int befRouteGougiEdano = 999;
			int curRouteGougiEdano = 0;
			long befGougiPatternNo = 999999999;
			long curGougiPatternNo = 0;
			for(GMap map : bumonSuishouRouteUserList){
				//一般ユーザーとしての承認ルート
				if(map.get("gougi_edano") == null){
					routeLogic.insertShouninRoute(
						denpyouId,
						++edano,
						(String)map.get("user_id"),
						(String)map.get("user_sei") + "　" + (String)map.get("user_mei"),
						(String)map.get("bumon_cd"),
						EteamCommon.connectBumonName(connection, (String)map.get("bumon_cd")),
						(String)map.get("bumon_role_id"),
						(String)map.get("bumon_role_name"),
						"", //gyoumu_role_id
						"", //gyoumu_role_name
						"0",//genzai_flg
						"0",//saishu_shounin_flg
						(Long)map.get("shounin_shori_kengen_no"),
						loginUserId);
					befRouteGougiEdano = 999;
					befGougiPatternNo = 999999999;
				//合議としての承認ルート
				}else{
					curRouteGougiEdano = (Integer)map.get("gougi_edano");
					curGougiPatternNo = (Long)map.get("gougi_pattern_no");

					//承認ルートテーブル登録
					if(curRouteGougiEdano < befRouteGougiEdano || (curRouteGougiEdano == befRouteGougiEdano && curGougiPatternNo != befGougiPatternNo)){
						routeLogic.insertShouninRoute(denpyouId, ++edano, "", "", "", "", "", "", "", "", "0", "0", null, loginUserId);
						gougiEdano = 0;
					}

					//合議枝番が変わっていたら承認ルート合議親テーブル登録
					if(curRouteGougiEdano != befRouteGougiEdano || (curRouteGougiEdano == befRouteGougiEdano && curGougiPatternNo != befGougiPatternNo)){
						routeLogic.insertShouninRouteGougiOya(denpyouId, edano, ++gougiEdano, (Long)map.get("gougi_pattern_no"), (String)map.get("gougi_name"), "0");//処理コードは0固定]
						gougiEdaEdano = 0;
					}

					//合議内データとみなして子登録
					routeLogic.insertShouninRouteGougiKo(denpyouId,
							 edano,
							 gougiEdano,
							 ++gougiEdaEdano,
							(String)map.get("user_id"),
							(String)map.get("user_sei") + "　" + (String)map.get("user_mei"),
							(String)map.get("bumon_cd"),
							EteamCommon.connectBumonName(connection, (String)map.get("bumon_cd")),
							(String)map.get("bumon_role_id"),
							(String)map.get("bumon_role_name"),
							 "0", //gougi_genzai_flg
							 (Long)map.get("shounin_shori_kengen_no"),
							 (String)map.get("shounin_ninzuu_cd"),
							 (Integer)map.get("shounin_ninzuu_hiritsu"),
							 "0", //syori_cd
							 0, //gouginai_group
							 null);
					befRouteGougiEdano = curRouteGougiEdano;
					befGougiPatternNo = curGougiPatternNo;
				}
			}

		// 参照起票でかつ参照元でルート変更フラグが立っていたら参照元の承認ルートから
		} else {
			List<GMap> shouninRouteList = wfLogic.selectShouninRoute(sanshouDenpyouId);
			for(GMap map : shouninRouteList) {

				// 部門フル名の設定 現在日時点の名称データを取得(業務ロールの場合はそのまま)
				if( isEmpty(map.get("gyoumu_role_id")) ){
					String bFull = EteamCommon.connectBumonName(connection, (String)(map.get("bumon_cd")) );
					if(bFull.length() == 0) {bFull = "(削除)";}
					map.put("bumon_full_name", bFull );
				}

				int edano = (int) map.get("edano");
				if(edano != 1 && !"1".equals(map.get("saishu_shounin_flg").toString())) {
					routeLogic.insertShouninRoute(
						denpyouId,
						(int)map.get("edano"),
						(String)map.get("user_id"),
						(String)map.get("user_full_name"),
						(String)map.get("bumon_cd"),
						(String)map.get("bumon_full_name"),
						(String)map.get("bumon_role_id"),
						(String)map.get("bumon_role_name"),
						(String)map.get("gyoumu_role_id"),
						(String)map.get("gyoumu_role_name"),
						"0",//genzai_flg
						"0",//saishu_shounin_flg
						(Long)map.get("shounin_shori_kengen_no"),
						loginUserId);
				}

			}
			//合議ルート親登録
			List<GMap> shouninRouteListGougiOya = wfLogic.selectShouninRouteGougiOya(sanshouDenpyouId);
			for(GMap map : shouninRouteListGougiOya) {
				routeLogic.insertShouninRouteGougiOya(
						denpyouId,
						(int)map.get("edano"),
						(int)map.get("gougi_edano"),
						(Long)map.get("gougi_pattern_no"),
						(String)map.get("gougi_name"),
						"0");//syori_cd
			}

			//合議ルート子登録
			List<GMap> shouninRouteListGougiKo = wfLogic.selectShouninRouteGougiKo(sanshouDenpyouId);
			for(GMap map : shouninRouteListGougiKo) {

				// 部門フル名の設定 現在日時点の名称データを取得
				String bFull = EteamCommon.connectBumonName(connection, (String)(map.get("bumon_cd")) );
				if(bFull.length() == 0) {bFull = "(削除)";}
				map.put("bumon_full_name", bFull );

				routeLogic.insertShouninRouteGougiKo(
						 denpyouId,
						 (int)map.get("edano"),
						 (int)map.get("gougi_edano"),
						 (int)map.get("gougi_edaedano"),
						 (String)map.get("user_id"),
						 (String)map.get("user_full_name"),
						 (String)map.get("bumon_cd"),
						 (String)map.get("bumon_full_name"),
						 (String)map.get("bumon_role_id"),
						 (String)map.get("bumon_role_name"),
						 "0",//gougi_genzai_flg
						 (Long)map.get("shounin_shori_kengen_no"),
						 map.get("shounin_ninzuu_cd").toString(),
						 (Integer)map.get("shounin_ninzuu_hiritsu"),
						 "0",//syori_cd
						 Integer.parseInt(map.get("gouginai_group").toString()),
						 null);
			}
			routeLogic.updateShouninRouteHenkouFlg(denpyouId, true);
		}

		//----------------------------------
		// 最終承認者を登録
		//----------------------------------
		GMap chuukiMongonMap = wfLogic.selectChuukiMongon(denpyouKbn);
		if(chuukiMongonMap != null) {
			List<GMap> saishuuShouninshaList = wfLogic.selectSaishuuSyouninRoute(denpyouKbn, (int) chuukiMongonMap.get("edano"));
			int edano = wfLogic.selectShouninRoute(denpyouId).size() + 1;
			for(GMap map : saishuuShouninshaList) {
				routeLogic.insertShouninRoute(
					denpyouId,
					edano,
					"", "", "", "", "", "",
					map.get("gyoumu_role_id").toString(),
					map.get("gyoumu_role_name").toString(),
					"0", //genzai_flg
					"1", //saishu_shounin_flg
					null,//shounin_shori_kengen_no
					loginUserId);
				String saishuuShouninShorikengen = map.get("saishuu_shounin_shori_kengen_name").toString();
				if(saishuuShouninShorikengen.isEmpty() == false){
					routeLogic.updateShouninRouteShorikengenName(denpyouId, edano, saishuuShouninShorikengen);
				}
				edano++;
			}
		}
	}

	//承認ルート関連
	/**
	 * 承認ルートのユーザー情報を更新
	 * @param denpyouId        伝票ID
	 * @param edano             枝番号
	 * @param bumonCd          部門コード
	 * @param bumonFullName   部門フル名
	 * @param bumonRoleId     部門ロールID
	 * @param bumonRoleName   部門ロール名
	 * @param loginUserId     ログインユーザーID
	 * @return 結果
	 */
	public int updateShouninRoute(String denpyouId, int edano, String bumonCd, String bumonFullName, String bumonRoleId, String bumonRoleName, String loginUserId) {
		final String sql = "UPDATE shounin_route "
						+  "SET    bumon_cd = ?, bumon_full_name = ?, bumon_role_id = ?, bumon_role_name = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  "WHERE  denpyou_id = ? AND edano = ? ";
		return connection.update(sql, bumonCd, bumonFullName, bumonRoleId, bumonRoleName, loginUserId, denpyouId, edano);
	}
	/**
	 * 伝票IDとユーザーIDをキーに「承認ルート」の現在フラグを更新する。
	 * @param denpyouId 伝票ID
	 * @param whereaAdd 追加更新条件
	 * @param genzaiFlg 現在フラグ
	 * @param loginUserId ログインユーザーID
	 * @return 更新対象枝番号
	 */
	protected Integer updateGenzaiFlg(String denpyouId, String whereaAdd, String genzaiFlg, String loginUserId) {
		final String sql = " UPDATE shounin_route "
						+  " SET genzai_flg = ?, koushin_user_id = ?, koushin_time = current_timestamp "
						+  " WHERE denpyou_id = ? " + whereaAdd
						+  " RETURNING edano";
		GMap record = connection.find(sql, genzaiFlg, loginUserId, denpyouId);
		return (null == record) ? null : (Integer)record.get("edano");
	}
	/**
	 * 承認ルート合議子の現在フラグを更新する
	 * @param denpyouId 伝票ID
	 * @param whereAdd 条件句
	 * @param flg gougi_genzai_flg
	 */
	protected void updateGenzaiFlgGougi(String denpyouId, String whereAdd, String flg) {
		String sql = "UPDATE shounin_route_gougi_ko SET gougi_genzai_flg=? WHERE denpyou_id=? " + whereAdd;
		connection.update(sql, flg, denpyouId);
	}
	/**
	 * 承認状況を承認ルートに紐付ける。
	 * @param denpyouId 伝票ID
	 * @param whereaAdd 追加更新条件
	 * @param joukyouEdano 承認状況枝番
	 * @param loginUserId ログインユーザーID
	 */
	protected void updateJoukyouEdano(String denpyouId, String whereaAdd, Integer joukyouEdano, String loginUserId){
		final String sql = " UPDATE shounin_route "
				+  " SET joukyou_edano = ?, koushin_user_id = ?, koushin_time = current_timestamp "
				+  " WHERE denpyou_id = ? " + whereaAdd;
		connection.update(sql, joukyouEdano, loginUserId, denpyouId);
	}
	/**
	 * 承認状況を承認ルート合議子に紐付ける。
	 * @param denpyouId 伝票ID
	 * @param whereaAdd 追加更新条件
	 * @param joukyouEdano 承認状況枝番
	 */
	protected void updateJoukyouEdanoGougi(String denpyouId, String whereaAdd, Integer joukyouEdano){
		final String sql = " UPDATE shounin_route_gougi_ko "
				+  " SET joukyou_edano = ? "
				+  " WHERE denpyou_id = ? " + whereaAdd;
		connection.update(sql, joukyouEdano, denpyouId);
	}

	/**
	 * 承認状況を追加する(承認ルート外)
	 * @param denpyou_id        伝票ID
	 * @param user              ユーザー
	 * @param joukyouCd てｓｔ
	 * @param joukyou           状況
	 * @param comment           コメント
	 * @return 枝番
	 */
	public int insertShouninJoukyou(String denpyou_id, User user, @SuppressWarnings("unused") String joukyouCd, String joukyou, String comment) {
		if(user.getGyoumuRoleId() == null){
			return insertShouninJoukyou(denpyou_id,
					user.getLoginUserId(), user.getDisplayUserNameHonnin(),
					"", "", "", "", "", "",
					"", joukyou, comment,
					user.getTourokuOrKoushinUserId(),
					null, null, null, null);
		}else{
			return insertShouninJoukyou(denpyou_id,
					user.getLoginUserId(), user.getDisplayUserNameHonnin(),
					"", "", "", "", user.getGyoumuRoleId(), user.getGyoumuRoleName(),
					"", joukyou, comment,
					user.getTourokuOrKoushinUserId(),
					null, null, null, null);
		}
	}
	//TODO　↑こいつに統合・・・呼ぶ側のハンドリングが増えるんで・・・
	/**
	 * 承認状況を追加する(承認ルート外)
	 * @param denpyou_id        伝票ID
	 * @param user              ユーザー
	 * @param joukyou           状況
	 * @param comment           コメント
	 * @return 枝番
	 */
	@Deprecated
	public int insertShouninJoukyouUserKengen(String denpyou_id, User user, String joukyou, String comment) {
		return insertShouninJoukyou(denpyou_id,
				user.getLoginUserId(), user.getDisplayUserNameHonnin(),
				"", "", "", "", "", "",
				"", joukyou, comment,
				user.getTourokuOrKoushinUserId(),
				null, null, null, null);
	}
	/**
	 * 承認状況を追加する(承認ルート外)
	 * @param denpyou_id        伝票ID
	 * @param user              ユーザー
	 * @param joukyou           状況
	 * @param comment           コメント
	 * @return 枝番
	 */
	@Deprecated
	public int insertShouninJoukyouRoleKengen(String denpyou_id, User user, String joukyou, String comment) {
		return insertShouninJoukyou(denpyou_id,
				user.getLoginUserId(), user.getDisplayUserNameHonnin(),
				"", "", "", "", user.getGyoumuRoleId(), user.getGyoumuRoleName(),
				"", joukyou, comment,
				user.getTourokuOrKoushinUserId(),
				null, null, null, null);
	}
	/**
	 * 承認状況を追加する（承認解除のみ）
	 * @param denpyouId 伝票ID
	 * @param user ユーザー
	 * @param gyoumuRoleId 業務ロールID
	 * @param gyoumuRoleName 業務ロール名
	 * @param joukyouCd 状況コード
	 * @param joukyou 状況文言(nullなら内部コードから参照)
	 * @param comment コメント
	 * @param edano 枝番
	 * @return 枝番
	 */
	public int insertShouninJoukyouShouninKaijo(String denpyouId, User user, String gyoumuRoleId, String gyoumuRoleName, String joukyouCd, String joukyou, String comment, int edano) {
		if(joukyou == null) joukyou = systemLogic.findNaibuCdName("joukyou", joukyouCd);
		int ret = insertShouninJoukyou(denpyouId,
				user.getSeigyoUserId(), user.getDisplayUserName(),
				"", "", "", "",
				gyoumuRoleId, gyoumuRoleName,
				joukyouCd, joukyou,
				n2b(comment),
				user.getTourokuOrKoushinUserId(),
				edano, null, null,
				null);
		updateShouninJoukyouShorikenge(denpyouId, edano, "1", gyoumuRoleId);
		return ret;
	}
	/**
	 * 承認状況を追加する(承認ルート内)
	 * @param denpyouId 伝票ID
	 * @param user ユーザー
	 * @param procRoute 承認ルートレコード
	 * @param joukyouCd 状況コード
	 * @param joukyou 状況文言(nullなら内部コードから参照)
	 * @param comment コメント
	 * @return 枝番
	 */
	public int insertShouninJoukyou(String denpyouId, User user, GMap procRoute, String joukyouCd, String joukyou, String comment) {
		if(joukyou == null) joukyou = systemLogic.findNaibuCdName("joukyou", joukyouCd);
		int ret = insertShouninJoukyou(denpyouId,
				user.getSeigyoUserId(), user.getDisplayUserName(),
				n2b(procRoute.get("bumon_cd")), n2b(procRoute.get("bumon_full_name")), n2b(procRoute.get("bumon_role_id")), n2b(procRoute.get("bumon_role_name")),
				n2b(procRoute.get("gyoumu_role_id")), n2b(procRoute.get("gyoumu_role_name")),
				joukyouCd, joukyou,
				n2b(comment),
				user.getTourokuOrKoushinUserId(),
				(Integer)procRoute.get("edano"), (Integer)procRoute.get("gougi_edano"), (Integer)procRoute.get("gougi_edaedano"),
				(Long)procRoute.get("shounin_shori_kengen_no"));
		updateShouninJoukyouShorikenge(denpyouId, (Integer)procRoute.get("edano"), (String)procRoute.get("saishu_shounin_flg"), n2b(procRoute.get("gyoumu_role_id")));
		return ret;
	}

	/**
	 * 承認状況を追加する(承認ルート内)
	 * @param denpyouId 伝票ID
	 * @param procRoute 承認ルートレコード
	 * @param joukyouCd 状況コード
	 * @param joukyou 状況文言
	 * @param comment コメント
	 * @param loginUserId ログインユーザーID
	 * @return 枝番
	 */
	public int insertShouninJoukyou(String denpyouId, GMap procRoute, String joukyouCd, String joukyou, String comment, String loginUserId) {
		int ret = insertShouninJoukyou(denpyouId,
				n2b(procRoute.get("user_id")), n2b(procRoute.get("user_full_name")),
				n2b(procRoute.get("bumon_cd")), n2b(procRoute.get("bumon_full_name")), n2b(procRoute.get("bumon_role_id")), n2b(procRoute.get("bumon_role_name")),
				n2b(procRoute.get("gyoumu_role_id")), n2b(procRoute.get("gyoumu_role_name")),
				joukyouCd, joukyou,
				n2b(comment),
				loginUserId,
				(Integer)procRoute.get("edano"), (Integer)procRoute.get("gougi_edano"), (Integer)procRoute.get("gougi_edaedano"),
				(Long)procRoute.get("shounin_shori_kengen_no"));
		updateShouninJoukyouShorikenge(denpyouId, (Integer)procRoute.get("edano"), (String)procRoute.get("saishu_shounin_flg"), n2b(procRoute.get("gyoumu_role_id")));
		return ret;
	}
	/**
	 * 承認状況を追加する(承認ルート内)
	 * @param denpyouId 伝票ID
	 * @param joukyouCd 状況コード
	 * @param joukyou 状況文言
	 * @param comment コメント
	 * @return 枝番
	 */
	public int insertShouninJoukyouBatch(String denpyouId, String joukyouCd, String joukyou, String comment){
		return insertShouninJoukyou(
				denpyouId,
				"", "",
				"", "", "", "",
				"", "",
				joukyouCd, joukyou, comment,
				"batch",
				null, null, null, null);
	}
	/**
	 * 承認状況を追加する
	 * @param denpyou_id        伝票ID
	 * @param user_id           ユーザーID
	 * @param user_full_name    ユーザーフル名
	 * @param bumon_cd          部門コード
	 * @param bumon_full_name   部門フル名
	 * @param bumon_role_id     部門ロールID
	 * @param bumon_role_name   部門ロール名
	 * @param gyoumu_role_id    業務ロールID
	 * @param gyoumu_role_name  業務ロール名
	 * @param joukyouCd         状況CD
	 * @param joukyou           状況
	 * @param comment           コメント
	 * @param userId ログインユーザーID
	 * @param routeEdano 承認ルート枝番号
	 * @param routeGougiEdano 承認ルート合議枝番号
	 * @param routeGougiEdaedano 承認ルート合議枝々番号
	 * @param shouninShoriKengenCd 承認処理権限コード
	 * @return 枝番
	 */
	protected int insertShouninJoukyou(String denpyou_id, String user_id, String user_full_name, String bumon_cd, String bumon_full_name, String bumon_role_id, String bumon_role_name, String gyoumu_role_id, String gyoumu_role_name, String joukyouCd, String joukyou, String comment, String userId, Integer routeEdano, Integer routeGougiEdano, Integer routeGougiEdaedano, Long shouninShoriKengenCd) {
		final String sql = "INSERT INTO shounin_joukyou VALUES"
				+ "(?, (SELECT COALESCE(MAX(edano + 1), 1) FROM shounin_joukyou WHERE denpyou_id = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp) RETURNING edano";
		return (int)connection.find(sql, denpyou_id, denpyou_id, user_id, user_full_name, bumon_cd, bumon_full_name, bumon_role_id, bumon_role_name, gyoumu_role_id, gyoumu_role_name, joukyouCd, joukyou, comment, routeEdano, routeGougiEdano, routeGougiEdaedano, shouninShoriKengenCd, wfLogic.findShouninShoriKengenName(shouninShoriKengenCd), userId, userId).get("edano");
	}

	/**
	 * 申請者または最終承認者の承認処理権限を変更する。
	 * @param denpyouId 伝票ID
	 * @param routeEdano 承認ルート枝番
	 * @param saishuShouninFlg 最終承認フラグ
	 * @param gyoumuRoleId 業務ロールID
	 */
	protected void updateShouninJoukyouShorikenge(String denpyouId, int routeEdano, String saishuShouninFlg, String gyoumuRoleId) {
		final String sql = "UPDATE shounin_joukyou SET shounin_shori_kengen_name = ? WHERE denpyou_id = ? AND edano = (SELECT MAX(edano) FROM shounin_joukyou WHERE denpyou_id = ?)";
		String denpyouKbn = denpyouId.substring(7, 11);
		String shorikengen = null;

		// 処理権限名を取得
		if(1==routeEdano){
			// 申請者の場合
			shorikengen = kihyouLogic.findDenpyouKanri(denpyouKbn).get("shinsei_shori_kengen_name").toString();
		}else if("1".equals(saishuShouninFlg)){
			// 業務ロール（最終承認者）の場合
			shorikengen = wfLogic.findSaiSaishuuShouninShoriKengen(denpyouKbn, gyoumuRoleId);
		}

		// 更新
		if(null != shorikengen && shorikengen.isEmpty() == false){
			connection.update(sql, shorikengen, denpyouId, denpyouId);
		}
	}
	
	//202207　追加
	/**
	 * 最終承認の一つ前に承認した承認者を取得<br>※現在使用非推奨、承認解除の仕様に従ってSQL文を書き直してから使用してください
	 * @param denpyouId 伝票ID
	 * @param gyoumuRoleId 最終承認者の業務ロールID
	 * @return ユーザーID
	 */
	@Deprecated
	public GMap findUserOneBeforeSaishushounin(String denpyouId,String gyoumuRoleId) {
		//取戻しは起票者まで戻る
		final String torimodoshiSql = "select * from shounin_joukyou js where js.denpyou_id = ? and js.joukyou_cd = '2' ";
		List<GMap> result =  connection.load(torimodoshiSql,denpyouId);
		
		String toriSql = " ";
		if(result.size() > 0) {
			toriSql = " and msj.edano > (select MAX(tori.edano) from shounin_joukyou tori where tori.denpyou_id = '"+ denpyouId +"' and tori.joukyou_cd = '2') ";
		}
		
		final String sql = "select * from shounin_joukyou js "
				+ "where js.denpyou_id = ? "
				+ "and js.edano = "
				+ "( "
				+ " select MAX(msj.edano) from shounin_joukyou msj "
				+ " where msj.denpyou_id = ?  "
				+ " and msj.joukyou_cd = '4' and msj.joukyou != '' and msj.gyoumu_role_id != ? "
				+ " and msj.edano < "
				+ " (select MAX(mmsj.edano) from shounin_joukyou mmsj "
				+ " where mmsj.denpyou_id = ? and mmsj.gyoumu_role_id = ? "
				+ " )  "
				+ " and msj.edano not in  "
				+ " (select joui.edano from shounin_joukyou joui where joui.denpyou_id = ? "
				+ " and joui.edano <  "
				+ " (select MAX(sashimodoshi.edano) from shounin_joukyou sashimodoshi where sashimodoshi.denpyou_id = ? and sashimodoshi.joukyou_cd = '6') "
				+ " and joui.joukyou_cd = '4' and joui.joukyou != '' "
				+ " and (joui.user_id in "
				+ " (select jouii.user_id from shounin_joukyou jouii where jouii.denpyou_id = ? and jouii.joukyou_cd = '4' and jouii.joukyou = '' and jouii.edano > (select MAX(sashimodoshi.edano) from shounin_joukyou sashimodoshi where sashimodoshi.denpyou_id = ? and sashimodoshi.joukyou_cd = '6') ) "
				+ " or joui.gyoumu_role_id in "
				+ " (select jouiii.gyoumu_role_id from shounin_joukyou jouiii where jouiii.denpyou_id = ? and jouiii.joukyou_cd = '4' and jouiii.joukyou = '' and jouiii.edano > (select MAX(sashimodoshi.edano) from shounin_joukyou sashimodoshi where sashimodoshi.denpyou_id = ? and sashimodoshi.joukyou_cd = '6')) ) "
				+ " )"
				+ toriSql
				+ ")";
		
		return connection.find(sql,denpyouId,denpyouId,gyoumuRoleId,denpyouId,gyoumuRoleId,denpyouId,denpyouId,denpyouId,denpyouId,denpyouId,denpyouId);
	}
	/**
	 * 承認者のshounin_routeテーブルでの枝番
	 * @param denpyouId 伝票ID
	 * @param userId ユーザーID
	 * @return 枝番(shounin_route)
	 */
	public int findShouninshaEdano(String denpyouId, String userId) {
		final String sql = "select edano from shounin_route where denpyou_id = ? and user_id = ? ";
		GMap result = connection.find(sql,denpyouId,userId);
		return result == null ? 1 : result.get("edano");
	}
	/**
	 * 承認者のshounin_routeテーブルでの枝番（業務ロール）
	 * @param denpyouId 伝票ID
	 * @param roleId 業務ロールID
	 * @return 枝番(shounin_route)
	 */
	public int findShouninshaEdanoForGyoumu(String denpyouId, String roleId) {
		final String sql = "select edano from shounin_route where denpyou_id = ? and gyoumu_role_id = ? ";
		GMap result = connection.find(sql,denpyouId,roleId);
		return result == null ? 1 : result.get("edano");
	}
	/**
	 * 承認ルートの最終承認ユーザー・業務ロールを取得
	 * @param denpyouId 伝票ID
	 * @return ルート内最終承認のレコード
	 */
	public GMap findSaishuShouninSha(String denpyouId) {
		final String sql = "SELECT * FROM shounin_route sr WHERE sr.denpyou_id = ? and sr.edano = (SELECT max(ssr.edano) FROM shounin_route ssr WHERE ssr.denpyou_id = ?)";
		return connection.find(sql,denpyouId, denpyouId);
	}
	
	/**
	 * 承認ルートの最終承認ユーザー・業務ロールを取得
	 * @param denpyouId 伝票ID
	 * @return ルート内最終承認のレコード
	 */
	public List<GMap> findSaishuShouninShaFlg(String denpyouId) {
		final String sql = "SELECT * FROM shounin_route sr WHERE sr.denpyou_id = ? and sr.saishu_shounin_flg = '1'";
		return connection.load(sql,denpyouId);
	}

	/**
	 * 指定伝票のマル秘フラグを更新。
	 * @param denpyouId 伝票ID
	 * @param userId 更新ユーザーID
	 * @param maruhiFlg マル秘フラグ
	 */
	public void updateMaruhiFlg(String denpyouId, String userId, String maruhiFlg) {
		final String sql =
					"UPDATE denpyou SET "
				+ "  maruhi_flg = ? , "
				+ "  koushin_user_id = ?,"
				+ "  koushin_time = current_timestamp "
				+ "WHERE "
				+ "  denpyou_id = ? ";
		connection.update(sql, maruhiFlg, userId, denpyouId);
	}

	/**
	 * 指定伝票のマル秘フラグを取得。
	 * @param denpyouId 伝票ID
	 * @return マル秘フラグ設定されていればtrue
	 */
	public boolean isMaruhiFlg(String denpyouId) {
		final String sql =
					"SELECT denpyou_id,maruhi_flg FROM denpyou "
				+ "WHERE "
				+ "  denpyou_id = ? ";
		GMap tmpMap = connection.find(sql, denpyouId);
		return "1".equals(tmpMap.get("maruhi_flg"));
	}

	/**
	 * 申請内容を取得する
	 *
	 * 戻り値の構造はユーザー定義届書と会計伝票で異なる。
	 *
	 * [ユーザー定義届書]
	 * List
	 * ├Map
	 * │├item_name:meisai01
	 * │├label_name:項目和名１
	 * │└value1:入力文言１
	 * └Map
	 *   ├item_name:meisai02
	 *   ├label_name:項目和名２
	 *   └value1:入力文言２
	 *
	 * [会計伝票]
	 * Map
	 * ├kamoku_cd:0001
	 * └kamoku_edaban:000002
	 *
	 * 戻り値のListの構造はユーザー定義届書と会計伝票で異なる。
	 * ・ユーザー定義届書		：項目単位のMapをListにしたもの
	 * ・会計伝票				：単にList[0]が申請テーブルの1レコード
	 *
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return 申請内容
	 */
	public Object loadShinsei(String denpyouKbn, String denpyouId) {

		// テーブル名
		String tableName = findTableName(denpyouKbn, true);
		if (tableName.length() == 0)
		{
			return null;
		}

		if(isKaniTodoke(denpyouKbn)){
			String sql =  " SELECT L.item_name"
				+ "       ,N.label_name"
				+ "       ,L.value1"
				+ "       ,COALESCE("
				+ "        CASE WHEN M.buhin_type = 'radio' OR M.buhin_type = 'pulldown'"
				+ "             THEN (SELECT text "
				+ "                     FROM kani_todoke_list_ko"
				+ "                    WHERE denpyou_kbn = M.denpyou_kbn "
				+ "                      AND version     = M.version "
				+ "                      AND item_name   = M.item_name "
				+ "                      AND value       = L.value1) "
				+ "             WHEN M.buhin_type = 'checkbox' AND L.value1 = '1' THEN 'あり'"
				+ "             WHEN M.buhin_type = 'checkbox' AND L.value1 = '0' THEN 'なし'"
				+ "             WHEN M.buhin_type = 'master'                      THEN value2"
				+ "        ELSE L.value1 END,'') AS disp_value"
				+ "   FROM kani_todoke L"
				+ "  INNER JOIN kani_todoke_koumoku M"
				+ "     ON L.denpyou_kbn = M.denpyou_kbn"
				+ "    AND L.version     = M.version"
				+ "    AND L.item_name   = M.item_name"
				+ "  INNER JOIN (select denpyou_kbn, version, item_name, label_name from kani_todoke_text     union all"
				+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_textarea union all"
				+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_list_oya union all"
				+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_checkbox union all"
				+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_master "
				+ "             ) N"
				+ "     ON L.denpyou_kbn = N.denpyou_kbn"
				+ "    AND L.version     = N.version"
				+ "    AND L.item_name   = N.item_name"
				+ "  WHERE L.denpyou_id  = ?"
				+ "  ORDER BY M.hyouji_jun";
			return connection.load(sql, denpyouId);
		} else {
			String sql = "SELECT * FROM " + tableName + " WHERE denpyou_id = ?";
			GMap shinseiList = connection.find(sql, denpyouId);
			// 軽減税率区分のカラムが存在する場合、値を取得
			if(shinseiList.containsKey("keigen_zeiritsu_kbn")) {
				String keigenZeiristuKbn = shinseiList.getString("keigen_zeiritsu_kbn");
				// 取得した値が"1"の時、消費税率の値の前に"*"を追加
				if(keigenZeiristuKbn.equals(EteamConst.keigenZeiritsuKbn.KEIGEN)) {
					shinseiList.put("zeiritsu", EteamConst.keigenZeiritsuKbn.KEIGEN_MARK + shinseiList.get("zeiritsu"));
				}
			}
			return shinseiList;
		}
	}

	/**
	 * 明細を取得する
	 *
	 * 戻り値の構造はユーザー定義届書と会計伝票で異なる。
	 *
	 * [ユーザー定義届書]
	 * Map
	 * ├1:List
	 * │  ├Map
	 * │  │├item_name:meisai01
	 * │  │├label_name:項目和名１
	 * │  │└value1:入力文言１
	 * │  └Map
	 * │    ├item_name:meisai02
	 * │    ├label_name:項目和名２
	 * │    └value1:入力文言２
	 * └2:List
	 *      略
	 *
	 * [会計伝票]
	 * Map
	 * ├1:Map
	 * │  ├kamoku_cd:0001
	 * │  └kamoku_edaban:000002
	 * └2:Map
	 *      略
	 *
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return 明細
	 */
	public Map<Integer, ?> loadMeisai(String denpyouKbn, String denpyouId) {
		String tableName = findTableName(denpyouKbn, false);
		if (tableName.length() == 0)
		{
			return null;
		}

		if(isKaniTodoke(denpyouKbn)){
			//明細×項目単位に取得
			String sql = " SELECT O.denpyou_id"
					+ "       ,O.denpyou_edano"
					+ "       ,O.item_name"
					+ "       ,N.label_name"
					+ "       ,O.value1"
					+ "       ,CASE WHEN M.buhin_type = 'radio' OR M.buhin_type = 'pulldown'"
					+ "             THEN (SELECT text "
					+ "                     FROM kani_todoke_list_ko"
					+ "                    WHERE denpyou_kbn = M.denpyou_kbn "
					+ "                      AND version     = M.version "
					+ "                      AND item_name   = M.item_name "
					+ "                      AND value       = O.value1) "
					+ "             WHEN M.buhin_type = 'checkbox' AND O.value1 = '1' THEN 'あり'"
					+ "             WHEN M.buhin_type = 'checkbox' AND O.value1 = '0' THEN 'なし'"
					+ "             WHEN M.buhin_type = 'master'                      THEN value2"
					+ "        ELSE O.value1 END AS disp_value"
					+ "   FROM (SELECT DISTINCT denpyou_id, denpyou_kbn, version FROM kani_todoke) L"
					+ "  INNER JOIN kani_todoke_meisai O"
					+ "     ON L.denpyou_id  = O.denpyou_id"
					+ "  INNER JOIN kani_todoke_koumoku M"
					+ "     ON L.denpyou_kbn = M.denpyou_kbn"
					+ "    AND L.version     = M.version"
					+ "    AND O.item_name   = M.item_name"
					+ "  INNER JOIN (select denpyou_kbn, version, item_name, label_name from kani_todoke_text     union all"
					+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_textarea union all"
					+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_list_oya union all"
					+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_checkbox union all"
					+ "              select denpyou_kbn, version, item_name, label_name from kani_todoke_master "
					+ "             ) N"
					+ "     ON L.denpyou_kbn = N.denpyou_kbn"
					+ "    AND L.version     = N.version"
					+ "    AND O.item_name   = N.item_name"
					+ "  WHERE L.denpyou_id  = ?"
					+ "  ORDER BY O.denpyou_edano, M.hyouji_jun";
			List<GMap> itemList = connection.load(sql, denpyouId);
			//項目単位になっているListを明細単位に分ける
			Map<Integer, List<GMap>> ret = new HashMap<>();
			for (GMap item : itemList) {
				Integer denpyouEdano = (Integer)item.get("denpyou_edano");
				if(! ret.containsKey(denpyouEdano)) {
					ret.put(denpyouEdano, new ArrayList<GMap>());
				}
				List<GMap> meisai = ret.get(denpyouEdano);
				meisai.add(item);
			}
			return ret;
		} else {
			//明細単位に取得
			String[] table = tableName.split(",");
			List<GMap> meisaiList = new ArrayList<GMap>();
			for (int i = 0; i < table.length; i++ ) {
				String sql = "SELECT * FROM " + table[i] + " WHERE denpyou_id = ? ";
				if (table[i].equals("kaigai_ryohi_karibarai_meisai") || table[i].equals("kaigai_ryohiseisan_meisai")) {
					sql += " ORDER BY denpyou_edano, kaigai_flg ";
				}else {
					sql += " ORDER BY denpyou_edano ";
				}
				meisaiList.addAll(connection.load(sql, denpyouId));
			}

			// 明細に消費税率が含まれる場合は表示用に加工する
			meisaiList.stream().forEach(m -> {
				if(m.containsKey("keigen_zeiritsu_kbn")){
					if(EteamConst.keigenZeiritsuKbn.KEIGEN.equals(m.get("keigen_zeiritsu_kbn"))) {
						m.put("zeiritsu", EteamConst.keigenZeiritsuKbn.KEIGEN_MARK + m.get("zeiritsu").toString());
					}
				}
			});

			//Map化
			Map<Integer, GMap> ret = new HashMap<>();
			int denpyouEdano = 1;
			for (GMap meisai : meisaiList) {
				ret.put(denpyouEdano++, meisai);
			}
			return ret;
		}
	}

	/**
	 * 申請データのBefore-After比較を行い、差分を表す文字列を返す。
	 * @param denpyouKbn        伝票区分
	 * @param shinseiBf         申請内容(更新前)
	 * @param shinseiAf         申請内容(更新後)
	 * @param meisaiBf          明細(更新前)
	 * @param meisaiAf          明細(更新後)
	 * @param tenpuFileListBf   添付ファイル(更新前)
	 * @param tenpuFileListAf   添付ファイル(更新後)
	 * @param ebunshoDataListBf e文書データ（更新前）
	 * @param ebunshoDataListAf e文書データ（更新後）
	 * @param ringiBf           稟議金額関連情報（更新前）
	 * @param ringiAf           稟議金額関連情報（更新後）
	 * @return                  更新差分
	 */
	@SuppressWarnings("unchecked")
	public String makeDiff(String denpyouKbn,
			Object shinseiBf, Map<Integer, ?> meisaiBf, List<GMap> tenpuFileListBf, List<GMap> ebunshoDataListBf, GMap ringiBf,
			Object shinseiAf, Map<Integer, ?> meisaiAf, List<GMap> tenpuFileListAf, List<GMap> ebunshoDataListAf, GMap ringiAf) {
		StringBuilder sbRtn = new StringBuilder();

		//稟議金額超過コメントの比較
		sbRtn.append(diffRingi(ringiBf, ringiAf));

		//ユーザー定義届書の申請内容＋明細比較
		if(isKaniTodoke(denpyouKbn)){
			sbRtn.append(diffKaniShinsei((List<GMap>)shinseiBf, (List<GMap>)shinseiAf));
			sbRtn.append(diffKaniMeisai((Map<Integer, List<GMap>>)meisaiBf, (Map<Integer, List<GMap>>)meisaiAf));
		//会計伝票の申請内容＋明細比較
		} else {
			sbRtn.append(diffKaikeiShinsei(denpyouKbn, (GMap)shinseiBf, (GMap)shinseiAf));
			sbRtn.append(diffKaikeiMeisai(denpyouKbn, (Map<Integer, GMap>)meisaiBf, (Map<Integer, GMap>)meisaiAf));
		}

		//添付ファイルの比較
		sbRtn.append(diffFile(tenpuFileListBf, tenpuFileListAf, ebunshoDataListBf, ebunshoDataListAf));

		return sbRtn.toString();
	}

	/**
	 * 申請データ内で支払日・計上日のBefore-After比較を行い、差分を表す文字列を返す。
	 * @param denpyouKbn 伝票区分
	 * @param shinseiBf 申請内容(更新前)
	 * @param shinseiAf 申請内容(更新後)
	 * @return 更新差分
	 */
	@SuppressWarnings("unchecked")
	public String makeDiffShiharaibi(String denpyouKbn, Object shinseiBf, Object shinseiAf) {
		StringBuilder sbRtn = new StringBuilder();

		//支払日・計上日分の承認状況は分割して登録
		if( !(isKaniTodoke(denpyouKbn)) && shinseiBf != null && shinseiAf != null ){
			String tableName = findTableName(denpyouKbn, true);
			Map<String, DenpyouItemInfo> mapDenpyouItemInfo = denpyouItemInfoLogic.getDenpyouItemInfo(tableName);

			String befShiharai = ((GMap)shinseiBf).get(ITEM_SHIHARAIBI) != null ? (String)((GMap)shinseiBf).get(ITEM_SHIHARAIBI).toString() : "";
			String befKeijou   = ((GMap)shinseiBf).get(ITEM_KEIJOUBI)   != null ? (String)((GMap)shinseiBf).get(ITEM_KEIJOUBI).toString()   : "";
			String aftShiharai = ((GMap)shinseiAf).get(ITEM_SHIHARAIBI) != null ? (String)((GMap)shinseiAf).get(ITEM_SHIHARAIBI).toString() : "";
			String aftKeijou   = ((GMap)shinseiAf).get(ITEM_KEIJOUBI)   != null ? (String)((GMap)shinseiAf).get(ITEM_KEIJOUBI).toString()   : "";

			//支払日・計上日に差分があれば表示用文言として返却
			if( !(befShiharai.equals(aftShiharai)) || !(befKeijou.equals(aftKeijou)) ){
				sbRtn.append( "■申請内容").append(BR);
				//計上日差分
				if( !(befKeijou.equals(aftKeijou)) ){
					DenpyouItemInfo denpyouItemInfo = mapDenpyouItemInfo.get(ITEM_KEIJOUBI);
					String lblKeijou = denpyouItemInfo.getName();
					befKeijou = denpyouItemInfo.getDispVal(befKeijou);
					aftKeijou = denpyouItemInfo.getDispVal(aftKeijou);
					sbRtn.append(lblKeijou + "：" + befKeijou + " ⇒ " + aftKeijou).append(BR);
				}
				//支払日差分
				if( !(befShiharai.equals(aftShiharai)) ){
					DenpyouItemInfo denpyouItemInfo = mapDenpyouItemInfo.get(ITEM_SHIHARAIBI);
					String lblShiharai = denpyouItemInfo.getName();
					befShiharai = denpyouItemInfo.getDispVal(befShiharai);
					aftShiharai = denpyouItemInfo.getDispVal(aftShiharai);
					sbRtn.append(lblShiharai + "：" + befShiharai + " ⇒ " + aftShiharai).append(BR);
				}
			}
		}

		return sbRtn.toString();
	}

	/**
	 * ユーザー定義届書の本体の前後比較
	 * @param itemListBf 変更前（項目のリスト）
	 * @param itemListAf 変更後（項目のリスト）
	 * @return 差分表記文言
	 */
	protected String diffKaniShinsei(List<GMap> itemListBf, List<GMap> itemListAf) {
		if (itemListBf == null || itemListAf == null)
		{
			return "";
		}

		//本体データの比較、差分あれば文言作る
		String diff = diffKaniCommon(itemListBf, itemListAf);
		return (diff.isEmpty()) ? "" :  "■申請内容" + BR + diff;
	}

	/**
	 * ユーザー定義届書の明細の前後比較
	 * @param itemListMapBf 変更前（明細単位、項目のリスト）
	 * @param itemListMapAf 変更後（明細単位、項目のリスト）
	 * @return 差分表記文言
	 */
	protected String diffKaniMeisai(Map<Integer, List<GMap>> itemListMapBf, Map<Integer, List<GMap>> itemListMapAf) {
		if (itemListMapBf == null || itemListMapAf == null)
		{
			return "";
		}

		StringBuilder ret = new StringBuilder();

		//前後サイズが違うなら以下コメント
		if (itemListMapBf.size() != itemListMapAf.size()) {
			ret.append("■明細").append(BR);
			if (itemListMapBf.size() < itemListMapAf.size()) {
				ret.append("明細行を追加しました。").append(BR);
			} else {
				ret.append("明細行を削除しました。").append(BR);
			}
			return ret.toString();
		}

		//明細単位にデータの比較、差分あれば文言作る
		for(Integer denpyouEdano = 1;  denpyouEdano <= itemListMapBf.size(); denpyouEdano ++) {
			List<GMap> itemListBf = itemListMapBf.get(denpyouEdano);
			List<GMap> itemListAf = itemListMapAf.get(denpyouEdano);
			String diff = diffKaniCommon(itemListBf, itemListAf);
			if (! diff.isEmpty()) ret.append("■明細#").append(denpyouEdano).append(BR).append(diff);
		}
		return ret.toString();
	}

	/**
	 * ユーザー定義届書の本体/明細の前後比較共通
	 * @param itemListBf 変更前（項目のリスト）
	 * @param itemListAf 変更後（項目のリスト）
	 * @return 差分表記文言
	 */
	protected String diffKaniCommon(List<GMap> itemListBf, List<GMap> itemListAf) {
		StringBuilder sb = new StringBuilder();

		//変更前項目単位
		for (GMap itemBf : itemListBf) {
			//変更前項目情報
			String itemNameBf = itemBf.get("item_name").toString();
			String labelNameBf = itemBf.get("label_name").toString();
			String strBfVal = n2b(itemBf.get("value1"));
			String strBfDispVal = n2b(itemBf.get("disp_value"));

			//変更後項目単位
			for (GMap itemAf : itemListAf) {
				//変更後項目情報
				String itemNameAf = itemAf.get("item_name").toString();
				String strAfVal = n2b(itemAf.get("value1"));
				String strAfDispVal = n2b(itemAf.get("disp_value"));

				//同じ項目について値比較
				if(itemNameBf.equals(itemNameAf)) {
					if(!strBfVal.equals(strAfVal)) {
						sb.append(labelNameBf + "：" + strBfDispVal + " ⇒ " + strAfDispVal + BR);
					}
					break;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 会計伝票の本体の前後比較
	 * @param denpyouKbn 伝票区分
	 * @param meisaiBf 変更前（項目のマップ）
	 * @param meisaiAf 変更後（項目のマップ）
	 * @return 差分表記文言
	 */
	protected String diffKaikeiShinsei(String denpyouKbn, GMap meisaiBf, GMap meisaiAf) {
		if (meisaiBf == null || meisaiAf == null)
		{
			return "";
		}

		String schemaName = EteamCommon.getContextSchemaName();
		String tableName = findTableName(denpyouKbn, true);

		// 伝票項目名取得
		 Map<String, DenpyouItemInfo> mapDenpyouItemInfo = denpyouItemInfoLogic.getDenpyouItemInfo(tableName);

		// カラムメタデータ取得
		List<String> columnList = dataAccessCategoryLogic.loadTableColumnNameStr(tableName, schemaName);

		//本体データの比較、差分あれば文言作る
		String diff = diffKaikeiCommon(denpyouKbn, meisaiBf, meisaiAf, mapDenpyouItemInfo, columnList);
		return (diff.isEmpty()) ? "" :  "■申請内容" + BR + diff;
	}

	/**
	 * 会計伝票の明細の前後比較
	 * @param denpyouKbn 伝票区分
	 * @param meisaiMapBf 変更前（明細単位、項目のマップ）
	 * @param meisaiMapAf 変更後（項目のマップ）
	 * @return 差分表記文言
	 */
	protected String diffKaikeiMeisai(String denpyouKbn, Map<Integer, GMap> meisaiMapBf, Map<Integer, GMap> meisaiMapAf) {
		if (meisaiMapBf == null || meisaiMapAf == null)
		{
			return "";
		}

		String schemaName = EteamCommon.getContextSchemaName();
		String tableNameStr = findTableName(denpyouKbn, false);
		StringBuilder ret = new StringBuilder();

		String[] tableName = tableNameStr.split(",");
		Map<String, DenpyouItemInfo> mapDenpyouItemInfo = new HashMap<String, DenpyouItemInfo>();
		List<String> columnList = new ArrayList<String>();

		for (int i = 0;  i < tableName.length; i++) {
			// 伝票項目名取得
			mapDenpyouItemInfo.putAll(denpyouItemInfoLogic.getDenpyouItemInfo(tableName[i]));
			// カラムメタデータ取得
			columnList.addAll(dataAccessCategoryLogic.loadTableColumnNameStr(tableName[i], schemaName));
		}
		//出張旅費精算等で２つ以上の明細テーブルでのカラム名が重複を除く
		columnList = new ArrayList<String>(new LinkedHashSet<String>(columnList));

		//前後サイズが違うなら以下コメント
		if (meisaiMapBf.size() != meisaiMapAf.size()) {
			ret.append("■明細").append(BR);
			if (meisaiMapBf.size() < meisaiMapAf.size()) {
				ret.append("明細を追加しました。").append(BR);
			} else {
				ret.append("明細を削除しました。").append(BR);
			}
			return ret.toString();
		}

		//明細単位にデータの比較、差分あれば文言作る
		for(Integer denpyouEdano = 1;  denpyouEdano <= meisaiMapBf.size(); denpyouEdano ++) {
			GMap meisaiBf = meisaiMapBf.get(denpyouEdano);
			GMap meisaiAf = meisaiMapAf.get(denpyouEdano);
			String diff = diffKaikeiCommon(denpyouKbn, meisaiBf, meisaiAf, mapDenpyouItemInfo, columnList);
			if (! diff.isEmpty()) ret.append("■明細#").append(denpyouEdano).append(BR).append(diff);
		}
		return ret.toString();
	}

	/**
	 * 会計伝票の本体/明細の前後比較共通
	 * @param denpyouKbn 伝票区分
	 * @param meisaiBf 変更前（項目のマップ）
	 * @param meisaiAf 変更後（項目マップ）
	 * @param mapDenpyouItemInfo 項目名マップ
	 * @param columnList カラムデータリスト
	 * @return 差分表記文言
	 */
	protected String diffKaikeiCommon(String denpyouKbn, GMap meisaiBf, GMap meisaiAf, Map<String, DenpyouItemInfo> mapDenpyouItemInfo, List<String> columnList) {
		StringBuilder sb = new StringBuilder();

		//項目単位
		for (String itemName : columnList) {

			if(!mapDenpyouItemInfo.containsKey(itemName)) {
				continue;
			}

			//支払日・計上日はmakeDiffShiharaibiで別枠として作成させるためスキップ
			if((!denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)) && (ITEM_SHIHARAIBI.equals(itemName) || ITEM_KEIJOUBI.equals(itemName))){
				continue;
			}

			DenpyouItemInfo denpyouItemInfo = mapDenpyouItemInfo.get(itemName);
			String labelName = denpyouItemInfo.getName();

			//変更前後データ
			Object bfVal =  meisaiBf.get(itemName);
			Object afVal =  meisaiAf.get(itemName);
			String strBfVal = "";
			String strAfVal = "";

			//値比較
			if (bfVal != null) {
				strBfVal = bfVal.toString();
			}
			if (afVal != null) {
				strAfVal = afVal.toString();
			}
			if (!strBfVal.equals(strAfVal)) {
				String strBfDispVal = denpyouItemInfo.getDispVal(strBfVal);
				String strAfDispVal = denpyouItemInfo.getDispVal(strAfVal);
				sb.append(labelName + "：" + strBfDispVal + " ⇒ " + strAfDispVal).append(BR);
			}
		}
		return sb.toString();
	}

	/**
	 * 添付ファイルの前後比較
	 * @param tenpuFileListBf   変更前（添付ファイルリスト）
	 * @param tenpuFileListAf   変更後（添付ファイルリスト）
	 * @param ebunshoDataListBf 変更前（e文書データ）
	 * @param ebunshoDataListAf 変更後（e文書データ）
	 * @return     差分表記文言
	 */
	protected String diffFile(List<GMap> tenpuFileListBf,   List<GMap> tenpuFileListAf, 
							  List<GMap> ebunshoDataListBf, List<GMap> ebunshoDataListAf) {
		if (tenpuFileListBf == null || tenpuFileListAf == null)
		{
			return "";
		}

		List<String>  tenpuFileNameBf = new ArrayList<String>();
		List<String>  tenpuFileNameAf = new ArrayList<String>();
		StringBuilder sbRireki   = new StringBuilder();

		// 前後サイズが異なるなら添付ファイル 追加 または 削除
		if (tenpuFileListBf.size() != tenpuFileListAf.size()) {
			for (GMap tenpuFileBf : tenpuFileListBf) tenpuFileNameBf.add(tenpuFileBf.get("file_name").toString());
			for (GMap tenpuFileAf : tenpuFileListAf) tenpuFileNameAf.add(tenpuFileAf.get("file_name").toString());
			sbRireki.append("■添付ファイル").append(BR);
			sbRireki.append(tenpuFileNameBf + " ⇒ ").append(BR).append(tenpuFileNameAf); 
		}
		
		// e文書項目比較		
		for (GMap ebunshoDataBf : ebunshoDataListBf) {
			for (GMap ebunshoDataAf : ebunshoDataListAf) { 
				// e文書番号＆e文書枝番号 が同じ、かつ差異がある場合、操作履歴作成
				if (ebunshoDataAf.get("ebunsho_no") != null && ebunshoDataBf.get("ebunsho_no") != null &&
					(ebunshoDataBf.get("ebunsho_no").equals(ebunshoDataAf.get("ebunsho_no")) && 
					 ebunshoDataBf.get("ebunsho_edano").equals(ebunshoDataAf.get("ebunsho_edano"))) &&
					!ebunshoDataBf.equals(ebunshoDataAf))
				{
					String diff = diffEbunsho(ebunshoDataBf, ebunshoDataAf);
					if (!sbRireki.toString().contains(ebunshoDataAf.getString("ebunsho_no"))) {
						sbRireki.append("■e文書 #" + ebunshoDataAf.get("ebunsho_no")).append(BR);
					}
					if (!diff.isEmpty()) {
						sbRireki.append(diff);
					}
				}
				else if (ebunshoDataAf.get("ebunsho_no") != null && ebunshoDataBf.get("ebunsho_no") != null &&
						 ebunshoDataBf.get("ebunsho_no").equals(ebunshoDataAf.get("ebunsho_no")) &&
						 ebunshoDataBf.get("edano").equals(ebunshoDataAf.get("edano"))) {
					// e文書無関係(リンクファイル=e文書番号null)はListから除外
					List<GMap> filteredEbunshoListBf = ebunshoDataListBf.stream().filter(x -> x.get("ebunsho_no") != null).collect(Collectors.toList());
					List<GMap> filteredEbunshoListAf = ebunshoDataListAf.stream().filter(x -> x.get("ebunsho_no") != null).collect(Collectors.toList());
					// 同じe文書番号のレコード数をカウント
					long ebunshoDataRecordBf   = filteredEbunshoListBf.stream().filter(gMapBf -> gMapBf.get("ebunsho_no").equals(ebunshoDataAf.get("ebunsho_no"))).count();
					long ebunshoDataRecordAf   = filteredEbunshoListAf.stream().filter(gMapAf -> gMapAf.get("ebunsho_no").equals(ebunshoDataAf.get("ebunsho_no"))).count();
					long ebunshoDataDifference = ebunshoDataRecordAf - ebunshoDataRecordBf; 
					// 同じe文書番号でレコード数が異なる（変更後が多い）場合、e文書明細行の追加
					if (ebunshoDataDifference > 0) {
						if (!sbRireki.toString().contains(ebunshoDataAf.get("ebunsho_no")) && !sbRireki.toString().contains("明細が追加されました。"))
						{
							sbRireki.append("■e文書 #" + ebunshoDataAf.get("ebunsho_no")).append(BR);
							sbRireki.append("e文書明細が追加されました。").append(BR);
						}
					}
				}
				// e文書番号が変更後にあり、変更前がnullの場合、リンクファイル⇒e文書への変更
				else if ((ebunshoDataAf.get("ebunsho_no") != null && ebunshoDataBf.get("ebunsho_no") == null) &&
						 (ebunshoDataBf.get("file_name").equals(ebunshoDataAf.get("file_name")))) {
					sbRireki.append("■e文書 #" + ebunshoDataAf.get("ebunsho_no")).append(BR);
					sbRireki.append("e文書対象：なし ⇒ あり").append(BR);
					String diff = diffEbunsho(ebunshoDataBf, ebunshoDataAf);
					if (!diff.isEmpty()) sbRireki.append(diff);
				}
			}
		}
		return sbRireki.toString();
	}
	
	/**
	 * e文書項目の前後比較
	 * @param tenpuFlieBf 変更前
	 * @param tenpuFileAf 変更後
	 * @return    差分表記文言
	 */
	protected String diffEbunsho (GMap tenpuFlieBf, GMap tenpuFileAf) {
		
		StringBuilder sb = new StringBuilder();
		
		String strBfVal = "";
		String strAfVal = "";
		String itemName = "";

		// フラグ
		Map<String, String> mapFlg = new HashMap<String, String>();
		mapFlg.put("1", "あり");
		mapFlg.put("0", "なし");
		mapFlg.put("" , "なし"); 
		
		for (String key : tenpuFlieBf.keySet()) {
			// キーから前後の値取得
			Object bfVal = tenpuFlieBf.get(key);
			Object afVal = tenpuFileAf.get(key); 

			// 変更前・変更後null処理
			if (bfVal != null) { strBfVal = bfVal.toString(); }
			else    { strBfVal = ""; }
			if (afVal != null) { strAfVal = afVal.toString(); }
			else    { strAfVal = ""; }
			// 値比較
			if (!strBfVal.equals(strAfVal) && !key.equals("ebunsho_no")) {
				if (key.equals("ebunsho_shubetsu")) {
					/** 書類種別 */
					Map<String, String> mapShubetsu = new HashMap<String, String>();
					mapShubetsu.put("" , "");
					mapShubetsu.put("0", "領収書");
					mapShubetsu.put("1", "請求書");
					mapShubetsu.put("2", "契約書");
					mapShubetsu.put("3", "納品書");
					mapShubetsu.put("4", "注文書");
					mapShubetsu.put("5", "見積書");
					itemName = "書類種別";
					strBfVal = mapShubetsu.get(strBfVal);
					strAfVal = mapShubetsu.get(strAfVal);
					sb.append(itemName + "：" + strBfVal + " ⇒ " + strAfVal).append(BR);
				} 
				else if (!(strBfVal.equals("")  && strAfVal.equals("0"))) {
					switch (key) {
						/** 電子取引フラグ */
						case "denshitorihiki_flg" :
							itemName = "電子取引";
							strBfVal = mapFlg.get(strBfVal);
							strAfVal = mapFlg.get(strAfVal);
							break;
							/** TS付与フラグ */
						case "tsfuyo_flg" :
							itemName = "TS付与";
							strBfVal = mapFlg.get(strBfVal);
							strAfVal = mapFlg.get(strAfVal);
							break;
							/** e文書年月日 */
						case "ebunsho_nengappi" :
							itemName = "e文書年月日";
							strBfVal = strBfVal.replace("-", "/");
							strAfVal = strAfVal.replace("-", "/");
							break;
							/** e文書金額 */
						case "ebunsho_kingaku" :
							itemName = "e文書金額";
							DecimalFormat decFormat = new DecimalFormat("###,###"); // カンマ区切り
							if (!StringUtils.isEmpty(strBfVal)) {
								int intBfVal = Integer.parseInt(strBfVal);
								strBfVal = decFormat.format(intBfVal);
							}
							if (!StringUtils.isEmpty(strAfVal)) {
								int intAfVal = Integer.parseInt(strAfVal); 
								strAfVal = decFormat.format(intAfVal);
							}
							break;
							/** e文書発行者 */
						case "ebunsho_hakkousha" :
							itemName = "e文書発行者";
							break;
							/** e文書品名 */
						case "ebunsho_hinmei" :
							itemName = "e文書品名";
							break;
					}
					sb.append(itemName + "：" + strBfVal + " ⇒ " + strAfVal).append(BR);
				}
			} 
		}
		return sb.toString();
	}
// -*	

	/**
	 * 稟議金額超過コメントの前後比較
	 * @param ringiBf 変更前
	 * @param ringiAf 変更後
	 * @return 差分表記文言
	 */
	protected String diffRingi(GMap ringiBf, GMap ringiAf){
		if (ringiBf == null || ringiAf == null)
		{
			return "";
		}

		String choukaCommentBf = ringiBf.get("ringi_kingaku_chouka_comment").toString();
		String choukaCommentAf = ringiAf.get("ringi_kingaku_chouka_comment").toString();

		// 超過コメントに差分なし
		if (choukaCommentBf.equals(choukaCommentAf))
		{
			return "";
		}

		// 超過コメントに差分あり
		StringBuilder sb = new StringBuilder();
		String name = ringiAf.get("ringiKingakuName").toString();
		sb.append("■" + name + "超過コメント").append(BR);
		sb.append(choukaCommentBf + " ⇒ ").append(BR).append(choukaCommentAf);
		return sb.toString();
	}

	/**
	 * 伝票IDとユーザーIDをキーに「承認ルート」の現在フラグを更新する。
	 * @param  seigyoUserId 制御ユーザーID
	 * @param  denpyouId     伝票ID
	 * @param  kidokuFlg     既読フラグ
	 * @param  loginUserId  登録ユーザーID
	 * @return 結果
	 */
	public int insertTsuuchi(String seigyoUserId, String denpyouId, String kidokuFlg, String loginUserId) {
		final String sql = "INSERT INTO tsuuchi "
				+ "(user_id, denpyou_id, edano, kidoku_flg, touroku_user_id, touroku_time, koushin_user_id, koushin_time) "
				+ "VALUES("
				+ "?, "
				+ "?, "
				+ "(SELECT MAX(edano) FROM shounin_joukyou WHERE denpyou_id = ?), "
				+ "?, "
				+ "?, "
				+ "current_timestamp, "
				+ "?, "
				+ "current_timestamp )";
		return connection.update(sql, seigyoUserId, denpyouId, denpyouId, kidokuFlg, loginUserId, loginUserId);
	}

	/**
	 * マル秘文書フラグ変更処理
	 * @param denpyouId 伝票ID
	 * @param user ログインユーザー
	 * @param maruhiFlg マル秘文書フラグ
	 */
	public void maruhiHenkou(String denpyouId, User user, String maruhiFlg){

		//処理するユーザーのマル秘フラグ変更権限確認
		if("1".equals(maruhiFlg) && user.isMaruhiSetteiFlg() == false) throw new EteamAccessDeniedException();
		if("0".equals(maruhiFlg) && user.isMaruhiKaijyoFlg() == false) throw new EteamAccessDeniedException();


		//承認状況登録用のデータ取得・設定
		String flgStr = "1".equals(maruhiFlg) ? "する" : "しない";

		//マル秘フラグの設定
		updateMaruhiFlg(denpyouId, user.getTourokuOrKoushinUserId(), maruhiFlg);

		//承認状況登録
		GMap procRoute = wfLogic.findProcRoute(denpyouId, user);
		if(procRoute != null){
			insertShouninJoukyou(denpyouId, user, procRoute, "", "マル秘扱い", flgStr);
		}else{
			insertShouninJoukyouUserKengen(denpyouId, user, "マル秘扱い", flgStr);
		}
	}

	/**
	 * 伝票の完了（承認・否認）通知
	 * 承認ルートに登録されたユーザーが対象。ただし、最終処理者以降は除く、業務ロールは除く。
	 *
	 * @param denpyouId 伝票ID
	 * @param userIdList ユーザーID
	 * @param user ログインユーザー
	 */
	public void tsuutiTouroku(String denpyouId, List<String> userIdList, User user) {
		userIdList = new ArrayList<>(new HashSet<>(userIdList));//重複けす
		userIdList.remove(user.getSeigyoUserId());//※処理ユーザーにまで飛ぶのは変なので
		for(String userId : userIdList){
			insertTsuuchi(userId, denpyouId, "0", user.getTourokuOrKoushinUserId());
		}
	}

	/**
	 * メール送信
	 * @param denpyouId 伝票ID
	 * @param version ヴァージョン
	 * @param userIdList 宛先のユーザーID　※承認の場合はユーザID\n処理権限名
	 * @param mailTsuuchiKbn メール通知区分
	 * @param user ユーザー
	 */
	public void sendRealTimeMail(String denpyouId, Integer version, List<String> userIdList, String mailTsuuchiKbn, User user) {
		String denpyouKbn = denpyouId.substring(7, 11);
		DenpyouShubetsuIchiranDao dsDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		String denpyouShubetsu = dsDao.find(denpyouKbn).denpyouShubetsu;

		//ユーザIDの重複を消すのと処理ユーザーにまで飛ぶのは変
		userIdList = new ArrayList<>(new HashSet<>(userIdList));
		for(int i = userIdList.size() - 1; i >= 0; i--) {
			String user1 = userIdList.get(i);
			if(user1.contains("\n")) user1 = user1.split("\n")[0];
			if(user1.equals(user.getSeigyoUserId())){
				userIdList.remove(i);
			}else {
				for(int j = 0; j < i; j++) {
					String user2 = userIdList.get(j);
					if(user2.contains("\n")) user2 = user2.split("\n")[0];
					if(user1.equals(user2)){
						userIdList.remove(i);
						break;
					}
				}
			}
		}

		// メール配信機能がOFFなら終わり
		if (! systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.MAIL_HAISHIN))
		{
			return;
		}

		String naiyou = getMailNaiyou(denpyouId, denpyouKbn, version, mailTsuuchiKbn);

		//送信対象ユーザーにメール送る
		for(String userId : userIdList){
			String shoriKengen = null;
			if(userId.contains("\n")){
				shoriKengen = userId.split("\n")[1];
				userId = userId.split("\n")[0];
			}

			//ユーザーのメール送信設定がOFFなら飛ばす
			if (! tsuuchiLogic.findMailTsuuchiSettei(userId,  mailTsuuchiKbn))
			{
				continue;
			}

			// メール送信対象ユーザーのメールアドレスを取得
			GMap userInfo = bumonUserLogic.selectUserInfo(userId);

			// メールアドレスがセットされているなら送る
			if(null != userInfo && StringUtils.isNotEmpty(userInfo.get("mail_address"))) {
				List<String> sendMailAdressList = new ArrayList<String>();
				sendMailAdressList.add(userInfo.get("mail_address"));
				String kenmei = WorkflowUtil.getMailTitle(mailTsuuchiKbn, denpyouShubetsu, shoriKengen);
				mailLogic.execute(
						kenmei,
						naiyou,
						sendMailAdressList, null, null);
			}
		}
	}

	/**
	 * メール本文生成
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param version ヴァージョン
	 * @param mailTsuuchiKbn メール通知区分（内部コード）
	 * @return メール本文
	 */
	protected String getMailNaiyou(String denpyouId, String denpyouKbn, Integer version, String mailTsuuchiKbn) {
		String text = WorkflowUtil.getMeilText(mailTsuuchiKbn);

		// 伝票を開くURL
		String denpyouShubetsuUrl = wfLogic.selectDenpyouShubetu(denpyouKbn).get("denpyou_shubetsu_url").toString();
		String url = setting.shotodokeUrl() + denpyouShubetsuUrl + "?denpyouKbn=" + denpyouKbn + "&denpyouId=" + denpyouId;
		if (version != null && version >= 1) {
			url = url + "&version=" + version;
		}

		text = text + "\r\n下記URLより確認してください。\r\n" + url;
		return text;
	}

	/**
	 * テーブル名の取得
	 * @param denpyouKbn 伝票区分
	 * @param isShinsei 申請部（明細部じゃない）ならtrue
	 * @return テーブル名
	 */
	protected String findTableName(String denpyouKbn, boolean isShinsei) {
		String tableName = "";
		switch (denpyouKbn) {
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:				tableName = (isShinsei ? "keihiseisan" : "keihiseisan_meisai"); break;
		case DENPYOU_KBN.KARIBARAI_SHINSEI:	 				tableName = (isShinsei ? "karibarai" : ""); break;
		case DENPYOU_KBN.SEIKYUUSHO_BARAI:					tableName = (isShinsei ? "seikyuushobarai" : "seikyuushobarai_meisai"); break;
		case DENPYOU_KBN.RYOHI_SEISAN:						tableName = (isShinsei ? "ryohiseisan" : "ryohiseisan_meisai,ryohiseisan_keihi_meisai"); break;
		case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:			tableName = (isShinsei ? "ryohi_karibarai" : "ryohi_karibarai_meisai,ryohi_karibarai_keihi_meisai"); break;
		case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:				tableName = (isShinsei ? "tsuukinteiki" : ""); break;
		case DENPYOU_KBN.FURIKAE_DENPYOU:					tableName = (isShinsei ? "furikae" : ""); break;
		case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU:			tableName = (isShinsei ? "tsukekae" : "tsukekae_meisai"); break;
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:			tableName = (isShinsei ? "jidouhikiotoshi" : "jidouhikiotoshi_meisai"); break;
		case DENPYOU_KBN.KOUTSUUHI_SEISAN:					tableName = (isShinsei ? "koutsuuhiseisan" : "koutsuuhiseisan_meisai"); break;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:				tableName = (isShinsei ? "kaigai_ryohiseisan" : "kaigai_ryohiseisan_meisai,kaigai_ryohiseisan_keihi_meisai"); break;
		case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:	tableName = (isShinsei ? "kaigai_ryohi_karibarai" : "kaigai_ryohi_karibarai_meisai,kaigai_ryohi_karibarai_keihi_meisai"); break;
		case DENPYOU_KBN.SIHARAIIRAI:						tableName = (isShinsei ? "shiharai_irai" : "shiharai_irai_meisai"); break;
		default:
			if(isKaniTodoke(denpyouKbn)) {
				tableName = (isShinsei ? "kani_todoke" : "kani_todoke_meisai");
			}
			break;
		}

		tableName = findTableName_Ext(tableName, denpyouKbn, isShinsei);

		return tableName;
	}

	/**
	 * カスタマイズ用、テーブル名取得
	 * @param tableName テーブル名
	 * @param denpyouKbn 伝票区分
	 * @param isShinsei 申請部（明細部じゃない）ならtrue
	 * @return テーブル名
	 */
	protected String findTableName_Ext(String tableName, String denpyouKbn, boolean isShinsei) {
		return tableName;
	}

	/**
	 * ユーザー定義届書かどうか判定する
	 * @param denpyouKbn 伝票区分
	 * @return ユーザー定義届書ならtrue
	 */
	public boolean isKaniTodoke(String denpyouKbn) {
		return denpyouKbn.substring(0, 1).equals("B");
	}

	/**
	 * 添付ファイルチェックフラグを取得
	 * @param denpyouKbn 伝票区分
	 * @return 添付ファイルチェック対象ならtrue
	 */
	public boolean isCheckTenpFile(String denpyouKbn){
		//伝票区分により分岐
		switch (denpyouKbn) {
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			return "1".equals(setting.tenpuCheckA001());
		case DENPYOU_KBN.SEIKYUUSHO_BARAI:
			return "1".equals(setting.tenpuCheckA003());
		case DENPYOU_KBN.RYOHI_SEISAN:
			return "1".equals(setting.tenpuCheckA004());
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			return "1".equals(setting.tenpuCheckA009());
		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			return "1".equals(setting.tenpuCheckA010());
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			return "1".equals(setting.tenpuCheckA011());
		case DENPYOU_KBN.SIHARAIIRAI:
			return "1".equals(setting.tenpuCheckA013());
		default:
			return false;
		}
	}

	/**
	 * e文書使用フラグを取得
	 * @param denpyouKbn 伝票区分
	 * @return e文書使用フラグONならtrue
	 */
	public boolean isUseEbunsho(String denpyouKbn) {
		//SIAS版でないか、/e文書使用オプション機能が無効ならfalse
		if (Open21Env.getVersion() != Version.SIAS || !RegAccess.checkEnableEbunshoOption())
		{
			return false;
		}

		//e文書使用制限有効かつe文書有効フラグONならチェック、それ以外ならfalse
		if("1".equals(setting.ebunshoEnableFlg()) || "2".equals(setting.ebunshoEnableFlg())){

			//伝票区分により分岐
			if (isKaniTodoke(denpyouKbn))
			{
				// ユーザー定義届出
				return "1".equals(setting.ebunshoSeiseiB000());
			}
			else
			{
				switch (denpyouKbn) {
				case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
					return "1".equals(setting.ebunshoSeiseiA001());
				case DENPYOU_KBN.KARIBARAI_SHINSEI:
					return "1".equals(setting.ebunshoSeiseiA002());
				case DENPYOU_KBN.SEIKYUUSHO_BARAI:
					return "1".equals(setting.ebunshoSeiseiA003());
				case DENPYOU_KBN.RYOHI_SEISAN:
					return "1".equals(setting.ebunshoSeiseiA004());
				case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
					return "1".equals(setting.ebunshoSeiseiA005());
				case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
					return "1".equals(setting.ebunshoSeiseiA006());
				case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
					return "1".equals(setting.ebunshoSeiseiA009());
				case DENPYOU_KBN.KOUTSUUHI_SEISAN:
					return "1".equals(setting.ebunshoSeiseiA010());
				case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
					return "1".equals(setting.ebunshoSeiseiA011());
				case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
					return "1".equals(setting.ebunshoSeiseiA012());
				case DENPYOU_KBN.SIHARAIIRAI:
					return "1".equals(setting.ebunshoSeiseiA013());
				case "search": // 検索の場合は会社設定上使用するなら、実際に使用している伝票種別の有無は見ない
					return true;
				default:
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 添付ファイルデータをPDF変換して返します。
	 * 元データがPDFなら添付のまま、PDF以外(画像)ならPDF変換処理を行う。
	 * @param denpyouId 伝票ID
	 * @param edano 該当添付ファイルの枝番
	 * @return PDFデータ
	 */
	public byte[] createPdfData(String denpyouId, int edano) {
		GMap map = wfLogic.tenpuFileFind(denpyouId, edano);
		if (null == map) throw new EteamDataNotFoundException();
		byte[] fileData = (byte[])map.get("binary_data");
		String fileNm = (String)map.get("file_name");
		String extension = EteamIO.getExtension(fileNm);
		// PDFならそのまま返却
		if ("pdf".equalsIgnoreCase(extension)){
			return fileData;
		}

		//会社設定「e文書変換時に画像を圧縮する」がオンなら取得画像を圧縮
		if("1".equals(setting.ebunshoCompressFlg())){
			// 画像イメージを1mb以内に圧縮
			return eLogic.image2Pdf_JpegCompress(fileData, extension);
		}

		// JPEG,JPGなら渡されたファイルの圧縮状態でPDF化
		if (("jpeg".equalsIgnoreCase(extension)) || ("jpg".equalsIgnoreCase(extension)) ){
			return eLogic.image2Pdf_Jpeg(fileData);
		}

		// それ以外の拡張子なら無圧縮PDF化
		return eLogic.image2Pdf_Lossless(fileData);

	}
	
	/**
	 * 添付ファイルデータをPDF変換して返します（圧縮なし固定・プレビュー表示高速化カスタマイズ用）。
	 * 元データがPDFなら添付のまま、PDF以外(画像)ならPDF変換処理を行う。
	 * @param denpyouId 伝票ID
	 * @param edano 該当添付ファイルの枝番
	 * @return PDFデータ
	 */
	public byte[] createPdfDataWithoutCompression(String denpyouId, int edano) {
		GMap map = wfLogic.tenpuFileFind(denpyouId, edano);
		if (null == map) throw new EteamDataNotFoundException();
		byte[] fileData = (byte[])map.get("binary_data");
		String fileNm = (String)map.get("file_name");
		String extension = EteamIO.getExtension(fileNm);
		// PDFならそのまま返却
		if ("pdf".equalsIgnoreCase(extension)){
			return fileData;
		}

		// JPEG,JPGなら渡されたファイルの圧縮状態でPDF化
		if (("jpeg".equalsIgnoreCase(extension)) || ("jpg".equalsIgnoreCase(extension)) ){
			return eLogic.image2Pdf_Jpeg(fileData);
		}

		// それ以外の拡張子なら無圧縮PDF化
		return eLogic.image2Pdf_Lossless(fileData);
	}

	/**
	 *  E文書情報
	 */
	@Getter @Setter @ToString
	class Ebunsho {
		/** 枝番号 */
		int edano;
		/** e文書番号 */
		String ebunshoNo;
		/** 今採番した？ */
		boolean now;
	}
	/**
	 * 添付ファイルとe文書番号を紐付ける。
	 * 採番済のものと、このメソッド内で採番した2系統があるが、両方まとめて返す。
	 * @param denpyouId 伝票番号
	 * @return 文書番号の紐づけリスト
	 */
	public Map<String, Ebunsho> createFilenameEbunshonoMap(String denpyouId) {
		Map<String, Ebunsho> ret = new HashMap<String, Ebunsho>();

		// 添付ファイル一覧取得
		List<GMap> fileList = wfLogic.selectTenpuFile(denpyouId);
		for (GMap data : fileList) {
			String fileNm = (String)data.get("file_name");
			int edano = (Integer)data.get("edano");
			Ebunsho e = new Ebunsho();
			ret.put(fileNm,  e);
			e.setEdano(edano);

			//すでにe文書番号があるならそれを
			String ebunshoNo = wfLogic.findEbunshoNo(denpyouId, edano);
			if (null != ebunshoNo) {
				e.setEbunshoNo(ebunshoNo);
			//e文書番号が付いていないなら採番
			} else {
				ebunshoNo = eLogic.createEbunshoNo();
				e.setEbunshoNo(ebunshoNo);
				e.setNow(true);
			}
		}
		return ret;
	}

	/**
	 * 関連伝票テーブルに新規登録する。
	 *
	 * @param denpyouId 伝票ID
	 * @param kanrenDenpyou 関連伝票ID
	 * @param kanrenDenpyouKbn 関連伝票の伝票区分
	 * @param kanrenDenpyouKihyoubi 関連伝票の起票日
	 * @param kanrenDenpyouShouninbi 関連伝票の承認日
	 * @return 処理結果
	 */
	public int insertKanrenDenpyou(String denpyouId, String kanrenDenpyou, String kanrenDenpyouKbn, Date kanrenDenpyouKihyoubi, Date kanrenDenpyouShouninbi)
	{
		final String sql = "INSERT INTO kanren_denpyou VALUES(?, ?, ?, ?, ?)";
		return connection.update(sql, denpyouId, kanrenDenpyou, kanrenDenpyouKbn, kanrenDenpyouKihyoubi, kanrenDenpyouShouninbi);
	}

	/**
	 * 関連伝票テーブルのデータを削除する。
	 * @param denpyouId 伝票ID
	 * @return 処理結果
	 */
	public int deleteKanrenDenpyou(String denpyouId)
	{
		final String sql = "DELETE FROM kanren_denpyou WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}

	/**
	 * 取得した関連伝票を整形する。
	 * @param kanrenList 関連伝票リスト
	 */
	public void formatKanrenDenpyou(List<GMap> kanrenList){

		for (GMap kanrenMap : kanrenList) {

			// 関連伝票に紐づけされた関連伝票リスト
			List<GMap> kanrenChildList = new ArrayList<>();
			// 表示用 関連伝票
			String embedSpaceStr = "";
			String putDenpyouId = (String)kanrenMap.get("denpyou_id");
			String putDenpyouShubetsu = (String)kanrenMap.get("denpyou_shubetsu");
			String putDenpyouKbn = (String)kanrenMap.get("denpyou_kbn");
			String putKihyouBi = formatUntilDate(kanrenMap.get("touroku_time").toString());
			String putShouninBi = formatUntilDate(kanrenMap.get("shounin_time").toString());
			String putDenpyouShubetsuUrl = (String)kanrenMap.get("denpyou_shubetsu_url");
			String putDenpyouUrl = formatKanrenDenpyouUrl ((String)kanrenMap.get("denpyou_shubetsu_url"), (String)kanrenMap.get("denpyou_id"), (String)kanrenMap.get("denpyou_kbn"), kanrenMap.get("version").toString());

			// 表示用 添付ファイル
			String putTenpuName = "";
			String putTenpuUrl = "";

			List<GMap> tenpuFileList = wfLogic.selectTenpuFile((String)kanrenMap.get("denpyou_id"));
			formatTenpuUrl(tenpuFileList, (String)kanrenMap.get("denpyou_id"), (String)kanrenMap.get("denpyou_kbn"));

			// 関連伝票リスト子
			fetchKanrenChildList(kanrenChildList, (String)kanrenMap.get("denpyou_id"), (String)kanrenMap.get("denpyou_id"), 0);

			for (GMap map : kanrenChildList){
				if (map.get("moto_denpyou_id").equals(kanrenMap.get("denpyou_id"))) {
					embedSpaceStr += "," + fetchEmbedSpace((int)map.get("list_edano"));
					putDenpyouShubetsu += "<br>" + map.get("denpyou_shubetsu");
					putDenpyouId += "<br>" + fetchEmbedSpace((int)map.get("list_edano")) + map.get("denpyou_id");
					putKihyouBi += "<br>" + formatUntilDate(map.get("touroku_time").toString());
					putShouninBi += "<br>" + formatUntilDate(map.get("shounin_time").toString());
					putDenpyouShubetsuUrl += "," + map.get("denpyou_shubetsu_url");
					putDenpyouKbn += "," + map.get("denpyou_kbn");
					putDenpyouUrl += "," + formatKanrenDenpyouUrl((String)map.get("denpyou_shubetsu_url"), (String)map.get("denpyou_id"), (String)map.get("denpyou_kbn"), map.get("version").toString());

					// 関連伝票リスト子の添付ファイル
					List<GMap> tenpu = wfLogic.selectTenpuFile((String)map.get("denpyou_id"));
					formatTenpuUrl(tenpu, (String)map.get("denpyou_id"), (String)map.get("denpyou_kbn"));
					tenpuFileList.addAll(tenpu);
				}
			}

			// 添付ファイル
			for (GMap tenpuMap : tenpuFileList) {
				putTenpuName += tenpuMap.get("file_name") + ",";
				putTenpuUrl += tenpuMap.get("tenpu_url") + ",";
			}

			kanrenMap.put("embed_space", embedSpaceStr);
			kanrenMap.put("file_name", putTenpuName);
			kanrenMap.put("tenpu_url", putTenpuUrl);
			kanrenMap.put("id", putDenpyouId);
			kanrenMap.put("shubetsu", putDenpyouShubetsu);
			kanrenMap.put("kbn", putDenpyouKbn);
			kanrenMap.put("denpyou_shubetsu_url", putDenpyouShubetsuUrl);
			kanrenMap.put("denpyou_url", putDenpyouUrl);
			kanrenMap.put("kihyou_bi", putKihyouBi);
			kanrenMap.put("shounin_bi", putShouninBi);
			kanrenMap.put("touroku_time", formatUntilDate(kanrenMap.get("touroku_time").toString()));
			kanrenMap.put("shounin_time", formatUntilDate(kanrenMap.get("shounin_time").toString()));
			if(null != kanrenMap.get("ringi_kingaku_hikitsugi_flg")){
				String putRingiKingakuHikitsugiFlg = kanrenMap.get("ringi_kingaku_hikitsugi_flg").toString();
				kanrenMap.put("ringi_kingaku_hikitsugi_flg", putRingiKingakuHikitsugiFlg);
			}

			kanrenChildList.clear();
		}
	}

	/**
	 * 関連伝票に紐づく関連伝票を再帰的に取得する。
	 * @param kanrenChildList 関連伝票リスト子
	 * @param denpyouId 伝票ID
	 * @param motoDenpyouId 紐付け元になる伝票ID
	 * @param depth リストの深度
	 */
	protected void fetchKanrenChildList (List<GMap> kanrenChildList, String denpyouId, String motoDenpyouId, int depth) {

		List<GMap> childList = wfLogic.loadKanrenDenpyou(denpyouId);

		depth++;
		for (GMap childMap : childList) {
			childMap.put("moto_denpyou_id", motoDenpyouId);
			childMap.put("list_edano", depth);
			childMap.put("touroku_time", formatUntilDate(childMap.get("touroku_time").toString()));
			childMap.put("shounin_time", formatUntilDate(childMap.get("shounin_time").toString()));
			kanrenChildList.add(childMap);

			fetchKanrenChildList(kanrenChildList, (String)childMap.get("denpyou_id"), motoDenpyouId, depth);
		}
	}

	/**
	 * 添付ファイルリストにURLをセットする。
	 * @param tenpu 添付ファイルリスト
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 */
	protected void formatTenpuUrl (List<GMap> tenpu, String denpyouId, String denpyouKbn){
		for (GMap map : tenpu) {
			map.put("tenpu_url", "denpyou_file_download?denpyouId=" + denpyouId + "&denpyouKbn=" + denpyouKbn + "&downloadFileNo=" + map.get("edano"));
		}
	}

	/**
	 * 関連伝票のURLを作成する。
	 *
	 * @param url 伝票種別URL
	 * @param id 伝票ID
	 * @param kbn 伝票区分
	 * @param version バージョン
	 * @return 連票URL
	 */
	protected String formatKanrenDenpyouUrl (String url, String id, String kbn, String version) {
		String ret = url + "?denpyouId=" + id + "&denpyouKbn=" + kbn;
		if (Integer.valueOf(version) > 0) {
			ret += "&version=" + version;
		}
		return ret;
	}

	/**
	 * 枝番による空白領域を取得する。
	 * @param listEdano ループ回数
	 * @return 結果
	 */
	protected String fetchEmbedSpace (int listEdano) {
		String ret = "";
		for (int i = 0; i < listEdano; i++) {
			ret += "　";
		}
		return ret;
	}

	/**
	 * 文字列をYYYY/MM/DDにフォーマット。
	 * @param str 文字列
	 * @return YYYY/MM/DDの文字列
	 */
	protected String formatUntilDate (String str) {
		return str.substring(0, 10).replaceAll("-", "/");
	}

	/**
	 * 追加ファイル名の変更。
	 * 既存ファイル・追加ファイルの中に同名がいたら、hoge.jpg → hoge_##.jpgの用に変更
	 * UTF-8のノーブレークスペース(&nbsp)が存在した場合、通常半角スペースに変換
	 * 『,(半角カンマ)』が存在した場合、『、(読点)』に変換
	 * UTF-8の波ダッシュ(～)がある場合Shift-JISの波形に変換
	 * Windowsのファイル名使用不可能文字『\/:*?"<>|』がある場合大文字に変換
	 * @param denpyouId 伝票ID
	 * @param addFileName 追加ファイル名
	 * @return 追加ファイル名(改)
	 */
	public String[] renameFile(String denpyouId, String[] addFileName) {
		String[] curs = EteamCommon.mapList2Arr(wfLogic.selectTenpuFile(denpyouId), "file_name");
		return WorkflowUtil.renameFile(denpyouId, addFileName, curs);
	}

	/**
	 * 伝票起案紐付けレコードロック<br>
	 * 伝票起案紐付けをプライマリーキーでレコードロックする。
	 *
	 * @param denpyouId 伝票ID
	 * @return 検索結果Map
	 */
	public GMap selectDenpyouKianHimozukeForUpdate(String denpyouId){
		return connection.find("SELECT * FROM denpyou_kian_himozuke WHERE denpyou_id = ? FOR UPDATE", denpyouId);
	}

	/**
	 * 伝票起案紐付けレコード取得<br>
	 * 伝票起案紐付けをプライマリーキーでレコードを取得する。
	 *
	 * @param denpyouId 伝票ID
	 * @return 検索結果Map
	 */
	public GMap selectDenpyouKianHimozuke(String denpyouId){
		return connection.find("SELECT * FROM denpyou_kian_himozuke WHERE denpyou_id = ?", denpyouId);
	}

	/**
	 * 伝票起案紐付けレコード削除<br>
	 * 指定された伝票IDで合致するレコードを削除する。
	 *
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int deleteDenpyouKianHimozuke(String denpyouId){
		return connection.update("DELETE FROM denpyou_kian_himozuke WHERE denpyou_id = ?", denpyouId);
	}

	/**
	 * 伝票起案紐付レコード登録<br>
	 * 指定された伝票IDで伝票起案紐付レコードを登録する。
	 *
	 * @param denpyouId 伝票ID
	 * @param kianDenpyouId 起案伝票ID
	 * @param kianDenpyouKbn 起案伝票区分
	 * @return 登録件数
	 */
	public int insertDenpyouKianHimozuke(String denpyouId, String kianDenpyouId, String kianDenpyouKbn){

		// 起案伝票から引き継ぐ伝票番号を取得する。
		String jisshiNendo = null;
		String jisshiKianbangou = null;
		String shishutsuNendo = null;
		String shishutsuKianbangou = null;
		if (!isEmpty(kianDenpyouId)){
			GMap aMap = this.kianTsuikaLogic.findKianDenpyouData(kianDenpyouId);
			jisshiNendo = (String)aMap.get("jisshi_nendo");
			jisshiKianbangou = (String)aMap.get("jisshi_kian_bangou");
			shishutsuNendo = (String)aMap.get("shishutsu_nendo");
			shishutsuKianbangou = (String)aMap.get("shishutsu_kian_bangou");
		}

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("INSERT INTO denpyou_kian_himozuke")
				 .append(" VALUES (?, null, null, null, null, null, '0', null, ?, ?, ?, ?, ?, ?, null, '', '')");

		// レコードを登録する。
		int cntResult = connection.update(sbMainSql.toString(), denpyouId, kianDenpyouId, kianDenpyouKbn, jisshiNendo, jisshiKianbangou, shishutsuNendo, shishutsuKianbangou);
		return cntResult;
	}

	/**
	 * 伝票起案紐付け更新（申請時の起案番号採番情報を反映）<br>
	 * 以下のカラムについてデータを更新する。
	 * <ul>
	 * <li>部門コード</li>
	 * <li>シリアル番号</li>
	 * <li>年度</li>
	 * <li>起案番号</li>
	 * <li>実施年度</li>
	 * <li>実施起案番号</li>
	 * <li>支出年度</li>
	 * <li>支出起案番号</li>
	 * </ul>
	 *
	 * @param denpyouId 伝票ID
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kian_bangou_from 開始起案番号
	 * @param kianbangou 起案番号
	 * @param jisshiNendo 実施年度
	 * @param jisshiKianbangou 実施起案番号
	 * @param shishutsuNendo 支出年度
	 * @param shishutsuKianbangou 支出起案番号
	 * @return 登録件数
	 */
	public int updateDenpyouKianHimozuke(String denpyouId, String bumonCd, String nendo, String ryakugou, int kian_bangou_from, int kianbangou, String jisshiNendo, String jisshiKianbangou, String shishutsuNendo, String shishutsuKianbangou) {

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("UPDATE denpyou_kian_himozuke SET")
				 .append("  bumon_cd = ?")
				 .append(" ,nendo = ?")
				 .append(" ,ryakugou = ?")
				 .append(" ,kian_bangou_from = ?")
				 .append(" ,kian_bangou = ?")
				 .append(" ,jisshi_nendo = ?")
				 .append(" ,jisshi_kian_bangou = ?")
				 .append(" ,shishutsu_nendo = ?")
				 .append(" ,shishutsu_kian_bangou = ?")
				 .append(" WHERE")
				 .append("  denpyou_id = ?");

		// レコードを更新する。
		int cntResult = connection.update(sbMainSql.toString(), bumonCd, nendo, ryakugou, kian_bangou_from, kianbangou, jisshiNendo, jisshiKianbangou, shishutsuNendo, shishutsuKianbangou, denpyouId);
		return cntResult;
	}

	/**
	 * 伝票起案紐付レコード更新（伝票の更新処理で起案伝票情報を反映）<br>
	 * 指定された伝票IDで伝票起案紐付レコードを更新する。
	 *
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param kianDenpyouId 起案伝票ID
	 * @param kianDenpyouKbn 起案伝票区分
	 * @return 登録件数
	 */
	public int updateDenpyouKianHimozuke(String denpyouId, String denpyouKbn, String kianDenpyouId, String kianDenpyouKbn){
		/*
		 * 起案伝票IDに設定がない場合は処理を終了する。
		 */
		if (isEmpty(kianDenpyouId)){
			return 0;
		}

		// 使用部品
		this.denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, super.connection);

		// 伝票起票紐付の現在レコードを取得する（レコードロック）。
		GMap mapDenpyou = this.selectDenpyouKianHimozukeForUpdate(denpyouId);
		String jisshiNendo = (String)mapDenpyou.get("jisshi_nendo");
		String jisshiKianbangou = (String)mapDenpyou.get("jisshi_kian_bangou");
		String shishutsuNendo = (String)mapDenpyou.get("shishutsu_nendo");
		String shishutsuKianbangou = (String)mapDenpyou.get("shishutsu_kian_bangou");

		// 伝票の予算執行対象を取得する。
		String yosanShikkouTaishou = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);

		// 予算執行対象の設定値により更新項目を変更する。
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou)){
			// 支出起案は実施起案を起案追加で変更できるため、下記項目を更新対象とする。

			// 起案伝票の伝票起票紐付レコードを取得する。
			GMap mapKianDenpyou = this.selectDenpyouKianHimozuke(kianDenpyouId);
			jisshiNendo = (String)mapKianDenpyou.get("jisshi_nendo");
			jisshiKianbangou = (String)mapKianDenpyou.get("jisshi_kian_bangou");

		}else if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)){
			// 支出依頼は実施起案の起案追加／支出起案の起案追加が行える。

			// 起案伝票の伝票起票紐付レコードを取得する。
			GMap mapKianDenpyou = this.selectDenpyouKianHimozuke(kianDenpyouId);

			// 起案伝票の予算執行対象を取得する。
			String kianYosanShikkouTaishou = denpyouKanriLogic.getYosanShikkouTaishou(kianDenpyouKbn);

			// 予算執行対象の設定値により更新項目を変更する。
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(kianYosanShikkouTaishou)){
				// 起案伝票が実施起案の場合、下記項目を更新対象とする。
				jisshiNendo = (String)mapKianDenpyou.get("jisshi_nendo");
				jisshiKianbangou = (String)mapKianDenpyou.get("jisshi_kian_bangou");
				shishutsuNendo = null;
				shishutsuKianbangou = null;

			}else if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(kianYosanShikkouTaishou)){
				// 起案伝票が支出起案の場合、下記項目を更新対象とする。
				jisshiNendo = (String)mapKianDenpyou.get("jisshi_nendo");
				jisshiKianbangou = (String)mapKianDenpyou.get("jisshi_kian_bangou");
				shishutsuNendo = (String)mapKianDenpyou.get("shishutsu_nendo");
				shishutsuKianbangou = (String)mapKianDenpyou.get("shishutsu_kian_bangou");
			}
		}

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("UPDATE denpyou_kian_himozuke SET")
				 .append("  kian_denpyou = ?")
				 .append(" ,kian_denpyou_kbn = ?")
				 .append(" ,jisshi_nendo = ?")
				 .append(" ,jisshi_kian_bangou = ?")
				 .append(" ,shishutsu_nendo = ?")
				 .append(" ,shishutsu_kian_bangou = ?")
				 .append(" WHERE denpyou_id = ?");

		// レコードを更新する。
		int cntResult = connection.update(sbMainSql.toString(), kianDenpyouId, kianDenpyouKbn, jisshiNendo, jisshiKianbangou, shishutsuNendo, shishutsuKianbangou, denpyouId);
		return cntResult;
	}

	/**
	 * 伝票起案紐付レコード更新（起案終了フラグ）<br>
	 * 指定された伝票IDに合致するレコードの起案終了フラグを更新する。
	 *
	 * @param denpyouId 伝票ID
	 * @param kianSyuryoFlg 起案終了フラグ
	 * @return 更新件数
	 */
	public int updateDenpyouKianHimozukeForKianSyuryo(String denpyouId, String kianSyuryoFlg){

		// 既存レコードをロックする。
		this.selectDenpyouKianHimozukeForUpdate(denpyouId);

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("UPDATE denpyou_kian_himozuke SET")
				 .append(" kian_syuryo_flg = ?");

		// 起案終了日を設定する。
		if ("1".equals(kianSyuryoFlg)){
			// 起案終了の場合はカレント日付を設定
			sbMainSql.append(" ,kian_syuryo_bi = CURRENT_DATE");
		}else{
			// 起案終了解除の場合はクリア
			sbMainSql.append(" ,kian_syuryo_bi = null");
		}

		sbMainSql.append(" WHERE denpyou_id = ?");

		// レコードを更新する。
		int cntResult = connection.update(sbMainSql.toString(), kianSyuryoFlg, denpyouId);
		return cntResult;
	}

	/**
	 * 伝票起案紐付レコード更新（稟議金額）<br>
	 * 指定された伝票IDに合致するレコードの稟議金額を更新する。
	 *
	 * @param denpyouId 伝票ID
	 * @param ringiKingaku 稟議金額
	 * @param hikitsugimotoDenpyouId 稟議金額引継ぎ元伝票ID
	 * @param comment 稟議金額超過コメント
	 * @return 更新件数
	 */
	public int updateDenpyouKianHimozukeForRingiKingaku(String denpyouId, BigDecimal ringiKingaku, String hikitsugimotoDenpyouId, String comment){

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("UPDATE denpyou_kian_himozuke SET")
				 .append(" ringi_kingaku = ?")
				 .append(",ringi_kingaku_hikitsugimoto_denpyou = ?")
				 .append(",ringi_kingaku_chouka_comment = ?")
				 .append(" WHERE denpyou_id = ?");

		// レコードを更新する。
		int cntResult = connection.update(sbMainSql.toString(), ringiKingaku, hikitsugimotoDenpyouId, comment, denpyouId);

		return cntResult;
	}

	/**
	 * 起案番号採番登録<br>
	 * 画面で指定された起案番号簿情報を伝票起案紐付けに反映する。<br>
	 *
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param dialogFlg 起案番号採番ダイアログ表示フラグ
	 * @param bumonCd 部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianbangouFrom 開始起案番号
	 */
	public void registKianbangou(String denpyouKbn, String denpyouId, String dialogFlg, String bumonCd, String nendo, String ryakugou, String kianbangouFrom){
		this.log.debug("/_/_/_/_/_/ 起案番号採番 /_/_/_/_/_/");
		this.log.debug("伝票区分=" + denpyouKbn);
		this.log.debug("伝票ID=" + denpyouId);
		this.log.debug("起案番号採番ダイアログ表示要否=" + dialogFlg);
		this.log.debug("起案番号採番ダイアログ選択値（部門コード）=" + bumonCd);
		this.log.debug("起案番号採番ダイアログ選択値（年度）=" + nendo);
		this.log.debug("起案番号採番ダイアログ選択値（略号）=" + ryakugou);
		this.log.debug("起案番号採番ダイアログ選択値（開始起案番号）=" + kianbangouFrom);

		// 起案番号簿選択ダイアログを表示しない場合は処理を行わない。
		if (!"1".equals(dialogFlg)){
			return;
		}

		// 使用部品
		KianBangouBoHenkouLogic kianBangouBoHenkouLogic = EteamContainer.getComponent(KianBangouBoHenkouLogic.class, super.connection);
		this.denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, super.connection);

		/*
		 * テーブルからレコードを取得する。
		 */
		int iKianbangouFrom = Integer.valueOf(kianbangouFrom).intValue();

		// 起案番号採番
		GMap mapKianbangouSaiban = kianBangouBoHenkouLogic.selectKianbangouSaibanForUpdate(bumonCd, nendo, ryakugou, iKianbangouFrom);
		// 予算執行対象
		String yosanShikkouTaishou = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);

		/*
		 * 起案番号を採番する。
		 */
		int newKianbangou = 0;

		// 起案番号（最終起案番号）を取得する。
		Object objKianbangouLast = null;
		if (null != mapKianbangouSaiban){
			objKianbangouLast = mapKianbangouSaiban.get("kian_bangou_last");
		}

		if (null == objKianbangouLast){
			// 起案番号（最終起案番号）が null（採番未済）の場合

			// 起案番号（最終起案番号）に開始起案番号を設定する。
			newKianbangou = iKianbangouFrom;

		}else {
			// 起案番号（最終起案番号）が null 以外の場合、値を＋１加算する。
			newKianbangou = Integer.valueOf(objKianbangouLast.toString()).intValue() + 1;
		}

		// 起案番号（最終起案番号）を更新する。
		if (null == mapKianbangouSaiban){
			kianBangouBoHenkouLogic.insertKianbangouSaiban(bumonCd, nendo, ryakugou, iKianbangouFrom, newKianbangou);
		}else{
			kianBangouBoHenkouLogic.updateKianbangouSaiban(bumonCd, nendo, ryakugou, iKianbangouFrom, newKianbangou);
		}

		/*
		 * 伝票起案紐付けを更新する。
		 */

		// 既存レコードをロックする。
		GMap aMap = this.selectDenpyouKianHimozukeForUpdate(denpyouId);

		// 起案番号簿文字列を生成する。
		String kianbangouZenkaku = convKianbangouToZenkaku(newKianbangou);
		String strKianbangouBo = String.format("%s %s号", ryakugou, kianbangouZenkaku);

		// 予算執行対象により設定する年度、起案番号項目を変える
		String jisshiNendo = (String)aMap.get("jisshi_nendo");
		String jisshiKianbangou = (String)aMap.get("jisshi_kian_bangou");
		String shishutsuNendo = (String)aMap.get("shishutsu_nendo");
		String shishutsuKianbangou = (String)aMap.get("shishutsu_kian_bangou");
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(yosanShikkouTaishou)){
			jisshiNendo = nendo;
			jisshiKianbangou = strKianbangouBo;
		}
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou)){
			shishutsuNendo = nendo;
			shishutsuKianbangou = strKianbangouBo;
		}
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishou)){
			jisshiNendo = nendo;
			jisshiKianbangou = strKianbangouBo;
		}

		// レコードを更新する。
		// 前回と異なる部門コード、シリアル番号である場合、既存の設定情報は上書きされる（起案番号は捨てられる）
		this.updateDenpyouKianHimozuke(denpyouId, bumonCd, nendo, ryakugou, iKianbangouFrom, newKianbangou, jisshiNendo, jisshiKianbangou, shishutsuNendo, shishutsuKianbangou);
	}

	/**
	 * 起案番号全角文字変換<br>
	 * 起案番号を全角文字に変換する。
	 *
	 * @param kianbangou 起案番号（半角）
	 * @return 起案番号（全角）
	 */
	public static String convKianbangouToZenkaku(int kianbangou) {
		// int を String[] に変換
		char[] noCharArray = String.valueOf(kianbangou).toCharArray();
		String[] noArray = new String[noCharArray.length];
		for (int i = 0; i < noCharArray.length; i++){
			noArray[i] = String.valueOf(noCharArray[i]);
		}
		// 半角文字を全角文字に変換する
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < noArray.length; i++){
			switch (noArray[i]){
			case "0":sb.append("０");break;
			case "1":sb.append("１");break;
			case "2":sb.append("２");break;
			case "3":sb.append("３");break;
			case "4":sb.append("４");break;
			case "5":sb.append("５");break;
			case "6":sb.append("６");break;
			case "7":sb.append("７");break;
			case "8":sb.append("８");break;
			case "9":sb.append("９");break;
			default:sb.append(noArray[i]);break;
			}
		}
		return sb.toString();
	}

	/**
	 * 予算・起案金額チェックデータを登録する
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param idYosanCheck 予算チェック対象
	 * @param nendo 年度
	 */
	public void insertYosanKianKingakuCheck(String denpyouId, String denpyouKbn, boolean idYosanCheck, String nendo){

		 YosanShikkouKanriCategoryLogic yosanKanriLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, super.connection);

		 List<GMap> dataList = null;
		 int overRate = 0;

		//予算チェック対象の場合
		if(idYosanCheck){
			YosanCheckLogic yCheckLogic = EteamContainer.getComponent(YosanCheckLogic.class, super.connection);
			GMap mapKianInfo = selectDenpyouKianHimozuke(denpyouId);
			String jisshi_nendo = (String)mapKianInfo.get("jisshi_nendo");
			dataList = yCheckLogic.loadData(denpyouId, denpyouKbn, jisshi_nendo);
			overRate = "".equals(setting.yosanChoukaHantei())? 0 : Integer.parseInt(setting.yosanChoukaHantei());

		//起案金額チェック対象の場合
		}else{
			KianKingakuCheckLogic kCheckLogic = EteamContainer.getComponent(KianKingakuCheckLogic.class, super.connection);
			dataList = kCheckLogic.loadData(denpyouId, denpyouKbn, nendo);
			overRate = "".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.KIAN_CHOUKA_HANTEI))? 0 : Integer.parseInt(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.KIAN_CHOUKA_HANTEI));
		}

		dataList.add(yosanKanriLogic.getGoukei(dataList));
		yosanKanriLogic.setJudgeCumment(dataList, overRate);

		//超過していない部門の明細は破棄して保存する
		boolean tmpIsOver = false;
		for(GMap map : dataList){
			if(!map.containsKey(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_CD)){
				continue;
			}
			String meisaiBumonCd = map.get(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_CD).toString();
			if("".equals(meisaiBumonCd)){
				tmpIsOver = "1".equals(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE).toString());
			}
			if("".equals(meisaiBumonCd) || (!"".equals(meisaiBumonCd) && tmpIsOver)){
				yosanKanriLogic.insertData(
						denpyouId,
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_CD).toString(),
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_CD).toString(),
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_CD).toString(),
						meisaiBumonCd,
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_NAME).toString(),
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_NAME).toString(),
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_NAME).toString(),
						map.get(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_NAME).toString(),
						new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU).toString()),
						new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU).toString()),
						new BigDecimal(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU).toString()));
			}
		}
	}

	/**
	 * 稟議金額の表示に関するデータを取得する
	 * @param denpyouId 伝票ID
	 * @return 結果マップ
	 */
	public GMap loadRingiKingakuData(String denpyouId){

		GMap motoDenpyouInfo = new GMap();

		// 稟議金額の引継有無を確認する。
		GMap mapHimozuke = this.selectDenpyouKianHimozuke(denpyouId);
		if (mapHimozuke == null){
			return null;
		}
		BigDecimal bdRingiKingaku = (BigDecimal)mapHimozuke.get("ringi_kingaku");
		if (bdRingiKingaku == null){
			// 稟議金額に設定がない時は処理を終了する。
			return null;
		}

		// 稟議金額の引継ぎ元を特定
		motoDenpyouInfo = wfLogic.findRingiMotoDenpyouId(motoDenpyouInfo, denpyouId);
		String motoDenpyouId = motoDenpyouInfo.get("ringi_kingaku_hikitsugimoto_denpyou_base").toString();

		List<String> hikisugisakiDenpyouList = new ArrayList<String>();
		for(GMap map : wfLogic.loadRingiSakiDenpyouId(new ArrayList<GMap>(), motoDenpyouId)){
			if(!hikisugisakiDenpyouList.contains(map.get("denpyou_id").toString())){
				hikisugisakiDenpyouList.add(map.get("denpyou_id").toString());
			}
		}

		// 稟議金額残高とラベル名称を取得
		return this.loadRingiKingakuData(motoDenpyouId, hikisugisakiDenpyouList, mapHimozuke);
	}

	/**
	 * 登録／更新前の稟議金額関連情報を取得する。
	 * 引継ぎ元の稟議関連情報はDBから取得するが、操作中の伝票の情報が画面入力から取得する。
	 * @param denpyouId 伝票ID
	 * @param ringiData 稟議関連項目マップ
	 * @return 結果マップ
	 */
	public  GMap loadRingiKingakuDataBeforeUpdate(String denpyouId, GMap ringiData){

		GMap motoDenpyouInfo = new GMap();

		// 稟議金額の引継ぎ元を特定
		motoDenpyouInfo = wfLogic.findRingiMotoDenpyouId(motoDenpyouInfo, ringiData.get("ringi_kingaku_hikitsugimoto_denpyou").toString());
		String motoDenpyouId = motoDenpyouInfo.get("ringi_kingaku_hikitsugimoto_denpyou_base").toString();

		List<String> hikisugisakiDenpyouList = new ArrayList<String>();
		for(GMap map : wfLogic.loadRingiSakiDenpyouId(new ArrayList<GMap>(), motoDenpyouId)){
			if(!hikisugisakiDenpyouList.contains(map.get("denpyou_id").toString())){
				hikisugisakiDenpyouList.add(map.get("denpyou_id").toString());
			}
		}
		// 登録・更新処理操作中の伝票はリストから削除
		if(hikisugisakiDenpyouList.contains(denpyouId)) hikisugisakiDenpyouList.remove(denpyouId);

		// 稟議金額残高（操作中の伝票の申請金額は含まない）とラベル名称を取得
		return this.loadRingiKingakuData(motoDenpyouId, hikisugisakiDenpyouList, ringiData);
	}

	/**
	 * 稟議金額の表示に関するデータを取得する
	 * @param denpyouId 伝票ID
	 * @param hikisugisakiDenpyouList 稟議金額の引継ぎ先伝票IDリスト
	 * @param ringiData 稟議情報マップ
	 * @return 結果マップ
	 */
	public GMap loadRingiKingakuData(String denpyouId, List<String> hikisugisakiDenpyouList, GMap ringiData){

		// ラベル名称
		ringiData.put("ringiKingakuName", getRingiKingakuName(denpyouId));

		BigDecimal seisanKingakuGoukei = calShinseiKingakuTotal(hikisugisakiDenpyouList);
		BigDecimal bdRingiKingaku = (BigDecimal)ringiData.get("ringi_kingaku");
		ringiData.put("ringiKingakuZandaka", bdRingiKingaku.subtract(seisanKingakuGoukei));

		return ringiData;

	}

	/**
	 * 稟議金額ラベル文言を取得する。
	 * @param denpyouId 伝票ID
	 * @return ラベル文言
	 */
	protected String getRingiKingakuName(String denpyouId){
		String result = wfLogic.findKaniTodokeLabelName(denpyouId, YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU);
		return "".equals(result)? "稟議金額" : result;
	}

	/**
	 * 稟議金額を引き継いだ伝票の申請金額合計を算出する。
	 * @param hikitsugiDenpyouList 稟議金額を引き継いだ伝票の伝票IDリスト
	 * @return 申請金額合計
	 */
	public BigDecimal calShinseiKingakuTotal(List<String> hikitsugiDenpyouList){
		GMap mapFind = null;
		BigDecimal seisanKingakuGoukei = BigDecimal.valueOf(0);

		for(int i = 0; i < hikitsugiDenpyouList.size()  ;i++){
			String denpyouId = hikitsugiDenpyouList.get(i).toString();
			GMap denpyouMap = wfLogic.selectDenpyou(denpyouId);
			String denpyouKbn = denpyouMap.get("denpyou_kbn").toString();

			switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			case DENPYOU_KBN.KARIBARAI_SHINSEI:
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
			case DENPYOU_KBN.RYOHI_SEISAN:
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
			case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
			case DENPYOU_KBN.FURIKAE_DENPYOU:
			case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU:
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
			case DENPYOU_KBN.SIHARAIIRAI:

				//有効な伝票かどうか判断する
				switch(denpyouMap.get("denpyou_joutai").toString()){
				case DENPYOU_JYOUTAI.KIHYOU_CHUU:
				case DENPYOU_JYOUTAI.SHINSEI_CHUU:
				case DENPYOU_JYOUTAI.SYOUNIN_ZUMI:

					if(null != mapFind) mapFind.clear();
					GMap mapWithKaribarai;
					switch (denpyouKbn){
					case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
						// A001 経費立替精算
						mapFind = kaikeiLogic.findKeihiSeisan(denpyouId);
						if("".equals(mapFind.get("karibarai_denpyou_id")) || "1".equals(mapFind.get("karibarai_on"))){
							seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("shiharai_kingaku_goukei"));
						}
						break;
					case DENPYOU_KBN.KARIBARAI_SHINSEI:
						// A002 仮払申請
						mapFind = kaikeiLogic.findKaribarai(denpyouId);
						//未精算の場合
						mapWithKaribarai = kaikeiLogic.findKeihiSeisanWithKaribarai(denpyouId);
						if(null == mapWithKaribarai || false == hikitsugiDenpyouList.contains(mapWithKaribarai.get("denpyou_id").toString())){
							if (null != mapFind.get("karibarai_kingaku")){
								seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("karibarai_kingaku"));
							}
						}
						break;
					case DENPYOU_KBN.SEIKYUUSHO_BARAI:
						// A003 請求書払い申請
						mapFind = kaikeiLogic.findSeikyuushobarai(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("shiharai_kingaku_goukei"));
						break;
					case DENPYOU_KBN.RYOHI_SEISAN:
						// A004 出張旅費精算
						mapFind = kaikeiLogic.findRyohiSeisan(denpyouId);
						if("".equals(mapFind.get("karibarai_denpyou_id")) || "1".equals(mapFind.get("karibarai_on"))){
							seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("goukei_kingaku"));
						}
						break;
					case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
						// A005 出張伺い申請
						mapFind = kaikeiLogic.findRyohiKaribarai(denpyouId);
						//未精算の場合
						mapWithKaribarai = kaikeiLogic.findRyohiSeisanWithKaribarai(denpyouId);
						if(null == mapWithKaribarai || false == hikitsugiDenpyouList.contains(mapWithKaribarai.get("denpyou_id").toString())){
							if (null != mapFind.get("karibarai_kingaku")){
								seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("karibarai_kingaku"));
							}
						}
						break;
					case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
						// A006 通勤定期申請
						mapFind = kaikeiLogic.findTsuukinnteiki(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("kingaku"));
						break;
					case DENPYOU_KBN.FURIKAE_DENPYOU:
						// A007 振替伝票
						mapFind = kaikeiLogic.findFurikae(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("kingaku"));
						break;
					case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU:
						// A008 総合付替伝票
						mapFind = kaikeiLogic.findTsukekae(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("kingaku_goukei"));
						break;
					case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
						// A009 自動引落伝票
						mapFind = kaikeiLogic.findJidouhikiotoshi(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("shiharai_kingaku_goukei"));
						break;
					case DENPYOU_KBN.KOUTSUUHI_SEISAN:
						// A010 交通費精算
						mapFind = kaikeiLogic.findKoutsuuhiSeisan(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("goukei_kingaku"));
						break;
					case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
						// A011 海外出張旅費精算
						mapFind = kaikeiLogic.findKaigaiRyohiSeisan(denpyouId);
						if("".equals(mapFind.get("karibarai_denpyou_id")) || "1".equals(mapFind.get("karibarai_on"))){
							seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("goukei_kingaku"));
						}
						break;
					case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
						// A012 海外出張伺い申請
						mapFind = kaikeiLogic.findKaigaiRyohiKaribarai(denpyouId);
						//未精算の場合
						mapWithKaribarai = kaikeiLogic.findKaigaiRyohiSeisanWithKaribarai(denpyouId);
						if(null == mapWithKaribarai || false == hikitsugiDenpyouList.contains(mapWithKaribarai.get("denpyou_id").toString())){
							if (null != mapFind.get("karibarai_kingaku")){
								seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("karibarai_kingaku"));
							}
						}
						break;
					case DENPYOU_KBN.SIHARAIIRAI:
						// A013 支払依頼申請
						mapFind = kaikeiLogic.findShiharaiIrai(denpyouId);
						seisanKingakuGoukei = seisanKingakuGoukei.add((BigDecimal)mapFind.get("sashihiki_shiharai"));
						break;
					}

				default:
					//伝票が無効な場合は加算しない
					break;
				}

				break;
			default:
				//ユーザー定義届書の場合は加算しない
				break;
			}
		}

		return seisanKingakuGoukei;
	}
	
	/**
	 * 税 修正ボタンの表示有無を取得
	 * @param denpyouKbn 伝票区分
	 * @return 消費税額設定フラグの設定値（0:修正不可 1:修正ボタン押下で修正可 2:修正可）
	 */
	public String judgeZeigakuShusei(String denpyouKbn) {
		switch (denpyouKbn) {
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			return setting.zeinukiZeigakuHenkouA001();
		case DENPYOU_KBN.SEIKYUUSHO_BARAI:
			return setting.zeinukiZeigakuHenkouA003();
		case DENPYOU_KBN.RYOHI_SEISAN:
			return setting.zeinukiZeigakuHenkouA004();
		case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
			return setting.zeinukiZeigakuHenkouA006();
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			return setting.zeinukiZeigakuHenkouA009();
		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			return setting.zeinukiZeigakuHenkouA010();
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			return setting.zeinukiZeigakuHenkouA011();
		case DENPYOU_KBN.SIHARAIIRAI:
			return setting.zeinukiZeigakuHenkouA013();
		default:
		}
		return null;
	}
	
	/**
	 * 税 入力方式初期値フラグを取得
	 * @param denpyouKbn 伝票区分
	 * @return 初期値が税抜入力ならtrue
	 */
	public boolean judgeNyuryokuDefault(String denpyouKbn) {
		//伝票区分により分岐
			switch (denpyouKbn) {
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
				return "1".equals(setting.zeiDefaultA003());
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				return "1".equals(setting.zeiDefaultA009());
			case DENPYOU_KBN.SIHARAIIRAI:
				return "1".equals(setting.zeiDefaultA013());
			default:
			}
		return false;
	}
	
	/**
	 * 税 入力方式変更可否フラグを取得
	 * @param denpyouKbn 伝票区分
	 * @return 入力方式変更フラグONならtrue
	 */
	public boolean judgeNyuryokuHoushiki(String denpyouKbn) {

		//伝票区分により分岐
			switch (denpyouKbn) {
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
				return "1".equals(setting.zeiHenkouA003());
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				return "1".equals(setting.zeiHenkouA009());
			case DENPYOU_KBN.SIHARAIIRAI:
				return "1".equals(setting.zeiHenkouA013());
			default:
			}
		return false;
	}
	
	/**
	 * 事業者区分 変更可否
	 * @param denpyouKbn 伝票区分
	 * @return 事業者区分の変更を許可する場合ならtrue
	 */
	public boolean judgeJigyoushaKbnHenkou(String denpyouKbn) {
		//伝票区分により分岐
			switch (denpyouKbn) {
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
				return "1".equals(setting.jigyoushaHenkouFlgA003());
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				return "1".equals(setting.jigyoushaHenkouFlgA009());
			case DENPYOU_KBN.SIHARAIIRAI:
				return "1".equals(setting.jigyoushaHenkouFlgA013());
			default:
			}
		return true;
	}
	
	/**
	 * インボイス制度開始設定
	 * @return 「開始」済の場合true
	 */
	public String judgeinvoiceStart() {
		//インボイス開始テーブルに値がない、またはインボイス開始フラグの値が「0」の場合"0"
		//インボイス開始フラグが「1」の場合"1"
		InvoiceStart result = this.invoiceStartDao.find();
		return result == null ? "0" : result.invoiceFlg;
	}
	
	
	/**
	 * 消費税額 端数処理設定
	 * @return 0:切り捨て、2:四捨五入
	 */
	public String judgeHasuuKeisan() {
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);//期別消費税設定
		return dto.shiirezeigakuKeisan == 1 && dto.hasuuShoriFlg == 1
				? setting.zeigakuHasuuHenkanFlg() //財務システム側で「仕入税額計算方法：積上計算(1)」且つ「税額端数処理：切り上げ(1)」に設定している場合、WFの設定
				: Integer.toString(dto.hasuuShoriFlg); // 上記以外なら財務の設定
	}
}
