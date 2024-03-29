package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateFormatUtils;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamNaibuCodeSetting.TESUURYOU;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KeijoubiShimebiAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiharaiIraiAbstractDao;
import eteam.database.abstractdao.ShiharaiIraiMeisaiAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KeijoubiShimebiDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiharaiIraiDao;
import eteam.database.dao.ShiharaiIraiMeisaiDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.KamokuEdabanZandaka;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiharaiIrai;
import eteam.database.dto.ShiharaiIraiMeisai;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 請求書払いAction
 */
@Getter @Setter @ToString
public class ShiharaiIraiAction extends WorkflowEventControl {

//＜定数＞

//＜画面入力＞
	//画面入力（申請内容）
	/** 取引先コード */
	String torihikisakiCd;
	/** 取引先名 */
	String torihikisakiNameRyakushiki;
	/** (一見先の場合のみ表示)支払先名 */
	String ichigensakiTorihikisakiName;
	/** 支払方法 */
	String shiharaiHouhou;
	/** 支払種別 */
	String shiharaiShubetsu;
	/** 計上日 */
	String keijoubi;
	/** 支払予定日 */
	String yoteibi;
	/** 支払日 */
	String shiharaibi;
	/** 支払期日 */
	String shiharaiKijitsu;

	/** EDI */
	String edi;
	
	/** 振込銀行コード */
	String furikomiGinkouCd;
	/** 振込銀行名 */
	String furikomiGinkouName;
	/** 振込支店コード */
	String furikomiGinkouShitenCd;
	/** 振込支店名 */
	String furikomiGinkouShitenName;
	/** 口座種別 */
	String kouzaShubetsu;
	/** 口座番号 */
	String kouzaBangou;
	/** 口座名義人 */
	String kouzaMeiginin;
	/** 手数料 */
	String tesuuryou;
	
	/** 支払合計 */
	String shiharaiGoukei;
	/** 相殺合計 */
	String sousaiGoukei;
	// 差引支払額は親クラスで定義しているkingakuを使用する
	/** マネキン源泉 */
	String manekinGensen;
	/** 捕捉 */
	String hosoku;
	/** 領収書・請求書等 */
	String shouhyouShoruiFlg;
	
	/** ヘッダーフィールド１コード */
	String hf1Cd;
	/** ヘッダーフィールド１名 */
	String hf1Name;
	/** ヘッダーフィールド２コード */
	String hf2Cd;
	/** ヘッダーフィールド２名 */
	String hf2Name;
	/** ヘッダーフィールド３コード */
	String hf3Cd;
	/** ヘッダーフィールド３名 */
	String hf3Name;
	/** ヘッダーフィールド４コード */
	String hf4Cd;
	/** ヘッダーフィールド４名 */
	String hf4Name;
	/** ヘッダーフィールド５コード */
	String hf5Cd;
	/** ヘッダーフィールド５名 */
	String hf5Name;
	/** ヘッダーフィールド６コード */
	String hf6Cd;
	/** ヘッダーフィールド６名 */
	String hf6Name;
	/** ヘッダーフィールド７コード */
	String hf7Cd;
	/** ヘッダーフィールド７名 */
	String hf7Name;
	/** ヘッダーフィールド８コード */
	String hf8Cd;
	/** ヘッダーフィールド８名 */
	String hf8Name;
	/** ヘッダーフィールド９コード */
	String hf9Cd;
	/** ヘッダーフィールド９名 */
	String hf9Name;
	/** ヘッダーフィールド１０コード */
	String hf10Cd;
	/** ヘッダーフィールド１０名 */
	String hf10Name;
	/** 入力方式 */
	String nyuryokuHoushiki;
	/** 事業者区分 */
	String jigyoushaKbn;
	/** 事業者登録番号 */
	String jigyoushaNo;
	/** インボイス対応伝票 */
	//String invoiceDenpyou;
	
	//インボイス対応
	/** 消費税額修正ボタン押下可否 */
	String shouhizeigakuShuusei;
	
	//画面入力（明細）
	/** 仕訳枝番号 */
	String[] shiwakeEdaNo;
	/** 取引名 */
	String[] torihikiName;
	
	/** 得意先コード */
	String[] tokuisakiCd;
	/** 得意先名 */
	String[] tokuisakiName;
	
	/** 仮勘定消込No */
	String[] kariKanjouKeshikomiNo;
	
	/** 勘定科目コード */
	String[] kamokuCd;
	/** 勘定科目名 */
	String[] kamokuName;
	/** 勘定科目枝番コード */
	String[] kamokuEdabanCd;
	/** 勘定科目枝番名 */
	String[] kamokuEdabanName;
	/** 負担部門コード */
	String[] futanBumonCd;
	/** 負担部門名 */
	String[] futanBumonName;
	/** プロジェクトコード */
	String[] projectCd;
	/** プロジェクト名 */
	String[] projectName;
	/** セグメントコード */
	String[] segmentCd;
	/** セグメント名 */
	String[] segmentName;
	/** ユニバーサルフィールド１コード */
	String[] uf1Cd;
	/** ユニバーサルフィールド１名 */
	String[] uf1Name;
	/** ユニバーサルフィールド２コード */
	String[] uf2Cd;
	/** ユニバーサルフィールド２名 */
	String[] uf2Name;
	/** ユニバーサルフィールド３コード */
	String[] uf3Cd;
	/** ユニバーサルフィールド３名 */
	String[] uf3Name;
	/** ユニバーサルフィールド４コード */
	String[] uf4Cd;
	/** ユニバーサルフィールド４名 */
	String[] uf4Name;
	/** ユニバーサルフィールド５コード */
	String[] uf5Cd;
	/** ユニバーサルフィールド５名 */
	String[] uf5Name;
	/** ユニバーサルフィールド６コード */
	String[] uf6Cd;
	/** ユニバーサルフィールド６名 */
	String[] uf6Name;
	/** ユニバーサルフィールド７コード */
	String[] uf7Cd;
	/** ユニバーサルフィールド７名 */
	String[] uf7Name;
	/** ユニバーサルフィールド８コード */
	String[] uf8Cd;
	/** ユニバーサルフィールド８名 */
	String[] uf8Name;
	/** ユニバーサルフィールド９コード */
	String[] uf9Cd;
	/** ユニバーサルフィールド９名 */
	String[] uf9Name;
	/** ユニバーサルフィールド１０コード */
	String[] uf10Cd;
	/** ユニバーサルフィールド１０名 */
	String[] uf10Name;
	/** 課税区分 */
	String[] kazeiKbn;
	/** 消費税率 */
	String[] zeiritsu;
	/** 軽減税率区分 */
	String[] keigenZeiritsuKbn;
	/** 支払金額 */
	String[] shiharaiKingaku;
	/** 摘要 */
	String[] tekiyou;
	/** 分離区分 */
	String[] bunriKbn;
	/** 借方仕入区分 */
	String[] kariShiireKbn;
	/** 税抜金額 */
	String[] zeinukiKingaku;
	/** 消費税額 */
	String[] shouhizeigaku;

//＜画面入力以外＞
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 伝票摘要注記 */
	String[] chuuki2;

	//プルダウン等の候補値
	/** 消費税率DropDownList */
	List<Shouhizeiritsu> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<NaibuCdSetting> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
	/** 支払方法ドメイン */
	String[] shiharaiHouhouDomain = {"1"};
	/** 支払種別DropDownList */
	List<NaibuCdSetting> shiharaiShubetsuList;
	/** 支払種別ドメイン */
	String[] shiharaiShubetsuDomain;
	/** 預金種別DropDownList */
	List<NaibuCdSetting> yokinShubetsuList;
	/** 預金種別ドメイン */
	String[] yokinShubetsuDomain;
	/** CSVアップロードフラグ */
	boolean csvUploadFlag = false;
	/** 外部呼出しユーザーId */
	String gaibuKoushinUserId;
	/** 入力方式DropDownList */
	List<NaibuCdSetting> nyuuryokuHoushikiList;
	/** 入力方式ドメイン */
	String[] nyuuryokuHoushikiDomain;
	/** 事業者区分DropDownList */
	List<NaibuCdSetting> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;
	/** 分離区分DropDownList */
	List<NaibuCdSetting> bunriKbnList;
	/** 分離区分ドメイン */
	String[] bunriKbnDomain;
	/** 仕入区分DropDownList */
	List<NaibuCdSetting> shiireKbnList;
	/** 仕入区分ドメイン */
	String[] shiireKbnDomain;
	/** インボイス対応伝票DropDownList */
	List<NaibuCdSetting> invoiceDenpyouList;
	/** インボイス対応伝票ドメイン */
	String[] invoiceDenpyouDomain;
	/**	税込or税抜ならtrue */
	boolean[] kazeiKbnCheck;

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス(申請内容） */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SIHARAIIRAI);
	/** 画面項目制御クラス(明細） */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.SIHARAIIRAI);
	
	/** 入力モード */
	boolean enableInput;
	/** 支払日表示モード(0:非表示、1:入力可、2:表示)※0はなし */
	int shiharaiBiMode;
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** 債務使用フラグ */
	String saimuShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SAIMU_SHIYOU_FLG);
	/** 計上日 締日制限 */
	boolean keijoubiSeigen = setting.shiharaiiraiKeijouSeigen().equals("1");
	/** 逆仕訳可能 */
	boolean gyakuShiwakeEnabled = false;
	/** 一見取引先である */
	boolean ichigenFlg;
	/** マネキン文言 */
	String manekinName = setting.manekinName();
	/** マネキン源泉利用可能(取引先により入力可能) */
	boolean manekinFlg;
	/** 計上日の選択候補 */
	List<String> keijoubiList;

	//明細単位制御情報
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean[] kamokuEdabanEnable;
	/** 負担部門選択ボタン */
	boolean[] futanBumonEnable;
	/** プロジェクト選択ボタン */
	boolean[] projectEnable;
	/** セグメント選択ボタン */
	boolean[] segmentEnable;
	/** UF1ボタン選択ボタン */
	boolean[] uf1Enable;
	/** UF2ボタン選択ボタン */
	boolean[] uf2Enable;
	/** UF3ボタン選択ボタン */
	boolean[] uf3Enable;
	/** UF4ボタン選択ボタン */
	boolean[] uf4Enable;
	/** UF5ボタン選択ボタン */
	boolean[] uf5Enable;
	/** UF6ボタン選択ボタン */
	boolean[] uf6Enable;
	/** UF7ボタン選択ボタン */
	boolean[] uf7Enable;
	/** UF8ボタン選択ボタン */
	boolean[] uf8Enable;
	/** UF9ボタン選択ボタン */
	boolean[] uf9Enable;
	/** UF10ボタン選択ボタン */
	boolean[] uf10Enable;
	/** 消費税率入力可否 */
	boolean[] zeiritsuEnable;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean[] enableBumonSecurity;
	
	/** 消費税率入力可否 */
	String[] shoriGroup;

	//明細登録時の領域
	/** 借方取引先コード */
	String[] kariTorihikisakiCd;
// /** 借方　UF1コード */
// String[] kariUf1Cd;
// /** 借方　UF2コード */
// String[] kariUf2Cd;
// /** 借方　UF3コード */
// String[] kariUf3Cd;
// /** 借方　UF4コード */
// String[] kariUf4Cd;
// /** 借方　UF5コード */
// String[] kariUf5Cd;
// /** 借方　UF6コード */
// String[] kariUf6Cd;
// /** 借方　UF7コード */
// String[] kariUf7Cd;
// /** 借方　UF8コード */
// String[] kariUf8Cd;
// /** 借方　UF9コード */
// String[] kariUf9Cd;
// /** 借方　UF10コード */
// String[] kariUf10Cd;

	/** 貸方取引先コード */
	String[] kashiTorihikisakiCd;
	/** 貸方負担部門コード */
	String[] kashiFutanBumonCd;
	/** 貸方科目コード */
	String[] kashiKamokuCd;
	/** 貸方科目枝番コード */
	String[] kashiKamokuEdabanCd;
	/** 貸方課税区分 */
	String[] kashiKazeiKbn;
