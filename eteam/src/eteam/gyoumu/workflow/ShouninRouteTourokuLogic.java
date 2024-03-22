package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamSettingInfo;
import eteam.common.select.WorkflowCategoryLogic;

/**
 * 承認ルート登録機能Logic
 */
public class ShouninRouteTourokuLogic extends EteamAbstractLogic {
	/**
	 * 承認ルートを追加する
	 * @param denpyou_id         伝票ID
	 * @param edano              枝番号
	 * @param user_id            ユーザーID
	 * @param user_full_name     ユーザーフル名
	 * @param bumon_cd           部門コード
	 * @param bumon_full_name    部門フル名
	 * @param bumon_role_id      部門ロールID
	 * @param bumon_role_name    部門ロール名
	 * @param gyoumu_role_id     業務ロールID
	 * @param gyoumu_role_name   業務ロール名
	 * @param genzai_flg         現在フラグ
	 * @param saishu_shounin_flg 最終承認フラグ
	 * @param shouninShoriKengenNo 承認処理権限番号
	 * @param login_user_id     ログインユーザーID
	 * @return 結果
	 */
	public int insertShouninRoute(String denpyou_id, int edano, String user_id, String user_full_name, String bumon_cd, String bumon_full_name, String bumon_role_id, String bumon_role_name, String gyoumu_role_id, String gyoumu_role_name, String genzai_flg, String saishu_shounin_flg, Long shouninShoriKengenNo, String login_user_id) {
		WorkflowCategoryLogic wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		
		GMap k = null;
		if(shouninShoriKengenNo != null) k = wfLogic.findShouninShoriKengen(shouninShoriKengenNo);
		
		final String sql = "INSERT INTO shounin_route VALUES( "
						+ "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
						+ "  ?, ?, ?, ?, ?, ?, "
						+ "  ?, current_timestamp, ?, current_timestamp) ";
		return connection.update(sql, denpyou_id, edano, user_id, user_full_name, bumon_cd, bumon_full_name, bumon_role_id, bumon_role_name, gyoumu_role_id, gyoumu_role_name, genzai_flg, saishu_shounin_flg, null,
				shouninShoriKengenNo, k==null ? "" : k.get("shounin_shori_kengen_name"), k==null ? "" : k.get("kihon_model_cd"), k==null ? "" : k.get("shounin_hissu_flg"), k==null ? "" : k.get("shounin_ken_flg"), k==null ? "" : k.get("henkou_flg"),
				login_user_id, login_user_id);
	}
	
	/**
	 * 承認ルートのレコードを削除する
	 * 指定枝番号以降のレコードを消す。
	 * @param denpyou_id 伝票ID
	 * @param edano 枝番号
	 * @return 結果
	 */
	public int deleteShouninRoute(String denpyou_id, int edano) {
		return connection.update("DELETE FROM shounin_route WHERE denpyou_id = ? AND edano >= ?", denpyou_id, edano);
	}

	/**
	 * 承認ルート変更フラグを更新
	 * @param denpyouId 伝票ID
	 * @param flg 承認ルート変更フラグ
	 */
	public void updateShouninRouteHenkouFlg(String denpyouId, boolean flg) {
		final String sql = "update denpyou SET shounin_route_henkou_flg = ? WHERE denpyou_id = ?";
		connection.update(sql, flg ? "1" : "0", denpyouId);
	}
	
	/**
	 * 処理権限名を更新
	 * @param denpyouId         伝票ID
	 * @param edano             枝番号
	 * @param shorikengen       処理権限名
	 */
	public void updateShouninRouteShorikengenName(String denpyouId, int edano, String shorikengen){
		final String sql = "update shounin_route SET shounin_shori_kengen_name = ? WHERE denpyou_id = ? AND edano = ?";
		connection.update(sql, shorikengen, denpyouId, edano);
	}
	
	/**
	 * 承認ルート合議親を追加する
	 * @param denpyou_id         伝票ID
	 * @param edano              枝番号
	 * @param gougi_edano        合議枝番号
	 * @param gougi_pattern_no   合議パターン番号
	 * @param gougi_name         合議名
	 * @param syori_cd           処理コード
	 * @return 結果
	 */
	public int insertShouninRouteGougiOya(String denpyou_id, int edano, int gougi_edano, Long gougi_pattern_no, String gougi_name, String syori_cd) {
		final String sql = "INSERT INTO shounin_route_gougi_oya VALUES(?, ?, ?, ?, ?, ?)";
		return connection.update(sql, denpyou_id, edano, gougi_edano, gougi_pattern_no, gougi_name, syori_cd);
	}
	
