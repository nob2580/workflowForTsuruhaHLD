package eteam.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts2.interceptor.SessionAware;

import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamSettingInfo;
import eteam.gyoumu.user.User;
import eteam.gyoumu.user.UserLogic;
import lombok.Getter;
import lombok.Setter;

//＜定数＞
//＜画面入力＞
//＜画面入力以外＞
//＜入力チェック＞
//＜イベント＞
//【標準フロー】
//1.入力チェック
//2.データ存在チェック
//3.アクセス可能チェック
//4.処理
//5.戻り値を返す

/**
 * 全アクションクラスの親。
 */
public abstract class EteamAbstractAction implements SessionAware {

	/** ログ */
	protected static EteamLogger log = EteamLogger.getLogger(EteamAbstractAction.class);

	/** ユーザ */
	@Setter User user;

	/** セッション */
	@Getter @Setter protected Map<String, Object> session;

	/** 設定情報 */
	@Getter @Setter protected EteamSettingInfo setting;

	/** エラー情報 */
	@Getter @Setter public List<String> errorList = new ArrayList<String>(){
        @Override
        public boolean add(String e) {
            if (! super.contains(e)) {
                return super.add(e);
            }
            return false;
        }
    };
    
    /** 初期化 */
    public EteamAbstractAction()
    {
        setting = new EteamSettingInfo();
    }

    /**
     * アクセスしているテナントID(スキーマ名)を取得
     * struts.xmlでリダイレクトのパスに必要なので、全Actionの属性として持たせておく。
     * @return テナントID(スキーマ名)
     */
    public String getSchemaName() {
        return EteamCommon.getContextSchemaName();
    }

    /**
     * セッション・ユーザー情報取得
     * @return セッション・ユーザー情報
     */
    protected User getUser() {
        //UTテスト用（ActionContextを使用しない）
        if (System.getProperty("ut_user") != null) {
            try(EteamConnection connection = EteamConnection.connect()) {
                UserLogic ul = new UserLogic();
                ul.init(connection);
                User user = ul.makeSessionUser(System.getProperty("ut_user"));
                if (System.getProperty("ut_gyoumu_role").isEmpty()) {
                    ul.changeGyoumuRole(user, null);
                } else {
                    ul.changeGyoumuRole(user, System.getProperty("ut_gyoumu_role"));
                }
            }
        }
        return user;
    }
    
    /**
     * 入力がnullまたは空でないことのチェックをして、エラーであればエラーメッセージをリストに詰める。
     * @param s 入力
     * @param name 項目名
     */
    public void checkHissu(String s, String name) {
        if(isEmpty(s)){
            errorList.add(name + "を入力してください。");
        }
    }

    /**
     * 入力がnullまたは空でないことのチェックをして、エラーであればエラーメッセージをリストに詰める。
     * @param f 入力
     * @param name 項目名
     */
    public void checkHissu(File f, String name) {
        if (! (null != f && f.exists() && 0 < f.length())) {
            errorList.add(name + "が選択されていません。またはファイルが空です。");
        }
    }

