package eteam.common;

public class EteamExtConst extends EteamConst {
	/**
	 * セッションキー
	 */
	public class Sessionkey {
		/** 強制ログイントリガーのURL */
		public static final String ORIGINAL_URL = "originalURL";
		/** 強制パスワード変更トリガーのURL */
		public static final String ORIGINAL_URL2 = "originalURL2";
		/** ユーザー情報 */
		public static final String USER = "user";
		/** パスワード有効期限チェック済みフラグ */
		public static final String PASSWORD_PERIOD_CHECK = "password_period_check";
		/** 請求書払いCSV */
		public static final String SEIKYUUSHO_CSV = "seikyuusho_csv";
		/** 支払依頼CSV */
		public static final String SHIHARAIIRAI_CSV = "shiharaiirai_csv";
		/** 部門一括登録CSV */
		public static final String BUMON_CSV = "bumon_csv";
		/** ユーザー一括登録CSV */
		public static final String USER_CSV = "user_csv";
		/** 部門推奨ルート一括登録CSV */
		public static final String ROUTE_CSV = "route_csv";
		/** 取引（仕訳）一括登録CSV */
		public static final String TORIHIKI_CSV = "torihiki_csv";
		/** 部門別取引（仕訳）一括登録CSV */
		public static final String BUMONBETSUTORIHIKI_CSV = "bumonbetsutorihiki_csv";
		/** 法人カード使用履歴CSV */
		public static final String HOUJINCARD_CSV ="houjincard_csv";
		//▼カスタマイズ
		/** テナントID */
		public static final String SCHEMA_NAME = "schemaName";
		//▲カスタマイズ
	}

}
