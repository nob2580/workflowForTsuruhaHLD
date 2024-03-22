package eteam.gyoumu.shiharai;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dto.InvoiceStart;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import jxl.format.Colour;


/**
 * 
 */
public class ShiharaiIraiIchiranXlsLogic extends EteamAbstractLogic{

//＜定数＞
	/** 統括：１頁の出力行数 */
	protected static int TOUKATSU_ROWLIMIT = 22;
	/** 依頼書：１頁の出力行数 */
	protected static int IRAISHO_ROWLIMIT = 45;
//＜部品＞
	/** WF */
	WorkflowCategoryLogic	workflowLogic;
	/** 会計 */
	BatchKaikeiCommonLogic	batchkaikeilogic;
	/** WF設定 */
	NaibuCdSettingDao naibuCdSetting;
	/** インボイス開始処理 */
	InvoiceStartAbstractDao invoiceStartFlg;
	
	/**
	 * @param denpyouId	伝票ID
	 * @param out			出力先
	 */	
	public void makeExcel(String[] denpyouId, OutputStream out) {
		
		// ---------------------------------------
		//オブジェクトNEW
		// ---------------------------------------
		workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		naibuCdSetting = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		invoiceStartFlg = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);		
		
		// ---------------------------------------
		//テンプレートEXCEL開く
		// ---------------------------------------
		EteamXls excel = EteamXls.createBook(this.getClass().getResourceAsStream("template_shiharaiirai_ichiran.xls"), out);	
		
		makeToukatsu(denpyouId, excel);
		makeIraisho(denpyouId,  excel);

