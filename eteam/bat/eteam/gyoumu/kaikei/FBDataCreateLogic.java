package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.gyoumu.kaikei.common.FBData;

/**
 * FBデータ作成ロジック
 * @author takahashi_ryousuke
 *
 */
public class FBDataCreateLogic extends EteamAbstractLogic  {

	
	/**
	 * 個人精算支払日を取得する
	 * @return 日付コントロールデータリスト
	 */
	public List<GMap> loadHizukeControl() {
		final String sql =
				" SELECT fb_data_sakuseibi_flg, "
			  + "        kojinseisan_shiharaibi "
			  + "   FROM hizuke_control "
			  + "  WHERE toujitsu_kbn_flg = '1' ";
		return connection.load(sql);
	}
	
	/**
	 * ヘッダレコード(振込元)を取得する
	 * @param kojinSeisanShiraibi 個人精算支払日
	 * @return FB抽出ヘッダレコードリスト
	 */
	public List<GMap> loadHeadRecord(Date kojinSeisanShiraibi) {
		
		final String sql =
				  "      SELECT "
				+ "             l.moto_kinyuukikan_cd AS moto_kinyuukikan_cd, "
				+ "             l.moto_kinyuukikan_shiten_cd AS moto_kinyuukikan_shiten_cd, "
				+ "             l.moto_yokin_shumoku_cd AS moto_yokin_shumoku_cd, "
				+ "             l.moto_kouza_bangou AS moto_kouza_bangou, "
				+ "             r.shubetsu_cd AS shubetsu_cd, "
				+ "             r.cd_kbn AS cd_kbn, "
				+ "             r.kaisha_cd AS kaisha_cd, "
				+ "             r.kaisha_name_hankana AS kaisha_name_hankana, "
				+ "             r.furikomi_date AS furikomi_date, "
				+ "             r.moto_kinyuukikan_name_hankana AS moto_kinyuukikan_name_hankana, "
				+ "             r.moto_kinyuukikan_shiten_name_hankana AS moto_kinyuukikan_shiten_name_hankana "
				+ "FROM (SELECT moto_kinyuukikan_cd, "
				+ "             moto_kinyuukikan_shiten_cd, "
				+ "             moto_yokin_shumoku_cd, "
				+ "             moto_kouza_bangou, "
				+ "             max(serial_no) AS serial_no "
				+ "        FROM fb "
				+ "       WHERE furikomi_date = ? "
				+ "    GROUP BY moto_kinyuukikan_cd, "
				+ "             moto_kinyuukikan_shiten_cd, "
				+ "             moto_yokin_shumoku_cd, "
				+ "             moto_kouza_bangou) AS l "
				+ "  INNER JOIN fb AS r ON "
				+ "             l.serial_no = r.serial_no "
				+ "    ORDER BY "
				+ "             l.moto_kinyuukikan_cd, "
				+ "             l.moto_kinyuukikan_shiten_cd, "
				+ "             l.moto_yokin_shumoku_cd, "
				+ "             l.moto_kouza_bangou ";
		return connection.load(sql, kojinSeisanShiraibi);
	}
	
	
	/**
	 * データレコードを取得する
	 * @param fbData FBデータ(ヘッダレコード)
	 * @return FB抽出データレコードリスト
	 */
	public List<GMap> loadDataRecord(FBData fbData) {
		
		final String sql = 
				  "     SELECT "
				+ "             l.saki_kinyuukikan_cd AS saki_kinyuukikan_cd, "
			    + "             l.saki_kinyuukikan_shiten_cd AS saki_kinyuukikan_shiten_cd, "
				+ "             l.saki_yokin_shumoku_cd AS saki_yokin_shumoku_cd, "
				+ "             l.saki_kouza_bangou AS saki_kouza_bangou, "
				+ "             l.kingaku AS kingaku, "
				+ "             r.saki_kinyuukikan_name_hankana AS saki_kinyuukikan_name_hankana, "
				+ "             r.saki_kinyuukikan_shiten_name_hankana AS saki_kinyuukikan_shiten_name_hankana, "
				+ "             r.saki_kouza_meigi_kana AS saki_kouza_meigi_kana, "
				+ "             r.shinki_cd AS shinki_cd, "
				+ "             r.kokyaku_cd1 AS kokyaku_cd1, "
				+ "             r.furikomi_kbn AS furikomi_kbn, "
				+ "             r.denpyou_id AS denpyou_id "
				+ "FROM (SELECT saki_kinyuukikan_cd, "
				+ "             saki_kinyuukikan_shiten_cd, "
				+ "             saki_yokin_shumoku_cd, "
				+ "             saki_kouza_bangou, "
				+ "             SUM(kingaku) AS kingaku, "
				+ "             MAX(serial_no) AS serial_no "
				+ "        FROM fb "
				+ "       WHERE furikomi_date = ? "
				+ "         AND moto_kinyuukikan_cd = ? "
				+ "         AND moto_kinyuukikan_shiten_cd = ? "
				+ "         AND moto_yokin_shumoku_cd = ? "
				+ "         AND moto_kouza_bangou = ? "
				+ "    GROUP BY saki_kinyuukikan_cd, "
				+ "             saki_kinyuukikan_shiten_cd, "
				+ "             saki_yokin_shumoku_cd, "
				+ "             saki_kouza_bangou) AS l "
				+ "  INNER JOIN fb AS r ON "
				+ "             l.serial_no = r.serial_no "
				+ "    ORDER BY l.saki_kinyuukikan_cd, "
				+ "             l.saki_kinyuukikan_shiten_cd, "
				+ "             l.saki_yokin_shumoku_cd, "
				+ "             l.saki_kouza_bangou";
		return connection.load(sql,
				fbData.getFurikomiDate(), fbData.getMotoKinyuukikanCd(), fbData.getMotoKinyuukikanShitenCd(),
				fbData.getMotoYokinShumokuCd(), fbData.getMotoKouzaBangou());
	}
	
