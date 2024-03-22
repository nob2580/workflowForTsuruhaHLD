package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.base.IEteamDTO;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.util.CollectionUtil;
import eteam.database.abstractdao.FurikaeAbstractDao;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.abstractdao.JidouhikiotoshiMeisaiAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiseisanAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiseisanKeihiMeisaiAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiseisanMeisaiAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.KeihiseisanMeisaiAbstractDao;
import eteam.database.abstractdao.KoutsuuhiseisanAbstractDao;
import eteam.database.abstractdao.KoutsuuhiseisanMeisaiAbstractDao;
import eteam.database.abstractdao.RyohiseisanAbstractDao;
import eteam.database.abstractdao.RyohiseisanKeihiMeisaiAbstractDao;
import eteam.database.abstractdao.RyohiseisanMeisaiAbstractDao;
import eteam.database.abstractdao.SeikyuushobaraiMeisaiAbstractDao;
import eteam.database.abstractdao.ShiharaiIraiAbstractDao;
import eteam.database.abstractdao.ShiharaiIraiMeisaiAbstractDao;
import eteam.database.abstractdao.TsukekaeAbstractDao;
import eteam.database.abstractdao.TsukekaeMeisaiAbstractDao;
import eteam.database.abstractdao.TsuukinteikiAbstractDao;
import eteam.database.dao.FurikaeDao;
import eteam.database.dao.JidouhikiotoshiMeisaiDao;
import eteam.database.dao.KaigaiRyohiseisanDao;
import eteam.database.dao.KaigaiRyohiseisanKeihiMeisaiDao;
import eteam.database.dao.KaigaiRyohiseisanMeisaiDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KeihiseisanMeisaiDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.KoutsuuhiseisanDao;
import eteam.database.dao.KoutsuuhiseisanMeisaiDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.RyohiseisanDao;
import eteam.database.dao.RyohiseisanKeihiMeisaiDao;
import eteam.database.dao.RyohiseisanMeisaiDao;
import eteam.database.dao.SeikyuushobaraiMeisaiDao;
import eteam.database.dao.ShiharaiIraiDao;
import eteam.database.dao.ShiharaiIraiMeisaiDao;
import eteam.database.dao.TsukekaeDao;
import eteam.database.dao.TsukekaeMeisaiDao;
import eteam.database.dao.TsuukinteikiDao;
import eteam.database.dto.InvoiceStart;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableWorkbook;

/**
 * 会計共通xlsロジック
 */
public class KaikeiCommonXlsLogic extends EteamAbstractLogic {
	
	/** 画面項目制御クラス(申請画面) */
	protected GamenKoumokuSeigyo ks;
	/** 画面項目制御クラス(経費明細) */
	protected GamenKoumokuSeigyo ks1;
	
	/**  出張区分：海外 */
	static final String SHUCCHOU_KAIGAI = "1";
	/**  出張区分：国内 */
	static final String SHUCCHOU_KOKUNAI = "0";
	/** インボイス伝票 */
	static boolean isBeforeInvoice = false;
	/** インボイス開始処理 */
	static boolean isStartInvoice = false;
	/**　消費税マスタ */
	KiShouhizeiSettingDao kiShouhizeiSetting;
	
	/**
	 * 初期化処理
	 * ※DI経由によりコンストラクタによる設定ができないので代替
	 * @param ksShinsei 画面項目制御クラス(申請画面)
	 * @param ksMeisai 画面項目制御クラス(経費明細)
	 */
	public void init(GamenKoumokuSeigyo ksShinsei, GamenKoumokuSeigyo ksMeisai)
	{
		this.ks  = ksShinsei;
		this.ks1 = ksMeisai;
	}
	
	/**
	 * 「インボイス対応前」出力ロジック
	 * @param invoiceDenpyou	0:インボイス対応　1:旧伝票(インボイス対応前)
	 * @param excel Excel
	 */
	public void beforeInvoice (String invoiceDenpyou, EteamXls excel)
	{
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		isStartInvoice = "1".equals(invoiceStartFlg);
		isBeforeInvoice = "1".equals(invoiceDenpyou);
		
		if (isStartInvoice && isBeforeInvoice) {
			excel.write("invoice_denpyou_flg", "インボイス対応前");
		}
	}	
	
	/**
	 * 出張旅費精算・旅費仮払・海外旅費精算・海外旅費仮払の「出張先・訪問先」項目の出力制御を行う
	 * @param shucchousaki			「出張先・訪問先」出力内容
	 * @param shucchousakiCol		「出張先・訪問先」セルの列番号
	 * @param shucchousakiRow		「出張先・訪問先」セルの行番号
	 * @param shucchousakiTitle		出張先・訪問先項目名
	 * @param excel					excel object
	 * @param book					出力ブック
	 */
	public void makeExcelShucchousakiLine(String shucchousaki, int shucchousakiCol, int shucchousakiRow, String shucchousakiTitle, EteamXls excel, WritableWorkbook book) {
		//高さ調整：出張先・訪問先は１行２２文字以内で計算。一行ずつインサート。
		List<String> shucchousakiList = excel.splitLine(shucchousaki, 44);
		if (1 < shucchousakiList.size()) {
			excel.copyRow(shucchousakiRow, shucchousakiRow + 1, 1, shucchousakiList.size() - 1, true);
		}
		int rowCount = 0;
		for (String shucchousakiStr : shucchousakiList) {
			//タイトルは1行目のみに出力する。
			excel.write(shucchousakiCol, shucchousakiRow + rowCount++, shucchousakiStr);
			if(rowCount == 1) {
				excel.write("shucchousaki_title",	shucchousakiTitle);
			}
		}
	}
	
	/**
	 * 経費明細の小計を作成する。
	 * @param meisaiKeihiList  明細リスト
	 * @param excel       excel object
	 * @param isKaribarai 仮払か
	 * @param isInvoice インボイス後伝票か
	 */
	public void makeExcelShoukeiKeihi(List<GMap> meisaiKeihiList, EteamXls excel, boolean isKaribarai, boolean isInvoice){
		//小計部分
		if(ks1.shiharaiKingaku.getPdfHyoujiFlg()){
			double shoukei=0;
			double shoukei10Percent = 0;
			double shoukei8Percent = 0;
			for (int i = 0; i < meisaiKeihiList.size(); i++) {
				GMap meisai = meisaiKeihiList.get(i);
				var zeiritsu = EteamXlsFmt.formatMoney(meisai.get("zeiritsu"));
				var meisaiKingaku = Double.parseDouble(meisai.get("shiharai_kingaku").toString());
				String shouhizei = meisai.get("shouhizeigaku") == null ? "0" : meisai.get("shouhizeigaku").toString();
				double shouhizeigaku = Double.parseDouble(shouhizei);
				shoukei += meisaiKingaku;
				shoukei10Percent += (!isKaribarai && zeiritsu.equals("10")) ? shouhizeigaku : 0;
				shoukei8Percent += (!isKaribarai && zeiritsu.equals("8")) ? shouhizeigaku : 0;
			}
			BigDecimal kingaku = BigDecimal.valueOf(0);
			kingaku = BigDecimal.valueOf(shoukei);
			excel.write("shoukei", 0, 0, EteamXlsFmt.formatMoney(kingaku));
			if(!isKaribarai && isStartInvoice && isInvoice)
			{
				excel.write("shoukei_keihi_10_title", 0, 0, "うち消費税10%");
				excel.makeBorder("shoukei_keihi_10_title");
				excel.write("shoukei_10_percent",	0, 0, EteamXlsFmt.formatMoney(BigDecimal.valueOf(shoukei10Percent)));
				excel.makeBorder("shoukei_10_percent");
				excel.write("shoukei_keihi_8_title", 0, 0, "うち消費税*8%");
				excel.makeBorder("shoukei_keihi_8_title");
				excel.write("shoukei_8_percent", 0, 0, EteamXlsFmt.formatMoney(BigDecimal.valueOf(shoukei8Percent)));
				excel.makeBorder("shoukei_8_percent");
			}
		}else{
			excel.hideRow(excel.getCell("shoukei").getRow());
		}
	}
	
	/**
	 * 経費明細部分を作成する。
	 * @param meisaiKeihiList	経費明細リスト
	 * @param excel       excel object
	 * @param hontai      申請データ
	 * @param meisaiRow   明細行
	 * @param meisaiCol   明細列
	 */
	public void makeExcelMeisaiKeihi(
			List<GMap> meisaiKeihiList, EteamXls excel, 
			GMap hontai, int meisaiRow, int meisaiCol) {
		
		initParts(connection);
		BatchKaikeiCommonLogic 		batchkaikeilogic	= EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		SystemKanriCategoryLogic 	sysLogic			= EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowXlsLogic 			wfXlsLogic			= EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		KaikeiCommonLogic 			kclogic				= EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		
		
		boolean	houjinCardRiyouFlg 	= sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean	kaishatehaiRiyouFlg	= sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSetting.findByDate(null).shiireZeigakuAnbunFlg;
		
		String shiyoushaAndShiyoubiTitle = "";
		String torihikiTitle = "";
		String kamokuTitle = "";
		String tekiyouTitle = "";
		String futanbumonTitle = "";
		String kazeiTitle = "";
		String shiharaikingakuTitle = "";
		String bunriKbnTitle = "";
		
		var isKaribarai = hontai.get("denpyou_id").toString().contains("A005") || hontai.get("denpyou_id").toString().contains("A012");
		
		//明細部分
		GMap meisai;
		//使用者＋使用日
		shiyoushaAndShiyoubiTitle = ks1.shiyoubi.getPdfHyoujiFlg() ? ks1.shiyoubi.getName() : "";
		//取引
		if(ks1.torihiki.getPdfHyoujiFlg()) {
			torihikiTitle = ks1.torihiki.getName();
		}
		//科目
		if(ks1.kamoku.getPdfHyoujiFlg()) {
			kamokuTitle = ks1.kamoku.getName();
		}
		if(ks1.kamokuEdaban.getPdfHyoujiFlg()) {
			kamokuTitle += (0==kamokuTitle.length())? ks1.kamokuEdaban.getName() : "\n" + ks1.kamokuEdaban.getName();
		}
		//摘要(＋支払先＋事業者区分)
		List<String> tekiyouTitleList = new ArrayList<>();
		if(ks1.tekiyou.getPdfHyoujiFlg()) {
			tekiyouTitleList.addAll(excel.splitLine(ks1.tekiyou.getName(), 14));
		}
		// 支払先
		if (ks1.shiharaisaki.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice && !isKaribarai) {
			tekiyouTitleList.addAll(excel.splitLine(ks1.shiharaisaki.getName(), 14));
		}
		// 事業者区分
		if (ks1.jigyoushaKbn.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice && !isKaribarai) {
			tekiyouTitleList.addAll(excel.splitLine(ks1.jigyoushaKbn.getName(), 14));
		}
		tekiyouTitle = String.join("\n", tekiyouTitleList);
		//課税区分
		if(ks1.kazeiKbn.getPdfHyoujiFlg()) {
			kazeiTitle = ks1.kazeiKbn.getName();
		}
		//負担部門(＋取引先＋振込先＋プロジェクト+セグメント)
		List<String> futanbumonTitleList = new ArrayList<>();
		if(ks1.futanBumon.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.futanBumon.getName(), 14));
		}
		if(ks1.torihikisaki.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.torihikisaki.getName(), 14));
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks1.project.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.project.getName(), 14));
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks1.segment.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.segment.getName(), 14));
		}
		futanbumonTitle = String.join("\n", futanbumonTitleList);
		//高さ調整：１行７文字以内で計算。
		String[] kamokuArr = kamokuTitle.split("\n");
		int kamokuLength = 0;
		for (int i = 0; i < kamokuArr.length; i++) {
			kamokuLength += excel.lineCount(kamokuArr[i], 14, 1);
		}
		String[] tekiyouArr = tekiyouTitle.split("\n");
		int tekiyouLength = 0;
		for (int i = 0; i < tekiyouArr.length; i++) {
			tekiyouLength += excel.lineCount(tekiyouArr[i], 14, 1);
		}
		String[] futanbumonArr = futanbumonTitle.split("\n");
		int futanbumonLength = 0;
		for (int i = 0; i < futanbumonArr.length; i++) {
			futanbumonLength += excel.lineCount(futanbumonArr[i], 14, 1);
		}
		int shiyouNum			= 	excel.lineCount(shiyoushaAndShiyoubiTitle, 12, 2);
		int kazeiLineNum		=	excel.lineCount(kazeiTitle, 4, 2);
		//支払金額(+うち消費税)
		List<String> kingakuTitleList = new ArrayList<>();
		if(ks1.shiharaiKingaku.getPdfHyoujiFlg()) {
			kingakuTitleList.addAll(excel.splitLine(ks1.shiharaiKingaku.getName(), 30));	
		}
		if(ks1.shouhizeigaku.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice
				&& !hontai.get("denpyou_id").toString().contains("A005") && !hontai.get("denpyou_id").toString().contains("A012")) {
			kingakuTitleList.addAll(excel.splitLine(ks1.shouhizeigaku.getName(), 30));			
		}
		shiharaikingakuTitle = String.join("\n", kingakuTitleList);
		String[] shiharaikingakuArr = shiharaikingakuTitle.split("\n");
		int shiharaikingakuLength = 0;
		for (int i = 0; i < shiharaikingakuArr.length; i++) {
			shiharaikingakuLength += excel.lineCount(shiharaikingakuArr[i], 14, 1);
		}
		
		
		if (kamokuLength >= 2 || futanbumonLength >= 2 ||  kazeiLineNum >= 2 || shiyouNum >= 2 || tekiyouLength >= 2 || shiharaikingakuLength >= 2) {
			excel.setHeight(meisaiRow - 2, wfXlsLogic.findMaxInt(kamokuLength, futanbumonLength, kazeiLineNum, shiyouNum, shiharaikingakuLength), 1);
		}
		
		excel.write("shiyoubi_title", 			shiyoushaAndShiyoubiTitle);
		excel.write("meisai_torihiki_title", 	torihikiTitle);
		excel.write("meisai_kamoku_title", 			kamokuTitle);
		excel.write("meisai_tekiyou_title", 	tekiyouTitle);
		excel.write("meisai_futan_bumon_title", futanbumonTitle);
		excel.write("kazei_title",				kazeiTitle);
		excel.write("shiharaikingaku_title", 	shiharaikingakuTitle);
		// 以下は分離区分・仕入区分が存在しないため処理を飛ばす
		// A005 出張伺い申請（仮払申請）
		// A012 経費伺い申請（仮払申請）
		if(!hontai.get("denpyou_id").toString().contains("A005") && !hontai.get("denpyou_id").toString().contains("A012"))
		{
			//分離区分＋仕入区分
			List<String> bunriKbnTitleList = new ArrayList<>();
			if(ks1.bunriKbn.getPdfHyoujiFlg()) {
				bunriKbnTitleList.addAll(excel.splitLine(ks1.bunriKbn.getName(), 14));
			}
			if(ks1.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
				bunriKbnTitleList.addAll(excel.splitLine(ks1.shiireKbn.getName(), 14));
			}
			bunriKbnTitle = String.join("\n", bunriKbnTitleList);
			excel.write("bunri_title", 	bunriKbnTitle);
		}
		
		if(ks1.zeiritsu.getPdfHyoujiFlg() == false) {
			excel.hideRow(excel.getCell("zeiritsu_title").getRow());
		}
		if(ks1.kousaihiShousai.getPdfHyoujiFlg()) {
			excel.write("kousaihi_title", ks1.kousaihiShousai.getName());
		} else {
			excel.hideRow(excel.getCell("kousaihi_title").getRow());
		}
		
		//明細テンプレート行を明細件数分コピー
		if (1 < meisaiKeihiList.size()) excel.copyRow(meisaiRow, meisaiRow + 2, 2, meisaiKeihiList.size() - 1, true);
		
		//先に警告リストだけ取得
		List<GMap> keikokuList = new ArrayList<GMap>();
		if(meisaiKeihiList.size() > 0) {
			String[] shiwakeEdanoCd		 	= CollectionUtil.toArrayStr(meisaiKeihiList, "shiwake_edano");
			String[] kousaihiHitoriKingaku	= CollectionUtil.toArrayStr(meisaiKeihiList, "kousaihi_hitori_kingaku");
			String[] kaigaiFlg				 	= CollectionUtil.makeArrayStr("0", meisaiKeihiList.size());
			keikokuList = kclogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, shiwakeEdanoCd, kousaihiHitoriKingaku, kaigaiFlg, kclogic.KOUSAI_ERROR_CD_KEIKOKU);
		}
		
		//1行ずつ書き込み
		int row = 0;
		for (int i = 0; i < meisaiKeihiList.size(); i++) {
			meisai = meisaiKeihiList.get(i);
			
			//注記
			String shiwakeTekiyouKeihiLength	= batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, hontai, meisai, "0");
