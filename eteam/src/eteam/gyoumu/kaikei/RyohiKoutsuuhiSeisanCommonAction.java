package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamEkispertCommon;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.BumonMasterAbstractDao;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.KaribaraiAbstractDao;
import eteam.database.abstractdao.KeijoubiShimebiAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.BumonMasterDao;
import eteam.database.dao.KamokuEdabanZandakaDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KaribaraiDao;
import eteam.database.dao.KeijoubiShimebiDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.houjincard.HoujinCardLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 旅費交通費・精算系共通Action。invoice対応優先のため、仮払系の統合は後回し
 * @author j_matsumoto
 *
 */
@Getter @Setter @ToString
public class RyohiKoutsuuhiSeisanCommonAction extends EteamEkispertCommon {
	// 画面入力（申請内容）
	/** 目的 */
	String mokuteki;
	/** 支払方法 */
	String shiharaihouhou;
	/** 支払希望日 */
	String shiharaiKiboubi;
	/** 精算期間開始日 */
	String seisankikanFrom;
	/** 精算期間終了日 */
	String seisankikanTo;
	/** 精算期間開始時刻（時） */
	String seisankikanFromHour;
	/** 精算期間開始時刻（分） */
	String seisankikanFromMin;
	/** 精算期間終了時刻（時） */
	String seisankikanToHour;
	/** 精算期間終了時刻（分） */
	String seisankikanToMin;
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
	/** 消費税率 */
	String zeiritsuRyohi;
	/** 軽減税率区分 */
	String keigenZeiritsuKbnRyohi;
	/** 仕訳枝番号 */
	String shiwakeEdaNoRyohi;
	/** 取引名 */
	String torihikiNameRyohi;
	/** 勘定科目コード */
	String kamokuCdRyohi;
	/** 勘定科目名 */
	String kamokuNameRyohi;
	/** 勘定科目枝番コード */
	String kamokuEdabanCdRyohi;
	/** 勘定科目枝番名 */
	String kamokuEdabanNameRyohi;
	/** 負担部門コード */
	String futanBumonCdRyohi;
	/** 負担部門名 */
	String futanBumonNameRyohi;
	/** 取引先コード */
	String torihikisakiCdRyohi;
	/** 取引先名 */
	String torihikisakiNameRyohi;
	/** プロジェクトコード */
	String projectCdRyohi;
	/** プロジェクト名 */
	String projectNameRyohi;
	/** セグメントコード */
	String segmentCdRyohi;
	/** セグメント名 */
	String segmentNameRyohi;
	/** ユニバーサルフィールド１コード */
	String uf1CdRyohi;
	/** ユニバーサルフィールド１名 */
	String uf1NameRyohi;
	/** ユニバーサルフィールド２コード */
	String uf2CdRyohi;
	/** ユニバーサルフィールド２名 */
	String uf2NameRyohi;
	/** ユニバーサルフィールド３コード */
	String uf3CdRyohi;
	/** ユニバーサルフィールド３名 */
	String uf3NameRyohi;
	/** ユニバーサルフィールド４コード */
	String uf4CdRyohi;
	/** ユニバーサルフィールド４名 */
	String uf4NameRyohi;
	/** ユニバーサルフィールド５コード */
	String uf5CdRyohi;
	/** ユニバーサルフィールド５名 */
	String uf5NameRyohi;
	/** ユニバーサルフィールド６コード */
	String uf6CdRyohi;
	/** ユニバーサルフィールド６名 */
	String uf6NameRyohi;
	/** ユニバーサルフィールド７コード */
	String uf7CdRyohi;
	/** ユニバーサルフィールド７名 */
	String uf7NameRyohi;
	/** ユニバーサルフィールド８コード */
	String uf8CdRyohi;
	/** ユニバーサルフィールド８名 */
	String uf8NameRyohi;
	/** ユニバーサルフィールド９コード */
	String uf9CdRyohi;
	/** ユニバーサルフィールド９名 */
	String uf9NameRyohi;
	/** ユニバーサルフィールド１０コード */
	String uf10CdRyohi;
	/** ユニバーサルフィールド１０名 */
	String uf10NameRyohi;
	/** 課税区分 */
	String kazeiKbnRyohi;
	/** 摘要 */
	String tekiyouRyohi;
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 伝票摘要注記 */
	String chuuki2Ryohi;
	/** 支払日 */
	String shiharaibi;
	/** 計上日 */
	String keijoubi;
	/** 補足 */
	String hosoku;

