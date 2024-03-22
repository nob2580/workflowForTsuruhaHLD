package eteam.base.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.EteamConst.Sessionkey;
import eteam.gyoumu.user.User;
import eteam.gyoumu.user.UserSessionLogic;
import eteam.gyoumu.userjouhoukanri.UserJouhouKanriLogic;

/**
 * ログイン後のパスワード有効期限切れチェックを行うインターセプターです。
 *
 */
public class EteamPasswordCheckIntercepter extends MethodFilterInterceptor {

	/** ロガー */
	protected static EteamLogger logger = EteamLogger.getLogger(EteamLoginIntercepter.class);

	/**
	 * パスワードの有効期限をチェックし、失効していれば強制パスワード変更画面へ飛ばす。
	 */
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpSession session =  request.getSession();

		// チェック済みかどうか判定
		if (null != session.getAttribute(Sessionkey.PASSWORD_PERIOD_CHECK)) {
			// チェック済みなので、通常処理。
			return invocation.invoke();
		}

		// パスワードの有効期限チェックをする。
		try(EteamConnection connection = EteamConnection.connect()) {
			UserJouhouKanriLogic userLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
			UserSessionLogic usLg = EteamContainer.getComponent(UserSessionLogic.class, connection);
			User user = usLg.userSessionCheck();
			
			if (!userLogic.isValidPasswordPeriod(user.getLoginUserId())) {
				if ("GET".equals(ServletActionContext.getRequest().getMethod())) {
					String originalURL = request.getRequestURI();
					originalURL = originalURL.substring(originalURL.lastIndexOf("/") + 1);
					if (null != request.getQueryString()) {
						originalURL = originalURL + "?"  + request.getQueryString();
					}
					logger.debug("originalURL[" + originalURL + "]");
					session.setAttribute(Sessionkey.ORIGINAL_URL2, originalURL);
				}
				logger.info("パスワード有効期限切れにつき強制パスワード変更画面へ");
				return "forcePasswordChange";
			}
			// 有効期限内の場合、チェック済み情報をセッションに持つ
			session.setAttribute(Sessionkey.PASSWORD_PERIOD_CHECK, user);
		}

		// 有効期限内なので、通常処理。
		return invocation.invoke();
    }
}
