package eteam.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

import eteam.common.EteamCommon;

/** 拡張ロガー */
public class EteamLogger {

	/** ロガー */
	protected Logger log;
	
	/** デフォルトファイルパス */
	protected String eteamPath = "";
	
	/** デフォルトファイルパス */
	protected String eteamErrPath = "";

	/** ロガーの設定
	 * @param log ロガー
	 */
	public void setLog(Logger log) {
		this.log = log;
	}

	/** ロガーの取得
	 * @param clazz クラス
	 * @return 拡張ロガー
	 */
	public static EteamLogger getLogger(@SuppressWarnings("rawtypes") Class clazz)
	{
		EteamLogger eLog = new EteamLogger();
		eLog.setLog(Logger.getLogger(clazz));
		return eLog;
	}
	
	/** 情報ログの出力
	 * @param message メッセージ
	 */
	public void info(String message)
	{
		setFile(1);
		setFile(2);
		log.info(message);
	}

	/** デバッグログの出力
	 * @param message メッセージ
	 */
	public void debug(String message)
	{
		setFile(1);
		setFile(2);
		log.debug(message);
	}

	/** 警告ログの出力
	 * @param message メッセージ
	 */
	public void warn(String message)
	{
		setFile(1);
		setFile(2);
		log.warn(message);
	}

	/** 警告ログの出力
	 * @param message メッセージ
	 * @param e 例外
	 */
	public void warn(String message, Throwable e)
	{
		setFile(1);
		setFile(2);
		log.warn(message, e);
	}

	/** エラーログの出力
	 * @param message メッセージ
	 */
	public void error(String message)
	{
		setFile(1);
		setFile(2);
		log.error(message);
	}

	/** エラーログの出力
	 * @param t エラー
	 */
	public void error(Throwable t)
	{
		setFile(1);
		setFile(2);
		log.error(t, t);
	}
	
	/** エラーログの出力
	 * @param message メッセージ
	 * @param t エラー
	 */
	public void error(String message, Throwable t)
	{
		setFile(1);
		setFile(2);
		log.error(message, t);
	}

	/** デバッグの使用有無
	 * @return デバッグの使用有無
	 */
	public boolean isDebugEnabled () {
		return log.isDebugEnabled();
	}

	/** ファイル出力先の設定
	 * @param event イベント(1:情報、2：エラー)
	 */
	protected void setFile(int event) {
		
		try {
			
			DailyRollingFileAppender appender = null;

			switch (event){
			case 1:
				appender = (DailyRollingFileAppender)log.getParent().getParent().getAppender("fout");
				break;
			case 2:
				appender = (DailyRollingFileAppender)log.getParent().getParent().getAppender("foute");
				break;
			}

			if(appender == null) {
				return;
			}

			// 日付パターン
			String datePattern = appender.getDatePattern().replace("'.'", "");
			String date = new SimpleDateFormat(datePattern).format(new Date());

			String file = "";
			String filePath = appender.getFile();
			String dirPath = "";
			String defaultPath = "";
			File objFile;
			
			if (eteamPath.equals("") || eteamErrPath.equals("")) {
				
				// ファイル直下のディレクトリパスの取得
				int lastIndex = filePath.lastIndexOf(File.separator);
				if (lastIndex < 0) {
					lastIndex = filePath.lastIndexOf("/");
					if (lastIndex < 0) {
						return;
					}
					dirPath = filePath.substring(0, lastIndex);
				} else {
					dirPath = filePath.substring(0, lastIndex);
				}
				
				objFile = new File(dirPath);

				switch (event){
				case 1:
					eteamPath = objFile.getPath() + File.separator + "eteam.log";
					break;
				case 2:
					eteamErrPath = objFile.getPath() + File.separator + "eteam_error.log";
					break;
				}
			}
			
			switch (event){
			case 1:
				defaultPath = eteamPath;
				break;
			case 2:
				defaultPath = eteamErrPath;
				break;
			}
			
			// 起動時、まっさらなlogファイルが作られる→後で過去日分の名前変更される時、日付拡張子が出てくるという仕様なので対策
			defaultPath = defaultPath.replace(".log", "") + "_" + date + ".log";
			
			try {

				String schema = "";

				if (null != ActionContext.getContext()) {
					schema = EteamCommon.getContextSchemaName();
				} else {
					schema = EteamCommon.getBatSchemaName();
				}
				
				if (schema != null && schema.length() > 0) {
					objFile = new File(defaultPath);
					String parentPath = objFile.getParent();
					String fileName = objFile.getName();
					String fileExt = getExtent(fileName);
					file = parentPath + File.separator + fileName.replace(fileExt, "").replace("_" + date, "") + "_" + schema + "_" + date + fileExt;
				} else {
					file = defaultPath;
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				file = defaultPath;
			}

			if (!filePath.equals(file)) {
				appender.setFile(file);
				appender.activateOptions();
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/** ファイル拡張子の取得
	 * @param fileName ファイル名
	 * @return 拡張子
	 */
	protected static String getExtent(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point);
	    }
	    return fileName;
	}
}