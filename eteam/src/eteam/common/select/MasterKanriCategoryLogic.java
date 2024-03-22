package eteam.common.select;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.keigenZeiritsuKbn;
import eteam.common.EteamConst.suitouchouZandakaType;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_SHUBETSU;
import eteam.common.EteamSettingInfo;
import eteam.common.RegAccess;
import eteam.common.open21.Open21Env;
import eteam.database.dao.GaikaSettingDao;
import eteam.database.dto.GaikaSetting;

/**
 * マスターカテゴリー内のSelect文を集約したLogic
 */
public class MasterKanriCategoryLogic extends EteamAbstractLogic {

	/** 財務枝番コード定数 */
	public static final String ZAIMU = "<ZAIMU>";

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* マスター管理一覧(master_kanri_ichiran) */
/* マスター管理版数(master_kanri_hansuu) */
	/**
	 * マスターデータを全て取得します。
	 * @return リスト
	 */
	public List<GMap> selectMasterDataList() {
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

		String sql =
					"SELECT "
				+ "  i.master_id, "
				+ "  i.master_name, "
				+ "  i.henkou_kahi_flg, "
				+ "  h.koushin_time "
				+ "FROM master_kanri_ichiran i "
				+ "INNER JOIN master_kanri_hansuu h ON "
				+ "  i.master_id = h.master_id AND h.delete_flg = '0' "
				+ ":WHERE "
				+ "ORDER BY i.master_id";

		/*
		 * 非表示にするマスターテーブルを指定する。
		 */
		List<String> hiddenMasterList = new ArrayList<>();

		// 外貨入力にチェックなしの場合、幣種マスター、幣種別レートマスターを非表示
		if (!sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA)) {
			hiddenMasterList.add("'heishu_master'");
			hiddenMasterList.add("'rate_master'");
		}

		// 「予算執行管理Aあり」以外の場合は起案番号簿を非表示
		if (!RegAccess.checkEnableYosanShikkouOption().equals(RegAccess.YOSAN_SHIKKOU_OP.A_OPTION)){
			hiddenMasterList.add("'kian_bangou_bo'");
		}

		// 財務側で外貨使用しない設定なら、幣種マスター(拠点)・幣種別レートマスター(拠点)・月次レート(拠点)は非表示
		if(RegAccess.checkEnableZaimuKyotenOption() && !isGaikaShiyou()){
			hiddenMasterList.add("'ki_heishu_master_kyoten'");
			hiddenMasterList.add("'rate_master_kyoten'");
			hiddenMasterList.add("'default_rate_kyoten'");
		}


		if (hiddenMasterList.size() > 0){
			sql = sql.replaceFirst(":WHERE", "WHERE i.master_id NOT IN (" + String.join(",", hiddenMasterList) +")");
		} else{
			sql = sql.replaceFirst(":WHERE", "");
		}

		return connection.load(sql);
	}
	/**
	 * マスターデータを全て取得します。
	 * @param masterId マスターID
	 * @return リスト
	 */
	public List<GMap> selectMasterDataVersionList(String masterId) {
		final String sql =
					"SELECT "
				+ "  version, "
				+ "  delete_flg, "
				+ "  koushin_time "
				+ "FROM master_kanri_hansuu "
				+ "WHERE "
				+ "  master_id = ? "
				+ "ORDER BY version ";
		return connection.load(sql, masterId);
	}

	/**
	 * マスター一覧を取得します。
	 * @param masterId マスターID
	 * @return 一件データ
	 */
	public GMap selectMasterKanriIchiran(String masterId) {
		final String sql =
					"SELECT master_id, master_name, "
					+ "  case henkou_kahi_flg when '1' then '1' when '2' then '1' else '0' end as henkou_kahi_flg, "
					+ "  case henkou_kahi_flg when '2' then '1' else '0' end as editable_flg "
				+ "FROM master_kanri_ichiran "
				+ "WHERE master_id = ?";
		return connection.find(sql, masterId);
	}

	/**
	 * マスターデータをマスターIDとバージョンをキーに取得します。
	 * @param masterId マスターID
	 * @param version  ヴァージョン
	 * @return 一件データ
	 */
	public GMap findMasterKanriHansuu(String masterId, int version) {
		final String sql =
					"SELECT "
				+ "  i.master_id, "
				+ "  i.master_name, "
				+ "  case i.henkou_kahi_flg when '1' then '1' when '2' then '1' else '0' end as henkou_kahi_flg, "
				+ "  case i.henkou_kahi_flg when '2' then '1' else '0' end as editable_flg, "
				+ "  h.version, "
				+ "  h.delete_flg, "
				+ "  h.file_name, "
				+ "  h.file_size, "
				+ "  h.content_type, "
				+ "  h.binary_data, "
				+ "  h.koushin_time "
				+ "FROM "
				+ "  master_kanri_ichiran i "
				+ "INNER JOIN master_kanri_hansuu h ON "
				+ "  i.master_id = h.master_id "
				+ "WHERE "
				+ "  h.master_id = ? AND h.version = ? ";
		return connection.find(sql, masterId, version);
	}

	/**
	 * マスターデータをマスターIDキーに取得します。
	 * ヴァージョンは同じマスターIDの最大のもの
	 * @param masterId マスターID
	 * @return 一件データ
	 */
	public GMap findMasterKanriHansuuMaxV(String masterId) {
		final String sql =
					"SELECT "
				+ "  i.master_id, "
				+ "  i.master_name, "
				+ "  case i.henkou_kahi_flg when '1' then '1' when '2' then '1' else '0' end as henkou_kahi_flg, "
				+ "  case i.henkou_kahi_flg when '2' then '1' else '0' end as editable_flg, "
				+ "  h.version, "
				+ "  h.delete_flg, "
				+ "  h.file_name, "
				+ "  h.file_size, "
				+ "  h.content_type, "
				+ "  h.binary_data, "
				+ "  h.koushin_time "
				+ "FROM "
				+ "  master_kanri_ichiran i "
				+ "INNER JOIN master_kanri_hansuu h ON "
				+   "  i.master_id = h.master_id "
				+ "WHERE "
				+ "  h.master_id = ? "
				+ "ORDER BY "
				+ "  h.version desc "
				+ "LIMIT 1";
		return connection.find(sql, masterId);
	}

/* マスター取込一覧(master_torikomi_ichiran) */

	/**
	 * マスター取込一覧からデータを取得します。
	 * @param procType 処理タイプ(1:通常マスター取込 2:マスター期間取込)
	 * @return リスト
	 */
	public List<GMap> selectMasterIchiran(int procType) {
		//使用環境により分岐
		switch (Open21Env.getVersion()) {
		case DE3 :
			if( procType == 1 ){
				return connection.load("SELECT * FROM master_torikomi_ichiran_de3 WHERE torikomi_kahi_flg = '1' ORDER BY master_id;");
			}else{
				return connection.load("SELECT * FROM master_torikomi_term_ichiran_de3 WHERE torikomi_kahi_flg = '1' ORDER BY master_id;");
			}
		case SIAS :
			//SIAS使用DBにより更に分岐
			if( procType == 1 ){
				switch(Open21Env.getSIASDBEngine()) {
				case SQLSERVER:	return connection.load("SELECT * FROM master_torikomi_ichiran_sias WHERE torikomi_kahi_flg = '1' ORDER BY CASE master_id WHEN 'kaisha_info' THEN 1 ELSE 2 END, master_id;");
				case POSTGRESQL:	return connection.load("SELECT * FROM master_torikomi_ichiran_mk2 WHERE torikomi_kahi_flg = '1' ORDER BY CASE master_id WHEN 'kaisha_info' THEN 1 ELSE 2 END, master_id;");
				default:			return null;
				}
			}else{
				switch(Open21Env.getSIASDBEngine()) {
				case SQLSERVER:	return connection.load("SELECT * FROM master_torikomi_term_ichiran_sias WHERE torikomi_kahi_flg = '1' ORDER BY CASE master_id WHEN 'kaisha_info' THEN 1 ELSE 2 END, master_id;");
				case POSTGRESQL:	return connection.load("SELECT * FROM master_torikomi_term_ichiran_mk2 WHERE torikomi_kahi_flg = '1' ORDER BY CASE master_id WHEN 'kaisha_info' THEN 1 ELSE 2 END, master_id;");
				default:			return null;
				}
			}
		default:
			return null;
		}
	}


/* マスター取込詳細(master_torikomi_shousai) */

	/**
	 * マスター取込詳細からデータを取得します。
	 * @param master_id マスターID
	 * @param procType 処理タイプ(1:通常マスター取込 2:マスター期間取込)
	 * @return リスト
	 */
	public List<GMap> selectMasterShousai(String master_id, int procType) {
		//使用環境により分岐
		switch (Open21Env.getVersion()) {
		case DE3 :
			if( procType == 1 ){
				return connection.load("SELECT * FROM master_torikomi_shousai_de3 WHERE master_id = ? ORDER BY entry_order;", master_id);
			}else{
				return connection.load("SELECT * FROM master_torikomi_term_shousai_de3 WHERE master_id = ? ORDER BY entry_order;", master_id);
			}
		case SIAS :
			//SIAS使用DBにより更に分岐
			if( procType == 1 ){
				switch(Open21Env.getSIASDBEngine()) {
				case SQLSERVER:	return connection.load("SELECT * FROM master_torikomi_shousai_sias WHERE master_id = ? ORDER BY entry_order;", master_id);
				case POSTGRESQL:	return connection.load("SELECT * FROM master_torikomi_shousai_mk2 WHERE master_id = ? ORDER BY entry_order;", master_id);
				default:			return null;
				}
			}else{
				switch(Open21Env.getSIASDBEngine()) {
				case SQLSERVER:	return connection.load("SELECT * FROM master_torikomi_term_shousai_sias WHERE master_id = ? ORDER BY entry_order;", master_id);
				case POSTGRESQL:	return connection.load("SELECT * FROM master_torikomi_term_shousai_mk2 WHERE master_id = ? ORDER BY entry_order;", master_id);
				default:			return null;
				}
			}
		default:
			return null;
		}
	}


