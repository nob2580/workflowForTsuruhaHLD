package eteam.gyoumu.tsuuchi;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KIHON_MODEL_CD;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.FurikaeDenpyouChuushutsuBat;
import eteam.gyoumu.kaikei.JidouHikiotoshiChuushutsuBat;
import eteam.gyoumu.kaikei.KaigaiRyohiKaribaraiChuushutsuBat;
import eteam.gyoumu.kaikei.KaigaiRyohiSeisanChuushutsuBat;
import eteam.gyoumu.kaikei.KaribaraiChuushutsuBat;
import eteam.gyoumu.kaikei.KeihiTatekaeSeisanChuushutsuBat;
import eteam.gyoumu.kaikei.KoutsuuhiSeisanChuushutsuBat;
import eteam.gyoumu.kaikei.RyohiKaribaraiChuushutsuBat;
import eteam.gyoumu.kaikei.RyohiSeisanChuushutsuBat;
import eteam.gyoumu.kaikei.SeikyuushoBaraiChuushutsuBat;
import eteam.gyoumu.kaikei.ShiharaiIraiChuushutsuBat;
import eteam.gyoumu.kaikei.SougouTsukekaeDenpyouChuushutsuBat;
import eteam.gyoumu.kaikei.TsuukinTeikiChuushutsuBat;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧参照用テーブル登録・更新用ロジック
 */
@Getter @Setter @ToString
public class DenpyouIchiranUpdateLogic extends EteamAbstractLogic {
	
	
	/** 処理便宜上の伝票区分：ユーザー定義届書 */
	protected static final String DUMMY_DENPYOU_KBN_KANI_TODOKE = "B000";
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(TsuuchiCategoryLogic.class);
	/** WHERE句(伝票・共通) */
	protected static String WHERE_DENPYOU_COMMON = "<DENPYOU_COMMON>";
	/** WHERE句(伝票・個別) */
	protected static String WHERE_DENPYOU_INDI = "<DENPYOU_INDI>";
	/** WHERE句(支給一覧・共通) */
	protected static String WHERE_SHIKYUU_COMMON = "<SHIKYUU_COMMON>";
	/** WHERE句(支給一覧・個別) */
	protected static String WHERE_SHIKYUU_INDI = "<SHIKYUU_INDI>";
	/** WHERE句(法人明細・共通) */
	protected static String WHERE_HOUJIN_COMMON = "<HOUJIN_COMMON>";
	/** WHERE句(法人明細・個別) */
	protected static String WHERE_HOUJIN_INDI = "<HOUJIN_INDI>";
	
	/**
	 * 全伝票ID数を取得。
	 * @return 全伝票ID数
	 */
	public long countAllDenpyouId(){
		String sql = "SELECT count(denpyou_id) AS cnt FROM denpyou d"
				+ " INNER JOIN denpyou_shubetsu_ichiran i ON i.denpyou_kbn = d.denpyou_kbn";
		GMap mp = connection.find(sql);
		long cnt = (long)mp.get("cnt");
		return cnt;
	}
	
	/**
	 * 全伝票IDを取得。
	 * @return 全伝票IDリスト
	 */
	public List<GMap> loadAllDenpyouId(){
		String sql = "SELECT denpyou_id FROM denpyou d"
				+ " INNER JOIN denpyou_shubetsu_ichiran i ON i.denpyou_kbn = d.denpyou_kbn";
		return connection.load(sql);
	}

	/**
	 * 指定した伝票IDのデータを伝票一覧テーブルから削除する。
	 * @param denpyouId 伝票番号
	 */
	public void deleteDenpyouIchiran(String denpyouId){
		String sqlupd = "DELETE FROM denpyou_ichiran d WHERE denpyou_id = ? ";
		connection.update(sqlupd,denpyouId);
	}
		
	/**
	 * 指定した伝票IDのデータを伝票一覧テーブルに登録する。(共通表示部分)
	 * @param denpyouId 伝票番号
	 */
	public void insertDenpyouIchiran(String denpyouId){
		String sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		GMap map = connection.find(sql, denpyouId);
		if(map == null)return; //伝票個別テーブルにデータが存在してdenpyouにデータが存在しない場合があるので念のため確認

		DenpyouIchiranData dd = new DenpyouIchiranData();
		
		String sqlupd = "";
		List<Object> paramsUpd = new ArrayList<Object>();
		paramsUpd.add(denpyouId);
		paramsUpd.add(dd.com.name);
		paramsUpd.add(dd.com.denpyouKbn);
		paramsUpd.add(dd.lstJissiKianBangou);
		paramsUpd.add(dd.lstShishutsuKianBangou);
		paramsUpd.add(dd.lstYosanShikkouTaishou);
		paramsUpd.add(dd.lstYosanCheckNengetsu);
		paramsUpd.add(dd.com.serialNo);
		paramsUpd.add(dd.com.denpyouShubetsu_url);
		paramsUpd.add(dd.com.tourokuTime);
		paramsUpd.add(dd.com.bumonFullName);
		paramsUpd.add(dd.com.userFullName);
		paramsUpd.add(dd.com.userId);
		paramsUpd.add(dd.com.denpyouJoutai);
		paramsUpd.add(dd.com.koushinTime);
		paramsUpd.add(dd.com.shouninbi);
		paramsUpd.add(dd.com.maruhiFlg);
		paramsUpd.add(dd.com.allCnt);
		paramsUpd.add(dd.com.curCnt);
		paramsUpd.add(dd.com.zanCnt);
		paramsUpd.add(dd.com.genBumonFullName);
		paramsUpd.add(dd.com.genUserFullName);
		paramsUpd.add(dd.com.genGyoumuRoleName);
		paramsUpd.add(dd.com.genName);
		paramsUpd.add(dd.lstVersion);
		paramsUpd.add(dd.lstKingaku);
		paramsUpd.add(dd.lstGaika);
		paramsUpd.add(dd.lstHoujinKingaku);
		paramsUpd.add(dd.lstTehaiKingaku);
		paramsUpd.add(dd.lstTorihikisaki1);
		paramsUpd.add(dd.lstShiharaibi);
		paramsUpd.add(dd.lstShiharaikiboubi);
		paramsUpd.add(dd.lstShiharaihouhou);
		paramsUpd.add(dd.lstSashihikiShikyuuKingaku);
		paramsUpd.add(dd.lstKeijoubi);
		paramsUpd.add(dd.lstShiwakeKeijoubi);
		paramsUpd.add(dd.lstSeisanYoteibi);
		paramsUpd.add(dd.lstKaribaraiDenpyouId);
		paramsUpd.add(dd.lstHoumonsaki);
		paramsUpd.add(dd.lstMokuteki);
		paramsUpd.add(dd.lstKenmei);
		paramsUpd.add(dd.lstNaiyou);
		paramsUpd.add(dd.lstUserSei);
		paramsUpd.add(dd.lstUserMei);
		paramsUpd.add(dd.lstSeisankikanFrom);
		paramsUpd.add(dd.lstSeisankikanTo);
		paramsUpd.add(dd.com.genUserId);
		paramsUpd.add(dd.com.genGyoumuRoleId);
		paramsUpd.add(dd.lstKianBangouUnyouFlg);
		paramsUpd.add(dd.lstYosanShikkouTaishouCd);
		paramsUpd.add(dd.lstKianSyuryoFlg);
		paramsUpd.add(connection.getArray(dd.lstFutanbumonCd));
		paramsUpd.add(connection.getArray(dd.lstKariFutanbumonCd));
		paramsUpd.add(connection.getArray(dd.lstKariKamokuCd));
		paramsUpd.add(connection.getArray(dd.lstKariKamokuEdabanCd));
		paramsUpd.add(connection.getArray(dd.lstKariTorihikisakiCd));
		paramsUpd.add(connection.getArray(dd.lstKashiFutanbumonCd));
		paramsUpd.add(connection.getArray(dd.lstKashiKamokuCd));
		paramsUpd.add(connection.getArray(dd.lstKashiKamokuEdabanCd));
		paramsUpd.add(connection.getArray(dd.lstKashiTorihikisakiCd));
		paramsUpd.add(connection.getDecimalArray(dd.lstMeisaiKingaku));
		paramsUpd.add(dd.lstTekiyou);
		paramsUpd.add(dd.lstHoujinCardUse);
		paramsUpd.add(dd.lstKaishaTehaiUse);
		paramsUpd.add(dd.lstRyoushuushoExist);
		paramsUpd.add(dd.lstMiseisanKaribaraiExist);
		paramsUpd.add(dd.lstMiseisanUkagaiExist);
		paramsUpd.add(dd.com.shiwakeStatus);
		paramsUpd.add(dd.com.fbStatus);
		paramsUpd.add(dd.lstJisshiNendo);
		paramsUpd.add(dd.lstShishutsuNendo);
		paramsUpd.add(dd.com.bumonCd);
		paramsUpd.add(dd.lstKianBangouInput);
		//インボイス対応タイミング新規項目
		paramsUpd.add(connection.getArray(dd.lstJigyoushakbn));
		paramsUpd.add(dd.com.shain_no);
		paramsUpd.add(connection.getArray(dd.lstShiharaiName));
		paramsUpd.add(connection.getDecimalArray(dd.lstZeinukiMeisaiKingaku));
		paramsUpd.add(dd.lstZeinukiKingaku);
		//insert
		sqlupd = ""
				+ "INSERT INTO denpyou_ichiran( "
			    + " denpyou_id, "
			    + " name, "
			    + " denpyou_kbn, "
			    + " jisshi_kian_bangou, "
			    + " shishutsu_kian_bangou, "
			    + " yosan_shikkou_taishou, "
			    + " yosan_check_nengetsu, "
			    + " serial_no, "
			    + " denpyou_shubetsu_url, "
			    + " touroku_time, "
			    + " bumon_full_name, "
			    + " user_full_name, "
			    + " user_id, "
			    + " denpyou_joutai, "
			    + " koushin_time, "
			    + " shouninbi, "
			    + " maruhi_flg, "
			    + " all_cnt, "
			    + " cur_cnt, "
			    + " zan_cnt, "
			    + " gen_bumon_full_name, "
			    + " gen_user_full_name, "
			    + " gen_gyoumu_role_name, "
			    + " gen_name, "
			    + " version, "
			    + " kingaku, "
			    + " gaika, "
			    + " houjin_kingaku, "
			    + " tehai_kingaku, "
			    + " torihikisaki1, "
			    + " shiharaibi, "
			    + " shiharaikiboubi, "
			    + " shiharaihouhou, "
			    + " sashihiki_shikyuu_kingaku, "
			    + " keijoubi, "
			    + " shiwakekeijoubi, "
			    + " seisan_yoteibi, "
			    + " karibarai_denpyou_id, "
			    + " houmonsaki, "
			    + " mokuteki, "
			    + " kenmei, "
			    + " naiyou, "
			    + " user_sei, "
			    + " user_mei, "
			    + " seisankikan_from, "
			    + " seisankikan_to, "
			    + " gen_user_id, "
			    + " gen_gyoumu_role_id, "
			    + " kian_bangou_unyou_flg, "
			    + " yosan_shikkou_taishou_cd, "
			    + " kian_syuryou_flg, "
			    + " futan_bumon_cd, "
			    + " kari_futan_bumon_cd, "
			    + " kari_kamoku_cd, "
			    + " kari_kamoku_edaban_cd, "
			    + " kari_torihikisaki_cd, "
			    + " kashi_futan_bumon_cd, "
			    + " kashi_kamoku_cd, "
			    + " kashi_kamoku_edaban_cd, "
			    + " kashi_torihikisaki_cd, "
			    + " meisai_kingaku, "
			    + " tekiyou, "
			    + " houjin_card_use, "
			    + " kaisha_tehai_use, "
			    + " ryoushuusho_exist, "
			    + " miseisan_karibarai_exist, "
			    + " miseisan_ukagai_exist, "
			    + " shiwake_status, "
			    + " fb_status, "
			    + " jisshi_nendo, "
			    + " shishutsu_nendo, "
			    + " bumon_cd, "
			    + " kian_bangou_input, "
			    + " jigyousha_kbn, "
			    + " shain_no, "
			    + " shiharai_name, "
			    + " zeinuki_meisai_kingaku, "
			    + " zeinuki_kingaku "
			    + ") "
			    + "VALUES( "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?, "
			    + " ?,?,?,?,? "
			    + ") ";
		//テーブルのカラム　要確認
		connection.update(sqlupd, paramsUpd.toArray());
	}

