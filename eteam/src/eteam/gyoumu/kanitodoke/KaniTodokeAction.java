package eteam.gyoumu.kanitodoke;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID;
import eteam.common.EteamSettingInfo;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.common.util.CollectionUtil;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.BuhinType;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.HtmlCreateMode;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.MasterKbn;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書申請Action
 */
@Getter @Setter @ToString
public class KaniTodokeAction extends WorkflowEventControl {

	//＜定数＞

	//＜画面入力＞

	/** 入力モード */
	boolean enableInput;
	
	/** 参照バージョン */
	String sanshouVersion;

	/** 最大伝票枝番号 */
	Integer maxDenpyouEdaNo = 1;

	/** 申請内容の値１ */ String shinsei01_val1; /** 申請内容の値２ */ String shinsei01_val2;
	/** 申請内容の値１ */ String shinsei02_val1; /** 申請内容の値２ */ String shinsei02_val2;
	/** 申請内容の値１ */ String shinsei03_val1; /** 申請内容の値２ */ String shinsei03_val2;
	/** 申請内容の値１ */ String shinsei04_val1; /** 申請内容の値２ */ String shinsei04_val2;
	/** 申請内容の値１ */ String shinsei05_val1; /** 申請内容の値２ */ String shinsei05_val2;
	/** 申請内容の値１ */ String shinsei06_val1; /** 申請内容の値２ */ String shinsei06_val2;
	/** 申請内容の値１ */ String shinsei07_val1; /** 申請内容の値２ */ String shinsei07_val2;
	/** 申請内容の値１ */ String shinsei08_val1; /** 申請内容の値２ */ String shinsei08_val2;
	/** 申請内容の値１ */ String shinsei09_val1; /** 申請内容の値２ */ String shinsei09_val2;
	/** 申請内容の値１ */ String shinsei10_val1; /** 申請内容の値２ */ String shinsei10_val2;
	/** 申請内容の値１ */ String shinsei11_val1; /** 申請内容の値２ */ String shinsei11_val2;
	/** 申請内容の値１ */ String shinsei12_val1; /** 申請内容の値２ */ String shinsei12_val2;
	/** 申請内容の値１ */ String shinsei13_val1; /** 申請内容の値２ */ String shinsei13_val2;
	/** 申請内容の値１ */ String shinsei14_val1; /** 申請内容の値２ */ String shinsei14_val2;
	/** 申請内容の値１ */ String shinsei15_val1; /** 申請内容の値２ */ String shinsei15_val2;
	/** 申請内容の値１ */ String shinsei16_val1; /** 申請内容の値２ */ String shinsei16_val2;
	/** 申請内容の値１ */ String shinsei17_val1; /** 申請内容の値２ */ String shinsei17_val2;
	/** 申請内容の値１ */ String shinsei18_val1; /** 申請内容の値２ */ String shinsei18_val2;
	/** 申請内容の値１ */ String shinsei19_val1; /** 申請内容の値２ */ String shinsei19_val2;
	/** 申請内容の値１ */ String shinsei20_val1; /** 申請内容の値２ */ String shinsei20_val2;
	/** 申請内容の値１ */ String shinsei21_val1; /** 申請内容の値２ */ String shinsei21_val2;
	/** 申請内容の値１ */ String shinsei22_val1; /** 申請内容の値２ */ String shinsei22_val2;
	/** 申請内容の値１ */ String shinsei23_val1; /** 申請内容の値２ */ String shinsei23_val2;
	/** 申請内容の値１ */ String shinsei24_val1; /** 申請内容の値２ */ String shinsei24_val2;
	/** 申請内容の値１ */ String shinsei25_val1; /** 申請内容の値２ */ String shinsei25_val2;
	/** 申請内容の値１ */ String shinsei26_val1; /** 申請内容の値２ */ String shinsei26_val2;
	/** 申請内容の値１ */ String shinsei27_val1; /** 申請内容の値２ */ String shinsei27_val2;
	/** 申請内容の値１ */ String shinsei28_val1; /** 申請内容の値２ */ String shinsei28_val2;
	/** 申請内容の値１ */ String shinsei29_val1; /** 申請内容の値２ */ String shinsei29_val2;
	/** 申請内容の値１ */ String shinsei30_val1; /** 申請内容の値２ */ String shinsei30_val2;

	/** 明細の値１ */ String[] meisai01_val1 = new String[0]; /** 明細の値２ */ String[] meisai01_val2 = new String[0];
	/** 明細の値１ */ String[] meisai02_val1 = new String[0]; /** 明細の値２ */ String[] meisai02_val2 = new String[0];
	/** 明細の値１ */ String[] meisai03_val1 = new String[0]; /** 明細の値２ */ String[] meisai03_val2 = new String[0];
	/** 明細の値１ */ String[] meisai04_val1 = new String[0]; /** 明細の値２ */ String[] meisai04_val2 = new String[0];
	/** 明細の値１ */ String[] meisai05_val1 = new String[0]; /** 明細の値２ */ String[] meisai05_val2 = new String[0];
	/** 明細の値１ */ String[] meisai06_val1 = new String[0]; /** 明細の値２ */ String[] meisai06_val2 = new String[0];
	/** 明細の値１ */ String[] meisai07_val1 = new String[0]; /** 明細の値２ */ String[] meisai07_val2 = new String[0];
	/** 明細の値１ */ String[] meisai08_val1 = new String[0]; /** 明細の値２ */ String[] meisai08_val2 = new String[0];
	/** 明細の値１ */ String[] meisai09_val1 = new String[0]; /** 明細の値２ */ String[] meisai09_val2 = new String[0];
	/** 明細の値１ */ String[] meisai10_val1 = new String[0]; /** 明細の値２ */ String[] meisai10_val2 = new String[0];
	/** 明細の値１ */ String[] meisai11_val1 = new String[0]; /** 明細の値２ */ String[] meisai11_val2 = new String[0];
	/** 明細の値１ */ String[] meisai12_val1 = new String[0]; /** 明細の値２ */ String[] meisai12_val2 = new String[0];
	/** 明細の値１ */ String[] meisai13_val1 = new String[0]; /** 明細の値２ */ String[] meisai13_val2 = new String[0];
	/** 明細の値１ */ String[] meisai14_val1 = new String[0]; /** 明細の値２ */ String[] meisai14_val2 = new String[0];
	/** 明細の値１ */ String[] meisai15_val1 = new String[0]; /** 明細の値２ */ String[] meisai15_val2 = new String[0];
	/** 明細の値１ */ String[] meisai16_val1 = new String[0]; /** 明細の値２ */ String[] meisai16_val2 = new String[0];
	/** 明細の値１ */ String[] meisai17_val1 = new String[0]; /** 明細の値２ */ String[] meisai17_val2 = new String[0];
	/** 明細の値１ */ String[] meisai18_val1 = new String[0]; /** 明細の値２ */ String[] meisai18_val2 = new String[0];
	/** 明細の値１ */ String[] meisai19_val1 = new String[0]; /** 明細の値２ */ String[] meisai19_val2 = new String[0];
	/** 明細の値１ */ String[] meisai20_val1 = new String[0]; /** 明細の値２ */ String[] meisai20_val2 = new String[0];
	/** 明細の値１ */ String[] meisai21_val1 = new String[0]; /** 明細の値２ */ String[] meisai21_val2 = new String[0];
	/** 明細の値１ */ String[] meisai22_val1 = new String[0]; /** 明細の値２ */ String[] meisai22_val2 = new String[0];
	/** 明細の値１ */ String[] meisai23_val1 = new String[0]; /** 明細の値２ */ String[] meisai23_val2 = new String[0];
	/** 明細の値１ */ String[] meisai24_val1 = new String[0]; /** 明細の値２ */ String[] meisai24_val2 = new String[0];
	/** 明細の値１ */ String[] meisai25_val1 = new String[0]; /** 明細の値２ */ String[] meisai25_val2 = new String[0];
	/** 明細の値１ */ String[] meisai26_val1 = new String[0]; /** 明細の値２ */ String[] meisai26_val2 = new String[0];
	/** 明細の値１ */ String[] meisai27_val1 = new String[0]; /** 明細の値２ */ String[] meisai27_val2 = new String[0];
	/** 明細の値１ */ String[] meisai28_val1 = new String[0]; /** 明細の値２ */ String[] meisai28_val2 = new String[0];
	/** 明細の値１ */ String[] meisai29_val1 = new String[0]; /** 明細の値２ */ String[] meisai29_val2 = new String[0];
	/** 明細の値１ */ String[] meisai30_val1 = new String[0]; /** 明細の値２ */ String[] meisai30_val2 = new String[0];

