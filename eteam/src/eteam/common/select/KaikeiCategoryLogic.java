package eteam.common.select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst.BUMON_CD;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;

/**
 * 会計カテゴリー内のSelect文を集約したLogic
 */
public class KaikeiCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* 請求書払い(seikyuushobarai) */
	/**
	 * 請求書払いテーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findSeikyuushobarai(String denpyouId){
		final String sql = "SELECT * FROM seikyuushobarai WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}

/* 請求書払い明細(seikyuushobarai_meisai) */
	/**
	 * 請求書払い明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadSeikyuushobaraiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM seikyuushobarai_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

/* 支払依頼申請 */
	/**
	 * 支払依頼テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findShiharaiIrai(String denpyouId){
		final String sql = "SELECT * FROM shiharai_irai WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}
	/**
	 * 支払依頼明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadShiharaiIraiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM shiharai_irai_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

/* 旅費仮払(ryohi_karibarai) */
	/**
	 * 旅費仮払テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findRyohiKaribarai(String denpyouId){
		final String sql = "SELECT * FROM ryohi_karibarai WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}

/* 旅費仮払明細(ryohi_karibarai_meisai) */
	/**
	 * 旅費仮払明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadRyohiKaribaraiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM ryohi_karibarai_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

/* 旅費仮払経費明細(ryohi_karibarai_keihi_meisai) */
	/**
	 * 旅費仮払経費明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadRyohiKeihiKaribaraiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

/* 旅費精算(ryohiseisan) */
	/**
	 * 仮払データと結合して旅費精算テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	public GMap findRyohiSeisan(String denpyouId){
		final String sql =
				"SELECT "
			+ "  ry.*"
			+ "  ,ka.kingaku "
			+ "  ,ka.karibarai_kingaku "
			+ "  ,ka.tekiyou as karibarai_tekiyou "
			+ "FROM ryohiseisan ry "
			+ "LEFT OUTER JOIN ryohi_karibarai ka on ry.karibarai_denpyou_id = ka.denpyou_id "
			+ "WHERE ry.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 指定した仮払申請に紐づく旅費精算テーブルを検索する。データがなければnull
	 * @param karibaraiDenpyouId 仮払伝票ID
	 * @return 検索結果
	 */
	public GMap findRyohiSeisanWithKaribarai(String karibaraiDenpyouId){
		final String sql = "SELECT seisan.denpyou_id FROM ryohiseisan seisan INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id WHERE d.denpyou_joutai NOT IN ('40', '90') AND karibarai_denpyou_id = ? ";
		return connection.find(sql, karibaraiDenpyouId);
	}


