package eteam.base;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import eteam.common.EteamConst;

/**
 * Excel編集クラス
 */
public class EteamXlsFmt {
	
	/**
	 * 日付型を文字列(yyyy/MM/dd)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	public static String formatDate(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof Date) {
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
	public static String formatTime(Object t){
		if (null == t) {
			return "";
		} else if (t instanceof Timestamp) {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(t);
		} else {
			throw new InvalidParameterException("Timestamp以外禁止.t:" + t);
		}
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
	 * BigDecimal型を文字列(カンマ区切り数値と小数点)に変換。(小数点以下の0埋めが2桁)
	 * @param d 変換前
	 * @return 変換後
	 */
	public static String formatMoneyDecimal(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("#,###.00###").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}
	
	/**
	 * BigDecimal型を文字列(カンマ区切り数値と小数点)に変換。(小数点以下の0埋めが2桁、数量用)
	 * @param d 変換前
	 * @return 変換後
	 */
	public static String formatSuuryouDecimal(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("#,##0.##").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}
	
	/**
	 * BigDecimal型を文字列(カンマ区切り数値と小数点)に変換。(小数点以下の0埋めが3桁)
	 * @param d 変換前
	 * @return 変換後
	 */
	public static String formatMoneyDecimalWithPadding3Digit(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("#,##0.###").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}
	
	/**
	 * BigDecimal型を文字列(カンマ区切り無しの数値と小数点)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	public static String formatMoneyDecimalNoComma(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("###0.00###").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}
	
	/**
	 * 時、分のObject型を文字列（HH:mm）に変換。
	 * @param hour 時
	 * @param min 分
	 * @return 変換後
	 */
	public static String formatTimeHHmm(Object hour, Object min){
		if ((hour == null || hour.equals("")) && (min == null || min.equals(""))) {
			return "00:00";
		} else if (min == null || min.equals("")) {
			return hour + ":00";
		} else {
			return hour + ":" + min;
		}
	}
	
	/**
	 * 税率を出力文字列に変換。軽減税率なら税率の前に*を付与。
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分。
	 * @return 変換後
	 */
	public static String formatZeiritsu(BigDecimal zeiritsu, String keigenZeiritsuKbn) {
		StringBuffer sb = new StringBuffer();
		if(EteamConst.keigenZeiritsuKbn.KEIGEN.equals(keigenZeiritsuKbn)) sb.append(EteamConst.keigenZeiritsuKbn.KEIGEN_MARK);
		sb.append(zeiritsu.toString());
		return sb.toString();
	}
}
