package eteam.common;


import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import eteam.base.EteamLogger;


/**
 * 駅すぱあと用Filterクラス
 */
public class SetCharacterEncodingFilter implements Filter {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(SetCharacterEncodingFilter.class);

	/** エンコーディング */
    protected String encoding = null;

    /** フィルターコンフィグ */
    protected FilterConfig filterConfig = null;

    /** エンコーディング無視フラグ */
    protected boolean ignore = true;

    /**
     * パラメータ破棄
     */
    public void destroy() {

        this.encoding = null;
        this.filterConfig = null;

    }

    /**
     * フィルター処理
     */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// Web.xmlに文字エンコードが選択されている場合、指定の文字コードでエンコードする
		if (ignore || (request.getCharacterEncoding() == null)) {
			String enc = selectEncoding(request);

			if (enc != null) {
				request.setCharacterEncoding(enc);
			}

			// ここで最初にゲットパラメータをしてstrutsにエンコーディングさせないようにする
			Enumeration<String> keys = request.getParameterNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				log.info(key + "[" + request.getParameter(key) + "]");
			}

		}
        chain.doFilter(request, response);

    }

    /**
     * 初期化処理
     */
    public void init(FilterConfig config) throws ServletException {

	this.filterConfig = config;
        this.encoding = config.getInitParameter("encoding");
        String value = config.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;

    }
    /**
     * エンコーディング選択
     * @param request リクエスト
     * @return エンコーディング
     */
    protected String selectEncoding(ServletRequest request) {

        return (this.encoding);

    }
}
