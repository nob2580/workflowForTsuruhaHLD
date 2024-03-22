package eteam.gyoumu.kanitodoke.kogamen;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeConst;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書マスター画面Action
 */
@Getter @Setter @ToString
public class KaniTodokeMasterItemAddAction extends EteamAbstractAction {

//＜定数＞
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
	/** ラベル名 */
	String masterKbn;
	/** 画面タイトル（予算執行表示用） */
	String gamenTitle;

//＜画面入力以外＞
	/** リスト */
	List<GMap> masterList;
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
			SystemKanriCategoryLogic systemKanriCategoryLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

			// 会社情報の取得
			GMap mapKaisha = systemKanriCategoryLogic.findKaishaInfo();

			// UF使用フラグ
			Integer uf1_shiyou_flg = Integer.parseInt(mapKaisha.get("uf1_shiyou_flg").toString());
			Integer uf2_shiyou_flg = Integer.parseInt(mapKaisha.get("uf2_shiyou_flg").toString());
			Integer uf3_shiyou_flg = Integer.parseInt(mapKaisha.get("uf3_shiyou_flg").toString());

			masterList = new ArrayList<GMap>();

			GMap mapAdd = new GMap();
			mapAdd.put("cd", "kanjyouKamokuSentaku");
			mapAdd.put("name", "勘定科目");
			masterList.add(mapAdd);

			mapAdd = new GMap();
			mapAdd.put("cd", "futanBumonSentaku");
			mapAdd.put("name", "負担部門");
			masterList.add(mapAdd);

			mapAdd = new GMap();
			mapAdd.put("cd", "torihikisakiSentaku");
			mapAdd.put("name", "取引先");
			masterList.add(mapAdd);

			if (uf1_shiyou_flg.equals(2) || uf1_shiyou_flg.equals(3)) {
				mapAdd = new GMap();
				mapAdd.put("cd", "uf1Sentaku");
				mapAdd.put("name", "UF1");
				masterList.add(mapAdd);
			}

			if (uf2_shiyou_flg.equals(2) || uf2_shiyou_flg.equals(3)) {
				mapAdd = new GMap();
				mapAdd.put("cd", "uf2Sentaku");
				mapAdd.put("name", "UF2");
				masterList.add(mapAdd);
			}

			if (uf3_shiyou_flg.equals(2) || uf3_shiyou_flg.equals(3)) {
				mapAdd = new GMap();
				mapAdd.put("cd", "uf3Sentaku");
				mapAdd.put("name", "UF3");
				masterList.add(mapAdd);
			}

			// 予算執行項目IDリスト
			List<GMap> lstYosanShikkouKoumokuId = systemKanriCategoryLogic.loadNaibuCdSetting("yosan_shikkou_koumoku_id");

			if (isEmpty(data)) {
				dispMode = DISP_MODE_ADD; // 追加
				labelName = ""; // ラベル名
				hissuFlg = 1; // 必須フラグ
				masterKbn = ""; // マスター区分
				gamenTitle = ""; // 画面タイトル（予算執行）

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

					// マスター区分とプルダウンリスト要素
					masterList.clear();
					switch (yosanId){
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_BUMON:
						// 収入部門
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_BUMON:
						// 支出部門
						masterKbn = KaniTodokeConst.MasterKbn.BUMON;
						mapAdd = new GMap();
						mapAdd.put("cd", masterKbn);
						mapAdd.put("name", "負担部門");
						masterList.add(mapAdd);
						break;

					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KAMOKU:
						// 収入科目
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU:
						// 収入科目
						masterKbn = KaniTodokeConst.MasterKbn.KAMOKU;
						mapAdd = new GMap();
						mapAdd.put("cd", masterKbn);
						mapAdd.put("name", "勘定科目");
						masterList.add(mapAdd);
						break;

					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_EDA:
						// 収入枝番
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA:
						// 支出枝番
						masterKbn = "kanjyouKamokuEdabanSentaku";
						mapAdd = new GMap();
						mapAdd.put("cd", masterKbn);
						mapAdd.put("name", "勘定科目枝番");
						masterList.add(mapAdd);
						break;
					default:
						break;
					}
				}

			} else {

				String[] dataArray = data.split("\r\n");
				String hissu_flg   = dataArray[DataListIndex.HISSU_FLG];  		// 必須フラグ
				String label_name  = dataArray[DataListIndex.LABEL_NAME];  		// ラベル名
				String master_kbn  = dataArray[DataListIndex.MASTER_KBN];  		// マスター区分

				dispMode = DISP_MODE_CHANGE; // 変更
				labelName = label_name; // ラベル名
				hissuFlg = Integer.parseInt(hissu_flg); // 必須フラグ
				masterKbn = master_kbn; // マスター区分
				gamenTitle = ""; // 画面タイトル（予算執行）

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

					// プルダウンリスト要素
					masterList.clear();
					switch (yosanId){
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_BUMON:
						// 収入部門
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_BUMON:
						// 支出部門
						mapAdd = new GMap();
						mapAdd.put("cd", KaniTodokeConst.MasterKbn.BUMON);
						mapAdd.put("name", "負担部門");
						masterList.add(mapAdd);
						break;
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KAMOKU:
						// 収入科目
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU:
						// 収入科目
						mapAdd = new GMap();
						mapAdd.put("cd", KaniTodokeConst.MasterKbn.KAMOKU);
						mapAdd.put("name", "勘定科目");
						masterList.add(mapAdd);
						break;
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_EDA:
						// 収入枝番
					case EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA:
						// 支出枝番
						mapAdd = new GMap();
						mapAdd.put("cd", "kanjyouKamokuEdabanSentaku");
						mapAdd.put("name", "勘定科目枝番");
						masterList.add(mapAdd);
						break;
					default:
						break;
					}
				}
			}

			// 戻り値を返す
			return "success";

		}
	}
}