	// 乗換案内エリア
	/** 乗換案内期間 */
	String norikaeKaishiBi;
	/** 乗換案内乗車区間 */
	String jousyaKukan;
	/** 乗換案内金額 */
	String norikaeKingaku;
	
	/** 分離区分 */
	String bunriKbn;
	/** 借方仕入区分 */
	String kariShiireKbn;
	/** 貸方仕入区分 */
	String kashiShiireKbn;
	/** 処理グループ */
	Integer shoriGroupRyohi;

	// 画面入力（明細
	/** 種別コード */
	String[] shubetsuCd;
	/** 種別１ */
	String[] shubetsu1;
	/** 種別２ */
	String[] shubetsu2;
	/** 期間開始日 */
	String[] kikanFrom;
	/** 期間終了日 */
	String[] kikanTo;
	/** 証憑書類必須フラグ */
	String[] shouhyouShoruiHissuFlg;
	/** 交通手段 */
	String[] koutsuuShudan;
	/** 領収書・請求書等フラグ */
	String[] ryoushuushoSeikyuushoTouFlg;
	/** 内容 */
	String[] naiyou;
	/** 備考 */
	String[] bikou;
	/** 往復フラグ */
	String[] oufukuFlg;
	/** 法人カード利用フラグ */
	String[] houjinCardFlgRyohi;
	/** 会社手配フラグ */
	String[] kaishaTehaiFlgRyohi;
	/** 自動入力フラグ */
	String[] jidounyuuryokuFlg;
	/** 日数 */
	String[] nissuu;
	/** 単価 */
	String[] tanka;
	/** 数量入力タイプ */
	String[] suuryouNyuryokuType;
	/** 数量 */
	String[] suuryou;
	/** 数量記号 */
	String[] suuryouKigou;
	/** 明細金額 */
	String[] meisaiKingaku;
	/** ICカード番号 */
	String[] icCardNo;
	/** ICカードシーケンス番号 */
	String[] icCardSequenceNo;
	/** 法人カード履歴紐付番号(旅費) */
	String[] himodukeCardMeisaiRyohi;
	
	//202210
	/** 早フラグ */
	String[] hayaFlg;
	/** 安フラグ */
	String[] yasuFlg;
	/** 楽フラグ */
	String[] rakuFlg;
	
	/** 支払先名（旅費） */
	String[] shiharaisakiNameRyohi;
	/** 事業者区分（旅費） */
	String[] jigyoushaKbnRyohi;
	/** 税抜金額（旅費） */
	String[] zeinukiKingakuRyohi;
	/** 消費税額（旅費） */
	String[] shouhizeigakuRyohi;
	
	/** 税額修正フラグ */
	String[] zeigakuFixFlg;
	
	// 画面入力以外
	// プルダウン等の候補値
	/** 消費税率DropDownList */
	List<Shouhizeiritsu> zeiritsuRyohiList;
	/** 消費税率ドメイン */
	String[] zeiritsuRyohiDomain;
	/** 課税区分DropDownList */
	List<NaibuCdSetting> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
	/** 支払方法のDropDownList */
	List<GMap> shiharaihouhouList;
	/** 支払方法ドメイン */
	String[] shiharaihouhouDomain;
	/** 交通手段のDropDownList */
	List<GMap> koutsuuShudanList;
	/** 交通手段ドメイン */
	String[] koutsuuShudanDomain;
	/** 数量入力タイプドメイン */
	String[] suuryouNyuryokuTypeDomain;
	/** 計上日の選択候補 */
	List<String> keijoubiList;
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
	
	/** 借方　UF1コード */
	String kariUf1CdRyohi;
	/** 借方　UF2コード */
	String kariUf2CdRyohi;
	/** 借方　UF3コード */
	String kariUf3CdRyohi;
	/** 借方　UF4コード */
	String kariUf4CdRyohi;
	/** 借方　UF5コード */
	String kariUf5CdRyohi;
	/** 借方　UF6コード */
	String kariUf6CdRyohi;
	/** 借方　UF7コード */
	String kariUf7CdRyohi;
	/** 借方　UF8コード */
	String kariUf8CdRyohi;
	/** 借方　UF9コード */
	String kariUf9CdRyohi;
	/** 借方　UF10コード */
	String kariUf10CdRyohi;
	/** 貸方負担部門コード */
	String kashiFutanBumonCdRyohi;
	/** 貸方負担部門名 */
	String kashiFutanBumonNameRyohi;
	/** 貸方科目コード */
	String kashiKamokuCdRyohi;
	/** 貸方科目名 */
	String kashiKamokuNameRyohi;
	/** 貸方科目枝番コード */
	String kashiKamokuEdabanCdRyohi;
	/** 貸方科目枝番名 */
	String kashiKamokuEdabanNameRyohi;
	/** 貸方課税区分 */
	String kashiKazeiKbnRyohi;

