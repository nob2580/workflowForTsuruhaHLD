package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;

/**
 * 仕訳データ変更画面Logicクラス
 *
 */
public class ShiwakeDataSiasHenkouLogic extends EteamAbstractLogic {
	
	/**
	 * 伝票IDをキーにシリアル番号、仕訳抽出状態を取得する
	 * @param denpyoId 伝票ID
	 * @return 検索結果(0件の場合はサイズ0のリストを返却)
	 */
	public List<GMap> selectSerialNo(String denpyoId){
		
		final String sql = 
					"SELECT serial_no, shiwake_status "
				+ "FROM shiwake_sias "
				+ "WHERE "
				+ " denpyou_id = ? "
				+ "ORDER BY serial_no";
		
		return connection.load(sql,denpyoId);
	}
	
	/**
	 * 仕訳抽出状態の名称を内部コード設定テーブルから取得する
	 * @param naibuCodeNm 内部コード名称
	 * @param naibuCode 内部コード
	 * @return 名称
	 */
	public GMap selectNaibuCdSetting(String naibuCodeNm, String naibuCode){

		final String sql = "SELECT name"
						+ " FROM"
						+ "  naibu_cd_setting"
						+ " WHERE "
						+ "  naibu_cd_name = ? AND naibu_cd = ?";

		return connection.find(sql, naibuCodeNm, naibuCode);
	}
	
	/**
	 * 伝票IDをキーに伝票種別URL,伝票区分を取得する
	 * @param denpyouId 伝票ID
	 * @return 検索結果(1件)
	 */
	public GMap selectDenpyouInfo(String denpyouId){

		final String sql = "SELECT denpyou_shubetsu_url, densi.denpyou_kbn"
						+ " FROM "
						+ "  denpyou_shubetsu_ichiran densi "
						+ " INNER JOIN denpyou den ON "
						+ "  densi.denpyou_kbn = den.denpyou_kbn"
						+ " WHERE"
						+ "  den.denpyou_id = ?";
		
		return connection.find(sql, denpyouId);
	}
	