//			String chuukiKeihi 				= (60 < EteamCommon.getByteLength(shiwakeTekiyouKeihiLength)) ? "\r\n" + EteamConst.Chuuki.CHUUKI_EXCEL : "";
			String chuukiKeihi 				= (kclogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouKeihiLength)) ? batchkaikeilogic.getTekiyouChuuki() :"";
			
			String shiyoushaAndShiyoubi = "";
			String torihikiKeiri = "";
			String kamoku       = ks1.kamoku.getCodeSeigyoValue((String)meisai.get("kari_kamoku_cd"),(String)meisai.get("kari_kamoku_name"), "\n");
			String kamokuEdaban = ks1.kamokuEdaban.getCodeSeigyoValue((String)meisai.get("kari_kamoku_edaban_cd"),(String)meisai.get("kari_kamoku_edaban_name"), "\n");
			
			String tekiyouKeiri = "";
			String futanBumonName = "";
			String kingakuList = "";
			String bunriKbnName = "";
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", (String)meisai.get("kari_kazei_kbn"));
			GMap kazeiInfo = sysLogic.findNaibuCdSetting("kazei_kbn", (String)meisai.get("kari_kazei_kbn"));
			String kazeiKbnGroup = (null != kazeiInfo)? (String)kazeiInfo.get("option1") : "";
			boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;
			String zeiritsu = EteamXlsFmt.formatZeiritsu(meisai.get("zeiritsu"), meisai.get("keigen_zeiritsu_kbn"));
			//No
			excel.write("meisai_no",					0, row, Integer.toString(i+1));
			if(ks1.shiyoubi.getPdfHyoujiFlg()){
				shiyoushaAndShiyoubi = EteamXlsFmt.formatDate(meisai.get("shiyoubi"));
				excel.write("shiyoubi",						0, row, shiyoushaAndShiyoubi);
			}
			//取引
			if(ks1.torihiki.getPdfHyoujiFlg()){
				torihikiKeiri = (String)meisai.get("torihiki_name");
				torihikiKeiri = ks1.torihiki.getCodeSeigyoValue(meisai.get("shiwake_edano").toString(), (String)meisai.get("torihiki_name"), "\n");
				excel.write("meisai_torihiki",			0, row, torihikiKeiri);
			}
			//科目(+科目枝番)
			StringBuffer kamokuBuf = new StringBuffer();
			if(ks1.kamoku.getPdfHyoujiFlg()) {
				kamokuBuf.append(kamoku);
			}
			if(ks1.kamokuEdaban.getPdfHyoujiFlg()){
				if(0!=kamokuBuf.length()) kamokuBuf.append("\n");
				kamokuBuf.append(kamokuEdaban);
			}
			excel.write("meisai_kamoku",						0, row, kamokuBuf.toString());
			//摘要(＋支払先＋事業者区分)
			List<String> tekiyouList = new ArrayList<>();
			if(ks1.tekiyou.getPdfHyoujiFlg()){
				tekiyouList.add((String)meisai.get("tekiyou") + chuukiKeihi);
			}
			// 支払先
			if (ks1.shiharaisaki.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice && !isKaribarai) {
				tekiyouList.add((String)meisai.get("shiharaisaki_name"));
			}
			// 事業者区分
			if (ks1.jigyoushaKbn.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice && !isKaribarai) {
				tekiyouList.add(sysLogic.findNaibuCdName("jigyousha_kbn", (String)meisai.get("jigyousha_kbn")));
			}
			tekiyouKeiri = String.join("\n", tekiyouList);
			excel.write("meisai_tekiyou",			0, row, tekiyouKeiri);
			//負担部門(＋取引先＋プロジェクト+セグメント)W
			List<String> futanbumonNameList = new ArrayList<>();
			if(ks1.futanBumon.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.futanBumon.getCodeSeigyoValue((String)meisai.get("kari_futan_bumon_cd"), (String)meisai.get("kari_futan_bumon_name"),"\n"));
			}
			if(ks1.torihikisaki.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.torihikisaki.getCodeSeigyoValue((String)meisai.get("torihikisaki_cd"), (String)meisai.get("torihikisaki_name_ryakushiki"),"\n"));
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks1.project.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.project.getCodeSeigyoValue((String)meisai.get("project_cd"), (String)meisai.get("project_name"),"\n"));
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks1.segment.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.segment.getCodeSeigyoValue((String)meisai.get("segment_cd"), (String)meisai.get("segment_name_ryakushiki"),"\n"));
			}
			futanBumonName = String.join("\n", futanbumonNameList);
			excel.write("meisai_futan_bumon",			0, row, futanBumonName);
			//課税区分
			if(ks1.kazeiKbn.getPdfHyoujiFlg()){
				excel.write("kazei",					0, row, kazei == null ? "" : kazei);
			}
			//税率
			if(ks1.zeiritsu.getPdfHyoujiFlg() && zeiritsuHyoujiFlg && !kazei.isEmpty()){
				excel.write("meisai_zeiritsu", 0, row, zeiritsu);
			}
			// 以下は分離区分・仕入区分が存在しないため処理を飛ばす
			// A005 出張伺い申請（仮払申請）
			// A012 経費伺い申請（仮払申請）
			if(!meisai.get("denpyou_id").toString().contains("A005") && !meisai.get("denpyou_id").toString().contains("A012"))
			{
				List<String> bunriKbnList = new ArrayList<>();
				if(ks1.bunriKbn.getPdfHyoujiFlg()) {
					bunriKbnList.add(sysLogic.findNaibuCdName("bunri_kbn", meisai.get("bunri_kbn")));
				}
				if( ks1.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
					bunriKbnList.add(sysLogic.findNaibuCdName("shiire_kbn",meisai.get("kari_shiire_kbn")));
				}
				bunriKbnName = String.join("\n", bunriKbnList);
				excel.write("bunri", 0, row, bunriKbnName);
			}
			//支払金額
			List<String> shiharaiKingakuList = new ArrayList<>();
			if(ks1.shiharaiKingaku.getPdfHyoujiFlg()){
				shiharaiKingakuList.add(EteamXlsFmt.formatMoney(meisai.get("shiharai_kingaku")));
			}
			if(ks1.shouhizeigaku.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice
					&& !meisai.get("denpyou_id").toString().contains("A005") && !meisai.get("denpyou_id").toString().contains("A012")) {
				shiharaiKingakuList.add(EteamXlsFmt.formatMoney(meisai.get("shouhizeigaku")));				
			}
			kingakuList = String.join("\n", shiharaiKingakuList);
			excel.write("meisai_shiharai_kingaku", 0, row, kingakuList);

			//法人カード利用
			if(null != ks1.houjinCardRiyou){
				if(houjinCardRiyouFlg && ks1.houjinCardRiyou.getPdfHyoujiFlg()){
					excel.write("meisai_houjincardriyou",	0, row, "1".equals(meisai.get("houjin_card_riyou_flg")) ? "C" : "");
				}
			}
			//会社手配
			if(null != ks1.kaishaTehai)
			if(kaishatehaiRiyouFlg && ks1.kaishaTehai.getPdfHyoujiFlg()){
				if("1".equals(meisai.get("kaisha_tehai_flg"))) excel.write("meisai_houjincardriyou",	0, row, "K");
			}
			
			//高さ調整：明細の取引、科目、摘要、負担部門は1行7文字以内で計算。
			//デフォルト２行だけどそれ以上の行数になるなら高さ変える。
			int shiyouLineNum			= excel.lineCount(shiyoushaAndShiyoubi, 12, 2);
			int torihikiLineNum			= excel.lineCount(torihikiKeiri, 14, 2);
			int kamokuLineNum			= excel.lineCount(kamokuBuf.toString(), 14, 2);
			int futanbumonLineNum 		= excel.lineCount(futanBumonName, 14, 2);
			int tekiyouLineNum			= excel.lineCount(tekiyouKeiri, 14, 2);
			int kingakuLineNum			= excel.lineCount(kingakuList, 10, 2);
			int kakuchouLineNum			= wfXlsLogic.findMaxInt(shiyouLineNum, torihikiLineNum, kamokuLineNum, futanbumonLineNum, tekiyouLineNum, kingakuLineNum);
			excel.setHeight(meisaiRow+row,  kakuchouLineNum, 2);

			//交際費詳細
			if(ks1.kousaihiShousai.getPdfHyoujiFlg() && "1".equals(meisai.get("kousaihi_shousai_hyouji_flg").toString())){
				String kousaihi = meisai.get("kousaihi_shousai").toString();
				if( meisai.get("kousaihi_ninzuu") != null) {
					String kousaihiNinzuu = meisai.get("kousaihi_ninzuu") == null ? "" : ((Integer)meisai.get("kousaihi_ninzuu")).toString() + "名";
					excel.write("kousaihi_ninzuu",					0, row, kousaihiNinzuu);
				}
				if( meisai.get("kousaihi_hitori_kingaku") != null) {
					String kousaihiHitoriKingaku = meisai.get("kousaihi_hitori_kingaku") == null ? "" : (EteamXlsFmt.formatMoney(meisai.get("kousaihi_hitori_kingaku"))) + "円";
					excel.write("kousaihi_hitori_kingaku",					0, row, kousaihiHitoriKingaku);
					
					//超過・不足分警告メッセージを追加
					for(GMap res : keikokuList) {
						if(res.get("index") != null && res.get("index").equals(i)) {
							kousaihi = kousaihi.isEmpty() ? res.get("errMsg") : kousaihi + "\r\n" + res.get("errMsg");
							break;
						}
					}
				}
				excel.write("kousaihi",					0, row, kousaihi);
				
				int kousaihiLineNum			= excel.lineCount(kousaihi, 58, 2);
				excel.setHeight(meisaiRow+row+1,  kousaihiLineNum, 2);
			} else {
				excel.hideRow(meisaiRow+row+1);
				//行番号の枠線を再セット
				excel.encloseInBorder(meisaiRow+row, meisaiCol, 1);
			}
			
			row+=2;
		}

	}
	
	/**
	 * 海外旅費／海外旅費仮払の経費明細部分を作成する。
	 * @param meisaiKeihiList 経費明細リスト
	 * @param excel       excel object
	 * @param hontai      申請データ
	 * @param meisaiRow   明細行
	 * @param meisaiCol   明細列
	 */
	public void makeExcelMeisaiKeihiforKaigai(
		List<GMap> meisaiKeihiList, EteamXls excel, 
		GMap hontai, int meisaiRow, int meisaiCol) {
	
		BatchKaikeiCommonLogic batchkaikeilogic	= EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		SystemKanriCategoryLogic sysLogic	= EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic	= EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		KaikeiCommonLogic kclogic	= EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		this.kiShouhizeiSetting = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		
		boolean	isGaikaOn				= sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
		boolean	houjinCardRiyouFlg 		= sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean	kaishatehaiRiyouFlg		= sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSetting.findByDate(null).shiireZeigakuAnbunFlg;
		
		String shiyoushaAndShiyoubiTitle = "";
		String torihikiTitle = "";
		String kamokuTitle = "";
		String tekiyouTitle = "";
		String futanbumonTitle = "";
		String kazeiTitle = "";
		String shiharaikingakuTitle = "";
		String gaikaTitle = "";
		String bunriTitle = "";
	
		GMap meisai;
		//使用日
		shiyoushaAndShiyoubiTitle = ks1.shiyoubi.getPdfHyoujiFlg() ? ks1.shiyoubi.getName() : "";
		//取引
		if(ks1.torihiki.getPdfHyoujiFlg()) {
			torihikiTitle = ks1.torihiki.getName();
		}
		//科目
		if(ks1.kamoku.getPdfHyoujiFlg()) {
			kamokuTitle = ks1.kamoku.getName();
		}
		if(ks1.kamokuEdaban.getPdfHyoujiFlg()) {
			kamokuTitle += (0==kamokuTitle.length())? ks1.kamokuEdaban.getName() : "\n" + ks1.kamokuEdaban.getName();
		}
		//摘要(＋支払先＋事業者区分)
		List<String> tekiyouTitleList = new ArrayList<>();
		if(ks1.tekiyou.getPdfHyoujiFlg()) {
			tekiyouTitleList.addAll(excel.splitLine(ks1.tekiyou.getName(), 14));
		}
		// 支払先
		if (ks1.shiharaisaki.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice) {
			tekiyouTitleList.addAll(excel.splitLine(ks1.shiharaisaki.getName(), 14));
		}
		// 事業者区分
		if (ks1.jigyoushaKbn.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice) {
			tekiyouTitleList.addAll(excel.splitLine(ks1.jigyoushaKbn.getName(), 14));
		}
		tekiyouTitle = String.join("\n", tekiyouTitleList);
		//課税区分
		if(ks1.kazeiKbn.getPdfHyoujiFlg()) {
			kazeiTitle = ks1.kazeiKbn.getName();
		}
		//負担部門(＋取引先＋振込先＋プロジェクト+セグメント)
		List<String> futanbumonTitleList = new ArrayList<>();
		if(ks1.futanBumon.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.futanBumon.getName(), 14));
		}
		if(ks1.torihikisaki.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.torihikisaki.getName(), 14));
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks1.project.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.project.getName(), 14));
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks1.segment.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks1.segment.getName(), 14));
		}
		futanbumonTitle = String.join("\n", futanbumonTitleList);
		//高さ調整：１行７文字以内で計算。
		String[] kamokuArr = kamokuTitle.split("\n");
		int kamokuLength = 0;
		for (int i = 0; i < kamokuArr.length; i++) {
			kamokuLength += excel.lineCount(kamokuArr[i], 14, 1);
		}
		String[] tekiyouArr = tekiyouTitle.split("\n");
		int tekiyouLength = 0;
		for (int i = 0; i < tekiyouArr.length; i++) {
			tekiyouLength += excel.lineCount(tekiyouArr[i], 14, 1);
		}
		String[] futanbumonArr = futanbumonTitle.split("\n");
		int futanbumonLength = 0;
		for (int i = 0; i < futanbumonArr.length; i++) {
			futanbumonLength += excel.lineCount(futanbumonArr[i], 14, 1);
		}
		int shiyouNum			= 	excel.lineCount(shiyoushaAndShiyoubiTitle, 12, 2);
		int kazeiLineNum		=	excel.lineCount(kazeiTitle, 4, 2);
		
		//支払金額(+うち消費税)
		List<String> kingakuTitleList = new ArrayList<>();
		if(ks1.shiharaiKingaku.getPdfHyoujiFlg()) {
			kingakuTitleList.addAll(excel.splitLine(ks1.shiharaiKingaku.getName(), 30));	
		}
		if(ks1.shouhizeigaku.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice
				&& !hontai.get("denpyou_id").toString().contains("A005") && !hontai.get("denpyou_id").toString().contains("A012")) {
			kingakuTitleList.addAll(excel.splitLine(ks1.shouhizeigaku.getName(), 30));			
		}
		shiharaikingakuTitle = String.join("\n", kingakuTitleList);
		String[] shiharaikingakuArr = shiharaikingakuTitle.split("\n");
		int shiharaikingakuLength = 0;
		for (int i = 0; i < shiharaikingakuArr.length; i++) {
			shiharaikingakuLength += excel.lineCount(shiharaikingakuArr[i], 14, 1);
		}
		
		if (kamokuLength >= 2 || futanbumonLength >= 2 ||  kazeiLineNum >= 2 || shiyouNum >= 2 || tekiyouLength >= 2 || shiharaikingakuLength >= 2) {
			excel.setHeight(meisaiRow - 2, wfXlsLogic.findMaxInt(kamokuLength, futanbumonLength, kazeiLineNum, shiyouNum, shiharaikingakuLength), 1);
		}
		
		if(ks.shucchouKbn.getPdfHyoujiFlg() == false) {
			excel.write("meisai_shucchou_kbn_title",	"");
		}
		excel.write("shiyoubi_title", shiyoushaAndShiyoubiTitle);
		excel.write("meisai_torihiki_title", torihikiTitle);
		excel.write("meisai_kamoku_title", kamokuTitle);
		excel.write("meisai_tekiyou_title", tekiyouTitle);
		excel.write("meisai_futan_bumon_title", futanbumonTitle);
		excel.write("kazei_title",				kazeiTitle);
		excel.write("shiharaikingaku_title", shiharaikingakuTitle);
		// 以下は分離区分・仕入区分が存在しないため処理を飛ばす
		// A005 出張伺い申請（仮払申請）
		// A012 経費伺い申請（仮払申請）
		if(!hontai.get("denpyou_id").toString().contains("A005") && !hontai.get("denpyou_id").toString().contains("A012"))
		{
			List<String> bunriKbnTitleList = new ArrayList<>();
			if(ks1.bunriKbn.getPdfHyoujiFlg()) {
				bunriKbnTitleList.addAll(excel.splitLine(ks1.bunriKbn.getName(), 14));
			}
			if(ks1.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
				bunriKbnTitleList.addAll(excel.splitLine(ks1.shiireKbn.getName(), 14));
			}
			bunriTitle = String.join("\n", bunriKbnTitleList);
			excel.write("bunri_title", bunriTitle);
		}
		
		//うち消費税額
		if(ks1.zeiritsu.getPdfHyoujiFlg() && isBeforeInvoice && !isStartInvoice) {
			excel.write("zeiritsu_title",	"");
		}
		
		//交際費詳細
		if(ks1.kousaihiShousai.getPdfHyoujiFlg()) {
			excel.write("kousaihi_title", ks1.kousaihiShousai.getName());
		} else {
			excel.hideRow(excel.getCell("kousaihi_title").getRow());
		}
		
		//幣種＋外貨
		if(isGaikaOn) {
			if(ks.heishu.getPdfHyoujiFlg()) {
				gaikaTitle = ks.heishu.getName();
			}
			if(ks.gaika.getPdfHyoujiFlg()) 
				gaikaTitle += (0==gaikaTitle.length())? ks.gaika.getName() : "\n" + ks.gaika.getName();
			excel.write("meisai_gaika_title",  gaikaTitle);
			excel.setBorder("meisai_gaika_title", Border.LEFT, BorderLineStyle.THIN);
		} else {
			excel.write("meisai_gaika_title", "");
		}
		
		//明細テンプレート行を明細件数分コピー
		if (1 < meisaiKeihiList.size()) excel.copyRow(meisaiRow, meisaiRow + 2, 2, meisaiKeihiList.size() - 1, true);
		
		//先に警告リストだけ取得
		List<GMap> keikokuList = new ArrayList<GMap>();
		if(meisaiKeihiList.size() > 0) {
			String[] shiwakeEdanoCd		 	= CollectionUtil.toArrayStr(meisaiKeihiList, "shiwake_edano");
			String[] kousaihiHitoriKingaku	= CollectionUtil.toArrayStr(meisaiKeihiList, "kousaihi_hitori_kingaku");
			String[] kaigaiFlg				 	= CollectionUtil.toArrayStr(meisaiKeihiList, "kaigai_flg");
			keikokuList = kclogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, shiwakeEdanoCd, kousaihiHitoriKingaku, kaigaiFlg, kclogic.KOUSAI_ERROR_CD_KEIKOKU);
		}
		
		
		//1行ずつ書き込み
		int row = 0;
		for (int i = 0; i < meisaiKeihiList.size(); i++) {
			meisai = meisaiKeihiList.get(i);
			
			//注記
			String shiwakeTekiyouKeihiLength	= batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, hontai, meisai, "0");
