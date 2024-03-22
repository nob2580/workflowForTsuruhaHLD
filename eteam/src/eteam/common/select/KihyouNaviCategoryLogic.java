package eteam.common.select;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU;

/**
 * 起票ナビカテゴリー内のSelect文を集約したLogic
 */
public class KihyouNaviCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* 伝票種別一覧(denpyou_shubetsu_ichiran) */
	/**
	 * 有効な伝票種別を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadShinkiKihyouIchiran(){
		
		final String sql = "SELECT * FROM denpyou_shubetsu_ichiran" +
				" WHERE current_date between yuukou_kigen_from and yuukou_kigen_to" +
				" ORDER BY hyouji_jun ";

		return connection.load(sql);
	}

	/**
	 * 有効な伝票種別を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadDenpyouKanri(){
		final String sql = "SELECT * " +
							" FROM denpyou_shubetsu_ichiran" +
							" ORDER BY hyouji_jun";

		return connection.load(sql);
	}

	/**
	 * 関連伝票選択対象の伝票種別を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadKanrenDenpyouKanri(){

		final String sql = "SELECT * FROM denpyou_shubetsu_ichiran " +
				" WHERE kanren_sentaku_flg = '1'" +
				" ORDER BY hyouji_jun ";

		return connection.load(sql);
	}

	/**
	 * 有効な伝票種別を取得する。
	 * @param denpyouKbn 伝票区分
	 * @return 検索結果
	 */
	public GMap findDenpyouKanri(String denpyouKbn ){

		final String sql = "SELECT * FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn=?";

		return connection.find(sql,denpyouKbn);
	}
	
	/**
	 * 伝票種別が金額分岐対象かどうかを判定する
	 * @param denpyouKbn 伝票区分
	 * @return 金額分岐対象である
	 */
	public boolean routeBunkiAri(String denpyouKbn){
		if(denpyouKbn.startsWith("B")){
			GMap d = findDenpyouKanri(denpyouKbn);
			return ! d.get("route_hantei_kingaku").equals(ROUTE_HANTEI_KINGAKU.BUNKI_NASHI);
		}
		return true;
	}

	/**
	 * 有効な伝票種別を取得する。
	 * @param denpyouKbn 伝票区分
	 * @return 検索結果
	 */
	public GMap findValidDenpyouKanri(String denpyouKbn ){

		final String sql = "SELECT * FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn = ? AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_to ";

		return connection.find(sql,denpyouKbn);
	}
	
	
/* 事象伝票区分リンク(jishou_dpkbn_kanren) */
	/**
	 * 事象IDに関連する有効な伝票種別を取得する。
	 * @param jishouId 事象ID
	 * @return 検索結果
	 */
	public List<GMap> loadShinkiKihyouIchiran(String jishouId){

		final String sql = "SELECT * FROM jishou_dpkbn_kanren a, denpyou_shubetsu_ichiran b" +
				" WHERE a.jishou_id=? AND a.denpyou_kbn=b.denpyou_kbn" +
				" AND current_date between b.yuukou_kigen_from and b.yuukou_kigen_to" +
				" ORDER BY a.hyouji_jun";
		
		return connection.load(sql, Integer.parseInt(jishouId));

	}
	/**
	 * 指定した事象IDの伝票情報を取得する。
	 * @param jishouId 事象ID
	 * @return 検索結果
	 */
	public List<GMap> loadDenpyouInfo(Long jishouId){
		final String sql = "SELECT a.denpyou_kbn, b.denpyou_shubetsu "
						 + "FROM jishou_dpkbn_kanren a "
						 + "INNER JOIN denpyou_shubetsu_ichiran b ON "
						 + "  a.denpyou_kbn = b.denpyou_kbn "
						 + "WHERE a.jishou_id = ? "
						 + "ORDER BY a.hyouji_jun ";
		return connection.load(sql,jishouId);
	}
	
/* 見出し(midashi) */
	/**
	 * ガイダンス起票一覧のメニューに表示する見出し、事象一覧を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadGuidanceKihyouIchiran(){
		final String sql = "SELECT m.midashi_id, m.midashi_name, j.jishou_id, j.jishou_name "
						 + "FROM midashi m "
						 + "INNER JOIN jishou j ON m.midashi_id = j.midashi_id "
						 + "ORDER BY m.hyouji_jun, j.hyouji_jun";
		return connection.load(sql);
	}

	/**
	 * 見出し一覧を取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadMidashi(){
		final String sql = "SELECT * FROM midashi ORDER BY hyouji_jun";
		return connection.load(sql);
	}

	/**
	 * 見出しIDから見出し情報を取得する。
	 * @param midashiId 見出しID
	 * @return 検索結果
	 */
	public GMap findMidashi(Long midashiId){
		final String sql = "SELECT * FROM midashi WHERE midashi_id=?";
		return connection.find(sql,midashiId);
	}
	
/* 事象(jishou) */
	/**
	 * 事象一覧を取得する。
	 * @param midashiId 見出しID
	 * @return 検索結果
	 */
	public List<GMap> loadJishou(Long midashiId){
		final String sql = "SELECT * FROM jishou WHERE midashi_id=? ORDER BY hyouji_jun";
		return connection.load(sql,midashiId);
	}

	/**
	 * 事象IDから事象情報を取得する。
	 * @param jishouId 事象ID
	 * @return 検索結果
	 */
	public GMap findJishou(Long jishouId){
		final String sql = "SELECT * FROM jishou WHERE jishou_id=?";
		return connection.find(sql,jishouId);
	}
	
/* お気に入り起票(bookmark) */
	/**
	 * お気に入りに登録してある有効な伝票を取得する。
	 * @param userId ユーザーID
	 * @return 検索結果
	 */
	public List<GMap> loadOkiniiriKihyou(String userId){
		
		final String sql = "SELECT * FROM bookmark a "
						 + "INNER JOIN denpyou_shubetsu_ichiran b ON a.denpyou_kbn=b.denpyou_kbn "
						 + "WHERE a.user_id=? "
						 + "AND current_date between b.yuukou_kigen_from and b.yuukou_kigen_to "
						 + "ORDER BY a.hyouji_jun";
		return connection.load(sql,userId);
		
	}

	/**
	 * お気に入りに登録してある全ての伝票を取得する。
	 * @param userId ユーザーID
	 * @return 検索結果
	 */
	public List<GMap> loadOkiniiri(String userId){
		
		final String sql = "SELECT a.hyouji_jun, a.denpyou_kbn, a.memo, "
						 + "b.gyoumu_shubetsu, b.denpyou_shubetsu, b.yuukou_kigen_from, b.yuukou_kigen_to FROM bookmark a "
						 + "INNER JOIN denpyou_shubetsu_ichiran b ON a.denpyou_kbn=b.denpyou_kbn "
						 + "WHERE a.user_id=? ORDER BY a.hyouji_jun";
		
		return connection.load(sql,userId);
		
	}

}
