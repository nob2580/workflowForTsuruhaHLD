package eteam.gyoumu.masterkanri;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.base.exception.EteamSQLExceptionHandler;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Open21KessankiBangou;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN_KYOTEN;
import eteam.common.RegAccess;
import eteam.common.open21.Open21Env;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.SuitouchouSettingDao;
import eteam.gyoumu.kaikei.TorihikiLogic;
import lombok.Getter;
import lombok.Setter;

/**
 * マスター取込バッチ
 */
public class MasterTorikomiBat extends EteamAbstractBat {

//＜定数＞
	/** 正常終了 */
	static final int SUCCESS = 0;
	/** 異常終了 */
	static final int ERROR   = 1;

//＜メンバ変数＞
	//Term側でしか使わない変数なのだが、構造上こっちでもつ
	/** 処理月初日 */
	@Getter @Setter
	Date targetMonth;
	/** 対象日(開始日) */
	@Getter @Setter
	Date targetDateFrom;
	/** 対象日(終了日) */
	@Getter @Setter
	Date targetDateTo;

	/** CSV作成用一時テーブル行番号保持用 */
	int row_num = 0;

//＜部品＞
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(MasterTorikomiBat.class);
	/** マスター取込Logic */
	MasterTorikomiLogic myLogic;
	/** マスターカテゴリSELECT */
	MasterKanriCategoryLogic masterKanriLogic;
	/** PostgreSQLカテゴリSELECT */
	PostgreSQLSystemCatalogsLogic postgreSystemLogic;
	/** システムカテゴリSELECT */
	SystemKanriCategoryLogic systemKanriLogic;
	/** 取引ロジック */
	TorihikiLogic torihikiLogic;
	/** 仕訳パターンマスターDAO */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** suitouchouSettingDao */
	SuitouchouSettingDao suitouchouSettingDao;
	/** コネクション(諸届) */
	EteamConnection connection;
	/** コネクション(OPEN21) */
	EteamConnection connectionOPEN21;
	/** コネクション(OPEN21 共通DB) */
	EteamConnection connectionOPEN21_Common;
	/** コネクション(SIAS) */
	EteamConnection connectionSIAS;
	/** コネクション(SIAS 債権債務) */
	EteamConnection connectionSIAS_SAIKEN;
	/** コネクション(SIAS 共通DB) */
	EteamConnection connectionSIAS_Common;

