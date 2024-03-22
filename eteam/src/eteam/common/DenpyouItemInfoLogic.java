package eteam.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.DenpyouItemInfo.BuhinFormat;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;

/**
 */
public class DenpyouItemInfoLogic extends EteamAbstractLogic {

	/** システムカテゴリSELECT */
	SystemKanriCategoryLogic systemLogic;
	/** マスターカテゴリSELECT */
	MasterKanriCategoryLogic masterLogic;
	
	/**
	 * @param _connection コネクション
	 */
	public void init (EteamConnection _connection) {
		super.init(_connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
	}
	
	/**
	 * @param tableName テーブル名
	 * @return 伝票項目名
	 */
	public Map<String, DenpyouItemInfo> getDenpyouItemInfo(String tableName){
		Map<String, DenpyouItemInfo> ret = new HashMap<String, DenpyouItemInfo>();

		//--------------------
		//値→文言の返還マップ各種を作る
		//--------------------
		// フラグ
		Map<String, String> mapFlg = new HashMap<String, String>();
		mapFlg.put("1", "あり");
		mapFlg.put("0", "なし");
		
		// 出張区分
		Map<String, String> mapKaigaiFlg = new HashMap<String, String>();
		mapKaigaiFlg.put("1", "海外");
		mapKaigaiFlg.put("0", "国内");
		
		// 税率
		Map<String, String> mapZeiritsu = new HashMap<String, String>();
		List<GMap> zeiritsuList = masterLogic.loadshouhizeiritsu();
		for(GMap map : zeiritsuList) {
			String isKeigen = (EteamConst.keigenZeiritsuKbn.KEIGEN.equals(map.get("keigen_zeiritsu_kbn")))? EteamConst.keigenZeiritsuKbn.KEIGEN_MARK : "";
			mapZeiritsu.put(isKeigen + map.get("zeiritsu").toString(), isKeigen + map.get("zeiritsu").toString() + "%");
		}
		
		Map<String, String> mapKazeiKbn = getNaibuCdSettingMap("kazei_kbn"); // 課税区分
		Map<String, String> mapShiharaiHouhou = getNaibuCdSettingMap("shiharai_houhou"); // 支払方法
		Map<String, String> mapShiyouKikanKbn = getNaibuCdSettingMap("shiyou_kikan_kbn"); // 使用期間区分
		Map<String, String> mapTsukekaeKbn = getNaibuCdSettingMap("tsukekae_kbn"); // 付替区分
		Map<String, String> mapShiharaiIraiHouhou = getNaibuCdSettingMap("shiharai_irai_houhou");
		Map<String, String> mapShiharaiIraiShubetsu = getNaibuCdSettingMap("shiharai_irai_shubetsu");
		Map<String, String> mapYokinShubetsu = getNaibuCdSettingMap("yokin_shubetsu");
		Map<String, String> mapTesuuryou = getNaibuCdSettingMap("tesuuryou");
		Map<String, String> mapKazeiKbnKyoten = getNaibuCdSettingMap("kazei_kbn"); // 課税区分（財務拠点入力用）
		mapKazeiKbnKyoten.putAll(getNaibuCdSettingMap("kazei_kbn_kyoten_furikae"));
		Map<String, String> mapInvoiceDenpyou = getNaibuCdSettingMap("invoice_denpyou"); //インボイスフラグ
		Map<String, String> mapJigyoushaKbn = getNaibuCdSettingMap("jigyousha_kbn"); //事業者区分
		Map<String, String> mapNyuryokuFlg = getNaibuCdSettingMap("nyuryoku_flg"); //入力方式
		Map<String, String> mapBunriKbn = getNaibuCdSettingMap("bunri_kbn"); //分離区分
		Map<String, String> mapShiireKbn = getNaibuCdSettingMap("shiire_kbn"); //仕入区分
		Map<String, String> mapZeigakuHoushiki = getNaibuCdSettingMap("shiirezeigaku_keisan"); //税額計算方式
		
		//--------------------
		//HF UF 名称取得
		var hu = new HfUfSeigyo();
		GamenKoumokuSeigyo ks = null;
		
		// 差引項目名称
		String sashihikiName = setting.sashihikiName();
		String sashihikiNameKaigai = setting.sashihikiNameKaigai();
		
		String denpyouKbn = null;
		switch (tableName) {
		case "keihiseisan":
			denpyouKbn = DENPYOU_KBN.KEIHI_TATEKAE_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("karibarai_denpyou_id" , new DenpyouItemInfo("仮払" + ks.karibaraiDenpyouId.getName()));
			ret.put("karibarai_mishiyou_flg" , new DenpyouItemInfo(ks.karibaraiMishiyouFlg.getName(), mapFlg));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("sashihiki_shikyuu_kingaku" , new DenpyouItemInfo(ks.sashihikiShikyuuKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shiharai_kingaku_goukei" , new DenpyouItemInfo(ks.shiharaiKingakuGoukei.getName(), BuhinFormat.MONEY));
			ret.put("houjin_card_riyou_kingaku" , new DenpyouItemInfo(ks.uchiHoujinCardRiyouGoukei.getName(), BuhinFormat.MONEY));
			ret.put("kaisha_tehai_kingaku" , new DenpyouItemInfo(ks.kaishaTehaiGoukei.getName(), BuhinFormat.MONEY));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "keihiseisan_meisai":
		case "ryohiseisan_keihi_meisai":
		case "ryohi_karibarai_keihi_meisai":
		case "kaigai_ryohiseisan_keihi_meisai":
		case "kaigai_ryohi_karibarai_keihi_meisai":
			denpyouKbn = DENPYOU_KBN.KEIHI_TATEKAE_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shiyoubi" , new DenpyouItemInfo(ks.shiyoubi.getName(), BuhinFormat.DATE));
			ret.put("shouhyou_shorui_flg" , new DenpyouItemInfo(ks.shouhyouShoruiFlg.getName(), mapFlg));
			ret.put("shain_no" , new DenpyouItemInfo(ks.userName.getName() + "(社員番号)"));
			ret.put("user_sei" , new DenpyouItemInfo(ks.userName.getName() + "(姓)"));
			ret.put("user_mei" , new DenpyouItemInfo(ks.userName.getName() + "(名)"));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki", new DenpyouItemInfo(ks.segment.getName()));
			ret.put("shiharai_kingaku" , new DenpyouItemInfo(ks.shiharaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("hontai_kingaku" , new DenpyouItemInfo("税抜金額", BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("houjin_card_riyou_flg" , new DenpyouItemInfo(ks.houjinCardRiyou.getName(), mapFlg));
			ret.put("kaisha_tehai_flg" , new DenpyouItemInfo(ks.kaishaTehai.getName(), mapFlg));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("kousaihi_shousai" , new DenpyouItemInfo(ks.kousaihiShousai.getName()));
			ret.put("kousaihi_ninzuu" , new DenpyouItemInfo("交際費人数"));
			ret.put("kousaihi_hitori_kingaku" , new DenpyouItemInfo("交際費一人当たりの金額", BuhinFormat.MONEY));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			break;
		case "karibarai":
			denpyouKbn = DENPYOU_KBN.KARIBARAI_SHINSEI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("karibarai_on" , new DenpyouItemInfo(ks.karibaraiOn.getName(), mapFlg));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("seisan_yoteibi" , new DenpyouItemInfo(ks.seisanYoteibi.getName(), BuhinFormat.DATE));
			ret.put("kingaku" , new DenpyouItemInfo(ks.shinseiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("karibarai_kingaku" , new DenpyouItemInfo(ks.kingaku.getName(), BuhinFormat.MONEY));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			break;
		case "seikyuushobarai":
			denpyouKbn = DENPYOU_KBN.SEIKYUUSHO_BARAI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shiharai_kigen", new DenpyouItemInfo(ks.shiharaiKigen.getName(), BuhinFormat.DATE));
			ret.put("shouhyou_shorui_flg" , new DenpyouItemInfo(ks.shouhyouShoruiFlg.getName(), mapFlg));
			ret.put("nyuryoku_houshiki" , new DenpyouItemInfo(ks.nyuryokuHoushiki.getName() , mapNyuryokuFlg));
			ret.put("kake_flg" , new DenpyouItemInfo(ks.kakeFlg.getName(), mapFlg));
			ret.put("shiharai_kingaku_goukei" , new DenpyouItemInfo(ks.shiharaiKingakuGoukei.getName(), BuhinFormat.MONEY));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("masref_flg" , new DenpyouItemInfo("マスター参照", mapFlg));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "seikyuushobarai_meisai":
			denpyouKbn = DENPYOU_KBN.SEIKYUUSHO_BARAI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("furikomisaki_jouhou" , new DenpyouItemInfo(ks.furikomisakiJouhou.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("shiharai_kingaku" , new DenpyouItemInfo(ks.shiharaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("hontai_kingaku" , new DenpyouItemInfo(ks.zeinukiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("kousaihi_shousai" , new DenpyouItemInfo(ks.kousaihiShousai.getName()));
			ret.put("kousaihi_ninzuu" , new DenpyouItemInfo("交際費人数"));
			ret.put("kousaihi_hitori_kingaku" , new DenpyouItemInfo("交際費一人当たりの金額", BuhinFormat.MONEY));
			break;
			
		case "jidouhikiotoshi":
			denpyouKbn = DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("hikiotoshibi" , new DenpyouItemInfo(ks.hikiotoshibi.getName(), BuhinFormat.DATE));
			ret.put("shiharai_kingaku_goukei" , new DenpyouItemInfo(ks.shiharaiKingakuGoukei.getName(), BuhinFormat.MONEY));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("nyuryoku_houshiki" , new DenpyouItemInfo(ks.nyuryokuHoushiki.getName() , mapNyuryokuFlg));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "jidouhikiotoshi_meisai":
			denpyouKbn = DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("shiharai_kingaku" , new DenpyouItemInfo(ks.shiharaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("hontai_kingaku" , new DenpyouItemInfo(ks.zeinukiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			break;
		case "ryohiseisan":
			denpyouKbn = DENPYOU_KBN.RYOHI_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shain_no" , new DenpyouItemInfo(ks.userName.getName() + "(社員番号)"));
			ret.put("user_sei" , new DenpyouItemInfo(ks.userName.getName() +  "(姓)"));
			ret.put("user_mei" , new DenpyouItemInfo(ks.userName.getName() +  "(名)"));
			ret.put("karibarai_denpyou_id" , new DenpyouItemInfo("仮払" + ks.karibaraiDenpyouId.getName()));
			ret.put("karibarai_mishiyou_flg" , new DenpyouItemInfo(ks.karibaraiMishiyouFlg.getName(), mapFlg));
			ret.put("shucchou_chuushi_flg" , new DenpyouItemInfo(ks.shucchouChuushiFlg.getName(), mapFlg));
			ret.put("houmonsaki" , new DenpyouItemInfo(ks.houmonsaki.getName()));
			ret.put("mokuteki" , new DenpyouItemInfo(ks.mokuteki.getName()));
			ret.put("seisankikan_from" , new DenpyouItemInfo(ks.seisankikan.getName() + "開始日", BuhinFormat.DATE));
			ret.put("seisankikan_from_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（時）"));
			ret.put("seisankikan_from_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（分）"));
			ret.put("seisankikan_to" , new DenpyouItemInfo(ks.seisankikan.getName() + "終了日", BuhinFormat.DATE));
			ret.put("seisankikan_to_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（時）"));
			ret.put("seisankikan_to_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() +  "終了（分）"));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("goukei_kingaku" , new DenpyouItemInfo(ks.goukeiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("sashihiki_shikyuu_kingaku" , new DenpyouItemInfo(ks.sashihikiShikyuuKingaku.getName(), BuhinFormat.MONEY));
			ret.put("houjin_card_riyou_kingaku" , new DenpyouItemInfo(ks.uchiHoujinCardRiyouGoukei.getName(), BuhinFormat.MONEY));
			ret.put("kaisha_tehai_kingaku" , new DenpyouItemInfo(ks.kaishaTehaiGoukei.getName(), BuhinFormat.MONEY));
			ret.put("sashihiki_num" , new DenpyouItemInfo(sashihikiName));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "ryohiseisan_meisai":
			denpyouKbn = DENPYOU_KBN.RYOHI_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shubetsu1" , new DenpyouItemInfo(ks.shubetsu1.getName()));
			ret.put("shubetsu2" , new DenpyouItemInfo(ks.shubetsu2.getName()));
			ret.put("kikan_from" , new DenpyouItemInfo(ks.kikan.getName() + "開始日", BuhinFormat.DATE));
			ret.put("kikan_to" , new DenpyouItemInfo(ks.kikan.getName() + "終了日", BuhinFormat.DATE));
			ret.put("kyuujitsu_nissuu" , new DenpyouItemInfo(ks.kyuujitsuNissuu.getName()));
			ret.put("koutsuu_shudan" , new DenpyouItemInfo(ks.koutsuuShudan.getName()));
			ret.put("ryoushuusho_seikyuusho_tou_flg" , new DenpyouItemInfo(ks.ryoushuushoSeikyuushoTouFlg.getName(), mapFlg));
			ret.put("naiyou" , new DenpyouItemInfo(ks.naiyouKoutsuuhi.getName()));
			ret.put("bikou"	, new DenpyouItemInfo(ks.bikouKoutsuuhi.getName()));
			ret.put("tanka" , new DenpyouItemInfo(ks.tanka.getName(), BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou" , new DenpyouItemInfo("数量", BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou_kigou" , new DenpyouItemInfo("数量記号"));
			ret.put("nissuu" , new DenpyouItemInfo(ks.nissuu.getName()));
			ret.put("houjin_card_riyou_flg" , new DenpyouItemInfo(ks.houjinCardRiyou.getName(), mapFlg));
			ret.put("kaisha_tehai_flg" , new DenpyouItemInfo(ks.kaishaTehai.getName(), mapFlg));
			ret.put("oufuku_flg" , new DenpyouItemInfo(ks.oufukuFlg.getName(), mapFlg));
			ret.put("meisai_kingaku" , new DenpyouItemInfo(ks.meisaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			break;
		case "ryohi_karibarai":
			denpyouKbn = DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shain_no" , new DenpyouItemInfo(ks.userName.getName() + "(社員番号)"));
			ret.put("user_sei" , new DenpyouItemInfo(ks.userName.getName() + "(姓)"));
			ret.put("user_mei" , new DenpyouItemInfo(ks.userName.getName() + "(名)"));
			ret.put("karibarai_on" , new DenpyouItemInfo(ks.karibaraiOn.getName(), mapFlg));
			ret.put("houmonsaki" , new DenpyouItemInfo(ks.houmonsaki.getName()));
			ret.put("mokuteki" , new DenpyouItemInfo(ks.mokuteki.getName()));
			ret.put("seisankikan_from" , new DenpyouItemInfo(ks.seisankikan.getName() + "開始日", BuhinFormat.DATE));
			ret.put("seisankikan_from_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（時）"));
			ret.put("seisankikan_from_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（分）"));
			ret.put("seisankikan_to" , new DenpyouItemInfo(ks.seisankikan.getName() + "終了日",  BuhinFormat.DATE));
			ret.put("seisankikan_to_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（時）"));
			ret.put("seisankikan_to_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（分）"));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("kingaku" , new DenpyouItemInfo(ks.shinseiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("karibarai_kingaku" , new DenpyouItemInfo(ks.karibaraiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("sashihiki_num" , new DenpyouItemInfo(sashihikiName));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			break;
		case "ryohi_karibarai_meisai":
			denpyouKbn = DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shubetsu1" , new DenpyouItemInfo(ks.shubetsu1.getName()));
			ret.put("shubetsu2" , new DenpyouItemInfo(ks.shubetsu2.getName()));
			ret.put("kikan_from" , new DenpyouItemInfo(ks.kikan.getName() + "開始日", BuhinFormat.DATE));
			ret.put("kikan_to" , new DenpyouItemInfo(ks.kikan.getName() + "終了日", BuhinFormat.DATE));
			ret.put("kyuujitsu_nissuu" , new DenpyouItemInfo(ks.kyuujitsuNissuu.getName()));
			ret.put("koutsuu_shudan" , new DenpyouItemInfo(ks.koutsuuShudan.getName()));
			ret.put("naiyou" , new DenpyouItemInfo(ks.naiyouKoutsuuhi.getName()));
			ret.put("bikou" , new DenpyouItemInfo(ks.bikouKoutsuuhi.getName()));
			ret.put("tanka" , new DenpyouItemInfo(ks.tanka.getName(), BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou" , new DenpyouItemInfo("数量", BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou_kigou" , new DenpyouItemInfo("数量記号"));
			ret.put("nissuu" , new DenpyouItemInfo(ks.nissuu.getName()));
			ret.put("oufuku_flg" , new DenpyouItemInfo(ks.oufukuFlg.getName(), mapFlg));
			ret.put("meisai_kingaku" , new DenpyouItemInfo(ks.meisaiKingaku.getName(), BuhinFormat.MONEY));
			break;
		case "kaigai_ryohiseisan":
			denpyouKbn = DENPYOU_KBN.KAIGAI_RYOHI_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shain_no" , new DenpyouItemInfo(ks.userName.getName() + "(社員番号)"));
			ret.put("user_sei" , new DenpyouItemInfo(ks.userName.getName() + "(姓)"));
			ret.put("user_mei" , new DenpyouItemInfo(ks.userName.getName() + "(名)"));
			ret.put("karibarai_denpyou_id" , new DenpyouItemInfo("仮払" + ks.karibaraiDenpyouId.getName()));
			ret.put("karibarai_mishiyou_flg" , new DenpyouItemInfo(ks.karibaraiMishiyouFlg.getName(), mapFlg));
			ret.put("shucchou_chuushi_flg" , new DenpyouItemInfo(ks.shucchouChuushiFlg.getName(), mapFlg));
			ret.put("houmonsaki" , new DenpyouItemInfo(ks.houmonsaki.getName()));
			ret.put("mokuteki" , new DenpyouItemInfo(ks.mokuteki.getName()));
			ret.put("seisankikan_from" , new DenpyouItemInfo(ks.seisankikan.getName() + "開始日", BuhinFormat.DATE));
			ret.put("seisankikan_from_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（時）"));
			ret.put("seisankikan_from_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（分）"));
			ret.put("seisankikan_to" , new DenpyouItemInfo(ks.seisankikan.getName() + "終了日", BuhinFormat.DATE));
			ret.put("seisankikan_to_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（時）"));
			ret.put("seisankikan_to_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（分）"));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("kaigai_torihiki_name" , new DenpyouItemInfo("海外" + ks.torihiki.getName()));
			ret.put("kaigai_kari_kamoku_name" , new DenpyouItemInfo("海外" + ks.kamoku.getName()));
			ret.put("kaigai_kari_kamoku_edaban_name" , new DenpyouItemInfo("海外" + ks.kamokuEdaban.getName()));
			ret.put("kaigai_kari_futan_bumon_name" , new DenpyouItemInfo("海外" + ks.futanBumon.getName()));
			ret.put("kaigai_torihikisaki_name_ryakushiki", new DenpyouItemInfo("海外" + ks.torihikisaki.getName()));
			ret.put("kaigai_project_name" , new DenpyouItemInfo("海外" + ks.project.getName()));
			ret.put("kaigai_segment_name_ryakushiki" , new DenpyouItemInfo("海外" + ks.segment.getName()));
			ret.put("kaigai_tekiyou" , new DenpyouItemInfo("海外" + ks.tekiyou.getName()));
			ret.put("kaigai_uf1_cd" , new DenpyouItemInfo("海外" + hu.uf1Name));
			ret.put("kaigai_uf2_cd" , new DenpyouItemInfo("海外" + hu.uf2Name));
			ret.put("kaigai_uf3_cd" , new DenpyouItemInfo("海外" + hu.uf3Name));
			ret.put("kaigai_uf4_cd" , new DenpyouItemInfo("海外" + hu.uf4Name));
			ret.put("kaigai_uf5_cd" , new DenpyouItemInfo("海外" + hu.uf5Name));
			ret.put("kaigai_uf6_cd" , new DenpyouItemInfo("海外" + hu.uf6Name));
			ret.put("kaigai_uf7_cd" , new DenpyouItemInfo("海外" + hu.uf7Name));
			ret.put("kaigai_uf8_cd" , new DenpyouItemInfo("海外" + hu.uf8Name));
			ret.put("kaigai_uf9_cd" , new DenpyouItemInfo("海外" + hu.uf9Name));
			ret.put("kaigai_uf10_cd" , new DenpyouItemInfo("海外" + hu.uf10Name));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("goukei_kingaku" , new DenpyouItemInfo(ks.goukeiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("sashihiki_shikyuu_kingaku" , new DenpyouItemInfo(ks.sashihikiShikyuuKingaku.getName(), BuhinFormat.MONEY));
			ret.put("houjin_card_riyou_kingaku" , new DenpyouItemInfo(ks.uchiHoujinCardRiyouGoukei.getName(), BuhinFormat.MONEY));
			ret.put("kaisha_tehai_kingaku" , new DenpyouItemInfo(ks.kaishaTehaiGoukei.getName(), BuhinFormat.MONEY));
			ret.put("kaigai_kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("kari_bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			if(isCheckKaigaiNittouGaikaColumn()) {
				ret.put("sashihiki_heishu_cd_kaigai" , new DenpyouItemInfo(ks.sashihikiHeishuCdKaigai.getName()));
				ret.put("sashihiki_rate_kaigai" , new DenpyouItemInfo(ks.sashihikiRateKaigai.getName()));
				ret.put("sashihiki_tanka_kaigai_gaika" , new DenpyouItemInfo(ks.sashihikiTankaKaigaiGaika.getName()));
				ret.put("sashihiki_tanka_kaigai" , new DenpyouItemInfo(ks.sashihikiTankaKaigai.getName(),BuhinFormat.MONEY));
			}
			ret.put("sashihiki_num_kaigai" , new DenpyouItemInfo(sashihikiNameKaigai));
			ret.put("sashihiki_num" , new DenpyouItemInfo(sashihikiName));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "kaigai_ryohiseisan_meisai":
			denpyouKbn = DENPYOU_KBN.KAIGAI_RYOHI_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("kaigai_flg" , new DenpyouItemInfo(ks.shucchouKbn.getName(), mapKaigaiFlg));
			ret.put("shubetsu1" , new DenpyouItemInfo(ks.shubetsu1.getName()));
			ret.put("shubetsu2" , new DenpyouItemInfo(ks.shubetsu2.getName()));
			ret.put("kikan_from" , new DenpyouItemInfo(ks.kikan.getName()+ "開始日", BuhinFormat.DATE));
			ret.put("kikan_to" , new DenpyouItemInfo(ks.kikan.getName()+ "終了日", BuhinFormat.DATE));
			ret.put("kyuujitsu_nissuu" , new DenpyouItemInfo(ks.kyuujitsuNissuu.getName()));
			ret.put("koutsuu_shudan" , new DenpyouItemInfo(ks.koutsuuShudan.getName()));
			ret.put("ryoushuusho_seikyuusho_tou_flg" , new DenpyouItemInfo(ks.ryoushuushoSeikyuushoTouFlg.getName(), mapFlg));
			ret.put("naiyou" , new DenpyouItemInfo(ks.naiyouKoutsuuhi.getName()));
			ret.put("bikou" , new DenpyouItemInfo(ks.bikouKoutsuuhi.getName()));
			ret.put("heishu_cd" , new DenpyouItemInfo(ks.heishu.getName()));
			ret.put("rate" , new DenpyouItemInfo(ks.rate.getName()));
			ret.put("gaika" , new DenpyouItemInfo(ks.gaika.getName()));
			ret.put("tanka" , new DenpyouItemInfo(ks.tanka.getName(), BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou" , new DenpyouItemInfo("数量", BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou_kigou" , new DenpyouItemInfo("数量記号"));
			ret.put("nissuu" , new DenpyouItemInfo(ks.nissuu.getName()));
			ret.put("houjin_card_riyou_flg" , new DenpyouItemInfo(ks.houjinCardRiyou.getName(), mapFlg));
			ret.put("kaisha_tehai_flg" , new DenpyouItemInfo(ks.kaishaTehai.getName(), mapFlg));
			ret.put("oufuku_flg" , new DenpyouItemInfo(ks.oufukuFlg.getName(), mapFlg));
			ret.put("meisai_kingaku" , new DenpyouItemInfo(ks.meisaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shiharaisaki_name" , new DenpyouItemInfo(ks.shiharaisaki.getName()));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			break;
		case "kaigai_ryohi_karibarai":
			denpyouKbn = DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shain_no" , new DenpyouItemInfo(ks.userName.getName() + "(社員番号)"));
			ret.put("user_sei" , new DenpyouItemInfo(ks.userName.getName() + "(姓)"));
			ret.put("user_mei" , new DenpyouItemInfo(ks.userName.getName() + "(名)"));
			ret.put("karibarai_on" , new DenpyouItemInfo(ks.karibaraiOn.getName(), mapFlg));
			ret.put("houmonsaki" , new DenpyouItemInfo(ks.houmonsaki.getName()));
			ret.put("mokuteki" , new DenpyouItemInfo(ks.mokuteki.getName()));
			ret.put("seisankikan_from" , new DenpyouItemInfo(ks.seisankikan.getName()+ "開始日", BuhinFormat.DATE));
			ret.put("seisankikan_from_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（時）"));
			ret.put("seisankikan_from_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（分）"));
			ret.put("seisankikan_to" , new DenpyouItemInfo(ks.seisankikan.getName()+ "終了日", BuhinFormat.DATE));
			ret.put("seisankikan_to_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（時）"));
			ret.put("seisankikan_to_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（分）"));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("kingaku" , new DenpyouItemInfo(ks.shinseiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("karibarai_kingaku"					, new DenpyouItemInfo(ks.karibaraiKingaku.getName(), BuhinFormat.MONEY));
			if(isCheckKaigaiNittouGaikaColumn()) {
				ret.put("sashihiki_heishu_cd_kaigai" , new DenpyouItemInfo(ks.sashihikiHeishuCdKaigai.getName()));
				ret.put("sashihiki_rate_kaigai" , new DenpyouItemInfo(ks.sashihikiRateKaigai.getName()));
				ret.put("sashihiki_tanka_kaigai_gaika" , new DenpyouItemInfo(ks.sashihikiTankaKaigaiGaika.getName()));
				ret.put("sashihiki_tanka_kaigai" , new DenpyouItemInfo(ks.sashihikiTankaKaigai.getName(),BuhinFormat.MONEY));
			}
			ret.put("sashihiki_num_kaigai" , new DenpyouItemInfo(sashihikiNameKaigai));
			ret.put("sashihiki_num" , new DenpyouItemInfo(sashihikiName));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			break;
		case "kaigai_ryohi_karibarai_meisai":
			denpyouKbn = DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("kaigai_flg" , new DenpyouItemInfo(ks.shucchouKbn.getName(), mapKaigaiFlg));
			ret.put("shubetsu1" , new DenpyouItemInfo(ks.shubetsu1.getName()));
			ret.put("shubetsu2" , new DenpyouItemInfo(ks.shubetsu2.getName()));
			ret.put("kikan_from" , new DenpyouItemInfo(ks.kikan.getName()+ "開始日", BuhinFormat.DATE));
			ret.put("kikan_to" , new DenpyouItemInfo(ks.kikan.getName()+ "終了日", BuhinFormat.DATE));
			ret.put("kyuujitsu_nissuu" , new DenpyouItemInfo(ks.kyuujitsuNissuu.getName()));
			ret.put("koutsuu_shudan" , new DenpyouItemInfo(ks.koutsuuShudan.getName()));
			ret.put("naiyou" , new DenpyouItemInfo(ks.naiyouKoutsuuhi.getName()));
			ret.put("bikou" , new DenpyouItemInfo(ks.bikouKoutsuuhi.getName()));
			ret.put("heishu_cd" , new DenpyouItemInfo(ks.heishu.getName()));
			ret.put("rate" , new DenpyouItemInfo(ks.rate.getName()));
			ret.put("gaika" , new DenpyouItemInfo(ks.gaika.getName()));
			ret.put("tanka" , new DenpyouItemInfo(ks.tanka.getName(), BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou" , new DenpyouItemInfo("数量", BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou_kigou" , new DenpyouItemInfo("数量記号"));
			ret.put("nissuu" , new DenpyouItemInfo(ks.nissuu.getName()));
			ret.put("oufuku_flg" , new DenpyouItemInfo(ks.oufukuFlg.getName(), mapFlg));
			ret.put("meisai_kingaku" , new DenpyouItemInfo(ks.meisaiKingaku.getName(), BuhinFormat.MONEY));
			break;
		case "koutsuuhiseisan":
			denpyouKbn = DENPYOU_KBN.KOUTSUUHI_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("mokuteki" , new DenpyouItemInfo(ks.mokuteki.getName()));
			ret.put("seisankikan_from" , new DenpyouItemInfo(ks.seisankikan.getName() +  "開始日", BuhinFormat.DATE));
			ret.put("seisankikan_from_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（時）"));
			ret.put("seisankikan_from_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "開始（分）"));
			ret.put("seisankikan_to" , new DenpyouItemInfo(ks.seisankikan.getName() + "終了日", BuhinFormat.DATE));
			ret.put("seisankikan_to_hour" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（時）"));
			ret.put("seisankikan_to_min" , new DenpyouItemInfo(ks.seisankikanJikoku.getName() + "終了（分）"));
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("shiharaikiboubi" , new DenpyouItemInfo(ks.shiharaiKiboubi.getName(), BuhinFormat.DATE));
			ret.put("shiharaihouhou" , new DenpyouItemInfo(ks.shiharaiHouhou.getName(), mapShiharaiHouhou));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("goukei_kingaku" , new DenpyouItemInfo(ks.goukeiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("houjin_card_riyou_kingaku" , new DenpyouItemInfo(ks.uchiHoujinCardRiyouGoukei.getName(), BuhinFormat.MONEY));
			ret.put("kaisha_tehai_kingaku" , new DenpyouItemInfo(ks.kaishaTehaiGoukei.getName(), BuhinFormat.MONEY));
			ret.put("sashihiki_shikyuu_kingaku" , new DenpyouItemInfo(ks.sashihikiShikyuuKingaku.getName(), BuhinFormat.MONEY));
			ret.put("keijoubi" , new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("shiharaibi" , new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "koutsuuhiseisan_meisai":
			denpyouKbn = DENPYOU_KBN.KOUTSUUHI_SEISAN;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("shubetsu1" , new DenpyouItemInfo(ks.shubetsu1.getName()));
			ret.put("shubetsu2" , new DenpyouItemInfo(ks.shubetsu2.getName()));
			ret.put("kikan_from" , new DenpyouItemInfo(ks.kikan.getName(), BuhinFormat.DATE));
			ret.put("koutsuu_shudan" , new DenpyouItemInfo(ks.koutsuuShudan.getName()));
			ret.put("ryoushuusho_seikyuusho_tou_flg"	, new DenpyouItemInfo(ks.ryoushuushoSeikyuushoTouFlg.getName(), mapFlg));
			ret.put("naiyou" , new DenpyouItemInfo(ks.naiyouKoutsuuhi.getName()));
			ret.put("bikou" , new DenpyouItemInfo(ks.bikouKoutsuuhi.getName()));
			ret.put("tanka" , new DenpyouItemInfo(ks.tanka.getName(), BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou" , new DenpyouItemInfo("数量", BuhinFormat.MONEY_DECIMAL));
			ret.put("suuryou_kigou" , new DenpyouItemInfo("数量記号"));
			ret.put("oufuku_flg" , new DenpyouItemInfo(ks.oufukuFlg.getName(), mapFlg));
			ret.put("houjin_card_riyou_flg" , new DenpyouItemInfo(ks.houjinCardRiyou.getName(), mapFlg));
			ret.put("kaisha_tehai_flg" , new DenpyouItemInfo(ks.kaishaTehai.getName(), mapFlg));
			ret.put("meisai_kingaku" , new DenpyouItemInfo(ks.meisaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "tsuukinteiki":
			denpyouKbn = DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("torihikisaki_name_ryakushiki" , new DenpyouItemInfo(ks.torihikisaki.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki" , new DenpyouItemInfo(ks.segment.getName()));
			ret.put("shiharaisaki_name" , new DenpyouItemInfo(ks.shiharaisaki.getName()));
			ret.put("jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("shiyou_kikan_kbn" , new DenpyouItemInfo(ks.shiyouKikanKbn.getName(), mapShiyouKikanKbn));
			ret.put("shiyou_kaishibi" , new DenpyouItemInfo(ks.shiyouKaishibi.getName(), BuhinFormat.DATE));
			ret.put("shiyou_shuuryoubi" , new DenpyouItemInfo(ks.shiyouShuuryoubi.getName(), BuhinFormat.DATE));
			ret.put("jyousha_kukan" , new DenpyouItemInfo(ks.jyoushaKukan.getName()));
			ret.put("kingaku" , new DenpyouItemInfo(ks.kingaku.getName(), BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("shiharaibi", new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "furikae":
			denpyouKbn = DENPYOU_KBN.FURIKAE_DENPYOU;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("denpyou_date" , new DenpyouItemInfo(ks.denpyouDate.getName(), BuhinFormat.DATE));
			ret.put("shouhyou_shorui_flg" , new DenpyouItemInfo(ks.shouhyouShoruiFlg.getName(), mapFlg));
			ret.put("kingaku" , new DenpyouItemInfo(ks.kingaku.getName(), BuhinFormat.MONEY));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			
			ret.put("kari_kamoku_name" , new DenpyouItemInfo("借方" + ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo("借方" + ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("kari_zeiritsu" , new DenpyouItemInfo("借方" + ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("kari_bunri_kbn" , new DenpyouItemInfo("借方" + ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo("借方" + ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo("借方" + ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo("借方" + ks.futanBumon.getName()));
			ret.put("kari_torihikisaki_name_ryakushiki"	, new DenpyouItemInfo("借方" + ks.torihikisaki.getName()));
			ret.put("kari_jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("kari_zeigaku_houshiki" , new DenpyouItemInfo(ks.uriagezeigakuKeisan.getName(), mapZeigakuHoushiki));
			ret.put("kari_project_name" , new DenpyouItemInfo("借方" + ks.project.getName()));
			ret.put("kari_segment_name_ryakushiki" , new DenpyouItemInfo("借方" + ks.segment.getName()));
			ret.put("kari_uf1_cd" , new DenpyouItemInfo("借方" + hu.uf1Name));
			ret.put("kari_uf2_cd" , new DenpyouItemInfo("借方" + hu.uf2Name));
			ret.put("kari_uf3_cd" , new DenpyouItemInfo("借方" + hu.uf3Name));
			ret.put("kari_uf4_cd" , new DenpyouItemInfo("借方" + hu.uf4Name));
			ret.put("kari_uf5_cd" , new DenpyouItemInfo("借方" + hu.uf5Name));
			ret.put("kari_uf6_cd" , new DenpyouItemInfo("借方" + hu.uf6Name));
			ret.put("kari_uf7_cd" , new DenpyouItemInfo("借方" + hu.uf7Name));
			ret.put("kari_uf8_cd" , new DenpyouItemInfo("借方" + hu.uf8Name));
			ret.put("kari_uf9_cd" , new DenpyouItemInfo("借方" + hu.uf9Name));
			ret.put("kari_uf10_cd" , new DenpyouItemInfo("借方" + hu.uf10Name));
			
			ret.put("kashi_kamoku_name" , new DenpyouItemInfo("貸方" + ks.kamoku.getName()));
			ret.put("kashi_kazei_kbn" , new DenpyouItemInfo("貸方" + ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("kashi_zeiritsu" , new DenpyouItemInfo("貸方" + ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("kashi_bunri_kbn" , new DenpyouItemInfo("貸方" + ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kashi_shiire_kbn" , new DenpyouItemInfo("貸方" + ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kashi_kamoku_edaban_name" , new DenpyouItemInfo("貸方" + ks.kamokuEdaban.getName()));
			ret.put("kashi_futan_bumon_name" , new DenpyouItemInfo("貸方" + ks.futanBumon.getName()));
			ret.put("kashi_torihikisaki_name_ryakushiki", new DenpyouItemInfo("貸方" + ks.torihikisaki.getName()));
			ret.put("kashi_jigyousha_kbn" , new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("kashi_zeigaku_houshiki" , new DenpyouItemInfo(ks.uriagezeigakuKeisan.getName(), mapZeigakuHoushiki));
			ret.put("kashi_segment_name_ryakushiki" , new DenpyouItemInfo("貸方" + ks.segment.getName()));
			ret.put("kashi_project_name" , new DenpyouItemInfo("貸方" + ks.project.getName()));
			ret.put("kashi_uf1_cd", new DenpyouItemInfo("貸方" + hu.uf1Name));
			ret.put("kashi_uf2_cd", new DenpyouItemInfo("貸方" + hu.uf2Name));
			ret.put("kashi_uf3_cd", new DenpyouItemInfo("貸方" + hu.uf3Name));
			ret.put("kashi_uf4_cd", new DenpyouItemInfo("貸方" + hu.uf4Name));
			ret.put("kashi_uf5_cd", new DenpyouItemInfo("貸方" + hu.uf5Name));
			ret.put("kashi_uf6_cd", new DenpyouItemInfo("貸方" + hu.uf6Name));
			ret.put("kashi_uf7_cd", new DenpyouItemInfo("貸方" + hu.uf7Name));
			ret.put("kashi_uf8_cd", new DenpyouItemInfo("貸方" + hu.uf8Name));
			ret.put("kashi_uf9_cd", new DenpyouItemInfo("貸方" + hu.uf9Name));
			ret.put("kashi_uf10_cd", new DenpyouItemInfo("貸方" + hu.uf10Name));
			
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("bikou" , new DenpyouItemInfo(ks.bikou.getName()));
			break;
		case "tsukekae":
			denpyouKbn = DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("denpyou_date" , new DenpyouItemInfo(ks.denpyouDate.getName(), BuhinFormat.DATE));
			ret.put("shouhyou_shorui_flg" , new DenpyouItemInfo(ks.shouhyouShoruiFlg.getName(), mapFlg));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName()));
			ret.put("tsukekae_kbn" , new DenpyouItemInfo("付替区分", mapTsukekaeKbn));
			ret.put("moto_kamoku_name" , new DenpyouItemInfo("付替元" + ks.kamoku.getName()));
			ret.put("moto_kazei_kbn" , new DenpyouItemInfo("付替元" + ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("moto_bunri_kbn" , new DenpyouItemInfo("付替元" + ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("moto_shiire_kbn" , new DenpyouItemInfo("付替元" + ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("moto_kamoku_edaban_name" , new DenpyouItemInfo("付替元" + ks.kamokuEdaban.getName()));
			ret.put("moto_futan_bumon_name" , new DenpyouItemInfo("付替元" + ks.futanBumon.getName()));
			ret.put("moto_torihikisaki_name_ryakushiki"	, new DenpyouItemInfo("付替元" + ks.torihikisaki.getName()));
			ret.put("moto_jigyousha_kbn" , new DenpyouItemInfo("付替元" + ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("moto_zeigaku_houshiki" , new DenpyouItemInfo("付替元" + ks.uriagezeigakuKeisan.getName(), mapZeigakuHoushiki));
			ret.put("moto_project_name" , new DenpyouItemInfo("付替元" + ks.project.getName()));
			ret.put("moto_segment_name_ryakushiki"		, new DenpyouItemInfo("付替元" + ks.segment.getName()));
			ret.put("moto_uf1_cd", new DenpyouItemInfo("付替元" + hu.uf1Name));
			ret.put("moto_uf2_cd", new DenpyouItemInfo("付替元" + hu.uf2Name));
			ret.put("moto_uf3_cd", new DenpyouItemInfo("付替元" + hu.uf3Name));
			ret.put("moto_uf4_cd", new DenpyouItemInfo("付替元" + hu.uf4Name));
			ret.put("moto_uf5_cd", new DenpyouItemInfo("付替元" + hu.uf5Name));
			ret.put("moto_uf6_cd", new DenpyouItemInfo("付替元" + hu.uf6Name));
			ret.put("moto_uf7_cd", new DenpyouItemInfo("付替元" + hu.uf7Name));
			ret.put("moto_uf8_cd", new DenpyouItemInfo("付替元" + hu.uf8Name));
			ret.put("moto_uf9_cd", new DenpyouItemInfo("付替元" + hu.uf9Name));
			ret.put("moto_uf10_cd", new DenpyouItemInfo("付替元" + hu.uf10Name));
			ret.put("kingaku_goukei" , new DenpyouItemInfo(ks.kingakuGoukei.getName(), BuhinFormat.MONEY));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			break;
		case "tsukekae_meisai":
			denpyouKbn = DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("saki_kamoku_name" , new DenpyouItemInfo("付替先" + ks.kamoku.getName()));
			ret.put("saki_kazei_kbn" , new DenpyouItemInfo("付替先" + ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("saki_bunri_kbn" , new DenpyouItemInfo("付替先" + ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("saki_shiire_kbn" , new DenpyouItemInfo("付替先" + ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("saki_kamoku_edaban_name" , new DenpyouItemInfo("付替先" + ks.kamokuEdaban.getName()));
			ret.put("saki_futan_bumon_name" , new DenpyouItemInfo("付替先" + ks.futanBumon.getName()));
			ret.put("saki_torihikisaki_name_ryakushiki"	, new DenpyouItemInfo("付替先" + ks.torihikisaki.getName()));
			ret.put("saki_jigyousha_kbn" , new DenpyouItemInfo("付替先" + ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("saki_zeigaku_houshiki" , new DenpyouItemInfo("付替先" + ks.uriagezeigakuKeisan.getName(), mapZeigakuHoushiki));
			ret.put("saki_project_name" , new DenpyouItemInfo("付替先" + ks.project.getName()));
			ret.put("saki_segment_name_ryakushiki" , new DenpyouItemInfo("付替先" + ks.segment.getName()));
			ret.put("saki_uf1_cd", new DenpyouItemInfo("付替先" + hu.uf1Name));
			ret.put("saki_uf2_cd", new DenpyouItemInfo("付替先" + hu.uf2Name));
			ret.put("saki_uf3_cd", new DenpyouItemInfo("付替先" + hu.uf3Name));
			ret.put("saki_uf4_cd", new DenpyouItemInfo("付替先" + hu.uf4Name));
			ret.put("saki_uf5_cd", new DenpyouItemInfo("付替先" + hu.uf5Name));
			ret.put("saki_uf6_cd", new DenpyouItemInfo("付替先" + hu.uf6Name));
			ret.put("saki_uf7_cd", new DenpyouItemInfo("付替先" + hu.uf7Name));
			ret.put("saki_uf8_cd", new DenpyouItemInfo("付替先" + hu.uf8Name));
			ret.put("saki_uf9_cd", new DenpyouItemInfo("付替先" + hu.uf9Name));
			ret.put("saki_uf10_cd", new DenpyouItemInfo("付替先" + hu.uf10Name));
			ret.put("kingaku" , new DenpyouItemInfo(ks.kingaku.getName(), BuhinFormat.MONEY));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			ret.put("bikou" , new DenpyouItemInfo(ks.bikou.getName()));
			break;
		case "shiharai_irai":
			denpyouKbn = DENPYOU_KBN.SIHARAIIRAI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("torihikisaki_name_ryakushiki", new DenpyouItemInfo(ks.torihikisaki.getName(), BuhinFormat.STRING));
			ret.put("jigyousha_kbn", new DenpyouItemInfo(ks.jigyoushaKbn.getName(), mapJigyoushaKbn));
			ret.put("jigyousha_no", new DenpyouItemInfo(ks.jigyoushaNo.getName()));
			ret.put("shiharai_houhou", new DenpyouItemInfo("支払方法", mapShiharaiIraiHouhou));
			ret.put("shiharai_shubetsu", new DenpyouItemInfo("支払種別", mapShiharaiIraiShubetsu));
			ret.put("keijoubi", new DenpyouItemInfo("計上日", BuhinFormat.DATE));
			ret.put("yoteibi", new DenpyouItemInfo(ks.yoteibi.getName(), BuhinFormat.DATE));
			ret.put("shiharaibi", new DenpyouItemInfo("支払日", BuhinFormat.DATE));
			ret.put("shiharai_kijitsu", new DenpyouItemInfo("支払期日", BuhinFormat.DATE));
			ret.put("ichigensaki_torihikisaki_name", new DenpyouItemInfo(ks.shiharaisaki.getName(), BuhinFormat.STRING));
			ret.put("edi", new DenpyouItemInfo("EDI", BuhinFormat.STRING));
			ret.put("shiharai_goukei", new DenpyouItemInfo(ks.shiharaiGoukei.getName(), BuhinFormat.MONEY));
			ret.put("sousai_goukei"	, new DenpyouItemInfo(ks.sousaiGoukei.getName(), BuhinFormat.STRING));
			ret.put("sashihiki_shiharai", new DenpyouItemInfo(ks.sashihikiShiharaiGaku.getName(), BuhinFormat.MONEY));
			ret.put("manekin_gensen", new DenpyouItemInfo(setting.manekinName(), BuhinFormat.STRING));
			ret.put("shouhyou_shorui_flg", new DenpyouItemInfo(ks.shouhyouShoruiFlg.getName(), mapFlg));
			ret.put("furikomi_ginkou_cd", new DenpyouItemInfo("銀行コード", BuhinFormat.STRING));
			ret.put("furikomi_ginkou_name", new DenpyouItemInfo("銀行名", BuhinFormat.STRING));
			ret.put("furikomi_ginkou_shiten_cd"	, new DenpyouItemInfo("支店コード", BuhinFormat.STRING));
			ret.put("furikomi_ginkou_shiten_name", new DenpyouItemInfo("支店名", BuhinFormat.STRING));
			ret.put("yokin_shubetsu", new DenpyouItemInfo("預金種別", mapYokinShubetsu));
			ret.put("kouza_bangou", new DenpyouItemInfo("口座番号", BuhinFormat.STRING));
			ret.put("kouza_meiginin" , new DenpyouItemInfo("口座名義人(ｶﾅ)", BuhinFormat.STRING));
			ret.put("tesuuryou" , new DenpyouItemInfo("手数料", mapTesuuryou));
			ret.put("hosoku" , new DenpyouItemInfo(ks.hosoku.getName(), BuhinFormat.STRING));
			ret.put("nyuryoku_houshiki" , new DenpyouItemInfo(ks.nyuryokuHoushiki.getName() , mapNyuryokuFlg));
			ret.put("invoice_denpyou" , new DenpyouItemInfo("インボイス対応伝票" , mapInvoiceDenpyou));
			break;
		case "shiharai_irai_meisai":
			denpyouKbn = DENPYOU_KBN.SIHARAIIRAI;
			ks = new GamenKoumokuSeigyo(denpyouKbn);
			ret.put("torihiki_name" , new DenpyouItemInfo(ks.torihiki.getName()));
			ret.put("kari_kamoku_name" , new DenpyouItemInfo(ks.kamoku.getName()));
			ret.put("kari_kazei_kbn" , new DenpyouItemInfo(ks.kazeiKbn.getName(), mapKazeiKbn));
			ret.put("zeiritsu" , new DenpyouItemInfo(ks.zeiritsu.getName(), mapZeiritsu));
			ret.put("bunri_kbn" , new DenpyouItemInfo(ks.bunriKbn.getName() ,mapBunriKbn));
			ret.put("kari_shiire_kbn" , new DenpyouItemInfo(ks.shiireKbn.getName(), mapShiireKbn));
			ret.put("kari_kamoku_edaban_name" , new DenpyouItemInfo(ks.kamokuEdaban.getName()));
			ret.put("kari_futan_bumon_name" , new DenpyouItemInfo(ks.futanBumon.getName()));
			ret.put("project_name" , new DenpyouItemInfo(ks.project.getName()));
			ret.put("segment_name_ryakushiki", new DenpyouItemInfo(ks.segment.getName()));
			ret.put("shiharai_kingaku" , new DenpyouItemInfo(ks.shiharaiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("zeinuki_kingaku" , new DenpyouItemInfo(ks.zeinukiKingaku.getName(), BuhinFormat.MONEY));
			ret.put("shouhizeigaku" , new DenpyouItemInfo(ks.shouhizeigaku.getName(), BuhinFormat.MONEY));
			ret.put("tekiyou" , new DenpyouItemInfo(ks.tekiyou.getName()));
			break;
		}
		
		// カスタマイズ用
		ret = getDenpyouItemInfo_Ext(tableName, ret, denpyouKbn);
		
		//--------------------
		//各伝票共通（一部テーブルには該当しないのだが）
		//--------------------
		ret.put("hf1_cd", new DenpyouItemInfo(hu.hf1Name));
		ret.put("hf2_cd", new DenpyouItemInfo(hu.hf2Name));
		ret.put("hf3_cd", new DenpyouItemInfo(hu.hf3Name));
		ret.put("hf4_cd", new DenpyouItemInfo(hu.hf4Name));
		ret.put("hf5_cd", new DenpyouItemInfo(hu.hf5Name));
		ret.put("hf6_cd", new DenpyouItemInfo(hu.hf6Name));
		ret.put("hf7_cd", new DenpyouItemInfo(hu.hf7Name));
		ret.put("hf8_cd", new DenpyouItemInfo(hu.hf8Name));
		ret.put("hf9_cd", new DenpyouItemInfo(hu.hf9Name));
		ret.put("hf10_cd", new DenpyouItemInfo(hu.hf10Name));
		ret.put("uf1_cd", new DenpyouItemInfo(hu.uf1Name));
		ret.put("uf2_cd", new DenpyouItemInfo(hu.uf2Name));
		ret.put("uf3_cd", new DenpyouItemInfo(hu.uf3Name));
		ret.put("uf4_cd", new DenpyouItemInfo(hu.uf4Name));
		ret.put("uf5_cd", new DenpyouItemInfo(hu.uf5Name));
		ret.put("uf6_cd", new DenpyouItemInfo(hu.uf6Name));
		ret.put("uf7_cd", new DenpyouItemInfo(hu.uf7Name));
		ret.put("uf8_cd", new DenpyouItemInfo(hu.uf8Name));
		ret.put("uf9_cd", new DenpyouItemInfo(hu.uf9Name));
		ret.put("uf10_cd", new DenpyouItemInfo(hu.uf10Name));
		
		return ret;
	}
	
	/**
	 * @param naibu_cd 内部コード
	 * @return 内部コード設定マップ
	 */
	protected Map<String, String> getNaibuCdSettingMap (String naibu_cd) {
	
		Map<String, String> mapRtn = new HashMap<String, String>();
		List<GMap> kazeiKbnList = systemLogic.loadNaibuCdSetting(naibu_cd);
		for(GMap map : kazeiKbnList) {
			mapRtn.put(map.get("naibu_cd").toString(), map.get("name").toString());
		} 
		return mapRtn;
	}
	
	/**
	 * @return 差引項目（幣種コード、レート、単価（邦貨・外貨）のチェック要否
	 */
	protected boolean isCheckKaigaiNittouGaikaColumn() {
		if (false == systemLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA))
		{
			return false;
		}
		if (!"1".equals(setting.kaigaiNittouTankaGaikaSettei()))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * /**
	 * カスタマイズ用
	 * @param tableName テーブル名
	 * @param ret 変更伝票内容
	 * @param denpyouKbn 伝票区分
	 * @return 結果セット
	 */
	protected Map<String, DenpyouItemInfo> getDenpyouItemInfo_Ext(
			  String tableName
			, Map<String, DenpyouItemInfo> ret
			, String denpyouKbn) {
		return ret;
	}
}
