package eteam.gyoumu.kanitodoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.BuhinType;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.HtmlCreateMode;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.MasterKbn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書作成画面Action
 */
@Getter @Setter @ToString
public class KaniTodokeTsuikaAction extends EteamAbstractAction {

//＜画面入力＞
	/** 伝票名 */
	String denpyouName;

	/** 伝票内容 */
	String denpyouNaiyou;

	/** 部品データリスト(申請内容) */
	String[] shinseiDataList;

	/** 部品データリスト(明細) */
	String[] meisaiDataList;

	/** オプションリスト(申請内容) */
	String[] shinseiOptionList;

	/** オプションリスト(明細) */
	String[] meisaiOptionList;

	/** 申請・明細トグルボタンフラグ */
	String toggleFlg;

	/** 部品数(申請内容) */
	Integer shinseiItemCnt;

	/** 部品数(明細) */
	Integer meisaiItemCnt;

	/** 申請内容html */
	String shinseiHtml;

	/** 明細html */
	String meisaiHtml;

	/** HTML作成モード */
	protected static final int MODE = HtmlCreateMode.LAYOUT;

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

	/** 明細の値１ */ String meisai01_val1; /** 明細の値２ */ String meisai01_val2;
	/** 明細の値１ */ String meisai02_val1; /** 明細の値２ */ String meisai02_val2;
	/** 明細の値１ */ String meisai03_val1; /** 明細の値２ */ String meisai03_val2;
	/** 明細の値１ */ String meisai04_val1; /** 明細の値２ */ String meisai04_val2;
	/** 明細の値１ */ String meisai05_val1; /** 明細の値２ */ String meisai05_val2;
	/** 明細の値１ */ String meisai06_val1; /** 明細の値２ */ String meisai06_val2;
	/** 明細の値１ */ String meisai07_val1; /** 明細の値２ */ String meisai07_val2;
	/** 明細の値１ */ String meisai08_val1; /** 明細の値２ */ String meisai08_val2;
	/** 明細の値１ */ String meisai09_val1; /** 明細の値２ */ String meisai09_val2;
	/** 明細の値１ */ String meisai10_val1; /** 明細の値２ */ String meisai10_val2;
	/** 明細の値１ */ String meisai11_val1; /** 明細の値２ */ String meisai11_val2;
	/** 明細の値１ */ String meisai12_val1; /** 明細の値２ */ String meisai12_val2;
	/** 明細の値１ */ String meisai13_val1; /** 明細の値２ */ String meisai13_val2;
	/** 明細の値１ */ String meisai14_val1; /** 明細の値２ */ String meisai14_val2;
	/** 明細の値１ */ String meisai15_val1; /** 明細の値２ */ String meisai15_val2;
	/** 明細の値１ */ String meisai16_val1; /** 明細の値２ */ String meisai16_val2;
	/** 明細の値１ */ String meisai17_val1; /** 明細の値２ */ String meisai17_val2;
	/** 明細の値１ */ String meisai18_val1; /** 明細の値２ */ String meisai18_val2;
	/** 明細の値１ */ String meisai19_val1; /** 明細の値２ */ String meisai19_val2;
	/** 明細の値１ */ String meisai20_val1; /** 明細の値２ */ String meisai20_val2;
	/** 明細の値１ */ String meisai21_val1; /** 明細の値２ */ String meisai21_val2;
	/** 明細の値１ */ String meisai22_val1; /** 明細の値２ */ String meisai22_val2;
	/** 明細の値１ */ String meisai23_val1; /** 明細の値２ */ String meisai23_val2;
	/** 明細の値１ */ String meisai24_val1; /** 明細の値２ */ String meisai24_val2;
	/** 明細の値１ */ String meisai25_val1; /** 明細の値２ */ String meisai25_val2;
	/** 明細の値１ */ String meisai26_val1; /** 明細の値２ */ String meisai26_val2;
	/** 明細の値１ */ String meisai27_val1; /** 明細の値２ */ String meisai27_val2;
	/** 明細の値１ */ String meisai28_val1; /** 明細の値２ */ String meisai28_val2;
	/** 明細の値１ */ String meisai29_val1; /** 明細の値２ */ String meisai29_val2;
	/** 明細の値１ */ String meisai30_val1; /** 明細の値２ */ String meisai30_val2;

//＜画面入力以外＞
	/** 伝票区分 */
	String denpyouKbn;
	/** コピー元の伝票区分（複製時） */
	String copyDenpyouKbn;
	/** コピー元の伝票バージョン（複製時） */
	String copyVersion;
	/** 時刻一覧リスト */
	List<GMap> jikokuList;

//＜部品＞
	/** コネクション */
	EteamConnection connection;