	/** 貸方1　UF1コード */
	String kashiUf1Cd1Ryohi;
	/** 貸方1　UF2コード */
	String kashiUf2Cd1Ryohi;
	/** 貸方1　UF3コード */
	String kashiUf3Cd1Ryohi;
	/** 貸方1　UF4コード */
	String kashiUf4Cd1Ryohi;
	/** 貸方1　UF5コード */
	String kashiUf5Cd1Ryohi;
	/** 貸方1　UF6コード */
	String kashiUf6Cd1Ryohi;
	/** 貸方1　UF7コード */
	String kashiUf7Cd1Ryohi;
	/** 貸方1　UF8コード */
	String kashiUf8Cd1Ryohi;
	/** 貸方1　UF9コード */
	String kashiUf9Cd1Ryohi;
	/** 貸方1　UF10コード */
	String kashiUf10Cd1Ryohi;
	/** 貸方2　UF1コード */
	String kashiUf1Cd2Ryohi;
	/** 貸方2　UF2コード */
	String kashiUf2Cd2Ryohi;
	/** 貸方2　UF3コード */
	String kashiUf3Cd2Ryohi;
	/** 貸方2　UF4コード */
	String kashiUf4Cd2Ryohi;
	/** 貸方2　UF5コード */
	String kashiUf5Cd2Ryohi;
	/** 貸方2　UF6コード */
	String kashiUf6Cd2Ryohi;
	/** 貸方2　UF7コード */
	String kashiUf7Cd2Ryohi;
	/** 貸方2　UF8コード */
	String kashiUf8Cd2Ryohi;
	/** 貸方2　UF9コード */
	String kashiUf9Cd2Ryohi;
	/** 貸方2　UF10コード */
	String kashiUf10Cd2Ryohi;
	/** 貸方3　UF1コード */
	String kashiUf1Cd3Ryohi;
	/** 貸方3　UF2コード */
	String kashiUf2Cd3Ryohi;
	/** 貸方3　UF3コード */
	String kashiUf3Cd3Ryohi;
	/** 貸方3　UF4コード */
	String kashiUf4Cd3Ryohi;
	/** 貸方3　UF5コード */
	String kashiUf5Cd3Ryohi;
	/** 貸方3　UF6コード */
	String kashiUf6Cd3Ryohi;
	/** 貸方3　UF7コード */
	String kashiUf7Cd3Ryohi;
	/** 貸方3　UF8コード */
	String kashiUf8Cd3Ryohi;
	/** 貸方3　UF9コード */
	String kashiUf9Cd3Ryohi;
	/** 貸方3　UF10コード */
	String kashiUf10Cd3Ryohi;
	/** 貸方4　UF1コード */
	String kashiUf1Cd4Ryohi;
	/** 貸方4　UF2コード */
	String kashiUf2Cd4Ryohi;
	/** 貸方4　UF3コード */
	String kashiUf3Cd4Ryohi;
	/** 貸方4　UF4コード */
	String kashiUf4Cd4Ryohi;
	/** 貸方4　UF5コード */
	String kashiUf5Cd4Ryohi;
	/** 貸方4　UF6コード */
	String kashiUf6Cd4Ryohi;
	/** 貸方4　UF7コード */
	String kashiUf7Cd4Ryohi;
	/** 貸方4　UF8コード */
	String kashiUf8Cd4Ryohi;
	/** 貸方4　UF9コード */
	String kashiUf9Cd4Ryohi;
	/** 貸方4　UF10コード */
	String kashiUf10Cd4Ryohi;
	/** 貸方5　UF1コード */
	String kashiUf1Cd5Ryohi;
	/** 貸方5　UF2コード */
	String kashiUf2Cd5Ryohi;
	/** 貸方5　UF3コード */
	String kashiUf3Cd5Ryohi;
	/** 貸方5　UF4コード */
	String kashiUf4Cd5Ryohi;
	/** 貸方5　UF5コード */
	String kashiUf5Cd5Ryohi;
	/** 貸方5　UF6コード */
	String kashiUf6Cd5Ryohi;
	/** 貸方5　UF7コード */
	String kashiUf7Cd5Ryohi;
	/** 貸方5　UF8コード */
	String kashiUf8Cd5Ryohi;
	/** 貸方5　UF9コード */
	String kashiUf9Cd5Ryohi;
	/** 貸方5　UF10コード */
	String kashiUf10Cd5Ryohi;
	/** 摘要コード */
	String tekiyouCdRyohi;