	//＜画面入力以外＞

	/** 申請内容html */
	String shinseiHtml;

	/** 明細html */
	String meisaiHtml;

	/** HTML作成モード */
	int mode = HtmlCreateMode.WORKFLOW;

	/** 時刻一覧リスト */
	List<GMap> jikokuList;

	//＜部品＞

	/** ユーザー定義届書ロジック */
	KaniTodokeLogic kaniTodokeLogic;

	/** ユーザー定義届書カテゴリ　SELECT */
	KaniTodokeCategoryLogic kaniTodokeCategoryLogic;

	/** ユーザー定義届書一覧更新 */
	KaniTodokeIchiranUpdateLogic kaniTodokeIchiranLogic;

	/** 会計共通 */
	KaikeiCommonLogic commonLogic;

	/** ワークフローSELECT */
	WorkflowCategoryLogic workflowLogic;

	/** 伝票管理Logic */
	DenpyouKanriLogic denpyouKanriLogic;

	//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 * @param gamenInfo 画面項目情報
	 */
	protected void denpyouFormatCheck(KaniTodokeGamenInfo gamenInfo) {

		Map<Integer, Map<String, String>> value1 = gamenInfo.getValue1();
		String[][] option = gamenInfo.getOption();

		// 項目名とデータを紐づけるためにマッピングする
		Map<String, String[]> mapItemDataMap = gamenInfo.getItemLayout();

		for (Integer denpyou_edano : gamenInfo.getSortDenpyouEdano(value1)) {

			Map<String, String> mapValue = value1.get(denpyou_edano);

			for (String item_name : gamenInfo.getItemSortList(mapValue)) {

				String[] data = mapItemDataMap.get(item_name);

				String area_kbn = data[DataListIndex.AREA_KBN];                                        // エリア区分
				String max_length = data[DataListIndex.MAX_LENGTH];                                    // 最大桁数
				String min_value = data[DataListIndex.MIN_VALUE];                                      // 最小値
				String max_value = data[DataListIndex.MAX_VALUE];                                      // 最大値
				String label_name = data[DataListIndex.LABEL_NAME];                                    // ラベル名
				String buhin_type = data[DataListIndex.BUHIN_TYPE];                                    // 部品タイプ
				String buhin_format = data[DataListIndex.BUHIN_FORMAT];                                // 部品形式
				String master_kbn = data[DataListIndex.MASTER_KBN];                                    // マスター区分
				String yosanId = data[DataListIndex.VALUE1];                                           // 予算執行項目ID
				String value = mapValue.get(item_name) == null ? "" : mapValue.get(item_name);         // 値
				String decimal_point = data[DataListIndex.DECIMAL_POINT];                                 // 小数点以下桁数
				String kotei_hyouji = data[DataListIndex.KOTEI_HYOUJI];                                // 固定表示

				// エラーラベル名
				String errLableName = "";

				switch (area_kbn){
				case AREA_KBN.SHINSEI:
					errLableName = label_name;
					break;
				case AREA_KBN.MEISAI:
					errLableName = label_name + "：" + denpyou_edano + "行目";
					break;
				}

				// 部品タイプ別に形式チェック
				switch (buhin_type){
				case BuhinType.TEXT:
				case BuhinType.TEXTAREA:

					switch (buhin_format){
					case BUHIN_FORMAT.NUMBER:

						// 数値チェック
						checkNumberRangeDecimalPoint(value, Long.parseLong(min_value), Long.parseLong(max_value), Integer.parseInt(decimal_point), errLableName, false);
						break;

					case BUHIN_FORMAT.MONEY:

						// 金額チェック
						if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUSHI_SAGAKU.equals(yosanId)){
							// 収支差額がマイナスを許容する。
							checkKingakuMinus(value, errLableName, false);
						}else{
							checkKingakuOver0(value, errLableName, false);
						}
						break;

					case BUHIN_FORMAT.DATE:

						// 日付チェック
						checkDate(value, errLableName, false);
						break;

					case BUHIN_FORMAT.TIME:

						// 時刻チェック
						checkTime(value, errLableName, false);
						break;

					default:

						if (!kotei_hyouji.equals(EteamNaibuCodeSetting.KOTEI_HYOUJI.YES)) {
						// 文字列チェック
						checkString(value, 1, Integer.parseInt(max_length), errLableName, false);
						}
						break;
					}
					break;

				case BuhinType.CHECKBOX:

					// チェックボックスフラグチェック
					checkDomain(value, EteamConst.Domain.FLG, errLableName, false);
					break;

				case BuhinType.RADIO:
				case BuhinType.PULLDOWN:

					// ドメイン取得
					String[] domain = kaniTodokeLogic.getItemOption(item_name, option);

					// ドメインチェック
					checkDomain(value, domain, errLableName, false);
					break;

				case BuhinType.MASTER:

					ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData();

					switch (master_kbn){
					case MasterKbn.BUMON:
						shiwakeCheckData.futanBumonNm = errLableName;
						shiwakeCheckData.futanBumonCd = value;
						commonLogic.checkShiwake(shiwakeCheckData, errorList);
						commonLogic.checkFutanBumonWithSecurity(shiwakeCheckData.futanBumonCd, shiwakeCheckData.futanBumonNm, super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
						break;
					case MasterKbn.KAMOKU:
						shiwakeCheckData.kamokuNm = errLableName;
						shiwakeCheckData.kamokuCd = value;
						setShiwakeKamokuEdaban(mapItemDataMap, denpyou_edano, mapValue, area_kbn, yosanId,
								shiwakeCheckData, YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KAMOKU, YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_EDA);
						setShiwakeKamokuEdaban(mapItemDataMap, denpyou_edano, mapValue, area_kbn, yosanId,
								shiwakeCheckData, YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU, YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA);
						commonLogic.checkShiwake(shiwakeCheckData, errorList);
						break;
					case MasterKbn.TORIHIKISAKI:
						shiwakeCheckData.torihikisakiNm = errLableName;
						shiwakeCheckData.torihikisakiCd = value;
						commonLogic.checkShiwake(shiwakeCheckData, errorList);
						checkShiiresaki(errLableName, value, denpyouKbn); //仕入先チェック
						break;
					case MasterKbn.UF1:
						shiwakeCheckData.uf1Nm = errLableName;
						shiwakeCheckData.uf1Cd = value;
						commonLogic.checkShiwake(shiwakeCheckData, errorList);
						break;
					case MasterKbn.UF2:
						shiwakeCheckData.uf2Nm = errLableName;
						shiwakeCheckData.uf2Cd = value;
						commonLogic.checkShiwake(shiwakeCheckData, errorList);
						break;
					case MasterKbn.UF3:
						shiwakeCheckData.uf3Nm = errLableName;
						shiwakeCheckData.uf3Cd = value;
						commonLogic.checkShiwake(shiwakeCheckData, errorList);
						break;
					}
					break;
				}
			}
		}
	}