/* 科目マスター(kamoku_master) */
	/**
	 * 勘定科目名（略称）取得: 名称文字列なので直接取るべきという意味で、二重の意味で@Deprecated
	 * @param kamokuCd 勘定科目コード
	 * @return 勘定科目名（略称）
	 */
	@Deprecated
	public GMap findKamokuName(String kamokuCd) {
		final String sql = "SELECT kamoku_name_ryakushiki FROM kamoku_master WHERE kamoku_gaibu_cd = ? ";
		return connection.find(sql, kamokuCd);
	}
	/**
	 * 勘定科目名（略称）取得
	 * @param kamokuCd 勘定科目コード
	 * @return 勘定科目名（略称）---レコードなしならブランク
	 */
	@Deprecated
	public String findKamokuNameStr(String kamokuCd) {
		GMap record = findKamokuName(kamokuCd);
		return (null == record) ? "" : (String)record.get("kamoku_name_ryakushiki");
	}

	/**
	 * 勘定科目コード、勘定科目名（略称）取得
	 * @return リスト
	 */
	@Deprecated
	public List<GMap> loadKamoku(){

		final String sql = "SELECT kamoku_gaibu_cd, kamoku_name_ryakushiki "
						 + "FROM kamoku_master "
						 + "ORDER BY kamoku_gaibu_cd ASC";
		return connection.load(sql);
	}

	/**
	 * 科目マスターより勘定科目コードをキーにレコードを取得する。
	 * @param kamokuCd 勘定科目コード
	 * @return 1件データ
	 */
	@Deprecated
	public GMap findKamokuMaster(String kamokuCd) {
		final String sql = "SELECT * FROM kamoku_master WHERE kamoku_gaibu_cd = ? ";
		return connection.find(sql, kamokuCd);
	}

	/**
	 * 勘定科目名（略称）取得
	 * ★★現預金出納帳向け
	 * @param kamokuCd 勘定科目コード
	 * @return 勘定科目名（略称）---レコードなしならブランク
	 */
	@Deprecated
	public String findKamokuNameStrOnlyGenyokin(String kamokuCd) {
		final String sql = "SELECT kamoku_name_ryakushiki FROM kamoku_master WHERE kamoku_gaibu_cd = ? "
								+ " AND (kamoku_naibu_cd LIKE '010010%' OR kamoku_naibu_cd LIKE '010020%')" ;
		GMap record =  connection.find(sql, kamokuCd);
		return (null == record) ? "" : (String)record.get("kamoku_name_ryakushiki");
	}

	/**
	 * 勘定科目コード、勘定科目名（略称）取得
	 * ★★現預金出納帳向け
	 * @return リスト
	 */
	@Deprecated
	public List<GMap> loadKamokuOnlyGenyokin(){

		final String sql = "SELECT kamoku_gaibu_cd, kamoku_name_ryakushiki "
						 + "FROM kamoku_master "
						 + "WHERE (kamoku_naibu_cd LIKE '010010%' OR kamoku_naibu_cd LIKE '010020%') "
						 + "ORDER BY kamoku_gaibu_cd ASC";
		return connection.load(sql);
	}

/* 科目枝番残高(kamoku_edaban_zandaka) */
	/**
	 * 勘定科目枝番名取得
	 * @param kamokuCd 勘定科目コード
	 * @param kamokuEdabanCd 勘定科目枝番コード
	 * @return 勘定科目枝番名 引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	@Deprecated
	public String findKamokuEdabanName(String kamokuCd, String kamokuEdabanCd) {
		if (null == kamokuCd || kamokuCd.isEmpty() || null == kamokuEdabanCd || kamokuEdabanCd.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT edaban_name FROM kamoku_edaban_zandaka WHERE kamoku_gaibu_cd = ? AND kamoku_edaban_cd = ?";
		Map<String ,Object> record = connection.find(sql, kamokuCd, kamokuEdabanCd);
		return (null == record) ? "" : (String)record.get("edaban_name");
	}

	/**
	 * 科目枝番残高が存在するか調べる
	 * @param kamokuCd 勘定科目コード
	 * @param kamokuEdabanCd 勘定科目枝番コード
	 * @return 存在すればtrue
	 */
	@Deprecated
	public boolean existsKamokuEdabanZandaka(String kamokuCd, String kamokuEdabanCd) {
		final String sql = "SELECT count(*) count FROM kamoku_edaban_zandaka WHERE kamoku_gaibu_cd = ? AND kamoku_edaban_cd = ? AND kessanki_bangou IN (0, 1)";
		return 0 != (long)connection.find(sql, kamokuCd, kamokuEdabanCd).get("count");
	}

	/**
	 * 勘定科目枝番、勘定科目枝番名取得
	 * @return リスト
	 */
	@SuppressWarnings("javadoc")
	@Deprecated
	public List<GMap> loadKamokuEdaban(String kamokuCd){

		final String sql = "SELECT kamoku_edaban_cd ,edaban_name "
						 + "FROM kamoku_edaban_zandaka "
						 + "WHERE kamoku_gaibu_cd = ? "
						 + "ORDER BY kamoku_edaban_cd ASC";
		return connection.load(sql,kamokuCd);
	}
	
	/**
	 * 枝番マスター取得
	 * 科目コードをと科目枝番コードをキーに枝番マスターよりデータを取得します。
	 * @param kamokuCd 勘定科目コード
	 * @param kamokuEdabanCd 勘定科目枝番コード
	 * @return 検索結果
	 */
	@Deprecated
	public GMap finEdabanMasterByCd(String kamokuCd, String kamokuEdabanCd) {
		final String sql = "SELECT * FROM kamoku_edaban_zandaka WHERE kamoku_gaibu_cd = ? AND kamoku_edaban_cd = ?";
		return connection.find(sql, kamokuCd, kamokuEdabanCd);
	}

