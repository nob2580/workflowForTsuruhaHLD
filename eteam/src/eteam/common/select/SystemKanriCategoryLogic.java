package eteam.common.select;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamSQLTableNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;

/**
 * システムカテゴリー内のSelect文を集約したLogic
 */
public class SystemKanriCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
	
/* 機能制御(kinou_seigyo) */
	
	/**
	 * 機能制御コードをキーに機能制御から１機能のデータを取得します。
	 * @param kinou_seigyo_cd  機能制御コード
	 * @return 検索結果
	 */
    @Deprecated
	public GMap findKinouSeigyo(String kinou_seigyo_cd) {
		final String sql = "SELECT kinou_seigyo_cd, kinou_seigyo_kbn FROM kinou_seigyo WHERE kinou_seigyo_cd = ? ;";
		return connection.find(sql, kinou_seigyo_cd);
	}
	
	/**
	 * 機能制御コードをキーに機能制御から１機能のデータを取得します。
	 * @param kinouSeigyoCd  機能制御コード
	 * @return 機能がONである
	 */
	public boolean judgeKinouSeigyoON(String kinouSeigyoCd) {
		GMap kinouSeigyo = findKinouSeigyo(kinouSeigyoCd);
		return KINOU_SEIGYO_KBN.YUUKOU.equals(kinouSeigyo.get("kinou_seigyo_kbn"));
	}
	
	/**
	 * 機能制御から全データを取得します。
	 * @return 検索結果
	 */
    @Deprecated
	public List<GMap> loadKinouSeigyo() {
		final String sql = "SELECT kinou_seigyo_cd, kinou_seigyo_kbn FROM kinou_seigyo ;";
		return connection.load(sql);
	}
	
/* メール設定(mail_settei) */
	/**
	 * メール設定よりデータを取得します。ただし、パスワードの取得はしません。
	 * @return 検索結果 リスト
	 */
    @Deprecated
	public GMap findMailSettei() {
		final String sql = "SELECT smtp_server_name, port_no, ninshou_houhou, angouka_houhou, mail_address, mail_id FROM mail_settei;";
		return connection.find(sql);
	}
	
	/**
	 * メール設定よりデータを取得します。
	 * @return 検索結果 リスト
	 */
    @Deprecated
	public GMap findMailSetteiAll() {
		final String sql = "SELECT smtp_server_name, port_no, ninshou_houhou, angouka_houhou, mail_address, mail_id, mail_password FROM mail_settei;";
		return connection.find(sql);
	}
	
/* 設定情報(setting_info) */
	/**
	 * setting_info(設定情報)テーブルの全レコード取得。
	 * @return 設定一覧
	 */
    @Deprecated
	public List<GMap> loadSettingVal(){
		final String sql = "SELECT * FROM setting_info ORDER BY hyouji_jun";
		return connection.load(sql);
	}
	
/* 設定情報(setting_info) */
	/**
	 * setting_info(設定情報)テーブルの変更可能レコード取得。
	 * @param isSystemKanri システム管理者ならば変更可能フラグ in (1,2)、それ以外なら変更可能フラグ in (1)を取得。
	 * @return 設定一覧
	 */
    @Deprecated
	public List<GMap> loadSettingVal2Edit(boolean isSystemKanri){
		final String sql = "SELECT * FROM setting_info WHERE editable_flg BETWEEN ? AND ? ORDER BY hyouji_jun";
		return isSystemKanri ? 
			connection.load(sql, "1", "2") : 
			connection.load(sql, "1", "1");
	}
	
/* 設定情報(setting_info) */
	/**
	 * setting_name(設定名)とスキーマをキーにsetting_info(設定情報)テーブルからsetting_val(設定値)を取得。
	 * @param setting_name 設定名
	 * @param schema スキーマ
	 * @return 設定値
	 */
    @Deprecated
	public int findSettingVal(String setting_name ,String schema){
		final String sql = "SELECT setting_val FROM  " + schema + ".setting_info WHERE setting_name = '"+ setting_name +"' ;";
		GMap record = connection.find(sql);
		return (record == null) ? 0 : Integer.valueOf(record.get("setting_val").toString());
	}
	
