package eteam.gyoumu.shiharai;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.TESUURYOU;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.gyoumu.kaikei.DaishoMasterCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.user.User;

/**
 * FBデータ作成（支払依頼）Logic
 */
public class ShiharaiIraiOutputLogic extends EteamAbstractLogic {
	/** ログ */
	EteamLogger log  = EteamLogger.getLogger(ShiharaiIraiOutputLogic.class);

	/**
	 * 支払依頼 支払日検索
	 * @param limitNum 件数
	 * @return 検索結果
	 */
	public List<GMap> loadShiharaiYoteiBi(int limitNum){
		final String sql =
				" SELECT DISTINCT s.yoteibi AS kojinseisan_shiharaibi "
			+ " FROM denpyou d "
			+ " JOIN shiharai_irai s ON "
			+ "   s.denpyou_id = d.denpyou_id "
			+ " WHERE "
			+ "   d.denpyou_joutai = ? AND "
			+ "   s.yoteibi >= current_date AND "
			+ "   s.shiharai_houhou = ? "
			+ " ORDER BY "
			+ "   s.yoteibi ASC "
			+ " LIMIT ? "
			+ " OFFSET 0 ";
		return connection.load(sql, DENPYOU_JYOUTAI.SYOUNIN_ZUMI, SHIHARAI_IRAI_HOUHOU.FURIKOMI, limitNum);
	}
	
