package eteam.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

/**
 * サーブレット関連
 */
public class ServletUtil {

	/**
	 * HTTPリクエスト 取得
	 * @return HTTPリクエスト
	 */
	public static HttpServletRequest getRequest() {
		return (ActionContext.getContext() == null) ? null : ServletActionContext.getRequest();
	}
	
	/**
	 * JSESSION IDの取得
	 * @return JSESSION ID
	 */
	public static String getJSessionId() {
		HttpServletRequest request = getRequest();
		return request == null ? null : request.getSession().getId();
	}

// /**
//  * セッション内のスキーマの箱(eteam/kyoten共通)を取得
//  * @return スキーマ毎の箱(eteam)
//  */
// public static GMap getSession(){
// HttpServletRequest request =  getRequest();
// HttpSession session =  request.getSession();
//        String key = "schema=" + EteamCommon.getContextSchemaName();
//        GMap map = (GMap)session.getAttribute(key);
//        if(map == null) {
//        	map = new GMap();
//        	session.setAttribute(key, map);
//        }
//        return map;
// }
//
// /**
//  * リクエストURL 取得
//  * @return リクエストURL
//  */
// public static String getRequestUrl() {
// HttpServletRequest request =  getRequest();
// // 「/」形式
// String originalURL = request.getRequestURI();
// // 「http://host/eteam/appl/hoge」形式
//// String originalURL = request.getRequestURL().toString();
// originalURL = originalURL.substring(originalURL.lastIndexOf("/") + 1);
// if (null != request.getQueryString()) {
// originalURL = originalURL + "?"  + request.getQueryString();
// }
// if (null != request.getQueryString()) {
// originalURL = originalURL + "?"  + request.getQueryString();
// }
// return originalURL;
// }
}
