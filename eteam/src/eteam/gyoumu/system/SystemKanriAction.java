package eteam.gyoumu.system;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.Env;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN_KYOTEN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.RegAccess;
import eteam.common.RegAccess.REG_KEY_NAME;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShiwakePatternMasterZaimuKyotenDao;
import eteam.database.dto.ShiwakePatternMasterZaimuKyoten;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.masterkanri.MasterTorikomiBat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * システム管理Action
 */
@Getter @Setter @ToString
public class SystemKanriAction extends EteamAbstractAction {

	/** カスタマイズ用Strategyクラス */
	protected AbstractSystemKanriActionCustomStrategy customStrategy;
	
	//＜定数＞
	/** e文書カテゴリ名 */
	protected static final String CATEGORY_NAME_EBUNSHO = "e文書";
	/** 予算カテゴリ名 */
	protected static final String CATEGORY_NAME_YOSAN_A = "予算執行";
	/** 支払依頼申請カテゴリ名 */
	protected static final String CATEGORY_NAME_SHIHARAI_IRAI = "支払依頼申請";
	/** 拠点入力カテゴリ名 */
	protected static final String CATEGORY_NAME_KYOTEN = "(拠点入力)";
	/** HF表示用 */
	protected static final String[] HF_FOR_DISPLAY  = {
		"ヘッダーフィールドマッピング①",
		"ヘッダーフィールドマッピング②",
		"ヘッダーフィールドマッピング③",
		"ヘッダーフィールドマッピング④",
		"ヘッダーフィールドマッピング⑤",
		"ヘッダーフィールドマッピング⑥",
		"ヘッダーフィールドマッピング⑦",
		"ヘッダーフィールドマッピング⑧",
		"ヘッダーフィールドマッピング⑨",
		"ヘッダーフィールドマッピング⑩"
	};
	/** UF表示用 */
	protected static final String[]  UF_FOR_DISPLAY  = {
		"ユニバーサルフィールドマッピング①",
		"ユニバーサルフィールドマッピング②",
		"ユニバーサルフィールドマッピング③",
		"ユニバーサルフィールドマッピング④",
		"ユニバーサルフィールドマッピング⑤",
		"ユニバーサルフィールドマッピング⑥",
		"ユニバーサルフィールドマッピング⑦",
		"ユニバーサルフィールドマッピング⑧",
		"ユニバーサルフィールドマッピング⑨",
		"ユニバーサルフィールドマッピング⑩"
	};
	/** UF表示用（固定値） */
	protected static final String[]  UF_KOTEI_FOR_DISPLAY  = {
		"ユニバーサルフィールドマッピング（固定値）①",
		"ユニバーサルフィールドマッピング（固定値）②",
		"ユニバーサルフィールドマッピング（固定値）③",
		"ユニバーサルフィールドマッピング（固定値）④",
		"ユニバーサルフィールドマッピング（固定値）⑤",
		"ユニバーサルフィールドマッピング（固定値）⑥",
		"ユニバーサルフィールドマッピング（固定値）⑦",
		"ユニバーサルフィールドマッピング（固定値）⑧",
		"ユニバーサルフィールドマッピング（固定値）⑨",
		"ユニバーサルフィールドマッピング（固定値）⑩"
	};


	// チェックボックスデータ
	/** 代行 */
	String daikou;
	/** 一般ユーザー代行者指定 */
	String userDaikouShitei;
	/** 管理者代行者指定 */
	String kanriDaikouShitei;
	/** ガイダンス起票 */
	String guidanceKihyou;
	/** お気に入り起票 */
	String okiniiriKihyou;
	/** メール配信 */
	String mailHaishin;
	/** CSV（振込）ダウンロード */
	String csvFurikomiDownload;
	/** バッチ連携結果確認 */
	String batchRenkeiKekkaKakunin;
	/** FBデータ作成 */
	String FBDataSakusei;
	/** 法人カード */
	String houjinCard;
	/** 会社手配 */
	String kaishaTehai;
	/** 外貨入力 */
	String gaikaNyuuryoku;
	/** ICカード */
	String icCard;
	/** 添付ファイルのプレビュー表示 */
	String previewTenpuFile;
	/** 伝票作成 */
	String denpyouSakusei;
	/** 起票 */
	String kihyou;
	/** 申請 */
	String shinsei;
	/** 取下げ */
	String torisage;
	/** 取戻し */
	String torimodoshi;
	/** 差戻し */
	String sashimodoshi;
	/** 承認 */
	String shounin;
	/** 否認 */
	String hinin;
	/** 承認ルート登録(申請者) */
	String shouninRouteTourokuShinseisha;
	/** 承認ルート登録(承認者) */
	String shouninRouteTourokuShouninsha;
	/** 上長承認後の取戻し */
	String shouningoTorimodoshi;
	/** 上位先決承認 */
	String jouiSenketsuShounin;
	/** 代行者の自己承認 */
	String daikoushaJikoShounin;
	/** ファイル添付機能 */
	String fileTenpu;
	/** WEB印刷ボタン表示 */
	String webInsatsuButton;
	/** 領収書・請求書等チェック必須 */
	String ryoushuushoSeikyuushoTouChkHissu;
	/** 操作履歴欄を表示する */
	String sousaRireki;
	/** 注記の表示 */ /*22120205追加*/
	String chuukiPreview;
	// メール配信部分のデータ
	/** SMTPサーバー名 */
	String mailServer;
	/** ポート番号 */
	String mailPort;
	/** 認証方法 */
	String mailNinshou;
	/** 暗号化方法 */
	String mailAngouka;
	/** メールアドレス */
	String mailAddress;
	/** ユーザー名 */
	String mailUser;
	/** パスワード */
	String mailPassword;
	/** 設定名 */
	String[] settingName;
	/** 設定値 */
	String[] settingVal;
	/** 差引単価（海外）外貨-レート */
	String sashihikiTankaKaigaiGaikaRate;

	//＜画面入力以外＞
	/** メール認証方法のDropDownList */
	List<GMap> ninshouList;
	/** メール暗号化方法のDropDownList */
	List<GMap> angoukaList;
	/** 設定一覧 */
	List<GMap> settingList;
	/** 設定に対する正規表現チェックリスト */
	Map<String, String[]> settingRegexMap;
	/** DBヴァージョン */
	String dbVersion;
	
	//＜部品＞
	/**	伝票種別一覧Dao */
	DenpyouShubetsuIchiranAbstractDao denpyouShubetsuIchiran;
	/**	システム管理ロジックDao */
	SystemKanriCategoryLogic systemLogic;
	/**マスター管理カテゴリロジック */
	MasterKanriCategoryLogic masterLogic;
	/**	会計共通ロジック */
	KaikeiCommonLogic kaikeiLogic;
	/**	会計カテゴリロジック */
	KaikeiCategoryLogic kcLogic;
	/**	PostgerSQLシステムカテゴリロジック */
	PostgreSQLSystemCatalogsLogic postgreSqlLogic;
	/**部門ユーザ管理ロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 仕訳パターンマスター拠点Dao */
	ShiwakePatternMasterZaimuKyotenDao shiwakePatternMasterZaimuKyotenDao;
	/** システム管理ロジック */
	SystemKanriLogic myLogic;
	/** ワークフローカテゴリロジック */
	WorkflowCategoryLogic wfLogic;
	
	//＜画面入力＞
	/** タブID */
	String tabId = "setting_workflow";
	/** e文書使用オプション有効フラグ */
	String ebunshoFlg = ""; //こっちのオプション設定は実はイベントごとに読み直しているので、hiddenに持つ意味はないが持っている
	/** 予算Aオプション有効フラグ */
	String yosanAFlg = ""; //こっちのオプション設定はhiddenにもっていない
	/** 支払依頼有無(オプション) */
	boolean shiharaiIraiOn;
	/**	請求書払い有無（オプション） */
	boolean seikyuushoBaraiOn;
	/**	届出ジェネレータ有無（オプション） */
	boolean kaniTodokeOn;

