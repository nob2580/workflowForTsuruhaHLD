package eteam.gyoumu.keihi;

import java.io.OutputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamXls;
import eteam.base.GMap;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiBumon;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiEdaban;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiKamoku;
import eteam.gyoumu.keihi.KeihiMeisaiLogic.KeihiSyuukeiBumon;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 経費明細表Logic
 */
public class KeihiMeisaihyoXlsLogic extends EteamAbstractLogic {

//＜定数＞
	/** テンプレート(ヘッダー)行 */
	protected static final int TEMPLATE_HEADER_ROW = 0;
	/** テンプレート(空)行 */
	protected static final int TEMPLATE_EMPTY_ROW = 6;
	/** テンプレート(枝番仕訳)行 */
	protected static final int TEMPLATE_EDABAN_MEISAI_ROW = 7;
	/** テンプレート(枝番小計)行 */
	protected static final int TEMPLATE_EDABAN_GOUKEI_ROW = 8;
	/** テンプレート(科目合計)行 */
	protected static final int TEMPLATE_KAMOKU_GOUKEI_ROW = 9;
	/** テンプレート(部門総合計)行 */
	protected static final int TEMPLATE_BUMON_GOUKEI_ROW = 10;
	/** 開始行 */
	protected static final int START_ROW = 0;
	/** ヘッダー行の行数 */
	protected static final int HEADER_ROW_COUNT = 7;
	/** 1ページあたりの行数 */
	protected static final int PAGE_ROW_COUNT = 43;
	/** 列位置（部門名） */
	protected static final int BUMON_COL   = 0 ;
	/** 列位置（科目名） */
	protected static final int KAMOKU_COL   = 1 ;
	/** 列位置（枝番名） */
	protected static final int EDABAN_COL   = 2 ;
	/** 列位置（日付） */
	protected static final int HIDUKE_COL   = 3;
	/** 列位置（相手先） */
	protected static final int TORIHIKI_COL = 4;
	/** 列位置（摘要） */
	protected static final int TEKIYOU_COL  = 5;
	/** 列位置（金額） */
	protected static final int KINGAKU_COL  = 6;
	/** 列位置（ページ数） */
	protected static final int PAGE_COL     = 7;

//＜インスタンス変数＞
	/** 出力対象のEXCELファイル */
	EteamXls excel;
	/** 出力対象のEXCELブック */
	WritableWorkbook book;
	/** 出力対象のEXCELシート */
	WritableSheet sheet;
	/** 日付フォーマット(MM/dd) */
	SimpleDateFormat dfm;
	/** 金額フォーマット(#,###) */
	DecimalFormat mfm;
	
	/** 経費明細部門表示フラグ */
	boolean showBumon = "1".equals(setting.keihimeisaiBumon());
	/** 経費明細枝番表示フラグ */
	boolean showEdaban = "1".equals(setting.keihimeisaiEdano());