// /** 貸方1　UF1コード */
// String[] kashiUf1Cd1;
// /** 貸方1　UF2コード */
// String[] kashiUf2Cd1;
// /** 貸方1　UF3コード */
// String[] kashiUf3Cd1;
// /** 貸方1　UF4コード */
// String[] kashiUf4Cd1;
// /** 貸方1　UF5コード */
// String[] kashiUf5Cd1;
// /** 貸方1　UF6コード */
// String[] kashiUf6Cd1;
// /** 貸方1　UF7コード */
// String[] kashiUf7Cd1;
// /** 貸方1　UF8コード */
// String[] kashiUf8Cd1;
// /** 貸方1　UF9コード */
// String[] kashiUf9Cd1;
// /** 貸方1　UF10コード */
// String[] kashiUf10Cd1;
// /** 貸方2　UF1コード */
// String[] kashiUf1Cd2;
// /** 貸方2　UF2コード */
// String[] kashiUf2Cd2;
// /** 貸方2　UF3コード */
// String[] kashiUf3Cd2;
// /** 貸方2　UF4コード */
// String[] kashiUf4Cd2;
// /** 貸方2　UF5コード */
// String[] kashiUf5Cd2;
// /** 貸方2　UF6コード */
// String[] kashiUf6Cd2;
// /** 貸方2　UF7コード */
// String[] kashiUf7Cd2;
// /** 貸方2　UF8コード */
// String[] kashiUf8Cd2;
// /** 貸方2　UF9コード */
// String[] kashiUf9Cd2;
// /** 貸方2　UF10コード */
// String[] kashiUf10Cd2;

	/** 掛け貸方取引先コード */
	String[] kakeKashiTorihikisakiCd;
	/** 掛け貸方負担部門コード */
	String[] kakeKashiFutanBumonCd;
	/** 掛け貸方科目コード */
	String[] kakeKashiKamokuCd;
	/** 掛け貸方科目枝番コード */
	String[] kakeKashiKamokuEdabanCd;
	/** 掛け貸方課税区分 */
	String[] kakeKashiKazeiKbn;
	
	/** 摘要コード */
	String[] tekiyouCd;

//＜部品＞
	/** 支払依頼ロジック */
	ShiharaiIraiLogic shiharaiIraiLogic;
	/** 支払依頼Dao */
	ShiharaiIraiAbstractDao shiharaiIraiDao;
	/** 支払依頼明細Dao */
	ShiharaiIraiMeisaiAbstractDao shiharaiIraiMeisaiDao;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** マスターSELECT */
	DaishoMasterCategoryLogic dMasterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** バッチ会計ロジック */
	BatchKaikeiCommonLogic batchkaikeilogic;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 計上日・締日Dao */
	KeijoubiShimebiAbstractDao keijoubiShimebiDao;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 税率Dao */
	ShouhizeiritsuAbstractDao zeiritsuDao;
	/** 科目マスタDao */
	KamokuMasterDao  kamokuMasterDao;
	/** 科目枝番残高マスタ */
	KamokuEdabanZandakaAbstractDao edabanZandaka;
	

