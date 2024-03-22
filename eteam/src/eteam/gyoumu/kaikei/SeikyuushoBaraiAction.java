package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.BumonMasterAbstractDao;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KeijoubiShimebiAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.SeikyuushobaraiAbstractDao;
import eteam.database.abstractdao.SeikyuushobaraiMeisaiAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KeijoubiShimebiDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.SeikyuushobaraiDao;
import eteam.database.dao.SeikyuushobaraiMeisaiDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.KamokuEdabanZandaka;
import eteam.database.dto.KamokuMaster;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.Seikyuushobarai;
import eteam.database.dto.SeikyuushobaraiMeisai;
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
@Getter
@Setter
@ToString
public class SeikyuushoBaraiAction extends WorkflowEventControl {

	//＜定数＞

	//＜画面入力＞

	// 画面入力（申請内容）
	/** 計上日 */
	String keijoubi;
	/** 支払期限 */
	String shiharaiKigen;
	/** 支払日 */
	String shiharaibi;
	/** マスター参照 */
	String masrefFlg;
	/** 領収書・請求書等 */
	String shouhyouShoruiFlg;
	/** 掛け */
	String hontaiKakeFlg;
	/** 本体金額合計 */
	String hontaiKingakuGoukei;
	/** 消費税額合計 */
	String shouhizeigakuGoukei;
	/** 税 入力初期値 */
	String nyuryokuflg;
	/** 税 入力方式の変更 */
	String zeiHenkouflg;
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 共通部分交際費注記 */
	String chuukiKousai1;
	// 支払金額合計は親クラスで定義しているkingakuを使用する
	/** 捕捉 */
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
	/** 入力方式 */
	String nyuryokuHoushiki;
	/** インボイス伝票 */
	//String invoiceDenpyou;

	// 画面入力（明細）
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
	/** 振込先 */
	String[] furikomisakiJouhou;
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
	/** 伝票摘要注記 */
	String[] chuuki2;
	/** 伝票交際費注記 */
	String[] chuukiKousai2;
	/** 交際費表示フラグ */
	String[] kousaihiHyoujiFlg;
	/** 人数項目表示フラグ */
	String[] ninzuuRiyouFlg;
	/** 交際費詳細 */
	String[] kousaihiShousai;
	/** 交際費人数 */
	String[] kousaihiNinzuu;
	/** 交際費一人あたり金額 */
	String[] kousaihiHitoriKingaku;
	/** 税抜金額 */
	String[] hontaiKingaku;
	/** 消費税額 */
	String[] shouhizeigaku;
	/** 分離区分 */
	String[] bunriKbn;
	/** 借方仕入区分 */
	String[] kariShiireKbn;
	/** 貸方仕入区分 */
	String[] kashiShiireKbn;
	/** 事業者区分 */
	String[] jigyoushaKbn;
	/** 処理グループ */
	String[] shoriGroup;

	//＜画面入力以外＞

	// プルダウン等の候補値
	/** 消費税率DropDownList */
	List<GMap> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<GMap> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
	/** 計上日の選択候補 */
	List<String> keijoubiList;
	/** CSVアップロードフラグ */
	boolean csvUploadFlag = false;
	/** 外部呼出しユーザーId */
	String gaibuKoushinUserId;
	/** 入力方式DropDownList */
	List<GMap> nyuryokuHoushikiList;
	/** 入力方式ドメイン */
	String[] nyuryokuHoushikiDomain;
	/** インボイス対応伝票DropDownList */
	List<NaibuCdSetting> invoiceDenpyouList;
	/** インボイス対応伝票ドメイン */
	String[] invoiceDenpyouDomain;
	/** 事業者区分DropDownList */
	List<GMap> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;
	/** 分離区分DropDownList */
	List<GMap> bunriKbnList;
	/** 分離区分ドメイン */
	String[] bunriKbnDomain;
	/** 仕入区分DropDownList */
	List<GMap> shiireKbnList;
	/** 仕入区分ドメイン */
	String[] shiireKbnDomain;
	/**	税込or税抜ならtrue */
	boolean[] kazeiKbnCheck;

	// 画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();