/* 設定情報(setting_info) */
	/**
	 * setting_name(設定名)をキーにsetting_info(設定情報)テーブルからsetting_val(設定値)を取得。
	 * @param setting_name 設定名
	 * @return 設定値
	 */
    @Deprecated
	public String findSettingValOnlyInfo(String setting_name){
		final String sql = "SELECT setting_val FROM setting_info WHERE setting_name = '"+ setting_name +"' ;";
		GMap record = connection.find(sql);
		return (record == null) ? "" : (String)record.get("setting_val");
	}
	
/* 会社情報(kaisha_info) */
	/**
	 * kaisha_info(会社情報)テーブルの全レコード取得。
	 * @return レコード
	 */
    @Deprecated
	public GMap findKaishaInfo(){
		final String sql = "SELECT * FROM kaisha_info";
		return connection.find(sql);
	}
	
/* 内部コード設定(naibu_cd_setting) */
	/**
	 * 内部コード設定取得
	 * naibu_cd_name(内部コード名称)をキーに、naibu_cd_setting(内部コード設定)よりデータを取得します。
	 * @param naibuCdName  内部コード名称 
	 * @return 検索結果
	 */
    @Deprecated
	public List<GMap> loadNaibuCdSetting(String naibuCdName) {
		final String sql = "SELECT naibu_cd, name, option1, option2, option3 FROM naibu_cd_setting WHERE naibu_cd_name = ? ORDER BY hyouji_jun;";
		return connection.load(sql, naibuCdName);
	}

	/**
	 * 内部名称と内部コードをキーに内部コード設定からデータを取得します。
	 * @param naibuCdName  内部コード名称
	 * @param naibuCd      内部コード
	 * @return 検索結果
	 */
    @Deprecated
	public GMap findNaibuCdSetting(String naibuCdName, String naibuCd) {
		final String sql = "SELECT naibu_cd, name, hyouji_jun, option1, option2, option3 FROM naibu_cd_setting WHERE naibu_cd_name = ? AND naibu_cd = ? ;";
		return connection.find(sql, naibuCdName, naibuCd);
	}

	/**
	 * 内部名称と内部コードをキーに内部コード設定からデータを取得します。
	 * @param naibuCdName  内部コード名称
	 * @param naibuCd      内部コード
	 * @return 検索結果(名称) 検索結果なければブランク
	 */
	public String findNaibuCdName(String naibuCdName, String naibuCd) {
		GMap record = findNaibuCdSetting(naibuCdName, naibuCd);
		return (record != null) ? (String)record.get("name") : "";
	}

	/**
	 * 課税区分によって課税・非課税かどうかを返す
	 * @param kazeiKbn 課税区分
	 * @param kamokuCd 科目コード
	 * @return 1:課税、0:非課税
	 */
	public String judgeKazeiFlg(String kazeiKbn, String kamokuCd){
		MasterKanriCategoryLogic masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		// 未設定のときは科目マスターの課税区分を参考にする
		if (kazeiKbn.equals(EteamNaibuCodeSetting.KAZEI_KBN.MISETTEI)) {
			GMap kamokuMap = masterLg.findKamokuMaster(kamokuCd);
			GMap kazeiKbnMap = findNaibuCdSetting("kamoku_kazei_kbn", kamokuMap.getString("kazei_kbn"));
			return (kazeiKbnMap != null) ? (String)kazeiKbnMap.get("option1") : "";
		} else {
			GMap kazeiKbnMap = findNaibuCdSetting("kazei_kbn", kazeiKbn);
			return (kazeiKbnMap != null) ? (String)kazeiKbnMap.get("option2") : "";
		}
	}
	
/* 画面権限制御(gamen_kengen_seigyo) */
	/**
	 * 画面IDに対する各利用可否フラグを取得します。
	 * @param gamenId 画面ID
	 * @return 検索結果
	 */ 
    @Deprecated
	public GMap findGamenKengenSeigyoInfoFlg(String gamenId) {
		final String sql = "SELECT bumon_shozoku_riyoukanou_flg, system_kanri_riyoukanou_flg, "
						+  "workflow_riyoukanou_flg, kaishasettei_riyoukanou_flg, keirishori_riyoukanou_flg, kinou_seigyo_cd "
						+  "FROM gamen_kengen_seigyo "
						+  "WHERE gamen_id = ?";
		return connection.find(sql, gamenId);
	}

	/**
	 * 画面IDに対する画面名を取得します。
	 * @param gamenId 画面ID
	 * @return 検索結果
	 */ 
    @Deprecated
	public GMap findGamenName(String gamenId) {
		final String sql = "SELECT gamen_name "
						+  "FROM gamen_kengen_seigyo "
						+  "WHERE gamen_id = ?";
		return connection.find(sql, gamenId);
	}