/* 負担部門(bumon_master) */
	/**
	 * 負担部門名取得
	 * @param futanBumonCd 負担部門コード
	 * @return 負担部門名 引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	@Deprecated
	public String findFutanBumonName(String futanBumonCd) {
		if (null == futanBumonCd || futanBumonCd.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT futan_bumon_name FROM bumon_master WHERE futan_bumon_cd = ?";
		Map<String ,Object> record = connection.find(sql, futanBumonCd);
		return (null == record) ? "" : (String)record.get("futan_bumon_name");
	}

	/**
	 * 部門マスター取得
	 * 部門コードをキーに部門マスターより有効期限内のデータを取得します。
	 * @param bumonCd   部門コード
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findBumonMasterByCd(String bumonCd) {
		final String sql =  "SELECT * FROM bumon_master WHERE futan_bumon_cd =? ";
		return connection.find(sql, bumonCd);
	}

	/**
	 * 部門マスター取得
	 * 負担部門コードをキーに部門マスターより有効期限内のデータを取得します。
	 * ただし指定されたセキュリティパターンで使用できない負担部門の場合はnullを返却します。
	 * @param bumonCd   負担部門コード
	 * @param kiList    取得対象決算期番号リスト
	 * @param sptnList  セキュリティパターンリスト
	 * @return 検索結果
	 */
	public GMap findBumonMasterByCdAndSptn(String bumonCd, List<Integer> kiList, List<Integer> sptnList ) {

		if( sptnList == null || sptnList.isEmpty() || kiList == null || kiList.isEmpty() ){return null;};

		String sptnStr = "";
		for(Integer sp : sptnList){
			sptnStr = sptnStr + sp.toString() +",";
		}
		sptnStr = sptnStr.substring(0, sptnStr.length() - 1);

		String kiStr = "";
		for(Integer ki : kiList){
			kiStr = kiStr + ki.toString() +",";
		}
		kiStr = kiStr.substring(0, kiStr.length() - 1);


		final String sql = "SELECT DISTINCT bm.futan_bumon_cd, bm.futan_bumon_name FROM bumon_master bm "
				+ " INNER JOIN ki_bumon_security kbs "
				+ " ON bm.futan_bumon_cd = kbs.futan_bumon_cd "
				+ " INNER JOIN ( SELECT DISTINCT kesn, kessanki_bangou FROM ki_kesn WHERE kessanki_bangou in("+ kiStr +") ) k "
				+ " ON kbs.kesn = k.kesn "
				+ " WHERE kbs.futan_bumon_cd = ? "
				+ " AND kbs.sptn in ("+ sptnStr +")";
		return connection.find(sql, bumonCd);
	}

	/**
	 * 「負担部門」をコード順に全件取得する。
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> furanBumonSentaku() {
		String sql = " SELECT futan_bumon_cd, futan_bumon_name FROM bumon_master ORDER BY futan_bumon_cd";
		return connection.load(sql);
	}

	/**
	 * 内部決算期のリストから「負担部門」のデータを取得する。
	 * 集計部門が指定されている場合は絞り込みも行う。
	 * セキュリティは参照しない。
	 * @param kiList 取得対象決算期番号リスト
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 検索結果
	 */
	public List<GMap> loadFutanBumonFromNoSptn(List<Integer> kiList, String syuukeiBumonCd) {

		//期のリストで絞り込み(指定なしの場合は全表示)
		String whereKi = "";
		if( kiList != null && !kiList.isEmpty() ){
			String tmpStr = "";
			for(Integer ki : kiList){
				tmpStr = tmpStr + ki.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereKi =  "      WHERE kessanki_bangou in("+ tmpStr +") ";
		}

		//集計部門コードで絞り込み(指定なしの場合は全表示)
		String whereSbcd = "";
		if( syuukeiBumonCd != null && !(syuukeiBumonCd.isEmpty()) ){
			whereSbcd =  " AND sb.syuukei_bumon_cd = '" + syuukeiBumonCd + "' ";
		}

		String mainSql =
				  " SELECT DISTINCT "
				+ "  sb.futan_bumon_cd, "
				+ "  sb.futan_bumon_name, "
				+ "  sb.syuukei_bumon_cd, "
				+ "  k.kesn, "
				+ "  k.kessanki_bangou "
				+ " FROM ki_syuukei_bumon sb "
				+ " INNER JOIN "
				+ "    ( SELECT DISTINCT kesn, kessanki_bangou FROM ki_kesn "
				+ whereKi
				+ " ) k "
				+ " ON sb.kesn = k.kesn "
				+ " WHERE 1 = 1 "
				+ whereSbcd;

		//一番新しい期を優先して取得
		String sql =
				"SELECT * FROM ( "
				+ mainSql
				+ " ) tmp "
				+ " WHERE NOT EXISTS ("
				+ mainSql
				+ "   AND sb.futan_bumon_cd = tmp.futan_bumon_cd"
				+ "   AND k.kessanki_bangou < tmp.kessanki_bangou"
				+ " ) "
				+ " ORDER BY futan_bumon_cd ";

		return connection.load(sql.toString());
	}

	/**
	 * 指定セキュリティパターン・内部決算期のリストから「負担部門」のデータを取得する。
	 * 集計部門が指定されている場合は絞り込みも行う。
	 * @param sptnList セキュリティパターンリスト
	 * @param kiList 取得対象決算期番号リスト
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 検索結果
	 */
	public List<GMap> loadFutanBumonFromSptn(List<Integer> sptnList, List<Integer> kiList, String syuukeiBumonCd) {

		//セキュリティパターンで絞り込み(指定なしの場合は全表示)
		String whereSptn = "";
		if( sptnList != null && !sptnList.isEmpty() ){
			String tmpStr = "";
			for(Integer sp : sptnList){
				tmpStr = tmpStr + sp.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereSptn =  "  AND bs.sptn in (" + tmpStr + ") ";
		}

		//期のリストで絞り込み(指定なしの場合は全表示)
		String whereKi = "";
		if( kiList != null && !kiList.isEmpty() ){
			String tmpStr = "";
			for(Integer ki : kiList){
				tmpStr = tmpStr + ki.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereKi =  "      WHERE kessanki_bangou in("+ tmpStr +") ";
		}

		//集計部門コードで絞り込み(指定なしの場合は全表示)
		String whereSbcd = "";
		if( syuukeiBumonCd != null && !(syuukeiBumonCd.isEmpty()) ){
			whereSbcd =  " AND sb.syuukei_bumon_cd = '" + syuukeiBumonCd + "' ";
		}

		String mainSql =
				  " SELECT DISTINCT "
				+ "  sb.futan_bumon_cd, "
				+ "  sb.futan_bumon_name, "
				+ "  k.kesn, "
				+ "  k.kessanki_bangou "
				+ " FROM ki_bumon_security bs "
				+ " INNER JOIN ki_syuukei_bumon sb "
				+ " ON bs.futan_bumon_cd = sb.syuukei_bumon_cd "
				+ " INNER JOIN "
				+ "    ( SELECT DISTINCT kesn, kessanki_bangou FROM ki_kesn "
				+ whereKi
				+ " ) k "
				+ " ON sb.kesn = k.kesn "
				+ " AND bs.kesn = sb.kesn "
				+ " WHERE 1 = 1 "
				+ whereSptn
				+ whereSbcd;

		//一番新しい期を優先して取得
		String sql =
				"SELECT * FROM ( "
				+ mainSql
				+ " ) tmp "
				+ " WHERE NOT EXISTS ("
				+ mainSql
				+ "   AND sb.futan_bumon_cd = tmp.futan_bumon_cd"
				+ "   AND k.kessanki_bangou < tmp.kessanki_bangou"
				+ " ) "
				+ " ORDER BY futan_bumon_cd ";

		return connection.load(sql.toString());
	}

	/**
	 * 指定セキュリティパターン・内部決算期のリストから「負担部門」のデータを取得する。
	 * 集計部門が指定されている場合は絞り込みも行う。
	 * @param sptnList セキュリティパターンリスト
	 * @param kiList 取得対象決算期番号リスト
	 * @param syuukeiBumonCdFrom 集計部門コードFrom
	 * @param syuukeiBumonCdTo 集計部門コードTo
	 * @return 検索結果
	 */
	public List<GMap> loadFutanBumonFromSptn(List<Integer> sptnList, List<Integer> kiList, String syuukeiBumonCdFrom, String syuukeiBumonCdTo) {

		//セキュリティパターンで絞り込み(指定なしの場合は全表示)
		String whereSptn = "";
		if( sptnList != null && !sptnList.isEmpty() ){
			String tmpStr = "";
			for(Integer sp : sptnList){
				tmpStr = tmpStr + sp.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereSptn =  "  AND bs.sptn in (" + tmpStr + ") ";
		}

		//期のリストで絞り込み(指定なしの場合は全表示)
		String whereKi = "";
		if( kiList != null && !kiList.isEmpty() ){
			String tmpStr = "";
			for(Integer ki : kiList){
				tmpStr = tmpStr + ki.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereKi =  "      WHERE kessanki_bangou in("+ tmpStr +") ";
		}

		//集計部門コードで絞り込み(指定なしの場合は全表示)
		String whereSbcd = "";
		if( syuukeiBumonCdFrom != null && !(syuukeiBumonCdFrom.isEmpty()) ){
			whereSbcd +=  " AND sb.syuukei_bumon_cd >= '" + syuukeiBumonCdFrom + "' ";
		}
		if( syuukeiBumonCdTo != null && !(syuukeiBumonCdTo.isEmpty()) ){
			whereSbcd +=  " AND sb.syuukei_bumon_cd <= '" + syuukeiBumonCdTo + "' ";
		}

		String mainSql =
				  " SELECT DISTINCT "
				+ "  sb.futan_bumon_cd, "
				+ "  sb.futan_bumon_name, "
				+ "  k.kesn, "
				+ "  k.kessanki_bangou "
				+ " FROM ki_bumon_security bs "
				+ " INNER JOIN ki_syuukei_bumon sb "
				+ " ON bs.futan_bumon_cd = sb.syuukei_bumon_cd "
				+ " INNER JOIN "
				+ "    ( SELECT DISTINCT kesn, kessanki_bangou FROM ki_kesn "
				+ whereKi
				+ " ) k "
				+ " ON sb.kesn = k.kesn "
				+ " AND bs.kesn = sb.kesn "
				+ " WHERE 1 = 1 "
				+ whereSptn
				+ whereSbcd;

		//一番新しい期を優先して取得
		String sql =
				"SELECT * FROM ( "
				+ mainSql
				+ " ) tmp "
				+ " WHERE NOT EXISTS ("
				+ mainSql
				+ "   AND sb.futan_bumon_cd = tmp.futan_bumon_cd"
				+ "   AND k.kessanki_bangou < tmp.kessanki_bangou"
				+ " ) "
				+ " ORDER BY futan_bumon_cd ";

		return connection.load(sql.toString());
	}

	/**
	 * 指定セキュリティパターン・内部決算期のリストから「集計部門」のデータを取得する。
	 * 集計部門が指定されている場合は絞り込みも行う。(存在チェック用)
	 * @param sptnList セキュリティパターンリスト
	 * @param kiList 取得対象決算期番号リスト
	 * @param syuukeiBumonCd 集計部門コード
	 * @return 検索結果
	 */
	public List<GMap> loadSyuukeiBumonFromSptn(List<Integer> sptnList, List<Integer> kiList, String syuukeiBumonCd) {
		//セキュリティパターンで絞り込み(指定なしの場合は全表示)
		String whereSptn = "";
		if( sptnList != null && !sptnList.isEmpty() ){
			String tmpStr = "";
			for(Integer sp : sptnList){
				tmpStr = tmpStr + sp.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereSptn =  "  AND bs.sptn in (" + tmpStr + ") ";
		}

		//期のリストで絞り込み(指定なしの場合は全表示)
		String whereKi = "";
		if( kiList != null && !kiList.isEmpty() ){
			String tmpStr = "";
			for(Integer ki : kiList){
				tmpStr = tmpStr + ki.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			whereKi =  "      WHERE kessanki_bangou in("+ tmpStr +") ";
		}

		//集計部門コードで絞り込み(指定なしの場合は全表示)
		String whereSbcd = "";
		if( syuukeiBumonCd != null && !(syuukeiBumonCd.isEmpty()) ){
			whereSbcd =  " AND sb.syuukei_bumon_cd = '" + syuukeiBumonCd + "' ";
		}

		String mainSql =
				  " SELECT DISTINCT "
				+ "  sb.syuukei_bumon_cd, "
				+ "  sb.syuukei_bumon_name, "
				+ "  k.kesn, "
				+ "  k.kessanki_bangou "
				+ " FROM ki_syuukei_bumon sb "
				+ " LEFT OUTER JOIN ki_bumon_security bs ON "
				+ "   (sb.kesn,sb.syuukei_bumon_cd) = (bs.kesn,bs.futan_bumon_cd) "
				+ " INNER JOIN "
				+ "    ( SELECT DISTINCT kesn, kessanki_bangou FROM ki_kesn "
				+ whereKi
				+ " ) k "
				+ " ON sb.kesn = k.kesn "
				+ " WHERE 1 = 1 "
				+ whereSptn
				+ whereSbcd;
		//一番新しい期を優先して取得
		String sql =
				"SELECT * FROM ( "
				+ mainSql
				+ " ) tmp "
				+ " WHERE NOT EXISTS ("
				+ mainSql
				+ "   AND sb.syuukei_bumon_cd = tmp.syuukei_bumon_cd"
				+ "   AND k.kessanki_bangou < tmp.kessanki_bangou"
				+ " ) "
				+ " ORDER BY syuukei_bumon_cd ";

		return connection.load(sql.toString());
}

	/**
	 * 「(期別)決算期」から指定日が開始日～終了日の範囲内のレコードを取得
	 * @param date 日付
	 * @return 内部決算期
	 */
	@Deprecated
	public GMap findKiKesn(Date date) {
		final String sql = "SELECT * FROM ki_kesn WHERE ? BETWEEN from_date AND to_date";
		return connection.find(sql, date);
	}

	/**
	 * 「(期別)決算期」から内部決算期(KESN)を取得
	 * @param date 日付
	 * @return 内部決算期
	 */
	public int findKesn(Date date) {
		GMap record = findKiKesn(date);
		return (record == null) ? 0 : (int)record.get("kesn");
	}

	/**
	 * 「(期別)決算期」から決算期番号(kessanki_bangou)を取得
	 * @param date 日付
	 * @return 決算期番号(取得できなかった場合は-1)
	 */
	public int findKessankiBangou(Date date) {
		GMap record = findKiKesn(date);
		return (record == null) ? -1 : (int)record.get("kessanki_bangou");
	}

	/**
	 * 「(期別)決算期」から決算期番号を条件に内部決算期(KESN)を取得
	 * @param kessankiBangou 決算期番号
	 * @return 決算期
	 */
	public int findKesnWhereKessankiBangou(int kessankiBangou) {
		final String sql = "SELECT DISTINCT kesn FROM ki_kesn WHERE kessanki_bangou = ?";
		GMap record = connection.find(sql, kessankiBangou);
		return (record == null) ? 0 : (int)record.get("kesn");
	}

	/**
	 * 「(期別)決算期」から年度開始日(DD-MM形式)を取得
	 * @return 年度開始日
	 */
	public String findKesnFromMMDD (){
		final String sql = "SELECT DISTINCT TO_CHAR(MIN(from_date), 'MM-DD') AS from_date_mmdd FROM ki_kesn GROUP BY kesn";
		GMap record = connection.find(sql);
		return (record == null) ? "0101" : record.get("from_date_mmdd").toString();
	}

	/**
	 * 「(期別)決算期」から年度終了日を取得
	 * @param kesn 決算期
	 * @return 年度終了日
	 */
	public Date findToDate(int kesn){
		final String sql = "SELECT to_date FROM ki_kesn WHERE kesn = ? ORDER BY from_date DESC LIMIT 1";
		GMap record = connection.find(sql, kesn);
		return (record == null) ? null : (Date)record.get("to_date");
	}

	/**
	 * 「(期別)決算期」から年度開始日を取得
	 * @param kesn 決算期
	 * @return 年度開始日
	 */
	public Date findFromDate(int kesn){
		final String sql = "SELECT from_date FROM ki_kesn WHERE kesn = ? ORDER BY from_date ASC LIMIT 1";
		GMap record = connection.find(sql, kesn);
		return (record == null) ? null : (Date)record.get("from_date");
	}

	/**
	 * 「(期別)決算期」から終了日を取得
	 * @param kesn 決算期
	 * @param targetDate 対象日付
	 * @return 対象月末日
	 */
	public Date findToDate(int kesn, Date targetDate){
		final String sql = "SELECT to_date FROM ki_kesn WHERE kesn = ? AND ? BETWEEN from_date AND to_date";
		GMap record = connection.find(sql, kesn, targetDate);
		return (record == null) ? null : (Date)record.get("to_date");
	}


	/**
	 * 「(期別)決算期」からすべての終了日を取得
	 * @return 結果マップ（終了日）
	 */
	public List<GMap> loadKesnToDateAll() {
		return connection.load("SELECT * FROM ki_kesn ORDER BY from_date DESC");
	}

	/**
	 * 「集計部門コード」「決算期番号」をキーに(期別)集計部門テーブルから集計部門名を取得する。
	 * 「集計部門コード」の指定が無い場合は指定決算期番号内の全データを取得する。
	 * @param syuukeiBumonCdList   集計部門コードリスト
	 * @param kiList               取得対象決算期番号リスト
	 * @return 検索結果
	 */
	public List<GMap> loadSyuukeiBumonList(List<String> syuukeiBumonCdList, List<Integer> kiList) {
		String addWhereSql = "";
		if ( syuukeiBumonCdList != null && (!syuukeiBumonCdList.isEmpty()) ){
			String syuukeiBumonCd = "'" + String.join("','", syuukeiBumonCdList) + "'";
			addWhereSql = "       AND syuukei_bumon_cd in ( " + syuukeiBumonCd + " ) ";
		}
		String kiStr = "";
		if(kiList != null && !(kiList.isEmpty()) ){
			for(Integer ke : kiList){
				kiStr = kiStr + ke.toString() +",";
			}
			kiStr = kiStr.substring(0, kiStr.length() - 1);
		}else{
			return null;
		}


		String mainSql =
				  " SELECT DISTINCT "
				+ "       sb.syuukei_bumon_cd, "
				+ "       sb.syuukei_bumon_name, "
				+ "       k.kessanki_bangou "
				+ "       FROM ki_syuukei_bumon sb "
				+ " INNER JOIN "
				+ "    ( SELECT DISTINCT kesn, kessanki_bangou FROM ki_kesn "
				+ "      WHERE kessanki_bangou in("+ kiStr +") ) k "
				+ " ON sb.kesn = k.kesn ";

		//一番新しい期を優先して取得
		String sql =
				"SELECT DISTINCT "
				+ " syuukei_bumon_cd, "
				+ " syuukei_bumon_name "
				+ " FROM ( "
				+ mainSql
				+ " ) tmp "
				+ " WHERE NOT EXISTS ("
				+ mainSql
				+ "   WHERE "
				+ "   sb.syuukei_bumon_cd = tmp.syuukei_bumon_cd"
				+ "   AND  k.kessanki_bangou < tmp.kessanki_bangou"
				+ " ) "
				+ addWhereSql
				+ " ORDER BY syuukei_bumon_cd ";
		return connection.load(sql.toString());
	}


	/**
	 * 内部決算期を指定し、「集計部門」のデータを全て取得する。
	 * @param kesn 内部決算期
	 * @param sptnList セキュリティパターンリスト
	 * @param isMeisaiSptn 明細部門／集計部門いずれを絞り込み対象とするか（trueの場合は明細部門）
	 *
	 * @return 検索結果
	 */
	public List<GMap> loadSyuukeiBumonForKeihiMeisai(int kesn, List<Integer> sptnList, boolean isMeisaiSptn) {

		String strSptnList = "";
		if(sptnList != null && !(sptnList.isEmpty()) ){
			String tmpStr = "";
			for(Integer sp : sptnList){
				tmpStr = tmpStr + sp.toString() +",";
			}
			tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
			strSptnList =  "  AND sec.sptn in (" + tmpStr + ") ";
		}

		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT ");
		sql.append("  sb.syuukei_bumon_cd, ");
		sql.append("  sb.syuukei_bumon_name ");
		sql.append("FROM ki_syuukei_bumon sb ");
		sql.append("LEFT OUTER JOIN ki_bumon_security sec ON ");
		if(isMeisaiSptn){
			sql.append("  (sb.kesn, sb.futan_bumon_cd) = (sec.kesn, sec.futan_bumon_cd) ");
		}else{
			sql.append("  (sb.kesn, sb.syuukei_bumon_cd) = (sec.kesn, sec.futan_bumon_cd) ");
		}
		sql.append("WHERE ");
		sql.append("  sb.kesn = ? ");
		sql.append(strSptnList);
		sql.append("ORDER BY ");
		sql.append("  sb.syuukei_bumon_cd ");
		params.add(kesn);
		return connection.load(sql.toString(), params.toArray());
	}

/* 部門科目残高(bumon_kamoku_zandaka) */
	/**
	 * 負担部門科目残高が存在するか調べる
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuCd 勘定科目コード
	 * @return 存在すればtrue
	 */
	public boolean existsFutanBumonKamokuZandaka(String futanBumonCd, String kamokuCd) {
		final String sql = "SELECT count(*) count FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kessanki_bangou IN (0, 1)";
		return 0 != (long)connection.find(sql, futanBumonCd, kamokuCd).get("count");
	}

/* 取引先マスター(torihikisaki_master) */
	/**
	 * 「取引先マスター」「取引先」「金融機関」を連結し検索する。
	 * @param torihikisakiCd    取引先コード
	 * @param shiiresakiFlg      仕入先のみに特定する
	 * @return 取引先情報
	 */
	public GMap findTorihikisakiJouhou(String torihikisakiCd, boolean shiiresakiFlg){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  tm.torihikisaki_cd, ");               // 取引先コード
		sql.append("  tm.torihikisaki_name_ryakushiki, ");  // 取引先名（略称）
		sql.append("  tm.menzei_jigyousha_flg, ");          // 免税事業者フラグ
		sql.append("  tm.tekikaku_no, ");                   // 適格請求書発行事業者登録番号
		sql.append("  t.kouza_bangou, ");                   // 口座番号
		sql.append("  t.kouza_meiginin, ");                 // 口座名義人
		sql.append("  t.yokin_shubetsu, ");                 // 預金種別
		sql.append("  t.furikomi_ginkou_cd, ");             // 振込銀行コード
		sql.append("  t.furikomi_ginkou_shiten_cd, ");      // 振込銀行支店コード
		sql.append("  k.kinyuukikan_name_kana, ");          // 金融機関名カナ
		sql.append("  k.shiten_name_kana,  ");              // 支店名カナ
		sql.append("  n.name yokin_shubetsu_name ");        // 支払種別（コード名）
		sql.append("FROM ");
		sql.append("  torihikisaki_master tm ");
		sql.append("    LEFT OUTER JOIN torihikisaki t ");
		sql.append("      ON tm.torihikisaki_cd = t.torihikisaki_cd ");
		sql.append("    LEFT OUTER JOIN kinyuukikan k  ");
		sql.append("      ON t.furikomi_ginkou_cd = k.kinyuukikan_cd AND t.furikomi_ginkou_shiten_cd = k.kinyuukikan_shiten_cd ");
		sql.append("    LEFT OUTER JOIN naibu_cd_setting n ");
		sql.append("      ON n.naibu_cd_name = 'yokin_shubetsu' AND n.naibu_cd = t.yokin_shubetsu ");
		sql.append("WHERE 1 = 1 ");

		List<Object> params = new ArrayList<>();
		if(torihikisakiCd != null && torihikisakiCd.length() > 0) {
			sql.append(" AND tm.torihikisaki_cd = ? ");
			params.add(torihikisakiCd);
		}
		if(shiiresakiFlg) {
			sql.append(" AND t.torihikisaki_cd IS NOT NULL ");
		}
		return connection.find(sql.toString(), params.toArray());
	}

	/**
	 * 「取引先マスター」「取引先」「金融機関」を連結し検索する。
	 * @param torihikisakiCd    取引先コード
	 * @param torihikisakiName  取引先名
	 * @param shiiresakiFlg      仕入先のみに特定する
	 * @param ichigensakiFlg    仕込先のみの場合に一見先も含む
	 * @return リスト
	 */
	public List<GMap> selectTorihikisakiJouhou(String torihikisakiCd, String torihikisakiName, boolean shiiresakiFlg, boolean ichigensakiFlg){
		int maxPerPage = Integer.parseInt(setting.recordNumPerPageTorihikisaki());
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  tm.torihikisaki_cd, ");               // 取引先コード
		sql.append("  tm.torihikisaki_name_ryakushiki, ");  // 取引先名（略称）
		sql.append("  t.kouza_bangou, ");                   // 口座番号
		sql.append("  t.kouza_meiginin, ");                 // 口座名義人
		sql.append("  t.yokin_shubetsu, ");                 // 預金種別
		sql.append("  t.furikomi_ginkou_cd, ");             // 振込銀行コード
		sql.append("  t.furikomi_ginkou_shiten_cd, ");      // 振込銀行支店コード
		sql.append("  k.kinyuukikan_name_kana, ");          // 金融機関名カナ
		sql.append("  k.shiten_name_kana,  ");              // 支店名カナ
		sql.append("  n.name yokin_shubetsu_name ");        // 支払種別（コード名）
		sql.append("FROM ");
		sql.append("  torihikisaki_master tm ");
		sql.append("    LEFT OUTER JOIN torihikisaki t ");
		sql.append("      ON tm.torihikisaki_cd = t.torihikisaki_cd ");
		sql.append("    LEFT OUTER JOIN kinyuukikan k  ");
		sql.append("      ON t.furikomi_ginkou_cd = k.kinyuukikan_cd AND t.furikomi_ginkou_shiten_cd = k.kinyuukikan_shiten_cd ");
		sql.append("    LEFT OUTER JOIN naibu_cd_setting n ");
		sql.append("      ON n.naibu_cd_name = 'yokin_shubetsu' AND n.naibu_cd = t.yokin_shubetsu ");
		sql.append("WHERE 1 = 1 ");

		List<Object> params = new ArrayList<>();
		if(torihikisakiCd != null && torihikisakiCd.length() > 0) {
			sql.append(" AND replace(replace(tm.torihikisaki_cd, ' ',''), '　', '') LIKE ? ");
			params.add("%" + torihikisakiCd.replace(" ", "").replace("　",  "").replace(" ", "") + "%");//3つめは&nbsp;
		}
		if(torihikisakiName != null && torihikisakiName.length() > 0) {
			sql.append(" AND unify_kana_width(replace(replace(tm.torihikisaki_name_ryakushiki, ' ',''), '　', '')) LIKE unify_kana_width(?) ");
			params.add("%" + torihikisakiName.replace(" ", "").replace("　",  "").replace(" ", "") + "%");
		}
		if(shiiresakiFlg) {
			if(ichigensakiFlg) {
				sql.append(" AND (t.torihikisaki_cd IS NOT NULL OR tm.torihikisaki_cd = ? ) ");
				params.add(setting.ichigenCd());
			}else {
				sql.append(" AND t.torihikisaki_cd IS NOT NULL ");
			}
		}
		// ソート順
		sql.append("ORDER BY tm.torihikisaki_cd ");
		sql.append("LIMIT " + (maxPerPage + 1));
		return connection.load(sql.toString(), params.toArray());
	}

	/**
	 * 取引先名（略式） 取得
	 * @param torihikisakiCd 取引先コード
	 * @return 取引先名（略式）  引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findTorihikisakiName(String torihikisakiCd) {
		if (null == torihikisakiCd || torihikisakiCd.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT torihikisaki_name_ryakushiki FROM torihikisaki_master WHERE torihikisaki_cd = ?";
		Map<String ,Object> record = connection.find(sql, torihikisakiCd);
		return (null == record) ? "" : (String)record.get("torihikisaki_name_ryakushiki");
	}

	/**
	 * 取引先名（略式） 取得
	 * @param torihikisakiCd 取引先コード
	 * @param shiiresakiFlg    	 	仕入先のみに特定する
	 * @return 取引先名（略式）	引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findTorihikisakiName(String torihikisakiCd, boolean shiiresakiFlg) {
		if (null == torihikisakiCd || torihikisakiCd.isEmpty())
		{
			return "";
		}
		String sql = "SELECT torihikisaki_name_ryakushiki FROM torihikisaki_master tm LEFT OUTER JOIN torihikisaki t USING(torihikisaki_cd) WHERE tm.torihikisaki_cd = ?";
		if(shiiresakiFlg) {
			sql +=  " AND t.torihikisaki_cd IS NOT NULL ";
		}
		Map<String ,Object> record = connection.find(sql, torihikisakiCd);
		return (null == record) ? "" : (String)record.get("torihikisaki_name_ryakushiki");
	}

/* 取引先科目残高(torihikisaki_kamoku_zandaka) */
	/**
	 * 取引先科目残高が存在するか調べる。
	 * @param torihikisakiCd 取引先コード
	 * @param kamokuCd 科目コード
	 * @return 存在すればtrue
	 */
	public boolean existsTorihikisakiKamokuZandaka(String torihikisakiCd, String kamokuCd) {
		final String sql = "SELECT count(*) count FROM torihikisaki_kamoku_zandaka WHERE torihikisaki_cd = ? AND kamoku_gaibu_cd = ?";
		return 0 != (long)connection.find(sql, torihikisakiCd, kamokuCd).get("count");
	}

/* 取引先(torihikisaki) */
	/**
	 * 取引先が支払依頼対象であるか？
	 * 取引先の支払先区分が「1：約定A」、「6：銀行振込」、「7：期日振込」以外だめ
	 * @param torihikisakiCd 取引先コード
	 * @return 支払依頼対象である
	 */
	public boolean shiharaiTaishou(String torihikisakiCd){
		if (isEmpty(torihikisakiCd))
		{
			return true;
		}

		GMap torihikisaki = connection.find("SELECT * FROM torihikisaki WHERE torihikisaki_cd = ?", torihikisakiCd);
		if (torihikisaki == null)
		{
			return false;
		}
		String toriShiharaiShu = n2b(torihikisaki.get("shiharai_shubetsu"));
		return toriShiharaiShu.equals(SHIHARAI_SHUBETSU.YAKUTEI_A) || toriShiharaiShu.equals(SHIHARAI_SHUBETSU.GINKOU_FURIKOMI) || toriShiharaiShu.equals(SHIHARAI_SHUBETSU.KIJITSU_FURIKOMI);
	}

/* ユニバーサルフィールド#残高(uf#_zandaka) */
	/**
	 * ユニバーサルフィールド残高が存在するか調べる
	 * @param no 1-10 の何れか(UF1/2/3..のどれをさすか)
	 * @param ufCd UFコード
	 * @param kamokuCd 勘定科目コード
	 * @return 存在すればtrue
	 */
	public boolean existsUFZandaka(String no, String ufCd, String kamokuCd) {
		String sql = "SELECT count(*) count FROM uf#_zandaka WHERE uf#_cd = ? AND kamoku_gaibu_cd = ?";
		sql = sql.replaceAll("#", no);
		return 0 != (long)connection.find(sql, ufCd, kamokuCd).get("count");
	}

	/* ユニバーサルフィールド(固定)#残高(uf_kotei#_zandaka) */
	/**
	 * ユニバーサルフィールド(固定)残高が存在するか調べる
	 * @param no 1-10 の何れか(uf_kotei1/2/3..のどれをさすか)
	 * @param ufKoteiCd UF(固定)コード
	 * @param kamokuCd 勘定科目コード
	 * @return 存在すればtrue
	 */
	public boolean existsUFKoteiZandaka(String no, String ufKoteiCd, String kamokuCd) {
		String sql = "SELECT count(*) count FROM uf_kotei#_zandaka WHERE uf_kotei#_cd = ? AND kamoku_gaibu_cd = ?";
		sql = sql.replaceAll("#", no);
		return 0 != (long)connection.find(sql, ufKoteiCd, kamokuCd).get("count");
	}

/* ユニバーサルフィールド#一覧(uf#_ichiran) */
	/**
	 * UF名称（略式）を取得する。
	 * @param no 1-10 の何れか(UF1/2/3..のどれをさすか)
	 * @param ufCd String
	 * @return UF名称（略式）  引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findUfName(String no, String ufCd) {
		if (null == ufCd || ufCd.isEmpty())
		{
			return "";
		}
		String sql = "SELECT uf#_name_ryakushiki as uf_name_ryakushiki FROM uf#_ichiran WHERE uf#_cd = ?";
		sql = sql.replaceAll("#", no);
		Map<String ,Object> record = connection.find(sql, ufCd);
		return (null == record) ? "" : (String)record.get("uf_name_ryakushiki");
	}

	/**
	 * UF固定値名称（略式）を取得する。
	 * @param no 1-10 の何れか(UF1/2/3..のどれをさすか)
	 * @param ufCd String
	 * @return UF名称（略式）  引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findUfKoteiName(String no, String ufCd) {
		if (null == ufCd || ufCd.isEmpty())
		{
			return "";
		}
		String sql = "SELECT uf_kotei#_name_ryakushiki as uf_kotei_name_ryakushiki FROM uf_kotei#_ichiran WHERE uf_kotei#_cd = ?";
		sql = sql.replaceAll("#", no);
		Map<String ,Object> record = connection.find(sql, ufCd);
		return (null == record) ? "" : (String)record.get("uf_kotei_name_ryakushiki");
	}

	/**
	 * UF#の一覧を取得する。
	 * @param no 1-10 の何れか(UF1/2/3..のどれをさすか)
	 * @return 指定UFの一覧
	 */
	public List<GMap> loadUF(String no) {
		String sql = "SELECT uf#_cd uf_cd, uf#_name_ryakushiki uf_name_ryakushiki FROM uf#_ichiran ORDER BY uf#_cd";
		sql = sql.replaceAll("#", no);
		return connection.load(sql);
	}

	/**
	 * UF固定値#の一覧を取得する。
	 * @param no 1-10 の何れか(UF1/2/3..のどれをさすか)
	 * @return 指定UFの一覧
	 */
	public List<GMap> loadUFKotei(String no) {
		String sql = "SELECT uf_kotei#_cd uf_cd, uf_kotei#_name_ryakushiki uf_name_ryakushiki FROM uf_kotei#_ichiran ORDER BY uf_kotei#_cd";
		sql = sql.replaceAll("#", no);
		return connection.load(sql);
	}

	/**
	 * HF名称（略式）を取得する。
	 * @param no 1-10 の何れか(HF1/2/3..のどれをさすか)
	 * @param hfCd String
	 * @return HF名称（略式）  引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findHfName(String no, String hfCd) {
		if (null == hfCd || hfCd.isEmpty())
		{
			return "";
		}
		String sql = "SELECT hf#_name_ryakushiki as hf_name_ryakushiki FROM hf#_ichiran WHERE hf#_cd = ?";
		sql = sql.replaceAll("#", no);
		Map<String ,Object> record = connection.find(sql, hfCd);
		return (null == record) ? "" : (String)record.get("hf_name_ryakushiki");
	}

	/**
	 * HF#の一覧を取得する。
	 * @param no 1-10 の何れか(HF1/2/3..のどれをさすか)
	 * @return 指定HFの一覧
	 */
	public List<GMap> loadHF(String no) {
		String sql = "SELECT hf#_cd hf_cd, hf#_name_ryakushiki hf_name_ryakushiki FROM hf#_ichiran ORDER BY hf#_cd";
		sql = sql.replaceAll("#", no);
		return connection.load(sql);
	}

/* 日付コントロール(hizuke_control) */
	/**
	 * 日付コントロールのレコードを取得する。
	 * @param kojinseisanShiharaibi 個人精算支払日
	 * @return 日付コントロールレコード
	 */
	public GMap findKojinSeisanShiharaibi(Date kojinseisanShiharaibi) {
		final String sql = "SELECT * FROM hizuke_control WHERE kojinseisan_shiharaibi = ?";
		return connection.find(sql, kojinseisanShiharaibi);
	}
	/**
	 * 日付コントロールのレコードを取得する。
	 * @param kojinseisanShiharaibi 個人精算支払日
	 * @return 日付コントロールレコード
	 */
	public Date findKojinSeisanShiharaibiAfter(Date kojinseisanShiharaibi) {
		final String sql = "SELECT MIN(kojinseisan_shiharaibi) kojinseisan_shiharaibi FROM hizuke_control WHERE kojinseisan_shiharaibi >= ?";
		return (Date)connection.find(sql, kojinseisanShiharaibi).get("kojinseisan_shiharaibi");
	}

	/**
	 * 日付コントロール上で、指定日以前の最後の営業日を返す。
	 * ※日付コントロールでヒットしなければ指定日そのままとする。（設定ミスだけど）
	 * @param d 指定日
	 * @return 指定日以前の最後の営業日
	 */
	public Date findEigyoubiBefore(Date d) {
		final String sql = "SELECT MAX(system_kanri_date) d FROM hizuke_control WHERE system_kanri_date <= ? AND kinyuukikan_eigyoubi_flg = '1'";
		GMap m = connection.find(sql, d);
		return (m == null) ? d : (Date)m.get("d");
	}

	/**
	 * 日付コントロール上で、指定日以降の最後の営業日を返す。
	 * ※日付コントロールでヒットしなければ指定日そのままとする。（設定ミスだけど）
	 * @param d 指定日
	 * @return 指定日以降の最後の営業日
	 */
	public Date findEigyoubiAfter(Date d) {
		final String sql = "SELECT MIN(system_kanri_date) d FROM hizuke_control WHERE system_kanri_date >= ? AND kinyuukikan_eigyoubi_flg = '1'";
		GMap m = connection.find(sql, d);
		return (m == null) ? d : (Date)m.get("d");
	}
	/**
	 * 計上日フラグ=1のリストを取得
	 * @param denpyouKbn 伝票区分
	 * @return 計上日リスト
	 */
	public List<String> loadKeijoubiList(String denpyouKbn) {
		int fromVal = 0;
		int toVal = 0;
		try {
			if(!isEmpty(denpyouKbn)){
				fromVal = Integer.parseInt(EteamSettingInfo.getSettingInfo("keijou_hani_mae_" + denpyouKbn));
				toVal = Integer.parseInt(EteamSettingInfo.getSettingInfo("keijou_hani_ato_" + denpyouKbn));
			}else {
				fromVal = connection.find("select MAX((NULLIF(setting_val, '')::int)) as val from setting_info WHERE setting_name LIKE 'keijou_hani_mae_%';").get("val");
				toVal = connection.find("select MAX((NULLIF(setting_val, '')::int)) as val from setting_info WHERE setting_name LIKE 'keijou_hani_ato_%';").get("val");
			}
		} catch(NumberFormatException|NullPointerException e){
			//該当なしの伝票区分だとここに入る可能性あり・・・空で返しとく
			List<String> retList = new ArrayList<>();
			return retList;
		}

		Date today = EteamCommon.today();

		Date from = new Date(DateUtils.addMonths(DateUtils.setDays(today, 1), -fromVal).getTime());
		Date to = new Date(DateUtils.addDays(DateUtils.setDays(DateUtils.addMonths(today, toVal+1), 1), -1).getTime());

		final String sql = "SELECT system_kanri_date FROM hizuke_control WHERE keijoubi_flg='1' AND system_kanri_date BETWEEN ? AND ? ORDER BY system_kanri_date";
		List<GMap> list = connection.load(sql, from, to);
		List<Date> dList = new ArrayList<>();
		for(GMap m : list) dList.add(m.get("system_kanri_date"));

		List<String> retList = new ArrayList<>();
		for(Date d : dList) retList.add(DateFormatUtils.format(d, "yyyy/MM/dd"));
		return retList;
	}

/* 金融機関(kinyuukikan) */
	/**
	 * @return 金融機関リスト
	 */
	public List<GMap> loadKinyuukikan() {
		final String sql = "SELECT * FROM kinyuukikan ORDER BY kinyuukikan_cd, kinyuukikan_shiten_cd;";
		return connection.load(sql);
	}

/* 金融機関(open21_kinyuukikan) */
	/**
	 * @return 金融機関リスト
	 */
	public List<GMap> loadOpen21Kinyuukikan() {
		final String sql = "SELECT * FROM open21_kinyuukikan ORDER BY kinyuukikan_cd, kinyuukikan_shiten_cd;";
		return connection.load(sql);
	}

/* 振込元口座(moto_kouza) */
	/**
	 * @return 振込元口座リスト
	 */
	@Deprecated
	public List<GMap> loadMotoKouza() {
		final String sql = "SELECT * FROM moto_kouza ORDER BY moto_kinyuukikan_cd, moto_kinyuukikan_shiten_cd, moto_yokinshubetsu ,moto_kouza_bangou;";
		return connection.load(sql);
	}

/* 消費税率(shouhizeiritsu) */
	/**
	 * 消費税率取得
	 * 全ての消費税率を取得します。（もともとは有効期限内の税率を取得していた様子）
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadshouhizeiritsu() {
		final String sql = "SELECT zeiritsu, keigen_zeiritsu_kbn, hasuu_keisan_kbn, yuukou_kigen_from, yuukou_kigen_to "
						+ "FROM shouhizeiritsu "
// + "WHERE current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to "
						+ "ORDER BY sort_jun, zeiritsu ;";
		return connection.load(sql);
	}

	/**
	 * 消費税率取得
	 * 本日、または過去の消費税率（軽減税率は除く）を取得します。基準日はシステム日付
	 * @return 結果マップ
	 */
	public List<GMap> loadShouhizeiritsuExclusionKeigen(){
		return loadshouhizeiritsu().stream()
										.filter(m -> m.get("keigen_zeiritsu_kbn").equals(EteamConst.keigenZeiritsuKbn.NORMAL))
										.collect(Collectors.toList());
	}

	/**
	 * システム日付ベースで有効な消費税率（軽減税率除く）を取得する。
	 * @return 消費税率
	 */
	public BigDecimal findValidShouhizeiritsuExclusionKeigen() {
		return findValidShouhizeiritsu(new Date(System.currentTimeMillis()), keigenZeiritsuKbn.NORMAL);
	}

	/**
	 * 指定の日付ベースで有効な消費税率を取得する。
	 * @param baseDate 基準日
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @return 消費税率
	 */
	public BigDecimal findValidShouhizeiritsu(Date baseDate, String keigenZeiritsuKbn) {
		GMap record = findValidShouhizeiritsuMap(baseDate, keigenZeiritsuKbn);
		return (null == record) ? null : (BigDecimal)record.get("zeiritsu");
	}

	/**
	 * システム日付ベースで有効な消費税率を取得する。
	 * @return 消費税率
	 */
	public GMap findValidShouhizeiritsuMap() {
		return findValidShouhizeiritsuMap(new Date(System.currentTimeMillis()), null);
	}

	/**
	 * 指定の日付ベースで有効な消費税率を取得する。
	 * @param baseDate 基準日
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @return 消費税率
	 */
	public GMap findValidShouhizeiritsuMap(Date baseDate, String keigenZeiritsuKbn) {
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> params = new ArrayList<Object>();
		sql.append("SELECT zeiritsu ,keigen_zeiritsu_kbn ");
		sql.append("FROM shouhizeiritsu ");
		sql.append("WHERE ? BETWEEN yuukou_kigen_from AND yuukou_kigen_to ");
		params.add(baseDate);

		// null以外は指定した軽減税率区分で絞り込む
		if(null != keigenZeiritsuKbn) {
			sql.append("  AND keigen_zeiritsu_kbn = ? ");
			params.add(keigenZeiritsuKbn);
		}

		sql.append("ORDER BY sort_jun ");
		sql.append("LIMIT 1");
		return connection.find(sql.toString(), params.toArray());
	}

	/**
	 * 消費税率が存在するか調べる
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @return 存在すればtrue
	 */
	public Boolean existsZeritsu(BigDecimal zeiritsu, String keigenZeiritsuKbn){

		final String sql = "SELECT COUNT(*) AS count FROM shouhizeiritsu WHERE zeiritsu = ? AND keigen_zeiritsu_kbn = ?";
		GMap record = connection.find(sql, zeiritsu, keigenZeiritsuKbn);
		int count = Integer.parseInt(record.get("count").toString());

		return (count > 0);
	}
	/**
	 * 基準日で有効な消費税率が存在するか調べる
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param kijyunBi 基準日
	 * @return 存在すればtrue
	 */
	public Boolean existsYuukouZeritsu(BigDecimal zeiritsu, String keigenZeiritsuKbn, Date kijyunBi){

		final String sql = "SELECT COUNT(*) AS count FROM shouhizeiritsu WHERE zeiritsu = ? AND keigen_zeiritsu_kbn = ? AND ? BETWEEN yuukou_kigen_from and yuukou_kigen_to";
		GMap record = connection.find(sql, zeiritsu, keigenZeiritsuKbn, kijyunBi);
		int count = Integer.parseInt(record.get("count").toString());

		return (count > 0);
	}



/* 社員(shain) */
	/**
	 * 社員レコード取得
	 * @param shainNo 社員番号
	 * @return 社員レコード
	 */
	public GMap findShain(String shainNo) {
		final String sql = "SELECT * FROM shain where shain_no = ?";
		return connection.find(sql, shainNo);
	}



/* 社員口座(shain_kouza) */
	/**
	 * 社員口座が存在するか調べる
	 * @param userId ユーザーID
	 * @return 存在すればtrue
	 */
	public Boolean existsShainKouza(String userId){

		final String sql = "SELECT COUNT(*) AS count FROM shain_kouza WHERE shain_no IN (SELECT shain_no FROM user_info WHERE user_id = ?)";
		GMap record = connection.find(sql, userId);
		int count = Integer.parseInt(record.get("count").toString());

		return (count > 0);
	}

/* 日当等マスター(nittou_nado_master) */
	/**
	 * 日当等の種別１を全取得する。
	 * @param isAll 全取得する(true)／しない(false)
	 * @return 種別１
	 */
	public List<String> loadNittouShubetsu1(boolean isAll) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT shubetsu1 FROM nittou_nado_master");
		if(!isAll) sql.append(" WHERE nittou_shukuhakuhi_flg = '1'");
		sql.append(" ORDER BY nittou_shukuhakuhi_flg DESC, shubetsu1");
		List<GMap> list = connection.load(sql.toString());
		List<String> ret = new ArrayList<>();
		for (GMap map : list) {
			ret.add((String)map.get("shubetsu1"));
		}
		return ret;
	}
	/**
	 * 日当等の種別２を全取得する。
	 * @param shubetsu1 種別１
	 * @param isAll 全取得する(true)／しない(false)
	 * @return 種別２
	 */
	public List<Map<String,String>> loadNittouShubetsu2(String shubetsu1, boolean isAll) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT (CASE shubetsu2 WHEN '-' THEN '' ELSE shubetsu2 END) AS shubetsu2, nittou_shukuhakuhi_flg, shouhyou_shorui_hissu_flg FROM nittou_nado_master WHERE shubetsu1 = ?");
		if(!isAll) sql.append(" AND nittou_shukuhakuhi_flg = '1'");
		sql.append(" ORDER BY nittou_shukuhakuhi_flg DESC, shubetsu2");
		List<GMap> list = connection.load(sql.toString(), shubetsu1);
		List<Map<String, String>> ret = new ArrayList<>();
		for (GMap map : list) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("shubetsu2", (String)map.get("shubetsu2"));
			tmpMap.put("nittou_shukuhakuhi_flg", (String)map.get("nittou_shukuhakuhi_flg"));
			tmpMap.put("shouhyou_shorui_hissu_flg", (String)map.get("shouhyou_shorui_hissu_flg"));
			ret.add(tmpMap);
		}
		return ret;
	}
	/**
	 * 種別１・２・役割の金額、証憑書類必須フラグ、日当・宿泊費フラグ、及び税区分を取得
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２ （１に対応する２がない場合、ブランクでもよい）
	 * @param yakushokuCd 役職
	 * @return 日当マスターレコード
	 */
	public GMap loadNittouKingakuAndFlgAndKbn(String shubetsu1, String shubetsu2, String yakushokuCd) {
	if (isEmpty(shubetsu2))
	{
		shubetsu2 = "-";
	}
	final String sql =
				"SELECT "
			+ "  tanka, shouhyou_shorui_hissu_flg, nittou_shukuhakuhi_flg, zei_kubun,"
			+ "  (CASE yakushoku_cd WHEN ? THEN 1 WHEN '-' THEN 2 ELSE 3 END) AS jun " //以下の優先度で金額取得：１．役職が一致　２．役職「-」のもの　３．それ以外のレコードで一番安いもの（これは設定ミスかも）
			+ "FROM nittou_nado_master "
			+ "WHERE "
			+ "  shubetsu1 = ? AND "
			+ "  shubetsu2 = ? "
			+ "ORDER BY jun, tanka, shouhyou_shorui_hissu_flg "
			+ "LIMIT 1 ";
		return connection.find(sql, yakushokuCd, shubetsu1, shubetsu2);
	}

	/**
	 * 種別１・２・役割の税区分を取得
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２ （１に対応する２がない場合、ブランクでもよい）
	 * @param yakushokuCd 役職
	 * @return 税区分
	 */
	public String findNittouZeiKubun(String shubetsu1, String shubetsu2, String yakushokuCd) {
		if (isEmpty(shubetsu2))
		{
			shubetsu2 = "-";
		}
		final String sql =
					"SELECT "
				+ "  zei_kubun,"
				+ "  (CASE yakushoku_cd WHEN ? THEN 1 WHEN '-' THEN 2 ELSE 3 END) AS jun " //以下の優先度で金額取得：１．役職が一致　２．役職「-」のもの　３．それ以外のレコードで一番安いもの（これは設定ミスかも）
				+ "FROM nittou_nado_master "
				+ "WHERE "
				+ "  shubetsu1 = ? AND "
				+ "  shubetsu2 = ? "
				+ "ORDER BY jun, tanka, shouhyou_shorui_hissu_flg "
				+ "LIMIT 1 ";
		Map<String ,Object> record = connection.find(sql, yakushokuCd, shubetsu1, shubetsu2);
		return (null == record) ? "" : (String)record.get("zei_kubun");
	}

	/**
	 * 種別１・２・役割の枝番を取得
	 * ※役職によって科目枝番が違うことはなさそう？
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２ （１に対応する２がない場合、ブランクでもよい）
	 * @param yakushokuCd 役職
	 * @return 税区分
	 */
	public String findNittouEdaban(String shubetsu1, String shubetsu2, String yakushokuCd) {
		if (isEmpty(shubetsu2))
		{
			shubetsu2 = "-";
		}
		final String sql =
					"SELECT "
				+ "  edaban,"
				+ "  (CASE yakushoku_cd WHEN ? THEN 1 WHEN '-' THEN 2 ELSE 3 END) AS jun " //以下の優先度で金額取得：１．役職が一致　２．役職「-」のもの　３．それ以外のレコードで一番安いもの（これは設定ミスかも）
				+ "FROM nittou_nado_master "
				+ "WHERE "
				+ "  shubetsu1 = ? AND "
				+ "  shubetsu2 = ? "
				+ "ORDER BY jun, tanka, shouhyou_shorui_hissu_flg "
				+ "LIMIT 1 ";
		Map<String ,Object> record = connection.find(sql, yakushokuCd, shubetsu1, shubetsu2);
		return (null == record) ? "" : (String)record.get("edaban");
	}

/* 海外日当等マスター(kaigai_nittou_nado_master) */
	/**
	 * 海外日当等の種別１を全取得する。
	 * @param isAll 全取得する(true)／しない(false)
	 * @return 種別１
	 */
	public List<String> loadKaigaiNittouShubetsu1(boolean isAll) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT shubetsu1 FROM kaigai_nittou_nado_master");
		if(!isAll) sql.append(" WHERE nittou_shukuhakuhi_flg = '1'");
		sql.append(" ORDER BY nittou_shukuhakuhi_flg DESC, shubetsu1");
		List<GMap> list = connection.load(sql.toString());
		List<String> ret = new ArrayList<>();
		for (GMap map : list) {
			ret.add((String)map.get("shubetsu1"));
		}
		return ret;
	}
	/**
	 * 海外日当等の種別２を全取得する。
	 * @param shubetsu1 種別１
	 * @param isAll 全取得する(true)／しない(false)
	 * @return 種別２
	 */
	public List<Map<String,String>> loadKaigaiNittouShubetsu2(String shubetsu1, boolean isAll) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT (CASE shubetsu2 WHEN '-' THEN '' ELSE shubetsu2 END) AS shubetsu2, nittou_shukuhakuhi_flg, shouhyou_shorui_hissu_flg FROM kaigai_nittou_nado_master WHERE shubetsu1 = ?");
		if(!isAll) sql.append(" AND nittou_shukuhakuhi_flg = '1'");
		sql.append(" ORDER BY nittou_shukuhakuhi_flg DESC, shubetsu2");
		List<GMap> list = connection.load(sql.toString(), shubetsu1);
		List<Map<String, String>> ret = new ArrayList<>();
		for (GMap map : list) {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put("shubetsu2", (String)map.get("shubetsu2"));
			tmpMap.put("nittou_shukuhakuhi_flg", (String)map.get("nittou_shukuhakuhi_flg"));
			tmpMap.put("shouhyou_shorui_hissu_flg", (String)map.get("shouhyou_shorui_hissu_flg"));
			ret.add(tmpMap);
		}
		return ret;
	}
	/**
	 * 種別１・２・役割の金額、証憑書類必須フラグ、日当・宿泊費フラグを取得
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２ （１に対応する２がない場合、ブランクでもよい）
	 * @param yakushokuCd 役職
	 * @return 日当マスターレコード
	 */
	public GMap loadKaigaiNittouKingaku(String shubetsu1, String shubetsu2, String yakushokuCd) {
	if (isEmpty(shubetsu2))
	{
		shubetsu2 = "-";
	}
	final String sql =
				"SELECT "
			+ "tanka, heishu_cd, tanka_gaika, shouhyou_shorui_hissu_flg, nittou_shukuhakuhi_flg, zei_kubun,"
			+ "  (CASE yakushoku_cd WHEN ? THEN 1 WHEN '-' THEN 2 ELSE 3 END) AS jun " //以下の優先度で金額取得：１．役職が一致　２．役職「-」のもの　３．それ以外のレコードで一番安いもの（これは設定ミスかも）
			+ "FROM kaigai_nittou_nado_master "
			+ "WHERE "
			+ "  shubetsu1 = ? AND "
			+ "  shubetsu2 = ? "
			+ "ORDER BY jun, tanka, shouhyou_shorui_hissu_flg "
			+ "LIMIT 1 ";
		return connection.find(sql, yakushokuCd, shubetsu1, shubetsu2);
	}
	/**
	 * 種別１・２・役割の税区分を取得
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２ （１に対応する２がない場合、ブランクでもよい）
	 * @param yakushokuCd 役職
	 * @return 税区分
	 */
	public String findKaigaiNittouZeiKubun(String shubetsu1, String shubetsu2, String yakushokuCd) {
		if (isEmpty(shubetsu2))
		{
			shubetsu2 = "-";
		}
		final String sql =
					"SELECT "
				+ "  zei_kubun,"
				+ "  (CASE yakushoku_cd WHEN ? THEN 1 WHEN '-' THEN 2 ELSE 3 END) AS jun " //以下の優先度で金額取得：１．役職が一致　２．役職「-」のもの　３．それ以外のレコードで一番安いもの（これは設定ミスかも）
				+ "FROM kaigai_nittou_nado_master "
				+ "WHERE "
				+ "  shubetsu1 = ? AND "
				+ "  shubetsu2 = ? "
				+ "ORDER BY jun, tanka, shouhyou_shorui_hissu_flg "
				+ "LIMIT 1 ";
		Map<String ,Object> record = connection.find(sql, yakushokuCd, shubetsu1, shubetsu2);
		return (null == record) ? "" : (String)record.get("zei_kubun");
	}

	/**
	 * 種別１・２・役割の税区分を取得
	 * ※役職によって科目枝番が違うことはなさそう？
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２ （１に対応する２がない場合、ブランクでもよい）
	 * @param yakushokuCd 役職
	 * @return 税区分
	 */
	public String findKaigaiNittouEdaban(String shubetsu1, String shubetsu2, String yakushokuCd) {
		if (isEmpty(shubetsu2))
		{
			shubetsu2 = "-";
		}
		final String sql =
					"SELECT "
				+ "  edaban,"
				+ "  (CASE yakushoku_cd WHEN ? THEN 1 WHEN '-' THEN 2 ELSE 3 END) AS jun " //以下の優先度で金額取得：１．役職が一致　２．役職「-」のもの　３．それ以外のレコードで一番安いもの（これは設定ミスかも）
				+ "FROM kaigai_nittou_nado_master "
				+ "WHERE "
				+ "  shubetsu1 = ? AND "
				+ "  shubetsu2 = ? "
				+ "ORDER BY jun, tanka, shouhyou_shorui_hissu_flg "
				+ "LIMIT 1 ";
		Map<String ,Object> record = connection.find(sql, yakushokuCd, shubetsu1, shubetsu2);
		return (null == record) ? "" : (String)record.get("edaban");
	}

/* プロジェクトマスター(project_master) */
	/**
	 * プロジェクト名取得
	 * @param projectCd プロジェクトコード
	 * @return プロジェクト名 引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findProjectName(String projectCd) {
		return this.findProjectName(projectCd, false);
	}
	/**
	 * プロジェクト名取得
	 * @param projectCd プロジェクトコード
	 * @param shouldCheckShuuryouKbn 終了区分をチェックするか
	 * @return プロジェクト名 引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findProjectName(String projectCd, boolean shouldCheckShuuryouKbn) {
		if (null == projectCd || projectCd.isEmpty())
		{
			return "";
		}
		final String sql =  "SELECT project_name FROM project_master WHERE project_cd = ?" + (shouldCheckShuuryouKbn ? " AND shuuryou_kbn = 0" : "");
		Map<String ,Object> record = connection.find(sql, projectCd);
		return (null == record) ? "" : (String)record.get("project_name");
	}
	/**
	 * プロジェクトコード、プロジェクト名取得
	 * @return リスト
	 */
	public List<GMap> loadProject(){

		final String sql = "SELECT project_cd, project_name "
				 + "FROM project_master "
				 + "WHERE shuuryou_kbn = 0 "
				 + "ORDER BY project_cd ASC";
		return connection.load(sql);
	}

	/* セグメントマスター(segment_master) */
	/**
	 * セグメント名取得
	 * @param segmentCd セグメントコード
	 * @return セグメント名 引数がnull or blankの場合、該当コードが見つからない場合はblankを返す。
	 */
	public String findSegmentName(String segmentCd) {
		if (null == segmentCd || segmentCd.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT segment_name_ryakushiki FROM segment_master WHERE segment_cd = ?";
		Map<String ,Object> record = connection.find(sql, segmentCd);
		return (null == record) ? "" : (String)record.get("segment_name_ryakushiki");
	}
	/**
	 * セグメントコード、セグメント名取得
	 * @return リスト
	 */
	public List<GMap> loadSegment(){

		final String sql = "SELECT segment_cd, segment_name_ryakushiki "
						 + "FROM segment_master "
						 + "ORDER BY segment_cd ASC";
		return connection.load(sql);
	}

	/**
	 * セグメント科目残高が存在するか調べる。
	 * @param segmentCd セグメントコード
	 * @param kamokuCd 科目コード
	 * @return 存在すればtrue
	 */
	public boolean existsSegmentKamokuZandaka(String segmentCd, String kamokuCd) {
		final String sql = "SELECT count(*) count FROM segment_kamoku_zandaka WHERE segment_cd = ? AND kamoku_gaibu_cd = ? AND kessanki_bangou IN (0, 1)";
		return 0 != (long)connection.find(sql, segmentCd, kamokuCd).get("count");
	}

/* 交通手段マスター(koutsuu_shudan_master) */
	/**
	 * ソート順、交通手段、証憑書類必須フラグを全取得する。
	 * @return 交通手段
	 */
	public List<GMap> loadKoutsuuShudan(){
		final String sql = "SELECT * FROM koutsuu_shudan_master ORDER BY sort_jun";
		return connection.load(sql);
	}

	/**
	 * ソート順、交通手段、証憑書類必須フラグを全取得する。(交通手段指定)
	 * @param koutsuuShudan 交通手段
	 * @return 交通手段
	 */
	public String findKoutsuuShudanEdaban(String koutsuuShudan){
		final String sql = "SELECT sort_jun, koutsuu_shudan, shouhyou_shorui_hissu_flg, zei_kubun, edaban FROM koutsuu_shudan_master WHERE koutsuu_shudan = ? ORDER BY sort_jun";
		Map<String ,Object> record = connection.find(sql, koutsuuShudan);
		return (null == record) ? "" : (String)record.get("edaban");
	}

	/**
	 * 証憑書類必須フラグ取得
	 * @param koutsuuShudan 交通手段
	 * @return 証憑書類必須フラグ
	 */
	public String findShouhyouShoruiHissuFlg(String koutsuuShudan){
		if (null == koutsuuShudan || koutsuuShudan.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT shouhyou_shorui_hissu_flg FROM koutsuu_shudan_master WHERE koutsuu_shudan = ?";
		Map<String ,Object> record = connection.find(sql, koutsuuShudan);
		return (null == record) ? "" : (String)record.get("shouhyou_shorui_hissu_flg");
	}

	/**
	 * 税区分を取得
	 * @param koutsuuShudan 交通手段
	 * @return 税区分
	 *
	 */
	public String findZeiKubun(String koutsuuShudan){
		if (null == koutsuuShudan || koutsuuShudan.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT zei_kubun FROM koutsuu_shudan_master WHERE koutsuu_shudan = ?";
		Map<String ,Object> record = connection.find(sql, koutsuuShudan);
		return (null == record) ? "" : (String)record.get("zei_kubun");
	}

/* 海外交通手段マスター(kaigai_koutsuu_shudan_master) */
	/**
	 * ソート順、交通手段、証憑書類必須フラグを全取得する。
	 * @return 交通手段
	 */
	public List<GMap> loadKaigaiKoutsuuShudan(){
		final String sql = "SELECT sort_jun, koutsuu_shudan, shouhyou_shorui_hissu_flg, zei_kubun, edaban FROM kaigai_koutsuu_shudan_master ORDER BY sort_jun";
		return connection.load(sql);
	}

	/**
	 * ソート順、交通手段、証憑書類必須フラグを全取得する。(交通手段指定)
	 * @param koutsuuShudan 交通手段
	 * @return 交通手段
	 */
	public String findKaigaiKoutsuuShudanEdaban(String koutsuuShudan){
		final String sql = "SELECT sort_jun, koutsuu_shudan, shouhyou_shorui_hissu_flg, zei_kubun, edaban FROM kaigai_koutsuu_shudan_master WHERE koutsuu_shudan = ? ORDER BY sort_jun";
		Map<String ,Object> record = connection.find(sql, koutsuuShudan);
		return (null == record) ? "" : (String)record.get("edaban");
	}

	/**
	 * 税区分を取得
	 * @param koutsuuShudan 交通手段
	 * @return 税区分
	 *
	 */
	public String findKaigaiZeiKubun(String koutsuuShudan){
		if (null == koutsuuShudan || koutsuuShudan.isEmpty())
		{
			return "";
		}
		final String sql = "SELECT zei_kubun FROM kaigai_koutsuu_shudan_master WHERE koutsuu_shudan = ?";
		Map<String ,Object> record = connection.find(sql, koutsuuShudan);
		return (null == record) ? "" : (String)record.get("zei_kubun");
	}

/* 振込日ルール(furikomi_bi_rule_hi) */
	/**
	 * 振込日ルール
	 * @param kijunDate 基準日(1～31)
	 * @return 交通手段
	 */
	public Integer findFurikomiBiRuleHi(int kijunDate){
		final String sql = "SELECT furikomi_date FROM furikomi_bi_rule_hi WHERE kijun_date = ?";
		GMap record = connection.find(sql, kijunDate);
		return (record == null) ? null : (Integer)record.get("furikomi_date");
	}

/* 振込日ルール(furikomi_bi_rule_youbi) */
	/**
	 * 振込日ルール
	 * @param kijunWeekday 基準曜日(1～7)
	 * @return 交通手段
	 */
	public Integer findFurikomiBiRuleYoubi(int kijunWeekday){
		final String sql = "SELECT furikomi_weekday FROM furikomi_bi_rule_youbi WHERE kijun_weekday = ?";
		GMap record = connection.find(sql, kijunWeekday);
		return (record == null) ? null : (Integer)record.get("furikomi_weekday");
	}

/* 幣種マスター(heishu_master) */
	/**
	 * 幣種マスターで使用可否が1のみ取得
	 * @return 幣種マスター
	 */
	public List<GMap> loadheishuCd(){
		final String sql = "SELECT hm.heishu_cd, hm.currency_unit, country_name, (CASE ra.availability_flg WHEN 1 THEN trunc((ra.rate / hm.conversion_unit), 8) ELSE NULL END) as rate FROM heishu_master hm LEFT OUTER JOIN rate_master ra ON hm.heishu_cd = ra.heishu_cd WHERE hm.availability_flg = '1' AND (start_date IS NULL OR ra.start_date <= current_date) ORDER BY hm.display_order";
		return connection.load(sql);
	}

	/**
	 * 幣種コードから幣種マスターを取得
	 * @param heishuCd 幣種コード
	 * @return 幣種マスター
	 */
	public GMap findHeishuCd(String heishuCd){
		final String sql = "SELECT hm.heishu_cd, hm.currency_unit, country_name, (CASE ra.availability_flg WHEN 1 THEN trunc((ra.rate / hm.conversion_unit), 8) ELSE NULL END) as rate FROM heishu_master hm LEFT OUTER JOIN rate_master ra ON hm.heishu_cd = ra.heishu_cd WHERE hm.heishu_cd = ? AND hm.availability_flg = '1' AND (start_date IS NULL OR start_date <= current_date)";
		return connection.find(sql, heishuCd);
	}

	/**
	 * 幣種コードから幣種マスターの使用可否を調べ、1のときtrue
	 * @param heishuCd 幣種コード
	 * @return 幣種マスター使用可否
	 */
	public boolean isUsingHeishuCd(String heishuCd){
		final String sql = "SELECT heishu_cd FROM heishu_master WHERE heishu_cd = ? AND availability_flg = '1'";
		GMap record = connection.find(sql, heishuCd);
		return (record == null) ? false : true;
	}

	/**
	 * 予算執行処理年月を取得する
	 * @return 結果マップ
	 */
	public GMap findYosanShikkouShoriNengetsu(){
		final String sql = "SELECT * FROM yosan_shikkou_shori_nengetsu";
		return connection.find(sql);
	}

	/**
	 * 残高タイプで指定された残高を取得する。
	 * @param zandakaType 残高タイプ
	 * @param kamokuCd 科目外部コード
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @return 結果リスト
	 */
	public List<GMap> loadZandaka(String zandakaType, String kamokuCd, String futanBumonCd, String kamokuEdabanCd){
		List<String> params = new ArrayList<>();
		String sql = "SELECT * FROM :ZANDAKA_TABLE WHERE 1 = 1 :AND";

		//SQLの可変部分を置換
		switch(zandakaType) {
		case suitouchouZandakaType.kamoku:
			sql = sql.replace(":ZANDAKA_TABLE", "kamoku_zandaka").replace(":AND", "AND kamoku_gaibu_cd = ?");
			params.add(kamokuCd);
			break;
		case suitouchouZandakaType.bumonKamoku:
			sql = sql.replace(":ZANDAKA_TABLE", "bumon_kamoku_zandaka").replace(":AND", "AND futan_bumon_cd = ? AND kamoku_gaibu_cd = ?");
			params.add(futanBumonCd);
			params.add(kamokuCd);
			break;
		case suitouchouZandakaType.kamokuEdaban:
			sql = sql.replace(":ZANDAKA_TABLE", "edaban_zandaka").replace(":AND", "AND kamoku_gaibu_cd = ? AND kamoku_edaban_cd = ?");
			params.add(kamokuCd);
			params.add(kamokuEdabanCd);
			break;
		default:
			sql = sql.replace(":ZANDAKA_TABLE", "bumon_kamoku_edaban_zandaka").replace(":AND", "AND futan_bumon_cd = ? AND kamoku_gaibu_cd = ? AND edaban_code = ?");
			params.add(futanBumonCd);
			params.add(kamokuCd);
			params.add(kamokuEdabanCd);
		}

		return connection.load(sql, params.toArray());
	}

	/**
	 * 社員仕訳財務枝番コード取得
	 * @param user_id 対象ユーザーID ※社員番号ではない
	 * @return 該当ユーザーの財務枝番コード
	 */
	public String getShainShiwakeEdano(String user_id){
		final String sql = "SELECT zaimu_edaban_cd FROM shain_kouza WHERE shain_no IN (SELECT shain_no FROM user_info WHERE user_id = ?)";
		GMap record = connection.find(sql, user_id);
		return (record == null) ? "" : (String)record.get("zaimu_edaban_cd");
	}

	/**
	 * 外貨使用かどうかの判定結果を返す
	 * @return 外貨使用ならtrue、外貨使用しない設定ならfalse
	 */
	private boolean isGaikaShiyou() {
		GaikaSettingDao gaikaSettingDao = EteamContainer.getComponent(GaikaSettingDao.class, connection);
		List<GaikaSetting> list = gaikaSettingDao.load();

		// マスター未取込みなら外貨使用しない
		if (list.size() == 0)
		{
			return false;
		}

		// 外貨使用フラグの値によって使用可否が決まる
		return list.get(0).getGaikaShiyouFlg() == 1;
	}
	
}