	/**
	 * トレーラレコードを取得する
	 * @param dataList FB抽出データレコードリスト
	 * @return FB抽出データトレーラレコード
	 */
	public GMap makeTrailerRecord(List<GMap> dataList){
		
		// データ件数のカウントと合計金額の算出
		int count = 0;
		BigDecimal kingaku = new BigDecimal("0");
		Iterator<GMap> dataListIt = dataList.iterator();
		while(dataListIt.hasNext()){
			GMap data = dataListIt.next();
			kingaku = kingaku.add(new BigDecimal(data.getString("kingaku")));
			count++;
		}

		//トレーラレコードの要素を設定
		GMap map = new GMap();
		map.put("goukei_kensuu", count); // 合計件数
		map.put("goukei_kingaku", kingaku); // 合計金額
		return map;
	}

	/**
	 * 「FBデータ作成」の承認状況を作成
	 * @param kojinSeisanShiraibi 個人精算支払日
	 * @return 登録件数
	 */
	public int insertFBDataSakuseiSyouninJoukyou(Date kojinSeisanShiraibi) {

		final String sql =
			"INSERT INTO shounin_joukyou "
		+ "SELECT "
		+ "  fb.denpyou_id "			//denpyou_id
		+ "  ,(SELECT COALESCE(MAX(edano + 1), 1) FROM shounin_joukyou tmp WHERE tmp.denpyou_id = fb.denpyou_id) "	//edano
		+ "  ,'' "					//user_id
		+ "  ,'' "					//user_full_name
		+ "  ,'' "					//bumon_cd
		+ "  ,'' "					//bumon_full_name
		+ "  ,'' "					//bumon_role_id
		+ "  ,'' "					//bumon_role_name
		+ "  ,'' "					//gyoumu_role_id
		+ "  ,'' "					//gyoumu_role_name
		+ "  ,'' "					//joukyou_cd
		+ "  ,'FBデータ作成' "		//joukyou
		+ "  ,'' "					//comment
		+ "  ,NULL "					//shounin_route_edano
		+ "  ,NULL "					//shounin_route_gougi_edano
		+ "  ,NULL "					//shounin_route_gougi_edaedano
		+ "  ,NULL "					//shounin_shori_kengen_no
		+ "  ,'' "					//shounin_shori_kengen_name
		+ "  ,'batch' "				//touroku_user_id
		+ "  ,current_timestamp "		//touroku_time
		+ "  ,'batch' "				//koushin_user_id
		+ "  ,current_timestamp "		//koushin_time
		+ "FROM fb "
		+ "WHERE "
		+ "  fb_status = '0' "
		+ "  AND furikomi_date = ? "
		+ "GROUP BY denpyou_id ";
		return connection.update(sql, kojinSeisanShiraibi);
	}
	
	/**
	 * FB抽出状態を更新する
	 * @param kojinSeisanShiraibi 個人精算支払日
	 * @return 更新件数
	 */
	public int updateFBStatus(Date kojinSeisanShiraibi) {
		
		final String sql = 
				" UPDATE fb "
			  + "    SET fb_status = '1', "
			  + " 		 koushin_time = current_timestamp "
			  + "  WHERE fb_status = '0' "
			  + "	 AND furikomi_date = ? ";
		return connection.update(sql, kojinSeisanShiraibi);
	}
	
	/**
	 *伝票一覧のFBデータ抽出フラグを更新する
	 * @param kojinSeisanShiraibi 個人精算支払日
	 * @return 更新件数
	 */
	public int updateDenpyouIchiranFBStatus(Date kojinSeisanShiraibi) {
		
		final String sql = 
				  "UPDATE denpyou_ichiran di "
				+ " SET fb_status = '1' "
				+ " WHERE di.denpyou_id IN "
				+ " (SELECT denpyou_id FROM fb WHERE fb_status = '1' AND furikomi_date = ? )";
		return connection.update(sql, kojinSeisanShiraibi);
	}
}