	/**
	 * 伝票一覧テーブルの指定した伝票IDのデータを更新する。(共通表示部分)
	 * @param denpyouId 伝票番号
	 */
	public void updateDenpyouIchiran(String denpyouId){

		//コネクションのコミットは本メソッドの外側で実行すること
		String denpyouKbn = denpyouId.substring(7, 11);

		DenpyouIchiranData dd = makeDenpyouIchiranData(denpyouId, denpyouKbn);
		if (dd == null)
		{
			return;
		} //表示用のデータが存在しない場合があるので念のため確認

		//--------------------
		//伝票一覧テーブルをアップデート
		//--------------------
		String sqlupd = "";
		List<Object> paramsUpd = new ArrayList<Object>();
		paramsUpd.add(dd.com.name);
		paramsUpd.add(dd.com.denpyouKbn);
		paramsUpd.add(dd.lstJissiKianBangou);
		paramsUpd.add(dd.lstShishutsuKianBangou);
		paramsUpd.add(dd.lstYosanShikkouTaishou);
		paramsUpd.add(dd.lstYosanCheckNengetsu);
		paramsUpd.add(dd.com.serialNo);
		paramsUpd.add(dd.com.denpyouShubetsu_url);
		paramsUpd.add(dd.com.tourokuTime);
		paramsUpd.add(dd.com.bumonFullName);
		paramsUpd.add(dd.com.userFullName);
		paramsUpd.add(dd.com.userId);
		paramsUpd.add(dd.com.denpyouJoutai);
		paramsUpd.add(dd.com.koushinTime);
		paramsUpd.add(dd.com.shouninbi);
		paramsUpd.add(dd.com.maruhiFlg);
		paramsUpd.add(dd.com.allCnt);
		paramsUpd.add(dd.com.curCnt);
		paramsUpd.add(dd.com.zanCnt);
		paramsUpd.add(dd.com.genBumonFullName);
		paramsUpd.add(dd.com.genUserFullName);
		paramsUpd.add(dd.com.genGyoumuRoleName);
		paramsUpd.add(dd.com.genName);
		paramsUpd.add(dd.lstVersion);
		paramsUpd.add(dd.lstKingaku);
		paramsUpd.add(dd.lstGaika);
		paramsUpd.add(dd.lstHoujinKingaku);
		paramsUpd.add(dd.lstTehaiKingaku);
		paramsUpd.add(dd.lstTorihikisaki1);
		paramsUpd.add(dd.lstShiharaibi);
		paramsUpd.add(dd.lstShiharaikiboubi);
		paramsUpd.add(dd.lstShiharaihouhou);
		paramsUpd.add(dd.lstSashihikiShikyuuKingaku);
		paramsUpd.add(dd.lstKeijoubi);
		paramsUpd.add(dd.lstShiwakeKeijoubi);
		paramsUpd.add(dd.lstSeisanYoteibi);
		paramsUpd.add(dd.lstKaribaraiDenpyouId);
		paramsUpd.add(dd.lstHoumonsaki);
		paramsUpd.add(dd.lstMokuteki);
		paramsUpd.add(dd.lstKenmei);
		paramsUpd.add(dd.lstNaiyou);
		paramsUpd.add(dd.lstUserSei);
		paramsUpd.add(dd.lstUserMei);
		paramsUpd.add(dd.lstSeisankikanFrom);
		paramsUpd.add(dd.lstSeisankikanTo);
		paramsUpd.add(dd.com.genUserId);
		paramsUpd.add(dd.com.genGyoumuRoleId);
		paramsUpd.add(dd.lstKianBangouUnyouFlg);
		paramsUpd.add(dd.lstYosanShikkouTaishouCd);
		paramsUpd.add(dd.lstKianSyuryoFlg);
		paramsUpd.add(connection.getArray(dd.lstFutanbumonCd));
		paramsUpd.add(connection.getArray(dd.lstKariFutanbumonCd));
		paramsUpd.add(connection.getArray(dd.lstKariKamokuCd));
		paramsUpd.add(connection.getArray(dd.lstKariKamokuEdabanCd));
		paramsUpd.add(connection.getArray(dd.lstKariTorihikisakiCd));
		paramsUpd.add(connection.getArray(dd.lstKashiFutanbumonCd));
		paramsUpd.add(connection.getArray(dd.lstKashiKamokuCd));
		paramsUpd.add(connection.getArray(dd.lstKashiKamokuEdabanCd));
		paramsUpd.add(connection.getArray(dd.lstKashiTorihikisakiCd));
		paramsUpd.add(connection.getDecimalArray(dd.lstMeisaiKingaku));
		paramsUpd.add(dd.lstTekiyou);
		paramsUpd.add(dd.lstHoujinCardUse);
		paramsUpd.add(dd.lstKaishaTehaiUse);
		paramsUpd.add(dd.lstRyoushuushoExist);
		paramsUpd.add(dd.lstMiseisanKaribaraiExist);
		paramsUpd.add(dd.lstMiseisanUkagaiExist);
		paramsUpd.add(dd.com.shiwakeStatus);
		paramsUpd.add(dd.com.fbStatus);
		paramsUpd.add(dd.lstJisshiNendo);
		paramsUpd.add(dd.lstShishutsuNendo);
		paramsUpd.add(dd.com.bumonCd);
		paramsUpd.add(dd.lstKianBangouInput);
		//インボイス等対応
		paramsUpd.add(connection.getArray(dd.lstJigyoushakbn));
		paramsUpd.add(dd.com.shain_no);
		paramsUpd.add(connection.getArray(dd.lstShiharaiName));
		paramsUpd.add(connection.getDecimalArray(dd.lstZeinukiMeisaiKingaku));
		paramsUpd.add(dd.lstZeinukiKingaku);
		paramsUpd.add(denpyouId);
		sqlupd = ""
				+ "UPDATE denpyou_ichiran SET "
			    + " name = ? ,"
			    + " denpyou_kbn = ? ,"
			    + " jisshi_kian_bangou = ? ,"
			    + " shishutsu_kian_bangou = ? ,"
			    + " yosan_shikkou_taishou = ? ,"
			    + " yosan_check_nengetsu = ? ,"
			    + " serial_no = ? ,"
			    + " denpyou_shubetsu_url = ? ,"
			    + " touroku_time = ? ,"
			    + " bumon_full_name = ? ,"
			    + " user_full_name = ? ,"
			    + " user_id = ? ,"
			    + " denpyou_joutai = ? ,"
			    + " koushin_time = ? ,"
			    + " shouninbi = ? ,"
			    + " maruhi_flg = ? ,"
			    + " all_cnt = ? ,"
			    + " cur_cnt = ? ,"
			    + " zan_cnt = ? ,"
			    + " gen_bumon_full_name = ? ,"
			    + " gen_user_full_name = ? ,"
			    + " gen_gyoumu_role_name = ? ,"
			    + " gen_name = ? ,"
			    + " version = ? ,"
			    + " kingaku = ? ,"
			    + " gaika = ? ,"
			    + " houjin_kingaku = ? ,"
			    + " tehai_kingaku = ? ,"
			    + " torihikisaki1 = ? ,"
			    + " shiharaibi = ? ,"
			    + " shiharaikiboubi = ? ,"
			    + " shiharaihouhou = ? ,"
			    + " sashihiki_shikyuu_kingaku = ? ,"
			    + " keijoubi = ? ,"
			    + " shiwakekeijoubi = ? ,"
			    + " seisan_yoteibi = ? ,"
			    + " karibarai_denpyou_id = ? ,"
			    + " houmonsaki = ? ,"
			    + " mokuteki = ? ,"
			    + " kenmei = ? ,"
			    + " naiyou = ? ,"
			    + " user_sei = ? ,"
			    + " user_mei = ? ,"
			    + " seisankikan_from = ? ,"
			    + " seisankikan_to = ? ,"
			    + " gen_user_id = ? ,"
			    + " gen_gyoumu_role_id = ? ,"
			    + " kian_bangou_unyou_flg = ? ,"
			    + " yosan_shikkou_taishou_cd = ? ,"
			    + " kian_syuryou_flg = ? ,"
			    + " futan_bumon_cd = ? ,"
			    + " kari_futan_bumon_cd = ? ,"
			    + " kari_kamoku_cd = ? ,"
			    + " kari_kamoku_edaban_cd = ? ,"
			    + " kari_torihikisaki_cd = ? ,"
			    + " kashi_futan_bumon_cd = ? ,"
			    + " kashi_kamoku_cd = ? ,"
			    + " kashi_kamoku_edaban_cd = ? ,"
			    + " kashi_torihikisaki_cd = ? ,"
			    + " meisai_kingaku = ? ,"
			    + " tekiyou = ? ,"
			    + " houjin_card_use = ? ,"
			    + " kaisha_tehai_use = ? ,"
			    + " ryoushuusho_exist = ? ,"
			    + " miseisan_karibarai_exist = ? ,"
			    + " miseisan_ukagai_exist = ? ,"
			    + " shiwake_status = ? ,"
			    + " fb_status = ? ,"
			    + " jisshi_nendo = ? ,"
			    + " shishutsu_nendo = ? ,"
			    + " bumon_cd = ? ,"
			    + " kian_bangou_input = ? ,"
			    + " jigyousha_kbn = ? ,"
			    + " shain_no = ? ,"
			    + " shiharai_name = ? ,"
			    + " zeinuki_meisai_kingaku = ? ,"
			    + " zeinuki_kingaku = ? "
			    + " WHERE denpyou_id = ? ";
		connection.update(sqlupd, paramsUpd.toArray());
		
		
		//--------------------
		//仮払伝票がある場合、それも更新
		//--------------------
		String tmpKaribaraidenpyouId = dd.lstKaribaraiDenpyouId;
		if( ((DENPYOU_KBN.KEIHI_TATEKAE_SEISAN).equals(denpyouKbn)
			  || (DENPYOU_KBN.RYOHI_SEISAN).equals(denpyouKbn)
			  || (DENPYOU_KBN.KAIGAI_RYOHI_SEISAN).equals(denpyouKbn) )
			    && (!tmpKaribaraidenpyouId.isEmpty()) ){
			String sql = "SELECT * FROM denpyou_ichiran WHERE denpyou_id = ?";
			GMap map = connection.find(sql,tmpKaribaraidenpyouId);
			if(map == null){insertDenpyouIchiran(tmpKaribaraidenpyouId);}
			updateDenpyouIchiran(tmpKaribaraidenpyouId);
		}
	}
	