    /**
     * 入力が半角数字であるかチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     * @return エラーメッセージあり
     */
    public boolean checkNumber(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("^[0-9]*$")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角数字で入力してください。");
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 入力が半角数字であるかチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小桁数
     * @param max 最大桁数
     * @param name 項目名
     * @param isKey キー項目である
     * @return エラーメッセージあり
     */
    public boolean checkNumber(String s, int min, int max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("^[0-9]{" + min + "," + max + "}$")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    if (min == max) {errorList.add(name + "は半角数字" + min + "桁で入力してください。");}
                    else {errorList.add(name + "は半角数字" + min + "～" + max + "桁で入力してください。"); }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 入力が半角数字であるかチェックをして、エラーであればエラーメッセージをリストに詰める。
     * 1以上であること
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小桁数
     * @param max 最大桁数
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkNumberOver1(String s, int min, int max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if (! checkNumber(s, min, max, name, isKey)) return;
            if (GenericTypeValidator.formatLong(s) < 1) {
                if(isKey) {
                    throw new EteamIllegalRequestException();
                } else {
                    errorList.add(name + "は1以上で入力してください");
                }
            }
        }
    }
    
    /**
     * 入力が0.5単位の日数であるかチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkNissuu(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if( (! s.matches("^([1-9]\\d{0,2}|0)(\\.\\d{0,1}+)?$")) || s.equals("0") || s.equals("0.0")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は0.5～999.5までの0.5日単位で入力してください。");
                }
            } else if(s.indexOf(".") > 0)  {
                if( !(s.substring(s.length()-1, s.length())).equals("0") && !(s.substring(s.length()-1, s.length())).equals("5") ){
                    errorList.add(name + "は0.5日単位で入力してください。");
                }
            }
        }
    }
    
    /**
     * 0を含めて、入力が0.5単位の日数であるかチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkNissuu0(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if( (! s.matches("^([1-9]\\d{0,2}|0)(\\.\\d{0,1}+)?$"))){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は0.0～999.5までの0.5日単位で入力してください。");
                }
            } else if(s.indexOf(".") > 0)  {
                if( !(s.substring(s.length()-1, s.length())).equals("0") && !(s.substring(s.length()-1, s.length())).equals("5") ){
                    errorList.add(name + "は0.5日単位で入力してください。");
                }
            }
        }
    }
    
    /**
     * 入力が半角数字および値の範囲の妥当性をチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小値
     * @param max 最大値
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkNumberRange(String s, long min, long max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            s = s.replaceAll(",", "");
            if (! GenericValidator.isLong(s)) {
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角数字で入力してください。");
                }
            } else {
                long l = GenericTypeValidator.formatLong(s);
                if(! (min <= l && l <= max)){
                    if(isKey){
                        throw new EteamIllegalRequestException();
                    }else{
                        errorList.add(name + "は" + formatMoney(new BigDecimal(min)) + "～" + formatMoney(new BigDecimal(max)) + "の範囲で入力してください。");
                    }
                }
            }
        }
    }
    
    /**
     * 入力が半角数字および値の範囲の妥当性をチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小値
     * @param max 最大値
     * @param point 小数点以下桁数
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkNumberRangeDecimalPoint(String s, double min, double max, int point, String name, boolean isKey) {
        if(isNotEmpty(s)){
            s = s.replaceAll(",", "");
            boolean isNumber = GenericValidator.isDouble(s);
            boolean hasValidDecimalPoint = isNumber && s.matches("^[-]?\\d+(\\.\\d{0," + point + "})?$");
            boolean isInRange = isNumber && GenericValidator.isInRange(GenericTypeValidator.formatDouble(s), min, max);
            
            // キー項目なら共通の不正例外（現在の使用箇所なし）
            if(!(hasValidDecimalPoint && isInRange) && isKey)
            {
                throw new EteamIllegalRequestException();
            }
            
            // 数値でないならフォーマットでエラーにしておしまい
            if(!isNumber)
            {
                errorList.add(name + "は半角数字で入力してください。");
                return;
            }
            
            // 桁数違反
            if(!hasValidDecimalPoint)
            {
                errorList.add(name + "は小数点" + point + "桁以下で入力してください。");
            }
            
            // 範囲違反（桁数違反と両立しうる）
            if(!isInRange)
            {
                errorList.add(name + "は" + formatMoney(new BigDecimal(min)) + "～" + formatMoney(new BigDecimal(max)) + "の範囲で入力してください。");
            }
        }
    }

    /**
     * 入力が半角数字および値の範囲の妥当性をチェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小値
     * @param max 最大値
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkNumberRange3(String s, long min, long max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("^[0-9]{" + min + "," + max + "}$")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角数字" + min + "～" + max + "の範囲で入力してください。");
                }
            }else{
                long l = GenericTypeValidator.formatLong(s);
                if(! (min <= l && l <= max)){
                    if(isKey){
                        throw new EteamIllegalRequestException();
                    }else{
                        errorList.add(name + "は半角数字" + min + "～" + max + "の範囲で入力してください。");
                    }
                }
            }
        }
    }

    /**
     * 文字列の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小桁数
     * @param max 最大桁数
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkString(String s, int min, int max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! (min <= s.length() && s.length() <= max)){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    if (min == max) {errorList.add(name + "は" + min + "文字で入力してください。"); }
                    else {errorList.add(name + "は" + min + "～" + max + "文字で入力してください。"); }
                }
            }
        }
    }
    
    /**
     * インボイス・事業者番号のチェックは特殊なので共通処理として追加する。
     * @param s 入力
     * @param name 項目名
     */
    public void checkJigyoushaNo(String s, String name)
    {
    	boolean isValidNumber = false;
    	try
    	{
    		Long.parseLong(s.substring(1));
    		isValidNumber = true;
    	}
    	catch(Exception e)
    	{
    	}
    	
    	if(isNotEmpty(s) && (s.length() != 14 || !s.startsWith("T") || !isValidNumber))
    	{
    		errorList.add(name + "はT+13桁の半角数字で入力してください。");
    	}
    }

    /**
     * 文字列が半角英数字であることをチェックし、エラーであればエラーメッセージをリストに詰める。
     * @param s 入力
     * @param name 項目名
     */
    public void checkHankakuEiSuu(String s, String name) {
        if(isNotEmpty(s)){
            if(s.matches(".*[^0-9a-zA-Z].*")){
                errorList.add(name + "は半角英数字で入力してください。");
            }
        }
    }

    /**
     * 文字列が半角英数字「_(アンダーバー)」であることをチェックし、エラーであればエラーメッセージをリストに詰める。
     * @param s 入力
     * @param name 項目名
     */
    public void checkHankakuEiSuuUnderbar(String s, String name) {
        if(isNotEmpty(s)){
            if(s.matches(".*[^0-9a-zA-Z_].*")){
                errorList.add(name + "は半角英数字「_(アンダーバー)」で入力してください。");
            }
        }
    }

    /**
     * 文字列が半角英数字「-(ハイフン)」であることをチェックし、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkHankakuEiSuuHaihun(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(s.matches(".*[^0-9a-zA-Z-].*")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角英数字「-(ハイフン)」で入力してください。");
                }
            }
        }
    }

    /**
     * 文字列が半角カナ英数字「-(ハイフン)」であることをチェックし、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkHankakuKanaEiSuuHaihun(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(s.matches(".*[^a-zA-Z0-9ｦ-ﾝ()｢｣.\\-\\\\/ﾞﾟ\\s].*")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角カナ英数記号で入力してください。");
                }
            }
        }
    }

    /**
     * 文字列が半角英数字「-(ハイフン)」「/(スラッシュ)」であることをチェックし、エラーであればエラーメッセージをリストに詰める。
     * @param s 入力
     * @param name 項目名
     */
    public void checkHankakuEiSuuHaihunSlash(String s, String name) {
        if(isNotEmpty(s)){
            if(s.matches(".*[^0-9a-zA-Z-/].*")){
                errorList.add(name + "は半角英数字「-(ハイフン)」「/(スラッシュ)」で入力してください。");
            }
        }
    }
    
    /**
     * 文字列の入力チェック(部門コード専用)をして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最小桁数
     * @param max 最大桁数
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkBumonCd(String s, int min, int max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! (min <= s.length() && s.length() <= max)){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    if (min == max) {errorList.add(name + "は" + min + "文字で入力してください。"); }
                    else {errorList.add(name + "は" + min + "～" + max + "文字で入力してください。"); }
                }
            }
            if(s.matches(".*[^0-9a-zA-Z-/].*")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角英数字「-(ハイフン)」「/(スラッシュ)」で入力してください。");
                }
            }
        }
    }

    /**
     * 日付の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkDate(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}") || ! GenericValidator.isDate(s, "yyyy/MM/dd", true)){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は日付形式(yyyy/MM/dd)で入力してください。");
                }
            }
        }
    }

    /**
     * 日付の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkDateYYYYMM(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("[0-9]{4}[0-9]{2}") || ! GenericValidator.isDate(s, "yyyyMM", true)){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は日付形式(yyyyMM)で入力してください。");
                }
            }
        }
    }
    
    /**
     * yyyyMMdd形式日付の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkYYYYMMDD(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("[0-9]{4}[0-9]{2}[0-9]{2}") || ! GenericValidator.isDate(s, "yyyyMMdd", true)){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は日付形式(yyyyMMdd)で入力してください。");
                }
            }
        }
    }

    /**
     * 日付の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkTime(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("^([1-9]*[1-9]*[0-9][0-9]):([0-5][0-9])$")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は時刻形式(hh:mm～hhhh:mm)で入力してください。");
                }
            }
        }
    }

    /**
     * 時刻（時）の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkHour(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("([0-1][0-9]|[2][0-3])")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は00～23までの2桁の数字を入力してください。");
                }
            }
        }
    }
    
    /**
     * 時刻（分）の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkMin(String s, String name, boolean isKey) {
        if(isNotEmpty(s)){
            if(! s.matches("[0-5][0-9]")){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は00～59までの2桁の数字を入力してください。");
                }
            }
        }
    }

    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。（0以上の標準値）
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuOver0(String s, String name, boolean isKey) {
        checkKingaku(s, 0L, 999999999999L, name, isKey);
    }

    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。（0以上の標準値、小数点以下3桁）
     * キー項目であればEteamIllegalRequestException
     * チェック機能的にはmax値の小数点以下を5桁にしたほうが合理的ではあるが、そうするとエラーメッセージに表示されるmax値と実際に入力できるmax値に乖離が生じるため個別メソッドとする。
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingaku3thDecimalPlaceOver0(String s, String name, boolean isKey) {
    	checkKingakuDecimal(s, new BigDecimal("0.0"), new BigDecimal("999999999999.999"), name, isKey);
    }
    
    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。（1以上の標準値）
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuOver1(String s, String name, boolean isKey) {
        checkKingaku(s, 1L, 999999999999L, name, isKey);
    }
    
    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。（1以上の標準値、小数点以下2桁）
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuDecimalOver1(String s, String name, boolean isKey) {
        checkKingakuDecimal(s, new BigDecimal("1.0"), new BigDecimal("999999999999.99"), name, isKey);
    }
    
    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。（1以上の標準値、小数点以下5桁）
     * キー項目であればEteamIllegalRequestException
     * 主にレートのチェックで用いられる。
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuDecimalMoreThan0(String s, String name, boolean isKey) {
        checkKingakuDecimalMoreThan(s, new BigDecimal("0.0"), new BigDecimal("999999.99999"), name, isKey);
    }

    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。（マイナスありの標準値）
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuMinus(String s, String name, boolean isKey) {
        checkKingaku(s, -999999999999L, 999999999999L, name, isKey);
    }

    /**
     * 金額の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最少値
     * @param max 最大値
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingaku(String s, long min, long max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            s = s.replaceAll(",", "");
            if (! GenericValidator.isLong(s)) {
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角数字(カンマ区切り)で入力してください。");
                }
            } else {
                long l = GenericTypeValidator.formatLong(s);
                if(! (min <= l && l <= max)){
                    if(isKey){
                        throw new EteamIllegalRequestException();
                    }else{
                        errorList.add(name + "は" + formatMoney(new BigDecimal(min)) + "～" + formatMoney(new BigDecimal(max)) + "の範囲で入力してください。");
                    }
                }
            }
        }
    }

    /**
     * 金額（小数点有り）の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最少値
     * @param max 最大値
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuDecimalMoreThan(String s, BigDecimal min, BigDecimal max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            s = s.replaceAll(",", "");
            if (! GenericValidator.isDouble(s)) {
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角数字(カンマ区切り)と小数点（ドット）で入力してください。");
                }
            } else {
            	BigDecimal l = new BigDecimal(s);
                if(! (min.compareTo(l) < 0 && l.compareTo(max) <= 0)){
                    if(isKey){
                        throw new EteamIllegalRequestException();
                    }else{
                        errorList.add(name + "は" + formatMoneyDecimal(min) + "より大きく、" + formatMoneyDecimal(max) + "以下の範囲で入力してください。");
                    }
                }
            }
        }
    }
    
    /**
     * 金額（小数点有り）の入力チェックをして、エラーであればエラーメッセージをリストに詰める。(外部クラスからの呼び出し用)
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最少値
     * @param max 最大値
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuDecimal(String s, String min, String max, String name, boolean isKey) {
    	checkKingakuDecimal(s, new BigDecimal(min), new BigDecimal(max), name, isKey);
    }
    
    /**
     * 金額（小数点有り）の入力チェックをして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param min 最少値
     * @param max 最大値
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkKingakuDecimal(String s, BigDecimal min, BigDecimal max, String name, boolean isKey) {
        if(isNotEmpty(s)){
            s = s.replaceAll(",", "");
            if (! GenericValidator.isFloat(s)) {
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "は半角数字(カンマ区切り)と小数点（ドット）で入力してください。");
                }
            } else {
            	BigDecimal l = new BigDecimal(s);
                if(! (min.compareTo(l) <= 0 && l.compareTo(max) <= 0)){
                    if(isKey){
                        throw new EteamIllegalRequestException();
                    }else{
                        errorList.add(name + "は" + formatMoneyDecimal(min) + "～" + formatMoneyDecimal(max) + "の範囲で入力してください。");
                    }
                }
            }
        }
    }

    /**
     * SJIS(Windows-31j)であることをチェックして、エラーであればエラーメッセージをリストに詰める。
     * @param s 入力
     * @param name 項目名
     */
    public void checkSJIS(String s, String name) {
        if(isNotEmpty(s)){
            try {
                byte[] bytes = s.getBytes(Encoding.MS932);
                if (! s.equals(new String(bytes, Encoding.MS932))) {
                    errorList.add(name + "に禁則文字が含まれています。Windows-31jの許容文字を入力してください。");
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("エンコード不正", e);
            }
        }
    }

    /**
     * ラジオボタンやプルダウンが想定値内であるか、チェックして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param domain 値の範囲
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkDomain(String s, String[] domain, String name, boolean isKey){
        if(isNotEmpty(s)){
            for (String ofDomain : domain) {
                if (s.equals(ofDomain)) {
                    return;
                }
            }
            if(isKey){
                throw new EteamIllegalRequestException();
            }else{
                errorList.add(name + "の送信値が不正です。");
            }
        }
    }

    /**
     * 内部コード設定テーブルに存在するか
     * @param conn コネクション
     * @param s 入力
     * @param codeName 内部コード名
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkCode(EteamConnection conn, String s, String codeName,String name, boolean isKey){
        if(isNotEmpty(s)){
            if (null == conn.find("select naibu_cd from naibu_cd_setting where naibu_cd_name=? and naibu_cd=?", codeName, s)) {
                if(isKey){
                    throw new EteamIllegalRequestException();
                } else{
                    errorList.add(name + "の送信値が不正です。");
                }
            }
        }
    }

    /**
     * チェックボックスが想定値("1"のみ)内であるか、チェックして、エラーであればエラーメッセージをリストに詰める。
     * キー項目であればEteamIllegalRequestException
     * @param s 入力
     * @param name 項目名
     * @param isKey キー項目である
     */
    public void checkCheckbox(String s, String name, boolean isKey){
        if(isNotEmpty(s)){
            if(! "1".equals(s)){
                if(isKey){
                    throw new EteamIllegalRequestException();
                }else{
                    errorList.add(name + "の送信値が不正です。");
                }
            }
        }
    }

    /**
     * ヌルはブランクへ、それ以外ならそのまま。
     * @param s 変換前
     * @return 変換後
     */
    public String n2b(String s) {
        return (null == s) ? "" : s;
    }
    
    /**
     * yyyy/MM/ddからDateへ。nullやブランクはnullへ。
     * @param yyyy_mm_dd 変換前
     * @return 変換後
     */
    public Date toDate(String yyyy_mm_dd){
        try {
            return isEmpty(yyyy_mm_dd) ?
                null :
                new Date(new SimpleDateFormat("yyyy/MM/dd").parse(yyyy_mm_dd).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 半角数字をIntegerに。nullやブランクはnullへ。
     * @param s 変換前
     * @return 変換後
     */
    public Integer toInteger(String s){
        return isEmpty(s) ?
            null :
            Integer.parseInt(s);
    }
    
    /**
     * 半角数字をLongに。nullやブランクはnullへ。
     * @param s 変換前
     * @return 変換後
     */
    public Long toLong(String s){
        return isEmpty(s) ?
            null :
            Long.parseLong(s);
    }
    
    /**
     * カンマ入り半角数字をBigDecimalに。nullやブランクはnullへ。
     * @param s 変換前
     * @return 変換後
     */
    public BigDecimal toDecimal(String s){
        return isEmpty(s) ?
            null :
            new BigDecimal(s.replaceAll(",", ""));
    }
    
    /**
     * カンマ入り半角数字をBigDecimalに。nullやブランクはBigDecimal.ZEROへ。
     * @param s 変換前
     * @return 変換後
     */
    public BigDecimal toDecimalZeroIfEmpty(String s){
        return isEmpty(s) ?
                BigDecimal.ZERO :
                new BigDecimal(s.replaceAll(",", ""));
        }

    /**
     * ファイルをバイト列へ。ファイルなしや0サイズならnullへ。
     * @param f 変換前
     * @return 変換後
     */
    public byte[] toByte(File f) {
        if (! (null != f && f.exists() && 0 < f.length())) {
            return null;
        }
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            FileUtils.copyFile(f, buf);
            return buf.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * 日付型を文字列(yyyy/MM/dd)に変換。
     * @param s 変換前
     * @return 変換後
     */
    public String formatString(Object s) {
        if (null == s) {
            return "";
        } else if (s instanceof String) {
            return ((String)s).trim();
        } else {
            throw new InvalidParameterException("String以外禁止.s:" + s);
        }
    }
    
    /**
     * 日付型を文字列(yyyy/MM/dd)に変換。
     * @param d 変換前
     * @return 変換後
     */
    public String formatDate(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof java.util.Date) {
            return new SimpleDateFormat("yyyy/MM/dd").format(d);
        } else {
            throw new InvalidParameterException("Date以外禁止.d:" + d);
        }
    }
    
    /**
     * Timestamp型を文字列(yyyy/MM/dd HH:mm)に変換。
     * @param t 変換前
     * @return 変換後
     */
    public String formatTime(Object t){
        if (null == t) {
            return "";
        } else if (t instanceof Timestamp) {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(t);
        } else {
            throw new InvalidParameterException("Timestamp以外禁止.t:" + t);
        }
    }
    
    /**
     * Timestamp型を文字列(yyyy/MM/dd HH:mm:ss)に変換。
     * @param t 変換前
     * @return 変換後
     */
    public String formatTimeSS(Object t){
        if (null == t) {
            return "";
        } else if (t instanceof Timestamp) {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(t);
        } else {
            throw new InvalidParameterException("Timestamp以外禁止.t:" + t);
        }
    }
    
    /**
     * BigDecimal型を文字列(カンマ区切り数値)に変換。
     * @param d 変換前
     * @return 変換後
     */
    public String formatMoney(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            return new DecimalFormat("#,###").format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }
    
    /**
     * BigDecimal型を文字列(カンマ区切り数値と小数点)に変換。(小数点以下の0埋め無し)
     * @param d 変換前
     * @return 変換後
     */
    public String formatMoneyDecimalWithNoPadding(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            return new DecimalFormat("#,##0.#####").format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }
    
    /**
     * BigDecimal型を文字列(カンマ区切り数値と小数点)に変換。(小数点以下の0埋めが2桁)
     * @param d 変換前
     * @return 変換後
     */
    public String formatMoneyDecimal(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            return new DecimalFormat("#,##0.00###").format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }
    
    /**
     * BigDecimal型を文字列(カンマ区切り無しの数値と小数点)に変換。
     * @param d 変換前
     * @return 変換後
     */
    public String formatMoneyDecimalNoComma(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            return new DecimalFormat("###0.00###").format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }

    /**
     * BigDecimal型を文字列(カンマ区切り無しの数値と小数点)に変換。(レート用)
     * @param d 変換前
     * @param p パターン
     * @return 変換後
     */
    public String formatDecimalNoComma(Object d, String p){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            String pattern = "";
            if(p.equals("rate")){
                pattern = "###0.00000";
            }
            return new DecimalFormat(pattern).format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }
     
    /**
     * 文字列がnullまたは空である時にtrueを返す。
     * @param s 検査対象
     * @return nullまたは空であればtrue
     */
    public boolean isEmpty(String s){
        return null == s || 0 == s.length();
    }

    /**
     * 文字列が格納されている場合にtrueを返す。
     * @param s 検査対象
     * @return not(nullまたは空)であればtrue
     */
    public boolean isNotEmpty(String s){
        return !isEmpty(s);
    }

    /**
     * アップロードファイルが存在するか(アップロードされたか)チェックする。
     * @param f ファイル(アップロードデータ)
     * @return 存在すればtrue
     */
    public boolean exists(File f) {
        return
            (null != f)
            && (f.exists())
            && (0 < f.length());
    }

    /** 
     * 半角スペース、全角スペースを取り除いた文字列を返却します。
     * @param str String 対象の文字列
     * @return 返却文字列
     */
    public String replaceSpaceDelete(String str) {
        return str.replaceAll("[ 　]", "");
    }
	
    /**
     * フォーマットチェックを行う。
     * エラーがあれば、項目単位のメッセージがerrorListに格納される。
     */
    protected abstract void formatCheck();

    /**
     * 必須チェックを行う。
     * エラーがあれば、項目単位のメッセージがerrorListに格納される。
     * @param eventNum イベント番号(1～)
     */
    protected abstract void hissuCheck(int eventNum);

    /**
     * 可変長リスト向け共通必須チェック。
     * listは項目単位である。list[i]の構造は以下のとおり。<br>
     * list[i][0] 値<br>
     * list[i][1] 項目和名（エラーメッセージ用）<br>
     * list[i][2] EVENT1のフラグ（0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック）<br>
     * list[i][3] EVENT2のフラグ（0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック）<br>
     * 以下略list[i][2～]はイベントの数分ある<br>
     * 
     * @param list チェック項目
     * @param eventNum イベント番号(1～)
     */
    protected void hissuCheckCommon(List<String[]> list, int eventNum)
    {
    	hissuCheckCommon(list.toArray(new String[list.size()][]), eventNum);
    }
    /**
     * hissuCheck(void)から呼ばれる共通処理。<br>
     * listは項目単位である。list[i]の構造は以下のとおり。<br>
     * list[i][0] 値<br>
     * list[i][1] 項目和名（エラーメッセージ用）<br>
     * list[i][2] EVENT1のフラグ（0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック）<br>
     * list[i][3] EVENT2のフラグ（0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック）<br>
     * 以下略list[i][2～]はイベントの数分ある<br>
     * 
     * @param list チェック項目
     * @param eventNum イベント番号(1～)
     */
    protected void hissuCheckCommon(String[][] list, int eventNum){
        int hissuIndex = eventNum + 1;
        for (String[] chk : list) {
        	hissuCheckCommon(chk[0], chk[1], chk[hissuIndex]);
        }
    }
    /**
     * 必須チェック
     * @param val 値
     * @param name 項目和名
     * @param flg フラグ（0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック）
     */
    protected void hissuCheckCommon(String val, String name, String flg){
        if ("2".equals(flg)) {
            //キーがないのは不正リクエスト
            if(isEmpty(val)){
                throw new EteamIllegalRequestException();
            }
        } else if ("1".equals(flg)) {
            //ユーザーの入力がないのは入力エラー
            if(isEmpty(val)){
                errorList.add(name + "を入力してください。");
            }
        }
    }
}