	/**
	 * 債務支払用の支払依頼データ(支払)を取得する
	 * @param shiharaibi 支払予定日
	 * @param shiharaiShubetsu 支払種別
	 * @param type 1:取引先マスタ、2:債務管理
	 * @param all 債務支払データ作成済フラグが1のも含めるならtrue
	 * @return 支払依頼データ
	 */
	public List<GMap> loadShiharaiIrai4SaimuPlus(Date shiharaibi, String shiharaiShubetsu, int type, boolean all) {
		KaikeiCategoryLogic lg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		KaikeiCommonLogic common = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		String sql = "SELECT "
						+ "  s.*, "
						+ "  m.denpyou_edano, "
						+ "  m.shiwake_edano, "
						+ "  (m.shiharai_kingaku - (CASE WHEN m.denpyou_edano = (SELECT MIN(tmp.denpyou_edano) FROM shiharai_irai_meisai tmp WHERE tmp.denpyou_id = s.denpyou_id AND tmp.shiharai_kingaku > 0) THEN s.manekin_gensen ELSE 0 END)) shiharai_kingaku, "
						+ "  m.kari_futan_bumon_cd, "
						+ "  yoteibi yoteibi2, "
						+ "  tekiyou, "
						+ "  d.daihyou_futan_bumon_cd, "
						+ "  'p' as shori_kbn "
						+ "FROM shiharai_irai s "
						+ "JOIN shiharai_irai_meisai m ON "
						+ "  s.denpyou_id = m.denpyou_id "
						+ "JOIN denpyou d ON "
						+ "  s.denpyou_id = d.denpyou_id "
						+ "WHERE "
						+ "  d.denpyou_joutai = ? AND "
						+ "  s.yoteibi = ? AND"
						+ "  s.shiharai_houhou = ? AND "
						+ "  s.torihikisaki_cd <> ? AND "
						+ "  m.shiharai_kingaku > 0 "
						+ "  :SHUBETSU "
						+ "  :MADE "
						+ "ORDER BY "
						+ "  s.denpyou_id, m.denpyou_edano ";
		List<Object> params = new ArrayList<>();
		params.add(DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
		params.add(shiharaibi);
		params.add(SHIHARAI_IRAI_HOUHOU.FURIKOMI);
		params.add(setting.ichigenCd());
		if (! isEmpty(shiharaiShubetsu)) {
			sql = sql.replace(":SHUBETSU", "AND s.shiharai_shubetsu = ?");
			params.add(shiharaiShubetsu);
		} else {
			sql = sql.replace(":SHUBETSU", "");
		}
		if (all) {
			sql = sql.replace(":MADE", "");
		} else {
			if (type == 1) sql = sql.replace(":MADE", "AND s.torihikisaki_made_flg = '0' ");
			else sql = sql.replace(":MADE", "AND s.saimu_made_flg = '0' ");
		}
		List<GMap> list = connection.load(sql, params.toArray());
		
		//貸方の科目・部門を判定
		for(GMap record : list){
			String kashiNo = (record.get("shiharai_shubetsu").equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) ? "2" : "3";
			GMap swk = lg.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, (int)record.get("shiwake_edano"));
			String kashiKamokuCd = (String)swk.get("kashi_kamoku_cd" + kashiNo);
			String kashiFutanBumonCd = common.convFutanBumon(record.get("kari_futan_bumon_cd"), swk.get("kashi_futan_bumon_cd" + kashiNo), record.get("daihyou_futan_bumon_cd"));
			record.put("kashiKamokuCd"		, kashiKamokuCd);
			record.put("kashiFutanBumonCd"	, kashiFutanBumonCd);
			record.put("sortKey", "" + record.get("denpyou_id") + ":" + record.get("kashiKamokuCd") + ":" + record.get("kashiFutanBumonCd") + ":" + record.get("denpyou_edano"));
			record.put("sameKey", "" + record.get("denpyou_id") + ":" + record.get("kashiKamokuCd") + ":" + record.get("kashiFutanBumonCd"));
		}
		
		//伝票ID・貸方科目・貸方部門でソート
		Collections.sort(list, new Comparator<GMap>(){
			@Override
			public int compare(GMap rec1, GMap rec2) {
				String colName1 = (String)rec1.get("sortKey");
				String colName2 = (String)rec2.get("sortKey");
				return colName1.compareTo(colName2);
			}    
		});

		//同じ伝票内の先頭に集約する（金額合計して処理フラグ立てておく）
		BigDecimal denpyouSum = null;
		String sameKey = "";
		String sameKeyBefore = "";
		String sameKeyAfter = "";
		for (int i = list.size() - 1; i >= 0; i--) {
			GMap record = list.get(i);
			sameKey = (String)record.get("sameKey");
			sameKeyBefore = (i == 0) ? "" : (String)list.get(i - 1).get("sameKey");
			sameKeyAfter = (i == list.size() - 1) ? "" : (String)list.get(i + 1).get("sameKey");
			
			//同じ伝票ID内・同じ貸方での金額集約
			if (! sameKey.equals(sameKeyAfter)) denpyouSum = new BigDecimal(0);
			denpyouSum = denpyouSum.add((BigDecimal)record.get("shiharai_kingaku"));
			
			if (! sameKey.equals(sameKeyBefore)) {
				record.put("shiharai_kingaku", denpyouSum);
				record.put("shoriFlg", "1");
			}
		}
		
		return list;
	}
	
