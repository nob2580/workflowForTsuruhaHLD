package eteam.base.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.GamenKengenSeigyoLogic;
import eteam.gyoumu.help.HelpLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeAction;
import eteam.gyoumu.user.User;
import eteam.gyoumu.user.UserSessionLogic;

/**
 * ログインが必要なイベントのインターセプター。
 * セッションのユーザー情報がなければ、ログイン画面に飛ばす役割。
 * ログイン画面に飛ばす時、ログイン後復帰用URL(リクエスト中)をセッションに退避する。
 */
public class EteamLoginIntercepter extends AbstractInterceptor{

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamLoginIntercepter.class);

	/**
	 * 未ログインならば、セッションに現在リクエストを入れてログイン画面へ。
	 */
	@Override
    public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpSession session =  request.getSession();
		String gamenId = EteamCommon.makeGamenId(invocation.getAction().getClass());

		try(EteamConnection connection = EteamConnection.connect()) {
			UserSessionLogic sesLg = EteamContainer.getComponent(UserSessionLogic.class, connection);
			
			User user = sesLg.userSessionCheck();
			connection.commit();//セッション時間延長の為コミット
			session.setAttribute("user", user);
			EteamAbstractAction action = (EteamAbstractAction)invocation.getAction();
			action.setUser(user);
		
			//未ログインのようなので、強制ログインさせる。
			if (null == user) {
				if ("GET".equals(ServletActionContext.getRequest().getMethod())) {
					String originalURL = request.getRequestURI();
					originalURL = originalURL.substring(originalURL.lastIndexOf("/") + 1);
					
					session.removeAttribute(Sessionkey.ORIGINAL_URL);
					if(!isMeisaAction(originalURL)) {
						if (null != request.getQueryString()) {
							originalURL = originalURL + "?"  + request.getQueryString();
						}
						log.debug("originalURL[" + originalURL + "]");
						session.setAttribute(Sessionkey.ORIGINAL_URL, originalURL);
					}
					
				}
				log.info("未ログインにつきログイン画面へ");
				return "forceLogin";
			}
		
			//以下ログイン済の場合
			//画面に対するアクセス権限のチェック
			GamenKengenSeigyoLogic accessLogic = EteamContainer.getComponent(GamenKengenSeigyoLogic.class, connection);
			if(! accessLogic.judgeAccess(gamenId, user)) {
				throw new EteamAccessDeniedException();
			}
	    	//ヘルプ情報有無検索
			//キーは通常は単なる画面ID、ユーザー定義届書の場合のみ画面ID + 伝票区分
			HelpLogic helpLogic = EteamContainer.getComponent(HelpLogic.class, connection);
			String helpGamenId = gamenId;
			if (EteamCommon.makeGamenId(KaniTodokeAction.class).equals(gamenId)) helpGamenId += ((KaniTodokeAction)invocation.getAction()).getDenpyouKbn();
	    	if (helpLogic.judgeHelpDisplay(user, helpGamenId)) {
	    		ServletActionContext.getRequest().setAttribute("helpGamenId", helpGamenId);
	    	}
	    	
		}

		//ログイン済のようなので、普通にイベント処理を行う。
		return invocation.invoke();
    }
	
	/**
	 * 明細子画面のActionかどうかを判定
	 * @param originalUrl オリジナルURL
	 * @return boolean
	 */
	private boolean isMeisaAction(String originalUrl) {
		
		switch(originalUrl) {
			case "bumon_sentaku":
			case "gyoumu_role_sentaku":
			case "bumon_role_sentaku":
			case "gougi_busho_sentaku":
			case "user_sentaku":
			case "user_sentaku_kensaku":
			case "kian_bangou_sentaku":
			case "norikae_annai_kensaku":
			case "teiki_kukan_get":
			case "station_sentaku":
			case "route_sentaku":
			case "kanren_denpyou_sentaku_init":
			case "kanren_denpyou_sentaku_kensaku":
			case "kian_check_init":
			case "yosan_check_init":
			case "ryohi_seisan_kakoMeisai_sentaku_kensaku":
			case "ic_card_rireki_sentaku":
			case "ic_card_rireki_sentaku_jyogaichange":
			case "houjin_card_siyou_ichiran":
			case "torihikisaki_sentaku":
			case "torihikisaki_sentaku_kensaku":
			case "torihiki_sentaku":
			case "kanjyou_kamoku_sentaku":
			case "kanjyou_kamoku_edaban_sentaku":
			case "universal_sentaku":
			case "header_sentaku":
			case "karibarai_anken_sentaku":
			case "karibarai_anken_sentaku_kensaku":
			case "project_sentaku":
			case "segment_sentaku":
			case "heishu_sentaku":
			case "ginkou_sentaku":
			case "ginkou_shiten_sentaku":
			case "kani_todoke_text_add":
			case "kani_todoke_radio_add":
			case "kani_todoke_textarea_add":
			case "kani_todoke_pulldown_add":
			case "kani_todoke_checkbox_add":
			case "kani_todoke_master_item_add":
			case "denpyou_ichiran_item_init":
			case "error_log_hyouji_kogamen":
			case "kian_bangou_bo_saiban_init":
			case "kian_bangou_syousai_kakunin":
			case "kian_tsuika":
			case "kian_tsuika_kensaku":
				return true;
		default:
			break;
		}
		
		return false;
	}
}
