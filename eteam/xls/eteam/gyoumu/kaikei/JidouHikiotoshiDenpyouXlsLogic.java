package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
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
import eteam.database.abstractdao.JidouhikiotoshiAbstractDao;
import eteam.database.abstractdao.JidouhikiotoshiMeisaiAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.Jidouhikiotoshi;
import eteam.database.dto.JidouhikiotoshiMeisai;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 自動引落伝票Logic
 */
public class JidouHikiotoshiDenpyouXlsLogic extends EteamAbstractLogic {
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU);
	
	//消費税の定数
	//TODO グローバルにしたほうがいい気がする
	/** 現在の消費税率 */
	String shouhizeiNow = "10";
	/** 現在の軽減税率 */
	String shouhizeiKeigenNow = "8";

	/**
	 * @param denpyouId 伝票ID
	 * @param out 出力先
	 */ 
	public void makeExcel(String denpyouId, OutputStream out) {

		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		SystemKanriCategoryLogic   	sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowXlsLogic   	wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		BatchKaikeiCommonLogic   	batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		JidouhikiotoshiMeisaiAbstractDao  	jidouhikiotoshiMeisaiDao = EteamContainer.getComponent(JidouhikiotoshiMeisaiAbstractDao.class, connection);
		JidouhikiotoshiAbstractDao   	jidouhikiotoshiDao = EteamContainer.getComponent(JidouhikiotoshiAbstractDao.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		NaibuCdSettingAbstractDao   	naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		KiShouhizeiSettingDao   	kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		KaikeiCommonXlsLogic   	commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_jidouhikiotoshi.xls"), out);
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
		Jidouhikiotoshi hontai = jidouhikiotoshiDao.find(denpyouId); 
		List<JidouhikiotoshiMeisai> meisaiList = jidouhikiotoshiMeisaiDao.loadByDenpyouId(denpyouId);
		String hosoku = hontai.hosoku;
		var denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU);
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		String isZeiNyuryoku = hontai.nyuryokuHoushiki;
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSettingDao.findByDate(null).shiireZeigakuAnbunFlg;
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		boolean isBeforeInvoice = "1".equals(invoiceStartFlg);
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;		
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String hikiotoshibiTitle   = "";
		String hikiotoshibi   = "";
		String shiharaikingakugoukeiTitle = "";
		String shiharaikingakugoukei   = "";
		String hosokuTitle   = "";
		
		String torihikiTitle = "";
		String kamokuTitle = "";
		String tekiyouTitle = "";
		String futanbumonTitle = "";
		String kazeiTitle = "";
		String shiharaikingakuTitle = "";
		String bunriTitle = "";
		
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
		//インボイス開始処理済みで、かつ旧伝票（インボイス対応前）の場合
		//if(isInvoiceStart && !isInvoice) excel.write("invoice_denpyou_flg", "インボイス対応前");
		commonXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		// 入力方式(0：税抜入力・1：税込入力)
		if (isBeforeInvoice && isInvoice && ks.nyuryokuHoushiki.getPdfHyoujiFlg()) {
			if ("0".equals(isZeiNyuryoku)) {
				excel.write("zeinyuryoku_houshiki", "税込入力");
			} else {
				excel.write("zeinyuryoku_houshiki", "税抜入力");
			} 
		}
		//引落日
		if(ks.hikiotoshibi.getPdfHyoujiFlg()){
			hikiotoshibiTitle = ks.hikiotoshibi.getName();
			hikiotoshibi = EteamXlsFmt.formatDate(hontai.hikiotoshibi);
		}
		//支払金額合計
		if(ks.shiharaiKingakuGoukei.getPdfHyoujiFlg()){
			shiharaikingakugoukeiTitle = ks.shiharaiKingakuGoukei.getName();
			shiharaikingakugoukei = EteamXlsFmt.formatMoney(hontai.shiharaiKingakuGoukei);
			excel.makeBorder("shiharaikingakugoukei_title");
			excel.makeBorder("shiharaikingakugoukei");
		}
		//計上日
		//表示・非表示の判別はsetting_infoを参照
		if(!"3".equals(setting.shiwakeSakuseiHouhouA009())) {
			excel.write("keijoubi", EteamXlsFmt.formatDate(hontai.keijoubi));
		} else {
			excel.write("keijoubi_title", "");
		}
		//インボイス対応 支払金額税抜金額消費税額（＋消費税詳細） 支払金額%・税抜金額%・消費税額%　は支払金額合計の表示と連動
		if(isInvoice && ks.shiharaiKingakuGoukei.getPdfHyoujiFlg()) {
			if(ks.shiharaiKingakuGoukei.getPdfHyoujiFlg()) {
				//タイトル直書きなら不要？
				//　税率は明細リストから取得するしかない
				String shiharai10Title = "支払金額10%";
				String shiharai8Title = "支払金額*8%";
				String zeinuki10Title = "税抜金額10%";
				String zeinuki8Title = "税抜金額*8%";
				String shouhizei10Title = "消費税額10%";
				String shouhizei8Title = "消費税額*8%";

				excel.write("shiharai_kingaku_10_title", shiharai10Title);
				excel.write("shiharai_kingaku_8_title",  shiharai8Title);
				excel.write("zeinuki_kingaku_10_title",  zeinuki10Title);
				excel.write("zeinuki_kingaku_8_title",  zeinuki8Title);
				excel.write("shouhizeigaku_10_title",  shouhizei10Title);
				excel.write("shouhizeigaku_8_title",  shouhizei8Title);
				
				excel.makeBorder("shiharai_kingaku_10_title");
				excel.makeBorder("shiharai_kingaku_8_title");
				excel.makeBorder("shiharai_kingaku_10");
				excel.makeBorder("shiharai_kingaku_8");
				excel.makeBorder("zeinuki_kingaku_10_title");
				excel.makeBorder("zeinuki_kingaku_8_title");
				excel.makeBorder("zeinuki_kingaku_10");
				excel.makeBorder("zeinuki_kingaku_8");
				excel.makeBorder("shouhizeigaku_10_title");
				excel.makeBorder("shouhizeigaku_8_title");
				excel.makeBorder("shouhizeigaku_10");
				excel.makeBorder("shouhizeigaku_8");
			}
		} else {
			excel.hideRow(excel.getCell("shiharai_kingaku_10_title").getRow());
			excel.hideRow(excel.getCell("shiharai_kingaku_8_title").getRow());
			
			excel.hideRow(excel.getCell("shouhizeigakushousai_title").getRow());
			excel.hideRow(excel.getCell("karibaraishouhizei_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_menzei_80_title").getRow());
		}
		
		if(!isInvoice) {
			excel.hideRow(excel.getCell("shouhizeigaku_shousai").getRow());
			excel.hideRow(excel.getCell("karibarai_shouhizei").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_menzei_80_title").getRow());
		}
		
		//excelに各項目出力
		excel.write("hikiotoshibi_title", hikiotoshibiTitle);
		excel.write("hikiotoshibi", hikiotoshibi);
		excel.write("shiharaikingakugoukei_title", shiharaikingakugoukeiTitle);
		excel.write("shiharaikingakugoukei", shiharaikingakugoukei);

		//■補足
		//高さ調整：補足は１行４７文字以内で計算。一行ずつインサート。
		if(ks.hosoku.getPdfHyoujiFlg()) {
			hosokuTitle = ks.hosoku.getName();
			excel.write("hosoku_title","■" + hosokuTitle);
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
		} else {
			excel.hideRow(excel.getCell("hosoku_title").getRow());
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
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()) kazeiTitle = ks.kazeiKbn.getName();
		//負担部門(＋取引先＋プロジェクト)
		List<String> futanbumonTitleList = new ArrayList<>();
		if(ks.futanBumon.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.futanBumon.getName(), 30));
		if(ks.torihikisaki.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.torihikisaki.getName(), 30));
		//事業者区分
		if(isInvoice&&ks.jigyoushaKbn.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.jigyoushaKbn.getName(), 30));
		//プロジェクト
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.project.getName(), 30));
		//セグメント
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg())
			futanbumonTitleList.addAll(excel.splitLine(ks.segment.getName(), 30));
		futanbumonTitle = String.join("\n", futanbumonTitleList);
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
		int kazeiLineNum = excel.lineCount(kazeiTitle, 4, 2);
		
		//支払金額（＋税抜金額＋消費税額）
		List<String> kingakuTitleList = new ArrayList<>();
		if(ks.shiharaiKingaku.getPdfHyoujiFlg()) /*shiharaikingakuTitle = ks.shiharaiKingaku.getName();*/
			kingakuTitleList.addAll(excel.splitLine(ks.shiharaiKingaku.getName(), 30));
		if(isInvoice&&ks.zeinukiKingaku.getPdfHyoujiFlg())
			kingakuTitleList.addAll(excel.splitLine(ks.zeinukiKingaku.getName(), 30));
		if(isInvoice&&ks.shouhizeigaku.getPdfHyoujiFlg())
			kingakuTitleList.addAll(excel.splitLine(ks.shouhizeigaku.getName(), 30));
		shiharaikingakuTitle = String.join("\n", kingakuTitleList);
		String[] shiharaikingakuArr = shiharaikingakuTitle.split("\n");
		int shiharaikingakuLength = 0;
		for (int i = 0; i < shiharaikingakuArr.length; i++) {
			shiharaikingakuLength += excel.lineCount(shiharaikingakuArr[i], 14, 1);
		}
		
		//分離区分＋仕入区分
		List<String> bunriKbnTitleList = new ArrayList<>();
		if(ks.bunriKbn.getPdfHyoujiFlg()) {
			if (!ks.bunriKbn.getName().isEmpty()) {
				bunriKbnTitleList.addAll(excel.splitLine(ks.bunriKbn.getName(), 14));
			}
		}
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
			if (!ks.shiireKbn.getName().isEmpty()) {
				bunriKbnTitleList.addAll(excel.splitLine(ks.shiireKbn.getName(), 14));
			}
		}
		bunriTitle = String.join("\n", bunriKbnTitleList);
		String[] bunriKbnArr = bunriTitle.split("\n");
		int bunriKbnLength = 0;
		for (int i = 0; i < bunriKbnArr.length; i++) {
			bunriKbnLength += excel.lineCount(bunriKbnArr[i], 14, 1);
		}
		
		if (kamokuLength >= 2 || futanbumonLength >= 2 ||  kazeiLineNum >= 2 || shiharaikingakuLength >= 2 || bunriKbnLength >= 2)
			excel.setHeight(meisaikoumokuRow, wfXlsLogic.findMaxInt(kamokuLength, futanbumonLength, kazeiLineNum,shiharaikingakuLength, bunriKbnLength), 1);
		
		//excelにタイトル出力
		excel.write("torihiki_title", torihikiTitle);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("tekiyou_title", tekiyouTitle);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("kazei_title", kazeiTitle);
		excel.write("shiharaikingaku_title", shiharaikingakuTitle);
		excel.write("bunri_title", bunriTitle);
		if(ks.zeiritsu.getPdfHyoujiFlg()  == false) excel.write("zeiritsu_title", "");
		
		//明細テンプレート行を明細件数分コピー
		if (1 < meisaiList.size()) excel.copyRow(meisaiRow, meisaiRow + 1, 1, meisaiList.size() - 1, true);
		//1行ずつ書き込み
		for (int i = 0; i < meisaiList.size(); i++) {
			JidouhikiotoshiMeisai meisai = meisaiList.get(i);
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, hontai.map, meisai.map, "0");
			String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";

			String torihiki     = "";
			String kamoku       = "";
			String kamokuEdaban = "";
			String tekiyou      = "";
			String futanBumonName = "";
			String kingakuName = "";
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", meisai.kariKazeiKbn);
			String zeiritsu = EteamXlsFmt.formatZeiritsu(meisai.zeiritsu,meisai.keigenZeiritsuKbn);
			var kazeiInfo = naibuCdSettingDao.find("kazei_kbn", meisai.kariKazeiKbn);
			String kazeiKbnGroup = (null != kazeiInfo)? kazeiInfo.option1 : "";
			boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false; 
			String bunriKbn  = "";
			
			//No
			excel.write("meisai_no", 0, i, Integer.toString(i+1));
			//取引
			if(ks.torihiki.getPdfHyoujiFlg()){
				torihiki = ks.torihiki.getCodeSeigyoValue(Integer.toString(meisai.shiwakeEdano), meisai.torihikiName, "\n");
				excel.write("torihiki", 0, i, torihiki);
			}
			//科目
			kamoku       = ks.kamoku.getCodeSeigyoValue(meisai.kariKamokuCd,meisai.kariKamokuName, "\n");
			kamokuEdaban = ks.kamokuEdaban.getCodeSeigyoValue(meisai.kariKamokuEdabanCd, meisai.kariKamokuEdabanName, "\n");
			StringBuffer kamokuBuf = new StringBuffer();
			if(ks.kamoku.getPdfHyoujiFlg()) kamokuBuf.append(kamoku);
			if(ks.kamokuEdaban.getPdfHyoujiFlg()){
				if(0!=kamokuBuf.length()) kamokuBuf.append("\n");
				kamokuBuf.append(kamokuEdaban);
			}
			excel.write("kamoku", 0, i, kamokuBuf.toString());
			//摘要
			if(ks.tekiyou.getPdfHyoujiFlg()){
				tekiyou = meisai.tekiyou + chuuki;
				excel.write("meisai_tekiyou", 0, i, tekiyou);
			}
			//負担部門(＋取引先＋事業者区分＋プロジェクト)
			List<String> futanbumonNameList = new ArrayList<>();
			if(ks.futanBumon.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.futanBumon.getCodeSeigyoValue(meisai.kariFutanBumonCd,meisai.kariFutanBumonName, "\n"));
			if(ks.torihikisaki.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.torihikisaki.getCodeSeigyoValue(meisai.torihikisakiCd, meisai.torihikisakiNameRyakushiki,"\n"));
			if(isInvoice && ks.jigyoushaKbn.getPdfHyoujiFlg()) {
				String jigyoushaKbnNum = meisai.jigyoushaKbn;
				String jigyoushaKbnStr = naibuCdSettingDao.find("jigyousha_kbn",jigyoushaKbnNum).name;
				futanbumonNameList.add(jigyoushaKbnStr);
			}
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.project.getCodeSeigyoValue(meisai.projectCd, meisai.projectName,"\n"));
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg())
				futanbumonNameList.add(ks.segment.getCodeSeigyoValue(meisai.segmentCd, meisai.segmentNameRyakushiki,"\n"));
			futanBumonName = String.join("\n", futanbumonNameList);
			excel.write("meisai_futan_bumon", 0, i, futanBumonName);
			//課税区分
			if(ks.kazeiKbn.getPdfHyoujiFlg()) {
				excel.write("kazei", 0, i, kazei == null ? "" : kazei);
			}
			//税率
			if(ks.zeiritsu.getPdfHyoujiFlg() && zeiritsuHyoujiFlg) {
				excel.write("zeiritsu", 0, i, zeiritsu);
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
			bunriKbn = String.join("\n", bunriKbnList);
			excel.write("bunri", 0, i, bunriKbn);
			//支払金額（＋本体金額＋消費税額）
			List<String> kingakuNameList = new ArrayList<>();
			if(ks.shiharaiKingaku.getPdfHyoujiFlg()) {
				kingakuNameList.add(EteamXlsFmt.formatMoney(meisai.shiharaiKingaku));
			}
			if(isInvoice && ks.zeinukiKingaku.getPdfHyoujiFlg()) {
				kingakuNameList.add(EteamXlsFmt.formatMoney(meisai.hontaiKingaku));
			}
			if(isInvoice && ks.shouhizeigaku.getPdfHyoujiFlg()) {
				kingakuNameList.add(EteamXlsFmt.formatMoney(meisai.shouhizeigaku));
			}
			
			kingakuName = String.join("\n", kingakuNameList);
			excel.write("meisai_shiharai_kingaku", 0, i, kingakuName);
			
			String     zeiritsucheck    = EteamXlsFmt.formatMoney(meisai.zeiritsu);
			BigDecimal shiharaikingaku1 = meisai.shiharaiKingaku == null ? BigDecimal.ZERO : meisai.shiharaiKingaku;
			BigDecimal zeinukikingaku   = meisai.hontaiKingaku == null ? BigDecimal.ZERO : meisai.hontaiKingaku;
			BigDecimal shouhizeigaku1   = meisai.shouhizeigaku == null ? BigDecimal.ZERO : meisai.shouhizeigaku;
			
			//明細ループで支払金額・税抜金額・消費税額の税率別合計を取得
			if(shouhizeiNow.equals(zeiritsucheck)) {
				shiharaikingaku10Sum = shiharaikingaku10Sum.add(shiharaikingaku1);
				zeinukikingaku10Sum  = zeinukikingaku10Sum.add(zeinukikingaku);
				shouhizeigaku10Sum   = shouhizeigaku10Sum.add(shouhizeigaku1);
			}
			if(shouhizeiKeigenNow.equals(zeiritsucheck)) {
				shiharaikingaku8Sum = shiharaikingaku8Sum.add(shiharaikingaku1);
				zeinukikingaku8Sum  = zeinukikingaku8Sum.add(zeinukikingaku);
				shouhizeigaku8Sum   = shouhizeigaku8Sum.add(shouhizeigaku1);
			}
			
			//高さ調整：明細の取引名、科目、摘要、負担部門を1行7文字で計算。デフォルト２行だけどそれ以上の行数になるなら高さ変える。
			int torihikiLineNum =	excel.lineCount(torihiki, 14, 2);
			int kamokuLineNum =	excel.lineCount(kamokuBuf.toString(), 14, 2);
			int futanbumonLineNum =	excel.lineCount(futanBumonName, 14, 2);
			int tekiyouLineNum =	excel.lineCount(tekiyou, 14, 2);
			int kakuchouLineNum =	wfXlsLogic.findMaxInt(torihikiLineNum, kamokuLineNum, futanbumonLineNum, tekiyouLineNum);
			excel.setHeight(meisaiRow+i, kakuchouLineNum, 2);
		}
		//ループで取得していた各合計を出力
		if(isInvoice && ks.shiharaiKingakuGoukei.getPdfHyoujiFlg()) {
			excel.write("shiharai_kingaku_10", EteamXlsFmt.formatMoney(shiharaikingaku10Sum));
			excel.write("zeinuki_kingaku_10", EteamXlsFmt.formatMoney(zeinukikingaku10Sum));
			excel.write("shouhizeigaku_10", EteamXlsFmt.formatMoney(shouhizeigaku10Sum));
			excel.write("shiharai_kingaku_8", EteamXlsFmt.formatMoney(shiharaikingaku8Sum));
			excel.write("zeinuki_kingaku_8", EteamXlsFmt.formatMoney(zeinukikingaku8Sum));
			excel.write("shouhizeigaku_8", EteamXlsFmt.formatMoney(shouhizeigaku8Sum));
		}
		
		// ---------------------------------------
		//共通部分
		// ---------------------------------------
		commonXlsLogic.getAndPrintInvoiceZei("A009", denpyouId, excel); // 消費税額詳細
		wfXlsLogic.makeExcel(excel, denpyouId, false);
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
}