	/**
	 * バッチ処理メイン
	 * @param argv 0:スキーマ名
	 */
	public static void main(String[] argv) {
		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		//スキーマ指定
		if (1 != argv.length) {
			throw new IllegalArgumentException("パラメータにスキーマ名が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);

		//実行
		MasterTorikomiBat bat = EteamContainer.getComponent(MasterTorikomiBat.class);
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "マスター取込";
	}

	@Override
	public String getCountName() {
		return "マスターレコード数";
	}

	/**
	 * 処理対象タイプを取得します。
	 * @return 処理対象(1:通常マスター取込 2:経費明細データ更新)
	 */
	protected int getProcType() {
		return 1;
	}

	@Override
	public int mainProc() {
		try {
			// eteamへ接続
			connection = EteamConnection.connect();
			myLogic = EteamContainer.getComponent(MasterTorikomiLogic.class, connection);
			masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			postgreSystemLogic = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class, connection);
			systemKanriLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			torihikiLogic = EteamContainer.getComponent(TorihikiLogic .class, connection);
			shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
			suitouchouSettingDao = EteamContainer.getComponent(SuitouchouSettingDao.class, connection);

			// マスター取込一覧から「1:取込可」のデータを取得
			List<GMap> masterIchiranList = masterKanriLogic.selectMasterIchiran(getProcType());
			if (masterIchiranList.size() == 0) {
				log.info("取込対象マスターが 0 件の為、処理を終了します。");
				return SUCCESS;
			}
			log.info("取込対象マスターは " + masterIchiranList.size() + " 件です。");

			int insertedRecordCount = 0;

			//使用環境により分岐
			switch (Open21Env.getVersion()) {

			//de3環境
			case DE3 :

				// OPEN21(de3)へDB接続
				connectionOPEN21 = EteamConnection.connect4De3Type4();
				connectionOPEN21_Common = EteamConnection.connect4De3Type4Common();

				// マスターIDの単位に取込処理
				for(GMap masterIchiranMap : masterIchiranList) {
					//普通のマスター取込
					if (getProcType() == 1) {
						int cnt = masterTorikomi_de3(masterIchiranMap);
						if (-1 == cnt)
						{
							return ERROR;
						}
						insertedRecordCount += cnt;
					//経費明細
					} else {
						GMap kesn = masterKanriLogic.findKiKesn(EteamCommon.d2d(getTargetMonth()));
						if(null != kesn) {
							setTargetDateFrom(kesn.get("from_date"));
							setTargetDateTo(kesn.get("to_date"));
							int cnt = masterTorikomi_de3(masterIchiranMap);
							if (-1 == cnt)
							{
								return ERROR;
							}
							insertedRecordCount += cnt;
						}else {
							log.info("処理年月" + getTargetMonth() + "は決算期が未登録のため処理をスキップします。");
						}
					}
				}
				if(RegAccess.checkEnableZaimuKyotenOption())updateSuitouchouKaishiZandaka();
				myLogic.afterUpdate();
				break;

			//SIAS環境
			case SIAS :

				// OPEN21(SIAS)へDB接続
				//SIAS使用DBにより更に分岐
				if(Open21Env.getSIASDBEngine() == Open21Env.SIASDBEngine.SQLSERVER) {
					connectionSIAS = EteamConnection.connect4SIAS();
					connectionSIAS_SAIKEN = EteamConnection.connect4SIASsaiken();
					connectionSIAS_Common = EteamConnection.connect4SIASCommon();
				}else if(Open21Env.getSIASDBEngine() == Open21Env.SIASDBEngine.POSTGRESQL) {
					connectionSIAS = EteamConnection.connect4SIASMK2();
					connectionSIAS_SAIKEN = EteamConnection.connect4SIASMK2saiken();
					connectionSIAS_Common = EteamConnection.connect4SIASMK2Common();
				}else {
					return ERROR;
				}

				// マスターIDの単位に取込処理
				for(GMap masterIchiranMap : masterIchiranList) {
					//普通のマスター取込
					if (getProcType() == 1) {
						int cnt = masterTorikomi_sias(masterIchiranMap);
						if (-1 == cnt)
						{
							return ERROR;
						}
						insertedRecordCount += cnt;
					//経費明細
					} else {
						GMap kesn = masterKanriLogic.findKiKesn(EteamCommon.d2d(getTargetMonth()));
						if(null != kesn) {
							setTargetDateFrom(kesn.get("from_date"));
							setTargetDateTo(kesn.get("to_date"));
							int cnt = masterTorikomi_sias(masterIchiranMap);
							if (-1 == cnt)
							{
								return ERROR;
							}
							insertedRecordCount += cnt;
						}else {
							log.info("処理年月" + getTargetMonth() + "は決算期が未登録のため処理をスキップします。");
						}
					}
				}
				if(RegAccess.checkEnableZaimuKyotenOption())updateSuitouchouKaishiZandaka();
				myLogic.afterUpdate();
				break;

			default:
				//入らないはず
				return ERROR;
			}

			// 会社情報を見て取引の整合性とる
			GMap kaishaInfo = systemKanriLogic.findKaishaInfo();
			if("0".equals(kaishaInfo.get("pjcd_shiyou_flg").toString())) myLogic.updateshiwakePForPjCd();
			if("0".equals(kaishaInfo.get("sgcd_shiyou_flg").toString())) myLogic.updateshiwakePForSgCd();
			for (int i = 1; i <= 10; i++) {
				if("0".equals(kaishaInfo.get("uf" + i + "_shiyou_flg").toString())) this.shiwakePatternMasterDao.clearUf(i, null, "batch");
			}
			for (int i = 1; i <= 10; i++) {
				if("0".equals(kaishaInfo.get("uf_kotei" + i + "_shiyou_flg").toString())) this.shiwakePatternMasterDao.clearUfKotei(i, null, "batch");
			}

			// コミット
			connection.commit();
			setCount(insertedRecordCount);

			return SUCCESS;
		} catch (Throwable e) {
			log.error("エラー発生", e);
			return ERROR;
		} finally {
			if (connection != null) connection.close();
			if (connectionOPEN21 != null) connectionOPEN21.close();
			if (connectionOPEN21_Common != null) connectionOPEN21_Common.close();
			if (connectionSIAS != null) connectionSIAS.close();
			if (connectionSIAS_SAIKEN != null) connectionSIAS_SAIKEN.close();
			if (connectionSIAS_Common != null) connectionSIAS_Common.close();
		}
	}

	/**
	 *
	 * @param masterIchiranMap マスター一覧テーブルのレコード
	 * @return 取込件数(0～)
	 * @throws ParseException フォーマット変換
	 * @throws IOException CSVの作成
	 */
	protected int masterTorikomi_de3(GMap masterIchiranMap) throws ParseException, IOException {

		// <<<masterTorikomi(元処理)からの変更点>>>
		//  ・可読性の為の修正
		//  ・翌期・当期の重複防止のNOT IN指定が、PK全体じゃなくてPK項目単位でやっているので不十分
		//  ・「取引先」(債務)の取得でORDER句が指定されていなかったが、PKのORDER指定をする

		//#################################################
		// よく使う項目を取り出しておく
		String etMasterId   = masterIchiranMap.get("master_id").toString();
		String etMasterName = masterIchiranMap.get("master_name").toString();
		String opMasterId   = masterIchiranMap.get("op_master_id").toString();
		log.info("###############");
		log.info(etMasterName + "の処理を開始します。");


		//#################################################
		// 取込先のテーブルが存在すること、マスター管理一覧に登録されていることをチェック
		if(! postgreSystemLogic.tableSonzaiCheck(etMasterId)) {
			throw new RuntimeException(etMasterName + "のテーブルが存在しません。");
		}
		if(masterKanriLogic.findMasterKanriHansuuMaxV(etMasterId) == null && getProcType() == 1) {
			throw new RuntimeException(etMasterName + "がマスター管理一覧、もしくはマスター管理版数に登録されていません。");
		}


		//#################################################
		// マスター取込詳細のデータを取得：OPEN21→eteamのマッピング情報
		List<GMap> masterShousaiList = masterKanriLogic.selectMasterShousai(etMasterId, getProcType());


		//#################################################
		//マスター取込詳細から取込元(OPEN21)に決算期があるか、科目外部コードがあるかを確認
		//PKのリスト(a,b,,,)と全カラムのリスト(a,b,,,)の文字列生成
		boolean findKessankiNo    = false;
		boolean findGaibukamokuCd = false;
		ArrayList<String> opPkList = new ArrayList<String>(); //KIを除くOPEN21のPK
		ArrayList<String> opColmnList = new ArrayList<String>();

		for(GMap masterShousai : masterShousaiList) {
			if("KI".equals(masterShousai.get("op_colume_id"))) findKessankiNo = true;
			if("KCOD".equals(masterShousai.get("op_colume_id"))) findGaibukamokuCd = true;
			if("1".equals(masterShousai.get("pk_flg")) && !("KI".equals(masterShousai.get("op_colume_id"))) ) opPkList.add(masterShousai.get("op_colume_id").toString());
			opColmnList.add(masterShousai.get("op_colume_id").toString());
		}
		String opPks = list2Str(opPkList); //KIを除くOPEN21のPK
		String opColumns  = list2Str(opColmnList);
		if (etMasterId.equals("segment_master"))
		{
			findKessankiNo = true;
		} //邪道だったけどまあいいでしょう。


		//#################################################
		// OPEN21のデータ取得用のSQLを作成
		String selectSql;
		if(findKessankiNo && !(etMasterId.equals("kaisha_info")) && ! etMasterId.startsWith("ki_")
				&& !(etMasterId.equals("kamoku_zandaka"))
				&& !(etMasterId.equals("edaban_zandaka"))
				&& !(etMasterId.equals("bumon_kamoku_yosan"))
				&& !(etMasterId.equals("bumon_kamoku_zandaka"))
				&& !(etMasterId.equals("bumon_kamoku_edaban_yosan"))
				&& !(etMasterId.equals("bumon_kamoku_edaban_zandaka")) ) {
			// 取得元(OPEN21)に決算期番号がある場合、翌期(KI=0)と当期(KI=1)両方からSELECTするが、翌期と当期でキー重複あれば翌期優先で取得
			selectSql =
				"WITH union_table AS ( "
			+ "  SELECT * FROM :TABLE k0 WHERE KI = 0 "
			+ "  UNION "
			+ "  SELECT * FROM :TABLE k1 WHERE KI = 1 AND STRING(:PK_COLUMNS) NOT IN (SELECT STRING(:PK_COLUMNS) FROM :TABLE tmp WHERE KI = 0) "
			+ ") "
			+ "SELECT :COLUMNS "
			+ "FROM union_table "
			+ ":WHERE "
			+ ":ORDER ";
		} else {
			// 取得元(OPEN21)に決算期番号がない場合、単にSELECT
			selectSql =
				"SELECT :COLUMNS "
			+ "FROM :TABLE tmp "
			+ ":WHERE "
			+ ":ORDER ";
		}
		selectSql = selectSql.replace(":TABLE"		, opMasterId);
		selectSql = selectSql.replace(":PK_COLUMNS"	, opPks);
		selectSql = selectSql.replace(":COLUMNS"	, opColumns);
		if(findGaibukamokuCd) {
			selectSql = selectSql.replace(":WHERE"	, "WHERE KCOD IS NOT NULL AND KCOD <> ''");
		} else {
			selectSql = selectSql.replace(":WHERE"	, "");
		}

		if(getProcType() == 2 && etMasterId.equals("ki_shiwake")) {
			// 期間指定反映の場合は指定された日付を含む月のみを反映する
			selectSql = selectSql.replace(":DYMD_START", new SimpleDateFormat("yyyyMMdd").format(getTargetDateFrom()));
			selectSql = selectSql.replace(":DYMD_END", new SimpleDateFormat("yyyyMMdd").format(getTargetDateTo()));
		}

		if(opPks.equals("")){
			selectSql = selectSql.replace(":ORDER"	, "");
		}else if(findKessankiNo) {
			selectSql = selectSql.replace(":ORDER"	, "ORDER BY KI, " + opPks);
		} else {
			selectSql = selectSql.replace(":ORDER"	, "ORDER BY " + opPks);
		}


		if (etMasterId.equals("bumon_kamoku_yosan")) {
			//部門科目予算取得用テーブル切替
			String tmpStr = setting.yosanYosanNo();
			selectSql = selectSql.replace(":BKYSN", "BKYSN" + tmpStr );
		}
		if (etMasterId.equals("bumon_kamoku_edaban_yosan")) {
			//部門科目枝番予算取得用テーブル切替
			String tmpStr = setting.yosanYosanNo();
			selectSql = selectSql.replace(":BKEYSN", "BKEYSN" + tmpStr );
		}



		log.info("作成したSQLは「" + selectSql.toString() + "」です。");


		//#################################################
		// OPEN21からデータ取得
		//List<GMap> open21DataList = connectionOPEN21.load(selectSql, params.toArray());
		ResultSet open21ResultSet;
		open21ResultSet = connectionOPEN21.loadFetchResultSet(selectSql);
		//log.info("処理データ件数は " + open21DataList.size() + " 件です。");


		//#################################################
		// OPEN21から取得したデータを諸届のマスターへ登録

		// まずは全レコード削除
		if (getProcType() == 1) {
			myLogic.delete(etMasterId);
		} else {
			myLogic.deleteTerm(etMasterId, new java.sql.Date(getTargetDateFrom().getTime()), new java.sql.Date(getTargetDateTo().getTime()));
		}

		// 各テーブルの登録用SQLを作成します。
		String insertSql = "INSERT INTO :TABLE VALUES(:PARAMS)";
		insertSql = insertSql.replace(":TABLE"	, etMasterId);
		insertSql = insertSql.replace(":PARAMS"	, makeParams(opColmnList.size()));

		int ResultCnt = 0;

		try{
			// CSVヘッダーをTEMP_CSVMAKEテーブルに書き込み
			setTempCsvDataHeader(etMasterId,etMasterName);

			// 1行ずつINSERTする
			while(open21ResultSet.next()){
				ResultCnt++;
				Object[] dataArray = new Object[masterShousaiList.size()];
				for(int i = 0; i < masterShousaiList.size(); i++) {
					GMap shousaiMap = masterShousaiList.get(i);
					String etColumnId = (String)shousaiMap.get("et_column_id");
					String opColumnId = shousaiMap.get("op_colume_id").toString();
					Object columnObj = open21ResultSet.getObject(opColumnId);

					// 部門仕入 共売の値 0→3へ置き換え
					if(etMasterId.equals("bumon_master") && opColumnId.equals("SY05") && columnObj != null)
					{
						if (columnObj.toString().equals("0"))
						{
							columnObj = "3";
						}
					}
					// カラムの型によって、Stringから各データ型へ変更します。
					switch (shousaiMap.get("et_data_type").toString()) {
					case "varchar":
						dataArray[i] = columnObj == null ? "" : columnObj.toString();
						break;
					case "int":
						dataArray[i] = columnObj == null ? null : Integer.parseInt(columnObj.toString());
						break;
					case "smallint":
						dataArray[i] = columnObj == null ? null : Integer.parseInt(columnObj.toString());
						if (columnObj == null && (etColumnId.endsWith("flg") || etColumnId.endsWith("check"))) columnObj = Integer.parseInt("0");
						break;
					case "decimal":
						dataArray[i] = columnObj == null ? null : new BigDecimal(columnObj.toString().replaceAll(",", ""));
						break;
					case "date":
						dataArray[i] = columnObj == null ?
								null : new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(columnObj.toString().replaceAll("-", "")).getTime());
						break;
					}
				}
				myLogic.insert(insertSql, dataArray);
			}

			// csv作成
			createCsv(etMasterId, etMasterName);//連絡票490対応によりヘッダーだけになった
		}catch(SQLException e){
			throw EteamSQLExceptionHandler.makeException(e);
		}finally{
			//フェッチ用ResultSetなど閉じる
			connectionOPEN21.CloseFetchResultSet();
		}


		//#################################################
		// 後処理
		//会社情報の債務使用フラグと軽減有無フラグは特殊
		if(etMasterId.equals("kaisha_info")){
			String saimuKakuninSql = "SELECT COUNT(trcd) AS count FROM SS_TORI";
			String saimuFlgSql = "UPDATE kaisha_info SET saimu_shiyou_flg = ?";
			String saimuShiyouFlg = "0";
			saimuShiyouFlg = (int)connectionOPEN21.find(saimuKakuninSql).get("count") > 0 ? "1" : "0";
			connection.update(saimuFlgSql, saimuShiyouFlg);

			//軽減税率有無フラグは2020.04.10版以降は固定で1(対応済)設定
			String keigenFlgSql = "UPDATE kaisha_info SET keigen_umu_flg = ?";
			String keigenUmuFlg = "1";
			connection.update(keigenFlgSql, keigenUmuFlg);
		}


		//#################################################
		// 終わり
		log.info("処理データ件数は " + ResultCnt + " 件です。");
		log.info(etMasterName + "の処理を終了します。");
		return ResultCnt;
	}

