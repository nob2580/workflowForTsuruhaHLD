package eteam.gyoumu.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.Env;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Path;
import eteam.common.EteamFileLogic;
import eteam.common.open21.Open21Env;
import eteam.common.select.SystemKanriCategoryLogic;

/**
 * バックアップ運用Logicクラス
 */
public class BackupUnyouLogic extends EteamAbstractLogic{
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(BackupUnyouLogic.class);
	
	/**
	 * ダンプファイル作成処理
	 * @param fileName ダンプファイル名
	 * @param schema スキーマ
	 * @param host ホスト
	 * @param port ポート番号
	 * @param user ユーザー
	 */
	public void exportDump(String fileName, String schema, String host,
			String port, String user) {
		
		//ワークフロー側PostgreSQLのバイナリディレクトリ取得
		String binDir = Open21Env.getBaseDirWF();
		if(!binDir.isEmpty()) {
			if(!Env.isLinux()) {
				binDir = binDir + "\\bin\\";
			}else {
				binDir = binDir + "/bin/";
			}
		}
		
		// コマンドをリスト形式で格納
		List<String> list  = new ArrayList<String>();
		list.add(binDir + "pg_dump");
		list.add("-h");
		list.add(host);
		list.add("-p");
		list.add(port);
		list.add("-E");
		list.add("utf8");
		list.add("-U");
		list.add(user);
		list.add("-n");
		list.add(schema);
		list.add("-f");
		list.add(fileName);
		list.add("-d");
		list.add("eteam");

		log.info("実行コマンド:"+ list);
		
		ProcessBuilder pb = new ProcessBuilder(list);
		
		// コマンド実行
		try {
			// 標準出力と標準エラー出力をマージ
			pb.redirectErrorStream(true);
			// コマンド実行
			Process proc = pb.start();
			String str;
			StringBuffer sb = new StringBuffer();
			BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while((str = brstd.readLine()) != null) {
				sb.append(str);
			}
			// コマンドログ
			if(sb.length() != 0){
				log.debug(sb.toString());
			}
			brstd.close();
			
		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}
	
	/**
	 * テーブルロックを行う
	 * @param schemaName スキーマ名
	 * @param host ホスト名
	 * @param port ポート名
	 * @param user ユーザー名
	 */
	public void tableLock(String schemaName, String host, String port, String user){
		
		//ワークフロー側PostgreSQLのバイナリディレクトリ取得
		String binDir = Open21Env.getBaseDirWF();
		if(!binDir.isEmpty()) {
			if(!Env.isLinux()) {
				binDir = binDir + "\\bin\\";
			}else {
				binDir = binDir + "/bin/";
			}
		}
		
		// コマンドをリスト形式で格納
		List<String> list = new ArrayList<String>();
		list.add(binDir + "psql");
		list.add("-h");
		list.add(host);
		list.add("-U");
		list.add(user);
		list.add("-d");
		list.add("eteam");
		list.add("-p");
		list.add(port);
		list.add("-c");
		list.add("SELECT * FROM " + schemaName + ".event_log FOR UPDATE;");

		log.debug("実行コマンド：" + list);

		ProcessBuilder pb = new ProcessBuilder(list);

		try {
			// 標準出力と標準エラー出力をマージ
			pb.redirectErrorStream(true);
			// コマンド実行
			Process proc = pb.start();
			String str;
			StringBuffer sb = new StringBuffer();
			BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((str = brstd.readLine()) != null) {
				sb.append(str);
			}
			// コマンドログ
			if (sb.length() != 0) {
				log.debug(sb.toString());
			}
			brstd.close();

		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}
	
	/**
	 * スキーマを削除する
	 * @param schemaName スキーマ名	
	 * @param user DBユーザ
	 * @param port ポート
	 * @param host ホスト名
	 */
	public void dropSchema(String schemaName, String host, String port, String user) {

		
		//ワークフロー側PostgreSQLのバイナリディレクトリ取得
		String binDir = Open21Env.getBaseDirWF();
		if(!binDir.isEmpty()) {
			if(!Env.isLinux()) {
				binDir = binDir + "\\bin\\";
			}else {
				binDir = binDir + "/bin/";
			}
		}
		
		// コマンドをリスト形式で格納
		List<String> list = new ArrayList<String>();
		list.add(binDir + "psql");
		list.add("-h");
		list.add(host);
		list.add("-U");
		list.add(user);
		list.add("-d");
		list.add("eteam");
		list.add("-p");
		list.add(port);
		list.add("-c");
		list.add("DROP SCHEMA " + schemaName + " CASCADE;");

		log.info("実行コマンド：" + list);

		ProcessBuilder pb = new ProcessBuilder(list);

		try {
			// 標準出力と標準エラー出力をマージ
			pb.redirectErrorStream(true);
			// コマンド実行
			Process proc = pb.start();
			String str;
			StringBuffer sb = new StringBuffer();
			BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((str = brstd.readLine()) != null) {
				sb.append(str);
			}
			// コマンドログ
			if (sb.length() != 0) {
				log.debug(sb.toString());
			}
			brstd.close();

		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}
	
	/**
	 * ダンプファイルをリストアする
	 * @param fileName ファイル名
	 * @param host ホスト名
	 * @param port ポート番号
	 * @param user DBユーザー
	 */
	public void restoreDump(String fileName, String host, String port, String user) {

		//ワークフロー側PostgreSQLのバイナリディレクトリ取得
		String binDir = Open21Env.getBaseDirWF();
		if(!binDir.isEmpty()) {
			if(!Env.isLinux()) {
				binDir = binDir + "\\bin\\";
			}else {
				binDir = binDir + "/bin/";
			}
		}
		
		// コマンドをリスト形式で格納
		List<String> list = new ArrayList<String>();
		list.add(binDir + "psql");
		list.add("-h");
		list.add(host);
		list.add("-U");
		list.add(user);
		list.add("-d");
		list.add("eteam");
		list.add("-p");
		list.add(port);
		list.add("-f");
		list.add(fileName);

		log.info("実行コマンド:" + list);

		ProcessBuilder pb = new ProcessBuilder(list);

		try {
			// 標準出力と標準エラー出力をマージ
			pb.redirectErrorStream(true);
			// コマンド実行
			Process proc = pb.start();
			String str;
			StringBuffer sb = new StringBuffer();
			BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((str = brstd.readLine()) != null) {
				sb.append(str);
			}

			// コマンドログ
			if (sb.length() != 0) {
				log.debug(sb.toString());
			}
			brstd.close();

		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}

	
	/**
	 * c:\eteam\patch\yymmdd\patchを実行する
	 */
	public void patch() {
		
		SystemKanriCategoryLogic systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		
		//復元後のスキーマヴァージョンは？
		//基本はVERSIONテーブルの情報が正。
		//15.05.21より過去のヴァージョンについては、VERSIONテーブルが存在しないので、設定情報の有無で判定。
		String version = systemLogic.findVersion();
		if (null == version) {
			 if (null == setting.tenantMaxUsersNum()) {
				version = "150309";
			} else if (null == setting.tsuukinteikiShiwakeSakuseiUmu()) {
				version = "150410";
			} else {
				version = "150424";//150430にパッチは存在しないので150424 or 150430をここでは150424としておく
			}
		}

		//c:\eteam\patch\yymmdd\patch.batを実行していく。
		//ただし、復元後スキーマのヴァージョンと最新の差分パッチのみを実行。
		List<File> patchDirs;
		if(!Env.isLinux()) {
			patchDirs = EteamFileLogic.getDirs(Path.PATCH);
		}else {
			patchDirs = EteamFileLogic.getDirs(Path.PATCH_LINUX);
		}
		for (File patchDir : patchDirs) {
			//復元後スキーマヴァージョン < パッチヴァージョン のパッチのみ実行
// if (Integer.parseInt(version) >= Integer.parseInt(patchDir.getName()))
// {
// continue;
// }
			if (version.compareTo(patchDir.getName()) >= 0)
			{
				continue;
			}

			// コマンドをリスト形式で格納
			List<String> list = new ArrayList<String>();
			if(!Env.isLinux()) {
				list.add(patchDir.getPath() + File.separator + "patch.bat");
			}else {
				list.add("sh");
				list.add(patchDir.getPath() + File.separator + "patch.sh");
			}
			list.add(EteamCommon.getContextSchemaName());
			log.info("実行コマンド:" + list);

			ProcessBuilder pb = new ProcessBuilder(list);
			try {
				// 標準出力と標準エラー出力をマージ
				pb.redirectErrorStream(true);
				// コマンド実行
				Process proc = pb.start();
				String str;
				BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((str = brstd.readLine()) != null) {
					log.info(str);
				}
				brstd.close();

			} catch (IOException e) {
				throw new RuntimeException("プロセスが実行できませんでした。", e);
			}
		}
	}

	/**
	 * c:\eteam\bat\bin\createDenpyouList.batを実行する
	 */
	public void denpyouIchiranUpdate() {
		// コマンドをリスト形式で格納
		List<String> list = new ArrayList<String>();
		if(!Env.isLinux()) {
			list.add("c:/eteam/bat/bin/createDenpyouList.bat");
		}else{
			list.add("sh");
			list.add("/var/eteam/bat/bin/createDenpyouList.sh");
		}
		list.add(EteamCommon.getContextSchemaName());
		log.info("実行コマンド:" + list);

		ProcessBuilder pb = new ProcessBuilder(list);
		try {
			// 標準出力と標準エラー出力をマージ
			pb.redirectErrorStream(true);
			// コマンド実行
			Process proc = pb.start();
			String str;
			BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((str = brstd.readLine()) != null) {
				log.info(str);
			}
			brstd.close();

		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}
	
	/**
	 * c:\eteam\bat\bin\createKaniTodokeList.batを実行する
	 */
	public void kaniTodokeIchiranUpdate() {
		// コマンドをリスト形式で格納
		List<String> list = new ArrayList<String>();
		if(!Env.isLinux()) {
			list.add("c:/eteam/bat/bin/createKaniTodokeList.bat");
		}else {
			list.add("sh");
			list.add("/var/eteam/bat/bin/createKaniTodokeList.sh");
		}
		list.add(EteamCommon.getContextSchemaName());
		log.info("実行コマンド:" + list);

		ProcessBuilder pb = new ProcessBuilder(list);
		try {
			// 標準出力と標準エラー出力をマージ
			pb.redirectErrorStream(true);
			// コマンド実行
			Process proc = pb.start();
			String str;
			BufferedReader brstd = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((str = brstd.readLine()) != null) {
				log.info(str);
			}
			brstd.close();

		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}
	
	/**
	 * @param dbVersion ダンプファイルのバージョン情報
	 * @return バージョン比較結果
	 */
	public boolean isGraterThanPgVersion(String dbVersion){
		Properties prop = new Properties();
		try {
			//プログラムのヴァージョン取得
			prop.load(EteamConnection.class.getResourceAsStream("/eteam.properties"));
			String pgVersion = prop.getProperty("version");
			log.debug(pgVersion);
			
			// 比較
			int dbVersion2 = Integer.parseInt(dbVersion);
			int pgVersion2 = Integer.parseInt(pgVersion);
			if(dbVersion2 > pgVersion2) {
				return true;
			}else{
				return false;
			}
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}