	/** 画面項目制御クラス(申請内容） */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SEIKYUUSHO_BARAI);

	/** 画面項目制御クラス(明細） */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.SEIKYUUSHO_BARAI);

	/** 入力モード */
	boolean enableInput;
	/** 計上日表示モード(0:非表示、1:入力可、2:表示) */
	int keijouBiMode;
	/** 支払日表示モード(0:非表示、1:入力可、2:表示) */
	int shiharaiBiMode;
	/** 計上日未登録 */
	boolean keijoubiMitouroku = true;
	/** 支払日未登録 */
	boolean shiharaibiMitouroku = true;
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** 申請者の計上日入力 */
	boolean shinseishaKeijoubiInput = setting.seikyuuKeijouNyuurohyku().equals("1");
	/** 計上日 締日制限 */
	boolean keijoubiSeigen = setting.seikyuuKeijouSeigen().equals("1");

	// 明細単位制御情報
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

	// 明細登録時の領域
	/** 借方取引先コード */
	String[] kariTorihikisakiCd;
	/** 借方プロジェクトコード */
	String[] kariProjectCd;
	/** 借方セグメントコード */
	String[] kariSegmentCd;
	/** 借方 UF1コード */
	String[] kariUf1Cd;
	/** 借方 UF2コード */
	String[] kariUf2Cd;
	/** 借方 UF3コード */
	String[] kariUf3Cd;
	/** 借方 UF4コード */
	String[] kariUf4Cd;
	/** 借方 UF5コード */
	String[] kariUf5Cd;
	/** 借方 UF6コード */
	String[] kariUf6Cd;
	/** 借方 UF7コード */
	String[] kariUf7Cd;
	/** 借方 UF8コード */
	String[] kariUf8Cd;
	/** 借方 UF9コード */
	String[] kariUf9Cd;
	/** 借方 UF10コード */
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
	/** 貸方プロジェクトコード */
	String[] kashiProjectCd;
	/** 貸方セグメントコード */
	String[] kashiSegmentCd;
	/** 貸方1 UF1コード */
	String[] kashiUf1Cd1;
	/** 貸方1 UF2コード */
	String[] kashiUf2Cd1;
	/** 貸方1 UF3コード */
	String[] kashiUf3Cd1;
	/** 貸方1 UF4コード */
	String[] kashiUf4Cd1;
	/** 貸方1 UF5コード */
	String[] kashiUf5Cd1;
	/** 貸方1 UF6コード */
	String[] kashiUf6Cd1;
	/** 貸方1 UF7コード */
	String[] kashiUf7Cd1;
	/** 貸方1 UF8コード */
	String[] kashiUf8Cd1;
	/** 貸方1 UF9コード */
	String[] kashiUf9Cd1;
	/** 貸方1 UF10コード */
	String[] kashiUf10Cd1;
	/** 貸方2 UF1コード */
	String[] kashiUf1Cd2;
	/** 貸方2 UF2コード */
	String[] kashiUf2Cd2;
	/** 貸方2 UF3コード */
	String[] kashiUf3Cd2;
	/** 貸方2 UF4コード */
	String[] kashiUf4Cd2;
	/** 貸方2 UF5コード */
	String[] kashiUf5Cd2;
	/** 貸方2 UF6コード */
	String[] kashiUf6Cd2;
	/** 貸方2 UF7コード */
	String[] kashiUf7Cd2;
	/** 貸方2 UF8コード */
	String[] kashiUf8Cd2;
	/** 貸方2 UF9コード */
	String[] kashiUf9Cd2;
	/** 貸方2 UF10コード */
	String[] kashiUf10Cd2;

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
	/** 掛け貸方プロジェクトコード */
	String[] kakeKashiProjectCd;
	/** 掛け貸方セグメントコード */
	String[] kakeKashiSegmentCd;
	/** 摘要コード */
	String[] tekiyouCd;

	//＜部品＞
	/** 請求書払いロジック */
	SeikyuushoBaraiLogic seikyuushoLogic;
	/** 請求書払いDao */
	SeikyuushobaraiAbstractDao seikyuushobaraiDao;
	/** 請求書払い明細Dao */
	SeikyuushobaraiMeisaiAbstractDao seikyuushobaraiMeisaiDao;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
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
	/** 科目枝番残高マスタ */
	KamokuEdabanZandakaAbstractDao edabanZandaka;
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;
	/** 負担部門マスタDao */
	BumonMasterAbstractDao bumonMasterDao;

	//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
		// 項目 //項目名 //キー項目？
		checkDate(shiharaiKigen, ks.shiharaiKigen.getName(), false);
		checkDomain(shouhyouShoruiFlg, EteamConst.Domain.FLG, ks.shouhyouShoruiFlg.getName(), false);
		checkString(hf1Cd, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString(hf1Name, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString(hf2Cd, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString(hf2Name, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString(hf3Cd, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString(hf3Name, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString(hf4Cd, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString(hf4Name, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString(hf5Cd, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString(hf5Name, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString(hf6Cd, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString(hf6Name, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString(hf7Cd, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString(hf7Name, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString(hf8Cd, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString(hf8Name, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString(hf9Cd, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString(hf9Name, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString(hf10Cd, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkString(hf10Name, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkDomain(hontaiKakeFlg, EteamConst.Domain.FLG, ks.kakeFlg.getName(), false);
		checkKingakuOver0(hontaiKingakuGoukei, "税抜金額合計", false);
		checkKingakuOver0(shouhizeigakuGoukei, "消費税額合計", false);
		checkKingakuOver1(kingaku, ks.shiharaiKingakuGoukei.getName(), false);
		checkString(hosoku, 1, 240, ks.hosoku.getName(), false);
		checkDate(keijoubi, "計上日", false);
		checkDate(shiharaibi, "支払日", false);
		checkDomain(masrefFlg, EteamConst.Domain.FLG, "マスター参照", false);
		checkDomain(this.nyuryokuHoushiki, this.nyuryokuHoushikiDomain, ks.nyuryokuHoushiki.getName(), false);
		checkDomain(this.invoiceDenpyou, this.invoiceDenpyouDomain, "インボイス対応伝票", false);
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;
			checkNumber(shiwakeEdaNo[i], 1, 10, ks.torihiki.getName() + "コード：" + ip + "行目", false);
			checkString(torihikiName[i], 1, 20, ks.torihiki.getName() + "名：" + ip + "行目", false);
			checkString(kamokuCd[i], 1, 6, ks.kamoku.getName() + "コード：" + ip + "行目", false);
			checkString(kamokuName[i], 1, 22, ks.kamoku.getName() + "名：" + ip + "行目", false);
			checkDomain(kazeiKbn[i], kazeiKbnDomain, ks.kazeiKbn.getName() + "：" + ip + "行目", false);
			//課税区分が税込or税抜系の場合のみチェック
			if (kazeiKbnCheck[i]) {
				checkDomain(zeiritsu[i], zeiritsuDomain, ks.zeiritsu.getName() + "：" + ip + "行目", false);
				checkDomain(keigenZeiritsuKbn[i], Domain.FLG, "軽減税率区分：" + ip + "行目", false);
			}

			checkString(kamokuEdabanCd[i], 1, 12, ks.kamokuEdaban.getName() + "コード：" + ip + "行目", false);
			checkString(kamokuEdabanName[i], 1, 20, ks.kamokuEdaban.getName() + "名：" + ip + "行目", false);
			checkString(futanBumonCd[i], 1, 8, ks.futanBumon.getName() + "コード：" + ip + "行目", false);
			checkString(futanBumonName[i], 1, 20, ks.futanBumon.getName() + "名：" + ip + "行目", false);
			checkString(torihikisakiCd[i], 1, 12, ks.torihikisaki.getName() + "コード" + ip + "行目", false);
			checkString(torihikisakiName[i], 1, 20, ks.torihikisaki.getName() + "名" + ip + "行目", false);
			checkString(furikomisakiJouhou[i], 1, 150, ks.furikomisakiJouhou.getName() + ip + "行目", false);
			checkString(projectCd[i], 1, 12, ks.project.getName() + "コード：" + ip + "行目", false);
			checkString(projectName[i], 1, 20, ks.project.getName() + "名：" + ip + "行目", false);
			checkString(segmentCd[i], 1, 8, ks.segment.getName() + "コード：" + ip + "行目", false);
			checkString(segmentName[i], 1, 20, ks.segment.getName() + "名：" + ip + "行目", false);
			checkString(uf1Cd[i], 1, 20, hfUfSeigyo.getUf1Name() + "：" + ip + "行目", false);
			checkString(uf1Name[i], 1, 20, hfUfSeigyo.getUf1Name() + "：" + ip + "行目", false);
			checkString(uf2Cd[i], 1, 20, hfUfSeigyo.getUf2Name() + "：" + ip + "行目", false);
			checkString(uf2Name[i], 1, 20, hfUfSeigyo.getUf2Name() + "：" + ip + "行目", false);
			checkString(uf3Cd[i], 1, 20, hfUfSeigyo.getUf3Name() + "：" + ip + "行目", false);
			checkString(uf3Name[i], 1, 20, hfUfSeigyo.getUf3Name() + "：" + ip + "行目", false);
			checkString(uf4Cd[i], 1, 20, hfUfSeigyo.getUf4Name() + "：" + ip + "行目", false);
			checkString(uf4Name[i], 1, 20, hfUfSeigyo.getUf4Name() + "：" + ip + "行目", false);
			checkString(uf5Cd[i], 1, 20, hfUfSeigyo.getUf5Name() + "：" + ip + "行目", false);
			checkString(uf5Name[i], 1, 20, hfUfSeigyo.getUf5Name() + "：" + ip + "行目", false);
			checkString(uf6Cd[i], 1, 20, hfUfSeigyo.getUf6Name() + "：" + ip + "行目", false);
			checkString(uf6Name[i], 1, 20, hfUfSeigyo.getUf6Name() + "：" + ip + "行目", false);
			checkString(uf7Cd[i], 1, 20, hfUfSeigyo.getUf7Name() + "：" + ip + "行目", false);
			checkString(uf7Name[i], 1, 20, hfUfSeigyo.getUf7Name() + "：" + ip + "行目", false);
			checkString(uf8Cd[i], 1, 20, hfUfSeigyo.getUf8Name() + "：" + ip + "行目", false);
			checkString(uf8Name[i], 1, 20, hfUfSeigyo.getUf8Name() + "：" + ip + "行目", false);
			checkString(uf9Cd[i], 1, 20, hfUfSeigyo.getUf9Name() + "：" + ip + "行目", false);
			checkString(uf9Name[i], 1, 20, hfUfSeigyo.getUf9Name() + "：" + ip + "行目", false);
			checkString(uf10Cd[i], 1, 20, hfUfSeigyo.getUf10Name() + "：" + ip + "行目", false);
			checkString(uf10Name[i], 1, 20, hfUfSeigyo.getUf10Name() + "：" + ip + "行目", false);
			checkKingakuOver1(shiharaiKingaku[i], ks.shiharaiKingaku.getName() + "：" + ip + "行目", false);
			checkKingakuOver1(hontaiKingaku[i], "本体金額：" + ip + "行目", false);
			checkKingakuOver0(shouhizeigaku[i], "消費税額：" + ip + "行目", false);
			checkString(tekiyou[i], 1, this.getIntTekiyouMaxLength(), ks.tekiyou.getName() + "：" + ip + "行目", false);
			checkSJIS(tekiyou[i], ks.tekiyou.getName() + "：" + ip + "行目");
			checkString(kousaihiShousai[i], 1, 240, ks.kousaihiShousai.getName() + "：" + ip + "行目", false);
			checkNumberOver1(kousaihiNinzuu[i], 1, 6, "交際費人数：" + ip + "行目", false);
			checkKingakuOver0(kousaihiHitoriKingaku[i], "交際費一人当たりの金額：" + ip + "行目", false);
			checkDomain(this.jigyoushaKbn[i], this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
			checkDomain(this.bunriKbn[i], this.bunriKbnDomain, ks.bunriKbn.getName() + "：" + ip + "行目", false);
			checkDomain(this.kariShiireKbn[i], this.shiireKbnDomain, ks.shiireKbn.getName() + "：" + ip + "行目", false);
			checkKingakuMinus(this.hontaiKingaku[i], ks1.zeinukiKingaku.getName() + "：" + ip + "行目", false);
			checkKingakuMinus(this.shouhizeigaku[i], ks1.shouhizeigaku.getName() + "：" + ip + "行目", false);
		}
	}

	/**
	 * 伝票内部項目の必須チェック
	 * 
	 * @param eventNum 1:登録/更新 2:CSVからの登録
	 *                 ※本来listにてevent毎にチェック有無を設定させる運用だが、ソースが見づらくなるためここではeventNumによる分岐で制御
	 */
	protected void denpyouHissuCheck(int eventNum) {
		String[][] list = {
				// 項目 項目名 必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{ shiharaiKigen, ks.shiharaiKigen.getName(), ks.shiharaiKigen.getHissuFlgS() },
				{ shouhyouShoruiFlg, ks.shouhyouShoruiFlg.getName(), ks.shouhyouShoruiFlg.getHissuFlgS() },
				{ nyuryokuHoushiki, "入力方式", "1" }, { invoiceDenpyou, "インボイス伝票", "1" },
				{ hontaiKakeFlg, ks.kakeFlg.getName(), (eventNum == 1 ? ks.kakeFlg.getHissuFlgS() : "0") },
				{ hontaiKingakuGoukei, "税抜金額合計", "1" }, { shouhizeigakuGoukei, "消費税額合計", "1" },
				{ kingaku, ks.shiharaiKingakuGoukei.getName(), ks.shiharaiKingakuGoukei.getHissuFlgS() },
				{ keijoubi, "計上日", keijouBiMode == 1 ? "1" : "0" },
				{ hosoku, ks.hosoku.getName(), (eventNum == 1 ? ks.hosoku.getHissuFlgS() : "0") }, };
		hissuCheckCommon(list, 1);

		if (!hfUfSeigyo.getHf1ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf1Cd, hfUfSeigyo.getHf1Name() + "コード", ks.hf1.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf2ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf2Cd, hfUfSeigyo.getHf2Name() + "コード", ks.hf2.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf3ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf3Cd, hfUfSeigyo.getHf3Name() + "コード", ks.hf3.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf4ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf4Cd, hfUfSeigyo.getHf4Name() + "コード", ks.hf4.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf5ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf5Cd, hfUfSeigyo.getHf5Name() + "コード", ks.hf5.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf6ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf6Cd, hfUfSeigyo.getHf6Name() + "コード", ks.hf6.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf7ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf7Cd, hfUfSeigyo.getHf7Name() + "コード", ks.hf7.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf8ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf8Cd, hfUfSeigyo.getHf8Name() + "コード", ks.hf8.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf9ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf9Cd, hfUfSeigyo.getHf9Name() + "コード", ks.hf9.getHissuFlgS() }, }, 1);
		if (!hfUfSeigyo.getHf10ShiyouFlg().equals("0"))
			hissuCheckCommon(new String[][] { { hf10Cd, hfUfSeigyo.getHf10Name() + "コード", ks.hf10.getHissuFlgS() }, },
					1);

		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;
			list = new String[][] {
					// 項目 項目名 必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{ shiwakeEdaNo[i], ks.torihiki.getName() + "コード：" + ip + "行目", ks.torihiki.getHissuFlgS() },
					{ torihikiName[i], ks.torihiki.getName() + "名：" + ip + "行目",
							(eventNum == 1 ? ks.torihiki.getHissuFlgS() : "0") },
					{ kamokuCd[i], ks.kamoku.getName() + "コード：" + ip + "行目",
							(eventNum == 1 ? ks.kamoku.getHissuFlgS() : "0") },
					{ kamokuName[i], ks.kamoku.getName() + "名：" + ip + "行目",
							(eventNum == 1 ? ks.kamoku.getHissuFlgS() : "0") },
					{ kamokuEdabanCd[i], ks.kamokuEdaban.getName() + "コード：" + ip + "行目",
							ks.kamokuEdaban.getHissuFlgS() },
					{ kamokuEdabanName[i], ks.kamokuEdaban.getName() + "名：" + ip + "行目",
							(eventNum == 1 ? ks.kamokuEdaban.getHissuFlgS() : "0") },
					{ futanBumonCd[i], ks.futanBumon.getName() + "コード：" + ip + "行目", ks.futanBumon.getHissuFlgS() },
					{ futanBumonName[i], ks.futanBumon.getName() + "名：" + ip + "行目",
							(eventNum == 1 ? ks.futanBumon.getHissuFlgS() : "0") },
					{ torihikisakiCd[i], ks.torihikisaki.getName() + "コード：" + ip + "行目",
							ks.torihikisaki.getHissuFlgS() },
					{ torihikisakiName[i], ks.torihikisaki.getName() + "名：" + ip + "行目",
							(eventNum == 1 ? ks.torihikisaki.getHissuFlgS() : "0") },
					{ furikomisakiJouhou[i], ks.furikomisakiJouhou.getName() + "：" + ip + "行目",
							(eventNum == 1 ? ks.furikomisakiJouhou.getHissuFlgS() : "0") },
					{ projectCd[i], ks.project.getName() + "コード：" + ip + "行目",
							(!"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg()) ? ks.project.getHissuFlgS() : "0" },
					{ projectName[i], ks.project.getName() + "名：" + ip + "行目",
							(!"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() && eventNum == 1)
									? ks.project.getHissuFlgS()
									: "0" },
					{ segmentCd[i], ks.segment.getName() + "コード：" + ip + "行目",
							(!"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg()) ? ks.segment.getHissuFlgS()
									: "0",
							"0" },
					{ segmentName[i], ks.segment.getName() + "名：" + ip + "行目",
							(!"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() && eventNum == 1)
									? ks.segment.getHissuFlgS()
									: "0",
							"0" },
					{ kazeiKbnCheck[i] ? zeiritsu[i] : "0", ks.zeiritsu.getName() + "：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0" },
					{ kazeiKbnCheck[i] ? keigenZeiritsuKbn[i] : "0", "軽減税率区分" + "：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0" },
					{ shiharaiKingaku[i], ks.shiharaiKingaku.getName() + "：" + ip + "行目",
							ks.shiharaiKingaku.getHissuFlgS() },
					{ tekiyou[i], ks.tekiyou.getName() + "：" + ip + "行目", ks.tekiyou.getHissuFlgS() },
					{ kousaihiShousai[i], ks.kousaihiShousai.getName() + "：" + ip + "行目",
							kousaihiEnable[i] ? ks.kousaihiShousai.getHissuFlgS() : "0" },
					{ kousaihiNinzuu[i], "交際費人数：" + ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0",
							"0" },
					{ kousaihiHitoriKingaku[i], "交際費一人当たりの金額：" + ip + "行目",
							(kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0" },
					{ jigyoushaKbn[i], ks.jigyoushaKbn.getName() + "：" + ip + "行目", ks.jigyoushaKbn.getHissuFlgS() },
					{ bunriKbn[i], ks.bunriKbn.getName() + "：" + ip + "行目", (eventNum == 1 ? ks.bunriKbn.getHissuFlgS() : "0") },
					{ kariShiireKbn[i], ks.shiireKbn.getName() + "：" + ip + "行目", (eventNum == 1 ? ks.shiireKbn.getHissuFlgS() : "0") } };
			hissuCheckCommon(list, 1);

			if (!hfUfSeigyo.getUf1ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf1Cd[i], hfUfSeigyo.getUf1Name() + "コード" + "：" + ip + "行目", ks.uf1.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf2ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf2Cd[i], hfUfSeigyo.getUf2Name() + "コード" + "：" + ip + "行目", ks.uf2.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf3ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf3Cd[i], hfUfSeigyo.getUf3Name() + "コード" + "：" + ip + "行目", ks.uf3.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf4ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf4Cd[i], hfUfSeigyo.getUf4Name() + "コード" + "：" + ip + "行目", ks.uf4.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf5ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf5Cd[i], hfUfSeigyo.getUf5Name() + "コード" + "：" + ip + "行目", ks.uf5.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf6ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf6Cd[i], hfUfSeigyo.getUf6Name() + "コード" + "：" + ip + "行目", ks.uf6.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf7ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf7Cd[i], hfUfSeigyo.getUf7Name() + "コード" + "：" + ip + "行目", ks.uf7.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf8ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf8Cd[i], hfUfSeigyo.getUf8Name() + "コード" + "：" + ip + "行目", ks.uf8.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf9ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf9Cd[i], hfUfSeigyo.getUf9Name() + "コード" + "：" + ip + "行目", ks.uf9.getHissuFlgS() }, }, 1);
			if (!hfUfSeigyo.getUf10ShiyouFlg().equals("0"))
				hissuCheckCommon(new String[][] {
						{ uf10Cd[i], hfUfSeigyo.getUf10Name() + "コード" + "：" + ip + "行目", ks.uf10.getHissuFlgS() }, },
						1);
		}
	}

	/**
	 * 必須チェック・形式チェック以外の入力チェック=相関チェック
	 */
	protected void soukanCheck() {

		// 支払日チェック
		if (getWfSeigyo().isKoushin() && commonLogic.isKeiriOrLastShounin(denpyouId, loginUserInfo)) {
			// 共通のチェック(
			if (isNotEmpty(shiharaibi)) {
				checkShiharaiBi(shiharaibi, keijoubi);
				if (0 < errorList.size())
					return;
			}
			// 請求書固有
			if ("1".equals(hontaiKakeFlg)) {
				// 掛けありなら、支払日とマスター参照チェックの両方未入力または両方入力済はエラー。両方入力済はスクリプト制御上ありえない。
				if (isEmpty(shiharaibi) && isEmpty(masrefFlg))
					errorList.add(ks.kakeFlg.getName() + "ありの場合、支払日を入力するか取引先マスター参照をチェックしてください。");
				if (isNotEmpty(shiharaibi) && isNotEmpty(masrefFlg))
					errorList.add(ks.kakeFlg.getName() + "ありの場合、支払日を入力するか取引先マスター参照をチェックしてください。");
			} else {
				// 掛けなしなら、支払日必須。マスター参照は入力不可能。
				if (isEmpty(shiharaibi))
					errorList.add("支払日を入力してください。");
				if (isNotEmpty(masrefFlg))
					errorList.add(ks.kakeFlg.getName() + "なしの場合、マスター参照のチェックは不可能です。");
			}
		}

		// 計上日のチェック
		if ((getWfSeigyo().isTouroku() || getWfSeigyo().isShinsei()) && keijoubiSeigen && isNotEmpty(keijoubi)) {
			Date shimebi = this.keijoubiShimebiDao.findMaxShimebiForDenpyouKbn(DENPYOU_KBN.SEIKYUUSHO_BARAI);
			if (shimebi != null && !toDate(keijoubi).after(shimebi)) {
				errorList.add("計上日には締日(" + formatDate(shimebi) + ")より後を入力してください。");
			}
		}

		// 掛けの混在チェック
		Map<String, String> kakeFlgMap = new HashMap<String, String>();
		for (String tmpShiwakeEdaNo : shiwakeEdaNo) {
			GMap shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI,
					Integer.parseInt(tmpShiwakeEdaNo)).map;
			kakeFlgMap.put((String) shiwakePattern.get("kake_flg"), "");
		}
		if (2 <= kakeFlgMap.size())
			errorList.add(ks.kakeFlg.getName() + "ありと" + ks.kakeFlg.getName() + "なしの" + ks.torihiki.getName()
					+ "を混在させることはできません。");

		// 伝票HF部チェック
		ShiwakeCheckData shiwakeCheckDataNaiyou = commonLogic.new ShiwakeCheckData();
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

		// 明細単位の仕訳チェック
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;

			// 仕訳パターン
			ShiwakePatternMaster shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI,
					Integer.parseInt(shiwakeEdaNo[i]));

			// 税率チェック
			if (kazeiKbnCheck[i]) {
				commonLogic.checkZeiritsu(kazeiKbn[i], toDecimal(zeiritsu[i]), keigenZeiritsuKbn[i], toDate(keijoubi), errorList, ip + "行目：");
			}

			// 借方
			ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData();
			shiwakeCheckData.torihikisakiNm = ip + "行目：" + ks.torihikisaki.getName() + "コード";
			shiwakeCheckData.torihikisakiCd = kariTorihikisakiCd[i];
			shiwakeCheckData.shiwakeEdaNoNm = ip + "行目：" + ks.torihiki.getName() + "コード";
			shiwakeCheckData.shiwakeEdaNo = shiwakeEdaNo[i];
			shiwakeCheckData.kamokuNm = ip + "行目：" + ks.kamoku.getName() + "コード";
			shiwakeCheckData.kamokuCd = kamokuCd[i];
			shiwakeCheckData.kamokuEdabanNm = ip + "行目：" + ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckData.kamokuEdabanCd = kamokuEdabanCd[i];
			shiwakeCheckData.futanBumonNm = ip + "行目：" + ks.futanBumon.getName() + "コード";
			shiwakeCheckData.futanBumonCd = futanBumonCd[i];
			shiwakeCheckData.projectNm = ip + "行目：" + ks.project.getName() + "コード";
			if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kariProjectCd))
				shiwakeCheckData.projectCd = kariProjectCd[i];
			shiwakeCheckData.segmentNm = ip + "行目：" + ks.segment.getName() + "コード";
			if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kariSegmentCd))
				shiwakeCheckData.segmentCd = kariSegmentCd[i];
			shiwakeCheckData.kazeiKbnNm = ip + "行目：" + ks.kazeiKbn.getName();
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
				shiwakeCheckData.uf1Cd = kariUf1Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf2Cd))
				shiwakeCheckData.uf2Cd = kariUf2Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf3Cd))
				shiwakeCheckData.uf3Cd = kariUf3Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf4Cd))
				shiwakeCheckData.uf4Cd = kariUf4Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf5Cd))
				shiwakeCheckData.uf5Cd = kariUf5Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf6Cd))
				shiwakeCheckData.uf6Cd = kariUf6Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf7Cd))
				shiwakeCheckData.uf7Cd = kariUf7Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf8Cd))
				shiwakeCheckData.uf8Cd = kariUf8Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf9Cd))
				shiwakeCheckData.uf9Cd = kariUf9Cd[i];
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kariUf10Cd))
				shiwakeCheckData.uf10Cd = kariUf10Cd[i];
			commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckData,
					super.daihyouFutanBumonCd, errorList);

			// 貸方
			ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData();
			String kashiNo = ("1".equals(hontaiKakeFlg)) ? "2" : "1";
			shiwakeCheckDataKashi.torihikisakiNm = ip + "行目：貸方" + ks.torihikisaki.getName() + "コード";
			shiwakeCheckDataKashi.torihikisakiCd = kashiTorihikisakiCd[i];
			shiwakeCheckDataKashi.shiwakeEdaNoNm = ip + "行目：" + ks.torihiki.getName() + "コード";
			shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo[i];
			shiwakeCheckDataKashi.kamokuNm = ip + "行目：貸方" + ks.kamoku.getName() + "コード" + kashiNo;
			shiwakeCheckDataKashi.kamokuCd = kashiKamokuCd[i];
			shiwakeCheckDataKashi.kamokuEdabanNm = ip + "行目：貸方" + ks.kamokuEdaban.getName() + "コード" + kashiNo;
			shiwakeCheckDataKashi.kamokuEdabanCd = kashiKamokuEdabanCd[i];
			shiwakeCheckDataKashi.futanBumonNm = ip + "行目：貸方" + ks.futanBumon.getName() + "コード" + kashiNo;
			shiwakeCheckDataKashi.futanBumonCd = kashiFutanBumonCd[i];
			shiwakeCheckDataKashi.kazeiKbnNm = ip + "行目：貸方" + ks.kazeiKbn.getName() + kashiNo;
			shiwakeCheckDataKashi.kazeiKbn = kashiKazeiKbn[i];
			shiwakeCheckDataKashi.projectNm = ip + "行目：貸方" + ks.project.getName() + "コード";
			shiwakeCheckDataKashi.segmentNm = ip + "行目：貸方" + ks.segment.getName() + "コード";
			shiwakeCheckDataKashi.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
			shiwakeCheckDataKashi.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
			shiwakeCheckDataKashi.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
			shiwakeCheckDataKashi.uf4Nm = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
			shiwakeCheckDataKashi.uf5Nm = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
			shiwakeCheckDataKashi.uf6Nm = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
			shiwakeCheckDataKashi.uf7Nm = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
			shiwakeCheckDataKashi.uf8Nm = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
			shiwakeCheckDataKashi.uf9Nm = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
			shiwakeCheckDataKashi.uf10Nm = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();
			if (EteamConst.ShiwakeConst.PROJECT.equals(
					"1".equals(hontaiKakeFlg) ? shiwakePattern.kashiProjectCd2 : shiwakePattern.kashiProjectCd1))
				shiwakeCheckDataKashi.projectCd = kashiProjectCd[i];
			if (EteamConst.ShiwakeConst.SEGMENT.equals(
					"1".equals(hontaiKakeFlg) ? shiwakePattern.kashiSegmentCd2 : shiwakePattern.kashiSegmentCd1))
				shiwakeCheckDataKashi.segmentCd = kashiSegmentCd[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf1Cd2 : shiwakePattern.kashiUf1Cd1))
				shiwakeCheckDataKashi.uf1Cd = "1".equals(hontaiKakeFlg) ? kashiUf1Cd2[i] : kashiUf1Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf2Cd2 : shiwakePattern.kashiUf2Cd1))
				shiwakeCheckDataKashi.uf2Cd = "1".equals(hontaiKakeFlg) ? kashiUf2Cd2[i] : kashiUf2Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf3Cd2 : shiwakePattern.kashiUf3Cd1))
				shiwakeCheckDataKashi.uf3Cd = "1".equals(hontaiKakeFlg) ? kashiUf3Cd2[i] : kashiUf3Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf4Cd2 : shiwakePattern.kashiUf4Cd1))
				shiwakeCheckDataKashi.uf4Cd = "1".equals(hontaiKakeFlg) ? kashiUf4Cd2[i] : kashiUf4Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf5Cd2 : shiwakePattern.kashiUf5Cd1))
				shiwakeCheckDataKashi.uf5Cd = "1".equals(hontaiKakeFlg) ? kashiUf5Cd2[i] : kashiUf5Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf6Cd2 : shiwakePattern.kashiUf6Cd1))
				shiwakeCheckDataKashi.uf6Cd = "1".equals(hontaiKakeFlg) ? kashiUf6Cd2[i] : kashiUf6Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf7Cd2 : shiwakePattern.kashiUf7Cd1))
				shiwakeCheckDataKashi.uf7Cd = "1".equals(hontaiKakeFlg) ? kashiUf7Cd2[i] : kashiUf7Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf8Cd2 : shiwakePattern.kashiUf8Cd1))
				shiwakeCheckDataKashi.uf8Cd = "1".equals(hontaiKakeFlg) ? kashiUf8Cd2[i] : kashiUf8Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf9Cd2 : shiwakePattern.kashiUf9Cd1))
				shiwakeCheckDataKashi.uf9Cd = "1".equals(hontaiKakeFlg) ? kashiUf9Cd2[i] : kashiUf9Cd1[i];
			if (EteamConst.ShiwakeConst.UF
					.equals("1".equals(hontaiKakeFlg) ? shiwakePattern.kashiUf10Cd2 : shiwakePattern.kashiUf10Cd1))
				shiwakeCheckDataKashi.uf10Cd = "1".equals(hontaiKakeFlg) ? kashiUf10Cd2[i] : kashiUf10Cd1[i];

			commonLogic.checkShiwake(denpyouKbn,
					"1".equals(hontaiKakeFlg) ? SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2
							: SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1,
					shiwakeCheckDataKashi, super.daihyouFutanBumonCd, errorList);

			// 貸方（掛け払い）
			if ("1".equals(hontaiKakeFlg)) {
				shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData();
				shiwakeCheckDataKashi.torihikisakiNm = ip + "行目：貸方" + ks.torihikisaki.getName() + "コード";
				shiwakeCheckDataKashi.torihikisakiCd = kakeKashiTorihikisakiCd[i];
				shiwakeCheckDataKashi.shiwakeEdaNoNm = ip + "行目：" + ks.torihiki.getName() + "コード";
				shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo[i];
				shiwakeCheckDataKashi.kamokuNm = ip + "行目：貸方" + ks.kamoku.getName() + "コード1";
				shiwakeCheckDataKashi.kamokuCd = kakeKashiKamokuCd[i];
				shiwakeCheckDataKashi.kamokuEdabanNm = ip + "行目：貸方" + ks.kamokuEdaban.getName() + "コード1";
				shiwakeCheckDataKashi.kamokuEdabanCd = kakeKashiKamokuEdabanCd[i];
				shiwakeCheckDataKashi.futanBumonNm = ip + "行目：貸方" + ks.futanBumon.getName() + "コード1";
				shiwakeCheckDataKashi.futanBumonCd = kakeKashiFutanBumonCd[i];
				shiwakeCheckDataKashi.kazeiKbnNm = ip + "行目：貸方" + ks.kazeiKbn.getName() + "1";
				shiwakeCheckDataKashi.kazeiKbn = kakeKashiKazeiKbn[i];
				shiwakeCheckDataKashi.projectNm = ip + "行目：貸方" + ks.project.getName() + "コード";
				shiwakeCheckDataKashi.segmentNm = ip + "行目：貸方" + ks.segment.getName() + "コード";
				if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd1))
					shiwakeCheckDataKashi.projectCd = kakeKashiProjectCd[i];
				if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd1))
					shiwakeCheckDataKashi.segmentCd = kakeKashiSegmentCd[i];
				shiwakeCheckDataKashi.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
				shiwakeCheckDataKashi.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
				shiwakeCheckDataKashi.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
				shiwakeCheckDataKashi.uf4Nm = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
				shiwakeCheckDataKashi.uf5Nm = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
				shiwakeCheckDataKashi.uf6Nm = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
				shiwakeCheckDataKashi.uf7Nm = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
				shiwakeCheckDataKashi.uf8Nm = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
				shiwakeCheckDataKashi.uf9Nm = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
				shiwakeCheckDataKashi.uf10Nm = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd1))
					shiwakeCheckDataKashi.uf1Cd = kashiUf1Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd1))
					shiwakeCheckDataKashi.uf2Cd = kashiUf2Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd1))
					shiwakeCheckDataKashi.uf3Cd = kashiUf3Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd1))
					shiwakeCheckDataKashi.uf4Cd = kashiUf4Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd1))
					shiwakeCheckDataKashi.uf5Cd = kashiUf5Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd1))
					shiwakeCheckDataKashi.uf6Cd = kashiUf6Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd1))
					shiwakeCheckDataKashi.uf7Cd = kashiUf7Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd1))
					shiwakeCheckDataKashi.uf8Cd = kashiUf8Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd1))
					shiwakeCheckDataKashi.uf9Cd = kashiUf9Cd1[i];
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd1))
					shiwakeCheckDataKashi.uf10Cd = kashiUf10Cd1[i];
				commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi,
						super.daihyouFutanBumonCd, errorList);
			}

			// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
			if (isNotEmpty(torihikisakiCd[i])) {
				ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
				torihikisaki.torihikisakiNm = ip + "行目：" + ks.torihikisaki.getName() + "コード";
				torihikisaki.torihikisakiCd = torihikisakiCd[i];
				commonLogic.checkTorihikisaki(torihikisaki, errorList);

				// 取引先仕入先チェック
				super.checkShiiresaki(ip + "行目：" + ks.torihikisaki.getName() + "コード", torihikisakiCd[i], denpyouKbn);
			}
		}
		// 交際費が入力されている場合基準値を満たしているかチェック
		List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.SEIKYUUSHO_BARAI, shiwakeEdaNo,
				kousaihiHitoriKingaku, null, commonLogic.KOUSAI_ERROR_CD_ERROR);
		for (GMap res : resultList) {
			if (res.get("errCd") != null && (int) res.get("errCd") == commonLogic.KOUSAI_ERROR_CD_ERROR)
				errorList.add((String) res.get("errMsg"));
		}

	}

	//＜伝票共通から呼ばれるイベント処理＞
	// 個別伝票について表示処理を行う。
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		// 参照フラグ(ture:参照起票である、false:参照起票でない)
		boolean sanshou = false;

		// 社員コード取得
		GMap userInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (userInfo == null) ? "" : (String) userInfo.get("shain_no");

		// 新規起票時の表示状態作成
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

			if (ks.shouhyouShoruiFlg.getHyoujiFlg())
				shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA003();

			nyuryokuHoushiki = setting.zeiDefaultA003();
			invoiceDenpyou = setInvoiceFlgWhenNew();
			// 登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			Seikyuushobarai shinseiData = this.seikyuushobaraiDao.find(denpyouId);
			shinseiData2Gamen(shinseiData);

			List<SeikyuushobaraiMeisai> meisaiList = this.seikyuushobaraiMeisaiDao.loadByDenpyouId(denpyouId);
			meisaiData2Gamen(meisaiList, sanshou, connection);

			// 参照起票の表示状態作成
		} else {
			sanshou = true;
			Seikyuushobarai shinseiData = this.seikyuushobaraiDao.find(sanshouDenpyouId);
			shinseiData2Gamen(shinseiData);

			List<SeikyuushobaraiMeisai> meisaiList = this.seikyuushobaraiMeisaiDao.loadByDenpyouId(sanshouDenpyouId);
			meisaiData2Gamen(meisaiList, sanshou, connection);

			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			if ("HF1".equals(shainCdRenkeiArea)) {
				hf1Cd = initShainCd;
			}
			if ("HF2".equals(shainCdRenkeiArea)) {
				hf2Cd = initShainCd;
			}
			if ("HF3".equals(shainCdRenkeiArea)) {
				hf3Cd = initShainCd;
			}
			if ("HF4".equals(shainCdRenkeiArea)) {
				hf4Cd = initShainCd;
			}
			if ("HF5".equals(shainCdRenkeiArea)) {
				hf5Cd = initShainCd;
			}
			if ("HF6".equals(shainCdRenkeiArea)) {
				hf6Cd = initShainCd;
			}
			if ("HF7".equals(shainCdRenkeiArea)) {
				hf7Cd = initShainCd;
			}
			if ("HF8".equals(shainCdRenkeiArea)) {
				hf8Cd = initShainCd;
			}
			if ("HF9".equals(shainCdRenkeiArea)) {
				hf9Cd = initShainCd;
			}
			if ("HF10".equals(shainCdRenkeiArea)) {
				hf10Cd = initShainCd;
			}

			// 日付はブランク
			shiharaiKigen = "";
		}

		// 表示制御（プルダウンとかの取得は表示方法によらず同じ）
		displaySeigyo(connection);

		// 支払日未登録で支払日登録可能な時のデフォルト値セット
		if (1 == shiharaiBiMode && shiharaibiMitouroku) {
			// 取引先マスタ参照する設定、かつ掛けありの時
			if (setting.seikyuuMasref().equals("1") && "1".equals(hontaiKakeFlg)) {
				masrefFlg = "1";
				this.seikyuushobaraiDao.updateShiharaibi(denpyouId, toDate(keijoubi), null, "1", loginUserId);
				if (!isEmpty(keijoubi)) {
					shiharaibiMitouroku = false;
				}
			} else {
				shiharaibi = shiharaiKigen;
				if (!shinseishaKeijoubiInput && "0".equals(hontaiKakeFlg)) {
					keijoubi = shiharaibi;
				}
			}
		}

		// 計上日未登録で申請者が計上日を入力する設定ならデフォルト値（システム日）をセット
		if (shinseishaKeijoubiInput && isEmpty(super.denpyouId)) {
			keijoubi = formatDate(new Date(System.currentTimeMillis()));
		}
	}

	// 登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);
		if (0 < errorList.size())
			return;

		// 必須チェック・形式チェック
		denpyouFormatCheck();
		if (!csvUploadFlag) {
			// 画面登録からの必須チェック
			denpyouHissuCheck(1);
		} else {
			// CSV登録からの必須チェック
			denpyouHissuCheck(2);
		}
		if (0 < errorList.size())
			return;

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		// マスター等から名称は引き直す
		reloadMaster(connection);

		// 相関チェック
		soukanCheck();
		if (0 < errorList.size())
			return;

		// CSV一括登録の際に起案伝票IDが入力されている場合、起案添付できる伝票IDかチェックする。
		if (csvUploadFlag && isNotEmpty(getKianDenpyouId()[0])) {
			checkKianDenpyouId();
			if (0 < errorList.size())
				return;
		}
	}

	// 更新ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		// 初期値が会社設定に依るため、一括登録のThreadクラスでなくここでセットすることにした
		if (csvUploadFlag)
			shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA003();

		// 申請内容登録
		Seikyuushobarai dto = this.createDto();
		this.seikyuushobaraiDao.insert(dto, dto.tourokuUserId);

		// 明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			SeikyuushobaraiMeisai meisaiDto = this.createMeisaiDto(i);
			this.seikyuushobaraiMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
		}

		// 追加登録アクション
		this.tourokuAdditionalAction(connection);
	}

	/**
	 * 登録時のカスタマイズ用追加アクション
	 */
	protected void tourokuAdditionalAction(EteamConnection connection)
	{
	}

	// 更新ボタン押下時に、個別伝票について以下を行う。
	// ・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	// ・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		// 必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 < errorList.size())
			return;

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		// マスター等から名称は引き直す
		reloadMaster(connection);

		// 相関チェック
		soukanCheck();
		if (0 < errorList.size())
			return;

		// 申請内容更新
		Seikyuushobarai dto = this.createDto();
		dto.koushinUserId = super.getUser().getTourokuOrKoushinUserId(); // こっちは登録と異なり外部を見ないこと固定なので上書き
		this.seikyuushobaraiDao.update(dto, dto.koushinUserId);

		// 明細削除
		this.seikyuushobaraiMeisaiDao.deleteByDenpyouId(this.denpyouId);

		// 明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			SeikyuushobaraiMeisai meisaiDto = this.createMeisaiDto(i);
			meisaiDto.koushinUserId = super.getUser().getTourokuOrKoushinUserId(); // こっちは登録と異なり外部を見ないこと固定なので上書き
			this.seikyuushobaraiMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
		}
	}

	// 承認ボタン押下時に、個別伝票について以下を行う。
	// ・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 再表示用
		displaySeigyo(connection);

		// 支払日登録可能ユーザーで未登録ならエラー（画面上入力があっても、それはシステムがデフォルト表示しただけで、DBは未登録だったりする）
		if (commonLogic.isKeiriOrLastShounin(denpyouId, loginUserInfo)) {
			if (keijoubiMitouroku && shiharaibiMitouroku) {
				errorList.add("計上日/支払日が未登録です。\r\n計上日/支払日登録を行ってから承認してください。");
			} else if (keijoubiMitouroku) {
				// ※計上日だけ未登録はないはず
				errorList.add("計上日が未登録です。\r\n計上日登録を行ってから承認してください。");
			} else if (shiharaibiMitouroku) {
				errorList.add("支払日が未登録です。\r\n支払日登録を行ってから承認してください。");
			}
		}

		// 実際にDBに登録されている値でチェック
		Seikyuushobarai dto = this.seikyuushobaraiDao.find(denpyouId);
		String shiharai = formatDate(dto.shiharaibi);
		String keijou = formatDate(dto.keijoubi);
		// 共通の支払日チェック
		if (isNotEmpty(shiharai) && commonLogic.isKeiriOrLastShounin(denpyouId, loginUserInfo)) {
			checkShiharaiBi(shiharai, keijou);
		}
	}

	// 登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData) {
		BigDecimal zandaka = (BigDecimal) ringiData.get("ringiKingakuZandaka");
		ringiData.put("ringiKingakuZandaka", zandaka.subtract(toDecimal(kingaku)));
	}

	/**
	 * 基準日の取得
	 * 
	 * @return 基準日
	 */
	@Override
	public String judgeKijunbi() {
		if (shinseishaKeijoubiInput) {
			return keijoubi;
		} else {
			return super.judgeKijunbi();
		}
	}

	// 会社設定で指定されたルールに則り、代表仕訳枝番号を決定する。
	@Override
	protected String getDaihyouTorihiki() {
		String ret = shiwakeEdaNo[0];
		String torihikiSentakuRule = EteamSettingInfo.getSettingInfo("route_hantei_torihiki_sentaku_rule");
		if (torihikiSentakuRule.equals(EteamConst.torihikiSentakuRule.KINGAKU_MAX)) {
			int maxIndex = 0;
			BigDecimal maxKingaku = toDecimal(shiharaiKingaku[maxIndex]);
			for (int i = 1; i < shiwakeEdaNo.length; i++) {
				if (maxKingaku.compareTo(toDecimal(shiharaiKingaku[i])) < 0) {
					maxIndex = i;
					maxKingaku = toDecimal(shiharaiKingaku[i]);
				}
			}
			ret = shiwakeEdaNo[maxIndex];
		}
		return ret;
	}

	// 科目が予算執行対象かどうか判定する。
	@Override
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection connection) {

		HashSet<String> kamokuCdSet = new HashSet<String>();
		for (int i = 0; i < kamokuCd.length; i++) {
			kamokuCdSet.add(kamokuCd[i]);
		}
		if (0 == kamokuCdSet.size()) {
			return false;
		}

		if (null == commonLogic) {
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		}
		return commonLogic.isYosanShikkouKamoku(denpyouId, kamokuCdSet, kianbangouBoDialogNendo);
	}

	//＜内部処理＞
	/**
	 * 初期化処理
	 * 
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		seikyuushoLogic = EteamContainer.getComponent(SeikyuushoBaraiLogic.class, connection);
		seikyuushobaraiDao = EteamContainer.getComponent(SeikyuushobaraiDao.class, connection);
		seikyuushobaraiMeisaiDao = EteamContainer.getComponent(SeikyuushobaraiMeisaiDao.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		this.keijoubiShimebiDao = EteamContainer.getComponent(KeijoubiShimebiDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		edabanZandaka =  EteamContainer.getComponent(KamokuEdabanZandakaAbstractDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		bumonMasterDao =  EteamContainer.getComponent(BumonMasterAbstractDao.class, connection);
	}

	/**
	 * 請求書払いテーブルのレコード情報を画面項目に移す
	 * 
	 * @param shinseiData 請求書払いレコード
	 */
	protected void shinseiData2Gamen(Seikyuushobarai shinseiData) {
		this.keijoubi = super.formatDate(shinseiData.keijoubi);
		this.shiharaiKigen = super.formatDate(shinseiData.shiharaiKigen);
		this.shiharaibi = super.formatDate(shinseiData.shiharaibi);
		this.masrefFlg = shinseiData.masrefFlg;
		this.shouhyouShoruiFlg = shinseiData.shouhyouShoruiFlg;
		this.hontaiKakeFlg = shinseiData.kakeFlg;
		this.hontaiKingakuGoukei = super.formatMoney(shinseiData.hontaiKingakuGoukei);
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
		this.shouhizeigakuGoukei = super.formatMoney(shinseiData.shouhizeigakuGoukei);
		this.kingaku = super.formatMoney(shinseiData.shiharaiKingakuGoukei);
		this.hosoku = shinseiData.hosoku;
		this.nyuryokuHoushiki = shinseiData.nyuryokuHoushiki;
		this.invoiceDenpyou = shinseiData.invoiceDenpyou;
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisai() {
		// 消費税率マップ
		GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();

		// 表示項目
		shiwakeEdaNo = new String[] { "" };
		torihikiName = new String[] { "" };
		kamokuCd = new String[] { "" };
		kamokuName = new String[] { "" };
		kamokuEdabanCd = new String[] { "" };
		kamokuEdabanName = new String[] { "" };
		futanBumonCd = new String[] { "" };
		futanBumonName = new String[] { "" };
		torihikisakiCd = new String[] { "" };
		torihikisakiName = new String[] { "" };
		furikomisakiJouhou = new String[] { "" };
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
		keigenZeiritsuKbn = new String[] { initZeiritsu.get("keigen_zeiritsu_kbn") };
		shiharaiKingaku = new String[] { "" };
		hontaiKingaku = new String[] { "" };
		shouhizeigaku = new String[] { "" };
		tekiyou = new String[] { "" };
		chuuki2 = new String[] { "" };
		chuukiKousai2 = new String[] { "" };
		kousaihiShousai = new String[] { "" };
		kousaihiNinzuu = new String[] { "" };
		kousaihiHitoriKingaku = new String[] { "" };
		this.bunriKbn = new String[] { "" };
		this.kariShiireKbn = new String[] { "" };
		this.kashiShiireKbn = new String[] { "" };
		this.hontaiKingaku = new String[] { "0" };
		this.shouhizeigaku = new String[] { "0" };
		this.jigyoushaKbn = new String[] { "0" };

		// 制御情報
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
	 * 
	 * @param meisaiList 明細レコードのリスト
	 * @param sanshou    参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection コネクション
	 */
	protected void meisaiData2Gamen(List<SeikyuushobaraiMeisai> meisaiList, boolean sanshou,
			EteamConnection connection) {
		int length = meisaiList.size();
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
		furikomisakiJouhou = new String[length];
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
		tekiyou = new String[length];
		chuuki2 = new String[length];
		chuukiKousai2 = new String[length];
		kousaihiShousai = new String[length];
		kousaihiNinzuu = new String[length];
		kousaihiHitoriKingaku = new String[length];
		kousaihiHyoujiFlg = new String[length];
		ninzuuRiyouFlg = new String[length];
		jigyoushaKbn = new String[length];
		bunriKbn = new String[length];
		kariShiireKbn = new String[length];
		kashiShiireKbn = new String[length];
		for (int i = 0; i < length; i++) {
			SeikyuushobaraiMeisai meisai = meisaiList.get(i);
			this.shiwakeEdaNo[i] = Integer.toString(meisai.shiwakeEdano);
			this.torihikiName[i] = meisai.torihikiName;
			this.kamokuCd[i] = meisai.kariKamokuCd;
			this.kamokuName[i] = meisai.kariKamokuName;
			this.kamokuEdabanCd[i] = meisai.kariKamokuEdabanCd;
			this.kamokuEdabanName[i] = meisai.kariKamokuEdabanName;
			this.futanBumonCd[i] = meisai.kariFutanBumonCd;
			this.futanBumonName[i] = meisai.kariFutanBumonName;
			this.torihikisakiCd[i] = meisai.torihikisakiCd;
			this.torihikisakiName[i] = meisai.torihikisakiNameRyakushiki;
			this.furikomisakiJouhou[i] = meisai.furikomisakiJouhou;
			this.projectCd[i] = meisai.projectCd;
			this.projectName[i] = meisai.projectName;
			this.segmentCd[i] = meisai.segmentCd;
			this.segmentName[i] = meisai.segmentNameRyakushiki;
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
			this.kazeiKbn[i] = meisai.kariKazeiKbn;
			this.zeiritsu[i] = super.formatMoney(meisai.zeiritsu);
			this.keigenZeiritsuKbn[i] = meisai.keigenZeiritsuKbn;
			this.shiharaiKingaku[i] = super.formatMoney(meisai.shiharaiKingaku);
			this.hontaiKingaku[i] = super.formatMoney(meisai.hontaiKingaku);
			this.shouhizeigaku[i] = super.formatMoney(meisai.shouhizeigaku);
			this.tekiyou[i] = meisai.tekiyou;
			this.kousaihiShousai[i] = meisai.kousaihiShousai;
			this.kousaihiHyoujiFlg[i] = meisai.kousaihiShousaiHyoujiFlg;
			this.ninzuuRiyouFlg[i] = meisai.kousaihiNinzuuRiyouFlg;
			this.kousaihiNinzuu[i] = meisai.kousaihiNinzuu == null ? "" : Integer.toString(meisai.kousaihiNinzuu);
			this.kousaihiHitoriKingaku[i] = super.formatMoney(meisai.kousaihiHitoriKingaku);
			this.jigyoushaKbn[i] = meisai.jigyoushaKbn;
			this.bunriKbn[i] = meisai.bunriKbn;
			this.kariShiireKbn[i] = meisai.kariShiireKbn;
			this.kashiShiireKbn[i] = meisai.kashiShiireKbn;
			if (!sanshou) {
				String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.SEIKYUUSHO_BARAI, meisai.map, null, "0");
				String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
				if (commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)) {
					chuuki1 = commonLogic.getTekiyouChuuki();
					chuuki2[i] = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
				}
			}
		}
		if (!sanshou) {
			// 交際費関連の警告メッセージ取得
			chuukiKousai1 = "";
			List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.SEIKYUUSHO_BARAI, shiwakeEdaNo,
					kousaihiHitoriKingaku, null, commonLogic.KOUSAI_ERROR_CD_KEIKOKU);
			for (GMap res : resultList) {
				if (res.get("index") != null) {
					chuukiKousai2[(Integer) res.get("index")] = (String) res.get("errMsg");
				} else if (res.get("keikokuMsg") != null) {
					chuukiKousai1 += (isEmpty(chuukiKousai1) ? "" : "\r\n") + (String) res.get("keikokuMsg");
				}
			}
		}
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * 
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		// プルダウンのリストを取得
		zeiritsuList = this.zeiritsuDao.load().stream().map(Shouhizeiritsu::getMap).collect(Collectors.toList()); // (支払依頼より)本当はGMapもやめてDtoに移行したいが、JSPも絡んでくるので一旦これで
		zeiritsuDomain = EteamCommon.mapList2Arr(zeiritsuList, "zeiritsu");
		kazeiKbnList = this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		kazeiKbnDomain = EteamCommon.mapList2Arr(kazeiKbnList, "naibu_cd");
		bunriKbnList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		bunriKbnDomain = EteamCommon.mapList2Arr(bunriKbnList, "naibu_cd");
		shiireKbnList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		shiireKbnDomain = EteamCommon.mapList2Arr(shiireKbnList, "naibu_cd");
		nyuryokuHoushikiList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("nyuryoku_flg").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		nyuryokuHoushikiDomain = EteamCommon.mapList2Arr(nyuryokuHoushikiList, "naibu_cd");
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		jigyoushaKbnList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");

		// 入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		// 支払日の入力状態・モード(0:非表示、1:入力可、2:表示)を判定
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		// 計上日の入力状態・モード(0:非表示、1:入力可、2:表示)を判定
		if (shinseishaKeijoubiInput) {
			keijouBiMode = 2;
			if (enableInput)
				keijouBiMode = 1;
		} else {
			keijouBiMode = shiharaiBiMode;
		}
		if (keijouBiMode == 1 && setting.keijouNyuuryoku().equals("2")) {
			keijoubiList = masterLogic.loadKeijoubiList(denpyouKbn);
		}

		// 計上日・支払日未登録チェック
		if (isNotEmpty(denpyouId)) {
			Seikyuushobarai seikyuushobarai = this.seikyuushobaraiDao.find(denpyouId);
			shiharaibiMitouroku = (seikyuushobarai.shiharaibi == null && !seikyuushobarai.masrefFlg.equals("1"));
			keijoubiMitouroku = (seikyuushobarai.keijoubi == null);
		}

		// 明細単位に仕訳パターンによる制御
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
		shoriGroup = new String[length];
		kazeiKbnCheck = new boolean[length];
		for (int i = 0; i < length; i++) {
			if (enableInput) {
				if (isNotEmpty(shiwakeEdaNo[i])) {

					// ※以下はDenpyouMeisaiAction側で再定義しているため無効な制御変数

					// 初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
					// 仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
					ShiwakePatternMaster shiwakePatternMaster = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI,
							Integer.parseInt(shiwakeEdaNo[i]));
					if (shiwakePatternMaster == null) {
						errorList.add("仕訳枝番号" + shiwakeEdaNo[i] + "は存在しません。");
					} else {
						GMap shiwakePattern = shiwakePatternMaster.map;
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
						kousaihiEnable[i] = info.kousaihiEnable;
						ninzuuEnable[i] = info.ninzuuEnable;
						zeiritsuEnable[i] = info.zeiritsuEnable;
					}
					//課税区分の制御
					if (isNotEmpty(kamokuCd[i])) {
						GMap kmk = masterLogic.findKamokuMaster(kamokuCd[i]);
						if (kmk != null) {
							shoriGroup[i] = kmk.get("shori_group").toString();
						}
					}
				}
			} else {
				kousaihiEnable[i] = "1".equals(kousaihiHyoujiFlg[i]);
				ninzuuEnable[i] = "1".equals(ninzuuRiyouFlg[i]);
			}
			kazeiKbnCheck[i] = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]);
		}
	}

	/**
	 * 登録時に仕訳パターンマスターより借方貸方の値を設定する。
	 * 
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		String daihyouBumonCd = super.daihyouFutanBumonCd;

		// 社員コード取得
		GMap userInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String shainCd = (userInfo == null) ? "" : (String) userInfo.get("shain_no");

		// 掛けは全明細で統一されている前提なので、1つ目の明細でチェック
		ShiwakePatternMaster shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI,
				Integer.parseInt(shiwakeEdaNo[0]));
		hontaiKakeFlg = shiwakeP.kakeFlg;

		// 明細行数分の領域確保
		int length = shiwakeEdaNo.length;
		kousaihiHyoujiFlg = new String[length];
		ninzuuRiyouFlg = new String[length];

		kariTorihikisakiCd = new String[length];
		kariProjectCd = new String[length];
		kariSegmentCd = new String[length];

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
		kashiProjectCd = new String[length];
		kashiSegmentCd = new String[length];

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

		kakeKashiTorihikisakiCd = new String[length];
		kakeKashiFutanBumonCd = new String[length];
		kakeKashiKamokuCd = new String[length];
		kakeKashiKamokuEdabanCd = new String[length];
		kakeKashiKazeiKbn = new String[length];
		kakeKashiProjectCd = new String[length];
		kakeKashiSegmentCd = new String[length];
		tekiyouCd = new String[length];
		// kariShiireKbn = new String[length];
		kashiShiireKbn = new String[length];

		enableBumonSecurity = new boolean[length];

		// 明細１行ずつ
		for (int i = 0; i < length; i++) {
			shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI,
					Integer.parseInt(shiwakeEdaNo[i]));

			if (csvUploadFlag) {
				// 取引で貸借どちらかが任意入力でなければ取引の値で上書き
				// 取引先
				if (!(shiwakeP.kariTorihikisakiCd.equals(EteamConst.ShiwakeConst.TORIHIKI)
						|| shiwakeP.kashiTorihikisakiCd1.equals(EteamConst.ShiwakeConst.TORIHIKI)
						|| shiwakeP.kashiTorihikisakiCd2.equals(EteamConst.ShiwakeConst.TORIHIKI)
						|| shiwakeP.kashiTorihikisakiCd3.equals(EteamConst.ShiwakeConst.TORIHIKI)
						|| shiwakeP.kashiTorihikisakiCd4.equals(EteamConst.ShiwakeConst.TORIHIKI)
						|| shiwakeP.kashiTorihikisakiCd5.equals(EteamConst.ShiwakeConst.TORIHIKI))) {

					torihikisakiCd[i] = shiwakeP.kariTorihikisakiCd;
				}
				// プロジェクト
				if (!(shiwakeP.kariProjectCd.equals(EteamConst.ShiwakeConst.PROJECT)
						|| shiwakeP.kashiProjectCd1.equals(EteamConst.ShiwakeConst.PROJECT)
						|| shiwakeP.kashiProjectCd2.equals(EteamConst.ShiwakeConst.PROJECT)
						|| shiwakeP.kashiProjectCd3.equals(EteamConst.ShiwakeConst.PROJECT)
						|| shiwakeP.kashiProjectCd4.equals(EteamConst.ShiwakeConst.PROJECT)
						|| shiwakeP.kashiProjectCd5.equals(EteamConst.ShiwakeConst.PROJECT))) {
					projectCd[i] = shiwakeP.kariProjectCd;
				}
				// セグメント
				if (!(shiwakeP.kariSegmentCd.equals(EteamConst.ShiwakeConst.SEGMENT)
						|| shiwakeP.kashiSegmentCd1.equals(EteamConst.ShiwakeConst.SEGMENT)
						|| shiwakeP.kashiSegmentCd2.equals(EteamConst.ShiwakeConst.SEGMENT)
						|| shiwakeP.kashiSegmentCd3.equals(EteamConst.ShiwakeConst.SEGMENT)
						|| shiwakeP.kashiSegmentCd4.equals(EteamConst.ShiwakeConst.SEGMENT)
						|| shiwakeP.kashiSegmentCd5.equals(EteamConst.ShiwakeConst.SEGMENT))) {
					segmentCd[i] = shiwakeP.kariSegmentCd;
				}
				// UF1
				if (!(shiwakeP.kariUf1Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf1Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf1Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf1Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf1Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf1Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf1Cd[i] = shiwakeP.kariUf1Cd;
				}
				// UF2
				if (!(shiwakeP.kariUf2Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf2Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf2Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf2Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf2Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf2Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf2Cd[i] = shiwakeP.kariUf2Cd;
				}
				// UF3
				if (!(shiwakeP.kariUf3Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf3Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf3Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf3Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf3Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf3Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf3Cd[i] = shiwakeP.kariUf3Cd;
				}
				// UF4
				if (!(shiwakeP.kariUf4Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf4Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf4Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf4Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf4Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf4Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf4Cd[i] = shiwakeP.kariUf4Cd;
				}
				// UF5
				if (!(shiwakeP.kariUf5Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf5Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf5Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf5Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf5Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf5Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf5Cd[i] = shiwakeP.kariUf5Cd;
				}
				// UF6
				if (!(shiwakeP.kariUf6Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf6Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf6Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf6Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf6Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf6Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf6Cd[i] = shiwakeP.kariUf6Cd;
				}
				// UF7
				if (!(shiwakeP.kariUf7Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf7Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf7Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf7Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf7Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf7Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf7Cd[i] = shiwakeP.kariUf7Cd;
				}
				// UF8
				if (!(shiwakeP.kariUf8Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf8Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf8Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf8Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf8Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf8Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf8Cd[i] = shiwakeP.kariUf8Cd;
				}
				// UF9
				if (!(shiwakeP.kariUf9Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf9Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf9Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf9Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf9Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf9Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf9Cd[i] = shiwakeP.kariUf9Cd;
				}
				// UF10
				if (!(shiwakeP.kariUf10Cd.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf10Cd1.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf10Cd2.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf10Cd3.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf10Cd4.equals(EteamConst.ShiwakeConst.UF)
						|| shiwakeP.kashiUf10Cd5.equals(EteamConst.ShiwakeConst.UF))) {
					uf10Cd[i] = shiwakeP.kariUf10Cd;
				}
			}

			// 取引名
			torihikiName[i] = shiwakeP.torihikiName;

			// 交際費
			kousaihiHyoujiFlg[i] = shiwakeP.kousaihiHyoujiFlg;
			ninzuuRiyouFlg[i] = shiwakeP.kousaihiNinzuuRiyouFlg;
			if (!"1".equals(kousaihiHyoujiFlg[i])) {
				kousaihiShousai[i] = "";
				kousaihiNinzuu[i] = "";
				kousaihiHitoriKingaku[i] = "";
			} else if (!"1".equals(ninzuuRiyouFlg[i])) {
				kousaihiNinzuu[i] = "";
				kousaihiHitoriKingaku[i] = "";
			}

			// 借方 取引先 ※仕訳チェック用、DB登録には関係ない
			kariTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kariTorihikisakiCd) ? torihikisakiCd[i]
					: (String) shiwakeP.kariTorihikisakiCd;

			// 代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
			Arrays.fill(enableBumonSecurity, true);
			// 負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
			if (List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU)
					.contains(shiwakeP.kariFutanBumonCd)) {
				enableBumonSecurity[i] = commonLogic.checkFutanBumonWithSecurity(futanBumonCd[i],
						ks.futanBumon.getName() + "コード：" + (i + 1) + "行目",
						loginUserInfo != null ? loginUserInfo : super.getUser(), denpyouId, getKihyouBumonCd(),
						errorList);
			}
			// 借方 負担部門
			futanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kariFutanBumonCd, daihyouBumonCd);
			// 借方 科目
			kamokuCd[i] = shiwakeP.kariKamokuCd;
			KamokuMaster kmk = kamokuMasterDao.find(kamokuCd[i]);
			if (kmk.shoriGroup != null) {
				shoriGroup[i] = kmk.shoriGroup.toString(); //事業者区分の制御で使用
			}

			// 借方 科目枝番
			String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
			switch (pKariKamokuEdabanCd) {
			case EteamConst.ShiwakeConst.EDABAN:
				// 何もしない(画面入力のまま)
				break;
			default:
				// 固定コード値 or ブランク
				kamokuEdabanCd[i] = pKariKamokuEdabanCd;
				break;
			}

			// 借方 プロジェクト
			kariProjectCd[i] = commonLogic.convProject(projectCd[i], shiwakeP.kariProjectCd);

			// 借方 セグメント
			kariSegmentCd[i] = commonLogic.convSegment(segmentCd[i], shiwakeP.kariSegmentCd);

			// 借方 UF1-10
			if (shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1
					&& shiwakeP.shainCdRenkeiFlg.equals(("1"))) {
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
					uf1Cd[i] = shainCd;
				if (ufno == 2)
					uf2Cd[i] = shainCd;
				if (ufno == 3)
					uf3Cd[i] = shainCd;
				if (ufno == 4)
					uf4Cd[i] = shainCd;
				if (ufno == 5)
					uf5Cd[i] = shainCd;
				if (ufno == 6)
					uf6Cd[i] = shainCd;
				if (ufno == 7)
					uf7Cd[i] = shainCd;
				if (ufno == 8)
					uf8Cd[i] = shainCd;
				if (ufno == 9)
					uf9Cd[i] = shainCd;
				if (ufno == 10)
					uf10Cd[i] = shainCd;
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

			// 借方 課税区分 請求書払いCSVアップロード対応
			// 借方 分離区分　分離区分はそのまま登録するため仕訳パターンマスタから取得しない　csv一括の場合は仕訳パターンマスタから取得する
			// 借方 仕入区分　借方の仕入区分はそのまま登録するため仕訳パターンマスタから取得しない　csv一括の場合は仕訳パターンマスタから取得する
			if (csvUploadFlag) {
				if ("0".equals(invoiceDenpyou)) {
					if(kamokuEdabanCd[i] == null || kamokuEdabanCd[i].equals("")) {
						kazeiKbn[i] = shiwakeP.kariKazeiKbn;
					}else {
						KamokuEdabanZandaka edaban = edabanZandaka.find(kamokuCd[i], kamokuEdabanCd[i]);
						kazeiKbn[i] = edaban.getKazeiKbn() == null ? shiwakeP.kariKazeiKbn : String.format("%3s", edaban.getKazeiKbn().toString()).replace(" ","0");
					}
				} else {
					// 旧課税区分を使用する場合
					if(kamokuEdabanCd[i] == null || kamokuEdabanCd[i].equals("")) {
						kazeiKbn[i] = shiwakeP.oldKariKazeiKbn;
					}else {
						KamokuEdabanZandaka edaban = edabanZandaka.find(kamokuCd[i], kamokuEdabanCd[i]);
						kazeiKbn[i] = edaban.getKazeiKbn() == null ? shiwakeP.oldKariKazeiKbn : String.format("%3s", edaban.getKazeiKbn().toString()).replace(" ","0");
					}
				}
				bunriKbn[i] = commonLogic.edabanBunriCheck(kamokuCd[i], kamokuEdabanCd[i], shiwakeP.kariBunriKbn, kazeiKbn[i]);
				// 負担部門コードが入力されているが、負担部門マスタに値が存在しない場合
				if (StringUtils.isNotEmpty(futanBumonCd[i]) && bumonMasterDao.find(futanBumonCd[i]) == null) {
					errorList.add((i+1) +"行目：負担部門コード[" + futanBumonCd[i] + "]は存在しません。");
					return;
				}else {
					kariShiireKbn[i] = shiwakeP.kariShiireKbn == null ? "": commonLogic.bumonShiireCheck(kamokuCd[i], futanBumonCd[i], kazeiKbn[i],shiwakeP.kariShiireKbn, this.getShiireZeiAnbun());
				}
			}
			// 借方 消費税率
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
				if(EteamSettingInfo.getSettingInfo("zei_default_A003").equals("0")) {
					hontaiKingaku[i] = shiharaiKingaku[i];
				}else {
					shiharaiKingaku[i] = hontaiKingaku[i];
				}
			}
			
			//事業者区分制御
			//変更可能な条件以外は事業者区分０固定
			if (!("0".equals(invoiceDenpyou) && 
					((List.of("001", "002", "011","013").contains(kazeiKbn[i]) && List.of("2","5","6","7","8","10").contains(shoriGroup[i]))
					|| shoriGroup[i].equals("21")))) {
				jigyoushaKbn[i] = "0";
			}

			// 仕訳パターンの貸方１/２どちら？ 掛けあり：１、掛けなし：２
			String kashiNo = ("1".equals(hontaiKakeFlg)) ? "2" : "1";

			// 貸方 取引先 ※仕訳チェック用、DB登録には関係ない
			kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.map.get("kashi_torihikisaki_cd" + kashiNo))
					? torihikisakiCd[i]
					: (String) shiwakeP.map.get("kashi_torihikisaki_cd" + kashiNo);

			// 貸方 負担部門コード
			kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i],
					(String) shiwakeP.map.get("kashi_futan_bumon_cd" + kashiNo), daihyouBumonCd);

			// 貸方 科目コード
			kashiKamokuCd[i] = (String) shiwakeP.map.get("kashi_kamoku_cd" + kashiNo);

			// 貸方 科目枝番コード（ブランク or コード値）
			kashiKamokuEdabanCd[i] = (String) shiwakeP.map.get("kashi_kamoku_edaban_cd" + kashiNo);

			// 貸方 課税区分
			if ("1".equals(invoiceDenpyou)) {
				kashiKazeiKbn[i] = (String) shiwakeP.map.get("kashi_kazei_kbn" + kashiNo);
			} else {
				// 旧課税区分を使用する場合
				kashiKazeiKbn[i] = (String) shiwakeP.map.get("old_kashi_kazei_kbn" + kashiNo);
			}

			// 貸方 プロジェクト
			kashiProjectCd[i] = commonLogic.convProject(projectCd[i], shiwakeP.map.get("kashi_project_cd" + kashiNo));

			// 貸方 セグメント
			kashiSegmentCd[i] = commonLogic.convSegment(segmentCd[i], shiwakeP.map.get("kashi_segment_cd" + kashiNo));

			// 貸方 UF1-10
			if ("1".equals(hontaiKakeFlg)) {
				kashiUf1Cd2[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd2);
				kashiUf2Cd2[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd2);
				kashiUf3Cd2[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd2);
				kashiUf4Cd2[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd2);
				kashiUf5Cd2[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd2);
				kashiUf6Cd2[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd2);
				kashiUf7Cd2[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd2);
				kashiUf8Cd2[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd2);
				kashiUf9Cd2[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd2);
				kashiUf10Cd2[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd2);

			} else {
				kashiUf1Cd1[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd1);
				kashiUf2Cd1[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd1);
				kashiUf3Cd1[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd1);
				kashiUf4Cd1[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd1);
				kashiUf5Cd1[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd1);
				kashiUf6Cd1[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd1);
				kashiUf7Cd1[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd1);
				kashiUf8Cd1[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd1);
				kashiUf9Cd1[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd1);
				kashiUf10Cd1[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd1);
			}

			if ("1".equals(hontaiKakeFlg)) {

				// 掛け 貸方 取引先
				kakeKashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kashiTorihikisakiCd1)
						? torihikisakiCd[i]
						: (String) shiwakeP.kashiTorihikisakiCd1;

				// 掛け 貸方 負担部門コード
				kakeKashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kashiFutanBumonCd1,
						daihyouFutanBumonCd);

				// 掛け 貸方 科目コード
				kakeKashiKamokuCd[i] = shiwakeP.kashiKamokuCd1;

				// 掛け 貸方 科目枝番コード（ブランク or コード値）
				kakeKashiKamokuEdabanCd[i] = shiwakeP.kashiKamokuEdabanCd1;

				// 掛け 貸方 課税区分
				kakeKashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn1;

				// 掛け 貸方 プロジェクト
				kakeKashiProjectCd[i] = commonLogic.convProject(projectCd[i], shiwakeP.kashiProjectCd1);

				// 掛け 貸方 セグメント
				kakeKashiSegmentCd[i] = commonLogic.convSegment(segmentCd[i], shiwakeP.kashiSegmentCd1);

				// 貸方1 UF1-10
				kashiUf1Cd1[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd1);
				kashiUf2Cd1[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd1);
				kashiUf3Cd1[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd1);
				kashiUf4Cd1[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd1);
				kashiUf5Cd1[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd1);
				kashiUf6Cd1[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd1);
				kashiUf7Cd1[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd1);
				kashiUf8Cd1[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd1);
				kashiUf9Cd1[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd1);
				kashiUf10Cd1[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd1);

			}
			// 貸方 分離区分　分離区分に貸方はないので削除

			// 貸方 仕入区分
			kashiShiireKbn[i] = (shiwakeP.kashiShiireKbn1 != null) ? shiwakeP.kashiShiireKbn1 : "";

			// 社員コードを摘要コードに反映する場合
			if ("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
				if (shainCd.length() > 4) {
					tekiyouCd[i] = shainCd.substring(shainCd.length() - 4);
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
	 * 
	 * @param connection コネクション
	 */
	protected void reloadMaster(EteamConnection connection) {

		// 申請内容の項目
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

		// とりあえず画面表示のない明細項目について、領域確保
		int length = shiwakeEdaNo.length;
		kashiFutanBumonName = new String[length];
		kashiKamokuName = new String[length];
		kashiKamokuEdabanName = new String[length];

		// 明細項目を１件ずつ変換
		for (int i = 0; i < length; i++) {
			futanBumonName[i] = enableBumonSecurity[i] ? masterLogic.findFutanBumonName(futanBumonCd[i]) : "";
			torihikisakiName[i] = masterLogic.findTorihikisakiName(torihikisakiCd[i]);
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
			kashiFutanBumonName[i] = masterLogic.findFutanBumonName(kashiFutanBumonCd[i]);
			kashiKamokuName[i] = masterLogic.findKamokuNameStr(kashiKamokuCd[i]);
			kashiKamokuEdabanName[i] = masterLogic.findKamokuEdabanName(kashiKamokuCd[i], kashiKamokuEdabanCd[i]);

			if (csvUploadFlag) {
				furikomisakiJouhou[i] = commonLogic.furikomisakiSakusei(torihikisakiCd[i]);
			}

		}

	}

	/**
	 * 支払日のチェック（何箇所かで同じチェックするのでメンテ漏れしないようにメソッド化。 ※伝票一覧の承認チェックと同期とること！
	 * 
	 * @param shiharai 支払日
	 * @param keijou   計上日
	 */
	protected void checkShiharaiBi(String shiharai, String keijou) {
		commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), toDate(keijou), "計上日", DENPYOU_KBN.SEIKYUUSHO_BARAI,
				loginUserInfo, errorList);// 振込方法はチェック飛ばす為のダミー
	}

	/**
	 * 領収書が必要かのチェックを行う。 画面からの入力で「領収書・請求書等」が「あり」の場合
	 * 
	 * @return 領収書が必要か
	 */
	protected boolean isUseShouhyouShorui() {
		if (!csvUploadFlag) {
			if (shouhyouShoruiFlg.equals(EteamConst.Domain.FLG[0]))
				return true;
		}
		return false;
	}

	/**
	 * 請求書払い申請EXCEL出力
	 * 
	 * @param connection コネクション
	 * @param out        出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		SeikyuushoBaraiXlsLogic seikyuushobaraixlsLogic = EteamContainer.getComponent(SeikyuushoBaraiXlsLogic.class,
				connection);
		seikyuushobaraixlsLogic.makeExcel(denpyouId, out);
	}

	/**
	 * @return 請求書払いDto
	 */
	protected Seikyuushobarai createDto() {
		Seikyuushobarai seikyuushobarai = new Seikyuushobarai();
		seikyuushobarai.denpyouId = this.denpyouId;
		seikyuushobarai.keijoubi = super.toDate(keijoubi);
		seikyuushobarai.shiharaiKigen = super.toDate(shiharaiKigen);
		seikyuushobarai.shiharaibi = super.toDate(shiharaibi);
		seikyuushobarai.masrefFlg = this.masrefFlg != null ? this.masrefFlg : "0";
		seikyuushobarai.shouhyouShoruiFlg = this.shouhyouShoruiFlg;
		seikyuushobarai.kakeFlg = this.hontaiKakeFlg;
		seikyuushobarai.hontaiKingakuGoukei = super.toDecimal(hontaiKingakuGoukei);
		seikyuushobarai.shouhizeigakuGoukei = super.toDecimal(shouhizeigakuGoukei);
		seikyuushobarai.shiharaiKingakuGoukei = super.toDecimal(kingaku);
		seikyuushobarai.hf1Cd = this.hf1Cd;
		seikyuushobarai.hf1NameRyakushiki = this.hf1Name;
		seikyuushobarai.hf2Cd = this.hf2Cd;
		seikyuushobarai.hf2NameRyakushiki = this.hf2Name;
		seikyuushobarai.hf3Cd = this.hf3Cd;
		seikyuushobarai.hf3NameRyakushiki = this.hf3Name;
		seikyuushobarai.hf4Cd = this.hf4Cd;
		seikyuushobarai.hf4NameRyakushiki = this.hf4Name;
		seikyuushobarai.hf5Cd = this.hf5Cd;
		seikyuushobarai.hf5NameRyakushiki = this.hf5Name;
		seikyuushobarai.hf6Cd = this.hf6Cd;
		seikyuushobarai.hf6NameRyakushiki = this.hf6Name;
		seikyuushobarai.hf7Cd = this.hf7Cd;
		seikyuushobarai.hf7NameRyakushiki = this.hf7Name;
		seikyuushobarai.hf8Cd = this.hf8Cd;
		seikyuushobarai.hf8NameRyakushiki = this.hf8Name;
		seikyuushobarai.hf9Cd = this.hf9Cd;
		seikyuushobarai.hf9NameRyakushiki = this.hf9Name;
		seikyuushobarai.hf10Cd = this.hf10Cd;
		seikyuushobarai.hf10NameRyakushiki = this.hf10Name;
		seikyuushobarai.hosoku = this.hosoku;
		seikyuushobarai.tourokuUserId = this.gaibuKoushinUserId != null ? this.gaibuKoushinUserId
				: super.getUser().getTourokuOrKoushinUserId();
		seikyuushobarai.koushinUserId = this.gaibuKoushinUserId != null ? this.gaibuKoushinUserId
				: super.getUser().getTourokuOrKoushinUserId();
		seikyuushobarai.nyuryokuHoushiki = this.nyuryokuHoushiki;
		seikyuushobarai.invoiceDenpyou = this.invoiceDenpyou;

		return seikyuushobarai;
	}

	/**
	 * @param i 明細番号
	 * @return 請求書払いDto
	 */
	protected SeikyuushobaraiMeisai createMeisaiDto(int i) {
		SeikyuushobaraiMeisai seikyuushobaraiMeisai = new SeikyuushobaraiMeisai();
		seikyuushobaraiMeisai.denpyouId = this.denpyouId;
		seikyuushobaraiMeisai.denpyouEdano = i + 1;
		seikyuushobaraiMeisai.shiwakeEdano = Integer.parseInt(shiwakeEdaNo[i]);
		seikyuushobaraiMeisai.torihikiName = this.torihikiName[i];
		seikyuushobaraiMeisai.tekiyou = this.tekiyou[i];
		seikyuushobaraiMeisai.zeiritsu = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]) ? super.toDecimal(zeiritsu[i]) : BigDecimal.ZERO;
		seikyuushobaraiMeisai.keigenZeiritsuKbn = super.isEmpty(keigenZeiritsuKbn[i]) ? "0" : this.keigenZeiritsuKbn[i];
		seikyuushobaraiMeisai.shiharaiKingaku = super.toDecimal(shiharaiKingaku[i]);
		seikyuushobaraiMeisai.hontaiKingaku = super.toDecimal(hontaiKingaku[i]);
		seikyuushobaraiMeisai.shouhizeigaku = super.toDecimal(shouhizeigaku[i]);
		seikyuushobaraiMeisai.kousaihiShousaiHyoujiFlg = this.kousaihiHyoujiFlg[i];
		seikyuushobaraiMeisai.kousaihiNinzuuRiyouFlg = this.ninzuuRiyouFlg[i];
		seikyuushobaraiMeisai.kousaihiShousai = this.kousaihiShousai[i];
		seikyuushobaraiMeisai.kousaihiNinzuu = super.isEmpty(kousaihiNinzuu[i]) ? null
				: Integer.parseInt(kousaihiNinzuu[i]);
		seikyuushobaraiMeisai.kousaihiHitoriKingaku = super.toDecimal(kousaihiHitoriKingaku[i]);
		seikyuushobaraiMeisai.kariFutanBumonCd = this.futanBumonCd[i];
		seikyuushobaraiMeisai.kariFutanBumonName = this.futanBumonName[i];
		seikyuushobaraiMeisai.torihikisakiCd = this.torihikisakiCd[i];
		seikyuushobaraiMeisai.torihikisakiNameRyakushiki = this.torihikisakiName[i];
		seikyuushobaraiMeisai.furikomisakiJouhou = this.furikomisakiJouhou[i];
		seikyuushobaraiMeisai.kariKamokuCd = this.kamokuCd[i];
		seikyuushobaraiMeisai.kariKamokuName = this.kamokuName[i];
		seikyuushobaraiMeisai.kariKamokuEdabanCd = this.kamokuEdabanCd[i];
		seikyuushobaraiMeisai.kariKamokuEdabanName = this.kamokuEdabanName[i];
		seikyuushobaraiMeisai.kariKazeiKbn = this.kazeiKbn[i];
		seikyuushobaraiMeisai.kashiFutanBumonCd = this.kashiFutanBumonCd[i];
		seikyuushobaraiMeisai.kashiFutanBumonName = this.kashiFutanBumonName[i];
		seikyuushobaraiMeisai.kashiKamokuCd = this.kashiKamokuCd[i];
		seikyuushobaraiMeisai.kashiKamokuName = this.kashiKamokuName[i];
		seikyuushobaraiMeisai.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd[i];
		seikyuushobaraiMeisai.kashiKamokuEdabanName = this.kashiKamokuEdabanName[i];
		seikyuushobaraiMeisai.kashiKazeiKbn = this.kashiKazeiKbn[i];
		seikyuushobaraiMeisai.uf1Cd = this.uf1Cd[i];
		seikyuushobaraiMeisai.uf1NameRyakushiki = this.uf1Name[i];
		seikyuushobaraiMeisai.uf2Cd = this.uf2Cd[i];
		seikyuushobaraiMeisai.uf2NameRyakushiki = this.uf2Name[i];
		seikyuushobaraiMeisai.uf3Cd = this.uf3Cd[i];
		seikyuushobaraiMeisai.uf3NameRyakushiki = this.uf3Name[i];
		seikyuushobaraiMeisai.uf4Cd = this.uf4Cd[i];
		seikyuushobaraiMeisai.uf4NameRyakushiki = this.uf4Name[i];
		seikyuushobaraiMeisai.uf5Cd = this.uf5Cd[i];
		seikyuushobaraiMeisai.uf5NameRyakushiki = this.uf5Name[i];
		seikyuushobaraiMeisai.uf6Cd = this.uf6Cd[i];
		seikyuushobaraiMeisai.uf6NameRyakushiki = this.uf6Name[i];
		seikyuushobaraiMeisai.uf7Cd = this.uf7Cd[i];
		seikyuushobaraiMeisai.uf7NameRyakushiki = this.uf7Name[i];
		seikyuushobaraiMeisai.uf8Cd = this.uf8Cd[i];
		seikyuushobaraiMeisai.uf8NameRyakushiki = this.uf8Name[i];
		seikyuushobaraiMeisai.uf9Cd = this.uf9Cd[i];
		seikyuushobaraiMeisai.uf9NameRyakushiki = this.uf9Name[i];
		seikyuushobaraiMeisai.uf10Cd = this.uf10Cd[i];
		seikyuushobaraiMeisai.uf10NameRyakushiki = this.uf10Name[i];
		seikyuushobaraiMeisai.projectCd = this.projectCd[i];
		seikyuushobaraiMeisai.projectName = this.projectName[i];
		seikyuushobaraiMeisai.segmentCd = this.segmentCd[i];
		seikyuushobaraiMeisai.segmentNameRyakushiki = this.segmentName[i];
		seikyuushobaraiMeisai.tekiyouCd = this.tekiyouCd[i];
		seikyuushobaraiMeisai.tourokuUserId = this.gaibuKoushinUserId != null ? gaibuKoushinUserId
				: super.getUser().getTourokuOrKoushinUserId();
		seikyuushobaraiMeisai.koushinUserId = this.gaibuKoushinUserId != null ? gaibuKoushinUserId
				: super.getUser().getTourokuOrKoushinUserId();
		seikyuushobaraiMeisai.jigyoushaKbn = this.jigyoushaKbn[i];
		seikyuushobaraiMeisai.bunriKbn = this.bunriKbn[i];
		seikyuushobaraiMeisai.kariShiireKbn = this.kariShiireKbn[i];
		seikyuushobaraiMeisai.kashiShiireKbn = this.kashiShiireKbn[i];
		return seikyuushobaraiMeisai;
	}
}
