package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.RyohiKaribaraiAbstractDao;
import eteam.database.abstractdao.RyohiseisanAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.RyohiKaribaraiDao;
import eteam.database.dao.RyohiseisanDao;
import eteam.database.dto.DenpyouShubetsuIchiran;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.RyohiKaribarai;
import eteam.database.dto.Ryohiseisan;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 *出張旅費精算(仮払清算) Logic
 */
public class RyohiSeisanXlsLogic extends KaikeiCommonXlsLogic {
	
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.RYOHI_SEISAN);	
	/** 画面項目制御クラス(経費明細) */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	/** 画面項目制御クラス(旅費仮払) */
	GamenKoumokuSeigyo ksKari = new GamenKoumokuSeigyo(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI);
	
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
		KaikeiCategoryLogic kaikeiCategoryLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		KaikeiCommonXlsLogic kaikeiXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		RyohiseisanAbstractDao ryohiseisanDao = EteamContainer.getComponent(RyohiseisanDao.class, connection);
		RyohiKaribaraiAbstractDao ryohiKaribaraiDao = EteamContainer.getComponent(RyohiKaribaraiDao.class, connection);
		NaibuCdSettingAbstractDao naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.kiShouhizeiSetting = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		WorkflowEventControlLogic workflowEventControlLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		kaikeiXlsLogic.init(ks,ks1);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_ryohiseisan.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell shucchousakiCell = book.findCellByName("shucchousaki");
		int shucchousakiRow = shucchousakiCell.getRow();
		int shucchousakiCol = shucchousakiCell.getColumn();
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
		Ryohiseisan hontai = ryohiseisanDao.find(denpyouId);			//ryohiseisan
		List<GMap> meisaiList = kaikeiCategoryLogic.loadRyohiSeisanMeisai(denpyouId);		//ryohiseisan_meisai
		List<GMap> meisaiKeihiList = kaikeiCategoryLogic.loadRyohiKeihiseisanMeisai(denpyouId);//ryohiseisan_keihi_meisai
		String hosoku = hontai.hosoku;
		String shiharaihouhouCd = hontai.shiharaihouhou;
		DenpyouShubetsuIchiran denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.RYOHI_SEISAN);
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.RYOHI_SEISAN, hontai.map, null, "0");
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;			
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
		boolean houjinCardRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean kaishatehaiRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		var kazeiInfo = naibuCdSettingDao.find("kazei_kbn", hontai.kariKazeiKbn);
		String kazeiKbnGroup = (null != kazeiInfo)? kazeiInfo.option1 : "";
		boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSetting.findByDate(null).shiireZeigakuAnbunFlg;
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		InvoiceStart invoice = invoiceStart.find();
		String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
		boolean isBeforeInvoice	= "1".equals(invoiceStartFlg);
		
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
		String torihikisakiTitle = "";
		String torihikisaki = "";
		String tekiyouRyohiTitle = "";
		String tekiyou = "";
		String sashihikishikyuukingakuTitle = "";
		String sashihikishikyuukingaku = "";
		String karibaraikingakuTitle = "";
		String karibaraikingaku = "";
		String houjinkingakuTitle = "";
		String houjinkingaku = "";
		String kaishatehaigoukeiTitle = "";
		String kaishatehaigoukei = "";

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
		if(ks.userName.getPdfHyoujiFlg()){
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
		}
		//出張先・訪問先
		if(ks.houmonsaki.getPdfHyoujiFlg()){
			shucchousakiTitle = ks.houmonsaki.getName();
			shucchousaki = hontai.houmonsaki;
		}
		//目的
		if(ks.mokuteki.getPdfHyoujiFlg()){
			mokutekiTitle = ks.mokuteki.getName();
			mokuteki = hontai.mokuteki;
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
		if(ks.zeiritsu.getPdfHyoujiFlg() && zeiritsuHyoujiFlg){
			zeiritsuTitle = ks.zeiritsu.getName();
			zeiritsu = EteamXlsFmt.formatZeiritsu(hontai.zeiritsu, hontai.keigenZeiritsuKbn) + " %";
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
		}
		
		//負担部門
		if(ks.futanBumon.getPdfHyoujiFlg()){
			futanbumonRyohiTitle = ks.futanBumon.getName();
			futanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.kariFutanBumonCd,hontai.kariFutanBumonName, " ");
		}
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiTitle = ks.torihikisaki.getName();
			torihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.torihikisakiCd,hontai.torihikisakiNameRyakushiki, " ");
		}
		
		//摘要
		if(ks.tekiyou.getPdfHyoujiFlg()){
			tekiyou = hontai.tekiyou + chuuki;
			tekiyouRyohiTitle = ks.tekiyou.getName();
		}
		//精算期間&精算時刻
		if(ks.seisankikan.getPdfHyoujiFlg() && ks.seisankikanJikoku.getPdfHyoujiFlg()){
			seisankikanTitle = ks.seisankikan.getName();
			seisankikan = 	EteamXlsFmt.formatDate(hontai.seisankikanFrom) + " " 
						+ 	EteamXlsFmt.formatTimeHHmm(hontai.seisankikanFromHour, hontai.seisankikanFromMin)
						+ 	" ～ " + EteamXlsFmt.formatDate(hontai.seisankikanTo) + " " 
						+ 	EteamXlsFmt.formatTimeHHmm(hontai.seisankikanToHour, hontai.seisankikanToMin);
		//精算期間のみ
		} else if (ks.seisankikan.getPdfHyoujiFlg()) {
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
		if(!"3".equals(setting.shiwakeSakuseiHouhouA004())) {
			excel.write("keijoubi", EteamXlsFmt.formatDate(hontai.keijoubi));
		} else {
			excel.write("keijoubi_title", "");
		}
		//支払希望日
		if(ks.shiharaiKiboubi.getPdfHyoujiFlg()){
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
			RyohiKaribarai karibaraikingakuMap = ryohiKaribaraiDao.find(hontai.karibaraiDenpyouId);
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
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
			projectTitle = ks.project.getName();
			project = ks.project.getCodeSeigyoValue(hontai.projectCd,hontai.projectName, " ");
		}
		//セグメント
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
			segmentTitle = ks.segment.getName();
			segment = ks.segment.getCodeSeigyoValue(hontai.segmentCd,hontai.segmentNameRyakushiki, " ");
		}
		//excelに各項目出力(「出張先・訪問先」行は改行要素があるため後述)
		excel.write("shiyousha_title", shiyoushaTitle);
		excel.write("shiyousha", shiyousha);
		excel.write("torihiki_title", torihikiRyohiTitle);
		excel.write("torihiki", torihiki);
		excel.write("mokuteki_title", mokutekiTitle);
		excel.write("mokuteki", mokuteki);
		excel.write("futanbumon_title", futanbumonRyohiTitle);
		excel.write("futanbumon", futanbumon);
		excel.write("torihikisaki_title", torihikisakiTitle );
		excel.write("torihikisaki", torihikisaki );
		excel.write("tekiyou_title", tekiyouRyohiTitle);
		excel.write("tekiyou", tekiyou);
		excel.write("seisankikan_title", seisankikanTitle);
		excel.write("seisankikan", seisankikan);
		excel.write("shiharaibi_title", shiharaibiTitle);
		excel.write("shiharaibi", shiharaibi);
		excel.write("kamoku_title", kamokuTitle );
		excel.write("kamoku", kamoku );
		excel.write("kazei_kbn_title", kazeiKbnTitle);
		excel.write("kazei_kbn", kazeiKbn);
		excel.write("zeiritsu_title", zeiritsuTitle);
		excel.write("zeiritsu", zeiritsu);
		excel.write("bunri_kbn_title", bunriKbnTitle);
		excel.write("bunri_kbn", bunriKbn);
		excel.write("shiire_kbn_title", shiireKbnTitle);
		excel.write("shiire_kbn", shiireKbn);
		excel.write("edaban_title", edabanTitle );
		excel.write("edaban", edaban );
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
		excel.write("project_title", projectTitle);
		excel.write("project", project);
		excel.write("segment_title", segmentTitle );
		excel.write("segment", segment );
		
		//■補足
		if(ks.hosoku.getPdfHyoujiFlg()){
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
		// 差引
		var sashihikiKingaku = BigDecimal.ZERO;
		var sashihikiZeigaku = BigDecimal.ZERO;
		
		//支払金額（税額別）
		String shiharaikingaku10Title = "支払金額10%";
		String shiharaikingaku8Title = "支払金額*8%";
		String zeinukikingaku10Title = "税抜金額10%";
		String zeinukikingaku8Title = "税抜金額*8%";
		String shouhizeigaku10Title = "消費税額10%";
		String shouhizeigaku8Title = "消費税額*8%";
		
		if("1".equals(invoiceStartFlg) && isInvoice && ks.goukeiKingaku.getPdfHyoujiFlg()) {
			var jointMeisaiList = Stream.concat(meisaiList.stream(), meisaiKeihiList.stream()).collect(Collectors.toList());
			for (int i = 0; i < jointMeisaiList.size(); i++) {
				var meisai = jointMeisaiList.get(i);
				String zeiritsucheck = EteamXlsFmt.formatMoney(meisai.getOrDefault("zeiritsu", hontai.zeiritsu));
				//税込・税抜系以外は集計しない →税率0として扱う
				if(!List.of("001", "002", "011", "012", "013", "014", "1", "2", "11", "12", "13", "14").contains(meisai.getOrDefault("kari_kazei_kbn", hontai.kariKazeiKbn))) {
					zeiritsucheck = "0";
				}
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
			
			if(hontai.sashihikiNum != null) {
				sashihikiKingaku = hontai.sashihikiNum.multiply(hontai.sashihikiTanka);
				sashihikiZeigaku = kaikeiCommonLogic.processShouhizeiFraction(workflowEventControlLogic.judgeHasuuKeisan(), sashihikiKingaku.multiply(hontai.zeiritsu).divide(new BigDecimal("100").add(hontai.zeiritsu), 3, RoundingMode.HALF_UP), true);
				var sashihikiZeinukiKingaku = sashihikiKingaku.subtract(sashihikiZeigaku);
				//加算時と同じく課税区分を確認する
				if(List.of("001", "002", "011", "012", "013", "014", "1", "2", "11", "12", "13", "14").contains(hontai.kariKazeiKbn)) {
					if(hontai.zeiritsu.intValue() == 8){
						shiharaikingaku8Sum = shiharaikingaku8Sum.subtract(sashihikiKingaku);
						zeinukikingaku8Sum = zeinukikingaku8Sum.subtract(sashihikiZeinukiKingaku);
						shouhizeigaku8Sum = shouhizeigaku8Sum.subtract(sashihikiZeigaku);
					}else if(hontai.zeiritsu.intValue() == 10) {
						shiharaikingaku10Sum = shiharaikingaku10Sum.subtract(sashihikiKingaku);
						zeinukikingaku10Sum = zeinukikingaku10Sum.subtract(sashihikiZeinukiKingaku);
						shouhizeigaku10Sum = shouhizeigaku10Sum.subtract(sashihikiZeigaku);
					}
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
		super.getAndPrintInvoiceZei("A004", denpyouId, excel, sashihikiKingaku, sashihikiZeigaku);
		
		//■明細 (その他・経費)
		kaikeiXlsLogic.makeExcelShoukeiKeihi(meisaiKeihiList, excel, false, isInvoice);
		kaikeiXlsLogic.makeExcelMeisaiKeihi(meisaiKeihiList, excel, hontai.map, meisaiRow, meisaiCol);
		
		//■明細（日当・宿泊費等）
		kaikeiXlsLogic.makeExcelShoukei(RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, hontai.map, false, isInvoice, sashihikiZeigaku);
		int countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(RYOHISEISAN_SYUBETSU.SONOTA, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow02, meisaiRow02 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelKokunaiMeisai(RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, meisaiRow02, false);

		//■明細（交通費）
		kaikeiXlsLogic.makeExcelShoukei(RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, hontai.map, false, isInvoice);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow01, meisaiRow01 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelKokunaiMeisai(RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, meisaiRow01, false);

		//■仮払未使用なので明細行消す
		if ("".equals(shiharaihouhouCd)) {
			int meisaiStartRow = excel.getCell("meisai01").getRow();
			for (int i = 0; i < 20; i++) excel.getBook().getSheet(0).removeRow(meisaiStartRow);
		}
		
		//■申請内容「出張先・訪問先」行
		kaikeiXlsLogic.makeExcelShucchousakiLine(shucchousaki, shucchousakiCol, shucchousakiRow, shucchousakiTitle ,excel, book);
		excel.write("shiharaikiboubi_title", shiharaikiboubiTitle);
		excel.write("shiharaikiboubi", shiharaikiboubi);
		excel.write("shiharaihouhou_title", shiharaihouhouTitle);
		excel.write("shiharaihouhou", shiharaihouhou);
		
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
	
}



