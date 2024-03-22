package eteam.gyoumu.workflow;

import java.sql.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamLogger;
import eteam.base.GMap;

/**
 * 最終承認ルート追加機能Logic
 */
public class ChuukiMongonSetteiTsuikaLogic extends EteamAbstractLogic {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(ChuukiMongonSetteiTsuikaLogic.class);
	
	/**
	 * 伝票種別一覧を検索する。データがなければサイズ0。
	 * @return 検索結果
	 */
	public List<GMap> load(){
		//BumonSuishouRouteTsuikaLogicと同期
		final String sql = "SELECT "
				 + "  denpyou_kbn, "
				 + "  denpyou_shubetsu, "
				 + "  gyoumu_shubetsu "
				 + "FROM denpyou_shubetsu_ichiran d "
				 + "WHERE yuukou_kigen_to >= current_date "
				 + "ORDER BY "
				 + "  (SELECT MIN(hyouji_jun) FROM denpyou_shubetsu_ichiran tmp where tmp.gyoumu_shubetsu = d.gyoumu_shubetsu), "	//(1)同業務種別内で先頭の伝票区分（業務種別がバラけないように）
				 + "  gyoumu_shubetsu, "																							//(2)これは第一オーダーと同じ意味でしかないはず。
				 + "  hyouji_jun "; 
		return connection.load(sql);
	}
	
	/**
	 * 最終承認ルートに重複レコードがないかチェックする
	 * @param denpyouKbn        伝票区分
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @return 検索結果
	 */
	public List<GMap> checkDuplicate(String denpyouKbn, Date yuukouKigenFrom, Date yuukouKigenTo) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(denpyou_kbn) AS cnt ");
		sql.append("  FROM saishuu_syounin_route_oya ");
		sql.append(" WHERE denpyou_kbn = ? ");
		sql.append("   AND (NOT(? < yuukou_kigen_from OR yuukou_kigen_to < ? )) ");

		return connection.load(sql.toString(), denpyouKbn, yuukouKigenTo, yuukouKigenFrom);
	}
	
	/**
	 * 最終承認ルートを登録する。
	 * @param denpyouKbn        伝票区分
	 * @param gyoumuRoleId         承認者
	 * @param shorikengen       最終承認処理権限名
	 * @param chukiMongon       注記文言
	 * @param yuukouKigenFrom   有効期限FROM
	 * @param yuukouKigenTo     有効期限TO
	 * @param userId           登録ユーザーID
	 * @return 登録件数
	 */
	public int insertSaishuuShouninRoute(String denpyouKbn, String[] gyoumuRoleId, String[] shorikengen, String chukiMongon,
			Date yuukouKigenFrom, Date yuukouKigenTo, String userId) {

		final String insOyaSql = "INSERT INTO saishuu_syounin_route_oya " + 
							  	 "VALUES (?, " +
							  	 		"(SELECT COALESCE(MAX(edano + 1), 1) FROM saishuu_syounin_route_oya WHERE denpyou_kbn = ?)" +
							  	        ", ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		int retOya = connection.update(insOyaSql, denpyouKbn, denpyouKbn, chukiMongon, yuukouKigenFrom, yuukouKigenTo, userId, userId);
		
		log.info("登録件数(最終承認ルート親:" + retOya + "件");
		
		final String insKoSql = "INSERT INTO saishuu_syounin_route_ko " +
								"VALUES (?, " +
										"(SELECT MAX(edano) FROM saishuu_syounin_route_oya WHERE denpyou_kbn = ?)" +
										", ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		
		// 枝番号
		int edaEdaNo = 0;
		
		int retKo = 0;
		for(int i = 0; i < gyoumuRoleId.length ; i++) {
			String id = gyoumuRoleId[i];
			
			if (!id.isEmpty()) {

				edaEdaNo++;
				retKo = retKo + connection.update(insKoSql, denpyouKbn, denpyouKbn, edaEdaNo, id, shorikengen[i], userId, userId);
			}
		}

		log.info("登録件数(最終承認ルート子:" + retKo + "件");

		return retOya + retKo;
	}
}
