package eteam.base.intercepter;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import eteam.base.EteamLogger;
import eteam.base.exception.EteamIllegalRequestException;

/**
 * GETメソッドを前提にしたイベントメソッドのインターセプター。
 * メソッドがGETでなければ不正アクセスエラーにする役割。
 */
public class EteamGetIntercepter extends AbstractInterceptor{

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamGetIntercepter.class);

	/**
	 * GETメソッドでなければエラーとする。
	 */
	@Override
    public String intercept(ActionInvocation invocation) throws Exception {
		String methodStr = ServletActionContext.getRequest().getMethod().toUpperCase();
		if (! "GET".equals(methodStr)) {
			log.error("GET メソッドではないので不正アクセス。");
			throw new EteamIllegalRequestException();
		}
		return invocation.invoke();
    }
}
