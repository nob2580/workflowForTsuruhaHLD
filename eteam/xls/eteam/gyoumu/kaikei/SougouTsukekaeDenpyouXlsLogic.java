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
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.abstractdao.TsukekaeAbstractDao;
import eteam.database.abstractdao.TsukekaeMeisaiAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.TsukekaeDao;
import eteam.database.dao.TsukekaeMeisaiDao;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.Tsukekae;
import eteam.database.dto.TsukekaeMeisai;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 総合付替伝票Logic
 */
public class SougouTsukekaeDenpyouXlsLogic extends KaikeiCommonXlsLogic {
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU);

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
		TsukekaeAbstractDao tsukekaeDao = EteamContainer.getComponent(TsukekaeDao.class, connection);
		TsukekaeMeisaiAbstractDao tsukekaeMeisaiDao = EteamContainer.getComponent(TsukekaeMeisaiDao.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		KaikeiCommonXlsLogic commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		KiShouhizeiSettingDao kiShouhizeiSetting = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		KamokuMasterDao kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_sougoutsukekae.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell meisaiCell = book.findCellByName("tsukekaesaki_denpyouno");
		int meisaiRow = meisaiCell.getRow();
		Cell hosokuCell = book.findCellByName("hosoku");
		int hosokuRow = hosokuCell.getRow();
		int hosokuCol = hosokuCell.getColumn();
		Cell tekiyouCell = book.findCellByName("tsukekaesaki_tekiyou");
		int tekiyouRow = tekiyouCell.getRow();
		Cell bikouCell = book.findCellByName("tsukekaesaki_bikou");
		int bikouRow = bikouCell.getRow();

		// ---------------------------------------
		//共通部分（下部）
		// ---------------------------------------
		wfXlsLogic.makeExcelBottom(excel, denpyouId);

		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		Tsukekae hontai = tsukekaeDao.find(denpyouId); 
		List<TsukekaeMeisai> 	meisaiList = tsukekaeMeisaiDao.loadByDenpyouId(denpyouId); 
		String hosoku = hontai.hosoku;
		var denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSetting.findByDate(null).shiireZeigakuAnbunFlg;
		boolean isUriageZeiHeiyou = 2 == kiShouhizeiSetting.findByDate(null).uriagezeigakuKeisan ? true : false;
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		boolean isInvoice   	= "0".equals(hontai.invoiceDenpyou);
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String denpyouTitle = "";
		String denpyou = "";
		String zeiritsuTitle = "";
		String zeiritsu = "";
		String kamokuTitle = "";
		String kamokuedabanTitle = "";
		String futanbumonTitle = "";
		String torihikisakiTitle = "";
		String kazeikbnTitle = "";
		String bunrikbnTitle = "";
		String shiirekbnTitle = "";
		String jigyoushakbnTitle = "";
		String zeigakukeisanTitle = "";
		String kingakuTitle = "";
		String goukeikingakuTitle = "";
		String tekiyouTitle = "";
		String bikouTitle = "";
		String projectTitle = "";
		String segmentTitle = "";
		String hosokuTitle = "";
		
		boolean motoTitleDisp = false;
		boolean sakiTitleDisp = false;
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu);
		//インボイス処理開始後、かつ旧伝票（インボイス前伝票）の場合
		commonXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		//伝票日
		if(ks.denpyouDate.getPdfHyoujiFlg()){
			denpyouTitle = ks.denpyouDate.getName();
			denpyou = EteamXlsFmt.formatDate(hontai.denpyouDate);
		}
		//消費税率
		if(ks.zeiritsu.getPdfHyoujiFlg()){
			zeiritsuTitle = ks.zeiritsu.getName();
			zeiritsu = EteamXlsFmt.formatZeiritsu(hontai.zeiritsu, hontai.keigenZeiritsuKbn) + "%";
		}
		//勘定科目
		if(ks.kamoku.getPdfHyoujiFlg()){
			kamokuTitle = ks.kamoku.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kanjou_title").getRow());
		}
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()){
			kazeikbnTitle = ks.kazeiKbn.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kazeikubun_title").getRow());
		}
		//分離区分
		if(ks.bunriKbn.getPdfHyoujiFlg()){
			bunrikbnTitle = ks.bunriKbn.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("bunrikubun_title").getRow());
		}
		//仕入区分
		if(ks.shiireKbn.getPdfHyoujiFlg()){
			shiirekbnTitle = ks.shiireKbn.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("shiirekubun_title").getRow());
		}
		//勘定科目枝番
		if(ks.kamokuEdaban.getPdfHyoujiFlg()){
			kamokuedabanTitle = ks.kamokuEdaban.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("kanjouedaban_title").getRow());
		}
		//負担部門
		if(ks.futanBumon.getPdfHyoujiFlg()){
			futanbumonTitle = ks.futanBumon.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("futanbumon_title").getRow());
		}
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiTitle = ks.torihikisaki.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("torihikisaki_title").getRow());
		}
		//事業者区分
		if(ks.jigyoushaKbn.getPdfHyoujiFlg() && hontai.invoiceDenpyou.equals("0")){
			jigyoushakbnTitle = ks.jigyoushaKbn.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("jigyoushakubun_title").getRow());
		}
		//税額計算方式
		if("1".equals(invoiceStartFlg) && isInvoice && ks.uriagezeigakuKeisan.getPdfHyoujiFlg() && isUriageZeiHeiyou){
			zeigakukeisanTitle = ks.uriagezeigakuKeisan.getName(); 
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("zeigakukeisan_title").getRow());
			excel.hideRow(excel.getCell("tsukekaesaki_zeigakukeisan_title").getRow());
		}
		//合計金額
		if(ks.kingakuGoukei.getPdfHyoujiFlg()){
			goukeikingakuTitle = ks.kingakuGoukei.getName();
			motoTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("goukeikingaku_title").getRow());
		}
		//金額
		if(ks.kingaku.getPdfHyoujiFlg()){
			kingakuTitle = ks.kingaku.getName();
			sakiTitleDisp = true;
		}
		//摘要
		if(ks.tekiyou.getPdfHyoujiFlg()){
			tekiyouTitle = ks.tekiyou.getName();
			sakiTitleDisp = true;
		}
		//備考
		if(ks.bikou.getPdfHyoujiFlg()){
			bikouTitle = ks.bikou.getName();
			sakiTitleDisp = true;
		}
		//プロジェクト表示/非表示処理。非表示の場合、プロジェクト項目も削除
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
			projectTitle = ks.project.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("project_title").getRow());
			excel.hideRow(excel.getCell("tsukekaesaki_project_title").getRow());
		}
		
		//セグメント表示/非表示処理。非表示の場合、セグメント項目も削除
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
			segmentTitle = ks.segment.getName();
			motoTitleDisp = true;
			sakiTitleDisp = true;
		}else{
			excel.hideRow(excel.getCell("segment_title").getRow());
			excel.hideRow(excel.getCell("tsukekaesaki_segment_title").getRow());
		}
		//excelに各項目出力
		excel.write("denpyoubi_title", denpyouTitle);
		excel.write("denpyou", denpyou);
		excel.write("zeiritsu_title", zeiritsuTitle);
		excel.write("zeiritsu", zeiritsu);
		excel.write("kanjou_title", kamokuTitle);
		excel.write("kazeikubun_title", kazeikbnTitle);
		excel.write("bunrikubun_title", bunrikbnTitle);
		excel.write("shiirekubun_title", shiirekbnTitle);
		excel.write("kanjouedaban_title", kamokuedabanTitle);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("torihikisaki_title", torihikisakiTitle);
		excel.write("jigyoushakubun_title", jigyoushakbnTitle);
		excel.write("zeigakukeisan_title", zeigakukeisanTitle);
		excel.write("project_title", projectTitle);
		excel.write("segment_title", segmentTitle);
		excel.write("kazeikubun_title", kazeikbnTitle);
		excel.write("goukeikingaku_title", goukeikingakuTitle);
		excel.write("tsukekaesaki_kanjou_title", kamokuTitle);
		excel.write("tsukekaesaki_kazeikubun_title", kazeikbnTitle);
		excel.write("tsukekaesaki_bunrikubun_title", bunrikbnTitle);
		excel.write("tsukekaesaki_shiirekubun_title", shiirekbnTitle);
		excel.write("tsukekaesaki_kanjouedaban_title", kamokuedabanTitle);
		excel.write("tsukekaesaki_futanbumon_title", futanbumonTitle);
		excel.write("tsukekaesaki_torihikisaki_title", torihikisakiTitle);
		excel.write("tsukekaesaki_jigyoushakubun_title", jigyoushakbnTitle);
		excel.write("tsukekaesaki_zeigakukeisan_title", zeigakukeisanTitle);
		excel.write("tsukekaesaki_project_title", projectTitle);
		excel.write("tsukekaesaki_segment_title", segmentTitle);
		excel.write("tsukekaesaki_tekiyou_title", tekiyouTitle);
		excel.write("tsukekaesaki_bikou_title", bikouTitle);
		excel.write("tsukekaesaki_kingaku_title", kingakuTitle);
		
		//■付替元
		
		//付替区分
		String tsukekae_kbn = hontai.tsukekaeKbn;
		if(tsukekae_kbn.equals("1")){
			excel.write("tsukekae_kbn", "借方");
		}else if(tsukekae_kbn.equals("2")){
			excel.write("tsukekae_kbn", "貸方");
		}
		
		String kanjou       		= ks.kamoku.getCodeSeigyoValue(hontai.motoKamokuCd, hontai.motoKamokuName, " ");
		String kanjouedaban = ks.kamokuEdaban.getCodeSeigyoValue(hontai.motoKamokuEdabanCd, hontai.motoKamokuEdabanName, " ");
		String futanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.motoFutanBumonCd, hontai.motoFutanBumonName, " ");
		String torihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.motoTorihikisakiCd, hontai.motoTorihikisakiNameRyakushiki, " ");
		String project = ks.project.getCodeSeigyoValue(hontai.motoProjectCd, hontai.motoProjectName, " ");
		String segment = ks.segment.getCodeSeigyoValue(hontai.motoSegmentCd, hontai.motoSegmentNameRyakushiki, " ");
		String motoZeigakuKeisan = sysLogic.findNaibuCdName("uriagezeigaku_keisan", hontai.motoZeigakuHoushiki);

		excel.write("kanjou", kanjou);
		excel.write("kazeikubun", sysLogic.findNaibuCdName("kazei_kbn", hontai.motoKazeiKbn));
		excel.write("bunrikubun", sysLogic.findNaibuCdName("bunri_kbn", hontai.motoBunriKbn));
		
		excel.write("shiirekubun", sysLogic.findNaibuCdName("shiire_kbn", hontai.motoShiireKbn));
		excel.write("kanjouedaban", kanjouedaban);
		excel.write("futanbumon", futanbumon);
		excel.write("torihikisaki", torihikisaki);
		excel.write("jigyoushakubun", sysLogic.findNaibuCdName("jigyousha_kbn", hontai.motoJigyoushaKbn));
		//(付替元)税額計算方式		
		String motoKmkShoriGroup = kamokuMasterDao.find(hontai.motoKamokuCd).shoriGroup.toString();
		if(("4".equals(motoKmkShoriGroup) || "9".equals(motoKmkShoriGroup)) &&
		   ("001".equals(hontai.motoKazeiKbn) || "002".equals(hontai.motoKazeiKbn))) { 
			excel.write("zeigakukeisan", sysLogic.findNaibuCdName("uriagezeigaku_keisan", motoZeigakuKeisan));
		} else {
			excel.write("zeigakukeisan", sysLogic.findNaibuCdName("uriagezeigaku_keisan", ""));
		} 
		
		excel.write("project", hontai.motoProjectName);
		excel.write("segment", hontai.motoSegmentNameRyakushiki);
		excel.write("goukeikingaku", EteamXlsFmt.formatMoney(hontai.kingakuGoukei));
		//プロジェクト名表示/非表示。
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
			excel.write("project", project);
		}
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
			excel.write("segment", segment);
		}
		
		//UF表示/非表示
		if (!uf1ShiyouFlg.equals("0") && ks.uf1.getPdfHyoujiFlg()) {
			motoTitleDisp = true;
			sakiTitleDisp = true;
			excel.write("moto_uf1_name", KaishaInfo.getKaishaInfo(ColumnName.UF1_NAME));

			if(uf1ShiyouFlg.equals("1")){
				excel.write("uf1", hontai.motoUf1Cd);
			}else if(uf1ShiyouFlg.equals("2") || uf1ShiyouFlg.equals("3")){
				excel.write("uf1", hontai.motoUf1NameRyakushiki);
			}
		}
		
		if (!uf2ShiyouFlg.equals("0") && ks.uf2.getPdfHyoujiFlg()) {
			String motoUf2 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if(uf2ShiyouFlg.equals("1")){
				motoUf2 = hontai.motoUf2Cd;
			}else if(uf2ShiyouFlg.equals("2") || uf2ShiyouFlg.equals("3")){
				motoUf2 = hontai.motoUf2NameRyakushiki;
			}
			excel.write("moto_uf2_name", KaishaInfo.getKaishaInfo(ColumnName.UF2_NAME));
			excel.write("uf2", motoUf2);
		}
		if (!uf3ShiyouFlg.equals("0") && ks.uf3.getPdfHyoujiFlg()) {
			String motoUf3 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if(uf3ShiyouFlg.equals("1")){
				motoUf3 = hontai.motoUf3Cd;
			}else if(uf3ShiyouFlg.equals("2") || uf3ShiyouFlg.equals("3")){
				motoUf3 = hontai.motoUf3NameRyakushiki;
			}
			excel.write("moto_uf3_name", KaishaInfo.getKaishaInfo(ColumnName.UF3_NAME));
			excel.write("uf3", motoUf3);
		}
		if (!uf4ShiyouFlg.equals("0") && ks.uf4.getPdfHyoujiFlg()) {
			String motoUf4 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf4ShiyouFlg.equals("1")) {
				motoUf4 = hontai.motoUf4Cd;
			} else if (uf4ShiyouFlg.equals("2") || uf4ShiyouFlg.equals("3")) {
				motoUf4 = hontai.motoUf4NameRyakushiki;
			}
			excel.write("moto_uf4_name", KaishaInfo.getKaishaInfo(ColumnName.UF4_NAME));
			excel.write("uf4", motoUf4);
		}
		if (!uf5ShiyouFlg.equals("0") && ks.uf5.getPdfHyoujiFlg()) {
			String motoUf5 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf5ShiyouFlg.equals("1")) {
				motoUf5 = hontai.motoUf5Cd;
			} else if (uf5ShiyouFlg.equals("2") || uf5ShiyouFlg.equals("3")) {
				motoUf5 = hontai.motoUf5NameRyakushiki;
			}
			excel.write("moto_uf5_name", KaishaInfo.getKaishaInfo(ColumnName.UF5_NAME));
			excel.write("uf5", motoUf5);
		}
		if (!uf6ShiyouFlg.equals("0") && ks.uf6.getPdfHyoujiFlg()) {
			String motoUf6 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf6ShiyouFlg.equals("1")) {
				motoUf6 = hontai.motoUf6Cd;
			} else if (uf6ShiyouFlg.equals("2") || uf6ShiyouFlg.equals("3")) {
				motoUf6 = hontai.motoUf6NameRyakushiki;
			}
			excel.write("moto_uf6_name", KaishaInfo.getKaishaInfo(ColumnName.UF6_NAME));
			excel.write("uf6", motoUf6);
		}
		if (!uf7ShiyouFlg.equals("0") && ks.uf7.getPdfHyoujiFlg()) {
			String motoUf7 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf7ShiyouFlg.equals("1")) {
				motoUf7 = hontai.motoUf7Cd;
			} else if (uf7ShiyouFlg.equals("2") || uf7ShiyouFlg.equals("3")) {
				motoUf7 = hontai.motoUf7NameRyakushiki;
			}
			excel.write("moto_uf7_name", KaishaInfo.getKaishaInfo(ColumnName.UF7_NAME));
			excel.write("uf7", motoUf7);
		}
		if (!uf8ShiyouFlg.equals("0") && ks.uf8.getPdfHyoujiFlg()) {
			String motoUf8 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf8ShiyouFlg.equals("1")) {
				motoUf8 = hontai.motoUf8Cd;
			} else if (uf8ShiyouFlg.equals("2") || uf8ShiyouFlg.equals("3")) {
				motoUf8 = hontai.motoUf8NameRyakushiki;
			}
			excel.write("moto_uf8_name", KaishaInfo.getKaishaInfo(ColumnName.UF8_NAME));
			excel.write("uf8", motoUf8);
		}
		if (!uf9ShiyouFlg.equals("0") && ks.uf9.getPdfHyoujiFlg()) {
			String motoUf9 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf9ShiyouFlg.equals("1")) {
				motoUf9 = hontai.motoUf9Cd;
			} else if (uf9ShiyouFlg.equals("2") || uf9ShiyouFlg.equals("3")) {
				motoUf9 = hontai.motoUf9NameRyakushiki;
			}
			excel.write("moto_uf9_name", KaishaInfo.getKaishaInfo(ColumnName.UF9_NAME));
			excel.write("uf9", motoUf9);
		}
		if (!uf10ShiyouFlg.equals("0") && ks.uf10.getPdfHyoujiFlg()) {
			String motoUf10 = null;
			motoTitleDisp = true;
			sakiTitleDisp = true;
			if (uf10ShiyouFlg.equals("1")) {
				motoUf10 = hontai.motoUf10Cd;
			} else if (uf10ShiyouFlg.equals("2") || uf10ShiyouFlg.equals("3")) {
				motoUf10 = hontai.motoUf10NameRyakushiki;
			}
			excel.write("moto_uf10_name", KaishaInfo.getKaishaInfo(ColumnName.UF10_NAME));
			excel.write("uf10", motoUf10);
		}

		//■補足
		if(ks.hosoku.getPdfHyoujiFlg()){
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
		
		//明細の長さ取得（空白まで）
		int tsukekaeLength = excel.getCell("tsukekaesaki_kingaku").getRow() - excel.getCell("tsukekaesaki_denpyouno").getRow() + 2;
		
		//■明細
		//明細テンプレート行コピー
		if (1< meisaiList.size()) excel.copyRow(meisaiRow, meisaiRow + tsukekaeLength, tsukekaeLength, ((meisaiList.size()-1)/2), true);
		
		String tsukekae_saki  	= null; //付替先の付替名セットする関数
		if(tsukekae_kbn.equals("1")){
			tsukekae_saki ="貸方#";
		}else if(tsukekae_kbn.equals("2")){
			tsukekae_saki  ="借方#";
		}

		//摘要行と備考行の初期化
		int tekiyouLineNum = 1;
		int bikouLineNum = 1;
		
		for (int i = 0; i < meisaiList.size(); i++) {
			
			//1行ずつ書き込み

			TsukekaeMeisai meisai = meisaiList.get(i);
			
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU, hontai.map, meisai.map, "0");
			int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;			
			String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
			
			String tekiyou = meisai.tekiyou + chuuki;
			String bikou = meisai.bikou;

			String sakiKanjou       	= ks.kamoku.getCodeSeigyoValue(meisai.sakiKamokuCd, meisai.sakiKamokuName, " ");
			String sakiKanjouedaban = ks.kamokuEdaban.getCodeSeigyoValue(meisai.sakiKamokuEdabanCd, meisai.sakiKamokuEdabanName, " ");
			String sakiFutanbumon = ks.futanBumon.getCodeSeigyoValue(meisai.sakiFutanBumonCd, meisai.sakiFutanBumonName, " ");
			String sakiTorihikisaki = ks.torihikisaki.getCodeSeigyoValue(meisai.sakiTorihikisakiCd, meisai.sakiTorihikisakiNameRyakushiki, " ");
			String sakiProject = ks.project.getCodeSeigyoValue(meisai.sakiProjectCd, meisai.sakiProjectName, " ");
			String sakiSegment = ks.segment.getCodeSeigyoValue(meisai.sakiSegmentCd, meisai.sakiSegmentNameRyakushiki, " ");

			//相対行列番号を決定　※i=0の時の書込位置はセル名で取得する前提
			int soutaiRow = tsukekaeLength * (i / 2);
			int soutaiCol = (i % 2) == 0 ? 0 : 13; //奇数番なら0、偶数番目なら13

			excel.write("tsukekaesaki_denpyouno", soutaiCol, soutaiRow, tsukekae_saki + Integer.toString(i+1));
			excel.write("tsukekaesaki_kanjou", soutaiCol, soutaiRow, sakiKanjou);
			if(ks.kazeiKbn.getPdfHyoujiFlg()) {
				excel.write("tsukekaesaki_kazeikubun", soutaiCol, soutaiRow, sysLogic.findNaibuCdName("kazei_kbn", meisai.sakiKazeiKbn));
			}
			if(ks.bunriKbn.getPdfHyoujiFlg()) {
				excel.write("tsukekaesaki_bunrikubun", soutaiCol, soutaiRow, sysLogic.findNaibuCdName("bunri_kbn", meisai.sakiBunriKbn));
			}
			if(ks.shiireKbn.getPdfHyoujiFlg()) {
				excel.write("tsukekaesaki_shiirekubun", soutaiCol, soutaiRow, sysLogic.findNaibuCdName("shiire_kbn", meisai.sakiShiireKbn));
			}
			if(ks.kamokuEdaban.getPdfHyoujiFlg()){
				excel.write("tsukekaesaki_kanjouedaban", soutaiCol, soutaiRow, sakiKanjouedaban);
			}
			if(ks.futanBumon.getPdfHyoujiFlg()){
				excel.write("tsukekaesaki_futanbumon", soutaiCol, soutaiRow, sakiFutanbumon);
			}
			if(ks.torihikisaki.getPdfHyoujiFlg()){
				excel.write("tsukekaesaki_torihikisaki", soutaiCol, soutaiRow, sakiTorihikisaki);
			}
			if(ks.jigyoushaKbn.getPdfHyoujiFlg()) {
				excel.write("tsukekaesaki_jigyoushakubun", soutaiCol, soutaiRow, sysLogic.findNaibuCdName("jigyousha_kbn", meisai.sakiJigyoushaKbn));
			}
			
			//(付替先)税額計算方式
			String sakiKmkShoriGroup = kamokuMasterDao.find(meisai.sakiKamokuCd).shoriGroup.toString();
			if(("4".equals(sakiKmkShoriGroup) || "9".equals(sakiKmkShoriGroup)) &&
			   ("001".equals(meisai.sakiKazeiKbn) || "002".equals(meisai.sakiKazeiKbn))) {
				excel.write("tsukekaesaki_zeigakukeisan", soutaiCol, soutaiRow, sysLogic.findNaibuCdName("uriagezeigaku_keisan", meisai.sakiZeigakuHoushiki));
			} else {
				excel.write("tsukekaesaki_zeigakukeisan", "");
			} 
			
			if(ks.uriagezeigakuKeisan.getPdfHyoujiFlg()) {
				excel.write("tsukekaesaki_tekiyou", soutaiCol, soutaiRow, tekiyou);
			}
			
			if(ks.bikou.getPdfHyoujiFlg()){
				excel.write("tsukekaesaki_bikou", soutaiCol, soutaiRow, bikou);
			}
			excel.write("tsukekaesaki_kingaku", soutaiCol, soutaiRow, EteamXlsFmt.formatMoney(meisai.kingaku));
			//プロジェクト名表示/非表示
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
				excel.write("tsukekaesaki_project", soutaiCol, soutaiRow, sakiProject);
			}
			//セグメント名表示/非表示
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
				excel.write("tsukekaesaki_segment", soutaiCol, soutaiRow, sakiSegment);
			}
			//UF表示/非表示処理
			if (!uf1ShiyouFlg.equals("0") && ks.uf1.getPdfHyoujiFlg()) {
				String sakiUf1 = null;
				if(uf1ShiyouFlg.equals("1")){
					sakiUf1 = meisai.sakiUf1Cd;
				}else if(uf1ShiyouFlg.equals("2") || uf1ShiyouFlg.equals("3")){
					sakiUf1 = meisai.sakiUf1NameRyakushiki;
				}
				excel.write("saki_uf1_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF1_NAME));
				excel.write("tsukekaesaki_uf1", soutaiCol, soutaiRow, sakiUf1);
			}
			if (!uf2ShiyouFlg.equals("0") && ks.uf2.getPdfHyoujiFlg()) {
				String sakiUf2 = null;
				if(uf2ShiyouFlg.equals("1")){
					sakiUf2 = meisai.sakiUf2Cd;
				}else if(uf2ShiyouFlg.equals("2") || uf2ShiyouFlg.equals("3")){
					sakiUf2= meisai.sakiUf2NameRyakushiki;
				}
				excel.write("saki_uf2_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF2_NAME));
				excel.write("tsukekaesaki_uf2", soutaiCol, soutaiRow, sakiUf2);
			}
			if (!uf3ShiyouFlg.equals("0") && ks.uf3.getPdfHyoujiFlg()) {
				String sakiUf3 = null;
				if(uf3ShiyouFlg.equals("1")){
					sakiUf3 = meisai.sakiUf3Cd;
				}else if(uf3ShiyouFlg.equals("2") || uf3ShiyouFlg.equals("3")){
					sakiUf3 = meisai.sakiUf3NameRyakushiki;
				}
				excel.write("saki_uf3_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF3_NAME));
				excel.write("tsukekaesaki_uf3", soutaiCol, soutaiRow, sakiUf3);
			}
			if (!uf4ShiyouFlg.equals("0") && ks.uf4.getPdfHyoujiFlg()) {
				String sakiUf4 = null;
				if (uf4ShiyouFlg.equals("1")) {
					sakiUf4 = meisai.sakiUf4Cd;
				} else if (uf4ShiyouFlg.equals("2") || uf4ShiyouFlg.equals("3")) {
					sakiUf4 = meisai.sakiUf4NameRyakushiki;
				}
				excel.write("saki_uf4_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF4_NAME));
				excel.write("tsukekaesaki_uf4", soutaiCol, soutaiRow, sakiUf4);
			}
			if (!uf5ShiyouFlg.equals("0") && ks.uf5.getPdfHyoujiFlg()) {
				String sakiUf5 = null;
				if (uf5ShiyouFlg.equals("1")) {
					sakiUf5 = meisai.sakiUf5Cd;
				} else if (uf5ShiyouFlg.equals("2") || uf5ShiyouFlg.equals("3")) {
					sakiUf5 = meisai.sakiUf5NameRyakushiki;
				}
				excel.write("saki_uf5_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF5_NAME));
				excel.write("tsukekaesaki_uf5", soutaiCol, soutaiRow, sakiUf5);
			}
			if (!uf6ShiyouFlg.equals("0") && ks.uf6.getPdfHyoujiFlg()) {
				String sakiUf6 = null;
				if (uf6ShiyouFlg.equals("1")) {
					sakiUf6 = meisai.sakiUf6Cd;
				} else if (uf6ShiyouFlg.equals("2") || uf6ShiyouFlg.equals("3")) {
					sakiUf6 = meisai.sakiUf6NameRyakushiki;
				}
				excel.write("saki_uf6_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF6_NAME));
				excel.write("tsukekaesaki_uf6", soutaiCol, soutaiRow, sakiUf6);
			}
			if (!uf7ShiyouFlg.equals("0") && ks.uf7.getPdfHyoujiFlg()) {
				String sakiUf7 = null;
				if (uf7ShiyouFlg.equals("1")) {
					sakiUf7 = meisai.sakiUf7Cd;
				} else if (uf7ShiyouFlg.equals("2") || uf7ShiyouFlg.equals("3")) {
					sakiUf7 = meisai.sakiUf7NameRyakushiki;
				}
				excel.write("saki_uf7_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF7_NAME));
				excel.write("tsukekaesaki_uf7", soutaiCol, soutaiRow, sakiUf7);
			}
			if (!uf8ShiyouFlg.equals("0") && ks.uf8.getPdfHyoujiFlg()) {
				String sakiUf8 = null;
				if (uf8ShiyouFlg.equals("1")) {
					sakiUf8 = meisai.sakiUf8Cd;
				} else if (uf8ShiyouFlg.equals("2") || uf8ShiyouFlg.equals("3")) {
					sakiUf8 = meisai.sakiUf8NameRyakushiki;
				}
				excel.write("saki_uf8_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF8_NAME));
				excel.write("tsukekaesaki_uf8", soutaiCol, soutaiRow, sakiUf8);
			}
			if (!uf9ShiyouFlg.equals("0") && ks.uf9.getPdfHyoujiFlg()) {
				String sakiUf9 = null;
				if (uf9ShiyouFlg.equals("1")) {
					sakiUf9 = meisai.sakiUf9Cd;
				} else if (uf9ShiyouFlg.equals("2") || uf9ShiyouFlg.equals("3")) {
					sakiUf9 = meisai.sakiUf9NameRyakushiki;
				}
				excel.write("saki_uf9_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF9_NAME));
				excel.write("tsukekaesaki_uf9", soutaiCol, soutaiRow, sakiUf9);
			}
			if (!uf10ShiyouFlg.equals("0") && ks.uf10.getPdfHyoujiFlg()) {
				String sakiUf10 = null;
				if (uf10ShiyouFlg.equals("1")) {
					sakiUf10 = meisai.sakiUf10Cd;
				} else if (uf10ShiyouFlg.equals("2") || uf10ShiyouFlg.equals("3")) {
					sakiUf10 = meisai.sakiUf10NameRyakushiki;
				}
				excel.write("saki_uf10_name", soutaiCol, soutaiRow, KaishaInfo.getKaishaInfo(ColumnName.UF10_NAME));
				excel.write("tsukekaesaki_uf10", soutaiCol, soutaiRow, sakiUf10);
			}

			//明細件数が奇数の場合に摘要行と備考行の初期化する。
			if(i != 0 && i%2 == 0){
				tekiyouLineNum = 1;
				bikouLineNum = 1;
			}
			//高さ調整：摘要と備考は1行21文字以内で計算。デフォルト1行だけどそれ以上の行数になるなら高さ変える。
			int tekiyouLineNumTmp = excel.lineCount(tekiyou, 42, 1);
			if( tekiyouLineNum < tekiyouLineNumTmp){
				tekiyouLineNum = tekiyouLineNumTmp;
			}
			excel.setHeight(tekiyouRow + soutaiRow,  tekiyouLineNum, 1);
			int bikouLineNumTmp  = excel.lineCount(bikou, 42, 1);
			if( bikouLineNum < bikouLineNumTmp){
				bikouLineNum = bikouLineNumTmp;
			}
			excel.setHeight(bikouRow + soutaiRow,  bikouLineNum, 1);
			
			//改ページ設定
			//1ページ目は4明細入る...3明細出し終わったら改頁
			//2ページ目以降は6明細入る...9明細出し終わったら改頁、15明細出し終わったら改頁、、、
			if ((i < meisaiList.size() - 2) &&						//該当明細がページ内最終グループなら改頁しない
				( (i == 2) || (i >= 4 && ((i-2) % 6 == 0))) ) {
				int row  = excel.getCell("force_page").getRow() + soutaiRow + 1;
				excel.getSheet().addRowPageBreak(row);
			}
		}
		
		//表示/非表示
		if (KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("0") || !ks.project.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("project").getRow());
		}
		if (KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("0") || !ks.segment.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("segment").getRow());
		}
		//UF表示/非表示処理。非表示の場合、プロジェクト項目も削除。
		if (uf1ShiyouFlg.equals("0") || !ks.uf1.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf1").getRow());
		}
		if (uf2ShiyouFlg.equals("0") || !ks.uf2.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf2").getRow());
		}
		if (uf3ShiyouFlg.equals("0") || !ks.uf3.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf3").getRow());
		}
		if (uf4ShiyouFlg.equals("0") || !ks.uf4.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf4").getRow());
		}
		if (uf5ShiyouFlg.equals("0") || !ks.uf5.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf5").getRow());
		}
		if (uf6ShiyouFlg.equals("0") || !ks.uf6.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf6").getRow());
		}
		if (uf7ShiyouFlg.equals("0") || !ks.uf7.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf7").getRow());
		}
		if (uf8ShiyouFlg.equals("0") || !ks.uf8.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf8").getRow());
		}
		if (uf9ShiyouFlg.equals("0") || !ks.uf9.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf9").getRow());
		}
		if (uf10ShiyouFlg.equals("0") || !ks.uf10.getPdfHyoujiFlg()) {
			excel.hideRow(excel.getCell("uf10").getRow());
		}

		//明細を隠す
		for (int i = 0; i < meisaiList.size(); i+=2) {
			//相対行列番号を決定　※i=0の時の書込位置はセル名で取得する前提
			int soutaiRow = tsukekaeLength * (i / 2);
			
			//勘定科目
			if(!ks.kamoku.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_kanjou").getRow() + soutaiRow);
			}
			//課税区分
			if(!ks.kazeiKbn.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_kazeikubun").getRow() + soutaiRow);
			}
			//分離区分
			if(!ks.bunriKbn.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_bunrikubun").getRow() + soutaiRow);
			}
			//仕入区分
			if(!ks.shiireKbn.getPdfHyoujiFlg() || !shiireHyoujiFlg) {
				excel.hideRow(excel.getCell("tsukekaesaki_shiirekubun").getRow() + soutaiRow);
			}
			//勘定科目枝番
			if(!ks.kamokuEdaban.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_kanjouedaban").getRow() + soutaiRow);
			}
			//負担部門
			if(!ks.futanBumon.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_futanbumon").getRow() + soutaiRow);
			}
			//取引先
			if(!ks.torihikisaki.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_torihikisaki").getRow() + soutaiRow);
			}
			//事業者区分
			if(!ks.jigyoushaKbn.getPdfHyoujiFlg() || hontai.invoiceDenpyou.equals("1")) {
				excel.hideRow(excel.getCell("tsukekaesaki_jigyoushakubun").getRow() + soutaiRow);
			}
			//税額計算方式
			if(!ks.uriagezeigakuKeisan.getPdfHyoujiFlg() || hontai.invoiceDenpyou.equals("1")) {
				excel.hideRow(excel.getCell("tsukekaesaki_zeigakukeisan").getRow() + soutaiRow);
			}
			//備考
			if(!ks.bikou.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_bikou").getRow() + soutaiRow);
			}
			//プロジェクト名表示/非表示
			if (KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("0") || !ks.project.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_project").getRow() + soutaiRow);
			}
			if (KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("0") || !ks.segment.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_segment").getRow() + soutaiRow);
			}
			
			//UF表示/非表示処理。非表示
			if (uf1ShiyouFlg.equals("0") || !ks.uf1.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf1").getRow() + soutaiRow);
			}
			if (uf2ShiyouFlg.equals("0") || !ks.uf2.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf2").getRow() + soutaiRow);
			}
			if (uf3ShiyouFlg.equals("0") || !ks.uf3.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf3").getRow() + soutaiRow);
			}
			if (uf4ShiyouFlg.equals("0") || !ks.uf4.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf4").getRow() + soutaiRow);
			}
			if (uf5ShiyouFlg.equals("0") || !ks.uf5.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf5").getRow() + soutaiRow);
			}
			if (uf6ShiyouFlg.equals("0") || !ks.uf6.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf6").getRow() + soutaiRow);
			}
			if (uf7ShiyouFlg.equals("0") || !ks.uf7.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf7").getRow() + soutaiRow);
			}
			if (uf8ShiyouFlg.equals("0") || !ks.uf8.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf8").getRow() + soutaiRow);
			}
			if (uf9ShiyouFlg.equals("0") || !ks.uf9.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf9").getRow() + soutaiRow);
			}
			if (uf10ShiyouFlg.equals("0") || !ks.uf10.getPdfHyoujiFlg()) {
				excel.hideRow(excel.getCell("tsukekaesaki_uf10").getRow() + soutaiRow);
			}

			//摘要
			if(!ks.tekiyou.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_tekiyou").getRow() + soutaiRow);
			}
			//備考
			if(!ks.bikou.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_bikou").getRow() + soutaiRow);
			}
			//金額
			if(!ks.kingaku.getPdfHyoujiFlg()){
				excel.hideRow(excel.getCell("tsukekaesaki_kingaku").getRow() + soutaiRow);
			}

			if(!sakiTitleDisp){
				excel.hideRow(excel.getCell("saki_title").getRow()+1 + soutaiRow);
			}
		}
		if(!motoTitleDisp){
			excel.hideRow(excel.getCell("moto_title").getRow());
			excel.hideRow(excel.getCell("moto_title").getRow()+1);
		}
		if(!sakiTitleDisp){
			excel.hideRow(excel.getCell("saki_title").getRow());
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
