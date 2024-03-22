package eteam.gyoumu.kaikei;

import java.io.OutputStream;

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
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;
import eteam.database.abstractdao.TsuukinteikiAbstractDao;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.TsuukinteikiDao;
import eteam.database.dto.Tsuukinteiki;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 通勤定期申請Logic
 */
public class TsuukinteikiShinseiXlsLogic extends KaikeiCommonXlsLogic {
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI);
	
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
		WorkflowCategoryLogic workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		TsuukinteikiAbstractDao tsuukinteikiDao = EteamContainer.getComponent(TsuukinteikiDao.class, connection);
		DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
		KaikeiCommonXlsLogic commonXlsLogic = EteamContainer.getComponent(KaikeiCommonXlsLogic.class, connection);
		KiShouhizeiSettingDao kiShouhizei = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_tsuukinteikishinsei.xls"), out);
		WritableWorkbook book = excel.getBook();
		Cell jyousha_kukanCell = book.findCellByName("jyousha_kukan");
		int jyousha_kukanRow = jyousha_kukanCell.getRow();
		
		// ---------------------------------------
		//共通部分（下部）
		// ---------------------------------------
		wfXlsLogic.makeExcelBottom(excel, denpyouId);
		
		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		Tsuukinteiki hontai = tsuukinteikiDao.find(denpyouId);
		String jyousha_kukan = hontai.jyoushaKukan;
		var denpyouShubetuMap = denpyouShubetsuIchiranDao.find(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI);
		String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, hontai.map, null, "0");
		// 通勤定期申請のみ摘要の出力がないので、頭の改行文字を削除
		String version = "";
		int tekiyouByte = 0;
		version = Open21Env.Version.SIAS.toString();
		tekiyouByte = "SIAS".equals(version) ? 120 : 60;			
		String chuuki = (tekiyouByte < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) ? batchkaikeilogic.getTekiyouChuuki().replace("\r\n", "") :"";
		boolean shiireHyoujiFlg   	= 1 == kiShouhizei.findByDate(null).shiireZeigakuAnbunFlg;
		boolean isInvoice = "0".equals(hontai.invoiceDenpyou);
		
		// ---------------------------------------
		// 項目名・データ格納用
		// ---------------------------------------
		String torihikiTitle = "";
		String torihiki = "";
		String siyoukikanTitle = "";
		String siyoukikan = "";
		String siyoukaisibiTitle = "";
		String siyoukaisibi = "";
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
		String kingakuTitle = "";
		String kingaku = "";
		String shouhizeigakuTitle = "";
		String shouhizeigaku = "";
		String futanbumonTitle = "";
		String futanbumon = "";
		String torihikisakiTitle = "";
		String torihikisaki = "";
		String projectTitle = "";
		String project = "";
		String segmentTitle = "";
		String segment = "";
	
		String jyoushakukanTitle = "";
		String jyoushakukan = "";
		Integer kingakuMoveLine = 0; // 金額出力位置判定用
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		//■伝票種別名
		excel.write("denpyou_shubetsu", denpyouShubetuMap.denpyouPrintShubetsu);
		//インボイス開始後、かつ旧伝票（インボイス前伝票）の場合
		commonXlsLogic.beforeInvoice(hontai.invoiceDenpyou, excel);
		
		//■申請内容
		//取引
		if(ks.torihiki.getPdfHyoujiFlg()){
			torihikiTitle = ks.torihiki.getName();
			torihiki = ks.torihiki.getCodeSeigyoValue(Integer.toString(hontai.shiwakeEdano), hontai.torihikiName, " ");
		}
		//使用期間
		if(ks.shiyouKikanKbn.getPdfHyoujiFlg()){
			siyoukikanTitle = ks.shiyouKikanKbn.getName();
			siyoukikan = sysLogic.findNaibuCdName("shiyou_kikan_kbn", hontai.shiyouKikanKbn);
		}
		//使用開始日
		if(ks.shiyouKaishibi.getPdfHyoujiFlg()){
			siyoukaisibiTitle = ks.shiyouKaishibi.getName();
			siyoukaisibi = EteamXlsFmt.formatDate(hontai.shiyouKaishibi);
		}
		//支払日(支払日は記載固定)
		shiharaibiTitle = "支払日";
		shiharaibi = EteamXlsFmt.formatDate(hontai.shiharaibi);

		//科目
		if(ks.kamoku.getPdfHyoujiFlg()){
			kamokuTitle = ks.kamoku.getName();
			kamoku = ks.kamoku.getCodeSeigyoValue(hontai.kariKamokuCd, hontai.kariKamokuName, " ");
			kingakuMoveLine=0;
		}
		
		//課税区分
		if(ks.kazeiKbn.getPdfHyoujiFlg()) {
			kazeiKbnTitle = ks.kazeiKbn.getName();
			String kazei = sysLogic.findNaibuCdName("kazei_kbn", hontai.kariKazeiKbn);
			kazeiKbn = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kariKazeiKbn, kazei, " ");
			kingakuMoveLine++;
		}
		
		//税率
		if(ks.zeiritsu.getPdfHyoujiFlg()) {
			zeiritsuTitle = "税率";
			if(hontai.kariKazeiKbn.equals("001") || hontai.kariKazeiKbn.equals("002") || 
			   hontai.kariKazeiKbn.equals("011") || hontai.kariKazeiKbn.equals("012") ||
			   hontai.kariKazeiKbn.equals("013") || hontai.kariKazeiKbn.equals("014")) 
			{
				zeiritsu = EteamXlsFmt.formatZeiritsu(hontai.zeiritsu, hontai.keigenZeiritsuKbn) + " %";
			}
			else
			{
				zeiritsu = "";
			}
			kingakuMoveLine++;
		}
		
		//分離区分
		if(ks.bunriKbn.getPdfHyoujiFlg()) {
			bunriKbnTitle = ks.bunriKbn.getName();
			String bunri = sysLogic.findNaibuCdName("bunri_kbn", hontai.bunriKbn);
			bunriKbn = ks.bunriKbn.getCodeSeigyoValue(hontai.bunriKbn, bunri, " ");
			kingakuMoveLine++;
		} 
		
		//仕入区分
		if(ks.shiireKbn.getPdfHyoujiFlg() && shiireHyoujiFlg) {
			shiireKbnTitle = ks.shiireKbn.getName(); 
			String shiire = sysLogic.findNaibuCdName("shiire_kbn", hontai.kariShiireKbn);
			shiireKbn = ks.shiireKbn.getCodeSeigyoValue(hontai.kariShiireKbn, shiire, " ");
			kingakuMoveLine++;
		} 
		
		//科目枝番
		if(ks.kamokuEdaban.getPdfHyoujiFlg()){
			edabanTitle = ks.kamokuEdaban.getName();
			edaban = ks.kamokuEdaban.getCodeSeigyoValue(hontai.kariKamokuEdabanCd, hontai.kariKamokuEdabanName, " ");
			kingakuMoveLine++;
		}
		
		//負担部門
		if(ks.futanBumon.getPdfHyoujiFlg() == true){
			futanbumonTitle = ks.futanBumon.getName();
			futanbumon = ks.futanBumon.getCodeSeigyoValue(hontai.kariFutanBumonCd, hontai.kariFutanBumonName, " ");
			kingakuMoveLine++;
		}
		//取引先
		if(ks.torihikisaki.getPdfHyoujiFlg()){
			torihikisakiTitle = ks.torihikisaki.getName();
			torihikisaki = ks.torihikisaki.getCodeSeigyoValue(hontai.torihikisakiCd, hontai.torihikisakiNameRyakushiki, " ");
			kingakuMoveLine++;
		}
		
		//プロジェクト
		if(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals("1") && ks.project.getPdfHyoujiFlg()){
			projectTitle = ks.project.getName();
			project = ks.project.getCodeSeigyoValue(hontai.projectCd, hontai.projectName, " ");
			kingakuMoveLine++;
		}
		
		//セグメント
		if(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals("1") && ks.segment.getPdfHyoujiFlg()){
			segmentTitle = ks.segment.getName();
			segment = ks.segment.getCodeSeigyoValue(hontai.segmentCd, hontai.segmentNameRyakushiki, " ");
			kingakuMoveLine++;
		}
		
		//金額
		if(ks.kingaku.getPdfHyoujiFlg()){
			
			// 一度excelに書き込む
			kingakuTitle = ks.kingaku.getName();
			kingaku = EteamXlsFmt.formatMoney(hontai.kingaku);
			excel.write("kingaku_title", kingakuTitle);
			excel.write("kingaku", kingaku);
			
			if (kingakuMoveLine != 0)
			{
				// 「負担部門～セグメント」の内、一番下に表示される項目の左側にコピーして罫線を引く
				Cell kingakuCell = book.findCellByName("kingaku_title");
				excel.copy(kingakuCell.getColumn(), kingakuCell.getRow(), kingakuCell.getColumn() + 9, kingakuCell.getRow(),
						kingakuCell.getColumn(), kingakuCell.getRow() + kingakuMoveLine);
				excel.encloseInBorder(kingakuCell.getRow() + kingakuMoveLine, kingakuCell.getColumn(), 1);
				excel.encloseInBorder(kingakuCell.getRow() + kingakuMoveLine, kingakuCell.getColumn() + 4, 1);
				
				// 元の項目は空にする
				kingakuTitle =  "";
				kingaku =  "";
			}
			else
			{
				// 位置を変えずに罫線を引く
				excel.makeBorder("kingaku_title");
				excel.makeBorder("kingaku");
			}
		}
		
		if(isInvoice) {
			//うち消費税額
			if(ks.shouhizeigaku.getPdfHyoujiFlg()) {
				// 一度excelに書き込む
				shouhizeigakuTitle = ks.shouhizeigaku.getName();
				shouhizeigaku = EteamXlsFmt.formatMoney(hontai.shouhizeigaku);
				excel.write("shouhizeigaku_title", shouhizeigakuTitle);
				excel.write("shouhizeigaku", shouhizeigaku);

				if (kingakuMoveLine != 0)
				{
					// 「負担部門～セグメント」の内、一番下に表示される項目の左側にコピーして罫線を引く
					Cell shouhizeigakuCell = book.findCellByName("shouhizeigaku_title");
					excel.copy(shouhizeigakuCell.getColumn(), shouhizeigakuCell.getRow(), shouhizeigakuCell.getColumn() + 9, shouhizeigakuCell.getRow(),
							shouhizeigakuCell.getColumn(), shouhizeigakuCell.getRow() + kingakuMoveLine);
					excel.encloseInBorder(shouhizeigakuCell.getRow() + kingakuMoveLine, shouhizeigakuCell.getColumn(), 1);
					excel.encloseInBorder(shouhizeigakuCell.getRow() + kingakuMoveLine, shouhizeigakuCell.getColumn() + 4, 1);

					// 元の項目は空にする
					shouhizeigakuTitle =  "";
					shouhizeigaku =  "";
				}
				else
				{
					// 位置を変えずに罫線を引く
					excel.makeBorder("shouhizeigaku_title");
					excel.makeBorder("shouhizeigaku");
				}
			}

			// インボイス関連処理
			super.getAndPrintInvoiceZei("A006", denpyouId, excel);
		} else {
			excel.hideRow(excel.getCell("shouhizeigaku_shousai").getRow());
			excel.hideRow(excel.getCell("karibarai_shouhizei").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_10_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_title").getRow());
			excel.hideRow(excel.getCell("karibarai_zeigaku_8_percent_menzei_80_title").getRow());
			excel.hideRow(excel.getCell("kariuke_zeigaku_8_percent_tsumiage_title").getRow());
			excel.hideRow(excel.getCell("kariuke_zeigaku_8_percent_warimodoshi_title").getRow());
			// 元の項目は空にする
			shouhizeigakuTitle =  "";
			shouhizeigaku =  "";
		}
		
		//excelに各項目出力
		excel.write("torihiki_title", torihikiTitle);
		excel.write("torihiki", torihiki);
		excel.write("siyoukikan_title", siyoukikanTitle);
		excel.write("siyoukikan", siyoukikan);
		excel.write("siyoukaisibi_title", siyoukaisibiTitle);
		excel.write("siyoukaishibi", siyoukaisibi);
		excel.write("shiharaibi_title", shiharaibiTitle);
		excel.write("shiharaibi", shiharaibi);
		excel.write("kamoku_title", kamokuTitle);
		excel.write("kamoku", kamoku);
		excel.write("kazeikbn_title", kazeiKbnTitle);
		excel.write("kazeikbn", kazeiKbn);
		excel.write("zeiritsu_title", zeiritsuTitle);
		excel.write("zeiritsu", zeiritsu);
		excel.write("bunrikbn_title", bunriKbnTitle);
		excel.write("bunrikbn", bunriKbn);
		excel.write("shiirekbn_title", shiireKbnTitle);
		excel.write("shiirekbn", shiireKbn); 
		excel.write("edaban_title", edabanTitle);
		excel.write("edaban", edaban);
		excel.write("kingaku_title", kingakuTitle);
		excel.write("kingaku", kingaku);
		excel.write("shouhizeigaku_title", shouhizeigakuTitle);
		excel.write("shouhizeigaku", shouhizeigaku);
		excel.write("futanbumon_title", futanbumonTitle);
		excel.write("futan_bumon", futanbumon);
		excel.write("torihikisaki_title", torihikisakiTitle );
		excel.write("torihikisaki", torihikisaki );
		excel.write("project_title", projectTitle );
		excel.write("project", project );
		excel.write("segment_title", segmentTitle );
		excel.write("segment", segment );
		excel.write("chuuki", chuuki);

		//■乗車区間
		if(ks.jyoushaKukan.getPdfHyoujiFlg()){
			jyoushakukanTitle = ks.jyoushaKukan.getName();
			jyoushakukan = jyousha_kukan;
			
			//高さ調整：補足は１行４７文字以内で計算。デフォルト３行だけどそれ以上の行数になるなら高さ変える。
			int jyousha_kukanLineNum = excel.lineCount(jyousha_kukan, 94, 4);
			excel.setHeight(jyousha_kukanRow,  jyousha_kukanLineNum, 3);
			excel.write("jyoushakukan_title", "■" + jyoushakukanTitle);
			excel.write("jyousha_kukan", jyoushakukan);
		
		}else{
			excel.hideRow(excel.getCell("jyoushakukan_title").getRow());
			excel.hideRow(excel.getCell("jyousha_kukan").getRow());
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