	/**
	 * 伝票一覧データを作成する
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return 伝票一覧データ
	 */
	protected DenpyouIchiranData makeDenpyouIchiranData(String denpyouId, String denpyouKbn) {
		WorkflowCategoryLogic wfLg = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		WorkflowEventControlLogic wfELg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		String sql = "";
		List<GMap> lst;
		
		sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		GMap map = connection.find(sql, denpyouId);
		if(map == null)return null; //伝票個別テーブルにデータが存在してdenpyouにデータが存在しない場合があるので念のため確認
		String denpyouStatus = map.get("denpyou_joutai");

		map = this.getDenpyouIchiranDat(denpyouId);
		if(map == null)return null; //表示用のデータが存在しない場合があるので念のため確認
		
		DenpyouIchiranData dd = new DenpyouIchiranData();

		//--------------------
		//起案番号等
		//--------------------
		dd.lstJissiKianBangou = map.get("jisshi_kian_bangou") == null ? "" : (String)map.get("jisshi_kian_bangou");
		dd.lstShishutsuKianBangou = map.get("shishutsu_kian_bangou") == null ? "" : (String)map.get("shishutsu_kian_bangou");
		dd.lstYosanShikkouTaishou = map.get("yosan_shikkou_taishou") == null ? "" : (String)map.get("yosan_shikkou_taishou");
		dd.lstYosanCheckNengetsu = map.get("yosan_check_nengetsu") == null ? "" : map.get("yosan_check_nengetsu");

		
		//--------------------
		//WF共通項目へのマッピング
		//--------------------
		setWfCom(denpyouId, denpyouKbn, dd.com);

		
		//--------------------
		//処理済件数 / 総件数 ※基本モデルが「承認者」で承認必須 または業務ロールのみをカウント
		//--------------------
		int allCount = 0;
		int sumiCount = 0;
		List<GMap> routeList = wfLg.selectShouninRoute(denpyouId);
		for(GMap route : routeList){
			if(!(boolean)route.get("isGougi")){
				if(((int)route.get("edano") == 1 || route.get("saishu_shounin_flg").equals("1") || (route.get("kihon_model_cd").equals(KIHON_MODEL_CD.SHOUNI) && route.get("shounin_hissu_flg").equals("1")))){
					allCount++;
					if(route.get("joukyou_edano") != null){
						sumiCount++;
					}
				}
			}else{
				List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
				for(GMap gougiOya : gougiOyaList){
					List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
					for(GMap gougiKo : gougiKoList){
						if(gougiKo.get("kihon_model_cd").equals(KIHON_MODEL_CD.SHOUNI) && gougiKo.get("shounin_hissu_flg").equals("1")){
							allCount++;
							if(gougiKo.get("joukyou_edano") != null || wfELg.gougiKoFinished(routeList, gougiKo)){
								sumiCount++;
							}
						}
					}
				}
				
			}
		}
		if (dd.com.denpyouJoutai.compareTo(DENPYOU_JYOUTAI.SHINSEI_CHUU) > 0)
		{
			sumiCount = allCount;
		}
		dd.com.allCnt = allCount;
		dd.com.curCnt = sumiCount;
		dd.com.zanCnt = allCount - sumiCount;


		//--------------------
		//現在作業者等
		//--------------------
		//該当伝票の承認対象ユーザー数を取得(合議内ユーザーもそれぞれ1人とカウント)
		sql = ""
		+ "SELECT "
		+ "  sh.bumon_full_name, "
		+ "  sh.user_full_name, "
		+ "  sh.gyoumu_role_name, "
		+ "  array_to_string(array_agg(sk.bumon_full_name),'｜') as gougi_bumon_full_name, "
		+ "  array_to_string(array_agg(sk.user_full_name),'｜') as gougi_user_full_name "
		+ "FROM (SELECT * FROM shounin_route WHERE denpyou_id = ? AND genzai_flg='1') sh "
		+ "LEFT OUTER  JOIN shounin_route_gougi_ko sk ON "
		+ "  sh.denpyou_id = sk.denpyou_id "
		+ "  AND sh.edano = sk.edano "
		+ "  AND sk.gougi_genzai_flg = '1' "
		+ "GROUP BY sh.edano, sh.bumon_full_name, sh.user_full_name, sh.gyoumu_role_name ";
		map = connection.find(sql, denpyouId);
		if(map != null){
			dd.com.genBumonFullName = !isEmpty(map.get("gougi_bumon_full_name")) ? (String)map.get("gougi_bumon_full_name") : (String)map.get("bumon_full_name");
			dd.com.genUserFullName = !isEmpty(map.get("gougi_user_full_name")) ? (String)map.get("gougi_user_full_name") : (String)map.get("user_full_name");
			dd.com.genGyoumuRoleName = !isEmpty(map.get("gyoumu_role_name")) ? (String)map.get("gyoumu_role_name") : "";
			dd.com.genName = !isEmpty(dd.com.genGyoumuRoleName) ? dd.com.genGyoumuRoleName : dd.com.genUserFullName;
		}
		
		//関連伝票(現在承認者)に関するデータの登録
		sql = "";
		sql += "SELECT sh.user_id AS shusr, sk.user_id AS skusr, sh.gyoumu_role_id FROM shounin_route sh ";
		sql += " LEFT OUTER JOIN (SELECT denpyou_id,min(edano) as genedano FROM shounin_route WHERE genzai_flg = '1' GROUP BY denpyou_id) shc  ";
		sql += "  ON sh.denpyou_id = shc.denpyou_id  ";
		sql += " LEFT OUTER JOIN shounin_route_gougi_ko sk  ";
		sql += "  ON sh.denpyou_id = sk.denpyou_id  ";
		sql += "  AND sh.edano = sk.edano ";
		sql += " LEFT OUTER JOIN  ";
		sql += "  (SELECT denpyou_id, edano,gougi_edano, bumon_role_id as gen_bri, min(gougi_edaedano) as minedaedano FROM shounin_route_gougi_ko  ";
		sql += "  WHERE gougi_genzai_flg = '1' GROUP BY denpyou_id, edano, gougi_edano, gen_bri) skg  ";
		sql += "  ON sk.denpyou_id = skg.denpyou_id AND sk.edano = skg.edano AND sk.gougi_edano = skg.gougi_edano  ";
		sql += " WHERE sh.denpyou_id = ?  ";
		sql += " AND ( ";
		sql += "         (sh.genzai_flg = '1' AND sk.gougi_edano IS NULL) "; //非合議で現在フラグが1
		sql += "      OR (sh.genzai_flg = '1' AND sk.gougi_genzai_flg = '1') "; //合議で合議現在フラグが1
		sql += "      OR (sh.genzai_flg = '0' AND sk.gougi_edano IS NULL AND sh.edano < shc.genedano AND sh.joukyou_edano IS NULL) "; //非合議で状況枝番なし
		sql += "      OR (sh.genzai_flg = '0' AND sk.gougi_edano IS NOT NULL AND sh.edano < shc.genedano AND sk.gougi_genzai_flg = '0' AND (sk.kihon_model_cd = '2' OR sk.kihon_model_cd = '3') AND sk.joukyou_edano IS NULL) "; //合議で現在が自分以降で状況枝番なしで基本モデルが2か3
		sql += "      OR (sh.genzai_flg = '1' AND sk.gougi_edano IS NOT NULL AND sh.edano < shc.genedano AND sk.gougi_edaedano < skg.minedaedano AND sk.gougi_genzai_flg = '0' AND (sk.kihon_model_cd = '2' OR sk.kihon_model_cd = '3') AND sk.joukyou_edano IS NULL) "; //合議で全体現在が自分で状況枝番なしで現在合議が自分以降で基本モデルが2か3
		sql += "     )";
		//現在承認者ユーザーID
		//現在承認者業務ロールID
		lst = connection.load(sql,denpyouId);
		for(GMap mp : lst){
			dd.com.genUserId = readDataAndAdd(dd.com.genUserId, "shusr", mp);
			dd.com.genUserId = readDataAndAdd(dd.com.genUserId, "skusr", mp);
			dd.com.genGyoumuRoleId = readDataAndAdd(dd.com.genGyoumuRoleId, "gyoumu_role_id", mp);
		}

		
		//--------------------
		//予算執行関連項目へのマッピング
		//--------------------
		
		//予算執行対象に関するデータの登録
		//起案終了 実施起案年度 支出起案年度 起案伝票ID
		//denpyou_kian_himozukeから取得
		sql = "SELECT ryakugou,kian_syuryo_flg,jisshi_nendo,shishutsu_nendo,kian_denpyou FROM denpyou_kian_himozuke WHERE denpyou_id = ?";
		map = connection.find(sql,denpyouId);
		dd.lstKianSyuryoFlg = "0";
		if(map != null){
			if( map.get("kian_syuryo_flg") != null && !((String)map.get("kian_syuryo_flg")).isEmpty() ){
				dd.lstKianSyuryoFlg = (String)map.get("kian_syuryo_flg");
			}
			if( map.get("jisshi_nendo") != null && !((String)map.get("jisshi_nendo")).isEmpty() ){
				dd.lstJisshiNendo = (String)map.get("jisshi_nendo");
			}
			if( map.get("shishutsu_nendo") != null && !((String)map.get("shishutsu_nendo")).isEmpty() ){
				dd.lstShishutsuNendo = (String)map.get("shishutsu_nendo");
			}

			if( map.get("ryakugou") != null && !((String)map.get("ryakugou")).isEmpty() ){
				dd.lstKianBangouInput = "1";
			}else{
				dd.lstKianBangouInput = "0";
			}
		}
		
		//起案番号運用フラグ 予算執行対象
		//denpyou_shubetsu_ichiranから取得
		sql = "SELECT kianbangou_unyou_flg, yosan_shikkou_taishou FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn = ?";
		map = connection.find(sql,denpyouKbn);
		if(map != null){
			if( !((String)map.get("kianbangou_unyou_flg")).isEmpty() ){
				dd.lstKianBangouUnyouFlg = (String)map.get("kianbangou_unyou_flg");
			}
			if( !((String)map.get("yosan_shikkou_taishou")).isEmpty() ){
				dd.lstYosanShikkouTaishouCd = (String)map.get("yosan_shikkou_taishou");
			}
		}

		
		//--------------------
		//伝票区分別の項目マッピング
		//--------------------

		//それぞれの伝票種別についてSQL発行して該当分データを取得する
		sql = "";
		BigDecimal zeinukiSum = new BigDecimal(0);
		switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN   	:
				//指定伝票ID分のデータをkeihiseisanとkeihiseisan_meisaiから取得
				sql = " SELECT * FROM keihiseisan WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstKingaku = (BigDecimal)map.get("shiharai_kingaku_goukei");
				dd.lstHoujinKingaku = (BigDecimal)map.get("houjin_card_riyou_kingaku");
				dd.lstTehaiKingaku = (BigDecimal)map.get("kaisha_tehai_kingaku");
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("sashihiki_shikyuu_kingaku");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				dd.lstKaribaraiDenpyouId = (String)map.get("karibarai_denpyou_id");
				
				dd.lstZeinukiKingaku = (BigDecimal)map.get("hontai_kingaku_goukei");
				
				sql = " SELECT * FROM keihiseisan_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.getString("kari_futan_bumon_cd"));
					dd.lstRyoushuushoExist = readDataAndAdd(dd.lstRyoushuushoExist, "shouhyou_shorui_flg", mp);
					dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "torihikisaki_name_ryakushiki", mp);
					if( "1".equals(mp.get("houjin_card_riyou_flg")) ){ dd.lstHoujinCardUse = "1";}
					if( "1".equals(mp.get("kaisha_tehai_flg")) ){ dd.lstKaishaTehaiUse = "1";}
					if( mp.get("denpyou_edano") != null && (Integer)mp.get("denpyou_edano") == 1 ){
						dd.lstKeijoubi = (Date)mp.get("shiyoubi");
						dd.lstUserSei = (String)mp.get("user_sei");
						dd.lstUserMei = (String)mp.get("user_mei");
					}
					dd.lstZeinukiMeisaiKingaku.add(mp.get("hontai_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
					dd.lstShiharaiName.add(mp.get("shiharaisaki_name"));
				}

				if (dd.lstHoujinCardUse.isEmpty())
				{
					dd.lstHoujinCardUse = "0";
				} //法人カード使用フラグ
				if (dd.lstKaishaTehaiUse.isEmpty())
				{
					dd.lstKaishaTehaiUse = "0";
				} //会社手配使用フラグ
				
				break;
				
			case DENPYOU_KBN.KARIBARAI_SHINSEI   	:
				//指定伝票ID分のデータをkaribaraiから取得
				sql = " SELECT * FROM karibarai WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				String karibaraiOn = (String)(map.get("karibarai_on"));
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				if("1".equals(karibaraiOn)){
					dd.lstKingaku = (BigDecimal)map.get("karibarai_kingaku");
					dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("karibarai_kingaku");
				}else{
					dd.lstKingaku = (BigDecimal)map.get("kingaku");
					dd.lstSashihikiShikyuuKingaku   = null;
				}
				dd.lstMeisaiKingaku.add(dd.lstKingaku);
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstSeisanYoteibi = (Date)map.get("seisan_yoteibi");
				
				//未精算仮払伝票フラグ
				if( !("40".equals(denpyouStatus)) && !("90".equals(denpyouStatus)) ){
					sql = "SELECT count(karibarai_denpyou_id) AS cnt FROM keihiseisan seisan "
							+ " INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id "
							+ " WHERE d.denpyou_joutai = '30' AND seisan.karibarai_denpyou_id = ? ";
					map = connection.find(sql,denpyouId);
					dd.lstMiseisanKaribaraiExist = ( karibaraiOn.equals("1") && (long)(map.get("cnt")) == 0 ) ? "1" : "";
					dd.lstMiseisanUkagaiExist  = ( karibaraiOn.equals("0") && (long)(map.get("cnt")) == 0 ) ? "1" : "";
				}
				
				break;
				
			case DENPYOU_KBN.SEIKYUUSHO_BARAI   	:
				//指定伝票ID分のデータをseikyuushobaraiとseikyuushobarai_meisaiから取得
				sql = " SELECT * FROM seikyuushobarai WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstKingaku = (BigDecimal)map.get("shiharai_kingaku_goukei");
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharai_kigen");
				dd.lstShiharaihouhou = "2";
				dd.lstKeijoubi = (Date)map.get("keijoubi");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				dd.lstRyoushuushoExist = (String)map.get("shouhyou_shorui_flg");
				
				dd.lstZeinukiKingaku = (BigDecimal)map.get("hontai_kingaku_goukei");
				
				sql = " SELECT * FROM seikyuushobarai_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
					dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "torihikisaki_name_ryakushiki", mp);
					dd.lstZeinukiMeisaiKingaku.add(mp.get("hontai_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
				}
				
				break;
				
			case DENPYOU_KBN.SIHARAIIRAI:
				sql = " SELECT * FROM shiharai_irai WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstKingaku = ((BigDecimal)map.get("shiharai_goukei")).subtract((BigDecimal)map.get("sousai_goukei"));
				dd.lstSashihikiShikyuuKingaku = (BigDecimal)map.get("sashihiki_shiharai");
				dd.lstShiharaibi = (Date)map.get("yoteibi");
				dd.lstShiharaikiboubi = (Date)map.get("yoteibi");
				dd.lstKeijoubi = (Date)map.get("keijoubi");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				dd.lstTorihikisaki1 = (String)map.get("torihikisaki_name_ryakushiki");
				
				dd.lstJigyoushakbn.add(map.get("jigyousha_kbn"));
				
				sql = " SELECT * FROM shiharai_irai_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
					dd.lstZeinukiMeisaiKingaku.add(mp.get("zeinuki_kingaku"));
					zeinukiSum = zeinukiSum.add(mp.get("zeinuki_kingaku"));
				}
				dd.lstZeinukiKingaku = zeinukiSum;