	/**
	 * 個人別残高一覧のEXCEL出力
	 * @param syuukeiBumonList 経費明細データ
	 * @param targetDate 対象日付
	 * @param syuukeiBumonName 集計部門名
	 * @param out 出力先
	 */
	public void makeExcel(List<KeihiSyuukeiBumon> syuukeiBumonList, Date targetDate, String syuukeiBumonName, OutputStream out) {

		dfm = new SimpleDateFormat("MM/dd");
		mfm = new DecimalFormat("#,###");
		excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_keihimeisaihyo.xls"), out);
		book = excel.getBook();
		sheet = book.getSheet(0);

		// ---------------------------------------
		// ヘッダー部(1～2行目)書き込み。※管理部門名と頁数は頁別にループ処理内で書き込み。
		// ---------------------------------------
		int targetDateCol = BUMON_COL + (showBumon ? 0:1);
		excel.write(targetDateCol, START_ROW, new SimpleDateFormat("yyyy").format(targetDate).toString() + "年" + new SimpleDateFormat("MM").format(targetDate).toString() + "月度");
		excel.write("header_date", new SimpleDateFormat("yyyy/MM/dd").format(System.currentTimeMillis()));
		
		// 対象0件の場合、体裁だけ整えて終了。部門名は設定不可
		if (syuukeiBumonList.isEmpty()){
			//ヘッダー
			excel.write("header_page", 1 + "頁");
			//合計行上の罫線
			setTopBorder(TEMPLATE_EDABAN_GOUKEI_ROW);
			setTopBorder(TEMPLATE_KAMOKU_GOUKEI_ROW);
			setTopBorder(TEMPLATE_BUMON_GOUKEI_ROW);
			//不要項目隠す
			if(!showEdaban) sheet.removeColumn(EDABAN_COL);
			if(!showBumon) sheet.removeColumn(BUMON_COL);
			//日付列幅調整
			sheet.setColumnView(HIDUKE_COL + (showBumon ? 0:-1) + (showEdaban ? 0:-1), 8);
			excel.closeBook();
			return;
		}
		
		// ---------------------------------------
		// 仕訳単位ループ（１行ずつ書いてく）
		// ---------------------------------------
		int pageNo = 0; //現在頁数
		int row = START_ROW; //現在行数(row -1行目までは書き出し済、次ここに書き出し)
		int pageRowCount = 0; //現在頁内出力済行数※部門が変わるか同部門内でPAGE_ROW分書き込みしたら改頁する
		boolean pageBreak = false; //次改頁する印
		boolean first = true; //初行の印
		//集計部門ループ
		for (int m = 0; m < syuukeiBumonList.size(); m++) {
			KeihiSyuukeiBumon sBumon = syuukeiBumonList.get(m);
			pageBreak = true;
			//部門ループ
			for (int i = 0; i < sBumon.getBumonList().size(); i++) {
				KeihiBumon bumon = sBumon.getBumonList().get(i);
				pageBreak = true;
				//科目ループ
				for (int j = 0; j < bumon.getKamokuList().size(); j++) {
					KeihiKamoku kamoku = bumon.getKamokuList().get(j);
					//枝番ループ
					for (int k = 0; k < kamoku.getEdabanList().size(); k++) {
						KeihiEdaban edaban = kamoku.getEdabanList().get(k);
						//仕訳ループ
						for (int z = 0; z < edaban.getShiwakeList().size(); z++) {
							GMap map = edaban.getShiwakeList().get(z);
		
							//ヘッダー行 ※1頁目はテンプレート行(1-5行目)をそのまま使う。2頁目以降はテンプレート行(1-5行目)を各頁の先頭にコピー
							if (pageBreak){
								if (! first) {
									excel.copyRow(TEMPLATE_HEADER_ROW, row, HEADER_ROW_COUNT, 1, false);
									sheet.addRowPageBreak(row);
								}
								writeHeader(row, map.get("oya_syuukei_bumon_name").toString(), ++pageNo);
								row += HEADER_ROW_COUNT; pageRowCount = HEADER_ROW_COUNT;
								if (first) {
									row += 4;//テンプレート行（明細と合計)を飛ばす為
								}
		
								//ページングリセット
								pageRowCount = HEADER_ROW_COUNT;
								pageBreak = false;
								first = false;
							}
		
							//仕訳行
							excel.copyRow(TEMPLATE_EDABAN_MEISAI_ROW, row, 1, 1, false);
							boolean headFlg = (pageRowCount == HEADER_ROW_COUNT);
							writeMeisai(row, map, ((i == 0 && k == 0 && z == 0)|| headFlg), ((k == 0 && z == 0) || headFlg), (z == 0 || headFlg) );//科目の先頭 or 頁先頭行のみ科目名出す
							row += 1; pageRowCount += 1;
		
							//枝番小計行
							if (z == edaban.getShiwakeList().size() - 1 && showEdaban) {
								excel.copyRow(TEMPLATE_EDABAN_GOUKEI_ROW, row, 1, 1, false);
								excel.copyRow(TEMPLATE_EMPTY_ROW, row + 1, 1, 1, false);
								writeGoukeiEdaban(row, map);
								row += 2; pageRowCount += 2;
							}
							//科目合計行
							if (k == kamoku.getEdabanList().size() - 1 && z == edaban.getShiwakeList().size() - 1) {
								excel.copyRow(TEMPLATE_KAMOKU_GOUKEI_ROW, row, 1, 1, false);
								excel.copyRow(TEMPLATE_EMPTY_ROW, row + 1, 1, 1, false);
								writeGoukeiKamoku(row, map);
								setTopBorder( row + 1 );
								row += 2; pageRowCount += 2;
							}
							//部門総合計行
							if (j == bumon.getKamokuList().size() - 1 && k == kamoku.getEdabanList().size() - 1 && z == edaban.getShiwakeList().size() - 1 && showBumon) {
								excel.copyRow(TEMPLATE_BUMON_GOUKEI_ROW, row, 1, 1, false);
								excel.copyRow(TEMPLATE_EMPTY_ROW, row + 1, 1, 1, false);
								writeGoukeiBumon(row, map);
								row += 2; pageRowCount += 2;
							}
		
							//部門の区切り or 部門内で一定行出したら改頁
							if (
								( j == bumon.getKamokuList().size() - 1 && k == kamoku.getEdabanList().size() - 1 && z == edaban.getShiwakeList().size() - 1) ||
								(pageRowCount >= PAGE_ROW_COUNT)
							) {
								//空行挿入
								int emptyRowCount = PAGE_ROW_COUNT + 2 - pageRowCount;
								if (emptyRowCount > 0) {
									excel.copyRow(TEMPLATE_EMPTY_ROW, row, 1, emptyRowCount, false);
									row += emptyRowCount; pageRowCount += emptyRowCount;
								}
								//次のターンでヘッダー行処理
								pageBreak = true;
							}
						}
						
						
						
					}
					
					
				}
			}
		}
		// ---------------------------------------
		// テンプレート行（明細、合計のみ。ヘッダー行は１頁目として残す）
		// ---------------------------------------
		if(!showEdaban) sheet.removeColumn(EDABAN_COL);
		if(!showBumon) sheet.removeColumn(BUMON_COL);
		sheet.removeRow(TEMPLATE_BUMON_GOUKEI_ROW);
		sheet.removeRow(TEMPLATE_KAMOKU_GOUKEI_ROW);
		sheet.removeRow(TEMPLATE_EDABAN_GOUKEI_ROW);
		sheet.removeRow(TEMPLATE_EDABAN_MEISAI_ROW);
		
		//日付列幅調整
		sheet.setColumnView(HIDUKE_COL + (showBumon ? 0:-1) + (showEdaban ? 0:-1), 8);

		// ---------------------------------------
		//EXCEL出力 メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}

	/**
	 * ヘッダー書き出し
	 * @param row 書き出し行
	 * @param syuukeiBumon 集計部門名
	 * @param pageNo 頁数
	 */
	protected void writeHeader(int row, String syuukeiBumon, int pageNo) {
		int bumonCol = KAMOKU_COL + (showBumon ? -1:0);
		excel.write(bumonCol, row + 3, syuukeiBumon);
		excel.write(PAGE_COL, row + 2, pageNo + "頁");
	}

	/**
	 * 明細行書き出し
	 * @param row 書き出し行
	 * @param map 経費明細レコード
	 * @param bumonFlg 部門名表示有無
	 * @param kamokuFlg 科目名表示有無
	 * @param edabanFlg 科目枝番名表示有無
	 */
	protected void writeMeisai(int row, GMap map, boolean bumonFlg, boolean kamokuFlg, boolean edabanFlg) {
		if (bumonFlg && showBumon) {
			excel.write(BUMON_COL,   row, map.get("futan_bumon_name").toString());
		}
		if (kamokuFlg) {
			excel.write(KAMOKU_COL,   row, map.get("kamoku_name_ryakushiki").toString());
		}
		if (edabanFlg && showEdaban) {
			excel.write(EDABAN_COL,   row, n2b(map.get("edaban_name")));
		}
		excel.write(HIDUKE_COL,       row, dfm.format(map.get("dymd")).toString());
		excel.write(TORIHIKI_COL,     row, n2b(map.get("torihikisaki_name_ryakushiki")));
		excel.write(TEKIYOU_COL,      row, map.get("tky").toString());
		excel.write(KINGAKU_COL,      row, mfm.format(map.get("valu")));
	}

	/**
	 * 合計行書き出し(枝番小計)
	 * @param row 書き出し行
	 * @param map 経費明細レコード
	 */
	protected void writeGoukeiEdaban(int row, GMap map) {
		excel.write(KINGAKU_COL, row, mfm.format(map.get("total_eda")));
		for (int col = EDABAN_COL; col <= KINGAKU_COL; col++) {
			excel.setBorder(row, col, Border.TOP, BorderLineStyle.THIN);
		}
	}
	/**
	 * 合計行書き出し(科目合計)
	 * @param row 書き出し行
	 * @param map 経費明細レコード
	 */
	protected void writeGoukeiKamoku(int row, GMap map) {
		excel.write(KINGAKU_COL, row, mfm.format(map.get("total_kmk")));
		for (int col = KAMOKU_COL; col <= KINGAKU_COL; col++) {
			excel.setBorder(row, col, Border.TOP, BorderLineStyle.THIN);
		}
	}
	/**
	 * 合計行書き出し(部門総合計)
	 * @param row 書き出し行
	 * @param map 経費明細レコード
	 */
	protected void writeGoukeiBumon(int row, GMap map) {
		excel.write(KINGAKU_COL, row, mfm.format(map.get("total_bmn")));
		for (int col = BUMON_COL; col <= KINGAKU_COL; col++) {
			excel.setBorder(row, col, Border.TOP, BorderLineStyle.THIN);
		}
	}

	/**
	 * 指定行の上罫線を引く
	 * @param row 指定行
	 */
	protected void setTopBorder(int row){
		for (int col = KAMOKU_COL; col <= KINGAKU_COL; col++) {
			excel.setBorder(row, col, Border.TOP, BorderLineStyle.THIN);
		}
	}
}