package eteam.gyoumu.tsuuchi;

import java.io.OutputStream;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import jxl.Cell;
import jxl.write.WritableWorkbook;

/**
 * 法人カード利用明細XlsLogic
 */
public class HoujinCardRiyouMeisaiXlsLogic extends EteamAbstractLogic {
	
	/** 法人カード利用明細PDF出力用excel作成
	 * @param meisaiList 検索結果リスト(明細)
	 * @param riyouBiFrom 利用日From
	 * @param riyouBiTo 利用日To
	 * @param out 出力先
	 */ 
	public void makeExcel(List<GMap> meisaiList, String riyouBiFrom, String riyouBiTo, OutputStream out) {
		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		HoujinCardRiyouMeisaiLogic houjinCardRiyouMeisaiLogic = EteamContainer.getComponent(HoujinCardRiyouMeisaiLogic.class);
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		String template = "template_houjinCardRiyouMeisai.xls";
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream(template), out);
		WritableWorkbook book = excel.getBook();
		Cell meisaiCell = book.findCellByName("shain_no");
		int meisaiRow = meisaiCell.getRow();

		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		
		//■データ部
		
		if (riyouBiFrom.equals("") && riyouBiTo.equals("")) {
			excel.write("riyoubi_from_to", "");
		} else if (riyouBiFrom.equals("")) {
			excel.write("riyoubi_from_to", " ～ " + riyouBiTo);
		} else if (riyouBiTo.equals("")) {
			excel.write("riyoubi_from_to", riyouBiFrom + " ～ ");
		} else {
			excel.write("riyoubi_from_to", riyouBiFrom + " ～ " + riyouBiTo);
		}
		
		/* データ編集 */
		List<GMap> dataList = houjinCardRiyouMeisaiLogic.makeIchiranData(meisaiList, true);
		/* データ書込み */
		if (1 < dataList.size()) {
			excel.copyRow(meisaiRow, meisaiRow + 1, 1, dataList.size(), true);
		}
		for (int i = 0; i < dataList.size(); i++) {
			GMap map = dataList.get(i);
			
			excel.write("shain_no"				, 0, i, map.get("shain_no") == null ? "" : map.get("shain_no").toString());
			excel.write("shain_name"			, 0, i, map.get("user_full_name") == null ? "" : map.get("user_full_name").toString());
			excel.write("kihyou_card_num"		, 0, i, map.get("houjin_card_shikibetsuyou_num") == null ? "" : map.get("houjin_card_shikibetsuyou_num").toString());
			excel.write("riyoubi"				, 0, i, EteamXlsFmt.formatDate(map.get("riyoubi")));
			excel.write("denpyou_id"			, 0, i, map.get("denpyou_id") == null ? "" : map.get("denpyou_id").toString());
			excel.write("denpyou_shubetsu"		, 0, i, map.get("denpyou_shubetsu") == null ? "" : map.get("denpyou_shubetsu").toString());
			String naiyouBikouTekiyou = map.get("naiyou_bikou_tekiyou") == null ? "" : map.get("naiyou_bikou_tekiyou").toString();
			excel.write("naiyou_bikou_tekiyou"	, 0, i, naiyouBikouTekiyou);
			excel.write("riyou_kingaku"			, 0, i, EteamXlsFmt.formatMoney(map.get("riyou_kingaku")));
			
			//内容・備考・摘要の行初期化
			int tekiyouLineNum = 1;
			//高さ調整：摘要と備考は1行35文字以内で計算。デフォルト1行だけどそれ以上の行数になるなら高さ変える。
			int tekiyouLineNumTmp = excel.lineCount(naiyouBikouTekiyou, 70, 1);
			if( tekiyouLineNum < tekiyouLineNumTmp){
				tekiyouLineNum = tekiyouLineNumTmp;
			}
			excel.setHeight(meisaiRow + i,  tekiyouLineNum, 1);
		}
		
		// 総合計
		excel.write("naiyou_bikou_tekiyou"	, 0, dataList.size(), "総合計");
		excel.write("riyou_kingaku"			, 0, dataList.size(), EteamXlsFmt.formatMoney(houjinCardRiyouMeisaiLogic.sumAllHoujinCardRiyou));
		
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();

	}
	

}