	/**
	 * 債務支払用の支払依頼データ(相殺)を取得する
	 * @param shiharaibi 支払予定日
	 * @param shiharaiShubetsu 支払種別
	 * @param all 債務支払データ作成済フラグが1のも含めるならtrue
	 * @return 支払依頼データ
	 */
	public List<GMap> loadShiharaiIrai4SaimuMinus(Date shiharaibi, String shiharaiShubetsu, boolean all) {
		KaikeiCategoryLogic lg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		KaikeiCommonLogic common = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		String sql = "SELECT "
						+ "  s.*, "
						+ "  m.denpyou_edano, "
						+ "  m.shiwake_edano, "
						+ "  m.shiharai_kingaku AS shiharai_kingaku, "
						+ "  m.kari_futan_bumon_cd, "
						+ "  yoteibi yoteibi2, "
						+ "  m.tekiyou, "
						+ "  d.daihyou_futan_bumon_cd, "
						+ "  'm' as shori_kbn "
						+ "FROM shiharai_irai s "
						+ "JOIN shiharai_irai_meisai m ON "
						+ "  s.denpyou_id = m.denpyou_id "
						+ "JOIN denpyou d ON "
						+ "  s.denpyou_id = d.denpyou_id "
						+ "WHERE "
						+ "  d.denpyou_joutai = ? AND "
						+ "  s.yoteibi = ? AND"
						+ "  s.shiharai_houhou = ? AND "
						+ "  s.torihikisaki_cd <> ? AND "
						+ "  m.shiharai_kingaku < 0 "
						+ "  :SHUBETSU "
						+ "  :SAIMU_MADE "
						+ "ORDER BY "
						+ "  s.denpyou_id, m.denpyou_edano ";
		List<Object> params = new ArrayList<>();
		params.add(DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
		params.add(shiharaibi);
		params.add(SHIHARAI_IRAI_HOUHOU.FURIKOMI);
		params.add(setting.ichigenCd());
		if (! isEmpty(shiharaiShubetsu)) {
			sql = sql.replace(":SHUBETSU", "AND shiharai_shubetsu = ?");
			params.add(shiharaiShubetsu);
		} else {
			sql = sql.replace(":SHUBETSU", "");
		}
		if (all) {
			sql = sql.replace(":SAIMU_MADE", "");
		} else {
			sql = sql.replace(":SAIMU_MADE", "AND saimu_made_flg = '0' ");
		}
		List<GMap> list = connection.load(sql, params.toArray());
		//貸方の科目・部門を判定
		for(GMap record : list){
			String kashiNo = (record.get("shiharai_shubetsu").equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) ? "2" : "3";
			GMap swk = lg.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, (int)record.get("shiwake_edano"));
			String kashiKamokuCd = (String)swk.get("kashi_kamoku_cd" + kashiNo);
			String kashiFutanBumonCd = common.convFutanBumon(record.get("kari_futan_bumon_cd"), swk.get("kashi_futan_bumon_cd" + kashiNo), record.get("daihyou_futan_bumon_cd"));
			record.put("kashiKamokuCd"		, kashiKamokuCd);
			record.put("kashiFutanBumonCd"	, kashiFutanBumonCd);
			record.put("sortKey", "" + record.get("denpyou_id") + ":" + record.get("kashiKamokuCd") + ":" + record.get("kashiFutanBumonCd") + ":" + record.get("denpyou_edano"));
			record.put("sameKey", "" + record.get("denpyou_id") + ":" + record.get("kashiKamokuCd") + ":" + record.get("kashiFutanBumonCd"));
		}
		
		

		for (GMap record : list) {
			record.put("shoriFlg", "1");
		}
		
		return list;
	}

	/**
	 * 債務支払データ作成済フラグを立てる
	 * @param denpyouId 伝票ID
	 */
	public void updateSaimuMade(String denpyouId) {
		final String sql = "UPDATE shiharai_irai SET saimu_made_flg = '1' WHERE denpyou_id = ?";
		connection.update(sql, denpyouId);
	}

	/**
	 * 債務支払CSVを作る
	 * @param list 支払依頼明細
	 * @param out 出力先
	 * @param yoteibi 支払予定日
	 * @return 途中でエラーがあればメッセージ、なければnull
	 */
	public String makeSaimuShiharai(List<GMap> list, Writer out, Date yoteibi) {
		KaikeiCategoryLogic lg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		DaishoMasterCategoryLogic mLg = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);
		
		String kaishaCd = setting.op21MparamKaishaCd();
		
