package eteam.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import eteam.base.EteamLogger;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;

/**
 * レジストリ操作を行うクラス
 */
public class RegAccess {

	/**
	 * 予算執行管理オプション定義
	 */
	public static final class YOSAN_SHIKKOU_OP {
		/** 予算執行管理なし("") */
		public static final String NO_OPTION = "";
		/** 予算執行管理Aあり(A) */
		public static final String A_OPTION = "A";
		/** 予算執行管理Bあり(B) */
		public static final String B_OPTION = "B";
	}
	

	/**
	 * レジストリキーとレジストリ名を持たせる
	 * 各種定数[0]がレジストリキー、各種定数[1]がレジストリ名
	 */
	public static class REG_KEY_NAME {

		//環境判定用
		/** ユーザーライセンス数(購入分のライセンス数)　※全テナント共通 */
		public static final String[] SIAS_CHECK = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
			"WFSerial"
		};
		/** ユーザーライセンス数(購入分のライセンス数)　※全テナント共通 */
		public static final String[] DE3_CHECK = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\ICS Open21 de3",
			"WFSerial"
		};

		//de3/SIAS 共通
		/** InstallDate */
		public static final String[] INSTALL_DATE = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion",
			"InstallDate"
		};
		/** BIOSReleaseDate(MM/DD/YYYY) */
		public static final String[] BIOS_RELEASE_DATE = {
			"HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\SystemInformation",
			"BIOSReleaseDate"
		};

		//de3/SIASで異なる. SIAS環境ならレジストリキーをde3→SIASに読み替え
		/** ユーザーライセンス数(購入分のライセンス数)　※全テナント共通 */
		public static final String[] USER_LICENSE_NUM = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\ICS Open21 de3",
			"WFSerial"
		};
		/** 駅すぱあとWEBサービスのキー(契約した接続キー)　※テナント別 */
		public static final String[] EKISPERT_WEBSERVICE_KEY = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\ICS Open21 de3\\", //後ろにスキーマ名を付加すること
			"ESSerial"
		};
		
		/** de3財務配布メディアインストーラ SVIP */
		public static final String[] DE3_SVIP = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\（株）ＩＣＳパートナーズ\\ICS Open21 de3",
			"SVIP"
		};
		
		/** de3財務配布メディアインストーラ MYIP */
		public static final String[] DE3_MYIP = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\（株）ＩＣＳパートナーズ\\ICS Open21 de3",
			"MYIP"
		};
		
		/** de3コネクタークライアント＆WFインストーラ SVIP */
		public static final String[] DE3_CLIENT_SVIP = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\（株）ＩＣＳパートナーズ\\ICS Open21 de3 Connector Client and WF",
			"SVIP"
		};
		
		/** de3コネクタークライアント＆WFインストーラ SVIP ※通常のインストーラでは作成されない。TSR環境用*/
		public static final String[] DE3_CLIENT_SUB_SVIP = {
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\（株）ＩＣＳパートナーズ\\ICS Open21 de3 Connector Client",
			"SVIP"
		};
		
		//SIASのみ
		/**
		 * 構成タイプ
		 * 1：スタンドアロン※、3：DBサーバー、5：DB+RDSサーバー、2：C/Sクライアント※、4：RDSサーバー　　　※：SIAS未リリースの構成。 
		 * 1,3,5 → DBサーバー=localhostに接続
		 * 2,4 → DBサーバー=当レジストリのIPアドレスに接続
		 */
		public static final String[] SIAS_TYPE = {
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
				"TYPE"
		};
		/**
		 * DBサーバーのIPアドレス
		 */
		public static final String[] SIAS_SVIP = {
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
				"SVIP"
		};
		/**
		 * DBサーバーのポート番号
		 * 1434 → ポート番号指定なし
		 * 1434以外 → 当レジストリのポートに接続
		 */
		public static final String[] SIAS_SVPORT = {
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
				"SVPORT"
		};
		/**
		 * オプション機能有効判定用
		 */
		public static final String[] SIAS_WFSET = {
				"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
				"WFSet"
		};
		
		/** 経費精算(WF本体)有効判定用 */
		public static final String[] SIAS_WFPRG ={
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
			"WFPrg"
		};
		
		/**
		 * SIASの会社DB利用種別
		 * 2：SQLServer、3：postgreSQL
		 * 該当値存在しなければ旧バージョンSIASとみなし、SQLServerとして扱う
		 */
		public static final String[] SIAS_DBENGINE ={
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\（株）ＩＣＳパートナーズ\\Prj312",
			"DBEngine"
		};
		
		/**
		 * ワークフロー側PostgreSQLの利用ポート
		 */
		public static final String[] WF_PORT ={
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\PostgreSQL\\Services\\postgresql-x64-11",
			"Port"
		};
		/**
		 * ワークフロー側PostgreSQLのベースディレクトリ
		 */
		public static final String[] WF_BASEDIR ={
			"HKEY_LOCAL_MACHINE\\SOFTWARE\\PostgreSQL\\Installations\\postgresql-x64-11",
			"Base Directory"
		};
	}

	/**
	 * レジストリの値を読み込む
	 * 
	 * @param key 0:キー、1:名前
	 * @return 値
	 * @exception RuntimeException レジストリが取得できない
	 */
	public static String readRegistory(String[] key) {
		return readRegistory(key[0], key[1]);
	}
	/**
	 * レジストリの値を読み込む
	 * 
	 * @param key キー
	 * @param name 名前
	 * @return 値
	 * @exception RuntimeException レジストリが取得できない
	 */
	public static String readRegistory(String key, String name) {
		//当メソッド呼び元ではDE3/SIASのレジストリ分けを意識していない(DE3用キーで定数定義している)ので、ここでキーを読み替えてあげる
		if (Open21Env.getVersion() == Version.SIAS) {
			key = key.replace("ICS Open21 de3", "Prj312");
		}
		
		try {
			
			if(!Env.isLinux()) {
				ProcessBuilder pb = new ProcessBuilder("reg", "query", "\"" + key + "\"", "/v", "\"" + name + "\"");
				// 標準出力と標準エラー出力をマージ
				pb.redirectErrorStream(true);

				// コマンド実行
				Process proc = pb.start();
				String str;
				StringBuffer sbStd = new StringBuffer();
				BufferedReader brStd = new BufferedReader(new InputStreamReader(proc.getInputStream(), "MS932"));
				while((str = brStd.readLine()) != null) {
					sbStd.append(str);
				}
				brStd.close();

				// レジストリ取得失敗
				if (0 != proc.exitValue()) {
					throw new RuntimeException("レジストリ[" + key + "][" + name + "]が取得できません。");
				}

				// 標準出力は複数文字列がブランク区切りになっているが、最後がレジストリ値
				return regAnalyze(sbStd.toString(), key, name);
			}else {
				// linuxなら/var/eteam/Icsp.Open21WF.confよりデータ読み出し
				try ( InputStream cnfIS = new FileInputStream("/var/eteam/Icsp.Open21WF.conf");
						InputStreamReader isr = new InputStreamReader(cnfIS, Encoding.UTF8);
						BufferedReader br = new BufferedReader(isr))
				{
					Properties prop = new Properties();
					prop.load(br);
					String retStr = prop.getProperty(name);
					if(retStr == null) {throw new RuntimeException("設定情報[" + name + "]が取得できません。");}
					return retStr;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException("プロセスが実行できませんでした。", e);
		}
	}
	
	/**
	 * レジストリの値を読み込む。該当レジストリが取得できなかった場合、defStrに指定した値を返却する。
	 * 
	 * @param key キー
	 * @param name 名前
	 * @param defStr 該当レジストリが取得できなかった場合に返却する値
	 * @return 値
	 */
	public static String readRegistoryWithDefaultStr(String key, String name, String defStr) {
		String r = null;
		try {
			r = readRegistory(key, name);
		} catch (RuntimeException e) {
			//レジストリなしの場合
			r = defStr;
		}
		return r;
	}

	/**
	 * レジストリの有無を調べる
	 * 
	 * @param key キー
	 * @param name 名前
	 * @return レジストリがあればtrue
	 */
	public static boolean existsRegistory(String key, String name) {
		String r = null;
		try {
			r = readRegistory(key, name);
		} catch (RuntimeException e) {
			//レジストリなしの場合}
		}
		return (r != null);
	}
	
	/**
	 * レジストリの有無を調べる
	 * 
	 * @param key キー
	 * @return レジストリがあればtrue
	 */
	public static boolean isExistsRegistoryKey(String key) {
		try {
			//当メソッド呼び元ではDE3/SIASのレジストリ分けを意識していない(DE3用キーで定数定義している)ので、ここでキーを読み替えてあげる
			if (Open21Env.getVersion() == Version.SIAS) {
				key = key.replace("ICS Open21 de3", "Prj312");
			}

			// コマンド
			ProcessBuilder pb = new ProcessBuilder("reg", "query", "\"" + key + "\"");

			try {
				// 標準出力と標準エラー出力をマージ
				pb.redirectErrorStream(true);

				// コマンド実行
				Process proc = pb.start();
				String str;
				StringBuffer sbStd = new StringBuffer();
				BufferedReader brStd = new BufferedReader(new InputStreamReader(proc.getInputStream(), "MS932"));
				while ((str = brStd.readLine()) != null) {
					sbStd.append(str);
				}
				brStd.close();

				// レジストリ取得失敗
				if (0 != proc.exitValue()) {
					throw new RuntimeException("レジストリ[" + key + "]は存在しません。");
				}
			} catch (IOException e) {
				throw new RuntimeException("プロセスが実行できませんでした。", e);
			}
			
			return true;
		} catch (RuntimeException e) {
			//レジストリなしの場合}
			return false;
		}
	}
	
	/**
	 * reg queryコマンド出力からキー値のみを取り出す
	 * @param commandRet コマンド出力	
	 * @param key キー全体
	 * @param name キー名
	 * @return キー値
	 */
	protected static String regAnalyze(String commandRet, String key, String name) {
		//コマンド結果が以下の形で来たとする
		//HKEY_LOCAL_MACHINE\SOFTWARE\（株）ＩＣＳパートナーズ\Prj312    WFSerial    REG_SZ    1234567890
		
		//キー名以降まで取り出 し、「キー名   REZ_○○   キー値」形式にする
		//WFSerial    REG_SZ    1234567890
		commandRet = commandRet.substring(key.length());
		commandRet = commandRet.substring(commandRet.indexOf(name));

		//キー名を外す
		//    REG_SZ    46336344360470945481685861231235912442359953
		commandRet = commandRet.substring(name.length());
		
		//ブランク帯、REZ_○○、ブランク帯を外す
		//1234567890
		int flg = 0, i = 0;
		while (i < commandRet.length()) {
			switch(flg) {
			case 0:
				if (' ' != commandRet.charAt(i))
				{
					flg++;
				}
				break;
			case 1:
				if (' ' == commandRet.charAt(i))
				{
					flg++;
				}
				break;
			case 2:
				if (' ' != commandRet.charAt(i))
				{
					flg++;
				}
				break;
			}
			if (flg == 3) return commandRet.substring(i);
			i++;
		}
		return "";
	}
	
	/**
	 * WFSetレジストリの値からe文書使用オプション機能の有効/無効を判別・返却する。
	 * @return true:e文書使用オプション機能有効 false:e文書使用オプション機能無効
	 */
	public static boolean checkEnableEbunshoOption(){
		boolean ebunshoFlg = false;
		//Linux版なら無効
		if (Env.isLinux())
		{
			return ebunshoFlg;
		}
		
		String wfSet = readRegistoryWithDefaultStr(REG_KEY_NAME.SIAS_WFSET[0], REG_KEY_NAME.SIAS_WFSET[1],"");
		if( wfSet.length() >= 25 ){
			int chkVal1S = Integer.parseInt(wfSet.substring(9, 10));
			int chkVal1E = Integer.parseInt(wfSet.substring(21,22));
			int chkVal2S = Integer.parseInt(wfSet.substring(12,13));
			int chkVal2E = Integer.parseInt(wfSet.substring(24,25));
			ebunshoFlg = ((chkVal1S - chkVal1E) == (chkVal2S - chkVal2E)) ? true : false;
		}else{
			ebunshoFlg = false;
		}
		
		return ebunshoFlg;
	}

	/**
	 * 予算執行管理オプション取得<br>
	 * WFSetレジストリの値から有効/無効を判定して結果を返却する。<br>
	 * 
	 * @return "":無効 "A":予算執行管理Aあり "B":予算執行管理Bあり
	 */
	public static String checkEnableYosanShikkouOption(){
		String result = RegAccess.YOSAN_SHIKKOU_OP.NO_OPTION;

		String wfSet = readRegistoryWithDefaultStr(REG_KEY_NAME.SIAS_WFSET[0], REG_KEY_NAME.SIAS_WFSET[1],"");
		if (30 < wfSet.length()){
			int chkOpAVal1S = Integer.parseInt(wfSet.substring(0, 1));
			int chkOpAVal1E = Integer.parseInt(wfSet.substring(33, 34));
			int chkOpAVal2S = Integer.parseInt(wfSet.substring(26, 27));
			int chkOPAVal2E = Integer.parseInt(wfSet.substring(32, 33));
			if ((chkOpAVal1S - chkOpAVal1E) == (chkOpAVal2S + chkOPAVal2E)){
				result = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION;
			}else{
				int chkOpBVal1S = Integer.parseInt(wfSet.substring(3, 4));
				int chkOpBVal1E = Integer.parseInt(wfSet.substring(29, 30));
				int chkOpBVal2S = Integer.parseInt(wfSet.substring(23, 24));
				int chkOPBVal2E = Integer.parseInt(wfSet.substring(27, 28));
				if ((chkOpBVal1S - chkOpBVal1E) == (chkOpBVal2S + chkOPBVal2E)){
					result = RegAccess.YOSAN_SHIKKOU_OP.B_OPTION;
				}
			}
		}
		
		if (Open21Env.getVersion() == Version.DE3) {
			String de3YosanOption = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.YOSAN_OPTION);
			if(de3YosanOption.equals( RegAccess.YOSAN_SHIKKOU_OP.A_OPTION)) {
				result = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION;
			}
			else if(de3YosanOption.equals( RegAccess.YOSAN_SHIKKOU_OP.B_OPTION)) {
				result = RegAccess.YOSAN_SHIKKOU_OP.B_OPTION;
			}
			else {
				result = RegAccess.YOSAN_SHIKKOU_OP.NO_OPTION;
			}
		}
		
		EteamLogger.getLogger(RegAccess.class).debug("予算執行管理オプション:" + result);
		return result;
	}

	/**
	 * WFSetレジストリの値から財務拠点使用オプション機能の有効/無効を判別・返却する。
	 * @return true:財務拠点使用オプション機能有効 false:財務拠点使用オプション機能無効
	 */
	public static boolean checkEnableZaimuKyotenOption(){
		boolean result = false;
		
		String wfSet = readRegistoryWithDefaultStr(REG_KEY_NAME.SIAS_WFSET[0], REG_KEY_NAME.SIAS_WFSET[1],"");
		if (37 < wfSet.length()){
			int chkOpAVal1S = Integer.parseInt(wfSet.substring(18, 19));
			int chkOpAVal1E = Integer.parseInt(wfSet.substring(37, 38));
			int chkOpAVal2S = Integer.parseInt(wfSet.substring(17, 18));
			int chkOPAVal2E = Integer.parseInt(wfSet.substring(36, 37));
			if ((chkOpAVal1S - chkOpAVal1E) == (chkOpAVal2S + chkOPAVal2E)){
				result = true;
			}
		}
		
		if (Open21Env.getVersion() == Version.DE3) {
			String de3KyotenOption = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.KYOTEN_OPTION);
			if(de3KyotenOption.equals( KINOU_SEIGYO_KBN.YUUKOU )) {
				result = true;
			}else {
				result = false;
			}
		}
		
		EteamLogger.getLogger(RegAccess.class).debug("Web拠点入力オプション:" + result);
		return result;
	}
	
	/**
	 * WFPrgレジストリの値から経費精算(WF本体)の有効/無効を判別・返却する。
	 * @return true:WF本体機能有効 false:WF本体機能無効
	 */
	public static boolean checkEnableKeihiSeisan(){
		
		boolean result;
		
		//SIASはレジストリ参照(WFPrgレジストリ値がない場合も有効とする)
		String wfPrg = readRegistoryWithDefaultStr(REG_KEY_NAME.SIAS_WFPRG[0], REG_KEY_NAME.SIAS_WFPRG[1],"");
		if(wfPrg.equals( KINOU_SEIGYO_KBN.YUUKOU ) || wfPrg.equals( "" )) {
			result = true;
		}else {
			result = false;
		}
		
		//de3はオプション値参照
		if (Open21Env.getVersion() == Version.DE3) {
			String de3YosanOption = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.IPPAN_OPTION);
			if(de3YosanOption.equals( KINOU_SEIGYO_KBN.YUUKOU )) {
				result = true;
			}else {
				result = false;
			}
		}
		
		EteamLogger.getLogger(RegAccess.class).debug("WF本体:" + result);
		return result;
	}
	
	
	/**
	 * SIAS会社データ保持用に利用しているDBを判別するレジストリ値を返却。
	 * @return 該当レジストリ値(レジストリなしなら空文字)
	 */
	public static String checkSIASDBEngine(){
		return readRegistoryWithDefaultStr(REG_KEY_NAME.SIAS_DBENGINE[0], REG_KEY_NAME.SIAS_DBENGINE[1],"");
	}
	
	/**
	 * UT
	 * レジストリを変更しつつ実行する。
	 * @param argv 未使用
	 */
	public static void main (String[] argv) {
		String[][] params = {
				{REG_KEY_NAME.INSTALL_DATE[0] ,REG_KEY_NAME.INSTALL_DATE[1]},
				{REG_KEY_NAME.BIOS_RELEASE_DATE[0] ,REG_KEY_NAME.BIOS_RELEASE_DATE[1]},
				{REG_KEY_NAME.USER_LICENSE_NUM[0] ,REG_KEY_NAME.USER_LICENSE_NUM[1]},
				{REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + "oemtest"	,REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]},
		};
		for (String[] p : params) {
			try {
				System.out.println(readRegistory(p[0], p[1]));
			} catch (RuntimeException e) {
				System.out.println(e);
			}
		}
	}
}
