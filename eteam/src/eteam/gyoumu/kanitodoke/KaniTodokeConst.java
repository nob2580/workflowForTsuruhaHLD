package eteam.gyoumu.kanitodoke;

/**
 * 定数定義
 * 画面固有やロジック固有のものは、各種クラスにて定義する。
 * それらをまたがって共有される定数は、当クラスにて定義する。
 */
public class KaniTodokeConst {

	/**
	 * データリストの要素位置
	 */
	public class DataListIndex {

		/** 項目名 */
		public static final int ITEM_NAME = 0;
		/** エリア区分 */
		public static final int AREA_KBN = 1;
		/** 表示順 */
		public static final int HYOUJI_JUN = 2;
		/** 伝票枝番号 */
		public static final int DENPYOU_EDANO = 3;
		/** 必須フラグ */
		public static final int HISSU_FLG = 4;
		/** 最大桁数 */
		public static final int MAX_LENGTH = 5;
		/** 最小値 */
		public static final int MIN_VALUE = 6;
		/** 最大値 */
		public static final int MAX_VALUE = 7;
		/** ラベル名 */
		public static final int LABEL_NAME = 8;
		/** 部品タイプ */
		public static final int BUHIN_TYPE = 9;
		/** 部品形式 */
		public static final int BUHIN_FORMAT = 10;
		/** 部品幅 */
		public static final int BUHIN_WIDTH = 11;
		/** 部品高さ */
		public static final int BUHIN_HEIGHT = 12;
		/** チェックボックスラベル名 */
		public static final int CHECKBOX_LABEL_NAME = 13;
		/** マスター区分 */
		public static final int MASTER_KBN = 14;
		/** 値1 */
		public static final int VALUE1 = 15;
		/** 小数点以下桁数 */
		public static final int DECIMAL_POINT = 16;
		/** 固定表示 */
		public static final int KOTEI_HYOUJI = 17;
	}
	
	/**
	 * オプションリストの要素位置
	 */
	public class OptionListIndex {
		
		/** 項目名 */
		public static final int ITEM_NAME = 0;
		/** エリア区分 */
		public static final int AREA_KBN = 1;
		/** 表示順 */
		public static final int HYOUJI_JUN = 2;
		/** オプションのテキスト */
		public static final int OPTION_TEXT = 3;
		/** オプションの値 */
		public static final int OPTION_VAL = 4;
		/** 選択項目名 */
		public static final int SELECT_ITEM = 5;
	}

	/**
	 * HTML作成モード
	 */
	public class HtmlCreateMode {
		
		/** レイアウト用 */
		public static final int LAYOUT = 1;
		/** ワークフロー用 */
		public static final int WORKFLOW = 2;
	}
	
	/**
	 * 部品タイプ
	 */
	public class BuhinType {
		
		/** テキストボックス */
		public static final String TEXT = "text";
		/** テキストエリア */
		public static final String TEXTAREA = "textarea";
		/** チェックボックス */
		public static final String CHECKBOX = "checkbox";
		/** ラジオボタン */
		public static final String RADIO = "radio";
		/** プルダウン */
		public static final String PULLDOWN = "pulldown";
		/** マスター */
		public static final String MASTER = "master";
	}
	
	/**
	 * マスター区分
	 */
	public class MasterKbn {
		
		/** 勘定科目選択 */
		public static final String KAMOKU = "kanjyouKamokuSentaku";
		/** 負担部門選択 */
		public static final String BUMON = "futanBumonSentaku";
		/** 取引先選択 */
		public static final String TORIHIKISAKI = "torihikisakiSentaku";
		/** UF1選択 */
		public static final String UF1 = "uf1Sentaku";
		/** UF2選択 */
		public static final String UF2 = "uf2Sentaku";
		/** UF3選択 */
		public static final String UF3 = "uf3Sentaku";
	}
}
