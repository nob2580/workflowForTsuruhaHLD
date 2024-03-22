package eteam.gyoumu.kanitodoke;

import java.io.OutputStream;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.gyoumu.workflow.WorkflowXlsLogic;
import jxl.format.Border;
import jxl.format.BorderLineStyle;

/**
 * ユーザー定義届書Logic
 */
public class KanitodokeXlsLogic extends EteamAbstractLogic {
	
	/**
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param out 出力先
	 * @param sokyuuFlg 遡及フラグ
	 */ 
	public void makeExcel(String denpyouId, OutputStream out, String version, String denpyouKbn, boolean sokyuuFlg) {

		KaniTodokeCategoryLogic kaniLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		WorkflowXlsLogic wfXlsLogic = EteamContainer.getComponent(WorkflowXlsLogic.class, connection);

		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_kanitodoke.xls"), out);

		
		// ---------------------------------------
		//共通部分（下部）
		// ---------------------------------------
		wfXlsLogic.makeExcelBottom(excel, denpyouId);

		
		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		GMap mapKaniTodokeName   =   kaniLogic.findKaniTodokeName(denpyouKbn, version);
		List<GMap> 		hontaiDataList =	kaniLogic.loadData(AREA_KBN.SHINSEI, denpyouKbn, version, denpyouId);
		List<GMap> 		hontaiKoumokuList =	kaniLogic.loadLayout(AREA_KBN.SHINSEI, denpyouKbn,version);
		List<GMap> 		meisaiDataList =	kaniLogic.loadData(AREA_KBN.MEISAI, denpyouKbn, version, denpyouId);
		List<GMap> 		meisaiKoumokuList =	kaniLogic.loadLayout(AREA_KBN.MEISAI, denpyouKbn,version);
		List<GMap> 		meisaiOption =	kaniLogic.loadOption(AREA_KBN.MEISAI, denpyouKbn,version);
		List<GMap> 		hontaiOption =	kaniLogic.loadOption(AREA_KBN.SHINSEI, denpyouKbn,version);

		
		// ---------------------------------------
		// 伝票種別名
		// ---------------------------------------
		excel.write("denpyoumei", (String)mapKaniTodokeName.get("denpyou_shubetsu"));

		
		// ---------------------------------------
		// 明細部の書き込み
		// ---------------------------------------
		
		//この２行をテンプレートとする、最後に隠す
		int meisaiHeaderTemplateRow = excel.getCell("syutsuryoku_meisai").getRow();
		int meisaiContentTemplateRow = meisaiHeaderTemplateRow + 1;
		int nextRow = meisaiHeaderTemplateRow + 2;
		int meisaiNo = 0;

		//※※※構造が面倒くさいので解説※※※
		//meisaiKoumokuListが項目表示順のリストで、meisaiDataListが明細枝番順、項目表示順の実質２重リスト
		//明細項目数=2・明細件数=3の場合だと、meisaiKoumokuList.size=2・meisaiDataList.size=6で、
		//　meisaiDataList[0]=明細１・項目１
		//　meisaiDataList[1]=明細１・項目２
		//　meisaiDataList[2]=明細２・項目１
		//　meisaiDataList[3]=明細２・項目２
		//　meisaiDataList[4]=明細３・項目１
		//　meisaiDataList[5]=明細３・項目２

		//明細枝番順×項目順の実質２重ループ
		for (int i = 0; i < meisaiDataList.size(); i++) {

			if("meisai01".equals(meisaiDataList.get(i).get("item_name"))){
				meisaiNo++;

				//明細２以降は１つ前の明細との空白行確保
				if(meisaiNo >= 2){
					excel.copyRow(meisaiHeaderTemplateRow, nextRow, 1, 1, true); 
					nextRow++;
				}

				//明細ヘッダ「■明細#1」、「■明細#2」とかを書く
				excel.copyRow(meisaiHeaderTemplateRow, nextRow, 1, 1, true);
				excel.write(0, nextRow, "■明細#" + Integer.toString(meisaiNo)); 
				nextRow++;
			}
			
			//項目内容文字数による高さ計算・・・内容は１行４１文字以内で計算。デフォルト1行だけどそれ以上の行数になるならセル分割。
			String naiyou = convert(meisaiDataList.get(i), meisaiKoumokuList.get(i % meisaiKoumokuList.size()), meisaiOption);
			List<String> naiyouSplit = excel.splitLine(naiyou, 82);

			//テンプレート行をこの項目で要する行数分確保・・・普通は１行だけどテキストエリアとかは１項目複数行になりえる
			excel.copyRow(meisaiContentTemplateRow, nextRow, 1, naiyouSplit.size(), true);
			if(naiyouSplit.size() >= 2) {
				//項目ラベルと項目内容の周りを罫線で囲んだ状態にする・・・中間業の罫線を消す
				//１行目　下なし
				excel.setBorder(nextRow, 1, Border.BOTTOM, BorderLineStyle.NONE);
				excel.setBorder(nextRow, 5, Border.BOTTOM, BorderLineStyle.NONE);
				//途中行　上下なし（３行以上じゃないと該当なし）
				for(int j = 1; j < naiyouSplit.size() - 1; j++) {
					excel.setBorder(nextRow + j, 1, Border.TOP, BorderLineStyle.NONE);
					excel.setBorder(nextRow + j, 1, Border.BOTTOM, BorderLineStyle.NONE);
					excel.setBorder(nextRow + j, 5, Border.TOP, BorderLineStyle.NONE);
					excel.setBorder(nextRow + j, 5, Border.BOTTOM, BorderLineStyle.NONE);

				}
				//最終行　上なし
				excel.setBorder(nextRow + naiyouSplit.size() - 1, 1, Border.TOP, BorderLineStyle.NONE);
				excel.setBorder(nextRow + naiyouSplit.size() - 1, 5, Border.TOP, BorderLineStyle.NONE);
			}

			//項目ラベル　書く
			GMap koumoku2 = meisaiKoumokuList.get(i % meisaiKoumokuList.size());
			excel.write(1, nextRow, koumoku2.get("label_name"));
			
			//項目内容　書く
			for(int j = 0; j < naiyouSplit.size(); j++) {
				excel.write(5, nextRow + j, naiyouSplit.get(j));
			}
			nextRow += naiyouSplit.size();
		}

		//テンプレート行は非表示
		excel.hideRow(meisaiHeaderTemplateRow);
		excel.hideRow(meisaiContentTemplateRow);

		
		// ---------------------------------------
		// 申請内容部の書き込み
		// ---------------------------------------
		//この１行をテンプレートとする、最後に隠す
		int hontaiContentTemplateRow = excel.getCell("shinsei").getRow();
		nextRow = hontaiContentTemplateRow + 1;
		
		//１項目ずつ書き込み
		for (int i = 0; i < hontaiDataList.size(); i++) {
			
			//項目内容文字数による高さ計算・・・内容は１行４１文字以内で計算。デフォルト1行だけどそれ以上の行数になるならセル分割。
			String naiyou = convert(hontaiDataList.get(i), hontaiKoumokuList.get(i), hontaiOption);
			List<String> naiyouSplit = excel.splitLine(naiyou, 82);

			//テンプレート行をこの項目で要する行数分確保・・・普通は１行だけどテキストエリアとかは１項目複数行になりえる
			excel.copyRow(hontaiContentTemplateRow, nextRow, 1, naiyouSplit.size(), true);
			if(naiyouSplit.size() >= 2) {
				//項目ラベルと項目内容の周りを罫線で囲んだ状態にする・・・中間業の罫線を消す
				//１行目　下なし
				excel.setBorder(nextRow, 1, Border.BOTTOM, BorderLineStyle.NONE);
				excel.setBorder(nextRow, 5, Border.BOTTOM, BorderLineStyle.NONE);
				//途中行　上下なし（３行以上じゃないと該当なし）
				for(int j = 1; j < naiyouSplit.size() - 1; j++) {
					excel.setBorder(nextRow + j, 1, Border.TOP, BorderLineStyle.NONE);
					excel.setBorder(nextRow + j, 1, Border.BOTTOM, BorderLineStyle.NONE);
					excel.setBorder(nextRow + j, 5, Border.TOP, BorderLineStyle.NONE);
					excel.setBorder(nextRow + j, 5, Border.BOTTOM, BorderLineStyle.NONE);
				}
				//最終行　上なし
				excel.setBorder(nextRow + naiyouSplit.size() - 1, 1, Border.TOP, BorderLineStyle.NONE);
				excel.setBorder(nextRow + naiyouSplit.size() - 1, 5, Border.TOP, BorderLineStyle.NONE);
			}

			//項目ラベル　書く
			GMap koumoku = hontaiKoumokuList.get(i);
			excel.write(1, nextRow, koumoku.get("label_name"));
			
			//項目内容　書く
			for(int j = 0; j < naiyouSplit.size(); j++) {
				excel.write(5, nextRow + j, naiyouSplit.get(j));
			}
			nextRow += naiyouSplit.size();
		}

		//テンプレート行は非表示
		excel.hideRow(hontaiContentTemplateRow);

		
		// ---------------------------------------
		//共通部分
		// ---------------------------------------
		wfXlsLogic.makeExcel(excel, denpyouId, sokyuuFlg);

		
		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}
	
	/**
	 * 項目リストを元に明細または申請の内容を変換する。
	 * 
	 * @param meisaiOrShinsei 明細または申請の内容
	 * @param handan 項目リスト
	 * @param listko リスト子
	 * @return 記載内容
	 */
	public static String convert(GMap meisaiOrShinsei, GMap handan, List<GMap> listko){

		String value1 = (String)meisaiOrShinsei.get("value1");
		String value2 = "";
		String buhin_type = (String)handan.get("buhin_type");
		String itemName = (String)handan.get("item_name");

		if(null != meisaiOrShinsei.get("value2")){
			value2 = (String)meisaiOrShinsei.get("value2");
		}
		if(buhin_type.equals("checkbox")){
			if (value1.equals("0")) {
				value1 = "×";
			} else {
				value1 = "○";
			}
			
		}else if(buhin_type.equals("radio")||buhin_type.equals("pulldown")){
			for(int x = 0; x < listko.size(); x++){
				GMap list = listko.get(x);
				if(itemName.equals(list.get("item_name")) && value1.equals(list.get("value"))){
					value1 = (String)list.get("text");
					break;
				}
			}
		}
		
		return value1 + " " + value2;
		
	}
}