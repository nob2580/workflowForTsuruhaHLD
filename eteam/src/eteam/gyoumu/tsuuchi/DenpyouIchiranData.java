package eteam.gyoumu.tsuuchi;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧テーブル設定用クラス
 *
 */
@Getter @Setter @ToString
public class DenpyouIchiranData {
	
	/** 重複なし、nullなし、ブランクなしのリスト 
	 * @param <String> 　*/ 
	@SuppressWarnings("hiding")
	class MyHashSet<String> extends HashSet<String> {
		@Override
		public boolean add(String s) {
			if(s == null || s.equals("")) {
				return false;
			}
			return super.add(s);
		}
	}

	/** WF共通部分 */
	DenpyouIchiranDataWFCom com = new DenpyouIchiranDataWFCom();

	/** 実施起案番号 */
	String lstJissiKianBangou = "";
	/** 支出起案番号 */
	String lstShishutsuKianBangou = "";
	/** 予算執行対象 */
	String lstYosanShikkouTaishou = "";
	/** 予算執行対象月 */
	String lstYosanCheckNengetsu = "";
	/** 簡易届バージョン */
	int lstVersion = 0;
	/** 金額 */
	BigDecimal lstKingaku = null;
	/** 外貨 */
	String lstGaika = "";
	/** 法人カード金額 */
	BigDecimal lstHoujinKingaku = null;
	/** 会社手配金額 */
	BigDecimal lstTehaiKingaku = null;
	/** 取引先1 */
	String lstTorihikisaki1 = "";
	/** 支払日 */
	Date lstShiharaibi = null;
	/** 支払希望日 */
	Date lstShiharaikiboubi = null;
	/** 支払方法 */
	String lstShiharaihouhou = "";
	/** 差引支給金額 */
	BigDecimal lstSashihikiShikyuuKingaku = null;
	/** 計上日 */
	Date lstKeijoubi = null;
	/** 仕訳計上日 */
	Date lstShiwakeKeijoubi = null;
	/** 精算予定日 */
	Date lstSeisanYoteibi = null;
	/** 仮払伝票ID */
	String lstKaribaraiDenpyouId = "";
	/** 訪問先 */
	String lstHoumonsaki = "";
	/** 目的 */
	String lstMokuteki = "";
	/** 簡易届件名 */
	String lstKenmei = "";
	/** 簡易届内容 */
	String lstNaiyou = "";
	/** ユーザー姓 */
	String lstUserSei = "";
	/** ユーザー名 */
	String lstUserMei = "";
	/** 精算期間From */
	Date lstSeisankikanFrom = null;
	/** 精算期間To */
	Date lstSeisankikanTo = null;

	/** 起案番号運用フラグ */
	String lstKianBangouUnyouFlg = "";
	/** 予算執行対象コード */
	String lstYosanShikkouTaishouCd = "";
	/** 起案終了フラグ */
	String lstKianSyuryoFlg = "";
	
	/** 負担部門コード */
	Set<String> lstFutanbumonCd = new MyHashSet<>();
	/** 借方負担部門コード */
	Set<String> lstKariFutanbumonCd = new MyHashSet<>();
	/** 借方科目コード */
	Set<String> lstKariKamokuCd = new MyHashSet<>();
	/** 借方科目枝番コード */
	Set<String> lstKariKamokuEdabanCd = new MyHashSet<>();
	/** 借方取引先コード */
	Set<String> lstKariTorihikisakiCd = new MyHashSet<>();
	/** 貸方負担部門コード */
	Set<String> lstKashiFutanbumonCd = new MyHashSet<>();
	/** 貸方科目コード */
	Set<String> lstKashiKamokuCd = new MyHashSet<>();
	/** 貸方科目枝番コード */
	Set<String> lstKashiKamokuEdabanCd = new MyHashSet<>();
	/** 貸方取引先コード */
	Set<String> lstKashiTorihikisakiCd = new MyHashSet<>();
	/** 明細金額 */
	Set<BigDecimal> lstMeisaiKingaku = new MyHashSet<>();
	/** 摘要 */
	String lstTekiyou = ""; 
	/** 法人カード使用フラグ */
	String lstHoujinCardUse = "";
	/** 会社手配使用フラグ */
	String lstKaishaTehaiUse = "";
	/** 領収書フラグ */
	String lstRyoushuushoExist = "";
	/** 未精算仮払伝票フラグ */
	String lstMiseisanKaribaraiExist = "";
	/** 未精算伺い伝票フラグ */
	String lstMiseisanUkagaiExist = "";
	
	/** 実施起案年度 */
	String lstJisshiNendo = "";
	/** 支出起案年度 */
	String lstShishutsuNendo = "";
	
	/** 起案番号採番フラグ */
	String lstKianBangouInput = "";
	
	/** 事業者区分 */
	Set<String> lstJigyoushakbn = new MyHashSet<>();
	/** 税抜金額 */
	BigDecimal lstZeinukiKingaku = new BigDecimal(0);
	/** 消費税額 */
	BigDecimal lstShouhizeiKingaku = new BigDecimal(0);
	/** インボイス対応伝票 */
	String lstInvoiceDenpyou = "";
	/** 支払先名 */
	Set<String> lstShiharaiName = new MyHashSet<>();
	/** 税抜明細金額 */
	Set<BigDecimal> lstZeinukiMeisaiKingaku = new MyHashSet<>();
}
