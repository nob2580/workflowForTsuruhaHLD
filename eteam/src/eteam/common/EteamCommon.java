package eteam.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.LocalizedMessage;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.seasar.framework.beans.util.BeanUtil;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.base.exception.EteamUploadFileSizeOverException;
import eteam.base.intercepter.EteamActionIntercepter;
import eteam.common.EteamConst.BrowserCode;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.RegAccess.REG_KEY_NAME;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.ShiwakeDataImportLogic;

/**
 * eTeam共通クラス
 */
public class EteamCommon {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamActionIntercepter.class);

	/**
	 * 期限のチェックを行い、エラーのリストを返却します。
	 * @param dateFrom String 期限開始日(yyyy/mm/dd)
	 * @param dateTo   String 期限終了日(yyyy/mm/dd)
	 * @return エラーの場合、リストを返却します。<br>
	 *         Key：errorCode エラーコードを返却します。<br>
	 *             <li>0:開始日がシステム日付より過去日の場合</li><br>
	 *             <li>1:終了日がシステム日付より過去日の場合</li><br>
	 *             <li>2:終了日が開始日より過去日の場合</li><br>
	 *         Key：errorMassage エラーメッセージを返却します。<br>
	 */
	public static List<Map<String, String>> kigenCheck(String dateFrom, String dateTo) {
		// システム日付を取得しStringへ変換します。
		String systemDate = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
		
		List<Map<String, String>> respons = new ArrayList<Map<String, String>>();
		if(dateFrom.compareTo(systemDate) < 0) {
			//すべての呼び出し元でエラーから除外しているが、チェック自体は残しておく。
			respons.add(errorJouhou("0", "開始日は本日、もしくは未来日を設定して下さい。"));
		}
		if(dateTo.compareTo(systemDate) < 0) {
			respons.add(errorJouhou("1", "終了日は本日、もしくは未来日を設定して下さい。"));
		}
		if(dateTo.compareTo(dateFrom) < 0) {
			respons.add(errorJouhou("2", "終了日は開始日と同じ日か、未来日を設定して下さい。"));
		}
		return respons;
	}
	
	/**
	 * 時刻を伴った期限のチェックを行い、エラーのリストを返却します。
	 * @param dateFrom     String 期限開始日(yyyy/mm/dd)
	 * @param dateFromHour String 期限開始時(hh)
	 * @param dateFromMin  String 期限開始分(mm)
	 * @param dateTo       String 期限終了日(yyyy/mm/dd)
	 * @param dateToHour   String 期限終了時(hh)
	 * @param dateToMin    String 期限終了分(mm)
	 * @return エラーの場合、リストを返却します。<br>
	 *         Key：errorCode エラーコードを返却します。<br>
	 *             <li>0:開始日がシステム日付より過去日の場合</li><br>
	 *             <li>1:終了日がシステム日付より過去日の場合</li><br>
	 *             <li>2:終了日が開始日より過去日の場合</li><br>
	 *             <li>3:終了時刻が開始時刻より過去時刻の場合</li><br>
	 *         Key：errorMassage エラーメッセージを返却します。<br>
	 */
	public static List<Map<String, String>> kigenCheckWithJikoku(String dateFrom, String dateFromHour, String dateFromMin, String dateTo, String dateToHour, String dateToMin) {
		// システム日付を取得しStringへ変換します。
		String systemDate = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
		
		List<Map<String, String>> respons = new ArrayList<Map<String, String>>();
		
		if ((dateFromHour == null || dateFromHour.equals("")) && !(dateFromMin == null || dateFromMin.equals(""))) {
			respons.add(errorJouhou("4", "開始時刻が分のみ入力されています。"));
		}
		if ((dateToHour == null || dateToHour.equals("")) && !(dateToMin == null || dateToMin.equals(""))) {
			respons.add(errorJouhou("4", "終了時刻が分のみ入力されています。"));
		}
		
		if (dateFromHour == null || dateFromHour.equals("")) {
			dateFromHour = "00";
		}
		if (dateFromMin == null || dateFromMin.equals("")) {
			dateFromMin = "00";
		}
		if (dateToHour == null || dateToHour.equals("")) {
			dateToHour = "00";
		}
		if (dateToMin == null || dateToMin.equals("")) {
			dateToMin = "00";
		}
		
		if(dateFrom.compareTo(systemDate) < 0) {
			respons.add(errorJouhou("0", "開始日は本日、もしくは未来日を設定して下さい。"));
		}
		if(dateTo.compareTo(systemDate) < 0) {
			respons.add(errorJouhou("1", "終了日は本日、もしくは未来日を設定して下さい。"));
		}
		if(dateTo.compareTo(dateFrom) < 0) {
			respons.add(errorJouhou("2", "終了日は開始日と同じ日か、未来日を設定して下さい。"));
		}
		if(dateTo.compareTo(dateFrom) == 0 && (dateToHour + dateToMin).compareTo(dateFromHour + dateFromMin) < 0) {
			respons.add(errorJouhou("3", "終了時刻は開始時刻と同じ時刻か、未来時刻を設定してください。"));
		}
		return respons;
	}
	
	/**
	 * 指定した2つの期限で重複期間があるかチェックします。
	 * ただしチェックの際、両データのFromとToが逆になっていないことは保証されている必要があります。
	 * @param kigenFrom1 期限From1
	 * @param kigenTo1   期限To1
	 * @param kigenFrom2 期限From2
	 * @param kigenTo2   期限To2
	 * @return 重複あればtrue
	 */
	public static boolean isKigenDuplicate(String kigenFrom1, String kigenTo1, String kigenFrom2, String kigenTo2 ){
		if ( !(toDate(kigenTo1).compareTo(toDate(kigenFrom2)) < 0
			|| toDate(kigenTo2).compareTo(toDate(kigenFrom1)) < 0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * エラーコードとエラーメッセージをMAPにし返却します。
	 * @param errCode エラーコード
	 * @param errMsg  エラーメッセージ
	 * @return ErrorMap
	 */
	protected static final Map<String, String> errorJouhou(String errCode, String errMsg) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("errorCode", errCode);
		map.put("errorMassage", errMsg);
		return map;
	}
	
	/**
	 * 背景色変更
	 * 期限に対応した背景色を返却します。
	 * @param dateFrom String  期限開始日(yyyy/MM/dd)
	 * @param dateTo   String  期限終了日(yyyy/MM/dd)
	 * @return 背景色
	 */
	public static String bkColorSettei(String dateFrom , String dateTo){
		
		String color = "";
		
		// システム日付の取得
		Date sysdate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String sysday = sdf.format(sysdate);
		// 期限開始日の比較
		int fromcmpday = sysday.compareTo(dateFrom);
		// 期限終了日の比較
		int tocmpday = (dateTo).compareTo(sysday);
		if(fromcmpday < 0){
			color = "wait-period-bgcolor"; //期間が未来の場合
		}
		else if(tocmpday < 0){
			color = "disabled-bgcolor"; //期間が無効の場合
		}
		else{
			color = "expiration-date-bgcolor"; //有効期限内
		}
		
		return color;
	}
	
	/**
	 * チェックテーブル定数
	 */
	public class CheckTable {
		/** ユーザー情報(KEY:ユーザーID) */
		public static final int USER_INFO_KEY_ID              = 10;
		/** ユーザー情報(KEY:社員番号) */
		public static final int USER_INFO_KEY_SHAIN_NO        = 11;
		/** ユーザー情報(KEY:メールアドレス) */
		public static final int USER_INFO_KEY_MAIL_ADDRESS    = 12;
		/** 所属部門（KEY:CD 全件） */
		public static final int SHOZOKU_BUMON_KEY_CD_ALL_DATE = 20;
		/** 所属部門（KEY:CD 有効期限内） */
		public static final int SHOZOKU_BUMON_KEY_CD_KIGENNAI = 21;
		/** 部門ロール（KEY:ID） */
		public static final int BUMON_ROLE_KEY_ID             = 30;
		/** 業務ロール（KEY:ID） */
		public static final int GYOUMU_ROLE_KEY_ID            = 40;
	}

	/**
	 * 検索キーを元にテーブルを検索し、データの存在チェックを行います。
	 * @param connection DBコネクション
	 * @param kensakuKey 検索キー
	 * @param checkTable チェックテーブル区分
	 * @return 存在すれば「true」存在しなければ「false」
	 */
	public static boolean dataSonzaiCheck(EteamConnection connection, String kensakuKey, int checkTable){
		BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		
		switch (checkTable) {
		case CheckTable.USER_INFO_KEY_ID:
			return bukc.selectUserInfo(kensakuKey) != null ? true : false;
		case CheckTable.USER_INFO_KEY_SHAIN_NO:
			return bukc.selectShainNo(kensakuKey) != null ? true : false;
		case CheckTable.USER_INFO_KEY_MAIL_ADDRESS:
			return bukc.selectMailAddress(kensakuKey) != null ? true : false;
		case CheckTable.SHOZOKU_BUMON_KEY_CD_ALL_DATE:
			return !(bukc.selectShozokuBumonAll(kensakuKey)).isEmpty() ? true : false;
		case CheckTable.SHOZOKU_BUMON_KEY_CD_KIGENNAI:
			return bukc.selectValidShozokuBumon(kensakuKey) != null ? true : false;
		case CheckTable.BUMON_ROLE_KEY_ID:
			return bukc.selectBumonRole(kensakuKey) != null ? true : false;
		case CheckTable.GYOUMU_ROLE_KEY_ID:
			return bukc.selectGyoumuRole(kensakuKey) != null ? true : false;
		default:
			return false;
		}
	}
	
	
	
	/**
	 * 対象部門から第一階層まで部門名を連結し返却します。
	 * @param connection DBコネクション
	 * @param bumonCd 対象部門
	 * @return 連結した部門名（親部門｜子部門｜孫部門｜…）
	 */
	public static String connectBumonName(EteamConnection connection, String bumonCd){
		return connectBumonName(connection,bumonCd,new Date(System.currentTimeMillis()));
	}
	
	/**
	 * 対象部門から第一階層まで部門名を連結し返却します。
	 * @param connection DBコネクション
	 * @param bumonCd 対象部門
	 * @param kijunBi 基準日
	 * @return 連結した部門名（親部門｜子部門｜孫部門｜…）
	 */
	public static String connectBumonName(EteamConnection connection, String bumonCd, Date kijunBi){
		BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		
		StringBuilder str = new StringBuilder();
		for(GMap bumonMap : bukc.selectParentBumon(bumonCd,kijunBi)) {
			if(bumonCd.matches("^[0]+$")) { // 部門コードが全社の場合
				return bumonMap.get("bumon_name").toString();
			}

			if(!bumonMap.get("oya_bumon_cd").toString().isEmpty()) {
				if(str.length() != 0) { str.append("｜"); }
				str.append(bumonMap.get("bumon_name").toString());
			}
		}
		return str.toString();
	}
	
	/**
	 * 指定月～当月の月リストを返す（全部１日）
	 * @param fromMonth 指定月
	 * @return 月リスト
	 */
	public static List<java.util.Date> createMonthList(java.util.Date fromMonth) {
		Calendar month = GregorianCalendar.getInstance();
		month.setTime(fromMonth);
		month.set(Calendar.DATE, 1);
		Calendar current = GregorianCalendar.getInstance();
		current.setTime(new Date(System.currentTimeMillis()));
		
		List<java.util.Date> ret = new ArrayList<>();
		while (month.before(current)) {
			ret.add(month.getTime());
			month.add(Calendar.MONTH, 1);
		}
		return ret;
	}
	
	/**
	 * ２年度前4月を取得（経費明細参照可能　開始日）
	 * @return ２年度前4月
	 */
	public static java.util.Date createMonthFirst() {
		//２年度前の４月１日を取る
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, -3);
		cal.add(Calendar.YEAR, -2);
		cal.set(Calendar.MONTH, 3);
		return cal.getTime();
	}
	
	/**
	 * ２年度前4月からの月のリストを取得します。
	 * @return 月のリストMapはkey = 月初の日付(yyyy/MM/dd)、valがyyyy年MM月
	 */
	public static List<Map<String, String>> createMonthList() {
		Calendar now = GregorianCalendar.getInstance();
		// 前後比較を安全に行うために中日にしておく
		now.set(Calendar.DATE, 10);
		SimpleDateFormat formatKey = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatVal = new SimpleDateFormat("yyyy年MM月");

		//２年度前の４月１日を取る
		Calendar cal = Calendar.getInstance();
		cal.setTime(createMonthFirst());

		List<Map<String, String>> ret = new ArrayList<Map<String,String>>(24);

		//２年度前の４月から当月までのリストを作る
		for (; cal.before(now); cal.add(Calendar.MONTH, 1)) {
			Map<String, String> month = new HashMap<String, String>(2);
			month.put("key", formatKey.format(cal.getTime()));
			month.put("val", formatVal.format(cal.getTime()));
			ret.add(month);
		}
		Collections.reverse(ret);

		return ret;
	}

	/**
	 * 月初・月末のDateを取得します。
	 * @param targetDate 対象日
	 * @return [0]が月初、[1]が月末
	 */
	public static Date[] getMonthDate(java.util.Date targetDate) {
		Date[] ret = new Date[2];
		Calendar calB = GregorianCalendar.getInstance();
		Calendar calF = GregorianCalendar.getInstance();
		calB.setTime(targetDate);
		calF.clear();
		calF.set(calB.get(Calendar.YEAR), calB.get(Calendar.MONTH), 1);
		Calendar calT = (Calendar)calF.clone();
		calT.add(Calendar.MONTH, 1);
		calT.add(Calendar.DATE, -1);
		ret[0] = new Date(calF.getTime().getTime());
		ret[1] = new Date(calT.getTime().getTime());

		return ret;
	}
	
	/**
	 * requestから値を取得してリンク文字列を作成します。
	 * @param action Action
	 * @param prefix URLプレフィックス
	 * @param nameSet パラメータに記載する名前のセット。
	 * @return URL
	 */
	public static StringBuffer getParmeterString(Object action, String prefix, Set<String> nameSet) {
		GMap prop = new GMap();
		BeanUtil.copyProperties(action, prop);

		StringBuffer buf = new StringBuffer(prefix);
		buf.append("?");
		for (String name : nameSet) {
			buf.append(name);
			buf.append("=");
			String tmp = "";
			try {
				tmp = URLEncoder.encode(prop.get(name) == null ? "" : prop.get(name).toString(),"UTF-8");
			}
			catch (UnsupportedEncodingException e) {}
			buf.append(tmp);
			buf.append("&");
		}
		return buf;
	}
	
	/**
	 * 業務ロールIDからシステム管理か制御区分の権限レベルの有無を判定します。
	 * @param connection DBコネクション
	 * @param gyoumuRoleId 業務ロールID
	 * @param seigyoKbn    制御区分
	 * @return 権限レベル「SU：システム管理」「WF,CO,KR：制御区分による」「空：権限なし」
	 */
	public static String getAccessAuthorityLevel(EteamConnection connection, String gyoumuRoleId, String seigyoKbn){
		BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		
		String accessAuthority;
		if(gyoumuRoleId == null){
			accessAuthority = ""; // 一般ユーザー
		} else {
			if(gyoumuRoleId.toString().equals("00000")) {
				// 業務ロールIDが「00000」の場合、システム管理として扱う。
				accessAuthority = "SU";
			} else {
				GMap map = bukc.selectGyoumuRoleKinouSeigyo(gyoumuRoleId, seigyoKbn);
				if("1".equals(map.get("gyoumu_role_kinou_seigyo_kbn").toString())) {
					// 業務ロール機能制御区分が「1:有効」の場合、権限あり
					accessAuthority = seigyoKbn;
				} else {
					accessAuthority = ""; // 権限なし
				}
			}
		}
		return accessAuthority;
	}
	
	/**
	 * メールアドレスの入力チェックします。
	 * @param mailAddress メールアドレス
	 * @return 正常の場合NULL、エラーの場合エラーメッセージリスト
	 */
	public static List<String> mailAddressCheck(String mailAddress){
		List<String> errMsgList = new ArrayList<String>();
		
		if (mailAddress != null && !mailAddress.isEmpty()) {
			String[] split = mailAddress.split("@");
			if(mailAddress.matches("^@.+|.+@$") || split.length != 2) {
				errMsgList.add("メールアドレスが不正です。「@(アットマーク)」を正しく使用して下さい。");
			} else if(mailAddress.matches(".*[ \\u00A0　\\u3000].*")) {
				errMsgList.add("メールアドレスにスペースが混在しています。");
			} else {
				String local  = split[0];
				String domain = split[1];

				if(local.matches("^\\..+|.+\\.$")) {
					errMsgList.add("メールアドレスのローカル部の先頭・末尾文字に「.(ドット)」は使用できません。");
				}
				if(domain.matches("^\\..+|.+\\.$")) {
					errMsgList.add("メールアドレスのドメイン部の先頭・末尾文字に「.(ドット)」は使用できません。");
				}
				if(local.matches(".+(\\.)\\1+.+") || domain.matches(".+(\\.)\\1+.")) {
					errMsgList.add("メールアドレスは「.(ドット)」は連続して使用できません。");
				}
				if(domain.matches(".*[^a-zA-Z0-9-\\.].*")) {
					errMsgList.add("メールアドレスのドメイン部は「a-z」「A-Z」「0-9」「-(ハイフン)」「.(ドット)」以外は使用できません。");
				}
			}
		}
		return errMsgList;
	}
	
	/**
	 * パスワードの文字種チェックを行います。
	 * @param password パスワード
	 * @return 正常の場合はNULL、エラーの場合はメッセージを返却します。
	 */
	public static List<String> passwordCheck(String password){
		int passKindMin = Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.PASS_KIND_MIN));
		List<String> errMsgList = new ArrayList<String>();

		if (password != null && !password.isEmpty()) {
			if(password.matches(".*[ \\u00A0　\\u3000].*")) {
				errMsgList.add("パスワードにスペースが混在しています。");
			}
	
			int charTypeCount = 0;
			boolean smallAlpha = false, largeAlpha = false, number = false, symbol = false;
			
			for (int i = 0; i < password.length(); i++) {
				String str = password.substring(i, i+1);
				// 英字(小文字)の使用チェック
				if (!smallAlpha && str.matches("[a-z]")) {charTypeCount++; smallAlpha = true; }
				// 英字(大文字)の使用チェック
				if (!largeAlpha && str.matches("[A-Z]")) {charTypeCount++; largeAlpha = true;}
				// 数字の使用チェック
				if (!number     && str.matches("[0-9]")) {charTypeCount++; number = true;}
				// 記号の使用チェック
				if (!symbol     && str.matches("[\\.\\+\\-\\~@_]")) {charTypeCount++; symbol = true;}
				
				if (charTypeCount >= passKindMin) {return errMsgList;}
			}
			
			// 使用している文字種が２種類以下の場合エラー
			errMsgList.add("パスワードは「英字(小文字)、英字(大文字)、数字、記号(.+-~@_)」の4種類のうち、" + passKindMin + "種類以上を使用して下さい。");
		}
		return errMsgList;
	}

	/**
	 * 全ページ数を計算します。(一覧部ページング)
	 * @param   maxPerPage  1ページあたりの最大表示件数
	 * @param   totalCount  データの総件数
	 * @return  全ページ数
	 */
	public static long calcTotalPageNum(int maxPerPage, long totalCount){
		long totalPageNum = 0;
		if(maxPerPage != 0){
			totalPageNum = (totalCount + maxPerPage -1)/ maxPerPage;
		}
		return totalPageNum;
	}
	
	/**
	 * 一覧部取得件数制御(「limit ～ offset 文」を生成します)
	 * @param   pageNo      表示させるページ番号
	 * @param   maxPerPage  1ページあたりの最大表示件数
	 * @return  SQL文字列
	 */
	public static StringBuffer makeSqlForTableNumCtl(int pageNo, int maxPerPage){
		StringBuffer sql = new StringBuffer();
		if(pageNo != 0) {
			//1ページ最大件数設定
			sql.append(" limit "+ maxPerPage);
		
			// 読み飛ばす件数
			if(pageNo != 0 && maxPerPage != 0){
				int offsetnum = maxPerPage*(pageNo -1);
				sql.append(" offset "+ offsetnum);
			}
		}
		return sql;
	}
	
	/**
	 * 'a','b',,,形式にする
	 * @param   list リスト
	 * @return  'a','b',,,
	 */
	public static String makeIn(List<String> list) {
		StringBuffer ret = new StringBuffer();
		for(int i = 0; i < list.size(); i++) {
			if(i != 0) ret.append(",");
			ret.append("'").append(list.get(i)).append("'");
		}
		return ret.toString();
	}
	
	/**
	 * 使用ブラウザの判定を行います。
	 * @param request {@link HttpServletRequest}
	 * @return ブラウザを表す文字列
	 */
	public static int getBrowserCode(HttpServletRequest request) {
		String ua = request.getHeader("user-agent");
		log.debug("UserAgent：" + ua);
		
		if(ua.indexOf("Edge") > 0) { // 必ずChromeより前にチェックする。
			log.debug("ブラウザ判定：Edge");
			return BrowserCode.EDGE;
		} else if(ua.startsWith("Opera") || ua.indexOf("Opera") > 0) { // スラッシュはなしで良い。
			log.debug("ブラウザ判定：OPERA");
			return BrowserCode.OPERA;
		} else if(ua.indexOf("Netscape") > 0) { // スラッシュはなしで良い。
			log.debug("ブラウザ判定：NETSCAPE");
			return BrowserCode.NETSCAPE;
		} else if(ua.indexOf("Firefox/") > 0) {
			log.debug("ブラウザ判定：FIREFOX");
			return BrowserCode.FIREFOX;
		} else if(ua.indexOf("Chrome/") > 0) {
			log.debug("ブラウザ判定：CHROME");
			return BrowserCode.CHROME;
		} else if(ua.indexOf("Safari/") > 0) { // 必ずChromeの後にチェックする。
			log.debug("ブラウザ判定：SAFARI");
			return BrowserCode.SAFARI;
		} else if(ua.indexOf("MSIE") > 0 || ua.indexOf("Trident") > 0) {
			log.debug("ブラウザ判定：IE");
			return BrowserCode.IE;
		} else {
			log.debug("ブラウザ判定：UNKNOWN");
			return BrowserCode.UNKNOWN;
		}
	}
	
	
	/**
	 * 部門リストを受け取り、階層構造へ編集し、リストを返却します。(現在日付のデータ使用)
	 * @param connection DBコネクション
	 * @param list 部門リスト(BumonUserKanriCategoryLogic#selectBumonTreeStructureの結果、level、bumon_cd順でソートされている)
	 * @return 編集後の部門リスト
	 */
	public static List<GMap> bumonListHenshuu(EteamConnection connection, List<GMap> list){
		return bumonListHenshuu(connection,list,new Date(System.currentTimeMillis()));
	}
	
	/**
	 * 部門リストを受け取り、階層構造へ編集し、リストを返却します。(指定基準日のデータ使用)
	 * @param connection DBコネクション
	 * @param list 部門リスト(BumonUserKanriCategoryLogic#selectBumonTreeStructureの結果、level、bumon_cd順でソートされている)
	 * @param kijunDate 基準日
	 * @return 編集後の部門リスト
	 */
	public static List<GMap> bumonListHenshuu(EteamConnection connection, List<GMap> list, Date kijunDate) {
		
		//一律、表示用に付加情報付ける
		for(GMap map : list){
			/* 接頭辞の設定（インテント） */
			map.put("prefix", EteamCommon.returnPrefix(map.get("level").toString()));
			/* 背景色の設定 */
			map.put("bg_color", EteamCommon.bkColorSettei(map.get("yuukou_kigen_from").toString(), map.get("yuukou_kigen_to").toString()));
			/* フル部門名の設定 */
			if(kijunDate != null){
				map.put("full_bumon_name", EteamCommon.connectBumonName(connection, map.get("bumon_cd").toString(),kijunDate));
			}else{
				map.put("full_bumon_name", EteamCommon.connectBumonName(connection, map.get("bumon_cd").toString()));
			}
		}
		
		//とりあえずROOTのみmainListで他はtempListという状態にする
		List<GMap> mainList = new ArrayList<GMap>();
		mainList.add(list.get(0));
		List<GMap> tempList = new ArrayList<GMap>();
		for(int i = 1; i < list.size(); i++){
			tempList.add(list.get(i));
		}
		
		//tempListの先頭から１つずつ取り出しては、mainListに移動させていく
		//mainListへの挿入場所は、mainListの後ろからなめてって兄弟か親が見つかったらそいつの後ろ
		while(tempList.size() > 0){
			GMap tempMap = tempList.get(0);
			for (int j = mainList.size() - 1; j >= 0; j--) {
				GMap mainMap = mainList.get(j);
				if (mainMap.get("oya_bumon_cd").equals(tempMap.get("oya_bumon_cd")) || mainMap.get("bumon_cd").equals(tempMap.get("oya_bumon_cd"))) {
					mainList.add(j + 1, tempMap);
					break;
				}
			}
			tempList.remove(0);
		}
		return mainList;
	}
	
	/** 
	 * 階層を判定してプレフィックスを返却します。
	 * @param level 階層
	 * @return プレフィックス
	 */
	public static String returnPrefix(String level) {
		StringBuilder prefix = new StringBuilder();
		for(int i = 1;i < Integer.parseInt(level); i++) {
			prefix.append("　　");
		}
		return prefix.toString();
	}
	
	/** 
	 * 渡された値に含まれる改行コードを判定して返却します。
	 * @param str 値
	 * @return 改行コード
	 */
	public static String getCRChars(String str){
		if(str.indexOf("\r\n") > -1){
			return "\r\n";
		}else if(str.indexOf("\n") > -1){
			return "\n";
		}else if(str.indexOf("\r") > -1){
			return "\r";
		}else{
			return "";
		}
	}

	/**
	 * アクションクラスから画面IDを判定する。
	 * アクションクラス名の語尾"Action"を取りのぞいたのが画面IDである。
	 * @param actionClass アクションクラス
	 * @return 画面ID
	 */
	@SuppressWarnings("rawtypes")
	public static String makeGamenId(Class actionClass) {
		String gamenId = actionClass.getSimpleName();
		gamenId = gamenId.replaceAll("ExtAction", "Action");// カスタマイズ案件で拡張したAction向けの処理
		gamenId = gamenId.replaceAll("Kyoten$", ""); // 財務拠点入力のワークフローコントロール向けの処理
		gamenId = gamenId.substring(0, gamenId.length() - EteamConst.Application.ACTION_LENGTH);
		return gamenId;
	}
	
	/**
	 * ContentDispositionを編集し返却します。
	 * @param browserCode  ブラウザー判定結果
	 * @param isAttachment アタッチメントの設定の有無
	 * @param fileName     ファイル名
	 * @return contentDisposition
	 */
	public static String contentDisposition(int browserCode, boolean isAttachment, String fileName) {
		StringBuilder stb = new StringBuilder();
		
		if (isAttachment) {
			stb.append("attachment; "); // ファイルダウンロード
		} else {
			stb.append("inline; "); // 画面表示
		}
		stb.append("filename=\"");
		try {
			if (browserCode == BrowserCode.OPERA || browserCode == BrowserCode.IE || browserCode == BrowserCode.EDGE) {
				// 【OK：IE/Edge/Chrome/Opera【NG：Firefox/Safari/Netscape
				stb.append(URLEncoder.encode(fileName, "UTF-8"));
			} else if (browserCode == BrowserCode.FIREFOX || browserCode == BrowserCode.CHROME) {
				// 【OK：Firefox/Chrome【NG：IE/Safari/Opera/Netscape RFC2047 標準の変換方法らしい
				stb.append(MimeUtility.encodeWord(fileName, "ISO-2022-JP", "B"));
			} else if (browserCode == BrowserCode.SAFARI) {
				// 【OK：Safari Ver5.1.7 他のバージョンは大丈夫か？？】
				stb.append(new String(fileName.getBytes(), "ISO-8859-1"));
			} else if(browserCode == BrowserCode.NETSCAPE) {
				// Netscapeは開発スコープ対象外の為、未対応
				stb.append(new String(fileName.getBytes(), "UTF-8"));
			} else {
				// それ以外は開発スコープ対象外の為、未対応
				stb.append(new String(fileName.getBytes(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		stb.append("\"");
		return stb.toString();
	}
	
	/**
	 * アップロードファイルサイズエラーチェック
	 */
	public static void uploadFileSizeCheck() {
		// アップロードファイルのサイズがStrutsデフォルトサイズを超えているか判定する。
		MultiPartRequestWrapper multipartRequest = null;
		try {
			multipartRequest = ((MultiPartRequestWrapper) ServletActionContext.getRequest());
		} catch (NullPointerException e) {
			//UTテスト用（ServletActionContextを使用しない）
		}
		if(multipartRequest != null && multipartRequest.hasErrors()) {
			Iterator<LocalizedMessage> i = multipartRequest.getErrors().iterator();
			while (i.hasNext()) {
				String errMag = i.next().getDefaultMessage();
				if(errMag.startsWith("Request exceeded allowed size limit") || errMag.startsWith("the request was rejected because its size")) {
					log.error(errMag);
					throw new EteamUploadFileSizeOverException();
				}
			}
		}
	}

	/**
	 * 伝票作成単位のマップを取得する。
	 * @return 結果
	 */
	public static Map<String, Boolean> getWfLevelRenkeiMap() {
		ShiwakeDataImportLogic shiwaleLg = EteamContainer.getComponent(ShiwakeDataImportLogic.class);
		return  shiwaleLg.getWfLevelRenkeiMap();
	}

	/**
	 * SJIS(Windows-31j)であることをチェックして、エラーであればエラーメッセージをリストに詰める。
	 * @param s 入力
	 * @param name 項目名
	 * @return エラーメッセージ。正常の時NULL
	 */
	public static String checkSJIS(String s, String name) {
		if (StringUtils.isNotEmpty(s)) {
			try {
				byte[] bytes = s.getBytes(Encoding.MS932);
				if (! s.equals(new String(bytes, Encoding.MS932))) {
					return (name + "に禁則文字が含まれています。Windows-31jの許容文字を入力してください。");
				}
			} catch (UnsupportedEncodingException e) {
				return (name + "に禁則文字が含まれています。Windows-31jの許容文字を入力してください。");
			}
		}
		return null;
	}

	/**
	 * 入力が半角数字および値の範囲の妥当性をチェックをして、エラーであればエラーメッセージをリストに詰める。
	 * キー項目であればEteamIllegalRequestException
	 * @param s 入力
	 * @param min 最小値
	 * @param max 最大値
	 * @param name 項目名
	 * @return エラーメッセージ。正常の時NULL
	 */
	public static String checkNumberRange2(String s, long min, long max, String name) {
		if (StringUtils.isNotEmpty(s)) {
			if (!s.matches("[0-9]+")) {
				return (name + "は半角数字で入力してください。");
			} else {
				long l = GenericTypeValidator.formatLong(s);
				if(! (min <= l && l <= max)){
					return (name + "は" + formatMoney(new BigDecimal(min)) + "～" + formatMoney(new BigDecimal(max)) + "の範囲で入力してください。");
				}
			}
		}
		return null;
	}

	/**
	 * BigDecimal型を文字列(カンマ区切り数値)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	public static String formatMoney(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("#,###").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}

	/**
	 * List<GMap>をString[]に
	 * @param list リスト
	 * @param columnName Mapのキー(カラム名等)
	 * @return 配列
	 */
	public static String[] mapList2Arr(List<GMap> list, String columnName) {
		String[] ret = new String[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i).get(columnName).toString();
		}
		return ret;
	}

	/**
	 * yyyy/MM/dd変換
	 * @param d 日付
	 * @return yyyy/MM/dd(dがnullならブランク)
	 */
	public static String formatDate(Date d) {
		return (null == d) ? "" : new SimpleDateFormat("yyyy/MM/dd").format(d);
	}
	
	/**
	 * yyyy/MM/dd変換
	 * @param d 日付
	 * @return yyyy/MM/dd(dがnullならブランク)
	 */
	public static String formatDate(Timestamp d) {
		return (null == d) ? "" : new SimpleDateFormat("yyyy/MM/dd").format(d);
	}

	/**
	 * yyyy/MM/ddからDateへ。nullやブランクはnullへ。
	 * @param yyyy_mm_dd 変換前
	 * @return 変換後
	 */
	public static Date toDate(String yyyy_mm_dd){
		try {
			if (StringUtils.isEmpty(yyyy_mm_dd))
			{
				return null;
			}
			if(yyyy_mm_dd.indexOf("-") != -1) {
				return new Date(new SimpleDateFormat("yyyy-MM-dd").parse(yyyy_mm_dd).getTime());
			}
			else {
				return new Date(new SimpleDateFormat("yyyy/MM/dd").parse(yyyy_mm_dd).getTime());
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * カンマ入り半角数字をBigDecimalに。nullやブランクはnullへ。
	 * @param s 変換前
	 * @return 変換後
	 */
	public static BigDecimal toDecimal(String s){
		return StringUtils.isEmpty(s) ?
			null :
			new BigDecimal(s.replaceAll(",", ""));
	}

	/**
	 * システム管理の機能制御を検索し機能の有効・無効を判定します。
	 * @param sysLogic システムカテゴリーロジック
	 * @param kinouCd  機能制御コード
	 * @return 有効:[true] 無効:[false]
	 */
	public static boolean funcControlJudgment(SystemKanriCategoryLogic sysLogic, String kinouCd) {
		return KINOU_SEIGYO_KBN.YUUKOU.equals(sysLogic.findKinouSeigyo(kinouCd).get("kinou_seigyo_kbn").toString());
	}

	/**
	 * 処理中WEBコンテキストのスキーマ名を取得
	 * Apache→Tomcatに渡されるリクエストヘッダー：schemaNameの値を取得。
	 * UTではserver.xmlのContextParameterから取得
	 * 
	 * @return スキーマ名
	 */
	public static String getContextSchemaName() {
		String ret = null;
		try{
			ret = ServletActionContext.getRequest().getHeader("schemaName");
		} catch (NullPointerException e) {
			//UT用
			return EteamThreadMap.get().get(SYSTEM_PROP.SCHEMA);
		}
		return (null != ret) ? ret : ServletActionContext.getServletContext().getInitParameter("schemaName");
	}

	/**
	 * 処理中BATのスキーマ名を取得
	 * @return スキーマ名
	 */
	public static String getBatSchemaName() {
		Map<String, String> threadMap = EteamThreadMap.get();
		return threadMap.get(SYSTEM_PROP.SCHEMA);
	}

	/**
	 * &nbsp;を半角スペースに置換
	 * @param s 文字列配列
	 */
	public static void trimNoBlank(String[] s) {
		if (s != null)
		{
			for (int i = 0; i < s.length; i++) {
				if (StringUtils.isNotEmpty(s[i])) {
					s[i] = s[i].replace(' ', ' ');
				}
			}
		}
	}
	
	/**
	 * 指定バイト数以内に文字列の先頭から切り取る
	 * @param s 元の文字列 
	 * @param length バイト数
	 * @return 切り取り後
	 */
	public static String byteCut(String s, int length) {
		if (s == null)
		{
			return "";
		}
		try {
			if (s.getBytes(Encoding.MS932).length <= length)
			{
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		StringBuffer buf = new StringBuffer();
		int byteCount = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			int cBLen;
			try {
				cBLen = c.getBytes(Encoding.MS932).length;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			if (byteCount + cBLen > length)
			{
				break;
			}
			buf.append(c);
			byteCount += cBLen;
		}
		return buf.toString();
	}
	
	/**
	 * util.Dateからsql.Date
	 * @param d util.Date
	 * @return sql.Date
	 */
	public static Date d2d(java.util.Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}
	
	
	/**
	 * SJISバイト数
	 * @param s 文字列
	 * @return バイト数
	 */
    public static int getByteLength(String s) {
        try {
			return s.getBytes(Encoding.MS932).length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("バイト数取得エラー[" + s + "]", e);
		}
    }
    
	//http://dev.livetp.com/java/0306.html からコピー
    
    /** 後ろに偶数個の「"」が現れる「,」にマッチする正規表現 */
    static final String REGEX_CSV_COMMA = ",(?=(([^\"]*\"){2})*[^\"]*$)";  
    /** 最初と最後の「"」にマッチする正規表現*/
    static final String REGEX_SURROUND_DOUBLEQUATATION = "^\"|\"$";  
    /** 「""」にマッチする正規表現 */
    static final String REGEX_DOUBLEQUOATATION = "\"\"";  

     /** 
      * カンマ区切りで行を分割し、文字列配列を返す。
      * 
      * ※下記について、アンエスケープ後の文字列を返す。
      * 1. 値にカンマ(,)を含む場合は,値の前後をダブルクオート(")で囲う
      * 2. ダブルクオート(")は，2つのダブルクオートに置換する("")
      * @param line 分割後
      * @return 文字列
      */
     public static String[] splitLineWithComma(String line) {
         // 分割後の文字列配列
         String[] arr = null;
  
         try {
             // １、「"」で囲まれていない「,」で行を分割する。
             Pattern cPattern = Pattern.compile(REGEX_CSV_COMMA); 
             String[] cols = cPattern.split(line, -1);  
              
             arr = new String[cols.length];  
             for (int i = 0, len = cols.length; i < len; i++) {  
                 String col = cols[i].trim();  
                  
                 // ２、最初と最後に「"」があれば削除する。
                 Pattern sdqPattern = 
                     Pattern.compile(REGEX_SURROUND_DOUBLEQUATATION); 
                 Matcher matcher = sdqPattern.matcher(col);  
                 col = matcher.replaceAll("");  
       
                 // ３、エスケープされた「"」を戻す。 
                 Pattern dqPattern = 
                     Pattern.compile(REGEX_DOUBLEQUOATATION); 
                 matcher = dqPattern.matcher(col);  
                 col = matcher.replaceAll("\"");  
                  
                 arr[i] = col;  
             }
         } catch (Exception e) {
             e.printStackTrace();
         } 
          
         return arr;  
     }

     /**
	 * Windowsのプロセス実行
	 * @param command コマンド
	 * @return 結果コード
	 */
	public static int executeWindowsProcess(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			return process.waitFor();
		} catch (IOException e) {
			log.error("実行コマンド:" + command);
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		} catch (InterruptedException e) {
			log.error("実行コマンド:" + command);
			throw new RuntimeException("プロセスが中断されました。", e);
		}
	}
	
	
    /**
	 * WindowsのプロセスをLinuxからSSH接続経由で実行
	 * @param batFileStr 実行バッチ名
	 * @param resFileStr 結果出力ファイル名
	 * @return 結果コード
	 */
	public static int executeWindowsProcessFromLinux(String batFileStr, String resFileStr) {
		String command = "";
		try {
			//SSH経由でbatFileStr実行
			//WindowsサーバへのSSH接続ユーザは設定済みの前提
			
			int resCode = -9999;
			
			//接続用ユーザーを取得
			String user = RegAccess.readRegistory("","WinUser");
			//接続先IPアドレスを取得
			String host = RegAccess.readRegistory(REG_KEY_NAME.SIAS_SVIP);
			//バッチ呼出コマンド作成
			command = "ssh " + user + "@" + host + " " + batFileStr;
			//バッチ呼出
			Runtime.getRuntime().exec(command);
			
			//1秒ごとにバッチ実行結果ファイル存在チェックし、ファイルが存在していたら該当ファイル内の数値を返却
			File resFile = new File(resFileStr);
			for(int t = 0 ; t < 30 ; t++) {
				//30秒経っても結果出力無しならタイムアウト
				if(resFile.exists()) {
					BufferedReader brd = new BufferedReader(new FileReader(resFile));
					String line = brd.readLine();
					if ( line != null ) {
						resCode = Integer.parseInt(line);
					}
					brd.close();
					break;
				}
	    		Thread.sleep(1000);
			}
			
			return resCode;
		} catch (IOException e) {
			log.error("実行コマンド:" + command);
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		} catch (InterruptedException e) {
			log.error("実行コマンド:" + command);
			throw new RuntimeException("プロセスが中断されました。", e);
		}
	}

     /**
      * 今日日付
      * @return 今日日付
      */
     public static Date today() {
    	 return new Date(System.currentTimeMillis());
     }
     /**
      * 明日日付
      * @return 今日日付
      */
     public static Date tomorrow() {
    	 return new java.sql.Date(today().getTime() + 24*60*60*1000);
     }
 	
 	/**
 	 * 日付が範囲内かチェックする
 	 * @param d 対象日付
 	 * @param from FROM
 	 * @param to TO
 	 * @return 範囲内ならtrue
 	 */
 	public static boolean between(java.util.Date d, java.util.Date from, java.util.Date to) {
 		int dVal  = Integer.valueOf(DateFormatUtils.format(d , "yyyyMMdd"));
 		int fromVal  = Integer.valueOf(DateFormatUtils.format(from , "yyyyMMdd"));
 		int toVal  = Integer.valueOf(DateFormatUtils.format(to , "yyyyMMdd"));
 		return fromVal <= dVal && dVal <= toVal;
 	}
}