package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
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
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.KeihiseisanAbstractDao;
import eteam.database.abstractdao.KeihiseisanMeisaiAbstractDao;
import eteam.database.abstractdao.KeijoubiShimebiAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KaribaraiDao;
import eteam.database.dao.KeihiseisanDao;
import eteam.database.dao.KeihiseisanMeisaiDao;
import eteam.database.dao.KeijoubiShimebiDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.KamokuMaster;
import eteam.database.dto.Karibarai;
import eteam.database.dto.Keihiseisan;
import eteam.database.dto.KeihiseisanMeisai;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.houjincard.HoujinCardLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 経費立替精算Action
 */
@Getter @Setter @ToString
public class KeihiTatekaeSeisanAction extends WorkflowEventControl {

//＜定数＞

//＜画面入力＞
	/** 仮払伝票ID */
	String karibaraiDenpyouId;
	/** 仮払あり・なし */
	String karibaraiOn;
	/** 仮払摘要 */
	String karibaraiTekiyou;
	/** 申請金額 */
	String karibaraiShinseiKingaku;
	/** 仮払金額 */
	String karibaraiKingaku;
	/** 仮払金額差額 */
	String karibaraiKingakuSagaku;
	/** 仮払未使用フラグ */
	String karibaraiMishiyouFlg;
	/** 支払方法*/
	String shiharaihouhou;
	/** 支払希望日 */
	String shiharaiKiboubi;
	/** 本体金額合計 */
	String hontaiKingakuGoukei;
	/** 消費税額合計 */
	String shouhizeigakuGoukei;
	/** 差引支給金額 */
	String sashihikiShikyuuKingaku;
	/** 支払日 */
	String shiharaibi;
	/** 計上日 */
	String keijoubi;
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 共通部分交際費注記 */
	String chuukiKousai1;
	// 支払金額合計は親クラスで定義しているkingakuを使用する
	/** 内法人カード利用合計 */
	String houjinCardRiyouGoukei;
	/** 会社手配合計 */
	String kaishaTehaiGoukei;
	/** 補足 */
	String hosoku;

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
	
	/** インボイス伝票 */
	//String invoiceDenpyou;

	/** 使用者ID */
	String[] userId;
	/** 使用者名 */
	String[] userName;
	/** 使用者社員番号 */
	String[] shainNo;
	/** 仕訳枝番号 */
	String[] shiwakeEdaNo;
	/** 取引名 */
	String[] torihikiName;
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
	/** 取引先コード */
	String[] torihikisakiCd;
	/** 取引先名 */
	String[] torihikisakiName;
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
	/** 使用日 */
	String[] shiyoubi;
	/** 証憑 */
	String[] shouhyouShorui;
	/** 支払金額 */
	String[] shiharaiKingaku;
	/** 本体金額 */
	String[] hontaiKingaku;
	/** 消費税額 */
	String[] shouhizeigaku;
	/** 法人カード利用フラグ */
	String[] houjinCardFlgKeihi;
	/** 会社手配フラグ */
	String[] kaishaTehaiFlgKeihi;
	/** 摘要 */
	String[] tekiyou;
	/** 伝票摘要注記 */
	String[] chuuki2;
	/** 伝票交際費注記 */
	String[] chuukiKousai2;
	/** 交際費表示フラグ */
	String[] kousaihiHyoujiFlg;
	/** 人数項目表示フラグ */
	String[] ninzuuRiyouFlg;
	/** 消交際費詳細 */
	String[] kousaihiShousai;
	/** 交際費人数 */
	String[] kousaihiNinzuu;
	/** 交際費一人あたり金額 */
	String[] kousaihiHitoriKingaku;
	/** 法人カード履歴紐付番号(経費) */
	String[] himodukeCardMeisaiKeihi;
	
	/** 事業者区分 */
	String[] jigyoushaKbn;
	/** 支払先名 */
	String[] shiharaisakiName;
	/** 分離区分 */
	String[] bunriKbn;
	/** 借方仕入区分 */
	String[] kariShiireKbn;
	
	/** 処理グループ */
	String[] shoriGroup;

//＜画面入力以外＞

	//プルダウン等の候補値
	/** 消費税率DropDownList */
	List<GMap> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<GMap> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
	/** 分離区分リスト */
	List<GMap> bunriKbnList;
	/** 分離区分ドメイン */
	String[] bunriKbnDomain;
	/** 仕入区分リスト */
	List<GMap> shiireKbnList;
	/** 仕入区分ドメイン */
	String[] shiireKbnDomain;
	/** 支払方法のDropDownList */
	List<GMap> shiharaihouhouList;
	/** 支払方法ドメイン */
	String[] shiharaihouhouDomain;
	/** インボイス対応伝票DropDownList */
	List<NaibuCdSetting> invoiceDenpyouList;
	/** インボイス対応伝票ドメイン */
	String[] invoiceDenpyouDomain;
	/** 領収書・請求書等デフォルト値 */
	String ryoushuushoSeikyuushoDefault = setting.shouhyouShoruiDefaultA001();
	/** 計上日の選択候補 */
	List<String> keijoubiList;
	/** 事業者区分List */
	List<GMap> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;
	/**	税込or税抜ならtrue */
	boolean[] kazeiKbnCheck;

	//法人カード履歴選択ダイアログ用
	/** 起票者ユーザーID */
	String kihyoushaUserId;
	/** 起票者ユーザー名 */
	String kihyoushaUserName;
	/** 起票者社員No */
	String kihyoushaShainNo;

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();

	/**  画面項目制御クラス(HeaderField.jsp用) */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	/**  画面項目制御クラス(仮払) */
	GamenKoumokuSeigyo ksKari = new GamenKoumokuSeigyo(DENPYOU_KBN.KARIBARAI_SHINSEI);

	/** 入力モード */
	boolean enableInput;
	/** ユーザーに対する仮払案件有無 */
	String userKaribaraiUmuFlg;
	/** 計上日表示モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン)) */
	int keijouBiMode;
	/** 支払日表示モード */
	int shiharaiBiMode;
	/** 支払方法モード */
	boolean disableShiharaiHouhou;
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** 発生主義かどうか */
	boolean hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA001());
	/** 計上日デフォルト設定 */
	String keijoubiDefaultSettei = setting.keijoubiDefaultA001();
	/** 仮払画面が有効であるかどうか */
	boolean karibaraiIsEnabled = false;
	/** 法人カード利用表示制御 */
	boolean houjinCardFlag;
	/** 会社手配表示制御 */
	boolean kaishaTehaiFlag;
	/** 計上日 締日制限 */
	boolean keijoubiSeigen = setting.keihiseisanKeijouSeigen().equals("1");
	/** 仮払申請の起案添付済みフラグ */
	String karibaraiKianTenpuZumiFlg;

	/** 代理起票フラグ（パラメータ）*/
	String dairiFlg = "0";
	/** 全明細表示 */
	boolean allMeisaiView;


	//明細単位制御情報
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean[] kamokuEdabanEnable;
	/** 負担部門選択ボタン */
	boolean[] futanBumonEnable;
	/** 取引先選択ボタン */
	boolean[] torihikisakiEnable;
	/** プロジェクト選択ボタン */
	boolean[] projectEnable;
	/** セグメント選択ボタン */
	boolean[] segmentEnable;
	/** 交際費表示 */
	boolean[] kousaihiEnable;
	/** 人数項目表示 */
	boolean[] ninzuuEnable;
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
	/** 使用者法人カード利用可否 */
	boolean houjinCardRirekiEnable;

	//明細登録時の領域
	/** 借方取引先コード */
	String[] kariTorihikisakiCd;
	/** 借方　UF1コード */
	String[] kariUf1Cd;
	/** 借方　UF2コード */
	String[] kariUf2Cd;
	/** 借方　UF3コード */
	String[] kariUf3Cd;
	/** 借方　UF4コード */
	String[] kariUf4Cd;
	/** 借方　UF5コード */
	String[] kariUf5Cd;
	/** 借方　UF6コード */
	String[] kariUf6Cd;
	/** 借方　UF7コード */
	String[] kariUf7Cd;
	/** 借方　UF8コード */
	String[] kariUf8Cd;
	/** 借方　UF9コード */
	String[] kariUf9Cd;
	/** 借方　UF10コード */
	String[] kariUf10Cd;

	/** 貸方取引先コード */
	String[] kashiTorihikisakiCd;
	/** 貸方負担部門コード */
	String[] kashiFutanBumonCd;
	/** 貸方負担部門名 */
	String[] kashiFutanBumonName;
	/** 貸方科目コード */
	String[] kashiKamokuCd;
	/** 貸方科目名 */
	String[] kashiKamokuName;
	/** 貸方科目枝番コード */
	String[] kashiKamokuEdabanCd;
	/** 貸方科目枝番名 */
	String[] kashiKamokuEdabanName;
	/** 貸方課税区分 */
	String[] kashiKazeiKbn;
	/** 貸方1　UF1コード */
	String[] kashiUf1Cd1;
	/** 貸方1　UF2コード */
	String[] kashiUf2Cd1;
	/** 貸方1　UF3コード */
	String[] kashiUf3Cd1;
	/** 貸方1　UF4コード */
	String[] kashiUf4Cd1;
	/** 貸方1　UF5コード */
	String[] kashiUf5Cd1;
	/** 貸方1　UF6コード */
	String[] kashiUf6Cd1;
	/** 貸方1　UF7コード */
	String[] kashiUf7Cd1;
	/** 貸方1　UF8コード */
	String[] kashiUf8Cd1;
	/** 貸方1　UF9コード */
	String[] kashiUf9Cd1;
	/** 貸方1　UF10コード */
	String[] kashiUf10Cd1;
	/** 貸方2　UF1コード */
	String[] kashiUf1Cd2;
	/** 貸方2　UF2コード */
	String[] kashiUf2Cd2;
	/** 貸方2　UF3コード */
	String[] kashiUf3Cd2;
	/** 貸方2　UF4コード */
	String[] kashiUf4Cd2;
	/** 貸方2　UF5コード */
	String[] kashiUf5Cd2;
	/** 貸方2　UF6コード */
	String[] kashiUf6Cd2;
	/** 貸方2　UF7コード */
	String[] kashiUf7Cd2;
	/** 貸方2　UF8コード */
	String[] kashiUf8Cd2;
	/** 貸方2　UF9コード */
	String[] kashiUf9Cd2;
	/** 貸方2　UF10コード */
	String[] kashiUf10Cd2;
	/** 貸方3　UF1コード */
	String[] kashiUf1Cd3;
	/** 貸方3　UF2コード */
	String[] kashiUf2Cd3;
	/** 貸方3　UF3コード */
	String[] kashiUf3Cd3;
	/** 貸方3　UF4コード */
	String[] kashiUf4Cd3;
	/** 貸方3　UF5コード */
	String[] kashiUf5Cd3;
	/** 貸方3　UF6コード */
	String[] kashiUf6Cd3;
	/** 貸方3　UF7コード */
	String[] kashiUf7Cd3;
	/** 貸方3　UF8コード */
	String[] kashiUf8Cd3;
	/** 貸方3　UF9コード */
	String[] kashiUf9Cd3;
	/** 貸方3　UF10コード */
	String[] kashiUf10Cd3;
	/** 貸方4　UF1コード */
	String[] kashiUf1Cd4;
	/** 貸方4　UF2コード */
	String[] kashiUf2Cd4;
	/** 貸方4　UF3コード */
	String[] kashiUf3Cd4;
	/** 貸方4　UF4コード */
	String[] kashiUf4Cd4;
	/** 貸方4　UF5コード */
	String[] kashiUf5Cd4;
	/** 貸方4　UF6コード */
	String[] kashiUf6Cd4;
	/** 貸方4　UF7コード */
	String[] kashiUf7Cd4;
	/** 貸方4　UF8コード */
	String[] kashiUf8Cd4;
	/** 貸方4　UF9コード */
	String[] kashiUf9Cd4;
	/** 貸方4　UF10コード */
	String[] kashiUf10Cd4;
	/** 貸方5　UF1コード */
	String[] kashiUf1Cd5;
	/** 貸方5　UF2コード */
	String[] kashiUf2Cd5;
	/** 貸方5　UF3コード */
	String[] kashiUf3Cd5;
	/** 貸方5　UF4コード */
	String[] kashiUf4Cd5;
	/** 貸方5　UF5コード */
	String[] kashiUf5Cd5;
	/** 貸方5　UF6コード */
	String[] kashiUf6Cd5;
	/** 貸方5　UF7コード */
	String[] kashiUf7Cd5;
	/** 貸方5　UF8コード */
	String[] kashiUf8Cd5;
	/** 貸方5　UF9コード */
	String[] kashiUf9Cd5;
	/** 貸方5　UF10コード */
	String[] kashiUf10Cd5;
	
	/** 貸方仕入区分 */
	String[] kashiShiireKbn;

	/** 摘要コード */
	String[] tekiyouCd;

//＜部品＞
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 経費精算ロジック */
	KeihiTatekaeSeisanLogic keihiTatekaeLogic;
	/** 経費精算DAO */
	KeihiseisanAbstractDao keihiseisanDao;
	/** 経費精算明細DAO */
	KeihiseisanMeisaiAbstractDao keihiseisanMeisaiDao;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスター管理ロジック */
	MasterKanriCategoryLogic masterLogic;
	/** 会計カテゴリロジック */
	KaikeiCategoryLogic kaikeiLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** ワークフローイベント */
	WorkflowEventControlLogic wfEventLogic;
	/** WF */
	WorkflowCategoryLogic wfLogic;
	/** 起票ナビカテゴリー */
	KihyouNaviCategoryLogic kihyouLogic;
	/** 部門ユーザーカテゴリー */
	BumonUserKanriCategoryLogic buLogic;
	/** バッチ会計ロジック */
	BatchKaikeiCommonLogic batchkaikeilogic;
	/** 法人カード使用履歴 */
	HoujinCardLogic hcLogic;
	/** 内部コード設定Dao */
	NaibuCdSettingDao naibuCdSettingDao;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 計上日・締日Dao */
	KeijoubiShimebiAbstractDao keijoubiShimebiDao;
	/** 税率Dao */
	ShouhizeiritsuAbstractDao zeiritsuDao;
	/** 仮払いDao */
	KaribaraiDao karibaraiDao;
	/** 科目マスタDao */
	KamokuMasterDao kamokuMasterDao;