		for (GMap record : list) {
			if (! record.containsKey("shoriFlg"))
			{
				continue;
			}
// String shoriKbn = (String)record.get("shori_kbn");
			String torihikisakiCd = (String)record.get("torihikisaki_cd");
			String ginkouCd = (String)record.get("furikomi_ginkou_cd");
			String shitenCd = (String)record.get("furikomi_ginkou_shiten_cd");
			String yokinShubetsu = (String)record.get("yokin_shubetsu");
			String kouzabangou = (String)record.get("kouza_bangou");
			String kouzaMeiginin = (String)record.get("kouza_meiginin");
			String tesuuryouFutanKbn = (String)record.get("tesuuryou");
			Furisaki furiN = new Furisaki(torihikisakiCd, ginkouCd, shitenCd, yokinShubetsu, kouzabangou, kouzaMeiginin, tesuuryouFutanKbn);
			String shiharaiShubetsu = (String)record.get("shiharai_shubetsu");
			
			//支払区分
			String shiharaiKbn = "";
			if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) {
				//定期なら取引先マスタ(支払ID=1)の支払区分
				GMap shiharaiHouhouMaster = mLg.findTorihikisakiShiharaiHouhou(torihikisakiCd);
				shiharaiKbn = (shiharaiHouhouMaster == null) ? "" : (String)shiharaiHouhouMaster.get("shiharai_kbn");

// //支払区分=約定Aの場合でも銀行振込として処理。
// //ただし同取引先・同支払予定日の差引支給金額合計が50万円以上の場合、該当取引先マスタの支払期日とする。※支払期日が未設定なら支払日。
// if (shiharaiKbn.equals(SHIHARAI_SHUBETSU.YAKUTEI_A)) {
// shiharaiKbn = SHIHARAI_SHUBETSU.GINKOU_FURIKOMI;
// }
			} else {
				//その他(一見除く)なら6(銀行振込)
				shiharaiKbn = SHIHARAI_SHUBETSU.GINKOU_FURIKOMI;
			}
			
			//貸方項目
			String kashiKamokuCd = record.get("kashiKamokuCd");
			String kashiFutanBumonCd = record.get("kashiFutanBumonCd");
			
			//摘要
			// SIAS→60バイトを超える文字はカット
			// De3→40バイトを超える文字はカット
			int tekiyouMaxByte = (Open21Env.getVersion() == Version.SIAS)? 60 : 40;
			String tekiyou = record.get("tekiyou");
			if(tekiyouMaxByte < EteamCommon.getByteLength(tekiyou)){
				tekiyou = EteamCommon.byteCut(tekiyou, tekiyouMaxByte);
			}
			
