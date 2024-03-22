package eteam.common;

/**
 * 定数定義
 * 画面固有やロジック固有のものは、各種クラスにて定義する。
 * それらをまたがって共有される定数は、当クラスにて定義する。
 */
public class EteamConst {

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
	}

	/**
	 * ブラウザー判定コード
	 */
	public class BrowserCode {
		/** ブラウザ不明 */
		public static final int UNKNOWN  = 0;
		/** ブラウザIE */
		public static final int IE       = 1;
		/** ブラウザFirefox */
		public static final int FIREFOX  = 2;
		/** ブラウザOpera */
		public static final int OPERA    = 3;
		/** ブラウザChrome */
		public static final int CHROME   = 4;
		/** ブラウザSafari */
		public static final int SAFARI   = 5;
		/** ブラウザNetscape */
		public static final int NETSCAPE = 6;
		/** ブラウザEdge */
		public static final int EDGE     = 7;
	}

	/**
	 * CONTENT TYPE(HTTP RESPONSE HEADER)
	 */
	public class ContentType {
		/** EXCEL */
		public static final String EXCEL = "application/vnd.ms-excel";
		/** ZIP */
		public static final String ZIP = "application/zip";
		/** PNG画像 */
		public static final String PNG = "image/png";
		/** PDF */
		public static final String PDF = "application/pdf";
		/** JS */
		public static final String JS = "application/javascript;charset=utf-8";
	}

	/**
	 * HTTP Header cache
	 */
	public class HttpHeaderName {
		/** キャシュ有効期限設定 */
		public static final String CACHE = "Cache-Control";
	}

	/**
	 * HTTP Header cache
	 */
	public class HttpHeaderValue {
		/** キャシュ有効期限設定 */
		public static final String CACHE = "max-age=3600";
	}

	/**
	 * 環境
	 */
	public class Env {
		/** データソース名 */
		public static final String DS_ENV = "java:comp/env/ETEAM_JDBC";
		/** Seasar 設定ファイル */
		public static final String SEASAR_DICON_FILENAME = "app.dicon";
	}

	/**
	 * アプリ共通
	 */
	public class Application {
		/** アクションクラス名の語尾の長さ */
		public static final int ACTION_LENGTH = 6; //"Action".length()
	}

	/** (所属部門)部門コード */
	public static class BUMON_CD {
		/** 全社 */
		public static final String ZENSHA = "0000";
	}

	/** 業務ロールID定義 */
	public static class GyoumuRoleId {
		/** システム管理 */
		public static final String SYSTEM_KANRI = "00000";
	}

	/**
	 * ユーザーID
	 */
	public static class UserId {
		/** ADMIN */
		public static final String ADMIN = "admin";
	}
	/** ドメイン */
	public static class Domain {
		/** フラグのドメイン */
		public static final String[] FLG = {"1", "0"};
	}

	/** 仕訳パターンの定数 */
	public static class ShiwakeConst {
		/** 勘定科目を入力ささえる(CSVアップロードのみ) */
		public static final String KAMOKU = "<KAMOKU>";
		/** 勘定枝番を入力させる */
		public static final String EDABAN = "<EDABAN>";
		/** 負担部門を入力させる */
		public static final String FUTAN = "<FUTAN>";
		/** 代表部門を使用する */
		public static final String DAIHYOUBUMON = "<DAIHYOUBUMON>";
		/** 初期代表を表示する */
		public static final String SYOKIDAIHYOU = "<SYOKIDAIHYOU>";
		/** 取引先を使用する */
		public static final String TORIHIKI = "<TORIHIKI>";
		/** プロジェクトコードを使用する */
		public static final String PROJECT = "<PROJECT>";
		/** セグメントコードを使用する */
		public static final String SEGMENT = "<SG>";
		/** UFを使用する */
		public static final String UF = "<UF>";
		/** 消費税率を使用する */
		public static final String ZEIRITSU = "<ZEIRITSU>";
	}
	/** 仕訳パターン(画面上文言) */
	public static class ShiwakeConstWa {
		/** 勘定枝番を入力させる */
		public static final String EDABAN = "任意枝番";
		/** 負担部門を入力させる */
		public static final String FUTAN = "任意部門";
		/** 代表部門を使用する */
		public static final String DAIHYOUBUMON = "代表部門";
		/** 初期代表を使用する */
		public static final String SYOKIDAIHYOU = "初期代表";
		/** 取引先を使用する */
		public static final String TORIHIKI = "任意取引先";
		/** プロジェクトコードを使用する */
		public static final String PROJECT = "任意プロジェクト";
		/** セグメントコードを使用する */
		public static final String SEGMENT = "任意セグメント";
		/** UFを使用する */
		public static final String UF = "任意UF";
	}

	/** OPEN21関連の定数 */
	public static class OP21 {
		/** 仕訳区分 */
		public static final int SHIWAKE_KBN = 41;
	}

	/** システムプロパティ */
	public static class SYSTEM_PROP {
		/** スキーマ名 */
		public static final String SCHEMA = "schema";
		/** currentTimeMillisの差分(１スレッド処理間隔高々。これ以上処理間隔があいたら別要求と判断する） */
		public static final long ONE_THREAD_MS = 2000;
	}

	/**
	 * テーブル名
	 */
	public static class TableName {
		/** パスワード */
		public static final String PASSWORD = "password";
	}

	/**
	 * 文字コード
	 */
	public static class Encoding {
		/** Windows-31j */
		public static final String MS932 = "MS932";
		/** UTF-8 */
		public static final String UTF8 = "UTF-8";
	}

	/**
	 * パス
	 */
	public static class Path {
		/** パッチ */
		public static final String PATCH = "c:\\eteam\\patch";
		/** パッチ(Linux) */
		public static final String PATCH_LINUX = "/var/eteam/patch";
	}

	/**
	 * 共通ファイル名
	 */
	public static class FileName {
		/** バックアップコメント */
		public static final String BACKUP_COMMENT_FILENAME = "backup_comment.txt";
	}

	/**
	 * パスワード有効期限区分
	 */
	public static class PasswordValidTerm {
		/** パスワード有効期限：無限 */
		public static final int INFI = 0;
		/** パスワード有効期限：通常 */
		public static final int NORMAL = 1;
		/** パスワード有効期限：失効 */
		public static final int NONE = 2;
	}

	/**
	 * 仕訳コミット単位
	 */
	public static class SHIWAKE_COMMIT_TYPE {
		/** 伝票種別 */
		public static final String DENPYOU_KBN = "1";
		/** 伝票単位 */
		public static final String DENPYOU_ID = "2";
	}
	/**
	 * 仕訳連携単位
	 */
	public static class SHIWAKE_RENKEI_TYPE {
		/** 伝票単位 */
		public static final String DENPYOU_ID = "1";
		/** 伝票種別 */
		public static final String DENPYOU_KBN = "2";
	}
	/**
	 * 仕訳作成形式
	 */
	public static class SHIWAKE_SAKUSEI_TYPE {
		/** 発生主義 */
		public static final String HASSEI = "hassei";
		/** 複合形式 */
		public static final String FUKUGOU = "fukugou";
	}

	/**
	 * 注記文言
	 */
	public static class Chuuki {
		/** EXCEL表記用文言 */
		public static String CHUUKI_EXCEL = "※財務転記時に一部がカットされます。";
	}

	/**
	 * 注記文言
	 */
	public static class HankanaCheck {
		/** 正規表現 */
		public static final String REGEX = "[a-zA-Z0-9ｧ-ﾝｦ()･.\\-\\\\/ﾞﾟ\\s ]+";
		/** 正規表現 */
		public static final String MATCH_COLUMN_STR1 = "_name_hankana";
		/** 正規表現 */
		public static final String MATCH_COLUMN_STR2 = "saki_kouza_meigi_kana";
	}
	/**
	 * マスター管理半角チェック
	 */
	public static class HankakuCheck {
		/** 正規表現 */
		public static final String REGEX = "[a-zA-Z0-9]+";
		/** チェック対象カラム */
		public static final String MATCH_COLUMN_STR1 = "heishu_cd";
	}
	/**
	 * 予算チェック粒度
	 */
	public static class yosanCheckTani {
		/** 集計部門 */
		public static final String BUMON = "0";
		/** 集計部門科目 */
		public static final String BUMON_KAMOKU = "1";
	}

	/**
	 * 予算チェック期間
	 * @author fujimoto
	 */
	public static class yosanCheckKikan {
		/** 通年 */
		public static final String YEAR = "0";
		/** 期首～予算執行対象月まで */
		public static final String TO_TAISHOUZUKI = "1";
	}

	/**
	 * 稟議超過時の承認等許可
	 */
	public static class ringiChoukaShounintou {
		/** 不許可 */
		public static final String NG = "0";
		/** 許可 */
		public static final String OK = "1";
	}

	/**
	 * 複数明細がある場合のルート判断基準
	 *
	 */
	public static class torihikiSentakuRule {
		/** 金額が最大 */
		public static String KINGAKU_MAX = "1";
		/** 先頭明細 */
		public static final String MEISAI_TOP  = "2";
	}

	/**
	 * 取引毎ルート設定可否
	 *
	 */
	public static class routeTorihiki {
		/** ルート毎設定可 */
		public static String OK = "1";
		/** デフォルト設定のみ */
		public static String NG = "0";
	}

	/**
	 * 取引毎ルート設定可否
	 *
	 */
	public static class ufKbn {
		/** ルート毎設定可 */
		public static String KAHEN = "0";
		/** デフォルト設定のみ */
		public static String KOTEI = "1";
	}

	/**
	 * 起案番号
	 */
	public static class kianBangou {
		/** 最大桁数 */
		public static final int MAX_LENGTH = 6;
		/** 最大値 */
		public static final long MAX_VALUE  = 999999;
		/** マスター管理 入力値範囲チェック 対象カラム */
		public static final String MASTER_CHK_NUM_RENGE_COLUMN1 = "kian_bangou_from";
		/** マスター管理 入力値範囲チェック 対象カラム */
		public static final String MASTER_CHK_NUM_RENGE_COLUMN2 = "kian_bangou_to";
	}

	/**
	 * ユーザー定義届書の部品タイプ
	 */
	public static class buhinType {
		/** チェックボックス */
		public static final String CHECKBOX = "checkbox";
		/** マスター */
		public static final String MASTER = "master";
		/** プルダウン */
		public static final String PULLDOWN = "pulldown";
		/** ラジオ */
		public static final String RADIO = "radio";
		/** テキスト */
		public static final String TEXT = "text";
		/** テキストボックス */
		public static final String TEXTAREA = "textarea";
	}

	/**
	 * 軽減税率区分
	 */
	public static class keigenZeiritsuKbn {
		/** 通常税率 */
		public static final String NORMAL = "0";
		/** 軽減税率 */
		public static final String KEIGEN = "1";
		/** 軽減税率マーク */
		public static final String KEIGEN_MARK = "*";
	}

	/**
	 * 現預金出納帳の残高タイプ
	 */
	public static class suitouchouZandakaType {
		/** 科目 */
		public static final String kamoku = "1";
		/** 部門科目 */
		public static final String bumonKamoku = "2";
		/** 科目枝番 */
		public static final String kamokuEdaban = "3";
		/** 部門科目枝番 */
		public static final String bumonKamokuEdaban = "4";
	}

	/** 財務拠点の伝票区分頭文字 */
	public static final String DENPYOU_KBN_INITIAL_KYOTEN = "Z";

	/**
	 * 決算期番号（OPEN21）
	 */
	public static class Open21KessankiBangou {
		/** 翌期 */
		public static final int YOKUKI = 0;
		/** 当期 */
		public static final int TOUKI = 1;
		/** 前期 */
		public static final int ZENKI = 2;
		/** 前々期 */
		public static final int ZENZENKI = 3;
	}

}
