package eteam.gyoumu.masterkanri;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;

import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import lombok.Getter;
import lombok.Setter;

/**
 * DBのカラム情報です。
 *
 */
public class MasterColumnInfo {
	
	/**
	 * マスターID
	 */
	String masterId;
	
	/**
	 * カラムNo
	 */
	@Getter
	protected int index;
	/**
	 * PKかどうか
	 */
	@Getter @Setter
	protected boolean isPk = false;
	/** DBデータ */
	protected GMap data;

	/**
	 * コンストラクタです。
	 * @param masterId マスターID
	 * @param index 列番号
	 * @param data 列情報
	 */
	public MasterColumnInfo(String masterId, int index, GMap data) {
		this.masterId = masterId;
		this.index = index;
		this.data = data;
	}

	/**
	 * 列に対応した入力指定classを取得します。
	 * @return 結果
	 */
	public String getInputClassText() {
		// 日付だけ対応する
		switch (getType()) {
		case "date":
			return " datepicker ";
		}
		return "";
	}

	/**
	 * DBの型に応じて正規化された文字列を返します。
	 * @param str 入力
	 * @return 正規化後の文字列
	 */
	public String getNormalizeString(String str) {
		return convertObjectToString(convertStringToObject(str));
	}

	/**
	 * 最大長属性文字列を取得します。
	 * @return 結果
	 */
	public String getMaxLengthAttrText() {
		//起案番号簿なら開始・終了起案番号の入力文字数制御
		if (masterId.equals("kian_bangou_bo") && (getName().equals("kian_bangou_from") || getName().equals("kian_bangou_to"))) {
			return " maxlength='6' ";
		}
		switch (getType()) {
		case "date":
			return " maxlength='10' ";
		case "int2":
			return " maxlength='6' ";
		case "int4":
			return " maxlength='11' ";
		case "int8":
			return " maxlength='20' ";
		case "numeric":
			return " maxlength='" + (getMaxLength() + 1) + "' ";
		default:
			break;
		}
		if (null != getMaxLength()) {
			return " maxlength='" + getMaxLength() + "' ";
		}
		return "";
	}

	/**
	 * 表示用の型名
	 * @return 型名
	 */
	public String getDisplayTypeName() {
		String tname = "不明";
		switch (getType()) {
		case "varchar":
		case "text":
		case "memo":
		case "bpchar":
			tname = "文字列";
			break;
		case "date":
			tname = "日付";
			break;
		case "time":
			tname = "時刻";
			break;
		case "timestamp":
			tname = "日付時刻";
			break;
		case "int2":
		case "int4":
		case "int8":
		case "numeric":
			tname = "数値";
			break;
		case "bytea":
			tname = "バイナリ";
			break;
		default:
			tname += "(" + getType() + ")";
			break;
		}
		return tname;
	}

	/**
	 * CSV登録用の型名
	 * @return 型名
	 */
	public String getCsvTypeName() {
		String tname = getType();
		switch (getType()) {
		case "varchar":
		case "text":
		case "memo":
		case "bpchar":
			tname = "varchar";
			break;
		case "int2":
			tname = "smallint";
			break;
		case "int4":
			tname = "int";
			break;
		case "int8":
			tname = "bigint";
			break;
		case "numeric":
			tname = "decimal";
			break;
		case "bytea":
			tname = "byte";
			break;
		case "date":
		case "time":
		case "timestamp":
		default:
			break;
		}
		return tname;
	}

