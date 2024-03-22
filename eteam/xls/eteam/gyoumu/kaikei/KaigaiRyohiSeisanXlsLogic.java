package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KAMOKU_KAZEI_KBN_GROUP;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiKaribaraiAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiseisanAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KaigaiRyohiKaribaraiDao;
import eteam.database.dao.KaigaiRyohiseisanDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dto.DenpyouShubetsuIchiran;
import eteam.database.dto.KaigaiRyohiKaribarai;
import eteam.database.dto.KaigaiRyohiseisan;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 海外出張旅費精算(仮払清算) Logic
 */
public class KaigaiRyohiSeisanXlsLogic extends KaikeiCommonXlsLogic {
	
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN); 
	/** 画面項目制御クラス(経費明細) */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	/** 画面項目制御クラス(海外旅費仮払) */
	GamenKoumokuSeigyo ksKari = new GamenKoumokuSeigyo(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
	
	/** 画面項目制御クラス(経費明細) */
	/**  出張区分：海外 */
	static final String SHUCCHOU_KAIGAI = "1";
	/**  出張区分：国内 */
	static final String SHUCCHOU_KOKUNAI = "0";

	/**
	 * 帳票PDF変換用のexcelファイルを作成します。
	 * @param denpyouId 伝票ID
	 * @param out 出力ストリーム
	 */
	public void makeExcel(String denpyouId, OutputStream out) {
		
		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		KaikeiCommonXlsLogic kaikeiXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		KaigaiRyohiseisanAbstractDao kaigaiRyohiseisanDao = EteamContainer.getComponent(KaigaiRyohiseisanDao.class, connection);
		KaigaiRyohiKaribaraiAbstractDao kaigaiRyohiKaribaraiDao = EteamContainer.getComponent(KaigaiRyohiKaribaraiDao.class, connection);
		NaibuCdSettingAbstractDao   	naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		KiShouhizeiSettingDao kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kaikeiXlsLogic.init(ks,ks1);

		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_kaigairyohiseisan.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell shucchousakiCell = book.findCellByName("shucchousaki");
		int shucchousakiRow = shucchousakiCell.getRow();
		int shucchousakiCol = shucchousakiCell.getColumn();
		Cell meisaiCell11 = book.findCellByName("meisai_no11");
		int meisaiRow11 = meisaiCell11.getRow();
		Cell meisaiCell12 = book.findCellByName("meisai_no12");
		int meisaiRow12 = meisaiCell12.getRow();
		Cell meisaiCell01 = book.findCellByName("meisai_no01");
		Cell meisaiCell02 = book.findCellByName("meisai_no02");
		int meisaiRow01 = meisaiCell01.getRow();
		int meisaiRow02 = meisaiCell02.getRow();
		Cell meisaiCell = book.findCellByName("meisai_no");
		int meisaiRow = meisaiCell.getRow();
		int meisaiCol = meisaiCell.getColumn();
		Cell hosokuCell = book.findCellByName("hosoku");
		int hosokuRow = hosokuCell.getRow();
		int hosokuCol = hosokuCell.getColumn();

		// ---------------------------------------
		//共通部分（下部）
		// ---------------------------------------
		wfXlsLogic.makeExcelBottom(excel, denpyouId);

		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		KaigaiRyohiseisan hontai = kaigaiRyohiseisanDao.find(denpyouId); //kaigai_ryohiseisan
		List<GMap> 	meisaiList = kaikeiLogic.loadKaigaiRyohiSeisanMeisai(denpyouId); //kaigai_ryohiseisan_meisai
		List<GMap> 	meisaiKeihiList = kaikeiLogic.loadKaigaiRyohiKeihiseisanMeisai(denpyouId); //kaigai_ryohiseisan_keihi_meisai
		String hosoku = hontai.hosoku;
		String shiharaihouhouCd = hontai.shiharaihouhou;
		DenpyouShubetsuIchiran denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN);
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN, hontai.map, null, "0");
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;	
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
		hontai.map.put("kaigaiTekiyouFlg", "1");
		shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN, hontai.map, null, "0");
		String chuukiKaigai = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
		boolean houjinCardRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean kaishatehaiRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		var kazeiInfo = naibuCdSettingDao.find("kazei_kbn", hontai.kariKazeiKbn);
		String kazeiKbnGroup = (null != kazeiInfo)? kazeiInfo.option1 : "";
		boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSettingDao.findByDate(null).shiireZeigakuAnbunFlg;
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String shiyoushaTitle = "";
		String shiyousha = "";
		String torihikiRyohiTitle = "";
		String torihiki = "";
		String seisankikanTitle = "";
		String seisankikan = "";
		String shucchousakiTitle = "";
		String shucchousaki = "";
		String shiharaikiboubiTitle = "";
		String shiharaikiboubi = "";
		String shiharaihouhouTitle = "";
		String shiharaihouhou = "";
		String karibaraiOn = "";
		String karibaraidenpyouidTitle = "";
		String karibaraidenpyouid = "";
		String mokutekiTitle = "";
		String mokuteki = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";
		String kamokuTitle = "";
		String kamoku = "";
		String kazeiKbnTitle = "";
		String kazeiKbn = "";
		String zeiritsuTitle = "";
		String zeiritsu = "";
		String bunriKbnTitle = "";
		String bunriKbn = "";
		String shiireKbnTitle = "";
		String shiireKbn = "";
		String edabanTitle = "";
		String edaban = "";
		String futanbumonRyohiTitle = "";
		String futanbumon = "";
		String torihikisakiRyohiTitle = "";
		String torihikisaki = "";
		String tekiyouRyohiTitle = "";
		String tekiyou = "";
		String projectKaigai = "";
		String segmentKaigai = "";
		String torihikiKaigai = "";
		String kamokuKaigai = "";
		String kazeiKbnKaigai = "";
		String edabanKaigai = "";
		String futanbumonKaigai = "";
		String torihikisakiKaigai = "";
		String tekiyouKaigai = "";
		String sashihikishikyuukingakuTitle = "";
		String sashihikishikyuukingaku = "";
		String karibaraikingakuTitle = "";
		String karibaraikingaku = "";
		String houjinkingakuTitle = "";
		String houjinkingaku = "";
		String kaishatehaigoukeiTitle = "";
		String kaishatehaigoukei = "";
		String hikazeikingakuTitle = "";
		String hikazeikingaku = "";
		String kazeikingakuTitle = "";
		String kazeikingaku = "";

		String meisaikingakugoukeiTitle = "";
		String meisaikingakugoukei = "";
		String projectTitle = "";
		String project = "";
		String segmentTitle = "";
		String segment = ""; 
		String hosokutitle = "";
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu);
		//インボイス開始後、かつインボイス前伝票（旧伝票）の場合
		kaikeiXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		// 使用者
		if(ks.userName.getPdfHyoujiFlg() == true){
			shiyoushaTitle = ks.userName.getName();
			shiyousha = hontai.userSei + "　" + hontai.userMei;
		}
		//取引
		if(ks.torihiki.getPdfHyoujiFlg()){
			torihikiRyohiTitle = ks.torihiki.getName();
			if (hontai.shiwakeEdano != null)
			{
				torihiki = ks.torihiki.getCodeSeigyoValue(hontai.shiwakeEdano.toString(), hontai.torihikiName, " ");
			}
			if  (hontai.kaigaiShiwakeEdano != null)
			{
				torihikiKaigai = ks.torihiki.getCodeSeigyoValue(hontai.kaigaiShiwakeEdano.toString(), hontai.kaigaiTorihikiName, " ");
			}
		}
		//出張先・訪問先
		if(ks.houmonsaki.getPdfHyoujiFlg() == true){
			shucchousakiTitle = ks.houmonsaki.getName();
			shucchousaki = hontai.houmonsaki;
		}
		//目的
		if(ks.mokuteki.getPdfHyoujiFlg() == true){
			mokutekiTitle = ks.mokuteki.getName();
			mokuteki = hontai.mokuteki;
		}
		//科目
		if(ks.kamoku.getPdfHyoujiFlg()){
			kamokuTitle = ks.kamoku.getName();
			kamoku = ks.kamoku.getCodeSeigyoValue(hontai.kariKamokuCd,hontai.kariKamokuName, " ");
			kamokuKaigai = ks.kamoku.getCodeSeigyoValue(hontai.kaigaiKariKamokuCd,hontai.kaigaiKariKamokuName, " ");
		}
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()){
			kazeiKbnTitle = ks.kazeiKbn.getName();
			kazeiKbn = sysLogic.findNaibuCdName("kazei_kbn", hontai.kariKazeiKbn);
			kazeiKbnKaigai = sysLogic.findNaibuCdName("kazei_kbn", hontai.kaigaiKariKazeiKbn);
		}
		//税率
		if(ks.zeiritsu.getPdfHyoujiFlg()) {
			zeiritsuTitle = ks.zeiritsu.getName();
			if(zeiritsuHyoujiFlg && !kazeiKbn.isEmpty()) {
				zeiritsu = EteamXlsFmt.formatZeiritsu(hontai.zeiritsu, hontai.keigenZeiritsuKbn) + " %";
			} else {
				zeiritsu = "";
			}
		}
		//分離区分
		if(ks.bunriKbn.getPdfHyoujiFlg()){
			bunriKbnTitle = ks.bunriKbn.getName();
			bunriKbn = sysLogic.findNaibuCdName("bunri_kbn", hontai.bunriKbn);
		}
		//仕入区分
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg){
			shiireKbnTitle = ks.shiireKbn.getName();
			shiireKbn = sysLogic.findNaibuCdName("shiire_kbn", hontai.kariShiireKbn);
		}
		//科目枝番
		if(ks.kamokuEdaban.getPdfHyoujiFlg()){
			edabanTitle = ks.kamokuEdaban.getName();
			edaban = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kariKamokuEdabanCd,hontai.kariKamokuEdabanName, " ");
			edabanKaigai = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kaigaiKariKamokuEdabanCd,hontai.kaigaiKariKamokuEdabanName, " ");
		}
		//負担部門
		if(ks.futanBumon.getPdfHyoujiFlg() == true){
			futanbumonRyohiTitle = ks.futanBumon.getName();
			futanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.kariFutanBumonCd,hontai.kariFutanBumonName, " ");
			futanbumonKaigai = ks.futanBumon.getCodeSeigyoValue(hontai.kaigaiKariFutanBumonCd,hontai.kaigaiKariFutanBumonName, " ");
		}
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiRyohiTitle = ks.torihikisaki.getName();
			torihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.torihikisakiCd,hontai.torihikisakiNameRyakushiki, " ");
			torihikisakiKaigai = ks.torihikisaki.getCodeSeigyoValue(hontai.kaigaiTorihikisakiCd,hontai.kaigaiTorihikisakiNameRyakushiki, " ");
		}
		//摘要
		if(ks.tekiyou.getPdfHyoujiFlg()){
			tekiyou = hontai.tekiyou + chuuki;
			tekiyouKaigai = hontai.kaigaiTekiyou + chuukiKaigai;
			tekiyouRyohiTitle = ks.tekiyou.getName();
		}
		//精算期間&精算時刻
		if(ks.seisankikan.getPdfHyoujiFlg() == true && ks.seisankikanJikoku.getPdfHyoujiFlg() == true){
			seisankikanTitle = ks.seisankikan.getName();
			seisankikan = EteamXlsFmt.formatDate(hontai.seisankikanFrom) + " " 
						+ EteamXlsFmt.formatTimeHHmm(hontai.seisankikanFromHour, hontai.seisankikanFromMin)
						+ " ～ " + EteamXlsFmt.formatDate(hontai.seisankikanTo) + " " 
						+ EteamXlsFmt.formatTimeHHmm(hontai.seisankikanToHour, hontai.seisankikanToMin);
		//精算期間のみ
		} else if (ks.seisankikan.getPdfHyoujiFlg() == true) {
			seisankikanTitle = ks.seisankikan.getName();
			if (!("".equals(shiharaihouhouCd))) {
				seisankikan = EteamXlsFmt.formatDate(hontai.seisankikanFrom) + "～" + EteamXlsFmt.formatDate(hontai.seisankikanTo);
			}
		}
		//支払日(支払日は記載固定)
		shiharaibiTitle = "支払日";
		shiharaibi = EteamXlsFmt.formatDate(hontai.shiharaibi);
		//計上日
		//表示・非表示の判別はsetting_infoを参照
		if(!"3".equals(setting.shiwakeSakuseiHouhouA011())) {
			excel.write("keijoubi", EteamXlsFmt.formatDate(hontai.keijoubi));
		} else {
			excel.write("keijoubi_title", "");
		}
		//支払希望日
		if(ks.shiharaiKiboubi.getPdfHyoujiFlg() == true){
			shiharaikiboubiTitle = "支払" + ks.shiharaiKiboubi.getName();
			shiharaikiboubi = EteamXlsFmt.formatDate(hontai.shiharaikiboubi);
		}
		//差引支給金額
		if(ks.sashihikiShikyuuKingaku.getPdfHyoujiFlg()){
			sashihikishikyuukingakuTitle = ks.sashihikiShikyuuKingaku.getName();
			sashihikishikyuukingaku = EteamXlsFmt.formatMoney(hontai.sashihikiShikyuuKingaku);
			excel.makeBorder("sashihikishikyuukingaku_title");
			excel.makeBorder("sashihikishikyuukingaku");
		}
		//明細金額合計
		if(ks.goukeiKingaku.getPdfHyoujiFlg()){
			meisaikingakugoukeiTitle = ks.goukeiKingaku.getName();
			meisaikingakugoukei = EteamXlsFmt.formatMoney(hontai.goukeiKingaku);
			excel.makeBorder("meisaikingakugoukei_title");
			excel.makeBorder("meisaikingakugoukei");
		}
		//法人カード利用
		if(houjinCardRiyouFlg && ks.uchiHoujinCardRiyouGoukei.getPdfHyoujiFlg()) {
			houjinkingakuTitle = ks.uchiHoujinCardRiyouGoukei.getName();
			houjinkingaku = EteamXlsFmt.formatMoney(hontai.houjinCardRiyouKingaku);
			//枠線を書く
			excel.makeBorder("houjincardkingakugoukei_title");
			excel.makeBorder("houjincardkingakugoukei");
		}
		//会社手配
		if(kaishatehaiRiyouFlg && ks.kaishaTehaiGoukei.getPdfHyoujiFlg()){
			kaishatehaigoukeiTitle = ks.kaishaTehaiGoukei.getName();
			kaishatehaigoukei = EteamXlsFmt.formatMoney(hontai.kaishaTehaiKingaku);
			//枠線をセット
			excel.makeBorder("kaishatehaigoukei_title");
			excel.makeBorder("kaishatehaigoukei");
			excel.makeBorder("kaishatehaigoukei2_title");
			excel.makeBorder("kaishatehaigoukei2");
		}
		
		//差引金額を減算した明細金額をもつ明細リストを作成(非課税金額・課税金額の計算で使用する)
		List<GMap> 	meisaiListCopy = new ArrayList<GMap>();
		for(GMap meisai : meisaiList){
			GMap tmp = new GMap();
			tmp.put("shubetsu_cd",    meisai.get("shubetsu_cd"));
			tmp.put("kaigai_flg",     meisai.get("kaigai_flg"));
			tmp.put("shubetsu1",      meisai.get("shubetsu1"));
			tmp.put("shubetsu2",      meisai.get("shubetsu2"));
			tmp.put("meisai_kingaku", meisai.get("meisai_kingaku"));
			tmp.put("kazei_flg", meisai.get("kazei_flg"));
			meisaiListCopy.add(tmp);
		}
		this.subtractSashihikiKingaku(hontai.map, meisaiListCopy);
		
		//2023/12/13 仮対応
		// 詳細は sumKazeiKingaku(L.678) に記載しています
		//非課税金額
		if(ks.hiKazeiKingaku.getPdfHyoujiFlg()){
			hikazeikingakuTitle = ks.hiKazeiKingaku.getName();
			hikazeikingaku = EteamXlsFmt.formatMoney(sumKazeiKingaku(KAMOKU_KAZEI_KBN_GROUP.HIKAZEI, meisaiListCopy, meisaiKeihiList, hontai.kariKazeiKbn, hontai.kaigaiKariKazeiKbn));
			excel.makeBorder("hikazeikingaku_title");
			excel.makeBorder("hikazeikingaku");
			excel.makeBorder("hikazeikingaku2_title");
			excel.makeBorder("hikazeikingaku2");
		}
		//課税金額
		if(ks.kazeiKingaku.getPdfHyoujiFlg()){
			kazeikingakuTitle = ks.kazeiKingaku.getName();
			kazeikingaku = EteamXlsFmt.formatMoney(sumKazeiKingaku(KAMOKU_KAZEI_KBN_GROUP.KAZEI, meisaiListCopy, meisaiKeihiList, hontai.kariKazeiKbn, hontai.kaigaiKariKazeiKbn));
			excel.makeBorder("kazeikingaku_title");
			excel.makeBorder("kazeikingaku");
			excel.makeBorder("kazeikingaku2_title");
			excel.makeBorder("kazeikingaku2");
		}

		//仮払伝票ID
		karibaraiOn = hontai.karibaraiOn;
		if(ks.karibaraiDenpyouId.getPdfHyoujiFlg()){
			if(karibaraiOn.equals("0")) {
				karibaraidenpyouidTitle = "伺い伝票ID";
			}else if(karibaraiOn.equals("1")) {
				karibaraidenpyouidTitle = "仮払" + ks.karibaraiDenpyouId.getName();
			}
			if ("".equals(shiharaihouhouCd)) {
				karibaraidenpyouid = hontai.karibaraiDenpyouId + "(仮払金未使用)";
			} else {
				karibaraidenpyouid = hontai.karibaraiDenpyouId;
			}
		}
		//仮払金額
		if(! isEmpty(karibaraiOn)) {
			KaigaiRyohiKaribarai karibaraikingakuMap = kaigaiRyohiKaribaraiDao.find(hontai.karibaraiDenpyouId);
			if(karibaraiOn.equals("0") && ksKari.shinseiKingaku.getPdfHyoujiFlg()) {
				karibaraikingakuTitle = "伺い" + ksKari.shinseiKingaku.getName();
				karibaraikingaku = EteamXlsFmt.formatMoney(karibaraikingakuMap.kingaku);
				excel.makeBorder("karibaraikingaku_title");
				excel.makeBorder("karibaraikingaku");
			}
			if(karibaraiOn.equals("1") && ksKari.karibaraiKingaku.getPdfHyoujiFlg()) {
				karibaraikingakuTitle = ksKari.karibaraiKingaku.getName();
				karibaraikingaku = EteamXlsFmt.formatMoney(karibaraikingakuMap.karibaraiKingaku);
				excel.makeBorder("karibaraikingaku_title");
				excel.makeBorder("karibaraikingaku");
			}
		}
		//支払方法
		if(ks.shiharaiHouhou.getPdfHyoujiFlg()){
			shiharaihouhouTitle = "支払" + ks.shiharaiHouhou.getName();
			if (!("".equals(shiharaihouhouCd))) {
				shiharaihouhou = sysLogic.findNaibuCdName("shiharai_houhou", shiharaihouhouCd);
			}
		}
		//プロジェクト
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg() == true){
			projectTitle = ks.project.getName();
			project = ks.project.getCodeSeigyoValue(hontai.projectCd, hontai.projectName, " ");
			projectKaigai = ks.project.getCodeSeigyoValue(hontai.kaigaiProjectCd, hontai.kaigaiProjectName, " ");
		}
		//セグメント
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg() == true){
			segmentTitle = ks.segment.getName();
			segment = ks.segment.getCodeSeigyoValue(hontai.segmentCd, hontai.segmentNameRyakushiki, " ");
			segmentKaigai = ks.segment.getCodeSeigyoValue(hontai.kaigaiSegmentCd, hontai.kaigaiSegmentNameRyakushiki, " ");
		}
		//excelに各項目出力(「出張先・訪問先」行は改行要素があるため後述)
		excel.write("shiyousha_title", shiyoushaTitle);
		excel.write("shiyousha", shiyousha);
		excel.write("torihiki_title", torihikiRyohiTitle);
		excel.write("torihiki", torihiki);
		excel.write("mokuteki_title", mokutekiTitle);
		excel.write("mokuteki", mokuteki);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("kamoku", kamoku);
		excel.write("kazei_kbn_title", kazeiKbnTitle);
		excel.write("kazei_kbn", kazeiKbn);
		excel.write("zeiritsu_title", zeiritsuTitle);
		excel.write("zeiritsu", zeiritsu);
		excel.write("bunri_kbn_title", bunriKbnTitle);
		excel.write("bunri_kbn", bunriKbn);
		excel.write("shiire_kbn_title", shiireKbnTitle);
		excel.write("shiire_kbn", shiireKbn);
		excel.write("edaban_title", edabanTitle);
		excel.write("edaban", edaban);
		excel.write("futanbumon_title", futanbumonRyohiTitle);
		excel.write("futanbumon", futanbumon);
		excel.write("torihikisaki_title", torihikisakiRyohiTitle );
		excel.write("torihikisaki", torihikisaki );
		excel.write("tekiyou_title", tekiyouRyohiTitle);
		excel.write("tekiyou", tekiyou);
		excel.write("shiharaibi_title", shiharaibiTitle);
		excel.write("shiharaibi", shiharaibi);
		excel.write("kaigai_torihiki_title", torihikiRyohiTitle);
		excel.write("kaigai_torihiki", torihikiKaigai);
		excel.write("kaigai_kamoku_title", kamokuTitle);
		excel.write("kaigai_kamoku", kamokuKaigai);
		excel.write("kaigai_kazei_kbn_title", kazeiKbnTitle);
		excel.write("kaigai_kazei_kbn", kazeiKbnKaigai);
		excel.write("kaigai_edaban_title", edabanTitle);
		excel.write("kaigai_edaban", edabanKaigai);
		excel.write("kaigai_futanbumon_title", futanbumonRyohiTitle);
		excel.write("kaigai_futanbumon", futanbumonKaigai);
		excel.write("kaigai_torihikisaki_title", torihikisakiRyohiTitle );
		excel.write("kaigai_torihikisaki", torihikisakiKaigai );
		excel.write("kaigai_tekiyou_title", tekiyouRyohiTitle);
		excel.write("kaigai_tekiyou", tekiyouKaigai);

		excel.write("shiharaikiboubi_title", shiharaikiboubiTitle);
		excel.write("shiharaikiboubi", shiharaikiboubi);
		excel.write("sashihikishikyuukingaku_title", sashihikishikyuukingakuTitle);
		excel.write("sashihikishikyuukingaku", sashihikishikyuukingaku);
		excel.write("meisaikingakugoukei_title", meisaikingakugoukeiTitle);
		excel.write("meisaikingakugoukei", meisaikingakugoukei);
		excel.write("karibaraidenpyouid_title", karibaraidenpyouidTitle);
		excel.write("karibaraidenpyouid", karibaraidenpyouid);
		excel.write("karibaraikingaku_title", karibaraikingakuTitle);
		excel.write("karibaraikingaku", karibaraikingaku);
		excel.write("houjincardkingakugoukei_title", houjinkingakuTitle );
		excel.write("houjincardkingakugoukei", houjinkingaku );
		if(houjinCardRiyouFlg){
			excel.write("kaishatehaigoukei_title", kaishatehaigoukeiTitle);
			excel.write("kaishatehaigoukei", kaishatehaigoukei);
			excel.write("hikazeikingaku_title", hikazeikingakuTitle);
			excel.write("hikazeikingaku", hikazeikingaku);
			excel.write("kazeikingaku_title", kazeikingakuTitle);
			excel.write("kazeikingaku", kazeikingaku);
			// 不要行を非表示
			excel.hideRow(excel.getCell("kaishatehaigoukei2_title").getRow());
		}else{
			// 法人カード利用しない場合は、添字「2」のセルに値をセットする
			excel.write("kaishatehaigoukei2_title", kaishatehaigoukeiTitle);
			excel.write("kaishatehaigoukei2", kaishatehaigoukei);
			excel.write("hikazeikingaku2_title", hikazeikingakuTitle);
			excel.write("hikazeikingaku2", hikazeikingaku);
			excel.write("kazeikingaku2_title", kazeikingakuTitle);
			excel.write("kazeikingaku2", kazeikingaku);
			// 不要行を非表示
			excel.hideRow(excel.getCell("houjincardkingakugoukei_title").getRow());
		}
		excel.write("shiharaihouhou_title", shiharaihouhouTitle);
		excel.write("shiharaihouhou", shiharaihouhou);
		excel.write("project_title", projectTitle);
		excel.write("project", project);
		excel.write("segment_title", segmentTitle );
		excel.write("segment", segment );
		excel.write("kaigai_project_title", projectTitle);
		excel.write("kaigai_project", projectKaigai);
		excel.write("kaigai_segment_title", segmentTitle);
		excel.write("kaigai_segment", segmentKaigai);
		
		//■補足
		if(ks.hosoku.getPdfHyoujiFlg() == true){
			hosokutitle = ks.hosoku.getName();
			excel.write("hosoku_title", "■" + hosokutitle );
			//高さ調整：補足は１行４７文字以内で計算。一行ずつインサート。
			List<String> hosokuList = excel.splitLine(hosoku, 94);
			if (1 < hosokuList.size()) {
				excel.copyRow(hosokuRow, hosokuRow + 1, 1, hosokuList.size() - 1, true);
			}
			int rowCount = 0;
			for (String hosokuStr : hosokuList) {
				excel.write(hosokuCol, hosokuRow + rowCount++, hosokuStr);
			}
			//枠線をセットする。
			excel.encloseInBorder(hosokuRow, hosokuCol, hosokuList.size());
		}else{
			excel.hideRow(excel.getCell("hosoku_title").getRow());
		}
		
		//計算用
		BigDecimal shiharaikingaku8Sum = BigDecimal.ZERO;
		BigDecimal zeinukikingaku8Sum = BigDecimal.ZERO;
		BigDecimal shouhizeigaku8Sum = BigDecimal.ZERO; 
		BigDecimal shiharaikingaku10Sum = BigDecimal.ZERO;
		BigDecimal zeinukikingaku10Sum = BigDecimal.ZERO;
		BigDecimal shouhizeigaku10Sum = BigDecimal.ZERO;
		
		//支払金額（税額別）
		String shiharaikingaku10Title = "支払金額10%";
		String shiharaikingaku8Title = "支払金額*8%";
		String zeinukikingaku10Title = "税抜金額10%";
		String zeinukikingaku8Title = "税抜金額*8%";
		String shouhizeigaku10Title = "消費税額10%";
		String shouhizeigaku8Title = "消費税額*8%";
		
		if(isInvoice && ks.goukeiKingaku.getPdfHyoujiFlg()) {
			var jointMeisaiList = Stream.concat(meisaiList.stream(), meisaiKeihiList.stream()).collect(Collectors.toList());
			for (int i = 0; i < jointMeisaiList.size(); i++) {
				var meisai = jointMeisaiList.get(i);
				//海外明細、または国内分の課税区分が税込/税抜系以外の場合は交通費・日当宿泊費明細を○％の合計金額に加算しない
				if(meisai.get("kaigai_flg").equals("1") || 
						(meisai.get("zeiritsu") == null && !List.of("001","002","011","012","013","014").contains(hontai.kariKazeiKbn))) {
					continue;
				}
				String zeiritsucheck = EteamXlsFmt.formatMoney(meisai.getOrDefault("zeiritsu", hontai.zeiritsu));
				BigDecimal shiharaikingaku1 = (BigDecimal)meisai.getOrDefault("shiharai_kingaku", meisai.get("meisai_kingaku"));
				BigDecimal zeinukikingaku = (BigDecimal)meisai.getOrDefault("zeinuki_kingaku", meisai.get("hontai_kingaku"));
				BigDecimal shouhizeigaku1 = meisai.get("shouhizeigaku");
				BigDecimal zeinukigaku = zeinukikingaku == null ? BigDecimal.ZERO : zeinukikingaku;
				BigDecimal zeigaku = shouhizeigaku1 == null ? BigDecimal.ZERO : shouhizeigaku1;
			
				if(zeiritsucheck.equals("8")){
					shiharaikingaku8Sum = shiharaikingaku8Sum.add(shiharaikingaku1);
					zeinukikingaku8Sum = zeinukikingaku8Sum.add(zeinukigaku);
					shouhizeigaku8Sum = shouhizeigaku8Sum.add(zeigaku);
				}else if(zeiritsucheck.equals("10")) {
					shiharaikingaku10Sum = shiharaikingaku10Sum.add(shiharaikingaku1);
					zeinukikingaku10Sum = zeinukikingaku10Sum.add(zeinukigaku);
					shouhizeigaku10Sum = shouhizeigaku10Sum.add(zeigaku);
				}
			}
			String shiharaikingaku10 = EteamXlsFmt.formatMoney(shiharaikingaku10Sum);
			String zeinukikingaku10 = EteamXlsFmt.formatMoney(zeinukikingaku10Sum);
			String shouhizeigaku10 = EteamXlsFmt.formatMoney(shouhizeigaku10Sum);
			String shiharaikingaku8 = EteamXlsFmt.formatMoney(shiharaikingaku8Sum);
			String zeinukikingaku8 = EteamXlsFmt.formatMoney(zeinukikingaku8Sum);
			String shouhizeigaku8 = EteamXlsFmt.formatMoney(shouhizeigaku8Sum);
		
			excel.write("shiharai_kingaku_10_title", shiharaikingaku10Title);
			excel.write("shiharai_kingaku_10", shiharaikingaku10);
			excel.write("shiharai_kingaku_8_title", shiharaikingaku8Title);
			excel.write("shiharai_kingaku_8", shiharaikingaku8);
			excel.write("zeinuki_kingaku_10_title", zeinukikingaku10Title);
			excel.write("zeinuki_kingaku_10", zeinukikingaku10);
			excel.write("zeinuki_kingaku_8_title", zeinukikingaku8Title);
			excel.write("zeinuki_kingaku_8", zeinukikingaku8);
			excel.write("shouhizeigaku_10_title", shouhizeigaku10Title);
			excel.write("shouhizeigaku_10", shouhizeigaku10);
			excel.write("shouhizeigaku_8_title", shouhizeigaku8Title);
			excel.write("shouhizeigaku_8", shouhizeigaku8);
		} else {
			excel.hideRow(excel.getCell("shouhizeigaku_10_title").getRow());
			excel.hideRow(excel.getCell("shouhizeigaku_8_title").getRow());
		}
		
		// インボイス関連処理
		super.getAndPrintInvoiceZei("A011", denpyouId, excel);
		
		//■明細 (その他・経費)
		kaikeiXlsLogic.makeExcelShoukeiKeihi(meisaiKeihiList, excel, false, isInvoice);
		kaikeiXlsLogic.makeExcelMeisaiKeihiforKaigai(meisaiKeihiList, excel, hontai.map, meisaiRow, meisaiCol);
		
		//■明細（日当・宿泊費等）:国内
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, hontai.map, false, isInvoice);
		int countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow02, meisaiRow02 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, meisaiRow02, false);

		//■明細（交通費）:国内
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, hontai.map, false, isInvoice);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow01, meisaiRow01 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, meisaiRow01, false);
		
		//■明細（日当・宿泊費等）:海外
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, hontai.map, false, isInvoice);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow12, meisaiRow12 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, meisaiRow12, false);

		//■明細（交通費）:海外
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, hontai.map, false, isInvoice);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow11, meisaiRow11 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, meisaiRow11, false);

		//■仮払未使用なので明細行消す
		if ("".equals(shiharaihouhouCd)) {
			int meisaiStartRow = excel.getCell("meisai11").getRow();
			for (int i = 0; i < 30; i++) excel.getBook().getSheet(0).removeRow(meisaiStartRow);
		}
		
		//■申請内容「出張先・訪問先」行
		kaikeiXlsLogic.makeExcelShucchousakiLine(shucchousaki, shucchousakiCol, shucchousakiRow, shucchousakiTitle, excel, book);
		excel.write("seisankikan_title", seisankikanTitle);
		excel.write("seisankikan", seisankikan);
		
		// ---------------------------------------
		//共通部分
		// ---------------------------------------
		wfXlsLogic.makeExcel(excel, denpyouId, false);
		//仮払なしの場合、稟議金額・稟議金額残高を非表示にする
		if(karibaraiOn.equals("0")){
			excel.hideRow(excel.getCell("ringi_kingaku_title").getRow());
		}

		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
	
	/**
	 * 非課税／課税金額を計算する
	 * @param kazeiFlg      課税フラグ  1:課税 0:非課税
	 * @param meisaiList    明細リスト
	 * @param keihimeisaiList 経費明細リスト
	 * 以下2023/12/13追加(仮対応)
	 * ※課税フラグのDB登録値が課税区分と合っていなかったため、都度課税区分から課税フラグをセットするよう仮対応
	 * @param kokunaiKazeiKbn 国内課税区分(仮対応で追加)
	 * @param kaigaiKazeiKbn 海外課税区分(仮対応で追加)
	 * @return 非課税／課税金額
	 */
	protected BigDecimal sumKazeiKingaku(String kazeiFlg, List<GMap> meisaiList, List<GMap> keihimeisaiList,String kokunaiKazeiKbn,String kaigaiKazeiKbn){
		double goukei = 0;
		GMap meisai = null;
		// 国内分、海外分の課税区分から課税/非課税を判別
		String kokunaiKazeiFlg = List.of("001","002","011","012","013","014").contains(kokunaiKazeiKbn) ? "1": "0";
		String kaigaiKazeiFlg = List.of("001","002","011","012","013","014").contains(kaigaiKazeiKbn) ? "1": "0";
		
		// 交通費・宿泊費明細
		for (int i = 0; i < meisaiList.size(); i++) {
			meisai = meisaiList.get(i);
			if(kazeiFlg.equals(meisai.get("kaigai_flg").equals("0") ? kokunaiKazeiFlg: kaigaiKazeiFlg)) {
				goukei += Double.parseDouble(meisai.get("meisai_kingaku").toString());
			}
		}
		// 経費明細分を加算（2023/12/13 都度明細の課税区分を取得するよう修正）
		for (int i=0 ; i < keihimeisaiList.size() ; i++){
			meisai = keihimeisaiList.get(i);
			String meisaiKazeiFlg = List.of("001","002","011","012","013","014").contains(meisai.get("kari_kazei_kbn")) ? "1": "0";
			if(kazeiFlg.equals(meisaiKazeiFlg)){
				goukei += Double.parseDouble(meisai.get("shiharai_kingaku").toString());
			}
		}
		return BigDecimal.valueOf(goukei);
	}
	
	/**
	 * 明細金額から差引金額を減算し、再計算後の金額を明細リストに再設定する
	 * @param hontai 申請内容
	 * @param meisaiList 明細リスト
	 */
	protected void subtractSashihikiKingaku(GMap hontai, List<GMap> meisaiList){
		// 設定情報から差引項目名称を取得
		String sashihikiName = setting.sashihikiName();
		String sashihikiNameKaigai = setting.sashihikiNameKaigai();
		
		// 差引名称が未入力の場合は処理を終了する。
		if((sashihikiName == null || sashihikiName.isEmpty()) 
				&& (sashihikiNameKaigai == null || sashihikiNameKaigai.isEmpty())){
			return;
		}
		
		// 差引金額
		double sashihikiKingaku = 0;
		if(false==sashihikiName.isEmpty()){
			double sashihikiNum = (null != hontai.get("sashihiki_num"))?  Double.parseDouble(hontai.get("sashihiki_num").toString()) : 0;
			double sashihikiTanka = (null != hontai.get("sashihiki_tanka"))? Double.parseDouble(hontai.get("sashihiki_tanka").toString()) : 0;
			sashihikiKingaku = sashihikiNum * sashihikiTanka;
			
		}
		
		// 差引金額（海外）
		double sashihikiKingakuKaigai = 0;
		if(false==sashihikiNameKaigai.isEmpty()){
			double sashihikiNumKaigai = (null != hontai.get("sashihiki_num_kaigai"))?  Double.parseDouble(hontai.get("sashihiki_num_kaigai").toString()) : 0;
			double sashihikiTankaKaigai = (null != hontai.get("sashihiki_tanka_kaigai"))? Double.parseDouble(hontai.get("sashihiki_tanka_kaigai").toString()) : 0;
			sashihikiKingakuKaigai = sashihikiNumKaigai * sashihikiTankaKaigai;
			
		}
		
		KaikeiCommonLogic kaikeiComLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		String yakushokuCd = kaikeiComLogic.getYakushokuCd(hontai.get("user_id").toString());
		
		for(GMap meisai : meisaiList){
			if(RYOHISEISAN_SYUBETSU.SONOTA.equals(meisai.get("shubetsu_cd").toString())){
				
				String kaigaiFlg = meisai.get("kaigai_flg").toString();
				String shubetsu1 = meisai.get("shubetsu1").toString();
				String shubetsu2 = meisai.get("shubetsu2").toString();
				double meisaiKingaku = Double.parseDouble(meisai.get("meisai_kingaku").toString());
				boolean isNittou;
				double sashihikiKingakuTmp;
				
				if(SHUCCHOU_KOKUNAI.equals(kaigaiFlg)){
					isNittou = kaikeiComLogic.isNittou(shubetsu1, shubetsu2, yakushokuCd);
					sashihikiKingakuTmp = sashihikiKingaku;
				}else{
					isNittou = kaikeiComLogic.isNittouKaigai(shubetsu1, shubetsu2, yakushokuCd);
					sashihikiKingakuTmp = sashihikiKingakuKaigai;
				}
				
				if(isNittou){
					if(meisaiKingaku > sashihikiKingakuTmp){
						meisaiKingaku -=  sashihikiKingakuTmp;
						sashihikiKingakuTmp = 0;
						
					}else{
						sashihikiKingakuTmp -= meisaiKingaku;
						meisaiKingaku = 0;
					}
				}
				
				// 差引金額を差引いた後の値をセット
				meisai.put("meisai_kingaku", BigDecimal.valueOf(meisaiKingaku));
				// 差引金額（残高）を戻す
				if(SHUCCHOU_KOKUNAI.equals(kaigaiFlg)){
					sashihikiKingaku = sashihikiKingakuTmp;
				}else{
					sashihikiKingakuKaigai = sashihikiKingakuTmp;
				}
			}
		}
	}

}

