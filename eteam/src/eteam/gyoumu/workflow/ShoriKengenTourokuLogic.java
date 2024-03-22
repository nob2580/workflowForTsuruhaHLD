package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;

/**
 * 合議部署追加機能Logic
 */
public class ShoriKengenTourokuLogic extends EteamAbstractLogic {
	
	/**
	 * 合議部署情報を取得する
	 * @return 検索結果
	 */
	public List<GMap> getShoriKengenList() {
		final String sql = "SELECT * FROM shounin_shori_kengen ORDER BY hyouji_jun";
		return connection.load(sql);
	}
	
	/**
	 * 承認処理権限を更新する
	 * @param shouninShoriKengenNo 承認処理権限番号
	 * @param shouninShoriKengenName 承認処理権限名
	 * @param kihonModelCd 基本モデルコード
	 * @param shouninHissuFlg 承認必須フラグ
	 * @param shouninKenFlg 承認権フラグ
	 * @param henkouFlg 変更フラグ
	 * @param setsumei 説明
	 * @param shouninMongon 承認文言
	 * @param hanreiHyoujiCd 凡例表示コード
	 * @param hyoujiJun 表示順
	 * @param userId ユーザーID
	 * @return 承認処理権限No
	 */
	public int updateShoriKengen(
			  int shouninShoriKengenNo
			, String shouninShoriKengenName
			, String kihonModelCd
			, String shouninHissuFlg
			, String shouninKenFlg
			, String henkouFlg
			, String setsumei
			, String shouninMongon
			, String hanreiHyoujiCd
			, int hyoujiJun
			, String userId
			) {
		
		final String sql = "UPDATE shounin_shori_kengen " + 
						   "   SET shounin_shori_kengen_name = ? " +
						   "      ,kihon_model_cd = ? " +
						   "      ,shounin_hissu_flg = ? " +
						   "      ,shounin_ken_flg = ? " +
						   "      ,henkou_flg = ? " +
						   "      ,setsumei = ? " +
						   "      ,shounin_mongon = ? " +
						   "      ,hanrei_hyouji_cd = ? " +
						   "      ,hyouji_jun = ? " +
						   "      ,koushin_user_id = ? " +
						   "      ,koushin_time = current_timestamp" +
						   " WHERE shounin_shori_kengen_no = ? RETURNING shounin_shori_kengen_no";
		
		GMap ret = connection.find(sql , shouninShoriKengenName, kihonModelCd, shouninHissuFlg, shouninKenFlg, henkouFlg, setsumei, shouninMongon, hanreiHyoujiCd, hyoujiJun, userId, shouninShoriKengenNo);
		return Integer.parseInt(ret.getString("shounin_shori_kengen_no"));
	}
	
	/**
	 * 承認処理権限を削除する
	 * @param list 承認処理権限No
	 * @return 処理結果
	 */
	public int deleteShoriKengen(List<Integer> list) {
		// SQL
		StringBuffer sql = new StringBuffer("DELETE FROM shounin_shori_kengen ");
		// パラメータ
		List<Object> params = new ArrayList<Object>();
		
		if (!list.isEmpty()) {
			sql.append(" WHERE shounin_shori_kengen_no NOT IN ( ");
			for (int i = 0; i < list.size(); i++) {
				if (i != 0) {
					sql.append(", ");
				}
				sql.append("?");
				params.add(Integer.parseInt(String.valueOf(list.get(i))));
			}
			sql.append(")");
		}
		return connection.update(sql.toString(), params.toArray());
	}
	
