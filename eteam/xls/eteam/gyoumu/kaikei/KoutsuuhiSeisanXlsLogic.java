package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
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
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.abstractdao.KiShouhizeiSettingAbstractDao;
import eteam.database.abstractdao.KoutsuuhiseisanAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KoutsuuhiseisanDao;
import eteam.database.dto.DenpyouShubetsuIchiran;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.Koutsuuhiseisan;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 交通費清算Logic
 */
public class KoutsuuhiSeisanXlsLogic extends KaikeiCommonXlsLogic {
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KOUTSUUHI_SEISAN);
	
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
		KaikeiCommonXlsLogic kaikeiXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		KoutsuuhiseisanAbstractDao koutsuuhiseisanDao = EteamContainer.getComponent(KoutsuuhiseisanDao.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		KiShouhizeiSettingAbstractDao kiShouhizei = EteamContainer.getComponent(KiShouhizeiSettingAbstractDao.class, connection);
		kaikeiXlsLogic.init(ks, null); // 経費共通部は交通費精算では使わないのでnullでいい
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_koutsuuhiseisan.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell meisaiCell = book.findCellByName("meisai_no01");
		int meisaiRow = meisaiCell.getRow();
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
		Koutsuuhiseisan hontai = koutsuuhiseisanDao.find(denpyouId); //koutsuuhiseisan
		List<GMap> meisaiList = kaikeiLogic.loadKoutsuuhiSeisanMeisai(denpyouId); //koutsuuhiseisan_meisai
		String hosoku = hontai.hosoku;
		String shiharaihouhouCd = hontai.shiharaihouhou;
		String shiharaihouhouName = sysLogic.findNaibuCdName("shiharai_houhou", shiharaihouhouCd);
		DenpyouShubetsuIchiran denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.KOUTSUUHI_SEISAN);
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KOUTSUUHI_SEISAN, hontai.map, null, "0");
		String version = "";
		int tekiyouByte = 0;
		version = Open21Env.Version.SIAS.toString();
		tekiyouByte = "SIAS".equals(version) ? 120 : 60;			
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
		boolean houjinCardRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean kaishatehaiRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		boolean isStartInvoice = "1".equals(invoiceStartFlg);
		boolean shiireKbnHyoujiFlg = 1 == kiShouhizei.findByDate(null).shiireZeigakuAnbunFlg;
				
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String torihikiTitle = "";
		String torihiki = "";
		String mokutekiTitle = "";
		String mokuteki = "";
		String tekiyouTitle = "";
		String tekiyou = "";
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
		String futanbumonTitle = "";
		String futanbumon = "";
		String torihikisakiTitle = "";
		String torihikisaki = "";
		String shiharaihouhouTitle = "";
		String shiharaihouhou = "";
		String seisankikanTitle = "";
		String seisankikan = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";

		String shiharaikiboubiTitle = "";
		String shiharaikiboubi = "";
		String goukeikingakuTitle = "";
		String goukeikingaku = "";
		String houjinkingakuTitle = "";
		String houjinkingaku = "";
		String kaishatehaigoukeiTitle = "";
		String kaishatehaigoukei = "";
		String sashihikiShikyuuKingakuTitle = "";
		String sashihikiShikyuuKingaku = "";
		String projectTitle = "";
		String project = "";
		String segmentTitle = "";
		String segment = "";
		
		String hosokuTitle = "";
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu);
		//インボイス開始後、かつインボイス前伝票（旧伝票）の場合
		kaikeiXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		//取引
		if(ks.torihiki.getPdfHyoujiFlg() == true){
			torihikiTitle = ks.torihiki.getName();
			if (hontai.shiwakeEdano != null)
			{
				torihiki = ks.torihiki.getCodeSeigyoValue(hontai.shiwakeEdano.toString(), hontai.torihikiName, " ");
			}
		}
		//目的
		if(ks.mokuteki.getPdfHyoujiFlg() == true){
			mokutekiTitle = ks.mokuteki.getName();
			mokuteki = hontai.mokuteki;
		}
		//摘要(注記除く)
		if(ks.tekiyou.getPdfHyoujiFlg() == true){
			tekiyou = hontai.tekiyou + chuuki;
			tekiyouTitle = ks.tekiyou.getName();
		}
		
		//科目
		if(ks.kamoku.getPdfHyoujiFlg()){
			kamokuTitle = ks.kamoku.getName();
			kamoku = ks.kamoku.getCodeSeigyoValue(hontai.kariKamokuCd,hontai.kariKamokuName, " ");
		}
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()){
			kazeiKbnTitle = ks.kazeiKbn.getName();
			kazeiKbn = sysLogic.findNaibuCdName("kazei_kbn", hontai.kariKazeiKbn);
		}
		//税率
		if(ks.zeiritsu.getPdfHyoujiFlg()) {
			zeiritsuTitle = ks.zeiritsu.getName();
			if (!kazeiKbn.isEmpty()) {
				zeiritsu = hontai.zeiritsu + " %";
			}
		}
		//分離区分
		if(ks.bunriKbn.getPdfHyoujiFlg()) {
			bunriKbnTitle = ks.bunriKbn.getName();
			if(!kazeiKbn.isEmpty()) {
				bunriKbn = sysLogic.findNaibuCdName("bunri_kbn", hontai.bunriKbn);
			}
		}
		//仕入区分
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireKbnHyoujiFlg) {
			shiireKbnTitle = ks.shiireKbn.getName();
			shiireKbn = sysLogic.findNaibuCdName("shiire_kbn", hontai.kariShiireKbn);
		}
		//科目枝番
		if(ks.kamokuEdaban.getPdfHyoujiFlg()){
			edabanTitle = ks.kamokuEdaban.getName();
			edaban = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kariKamokuEdabanCd,hontai.kariKamokuEdabanName, " ");
		}
		
		//負担部門
		if(ks.futanBumon.getPdfHyoujiFlg() == true){
			futanbumonTitle = ks.futanBumon.getName();
			futanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.kariFutanBumonCd,hontai.kariFutanBumonName, " ");
		}
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiTitle = ks.torihikisaki.getName();
			torihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.torihikisakiCd,hontai.torihikisakiNameRyakushiki, " ");
		}
				
		//支払方法
		if(ks.shiharaiHouhou.getPdfHyoujiFlg() == true){
			shiharaihouhouTitle = "支払" + ks.shiharaiHouhou.getName();
			shiharaihouhou = shiharaihouhouName;
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
			seisankikan = EteamXlsFmt.formatDate(hontai.seisankikanFrom) + "～" + EteamXlsFmt.formatDate(hontai.seisankikanTo);
		}
		//支払日(支払日は記載固定)
		shiharaibiTitle = "支払日";
		shiharaibi = EteamXlsFmt.formatDate(hontai.shiharaibi);
		//計上日
		//表示・非表示の判別はsetting_infoを参照
		if(!"3".equals(setting.shiwakeSakuseiHouhouA010())) {
			excel.write("keijoubi", EteamXlsFmt.formatDate(hontai.keijoubi));
		} else {
			excel.write("keijoubi_title", "");
		}
		//支払希望日
		if(ks.shiharaiKiboubi.getPdfHyoujiFlg() == true){
			shiharaikiboubiTitle = "支払" + ks.shiharaiKiboubi.getName();
			shiharaikiboubi = EteamXlsFmt.formatDate(hontai.shiharaikiboubi);
		}
		//合計金額
		if(ks.goukeiKingaku.getPdfHyoujiFlg() == true){
			goukeikingakuTitle = ks.goukeiKingaku.getName();
			goukeikingaku = EteamXlsFmt.formatMoney(hontai.goukeiKingaku);
			excel.makeBorder("meisaikingakugoukei_title");
			excel.makeBorder("meisaikingakugoukei");
		}
		//法人カード利用
		if(houjinCardRiyouFlg && ks.uchiHoujinCardRiyouGoukei.getPdfHyoujiFlg() == true) {
			houjinkingakuTitle = ks.uchiHoujinCardRiyouGoukei.getName();
			houjinkingaku = EteamXlsFmt.formatMoney(hontai.houjinCardRiyouKingaku);
			//枠線を書く
			excel.makeBorder("houjincardkingakugoukei_title");
			excel.makeBorder("houjincardkingakugoukei");
		}
		//会社手配
		if(kaishatehaiRiyouFlg && ks.kaishaTehaiGoukei.getPdfHyoujiFlg() == true){
			kaishatehaigoukeiTitle =  ks.kaishaTehaiGoukei.getName();
			kaishatehaigoukei = EteamXlsFmt.formatMoney(hontai.kaishaTehaiKingaku);
			//枠線をセット
			excel.makeBorder("kaishatehaigoukei_title");
			excel.makeBorder("kaishatehaigoukei");
			excel.makeBorder("kaishatehaigoukei2_title");
			excel.makeBorder("kaishatehaigoukei2");
		}
		//差引支給金額
		if(ks.sashihikiShikyuuKingaku.getPdfHyoujiFlg() == true) {
			//差引支給金額
			sashihikiShikyuuKingakuTitle = ks.sashihikiShikyuuKingaku.getName();
			sashihikiShikyuuKingaku      = EteamXlsFmt.formatMoney(hontai.sashihikiShikyuuKingaku);
			//枠線を書く
			excel.makeBorder("sasihikishikyuukingaku_title");
			excel.makeBorder("sashihiki_shikyuu_kingaku");
		} 
		//プロジェクト表示処理
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg() == true){
			projectTitle = ks.project.getName();
			project = ks.project.getCodeSeigyoValue(hontai.projectCd, hontai.projectName, " ");
		}
		//セグメント
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
			segmentTitle = ks.segment.getName();
			segment = ks.segment.getCodeSeigyoValue(hontai.segmentCd,hontai.segmentNameRyakushiki, " ");
		}
		
		//excelに各項目出力
		excel.write("torihiki_title", torihikiTitle );
		excel.write("torihiki", torihiki );
		excel.write("mokuteki_title", mokutekiTitle );
		excel.write("mokuteki", mokuteki );
		excel.write("tekiyou_title", tekiyouTitle );
		excel.write("tekiyou", tekiyou);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("kamoku", kamoku);
		excel.write("kazei_kbn_title", kazeiKbnTitle);
		excel.write("kazei_kbn", kazeiKbn);
		excel.write("zeiritsu_title", zeiritsuTitle);
		excel.write("zeiritsu", zeiritsu);
		excel.write("bunrikbn_title", bunriKbnTitle);
		excel.write("bunrikbn", bunriKbn);
		excel.write("shiirekbn_title", shiireKbnTitle);
		excel.write("shiirekbn", shiireKbn);
		excel.write("edaban_title", edabanTitle);
		excel.write("edaban", edaban);
		excel.write("futanbumon_title", futanbumonTitle );
		excel.write("futanbumon", futanbumon );
		excel.write("torihikisaki_title", torihikisakiTitle );
		excel.write("torihikisaki", torihikisaki );
		excel.write("shiharaihouhou_title", shiharaihouhouTitle );
		excel.write("shiharaihouhou", shiharaihouhou );
		excel.write("seisankikan_title", seisankikanTitle );
		excel.write("seisankikan", seisankikan );
		excel.write("shiharaibi_title", shiharaibiTitle );
		excel.write("shiharaibi", shiharaibi );
		excel.write("shiharaikiboubi_title", shiharaikiboubiTitle );
		excel.write("shiharaikiboubi", shiharaikiboubi );
		excel.write("meisaikingakugoukei_title", goukeikingakuTitle );
		excel.write("meisaikingakugoukei", goukeikingaku );
		excel.write("houjincardkingakugoukei_title",houjinkingakuTitle );
		excel.write("houjincardkingakugoukei", houjinkingaku );
		if(houjinCardRiyouFlg){
			excel.write("kaishatehaigoukei_title", kaishatehaigoukeiTitle);
			excel.write("kaishatehaigoukei", kaishatehaigoukei);
			// 不要行を非表示
			excel.hideRow(excel.getCell("kaishatehaigoukei2_title").getRow());
		}else{
			// 法人カード利用しない場合は、添字「2」のセルに値をセットする
			excel.write("kaishatehaigoukei2_title", kaishatehaigoukeiTitle);
			excel.write("kaishatehaigoukei2", kaishatehaigoukei);
			// 不要行を非表示
			excel.hideRow(excel.getCell("houjincardkingakugoukei_title").getRow());
			if(!kaishatehaiRiyouFlg) excel.hideRow(excel.getCell("kaishatehaigoukei2_title").getRow());
		}
		excel.write("sasihikishikyuukingaku_title", sashihikiShikyuuKingakuTitle );
		excel.write("sashihiki_shikyuu_kingaku"   , sashihikiShikyuuKingaku ); 
		excel.write("project_title", projectTitle);
		excel.write("project", project);
		excel.write("segment_title", segmentTitle );
		excel.write("segment", segment );
		
		//科目が非表示かつ摘要2行以内(38文字)の場合は行詰め
		if(ks.kamoku.getPdfHyoujiFlg() == false && 38 > tekiyou.length()){
			excel.hideRow(excel.getCell("kamoku_title").getRow());
		}
		//仕入区分非表示または仕入税額按分フラグが個別対応(1)、かつ摘要2行以内(38文字)の場合は行詰め
		if((ks.shiireKbn.getPdfHyoujiFlg() == null || !shiireKbnHyoujiFlg) && 38 > tekiyou.length()) {
			excel.hideRow(excel.getCell("shiirekbn_title").getRow());
		}
		//科目枝番が非表示かつ摘要2行以内(38文字)の場合は行詰め
		if(ks.kamokuEdaban.getPdfHyoujiFlg() == false && 38 > tekiyou.length()){
			excel.hideRow(excel.getCell("edaban_title").getRow());
		}
		//負担部門が非表示かつ摘要2行以内(38文字)の場合は行詰め
		if(ks.futanBumon.getPdfHyoujiFlg() == false && 38 > tekiyou.length()){
			excel.hideRow(excel.getCell("futanbumon_title").getRow());
		}
		//プロジェクトが非表示かつ摘要2行以内(38文字)の場合は行詰め
		if((!KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") || ks.project.getPdfHyoujiFlg() == false) && 38 > tekiyou.length() ){
			excel.hideRow(excel.getCell("project_title").getRow());
		}		
		//セグメントが非表示かつ摘要2行以内(38文字)の場合は行詰め
		if((!KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") || ks.segment.getPdfHyoujiFlg() == false) && 38 > tekiyou.length() ){
			excel.hideRow(excel.getCell("segment_title").getRow());
		};
		
		//■補足
		if(ks.hosoku.getPdfHyoujiFlg() == true){
			hosokuTitle = ks.hosoku.getName();
			excel.write("hosoku_title", "■" + hosokuTitle);
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

		if(isStartInvoice && isInvoice && ks.goukeiKingaku.getPdfHyoujiFlg()) {
			for (int i = 0; i < meisaiList.size(); i++) {
				var meisai = meisaiList.get(i);
				String zeiritsucheck = EteamXlsFmt.formatMoney(hontai.zeiritsu);
				//税込・税抜系以外は集計しない →税率0として扱う
				if(!List.of("001", "002", "011", "012", "013", "014", "1", "2", "11", "12", "13", "14").contains(hontai.kariKazeiKbn)) {
					zeiritsucheck = "0";
				}
				BigDecimal shiharaikingaku1 = meisai.get("meisai_kingaku") == null ? BigDecimal.ZERO : meisai.get("meisai_kingaku");
				BigDecimal zeinukikingaku = meisai.get("zeinuki_kingaku")  == null ? BigDecimal.ZERO : meisai.get("zeinuki_kingaku");
				BigDecimal shouhizeigaku1 = meisai.get("shouhizeigaku") == null ? BigDecimal.ZERO : meisai.get("shouhizeigaku");
			
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
		
		// インボイス関連処理
		super.getAndPrintInvoiceZei("A010", denpyouId, excel);
		
		//■明細
		//明細テンプレート行を明細件数分コピー（交通費の場合、種別コードの区別が不要なので素直に明細行数をカウントすればよい）
		if (1 < meisaiList.size()) excel.copyRow(meisaiRow, meisaiRow + 1, 1, meisaiList.size() - 1, true);
		// 同じ明細なのに修正箇所が増えるのを防ぐために、他の旅費精算系と共通化する
		kaikeiXlsLogic.makeExcelKokunaiMeisai(RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, meisaiRow, false);
		
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