//			String chuukiKeihi 				= (60 < EteamCommon.getByteLength(shiwakeTekiyouKeihiLength)) ? "\r\n" + EteamConst.Chuuki.CHUUKI_EXCEL : "";
			String chuukiKeihi 				= (kclogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouKeihiLength)) ? batchkaikeilogic.getTekiyouChuuki() :"";
			
			String shucchouKbn = SHUCCHOU_KAIGAI.equals(meisai.get("kaigai_flg"))? "海外" : "国内";
			String shiyoushaAndShiyoubi = "";
			String torihikiKeiri = "";
			String kamoku       = ks1.kamoku.getCodeSeigyoValue((String)meisai.get("kari_kamoku_cd"),(String)meisai.get("kari_kamoku_name"), "\n");
			String kamokuEdaban = ks1.kamokuEdaban.getCodeSeigyoValue((String)meisai.get("kari_kamoku_edaban_cd"),(String)meisai.get("kari_kamoku_edaban_name"), "\n");

			String tekiyouKeiri = "";
			String futanBumonName = "";
			String kingakuList = "";
			String bunriKbnName = "";
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", (String)meisai.get("kari_kazei_kbn"));
			String zeiritsu = EteamXlsFmt.formatZeiritsu(meisai.get("zeiritsu"), meisai.get("keigen_zeiritsu_kbn"));
			GMap kazeiInfo = sysLogic.findNaibuCdSetting("kazei_kbn", (String)meisai.get("kari_kazei_kbn"));
			String kazeiKbnGroup = (null != kazeiInfo)? (String)kazeiInfo.get("option1") : "";
			boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;
			String heishu = (String)meisai.get("heishu_cd");
			String gaika = EteamXlsFmt.formatMoneyDecimal(meisai.get("gaika"));
			
			//No
			excel.write("meisai_no",					0, row, Integer.toString(i+1));
			//出張区分
			if(ks.shucchouKbn.getPdfHyoujiFlg()){
				excel.write("meisai_shucchou",				0, row, shucchouKbn);
				
			}
			//使用日
			if(ks1.shiyoubi.getPdfHyoujiFlg()){
				shiyoushaAndShiyoubi = EteamXlsFmt.formatDate(meisai.get("shiyoubi"));
				excel.write("shiyoubi",						0, row, shiyoushaAndShiyoubi);
			}
			//取引
			if(ks1.torihiki.getPdfHyoujiFlg()){
				torihikiKeiri = ks1.torihiki.getCodeSeigyoValue(meisai.getString("shiwake_edano"), (String)meisai.get("torihiki_name"), "\n");
				excel.write("meisai_torihiki",			0, row, torihikiKeiri);
			}
			//科目(+科目枝番)
			StringBuffer kamokuBuf = new StringBuffer();
			if(ks1.kamoku.getPdfHyoujiFlg()) {
				kamokuBuf.append(kamoku);
			}
			if(ks1.kamokuEdaban.getPdfHyoujiFlg()){
				if(0!=kamokuBuf.length()) kamokuBuf.append("\n");
				kamokuBuf.append(kamokuEdaban);
			}
			excel.write("meisai_kamoku", 0, row, kamokuBuf.toString());
			//摘要(＋支払先＋事業者区分)
			List<String> tekiyouList = new ArrayList<>();
			if(ks1.tekiyou.getPdfHyoujiFlg()){
				tekiyouList.add((String)meisai.get("tekiyou") + chuukiKeihi);
			}
			// 支払先
			if (ks1.shiharaisaki.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice) {
				tekiyouList.add((String)meisai.get("shiharaisaki_name"));
			}
			// 事業者区分
			if (ks1.jigyoushaKbn.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice) {
				tekiyouList.add(sysLogic.findNaibuCdName("jigyousha_kbn", (String)meisai.get("jigyousha_kbn")));
			}
			tekiyouKeiri = String.join("\n", tekiyouList);
			excel.write("meisai_tekiyou",			0, row, tekiyouKeiri);
			//負担部門(＋取引先＋プロジェクト+セグメント)
			List<String> futanbumonNameList = new ArrayList<>();
			if(ks1.futanBumon.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.futanBumon.getCodeSeigyoValue((String)meisai.get("kari_futan_bumon_cd"),(String)meisai.get("kari_futan_bumon_name"), "\n"));
			}
			if(ks1.torihikisaki.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.torihikisaki.getCodeSeigyoValue((String)meisai.get("torihikisaki_cd"), (String)meisai.get("torihikisaki_name_ryakushiki"),"\n"));
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks1.project.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.project.getCodeSeigyoValue((String)meisai.get("project_cd"), (String)meisai.get("project_name"),"\n"));
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks1.segment.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks1.segment.getCodeSeigyoValue((String)meisai.get("segment_cd"), (String)meisai.get("segment_name_ryakushiki"),"\n"));
			}
			futanBumonName = String.join("\n", futanbumonNameList);
			excel.write("meisai_futan_bumon",			0, row, futanBumonName);
			//課税区分
			if(ks1.kazeiKbn.getPdfHyoujiFlg()){
				excel.write("kazei",					0, row, kazei == null ? "" : kazei);
			}
			//税率
			if(ks1.zeiritsu.getPdfHyoujiFlg() && SHUCCHOU_KOKUNAI.equals(meisai.get("kaigai_flg")) && zeiritsuHyoujiFlg && !kazei.isEmpty()) {
				excel.write("meisai_zeiritsu",					0, row, zeiritsu);
			}
			//幣種＋外貨
			StringBuffer gaikaBuf = new StringBuffer();
			if(isGaikaOn){
				if(ks.heishu.getPdfHyoujiFlg()) {
					gaikaBuf.append(heishu);
				}
				if(ks.gaika.getPdfHyoujiFlg()){
					if(0!=gaikaBuf.length()) gaikaBuf.append("\n");
					gaikaBuf.append(gaika);
				}
				excel.write("meisai_gaika", 0, row, gaikaBuf.toString());
				excel.setBorder("meisai_gaika", row, 0, Border.LEFT, BorderLineStyle.THIN);
			}
			// 以下は分離区分・仕入区分が存在しないため処理を飛ばす
			// A005 出張伺い申請（仮払申請）
			// A012 経費伺い申請（仮払申請）
			if(!meisai.get("denpyou_id").toString().contains("A005") && !meisai.get("denpyou_id").toString().contains("A012"))
			{
				//分離区分（＋仕入区分）
				List<String> bunriKbnList = new ArrayList<>();
				if(ks1.bunriKbn.getPdfHyoujiFlg()) {
					bunriKbnList.add(sysLogic.findNaibuCdName("bunri_kbn", (String)meisai.get("bunri_kbn")));
				}
				if( ks1.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
					bunriKbnList.add(sysLogic.findNaibuCdName("shiire_kbn", (String)meisai.get("kari_shiire_kbn")));
				}
				bunriKbnName = String.join("\n", bunriKbnList);
				excel.write("bunri", 0, row, bunriKbnName);
			}
			
			//支払金額
			List<String> shiharaiKingakuList = new ArrayList<>();
			if(ks1.shiharaiKingaku.getPdfHyoujiFlg()){
				shiharaiKingakuList.add(EteamXlsFmt.formatMoney(meisai.get("shiharai_kingaku")));
			}
			if(ks1.shouhizeigaku.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice
					&& !meisai.get("denpyou_id").toString().contains("A005") && !meisai.get("denpyou_id").toString().contains("A012")) {
				shiharaiKingakuList.add(EteamXlsFmt.formatMoney(meisai.get("shouhizeigaku")));				
			}
			kingakuList = String.join("\n", shiharaiKingakuList);
			excel.write("meisai_shiharai_kingaku", 0, row, kingakuList);

			//法人カード利用
			if(null != ks1.houjinCardRiyou){
				if(houjinCardRiyouFlg && ks1.houjinCardRiyou.getPdfHyoujiFlg()){
					excel.write("meisai_houjincardriyou",	0, row, "1".equals(meisai.get("houjin_card_riyou_flg")) ? "C" : "");
				}
			}
			//会社手配
			if(null != ks1.kaishaTehai){
				if(kaishatehaiRiyouFlg && ks1.kaishaTehai.getPdfHyoujiFlg()){
					if("1".equals(meisai.get("kaisha_tehai_flg"))) excel.write("meisai_houjincardriyou",	0, row, "K");
				}
			}
			
			//高さ調整：明細の取引、科目、摘要、負担部門は1行7文字以内で計算。
			//→幣種・外貨の追加にともない、セル幅を狭めたため、1行5文字以内に変更。
			//デフォルト２行だけどそれ以上の行数になるなら高さ変える。
			int shiyouLineNum			= excel.lineCount(shiyoushaAndShiyoubi, 12, 2);
			int torihikiLineNum			= excel.lineCount(torihikiKeiri, 10, 2);
			int kamokuLineNum			= excel.lineCount(kamokuBuf.toString(), 10, 2);
			int futanbumonLineNum 		= excel.lineCount(futanBumonName, 10, 2);
			int tekiyouLineNum			= excel.lineCount(tekiyouKeiri, 10, 2);
			int gaikaLineNum			= excel.lineCount(gaikaBuf.toString(), 10, 2);
			int kingakuLineNum			= excel.lineCount(kingakuList, 10, 2);
			int kakuchouLineNum			= wfXlsLogic.findMaxInt(shiyouLineNum, torihikiLineNum, kamokuLineNum, futanbumonLineNum, tekiyouLineNum, gaikaLineNum, kingakuLineNum);
			excel.setHeight(meisaiRow+row,  kakuchouLineNum, 2);
			
			//交際費詳細
			if(ks1.kousaihiShousai.getPdfHyoujiFlg() && "1".equals(meisai.get("kousaihi_shousai_hyouji_flg").toString())){
				String kousaihi = meisai.get("kousaihi_shousai").toString();
				if( meisai.get("kousaihi_ninzuu") != null) {
					String kousaihiNinzuu = meisai.get("kousaihi_ninzuu") == null ? "" : ((Integer)meisai.get("kousaihi_ninzuu")).toString() + "名";
					excel.write("kousaihi_ninzuu",					0, row, kousaihiNinzuu);
				}
				if( meisai.get("kousaihi_hitori_kingaku") != null) {
					String kousaihiHitoriKingaku = meisai.get("kousaihi_hitori_kingaku") == null ? "" : (EteamXlsFmt.formatMoney(meisai.get("kousaihi_hitori_kingaku"))) + "円";
					excel.write("kousaihi_hitori_kingaku",					0, row, kousaihiHitoriKingaku);
					
					//超過・不足分警告メッセージを追加
					for(GMap res : keikokuList) {
						if(res.get("index") != null && res.get("index").equals(i)) {
							kousaihi = kousaihi.isEmpty() ? res.get("errMsg") : kousaihi + "\r\n" + res.get("errMsg");
							break;
						}
					}
				}
				excel.write("kousaihi",					0, row, kousaihi);
				
				int kousaihiLineNum			= excel.lineCount(kousaihi, 49, 2);		//1行が文字数丁度の場合、excel側で改行を入れられてしまうため1文字少なくカウント
				excel.setHeight(meisaiRow+row+1,  kousaihiLineNum, 2);
			} else {
				excel.hideRow(meisaiRow+row+1);
				//行番号の枠線を再セット
				excel.encloseInBorder(meisaiRow+row, meisaiCol, 1);
			}
			
			row+=2;
		}
	}
	
	/**
	 * 明細リストに含まれる指定した種別の件数を取得する。
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @return 明細件数
	 */
	public int courntMeisaiShubetsuCd(String shubetsuCd, List<GMap> meisaiList) {
		int count=0;
		for (int i = 0; i < meisaiList.size(); i++) {
			GMap meisai = meisaiList.get(i);
			String shubetsuCdMeisai = (String)meisai.get("shubetsu_cd");
			if(shubetsuCd.equals(shubetsuCdMeisai)) count++;
		}
		return count;
	}
	
	/**
	 * 旅費明細国内分の小計を作成する。
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @param excel       excel object
	 * @param hontai      伝票情報マップ
	 * @param isKaribarai 仮払か
	 * @param isInvoice invoiceか？
	 * @param sashihikiZeigaku 差引税額
	 */
	public void makeExcelShoukei(
			String shubetsuCd, List<GMap> meisaiList, 
			EteamXls excel, GMap hontai, boolean isKaribarai, boolean isInvoice, BigDecimal sashihikiZeigaku) {
		this.makeExcelShoukei("0", shubetsuCd, meisaiList, excel, hontai, isKaribarai, isInvoice, sashihikiZeigaku);
	}
	
	/**
	 * 旅費明細国内分の小計を作成する。
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @param excel       excel object
	 * @param hontai      伝票情報マップ
	 * @param isKaribarai 仮払か
	 * @param isInvoice invoiceか？
	 */
	public void makeExcelShoukei(
			String shubetsuCd, List<GMap> meisaiList, 
			EteamXls excel, GMap hontai, boolean isKaribarai, boolean isInvoice) {
		this.makeExcelShoukei("0", shubetsuCd, meisaiList, excel, hontai, isKaribarai, isInvoice, BigDecimal.ZERO);
	}
	
	/**
	 * 国内旅費系の明細部分を作成する。
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @param excel       excel object
	 * @param meisaiRow   明細行
	 * @param isKaribarai 仮払か
	 */
	public void makeExcelKokunaiMeisai(
			String shubetsuCd, List<GMap> meisaiList, 
			EteamXls excel, int meisaiRow, boolean isKaribarai) {

		// 海外フラグ0の場合として共通化する
		this.makeExcelMeisai("0", shubetsuCd, meisaiList, excel, meisaiRow, isKaribarai);
	}

	/**
	 * 明細リストに含まれる指定した種別の件数を取得する。
	 * @param kaigaiFlg   出張区分 1:海外   0:国内
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @return 明細件数
	 */
	public int courntMeisaiShubetsuCd(String kaigaiFlg, String shubetsuCd, List<GMap> meisaiList) {
		int count=0;
		for (int i = 0; i < meisaiList.size(); i++) {
			GMap meisai = meisaiList.get(i);
			String kaigaiFlgMeisai = (String)meisai.get("kaigai_flg");
			String shubetsuCdMeisai = (String)meisai.get("shubetsu_cd");
			if(kaigaiFlg.equals(kaigaiFlgMeisai) && shubetsuCd.equals(shubetsuCdMeisai)) count++;
		}
		return count;
	}

	/**
	 * 旅費精算・仮払の明細部分を作成する。
	 * @param kaigaiFlg   出張区分 1:海外   0:国内
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @param excel       excel object
	 * @param meisaiRow   明細行
	 * @param isKaribarai 仮払か
	 */
	public void makeExcelMeisai(
			String kaigaiFlg, String shubetsuCd, List<GMap> meisaiList,
			EteamXls excel, int meisaiRow, boolean isKaribarai) {

		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic	= EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		NaibuCdSettingDao naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		boolean	isGaikaOn = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
		boolean	houjinCardRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean	kaishatehaiRiyouFlg	= sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		
		int rowCnt = 0;
		String shubetsu1Title = "";
		String shubetsu2Title = "";
		String koutsuushudanTitle = "";
		String kikanTitle = "";
		String naiyoubikouTitle = "";
		String oufukuTitle = "";
		String kingakuTitle = "";
		String gaikaTitle = "";
		
		//種別
		if(ks.shubetsu1.getPdfHyoujiFlg()){
			shubetsu1Title = ks.shubetsu1.getName();
		}
		// 種別２も常時表示する
		if(ks.shubetsu2.getPdfHyoujiFlg()){
			shubetsu2Title = ks.shubetsu2.getName();
		}
		//交通手段
		if(ks.koutsuuShudan.getPdfHyoujiFlg() == true){
			koutsuushudanTitle = ks.koutsuuShudan.getName();
		}
		//期間
		if(ks.kikan.getPdfHyoujiFlg() == true){
			kikanTitle = ks.kikan.getName();
		}
		//内容備考
		//高さ調整：国内明細→１行１５文字以内、海外明細→1行10文字以内 で計算。
		int titleMaxPerLine = kaigaiFlg.equals(SHUCCHOU_KAIGAI)? 20 : 30;
		List<String> naiyoubikouTitleList = new ArrayList<>();
		if (shubetsuCd.equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)) {
			if (ks.naiyouKoutsuuhi.getPdfHyoujiFlg()) {
				naiyoubikouTitleList.addAll(excel.splitLine(ks.naiyouKoutsuuhi.getName(), titleMaxPerLine));
			}
			if (ks.bikouKoutsuuhi.getPdfHyoujiFlg()) {
				naiyoubikouTitleList.addAll(excel.splitLine(ks.bikouKoutsuuhi.getName(), titleMaxPerLine));
			}
		} else {
			if (ks.naiyouNittou.getPdfHyoujiFlg()) {
				naiyoubikouTitleList.addAll(excel.splitLine(ks.naiyouNittou.getName(), titleMaxPerLine));
			}
			if (ks.bikouNittou.getPdfHyoujiFlg()) {
				naiyoubikouTitleList.addAll(excel.splitLine(ks.bikouNittou.getName(), titleMaxPerLine));
			}
		}
		// 支払先
		if (!isKaribarai && ks.shiharaisaki.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice) {
			naiyoubikouTitleList.addAll(excel.splitLine(ks.shiharaisaki.getName(), titleMaxPerLine));
		}
		// 事業者区分
		if (!isKaribarai && ks.jigyoushaKbn.getPdfHyoujiFlg() && !kaigaiFlg.equals(SHUCCHOU_KAIGAI) && !isBeforeInvoice && isStartInvoice) {
			naiyoubikouTitleList.addAll(excel.splitLine(ks.jigyoushaKbn.getName(), titleMaxPerLine));
		}
		naiyoubikouTitle = String.join("\n", naiyoubikouTitleList);
		
		//往復
		if(ks.oufukuFlg.getPdfHyoujiFlg() == true){
			oufukuTitle = ks.oufukuFlg.getName();
		}
		//金額
		if(ks.meisaiKingaku.getPdfHyoujiFlg()){
			kingakuTitle = ks.meisaiKingaku.getName();
		}
		//消費税額
		if(!isKaribarai && ks.shouhizeigaku.getPdfHyoujiFlg() && !kaigaiFlg.equals(SHUCCHOU_KAIGAI) && !isBeforeInvoice && isStartInvoice){
			kingakuTitle += ((kingakuTitle.isEmpty() ? "" : "\n") + ks.shouhizeigaku.getName());
		}
		
		//excelにタイトル出力
		// 種別1・2は、交通費なら一つに集約
		// 日当・宿泊費ならそれぞれをセルに入れる
		if(shubetsuCd.equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI))
		{
			shubetsu1Title += ((isEmpty(shubetsu1Title) || isEmpty(shubetsu2Title)) ? "" : "\n")
					+ (isEmpty(shubetsu2Title) ? "" : shubetsu2Title);
			int max = Math.max(Math.max(naiyoubikouTitle.split("\n").length, shubetsu1Title.split("\n").length), kingakuTitle.split("\n").length);
			excel.setHeight(meisaiRow - 1, max, 1);
			excel.write("meisai_shubetsu_title" + kaigaiFlg + shubetsuCd, shubetsu1Title);
		}
		else
		{
			excel.write("meisai_shubetsu_title" + kaigaiFlg + shubetsuCd + "1",	shubetsu1Title);
			excel.write("meisai_shubetsu_title" + kaigaiFlg + shubetsuCd + "2",	shubetsu2Title);
			excel.setHeight(meisaiRow - 1, Math.max(naiyoubikouTitle.split("\n").length, kingakuTitle.split("\n").length), 1);
		}
		excel.write("meisai_kikan_title" + kaigaiFlg + shubetsuCd,	 kikanTitle);
		excel.write("meisai_naiyou_title" + kaigaiFlg + shubetsuCd,	 naiyoubikouTitle);
		excel.write("meisai_kingaku_title" + kaigaiFlg + shubetsuCd, kingakuTitle);
		//交通費部のみ交通手段・往復フラグのタイトル出力
		if(RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCd)){
			excel.write("meisai_koutsuu_shudan_title" + kaigaiFlg + shubetsuCd,	koutsuushudanTitle);
			excel.write("meisai_oufuku_title" + kaigaiFlg + shubetsuCd,			oufukuTitle);
		}
		if(null != ks.ryoushuushoSeikyuushoTouFlg && ks.ryoushuushoSeikyuushoTouFlg.getPdfHyoujiFlg() == false){
			excel.write("meisai_shouhyou_title" + kaigaiFlg + shubetsuCd, "");
		}
		
		//幣種+外貨
		if(kaigaiFlg.equals(SHUCCHOU_KAIGAI)){
			if(isGaikaOn) {
				if(ks.heishu.getPdfHyoujiFlg()) {
					gaikaTitle = ks.heishu.getName();
				}
				if(ks.gaika.getPdfHyoujiFlg()) 
					gaikaTitle += (0==gaikaTitle.length())? ks.gaika.getName() : "\n" + ks.gaika.getName();
				excel.write("meisai_gaika_title" + kaigaiFlg + shubetsuCd, gaikaTitle);
				excel.setBorder("meisai_gaika_title" + kaigaiFlg + shubetsuCd, Border.LEFT, BorderLineStyle.THIN);
			} else {
				excel.write("meisai_gaika_title" + kaigaiFlg + shubetsuCd, "");
			}
		}
		
		for (int i = 0; i < meisaiList.size(); i++) {
			//1行ずつ書き込み
			GMap meisai = meisaiList.get(i);
			String kaigaiFlgMeisai  = (String)meisai.get("kaigai_flg");
			String shubetsuCdMeisai = (String)meisai.get("shubetsu_cd");
			String shouhyouHissuFlg = (String)meisai.get("shouhyou_shorui_hissu_flg");
			
			if(kaigaiFlgMeisai == null)
			{
				kaigaiFlgMeisai = SHUCCHOU_KOKUNAI; // null=国内旅費系
			}
			
			if(shubetsuCdMeisai == null)
			{
				shubetsuCdMeisai = RYOHISEISAN_SYUBETSU.KOUTSUUHI; // null=交通費精算
			}
			
			if(kaigaiFlgMeisai.equals(kaigaiFlg) && shubetsuCdMeisai.equals(shubetsuCd)) {
				
				excel.write("meisai_no" + kaigaiFlg + shubetsuCd,		0, rowCnt, Integer.toString(rowCnt+1));
				
				// 種別1
				String shubetsu1 = (String)meisai.get("shubetsu1");
				String shubetsu2 = (String)meisai.get("shubetsu2");
				StringBuffer shubetsu1Buf = new StringBuffer();
				if(ks.shubetsu1.getPdfHyoujiFlg()) shubetsu1Buf.append(shubetsu1);
				if(ks.shubetsu2.getPdfHyoujiFlg()){
					if (RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCd)) {
						if(0!=shubetsu1Buf.length()) shubetsu1Buf.append("\r\n");
						shubetsu1Buf.append(shubetsu2);
					}else{
						if("".equals(shubetsu2)) {
							// 日当等マスターの証憑必須フラグ="1"のものは種別の後ろに"*"をつける
							if("1".equals(shouhyouHissuFlg)) shubetsu1Buf.append("*");
						}
					}
				}
				excel.write("meisai_shubetsu" + kaigaiFlg + shubetsuCd,	0, rowCnt, shubetsu1Buf.toString());
				
				
				// 交通手段
				String koutsuushudan = "";
				// 種別2
				StringBuffer shubetsu2Buf = new StringBuffer();
				if(RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCd)) {
					if(ks.koutsuuShudan.getPdfHyoujiFlg()){
						koutsuushudan = (String)meisai.get("koutsuu_shudan");
						// 交通手段の証憑必須フラグ="1"のものは交通手段の後ろに"*"をつける
						if("1".equals(shouhyouHissuFlg)) koutsuushudan = koutsuushudan + "*";
						excel.write("meisai_koutsuu_shudan" + kaigaiFlg + shubetsuCd,		0, rowCnt, koutsuushudan);
					}
					/*早安楽*/
					if(ks.hayaYasuRaku.getPdfHyoujiFlg()) {
						Colour hayaColor = Colour.BLUE;
						Colour yasuColor = Colour.GREEN;
						Colour rakuColor = Colour.ORANGE;
						Colour moji = Colour.WHITE;
						// 国内交通費限定なので海外フラグや種別コードは意味がなく、付けていない
						if("1".equals(meisai.get("haya_flg"))) {
							excel.writeWithColorBackgroundColor("meisai_haya",0, rowCnt , "早",moji, hayaColor);
						}
						if("1".equals(meisai.get("yasu_flg"))) {
							excel.writeWithColorBackgroundColor("meisai_yasu",0, rowCnt , "安",moji, yasuColor);
						}
						if("1".equals(meisai.get("raku_flg"))) {
							excel.writeWithColorBackgroundColor("meisai_raku",0, rowCnt , "楽",moji, rakuColor);
						}
					}
				} else {
					// 日当宿泊費では、種別2について、交通手段と同じ位置なので枠を使いまわし。
					// TSR様時代からのもので、ちょっと気持ちは悪いが一旦このままで放置
					if(ks.shubetsu2.getPdfHyoujiFlg()){
						shubetsu2Buf.append(shubetsu2);
						if(!"".equals(shubetsu2)) {
							// 日当等マスターの証憑必須フラグ="1"のものは種別の後ろに"*"をつける
							if("1".equals(shouhyouHissuFlg)) {
								shubetsu2Buf.append("*");
							}
						}
						excel.write("meisai_koutsuu_shudan" + kaigaiFlg + shubetsuCd,		0, rowCnt, shubetsu2Buf.toString());
					}
				}
				
				// 領収書・請求書等
				if(null != ks.ryoushuushoSeikyuushoTouFlg && ks.ryoushuushoSeikyuushoTouFlg.getPdfHyoujiFlg()){
					excel.write("meisai_shouhyou" + kaigaiFlg + shubetsuCd, 0, rowCnt, "1".equals(meisai.get("ryoushuusho_seikyuusho_tou_flg")) ? "○" : "");
				}
				
				// 期間
				if(ks.kikan.getPdfHyoujiFlg()){
					excel.write("meisai_kikan" + kaigaiFlg + shubetsuCd, 0, rowCnt, EteamXlsFmt.formatDate(meisai.get("kikan_from")) + (null != meisai.get("kikan_to") ? "\r\n" + EteamXlsFmt.formatDate(meisai.get("kikan_to")) : ""));
				}
				
				// 内容/備考
				StringBuffer naiyouBuf = new StringBuffer();
				if (shubetsuCd.equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)) {
					if(ks.naiyouKoutsuuhi.getPdfHyoujiFlg()) {
						naiyouBuf.append((String)meisai.get("naiyou"));
					}
					if(ks.bikouKoutsuuhi.getPdfHyoujiFlg()){
						if(0!=naiyouBuf.length()) {
							naiyouBuf.append("\r\n");
						}
						naiyouBuf.append((String)meisai.get("bikou"));
					}
				} else {
					if(ks.naiyouNittou.getPdfHyoujiFlg()) {
						naiyouBuf.append((String)meisai.get("naiyou"));
					}
					if(ks.bikouNittou.getPdfHyoujiFlg()){
						if(0!=naiyouBuf.length()) {
							naiyouBuf.append("\r\n");
						}
						naiyouBuf.append((String)meisai.get("bikou"));
					}
				}
				// 支払先
				if(!isKaribarai && ks.shiharaisaki.getPdfHyoujiFlg() && !isBeforeInvoice && isStartInvoice){
					if(0!=naiyouBuf.length()) {
						naiyouBuf.append("\r\n");
					}
					naiyouBuf.append((String)meisai.get("shiharaisaki_name"));
				}
				// 事業者区分
				if(!isKaribarai && ks.jigyoushaKbn.getPdfHyoujiFlg() && !kaigaiFlg.equals(SHUCCHOU_KAIGAI) && !isBeforeInvoice && isStartInvoice){
					if(0!=naiyouBuf.length()) {
						naiyouBuf.append("\r\n");
					}
					naiyouBuf.append(naibuCdSettingDao.find("jigyousha_kbn", (String)meisai.get("jigyousha_kbn")).name);
				}
				
				int naiyouLineLength = kaigaiFlg.equals(SHUCCHOU_KAIGAI)
						? 26 // 海外は26固定
						: shubetsuCd.equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)
							? 30 // 国内は交通費なら30
							: 42; // 日当宿泊費なら42
				excel.write("meisai_naiyou" + kaigaiFlg + shubetsuCd, 0, rowCnt, String.join("\r\n", excel.splitLine(naiyouBuf.toString(), naiyouLineLength)));
				
				//幣種+外貨
				StringBuffer gaikaBuf = new StringBuffer();
				if(kaigaiFlg.equals(SHUCCHOU_KAIGAI)){
					String heishu = (String)meisai.get("heishu_cd");
					String gaika = EteamXlsFmt.formatMoneyDecimal(meisai.get("gaika"));
					if(isGaikaOn){
						if(ks.heishu.getPdfHyoujiFlg()) {
							gaikaBuf.append(heishu);
						}
						if(ks.gaika.getPdfHyoujiFlg()){
							if(0!=gaikaBuf.length()) {
								gaikaBuf.append("\r\n");
							}
							gaikaBuf.append(gaika);
						}
						excel.write("meisai_gaika" + kaigaiFlg + shubetsuCd, 0, rowCnt, gaikaBuf.toString());
						excel.setBorder("meisai_gaika" + kaigaiFlg + shubetsuCd, rowCnt, 0, Border.LEFT, BorderLineStyle.THIN);
					}
				}
				
				// 往復フラグ
				if(RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCd)) {
					if(ks.oufukuFlg.getPdfHyoujiFlg() == true){
						excel.write("meisai_oufuku" + kaigaiFlg + shubetsuCd,	0, rowCnt, "1".equals(meisai.get("oufuku_flg")) ? "○" : "");
					}
				}
				
				//明細金額
				StringBuffer meisaiKingakuBuf = new StringBuffer();
				if(ks.meisaiKingaku.getPdfHyoujiFlg()){
					meisaiKingakuBuf.append(EteamXlsFmt.formatMoney(meisai.get("meisai_kingaku")));
					if (RYOHISEISAN_SYUBETSU.SONOTA.equals(shubetsuCd)) {
						if(ks.tanka.getPdfHyoujiFlg() || ks.nissuu.getPdfHyoujiFlg()){
							meisaiKingakuBuf.append(this.getSonotaTankaNissuu(meisai));
						}
					}else if(RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCd) && kaigaiFlg.equals("0")) {
						// if(ks.tanka.getPdfHyoujiFlg()) {
							//交通手段の数量入力タイプを取得する。 
							String suuryouNyuryokuType = meisai.get("suuryou_nyuryoku_type");
							//数量入力タイプが「0」の時は、明細金額のみを表示する。
							//数量入力タイプが「1」又は「2」の時は明細金額の下に「単価×数量」も表示する。
							// 22082403→一旦単価表示の設定無視で固定
							if(!suuryouNyuryokuType.equals("0")) {
								meisaiKingakuBuf.append(String.format(
										"\r\n%s円×%s"+meisai.get("suuryou_kigou"), 
										EteamXlsFmt.formatMoneyDecimalWithPadding3Digit(meisai.get("tanka")), 
										EteamXlsFmt.formatSuuryouDecimal(meisai.get("suuryou")))
										);
							}
						// }
					}
				}
				//消費税額
				if(!isKaribarai && ks.shouhizeigaku.getPdfHyoujiFlg() && !kaigaiFlg.equals(SHUCCHOU_KAIGAI) && !isBeforeInvoice && isStartInvoice){
					if(0!=meisaiKingakuBuf.length()) {
						meisaiKingakuBuf.append("\r\n");
					}
					meisaiKingakuBuf.append(EteamXlsFmt.formatMoney(meisai.get("shouhizeigaku")));
				}
				excel.write("meisai_kingaku" + kaigaiFlg + shubetsuCd,	0, rowCnt, meisaiKingakuBuf.toString());

				//法人カード利用
				if(null != ks.houjinCardRiyou){
					if (houjinCardRiyouFlg && ks.houjinCardRiyou.getPdfHyoujiFlg()) {
						excel.write("meisai_houjincardriyou" + kaigaiFlg + shubetsuCd,	0, rowCnt, "1".equals(meisai.get("houjin_card_riyou_flg")) ? "C" : "");
					}
				}
				//会社手配
				if(null != ks.kaishaTehai){
					if(kaishatehaiRiyouFlg && ks.kaishaTehai.getPdfHyoujiFlg()){
						if("1".equals(meisai.get("kaisha_tehai_flg"))) {
							excel.write("meisai_houjincardriyou" + kaigaiFlg + shubetsuCd,	0, rowCnt, "K");
						}
					}
				}

				//高さ調整：各項目毎に必要な行数計算、デフォルト２行だけどそれ以上の行数になるなら高さ変える。
				int shubetsu1LineNum = excel.lineCount(shubetsu1Buf.toString(), 14, 2);
				int shubetsu2LineNum = excel.lineCount(shubetsu2Buf.toString(), 14, 2);
				int koutsuushudanLineNum = excel.lineCount(koutsuushudan, 10, 2);
				int naiyouLineNum =  excel.lineCount(naiyouBuf.toString(), naiyouLineLength, 2);
				int gaikaLineNum = excel.lineCount(gaikaBuf.toString(), 10, 2);
				int meisaiKingakuLineNum = excel.lineCount(meisaiKingakuBuf.toString(), 10, 2);
				int kakuchouLineNum	= wfXlsLogic.findMaxInt(shubetsu1LineNum, shubetsu2LineNum, koutsuushudanLineNum, naiyouLineNum, gaikaLineNum, meisaiKingakuLineNum);
				excel.setHeight(meisaiRow + rowCnt,  kakuchouLineNum, 2);
				
				rowCnt++;
			}
		}
	}
	
	/**
	 * 海外旅費／海外旅費仮払の小計を作成する。
	 * @param kaigaiFlg   出張区分   1:海外 2:国内
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @param excel       excel object
	 * @param hontai      伝票情報マップ
	 * @param isKaribarai 仮払か
	 * @param isInvoice invoiceか？
	 */
	public void makeExcelShoukei(
			String kaigaiFlg, String shubetsuCd, List<GMap> meisaiList,
			EteamXls excel, GMap hontai, boolean isKaribarai, boolean isInvoice) {
		this.makeExcelShoukei(kaigaiFlg, shubetsuCd, meisaiList, excel, hontai, isKaribarai, isInvoice, BigDecimal.ZERO);
	}
	
	/**
	 * 海外旅費／海外旅費仮払の小計を作成する。
	 * @param kaigaiFlg   出張区分   1:海外 2:国内
	 * @param shubetsuCd  種別コード 1:交通費 2:日当・宿泊費等
	 * @param meisaiList  明細リスト
	 * @param excel       excel object
	 * @param hontai      伝票情報マップ
	 * @param isKaribarai 仮払か
	 * @param isInvoice invoiceか？
	 * @param sashihikiZeigaku 差引税額
	 */
	public void makeExcelShoukei(
			String kaigaiFlg, String shubetsuCd, List<GMap> meisaiList,
			EteamXls excel, GMap hontai, boolean isKaribarai, boolean isInvoice, BigDecimal sashihikiZeigaku) {
		
		// 明細金額の合計値を求める
		double shoukei=0;
		double shoukei10Percent = 0;
		double shoukei8Percent = 0;
		var zeiritsu = EteamXlsFmt.formatMoney(hontai.get("zeiritsu"));
		boolean isKaigai = "1".equals(kaigaiFlg);
		for (int i = 0; i < meisaiList.size(); i++) {
			GMap meisai = meisaiList.get(i);
			String kaigaiFlgMeisai = meisai.get("kaigai_flg") == null ? "0" : meisai.get("kaigai_flg");
			String shubetsuCdMeisai = (String)meisai.get("shubetsu_cd");
			if(kaigaiFlg.equals(kaigaiFlgMeisai) && shubetsuCd.equals(shubetsuCdMeisai)) {
				double meisaiKingaku = Double.parseDouble(meisai.get("meisai_kingaku").toString());
				double shouhizeigaku = 0;
				if(!isKaribarai) {
					shouhizeigaku = Double.parseDouble(meisai.get("shouhizeigaku").toString());
				}
				shoukei += meisaiKingaku;
				shoukei10Percent += (!isKaribarai && !isKaigai && zeiritsu.equals("10")) ? shouhizeigaku : 0;
				shoukei8Percent += (!isKaribarai && !isKaigai && zeiritsu.equals("8")) ? shouhizeigaku : 0;
			}
		}
		
		// 種別が日当・宿泊費等の場合
		if( shubetsuCd.equals(RYOHISEISAN_SYUBETSU.SONOTA) ){
			
			// 設定情報から差引項目名称を取得
			String sashihikiName = EteamSettingInfo.getSettingInfo(isKaigai? EteamSettingInfo.Key.SASHIHIKI_NAME_KAIGAI : EteamSettingInfo.Key.SASHIHIKI_NAME);
			
			// 差引項目名称がブランクでない場合
			if( !isEmpty(sashihikiName)){
				
				//差引幣種コード、差引レート、差引単価（外貨）、差引単価（邦貨）
				if(isKaigai) makeExcelSashihikiGaika(excel, hontai);
				
				// 明細の小計から差引金額を減算
				String sashihikiNum = "";
				double sashihikiTanka = 0;
				
				var sashihikiSuffix = isKaigai ? "_kaigai" : "";
				if(hontai.get("sashihiki_num" + sashihikiSuffix) != null) {
					sashihikiNum = hontai.get("sashihiki_num" + sashihikiSuffix).toString();
				}
				if(hontai.get("sashihiki_tanka" + sashihikiSuffix) != null) {
					sashihikiTanka = Double.parseDouble(hontai.get("sashihiki_tanka" + sashihikiSuffix).toString());
				}
				
				double sashihikiTotal = sashihikiTanka * Double.parseDouble(sashihikiNum.isEmpty()? "0" : sashihikiNum);
				shoukei -= sashihikiTotal;
				if(((String)hontai.get("denpyou_id")).contains("A004")) {
					shoukei10Percent -= zeiritsu.equals("10") ? sashihikiZeigaku.doubleValue() : 0;
					shoukei8Percent -= zeiritsu.equals("8") ? sashihikiZeigaku.doubleValue() : 0;
				}
				
				//差引回数、差引金額
				int i = 0;
				if(ks.sashihikiNum.getPdfHyoujiFlg()){
					i++;
					excel.write("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i + "_title",			sashihikiName);		// 差引項目名
					excel.write("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i, 					sashihikiNum);	// 差引回数
					excel.makeBorder("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i + "_title");
					excel.makeBorder("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i);
				}
				if(ks.sashihikiKingaku.getPdfHyoujiFlg()){
					i++;
					excel.write("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i + "_title",	ks.sashihikiKingaku.getName()); 		// 差引金額タイトル
					excel.write("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i,			EteamXlsFmt.formatMoney(BigDecimal.valueOf(0 - (sashihikiTanka * Double.parseDouble(sashihikiNum.isEmpty()? "0" : sashihikiNum)))));	// 差引金額
					excel.makeBorder("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i + "_title");
					excel.makeBorder("sashihiki_koumoku"  + kaigaiFlg + shubetsuCd + "_" + i);
				}
				if(i==0){
					excel.hideRow(excel.getCell("sashihiki_koumoku" + kaigaiFlg + shubetsuCd + "_1_title").getRow());
				}
				
			// 差引項目名称ブランクの場合
			}else{
				// 差引項目を行ごと非表示にする
				if(isKaigai) {
					excel.hideRow(excel.getCell("sashihiki_koumoku_gaika" + kaigaiFlg + shubetsuCd + "_1_title").getRow());
					excel.hideRow(excel.getCell("sashihiki_koumoku_gaika" + kaigaiFlg + shubetsuCd + "_3_title").getRow());
				}
				excel.hideRow(excel.getCell("sashihiki_koumoku" + kaigaiFlg + shubetsuCd + "_1_title").getRow());
			}
		}

		if(ks.meisaiKingaku.getPdfHyoujiFlg()){
			BigDecimal kingaku = BigDecimal.valueOf(shoukei);
			excel.write("shoukei" + kaigaiFlg + shubetsuCd,	0, 0, EteamXlsFmt.formatMoney(kingaku));
			if(!isKaribarai && !isKaigai  && isStartInvoice && isInvoice)
			{
				excel.write("shoukei_10_title"  + kaigaiFlg + shubetsuCd, 0, 0, "うち消費税10%");
				excel.makeBorder("shoukei_10_title"  + kaigaiFlg + shubetsuCd);
				excel.write("shoukei_10_percent" + kaigaiFlg + shubetsuCd,	0, 0, EteamXlsFmt.formatMoney(BigDecimal.valueOf(shoukei10Percent)));
				excel.makeBorder("shoukei_10_percent" + kaigaiFlg + shubetsuCd);
				excel.write("shoukei_8_title"  + kaigaiFlg + shubetsuCd, 0, 0, "うち消費税*8%");
				excel.makeBorder("shoukei_8_title"  + kaigaiFlg + shubetsuCd);				
				excel.write("shoukei_8_percent" + kaigaiFlg + shubetsuCd,	0, 0, EteamXlsFmt.formatMoney(BigDecimal.valueOf(shoukei8Percent)));
				excel.makeBorder("shoukei_8_percent" + kaigaiFlg + shubetsuCd);
			}
		}else{
			excel.hideRow(excel.getCell("shoukei" + kaigaiFlg + shubetsuCd).getRow());
		}
	}
	
	
	/**
	 * 海外旅費／海外旅費仮払の小計（海外・日当宿泊費）の差引項目（幣種コード、レート、単価（外貨）、単価（邦貨））を出力
	 * @param excel   excel object
	 * @param hontai  伝票情報マップ
	 */
	protected void makeExcelSashihikiGaika(EteamXls excel, GMap hontai) {
		
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		final String selName = "sashihiki_koumoku_gaika12_";	//1=海外、2=日当・宿泊費 の意味。
		int i = 0;
		if(sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA) 
				&& "1".equals(setting.kaigaiNittouTankaGaikaSettei())) {
			
			if(ks.sashihikiHeishuCdKaigai.getPdfHyoujiFlg()){
				i++;
				excel.write(selName + i + "_title",		ks.sashihikiHeishuCdKaigai.getName());
				excel.write(selName + i, 					hontai.get("sashihiki_heishu_cd_kaigai"));
				excel.makeBorder(selName + i + "_title");
				excel.makeBorder(selName + i);
			}
			if(ks.sashihikiRateKaigai.getPdfHyoujiFlg()){
				i++;
				excel.write(selName + i + "_title",		ks.sashihikiRateKaigai.getName());
				excel.write(selName + i, 					EteamXlsFmt.formatMoneyDecimalNoComma(hontai.get("sashihiki_rate_kaigai")));
				excel.makeBorder(selName + i + "_title");
				excel.makeBorder(selName + i);
			}
			if(ks.sashihikiTankaKaigaiGaika.getPdfHyoujiFlg()){
				i++;
				excel.write(selName + i + "_title",		ks.sashihikiTankaKaigaiGaika.getName());
				excel.write(selName + i, 					EteamXlsFmt.formatMoneyDecimal(hontai.get("sashihiki_tanka_kaigai_gaika")));
				excel.makeBorder(selName + i + "_title");
				excel.makeBorder(selName + i);
			}
			if(ks.sashihikiTankaKaigai.getPdfHyoujiFlg()){
				i++;
				excel.write(selName + i + "_title",		ks.sashihikiTankaKaigai.getName());
				excel.write(selName + i, 					EteamXlsFmt.formatMoney(hontai.get("sashihiki_tanka_kaigai")));
				excel.makeBorder(selName + i + "_title");
				excel.makeBorder(selName + i);
			}
		}
		
		//出力する項目数に応じて不要な行を削除
		if(i < 3) {
			excel.hideRow(excel.getCell(selName + "3_title").getRow());
		}
		if(i < 1) {
			excel.hideRow(excel.getCell(selName + "1_title").getRow());
		}
	}
	
	/**
	 * @param meisai 明細
	 * @return 出力に応じたフォーマット
	 */
	private String getSonotaTankaNissuu(GMap meisai)
	{
		return (ks.tanka.getPdfHyoujiFlg() && ks.nissuu.getPdfHyoujiFlg())
				? String.format("\r\n%s円×%s日", EteamXlsFmt.formatMoney(meisai.get("tanka")), meisai.get("nissuu")) // 単価・日数とも出力
				: ks.tanka.getPdfHyoujiFlg()
					? String.format("\r\n%s円", EteamXlsFmt.formatMoney(meisai.get("tanka"))) // 単価のみ
					: String.format("\r\n%s日", meisai.getString("nissuu")); // 日数のみ。paramsの最初の引数で通常のgetを入れると配列扱いされエラーになる
	}
	
	/**
	 * 消費税額詳細
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param excel Excel
	 */
	protected void getAndPrintInvoiceZei(String denpyouKbn, String denpyouId, EteamXls excel)
	{
		this.getAndPrintInvoiceZei(denpyouKbn, denpyouId, excel, BigDecimal.ZERO, BigDecimal.ZERO);
	}
	
	/**
	 * 消費税額詳細
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param excel Excel
	 * @param sashihikiKingaku 差引金額
	 * @param sashihikiZeigaku 差引分税額
	 */
	protected void getAndPrintInvoiceZei(String denpyouKbn, String denpyouId, EteamXls excel, BigDecimal sashihikiKingaku, BigDecimal sashihikiZeigaku)
	{
		// 空値及び以下は対象外なのでおしまい（ただし、そもそも呼び出すべきではない）
		// A002 経費伺い申請（仮払申請）
		// A005 出張伺い申請（仮払申請）
		// A012 海外出張伺い申請（仮払申請）
		// B伝票 簡易届
		if(isEmpty(denpyouKbn) || List.of("A002", "A005", "A012").contains(denpyouKbn) || denpyouKbn.contains("B"))
		{
			return;
		}
		
		excel.selectSheet(0);
		
		KamokuMasterAbstractDao kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		
		BigDecimal karibaraiZeigaku10PercentDecimal = new BigDecimal(0);
		BigDecimal karibaraiZeigaku10PercentMenzei80Decimal = new BigDecimal(0);
		BigDecimal karibaraiZeigaku8PercentDecimal = new BigDecimal(0);
		BigDecimal karibaraiZeigaku8PercentMenzei80Decimal = new BigDecimal(0);
		BigDecimal kariukeZeigaku10PercentDecimal = new BigDecimal(0);
		BigDecimal kariukeZeigaku10PercentTsumiageDecimal = new BigDecimal(0);
		BigDecimal kariukeZeigaku10PercentWarimodoshiDecimal = new BigDecimal(0);
		BigDecimal kariukeZeigaku8PercentDecimal = new BigDecimal(0);
		BigDecimal kariukeZeigaku8PercentTsumiageDecimal = new BigDecimal(0);
		BigDecimal kariukeZeigaku8PercentWarimodoshiDecimal = new BigDecimal(0);

		List<String> kamokuCdList = new ArrayList<String>();
		List<Boolean> isKarikataList = new ArrayList<Boolean>();
		List<String> kazeiKbnList = new ArrayList<String>();
		List<String> jigyoushaKbnList = new ArrayList<String>();
		List<BigDecimal> kingakuList = new ArrayList<BigDecimal>();
		List<BigDecimal> zeigakuList = new ArrayList<BigDecimal>();
		List<BigDecimal> zeiritsuList = new ArrayList<BigDecimal>();
		List<String> zeigakuKeisanList = new ArrayList<String>();
		
		// （経費）明細借方を見るもの
		// A001 経費立替精算
		// A003 請求書払い申請
		// A013 支払依頼申請
		// A009 自動引落伝票
		// A004 出張旅費精算（仮払精算）
		// A011 海外出張旅費精算（仮払精算）
		if(List.of("A001", "A003", "A013", "A009", "A004", "A011").contains(denpyouKbn))
		{
			// mapは共通なので、そこにアクセスして処理を共通化するためにIEteamDTOのリストを作っておく
			List<IEteamDTO> dtoList = new ArrayList<IEteamDTO>();
			String jigyoushaKbn = ""; // 支払依頼だけ本体側にあるので対策
			
			switch (denpyouKbn) {
				case "A001":
					KeihiseisanMeisaiAbstractDao keihiseisanMeisaiDao = EteamContainer.getComponent(KeihiseisanMeisaiDao.class, connection);
					dtoList = keihiseisanMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
				case "A003":
					SeikyuushobaraiMeisaiAbstractDao seikyuushobaraiMeisaiDao = EteamContainer.getComponent(SeikyuushobaraiMeisaiDao.class, connection);
					dtoList = seikyuushobaraiMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
				case "A013":
					ShiharaiIraiMeisaiAbstractDao shiharaiIraiMeisaiDao = EteamContainer.getComponent(ShiharaiIraiMeisaiDao.class, connection);
					dtoList = shiharaiIraiMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					ShiharaiIraiAbstractDao shiharaiIraiDao = EteamContainer.getComponent(ShiharaiIraiDao.class, connection);
					jigyoushaKbn = shiharaiIraiDao.find(denpyouId).jigyoushaKbn;
					break;
				case "A009":
					JidouhikiotoshiMeisaiAbstractDao jidouhikiotoshiMeisaiDao = EteamContainer.getComponent(JidouhikiotoshiMeisaiDao.class, connection);
					dtoList = jidouhikiotoshiMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
				case "A004":
					RyohiseisanKeihiMeisaiAbstractDao ryohiseisanKeihiMeisaiDao = EteamContainer.getComponent(RyohiseisanKeihiMeisaiDao.class, connection);
					dtoList = ryohiseisanKeihiMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
				case "A011":
					KaigaiRyohiseisanKeihiMeisaiAbstractDao kaigaiRyohiseisanKeihiMeisaiDao = EteamContainer.getComponent(KaigaiRyohiseisanKeihiMeisaiDao.class, connection);
					dtoList = kaigaiRyohiseisanKeihiMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
			}
			
			// 各リストへの追加
			for(var dto : dtoList)
			{
				var map = dto.getMap();
				BigDecimal shiharaiKingaku = (map.containsKey("shiharai_kingaku"))
				        ? map.get("shiharai_kingaku")
				        : (map.containsKey("meisai_kingaku"))
				            ? map.get("meisai_kingaku")
				            : BigDecimal.ZERO;
				BigDecimal shouhizeigaku = (map.containsKey("shouhizeigaku"))
				        ? map.get("shouhizeigaku")
				        : BigDecimal.ZERO;

				kamokuCdList.add(map.get("kari_kamoku_cd"));
				isKarikataList.add(true);
				kazeiKbnList.add((String) map.getOrDefault("kazei_kbn", map.getOrDefault("kari_kazei_kbn", "0")));
				jigyoushaKbnList.add((String) map.getOrDefault("jigyousha_kbn", map.getOrDefault("kari_jigyousha_kbn", isEmpty(jigyoushaKbn) ? "0" : jigyoushaKbn)));
				kingakuList.add(shiharaiKingaku);
				zeigakuList.add(shouhizeigaku);
				zeiritsuList.add(map.get("zeiritsu"));
				zeigakuKeisanList.add("");
			}
		}
		
		
		// 伝票本体借方+旅費明細借方
		// A004 出張旅費精算（仮払精算）
		// A011 海外出張旅費精算（仮払精算）
		// A010 交通費精算
		if(List.of("A004", "A010", "A011").contains(denpyouKbn))
		{
			// mapは共通なので多態的に処理する
			IEteamDTO hontaiDto = null;
			// mapは共通なので、そこにアクセスして処理を共通化するためにIEteamDTOのリストを作っておく
			List<IEteamDTO> meisaiDtoList = new ArrayList<IEteamDTO>();
			
			switch (denpyouKbn) {
				case "A004":
					RyohiseisanAbstractDao ryohiseisanDao = EteamContainer.getComponent(RyohiseisanDao.class, connection);
					hontaiDto = ryohiseisanDao.find(denpyouId);
					RyohiseisanMeisaiAbstractDao ryohiseisanMeisaiDao = EteamContainer.getComponent(RyohiseisanMeisaiDao.class, connection);
					meisaiDtoList = ryohiseisanMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
				case "A011":
					KaigaiRyohiseisanAbstractDao kaigaiRyohiseisanDao = EteamContainer.getComponent(KaigaiRyohiseisanDao.class, connection);
					hontaiDto = kaigaiRyohiseisanDao.find(denpyouId);
					KaigaiRyohiseisanMeisaiAbstractDao kaigaiRyohiseisanMeisaiDao = EteamContainer.getComponent(KaigaiRyohiseisanMeisaiDao.class, connection);
					meisaiDtoList = kaigaiRyohiseisanMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
				case "A010":
					KoutsuuhiseisanAbstractDao koutsuuhiseisanDao = EteamContainer.getComponent(KoutsuuhiseisanDao.class, connection);
					hontaiDto = koutsuuhiseisanDao.find(denpyouId);
					KoutsuuhiseisanMeisaiAbstractDao koutsuuhiseisanMeisaiDao = EteamContainer.getComponent(KoutsuuhiseisanMeisaiDao.class, connection);
					meisaiDtoList = koutsuuhiseisanMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> (IEteamDTO) dto).collect(Collectors.toList());
					break;
			}
			
			var hontaiMap = hontaiDto.getMap();
			
			// 各リストへの追加
			for(var meisaiDto : meisaiDtoList)
			{
				var map = meisaiDto.getMap();
				BigDecimal shiharaiKingaku = (map.containsKey("shiharai_kingaku"))
				        ? map.get("shiharai_kingaku")
				        : (map.containsKey("meisai_kingaku"))
				            ? map.get("meisai_kingaku")
				            : BigDecimal.ZERO;
				BigDecimal shouhizeigaku = (map.containsKey("shouhizeigaku"))
				        ? map.get("shouhizeigaku")
				        : BigDecimal.ZERO;

				kamokuCdList.add(hontaiMap.get("kari_kamoku_cd"));
				isKarikataList.add(true);
				kazeiKbnList.add((String) hontaiMap.getOrDefault("kazei_kbn", hontaiMap.getOrDefault("kari_kazei_kbn", "0")));
				jigyoushaKbnList.add((String) map.getOrDefault("jigyousha_kbn", map.getOrDefault("kari_jigyousha_kbn", "0")));
				kingakuList.add(shiharaiKingaku);
				zeigakuList.add(shouhizeigaku);
				zeiritsuList.add(hontaiMap.get("zeiritsu"));
				zeigakuKeisanList.add("");
			}
			
			// 旅費だけ、差し引きを考慮
			if(denpyouKbn.equals("A004"))
			{
				kamokuCdList.add(hontaiMap.get("kari_kamoku_cd"));
				isKarikataList.add(true);
				kazeiKbnList.add((String) hontaiMap.getOrDefault("kazei_kbn", hontaiMap.getOrDefault("kari_kazei_kbn", "0")));
				jigyoushaKbnList.add("0");
				kingakuList.add(sashihikiKingaku.multiply(new BigDecimal("-1")));
				zeigakuList.add(sashihikiZeigaku.multiply(new BigDecimal("-1")));
				zeiritsuList.add(hontaiMap.get("zeiritsu"));
				zeigakuKeisanList.add("");
			}
		}
		
		// 伝票本体借方（総合付替は付替元）
		// A006 通勤定期申請
		// A008 総合付替伝票
		if(List.of("A006", "A008").contains(denpyouKbn))
		{
			// mapは共通なので多態的に処理する
			IEteamDTO hontaiDto = null;
			boolean isTsuukinTeiki = denpyouKbn.equals("A006");
			
			// 2つしかないのでここはif~elseで処理
			TsuukinteikiAbstractDao tsuukinteikiDao = EteamContainer.getComponent(TsuukinteikiDao.class, connection);
			TsukekaeAbstractDao tsukekaeDao = EteamContainer.getComponent(TsukekaeDao.class, connection);
			TsukekaeMeisaiAbstractDao tsukekaeMeisaiDao = EteamContainer.getComponent(TsukekaeMeisaiDao.class, connection);
			hontaiDto = isTsuukinTeiki ? tsuukinteikiDao.find(denpyouId) : tsukekaeDao.find(denpyouId);
			var hontaiMap = hontaiDto.getMap();
			BigDecimal shouhizeigaku = isTsuukinTeiki ? hontaiMap.get("shouhizeigaku") : tsukekaeMeisaiDao.loadByDenpyouId(denpyouId).stream().map(dto -> dto.shouhizeigaku).reduce(BigDecimal.ZERO, BigDecimal::add);
			
			// 各リストの設定
			kamokuCdList.add((String) hontaiMap.getOrDefault("kari_kamoku_cd", hontaiMap.get("moto_kamoku_cd")));
			isKarikataList.add(true);
			kazeiKbnList.add((String) hontaiMap.getOrDefault("kari_kazei_kbn", hontaiMap.getOrDefault("moto_kazei_kbn", "0")));
			jigyoushaKbnList.add((String) hontaiMap.getOrDefault("jigyousha_kbn", hontaiMap.getOrDefault("moto_jigyousha_kbn", "0")));
			kingakuList.add((BigDecimal) hontaiMap.getOrDefault("kingaku", BigDecimal.ZERO));
			zeigakuList.add((BigDecimal) hontaiMap.getOrDefault("shouhizeigaku", shouhizeigaku));
			zeiritsuList.add((BigDecimal) hontaiMap.get("zeiritsu"));
			zeigakuKeisanList.add((String) hontaiMap.getOrDefault("moto_zeigaku_houshiki", ""));
		}
		
		// 本体・貸借両方
		// A007 振替伝票
		if(denpyouKbn.equals("A007"))
		{
			FurikaeAbstractDao furikaeDao = EteamContainer.getComponent(FurikaeDao.class, connection);
			var dto = furikaeDao.find(denpyouId);
			
			// 各リストの設定
			// 借方
			kamokuCdList.add(dto.kariKamokuCd);
			isKarikataList.add(true);
			kazeiKbnList.add(dto.kariKazeiKbn);
			jigyoushaKbnList.add(dto.kariJigyoushaKbn);
			kingakuList.add(dto.kingaku);
			zeigakuList.add(dto.shouhizeigaku);
			zeiritsuList.add(dto.kariZeiritsu);
			zeigakuKeisanList.add(dto.kariZeigakuHoushiki);
			
			// 貸方
			kamokuCdList.add(dto.kashiKamokuCd);
			isKarikataList.add(false);
			kazeiKbnList.add(dto.kashiKazeiKbn);
			jigyoushaKbnList.add(dto.kashiJigyoushaKbn);
			kingakuList.add(dto.kingaku);
			zeigakuList.add(dto.shouhizeigaku);
			zeiritsuList.add(dto.kashiZeiritsu);
			zeigakuKeisanList.add(dto.kashiZeigakuHoushiki);
		}
		
		// 付替先→明細貸方
		// A008 総合付替伝票
		if(denpyouKbn.equals("A008"))
		{
			TsukekaeMeisaiAbstractDao tsukekaeMeisaiDao = EteamContainer.getComponent(TsukekaeMeisaiDao.class, connection);
			TsukekaeAbstractDao tsukekaeDao = EteamContainer.getComponent(TsukekaeDao.class, connection);
			var zeiritsu = tsukekaeDao.find(denpyouId).zeiritsu;
			var dtoList = tsukekaeMeisaiDao.loadByDenpyouId(denpyouId);
			
			// 各リストへの追加
			for(var dto : dtoList)
			{
				kamokuCdList.add(dto.sakiKamokuCd);
				isKarikataList.add(false);
				kazeiKbnList.add(dto.sakiKazeiKbn);
				jigyoushaKbnList.add(dto.sakiJigyoushaKbn);
				kingakuList.add(dto.kingaku);
				zeigakuList.add(dto.shouhizeigaku);
				zeiritsuList.add(zeiritsu); // 全体で共通
				zeigakuKeisanList.add(dto.sakiZeigakuHoushiki);
			}
		}
		
		for(int index = 0; index < kamokuCdList.size(); index++)
		{
			//初期化
			BigDecimal zeigaku = BigDecimal.ZERO;
			Integer shoriGroup = 0;
			;
			String kamokuCd = kamokuCdList.get(index);
			//海外出張旅費精算での国内用データなしの場合
			if(!kamokuCd.isEmpty()) {
				var existKamoku = kamokuMasterDao.find(kamokuCd);
				if(existKamoku != null) {
					shoriGroup = kamokuMasterDao.find(kamokuCd).shoriGroup;
					zeigaku = shoriGroup < 21 ? zeigakuList.get(index) : kingakuList.get(index);
				}
			}
			// 0円と空値は無視
			if(zeigaku == null || zeigaku.intValue() == 0)
			{
				continue;
			}
			//税込・税抜系以外の消費税は集計しない
			if(!List.of("001", "002", "011", "012", "013", "014", "1", "2", "11", "12", "13", "14").contains(kazeiKbnList.get(index))) {
				continue;
			}
			
			boolean isKarikata = isKarikataList.get(index);
			boolean isKaribarai = List.of(5,6,10,21).contains(shoriGroup)
					|| (List.of(2,7,8).contains(shoriGroup) && List.of("011", "013", "11", "13").contains(kazeiKbnList.get(index)));
			boolean is10Percent = zeiritsuList.get(index).intValue() == 10;
			boolean isMenzei = isKaribarai && jigyoushaKbnList.get(index).equals("1");
			boolean isWarimodoshi = zeigakuKeisanList.get(index).equals("0");
			initParts(connection);
			int zeigakuHoushiki = kiShouhizeiSetting.findByDate(null).uriagezeigakuKeisan;
			
			BigDecimal multiplier = new BigDecimal(isKarikata == isKaribarai ? 1 : -1);
			
			zeigaku = zeigaku.multiply(multiplier);
			
			// 愚直に書くとネストが深くなりすぎるので、continueを使って若干簡略化
			// Streamを使って簡略化できる可能性はあるが、一旦保留
			if (isKaribarai) { // 仮払
				if (is10Percent) { // 10%
					if (isMenzei) {
						karibaraiZeigaku10PercentMenzei80Decimal = karibaraiZeigaku10PercentMenzei80Decimal.add(zeigaku); // 免税
						continue;
					}
					karibaraiZeigaku10PercentDecimal = karibaraiZeigaku10PercentDecimal.add(zeigaku); // 通常
					continue;
				}
				if (isMenzei) {
					karibaraiZeigaku8PercentMenzei80Decimal = karibaraiZeigaku8PercentMenzei80Decimal.add(zeigaku); // 8%免税
					continue;
				}
				karibaraiZeigaku8PercentDecimal = karibaraiZeigaku8PercentDecimal.add(zeigaku); // 8%通常
				continue;
			}
			if (is10Percent) { // 仮受10%
				kariukeZeigaku10PercentDecimal = kariukeZeigaku10PercentDecimal.add(zeigaku);
				if (isWarimodoshi || zeigakuHoushiki == 0) {
					kariukeZeigaku10PercentWarimodoshiDecimal = kariukeZeigaku10PercentWarimodoshiDecimal.add(zeigaku); // 割戻し
					continue;
				}else if(zeigakuHoushiki == 1) {
					kariukeZeigaku10PercentTsumiageDecimal = kariukeZeigaku10PercentTsumiageDecimal.add(zeigaku); // 積上げ
					continue;
				}
				continue;
			}
			kariukeZeigaku8PercentDecimal = kariukeZeigaku8PercentDecimal.add(zeigaku); // 仮受8%
			if (isWarimodoshi || zeigakuHoushiki == 0) {
				kariukeZeigaku8PercentWarimodoshiDecimal = kariukeZeigaku8PercentWarimodoshiDecimal.add(zeigaku); // 割戻し
				continue;
			}else if(zeigakuHoushiki == 1) {
				kariukeZeigaku8PercentTsumiageDecimal = kariukeZeigaku8PercentTsumiageDecimal.add(zeigaku); // 積上げ
			}
		}
		
		String[] karibaraiTableRows = new String[] { "karibarai_zeigaku_10_percent_title", "karibarai_zeigaku_10_percent_menzei_80_title", "karibarai_zeigaku_8_percent_title", "karibarai_zeigaku_8_percent_menzei_80_title" };
		String[] kariukeTableRows   = new String[] { "kariuke_zeigaku_10_percent_title", "kariuke_zeigaku_10_percent_tsumiage_title", "kariuke_zeigaku_10_percent_warimodoshi_title", "kariuke_zeigaku_8_percent_title", "kariuke_zeigaku_8_percent_tsumiage_title", "kariuke_zeigaku_8_percent_warimodoshi_title" };
		var karibaraiList = List.of(karibaraiZeigaku10PercentDecimal, karibaraiZeigaku10PercentMenzei80Decimal, karibaraiZeigaku8PercentDecimal, karibaraiZeigaku8PercentMenzei80Decimal);
		var kariukeList = List.of(kariukeZeigaku10PercentDecimal, kariukeZeigaku10PercentTsumiageDecimal, kariukeZeigaku10PercentWarimodoshiDecimal, kariukeZeigaku8PercentDecimal, kariukeZeigaku8PercentTsumiageDecimal, kariukeZeigaku8PercentWarimodoshiDecimal);
		// 全て0ならおしまい		
		if((karibaraiList != null && karibaraiList.stream().allMatch(bd -> bd.signum() == 0)) && 
		   ((!List.of("A007", "A008").contains(denpyouKbn)) || 	(kariukeList != null && kariukeList.stream().allMatch(bd -> bd.signum() == 0))))
		{
			// hideRow処理
			excel.hideRow(excel.getCell("shouhizeigaku_shousai").getRow());
			excel.hideRow(excel.getCell("karibarai_shouhizei").getRow());
			for(int i = 0; i < kariukeTableRows.length; i++)
			{
				excel.hideRow(excel.getCell(kariukeTableRows[i]).getRow());
			}
			return;
		}
		//インボイス開始処理前またはインボイス前伝票なら出力しない
		if(!isStartInvoice || isBeforeInvoice) {
			// hideRow処理
			excel.hideRow(excel.getCell("shouhizeigaku_shousai").getRow());
			excel.hideRow(excel.getCell("karibarai_shouhizei").getRow());
			for(int i = 0; i < kariukeTableRows.length; i++)
			{
				excel.hideRow(excel.getCell(kariukeTableRows[i]).getRow());
			}
			return;
		}
		
		excel.write("shouhizeigaku_shousai", "消費税額詳細");
		
		if(karibaraiList.stream().anyMatch(bd -> bd.compareTo(BigDecimal.ZERO) != 0))
		{
			excel.write("karibarai_shouhizei", "仮払消費税");
			excel.makeBorder("karibarai_shouhizei");
		}
		
		if(List.of("A007", "A008").contains(denpyouKbn) && kariukeList.stream().anyMatch(bd -> bd.compareTo(BigDecimal.ZERO) != 0))
		{
			excel.write("kariuke_shouhizei", "仮受消費税");
			excel.makeBorder("kariuke_shouhizei");
		}
		boolean[] isKaribaraiWritten = new boolean[] {false, false, false, false, false, false};
		boolean[] isKariukeWritten   = new boolean[] {false, false, false, false, false, false};
		
		// 全体共通
		isKaribaraiWritten[0] = this.writeIfNotZero(karibaraiZeigaku10PercentDecimal, 			"karibarai_zeigaku_10_percent", "10%", excel);
		isKaribaraiWritten[1] = this.writeIfNotZero(karibaraiZeigaku10PercentMenzei80Decimal, 	"karibarai_zeigaku_10_percent_menzei_80", "10%（仕入税額控除経過措置割合（80％））", excel);
		isKaribaraiWritten[2] = this.writeIfNotZero(karibaraiZeigaku8PercentDecimal, 			"karibarai_zeigaku_8_percent", "軽減税率8%", excel);
		isKaribaraiWritten[3] = this.writeIfNotZero(karibaraiZeigaku8PercentMenzei80Decimal, 	"karibarai_zeigaku_8_percent_menzei_80", "軽減税率8%（仕入税額控除経過措置割合（80％））", excel);
		
		// 仮受消費税を表示するケースの場合
		if(List.of("A007", "A008").contains(denpyouKbn))
		{
			isKariukeWritten[0] = this.writeIfNotZero(kariukeZeigaku10PercentDecimal, "kariuke_zeigaku_10_percent", "10%", excel);
			isKariukeWritten[1] = this.writeIfNotZero(kariukeZeigaku10PercentTsumiageDecimal, "kariuke_zeigaku_10_percent_tsumiage", "10%（積上げ計算）", excel);
			isKariukeWritten[2] = this.writeIfNotZero(kariukeZeigaku10PercentWarimodoshiDecimal, "kariuke_zeigaku_10_percent_warimodoshi", "10%（割戻し計算）", excel);
			isKariukeWritten[3] = this.writeIfNotZero(kariukeZeigaku8PercentDecimal, "kariuke_zeigaku_8_percent", "軽減税率8%", excel);
			isKariukeWritten[4] = this.writeIfNotZero(kariukeZeigaku8PercentTsumiageDecimal, "kariuke_zeigaku_8_percent_tsumiage", "軽減税率8%（積上げ計算）", excel);
			isKariukeWritten[5] = this.writeIfNotZero(kariukeZeigaku8PercentWarimodoshiDecimal, "kariuke_zeigaku_8_percent_warimodoshi", "軽減税率8%（割戻し計算）", excel);
		
			// 不要な行の除去
			for(int i = 0; i < kariukeTableRows.length; i++)
			{
				if(!isKaribaraiWritten[i] && !isKariukeWritten[i])
				{
					excel.hideRow(excel.getCell(kariukeTableRows[i]).getRow());
				}
			}
		} else {
			// 振替伝票(A007)・総合付替伝票(A008)以外は仮受消費税はなし
			for(int i = 0; i < karibaraiTableRows.length; i++)
			{
				if(!isKaribaraiWritten[i])
				{
					excel.hideRow(excel.getCell(karibaraiTableRows[i]).getRow());
				}
			}
			
			//TODO もっと簡潔にできる　そもそもA007・A008以外には仮受が必要ない　でもL.1526～1537のためテンプレ側から仮受を消せない
			if(excel.isExistCell(kariukeTableRows[4]))
			{
				excel.hideRow(excel.getCell(kariukeTableRows[4]).getRow());
			}
			if(excel.isExistCell(kariukeTableRows[5]))
			{
				excel.hideRow(excel.getCell(kariukeTableRows[5]).getRow());
			}
		}
	}
    
    /**
     * @param kingaku 金額
     * @param cellNameBase セル名基幹部
     * @param title タイトル
     * @param excel Excel
     * @return 0以上で、セルが使われたか
     */
    protected boolean writeIfNotZero(BigDecimal kingaku, String cellNameBase, String title, EteamXls excel)
    {
    	if(kingaku.compareTo(BigDecimal.ZERO) == 0)
    	{
    		return false;
    	}
    	
		excel.write(cellNameBase + "_title", title);
		excel.makeBorder(cellNameBase + "_title");
		excel.write(cellNameBase + "_kingaku", EteamXlsFmt.formatMoney(kingaku));
		excel.makeBorder(cellNameBase + "_kingaku");
		return true;
    }
    
    //＜内部処理＞
  	/**
  	 * 初期化処理
  	 * @param connection コネクション
  	 */
  	protected void initParts(EteamConnection connection) {
  		kiShouhizeiSetting = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
  	}
}
