package eteam.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import eteam.base.EteamConnection;
import eteam.base.EteamLogger;

/**
 * env.propertiesから環境情報を返す
 */
public class Env {
	
	/** WF設定定義ファイル */
	protected static final String ETEAM_PROPFILE = "C:/eteam/def/eteamEnv.properties";
	/** WF設定定義ファイル(Linux) */
	protected static final String ETEAM_PROPFILE_LINUX = "/var/eteam/def/eteamEnv.properties";
	

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(Env.class);

	/** プロパティ */
	protected static Properties prop = new Properties();
	
	//プロパティファイル読み込み
	static {
		try {
			prop.load(EteamConnection.class.getResourceAsStream("/env.properties"));
		} catch (IOException e) {
			log.warn("env.properties読込失敗", e);
		}
	}

	/**
	 * UTモードか(OPEN21連携)
	 * @return UTモードならtrue
	 */
	public static boolean open21IsDummy() {
		return "true".equals(prop.get("open21IsDummy"));
	}
	/**
	 * 仕訳チェックはダミーか
	 * @return ダミーならtrue
	 */
	public static boolean checkShiwakeIsDummy(){
		return "true".equals(prop.get("checkShiwakeIsDummy"));
	}
	/**
	 * タイムスタンプはダミーか
	 * @return ダミーならtrue
	 */
	public static boolean timeStampDummy(){
		return "true".equals(prop.get("timeStampDummy"));
	}
	
	/**
	 * 仕訳用一時ファイルを残すか
	 * @return 残すならtrue
	 */
	public static boolean shiwakeTempFileKeep(){
		
		File propFile;
		if(!Env.isLinux()) {
			propFile = new File(ETEAM_PROPFILE);
		}else {
			propFile = new File(ETEAM_PROPFILE_LINUX);
		}
		if(propFile.exists()){
			Properties prp = new Properties();
			try (InputStream iniIS = new FileInputStream(propFile);
				 InputStreamReader isr = new InputStreamReader(iniIS, "SJIS");
				 BufferedReader br = new BufferedReader(isr)){
				prp.load(br);
				String iniCode = prp.getProperty("shiwakeTempFileKeep");
				if("true".equals(iniCode)){
					return true;
				}
			} catch (IOException e) {
				log.warn("eteamEnv.properties読込失敗", e);
			}
		}
		
		return false;
	}
	
	/**
	 * ユーザーライセンス数はダミーか
	 * @return ダミーならtrue
	 */
	public static boolean userLicenseIsDummy(){
		return prop.get("userLicenseIsDummy") != null;
	}
	/**
	 * ダミーのユーザーライセンス数を返す
	 * @return ダミーのユーザーライセンス数
	 */
	public static long dummyUserLicense(){
		String license = (String)prop.get("userLicenseIsDummy");
		try{
			return Long.parseLong(license);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Linux向けモジュールか
	 * @return Linux向けモジュールならtrue
	 */
	public static boolean isLinux(){
		return prop.get("isLinux") != null;
	}

	/**
	 * UT
	 * レジストリを変更しつつ実行する。
	 * @param argv 未使用
	 */
	public static void main(String[] argv) {
		System.out.println(open21IsDummy());
		System.out.println(checkShiwakeIsDummy());
		System.out.println(userLicenseIsDummy());
		System.out.println(dummyUserLicense());
	}
	
	/**
	 * スマート精算で読取情報の吐き出しを行うか
	 * @return 行うならtrue
	 */
	public static boolean smartSeisanSpitOut(){
		
		File propFile;
		if(!Env.isLinux()) {
			propFile = new File(ETEAM_PROPFILE);
		}else {
			propFile = new File(ETEAM_PROPFILE_LINUX);
		}
		if(propFile.exists()){
			Properties prp = new Properties();
			try (InputStream iniIS = new FileInputStream(propFile);
				 InputStreamReader isr = new InputStreamReader(iniIS, "SJIS");
				 BufferedReader br = new BufferedReader(isr)){
				prp.load(br);
				String iniCode = prp.getProperty("smartSeisanSpitOut");
				if("true".equals(iniCode)){
					return true;
				}
			} catch (IOException e) {
				log.warn("eteamEnv.properties読込失敗", e);
			}
		}
		
		return false;
	}
}
