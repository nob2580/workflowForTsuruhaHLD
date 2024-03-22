package eteam.gyoumu.kanitodoke.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書テキスト追加画面Action
 */
@Getter @Setter @ToString
public class KaniTodokeTextAddAction extends EteamAbstractAction {

//＜定数＞
	/** 部品形式 */
	protected static final String BUHIN_FORMAT = "buhin_format";
	/** 固定表示 */
	protected static final String KOTEI_HYOUJI = "kotei_hyouji";
	/** 小数点以下桁数 */
	protected static final String DECIMAL_POINT = "decimal_point";
	/** 部品幅 */
	protected static final String BUHIN_WIDTH = "buhin_width";
	/** 表示モード(追加) */
	protected static final int DISP_MODE_ADD = 1;
	/** 表示モード(変更) */
	protected static final int DISP_MODE_CHANGE = 2;
	
//＜画面入力＞
	/** 表示モード */
	Integer dispMode;
	/** ラベル名 */
	String labelName;
	/** 必須フラグ */
	Integer hissuFlg;
	/** 文字形式 */
	String textFormat; 
	/** 文字幅 */
	String textWidth; 
	/** 最大長 */
	String maxLength;
	/** 最小値 */
	String minValue;
	/** 最大値 */
	String maxValue;
	/** 画面タイトル（予算執行表示用） */
	String gamenTitle;
	/** 非活性フラグ（予算執行表示用） */
	String yosanDisableFlg;
	/** 小数点以下桁数 */
	String decimalPoint;
	/** 固定表示 */
	String koteiHyouji;
	
//＜画面入力以外＞
	/** 文字形式 */
	List<GMap> textFormatList;
	/** 固定表示 */
	List<GMap> koteiHyoujiList;
	/** 小数点以下桁数 */
	List<GMap> decimalPointList;
	/** 文字幅 */
	List<GMap> textWidthList;
	/** データ */
	String data;
	/** 予算執行項目ID（予算執行用） */
	String yosanId;
	
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * 
	 * @return 処理結果
	 */
	public String init() {

		// コネクション生成
		try(EteamConnection connection = EteamConnection.connect()) {
			SystemKanriCategoryLogic sysLc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

			// 選択要素（形式）
			textFormatList = sysLc.loadNaibuCdSetting(BUHIN_FORMAT);
			// 選択要素（固定表示）
			koteiHyoujiList = sysLc.loadNaibuCdSetting(KOTEI_HYOUJI);
			// 選択要素（小数点以下桁数）
			decimalPointList = sysLc.loadNaibuCdSetting(DECIMAL_POINT);
			// 選択要素（幅）
			textWidthList = sysLc.loadNaibuCdSetting(BUHIN_WIDTH);
			// 予算執行項目IDリスト
			List<GMap> lstYosanShikkouKoumokuId = sysLc.loadNaibuCdSetting("yosan_shikkou_koumoku_id");

			if (isEmpty(data)) {
				dispMode = DISP_MODE_ADD; // 追加
				labelName = ""; // ラベル名
				hissuFlg = 1; // 必須フラグ
				textFormat = EteamNaibuCodeSetting.BUHIN_FORMAT.STRING; // テキスト形式
				textWidth = EteamNaibuCodeSetting.BUHIN_WIDTH.INPUT_MEDIUM; // テキスト幅
				maxLength = ""; // 最大長
				minValue = ""; // 最小値
				maxValue = ""; // 最大値
				gamenTitle = ""; // 画面タイトル（予算執行）
				yosanDisableFlg = "0"; // 非活性フラグ（予算執行）
				decimalPoint = EteamNaibuCodeSetting.DECIMAL_POINT.ZERO; // 小数点以下桁数
				koteiHyouji = EteamNaibuCodeSetting.KOTEI_HYOUJI.NO; // 固定表示

				// 予算執行入力部品の場合は設定内容を変更する
				if (!isEmpty(yosanId)) {

					// 金額形式に再設定する
					switch (yosanId){
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU:
						// 稟議金額
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI:
						// 収入金額合計
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI:
						// 支出金額合計
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUSHI_SAGAKU:
						// 収支差額
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KINGAKU:
						// 収入金額
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KINGAKU:
						// 支出金額
						textFormat = EteamNaibuCodeSetting.BUHIN_FORMAT.MONEY;
						break;
					default:
						break;
					}

					// 画面タイトル（予算執行）
					String koumokuNm = "";
					for (GMap aMap : lstYosanShikkouKoumokuId){
						if (yosanId.equals(aMap.get("naibu_cd").toString())){
							koumokuNm = aMap.get("name").toString();
							break;
						}
					}
					gamenTitle = String.format("届書 %s ", koumokuNm);

					// 非活性フラグ（予算執行）
					yosanDisableFlg = "1";
				}

			} else {

				String[] dataArray = data.split("\r\n");
				String hissu_flg    = dataArray[DataListIndex.HISSU_FLG];  		// 必須フラグ
				String max_length   = dataArray[DataListIndex.MAX_LENGTH];  	// 最大桁数
				String min_value    = dataArray[DataListIndex.MIN_VALUE];  		// 最小値
				String max_value    = dataArray[DataListIndex.MAX_VALUE];  		// 最大値
				String label_name   = dataArray[DataListIndex.LABEL_NAME];  	// ラベル名
				String buhin_format = dataArray[DataListIndex.BUHIN_FORMAT]; // 部品形式
				String buhin_width  = dataArray[DataListIndex.BUHIN_WIDTH]; // 部品幅
				String decimal_point= dataArray[DataListIndex.DECIMAL_POINT]; // 小数点以下桁数
				String kotei_hyouji = dataArray[DataListIndex.KOTEI_HYOUJI]; // 固定表示

				dispMode = DISP_MODE_CHANGE; // 変更
				labelName = label_name; // ラベル名
				hissuFlg = Integer.parseInt(hissu_flg); // 必須フラグ
				textFormat = buhin_format; // テキスト形式
				textWidth = buhin_width; // テキスト幅
				maxLength = max_length; // 最大長
				minValue = min_value; // 最小値
				maxValue = max_value; // 最大値
				gamenTitle = ""; // 画面タイトル（予算執行）
				yosanDisableFlg = "0"; // 非活性フラグ（予算執行）
				decimalPoint = decimal_point; // 小数点以下桁数
				koteiHyouji = kotei_hyouji; // 固定表示

				// 予算執行入力部品の場合は設定内容を変更する
				if (!isEmpty(yosanId)) {

					// 画面タイトル（予算執行）
					String koumokuNm = "";
					for (GMap aMap : lstYosanShikkouKoumokuId){
						if (yosanId.equals(aMap.get("naibu_cd").toString())){
							koumokuNm = aMap.get("name").toString();
							break;
						}
					}
					gamenTitle = String.format("届書 %s ", koumokuNm);

					// 非活性フラグ（予算執行）
					yosanDisableFlg = "1";
				}
			}

			// 戻り値を返す
			return "success";

		}
	}
}
