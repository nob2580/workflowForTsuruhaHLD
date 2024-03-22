package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import eteam.database.abstractdao.JidouhikiotoshiAbstractDao;
import eteam.database.abstractdao.JidouhikiotoshiMeisaiAbstractDao;
import eteam.database.abstractdao.KeijoubiShimebiAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.JidouhikiotoshiDao;
import eteam.database.dao.JidouhikiotoshiMeisaiDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KeijoubiShimebiDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.Jidouhikiotoshi;
import eteam.database.dto.JidouhikiotoshiMeisai;
import eteam.database.dto.KamokuMaster;
import eteam.database.dto.NaibuCdSetting;
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
 * 自動引落伝票画面Action
 */
@Getter @Setter @ToString
public class JidouHikiotoshiDenpyouAction extends WorkflowEventControl {

//＜定数＞

//＜画面入力＞

	//画面入力（申請内容）
	/** 引落日 */
	String hikiotoshibi;
	/** 計上日 */
	String keijoubi;
	/** 領収書・請求書等 */
	String shouhyouShoruiFlg;
	/** 本体金額合計 */
	String hontaiKingakuGoukei;
	/** 消費税額合計 */
	String shouhizeigakuGoukei;
	/** 共通部分摘要注記 */
	String chuuki1;
	// 支払金額合計は親クラスで定義しているkingakuを使用する
	/** 捕捉 */
	String hosoku;
	/** 税 入力初期値 */
	String nyuryokuflg;

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
	/** インボイス伝票 */ /*WorkflowEventControlに置いた*/
	//String invoiceDenpyou;

	//画面入力（明細）
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
	/** 支払先(取引先)コード */
	String[] torihikisakiCd;
	/** 支払先(取引先)名 */
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
	/** 支払金額 */
	String[] shiharaiKingaku;
	/** 本体金額(税抜金額) */
	String[] hontaiKingaku;
	/** 消費税額 */
	String[] shouhizeigaku;
	/** 摘要 */
	String[] tekiyou;
	/** 摘要注記 */
	String[] chuuki2;
	
	/** 事業者区分 */
	String[] jigyoushaKbn;
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
	//インボイス対応追加
	/** 分離区分リスト */
	List<GMap> bunriKbnList;
	/** 分離区分ドメイン */
	String[] bunriKbnDomain;
	/** 仕入区分リスト */
	List<GMap> shiireKbnList;
	/** 仕入区分ドメイン */
	String[] shiireKbnDomain;
	/** 入力方式DropDownList */
	List<GMap> nyuryokuHoushikiList;
	/** 入力方式ドメイン */
	String[] nyuryokuHoushikiDomain;
	/** 事業者区分List */
	List<GMap> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;
	/** インボイス対応伝票DropDownList */
	List<NaibuCdSetting> invoiceDenpyouList;
	/** インボイス対応伝票ドメイン */
	String[] invoiceDenpyouDomain;
	/**	税込or税抜ならtrue */
	boolean[] kazeiKbnCheck;
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 申請内容 */

	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();

	/** 入力モード */
	boolean enableInput;
	/** 計上日表示モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン)) */
	int keijouBiMode;
	/** 発生主義かどうか */
	boolean hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA009());
	/** 申請者の計上日入力 */
	boolean shinseishaKeijoubiInput = setting.jidouhikiKeijouNyuurohyku().equals("1");
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** 計上日 締日制限 */
	boolean keijoubiSeigen = setting.jidouhikiKeijouSeigen().equals("1");
	/**  画面項目制御クラス(申請内容） */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU);
	/**  画面項目制御クラス(明細） */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU);
	/** 計上日の選択候補 */
	List<String> keijoubiList;

	//明細単位制御情報
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean[] kamokuEdabanEnable;
	/** 負担部門選択ボタン */
	boolean[] futanBumonEnable;
	/** 取引先（支払先）選択ボタン */
	boolean[] torihikisakiEnable;
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
	
	/** 貸方仕入区分 */
	String[] kashiShiireKbn;
	
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
	/** 自動引落伝票ロジック */
	JidouHikiotoshiDenpyouLogic gyoumuLogic;
	/** 自動引落Dao */
	JidouhikiotoshiAbstractDao jidouhikiotoshiDao;
	/** 自動引落明細Dao */
	JidouhikiotoshiMeisaiAbstractDao jidouhikiotoshiMeisaiDao;
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
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;

