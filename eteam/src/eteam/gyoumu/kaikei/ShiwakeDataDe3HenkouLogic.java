package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 仕訳データ変更画面Logicクラス
 *
 */
public class ShiwakeDataDe3HenkouLogic extends EteamAbstractLogic {
	
	/**
	 * 伝票IDをキーにシリアル番号、仕訳抽出状態を取得する
	 * @param denpyoId 伝票ID
	 * @return 検索結果(0件の場合はサイズ0のリストを返却)
	 */
	public List<GMap> selectSerialNo(String denpyoId){
		
		final String sql = 
					"SELECT serial_no, shiwake_status "
				+ "FROM shiwake_de3 "
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
		
		
		final String sql = " SELECT * "
				+ "  ,rbm.futan_bumon_name AS kari_bumon_name"
				+ "  ,rto.torihikisaki_name_ryakushiki AS kari_torihikisaki_name"
				+ "  ,rkm.kamoku_name_ryakushiki AS kari_kamoku_name"
				+ "  ,red.edaban_name AS kari_edaban_name"
				+ "  ,rpj.project_name AS kari_project_name"
				+ "  ,rsg.segment_name_ryakushiki AS kari_segment_name"
				+ "  ,rd1.uf1_name_ryakushiki AS kari_uf1_name"
				+ "  ,rd2.uf2_name_ryakushiki AS kari_uf2_name"
				+ "  ,rd3.uf3_name_ryakushiki AS kari_uf3_name"
				+ "  ,sbm.futan_bumon_name AS kashi_bumon_name"
				+ "  ,sto.torihikisaki_name_ryakushiki AS kashi_torihikisaki_name"
				+ "  ,skm.kamoku_name_ryakushiki AS kashi_kamoku_name"
				+ "  ,sed.edaban_name AS kashi_edaban_name"
				+ "  ,spj.project_name AS kashi_project_name"
				+ "  ,ssg.segment_name_ryakushiki AS kashi_segment_name"
				+ "  ,sd1.uf1_name_ryakushiki AS kashi_uf1_name"
				+ "  ,sd2.uf2_name_ryakushiki AS kashi_uf2_name"
				+ "  ,sd3.uf3_name_ryakushiki AS kashi_uf3_name"
				+ " FROM"
				+ "  shiwake_de3 s"
				+ " LEFT OUTER JOIN bumon_master rbm ON s.rbmn = rbm.futan_bumon_cd"
				+ " LEFT OUTER JOIN torihikisaki_master rto ON s.rtor = rto.torihikisaki_cd"
				+ " LEFT OUTER JOIN kamoku_master rkm ON s.rkmk = rkm.kamoku_gaibu_cd"
				+ " LEFT OUTER JOIN kamoku_edaban_zandaka red ON s.rkmk = red.kamoku_gaibu_cd AND s.reda = red.kamoku_edaban_cd"
				+ " LEFT OUTER JOIN project_master rpj ON s.rprj = rpj.project_cd"
				+ " LEFT OUTER JOIN segment_master rsg ON s.rseg = rsg.segment_cd"
				+ " LEFT OUTER JOIN uf1_ichiran rd1 ON s.rdm1 = rd1.uf1_cd"
				+ " LEFT OUTER JOIN uf2_ichiran rd2 ON s.rdm2 = rd2.uf2_cd"
				+ " LEFT OUTER JOIN uf3_ichiran rd3 ON s.rdm3 = rd3.uf3_cd"
				+ " LEFT OUTER JOIN bumon_master sbm ON s.sbmn = sbm.futan_bumon_cd"
				+ " LEFT OUTER JOIN torihikisaki_master sto ON s.stor = sto.torihikisaki_cd"
				+ " LEFT OUTER JOIN kamoku_master skm ON s.skmk = skm.kamoku_gaibu_cd"
				+ " LEFT OUTER JOIN kamoku_edaban_zandaka sed ON s.skmk = sed.kamoku_gaibu_cd AND s.seda = sed.kamoku_edaban_cd"
				+ " LEFT OUTER JOIN project_master spj ON s.sprj = spj.project_cd"
				+ " LEFT OUTER JOIN segment_master ssg ON s.sseg = ssg.segment_cd"
				+ " LEFT OUTER JOIN uf1_ichiran sd1 ON s.sdm1 = sd1.uf1_cd"
				+ " LEFT OUTER JOIN uf2_ichiran sd2 ON s.sdm2 = sd2.uf2_cd"
				+ " LEFT OUTER JOIN uf3_ichiran sd3 ON s.sdm3 = sd3.uf3_cd"
				+ " WHERE"
				+ " serial_no = ?"
				+ " AND denpyou_id= ?";
		
		return connection.find(sql,serialNo,denpyouId);
	}
	
	/**
	 * 仕訳抽出テーブルを更新する
	 * @param dataMap 更新結果
	 * @return 更新件数(1件)
	 */
	public int updateShiwakeData(GMap dataMap){
		
		final String sql = "UPDATE shiwake_de3 "
						+  "SET "
						+  " koushin_time = current_timestamp,"
						+  " dymd = ?,"
						+  " seiri = ?,"
						+  " dcno = ?,"
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
						+  " tky = ?,"
						+  " tno = ?,"
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
						+  " sten = ?,"
						+  " dkec = ?,"
						+  " kymd = ?,"
						+  " kbmn = ?,"
						+  " kusr = ?,"
						+  " fusr = ?,"
						+  " fsen = ?,"
						+  " sgno = ?,"
						+  " bunri = ?,"
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
						dataMap.get("tky"),
						dataMap.get("tno"),
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
						dataMap.get("sten"),
						dataMap.get("dkec"),
						dataMap.get("kymd"),
						dataMap.get("kbmn"),
						dataMap.get("kusr"),
						dataMap.get("fusr"),
						dataMap.get("fsen"),
						dataMap.get("sgno"),
						dataMap.get("bunri"),
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
		
		final String sql = "UPDATE shiwake_de3 SET "
							+ "koushin_time = current_timestamp, "
							+ "shiwake_status = ? " 
							+ "WHERE serial_no = ? ";
		return connection.update(sql, shiwakeStatus, serialNo);

	}

}