	/** ユーザー定義届書作成　SELECT */
	KaniTodokeLogic kaniTodokeLogic;
	
	/** ユーザー定義届書カテゴリ　SELECT */
	KaniTodokeCategoryLogic kaniTodokeCategoryLogic;

	/** 会計共通 */
	KaikeiCommonLogic commonLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouName  , 1, 20 ,"伝票種別"  ,false);
		checkString(denpyouNaiyou, 1, 160,"内容",false);
	}

	/**
	 * 形式チェック
	 * @param gamenInfo 画面項目情報
	 */
	protected void formatCheck(KaniTodokeGamenInfo gamenInfo) {

		String[][] layoutArray = gamenInfo.getLayout();
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		String[][] optionArray = gamenInfo.getOption();

		for (String[] data : layoutArray) {

			String item_name = data[DataListIndex.ITEM_NAME];                                      // 項目名
			String area_kbn = data[DataListIndex.AREA_KBN];                                        // エリア区分
			String max_length = data[DataListIndex.MAX_LENGTH];                                    // 最大桁数
			String min_value = data[DataListIndex.MIN_VALUE];                                      // 最小値
			String max_value = data[DataListIndex.MAX_VALUE];                                      // 最大値
			String label_name = data[DataListIndex.LABEL_NAME];                                    // ラベル名
			String buhin_type = data[DataListIndex.BUHIN_TYPE];                                    // 部品タイプ
			String buhin_format = data[DataListIndex.BUHIN_FORMAT];                                // 部品形式
			String master_kbn = data[DataListIndex.MASTER_KBN];                                    // マスター区分
			String yosanId = data[DataListIndex.VALUE1];                                           // 予算執行項目ID
			String value = mapValue1.get(1).get(item_name) == null ? "" : mapValue1.get(1).get(item_name);         // 値
			String decimal_point = data[DataListIndex.DECIMAL_POINT];                              // 小数点以下桁数
			String kotei_hyouji = data[DataListIndex.KOTEI_HYOUJI];                                // 固定表示

			// エラーラベル名
			String errLableName = "";

			switch (area_kbn){
			case AREA_KBN.SHINSEI:
				errLableName = "申請内容." + label_name;
				break;
			case AREA_KBN.MEISAI:
				errLableName = "明細." + label_name;
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
					checkKingakuOver0(value, errLableName, false);
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
				
				//選択項目マスタに指定項目存在するかチェック
				if(!kaniTodokeLogic.checkSelectItemExist(item_name, optionArray)) {
					errorList.add(errLableName + "指定項目は選択項目マスターに存在しません。");
				};

				// ドメイン取得
				String[] domain = kaniTodokeLogic.getItemOption(item_name, optionArray);

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
					if( !(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId())) ) {
						commonLogic.checkFutanBumonWithSecurity(shiwakeCheckData.futanBumonCd, shiwakeCheckData.futanBumonNm, super.getUser(), null, null, errorList);
					}
					break;
				case MasterKbn.KAMOKU:
					shiwakeCheckData.kamokuNm = errLableName;
					shiwakeCheckData.kamokuCd = value;
					setShiwakeKamokuEdaban(layoutArray, mapValue1, area_kbn, yosanId, shiwakeCheckData, 
							YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KAMOKU, YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_EDA);
					setShiwakeKamokuEdaban(layoutArray, mapValue1, area_kbn, yosanId, shiwakeCheckData, 
							YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU, YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA);
					commonLogic.checkShiwake(shiwakeCheckData, errorList);
					break;
				case MasterKbn.TORIHIKISAKI:
					shiwakeCheckData.torihikisakiNm = errLableName;
					shiwakeCheckData.torihikisakiCd = value;
					commonLogic.checkShiwake(shiwakeCheckData, errorList);
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

	/**
	 * 科目によって対応する科目枝番をセット
	 * @param layoutArray レイアウト行
	 * @param mapValue1 値格納MAP
	 * @param area_kbn エリア区分
	 * @param yosanId 予算ID
	 * @param shiwakeCheckData 仕訳データ
	 * @param kamokuKbn 予算執行項目ID(科目)
	 * @param kamokuEdaKbn 予算執行項目ID(科目枝番)
	 */
	protected void setShiwakeKamokuEdaban(String[][] layoutArray, Map<Integer, Map<String, String>> mapValue1,
			String area_kbn, String yosanId, ShiwakeCheckData shiwakeCheckData, String kamokuKbn, String kamokuEdaKbn) {
		if (yosanId.equals(kamokuKbn)) {
			for (String[] d : layoutArray) {
				if (d[DataListIndex.VALUE1].equals(kamokuEdaKbn)) {
					switch (area_kbn){
					case AREA_KBN.SHINSEI:
						shiwakeCheckData.kamokuEdabanNm = "申請内容." + d[DataListIndex.LABEL_NAME];
						break;
					case AREA_KBN.MEISAI:
						shiwakeCheckData.kamokuEdabanNm = "明細." + d[DataListIndex.LABEL_NAME];
						break;
					}
					shiwakeCheckData.kamokuEdabanCd = mapValue1.get(1).get(d[DataListIndex.ITEM_NAME]) == null ? "" : mapValue1.get(1).get(d[DataListIndex.ITEM_NAME]);
				}
			}
		}
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = { 
				{ denpyouName,   "伝票種別",   "1" },
				{ denpyouNaiyou, "内容", "1" }
		};
		hissuCheckCommon(list, eventNum);
	}

	/**
	 * 相関チェック<br>
	 * 登録ボタン押下時に相関チェックを実施する。<br>
	 * 
	 * @param shinseiGamenInfo 画面情報（申請内容）
	 * @param meisaiGamenInfo 画面情報（明細）
	 */
	protected void soukanCheck(KaniTodokeGamenInfo shinseiGamenInfo, KaniTodokeGamenInfo meisaiGamenInfo) {
		// 登録時と変更時の共通相関チェックを実施する。
		List<String> lstMsg = this.kaniTodokeLogic.soukanCheck(denpyouKbn, shinseiGamenInfo, meisaiGamenInfo);
		if (0 < lstMsg.size()){
			// エラーがある場合はメッセージを移送する。
			errorList.addAll(lstMsg);
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * 
	 * @return 処理結果
	 */
	public String init() {

		// コネクション生成
		connection = EteamConnection.connect();

		try{

			// 部品初期化
			initParts();

			toggleFlg = "0"; // 申請内容へ追加を選択
			shinseiItemCnt = 0; // 申請エリアの項目数の初期化
			meisaiItemCnt = 0; // 明細エリアの項目数の初期化

			// コピー元の伝票区分の有無により複製モードを判定する。
			if (isEmpty(this.copyDenpyouKbn)){
				// コピー元の伝票区分に設定がない場合は従来の新規作成を行う。

				// レイアウト作成用のHTMLを生成する
				shinseiHtml = kaniTodokeLogic.createHtml(MODE, AREA_KBN.SHINSEI);
				meisaiHtml = kaniTodokeLogic.createHtml(MODE, AREA_KBN.MEISAI);

			}else{
				// コピー元の伝票区分に設定がある場合は複製による新規作成を行う。

				// 画面項目情報を取得する
				KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI, true);
				KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI, true);

				// 画面項目を再構築する
				shinseiHtml = kaniTodokeLogic.createHtml(MODE, AREA_KBN.SHINSEI, shinseiGamenInfo);
				meisaiHtml = kaniTodokeLogic.createHtml(MODE, AREA_KBN.MEISAI, meisaiGamenInfo);

				// 画面項目数を設定する
				shinseiItemCnt = shinseiGamenInfo.getItemCount();
				meisaiItemCnt = meisaiGamenInfo.getItemCount();
			}

			// カスタマイズ用
			initExt();

			return "success";

		} finally {
			connection.close();
		}
	}

	/**
	 * 初期表示イベント(カスタマイズ用)
	 */
	protected void initExt() {}

	/**
	 * 登録イベント
	 * @return 処理結果
	 */
	public String touroku() {

		// コネクション生成
		connection = EteamConnection.connect();

		try{

			// 部品初期化
			initParts();

			// 画面項目情報を取得する
			KaniTodokeGamenInfo shinseiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.SHINSEI, false);
			KaniTodokeGamenInfo meisaiGamenInfo = createKaniTodokeGamenInfo(AREA_KBN.MEISAI, false);

			// 項目数の取得
			shinseiItemCnt = shinseiGamenInfo.getItemCount();
			meisaiItemCnt = meisaiGamenInfo.getItemCount();

			// 入力チェック
			hissuCheck(1);

			// 形式チェック(共通)
			formatCheck();

			// 入力部品数チェック
			if (shinseiItemCnt < 1) {
				errorList.add("申請内容に入力部品を追加してください。");
			}

			//設定情報テーブルからユーザー定義届書レイアウトに追加できる項目件数（1～99）
			int itemNum = 30;

			if (shinseiItemCnt > itemNum) {
				errorList.add("申請内容に追加できる入力部品数の上限を超えています。");
			}

			if (meisaiItemCnt > itemNum) {
				errorList.add("明細に追加できる入力部品数の上限を超えています。");
			}

			// 形式チェック
			formatCheck(shinseiGamenInfo);
			formatCheck(meisaiGamenInfo);

			// 相関チェック
			this.soukanCheck(shinseiGamenInfo, meisaiGamenInfo);

			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {

				// 画面項目を再構築する
				shinseiHtml = kaniTodokeLogic.createHtml(MODE, AREA_KBN.SHINSEI, shinseiGamenInfo);
				meisaiHtml = kaniTodokeLogic.createHtml(MODE, AREA_KBN.MEISAI, meisaiGamenInfo);

				// 戻り値を返す
				return "error";
			}

			// 伝票区分
			denpyouKbn = kaniTodokeCategoryLogic.getNewDenpyouKbn();

			// バージョン
			int version = kaniTodokeCategoryLogic.findNewVersion(denpyouKbn);

			// ユーザーID
			String userId = getUser().getTourokuOrKoushinUserId();

			// ユーザー定義届書メタテーブルに登録
			kaniTodokeLogic.insertKaniTodokeMetaData(denpyouKbn, userId);

			// ユーザー定義届書バージョンテーブルに登録
			kaniTodokeLogic.insertKaniTodokeVersion(denpyouKbn, version, denpyouName, denpyouNaiyou, userId);

			// ユーザー定義届書項目定義テーブルに登録
			kaniTodokeLogic.insertKaniTodokeKoumokuTeigi(denpyouKbn, version, shinseiGamenInfo, userId);
			kaniTodokeLogic.insertKaniTodokeKoumokuTeigi(denpyouKbn, version, meisaiGamenInfo, userId);

			// 伝票種別一覧に登録
			kaniTodokeLogic.insertDenpyouShubetsuIchiran(denpyouKbn, denpyouName, denpyouNaiyou, userId);

			// カスタマイズ用
			tourokuExt();

			// コミット
			connection.commit();

			// 戻り値を返す
			return "success";

		} finally {
			connection.close();
		}
	}

	/**
	 * 登録イベント(カスタマイズ用)
	 */
	protected void tourokuExt() {}

	/**
	 * 部品初期化
	 */
	protected void initParts() {
		// ユーザー定義届書Logic
		kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, connection);
		// ユーザー定義届書内のSelect文を集約したLogic
		kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		// 会計共通Logic
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		// 内部コード取得
		SystemKanriCategoryLogic sysLc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		jikokuList = sysLc.loadNaibuCdSetting("jikoku_ichiran");
	}

	/**
	 * ユーザー定義届書の画面項目情報を作成する
	 * 
	 * @param areaKbn エリア区分
	 * @param isInit 初期処理かどうかの判定
	 * @return 画面項目情報
	 */
	protected KaniTodokeGamenInfo createKaniTodokeGamenInfo(String areaKbn, Boolean isInit) {

		// リストマップを配列へ変換する
		String[] layoutArray = new String[0];
		String[] optionArray = new String[0];

		// 呼出元により処理を分岐
		if (isInit) {
			// 初期処理（複製モード）からの呼出時は複製元のユーザー定義届書レイアウトを読み込む

			// データ取得
			List<GMap> listMapData = new ArrayList<GMap>();
			listMapData = kaniTodokeCategoryLogic.loadData(areaKbn, this.copyDenpyouKbn, this.copyVersion, "");

			// データをメンバー変数に設定する
			this.setMemberValue(listMapData);

			// レイアウト・オプション取得
			List<GMap> listMapLayout = new ArrayList<GMap>();
			List<GMap> listMapOption = new ArrayList<GMap>();
			listMapLayout = kaniTodokeCategoryLogic.loadLayout(areaKbn, this.copyDenpyouKbn, this.copyVersion);
			listMapOption = kaniTodokeCategoryLogic.loadOption(areaKbn, this.copyDenpyouKbn, this.copyVersion);

			// リストマップから１次元配列へ変換する
			layoutArray = kaniTodokeLogic.toArrayListMapData(listMapLayout);
			optionArray = kaniTodokeLogic.toArrayListMapOption(listMapOption);

		}else{
			// 登録処理からの呼出時は画面にはいちされた入力部品を使用
			switch (areaKbn){
			case AREA_KBN.SHINSEI:
				layoutArray = shinseiDataList;
				optionArray = shinseiOptionList;
				break;
			case AREA_KBN.MEISAI:
				layoutArray = meisaiDataList;
				optionArray = meisaiOptionList;
				break;
			}
		}

		// 配列の１次元から２次元への変換
		String[][] layoutArray2D = toArray2D(layoutArray);
		String[][] optionArray2D = toArray2D(optionArray);

		// 項目名の配列の取得
		String[] itemNameArray = getItemNameArray(layoutArray2D);

		// 入力値を項目名に紐付けるためにマッピングする
		Map<Integer, Map<String, String>> mapValue1 = new HashMap<Integer, Map<String, String>>();
		Map<Integer, Map<String, String>> mapValue2 = new HashMap<Integer, Map<String, String>>();
		mapItemValue(itemNameArray, mapValue1, mapValue2);

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

	/** 入力値を項目名に紐付けるためにマッピングする
	 * @param itemNameArray 項目名配列
	 * @param mapValue1 値１マップ
	 * @param mapValue2 値２マップ
	 */
	protected void mapItemValue(String[] itemNameArray, Map<Integer, Map<String, String>> mapValue1, Map<Integer, Map<String, String>> mapValue2) {
		for (String item_name : itemNameArray) {

			// 入力値を項目名に紐付けるためにマッピングする
			this.mapItemValue(mapValue1, mapValue2, item_name, 1);
		}
	}
	
	/** 入力値を項目名に紐付けるためにマッピングする
	 * @param mapValue1Tmp 値１マップ
	 * @param mapValue2Tmp 値２マップ
	 * @param item_name 項目名
	 * @param denpyou_edano 伝票枝番号
	 */
	protected void mapItemValue(Map<Integer, Map<String, String>> mapValue1Tmp, Map<Integer, Map<String, String>> mapValue2Tmp, String item_name, Integer denpyou_edano) {

		if(!mapValue1Tmp.containsKey(denpyou_edano)) {
			mapValue1Tmp.put(denpyou_edano, new HashMap<String, String>());
		}

		if(!mapValue2Tmp.containsKey(denpyou_edano)) {
			mapValue2Tmp.put(denpyou_edano, new HashMap<String, String>());
		}

		Map<String, String> mapValue1 = mapValue1Tmp.get(denpyou_edano);
		Map<String, String> mapValue2 = mapValue2Tmp.get(denpyou_edano);

		if(item_name.equals("shinsei01")){mapValue1.put(item_name, shinsei01_val1);mapValue2.put(item_name, shinsei01_val2);}
		if(item_name.equals("shinsei02")){mapValue1.put(item_name, shinsei02_val1);mapValue2.put(item_name, shinsei02_val2);}
		if(item_name.equals("shinsei03")){mapValue1.put(item_name, shinsei03_val1);mapValue2.put(item_name, shinsei03_val2);}
		if(item_name.equals("shinsei04")){mapValue1.put(item_name, shinsei04_val1);mapValue2.put(item_name, shinsei04_val2);}
		if(item_name.equals("shinsei05")){mapValue1.put(item_name, shinsei05_val1);mapValue2.put(item_name, shinsei05_val2);}
		if(item_name.equals("shinsei06")){mapValue1.put(item_name, shinsei06_val1);mapValue2.put(item_name, shinsei06_val2);}
		if(item_name.equals("shinsei07")){mapValue1.put(item_name, shinsei07_val1);mapValue2.put(item_name, shinsei07_val2);}
		if(item_name.equals("shinsei08")){mapValue1.put(item_name, shinsei08_val1);mapValue2.put(item_name, shinsei08_val2);}
		if(item_name.equals("shinsei09")){mapValue1.put(item_name, shinsei09_val1);mapValue2.put(item_name, shinsei09_val2);}
		if(item_name.equals("shinsei10")){mapValue1.put(item_name, shinsei10_val1);mapValue2.put(item_name, shinsei10_val2);}
		if(item_name.equals("shinsei11")){mapValue1.put(item_name, shinsei11_val1);mapValue2.put(item_name, shinsei11_val2);}
		if(item_name.equals("shinsei12")){mapValue1.put(item_name, shinsei12_val1);mapValue2.put(item_name, shinsei12_val2);}
		if(item_name.equals("shinsei13")){mapValue1.put(item_name, shinsei13_val1);mapValue2.put(item_name, shinsei13_val2);}
		if(item_name.equals("shinsei14")){mapValue1.put(item_name, shinsei14_val1);mapValue2.put(item_name, shinsei14_val2);}
		if(item_name.equals("shinsei15")){mapValue1.put(item_name, shinsei15_val1);mapValue2.put(item_name, shinsei15_val2);}
		if(item_name.equals("shinsei16")){mapValue1.put(item_name, shinsei16_val1);mapValue2.put(item_name, shinsei16_val2);}
		if(item_name.equals("shinsei17")){mapValue1.put(item_name, shinsei17_val1);mapValue2.put(item_name, shinsei17_val2);}
		if(item_name.equals("shinsei18")){mapValue1.put(item_name, shinsei18_val1);mapValue2.put(item_name, shinsei18_val2);}
		if(item_name.equals("shinsei19")){mapValue1.put(item_name, shinsei19_val1);mapValue2.put(item_name, shinsei19_val2);}
		if(item_name.equals("shinsei20")){mapValue1.put(item_name, shinsei20_val1);mapValue2.put(item_name, shinsei20_val2);}
		if(item_name.equals("shinsei21")){mapValue1.put(item_name, shinsei21_val1);mapValue2.put(item_name, shinsei21_val2);}
		if(item_name.equals("shinsei22")){mapValue1.put(item_name, shinsei22_val1);mapValue2.put(item_name, shinsei22_val2);}
		if(item_name.equals("shinsei23")){mapValue1.put(item_name, shinsei23_val1);mapValue2.put(item_name, shinsei23_val2);}
		if(item_name.equals("shinsei24")){mapValue1.put(item_name, shinsei24_val1);mapValue2.put(item_name, shinsei24_val2);}
		if(item_name.equals("shinsei25")){mapValue1.put(item_name, shinsei25_val1);mapValue2.put(item_name, shinsei25_val2);}
		if(item_name.equals("shinsei26")){mapValue1.put(item_name, shinsei26_val1);mapValue2.put(item_name, shinsei26_val2);}
		if(item_name.equals("shinsei27")){mapValue1.put(item_name, shinsei27_val1);mapValue2.put(item_name, shinsei27_val2);}
		if(item_name.equals("shinsei28")){mapValue1.put(item_name, shinsei28_val1);mapValue2.put(item_name, shinsei28_val2);}
		if(item_name.equals("shinsei29")){mapValue1.put(item_name, shinsei29_val1);mapValue2.put(item_name, shinsei29_val2);}
		if(item_name.equals("shinsei30")){mapValue1.put(item_name, shinsei30_val1);mapValue2.put(item_name, shinsei30_val2);}

		if(item_name.equals("meisai01")){mapValue1.put(item_name, meisai01_val1);mapValue2.put(item_name, meisai01_val2);}
		if(item_name.equals("meisai02")){mapValue1.put(item_name, meisai02_val1);mapValue2.put(item_name, meisai02_val2);}
		if(item_name.equals("meisai03")){mapValue1.put(item_name, meisai03_val1);mapValue2.put(item_name, meisai03_val2);}
		if(item_name.equals("meisai04")){mapValue1.put(item_name, meisai04_val1);mapValue2.put(item_name, meisai04_val2);}
		if(item_name.equals("meisai05")){mapValue1.put(item_name, meisai05_val1);mapValue2.put(item_name, meisai05_val2);}
		if(item_name.equals("meisai06")){mapValue1.put(item_name, meisai06_val1);mapValue2.put(item_name, meisai06_val2);}
		if(item_name.equals("meisai07")){mapValue1.put(item_name, meisai07_val1);mapValue2.put(item_name, meisai07_val2);}
		if(item_name.equals("meisai08")){mapValue1.put(item_name, meisai08_val1);mapValue2.put(item_name, meisai08_val2);}
		if(item_name.equals("meisai09")){mapValue1.put(item_name, meisai09_val1);mapValue2.put(item_name, meisai09_val2);}
		if(item_name.equals("meisai10")){mapValue1.put(item_name, meisai10_val1);mapValue2.put(item_name, meisai10_val2);}
		if(item_name.equals("meisai11")){mapValue1.put(item_name, meisai11_val1);mapValue2.put(item_name, meisai11_val2);}
		if(item_name.equals("meisai12")){mapValue1.put(item_name, meisai12_val1);mapValue2.put(item_name, meisai12_val2);}
		if(item_name.equals("meisai13")){mapValue1.put(item_name, meisai13_val1);mapValue2.put(item_name, meisai13_val2);}
		if(item_name.equals("meisai14")){mapValue1.put(item_name, meisai14_val1);mapValue2.put(item_name, meisai14_val2);}
		if(item_name.equals("meisai15")){mapValue1.put(item_name, meisai15_val1);mapValue2.put(item_name, meisai15_val2);}
		if(item_name.equals("meisai16")){mapValue1.put(item_name, meisai16_val1);mapValue2.put(item_name, meisai16_val2);}
		if(item_name.equals("meisai17")){mapValue1.put(item_name, meisai17_val1);mapValue2.put(item_name, meisai17_val2);}
		if(item_name.equals("meisai18")){mapValue1.put(item_name, meisai18_val1);mapValue2.put(item_name, meisai18_val2);}
		if(item_name.equals("meisai19")){mapValue1.put(item_name, meisai19_val1);mapValue2.put(item_name, meisai19_val2);}
		if(item_name.equals("meisai20")){mapValue1.put(item_name, meisai20_val1);mapValue2.put(item_name, meisai20_val2);}
		if(item_name.equals("meisai21")){mapValue1.put(item_name, meisai21_val1);mapValue2.put(item_name, meisai21_val2);}
		if(item_name.equals("meisai22")){mapValue1.put(item_name, meisai22_val1);mapValue2.put(item_name, meisai22_val2);}
		if(item_name.equals("meisai23")){mapValue1.put(item_name, meisai23_val1);mapValue2.put(item_name, meisai23_val2);}
		if(item_name.equals("meisai24")){mapValue1.put(item_name, meisai24_val1);mapValue2.put(item_name, meisai24_val2);}
		if(item_name.equals("meisai25")){mapValue1.put(item_name, meisai25_val1);mapValue2.put(item_name, meisai25_val2);}
		if(item_name.equals("meisai26")){mapValue1.put(item_name, meisai26_val1);mapValue2.put(item_name, meisai26_val2);}
		if(item_name.equals("meisai27")){mapValue1.put(item_name, meisai27_val1);mapValue2.put(item_name, meisai27_val2);}
		if(item_name.equals("meisai28")){mapValue1.put(item_name, meisai28_val1);mapValue2.put(item_name, meisai28_val2);}
		if(item_name.equals("meisai29")){mapValue1.put(item_name, meisai29_val1);mapValue2.put(item_name, meisai29_val2);}
		if(item_name.equals("meisai30")){mapValue1.put(item_name, meisai30_val1);mapValue2.put(item_name, meisai30_val2);}
	}

	/**
	 * ２次元配列化
	 * @param array １次元配列
	 * @return ２次元配列
	 */
	protected String[][] toArray2D(String[] array) {
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
	protected String[] getItemNameArray(String[][] dataArray) {
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
	 * 値をメンバ変数に設定する
	 * @param listMap リストマップ
	 */
	protected void setMemberValue(List<GMap> listMap)
	{
		for(GMap map : listMap) {

			String item_name = map.get("item_name") == null ? "" : map.get("item_name").toString();
			String value1 = map.get("value1") == null ? "" : map.get("value1").toString();
			String value2 = map.get("value2") == null ? "" : map.get("value2").toString();

			// 入力値をメンバ変数に設定する
			this.setMemberValue(value1, value2, item_name);
		}
	}
	
	/**
	 * 値をメンバ変数に設定する
	 * @param value1 値1
	 * @param value2 値2
	 * @param item_name 項目名
	 */
	protected void setMemberValue(String value1, String value2, String item_name){

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

		if(item_name.equals("meisai01")) {meisai01_val1 = value1; meisai01_val2 = value2;}
		if(item_name.equals("meisai02")) {meisai02_val1 = value1; meisai02_val2 = value2;}
		if(item_name.equals("meisai03")) {meisai03_val1 = value1; meisai03_val2 = value2;}
		if(item_name.equals("meisai04")) {meisai04_val1 = value1; meisai04_val2 = value2;}
		if(item_name.equals("meisai05")) {meisai05_val1 = value1; meisai05_val2 = value2;}
		if(item_name.equals("meisai06")) {meisai06_val1 = value1; meisai06_val2 = value2;}
		if(item_name.equals("meisai07")) {meisai07_val1 = value1; meisai07_val2 = value2;}
		if(item_name.equals("meisai08")) {meisai08_val1 = value1; meisai08_val2 = value2;}
		if(item_name.equals("meisai09")) {meisai09_val1 = value1; meisai09_val2 = value2;}
		if(item_name.equals("meisai10")) {meisai10_val1 = value1; meisai10_val2 = value2;}
		if(item_name.equals("meisai11")) {meisai11_val1 = value1; meisai11_val2 = value2;}
		if(item_name.equals("meisai12")) {meisai12_val1 = value1; meisai12_val2 = value2;}
		if(item_name.equals("meisai13")) {meisai13_val1 = value1; meisai13_val2 = value2;}
		if(item_name.equals("meisai14")) {meisai14_val1 = value1; meisai14_val2 = value2;}
		if(item_name.equals("meisai15")) {meisai15_val1 = value1; meisai15_val2 = value2;}
		if(item_name.equals("meisai16")) {meisai16_val1 = value1; meisai16_val2 = value2;}
		if(item_name.equals("meisai17")) {meisai17_val1 = value1; meisai17_val2 = value2;}
		if(item_name.equals("meisai18")) {meisai18_val1 = value1; meisai18_val2 = value2;}
		if(item_name.equals("meisai19")) {meisai19_val1 = value1; meisai19_val2 = value2;}
		if(item_name.equals("meisai20")) {meisai20_val1 = value1; meisai20_val2 = value2;}
		if(item_name.equals("meisai21")) {meisai21_val1 = value1; meisai21_val2 = value2;}
		if(item_name.equals("meisai22")) {meisai22_val1 = value1; meisai22_val2 = value2;}
		if(item_name.equals("meisai23")) {meisai23_val1 = value1; meisai23_val2 = value2;}
		if(item_name.equals("meisai24")) {meisai24_val1 = value1; meisai24_val2 = value2;}
		if(item_name.equals("meisai25")) {meisai25_val1 = value1; meisai25_val2 = value2;}
		if(item_name.equals("meisai26")) {meisai26_val1 = value1; meisai26_val2 = value2;}
		if(item_name.equals("meisai27")) {meisai27_val1 = value1; meisai27_val2 = value2;}
		if(item_name.equals("meisai28")) {meisai28_val1 = value1; meisai28_val2 = value2;}
		if(item_name.equals("meisai29")) {meisai29_val1 = value1; meisai29_val2 = value2;}
		if(item_name.equals("meisai30")) {meisai30_val1 = value1; meisai30_val2 = value2;}
	}
}
