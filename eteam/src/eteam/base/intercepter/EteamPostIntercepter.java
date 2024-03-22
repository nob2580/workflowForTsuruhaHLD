package eteam.base.intercepter;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import eteam.base.EteamLogger;
import eteam.base.exception.EteamIllegalRequestException;

/**
 * POSTメソッドを前提にしたイベントメソッドのインターセプター。
 * メソッドがPOSTでなければ不正アクセスエラーにする役割。
 */
@SuppressWarnings("serial")
public class EteamPostIntercepter extends AbstractInterceptor{

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamPostIntercepter.class);

	/**
	 * POSTメソッドでなければエラーとする。
	 */
	@Override
    public String intercept(ActionInvocation invocation) throws Exception {
		String methodStr = ServletActionContext.getRequest().getMethod().toUpperCase();
		if (! "POST".equals(methodStr)) {
			log.error("POST メソッドではないので不正アクセス。");
			throw new EteamIllegalRequestException();
		}
		return invocation.invoke();
    }
}