	/**
	 * シリアル番号をキーに仕訳データを取得する
	 * @param serialNo シリアル番号
	 * @param denpyouId 伝票ID
	 * @return 検索結果(1件)
	 */
	public GMap selectShiwakeData(int serialNo, String denpyouId){
		String kbn = denpyouId.substring(7, 11);
		
		String sql = " SELECT * "
				+ "  ,:HF1_NAME AS hf1_name"
				+ "  ,:HF2_NAME AS hf2_name"
				+ "  ,:HF3_NAME AS hf3_name"
				+ "  ,:HF4_NAME AS hf4_name"
				+ "  ,:HF5_NAME AS hf5_name"
				+ "  ,:HF6_NAME AS hf6_name"
				+ "  ,:HF7_NAME AS hf7_name"
				+ "  ,:HF8_NAME AS hf8_name"
				+ "  ,:HF9_NAME AS hf9_name"
				+ "  ,:HF10_NAME AS hf10_name"
				+ "  ,rbm.futan_bumon_name AS kari_bumon_name"
				+ "  ,rto.torihikisaki_name_ryakushiki AS kari_torihikisaki_name"
				+ "  ,rkm.kamoku_name_ryakushiki AS kari_kamoku_name"
				+ "  ,red.edaban_name AS kari_edaban_name"
				+ "  ,rpj.project_name AS kari_project_name"
				+ "  ,rsg.segment_name_ryakushiki AS kari_segment_name"
				+ "  ,:R_UF1_NAME AS kari_uf1_name"
				+ "  ,:R_UF2_NAME AS kari_uf2_name"
				+ "  ,:R_UF3_NAME AS kari_uf3_name"
				+ "  ,:R_UF4_NAME AS kari_uf4_name"
				+ "  ,:R_UF5_NAME AS kari_uf5_name"
				+ "  ,:R_UF6_NAME AS kari_uf6_name"
				+ "  ,:R_UF7_NAME AS kari_uf7_name"
				+ "  ,:R_UF8_NAME AS kari_uf8_name"
				+ "  ,:R_UF9_NAME AS kari_uf9_name"
				+ "  ,:R_UF10_NAME AS kari_uf10_name"
				+ "  ,:R_UF_KOTEI1_NAME AS kari_uf_kotei1_name"
				+ "  ,:R_UF_KOTEI2_NAME AS kari_uf_kotei2_name"
				+ "  ,:R_UF_KOTEI3_NAME AS kari_uf_kotei3_name"
				+ "  ,:R_UF_KOTEI4_NAME AS kari_uf_kotei4_name"
				+ "  ,:R_UF_KOTEI5_NAME AS kari_uf_kotei5_name"
				+ "  ,:R_UF_KOTEI6_NAME AS kari_uf_kotei6_name"
				+ "  ,:R_UF_KOTEI7_NAME AS kari_uf_kotei7_name"
				+ "  ,:R_UF_KOTEI8_NAME AS kari_uf_kotei8_name"
				+ "  ,:R_UF_KOTEI9_NAME AS kari_uf_kotei9_name"
				+ "  ,:R_UF_KOTEI10_NAME AS kari_uf_kotei10_name"
				+ "  ,sbm.futan_bumon_name AS kashi_bumon_name"
				+ "  ,sto.torihikisaki_name_ryakushiki AS kashi_torihikisaki_name"
				+ "  ,skm.kamoku_name_ryakushiki AS kashi_kamoku_name"
				+ "  ,sed.edaban_name AS kashi_edaban_name"
				+ "  ,spj.project_name AS kashi_project_name"
				+ "  ,ssg.segment_name_ryakushiki AS kashi_segment_name"
				+ "  ,:S_UF1_NAME AS kashi_uf1_name"
				+ "  ,:S_UF2_NAME AS kashi_uf2_name"
				+ "  ,:S_UF3_NAME AS kashi_uf3_name"
				+ "  ,:S_UF4_NAME AS kashi_uf4_name"
				+ "  ,:S_UF5_NAME AS kashi_uf5_name"
				+ "  ,:S_UF6_NAME AS kashi_uf6_name"
				+ "  ,:S_UF7_NAME AS kashi_uf7_name"
				+ "  ,:S_UF8_NAME AS kashi_uf8_name"
				+ "  ,:S_UF9_NAME AS kashi_uf9_name"
				+ "  ,:S_UF10_NAME AS kashi_uf10_name"
				+ "  ,:S_UF_KOTEI1_NAME AS kashi_uf_kotei1_name"
				+ "  ,:S_UF_KOTEI2_NAME AS kashi_uf_kotei2_name"
				+ "  ,:S_UF_KOTEI3_NAME AS kashi_uf_kotei3_name"
				+ "  ,:S_UF_KOTEI4_NAME AS kashi_uf_kotei4_name"
				+ "  ,:S_UF_KOTEI5_NAME AS kashi_uf_kotei5_name"
				+ "  ,:S_UF_KOTEI6_NAME AS kashi_uf_kotei6_name"
				+ "  ,:S_UF_KOTEI7_NAME AS kashi_uf_kotei7_name"
				+ "  ,:S_UF_KOTEI8_NAME AS kashi_uf_kotei8_name"
				+ "  ,:S_UF_KOTEI9_NAME AS kashi_uf_kotei9_name"
				+ "  ,:S_UF_KOTEI10_NAME AS kashi_uf_kotei10_name"
				+ " FROM"
				+ "  shiwake_sias s"
				+ " :HF1_ICHIRAN"
				+ " :HF2_ICHIRAN"
				+ " :HF3_ICHIRAN"
				+ " :HF4_ICHIRAN"
				+ " :HF5_ICHIRAN"
				+ " :HF6_ICHIRAN"
				+ " :HF7_ICHIRAN"
				+ " :HF8_ICHIRAN"
				+ " :HF9_ICHIRAN"
				+ " :HF10_ICHIRAN"
				+ " LEFT OUTER JOIN bumon_master rbm ON s.rbmn = rbm.futan_bumon_cd"
				+ " LEFT OUTER JOIN torihikisaki_master rto ON s.rtor = rto.torihikisaki_cd"
				+ " LEFT OUTER JOIN kamoku_master rkm ON s.rkmk = rkm.kamoku_gaibu_cd"
				+ " LEFT OUTER JOIN kamoku_edaban_zandaka red ON s.rkmk = red.kamoku_gaibu_cd AND s.reda = red.kamoku_edaban_cd"
				+ " LEFT OUTER JOIN project_master rpj ON s.rprj = rpj.project_cd"
				+ " LEFT OUTER JOIN segment_master rsg ON s.rseg = rsg.segment_cd"
				+ " :R_UF1_ICHIRAN"
				+ " :R_UF2_ICHIRAN"
				+ " :R_UF3_ICHIRAN"
				+ " :R_UF4_ICHIRAN"
				+ " :R_UF5_ICHIRAN"
				+ " :R_UF6_ICHIRAN"
				+ " :R_UF7_ICHIRAN"
				+ " :R_UF8_ICHIRAN"
				+ " :R_UF9_ICHIRAN"
				+ " :R_UF10_ICHIRAN"
				+ " :R_UF_KOTEI1_ICHIRAN"
				+ " :R_UF_KOTEI2_ICHIRAN"
				+ " :R_UF_KOTEI3_ICHIRAN"
				+ " :R_UF_KOTEI4_ICHIRAN"
				+ " :R_UF_KOTEI5_ICHIRAN"
				+ " :R_UF_KOTEI6_ICHIRAN"
				+ " :R_UF_KOTEI7_ICHIRAN"
				+ " :R_UF_KOTEI8_ICHIRAN"
				+ " :R_UF_KOTEI9_ICHIRAN"
				+ " :R_UF_KOTEI10_ICHIRAN"
				+ " LEFT OUTER JOIN bumon_master sbm ON s.sbmn = sbm.futan_bumon_cd"
				+ " LEFT OUTER JOIN torihikisaki_master sto ON s.stor = sto.torihikisaki_cd"
				+ " LEFT OUTER JOIN kamoku_master skm ON s.skmk = skm.kamoku_gaibu_cd"
				+ " LEFT OUTER JOIN kamoku_edaban_zandaka sed ON s.skmk = sed.kamoku_gaibu_cd AND s.seda = sed.kamoku_edaban_cd"
				+ " LEFT OUTER JOIN project_master spj ON s.sprj = spj.project_cd"
				+ " LEFT OUTER JOIN segment_master ssg ON s.sseg = ssg.segment_cd"
				+ " :S_UF1_ICHIRAN"
				+ " :S_UF2_ICHIRAN"
				+ " :S_UF3_ICHIRAN"
				+ " :S_UF4_ICHIRAN"
				+ " :S_UF5_ICHIRAN"
				+ " :S_UF6_ICHIRAN"
				+ " :S_UF7_ICHIRAN"
				+ " :S_UF8_ICHIRAN"
				+ " :S_UF9_ICHIRAN"
				+ " :S_UF10_ICHIRAN"
				+ " :S_UF_KOTEI1_ICHIRAN"
				+ " :S_UF_KOTEI2_ICHIRAN"
				+ " :S_UF_KOTEI3_ICHIRAN"
				+ " :S_UF_KOTEI4_ICHIRAN"
				+ " :S_UF_KOTEI5_ICHIRAN"
				+ " :S_UF_KOTEI6_ICHIRAN"
				+ " :S_UF_KOTEI7_ICHIRAN"
				+ " :S_UF_KOTEI8_ICHIRAN"
				+ " :S_UF_KOTEI9_ICHIRAN"
				+ " :S_UF_KOTEI10_ICHIRAN"
				+ " WHERE"
				+ " serial_no = ?"
				+ " AND denpyou_id= ?";
		
		if (Integer.parseInt(setting.hf1Mapping()) == 0) {
			sql = sql.replaceAll(":HF1_NAME", "''");
			sql = sql.replaceAll(":HF1_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF1_NAME", "h1.hf1_name_ryakushiki");
			sql = sql.replaceAll(":HF1_ICHIRAN", "LEFT OUTER JOIN hf1_ichiran h1 ON s.hf" + setting.hf1Mapping() + " = h1.hf1_cd");
		}
		
		if (Integer.parseInt(setting.hf2Mapping()) == 0) {
			sql = sql.replaceAll(":HF2_NAME", "''");
			sql = sql.replaceAll(":HF2_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF2_NAME", "h2.hf2_name_ryakushiki");
			sql = sql.replaceAll(":HF2_ICHIRAN", "LEFT OUTER JOIN hf2_ichiran h2 ON s.hf" + setting.hf2Mapping() + " = h2.hf2_cd");
		}
		
		if (Integer.parseInt(setting.hf3Mapping()) == 0) {
			sql = sql.replaceAll(":HF3_NAME", "''");
			sql = sql.replaceAll(":HF3_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF3_NAME", "h3.hf3_name_ryakushiki");
			sql = sql.replaceAll(":HF3_ICHIRAN", "LEFT OUTER JOIN hf3_ichiran h3 ON s.hf" + setting.hf3Mapping() + " = h3.hf3_cd");
		}
		
		if (Integer.parseInt(setting.hf4Mapping()) == 0) {
			sql = sql.replaceAll(":HF4_NAME", "''");
			sql = sql.replaceAll(":HF4_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF4_NAME", "h4.hf4_name_ryakushiki");
			sql = sql.replaceAll(":HF4_ICHIRAN", "LEFT OUTER JOIN hf4_ichiran h4 ON s.hf" + setting.hf4Mapping() + " = h4.hf4_cd");
		}
		
		if (Integer.parseInt(setting.hf5Mapping()) == 0) {
			sql = sql.replaceAll(":HF5_NAME", "''");
			sql = sql.replaceAll(":HF5_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF5_NAME", "h5.hf5_name_ryakushiki");
			sql = sql.replaceAll(":HF5_ICHIRAN", "LEFT OUTER JOIN hf5_ichiran h5 ON s.hf" + setting.hf5Mapping() + " = h5.hf5_cd");
		}
		
		if (Integer.parseInt(setting.hf6Mapping()) == 0) {
			sql = sql.replaceAll(":HF6_NAME", "''");
			sql = sql.replaceAll(":HF6_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF6_NAME", "h6.hf6_name_ryakushiki");
			sql = sql.replaceAll(":HF6_ICHIRAN", "LEFT OUTER JOIN hf6_ichiran h6 ON s.hf" + setting.hf6Mapping() + " = h6.hf6_cd");
		}
		
		if (Integer.parseInt(setting.hf7Mapping()) == 0) {
			sql = sql.replaceAll(":HF7_NAME", "''");
			sql = sql.replaceAll(":HF7_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF7_NAME", "h7.hf7_name_ryakushiki");
			sql = sql.replaceAll(":HF7_ICHIRAN", "LEFT OUTER JOIN hf7_ichiran h7 ON s.hf" + setting.hf7Mapping() + " = h7.hf7_cd");
		}
		
		if (Integer.parseInt(setting.hf8Mapping()) == 0) {
			sql = sql.replaceAll(":HF8_NAME", "''");
			sql = sql.replaceAll(":HF8_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF8_NAME", "h8.hf8_name_ryakushiki");
			sql = sql.replaceAll(":HF8_ICHIRAN", "LEFT OUTER JOIN hf8_ichiran h8 ON s.hf" + setting.hf8Mapping() + " = h8.hf8_cd");
		}
		
		if (Integer.parseInt(setting.hf9Mapping()) == 0) {
			sql = sql.replaceAll(":HF9_NAME", "''");
			sql = sql.replaceAll(":HF9_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF9_NAME", "h9.hf9_name_ryakushiki");
			sql = sql.replaceAll(":HF9_ICHIRAN", "LEFT OUTER JOIN hf9_ichiran h9 ON s.hf" + setting.hf9Mapping() + " = h9.hf9_cd");
		}
		
		if (Integer.parseInt(setting.hf10Mapping()) == 0) {
			sql = sql.replaceAll(":HF10_NAME", "''");
			sql = sql.replaceAll(":HF10_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":HF10_NAME", "h10.hf10_name_ryakushiki");
			sql = sql.replaceAll(":HF10_ICHIRAN", "LEFT OUTER JOIN hf10_ichiran h10 ON s.hf" + setting.hf10Mapping() + " = h10.hf10_cd");
		}
		
		if (Integer.parseInt(setting.uf1Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF1_NAME", "''");
			sql = sql.replaceAll(":S_UF1_NAME", "''");
			sql = sql.replaceAll(":R_UF1_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF1_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF1_NAME", "rd1.uf1_name_ryakushiki");
			sql = sql.replaceAll(":S_UF1_NAME", "sd1.uf1_name_ryakushiki");
			sql = sql.replaceAll(":R_UF1_ICHIRAN", "LEFT OUTER JOIN uf1_ichiran rd1 ON s.rdm" + setting.uf1Mapping() + " = rd1.uf1_cd");
			sql = sql.replaceAll(":S_UF1_ICHIRAN", "LEFT OUTER JOIN uf1_ichiran sd1 ON s.sdm" + setting.uf1Mapping() + " = sd1.uf1_cd");
		}
		
		if (Integer.parseInt(setting.uf2Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF2_NAME", "''");
			sql = sql.replaceAll(":S_UF2_NAME", "''");
			sql = sql.replaceAll(":R_UF2_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF2_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF2_NAME", "rd2.uf2_name_ryakushiki");
			sql = sql.replaceAll(":S_UF2_NAME", "sd2.uf2_name_ryakushiki");
			sql = sql.replaceAll(":R_UF2_ICHIRAN", "LEFT OUTER JOIN uf2_ichiran rd2 ON s.rdm" + setting.uf2Mapping() + " = rd2.uf2_cd");
			sql = sql.replaceAll(":S_UF2_ICHIRAN", "LEFT OUTER JOIN uf2_ichiran sd2 ON s.sdm" + setting.uf2Mapping() + " = sd2.uf2_cd");
		}
		
		if (Integer.parseInt(setting.uf3Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF3_NAME", "''");
			sql = sql.replaceAll(":S_UF3_NAME", "''");
			sql = sql.replaceAll(":R_UF3_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF3_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF3_NAME", "rd3.uf3_name_ryakushiki");
			sql = sql.replaceAll(":S_UF3_NAME", "sd3.uf3_name_ryakushiki");
			sql = sql.replaceAll(":R_UF3_ICHIRAN", "LEFT OUTER JOIN uf3_ichiran rd3 ON s.rdm" + setting.uf3Mapping() + " = rd3.uf3_cd");
			sql = sql.replaceAll(":S_UF3_ICHIRAN", "LEFT OUTER JOIN uf3_ichiran sd3 ON s.sdm" + setting.uf3Mapping() + " = sd3.uf3_cd");
		}
		
		if (Integer.parseInt(setting.uf4Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF4_NAME", "''");
			sql = sql.replaceAll(":S_UF4_NAME", "''");
			sql = sql.replaceAll(":R_UF4_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF4_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF4_NAME", "rd4.uf4_name_ryakushiki");
			sql = sql.replaceAll(":S_UF4_NAME", "sd4.uf4_name_ryakushiki");
			sql = sql.replaceAll(":R_UF4_ICHIRAN", "LEFT OUTER JOIN uf4_ichiran rd4 ON s.rdm" + setting.uf4Mapping() + " = rd4.uf4_cd");
			sql = sql.replaceAll(":S_UF4_ICHIRAN", "LEFT OUTER JOIN uf4_ichiran sd4 ON s.sdm" + setting.uf4Mapping() + " = sd4.uf4_cd");
		}
		
		if (Integer.parseInt(setting.uf5Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF5_NAME", "''");
			sql = sql.replaceAll(":S_UF5_NAME", "''");
			sql = sql.replaceAll(":R_UF5_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF5_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF5_NAME", "rd5.uf5_name_ryakushiki");
			sql = sql.replaceAll(":S_UF5_NAME", "sd5.uf5_name_ryakushiki");
			sql = sql.replaceAll(":R_UF5_ICHIRAN", "LEFT OUTER JOIN uf5_ichiran rd5 ON s.rdm" + setting.uf5Mapping() + " = rd5.uf5_cd");
			sql = sql.replaceAll(":S_UF5_ICHIRAN", "LEFT OUTER JOIN uf5_ichiran sd5 ON s.sdm" + setting.uf5Mapping() + " = sd5.uf5_cd");
		}
		
		if (Integer.parseInt(setting.uf6Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF6_NAME", "''");
			sql = sql.replaceAll(":S_UF6_NAME", "''");
			sql = sql.replaceAll(":R_UF6_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF6_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF6_NAME", "rd6.uf6_name_ryakushiki");
			sql = sql.replaceAll(":S_UF6_NAME", "sd6.uf6_name_ryakushiki");
			sql = sql.replaceAll(":R_UF6_ICHIRAN", "LEFT OUTER JOIN uf6_ichiran rd6 ON s.rdm" + setting.uf6Mapping() + " = rd6.uf6_cd");
			sql = sql.replaceAll(":S_UF6_ICHIRAN", "LEFT OUTER JOIN uf6_ichiran sd6 ON s.sdm" + setting.uf6Mapping() + " = sd6.uf6_cd");
		}
		
		if (Integer.parseInt(setting.uf7Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF7_NAME", "''");
			sql = sql.replaceAll(":S_UF7_NAME", "''");
			sql = sql.replaceAll(":R_UF7_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF7_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF7_NAME", "rd7.uf7_name_ryakushiki");
			sql = sql.replaceAll(":S_UF7_NAME", "sd7.uf7_name_ryakushiki");
			sql = sql.replaceAll(":R_UF7_ICHIRAN", "LEFT OUTER JOIN uf7_ichiran rd7 ON s.rdm" + setting.uf7Mapping() + " = rd7.uf7_cd");
			sql = sql.replaceAll(":S_UF7_ICHIRAN", "LEFT OUTER JOIN uf7_ichiran sd7 ON s.sdm" + setting.uf7Mapping() + " = sd7.uf7_cd");
		}
		
		if (Integer.parseInt(setting.uf8Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF8_NAME", "''");
			sql = sql.replaceAll(":S_UF8_NAME", "''");
			sql = sql.replaceAll(":R_UF8_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF8_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF8_NAME", "rd8.uf8_name_ryakushiki");
			sql = sql.replaceAll(":S_UF8_NAME", "sd8.uf8_name_ryakushiki");
			sql = sql.replaceAll(":R_UF8_ICHIRAN", "LEFT OUTER JOIN uf8_ichiran rd8 ON s.rdm" + setting.uf8Mapping() + " = rd8.uf8_cd");
			sql = sql.replaceAll(":S_UF8_ICHIRAN", "LEFT OUTER JOIN uf8_ichiran sd8 ON s.sdm" + setting.uf8Mapping() + " = sd8.uf8_cd");
		}
		
		if (Integer.parseInt(setting.uf9Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF9_NAME", "''");
			sql = sql.replaceAll(":S_UF9_NAME", "''");
			sql = sql.replaceAll(":R_UF9_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF9_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF9_NAME", "rd9.uf9_name_ryakushiki");
			sql = sql.replaceAll(":S_UF9_NAME", "sd9.uf9_name_ryakushiki");
			sql = sql.replaceAll(":R_UF9_ICHIRAN", "LEFT OUTER JOIN uf9_ichiran rd9 ON s.rdm" + setting.uf9Mapping() + " = rd9.uf9_cd");
			sql = sql.replaceAll(":S_UF9_ICHIRAN", "LEFT OUTER JOIN uf9_ichiran sd9 ON s.sdm" + setting.uf9Mapping() + " = sd9.uf9_cd");
		}
		
		if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.UF10_MAPPING)) == 0) {
			sql = sql.replaceAll(":R_UF10_NAME", "''");
			sql = sql.replaceAll(":S_UF10_NAME", "''");
			sql = sql.replaceAll(":R_UF10_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF10_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF10_NAME", "rd10.uf10_name_ryakushiki");
			sql = sql.replaceAll(":S_UF10_NAME", "sd10.uf10_name_ryakushiki");
			sql = sql.replaceAll(":R_UF10_ICHIRAN", "LEFT OUTER JOIN uf10_ichiran rd10 ON s.rdm" + EteamSettingInfo.getSettingInfo(Key.UF10_MAPPING) + " = rd10.uf10_cd");
			sql = sql.replaceAll(":S_UF10_ICHIRAN", "LEFT OUTER JOIN uf10_ichiran sd10 ON s.sdm" + EteamSettingInfo.getSettingInfo(Key.UF10_MAPPING) + " = sd10.uf10_cd");
		}

		if (Integer.parseInt(setting.ufKotei1Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI1_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI1_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI1_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI1_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI1_NAME", "rdk1.uf_kotei1_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI1_NAME", "sdk1.uf_kotei1_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI1_ICHIRAN", "LEFT OUTER JOIN uf_kotei1_ichiran rdk1 ON s.rdm" + setting.ufKotei1Mapping() + " = rdk1.uf_kotei1_cd");
			sql = sql.replaceAll(":S_UF_KOTEI1_ICHIRAN", "LEFT OUTER JOIN uf_kotei1_ichiran sdk1 ON s.sdm" + setting.ufKotei1Mapping() + " = sdk1.uf_kotei1_cd");
		}

		if (Integer.parseInt(setting.ufKotei2Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI2_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI2_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI2_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI2_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI2_NAME", "rdk2.uf_kotei2_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI2_NAME", "sdk2.uf_kotei2_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI2_ICHIRAN", "LEFT OUTER JOIN uf_kotei2_ichiran rdk2 ON s.rdm" + setting.ufKotei2Mapping() + " = rdk2.uf_kotei2_cd");
			sql = sql.replaceAll(":S_UF_KOTEI2_ICHIRAN", "LEFT OUTER JOIN uf_kotei2_ichiran sdk2 ON s.sdm" + setting.ufKotei2Mapping() + " = sdk2.uf_kotei2_cd");
		}

		if (Integer.parseInt(setting.ufKotei3Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI3_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI3_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI3_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI3_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI3_NAME", "rdk3.uf_kotei3_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI3_NAME", "sdk3.uf_kotei3_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI3_ICHIRAN", "LEFT OUTER JOIN uf_kotei3_ichiran rdk3 ON s.rdm" + setting.ufKotei3Mapping() + " = rdk3.uf_kotei3_cd");
			sql = sql.replaceAll(":S_UF_KOTEI3_ICHIRAN", "LEFT OUTER JOIN uf_kotei3_ichiran sdk3 ON s.sdm" + setting.ufKotei3Mapping() + " = sdk3.uf_kotei3_cd");
		}

		if (Integer.parseInt(setting.ufKotei4Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI4_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI4_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI4_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI4_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI4_NAME", "rdk4.uf_kotei4_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI4_NAME", "sdk4.uf_kotei4_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI4_ICHIRAN", "LEFT OUTER JOIN uf_kotei4_ichiran rdk4 ON s.rdm" + setting.ufKotei4Mapping() + " = rdk4.uf_kotei4_cd");
			sql = sql.replaceAll(":S_UF_KOTEI4_ICHIRAN", "LEFT OUTER JOIN uf_kotei4_ichiran sdk4 ON s.sdm" + setting.ufKotei4Mapping() + " = sdk4.uf_kotei4_cd");
		}

		if (Integer.parseInt(setting.ufKotei5Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI5_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI5_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI5_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI5_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI5_NAME", "rdk5.uf_kotei5_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI5_NAME", "sdk5.uf_kotei5_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI5_ICHIRAN", "LEFT OUTER JOIN uf_kotei5_ichiran rdk5 ON s.rdm" + setting.ufKotei5Mapping() + " = rdk5.uf_kotei5_cd");
			sql = sql.replaceAll(":S_UF_KOTEI5_ICHIRAN", "LEFT OUTER JOIN uf_kotei5_ichiran sdk5 ON s.sdm" + setting.ufKotei5Mapping() + " = sdk5.uf_kotei5_cd");
		}

		if (Integer.parseInt(setting.ufKotei6Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI6_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI6_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI6_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI6_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI6_NAME", "rdk6.uf_kotei6_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI6_NAME", "sdk6.uf_kotei6_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI6_ICHIRAN", "LEFT OUTER JOIN uf_kotei6_ichiran rdk6 ON s.rdm" + setting.ufKotei6Mapping() + " = rdk6.uf_kotei6_cd");
			sql = sql.replaceAll(":S_UF_KOTEI6_ICHIRAN", "LEFT OUTER JOIN uf_kotei6_ichiran sdk6 ON s.sdm" + setting.ufKotei6Mapping() + " = sdk6.uf_kotei6_cd");
		}

		if (Integer.parseInt(setting.ufKotei7Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI7_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI7_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI7_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI7_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI7_NAME", "rdk7.uf_kotei7_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI7_NAME", "sdk7.uf_kotei7_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI7_ICHIRAN", "LEFT OUTER JOIN uf_kotei7_ichiran rdk7 ON s.rdm" + setting.ufKotei7Mapping() + " = rdk7.uf_kotei7_cd");
			sql = sql.replaceAll(":S_UF_KOTEI7_ICHIRAN", "LEFT OUTER JOIN uf_kotei7_ichiran sdk7 ON s.sdm" + setting.ufKotei7Mapping() + " = sdk7.uf_kotei7_cd");
		}

		if (Integer.parseInt(setting.ufKotei8Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI8_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI8_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI8_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI8_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI8_NAME", "rdk8.uf_kotei8_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI8_NAME", "sdk8.uf_kotei8_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI8_ICHIRAN", "LEFT OUTER JOIN uf_kotei8_ichiran rdk8 ON s.rdm" + setting.ufKotei8Mapping() + " = rdk8.uf_kotei8_cd");
			sql = sql.replaceAll(":S_UF_KOTEI8_ICHIRAN", "LEFT OUTER JOIN uf_kotei8_ichiran sdk8 ON s.sdm" + setting.ufKotei8Mapping() + " = sdk8.uf_kotei8_cd");
		}

		if (Integer.parseInt(setting.ufKotei9Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI9_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI9_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI9_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI9_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI9_NAME", "rdk9.uf_kotei9_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI9_NAME", "sdk9.uf_kotei9_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI9_ICHIRAN", "LEFT OUTER JOIN uf_kotei9_ichiran rdk9 ON s.rdm" + setting.ufKotei9Mapping() + " = rdk9.uf_kotei9_cd");
			sql = sql.replaceAll(":S_UF_KOTEI9_ICHIRAN", "LEFT OUTER JOIN uf_kotei9_ichiran sdk9 ON s.sdm" + setting.ufKotei9Mapping() + " = sdk9.uf_kotei9_cd");
		}

		if (Integer.parseInt(setting.ufKotei10Mapping()) == 0) {
			sql = sql.replaceAll(":R_UF_KOTEI10_NAME", "''");
			sql = sql.replaceAll(":S_UF_KOTEI10_NAME", "''");
			sql = sql.replaceAll(":R_UF_KOTEI10_ICHIRAN", "");
			sql = sql.replaceAll(":S_UF_KOTEI10_ICHIRAN", "");
			
		} else {
			sql = sql.replaceAll(":R_UF_KOTEI10_NAME", "rdk10.uf_kotei10_name_ryakushiki");
			sql = sql.replaceAll(":S_UF_KOTEI10_NAME", "sdk10.uf_kotei10_name_ryakushiki");
			sql = sql.replaceAll(":R_UF_KOTEI10_ICHIRAN", "LEFT OUTER JOIN uf_kotei10_ichiran rdk10 ON s.rdm" + setting.ufKotei10Mapping() + " = rdk10.uf_kotei10_cd");
			sql = sql.replaceAll(":S_UF_KOTEI10_ICHIRAN", "LEFT OUTER JOIN uf_kotei10_ichiran sdk10 ON s.sdm" + setting.ufKotei10Mapping() + " = sdk10.uf_kotei10_cd");
		}
		
		return connection.find(sql,serialNo,denpyouId);
	}
	
	/**
	 * 仕訳抽出テーブルを更新する
	 * @param dataMap 更新結果
	 * @return 更新件数(1件)
	 */
	public int updateShiwakeData(GMap dataMap){
		
		final String sql = "UPDATE shiwake_sias "
						+  "SET "
						+  " koushin_time = current_timestamp,"
						+  " dymd = ?,"
						+  " seiri = ?,"
						+  " dcno = ?,"
						+  " hf1 = ?,"
						+  " hf2 = ?,"
						+  " hf3 = ?,"
						+  " hf4 = ?,"
						+  " hf5 = ?,"
						+  " hf6 = ?,"
						+  " hf7 = ?,"
						+  " hf8 = ?,"
						+  " hf9 = ?,"
						+  " hf10 = ?,"
						+  " rbmn = ?,"
						+  " rtor = ?,"
						+  " rkmk = ?,"
						+  " reda = ?,"
						+  " rkoj = ?,"
						+  " rkos = ?,"
						+  " rprj = ?,"
						+  " rseg = ?,"
						+  " rdm1 = ?,"
						+  " rdm2 = ?,"
						+  " rdm3 = ?,"
						+  " rdm4 = ?,"
						+  " rdm5 = ?,"
						+  " rdm6 = ?,"
						+  " rdm7 = ?,"
						+  " rdm8 = ?,"
						+  " rdm9 = ?,"
						+  " rdm10 = ?,"
						+  " rdm11 = ?,"
						+  " rdm12 = ?,"
						+  " rdm13 = ?,"
						+  " rdm14 = ?,"
						+  " rdm15 = ?,"
						+  " rdm16 = ?,"
						+  " rdm17 = ?,"
						+  " rdm18 = ?,"
						+  " rdm19 = ?,"
						+  " rdm20 = ?,"
						+  " rtky = ?,"
						+  " rtno = ?,"
						+  " sbmn = ?,"
						+  " stor = ?,"
						+  " skmk = ?,"
						+  " seda = ?,"
						+  " skoj = ?,"
						+  " skos = ?,"
						+  " sprj = ?,"
						+  " sseg = ?,"
						+  " sdm1 = ?,"
						+  " sdm2 = ?,"
						+  " sdm3 = ?,"
						+  " sdm4 = ?,"
						+  " sdm5 = ?,"
						+  " sdm6 = ?,"
						+  " sdm7 = ?,"
						+  " sdm8 = ?,"
						+  " sdm9 = ?,"
						+  " sdm10 = ?,"
						+  " sdm11 = ?,"
						+  " sdm12 = ?,"
						+  " sdm13 = ?,"
						+  " sdm14 = ?,"
						+  " sdm15 = ?,"
						+  " sdm16 = ?,"
						+  " sdm17 = ?,"
						+  " sdm18 = ?,"
						+  " sdm19 = ?,"
						+  " sdm20 = ?,"
						+  " stky = ?,"
						+  " stno = ?,"
						+  " exvl = ?,"
						+  " valu = ?,"
						+  " zkmk = ?,"
						+  " zrit = ?,"
						+  " zkeigen = ?,"
						+  " zzkb = ?,"
						+  " zgyo = ?,"
						+  " zsre = ?,"
						+  " rrit = ?,"
						+  " rkeigen = ?,"
						+  " srit = ?,"
						+  " skeigen = ?,"
						+  " rzkb = ?,"
						+  " rgyo = ?,"
						+  " rsre = ?,"
						+  " szkb = ?,"
						+  " sgyo = ?,"
						+  " ssre = ?,"
						+  " symd = ?,"
						+  " skbn = ?,"
						+  " skiz = ?,"
						+  " uymd = ?,"
						+  " ukbn = ?,"
						+  " ukiz = ?,"
						+  " dkec = ?,"
						+  " kymd = ?,"
						+  " kbmn = ?,"
						+  " kusr = ?,"
						+  " fusr = ?,"
						+  " fsen = ?,"
						+  " tkflg = ?,"
						+  " sgno = ?,"
						+  " bunri = ?,"
						+  " heic = ?,"
						+  " rate = ?,"
						+  " gexvl = ?,"
						+  " gvalu = ?,"
						+  " gsep = ?,"
						+  " rurizeikeisan = ?,"
						+  " surizeikeisan = ?,"
						+  " zurizeikeisan = ?,"
						+  " rmenzeikeika = ?,"
						+  " smenzeikeika = ?,"
						+  " zmenzeikeika = ?"
						+ " WHERE"
						+  " serial_no = ?";
		
		return connection.update(sql,
						dataMap.get("dymd"),
						dataMap.get("seiri"),
						dataMap.get("dcno"),
						dataMap.get("hf1"),
						dataMap.get("hf2"),
						dataMap.get("hf3"),
						dataMap.get("hf4"),
						dataMap.get("hf5"),
						dataMap.get("hf6"),
						dataMap.get("hf7"),
						dataMap.get("hf8"),
						dataMap.get("hf9"),
						dataMap.get("hf10"),
						dataMap.get("rbmn"),
						dataMap.get("rtor"),
						dataMap.get("rkmk"),
						dataMap.get("reda"),
						dataMap.get("rkoj"),
						dataMap.get("rkos"),
						dataMap.get("rprj"),
						dataMap.get("rseg"),
						dataMap.get("rdm1"),
						dataMap.get("rdm2"),
						dataMap.get("rdm3"),
						dataMap.get("rdm4"),
						dataMap.get("rdm5"),
						dataMap.get("rdm6"),
						dataMap.get("rdm7"),
						dataMap.get("rdm8"),
						dataMap.get("rdm9"),
						dataMap.get("rdm10"),
						dataMap.get("rdm11"),
						dataMap.get("rdm12"),
						dataMap.get("rdm13"),
						dataMap.get("rdm14"),
						dataMap.get("rdm15"),
						dataMap.get("rdm16"),
						dataMap.get("rdm17"),
						dataMap.get("rdm18"),
						dataMap.get("rdm19"),
						dataMap.get("rdm20"),
						dataMap.get("rtky"),
						dataMap.get("rtno"),
						dataMap.get("sbmn"),
						dataMap.get("stor"),
						dataMap.get("skmk"),
						dataMap.get("seda"),
						dataMap.get("skoj"),
						dataMap.get("skos"),
						dataMap.get("sprj"),
						dataMap.get("sseg"),
						dataMap.get("sdm1"),
						dataMap.get("sdm2"),
						dataMap.get("sdm3"),
						dataMap.get("sdm4"),
						dataMap.get("sdm5"),
						dataMap.get("sdm6"),
						dataMap.get("sdm7"),
						dataMap.get("sdm8"),
						dataMap.get("sdm9"),
						dataMap.get("sdm10"),
						dataMap.get("sdm11"),
						dataMap.get("sdm12"),
						dataMap.get("sdm13"),
						dataMap.get("sdm14"),
						dataMap.get("sdm15"),
						dataMap.get("sdm16"),
						dataMap.get("sdm17"),
						dataMap.get("sdm18"),
						dataMap.get("sdm19"),
						dataMap.get("sdm20"),
						dataMap.get("stky"),
						dataMap.get("stno"),
						dataMap.get("exvl"),
						dataMap.get("valu"),
						dataMap.get("zkmk"),
						dataMap.get("zrit"),
						dataMap.get("zkeigen"),
						dataMap.get("zzkb"),
						dataMap.get("zgyo"),
						dataMap.get("zsre"),
						dataMap.get("rrit"),
						dataMap.get("rkeigen"),
						dataMap.get("srit"),
						dataMap.get("skeigen"),
						dataMap.get("rzkb"),
						dataMap.get("rgyo"),
						dataMap.get("rsre"),
						dataMap.get("szkb"),
						dataMap.get("sgyo"),
						dataMap.get("ssre"),
						dataMap.get("symd"),
						dataMap.get("skbn"),
						dataMap.get("skiz"),
						dataMap.get("uymd"),
						dataMap.get("ukbn"),
						dataMap.get("ukiz"),
						dataMap.get("dkec"),
						dataMap.get("kymd"),
						dataMap.get("kbmn"),
						dataMap.get("kusr"),
						dataMap.get("fusr"),
						dataMap.get("fsen"),
						dataMap.get("tkflg"),
						dataMap.get("sgno"),
						dataMap.get("bunri"),
						dataMap.get("heic"),
						dataMap.get("rate"),
						dataMap.get("gexvl"),
						dataMap.get("gvalu"),
						dataMap.get("gsep"),
						dataMap.get("rurizeikeisan"), //インボイス項目
						dataMap.get("surizeikeisan"),
						dataMap.get("zurizeikeisan"),
						dataMap.get("rmenzeikeika"),
						dataMap.get("smenzeikeika"),
						dataMap.get("zmenzeikeika"),
						dataMap.get("serial_no")
						);
	}

	/**
	 * 仕訳抽出状態を更新する
	 * @param shiwakeStatus 仕訳抽出状態(9)
	 * @param serialNo シリアル番号
	 * @return 処理件数
	 */
	public int updateShiwakeStatus(String shiwakeStatus, int serialNo){
		
		final String sql = "UPDATE shiwake_sias SET "
							+ "koushin_time = current_timestamp, "
							+ "shiwake_status = ? " 
							+ "WHERE serial_no = ? ";
		return connection.update(sql, shiwakeStatus, serialNo);

	}
	
	/**
	 * dcno(serialNo)の更新 (String) shiwakeData.get("dcno")
	 * @param denpyouId 
	 * @param beforeDcno 
	 * @param afterDcno 
	 * @return 
	 */
	public int updateDcno(String denpyouId, int beforeDcno, int afterDcno) {
		final String sql = "UPDATE denpyou SET "
				+ "serial_no = ? "
				+ "WHERE denpyou_id = ? "
				+ "and serial_no = ?";
		connection.update(sql,afterDcno, denpyouId, beforeDcno);
		
		final String sqlIchiran = "UPDATE denpyou_ichiran SET "
				+ "serial_no = ? "
				+ "WHERE denpyou_id = ? "
				+ "and serial_no = ?";
		
		return connection.update(sqlIchiran,afterDcno, denpyouId, beforeDcno);
	}

}
