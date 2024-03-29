package eteam.common.open21;

import eteam.common.Env;
import eteam.common.RegAccess;
import eteam.common.RegAccess.REG_KEY_NAME;

/**
 * OPEN21の環境判定
 */
public class Open21Env {

	/** ヴァージョン */
	public enum Version {
		/** de3 */ DE3,
		/** SIAS */ SIAS
	}

	/** SIASのDB種別 */
	public enum SIASDBEngine {
		/** SQLServer */ SQLSERVER,
		/** PostgerSQL */ POSTGRESQL
	}

	/** ヴァージョン */
	protected static Version v;
	static {
		if (RegAccess.existsRegistory(REG_KEY_NAME.SIAS_CHECK[0], REG_KEY_NAME.SIAS_CHECK[1])) {
			v = Version.SIAS;
		} else if (RegAccess.existsRegistory(REG_KEY_NAME.DE3_CHECK[0], REG_KEY_NAME.DE3_CHECK[1])) {
			v = Version.DE3;
		} else {
			throw new RuntimeException("OPEN21のユーザライセンスレジストリが存在しない為、処理を続けることができません。");
		}
	}

	/**
	 * 稼働環境(ヴァージョン)を取得する。
	 * @return 稼働環境(ヴァージョン)
	 */
	public static Version getVersion() {
		return v;
	}

	/**
	 * SIAS会社データ保持用に利用しているDBを取得する。
	 * @return SIASDB種別
	 */
	public static SIASDBEngine getSIASDBEngine() {
		String dbEngine = RegAccess.checkSIASDBEngine();
		//該当値「3」ならPostgreSQL
		if( "3".equals(dbEngine) ){
			return SIASDBEngine.POSTGRESQL;
		}
		//2020/04/02時点では「3」以外ならSQLServerとみなす
		return SIASDBEngine.SQLSERVER;
	}

	/**
	 * OPEN21の使用がUTモードか調べる。
	 * batconnect.propertiesでisut = trueかどうか。
	 * @return UTモードならtrue
	 */
	public static boolean readIsUt() {
		return Env.open21IsDummy();
	}

	/**
	 * SIASマスター取込向けコネクタ用の接続先ホスト・インスタンス名の取得
	 * @return 接続先ホスト・インスタンス名
	 */
	public static String getHost4SIAS(){
		if (readIsUt()) {
// return "192.168.64.87\\ICSP";
			return "192.168.64.101\\ICSP"; //軽減対応済みのSIASに接続
		}
		String host;
		String regType = RegAccess.readRegistory(REG_KEY_NAME.SIAS_TYPE);
		String regIp = RegAccess.readRegistory(REG_KEY_NAME.SIAS_SVIP);
		String regPort = RegAccess.readRegistory(REG_KEY_NAME.SIAS_SVPORT);

		switch (regType) {
		case "1":
		case "3":
		case "5":
			host = "localhost";
			break;
		case "2":
		case "4":
			host = regIp;
			break;
		default:
			throw new RuntimeException("レジストリ[" + REG_KEY_NAME.SIAS_TYPE[0] + "][" + REG_KEY_NAME.SIAS_TYPE[1] + "]が存在しません。");
		}
		if(Open21Env.getSIASDBEngine() == Open21Env.SIASDBEngine.SQLSERVER) {
			host = host + "\\ICSP";
		}

		switch (regPort) {
		case "1434":
			break;
		default:
			host = host + ":" + regPort;
			break;
		}
		return host;
	}

	/**
	 * de3マスター取込向けコネクタ用の接続先ホスト・インスタンス名の取得
	 * @return 接続先ホスト・インスタンス名
	 */
	public static String getHost4DE3(){

		String port = "2638";

		if (readIsUt()) {
// return "192.168.64.85:" + port;
			return "192.168.64.100:" + port; //軽減対応済みde3に接続
		}
		String host = "";

		if (RegAccess.isExistsRegistoryKey(REG_KEY_NAME.DE3_SVIP[0]))
		{
			// de3財務配布メディア　インストール環境
			String svip = RegAccess.readRegistory(REG_KEY_NAME.DE3_SVIP);
			String myip = RegAccess.readRegistory(REG_KEY_NAME.DE3_MYIP);
			if (!svip.isEmpty())
			{
				host = svip;
			}
			else if (!myip.isEmpty()) {
				host = myip;
			}
			else
			{
				throw new RuntimeException("レジストリ[" + REG_KEY_NAME.DE3_SVIP[0] + "][" + REG_KEY_NAME.DE3_SVIP[1] + "]または[" + REG_KEY_NAME.DE3_MYIP[1] + "]に値が存在しません。");
			}
		}
		else if (RegAccess.isExistsRegistoryKey(REG_KEY_NAME.DE3_CLIENT_SVIP[0]))
		{
			// de3コネクタークライアント＆WFインストール環境
			String clientSvip = RegAccess.readRegistory(REG_KEY_NAME.DE3_CLIENT_SVIP);
			if (!clientSvip.isEmpty())
			{
				host = clientSvip;
			}
			else
			{
				throw new RuntimeException("レジストリ[" + REG_KEY_NAME.DE3_CLIENT_SVIP[0] + "][" + REG_KEY_NAME.DE3_CLIENT_SVIP[1] + "]に値が存在しません。");

			}
		}
		else if (RegAccess.isExistsRegistoryKey(REG_KEY_NAME.DE3_CLIENT_SUB_SVIP[0]))
		{
			// de3コネクタークライアント＆WFインストール環境2
			String clientSubSvip = RegAccess.readRegistory(REG_KEY_NAME.DE3_CLIENT_SUB_SVIP);

			if (!clientSubSvip.isEmpty())
			{
				host = clientSubSvip;
			}
			else
			{
				throw new RuntimeException("レジストリ[" + REG_KEY_NAME.DE3_CLIENT_SUB_SVIP[0] + "][" + REG_KEY_NAME.DE3_CLIENT_SUB_SVIP[1] + "]に値が存在しません。");
			}
		}
		else
		{
			throw new RuntimeException("レジストリ[" + REG_KEY_NAME.SIAS_TYPE[0] + "]または[" + REG_KEY_NAME.DE3_CLIENT_SVIP[0] + "]または[" + REG_KEY_NAME.DE3_CLIENT_SUB_SVIP[0] +"]が存在しません。");
		}
		return host +":" + port;
	}

	/**
	 * ワークフロー側PostgreSQLの接続用ポート番号取得
	 * @return 接続先ポート番号のString
	 */
	public static String getPortWF(){
		//16進数で格納されているため10進数に変換
		String port16 = RegAccess.readRegistoryWithDefaultStr(REG_KEY_NAME.WF_PORT[0], REG_KEY_NAME.WF_PORT[1],"5432");
		int port = Integer.decode(port16);
		return String.valueOf(port);
	}

	/**
	 * ワークフロー側PostgreSQLのベースディレクトリ取得
	 * @return ベースディレクトリString
	 */
	public static String getBaseDirWF(){
		return RegAccess.readRegistoryWithDefaultStr(REG_KEY_NAME.WF_BASEDIR[0], REG_KEY_NAME.WF_BASEDIR[1],"");
	}

	/**
	 * UT
	 * レジストリを変更しつつ実行する。
	 * @param argv 未使用
	 */
	public static void main(String[] argv) {
// System.out.println(getVersion());
// System.out.println(readIsUt());
// System.out.println(getHost4SIAS());
		System.out.println(getHost4SIAS());
	}
}