//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
		// 項目 //項目名 //キー項目？
		checkString (torihikisakiCd, 1, 12, ks.torihikisaki.getName() + "コード", false);
		checkString (torihikisakiNameRyakushiki, 1, 20, ks.torihikisaki.getName() + "名", false);

		checkDomain (shiharaiHouhou, shiharaiHouhouDomain, "支払方法", false);
		checkDomain (shiharaiShubetsu, shiharaiShubetsuDomain, "支払種別", false);
		checkDate (keijoubi, "計上日", false);
		checkDate (yoteibi, ks.yoteibi.getName(), false);
		checkDate (shiharaibi, "支払日", false);
		checkDomain (shouhyouShoruiFlg, EteamConst.Domain.FLG, ks.shouhyouShoruiFlg.getName(),  false);
		checkString (hf1Cd, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf1Name, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf2Cd, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf2Name, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf3Cd, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString (hf3Name, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString (hf4Cd, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString (hf4Name, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString (hf5Cd, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString (hf5Name, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString (hf6Cd, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString (hf6Name, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString (hf7Cd, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString (hf7Name, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString (hf8Cd, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString (hf8Name, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString (hf9Cd, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString (hf9Name, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString (hf10Cd, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkString (hf10Name, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkString (edi, 1, 20, "EDI", false);
		checkHankakuEiSuuHaihunSlash(edi,  "EDI");
		checkNumber (furikomiGinkouCd, 4, 4, "銀行コード", false);
		checkNumber (furikomiGinkouShitenCd, 3, 3, "支店コード", false);
		checkDomain (kouzaShubetsu, yokinShubetsuDomain, "口座種別", false);
		checkString (kouzaBangou, 7, 7, "口座番号", false);
		checkString (kouzaMeiginin, 1, 30, "口座名義人", false);
		checkHankakuKanaEiSuuHaihun(kouzaMeiginin,  "口座名義人", false);
		checkDomain (tesuuryou, Domain.FLG, "手数料", false);//1:先方 0:自社
		checkKingakuOver0 (shiharaiGoukei, ks.shiharaiGoukei.getName(), false);
		checkKingakuMinus (sousaiGoukei, ks.sousaiGoukei.getName(), false);
		checkKingakuMinus (kingaku, ks.sashihikiShiharaiGaku.getName(), false);
		checkKingakuOver0 (manekinGensen, manekinName, false);
		checkString (hosoku, 1, 240, ks.hosoku.getName(), false);
		checkDomain (this.nyuryokuHoushiki, this.nyuuryokuHoushikiDomain, ks.nyuryokuHoushiki.getName(), false);
		checkDomain (this.jigyoushaKbn, this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
		checkJigyoushaNo (this.jigyoushaNo, ks.jigyoushaNo.getName());

		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;
			checkNumber (shiwakeEdaNo[i], 1, 10, ks1.torihiki.getName() +  "コード：" + ip + "行目",false);
			checkString (torihikiName[i], 1, 20, ks1.torihiki.getName() +  "名："  + ip + "行目",false);
			checkString (kamokuCd[i], 1, 6, ks1.kamoku.getName() +  "コード："  + ip + "行目",false);
			checkString (kamokuName[i], 1, 22, ks1.kamoku.getName() +  "名："  + ip + "行目",false);
			checkString (kamokuEdabanCd[i], 1, 12, ks1.kamokuEdaban.getName() +  "コード："  + ip + "行目",false);
			checkString (kamokuEdabanName[i], 1, 20, ks1.kamokuEdaban.getName() +  "名："  + ip + "行目",false);
			checkString (futanBumonCd[i], 1, 8, ks1.futanBumon.getName() +  "コード："  + ip + "行目",false);
			checkString (futanBumonName[i], 1, 20, ks1.futanBumon.getName() +  "名："  + ip + "行目",false);
			checkString (projectCd[i], 1, 12, ks1.project.getName() +  "コード："  + ip + "行目",false);
			checkString (projectName[i], 1, 20, ks1.project.getName() +  "名："  + ip + "行目",false);
			checkString (segmentCd[i], 1, 8, ks1.segment.getName() + "コード：" + ip + "行目",false);
			checkString (segmentName[i], 1, 20, ks1.segment.getName() + "名：" + ip + "行目",false);
			checkString (uf1Cd[i], 1, 20, hfUfSeigyo.getUf1Name() +  "：" + ip + "行目",false);
			checkString (uf1Name[i], 1, 20, hfUfSeigyo.getUf1Name() +  "：" + ip + "行目",false);
			checkString (uf2Cd[i], 1, 20, hfUfSeigyo.getUf2Name() +  "：" + ip + "行目",false);
			checkString (uf2Name[i], 1, 20, hfUfSeigyo.getUf2Name() +  "：" + ip + "行目",false);
			checkString (uf3Cd[i], 1, 20, hfUfSeigyo.getUf3Name() +  "：" + ip + "行目",false);
			checkString (uf3Name[i], 1, 20, hfUfSeigyo.getUf3Name() +  "：" + ip + "行目",false);
			checkString (uf4Cd[i], 1, 20, hfUfSeigyo.getUf4Name() +  "：" + ip + "行目",false);
			checkString (uf4Name[i], 1, 20, hfUfSeigyo.getUf4Name() +  "：" + ip + "行目",false);
			checkString (uf5Cd[i], 1, 20, hfUfSeigyo.getUf5Name() +  "：" + ip + "行目",false);
			checkString (uf5Name[i], 1, 20, hfUfSeigyo.getUf5Name() +  "：" + ip + "行目",false);
			checkString (uf6Cd[i], 1, 20, hfUfSeigyo.getUf6Name() +  "：" + ip + "行目",false);
			checkString (uf6Name[i], 1, 20, hfUfSeigyo.getUf6Name() +  "：" + ip + "行目",false);
			checkString (uf7Cd[i], 1, 20, hfUfSeigyo.getUf7Name() +  "：" + ip + "行目",false);
			checkString (uf7Name[i], 1, 20, hfUfSeigyo.getUf7Name() +  "：" + ip + "行目",false);
			checkString (uf8Cd[i], 1, 20, hfUfSeigyo.getUf8Name() +  "：" + ip + "行目",false);
			checkString (uf8Name[i], 1, 20, hfUfSeigyo.getUf8Name() +  "：" + ip + "行目",false);
			checkString (uf9Cd[i], 1, 20, hfUfSeigyo.getUf9Name() +  "：" + ip + "行目",false);
			checkString (uf9Name[i], 1, 20, hfUfSeigyo.getUf9Name() +  "：" + ip + "行目",false);
			checkString (uf10Cd[i], 1, 20, hfUfSeigyo.getUf10Name() + "：" + ip + "行目",false);
			checkString (uf10Name[i], 1, 20, hfUfSeigyo.getUf10Name() + "：" + ip + "行目",false);
			checkDomain (kazeiKbn[i], kazeiKbnDomain, ks1.kazeiKbn.getName() +  "："  + ip + "行目",false);
			//課税区分が税込or税抜系の場合のみチェック
			if (kazeiKbnCheck[i]) {
				checkDomain (zeiritsu[i], zeiritsuDomain, ks1.zeiritsu.getName() +  "：" + ip + "行目",false);
				checkDomain (keigenZeiritsuKbn[i], Domain.FLG, "軽減税率区分" +  "：" + ip + "行目",false);
			}
			checkKingakuMinus (shiharaiKingaku[i], "金額"  +  "："  + ip + "行目",false);
			checkString (tekiyou[i], 1, this.getIntTekiyouMaxLength(), ks1.tekiyou.getName() +  "："  + ip + "行目",false);
			checkSJIS (tekiyou[i], ks1.tekiyou.getName() +  "："  + ip + "行目");
			checkDomain(this.bunriKbn[i], this.bunriKbnDomain, ks1.bunriKbn.getName() +  "：" + ip + "行目",false);
			checkDomain(this.kariShiireKbn[i], this.shiireKbnDomain, ks1.shiireKbn.getName() +  "：" + ip + "行目",false);
			checkKingakuMinus (this.zeinukiKingaku[i], ks1.zeinukiKingaku.getName() +  "："  + ip + "行目",false);
			checkKingakuMinus (this.shouhizeigaku[i], ks1.shouhizeigaku.getName()  +  "："  + ip + "行目",false);

		}
	}

	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum  1:登録/更新
	 */
	protected void denpyouHissuCheck(int eventNum) {
		String[][] list = {
			//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{torihikisakiCd, ks.torihikisaki.getName(), ks.torihikisaki.getHissuFlgS()},
			{this.ichigensakiTorihikisakiName, this.ks.shiharaisaki.getName(),  this.ichigenFlg && this.ks.shiharaisaki.getHissuFlg() ? "1" : "0"}, // 一見取引先の時、一見先名を入力しないとエラーを必須チェックに移動
// {torihikisakiNameRyakushiki, ks.torihikisaki.getName(), ks.torihikisaki.getHissuFlgS()},
			{shiharaiHouhou, "支払方法", "1"},
			{shiharaiShubetsu, "支払種別", "1"},
			{keijoubi, "計上日", "1"},
// {yoteibi, ks.yoteibi.getName(), ks.yoteibi.getHissuFlgS()},
			{shouhyouShoruiFlg, ks.shouhyouShoruiFlg.getName(), ks.shouhyouShoruiFlg.getHissuFlgS()},
			{shiharaiGoukei, ks.shiharaiGoukei.getName(), ks.shiharaiGoukei.getHissuFlgS()},
			{sousaiGoukei, ks.sousaiGoukei.getName(), ks.sousaiGoukei.getHissuFlgS()},
			{kingaku, ks.sashihikiShiharaiGaku.getName(), ks.sashihikiShiharaiGaku.getHissuFlgS()},
			{hosoku, ks.hosoku.getName(), csvUploadFlag ? "0" : ks.hosoku.getHissuFlgS()},
			{this.nyuryokuHoushiki, ks.nyuryokuHoushiki.getName(), ks.nyuryokuHoushiki.getHissuFlgS()},
			{this.jigyoushaKbn, ks.jigyoushaKbn.getName(), ks.jigyoushaKbn.getHissuFlgS()},
			{this.jigyoushaNo, ks.jigyoushaNo.getName(), ks.jigyoushaNo.getHissuFlgS()},
		};
		hissuCheckCommon(list, eventNum);
		
		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード"	, ks.hf1.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード"	, ks.hf2.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード"	, ks.hf3.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード"	, ks.hf4.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード"	, ks.hf5.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード"	, ks.hf6.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード"	, ks.hf7.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード"	, ks.hf8.getHissuFlgS()},}, eventNum);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード"	, ks.hf9.getHissuFlgS()},}, eventNum); 
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード"	, ks.hf10.getHissuFlgS()},}, eventNum); 
		
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;
			list = new String[][]{
				//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shiwakeEdaNo[i], ks1.torihiki.getName() + "コード："	+ ip + "行目", ks1.torihiki.getHissuFlgS()},
// {torihikiName[i], ks1.torihiki.getName() + "名："		+ ip + "行目", ks1.torihiki.getHissuFlgS()},
// {kamokuCd[i], ks1.kamoku.getName() + "コード："	+ ip + "行目", ks1.kamoku.getHissuFlgS()},
// {kamokuName[i], ks1.kamoku.getName() + "名："		+ ip + "行目", ks1.kamoku.getHissuFlgS()},
				{kamokuEdabanCd[i], ks1.kamokuEdaban.getName() + "コード："	+ ip + "行目", ks1.kamokuEdaban.getHissuFlgS()},
// {kamokuEdabanName[i], ks1.kamokuEdaban.getName() + "名："		+ ip + "行目", ks1.kamokuEdaban.getHissuFlgS()},
				{futanBumonCd[i], ks1.futanBumon.getName() + "コード："	+ ip + "行目", ks1.futanBumon.getHissuFlgS()},
// {futanBumonName[i], ks1.futanBumon.getName() + "名："		+ ip + "行目", ks1.futanBumon.getHissuFlgS()},
				{projectCd[i], ks1.project.getName() + "コード："	+ ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0"},
// {projectName[i], ks1.project.getName() + "名："		+ ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0"},
				{segmentCd[i], ks1.segment.getName() + "コード："	+ ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"},
// {segmentName[i], ks1.segment.getName() + "名："		+ ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"},
				{kazeiKbnCheck[i] ? zeiritsu[i] : "0", ks1.zeiritsu.getName() + "：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0"},
				{kazeiKbnCheck[i] ? keigenZeiritsuKbn[i] : "0", "軽減税率区分：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0"},
				{shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："			+ ip + "行目", ks1.shiharaiKingaku.getHissuFlgS()},
				{tekiyou[i], ks1.tekiyou.getName() + "："			+ ip + "行目", ks1.tekiyou.getHissuFlgS()},
				{this.bunriKbn[i], ks1.bunriKbn.getName() +  "：" + ip + "行目", csvUploadFlag ? "0" : ks1.bunriKbn.getHissuFlgS()},
				{this.kariShiireKbn[i], ks1.shiireKbn.getName() +  "：" + ip + "行目", csvUploadFlag ? "0" : ks1.shiireKbn.getHissuFlgS()},
				{this.zeinukiKingaku[i], ks1.zeinukiKingaku.getName() +  "："  + ip + "行目", ks1.zeinukiKingaku.getHissuFlgS()},
				{this.shouhizeigaku[i], ks1.shouhizeigaku.getName()  +  "："  + ip + "行目", ks1.shouhizeigaku.getHissuFlgS()},
			};
			hissuCheckCommon(list, eventNum);
			
			if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1Cd[i], hfUfSeigyo.getUf1Name() + "コード" + "："+ ip + "行目", ks1.uf1.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2Cd[i], hfUfSeigyo.getUf2Name() + "コード" + "："+ ip + "行目", ks1.uf2.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3Cd[i], hfUfSeigyo.getUf3Name() + "コード" + "："+ ip + "行目", ks1.uf3.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4Cd[i], hfUfSeigyo.getUf4Name() + "コード" + "："+ ip + "行目", ks1.uf4.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5Cd[i], hfUfSeigyo.getUf5Name() + "コード" + "："+ ip + "行目", ks1.uf5.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6Cd[i], hfUfSeigyo.getUf6Name() + "コード" + "："+ ip + "行目", ks1.uf6.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7Cd[i], hfUfSeigyo.getUf7Name() + "コード" + "："+ ip + "行目", ks1.uf7.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8Cd[i], hfUfSeigyo.getUf8Name() + "コード" + "："+ ip + "行目", ks1.uf8.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9Cd[i], hfUfSeigyo.getUf9Name() + "コード" + "："+ ip + "行目", ks1.uf9.getHissuFlgS()},}, eventNum);
			if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10Cd[i], hfUfSeigyo.getUf10Name() + "コード" + "："+ ip + "行目", ks1.uf10.getHissuFlgS()},}, eventNum);
		}
	}

	/**
	 * 必須チェック・形式チェック以外の入力チェック=相関チェック
	 * @param keijoubiTourokuFlg 0:エラーメッセージとして支払日が先、1:エラーメッセージとして計上日が先
	 *								※実態として、個別伝票から呼び出す場合は0、伝票一覧から支払日一括登録の場合は0、伝票一覧から計上日一括登録の場合は1
	 */
	protected void soukanCheck(String keijoubiTourokuFlg) {
		Date shimebi = this.keijoubiShimebiDao.findMaxShimebiForDenpyouKbn(DENPYOU_KBN.SIHARAIIRAI);
		Date today = EteamCommon.d2d(new Date(System.currentTimeMillis()));
		String joutai = getWfSeigyo().getDenpyouJoutai();
		
		//計上日のチェック
		if ((joutai.equals(DENPYOU_JYOUTAI.MI_KIHYOU) || joutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) && isNotEmpty(keijoubi)) {
			if (keijoubiSeigen && shimebi != null && ! toDate(keijoubi).after(shimebi)) {
				errorList.add("計上日には締日(" + formatDate(shimebi) + ")より後を入力してください。");
			}
		}
		
		//支払予定日のチェック
		if (isEmpty(yoteibi)) {
			errorList.add(ks.yoteibi.getName() + "を入力してください。");
		}
		if (isNotEmpty(keijoubi) && isNotEmpty(yoteibi)){
			if(toDate(keijoubi).after(toDate(yoteibi))){
				if(keijoubiTourokuFlg.equals("0")) {
					errorList.add(ks.yoteibi.getName() + "には計上日以降を入力してください。");
				}else if(keijoubiTourokuFlg.equals("1")){ 
					errorList.add( "計上日は"+ ks.yoteibi.getName() +"以前の日付を指定してください。");
				}
			}
		}
		
		// 支払種別以降は伝票一覧では変更されず、不要かつプロジェクトの仕訳チェックで障壁になりかねないのでここで終了
		// 20221202XX フラグ判定が逆だったので変更
		if(keijoubiTourokuFlg.equals("1"))
		{
			return;
		}
		
		//支払種別=その他の場合、支払予定日は手入力なので本日移行であることをチェック
		if ((joutai.equals(DENPYOU_JYOUTAI.MI_KIHYOU) || joutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) && isNotEmpty(yoteibi)) {
			if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)){
				if (! (toDate(yoteibi).equals(today) || toDate(yoteibi).after(today))) {
					errorList.add(ks.yoteibi.getName() + "には本日(" + formatDate(today) + ")以降を入力してください。");
				}
			}
		}

		//支払種別が「その他」の場合、振込先が必須(定期の場合は自動入力なのでなくても進むけど普通はあるはず)
		if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)) {
			if (isEmpty(furikomiGinkouCd)) errorList.add("銀行コードを入力してください。");
			if (isEmpty(furikomiGinkouShitenCd)) errorList.add("支店コードを入力してください。");
			if (isEmpty(kouzaShubetsu)) errorList.add("口座種別を入力してください。");
			if (isEmpty(kouzaBangou)) errorList.add("口座番号を入力してください。");
			if (isEmpty(kouzaMeiginin)) errorList.add("口座名義人を入力してください。");
			if (isEmpty(tesuuryou)) errorList.add("手数料を入力してください。");
		}
		
		// 伝票HF部チェック
		ShiwakeCheckData shiwakeCheckDataNaiyou = commonLogic.new ShiwakeCheckData() ;
		shiwakeCheckDataNaiyou.hf1Nm = hfUfSeigyo.getHf1Name();
		shiwakeCheckDataNaiyou.hf1Cd = hf1Cd;
		shiwakeCheckDataNaiyou.hf2Nm = hfUfSeigyo.getHf2Name();
		shiwakeCheckDataNaiyou.hf2Cd = hf2Cd;
		shiwakeCheckDataNaiyou.hf3Nm = hfUfSeigyo.getHf3Name();
		shiwakeCheckDataNaiyou.hf3Cd = hf3Cd;
		shiwakeCheckDataNaiyou.hf4Nm = hfUfSeigyo.getHf4Name();
		shiwakeCheckDataNaiyou.hf4Cd = hf4Cd;
		shiwakeCheckDataNaiyou.hf5Nm = hfUfSeigyo.getHf5Name();
		shiwakeCheckDataNaiyou.hf5Cd = hf5Cd;
		shiwakeCheckDataNaiyou.hf6Nm = hfUfSeigyo.getHf6Name();
		shiwakeCheckDataNaiyou.hf6Cd = hf6Cd;
		shiwakeCheckDataNaiyou.hf7Nm = hfUfSeigyo.getHf7Name();
		shiwakeCheckDataNaiyou.hf7Cd = hf7Cd;
		shiwakeCheckDataNaiyou.hf8Nm = hfUfSeigyo.getHf8Name();
		shiwakeCheckDataNaiyou.hf8Cd = hf8Cd;
		shiwakeCheckDataNaiyou.hf9Nm = hfUfSeigyo.getHf9Name();
		shiwakeCheckDataNaiyou.hf9Cd = hf9Cd;
		shiwakeCheckDataNaiyou.hf10Nm = hfUfSeigyo.getHf10Name();
		shiwakeCheckDataNaiyou.hf10Cd = hf10Cd;
		commonLogic.checkShiwake(DENPYOU_KBN.SIHARAIIRAI, shiwakeCheckDataNaiyou, errorList);
		
		// 取引先仕入先チェック
		if (! ichigenFlg) super.checkShiiresaki(ks.torihikisaki.getName() + "コード", torihikisakiCd, DENPYOU_KBN.SIHARAIIRAI);
		
		if (! errorList.isEmpty())
		{
			return;
		}

		//明細単位の仕訳チェック
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;

			//税率チェック
			if (kazeiKbnCheck[i]) {
				commonLogic.checkZeiritsu(toDecimal(zeiritsu[i]), keigenZeiritsuKbn[i], null, errorList, ip + "行目：");
			}
			
			//仕訳パターン
			ShiwakePatternMaster shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[i]));
			
			//税率チェック
			//ダイショー様　要望整理No86)消費税率5％と消費税率8％を入力することがあるのですが、消費税率5％で入力すると”消費税率は日付[yyyy/mm/dd]で有効ではありません。”のメッセージが表示され、登録できません。消費税率が8％のみ登録できる状態なのですが、5％・8％両方で入力出来るようにしてください。
// commonLogic.checkZeiritsu(toDecimal(zeiritsu[i]), toDate(keijoubi), errorList);

			// 借方
			ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData() ;
			shiwakeCheckData.torihikisakiNm = ip + "行目：" + ks.torihikisaki.getName()  + "コード";
			shiwakeCheckData.torihikisakiCd = kariTorihikisakiCd[i];
			shiwakeCheckData.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
			shiwakeCheckData.shiwakeEdaNo = shiwakeEdaNo[i];
			shiwakeCheckData.kamokuNm = ip + "行目：" + ks1.kamoku.getName() + "コード";
			shiwakeCheckData.kamokuCd = kamokuCd[i];
			shiwakeCheckData.kamokuEdabanNm = ip + "行目：" + ks1.kamokuEdaban.getName() + "コード";
			shiwakeCheckData.kamokuEdabanCd = kamokuEdabanCd[i];
			shiwakeCheckData.futanBumonNm = ip + "行目：" + ks1.futanBumon.getName() + "コード";
			shiwakeCheckData.futanBumonCd = futanBumonCd[i];
			shiwakeCheckData.projectNm = ip + "行目：" + ks1.project.getName() + "コード";
			if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kariProjectCd))
			{
				shiwakeCheckData.projectCd = projectCd[i];
			}
			shiwakeCheckData.segmentNm = ip + "行目：" + ks1.segment.getName() + "コード";
			if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kariSegmentCd))
			{
				shiwakeCheckData.segmentCd = segmentCd[i];
			}
			shiwakeCheckData.kazeiKbnNm = ip + "行目：" + ks1.kazeiKbn.getName();
			shiwakeCheckData.kazeiKbn = kazeiKbn[i];
			shiwakeCheckData.uf1Nm = ip + "行目：" + hfUfSeigyo.getUf1Name();
			shiwakeCheckData.uf2Nm = ip + "行目：" + hfUfSeigyo.getUf2Name();
			shiwakeCheckData.uf3Nm = ip + "行目：" + hfUfSeigyo.getUf3Name();
			shiwakeCheckData.uf4Nm = ip + "行目：" + hfUfSeigyo.getUf4Name();
			shiwakeCheckData.uf5Nm = ip + "行目：" + hfUfSeigyo.getUf5Name();
			shiwakeCheckData.uf6Nm = ip + "行目：" + hfUfSeigyo.getUf6Name();
			shiwakeCheckData.uf7Nm = ip + "行目：" + hfUfSeigyo.getUf7Name();
			shiwakeCheckData.uf8Nm = ip + "行目：" + hfUfSeigyo.getUf8Name();
			shiwakeCheckData.uf9Nm = ip + "行目：" + hfUfSeigyo.getUf9Name();
			shiwakeCheckData.uf10Nm = ip + "行目：" + hfUfSeigyo.getUf10Name();
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf1Cd))
			{
				shiwakeCheckData.uf1Cd = uf1Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf2Cd))
			{
				shiwakeCheckData.uf2Cd = uf2Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf3Cd))
			{
				shiwakeCheckData.uf3Cd = uf3Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf4Cd))
			{
				shiwakeCheckData.uf4Cd = uf4Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf5Cd))
			{
				shiwakeCheckData.uf5Cd = uf5Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf6Cd))
			{
				shiwakeCheckData.uf6Cd = uf6Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf7Cd))
			{
				shiwakeCheckData.uf7Cd = uf7Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf8Cd))
			{
				shiwakeCheckData.uf8Cd = uf8Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf9Cd))
			{
				shiwakeCheckData.uf9Cd = uf9Cd[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf10Cd))
			{
				shiwakeCheckData.uf10Cd = uf10Cd[i];
			}
			commonLogic.checkShiwake(DENPYOU_KBN.SIHARAIIRAI, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckData, super.daihyouFutanBumonCd, errorList);

			// 貸方（未払）
			ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData() ;
			String kashiNo = (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) ? "2" : "3";
			shiwakeCheckDataKashi.torihikisakiNm = ks.torihikisaki.getName()  + "コード";
			shiwakeCheckDataKashi.torihikisakiCd = kashiTorihikisakiCd[i];
			shiwakeCheckDataKashi.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
			shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo[i];
			shiwakeCheckDataKashi.kamokuNm = ip + "行目：貸方" + ks1.kamoku.getName() + "コード" + kashiNo;
			shiwakeCheckDataKashi.kamokuCd = kashiKamokuCd[i];
			shiwakeCheckDataKashi.kamokuEdabanNm = ip + "行目：貸方" + ks1.kamokuEdaban.getName() + "コード" + kashiNo;
			shiwakeCheckDataKashi.kamokuEdabanCd = kashiKamokuEdabanCd[i];
			shiwakeCheckDataKashi.futanBumonNm = ip + "行目：貸方" + ks1.futanBumon.getName() + "コード" + kashiNo;
			shiwakeCheckDataKashi.futanBumonCd = kashiFutanBumonCd[i];
			shiwakeCheckDataKashi.kazeiKbnNm = ip + "行目：貸方" + ks1.kazeiKbn.getName() + kashiNo;
			shiwakeCheckDataKashi.kazeiKbn = kashiKazeiKbn[i];
			commonLogic.checkShiwake(DENPYOU_KBN.SIHARAIIRAI, kashiNo.equals("2") ? SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2 : SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, shiwakeCheckDataKashi, super.daihyouFutanBumonCd, errorList);

			// 貸方（支払）
			shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData() ;
			shiwakeCheckDataKashi.torihikisakiNm = ks.torihikisaki.getName()  + "コード";
			shiwakeCheckDataKashi.torihikisakiCd = kakeKashiTorihikisakiCd[i];
			shiwakeCheckDataKashi.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
			shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo[i];
			shiwakeCheckDataKashi.kamokuNm = ip + "行目：貸方" + ks1.kamoku.getName() + "コード1";
			shiwakeCheckDataKashi.kamokuCd = kakeKashiKamokuCd[i];
			shiwakeCheckDataKashi.kamokuEdabanNm = ip + "行目：貸方" + ks1.kamokuEdaban.getName() + "コード1";
			shiwakeCheckDataKashi.kamokuEdabanCd = kakeKashiKamokuEdabanCd[i];
			shiwakeCheckDataKashi.futanBumonNm = ip + "行目：貸方" + ks1.futanBumon.getName() + "コード1";
			shiwakeCheckDataKashi.futanBumonCd = kakeKashiFutanBumonCd[i];
			shiwakeCheckDataKashi.kazeiKbnNm = ip + "行目：貸方" + ks1.kazeiKbn.getName() + "1";
			shiwakeCheckDataKashi.kazeiKbn = kakeKashiKazeiKbn[i];
			commonLogic.checkShiwake(DENPYOU_KBN.SIHARAIIRAI, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi, super.daihyouFutanBumonCd, errorList);
		}
	}

	/**
	 * 支払依頼申請画面から相関チェックを呼び出す際、こちらで呼び出し元フラグを設定し渡してあげる。
	 * 支払依頼申請画面からの呼び出しのため、呼び出し元フラグは"0"となる。
	 */
	protected void soukanCheck(){
		soukanCheck("0");
	}