	/**
	 * 承認ルート合議親のレコードを削除する
	 * 指定枝番号以降のレコードを消す。
	 * @param denpyou_id 伝票ID
	 * @param edano 枝番号
	 * @return 結果
	 */
	public int deleteShouninRouteGougiOya(String denpyou_id, int edano) {
		return connection.update("DELETE FROM shounin_route_gougi_oya WHERE denpyou_id = ? AND edano >= ?", denpyou_id, edano);
	}
	
	
	/**
	 * 承認ルート合議子を追加する
	 * @param denpyou_id                伝票ID
	 * @param edano                     枝番号
	 * @param gougi_edano               合議枝番号
	 * @param gougi_edaedano            合議枝々番号
	 * @param user_id                   ユーザーID
	 * @param user_full_name            ユーザー名
	 * @param bumon_cd                  部門コード
	 * @param bumon_full_name           部門フル名
	 * @param bumon_role_id             部門ロールID
	 * @param bumon_role_name           部門ロール名
	 * @param gougi_genzai_flg          合議現在フラグ
	 * @param shounin_shori_kengen_no   承認処理権限番号
	 * @param shounin_ninzuu_cd         承認人数コード
	 * @param shounin_ninzuu_hiritsu    承認人数比率
	 * @param syori_cd                  処理コード
	 * @param gouginai_group            合議部署内グループ
	 * @param gougi_joukyou_edano       合議承認状況枝番
	 * @return 結果
	 */
	public int insertShouninRouteGougiKo(String denpyou_id, 
										 int      edano,
										 int      gougi_edano,
										 int      gougi_edaedano,
										 String  user_id,
										 String  user_full_name,
										 String  bumon_cd,
										 String  bumon_full_name,
										 String  bumon_role_id,
										 String  bumon_role_name,
										 String  gougi_genzai_flg,
										 Long    shounin_shori_kengen_no,
										 String  shounin_ninzuu_cd,
										 Integer shounin_ninzuu_hiritsu,
										 String  syori_cd,
										 int      gouginai_group,
										 Integer gougi_joukyou_edano)
										 {
		WorkflowCategoryLogic wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		
		GMap k = null;
		if(shounin_shori_kengen_no != null) k = wfLogic.findShouninShoriKengen(shounin_shori_kengen_no);
		
		final String sql = "INSERT INTO shounin_route_gougi_ko VALUES( "
						+ "  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
						+ "  ?, ?, ?, ?, ?, ?, ?, ?, "
						+ "  ?, ?, ?) ";
		return connection.update(sql, denpyou_id, edano, gougi_edano, gougi_edaedano, user_id, user_full_name, bumon_cd, bumon_full_name, bumon_role_id, bumon_role_name, gougi_genzai_flg,
				shounin_shori_kengen_no, k==null ? "" : k.get("shounin_shori_kengen_name"), k==null ? "" : k.get("kihon_model_cd"), k==null ? "" : k.get("shounin_hissu_flg"), k==null ? "" : k.get("shounin_ken_flg"), k==null ? "" : k.get("henkou_flg"),
				shounin_ninzuu_cd, shounin_ninzuu_hiritsu, syori_cd, gouginai_group, gougi_joukyou_edano);
	}
	
	/**
	 * 承認ルート合議子のレコードを削除する
	 * 指定枝番号以降のレコードを消す。
	 * @param denpyou_id 伝票ID
	 * @param edano 枝番号
	 * @return 結果
	 */
	public int deleteShouninRouteGougiKo(String denpyou_id, int edano) {
		return connection.update("DELETE FROM shounin_route_gougi_ko WHERE denpyou_id = ? AND edano >= ?", denpyou_id, edano);
	}
	
	
//TODO 共通部に承認処理権限取得処理ができたらそれを使用する
	/**
	 * 承認処理権限取得（仮処理）
	 * @return 処理権限リスト
	 */
	public List<GMap> loadShouninShoriKengen() {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.HYOUJUN + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "' "
							+ "ORDER BY hyouji_jun";
		
		return connection.load(sql);
	}
	
	/**
	 * 承認処理権限取得（合議）
	 * @return 処理権限リスト
	 */
	public List<GMap> loadShouninShoriKengenGougi() {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.GOUGI + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "' "
							+ "ORDER BY hyouji_jun";
		
		return connection.load(sql);
	}
	
	/**
	 * 明細テーブルから仕訳枝番番号を取得
	 * @param table テーブル名
	 * @param colum カラム名
	 * @param denpyouId 伝票ID
	 * @return 仕訳枝番号
	 */
	public String getMeisaiShiwakeEdaNo(String table, String colum, String denpyouId){
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT " + colum + " AS shiwake_edano FROM " + table);
		sql.append(" WHERE denpyou_id = ?");
		params.add(denpyouId);
		
		String torihikiSentakuRule = EteamSettingInfo.getSettingInfo("route_hantei_torihiki_sentaku_rule");
		if(torihikiSentakuRule.equals(EteamConst.torihikiSentakuRule.KINGAKU_MAX)){
			//金額が最大値の明細
			sql.append(" AND denpyou_edano = (SELECT denpyou_edano FROM " + table + " WHERE denpyou_id = ? ORDER BY shiharai_kingaku DESC, denpyou_edano limit 1)");
			params.add(denpyouId);
		}else{
			//先頭の明細
			sql.append(" AND denpyou_edano = 1");
		}
		
		GMap ret = connection.find(sql.toString(), params.toArray());
		return (null==ret)? null : ret.get("shiwake_edano").toString();
	}
	
	/**
	 * 申請本体テーブルから仕訳枝番番号を取得
	 * @param table テーブル名
	 * @param colum カラム名
	 * @param denpyouId 伝票ID
	 * @return 仕訳枝番号
	 */
	public String getShiwakeEdaNo(String table, String colum, String denpyouId){
		
		String sql = "SELECT " + colum + " AS shiwake_edano FROM " + table + " WHERE denpyou_id = ?";
		GMap ret = connection.find(sql, denpyouId);
		return (null==ret || null==ret.get("shiwake_edano"))? null : ret.get("shiwake_edano").toString();
	}
}