	/**
	 * この列の文字列をObject型に変換します。
	 * @param str オブジェクト
	 * @return 文字列
	 */
	public Object convertStringToObject(String str) {
		if (null == str) {
			return null;
		}
		switch (getType()) {
		case "varchar":
		case "text":
		case "memo":
			return str;
		case "bpchar":
			if (str.length() >= getMaxLength()) {
				return str.substring(0, getMaxLength());
			}
			return StringUtils.rightPad(str, getMaxLength());
		case "int2":
		case "int4":
			return Integer.parseInt(str);
		case "int8":
			return Long.parseLong(str);
		case "numeric":
			return EteamCommon.toDecimal(str);
		case "date":
			return EteamCommon.toDate(str);
		case "time":
			try {
				return new Time(new SimpleDateFormat("HH:mm:ss").parse(str).getTime());
			} catch (ParseException e) {}
			break;
		case "timestamp":
			try {
				return new Timestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(str).getTime());
			} catch (ParseException e) {}
			break;
		case "bytea":
		default:
			break;
		}
		return null;
	}

	/**
	 * この列のObject型を文字列に変換します。
	 * @param obj オブジェクト
	 * @return 文字列
	 */
	public String convertObjectToString(Object obj) {
		String str = "";
		if (null == obj) {
			return "";
		}
		if (obj instanceof String) {
			return (String)obj;
		}
		switch (getType()) {
		case "varchar":
		case "text":
		case "memo":
		case "bpchar":
			str = (String)obj;
			break;
		case "int2":
		case "int4":
		case "int8":
			str = obj.toString();
			break;
		case "numeric":
			str = ((BigDecimal)obj).toPlainString();
			break;
		case "date":
			str = EteamCommon.formatDate((java.sql.Date)obj);
			break;
		case "time":
			str = new SimpleDateFormat("HH:mm:ss").format(new Date(((Time)obj).getTime())); 
			break;
		case "timestamp":
			str = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(((Timestamp)obj).getTime()));
		case "bytea":
		default:
			str = "";
			break;
		}
		return str;
	}

	/**
	 * 入力チェックを行います。
	 * @param value 値
	 * @return エラーメッセージ。エラーじゃない時はnull
	 */
	public String checkInput(String value) {
		// 必須チェック
		if (isRequired() && StringUtils.isEmpty(value)) {
			return getDisplayName() + "を入力してください。";
		}

		// SJISチェックをする。（ファイルに落とせなくなるから）
		String errMsg = EteamCommon.checkSJIS(value, getDisplayName());
		if (null != errMsg) {
			return errMsg;
		}
// // カンマチェックをする。変更受入側がエスケープ対応していないため
// if (StringUtils.isNotEmpty(value) && 0 <= value.indexOf(',')) {
// return getDisplayName() + "に「,」が含まれています。";
// }

		// フォーマットチェック
		switch (getType()) {
		case "varchar":
		case "text":
		case "memo":
		case "bpchar":
			if (StringUtils.isNotEmpty(value) && null != getMaxLength() && value.length() > getMaxLength()) {
				return getDisplayName() + "は1～" + getMaxLength() + "文字で入力してください。";
			}
			
			// 半角カナチェック
			String name = getName();
			if (name.endsWith(EteamConst.HankanaCheck.MATCH_COLUMN_STR1) || name.equals(EteamConst.HankanaCheck.MATCH_COLUMN_STR2)) {
				String regex = EteamConst.HankanaCheck.REGEX;
				if (!"".equals(regex) && StringUtils.isNotEmpty(value) && !Pattern.compile("^" + regex + "$").matcher(value).find()) {
					return getDisplayName() + "は半角カナ(英数字記号含む)で入力してください。";
				}
			// 半角英数字チェック
			}else if(name.equals(EteamConst.HankakuCheck.MATCH_COLUMN_STR1)){
				String regex = EteamConst.HankakuCheck.REGEX;
				if(!"".equals(regex) && StringUtils.isNotEmpty(value) && !Pattern.compile("^" + regex + "$").matcher(value).find()){
					return getDisplayName() + "は半角英数字で入力してください。";
				}
			}
			
			// 税率区分チェック（通常税率=0,軽減税率=1）
			if(name.equals("keigen_zeiritsu_kbn")) {
				String regex = "^[0|1]$";
				if (!"".equals(regex) && StringUtils.isNotEmpty(value) && !Pattern.compile(regex).matcher(value).find()) {
					return getDisplayName() + "は半角数字の0または1で入力してください。";
				}
			}
			break;
		case "date":
			if (StringUtils.isNotEmpty(value) &&
				(!value.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}") || !GenericValidator.isDate(value, "yyyy/MM/dd", true))) {
				return getDisplayName() + "は日付形式(yyyy/MM/dd)で入力してください。";
			}
			break;
		case "int2":
			// 数値チェック
			if (getName().equals("kijun_weekday")) {
				String regex = "^[1|2|3|4|5|6|7]$";
				if (!"".equals(regex) && StringUtils.isNotEmpty(value) && !Pattern.compile("^" + regex + "$").matcher(value).find()) {
					return getDisplayName() + "は1~7で入力してください。";
				}
			}else if (getName().equals("furikomi_weekday")) {
				String regex = "^[1-9][1|2|3|4|5]$";
				if (!"".equals(regex) && StringUtils.isNotEmpty(value) && !Pattern.compile("^" + regex + "$").matcher(value).find()) {
					return getDisplayName() + "の一の位は1~5、十の位は1~9で入力してください。";
				}
			}else{
				if (StringUtils.isNotEmpty(value)) {
					errMsg = EteamCommon.checkNumberRange2(value, -32768L, 32767L, getDisplayName());
					if (null != errMsg) {
						return errMsg;
					}
				}
			}
			break;
		case "int4":
			if (masterId.equals("kian_bangou_bo") && (getName().equals(EteamConst.kianBangou.MASTER_CHK_NUM_RENGE_COLUMN1) || getName().equals(EteamConst.kianBangou.MASTER_CHK_NUM_RENGE_COLUMN2))) {
				errMsg = EteamCommon.checkNumberRange2(value, 1, EteamConst.kianBangou.MAX_VALUE, getDisplayName());
				if (null != errMsg) {
					return errMsg;
				}
			}else {
				if (StringUtils.isNotEmpty(value)) {
					errMsg = EteamCommon.checkNumberRange2(value, -2147483648L, 2147483647L, getDisplayName());
					if (null != errMsg) {
						return errMsg;
					}
				}
			}
			break;
		case "int8":
			if (StringUtils.isNotEmpty(value)) {
				errMsg = EteamCommon.checkNumberRange2(value, -9223372036854775808L, 9223372036854775807L, getDisplayName());
				if (null != errMsg) {
					return errMsg;
				}
			}
			break;
		case "numeric":
			if (StringUtils.isNotEmpty(value) && null == getMaxDecimalLentgh() && !value.matches("^-?[0-9]{1," + getMaxLength() + "}$")) {
				return getDisplayName() + "は" + getMaxLength() + "桁の半角数字で入力してください。";
			}
			else if(StringUtils.isNotEmpty(value) && null != getMaxDecimalLentgh()){
				String[] tmp = value.split("\\.");
				Integer len = getMaxLength()-getMaxDecimalLentgh();
				if(tmp.length > 2  || !tmp[0].matches("^-?[0-9]{1," + len + "}$")){
					return getDisplayName() + "の整数部は" + len + "桁の半角数字で入力してください。";
				}
				if(tmp.length == 2 && !tmp[1].matches("^-?[0-9]{1," + getMaxDecimalLentgh() + "}$")){
					return getDisplayName() + "の小数部は" + getMaxDecimalLentgh() + "桁の半角数字で入力してください。";
				}
			}
			break;
		case "time":
		case "timestamp":
		case "bytea":
		default:
			return getDisplayName() + "の型(" + getType() + ")は非対応形式です。";
		}

		return null;
	}

	/**
	 * 項目が必須かどうか
	 * @return 必須のとき真
	 */
	public boolean isRequired() {
		if (isPk()) {
			return true;
		}
		if (getType().startsWith("varchar")) {
			return false;
		}
		return !isNullable();
	}

	/**
	 * 列名
	 * @return 列名
	 */
	public String getName() {
		return data.get("column_name").toString().toLowerCase();
	}

	/**
	 * 列和名
	 * @return 列和名
	 */
	public String getDisplayName() {
		return (String)data.get("column_comment");
	}

	/**
	 * 最大長
	 * @return 最大長
	 */
	public Integer getMaxLength() {
		Integer val = (Integer)data.get("column_length");
		if (null == val) {
			return val;
		}
		if (val.intValue() > 0) {
			return val;
		}
		return null;
	}

	/**
	 * タイプ
	 * @return タイプ
	 */
	public String getType() {
		return data.get("column_type").toString().toLowerCase();
	}

	/**
	 * NULL可能
	 * @return NULL可
	 */
	public boolean isNullable() {
		return !(boolean)data.get("column_notnull");
	}
	
	/**
	 * 最大小数桁数
	 * @return 最大小数桁数
	 */
	public Integer getMaxDecimalLentgh(){
		Integer val = (Integer)data.get("column_decimal_length");
		if (null == val) {
			return val;
		}
		if (val.intValue() > 0) {
			return val;
		}
		return null;
	}
	
}