/*バッチログ(batch_log) */
	/**
	 * バッチ名のリスト（重複除く）を取得する。
	 * @return バッチ名のリスト
	 */
    @Deprecated
	public List<GMap> loadBatchName() {
		// バッチ区分はeteamプロジェクトの場合「1」固定
		final String sql = "SELECT DISTINCT batch_name from batch_log  WHERE batch_kbn = '1' ORDER BY batch_name";
		return connection.load(sql);
	}

	/**
	 * バッチ情報テーブルのデータ件数を取得する。データがなければサイズ0。
	 * @param startTime  開始日付
	 * @param endTime    終了日付
	 * @param batchName  バッチ名
	 * @param status    ステータス
	 * @return 検索結果件数
	 */
    @Deprecated
	public long findBatchInfoCount(Timestamp startTime, Timestamp endTime, String batchName, String status){
		final StringBuffer sql = new StringBuffer("SELECT COUNT(batch_name) AS count FROM batch_log ");

		List<Object> params = new ArrayList<Object>();
		// 条件.開始日付 終了日付
		sql.append("WHERE start_time BETWEEN ? AND ? ");
		params.add(startTime);
		params.add(endTime);
		// バッチ区分（eteamプロジェクトは「1」）
		sql.append("AND batch_kbn = '1' ");
		// 条件.バッチ名
		if (StringUtils.isNotEmpty(batchName)) {
			sql.append("AND batch_name = ? ");
			params.add(batchName);
		}
		// 条件.ステータス
		if (StringUtils.isNotEmpty(status)) {
			sql.append("AND batch_status = ? ");
			params.add(status);
		}
		GMap datacount = connection.find(sql.toString(), params.toArray());
		long count = (long)datacount.get("count");
		return count;
	}

	/**
	 * バッチ連携結果情報一覧を取得する
	 * @param startTime  開始日付
	 * @param endTime    終了日付
	 * @param batchName  バッチ名
	 * @param status    ステータス
	 * @param sortItem  ソート項目
	 * @param sortOrder ソート順
	 * @param pageNo    ページ番号
	 * @param pageMax   １ページ最大表示件数
	 * @return バッチ連携結果一覧リスト
	 */
	public List<GMap> loadBatchRenkeiKekkaList(Timestamp startTime, Timestamp endTime, String batchName, String status, String sortItem, String sortOrder, int pageNo, int pageMax) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.shiwake_serial_no,  a.batch_name, a.start_time, a.batch_status, b.name as batch_status_name, count_name, count ");
		sql.append("FROM (SELECT batch.*, himoduke.serial_no as shiwake_serial_no FROM batch_log batch LEFT OUTER JOIN (SELECT serial_no FROM batch_log_invalid_log_himoduke GROUP BY serial_no) himoduke ON batch.serial_no = himoduke.serial_no )a ");
		sql.append("LEFT OUTER JOIN naibu_cd_setting b ON (b.naibu_cd_name, b.naibu_cd) = ('batch_status', a.batch_status) ");
		
		List<Object> params = new ArrayList<Object>();
		// 条件.開始日付 終了日付
		sql.append("WHERE start_time BETWEEN ? AND ? ");
		params.add(startTime);
		params.add(endTime);
		// バッチ区分（eteamプロジェクトは「1」）
		sql.append("AND batch_kbn = '1' ");
		// 条件.バッチ名
		if (StringUtils.isNotEmpty(batchName)) {
			sql.append("AND batch_name = ? ");
			params.add(batchName);
		}
		// 条件.ステータス
		if (StringUtils.isNotEmpty(status)) {
			sql.append("AND batch_status = ? ");
			params.add(status);
		}
		
		//ソート
		sql.append("ORDER BY ");
		sql.append(sortItem); 
		sql.append(" "); 
		sql.append(sortOrder); 
		
		//ページ番号（取得件数）*/
		sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));

		return connection.load(sql.toString(), params.toArray());
	}
	