			//振込（その他）の場合はマスター登録済みの銀行IDを探す
			Integer ginkouId = null;
			if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)) {
				ginkouId = 1;
				GMap furi1 = mLg.findFurikomisaki(torihikisakiCd);
				if (furi1 != null) {
					Furisaki furi1Obj = new Furisaki(torihikisakiCd, (String)furi1.get("furikomi_ginkou_cd"), (String)furi1.get("furikomi_ginkou_shiten_cd"), (String)furi1.get("yokin_shubetsu"), (String)furi1.get("kouza_bangou"), (String)furi1.get("kouza_meiginin_furigana"), furi1.get("tesuuryou_futan_kbn").toString());
					if (! furiN.equals(furi1Obj)) {
						int furiNum = lg.findSonotaFuriNum(
								torihikisakiCd, yoteibi, 
								furi1Obj.ginkouCd, furi1Obj.shitenCd, furi1Obj.yokinShubetsu, furi1Obj.kouzabangou, furi1Obj.kouzaMeiginin, tesuuryouConvRev(furi1Obj.tesuuryouFutanKbn),
								ginkouCd, shitenCd, yokinShubetsu, kouzabangou, kouzaMeiginin, tesuuryouFutanKbn);
							if (furiNum == 1)
							{
								ginkouId = 2;
							}
							if (furiNum == 2)
							{
								ginkouId = 3;
							}
					}
				}
			}
			if(Open21Env.getVersion() == Version.SIAS){
				write (out, kaishaCd); //1.会社コード
				write (out, ""); //2.SEQ
				write (out, ""); //3.明細行№
				write (out, ""); //4.取引区分
				write (out, DateFormatUtils.format((Date)record.get("keijoubi"), "yyyyMMdd")); //5.伝票日付
				writeQt (out, ""); //6.証憑番号・インボイス
				writeQt (out, ""); //7.稟議書番号
				writeQt (out, record.get("edi")); //8.拡張コード１
				writeQt (out, ""); //9.拡張コード２
				writeQt (out, ""); //10.拡張コード３
				if(KaishaInfo.getKaishaInfo(ColumnName.TORIHIKISAKI_CD_TYPE).equals("1"))
				write (out, record.get("torihikisaki_cd")); //11.取引先コード
				else
				writeQt (out, record.get("torihikisaki_cd")); //11.取引先コード
				write (out, ""); //12.取引先補助コード
				writeQt (out, ""); //13.取引先名称
				writeQt (out, ""); //14.取引先フリガナ
				if(KaishaInfo.getKaishaInfo(ColumnName.KAMOKU_CD_TYPE).equals("1"))
				write (out, kashiKamokuCd); //15.科目コード
				else
				writeQt (out, kashiKamokuCd); //15.科目コード
				if(KaishaInfo.getKaishaInfo(ColumnName.FUTAN_BUMON_CD_TYPE).equals("1"))
				write (out, kashiFutanBumonCd); //16.部門コード
				else
				writeQt (out, kashiFutanBumonCd); //16.部門コード
				write (out, record.get("shiharai_kingaku").toString()); //17.金額
				write (out, ""); //18.ダミー
				write (out, shiharaiKbn); //19.支払区分
				write (out, ""); //20.直接支払区分
				write (out, ""); //21.源泉税計算区分
				write (out, ""); //22.控除区分
				write (out, ""); //23.減算元区分
				write (out, ""); //24.節印紙実行
				write (out, DateFormatUtils.format((Date)record.get("yoteibi2"), "yyyyMMdd")); //25.支払日
				write (out, ""); //26.支払期日
				writeQt (out, ""); //27.手形・小切手番号(前３桁)
				write (out, ""); //28.手形・小切手番号(後６桁)
				writeQt (out, ""); //29.管理番号(前３桁)
				write (out, ""); //30.管理番号(後６桁)
				writeQt (out, ""); //31.一括支払管理番号
				write (out, ""); //32.名寄区分
				write (out, ""); //33.ファクタリングID
				write (out, ""); //34.口座ID
				write (out, ""); //35.自社銀行コード
				write (out, ""); //36.自社支店コード
				write (out, ""); //37.自社預金種別
				write (out, ""); //38.自社口座番号
				write (out, ginkouId == null ? "" : ginkouId.toString()); //39.取引先銀行No
				write (out, ""); //40.取引先銀行コード
				write (out, ""); //41.取引先支店コード
				writeQt (out, tekiyou); //42.備考
				write (out, ""); //43.承認状況
				write (out, ""); //44.前払金データ区分
				write (out, ""); //45.計上時レート区分
				write (out, ""); //46.(外貨）幣種
				write (out, ""); //47.(外貨）レート
				write (out, ""); //48.(外貨）金額
				write (out, ""); //49.外貨）口座番号/IBANコード
				write (out, ""); //50.外貨）SWIFT（BIC）コード
				write (out, ""); //51.ユーザーNo
				write (out, ""); //52.最終更新日
				write (out, ""); //53.ダミー
				write (out, ""); //54.ダミー
				write (out, ""); //55.ダミー
				write (out, ""); //56.ダミー
				write (out, ""); //57.ダミー
				write (out, ""); //58.ダミー
				write (out, ""); //59.ダミー
				writeLast(out, ""); //60.ダミー
			}else{
				write (out, kaishaCd); //1.会社コード
				write (out, DateFormatUtils.format((Date)record.get("keijoubi"), "yyyyMMdd")); //2.伝票日付
				writeQt (out, ""); //3.証憑番号
				writeQt (out, ""); //4.稟議書番号
				writeQt (out, record.get("edi")); //5.拡張コード１
				writeQt (out, ""); //6.拡張コード２
				writeQt (out, ""); //7.拡張コード３
				if(KaishaInfo.getKaishaInfo(ColumnName.TORIHIKISAKI_CD_TYPE).equals("1"))
				write (out, record.get("torihikisaki_cd")); //8.取引先コード
				else
				writeQt (out, record.get("torihikisaki_cd")); //8.取引先コード
				write (out, ""); //9.取引先補助コード
				writeQt (out, ""); //10.取引先名称
				writeQt (out, ""); //11.取引先50音
				if(KaishaInfo.getKaishaInfo(ColumnName.KAMOKU_CD_TYPE).equals("1"))
				write (out, kashiKamokuCd); //12.科目コード
				else
				writeQt (out, kashiKamokuCd); //12.科目コード
				if(KaishaInfo.getKaishaInfo(ColumnName.FUTAN_BUMON_CD_TYPE).equals("1"))
				write (out, kashiFutanBumonCd); //13.部門コード
				else
				writeQt (out, kashiFutanBumonCd); //13.部門コード
				write (out, ""); //14.工事コード
				write (out, record.get("shiharai_kingaku").toString()); //15.金額
				write (out, ""); //16.税抜金額
				write (out, shiharaiKbn); //17.支払区分
				write (out, ""); //18.支払計上
				write (out, ""); //19.源泉計算区分
				write (out, ""); //20.節印紙実行
				write (out, DateFormatUtils.format((Date)record.get("yoteibi2"), "yyyyMMdd")); //21.支払日
				write (out, ""); //22.支払期日
				writeQt (out, ""); //23.手形・小切手番号前３桁
				write (out, ""); //24.手形・小切手番号後６桁
				writeQt (out, ""); //25.管理番号前３桁
				write (out, ""); //26.管理番号後６桁
				writeQt (out, ""); //27.一括支払管理番号
				writeQt (out, ""); //28.支払人・譲渡元コード
				write (out, ""); //29.支払人・譲渡元補助コード"
				writeQt (out, ""); //30.支払人・譲渡元名称
				write (out, ""); //31.名寄区分
				write (out, ""); //32.自社銀行コード
				write (out, ""); //33.自社銀行支店コード
				write (out, ""); //34.自社預金種別
				write (out, ""); //35.自社口座番号
				write (out, ""); //36.自社銀行契約者コード
				write (out, ginkouId == null ? "" : ginkouId.toString()); //37.振込先銀行No
				write (out, ""); //38.振込先銀行コード
				write (out, ""); //39.振込先銀行支店コード
				write (out, ""); //40.消込コード
				writeQt (out, tekiyou); //41.備考
				write (out, ""); //42.承認状況
				write (out, ""); //43.ユーザーNo
				write (out, ""); //44.最終更新日
				write (out, ""); //45.ダミー
				write (out, ""); //46.ダミー
				write (out, ""); //47.ダミー
				write (out, ""); //48.ダミー
				write (out, ""); //49.ダミー
				writeLast(out, ""); //50.ダミー
			}
		}
		return null;
	}

	/**
	 * 書き込み（ダブルコート囲い・カンマ付）
	 * @param out 出力先
	 * @param o 文字列
	 */
	protected void writeQt(Writer out, Object o) {
		if (o == null)
		{
			o = "";
		}
		try {
			out.write("\"" + o.toString() + "\",");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 書き込み（カンマ付）
	 * @param out 出力先
	 * @param o 文字列
	 */
	protected void write(Writer out, Object o) {
		if (o == null)
		{
			o = "";
		}
		try {
			out.write(o.toString() + ",");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 書き込み（改行付）
	 * @param out 出力先
	 * @param s 文字列
	 */
	protected void writeLast(Writer out, String s) {
		try {
			out.write(s + "\r\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * WF→OPEN21のコード体系変換（手数料負担区分）
	 * @param s 手数料負担区分(WF)
	 * @return 手数料負担区分(OPEN21)
	 */
	protected String tesuuryouConvRev(String s) {
		return s.equals("1") ?  TESUURYOU.JISHA_FUTAN: TESUURYOU.SENPOU_FUTAN;
	}
	
	/** 振込先 */
	class Furisaki {
		/** 取引先コード */ String torihikisakiCd;
		/** 金融機関コード */ String ginkouCd;
		/** 金融機関支店コード */ String shitenCd;
		/** 預金種別 */ String yokinShubetsu;
		/** 口座番号 */ String kouzabangou;
		/** 口座名義人(ｶﾅ) */ String kouzaMeiginin;
		/** 手数料負担区分 */ String tesuuryouFutanKbn;
		
		/**
		 * new
		 * @param torihikisakiCd 取引先コード
		 * @param ginkouCd 金融機関コード
		 * @param shitenCd 金融機関支店コード
		 * @param yokinShubetsu 預金種別
		 * @param kouzabangou 口座番号
		 * @param kouzaMeiginin 口座名義人(ｶﾅ)
		 * @param tesuuryouFutanKbn 手数料負担区分
		 */
		public Furisaki(String torihikisakiCd, String ginkouCd, String shitenCd, String yokinShubetsu, String kouzabangou, String kouzaMeiginin, String tesuuryouFutanKbn) {
			this.torihikisakiCd = torihikisakiCd;
			this.ginkouCd = ginkouCd;
			this.shitenCd = shitenCd;
			this.yokinShubetsu = yokinShubetsu;
			this.kouzabangou = kouzabangou;
			this.kouzaMeiginin = kouzaMeiginin;
			this.tesuuryouFutanKbn = tesuuryouFutanKbn;
		}

		/**
		 * 同じ振込先か判定
		 * @param o 口座
		 * @return 同じ振込先か
		 */
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Furisaki)) throw new RuntimeException("Furisakiでない");
			Furisaki ot = (Furisaki)o;
			return
					this.torihikisakiCd .equals(ot.torihikisakiCd)
				&&	this.ginkouCd .equals(ot.ginkouCd)
				&&	this.shitenCd .equals(ot.shitenCd)
				&&	this.yokinShubetsu .equals(ot.yokinShubetsu)
				&&	this.kouzabangou .equals(ot.kouzabangou)
				&&	this.kouzaMeiginin .equals(ot.kouzaMeiginin)
				&&  this.tesuuryouFutanKbn .equals(ot.tesuuryouFutanKbn);
		}
	}

	/**
	 * 債務CSV連携EXE実行
	 * @param user ユーザ
	 * @return EXE実行結果
	 */
	public int callSaimuExeSIAS(User user) {
		StringBuilder command = new StringBuilder("");
		command.append("c:\\eteam\\bat\\bin\\sias\\ShDataImporter.exe").append(" ");
		command.append(setting.op21MparamKaishaCd()).append(" "); //1.会社コード
		command.append(setting.op21MparamKidouUser()).append(" "); //2.ユーザーコード
		command.append(EteamCommon.getContextSchemaName()); //3.スキーマ名
		log.debug("★★★実行コマンド" + command.toString());

		// コマンドを実行しインポートEXEを起動します。
		try{
			Process process = Runtime.getRuntime().exec(command.toString());
			return process.waitFor();
		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("プロセスが中断されました。", e);
		}
	}

	/**
	 * 債務CSV連携EXE実行
	 * @param user ユーザ
	 * @return EXE実行結果
	 */
	public int callSaimuExeDE3(User user) {
		StringBuilder command;
		command = new StringBuilder("c:\\eteam\\bat\\bin\\de3\\ShDataImporter.exe").append(" ");
		command.append(setting.op21MparamKaishaCd()).append(" "); //1.会社コード
		command.append(setting.op21MparamKidouUser()).append(" "); //2.ユーザーコード
		command.append(EteamCommon.getContextSchemaName()); //3.スキーマ名
		log.debug("★★★実行コマンド" + command.toString());

		// コマンドを実行しインポートEXEを起動します。
		try{
			Process process = Runtime.getRuntime().exec(command.toString());
			return process.waitFor();
		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("プロセスが中断されました。", e);
		}
	}
	
	/**
	 * 債務EXEの処理結果コードからメッセージを生成
	 * @param cd 処理結果コード
	 * @return メッセージ
	 */
	public String saimuExeCd2Msg(int cd){
		String msg;
		switch(cd){
			case 0:		msg = "正常に連携されました。"; break;
			case 1:		msg = "データがありません。"; break;
			case 2:		msg = "連携されました。※一部エラーあり"; break;
			case 3:		msg = "連携に失敗しました。"; break;
			default:	msg = "連携結果が不明です。"; break;
		}
		return msg + "(結果コード" + cd + ")";
	}
}
