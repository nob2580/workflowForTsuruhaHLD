package eteam.symbol;

import java.util.ResourceBundle;

/**
 * フッター・フッター等の文言管理
 * logo.pngも合わせてメンテしてください。
 */
public class EteamSymbol {
	/** ヘッダー文言 */
	public static final String SYSTEM_NAME = "OPEN21 Workflow";
	/** フッター文言 */
	public static final String FOOTER = "Copyright&copy; 会計システムのICS Partners. All rights reserved.";
	/** VERSION */
	protected static String VERSION = null;

	static {
		try {
			ResourceBundle prop = ResourceBundle.getBundle("eteam");
			VERSION = prop.getString("version");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ヴァージョン("yyMMddXX"形式)を標準出力する
	 * @param argv 未使用
	 */
	public static void main(String[] argv) {
		System.out.println(VERSION);
	}

	/**
	 * ヴァージョン("Ver yy.MM.dd.XX"形式)を返す
	 * @return ヴァージョン("Ver yy.MM.dd.XX"形式)
	 */
	public static String formatVerYYMMDDXX() {
		return "Ver " + VERSION.substring(0, 2) + "." + VERSION.substring(2, 4) + "." + VERSION.substring(4, 6) + "." + VERSION.substring(6, 8);
	}
}
