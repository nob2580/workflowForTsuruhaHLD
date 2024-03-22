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
 * 仮払申請Logic
 */
public class KaribaraiShinseiXlsLogic extends EteamAbstractLogic {
	/**
	 * @param denpyouId 伝票ID
	 * @param out 出力先
	 * @param qrText QRコードURL
	 */ 
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();

	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KARIBARAI_SHINSEI);

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
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_karibarai.xls"), out);
		WritableWorkbook book = excel.getBook();
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
		GMap hontai = kaikeiLogic.findKaribarai(denpyouId);
		String hosoku = (String)hontai.get("hosoku");
		String shiharaihouhouCd = (String)hontai.get("shiharaihouhou");
		String shiharaihouhouName = (String)sysLogic.findNaibuCdSetting("shiharai_houhou", shiharaihouhouCd).get("name");
		GMap denpyouShubetuMap = workflowLogic.selectDenpyouShubetu(DENPYOU_KBN.KARIBARAI_SHINSEI);
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KARIBARAI_SHINSEI, hontai, null, "0");
		String version = "";
		int tekiyouByte = 0;
		version = Open21Env.Version.SIAS.toString();
		tekiyouByte = "SIAS".equals(version) ? 120 : 60;			
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki() :"";;
		boolean isKaribaraiAri = hontai.get("karibarai_on").equals("1");
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String torihikiTitle = "";
		String torihiki = "";
		String shiharaikiboubiTitle = "";
		String shiharaikiboubi = "";
		String shiharaihouhouTitle = "";
		String shiharaihouhou = "";
		String shiharaibiTitle = "";
		String shiharaibi = "";
		String karibaraionTitle = "";
		String karibaraion = "";
		String tekiyouTitle = "";
		String tekiyou = "";
		String kamokuTitle = "";
		String kamoku = "";
		String edabanTitle = "";
		String edaban = "";
		String futanbumonTitle = "";
		String futanbumon = "";
		String torihikisakiTitle = "";
		String torihikisaki = "";
		String projectTitle = "";
		String project = "";
		String segmentTitle = "";
		String segment = "";
		String shinseikingakuTitle = "";
		String shinseikingaku = "";
		String karibaraikingakuTitle = "";
		String karibaraikingaku = "";
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		if(hontai.get("karibarai_on").equals("0")){
			excel.write("denpyou_shubetsu", (String)denpyouShubetuMap.get("denpyou_print_karibarai_nashi_shubetsu"));
		}else{
			excel.write("denpyou_shubetsu", (String)denpyouShubetuMap.get("denpyou_print_shubetsu"));
		}
	
		//■申請内容
		//取引（仮払ありのとき記載）
		if(ks.torihiki.getPdfHyoujiFlg() && isKaribaraiAri){
			torihikiTitle = ks.torihiki.getName();
			if (hontai.get("shiwake_edano") != null)
			{
				torihiki = ks.torihiki.getCodeSeigyoValue(hontai.get("shiwake_edano").toString(), (String)hontai.get("torihiki_name"), " ");
			}
		}
		//支払希望日（仮払ありのとき記載）
		if(ks.shiharaiKiboubi.getPdfHyoujiFlg() == true  && isKaribaraiAri){
			shiharaikiboubiTitle = "支払" + ks.shiharaiKiboubi.getName();
			shiharaikiboubi = EteamXlsFmt.formatDate(hontai.get("shiharaikiboubi"));
		}
		//支払方法（仮払ありのとき記載）
		if(ks.shiharaiHouhou.getPdfHyoujiFlg() && isKaribaraiAri){
			shiharaihouhouTitle = "支払" + ks.shiharaiHouhou.getName();
			shiharaihouhou = shiharaihouhouName;
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
		//摘要（仮払ありのとき記載）
		if(ks.tekiyou.getPdfHyoujiFlg()  && isKaribaraiAri){
			tekiyouTitle = ks.tekiyou.getName();
			tekiyou = (String)hontai.get("tekiyou") + chuuki;
		}
		//支払日(支払日は記載固定)（仮払ありのとき記載）
		if(isKaribaraiAri){
			shiharaibiTitle = "支払日";
			shiharaibi = EteamXlsFmt.formatDate(hontai.get("shiharaibi"));
		}
		
		//仮払ありのとき記載
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
				project = ks.project.getCodeSeigyoValue((String)hontai.get("project_cd"),(String)hontai.get("project_name"), " ");
			}
			
			//セグメント
			if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
				segmentTitle = ks.segment.getName();
				segment = ks.segment.getCodeSeigyoValue((String)hontai.get("segment_cd"),(String)hontai.get("segment_name_ryakushiki"), " ");
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
		if(ks.kingaku.getPdfHyoujiFlg()){
			karibaraikingakuTitle = ks.kingaku.getName();
			karibaraikingaku = EteamXlsFmt.formatMoney(hontai.get("karibarai_kingaku"));
			excel.makeBorder("karibaraikingaku_title");
			excel.makeBorder("karibarai_kingaku");
		}
		
		//excelに各項目出力
		excel.write("torihiki_title", torihikiTitle );
		excel.write("torihiki", torihiki );
		excel.write("shiharaikiboubi_title", shiharaikiboubiTitle );
		excel.write("shiharai_kibou_bi", shiharaikiboubi );
		excel.write("shiharaihouhou_title", shiharaihouhouTitle );
		excel.write("shiharai_houhou", shiharaihouhou );
		excel.write("karibaraidenpyouflg_title",karibaraionTitle);
		excel.write("karibaraidenpyouflg", karibaraion);
		excel.write("tekiyou_title", tekiyouTitle );
		excel.write("tekiyou", tekiyou );
		excel.write("shiharaibi_title", shiharaibiTitle );
		excel.write("shiharai_bi", shiharaibi );
		excel.write("kamoku_title", kamokuTitle );
		excel.write("kamoku", kamoku );
		excel.write("edaban_title", edabanTitle );
		excel.write("edaban", edaban );
		excel.write("futanbumon_title", futanbumonTitle );
		excel.write("kari_futan_bumon_name", futanbumon );
		excel.write("torihikisaki_title", torihikisakiTitle );
		excel.write("torihikisaki", torihikisaki );
		excel.write("project_title", projectTitle );
		excel.write("project", project );
		excel.write("segment_title", segmentTitle );
		excel.write("segment", segment );
		excel.write("shinseikingaku_title", shinseikingakuTitle);
		excel.write("shinseikingaku", shinseikingaku);
		excel.write("karibaraikingaku_title", karibaraikingakuTitle );
		excel.write("karibarai_kingaku", karibaraikingaku );
		
// //負担部門が非表示かつ摘要2行以内(38文字)の場合は行詰め
// if(ks.futanBumon.getPdfHyoujiFlg() == false && 38 > tekiyou.length()){
// excel.hideRow(excel.getCell("futanbumon_title").getRow());
// }
		
		//■使用目的
		if(ks.hosoku.getPdfHyoujiFlg() == true){
			String hosokuTitle = ks.hosoku.getName();
			excel.write("siyoumokuteki_title", "■" + hosokuTitle );
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
			excel.write("siyoumokuteki_title","");
			excel.hideRow(excel.getCell("siyoumokuteki_title").getRow());
		}
				
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