	// 画面制御情報	
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** プロジェクトコード表示 */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();

	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks;

	/** 入力モード */
	boolean enableInput;
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean kamokuEdabanEnableRyohi;
	/** 負担部門選択ボタン押下可否 */
	boolean futanBumonEnableRyohi;
	/** 取引先選択ボタン押下可否 */
	boolean torihikisakiEnableRyohi;
	/** プロジェクト選択ボタン */
	boolean projectEnableRyohi;
	/** セグメント選択ボタン */
	boolean segmentEnableRyohi;
	/** UF1ボタン押下可否 */
	boolean uf1EnableRyohi;
	/** UF2ボタン押下可否 */
	boolean uf2EnableRyohi;
	/** UF3ボタン押下可否 */
	boolean uf3EnableRyohi;
	/** UF4ボタン選択ボタン */
	boolean uf4EnableRyohi;
	/** UF5ボタン選択ボタン */
	boolean uf5EnableRyohi;
	/** UF6ボタン選択ボタン */
	boolean uf6EnableRyohi;
	/** UF7ボタン選択ボタン */
	boolean uf7EnableRyohi;
	/** UF8ボタン選択ボタン */
	boolean uf8EnableRyohi;
	/** UF9ボタン選択ボタン */
	boolean uf9EnableRyohi;
	/** UF10ボタン選択ボタン */
	boolean uf10EnableRyohi;
	/** ユーザーに対する仮払案件有無 */
	String userKaribaraiUmuFlg;
	/** 計上日表示モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン)) */
	int keijouBiMode;
	/** 支払日登録可能。0:非表示、1:入力可、2:表示 */
	int shiharaiBiMode;
	/** 駅すぱあと連携可否 */
	boolean ekispertEnable;
	/** ICカード履歴選択可否 */
	boolean icCardEnable;
	/** 支払方法モード */
	boolean disableShiharaiHouhou;
	/** 発生主義かどうか */
	boolean hasseiShugi;
	/** 計上日デフォルト設定 */
	String keijoubiDefaultSettei;
	/** 日当等を表示するか */
	boolean enableNittou;
	/** 法人カード利用表示制御 */
	boolean houjinCardFlag;
	/** 会社手配表示制御 */
	boolean kaishaTehaiFlag;
	/** 計上日 締日制限 */
	boolean keijoubiSeigen;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean enableBumonSecurity;
	/** 使用者法人カード利用可否 */
	boolean houjinCardRirekiEnable;
	
	// ＜部品＞
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** ワークフローイベント */
	WorkflowEventControlLogic wfEventLogic;
	/** 法人カード使用履歴 */
	HoujinCardLogic hcLogic;
	/** WF */
	WorkflowCategoryLogic wfLogic;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 計上日・締日Dao */
	KeijoubiShimebiAbstractDao keijoubiShimebiDao;
	/** 税率Dao */
	ShouhizeiritsuAbstractDao zeiritsuDao;
	/** 仮払いDao */
	KaribaraiAbstractDao karibaraiDao;
	/** 科目マスタDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** 科目枝番残高Dao */
	KamokuEdabanZandakaAbstractDao kamokuEdabanZandakaDao;
	/** 部門マスタDao */
	BumonMasterAbstractDao bumonMasterDao;
	
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
		naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		keijoubiShimebiDao = EteamContainer.getComponent(KeijoubiShimebiDao.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		karibaraiDao = EteamContainer.getComponent(KaribaraiDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		kamokuEdabanZandakaDao = EteamContainer.getComponent(KamokuEdabanZandakaDao.class, connection);
		bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);
	}

	
	/**
	 * 国内伝票の仕訳をチェックする。
	 * @param i 貸方いくつ？（0なら借方）
	 * @param shiwakePattern 仕訳パターン
	 * @param denpyouKbnTmp 伝票区分
	 * @param daihyouBumonCd 代表部門コード
	 */
	protected void checkShiwakeKashi(int i, ShiwakePatternMaster shiwakePattern, String denpyouKbnTmp, String daihyouBumonCd)
	{
		this.checkShiwakeKashi(i, shiwakePattern.map, denpyouKbnTmp, daihyouBumonCd);
	}
	