	//＜部品＞
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(MasterTorikomiBat.class);

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkCheckbox(daikou, "代行", false);
		checkCheckbox(userDaikouShitei, "一般ユーザー代行指定", false);
		checkCheckbox(kanriDaikouShitei, "管理者代行指定", false);
		checkCheckbox(guidanceKihyou, "ガイダンス起票", false);
		checkCheckbox(okiniiriKihyou, "お気に入り起票", false);
		checkCheckbox(mailHaishin, "メール配信(SMTP)", false);
		checkCheckbox(csvFurikomiDownload, "CSV（振込）ダウンロード", false);
		checkCheckbox(batchRenkeiKekkaKakunin, "バッチ連携結果確認", false);
		checkCheckbox(FBDataSakusei, "FBデータ作成", false);
		checkCheckbox(houjinCard, "法人カード",  			false);
		checkCheckbox(kaishaTehai, "会社手配",  				false);
		checkCheckbox(gaikaNyuuryoku, "外貨入力",  				false);
		checkCheckbox(icCard, "ICカード",  				false);
		checkCheckbox(previewTenpuFile, "添付ファイルのプレビュー表示", false);
		checkCheckbox(denpyouSakusei, "伝票作成", false);
		checkCheckbox(kihyou, "起票", false);
		checkCheckbox(shinsei, "申請", false);
		checkCheckbox(torisage, "取下げ", false);
		checkCheckbox(torimodoshi, "取戻し", false);
		checkCheckbox(sashimodoshi, "差戻し", false);
		checkCheckbox(shounin, "承認", false);
		checkCheckbox(hinin, "否認", false);
		checkCheckbox(shouninRouteTourokuShinseisha, "申請者承認ルート登録", false);
		checkCheckbox(shouninRouteTourokuShouninsha, "承認者承認ルート登録", false);
		checkCheckbox(shouningoTorimodoshi, "上長承認後の取戻し", false);
		checkCheckbox(jouiSenketsuShounin, "上位先決承認", false);
		checkCheckbox(daikoushaJikoShounin, "代行者の自己承認", false);
		checkCheckbox(fileTenpu, "ファイル添付機能", false);
		checkCheckbox(webInsatsuButton, "WEB印刷ボタン表示", false);
		checkCheckbox(ryoushuushoSeikyuushoTouChkHissu,
												"領収書・請求書等チェック必須", false);
		checkCheckbox(sousaRireki, "操作履歴欄の表示", false);
		checkCheckbox(chuukiPreview, "注記の表示", false);
		checkString(mailAddress, 6, 50, "メールアドレス", false);
		errorList.addAll(EteamCommon.mailAddressCheck(mailAddress));
	}

	@Override
	protected void hissuCheck(int eventNum) {
		if (2 == eventNum) {
			String[][] list;
			// メール配信チェックがONの時のみ、必須チェック
			if(EteamNaibuCodeSetting.KINOU_SEIGYO_KBN.YUUKOU.equals(mailHaishin)) {
				list = new String[][]{
					//項目								必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{mailServer, "SMTPサーバー名", "1"},
					{mailPort, "ポート番号", "1"},
					{mailNinshou, "認証方法", "1"},
					{mailAngouka, "暗号化方法", "1"},
					{mailAddress, "メールアドレス", "1"},
					{mailUser, "ユーザ名", "1"},
					// パスワード(mailPassword)は必須入力であるが、入力されていた場合のみ更新を行うため、必須入力チェックは行わない。
				};
				hissuCheckCommon(list, 1);
			}
		}
	}

	/**
	 * 設定情報のチェック（設定情報テーブルのメタ情報に従う）
	 * @param connection コネクション
	 */
	protected void settingCheck(EteamConnection connection) {
		initParts(connection);
		
		ShiwakeCheckData s;

		String shainCdRenkei = "";
		String houjinCardRenkei = "";
		String denpyouIDHanei = "";
		String denpyouNoStart = "";
		String denpyouNoEnd = "";

		String[] hfMapping = new String[HF_FOR_DISPLAY.length];
		for(int i = 0 ; i < HF_FOR_DISPLAY.length ; i++){ hfMapping[i] = ""; }
		String[] ufMapping = new String[UF_FOR_DISPLAY.length];
		for(int i = 0 ; i < UF_FOR_DISPLAY.length ; i++){ ufMapping[i] = ""; }
		String[] ufKoteiMapping = new String[UF_KOTEI_FOR_DISPLAY.length];
		for(int i = 0 ; i < UF_KOTEI_FOR_DISPLAY.length ; i++){ ufKoteiMapping[i] = ""; }

		String ebunshoEnableFlg = "";
		String ebunshoSeiseiA001 = "";
		String ebunshoSeiseiA002 = "";
		String ebunshoSeiseiA003 = "";
		String ebunshoSeiseiA004 = "";
		String ebunshoSeiseiA005 = "";
		String ebunshoSeiseiA006 = "";
		String ebunshoSeiseiA009 = "";
		String ebunshoSeiseiA010 = "";
		String ebunshoSeiseiA011 = "";
		String ebunshoSeiseiA012 = "";
		String ebunshoSeiseiA013 = "";
		String ebunshoSeiseiZ001 = "";
		String ebunshoSeiseiZ002 = "";
		String ebunshoSeiseiB000 = "";
		String ebunshoShubetsuA001 = "";
		String ebunshoShubetsuA002 = "";
		String ebunshoShubetsuA003 = "";
		String ebunshoShubetsuA004 = "";
		String ebunshoShubetsuA005 = "";
		String ebunshoShubetsuA006 = "";
		String ebunshoShubetsuA009 = "";
		String ebunshoShubetsuA010 = "";
		String ebunshoShubetsuA011 = "";
		String ebunshoShubetsuA012 = "";
		String ebunshoShubetsuA013 = "";
		String ebunshoShubetsuZ001 = "";
		String ebunshoShubetsuZ002 = "";
		String ebunshoShubetsuB000 = "";

		String sashihikiTankaKaigaiGaikaHeishuCd = "";
		String sashihikiTankaKaigaiGaika = "";
		String kaigaiNittouTankaGaikaSettei = "";

		String shiwakeKbn = "";
		String kakuteiUmu = "";

		String keijouNyuuryokuSeikyu = "";
		String keijouNyuuryokuShiharai = "";
		String keijouNyuuryokuJidou = "";
		String keijouNyuuryokuKeihi = "";
		String keijouNyuuryokuRyohi = "";
		String keijouNyuuryokuKaigaiRyohi = "";
		String keijouNyuuryokuKoutsuu = "";

		String keijouSeigenSeikyu = "";
		String keijouSeigenShiharai = "";
		String keijouSeigenJidou = "";
		String keijouSeigenKeihi = "";
		String keijouSeigenRyohi = "";
		String keijouSeigenKaigaiRyohi = "";
		String keijouSeigenKoutsuu = "";

		String kianNoHaneiRyakugou = "";
		String kianNoHaneiBangou = "";

		String userTeigiTodokeKensakuKenmei = "";
		String userTeigiTodokeKensakuNaiyou = "";

		String ouinFormat = "";
		String ouinNaiyou = "";

		String icCardTrain = "";
		String icCardBus = "";

		String shokuchiCd = "";

		String kojoName = "";
		String kojoKamokuCd = "";
		String kojoEdabanCd = "";
		String kojoFutanBumonCd = "";

		String ichigenCd = "";
		String ichigenTesuuryou = "";
		String ichigenKamokuCd = "";
		String ichigenEdabanCd = "";
		String ichigenFutanBumonCd = "";

		String kamokuCdFuteiki = "";
		String kamokuCdTeiki  = "";
		String kamokuCdSetsubi = "";

		String op21mparamKaishaCd = "";
		String uf1DenpyouIdHanei = "";
		String op21MparamKidouUser = "";
		String tenpuFileRenkei = "";
		String shiwakeSakuseiErrorAction = "";
		String shiwakeSakuseiHouhouA001 = "";
		String shiwakeSakuseiHouhouA004 = "";
		String shiwakeSakuseiHouhouA009 = "";
		String shiwakeSakuseiHouhouA010 = "";
		String shiwakeSakuseiHouhouA011 = "";
		String op21mparamKaishaCdKyoten = "";
		String shokuchiCdKyoten ="";
		String tenpuFileRenkeiKyoten = "";
		String uf1DenpyouIdHaneiKyoten = "";
		String shainCdRenkeiKyoten = "";
		String[] hfMappingKyoten = new String[HF_FOR_DISPLAY.length];
		for(int i = 0 ; i < HF_FOR_DISPLAY.length ; i++){ hfMappingKyoten[i] = ""; }
		String[] ufMappingKyoten = new String[UF_FOR_DISPLAY.length];
		for(int i = 0 ; i < UF_FOR_DISPLAY.length ; i++){ ufMappingKyoten[i] = ""; }
		String[] ufKoteiMappingKyoten = new String[UF_KOTEI_FOR_DISPLAY.length];
		for(int i = 0 ; i < UF_KOTEI_FOR_DISPLAY.length ; i++){ ufKoteiMappingKyoten[i] = ""; }
		String shiwakeSakuseiErrorActionKyoten = "";
		String op21MparamKidouUserKyoten = "";
		String bumonFurikaeDenpyouNoStart = "";
		String bumonFurikaeDenpyouNoEnd = "";
		String suitouchouDenpyouNoStart = "";
		String suitouchouDenpyouNoEnd = "";


		for (int i = 0; i < settingName.length; i++) {
			String[] tmpArr = settingRegexMap.get(settingName[i]);
			String settingNameWa = this.customStrategy != null ? this.customStrategy.setCustomName(tmpArr[0]) : tmpArr[0]; // カスタム項目名の設定
			String hissuFlg = tmpArr[1];
			String regex = tmpArr[2];
			//必須チェック
			if ("1".equals(hissuFlg) && isEmpty(settingVal[i])) {
				switch(settingName[i]) {
				case "denpyou_serial_no_start":
				case "denpyou_serial_no_end":
					// 開始番号(伝票番号)と終了番号(伝票番号)は経費精算(WF本体)オプションが有効の場合のみチェックする
					if(RegAccess.checkEnableKeihiSeisan()) errorList.add(settingNameWa + "の設定内容を入力してください。");
					break;
				default:
					errorList.add(settingNameWa + "の設定内容を入力してください。");
					break;
				}
			//フォーマット正規表現チェック
			} else if (! "".equals(regex) && ! isEmpty(settingVal[i]) && ! Pattern.compile("^" + regex + "$").matcher(settingVal[i]).find()) {
				errorList.add(settingNameWa + "の入力内容が正しくありません。");
			}
			//文字コードチェック
			checkSJIS(settingVal[i], settingNameWa);

			if (settingName[i].equals(Key.SHAIN_CD_RENKEI))
			{
									shainCdRenkei = settingVal[i];
			}
			if(settingName[i].equals(Key.HOUJIN_CARD_RENKEI)) houjinCardRenkei = settingVal[i];
			if(settingName[i].equals(Key.UF1_DENPYOU_ID_HANEI)) denpyouIDHanei = settingVal[i];
			if(settingName[i].equals(Key.DENPYOU_SERIAL_NO_START)) denpyouNoStart = settingVal[i];
			if(settingName[i].equals(Key.DENPYOU_SERIAL_NO_END)) denpyouNoEnd = settingVal[i];

			if(settingName[i].equals(Key.HF1_MAPPING)) hfMapping[0] = settingVal[i];
			if(settingName[i].equals(Key.HF2_MAPPING)) hfMapping[1] = settingVal[i];
			if(settingName[i].equals(Key.HF3_MAPPING)) hfMapping[2] = settingVal[i];
			if(settingName[i].equals(Key.HF4_MAPPING)) hfMapping[3] = settingVal[i];
			if(settingName[i].equals(Key.HF5_MAPPING)) hfMapping[4] = settingVal[i];
			if(settingName[i].equals(Key.HF6_MAPPING)) hfMapping[5] = settingVal[i];
			if(settingName[i].equals(Key.HF7_MAPPING)) hfMapping[6] = settingVal[i];
			if(settingName[i].equals(Key.HF8_MAPPING)) hfMapping[7] = settingVal[i];
			if(settingName[i].equals(Key.HF9_MAPPING)) hfMapping[8] = settingVal[i];
			if(settingName[i].equals(Key.HF10_MAPPING)) hfMapping[9] = settingVal[i];
			if(settingName[i].equals(Key.UF1_MAPPING)) ufMapping[0] = settingVal[i];
			if(settingName[i].equals(Key.UF2_MAPPING)) ufMapping[1] = settingVal[i];
			if(settingName[i].equals(Key.UF3_MAPPING)) ufMapping[2] = settingVal[i];
			if(settingName[i].equals(Key.UF4_MAPPING)) ufMapping[3] = settingVal[i];
			if(settingName[i].equals(Key.UF5_MAPPING)) ufMapping[4] = settingVal[i];
			if(settingName[i].equals(Key.UF6_MAPPING)) ufMapping[5] = settingVal[i];
			if(settingName[i].equals(Key.UF7_MAPPING)) ufMapping[6] = settingVal[i];
			if(settingName[i].equals(Key.UF8_MAPPING)) ufMapping[7] = settingVal[i];
			if(settingName[i].equals(Key.UF9_MAPPING)) ufMapping[8] = settingVal[i];
			if(settingName[i].equals(Key.UF10_MAPPING)) ufMapping[9] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI1_MAPPING)) ufKoteiMapping[0] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI2_MAPPING)) ufKoteiMapping[1] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI3_MAPPING)) ufKoteiMapping[2] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI4_MAPPING)) ufKoteiMapping[3] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI5_MAPPING)) ufKoteiMapping[4] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI6_MAPPING)) ufKoteiMapping[5] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI7_MAPPING)) ufKoteiMapping[6] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI8_MAPPING)) ufKoteiMapping[7] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI9_MAPPING)) ufKoteiMapping[8] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI10_MAPPING)) ufKoteiMapping[9] = settingVal[i];

			if(settingName[i].equals(Key.EBUNSHO_ENABLE_FLG)) ebunshoEnableFlg = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A001)) ebunshoSeiseiA001 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A002)) ebunshoSeiseiA002 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A003)) ebunshoSeiseiA003 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A004)) ebunshoSeiseiA004 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A005)) ebunshoSeiseiA005 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A006)) ebunshoSeiseiA006 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A009)) ebunshoSeiseiA009 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A010)) ebunshoSeiseiA010 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A011)) ebunshoSeiseiA011 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A012)) ebunshoSeiseiA012 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_A013)) ebunshoSeiseiA013 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_Z001)) ebunshoSeiseiZ001 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_Z002)) ebunshoSeiseiZ002 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SEISEI_B000)) ebunshoSeiseiB000 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A001)) ebunshoShubetsuA001 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A002)) ebunshoShubetsuA002 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A003)) ebunshoShubetsuA003 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A004)) ebunshoShubetsuA004 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A005)) ebunshoShubetsuA005 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A006)) ebunshoShubetsuA006 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A009)) ebunshoShubetsuA009 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A010)) ebunshoShubetsuA010 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A011)) ebunshoShubetsuA011 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A012)) ebunshoShubetsuA012 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_A013)) ebunshoShubetsuA013 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_Z001)) ebunshoShubetsuZ001 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_Z002)) ebunshoShubetsuZ002 = settingVal[i];
			if(settingName[i].equals(Key.EBUNSHO_SHUBETSU_B000)) ebunshoShubetsuB000 = settingVal[i];

			if(settingName[i].equals(Key.SASHIHIKI_TANKA_KAIGAI_GAIKA_HEISHU)) sashihikiTankaKaigaiGaikaHeishuCd = settingVal[i];
			if(settingName[i].equals(Key.SASHIHIKI_TANKA_KAIGAI_GAIKA)) sashihikiTankaKaigaiGaika = settingVal[i];
			if(settingName[i].equals(Key.KAIGAI_NITTOU_TANKA_GAIKA_SETTEI)) kaigaiNittouTankaGaikaSettei = settingVal[i];

			if(settingName[i].equals(Key.SHIWAKE_KBN)) shiwakeKbn = settingVal[i];
			if(settingName[i].equals(Key.KAKUTEI_UMU)) kakuteiUmu = settingVal[i];

			if(settingName[i].equals(Key.SEIKYUU_KEIJOU_NYUUROHYKU)) keijouNyuuryokuSeikyu = settingVal[i];
			if(settingName[i].equals(Key.JIDOUHIKI_KEIJOU_NYUUROHYKU)) keijouNyuuryokuJidou = settingVal[i];
			if(settingName[i].equals(Key.KEIJOUBI_DEFAULT_A001)) keijouNyuuryokuKeihi = settingVal[i];
			if(settingName[i].equals(Key.KEIJOUBI_DEFAULT_A004)) keijouNyuuryokuRyohi = settingVal[i];
			if(settingName[i].equals(Key.KEIJOUBI_DEFAULT_A011)) keijouNyuuryokuKaigaiRyohi = settingVal[i];
			if(settingName[i].equals(Key.KEIJOUBI_DEFAULT_A010)) keijouNyuuryokuKoutsuu = settingVal[i];

			if(settingName[i].equals(Key.SEIKYUU_KEIJOU_SEIGEN)) keijouSeigenSeikyu = settingVal[i];
			if(settingName[i].equals(Key.SHIHARAIIRAI_KEIJOU_SEIGEN)) keijouSeigenShiharai = settingVal[i];
			if(settingName[i].equals(Key.JIDOUHIKI_KEIJOU_SEIGEN)) keijouSeigenJidou = settingVal[i];
			if(settingName[i].equals(Key.KEIHISEISAN_KEIJOU_SEIGEN)) keijouSeigenKeihi = settingVal[i];
			if(settingName[i].equals(Key.RYOHISEISAN_KEIJOU_SEIGEN)) keijouSeigenRyohi = settingVal[i];
			if(settingName[i].equals(Key.KAIGAIRYOHISEISAN_KEIJOU_SEIGEN)) keijouSeigenKaigaiRyohi = settingVal[i];
			if(settingName[i].equals(Key.KOUTSUUHISEISAN_KEIJOU_SEIGEN)) keijouSeigenKoutsuu = settingVal[i];

			if(settingName[i].equals(Key.KIAN_NO_HANEI_RYAKUGOU)) kianNoHaneiRyakugou = settingVal[i];
			if(settingName[i].equals(Key.KIAN_NO_HANEI_BANGOU)) kianNoHaneiBangou = settingVal[i];

			if(settingName[i].equals(Key.USER_TEIGI_TODOKE_KENSAKU_KENMEI)) userTeigiTodokeKensakuKenmei = settingVal[i];
			if(settingName[i].equals(Key.USER_TEIGI_TODOKE_KENSAKU_NAIYOU)) userTeigiTodokeKensakuNaiyou = settingVal[i];

			if(settingName[i].equals(Key.OUIN_FORMAT)) ouinFormat = settingVal[i];
			if(settingName[i].equals(Key.OUIN_NAIYOU)) ouinNaiyou = settingVal[i];

			if(settingName[i].equals(Key.IC_CARD_TRAIN)) icCardTrain = settingVal[i];
			if(settingName[i].equals(Key.IC_CARD_BUS)) icCardBus = settingVal[i];

			if(settingName[i].equals(Key.SHOKUCHI_CD)) shokuchiCd = settingVal[i];

			if(settingName[i].equals(Key.MANEKIN_NAME)) kojoName = settingVal[i];
			if(settingName[i].equals(Key.MANEKIN_CD)) kojoKamokuCd = settingVal[i];
			if(settingName[i].equals(Key.MANEKIN_EDABAN)) kojoEdabanCd = settingVal[i];
			if(settingName[i].equals(Key.MANEKIN_BUMON)) kojoFutanBumonCd = settingVal[i];

			if(settingName[i].equals(Key.ICHIGEN_CD)) ichigenCd = settingVal[i];
			if(settingName[i].equals(Key.ICHIGEN_TESUURYOU)) ichigenTesuuryou = settingVal[i];
			if(settingName[i].equals(Key.ICHIGEN_TESUURYOU_KAMOKU_CD)) ichigenKamokuCd = settingVal[i];
			if(settingName[i].equals(Key.ICHIGEN_TESUURYOU_EDABAN_CD)) ichigenEdabanCd = settingVal[i];
			if(settingName[i].equals(Key.ICHIGEN_TESUURYOU_BUMON_CD)) ichigenFutanBumonCd = settingVal[i];

			if(settingName[i].equals(Key.KASHIKATAKAMOKU_CD_MIBARAI_FUTEIKI)) kamokuCdFuteiki = settingVal[i];
			if(settingName[i].equals(Key.KASHIKATAKAMOKU_CD_MIBARAI_TEIKI)) kamokuCdTeiki = settingVal[i];
			if(settingName[i].equals(Key.KASHIKATAKAMOKU_CD_SETSUBI_MIBARAI)) kamokuCdSetsubi = settingVal[i];

			if(settingName[i].equals(Key.OP21MPARAM_KAISHA_CD)) op21mparamKaishaCd = settingVal[i];
			if(settingName[i].equals(Key.UF1_DENPYOU_ID_HANEI)) uf1DenpyouIdHanei = settingVal[i];
			if(settingName[i].equals(Key.OP21MPARAM_KIDOU_USER)) op21MparamKidouUser = settingVal[i];
			if(settingName[i].equals(Key.TENPU_FILE_RENKEI)) tenpuFileRenkei = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_ERROR_ACTION)) shiwakeSakuseiErrorAction = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_HOUHOU_A001)) shiwakeSakuseiHouhouA001 = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_HOUHOU_A004)) shiwakeSakuseiHouhouA004 = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_HOUHOU_A009)) shiwakeSakuseiHouhouA009 = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_HOUHOU_A010)) shiwakeSakuseiHouhouA010 = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_HOUHOU_A011)) shiwakeSakuseiHouhouA011 = settingVal[i];
			if(settingName[i].equals(Key.OP21MPARAM_KAISHA_CD_KYOTEN)) op21mparamKaishaCdKyoten = settingVal[i];
			if(settingName[i].equals(Key.SHOKUCHI_CD_KYOTEN)) shokuchiCdKyoten =settingVal[i];
			if(settingName[i].equals(Key.OP21MPARAM_KIDOU_USER_KYOTEN)) op21MparamKidouUserKyoten = settingVal[i];
			if(settingName[i].equals(Key.UF1_DENPYOU_ID_HANEI_KYOTEN)) uf1DenpyouIdHaneiKyoten = settingVal[i];
			if(settingName[i].equals(Key.SHAIN_CD_RENKEI_KYOTEN)) shainCdRenkeiKyoten = settingVal[i];
			if(settingName[i].equals(Key.HF1_MAPPING_KYOTEN)) hfMappingKyoten[0] = settingVal[i];
			if(settingName[i].equals(Key.HF2_MAPPING_KYOTEN)) hfMappingKyoten[1] = settingVal[i];
			if(settingName[i].equals(Key.HF3_MAPPING_KYOTEN)) hfMappingKyoten[2] = settingVal[i];
			if(settingName[i].equals(Key.HF4_MAPPING_KYOTEN)) hfMappingKyoten[3] = settingVal[i];
			if(settingName[i].equals(Key.HF5_MAPPING_KYOTEN)) hfMappingKyoten[4] = settingVal[i];
			if(settingName[i].equals(Key.HF6_MAPPING_KYOTEN)) hfMappingKyoten[5] = settingVal[i];
			if(settingName[i].equals(Key.HF7_MAPPING_KYOTEN)) hfMappingKyoten[6] = settingVal[i];
			if(settingName[i].equals(Key.HF8_MAPPING_KYOTEN)) hfMappingKyoten[7] = settingVal[i];
			if(settingName[i].equals(Key.HF9_MAPPING_KYOTEN)) hfMappingKyoten[8] = settingVal[i];
			if(settingName[i].equals(Key.HF10_MAPPING_KYOTEN)) hfMappingKyoten[9] = settingVal[i];
			if(settingName[i].equals(Key.UF1_MAPPING_KYOTEN)) ufMappingKyoten[0] = settingVal[i];
			if(settingName[i].equals(Key.UF2_MAPPING_KYOTEN)) ufMappingKyoten[1] = settingVal[i];
			if(settingName[i].equals(Key.UF3_MAPPING_KYOTEN)) ufMappingKyoten[2] = settingVal[i];
			if(settingName[i].equals(Key.UF4_MAPPING_KYOTEN)) ufMappingKyoten[3] = settingVal[i];
			if(settingName[i].equals(Key.UF5_MAPPING_KYOTEN)) ufMappingKyoten[4] = settingVal[i];
			if(settingName[i].equals(Key.UF6_MAPPING_KYOTEN)) ufMappingKyoten[5] = settingVal[i];
			if(settingName[i].equals(Key.UF7_MAPPING_KYOTEN)) ufMappingKyoten[6] = settingVal[i];
			if(settingName[i].equals(Key.UF8_MAPPING_KYOTEN)) ufMappingKyoten[7] = settingVal[i];
			if(settingName[i].equals(Key.UF9_MAPPING_KYOTEN)) ufMappingKyoten[8] = settingVal[i];
			if(settingName[i].equals(Key.UF10_MAPPING_KYOTEN)) ufMappingKyoten[9] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI1_MAPPING_KYOTEN)) ufKoteiMappingKyoten[0] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI2_MAPPING_KYOTEN)) ufKoteiMappingKyoten[1] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI3_MAPPING_KYOTEN)) ufKoteiMappingKyoten[2] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI4_MAPPING_KYOTEN)) ufKoteiMappingKyoten[3] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI5_MAPPING_KYOTEN)) ufKoteiMappingKyoten[4] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI6_MAPPING_KYOTEN)) ufKoteiMappingKyoten[5] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI7_MAPPING_KYOTEN)) ufKoteiMappingKyoten[6] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI8_MAPPING_KYOTEN)) ufKoteiMappingKyoten[7] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI9_MAPPING_KYOTEN)) ufKoteiMappingKyoten[8] = settingVal[i];
			if(settingName[i].equals(Key.UF_KOTEI10_MAPPING_KYOTEN)) ufKoteiMappingKyoten[9] = settingVal[i];
			if(settingName[i].equals(Key.TENPU_FILE_RENKEI_KYOTEN)) tenpuFileRenkeiKyoten = settingVal[i];
			if(settingName[i].equals(Key.SHIWAKE_SAKUSEI_ERROR_ACTION_KYOTEN)) shiwakeSakuseiErrorActionKyoten = settingVal[i];
			if(settingName[i].equals(Key.BUMON_FURIKAE_DENPYOU_SERIAL_NO_START)) bumonFurikaeDenpyouNoStart = settingVal[i];
			if(settingName[i].equals(Key.BUMON_FURIKAE_DENPYOU_SERIAL_NO_END)) bumonFurikaeDenpyouNoEnd = settingVal[i];
			if(settingName[i].equals(Key.SUITOUCHOU_DENPYOU_SERIAL_NO_START)) suitouchouDenpyouNoStart = settingVal[i];
			if(settingName[i].equals(Key.SUITOUCHOU_DENPYOU_SERIAL_NO_END)) suitouchouDenpyouNoEnd = settingVal[i];
			
			// カスタマイズ用Strategyクラスが存在する場合
			if(this.customStrategy != null)
			{
				this.customStrategy.keyName = this.settingName[i];
				this.customStrategy.targetSettingValue = this.settingVal[i];
				this.customStrategy.errorList = this.errorList;
				this.customStrategy.setTargetValueBykeyName();
			}
		}

		// 元々ここにearly returnがあったが除去。カスタマイズ専用で共通には含めないなら戻してください

		// 代行機能の相関チェック
		if( EteamNaibuCodeSetting.KINOU_SEIGYO_KBN.YUUKOU.equals(daikou) ){
			if( (userDaikouShitei == null || EteamNaibuCodeSetting.KINOU_SEIGYO_KBN.MUKOU.equals(userDaikouShitei)) && (kanriDaikouShitei == null || EteamNaibuCodeSetting.KINOU_SEIGYO_KBN.MUKOU.equals(kanriDaikouShitei)) ){
				errorList.add("代行機能を有効にする場合、一般ユーザーか管理者どちらかの代行者指定許可を有効にしてください。");
			}
		}

		// 社員コード連携の相関チェック
		if( Open21Env.getVersion() == Version.DE3 && isNotEmpty(shainCdRenkei) ) {
			// DE3版に限り、会社情報のUFx使用フラグが0以外で社員コード連携がUFxを使用する設定はエラー
			switch (shainCdRenkei) {
			case "UF1":
				if("0".equals(KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG))) {
					errorList.add("ユニバーサルフィールド１を使用しない設定になっているため、社員コード連携「ユニバーサルフィールド１に連携する」は入力できません。");
				}
				break;
			case "UF2":
				if("0".equals(KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG))) {
					errorList.add("ユニバーサルフィールド２を使用しない設定になっているため、社員コード連携「ユニバーサルフィールド２に連携する」は入力できません。");
				}
				break;
			case "UF3":
				if("0".equals(KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG))) {
					errorList.add("ユニバーサルフィールド３を使用しない設定になっているため、社員コード連携「ユニバーサルフィールド３に連携する」は入力できません。");
				}
				break;
			}
		}

		// 法人カード識別用番号連携の相関チェック
		if( Open21Env.getVersion() == Version.DE3 && isNotEmpty(houjinCardRenkei) ) {
			// DE3版に限り、会社情報のUFx使用フラグが0以外で法人カード識別用番号連携がUFxを使用する設定はエラー
			switch (houjinCardRenkei) {
			case "UF1":
				if("0".equals(KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG))) {
					errorList.add("ユニバーサルフィールド１を使用しない設定になっているため、法人カード識別用番号連携「ユニバーサルフィールド１に連携する」は入力できません。");
				}
				break;
			case "UF2":
				if("0".equals(KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG))) {
					errorList.add("ユニバーサルフィールド２を使用しない設定になっているため、法人カード識別用番号連携「ユニバーサルフィールド２に連携する」は入力できません。");
				}
				break;
			case "UF3":
				if("0".equals(KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG))) {
					errorList.add("ユニバーサルフィールド３を使用しない設定になっているため、法人カード識別用番号連携「ユニバーサルフィールド３に連携する」は入力できません。");
				}
				break;
			}
		}

		// 社員コード連携・伝票IDの連携先（旧名：ユニバーサルフィールド伝票ID反映）相関チェック
		if(isNotEmpty(shainCdRenkei) && isNotEmpty(denpyouIDHanei)) {
			if(denpyouIDHanei.length() >= 3 && shainCdRenkei.length() >= 3){
				// ユニバーサルフィールド伝票ID反映先が社員コード連携先ヘッダーフィールドと同一の場合はエラー
				if(denpyouIDHanei.substring(0,2).equals("HF")){
					if(denpyouIDHanei.equals(shainCdRenkei)){
						errorList.add("伝票IDの連携先が社員コードの連携先と重複しています。");
					}
				// ユニバーサルフィールド伝票ID反映先が社員コード連携先ユニバーサルフィールドと同一の場合はエラー
				}else if(denpyouIDHanei.substring(0,2).equals("UF")){
					if(denpyouIDHanei.equals(shainCdRenkei)){
						errorList.add("伝票IDの連携先が社員コードの連携先と重複しています。");
					}
				}
			}
		}

		// 社員コード連携・法人カード識別用番号連携相関チェック
		if(isNotEmpty(shainCdRenkei) && isNotEmpty(houjinCardRenkei)) {
			if(houjinCardRenkei.length() >= 3 && shainCdRenkei.length() >= 3){
				// 法人カード識別用番号連携先が社員コード連携先ユニバーサルフィールドと同一の場合はエラー
				if(houjinCardRenkei.substring(0,2).equals("UF")){
					if(houjinCardRenkei.equals(shainCdRenkei)){
						errorList.add("法人カード識別用番号の連携先が社員コードの連携先と重複しています。");
					}
				}
			}
			if (houjinCardRenkei.equals("T") && houjinCardRenkei.equals(shainCdRenkei)) {
				errorList.add("法人カード識別用番号連携が「摘要コードに反映する」の場合は、社員コード連携「摘要コードに連携する」は入力できません。");
			}
		}

		// 法人カード識別用番号連携・ユニバーサルフィールド伝票ID反映相関チェック
		if(isNotEmpty(houjinCardRenkei) && isNotEmpty(denpyouIDHanei)) {
			if(denpyouIDHanei.length() >= 3 && houjinCardRenkei.length() >= 3){
				// ユニバーサルフィールド伝票ID反映先が法人カード識別用番号連携先ユニバーサルフィールドと同一の場合はエラー
				if(denpyouIDHanei.substring(0,2).equals("UF")){
					if(denpyouIDHanei.equals(houjinCardRenkei)){
						errorList.add("伝票IDの連携先が法人カード識別用番号の連携先と重複しています。");
					}
				}
			}
		}

		// ヘッダーフィールドマッピングの重複チェック
		for (int i = 0;  i < HF_FOR_DISPLAY.length; i++) {
			if(isNotEmpty(hfMapping[i])){
				for (int j = i+1; j < HF_FOR_DISPLAY.length; j++) {
					if (! ("0".equals(hfMapping[i])) ){
						if( hfMapping[i].equals(hfMapping[j]) ){
							errorList.add(HF_FOR_DISPLAY[i] + "が" + HF_FOR_DISPLAY[j] + "と重複しています。");
						}
					}
				}
			}
		}

		// ユニバーサルフィールドマッピングの重複チェック
		for (int i = 0;  i < UF_FOR_DISPLAY.length; i++) {
			if(isNotEmpty(ufMapping[i])){
				for (int j = i+1; j < UF_FOR_DISPLAY.length; j++) {
					if (! ("0".equals(ufMapping[i])) ){
						if( ufMapping[i].equals(ufMapping[j]) ){
							errorList.add(UF_FOR_DISPLAY[i] + "が" + UF_FOR_DISPLAY[j] + "と重複しています。");
						}
					}
				}
				for (int j = 0; j < UF_KOTEI_FOR_DISPLAY.length; j++) {
					if (! ("0".equals(ufMapping[i])) ){
						if( ufMapping[i].equals(ufKoteiMapping[j]) ){
							errorList.add(UF_FOR_DISPLAY[i] + "が" + UF_KOTEI_FOR_DISPLAY[j] + "と重複しています。");
						}
					}
				}
			}
		}

		// ユニバーサルフィールドマッピングの重複チェック
		for (int i = 0;  i < UF_KOTEI_FOR_DISPLAY.length; i++) {
			if(isNotEmpty(ufKoteiMapping[i])){
				for (int j = i+1; j < UF_KOTEI_FOR_DISPLAY.length; j++) {
					if (! ("0".equals(ufKoteiMapping[i])) ){
						if( ufKoteiMapping[i].equals(ufKoteiMapping[j]) ){
							errorList.add(UF_KOTEI_FOR_DISPLAY[i] + "が" + UF_KOTEI_FOR_DISPLAY[j] + "と重複しています。");
						}
					}
				}
			}
		}

		// ユニバーサルフィールド伝票ID反映先とヘッダー・ユニバーサルフィールドマッピングの重複チェック
		if(denpyouIDHanei.length() >= 3){
			String numStr = denpyouIDHanei.substring(2);
			if( "HF".equals(denpyouIDHanei.substring(0,2)) ){
				for (int i = 0; i < HF_FOR_DISPLAY.length; i++) {
					if(numStr.equals(hfMapping[i])){
						errorList.add("伝票IDの連携先が" + HF_FOR_DISPLAY[i] + "と重複しています。");
					}
				}
			}
			if( "UF".equals(denpyouIDHanei.substring(0,2)) ){
				for (int i = 0; i < UF_FOR_DISPLAY.length; i++) {
					if(numStr.equals(ufMapping[i])){
						errorList.add("伝票IDの連携先が" + UF_FOR_DISPLAY[i] + "と重複しています。");
					}
				}
				for (int i = 0; i < UF_KOTEI_FOR_DISPLAY.length; i++) {
					if(numStr.equals(ufKoteiMapping[i])){
						errorList.add("伝票IDの連携先が" + UF_KOTEI_FOR_DISPLAY[i] + "と重複しています。");
					}
				}
			}
		}

		//20220518 追加
		//20220622条件追加 de3にはHF/UFマッピングがないのでチェックの必要がない
		if(Open21Env.getVersion() == Version.SIAS && isNotEmpty(shainCdRenkei)) {
			if(shainCdRenkei.length() >= 3) {
				String numStr = shainCdRenkei.substring(2);
				if( "HF".equals(shainCdRenkei.substring(0,2)) ) {
					for (int i = 0; i < HF_FOR_DISPLAY.length; i++) {
						if(numStr.equals(hfMapping[i])) {
							errorList.add("社員コードの連携先が" + HF_FOR_DISPLAY[i] + "と重複しています。");
						}
					}
				}
				if( "UF".equals(shainCdRenkei.substring(0,2)) ) {
					for (int i = 0; i < UF_FOR_DISPLAY.length; i++) {
						if(numStr.equals(ufMapping[i])){
							errorList.add("社員コードの連携先が" + UF_FOR_DISPLAY[i] + "と重複しています。");
						}
					}
					for (int i = 0; i < UF_KOTEI_FOR_DISPLAY.length; i++) {
						if(numStr.equals(ufKoteiMapping[i])){
							errorList.add("社員コードの連携先が" + UF_KOTEI_FOR_DISPLAY[i] + "と重複しています。");
						}
					}
				}
			}
		}

		if(Open21Env.getVersion() == Version.SIAS && isNotEmpty(houjinCardRenkei)) {
			if(houjinCardRenkei.length() >= 3) {
				String numStr = houjinCardRenkei.substring(2);
				if( "UF".equals(houjinCardRenkei.substring(0,2)) ) {
					for (int i = 0; i < UF_FOR_DISPLAY.length; i++) {
						if(numStr.equals(ufMapping[i])){
							errorList.add("法人カード識別用番号の連携先が" + UF_FOR_DISPLAY[i] + "と重複しています。");
						}
					}
					for (int i = 0; i < UF_KOTEI_FOR_DISPLAY.length; i++) {
						if(numStr.equals(ufKoteiMapping[i])){
							errorList.add("法人カード識別用番号の連携先が" + UF_KOTEI_FOR_DISPLAY[i] + "と重複しています。");
						}
					}
				}
			}
		}

		// 仕訳区分が2(拡張・部署入出力用)の場合、確定有無が入力されていない場合はエラー
		if("2".equals(shiwakeKbn)){
			if(isEmpty(kakuteiUmu)){
				errorList.add("確定有無の設定内容を入力してください。");
			}
		}


		// 開始番号(伝票番号) ≧ 終了番号(伝票番号) の場合はエラー
		if(isNotEmpty(denpyouNoStart) && isNotEmpty(denpyouNoEnd)) {
			if(errorList.isEmpty() && Integer.parseInt(denpyouNoStart) >= Integer.parseInt(denpyouNoEnd)) errorList.add("終了番号(伝票番号)は開始番号(伝票番号)より後を指定してください。");
		}

		// 仕訳作成方法と計上日入力方法の相関チェック
		if (RegAccess.checkEnableKeihiSeisan()){
			if(shiwakeSakuseiHouhouA001.equals("3") && keijouNyuuryokuKeihi.equals("3")){
				errorList.add("仕訳作成方法(経費立替精算)が「現金主義」の場合、経費立替精算(計上日入力)に「申請者が計上日を入力」を入力できません。");
			}
			if(shiwakeSakuseiHouhouA004.equals("3") && keijouNyuuryokuRyohi.equals("3")){
				errorList.add("仕訳作成方法(出張旅費精算)が「現金主義」の場合、出張旅費精算(計上日入力)に「申請者が計上日を入力」を入力できません。");
			}
			if(shiwakeSakuseiHouhouA009.equals("3") && keijouNyuuryokuJidou.equals("1")){
				errorList.add("仕訳作成方法(自動引落)が「現金主義」の場合、自動引落伝票(計上日入力)に「申請者が計上日を入力」を入力できません。");
			}
			if(shiwakeSakuseiHouhouA010.equals("3") && keijouNyuuryokuKoutsuu.equals("3")){
				errorList.add("仕訳作成方法(交通費精算)が「現金主義」の場合、交通費精算(計上日入力)に「申請者が計上日を入力」を入力できません。");
			}
			if(shiwakeSakuseiHouhouA011.equals("3") && keijouNyuuryokuKaigaiRyohi.equals("3")){
				errorList.add("仕訳作成方法(海外出張旅費精算)が「現金主義」の場合、海外出張旅費精算(計上日入力)に「申請者が計上日を入力」を入力できません。");
			}
		}

		// e文書使用制限オプションが有効、かつe文書有効フラグオン時、生成対象となる伝票種別が最低1つあるかチェック
		if(ebunshoFlg.equals("1") && ("1".equals(ebunshoEnableFlg) || "2".equals(ebunshoEnableFlg))){
			boolean isNotKeihiUseEbunsho = Stream
					.of(ebunshoSeiseiA001, ebunshoSeiseiA002, ebunshoSeiseiA003, ebunshoSeiseiA004, ebunshoSeiseiA005, ebunshoSeiseiA006, ebunshoSeiseiA009, ebunshoSeiseiA010, ebunshoSeiseiA011, ebunshoSeiseiA012, ebunshoSeiseiA013, ebunshoSeiseiB000)
					.allMatch(item -> item.equals("0"));
			boolean isNotKyotenUseEbunsho = Stream
							.of(ebunshoSeiseiZ001, ebunshoSeiseiZ002)
							.allMatch(item -> item.equals("0"));
			if ((RegAccess.checkEnableKeihiSeisan() && RegAccess.checkEnableZaimuKyotenOption() && isNotKeihiUseEbunsho && isNotKyotenUseEbunsho) // 両方使用
					|| (RegAccess.checkEnableKeihiSeisan()  && !RegAccess.checkEnableZaimuKyotenOption() && isNotKeihiUseEbunsho) // 経費精算のみ使用
					|| (!RegAccess.checkEnableKeihiSeisan()  && RegAccess.checkEnableZaimuKyotenOption() && isNotKyotenUseEbunsho)){ // 拠点のみ使用
				errorList.add("e文書番号生成対象の伝票種別が設定されていません。");
			}

			// e文書種別形式チェック
			if (RegAccess.checkEnableKeihiSeisan()){
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA001, true)) {
					errorList.add("書類種別(経費立替精算)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA002, true)) {
					errorList.add("書類種別(経費伺い申請（仮払申請）)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA003, true) && seikyuushoBaraiOn) {
					errorList.add("書類種別(請求書払い申請)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA004, true)) {
					errorList.add("書類種別(出張旅費精算)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA005, true)) {
					errorList.add("書類種別(出張伺い申請（仮払申請）)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA006, true)) {
					errorList.add("書類種別(通勤定期申請)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA009, true)) {
					errorList.add("書類種別(自動引落伝票)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA010, true)) {
					errorList.add("書類種別(交通費精算)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA011, true)) {
					errorList.add("書類種別(海外出張旅費精算)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA012, true)) {
					errorList.add("書類種別(海外出張伺い申請（仮払申請）)の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuA013, true) && shiharaiIraiOn) {
					errorList.add("書類種別(支払依頼申請)の入力内容が正しくありません。");
				}
			
				if (!isOKEbunshoShubetsu(ebunshoShubetsuB000, true) && kaniTodokeOn) {
					errorList.add("書類種別(ユーザー定義届出)の入力内容が正しくありません。");
				}
			}
			if(RegAccess.checkEnableZaimuKyotenOption()) {
				if (!isOKEbunshoShubetsu(ebunshoShubetsuZ001, false)) {
					errorList.add("書類種別(振替伝票(拠点入力))の入力内容が正しくありません。");
				}
				if (!isOKEbunshoShubetsu(ebunshoShubetsuZ002, false)) {
					errorList.add("書類種別(現預金出納帳)の入力内容が正しくありません。");
				}
			}
		}

		//差引単価（海外）外貨の幣種コードのマスター存在チェック
		if(isNotEmpty(sashihikiTankaKaigaiGaikaHeishuCd)) {
			if(null == masterLogic.findHeishuCd(sashihikiTankaKaigaiGaikaHeishuCd)) {
				errorList.add("差引単価（海外）外貨の幣種コードには、使用可能な幣種コードを入力してください。");
			}
		}

		// 外貨入力ONで海外日当用外貨設定がONなら差引単価（外貨）の入力が必要
		if(isEmpty(sashihikiTankaKaigaiGaika)) {
			if("1".equals(gaikaNyuuryoku) && "1".equals(kaigaiNittouTankaGaikaSettei)) {
				errorList.add("海外用日当の単価を外貨を使用する設定にする場合、差引単価（海外）外貨にも入力が必要です。");
			}
		}

		//予算執行管理オプションが「予算執行管理Aあり」の場合
		String yosanShikkouOption = RegAccess.checkEnableYosanShikkouOption();
		if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){

			//起案番号のHF転記先①略号が0以外なら他のHF使用項目との重複チェック
			if(Open21Env.getVersion() == Version.SIAS && !("0".equals(kianNoHaneiRyakugou))){

				//ユニバーサルフィールド伝票ID反映
				if(denpyouIDHanei.length() >= 3){
					if( "HF".equals(denpyouIDHanei.substring(0,2)) ){
						String numStr = denpyouIDHanei.substring(2);
						if( kianNoHaneiRyakugou.equals(numStr) ){
							errorList.add("起案番号のHF転記先①略号が伝票IDの連携先と重複しています。");
						}
					}
				}
				//社員コード連携
				if(shainCdRenkei.length() >= 3){
					if( "HF".equals(shainCdRenkei.substring(0,2)) ){
						String numStr = shainCdRenkei.substring(2);
						if( kianNoHaneiRyakugou.equals(numStr) ){
							errorList.add("起案番号のHF転記先①略号が社員コードの連携先と重複しています。");
						}
					}
				}
				//ヘッダーフィールドマッピング
				for (int i = 0; i < HF_FOR_DISPLAY.length; i++) {
					if( kianNoHaneiRyakugou.equals(hfMapping[i]) ){
						errorList.add("起案番号のHF転記先①略号が" + HF_FOR_DISPLAY[i] + "と重複しています。");
					}
				}
			}

			//起案番号のHF転記先②番号が0以外なら他のHF使用項目との重複チェック
			if(Open21Env.getVersion() == Version.SIAS && !("0".equals(kianNoHaneiBangou))){

				//ユニバーサルフィールド伝票ID反映
				if(denpyouIDHanei.length() >= 3){
					if( "HF".equals(denpyouIDHanei.substring(0,2)) ){
						String numStr = denpyouIDHanei.substring(2);
						if( kianNoHaneiBangou.equals(numStr) ){
							errorList.add("起案番号のHF転記先②番号が伝票IDの連携先と重複しています。");
						}
					}
				}
				//社員コード連携
				if(shainCdRenkei.length() >= 3){
					if( "HF".equals(shainCdRenkei.substring(0,2)) ){
						String numStr = shainCdRenkei.substring(2);
						if( kianNoHaneiBangou.equals(numStr) ){
							errorList.add("起案番号のHF転記先②番号が社員コードの連携先と重複しています。");
						}
					}
				}
				//ヘッダーフィールドマッピング
				for (int i = 0; i < HF_FOR_DISPLAY.length; i++) {
					if( kianNoHaneiBangou.equals(hfMapping[i]) ){
						errorList.add("起案番号のHF転記先②番号が" + HF_FOR_DISPLAY[i] + "と重複しています。"); }
				}

				//起案番号のHF転記先①略号
				if( kianNoHaneiBangou.equals(kianNoHaneiRyakugou) ){
					errorList.add("起案番号のHF転記先②番号が起案番号のHF転記先①略号と重複しています。");
				}
			}

		}

		//申請者の計上日入力を許容するなら申請者の計上日制限は必須
		if (keijouNyuuryokuSeikyu.equals("1") && keijouSeigenSeikyu.equals("") && seikyuushoBaraiOn) {
			errorList.add("請求書払い申請(申請者の計上日制限)の設定内容を入力してください。");
		}
		if (keijouNyuuryokuShiharai.equals("1") && keijouSeigenShiharai.equals("") && shiharaiIraiOn) {
			errorList.add("支払依頼申請(申請者の計上日制限)の設定内容を入力してください。");
		}
		if (keijouNyuuryokuJidou.equals("1") && keijouSeigenJidou.equals("")) {
			errorList.add("自動引落伝票(申請者の計上日制限)の設定内容を入力してください。");
		}
		if (keijouNyuuryokuKeihi.equals("3") && keijouSeigenKeihi.equals("")) {
			errorList.add("経費立替精算(申請者の計上日制限)の設定内容を入力してください。");
		}
		if (keijouNyuuryokuRyohi.equals("3") && keijouSeigenRyohi.equals("")) {
			errorList.add("出張旅費精算(申請者の計上日制限)の設定内容を入力してください。");
		}
		if (keijouNyuuryokuKaigaiRyohi.equals("3") && keijouSeigenKaigaiRyohi.equals("")) {
			errorList.add("海外出張旅費精算(申請者の計上日制限)の設定内容を入力してください。");
		}
		if (keijouNyuuryokuKoutsuu.equals("3") && keijouSeigenKoutsuu.equals("")) {
			errorList.add("交通費精算(申請者の計上日制限)の設定内容を入力してください。");
		}


		//ユーザー定義届書の件名・内容項目名が同一ならエラー
		if(userTeigiTodokeKensakuKenmei.equals(userTeigiTodokeKensakuNaiyou)  && kaniTodokeOn){
			errorList.add("ユーザー定義届書の件名・内容の項目名に同一値は設定できません。");
		}

		//押印枠数の出力形式
		if("0".equals(ouinNaiyou)){
			if("W".equals(ouinFormat)||"X".equals(ouinFormat)){
				errorList.add("押印枠　表示内容は非表示以外を指定してください。");
			}
		}

		//ICカードデフォルト交通手段(電車)
		if(isNotEmpty(icCardTrain)){
			//交通手段マスターに存在する値で、数量入力タイプ・証憑書類必須フラグ0のデータかチェック
			boolean koutsuExist = false;
			boolean suuryoChk = false;
			boolean shouhyouChk = false;
			List<GMap> koutsuList = masterLogic.loadKoutsuuShudan();
			for(GMap mp : koutsuList){
				String koutsuStr = (String)mp.get("koutsuu_shudan");
				String suuryoStr = (String)mp.get("suuryou_nyuryoku_type");
				String shouhyouStr = (String)mp.get("shouhyou_shorui_hissu_flg");
				if(icCardTrain.equals(koutsuStr)){
					koutsuExist = true;
					if("0".equals(suuryoStr)){
						suuryoChk = true;
					}
					if("0".equals(shouhyouStr)) {
						shouhyouChk = true;
					}
					break;
				}
			}
			if(!koutsuExist){
				errorList.add("ICカードデフォルト交通手段には国内用交通手段マスターに登録されている文言を指定してください。");
			}else if(!suuryoChk) {
				errorList.add("ICカードデフォルト交通手段には数量入力タイプ「0」の交通手段を指定してください。");
			}else if(!shouhyouChk) {
				errorList.add("ICカードデフォルト交通手段には証憑書類必須フラグ「0」の交通手段を指定してください。");
			}
		}

		//ICカードデフォルト交通手段(バス)
		if(isNotEmpty(icCardBus)){
			//交通手段マスターに存在する値で、数量入力タイプ・証憑書類必須フラグ0のデータかチェック
			boolean koutsuExist = false;
			boolean suuryoChk = false;
			boolean shouhyouChk = false;
			List<GMap> koutsuList = masterLogic.loadKoutsuuShudan();
			for(GMap mp : koutsuList){
				String koutsuStr = (String)mp.get("koutsuu_shudan");
				String suuryoStr = (String)mp.get("suuryou_nyuryoku_type");
				String shouhyouStr = (String)mp.get("shouhyou_shorui_hissu_flg");
				if(icCardBus.equals(koutsuStr)){
					koutsuExist = true;
					if("0".equals(suuryoStr)){
						suuryoChk = true;
					}
					if("0".equals(shouhyouStr)) {
						shouhyouChk = true;
					}
					break;
				}
			}
			if(!koutsuExist){
				errorList.add("ICカード「バス/路面等」交通手段には国内用交通手段マスターに登録されている文言を指定してください。");
			}else if(!suuryoChk) {
				errorList.add("ICカード「バス/路面等」交通手段には数量入力タイプ「0」の交通手段を指定してください。");
			}else if(!shouhyouChk) {
				errorList.add("ICカード「バス/路面等」交通手段には証憑書類必須フラグ「0」の交通手段を指定してください。");
			}
		}

		//支払い依頼オプションがONの場合のみチェック
		if(shiharaiIraiOn) {
			//控除
			if(! isEmpty(kojoName)){
				try {
					if(kojoName.getBytes(EteamConst.Encoding.MS932).length > 20){
						errorList.add("控除項目　項目名称は20バイト以内で入力してください。");
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
	
				if(isEmpty(kojoKamokuCd)){
					errorList.add("控除項目　勘定科目コードを入力してください。");
				}
			}
	
			//一見先
			if(! isEmpty(ichigenCd)){
				if(isEmpty(ichigenTesuuryou)){
					errorList.add("一見先　振込手数料を入力してください。");
				}
				if(isEmpty(ichigenKamokuCd)){
					errorList.add("一見先　振込手数料勘定科目コードを入力してください。");
				}
			}
	
			//諸口の科目
			if(isNotEmpty(shokuchiCd)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.kamokuNm = "諸口コード";
				s.kamokuCd = shokuchiCd;
				kaikeiLogic.checkShiwake(s, errorList);
			}
	
			//控除項目の科目等
			if(isNotEmpty(kojoKamokuCd)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.kamokuNm = "控除項目　勘定科目コード";
				s.kamokuCd = kojoKamokuCd;
				s.kamokuEdabanNm = "控除項目　勘定科目枝番コード";
				s.kamokuEdabanCd = kojoEdabanCd;
				s.futanBumonNm = "控除項目　負担部門コード";
				s.futanBumonCd = kojoFutanBumonCd;
				kaikeiLogic.checkShiwake(s, errorList);
			}
	
			//一見先の科目等
			if(isNotEmpty(ichigenCd)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.torihikisakiNm = "一見先　取引先コード";
				s.torihikisakiCd = ichigenCd;
				kaikeiLogic.checkTorihikisaki(s, errorList);
			}
			if(isNotEmpty(ichigenKamokuCd)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.kamokuNm = "一見先　振込手数料勘定科目コード";
				s.kamokuCd = ichigenKamokuCd;
				if(!(EteamConst.ShiwakeConst.FUTAN).equals(ichigenFutanBumonCd) && !(EteamConst.ShiwakeConst.DAIHYOUBUMON).equals(ichigenFutanBumonCd)) {
					s.futanBumonNm = "一見先　振込手数料負担部門コード";
					s.futanBumonCd = ichigenFutanBumonCd;
				}
				s.kamokuEdabanNm = "一見先　振込手数料勘定科目枝番コード";
				s.kamokuEdabanCd = ichigenEdabanCd;
				kaikeiLogic.checkShiwake(s, errorList);
			}
	
			//一見先　振込手数料負担部門コード
			if( (EteamConst.ShiwakeConst.FUTAN).equals(ichigenFutanBumonCd) ) {
				//登録済み全支払依頼の取引借方負担部門が<FUTAN>であるかチェック(ブランクも不可)
				
				List<GMap> list = kcLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI);
				if(!list.isEmpty()) {
					for (GMap record : list) {
						String toriFutanBumonCd = (String)record.get("kari_futan_bumon_cd");
						if(!EteamConst.ShiwakeConst.FUTAN.equals(toriFutanBumonCd) ) {
							errorList.add("支払依頼申請取引で任意部門以外の借方負担部門コードが設定されている場合、「一見先　振込手数料負担部門コード」に任意部門は設定できません。");
							break;
						}
					}
				}
			}
	
			//未払い不定期　貸方科目コード、未払い定期　　貸方科目コード、設備未払い　　貸方科目コード
			if(isNotEmpty(kamokuCdFuteiki)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.kamokuNm = "未払い不定期　貸方科目コード";
				s.kamokuCd = kamokuCdFuteiki;
				kaikeiLogic.checkShiwake(s, errorList);
			}
			if(isNotEmpty(kamokuCdTeiki)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.kamokuNm = "未払い定期　　貸方科目コード";
				s.kamokuCd = kamokuCdTeiki;
				kaikeiLogic.checkShiwake(s, errorList);
			}
			if(isNotEmpty(kamokuCdFuteiki)){
				s = kaikeiLogic.new ShiwakeCheckData();
				s.kamokuNm = "設備未払い　　貸方科目コード";
				s.kamokuCd = kamokuCdSetsubi;
				kaikeiLogic.checkShiwake(s, errorList);
			}
		}

		// 以下財務拠点オプション使用時のみチェック
		if (!RegAccess.checkEnableZaimuKyotenOption())
		{
			return;
		}

		// OPEN21連携とOPEN21連携（拠点入力）の相関チェック
		// 画面入力で同じ値が入るよう制御しているが念の為実施する
		if(!op21mparamKaishaCdKyoten.isEmpty()) {
			if(!op21mparamKaishaCdKyoten.equals(op21mparamKaishaCd)) {
				errorList.add("OPEN21連携（拠点入力）の会社コードにはOPEN21連携の会社コードと同じ値を入力してください。");
			}
		}
		if(!shokuchiCdKyoten.isEmpty()) {
			if(!shokuchiCdKyoten.equals(shokuchiCd)) {
				errorList.add("OPEN21連携（拠点入力）の諸口コードにはOPEN21連携の諸口コードと同じ値を入力してください。");
			}
		}
		if(!op21MparamKidouUserKyoten.isEmpty()) {
			if(!op21MparamKidouUserKyoten.equals(op21MparamKidouUser)) {
				errorList.add("OPEN21連携（拠点入力）の起動ユーザーにはOPEN21連携の起動ユーザーと同じ値を入力してください。");
			}
		}
		if(!uf1DenpyouIdHaneiKyoten.isEmpty()) {
			if(!uf1DenpyouIdHaneiKyoten.equals(uf1DenpyouIdHanei)) {
				errorList.add("OPEN21連携（拠点入力）のユニバーサルフィールド伝票ID反映にはOPEN21連携のユニバーサルフィールド伝票ID反映と同じ値を入力してください。");
			}
		}
		if(!shainCdRenkeiKyoten.isEmpty()) {
			if(!shainCdRenkeiKyoten.equals(shainCdRenkei)) {
				errorList.add("OPEN21連携（拠点入力）の社員コード連携にはOPEN21連携の社員コード連携と同じ値を入力してください。");
			}
		}
		for (int i = 0; i < HF_FOR_DISPLAY.length; i++) {
			if(!hfMappingKyoten[i].isEmpty()) {
				if(!hfMappingKyoten[i].equals(hfMapping[i])) {
					errorList.add("OPEN21連携（拠点入力）の"  + HF_FOR_DISPLAY[i] + "にはOPEN21連携の"  + HF_FOR_DISPLAY[i] + "と同じ値を入力してください。");
				}
			}
		}
		for (int i = 0; i < UF_FOR_DISPLAY.length; i++) {
			if(!ufMappingKyoten[i].isEmpty()) {
				if(!ufMappingKyoten[i].equals(ufMapping[i])) {
					errorList.add("OPEN21連携（拠点入力）の"  + UF_FOR_DISPLAY[i] + "にはOPEN21連携の"  + UF_FOR_DISPLAY[i] + "と同じ値を入力してください。");
				}
			}
		}
		for (int i = 0; i < UF_KOTEI_FOR_DISPLAY.length; i++) {
			if(!ufKoteiMappingKyoten[i].isEmpty()) {
				if(!ufKoteiMappingKyoten[i].equals(ufKoteiMapping[i])) {
					errorList.add("OPEN21連携（拠点入力）の"  + UF_KOTEI_FOR_DISPLAY[i] + "にはOPEN21連携の"  + UF_KOTEI_FOR_DISPLAY[i] + "と同じ値を入力してください。");
				}
			}
		}
		if(!tenpuFileRenkeiKyoten.isEmpty()) {
			if(!tenpuFileRenkeiKyoten.equals(tenpuFileRenkei)) {
				errorList.add("OPEN21連携（拠点入力）の添付ファイル連携にはOPEN21連携の添付ファイル連携と同じ値を入力してください。");
			}
		}
		if(!shiwakeSakuseiErrorActionKyoten.isEmpty()) {
			if(!shiwakeSakuseiErrorActionKyoten.equals(shiwakeSakuseiErrorAction)) {
				errorList.add("OPEN21連携（拠点入力）の仕訳作成エラー発生時の進め方にはOPEN21連携の仕訳作成エラー発生時の進め方と同じ値を入力してください。");
			}
		}


		// 『経費精算』『振替伝票』『現預金出納帳』伝票番号の重複チェック
		if(StringUtils.isNotEmpty(bumonFurikaeDenpyouNoStart) && StringUtils.isNotEmpty(bumonFurikaeDenpyouNoEnd) && StringUtils.isNotEmpty(suitouchouDenpyouNoStart) && StringUtils.isNotEmpty(suitouchouDenpyouNoEnd)) {
			// 『経費精算』の伝票番号を基準に（経費精算の伝票番号が未入力ならチェック不要）
			if(isNotEmpty(denpyouNoStart) && isNotEmpty(denpyouNoEnd)) {
				if(toInteger(denpyouNoStart) <= toInteger(bumonFurikaeDenpyouNoStart) && toInteger(bumonFurikaeDenpyouNoStart) <= toInteger(denpyouNoEnd)
					|| toInteger(denpyouNoStart) <= toInteger(bumonFurikaeDenpyouNoEnd) && toInteger(bumonFurikaeDenpyouNoEnd) <= toInteger(denpyouNoEnd)) {
					errorList.add("OPEN21連携の開始番号(伝票番号)及び終了番号(伝票番号)と、OPEN21連携（拠点入力）の振替伝票開始番号(伝票番号)及び振替伝票終了番号(伝票番号)の範囲が重複しています。");
				}
				if(toInteger(denpyouNoStart) <= toInteger(suitouchouDenpyouNoStart) && toInteger(suitouchouDenpyouNoStart) <= toInteger(denpyouNoEnd)
					|| toInteger(denpyouNoStart) <= toInteger(suitouchouDenpyouNoEnd) && toInteger(suitouchouDenpyouNoEnd) <= toInteger(denpyouNoEnd)) {
					errorList.add("OPEN21連携の開始番号(伝票番号)及び終了番号(伝票番号)と、OPEN21連携（拠点入力）の現預金出納帳開始番号(伝票番号)及び現預金出納帳終了番号(伝票番号)の範囲が重複しています。");
				}
			}
			// 『振替伝票』『現預金出納帳』伝票番号でもチェック
			if(toInteger(suitouchouDenpyouNoStart) <= toInteger(bumonFurikaeDenpyouNoStart) && toInteger(bumonFurikaeDenpyouNoStart) <= toInteger(suitouchouDenpyouNoEnd)
				|| toInteger(suitouchouDenpyouNoStart) <= toInteger(bumonFurikaeDenpyouNoEnd) && toInteger(bumonFurikaeDenpyouNoEnd) <= toInteger(suitouchouDenpyouNoEnd)
				|| toInteger(bumonFurikaeDenpyouNoStart) <= toInteger(suitouchouDenpyouNoStart) && toInteger(suitouchouDenpyouNoStart) <= toInteger(bumonFurikaeDenpyouNoEnd)
				|| toInteger(bumonFurikaeDenpyouNoStart) <= toInteger(suitouchouDenpyouNoEnd) && toInteger(suitouchouDenpyouNoEnd) <= toInteger(bumonFurikaeDenpyouNoEnd)) {
				errorList.add("OPEN21連携（拠点入力）の振替伝票開始番号(伝票番号)及び振替伝票終了番号(伝票番号)と現預金出納帳開始番号(伝票番号)及び現預金出納帳終了番号(伝票番号)の範囲が重複しています。");
			}
		}

		// 開始番号(伝票番号) ≧ 終了番号(伝票番号) の場合はエラー
		if(errorList.isEmpty() && Integer.parseInt(bumonFurikaeDenpyouNoStart) >= Integer.parseInt(bumonFurikaeDenpyouNoEnd)) errorList.add("振替伝票終了番号(伝票番号)は振替伝票開始番号(伝票番号)より後を指定してください。");
		if(errorList.isEmpty() && Integer.parseInt(suitouchouDenpyouNoStart) >= Integer.parseInt(suitouchouDenpyouNoEnd)) errorList.add("現預金出納帳終了番号(伝票番号)は現預金出納帳開始番号(伝票番号)より後を指定してください。");
		
		// カスタマイズ用追加チェックはここで行う
		if(this.customStrategy != null)
		{
			this.customStrategy.errorList = this.errorList;
			this.customStrategy.checkAddtionalSettingInfo();
			
		}
	}

	/**
	 * 購入ライセンス数の取得
	 * 取得できなかった場合エラー画面に遷移し、メッセージを表示する。
	 * @return 購入ライセンス数
	 */
	protected static long readUserLicense() {
		if(Env.userLicenseIsDummy()) return Env.dummyUserLicense();

		// ロジックの意味は「変更_00023別紙(利用ユーザー数制限について).xlsx」を見てください。
		try{
			//本物
			String r = RegAccess.readRegistory(REG_KEY_NAME.USER_LICENSE_NUM[0], REG_KEY_NAME.USER_LICENSE_NUM[1]);
			String r2 = Env.isLinux() ? "" : RegAccess.readRegistory(REG_KEY_NAME.INSTALL_DATE[0], REG_KEY_NAME.INSTALL_DATE[1]);
			String r3 = Env.isLinux() ? "" : RegAccess.readRegistory(REG_KEY_NAME.BIOS_RELEASE_DATE[0], REG_KEY_NAME.BIOS_RELEASE_DATE[1]);
			//UT用
			//1名
// String r = "543895755940030258390303010100001010013850";
// String r2 = "0x0078c2f1";//07914225
// String r3 = "01/01/1980";
			//750名
// String r = "4308721660359937480483281231235912322109851";
// String r2 = "0x53eb10f1";//1407914225
// String r3 = "02/29/2012";
			//999999名
// String r = "65245870689003694776913341231235925312346953";
// String r2 = "0xffffffff";//4294967295
// String r3 = "12/31/3000";
			long r2Val = Env.isLinux() ? 0 : Long.parseLong(StringUtils.right(Long.toString(Long.decode(r2)), 8));
			long r3Val = Env.isLinux() ? 0 : Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new SimpleDateFormat("MM/dd/yyyy").parse(r3)));

			int g = Integer.parseInt(r.substring(r.length()-2));
			int f = Integer.parseInt(r.substring(r.length()-3, r.length()-3+1));
			long a = Long.parseLong(r.substring(0,f));

			String b2e = r.substring(f, r.length()-3);

			if(r.length() != (g - f)){
				throw new RuntimeException();
			}

			for(int k = 1; k <= b2e.length() - 23; k++) {
				long b = Long.parseLong(StringUtils.left(b2e, k));
				long c = Long.parseLong(StringUtils.mid(b2e, k, 8));
				String d = StringUtils.mid(b2e, k + 8, 8);
				long e = Long.parseLong(b2e.substring(k + 16));

				if (! isMMDDHHMM(d))
				{
					continue;
				}
				long dVal = Long.parseLong(d);

				long lisenceB = dVal + c - b;

				if ((e - dVal) % 13 != 0)
				{
					continue;
				}
				long lisenceE = (e - dVal) / 13;

				if (lisenceB != lisenceE)
				{
					continue;
				}
				
				//Linuxの場合はユーザー数二重チェックは行わない
				if (Env.isLinux())
				{
					return lisenceB;
				}

// 2023/12/08 Ver23.05.31.28 2重チェックまでは不要 *- 				
//				if (r2Val * 7 - dVal != a) throw new RuntimeException();
//
//				if (r3Val * 3 - dVal != c )  throw new RuntimeException();
// -*
				
				return lisenceB;
			}
			throw new RuntimeException();
		}catch (Exception err) {
			log.error("ユーザーライセンス読取エラー", err);
			throw new RuntimeException("インストール情報に問題があります。弊社担当者までお問合せをお願い致します。");
		}
	}


	/**
	 * テナント最大ユーザー数のチェック
	 * @param connection コネクション
	 * @param lisenceNum 購入ライセンス数
	 */
	protected void tenantNumCheck(EteamConnection connection,long lisenceNum){
		
		initParts(connection);

		int settingInfoValue = 0;
		int otherTenantsUserLimit = 0;

		//テナント最大ユーザー数の入力値を取得→見つからなければ該当項目の更新権限なし(adminじゃない)なので飛ばす
		for (int i = 0; i < settingName.length; i++) {
			if(EteamSettingInfo.Key.TENANT_MAX_USERS_NUM.equals(settingName[i])){
				settingInfoValue = Integer.parseInt(settingVal[i]);
			}
		}
		if (0 == settingInfoValue)
		{
			return;
		}

		//アクセスしているテナントID(スキーマ名)以外のテナント最大ユーザー数の総和を取得
		List<String> shemaList= postgreSqlLogic.loadAllSchemaName();
		for(int i = 0; i < shemaList.size(); i++) {
			if(!EteamCommon.getContextSchemaName().equals(shemaList.get(i)))
			otherTenantsUserLimit =  otherTenantsUserLimit + systemLogic.findSettingVal(EteamSettingInfo.Key.TENANT_MAX_USERS_NUM, shemaList.get(i));
		}

		//購入ライセンス数 < テナント最大ユーザー数（全テナント総和）ならエラー
		if(lisenceNum < otherTenantsUserLimit + settingInfoValue){
			errorList.add("テナント最大ユーザー数の全テナント総和が購入ライセンス数を超えています。");
			return;
		}

		//テナント最大ユーザー数(入力) < 登録済ユーザー数(DB)ならエラー
		long tourokuzumiUserCount = bumonUserLogic.findUserCount();
		if(settingInfoValue < tourokuzumiUserCount) {
			errorList.add("テナント最大ユーザー数が登録済ユーザー数を下回っています。");
			return;
		}
	}


	//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);

			// プルダウン等制御情報取得
			displaySeiyo(connection);

			//1.入力チェック なし
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理（初期表示）

			// 機能制御テーブルよりデータを取得→チェックボックスの表示
			List<GMap> kinouSeigyoList = systemLogic.loadKinouSeigyo();
			for (GMap map : kinouSeigyoList) {
				switch((String)map.get("kinou_seigyo_cd")) {
					case KINOU_SEIGYO_CD.DAIKOU:								daikou = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.USER_DAIKOU_SHITEI:					userDaikouShitei = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.KANRI_DAIKOU_SHITEI:					kanriDaikouShitei = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.GUIDANCE_KIHYOU:						guidanceKihyou = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.OKINIIRIKIHYOU:						okiniiriKihyou = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.MAIL_HAISHIN:						mailHaishin = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.CSV_DOWNLOAD:						csvFurikomiDownload = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.BATCH_RENKEIKEKKA_KAKUNIN:			batchRenkeiKekkaKakunin = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.FBDATA_SAKUSEI:						FBDataSakusei = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.HOUJIN_CARD:							houjinCard = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.KAISHA_TEHAI:						kaishaTehai = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.GAIKA:								gaikaNyuuryoku = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.IC_CARD:								icCard = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.PREVIEW_TENPU_FILE: previewTenpuFile = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.DENPYOU_SAKUSEI:						denpyouSakusei = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_KIHYOU:						kihyou = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_SHINSEI:					shinsei = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_TORISAGE:					torisage = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_TORIMODOSHI:				torimodoshi = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_SASHIMODOSHI:				sashimodoshi = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_SHOUNIN:					shounin = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WORKFLOW_HININ:						hinin = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHINSEISHA:		shouninRouteTourokuShinseisha = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHOUNINSHA:		shouninRouteTourokuShouninsha = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.SHOUNINGO_TORIMODOSHI:				shouningoTorimodoshi = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.JOUI_SENKETSU_SHOUNIN:				jouiSenketsuShounin = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.DAIKOUSHA_JIKO_SHOUNIN: daikoushaJikoShounin = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.FILE_TENPU:							fileTenpu = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.WEB_INSATSU_BUTTON:					webInsatsuButton = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU:ryoushuushoSeikyuushoTouChkHissu = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.SOUSA_RIREKI:						sousaRireki = map.get("kinou_seigyo_kbn"); break;
					case KINOU_SEIGYO_CD.CHUUKI_PREVIEW:						chuukiPreview = map.get("kinou_seigyo_kbn"); break;
				}
			}
			// メール設定テーブルよりデータを取得（メール配信チェックがONの時）
			if(EteamNaibuCodeSetting.KINOU_SEIGYO_KBN.YUUKOU.equals(mailHaishin)) {
				GMap mailSetteiMap = systemLogic.findMailSettei();
				mailServer  = mailSetteiMap.get("smtp_server_name").toString(); // SMTPサーバー名
				mailPort    = mailSetteiMap.get("port_no").toString(); // ポート番号
				mailNinshou = mailSetteiMap.get("ninshou_houhou").toString(); // 認証方法
				mailAngouka = mailSetteiMap.get("angouka_houhou").toString(); // 暗号化方法
				mailAddress = mailSetteiMap.get("mail_address").toString(); // メールアドレス
				mailUser    = mailSetteiMap.get("mail_id").toString(); // ユーザー名
				// パスワードについては、表示しない
			}

			//入出力項目は入出力項目なのでListから配列につめないと駄目
			settingName = new String[settingList.size()];
			settingVal = new String[settingList.size()];
			int i = 0;
			for (GMap record : settingList) {
				settingName[i] = (String)record.get("setting_name");
				settingVal[i] = (String)record.get("setting_val");
				i++;
			}
			
			//5.戻り値を返す
			return "success";

		}
	}

	/**
	 * 更新イベント
	 * 更新ボタン押下時
	 * システム管理画面で設定した内容を更新する。
	 * @return 処理結果
	 */
	public String koushin() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);

			// プルダウン等制御情報取得
			displaySeiyo(connection);

			//1.入力チェック
			formatCheck();
			hissuCheck(2);
			settingCheck(connection);
			if(0 < errorList.size()) {
				return "error";
			}
			tenantNumCheck(connection,readUserLicense());
			if(0 < errorList.size()) {
				return "error";
			}

			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理

			//入力内容を元にテーブルへ更新処理を行う
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.DAIKOU, n2zero(daikou));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.USER_DAIKOU_SHITEI, n2zero(userDaikouShitei));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.KANRI_DAIKOU_SHITEI, n2zero(kanriDaikouShitei));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.GUIDANCE_KIHYOU, n2zero(guidanceKihyou));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.OKINIIRIKIHYOU, n2zero(okiniiriKihyou));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.MAIL_HAISHIN, n2zero(mailHaishin));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.CSV_DOWNLOAD, n2zero(csvFurikomiDownload));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.BATCH_RENKEIKEKKA_KAKUNIN, n2zero(batchRenkeiKekkaKakunin));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.FBDATA_SAKUSEI, n2zero(FBDataSakusei));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.HOUJIN_CARD, n2zero(houjinCard));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.KAISHA_TEHAI, n2zero(kaishaTehai));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.GAIKA, n2zero(gaikaNyuuryoku));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.IC_CARD, n2zero(icCard));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.PREVIEW_TENPU_FILE, n2zero(previewTenpuFile));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.DENPYOU_SAKUSEI, n2zero(denpyouSakusei));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_KIHYOU, n2zero(kihyou));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_SHINSEI, n2zero(shinsei));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_TORISAGE, n2zero(torisage));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_TORIMODOSHI, n2zero(torimodoshi));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_SASHIMODOSHI, n2zero(sashimodoshi));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_SHOUNIN, n2zero(shounin));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WORKFLOW_HININ, n2zero(hinin));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHINSEISHA, n2zero(shouninRouteTourokuShinseisha));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHOUNINSHA, n2zero(shouninRouteTourokuShouninsha));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU, (systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHINSEISHA)||systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHOUNINSHA))?"1":"0");
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.SHOUNINGO_TORIMODOSHI, n2zero(shouningoTorimodoshi));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.JOUI_SENKETSU_SHOUNIN, n2zero(jouiSenketsuShounin));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.DAIKOUSHA_JIKO_SHOUNIN, n2zero(daikoushaJikoShounin));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.FILE_TENPU, n2zero(fileTenpu));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.WEB_INSATSU_BUTTON, n2zero(webInsatsuButton));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU, n2zero(ryoushuushoSeikyuushoTouChkHissu));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.SOUSA_RIREKI, n2zero(sousaRireki));
			myLogic.updateKinouSeigyo(KINOU_SEIGYO_CD.CHUUKI_PREVIEW, n2zero(chuukiPreview));

			// メール配信チェックボックスにチェックが入っている場合、入力内容で更新処理を行う。
			if(EteamNaibuCodeSetting.KINOU_SEIGYO_KBN.YUUKOU.equals(mailHaishin)) {
				if("".equals(mailPassword)) {
					// メールパスワードが未入力の場合：パスワードは更新しない
					myLogic.updateMailSettei(mailServer, mailPort, mailNinshou, mailAngouka, mailAddress, mailUser);
				} else {
					// メールパスワードが入力された場合：パスワードを更新する
					myLogic.updateMailSettei(mailServer, mailPort, mailNinshou, mailAngouka, mailAddress, mailUser, mailPassword);
				}
			// メール配信チェックボックスにチェックが入っていない場合、認証方法と暗号化方法はデフォルト値、その他の項目を""（空文字）で更新処理を行う。
			} else {
				myLogic.updateMailSettei("", "", mailNinshou, mailAngouka, "", "","");
			}

			// 設定情報毎に更新
			String idRenkei = "";
			String shainCdRenkei = "";
			String denpyouNoStart = "";
			String denpyouNoEnd = "";
			String bumonFurikaeDenpyouNoStart = "";
			String bumonFurikaeDenpyouNoEnd = "";
			String suitouchouDenpyouNoStart = "";
			String suitouchouDenpyouNoEnd = "";
			String[] ufMapping = new String[UF_FOR_DISPLAY.length];
			for(int i = 0 ; i < UF_FOR_DISPLAY.length ; i++){ ufMapping[i] = ""; }
			String[] ufKoteiMapping = new String[UF_KOTEI_FOR_DISPLAY.length];
			for(int i = 0 ; i < UF_KOTEI_FOR_DISPLAY.length ; i++){ ufKoteiMapping[i] = ""; }

			for (int i = 0; i < settingName.length; i++) {
				myLogic.updateSettingInfo(settingName[i], settingVal[i]);
				if(settingName[i].equals(EteamSettingInfo.Key.UF1_DENPYOU_ID_HANEI) ) { idRenkei = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.SHAIN_CD_RENKEI) ) { shainCdRenkei = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.DENPYOU_SERIAL_NO_START) ) { denpyouNoStart = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.DENPYOU_SERIAL_NO_END) ) { denpyouNoEnd = settingVal[i]; }
				if(settingName[i].equals(Key.BUMON_FURIKAE_DENPYOU_SERIAL_NO_START)) bumonFurikaeDenpyouNoStart = settingVal[i];
				if(settingName[i].equals(Key.BUMON_FURIKAE_DENPYOU_SERIAL_NO_END)) bumonFurikaeDenpyouNoEnd = settingVal[i];
				if(settingName[i].equals(Key.SUITOUCHOU_DENPYOU_SERIAL_NO_START)) suitouchouDenpyouNoStart = settingVal[i];
				if(settingName[i].equals(Key.SUITOUCHOU_DENPYOU_SERIAL_NO_END)) suitouchouDenpyouNoEnd = settingVal[i];
				if(settingName[i].equals(EteamSettingInfo.Key.UF1_MAPPING) ) { ufMapping[0] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF2_MAPPING) ) { ufMapping[1] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF3_MAPPING) ) { ufMapping[2] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF4_MAPPING) ) { ufMapping[3] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF5_MAPPING) ) { ufMapping[4] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF6_MAPPING) ) { ufMapping[5] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF7_MAPPING) ) { ufMapping[6] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF8_MAPPING) ) { ufMapping[7] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF9_MAPPING) ) { ufMapping[8] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF10_MAPPING) ) { ufMapping[9] = settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI1_MAPPING) ) { ufKoteiMapping[0]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI2_MAPPING) ) { ufKoteiMapping[1]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI3_MAPPING) ) { ufKoteiMapping[2]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI4_MAPPING) ) { ufKoteiMapping[3]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI5_MAPPING) ) { ufKoteiMapping[4]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI6_MAPPING) ) { ufKoteiMapping[5]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI7_MAPPING) ) { ufKoteiMapping[6]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI8_MAPPING) ) { ufKoteiMapping[7]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI9_MAPPING) ) { ufKoteiMapping[8]= settingVal[i]; }
				if(settingName[i].equals(EteamSettingInfo.Key.UF_KOTEI10_MAPPING) ) { ufKoteiMapping[9]= settingVal[i]; }
			}

			//レジストリでe文書利用OFFの場合e文書関連設定値を"0"に設定
			if(! ebunshoFlg.equals("1")){
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_ENABLE_FLG,  "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A001, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A002, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A003, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A004, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A005, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A006, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A009, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A010, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A011, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A012, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_A013, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SEISEI_B000, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A001, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A002, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A003, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A004, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A005, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A006, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A009, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A010, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A011, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A012, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_A013, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_SHUBETSU_B000, "0");
				myLogic.updateSettingInfo(EteamSettingInfo.Key.EBUNSHO_COMPRESS_FLG, "0");
			}

			// 社員コード連携フラグが「0」(連携しない)だった場合、仕訳パターンマスターの社員コード連携フラグを「1」→「0」(連携しない)にする。
			if(isNotEmpty(shainCdRenkei) && "0".equals(shainCdRenkei)) {
				myLogic.updateshiwakePForshainRenkei("0", "1", getUser().getTourokuOrKoushinUserId());
			}

			// 伝票番号採番テーブルの更新
			if(isNotEmpty(denpyouNoStart) && isNotEmpty(denpyouNoEnd)) {
				myLogic.updateDenpyouSerialNoSaiban(Integer.parseInt(denpyouNoStart), Integer.parseInt(denpyouNoEnd));
			}

			if (RegAccess.checkEnableZaimuKyotenOption()) {
				myLogic.updateDenpyouSerialNoSaibanKyoten(DENPYOU_KBN_KYOTEN.BUMON_FURIKAE_DENPYOU, Integer.parseInt(bumonFurikaeDenpyouNoStart), Integer.parseInt(bumonFurikaeDenpyouNoEnd));
				myLogic.updateDenpyouSerialNoSaibanKyoten(DENPYOU_KBN_KYOTEN.GENYOKIN_SUITOUCHOU, Integer.parseInt(suitouchouDenpyouNoStart), Integer.parseInt(suitouchouDenpyouNoEnd));
			}

			// UFマッピング消したなら取引の該当UFをクリア(SIASのみ de3では設定値とれないはず)
			for (int i = 0; i < UF_FOR_DISPLAY.length; i++) {
				if(ufMapping[i].equals("0")) {
					shiwakePatternMasterDao.clearUf(i+1, null, getUser().getTourokuOrKoushinUserId());
					if (RegAccess.checkEnableZaimuKyotenOption()) shiwakePatternMasterZaimuKyotenDao.clearUf(i+1, null, getUser().getTourokuOrKoushinUserId());
				}
			}
			for (int i = 0; i < UF_KOTEI_FOR_DISPLAY.length; i++) {
				if(ufKoteiMapping[i].equals("0")) {
					shiwakePatternMasterDao.clearUfKotei(i+1, null, getUser().getTourokuOrKoushinUserId());
					if (RegAccess.checkEnableZaimuKyotenOption()) shiwakePatternMasterZaimuKyotenDao.clearUfKotei(i+1, null, getUser().getTourokuOrKoushinUserId());
				}
			}
			if(Open21Env.getVersion() == Version.DE3){
				if(idRenkei.startsWith("UF")) {
					shiwakePatternMasterDao.clearUf(Integer.parseInt(idRenkei .substring(2)), null, getUser().getTourokuOrKoushinUserId());
					if (RegAccess.checkEnableZaimuKyotenOption()) shiwakePatternMasterZaimuKyotenDao.clearUf(Integer.parseInt(idRenkei .substring(2)), null, getUser().getTourokuOrKoushinUserId());
				}
				if(shainCdRenkei.startsWith("UF")) {
					shiwakePatternMasterDao.clearUf(Integer.parseInt(shainCdRenkei .substring(2)), null, getUser().getTourokuOrKoushinUserId());
					if (RegAccess.checkEnableZaimuKyotenOption()) shiwakePatternMasterZaimuKyotenDao.clearUf(Integer.parseInt(shainCdRenkei .substring(2)), null, getUser().getTourokuOrKoushinUserId());
				}
			}else{
				//伝票ID連携先とUF①②③マッピング先のバッティングはない
				for (int i = 0; i < UF_FOR_DISPLAY.length; i++) {
					if(shainCdRenkei.startsWith("UF") && shainCdRenkei.substring(2).equals(ufMapping[i])) {
						shiwakePatternMasterDao.clearUf(i+1, null, getUser().getTourokuOrKoushinUserId());
					}
				}
				for (int i = 0; i < UF_KOTEI_FOR_DISPLAY.length; i++) {
					if(shainCdRenkei.startsWith("UF") && shainCdRenkei.substring(2).equals(ufKoteiMapping[i])) {
						shiwakePatternMasterDao.clearUfKotei(i+1, null, getUser().getTourokuOrKoushinUserId());
						if (RegAccess.checkEnableZaimuKyotenOption()) shiwakePatternMasterZaimuKyotenDao.clearUfKotei(i+1, null, getUser().getTourokuOrKoushinUserId());
					}
				}
			}

			// ファイル添付がOFFの時、拠点入力の仕訳パターンマスターの添付ファイル必須フラグを一律「0」に変更する。
			if(!"1".equals(fileTenpu) && RegAccess.checkEnableZaimuKyotenOption()) {
				List<ShiwakePatternMasterZaimuKyoten> list = shiwakePatternMasterZaimuKyotenDao.loadByDenpyouKbn(DENPYOU_KBN_KYOTEN.GENYOKIN_SUITOUCHOU);
				for(ShiwakePatternMasterZaimuKyoten patternMaster : list) {
					if("1".equals(patternMaster.tenpuFileHissuFlg)) {
						patternMaster.setTenpuFileHissuFlg("0");
						shiwakePatternMasterZaimuKyotenDao.update(patternMaster, getUser().getTourokuOrKoushinUserId());
					}
				}
			}

			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * プルダウン等再取得
	 * @param connection コネクション
	 */
	protected void displaySeiyo(EteamConnection connection) {

		initParts(connection);
		/** 支払依頼有無(オプション) */
		shiharaiIraiOn = denpyouShubetsuIchiran.find(DENPYOU_KBN.SIHARAIIRAI) != null;
		/**	請求書払い有無（オプション） */
		seikyuushoBaraiOn = denpyouShubetsuIchiran.find(DENPYOU_KBN.SEIKYUUSHO_BARAI) != null;
		/**	届出ジェネレータ有無（オプション） */
		kaniTodokeOn = systemLogic.findGamenKengenSeigyoInfoFlg("KaniTodoke").get("system_kanri_riyoukanou_flg").equals("1");

		ninshouList = systemLogic.loadNaibuCdSetting("ninshou_houhou");
		angoukaList = systemLogic.loadNaibuCdSetting("angouka_houhou");

		//設定情報取得
		settingList = systemLogic.loadSettingVal2Edit(GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId()));

		//e文書使用制限オプションが無効の場合、e文書関連の設定タブを削除
		ebunshoFlg = RegAccess.checkEnableEbunshoOption() ? "1" : "0";
		if(!("1".equals(ebunshoFlg))){
			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				GMap data = itr.next();
				String category = (String)data.get("category");
				if (category.contains(CATEGORY_NAME_EBUNSHO)) {
					itr.remove();
				}
			}
		}

		//予算執行オプションが無効の場合、予算執行の設定タブを削除
		yosanAFlg = RegAccess.checkEnableYosanShikkouOption().equals(RegAccess.YOSAN_SHIKKOU_OP.A_OPTION)? "1" : "0";
		if(!("1".equals(yosanAFlg))){
			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				GMap data = itr.next();
				String category = (String)data.get("category");
				if (category.equals(CATEGORY_NAME_YOSAN_A)) {
					itr.remove();
				}
			}
		}

		//支払依頼申請オプションが無効の場合、支払依頼申請の設定タブ及び支払依頼申請に関わる設定項目を削除
		if(!shiharaiIraiOn){
			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				Map<String, Object> data = itr.next();
				String category = (String)data.get("category");
				String sname = (String)data.get("setting_name");
				if (category.equals(CATEGORY_NAME_SHIHARAI_IRAI) ||sname.contains("A013") || sname.contains("shiharaiirai")) {
					itr.remove();
				}
			}
		}
		
		//請求書払い申請オプションが無効の場合、請求書払い申請に関わる設定項目を削除
		if(!seikyuushoBaraiOn) {
			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				Map<String, Object> data = itr.next();
				String sname = (String)data.get("setting_name");
				if (sname.contains("A003") || sname.contains("seikyuu_") || sname.contains("_shiharai_shiwake_sakusei")) {
					itr.remove();
				}
			}
		}
		
		//届出ジェネレータオプションが無効の場合、届出ジェネレータに関わる設定項目を削除
		if(!kaniTodokeOn) {
			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				Map<String, Object> data = itr.next();
				String sname = (String)data.get("setting_name");
				if (sname.contains("B000")) {
					itr.remove();
				}
			}
		}

		//経費精算(WF本体)が無効の場合、以下項目の設定タブを削除
		if(!(RegAccess.checkEnableKeihiSeisan())){
			List<String> jyogaiList = new ArrayList<String>();
			jyogaiList.add("駅すぱあと連携");
			jyogaiList.add("法人カード");
			jyogaiList.add("ICカード");
			jyogaiList.add("FB 連携");
			jyogaiList.add("伝票作成単位");
			jyogaiList.add("仕訳摘要内容");
			jyogaiList.add("支払方法");
			jyogaiList.add("計上日・支払");
			jyogaiList.add("振込日ルール適用");
			jyogaiList.add("添付ファイル");
			jyogaiList.add("添付伝票");
			jyogaiList.add("出張");
			jyogaiList.add("経費伺い（仮払）運用方法");
			jyogaiList.add("出張伺い（仮払）運用方法");
			jyogaiList.add("海外出張伺い（仮払）運用方法");

			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				GMap data = itr.next();
				String category = (String)data.get("category");
				if (jyogaiList.contains(category)) {
					itr.remove();
				}
			}
		}

		//財務拠点入力オプションが無効の場合、OPEN21連携（拠点入力）の設定タブを削除
		if(!(RegAccess.checkEnableZaimuKyotenOption())){
			Iterator<GMap> itr = settingList.iterator();
			while(itr.hasNext()){
				GMap data = itr.next();
				String category = (String)data.get("category");
				if (category.contains(CATEGORY_NAME_KYOTEN)) {
					itr.remove();
				}
			}
		}

		//差引単価（海外）レートの表示内容を取得
		for (GMap record : settingList) {
			if(record.get("setting_name").equals(Key.SASHIHIKI_TANKA_KAIGAI_GAIKA_HEISHU)) {
				GMap heishuMaster = masterLogic.findHeishuCd(record.get("setting_val"));
				if(null != heishuMaster) sashihikiTankaKaigaiGaikaRate = formatDecimalNoComma(heishuMaster.get("rate"), "rate");
				break;
			}
		}

		//表示加工処理.
		//１．上からなめていって、１つ上と同じカテゴリならカテゴリをブランクにする。
		String prevCategory = null;
		for (GMap record : settingList) {
			String curCategory = (String)record.get("category");
			if (curCategory.equals(prevCategory)) {
				record.put("category", "");
			}
			prevCategory = curCategory;
		}
		//２．下からなめていって、カテゴリが残っているレコード（カテゴリ単位の先頭行）にカテゴリ数をセット
		int categoryCount = 0;
		for (int i = settingList.size() - 1; 0 <= i; i--) {
			GMap record = settingList.get(i);
			categoryCount++;
			if (! "".equals(record.get("category"))) {
				record.put("categoryCount", categoryCount);
				categoryCount = 0;
			}
		}
		categoryCount++;

		//設定項目単位の正規表現フォーマット
		settingRegexMap = new HashMap<String, String[]>();
		for (GMap record : settingList) {
			settingRegexMap.put((String)record.get("setting_name"), new String[]{
					(String)record.get("setting_name_wa"),
					(String)record.get("hissu_flg"),
					(String)record.get("format_regex"),});
		}

		//DB Version
		dbVersion = systemLogic.findVersionYYMMDDXX();
	}
	
	//＜内部処理＞
	/**
	 * 初期化処理
	 * 
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		kcLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		postgreSqlLogic = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		denpyouShubetsuIchiran = EteamContainer.getComponent(DenpyouShubetsuIchiranAbstractDao.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		shiwakePatternMasterZaimuKyotenDao = EteamContainer.getComponent(ShiwakePatternMasterZaimuKyotenDao.class, connection);
		myLogic = EteamContainer.getComponent(SystemKanriLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
	}

	/**
	 * 正しいMMDDHHMM形式かチェックする
	 * @param s チェック対象の文字列
	 * @return 正しい形式かどうか
	 */
	protected static boolean isMMDDHHMM(String s) {
		try{
			//長さ足りなければNG
			if (StringUtils.isEmpty(s))
			{
				return false;
			}
			if (8 != s.length())
			{
				return false;
			}

			//0229(閏年)は存在しない日になってしまうので0228にする
			if (s .substring(0,4).equals("0229")) s = "0228" + s .substring(4,8);

			//存在する日時でなければNG
			SimpleDateFormat format = new SimpleDateFormat("MMddHHmm");
			format.setLenient(false);
			format.parse(s);
			return true;
		}catch(ParseException e){
			return false;
		}
	}

	/**
	 * e文書種別の形式が0-5のカンマ区切りかチェックする
	 * ※正規表現で上手くいかず個別実装
	 * @param commaSeparatedString チェック対象の文字列
	 * @param isKeihi e文書（拠点入力）タブの伝票種別であるか
	 * @return 正しい形式かどうか
	 */
	protected boolean isOKEbunshoShubetsu(String commaSeparatedString, boolean isKeihi)
	{
        List<String> list = Arrays.asList(commaSeparatedString.split(","));

        if(list.size() == 0)
        {
			// カンマのみ
			return false;
        }
        else if (list.size() != list.stream().distinct().count())
        {
        	// 値重複
			return false;
        }

		for (String str :list)
		{
			if(isKeihi) {
				if (!str.matches("[0-5]"))
				{
						return false;
				}
			}else {
				if (!str.matches("[0-7]"))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 入力値がnullの場合に0を返す
	 * @param s 入力値
	 * @return 入力値または0
	 */
	protected String n2zero(String s) {
		return s == null ? "0" : s;
	}
}