/* バッチ不良ログファイル紐づけ(batch_log_invalid_log_himoduke) */
	/**
	 * シリアル番号から紐付けられたレコードを取得。
	 * @param serialNo シリアル番号
	 * @return バッチ不良ログファイル紐づけList
	 */
    @Deprecated
	public List<GMap> loadBatchErrorLogHimoduke(int serialNo){
		final String sql = "SELECT * FROM batch_log_invalid_log_himoduke WHERE serial_no = ? ORDER BY edaban ";
		return connection.load(sql, serialNo);
	}
	
/* 不良伝票ログ(batch_log_invalid_denpyou_log) */
	/**
	 * ファイル名からログを取得。
	 * @param fileName ファイル名
	 * @return 不良伝票ログList
	 */
    @Deprecated
	public List<GMap> loadInvalidDenpyouLog(String fileName){
		final String sql = "SELECT * FROM batch_log_invalid_denpyou_log WHERE file_name = ? ";
		return connection.load(sql, fileName);
	}
	
/* 不良データログ(batch_log_invalid_file_log) */
	/**
	 * ファイル名からログを取得。
	 * @param fileName ファイル名
	 * @return 不良データログList
	 */
    @Deprecated
	public List<GMap> loadInvalidFileLog(String fileName){
		final String sql = "SELECT * FROM batch_log_invalid_file_log WHERE file_name = ? ";
		return connection.load(sql, fileName);
	}
	
/* 画面項目制御(gamen_koumoku_seigyo) */
	/**
	 * gamen_koumoku_seigyo(画面項目制御)テーブルの指定テーブルのレコード取得。
	 * @param denpyouKbn 伝票区分
	 * @return レコード
	 */
    @Deprecated
	public List<GMap> loadGamenKoumokuInfo(String denpyouKbn){
		final String sql = "SELECT * FROM gamen_koumoku_seigyo WHERE denpyou_kbn = ? ORDER BY denpyou_kbn, hyouji_jun";
		return connection.load(sql, denpyouKbn);
	}
	
	/**
	 * gamen_koumoku_seigyo(画面項目制御)テーブルと紐づく伝票種別の取得。
	 * @return レコード
	 */
    @Deprecated
	public List<GMap> loadGamenKoumokuWithDenpyouSyubetsu(){
		final String sql = "SELECT "
						+ "  g.*, "
						+ "  (select max(hyouji_jun) from gamen_koumoku_seigyo where denpyou_kbn = g.denpyou_kbn) as hyouji_jun_max "
						+ "FROM gamen_koumoku_seigyo g "
						+ "ORDER BY g.denpyou_kbn, g.hyouji_jun ";
		return connection.load(sql);
	}
	
	/**
	 * gamen_koumoku_seigyo(画面項目制御)テーブルに紐づく伝票区分と伝票種別名を取得。
	 * @return レコード
	 */
	public List<GMap> loadGamenKoumokuWithDenpyouIchiran(){
		final String sql = "SELECT "
						+ "  d.denpyou_kbn "
						+ "  , d.denpyou_shubetsu "
						+ "FROM "
						+ "  denpyou_shubetsu_ichiran d "
						+ "  INNER JOIN ( "
						+ "    SELECT "
						+ "      denpyou_kbn "
						+ "    FROM "
						+ "      gamen_koumoku_seigyo "
						+ "    GROUP BY "
						+ "      denpyou_kbn "
						+ "  ) g "
						+ "    ON d.denpyou_kbn = g.denpyou_kbn "
						+ "ORDER BY "
						+ "  d.hyouji_jun "
						+ "  , d.denpyou_kbn ";

		return connection.load(sql);
	}
	
/*ヴァージョン(version) */
	/**
	 * DBのヴァージョンを取得する。yyMMdd形式で入っている想定。
	 * OEMの150521以前のヴァージョンで動いている古いスキーマだと存在しない。
	 * @return ヴァージョン
	 */
    @Deprecated
	public String findVersion() {
		final String sql = "SELECT version FROM version";
		try {
			GMap record = connection.find(sql);
			return (record == null) ? null : (String)record.get("version");
		} catch (EteamSQLTableNotFoundException e) {
			return null;
		}
	}
	/**
	 * DBのヴァージョンを取得する。yy.MM.xx.XX形式で返す
	 * yymmddxx形式になってからの使用
	 * @return ヴァージョン
	 */
	public String findVersionYYMMDDXX() {
		String version = findVersion();
		return version.substring(0, 2) + "." + version.substring(2, 4) + "." + version.substring(4, 6) + "." + version.substring(6, 8);
	}
}