	/**
	 * 科目によって対応する科目枝番をセット
	 * @param mapItemDataMap データMAP
	 * @param denpyou_edano 枝番
	 * @param mapValue 値格納MAP
	 * @param area_kbn エリア区分
	 * @param yosanId 予算ID
	 * @param shiwakeCheckData 仕訳データ
	 * @param kamokuKbn 予算執行項目ID(科目)
	 * @param kamokuEdaKbn 予算執行項目ID(科目枝番)
	 */
	protected void setShiwakeKamokuEdaban(Map<String, String[]> mapItemDataMap, Integer denpyou_edano,
			Map<String, String> mapValue, String area_kbn, String yosanId, ShiwakeCheckData shiwakeCheckData,
			String kamokuKbn, String kamokuEdaKbn) {
		if (yosanId.equals(kamokuKbn)) {
			for (int i = 1; i <= mapItemDataMap.size(); i++) {
				String in = area_kbn + String.format("%02d", i);
				String[] d =  mapItemDataMap.get(in);
				if (d[DataListIndex.VALUE1].equals(kamokuEdaKbn)) {
					switch (area_kbn){
					case AREA_KBN.SHINSEI:
						shiwakeCheckData.kamokuEdabanNm = d[DataListIndex.LABEL_NAME];
						break;
					case AREA_KBN.MEISAI:
						shiwakeCheckData.kamokuEdabanNm = d[DataListIndex.LABEL_NAME] + "：" + denpyou_edano + "行目";
						break;
					}
					shiwakeCheckData.kamokuEdabanCd = mapValue.get(in) == null ? "" : mapValue.get(in);
				}
			}
		}
	}

	/**
	 * 伝票内部項目の必須チェック
	 * @param gamenInfo 画面項目情報
	 */
	protected void denpyouHissuCheck(KaniTodokeGamenInfo gamenInfo) {

		Map<Integer, Map<String, String>> value1 = gamenInfo.getValue1();

		// 項目名とデータを紐づけるためにマッピングする
		Map<String, String[]> mapItemDataMap = gamenInfo.getItemLayout();

		for (Integer denpyou_edano : gamenInfo.getSortDenpyouEdano(value1)) {

			Map<String, String> mapValue = value1.get(denpyou_edano);

			for (String item_name : gamenInfo.getItemSortList(mapValue)) {

				String[] data = mapItemDataMap.get(item_name);

				// 必須フラグ
				String hissu_flg = data[DataListIndex.HISSU_FLG];
				
				if (hissu_flg.equals("1")) {
					
					String area_kbn = data[DataListIndex.AREA_KBN];                                        // エリア区分
					String label_name = data[DataListIndex.LABEL_NAME];                                    // ラベル名
					String buhin_type = data[DataListIndex.BUHIN_TYPE];                                    // 部品タイプ
					String value = mapValue.get(item_name) == null ? "" : mapValue.get(item_name);         // 値

					// エラーラベル名
					String errLableName = "";

					switch (area_kbn){
					case AREA_KBN.SHINSEI:
						errLableName = label_name;
						break;
					case AREA_KBN.MEISAI:
						errLableName = label_name + "：" + denpyou_edano + "行目";
						break;
					}
					
					// チェックボックスで且つ、フラグOFFの場合
					if (buhin_type.equals(BuhinType.CHECKBOX) && value.equals("0")) {
						value = "";
					}
					
					hissuCheckCommon(new String[][]{{value,errLableName,"1"}}, 1);
				}
			}
		}
	}

	//＜伝票共通から呼ばれるイベント処理＞
	//個別伝票について表示処理を行う。
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {

		// 部品初期化
		initParts(connection);

		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		// 画面項目情報を取得する
		KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI,true);
		KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI,true);
		
		// 申請内容のレイアウトが存在しない場合、データなしエラー
		if(shinseiGamenInfo.getItemCount() <= 0) {
			throw new EteamDataNotFoundException();
		}

		// 画面項目を再構築する
		shinseiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.SHINSEI, shinseiGamenInfo, enableInput);
		meisaiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.MEISAI, meisaiGamenInfo, enableInput);
		
		// 制御用金額項目をセット
		setBumonSuishouRouteKingaku(shinseiGamenInfo);
	}

	//登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {

		// 部品初期化
		initParts(connection);

		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		// 画面項目情報を取得する
		KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI,false);
		KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI,false);

		// 必須チェック
		denpyouHissuCheck(shinseiGamenInfo);
		denpyouHissuCheck(meisaiGamenInfo);

		// 形式チェック
		denpyouFormatCheck(shinseiGamenInfo);
		denpyouFormatCheck(meisaiGamenInfo);

		if (0 < errorList.size()) {

			// 画面項目を再構築する
			shinseiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.SHINSEI, shinseiGamenInfo, enableInput);
			meisaiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.MEISAI, meisaiGamenInfo, enableInput);
		}
	}
	
	@Override
	protected void afterFileCheckError()
	{
		// 画面項目情報を取得する
		KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI, false);
		KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI, false);
		
		// 画面項目を再構築する
		shinseiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.SHINSEI, shinseiGamenInfo, enableInput);
		meisaiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.MEISAI, meisaiGamenInfo, enableInput);
	}

	//登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		// 部品初期化
		initParts(connection);

		// 画面項目情報を取得する
		KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI, false);
		KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI, false);

		kaniTodokeLogic.insertShinsei(denpyouId, denpyouKbn, version, shinseiGamenInfo, super.getUser().getTourokuOrKoushinUserId());
		kaniTodokeLogic.insertMeisai(denpyouId, meisaiGamenInfo, super.getUser().getTourokuOrKoushinUserId());
		kaniTodokeLogic.insertSummary(denpyouId, denpyouKbn, version, shinseiGamenInfo, meisaiGamenInfo);

		// 部門推奨ルート用金額値の設定
		setBumonSuishouRouteKingaku(shinseiGamenInfo);
	}

	//更新ボタン押下時に、個別伝票について以下を行う。
	//・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	//・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {

		// 部品初期化
		initParts(connection);

		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		// 画面項目情報を取得する
		KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI,false);
		KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI,false);

		// 必須チェック
		denpyouHissuCheck(shinseiGamenInfo);
		denpyouHissuCheck(meisaiGamenInfo);

		// 形式チェック
		denpyouFormatCheck(shinseiGamenInfo);
		denpyouFormatCheck(meisaiGamenInfo);

		if (0 < errorList.size()) {

			// 画面項目を再構築する
			shinseiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.SHINSEI, shinseiGamenInfo, enableInput);
			meisaiHtml = kaniTodokeLogic.createHtml(mode, AREA_KBN.MEISAI, meisaiGamenInfo, enableInput);
			return;
		}

		kaniTodokeLogic.updateShinsei(denpyouId, shinseiGamenInfo, super.getUser().getTourokuOrKoushinUserId());
		kaniTodokeLogic.deleteMeisai(denpyouId);
		kaniTodokeLogic.insertMeisai(denpyouId, meisaiGamenInfo, super.getUser().getTourokuOrKoushinUserId());
		kaniTodokeLogic.updateSummary(denpyouId, denpyouKbn, version, shinseiGamenInfo, meisaiGamenInfo);

		// 部門推奨ルート用金額値の設定
		setBumonSuishouRouteKingaku(shinseiGamenInfo);
	}

	//登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){
		// 処理なし
	}
	
	//ユーザー定義届書一覧を登録する
	@Override
	protected void insertKaniTodokeIchiran(EteamConnection connection){
		
		// ユーザー定義届書一覧を登録
		kaniTodokeIchiranLogic.deleteKaniTodokeIchiran(denpyouId);
		kaniTodokeIchiranLogic.insertKaniTodokeIchiran(denpyouId);
		kaniTodokeIchiranLogic.updateKaniTodokeIchiran(denpyouId);

		// ユーザー定義届書明細一覧を登録
		kaniTodokeIchiranLogic.deleteKaniTodokeMeisaiIchiran(denpyouId);
		kaniTodokeIchiranLogic.insertKaniTodokeMeisaiIchiran(denpyouId);
		kaniTodokeIchiranLogic.updateKaniTodokeMeisaiIchiran(denpyouId);
	}

	//支出科目と支出部門の集計部門が予算執行対象かどうか判定する。
	@Override
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection connection) {
		
		boolean isKamokuTani = EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.YOSAN_CHECK_TANI));
		
		if(null == commonLogic){
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		}
		if(null == kaniTodokeLogic){
			kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, connection);
		}

		if(isKamokuTani){ 
			// 集計単位=部門科目
			String itemName = kaniTodokeLogic.getYosanShikkouItemName(denpyouKbn, version, YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU);
			String itemVal1[] = retVal1(itemName);
			if(itemVal1 == null) { return true; }
			
			HashSet<String> kamokuCdSet = new HashSet<String>();
			for(int i = 0 ; i < itemVal1.length ; i++){
				if(isNotEmpty(itemVal1[i])){ kamokuCdSet.add(itemVal1[i]); }
			}
			
			if(0 == kamokuCdSet.size()) { return true; }
			return commonLogic.isYosanShikkouKamoku(denpyouId, kamokuCdSet, kianbangouBoDialogNendo);
		}else{
			// 集計単位=部門単位
			String itemName = kaniTodokeLogic.getYosanShikkouItemName(denpyouKbn, version, YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_BUMON);
			String itemVal1[] = retVal1(itemName);
			if(itemVal1 == null) { return true; }
			
			HashSet<String> bumonCdSet = new HashSet<String>();
			for(int i = 0 ; i < itemVal1.length ; i++){
				if(isNotEmpty(itemVal1[i])){ bumonCdSet.add(itemVal1[i]); }
			}
			
			if(0 == bumonCdSet.size()) { return true; }
			return commonLogic.isYosanShikkouBumon(denpyouId, bumonCdSet, kianbangouBoDialogNendo);
		}
	}
	
