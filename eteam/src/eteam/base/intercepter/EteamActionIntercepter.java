package eteam.base.intercepter;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.exception.EteamDBConnectException;
import eteam.common.EteamCommon;
import eteam.common.EventLogLogic;
import eteam.gyoumu.user.User;

/**
 * アクションクラス全般のインターセプタ。
 * アクション開始終了のログ（トレースログ[テキスト] + リクエストログ[DB])を出力する役割。
 */
public class EteamActionIntercepter extends AbstractInterceptor{
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamActionIntercepter.class);

	@Override
    public String intercept(ActionInvocation actioninvocation) throws Exception {
		try(EteamConnection connection = EteamConnection.connect(); ) { //インターセプターでのSQL発行はロックに注意。
			//諸々イベントの情報
			Map<String, Object> map = ActionContext.getContext().getSession();
			User user = (User)map.get("user");
			Class<? extends Object> actionClass = actioninvocation.getAction().getClass();
			String actionName = actionClass.getSimpleName();
			String gamenId = EteamCommon.makeGamenId(actionClass);
			String eventId = actioninvocation.getProxy().getMethod();
			String userId = (null == user) ? "" : user.getTourokuOrKoushinUserId();
			String process = actionName + "#" + eventId;
	
			//部品new
			EventLogLogic eventLogic = EteamContainer.getComponent(EventLogLogic.class, connection);
	
			//開始ログ
			log.info(process + "　開始");
	
			//開始イベントログ
			long serialNo = eventLogic.startLog(userId, gamenId, eventId);
			connection.commit();

	    	//実行
			String result = actioninvocation.invoke();
			//各Action内の例外はinvoke()の中で閉じていて、ここには上がってこない。
			//ここまで終わっていれば、画面表示自体はされる。終了イベントログ出力とヘルプ情報紐付けは行われないが。
			
			//終了イベントログ
	    	eventLogic.endLog(serialNo, result);
			connection.commit();
	
			//終了ログ
			log.info(process + "　終了[" + result + "]");
			if (log.isDebugEnabled()) {
				log.debug("Actionクラスダンプ[" + actioninvocation.getAction() + "]");
			}
			if ("systemError".equals(result)) {
				log.error("systemErrorのActionクラスダンプ[" + actioninvocation.getAction() + "]");
			}
	
			//基本はActionクラスのイベントメソッドの戻り値が返る。
			//ただし、インターセプターかアクションが例外を投げた場合、struts.xmlで定義した例外に対応するresultが返る。
			//(EteamAccessDeniedException が発生したら "accessDeniedError" が返る、等)
			return result;
		} catch (EteamDBConnectException e) {
			//DB接続できない（waittimeout）場合だけは理由が分かっているので個別のエラー画面に飛ばす。
			log.error("インターセプターにてDB接続エラー", e);
			return "dbConnectError";
		} catch (Throwable e) {
			//個別Actionの例外はinvokeの中で閉じている。
			//ここに到達するのは基盤制御側でのエラーが発生した場合。500(Server Internal Error)が帰る。
			log.error("インターセプターにてエラー", e);
			throw e;
		}
	}
}