//＜伝票共通から呼ばれるイベント処理＞
	//個別伝票について表示処理を行う。
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		//参照フラグ(ture:参照起票である、false:参照起票でない)
		boolean sanshou = false;
		
		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (String)usrInfo.get("shain_no");

		//新規起票時の表示状態作成
		if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {
			makeDefaultMeisai();
			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			hf1Cd = ("HF1".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf2Cd = ("HF2".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf3Cd = ("HF3".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf4Cd = ("HF4".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf5Cd = ("HF5".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf6Cd = ("HF6".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf7Cd = ("HF7".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf8Cd = ("HF8".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf9Cd = ("HF9".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf10Cd = ("HF10".equals(shainCdRenkeiArea)) ? initShainCd : "";

			// 入力方式の初期値
			nyuryokuHoushiki = setting.zeiDefaultA013();
			//インボイス伝票初期値
			invoiceDenpyou = setInvoiceFlgWhenNew();
			
			if(ks.shouhyouShoruiFlg.getHyoujiFlg()) shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA013();
			
		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			ShiharaiIrai shinseiData = this.shiharaiIraiDao.find(denpyouId);
			shinseiData2Gamen(shinseiData);

			List<ShiharaiIraiMeisai> meisaiList = this.shiharaiIraiMeisaiDao.loadByDenpyouId(denpyouId);
			meisaiData2Gamen(meisaiList, sanshou, connection);

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			ShiharaiIrai shinseiData = this.shiharaiIraiDao.find(super.sanshouDenpyouId);
			shinseiData2Gamen(shinseiData);

			List<ShiharaiIraiMeisai> meisaiList = this.shiharaiIraiMeisaiDao.loadByDenpyouId(super.sanshouDenpyouId);
			meisaiData2Gamen(meisaiList, sanshou, connection);
			
			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			if("HF1".equals(shainCdRenkeiArea)){ hf1Cd = initShainCd; }
			if("HF2".equals(shainCdRenkeiArea)){ hf2Cd = initShainCd; }
			if("HF3".equals(shainCdRenkeiArea)){ hf3Cd = initShainCd; }
			if("HF4".equals(shainCdRenkeiArea)){ hf4Cd = initShainCd; }
			if("HF5".equals(shainCdRenkeiArea)){ hf5Cd = initShainCd; }
			if("HF6".equals(shainCdRenkeiArea)){ hf6Cd = initShainCd; }
			if("HF7".equals(shainCdRenkeiArea)){ hf7Cd = initShainCd; }
			if("HF8".equals(shainCdRenkeiArea)){ hf8Cd = initShainCd; }
			if("HF9".equals(shainCdRenkeiArea)){ hf9Cd = initShainCd; }
			if("HF10".equals(shainCdRenkeiArea)){ hf10Cd = initShainCd; }
			
			// 日付はブランク
			keijoubi = "";
			yoteibi = "";
			shiharaibi = "";
			shiharaiKijitsu = "";
		}

		//表示制御（プルダウンとかの取得は表示方法によらず同じ）
		displaySeigyo(connection);
	}

	//登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 <errorList.size())
		{
			return;
		}

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
		reloadMaster(connection);
		if (0 <errorList.size())
		{
			return;
		}
		
		//相関チェック
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}
		
		//CSV一括登録の際に起案伝票IDが入力されている場合、起案添付できる伝票IDかチェックする。
		if(csvUploadFlag && isNotEmpty(getKianDenpyouId()[0])) {
			checkKianDenpyouId();
			if (0 <errorList.size())
			{
				return;
			}
		}
	}

	//更新ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {
		
		// 初期値が会社設定に依るため、一括登録のThreadクラスでなくここでセットすることにした
		if(csvUploadFlag) shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA013();

		//申請内容登録
		ShiharaiIrai dto = this.createDto();
		this.shiharaiIraiDao.insert(dto, dto.tourokuUserId);

		//明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			ShiharaiIraiMeisai meisaiDto = this.createMeisaiDto(i);
			this.shiharaiIraiMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
		}
	}

	//更新ボタン押下時に、個別伝票について以下を行う。
	//・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	//・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 <errorList.size())
		{
			return;
		}

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
		reloadMaster(connection);
		if (0 <errorList.size())
		{
			return;
		}
		
		//相関チェック
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}

		//申請内容登録
		ShiharaiIrai dto = this.createDto();
		dto.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		this.shiharaiIraiDao.update(dto, dto.koushinUserId);

		//明細削除
		this.shiharaiIraiMeisaiDao.deleteByDenpyouId(this.denpyouId);

		//明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			ShiharaiIraiMeisai meisaiDto = this.createMeisaiDto(i);
			meisaiDto.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
			this.shiharaiIraiMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
		}
	}

	//承認ボタン押下時に、個別伝票について以下を行う。
	//・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 再表示用
		displaySeigyo(connection);
		
		// 実際にDBに登録されている値でチェック
		ShiharaiIrai shinsei = this.shiharaiIraiDao.find(denpyouId);
		String shiharai = formatDate(shinsei.shiharaibi);
		String keijou = formatDate(shinsei.keijoubi);
		//共通の支払日チェック
		if (isNotEmpty(shiharai) && commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			checkShiharaiBi(shiharai, keijou);
		}
	}
	
	//登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){
		BigDecimal zandaka = (BigDecimal)ringiData.get("ringiKingakuZandaka");
		ringiData.put("ringiKingakuZandaka", zandaka.subtract(toDecimal(kingaku)));
	}
	
	/**
	 * 基準日の取得
	 * @return 基準日
	 */
	@Override
	public String judgeKijunbi() {
		return keijoubi;
	}

	// 会社設定で指定されたルールに則り、代表仕訳枝番号を決定する。
	@Override
	protected String getDaihyouTorihiki(){
		String ret = shiwakeEdaNo[0];
		String torihikiSentakuRule = EteamSettingInfo.getSettingInfo("route_hantei_torihiki_sentaku_rule");
		if(torihikiSentakuRule.equals(EteamConst.torihikiSentakuRule.KINGAKU_MAX)){
			int maxIndex = 0;
			BigDecimal maxKingaku = toDecimal(shiharaiKingaku[maxIndex]);
			for(int i = 1 ; i < shiwakeEdaNo.length ; i++){
				if(maxKingaku.compareTo(toDecimal(shiharaiKingaku[i])) < 0){
					maxIndex = i;
					maxKingaku = toDecimal(shiharaiKingaku[i]);
				}
			}
			ret = shiwakeEdaNo[maxIndex];
		}
		return ret;
	}
	
	/**
	 * 請求書払い申請EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		ShiharaiIraiXlsLogic xlsLogic = EteamContainer.getComponent(ShiharaiIraiXlsLogic.class, connection);
		xlsLogic.makeExcel(denpyouId, out);
	}

//＜独自のイベント処理＞
	/**
	 * 逆仕訳イベント
	 * @return 処理結果
	 */
	public String gyakuShiwake() {
// //親のパラメータなければエラー
// super.formatCheck();
// super.hissuCheck(3);
// 
// try(EteamConnection connection = EteamConnection.connect()) {
// initParts(connection);
// 
// //逆仕訳できる状態じゃなかったら何もせず終わり
// if (! judgeGyakuShiwake(connection)) {
// return "success";
// }
//
// //逆仕訳処理
// SIHARAIIRAIChuushutsuBat bat = EteamContainer.getComponent(SIHARAIIRAIChuushutsuBat.class);
// bat.denpyouChuushutsuGyaku(denpyouId); 
// 
			return "success";
// }
	}
	
	/**
	 * 取引先マスターリロード
	 * @return 処理結果
	 */
	public String torihikisakiMasterReload() {
		try(EteamConnection connection = EteamConnection.connect()) {
			super.setConnection(connection);
			
			//再表示用(親)
			super.initialize();
			super.accessCheck(Event.INIT);
			super.loadData();
			//再表示用(子)
			this.initParts(connection);
			this.displaySeigyo(connection);

			//マスターリロード
			reloadMasterShiharai(connection);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			return "success";
		}
	}

	//科目が予算執行対象かどうか判定する。
	@Override
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection connection) {
	
		HashSet<String> kamokuCdSet = new HashSet<String>();
		for(int i = 0 ; i < kamokuCd.length ; i++){
			kamokuCdSet.add(kamokuCd[i]);
		}
		if(0 == kamokuCdSet.size()) { return false; }
		
		if(null == commonLogic){
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		}
		return commonLogic.isYosanShikkouKamoku(denpyouId, kamokuCdSet, kianbangouBoDialogNendo);
	}
