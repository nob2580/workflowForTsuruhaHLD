package eteam.base;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * Tomcat起動/終了時処理
 * web.xmlより呼び出される
 */
public class InitializationListener implements ServletContextListener{

	/**
	 * Tomcat起動時処理
	 */
	public void contextInitialized(ServletContextEvent event) {
		
		// Java11対応によりTomcatがlog4j.propertiesを自動認識しないため明示
		PropertyConfigurator.configure(InitializationListener.class.getResourceAsStream("/log4j.properties"));
		
		// ユーザセッションクリア
		try(EteamConnection connection = EteamConnection.connect4NoTrx()){
			connection.update("DELETE FROM public.user_session");
		}
	}

	/**
	 * Tomcatシャットダウン時処理
	 */
	public void contextDestroyed(ServletContextEvent event) {
		//NOP
	}
}