		// ---------------------------------------
		//EXCEL出力　メモリ上にEXCELファイルバイナリデータが作られる
		// ---------------------------------------
		excel.closeBook();
	}

	/**
	 * 統括シート作成
	 * @param denpyouIdArr	伝票ID
	 * @param xls			EXCEL
	 */
	protected void makeToukatsu(String[] denpyouIdArr, EteamXls xls) {
		xls.selectSheet(0);
		BigDecimal shiharaiSum = new BigDecimal(0);
		BigDecimal sousaiSum = new BigDecimal(0);
		BigDecimal gensenSum = new BigDecimal(0);
		BigDecimal sashihikiSum = new BigDecimal(0);

		List<Integer> pageRows = new ArrayList<>();
		int row = 11;
		int page = 0;
		int rowOnPage = 0;
		String preUnit = null;
		for (int i = 0; i < denpyouIdArr.length; i++) {
			
			//行単位＝申請単位のデータ取得
			String denpyouId = denpyouIdArr[i];
			GMap irai = findShiharaiIrai(denpyouId);						
			BigDecimal shiharai		= (BigDecimal)irai.get("shiharai_goukei");
			BigDecimal sousai		= (BigDecimal)irai.get("sousai_goukei");
			BigDecimal gensen		= (BigDecimal)irai.get("manekin_gensen");
			BigDecimal sashihiki	= (BigDecimal)irai.get("sashihiki_shiharai");
			String unit				= (String)irai.get("unit");
			//インボイス伝票フラグ取得
			boolean isInvoice = "0".equals(irai.get("invoice_denpyou"));
			InvoiceStart invoice = invoiceStartFlg.find();
			String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
			boolean isBeforeInvoice = "0".equals(invoiceStartFlg);
			
			if(!isBeforeInvoice && isInvoice) {
				xls.write("soukatsu_jijgyousha_kbn_title", "事業者区分");
			}
			
			//前のループまでの出力単位合計行書き込み
			if ((i > 0 && i != denpyouIdArr.length &&  ! unit.equals(preUnit))) {
				writeToukatsuTailer(xls, row, shiharaiSum, sousaiSum, gensenSum, sashihikiSum);
				shiharaiSum = new BigDecimal(0);
				sousaiSum = new BigDecimal(0);
				gensenSum = new BigDecimal(0);
				sashihikiSum = new BigDecimal(0);
				row++;
			}

			//改行判定 1ページ目が22行分入る
			boolean pageBreak = (rowOnPage % TOUKATSU_ROWLIMIT == 0);
			//依頼部門、支払方法、支払種別、支払予定日が変わったら改頁
			if (! unit.equals(preUnit)) pageBreak = true;
			if (pageBreak) {
				page++;
				writeToukatsuHeader(xls, irai, row, page);
				pageRows.add(row);
				if (i > 0) xls.pageBreak(row);
				row += 9;
				rowOnPage = 0;
			}
			
			//明細（伝票）単位の出力
			xls.copyRow(9, row, 1, 1, false);
			xls.write (0, row, StringUtils.leftPad(irai.get("serial_no").toString(), 8, '0'));
			xls.write (1, row, (String)irai.get("hassei_shubetsu"));
			xls.writeWithColor (2, row, (String)irai.get("shiharai_shubetsu_name"), irai.get("shiharai_shubetsu").equals(SHIHARAI_IRAI_SHUBETSU.SONOTA) ? Colour.RED : Colour.BLACK);
			xls.write (3, row, (String)irai.get("shiharai_houhou_name"));
			xls.write (4, row, (String)irai.get("torihikisaki_cd"));
			xls.write (5, row, (String)irai.get("torihikisaki_name_seishiki"));
			if (!isBeforeInvoice && isInvoice) {
				xls.write (7, row, (String)irai.get("jigyousha_kbn_name"));
			}
			xls.write (8, row, EteamXlsFmt.formatMoney(shiharai));
			xls.write (9, row, EteamXlsFmt.formatMoney(sousai));
			xls.write (10, row, EteamXlsFmt.formatMoney(gensen));
			xls.write (11, row, EteamXlsFmt.formatMoney(sashihiki));
			row++; rowOnPage++;

			//出力単位合計計算
			shiharaiSum		= shiharaiSum.add(shiharai);
			sousaiSum		= sousaiSum.add(sousai);
			gensenSum		= gensenSum.add(gensen);
			sashihikiSum	= sashihikiSum.add(sashihiki);
			preUnit	= unit;
		}
		
		//出力単位合計行書き込み
		writeToukatsuTailer(xls, row, shiharaiSum, sousaiSum, gensenSum, sashihikiSum);
		
		//頁(#/#)をまとめて書き込み
		for (int i = 0; i < pageRows.size(); i++) {
			xls.write(11, pageRows.get(i) + 1, "ページ　：　" + (i+1) + "/" + pageRows.size());
		}
		
		//テンプレート部分削除
		for (int i = 0; i <= 10; i++) xls.hideRow(i);
	}

	/**
	 * 統括シートヘッダー出力
	 * @param xls	EXCEL
	 * @param irai	データ
	 * @param row	行
	 * @param page	頁
	 */
	protected void writeToukatsuHeader(EteamXls xls, GMap irai, int row, int page) {
		xls.copyRow(0, row, 9, 1, false);
		xls.write(11,row	, "作成日　：　" + DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
		xls.write(4	,row + 1, "（ " + DateFormatUtils.format(new Date(), "yyyy年MM月dd日") + "　分 ）");
		xls.write(1	,row + 4, "：" + torimBumonSeparator((String)irai.get("kihyou_bumon_name")));
		xls.write(1	,row + 5, "：" + irai.get("shounin_user_name"));
		xls.write(1	,row + 6, "：" + EteamXlsFmt.formatDate(irai.get("yoteibi")));
		xls.write(4	,row + 6, "：" + EteamXlsFmt.formatDate(irai.get("shiharai_kijitsu")));
	}

	/**
	 * 統括シートフッター出力
	 * @param xls			EXCEL
	 * @param row			書き込み行数
	 * @param shiharaiSum	支払
	 * @param sousaiSum		相殺
	 * @param gensenSum		源泉
	 * @param sashihikiSum	差引
	 */
	protected void writeToukatsuTailer(EteamXls xls, int row, BigDecimal shiharaiSum, BigDecimal sousaiSum, BigDecimal gensenSum, BigDecimal sashihikiSum) {
		xls.copyRow(10, row, 1, 1, false);
		xls.write(8		, row, EteamXlsFmt.formatMoney(shiharaiSum));
		xls.write(9		, row, EteamXlsFmt.formatMoney(sousaiSum));
		xls.write(10	, row, EteamXlsFmt.formatMoney(gensenSum));
		xls.write(11	, row, EteamXlsFmt.formatMoney(sashihikiSum));
	}


	/**
	 * 依頼書シート作成
	 * @param denpyouIdArr	伝票ID
	 * @param xls			EXCEL
	 */
	protected void makeIraisho(String[] denpyouIdArr, EteamXls xls) {
		xls.selectSheet(1);

		//ヘッダー
		xls.write("title_manekin", setting.manekinName());
		
		List<Integer> pageRows = new ArrayList<>();
		int row = 25;
		int rowCount = 0;
		int page = 0;
		String preUnit = null;
		String 			jigyoushaKbn	= "通常課税";
		//消費税額詳細
		BigDecimal karibaraiZeigaku10PercentDecimal = new BigDecimal(0);
		BigDecimal karibaraiZeigaku10PercentMenzei80Decimal = new BigDecimal(0);
		BigDecimal karibaraiZeigaku8PercentDecimal = new BigDecimal(0);
		BigDecimal karibaraiZeigaku8PercentMenzei80Decimal = new BigDecimal(0);
		
		for (int i = 0; i < denpyouIdArr.length; i++) {
			//初期化
			karibaraiZeigaku10PercentDecimal = BigDecimal.ZERO;
			karibaraiZeigaku10PercentMenzei80Decimal = BigDecimal.ZERO;;
			karibaraiZeigaku8PercentDecimal = BigDecimal.ZERO;;
			karibaraiZeigaku8PercentMenzei80Decimal = BigDecimal.ZERO;;
			
			//行単位＝申請単位のデータ取得
			String 		denpyouId 	= denpyouIdArr[i];
			GMap		irai		= findShiharaiIrai(denpyouId);
			String		unit		= (String)irai.get("unit");
			List<GMap>	meisaiList	= loadMeisai(denpyouId);
			Colour 		color 		= irai.get("shiharai_shubetsu").equals(SHIHARAI_IRAI_SHUBETSU.SONOTA) ? Colour.RED : Colour.BLACK;
			boolean isInvoice = "0".equals(irai.get("invoice_denpyou"));
			InvoiceStart invoice = invoiceStartFlg.find();
			String invoiceStartFlg = invoice == null ? "0" : invoice.invoiceFlg;
			boolean isBeforeInvoice = "0".equals(invoiceStartFlg);
			//インボイス追加項目ヘッダー
			if(!isBeforeInvoice && isInvoice) {
				xls.write("jigyousha_kbn_title", "事業者区分");
				xls.write("zeinuki_kingaku_title", "税抜金額");
				xls.write("shouhizeigaku_title", "消費税額");
			}
			
			//改頁判定
			if (! unit.equals(preUnit) || rowCount >= IRAISHO_ROWLIMIT ) {
				page++;
				writeIraishoHeader(xls, irai, row, page);
				pageRows.add(row);
				if (i > 0) {
					xls.pageBreak(row);
				}
				row 	+= 9;
				rowCount = 0;
			}

			//本体行の出力
			xls.copyRow(9, row, 4, 1, false);

			xls.write			(1	, row	, StringUtils.leftPad(irai.get("serial_no").toString(), 8, '0'));
			xls.write			(3	, row	, EteamXlsFmt.formatDate(irai.get("keijoubi")));
			xls.write			(4	, row	, (String)irai.get("shiharai_houhou_name"));
			xls.writeWithColor	(5	, row	, (String)irai.get("shiharai_shubetsu_name"), color);
			xls.write			(6	, row	, EteamXlsFmt.formatDate(irai.get("yoteibi")));
			xls.write			(7	, row	, EteamXlsFmt.formatDate(irai.get("shiharai_kijitsu")));
			xls.write			(8	, row	, (String)irai.get("hf1_cd"));
			xls.write			(9	, row	, (String)irai.get("edi"));

			xls.writeWithColor	(1	, row+1	, (String)irai.get("torihikisaki_cd"), color);
			xls.write			(3	, row+1	, (String)irai.get("torihikisaki_name_seishiki"));
			xls.writeWithColor	(5	, row+1	, (String)irai.get("furikomi_ginkou_name"), color);
			xls.writeWithColor	(6	, row+1	, (String)irai.get("furikomi_ginkou_shiten_name"), color);
			xls.write			(7	, row+1	, n2b(irai.get("yokin_shubetsu_name")) + " " + irai.get("kouza_bangou") + " " + irai.get("kouza_meiginin"));
			if (!isEmpty(irai.get("tesuuryou")))
			xls.write			(10	, row+1	, irai.get("tesuuryou").equals("1") ? "先方負担" : "自社負担");

			if (irai.get("shiharai_shubetsu").equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)) {
				xls.writeWithColor	(1	, row+2	, "不突合い", color);
			}
			if ("1".equals(irai.get("jigyousha_kbn"))) {
				jigyoushaKbn = "免税80%";
			}
			//インボイス伝票フラグ取得
			if (!isBeforeInvoice && isInvoice) {
				xls.write			(3	, row+2	, jigyoushaKbn);
			}
			xls.write			(5	, row+2	, EteamXlsFmt.formatMoney(irai.get("shiharai_goukei")));
			xls.write			(7	, row+2	, EteamXlsFmt.formatMoney(irai.get("sousai_goukei")));
			xls.write			(9	, row+2	, EteamXlsFmt.formatMoney(irai.get("sashihiki_shiharai")));
			xls.write			(10	, row+2	, EteamXlsFmt.formatMoney(irai.get("manekin_gensen")));
			row += 4;	rowCount += 4;
			
			//明細単位の出力
			for (int j = 0; j < meisaiList.size(); j++) {

				//改頁判定
				if (rowCount >= IRAISHO_ROWLIMIT) {
					page++;
					writeIraishoHeader(xls, irai, row, page);
					pageRows.add(row);
					if (i > 0 || j > 0) {
						xls.pageBreak(row);
					}
					row 	+= 9;
					rowCount = 0;
				}
				
				GMap meisai = meisaiList.get(j);
				xls.copyRow(13, row, 2, 1, false);

				xls.write(2	, row	, meisai.get("denpyou_edano").toString());
				xls.write(3	, row	, meisai.get("kari_kamoku_cd").toString());
				xls.write(3	, row+1	, meisai.get("kari_kamoku_name").toString());
				xls.write(4	, row	, meisai.get("kari_kamoku_edaban_cd").toString());
				xls.write(4	, row+1	, meisai.get("kari_kamoku_edaban_name").toString());
				xls.write(5	, row	, meisai.get("kari_futan_bumon_cd").toString());
				xls.write(5	, row+1	, meisai.get("kari_futan_bumon_name").toString());
				xls.write(6	, row	, meisai.get("kari_kazei_kbn_name").toString());
				String zritsu = meisai.get("zeiritsu").toString();
				xls.write(7	, row	, zritsu + " %");
				xls.write(8	, row	, EteamXlsFmt.formatMoney(meisai.get("shiharai_kingaku")));
				xls.write(8	, row+1	, (String)meisai.get("tekiyou"));
				if (!isBeforeInvoice && isInvoice) {					
					xls.write(9 , row	, EteamXlsFmt.formatMoney(meisai.get("zeinuki_kingaku")));
					xls.write(10, row	, EteamXlsFmt.formatMoney(meisai.get("shouhizeigaku")));
					//消費税額詳細計算
					if ("0".equals(irai.get("jigyousha_kbn"))) {
						if ("10".equals(zritsu)) {
							karibaraiZeigaku10PercentDecimal = karibaraiZeigaku10PercentDecimal.add(meisai.get("shouhizeigaku"));
						}
						else if ("8".equals(zritsu)) {
							karibaraiZeigaku8PercentDecimal = karibaraiZeigaku8PercentDecimal.add(meisai.get("shouhizeigaku"));
						}
					} else {
						if ("10".equals(zritsu)) {
							karibaraiZeigaku10PercentMenzei80Decimal = karibaraiZeigaku10PercentMenzei80Decimal.add(meisai.get("shouhizeigaku"));
						}
						else if ("8".equals(zritsu)) {
							karibaraiZeigaku8PercentMenzei80Decimal = karibaraiZeigaku8PercentMenzei80Decimal.add(meisai.get("shouhizeigaku"));
						}
					}
				}
				row += 3;	rowCount += 3;
			}
			//消費税額詳細
			if(!isBeforeInvoice && isInvoice) {
				
				xls.copyRow(16, row, 4, 1, false);
			
				boolean isPrint = false;
				if ("0".equals(irai.get("jigyousha_kbn"))) {
					if(karibaraiZeigaku10PercentDecimal.compareTo(BigDecimal.ZERO) == 1) {
						xls.write(2, row+2, "10%" );
						xls.write(6, row+2, EteamXlsFmt.formatMoney(karibaraiZeigaku10PercentDecimal));
						isPrint = true;
					} else {
						xls.hideRow(row+2);
					}
					if(karibaraiZeigaku8PercentDecimal.compareTo(BigDecimal.ZERO) == 1) {
						if(isPrint) {
							xls.write(2, row+3, "軽減税率8%");
							xls.write(6, row+3, EteamXlsFmt.formatMoney(karibaraiZeigaku8PercentDecimal));
						} else {
							xls.write(2, row+2, "軽減税率8%");
							xls.write(6, row+2, EteamXlsFmt.formatMoney(karibaraiZeigaku8PercentDecimal));
						}
					} else {
						if(isPrint) {
							xls.hideRow(row+3);
						} else {
							xls.hideRow(row+2);
						}
					}
				} else {
					if(karibaraiZeigaku10PercentMenzei80Decimal.compareTo(BigDecimal.ZERO) == 1) {
						xls.write(2, row+2, "10%（仕入税額控除経過措置割合80%）");
						xls.write(6, row+2, EteamXlsFmt.formatMoney(karibaraiZeigaku10PercentMenzei80Decimal));
						isPrint = true;
					} else {
						xls.hideRow(row+2);
					}
					if(karibaraiZeigaku8PercentMenzei80Decimal.compareTo(BigDecimal.ZERO) == 1) {
						if(isPrint) {
							xls.write(2, row+3, "軽減税率8%（仕入れ税額控除経過措置割合80%）");
							xls.write(6, row+3, EteamXlsFmt.formatMoney(karibaraiZeigaku8PercentMenzei80Decimal));
						} else {
							xls.write(2, row+2, "軽減税率8%（仕入れ税額控除経過措置割合80%）");
							xls.write(6, row+2, EteamXlsFmt.formatMoney(karibaraiZeigaku8PercentMenzei80Decimal));
						}
					} else {
						if(isPrint) {
							xls.hideRow(row+3);
						} else {
							xls.hideRow(row+2);
						}
					}
				}

				row += 4; rowCount += 4;
			}
			
			//伝票単位区切り
			row++;	rowCount++;
			preUnit	= unit;
		}
		
		//頁(#/#)をまとめて書き込み
		for (int i = 0; i < pageRows.size(); i++) {
			xls.write(10, pageRows.get(i) + 1, "ページ　：　" + (i+1) + "/" + pageRows.size());
		}
		
		//テンプレート部分削除
		for (int i = 0; i <= 24; i++) xls.hideRow(i);
	}

	/**
	 * 依頼書シートヘッダー出力
	 * @param xls	EXCEL
	 * @param irai	データ
	 * @param row	行
	 * @param page	頁
	 */
	protected void writeIraishoHeader(EteamXls xls, GMap irai, int row, int page) {
		xls.copyRow(0, row, 9, 1, false);
		xls.write(10,row	 , "作成日　：　" 	+ DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
		xls.write(6	,row + 1 , "（ " 			+ DateFormatUtils.format(new Date(), "yyyy年MM月dd日") + "　分 ）");
		xls.write(1	,row + 2 , "部門　：　" 	+ torimBumonSeparator((String)irai.get("kihyou_bumon_name")));
		xls.write(4	,row + 2 , "承認者　：　" 	+ irai.get("shounin_user_name"));
	}
	
	/**
	 * 申請単位SELECT
	 * @param denpyouId	伝票ID
	 * @return				申請単位データ
	 */
	protected GMap findShiharaiIrai(String denpyouId) {

		String sql = 
			"SELECT "
		+	"  irai.* "
		+	"  , (CASE WHEN tr.torihikisaki_cd = ? THEN irai.ichigensaki_torihikisaki_name ELSE COALESCE(tr.torihikisaki_name_seishiki, irai.torihikisaki_name_ryakushiki) END) torihikisaki_name_seishiki"
		+	"  , cd1.name shiharai_houhou_name "
		+	"  , cd2.name shiharai_shubetsu_name "
		+	"  , cd3.name yokin_shubetsu_name "
		+	"  , cd4.name jigyousha_kbn_name "
		+	"  , d.serial_no "
		+	"  , sr1.bumon_full_name AS kihyou_bumon_name "
		+	"  , sr2.user_full_name || sr2.gyoumu_role_name AS shounin_user_name "
		+   "  , (sr1.bumon_cd || 'ー' || irai.shiharai_houhou || 'ー' || irai.shiharai_shubetsu || 'ー' || irai.yoteibi || 'ー' || COALESCE(TO_CHAR(shiharai_kijitsu,'yyyyMMdd'), '')) AS unit "

		//支払依頼
		+	"FROM shiharai_irai irai "
		+	"LEFT OUTER JOIN naibu_cd_setting cd1 ON "
		+	"  cd1.naibu_cd_name = 'shiharai_irai_houhou' AND "
		+	"  cd1.naibu_cd = irai.shiharai_houhou "
		+	"LEFT OUTER JOIN naibu_cd_setting cd2 ON "
		+	"  cd2.naibu_cd_name = 'shiharai_irai_shubetsu' AND "
		+	"  cd2.naibu_cd = irai.shiharai_shubetsu "
		+	"LEFT OUTER JOIN naibu_cd_setting cd3 ON "
		+	"  cd3.naibu_cd_name = 'yokin_shubetsu' AND "
		+	"  cd3.naibu_cd = irai.yokin_shubetsu "
		//事業者区分
		+   "LEFT OUTER JOIN naibu_cd_setting cd4 ON "
		+   "  cd4.naibu_cd_name = 'jigyousha_kbn' AND "
		+   "  cd4.naibu_cd = irai.jigyousha_kbn "
		//伝票
		+	"JOIN denpyou d ON "
		+	"  d.denpyou_id = irai.denpyou_id "
		//承認ルート（枝番1）
		+	"JOIN shounin_route sr1 ON "
		+	"  sr1.denpyou_id = d.denpyou_id AND "
		+	"  sr1.edano = 1 "
		//承認ルート（枝番2）
		+	"LEFT OUTER JOIN shounin_route sr2 ON "
		+	"  sr2.denpyou_id = d.denpyou_id AND "
		+	"  sr2.edano = 2 "
		//取引先マスター
		+	"LEFT OUTER JOIN torihikisaki_master tr ON "
		+	"  tr.torihikisaki_cd = irai.torihikisaki_cd "
		
		+	"WHERE "
		+	"  irai.denpyou_id = ? ";
		return connection.find(sql, setting.ichigenCd(), denpyouId);
	}
	
	/**
	 * 申請単位に明細データSELECT
	 * @param denpyouId	伝票ID
	 * @return			申請単位明細データ
	 */
	protected List<GMap> loadMeisai(String denpyouId) {

		String sql = 
			"SELECT "
		+	"  meisai.* "
		+	"  , cd1.name kari_kazei_kbn_name "
		+   "  , cd2.name jigyousha_kbn_name "

		//支払依頼
		+	"FROM shiharai_irai_meisai meisai "
		//課税区分
		+	"LEFT OUTER JOIN naibu_cd_setting cd1 ON "
		+	"  cd1.naibu_cd_name = 'kazei_kbn' AND "
		+	"  cd1.naibu_cd = meisai.kari_kazei_kbn "
		//事業者区分
		+   "LEFT OUTER JOIN shiharai_irai shinsei ON "
		+   "    shinsei.denpyou_id = meisai.denpyou_id "
		+ 	"LEFT OUTER JOIN naibu_cd_setting cd2 ON "
		+	"	cd2.naibu_cd_name = 'jigyousha_kbn' AND "
		+ 	"	cd2.naibu_cd = shinsei.jigyousha_kbn "
		+	"WHERE "
		+	"  meisai.denpyou_id = ? "
		+	"ORDER BY "
		+	"  meisai.denpyou_edano ";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * フル部門名から区切り文字（｜）を取り除く
	 * @param bumonName	部門名
	 * @return			取り除いたの
	 */
	protected String torimBumonSeparator(String bumonName) {
		if (isEmpty(bumonName)) return "";
		if (bumonName.indexOf('｜') == -1) return bumonName;
		return bumonName.substring(bumonName.lastIndexOf("｜") + 1);
	}
}