//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		shiharaiIraiLogic = EteamContainer.getComponent(ShiharaiIraiLogic.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		dMasterLogic = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		this.shiharaiIraiDao = EteamContainer.getComponent(ShiharaiIraiDao.class, connection);
		this.shiharaiIraiMeisaiDao = EteamContainer.getComponent(ShiharaiIraiMeisaiDao.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		this.keijoubiShimebiDao = EteamContainer.getComponent(KeijoubiShimebiDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		edabanZandaka =  EteamContainer.getComponent(KamokuEdabanZandakaAbstractDao.class, connection);
	}

	/**
	 * 請求書払いテーブルのレコード情報を画面項目に移す
	 * @param shinseiData 請求書払いレコード
	 */
	protected void shinseiData2Gamen(ShiharaiIrai shinseiData) {
		this.keijoubi = super.formatDate(shinseiData.keijoubi);
		this.yoteibi = super.formatDate(shinseiData.yoteibi);
		this.shiharaibi = super.formatDate(shinseiData.shiharaibi);
		this.shiharaiKijitsu = super.formatDate(shinseiData.shiharaiKijitsu);
		this.torihikisakiCd = shinseiData.torihikisakiCd;
		this.torihikisakiNameRyakushiki = shinseiData.torihikisakiNameRyakushiki;
		this.ichigensakiTorihikisakiName = shinseiData.ichigensakiTorihikisakiName;
		this.edi = shinseiData.edi;
		this.shiharaiGoukei = super.formatMoney(shinseiData.shiharaiGoukei);
		this.sousaiGoukei = super.formatMoney(shinseiData.sousaiGoukei);
		this.kingaku = super.formatMoney(shinseiData.sashihikiShiharai);
		this.manekinGensen = super.formatMoney(shinseiData.manekinGensen);
		this.shouhyouShoruiFlg = shinseiData.shouhyouShoruiFlg;
		this.hf1Cd = shinseiData.hf1Cd;
		this.hf1Name = shinseiData.hf1NameRyakushiki;
		this.hf2Cd = shinseiData.hf2Cd;
		this.hf2Name = shinseiData.hf2NameRyakushiki;
		this.hf3Cd = shinseiData.hf3Cd;
		this.hf3Name = shinseiData.hf3NameRyakushiki;
		this.hf4Cd = shinseiData.hf4Cd;
		this.hf4Name = shinseiData.hf4NameRyakushiki;
		this.hf5Cd = shinseiData.hf5Cd;
		this.hf5Name = shinseiData.hf5NameRyakushiki;
		this.hf6Cd = shinseiData.hf6Cd;
		this.hf6Name = shinseiData.hf6NameRyakushiki;
		this.hf7Cd = shinseiData.hf7Cd;
		this.hf7Name = shinseiData.hf7NameRyakushiki;
		this.hf8Cd = shinseiData.hf8Cd;
		this.hf8Name = shinseiData.hf8NameRyakushiki;
		this.hf9Cd = shinseiData.hf9Cd;
		this.hf9Name = shinseiData.hf9NameRyakushiki;
		this.hf10Cd = shinseiData.hf10Cd;
		this.hf10Name = shinseiData.hf10NameRyakushiki;
		this.shiharaiHouhou = shinseiData.shiharaiHouhou;
		this.shiharaiShubetsu = shinseiData.shiharaiShubetsu;
		this.furikomiGinkouCd = shinseiData.furikomiGinkouCd;
		this.furikomiGinkouName = shinseiData.furikomiGinkouName;
		this.furikomiGinkouShitenCd = shinseiData.furikomiGinkouShitenCd;
		this.furikomiGinkouShitenName = shinseiData.furikomiGinkouShitenName;
		this.kouzaShubetsu = shinseiData.yokinShubetsu;
		this.kouzaBangou = shinseiData.kouzaBangou;
		this.kouzaMeiginin = shinseiData.kouzaMeiginin;
		this.tesuuryou = shinseiData.tesuuryou;
		this.hosoku = shinseiData.hosoku;
		this.nyuryokuHoushiki = shinseiData.nyuryokuHoushiki;
		this.jigyoushaKbn = shinseiData.jigyoushaKbn;
		this.jigyoushaNo = shinseiData.jigyoushaNo;
		this.invoiceDenpyou = shinseiData.invoiceDenpyou;
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisai() {
		//消費税率マップ
		GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();
		
		//表示項目
		shiwakeEdaNo = new String[] { "" };
		torihikiName = new String[] { "" };
		kamokuCd = new String[] { "" };
		kamokuName = new String[] { "" };
		kamokuEdabanCd = new String[] { "" };
		kamokuEdabanName = new String[] { "" };
		futanBumonCd = new String[] { "" };
		futanBumonName = new String[] { "" };
		tokuisakiCd = new String[] { "" };
		tokuisakiName = new String[] { "" };
		kariKanjouKeshikomiNo = new String[] { "" };
		projectCd = new String[] { "" };
		projectName = new String[] { "" };
		segmentCd = new String[] { "" };
		segmentName = new String[] { "" };
		uf1Cd = new String[] { "" };
		uf1Name = new String[] { "" };
		uf2Cd = new String[] { "" };
		uf2Name = new String[] { "" };
		uf3Cd = new String[] { "" };
		uf3Name = new String[] { "" };
		uf4Cd = new String[] { "" };
		uf4Name = new String[] { "" };
		uf5Cd = new String[] { "" };
		uf5Name = new String[] { "" };
		uf6Cd = new String[] { "" };
		uf6Name = new String[] { "" };
		uf7Cd = new String[] { "" };
		uf7Name = new String[] { "" };
		uf8Cd = new String[] { "" };
		uf8Name = new String[] { "" };
		uf9Cd = new String[] { "" };
		uf9Name = new String[] { "" };
		uf10Cd = new String[] { "" };
		uf10Name = new String[] { "" };
		kazeiKbn = new String[] { "" };
		zeiritsu = new String[] { initZeiritsu.get("zeiritsu").toString() };
		keigenZeiritsuKbn = new String[] { initZeiritsu.get("keigen_zeiritsu_flg") };
		shiharaiKingaku = new String[] { "" };
		tekiyou = new String[] { "" };
		chuuki2 = new String[] { "" };
		this.bunriKbn = new String[] { null }; // nullPointerExceptionにならないかは要検証。一応デフォルト値。
		this.kariShiireKbn = new String[] { "" };
		this.zeinukiKingaku = new String[] { "0" };
		this.shouhizeigaku = new String[] { "0" };
		
		//制御情報
		kamokuEdabanEnable = new boolean[1];
		futanBumonEnable = new boolean[1];
		projectEnable = new boolean[1];
		zeiritsuEnable = new boolean[1];
	}

	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList  明細レコードのリスト
	 * @param sanshou     参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection  コネクション
	 */
	protected void meisaiData2Gamen(List<ShiharaiIraiMeisai> meisaiList, boolean sanshou, EteamConnection connection) {
		int length = meisaiList.size();
		shiwakeEdaNo  = new String[length];
		torihikiName = new String[length];
		kamokuCd = new String[length];
		kamokuName = new String[length];
		kamokuEdabanCd = new String[length];
		kamokuEdabanName = new String[length];
		futanBumonCd = new String[length];
		futanBumonName = new String[length];
		tokuisakiCd = new String[length];
		tokuisakiName = new String[length];
		projectCd = new String[length];
		projectName = new String[length];
		segmentCd = new String[length];
		segmentName = new String[length];
		uf1Cd = new String[length];
		uf1Name = new String[length];
		uf2Cd = new String[length];
		uf2Name = new String[length];
		uf3Cd = new String[length];
		uf3Name = new String[length];
		uf4Cd = new String[length];
		uf4Name = new String[length];
		uf5Cd = new String[length];
		uf5Name = new String[length];
		uf6Cd = new String[length];
		uf6Name = new String[length];
		uf7Cd = new String[length];
		uf7Name = new String[length];
		uf8Cd = new String[length];
		uf8Name = new String[length];
		uf9Cd = new String[length];
		uf9Name = new String[length];
		uf10Cd = new String[length];
		uf10Name = new String[length];
		kazeiKbn = new String[length];
		zeiritsu = new String[length];
		keigenZeiritsuKbn = new String[length];
		shiharaiKingaku = new String[length];
		tekiyou = new String[length];
		chuuki2 = new String[length];
		tekiyouCd = new String[length];
		this.bunriKbn = new String[length];
		this.kariShiireKbn = new String[length];
		this.zeinukiKingaku = new String[length];
		this.shouhizeigaku = new String[length];
		
		for (int i = 0; i < length; i++) {
			ShiharaiIraiMeisai meisai = meisaiList.get(i);
			this.shiwakeEdaNo[i] = Integer.toString(meisai.shiwakeEdano);
			this.torihikiName[i] = meisai.torihikiName;
			this.tekiyou[i] = meisai.tekiyou;
			this.shiharaiKingaku[i] = super.formatMoney(meisai.shiharaiKingaku);
			this.futanBumonCd[i] = meisai.kariFutanBumonCd;
			this.futanBumonName[i] = meisai.kariFutanBumonName;
			this.kamokuCd[i] = meisai.kariKamokuCd;
			this.kamokuName[i] = meisai.kariKamokuName;
			this.kamokuEdabanCd[i] = meisai.kariKamokuEdabanCd;
			this.kamokuEdabanName[i] = meisai.kariKamokuEdabanName;
			this.kazeiKbn[i] = meisai.kariKazeiKbn;
			this.zeiritsu[i] = super.formatMoney(meisai.zeiritsu);
			this.keigenZeiritsuKbn[i] = meisai.keigenZeiritsuKbn;
			this.uf1Cd[i] = meisai.uf1Cd;
			this.uf1Name[i] = meisai.uf1NameRyakushiki;
			this.uf2Cd[i] = meisai.uf2Cd;
			this.uf2Name[i] = meisai.uf2NameRyakushiki;
			this.uf3Cd[i] = meisai.uf3Cd;
			this.uf3Name[i] = meisai.uf3NameRyakushiki;
			this.uf4Cd[i] = meisai.uf4Cd;
			this.uf4Name[i] = meisai.uf4NameRyakushiki;
			this.uf5Cd[i] = meisai.uf5Cd;
			this.uf5Name[i] = meisai.uf5NameRyakushiki;
			this.uf6Cd[i] = meisai.uf6Cd;
			this.uf6Name[i] = meisai.uf6NameRyakushiki;
			this.uf7Cd[i] = meisai.uf7Cd;
			this.uf7Name[i] = meisai.uf7NameRyakushiki;
			this.uf8Cd[i] = meisai.uf8Cd;
			this.uf8Name[i] = meisai.uf8NameRyakushiki;
			this.uf9Cd[i] = meisai.uf9Cd;
			this.uf9Name[i] = meisai.uf9NameRyakushiki;
			this.uf10Cd[i] = meisai.uf10Cd;
			this.uf10Name[i] = meisai.uf10NameRyakushiki;
			this.projectCd[i] = meisai.projectCd;
			this.projectName[i] = meisai.projectName;
			this.segmentCd[i] = meisai.segmentCd;
			this.segmentName[i] = meisai.segmentNameRyakushiki;
			this.tekiyouCd[i] = meisai.tekiyouCd;
			this.bunriKbn[i] = meisai.bunriKbn;
			this.kariShiireKbn[i] = meisai.kariShiireKbn;
			this.zeinukiKingaku[i] = super.formatMoney(meisai.zeinukiKingaku);
			this.shouhizeigaku[i] = super.formatMoney(meisai.shouhizeigaku);
			if(!sanshou){
				String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.SIHARAIIRAI, meisai.map, null, "0"); // ごまかし（いずれはdtoでやりたいが…）
				String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
				if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
					chuuki1 = commonLogic.getTekiyouChuuki();
					chuuki2[i] = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
				}
			}
		}
		
		//その他・一見先で差引支払額が一見先振込手数料以下ならメッセージ
		if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA) && torihikisakiCd.equals(setting.ichigenCd()) && tesuuryou.equals(TESUURYOU.SENPOU_FUTAN)) {
			BigDecimal tesuuryouGaku = dMasterLogic.judgeTesuuryou(toDecimal(kingaku), furikomiGinkouCd);
			if (toDecimal(kingaku).doubleValue() <= tesuuryouGaku.doubleValue()) {
				if (isEmpty(chuuki1))
				{
					chuuki1 = "";
				} else chuuki1 += "\r\n";
				chuuki1 += "差引支払額が振込手数料以下です。";
			}
		}
	}
	
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		//プルダウンのリストを取得
		zeiritsuList = this.zeiritsuDao.load();
		zeiritsuDomain = zeiritsuList.stream().map(item -> item.zeiritsu.toString()).collect(Collectors.toList()).toArray(new String[0]);
		kazeiKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn"); // 支払依頼専用区分はただの重複で、今となっては考慮不要
		kazeiKbnDomain = kazeiKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		shiharaiShubetsuList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiharai_irai_shubetsu");
		shiharaiShubetsuDomain = shiharaiShubetsuList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		yokinShubetsuList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("yokin_shubetsu");
		yokinShubetsuDomain = yokinShubetsuList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		bunriKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn");
		bunriKbnDomain = bunriKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		shiireKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn");
		shiireKbnDomain = shiireKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		nyuuryokuHoushikiList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("nyuryoku_flg");
		nyuuryokuHoushikiDomain = nyuuryokuHoushikiList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		jigyoushaKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn");
		jigyoushaKbnDomain = jigyoushaKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);

		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();
		
		//支払日の入力状態・モード(0:非表示、1:入力可、2:表示)を判定
// shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);
// if (shiharaiBiMode == 0)
		{
			shiharaiBiMode = 2;
		}
		if (enableInput)
		{
			shiharaiBiMode = 1;
		}//ダイショー様　WF対応一覧No20：申請後の伝票に対して、支払日を登録した状態にしてください。
		if(setting.keijouNyuuryoku().equals("2")) {
			keijoubiList = masterLogic.loadKeijoubiList(DENPYOU_KBN.SIHARAIIRAI);
		}

		//明細単位に仕訳パターンによる制御
		int length = shiwakeEdaNo.length;
		kamokuEdabanEnable = new boolean[length];
		futanBumonEnable = new boolean[length];
		projectEnable = new boolean[length];
		segmentEnable = new boolean[length];
		uf1Enable = new boolean[length];
		uf2Enable = new boolean[length];
		uf3Enable = new boolean[length];
		uf4Enable = new boolean[length];
		uf5Enable = new boolean[length];
		uf6Enable = new boolean[length];
		uf7Enable = new boolean[length];
		uf8Enable = new boolean[length];
		uf9Enable = new boolean[length];
		uf10Enable = new boolean[length];
		zeiritsuEnable = new boolean[length];
		shoriGroup = new String[length];
		kazeiKbnCheck = new boolean[length];
		for (int i = 0; i < length; i++) {
			if (enableInput) {
				if (isNotEmpty(shiwakeEdaNo[i])) {
					//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
					//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
					GMap shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[i])).map;
					InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
					kamokuEdabanEnable[i] = info.kamokuEdabanEnable;
					futanBumonEnable[i] = info.futanBumonEnable;
					projectEnable[i] = info.projectEnable;
					segmentEnable[i] = info.segmentEnable;
					uf1Enable[i] = info.uf1Enable;
					uf2Enable[i] = info.uf2Enable;
					uf3Enable[i] = info.uf3Enable;
					uf4Enable[i] = info.uf4Enable;
					uf5Enable[i] = info.uf5Enable;
					uf6Enable[i] = info.uf6Enable;
					uf7Enable[i] = info.uf7Enable;
					uf8Enable[i] = info.uf8Enable;
					uf9Enable[i] = info.uf9Enable;
					uf10Enable[i] = info.uf10Enable;
					zeiritsuEnable[i] = info.zeiritsuEnable;
				}
				//課税区分の制御
				if (isNotEmpty(kamokuCd[i])) {
					GMap kmk = masterLogic.findKamokuMaster(kamokuCd[i]);
					if (kmk != null) shoriGroup[i] = kmk.get("shori_group").toString();
				}
			}
			kazeiKbnCheck[i] = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]);
		}

		//経理で逆仕訳未作成なら逆仕訳可能
		gyakuShiwakeEnabled = judgeGyakuShiwake(connection);
		
		//一見取引先判定
		ichigenFlg = isNotEmpty(torihikisakiCd) && torihikisakiCd.equals(setting.ichigenCd());
		GMap master = masterLogic.findTorihikisakiJouhou(torihikisakiCd, false);
		String tekikakuJigyousha = master == null ? "" : (String)master.get("tekikaku_no");
		if (! ichigenFlg)
		{
			ichigensakiTorihikisakiName = "";
			//一見先以外なら取引先マスタを優先
			if(isNotEmpty(tekikakuJigyousha)) {
				jigyoushaNo = tekikakuJigyousha;
			}
		}else if(csvUploadFlag){
			//一見先ならcsvの適格事業者番号を優先
			if(isEmpty(jigyoushaNo) && isNotEmpty(tekikakuJigyousha)) {
				jigyoushaNo = tekikakuJigyousha;
			}
		}
		
		//マスタ→定期、一見先→その他で固定
		if (enableInput) {
			shiharaiShubetsu = ichigenFlg ?  SHIHARAI_IRAI_SHUBETSU.SONOTA : SHIHARAI_IRAI_SHUBETSU.TEIKI;
		}
		
		//マネキン使用判定
		if (isNotEmpty(torihikisakiCd)) {
			GMap hojo = dMasterLogic.findTorihikisakiHojoU(torihikisakiCd);
			if (hojo != null && (int)hojo.get("dm2") == 1) {
				manekinFlg = true;
			}
		}
		if (! manekinFlg) {
			manekinGensen = "";
		}
	}

	/**
	 * 登録時に仕訳パターンマスターより借方貸方の値を設定する。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		String daihyouBumonCd = super.daihyouFutanBumonCd;
		
		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String shainCd = (String)usrInfo.get("shain_no");

		//掛けは全明細で統一されている前提なので、1つ目の明細でチェック
		ShiwakePatternMaster shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[0]));

		//明細行数分の領域確保
		int length = shiwakeEdaNo.length;
		kariTorihikisakiCd = new String[length];

		kashiTorihikisakiCd = new String[length];
		kashiFutanBumonCd = new String[length];
		kashiKamokuCd = new String[length];
		kashiKamokuEdabanCd = new String[length];
		kashiKazeiKbn = new String[length];
// kariUf1Cd = new String[length];
// kariUf2Cd = new String[length];
// kariUf3Cd = new String[length];
// kariUf4Cd = new String[length];
// kariUf5Cd = new String[length];
// kariUf6Cd = new String[length];
// kariUf7Cd = new String[length];
// kariUf8Cd = new String[length];
// kariUf9Cd = new String[length];
// kariUf10Cd = new String[length];

		kakeKashiTorihikisakiCd = new String[length];
		kakeKashiFutanBumonCd = new String[length];
		kakeKashiKamokuCd = new String[length];
		kakeKashiKamokuEdabanCd = new String[length];
		kakeKashiKazeiKbn = new String[length];
		tekiyouCd = new String[length];
		//this.bunriKbn = new String[length];
		//this.kariShiireKbn = new String[length];

		//明細１行ずつ
		for (int i = 0; i < length; i++) {
			shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[i]));
			
			if(csvUploadFlag) {
				//取引で貸借どちらかが任意入力でなければ取引の値で上書き
				String[] colNameCore = new String[]{ "project", "segment", "uf" };
				String[] constName = new String[]{ ShiwakeConst.PROJECT, ShiwakeConst.SEGMENT, ShiwakeConst.UF };
				var arrayList = new String[][] { this.projectCd, this.segmentCd, this.uf1Cd, this.uf2Cd, this.uf3Cd, this.uf4Cd, this.uf5Cd, this.uf6Cd, this.uf7Cd, this.uf8Cd, this.uf9Cd, this.uf10Cd };
				for(int j = 0; j < colNameCore.length + 9; j++)
				{
					int index = Math.min(colNameCore.length - 1, j);
					int ufNo = j - 1;
					String ufNoString = ufNo <= 0 ? "" : Integer.toString(ufNo);
					String codeCore = colNameCore[index] + ufNoString;
					GMap map = shiwakeP.map;
					var valueList = List.of( map.get( "kari_" + codeCore +"_cd"), map.get( "kashi_" + codeCore + "_cd1"), map.get( "kashi_" + codeCore + "_cd2"), map.get( "kashi_" + codeCore + "_cd3"), map.get( "kashi_" + codeCore + "_cd4"), map.get( "kashi_" + codeCore + "_cd5") );
					if(!valueList.contains(constName[index]))
					{
						arrayList[j][i] = (String)valueList.get(0);
					}
				}
			}

			//取引名
			torihikiName[i] = shiwakeP.torihikiName;

			//借方--------------------
			//借方　取引先 ※仕訳チェック用、DB登録には関係ない
			kariTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kariTorihikisakiCd) ? torihikisakiCd : shiwakeP.kariTorihikisakiCd;

			//借方　負担部門
			futanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kariFutanBumonCd, daihyouBumonCd);

			//借方　科目
			kamokuCd[i] = shiwakeP.kariKamokuCd;

			//借方　科目枝番
			String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
			switch (pKariKamokuEdabanCd) {
				case EteamConst.ShiwakeConst.EDABAN:
					//何もしない(画面入力のまま)
					break;
				default:
					//固定コード値 or ブランク
					kamokuEdabanCd[i] = pKariKamokuEdabanCd;
					break;
			}
			
			//借方　UF1-10
			if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.shainCdRenkeiFlg.equals(("1"))){
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
				{
					 uf1Cd[i]  = shainCd;
				}
				if (ufno == 2)
				{
					 uf2Cd[i]  = shainCd;
				}
				if (ufno == 3)
				{
					 uf3Cd[i]  = shainCd;
				}
				if (ufno == 4)
				{
					 uf4Cd[i]  = shainCd;
				}
				if (ufno == 5)
				{
					 uf5Cd[i]  = shainCd;
				}
				if (ufno == 6)
				{
					 uf6Cd[i]  = shainCd;
				}
				if (ufno == 7)
				{
					 uf7Cd[i]  = shainCd;
				}
				if (ufno == 8)
				{
					 uf8Cd[i]  = shainCd;
				}
				if (ufno == 9)
				{
					 uf9Cd[i]  = shainCd;
				}
				if (ufno == 10)
				{
					uf10Cd[i] = shainCd;
				}
			}
			
			// 借方　課税区分 支払い依頼CSVアップロード対応
			if (isEmpty(kazeiKbn[i]))
			{
				if(kamokuEdabanCd[i] == null || kamokuEdabanCd[i].equals("")) {
					kazeiKbn[i] = shiwakeP.kariKazeiKbn;
				}else {
					KamokuEdabanZandaka edaban = edabanZandaka.find(kamokuCd[i], kamokuEdabanCd[i]);
					kazeiKbn[i] = edaban.getKazeiKbn() == null ? shiwakeP.kariKazeiKbn : String.format("%3s", edaban.getKazeiKbn().toString()).replace(" ","0");
				}
			}
			
			// 借方　消費税率
			if (List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i])) {
				String[] convZeiritsu = commonLogic.convZeiritsu(zeiritsu[i], keigenZeiritsuKbn[i], shiwakeP.kariZeiritsu, shiwakeP.kariKeigenZeiritsuKbn);
				zeiritsu[i] = convZeiritsu[0];
				keigenZeiritsuKbn[i] = convZeiritsu[1];
			//CSV対応 税込系or税抜系以外の取引仕訳と選択した場合、CSVの内容に関係なく税率・消費税額を「0」セット
			}else if(csvUploadFlag){
				zeiritsu[i] = "0";
				keigenZeiritsuKbn[i] = "0";
				shouhizeigaku[i] = "0";
				//入力方式のデフォルト値に従って税抜金額・支払金額もセットし直し
				if(EteamSettingInfo.getSettingInfo("zei_default_A013").equals("0")) {
					zeinukiKingaku[i] = shiharaiKingaku[i];
				}else {
					shiharaiKingaku[i] = zeinukiKingaku[i];
				}
			}
			// 借方 分離区分・仕入区分　csv一括の場合は仕訳パターンマスタから取得する
			if (csvUploadFlag) {
				bunriKbn[i] = commonLogic.edabanBunriCheck(kamokuCd[i], kamokuEdabanCd[i], shiwakeP.kariBunriKbn, kazeiKbn[i]);
				kariShiireKbn[i] = shiwakeP.kariShiireKbn == null ? "": commonLogic.bumonShiireCheck(kamokuCd[i], futanBumonCd[i], kazeiKbn[i],shiwakeP.kariShiireKbn, this.getShiireZeiAnbun());
			}

			//貸方（未払）--------------------
			String kashiNo = (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) ? "2" : "3";

			//貸方　取引先 ※仕訳チェック用、DB登録には関係ない
			kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.map.get("kashi_torihikisaki_cd" + kashiNo)) ? torihikisakiCd : (String)shiwakeP.map.get("kashi_torihikisaki_cd" + kashiNo);

			//貸方　負担部門コード
			kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], (String)shiwakeP.map.get("kashi_futan_bumon_cd" + kashiNo), daihyouBumonCd);

			//貸方　科目コード
			kashiKamokuCd[i] = (String)shiwakeP.map.get("kashi_kamoku_cd" + kashiNo);

			//貸方　科目枝番コード（ブランク or コード値）
			kashiKamokuEdabanCd[i] = (String)shiwakeP.map.get("kashi_kamoku_edaban_cd" + kashiNo);

			//貸方　課税区分
			kashiKazeiKbn[i] = (String)shiwakeP.map.get("kashi_kazei_kbn" + kashiNo);

			//貸方（支払）--------------------
			//掛け　貸方　取引先
			kakeKashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kashiTorihikisakiCd1) ? torihikisakiCd : (String)shiwakeP.kashiTorihikisakiCd1;

			//掛け　貸方　負担部門コード
			kakeKashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kashiFutanBumonCd1, daihyouFutanBumonCd);

			//掛け　貸方　科目コード
			kakeKashiKamokuCd[i] = shiwakeP.kashiKamokuCd1;

			//掛け　貸方　科目枝番コード（ブランク or コード値）
			kakeKashiKamokuEdabanCd[i] = shiwakeP.kashiKamokuEdabanCd1;

			//掛け　貸方　課税区分
			kakeKashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn1;
			

			//社員コードを摘要コードに反映する場合--------------------
			if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
				if(shainCd.length() > 4) {
					tekiyouCd[i] = shainCd.substring(shainCd.length()-4);
				} else {
					tekiyouCd[i] = shainCd;
				}
			} else {
				tekiyouCd[i] = "";
			}
		}
	}

	/**
	 * DB登録前にマスターから名称を取得する。（クライアントから送られた名称は破棄）
	 * @param connection コネクション
	 */
	protected void reloadMaster(EteamConnection connection) {

		//申請内容の項目
		torihikisakiNameRyakushiki = masterLogic.findTorihikisakiName(torihikisakiCd);
		hf1Name = masterLogic.findHfName("1", hf1Cd);
		hf2Name = masterLogic.findHfName("2", hf2Cd);
		hf3Name = masterLogic.findHfName("3", hf3Cd);
		hf4Name = masterLogic.findHfName("4", hf4Cd);
		hf5Name = masterLogic.findHfName("5", hf5Cd);
		hf6Name = masterLogic.findHfName("6", hf6Cd);
		hf7Name = masterLogic.findHfName("7", hf7Cd);
		hf8Name = masterLogic.findHfName("8", hf8Cd);
		hf9Name = masterLogic.findHfName("9", hf9Cd);
		hf10Name = masterLogic.findHfName("10", hf10Cd);
		
		//とりあえず画面表示のない明細項目について、領域確保
		int length = shiwakeEdaNo.length;
		tokuisakiName = new String[length];

		//明細項目を１件ずつ変換
		for (int i = 0; i < length; i++) {
			futanBumonName[i] = masterLogic.findFutanBumonName(futanBumonCd[i]);
			kamokuName[i] = masterLogic.findKamokuNameStr(kamokuCd[i]);
			kamokuEdabanName[i] = masterLogic.findKamokuEdabanName(kamokuCd[i], kamokuEdabanCd[i]);
			projectName[i] = masterLogic.findProjectName(projectCd[i]);
			segmentName[i] = masterLogic.findSegmentName(segmentCd[i]);
			uf1Name[i] = masterLogic.findUfName("1", uf1Cd[i]);
			uf2Name[i] = masterLogic.findUfName("2", uf2Cd[i]);
			uf3Name[i] = masterLogic.findUfName("3", uf3Cd[i]);
			uf4Name[i] = masterLogic.findUfName("4", uf4Cd[i]);
			uf5Name[i] = masterLogic.findUfName("5", uf5Cd[i]);
			uf6Name[i] = masterLogic.findUfName("6", uf6Cd[i]);
			uf7Name[i] = masterLogic.findUfName("7", uf7Cd[i]);
			uf8Name[i] = masterLogic.findUfName("8", uf8Cd[i]);
			uf9Name[i] = masterLogic.findUfName("9", uf9Cd[i]);
			uf10Name[i] = masterLogic.findUfName("10", uf10Cd[i]);
		}
		
		//支払先
		reloadMasterShiharai(connection);
	}

	/**
	 * DB登録前にマスターから名称を取得する。（支払先関連のみ）
	 * @param connection コネクション
	 */
	protected void reloadMasterShiharai(EteamConnection connection) {
		KihyouNaviCategoryLogic kl = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

		//--------------------
		//振込(定期)の場合、取引先の支払先区分が「1：約定A」、「6：銀行振込」、「7：期日振込」以外だめ
		//--------------------
		if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)){
			if(!masterLogic.shiharaiTaishou(torihikisakiCd)){
				errorList.add(kl.findDenpyouKanri(DENPYOU_KBN.SIHARAIIRAI).get("denpyou_shubetsu") + "対象の" + ks.torihikisaki.getName() + "ではありません。");
			}
		}
		
		//--------------------
		//振込の場合振込先をマスターから取得。
		//--------------------
		boolean furikomisakiClear = false;
		
		//定期なら必ず、定期以外（その他か未入力）の場合は銀行コードが未入力なら取得する。定期以外で銀行コードが入力されていたらそれを活かす。
		if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI) || isEmpty(furikomiGinkouCd)) {
			if (isNotEmpty(torihikisakiCd)) {
				GMap furisaki = dMasterLogic.findFurikomisaki(torihikisakiCd);
				if (furisaki != null) {
					furikomiGinkouCd = (String)furisaki.get("furikomi_ginkou_cd");
					furikomiGinkouShitenCd = (String)furisaki.get("furikomi_ginkou_shiten_cd");
					kouzaShubetsu = (String)furisaki.get("yokin_shubetsu");
					kouzaBangou = (String)furisaki.get("kouza_bangou");
					kouzaMeiginin = (String)furisaki.get("kouza_meiginin_furigana");
					tesuuryou = furisaki.get("tesuuryou_futan_kbn").equals(2) ? "1" : "0";
				} else {
					if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) {
						errorList.add("振込先がマスターから取得できません。");
						furikomisakiClear = true;
					}
				}
			}
		}
		
		if (furikomisakiClear) {
			furikomiGinkouCd = "";
			furikomiGinkouShitenCd = "";
			kouzaShubetsu = "";
			kouzaBangou = "";
			kouzaMeiginin = "";
			tesuuryou = "";
		}

		//--------------------		
		//銀行・支店の名称
		//--------------------
		if (isNotEmpty(furikomiGinkouCd)) {
			String tmp = dMasterLogic.findKinyuukikanName(furikomiGinkouCd);
			if (isEmpty(tmp)) {
				errorList.add("銀行コードが不正です。");
			}
			furikomiGinkouName = tmp;
		} else {
			furikomiGinkouName = "";
		}
		if (isNotEmpty(furikomiGinkouCd) && isNotEmpty(furikomiGinkouShitenCd)) {
			String tmp = dMasterLogic.findKinyuukikanShitenName(furikomiGinkouCd, furikomiGinkouShitenCd);
			if (isEmpty(tmp)) {
				errorList.add("銀行コード、支店コードが不正です。");
			}
			furikomiGinkouShitenName = tmp;
		} else {
			furikomiGinkouShitenName = "";
		}

		//--------------------
		//支払日、支払期日をマスターから取る
		//支払予定日が未入力なら支払日にする
		//--------------------
		GMap furisaki = dMasterLogic.findFurikomisaki(torihikisakiCd);
		shiharaibi = "";
		shiharaiKijitsu = "";
		if (isNotEmpty(torihikisakiCd)) {
			Date shiharaiKijunbi = null;
			try {
				shiharaiKijunbi = toDate(keijoubi);
			} catch(Exception e){
				errorList.add("計上日が不正です。");
				return;
			}
			if (shiharaiKijunbi != null) {
				Date masterShiharaibi = dMasterLogic.judgeShiharaibi(torihikisakiCd, shiharaiKijunbi, 1, shiharaiHouhou, shiharaiShubetsu);
				if (masterShiharaibi != null) {
					shiharaibi = formatDate(masterShiharaibi);
					if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI) && furisaki != null && (furisaki.get("shiharai_shubetsu").toString().equals(SHIHARAI_SHUBETSU.YAKUTEI_A) || furisaki.get("shiharai_shubetsu").toString().equals(SHIHARAI_SHUBETSU.KIJITSU_FURIKOMI))) {
						Date masterShiharaikijitsu = dMasterLogic.judgeShiharaibi(torihikisakiCd, toDate(shiharaibi), 2, shiharaiHouhou, shiharaiShubetsu);
						if (masterShiharaikijitsu != null) {
							shiharaiKijitsu = formatDate(masterShiharaikijitsu);
						} else {
							errorList.add("支払期日がマスターから取得できません。");
						}
					}
				} else {
					if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI))
						errorList.add("支払日がマスターから取得できません。");
				}
			}
		}
		
		//振込(定期)の場合、支払予定日＝マスター支払日にする→クライアントで変更不可能
		//振込(その他)、自動引落でも、支払予定日未入力で顧客マスター取れていたら支払予定日=マスター支払日にする→クライアントで変更可能
		if (isEmpty(yoteibi) || shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) {
			yoteibi = shiharaibi;
		}
	}
	
	/**
	 * 伝票一覧からの計上日(+支払日・支払期限)更新にあたり外部から相関チェックを呼び出す。
	 * @param connection  コネクション
	 * @param chkKeijoubi 確認する計上日
	 */
	public void soukanCheckFromDenpyouIchiran(EteamConnection connection, String chkKeijoubi) {
		super.setConnection(connection);
		super.initialize();
		super.accessCheck(Event.KOUSHIN);
		super.loadData();

		this.initParts(connection);
		this.displaySeigyo(connection);
		this.reloadShiwakePattern(connection);
		
		this.setKeijoubi(chkKeijoubi);
		
		this.reloadMaster(connection);
		this.soukanCheck("1");
		return;
	}

	/**
	 * 支払日のチェック（何箇所かで同じチェックするのでメンテ漏れしないようにメソッド化。
	 * ※伝票一覧の承認チェックと同期とること！
	 * @param shiharai 支払日
	 * @param keijou   計上日
	 */
	protected void checkShiharaiBi(String shiharai, String keijou) {
// commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), toDate(keijou), "計上日", DENPYOU_KBN.SIHARAIIRAI, loginUserInfo, errorList);//振込方法はチェック飛ばす為のダミー
	}

	/**
	 * 領収書が必要かのチェックを行う。
	 * 画面からの入力で「領収書・請求書等」が「あり」の場合
	 * @return  領収書が必要か
	 */
	protected boolean isUseShouhyouShorui(){
		if(! csvUploadFlag){
			if (shouhyouShoruiFlg.equals(EteamConst.Domain.FLG[0]))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 逆仕訳可能かどうか判定
	 * 承認済・経理権限・逆仕訳未済・計上月内である→可能
	 * @param connection コネクション
	 * @return 逆仕訳可能
	 */
	protected boolean judgeGyakuShiwake(EteamConnection connection) {
		if (isEmpty(denpyouId))
		{
			return false;
		}
		WorkflowCategoryLogic wf = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		GMap denpyou = wf.selectDenpyou(denpyouId);
		ShiharaiIrai shiharai = this.shiharaiIraiDao.find(denpyouId);
		return 
			denpyou.get("denpyou_joutai").equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI) &&
			commonLogic.userIsKeiri(getUser()) &&
			shiharai.gyakuShiwakeFlg.equals("0") &&
			(DateFormatUtils.format(shiharai.keijoubi, "yyyyMM").compareTo(DateFormatUtils.format(new Date(System.currentTimeMillis()), "yyyyMM")) >= 0);
	}
	
	/**
	 * @return 支払依頼Dto
	 */
	protected ShiharaiIrai createDto()
	{
		ShiharaiIrai shiharaiIrai = new ShiharaiIrai();
		shiharaiIrai.denpyouId = this.denpyouId;
		shiharaiIrai.keijoubi = super.toDate(keijoubi);
		shiharaiIrai.yoteibi = super.toDate(yoteibi);
		shiharaiIrai.shiharaibi = super.toDate(shiharaibi);
		shiharaiIrai.shiharaiKijitsu = super.toDate(shiharaiKijitsu);
		shiharaiIrai.torihikisakiCd = this.torihikisakiCd;
		shiharaiIrai.torihikisakiNameRyakushiki = this.torihikisakiNameRyakushiki;
		shiharaiIrai.ichigensakiTorihikisakiName = this.ichigensakiTorihikisakiName;
		shiharaiIrai.edi = this.edi;
		shiharaiIrai.shiharaiGoukei = super.toDecimal(shiharaiGoukei);
		shiharaiIrai.sousaiGoukei = super.toDecimal(sousaiGoukei);
		shiharaiIrai.sashihikiShiharai = super.toDecimal(kingaku);
		shiharaiIrai.manekinGensen = super.toDecimal(manekinGensen) == null ? BigDecimal.ZERO : toDecimal(manekinGensen);
		shiharaiIrai.shouhyouShoruiFlg = this.shouhyouShoruiFlg;
		shiharaiIrai.hf1Cd = this.hf1Cd;
		shiharaiIrai.hf1NameRyakushiki = this.hf1Name;
		shiharaiIrai.hf2Cd = this.hf2Cd;
		shiharaiIrai.hf2NameRyakushiki = this.hf2Name;
		shiharaiIrai.hf3Cd = this.hf3Cd;
		shiharaiIrai.hf3NameRyakushiki = this.hf3Name;
		shiharaiIrai.hf4Cd = this.hf4Cd;
		shiharaiIrai.hf4NameRyakushiki = this.hf4Name;
		shiharaiIrai.hf5Cd = this.hf5Cd;
		shiharaiIrai.hf5NameRyakushiki = this.hf5Name;
		shiharaiIrai.hf6Cd = this.hf6Cd;
		shiharaiIrai.hf6NameRyakushiki = this.hf6Name;
		shiharaiIrai.hf7Cd = this.hf7Cd;
		shiharaiIrai.hf7NameRyakushiki = this.hf7Name;
		shiharaiIrai.hf8Cd = this.hf8Cd;
		shiharaiIrai.hf8NameRyakushiki = this.hf8Name;
		shiharaiIrai.hf9Cd = this.hf9Cd;
		shiharaiIrai.hf9NameRyakushiki = this.hf9Name;
		shiharaiIrai.hf10Cd = this.hf10Cd;
		shiharaiIrai.hf10NameRyakushiki = this.hf10Name;
		shiharaiIrai.shiharaiHouhou = this.shiharaiHouhou;
		shiharaiIrai.shiharaiShubetsu = this.shiharaiShubetsu;
		shiharaiIrai.furikomiGinkouCd = this.furikomiGinkouCd;
		shiharaiIrai.furikomiGinkouName = this.furikomiGinkouName;
		shiharaiIrai.furikomiGinkouShitenCd = this.furikomiGinkouShitenCd;
		shiharaiIrai.furikomiGinkouShitenName = this.furikomiGinkouShitenName;
		shiharaiIrai.yokinShubetsu = this.kouzaShubetsu == null ? "" : kouzaShubetsu;
		shiharaiIrai.kouzaBangou = this.kouzaBangou;
		shiharaiIrai.kouzaMeiginin = this.kouzaMeiginin;
		shiharaiIrai.tesuuryou = this.tesuuryou == null ? "" : tesuuryou;
		shiharaiIrai.hosoku = this.hosoku;
		shiharaiIrai.gyakuShiwakeFlg = "0";
		shiharaiIrai.shutsuryokuFlg = "0";
		shiharaiIrai.csvUploadFlg = "0";
		shiharaiIrai.hasseiShubetsu = "経費";
		shiharaiIrai.saimuMadeFlg = "0";
		shiharaiIrai.fbMadeFlg = "0";
		shiharaiIrai.tourokuUserId = this.gaibuKoushinUserId != null ? gaibuKoushinUserId : super.getUser().getTourokuOrKoushinUserId();
		shiharaiIrai.koushinUserId = this.gaibuKoushinUserId != null ? gaibuKoushinUserId : super.getUser().getTourokuOrKoushinUserId();
		shiharaiIrai.nyuryokuHoushiki = this.nyuryokuHoushiki;
		shiharaiIrai.jigyoushaKbn = isEmpty(this.jigyoushaKbn) ? "0" : this.jigyoushaKbn;
		shiharaiIrai.jigyoushaNo = isEmpty(this.jigyoushaNo) ? "" : this.jigyoushaNo;
		shiharaiIrai.invoiceDenpyou = this.invoiceDenpyou;
		return shiharaiIrai;
	}
	
	/**
	 * @param i 明細番号
	 * @return 支払依頼明細Dto
	 */
	protected ShiharaiIraiMeisai createMeisaiDto(int i)
	{
		ShiharaiIraiMeisai shiharaiIraiMeisai = new ShiharaiIraiMeisai();
		shiharaiIraiMeisai.denpyouId = this.denpyouId;
		shiharaiIraiMeisai.denpyouEdano = i + 1;
		shiharaiIraiMeisai.shiwakeEdano = Integer.parseInt(shiwakeEdaNo[i]);
		shiharaiIraiMeisai.torihikiName = this.torihikiName[i];
		shiharaiIraiMeisai.tekiyou = this.tekiyou[i];
		shiharaiIraiMeisai.shiharaiKingaku = super.toDecimal(shiharaiKingaku[i]);
		shiharaiIraiMeisai.kariFutanBumonCd = this.futanBumonCd[i];
		shiharaiIraiMeisai.kariFutanBumonName = this.futanBumonName[i];
		shiharaiIraiMeisai.kariKamokuCd = this.kamokuCd[i];
		shiharaiIraiMeisai.kariKamokuName = this.kamokuName[i];
		shiharaiIraiMeisai.kariKamokuEdabanCd = this.kamokuEdabanCd[i];
		shiharaiIraiMeisai.kariKamokuEdabanName = this.kamokuEdabanName[i];
		shiharaiIraiMeisai.kariKazeiKbn = this.kazeiKbn[i];
		shiharaiIraiMeisai.zeiritsu = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]) ? super.toDecimal(zeiritsu[i]) : BigDecimal.ZERO;
		shiharaiIraiMeisai.keigenZeiritsuKbn = super.isEmpty(keigenZeiritsuKbn[i]) ? "0" : this.keigenZeiritsuKbn[i];
		shiharaiIraiMeisai.uf1Cd = this.uf1Cd[i];
		shiharaiIraiMeisai.uf1NameRyakushiki = this.uf1Name[i];
		shiharaiIraiMeisai.uf2Cd = this.uf2Cd[i];
		shiharaiIraiMeisai.uf2NameRyakushiki = this.uf2Name[i];
		shiharaiIraiMeisai.uf3Cd = this.uf3Cd[i];
		shiharaiIraiMeisai.uf3NameRyakushiki = this.uf3Name[i];
		shiharaiIraiMeisai.uf4Cd = this.uf4Cd[i];
		shiharaiIraiMeisai.uf4NameRyakushiki = this.uf4Name[i];
		shiharaiIraiMeisai.uf5Cd = this.uf5Cd[i];
		shiharaiIraiMeisai.uf5NameRyakushiki = this.uf5Name[i];
		shiharaiIraiMeisai.uf6Cd = this.uf6Cd[i];
		shiharaiIraiMeisai.uf6NameRyakushiki = this.uf6Name[i];
		shiharaiIraiMeisai.uf7Cd = this.uf7Cd[i];
		shiharaiIraiMeisai.uf7NameRyakushiki = this.uf7Name[i];
		shiharaiIraiMeisai.uf8Cd = this.uf8Cd[i];
		shiharaiIraiMeisai.uf8NameRyakushiki = this.uf8Name[i];
		shiharaiIraiMeisai.uf9Cd = this.uf9Cd[i];
		shiharaiIraiMeisai.uf9NameRyakushiki = this.uf9Name[i];
		shiharaiIraiMeisai.uf10Cd = this.uf10Cd[i];
		shiharaiIraiMeisai.uf10NameRyakushiki = this.uf10Name[i];
		shiharaiIraiMeisai.projectCd = this.projectCd[i];
		shiharaiIraiMeisai.projectName = this.projectName[i];
		shiharaiIraiMeisai.segmentCd = this.segmentCd[i];
		shiharaiIraiMeisai.segmentNameRyakushiki = this.segmentName[i];
		shiharaiIraiMeisai.tekiyouCd = this.tekiyouCd[i];
		shiharaiIraiMeisai.tourokuUserId = this.gaibuKoushinUserId != null ? gaibuKoushinUserId : super.getUser().getTourokuOrKoushinUserId();
		shiharaiIraiMeisai.koushinUserId = this.gaibuKoushinUserId != null ? gaibuKoushinUserId : super.getUser().getTourokuOrKoushinUserId();
		shiharaiIraiMeisai.bunriKbn = this.bunriKbn[i];
		shiharaiIraiMeisai.kariShiireKbn = this.kariShiireKbn[i];
		shiharaiIraiMeisai.zeinukiKingaku = super.toDecimal(this.zeinukiKingaku[i]);
		shiharaiIraiMeisai.shouhizeigaku = super.toDecimalZeroIfEmpty(this.shouhizeigaku[i]);

		return shiharaiIraiMeisai;
	}
}
