package eteam.gyoumu.shiharai;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import jxl.Cell;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WritableWorkbook;

/**
 * 
 */
public class ShiharaiYoteiSoukatsuhyoXlsLogic extends EteamAbstractLogic{

//＜定数＞
	/** 支払予定総括表：１頁の出力行数 */
	protected static int SHIHARAI_YOTEI_SOUKATSU_ROWLIMIT = 22;
	
//＜部品＞
	/** WF */
	WorkflowCategoryLogic workflowLogic;
	/** 会計 */
	BatchKaikeiCommonLogic batchkaikeilogic;
	/**
	 * @param shiharaiYoteiBiFrom 支払予定日From
	 * @param shiharaiYoteiBiTo 支払予定日To
	 * @param keijouBiFrom 計上日From
	 * @param keijouBiTo 計上日To
	 * @param saisyuShouninZumiCheck 最終承認済チェックボックス
	 * @param out 出力先
	 */ 
	public void makeExcel(Date shiharaiYoteiBiFrom, Date shiharaiYoteiBiTo, Date keijouBiFrom,  Date keijouBiTo, String saisyuShouninZumiCheck, OutputStream out) {

		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_shiharaiyoteisoukatsu.xls"), out);
		makeShiharaiYoteiSoukatsuHyo(excel, shiharaiYoteiBiFrom, shiharaiYoteiBiTo, keijouBiFrom, keijouBiTo, saisyuShouninZumiCheck, out);
		
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
	
	/**
	 * 支払予定総括表作成
	 * @param xls Excelオブジェクト
	 * @param shiharaiYoteiBiFrom 支払予定日From
	 * @param shiharaiYoteiBiTo 支払予定日To
	 * @param keijouBiFrom 計上日From
	 * @param keijouBiTo 計上日To
	 * @param saisyuShouninZumiCheck 最終承認済チェックボックス
	 * @param out 出力先
	 */
	protected void makeShiharaiYoteiSoukatsuHyo(EteamXls xls, Date shiharaiYoteiBiFrom, Date shiharaiYoteiBiTo, Date keijouBiFrom,  Date keijouBiTo, String saisyuShouninZumiCheck, OutputStream out) {
		
		KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		
		WritableWorkbook book = xls.getBook();
		Cell furikomiCell = book.findCellByName("furikomi_shiharai_houhou");
		int furikomiShiharaiHouhouRow = furikomiCell.getRow();
		int shiharaiHouhouCol = furikomiCell.getColumn();
		Cell jidouHikiotoshiCell = book.findCellByName("jidou_hikiotoshi_shiharai_houhou");
		int jidouHikiotoshiShiharaiHouhouRow = jidouHikiotoshiCell.getRow();
		Cell shiharaiYoteibiCell = book.findCellByName("furikomi_shiharai_yoteibi");
		int shiharaiYoteibiCol = shiharaiYoteibiCell.getColumn();
		Cell teikiCell = book.findCellByName("furikomi_teiki");
		int teikiCol = teikiCell.getColumn();
		Cell sonotaCell = book.findCellByName("furikomi_sonota");
		int sonotaCol = sonotaCell.getColumn();
		Cell shoukei01Cell = book.findCellByName("furikomi_shoukei01");
		int  shoukei01Col = shoukei01Cell.getColumn();
		Cell koubaiCell = book.findCellByName("furikomi_koubai");
		int  koubaiCol = koubaiCell.getColumn();
		Cell leaseCell = book.findCellByName("furikomi_lease");
		int  leaseCol = leaseCell.getColumn();
		Cell shoukei02Cell = book.findCellByName("furikomi_shoukei02");
		int  shoukei02Col = shoukei02Cell.getColumn();
		Cell goukei01Cell = book.findCellByName("furikomi_goukei01");
		int  goukei01Col = goukei01Cell.getColumn();
		Cell mibaraiFuteikiCell = book.findCellByName("furikomi_mibarai_futeiki");
		int mibaraiFuteikiCol = mibaraiFuteikiCell.getColumn();
		Cell mibaraiTeikiCell = book.findCellByName("furikomi_mibarai_teiki");
		int mibaraiTeikiCol = mibaraiTeikiCell.getColumn();
		Cell setsubiMibaraiCell = book.findCellByName("furikomi_setsubi_mibarai");
		int setsubiMibaraiCol = setsubiMibaraiCell.getColumn();
		
		int offsetRec=0;
		
		//振込、自動引落の集計結果格納行を用意する
		long shiharaiIraiCountJidouHikiotoshi = countShiharaiIrai(shiharaiYoteiBiFrom, shiharaiYoteiBiTo,
																	keijouBiFrom, keijouBiTo,
																	saisyuShouninZumiCheck, SHIHARAI_IRAI_HOUHOU.JIDOUHIKIOTOSHI);
		if (0 == shiharaiIraiCountJidouHikiotoshi) {
			xls.hideRow(jidouHikiotoshiShiharaiHouhouRow);
			xls.hideRow(jidouHikiotoshiShiharaiHouhouRow+1);
			offsetRec=2;
		}
		else if (1 < shiharaiIraiCountJidouHikiotoshi) {
			xls.copyRow(jidouHikiotoshiShiharaiHouhouRow, jidouHikiotoshiShiharaiHouhouRow + 1, 1, (int)shiharaiIraiCountJidouHikiotoshi - 1, true);
		}
		long shiharaiIraiCountFurikomi = countShiharaiIrai(shiharaiYoteiBiFrom, shiharaiYoteiBiTo,
															keijouBiFrom, keijouBiTo,
															saisyuShouninZumiCheck, SHIHARAI_IRAI_HOUHOU.FURIKOMI);
		if (0 == shiharaiIraiCountFurikomi) {
			xls.hideRow(furikomiShiharaiHouhouRow);
			xls.hideRow(furikomiShiharaiHouhouRow+1);
			offsetRec=2;
		}
		else if (1 < shiharaiIraiCountFurikomi) {
			xls.copyRow(furikomiShiharaiHouhouRow, furikomiShiharaiHouhouRow + 1, 1, (int)shiharaiIraiCountFurikomi - 1, true);
		}
		
		//振込集計
		List<GMap>	shiharaiIraiListFurikomi = loadShiharaiIrai(shiharaiYoteiBiFrom, shiharaiYoteiBiTo, keijouBiFrom, keijouBiTo, saisyuShouninZumiCheck, SHIHARAI_IRAI_HOUHOU.FURIKOMI);
		
		int xlsRowCnt = furikomiShiharaiHouhouRow;
		String yoteibiTmp = "";
		BigDecimal furikomiTeiki = new BigDecimal(0);
		BigDecimal furikomiSonota = new BigDecimal(0);
		BigDecimal furikomiShoukei01 = new BigDecimal(0);
		BigDecimal furikomiKoubai = new BigDecimal(0);
		BigDecimal furikomiLease = new BigDecimal(0);
		BigDecimal furikomiShoukei02 = new BigDecimal(0);
		BigDecimal furikomiGoukei01 = new BigDecimal(0);
		BigDecimal furikomiMibaraiFuteiki = new BigDecimal(0);
		BigDecimal furikomiMibaraiTeiki = new BigDecimal(0);
		BigDecimal furikomiSetsubiMibarai = new BigDecimal(0);
		BigDecimal furikomiTeikiShoukei = new BigDecimal(0);
		BigDecimal furikomiSonotaShoukei = new BigDecimal(0);
		BigDecimal furikomiShoukei01Sum = new BigDecimal(0);
		BigDecimal furikomiKoubaiShoukei = new BigDecimal(0);
		BigDecimal furikomiLeaseShoukei = new BigDecimal(0);
		BigDecimal furikomiShoukei02Sum = new BigDecimal(0);
		BigDecimal furikomiGoukei01Sum = new BigDecimal(0);
		BigDecimal furikomiMibaraiFuteikiSum = new BigDecimal(0);
		BigDecimal furikomiMibaraiTeikiSum = new BigDecimal(0);
		BigDecimal furikomiSetsubiMibaraiSum = new BigDecimal(0);
		
		for (int i = 0; i < shiharaiIraiListFurikomi.size(); i++) {
			GMap shiharaiFurikomi = shiharaiIraiListFurikomi.get(i);
			String yoteibi = EteamXlsFmt.formatDate(shiharaiFurikomi.get("yoteibi"));
			String hasseiShubetsu = (String)shiharaiFurikomi.get("hassei_shubetsu");
			String shiharaiShubetsu = (String)shiharaiFurikomi.get("shiharai_shubetsu");
			BigDecimal sashihikiShiharai = (BigDecimal)shiharaiFurikomi.get("sashihiki_shiharai");
			String denpyouId = (String)shiharaiFurikomi.get("denpyou_id");
			
			if(i==0) {
				yoteibiTmp = yoteibi;
				xls.write(shiharaiHouhouCol, xlsRowCnt, "振込");
			}
			if(!yoteibi.equals(yoteibiTmp)){
				xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.TOP, BorderLineStyle.NONE);
				xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.BOTTOM, BorderLineStyle.NONE);
				xls.write(shiharaiYoteibiCol, xlsRowCnt, yoteibiTmp);
				xls.write(teikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiTeiki));
				xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSonota));
				xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiShoukei01));
				xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiKoubai));
				xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiLease));
				furikomiShoukei02 = furikomiKoubai.add(furikomiLease);
				xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiShoukei02));
				furikomiGoukei01 = furikomiShoukei01.add(furikomiShoukei02);
				xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiGoukei01));
				xls.write(mibaraiFuteikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiFuteiki));
				xls.write(mibaraiTeikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiTeiki));
				xls.write(setsubiMibaraiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSetsubiMibarai));
				
				furikomiTeiki = new BigDecimal(0);
				furikomiSonota = new BigDecimal(0);
				furikomiShoukei01 = new BigDecimal(0);;
				furikomiKoubai = new BigDecimal(0);
				furikomiLease = new BigDecimal(0);
				furikomiMibaraiFuteiki = new BigDecimal(0);
				furikomiMibaraiTeiki = new BigDecimal(0);
				furikomiSetsubiMibarai = new BigDecimal(0);
				
				xlsRowCnt++;
			}
			yoteibiTmp = yoteibi;
			if(hasseiShubetsu.equals("経費")) {
				//定期
				if(SHIHARAI_IRAI_SHUBETSU.TEIKI.equals(shiharaiShubetsu)) {
					furikomiTeiki = furikomiTeiki.add(sashihikiShiharai);
					furikomiTeikiShoukei = furikomiTeikiShoukei.add(sashihikiShiharai);
				}
				//その他
				else if(SHIHARAI_IRAI_SHUBETSU.SONOTA.equals(shiharaiShubetsu)) {
					furikomiSonota = furikomiSonota.add(sashihikiShiharai);
					furikomiSonotaShoukei = furikomiSonotaShoukei.add(sashihikiShiharai);
				}
				furikomiShoukei01 = furikomiShoukei01.add(sashihikiShiharai);
				furikomiShoukei01Sum = furikomiShoukei01Sum.add(sashihikiShiharai);
			}
			if(hasseiShubetsu.equals("購買")) {
				furikomiKoubai = furikomiKoubai.add(sashihikiShiharai);
				furikomiKoubaiShoukei = furikomiKoubaiShoukei.add(sashihikiShiharai);
			}
			if(hasseiShubetsu.equals("リース")) {
				furikomiLease = furikomiLease.add(sashihikiShiharai);
				furikomiLeaseShoukei = furikomiLeaseShoukei.add(sashihikiShiharai);
			}
			//伝票別で明細を取得
			List<GMap> 	meisaiList = kaikeiLogic.loadShiharaiIraiMeisai(denpyouId);
			if(meisaiList.size() > 0) {
				// 1件目の明細の貸方科目で未払い不定期、未払い定期を振分ける。
				GMap meisai = meisaiList.get(0);
				int shiwakeEdano = (int)meisai.get("shiwake_edano");
				GMap shiwakeP = kaikeiLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, shiwakeEdano);
				if (shiwakeP == null)
				{
					continue;
				}
				String kashiNo = (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) ? "2" : "3";
				String kashikataKamokuCd = ((String)shiwakeP.get("kashi_kamoku_cd" + kashiNo));
				//未払い不定期
				if(kashikataKamokuCd.equals(setting.kashikatakamokuCdMibaraiFuteiki())){
					furikomiMibaraiFuteiki = furikomiMibaraiFuteiki.add(sashihikiShiharai);
					furikomiMibaraiFuteikiSum = furikomiMibaraiFuteikiSum.add(sashihikiShiharai);
				}
				//未払い定期
				if( kashikataKamokuCd.equals(setting.kashikatakamokuCdMibaraiTeiki())){
					furikomiMibaraiTeiki = furikomiMibaraiTeiki.add(sashihikiShiharai);
					furikomiMibaraiTeikiSum = furikomiMibaraiTeikiSum.add(sashihikiShiharai);
				}
				//設備未払い
				if( kashikataKamokuCd.equals(setting.kashikatakamokuCdSetsubiMibarai())){
					furikomiSetsubiMibarai = furikomiSetsubiMibarai.add(sashihikiShiharai);
					furikomiSetsubiMibaraiSum = furikomiSetsubiMibaraiSum.add(sashihikiShiharai);
				}
			}
		}
		//振込 集計 最後の予定日データを出力
		if(shiharaiIraiListFurikomi.size()>0) {
			xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.TOP, BorderLineStyle.NONE);
			xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.BOTTOM, BorderLineStyle.NONE);
			
			xls.write(shiharaiYoteibiCol, xlsRowCnt, yoteibiTmp);
			xls.write(teikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiTeiki));
			xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSonota));
			xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiShoukei01));
			xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiKoubai));
			xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiLease));
			furikomiShoukei02 = furikomiKoubai.add(furikomiLease);
			xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiShoukei02));
			furikomiGoukei01 = furikomiShoukei01.add(furikomiShoukei02);
			xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiGoukei01));
			
			xls.write(mibaraiFuteikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiFuteiki));
			xls.write(mibaraiTeikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiTeiki));
			xls.write(setsubiMibaraiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSetsubiMibarai));
			xlsRowCnt++;
			
			//小計
			xls.write(teikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiTeikiShoukei));
			xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSonotaShoukei));
			xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiShoukei01Sum));
			xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiKoubaiShoukei));
			xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiLeaseShoukei));
			furikomiShoukei02Sum = furikomiKoubaiShoukei.add(furikomiLeaseShoukei);
			xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiShoukei02Sum));
			furikomiGoukei01Sum = furikomiShoukei01Sum.add(furikomiShoukei02Sum);
			xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiGoukei01Sum));
			xls.write(mibaraiFuteikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiFuteikiSum));
			xls.write(mibaraiTeikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiTeikiSum));
			xls.write(setsubiMibaraiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSetsubiMibaraiSum));
		}
		else {
			xlsRowCnt++;
		}
		xlsRowCnt++;
		
		//自動引落
		List<GMap>	shiharaiIraiListJidouHikiotoshi = loadShiharaiIrai(shiharaiYoteiBiFrom, shiharaiYoteiBiTo, keijouBiFrom, keijouBiTo, saisyuShouninZumiCheck, SHIHARAI_IRAI_HOUHOU.JIDOUHIKIOTOSHI);
		BigDecimal jidouHikiotoshiSonota = new BigDecimal(0);
		BigDecimal jidouHikiotoshiKoubai = new BigDecimal(0);
		BigDecimal jidouHikiotoshiLease = new BigDecimal(0);
		BigDecimal jidouHikiotoshiShoukei02 = new BigDecimal(0);
		BigDecimal jidouHikiotoshiGoukei01 = new BigDecimal(0);
		
		BigDecimal jidouHikiotoshiSonotaShoukei = new BigDecimal(0);
		BigDecimal jidouHikiotoshiShoukei01Sum = new BigDecimal(0);
		BigDecimal jidouHikiotoshiKoubaiShoukei = new BigDecimal(0);
		BigDecimal jidouHikiotoshiLeaseShoukei = new BigDecimal(0);
		BigDecimal jidouHikiotoshiShoukei02Sum = new BigDecimal(0);
		BigDecimal jidouHikiotoshiGoukei01Sum = new BigDecimal(0);
		
		for (int i = 0; i < shiharaiIraiListJidouHikiotoshi.size(); i++) {
			GMap shiharaiJidouHikiotoshi = shiharaiIraiListJidouHikiotoshi.get(i);
			String yoteibi = EteamXlsFmt.formatDate(shiharaiJidouHikiotoshi.get("yoteibi"));
			String hasseiShubetsu = (String)shiharaiJidouHikiotoshi.get("hassei_shubetsu");
			BigDecimal sashihikiShiharai = (BigDecimal)shiharaiJidouHikiotoshi.get("sashihiki_shiharai");
			if(i==0) {
				yoteibiTmp = yoteibi;
				xls.write(shiharaiHouhouCol, xlsRowCnt, "自動引落");
			}
			if(!yoteibi.equals(yoteibiTmp)){
				xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.TOP, BorderLineStyle.NONE);
				xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.BOTTOM, BorderLineStyle.NONE);
				xls.write(shiharaiYoteibiCol, xlsRowCnt, yoteibiTmp);
				xls.write(teikiCol, xlsRowCnt, "0");
				
				xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiSonota));
				xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiSonota)); //経費：その他と同じ
				
				xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiKoubai));
				xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiLease));
				
				jidouHikiotoshiShoukei02 = jidouHikiotoshiKoubai.add(jidouHikiotoshiLease);
				xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiShoukei02));
				
				jidouHikiotoshiGoukei01 = jidouHikiotoshiSonota.add(jidouHikiotoshiShoukei02);
				xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiGoukei01));
				
				xls.write(mibaraiFuteikiCol, xlsRowCnt, "0");
				xls.write(mibaraiTeikiCol, xlsRowCnt, "0");
				xls.write(setsubiMibaraiCol, xlsRowCnt, "0");
				
				jidouHikiotoshiSonota = new BigDecimal(0);
				jidouHikiotoshiKoubai = new BigDecimal(0);
				jidouHikiotoshiLease = new BigDecimal(0);
				
				xlsRowCnt++;
			}
			yoteibiTmp = yoteibi;
			if(hasseiShubetsu.equals("経費")) {
				//その他
				jidouHikiotoshiSonota = jidouHikiotoshiSonota.add(sashihikiShiharai);
				jidouHikiotoshiSonotaShoukei = jidouHikiotoshiSonotaShoukei.add(sashihikiShiharai);
				jidouHikiotoshiShoukei01Sum = jidouHikiotoshiShoukei01Sum.add(sashihikiShiharai);
			}
			if(hasseiShubetsu.equals("購買")) {
				jidouHikiotoshiKoubai = jidouHikiotoshiKoubai.add(sashihikiShiharai);
				jidouHikiotoshiKoubaiShoukei = jidouHikiotoshiKoubaiShoukei.add(sashihikiShiharai);
			}
			if(hasseiShubetsu.equals("リース")) {
				jidouHikiotoshiLease = jidouHikiotoshiLease.add(sashihikiShiharai);
				jidouHikiotoshiLeaseShoukei = jidouHikiotoshiLeaseShoukei.add(sashihikiShiharai);
			}
			
		}
		//自動引落 集計 最後の予定日データを出力
		if(shiharaiIraiListJidouHikiotoshi.size()>0) {
			xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.TOP, BorderLineStyle.NONE);
			xls.setBorder(xlsRowCnt, shiharaiHouhouCol, Border.BOTTOM, BorderLineStyle.NONE);
			
			xls.write(shiharaiYoteibiCol, xlsRowCnt, yoteibiTmp);
			xls.write(teikiCol, xlsRowCnt, "0");
			
			xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiSonota));
			xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiSonota)); //経費：その他と同じ
			
			xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiKoubai));
			xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiLease));
			
			jidouHikiotoshiShoukei02 = jidouHikiotoshiKoubai.add(jidouHikiotoshiLease);
			xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiShoukei02));
			
			jidouHikiotoshiGoukei01 = jidouHikiotoshiSonota.add(jidouHikiotoshiShoukei02);
			xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiGoukei01));
			
			xls.write(mibaraiFuteikiCol, xlsRowCnt, "0");
			xls.write(mibaraiTeikiCol, xlsRowCnt, "0");
			xls.write(setsubiMibaraiCol, xlsRowCnt, "0");
			xlsRowCnt++;
			
			//小計
			xls.write(teikiCol, xlsRowCnt,"0");
			xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiSonotaShoukei));
			xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiShoukei01Sum));
			
			xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiKoubaiShoukei));
			xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiLeaseShoukei));
			
			jidouHikiotoshiShoukei02Sum = jidouHikiotoshiKoubaiShoukei.add(jidouHikiotoshiLeaseShoukei);
			xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiShoukei02Sum));
			
			jidouHikiotoshiGoukei01Sum = jidouHikiotoshiShoukei01Sum.add(jidouHikiotoshiShoukei02Sum);
			xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(jidouHikiotoshiGoukei01Sum));
			
			xls.write(mibaraiFuteikiCol, xlsRowCnt, "0");
			xls.write(mibaraiTeikiCol, xlsRowCnt, "0");
			xls.write(setsubiMibaraiCol, xlsRowCnt, "0");
		}
		else {
			xlsRowCnt++;
		}
		xlsRowCnt++;
		
		//総合計
		BigDecimal goukeiSonota = new BigDecimal(0);
		BigDecimal goukeiShoukeiSum01 = new BigDecimal(0);
		BigDecimal goukeiKoubaiShoukei = new BigDecimal(0);
		BigDecimal goukeiLeaseShoukei = new BigDecimal(0);
		BigDecimal goukeiShoukeiSum02 = new BigDecimal(0);
		BigDecimal goukeiGoukei01Sum = new BigDecimal(0);
		xls.write(teikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiTeikiShoukei));
		goukeiSonota = furikomiSonotaShoukei.add(jidouHikiotoshiSonotaShoukei);
		xls.write(sonotaCol, xlsRowCnt, EteamXlsFmt.formatMoney(goukeiSonota));
		goukeiShoukeiSum01 = furikomiShoukei01Sum.add(jidouHikiotoshiShoukei01Sum);
		xls.write(shoukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(goukeiShoukeiSum01));
		goukeiKoubaiShoukei = furikomiKoubaiShoukei.add(jidouHikiotoshiKoubaiShoukei);
		xls.write(koubaiCol, xlsRowCnt, EteamXlsFmt.formatMoney(goukeiKoubaiShoukei));
		goukeiLeaseShoukei = furikomiLeaseShoukei.add(jidouHikiotoshiLeaseShoukei);
		xls.write(leaseCol, xlsRowCnt, EteamXlsFmt.formatMoney(goukeiLeaseShoukei));
		goukeiShoukeiSum02 = furikomiShoukei02Sum.add(jidouHikiotoshiShoukei02Sum);
		xls.write(shoukei02Col, xlsRowCnt, EteamXlsFmt.formatMoney(goukeiShoukeiSum02));
		goukeiGoukei01Sum = furikomiGoukei01Sum.add(jidouHikiotoshiGoukei01Sum);
		xls.write(goukei01Col, xlsRowCnt, EteamXlsFmt.formatMoney(goukeiGoukei01Sum));
		xls.write(mibaraiFuteikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiFuteikiSum));
		xls.write(mibaraiTeikiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiMibaraiTeikiSum));
		xls.write(setsubiMibaraiCol, xlsRowCnt, EteamXlsFmt.formatMoney(furikomiSetsubiMibaraiSum));
		
		//ヘッダおよび改ページ設定
		int page = 0;
		xlsRowCnt = xlsRowCnt - furikomiShiharaiHouhouRow + 1; //データ行数
		int souPageSu = ( (xlsRowCnt-offsetRec) / SHIHARAI_YOTEI_SOUKATSU_ROWLIMIT) + 1;
		if( ((xlsRowCnt-offsetRec)%SHIHARAI_YOTEI_SOUKATSU_ROWLIMIT ) == 0) {
			souPageSu--;
		}
		int rowCount = 0;
		for (int i = 0; i < xlsRowCnt; i++) {
			//振込または自動引落が0件の場合に改ページのタイミングを調整
			int headRow = i;
			if(page > 0) headRow = i + furikomiShiharaiHouhouRow * (page - 1);
			String teiki = xls.getSheet().getCell(teikiCol, headRow+7).getContents();
			
			//改頁判定
			if ( (rowCount % SHIHARAI_YOTEI_SOUKATSU_ROWLIMIT ) == 0 && !isEmpty(teiki)) {
				page++;
				String shiharaiHouhou = "振込";
				if (shiharaiIraiCountFurikomi < i) shiharaiHouhou = "自動引落";
				writeShiharaiYoteiSoukatsuHyoHeader(xls, rowCount, i, page, souPageSu, shiharaiYoteiBiFrom, shiharaiYoteiBiTo, keijouBiFrom, keijouBiTo, new Date(), furikomiShiharaiHouhouRow, shiharaiHouhou);
			}
			if (!isEmpty(teiki))
			{
				rowCount++;
			}
		}
	}
	
	/**
	 * 支払依頼データSELECT
	 * @param shiharaiYoteiBiFrom 支払予定日From
	 * @param shiharaiYoteiBiTo 支払予定日To
	 * @param keijouBiFrom 計上日From
	 * @param keijouBiTo 計上日To
	 * @param saisyuShouninZumiCheck 最終承認済チェックボックス
	 * @param shiharaiHouhou 支払方法
	 * @return 申請単位明細データ
	 */
	protected List<GMap> loadShiharaiIrai(Date shiharaiYoteiBiFrom, Date shiharaiYoteiBiTo,  Date keijouBiFrom, Date keijouBiTo, String saisyuShouninZumiCheck, String shiharaiHouhou) {
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT");
		sql.append("   i.denpyou_id ");
		sql.append("  ,i.yoteibi2 yoteibi ");
		sql.append("  ,i.shiharai_houhou ");
		sql.append("  ,i.hassei_shubetsu ");
		sql.append("  ,i.shiharai_shubetsu ");
		sql.append("  ,i.sashihiki_shiharai ");
		sql.append("  ,i.manekin_gensen ");
		sql.append("  ,d.denpyou_joutai ");
		
		sql.append(" FROM (SELECT s.*, yoteibi yoteibi2 FROM shiharai_irai s) i ");
		sql.append(" INNER JOIN denpyou d ON ");
		sql.append("  i.denpyou_id = d.denpyou_id ");
		sql.append(" WHERE ");
		if(saisyuShouninZumiCheck != null) {
			sql.append(" d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "'");
		} else {
			sql.append(" ( d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "' OR d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SHINSEI_CHUU + "')");
		}
		if (shiharaiYoteiBiFrom != null) {
			sql.append(" AND i.yoteibi2 >= ? ");
			params.add(shiharaiYoteiBiFrom);
		}
		if (shiharaiYoteiBiTo != null) {
			sql.append(" AND i.yoteibi2 < (CAST(? AS DATE) + 1)");
			params.add(shiharaiYoteiBiTo);
		}
		if (keijouBiFrom != null) {
			sql.append(" AND i.keijoubi >= ? ");
			params.add(keijouBiFrom);
		}
		if (keijouBiTo != null) {
			sql.append(" AND i.keijoubi < (CAST(? AS DATE) + 1)");
			params.add(keijouBiTo);
		}
		sql.append(" AND i.shiharai_houhou = '" + shiharaiHouhou + "'");
		sql.append(" ORDER BY ");
		sql.append("  i.yoteibi2 ");
		return connection.load(sql.toString(), params.toArray());
	}
	
	/**
	 * 支払依頼データCOUNT
	 * @param shiharaiYoteiBiFrom 支払予定日From
	 * @param shiharaiYoteiBiTo 支払予定日To
	 * @param keijouBiFrom 計上日From
	 * @param keijouBiTo 計上日To
	 * @param saisyuShouninZumiCheck 最終承認済チェックボックス
	 * @param shiharaiHouhou 支払方法
	 * @return 申請単位明細データ
	 */
	public long countShiharaiIrai(Date shiharaiYoteiBiFrom, Date shiharaiYoteiBiTo, Date keijouBiFrom, Date keijouBiTo, String saisyuShouninZumiCheck, String shiharaiHouhou) {
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("SELECT");
		sql.append(" COUNT(distinct i.yoteibi2) ");
		sql.append(" FROM (SELECT s.*, yoteibi yoteibi2 FROM shiharai_irai s) i ");
		sql.append(" INNER JOIN denpyou d ON ");
		sql.append("  i.denpyou_id = d.denpyou_id ");
		sql.append(" WHERE ");
		if(saisyuShouninZumiCheck != null) {
			sql.append(" d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "'");
		} else {
			sql.append(" ( d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "' OR d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SHINSEI_CHUU + "')");
		}
		if (shiharaiYoteiBiFrom != null) {
			sql.append(" AND i.yoteibi2 >= ? ");
			params.add(shiharaiYoteiBiFrom);
		}
		if (shiharaiYoteiBiTo != null) {
			sql.append(" AND i.yoteibi2 < (CAST(? AS DATE) + 1)");
			params.add(shiharaiYoteiBiTo);
		}
		if (keijouBiFrom != null) {
			sql.append(" AND i.keijoubi >= ? ");
			params.add(keijouBiFrom);
		}
		if (keijouBiTo != null) {
			sql.append(" AND i.keijoubi < (CAST(? AS DATE) + 1)");
			params.add(keijouBiTo);
		}
		
		sql.append(" AND i.shiharai_houhou = '" + shiharaiHouhou + "'");
		
		long count = (long)connection.find(sql.toString(), params.toArray()).get("count");
		return count;
	}
	
	/**
	 * 依頼書シートヘッダー出力
	 * @param xls EXCEL
	 * @param rowCount 実データ数 カウント
	 * @param row 行
	 * @param page 頁
	 * @param souPageSu 総頁数
	 * @param shiharaiYoteiBiFrom 支払予定日（開始）
	 * @param shiharaiYoteiBiTo 支払予定日（終了）
	 * @param keijouBiFrom 計上日（開始）
	 * @param keijouBiTo 計上日（終了）
	 * @param sakuseiHizuke 作成日付
	 * @param offSetRow オフセット行
	 * @param shiharaiHouhou 支払方法
	 */
	protected void writeShiharaiYoteiSoukatsuHyoHeader(EteamXls xls, int rowCount, int row, int page, int souPageSu, Date shiharaiYoteiBiFrom, Date shiharaiYoteiBiTo, Date keijouBiFrom,  Date keijouBiTo, Date sakuseiHizuke, int offSetRow, String shiharaiHouhou) {

		WritableWorkbook book = xls.getBook();
		Cell headShiharaiYoteiKaishibiCell = book.findCellByName("head_shiharai_yoteibi");
		int headShiharaiYoteiKaishibiCol = headShiharaiYoteiKaishibiCell.getColumn();
		Cell headSakuseiHizukeNowCell = book.findCellByName("head_sakusei_hizuke");
		int headSakuseiHizukeNowCol = headSakuseiHizukeNowCell.getColumn();
		Cell headPageCell = book.findCellByName("head_page");
		int headPageCol = headPageCell.getColumn();
		Cell furikomiCell = book.findCellByName("furikomi_shiharai_houhou");
		int shiharaiHouhouCol = furikomiCell.getColumn();
		Cell headKeijoubiCell = book.findCellByName("head_keijoubi");
		int headKeijoubiCol = headKeijoubiCell.getColumn();
		
		//改ページ
		int headRow = 0;
		if(rowCount > 0) {
			headRow = row + offSetRow * (page - 1);
			xls.setBorder(headRow - 1, shiharaiHouhouCol, Border.BOTTOM, BorderLineStyle.THIN);
			xls.copyRow(0, headRow, 7, 1, true);
			xls.getSheet().addRowPageBreak(headRow);
		}
		String shiharaiHouhouCell = xls.getSheet().getCell(shiharaiHouhouCol, headRow+7).getContents();
		if(isEmpty(shiharaiHouhouCell)) {
			xls.write(shiharaiHouhouCol, headRow+7, shiharaiHouhou);
		}
		
		if( shiharaiYoteiBiFrom != null && shiharaiYoteiBiTo != null) {
			xls.write(headShiharaiYoteiKaishibiCol, headRow + 3, "支払予定日 : " + DateFormatUtils.format(shiharaiYoteiBiFrom, "yyyy/MM/dd") + "（開始）～" + DateFormatUtils.format(shiharaiYoteiBiTo, "yyyy/MM/dd") + "（終了）");
		}
		else if(shiharaiYoteiBiFrom != null){
			xls.write(headShiharaiYoteiKaishibiCol, headRow + 3, "支払予定日 : " + DateFormatUtils.format(shiharaiYoteiBiFrom, "yyyy/MM/dd") + "（開始）～");
		}
		else if(shiharaiYoteiBiTo != null){
			xls.write(headShiharaiYoteiKaishibiCol, headRow + 3, "支払予定日 : ～" + DateFormatUtils.format(shiharaiYoteiBiTo, "yyyy/MM/dd") + "（終了）");
		}
		else {
			xls.write(headShiharaiYoteiKaishibiCol, headRow + 3, "支払予定日 : ");
		}
		
		if(keijouBiFrom != null && keijouBiTo != null) {
			xls.write(headKeijoubiCol, headRow + 3, "計上日 : " + DateFormatUtils.format(keijouBiFrom, "yyyy/MM/dd") + "（開始）～" + DateFormatUtils.format(keijouBiTo, "yyyy/MM/dd") + "（終了）");
		}
		else if(keijouBiFrom != null) {
			xls.write(headKeijoubiCol, headRow + 3, "計上日 : " + DateFormatUtils.format(keijouBiFrom, "yyyy/MM/dd") + "（開始）～");
		}
		else if(keijouBiTo != null) {
			xls.write(headKeijoubiCol, headRow + 3, "計上日 : ～" + DateFormatUtils.format(keijouBiTo, "yyyy/MM/dd") + "（終了）");
		}
		else {
			xls.write(headKeijoubiCol, headRow + 3, "計上日 : ");
		}
		
		xls.write(headSakuseiHizukeNowCol, headRow + 1, "作成日付 : " + DateFormatUtils.format(sakuseiHizuke, "yyyy/MM/dd HH:mm:ss"));
		xls.write(headPageCol, headRow + 1, "頁 : " + page + "/" + souPageSu);
	}
	
}