//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		// ユーザー定義届書Logic
		kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, connection);
		// ユーザー定義届書内のSelect文を集約したLogic
		kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		// ユーザー定義届書一覧ロジック
		kaniTodokeIchiranLogic = EteamContainer.getComponent(KaniTodokeIchiranUpdateLogic.class);;
		kaniTodokeIchiranLogic.init(connection);
		// 会計共通Logic
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		// ワークフローカテゴリー内のSelect文を集約したLogic
		workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		// 伝票管理Logic
		denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		// 内部コード取得
		SystemKanriCategoryLogic sysLc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		jikokuList = sysLc.loadNaibuCdSetting("jikoku_ichiran");
	}

	/** ユーザー定義届書の画面項目情報を作成する
	 * @param areaKbn エリア区分
	 * @param isInit 初期処理かどうかの判定
	 * @return 画面項目情報
	 */
	protected KaniTodokeGamenInfo createKaniTodokeGamenInfo(String areaKbn, Boolean isInit) {

		// 初期表示の場合、データ取得およびメンバー変数に値設定
		if (isInit) {

			String denpyouIdTmp = "";

			if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {
				// 新規起票(伝票IDも参照伝票IDも表示パラメータに含まれない場合)
				maxDenpyouEdaNo = 1;
			} else {

				//登録済伝票
				if (isNotEmpty(super.denpyouId)) {
					denpyouIdTmp = denpyouId;
				} 

				//参照起票
				if (isNotEmpty(super.sanshouDenpyouId)) {
					denpyouIdTmp = sanshouDenpyouId;

					//参照起票
					sanshouVersion = version;
					version = String.valueOf(kaniTodokeCategoryLogic.findMaxVersion(denpyouKbn));
				}

				maxDenpyouEdaNo = kaniTodokeCategoryLogic.getMaxDenpyouEdaNo(denpyouIdTmp, denpyouKbn, version, areaKbn);
			}

			// 明細項目値の配列初期化
			if (areaKbn.equals(AREA_KBN.MEISAI)) {
				initMeisaiValueArray(maxDenpyouEdaNo);
			}

			// データ取得
			List<GMap> listMapData;

			if (isNotEmpty(super.sanshouDenpyouId)) {
				listMapData = kaniTodokeCategoryLogic.loadSanshouData(areaKbn, denpyouKbn, version, denpyouIdTmp, true);
			} else {
				listMapData = kaniTodokeCategoryLogic.loadData(areaKbn, denpyouKbn, version, denpyouIdTmp);
			}

			// データをメンバー変数に設定する
			setMemberValue(listMapData);
		} 

		// レイアウト・オプション取得
		List<GMap> listMapLayout = kaniTodokeCategoryLogic.loadLayout(areaKbn, denpyouKbn, version);
		List<GMap> listMapOption = kaniTodokeCategoryLogic.loadOption(areaKbn, denpyouKbn, version);
		Map<String, GMap> defaultDataMap = CollectionUtil.list2Map(kaniTodokeCategoryLogic.loadData(areaKbn, denpyouKbn, version, ""), "item_name");
		Map<String, GMap> layoutMap  = CollectionUtil.list2Map(listMapLayout, "item_name");

		// リストマップを１次元配列へ変換する
		String[] layoutArray = kaniTodokeLogic.toArrayListMapData(listMapLayout);
		String[] optionArray = kaniTodokeLogic.toArrayListMapOption(listMapOption);

		// 配列の１次元から２次元への変換
		String[][] layoutArray2D = toArray2D(layoutArray);
		String[][] optionArray2D = toArray2D(optionArray);

		// 項目名の配列の取得
		String[] itemNameArray = getItemNameArray(layoutArray2D);
		
		Map<Integer, Map<String, String>> mapValue1 = new HashMap<Integer, Map<String, String>>();
		Map<Integer, Map<String, String>> mapValue2 = new HashMap<Integer, Map<String, String>>();

		switch (areaKbn){
		case AREA_KBN.SHINSEI:
			// 値を項目名に紐付けるためにマッピングする
			mapItemValue(1, itemNameArray, mapValue1, mapValue2, layoutMap, defaultDataMap);
			break;

		case AREA_KBN.MEISAI:
			// 値を項目名に紐付けるためにマッピングする
			mapItemValue(maxDenpyouEdaNo, itemNameArray, mapValue1, mapValue2, layoutMap, defaultDataMap);
			break;
		}

		// 項目名とデータを紐づけるためにマッピングする
		Map<String, String[]> mapItemData = kaniTodokeLogic.mapItemLayout(layoutArray2D);

		// 項目名とオプションを紐づけるためにマッピングする
		Map<String, List<String[]>> mapOption = kaniTodokeLogic.mapItemOption(optionArray2D);

		KaniTodokeGamenInfo kaniTodokeGamenInfo = new KaniTodokeGamenInfo();

		kaniTodokeGamenInfo.setLayout(layoutArray2D);
		kaniTodokeGamenInfo.setOption(optionArray2D);
		kaniTodokeGamenInfo.setValue1(mapValue1);
		kaniTodokeGamenInfo.setValue2(mapValue2);
		kaniTodokeGamenInfo.setItemNames(itemNameArray);
		kaniTodokeGamenInfo.setItemLayout(mapItemData);
		kaniTodokeGamenInfo.setItemOption(mapOption);

		return kaniTodokeGamenInfo;
	}

	/**
	 * 値をメンバ変数に設定する
	 * @param listMap リストマップ
	 */
	protected void setMemberValue(List<GMap> listMap)
	{
		for(GMap map : listMap) {

			Integer denpyou_edano = Integer.parseInt(map.get("denpyou_edano") == null ? "1" : map.get("denpyou_edano").toString());
			String item_name = map.get("item_name") == null ? "" : map.get("item_name").toString();
			String value1 = map.get("value1") == null ? "" : map.get("value1").toString();
			String value2 = map.get("value2") == null ? "" : map.get("value2").toString();

			// 入力値をメンバ変数に設定する
			setMemberValue(value1, value2, item_name, denpyou_edano);
		}
	}

	/**
	 * 値をメンバ変数に設定する
	 * @param value1 値1
	 * @param value2 値2
	 * @param item_name 項目名
	 * @param denpyou_edano 伝票枝番号
	 */
	protected void setMemberValue(String value1, String value2, String item_name, Integer denpyou_edano){

		if(item_name.equals("shinsei01")) {shinsei01_val1 = value1; shinsei01_val2 = value2;}
		if(item_name.equals("shinsei02")) {shinsei02_val1 = value1; shinsei02_val2 = value2;}
		if(item_name.equals("shinsei03")) {shinsei03_val1 = value1; shinsei03_val2 = value2;}
		if(item_name.equals("shinsei04")) {shinsei04_val1 = value1; shinsei04_val2 = value2;}
		if(item_name.equals("shinsei05")) {shinsei05_val1 = value1; shinsei05_val2 = value2;}
		if(item_name.equals("shinsei06")) {shinsei06_val1 = value1; shinsei06_val2 = value2;}
		if(item_name.equals("shinsei07")) {shinsei07_val1 = value1; shinsei07_val2 = value2;}
		if(item_name.equals("shinsei08")) {shinsei08_val1 = value1; shinsei08_val2 = value2;}
		if(item_name.equals("shinsei09")) {shinsei09_val1 = value1; shinsei09_val2 = value2;}
		if(item_name.equals("shinsei10")) {shinsei10_val1 = value1; shinsei10_val2 = value2;}
		if(item_name.equals("shinsei11")) {shinsei11_val1 = value1; shinsei11_val2 = value2;}
		if(item_name.equals("shinsei12")) {shinsei12_val1 = value1; shinsei12_val2 = value2;}
		if(item_name.equals("shinsei13")) {shinsei13_val1 = value1; shinsei13_val2 = value2;}
		if(item_name.equals("shinsei14")) {shinsei14_val1 = value1; shinsei14_val2 = value2;}
		if(item_name.equals("shinsei15")) {shinsei15_val1 = value1; shinsei15_val2 = value2;}
		if(item_name.equals("shinsei16")) {shinsei16_val1 = value1; shinsei16_val2 = value2;}
		if(item_name.equals("shinsei17")) {shinsei17_val1 = value1; shinsei17_val2 = value2;}
		if(item_name.equals("shinsei18")) {shinsei18_val1 = value1; shinsei18_val2 = value2;}
		if(item_name.equals("shinsei19")) {shinsei19_val1 = value1; shinsei19_val2 = value2;}
		if(item_name.equals("shinsei20")) {shinsei20_val1 = value1; shinsei20_val2 = value2;}
		if(item_name.equals("shinsei21")) {shinsei21_val1 = value1; shinsei21_val2 = value2;}
		if(item_name.equals("shinsei22")) {shinsei22_val1 = value1; shinsei22_val2 = value2;}
		if(item_name.equals("shinsei23")) {shinsei23_val1 = value1; shinsei23_val2 = value2;}
		if(item_name.equals("shinsei24")) {shinsei24_val1 = value1; shinsei24_val2 = value2;}
		if(item_name.equals("shinsei25")) {shinsei25_val1 = value1; shinsei25_val2 = value2;}
		if(item_name.equals("shinsei26")) {shinsei26_val1 = value1; shinsei26_val2 = value2;}
		if(item_name.equals("shinsei27")) {shinsei27_val1 = value1; shinsei27_val2 = value2;}
		if(item_name.equals("shinsei28")) {shinsei28_val1 = value1; shinsei28_val2 = value2;}
		if(item_name.equals("shinsei29")) {shinsei29_val1 = value1; shinsei29_val2 = value2;}
		if(item_name.equals("shinsei30")) {shinsei30_val1 = value1; shinsei30_val2 = value2;}

		if (denpyou_edano != null) {

			Integer idx = denpyou_edano - 1;

			if(item_name.equals("meisai01")) {meisai01_val1[idx] = value1; meisai01_val2[idx] = value2;}
			if(item_name.equals("meisai02")) {meisai02_val1[idx] = value1; meisai02_val2[idx] = value2;}
			if(item_name.equals("meisai03")) {meisai03_val1[idx] = value1; meisai03_val2[idx] = value2;}
			if(item_name.equals("meisai04")) {meisai04_val1[idx] = value1; meisai04_val2[idx] = value2;}
			if(item_name.equals("meisai05")) {meisai05_val1[idx] = value1; meisai05_val2[idx] = value2;}
			if(item_name.equals("meisai06")) {meisai06_val1[idx] = value1; meisai06_val2[idx] = value2;}
			if(item_name.equals("meisai07")) {meisai07_val1[idx] = value1; meisai07_val2[idx] = value2;}
			if(item_name.equals("meisai08")) {meisai08_val1[idx] = value1; meisai08_val2[idx] = value2;}
			if(item_name.equals("meisai09")) {meisai09_val1[idx] = value1; meisai09_val2[idx] = value2;}
			if(item_name.equals("meisai10")) {meisai10_val1[idx] = value1; meisai10_val2[idx] = value2;}
			if(item_name.equals("meisai11")) {meisai11_val1[idx] = value1; meisai11_val2[idx] = value2;}
			if(item_name.equals("meisai12")) {meisai12_val1[idx] = value1; meisai12_val2[idx] = value2;}
			if(item_name.equals("meisai13")) {meisai13_val1[idx] = value1; meisai13_val2[idx] = value2;}
			if(item_name.equals("meisai14")) {meisai14_val1[idx] = value1; meisai14_val2[idx] = value2;}
			if(item_name.equals("meisai15")) {meisai15_val1[idx] = value1; meisai15_val2[idx] = value2;}
			if(item_name.equals("meisai16")) {meisai16_val1[idx] = value1; meisai16_val2[idx] = value2;}
			if(item_name.equals("meisai17")) {meisai17_val1[idx] = value1; meisai17_val2[idx] = value2;}
			if(item_name.equals("meisai18")) {meisai18_val1[idx] = value1; meisai18_val2[idx] = value2;}
			if(item_name.equals("meisai19")) {meisai19_val1[idx] = value1; meisai19_val2[idx] = value2;}
			if(item_name.equals("meisai20")) {meisai20_val1[idx] = value1; meisai20_val2[idx] = value2;}
			if(item_name.equals("meisai21")) {meisai21_val1[idx] = value1; meisai21_val2[idx] = value2;}
			if(item_name.equals("meisai22")) {meisai22_val1[idx] = value1; meisai22_val2[idx] = value2;}
			if(item_name.equals("meisai23")) {meisai23_val1[idx] = value1; meisai23_val2[idx] = value2;}
			if(item_name.equals("meisai24")) {meisai24_val1[idx] = value1; meisai24_val2[idx] = value2;}
			if(item_name.equals("meisai25")) {meisai25_val1[idx] = value1; meisai25_val2[idx] = value2;}
			if(item_name.equals("meisai26")) {meisai26_val1[idx] = value1; meisai26_val2[idx] = value2;}
			if(item_name.equals("meisai27")) {meisai27_val1[idx] = value1; meisai27_val2[idx] = value2;}
			if(item_name.equals("meisai28")) {meisai28_val1[idx] = value1; meisai28_val2[idx] = value2;}
			if(item_name.equals("meisai29")) {meisai29_val1[idx] = value1; meisai29_val2[idx] = value2;}
			if(item_name.equals("meisai30")) {meisai30_val1[idx] = value1; meisai30_val2[idx] = value2;}
		}
	}

	/** 入力値を項目名に紐付けるためにマッピングする
	 * @param denpyouEdaNo 最大伝票枝番号
	 * @param itemNameArray 項目名配列
	 * @param mapValue1 値１マップ
	 * @param mapValue2 値２マップ
	 * @param layoutMap レイアウトのマップ<項目名, buhinFormat等含むマップ>
	 * @param defaultDataMap デフォルト値のマップ<項目名, default_value1等含むマップ>
	 */
	protected void mapItemValue(Integer denpyouEdaNo, String[] itemNameArray, Map<Integer, Map<String, String>> mapValue1, Map<Integer, Map<String, String>> mapValue2, Map<String, GMap> layoutMap, Map<String, GMap> defaultDataMap)
	{
		for (int no = 1; no <= denpyouEdaNo; no++) {

			for (String item_name : itemNameArray) {

				// 入力値を項目名に紐付けるためにマッピングする
				mapItemValue(mapValue1, mapValue2, item_name, no, layoutMap, defaultDataMap);
			}
		}
	}

	/** 入力値を項目名に紐付けるためにマッピングする
	 * @param mapValue1 値１マップ
	 * @param mapValue2 値２マップ
	 * @param item_name 項目名
	 * @param denpyou_edano 伝票枝番号
	 * @param layoutMap レイアウトのマップ<項目名, buhinFormat等含むマップ>
	 * @param defaultDataMap デフォルト値のマップ<項目名, default_value1等含むマップ>
	 */
	protected void mapItemValue(Map<Integer, Map<String, String>> mapValue1, Map<Integer, Map<String, String>> mapValue2, String item_name, Integer denpyou_edano, Map<String, GMap> layoutMap, Map<String, GMap> defaultDataMap)
	{
		if (denpyou_edano != null) {

			if(!mapValue1.containsKey(denpyou_edano)) {
				mapValue1.put(denpyou_edano, new HashMap<String, String>());
			}

			if(!mapValue2.containsKey(denpyou_edano)) {
				mapValue2.put(denpyou_edano, new HashMap<String, String>());
			}

			Map<String, String> mapValue1Tmp = mapValue1.get(denpyou_edano);
			Map<String, String> mapValue2Tmp = mapValue2.get(denpyou_edano);

			if(!mapValue1Tmp.containsKey(item_name) && !mapValue2Tmp.containsKey(item_name)) {
				//固定表示の場合はkani_todoke_koumoku.default_value1の値を再設定
				GMap layout = layoutMap.get(item_name);
				GMap defaultData = defaultDataMap.get(item_name);
				if("1".equals(layout.get("kotei_hyouji"))){
					mapValue1Tmp.put(item_name, (String)defaultData.get("value1"));
					return;
				}
				if(item_name.equals("shinsei01")){mapValue1Tmp.put(item_name, shinsei01_val1);mapValue2Tmp.put(item_name, shinsei01_val2);}
				if(item_name.equals("shinsei02")){mapValue1Tmp.put(item_name, shinsei02_val1);mapValue2Tmp.put(item_name, shinsei02_val2);}
				if(item_name.equals("shinsei03")){mapValue1Tmp.put(item_name, shinsei03_val1);mapValue2Tmp.put(item_name, shinsei03_val2);}
				if(item_name.equals("shinsei04")){mapValue1Tmp.put(item_name, shinsei04_val1);mapValue2Tmp.put(item_name, shinsei04_val2);}
				if(item_name.equals("shinsei05")){mapValue1Tmp.put(item_name, shinsei05_val1);mapValue2Tmp.put(item_name, shinsei05_val2);}
				if(item_name.equals("shinsei06")){mapValue1Tmp.put(item_name, shinsei06_val1);mapValue2Tmp.put(item_name, shinsei06_val2);}
				if(item_name.equals("shinsei07")){mapValue1Tmp.put(item_name, shinsei07_val1);mapValue2Tmp.put(item_name, shinsei07_val2);}
				if(item_name.equals("shinsei08")){mapValue1Tmp.put(item_name, shinsei08_val1);mapValue2Tmp.put(item_name, shinsei08_val2);}
				if(item_name.equals("shinsei09")){mapValue1Tmp.put(item_name, shinsei09_val1);mapValue2Tmp.put(item_name, shinsei09_val2);}
				if(item_name.equals("shinsei10")){mapValue1Tmp.put(item_name, shinsei10_val1);mapValue2Tmp.put(item_name, shinsei10_val2);}
				if(item_name.equals("shinsei11")){mapValue1Tmp.put(item_name, shinsei11_val1);mapValue2Tmp.put(item_name, shinsei11_val2);}
				if(item_name.equals("shinsei12")){mapValue1Tmp.put(item_name, shinsei12_val1);mapValue2Tmp.put(item_name, shinsei12_val2);}
				if(item_name.equals("shinsei13")){mapValue1Tmp.put(item_name, shinsei13_val1);mapValue2Tmp.put(item_name, shinsei13_val2);}
				if(item_name.equals("shinsei14")){mapValue1Tmp.put(item_name, shinsei14_val1);mapValue2Tmp.put(item_name, shinsei14_val2);}
				if(item_name.equals("shinsei15")){mapValue1Tmp.put(item_name, shinsei15_val1);mapValue2Tmp.put(item_name, shinsei15_val2);}
				if(item_name.equals("shinsei16")){mapValue1Tmp.put(item_name, shinsei16_val1);mapValue2Tmp.put(item_name, shinsei16_val2);}
				if(item_name.equals("shinsei17")){mapValue1Tmp.put(item_name, shinsei17_val1);mapValue2Tmp.put(item_name, shinsei17_val2);}
				if(item_name.equals("shinsei18")){mapValue1Tmp.put(item_name, shinsei18_val1);mapValue2Tmp.put(item_name, shinsei18_val2);}
				if(item_name.equals("shinsei19")){mapValue1Tmp.put(item_name, shinsei19_val1);mapValue2Tmp.put(item_name, shinsei19_val2);}
				if(item_name.equals("shinsei20")){mapValue1Tmp.put(item_name, shinsei20_val1);mapValue2Tmp.put(item_name, shinsei20_val2);}
				if(item_name.equals("shinsei21")){mapValue1Tmp.put(item_name, shinsei21_val1);mapValue2Tmp.put(item_name, shinsei21_val2);}
				if(item_name.equals("shinsei22")){mapValue1Tmp.put(item_name, shinsei22_val1);mapValue2Tmp.put(item_name, shinsei22_val2);}
				if(item_name.equals("shinsei23")){mapValue1Tmp.put(item_name, shinsei23_val1);mapValue2Tmp.put(item_name, shinsei23_val2);}
				if(item_name.equals("shinsei24")){mapValue1Tmp.put(item_name, shinsei24_val1);mapValue2Tmp.put(item_name, shinsei24_val2);}
				if(item_name.equals("shinsei25")){mapValue1Tmp.put(item_name, shinsei25_val1);mapValue2Tmp.put(item_name, shinsei25_val2);}
				if(item_name.equals("shinsei26")){mapValue1Tmp.put(item_name, shinsei26_val1);mapValue2Tmp.put(item_name, shinsei26_val2);}
				if(item_name.equals("shinsei27")){mapValue1Tmp.put(item_name, shinsei27_val1);mapValue2Tmp.put(item_name, shinsei27_val2);}
				if(item_name.equals("shinsei28")){mapValue1Tmp.put(item_name, shinsei28_val1);mapValue2Tmp.put(item_name, shinsei28_val2);}
				if(item_name.equals("shinsei29")){mapValue1Tmp.put(item_name, shinsei29_val1);mapValue2Tmp.put(item_name, shinsei29_val2);}
				if(item_name.equals("shinsei30")){mapValue1Tmp.put(item_name, shinsei30_val1);mapValue2Tmp.put(item_name, shinsei30_val2);}

				Integer idx = denpyou_edano - 1;

				if(item_name.equals("meisai01") && meisai01_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai01_val1[idx]);}
				if(item_name.equals("meisai02") && meisai02_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai02_val1[idx]);}
				if(item_name.equals("meisai03") && meisai03_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai03_val1[idx]);}
				if(item_name.equals("meisai04") && meisai04_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai04_val1[idx]);}
				if(item_name.equals("meisai05") && meisai05_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai05_val1[idx]);}
				if(item_name.equals("meisai06") && meisai06_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai06_val1[idx]);}
				if(item_name.equals("meisai07") && meisai07_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai07_val1[idx]);}
				if(item_name.equals("meisai08") && meisai08_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai08_val1[idx]);}
				if(item_name.equals("meisai09") && meisai09_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai09_val1[idx]);}
				if(item_name.equals("meisai10") && meisai10_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai10_val1[idx]);}
				if(item_name.equals("meisai11") && meisai11_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai11_val1[idx]);}
				if(item_name.equals("meisai12") && meisai12_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai12_val1[idx]);}
				if(item_name.equals("meisai13") && meisai13_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai13_val1[idx]);}
				if(item_name.equals("meisai14") && meisai14_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai14_val1[idx]);}
				if(item_name.equals("meisai15") && meisai15_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai15_val1[idx]);}
				if(item_name.equals("meisai16") && meisai16_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai16_val1[idx]);}
				if(item_name.equals("meisai17") && meisai17_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai17_val1[idx]);}
				if(item_name.equals("meisai18") && meisai18_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai18_val1[idx]);}
				if(item_name.equals("meisai19") && meisai19_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai19_val1[idx]);}
				if(item_name.equals("meisai20") && meisai20_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai20_val1[idx]);}
				if(item_name.equals("meisai21") && meisai21_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai21_val1[idx]);}
				if(item_name.equals("meisai22") && meisai22_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai22_val1[idx]);}
				if(item_name.equals("meisai23") && meisai23_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai23_val1[idx]);}
				if(item_name.equals("meisai24") && meisai24_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai24_val1[idx]);}
				if(item_name.equals("meisai25") && meisai25_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai25_val1[idx]);}
				if(item_name.equals("meisai26") && meisai26_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai26_val1[idx]);}
				if(item_name.equals("meisai27") && meisai27_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai27_val1[idx]);}
				if(item_name.equals("meisai28") && meisai28_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai28_val1[idx]);}
				if(item_name.equals("meisai29") && meisai29_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai29_val1[idx]);}
				if(item_name.equals("meisai30") && meisai30_val1.length >= denpyou_edano) {mapValue1Tmp.put(item_name, meisai30_val1[idx]);}

				if(item_name.equals("meisai01") && meisai01_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai01_val2[idx]);}
				if(item_name.equals("meisai02") && meisai02_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai02_val2[idx]);}
				if(item_name.equals("meisai03") && meisai03_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai03_val2[idx]);}
				if(item_name.equals("meisai04") && meisai04_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai04_val2[idx]);}
				if(item_name.equals("meisai05") && meisai05_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai05_val2[idx]);}
				if(item_name.equals("meisai06") && meisai06_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai06_val2[idx]);}
				if(item_name.equals("meisai07") && meisai07_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai07_val2[idx]);}
				if(item_name.equals("meisai08") && meisai08_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai08_val2[idx]);}
				if(item_name.equals("meisai09") && meisai09_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai09_val2[idx]);}
				if(item_name.equals("meisai10") && meisai10_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai10_val2[idx]);}
				if(item_name.equals("meisai11") && meisai11_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai11_val2[idx]);}
				if(item_name.equals("meisai12") && meisai12_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai12_val2[idx]);}
				if(item_name.equals("meisai13") && meisai13_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai13_val2[idx]);}
				if(item_name.equals("meisai14") && meisai14_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai14_val2[idx]);}
				if(item_name.equals("meisai15") && meisai15_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai15_val2[idx]);}
				if(item_name.equals("meisai16") && meisai16_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai16_val2[idx]);}
				if(item_name.equals("meisai17") && meisai17_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai17_val2[idx]);}
				if(item_name.equals("meisai18") && meisai18_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai18_val2[idx]);}
				if(item_name.equals("meisai19") && meisai19_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai19_val2[idx]);}
				if(item_name.equals("meisai20") && meisai20_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai20_val2[idx]);}
				if(item_name.equals("meisai21") && meisai21_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai21_val2[idx]);}
				if(item_name.equals("meisai22") && meisai22_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai22_val2[idx]);}
				if(item_name.equals("meisai23") && meisai23_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai23_val2[idx]);}
				if(item_name.equals("meisai24") && meisai24_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai24_val2[idx]);}
				if(item_name.equals("meisai25") && meisai25_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai25_val2[idx]);}
				if(item_name.equals("meisai26") && meisai26_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai26_val2[idx]);}
				if(item_name.equals("meisai27") && meisai27_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai27_val2[idx]);}
				if(item_name.equals("meisai28") && meisai28_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai28_val2[idx]);}
				if(item_name.equals("meisai29") && meisai29_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai29_val2[idx]);}
				if(item_name.equals("meisai30") && meisai30_val2.length >= denpyou_edano) {mapValue2Tmp.put(item_name, meisai30_val2[idx]);}
			}
		}
	}

	/**
	 * 明細項目値の配列初期化
	 * @param denpyouEdaNo 最大伝票枝番号
	 */
	protected void initMeisaiValueArray(Integer denpyouEdaNo) {

		meisai01_val1 = new String[denpyouEdaNo]; meisai01_val2 = new String[denpyouEdaNo];
		meisai02_val1 = new String[denpyouEdaNo]; meisai02_val2 = new String[denpyouEdaNo];
		meisai03_val1 = new String[denpyouEdaNo]; meisai03_val2 = new String[denpyouEdaNo];
		meisai04_val1 = new String[denpyouEdaNo]; meisai04_val2 = new String[denpyouEdaNo];
		meisai05_val1 = new String[denpyouEdaNo]; meisai05_val2 = new String[denpyouEdaNo];
		meisai06_val1 = new String[denpyouEdaNo]; meisai06_val2 = new String[denpyouEdaNo];
		meisai07_val1 = new String[denpyouEdaNo]; meisai07_val2 = new String[denpyouEdaNo];
		meisai08_val1 = new String[denpyouEdaNo]; meisai08_val2 = new String[denpyouEdaNo];
		meisai09_val1 = new String[denpyouEdaNo]; meisai09_val2 = new String[denpyouEdaNo];
		meisai10_val1 = new String[denpyouEdaNo]; meisai10_val2 = new String[denpyouEdaNo];
		meisai11_val1 = new String[denpyouEdaNo]; meisai11_val2 = new String[denpyouEdaNo];
		meisai12_val1 = new String[denpyouEdaNo]; meisai12_val2 = new String[denpyouEdaNo];
		meisai13_val1 = new String[denpyouEdaNo]; meisai13_val2 = new String[denpyouEdaNo];
		meisai14_val1 = new String[denpyouEdaNo]; meisai14_val2 = new String[denpyouEdaNo];
		meisai15_val1 = new String[denpyouEdaNo]; meisai15_val2 = new String[denpyouEdaNo];
		meisai16_val1 = new String[denpyouEdaNo]; meisai16_val2 = new String[denpyouEdaNo];
		meisai17_val1 = new String[denpyouEdaNo]; meisai17_val2 = new String[denpyouEdaNo];
		meisai18_val1 = new String[denpyouEdaNo]; meisai18_val2 = new String[denpyouEdaNo];
		meisai19_val1 = new String[denpyouEdaNo]; meisai19_val2 = new String[denpyouEdaNo];
		meisai20_val1 = new String[denpyouEdaNo]; meisai20_val2 = new String[denpyouEdaNo];
		meisai21_val1 = new String[denpyouEdaNo]; meisai21_val2 = new String[denpyouEdaNo];
		meisai22_val1 = new String[denpyouEdaNo]; meisai22_val2 = new String[denpyouEdaNo];
		meisai23_val1 = new String[denpyouEdaNo]; meisai23_val2 = new String[denpyouEdaNo];
		meisai24_val1 = new String[denpyouEdaNo]; meisai24_val2 = new String[denpyouEdaNo];
		meisai25_val1 = new String[denpyouEdaNo]; meisai25_val2 = new String[denpyouEdaNo];
		meisai26_val1 = new String[denpyouEdaNo]; meisai26_val2 = new String[denpyouEdaNo];
		meisai27_val1 = new String[denpyouEdaNo]; meisai27_val2 = new String[denpyouEdaNo];
		meisai28_val1 = new String[denpyouEdaNo]; meisai28_val2 = new String[denpyouEdaNo];
		meisai29_val1 = new String[denpyouEdaNo]; meisai29_val2 = new String[denpyouEdaNo];
		meisai30_val1 = new String[denpyouEdaNo]; meisai30_val2 = new String[denpyouEdaNo];
	}

	/**
	 * ２次元配列化
	 * @param array １次元配列
	 * @return ２次元配列
	 */
	protected String[][] toArray2D(String[] array)
	{
		String[][] array2D = new String[0][0];
		if (array != null && array.length > 0) {
			array2D = new String[array.length][];
			for (int i = 0; i < array.length; i++) {
				array2D[i] = array[i].split("\r\n", -1);
			}
		}
		return array2D;
	}

	/**
	 * 項目名配列を取得する
	 * @param dataArray データ配列
	 * @return 項目名配列
	 */
	protected String[] getItemNameArray(String[][] dataArray)
	{
		List<String> itemNameList = new ArrayList<String>();

		for (int i = 0; i < dataArray.length; i++) {
			String item_name = dataArray[i][DataListIndex.ITEM_NAME];
			if(!itemNameList.contains(item_name)) {
				itemNameList.add(item_name);
			}
		}

		return itemNameList.toArray(new String[0]);
	}
	
	/**
	 * ユーザー定義届書EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */ 
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		initParts(connection);
		version = workflowLogic.selectDenpyou(denpyouId).get("version").toString();
		KanitodokeXlsLogic kanitodokexlsLogic = EteamContainer.getComponent(KanitodokeXlsLogic.class, connection);
		kanitodokexlsLogic.makeExcel(denpyouId, out, version, denpyouKbn, sokyuuFlg);
	}

	/**
	 * 部門推奨ルート用金額値設定<br>
	 * 伝票管理に設定された「ルート判定金額」が「分岐なし」以外の場合に
	 * 対象項目の金額値を基底クラスの金額に設定する。
	 * 
	 * @param shinseiGamenInfo 申請内容のユーザー定義届書画面情報
	 */
	protected void setBumonSuishouRouteKingaku(KaniTodokeGamenInfo shinseiGamenInfo){

		// 伝票管理に設定された「ルート判定金額」の指定値を取得して、対応する予算執行項目IDを決定する。
		String[] kingakuId = null;
		String routeHanteiKingakuCd = denpyouKanriLogic.getRouteHanteiKingaku(denpyouKbn);
		switch (routeHanteiKingakuCd){
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.RINGI_KINGAKU:
			kingakuId = new String[1];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHISHUTSU_GOUKEI:
			kingakuId = new String[1];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHISHUTSU_SHUUNYUU:
			kingakuId = new String[2];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			kingakuId[1] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHUUNYUU_GOUKEI:
			kingakuId = new String[1];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			break;
		default:
			break;
		}

		// ルート判定金額が「分岐なし」以外の場合に金額値の設定を行う。
		if (null != kingakuId){

			// ユーザー定義届書画面情報からレイアウトを取得する。
			String[][] layout = shinseiGamenInfo.getLayout();

			// レイアウトから予算執行項目IDに合致する入力値を取得する。
			String[] kingakuVal = new String[kingakuId.length];
			for (int i = 0; i < kingakuId.length; i++){
				kingakuVal[i] = "0";
				int kingakuPos = 0;
				for (; kingakuPos < layout.length; kingakuPos++){
					if (layout[kingakuPos][KaniTodokeConst.DataListIndex.VALUE1].equals(kingakuId[i])){
						// ユーザー定義届書画面情報のアイテムリストからレイアウトと同じ配列番目のアイテム名を取得
						String itemName = shinseiGamenInfo.getItemNames()[kingakuPos];
						// ユーザー定義届書画面情報のデータ値１から申請内容のマップを取得
						Map<String, String> valueMap = shinseiGamenInfo.getValue1().get(1);
						// マップからアイテム名をキーに入力値を取得
						if (!super.isEmpty(valueMap.get(itemName))){
							kingakuVal[i] = valueMap.get(itemName);
						}
						break;
					}
				}
			}

			// 部門推奨ルート用金額値を設定する。
			if (1 == kingakuVal.length){
				super.setKingaku(kingakuVal[0].replace(",", ""));
			}else{
				// 項目が２つ有るものは値が大きい方を設定
				long value1 = Long.parseLong(kingakuVal[0].replace(",", ""));
				long value2 = Long.parseLong(kingakuVal[1].replace(",", ""));
				if (value1 < value2){
					super.setKingaku(String.valueOf(value2));
				}else{
					super.setKingaku(String.valueOf(value1));
				}
			}
		}
	}
	
	/**
	 * @param itemName itemName
	 * @return meisaival
	 */
	protected String[] retVal1(String itemName){
		switch(itemName){
			case "meisai01":	 return meisai01_val1 ;
			case "meisai02":	 return meisai02_val1 ;
			case "meisai03":	 return meisai03_val1 ;
			case "meisai04":	 return meisai04_val1 ;
			case "meisai05":	 return meisai05_val1 ;
			case "meisai06":	 return meisai06_val1 ;
			case "meisai07":	 return meisai07_val1 ;
			case "meisai08":	 return meisai08_val1 ;
			case "meisai09":	 return meisai09_val1 ;
			case "meisai10":	 return meisai10_val1 ;
			case "meisai11":	 return meisai11_val1 ;
			case "meisai12":	 return meisai12_val1 ;
			case "meisai13":	 return meisai13_val1 ;
			case "meisai14":	 return meisai14_val1 ;
			case "meisai15":	 return meisai15_val1 ;
			case "meisai16":	 return meisai16_val1 ;
			case "meisai17":	 return meisai17_val1 ;
			case "meisai18":	 return meisai18_val1 ;
			case "meisai19":	 return meisai19_val1 ;
			case "meisai20":	 return meisai20_val1 ;
			case "meisai21":	 return meisai21_val1 ;
			case "meisai22":	 return meisai22_val1 ;
			case "meisai23":	 return meisai23_val1 ;
			case "meisai24":	 return meisai24_val1 ;
			case "meisai25":	 return meisai25_val1 ;
			case "meisai26":	 return meisai26_val1 ;
			case "meisai27":	 return meisai27_val1 ;
			case "meisai28":	 return meisai28_val1 ;
			case "meisai29":	 return meisai29_val1 ;
			case "meisai30":	 return meisai30_val1 ;
		}
		return null;
	}
}
