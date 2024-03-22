package eteam.database.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import eteam.base.GMap;
import eteam.database.abstractdao.SuitouchouSettingAbstractDao;

/**
 * 出納帳設定に対するデータ操作クラス
 */
public class SuitouchouSettingDao extends SuitouchouSettingAbstractDao {

	/**
	 * 出納帳設定を取得
	 * @param denpyouKbn 伝票区分
	 * @return 結果リスト
	 */
	public List<GMap> loadSuitouchouSetting(String denpyouKbn){
		final String sql = "SELECT * FROM suitouchou_setting WHERE denpyou_kbn = ? ORDER BY zaimu_kyoten_nyuryoku_pattern_no";
		return connection.load(sql, denpyouKbn);
	}
	
	/**
	 * 出納帳の開始残高を更新する。
	 * @param kaishiKesn 開始内部決算期
	 * @param kaishiZandaka 開始残高
	 * @param kaishiZandakaTorikomiZumiFlg 開始残高取込済フラグ
	 * @param userId ユーザーID
	 * @param denpyouKbn 伝票区分
	 * @param zaimuKyotenNyuryokuPatternNo 財務拠点入力パターン番号
	 * @return 結果
	 */
	public int updateSuitouchouKaishiZandaka(int kaishiKesn, BigDecimal kaishiZandaka, String kaishiZandakaTorikomiZumiFlg, String userId, String denpyouKbn, String zaimuKyotenNyuryokuPatternNo) {
		final String sql = "UPDATE suitouchou_setting SET"
							+ " kaishi_kesn=?, kaishi_zandaka=?, kaishi_zandaka_torikomi_zumi_flg=?, "
							+ " touroku_user_id = ? , touroku_time = current_timestamp"
							+ " WHERE denpyou_kbn = ? AND zaimu_kyoten_nyuryoku_pattern_no = ?";
		return connection.update(sql, kaishiKesn, kaishiZandaka, kaishiZandakaTorikomiZumiFlg, userId, denpyouKbn, zaimuKyotenNyuryokuPatternNo);
	}
	
	/**
	 * 現預金出納帳について、パターンを指定して前日残高を集計・返却する。
	 * 以下条件で算出
	 * ①伝票日付が期首（※）～伝票日付の1日前
	 * ②同一出納帳パターンNoで起票された出納帳
	 * ③伝票状態が起票中または申請中または承認中
	 * （※）
	 *   開始残高取込済みフラグ=0なら、開始年月日
	 *   開始残高取込済みフラグ=1なら、当期期首日
	 * @param patternNo 財務拠点入力パターンNo
	 * @param curDate 指定日付(当日)
	 * @param dgamen 伝票画面ならtrue、現預金確認画面ならfalse
	 * @return 検索結果
	 */
	public BigDecimal findSuitouchcouZenjitsuZandaka(String patternNo, Date curDate ,boolean dgamen) {
		BigDecimal ret = new BigDecimal(0);
		
		String sql = "SELECT l.kaishi_zandaka + COALESCE(r.ngk, 0) - COALESCE(r.sgk, 0) AS zenjitsu_zandaka "
				+ " FROM suitouchou_setting l "
				+ " LEFT OUTER JOIN "
				+ " (SELECT g.zaimu_kyoten_nyuryoku_pattern_no as zp "
				+ "        ,SUM(nyukin_goukei)  AS ngk "
				+ "        ,SUM(shukkin_goukei) AS sgk "
				+ "  FROM suitouchou g "
				+ "  INNER JOIN suitouchou_setting s "
				+ "  ON g.zaimu_kyoten_nyuryoku_pattern_no = s.zaimu_kyoten_nyuryoku_pattern_no "
				+ "  INNER JOIN denpyou d "
				+ "  ON g.denpyou_id = d.denpyou_id "
				+ "  AND (d.denpyou_joutai = '20' OR d.denpyou_joutai = '30') "
				+ "  WHERE s.denpyou_kbn = 'Z002' AND g.zaimu_kyoten_nyuryoku_pattern_no = ? "
				+ "    AND (CASE kaishi_zandaka_torikomi_zumi_flg WHEN '0' THEN kaishi_date ELSE (SELECT MIN(from_date) FROM ki_kesn WHERE kessanki_bangou = '1') END) <= g.denpyou_date ";
		if(dgamen) {
			sql +=  " AND g.denpyou_date < ? ";
		}else {
			sql +=  " AND g.denpyou_date <= ? ";
		}
			sql += "  GROUP BY g.zaimu_kyoten_nyuryoku_pattern_no) r "
				+ " ON l.zaimu_kyoten_nyuryoku_pattern_no = r.zp "
				+ " WHERE l.denpyou_kbn = 'Z002' AND l.zaimu_kyoten_nyuryoku_pattern_no = ? "; 
		GMap mp = connection.find(sql, patternNo, curDate, patternNo);
		if(mp != null) {
			ret = mp.get("zenjitsu_zandaka");
		}
		return ret;
	}
	
	/**
	 * 出納帳設定を取得
	 * @param denpyouKbn 伝票区分
	 * @param zaimuKyotenNyuryokuPatternNo 財務拠点入力パターン番号
	 * @return 結果マップ
	 */
	public GMap findSuitouchouSetting(String denpyouKbn, String zaimuKyotenNyuryokuPatternNo) {
		final String sql = "SELECT * FROM suitouchou_setting WHERE denpyou_kbn = ? AND zaimu_kyoten_nyuryoku_pattern_no = ?";
		return connection.find(sql, denpyouKbn, zaimuKyotenNyuryokuPatternNo);
	}
}