	/**
	 * 国内伝票の仕訳をチェックする。
	 * @param i 貸方いくつ？（0なら借方）
	 * @param shiwakePattern 仕訳パターン
	 * @param denpyouKbnTmp 伝票区分
	 * @param daihyouBumonCd 代表部門コード
	 */
	protected void checkShiwakeKashi(int i, GMap shiwakePattern, String denpyouKbnTmp, String daihyouBumonCd)
	{
		boolean isKari = i == 0;
		String prefixJa = isKari ? "" : "貸方";
		String prefixEn = isKari ? "kari_" : "kashi_";
		String suffix = isKari ? "" : String.valueOf(i);
		ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData();
		shiwakeCheckDataKashi.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
		shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNoRyohi;
		// 原因不明だが、貸方3～5では科目・枝番・部門・課税区分はチェックされていない
		if(i <= 2)
		{
			shiwakeCheckDataKashi.kamokuNm = prefixJa + ks.kamoku.getName() + "コード";
			shiwakeCheckDataKashi.kamokuCd = isKari ? kamokuCdRyohi : kashiKamokuCdRyohi;
			shiwakeCheckDataKashi.kamokuEdabanNm = prefixJa + ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKashi.kamokuEdabanCd = isKari ? kamokuEdabanCdRyohi : kashiKamokuEdabanCdRyohi;
			shiwakeCheckDataKashi.futanBumonNm = prefixJa + ks.futanBumon.getName() + "コード";
			shiwakeCheckDataKashi.futanBumonCd = isKari ? futanBumonCdRyohi : kashiFutanBumonCdRyohi;
			shiwakeCheckDataKashi.kazeiKbnNm = (isKari ? "借方" : "貸方") + "課税区分";
			shiwakeCheckDataKashi.kazeiKbn = isKari ? kazeiKbnRyohi : kashiKazeiKbnRyohi;
		}
		shiwakeCheckDataKashi.torihikisakiNm = prefixJa + ks.torihikisaki.getName() + "コード";
		shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get(prefixEn + "torihikisaki_cd" + suffix)) ? torihikisakiCdRyohi : (String)shiwakePattern.get(prefixEn + "torihikisaki_cd" + suffix);
		shiwakeCheckDataKashi.projectNm =  prefixJa + ks.project.getName() + "コード";
		if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get(prefixEn + "project_cd" + suffix)))
		{
			shiwakeCheckDataKashi.projectCd = projectCdRyohi;
		}
		shiwakeCheckDataKashi.segmentNm =  prefixJa + ks.segment.getName() + "コード";
		if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get(prefixEn + "segment_cd" + suffix)))
		{
			shiwakeCheckDataKashi.segmentCd = segmentCdRyohi;
		}
		shiwakeCheckDataKashi.uf1Nm = prefixJa + hfUfSeigyo.getUf1Name();
		shiwakeCheckDataKashi.uf2Nm = prefixJa + hfUfSeigyo.getUf2Name();
		shiwakeCheckDataKashi.uf3Nm = prefixJa + hfUfSeigyo.getUf3Name();
		shiwakeCheckDataKashi.uf4Nm = prefixJa + hfUfSeigyo.getUf4Name();
		shiwakeCheckDataKashi.uf5Nm = prefixJa + hfUfSeigyo.getUf5Name();
		shiwakeCheckDataKashi.uf6Nm = prefixJa + hfUfSeigyo.getUf6Name();
		shiwakeCheckDataKashi.uf7Nm = prefixJa + hfUfSeigyo.getUf7Name();
		shiwakeCheckDataKashi.uf8Nm = prefixJa + hfUfSeigyo.getUf8Name();
		shiwakeCheckDataKashi.uf9Nm = prefixJa + hfUfSeigyo.getUf9Name();
		shiwakeCheckDataKashi.uf10Nm = prefixJa + hfUfSeigyo.getUf10Name();
		String[][] ufCdRyohiArrays =
			{
				{ kariUf1CdRyohi, kariUf2CdRyohi, kariUf3CdRyohi, kariUf4CdRyohi, kariUf5CdRyohi, kariUf6CdRyohi, kariUf7CdRyohi, kariUf8CdRyohi, kariUf9CdRyohi, kariUf10CdRyohi },
				{ kashiUf1Cd1Ryohi, kashiUf2Cd1Ryohi, kashiUf3Cd1Ryohi, kashiUf4Cd1Ryohi, kashiUf5Cd1Ryohi, kashiUf6Cd1Ryohi, kashiUf7Cd1Ryohi, kashiUf8Cd1Ryohi, kashiUf9Cd1Ryohi, kashiUf10Cd1Ryohi },
				{ kashiUf1Cd2Ryohi, kashiUf2Cd2Ryohi, kashiUf3Cd2Ryohi, kashiUf4Cd2Ryohi, kashiUf5Cd2Ryohi, kashiUf6Cd2Ryohi, kashiUf7Cd2Ryohi, kashiUf8Cd2Ryohi, kashiUf9Cd2Ryohi, kashiUf10Cd2Ryohi },
				{ kashiUf1Cd3Ryohi, kashiUf2Cd3Ryohi, kashiUf3Cd3Ryohi, kashiUf4Cd3Ryohi, kashiUf5Cd3Ryohi, kashiUf6Cd3Ryohi, kashiUf7Cd3Ryohi, kashiUf8Cd3Ryohi, kashiUf9Cd3Ryohi, kashiUf10Cd3Ryohi },
				{ kashiUf1Cd4Ryohi, kashiUf2Cd4Ryohi, kashiUf3Cd4Ryohi, kashiUf4Cd4Ryohi, kashiUf5Cd4Ryohi, kashiUf6Cd4Ryohi, kashiUf7Cd4Ryohi, kashiUf8Cd4Ryohi, kashiUf9Cd4Ryohi, kashiUf10Cd4Ryohi },
				{ kashiUf1Cd5Ryohi, kashiUf2Cd5Ryohi, kashiUf3Cd5Ryohi, kashiUf4Cd5Ryohi, kashiUf5Cd5Ryohi, kashiUf6Cd5Ryohi, kashiUf7Cd5Ryohi, kashiUf8Cd5Ryohi, kashiUf9Cd5Ryohi, kashiUf10Cd5Ryohi }
			};
		
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf1_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf1Cd = ufCdRyohiArrays[i][0];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf2_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf2Cd = ufCdRyohiArrays[i][1];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf3_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf3Cd = ufCdRyohiArrays[i][2];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf4_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf4Cd = ufCdRyohiArrays[i][3];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf5_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf5Cd = ufCdRyohiArrays[i][4];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf6_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf6Cd = ufCdRyohiArrays[i][5];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf7_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf7Cd = ufCdRyohiArrays[i][6];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf8_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf8Cd = ufCdRyohiArrays[i][7];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf9_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf9Cd = ufCdRyohiArrays[i][8];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf10_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf10Cd = ufCdRyohiArrays[i][9];
		}
		String[] kbnArray = { SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5 };
		commonLogic.checkShiwake(denpyouKbnTmp, kbnArray[i], shiwakeCheckDataKashi, daihyouBumonCd, errorList);
	}

	//個別業務の申請時の処理を実装。
	//エラーが有る場合は「errorList」にエラーメッセージを追加する。
	@Override
	protected void shinseiKobetsuDenpyou (EteamConnection connection) {
		initParts(connection);

		//表示制御（エラー発生時用）
		displaySeigyo(connection);
	}

	/** 
	 * （要共通化）画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。このクラスでは空で、具体的な実装は親クラスで行う。
	 * @param connection connection
	 */
	protected void displaySeigyo(EteamConnection connection) {}
	/**
	 * 基準日の取得
	 * @return 基準日
	 */
	@Override
	public String judgeKijunbi() {
		if (keijoubiDefaultSettei.equals("3")) {
			return keijoubi;
		} else {
			return super.judgeKijunbi();
		}
	}

	/**
	 * 未登録の時、デフォルトの表示状態に項目セットする。
	 * @param connection コネクション
	 */
	protected void makeDefaultShinsei(EteamConnection connection) {
		zeiritsuRyohi = masterLogic.findValidShouhizeiritsuExclusionKeigen().toString();
	}
}