	/**
	 * 承認処理権限を追加する
	 * @param shouninShoriKengenName 承認処理権限名
	 * @param kihonModelCd 基本モデルコード
	 * @param shouninHissuFlg 承認必須フラグ
	 * @param shouninKenFlg 承認権フラグ
	 * @param henkouFlg 変更フラグ
	 * @param setsumei 説明
	 * @param shouninMongon 承認文言
	 * @param hanreiHyoujiCd 凡例表示コード
	 * @param hyoujiJun 表示順
	 * @param userId ユーザーID
	 * @return 承認処理権限No
	 */
	public int insertShoriKengen(
			  String shouninShoriKengenName
			, String kihonModelCd
			, String shouninHissuFlg
			, String shouninKenFlg
			, String henkouFlg
			, String setsumei
			, String shouninMongon
			, String hanreiHyoujiCd
			, int hyoujiJun
			, String userId
			) {
		final String sql = "INSERT INTO shounin_shori_kengen( "
						+ " shounin_shori_kengen_name, "
						+ " kihon_model_cd, "
						+ " shounin_hissu_flg, "
						+ " shounin_ken_flg, "
						+ " henkou_flg, "
						+ " setsumei, "
						+ " shounin_mongon, "
						+ " hanrei_hyouji_cd, "
						+ " hyouji_jun, "
						+ " touroku_user_id, "
						+ " touroku_time, "
						+ " koushin_user_id, "
						+ " koushin_time ) "
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp) RETURNING shounin_shori_kengen_no";
		GMap ret = connection.find(sql, shouninShoriKengenName, kihonModelCd, shouninHissuFlg, shouninKenFlg, henkouFlg, setsumei, shouninMongon, hanreiHyoujiCd, hyoujiJun, userId, userId);
		return Integer.parseInt(ret.getString("shounin_shori_kengen_no"));
	}
	
	/**
	 * 合議部署に対象の承認処理権限Noがあるか調べる
	 * @param shoriNo 承認処理権限No
	 * @return 合議部署
	 */
	public boolean loadGougiPattern(String shoriNo) {
		final String sql = "SELECT "
						  + "    CASE "
						  + "    WHEN EXISTS ( "
						  + "      SELECT "
						  + "          * "
						  + "      FROM "
						  + "        bumon_suishou_route_ko bsrk "
						  + "      WHERE "
						  + "        bsrk.shounin_shori_kengen_no = ? "
						  + "    ) "
						  + "    THEN TRUE "
						  + "    ELSE FALSE "
						  + "    END; ";
		GMap rel = connection.find(sql, Integer.parseInt(shoriNo));
		return rel.get("case").equals(true);
	}
	
	/**
	 * 部門推奨ルートに対象の承認処理権限Noがあるか調べる
	 * @param shoriNo 承認処理権限No
	 * @return 部門推奨ルート
	 */
	public boolean loadBumonSuishouRoute(String shoriNo) {
		final String sql = "SELECT "
						  + "    CASE "
						  + "    WHEN EXISTS ( "
						  + "      SELECT "
						  + "          * "
						  + "      FROM "
						  + "        gougi_pattern_ko gpk "
						  + "      WHERE "
						  + "        gpk.shounin_shori_kengen_no = ? "
						  + "    ) "
						  + "    THEN TRUE "
						  + "    ELSE FALSE "
						  + "    END; ";
		GMap rel = connection.find(sql, Integer.parseInt(shoriNo));
		return rel.get("case").equals(true);
	}
	
	/**
	 * 有効な承認ルートの合議部署に対象の承認処理権限Noがあるか調べる
	 * @param shoriNo 承認処理権限No
	 * @return 承認ルート合議子
	 */
	public boolean loadShouninRouteGougiKo(String shoriNo) {
		final String sql = "SELECT "
						  + "    CASE "
						  + "    WHEN EXISTS ( "
						  + "      SELECT "
						  + "          * "
						  + "      FROM "
						  + "        shounin_route_gougi_ko srgk "
						  + "        INNER JOIN denpyou d "
						  + "          ON srgk.denpyou_id = d.denpyou_id "
						  + "      WHERE "
						  + "  d.denpyou_joutai IN ('" + EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU + "', '" + EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU + "') "
						  + "        AND srgk.shounin_shori_kengen_no = ? "
						  + "    ) "
						  + "    THEN TRUE "
						  + "    ELSE FALSE "
						  + "    END; ";
		GMap rel = connection.find(sql, Integer.parseInt(shoriNo));
		return rel.get("case").equals(true);
	}
	
	/**
	 * 有効な承認ルートの部門推奨ルートに対象の承認処理権限Noがあるか調べる
	 * @param shoriNo 承認処理権限No
	 * @return boolean
	 */
	public boolean loadShouninRoute(String shoriNo) {
		final String sql = "SELECT "
						  + "    CASE "
						  + "    WHEN EXISTS ( "
						  + "      SELECT "
						  + "          * "
						  + "      FROM "
						  + "        shounin_route sr "
						  + "        INNER JOIN denpyou d "
						  + "          ON sr.denpyou_id = d.denpyou_id "
						  + "      WHERE "
						  + "        d.denpyou_joutai IN ('" + EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU + "', '" + EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU + "') "
						  + "        AND sr.shounin_shori_kengen_no = ? "
						  + "    ) "
						  + "    THEN TRUE "
						  + "    ELSE FALSE "
						  + "    END; ";
		GMap rel = connection.find(sql, Integer.parseInt(shoriNo));
		return rel.get("case").equals(true);
	}
}
