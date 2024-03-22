package eteam.gyoumu.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.FileName;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamSettingInfo;
import eteam.common.open21.Open21Env;
import eteam.common.select.SystemKanriCategoryLogic;

/**
 * 日中データ連携バッチ処理のスレッドクラスです。
 */
public class BackupUnyouThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(BackupUnyouThread.class);

	/** モード(0:作成 1:復元) */
	int mode;
	/** チェックボックスの選択リスト */
	String[] sentakuList;
	/** スキーマ名 */
	String schema;
	/** コメント */
	String comment;
	
	//＜部品＞
	/** バックアップ運用Logicクラス */
	BackupUnyouLogic backupLogic;
	/** システム管理　SELECT */
	SystemKanriCategoryLogic systemLogic;
	/** コネクションクラス */
	EteamConnection connection;
	
	//＜画面入力以外＞
	/** スキーマ名 */
	String schemaName;
	/** ファイルディレクトリ */
	String fileDir;

    /**
     * コンストラクタ
     * @param mode        モード(0:作成 1:復元)
     * @param sentakuList チェックボックスの選択リスト(復元時)
     * @param schema      スキーマ名
     * @param comment     コメント
     */
	public BackupUnyouThread(int mode, String[] sentakuList, String schema, String comment) {
		this.mode = mode;
		this.sentakuList = sentakuList;
		this.schema = schema;
		this.comment = comment;
    }

    /**
     * 各バッチ処理を実行する。
     */
    public void run() {
    	try {
    		
    		//画面遷移を優先させるため5秒ほどウェイト
    		Thread.sleep(5000);
    		
    		//スキーマを起動元のコンテキストから渡されたものに
    		Map<String, String> threadMap = EteamThreadMap.get();
    		threadMap.put(SYSTEM_PROP.SCHEMA, schema);
			switch(mode){
			case 0:
				sakusei();
				break;
			case 1:
				fukugen();
				break;
			default:
				throw new IllegalArgumentException("パラメータに動作モードが指定されていません。");
			}
	
		} catch (Throwable e) {
			log.error("エラー発生", e);
		}
    }
    
	/**
	 * バックアップ作成処理
	 */
	public void sakusei() {

		// ファイルのディレクトリを、設定情報より取得
		fileDir = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.DBDUMP_URL);
		// ログインユーザの使用しているスキーマ名を取得
		schemaName = EteamCommon.getContextSchemaName();
		
		connection = EteamConnection.connect();
		try{
			backupLogic = EteamContainer.getComponent(BackupUnyouLogic.class, connection);
			systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			// 現在時刻をファイル名用にフォーマット
			String fileDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			// バージョン情報を取得
			String version = systemLogic.findVersion();
			// ファイル名を生成
			String fileName = fileDir + File.separator + schemaName  + "_" + version + "_" + fileDate + ".dump";
			//バックアップコメントの追加
			addBackupComment(fileDate, version);
			// 接続DB情報を取得
			Map<String, String> dbinfo = getDbInfo();
			// バックアップ処理実行
			backupLogic.exportDump(fileName, schemaName, dbinfo.get("host"), dbinfo.get("port"), dbinfo.get("user"));
			connection.commit();
		} finally {
			connection.close();
		}
		return;
	}
	
	/**
	 * バックアップ復元処理
	 */
	public void fukugen() {

		// ファイルのディレクトリを、設定情報より取得
		fileDir = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.DBDUMP_URL);
		// ログインユーザの使用しているスキーマ名を取得
		schemaName = EteamCommon.getContextSchemaName();
		
		connection = EteamConnection.connect();
		try{
			backupLogic = EteamContainer.getComponent(BackupUnyouLogic.class, connection);
			
			// 選択ファイル
			String fileName = fileDir + File.separator + sentakuList[0] + ".dump";
			// 接続DB情報を取得
			Map<String, String> dbinfo = getDbInfo();
			// 1.テーブルロック
			backupLogic.tableLock(schemaName,dbinfo.get("host"),dbinfo.get("port"),dbinfo.get("user"));
			// 2.スキーマごと全部削除
			backupLogic.dropSchema(schemaName,dbinfo.get("host"),dbinfo.get("port"),dbinfo.get("user"));
			// 3.リストア
			backupLogic.restoreDump(fileName,  dbinfo.get("host"), dbinfo.get("port"), dbinfo.get("user"));
			// 4.patch適用
			backupLogic.patch();
			// 5.伝票一覧再作成
			backupLogic.denpyouIchiranUpdate();
			// 6.ユーザー定義届書一覧再作成
			backupLogic.kaniTodokeIchiranUpdate();
			
			connection.commit();
		} finally { 
			connection.close();
		}
		return;
	}
	
	/**
	 * プロパティファイルからDBの情報を取得し返却する。
	 * @return map DB接続情報
	 */
	protected Map<String, String> getDbInfo() {

		Map<String, String> dbinfo = new HashMap<String, String>();

		//batconnect.propertiesは削除するため、接続情報は一時的にソース直記載
		String url = "jdbc:postgresql://localhost:" + Open21Env.getPortWF() + "/eteam"; //デフォルトポートは5432
		String user = "eteam";

		// URLよりホスト名、ポートを抽出する
		String[] spUrl = url.split(":");
		String host = spUrl[2].replaceAll("//", "");
		String[] port = spUrl[3].split("/");

		dbinfo.put("user", user); // ユーザー
		dbinfo.put("host", host); // ホスト名
		dbinfo.put("port", port[0]); // ポート番号

		return dbinfo;
	}
	
	/**
	 * バックアップコメントをバックアップコメントファイルに追加する。
	 * @param fileDate ファイル名
	 * @param version ダンプファイルのバージョン情報
	 */
	protected void addBackupComment(String fileDate, String version) {
		try{
			PrintWriter pw;
			FileOutputStream fos;
			fos = new FileOutputStream(fileDir + File.separator + FileName.BACKUP_COMMENT_FILENAME,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, Encoding.UTF8);
			pw = new PrintWriter(osw);
			pw.write(schemaName + "_" + version + "_" + fileDate + ".dump ");
			pw.println(comment);
			pw.close();
		} catch (IOException e) {
			throw new RuntimeException("バックアップコメントの書き込みエラー",e);
		}
	}

}
