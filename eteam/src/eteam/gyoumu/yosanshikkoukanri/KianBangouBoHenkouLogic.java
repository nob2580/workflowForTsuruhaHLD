package eteam.gyoumu.yosanshikkoukanri;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;

/**
 * 起案番号簿変更Logic
 */
public class KianBangouBoHenkouLogic extends EteamAbstractLogic {

	/**
	 * 起案番号簿データ取得<br>
	 * 起案番号簿をプライマリーキーで検索する。
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianbangouFrom 開始起案番号
	 * @return 検索結果Map
	 */
	public GMap findKianbangouBoByPk(String bumonCd, String nendo, String ryakugou, Integer kianbangouFrom){

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("   A.*")
				 .append("  ,CASE WHEN B.bumon_name IS NULL THEN '(削除)' ELSE B.bumon_name END ")
				 .append(" FROM kian_bangou_bo AS A")
				 .append(" LEFT OUTER JOIN shozoku_bumon AS B ON B.bumon_cd = A.bumon_cd")
				 .append(" WHERE")
				 .append("  A.bumon_cd = ?")
				 .append("  AND A.nendo = ?")
				 .append("  AND A.ryakugou = ?")
				 .append("  AND A.kian_bangou_from = ?")
				 .append("  AND CURRENT_DATE BETWEEN B.yuukou_kigen_from AND B.yuukou_kigen_to");

		// 検索を実施する。
		GMap mapResult = connection.find(sbMainSql.toString(), bumonCd, nendo, ryakugou, kianbangouFrom);
		return mapResult;
	}

	/**
	 * 起案番号簿選択表示デフォルトレコード取得<br>
	 * 指定された所属部門および起案番号簿選択表示フラグがデフォルト指定されたレコードを取得する。
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kian_bangou_from 開始起案番号
	 * @param isLock レコードロックフラグ(true:ロックする false:ロックしない)
	 * @return 検索結果リスト
	 */
	public List<GMap> findKianbangouBoByDefault(String bumonCd, String nendo, String ryakugou, int kian_bangou_from, boolean isLock){

		// MainSQL（自分自身を除く）
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("  A.*")
				 .append(" FROM kian_bangou_bo AS A")
				 .append(" WHERE (A.bumon_cd, A.nendo, A.ryakugou, A.kian_bangou_from) IN (")
				 .append("  SELECT AA.bumon_cd, AA.nendo, AA.ryakugou, AA.kian_bangou_from")
				 .append("  FROM kian_bangou_bo AS AA")
				 .append("  WHERE AA.bumon_cd = ? AND (AA.nendo != ? OR AA.ryakugou != ? OR AA.kian_bangou_from != ?)")
				 .append(" )")
				 .append(" AND A.kianbangou_bo_sentaku_hyouji_flg = ?");
		if (isLock){
			sbMainSql.append(" FOR UPDATE");
		}

		// 検索を実施する。
		List<GMap> lstResult = connection.load(sbMainSql.toString(), bumonCd, nendo, ryakugou, kian_bangou_from, EteamNaibuCodeSetting.KIAN_BANGOU_BO_SENTAKU_HYOUJI.DEFAULT);
		return lstResult;
	}

	/**
	 * 起案番号簿更新<br>
	 * 以下のカラムについてデータを更新する。
	 * <ul>
	 * <li>起案番号簿選択表示フラグ</li>
	 * <li>伝票検索表示フラグ</li>
	 * </ul>
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianBangouFrom 開始起案番号
	 * @param kianHyoujiFlg 起案番号簿選択表示フラグ
	 * @param denpyouHyoujiFlg 伝票検索表示フラグ
	 * @return 登録件数
	 */
	public int updateKianbangouBoForFlg(String bumonCd, String nendo, String ryakugou, int kianBangouFrom, String kianHyoujiFlg, String denpyouHyoujiFlg) {

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("UPDATE kian_bangou_bo SET")
				 .append("  kianbangou_bo_sentaku_hyouji_flg = ?")
				 .append(" ,denpyou_kensaku_hyouji_flg = ?")
				 .append(" WHERE")
				 .append("  bumon_cd = ?")
				 .append("  AND nendo = ?")
				 .append("  AND ryakugou = ?")
				 .append("  AND kian_bangou_from = ?");


		// レコードを更新する。
		int cntResult = connection.update(sbMainSql.toString(), kianHyoujiFlg, denpyouHyoujiFlg, bumonCd, nendo, ryakugou, kianBangouFrom);
		return cntResult;
	}

