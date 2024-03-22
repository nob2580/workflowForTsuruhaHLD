package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.util.List;

import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;
import eteam.database.abstractdao.FurikaeAbstractDao;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.FurikaeDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.Furikae;
import eteam.database.dto.InvoiceStart;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 振替伝票Logic
 */
public class FurikaeDenpyouXlsLogic extends KaikeiCommonXlsLogic {

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.FURIKAE_DENPYOU);
	
	/** UF1表示 */
	String uf1ShiyouFlg = hfUfSeigyo.getUf1ShiyouFlg();
	/** UF2表示 */
	String uf2ShiyouFlg = hfUfSeigyo.getUf2ShiyouFlg();
	/** UF3表示 */
	String uf3ShiyouFlg = hfUfSeigyo.getUf3ShiyouFlg();
	/** UF4表示 */
	 String uf4ShiyouFlg = hfUfSeigyo.getUf4ShiyouFlg();
	/** UF5表示 */
	 String uf5ShiyouFlg = hfUfSeigyo.getUf5ShiyouFlg();
	/** UF6表示 */
	 String uf6ShiyouFlg = hfUfSeigyo.getUf6ShiyouFlg();
	/** UF7表示 */
	 String uf7ShiyouFlg = hfUfSeigyo.getUf7ShiyouFlg();
	/** UF8表示 */
	 String uf8ShiyouFlg = hfUfSeigyo.getUf8ShiyouFlg();
	/** UF9表示 */
	 String uf9ShiyouFlg = hfUfSeigyo.getUf9ShiyouFlg();
	/** UF10表示 */
	 String uf10ShiyouFlg = hfUfSeigyo.getUf10ShiyouFlg();

	/**
	 * @param denpyouId 伝票ID
	 * @param out 出力先
	 */ 
	public void makeExcel(String denpyouId, OutputStream out) {

		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		FurikaeAbstractDao FurikaeDao = EteamContainer.getComponent(FurikaeDao.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		KaikeiCommonXlsLogic commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		KiShouhizeiSettingDao kiShouhizeiSetting = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		KamokuMasterDao kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_furikae.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell tekiyouCell = book.findCellByName("tekiyou");
		int tekiyouRow = tekiyouCell.getRow();

		// ---------------------------------------
		//共通部分（下部）
		// ---------------------------------------
		wfXlsLogic.makeExcelBottom(excel, denpyouId);

		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		Furikae hontai = FurikaeDao.find(denpyouId);
		var denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.FURIKAE_DENPYOU);
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.FURIKAE_DENPYOU, hontai.map, null, "0");
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;			
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		boolean shiireHyoujiFlg   	= 1 == kiShouhizeiSetting.findByDate(null).shiireZeigakuAnbunFlg;
		boolean isInvoice   	= "0".equals(hontai.invoiceDenpyou);
		boolean isUriageZeiHeiyou = 2 == kiShouhizeiSetting.findByDate(null).uriagezeigakuKeisan ? true : false;
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String kingakuTitle = "";
		String kingaku = "";
		String denpyoubiTitle = "";
		String denpyoubi = "";
		String kanjouTitle = "";
		String karikanjou = "";
		String kashikanjou = "";
		String kanjouedabanTitle = "";
		String karikanjouedaban = "";
		String kashikanjouedaban = "";
		String futanbumonTitle = "";
		String karifutanbumon = "";
		String kashifutanbumon = "";
		String torihikisakiTitle = "";
		String karitorihikisaki = "";
		String kashitorihikisaki = "";
		String jigyoushakbnTitle = "";
		String karijigyoushakbn = "";
		String kashijigyoushakbn = "";
		String kazeikubunTitle = "";
		String karikazeikubun = "";
		String kashikazeikubun = "";
		String zeiritsuTitle = "";
		String karizeiritsu = "";
		String kashizeiritsu = "";
		String bunrikbnTitle = "";
		String karibunrikbn = "";
		String kashibunrikbn = "";
		String shiirekbnTitle = "";
		String karishiirekbn = "";
		String kashishiirekbn = "";
		String zeigakukeisanTitle = "";
		String karizeigakukeisan = "";
		String kashizeigakukeisan = "";
		String tekiyouTitle = "";
		
		String bikouTitle = "";
		String bikou = "";
		String projectTitle = "";
		String kariproject = "";
		String kashiproject = "";
		String segmentTitle = "";
		String kariSegment = "";
		String kashiSegment = "";
		String uf1Title = "";
		String kariUf1 = "";
		String kashiUf1 = "";
		String uf2Title = "";
		String kariUf2 = "";
		String kashiUf2 = "";
		String uf3Title = "";
		String kariUf3 = "";
		String kashiUf3 = "";
		String uf4Title = "";
		String kariUf4 = "";
		String kashiUf4 = "";
		String uf5Title = "";
		String kariUf5 = "";
		String kashiUf5 = "";
		String uf6Title = "";
		String kariUf6 = "";
		String kashiUf6 = "";
		String uf7Title = "";
		String kariUf7 = "";
		String kashiUf7 = "";
		String uf8Title = "";
		String kariUf8 = "";
		String kashiUf8 = "";
		String uf9Title = "";
		String kariUf9 = "";
		String kashiUf9 = "";
		String uf10Title = "";
		String kariUf10 = "";
		String kashiUf10 = "";

		String tekiyou = "";
		
		boolean titleDisp = false;
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu);
		
		//インボイス処理開始後、かつインボイスフラグが「1:(インボイス対応前)」のときのみ表示
		commonXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		// インボイス関連処理
		super.getAndPrintInvoiceZei("A007", denpyouId, excel);
		
		//処理グループの取得
		String kariKmkShoriGroup = kamokuMasterDao.find(hontai.kariKamokuCd).shoriGroup.toString();
		String kashiKmkShoriGroup = kamokuMasterDao.find(hontai.kashiKamokuCd).shoriGroup.toString();

		//■申請内容(表示フラグで制御)
		//金額
		if(ks.kingaku.getPdfHyoujiFlg()){
			kingakuTitle = ks.kingaku.getName();
			kingaku = EteamXlsFmt.formatMoney(hontai.kingaku);
			excel.makeBorder("kingaku_title");
			excel.makeBorder("kingaku");
		}
		//伝票日
		if(ks.denpyouDate.getPdfHyoujiFlg()){
			denpyoubiTitle = ks.denpyouDate.getName();
			denpyoubi = EteamXlsFmt.formatDate(hontai.denpyouDate);
		}
		
		//勘定科目
		if(ks.kamoku.getPdfHyoujiFlg()){
			kanjouTitle = ks.kamoku.getName();
			karikanjou = ks.kamoku.getCodeSeigyoValue(hontai.kariKamokuCd, hontai.kariKamokuName, " ");
			kashikanjou= ks.kamoku.getCodeSeigyoValue(hontai.kashiKamokuCd, hontai.kashiKamokuName, " ");
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kanjou_title").getRow());
		} 
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()){
			kazeikubunTitle = ks.kazeiKbn.getName();
			karikazeikubun = sysLogic.findNaibuCdName("kazei_kbn", hontai.kariKazeiKbn);
			kashikazeikubun = sysLogic.findNaibuCdName("kazei_kbn", hontai.kashiKazeiKbn);
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kazeikubun_title").getRow());
		}
		//消費税率
		if(ks.zeiritsu.getPdfHyoujiFlg()){
			zeiritsuTitle = ks.zeiritsu.getName();
			if(List.of("001", "002", "011", "012", "013", "014").contains(hontai.kariKazeiKbn) || List.of("21", "22").contains(kariKmkShoriGroup)) 
			{
				karizeiritsu = EteamXlsFmt.formatZeiritsu(hontai.kariZeiritsu, hontai.kariKeigenZeiritsuKbn) + " %";
			}
			else 
			{
				karizeiritsu = "";
			}
			if(List.of("001", "002", "011", "012", "013", "014").contains(hontai.kashiKazeiKbn)	|| List.of("21", "22").contains(kashiKmkShoriGroup))
			{
				kashizeiritsu = EteamXlsFmt.formatZeiritsu(hontai.kashiZeiritsu, hontai.kashiKeigenZeiritsuKbn) + " %";
			}
			else
			{
				kashizeiritsu = "";
			}
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("zeiritsu_title").getRow());
		}
		//分離区分
		if(ks.bunriKbn.getPdfHyoujiFlg()){
			bunrikbnTitle = ks.bunriKbn.getName();
			String kariBunriKbn = hontai.kariBunriKbn == null ? "9" : hontai.kariBunriKbn ;
			if(hontai.kariKazeiKbn.equals("001") || hontai.kariKazeiKbn.equals("011")|| hontai.kariKazeiKbn.equals("012")) {
				karibunrikbn = sysLogic.findNaibuCdName("bunri_kbn", kariBunriKbn);
			}
			String kashiBunriKbn = hontai.kashiBunriKbn == null ? "9" : hontai.kashiBunriKbn ;
			if(hontai.kashiKazeiKbn.equals("001") || hontai.kashiKazeiKbn.equals("011")|| hontai.kashiKazeiKbn.equals("012")) {
				kashibunrikbn = sysLogic.findNaibuCdName("bunri_kbn", kashiBunriKbn);
			}			
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("bunrikbn_title").getRow());
		}
		//仕入区分
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg){
			shiirekbnTitle = ks.shiireKbn.getName();
			karishiirekbn = sysLogic.findNaibuCdName("shiire_kbn", hontai.kariShiireKbn);
			kashishiirekbn = sysLogic.findNaibuCdName("shiire_kbn", hontai.kashiShiireKbn);
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("shiirekbn_title").getRow());
		}
		//勘定科目枝番
		if(ks.kamokuEdaban.getPdfHyoujiFlg()){
			kanjouedabanTitle = ks.kamokuEdaban.getName();
			karikanjouedaban = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kariKamokuEdabanCd, hontai.kariKamokuEdabanName, " ");
			kashikanjouedaban = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kashiKamokuEdabanCd, hontai.kashiKamokuEdabanName, " ");
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kanjouedaban_title").getRow());
		}
		//負担部門
		if(ks.futanBumon.getPdfHyoujiFlg()){
			futanbumonTitle = ks.futanBumon.getName();
			karifutanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.kariFutanBumonCd, hontai.kariFutanBumonName, " ");
			kashifutanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.kashiFutanBumonCd, hontai.kashiFutanBumonName, " ");
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("futanbumon_title").getRow());
		}
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiTitle = ks.torihikisaki.getName();
			karitorihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.kariTorihikisakiCd.toString(), hontai.kariTorihikisakiNameRyakushiki," ");
			kashitorihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.kashiTorihikisakiCd.toString(), hontai.kashiTorihikisakiNameRyakushiki," ");
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("torihikisaki_title").getRow());
		}
		//事業者区分
		if("1".equals(invoiceStartFlg) && isInvoice && ks.jigyoushaKbn.getPdfHyoujiFlg()){
			jigyoushakbnTitle = ks.jigyoushaKbn.getName();
			karijigyoushakbn = sysLogic.findNaibuCdName("jigyousha_kbn", hontai.kariJigyoushaKbn);
			kashijigyoushakbn = sysLogic.findNaibuCdName("jigyousha_kbn", hontai.kashiJigyoushaKbn);
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("jigyoushakbn_title").getRow());
		}
		//税額計算方式
		if("1".equals(invoiceStartFlg) && isInvoice && ks.uriagezeigakuKeisan.getPdfHyoujiFlg() && isUriageZeiHeiyou){
			zeigakukeisanTitle = ks.uriagezeigakuKeisan.getName();
			//借方
			if(List.of("4","9").contains(kariKmkShoriGroup) && List.of("001", "002").contains(hontai.kariKazeiKbn)) { 
				karizeigakukeisan = sysLogic.findNaibuCdName("uriagezeigaku_keisan", hontai.kariZeigakuHoushiki);
			} else {
				karizeigakukeisan = "";
			}
			//貸方
			if(List.of("4","9").contains(kashiKmkShoriGroup) && List.of("001", "002").contains(hontai.kashiKazeiKbn)) {
				kashizeigakukeisan = sysLogic.findNaibuCdName("uriagezeigaku_keisan", hontai.kashiZeigakuHoushiki);
			} else {
				kashizeigakukeisan = "";
			}
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("zeigakukeisan_title").getRow());
		}

		//摘要
		if(ks.tekiyou.getPdfHyoujiFlg()){
			tekiyouTitle = ks.tekiyou.getName();
			tekiyou = hontai.tekiyou + chuuki;
			titleDisp = true;
			
			//摘要行と備考行の初期化
			int tekiyouLineNum = 1;
			//高さ調整：摘要と備考は1行21文字以内で計算。デフォルト1行だけどそれ以上の行数になるなら高さ変える。
			int tekiyouLineNumTmp = excel.lineCount(tekiyou, 42, 1);
			if( tekiyouLineNum < tekiyouLineNumTmp){
				tekiyouLineNum = tekiyouLineNumTmp;
			}
			excel.setHeight(tekiyouRow,  tekiyouLineNum, 1);
			
		}else{
			excel.hideRow(excel.getCell("tekiyou_title").getRow());
		}
		//備考
		if(ks.bikou.getPdfHyoujiFlg()){
			bikouTitle = ks.bikou.getName();
			bikou = hontai.bikou;
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("bikou_title").getRow());
		}
		
		//プロジェクト表示/非表示処理。非表示の場合、プロジェクト項目も削除
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
			projectTitle = ks.project.getName();
			kariproject = ks.project.getCodeSeigyoValue(hontai.kariProjectCd, hontai.kariProjectName, " ");
			kashiproject = ks.project.getCodeSeigyoValue(hontai.kashiProjectCd, hontai.kashiProjectName, " ");
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kari_purojekuto").getRow());
		}
		
		//セグメント表示/非表示処理。非表示の場合、セグメント項目も削除
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
			segmentTitle = ks.segment.getName();
			kariSegment = ks.segment.getCodeSeigyoValue(hontai.kariSegmentCd, hontai.kariSegmentNameRyakushiki, " ");
			kashiSegment = ks.segment.getCodeSeigyoValue(hontai.kashiSegmentCd, hontai.kashiSegmentNameRyakushiki, " ");
			titleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kari_segment").getRow());
		}
		
		//UF表示/非表示処理
		if (!uf1ShiyouFlg.equals("0") && ks.uf1.getPdfHyoujiFlg()) {
			if(uf1ShiyouFlg.equals("1")){
				kariUf1 = hontai.kariUf1Cd;
				kashiUf1 = hontai.kashiUf1Cd;
			}else if(uf1ShiyouFlg.equals("2") || uf1ShiyouFlg.equals("3")){
				kariUf1 = hontai.kariUf1NameRyakushiki;
				kashiUf1 = hontai.kashiUf1NameRyakushiki;
			}
			uf1Title = KaishaInfo.getKaishaInfo(ColumnName.UF1_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf1").getRow());
		}

		if (!uf2ShiyouFlg.equals("0") && ks.uf2.getPdfHyoujiFlg()) {
			if(uf2ShiyouFlg.equals("1")){
				kariUf2 = hontai.kariUf2Cd;
				kashiUf2 = hontai.kashiUf2Cd;
			}else if(uf2ShiyouFlg.equals("2") || uf2ShiyouFlg.equals("3")){
				kariUf2 = hontai.kariUf2NameRyakushiki;
				kashiUf2 = hontai.kashiUf2NameRyakushiki;
			}
			uf2Title = KaishaInfo.getKaishaInfo(ColumnName.UF2_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf2").getRow());
		}
		
		if (!uf3ShiyouFlg.equals("0") && ks.uf3.getPdfHyoujiFlg()) {
			if(uf3ShiyouFlg.equals("1")){
				kariUf3 = hontai.kariUf3Cd;
				kashiUf3 = hontai.kashiUf3Cd;
			}else if(uf3ShiyouFlg.equals("2") || uf3ShiyouFlg.equals("3")){
				kariUf3 = hontai.kariUf3NameRyakushiki;
				kashiUf3 = hontai.kashiUf3NameRyakushiki;
			}
			uf3Title = KaishaInfo.getKaishaInfo(ColumnName.UF3_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf3").getRow());
		}
		
		if (!uf4ShiyouFlg.equals("0") && ks.uf4.getPdfHyoujiFlg()) {
			if (uf4ShiyouFlg.equals("1")) {
				kariUf4 =  hontai.kariUf4Cd;
				kashiUf4 = hontai.kashiUf4Cd;
			} else if (uf4ShiyouFlg.equals("2") || uf4ShiyouFlg.equals("3")) {
				kariUf4 = hontai.kariUf4NameRyakushiki;
				kashiUf4 = hontai.kashiUf4NameRyakushiki;
			}
			uf4Title = KaishaInfo.getKaishaInfo(ColumnName.UF4_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf4").getRow());
		}
		
		if (!uf5ShiyouFlg.equals("0") && ks.uf5.getPdfHyoujiFlg()) {
			if (uf5ShiyouFlg.equals("1")) {
				kariUf5 = hontai.kariUf5Cd;
				kashiUf5 = hontai.kashiUf5Cd;
			} else if (uf5ShiyouFlg.equals("2") || uf5ShiyouFlg.equals("3")) {
				kariUf5 = hontai.kariUf5NameRyakushiki;
				kashiUf5 = hontai.kashiUf5NameRyakushiki;
			}
			uf5Title = KaishaInfo.getKaishaInfo(ColumnName.UF5_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf5").getRow());
		}
		
		if (!uf6ShiyouFlg.equals("0") && ks.uf6.getPdfHyoujiFlg()) {
			if (uf6ShiyouFlg.equals("1")) {
				kariUf6 = hontai.kariUf6Cd;
				kashiUf6 = hontai.kashiUf6Cd;
			} else if (uf6ShiyouFlg.equals("2") || uf6ShiyouFlg.equals("3")) {
				kariUf6 = hontai.kariUf6NameRyakushiki;
				kashiUf6 = hontai.kashiUf6NameRyakushiki;
			}
			uf6Title = KaishaInfo.getKaishaInfo(ColumnName.UF6_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf6").getRow());
		}
		
		if (!uf7ShiyouFlg.equals("0") && ks.uf7.getPdfHyoujiFlg()) {
			if (uf7ShiyouFlg.equals("1")) {
				kariUf7 = hontai.kariUf7Cd;
				kashiUf7 = hontai.kashiUf7Cd;
			} else if (uf7ShiyouFlg.equals("2") || uf7ShiyouFlg.equals("3")) {
				kariUf7 = hontai.kariUf7NameRyakushiki;
				kashiUf7 = hontai.kashiUf7NameRyakushiki;
			}
			uf7Title = KaishaInfo.getKaishaInfo(ColumnName.UF7_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf7").getRow());
		}
		
		if (!uf8ShiyouFlg.equals("0") && ks.uf8.getPdfHyoujiFlg()) {
			if (uf8ShiyouFlg.equals("1")) {
				kariUf8 = hontai.kariUf8Cd;
				kashiUf8 = hontai.kashiUf8Cd;
			} else if (uf8ShiyouFlg.equals("2") || uf8ShiyouFlg.equals("3")) {
				kariUf8 = hontai.kariUf8NameRyakushiki;
				kashiUf8 = hontai.kashiUf8NameRyakushiki;
			}
			uf8Title = KaishaInfo.getKaishaInfo(ColumnName.UF8_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf8").getRow());
		}
		
		if (!uf9ShiyouFlg.equals("0") && ks.uf9.getPdfHyoujiFlg()) {
			if (uf9ShiyouFlg.equals("1")) {
				kariUf9 = hontai.kariUf9Cd;
				kashiUf9 = hontai.kashiUf9Cd;
			} else if (uf9ShiyouFlg.equals("2") || uf9ShiyouFlg.equals("3")) {
				kariUf9 = hontai.kariUf9NameRyakushiki;
				kashiUf9 = hontai.kashiUf9NameRyakushiki;
			}
			uf9Title = KaishaInfo.getKaishaInfo(ColumnName.UF9_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf9").getRow());
		}
		
		if (!uf10ShiyouFlg.equals("0") && ks.uf10.getPdfHyoujiFlg()) {
			if (uf10ShiyouFlg.equals("1")) {
				kariUf10 = hontai.kariUf10Cd;
				kashiUf10 = hontai.kashiUf10Cd;
			} else if (uf10ShiyouFlg.equals("2") || uf10ShiyouFlg.equals("3")) {
				kariUf10 = hontai.kariUf10NameRyakushiki;
				kashiUf10 = hontai.kashiUf10NameRyakushiki;
			}
			uf10Title = KaishaInfo.getKaishaInfo(ColumnName.UF10_NAME);
			titleDisp = true;
		} else {
			excel.hideRow(excel.getCell("kari_uf10").getRow());
		}

		//excelに各項目出力
		excel.write("kingaku_title", kingakuTitle);
		excel.write("kingaku", kingaku);
		excel.write("denpyoubi_title", denpyoubiTitle);
		excel.write("denpyou", denpyoubi);
		excel.write("kanjou_title", kanjouTitle);
		excel.write("kari_kanjou", karikanjou);
		excel.write("kashi_kanjou", kashikanjou);
		excel.write("kazeikubun_title", kazeikubunTitle);
		excel.write("kari_kazeikubun", karikazeikubun);
		excel.write("kashi_kazeikubun", kashikazeikubun);
		excel.write("zeiritsu_title", zeiritsuTitle);
		excel.write("kari_zeiritsu", karizeiritsu);
		excel.write("kashi_zieritsu", kashizeiritsu);
		excel.write("bunrikbn_title", bunrikbnTitle);
		excel.write("kari_bunrikbn", karibunrikbn);
		excel.write("kashi_bunrikbn", kashibunrikbn);
		excel.write("shiirekbn_title", shiirekbnTitle);
		excel.write("kari_shiirekbn", karishiirekbn);
		excel.write("kashi_shiirekbn", kashishiirekbn);
		excel.write("kanjouedaban_title", kanjouedabanTitle);
		excel.write("kari_kanjouedaban", karikanjouedaban);
		excel.write("kashi_kanjouedaban", kashikanjouedaban);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("kari_futanbumon", karifutanbumon);
		excel.write("kashi_futanbumon", kashifutanbumon);
		excel.write("torihikisaki_title", torihikisakiTitle);
		excel.write("kari_torihikisaki", karitorihikisaki);
		excel.write("kashi_torihikisaki", kashitorihikisaki);
		excel.write("jigyoushakbn_title", jigyoushakbnTitle);
		excel.write("kari_jigyoushakbn", karijigyoushakbn);
		excel.write("kashi_jigyoushakbn", kashijigyoushakbn);
		excel.write("zeigakukeisan_title", zeigakukeisanTitle);
		excel.write("kari_zeigakukeisan", karizeigakukeisan);
		excel.write("kashi_zeigakukeisan", kashizeigakukeisan);
		excel.write("tekiyou_title", tekiyouTitle);
		excel.write("tekiyou", tekiyou);
		excel.write("bikou_title", bikouTitle);
		excel.write("bikou", bikou);
		excel.write("project_title", projectTitle);
		excel.write("kari_purojekuto", kariproject);
		excel.write("kashi_purojekuto", kashiproject);
		excel.write("segment_title", segmentTitle);
		excel.write("kari_segment", kariSegment);
		excel.write("kashi_segment", kashiSegment);
		
		excel.write("uf1_name", uf1Title);
		excel.write("kari_uf1", kariUf1);
		excel.write("kashi_uf1", kashiUf1);
		excel.write("uf2_name", uf2Title);
		excel.write("kari_uf2", kariUf2);
		excel.write("kashi_uf2", kashiUf2);
		excel.write("uf3_name", uf3Title);
		excel.write("kari_uf3", kariUf3);
		excel.write("kashi_uf3", kashiUf3);
		excel.write("uf4_name",  uf4Title);
		excel.write("kari_uf4",  kariUf4);
		excel.write("kashi_uf4", kashiUf4);
		excel.write("uf5_name",  uf5Title);
		excel.write("kari_uf5",  kariUf5);
		excel.write("kashi_uf5", kashiUf5);
		excel.write("uf6_name",  uf6Title);
		excel.write("kari_uf6",  kariUf6);
		excel.write("kashi_uf6", kashiUf6);
		excel.write("uf7_name",  uf7Title);
		excel.write("kari_uf7",  kariUf7);
		excel.write("kashi_uf7", kashiUf7);
		excel.write("uf8_name",  uf8Title);
		excel.write("kari_uf8",  kariUf8);
		excel.write("kashi_uf8", kashiUf8);
		excel.write("uf9_name",  uf9Title);
		excel.write("kari_uf9",  kariUf9);
		excel.write("kashi_uf9", kashiUf9);
		excel.write("uf10_name",  uf10Title);
		excel.write("kari_uf10",  kariUf10);
		excel.write("kashi_uf10", kashiUf10);

		if(!titleDisp){
			excel.hideRow(excel.getCell("kingaku_title").getRow() + 2);
			excel.hideRow(excel.getCell("kingaku_title").getRow() + 3);
		}
		
		// ---------------------------------------
		//共通部分
		// ---------------------------------------
		wfXlsLogic.makeExcel(excel, denpyouId, false);

		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
}