//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
			// 項目										//項目名											//キー項目？
			checkDate (hikiotoshibi, ks.hikiotoshibi.getName(), false);
			checkDate (keijoubi, "計上日", false);
			checkDomain(shouhyouShoruiFlg, EteamConst.Domain.FLG, ks.shouhyouShoruiFlg.getName(), false);
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
			checkKingakuOver0 (hontaiKingakuGoukei, "本体金額合計", false);
			checkKingakuOver0 (shouhizeigakuGoukei, "消費税額合計", false);
			checkKingakuOver1 (kingaku, ks.shiharaiKingakuGoukei.getName(), false);
			checkString (hosoku, 1, 240, ks.hosoku.getName(), false);
			checkDomain (this.nyuryokuHoushiki, this.nyuryokuHoushikiDomain, ks.nyuryokuHoushiki.getName(), false);
			checkDomain (invoiceDenpyou, invoiceDenpyouDomain, "インボイス対応伝票", false);
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;
			checkNumber (shiwakeEdaNo[i], 1, 10, ks1.torihiki.getName() + "コード："		+ ip + "行目", false);
			checkString (torihikiName[i], 1, 20, ks1.torihiki.getName() + "名："			+ ip + "行目", false);
			checkString (kamokuCd[i], 1, 6, ks1.kamoku.getName() +"コード："			+ ip + "行目", false);
			checkString (kamokuName[i], 1, 22, ks1.kamoku.getName() + "名："				+ ip + "行目", false);
			checkString (kamokuEdabanCd[i], 1, 12, ks1.kamokuEdaban.getName() + "コード：" 	+ ip + "行目", false);
			checkString (kamokuEdabanName[i], 1, 20, ks1.kamokuEdaban.getName() + "名："		+ ip + "行目", false);
			checkString (futanBumonCd[i], 1, 8, ks1.futanBumon.getName() + "コード："		+ ip + "行目", false);
			checkString (futanBumonName[i], 1, 20, ks1.futanBumon.getName() + "名："			+ ip + "行目", false);
			checkString (torihikisakiCd[i], 1, 12, ks1.torihikisaki.getName() + "コード："	+ ip + "行目", false);
			checkString (torihikisakiName[i], 1, 20, ks1.torihikisaki.getName() + "名："		+ ip + "行目", false);
			checkString (projectCd[i], 1, 12, ks1.project.getName() + "コード："		+ ip + "行目", false);
			checkString (projectName[i], 1, 20, ks1.project.getName() + "名："			+ ip + "行目", false);
			checkString (segmentCd[i], 1, 8, ks1.segment.getName() + "コード："			+ ip + "行目", false);
			checkString (segmentName[i], 1, 20, ks1.segment.getName() + "名："				+ ip + "行目", false);
			checkString (uf1Cd[i], 1, 20, hfUfSeigyo.getUf1Name() + "："				+ ip + "行目", false);
			checkString (uf1Name[i], 1, 20, hfUfSeigyo.getUf1Name() + "："				+ ip + "行目", false);
			checkString (uf2Cd[i], 1, 20, hfUfSeigyo.getUf2Name() + "："				+ ip + "行目", false);
			checkString (uf2Name[i], 1, 20, hfUfSeigyo.getUf2Name() + "："				+ ip + "行目", false);
			checkString (uf3Cd[i], 1, 20, hfUfSeigyo.getUf3Name() + "："				+ ip + "行目", false);
			checkString (uf3Name[i], 1, 20, hfUfSeigyo.getUf3Name() + "："				+ ip + "行目", false);
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
			checkDomain (kazeiKbn[i], kazeiKbnDomain, ks1.kazeiKbn.getName() + "："				+ ip + "行目", false);
			if(kazeiKbnCheck[i]) {
				checkDomain (zeiritsu[i], zeiritsuDomain, ks1.zeiritsu.getName() + "：" 			+ ip + "行目", false);
				checkDomain (keigenZeiritsuKbn[i], Domain.FLG, "軽減税率区分："						+ ip + "行目", false);
			}
			checkKingakuOver1 (shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："		+ ip + "行目", false);
			checkKingakuOver1 (hontaiKingaku[i], "本体金額："									+ ip + "行目", false);
			checkKingakuOver0 (shouhizeigaku[i], "消費税額："									+ ip + "行目", false);
			checkString (tekiyou[i], 1, this.getIntTekiyouMaxLength(), ks1.tekiyou.getName() + "："				+ ip + "行目", false);
			checkSJIS (tekiyou[i], ks1.tekiyou.getName() + "："				+ ip + "行目");
			checkDomain (this.jigyoushaKbn[i], jigyoushaKbnDomain, ks.jigyoushaKbn.getName() + "：" + ip + "行目", false);
			checkDomain (this.bunriKbn[i], bunriKbnDomain, ks1.bunriKbn.getName() +  "：" + ip + "行目",false);
			checkDomain (this.kariShiireKbn[i], shiireKbnDomain, ks1.shiireKbn.getName() +  "：" + ip + "行目",false);
			//税抜金額＝本体金額として、税抜金額と消費税額はインボイス前から自動引落伝票に存在する
		}
	}

	/**
	 * 伝票内部項目の必須チェック
	 */
	protected void denpyouHissuCheck() {
		// E1=登録・更新
		String[][] list = {
				//項目							項目名 										必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{hikiotoshibi, ks.hikiotoshibi.getName(), ks.hikiotoshibi.getHissuFlgS()},
				{keijoubi, "計上日", keijouBiMode == 1 || keijouBiMode == 3 ? "1" : "0"},
				{shouhyouShoruiFlg, ks.shouhyouShoruiFlg.getName(), ks.shouhyouShoruiFlg.getHissuFlgS()},
				{hontaiKingakuGoukei, "本体金額合計", "1"},
				{shouhizeigakuGoukei, "消費税額合計", "1"},
				{kingaku, ks.shiharaiKingakuGoukei.getName(), ks.shiharaiKingakuGoukei.getHissuFlgS()},
				{hosoku, ks.hosoku.getName(), ks.hosoku.getHissuFlgS()},
		};
		hissuCheckCommon(list, 1);

		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード"	, ks.hf1.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード"	, ks.hf2.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード"	, ks.hf3.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード"	, ks.hf4.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード"	, ks.hf5.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード"	, ks.hf6.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード"	, ks.hf7.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード"	, ks.hf8.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード"	, ks.hf9.getHissuFlgS()},}, 1);
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード"	, ks.hf10.getHissuFlgS()},}, 1);

		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;
			list = new String[][]{
				//項目							項目名 															必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shiwakeEdaNo[i], ks1.torihiki.getName() + "コード："		+ ip + "行目", ks1.torihiki.getHissuFlgS()},
				{torihikiName[i], ks1.torihiki.getName() + "名："			+ ip + "行目", ks1.torihiki.getHissuFlgS()},
				{kamokuCd[i], ks1.kamoku.getName() + "コード："			+ ip + "行目", ks1.kamoku.getHissuFlgS()},
				{kamokuName[i], ks1.kamoku.getName() + "名："				+ ip + "行目", ks1.kamoku.getHissuFlgS()},
				{kamokuEdabanCd[i], ks1.kamokuEdaban.getName() + "コード："	+ ip + "行目", ks1.kamokuEdaban.getHissuFlgS()},
				{kamokuEdabanName[i] ,		ks1.kamokuEdaban.getName() + "名："		+ ip + "行目", ks1.kamokuEdaban.getHissuFlgS()},
				{futanBumonCd[i], ks1.futanBumon.getName() + "コード："		+ ip + "行目", ks1.futanBumon.getHissuFlgS()},
				{futanBumonName[i], ks1.futanBumon.getName() + "名："			+ ip + "行目", ks1.futanBumon.getHissuFlgS()},
				{torihikisakiCd[i], ks1.torihikisaki.getName() + "コード"	+ ip + "行目", ks1.torihikisaki.getHissuFlgS()},
				{torihikisakiName[i], ks1.torihikisaki.getName() + "名"		+ ip + "行目", ks1.torihikisaki.getHissuFlgS()},
				{projectCd[i], ks1.project.getName() + "コード："		+ ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0"},
				{projectName[i], ks1.project.getName() + "名："			+ ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0"},
				{segmentCd[i], ks1.segment.getName() + "コード："		+ ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"},
				{segmentName[i], ks1.segment.getName() + "名："			+ ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"},
				{kazeiKbnCheck[i] ? zeiritsu[i]:"0", ks1.zeiritsu.getName() + "："		+ ip + "行目",  kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0"},
				{kazeiKbnCheck[i] ? keigenZeiritsuKbn[i]:"0", "軽減税率区分" + "：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0"},
				{shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："		+ ip + "行目", ks1.shiharaiKingaku.getHissuFlgS()},
				{tekiyou[i], ks1.tekiyou.getName() + "："				+ ip + "行目", ks1.tekiyou.getHissuFlgS()},
				{jigyoushaKbn[i], ks.jigyoushaKbn.getName() + "：" + ip + "行目", ks.jigyoushaKbn.getHissuFlgS() },
				{bunriKbn[i], ks.bunriKbn.getName() + "：" + ip + "行目", ks.bunriKbn.getHissuFlgS() },
				{kariShiireKbn[i], ks.shiireKbn.getName() + "：" + ip + "行目", ks.shiireKbn.getHissuFlgS() }
			};
			hissuCheckCommon(list, 1);

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

	/**
	 * 必須チェック・形式チェック以外の入力チェック=相関チェック
	 */
	protected void soukanCheck() {

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

		//明細単位の仕訳チェック
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			int ip = i + 1;

			//旅費精算の仕訳パターン取得
			ShiwakePatternMaster shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU,Integer.parseInt(shiwakeEdaNo[i]));

			//税率チェック
			//計上日を経理で入力する設定の場合、初回登録時と更新時で行われる税率チェックの平仄を合わす
			String keijoubiTmp = keijoubi;
			if (!shinseishaKeijoubiInput)
			{
				keijoubiTmp = StringUtils.isEmpty(keijoubi) ? hikiotoshibi : keijoubi;
			}
			if(kazeiKbnCheck[i]) {
				commonLogic.checkZeiritsu(kazeiKbn[i], toDecimal(zeiritsu[i]), keigenZeiritsuKbn[i], toDate(keijoubiTmp), errorList, ip + "行目：");
			}

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
			commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckData, super.daihyouFutanBumonCd, errorList);

			// 貸方
			ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData() ;
			shiwakeCheckDataKashi.torihikisakiNm = ip + "行目：貸方" + ks1.torihikisaki.getName() + "コード";
			shiwakeCheckDataKashi.torihikisakiCd = kashiTorihikisakiCd[i];
			shiwakeCheckDataKashi.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
			shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo[i];
			shiwakeCheckDataKashi.kamokuNm = ip + "行目：貸方" + ks1.kamoku.getName() + "コード";
			shiwakeCheckDataKashi.kamokuCd = kashiKamokuCd[i];
			shiwakeCheckDataKashi.kamokuEdabanNm = ip + "行目：貸方" + ks1.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKashi.kamokuEdabanCd = kashiKamokuEdabanCd[i];
			shiwakeCheckDataKashi.futanBumonNm = ip + "行目：貸方" + ks1.futanBumon.getName() + "コード";
			shiwakeCheckDataKashi.futanBumonCd = kashiFutanBumonCd[i];
			shiwakeCheckDataKashi.kazeiKbnNm = ip + "行目：貸方" + ks1.kazeiKbn.getName();
			shiwakeCheckDataKashi.kazeiKbn = kashiKazeiKbn[i];
			shiwakeCheckDataKashi.projectNm = ip + "行目：貸方" + ks1.project.getName() + "コード";
			if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd1))
			{
				shiwakeCheckDataKashi.projectCd = projectCd[i];
			}
			shiwakeCheckDataKashi.segmentNm = ip + "行目：貸方" + ks1.segment.getName() + "コード";
			if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd1))
			{
				shiwakeCheckDataKashi.segmentCd = segmentCd[i];
			}
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
			{
				shiwakeCheckDataKashi.uf1Cd = kashiUf1Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd1))
			{
				shiwakeCheckDataKashi.uf2Cd = kashiUf2Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd1))
			{
				shiwakeCheckDataKashi.uf3Cd = kashiUf3Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd1))
			{
				shiwakeCheckDataKashi.uf4Cd = kashiUf4Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd1))
			{
				shiwakeCheckDataKashi.uf5Cd = kashiUf5Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd1))
			{
				shiwakeCheckDataKashi.uf6Cd = kashiUf6Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd1))
			{
				shiwakeCheckDataKashi.uf7Cd = kashiUf7Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd1))
			{
				shiwakeCheckDataKashi.uf8Cd = kashiUf8Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd1))
			{
				shiwakeCheckDataKashi.uf9Cd = kashiUf9Cd1[i];
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd1))
			{
				shiwakeCheckDataKashi.uf10Cd = kashiUf10Cd1[i];
			}
			commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi, super.daihyouFutanBumonCd, errorList);

			//貸方2
			ShiwakeCheckData shiwakeCheckDataKashi2 = commonLogic.new ShiwakeCheckData() ;
			if (hasseiShugi) {
				shiwakeCheckDataKashi2.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
				shiwakeCheckDataKashi2.shiwakeEdaNo = shiwakeEdaNo[i];
				shiwakeCheckDataKashi2.projectNm = ip + "行目：貸方" + ks1.project.getName() + "コード";
				if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.kashiProjectCd2))
				{
					shiwakeCheckDataKashi2.projectCd = projectCd[i];
				}
				shiwakeCheckDataKashi2.segmentNm = ip + "行目：貸方" + ks1.segment.getName() + "コード";
				if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.kashiSegmentCd2))
				{
					shiwakeCheckDataKashi2.segmentCd = segmentCd[i];
				}
				shiwakeCheckDataKashi2.uf1Nm = ip + "行目：貸方" + hfUfSeigyo.getUf1Name();
				shiwakeCheckDataKashi2.uf2Nm = ip + "行目：貸方" + hfUfSeigyo.getUf2Name();
				shiwakeCheckDataKashi2.uf3Nm = ip + "行目：貸方" + hfUfSeigyo.getUf3Name();
				shiwakeCheckDataKashi2.uf4Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf4Name();
				shiwakeCheckDataKashi2.uf5Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf5Name();
				shiwakeCheckDataKashi2.uf6Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf6Name();
				shiwakeCheckDataKashi2.uf7Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf7Name();
				shiwakeCheckDataKashi2.uf8Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf8Name();
				shiwakeCheckDataKashi2.uf9Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf9Name();
				shiwakeCheckDataKashi2.uf10Nm    = ip + "行目：貸方" + hfUfSeigyo.getUf10Name();
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf1Cd2))
				{
					shiwakeCheckDataKashi2.uf1Cd = kashiUf1Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf2Cd2))
				{
					shiwakeCheckDataKashi2.uf2Cd = kashiUf2Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf3Cd2))
				{
					shiwakeCheckDataKashi2.uf3Cd = kashiUf3Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf4Cd2))
				{
					shiwakeCheckDataKashi2.uf4Cd = kashiUf4Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf5Cd2))
				{
					shiwakeCheckDataKashi2.uf5Cd = kashiUf5Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf6Cd2))
				{
					shiwakeCheckDataKashi2.uf6Cd = kashiUf6Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf7Cd2))
				{
					shiwakeCheckDataKashi2.uf7Cd = kashiUf7Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf8Cd2))
				{
					shiwakeCheckDataKashi2.uf8Cd = kashiUf8Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf9Cd2))
				{
					shiwakeCheckDataKashi2.uf9Cd = kashiUf9Cd2[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.kashiUf10Cd2))
				{
					shiwakeCheckDataKashi2.uf10Cd = kashiUf10Cd2[i];
				}
				commonLogic.checkShiwake(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, shiwakeCheckDataKashi2, super.daihyouFutanBumonCd, errorList);
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
			Date shimebi = this.keijoubiShimebiDao.findMaxShimebiForDenpyouKbn(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU);
			if (shimebi != null && ! toDate(keijoubi).after(shimebi)) {
				errorList.add("計上日には締日(" + formatDate(shimebi) + ")より後を入力してください。");
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
		GMap userInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");

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

			if(ks.shouhyouShoruiFlg.getHyoujiFlg())
			{
				shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA009();
			}
			nyuryokuHoushiki = setting.zeiDefaultA009();
			invoiceDenpyou = setInvoiceFlgWhenNew();
		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			Jidouhikiotoshi shinseiData = jidouhikiotoshiDao.find(denpyouId);
			shinseiData2Gamen(shinseiData);

			List<JidouhikiotoshiMeisai> meisaiList = jidouhikiotoshiMeisaiDao.loadByDenpyouId(denpyouId);
			meisaiData2Gamen(meisaiList, sanshou, shinseiData.map, connection);

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			Jidouhikiotoshi shinseiData = jidouhikiotoshiDao.find(sanshouDenpyouId);
			shinseiData2Gamen(shinseiData);

			List<JidouhikiotoshiMeisai> meisaiList = jidouhikiotoshiMeisaiDao.loadByDenpyouId(sanshouDenpyouId);
			meisaiData2Gamen(meisaiList, sanshou, shinseiData.map, connection);

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
			hikiotoshibi = "";
			keijoubi = "";
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
		denpyouHissuCheck();
		if (0 <errorList.size())
		{
			return;
		}

		// 引落日チェック
		commonLogic.checkHikiotoshiBi(toDate(hikiotoshibi), toDate(keijoubi), errorList);
		if (0 <errorList.size())
		{
			return;
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

		// 発生主義かつ入力不可の場合、計上日を引落日に自動設定
		setKeijoubi();
		if (0 <errorList.size())
		{
			return;
		}
	}

	//更新ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		//申請内容登録
		Jidouhikiotoshi dto = this.createDto();
		this.jidouhikiotoshiDao.insert(dto, dto.tourokuUserId);

		//明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			JidouhikiotoshiMeisai meisaiDto = this.createMeisaiDto(i);
			this.jidouhikiotoshiMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
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
		denpyouHissuCheck();
		if (0 <errorList.size())
		{
			return;
		}
		// 計上日を入力する設定の場合のみ引落日と計上日の相関チェック
		if(keijouBiMode == 1 || keijouBiMode == 3){
			commonLogic.checkHikiotoshiBi(toDate(hikiotoshibi), toDate(keijoubi), errorList);
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

		// 発生主義かつ入力不可の場合、引落日に自動設定
		setKeijoubi();
		if (0 <errorList.size())
		{
			return;
		}

		//申請内容更新
		Jidouhikiotoshi dto = this.createDto();
		this.jidouhikiotoshiDao.update(dto, dto.koushinUserId);
		
		//明細削除
		this.jidouhikiotoshiMeisaiDao.deleteByDenpyouId(this.denpyouId);
		// 挿入
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			JidouhikiotoshiMeisai meisaiDto = this.createMeisaiDto(i);
			this.jidouhikiotoshiMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
		}
	}

	//承認ボタン押下時に、個別伝票について以下を行う。
	//・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		// 再表示用
		displaySeigyo(connection);

		// 計上日を入力する設定の場合のみDBの値で引落日と計上日の相関チェック
		if(hasseiShugi){
			Jidouhikiotoshi dto = jidouhikiotoshiDao.find(denpyouId);
			String hikiotoshi = formatDate(dto.hikiotoshibi);
			String keijou = formatDate(dto.keijoubi);
			commonLogic.checkHikiotoshiBi(toDate(hikiotoshi) , toDate(keijou), errorList);

			// エラーの場合の表示用に現実の値を設定
			hikiotoshibi = hikiotoshi;
			keijoubi = keijou;
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
		if (shinseishaKeijoubiInput) {
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
//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		gyoumuLogic = EteamContainer.getComponent(JidouHikiotoshiDenpyouLogic.class, connection);
		this.jidouhikiotoshiDao = EteamContainer.getComponent(JidouhikiotoshiDao.class, connection);
		this.jidouhikiotoshiMeisaiDao = EteamContainer.getComponent(JidouhikiotoshiMeisaiDao.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		keijoubiShimebiDao = EteamContainer.getComponent(KeijoubiShimebiDao.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}


	/**
	 * 自動引落テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 自動引落レコード
	 */
	protected void shinseiData2Gamen(Jidouhikiotoshi shinseiData) {
		hikiotoshibi = super.formatDate(shinseiData.hikiotoshibi);
		keijoubi = super.formatDate(shinseiData.keijoubi);
		shouhyouShoruiFlg = shinseiData.shouhyouShoruiFlg;
		hontaiKingakuGoukei = super.formatMoney(shinseiData.hontaiKingakuGoukei);
		hf1Cd = shinseiData.hf1Cd;
		hf1Name = shinseiData.hf1NameRyakushiki;
		hf2Cd = shinseiData.hf2Cd;
		hf2Name = shinseiData.hf2NameRyakushiki;
		hf3Cd = shinseiData.hf3Cd;
		hf3Name = shinseiData.hf3NameRyakushiki;
		hf4Cd = shinseiData.hf4Cd;
		hf4Name = shinseiData.hf4NameRyakushiki;
		hf5Cd = shinseiData.hf5Cd;
		hf5Name = shinseiData.hf5NameRyakushiki;
		hf6Cd = shinseiData.hf6Cd;
		hf6Name = shinseiData.hf6NameRyakushiki;
		hf7Cd = shinseiData.hf7Cd;
		hf7Name = shinseiData.hf7NameRyakushiki;
		hf8Cd = shinseiData.hf8Cd;
		hf8Name = shinseiData.hf8NameRyakushiki;
		hf9Cd = shinseiData.hf9Cd;
		hf9Name = shinseiData.hf9NameRyakushiki;
		hf10Cd = shinseiData.hf10Cd;
		hf10Name = shinseiData.hf10NameRyakushiki;
		shouhizeigakuGoukei = super.formatMoney(shinseiData.shouhizeigakuGoukei);
		kingaku = super.formatMoney(shinseiData.shiharaiKingakuGoukei);
		hosoku = shinseiData.hosoku;
		nyuryokuHoushiki = shinseiData.nyuryokuHoushiki;
		invoiceDenpyou = shinseiData.invoiceDenpyou;
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisai() {
		//消費税率マップ
		GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();

		//表示項目
		shiwakeEdaNo = new String[1]; shiwakeEdaNo[0] = "";
		torihikiName = new String[1]; torihikiName[0] = "";
		kamokuCd = new String[1]; kamokuCd[0] = "";
		kamokuName = new String[1]; kamokuName[0] = "";
		kamokuEdabanCd = new String[1]; kamokuEdabanCd[0] = "";
		kamokuEdabanName = new String[1]; kamokuEdabanName[0]= "";
		futanBumonCd = new String[1]; futanBumonCd[0] = "";
		futanBumonName = new String[1]; futanBumonName[0] = "";
		torihikisakiCd = new String[1]; torihikisakiCd[0] = "";
		torihikisakiName = new String[1]; torihikisakiName[0] = "";
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
		tekiyou = new String[1]; tekiyou[0] = "";
		chuuki2 = new String[1]; chuuki2[0] = "";
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
	protected void meisaiData2Gamen(List<JidouhikiotoshiMeisai> meisaiList, boolean sanshou, GMap shinseiData, EteamConnection connection) {
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
		jigyoushaKbn = new String[length];
		bunriKbn = new String[length];
		kariShiireKbn = new String[length];
		kashiShiireKbn = new String[length];
		for (int i = 0; i < length; i++) {
			JidouhikiotoshiMeisai meisai = meisaiList.get(i);
			shiwakeEdaNo[i] = Integer.toString(meisai.shiwakeEdano);
			torihikiName[i] = meisai.torihikiName;
			kamokuCd[i] = meisai.kariKamokuCd;
			kamokuName[i] = meisai.kariKamokuName;
			kamokuEdabanCd[i] = meisai.kariKamokuEdabanCd;
			kamokuEdabanName[i] = meisai.kariKamokuEdabanName;
			futanBumonCd[i] = meisai.kariFutanBumonCd;
			futanBumonName[i] = meisai.kariFutanBumonName;
			torihikisakiCd[i] = meisai.torihikisakiCd;
			torihikisakiName[i] = meisai.torihikisakiNameRyakushiki;
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
			keigenZeiritsuKbn[i] = meisai.keigenZeiritsuKbn;
			shiharaiKingaku[i] = super.formatMoney(meisai.shiharaiKingaku);
			hontaiKingaku[i] = super.formatMoney(meisai.hontaiKingaku);
			shouhizeigaku[i] = super.formatMoney(meisai.shouhizeigaku);
			this.tekiyou[i] = meisai.tekiyou;
			this.jigyoushaKbn[i]= meisai.jigyoushaKbn;
			this.bunriKbn[i] = meisai.bunriKbn;
			kariShiireKbn[i] = meisai.kariShiireKbn;
			kashiShiireKbn[i] = meisai.kashiShiireKbn;//コピペ
			if(!sanshou){
// String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, shinseiData, meisai, "0");
// String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, meisai.map, null, "0");
				String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU, shinseiData, meisai.map, "0");
				String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
				if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
					chuuki1 = commonLogic.getTekiyouChuuki();
					chuuki2[i] = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
				}
			}
		}
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		//プルダウンのリストを取得
		zeiritsuList = this.zeiritsuDao.load().stream().map(Shouhizeiritsu::getMap).collect(Collectors.toList());
		zeiritsuDomain = EteamCommon.mapList2Arr(zeiritsuList, "zeiritsu");
		kazeiKbnList =  naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		kazeiKbnDomain = EteamCommon.mapList2Arr(kazeiKbnList, "naibu_cd");
		bunriKbnList =  naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		bunriKbnDomain = EteamCommon.mapList2Arr(bunriKbnList, "naibu_cd");
		shiireKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		shiireKbnDomain = EteamCommon.mapList2Arr(shiireKbnList, "naibu_cd");
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		nyuryokuHoushikiList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("nyuryoku_flg").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		nyuryokuHoushikiDomain = EteamCommon.mapList2Arr(nyuryokuHoushikiList, "naibu_cd");
		jigyoushaKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");
		
		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();
		
// //TODO 期別消費税設定の取得 基準とする日がどれかわからないので、とりあえず最新を取れるようにnull　各伝票で別々に取得する必要があるのかまだ決まってないのでCO
// KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
// shouhizeikbn = Integer.toString(dto.shouhizeiKbn); //分離区分のdisable対応の消費税区分
// shiireZeiAnbun = Integer.toString(dto.shiireZeigakuAnbunFlg);//仕入区分のenable/非表示の按分法

		//計上日の入力状態・モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン))を判定
		keijouBiMode = commonLogic.judgeKeijouBiMode(hasseiShugi, shinseishaKeijoubiInput, denpyouId, loginUserInfo, enableInput, setting.keijouNyuuryoku());
		//計上日プルダウンのリスト取得
		if(keijouBiMode == 3) keijoubiList = masterLogic.loadKeijoubiList(denpyouKbn);

		//明細単位に仕訳パターンによる制御
		int length = shiwakeEdaNo.length;
		kamokuEdabanEnable = new boolean[length];
		futanBumonEnable = new boolean[length];
		torihikisakiEnable = new boolean[length];
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
		kazeiKbnCheck = new boolean[length];
		for (int i = 0; i < length; i++) {
			if (enableInput) {
				if (isNotEmpty(shiwakeEdaNo[i])) {

					//※以下はDenpyouMeisaiAction側で再定義しているため無効な制御変数

					//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
					//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
					GMap shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU,Integer.parseInt(shiwakeEdaNo[i])).map;
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
				}
			}
			kazeiKbnCheck[i] = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]);
		}
	}

	/**
	 * 登録時に仕訳パターンマスターより借方貸方の値を設定する。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		String daihyouBumonCd = super.daihyouFutanBumonCd;

		// 社員コード取得
		GMap userInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");

		//明細行数分の領域確保
		int length = shiwakeEdaNo.length;

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
		kashiShiireKbn = new String[length];

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

		tekiyouCd  			= new String[length];

		enableBumonSecurity = new boolean[length];

		//明細１行ずつ
		for (int i = 0; i < length; i++) {
			ShiwakePatternMaster shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU,Integer.parseInt(shiwakeEdaNo[i]));

			//借方　取引先 ※仕訳チェック用、DB登録には関係ない
			kariTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kariTorihikisakiCd) ? torihikisakiCd[i] : (String)shiwakeP.kariTorihikisakiCd;

			//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
			Arrays.fill(enableBumonSecurity, true);
			//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
			if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) ){
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
			//事業者区分変更不可の条件の場合は通常課税固定
			if (!("0".equals(invoiceDenpyou) && 
					((List.of("001", "002", "011", "013").contains(kazeiKbn[i]) && List.of("2","5","6","7","8","10").contains(shoriGroup[i]))
					|| shoriGroup[i].equals("21")))) {
				jigyoushaKbn[i] = "0";
			}

			//貸方　取引先 ※仕訳チェック用、DB登録には関係ない
			kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kashiTorihikisakiCd1) ? torihikisakiCd[i] : (String)shiwakeP.kashiTorihikisakiCd1;

			//貸方　負担部門コード
			kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kashiFutanBumonCd1, daihyouBumonCd);

			//貸方　科目コード
			kashiKamokuCd[i] = shiwakeP.kashiKamokuCd1;

			//貸方　科目枝番コード
			kashiKamokuEdabanCd[i] = shiwakeP.kashiKamokuEdabanCd1;

			//貸方　UF1-10
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

			//貸方2　UF1-10
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

			//貸方　課税区分
			kashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn1;
			//貸方　仕入区分
			kashiShiireKbn[i] = shiwakeP.kashiShiireKbn1 != null ? (String)shiwakeP.kashiShiireKbn1 : "";

			//社員コードを摘要コードに反映する場合
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
		kashiFutanBumonName = new String[length];
		kashiKamokuName = new String[length];
		kashiKamokuEdabanName = new String[length];

		//明細項目を１件ずつ変換
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
		}
	}

	/**
	 * 自動引落伝票EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		JidouHikiotoshiDenpyouXlsLogic jidouhikiotoshidenpyouxlsLogic = EteamContainer.getComponent(JidouHikiotoshiDenpyouXlsLogic.class, connection);
		jidouhikiotoshidenpyouxlsLogic.makeExcel(denpyouId, out);
	}

	/**
	 * 領収書が必要かのチェックを行う。
	 * 画面からの入力で「領収書・請求書等」が「あり」の場合
	 * @return  領収書が必要か
	 */
	@Override
	protected boolean isUseShouhyouShorui(){
		return shouhyouShoruiFlg.equals(EteamConst.Domain.FLG[0]);
	}


	/**
	 * 登録更新時の計上日の値制御メソッド
	 * 発生主義かつ入力不可の場合、引落日に自動設定
	 */
	private void setKeijoubi() {
		if (hasseiShugi && (keijouBiMode == 0  || keijouBiMode == 2)) {
			keijoubi = hikiotoshibi;
			if(setting.keijouNyuuryoku().equals("2")) {
				keijoubi = gyoumuLogic.getkeijoubiPulldown(denpyouKbn, hikiotoshibi, errorList);
			}
		}
	}
	

	
	/**
	 * @return 自動引落DTO作成
	 */
	protected Jidouhikiotoshi createDto()
	{
		Jidouhikiotoshi jidouhikiotoshi = new Jidouhikiotoshi();
		jidouhikiotoshi.denpyouId = this.denpyouId;
		jidouhikiotoshi.keijoubi = super.toDate(this.keijoubi);
		jidouhikiotoshi.hikiotoshibi = super.toDate(this.hikiotoshibi);
		jidouhikiotoshi.hontaiKingakuGoukei = super.toDecimal(this.hontaiKingakuGoukei);
		jidouhikiotoshi.shouhizeigakuGoukei = super.toDecimal(this.shouhizeigakuGoukei);
		jidouhikiotoshi.shiharaiKingakuGoukei = super.toDecimal(super.kingaku); // 親クラスの金額で代用しているということなので
		jidouhikiotoshi.hf1Cd = this.hf1Cd;
		jidouhikiotoshi.hf1NameRyakushiki = this.hf1Name;
		jidouhikiotoshi.hf2Cd = this.hf2Cd;
		jidouhikiotoshi.hf2NameRyakushiki = this.hf2Name;
		jidouhikiotoshi.hf3Cd = this.hf3Cd;
		jidouhikiotoshi.hf3NameRyakushiki = this.hf3Name;
		jidouhikiotoshi.hf4Cd = this.hf4Cd;
		jidouhikiotoshi.hf4NameRyakushiki = this.hf4Name;
		jidouhikiotoshi.hf5Cd = this.hf5Cd;
		jidouhikiotoshi.hf5NameRyakushiki = this.hf5Name;
		jidouhikiotoshi.hf6Cd = this.hf6Cd;
		jidouhikiotoshi.hf6NameRyakushiki = this.hf6Name;
		jidouhikiotoshi.hf7Cd = this.hf7Cd;
		jidouhikiotoshi.hf7NameRyakushiki = this.hf7Name;
		jidouhikiotoshi.hf8Cd = this.hf8Cd;
		jidouhikiotoshi.hf8NameRyakushiki = this.hf8Name;
		jidouhikiotoshi.hf9Cd = this.hf9Cd;
		jidouhikiotoshi.hf9NameRyakushiki = this.hf9Name;
		jidouhikiotoshi.hf10Cd = this.hf10Cd;
		jidouhikiotoshi.hf10NameRyakushiki = this.hf10Name;
		jidouhikiotoshi.hosoku = this.hosoku;
		jidouhikiotoshi.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		jidouhikiotoshi.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		jidouhikiotoshi.shouhyouShoruiFlg = this.shouhyouShoruiFlg;
		jidouhikiotoshi.nyuryokuHoushiki = this.nyuryokuHoushiki;
		jidouhikiotoshi.invoiceDenpyou = this.invoiceDenpyou;
		return jidouhikiotoshi;
	}
	
	/**
	 * @param i 行数
	 * @return 自動引落明細DTO
	 */
	protected JidouhikiotoshiMeisai createMeisaiDto(int i)
	{
		JidouhikiotoshiMeisai jidouhikiotoshiMeisai = new JidouhikiotoshiMeisai();
		jidouhikiotoshiMeisai.denpyouId = this.denpyouId;
		jidouhikiotoshiMeisai.denpyouEdano = i + 1;
		jidouhikiotoshiMeisai.shiwakeEdano = Integer.parseInt(this.shiwakeEdaNo[i]);
		jidouhikiotoshiMeisai.torihikiName = this.torihikiName[i];
		jidouhikiotoshiMeisai.tekiyou = this.tekiyou[i];
		jidouhikiotoshiMeisai.zeiritsu = kazeiKbnCheck[i] ? super.toDecimal(zeiritsu[i]) : BigDecimal.ZERO;
		jidouhikiotoshiMeisai.keigenZeiritsuKbn = super.isEmpty(keigenZeiritsuKbn[i]) ? "0" :this.keigenZeiritsuKbn[i];
		jidouhikiotoshiMeisai.shiharaiKingaku = super.toDecimal(this.shiharaiKingaku[i]);
		jidouhikiotoshiMeisai.hontaiKingaku = super.toDecimal(this.hontaiKingaku[i]);
		jidouhikiotoshiMeisai.shouhizeigaku = super.toDecimal(this.shouhizeigaku[i]);
		jidouhikiotoshiMeisai.kariFutanBumonCd = this.futanBumonCd[i];
		jidouhikiotoshiMeisai.kariFutanBumonName = this.futanBumonName[i];
		jidouhikiotoshiMeisai.torihikisakiCd = this.torihikisakiCd[i];
		jidouhikiotoshiMeisai.torihikisakiNameRyakushiki = this.torihikisakiName[i];
		jidouhikiotoshiMeisai.kariKamokuCd = this.kamokuCd[i];
		jidouhikiotoshiMeisai.kariKamokuName = this.kamokuName[i];
		jidouhikiotoshiMeisai.kariKamokuEdabanCd = this.kamokuEdabanCd[i];
		jidouhikiotoshiMeisai.kariKamokuEdabanName = this.kamokuEdabanName[i];
		jidouhikiotoshiMeisai.kariKazeiKbn = this.kazeiKbn[i];
		jidouhikiotoshiMeisai.kashiFutanBumonCd = this.kashiFutanBumonCd[i];
		jidouhikiotoshiMeisai.kashiFutanBumonName = this.kashiFutanBumonName[i];
		jidouhikiotoshiMeisai.kashiKamokuCd = this.kashiKamokuCd[i];
		jidouhikiotoshiMeisai.kashiKamokuName = this.kashiKamokuName[i];
		jidouhikiotoshiMeisai.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd[i];
		jidouhikiotoshiMeisai.kashiKamokuEdabanName = this.kashiKamokuEdabanName[i];
		jidouhikiotoshiMeisai.kashiKazeiKbn = this.kashiKazeiKbn[i];
		jidouhikiotoshiMeisai.uf1Cd = this.uf1Cd[i];
		jidouhikiotoshiMeisai.uf1NameRyakushiki = this.uf1Name[i];
		jidouhikiotoshiMeisai.uf2Cd = this.uf2Cd[i];
		jidouhikiotoshiMeisai.uf2NameRyakushiki = this.uf2Name[i];
		jidouhikiotoshiMeisai.uf3Cd = this.uf3Cd[i];
		jidouhikiotoshiMeisai.uf3NameRyakushiki = this.uf3Name[i];
		jidouhikiotoshiMeisai.uf4Cd = this.uf4Cd[i];
		jidouhikiotoshiMeisai.uf4NameRyakushiki = this.uf4Name[i];
		jidouhikiotoshiMeisai.uf5Cd = this.uf5Cd[i];
		jidouhikiotoshiMeisai.uf5NameRyakushiki = this.uf5Name[i];
		jidouhikiotoshiMeisai.uf6Cd = this.uf6Cd[i];
		jidouhikiotoshiMeisai.uf6NameRyakushiki = this.uf6Name[i];
		jidouhikiotoshiMeisai.uf7Cd = this.uf7Cd[i];
		jidouhikiotoshiMeisai.uf7NameRyakushiki = this.uf7Name[i];
		jidouhikiotoshiMeisai.uf8Cd = this.uf8Cd[i];
		jidouhikiotoshiMeisai.uf8NameRyakushiki = this.uf8Name[i];
		jidouhikiotoshiMeisai.uf9Cd = this.uf9Cd[i];
		jidouhikiotoshiMeisai.uf9NameRyakushiki = this.uf9Name[i];
		jidouhikiotoshiMeisai.uf10Cd = this.uf10Cd[i];
		jidouhikiotoshiMeisai.uf10NameRyakushiki = this.uf10Name[i];
		jidouhikiotoshiMeisai.projectCd = this.projectCd[i];
		jidouhikiotoshiMeisai.projectName = this.projectName[i];
		jidouhikiotoshiMeisai.segmentCd = this.segmentCd[i];
		jidouhikiotoshiMeisai.segmentNameRyakushiki = this.segmentName[i];
		jidouhikiotoshiMeisai.tekiyouCd = this.tekiyouCd[i];
		jidouhikiotoshiMeisai.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		jidouhikiotoshiMeisai.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		jidouhikiotoshiMeisai.jigyoushaKbn = this.jigyoushaKbn[i];
		jidouhikiotoshiMeisai.bunriKbn = this.bunriKbn[i];
		jidouhikiotoshiMeisai.kariShiireKbn = this.kariShiireKbn[i];
		jidouhikiotoshiMeisai.kashiShiireKbn = this.kashiShiireKbn[i];
		return jidouhikiotoshiMeisai;
	}
}
