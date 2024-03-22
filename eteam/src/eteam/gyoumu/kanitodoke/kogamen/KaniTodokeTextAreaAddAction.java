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
 * ユーザー定義届書テキストエリア追加画面Action
 */
@Getter @Setter @ToString
public class KaniTodokeTextAreaAddAction extends EteamAbstractAction {

//＜定数＞
	/** 固定表示 */
	protected static final String KOTEI_HYOUJI = "kotei_hyouji";
	/** 部品幅 */
	protected static final String BUHIN_WIDTH = "buhin_width";
	/** 部品高さ */
	protected static final String BUHIN_HEIGHT = "buhin_height";
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
	/** 文字幅 */
	String textWidth; 
	/** 文字高さ */
	String textHeight; 
	/** 最大長 */
	String maxLength;
	/** 画面タイトル（予算執行表示用） */
	String gamenTitle;
	/** 固定表示 */
	String koteiHyouji;

//＜画面入力以外＞
	/** 固定表示 */
	List<GMap> koteiHyoujiList;
	/** 文字幅 */
	List<GMap> textWidthList;
	/** 文字高さ */
	List<GMap> textHeightList;
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
		try(EteamConnection connection = EteamConnection.connect())
		{
			SystemKanriCategoryLogic sysLc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

			// 選択要素（固定表示）
			koteiHyoujiList = sysLc.loadNaibuCdSetting(KOTEI_HYOUJI);
			// 選択要素（幅）
			textWidthList = sysLc.loadNaibuCdSetting(BUHIN_WIDTH);
			// 選択要素（高さ）
			textHeightList = sysLc.loadNaibuCdSetting(BUHIN_HEIGHT);
			// 予算執行項目IDリスト
			List<GMap> lstYosanShikkouKoumokuId = sysLc.loadNaibuCdSetting("yosan_shikkou_koumoku_id");

			if (isEmpty(data)) {
				dispMode = DISP_MODE_ADD; // 追加
				labelName = ""; // ラベル名
				hissuFlg = 1; // 必須フラグ
				textWidth = EteamNaibuCodeSetting.BUHIN_WIDTH.INPUT_XXLARGE; // テキスト幅
				textHeight = EteamNaibuCodeSetting.BUHIN_HEIGHT.TEXTAREA_MEDIUM;// テキスト長さ
				maxLength = ""; // 最大長
				gamenTitle = ""; // 画面タイトル（予算執行）
				koteiHyouji = EteamNaibuCodeSetting.KOTEI_HYOUJI.NO; // 固定表示

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
				}

			} else {

				String[] dataArray = data.split("\r\n");
				String hissu_flg    = dataArray[DataListIndex.HISSU_FLG];  		// 必須フラグ
				String max_length   = dataArray[DataListIndex.MAX_LENGTH];  	// 最大桁数
				String label_name   = dataArray[DataListIndex.LABEL_NAME];  	// ラベル名
				String buhin_width  = dataArray[DataListIndex.BUHIN_WIDTH]; // 部品幅
				String buhin_height = dataArray[DataListIndex.BUHIN_HEIGHT]; // 部品高さ
				String kotei_hyouji = dataArray[DataListIndex.KOTEI_HYOUJI]; // 固定表示

				dispMode = DISP_MODE_CHANGE; // 変更
				labelName = label_name; // ラベル名
				hissuFlg = Integer.parseInt(hissu_flg); // 必須フラグ
				textWidth = buhin_width; // テキスト幅
				textHeight = buhin_height; // テキスト長さ
				maxLength = max_length; // 最大長
				gamenTitle = ""; // 画面タイトル（予算執行）
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
				}
			}

			// 戻り値を返す
			return "success";

		}
	}
}
