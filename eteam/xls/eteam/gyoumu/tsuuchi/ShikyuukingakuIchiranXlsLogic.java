package eteam.gyoumu.tsuuchi;

import java.io.OutputStream;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.SHIKYU_ICHIRAN_SORT_COLUMN_DATE;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.tsuuchi.ShikyuukingakuIchiranLogic.GoukeiShikyuuData;
import eteam.gyoumu.tsuuchi.ShikyuukingakuIchiranLogic.ShainGoukeiShikyuuData;
import eteam.gyoumu.tsuuchi.ShikyuukingakuIchiranLogic.ShikyuuData;
import jxl.Cell;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WritableWorkbook;

/**
 * 社員別支給金額一覧XlsLogic
 */
public class ShikyuukingakuIchiranXlsLogic extends EteamAbstractLogic {
	
	/** 社員別支給金額一覧PDF出力用excel作成
	 * @param isShainBetsuSummary 社員別サマリ
	 * @param meisaiList 検索結果(明細)
	 * @param goukeiData 検索結果(合計)
	 * @param dateColumn 日付カラム名称(起票日or使用日)
	 * @param out 出力先
	 */ 
	public void makeExcel(Boolean isShainBetsuSummary, List<ShikyuuData> meisaiList, GoukeiShikyuuData goukeiData, String dateColumn, OutputStream out) {
		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		boolean houjinRiyou = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		boolean tehaiRiyou  = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);

		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		String template = "";
		if (isShainBetsuSummary) {
			// 社員別サマリあり
			template = "template_shainbetsuShikyuuIchiran_summary.xls";
		} else {
			// 社員別サマリなし
			template = "template_shainbetsuShikyuuIchiran.xls";
		}
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream(template), out);
		WritableWorkbook book = excel.getBook();
		Cell meisaiCell = book.findCellByName("shain_no");
		int meisaiRow = meisaiCell.getRow();

		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		
		//■ヘッダ部
		// 起票日 or 使用日
		if(SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(dateColumn)){
			excel.write("header_date", "起票日");
		}else{
			excel.write("header_date", "使用日");
		}
		
		//■金額合計部
		
		// 支給金額（差引マイナス）
		excel.write("sum_genkin_shikyuu", EteamXlsFmt.formatMoney(goukeiData.sumGenkinShikyuu));
		excel.write("sum_furikomi_shikyuu", EteamXlsFmt.formatMoney(goukeiData.sumFurikomiShikyuu));
		excel.write("sum_genkin_sashihiki", EteamXlsFmt.formatMoney(goukeiData.sumGenkinSashihiki));
		excel.write("sum_furikomi_sashihiki", EteamXlsFmt.formatMoney(goukeiData.sumFurikomiSashihiki));
		
		//法人カード利用
		if (houjinRiyou) {
			excel.write("sum_houjin_title", "法人カード合計");
			excel.write("sum_houjin", EteamXlsFmt.formatMoney(goukeiData.sumHoujinKingaku));
			excel.makeBorder("sum_houjin_title");
			excel.makeBorder("sum_houjin");
			int row = excel.getCell("sum_houjin_title").getRow();
			int col = excel.getCell("sum_houjin_title").getColumn();
			excel.setBorder(row, col, Border.RIGHT, BorderLineStyle.DOUBLE);
		} else {
			Cell firstCell = excel.getCell("houjin_shiharai");
			excel.hideColumns(firstCell.getColumn(), firstCell.getColumn() + 4);
		}
		//会社手配利用
		if (tehaiRiyou) {
			excel.write("sum_tehai_title", "会社手配合計");
			excel.write("sum_tehai", EteamXlsFmt.formatMoney(goukeiData.sumTehaiKingaku));
			excel.makeBorder("sum_tehai_title");
			excel.makeBorder("sum_tehai");
			int row = excel.getCell("sum_tehai_title").getRow();
			int col = excel.getCell("sum_tehai_title").getColumn();
			excel.setBorder(row, col, Border.RIGHT, BorderLineStyle.DOUBLE);
		} else {
			Cell firstCell = excel.getCell("tehai_shiharai");
			excel.hideColumns(firstCell.getColumn(), firstCell.getColumn() + 4);
		}
		
		//■明細部
		//明細行数カウント
		int rowCount = 0;
		for (ShikyuuData d : meisaiList) {
			rowCount++;
			if (! isShainBetsuSummary && d.getShainGoukei() != null) {
				rowCount++;
			}
		}
		//明細行テンプレートコピー
		if (1 < rowCount) {
			excel.copyRow(meisaiRow, meisaiRow + 1, 1, rowCount - 1, true);
		}
		//明細行出力
		for (int i = 0, row = 0; i < meisaiList.size(); i++, row++) {
			ShikyuuData d = meisaiList.get(i);
			
			excel.write("shain_no"			, 0, row, d.getShainNo());
			excel.write("shain_name"		, 0, row, d.getUserFullName());
			
			if (isShainBetsuSummary) {
				// 社員別サマリあり
				
				// カンマで区切られた複数所属部門を改行しセルの高さを調整する
				String[] bumonNameArr = d.getBumonFullName().split(",");
				String delimiter = System.getProperty("line.separator");
				String bumonName = String.join(delimiter, bumonNameArr); 
				//高さ調整:内容は１行１４文字(28バイト)以内で計算。
				int lineNum = 0;
				for (String str : bumonNameArr) {
					lineNum += excel.lineCount(str, 28, 1);
				}
				
				excel.write("kihyou_bumon"		, 0, row, bumonName);
				excel.setHeight(meisaiRow + i, lineNum, 1);
				if(SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(dateColumn)){
					excel.write("kihyoubi"			, 0, row, d.getTourokuTime());
				}else{
					excel.write("kihyoubi"			, 0, row, d.getShiyoubi());
				}
				excel.write("shiharaibi"		, 0, row, d.getShiharaibi());
			} else {
				// 社員別サマリなし
				excel.write("kihyou_bumon"		, 0, row, d.getBumonFullName());
				if(SHIKYU_ICHIRAN_SORT_COLUMN_DATE.KIHYOUBI.equals(dateColumn)){
					excel.write("kihyoubi"			, 0, row, d.getTourokuTime());
				}else{
					excel.write("kihyoubi"			, 0, row, d.getShiyoubi());
				}
				excel.write("denpyou_id"		, 0, row, d.getDenpyouId());
				excel.write("denpyou_shubetsu"	, 0, row, d.getDenpyouShubetsu());
				excel.write("shiharaibi"		, 0, row, d.getShiharaibi());
				
				// 社員番号が前段と一致していたら同一ユーザーとみなして社員番号を表示しない
				if (i != 0) {
					if (meisaiList.get(i-1).shainNo.equals(d.shainNo)) {
						excel.write("shain_no", 0, row, "");
						//名前も一緒
						if (meisaiList.get(i-1).userFullName.equals(d.userFullName)) {
							excel.write("shain_name", 0, row, "");
						}
						//部門も一緒
						if (meisaiList.get(i-1).bumonFullName.equals(d.bumonFullName)) {
							excel.write("kihyou_bumon", 0, row, "");
						}
					}
				}
			}
			
			//サマリ有無によらず共通の出力部分（金額欄）
			excel.write("genkin_shiharai"	, 0, row, EteamXlsFmt.formatMoney(d.getGenkinShiharai()));
			excel.write("furikomi_shiharai"	, 0, row, EteamXlsFmt.formatMoney(d.getFurikomiShiharai()));
			if (houjinRiyou) {
				excel.write("houjin_shiharai"	, 0, row, EteamXlsFmt.formatMoney(d.getHoujinShiharai()));
			}
			if (tehaiRiyou) {
				excel.write("tehai_shiharai"	, 0, row, EteamXlsFmt.formatMoney(d.getTehaiShiharai()));
			}
			excel.write("karibarai"			, 0, row, EteamXlsFmt.formatMoney(d.getKaribarai()));
			if(d.getGenkinSashihiki().doubleValue() < 0) {
				excel.writeWithColor("genkin_sashihiki"	, 0, row, EteamXlsFmt.formatMoney(d.getGenkinSashihiki()), jxl.format.Colour.RED);
			} else {
				excel.write("genkin_sashihiki"	, 0, row, EteamXlsFmt.formatMoney(d.getGenkinSashihiki()));
			}
			if(d.getFurikomiSashihiki().doubleValue() < 0) {
				excel.writeWithColor("furikomi_sashihiki"	, 0, row, EteamXlsFmt.formatMoney(d.getFurikomiSashihiki()), jxl.format.Colour.RED);
			} else {
				excel.write("furikomi_sashihiki", 0, row, EteamXlsFmt.formatMoney(d.getFurikomiSashihiki()));
			}
			
			// 社員別合計
			if (! isShainBetsuSummary && d.getShainGoukei() != null) {
				row++;
				ShainGoukeiShikyuuData s = d.getShainGoukei();
				excel.write("denpyou_shubetsu"	, 0, row, "合計");
				excel.write("genkin_shiharai"	, 0, row, EteamXlsFmt.formatMoney(s.getSumGenkinShiharai()));
				excel.write("furikomi_shiharai"	, 0, row, EteamXlsFmt.formatMoney(s.getSumFurikomiShiharai()));
				if (houjinRiyou) {
					excel.write("houjin_shiharai"	, 0, row, EteamXlsFmt.formatMoney(s.getSumHoujinShiharai()));
				}
				if (tehaiRiyou) {
					excel.write("tehai_shiharai"	, 0, row, EteamXlsFmt.formatMoney(s.getSumTehaiShiharai()));
				}
				excel.write("karibarai"			, 0, row, EteamXlsFmt.formatMoney(s.getSumKaribarai()));
				if(s.getSumGenkinSashihiki().doubleValue() < 0) {
					excel.writeWithColor("genkin_sashihiki"	, 0, row, EteamXlsFmt.formatMoney(s.getSumGenkinSashihiki()), jxl.format.Colour.RED);
				} else {
					excel.write("genkin_sashihiki"	, 0, row, EteamXlsFmt.formatMoney(s.getSumGenkinSashihiki()));
				}
				if(s.getSumFurikomiSashihiki().doubleValue() < 0) {
					excel.writeWithColor("furikomi_sashihiki"	, 0, row, EteamXlsFmt.formatMoney(s.getSumFurikomiSashihiki()), jxl.format.Colour.RED);
				} else {
					excel.write("furikomi_sashihiki", 0, row, EteamXlsFmt.formatMoney(s.getSumFurikomiSashihiki()));
				}
			}
		}
		
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
}