//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		// 項目										//項目名											//キー項目？
		checkString (karibaraiDenpyouId, 1, 19, ks1.karibaraiDenpyouId.getName(), false);
		checkDomain (karibaraiOn, Domain.FLG, ks1.karibaraiOn.getName(), false);
		checkString (karibaraiTekiyou, 1, this.getIntTekiyouMaxLength(), ks1.karibaraiTekiyou.getName(), false);
		checkKingakuOver0 (karibaraiShinseiKingaku, ks1.shinseiKingaku.getName(), false);
		checkCheckbox (karibaraiMishiyouFlg,       				ks1.karibaraiMishiyouFlg.getName(),   			false);
		checkDomain (shiharaihouhou, shiharaihouhouDomain, ks1.shiharaiHouhou.getName(), false);
		checkDate (shiharaiKiboubi, ks1.shiharaiKiboubi.getName(), false);
		checkKingakuOver0 (hontaiKingakuGoukei, "本体金額合計", false);
		checkKingakuOver0 (shouhizeigakuGoukei, "消費税額合計", false);
		checkKingakuOver0 (houjinCardRiyouGoukei, ks1.uchiHoujinCardRiyouGoukei.getName(), false);
		checkKingakuOver0 (kaishaTehaiGoukei, ks1.kaishaTehaiGoukei.getName(), false);
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
		checkDomain (invoiceDenpyou, invoiceDenpyouDomain, "インボイス対応伝票", false);

		if (keihiEntryFlg) {
			checkKingakuOver1 (kingaku, ks1.shiharaiKingakuGoukei.getName(), false);
		} else {
			checkKingakuOver0 (kingaku, ks1.shiharaiKingakuGoukei.getName(), false);
		}

		checkDate (shiharaibi, "支払日", false);
		checkDate (keijoubi, "計上日", false);
		checkString (hosoku, 1, 240, ks1.hosoku.getName(), false);

		if (keihiEntryFlg) {

			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				int ip = i + 1;
				checkDate (shiyoubi[i], ks1.shiyoubi.getName() + "："					+ ip + "行目", false);
				checkDomain (shouhyouShorui[i],EteamConst.Domain.FLG, ks1.shouhyouShoruiFlg.getName() + "："			+ ip + "行目", false);
				//姓名の間に空白が入るので21文字
				checkString (userName[i], 1, 21, ks1.userName.getName() + "名："				+ ip + "行目", false);
				checkString (shainNo[i], 1, 15, ks1.userName.getName() + "社員番号："			+ ip + "行目", false);
				checkNumber (shiwakeEdaNo[i], 1, 10, ks1.torihiki.getName() + "コード："			+ ip + "行目", false);
				checkString (torihikiName[i], 1, 20, ks1.torihiki.getName() + "名："				+ ip + "行目", false);
				checkString (kamokuCd[i], 1, 6, ks1.kamoku.getName() + "コード："				+ ip + "行目", false);
				checkString (kamokuName[i], 1, 22, ks1.kamoku.getName() + "名："					+ ip + "行目", false);
				checkString (kamokuEdabanCd[i], 1, 12, ks1.kamokuEdaban.getName() + "コード："		+ ip + "行目", false);
				checkString (kamokuEdabanName[i], 1, 20, ks1.kamokuEdaban.getName() + "名："			+ ip + "行目", false);
				checkString (futanBumonCd[i], 1, 8, ks1.futanBumon.getName() + "コード："			+ ip + "行目", false);
				checkString (futanBumonName[i], 1, 20, ks1.futanBumon.getName() + "名："				+ ip + "行目", false);
				checkString (torihikisakiCd[i], 1, 12, ks1.torihikisaki.getName() + "コード："		+ ip + "行目", false);
				checkString (torihikisakiName[i], 1, 20, ks1.torihikisaki.getName() + "名："			+ ip + "行目", false);
				checkString (projectCd[i], 1, 12, ks1.project.getName() + "コード："			+ ip + "行目", false);
				checkString (projectName[i], 1, 20, ks1.project.getName() + "名："				+ ip + "行目", false);
				checkString (segmentCd[i], 1, 8, ks1.segment.getName() + "コード："			+ ip + "行目", false);
				checkString (segmentName[i], 1, 20, ks1.segment.getName() + "名："				+ ip + "行目", false);
				checkString (uf1Cd[i], 1, 20, hfUfSeigyo.getUf1Name() + "："					+ ip + "行目", false);
				checkString (uf1Name[i], 1, 20, hfUfSeigyo.getUf1Name() + "："					+ ip + "行目", false);
				checkString (uf2Cd[i], 1, 20, hfUfSeigyo.getUf2Name() + "："					+ ip + "行目", false);
				checkString (uf2Name[i], 1, 20, hfUfSeigyo.getUf2Name() + "："					+ ip + "行目", false);
				checkString (uf3Cd[i], 1, 20, hfUfSeigyo.getUf3Name() + "："					+ ip + "行目", false);
				checkString (uf3Name[i], 1, 20, hfUfSeigyo.getUf3Name() + "："					+ ip + "行目", false);
				checkString (uf4Cd[i], 1, 20, hfUfSeigyo.getUf4Name() + "："					+ ip + "行目", false);
				checkString (uf4Name[i], 1, 20, hfUfSeigyo.getUf4Name() + "："					+ ip + "行目", false);
				checkString (uf5Cd[i], 1, 20, hfUfSeigyo.getUf5Name() + "："					+ ip + "行目", false);
				checkString (uf5Name[i], 1, 20, hfUfSeigyo.getUf5Name() + "："					+ ip + "行目", false);
				checkString (uf6Cd[i], 1, 20, hfUfSeigyo.getUf6Name() + "："					+ ip + "行目", false);
				checkString (uf6Name[i], 1, 20, hfUfSeigyo.getUf6Name() + "："					+ ip + "行目", false);
				checkString (uf7Cd[i], 1, 20, hfUfSeigyo.getUf7Name() + "："					+ ip + "行目", false);
				checkString (uf7Name[i], 1, 20, hfUfSeigyo.getUf7Name() + "："					+ ip + "行目", false);
				checkString (uf8Cd[i], 1, 20, hfUfSeigyo.getUf8Name() + "："					+ ip + "行目", false);
				checkString (uf8Name[i], 1, 20, hfUfSeigyo.getUf8Name() + "："					+ ip + "行目", false);
				checkString (uf9Cd[i], 1, 20, hfUfSeigyo.getUf9Name() + "："					+ ip + "行目", false);
				checkString (uf9Name[i], 1, 20, hfUfSeigyo.getUf9Name() + "："					+ ip + "行目", false);
				checkString (uf10Cd[i], 1, 20, hfUfSeigyo.getUf10Name() + "："					+ ip + "行目", false);
				checkString (uf10Name[i], 1, 20, hfUfSeigyo.getUf10Name() + "："					+ ip + "行目", false);
				checkDomain (kazeiKbn[i], kazeiKbnDomain, ks1.kazeiKbn.getName() + "："					+ ip + "行目", false);
				if(kazeiKbnCheck[i]) {
					checkDomain (zeiritsu[i], zeiritsuDomain, ks1.zeiritsu.getName() + "："	+ ip + "行目", false);
					checkDomain (keigenZeiritsuKbn[i], Domain.FLG, "軽減税率区分" + "：" + ip + "行目", false);
				}
				checkDomain (houjinCardFlgKeihi[i], Domain.FLG, ks1.houjinCardRiyou.getName() + "フラグ:"		+ ip + "行目", false);
				checkDomain (kaishaTehaiFlgKeihi[i], Domain.FLG, ks1.kaishaTehai.getName() + "フラグ:"			+ ip + "行目", false);
				checkKingakuOver1 (shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："			+ ip + "行目", false);
				checkKingakuOver0 (hontaiKingaku[i], "本体金額合計" + "："								+ ip + "行目", false);
				checkKingakuOver0 (shouhizeigaku[i], "消費税額合計" + "："								+ ip + "行目", false);
				checkString (tekiyou[i], 1, this.getIntTekiyouMaxLength(), ks1.tekiyou.getName() + "："					+ ip + "行目", false);
				checkSJIS (tekiyou[i], ks1.tekiyou.getName() + "："					+ ip + "行目");
				checkString (kousaihiShousai[i], 1, 240, ks1.kousaihiShousai.getName() + "："			+ ip + "行目", false);
				checkNumberOver1 (kousaihiNinzuu[i], 1, 6, "交際費人数："			+ ip + "行目", false);
				checkKingakuOver0 (kousaihiHitoriKingaku[i], "交際費一人当たりの金額："	+ ip + "行目", false);
				checkDomain (this.jigyoushaKbn[i], jigyoushaKbnDomain, ks.jigyoushaKbn.getName() + "：" + ip + "行目", false);
				checkDomain (this.bunriKbn[i], bunriKbnDomain, ks1.bunriKbn.getName() +  "：" + ip + "行目",false);
				checkDomain (this.kariShiireKbn[i], shiireKbnDomain, ks1.shiireKbn.getName() +  "：" + ip + "行目",false);
			}
		}
	}

	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum  1:登録/更新、2:支払日更新
	 */
	protected void denpyouHissuCheck(int eventNum) {

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		String[][] list = {
				//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{karibaraiDenpyouId, ks1.karibaraiDenpyouId.getName(), karibaraiIsEnabled ? ks1.karibaraiDenpyouId.getHissuFlgS() : "0", "0"},
				{karibaraiOn, ks1.karibaraiOn.getName(), karibaraiIsEnabled ? ks1.karibaraiOn.getHissuFlgS() : "0","0"},
				{karibaraiTekiyou, ks1.karibaraiTekiyou.getName(), karibaraiIsEnabled ? ks1.karibaraiTekiyou.getHissuFlgS() : "0", "0"},
				{karibaraiShinseiKingaku, ks1.shinseiKingaku.getName(), karibaraiIsEnabled ? ks1.shinseiKingaku.getHissuFlgS() : "0","0"},
				{karibaraiKingakuSagaku, ks1.karibaraiKingakuSagaku.getName(), karibaraiIsEnabled ? ks1.karibaraiKingakuSagaku.getHissuFlgS() : "0","0"},
				{shiharaihouhou, ks1.shiharaiHouhou.getName(), keihiEntryFlg ? ks1.shiharaiHouhou.getHissuFlgS() : "0", "0"},
				{shiharaiKiboubi, ks1.shiharaiKiboubi.getName(), keihiEntryFlg ? ks1.shiharaiKiboubi.getHissuFlgS() : "0", "0"},
				{houjinCardRiyouGoukei, ks1.uchiHoujinCardRiyouGoukei.getName(), keihiEntryFlg ? ks1.uchiHoujinCardRiyouGoukei.getHissuFlgS() : "0", "0"},
				{kaishaTehaiGoukei, ks1.kaishaTehaiGoukei.getName(), keihiEntryFlg ? ks1.kaishaTehaiGoukei.getHissuFlgS() : "0", "0"},
				{hontaiKingakuGoukei, "本体金額合計", keihiEntryFlg ? "1" : "0", "0"},
				{shouhizeigakuGoukei, "消費税額合計", keihiEntryFlg ? "1" : "0", "0"},
				{kingaku, ks1.shiharaiKingakuGoukei.getName(), keihiEntryFlg ? ks1.shiharaiKingakuGoukei.getHissuFlgS() : "0", "0"},
				{shiharaibi, "支払日", "0", "1"},
				{keijoubi, "計上日", keihiEntryFlg && (keijouBiMode == 1 || keijouBiMode == 3) ? "1" : "0", keihiEntryFlg && hasseiShugi ? "1" : "0"},
				{hosoku, ks1.hosoku.getName(), keihiEntryFlg ? ks1.hosoku.getHissuFlgS() : "0", "0"},
		};
		hissuCheckCommon(list, eventNum);

		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード"	,keihiEntryFlg ? ks1.hf1.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード"	,keihiEntryFlg ? ks1.hf2.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード"	,keihiEntryFlg ? ks1.hf3.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード"	,keihiEntryFlg ? ks1.hf4.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード"	,keihiEntryFlg ? ks1.hf5.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード"	,keihiEntryFlg ? ks1.hf6.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード"	,keihiEntryFlg ? ks1.hf7.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード"	,keihiEntryFlg ? ks1.hf8.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード"	,keihiEntryFlg ? ks1.hf9.getHissuFlgS() : "0"},}, 1);
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード"	,keihiEntryFlg ? ks1.hf10.getHissuFlgS() : "0"},}, 1);

		if (keihiEntryFlg) {

			String checkFlg = "0";
			if(denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN)) checkFlg = ks1.userName.getHissuFlgS();

			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				int ip = i + 1;
				list = new String[][]{
					//項目							項目名 														必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{userId[i], ks1.userName.getName() + "ID："			+ ip + "行目", checkFlg, "0"},
					{userName[i], ks1.userName.getName() + "名："			+ ip + "行目", checkFlg, "0"},
					{shainNo[i], ks1.userName.getName() + "社員番号："		+ ip + "行目", checkFlg, "0"},
					{shiyoubi[i], ks1.shiyoubi.getName() + "："				+ ip + "行目", ks1.shiyoubi.getHissuFlgS(), "0"},
					{shouhyouShorui[i], ks1.shouhyouShoruiFlg.getName() + "："		+ ip + "行目", ks1.shouhyouShoruiFlg.getHissuFlgS(), "0"},
					{shiwakeEdaNo[i], ks1.torihiki.getName() + "コード："		+ ip + "行目", ks1.torihiki.getHissuFlgS(), "0"},
					{torihikiName[i], ks1.torihiki.getName() + "名："			+ ip + "行目", ks1.torihiki.getHissuFlgS(), "0"},
					{kamokuCd[i], ks1.kamoku.getName() + "コード："			+ ip + "行目", ks1.kamoku.getHissuFlgS(), "0"},
					{kamokuName[i], ks1.kamoku.getName() + "名："				+ ip + "行目", ks1.kamoku.getHissuFlgS(), "0"},
					{kamokuEdabanCd[i], ks1.kamokuEdaban.getName() + "コード："	+ ip + "行目", ks1.kamokuEdaban.getHissuFlgS(), "0"},
					{kamokuEdabanName[i], ks1.kamokuEdaban.getName() + "名："		+ ip + "行目", ks1.kamokuEdaban.getHissuFlgS(), "0"},
					{futanBumonCd[i], ks1.futanBumon.getName() + "コード："		+ ip + "行目", ks1.futanBumon.getHissuFlgS(), "0"},
					{futanBumonName[i], ks1.futanBumon.getName() + "名："			+ ip + "行目", ks1.futanBumon.getHissuFlgS(), "0"},
					{projectCd[i], ks1.project.getName() + "コード："		+ ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0", "0"},
					{projectName[i], ks1.project.getName() + "名："			+ ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0", "0"},
					{segmentCd[i], ks1.segment.getName() + "コード："		+ ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"},
					{segmentName[i], ks1.segment.getName() + "名："			+ ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"},
					{torihikisakiCd[i], ks1.torihikisaki.getName()+ "コード："	+ ip + "行目", ks1.torihikisaki.getHissuFlgS(), "0"},
					{torihikisakiName[i], ks1.torihikisaki.getName()+ "名："		+ ip + "行目", ks1.torihikisaki.getHissuFlgS(), "0"},
					{ kazeiKbnCheck[i] ? zeiritsu[i]:"0", ks.zeiritsu.getName() + "：" + ip + "行目",kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0" ,"0"},
					{ kazeiKbnCheck[i] ? keigenZeiritsuKbn[i]:"0", "軽減税率区分" + "：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0","0"},
					{shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："		+ ip + "行目", ks1.shiharaiKingaku.getHissuFlgS(), "0"},
					{houjinCardFlgKeihi[i], ks1.houjinCardRiyou.getName() + ":"		+ ip + "行目", ks1.houjinCardRiyou.getHissuFlgS(),"0"},
					{kaishaTehaiFlgKeihi[i], ks1.kaishaTehai.getName() + ":"			+ ip + "行目", ks1.kaishaTehai.getHissuFlgS(),"0"},
					{tekiyou[i], ks1.tekiyou.getName() + "："				+ ip + "行目", ks1.tekiyou.getHissuFlgS(), "0"},
					{kousaihiShousai[i], ks1.kousaihiShousai.getName() + "："		+ ip + "行目", kousaihiEnable[i] ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
					{kousaihiNinzuu[i], "交際費人数："	+ ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0"},
					{kousaihiHitoriKingaku[i], "交際費一人当たりの金額：" + ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0"},
					{shiharaisakiName[i], ks1.shiharaisaki.getName() + "：" + ip + "行目", ( "0".equals(invoiceDenpyou) && ks1.shiharaisaki.getHyoujiFlg() ) ? ks1.shiharaisaki.getHissuFlgS() : "0","0"},
					{jigyoushaKbn[i], ks1.jigyoushaKbn.getName() + "：" + ip + "行目", ks.jigyoushaKbn.getHissuFlgS(),"0" },
					{bunriKbn[i], ks1.bunriKbn.getName() + "：" + ip + "行目", ks1.bunriKbn.getHissuFlgS(),"0" },
					{kariShiireKbn[i], ks1.shiireKbn.getName() + "：" + ip + "行目", ks1.shiireKbn.getHissuFlgS(),"0" },
				};
				hissuCheckCommon(list, eventNum);

				if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1Cd[i], hfUfSeigyo.getUf1Name() + "コード" + "："+ ip + "行目", ks1.uf1.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2Cd[i], hfUfSeigyo.getUf2Name() + "コード" + "："+ ip + "行目",ks1.uf2.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3Cd[i], hfUfSeigyo.getUf3Name() + "コード" + "："+ ip + "行目", ks1.uf3.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4Cd[i], hfUfSeigyo.getUf4Name() + "コード" + "："+ ip + "行目", ks1.uf4.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5Cd[i], hfUfSeigyo.getUf5Name() + "コード" + "："+ ip + "行目", ks1.uf5.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6Cd[i], hfUfSeigyo.getUf6Name() + "コード" + "："+ ip + "行目", ks1.uf6.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7Cd[i], hfUfSeigyo.getUf7Name() + "コード" + "："+ ip + "行目", ks1.uf7.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8Cd[i], hfUfSeigyo.getUf8Name() + "コード" + "："+ ip + "行目", ks1.uf8.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9Cd[i], hfUfSeigyo.getUf9Name() + "コード" + "："+ ip + "行目", ks1.uf9.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10Cd[i], hfUfSeigyo.getUf10Name() + "コード" + "："+ ip + "行目", ks1.uf10.getHissuFlgS()},}, 1);
			}
		}
	}

	/**
	 * 必須チェック・形式チェック以外の入力チェック=相関チェック
	 */
	protected void soukanCheck() {

		// 登録時のみ仮払伝票IDが登録できるものかチェック
		if(isEmpty(super.denpyouId) && !karibaraiDenpyouId.isEmpty()) {
			List<GMap> karibaraiChk = kaikeiLogic.loadKaribaraiAnken(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN , karibaraiDenpyouId, super.getUser().getSeigyoUserId());
			if(karibaraiChk.isEmpty()){
				errorList.add(ks1.karibaraiDenpyouId.getName() + "[" + karibaraiDenpyouId + "]の仮払は精算済みです。");
			}
		}
		
		//202208 tuika 承認解除は登録・更新時どっちも確認必要では？
		if(karibaraiDenpyouId != null && !karibaraiDenpyouId.isEmpty()) {
			GMap forDenpyouJoutai = wfLogic.selectDenpyou(karibaraiDenpyouId); 
			String joutai = forDenpyouJoutai.get("denpyou_joutai").toString(); 
			if(!joutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI)) { 
				errorList.add(karibaraiDenpyouId+"は承認解除されているため、仮払案件として選択できません。"); 
			}
		}

		//社員口座チェック(通常起票では伝票単位のチェック)
		if(dairiFlg.equals("0"))commonLogic.checkShainKouza(super.getKihyouUserId(), shiharaihouhou, errorList);

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
		commonLogic.checkShiwake(shiwakeCheckDataNaiyou, errorList);

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細単位のチェック
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				int ip = i + 1;

				//法人カード履歴使用明細のチェック(経費)
				if(!isEmpty(himodukeCardMeisaiKeihi[i])){
					int chkCd = hcLogic.checkHimodukeUsed(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
					switch(chkCd) {
					case -1:	errorList.add(ip + "行目：指定した法人カード履歴は除外済です。");break;
					case -2:	errorList.add(ip + "行目：指定した法人カード履歴は別伝票に紐付済みです。");break;
					case -99:	errorList.add(ip + "行目：指定した法人カード履歴データが存在しません。");break;
					}
				}

				//税率チェック
				if(kazeiKbnCheck[i]) {
					commonLogic.checkZeiritsu(kazeiKbn[i], toDecimal(zeiritsu[i]), keigenZeiritsuKbn[i], toDate(shiyoubi[i]), errorList, ip + "行目：");
				}

				//社員口座チェック(代理起票では明細単位のチェック)
				if(dairiFlg.equals("1"))commonLogic.checkShainKouza(userId[i], shiharaihouhou, errorList, ip + "行目：");

				String daihyouBumonCd = super.daihyouFutanBumonCd;
				// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
				if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userId[i]))) {
					daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userId[i]);
				}

				//各経費明細の仕訳パターン
				ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(denpyouKbn,Integer.parseInt(shiwakeEdaNo[i]));

				// 借方
				ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData() ;
				shiwakeCheckData.torihikisakiNm = ip + "行目：" + ks1.torihikisaki.getName() + "コード";
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
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf1Cd))shiwakeCheckData.uf1Cd = kariUf1Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf2Cd))shiwakeCheckData.uf2Cd = kariUf2Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf3Cd))shiwakeCheckData.uf3Cd = kariUf3Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf4Cd))shiwakeCheckData.uf4Cd = kariUf4Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf5Cd))shiwakeCheckData.uf5Cd = kariUf5Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf6Cd))shiwakeCheckData.uf6Cd = kariUf6Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf7Cd))shiwakeCheckData.uf7Cd = kariUf7Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf8Cd))shiwakeCheckData.uf8Cd = kariUf8Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf9Cd))shiwakeCheckData.uf9Cd = kariUf9Cd[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf10Cd))shiwakeCheckData.uf10Cd = kariUf10Cd[i];
				commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckData, daihyouBumonCd, errorList);

				// 貸方
				ShiwakeCheckData shiwakeCheckDataKashi1 = commonLogic.new ShiwakeCheckData() ;
				shiwakeCheckDataKashi1.torihikisakiNm = ip + "行目：貸方" + ks1.torihikisaki.getName() + "コード";
				shiwakeCheckDataKashi1.torihikisakiCd = kashiTorihikisakiCd[i];
				shiwakeCheckDataKashi1.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
				shiwakeCheckDataKashi1.shiwakeEdaNo = shiwakeEdaNo[i];
				shiwakeCheckDataKashi1.kamokuNm = ip + "行目：貸方" + ks1.kamoku.getName() + "コード";
				shiwakeCheckDataKashi1.kamokuCd = kashiKamokuCd[i];
				shiwakeCheckDataKashi1.kamokuEdabanNm = ip + "行目：貸方" + ks1.kamokuEdaban.getName() + "コード";
				shiwakeCheckDataKashi1.kamokuEdabanCd = kashiKamokuEdabanCd[i];
				shiwakeCheckDataKashi1.futanBumonNm = ip + "行目：貸方" + ks1.futanBumon.getName() + "コード";
				shiwakeCheckDataKashi1.futanBumonCd = kashiFutanBumonCd[i];
				shiwakeCheckDataKashi1.kazeiKbnNm = ip + "行目：貸方" + ks1.kazeiKbn.getName();
				shiwakeCheckDataKashi1.kazeiKbn = kashiKazeiKbn[i];
				shiwakeCheckDataKashi1.projectNm = ip + "行目：貸方" + ks1.project.getName() + "コード";
				shiwakeCheckDataKashi1.segmentNm = ip + "行目：貸方" + ks1.segment.getName() + "コード";
				shiwakeCheckDataKashi1.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
				shiwakeCheckDataKashi1.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
				shiwakeCheckDataKashi1.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
				shiwakeCheckDataKashi1.uf4Nm = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
				shiwakeCheckDataKashi1.uf5Nm = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
				shiwakeCheckDataKashi1.uf6Nm = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
				shiwakeCheckDataKashi1.uf7Nm = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
				shiwakeCheckDataKashi1.uf8Nm = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
				shiwakeCheckDataKashi1.uf9Nm = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
				shiwakeCheckDataKashi1.uf10Nm = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();

				switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					// 振込
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd1))
					{
						shiwakeCheckDataKashi1.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd1))
					{
						shiwakeCheckDataKashi1.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd1))
					{
						shiwakeCheckDataKashi1.uf1Cd = kashiUf1Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd1))
					{
						shiwakeCheckDataKashi1.uf2Cd = kashiUf2Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd1))
					{
						shiwakeCheckDataKashi1.uf3Cd = kashiUf3Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd1))
					{
						shiwakeCheckDataKashi1.uf4Cd = kashiUf4Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd1))
					{
						shiwakeCheckDataKashi1.uf5Cd = kashiUf5Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd1))
					{
						shiwakeCheckDataKashi1.uf6Cd = kashiUf6Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd1))
					{
						shiwakeCheckDataKashi1.uf7Cd = kashiUf7Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd1))
					{
						shiwakeCheckDataKashi1.uf8Cd = kashiUf8Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd1))
					{
						shiwakeCheckDataKashi1.uf9Cd = kashiUf9Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd1))
					{
						shiwakeCheckDataKashi1.uf10Cd = kashiUf10Cd1[i];
					}
					commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi1, daihyouBumonCd, errorList);
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					// 現金
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd2))
					{
						shiwakeCheckDataKashi1.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd2))
					{
						shiwakeCheckDataKashi1.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd2))
					{
						shiwakeCheckDataKashi1.uf1Cd = kashiUf1Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd2))
					{
						shiwakeCheckDataKashi1.uf2Cd = kashiUf2Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd2))
					{
						shiwakeCheckDataKashi1.uf3Cd = kashiUf3Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd2))
					{
						shiwakeCheckDataKashi1.uf4Cd = kashiUf4Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd2))
					{
						shiwakeCheckDataKashi1.uf5Cd = kashiUf5Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd2))
					{
						shiwakeCheckDataKashi1.uf6Cd = kashiUf6Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd2))
					{
						shiwakeCheckDataKashi1.uf7Cd = kashiUf7Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd2))
					{
						shiwakeCheckDataKashi1.uf8Cd = kashiUf8Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd2))
					{
						shiwakeCheckDataKashi1.uf9Cd = kashiUf9Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd2))
					{
						shiwakeCheckDataKashi1.uf10Cd = kashiUf10Cd2[i];
					}
					commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, shiwakeCheckDataKashi1, daihyouBumonCd, errorList);
					break;
				}

				// 貸方3
				if (hasseiShugi) {
					ShiwakeCheckData shiwakeCheckDataKashi3 = commonLogic.new ShiwakeCheckData();
					shiwakeCheckDataKashi3.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
					shiwakeCheckDataKashi3.shiwakeEdaNo = shiwakeEdaNo[i];
					shiwakeCheckDataKashi3.projectNm = ip + "行目：貸方" + ks1.project.getName() + "コード";
					shiwakeCheckDataKashi3.segmentNm = ip + "行目：貸方" + ks1.segment.getName() + "コード";
					shiwakeCheckDataKashi3.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
					shiwakeCheckDataKashi3.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
					shiwakeCheckDataKashi3.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
					shiwakeCheckDataKashi3.uf4Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
					shiwakeCheckDataKashi3.uf5Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
					shiwakeCheckDataKashi3.uf6Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
					shiwakeCheckDataKashi3.uf7Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
					shiwakeCheckDataKashi3.uf8Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
					shiwakeCheckDataKashi3.uf9Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
					shiwakeCheckDataKashi3.uf10Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd3))
					{
						shiwakeCheckDataKashi3.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd3))
					{
						shiwakeCheckDataKashi3.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd3))
					{
						shiwakeCheckDataKashi3.uf1Cd = kashiUf1Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd3))
					{
						shiwakeCheckDataKashi3.uf2Cd = kashiUf2Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd3))
					{
						shiwakeCheckDataKashi3.uf3Cd = kashiUf3Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd3))
					{
						shiwakeCheckDataKashi3.uf4Cd = kashiUf4Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd3))
					{
						shiwakeCheckDataKashi3.uf5Cd = kashiUf5Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd3))
					{
						shiwakeCheckDataKashi3.uf6Cd = kashiUf6Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd3))
					{
						shiwakeCheckDataKashi3.uf7Cd = kashiUf7Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd3))
					{
						shiwakeCheckDataKashi3.uf8Cd = kashiUf8Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd3))
					{
						shiwakeCheckDataKashi3.uf9Cd = kashiUf9Cd3[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd3))
					{
						shiwakeCheckDataKashi3.uf10Cd = kashiUf10Cd3[i];
					}
					commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, shiwakeCheckDataKashi3, daihyouBumonCd, errorList);
				}

				// 貸方4
				if (houjinCardFlgKeihi[i].equals("1")) {
					ShiwakeCheckData shiwakeCheckDataKashi4 = commonLogic.new ShiwakeCheckData();
					shiwakeCheckDataKashi4.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
					shiwakeCheckDataKashi4.shiwakeEdaNo = shiwakeEdaNo[i];
					shiwakeCheckDataKashi4.projectNm = ip + "行目：貸方" + ks1.project.getName() + "コード";
					shiwakeCheckDataKashi4.segmentNm = ip + "行目：貸方" + ks1.segment.getName() + "コード";
					shiwakeCheckDataKashi4.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
					shiwakeCheckDataKashi4.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
					shiwakeCheckDataKashi4.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
					shiwakeCheckDataKashi4.uf4Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
					shiwakeCheckDataKashi4.uf5Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
					shiwakeCheckDataKashi4.uf6Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
					shiwakeCheckDataKashi4.uf7Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
					shiwakeCheckDataKashi4.uf8Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
					shiwakeCheckDataKashi4.uf9Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
					shiwakeCheckDataKashi4.uf10Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd4))
					{
						shiwakeCheckDataKashi4.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd4))
					{
						shiwakeCheckDataKashi4.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd4))
					{
						shiwakeCheckDataKashi4.uf1Cd = kashiUf1Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd4))
					{
						shiwakeCheckDataKashi4.uf2Cd = kashiUf2Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd4))
					{
						shiwakeCheckDataKashi4.uf3Cd = kashiUf3Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd4))
					{
						shiwakeCheckDataKashi4.uf4Cd = kashiUf4Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd4))
					{
						shiwakeCheckDataKashi4.uf5Cd = kashiUf5Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd4))
					{
						shiwakeCheckDataKashi4.uf6Cd = kashiUf6Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd4))
					{
						shiwakeCheckDataKashi4.uf7Cd = kashiUf7Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd4))
					{
						shiwakeCheckDataKashi4.uf8Cd = kashiUf8Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd4))
					{
						shiwakeCheckDataKashi4.uf9Cd = kashiUf9Cd4[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd4))
					{
						shiwakeCheckDataKashi4.uf10Cd = kashiUf10Cd4[i];
					}
					commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, shiwakeCheckDataKashi4, daihyouBumonCd, errorList);
				}

				// 貸方5
				if (kaishaTehaiFlgKeihi[i].equals("1")) {
					ShiwakeCheckData shiwakeCheckDataKashi5 = commonLogic.new ShiwakeCheckData();
					shiwakeCheckDataKashi5.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
					shiwakeCheckDataKashi5.shiwakeEdaNo = shiwakeEdaNo[i];
					shiwakeCheckDataKashi5.projectNm = ip + "行目：貸方" + ks1.project.getName() + "コード";
					shiwakeCheckDataKashi5.segmentNm = ip + "行目：貸方" + ks1.segment.getName() + "コード";
					shiwakeCheckDataKashi5.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
					shiwakeCheckDataKashi5.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
					shiwakeCheckDataKashi5.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
					shiwakeCheckDataKashi5.uf4Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
					shiwakeCheckDataKashi5.uf5Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
					shiwakeCheckDataKashi5.uf6Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
					shiwakeCheckDataKashi5.uf7Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
					shiwakeCheckDataKashi5.uf8Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
					shiwakeCheckDataKashi5.uf9Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
					shiwakeCheckDataKashi5.uf10Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd5))
					{
						shiwakeCheckDataKashi5.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd5))
					{
						shiwakeCheckDataKashi5.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd5))
					{
						shiwakeCheckDataKashi5.uf1Cd = kashiUf1Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd5))
					{
						shiwakeCheckDataKashi5.uf2Cd = kashiUf2Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd5))
					{
						shiwakeCheckDataKashi5.uf3Cd = kashiUf3Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd5))
					{
						shiwakeCheckDataKashi5.uf4Cd = kashiUf4Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd5))
					{
						shiwakeCheckDataKashi5.uf5Cd = kashiUf5Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd5))
					{
						shiwakeCheckDataKashi5.uf6Cd = kashiUf6Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd5))
					{
						shiwakeCheckDataKashi5.uf7Cd = kashiUf7Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd5))
					{
						shiwakeCheckDataKashi5.uf8Cd = kashiUf8Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd5))
					{
						shiwakeCheckDataKashi5.uf9Cd = kashiUf9Cd5[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd5))
					{
						shiwakeCheckDataKashi5.uf10Cd = kashiUf10Cd5[i];
					}
					commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, shiwakeCheckDataKashi5, daihyouBumonCd, errorList);
				}

				// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
				if (isNotEmpty(torihikisakiCd[i])) {
					ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
					torihikisaki.torihikisakiNm = ip + "行目：" + ks1.torihikisaki.getName() + "コード";
					torihikisaki.torihikisakiCd = torihikisakiCd[i];
					commonLogic.checkTorihikisaki(torihikisaki, errorList);

					// 取引先仕入先チェック
					super.checkShiiresaki(ip + "行目：" + ks1.torihikisaki.getName() + "コード", torihikisakiCd[i], denpyouKbn);
				}
			}
			//計上日のチェック

			if ((getWfSeigyo().isTouroku() || getWfSeigyo().isShinsei()) && keijoubiSeigen && isNotEmpty(keijoubi)) {
				Date shimebi = keijoubiShimebiDao.findMaxShimebiForDenpyouKbn(denpyouKbn);
				if (shimebi != null && ! toDate(keijoubi).after(shimebi)) {
					errorList.add("計上日には締日(" + formatDate(shimebi) + ")より後を入力してください。");
				}

			}

			//交際費が入力されている場合基準値を満たしているかチェック
			List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,shiwakeEdaNo,kousaihiHitoriKingaku,null,commonLogic.KOUSAI_ERROR_CD_ERROR);
			for(GMap res : resultList) {if(res.get("errCd") != null && (int)res.get("errCd") == commonLogic.KOUSAI_ERROR_CD_ERROR ) errorList.add((String)res.get("errMsg"));}
		}

		// 稟議金額引継ぎフラグの入力チェック
		int work = 0;
		if(!karibaraiDenpyouId.isEmpty()){
			if (super.ringiKingakuHikitsugiFlg != null)
			{
				for(int i = 0; i < super.ringiKingakuHikitsugiFlg.length; i++){
					if(!"".equals(ringiKingakuHikitsugiFlg[i])) {
						work += Integer.parseInt(ringiKingakuHikitsugiFlg[i]);
					}
				}
			}
			if(Domain.FLG[0].equals(karibaraiOn)){
				// 仮払ありの場合
				GMap mapKianHimozuke = wfEventLogic.selectDenpyouKianHimozuke(karibaraiDenpyouId);
				if(null != (BigDecimal)mapKianHimozuke.get("ringi_kingaku")){
					if(work > 0){
						errorList.add("稟議金額を引継いでいる仮払申請が仮払選択されているため、添付伝票から稟議金額を引継ぐことはできません。");
					}
				}
			}else{
				// 仮払なしの場合
				if(work > 0){
					errorList.add("仮払なしの仮払申請が仮払選択されているため、添付伝票から稟議金額を引継ぐことはできません。");
				}
			}
		}

		//仮払未使用フラグの入力チェック
		if("1".equals(karibaraiMishiyouFlg)){
			if(commonLogic.isKaribaraiKianTenpuZumi(denpyouId, karibaraiDenpyouId)){
				errorList.add("仮払選択された伝票は起案添付済のため、仮払未使用で登録することはできません。");
				karibaraiMishiyouFlg = "0";
			}
		}
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
		String initShainCd = (usrInfo == null) ? "" : (String)usrInfo.get("shain_no");

		//新規起票時の表示状態作成
		if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {

			//代理起票できるかチェック
			dairiCheck();

			//初期入力状態
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
			
			invoiceDenpyou = setInvoiceFlgWhenNew();

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {

			GMap shinseiData = kaikeiLogic.findKeihiSeisan(denpyouId);
			shinseiData2Gamen(shinseiData);

			// 経費入力フラグ
			boolean keihiEntryFlg = judgeKeihiEntry();

			if (keihiEntryFlg) {
				List<GMap> meisaiList = kaikeiLogic.loadKeihiSeisanMeisai(denpyouId);
// List<KeihiseisanMeisai> meisaiList = keihiseisanMeisaiDao.loadByDenpyouId(denpyouId);
				meisaiData2Gamen(meisaiList, sanshou, shinseiData, connection);
			} else {
				makeDefaultMeisai();
			}

		//参照起票の表示状態作成
		} else {
			GMap shinseiData = kaikeiLogic.findKeihiSeisan(sanshouDenpyouId);
			shinseiData2Gamen(shinseiData);

			//代理起票できるかチェック
			dairiCheck();

			// 経費入力フラグ
			boolean keihiEntryFlg = judgeKeihiEntry();

			if (keihiEntryFlg) {
				sanshou = true;
				List<GMap> meisaiList = kaikeiLogic.loadKeihiSeisanMeisaiNoHoujinHimoduke(sanshouDenpyouId);
// List<KeihiseisanMeisai> meisaiList = keihiseisanMeisaiDao.loadByDenpyouIdHoujinHimoduke(sanshouDenpyouId);
				if (!meisaiList.isEmpty()) {
					if(getWfSeigyo().isOthersSanshouKihyou()) {
						//TODO ↓ココで参照起票の微調整が行われている　経費精算系複数明細から呼ばれていてmeisaiListの引数の型はGMap　DTO使用するように変えたい
						//理想：adjustmentTashaCopyが必要になる伝票用にインターフェース用意してくっつける
						//納品優先のため、インボイス対応では対応しない
						commonLogic.adjustmentTashaCopy(dairiFlg, getUser(), denpyouKbn, meisaiList);
					}
					meisaiData2Gamen(meisaiList, sanshou, shinseiData, connection);
				} else {
					makeDefaultMeisai();
				}
			} else {
				makeDefaultMeisai();
			}

			// 仮払案件がある場合はクリア
			if(!isEmpty(karibaraiDenpyouId)) {
				karibaraiDenpyouId ="";
				karibaraiOn = "";
				karibaraiTekiyou = "";
				karibaraiShinseiKingaku ="";
				karibaraiKingakuSagaku = "";
				karibaraiKingaku ="";
				karibaraiMishiyouFlg = "";
			}

			//差引支給金額は登録時に決定
			sashihikiShikyuuKingaku = "";

			// 支払方法は会社設定で設定されている中になければクリア
			String shiharaiHouhouSetting = setting.shiharaiHouhouA001();
			String[] shiharaiHouhouArray = shiharaiHouhouSetting.split(",");
			if (!Arrays.asList(shiharaiHouhouArray).contains(shiharaihouhou))
			{
				shiharaihouhou = "";
			}

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
			shiharaiKiboubi = "";
			for (int i = 0; i < shiyoubi.length; i++) {
				shiyoubi[i] = "";
			}
			keijoubi = "";
			shiharaibi = "";
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

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		// 経費入力が不要の場合、申請内容をリセットする
		if (!keihiEntryFlg) {
			resetShinsei(connection);
		}

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 < errorList.size())
		{
			return;
		}

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
 		reloadMaster(connection);

		//相関チェック
		soukanCheck();
		if (0 < errorList.size())
		{
			return;
		}

		//計上日の引数設定
		keijoubi = commonLogic.setKeijoubi(hasseiShugi, keijouBiMode, keijoubi, keijoubiDefaultSettei, setting.keijouNyuuryoku(), denpyouKbn, shiyoubi[0], errorList,loginUserInfo);
		if (0 < errorList.size())
		{
			return;
		}
	}

	//登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

 		//申請内容登録
		Keihiseisan dto = this.createDto();
		this.keihiseisanDao.insert(dto, dto.tourokuUserId);

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細登録
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				KeihiseisanMeisai meisaiDto = this.createMeisaiDto(i);
				this.keihiseisanMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
				if(!isEmpty(himodukeCardMeisaiKeihi[i])){
					hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
				}
			}
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

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		// 経費入力が不要の場合、申請内容をリセットする
		if (!keihiEntryFlg) {
			resetShinsei(connection);
		}

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 <errorList.size())
		{
			return;
		}

		//支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			denpyouHissuCheck(2);
			if (0 <errorList.size())
			{
				return;
			}
			checkShiharaiBi(shiharaibi, hasseiShugi ? keijoubi : shiyoubi[0], hasseiShugi ? "計上日" : "使用日");
			if (0 <errorList.size())
			{
				return;
			}
		}

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
 		reloadMaster(connection);

		//相関チェック
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}

		//計上日の値を設定に沿ってセット
		keijoubi = commonLogic.setKeijoubi(hasseiShugi, keijouBiMode, keijoubi, keijoubiDefaultSettei, setting.keijouNyuuryoku(), denpyouKbn, shiyoubi[0], errorList,loginUserInfo);
		if (0 <errorList.size())
		{
			return;
		}

		//申請内容更新
		Keihiseisan dto = this.createDto();
		this.keihiseisanDao.update(dto, dto.koushinUserId);

		//明細削除
		this.keihiseisanMeisaiDao.deleteByDenpyouId(this.denpyouId);
		hcLogic.removeDenpyouHimozuke(denpyouId);

		if (keihiEntryFlg) {

			//明細更新
	 		for (int i = 0; i < shiwakeEdaNo.length; i++) {
				KeihiseisanMeisai meisaiDto = this.createMeisaiDto(i);
				this.keihiseisanMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
				if(!isEmpty(himodukeCardMeisaiKeihi[i])){
					hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
				}
			}
		}
	}

	//承認ボタン押下時に、個別伝票について以下を行う。
	//・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 再表示用
		displaySeigyo(connection);

		// 支払日・計上日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			GMap data = kaikeiLogic.findKeihiSeisan(denpyouId);
			String shiharai = formatDate(data.get("shiharaibi"));
			String keijou = hasseiShugi ? formatDate(data.get("keijoubi")) : formatDate(data.get("shiyoubi"));
			String keijouName = hasseiShugi ? "計上日" : "使用日";
			if(judgeKeihiEntry() && hasseiShugi && isEmpty(keijou)) errorList.add(keijouName + "が未登録です。" + keijouName +"を登録してください。");
			checkShiharaiBi(shiharai, keijou, keijouName);
			// エラーの場合の表示用に現実の値を設定
			shiharaibi = shiharai;
			if (hasseiShugi)
			{
				keijoubi = keijou;
			} else shiyoubi[0] = keijou;
		}

		//仮払指定時かつ最終承認時は仮払伝票の精算完了日を更新
		if( (!isEmpty(karibaraiDenpyouId)) && wfEventLogic.isLastShounin(denpyouId,loginUserInfo)){
			this.keihiseisanDao.updateKaribaraiSeisanbi(karibaraiDenpyouId,loginUserId);
		}
	}

	//個別業務の申請時の処理を実装。
	//エラーが有る場合は「errorList」にエラーメッセージを追加する。
	@Override
	protected void shinseiKobetsuDenpyou (EteamConnection connection) {
		initParts(connection);

		//表示制御（エラー発生時用）
		displaySeigyo(connection);
	}

	//登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){
		BigDecimal zandaka = (BigDecimal)ringiData.get("ringiKingakuZandaka");

		//仮払未選択または（仮払選択済みで）仮払ありの場合
		if("".equals(karibaraiDenpyouId) || Domain.FLG[0].equals(karibaraiOn)){
			zandaka = zandaka.subtract(toDecimal(kingaku));

			//仮払が稟議金額を引継いでいる場合は仮払金額残高に加算する。
			if(0 != karibaraiKingaku.length()){
				// 稟議金額の引継ぎ元を特定
				GMap motoDenpyouInfo = new GMap();
				motoDenpyouInfo = wfLogic.findRingiMotoDenpyouId(motoDenpyouInfo, karibaraiDenpyouId);
				String motoDenpyouId = motoDenpyouInfo.get("ringi_kingaku_hikitsugimoto_denpyou_base").toString();
				if(!"".equals(motoDenpyouId)){
					zandaka = zandaka.add(toDecimal(karibaraiKingaku));
				}
			}
		}

		ringiData.put("ringiKingakuZandaka", zandaka);
	}

	//稟議金額の表示有無を判定する。
	@Override
	protected boolean judgeRingiKingakuHyoujiKobetsu(){
		boolean isCalc = true;
		// 仮払なしの場合は稟議金額を表示しない
		if (Domain.FLG[1].equals(karibaraiOn)){
			isCalc = false;
		}
		return isCalc;
	}

	//画面に入力された仮払伝票IDを呼び出し元に返却する。
	@Override
	protected String getKaribaraiDenpyouIdKobetsu(){
		return this.karibaraiDenpyouId;
	}

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

	// 申請内容が仮払金未使用かどうか。
	@Override
	protected boolean isKaribaraiMishiyou() {
		boolean result = false;
		if("1".equals(this.karibaraiMishiyouFlg)){
			result =  true;
		}
		return result;
	}

	// 起案終了／起案終了解除の対象とする仮払伝票IDを取得する。
	@Override
	protected String getKianShuuryouKaribaraiDenpyouId(){
		String result = null;

		// 仮払申請が予算執行対象
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.KARIBARAI_SHINSEI);
		if(YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(denpyouShubetsuMap.get("yosan_shikkou_taishou").toString())){

			// 仮払金未使用
			if("1".equals(this.karibaraiMishiyouFlg)){
				// 仮払伝票IDを返却
				result = this.karibaraiDenpyouId;
			}
		}
		return result;
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
		return commonLogic.isYosanShikkouKamoku(denpyouId, kamokuCdSet, super.kianbangouBoDialogNendo);
	}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		keihiTatekaeLogic = EteamContainer.getComponent(KeihiTatekaeSeisanLogic.class, connection);
		keihiseisanDao = EteamContainer.getComponent(KeihiseisanDao.class, connection);
		keihiseisanMeisaiDao = EteamContainer.getComponent(KeihiseisanMeisaiDao.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
		naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		keijoubiShimebiDao = EteamContainer.getComponent(KeijoubiShimebiDao.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		karibaraiDao = EteamContainer.getComponent(KaribaraiDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}


	/**
	 * 申請内容をリセットする。
	 * @param connection コネクション
	 */
	protected void resetShinsei(EteamConnection connection) {
		shiharaihouhou = "";
		shiharaiKiboubi = "";
		hf1Cd = "";
		hf1Name = "";
		hf2Cd = "";
		hf2Name = "";
		hf3Cd = "";
		hf3Name = "";
		hf4Cd = "";
		hf4Name = "";
		hf5Cd = "";
		hf5Name = "";
		hf6Cd = "";
		hf6Name = "";
		hf7Cd = "";
		hf7Name = "";
		hf8Cd = "";
		hf8Name = "";
		hf9Cd = "";
		hf9Name = "";
		hf10Cd = "";
		hf10Name = "";
	}

	/**
	 * 経費精算テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 経費精算レコード
	 */
	protected void shinseiData2Gamen(GMap shinseiData) {
		dairiFlg = (String)shinseiData.get("dairiflg");
		keijoubi = formatDate(shinseiData.get("keijoubi"));
		shiharaibi = formatDate(shinseiData.get("shiharaibi"));
		shiharaiKiboubi = formatDate(shinseiData.get("shiharaikiboubi"));
		karibaraiDenpyouId = (String)shinseiData.get("karibarai_denpyou_id");
		karibaraiOn = (String)shinseiData.get("karibarai_on");
		karibaraiTekiyou = (String)shinseiData.get("tekiyou");
		karibaraiShinseiKingaku = formatMoney(shinseiData.get("kingaku"));
		karibaraiKingaku = formatMoney(shinseiData.get("karibarai_kingaku"));
		karibaraiMishiyouFlg = (String)shinseiData.get("karibarai_mishiyou_flg");
		shiharaihouhou = (String)shinseiData.get("shiharaihouhou");
		hontaiKingakuGoukei = formatMoney(shinseiData.get("hontai_kingaku_goukei"));
		houjinCardRiyouGoukei = formatMoney(shinseiData.get("houjin_card_riyou_kingaku"));
		kaishaTehaiGoukei = formatMoney(shinseiData.get("kaisha_tehai_kingaku"));
		hf1Cd = (String)shinseiData.get("hf1_cd");
		hf1Name = (String)shinseiData.get("hf1_name_ryakushiki");
		hf2Cd = (String)shinseiData.get("hf2_cd");
		hf2Name = (String)shinseiData.get("hf2_name_ryakushiki");
		hf3Cd = (String)shinseiData.get("hf3_cd");
		hf3Name = (String)shinseiData.get("hf3_name_ryakushiki");
		hf4Cd = (String)shinseiData.get("hf4_cd");
		hf4Name = (String)shinseiData.get("hf4_name_ryakushiki");
		hf5Cd = (String)shinseiData.get("hf5_cd");
		hf5Name = (String)shinseiData.get("hf5_name_ryakushiki");
		hf6Cd = (String)shinseiData.get("hf6_cd");
		hf6Name = (String)shinseiData.get("hf6_name_ryakushiki");
		hf7Cd = (String)shinseiData.get("hf7_cd");
		hf7Name = (String)shinseiData.get("hf7_name_ryakushiki");
		hf8Cd = (String)shinseiData.get("hf8_cd");
		hf8Name = (String)shinseiData.get("hf8_name_ryakushiki");
		hf9Cd = (String)shinseiData.get("hf9_cd");
		hf9Name = (String)shinseiData.get("hf9_name_ryakushiki");
		hf10Cd = (String)shinseiData.get("hf10_cd");
		hf10Name = (String)shinseiData.get("hf10_name_ryakushiki");
		shouhizeigakuGoukei = formatMoney(shinseiData.get("shouhizeigaku_goukei"));
		kingaku = formatMoney(shinseiData.get("shiharai_kingaku_goukei"));
		sashihikiShikyuuKingaku = formatMoney(shinseiData.get("sashihiki_shikyuu_kingaku"));
		hosoku = (String)shinseiData.get("hosoku");
		invoiceDenpyou = (String)shinseiData.get("invoice_denpyou");
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisai() {
		//消費税率マップ
		GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();

		//表示項目
		shiyoubi = new String[1]; shiyoubi[0] = "";
		shouhyouShorui = new String[1]; shouhyouShorui[0] = "";
		userId = new String[1]; userId[0] = "";
		userName = new String[1]; userName[0] = "";
		shainNo = new String[1]; shainNo[0] = "";
		shiwakeEdaNo = new String[1]; shiwakeEdaNo[0] = "";
		torihikiName = new String[1]; torihikiName[0] = "";
		kamokuCd = new String[1]; kamokuCd[0] = "";
		kamokuName = new String[1]; kamokuName[0] = "";
		kamokuEdabanCd = new String[1]; kamokuEdabanCd[0] = "";
		kamokuEdabanName = new String[1]; kamokuEdabanName[0]= "";
		futanBumonCd = new String[1]; futanBumonCd[0] = "";
		futanBumonName = new String[1]; futanBumonName[0] = "";
		torihikisakiCd = new String[1]; torihikisakiCd[0] = "";
		torihikisakiName = new String[1]; torihikisakiName[0]= "";
		projectCd = new String[1]; projectCd[0] = "";
		projectName = new String[1]; projectName[0] = "";
		segmentCd = new String[1]; segmentCd[0] = "";
		segmentName = new String[1]; segmentName[0] = "";
		uf1Cd = new String[1]; uf1Cd[0] = "";
		uf1Name = new String[1]; uf1Name[0] = "";
		uf2Cd = new String[1]; uf2Cd[0] = "";
		uf2Name = new String[1]; uf2Name[0] = "";
		uf3Cd = new String[1]; uf3Cd[0] = "";
		uf3Name = new String[1]; uf3Name[0] = "";
		uf4Cd = new String[1]; uf4Cd[0] = "";
		uf4Name = new String[1]; uf4Name[0] = "";
		uf5Cd = new String[1]; uf5Cd[0] = "";
		uf5Name = new String[1]; uf5Name[0] = "";
		uf6Cd = new String[1]; uf6Cd[0] = "";
		uf6Name = new String[1]; uf6Name[0] = "";
		uf7Cd = new String[1]; uf7Cd[0] = "";
		uf7Name = new String[1]; uf7Name[0] = "";
		uf8Cd = new String[1]; uf8Cd[0] = "";
		uf8Name = new String[1]; uf8Name[0] = "";
		uf9Cd = new String[1]; uf9Cd[0] = "";
		uf9Name = new String[1]; uf9Name[0] = "";
		uf10Cd = new String[1]; uf10Cd[0] = "";
		uf10Name = new String[1]; uf10Name[0] = "";
		kazeiKbn = new String[1]; kazeiKbn[0] = "";
		zeiritsu = new String[1]; zeiritsu[0] = initZeiritsu.getString("zeiritsu");
		keigenZeiritsuKbn = new String[1]; keigenZeiritsuKbn[0] = initZeiritsu.get("keigen_zeiritsu_flg");
		shiharaiKingaku = new String[1]; shiharaiKingaku[0] = "";
		hontaiKingaku = new String[1]; hontaiKingaku[0] = "";
		shouhizeigaku = new String[1]; shouhizeigaku[0] = "";
		houjinCardFlgKeihi = new String[1]; houjinCardFlgKeihi[0]= "";
		kaishaTehaiFlgKeihi = new String[1]; kaishaTehaiFlgKeihi[0]= "";
		tekiyou = new String[1]; tekiyou[0] = "";
		chuuki2 = new String[1]; chuuki2[0] = "";
		chuukiKousai2 = new String[2]; chuukiKousai2[0] = ""; chuukiKousai2[1] = "";
		kousaihiShousai = new String[1]; kousaihiShousai[0] = "";
		kousaihiNinzuu = new String[1]; kousaihiNinzuu[0] = "";
		kousaihiHitoriKingaku = new String[1]; kousaihiHitoriKingaku[0] = "";
		himodukeCardMeisaiKeihi = new String[1]; himodukeCardMeisaiKeihi[0] = "";
		shiharaisakiName = new String[1]; shiharaisakiName[0] = "";
		jigyoushaKbn = new String[1]; jigyoushaKbn[0] = "";
		bunriKbn = new String[1]; bunriKbn[0] = "";
		kariShiireKbn = new String[1]; kariShiireKbn[0] = "";
		kashiShiireKbn = new String[1]; kashiShiireKbn[0] = "";

		//制御情報
		kamokuEdabanEnable = new boolean[1];
		futanBumonEnable = new boolean[1];
		torihikisakiEnable = new boolean[1];
		projectEnable = new boolean[1];
		segmentEnable = new boolean[1];
		kousaihiEnable = new boolean[1];
		ninzuuEnable = new boolean[1];
		uf1Enable = new boolean[1];
		uf2Enable = new boolean[1];
		uf3Enable = new boolean[1];
		uf4Enable = new boolean[1];
		uf5Enable = new boolean[1];
		uf6Enable = new boolean[1];
		uf7Enable = new boolean[1];
		uf8Enable = new boolean[1];
		uf9Enable = new boolean[1];
		uf10Enable = new boolean[1];
		zeiritsuEnable = new boolean[1];
	}

	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList  明細レコードのリスト
	 * @param sanshou     参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param shinseiData 伝票内容
	 * @param connection  コネクション
	 */
	protected void meisaiData2Gamen(List<GMap> meisaiList, boolean sanshou, GMap shinseiData, EteamConnection connection) {
		int length = meisaiList.size();
		userId = new String[length];
		userName = new String[length];
		shainNo = new String[length];
		shiyoubi = new String[length];
		shouhyouShorui = new String[length];
		shiwakeEdaNo = new String[length];
		torihikiName = new String[length];
		kamokuCd = new String[length];
		kamokuName = new String[length];
		kamokuEdabanCd = new String[length];
		kamokuEdabanName = new String[length];
		futanBumonCd = new String[length];
		futanBumonName = new String[length];
		torihikisakiCd = new String[length];
		torihikisakiName = new String[length];
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
		hontaiKingaku = new String[length];
		shouhizeigaku = new String[length];
		houjinCardFlgKeihi = new String[length];
		kaishaTehaiFlgKeihi = new String[length];
		tekiyou = new String[length];
		chuuki2 = new String[length];
		chuukiKousai2 = new String[length];
		kousaihiShousai = new String[length];
		kousaihiNinzuu = new String[length];
		kousaihiHitoriKingaku = new String[length];
		kousaihiHyoujiFlg = new String[length];
		ninzuuRiyouFlg = new String[length];
		himodukeCardMeisaiKeihi = new String[length];
		shiharaisakiName = new String[length];
		jigyoushaKbn = new String[length];
		bunriKbn = new String[length];
		kariShiireKbn = new String[length];
		kashiShiireKbn = new String[length];
		for (int i = 0; i < length; i++) {
			GMap meisai = meisaiList.get(i);
			userId[i] = (String)meisai.get("user_id");
			userName[i] = (String)meisai.get("user_sei") + "　" + (String)meisai.get("user_mei");
			shainNo[i] = (String)meisai.get("shain_no");
			shiyoubi[i] = formatDate(meisai.get("shiyoubi"));
			shouhyouShorui[i] = (String)meisai.get("shouhyou_shorui_flg");
			shiwakeEdaNo[i] = ((Integer)meisai.get("shiwake_edano")).toString();
			torihikiName[i] = (String)meisai.get("torihiki_name");
			kamokuCd[i] = (String)meisai.get("kari_kamoku_cd");
			kamokuName[i] = (String)meisai.get("kari_kamoku_name");
			kamokuEdabanCd[i] = (String)meisai.get("kari_kamoku_edaban_cd");
			kamokuEdabanName[i] = (String)meisai.get("kari_kamoku_edaban_name");
			futanBumonCd[i] = (String)meisai.get("kari_futan_bumon_cd");
			futanBumonName[i] = (String)meisai.get("kari_futan_bumon_name");
			torihikisakiCd[i] = (String)meisai.get("torihikisaki_cd");
			torihikisakiName[i] = (String)meisai.get("torihikisaki_name_ryakushiki");
			projectCd[i] = (String)meisai.get("project_cd");
			projectName[i] = (String)meisai.get("project_name");
			segmentCd[i] = (String)meisai.get("segment_cd");
			segmentName[i] = (String)meisai.get("segment_name_ryakushiki");
			uf1Cd[i] = (String)meisai.get("uf1_cd");
			uf1Name[i] = (String)meisai.get("uf1_name_ryakushiki");
			uf2Cd[i] = (String)meisai.get("uf2_cd");
			uf2Name[i] = (String)meisai.get("uf2_name_ryakushiki");
			uf3Cd[i] = (String)meisai.get("uf3_cd");
			uf3Name[i] = (String)meisai.get("uf3_name_ryakushiki");
			uf4Cd[i] = (String)meisai.get("uf4_cd");
			uf4Name[i] = (String)meisai.get("uf4_name_ryakushiki");
			uf5Cd[i] = (String)meisai.get("uf5_cd");
			uf5Name[i] = (String)meisai.get("uf5_name_ryakushiki");
			uf6Cd[i] = (String)meisai.get("uf6_cd");
			uf6Name[i] = (String)meisai.get("uf6_name_ryakushiki");
			uf7Cd[i] = (String)meisai.get("uf7_cd");
			uf7Name[i] = (String)meisai.get("uf7_name_ryakushiki");
			uf8Cd[i] = (String)meisai.get("uf8_cd");
			uf8Name[i] = (String)meisai.get("uf8_name_ryakushiki");
			uf9Cd[i] = (String)meisai.get("uf9_cd");
			uf9Name[i] = (String)meisai.get("uf9_name_ryakushiki");
			uf10Cd[i] = (String)meisai.get("uf10_cd");
			uf10Name[i] = (String)meisai.get("uf10_name_ryakushiki");
			kazeiKbn[i] = (String)meisai.get("kari_kazei_kbn");
			zeiritsu[i] = formatMoney(meisai.get("zeiritsu"));
			keigenZeiritsuKbn[i]= meisai.get("keigen_zeiritsu_kbn");
			shiharaiKingaku[i] = formatMoney(meisai.get("shiharai_kingaku"));
			hontaiKingaku[i] = formatMoney(meisai.get("hontai_kingaku"));
			shouhizeigaku[i] = formatMoney(meisai.get("shouhizeigaku"));
			houjinCardFlgKeihi[i]= (String)meisai.get("houjin_card_riyou_flg");
			kaishaTehaiFlgKeihi[i]= (String)meisai.get("kaisha_tehai_flg");
			tekiyou[i] = (String)meisai.get("tekiyou");
			shiharaisakiName[i] = (String)meisai.get("shiharaisaki_name");
			jigyoushaKbn[i] = (String)meisai.get("jigyousha_kbn");
			bunriKbn[i] = (String)meisai.get("bunri_kbn");
			kariShiireKbn[i] = (String)meisai.get("kari_shiire_kbn");
			kashiShiireKbn[i] = (String)meisai.get("kashi_shiire_kbn");
			kousaihiShousai[i] = (String)meisai.get("kousaihi_shousai");
			kousaihiHyoujiFlg[i]= (String)meisai.get("kousaihi_shousai_hyouji_flg");
			ninzuuRiyouFlg[i]= (String)meisai.get("kousaihi_ninzuu_riyou_flg");
			kousaihiNinzuu[i] = meisai.get("kousaihi_ninzuu") == null ? "" : ((Integer)meisai.get("kousaihi_ninzuu")).toString();
			kousaihiHitoriKingaku[i] = formatMoney(meisai.get("kousaihi_hitori_kingaku"));
			if(!sanshou){
				String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, shinseiData, meisai, "0");
				String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
				if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
					chuuki1 = commonLogic.getTekiyouChuuki();
					chuuki2[i] = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
				}
			}
			himodukeCardMeisaiKeihi[i] = null == meisai.get("himoduke_card_meisai") ? "" : meisai.get("himoduke_card_meisai").toString();
		}
		if(!sanshou){
			//交際費関連の警告メッセージ取得
			chuukiKousai1 = "";
			List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,shiwakeEdaNo,kousaihiHitoriKingaku,null,commonLogic.KOUSAI_ERROR_CD_KEIKOKU);
			for(GMap res : resultList) {
				if(res.get("index") != null) {
					chuukiKousai2[(Integer)res.get("index")] = (String)res.get("errMsg");
				}else if(res.get("keikokuMsg") != null) {
					chuukiKousai1 += (isEmpty(chuukiKousai1) ? "" : "\r\n") + (String)res.get("keikokuMsg");
				}
			}
		}
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		//プルダウンのリストを取得
		zeiritsuList = zeiritsuDao.load().stream().map(Shouhizeiritsu::getMap).collect(Collectors.toList());
		zeiritsuDomain = EteamCommon.mapList2Arr(zeiritsuList, "zeiritsu");
		kazeiKbnList =  naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("kazei_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		kazeiKbnDomain = EteamCommon.mapList2Arr(kazeiKbnList, "naibu_cd");
		bunriKbnList =  naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		bunriKbnDomain = EteamCommon.mapList2Arr(bunriKbnList, "naibu_cd");
		shiireKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		shiireKbnDomain = EteamCommon.mapList2Arr(shiireKbnList, "naibu_cd");
		shiharaihouhouList = commonLogic.makeShiharaiHouhouList(EteamSettingInfo.Key.SHIHARAI_HOUHOU_A001, shiharaihouhou);
		shiharaihouhouDomain = EteamCommon.mapList2Arr(shiharaihouhouList, "naibu_cd");
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		jigyoushaKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");

		//支払方法を入力可能にするかどうか判定。
		disableShiharaiHouhou = (shiharaihouhouList.size() <= 1);

		//仮払案件ボタンの表示判定
		if(enableInput) {
			judgeKaribaraiAnken(connection);
		} else {
			userKaribaraiUmuFlg = "0";
		}

		//支払日登録可能かどうか制御 (0:非表示、1:入力可、2:表示)
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細単位に仕訳パターンによる制御
			int length = shiwakeEdaNo.length;
			kamokuEdabanEnable = new boolean[length];
			futanBumonEnable = new boolean[length];
			torihikisakiEnable = new boolean[length];
			projectEnable = new boolean[length];
			segmentEnable = new boolean[length];
			kousaihiEnable = new boolean[length];
			ninzuuEnable = new boolean[length];
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
			kazeiKbnCheck = new boolean[length];
			for (int i = 0; i < length; i++) {
				if (enableInput) {
					if (isNotEmpty(shiwakeEdaNo[i])) {
						//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
						//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
						GMap shiwakePattern = shiwakePatternMasterDao.find(denpyouKbn,Integer.parseInt(shiwakeEdaNo[i])).map;
						InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
						kamokuEdabanEnable[i] = info.kamokuEdabanEnable;
						futanBumonEnable[i] = info.futanBumonEnable;
						torihikisakiEnable[i] = info.torihikisakiEnable;
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
						kousaihiEnable[i] = info.kousaihiEnable;
						ninzuuEnable[i] = info.ninzuuEnable;
					}
				} else {
					kousaihiEnable[i] = "1".equals(kousaihiHyoujiFlg[i]);
					ninzuuEnable[i] = "1".equals(ninzuuRiyouFlg[i]);
				}
				kazeiKbnCheck[i] = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]);
			}
		}

		//計上日の入力状態・モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン))を判定
		keijouBiMode = commonLogic.judgeKeijouBiMode(hasseiShugi, keijoubiDefaultSettei.equals("3"), denpyouId, loginUserInfo, enableInput, setting.keijouNyuuryoku());
		//計上日プルダウンのリスト取得
		if(keijouBiMode == 3) keijoubiList = masterLogic.loadKeijoubiList(denpyouKbn);

		//経理担当が初めて伝票を開き、その伝票が振込のとき、支払日に自動入力
		if (shiharaiBiMode == 1  && isEmpty(shiharaibi) && shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI)) {
			Date shiharai = null;
			if (setting.furikomiRuleA001().equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(keijoubi));
			} else if (setting.furikomiRuleA001().equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(keijoubi));
			}
			if (shiharai != null) {
				shiharaibi = formatDate(shiharai);
				// マスターから基準日(計上日)に対する振込日が見つかった場合、支払日の更新
				this.keihiseisanDao.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}

		// 代理起票フラグ（パラメータ）
		if(isNotEmpty(denpyouId)) {
			GMap data = kaikeiLogic.findKeihiSeisan(denpyouId);
			dairiFlg = data.get("dairiflg").toString();
		}
		else {
			HttpServletRequest request = null;
			try {
				request =  ServletActionContext.getRequest();
			} catch(NullPointerException e) {
				//UTテスト用（ServletActionContextを使用しない）
			}
			if (null != request && null != request.getQueryString()) {
				String[] param = request.getQueryString().split("&", 0);
				for (int i = 0 ; i < param.length ; i++){
					if(param[i].equals("dairiFlg=1")) {
						dairiFlg = "1";
						break;
					}
				}
			}

		}

		// 全明細参照判定
		allMeisaiView = keihiTatekaeLogic.judgeAllMeisaiView(denpyouId, getUser(), dairiFlg);
		if(!allMeisaiView) {
			enableInput = false;
		}

		//仮払選択表示設定
		GMap denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.KARIBARAI_SHINSEI);
		if (denpyouShubetsuMap != null) {
			karibaraiIsEnabled = true;
		} else {
			karibaraiIsEnabled = false;
		}

		// 法人カードの表示可否
		houjinCardFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		GMap chkMap  = bumonUsrLogic.selectUserInfo(getUser().getSeigyoUserId());
		if( ("1".equals(chkMap.get("houjin_card_riyou_flag")) || "1".equals(dairiFlg) ) && houjinCardFlag == true ){
			houjinCardRirekiEnable = true;
		}else{
			houjinCardRirekiEnable = false;
		}
		// 会社手配の表示可否
		kaishaTehaiFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);

		// 法人カード履歴選択ダイアログ用設定
		kihyoushaUserId = getKihyouUserId();
		GMap record = bumonUsrLogic.selectUserInfo(kihyoushaUserId);
		if(null != record){
			kihyoushaShainNo = (String) record.get("shain_no");
			kihyoushaUserName =  (String) record.get("user_sei") + "　" + (String) record.get("user_mei");
		}

		//仮払申請の起案添付済みフラグを設定（新規起票以外）
		if(isNotEmpty(denpyouId)){
			if(commonLogic.isKaribaraiKianTenpuZumi(denpyouId, karibaraiDenpyouId)){
				karibaraiKianTenpuZumiFlg = "1";
			}else{
				karibaraiKianTenpuZumiFlg = "0";
			}
		}
	}

	/**
	 * 登録時に仕訳パターンマスターより借方貸方の値を設定する。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {

		//仮払情報の取得
		BigDecimal tmpkaribaraiKingaku;
		if(! karibaraiDenpyouId.isEmpty()) {
			Karibarai karibarai = karibaraiDao.find(karibaraiDenpyouId);
			tmpkaribaraiKingaku = karibarai.karibaraiOn.equals("1") ? karibarai.karibaraiKingaku : BigDecimal.ZERO;
		} else {
			tmpkaribaraiKingaku = new BigDecimal(0);
		}

		//差引支給金額の計算
		BigDecimal tmpShiharaiKingaku = toDecimal(kingaku);
		BigDecimal tmpHoujinKingaku = toDecimal(houjinCardRiyouGoukei);
		BigDecimal tmpKaishaTehaiKingaku = toDecimal(kaishaTehaiGoukei);
		sashihikiShikyuuKingaku = formatMoney(((tmpShiharaiKingaku.subtract(tmpHoujinKingaku)).subtract(tmpKaishaTehaiKingaku)).subtract(tmpkaribaraiKingaku));

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細行数分の領域確保
			int length = shiwakeEdaNo.length;

			kousaihiHyoujiFlg = new String[length];
			ninzuuRiyouFlg = new String[length];
			kariTorihikisakiCd = new String[length];

			kariUf1Cd = new String[length];
			kariUf2Cd = new String[length];
			kariUf3Cd = new String[length];
			kariUf4Cd = new String[length];
			kariUf5Cd = new String[length];
			kariUf6Cd = new String[length];
			kariUf7Cd = new String[length];
			kariUf8Cd = new String[length];
			kariUf9Cd = new String[length];
			kariUf10Cd = new String[length];
			kashiTorihikisakiCd = new String[length];
			kashiFutanBumonCd = new String[length];
			kashiKamokuCd = new String[length];
			kashiKamokuEdabanCd = new String[length];
			kashiKazeiKbn = new String[length];
			tekiyouCd  			= new String[length];

			kashiUf1Cd1 = new String[length];
			kashiUf2Cd1 = new String[length];
			kashiUf3Cd1 = new String[length];
			kashiUf4Cd1 = new String[length];
			kashiUf5Cd1 = new String[length];
			kashiUf6Cd1 = new String[length];
			kashiUf7Cd1 = new String[length];
			kashiUf8Cd1 = new String[length];
			kashiUf9Cd1 = new String[length];
			kashiUf10Cd1 = new String[length];

			kashiUf1Cd2 = new String[length];
			kashiUf2Cd2 = new String[length];
			kashiUf3Cd2 = new String[length];
			kashiUf4Cd2 = new String[length];
			kashiUf5Cd2 = new String[length];
			kashiUf6Cd2 = new String[length];
			kashiUf7Cd2 = new String[length];
			kashiUf8Cd2 = new String[length];
			kashiUf9Cd2 = new String[length];
			kashiUf10Cd2 = new String[length];

			kashiUf1Cd3 = new String[length];
			kashiUf2Cd3 = new String[length];
			kashiUf3Cd3 = new String[length];
			kashiUf4Cd3 = new String[length];
			kashiUf5Cd3 = new String[length];
			kashiUf6Cd3 = new String[length];
			kashiUf7Cd3 = new String[length];
			kashiUf8Cd3 = new String[length];
			kashiUf9Cd3 = new String[length];
			kashiUf10Cd3 = new String[length];

			kashiUf1Cd4 = new String[length];
			kashiUf2Cd4 = new String[length];
			kashiUf3Cd4 = new String[length];
			kashiUf4Cd4 = new String[length];
			kashiUf5Cd4 = new String[length];
			kashiUf6Cd4 = new String[length];
			kashiUf7Cd4 = new String[length];
			kashiUf8Cd4 = new String[length];
			kashiUf9Cd4 = new String[length];
			kashiUf10Cd4 = new String[length];

			kashiUf1Cd5 = new String[length];
			kashiUf2Cd5 = new String[length];
			kashiUf3Cd5 = new String[length];
			kashiUf4Cd5 = new String[length];
			kashiUf5Cd5 = new String[length];
			kashiUf6Cd5 = new String[length];
			kashiUf7Cd5 = new String[length];
			kashiUf8Cd5 = new String[length];
			kashiUf9Cd5 = new String[length];
			kashiUf10Cd5 = new String[length];

			enableBumonSecurity = new boolean[length];
			
			kashiShiireKbn = new String[length];

			//--------------------
			//明細情報の設定
			//--------------------
			for (int i = 0; i < length; i++) {

				// 使用者の社員コード取得
				GMap usrInfo = bumonUsrLogic.selectUserInfo(userId[i]);
				String shainCd = (String)usrInfo.get("shain_no");
				// 法人カード識別用番号取得
				String houjinCard = (String)usrInfo.get("houjin_card_shikibetsuyou_num");

				//社員財務枝番コード取得
				//※経費立替精算に限りここで使用者ごとの財務枝番コード取得
				String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(userId[i]);

				// 使用者の代表負担部門コード
				String daihyouBumonCd = super.daihyouFutanBumonCd;
				// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
				if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userId[i]))) {
					daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userId[i]);
				}

				// 取引
// GMap shiwakeP = kaikeiLogic.findShiwakePattern(super.denpyouKbn, Integer.parseInt(shiwakeEdaNo[i]));
				ShiwakePatternMaster shiwakeP = shiwakePatternMasterDao.find(super.denpyouKbn,Integer.parseInt(shiwakeEdaNo[i]));
				

				//交際費
				kousaihiHyoujiFlg[i] = shiwakeP.kousaihiHyoujiFlg;
				ninzuuRiyouFlg[i] = shiwakeP.kousaihiNinzuuRiyouFlg;
				if (! "1".equals(kousaihiHyoujiFlg[i])) {
					kousaihiShousai[i] = "";
					kousaihiNinzuu[i] = "";
					kousaihiHitoriKingaku[i] = "";
				}else if (! "1".equals(ninzuuRiyouFlg[i])) {
					kousaihiNinzuu[i] = "";
					kousaihiHitoriKingaku[i] = "";
				}

				//借方　取引先 ※仕訳チェック用、DB登録には関係ない
				kariTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kariTorihikisakiCd) ? torihikisakiCd[i] : shiwakeP.kariTorihikisakiCd;

				//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
				Arrays.fill(enableBumonSecurity, true);
				//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
				if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd)){
					enableBumonSecurity[i] = commonLogic.checkFutanBumonWithSecurity(futanBumonCd[i], ks1.futanBumon.getName() + "コード："+ (i+1) + "行目", super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
				}

				//借方　負担部門
				futanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kariFutanBumonCd, daihyouBumonCd);

				//借方　科目
				kamokuCd[i] = shiwakeP.kariKamokuCd;
				KamokuMaster kmk = kamokuMasterDao.find(kamokuCd[i]);
				if (kmk.shoriGroup != null) {
					shoriGroup[i] = kmk.shoriGroup.toString(); //事業者区分の制御で使用
				}

				//借方　科目枝番
				String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
				switch (pKariKamokuEdabanCd) {
				//何もしない(画面入力のまま)
				case EteamConst.ShiwakeConst.EDABAN:
					break;
					//固定コード値 or ブランク
				default:
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
				kariUf1Cd[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kariUf1Cd);
				kariUf2Cd[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kariUf2Cd);
				kariUf3Cd[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kariUf3Cd);
				kariUf4Cd[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kariUf4Cd);
				kariUf5Cd[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kariUf5Cd);
				kariUf6Cd[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kariUf6Cd);
				kariUf7Cd[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kariUf7Cd);
				kariUf8Cd[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kariUf8Cd);
				kariUf9Cd[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kariUf9Cd);
				kariUf10Cd[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kariUf10Cd);

				// 借方　消費税率
				if(kazeiKbnCheck[i]) {
					String[] convZeiritsu = commonLogic.convZeiritsu(zeiritsu[i], keigenZeiritsuKbn[i], shiwakeP.kariZeiritsu, shiwakeP.kariKeigenZeiritsuKbn);
					zeiritsu[i] = convZeiritsu[0];
					keigenZeiritsuKbn[i] = convZeiritsu[1];
				}
				//事業者区分制御
				if (!("0".equals(invoiceDenpyou) &&
						((List.of("001", "002", "011", "013").contains(kazeiKbn[i]) && List.of("2","5","6","7","8","10").contains(shoriGroup[i]))
						|| shoriGroup[i].equals("21")))) {
					jigyoushaKbn[i] = "0";
				}

				//支払方法により貸方1 or 貸方2を切替する
				switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					//振込
					kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kashiTorihikisakiCd1) ? torihikisakiCd[i] : shiwakeP.kashiTorihikisakiCd1; //貸方1　取引先コード
					kashiFutanBumonCd[i] = shiwakeP.kashiFutanBumonCd1; //貸方1　負担部門コード
					kashiKamokuCd[i] = shiwakeP.kashiKamokuCd1; //貸方1　科目コード
					//貸方1　科目枝番コード
					String pKashiKamokuEdabanCd =  shiwakeP.kashiKamokuEdabanCd1; 
					this.kashiKamokuEdabanCd[i] = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
							? shainShiwakeEdaNo
							: pKashiKamokuEdabanCd;
					kashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn1; //貸方1　課税区分
					kashiShiireKbn[i] = shiwakeP.kashiShiireKbn1 != null ? (String)shiwakeP.kashiShiireKbn1 : "";//貸方1　仕入区分
					kashiUf1Cd1[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd1);//貸方1　UF1-10
					kashiUf2Cd1[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd1);
					kashiUf3Cd1[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd1);
					kashiUf4Cd1[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd1);
					kashiUf5Cd1[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd1);
					kashiUf6Cd1[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd1);
					kashiUf7Cd1[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd1);
					kashiUf8Cd1[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd1);
					kashiUf9Cd1[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd1);
					kashiUf10Cd1[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd1);
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					//現金
					kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kashiTorihikisakiCd2) ? torihikisakiCd[i] : shiwakeP.kashiTorihikisakiCd2; //貸方2　取引先コード
					kashiFutanBumonCd[i] = shiwakeP.kashiFutanBumonCd2; //貸方2　負担部門コード
					kashiKamokuCd[i] = shiwakeP.kashiKamokuCd2; //貸方2　科目コード
					kashiKamokuEdabanCd[i] = shiwakeP.kashiKamokuEdabanCd2; //貸方2　科目枝番コード
					kashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn2; //貸方2　課税区分
					kashiShiireKbn[i] = shiwakeP.kashiShiireKbn2 != null ? (String)shiwakeP.kashiShiireKbn2 : "";//貸方2　仕入区分
					kashiUf1Cd2[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd2); //貸方2　UF1-10
					kashiUf2Cd2[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd2);
					kashiUf3Cd2[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd2);
					kashiUf4Cd2[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd2);
					kashiUf5Cd2[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd2);
					kashiUf6Cd2[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd2);
					kashiUf7Cd2[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd2);
					kashiUf8Cd2[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd2);
					kashiUf9Cd2[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd2);
					kashiUf10Cd2[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd2);
					break;
				}

				//代表部門
				kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], kashiFutanBumonCd[i], daihyouBumonCd);

				// 貸方3　UF1-10
				kashiUf1Cd3[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd3);
				kashiUf2Cd3[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd3);
				kashiUf3Cd3[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd3);
				kashiUf4Cd3[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd3);
				kashiUf5Cd3[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd3);
				kashiUf6Cd3[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd3);
				kashiUf7Cd3[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd3);
				kashiUf8Cd3[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd3);
				kashiUf9Cd3[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd3);
				kashiUf10Cd3[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd3);

				// 貸方4　UF1-10
				kashiUf1Cd4[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd4);
				kashiUf2Cd4[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd4);
				kashiUf3Cd4[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd4);
				kashiUf4Cd4[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd4);
				kashiUf5Cd4[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd4);
				kashiUf6Cd4[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd4);
				kashiUf7Cd4[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd4);
				kashiUf8Cd4[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd4);
				kashiUf9Cd4[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd4);
				kashiUf10Cd4[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd4);

				// 貸方5　UF1-10
				kashiUf1Cd5[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd5);
				kashiUf2Cd5[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd5);
				kashiUf3Cd5[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd5);
				kashiUf4Cd5[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd5);
				kashiUf5Cd5[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd5);
				kashiUf6Cd5[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd5);
				kashiUf7Cd5[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd5);
				kashiUf8Cd5[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd5);
				kashiUf9Cd5[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd5);
				kashiUf10Cd5[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd5);

				//社員コードを摘要コードに反映する場合
				if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
					if(shainCd.length() > 4) {
						tekiyouCd[i] = shainCd.substring(shainCd.length()-4);
					} else {
						tekiyouCd[i] = shainCd;
					}
				//法人カード識別用番号を摘要コードに反映する場合
				} else if("T".equals(setting.houjinCardRenkei())) {
					if(houjinCard.length() > 4) {
						tekiyouCd[i] = houjinCard.substring(houjinCard.length()-4);
					} else {
						tekiyouCd[i] = houjinCard;
					}
				} else {
					tekiyouCd[i] = "";
				}
			}
		}
	}

	/**
	 * DB登録前にマスターから名称を取得する。（クライアントから送られた名称は破棄）
	 * @param connection コネクション
	 */
	protected void reloadMaster(EteamConnection connection) {

		//申請内容の項目
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

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//とりあえず画面表示のない明細項目について、領域確保
			int length = shiwakeEdaNo.length;
			kashiFutanBumonName = new String[length];
			kashiKamokuName = new String[length];
			kashiKamokuEdabanName = new String[length];

			//明細項目を１件ずつ変換
			for (int i = 0; i < length; i++) {
				futanBumonName[i] = enableBumonSecurity[i] ? masterLogic.findFutanBumonName(futanBumonCd[i]) : "";
				torihikisakiName[i] = masterLogic.findTorihikisakiName(torihikisakiCd[i]);
				kamokuName[i] = kamokuMasterDao.findKamokuNameStr(kamokuCd[i]);
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
				kashiFutanBumonName[i] = masterLogic.findFutanBumonName(kashiFutanBumonCd[i]);
				kashiKamokuName[i] = kamokuMasterDao.findKamokuNameStr(kashiKamokuCd[i]);
				kashiKamokuEdabanName[i] = masterLogic.findKamokuEdabanName(kashiKamokuCd[i], kashiKamokuEdabanCd[i]);
			}
		}
	}

	/**
	 * ユーザーに対して仮払案件があるか判定する。
	 * @param connection コネクション
	 */
	protected void  judgeKaribaraiAnken(EteamConnection connection) {
		userKaribaraiUmuFlg = "0";
		// 子画面の初期表示イベントに合わせて渡す。
		List<GMap> karibaraiUmuList = kaikeiLogic.loadKaribaraiAnken(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, "", super.getKihyouUserId());

		if(! karibaraiUmuList.isEmpty()){
			userKaribaraiUmuFlg = "1";
		}
	}

	/**
	 * 経費入力が必須か判定する。
	 * @return true:必須／false:不要
	 */
	protected boolean judgeKeihiEntry() {
		return (isNotEmpty(karibaraiMishiyouFlg) && karibaraiMishiyouFlg.equals("1")) ? false : true;
	}

	/**
	 * 支払日のチェックを行う。エラーがあれば、エラーリストにメッセージが詰まる。
	 * @param shiharai 支払日
	 * @param keijou 計上日
	 * @param keijouName 計上日項目名
	 */
	protected void checkShiharaiBi(String shiharai, String keijou, String keijouName) {
		String pShiharaihouhou = (0 < toDecimal(sashihikiShikyuuKingaku).compareTo(BigDecimal.ZERO)) ? shiharaihouhou : SHIHARAI_HOUHOU.GENKIN;//差引支給金額が0以下なら（つまり本当に振込があるなら）支払方法と関係なく当日支払日許容とする。
		commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), toDate(keijou), keijouName, pShiharaihouhou, loginUserInfo, errorList);
	}

	/**
	 * 領収書が必要かのチェックを行う。
	 * 経費立替精算 申請内容の「領収書・請求書等」がありの場合
	 * @return  領収書が必要か
	 */
	protected boolean isUseShouhyouShorui(){
		for (int i = 0; i < shouhyouShorui.length; i++) {
			if("1".equals(shouhyouShorui[i])){
				return true;
			}
		}
		return false;
	}

	/**
	 * 経費立替清算EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		KeihiTatekaeSeisanXlsLogic keihitatekaeseisanxsLogic = EteamContainer.getComponent(KeihiTatekaeSeisanXlsLogic.class, connection);
		keihitatekaeseisanxsLogic.makeExcel(denpyouId, out);
	}

	/**
	 * 代理起票できるかのチェック
	 */
	protected void dairiCheck() {
		//代理起票できるかチェック
		if (dairiFlg.equals("1")) {
			//代理権限ないユーザーはできない（メニューで非表示だがURL直接来られた時制御が破綻するし）
			GMap userJouhouMap = buLogic.selectUserJouhou(getUser().getSeigyoUserId());
			String userDairiKihyouFlg = (String)userJouhouMap.get("dairikihyou_flg");
			if (userDairiKihyouFlg.equals("0")) {
				throw new EteamAccessDeniedException("代理起票権限ないユーザーでの代理起票は不可能");
			}
			//代行モードで代理起票させない（メニューで非表示だがURL直接来られた時制御が破綻するし）
			if (! getUser().getLoginUserId().equals(getUser().getSeigyoUserId())) {
				throw new EteamAccessDeniedException("代行モードでの代理起票は不可能");
			}
		}
	}
	
	/**
	 * @return 経費精算Dto
	 */
	protected Keihiseisan createDto()
	{
		Keihiseisan keihiseisan = new Keihiseisan();
		keihiseisan.denpyouId = this.denpyouId;
		keihiseisan.karibaraiDenpyouId = this.karibaraiDenpyouId;
		keihiseisan.karibaraiOn = this.karibaraiOn;
		keihiseisan.karibaraiMishiyouFlg = (super.isEmpty(karibaraiMishiyouFlg) ? "0" : karibaraiMishiyouFlg);
		keihiseisan.dairiflg = this.dairiFlg;
		keihiseisan.keijoubi = super.toDate(keijoubi);
		keihiseisan.shiharaibi = super.toDate(shiharaibi);
		keihiseisan.shiharaikiboubi = super.toDate(shiharaiKiboubi);
		keihiseisan.shiharaihouhou = this.shiharaihouhou;
		keihiseisan.hontaiKingakuGoukei = super.toDecimal(hontaiKingakuGoukei);
		keihiseisan.shouhizeigakuGoukei = super.toDecimal(shouhizeigakuGoukei);
		keihiseisan.shiharaiKingakuGoukei = super.toDecimal(kingaku);
		keihiseisan.houjinCardRiyouKingaku = super.toDecimal(houjinCardRiyouGoukei);
		keihiseisan.kaishaTehaiKingaku = super.toDecimal(kaishaTehaiGoukei);
		keihiseisan.sashihikiShikyuuKingaku = super.toDecimal(sashihikiShikyuuKingaku);
		keihiseisan.hf1Cd = this.hf1Cd;
		keihiseisan.hf1NameRyakushiki = this.hf1Name;
		keihiseisan.hf2Cd = this.hf2Cd;
		keihiseisan.hf2NameRyakushiki = this.hf2Name;
		keihiseisan.hf3Cd = this.hf3Cd;
		keihiseisan.hf3NameRyakushiki = this.hf3Name;
		keihiseisan.hf4Cd = this.hf4Cd;
		keihiseisan.hf4NameRyakushiki = this.hf4Name;
		keihiseisan.hf5Cd = this.hf5Cd;
		keihiseisan.hf5NameRyakushiki = this.hf5Name;
		keihiseisan.hf6Cd = this.hf6Cd;
		keihiseisan.hf6NameRyakushiki = this.hf6Name;
		keihiseisan.hf7Cd = this.hf7Cd;
		keihiseisan.hf7NameRyakushiki = this.hf7Name;
		keihiseisan.hf8Cd = this.hf8Cd;
		keihiseisan.hf8NameRyakushiki = this.hf8Name;
		keihiseisan.hf9Cd = this.hf9Cd;
		keihiseisan.hf9NameRyakushiki = this.hf9Name;
		keihiseisan.hf10Cd = this.hf10Cd;
		keihiseisan.hf10NameRyakushiki = this.hf10Name;
		keihiseisan.hosoku = this.hosoku;
		keihiseisan.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		keihiseisan.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		keihiseisan.invoiceDenpyou = this.invoiceDenpyou;
		return keihiseisan;
	}
	
	/**
	 * @param i 明細番号
	 * @return 経費明細Dto
	 */
	protected KeihiseisanMeisai createMeisaiDto(int i)
	{
		GMap usrInfo = bumonUsrLogic.selectUserInfo(userId[i]);
		String shainCd = denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN) ? (String)usrInfo.get("shain_no") : "";
		String userSei = denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN) ? (String)usrInfo.get("user_sei") : "";
		String userMei = denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN) ? (String)usrInfo.get("user_mei") : "";
		KeihiseisanMeisai keihiseisanMeisai = new KeihiseisanMeisai();
		keihiseisanMeisai.denpyouId = this.denpyouId;
		keihiseisanMeisai.denpyouEdano = i + 1;
		keihiseisanMeisai.shiwakeEdano = Integer.parseInt(shiwakeEdaNo[i]);
		keihiseisanMeisai.userId = this.userId[i];
		keihiseisanMeisai.shainNo = shainCd;
		keihiseisanMeisai.userSei = userSei;
		keihiseisanMeisai.userMei = userMei;
		keihiseisanMeisai.shiyoubi = super.toDate(shiyoubi[i]);
		keihiseisanMeisai.shouhyouShoruiFlg = this.shouhyouShorui[i];
		keihiseisanMeisai.torihikiName = this.torihikiName[i];
		keihiseisanMeisai.tekiyou = this.tekiyou[i];
		keihiseisanMeisai.zeiritsu = kazeiKbnCheck[i] ? super.toDecimal(zeiritsu[i]): BigDecimal.ZERO;
		keihiseisanMeisai.keigenZeiritsuKbn = super.isEmpty(keigenZeiritsuKbn[i]) ? "0" : this.keigenZeiritsuKbn[i];
		keihiseisanMeisai.shiharaiKingaku = super.toDecimal(shiharaiKingaku[i]);
		keihiseisanMeisai.hontaiKingaku = super.toDecimal(hontaiKingaku[i]);
		keihiseisanMeisai.shouhizeigaku = super.toDecimal(shouhizeigaku[i]);
		keihiseisanMeisai.houjinCardRiyouFlg = this.houjinCardFlgKeihi[i];
		keihiseisanMeisai.kaishaTehaiFlg = this.kaishaTehaiFlgKeihi[i];
		keihiseisanMeisai.kousaihiShousaiHyoujiFlg = this.kousaihiHyoujiFlg[i];
		keihiseisanMeisai.kousaihiNinzuuRiyouFlg = this.ninzuuRiyouFlg[i];
		keihiseisanMeisai.kousaihiShousai = this.kousaihiShousai[i];
		keihiseisanMeisai.kousaihiNinzuu = super.isEmpty(kousaihiNinzuu[i]) ? null : Integer.parseInt(kousaihiNinzuu[i]);
		keihiseisanMeisai.kousaihiHitoriKingaku = super.toDecimal(kousaihiHitoriKingaku[i]);
		keihiseisanMeisai.kariFutanBumonCd = this.futanBumonCd[i];
		keihiseisanMeisai.kariFutanBumonName = this.futanBumonName[i];
		keihiseisanMeisai.torihikisakiCd = this.torihikisakiCd[i];
		keihiseisanMeisai.torihikisakiNameRyakushiki = this.torihikisakiName[i];
		keihiseisanMeisai.kariKamokuCd = this.kamokuCd[i];
		keihiseisanMeisai.kariKamokuName = this.kamokuName[i];
		keihiseisanMeisai.kariKamokuEdabanCd = this.kamokuEdabanCd[i];
		keihiseisanMeisai.kariKamokuEdabanName = this.kamokuEdabanName[i];
		keihiseisanMeisai.kariKazeiKbn = this.kazeiKbn[i];
		keihiseisanMeisai.kashiFutanBumonCd = this.kashiFutanBumonCd[i];
		keihiseisanMeisai.kashiFutanBumonName = this.kashiFutanBumonName[i];
		keihiseisanMeisai.kashiKamokuCd = this.kashiKamokuCd[i];
		keihiseisanMeisai.kashiKamokuName = this.kashiKamokuName[i];
		keihiseisanMeisai.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd[i];
		keihiseisanMeisai.kashiKamokuEdabanName = this.kashiKamokuEdabanName[i];
		keihiseisanMeisai.kashiKazeiKbn = this.kashiKazeiKbn[i];
		keihiseisanMeisai.uf1Cd = this.uf1Cd[i];
		keihiseisanMeisai.uf1NameRyakushiki = this.uf1Name[i];
		keihiseisanMeisai.uf2Cd = this.uf2Cd[i];
		keihiseisanMeisai.uf2NameRyakushiki = this.uf2Name[i];
		keihiseisanMeisai.uf3Cd = this.uf3Cd[i];
		keihiseisanMeisai.uf3NameRyakushiki = this.uf3Name[i];
		keihiseisanMeisai.uf4Cd = this.uf4Cd[i];
		keihiseisanMeisai.uf4NameRyakushiki = this.uf4Name[i];
		keihiseisanMeisai.uf5Cd = this.uf5Cd[i];
		keihiseisanMeisai.uf5NameRyakushiki = this.uf5Name[i];
		keihiseisanMeisai.uf6Cd = this.uf6Cd[i];
		keihiseisanMeisai.uf6NameRyakushiki = this.uf6Name[i];
		keihiseisanMeisai.uf7Cd = this.uf7Cd[i];
		keihiseisanMeisai.uf7NameRyakushiki = this.uf7Name[i];
		keihiseisanMeisai.uf8Cd = this.uf8Cd[i];
		keihiseisanMeisai.uf8NameRyakushiki = this.uf8Name[i];
		keihiseisanMeisai.uf9Cd = this.uf9Cd[i];
		keihiseisanMeisai.uf9NameRyakushiki = this.uf9Name[i];
		keihiseisanMeisai.uf10Cd = this.uf10Cd[i];
		keihiseisanMeisai.uf10NameRyakushiki = this.uf10Name[i];
		keihiseisanMeisai.projectCd = this.projectCd[i];
		keihiseisanMeisai.projectName = this.projectName[i];
		keihiseisanMeisai.segmentCd = this.segmentCd[i];
		keihiseisanMeisai.segmentNameRyakushiki = this.segmentName[i];
		keihiseisanMeisai.tekiyouCd = this.tekiyouCd[i];
		keihiseisanMeisai.himodukeCardMeisai = super.isEmpty(himodukeCardMeisaiKeihi[i]) ? null : Long.parseLong(himodukeCardMeisaiKeihi[i]);
		keihiseisanMeisai.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		keihiseisanMeisai.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		keihiseisanMeisai.jigyoushaKbn = this.jigyoushaKbn[i];
		keihiseisanMeisai.shiharaisakiName = this.shiharaisakiName[i];
		keihiseisanMeisai.bunriKbn = this.bunriKbn[i];
		keihiseisanMeisai.kariShiireKbn = this.kariShiireKbn[i];
		keihiseisanMeisai.kashiShiireKbn = this.kashiShiireKbn[i];
		return keihiseisanMeisai;
	}
}
