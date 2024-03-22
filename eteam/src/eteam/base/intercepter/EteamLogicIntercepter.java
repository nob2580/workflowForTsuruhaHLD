package eteam.base.intercepter;

import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

import eteam.base.EteamLogger;

/**
 * ロジッククラス全般のインターセプタ。
 * メソッドの入出力を自動ログ出力する役割。
 */
public class EteamLogicIntercepter extends AbstractInterceptor {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamLogicIntercepter.class);

	/**
	 * ロジック実行前後のログ出力インターセプト
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String process = getTargetClass(invocation).getSimpleName() + "#" + invocation.getMethod().getName();
		if (log.isDebugEnabled()) {
	        if (log.isDebugEnabled()) log.debug(process + "　開始。引数ダンプ[" + makeParameterDump(invocation) + "]");
		}

		//実行
		Object ret = null;
		try {
			ret = invocation.proceed();
		} catch (Throwable e) {
	        log.error(process + "　エラー発生。引数ダンプ[" + makeParameterDump(invocation) + "]");
	        throw e;
		}

        if (log.isDebugEnabled()) {
        	if (ret != null && ret instanceof List<?>) {
        		log.debug(process + "　終了。戻り値ダンプ[" + ((List<?>)ret).size() + "]");
        	} else if (ret != null && ret instanceof Map<?,?>) {
            	log.debug(process + "　終了。戻り値ダンプ[" + ((Map<?,?>)ret).size() + "]");
        	} else {
        		log.debug(process + "　終了。戻り値ダンプ[" + ret + "]");
        	}
		}
        return ret;
    }

	/**
	 * 引数ダンプ[val1][val2]...を作成
	 * @param invocation メソッド
	 * @return 引数ダンプ
	 */
	protected String makeParameterDump(MethodInvocation invocation) {
		StringBuffer buf = new StringBuffer();
        Object[] params = invocation.getArguments();
        for (Object param : params) {
        	appendParam(buf, param);
        }
        return buf.toString();
	}

	/**
	 * 再帰的に[val1][val2]のように文字列追加していく。
	 * @param buf 文字列
	 * @param param []内のオブジェクト
	 */
	protected void appendParam(StringBuffer buf, Object param) {
		buf.append("[");
    	if (param instanceof Object[]) {
            for (Object param2 : (Object[])param) {
            	appendParam(buf, param2);
            }
    	} else {
    		buf.append(param);
    	}
    	buf.append("]");
	}
}