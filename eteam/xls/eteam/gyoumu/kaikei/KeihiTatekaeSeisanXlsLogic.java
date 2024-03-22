package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
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
import eteam.common.select.WorkflowCategoryLogic;
import eteam.common.util.CollectionUtil;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;



/**
 * 経費立替精算Logic
 */
public class KeihiTatekaeSeisanXlsLogic extends EteamAbstractLogic {
	/**
	 * @param denpyouId 伝票ID
	 * @param out 出力先
	 * @param qrText QRコードURL
	 */ 

	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	/** 画面項目制御クラス(仮払) */
	GamenKoumokuSeigyo ksKari = new GamenKoumokuSeigyo(DENPYOU_KBN.KARIBARAI_SHINSEI);
	
	//消費税の定数
	//TODO グローバルにしたほうがいい気がする
	/** 現在の消費税率 */
	String shouhizeiNow = "10";
	/** 現在の軽減税率 */
	String shouhizeiKeigenNow = "8";
	
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
		KeihiTatekaeSeisanLogic keihitatekaeseisanLogic = EteamContainer.getComponent(KeihiTatekaeSeisanLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);
		WorkflowCategoryLogic workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		KaikeiCommonLogic kclogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		KiShouhizeiSettingDao kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		NaibuCdSettingDao naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		KaikeiCommonXlsLogic commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		InvoiceStartAbstractDao invoiceStart = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_keihitatekae.xls"), out);
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
		GMap hontai = kaikeiLogic.findKeihiSeisan(denpyouId);
		List<GMap>	meisaiList = kaikeiLogic.loadKeihiSeisanMeisai(denpyouId);
		String hosoku = hontai.get("hosoku");
		String shiharaihouhouCd = hontai.get("shiharaihouhou");
		String dairiFlg = hontai.get("dairiflg");
		GMap denpyouShubetuMap = workflowLogic.selectDenpyouShubetu(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
		boolean houjinCardRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean kaishatehaiRiyouFlg = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		boolean shiireHyoujiFlg = 1 == kiShouhizeiSettingDao.findByDate(null).shiireZeigakuAnbunFlg;
		boolean isInvoice = "0".equals(hontai.get("invoice_denpyou"));
		int tekiyouByte = "SIAS".equals(Open21Env.Version.SIAS.toString()) ? 120 : 60;	

		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String karibaraiOn = "";
		String karibaraidenpyouidTitle = "";
		String karibaraidenpyouid = "";
		String shiharaikiboubiTitle = "";
		String shiharaikiboubi = "";
		String shiharaihouhouTitle = "";
		String shiharaihouhou = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";
		String sashihikishikyuukingakuTitle = "";
		String sashihikishikyuukingaku = "";
		String houjinkingakuTitle = "";
		String houjinkingaku = "";
		String kaishatehaigoukeiTitle = "";
		String kaishatehaigoukei = "";
		String karibaraikingakuTitle = "";
		String karibaraikingaku = "";
		String shiharaikingakuGoukeiTitle = "";
		String shiharaikingakuGoukei = "";
		
		String hosokuTitle = "";
		
		String shiyoushaAndShiyoubiTitle = "";
		String torihikiTitle = "";
		String kamokuTitle = "";
		String tekiyouTitle = "";
		String futanbumonTitle = "";
		String kazeiTitle = "";
		String shiharaikingakuTitle = "";
		String bunriKbnTitle = "";
		
		BigDecimal shiharaikingaku8Sum = BigDecimal.ZERO;
		BigDecimal zeinukikingaku8Sum = BigDecimal.ZERO;
		BigDecimal shouhizeigaku8Sum = BigDecimal.ZERO; 
		BigDecimal shiharaikingaku10Sum = BigDecimal.ZERO;
		BigDecimal zeinukikingaku10Sum = BigDecimal.ZERO;
		BigDecimal shouhizeigaku10Sum = BigDecimal.ZERO;
		
		//非代理者なら自分ののみ参照
		@SuppressWarnings("deprecation")
		User userInfo = super.getUser();
		boolean allMeisaiView = keihitatekaeseisanLogic.judgeAllMeisaiView(denpyouId, userInfo, dairiFlg);
		
		/** 代理起票は明細から計算 */
		BigDecimal tmpShiharaikingaku;
		BigDecimal tmpShiharaikingakuGoukei = new BigDecimal(0);
		String user_id;
		GMap meisai;
		if(!allMeisaiView) {
			for (int i = 0; i < meisaiList.size(); i++) {
				meisai = meisaiList.get(i);
				user_id = (String)meisai.get("user_id");
				if(!userInfo.getSeigyoUserId().equals(user_id)) continue;
				tmpShiharaikingaku = new BigDecimal(meisai.get("shiharai_kingaku").toString());
				tmpShiharaikingakuGoukei = tmpShiharaikingakuGoukei.add(tmpShiharaikingaku);
			}
			shiharaikingakuGoukei = EteamXlsFmt.formatMoney(tmpShiharaikingakuGoukei);
			sashihikishikyuukingaku = shiharaikingakuGoukei; // 代理起票は差引支給金額と支払合計金額が同じになる。
		}
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", (String)denpyouShubetuMap.get("denpyou_print_shubetsu"));
		//インボイス処理開始後、かつ旧伝票（インボイス前伝票）の場合
		commonXlsLogic.beforeInvoice(hontai.get("invoice_denpyou"), excel);
		
		//■申請内容
		//仮払伝票
		if(ks1.karibaraiDenpyouId.getHyoujiFlg()){
			karibaraiOn = (String)hontai.get("karibarai_on");
		}
		if(ks1.karibaraiDenpyouId.getPdfHyoujiFlg()){
			if(karibaraiOn.equals("0")) {
				karibaraidenpyouidTitle = "伺い伝票ID";
			}else if(karibaraiOn.equals("1")) {
				karibaraidenpyouidTitle = "仮払" + ks1.karibaraiDenpyouId.getName();
			}
			
			if ("".equals(shiharaihouhouCd)) {
				karibaraidenpyouid = (String)hontai.get("karibarai_denpyou_id") + "(仮払金未使用)";
			} else {
				karibaraidenpyouid = (String)hontai.get("karibarai_denpyou_id");
			}
		}
		//支払希望日
		if(ks1.shiharaiKiboubi.getPdfHyoujiFlg()){
			shiharaikiboubiTitle = "支払" + ks1.shiharaiKiboubi.getName();
			shiharaikiboubi = EteamXlsFmt.formatDate(hontai.get("shiharaikiboubi"));
		}
		//支払方法
		if(ks1.shiharaiHouhou.getPdfHyoujiFlg()){
			shiharaihouhouTitle = "支払" + ks1.shiharaiHouhou.getName();
			if (!"".equals(shiharaihouhouCd)) {
				shiharaihouhou = (String)sysLogic.findNaibuCdSetting("shiharai_houhou", shiharaihouhouCd).get("name");
			}
		}
		//支払日(支払日は記載固定)
		shiharaibiTitle = "支払日";
		shiharaibi = EteamXlsFmt.formatDate(hontai.get("shiharaibi"));
		//差引支給金額
		if(ks1.sashihikiShikyuuKingaku.getPdfHyoujiFlg()){
			sashihikishikyuukingakuTitle = ks1.sashihikiShikyuuKingaku.getName();
			if(allMeisaiView) {
				sashihikishikyuukingaku = EteamXlsFmt.formatMoney(hontai.get("sashihiki_shikyuu_kingaku"));
			}
			excel.makeBorder("sasihikishikyuukingaku_title");
			excel.makeBorder("sashihiki_shikyuu_kingaku");
		}
		//法人カード利用
		if(houjinCardRiyouFlg && ks1.uchiHoujinCardRiyouGoukei.getPdfHyoujiFlg()) {
			houjinkingakuTitle = ks1.uchiHoujinCardRiyouGoukei.getName();
			houjinkingaku = EteamXlsFmt.formatMoney(hontai.get("houjin_card_riyou_kingaku"));
			//枠線を書く
			excel.makeBorder("houjincardkingakugoukei_title");
			excel.makeBorder("houjincardkingakugoukei");
		}
		//会社手配
		if(kaishatehaiRiyouFlg && ks1.kaishaTehaiGoukei.getPdfHyoujiFlg()){
			kaishatehaigoukeiTitle = ks1.kaishaTehaiGoukei.getName();
			kaishatehaigoukei = EteamXlsFmt.formatMoney(hontai.get("kaisha_tehai_kingaku"));
			//枠線をセット
			excel.makeBorder("kaishatehaigoukei_title");
			excel.makeBorder("kaishatehaigoukei");
			excel.makeBorder("kaishatehaigoukei2_title");
			excel.makeBorder("kaishatehaigoukei2");
		}
		//仮払金額
		if(! isEmpty(karibaraiOn)) {
			GMap karibaraikingakuMap = kaikeiLogic.findKaribarai((String)hontai.get("karibarai_denpyou_id"));
			if(karibaraiOn.equals("0") && ksKari.shinseiKingaku.getPdfHyoujiFlg()) {
				karibaraikingakuTitle = "伺い" + ksKari.shinseiKingaku.getName();
				karibaraikingaku = EteamXlsFmt.formatMoney(karibaraikingakuMap.get("kingaku"));
				excel.makeBorder("karibaraikingaku_title");
				excel.makeBorder("karibarai_kingaku");
			}
			if(karibaraiOn.equals("1") && ksKari.kingaku.getPdfHyoujiFlg()) {
				karibaraikingakuTitle = ksKari.kingaku.getName();
				karibaraikingaku = EteamXlsFmt.formatMoney(karibaraikingakuMap.get("karibarai_kingaku"));
				excel.makeBorder("karibaraikingaku_title");
				excel.makeBorder("karibarai_kingaku");
			}
		}
		//支払金額合計
		if(ks1.shiharaiKingakuGoukei.getPdfHyoujiFlg()){
			shiharaikingakuGoukeiTitle = ks1.shiharaiKingakuGoukei.getName();
			excel.makeBorder("shiharaikingakugoukei_title");
			if(allMeisaiView) {
				shiharaikingakuGoukei = EteamXlsFmt.formatMoney(hontai.get("shiharai_kingaku_goukei"));
			}
			excel.makeBorder("shiharai_kingaku_goukei");
		}
		//計上日
		//表示・非表示の判別はsetting_infoを参照
		if(!"3".equals(setting.shiwakeSakuseiHouhouA001())) {
			excel.write("keijoubi", EteamXlsFmt.formatDate(hontai.get("keijoubi")));
		} else {
			excel.write("keijoubi_title", "");
		}
		//金額詳細
		if(isInvoice && ks1.shiharaiKingakuGoukei.getPdfHyoujiFlg()) {
			//タイトル直書きなら不要？
			//　税率は明細リストから取得するしかない
			String shiharai10Title = "支払金額10%";
			String shiharai8Title = "支払金額*8%";
			String zeinuki10Title = "税抜金額10%";
			String zeinuki8Title = "税抜金額*8%";
			String shouhizei10Title = "消費税額10%";
			String shouhizei8Title = "消費税額*8%";

			excel.write("shiharai_kingaku_10_title", shiharai10Title);
			excel.write("shiharai_kingaku_8_title", shiharai8Title);
			excel.write("zeinuki_kingaku_10_title", zeinuki10Title);
			excel.write("zeinuki_kingaku_8_title", zeinuki8Title);
			excel.write("shouhizeigaku_10_title", shouhizei10Title);
			excel.write("shouhizeigaku_8_title", shouhizei8Title);
				
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
		}else {
			excel.hideRow(excel.getCell("shiharai_kingaku_10_title").getRow());
			excel.hideRow(excel.getCell("shiharai_kingaku_8_title").getRow());
		}
		//消費税額詳細
		commonXlsLogic.getAndPrintInvoiceZei(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,denpyouId,excel);
		if(!isInvoice) {
			excel.hideRow(excel.getCell("shouhizeigaku_shousai").getRow());
			excel.hideRow(excel.getCell("karibarai_shouhizei").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_menzei_80_title").getRow());
		}
		
		//excelに各項目出力
		excel.write("karibaraidenpyouid_title", karibaraidenpyouidTitle );
		excel.write("karibarai_denpyou_id", karibaraidenpyouid );
		excel.write("shiharaikiboubi_title", shiharaikiboubiTitle );
		excel.write("shiharaikiboubi", shiharaikiboubi );
		excel.write("shiharaihouhou_title", shiharaihouhouTitle );
		excel.write("shiharaihouhou", shiharaihouhou );
		excel.write("shiharaibi_title", shiharaibiTitle );
		excel.write("shiharaibi", shiharaibi );
		excel.write("sasihikishikyuukingaku_title", sashihikishikyuukingakuTitle );
		excel.write("sashihiki_shikyuu_kingaku", sashihikishikyuukingaku );
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
		excel.write("karibaraikingaku_title", karibaraikingakuTitle );
		excel.write("karibarai_kingaku", karibaraikingaku );
		excel.write("shiharaikingakugoukei_title", shiharaikingakuGoukeiTitle );
		excel.write("shiharai_kingaku_goukei", shiharaikingakuGoukei );
		
		//■補足
		if(ks1.hosoku.getPdfHyoujiFlg()){
			hosokuTitle = ks1.hosoku.getName();
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
		
		//■明細
		//使用者＋使用日
		if(ks1.userName.getPdfHyoujiFlg()) shiyoushaAndShiyoubiTitle = ks1.userName.getName();
		if(ks1.shiyoubi.getPdfHyoujiFlg())
			shiyoushaAndShiyoubiTitle += (0==shiyoushaAndShiyoubiTitle.length())? ks1.shiyoubi.getName() : "\n" + ks1.shiyoubi.getName();
		//取引
		if(ks1.torihiki.getPdfHyoujiFlg()) torihikiTitle = ks1.torihiki.getName();
		//科目
		if(ks1.kamoku.getPdfHyoujiFlg()) kamokuTitle = ks1.kamoku.getName();
		if(ks1.kamokuEdaban.getPdfHyoujiFlg())
			kamokuTitle += (0==kamokuTitle.length())? ks1.kamokuEdaban.getName() : "\n" + ks1.kamokuEdaban.getName();
		//摘要(＋支払先名＋事業者区分)
		List<String> tekiyouTitleList = new ArrayList<>();
		if(ks1.tekiyou.getPdfHyoujiFlg()) 
			tekiyouTitleList.addAll(excel.splitLine(ks1.tekiyou.getName(), 14));
		if(isInvoice&&ks1.shiharaisaki.getPdfHyoujiFlg())
			tekiyouTitleList.addAll(excel.splitLine(ks1.shiharaisaki.getName(), 14));
		if(isInvoice&&ks1.jigyoushaKbn.getPdfHyoujiFlg())
			tekiyouTitleList.addAll(excel.splitLine(ks1.jigyoushaKbn.getName(), 14));
		tekiyouTitle = String.join("\n", tekiyouTitleList);
		int tekiyouLength = 0;
		String[] tekiyouArr = tekiyouTitle.split("\n");
		for (int i = 0; i < tekiyouArr.length; i++) {
			tekiyouLength += excel.lineCount(tekiyouArr[i], 14, 1);
		}
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
		//高さ調整：使用者は１行６文字以内、負担部門は１行７文字以内、課税区分は１行２文字以内で計算。
		int shiyouNum = 	excel.lineCount(shiyoushaAndShiyoubiTitle, 12, 2);
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
		int kazeiLineNum =	excel.lineCount(kazeiTitle, 4, 2);
		//支払金額
		List<String> kingakuTitleList = new ArrayList<>();
		if(ks1.shiharaiKingaku.getPdfHyoujiFlg()) /*shiharaikingakuTitle = ks1.shiharaiKingaku.getName();*/
			kingakuTitleList.addAll(excel.splitLine(ks1.shiharaiKingaku.getName(), 14));
		if(isInvoice && ks1.shouhizeigaku.getPdfHyoujiFlg())
			kingakuTitleList.addAll(excel.splitLine(ks1.shouhizeigaku.getName(), 14));
		shiharaikingakuTitle = String.join("\n", kingakuTitleList);
		String[] shiharaikingakuArr = shiharaikingakuTitle.split("\n");
		int shiharaikingakuLength = 0;
		for (int i = 0; i < shiharaikingakuArr.length; i++) {
			shiharaikingakuLength += excel.lineCount(shiharaikingakuArr[i], 14, 1);
		}
		
		//分離区分＋仕入区分
		List<String> bunriKbnTitleList = new ArrayList<>();
		if(ks1.bunriKbn.getPdfHyoujiFlg()) {
			bunriKbnTitleList.addAll(excel.splitLine(ks1.bunriKbn.getName(), 14));
		}
		if(ks1.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
			bunriKbnTitleList.addAll(excel.splitLine(ks1.shiireKbn.getName(), 14));
		}
		bunriKbnTitle = String.join("\n", bunriKbnTitleList);
		String[] bunriKbnArr = bunriKbnTitle.split("\n");
		int bunriKbnLength = 0;
		for (int i = 0; i < bunriKbnArr.length; i++) {
			bunriKbnLength += excel.lineCount(bunriKbnArr[i], 14, 1);
		}
		
		if (kamokuLength >= 2 || tekiyouLength >= 2 || futanbumonLength >= 2 ||  kazeiLineNum >= 2 || shiyouNum >= 2 || shiharaikingakuLength >= 2 || bunriKbnLength >= 2) {
			excel.setHeight(meisaikoumokuRow, wfXlsLogic.findMaxInt(kamokuLength, tekiyouLength, futanbumonLength, kazeiLineNum, shiyouNum,shiharaikingakuLength, bunriKbnLength), 1);
		}
		
		excel.write("shiyousha_title", shiyoushaAndShiyoubiTitle);
		excel.write("torihiki_title", torihikiTitle);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("tekiyou_title", tekiyouTitle);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("kazei_title", kazeiTitle);
		excel.write("shiharaikingaku_title", shiharaikingakuTitle);
		excel.write("bunri_title", bunriKbnTitle);
		if(!ks1.zeiritsu.getPdfHyoujiFlg()) {
			excel.write("zeiritsu_title", "");
		}
		if(ks1.kousaihiShousai.getPdfHyoujiFlg()) {
			excel.write("kousaihi_title", ks1.kousaihiShousai.getName());
		} else {
			excel.hideRow(excel.getCell("kousaihi_title").getRow());
		}
		
		//明細テンプレート行を明細件数分コピー
		int copyKaisu = 0;
		for (int i = 0; i < meisaiList.size(); i++) {
			meisai = meisaiList.get(i);
			user_id = (String)meisai.get("user_id");
			if( !allMeisaiView &&  !userInfo.getSeigyoUserId().equals(user_id)) continue;
			copyKaisu++;
		}
		if (1 < copyKaisu) excel.copyRow(meisaiRow, meisaiRow + 2, 2, copyKaisu - 1, true);
		
		//先に警告リストだけ取得
		List<GMap> keikokuList = new ArrayList<GMap>();
		if(meisaiList.size() > 0) {
			String[] shiwakeEdanoCd  	= CollectionUtil.toArrayStr(meisaiList, "shiwake_edano");
			String[] kousaihiHitoriKingaku = CollectionUtil.toArrayStr(meisaiList, "kousaihi_hitori_kingaku");
			String[] kaigaiFlg  	= CollectionUtil.makeArrayStr("0", meisaiList.size());
			keikokuList = kclogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, shiwakeEdanoCd, kousaihiHitoriKingaku, kaigaiFlg, kclogic.KOUSAI_ERROR_CD_KEIKOKU);
		}
		
		//1行ずつ書き込み
		int row = 0; //出力相対位置（行）
		for (int i= 0; i < meisaiList.size(); i++) {
			meisai = meisaiList.get(i);
			user_id = (String)meisai.get("user_id");
			if( !allMeisaiView &&  !userInfo.getSeigyoUserId().equals(user_id)) continue;
			
			//注記
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, hontai, meisai, "0");
			String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
			
			String shiyousha    = (String)meisai.get("user_sei") + "　" + (String)meisai.get("user_mei");
			String shiyoubi     = EteamXlsFmt.formatDate(meisai.get("shiyoubi"));
			String torihiki     = "";
			String kamoku       = ks1.kamoku.getCodeSeigyoValue((String)meisai.get("kari_kamoku_cd"),(String)meisai.get("kari_kamoku_name"), "\n");
			String kamokuEdaban = ks1.kamokuEdaban.getCodeSeigyoValue((String)meisai.get("kari_kamoku_edaban_cd"),(String)meisai.get("kari_kamoku_edaban_name"), "\n");
			String tekiyou      = "";
			String futanBumonName = "";
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", (String)meisai.get("kari_kazei_kbn"));
			String zeiritsu = EteamXlsFmt.formatZeiritsu(meisai.get("zeiritsu"), meisai.get("keigen_zeiritsu_kbn"));
			GMap kazeiInfo = sysLogic.findNaibuCdSetting("kazei_kbn", (String)meisai.get("kari_kazei_kbn"));
			String kazeiKbnGroup = (null != kazeiInfo)? (String)kazeiInfo.get("option1") : "";
			boolean zeiritsuHyoujiFlg = (kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI) || kazeiKbnGroup.equals(EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI))? true : false;
			String shiharaikingaku = EteamXlsFmt.formatMoney(meisai.get("shiharai_kingaku"));
			String bunriKbn = "";
			
			//No
			excel.write("meisai_no", 0, row, Integer.toString(i+1));
			//使用者(＋使用日）
			StringBuffer shiyoushaBuf = new StringBuffer();
			if(ks1.userName.getPdfHyoujiFlg()) shiyoushaBuf.append(shiyousha);
			if(ks1.shiyoubi.getPdfHyoujiFlg()){
				if(0!=shiyoushaBuf.length()) shiyoushaBuf.append("\n");
				shiyoushaBuf.append(shiyoubi);
			}
			excel.write("shiyousha", 0, row, shiyoushaBuf.toString());
			//取引
			if(ks1.torihiki.getPdfHyoujiFlg()){
				torihiki = ks1.torihiki.getCodeSeigyoValue(meisai.get("shiwake_edano").toString(), (String)meisai.get("torihiki_name"), "\n");
				excel.write("torihiki", 0, row, torihiki);
			}
			//科目
			StringBuffer kamokuBuf = new StringBuffer();
			if(ks1.kamoku.getPdfHyoujiFlg()) kamokuBuf.append(kamoku);
			if(ks1.kamokuEdaban.getPdfHyoujiFlg()){
				if(0!=kamokuBuf.length()) kamokuBuf.append("\n");
				kamokuBuf.append(kamokuEdaban);
			}
			excel.write("kamoku", 0, row, kamokuBuf.toString());
			//摘要
			List<String> tekiyouNameList = new ArrayList<>();
			if(ks1.tekiyou.getPdfHyoujiFlg()){
				tekiyouNameList.add((String)meisai.get("tekiyou") + chuuki);
			}
			//支払先
			if(isInvoice && ks1.shiharaisaki.getPdfHyoujiFlg()) {
				String shiharasiakiNameStr = (String)meisai.get("shiharaisaki_name");
				tekiyouNameList.add(shiharasiakiNameStr);
			}
			//事業者区分
			if(isInvoice && ks1.jigyoushaKbn.getPdfHyoujiFlg()) {
				String jigyoushaKbnNum = (String)meisai.get("jigyousha_kbn");
				String jigyoushaKbnStr = naibuCdSettingDao.find("jigyousha_kbn",jigyoushaKbnNum).name;
				tekiyouNameList.add(jigyoushaKbnStr);
			}
			tekiyou =  String.join("\n", tekiyouNameList);
			excel.write("meisai_tekiyou", 0, row, tekiyou);
			//負担部門(＋取引先＋プロジェクト)
			List<String> futanbumonNameList = new ArrayList<>();
			if(ks1.futanBumon.getPdfHyoujiFlg())
				futanbumonNameList.add(ks1.futanBumon.getCodeSeigyoValue((String)meisai.get("kari_futan_bumon_cd"), (String)meisai.get("kari_futan_bumon_name"),"\n"));
			if(ks1.torihikisaki.getPdfHyoujiFlg())
				futanbumonNameList.add(ks1.torihikisaki.getCodeSeigyoValue((String)meisai.get("torihikisaki_cd"), (String)meisai.get("torihikisaki_name_ryakushiki"),"\n"));
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks1.project.getPdfHyoujiFlg())
				futanbumonNameList.add(ks1.project.getCodeSeigyoValue((String)meisai.get("project_cd"), (String)meisai.get("project_name"),"\n"));
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks1.segment.getPdfHyoujiFlg())
				futanbumonNameList.add(ks1.segment.getCodeSeigyoValue((String)meisai.get("segment_cd"), (String)meisai.get("segment_name_ryakushiki"),"\n"));

			futanBumonName = String.join("\n", futanbumonNameList);
			excel.write("meisai_futan_bumon", 0, row, futanBumonName);
			//課税区分
			if(ks1.kazeiKbn.getPdfHyoujiFlg()){
				excel.write("kazei", 0, row, kazei == null ? "" : kazei);
			}
			//税率
			if(ks1.zeiritsu.getPdfHyoujiFlg() && zeiritsuHyoujiFlg){
				excel.write("zeiritsu", 0, row, zeiritsu);
			}
			//分離区分（＋仕入区分）
			List<String> bunriKbnList = new ArrayList<>();
			if(ks1.bunriKbn.getPdfHyoujiFlg()) {
				if (meisai.get("bunri_kbn") != null) {
					if (!meisai.get("bunri_kbn").toString().isEmpty()) {
						bunriKbnList.add(naibuCdSettingDao.find("bunri_kbn",meisai.get("bunri_kbn")).name);
					}
				}
			}
			if( ks1.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
				if (meisai.get("kari_shiire_kbn") != null) {
					if (!meisai.get("kari_shiire_kbn").toString().isEmpty()) {
						bunriKbnList.add(naibuCdSettingDao.find("shiire_kbn",meisai.get("kari_shiire_kbn")).name);
					}
				}
			}
			bunriKbn = String.join("\n", bunriKbnList);
			excel.write("bunri", 0, row, bunriKbn);
			
			//支払金額（＋消費税額）
			List<String> kingakuNameList = new ArrayList<>();
			if(ks1.shiharaiKingaku.getPdfHyoujiFlg()) {
				kingakuNameList.add(EteamXlsFmt.formatMoney(meisai.get("shiharai_kingaku")));
			}
			if(isInvoice && ks1.shouhizeigaku.getPdfHyoujiFlg()) {
				kingakuNameList.add(EteamXlsFmt.formatMoney(meisai.get("shouhizeigaku")));
			}
			
			shiharaikingaku = String.join("\n", kingakuNameList);
			excel.write("meisai_shiharai_kingaku", 0, row, shiharaikingaku);
			
			//明細ループで支払金額・税抜金額・消費税額の税率別合計を取得
			String zeiritsucheck = EteamXlsFmt.formatMoney(meisai.get("zeiritsu"));
			BigDecimal shiharaikingaku1 = meisai.get("shiharai_kingaku") == null ? BigDecimal.ZERO : meisai.get("shiharai_kingaku");
			BigDecimal zeinukikingaku = meisai.get("hontai_kingaku") == null ? BigDecimal.ZERO : meisai.get("hontai_kingaku");
			BigDecimal shouhizeigaku1 = meisai.get("shouhizeigaku") == null ? BigDecimal.ZERO : meisai.get("shouhizeigaku");
			if(shouhizeiNow.equals(zeiritsucheck)) {
				shiharaikingaku10Sum = shiharaikingaku10Sum.add(shiharaikingaku1);
				zeinukikingaku10Sum = zeinukikingaku10Sum.add(zeinukikingaku);
				shouhizeigaku10Sum = shouhizeigaku10Sum.add(shouhizeigaku1);
			}
			if(shouhizeiKeigenNow.equals(zeiritsucheck)) {
				shiharaikingaku8Sum = shiharaikingaku8Sum.add(shiharaikingaku1);
				zeinukikingaku8Sum = zeinukikingaku8Sum.add(zeinukikingaku);
				shouhizeigaku8Sum = shouhizeigaku8Sum.add(shouhizeigaku1);
			}
			
			//法人カード利用
			if (houjinCardRiyouFlg && ks1.houjinCardRiyou.getPdfHyoujiFlg()) {
				excel.write("meisai_houjincardriyou", 0, row, "1".equals(meisai.get("houjin_card_riyou_flg")) ? "C" : "");
			}
			//会社手配
			if(kaishatehaiRiyouFlg && ks1.kaishaTehai.getPdfHyoujiFlg()){
				if("1".equals(meisai.get("kaisha_tehai_flg"))) {
					excel.write("meisai_houjincardriyou", 0, row, "K");
				}
			}

			//高さ調整：明細の取引、科目、摘要、負担部門は1行7文字以内で計算。
			//デフォルト２行だけどそれ以上の行数になるなら高さ変える。
			int shiyouLineNum = excel.lineCount(shiyoushaBuf.toString(), 10, 2);
			int torihikiLineNum = excel.lineCount(torihiki, 14, 2);
			int kamokuLineNum = excel.lineCount(kamokuBuf.toString(), 14, 2);
			int futanbumonLineNum = excel.lineCount(futanBumonName, 14, 2);
			int tekiyouLineNum = excel.lineCount(tekiyou, 12, 2);
			int kakuchouLineNum = wfXlsLogic.findMaxInt(shiyouLineNum, torihikiLineNum, kamokuLineNum, futanbumonLineNum, tekiyouLineNum);
			excel.setHeight(meisaiRow+row,  kakuchouLineNum, 2);
			
			//交際費詳細
			if(ks1.kousaihiShousai.getPdfHyoujiFlg() && "1".equals(meisai.get("kousaihi_shousai_hyouji_flg").toString())){
				String kousaihi = meisai.get("kousaihi_shousai").toString();
				if( meisai.get("kousaihi_ninzuu") != null) {
					String kousaihiNinzuu = meisai.get("kousaihi_ninzuu") == null ? "" : ((Integer)meisai.get("kousaihi_ninzuu")).toString() + "名";
					excel.write("kousaihi_ninzuu", 0, row, kousaihiNinzuu);
				}
				if( meisai.get("kousaihi_hitori_kingaku") != null) {
					String kousaihiHitoriKingaku = meisai.get("kousaihi_hitori_kingaku") == null ? "" : (EteamXlsFmt.formatMoney(meisai.get("kousaihi_hitori_kingaku"))) + "円";
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
				
				int kousaihiLineNum = excel.lineCount(kousaihi, 55, 2); //1行が文字数丁度の場合、excel側で改行を入れられてしまうため1文字少なくカウント
				excel.setHeight(meisaiRow+row+1,  kousaihiLineNum, 2);
			} else {
				excel.hideRow(meisaiRow+row+1);
				//行番号の枠線を再セット
				excel.encloseInBorder(meisaiRow+row, meisaiCol, 1);
			}
			row+=2;
		}
		
		//ループで取得していた各合計を出力
		if(isInvoice && ks1.shiharaiKingakuGoukei.getPdfHyoujiFlg()) {
			excel.write("shiharai_kingaku_10", EteamXlsFmt.formatMoney(shiharaikingaku10Sum));
			excel.write("zeinuki_kingaku_10", EteamXlsFmt.formatMoney(zeinukikingaku10Sum));
			excel.write("shouhizeigaku_10", EteamXlsFmt.formatMoney(shouhizeigaku10Sum));
			excel.write("shiharai_kingaku_8", EteamXlsFmt.formatMoney(shiharaikingaku8Sum));
			excel.write("zeinuki_kingaku_8", EteamXlsFmt.formatMoney(zeinukikingaku8Sum));
			excel.write("shouhizeigaku_8", EteamXlsFmt.formatMoney(shouhizeigaku8Sum));
		}
		
		//■仮払未使用なので明細行消す
		if ("".equals(shiharaihouhouCd)) {
			int meisaiStartRow = excel.getCell("meisai").getRow();
			for (int i = 0; i < 4; i++) excel.getBook().getSheet(0).removeRow(meisaiStartRow);
		}
		
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