/* 旅費精算明細(ryohiseisan_meisai) */
	/**
	 * 旅費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadRyohiSeisanMeisai(String denpyouId) {
		final String sql = "SELECT * FROM ryohiseisan_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 旅費精算明細テーブルを検索する。(参照起票用にICカード・法人カード紐付を除外)
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadRyohiSeisanMeisaiNOIC(String denpyouId) {
		final String sql = "SELECT * FROM ryohiseisan_meisai WHERE denpyou_id =? AND ic_card_no = '' AND himoduke_card_meisai IS NULL  ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 旅費経費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadRyohiKeihiseisanMeisai(String denpyouId) {
		final String sql = "SELECT * FROM ryohiseisan_keihi_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 旅費経費精算明細テーブルを検索する。(参照起票用に法人カード紐付を除外)
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadRyohiKeihiseisanMeisaiNoHoujinHimoduke(String denpyouId) {
		final String sql = "SELECT * FROM ryohiseisan_keihi_meisai WHERE denpyou_id =? AND himoduke_card_meisai IS NULL ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * ユーザーに紐づく旅費精算明細データを検索する。
	 * @param kikanFrom      期間開始日
	 * @param kikanTo        期間終了日
	 * @param naiyou         内容
	 * @param seigyoUserId   制御ユーザーID
	 * @param shubetsuCd     種別コード
	 * @return               検索結果
	 */
	public List<GMap> loadRyohiSeisanMeisaiForUser(Date kikanFrom, Date kikanTo, String naiyou, String seigyoUserId, String shubetsuCd){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT DISTINCT * FROM (");
		// 旅費精算のメインSQL
		String sqlRm = " SELECT "
					+ "  rm.kikan_from AS kikan_from, "
					+ "  rm.kikan_to AS kikan_to, "
					+ "  rm.shubetsu_cd AS shubetsu_cd, "
					+ "  rm.shubetsu1 AS shubetsu1, "
					+ "  rm.shubetsu2 AS shubetsu2, "
					+ "  rm.koutsuu_shudan AS koutsuu_shudan, "
					+ "  rm.ryoushuusho_seikyuusho_tou_flg AS ryoushuusho_seikyuusho_tou_flg, "
					+ "  rm.naiyou AS naiyou, "
					+ "  rm.bikou AS bikou, "
					+ "  rm.oufuku_flg AS oufuku_flg, "
					+ "  rm.jidounyuuryoku_flg AS jidounyuuryoku_flg, "
					+ "  rm.nissuu AS nissuu, "
					+ "  rm.tanka AS tanka, "
					+ "  rm.suuryou AS suuryou, "
					+ "  rm.meisai_kingaku AS meisai_kingaku, "
					+ "  rm.shouhyou_shorui_hissu_flg AS shouhyou_shorui_hissu_flg "
					+ " FROM ryohiseisan_meisai rm"
					+ " WHERE "
					+ "  rm.denpyou_id in (SELECT denpyou_id FROM shounin_route WHERE edano = '1' and user_id = ?) AND rm.shubetsu_cd = ? "
					+ "  AND rm.ic_card_no = '' "
					+ "  AND rm.himoduke_card_meisai IS NULL ";

		sql.append(sqlRm);
		params.add(seigyoUserId);
		params.add(shubetsuCd);

		// 入力条件
		// 期間開始日と終了日
		sql.append(" AND (rm.kikan_from BETWEEN ? AND ? OR rm.kikan_to BETWEEN ? AND ? )");
		params.add(kikanFrom);
		params.add(kikanTo);
		params.add(kikanFrom);
		params.add(kikanTo);
		// 内容
		if(naiyou != null && !naiyou.isEmpty())
		{
			sql.append(" AND unify_kana_width(rm.naiyou) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(naiyou).append("%").toString());
		}

		sql.append(" UNION ALL ");
		// 旅費仮払精算のメインSQL
		String sqlRkm = " SELECT "
					+ "  rkm.kikan_from AS kikan_from, "
					+ "  rkm.kikan_to AS kikan_to, "
					+ "  rkm.shubetsu_cd AS shubetsu_cd, "
					+ "  rkm.shubetsu1 AS shubetsu1, "
					+ "  rkm.shubetsu2 AS shubetsu2, "
					+ "  rkm.koutsuu_shudan AS koutsuu_shudan, "
					+ "  '0' AS ryoushuusho_seikyuusho_tou_flg, "
					+ "  rkm.naiyou AS naiyou, "
					+ "  rkm.bikou AS bikou, "
					+ "  rkm.oufuku_flg AS oufuku_flg, "
					+ "  rkm.jidounyuuryoku_flg AS jidounyuuryoku_flg, "
					+ "  rkm.nissuu AS nissuu, "
					+ "  rkm.tanka AS tanka, "
					+ "  rkm.suuryou AS suuryou, "
					+ "  rkm.meisai_kingaku AS meisai_kingaku, "
					+ "  rkm.shouhyou_shorui_hissu_flg AS shouhyou_shorui_hissu_flg "
					+ " FROM ryohi_karibarai_meisai rkm"
					+ " WHERE rkm.denpyou_id in (SELECT denpyou_id FROM shounin_route WHERE edano = '1' and user_id = ?) AND rkm.shubetsu_cd = ?";

		sql.append(sqlRkm);
		params.add(seigyoUserId);
		params.add(shubetsuCd);

		// 入力条件
		// 期間開始日と終了日
		sql.append(" AND (rkm.kikan_from BETWEEN ? AND ? OR rkm.kikan_to BETWEEN ? AND ? )");
		params.add(kikanFrom);
		params.add(kikanTo);
		params.add(kikanFrom);
		params.add(kikanTo);
		// 内容
		if(naiyou != null && !naiyou.isEmpty())
		{
			sql.append(" AND unify_kana_width(rkm.naiyou) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(naiyou).append("%").toString());
		}

		// ソート条件
		sql.append(") ryohi");
		sql.append(" ORDER BY ryohi.kikan_from DESC");

		return connection.load(sql.toString(), params.toArray());
	}

/* 海外旅費仮払(ryohi_karibarai) */
	/**
	 * 海外旅費仮払テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findKaigaiRyohiKaribarai(String denpyouId){
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}

/* 海外旅費仮払明細(ryohi_karibarai_meisai) */
	/**
	 * 海外旅費仮払明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKaigaiRyohiKaribaraiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

/* 海外旅費仮払経費明細(ryohi_karibarai_keihi_meisai) */
	/**
	 * 海外旅費仮払経費明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKaigaiRyohiKeihiKaribaraiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/* 海外旅費精算(kaigai_ryohiseisan) */
	/**
	 * 仮払データと結合して海外旅費精算テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	public GMap findKaigaiRyohiSeisan(String denpyouId){
		final String sql =
				"SELECT "
			+ "  ry.* "
			+ "  ,ka.kingaku "
			+ "  ,ka.karibarai_kingaku "
			+ "  ,ka.tekiyou as karibarai_tekiyou "
			+ "FROM kaigai_ryohiseisan ry "
			+ "LEFT OUTER JOIN kaigai_ryohi_karibarai ka on ry.karibarai_denpyou_id = ka.denpyou_id "
			+ "WHERE ry.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 指定した仮払申請に紐づく海外旅費精算テーブルを検索する。データがなければnull
	 * @param karibaraiDenpyouId 仮払伝票ID
	 * @return 検索結果
	 */
	public GMap findKaigaiRyohiSeisanWithKaribarai(String karibaraiDenpyouId){
		final String sql = "SELECT seisan.denpyou_id FROM kaigai_ryohiseisan seisan INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id WHERE d.denpyou_joutai NOT IN ('40', '90') AND karibarai_denpyou_id = ?";
		return connection.find(sql, karibaraiDenpyouId);
	}


/* 海外旅費精算明細(kaigai_ryohiseisan_meisai) */
	/**
	 * 海外旅費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKaigaiRyohiSeisanMeisai(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohiseisan_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 旅費精算明細テーブルを検索する。(参照起票用にICカード・法人カード紐付を除外)
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKaigaiRyohiSeisanMeisaiNOIC(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohiseisan_meisai WHERE denpyou_id =? AND ic_card_no = '' AND himoduke_card_meisai IS NULL ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 海外旅費経費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKaigaiRyohiKeihiseisanMeisai(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohiseisan_keihi_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 海外旅費経費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKaigaiRyohiKeihiseisanMeisaiNoHoujinHimoduke(String denpyouId) {
		final String sql = "SELECT * FROM kaigai_ryohiseisan_keihi_meisai WHERE denpyou_id =?  AND himoduke_card_meisai IS NULL ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * ユーザーに紐づく海外旅費精算明細データを検索する。
	 * @param kikanFrom      期間開始日
	 * @param kikanTo        期間終了日
	 * @param naiyou         内容
	 * @param seigyoUserId   制御ユーザーID
	 * @param shubetsuCd     種別コード
	 * @param kaigaiFlg      海外フラグ
	 * @return               検索結果
	 */
	public List<GMap> loadKaigaiRyohiSeisanMeisaiForUser(Date kikanFrom, Date kikanTo, String naiyou, String seigyoUserId, String shubetsuCd, String kaigaiFlg){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT DISTINCT * FROM (");
		// 海外旅費精算のメインSQL
		String sqlRm = " SELECT "
					+ "  rm.kikan_from AS kikan_from, "
					+ "  rm.kikan_to AS kikan_to, "
					+ "  rm.shubetsu_cd AS shubetsu_cd, "
					+ "  rm.shubetsu1 AS shubetsu1, "
					+ "  rm.shubetsu2 AS shubetsu2, "
					+ "  rm.koutsuu_shudan AS koutsuu_shudan, "
					+ "  rm.ryoushuusho_seikyuusho_tou_flg AS ryoushuusho_seikyuusho_tou_flg, "
					+ "  rm.naiyou AS naiyou, "
					+ "  rm.bikou AS bikou, "
					+ "  rm.oufuku_flg AS oufuku_flg, "
					+ "  rm.jidounyuuryoku_flg AS jidounyuuryoku_flg, "
					+ "  rm.nissuu AS nissuu, "
					+ "  rm.heishu_cd AS heishu_cd, "
					+ "  rm.rate AS rate, "
					+ "  rm.gaika AS gaika, "
					+ "  rm.currency_unit AS currency_unit, "
					+ "  rm.tanka AS tanka, "
					+ "  rm.meisai_kingaku AS meisai_kingaku, "
					+ "  rm.shouhyou_shorui_hissu_flg AS shouhyou_shorui_hissu_flg "
					+ " FROM kaigai_ryohiseisan_meisai rm"
					+ " WHERE "
					+ "  rm.denpyou_id in (SELECT denpyou_id FROM shounin_route WHERE edano = '1' and user_id = ?) AND rm.shubetsu_cd = ? AND rm.kaigai_flg =? "
					+ "  AND rm.ic_card_no = '' "
					+ "  AND rm.himoduke_card_meisai IS NULL ";

		sql.append(sqlRm);
		params.add(seigyoUserId);
		params.add(shubetsuCd);
		params.add(kaigaiFlg);

		// 入力条件
		// 期間開始日と終了日
		sql.append(" AND (rm.kikan_from BETWEEN ? AND ? OR rm.kikan_to BETWEEN ? AND ? )");
		params.add(kikanFrom);
		params.add(kikanTo);
		params.add(kikanFrom);
		params.add(kikanTo);
		// 内容
		if(naiyou != null && !naiyou.isEmpty())
		{
			sql.append(" AND unify_kana_width(rm.naiyou) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(naiyou).append("%").toString());
		}

		sql.append(" UNION ALL ");
		// 海外旅費仮払精算のメインSQL
		String sqlRkm = " SELECT "
					+ "  rkm.kikan_from AS kikan_from, "
					+ "  rkm.kikan_to AS kikan_to, "
					+ "  rkm.shubetsu_cd AS shubetsu_cd, "
					+ "  rkm.shubetsu1 AS shubetsu1, "
					+ "  rkm.shubetsu2 AS shubetsu2, "
					+ "  rkm.koutsuu_shudan AS koutsuu_shudan, "
					+ "  '0' AS ryoushuusho_seikyuusho_tou_flg, "
					+ "  rkm.naiyou AS naiyou, "
					+ "  rkm.bikou AS bikou, "
					+ "  rkm.oufuku_flg AS oufuku_flg, "
					+ "  rkm.jidounyuuryoku_flg AS jidounyuuryoku_flg, "
					+ "  rkm.nissuu AS nissuu, "
					+ "  rkm.heishu_cd AS heishu_cd, "
					+ "  rkm.rate AS rate, "
					+ "  rkm.gaika AS gaika, "
					+ "  rkm.currency_unit AS currency_unit, "
					+ "  rkm.tanka AS tanka, "
					+ "  rkm.meisai_kingaku AS meisai_kingaku, "
					+ "  rkm.shouhyou_shorui_hissu_flg AS shouhyou_shorui_hissu_flg "
					+ " FROM kaigai_ryohi_karibarai_meisai rkm"
					+ " WHERE rkm.denpyou_id in (SELECT denpyou_id FROM shounin_route WHERE edano = '1' and user_id = ?) AND rkm.shubetsu_cd = ? AND rkm.kaigai_flg =? ";

		sql.append(sqlRkm);
		params.add(seigyoUserId);
		params.add(shubetsuCd);
		params.add(kaigaiFlg);

		// 入力条件
		// 期間開始日と終了日
		sql.append(" AND (rkm.kikan_from BETWEEN ? AND ? OR rkm.kikan_to BETWEEN ? AND ? )");
		params.add(kikanFrom);
		params.add(kikanTo);
		params.add(kikanFrom);
		params.add(kikanTo);
		// 内容
		if(naiyou != null && !naiyou.isEmpty())
		{
			sql.append(" AND unify_kana_width(rkm.naiyou) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(naiyou).append("%").toString());
		}

		// ソート条件
		sql.append(") ryohi");
		sql.append(" ORDER BY ryohi.kikan_from DESC");

		return connection.load(sql.toString(), params.toArray());
	}

/* 通勤定期(tsuukinteiki) */
	/**
	 * 通勤定期テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findTsuukinnteiki(String denpyouId){
		final String sql = "SELECT * FROM tsuukinteiki WHERE denpyou_id = ?";
		return connection.find(sql, denpyouId);
	}

/* 仮払(karibarai) */

	/**
	 * 「仮払案件」を検索する。データがなければnull。
	 * @param denpyouKbn      伝票区分
	 * @param denpyouID       伝票ID
	 * @param seigyoUserId    制御ユーザーID（旅費精算の場合は画面の使用者のユーザーIDを使用）
	 * @return                検索結果
	 */
	public List<GMap> loadKaribaraiAnken(String denpyouKbn, String denpyouID, String seigyoUserId){

		StringBuffer sqlbf = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		String kariDenpyou = "";
		String seisan = "";
		String select = "";
		String jouken = "";

		if(denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN)) {
			// 仮払申請の場合
			kariDenpyou = "karibarai"; // 仮払
			seisan = "keihiseisan"; // 経費精算
			select = "  ,CASE karibarai_on WHEN '1' THEN karibarai_kingaku ELSE kingaku END as kingaku "
					+ " ,karibarai_on "
					+ " ,kingaku shinsei_kingaku "
					+ " ,karibarai_kingaku ";
			jouken = "shounin_route WHERE edano = 1 AND user_id = ?)"; //ユーザーが起票者である

		} else if(denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN)) {
			// 旅費仮払申請の場合
			kariDenpyou = "ryohi_karibarai"; // 旅費仮払
			seisan = "ryohiseisan"; // 旅費精算
			select = "  ,CASE karibarai_on WHEN '1' THEN karibarai_kingaku ELSE kingaku END as kingaku "
					+ " ,karibarai_on "
					+ " ,houmonsaki "
					+ " ,mokuteki "
					+ " ,kingaku shinsei_kingaku "
					+ " ,karibarai_kingaku "
					+ " ,seisankikan_from "
					+ " ,seisankikan_from_hour "
					+ " ,seisankikan_from_min "
					+ " ,seisankikan_to "
					+ " ,seisankikan_to_hour "
					+ " ,seisankikan_to_min "
					+ " ,hosoku ";
			jouken = "ryohi_karibarai WHERE user_id = ?)"; //使用者が同一
		} else if(denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)) {
			// 海外旅費仮払申請の場合
			kariDenpyou = "kaigai_ryohi_karibarai"; // 海外旅費仮払
			seisan = "kaigai_ryohiseisan"; // 海外旅費精算
			select = "  ,CASE karibarai_on WHEN '1' THEN karibarai_kingaku ELSE kingaku END as kingaku "
					+ " ,karibarai_on "
					+ " ,houmonsaki "
					+ " ,mokuteki "
					+ " ,kingaku shinsei_kingaku "
					+ " ,karibarai_kingaku "
					+ " ,seisankikan_from "
					+ " ,seisankikan_from_hour "
					+ " ,seisankikan_from_min "
					+ " ,seisankikan_to "
					+ " ,seisankikan_to_hour "
					+ " ,seisankikan_to_min "
					+ " ,hosoku ";
			jouken = "kaigai_ryohi_karibarai WHERE user_id = ?)"; //使用者が同一
		} else {
			return null;//ありえないから落ちる
		}

		String sql = "SELECT "
		           + "  kari.denpyou_id "
		           + "  ,tekiyou "
		           + select
		           + "  ,0 as version "
		           + "  ,dsi.denpyou_shubetsu_url "
		           + "  ,dsi.denpyou_shubetsu "
		           + "  ,SUBSTR(kari.denpyou_id, 8, 4) as denpyou_kbn "
		           + "  ,kari.touroku_time "
		           + "  ,sj.touroku_time as shounin_time "
		           + "  ,CASE WHEN dkh.ringi_kingaku IS NULL THEN '' ELSE '1' END as ringi_hikitsugi_um_flg "
		           + "FROM "
		           + "    ( "
		           +          kariDenpyou + " kari "
		           + "        INNER JOIN shounin_joukyou sj "
		           + "          ON sj.denpyou_id = kari.denpyou_id "
		           + "          AND sj.joukyou_cd = '4' "
		           + "          AND sj.edano = ( "
		           + "            SELECT "
		           + "              MAX(edano) "
		           + "            FROM "
		           + "              shounin_joukyou "
		           + "            WHERE "
		           + "              denpyou_id = kari.denpyou_id "
		           + "              AND joukyou_cd = '4' "
		           + "          ) "
		           + "    ) "
		           + "      INNER JOIN denpyou_shubetsu_ichiran dsi "
		           + "        ON SUBSTR(kari.denpyou_id, 8, 4) = dsi.denpyou_kbn "
		           + "      INNER JOIN denpyou_kian_himozuke dkh "
		           + "        ON kari.denpyou_id = dkh.denpyou_id "
		           + "WHERE "
		           + "  kari.denpyou_id IN (SELECT denpyou_id FROM " + jouken
		           //承認済である
		           + "  AND kari.denpyou_id IN (SELECT denpyou_id FROM denpyou WHERE denpyou_joutai = '30')"
		           //他の精算伝票（否認済と取下済は無視）に紐付かない
		           + "  AND kari.denpyou_id NOT IN (SELECT karibarai_denpyou_id FROM " + seisan + " seisan INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id WHERE d.denpyou_joutai NOT IN ('40', '90'))";

		// メインSQL
		sqlbf.append(sql);
		params.add(seigyoUserId); // 制御ユーザーID（旅費精算・海外旅費精算の場合は画面の使用者のユーザーIDを使用）

		// 検索条件（伝票ID）
		if(denpyouID != null && !denpyouID.isEmpty()) {
			sqlbf.append("  AND kari.denpyou_id = ? ");
			params.add(denpyouID);
		}

		// ソート条件
		sqlbf.append(" ORDER BY kari.denpyou_id");

		return connection.load(sqlbf.toString(), params.toArray());
	}

	/**
	 * 仮払いテーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public  GMap findKaribarai(String denpyouId){
		final String sql = "SELECT * FROM karibarai WHERE denpyou_id = ?";
		return connection.find(sql, denpyouId);
	}

/* 経費精算(keihiseisan) */
	/**
	 * 仮払データと結合して経費精算テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	public GMap findKeihiSeisan(String denpyouId){
		final String sql = "SELECT "
						 + "   ke.* "
						 + "  ,ka.kingaku "
						 + "  ,ka.karibarai_kingaku "
						 + "  ,ka.tekiyou "
						 + "FROM keihiseisan ke "
						 + "LEFT OUTER JOIN karibarai ka ON "
						 + "  ke.karibarai_denpyou_id = ka.denpyou_id "
						 + "WHERE ke.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 指定した仮払申請に紐づく経費精算テーブルを検索する。データがなければnull
	 * @param karibaraiDenpyouId 仮払伝票ID
	 * @return 検索結果
	 */
	public GMap findKeihiSeisanWithKaribarai(String karibaraiDenpyouId){
		final String sql = "SELECT seisan.denpyou_id FROM keihiseisan seisan INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id WHERE d.denpyou_joutai NOT IN ('40', '90') AND karibarai_denpyou_id = ?";
		return connection.find(sql, karibaraiDenpyouId);
	}

/* 経費精算明細(keihiseisan_meisai) */
	/**
	 * 経費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKeihiSeisanMeisai(String denpyouId) {
		final String sql = "SELECT * FROM keihiseisan_meisai WHERE denpyou_id =? order by denpyou_edano";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 経費精算明細テーブルを検索する。(参照起票用に法人カード紐付を除外)
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKeihiSeisanMeisaiNoHoujinHimoduke(String denpyouId) {
		final String sql = "SELECT * FROM keihiseisan_meisai WHERE denpyou_id =? AND himoduke_card_meisai IS NULL order by denpyou_edano";
		return connection.load(sql, denpyouId);
	}


/* 振替(furikae) */
	/**
	 * 振替伝票テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findFurikae(String denpyouId){
		final String sql = "SELECT * FROM furikae WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}


/* 付替(tsukekae) */
	/**
	 * 付替テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findTsukekae(String denpyouId){
		final String sql = "SELECT * FROM tsukekae WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}


/* 付替明細(tsukekae_meisai) */
	/**
	 * 付替明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadTsukekaeMeisai(String denpyouId) {
		final String sql = "SELECT * FROM tsukekae_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano ";
		return connection.load(sql, denpyouId);
	}


/* 自動引落(jidouhikiotoshi) */
	/**
	 * 自動引落テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findJidouhikiotoshi(String denpyouId){
		final String sql = "SELECT * FROM jidouhikiotoshi WHERE denpyou_id =?";
		return connection.find(sql, denpyouId);
	}

/* 自動引落明細(jidouhikiotoshi_meisai) */
	/**
	 * 請求書払い明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadJidouhikiotoshiMeisai(String denpyouId) {
		final String sql = "SELECT * FROM jidouhikiotoshi_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/* 交通費精算(koutsuuhiseisan) */
	/**
	 * 交通費精算テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findKoutsuuhiSeisan(String denpyouId){
		final String sql = "SELECT * FROM koutsuuhiseisan WHERE denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

/* 交通費精算明細(koutsuuhiseisan_meisai) */
	/**
	 * 交通費精算明細テーブルを検索する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKoutsuuhiSeisanMeisai(String denpyouId) {
		final String sql = "SELECT * FROM koutsuuhiseisan_meisai WHERE denpyou_id =? ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 交通費精算明細テーブルを検索する。(参照起票用にICカード・法人カード紐付を除外)
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadKoutsuuhiSeisanMeisaiNOIC(String denpyouId) {
		final String sql = "SELECT * FROM koutsuuhiseisan_meisai WHERE denpyou_id =? AND ic_card_no = '' AND himoduke_card_meisai IS NULL ORDER BY denpyou_edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * ユーザーに紐づく交通費精算明細データを検索する。
	 * @param kikanFrom      期間開始日
	 * @param kikanTo        期間終了日
	 * @param naiyou         内容
	 * @param seigyoUserId   制御ユーザーID
	 * @return               検索結果
	 */
	public List<GMap> loadKoutsuuhiSeisanMeisaiForUser(Date kikanFrom, Date kikanTo, String naiyou, String seigyoUserId){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		String sqls = " SELECT * "
					+ " FROM koutsuuhiseisan_meisai rm "
					+ " WHERE "
					+ "  denpyou_id in (SELECT denpyou_id FROM shounin_route WHERE edano = '1' and user_id = ?) "
					+ "  AND rm.ic_card_no = '' "
					+ "  AND rm.himoduke_card_meisai IS NULL ";

		sql.append(sqls);         // メインSQL
		params.add(seigyoUserId); // 制御ユーザーID

		// 入力条件
		// 期間開始日と終了日
		sql.append(" AND (kikan_from BETWEEN ? AND ?)");
		params.add(kikanFrom);
		params.add(kikanTo);
		// 内容
		if(naiyou != null && !naiyou.isEmpty())
		{
			sql.append(" AND unify_kana_width(naiyou) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(naiyou).append("%").toString());
		}

		// ソート条件
		sql.append(" ORDER BY kikan_from DESC");

		return connection.load(sql.toString(), params.toArray());
	}


/* 仕訳(shiwake) */
	/**
	 * 伝票に紐付く仕訳(de3)有無を調べる
	 * @param denpyouId 伝票ID
	 * @return 仕訳があればtrue
	 */
	@Deprecated
	public boolean hasShiwakeDe3(String denpyouId) {
		final String sql = "SELECT COUNT(*) count FROM shiwake_de3 WHERE denpyou_id = ?";
		return ((long)connection.find(sql, denpyouId).get("count")) > 0;
	}
	/**
	 * 伝票に紐付く仕訳(SIAS)有無を調べる
	 * @param denpyouId 伝票ID
	 * @return 仕訳があればtrue
	 */
	@Deprecated
	public boolean hasShiwakeSias(String denpyouId) {
		final String sql = "SELECT COUNT(*) count FROM shiwake_sias WHERE denpyou_id = ?";
		return ((long)connection.find(sql, denpyouId).get("count")) > 0;
	}

/* 仕訳パターン(shiwake_pattern_master) */
	/**
	 * 仕訳パターンを取得する。
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdano 仕訳枝番号
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findShiwakePattern(String denpyouKbn, int shiwakeEdano) {
		final String sql = "SELECT * FROM shiwake_pattern_master where denpyou_kbn = ? and shiwake_edano = ?";
		return connection.find(sql, denpyouKbn, shiwakeEdano);
	}

	/**
	 * 取引と部門の紐付けチェック
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdano 仕訳枝番号
	 * @param bumonCd 部門コード
	 * @return 紐付けあり
	 */
	public boolean bumonTorihikiCheck(String denpyouKbn, int shiwakeEdano, String bumonCd) {
		String sql =
			"SELECT shiwake_edano "
		+ "FROM shiwake_pattern_master sp "
		+ "WHERE "
		+ "  sp.denpyou_kbn = ? "
		+ "  AND sp.shiwake_edano = ? "
		+ "  AND sp.delete_flg = '0' "
		+ "  AND ( "
		+ "    (sp.default_hyouji_flg = '1') "
		+ "    OR (exists (SELECT * FROM shozoku_bumon_shiwake_pattern_master tmp WHERE (tmp.denpyou_kbn, tmp.shiwake_edano, tmp.bumon_cd)=(sp.denpyou_kbn, sp.shiwake_edano, ?)))) "
		+ "ORDER BY "
		+ "  sp.hyouji_jun ";
		return connection.find(sql, denpyouKbn, shiwakeEdano, bumonCd) != null;
	}

	/**
	 * 仕訳パターンを取得する。
	 * @param denpyouKbn 伝票区分
	 * @return 検索結果
	 */
	@Deprecated
	public  List<GMap> findShiwakePattern(String denpyouKbn) {
		final String sql = "SELECT * FROM shiwake_pattern_master where denpyou_kbn = ? and delete_flg = '0' and default_hyouji_flg = '1' and current_date BETWEEN yuukou_kigen_from and yuukou_kigen_to";
		return connection.load(sql, denpyouKbn);
	}
	
	/**
	 * CSVダウンロード用に仕訳パターンを取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadShiwakePatternForCSV() {
		final String primaryKeys = "" 
				+ "  denpyou_kbn "
				+ " ,shiwake_edano ";
		final String sql = "SELECT * FROM shiwake_pattern_master ORDER BY "+ primaryKeys; // ORDER BYではPrimary keyで一意に順序が決まるので、それ以下は意味なし。全カラム指定にされていたので修正。
		return connection.load(sql);
	}

/* 部門取引パターン(bumon_torihiki_pattern) */



/* 取引(仕訳)一覧(torihiki_ichiran) */
	/**
	 * 伝票種別をキーに取引(仕訳)一覧を取得する。（取引選択画面用）
	 * @param denpyouKbn 伝票種別
	 * @param bumonCd （所属）部門コード
	 * @return 取引のリスト
	 */
	public List<GMap> loadTorihiki(String denpyouKbn, String bumonCd) {
		String sql =
			"SELECT "
		+ "  sp.*, "
		+ "  km.karikanjou_keshikomi_no_flg, "
		+ "  km.kamoku_name_ryakushiki, "
		+ "  km.shori_group, "
		+ "  bm.futan_bumon_name, "
		+ "  ke.edaban_name "
		+ "FROM shiwake_pattern_master sp "
		+ "LEFT OUTER JOIN kamoku_master km ON "
		+ "  sp.kari_kamoku_cd = km.kamoku_gaibu_cd "
		+ "LEFT OUTER JOIN kamoku_edaban_zandaka ke ON "
		+ "  sp.kari_kamoku_cd = ke.kamoku_gaibu_cd "
		+ "  AND sp.kari_kamoku_edaban_cd = ke.kamoku_edaban_cd "
		+ "LEFT OUTER JOIN bumon_master bm ON "
		+ "  sp.kari_futan_bumon_cd = bm.futan_bumon_cd "
		+ "WHERE "
		+ "  sp.denpyou_kbn = ? "
		+ "  AND 1 <= sp.shiwake_edano "
		+ "  AND current_date BETWEEN sp.yuukou_kigen_from AND sp.yuukou_kigen_to "
		+ "  AND sp.delete_flg = '0' "
		+ "  AND ( "
		+ "    (sp.default_hyouji_flg = '1') "
		+ "    OR (exists (SELECT * FROM shozoku_bumon_shiwake_pattern_master tmp WHERE (tmp.denpyou_kbn, tmp.shiwake_edano, tmp.bumon_cd)=(sp.denpyou_kbn, sp.shiwake_edano, ?)))) "
		+ "ORDER BY "
		+ "  sp.hyouji_jun, sp.shiwake_edano ";
		return connection.load(sql, denpyouKbn, bumonCd);
	}
	/**
	 * 伝票種別をキーに取引(仕訳)一覧を取得する。
	 * @param denpyouKbn String 伝票種別
	 * @param sortItem String ソートする項目
	 * @param sortOrder String ソート順
	 * @return 検索結果
	 */
	public List<GMap> selectTorihikiIchiran(String denpyouKbn, String sortItem, String sortOrder){

		String sql = "SELECT a.denpyou_kbn, a.shiwake_edano, a.yuukou_kigen_from, a.yuukou_kigen_to, a.bunrui1, a.bunrui2, a.bunrui3, a.torihiki_name, "
						+  "a.kari_kamoku_cd, b.kamoku_name_ryakushiki as kari_kamoku_name, "
						+  "a.kashi_kamoku_cd1, c.kamoku_name_ryakushiki as kashi_kamoku_name1, "
						+  "a.kashi_kamoku_cd2, d.kamoku_name_ryakushiki as kashi_kamoku_name2, "
						+  "a.kashi_kamoku_cd3, e.kamoku_name_ryakushiki as kashi_kamoku_name3, "
						+  "a.kashi_kamoku_cd4, f.kamoku_name_ryakushiki as kashi_kamoku_name4, "
						+  "a.kashi_kamoku_cd5, g.kamoku_name_ryakushiki as kashi_kamoku_name5 "
						+  "FROM shiwake_pattern_master a "
						+  "LEFT OUTER JOIN kamoku_master b "
						+  "ON b.kamoku_gaibu_cd = a.kari_kamoku_cd "
						+  "LEFT OUTER JOIN kamoku_master c "
						+  "ON c.kamoku_gaibu_cd = a.kashi_kamoku_cd1 "
						+  "LEFT OUTER JOIN kamoku_master d "
						+  "ON d.kamoku_gaibu_cd = a.kashi_kamoku_cd2 "
						+  "LEFT OUTER JOIN kamoku_master e "
						+  "ON e.kamoku_gaibu_cd = a.kashi_kamoku_cd3 "
						+  "LEFT OUTER JOIN kamoku_master f "
						+  "ON f.kamoku_gaibu_cd = a.kashi_kamoku_cd4 "
						+  "LEFT OUTER JOIN kamoku_master g "
						+  "ON g.kamoku_gaibu_cd = a.kashi_kamoku_cd5 "
						+  "WHERE  a.denpyou_kbn = ? AND a.delete_flg = '0' ";

		sql = sql + "ORDER BY (CASE WHEN current_date > a.yuukou_kigen_to THEN 1 ELSE 0 END), " + "a." + sortItem + " " + sortOrder + ", a.shiwake_edano ";
		return connection.load(sql, denpyouKbn);
	}
	/**
	 * 仕訳パターンを削除する。
	 * @param userId ユーザーID
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdano 仕訳枝番号
	 * @return 処理件数
	 */
	public int deleteTorihikiIchiran(String userId, String denpyouKbn, int shiwakeEdano) {

		final String sql1 = "UPDATE shiwake_pattern_master SET delete_flg = '1', koushin_user_id = ?, koushin_time = current_timestamp WHERE denpyou_kbn = ? and shiwake_edano = ?";
		int ret = connection.update(sql1, userId, denpyouKbn, shiwakeEdano);

		if(ret > 0)
		{
			final String sql2 = "DELETE FROM shozoku_bumon_shiwake_pattern_master WHERE denpyou_kbn = ? and shiwake_edano = ?";
			connection.update(sql2, denpyouKbn, shiwakeEdano);
		}

		return ret;
	}

/* 取引（仕訳）追加(torihiki_tsuika) */
	/**
	 * 仕訳パターン設定テーブルから仕訳パターン設定情報を取得する。
	 * @param denpyouKbn String 伝票区分
	 * @param settingKbn String 設定区分
	 * @param settingItem String 設定項目
	 * @return 検索結果
	 */
	@Deprecated
	public GMap findShiwakePatternSetting(String denpyouKbn, String settingKbn, String settingItem){
		final String sql =
				"SELECT "
			+ "     default_value "
			+ "    ,hyouji_flg "
			+ "    ,shiwake_pattern_var1 "
			+ "    ,shiwake_pattern_var2 "
			+ "    ,shiwake_pattern_var3 "
			+ "    ,shiwake_pattern_var4 "
			+ "    ,shiwake_pattern_var5 "
			+ "FROM shiwake_pattern_setting "
			+ "WHERE "
			+ "     denpyou_kbn = ? "
			+ " AND setting_kbn = ? "
			+ " AND setting_item = ? ";

		return connection.find(sql, denpyouKbn, settingKbn, settingItem);
	}
	/**
	 * 仕訳パターン設定テーブルから仕訳パターン設定情報を取得する。
	 * @param denpyouKbn String 伝票区分
	 * @param settingKbn String 設定区分
	 * @param settingItem String 設定項目
	 * @return 検索結果
	 */
	@Deprecated
	public String findShiwakePatternDefaultValue(String denpyouKbn, String settingKbn, String settingItem){
		GMap record = findShiwakePatternSetting(denpyouKbn, settingKbn, settingItem);
		return (null == record) ? "" : (String)record.get("default_value");
	}

	/**
	 * 仕訳パターン変数の設定を取得する。
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> selectShiwakeVar(){

		final String sql =
				" SELECT *"
			+ "   FROM shiwake_pattern_var_setting "
			+ "  ORDER BY hyouji_jun";

		return connection.load(sql);
	}


	/**
	 * 仕訳パターン変数の設定を取得する。
	 * @param denpyouKbn 伝票区分
	 * @param settingKbn 設定区分
	 * @param settingItem 設定項目
	 * @return 検索結果
	 */
	public List<GMap> selectShiwakeVar(String denpyouKbn, String settingKbn, String settingItem){

		final String sql =
		    "SELECT a.shiwake_pattern_var"
		+ "      ,b.shiwake_pattern_var_name"
		+ "  FROM (SELECT shiwake_pattern_var1 as shiwake_pattern_var"
		+ "          FROM shiwake_pattern_setting"
		+ "         where denpyou_kbn  = ?"
		+ "           AND setting_kbn  = ?"
		+ "           AND setting_item = ?"
		+ "         UNION ALL"
		+ "        SELECT shiwake_pattern_var2"
		+ "          FROM shiwake_pattern_setting"
		+ "         where denpyou_kbn  = ?"
		+ "           AND setting_kbn  = ?"
		+ "           AND setting_item = ?"
		+ "         UNION ALL"
		+ "        SELECT shiwake_pattern_var3"
		+ "          FROM shiwake_pattern_setting"
		+ "         where denpyou_kbn  = ?"
		+ "           AND setting_kbn  = ?"
		+ "           AND setting_item = ?"
		+ "         UNION ALL"
		+ "        SELECT shiwake_pattern_var4"
		+ "          FROM shiwake_pattern_setting"
		+ "         where denpyou_kbn  = ?"
		+ "           AND setting_kbn  = ?"
		+ "           AND setting_item = ?"
		+ "         UNION ALL"
		+ "        SELECT shiwake_pattern_var5"
		+ "          FROM shiwake_pattern_setting"
		+ "         where denpyou_kbn  = ?"
		+ "           AND setting_kbn  = ?"
		+ "           AND setting_item = ?"
		+ "       ) a"
		+ " INNER JOIN shiwake_pattern_var_setting b"
		+ "    ON a.shiwake_pattern_var  = b.shiwake_pattern_var"
		+ " WHERE a.shiwake_pattern_var != ''"
		+ " ORDER BY b.hyouji_jun";

		List<Object> params = new ArrayList<Object>();

		params.add(denpyouKbn);
		params.add(settingKbn);
		params.add(settingItem);

		params.add(denpyouKbn);
		params.add(settingKbn);
		params.add(settingItem);

		params.add(denpyouKbn);
		params.add(settingKbn);
		params.add(settingItem);

		params.add(denpyouKbn);
		params.add(settingKbn);
		params.add(settingItem);

		params.add(denpyouKbn);
		params.add(settingKbn);
		params.add(settingItem);

		return connection.load(sql, params.toArray());
	}

/* 部門別取引(仕訳)一覧(BumonbetsuTorihikiIchiran) */
	/**
	 * 伝票種別をキーに部門別取引(仕訳)一覧を取得する。
	 * @param denpyouKbn String 伝票区分
	 * @param shozokuBumonCd String 所属部門コード
	 * @param torihikiName String 取引名('%取引名%'のよう)
	 * @param tourokuStatus String 登録状態(0, 1)
	 * @param sortItem String ソートする項目
	 * @param sortOrder String ソート順
	 * @return 検索結果
	 */
	public List<GMap> selectBumonbetsuTorihikiIchiran(String denpyouKbn, String shozokuBumonCd, String torihikiName, String tourokuStatus, String sortItem, String sortOrder){

		String sql =
				  " SELECT"
				+ "     A.touroku_status"
				+ "    ,A.denpyou_kbn"
				+ "    ,A.shiwake_edano "
				+ "    ,B.name"
				+ "    ,A.shiwake_edano"
				+ "    ,A.bunrui1"
				+ "    ,A.bunrui2"
				+ "    ,A.bunrui3"
				+ "    ,A.torihiki_name"
				+ "    ,A.default_hyouji_flg"
				+ "    ,A.kari_kamoku_cd"
				+ "    ,C.kamoku_name_ryakushiki AS kari_kamoku_name"
				+ "    ,A.yuukou_kigen_from "
				+ "    ,A.yuukou_kigen_to "
				+ "  FROM"
				+ "  ("
				+ "    SELECT"
				+ "      case when A2.bumon_cd is null then 0 else 1 end AS touroku_status"
				+ "      ,A1.denpyou_kbn"
				+ "      ,A1.shiwake_edano"
				+ "      ,A1.bunrui1"
				+ "      ,A1.bunrui2"
				+ "      ,A1.bunrui3"
				+ "      ,A1.torihiki_name"
				+ "      ,A1.default_hyouji_flg"
				+ "      ,A1.kari_kamoku_cd"
				+ "      ,A1.hyouji_jun"
				+ "      ,A1.yuukou_kigen_from "
				+ "      ,A1.yuukou_kigen_to "
				+ "     FROM shiwake_pattern_master A1"
				+ "     LEFT OUTER JOIN shozoku_bumon_shiwake_pattern_master A2 ON"
				+ "       A2.bumon_cd = ?"
				+ "       AND A2.denpyou_kbn = A1.denpyou_kbn"
				+ "       AND A2.shiwake_edano = A1.shiwake_edano"
				+ "     WHERE"
				+ "       A1.denpyou_kbn = ? AND A1.delete_flg = '0'";

		List<Object> params = new ArrayList<Object>();

		params.add(shozokuBumonCd);
		params.add(denpyouKbn);

		// 取引名条件の設定
		if (null != torihikiName && !"".equals(torihikiName.trim())) {
			sql +=
				  "       AND unify_kana_width(A1.torihiki_name) LIKE unify_kana_width(?)";
				  params.add(new StringBuilder().append("%").append(torihikiName.trim()).append("%").toString());
		}

		sql +=
				  "  ) A  "
				+ "  LEFT OUTER JOIN naibu_cd_setting B ON"
				+ "    B.naibu_cd_name = 'shiwake_pattern_denpyou_kbn'"
				+ "    AND B.naibu_cd = A.denpyou_kbn"
				+ "  LEFT OUTER JOIN kamoku_master C ON "
				+ "    C.kamoku_gaibu_cd = A.kari_kamoku_cd"
				+ "  WHERE"
				+ "    A.touroku_status IN (" + tourokuStatus + ")";
		sql +=
				" ORDER BY (CASE WHEN current_date > a.yuukou_kigen_to THEN 1 ELSE 0 END), " + " A." + sortItem + " " + sortOrder + ", A.shiwake_edano ";

		return connection.load(sql, params.toArray());
	}

	/**
	 * 部門別取引(仕訳)一覧を取得する。
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadBumonbetsuTorihikiIchiran(){
		final String sql =
			    "SELECT * FROM shozoku_bumon_shiwake_pattern_master ORDER BY bumon_cd, denpyou_kbn, shiwake_edano ";
		return connection.load(sql);
	}

/* CSVダウンロード(CSVDownload) */
	/**
	 * 対象ファイルリスト検索
	 * @param fbStatus String FB抽出状態
	 * @param fromDate Date 月初日の日付
	 * @param toDate Date 月末日の日付
	 * @return 検索結果
	 */
	@Deprecated
	public List<GMap> loadTaishouFileList(String fbStatus, Date fromDate, Date toDate){
		final String sql =
				" SELECT "
			+ "       DISTINCT furikomi_date "
			+ " FROM  fb "
			+ " WHERE "
			+ "       fb_status = ? "
			+ "   AND furikomi_date BETWEEN ? AND ? "
// +	"   AND denpyou_id IN (SELECT denpyou_id FROM shounin_route WHERE gyoumu_role_id = ? )  "
			+ " ORDER BY "
			+ "   furikomi_date ASC ";

		return connection.load(sql, fbStatus, fromDate, toDate);
	}

	/**
	 * FB抽出データの検索
	 * @param fbStatus String FB抽出状態
	 * @param  gyoumuRoleShozokuBumonCd 業務ロール所属部門コード
	 * @param furikomiDate Date 振込日
	 * @return 検索結果
	 */
	public List<GMap> loadFbDataList(String fbStatus, String[] gyoumuRoleShozokuBumonCd, Date furikomiDate){
		BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

		String sql =

				" SELECT "
			+ " 	 A.denpyou_id "
			+ " 	,A.kokyaku_cd1 "
			+ " 	,B.saki_kouza_meigi_kana "
			+ " 	,B.saki_kouza_meigi_kanji "
			+ " 	,A.furikomi_date "
			+ " 	,C.bumon_cd "
			+ " 	,C.bumon_name "
			+ " 	,A.kingaku "
			+ " 	,A.moto_kinyuukikan_cd "
			+ " 	,A.moto_kinyuukikan_name_hankana "
			+ " 	,A.moto_kinyuukikan_shiten_cd "
			+ " 	,A.moto_kinyuukikan_shiten_name_hankana "
			+ " 	,A.moto_yokin_shumoku_cd "
			+ " 	,A.moto_kouza_bangou "
			+ " 	,A.saki_kinyuukikan_cd "
			+ " 	,A.saki_kinyuukikan_name_hankana "
			+ " 	,A.saki_kinyuukikan_shiten_cd "
			+ " 	,A.saki_kinyuukikan_shiten_name_hankana "
			+ " 	,A.saki_yokin_shumoku_cd "
			+ " 	,A.saki_kouza_bangou "
			+ " FROM  "
			+ "     fb A "
			+ " LEFT OUTER JOIN "
			+ " (  "
			+ " 	SELECT  "
			+ " 		 B1.user_id "
			+ " 		,B1.shain_no "
			+ " 		,B2.saki_kouza_meigi_kana "
			+ " 		,B2.saki_kouza_meigi_kanji "
			+ " 	FROM  "
			+ " 		user_info B1  "
			+ " 	LEFT OUTER JOIN  "
			+ " 		shain_kouza B2  "
			+ " 	ON B2.shain_no = B1.shain_no  "
			+ " ) B "
			+ "   ON A.user_id = B.user_id "
			+ " LEFT OUTER JOIN "
			+ " (  "
			+ " 	SELECT  "
			+ " 		 C1.denpyou_id "
			+ " 		,C1.edano "
			+ " 		,C1.bumon_cd "
			+ " 		,C2.bumon_name "
			+ " 	FROM  "
			+ " 		shounin_route C1  "
			+ " 	LEFT OUTER JOIN  "
			+ " 		shozoku_bumon C2  "
			+ " 	ON C2.bumon_cd = C1.bumon_cd  "
			+ " 	AND ( ? BETWEEN C2.yuukou_kigen_from AND C2.yuukou_kigen_to )  "
			+ " ) C "
			+ "   ON A.denpyou_id = C.denpyou_id  "
			+ " WHERE  "
			+ "       A.fb_status = ?  "
			+ "   AND A.furikomi_date = ?  "
			+ "   AND C.edano = '1'  ";

		if(!(Arrays.asList(gyoumuRoleShozokuBumonCd).contains(BUMON_CD.ZENSHA)) ) {
			sql +=   "    AND C.bumon_cd IN ( " + bumonUserLogic.getMergeGyoumuRoleShozokuBumonCd(gyoumuRoleShozokuBumonCd, true) + " )";
		}
			sql += " ORDER BY  "
				+ "    A.moto_kouza_bangou ASC  "
				+ "   ,B.saki_kouza_meigi_kana ASC  "
				+ "   ,A.denpyou_id ASC  ";

		return connection.load(sql, furikomiDate, fbStatus, furikomiDate);
	}

/* FBデータ作成予定(fb_data) */


/* FBデータ再作成 */
	/**
	 * 個人精算支払日検索
	 * @param limitNum int limitNum
	 * @return 検索結果
	 */
	public List<GMap> loadKojinSeisanShiharaiBi(int limitNum){
		final String sql =
				" SELECT "
			+ " 	* "
			+ " FROM "
			+ " ("
			+ " 	SELECT "
			+ " 		DISTINCT A.kojinseisan_shiharaibi,"
			+ " 		CASE WHEN B.furikomi_date IS NOT NULL THEN 1 ELSE 0 END AS sakuseizumi "
			+ " 	FROM  hizuke_control A "
			+ " 	LEFT OUTER JOIN "
			+ " 		( "
			+ " 			SELECT DISTINCT furikomi_date "
			+ " 			FROM  fb "
			+ " 			WHERE fb_status = '1' "
			+ " 		) B "
			+ " 	ON A.kojinseisan_shiharaibi = B.furikomi_date "
			+ " 	WHERE "
			+ " 		A.kojinseisan_shiharaibi >= current_date "
			+ " ) C "
			+ " ORDER BY "
			+ "   kojinseisan_shiharaibi ASC "
			+ " LIMIT ? "
			+ " OFFSET 0 ";

		return connection.load(sql, limitNum);
	}

/* 請求書払いCSVアップロード */

/* 請求書計上日締日 */
	/**
	 * 請求書計上日締日 取得
	 * @return 請求書計上日締日(降順)
	 */
	@Deprecated
	public Date findMaxShimebi() {
		final String sql = "SELECT MAX(shimebi) shimebi FROM keijoubi_shimebi where denpyou_kbn = 'A003'";
		GMap record = connection.find(sql);
		return (record == null) ? null : (Date)record.get("shimebi");
	}
	
	/**
	 * 伝票区分による計上日締日 取得
	 * @param denpyouKbn 伝票区分
	 * @return 請求書計上日締日(降順)
	 */
	@Deprecated
	public Date findMaxShimebiForDenpyouKbn(String denpyouKbn) {
		final String sql = "SELECT MAX(shimebi) shimebi FROM keijoubi_shimebi where denpyou_kbn = ?";
		GMap record = connection.find(sql, denpyouKbn);
		return (record == null) ? null : (Date)record.get("shimebi");
	}

/* 伝票区分別 計上日締日 */
	/**
	 * 計上日締日 取得
	 * @param denpyouKbn 伝票区分
	 * @return 請求書計上日締日(降順)
	 */
	@Deprecated
	public List<GMap> loadKeijoubiShimebi(String denpyouKbn) {
		final String sql = "SELECT shimebi FROM keijoubi_shimebi WHERE denpyou_kbn = ? ORDER BY shimebi DESC LIMIT 10";
		return connection.load(sql, denpyouKbn);
	}
	
/* ICカード利用履歴(ic_card_rireki) */
	/**
	 * ICカード情報取得
	 * @param userId        ユーザーID
	 * @param kikankaishi   期間開始日
	 * @param kikansyuuryou 期間終了日
	 * @param lineNameFrom  出発路線名
	 * @param ekiNameFrom   出発駅名
	 * @param lineNameTo    到着路線名
	 * @param ekiNameTo     到着駅名
	 * @param sortKbn       ソート区分
	 * @param jyogaiDataHyoujiFlg 除外データ表示フラグ
	 * @return リスト
	 */
	public List<GMap> loadIcCardRireki(String userId, Date kikankaishi, Date kikansyuuryou, String lineNameFrom, String ekiNameFrom, String lineNameTo, String ekiNameTo, String sortKbn, String jyogaiDataHyoujiFlg){
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//否認済・取下済の場合はICカード履歴一覧から表示除外しない
		String denpyouHinin = EteamNaibuCodeSetting.DENPYOU_JYOUTAI.HININ_ZUMI;
		String denpyouTorikeshi = EteamNaibuCodeSetting.DENPYOU_JYOUTAI.TORISAGE_ZUMI;
		
		String sqlRm = "SELECT icr.ic_card_no, icr.ic_card_sequence_no, icr.ic_card_riyoubi, icr.tanmatu_cd, icr.line_name_from, icr.eki_name_from, icr.line_name_to, icr.eki_name_to, icr.kingaku "
					 + "FROM ic_card_rireki icr "
					 + "WHERE "
					 + "   NOT EXISTS"
					 + "      (SELECT ic_card_no "
					 + "       FROM ryohiseisan_meisai rsm "
					 + "       INNER JOIN denpyou dp1 "
					 + "       ON rsm.denpyou_id = dp1.denpyou_id "
					 + "       WHERE icr.ic_card_no = rsm.ic_card_no "
					 + "          AND icr.ic_card_sequence_no = rsm.ic_card_sequence_no "
					 + "          AND '" + denpyouHinin + "' != dp1.denpyou_joutai "
					 + "          AND '" + denpyouTorikeshi + "' != dp1.denpyou_joutai) "
					 + "   AND NOT EXISTS"
					 + "      (SELECT ic_card_no "
					 + "       FROM kaigai_ryohiseisan_meisai rkm "
					 + "       INNER JOIN denpyou dp2 "
					 + "       ON rkm.denpyou_id = dp2.denpyou_id "
					 + "       WHERE icr.ic_card_no = rkm.ic_card_no "
					 + "          AND icr.ic_card_sequence_no = rkm.ic_card_sequence_no "
					 + "          AND '" + denpyouHinin + "' != dp2.denpyou_joutai "
					 + "          AND '" + denpyouTorikeshi + "' != dp2.denpyou_joutai) "
					 + "   AND NOT EXISTS"
					 + "      (SELECT ic_card_no "
					 + "       FROM koutsuuhiseisan_meisai ksm "
					 + "       INNER JOIN denpyou dp3 "
					 + "       ON ksm.denpyou_id = dp3.denpyou_id "
					 + "       WHERE icr.ic_card_no = ksm.ic_card_no "
					 + "          AND icr.ic_card_sequence_no = ksm.ic_card_sequence_no "
					 + "          AND '" + denpyouHinin + "' != dp3.denpyou_joutai "
					 + "          AND '" + denpyouTorikeshi + "' != dp3.denpyou_joutai) "
					 + "   AND icr.user_id = ? "
					 + "   AND icr.ic_card_riyoubi BETWEEN ? AND ? "
					 + "   AND NOT (icr.line_name_from = icr.line_name_to  AND eki_name_from = icr.eki_name_to AND icr.kingaku = 0) "
					 + "   AND shori_cd NOT IN ('0','2','3','7','20','70','73','198') "; // 20221219 対象外処理コードの除外を追加
		
		sql.append(sqlRm);
		params.add(userId);
		params.add(kikankaishi);
		params.add(kikansyuuryou);
		
		if(lineNameFrom != null && !lineNameFrom.isEmpty()){
			sql.append("   AND unify_kana_width(icr.line_name_from) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(lineNameFrom).append("%").toString());
		}
		
		if(ekiNameFrom != null && !ekiNameFrom.isEmpty()){
			sql.append("   AND unify_kana_width(icr.eki_name_from) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(ekiNameFrom).append("%").toString());
		}
		
		if(lineNameTo != null && !lineNameTo.isEmpty()){
			sql.append("   AND unify_kana_width(icr.line_name_to) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(lineNameTo).append("%").toString());
		}
		
		if(ekiNameTo != null && !ekiNameTo.isEmpty()){
			sql.append("   AND unify_kana_width(icr.eki_name_to) LIKE unify_kana_width(?) ");
			params.add(new StringBuilder().append("%").append(ekiNameTo).append("%").toString());
		}
		
		if(("1").equals(jyogaiDataHyoujiFlg)){
			sql.append("   AND icr.jyogai_flg = '1' ");
		}else {
			sql.append("   AND icr.jyogai_flg = '0' ");
		}

		if(("2").equals(sortKbn)){
			sql.append("ORDER BY icr.ic_card_riyoubi DESC, icr.ic_card_sequence_no DESC ");
		}else {
			sql.append("ORDER BY icr.ic_card_riyoubi ASC, icr.ic_card_sequence_no ASC ");
		}
		
		
		return connection.load(sql.toString(), params.toArray());
	}
	
	/**
	 * 指定した起案伝票が添付済みの有効な伝票の数を取得する
	 * @param oyaDenpyouId 伝票ID
	 * @param kianDenpyouId 起案伝票ID
	 * @return 起案添付済み伝票数
	 */
	public int countKianTenpuZumi(String oyaDenpyouId, String kianDenpyouId){
		final String sql = "SELECT count(*) AS count FROM denpyou_kian_himozuke k INNER JOIN denpyou d ON d.denpyou_id = k.denpyou_id"
						 + " WHERE d.denpyou_joutai NOT IN ('40', '90') AND k.denpyou_id <> ? AND k.kian_denpyou = ?";
		return Integer.parseInt(connection.find(sql, oyaDenpyouId, kianDenpyouId).get("count").toString());
	}

	/**
	 * 同じ取引先・支払予定日の振込(その他)の登録済支払先
	 * @param torihikisakiCd 取引先コード
	 * @param yoteibi 支払予定日
	 * @param furikomiGinkouCd1 金融機関コード（以下のキーで指定した振込先以外の振込先で
	 * @param furikomiGinkouShitenCd1 金融機関支店コード
	 * @param kouzaShubetsu1 講座種別
	 * @param kouzaBangou1 口座番号
	 * @param kouzaMeiginin1 口座名義人(ｶﾅ)
	 * @param tesuuryou1 手数料負担区分
	 * @param furikomiGinkouCd 金融機関コード（以下のキーで指定した振込先が何番目か
	 * @param furikomiGinkouShitenCd 金融機関支店コード
	 * @param kouzaShubetsu 講座種別
	 * @param kouzaBangou 口座番号
	 * @param kouzaMeiginin 口座名義人(ｶﾅ)
	 * @param tesuuryou 手数料負担区分
	 * @return 件数
	 */
	public int findSonotaFuriNum(
			String torihikisakiCd, Date yoteibi,
			String furikomiGinkouCd1, String furikomiGinkouShitenCd1, String kouzaShubetsu1, String kouzaBangou1, String kouzaMeiginin1, String tesuuryou1,
			String furikomiGinkouCd, String furikomiGinkouShitenCd, String kouzaShubetsu, String kouzaBangou, String kouzaMeiginin, String tesuuryou
	) {
		final String sql = "SELECT row_num FROM"
						+ "(SELECT DISTINCT "
						+ "  ROW_NUMBER() over() row_num "
						+ "  ,s.furikomi_ginkou_cd "
						+ "  ,s.furikomi_ginkou_shiten_cd "
						+ "  ,s.yokin_shubetsu "
						+ "  ,s.kouza_bangou "
						+ "  ,s.kouza_meiginin "
						+ "  ,s.tesuuryou "
						+ "FROM shiharai_irai s "
						+ "JOIN denpyou d ON s.denpyou_id=d.denpyou_id "
						+ "WHERE "
						+ "  s.torihikisaki_cd=?"
						+ "  AND s.shiharai_houhou=? "
						+ "  AND s.shiharai_shubetsu=? "
						+ "  AND s.yoteibi=? "
						+ "  AND NOT( "
						+ "    s.furikomi_ginkou_cd=? "
						+ "    AND s.furikomi_ginkou_shiten_cd=? "
						+ "    AND s.yokin_shubetsu=? "
						+ "    AND s.kouza_bangou=? "
						+ "    AND s.kouza_meiginin=? "
						+ "    AND s.tesuuryou=?) "
						+ "  AND d.denpyou_joutai NOT IN(?,?) "
						+ "ORDER BY "
						+ "  s.furikomi_ginkou_cd "
						+ "  ,s.furikomi_ginkou_shiten_cd "
						+ "  ,s.yokin_shubetsu "
						+ "  ,s.kouza_bangou "
						+ "  ,s.kouza_meiginin "
						+ "  ,s.tesuuryou "
						+ ") tmp "
						+ "WHERE "
						+ "  furikomi_ginkou_cd=? "
						+ "  AND furikomi_ginkou_shiten_cd=? "
						+ "  AND yokin_shubetsu=? "
						+ "  AND kouza_bangou=? "
						+ "  AND kouza_meiginin=? "
						+ "  AND tesuuryou=? ";
		GMap map = connection.find(sql,
				torihikisakiCd, SHIHARAI_IRAI_HOUHOU.FURIKOMI, SHIHARAI_IRAI_SHUBETSU.SONOTA, yoteibi, 
				furikomiGinkouCd1, furikomiGinkouShitenCd1, kouzaShubetsu1, kouzaBangou1, kouzaMeiginin1, tesuuryou1, 
				DENPYOU_JYOUTAI.HININ_ZUMI, DENPYOU_JYOUTAI.TORISAGE_ZUMI, 
				furikomiGinkouCd, furikomiGinkouShitenCd, kouzaShubetsu, kouzaBangou, kouzaMeiginin, tesuuryou);
		return map == null ? 0 : (int)(long)map.get("row_num");
	}
}