	/**
	 * 起案番号採番データ取得<br>
	 * 起案番号採番をプライマリーキーで検索する。
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianbangouFrom 開始起案番号
	 * @return 検索結果Map
	 */
	public GMap findKianbangouSaibanByPk(String bumonCd, String nendo, String ryakugou, Integer kianbangouFrom){

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("   A.*")
				 .append("  ,CASE WHEN B.bumon_name IS NULL THEN '(削除)' ELSE B.bumon_name END ")
				 .append(" FROM kian_bangou_saiban AS A")
				 .append(" LEFT OUTER JOIN shozoku_bumon AS B ON B.bumon_cd = A.bumon_cd")
				 .append(" WHERE")
				 .append("  A.bumon_cd = ?")
				 .append("  AND A.nendo = ?")
				 .append("  AND A.ryakugou = ?")
				 .append("  AND A.kian_bangou_from = ?")
				 .append("  AND CURRENT_DATE BETWEEN B.yuukou_kigen_from AND B.yuukou_kigen_to");

		// 検索を実施する。
		GMap mapResult = connection.find(sbMainSql.toString(), bumonCd, nendo, ryakugou, kianbangouFrom);
		return mapResult;
	}

	/**
	 * 起案番号採番レコードロック<br>
	 * 起案番号採番をプライマリーキーでレコードロックする。
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianbangouFrom 開始起案番号
	 * @return 検索結果Map
	 */
	public GMap selectKianbangouSaibanForUpdate(String bumonCd, String nendo, String ryakugou, Integer kianbangouFrom){
		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT * FROM kian_bangou_saiban")
				 .append(" WHERE bumon_cd = ? AND nendo = ? AND ryakugou = ? AND kian_bangou_from = ?")
				 .append(" FOR UPDATE");
		return connection.find(sbMainSql.toString(), bumonCd, nendo, ryakugou, kianbangouFrom);
	}

	/**
	 * 最終起案番号登録<br>
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianbangouFrom 開始起案番号
	 * @param kianbangouLast 最終起案番号
	 * @return 登録件数
	 */
	public int insertKianbangouSaiban(String bumonCd, String nendo, String ryakugou, Integer kianbangouFrom, Integer kianbangouLast) {

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("INSERT INTO kian_bangou_saiban")
				 .append(" VALUES (?, ?, ?, ?, ?)");


		// レコードを登録する。
		int cntResult = connection.update(sbMainSql.toString(), bumonCd, nendo, ryakugou, kianbangouFrom, kianbangouLast);
		return cntResult;
	}

	/**
	 * 最終起案番号更新<br>
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianbangouFrom 開始起案番号
	 * @param kianbangouLast 最終起案番号
	 * @return 登録件数
	 */
	public int updateKianbangouSaiban(String bumonCd, String nendo, String ryakugou, Integer kianbangouFrom, Integer kianbangouLast) {

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("UPDATE kian_bangou_saiban SET")
				 .append("  kian_bangou_last = ?")
				 .append(" WHERE")
				 .append("  bumon_cd = ?")
				 .append("  AND nendo = ?")
				 .append("  AND ryakugou = ?")
				 .append("  AND kian_bangou_from = ?");


		// レコードを更新する。
		int cntResult = connection.update(sbMainSql.toString(), kianbangouLast, bumonCd, nendo, ryakugou, kianbangouFrom);
		return cntResult;
	}
}
