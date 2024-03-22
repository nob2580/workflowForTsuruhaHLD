package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiharaiIraiAbstractDao;
import eteam.database.abstractdao.ShiharaiIraiMeisaiAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiharaiIraiDao;
import eteam.database.dao.ShiharaiIraiMeisaiDao;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.ShiharaiIrai;
import eteam.database.dto.ShiharaiIraiMeisai;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 請求書払い申請Logic
 */
public class ShiharaiIraiXlsLogic extends KaikeiCommonXlsLogic {
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SIHARAIIRAI);
	
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
		ShiharaiIraiAbstractDao shiharaiIraiDao = EteamContainer.getComponent(ShiharaiIraiDao.class, connection);
		ShiharaiIraiMeisaiAbstractDao shiharaiIraiMeisaiDao = EteamContainer.getComponent(ShiharaiIraiMeisaiDao.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		NaibuCdSettingAbstractDao naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		KiShouhizeiSettingDao   	kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		KaikeiCommonXlsLogic commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_shiharaiirai.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell meisaiCell = book.findCellByName("meisai_no");
		int meisaiRow = meisaiCell.getRow();
		Cell meisaikoumokuCell = book.findCellByName("puroject_hyouji_hihyouji");
		int meisaikoumokuRow = meisaikoumokuCell.getRow();
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
		ShiharaiIrai hontai = shiharaiIraiDao.find(denpyouId);
		List<ShiharaiIraiMeisai> 	meisaiList = shiharaiIraiMeisaiDao.loadByDenpyouId(denpyouId);
		String hosoku = hontai.hosoku;
		var denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.SIHARAIIRAI);
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSettingDao.findByDate(null).shiireZeigakuAnbunFlg;
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;	
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String yoteibiTitle = "";
		String yoteibi = "";
		String torihikisakiTitle = "";
		String torihikisaki = "";
		String shiharaikingakugoukeiTitle = "";
		String shiharaikingakugoukei = "";
		String keijoubi = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";
		String jigyoushaKbnTitle = "";
		String jigyoushaKbn = "";
		String jigyoushaNoTitle = "";
		String jigyoushaNo = "";
		String nyuryokuHoushiki = "";
		
		String hosokuTitle = "";
		
		String torihikiTitle = "";
		String kamokuTitle = "";
		String tekiyouTitle = "";
		String futanbumonTitle = "";
		String kazeiTitle = "";
		String shiharaikingakuTitle = "";
		String zeiritsuTitle = "";
		String bunriKbnTitle = "";
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu); 
		// インボイスフラグ（インボイス対応前伝票時のみ「インボイス対応前」出力
		commonXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		//予定日
		if (ks.yoteibi.getPdfHyoujiFlg()) {
			yoteibiTitle = ks.yoteibi.getName();
		}
		yoteibi = EteamXlsFmt.formatDate(hontai.yoteibi);
		//計上日(請求書払いの計上日は記載固定)
		keijoubi = EteamXlsFmt.formatDate(hontai.keijoubi);
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiTitle = ks.torihikisaki.getName();
			torihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.torihikisakiCd, hontai.torihikisakiNameRyakushiki," ");
			if (! isEmpty(hontai.ichigensakiTorihikisakiName))
			{
				torihikisaki += "　" + hontai.ichigensakiTorihikisakiName;
			}
		} 
		// 事業者区分
		if("1".equals(invoiceStartFlg) && isInvoice && ks.jigyoushaKbn.getPdfHyoujiFlg())
		{
			jigyoushaKbnTitle = ks.jigyoushaKbn.getName();
			jigyoushaKbn = "1".equals(hontai.jigyoushaKbn) ? "免税80%" : "通常課税";
		} 
		// 事業者No
		if(ks.jigyoushaNo.getPdfHyoujiFlg() && !hontai.jigyoushaNo.isEmpty())
		{
			jigyoushaNoTitle = ks.jigyoushaNo.getName();
			jigyoushaNo = hontai.jigyoushaNo;
		}
		// 入力方式(0：税込入力　1：税抜入力)
		if("1".equals(invoiceStartFlg) && isInvoice && ks.nyuryokuHoushiki.getPdfHyoujiFlg())
		{
			nyuryokuHoushiki = hontai.nyuryokuHoushiki.equals("0") ? "税込入力" : "税抜入力";
		}
		
		//支払日(支払日は記載固定)
		shiharaibiTitle = "支払日";
		shiharaibi = EteamXlsFmt.formatDate(hontai.shiharaibi);
		
		//excelに各項目出力
		excel.write("yoteibi_title", yoteibiTitle);
		excel.write("yoteibi", yoteibi);
		excel.write("torihikisaki_title", torihikisakiTitle);
		excel.write("torihikisaki", torihikisaki);
		excel.write("keijoubi", keijoubi);
		excel.write("shiharaibi_title", shiharaibiTitle);
		excel.write("shiharaibi", shiharaibi);
		excel.write("jigyousha_kbn", jigyoushaKbn);
		excel.write("jigyousha_kbn_title", jigyoushaKbnTitle);
		excel.write("jigyousha_no_title", jigyoushaNoTitle);
		excel.write("jigyousha_no", jigyoushaNo);
		excel.write("nyuryoku_houshiki", nyuryokuHoushiki);

		//■補足
		if(ks.hosoku.getPdfHyoujiFlg()){
			hosokuTitle = ks.hosoku.getName();
			excel.write("hosoku_title","■" + hosokuTitle);
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
		
		//支払金額合計
		shiharaikingakugoukeiTitle = ks.sashihikiShiharaiGaku.getName();
		shiharaikingakugoukei = EteamXlsFmt.formatMoney(hontai.sashihikiShiharai);
		excel.write("shiharaikingakugoukei_title", shiharaikingakugoukeiTitle);
		excel.write("shiharai_kingaku_goukei", shiharaikingakugoukei);
		
		if(isInvoice) {
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

			for (int i = 0; i < meisaiList.size(); i++) {
				var meisai = meisaiList.get(i);
				String zeiritsucheck = EteamXlsFmt.formatMoney(meisai.zeiritsu);
				BigDecimal shiharaikingaku1 = meisai.shiharaiKingaku == null ? BigDecimal.ZERO : meisai.shiharaiKingaku;
				BigDecimal zeinukikingaku = meisai.zeinukiKingaku == null ? BigDecimal.ZERO : meisai.zeinukiKingaku;
				BigDecimal shouhizeigaku1 = meisai.shouhizeigaku == null ? BigDecimal.ZERO : meisai.shouhizeigaku;

				if(zeiritsucheck.equals("8")) {
					shiharaikingaku8Sum = shiharaikingaku8Sum.add(shiharaikingaku1);
					zeinukikingaku8Sum = zeinukikingaku8Sum.add(zeinukikingaku);
					shouhizeigaku8Sum = shouhizeigaku8Sum.add(shouhizeigaku1);
				}else if(zeiritsucheck.equals("10")) {
					shiharaikingaku10Sum = shiharaikingaku10Sum.add(shiharaikingaku1);
					zeinukikingaku10Sum = zeinukikingaku10Sum.add(zeinukikingaku);
					shouhizeigaku10Sum = shouhizeigaku10Sum.add(shouhizeigaku1);
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

			// インボイス関連処理
			super.getAndPrintInvoiceZei("A013", denpyouId, excel);
		} else {
			excel.hideRow(excel.getCell("shiharai_kingaku_10_title").getRow());
			excel.hideRow(excel.getCell("shiharai_kingaku_8_title").getRow());
			excel.hideRow(excel.getCell("shouhizeigaku_shousai").getRow());
			excel.hideRow(excel.getCell("karibarai_shouhizei").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("kariuke_zeigaku_8_percent_tsumiage_title").getRow());
			excel.hideRow(excel.getCell("kariuke_zeigaku_8_percent_warimodoshi_title").getRow());
		}
		
		//■明細
		//取引
		if(ks.torihiki.getPdfHyoujiFlg()) torihikiTitle = ks.torihiki.getName();
		//科目
		if(ks.kamoku.getPdfHyoujiFlg()) kamokuTitle = ks.kamoku.getName();
		if(ks.kamokuEdaban.getPdfHyoujiFlg())
			kamokuTitle += (0==kamokuTitle.length())? ks.kamokuEdaban.getName() : "\n" + ks.kamokuEdaban.getName();
		//摘要
		if(ks.tekiyou.getPdfHyoujiFlg()) tekiyouTitle = ks.tekiyou.getName();
		//負担部門(＋プロジェクト+segment)
		//高さ調整：１行１５文字以内で計算。
		List<String> futanbumonTitleList = new ArrayList<>();
		if(ks.futanBumon.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.futanBumon.getName(), 14));
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.project.getName(), 14));
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.segment.getName(), 14));
		futanbumonTitle = String.join("\n", futanbumonTitleList);
		//高さ調整：１行７文字以内で計算。
		String[] kamokuArr  	= kamokuTitle.split("\n");
		int  kamokuLength = 0;
		for (int i = 0; i < kamokuArr.length; i++) {
			kamokuLength += excel.lineCount(kamokuArr[i], 14, 1);
		}
		String[] futanbumonArr = futanbumonTitle.split("\n");
		int  futanbumonLength = 0;
		for (int i = 0; i < futanbumonArr.length; i++) {
			futanbumonLength += excel.lineCount(futanbumonArr[i], 14, 1);
		}
		int kazeiLineNum = excel.lineCount(kazeiTitle, 4, 2);
		
		//分離区分＋仕入区分
		List<String> bunriKbnTitleList = new ArrayList<>();
		if(ks.bunriKbn.getPdfHyoujiFlg()) {
			bunriKbnTitleList.addAll(excel.splitLine(ks.bunriKbn.getName(), 14));
		}
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
			bunriKbnTitleList.addAll(excel.splitLine(ks.shiireKbn.getName(), 14));
		}
		bunriKbnTitle = String.join("\n", bunriKbnTitleList);
		String[] bunriKbnArr = bunriKbnTitle.split("\n");
		int bunriKbnLength = 0;
		for (int i = 0; i < bunriKbnArr.length; i++) {
			bunriKbnLength += excel.lineCount(bunriKbnArr[i], 14, 1);
		}
		
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()) {
			kazeiTitle = ks.kazeiKbn.getName();
		}
		//税率
		if(ks.zeiritsu.getPdfHyoujiFlg()) zeiritsuTitle = ks.zeiritsu.getName();
		//金額
		if(ks.shiharaiKingaku.getPdfHyoujiFlg()){
			shiharaikingakuTitle = ks.shiharaiKingaku.getName();
		}
		//税抜金額
		if(ks.zeinukiKingaku.getPdfHyoujiFlg() && isInvoice){
			shiharaikingakuTitle += (shiharaikingakuTitle.equals("") ? "" : "\n") + ks.zeinukiKingaku.getName();
		}
		//消費税額
		if(ks.shouhizeigaku.getPdfHyoujiFlg() && isInvoice){
			shiharaikingakuTitle += (shiharaikingakuTitle.equals("") ? "" : "\n") + ks.shouhizeigaku.getName();
		}
		String[] kingakuArr = shiharaikingakuTitle.split("\n");
		int kingakuLength = 0;
		for (int i = 0; i < kingakuArr.length; i++) {
			kingakuLength += excel.lineCount(kingakuArr[i], 14, 1);
		}
		
		// 高さ調整
		excel.setHeight(meisaikoumokuRow, wfXlsLogic.findMaxInt(kamokuLength, futanbumonLength, kazeiLineNum, kingakuLength, bunriKbnLength), 1);
		
		excel.write("torihiki_title", torihikiTitle);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("tekiyou_title", tekiyouTitle);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("kazei_title", kazeiTitle);
		excel.write("shiharaikingaku_title", shiharaikingakuTitle);
		excel.write("bunri_title", bunriKbnTitle);
		if(ks.zeiritsu.getPdfHyoujiFlg()) {
			excel.write("zeiritsu_title", zeiritsuTitle);
		} else {
			excel.write("zeiritsu_title", "");
		}
		
		//明細テンプレート行を明細件数分コピー
		if (1 < meisaiList.size()) {
			excel.copyRow(meisaiRow, meisaiRow + 1, 1, meisaiList.size() - 1, true);
		}
		
		//1行ずつ書き込み
		int row = 0; //出力相対位置（行）
		for (int i = 0; i < meisaiList.size(); i++) {
			ShiharaiIraiMeisai meisai = meisaiList.get(i);
			//注記
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, hontai.map, meisai.map, "0");
			String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
			
			String torihiki     		= "";
			String kamoku       		= "";
			String kamokuEdaban = "";
			String tekiyou = "";
			String futanBumonName = "";
			String shiharaikingaku = "";
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", meisai.kariKazeiKbn);
			String bunriKbn = "";
			
			if(kazei.isEmpty()) {
				//支払依頼申請専用の課税区分(課込仕入・非課税仕入)のコードも参照
				kazei = sysLogic.findNaibuCdName("kazei_kbn_shiharaiirai", meisai.kariKazeiKbn);
			}
			String zeiritsu = EteamXlsFmt.formatZeiritsu(meisai.zeiritsu, meisai.keigenZeiritsuKbn);
			var kazeiInfo = naibuCdSettingDao.find("kazei_kbn", meisai.kariKazeiKbn);
			String kazeiKbnGroup = (null != kazeiInfo)? kazeiInfo.option1 : "";
			boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;

			//No
			excel.write("meisai_no", 0, row, Integer.toString(i+1));
			//取引
			if(ks.torihiki.getPdfHyoujiFlg()){
				torihiki = ks.torihiki.getCodeSeigyoValue(Integer.toString(meisai.shiwakeEdano), meisai.torihikiName, "\n");
				excel.write("torihiki", 0, row, torihiki);
			}
			
			//科目
			kamoku       = ks.kamoku.getCodeSeigyoValue(meisai.kariKamokuCd, meisai.kariKamokuName, "\n");
			kamokuEdaban = ks.kamokuEdaban.getCodeSeigyoValue(meisai.kariKamokuEdabanCd,meisai.kariKamokuEdabanName, "\n");
			StringBuffer kamokuBuf = new StringBuffer();
			if(ks.kamoku.getPdfHyoujiFlg()) kamokuBuf.append(kamoku);
			if(ks.kamokuEdaban.getPdfHyoujiFlg()){
				if(0!=kamokuBuf.length()) kamokuBuf.append("\n");
				kamokuBuf.append(kamokuEdaban);
			}
			kamoku = kamokuBuf.toString();
			excel.write("kamoku", 0, row, kamoku);
			//摘要
			if(ks.tekiyou.getPdfHyoujiFlg()){
				tekiyou = meisai.tekiyou + chuuki;
				excel.write("meisai_tekiyou", 0, row, tekiyou);
			}
			//負担部門(＋プロジェクト+segment)
			List<String> futanbumonNameList = new ArrayList<>();
			if(ks.futanBumon.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.futanBumon.getCodeSeigyoValue(meisai.kariFutanBumonCd, meisai.kariFutanBumonName, "\n"));
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.project.getCodeSeigyoValue(meisai.projectCd, meisai.projectName,"\n"));
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.segment.getCodeSeigyoValue(meisai.segmentCd, meisai.segmentNameRyakushiki,"\n"));
			futanBumonName = String.join("\n", futanbumonNameList);
			excel.write("meisai_futan_bumon", 0, row, futanBumonName);
			//課税区分
			if(ks.kazeiKbn.getPdfHyoujiFlg()){
				excel.write("kazei", 0, row, kazei == null ? "" : kazei);
			}
			//税率
			if(ks.zeiritsu.getPdfHyoujiFlg() && zeiritsuHyoujiFlg){
				excel.write("zeiritsu", 0, row, zeiritsu);
			}
			//分離区分（＋仕入区分）
			List<String> bunriKbnList = new ArrayList<>();
			if(ks.bunriKbn.getPdfHyoujiFlg()) {
				if (!(StringUtils.isEmpty(meisai.bunriKbn))) {
					bunriKbnList.add(naibuCdSettingDao.find("bunri_kbn",meisai.bunriKbn).name);
				}
			}
			if( ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
				if (!StringUtils.isEmpty(meisai.kariShiireKbn)) {
					bunriKbnList.add(naibuCdSettingDao.find("shiire_kbn",meisai.kariShiireKbn).name);
				}
			}
			bunriKbn = String.join("\n", bunriKbnList);
			excel.write("bunri", 0, row, bunriKbn);
			//支払金額
			if(ks.shiharaiKingaku.getPdfHyoujiFlg()){
				shiharaikingaku = EteamXlsFmt.formatMoney(meisai.shiharaiKingaku);
			}
			//税抜金額
			if(ks.zeinukiKingaku.getPdfHyoujiFlg() && isInvoice){
				shiharaikingaku += (shiharaikingaku.equals("") ? "" : "\n") + EteamXlsFmt.formatMoney(meisai.zeinukiKingaku);
			}
			//消費税額
			if(ks.shouhizeigaku.getPdfHyoujiFlg() && isInvoice){
				shiharaikingaku += (shiharaikingaku.equals("") ? "" : "\n") + EteamXlsFmt.formatMoney(meisai.shouhizeigaku);
			}
			excel.write("meisai_shiharai_kingaku", 0, row, shiharaikingaku);

			//高さ調整：明細の取引名、科目、摘要、負担部門を1行7文字で計算。
			//デフォルト２行だけどそれ以上の行数になるなら高さ変える。
			int torihikiLineNum = excel.lineCount(torihiki, 14, 2);
			int kamokuLineNum = excel.lineCount(kamoku, 14, 2);
			int futanbumonLineNum = excel.lineCount(futanBumonName, 14, 2);
			int tekiyouLineNum = excel.lineCount(tekiyou, 14, 2);
			int kakuchouLineNum = wfXlsLogic.findMaxInt(torihikiLineNum, kamokuLineNum, futanbumonLineNum, tekiyouLineNum);
			excel.setHeight(meisaiRow+row,  kakuchouLineNum, 2);
			
			row+=1;
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
