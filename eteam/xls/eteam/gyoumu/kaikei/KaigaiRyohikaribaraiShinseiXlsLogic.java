package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 海外旅費仮払申請Logic
 */
public class KaigaiRyohikaribaraiShinseiXlsLogic extends EteamAbstractLogic {
	/**
	 * @param denpyouId 伝票ID
	 * @param out 出力先
	 * @param qrText QRコードURL
	 */
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI); 
	/** 画面項目制御クラス(経費明細) */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	
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
		WorkflowCategoryLogic workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		KaikeiCommonXlsLogic kaikeiXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		// 経費明細の以下は強制未使用
		ks1.houjinCardRiyou = null;
		ks1.kaishaTehai = null;
		kaikeiXlsLogic.init(ks,ks1);

		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_kaigairyohikaribaraishinsei.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell shucchousakiCell = book.findCellByName("shucchousaki");
		int shucchousakiRow = shucchousakiCell.getRow();
		int shucchousakiCol = shucchousakiCell.getColumn();
		Cell meisaiCell11 = book.findCellByName("meisai_no11");
		int meisaiRow11 = meisaiCell11.getRow();
		Cell meisaiCell12 = book.findCellByName("meisai_no12");
		int meisaiRow12 = meisaiCell12.getRow();
		Cell meisaiCell01 = book.findCellByName("meisai_no01");
		int meisaiRow01 = meisaiCell01.getRow();
		Cell meisaiCell02 = book.findCellByName("meisai_no02");
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
		GMap hontai = kaikeiLogic.findKaigaiRyohiKaribarai(denpyouId); //kaigai_ryohikaribarai
		List<GMap> 	meisaiList = kaikeiLogic.loadKaigaiRyohiKaribaraiMeisai(denpyouId); //kaigai_ryohikaribarai_meisai
		List<GMap> 	meisaiKeihiList = kaikeiLogic.loadKaigaiRyohiKeihiKaribaraiMeisai(denpyouId); //kaigai_ryohikaribarai_keihi_meisai
		String hosoku = (String)hontai.get("hosoku");
		GMap denpyouShubetuMap = workflowLogic.selectDenpyouShubetu(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
		String shiharaihouhouCd = (String)hontai.get("shiharaihouhou");
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, hontai, null, "0");
		String version = "";
		int tekiyouByte = 0;
		version = Open21Env.Version.SIAS.toString();
		tekiyouByte = "SIAS".equals(version) ? 120 : 60;			
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";
		boolean isKaribaraiAri = hontai.get("karibarai_on").equals("1");
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String shiyoushaTitle = "";
		String shiyousha = "";
		String torihikiTitle = "";
		String torihiki = "";
		String seisankikanTitle = "";
		String seisankikan = "";
		String shucchousakiTitle = "";
		String shucchousaki = "";
		String shiharaikiboubiTitle = "";
		String shiharaikiboubi = "";
		String shiharaihouhouTitle = "";
		String shiharaihouhouName = "";
		String mokutekiTitle = "";
		String mokuteki = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";
		String kamokuTitle = "";
		String kamoku = "";
		String edabanTitle = "";
		String edaban = "";
		String karibaraionTitle = "";
		String karibaraion = "";
		String futanbumonTitle = "";
		String futanbumon = "";
		String torihikisakiTitle = "";
		String torihikisaki = "";
		String projectTitle = "";
		String project = "";
		String segmentTitle = "";
		String segment = "";
		String tekiyouTitle = "";
		String tekiyou = "";
		String shinseikingakuTitle = "";
		String shinseikingaku = "";
		String karibaraikingakuTitle = "";
		String karibaraikingaku = "";
		String hosokutitle = "";
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		if(hontai.get("karibarai_on").equals("0")){
			excel.write("denpyou_shubetsu", (String)denpyouShubetuMap.get("denpyou_print_karibarai_nashi_shubetsu"));
		}
		else {
			excel.write("denpyou_shubetsu", (String)denpyouShubetuMap.get("denpyou_print_shubetsu"));
		}
		
		//■申請内容
		// 使用者
		if(ks.userName.getPdfHyoujiFlg() == true){
			shiyoushaTitle =  ks.userName.getName();
			shiyousha = (String)hontai.get("user_sei") + "　" + (String)hontai.get("user_mei");
		}
		//取引
		if(ks.torihiki.getPdfHyoujiFlg() == true && isKaribaraiAri){
			torihikiTitle = ks.torihiki.getName();
			if (hontai.get("shiwake_edano") != null)
			{
				torihiki = ks.torihiki.getCodeSeigyoValue(hontai.get("shiwake_edano").toString(), (String)hontai.get("torihiki_name"), " ");
			}
		}
		//精算期間&精算時刻
		if(ks.seisankikan.getPdfHyoujiFlg() == true && ks.seisankikanJikoku.getPdfHyoujiFlg() == true){
			seisankikanTitle = ks.seisankikan.getName();
			seisankikan = EteamXlsFmt.formatDate(hontai.get("seisankikan_from")) + " " 
						+ EteamXlsFmt.formatTimeHHmm(hontai.get("seisankikan_from_hour"), hontai.get("seisankikan_from_min"))
						+ " ～ " + EteamXlsFmt.formatDate(hontai.get("seisankikan_to")) + " " 
						+ EteamXlsFmt.formatTimeHHmm(hontai.get("seisankikan_to_hour"), hontai.get("seisankikan_to_min"));
		//精算期間のみ
		} else if (ks.seisankikan.getPdfHyoujiFlg() == true) {
			seisankikanTitle = ks.seisankikan.getName();
			seisankikan = EteamXlsFmt.formatDate(hontai.get("seisankikan_from")) + "～" + EteamXlsFmt.formatDate(hontai.get("seisankikan_to"));
		}
		//出張先・訪問先
		if(ks.houmonsaki.getPdfHyoujiFlg() == true){
			shucchousakiTitle = ks.houmonsaki.getName();
			shucchousaki = (String)hontai.get("houmonsaki");
		}
		//支払希望日(仮払ありのとき記載)
		if(ks.shiharaiKiboubi.getPdfHyoujiFlg() == true && isKaribaraiAri){
			shiharaikiboubiTitle = "支払" + ks.shiharaiKiboubi.getName();
			shiharaikiboubi = EteamXlsFmt.formatDate(hontai.get("shiharaikiboubi"));
		}
		//支払方法(仮払ありのとき記載)
		if (ks.shiharaiHouhou.getPdfHyoujiFlg() && isKaribaraiAri) {
			shiharaihouhouTitle = "支払" + ks.shiharaiHouhou.getName();
			shiharaihouhouName = (String)sysLogic.findNaibuCdSetting("shiharai_houhou", shiharaihouhouCd).get("name");
		}
		//目的
		if(ks.mokuteki.getPdfHyoujiFlg() == true){
			mokutekiTitle = ks.mokuteki.getName();
			mokuteki = (String)hontai.get("mokuteki");
		}
		//支払日(仮払ありのとき記載)
		if (isKaribaraiAri) {
			shiharaibiTitle = "支払日";
			shiharaibi = EteamXlsFmt.formatDate(hontai.get("shiharaibi"));
		}
		//仮払（あり/なし）
		if(ks.karibaraiOn.getPdfHyoujiFlg() == true){
			karibaraionTitle = ks.karibaraiOn.getName();
			if(isKaribaraiAri){
				karibaraion = "あり";
			} else {
				karibaraion = "なし";
			}
		}
		
		// 仮払ありのとき記載
		if (isKaribaraiAri)
		{
			//科目
			if(ks.kamoku.getPdfHyoujiFlg()){
				kamokuTitle = ks.kamoku.getName();
				kamoku = ks.kamoku.getCodeSeigyoValue((String)hontai.get("kari_kamoku_cd"),(String)hontai.get("kari_kamoku_name"), " ");
			}
			//科目枝番
			if(ks.kamokuEdaban.getPdfHyoujiFlg()){
				edabanTitle = ks.kamokuEdaban.getName();
				edaban = ks.kamokuEdaban.getCodeSeigyoValue((String)hontai.get("kari_kamoku_edaban_cd"),(String)hontai.get("kari_kamoku_edaban_name"), " ");
			}
			
			//負担部門
			if(ks.futanBumon.getPdfHyoujiFlg()){
				futanbumonTitle = ks.futanBumon.getName();
				futanbumon = ks.futanBumon.getCodeSeigyoValue((String)hontai.get("kari_futan_bumon_cd"),(String)hontai.get("kari_futan_bumon_name"), " ");
			}
			//取引先
			if(ks.torihikisaki.getPdfHyoujiFlg()){
				torihikisakiTitle = ks.torihikisaki.getName();
				torihikisaki = ks.torihikisaki.getCodeSeigyoValue((String)hontai.get("torihikisaki_cd"),(String)hontai.get("torihikisaki_name_ryakushiki"), " ");
			}
			//プロジェクト
			if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
				projectTitle = ks.project.getName();
				project = ks.project.getCodeSeigyoValue((String)hontai.get("project_cd"), (String)hontai.get("project_name"), " ");
			}
			//セグメント
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
				segmentTitle = ks.segment.getName();
				segment = ks.segment.getCodeSeigyoValue((String)hontai.get("segment_cd"), (String)hontai.get("segment_name_ryakushiki"), " ");
			}
			//摘要
			if (ks.tekiyou.getPdfHyoujiFlg()) {
				tekiyou = (String)hontai.get("tekiyou") + chuuki;
				tekiyouTitle = ks.tekiyou.getName();
			}
		}
		
		//申請金額
		if(ks.shinseiKingaku.getPdfHyoujiFlg() == true){
			shinseikingakuTitle = ks.shinseiKingaku.getName();
			shinseikingaku =  EteamXlsFmt.formatMoney(hontai.get("kingaku"));
			excel.makeBorder("shinseikingaku_title");
			excel.makeBorder("shinseikingaku");
		}
		//仮払金額
		if(ks.karibaraiKingaku.getPdfHyoujiFlg()){
			karibaraikingakuTitle = ks.karibaraiKingaku.getName();
			if ("1".equals(hontai.get("karibarai_on"))){
				karibaraikingaku =  EteamXlsFmt.formatMoney(hontai.get("karibarai_kingaku"));
			}
			excel.makeBorder("karibaraikingaku_title");
			excel.makeBorder("karibaraikingaku");
		}
		
		//excelに各項目出力(「出張先・訪問先」行は改行要素があるため後述)
		excel.write("shiyousha_title", shiyoushaTitle );
		excel.write("shiyousha", shiyousha);
		excel.write("torihiki_title", torihikiTitle );
		excel.write("torihiki", torihiki);
		excel.write("seisankikan_title", seisankikanTitle);
		excel.write("seisankikan", seisankikan);
		excel.write("mokuteki_title", mokutekiTitle);
		excel.write("mokuteki", mokuteki);
		excel.write("shiharaibi_title", shiharaibiTitle);
		excel.write("shiharaibi", shiharaibi);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("kamoku", kamoku);
		excel.write("edaban_title", edabanTitle);
		excel.write("edaban", edaban);
		excel.write("karibaraidenpyouflg_title", karibaraionTitle);
		excel.write("karibaraidenpyouflg", karibaraion);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("futanbumon", futanbumon);
		excel.write("torihikisaki_title", torihikisakiTitle );
		excel.write("torihikisaki", torihikisaki );
		excel.write("project_title", projectTitle);
		excel.write("project", project);
		excel.write("segment_title", segmentTitle );
		excel.write("segment", segment );
		excel.write("tekiyou_title", tekiyouTitle);
		excel.write("tekiyou", tekiyou);
		excel.write("shinseikingaku_title", shinseikingakuTitle);
		excel.write("shinseikingaku", shinseikingaku);
		excel.write("karibaraikingaku_title", karibaraikingakuTitle);
		excel.write("karibaraikingaku", karibaraikingaku);

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
		
		//■明細 (その他・経費) 
		kaikeiXlsLogic.makeExcelShoukeiKeihi(meisaiKeihiList, excel, true, false);
		kaikeiXlsLogic.makeExcelMeisaiKeihiforKaigai(meisaiKeihiList, excel, hontai, meisaiRow, meisaiCol);
		
		
		//■明細（日当・宿泊費等）：国内
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, hontai, true, false);
		int countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow02, meisaiRow02 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, meisaiRow02, true);
		
		//■明細（交通費）：国内
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, hontai, true, false);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow01, meisaiRow01 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KOKUNAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, meisaiRow01, true);
		
		//■明細（日当・宿泊費等）：海外
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, hontai, true, false);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow12, meisaiRow12 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.SONOTA, meisaiList, excel, meisaiRow12, true);
		
		//■明細（交通費）：海外
		kaikeiXlsLogic.makeExcelShoukei(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, hontai, true, false);
		countMeisaiShubetsu = kaikeiXlsLogic.courntMeisaiShubetsuCd(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList);
		if (1 < countMeisaiShubetsu) excel.copyRow(meisaiRow11, meisaiRow11 + 1, 1, countMeisaiShubetsu - 1, true);
		kaikeiXlsLogic.makeExcelMeisai(SHUCCHOU_KAIGAI, RYOHISEISAN_SYUBETSU.KOUTSUUHI, meisaiList, excel, meisaiRow11, true);
		
		//■申請内容「出張先・訪問先」行
		kaikeiXlsLogic.makeExcelShucchousakiLine(shucchousaki, shucchousakiCol, shucchousakiRow, shucchousakiTitle, excel, book);
		excel.write("shiharaikiboubi_title", shiharaikiboubiTitle);
		excel.write("shiharaikiboubi", shiharaikiboubi);
		excel.write("shiharaihouhou_title", shiharaihouhouTitle);
		excel.write("shiharaihouhou", shiharaihouhouName);
		
		// ---------------------------------------
		//共通部分
		// ---------------------------------------
		wfXlsLogic.makeExcel(excel, denpyouId, false);
		//仮払なしの場合、稟議金額・稟議金額残高を非表示にする
		if(!isKaribaraiAri){
			excel.hideRow(excel.getCell("ringi_kingaku_title").getRow());
		}
		
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
	
}
