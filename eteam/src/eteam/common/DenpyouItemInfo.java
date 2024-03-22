package eteam.common;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;


/**
 * 伝票項目情報
 * @author takagi_shinsuke
 */
public class DenpyouItemInfo {

	/**
	 * コンストラクタ
	 * @param name 伝票項目名
	 */
	public DenpyouItemInfo(String name) {
		
		_name = name;
	}
	
	/**
	 * コンストラクタ
	 * @param name 伝票項目名
	 * @param list リスト
	 */
	public DenpyouItemInfo(String name, Map<String, String> list) {
		
		_name = name;
		_list = list;
	}
	
	/**
	 * コンストラクタ
	 * @param name 伝票項目名
	 * @param buhinFormat 部品形式
	 */
	public DenpyouItemInfo(String name,BuhinFormat buhinFormat) {
		
		_name = name;
		_buhinFormat = buhinFormat;
	}
	
	
	/**
	 * 伝票項目名
	 */
	protected String _name = "";
	
	/**
	 * 部品形式
	 */
	protected BuhinFormat _buhinFormat = null;
	
	/**
	 * リスト
	 */
	Map<String, String> _list = null;
	
	/** 部品形式 */
	protected enum BuhinFormat{
		/** 文字列 */
		STRING,
		/** 数値 */
		NUMBER,
		/** 日付 */
		DATE,
		/** 金額 */
		MONEY,
		/** 時間 */
		TIME,
		/** 金額(小数点あり) */
		MONEY_DECIMAL
	}
	
	/**
	 * 伝票項目名の取得
	 * @return 伝票項目名
	 */
	public String getName() {
		return _name;
	}

	/**
	 * 伝票項目の表示値の取得
	 * @param value 値
	 * @return 表示値
	 */
	public String getDispVal(String value) {
		if (_list != null && _list.size() > 0) {
			if(_list.containsKey(value)){
				return _list.get(value).toString();
			} else {
				return value;
			}
		} else {
			
			if(Objects.nonNull(_buhinFormat)){
				if (_buhinFormat == BuhinFormat.DATE) {
					return formatDate(toDate(value));
				} else if (_buhinFormat == BuhinFormat.MONEY) {
					return formatMoney(toDecimal(value));
				} else if(_buhinFormat == BuhinFormat.MONEY_DECIMAL) {
					return formatMoneyDecimal(toDecimal(value));
				} else {
					return value;
				}
			} else {
				return value;
			}
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
		} else if (d instanceof Date) {
			return new SimpleDateFormat("yyyy/MM/dd").format(d);
		} else {
			throw new InvalidParameterException("Date以外禁止.d:" + d);
		}
	}
	
	/**
	 * yyyy/MM/ddからDateへ。nullやブランクはnullへ。
	 * @param yyyy_mm_dd 変換前
	 * @return 変換後
	 */
	public Date toDate(String yyyy_mm_dd){
		try {
			return StringUtils.isEmpty(yyyy_mm_dd) ?
				null :
				new Date(new SimpleDateFormat("yyyy-MM-dd").parse(yyyy_mm_dd).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
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
     * BigDecimal型を文字列(カンマ区切り数値と小数点)に変換。
     * @param d 変換前
     * @return 変換後
     */
    public String formatMoneyDecimal(Object d){
        if (null == d) {
            return "";
        } else if (d instanceof BigDecimal) {
            return new DecimalFormat("#,##0.#####").format(d);
        } else {
            throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
        }
    }
	
	/**
	 * カンマ入り半角数字をBigDecimalに。nullやブランクはnullへ。
	 * @param s 変換前
	 * @return 変換後
	 */
	public BigDecimal toDecimal(String s){
		return StringUtils.isEmpty(s) ?
			null :
			new BigDecimal(s.replaceAll(",", ""));
	}
}