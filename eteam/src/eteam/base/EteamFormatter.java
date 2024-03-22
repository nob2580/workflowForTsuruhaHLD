package eteam.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * フォーマッター
 */
public class EteamFormatter {

	/**
	 * 文中URLをリンク返還するための検索用正規表現です。$1にURL全体がマッチします。
	 */
	protected static final String URL_LINK_REGEX = "(?i)(https?://\\w([\\w\\-]*\\w)?(\\.\\w([\\w\\-]*\\w)?)*(:\\d+)?(/(&amp;|&apos;|[\\w\\-_.!~*'():@=+$,]|%[0-9a-fA-F]{2})+(;(&amp;|&apos;|[\\w\\-_.!~*'():@=+$,]|%[0-9a-fA-F]{2})*)?)*/?(\\?(&amp;|&apos;|[\\w\\-_.!~*'():@=+$,;/?]|%[0-9a-fA-F]{2})*)?(#(&amp;|&apos;|[\\w\\-_.!~*'():@=+$,;/?]|%[0-9a-fA-F]{2})*)?)";

	/**
	 * HTML特殊文字エスケープ
	 * @param s 変換前
	 * @return 変換後
	 */
	public static String htmlEscape(String s) {
		if (s == null)
		{
			return "";
		}
	    s = s.replaceAll("&", "&amp;");
	    s = s.replaceAll("<", "&lt;");
	    s = s.replaceAll(">", "&gt;");
	    s = s.replaceAll("\"", "&quot;");
	    s = s.replaceAll("'", "&apos;");
	    s = s.replaceAll(" ", "&nbsp;");
	    return s;
	}

	/**
	 * HTML特殊文字エスケープ + 改行コード<br/>変換
	 * @param s 変換前
	 * @return 変換後
	 */
	public static String htmlEscapeBr(String s){
		s = htmlEscape(s);
		s = s.replaceAll("\r\n", "\n").replaceAll("\n", "<br/>");
		return s;
	}

	/**
	 * HTML特殊文字エスケープ + 改行コード<br/>変換 + URLを<a>タグ変換
	 * @param s 変換前
	 * @return 変換後
	 */
	@Deprecated
	public static String __htmlEscapeBrLink(String s){
		s = htmlEscape(s);
		s = s.replaceAll("\r\n", "\n").replaceAll("\n", "<br/>");
		s = s.replaceAll("(http://|https://)(([\\w\\.\\-\\?&=/:_!,+#;](?!&lt;)(?!&gt;)(?!&quot;)(?!&nbsp;))*[\\w\\.\\-\\?&=/:_!,+#;]?)","<a href='$1$2' target='_blank'>$1$2</a>");
		return s;
	}

	/**
	 * HTML特殊文字エスケープ + 改行コード<br/>変換 + URLを<a>タグ変換
	 * @param s 変換前
	 * @return 変換後
	 */
	public static String htmlEscapeBrLink(String s){
		s = htmlEscape(s);
		s = s.replaceAll("\r\n", "\n").replaceAll("\n", "<br/>");
		s = s.replaceAll(URL_LINK_REGEX, "<a href='$1' target='_blank'>$1</a>");
		return s;
	}

	/**
	 * URLエンコード
	 * @param s 変換前
	 * @return 変換後
	 */
	public static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}