				break;
				
			case DENPYOU_KBN.RYOHI_SEISAN   	:
				//指定伝票ID分のデータをryohiseisanとryohiseisan_meisaiとryohiseisan_keihi_meisaiから取得
				
				sql = " SELECT * FROM ryohiseisan WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				dd.lstKingaku = (BigDecimal)map.get("goukei_kingaku");
				dd.lstHoujinKingaku = (BigDecimal)map.get("houjin_card_riyou_kingaku");
				dd.lstTehaiKingaku = (BigDecimal)map.get("kaisha_tehai_kingaku");
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("sashihiki_shikyuu_kingaku");
				dd.lstKeijoubi = (Date)map.get("seisankikan_to");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				dd.lstKaribaraiDenpyouId = (String)map.get("karibarai_denpyou_id");
				dd.lstHoumonsaki = (String)map.get("houmonsaki");
				dd.lstMokuteki = (String)map.get("mokuteki");
				dd.lstUserSei = (String)map.get("user_sei");
				dd.lstUserMei = (String)map.get("user_mei");
				dd.lstSeisankikanFrom = (Date)map.get("seisankikan_from");
				dd.lstSeisankikanTo = (Date)map.get("seisankikan_to");
				
				sql = " SELECT * FROM ryohiseisan_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("meisai_kingaku"));
					dd.lstZeinukiMeisaiKingaku.add(mp.get("zeinuki_kingaku"));
					zeinukiSum = zeinukiSum.add(mp.get("zeinuki_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
					dd.lstShiharaiName.add(mp.get("shiharaisaki_name"));
					dd.lstRyoushuushoExist = readDataAndAdd(dd.lstRyoushuushoExist, "ryoushuusho_seikyuusho_tou_flg", mp);
					if( "1".equals(mp.get("houjin_card_riyou_flg")) ){ dd.lstHoujinCardUse = "1";}
					if( "1".equals(mp.get("kaisha_tehai_flg")) ){ dd.lstKaishaTehaiUse = "1";}
				}
				
				sql = " SELECT * FROM ryohiseisan_keihi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
					dd.lstRyoushuushoExist = readDataAndAdd(dd.lstRyoushuushoExist, "shouhyou_shorui_flg", mp);
					dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "torihikisaki_name_ryakushiki", mp);
					if( "1".equals(mp.get("houjin_card_riyou_flg")) ){ dd.lstHoujinCardUse = "1";}
					if( "1".equals(mp.get("kaisha_tehai_flg")) ){ dd.lstKaishaTehaiUse = "1";}
					BigDecimal zeinukiKingaku = mp.get("hontai_kingaku") == null ? BigDecimal.ZERO : mp.get("hontai_kingaku");
					dd.lstZeinukiMeisaiKingaku.add(zeinukiKingaku);
					zeinukiSum = zeinukiSum.add(zeinukiKingaku);
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
					dd.lstShiharaiName.add(mp.get("shiharaisaki_name"));
				}

				if (dd.lstHoujinCardUse.isEmpty())
				{
					dd.lstHoujinCardUse = "0";
				} //法人カード使用フラグ
				if (dd.lstKaishaTehaiUse.isEmpty())
				{
					dd.lstKaishaTehaiUse = "0";
				} //会社手配使用フラグ
				
				dd.lstZeinukiKingaku = zeinukiSum;//ryohiseisanテーブルに税抜合計金額が無いため、税抜明細金額合計と同じ値が入る

				break;
				
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI  	:
				//指定伝票ID分のデータをryohi_karibaraiとryohi_karibarai_meisaiとryohi_karibarai_keihi_meisaiから取得
				sql = " SELECT * FROM ryohi_karibarai WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				String rkKaribaraiOn = "";
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				rkKaribaraiOn = readDataAndAdd(rkKaribaraiOn, "karibarai_on", map);
				if( "1".equals(map.get("karibarai_on")) ){
					dd.lstKingaku = (BigDecimal)map.get("karibarai_kingaku");
					dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("karibarai_kingaku");
				}else{
					dd.lstKingaku = (BigDecimal)map.get("kingaku");
					dd.lstSashihikiShikyuuKingaku   = null;
				}
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstHoumonsaki = (String)map.get("houmonsaki");
				dd.lstMokuteki = (String)map.get("mokuteki");
				dd.lstUserSei = (String)map.get("user_sei");
				dd.lstUserMei = (String)map.get("user_mei");
				dd.lstSeisankikanFrom = (Date)map.get("seisankikan_from");
				dd.lstSeisankikanTo = (Date)map.get("seisankikan_to");
				
				sql = " SELECT * FROM ryohi_karibarai_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("meisai_kingaku"));
				}
				
				sql = " SELECT * FROM ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
				}
				
				//未精算仮払伝票フラグ
				if(!("40".equals(denpyouStatus)) && !("90".equals(denpyouStatus)) ){
					sql = "SELECT count(karibarai_denpyou_id) AS cnt FROM ryohiseisan seisan "
							+ " INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id "
							+ " WHERE d.denpyou_joutai = '30' AND seisan.karibarai_denpyou_id = ? ";
					map = connection.find(sql,denpyouId);
					dd.lstMiseisanKaribaraiExist = ( rkKaribaraiOn.equals("1") && (long)(map.get("cnt")) == 0 ) ? "1" : "";
					dd.lstMiseisanUkagaiExist  = ( rkKaribaraiOn.equals("0") && (long)(map.get("cnt")) == 0 ) ? "1" : "";
				}

				break;
				
			case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI   	:
				//指定伝票ID分のデータをtsuukinteikiから取得
				sql = " SELECT * FROM tsuukinteiki WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				dd.lstKingaku = (BigDecimal)map.get("kingaku");
				dd.lstMeisaiKingaku.add(dd.lstKingaku);
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaihouhou = "2";
				dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("kingaku");
				dd.lstZeinukiKingaku = (BigDecimal)map.get("zeinuki_kingaku");
				dd.lstZeinukiMeisaiKingaku.add(dd.lstZeinukiKingaku);
				dd.lstJigyoushakbn.add(map.get("jigyousha_kbn"));
				dd.lstShiharaiName.add(map.get("shiharaisaki_name"));

				break;
				
			case DENPYOU_KBN.FURIKAE_DENPYOU   	:
				//指定伝票ID分のデータをfurikaeから取得
				sql = " SELECT * FROM furikae WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				dd.lstFutanbumonCd.add(map.get("kashi_futan_bumon_cd"));
				dd.lstRyoushuushoExist = (String)map.get("shouhyou_shorui_flg");
				dd.lstKingaku = (BigDecimal)map.get("kingaku");
				dd.lstMeisaiKingaku.add(dd.lstKingaku);
				dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "kari_torihikisaki_name_ryakushiki", map);
				dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "kashi_torihikisaki_name_ryakushiki", map); //lstTorihikisaki2はlstTorihikisaki1に統合
				dd.lstKeijoubi = (Date)map.get("denpyou_date");
				
				dd.lstZeinukiKingaku = (BigDecimal)map.get("hontai_kingaku");
				dd.lstZeinukiMeisaiKingaku.add(dd.lstZeinukiKingaku);
				dd.lstJigyoushakbn.add(map.get("kari_jigyousha_kbn"));
				dd.lstJigyoushakbn.add(map.get("kashi_jigyousha_kbn"));
				
				break;
				
			case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU  	:
				//指定伝票ID分のデータをtsukekaeとtsukekae_meisaiから取得
				sql = " SELECT * FROM tsukekae WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstRyoushuushoExist = (String)map.get("shouhyou_shorui_flg");
				dd.lstKingaku = (BigDecimal)map.get("kingaku_goukei");
				dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "moto_torihikisaki_name_ryakushiki", map);
				dd.lstKeijoubi = (Date)map.get("denpyou_date");
				dd.lstFutanbumonCd.add(map.get("moto_futan_bumon_cd"));
				
				dd.lstJigyoushakbn.add(map.get("moto_jigyousha_kbn"));
				
				sql = " SELECT * FROM tsukekae_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstZeinukiKingaku = (BigDecimal)mp.get("hontai_kingaku");
					dd.lstMeisaiKingaku.add(mp.get("kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("saki_futan_bumon_cd"));
					dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "saki_torihikisaki_name_ryakushiki", mp); //lstTorihikisaki2はlstTorihikisaki1に統合
					dd.lstZeinukiMeisaiKingaku.add(mp.get("hontai_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("saki_jigyousha_kbn"));
				}

				break;
				
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU :
				//指定伝票ID分のデータをjidouhikiotoshiとjidouhikiotoshi_meisaiから取得
				sql = " SELECT * FROM jidouhikiotoshi WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstKingaku = (BigDecimal)map.get("shiharai_kingaku_goukei");
				dd.lstShiharaibi = (Date)map.get("hikiotoshibi");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				
				dd.lstZeinukiKingaku = (BigDecimal)map.get("hontai_kingaku_goukei");

				sql = " SELECT * FROM jidouhikiotoshi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku"));
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
					dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "torihikisaki_name_ryakushiki", mp);
					dd.lstZeinukiMeisaiKingaku.add(mp.get("hontai_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
				}

				break;
				
			case DENPYOU_KBN.KOUTSUUHI_SEISAN   	:
				//指定伝票ID分のデータをkoutsuuhiseisanとkoutsuuhiseisan_meisaiから取得
				sql = " SELECT * FROM koutsuuhiseisan WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				dd.lstKingaku = (BigDecimal)map.get("goukei_kingaku");
				dd.lstHoujinKingaku = (BigDecimal)map.get("houjin_card_riyou_kingaku");
				dd.lstTehaiKingaku = (BigDecimal)map.get("kaisha_tehai_kingaku");
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("sashihiki_shikyuu_kingaku");
				dd.lstKeijoubi = (Date)map.get("seisankikan_to");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				dd.lstMokuteki = (String)map.get("mokuteki");
				dd.lstSeisankikanFrom = (Date)map.get("seisankikan_from");
				dd.lstSeisankikanTo = (Date)map.get("seisankikan_to");
				
				//各フラグのみ明細から取得
				sql = " SELECT * FROM koutsuuhiseisan_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("meisai_kingaku"));
					dd.lstRyoushuushoExist = readDataAndAdd(dd.lstRyoushuushoExist, "ryoushuusho_seikyuusho_tou_flg", mp);
					if( "1".equals(mp.get("houjin_card_riyou_flg")) ){ dd.lstHoujinCardUse = "1";}
					if( "1".equals(mp.get("kaisha_tehai_flg")) ){ dd.lstKaishaTehaiUse = "1";}
					dd.lstZeinukiMeisaiKingaku.add(mp.get("zeinuki_kingaku"));
					zeinukiSum = zeinukiSum.add(mp.get("zeinuki_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
					dd.lstShiharaiName.add(mp.get("shiharaisaki_name"));
				}
				dd.lstZeinukiKingaku = zeinukiSum;
				
				if (dd.lstHoujinCardUse.isEmpty())
				{
					dd.lstHoujinCardUse = "0";
				} //法人カード使用フラグ
				if (dd.lstKaishaTehaiUse.isEmpty())
				{
					dd.lstKaishaTehaiUse = "0";
				} //会社手配使用フラグ
				
				break;
				
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN   	:
				//指定伝票ID分のデータをkaigai_ryohi_seisanとkaigai_ryohi_karibarai_meisaiとkaigai_ryohi_karibarai_keihi_meisaiから取得
				//国内と海外のデータは区別つくため取得テーブルはkaigai側のみのはず
				sql = " SELECT * FROM kaigai_ryohiseisan WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "kaigai_tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				dd.lstFutanbumonCd.add(map.get("kaigai_kari_futan_bumon_cd"));
				dd.lstKingaku = (BigDecimal)map.get("goukei_kingaku");
				dd.lstHoujinKingaku = (BigDecimal)map.get("houjin_card_riyou_kingaku");
				dd.lstTehaiKingaku = (BigDecimal)map.get("kaisha_tehai_kingaku");
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("sashihiki_shikyuu_kingaku");
				dd.lstKeijoubi = (Date)map.get("seisankikan_to");
				dd.lstShiwakeKeijoubi = (Date)map.get("keijoubi");
				dd.lstKaribaraiDenpyouId = (String)map.get("karibarai_denpyou_id");
				dd.lstHoumonsaki = (String)map.get("houmonsaki");
				dd.lstMokuteki = (String)map.get("mokuteki");
				dd.lstUserSei = (String)map.get("user_sei");
				dd.lstUserMei = (String)map.get("user_mei");
				dd.lstSeisankikanFrom = (Date)map.get("seisankikan_from");
				dd.lstSeisankikanTo = (Date)map.get("seisankikan_to");
					
				sql = " SELECT * FROM kaigai_ryohiseisan_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("meisai_kingaku"));
					dd.lstRyoushuushoExist = readDataAndAdd(dd.lstRyoushuushoExist, "ryoushuusho_seikyuusho_tou_flg", mp);
					if( "1".equals(mp.get("houjin_card_riyou_flg")) ){ dd.lstHoujinCardUse = "1";}
					if( "1".equals(mp.get("kaisha_tehai_flg")) ){ dd.lstKaishaTehaiUse = "1";}
					dd.lstZeinukiMeisaiKingaku.add(mp.get("zeinuki_kingaku"));
					zeinukiSum = zeinukiSum.add(mp.get("zeinuki_kingaku"));
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
					dd.lstShiharaiName.add(mp.get("shiharaisaki_name"));
				}
				
				sql = " SELECT * FROM kaigai_ryohiseisan_keihi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku")); 
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
					dd.lstRyoushuushoExist = readDataAndAdd(dd.lstRyoushuushoExist, "shouhyou_shorui_flg", mp);
					if( "1".equals(mp.get("houjin_card_riyou_flg")) ){ dd.lstHoujinCardUse = "1";}
					if( "1".equals(mp.get("kaisha_tehai_flg")) ){ dd.lstKaishaTehaiUse = "1";}
					dd.lstTorihikisaki1 = readDataAndAdd(dd.lstTorihikisaki1, "torihikisaki_name_ryakushiki", mp);
					BigDecimal zeinukiKingaku = mp.get("hontai_kingaku") == null ? BigDecimal.ZERO : mp.get("hontai_kingaku");
					dd.lstZeinukiMeisaiKingaku.add(zeinukiKingaku);
					zeinukiSum = zeinukiSum.add(zeinukiKingaku);
					dd.lstJigyoushakbn.add(mp.get("jigyousha_kbn"));
					dd.lstShiharaiName.add(mp.get("shiharaisaki_name"));
				}
				dd.lstZeinukiKingaku = zeinukiSum;
				
				if (dd.lstHoujinCardUse.isEmpty())
				{
					dd.lstHoujinCardUse = "0";
				} //法人カード使用フラグ
				if (dd.lstKaishaTehaiUse.isEmpty())
				{
					dd.lstKaishaTehaiUse = "0";
				} //会社手配使用フラグ
				
				sql = "SELECT STRING_AGG(gaika, ')' || CHR(13) || CHR(10) || '(' ORDER BY display_order) as gaika "
						+ " FROM (SELECT(TO_CHAR(SUM(tmp.gaika),'FM999,999,999,999.00') || tmp.heishu_cd) gaika,hcd.display_order "
						+ " FROM (SELECT gaika,heishu_cd FROM kaigai_ryohiseisan_meisai m "
						+ " WHERE m.denpyou_id = ? "
						+ " UNION ALL SELECT gaika,heishu_cd FROM kaigai_ryohiseisan_keihi_meisai m "
						+ " WHERE m.denpyou_id = ? )tmp "
						+ " LEFT OUTER JOIN heishu_master hcd ON tmp.heishu_cd=hcd.heishu_cd GROUP BY tmp.heishu_cd,hcd.display_order) tmp";
				GMap gkkr = connection.find(sql, denpyouId,denpyouId);
				if(gkkr != null && gkkr.get("gaika") != null){
					dd.lstGaika = (String)gkkr.get("gaika");
				}
				
				break;
				
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI  :
				//指定伝票ID分のデータをkaigai_ryohi_karibaraiとkaigai_ryohi_karibarai_meisaiとkaigai_ryohi_karibarai_keihi_meisaiから取得
				//国内と海外のデータは区別つくため取得テーブルはkaigai側のみのはず
				sql = " SELECT * FROM kaigai_ryohi_karibarai WHERE denpyou_id = ? ";
				map = connection.find(sql,denpyouId);
				String kgKaribaraiOn = "";
				dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", map);
				dd.lstFutanbumonCd.add(map.get("kari_futan_bumon_cd"));
				kgKaribaraiOn = readDataAndAdd(kgKaribaraiOn, "karibarai_on", map);
				if( "1".equals(map.get("karibarai_on")) ){
					dd.lstKingaku = (BigDecimal)map.get("karibarai_kingaku");
					dd.lstSashihikiShikyuuKingaku   = (BigDecimal)map.get("karibarai_kingaku");
				}else{
					dd.lstKingaku = (BigDecimal)map.get("kingaku");
					dd.lstSashihikiShikyuuKingaku   = null;
				}
				dd.lstShiharaibi = (Date)map.get("shiharaibi");
				dd.lstShiharaikiboubi = (Date)map.get("shiharaikiboubi");
				dd.lstShiharaihouhou = (String)map.get("shiharaihouhou");
				dd.lstHoumonsaki = (String)map.get("houmonsaki");
				dd.lstMokuteki = (String)map.get("mokuteki");
				dd.lstUserSei = (String)map.get("user_sei");
				dd.lstUserMei = (String)map.get("user_mei");
				dd.lstSeisankikanFrom = (Date)map.get("seisankikan_from");
				dd.lstSeisankikanTo = (Date)map.get("seisankikan_to");

				sql = " SELECT * FROM kaigai_ryohi_karibarai_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("meisai_kingaku"));
				}

				sql = " SELECT * FROM kaigai_ryohi_karibarai_keihi_meisai WHERE denpyou_id = ? ORDER BY denpyou_edano";
				lst = connection.load(sql,denpyouId);
				for(GMap mp : lst){
					dd.lstMeisaiKingaku.add(mp.get("shiharai_kingaku")); 
					dd.lstTekiyou = readDataAndAdd(dd.lstTekiyou, "tekiyou", mp);
					dd.lstFutanbumonCd.add(mp.get("kari_futan_bumon_cd"));
				}
				
				//未精算仮払伝票フラグ
				if( !("40".equals(denpyouStatus)) && !("90".equals(denpyouStatus)) ){
					sql = "SELECT count(karibarai_denpyou_id) AS cnt FROM kaigai_ryohiseisan seisan "
							+ " INNER JOIN denpyou d ON seisan.denpyou_id = d.denpyou_id "
							+ " WHERE d.denpyou_joutai = '30' AND seisan.karibarai_denpyou_id = ? ";
					map = connection.find(sql,denpyouId);
					dd.lstMiseisanKaribaraiExist = ( kgKaribaraiOn.equals("1") && (long)(map.get("cnt")) == 0 ) ? "1" : "";
					dd.lstMiseisanUkagaiExist  = ( kgKaribaraiOn.equals("0") && (long)(map.get("cnt")) == 0 ) ? "1" : "";
				}
				
				sql = "SELECT STRING_AGG(gaika, ')' || CHR(13) || CHR(10) || '(' ORDER BY display_order) as gaika "
						+ "  FROM (SELECT(TO_CHAR(SUM(tmp.gaika),'FM999,999,999,999.00') || tmp.heishu_cd) gaika,hcd.display_order "
						+ "  FROM (SELECT gaika,heishu_cd FROM kaigai_ryohi_karibarai_meisai m "
						+ "  WHERE m.denpyou_id = ? "
						+ "  UNION ALL SELECT gaika,heishu_cd FROM kaigai_ryohi_karibarai_keihi_meisai m "
						+ "  WHERE m.denpyou_id = ? )tmp "
						+ "  LEFT OUTER JOIN heishu_master hcd ON tmp.heishu_cd=hcd.heishu_cd GROUP BY tmp.heishu_cd,hcd.display_order) tmp";
				GMap gkks = connection.find(sql, denpyouId,denpyouId);
				if(gkks != null && gkks.get("gaika") != null){
					dd.lstGaika = (String)gkks.get("gaika");
				}
				
				break;
				
			default:
				//簡易届のみの前提
				
				if( "B".equals(denpyouKbn.substring(0,1)) ){
					// 負担部門取得
					sql = "SELECT m.value1 AS futan_bumon_cd FROM kani_todoke kk, kani_todoke_meisai m, kani_todoke_koumoku k "
							+ " WHERE kk.denpyou_id = ?"
							+ " AND m.denpyou_id = kk.denpyou_id "
							+ " AND k.denpyou_kbn = kk.denpyou_kbn "
							+ " AND k.version = kk.version "
							+ " AND m.item_name = k.item_name "
							+ " AND k.area_kbn = 'meisai' "
							+ " AND (k.yosan_shikkou_koumoku_id = 'shishutsu_bumon' OR k.yosan_shikkou_koumoku_id = 'syuunyuu_bumon') ";
					lst = connection.load(sql,denpyouId);
					for(GMap mp : lst){
						dd.lstFutanbumonCd.add(mp.get("futan_bumon_cd"));
					}
					
					//簡易届申請分の内容をテーブルに追加
					sql = "SELECT kk.value1 AS naiyou FROM kani_todoke kk, kani_todoke_koumoku k "
							+ " WHERE kk.denpyou_id = ?"
							+ " AND k.denpyou_kbn = kk.denpyou_kbn "
							+ " AND k.version = kk.version "
							+ " AND k.item_name = kk.item_name "
							+ " AND k.area_kbn = 'shinsei' "
							+ " AND k.yosan_shikkou_koumoku_id = 'naiyou_shinsei' ";
					lst = connection.load(sql,denpyouId);
					for(GMap mp : lst){
						dd.lstNaiyou = readDataAndAdd(dd.lstNaiyou, "naiyou", mp);
					}
					//簡易届明細分の内容をテーブルに追加しておく
					sql = "SELECT m.value1 AS naiyou FROM kani_todoke kk, kani_todoke_meisai m, kani_todoke_koumoku k "
							+ " WHERE kk.denpyou_id = ?"
							+ " AND m.denpyou_id = kk.denpyou_id "
							+ " AND k.denpyou_kbn = kk.denpyou_kbn "
							+ " AND k.version = kk.version "
							+ " AND m.item_name = k.item_name "
							+ " AND k.area_kbn = 'meisai' "
							+ " AND k.yosan_shikkou_koumoku_id = 'naiyou_meisai' ";
					lst = connection.load(sql,denpyouId);
					for(GMap mp : lst){
						dd.lstNaiyou = readDataAndAdd(dd.lstNaiyou, "naiyou", mp);
					}
					
					//件名・金額取得
					sql = " SELECT ds.route_hantei_kingaku, "
						    + " k.version, "
						    + " sam.kenmei, "
						    + " sam.ringi_kingaku, "
						    + " sam.shishutsu_kingaku_goukei, "
						    + " sam.shuunyuu_kingaku_goukei "
						    + " FROM kani_todoke k "
						    + " LEFT OUTER JOIN kani_todoke_summary sam ON k.denpyou_id=sam.denpyou_id "
						    + " LEFT OUTER JOIN denpyou_shubetsu_ichiran ds ON k.denpyou_kbn=ds.denpyou_kbn "
						    + " WHERE k.denpyou_id = ? ";
					map = connection.find(sql,denpyouId);
					String rhk = "";
					if(map != null){
						dd.lstVersion = (int)map.get("version");
						dd.lstKenmei = readDataAndAdd(dd.lstKenmei, "kenmei", map);
						if(map.get("route_hantei_kingaku") != null){
							rhk = (String)map.get("route_hantei_kingaku");
							switch(rhk){
							case "1":dd.lstKingaku = (BigDecimal)map.get("ringi_kingaku");break;
							case "2":dd.lstKingaku = (BigDecimal)map.get("shishutsu_kingaku_goukei");break;
							case "3":
								BigDecimal shik = map.get("shishutsu_kingaku_goukei") == null ? new BigDecimal(0) : (BigDecimal)map.get("shishutsu_kingaku_goukei");
								BigDecimal sunk = map.get("shuunyuu_kingaku_goukei") == null ? new BigDecimal(0) : (BigDecimal)map.get("shuunyuu_kingaku_goukei");
								if(shik.compareTo(sunk) > 0){
									dd.lstKingaku = shik;
								}else{
									dd.lstKingaku = sunk;
								}
								break;
							case "4":dd.lstKingaku = (BigDecimal)map.get("shuunyuu_kingaku_goukei");break;
							default:break;
							}
							
						}
					}
					
					//明細金額
					sql = "SELECT "
							+ "  TO_NUMBER(REPLACE(m.value1,',',''), '000000000000000') AS value1 "
							+ "FROM kani_todoke_meisai m "
							+ "JOIN kani_todoke_text t ON "
							+ "  t.denpyou_kbn=? "
							+ "  AND t.version=(SELECT distinct VERSION FROM kani_todoke k WHERE k.denpyou_id=m.denpyou_id) "
							+ "  AND t.item_name=m.item_name "
							+ "WHERE "
							+ "  m.denpyou_id = ? "
							+ "  AND t.buhin_format = 'autoNumericWithCalcBox' "
							+ "  AND m.value1 IS NOT NULL AND m.value1 <> '' ";
					lst = connection.load(sql,denpyouKbn, denpyouId);
					for(GMap mp : lst){
						if(mp.get("value1") != null) dd.lstMeisaiKingaku.add(mp.get("value1"));
					}
				}
				
				break;
		}

		//--------------------
		//伝票種別毎に仕訳作って貸借項目セット
		//--------------------
		List<ShiwakeData> shiwakes = makeShiwake(denpyouKbn, denpyouId);
		if(shiwakes != null) for(ShiwakeData s : shiwakes) {
			if(!s.getR().getKMK().equals(setting.shokuchiCd())) {
				dd.lstKariFutanbumonCd .add(s.getR().getBMN());
				dd.lstKariKamokuCd .add(s.getRKMK().equals("") ? s.getR().getKMK() : s.getRKMK());
				dd.lstKariKamokuEdabanCd .add(s.getR().getEDA());
				dd.lstKariTorihikisakiCd .add(s.getR().getTOR());
				
			}
			if(!s.getS().getKMK().equals(setting.shokuchiCd())) {
				dd.lstKashiFutanbumonCd .add(s.getS().getBMN());
				dd.lstKashiKamokuCd .add(s.getSKMK().equals("") ? s.getS().getKMK():s.getSKMK());
				dd.lstKashiKamokuEdabanCd .add(s.getS().getEDA());
				dd.lstKashiTorihikisakiCd .add(s.getS().getTOR());
				if(s.getS().getVALU_hontai() != null) {
					dd.lstMeisaiKingaku .add(s.getS().getVALU_hontai());
				}
			}
		} 
		
		return dd;
	}
	
	/**
	 * 伝票一覧テーブルのWF共通部分へのマッピング
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param com WF共通の箱
	 */
	protected void setWfCom(String denpyouId, String denpyouKbn, DenpyouIchiranDataWFCom com) {
		String sql = "";
		List<GMap> lst;

		GMap map = this.getDenpyouIchiranDat(denpyouId);
		String chuushutsuZumiFlg = map.get("chuushutsu_zumi_flg");//TODO:既存ロジックだがこれは必ずnullだし後続のロジックが絶対に通らない系になってるキガス・・・
		
		//各種表示用データを登録
		com.denpyouKbn = map.get("denpyou_kbn") == null ? "" : (String)map.get("denpyou_kbn");
		com.serialNo = map.get("serial_no");
		com.denpyouJoutai = map.get("denpyou_joutai");
		com.name = map.get("name") == null ? "" : (String)map.get("name");
		com.denpyouShubetsu_url = map.get("denpyou_shubetsu_url") == null ? "" : (String)map.get("denpyou_shubetsu_url");
		com.tourokuTime = map.get("touroku_time");
		com.koushinTime = map.get("koushin_time");
		com.shouninbi = map.get("shouninbi");
		com.bumonFullName = map.get("bumon_full_name");
		com.userId = map.get("user_id");
		com.userFullName = map.get("user_full_name");
		com.maruhiFlg = map.get("maruhi_flg");
		com.shain_no = map.get("shain_no");
		
		sql =  " SELECT DISTINCT shiwake_status FROM shiwake_de3 WHERE denpyou_id = ? "
				+" UNION SELECT DISTINCT shiwake_status FROM shiwake_sias WHERE denpyou_id = ? ";
		lst = connection.load(sql,denpyouId,denpyouId);
		for(GMap mp : lst){
			com.shiwakeStatus = readDataAndAdd(com.shiwakeStatus, "shiwake_status", mp);
			//仕訳レコード未作成だけどdenpyou.chuushutsu_zumi_flg!=0(抽出処理済)なのは通勤定期で仕訳作成しない設定の場合を想定・・・「未抽出」の検索に出ないようにshiwake_status=N(本来の値じゃない)にしておく
			if(com.shiwakeStatus.equals("") && !chuushutsuZumiFlg.equals("0")){
				com.shiwakeStatus = "9";
			}
		}
		
		//起票部門コード
		sql = " SELECT bumon_cd FROM shounin_route WHERE denpyou_id = ? AND edano = '1'";
		map = connection.find(sql,denpyouId);
		if(map != null){
			if( map.get("bumon_cd") != null && !((String)map.get("bumon_cd")).isEmpty() ){
				com.bumonCd = (String)map.get("bumon_cd");
			}
		}

		//FBデータ作成ステータス
		sql =   "SELECT B.denpyou_id FROM :TABLE A "
				+ " INNER JOIN fb B "
				+ " ON A.denpyou_id = B.denpyou_id "
				+ " WHERE A.denpyou_id = ? "
				+ " :SHIHARAI "
				+ " AND B.fb_status = '1' ";
		switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:				sql = sql.replace(":TABLE", "keihiseisan"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.KARIBARAI_SHINSEI:					sql = sql.replace(":TABLE", "karibarai"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.RYOHI_SEISAN:						sql = sql.replace(":TABLE", "ryohiseisan"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:			sql = sql.replace(":TABLE", "ryohi_karibarai"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.KOUTSUUHI_SEISAN:					sql = sql.replace(":TABLE", "koutsuuhiseisan"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:				sql = sql.replace(":TABLE", "kaigai_ryohiseisan"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:	sql = sql.replace(":TABLE", "kaigai_ryohi_karibarai"); sql = sql.replace(":SHIHARAI", " AND A.shiharaihouhou = '2' ");break;
			case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:				sql = sql.replace(":TABLE", "tsuukinteiki"); sql = sql.replace(":SHIHARAI", "");break;
			default:											sql = "";break;
		}
		if(!sql.isEmpty()){
			GMap mp = connection.find(sql,denpyouId);
			com.fbStatus = (mp != null) ? "1" : "0";
		}
	}

	/**
	 * 指定Map内データを参照して、区切り文字を追加して登録。
	 * @param orgStr  文字
	 * @param mapName 取得データ名
	 * @param map     データマップ
	 * @return 1つ目のデータならorgStrそのもの、2つ目以降なら区切り文字の追加されたデータ。
	 */
	protected String readDataAndAdd(String orgStr, String mapName, GMap map){
		
		//like検索を実行した際、区切り文字まで検索結果に含まれてしまう
		//回避のためにテーブルに配列を使用するとlike検索ができない
		//使用しなさそうな文字"╂"(罫線、＋とは別の文字)を指定
		
		if( map.get(mapName) != null && !((String)map.get(mapName)).isEmpty() ){
			if(orgStr.length() > 0){
				String addStr = (String)map.get(mapName);
				String[] chkStr = orgStr.split("╂", -1);
				//追加要素が既に含まれているなら格納せず返却
				for(String st : chkStr){
					if(addStr.equals(st)){
						return orgStr;
					}
				}
				orgStr += "╂" + addStr;
			}else{
				orgStr = (String)map.get(mapName);
			}
		}
		return orgStr;
	}
	
	/**
	 * 伝票一覧の元ネタ取得
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	protected GMap getDenpyouIchiranDat(String denpyouId){

		// 伝票一覧への表示項目を増やした場合、本メソッドとDenpyouIchiranTableLogic.updateDenpyouIchiranShousaiにも該当分のデータを追加すること
		String sqlMain = 

			//中間テーブル L（伝票共通部分）
			  "WITH "
			+ "L AS(SELECT "
			+ "    d.name"
			+ "    ,a.denpyou_kbn"
			+ "    ,a.denpyou_id"
			+ "    ,f.jisshi_kian_bangou"
			+ "    ,f.shishutsu_kian_bangou"
			+ "    ,g.name AS yosan_shikkou_taishou"
			+ "    ,a.serial_no"
			+ "    ,b.denpyou_shubetsu"
			+ "    ,b.denpyou_shubetsu_url"
			+ "    ,c.touroku_time"
			+ "    ,c.bumon_full_name"
			+ "    ,c.user_full_name"
			+ "    ,c.user_id"
			+ "    ,c.gyoumu_role_id"
			+ "    ,a.denpyou_joutai"
			+ "    ,b.gyoumu_shubetsu"
			+ "    ,c.koushin_time"
			+ "    ,CASE WHEN a.denpyou_joutai IN ('20','30') THEN e.koushin_time ELSE NULL END AS shouninbi"
			+ "    ,a.maruhi_flg"
			+ "    ,a.yosan_check_nengetsu"
			+ "    ,u.shain_no"
			+ "  FROM denpyou a"
			+ "  INNER JOIN denpyou_shubetsu_ichiran b ON "
			+ "     a.denpyou_kbn = b.denpyou_kbn "
			+ "  INNER JOIN shounin_route c ON "
			+ "     a.denpyou_id = c.denpyou_id "
			+ "     AND c.edano = 1"
			+ "  INNER JOIN denpyou_kian_himozuke f ON "
			+ "     a.denpyou_id = f.denpyou_id "
			+ "  INNER JOIN naibu_cd_setting d ON "
			+ "     d.naibu_cd_name  = 'denpyou_jyoutai'"
			+ "     AND a.denpyou_joutai = d.naibu_cd"
			+ "  LEFT OUTER JOIN user_info u ON "
			+ "     a.touroku_user_id = u.user_id "
			+ "  LEFT OUTER JOIN naibu_cd_setting g ON "
			+ "     g.naibu_cd_name  = 'yosan_shikkou_taishou'"
			+ "     AND b.yosan_shikkou_taishou = g.naibu_cd"
			+ "  LEFT OUTER JOIN (SELECT denpyou_id, MAX(koushin_time) koushin_time FROM shounin_joukyou WHERE joukyou_cd='4' GROUP BY denpyou_id) e ON "
			+ "     a.denpyou_id = e.denpyou_id "
			+ "  WHERE a.denpyou_id = ? "
			+ ")"

			// SQL本体
			+ "SELECT "
			+ "  L.name"
			+ "  ,L.denpyou_kbn"
			+ "  ,L.denpyou_id"
			+ "  ,L.jisshi_kian_bangou"
			+ "  ,L.shishutsu_kian_bangou"
			+ "  ,L.yosan_shikkou_taishou"
			+ "  ,L.serial_no"
			+ "  ,L.denpyou_shubetsu"
			+ "  ,L.denpyou_shubetsu_url"
			+ "  ,L.touroku_time"
			+ "  ,L.bumon_full_name"
			+ "  ,L.user_full_name"
			+ "  ,L.user_id"
			+ "  ,L.gyoumu_role_id"
			+ "  ,L.denpyou_joutai"
			+ "  ,L.gyoumu_shubetsu"
			+ "  ,L.koushin_time "
			+ "  ,L.shouninbi "
			+ "  ,L.maruhi_flg "
			+ "  ,L.yosan_check_nengetsu "
			+ "  ,L.shain_no "
// + getLAppendColumn_Ext()

			//中間テーブル L（伝票共通部分）
			+ "FROM L ";

		return connection.find(sqlMain, denpyouId);
	}
	
	/**
	 * メモリ上で仕訳作る
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return 仕訳リスト
	 */
	protected List<ShiwakeData> makeShiwake(String denpyouKbn, String denpyouId) {
		//仕訳ができているならもうそいつでいい
		final String sql =
				"SELECT "
			+ "  rbmn, rtor, rkmk, reda, "
			+ "  sbmn, stor, skmk, seda  "
			+ "FROM shiwake_de3 WHERE denpyou_id = ? "
			+ "UNION "
			+ "SELECT "
			+ "  rbmn, rtor, rkmk, reda, "
			+ "  sbmn, stor, skmk, seda  "
			+ "FROM shiwake_sias WHERE denpyou_id = ? ";
		List<GMap> list = connection.load(sql,  denpyouId, denpyouId);
		if(!list.isEmpty()){
			List<ShiwakeData> retList = new ArrayList<>();
			for(GMap map : list) {
				ShiwakeData s = new ShiwakeData();
				s.getR().setKMK(map.get("rkmk"));
				s.getR().setEDA(map.get("reda"));
				s.getR().setBMN(map.get("rbmn"));
				s.getR().setTOR(map.get("rtor"));
				s.getS().setKMK(map.get("skmk"));
				s.getS().setEDA(map.get("seda"));
				s.getS().setBMN(map.get("sbmn"));
				s.getS().setTOR(map.get("stor"));
				retList.add(s);
			}
			return retList;
		}
		
		//仕訳ができていないならメモリ上で作ってみる
		try {
			EteamAbstractKaikeiBat bat = null;
			switch(denpyouKbn) {
				case "A001": bat = EteamContainer.getComponent(KeihiTatekaeSeisanChuushutsuBat.class); break;
				case "A002": bat = EteamContainer.getComponent(KaribaraiChuushutsuBat.class); break;
				case "A003": bat = EteamContainer.getComponent(SeikyuushoBaraiChuushutsuBat.class); break;
				case "A004": bat = EteamContainer.getComponent(RyohiSeisanChuushutsuBat.class); break;
				case "A005": bat = EteamContainer.getComponent(RyohiKaribaraiChuushutsuBat.class); break;
				case "A006": bat = EteamContainer.getComponent(TsuukinTeikiChuushutsuBat.class); break;
				case "A007": bat = EteamContainer.getComponent(FurikaeDenpyouChuushutsuBat.class); break;
				case "A008": bat = EteamContainer.getComponent(SougouTsukekaeDenpyouChuushutsuBat.class); break;
				case "A009": bat = EteamContainer.getComponent(JidouHikiotoshiChuushutsuBat.class); break;
				case "A010": bat = EteamContainer.getComponent(KoutsuuhiSeisanChuushutsuBat.class); break;
				case "A011": bat = EteamContainer.getComponent(KaigaiRyohiSeisanChuushutsuBat.class); break;
				case "A012": bat = EteamContainer.getComponent(KaigaiRyohiKaribaraiChuushutsuBat.class); break;
				case "A013": bat = EteamContainer.getComponent(ShiharaiIraiChuushutsuBat.class); break;
			}
			if (bat == null)
			{
				return null;
			}
			bat.setOnMemory(true);
			bat.setConnection(connection);
			return bat.makeShiwake(denpyouId);
		}catch(Exception e) {
			log.error("仕訳作成処理でエラー", e);
		}
		return null;
	}

// /**
//  * 動作確認
//  * @param argv 0:スキーマ名
//  */
// public static void main(String[] argv) {
// String schemaName = "ver190531";
// String denpyouId = "181130-A003-0000002";
//
// //準備
// PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));
// Map<String, String> threadMap = EteamThreadMap.get();
// threadMap.put(SYSTEM_PROP.SCHEMA, schemaName);
// 
//
// //実行
// EteamConnection connection = EteamConnection.connect();
// try{
// connection.update("delete from denpyou_ichiran_kyoten WHERE denpyou_id=?", denpyouId);
// DenpyouIchiranUpdateLogic l = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
// l.insertDenpyouIchiran(denpyouId);
// l.updateDenpyouIchiran(denpyouId);
// connection.commit();
// } catch(Exception e) {
// e.printStackTrace();
// } finally {
// connection.close();
// }
// }
}
