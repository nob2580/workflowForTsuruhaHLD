package eteam.gyoumu.workflow;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;

/**
 * 部門推奨ルート追加機能Logic
 */
public class BumonSuishouRouteTsuikaLogic extends EteamAbstractLogic {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(BumonSuishouRouteTsuikaLogic.class);
	
	/**
	 * 伝票種別一覧を検索する。データがなければサイズ0。
	 * @return 検索結果
	 */
	public List<GMap> load(){
		//ChuukiMongonSetteiTsuikaLogicと同期
		final String sql = "SELECT "
						 + "  denpyou_kbn, "
						 + "  denpyou_shubetsu, "
						 + "  gyoumu_shubetsu, "
						 + "  route_torihiki_flg, "
						 + "  version "
						 + "FROM denpyou_shubetsu_ichiran d "
						 + "WHERE yuukou_kigen_to >= current_date "
						 + "ORDER BY "
						 + "  (SELECT MIN(hyouji_jun) FROM denpyou_shubetsu_ichiran tmp where tmp.gyoumu_shubetsu = d.gyoumu_shubetsu), "	//(1)同業務種別内で先頭の伝票区分（業務種別がバラけないように）
						 + "  gyoumu_shubetsu, "																							//(2)これは第一オーダーと同じ意味でしかないはず。
						 + "  hyouji_jun "; //(3)同業務種別内でのソート
		return connection.load(sql);
	}
	
	/**
	 * 部門推奨ルートを登録する。
	 * @param denpyouKbn        伝票区分
	 * @param bumonCd           部門コード
	 * @param defaultFlg        デフォルト設定フラグ
	 * @param shiwakeEdaNo      仕訳枝番
	 * @param kingakuFrom       金額FROM
	 * @param kingakuTo         金額TO
	 * @param bumonRole         承認者
	 * @param shoriKengen       処理権限
	 * @param gougiCd           合議コード
	 * @param gougiEdano        合議枝番
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @param userId           登録ユーザーID
	 * @return 登録件数
	 */
	public int insertBumonSuishoRoute(String denpyouKbn, String bumonCd, String defaultFlg, Object[] shiwakeEdaNo,
			BigDecimal kingakuFrom, BigDecimal kingakuTo, String[] bumonRole, String[] shoriKengen, String[] gougiCd, String[] gougiEdano,
			Date yuukouKigenFrom, Date yuukouKigenTo, String userId) {
		
		final String insOyaSql = "INSERT INTO bumon_suishou_route_oya " + 
							  	 "VALUES (?, ?, " + 
							  	 "        (SELECT COALESCE(MAX(edano + 1), 1) FROM bumon_suishou_route_oya WHERE denpyou_kbn = ?)" +
							  	 "        , ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		int retOya = connection.update(insOyaSql, denpyouKbn, bumonCd, denpyouKbn, defaultFlg, (null == shiwakeEdaNo)? null:connection.getIntegerArray(shiwakeEdaNo), kingakuFrom, kingakuTo, yuukouKigenFrom, yuukouKigenTo, userId, userId);
		
		log.info("登録件数(部門推奨ルート親:" + retOya + "件");
		
		final String insKoSql = "INSERT INTO bumon_suishou_route_ko " +
								"VALUES (?, ?, " +
								"       (SELECT MAX(edano) FROM bumon_suishou_route_oya WHERE denpyou_kbn = ?)" +
								"        , ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		// 枝枝番号
		int edaedaNo = 0;
		
		int retKo = 0;
		for(int i = 0; i < bumonRole.length; i++) {
			edaedaNo++;
			retKo = retKo + connection.update(insKoSql, denpyouKbn, bumonCd, denpyouKbn, edaedaNo, bumonRole[i], 
											  !shoriKengen[i].equals("") ? Integer.parseInt(shoriKengen[i]) : null, 
											  !gougiCd[i].equals("") ? Integer.parseInt(gougiCd[i]) : null, 
											  !gougiEdano[i].equals("") ? Integer.parseInt(gougiEdano[i]) : null, 
											  userId, userId);
		}

		log.info("登録件数(部門推奨ルート子:" + retKo + "件");

		return retOya + retKo;
	}
	
	
	
	
	/**
	 * 枝番を指定して部門推奨ルートを登録する。
	 * @param denpyouKbn        伝票区分
	 * @param bumonCd           部門コード
	 * @param edano             指定枝番
	 * @param defaultFlg        デフォルト設定フラグ
	 * @param shiwakeEdaNo      仕訳枝番
	 * @param kingakuFrom       金額FROM
	 * @param kingakuTo         金額TO
	 * @param bumonRole         承認者
	 * @param shoriKengen       処理権限
	 * @param gougiCd           合議コード
	 * @param gougiEdano        合議枝番
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @param userId            登録ユーザーID
	 * @return 登録件数
	 */
	public int insertBumonSuishoRouteWithEdano(String denpyouKbn, String bumonCd, int edano, String defaultFlg, Object[] shiwakeEdaNo,
			BigDecimal kingakuFrom, BigDecimal kingakuTo, String[] bumonRole, String[] shoriKengen, String[] gougiCd, String[] gougiEdano,
			Date yuukouKigenFrom, Date yuukouKigenTo, String userId) {
		
		final String insOyaSql = "INSERT INTO bumon_suishou_route_oya " + 
							  	 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		int retOya = connection.update(insOyaSql, denpyouKbn, bumonCd, edano, defaultFlg, (null == shiwakeEdaNo)? null:connection.getIntegerArray(shiwakeEdaNo), kingakuFrom, kingakuTo, yuukouKigenFrom, yuukouKigenTo, userId, userId);
		
		log.info("登録件数(部門推奨ルート親:" + retOya + "件");
		
		final String insKoSql = "INSERT INTO bumon_suishou_route_ko " +
								"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		// 枝枝番号
		int edaedaNo = 0;
		
		int retKo = 0;
		for(int i = 0; i < bumonRole.length; i++) {
			edaedaNo++;
			retKo = retKo + connection.update(insKoSql, denpyouKbn, bumonCd, edano, edaedaNo, bumonRole[i], 
											  !shoriKengen[i].equals("") ? Integer.parseInt(shoriKengen[i]) : null, 
											  !gougiCd[i].equals("") ? Integer.parseInt(gougiCd[i]) : null, 
											  !gougiEdano[i].equals("") ? Integer.parseInt(gougiEdano[i]) : null, 
											  userId, userId);
		}

		log.info("登録件数(部門推奨ルート子:" + retKo + "件");

		return retOya + retKo;
	}
	
	/**
	 * TODO:承認処理権限取得（移動予定）
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
	 * TODO:承認処理権限Noによる、承認処理権限取得（移動予定）
	 * @param shoriNo 処理権限No
	 * @return 処理権限
	 */
	public GMap findShouninShoriKengen(String shoriNo) {
		final String sql = "SELECT * "
							+ "FROM shounin_shori_kengen "
							+ "WHERE (hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.HYOUJUN + "' OR hanrei_hyouji_cd = '" + EteamNaibuCodeSetting.HANREI_HYOUJI_CD.KYOUTSUU + "') "
							+ "AND shounin_shori_kengen_no = ?";
		
		return connection.find(sql, Integer.parseInt(shoriNo));
	}
	
	/**
	 * @param routeTrihikiflg ルート取引毎設定フラグ
	 * @return true：取引入力可、false:取引入力不可
	 */
	public boolean judgeTorihikiInput(String routeTrihikiflg){
		
		if(EteamConst.routeTorihiki.OK.equals(routeTrihikiflg)){
			return true;
		}else{
			return false;
		}
	}
	
}