	/**
	 *
	 * @param masterIchiranMap マスター一覧テーブルのレコード
	 * @return 取込件数(0～)
	 * @throws ParseException フォーマット変換
	 * @throws IOException CSVの作成
	 */
	protected int masterTorikomi_sias(GMap masterIchiranMap) throws ParseException, IOException {

		// <<<de3からの変更点>>>
		//  ・OPEN21接続用のコネクタをSIAS向けに変更
		//  ・データ取得SQL構成をWITH～AS形式からSELECTのサブクエリ形式に変更
		//  ・データ取得SQLのSTRING句をCONCAT句に変更(引数1つのパターンを調整するための処理も追加)

		//#################################################
		// よく使う項目を取り出しておく
		String etMasterId   = masterIchiranMap.get("master_id").toString();
		String etMasterName = masterIchiranMap.get("master_name").toString();
		String opMasterId   = masterIchiranMap.get("op_master_id").toString();
		log.info("###############");
		log.info(etMasterName + "の処理を開始します。");

		// 拠点外貨関係
		// DB存在チェック
		// 外貨対応に伴い、旧版でも動作させるために追加
		boolean isPostgre = Open21Env.getSIASDBEngine() == Open21Env.SIASDBEngine.POSTGRESQL;
		boolean isDefaultRateUsed = this.connectionSIAS.tableSonzaiCheck("defaultrate", isPostgre); // 期幣種でも使用する
		if(etMasterId.equals("default_rate_kyoten") && !isDefaultRateUsed)
		{
			log.info(etMasterName+"に対応する財務のテーブルが存在しないバージョンのため、処理をスキップします。");
			log.info(etMasterName + "の処理を終了します。");
			return 0;
		}
		
		//#################################################
		// 取込先のテーブルが存在すること、マスター管理一覧に登録されていることをチェック
		if(! postgreSystemLogic.tableSonzaiCheck(etMasterId)) {
			throw new RuntimeException(etMasterName + "のテーブルが存在しません。");
		}
		if(masterKanriLogic.findMasterKanriHansuuMaxV(etMasterId) == null && getProcType() == 1) {
			throw new RuntimeException(etMasterName + "がマスター管理一覧、もしくはマスター管理版数に登録されていません。");
		}

		//#################################################
		// マスター取込詳細のデータを取得：OPEN21→eteamのマッピング情報
		List<GMap> masterShousaiList = masterKanriLogic.selectMasterShousai(etMasterId, getProcType());


		//#################################################
		//マスター取込詳細から取込元(OPEN21)に決算期があるか、科目外部コードがあるかを確認
		//PKのリスト(a,b,,,)と全カラムのリスト(a,b,,,)の文字列生成
		boolean findKessankiNo    = false;
		boolean findGaibukamokuCd = false;
		ArrayList<String> opPkList = new ArrayList<String>(); //KIを除くOPEN21のPK
		ArrayList<String> opColmnList = new ArrayList<String>();

		for(GMap masterShousai : masterShousaiList) {
			if("KI".equals(masterShousai.get("op_colume_id"))) findKessankiNo = true;
			if("KCOD".equals(masterShousai.get("op_colume_id"))) findGaibukamokuCd = true;
			if("1".equals(masterShousai.get("pk_flg")) && !("KI".equals(masterShousai.get("op_colume_id"))) ) opPkList.add(masterShousai.get("op_colume_id").toString());
			opColmnList.add(masterShousai.get("op_colume_id").toString());
		}
		String opPks = list2Str(opPkList); //KIを除くOPEN21のPK
		String opColumns  = list2Str(opColmnList);
		if (etMasterId.equals("segment_master"))
		{
			findKessankiNo = true;
		} //邪道だったけどまあいいでしょう。


		//#################################################
		// HFまたはUFが紐付けられる番号を取得する。
		// HFまたはUFの使用フラグを取得する。
		int hfufMapping = 0;
		int hfufShiyouFlg = 0;
		switch (etMasterId) {
		case "hf1_ichiran":
			hfufMapping = Integer.parseInt(setting.hf1Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf1_shiyou_flg");
			break;
		case "hf2_ichiran":
			hfufMapping = Integer.parseInt(setting.hf2Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf2_shiyou_flg");
			break;
		case "hf3_ichiran":
			hfufMapping = Integer.parseInt(setting.hf3Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf3_shiyou_flg");
			break;
		case "hf4_ichiran":
			hfufMapping = Integer.parseInt(setting.hf4Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf4_shiyou_flg");
			break;
		case "hf5_ichiran":
			hfufMapping = Integer.parseInt(setting.hf5Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf5_shiyou_flg");
			break;
		case "hf6_ichiran":
			hfufMapping = Integer.parseInt(setting.hf6Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf6_shiyou_flg");
			break;
		case "hf7_ichiran":
			hfufMapping = Integer.parseInt(setting.hf7Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf7_shiyou_flg");
			break;
		case "hf8_ichiran":
			hfufMapping = Integer.parseInt(setting.hf8Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf8_shiyou_flg");
			break;
		case "hf9_ichiran":
			hfufMapping = Integer.parseInt(setting.hf9Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf9_shiyou_flg");
			break;
		case "hf10_ichiran":
			hfufMapping = Integer.parseInt(setting.hf10Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("hf10_shiyou_flg");
			break;
		case "uf1_ichiran":
		case "uf1_zandaka":
			hfufMapping = Integer.parseInt(setting.uf1Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf1_shiyou_flg");
			break;
		case "uf2_ichiran":
		case "uf2_zandaka":
			hfufMapping = Integer.parseInt(setting.uf2Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf2_shiyou_flg");
			break;
		case "uf3_ichiran":
		case "uf3_zandaka":
			hfufMapping = Integer.parseInt(setting.uf3Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf3_shiyou_flg");
			break;
		case "uf4_ichiran":
		case "uf4_zandaka":
			hfufMapping = Integer.parseInt(setting.uf4Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf4_shiyou_flg");
			break;
		case "uf5_ichiran":
		case "uf5_zandaka":
			hfufMapping = Integer.parseInt(setting.uf5Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf5_shiyou_flg");
			break;
		case "uf6_ichiran":
		case "uf6_zandaka":
			hfufMapping = Integer.parseInt(setting.uf6Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf6_shiyou_flg");
			break;
		case "uf7_ichiran":
		case "uf7_zandaka":
			hfufMapping = Integer.parseInt(setting.uf7Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf7_shiyou_flg");
			break;
		case "uf8_ichiran":
		case "uf8_zandaka":
			hfufMapping = Integer.parseInt(setting.uf8Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf8_shiyou_flg");
			break;
		case "uf9_ichiran":
		case "uf9_zandaka":
			hfufMapping = Integer.parseInt(setting.uf9Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf9_shiyou_flg");
			break;
		case "uf10_ichiran":
		case "uf10_zandaka":
			hfufMapping = Integer.parseInt(setting.uf10Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf10_shiyou_flg");
			break;
		case "uf_kotei1_ichiran":
		case "uf_kotei1_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei1Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei1_shiyou_flg");
			break;
		case "uf_kotei2_ichiran":
		case "uf_kotei2_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei2Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei2_shiyou_flg");
			break;
		case "uf_kotei3_ichiran":
		case "uf_kotei3_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei3Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei3_shiyou_flg");
			break;
		case "uf_kotei4_ichiran":
		case "uf_kotei4_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei4Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei4_shiyou_flg");
			break;
		case "uf_kotei5_ichiran":
		case "uf_kotei5_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei5Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei5_shiyou_flg");
			break;
		case "uf_kotei6_ichiran":
		case "uf_kotei6_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei6Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei6_shiyou_flg");
			break;
		case "uf_kotei7_ichiran":
		case "uf_kotei7_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei7Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei7_shiyou_flg");
			break;
		case "uf_kotei8_ichiran":
		case "uf_kotei8_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei8Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei8_shiyou_flg");
			break;
		case "uf_kotei9_ichiran":
		case "uf_kotei9_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei9Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei9_shiyou_flg");
			break;
		case "uf_kotei10_ichiran":
		case "uf_kotei10_zandaka":
			hfufMapping = Integer.parseInt(setting.ufKotei10Mapping());
			hfufShiyouFlg = (int)systemKanriLogic.findKaishaInfo().get("uf_kotei10_shiyou_flg");
			break;
		}

		// 0は「使用しない」なので、csv初期化
		// 使用フラグが0のとき初期化
		if (etMasterId.equals("hf1_ichiran") || etMasterId.equals("hf2_ichiran") || etMasterId.equals("hf3_ichiran") ||
			etMasterId.equals("hf4_ichiran") || etMasterId.equals("hf5_ichiran") || etMasterId.equals("hf6_ichiran") ||
			etMasterId.equals("hf7_ichiran") || etMasterId.equals("hf8_ichiran") || etMasterId.equals("hf9_ichiran") || etMasterId.equals("hf10_ichiran") ||
			etMasterId.equals("uf1_ichiran") || etMasterId.equals("uf2_ichiran") || etMasterId.equals("uf3_ichiran") ||
			etMasterId.equals("uf4_ichiran") || etMasterId.equals("uf5_ichiran") || etMasterId.equals("uf6_ichiran") ||
			etMasterId.equals("uf7_ichiran") || etMasterId.equals("uf8_ichiran") || etMasterId.equals("uf9_ichiran") || etMasterId.equals("uf10_ichiran") ||
			etMasterId.equals("uf1_zandaka") || etMasterId.equals("uf2_zandaka") || etMasterId.equals("uf3_zandaka") ||
			etMasterId.equals("uf4_zandaka") || etMasterId.equals("uf5_zandaka") || etMasterId.equals("uf6_zandaka") ||
			etMasterId.equals("uf7_zandaka") || etMasterId.equals("uf8_zandaka") || etMasterId.equals("uf9_zandaka") || etMasterId.equals("uf10_zandaka") ||
			etMasterId.equals("uf_kotei1_ichiran") || etMasterId.equals("uf_kotei2_ichiran") || etMasterId.equals("uf_kotei3_ichiran") ||
			etMasterId.equals("uf_kotei4_ichiran") || etMasterId.equals("uf_kotei5_ichiran") || etMasterId.equals("uf_kotei6_ichiran") ||
			etMasterId.equals("uf_kotei7_ichiran") || etMasterId.equals("uf_kotei8_ichiran") || etMasterId.equals("uf_kotei9_ichiran") || etMasterId.equals("uf_kotei10_ichiran") ||
			etMasterId.equals("uf_kotei1_zandaka") || etMasterId.equals("uf_kotei2_zandaka") || etMasterId.equals("uf_kotei3_zandaka") ||
			etMasterId.equals("uf_kotei4_zandaka") || etMasterId.equals("uf_kotei5_zandaka") || etMasterId.equals("uf_kotei6_zandaka") ||
			etMasterId.equals("uf_kotei7_zandaka") || etMasterId.equals("uf_kotei8_zandaka") || etMasterId.equals("uf_kotei9_zandaka") || etMasterId.equals("uf_kotei10_zandaka")) {
			if (hfufMapping == 0 || hfufShiyouFlg == 0) {
				log.info(etMasterName + "は使用しない為、処理をスキップします。");
				//全レコード削除
				myLogic.delete(etMasterId);
				return 0;
			}
		}

		// UF4～20は残高を使用しないため、csv初期化
		if (etMasterId.equals("uf1_zandaka") || etMasterId.equals("uf2_zandaka") || etMasterId.equals("uf3_zandaka") ||
			etMasterId.equals("uf4_zandaka") || etMasterId.equals("uf5_zandaka") || etMasterId.equals("uf6_zandaka") ||
			etMasterId.equals("uf7_zandaka") || etMasterId.equals("uf8_zandaka") || etMasterId.equals("uf9_zandaka") || etMasterId.equals("uf10_zandaka") ||
			etMasterId.equals("uf_kotei1_zandaka") || etMasterId.equals("uf_kotei2_zandaka") || etMasterId.equals("uf_kotei3_zandaka") ||
			etMasterId.equals("uf_kotei4_zandaka") || etMasterId.equals("uf_kotei5_zandaka") || etMasterId.equals("uf_kotei6_zandaka") ||
			etMasterId.equals("uf_kotei7_zandaka") || etMasterId.equals("uf_kotei8_zandaka") || etMasterId.equals("uf_kotei9_zandaka") || etMasterId.equals("uf_kotei10_zandaka")) {
			if (hfufMapping > 3) {
				log.info(etMasterName + "は使用しない為、処理をスキップします。");
				//全レコード削除
				myLogic.delete(etMasterId);
				return 0;
			}
		}

		//#################################################
		// OPEN21のデータ取得用のSQLを作成
		String selectSql;
		if(findKessankiNo
				&& !(etMasterId.equals("kaisha_info"))
				&& ! etMasterId.startsWith("ki_")
				&& !(etMasterId.equals("kamoku_zandaka"))
				&& !(etMasterId.equals("edaban_zandaka"))
				&& !(etMasterId.equals("bumon_kamoku_yosan"))
				&& !(etMasterId.equals("bumon_kamoku_zandaka"))
				&& !(etMasterId.equals("bumon_kamoku_edaban_yosan"))
				&& !(etMasterId.equals("bumon_kamoku_edaban_zandaka")) ){
			// 取得元(OPEN21)に決算期番号がある場合、翌期(KI=0)と当期(KI=1)両方からSELECTするが、翌期と当期でキー重複あれば翌期優先で取得
			selectSql =
				"SELECT :COLUMNS FROM ("
			+ "  SELECT * FROM :TABLE k0 WHERE KI=0 "
			+ "  UNION "
			+ "  SELECT * FROM :TABLE k1 WHERE KI=1 AND NOT EXISTS(SELECT * FROM :TABLE k0 WHERE k0.KI=0 " + make01Exact(opPkList) + ")"
			+ ") union_table "
			+ ":WHERE "
			+ ":ORDER ";
		} else {
			// 取得元(OPEN21)に決算期番号がない場合、単にSELECT
			selectSql =
				"SELECT :COLUMNS "
			+ "FROM :TABLE tmp "
			+ ":WHERE "
			+ ":ORDER ";
		}
		selectSql = selectSql.replace(":TABLE"		, opMasterId);
		selectSql = selectSql.replace(":COLUMNS"	, opColumns);
		if(findGaibukamokuCd) {
			selectSql = selectSql.replace(":WHERE"	, "WHERE KCOD IS NOT NULL AND KCOD <> ''");
		} else {
			selectSql = selectSql.replace(":WHERE"	, "");
		}

		if(getProcType() == 2 && etMasterId.equals("ki_shiwake")) {
			// 期間指定反映の場合は指定された日付を含む月のみを反映する
			selectSql = selectSql.replace(":DYMD_START"	, new SimpleDateFormat("yyyyMMdd").format(getTargetDateFrom()));
			selectSql = selectSql.replace(":DYMD_END"	, new SimpleDateFormat("yyyyMMdd").format(getTargetDateTo()));
		}

		if(opPks.equals("")){
			selectSql = selectSql.replace(":ORDER"	, "");
		}else if(findKessankiNo) {
			selectSql = selectSql.replace(":ORDER"	, "ORDER BY KI, " + opPks);
		} else {
			selectSql = selectSql.replace(":ORDER"	, "ORDER BY " + opPks);
		}
		if (etMasterId.equals("hf1_ichiran") || etMasterId.equals("hf2_ichiran") || etMasterId.equals("hf3_ichiran") ||
			etMasterId.equals("hf4_ichiran") || etMasterId.equals("hf5_ichiran") || etMasterId.equals("hf6_ichiran") ||
			etMasterId.equals("hf7_ichiran") || etMasterId.equals("hf8_ichiran") || etMasterId.equals("hf9_ichiran") || etMasterId.equals("hf10_ichiran") ||
			etMasterId.equals("uf1_ichiran") || etMasterId.equals("uf2_ichiran") || etMasterId.equals("uf3_ichiran") ||
			etMasterId.equals("uf4_ichiran") || etMasterId.equals("uf5_ichiran") || etMasterId.equals("uf6_ichiran") ||
			etMasterId.equals("uf7_ichiran") || etMasterId.equals("uf8_ichiran") || etMasterId.equals("uf9_ichiran") || etMasterId.equals("uf10_ichiran") ||
			etMasterId.equals("uf1_zandaka") || etMasterId.equals("uf2_zandaka") || etMasterId.equals("uf3_zandaka") ||
			etMasterId.equals("uf4_zandaka") || etMasterId.equals("uf5_zandaka") || etMasterId.equals("uf6_zandaka") ||
			etMasterId.equals("uf7_zandaka") || etMasterId.equals("uf8_zandaka") || etMasterId.equals("uf9_zandaka") || etMasterId.equals("uf10_zandaka") ||
			etMasterId.equals("uf_kotei1_ichiran") || etMasterId.equals("uf_kotei2_ichiran") || etMasterId.equals("uf_kotei3_ichiran") ||
			etMasterId.equals("uf_kotei4_ichiran") || etMasterId.equals("uf_kotei5_ichiran") || etMasterId.equals("uf_kotei6_ichiran") ||
			etMasterId.equals("uf_kotei7_ichiran") || etMasterId.equals("uf_kotei8_ichiran") || etMasterId.equals("uf_kotei9_ichiran") || etMasterId.equals("uf_kotei10_ichiran") ||
			etMasterId.equals("uf_kotei1_zandaka") || etMasterId.equals("uf_kotei2_zandaka") || etMasterId.equals("uf_kotei3_zandaka") ||
			etMasterId.equals("uf_kotei4_zandaka") || etMasterId.equals("uf_kotei5_zandaka") || etMasterId.equals("uf_kotei6_zandaka") ||
			etMasterId.equals("uf_kotei7_zandaka") || etMasterId.equals("uf_kotei8_zandaka") || etMasterId.equals("uf_kotei9_zandaka") || etMasterId.equals("uf_kotei10_zandaka")) {
			selectSql = selectSql.replace(":NUM"	, String.valueOf(hfufMapping));
		}
		if (etMasterId.equals("kamoku_master")) {
			GMap hfOrUf = new GMap();
			hfOrUf.put("uf1", setting.uf1Mapping());
			hfOrUf.put("uf2", setting.uf2Mapping());
			hfOrUf.put("uf3", setting.uf3Mapping());
			hfOrUf.put("uf3", setting.uf3Mapping());
			hfOrUf.put("uf4", setting.uf4Mapping());
			hfOrUf.put("uf5", setting.uf5Mapping());
			hfOrUf.put("uf6", setting.uf6Mapping());
			hfOrUf.put("uf7", setting.uf7Mapping());
			hfOrUf.put("uf8", setting.uf8Mapping());
			hfOrUf.put("uf9", setting.uf9Mapping());
			hfOrUf.put("uf10", setting.uf10Mapping());
			hfOrUf.put("uf_kotei1", setting.ufKotei1Mapping());
			hfOrUf.put("uf_kotei2", setting.ufKotei2Mapping());
			hfOrUf.put("uf_kotei3", setting.ufKotei3Mapping());
			hfOrUf.put("uf_kotei4", setting.ufKotei4Mapping());
			hfOrUf.put("uf_kotei5", setting.ufKotei5Mapping());
			hfOrUf.put("uf_kotei6", setting.ufKotei6Mapping());
			hfOrUf.put("uf_kotei7", setting.ufKotei7Mapping());
			hfOrUf.put("uf_kotei8", setting.ufKotei8Mapping());
			hfOrUf.put("uf_kotei9", setting.ufKotei9Mapping());
			hfOrUf.put("uf_kotei10", setting.ufKotei10Mapping());

			for (int i = 1; i <=10; i++) {
				if (Integer.parseInt((String)hfOrUf.get("uf" + i)) == 0) {
					selectSql = selectSql.replace(":DM" + i + "CK", "0");
				} else {
					selectSql = selectSql.replace(":DM" + i + "CK", "K.DM" + Integer.parseInt((String)hfOrUf.get("uf" + i)) + "2");
				}
			}

			for (int i = 1; i <=10; i++) {
				if (Integer.parseInt((String)hfOrUf.get("uf_kotei" + i)) == 0) {
					selectSql = selectSql.replace(":DMK" + i + "CK", "0");
				} else {
					selectSql = selectSql.replace(":DMK" + i + "CK", "K.DM" + Integer.parseInt((String)hfOrUf.get("uf_kotei" + i)) + "2");
				}
			}
		}
		if (etMasterId.equals("kaisha_info")) {
			GMap hfOrUf = new GMap();
			hfOrUf.put("hf1", setting.hf1Mapping());
			hfOrUf.put("hf2", setting.hf2Mapping());
			hfOrUf.put("hf3", setting.hf3Mapping());
			hfOrUf.put("hf4", setting.hf4Mapping());
			hfOrUf.put("hf5", setting.hf5Mapping());
			hfOrUf.put("hf6", setting.hf6Mapping());
			hfOrUf.put("hf7", setting.hf7Mapping());
			hfOrUf.put("hf8", setting.hf8Mapping());
			hfOrUf.put("hf9", setting.hf9Mapping());
			hfOrUf.put("hf10", setting.hf10Mapping());
			hfOrUf.put("uf1", setting.uf1Mapping());
			hfOrUf.put("uf2", setting.uf2Mapping());
			hfOrUf.put("uf3", setting.uf3Mapping());
			hfOrUf.put("uf4", setting.uf4Mapping());
			hfOrUf.put("uf5", setting.uf5Mapping());
			hfOrUf.put("uf6", setting.uf6Mapping());
			hfOrUf.put("uf7", setting.uf7Mapping());
			hfOrUf.put("uf8", setting.uf8Mapping());
			hfOrUf.put("uf9", setting.uf9Mapping());
			hfOrUf.put("uf10", setting.uf10Mapping());
			hfOrUf.put("uf_kotei1", setting.ufKotei1Mapping());
			hfOrUf.put("uf_kotei2", setting.ufKotei2Mapping());
			hfOrUf.put("uf_kotei3", setting.ufKotei3Mapping());
			hfOrUf.put("uf_kotei4", setting.ufKotei4Mapping());
			hfOrUf.put("uf_kotei5", setting.ufKotei5Mapping());
			hfOrUf.put("uf_kotei6", setting.ufKotei6Mapping());
			hfOrUf.put("uf_kotei7", setting.ufKotei7Mapping());
			hfOrUf.put("uf_kotei8", setting.ufKotei8Mapping());
			hfOrUf.put("uf_kotei9", setting.ufKotei9Mapping());
			hfOrUf.put("uf_kotei10", setting.ufKotei10Mapping());

			// HFの使用フラグと名称を置き換え
			for (int i = 1; i <= 10; i++) {
				// HFを使用しない時
				// 使用フラグ → 0
				// 名称       → 空
				// ※半角スペースは10がi=1の際置換されないよう付与
				if (Integer.parseInt((String)hfOrUf.get("hf" + i)) == 0) {
					selectSql = selectSql.replace(":DUF" + i + "FLG"	, "0");
					selectSql = selectSql.replace(":HDF" + i + " " 			, "0 ");
					selectSql = selectSql.replace(":DUF" + i + "NM"		, "''");
				} else {
					selectSql = selectSql.replace(":DUF" + i + "FLG"	, "V.DUF" + Integer.parseInt((String)hfOrUf.get("hf" + i)) + "FLG");
					selectSql = selectSql.replace(":HDF" + i + " "			, "(SELECT OP.IDATA FROM OPTION1 OP WHERE OP.PRGID='CCINFOMNT' AND OP.USNO=10000 AND OP.KEYNM1='DMNTFRIV' AND OP.KEYNM2 = 'HDF" + Integer.parseInt((String)hfOrUf.get("hf" + i)) + "') ");
					selectSql = selectSql.replace(":DUF" + i + "NM"		, "RTRIM(V.DUF" + Integer.parseInt((String)hfOrUf.get("hf" + i)) + "NM)");
				}
			}

			// UFの使用フラグと名称を置き換え
			for (int j = 1; j <= 10; j++) {
				// UFを使用しない時
				// 使用フラグ → 0
				// 名称       → 空
				if (Integer.parseInt((String)hfOrUf.get("uf" + j)) == 0) {
					selectSql = selectSql.replace(":D" + j + "FLG"		, "0");
					selectSql = selectSql.replace(":D" + j + "NM"		, "''");
				} else {
					selectSql = selectSql.replace(":D" + j + "FLG"		, "V.D" + Integer.parseInt((String)hfOrUf.get("uf" + j)) + "FLG");
					selectSql = selectSql.replace(":D" + j + "NM"		, "RTRIM(V.D" + Integer.parseInt((String)hfOrUf.get("uf" + j)) + "NM)");
				}
			}

			// UFの使用フラグと名称を置き換え
			for (int j = 1; j <= 10; j++) {
				// UFを使用しない時
				// 使用フラグ → 0
				// 名称       → 空
				if (Integer.parseInt((String)hfOrUf.get("uf_kotei" + j)) == 0) {
					selectSql = selectSql.replace(":DK" + j + "FLG"		, "0");
					selectSql = selectSql.replace(":DK" + j + "NM"		, "''");
				} else {
					selectSql = selectSql.replace(":DK" + j + "FLG"		, "V.D" + Integer.parseInt((String)hfOrUf.get("uf_kotei" + j)) + "FLG");
					selectSql = selectSql.replace(":DK" + j + "NM"		, "RTRIM(V.D" + Integer.parseInt((String)hfOrUf.get("uf_kotei" + j)) + "NM)");
				}
			}
		}


		if (etMasterId.equals("bumon_kamoku_yosan")) {
			//部門科目予算取得用テーブル切替
			String tmpStr = setting.yosanYosanNo();
			selectSql = selectSql.replace(":BKYSN", "BKYSN" + tmpStr );
		}
		if (etMasterId.equals("bumon_kamoku_edaban_yosan")) {
			//部門科目枝番予算取得用テーブル切替
			String tmpStr = setting.yosanYosanNo();
			selectSql = selectSql.replace(":BKEYSN", "BKEYSN" + tmpStr );
		}

		// 債権債務DBとの接続がある場合、取引先取得SQLに支払区分識別キーの条件を追加
		if(tableIsSiken(etMasterId)){
			if(connectionSIAS_SAIKEN != null){
				selectSql = selectSql.replace("SK.SKBNCOD = SH.SKBNCOD"	, "SK.SKBNCOD = SH.SKBNCOD AND SK.SKKBN = 11");
			}else{
				selectSql = selectSql.replace("TR.HJCD = TMP_TR.HJCD AND", "TR.HJCD = TMP_TR.HJCD").replace("TR.SAIMU = 1", "");
			}
		}else {
			selectSql = selectSql.replace("TR.HJCD = TMP_TR.HJCD AND", "TR.HJCD = TMP_TR.HJCD").replace("TR.SAIMU = 1", "");
		}

		// 債権債務DBとの接続がある場合、取引先振込先は銀行ID=1じゃなくて初期値=1に置換
		if(etMasterId.equals("torihikisaki")){
			if(connectionSIAS_SAIKEN != null){
				selectSql = selectSql.replace("FR.GIN_ID = 1", " FR.FDEF = 1");
			}
		}

		// 外貨のカラムがない場合、本当は早めに処理したいが、形になるのがどこか分かりにくいのでここで最終的に処理
		if(etMasterId.equals("gaika_setting") && !isDefaultRateUsed)
		{
			selectSql = selectSql.replace("gc.rateref", "0 as rateref");
		}
		
		log.info("作成したSQLは「" + selectSql.toString() + "」です。");


		//#################################################
		// OPEN21からデータ取得
		ResultSet open21ResultSet;

		// 取引先は債権債務DBとの接続が確立できている場合のみ債権債務DBから取得
		if(tableIsSiken(etMasterId)){
			if(connectionSIAS_SAIKEN != null){
				open21ResultSet = connectionSIAS_SAIKEN.loadFetchResultSet(selectSql);
			}else{
				open21ResultSet = connectionSIAS.loadFetchResultSet(selectSql);
			}
		}else{
			open21ResultSet = connectionSIAS.loadFetchResultSet(selectSql);
		}

		// e文書検索でデータがない場合は終わり
		try {
			if(etMasterId.equals("ebunsho_kensaku") && !open21ResultSet.first())
			{
				log.info("e文書検索設定のデータが存在しないため、処理をスキップします。");
				log.info(etMasterName + "の処理を終了します。");
				return 0;
			}

			open21ResultSet.beforeFirst();
		} catch (SQLException e1) {
			throw EteamSQLExceptionHandler.makeException(e1);
		}

		//#################################################
		// OPEN21から取得したデータを諸届のマスターへ登録

		// まずは全レコード削除
		if (getProcType() == 1) {
			myLogic.delete(etMasterId);
		} else {
			myLogic.deleteTerm(etMasterId, new java.sql.Date(getTargetDateFrom().getTime()), new java.sql.Date(getTargetDateTo().getTime()));
		}

		// 各テーブルの登録用SQLを作成します。
		String insertSql = "INSERT INTO :TABLE VALUES(:PARAMS)";
		insertSql = insertSql.replace(":TABLE"	, etMasterId);
		insertSql = insertSql.replace(":PARAMS"	, makeParams(opColmnList.size()));

		int ResultCnt = 0;

		try{
			// CSVヘッダーをTEMP_CSVMAKEテーブルに書き込み
			setTempCsvDataHeader(etMasterId,etMasterName);

			// 1行ずつINSERTする
			while(open21ResultSet.next()){
				ResultCnt++;
				Object[] dataArray = new Object[masterShousaiList.size()];
				for(int i = 0; i < masterShousaiList.size(); i++) {
					GMap shousaiMap = masterShousaiList.get(i);
					String etColumnId = (String)shousaiMap.get("et_column_id");
					String opColumnId = shousaiMap.get("op_colume_id").toString();
					Object columnObj = open21ResultSet.getObject(opColumnId);

					// e文書検索設定での書類種別の置き換え
					if(etMasterId.equals("ebunsho_kensaku") && opColumnId.equals("KEYNM2"))
					{
						columnObj = this.convertDocType(columnObj.toString());
					}
					// 部門仕入 共売の値 0→3へ置き換え
					if(etMasterId.equals("bumon_master") && opColumnId.equals("SY05") && columnObj != null)
					{
						if (columnObj.toString().equals("0"))
						{
							columnObj = "3";
						}
					}

					// カラムの型によって、Stringから各データ型へ変更します。
					switch (shousaiMap.get("et_data_type").toString()) {
					case "varchar":
						dataArray[i] = columnObj == null ? "" : columnObj.toString();
						break;
					case "int":
						dataArray[i] = columnObj == null ? null : Integer.parseInt(columnObj.toString());
						break;
					case "smallint":
						if (columnObj == null && (etColumnId.endsWith("flg") || etColumnId.endsWith("check"))) columnObj = Integer.parseInt("0");
						dataArray[i] = columnObj == null ? null : Integer.parseInt(columnObj.toString());
						break;
					case "decimal":
						dataArray[i] = columnObj == null ? null : new BigDecimal(columnObj.toString().replaceAll(",", ""));
						break;
					case "date":
						dataArray[i] = columnObj == null ?
								null : new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(columnObj.toString().replaceAll("-", "")).getTime());
						break;
					}
				}
				myLogic.insert(insertSql, dataArray);
			}

			// csv作成
			createCsv(etMasterId, etMasterName);//連絡票490対応によりヘッダーだけになった
		}catch(SQLException e){
			throw EteamSQLExceptionHandler.makeException(e);
		}finally{
			//フェッチ用ResultSetなど閉じる
			if(tableIsSiken(etMasterId)){
				if(connectionSIAS_SAIKEN != null){
					connectionSIAS_SAIKEN.CloseFetchResultSet();
				}else{
					connectionSIAS.CloseFetchResultSet();
				}
			}else{
				connectionSIAS.CloseFetchResultSet();
			}
		}


		//#################################################
		// 後処理
		//会社情報の債務使用フラグと軽減有無フラグは特殊
		if(etMasterId.equals("kaisha_info")){
			String saimuKakuninSql = "SELECT COUNT(trcd) AS count FROM SS_TORI";
			String saimuFlgSql = "UPDATE kaisha_info SET saimu_shiyou_flg = ?";
			String saimuShiyouFlg = "0";
			if(connectionSIAS_SAIKEN != null){
				if(Open21Env.getSIASDBEngine() == Open21Env.SIASDBEngine.SQLSERVER) {
					saimuShiyouFlg = (int)connectionSIAS_SAIKEN.find(saimuKakuninSql).get("count") > 0 ? "1" : "0";
				}else if(Open21Env.getSIASDBEngine() == Open21Env.SIASDBEngine.POSTGRESQL) {
					saimuShiyouFlg = Integer.parseInt(connectionSIAS_SAIKEN.find(saimuKakuninSql).get("count").toString()) > 0 ? "1" : "0";
				}
			}
			connection.update(saimuFlgSql, saimuShiyouFlg);

			//軽減税率有無フラグは2020.04.10版以降は固定で1(対応済)設定
			String keigenFlgSql = "UPDATE kaisha_info SET keigen_umu_flg = ?";
			String keigenUmuFlg = "1";
			connection.update(keigenFlgSql, keigenUmuFlg);
		}


		//#################################################
		// 終わり
		log.info("処理データ件数は " + ResultCnt + " 件です。");
		log.info(etMasterName + "の処理を終了します。");
		return ResultCnt;
	}


	/**
	 * マスター管理版数登録用CSVのヘッダ部データを作成する。
	 * @param etMasterId マスターID
	 * @param etMasterName マスター名
	 * @throws ParseException フォーマット変換
	 * @throws IOException CSVの作成
	 */
	protected void setTempCsvDataHeader(String etMasterId,String etMasterName) throws ParseException, IOException {

		//CSV作成用一時テーブルのリセット
		myLogic.delete("TEMP_CSVMAKE");
		row_num = 1;

		//#################################################
		// 諸届のマスターのメタデータ取得：カラム名やPK項目
		List<GMap> currentColumnList = postgreSystemLogic.selectColumnInfo(EteamThreadMap.get().get(SYSTEM_PROP.SCHEMA), etMasterId);
		List<String> currentPrimaryKeyList = postgreSystemLogic.selectPKColumn(EteamThreadMap.get().get(SYSTEM_PROP.SCHEMA), etMasterId);


		//#################################################
		// マスター管理版数へ登録するCSVを作成

		// 取得したカラム名やPKリストをカンマ繋ぎにする
		StringBuilder logicalName  = new StringBuilder(); // 論理名
		StringBuilder physicalName = new StringBuilder(); // 物理名
		StringBuilder columnType   = new StringBuilder(); // データ型
		StringBuilder constraints  = new StringBuilder(); // プライマリーキー・NOTNULL制約
		//StringBuilder fileData     = new StringBuilder(); // ファイルデータ
		for(int i = 0; i < currentColumnList.size(); i++) {
			GMap map = currentColumnList.get(i);
			// CSV1行目：カラム名（和名）
			logicalName.append(map.get("column_comment").toString()).append(",");
			// CSV2行目：カラム名（物理名）
			physicalName.append(map.get("column_name").toString()).append(",");
			// CSV3行目：型
			if(null != map.get("column_length")) {
				columnType.append(dataTypeHenkan(map.get("column_type").toString()) + "(" + map.get("column_length").toString() + ")").append(",");
			} else {
				columnType.append(dataTypeHenkan(map.get("column_type").toString())).append(",");
			}
			//CSV4行目：PKなら1、NOTNULLなら2、それ以外なら空
			if(dataPrimaryKeyCheck(map.get("column_name").toString(), currentPrimaryKeyList)) {
				constraints.append("1").append(",");
			} else if ("true".equals(map.get("column_notnull").toString())) {
				constraints.append("2").append(",");
			} else {
				constraints.append(",");
			}
		}

		// １行目(論理名)と２行目(物理名)と３行目(データ型)と４行目(制約)をCSV用テーブルに書き込み
		myLogic.insertTempCsvTable((logicalName.toString().substring(0, logicalName.length() -1)) + "\r\n",row_num++);
		myLogic.insertTempCsvTable((physicalName.toString().substring(0, physicalName.length() -1))+ "\r\n",row_num++);
		myLogic.insertTempCsvTable((columnType.toString().substring(0, columnType.length() -1)) + "\r\n",row_num++);
		myLogic.insertTempCsvTable((constraints.toString().substring(0, constraints.length() -1)) + "\r\n",row_num++);

	}

	/**
	 * マスター管理版数登録用のCSVを作成する。
	 * @param etMasterId マスターID
	 * @param etMasterName マスター名
	 * @throws ParseException フォーマット変換
	 * @throws IOException CSVの作成
	 */
	protected void createCsv(String etMasterId, String etMasterName) throws ParseException, IOException {

		// マスター管理版数へ登録
		if (getProcType() == 1) {
			String fileName = etMasterId + "(" + etMasterName + ")" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + ".csv";
			myLogic.updateMasterKanriCsv(etMasterId,fileName.toString(),ContentType.EXCEL);
		}
	}

	/**
	 * CSV出力用の項目フォーマット
	 * @param obj オブジェクト
	 * @return オブジェクトフォーマット後
	 */
	@SuppressWarnings("unused")
	protected String format4Csv(Object obj) {
		return (null == obj) ?
			"" :
			(obj instanceof Date) ?
					new SimpleDateFormat("yyyy/MM/dd").format((Date)obj) :
					String.valueOf(obj);
	}

	/**
	 * システム管理しているデータ型を登録時のデータ型へ変換を行う
	 * PostgreSQLのシステムで管理しているデータ型とCSVアップロードに使用するデータ型が異なるため
	 * @param dataType PostgreSQLのシステムで管理しているデータ型
	 * @return 変換後の型
	 */
	protected String dataTypeHenkan(String dataType) {
		String result = "";
		switch (dataType) {
			case "varchar"  : result = "VARCHAR";   break;
			case "int2"     : result = "SMALLINT";  break;
			case "int4"     : result = "INT";       break;
			case "numeric"  : result = "DECIMAL";   break;
			case "date"     : result = "DATE";      break;
			case "timestamp": result = "TIMESTAMP"; break;
		}
		return result;
	}


	/**
	 * カラムがプライマリーキーかどうか判定する。
	 * @param column         カラム名
	 * @param primaryKeyList プライマリーキーのリスト
	 * @return プライマリーキーならtrue
	 */
	protected boolean dataPrimaryKeyCheck(String column ,List<String> primaryKeyList) {
		boolean result = false;

		for(String pk : primaryKeyList) {
			if(column.equals(pk)) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * リストから文字列(a,b,c形式)へ
	 * @param l リスト
	 * @return 文字列
	 */
	protected String list2Str(List<String> l) {

		if (l.size() == 0)
		{
			return "";
		}

		StringBuffer ret = new StringBuffer();
		ret.append(l.get(0));
		for (int i = 1; i < l.size(); i++) {
			ret.append(",").append(l.get(i));
		}
		return ret.toString();
	}

	/**
	 * "?, ?,,, ?"の文字列を作る
	 * @param size ?の数
	 * @return 文字列
	 */
	protected String makeParams(int size) {
		StringBuffer ret = new StringBuffer();
		ret.append("?");
		for (int i = 1; i < size; i++) {
			ret.append(", ?");
		}
		return ret.toString();
	}

	/**
	 * テーブルIDが債権債務DBからの取得かどうか
	 * @param etMasterId 諸届側テーブル名
	 * @return 債権債務DBである
	 */
	protected boolean tableIsSiken(String etMasterId) {
		return
			   etMasterId.equals("torihikisaki")
			|| etMasterId.equals("torihikisaki_furikomisaki")
			|| etMasterId.equals("torihikisaki_hojo")
			|| etMasterId.equals("torihikisaki_shiharaihouhou");
	}

	/**
	 * l=("a","b","c")なら"AND k0.a=k1.a AND k0.b=k1.b AND k0.c=k1.c"を作る
	 * @param l リスト
	 * @return 文字列
	 */
	protected String make01Exact(List<String> l){
		StringBuffer ret = new StringBuffer();;
		for (int i = 0; i < l.size(); i++){
			ret.append(" AND k0." + l.get(i) + "=k1." + l.get(i));
		}
		return ret.toString();
	}

	/**
	 * 現預金出納帳の開始残高を更新する
	 */
	protected void updateSuitouchouKaishiZandaka() {
		List<GMap> suitouchouList = suitouchouSettingDao.loadSuitouchouSetting(DENPYOU_KBN_KYOTEN.GENYOKIN_SUITOUCHOU);
		for(GMap suitouchou : suitouchouList) {
			String denpyouKbn = suitouchou.get("denpyou_kbn");
			String zaimuKyotenNyuryokuPatternNo = suitouchou.get("zaimu_kyoten_nyuryoku_pattern_no");
			String zandakaType = suitouchou.get("zandaka_type");
			String kamokuCd = suitouchou.get("kamoku_gaibu_cd");
			String futanBumonCd = suitouchou.get("futan_bumon_cd");
			String kamokuEdabanCd = suitouchou.get("kamoku_edaban_cd");
			Integer kaishiKesn = suitouchou.get("kaishi_kesn");
			if (null == kaishiKesn)
			{
				continue;
			} // 開始残高登録が未登録ならばスキップ

			//開始決算期 < 当期内部決算期なら、財務で決算更新されたと判断してよい
			int kesnTouki = masterKanriLogic.findKesnWhereKessankiBangou(Open21KessankiBangou.TOUKI);
			if(kaishiKesn < kesnTouki) {

				//当期の残高を出納帳個別設定に保存する
				List<GMap> zandakaList = masterKanriLogic.loadZandaka(zandakaType, kamokuCd, futanBumonCd, kamokuEdabanCd) ;
				for(GMap zandaka : zandakaList) {
					if( Open21KessankiBangou.TOUKI == (int)zandaka.get("kessanki_bangou") ) {

						int taishakuZokusei = zandaka.get("taishaku_zokusei");
						BigDecimal zandaka00 = BigDecimal.ZERO;
						BigDecimal zandakaR00 = zandaka.get("zandaka_kari00");
						BigDecimal zandakaS00 = zandaka.get("zandaka_kashi00");
						if(0 == taishakuZokusei){
							zandaka00 = zandakaR00.subtract(zandakaS00);
						}else {
							zandaka00 = zandakaS00.subtract(zandakaR00);
						}
						suitouchouSettingDao.updateSuitouchouKaishiZandaka(kesnTouki, zandaka00, "1", "batch", denpyouKbn, zaimuKyotenNyuryokuPatternNo);
					}
				}
			}
		}
	}
	
	/**
	 * @param opDocType SIAS側文書種別
	 * @return eteam側文書種別
	 */
	private String convertDocType(String opDocType)
	{
		// 本当はenumにした方がいいかもしれないが一旦これで
		// defaultへの到達は仮にあった場合異常
		switch(opDocType)
		{
			case "RYOSHUSHO":
				return "0";
			case "SEIKYUSHO":
				return "1";
			case "KEIYAKUSHO":
				return "2";
			case "NOUHINSHO":
				return "3";
			case "CHUMONSHO":
				return "4";
			case "MITSUMORISHO":
				return "5";
			case "RYOSHUHIKAE":
				return "6";
			case "SEIKYUHIKAE":
				return "7";
			default:
				return null;
		}
	}
}
