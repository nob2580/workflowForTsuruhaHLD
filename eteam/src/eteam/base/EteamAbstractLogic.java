package eteam.base;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;

import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamSettingInfo;
import eteam.gyoumu.user.User;

/**
 * ロジッククラスの親
 */
public class EteamAbstractLogic {

	/** 初期化 */
	public EteamAbstractLogic()
	{
		setting = new EteamSettingInfo();
	}
	
	/** コネクション */
	protected EteamConnection connection;

	/** 設定情報 */
	protected EteamSettingInfo setting;
	
	/**
	 * コネクション渡し
	 * @param connection コネクション
	 */
	public void init (@SuppressWarnings("hiding") EteamConnection connection) {
		this.connection = connection;
	}

	/**
	 * セッション・ユーザー情報取得
	 * バッチで使用した時はnullを返すので要注意
	 * @return セッション・ユーザー情報
	 */
	@Deprecated
	protected User getUser() {
		return (ActionContext.getContext() != null) ? (User)ActionContext.getContext().getSession().get(Sessionkey.USER) : null;
	}
	
	
	/**
	 * trueならbufferにAppendする。主にSQL文用。
	 * @param condition 条件
	 * @param buffer バッファー
	 * @param additionalText 追加テキスト
	 * @param params パラメーター（追加したくない場合はnull）
	 * @param additionalParams 追加パラメーター
	 */
	protected void appendIf(boolean condition, StringBuffer buffer, String additionalText, List<Object> params, Object... additionalParams) 
	{
		if(condition && buffer != null && !isEmpty(additionalText))
		{
			buffer.append(additionalText);
			if(params == null)
			{
				return;
			}
			for(var param : additionalParams)
			{
				params.add(param);
			}
		}
	}
	
	/**
	 * 空でないならbufferにAppendする。主にSQL用。
	 * @param buffer バッファー
	 * @param additionalText 追加テキスト
	 * @param params パラメーター（追加したくない場合はnull）
	 * @param additionalParam 追加パラメーター（簡単のため、一つのケースのみをまずは想定）
	 */
	protected void appendIfNotEmpty(StringBuffer buffer, String additionalText, List<Object> params, Object additionalParam)
	{
		this.appendIf(!isEmpty(additionalParam), buffer, additionalText, params, additionalParam);
	}
    
	/**
	 * 空でないならbufferにAppendする。LIKEを使う場合専用。
	 * @param buffer バッファー
	 * @param columnName カラム名（必要ならテーブル名込みで指定）
	 * @param params パラメーター（追加したくない場合はnull）
	 * @param additionalParam 追加パラメーター（簡単のため、一つのケースのみをまずは想定）
	 */
	protected void appendIfNotEmptyForLikeQuery(StringBuffer buffer, String columnName, List<Object> params, Object additionalParam)
	{
		this.appendIf(!isEmpty(additionalParam), buffer, "  AND unify_kana_width(" + columnName + ") LIKE unify_kana_width(?)", params, "%" + additionalParam + "%");
	}

    /**
     * 日付範囲指定用where文生成
     * @param start 開始
     * @param end 終了
     * @param buffer バッファー
     * @param columnName DBのカラム名（必要ならテーブル名も込みで）
     * @param params パラメーターリスト
     */
    protected void appendWhereDate(Date start, Date end, StringBuffer buffer, String columnName, List<Object> params)
    {
        this.appendIfNotEmpty(buffer, "  AND " + columnName + " >= ?", params, start);
        this.appendIfNotEmpty(buffer, "  AND " + columnName + " < (CAST(? AS DATE) + 1)", params, end);
    }
	
	/**
	 * ヌルはブランクへ、それ以外ならそのまま。
	 * @param s 変換前
	 * @return 変換後
	 */
	protected static String n2b(Object s) {
		return (null == s) ? "" : s.toString();
	}
	
	/**
	 * ヌルはブランクへ、それ以外ならそのまま。
	 * @param s 変換前
	 * @return 変換後
	 */
	protected static boolean isEmpty(Object s) {
		return (null == s) || s.toString().isEmpty();
	}
	
	/**
	 * カンマ入り半角数字をBigDecimalに。nullやブランクはnullへ。
	 * @param s 変換前
	 * @return 変換後
	 */
	protected BigDecimal toDecimal(String s){
		return (isEmpty(s)) ?
			null :
			new BigDecimal(s.replaceAll(",", ""));
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
}
