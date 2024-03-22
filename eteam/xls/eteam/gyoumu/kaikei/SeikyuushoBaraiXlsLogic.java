package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.util.CollectionUtil;
import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.SeikyuushobaraiAbstractDao;
import eteam.database.abstractdao.SeikyuushobaraiMeisaiAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.SeikyuushobaraiDao;
import eteam.database.dao.SeikyuushobaraiMeisaiDao;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.Seikyuushobarai;
import eteam.database.dto.SeikyuushobaraiMeisai;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 請求書払い申請Logic
 */
public class SeikyuushoBaraiXlsLogic extends KaikeiCommonXlsLogic {
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SEIKYUUSHO_BARAI);
	
	/**
	 * @param denpyouId 伝票ID
	 * @param out 出力先
	 */ 
	public void makeExcel(String denpyouId, OutputStream out) {

		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		KaikeiCommonLogic kclogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		SeikyuushobaraiMeisaiAbstractDao seikyuushobaraiMeisaiDao = EteamContainer.getComponent(SeikyuushobaraiMeisaiDao.class, connection);
		SeikyuushobaraiAbstractDao seikyuushobaraiDao = EteamContainer.getComponent(SeikyuushobaraiDao.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		NaibuCdSettingAbstractDao naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		KaikeiCommonXlsLogic commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		KiShouhizeiSettingDao kiShouhizeiSetting = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream(this.getExcelFileName()), out);
		WritableWorkbook book = excel.getBook();
		Cell meisaiCell = book.findCellByName("meisai_no");
		int meisaiRow = meisaiCell.getRow();
		int meisaiCol = meisaiCell.getColumn();
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
		Seikyuushobarai hontai = seikyuushobaraiDao.find(denpyouId); 
		List<SeikyuushobaraiMeisai> meisaiList = seikyuushobaraiMeisaiDao.loadByDenpyouId(denpyouId);
		List<GMap>  meisaiListG = kaikeiLogic.loadSeikyuushobaraiMeisai(denpyouId);
		String hosoku = hontai.hosoku;
		var denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI);
		String isZeiNyuryoku = hontai.nyuryokuHoushiki;
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		boolean isBeforeInvoice = "1".equals(invoiceStartFlg);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSetting.findByDate(null).shiireZeigakuAnbunFlg;
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;	
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String shiharaikigenTitle = "";
		String shiharaikigen = "";
		String shiharaikingakugoukeiTitle = "";
		String shiharaikingakugoukei = "";
		String shiharaikingaku10Title = "";
		String shiharaikingaku8Title = "";
		String zeinukikingaku10Title = "";
		String zeinukikingaku8Title = "";
		String shouhizeigaku10Title = "";
		String shouhizeigaku8Title = "";
		String keijoubi = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";
		
		String hosokuTitle = "";
		
		String torihikiTitle = "";
		String kamokuTitle = "";
		String tekiyouTitle = "";
		String futanbumonTitle = "";
		String kazeiTitle = "";
		String bunriTitle = "";
		String shiharaikingakuTitle = "";
		
		//計算用
		BigDecimal shiharaikingaku8Sum = BigDecimal.ZERO;
		BigDecimal zeinukikingaku8Sum = BigDecimal.ZERO;
		BigDecimal shouhizeigaku8Sum = BigDecimal.ZERO; 
		BigDecimal shiharaikingaku10Sum = BigDecimal.ZERO;
		BigDecimal zeinukikingaku10Sum = BigDecimal.ZERO;
		BigDecimal shouhizeigaku10Sum = BigDecimal.ZERO;
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu);
		//インボイス処理開始後、かつインボイスフラグが「1:(インボイス対応前)」の場合
		commonXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		// 入力方式(0：税抜入力・1：税込入力)
		if (isBeforeInvoice && isInvoice && ks.nyuryokuHoushiki.getPdfHyoujiFlg()) {
			if ("0".equals(isZeiNyuryoku)) {
				excel.write("nyuryoku_housiki", "税込入力");
			} else {
				excel.write("nyuryoku_housiki", "税抜入力");
			} 
		}
		//支払期限
		if(ks.shiharaiKigen.getPdfHyoujiFlg()){
			shiharaikigenTitle = ks.shiharaiKigen.getName();
			shiharaikigen = EteamXlsFmt.formatDate(hontai.shiharaiKigen);
		}
		//計上日(請求書払いの計上日は記載固定)
		keijoubi = EteamXlsFmt.formatDate(hontai.keijoubi);
		//支払金額合計
		if(ks.shiharaiKingakuGoukei.getPdfHyoujiFlg()){
			shiharaikingakugoukeiTitle = ks.shiharaiKingakuGoukei.getName();
			shiharaikingakugoukei = EteamXlsFmt.formatMoney(hontai.shiharaiKingakuGoukei);
		}
		
		//excelに各項目出力
		excel.write("shiharaikigen_title", shiharaikigenTitle);
		excel.write("shiharaikigen", shiharaikigen);
		excel.write("shiharai_kingaku_goukei_title", shiharaikingakugoukeiTitle);
		excel.write("shiharai_kingaku_goukei", shiharaikingakugoukei);

		if (isInvoice && ks.shiharaiKingakuGoukei.getPdfHyoujiFlg()) {
			//支払金額（税額別）
			shiharaikingaku10Title = ks.shiharaiKingakuGoukei10Percent.getName();
			shiharaikingaku8Title = ks.shiharaiKingakuGoukei8Percent.getName();
			zeinukikingaku10Title = ks.zeinukiKingaku10Percent.getName();
			zeinukikingaku8Title = ks.zeinukiKingaku8Percent.getName();
			shouhizeigaku10Title = ks.shouhizeigaku10Percent.getName();
			shouhizeigaku8Title = ks.shouhizeigaku8Percent.getName();

			for (int i = 0; i < meisaiList.size(); i++) {
				var meisai = meisaiList.get(i);
				String zeiritsucheck = EteamXlsFmt.formatMoney(meisai.zeiritsu);
				BigDecimal shiharaikingaku1 = meisai.shiharaiKingaku == null ? BigDecimal.ZERO : meisai.shiharaiKingaku;
				BigDecimal zeinukikingaku  	= meisai.hontaiKingaku == null ? BigDecimal.ZERO : meisai.hontaiKingaku;
				BigDecimal shouhizeigaku1 = meisai.shouhizeigaku == null ? BigDecimal.ZERO : meisai.shouhizeigaku;

				if(zeiritsucheck.equals("8")){
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
		} else {
			excel.hideRow(excel.getCell("shiharai_kingaku_10_title").getRow());
			excel.hideRow(excel.getCell("shiharai_kingaku_8_title").getRow());
		}
		
		
		// 消費税額詳細
		super.getAndPrintInvoiceZei("A003", denpyouId, excel);
		
		//支払日(支払日は記載固定)
		shiharaibiTitle = "支払日";
		shiharaibi = EteamXlsFmt.formatDate(hontai.shiharaibi);
		
		excel.write("keijoubi", keijoubi);
		excel.write("shiharaibi_title", shiharaibiTitle);
		excel.write("shiharaibi", shiharaibi);

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
		
		//■明細
		//取引
		if(ks.torihiki.getPdfHyoujiFlg()) {
			torihikiTitle = ks.torihiki.getName();
		}
		//科目
		if(ks.kamoku.getPdfHyoujiFlg()) {
			kamokuTitle = ks.kamoku.getName();
		}
		if(ks.kamokuEdaban.getPdfHyoujiFlg()) {
			kamokuTitle += (0==kamokuTitle.length())? ks.kamokuEdaban.getName() : "\n" + ks.kamokuEdaban.getName();
		}
		//摘要
		if(ks.tekiyou.getPdfHyoujiFlg()) {
			tekiyouTitle = ks.tekiyou.getName();
		}
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()) {
			kazeiTitle = ks.kazeiKbn.getName();
		}
		//分離区分＋仕入区分
		List<String> bunriKbnTitleList = new ArrayList<>();
		if(ks.bunriKbn.getPdfHyoujiFlg()) {
			bunriKbnTitleList.addAll(excel.splitLine(ks.bunriKbn.getName(), 14));
		}
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
			bunriKbnTitleList.addAll(excel.splitLine(ks.shiireKbn.getName(), 14));
		}
		bunriTitle = String.join("\n", bunriKbnTitleList);
		
		//負担部門(＋取引先＋振込先＋プロジェクト+セグメント)
		//高さ調整：１行１５文字以内で計算。
		List<String> futanbumonTitleList = new ArrayList<>();
		if(ks.futanBumon.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks.futanBumon.getName(), 30));
		}
		if(ks.torihikisaki.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks.torihikisaki.getName(), 30));
		}
		 if(hontai.invoiceDenpyou.equals("0")){
			 futanbumonTitleList.addAll(excel.splitLine(ks.jigyoushaKbn.getName(), 30)); 
		 }
		if(ks.furikomisakiJouhou.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks.furikomisakiJouhou.getName(), 30));
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks.project.getName(), 30));
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()) {
			futanbumonTitleList.addAll(excel.splitLine(ks.segment.getName(), 30));
		}
		futanbumonTitle = String.join("\n", futanbumonTitleList);
		//支払金額
		if(ks.shiharaiKingaku.getPdfHyoujiFlg()){
			List<String> shiharaikingakuTitleList1 = new ArrayList<>();
			shiharaikingakuTitleList1.addAll(excel.splitLine(ks.shiharaiKingaku.getName(), 30));
			if(hontai.invoiceDenpyou.equals("0")) {
				shiharaikingakuTitleList1.addAll(excel.splitLine("税抜金額", 30));
				shiharaikingakuTitleList1.addAll(excel.splitLine("消費税額", 30));
			}
			shiharaikingakuTitle = String.join("\n", shiharaikingakuTitleList1);
		}
		//高さ調整：１行７文字以内で計算。
		String[] kamokuArr = kamokuTitle.split("\n");
		int kamokuLength = 0;
		for (int i = 0; i < kamokuArr.length; i++) {
			kamokuLength += excel.lineCount(kamokuArr[i], 14, 1);
		}
		String[] futanbumonArr = futanbumonTitle.split("\n");
		int futanbumonLength = 0;
		for (int i = 0; i < futanbumonArr.length; i++) {
			futanbumonLength += excel.lineCount(futanbumonArr[i], 14, 1);
		}
		String[] shiharaikingakuArr = shiharaikingakuTitle.split("\n");
		int shiharaikingakuLength = 0;
		for (int i = 0; i < shiharaikingakuArr.length; i++) {
			shiharaikingakuLength += excel.lineCount(shiharaikingakuArr[i], 14, 1);
		}
		String[] bunriKbnArr = bunriTitle.split("\n");
		int bunriKbnLength = 0;
		for (int i = 0; i < bunriKbnArr.length; i++) {
			bunriKbnLength += excel.lineCount(bunriKbnArr[i], 14, 1);
		}
		int kazeiLineNum =	excel.lineCount(kazeiTitle, 4, 2);
		if (kamokuLength >= 2 || futanbumonLength >= 2 ||  kazeiLineNum >= 2 || shiharaikingakuLength >= 2 || bunriKbnLength >= 2) {
			excel.setHeight(meisaikoumokuRow, wfXlsLogic.findMaxInt(kamokuLength, futanbumonLength, kazeiLineNum, shiharaikingakuLength, bunriKbnLength), 1);
		}
		
		excel.write("torihiki_title", torihikiTitle);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("tekiyou_title", tekiyouTitle);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("kazei_title", kazeiTitle);
		excel.write("bunri_title", bunriTitle);
		excel.write("shiharaikingaku_title", shiharaikingakuTitle);
		if(!ks.zeiritsu.getPdfHyoujiFlg()) {
			excel.write("zeiritsu_title", "");
		}
		if(ks.kousaihiShousai.getPdfHyoujiFlg()) {
			excel.write("kousaihi_title", ks.kousaihiShousai.getName());
		} else {
			excel.hideRow(excel.getCell("kousaihi_title").getRow());
		}
		
		//明細テンプレート行を明細件数分コピー
		if (1 < meisaiList.size()) {
			excel.copyRow(meisaiRow, meisaiRow + 2, 2, meisaiList.size() - 1, true);
		}
		
		//先に警告リストだけ取得
		List<GMap> keikokuList = new ArrayList<GMap>();
		if(meisaiList.size() > 0) {
			String[] shiwakeEdanoCd  	= CollectionUtil.toArrayStr(meisaiListG, "shiwake_edano");
			String[] kousaihiHitoriKingaku = CollectionUtil.toArrayStr(meisaiListG, "kousaihi_hitori_kingaku");
			String[] kaigaiFlg  	= CollectionUtil.makeArrayStr("0", meisaiList.size());
			keikokuList = kclogic.checkKousaihiKijun(DENPYOU_KBN.SEIKYUUSHO_BARAI, shiwakeEdanoCd, kousaihiHitoriKingaku, kaigaiFlg, kclogic.KOUSAI_ERROR_CD_KEIKOKU);
		}
		
		//1行ずつ書き込み
		int row = 0; //出力相対位置（行）
		for (int i = 0; i < meisaiList.size(); i++) {
			SeikyuushobaraiMeisai meisai = meisaiList.get(i);
			//注記
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.SEIKYUUSHO_BARAI, hontai.map, meisai.map, "0");
			String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
			
			String torihiki     = "";
			String kamoku       = "";
			String kamokuEdaban = "";
			String tekiyou = "";
			String futanBumonName = "";
			String shiharaikingaku = "";
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", meisai.kariKazeiKbn);
			String zeiritsu = EteamXlsFmt.formatZeiritsu(meisai.zeiritsu, meisai.keigenZeiritsuKbn);
			var kazeiInfo = naibuCdSettingDao.find("kazei_kbn", meisai.kariKazeiKbn);
			String kazeiKbnGroup = (null != kazeiInfo)? kazeiInfo.option1 : "";
			boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;
			String bunri = sysLogic.findNaibuCdName("bunri_kbn", meisai.bunriKbn);

			//No
			excel.write("meisai_no", 0, row, Integer.toString(i+1));
			//取引
			if(ks.torihiki.getPdfHyoujiFlg()){
				torihiki = ks.torihiki.getCodeSeigyoValue(Integer.toString(meisai.shiwakeEdano), meisai.torihikiName, "\n");
				excel.write("torihiki", 0, row, torihiki);
			}
			
			//科目
			kamoku       = ks.kamoku.getCodeSeigyoValue(meisai.kariKamokuCd,meisai.kariKamokuName, "\n");
			kamokuEdaban = ks.kamokuEdaban.getCodeSeigyoValue(meisai.kariKamokuEdabanCd, meisai.kariKamokuEdabanName, "\n");
			StringBuffer kamokuBuf = new StringBuffer();
			if(ks.kamoku.getPdfHyoujiFlg()) {
				kamokuBuf.append(kamoku);
			}
			if(ks.kamokuEdaban.getPdfHyoujiFlg()){
				if(0!=kamokuBuf.length()) {
					kamokuBuf.append("\n");
				}
				kamokuBuf.append(kamokuEdaban);
			}
			excel.write("kamoku", 0, row, kamokuBuf.toString());
			//摘要
			if(ks.tekiyou.getPdfHyoujiFlg()){
				tekiyou = meisai.tekiyou + chuuki;
				excel.write("meisai_tekiyou", 0, row, tekiyou);
			}
			//負担部門(＋取引先＋振込先＋プロジェクト+セグメント)
			List<String> futanbumonNameList = new ArrayList<>();
			if(ks.futanBumon.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks.futanBumon.getCodeSeigyoValue(meisai.kariFutanBumonCd,meisai.kariFutanBumonName, "\n"));
			}
			if(ks.torihikisaki.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks.torihikisaki.getCodeSeigyoValue(meisai.torihikisakiCd, meisai.torihikisakiNameRyakushiki,"\n"));
			}
			if(hontai.invoiceDenpyou.equals("0") && ks.jigyoushaKbn.getPdfHyoujiFlg()) {
				futanbumonNameList.add(sysLogic.findNaibuCdName("jigyousha_kbn", meisai.jigyoushaKbn));
			}
			if(ks.furikomisakiJouhou.getPdfHyoujiFlg()) {
				futanbumonNameList.add(meisai.furikomisakiJouhou);
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks.project.getCodeSeigyoValue(meisai.projectCd, meisai.projectName,"\n"));
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()) {
				futanbumonNameList.add(ks.segment.getCodeSeigyoValue(meisai.segmentCd, meisai.segmentNameRyakushiki,"\n"));
			}
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
				if (!(StringUtils.isEmpty(meisai.kariShiireKbn))) {
					bunriKbnList.add(naibuCdSettingDao.find("shiire_kbn",meisai.kariShiireKbn).name);
				}
			}
			bunri = String.join("\n", bunriKbnList);
			excel.write("bunri", 0, row, bunri);
			//支払金額
			List<String> shiharaikingakuList = new ArrayList<>();
			if(ks.shiharaiKingaku.getPdfHyoujiFlg()){
				shiharaikingakuList.add(EteamXlsFmt.formatMoney(meisai.shiharaiKingaku));
			}
			if(hontai.invoiceDenpyou.equals("0")) {
				shiharaikingakuList.add(EteamXlsFmt.formatMoney(meisai.hontaiKingaku));
				shiharaikingakuList.add(EteamXlsFmt.formatMoney(meisai.shouhizeigaku));
			}
			shiharaikingaku = String.join("\n", shiharaikingakuList);
			excel.write("meisai_shiharai_kingaku", 0, row, shiharaikingaku);

			//高さ調整：明細の取引名、科目、摘要、負担部門を1行7文字で計算。
			//デフォルト２行だけどそれ以上の行数になるなら高さ変える。
			int torihikiLineNum =	excel.lineCount(torihiki, 14, 2);
			int kamokuLineNum =	excel.lineCount(kamokuBuf.toString(), 14, 2);
			int futanbumonLineNum = excel.lineCount(futanBumonName, 14, 2);
			int tekiyouLineNum = excel.lineCount(tekiyou, 14, 2);
			int shiharaikingakuLineNum = excel.lineCount(shiharaikingaku, 14, 2);
			int kakuchouLineNum = wfXlsLogic.findMaxInt(torihikiLineNum, kamokuLineNum, futanbumonLineNum, tekiyouLineNum, shiharaikingakuLineNum);
			excel.setHeight(meisaiRow+row,  kakuchouLineNum, 2);
			
			//交際費詳細
			if(ks.kousaihiShousai.getPdfHyoujiFlg() && "1".equals(meisai.kousaihiShousaiHyoujiFlg)){
				String kousaihi = meisai.kousaihiShousai;
				if( meisai.kousaihiNinzuu != null) {
					String kousaihiNinzuu = meisai.kousaihiNinzuu == null ? "" : meisai.kousaihiNinzuu + "名";
					excel.write("kousaihi_ninzuu", 0, row, kousaihiNinzuu);
				}
				if( meisai.kousaihiHitoriKingaku != null) {
					String kousaihiHitoriKingaku = meisai.kousaihiHitoriKingaku == null ? "" : (EteamXlsFmt.formatMoney(meisai.kousaihiHitoriKingaku)) + "円";
					excel.write("kousaihi_hitori_kingaku", 0, row, kousaihiHitoriKingaku);
					
					//超過・不足分警告メッセージを追加
					for(GMap res : keikokuList) {
						if(res.get("index") != null && res.get("index").equals(i)) {
							kousaihi = kousaihi.isEmpty() ? res.get("errMsg") : kousaihi + "\r\n" + res.get("errMsg");
							break;
						}
					}
				}
				excel.write("kousaihi", 0, row, kousaihi);
				
				int kousaihiLineNum = excel.lineCount(kousaihi, 52, 2);
				excel.setHeight(meisaiRow+row+1,  kousaihiLineNum, 2);
			} else {
				excel.hideRow(meisaiRow+row+1);
				//行番号の枠線を再セット
				excel.encloseInBorder(meisaiRow+row, meisaiCol, 1);
			}
			
			row+=2;
		}
		
		// 追加項目分
		this.setAdditionalProperties(excel, hontai, meisaiList);
		
		// ---------------------------------------
		//共通部分
		// ---------------------------------------
		wfXlsLogic.makeExcel(excel, denpyouId, false);
		
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
	
	/**
	 * カスタマイズ用。Excelファイル名を取得する。
	 * @return Excelファイル名
	 */
	protected String getExcelFileName() {
		return "template_seikyuusyo.xls";
	}
	
	/**
	 * カスタマイズ用。追加項目の設定。
	 * @param excel excel
	 * @param hontai 本体Dto
	 * @param meisaiList 明細Dtoのリスト
	 */
	protected void setAdditionalProperties(EteamXls excel, Seikyuushobarai hontai, List<SeikyuushobaraiMeisai> meisaiList) {
		
	}
}
