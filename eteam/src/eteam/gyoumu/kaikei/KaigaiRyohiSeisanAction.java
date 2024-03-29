package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KAMOKU_KAZEI_KBN_GROUP;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.KaigaiRyohiseisanAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiseisanKeihiMeisaiAbstractDao;
import eteam.database.abstractdao.KaigaiRyohiseisanMeisaiAbstractDao;
import eteam.database.dao.KaigaiRyohiseisanDao;
import eteam.database.dao.KaigaiRyohiseisanKeihiMeisaiDao;
import eteam.database.dao.KaigaiRyohiseisanMeisaiDao;
import eteam.database.dto.KaigaiRyohiseisan;
import eteam.database.dto.KaigaiRyohiseisanKeihiMeisai;
import eteam.database.dto.KaigaiRyohiseisanMeisai;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 海外旅費精算Action
 */
@Getter @Setter @ToString
public class KaigaiRyohiSeisanAction extends RyohiSeisanCommonAction {

//＜定数＞

//＜画面入力＞

	//画面入力（申請内容）
	/** 課税フラグ（旅費） */
	String kazeiFlgRyohi;
	/** 課税フラグ（旅費）科目マスター.課税区分から判別 */
	String kazeiFlgRyohiKamoku;
	/** 課税フラグ（旅費/海外） */
	String kazeiFlgRyohiKaigai;
	/** 課税フラグ（旅費/海外）科目マスター.課税区分から判別 */
	String kazeiFlgRyohiKaigaiKamoku;
	/** 仕訳枝番号（旅費/海外） */
	String kaigaiShiwakeEdaNoRyohi;
	/** 取引名（旅費/海外） */
	String kaigaiTorihikiNameRyohi;
	/** 勘定科目コード（旅費/海外） */
	String kaigaiKamokuCdRyohi;
	/** 勘定科目名（旅費/海外） */
	String kaigaiKamokuNameRyohi;
	/** 勘定科目枝番コード（旅費/海外） */
	String kaigaiKamokuEdabanCdRyohi;
	/** 勘定科目枝番名（旅費/海外） */
	String kaigaiKamokuEdabanNameRyohi;
	/** 負担部門コード（旅費/海外） */
	String kaigaiFutanBumonCdRyohi;
	/** 負担部門名（旅費/海外） */
	String kaigaiFutanBumonNameRyohi;
	/** 取引先コード（旅費/海外） */
	String kaigaiTorihikisakiCdRyohi;
	/** 取引先名（旅費/海外） */
	String kaigaiTorihikisakiNameRyohi;
	/** プロジェクトコード（旅費/海外） */
	String kaigaiProjectCdRyohi;
	/** プロジェクト名（旅費/海外） */
	String kaigaiProjectNameRyohi;
	/** セグメントコード（旅費/海外） */
	String kaigaiSegmentCdRyohi;
	/** セグメント名（旅費/海外） */
	String kaigaiSegmentNameRyohi;
	/** ユニバーサルフィールド１コード（旅費） */
	String kaigaiUf1CdRyohi;
	/** ユニバーサルフィールド１名（旅費） */
	String kaigaiUf1NameRyohi;
	/** ユニバーサルフィールド２コード（旅費） */
	String kaigaiUf2CdRyohi;
	/** ユニバーサルフィールド２名（旅費） */
	String kaigaiUf2NameRyohi;
	/** ユニバーサルフィールド３コード（旅費） */
	String kaigaiUf3CdRyohi;
	/** ユニバーサルフィールド３名（旅費） */
	String kaigaiUf3NameRyohi;
	/** ユニバーサルフィールド４コード（旅費） */
	String kaigaiUf4CdRyohi;
	/** ユニバーサルフィールド４名（旅費） */
	String kaigaiUf4NameRyohi;
	/** ユニバーサルフィールド５コード（旅費） */
	String kaigaiUf5CdRyohi;
	/** ユニバーサルフィールド５名（旅費） */
	String kaigaiUf5NameRyohi;
	/** ユニバーサルフィールド６コード（旅費） */
	String kaigaiUf6CdRyohi;
	/** ユニバーサルフィールド６名（旅費） */
	String kaigaiUf6NameRyohi;
	/** ユニバーサルフィールド７コード（旅費） */
	String kaigaiUf7CdRyohi;
	/** ユニバーサルフィールド７名（旅費） */
	String kaigaiUf7NameRyohi;
	/** ユニバーサルフィールド８コード（旅費） */
	String kaigaiUf8CdRyohi;
	/** ユニバーサルフィールド８名（旅費） */
	String kaigaiUf8NameRyohi;
	/** ユニバーサルフィールド９コード（旅費） */
	String kaigaiUf9CdRyohi;
	/** ユニバーサルフィールド９名（旅費） */
	String kaigaiUf9NameRyohi;
	/** ユニバーサルフィールド１０コード（旅費） */
	String kaigaiUf10CdRyohi;
	/** ユニバーサルフィールド１０名（旅費） */
	String kaigaiUf10NameRyohi;
	/** 課税区分（旅費/旅費） */
	String kaigaiKazeiKbnRyohi;
	/** 摘要（旅費/海外） */
	String kaigaiTekiyouRyohi;
	/** 伝票摘要注記（旅費） */
	String kaigaiChuuki2Ryohi;
	/** 差引回数（海外） */
	String sashihikiNumKaigai;
	/** 差引幣種（海外） */
	String sashihikiHeishuCdKaigai;
	/** 差引レート（海外） */
	String sashihikiRateKaigai;
	/** 差引単価（海外）外貨 */
	String sashihikiTankaKaigaiGaika;
	/** 差引単価（海外）邦貨 */
	String sashihikiTankaKaigai;
	/** 差引金額（海外） */
	String sashihikiKingakuKaigai;
	/** 海外分離区分 */
	String kaigaiBunriKbn;
	/** 海外借方仕入区分 */
	String kaigaiKariShiireKbn;
	/** 海外貸方仕入区分 */
	String kaigaiKashiShiireKbn;
	/** 海外処理グループ */
	Integer kaigaiShoriGroupRyohi;

	//画面入力（明細/旅費）
	/** 海外フラグ */
	String[] kaigaiFlgRyohi;
	/** 幣種コード */
	String[] heishuCdRyohi;
	/** レート */
	String[] rateRyohi;
	/** 外貨 */
	String[] gaikaRyohi;
	/** 通貨単位 */
	String[] taniRyohi;
	/** 税区分 */
	String[] zeiKubun;
	/** 課税フラグ */
	String[] kazeiFlgRyohiMeisai;

	//画面入力（明細/経費）
	/** 課税フラグ */
	String[] kazeiFlg;
	/** 幣種コード */
	String[] heishuCd;
	/** レート */
	String[] rate;
	/** 外貨 */
	String[] gaika;
	/** 通貨単位 */
	String[] tani;

//＜画面入力以外＞

	//プルダウン等の候補値
	/** 海外交通手段のDropDownList */
	List<GMap> kaigaiKoutsuuShudanList;
	/** 海外交通手段ドメイン */
	String[] kaigaiKoutsuuShudanDomain;

	/** 借方　UF1コード（旅費／海外） */
	String kaigaiKariUf1CdRyohi;
	/** 借方　UF2コード（旅費／海外） */
	String kaigaiKariUf2CdRyohi;
	/** 借方　UF3コード（旅費／海外） */
	String kaigaiKariUf3CdRyohi;
	/** 借方　UF4コード（旅費／海外） */
	String kaigaiKariUf4CdRyohi;
	/** 借方　UF5コード（旅費／海外） */
	String kaigaiKariUf5CdRyohi;
	/** 借方　UF6コード（旅費／海外） */
	String kaigaiKariUf6CdRyohi;
	/** 借方　UF7コード（旅費／海外） */
	String kaigaiKariUf7CdRyohi;
	/** 借方　UF8コード（旅費／海外） */
	String kaigaiKariUf8CdRyohi;
	/** 借方　UF9コード（旅費／海外） */
	String kaigaiKariUf9CdRyohi;
	/** 借方　UF10コード（旅費／海外） */
	String kaigaiKariUf10CdRyohi;

	/** 貸方負担部門コード（旅費／海外） */
	String kaigaiKashiFutanBumonCdRyohi;
	/** 貸方負担部門名（旅費／海外） */
	String kaigaiKashiFutanBumonNameRyohi;
	/** 貸方科目コード（旅費／海外） */
	String kaigaiKashiKamokuCdRyohi;
	/** 貸方科目名（旅費／海外） */
	String kaigaiKashiKamokuNameRyohi;
	/** 貸方科目枝番コード（旅費／海外） */
	String kaigaiKashiKamokuEdabanCdRyohi;
	/** 貸方科目枝番名（旅費／海外） */
	String kaigaiKashiKamokuEdabanNameRyohi;
	/** 貸方課税区分（旅費／海外） */
	String kaigaiKashiKazeiKbnRyohi;
	/** 貸方1　UF1コード（旅費／海外） */
	String kaigaiKashiUf1Cd1Ryohi;
	/** 貸方1　UF2コード（旅費／海外） */
	String kaigaiKashiUf2Cd1Ryohi;
	/** 貸方1　UF3コード（旅費／海外） */
	String kaigaiKashiUf3Cd1Ryohi;
	/** 貸方1　UF4コード（旅費／海外） */
	String kaigaiKashiUf4Cd1Ryohi;
	/** 貸方1 UF5コード（旅費／海外） */
	String kaigaiKashiUf5Cd1Ryohi;
	/** 貸方1 UF6コード（旅費／海外） */
	String kaigaiKashiUf6Cd1Ryohi;
	/** 貸方1 UF7コード（旅費／海外） */
	String kaigaiKashiUf7Cd1Ryohi;
	/** 貸方1 UF8コード（旅費／海外） */
	String kaigaiKashiUf8Cd1Ryohi;
	/** 貸方1 UF9コード（旅費／海外） */
	String kaigaiKashiUf9Cd1Ryohi;
	/** 貸方1 UF10コード（旅費／海外） */
	String kaigaiKashiUf10Cd1Ryohi;
	/** 貸方2 UF1コード（旅費／海外） */
	String kaigaiKashiUf1Cd2Ryohi;
	/** 貸方2　UF2コード（旅費／海外） */
	String kaigaiKashiUf2Cd2Ryohi;
	/** 貸方2　UF3コード（旅費／海外） */
	String kaigaiKashiUf3Cd2Ryohi;
	/** 貸方2　UF4コード（旅費／海外） */
	String kaigaiKashiUf4Cd2Ryohi;
	/** 貸方2　UF5コード（旅費／海外） */
	String kaigaiKashiUf5Cd2Ryohi;
	/** 貸方2　UF6コード（旅費／海外） */
	String kaigaiKashiUf6Cd2Ryohi;
	/** 貸方2　UF7コード（旅費／海外） */
	String kaigaiKashiUf7Cd2Ryohi;
	/** 貸方2　UF8コード（旅費／海外） */
	String kaigaiKashiUf8Cd2Ryohi;
	/** 貸方2　UF9コード（旅費／海外） */
	String kaigaiKashiUf9Cd2Ryohi;
	/** 貸方2　UF10コード（旅費／海外） */
	String kaigaiKashiUf10Cd2Ryohi;
	/** 貸方3　UF1コード（旅費／海外） */
	String kaigaiKashiUf1Cd3Ryohi;
	/** 貸方3　UF2コード（旅費／海外） */
	String kaigaiKashiUf2Cd3Ryohi;
	/** 貸方3　UF3コード（旅費／海外） */
	String kaigaiKashiUf3Cd3Ryohi;
	/** 貸方3　UF4コード（旅費／海外） */
	String kaigaiKashiUf4Cd3Ryohi;
	/** 貸方3　UF5コード（旅費／海外） */
	String kaigaiKashiUf5Cd3Ryohi;
	/** 貸方3　UF6コード（旅費／海外） */
	String kaigaiKashiUf6Cd3Ryohi;
	/** 貸方3　UF7コード（旅費／海外） */
	String kaigaiKashiUf7Cd3Ryohi;
	/** 貸方3　UF8コード（旅費／海外） */
	String kaigaiKashiUf8Cd3Ryohi;
	/** 貸方3　UF9コード（旅費／海外） */
	String kaigaiKashiUf9Cd3Ryohi;
	/** 貸方3　UF10コード（旅費／海外） */
	String kaigaiKashiUf10Cd3Ryohi;
	/** 貸方4　UF1コード（旅費／海外） */
	String kaigaiKashiUf1Cd4Ryohi;
	/** 貸方4　UF2コード（旅費／海外） */
	String kaigaiKashiUf2Cd4Ryohi;
	/** 貸方4　UF3コード（旅費／海外） */
	String kaigaiKashiUf3Cd4Ryohi;
	/** 貸方4　UF4コード（旅費／海外） */
	String kaigaiKashiUf4Cd4Ryohi;
	/** 貸方4　UF5コード（旅費／海外） */
	String kaigaiKashiUf5Cd4Ryohi;
	/** 貸方4　UF6コード（旅費／海外） */
	String kaigaiKashiUf6Cd4Ryohi;
	/** 貸方4　UF7コード（旅費／海外） */
	String kaigaiKashiUf7Cd4Ryohi;
	/** 貸方4　UF8コード（旅費／海外） */
	String kaigaiKashiUf8Cd4Ryohi;
	/** 貸方4　UF9コード（旅費／海外） */
	String kaigaiKashiUf9Cd4Ryohi;
	/** 貸方4　UF10コード（旅費／海外） */
	String kaigaiKashiUf10Cd4Ryohi;
	/** 貸方5　UF1コード（旅費／海外） */
	String kaigaiKashiUf1Cd5Ryohi;
	/** 貸方5　UF2コード（旅費／海外） */
	String kaigaiKashiUf2Cd5Ryohi;
	/** 貸方5　UF3コード（旅費／海外） */
	String kaigaiKashiUf3Cd5Ryohi;
	/** 貸方5　UF4コード（旅費／海外） */
	String kaigaiKashiUf4Cd5Ryohi;
	/** 貸方5　UF5コード（旅費／海外） */
	String kaigaiKashiUf5Cd5Ryohi;
	/** 貸方5　UF6コード（旅費／海外） */
	String kaigaiKashiUf6Cd5Ryohi;
	/** 貸方5　UF7コード（旅費／海外） */
	String kaigaiKashiUf7Cd5Ryohi;
	/** 貸方5　UF8コード（旅費／海外） */
	String kaigaiKashiUf8Cd5Ryohi;
	/** 貸方5　UF9コード（旅費／海外） */
	String kaigaiKashiUf9Cd5Ryohi;
	/** 貸方5　UF10コード（旅費／海外） */
	String kaigaiKashiUf10Cd5Ryohi;
	
	/** 摘要コード（旅費/海外） */
	String kaigaiTekiyouCdRyohi;

	//画面制御情報→共通化済み

	// 会社設定情報
	/** 差引項目名称（海外） */
	String sashihikiNameKaigai = setting.sashihikiNameKaigai();
	/** 差引単価（海外） */
	String sashihikiTankaKaigaiSI = setting.sashihikiTankaKaigai();
	
	/** UF1ボタン押下可否 */
	boolean kaigaiUf1EnableRyohi;
	/** UF2ボタン押下可否 */
	boolean kaigaiUf2EnableRyohi;
	/** UF3ボタン押下可否 */
	boolean kaigaiUf3EnableRyohi;
	/** UF4ボタン押下可否 */
	boolean kaigaiUf4EnableRyohi;
	/** UF5ボタン押下可否 */
	boolean kaigaiUf5EnableRyohi;
	/** UF6ボタン押下可否 */
	boolean kaigaiUf6EnableRyohi;
	/** UF7ボタン押下可否 */
	boolean kaigaiUf7EnableRyohi;
	/** UF8ボタン押下可否 */
	boolean kaigaiUf8EnableRyohi;
	/** UF9ボタン押下可否 */
	boolean kaigaiUf9EnableRyohi;
	/** UF10ボタン押下可否 */
	boolean kaigaiUf10EnableRyohi;
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean kaigaiKamokuEdabanEnableRyohi;
	/** 負担部門選択ボタン押下可否 */
	boolean kaigaiFutanBumonEnableRyohi;
	/** 取引先選択ボタン押下可否 */
	boolean kaigaiTorihikisakiEnableRyohi;
	/** プロジェクト選択ボタン(海外) */
	boolean kaigaiProjectEnableRyohi;
	/** セグメント選択ボタン(海外) */
	boolean kaigaiSegmentEnableRyohi;
	/** 外貨表示制御 */
	boolean enableGaika;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean enableKaigaiBumonSecurity;
	/** 差引項目表示フラグ */
	boolean sasihikiHyoujiFlgKaigai = isNotEmpty(sashihikiNameKaigai);
	/** 差引項目外貨表示フラグ（海外） */
	boolean sashihikiHyoujiFlgKaigaiGaika = "1".equals(setting.kaigaiNittouTankaGaikaSettei());

//＜部品＞
	/** 旅費精算ロジック */
	KaigaiRyohiSeisanLogic kaigaiRyohiSeisanLogic;
	/** 海外旅費精算DAO */
	KaigaiRyohiseisanAbstractDao kaigaiRyohiseisanDao;
	/** 海外旅費精算明細DAO */
	KaigaiRyohiseisanMeisaiAbstractDao kaigaiRyohiseisanMeisaiDao;
	/** 海外旅費精算経費明細DAO */
	KaigaiRyohiseisanKeihiMeisaiAbstractDao kaigaiRyohiseisanKeihiMeisaiDao;


//＜親子制御＞

//＜入力チェック＞
	@Override
	protected void denpyouFormatCheck() {
		// 基本項目のチェック（ID～合計金額）
		super.commonDenpyouFormatCheckBase();
		
		// 海外分のチェック
		checkString(kaigaiShiwakeEdaNoRyohi, 1, 10, "海外"+ks.torihiki.getName() + "コード", false);
		checkString(kaigaiTorihikiNameRyohi, 1, 20, "海外"+ks.torihiki.getName() + "名", false);
		checkString(kaigaiKamokuCdRyohi, 1, 6, "海外"+ks.kamoku.getName() + "コード", false);
		checkString(kaigaiKamokuNameRyohi, 1, 22, "海外"+ks.kamoku.getName() + "名", false);
		checkString(kaigaiKamokuEdabanCdRyohi, 1, 12, "海外"+ks.kamokuEdaban.getName() + "コード", false);
		checkString(kaigaiKamokuEdabanNameRyohi, 1, 20, "海外"+ks.kamokuEdaban.getName() + "名", false);
		checkString(kaigaiFutanBumonCdRyohi, 1, 8, "海外"+ks.futanBumon.getName() + "コード", false);
		checkString(kaigaiFutanBumonNameRyohi, 1, 20, "海外"+ks.futanBumon.getName() + "名", false);
		checkString(kaigaiTorihikisakiCdRyohi, 1, 12, "海外"+ks.torihikisaki.getName() + "コード", false);
		checkString(kaigaiTorihikisakiNameRyohi, 1, 20, "海外"+ks.torihikisaki.getName() + "名", false);
		checkString(kaigaiProjectCdRyohi, 1, 12, "海外"+ks.project.getName() + "コード", false);
		checkString(kaigaiProjectNameRyohi, 1, 20, "海外"+ks.project.getName() + "名", false);
		checkString(kaigaiSegmentCdRyohi, 1, 8, "海外"+ks.segment.getName() + "コード", false);
		checkString(kaigaiSegmentNameRyohi, 1, 20, "海外"+ks.segment.getName() + "名", false);
		checkString(kaigaiUf1CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf1Name(), false);
		checkString(kaigaiUf1NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf1Name(), false);
		checkString(kaigaiUf2CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf2Name(), false);
		checkString(kaigaiUf2NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf2Name(), false);
		checkString(kaigaiUf3CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf3Name(), false);
		checkString(kaigaiUf3NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf3Name(), false);
		checkString(kaigaiUf4CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf4Name(), false);
		checkString(kaigaiUf4NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf4Name(), false);
		checkString(kaigaiUf5CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf5Name(), false);
		checkString(kaigaiUf5NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf5Name(), false);
		checkString(kaigaiUf6CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf6Name(), false);
		checkString(kaigaiUf6NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf6Name(), false);
		checkString(kaigaiUf7CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf7Name(), false);
		checkString(kaigaiUf7NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf7Name(), false);
		checkString(kaigaiUf8CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf8Name(), false);
		checkString(kaigaiUf8NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf8Name(), false);
		checkString(kaigaiUf9CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf9Name(), false);
		checkString(kaigaiUf9NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf9Name(), false);
		checkString(kaigaiUf10CdRyohi, 1, 20, "海外"+hfUfSeigyo.getUf10Name(), false);
		checkString(kaigaiUf10NameRyohi, 1, 20, "海外"+hfUfSeigyo.getUf10Name(), false);
		checkString(kaigaiTekiyouRyohi, 1, this.getIntTekiyouMaxLength(), "海外"+ks.tekiyou.getName(), false);
		checkSJIS(kaigaiTekiyouRyohi, "海外"+ks.tekiyou.getName());
		checkDomain(this.kaigaiBunriKbn, this.bunriKbnDomain, "海外" + ks.bunriKbn.getName(), false);
		checkDomain(this.kaigaiKariShiireKbn, this.shiireKbnDomain, "海外" + ks.shiireKbn.getName(), false);
		// 国内分のチェック
		super.commonKokunaiRyohiCheck(true);
		// 会社設定に差引項目が入力されている場合
		if( sasihikiHyoujiFlgKaigai ){
			if(sashihikiHyoujiFlgKaigaiGaika) {
				//レートと単価（外貨）から導かれる単価（邦貨）が最大値を超えないこと
				checkKingakuDecimalOver1(sashihikiTankaKaigaiGaika, ks.sashihikiTankaKaigaiGaika.getName() ,false);
				checkKingaku(sashihikiTankaKaigai, 0, 999999, ks.sashihikiTankaKaigai.getName() ,false);
			}
			checkNumberRange3(sashihikiNumKaigai, 1, 99, sashihikiNameKaigai, false);
			checkKingakuMinus(sashihikiKingakuKaigai, ks.sashihikiKingaku.getName(), false);
		}
		if( sasihikiHyoujiFlg ){
			checkNumberRange3(sashihikiNum, 1, 99, sashihikiName, false);
			checkKingakuMinus(sashihikiKingaku, ks.sashihikiKingaku.getName(), false);
		}
		checkString (hosoku,1 ,240 , ks.hosoku.getName(), false);

		if (judgeKeihiEntry()) {

			//画面入力（明細）
			for (int i = 0; i < shubetsuCd.length; i++) {
				super.commonRyohiMeisaiFormatCheck(i, (kaigaiFlgRyohi[i].equals("1")), (kaigaiFlgRyohi[i].equals("1"))? kaigaiKoutsuuShudanDomain : koutsuuShudanDomain);
			}

			//画面入力（経費明細）
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				String ip = "その他 経費 " + (i + 1);
				this.commonKeihiMeisaiFormatCheck(i, ip);
				if (kaigaiFlg[i].equals("1")) {
					checkString (heishuCd[i], 1, 4, ks.heishu.getName() + "：" + ip + "行目", false);
					checkKingakuDecimalMoreThan0(rate[i], ks.rate.getName() + "：" + ip + "行目", false);
					checkKingakuDecimalOver1 (gaika[i], ks.gaika.getName() + "：" + ip + "行目", false);
					checkString (tani[i], 1, 20, "通貨単位" + "：" + ip + "行目", false);
				}
			}
		}
	}
	
	@Override
	protected void checkKaigaiMeisaiKoumokuFormat(int i, String ip)
	{
		checkString (heishuCdRyohi[i], 1, 4, ks.heishu.getName()  + ":" + ip + "行目", false);
		checkKingakuDecimalMoreThan0(rateRyohi[i], ks.rate.getName()  + ":" + ip + "行目", false);
		checkKingakuDecimalOver1 (gaikaRyohi[i], ks.gaika.getName()  + ":" + ip + "行目", false);
		checkString (taniRyohi[i], 1, 20, "通貨単位" + ":" + ip + "行目", false);
	}
	
	@Override
	protected void addKaigaiShiwakeKoumokuForHissuCheck(List<String[]> listArrays, boolean keihiEntryFlg)
	{
		listArrays.add(new String[] {kaigaiShiwakeEdaNoRyohi, "海外" + ks.torihiki.getName() + "コード", keihiEntryFlg ? ks.torihiki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiTorihikiNameRyohi, "海外" + ks.torihiki.getName() + "名", keihiEntryFlg ? ks.torihiki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiKamokuCdRyohi, "海外" + ks.kamoku.getName() + "コード", keihiEntryFlg ? ks.kamoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiKamokuNameRyohi, "海外" + ks.kamoku.getName() + "名", keihiEntryFlg ? ks.kamoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiKamokuEdabanCdRyohi, "海外" + ks.kamokuEdaban.getName() + "コード", keihiEntryFlg ? ks.kamokuEdaban.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiKamokuEdabanNameRyohi, "海外" + ks.kamokuEdaban.getName() + "名", keihiEntryFlg ? ks.kamokuEdaban.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiFutanBumonCdRyohi, "海外" + ks.futanBumon.getName() + "コード", keihiEntryFlg ? ks.futanBumon.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiFutanBumonNameRyohi, "海外" + ks.futanBumon.getName() + "名", keihiEntryFlg ? ks.futanBumon.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiTorihikisakiCdRyohi, "海外" + ks.torihikisaki.getName() + "コード", keihiEntryFlg ? ks.torihikisaki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiTorihikisakiNameRyohi, "海外" + ks.torihikisaki.getName() + "名", keihiEntryFlg ? ks.torihikisaki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiProjectCdRyohi, "海外" + ks.project.getName() + "コード", keihiEntryFlg && ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiProjectNameRyohi, "海外" + ks.project.getName() + "名", keihiEntryFlg && ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiSegmentCdRyohi, "海外" + ks.segment.getName() + "コード", keihiEntryFlg && ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiSegmentNameRyohi, "海外" + ks.segment.getName() + "名", keihiEntryFlg && ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kaigaiTekiyouRyohi, ks.tekiyou.getName(), keihiEntryFlg ? ks.tekiyou.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {this.kaigaiBunriKbn, "海外" + ks.bunriKbn.getName(), keihiEntryFlg ? ks.bunriKbn.getHissuFlgS() : "0", "0"});
		listArrays.add(new String[] {this.kaigaiKariShiireKbn, "海外" + ks.shiireKbn.getName(), keihiEntryFlg ? ks.shiireKbn.getHissuFlgS() : "0", "0"});
	}
	
	@Override
	protected void hissuCheckSashihikiKingakuKaigai(int eventNum, boolean keihiEntryFlg)
	{
		if( sasihikiHyoujiFlgKaigai ){
			var list = new String[][] {
					{sashihikiNumKaigai, ks.sashihikiNum.getName(), keihiEntryFlg ? ks.sashihikiNum.getHissuFlgS() : "0", "0"},
					{sashihikiKingakuKaigai, ks.sashihikiKingaku.getName(), keihiEntryFlg ? ks.sashihikiKingaku.getHissuFlgS() : "0", "0"},
				};
			hissuCheckCommon(list, eventNum);
		}
	}
	
	@Override
	protected void addKaigaiRyohiMeisaiKoumokuForHissuCheck(List<String[]> ryohiMeisaiListArrays, int i, String ip)
	{
		ryohiMeisaiListArrays.add(new String[] {heishuCdRyohi[i], ks.heishu.getName() + ":" + ip + "行目", ks.heishu.getHissuFlgS(),"0"});
		ryohiMeisaiListArrays.add(new String[] {rateRyohi[i], ks.rate.getName() + ":" + ip + "行目", ks.rate.getHissuFlgS(),"0"});
		ryohiMeisaiListArrays.add(new String[] {gaikaRyohi[i], ks.gaika.getName() + ":" + ip + "行目", ks.gaika.getHissuFlgS(),"0"});
		ryohiMeisaiListArrays.add(new String[] {taniRyohi[i], "通貨単位" + ":" + ip + "行目", "0","0"});
	}
	
	@Override
	protected void addKaigaiKeihiMeisaiKoumokuForHissuCheck(List<String[]> keihiMeisaiListArrays, int i, String ip)
	{
		keihiMeisaiListArrays.add(0, new String[] {kaigaiFlg[i], ks.shucchouKbn.getName() + "：" + ip + "行目", ks.shucchouKbn.getHissuFlgS(), "0"});
		if (isKaigaiKeihiMeisai(i)) {
			keihiMeisaiListArrays.add(new String[] {heishuCd[i], ks.heishu.getName() + ":" + ip + "行目", ks.heishu.getHissuFlgS(),"0"});
			keihiMeisaiListArrays.add(new String[] {rate[i], ks.rate.getName() + ":" + ip + "行目", ks.rate.getHissuFlgS(),"0"});
			keihiMeisaiListArrays.add(new String[] {gaika[i], ks.gaika.getName() + ":" + ip + "行目", ks.gaika.getHissuFlgS(),"0"});
			keihiMeisaiListArrays.add(new String[] {tani[i], "通貨単位" + ":" + ip + "行目", "0","0"});
		}
	}

	@Override
	protected void soukanCheck(String user_Id) {
		// 国内旅費伝票までの共通部
		super.commonSoukanCheckBase(user_Id, true);

		// 共通機能（会計共通）．会計項目入力チェック(海外)
		if (isNotEmpty(kaigaiShiwakeEdaNoRyohi)) {
			//旅費精算の仕訳パターン取得
			ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(denpyouKbn, Integer.parseInt(kaigaiShiwakeEdaNoRyohi));
			// 使用者の代表負担部門コード
			String daihyouBumonCd = super.daihyouFutanBumonCd;
			// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
			if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
				daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
			}
			// 借方
			this.checkShiwakeKaigaiKashi(0, shiwakePattern, denpyouKbn, daihyouBumonCd);
			
			// 貸方
			// 貸方1：振込、貸方2：現金
			this.checkShiwakeKaigaiKashi(shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, shiwakePattern, denpyouKbn, daihyouBumonCd);

			// 貸方3
			if (hasseiShugi) {
				this.checkShiwakeKaigaiKashi(3, shiwakePattern, denpyouKbn, daihyouBumonCd);
			}

			boolean houjinCardFlg = false;
			boolean kaishaTehaiFlg = false;
			for (int i = 0; i < houjinCardFlgRyohi.length; i++) {
				if (houjinCardFlgRyohi[i].equals("1") && kaigaiFlgRyohi[i].equals("1"))
				{
					houjinCardFlg = true;
				}
				if (kaishaTehaiFlgRyohi[i].equals("1") && kaigaiFlgRyohi[i].equals("1"))
				{
					kaishaTehaiFlg = true;
				}
			}

			// 貸方4
			if (houjinCardFlg) {
				this.checkShiwakeKaigaiKashi(4, shiwakePattern, denpyouKbn, daihyouBumonCd);
			}

			// 貸方5
			if (kaishaTehaiFlg) {
				this.checkShiwakeKaigaiKashi(5, shiwakePattern, denpyouKbn, daihyouBumonCd);
			}

			// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
			if (isNotEmpty(kaigaiTorihikisakiCdRyohi)) {
				ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
				torihikisaki.torihikisakiNm = ks.torihikisaki.getName() + "コード";
				torihikisaki.torihikisakiCd =kaigaiTorihikisakiCdRyohi;
				commonLogic.checkTorihikisaki(torihikisaki, errorList);

				// 取引先仕入先チェック
				super.checkShiiresaki(ks.torihikisaki.getName() + "コード", kaigaiTorihikisakiCdRyohi, denpyouKbn);
			}
		}

		// 明細必須フラグONで、仕訳枝番号(国内)が未入力の場合
		if (judgeKeihiEntry() && isEmpty(shiwakeEdaNoRyohi)){
			if(!"".equals(kaigaiFlgRyohi[0])){
				//meisaiCount = 全旅費明細数 - (海外分)旅費明細数
				int meisaiCount = kaigaiFlgRyohi.length;
				for(int i = 0 ; i < kaigaiFlgRyohi.length ; i++){ meisaiCount -= Integer.parseInt(kaigaiFlgRyohi[i]); }
				// 国内分の旅費明細が1件以上あればエラー
				if(meisaiCount > 0){
					errorList.add("国内取引を入力してください。");
				}
			}
		}
		
		// 明細の共通チェック
		super.soukanCheckMeisai();
	}
	
	@Override
	protected void checkSashihiki()
	{
		// 日当の金額チェック
		if (meisaiKingaku != null){
			double nittouGoukeiKaigai = 0;
			double nittouGoukei = 0;
			for(int i=0 ; i < meisaiKingaku.length ; i++){
				if(RYOHISEISAN_SYUBETSU.SONOTA.equals(shubetsuCd[i]) && isNotEmpty(meisaiKingaku[i])){
					//日当かどうか判断
					if("1".equals(kaigaiFlgRyohi[i])){
						if(commonLogic.isNittouKaigai(shubetsu1[i], shubetsu2[i], commonLogic.getYakushokuCd(userIdRyohi))) nittouGoukeiKaigai += toDecimal(meisaiKingaku[i]).doubleValue();
					}else{
						if(commonLogic.isNittou(shubetsu1[i], shubetsu2[i], commonLogic.getYakushokuCd(userIdRyohi))) nittouGoukei += toDecimal(meisaiKingaku[i]).doubleValue();
					}
				}
			}
			if( sasihikiHyoujiFlgKaigai && isNotEmpty(sashihikiKingakuKaigai)){
				nittouGoukeiKaigai += toDecimal(sashihikiKingakuKaigai).doubleValue();
				if (nittouGoukeiKaigai < 0) errorList.add("日当（海外分）の合計がマイナスです。");
			}
			if( sasihikiHyoujiFlg && isNotEmpty(sashihikiKingaku)){
				nittouGoukei += toDecimal(sashihikiKingaku).doubleValue();
				if (nittouGoukei < 0) errorList.add("日当（国内分）の合計がマイナスです。");
			}
		}
	}
	
	/**
	 * 海外貸方仕訳チェック共通化
	 * @param i 貸方いくつ？（0は借方）
	 * @param shiwakePattern 仕訳パターン
	 * @param denpyouKbnTmp 伝票区分
	 * @param daihyouBumonCd 代表部門コード
	 */
	protected void checkShiwakeKaigaiKashi(int i, ShiwakePatternMaster shiwakePattern, String denpyouKbnTmp, String daihyouBumonCd)
	{
		this.checkShiwakeKaigaiKashi(i, shiwakePattern.map, denpyouKbnTmp, daihyouBumonCd);
	}
	
	/**
	 * 海外貸方仕訳チェック共通化
	 * @param i 貸方いくつ？（0は借方）
	 * @param shiwakePattern 仕訳パターン
	 * @param denpyouKbnTmp 伝票区分
	 * @param daihyouBumonCd 代表部門コード
	 */
	protected void checkShiwakeKaigaiKashi(int i, GMap shiwakePattern, String denpyouKbnTmp, String daihyouBumonCd)
	{
		boolean isKari = i == 0;
		String prefixJa = isKari ? "" : "貸方";
		String prefixEn = isKari ? "kari_" : "kashi_";
		String suffix = isKari ? "" : String.valueOf(i);
		ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData();
		shiwakeCheckData.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
		shiwakeCheckData.shiwakeEdaNo = kaigaiShiwakeEdaNoRyohi;
		// 原因不明だが、貸方3～5では科目・枝番・部門・課税区分はチェックされていない
		if(i <= 2)
		{
			shiwakeCheckData.kamokuNm = prefixJa + ks.kamoku.getName() + "コード";
			shiwakeCheckData.kamokuCd = isKari ? kaigaiKamokuCdRyohi : kaigaiKashiKamokuCdRyohi;
			shiwakeCheckData.kamokuEdabanNm = prefixJa + ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckData.kamokuEdabanCd = isKari ? kaigaiKamokuEdabanCdRyohi : kaigaiKashiKamokuEdabanCdRyohi;
			shiwakeCheckData.futanBumonNm = prefixJa + ks.futanBumon.getName() + "コード";
			shiwakeCheckData.futanBumonCd = isKari ? kaigaiFutanBumonCdRyohi : kaigaiKashiFutanBumonCdRyohi;
			shiwakeCheckData.kazeiKbnNm = (isKari ? "借方" : "貸方") + "課税区分";
			shiwakeCheckData.kazeiKbn = isKari ? kaigaiKazeiKbnRyohi : kaigaiKashiKazeiKbnRyohi;
		}
		shiwakeCheckData.torihikisakiNm = prefixJa + ks.torihikisaki.getName() + "コード";
		shiwakeCheckData.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get(prefixEn + "torihikisaki_cd" + suffix)) ? kaigaiTorihikisakiCdRyohi : (String)shiwakePattern.get(prefixEn + "torihikisaki_cd" + suffix);
		shiwakeCheckData.projectNm =  prefixJa + ks.project.getName() + "コード";
		if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get(prefixEn + "project_cd" + suffix)))
		{
			shiwakeCheckData.projectCd = kaigaiProjectCdRyohi;
		}
		shiwakeCheckData.segmentNm =  prefixJa + ks.segment.getName() + "コード";
		if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get(prefixEn + "segment_cd" + suffix)))
		{
			shiwakeCheckData.segmentCd = kaigaiSegmentCdRyohi;
		}
		shiwakeCheckData.uf1Nm = prefixJa + hfUfSeigyo.getUf1Name();
		shiwakeCheckData.uf2Nm = prefixJa + hfUfSeigyo.getUf2Name();
		shiwakeCheckData.uf3Nm = prefixJa + hfUfSeigyo.getUf3Name();
		shiwakeCheckData.uf4Nm = prefixJa + hfUfSeigyo.getUf4Name();
		shiwakeCheckData.uf5Nm = prefixJa + hfUfSeigyo.getUf5Name();
		shiwakeCheckData.uf6Nm = prefixJa + hfUfSeigyo.getUf6Name();
		shiwakeCheckData.uf7Nm = prefixJa + hfUfSeigyo.getUf7Name();
		shiwakeCheckData.uf8Nm = prefixJa + hfUfSeigyo.getUf8Name();
		shiwakeCheckData.uf9Nm = prefixJa + hfUfSeigyo.getUf9Name();
		shiwakeCheckData.uf10Nm = prefixJa + hfUfSeigyo.getUf10Name();
		String[][] ufCdRyohiArrays =
			{
				{ kaigaiKariUf1CdRyohi, kaigaiKariUf2CdRyohi, kaigaiKariUf3CdRyohi, kaigaiKariUf4CdRyohi, kaigaiKariUf5CdRyohi, kaigaiKariUf6CdRyohi, kaigaiKariUf7CdRyohi, kaigaiKariUf8CdRyohi, kaigaiKariUf9CdRyohi, kaigaiKariUf10CdRyohi },
				{ kaigaiKashiUf1Cd1Ryohi, kaigaiKashiUf2Cd1Ryohi, kaigaiKashiUf3Cd1Ryohi, kaigaiKashiUf4Cd1Ryohi, kaigaiKashiUf5Cd1Ryohi, kaigaiKashiUf6Cd1Ryohi, kaigaiKashiUf7Cd1Ryohi, kaigaiKashiUf8Cd1Ryohi, kaigaiKashiUf9Cd1Ryohi, kaigaiKashiUf10Cd1Ryohi },
				{ kaigaiKashiUf1Cd2Ryohi, kaigaiKashiUf2Cd2Ryohi, kaigaiKashiUf3Cd2Ryohi, kaigaiKashiUf4Cd2Ryohi, kaigaiKashiUf5Cd2Ryohi, kaigaiKashiUf6Cd2Ryohi, kaigaiKashiUf7Cd2Ryohi, kaigaiKashiUf8Cd2Ryohi, kaigaiKashiUf9Cd2Ryohi, kaigaiKashiUf10Cd2Ryohi },
				{ kaigaiKashiUf1Cd3Ryohi, kaigaiKashiUf2Cd3Ryohi, kaigaiKashiUf3Cd3Ryohi, kaigaiKashiUf4Cd3Ryohi, kaigaiKashiUf5Cd3Ryohi, kaigaiKashiUf6Cd3Ryohi, kaigaiKashiUf7Cd3Ryohi, kaigaiKashiUf8Cd3Ryohi, kaigaiKashiUf9Cd3Ryohi, kaigaiKashiUf10Cd3Ryohi },
				{ kaigaiKashiUf1Cd4Ryohi, kaigaiKashiUf2Cd4Ryohi, kaigaiKashiUf3Cd4Ryohi, kaigaiKashiUf4Cd4Ryohi, kaigaiKashiUf5Cd4Ryohi, kaigaiKashiUf6Cd4Ryohi, kaigaiKashiUf7Cd4Ryohi, kaigaiKashiUf8Cd4Ryohi, kaigaiKashiUf9Cd4Ryohi, kaigaiKashiUf10Cd4Ryohi },
				{ kaigaiKashiUf1Cd5Ryohi, kaigaiKashiUf2Cd5Ryohi, kaigaiKashiUf3Cd5Ryohi, kaigaiKashiUf4Cd5Ryohi, kaigaiKashiUf5Cd5Ryohi, kaigaiKashiUf6Cd5Ryohi, kaigaiKashiUf7Cd5Ryohi, kaigaiKashiUf8Cd5Ryohi, kaigaiKashiUf9Cd5Ryohi, kaigaiKashiUf10Cd5Ryohi }
			};
		
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf1_cd" + suffix)))
		{
			shiwakeCheckData.uf1Cd = ufCdRyohiArrays[i][0];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf2_cd" + suffix)))
		{
			shiwakeCheckData.uf2Cd = ufCdRyohiArrays[i][1];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf3_cd" + suffix)))
		{
			shiwakeCheckData.uf3Cd = ufCdRyohiArrays[i][2];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf4_cd" + suffix)))
		{
			shiwakeCheckData.uf4Cd = ufCdRyohiArrays[i][3];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf5_cd" + suffix)))
		{
			shiwakeCheckData.uf5Cd = ufCdRyohiArrays[i][4];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf6_cd" + suffix)))
		{
			shiwakeCheckData.uf6Cd = ufCdRyohiArrays[i][5];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf7_cd" + suffix)))
		{
			shiwakeCheckData.uf7Cd = ufCdRyohiArrays[i][6];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf8_cd" + suffix)))
		{
			shiwakeCheckData.uf8Cd = ufCdRyohiArrays[i][7];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf9_cd" + suffix)))
		{
			shiwakeCheckData.uf9Cd = ufCdRyohiArrays[i][8];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf10_cd" + suffix)))
		{
			shiwakeCheckData.uf10Cd = ufCdRyohiArrays[i][9];
		}
		String[] kbnArray = { SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5 };
		commonLogic.checkShiwake(denpyouKbnTmp, kbnArray[i], shiwakeCheckData, daihyouBumonCd, errorList);
	}

//＜伝票共通から呼ばれるイベント処理＞
	
	@Override
	protected void displaySashihikiTankaKaigai()
	{
		sashihikiTankaKaigai = sashihikiTankaKaigaiSI;

		// 差引単価（海外）を差引単価外貨×差引レートで上書き
		if(sashihikiHyoujiFlgKaigaiGaika) {
			sashihikiHeishuCdKaigai = setting.sashihikiTankaKaigaiGaikaHeishu();
			GMap rateMastermap = masterLogic.findHeishuCd(sashihikiHeishuCdKaigai);
			if(null != rateMastermap) sashihikiRateKaigai = formatMoneyDecimalNoComma(rateMastermap.get("rate"));
			sashihikiTankaKaigaiGaika = formatMoneyDecimal(toDecimal(setting.sashihikiTankaKaigaiGaika()));
			sashihikiTankaKaigai = formatMoney(commonLogic.calSashihikiTankaKaigai(toDecimal(sashihikiTankaKaigaiGaika), toDecimal(sashihikiRateKaigai)));
		}
	}
	
	@Override
	protected void displayKazeiFlgRyohi(ShiwakePatternMaster record)
	{
		kazeiFlgRyohi = sysLogic.judgeKazeiFlg(record.kariKazeiKbn, record.kariKamokuCd);
	}
	
	@Override
	protected void displayKaigaiShiwakeRyohi(List<ShiwakePatternMaster> kaigaiList)
	{
		if(!kaigaiList.isEmpty() && kaigaiList.size() == 1) {
			kaigaiShiwakeEdaNoRyohi = Integer.toString(kaigaiList.get(0).shiwakeEdano);
			kaigaiTorihikiNameRyohi =  kaigaiList.get(0).torihikiName;
			kaigaiTekiyouRyohi = kaigaiTorihikiNameRyohi;
			kazeiFlgRyohiKaigai = sysLogic.judgeKazeiFlg(kaigaiList.get(0).kariKazeiKbn, kaigaiList.get(0).kariKamokuCd);
			if(kaigaiList.get(0).tekiyouFlg.equals("1") ) {
				kaigaiTekiyouRyohi = kaigaiList.get(0).tekiyou;
			}
			shiharaihouhou = SHIHARAI_HOUHOU.FURIKOMI;
			this.kaigaiKazeiKbnRyohi = kaigaiList.get(0).kariKazeiKbn;
			this.kaigaiBunriKbn = kaigaiList.get(0).kariBunriKbn;
			this.kaigaiKariShiireKbn = kaigaiList.get(0).kariShiireKbn;
			if(!List.of("<FUTAN>","<DAIHYOUBUMON>","<SYOKIDAIHYOU>").contains(kaigaiList.get(0).kariFutanBumonCd)) {
				this.kaigaiFutanBumonCdRyohi = kaigaiList.get(0).kariFutanBumonCd;
			}
			if(!kaigaiList.get(0).kariTorihikisakiCd.equals("<TORIHIKI>")) {
				this.kaigaiTorihikisakiCdRyohi = kaigaiList.get(0).kariTorihikisakiCd;
			}
			if(!kaigaiList.get(0).kariSegmentCd.equals("<SG>")) {
				this.kaigaiSegmentCdRyohi = kaigaiList.get(0).kariSegmentCd;
			}
			if(!kaigaiList.get(0).kariProjectCd.equals("<PROJECT>")) {
				this.kaigaiProjectCdRyohi = kaigaiList.get(0).kariProjectCd;
			}
		}
	}

	//登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {
		super.commonCheckKobetsuDenpyou(connection, true);
	}

	//登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {
		//申請内容登録
		KaigaiRyohiseisan dto = this.createDto();
		this.kaigaiRyohiseisanDao.insert(dto, dto.tourokuUserId);

		// 明細必須フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細登録（旅費）
			if (!shubetsuCd[0].isEmpty()) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					KaigaiRyohiseisanMeisai kaigaiRyohiseisanMeisai =this.createkaigaiRyohiseisanMeisai(i);
					kaigaiRyohiseisanMeisaiDao.insert(kaigaiRyohiseisanMeisai, super.getUser().getTourokuOrKoushinUserId());
					if(!isEmpty(himodukeCardMeisaiRyohi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
					}
				}
			}

			//明細登録（経費）
			if (!shiwakeEdaNo[0].isEmpty()) {
				for (int i = 0; i < shiwakeEdaNo.length; i++) {
					KaigaiRyohiseisanKeihiMeisai keihiMeisaiDto = this.createKaigaiRyohiseisanKeihiMeisai(i);
					this.kaigaiRyohiseisanKeihiMeisaiDao.insert(keihiMeisaiDto, keihiMeisaiDto.tourokuUserId);
					if(!isEmpty(himodukeCardMeisaiKeihi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
					}
				}
			}
		}
	}

	//更新ボタン押下時に、個別伝票について以下を行う。
	//・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	//・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		super.commonCheckKobetsuDenpyou(connection, false);
		if (0 <errorList.size())
		{
			return;
		}

		//申請内容更新
		KaigaiRyohiseisan dto = this.createDto();
		this.kaigaiRyohiseisanDao.update(dto, dto.koushinUserId);

		//明細削除＆登録
		kaigaiRyohiseisanMeisaiDao.deleteByDenpyouId(denpyouId);
		this.kaigaiRyohiseisanKeihiMeisaiDao.deleteByDenpyouId(this.denpyouId);
		hcLogic.removeDenpyouHimozuke(denpyouId);

		if (judgeKeihiEntry()) {
			//明細登録（旅費）
			if (!shubetsuCd[0].isEmpty()) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					KaigaiRyohiseisanMeisai kaigaiRyohiseisanMeisai = this.createkaigaiRyohiseisanMeisai(i);
					kaigaiRyohiseisanMeisaiDao.insert(kaigaiRyohiseisanMeisai, super.getUser().getTourokuOrKoushinUserId());
					if(!isEmpty(himodukeCardMeisaiRyohi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
					}
				}
			}
			//明細登録（経費）
			if (!shiwakeEdaNo[0].isEmpty()) {
				for (int i = 0; i < shiwakeEdaNo.length; i++) {
					KaigaiRyohiseisanKeihiMeisai keihiMeisaiDto = this.createKaigaiRyohiseisanKeihiMeisai(i);
					this.kaigaiRyohiseisanKeihiMeisaiDao.insert(keihiMeisaiDto, keihiMeisaiDto.tourokuUserId);
					if(!isEmpty(himodukeCardMeisaiKeihi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
					}
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

		//支払日・計上日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			GMap data = kaikeiLogic.findKaigaiRyohiSeisan(denpyouId);
			String shiharai = formatDate(data.get("shiharaibi"));
			String keijou = hasseiShugi ? formatDate(data.get("keijoubi"))  : formatDate(data.get("seisankikan_to"));
			String keijouName = hasseiShugi ? "計上日" : ks.seisankikan.getName() + "終了日";
			if(judgeKeihiEntry() && hasseiShugi && isEmpty(keijou)) errorList.add(keijouName + "が未登録です。" + keijouName +"を登録してください。");
			checkShiharaiBi(shiharai, keijou, keijouName);
			// エラーの場合の表示用に現実の値を設定
			shiharaibi = shiharai;
			if (hasseiShugi)
			{
				keijoubi = keijou;
			} else seisankikanTo = keijou;
		}

		//仮払指定時かつ最終承認時は仮払伝票の精算完了日を更新
		if( (!isEmpty(karibaraiDenpyouId)) && wfEventLogic.isLastShounin(denpyouId,loginUserInfo)){
			kaigaiRyohiseisanDao.updateKaribaraiSeisanbi(karibaraiDenpyouId,loginUserId);
		}
	}

	//代表仕訳枝番号を設定
	@Override
	protected String getDaihyouTorihiki(){
		return kaigaiShiwakeEdaNoRyohi;
	}

	// 起案終了／起案終了解除の対象とする仮払伝票IDを取得する。
	@Override
	protected String getKianShuuryouKaribaraiDenpyouId(){
		String result = null;

		// 仮払申請が予算執行対象
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
		if(YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(denpyouShubetsuMap.get("yosan_shikkou_taishou").toString())){

			// 仮払金未使用 または 出張中止
			if("1".equals(this.karibaraiMishiyouFlg) || "1".equals(this.shucchouChuushiFlg)){
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
		if(isNotEmpty(kaigaiKamokuCdRyohi)) kamokuCdSet.add(kaigaiKamokuCdRyohi);
		if(isNotEmpty(kamokuCdRyohi)) kamokuCdSet.add(kamokuCdRyohi);
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
	@Override
	protected void initParts(EteamConnection connection) {
		super.initParts(connection);
		kaigaiRyohiSeisanLogic = EteamContainer.getComponent(KaigaiRyohiSeisanLogic.class, connection);
		batchKaikeiLogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		kaigaiRyohiseisanDao = EteamContainer.getComponent(KaigaiRyohiseisanDao.class, connection);
		kaigaiRyohiseisanMeisaiDao = EteamContainer.getComponent(KaigaiRyohiseisanMeisaiDao.class, connection);
		kaigaiRyohiseisanKeihiMeisaiDao = EteamContainer.getComponent(KaigaiRyohiseisanKeihiMeisaiDao.class, connection);
	}
	
	@Override
	protected void resetKaigaiKoumoku()
	{
		kazeiFlgRyohi = "";
		kaigaiShiwakeEdaNoRyohi = "";
		kaigaiTorihikiNameRyohi = "";
		kaigaiKamokuCdRyohi = "";
		kaigaiKamokuNameRyohi = "";
		kaigaiKamokuEdabanCdRyohi = "";
		kaigaiKamokuEdabanNameRyohi = "";
		kaigaiFutanBumonCdRyohi = "";
		kaigaiFutanBumonNameRyohi = "";
		kaigaiTorihikisakiCdRyohi = "";
		kaigaiTorihikisakiNameRyohi = "";
		kaigaiProjectCdRyohi = "";
		kaigaiProjectNameRyohi = "";
		kaigaiSegmentCdRyohi = "";
		kaigaiSegmentNameRyohi = "";
		kaigaiKazeiKbnRyohi = "";
		kaigaiKashiFutanBumonCdRyohi= "";
		kaigaiKashiFutanBumonNameRyohi= "";
		kaigaiKashiKamokuCdRyohi = "";
		kaigaiKashiKamokuNameRyohi = "";
		kaigaiKashiKamokuEdabanCdRyohi= "";
		kaigaiKashiKamokuEdabanNameRyohi= "";
		kaigaiKashiKazeiKbnRyohi = "";
		kazeiFlgRyohiKaigai = "";
		kaigaiTekiyouRyohi = "";
		kaigaiTekiyouCdRyohi = "";
		sashihikiNumKaigai = "";
		sashihikiKingakuKaigai = "";
		sashihikiHeishuCdKaigai = "";
		sashihikiRateKaigai = "";
		sashihikiTankaKaigaiGaika = "";
		this.kaigaiBunriKbn = "";
		this.kaigaiKariShiireKbn = "";
		this.kaigaiKashiShiireKbn = "";
	}

	/**
	 * 旅費精算テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 旅費精算レコード
	 * @param sanshou 参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection コネクション
	 */
	protected void shinseiData2Gamen(GMap shinseiData, boolean sanshou, EteamConnection connection) {
		super.shinseiData2Gamen(shinseiData, sanshou, connection);
		kazeiFlgRyohi = (String)shinseiData.get("ryohi_kazei_flg");
		kaigaiShiwakeEdaNoRyohi = ((shinseiData.get("kaigai_shiwake_edano") == null) ? "" : ((Integer)shinseiData.get("kaigai_shiwake_edano")).toString());
		kaigaiTorihikiNameRyohi = (String)shinseiData.get("kaigai_torihiki_name");
		kaigaiKamokuCdRyohi = (String)shinseiData.get("kaigai_kari_kamoku_cd");
		kaigaiKamokuNameRyohi = (String)shinseiData.get("kaigai_kari_kamoku_name");
		kaigaiKamokuEdabanCdRyohi = (String)shinseiData.get("kaigai_kari_kamoku_edaban_cd");
		kaigaiKamokuEdabanNameRyohi = (String)shinseiData.get("kaigai_kari_kamoku_edaban_name");
		kaigaiFutanBumonCdRyohi = (String)shinseiData.get("kaigai_kari_futan_bumon_cd");
		kaigaiFutanBumonNameRyohi = (String)shinseiData.get("kaigai_kari_futan_bumon_name");
		kaigaiTorihikisakiCdRyohi = (String) shinseiData.get("kaigai_torihikisaki_cd");
		kaigaiTorihikisakiNameRyohi = (String) shinseiData.get("kaigai_torihikisaki_name_ryakushiki");
		kaigaiProjectCdRyohi = (String) shinseiData.get("kaigai_project_cd");
		kaigaiProjectNameRyohi = (String) shinseiData.get("kaigai_project_name");
		kaigaiSegmentCdRyohi = (String) shinseiData.get("kaigai_segment_cd");
		kaigaiSegmentNameRyohi = (String) shinseiData.get("kaigai_segment_name_ryakushiki");
		kazeiFlgRyohiKaigai = (String) shinseiData.get("kaigai_kazei_flg");
		kaigaiUf1CdRyohi = (String) shinseiData.get("kaigai_uf1_cd");
		kaigaiUf1NameRyohi = (String) shinseiData.get("kaigai_uf1_name_ryakushiki");
		kaigaiUf2CdRyohi = (String) shinseiData.get("kaigai_uf2_cd");
		kaigaiUf2NameRyohi = (String) shinseiData.get("kaigai_uf2_name_ryakushiki");
		kaigaiUf3CdRyohi = (String) shinseiData.get("kaigai_uf3_cd");
		kaigaiUf3NameRyohi = (String) shinseiData.get("kaigai_uf3_name_ryakushiki");
		kaigaiUf4CdRyohi = (String) shinseiData.get("kaigai_uf4_cd");
		kaigaiUf4NameRyohi = (String) shinseiData.get("kaigai_uf4_name_ryakushiki");
		kaigaiUf5CdRyohi = (String) shinseiData.get("kaigai_uf5_cd");
		kaigaiUf5NameRyohi = (String) shinseiData.get("kaigai_uf5_name_ryakushiki");
		kaigaiUf6CdRyohi = (String) shinseiData.get("kaigai_uf6_cd");
		kaigaiUf6NameRyohi = (String) shinseiData.get("kaigai_uf6_name_ryakushiki");
		kaigaiUf7CdRyohi = (String) shinseiData.get("kaigai_uf7_cd");
		kaigaiUf7NameRyohi = (String) shinseiData.get("kaigai_uf7_name_ryakushiki");
		kaigaiUf8CdRyohi = (String) shinseiData.get("kaigai_uf8_cd");
		kaigaiUf8NameRyohi = (String) shinseiData.get("kaigai_uf8_name_ryakushiki");
		kaigaiUf9CdRyohi = (String) shinseiData.get("kaigai_uf9_cd");
		kaigaiUf9NameRyohi = (String) shinseiData.get("kaigai_uf9_name_ryakushiki");
		kaigaiUf10CdRyohi = (String) shinseiData.get("kaigai_uf10_cd");
		kaigaiUf10NameRyohi = (String) shinseiData.get("kaigai_uf10_name_ryakushiki");
		kaigaiTekiyouRyohi = (String)shinseiData.get("kaigai_tekiyou");
		var chuuki = super.setChuuki(sanshou, shinseiData, null, batchKaikeiLogic, commonLogic);
		this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : chuuki[0];
		this.chuuki2Ryohi = chuuki[1];
		
		// 海外分
		shinseiData.put("kaigaiTekiyouFlg", "1");
		var kaigaiChuuki = super.setChuuki(sanshou, shinseiData, null, batchKaikeiLogic, commonLogic);
		shinseiData.remove("kaigaiTekiyouFlg");
		this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : kaigaiChuuki[0];
		this.kaigaiChuuki2Ryohi = kaigaiChuuki[1];
		
		if(sasihikiHyoujiFlgKaigai){
			sashihikiNumKaigai = (shinseiData.get("sashihiki_num_kaigai") == null)? "" : shinseiData.get("sashihiki_num_kaigai").toString();
			sashihikiTankaKaigai = (shinseiData.get("sashihiki_tanka_kaigai") == null)? "": formatMoney(shinseiData.get("sashihiki_tanka_kaigai"));
			sashihikiHeishuCdKaigai = shinseiData.getString("sashihiki_heishu_cd_kaigai");
			sashihikiRateKaigai = (shinseiData.get("sashihiki_rate_kaigai") == null)? "" : formatMoneyDecimalNoComma(shinseiData.get("sashihiki_rate_kaigai"));
			sashihikiTankaKaigaiGaika = (shinseiData.get("sashihiki_tanka_kaigai_gaika") == null)? "" : formatMoneyDecimal(shinseiData.get("sashihiki_tanka_kaigai_gaika"));
		} else {
			sashihikiNumKaigai = "";
			sashihikiTankaKaigai = "";
			sashihikiHeishuCdKaigai="";
			sashihikiRateKaigai = "";
			sashihikiTankaKaigaiGaika="";
		}

		this.kaigaiKazeiKbnRyohi = (String) shinseiData.get("kaigai_kari_kazei_kbn");
		this.kaigaiBunriKbn = (String) shinseiData.get("kaigai_bunri_kbn");
		this.kaigaiKariShiireKbn = (String) shinseiData.get("kaigai_kari_shiire_kbn");
		this.kaigaiKashiShiireKbn = (String) shinseiData.get("kaigai_kashi_shiire_kbn");
	}

	@Override
	protected void makeDefaultMeisaiKaigaiKoumoku()
	{
		setKaigaiMeisaiArray(-1);
	}
	
	@Override
	protected void setKaigaiMeisaiArray(int length)
	{
		boolean isDefault = length == -1;
		kaigaiFlgRyohi = isDefault ? new String[] { "" } : new String[length];
		heishuCdRyohi = isDefault ? new String[] { "" } : new String[length];
		rateRyohi = isDefault ? new String[] { "" } : new String[length];
		gaikaRyohi = isDefault ? new String[] { "" } : new String[length];
		taniRyohi = isDefault ? new String[] { "" } : new String[length];
		zeiKubun = isDefault ? new String[] { "" } : new String[length];
		kazeiFlgRyohiMeisai = isDefault ? new String[] { "" } : new String[length];
	}
	
	@Override
	protected void setKaigaiMeisaiValues(int i, GMap meisai)
	{
		kaigaiFlgRyohi[i] = (String)meisai.get("kaigai_flg");
		heishuCdRyohi[i] = (String)meisai.get("heishu_cd");
		rateRyohi[i] = formatMoneyDecimalNoComma(meisai.get("rate"));
		gaikaRyohi[i] = formatMoneyDecimal(meisai.get("gaika"));
		taniRyohi[i] = (String)meisai.get("currency_unit");
		zeiKubun[i] = (String)meisai.get("zei_kubun");
		kazeiFlgRyohiMeisai[i] = (String)meisai.get("kazei_flg");

		// 日当フラグは海外の時だけ上書き。国内の時は共通処理で処理済み。
		if("1".equals(kaigaiFlgRyohi[i])){
			nittouFlg[i] = commonLogic.isNittouKaigai(shubetsu1[i], shubetsu2[i], commonLogic.getYakushokuCd(userIdRyohi)) ? "1" : "";
		}
	}
	
	@Override
	protected void makeDefaultMeisaiKeihiKaigaiKoumoku()
	{
		setKaigaiMeisaiArrayKeihi(-1);
	}

	@Override
	protected void setKaigaiMeisaiArrayKeihi(int length)
	{
		boolean isDefault = length == -1;
		kaigaiFlg = isDefault ? new String[] { "" } : new String[length];
		heishuCd = isDefault ? new String[] { "" } : new String[length];
		rate = isDefault ? new String[] { "" } : new String[length];
		gaika = isDefault ? new String[] { "" } : new String[length];
		tani = isDefault ? new String[] { "" } : new String[length];
		kazeiFlg = isDefault ? new String[] { "" } : new String[length];
	}
	
	@Override
	protected void setKaigaiMeisaiValuesKeihi(int i, GMap meisai)
	{
		kaigaiFlg[i] = (String)meisai.get("kaigai_flg");
		kazeiFlg[i] = (String)meisai.get("kazei_flg");
		heishuCd[i] = (String)meisai.get("heishu_cd");
		rate[i] = formatMoneyDecimalNoComma(meisai.get("rate"));
		gaika[i] = formatMoneyDecimal(meisai.get("gaika"));
		tani[i] = (String)meisai.get("currency_unit");
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		super.displaySeigyo(connection);
		kaigaiKoutsuuShudanList = masterLogic.loadKaigaiKoutsuuShudan();
		kaigaiKoutsuuShudanDomain =  EteamCommon.mapList2Arr(kaigaiKoutsuuShudanList, "koutsuu_shudan");

		//入力可能時の制御
		if (enableInput) {
			// 仕訳パターンによる制御（海外）
			if (isNotEmpty(kaigaiShiwakeEdaNoRyohi)) {
				//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(this.denpyouKbn, Integer.parseInt(kaigaiShiwakeEdaNoRyohi));
				InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
				kaigaiKamokuEdabanEnableRyohi = info.kamokuEdabanEnable;
				kaigaiFutanBumonEnableRyohi = info.futanBumonEnable;
				kaigaiTorihikisakiEnableRyohi = info.torihikisakiEnable;
				kaigaiProjectEnableRyohi = info.projectEnable;
				kaigaiSegmentEnableRyohi = info.segmentEnable;
				kaigaiUf1EnableRyohi = info.uf1Enable;
				kaigaiUf2EnableRyohi = info.uf2Enable;
				kaigaiUf3EnableRyohi = info.uf3Enable;
				kaigaiUf4EnableRyohi = info.uf4Enable;
				kaigaiUf5EnableRyohi = info.uf5Enable;
				kaigaiUf6EnableRyohi = info.uf6Enable;
				kaigaiUf7EnableRyohi = info.uf7Enable;
				kaigaiUf8EnableRyohi = info.uf8Enable;
				kaigaiUf9EnableRyohi = info.uf9Enable;
				kaigaiUf10EnableRyohi = info.uf10Enable;
			}
		}

		//経理担当が初めて伝票を開き、その伝票が振込のとき、支払日に自動入力
		if (shiharaiBiMode == 1 && isEmpty(shiharaibi) && shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI)) {
			java.sql.Date shiharai = null;
			if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A011).equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(keijoubi));
			} else if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A011).equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(keijoubi));
			}
			if (shiharai != null) {
				shiharaibi = formatDate(shiharai);
				// マスターから基準日(計上日)に対する振込日が見つかった場合、支払日の更新
				this.kaigaiRyohiseisanDao.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}

		// 外貨の表示可否
		enableGaika = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
		// 外貨非表示なら差引項目外貨表示フラグ（海外）は無効化
		if (!enableGaika)
		{
			sashihikiHyoujiFlgKaigaiGaika = false;
		}


		
		//課税区分の制御
		if (isNotEmpty(kamokuCdRyohi)) {
			var kamoku = this.kamokuMasterDao.find(this.kamokuCdRyohi);
			this.shoriGroupRyohi = kamoku == null ? null : kamoku.shoriGroup;
		}
		//課税区分の制御
		if (isNotEmpty(kaigaiKamokuCdRyohi)) {
			var kamoku = this.kamokuMasterDao.find(this.kaigaiKamokuCdRyohi);
			this.kaigaiShoriGroupRyohi = kamoku == null ? null : kamoku.shoriGroup;
		}
	}

	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		// 国内仕訳までの共通部のリロード
		super.reloadShiwakePatternKokunai(connection);
		
		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		String shainCd = (usrInfo == null) ? "" : (String)usrInfo.get("shain_no");
		// 法人カード識別用番号取得
		String houjinCard = (usrInfo == null) ? "" : (String)usrInfo.get("houjin_card_shikibetsuyou_num");
		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(userIdRyohi);

		//海外の仕訳
		if (isNotEmpty(kaigaiShiwakeEdaNoRyohi)) {

			//仕訳パターン取得
			ShiwakePatternMaster shiwakeP = shiwakePatternMasterDao.find(super.denpyouKbn, Integer.parseInt(kaigaiShiwakeEdaNoRyohi));
			// 使用者の代表負担部門コード
			String daihyouBumonCd = super.daihyouFutanBumonCd;
			// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
			if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
				daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
			}

			//取引名
			kaigaiTorihikiNameRyohi = shiwakeP.torihikiName;

			//借方　科目
			kaigaiKamokuCdRyohi = shiwakeP.kariKamokuCd;

			//借方　科目枝番
			String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
			switch (pKariKamokuEdabanCd) {
				//何もしない(画面入力のまま)
				case EteamConst.ShiwakeConst.EDABAN:
					break;
				//固定コード値 or ブランク
				default:
					kaigaiKamokuEdabanCdRyohi = pKariKamokuEdabanCd;
					break;
			}

			//借方　UF1-10
			if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.shainCdRenkeiFlg.equals(("1"))){
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
				{
					 kaigaiUf1CdRyohi = shainCd;
				}
				if (ufno == 2)
				{
					 kaigaiUf2CdRyohi = shainCd;
				}
				if (ufno == 3)
				{
					 kaigaiUf3CdRyohi = shainCd;
				}
				if (ufno == 4)
				{
					 kaigaiUf4CdRyohi = shainCd;
				}
				if (ufno == 5)
				{
					 kaigaiUf5CdRyohi = shainCd;
				}
				if (ufno == 6)
				{
					 kaigaiUf6CdRyohi = shainCd;
				}
				if (ufno == 7)
				{
					 kaigaiUf7CdRyohi = shainCd;
				}
				if (ufno == 8)
				{
					 kaigaiUf8CdRyohi = shainCd;
				}
				if (ufno == 9)
				{
					 kaigaiUf9CdRyohi = shainCd;
				}
				if (ufno == 10)
				{
					kaigaiUf10CdRyohi = shainCd;
				}
			}
			kaigaiKariUf1CdRyohi = commonLogic.convUf(kaigaiUf1CdRyohi, shiwakeP.kariUf1Cd);
			kaigaiKariUf2CdRyohi = commonLogic.convUf(kaigaiUf2CdRyohi, shiwakeP.kariUf2Cd);
			kaigaiKariUf3CdRyohi = commonLogic.convUf(kaigaiUf3CdRyohi, shiwakeP.kariUf3Cd);
			kaigaiKariUf4CdRyohi = commonLogic.convUf(kaigaiUf4CdRyohi, shiwakeP.kariUf4Cd);
			kaigaiKariUf5CdRyohi = commonLogic.convUf(kaigaiUf5CdRyohi, shiwakeP.kariUf5Cd);
			kaigaiKariUf6CdRyohi = commonLogic.convUf(kaigaiUf6CdRyohi, shiwakeP.kariUf6Cd);
			kaigaiKariUf7CdRyohi = commonLogic.convUf(kaigaiUf7CdRyohi, shiwakeP.kariUf7Cd);
			kaigaiKariUf8CdRyohi = commonLogic.convUf(kaigaiUf8CdRyohi, shiwakeP.kariUf8Cd);
			kaigaiKariUf9CdRyohi = commonLogic.convUf(kaigaiUf9CdRyohi, shiwakeP.kariUf9Cd);
			kaigaiKariUf10CdRyohi = commonLogic.convUf(kaigaiUf10CdRyohi, shiwakeP.kariUf10Cd);

			//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
			enableKaigaiBumonSecurity = true;
			//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
			if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) ){
				enableKaigaiBumonSecurity = commonLogic.checkFutanBumonWithSecurity(kaigaiFutanBumonCdRyohi, "海外" + ks1.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
			}
			//借方　負担部門
			kaigaiFutanBumonCdRyohi = commonLogic.convFutanBumon(kaigaiFutanBumonCdRyohi, shiwakeP.kariFutanBumonCd, daihyouBumonCd);
			// 代表部門の時の仕入区分
			if(List.of(EteamConst.ShiwakeConst.DAIHYOUBUMON, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) && this.kaigaiFutanBumonCdRyohi.equals(daihyouBumonCd)) {
				// 一旦代表部門の仕入区分をセット
				var shiireKbn = this.bumonMasterDao.find(this.kaigaiFutanBumonCdRyohi).shiireKbn;
				this.kaigaiKariShiireKbn = shiireKbn == null ? this.kaigaiKariShiireKbn : shiireKbn.toString();
				// 処理グループ&課税区分、仕入税額按分を考慮し、使用不可なら0に戻す
				if(kaigaiShoriGroupRyohi == null) {
					kaigaiShoriGroupRyohi = this.kamokuMasterDao.find(this.kaigaiKamokuCdRyohi).shoriGroup;
				}
				if(!this.commonLogic.isValidShiireKbn(this.kaigaiShoriGroupRyohi.toString(), this.kaigaiKazeiKbnRyohi, this.kaigaiKariShiireKbn, this.getShiireZeiAnbun())) {
					this.kaigaiKariShiireKbn = "0";
				}
			}
			
			// 借方　課税区分
			//kaigaiKazeiKbnRyohi = shiwakeP.kariKazeiKbn;

			//支払方法により貸方1 or 貸方2を切替する
			switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					//振込
					kaigaiKashiKamokuCdRyohi = shiwakeP.kashiKamokuCd1; //貸方1　科目コード
					//貸方1　科目枝番コード
					String pKashiKamokuEdabanCd = shiwakeP.kashiKamokuEdabanCd1;
					this.kaigaiKashiKamokuEdabanCdRyohi = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
							? shainShiwakeEdaNo
							: pKashiKamokuEdabanCd;
					kaigaiKashiFutanBumonCdRyohi = shiwakeP.kashiFutanBumonCd1; //貸方1　負担部門コード
					kaigaiKashiKazeiKbnRyohi = shiwakeP.kashiKazeiKbn1; //貸方1　課税区分
					kaigaiKashiShiireKbn = (shiwakeP.kashiShiireKbn1 != null) ? shiwakeP.kashiShiireKbn1 : "" ;
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					//現金
					kaigaiKashiKamokuCdRyohi = shiwakeP.kashiKamokuCd2; //貸方2　科目コード
					kaigaiKashiKamokuEdabanCdRyohi = shiwakeP.kashiKamokuEdabanCd2; //貸方2　科目枝番コード
					kaigaiKashiFutanBumonCdRyohi = shiwakeP.kashiFutanBumonCd2; //貸方2　負担部門コード
					kaigaiKashiKazeiKbnRyohi = shiwakeP.kashiKazeiKbn2; //貸方2　課税区分
					kaigaiKashiShiireKbn = (shiwakeP.kashiShiireKbn2 != null) ? shiwakeP.kashiShiireKbn2 : "" ;
					break;
			}

			//代表部門
			kaigaiKashiFutanBumonCdRyohi = commonLogic.convFutanBumon(kaigaiFutanBumonCdRyohi, kaigaiKashiFutanBumonCdRyohi, daihyouBumonCd);

			kaigaiKashiUf1Cd1Ryohi = commonLogic.convUf(kaigaiUf1CdRyohi, shiwakeP.kashiUf1Cd1);
			kaigaiKashiUf2Cd1Ryohi = commonLogic.convUf(kaigaiUf2CdRyohi, shiwakeP.kashiUf2Cd1);
			kaigaiKashiUf3Cd1Ryohi = commonLogic.convUf(kaigaiUf3CdRyohi, shiwakeP.kashiUf3Cd1);
			kaigaiKashiUf4Cd1Ryohi = commonLogic.convUf(kaigaiUf4CdRyohi, shiwakeP.kashiUf4Cd1);
			kaigaiKashiUf5Cd1Ryohi = commonLogic.convUf(kaigaiUf5CdRyohi, shiwakeP.kashiUf5Cd1);
			kaigaiKashiUf6Cd1Ryohi = commonLogic.convUf(kaigaiUf6CdRyohi, shiwakeP.kashiUf6Cd1);
			kaigaiKashiUf7Cd1Ryohi = commonLogic.convUf(kaigaiUf7CdRyohi, shiwakeP.kashiUf7Cd1);
			kaigaiKashiUf8Cd1Ryohi = commonLogic.convUf(kaigaiUf8CdRyohi, shiwakeP.kashiUf8Cd1);
			kaigaiKashiUf9Cd1Ryohi = commonLogic.convUf(kaigaiUf9CdRyohi, shiwakeP.kashiUf9Cd1);

			kaigaiKashiUf1Cd2Ryohi = commonLogic.convUf(kaigaiUf1CdRyohi, shiwakeP.kashiUf1Cd2);
			kaigaiKashiUf2Cd2Ryohi = commonLogic.convUf(kaigaiUf2CdRyohi, shiwakeP.kashiUf2Cd2);
			kaigaiKashiUf3Cd2Ryohi = commonLogic.convUf(kaigaiUf3CdRyohi, shiwakeP.kashiUf3Cd2);
			kaigaiKashiUf4Cd2Ryohi = commonLogic.convUf(kaigaiUf4CdRyohi, shiwakeP.kashiUf4Cd2);
			kaigaiKashiUf5Cd2Ryohi = commonLogic.convUf(kaigaiUf5CdRyohi, shiwakeP.kashiUf5Cd2);
			kaigaiKashiUf6Cd2Ryohi = commonLogic.convUf(kaigaiUf6CdRyohi, shiwakeP.kashiUf6Cd2);
			kaigaiKashiUf7Cd2Ryohi = commonLogic.convUf(kaigaiUf7CdRyohi, shiwakeP.kashiUf7Cd2);
			kaigaiKashiUf8Cd2Ryohi = commonLogic.convUf(kaigaiUf8CdRyohi, shiwakeP.kashiUf8Cd2);
			kaigaiKashiUf9Cd2Ryohi = commonLogic.convUf(kaigaiUf9CdRyohi, shiwakeP.kashiUf9Cd2);
			kaigaiKashiUf10Cd2Ryohi = commonLogic.convUf(kaigaiUf10CdRyohi, shiwakeP.kashiUf10Cd2);

			kaigaiKashiUf1Cd3Ryohi = commonLogic.convUf(kaigaiUf1CdRyohi, shiwakeP.kashiUf1Cd3);
			kaigaiKashiUf2Cd3Ryohi = commonLogic.convUf(kaigaiUf2CdRyohi, shiwakeP.kashiUf2Cd3);
			kaigaiKashiUf3Cd3Ryohi = commonLogic.convUf(kaigaiUf3CdRyohi, shiwakeP.kashiUf3Cd3);
			kaigaiKashiUf4Cd3Ryohi = commonLogic.convUf(kaigaiUf4CdRyohi, shiwakeP.kashiUf4Cd3);
			kaigaiKashiUf5Cd3Ryohi = commonLogic.convUf(kaigaiUf5CdRyohi, shiwakeP.kashiUf5Cd3);
			kaigaiKashiUf6Cd3Ryohi = commonLogic.convUf(kaigaiUf6CdRyohi, shiwakeP.kashiUf6Cd3);
			kaigaiKashiUf7Cd3Ryohi = commonLogic.convUf(kaigaiUf7CdRyohi, shiwakeP.kashiUf7Cd3);
			kaigaiKashiUf8Cd3Ryohi = commonLogic.convUf(kaigaiUf8CdRyohi, shiwakeP.kashiUf8Cd3);
			kaigaiKashiUf9Cd3Ryohi = commonLogic.convUf(kaigaiUf9CdRyohi, shiwakeP.kashiUf9Cd3);
			kaigaiKashiUf10Cd3Ryohi = commonLogic.convUf(kaigaiUf10CdRyohi, shiwakeP.kashiUf10Cd3);

			kaigaiKashiUf1Cd4Ryohi = commonLogic.convUf(kaigaiUf1CdRyohi, shiwakeP.kashiUf1Cd4);
			kaigaiKashiUf2Cd4Ryohi = commonLogic.convUf(kaigaiUf2CdRyohi, shiwakeP.kashiUf2Cd4);
			kaigaiKashiUf3Cd4Ryohi = commonLogic.convUf(kaigaiUf3CdRyohi, shiwakeP.kashiUf3Cd4);
			kaigaiKashiUf4Cd4Ryohi = commonLogic.convUf(kaigaiUf4CdRyohi, shiwakeP.kashiUf4Cd4);
			kaigaiKashiUf5Cd4Ryohi = commonLogic.convUf(kaigaiUf5CdRyohi, shiwakeP.kashiUf5Cd4);
			kaigaiKashiUf6Cd4Ryohi = commonLogic.convUf(kaigaiUf6CdRyohi, shiwakeP.kashiUf6Cd4);
			kaigaiKashiUf7Cd4Ryohi = commonLogic.convUf(kaigaiUf7CdRyohi, shiwakeP.kashiUf7Cd4);
			kaigaiKashiUf8Cd4Ryohi = commonLogic.convUf(kaigaiUf8CdRyohi, shiwakeP.kashiUf8Cd4);
			kaigaiKashiUf9Cd4Ryohi = commonLogic.convUf(kaigaiUf9CdRyohi, shiwakeP.kashiUf9Cd4);
			kaigaiKashiUf10Cd4Ryohi = commonLogic.convUf(kaigaiUf10CdRyohi, shiwakeP.kashiUf10Cd4);

			kaigaiKashiUf1Cd5Ryohi = commonLogic.convUf(kaigaiUf1CdRyohi, shiwakeP.kashiUf1Cd5);
			kaigaiKashiUf2Cd5Ryohi = commonLogic.convUf(kaigaiUf2CdRyohi, shiwakeP.kashiUf2Cd5);
			kaigaiKashiUf3Cd5Ryohi = commonLogic.convUf(kaigaiUf3CdRyohi, shiwakeP.kashiUf3Cd5);
			kaigaiKashiUf4Cd5Ryohi = commonLogic.convUf(kaigaiUf4CdRyohi, shiwakeP.kashiUf4Cd5);
			kaigaiKashiUf5Cd5Ryohi = commonLogic.convUf(kaigaiUf5CdRyohi, shiwakeP.kashiUf5Cd5);
			kaigaiKashiUf6Cd5Ryohi = commonLogic.convUf(kaigaiUf6CdRyohi, shiwakeP.kashiUf6Cd5);
			kaigaiKashiUf7Cd5Ryohi = commonLogic.convUf(kaigaiUf7CdRyohi, shiwakeP.kashiUf7Cd5);
			kaigaiKashiUf8Cd5Ryohi = commonLogic.convUf(kaigaiUf8CdRyohi, shiwakeP.kashiUf8Cd5);
			kaigaiKashiUf9Cd5Ryohi = commonLogic.convUf(kaigaiUf9CdRyohi, shiwakeP.kashiUf9Cd5);
			kaigaiKashiUf10Cd5Ryohi = commonLogic.convUf(kaigaiUf10CdRyohi, shiwakeP.kashiUf10Cd5);

			//画面に反映
			if (!isEmpty(kaigaiKariUf1CdRyohi))
			{
				kaigaiUf1CdRyohi = kaigaiKariUf1CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf2CdRyohi))
			{
				kaigaiUf2CdRyohi = kaigaiKariUf2CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf3CdRyohi))
			{
				kaigaiUf3CdRyohi = kaigaiKariUf3CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf4CdRyohi))
			{
				kaigaiUf4CdRyohi = kaigaiKariUf4CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf5CdRyohi))
			{
				kaigaiUf5CdRyohi = kaigaiKariUf5CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf6CdRyohi))
			{
				kaigaiUf6CdRyohi = kaigaiKariUf6CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf7CdRyohi))
			{
				kaigaiUf7CdRyohi = kaigaiKariUf7CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf8CdRyohi))
			{
				kaigaiUf8CdRyohi = kaigaiKariUf8CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf9CdRyohi))
			{
				kaigaiUf9CdRyohi = kaigaiKariUf9CdRyohi;
			}
			if (!isEmpty(kaigaiKariUf10CdRyohi))
			{
				kaigaiUf10CdRyohi = kaigaiKariUf10CdRyohi;
			}

			//社員コードを摘要コードに反映する場合
			if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
				if(shainCd.length() > 4) {
					kaigaiTekiyouCdRyohi = shainCd.substring(shainCd.length()-4);
				} else {
					kaigaiTekiyouCdRyohi = shainCd;
				}
			//法人カード識別用番号を摘要コードに反映する場合
			} else if("T".equals(setting.houjinCardRenkei())) {
				if(houjinCard.length() > 4) {
					kaigaiTekiyouCdRyohi = houjinCard.substring(houjinCard.length()-4);
				} else {
					kaigaiTekiyouCdRyohi = houjinCard;
				}
			}  else {
				kaigaiTekiyouCdRyohi = "";
			}
		}

		// 経費部分のリロード
		super.reloadShiwakePatternKeihi();
	}
	
	protected void reloadKaigaiMaster()
	{
		kaigaiKamokuNameRyohi = this.kamokuMasterDao.findKamokuNameStr(kaigaiKamokuCdRyohi);
		kaigaiKamokuEdabanNameRyohi = this.kamokuEdabanZandakaDao.findEdabanName(kaigaiKamokuCdRyohi, kaigaiKamokuEdabanCdRyohi);
		kaigaiFutanBumonNameRyohi = enableKaigaiBumonSecurity ? this.bumonMasterDao.findFutanBumonName(kaigaiFutanBumonCdRyohi) : "";
		kaigaiTorihikisakiNameRyohi = masterLogic.findTorihikisakiName(kaigaiTorihikisakiCdRyohi);
		kaigaiProjectNameRyohi = masterLogic.findProjectName(kaigaiProjectCdRyohi);
		kaigaiSegmentNameRyohi = masterLogic.findSegmentName(kaigaiSegmentCdRyohi);

		kaigaiUf1NameRyohi = masterLogic.findUfName("1", kaigaiUf1CdRyohi);
		kaigaiUf2NameRyohi = masterLogic.findUfName("2", kaigaiUf2CdRyohi);
		kaigaiUf3NameRyohi = masterLogic.findUfName("3", kaigaiUf3CdRyohi);
		kaigaiUf4NameRyohi = masterLogic.findUfName("4", kaigaiUf4CdRyohi);
		kaigaiUf5NameRyohi = masterLogic.findUfName("5", kaigaiUf5CdRyohi);
		kaigaiUf6NameRyohi = masterLogic.findUfName("6", kaigaiUf6CdRyohi);
		kaigaiUf7NameRyohi = masterLogic.findUfName("7", kaigaiUf7CdRyohi);
		kaigaiUf8NameRyohi = masterLogic.findUfName("8", kaigaiUf8CdRyohi);
		kaigaiUf9NameRyohi = masterLogic.findUfName("9", kaigaiUf9CdRyohi);
		kaigaiUf10NameRyohi = masterLogic.findUfName("10", kaigaiUf10CdRyohi);

		//貸方（海外）
		kaigaiKashiKamokuNameRyohi = this.kamokuMasterDao.findKamokuNameStr(kaigaiKashiKamokuCdRyohi);
		kaigaiKashiKamokuEdabanNameRyohi= this.kamokuEdabanZandakaDao.findEdabanName(kaigaiKashiKamokuCdRyohi, kaigaiKashiKamokuEdabanCdRyohi);
		kaigaiKashiFutanBumonNameRyohi = this.bumonMasterDao.findFutanBumonName(kaigaiKashiFutanBumonCdRyohi);
	}

	/**
	 * 出張旅費精算申請EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		KaigaiRyohiSeisanXlsLogic kaigairyohiseisanxlsLogic = EteamContainer.getComponent(KaigaiRyohiSeisanXlsLogic.class, connection);
		kaigairyohiseisanxlsLogic.makeExcel(denpyouId, out);
	}
	
	@Override
	protected void setSashihikiNumKaigai(String value)
	{
		this.sashihikiNumKaigai = value;
	}
	
	@Override
	protected void setZeiKbnAndKazeiFlg(GMap map, String yakushokuCd)
	{
		String kaigaiFlgRyohiTmp = (String)map.get("kaigai_flg");
		String shubetsuCdTmp = (String)map.get("shubetsu_cd");
		String koutsuuShudanTmp = (String)map.get("koutsuu_shudan");
		String shubetsu1Tmp = (String)map.get("shubetsu1");
		String shubetsu2Tmp = (String)map.get("shubetsu2");

		// マスターから税区分を取得
		String zeiKbn = "";
		if(RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCdTmp)){
			// 海外交通費の場合
			if("1".equals(kaigaiFlgRyohiTmp)){
				zeiKbn = masterLogic.findKaigaiZeiKubun(koutsuuShudanTmp);
			// 国内交通費の場合
			}else{
				zeiKbn = masterLogic.findZeiKubun(koutsuuShudanTmp);
			}
		}else{
			// 海外宿泊費等の場合
			if("1".equals(kaigaiFlgRyohiTmp)){
				zeiKbn = masterLogic.findKaigaiNittouZeiKubun(shubetsu1Tmp, shubetsu2Tmp, yakushokuCd);
			// 国内宿泊費等の場合
			}else{
				zeiKbn = masterLogic.findNittouZeiKubun(shubetsu1Tmp, shubetsu2Tmp, yakushokuCd);
			}
		}
		map.put("zei_kubun", zeiKbn);

		// 課税フラグの設定
		String kazeiFlgMeisai = "1".equals(kaigaiFlgRyohiTmp)? kazeiFlgRyohiKaigai: kazeiFlgRyohi;
		if("2".equals(zeiKbn) || "3".equals(zeiKbn)){
			kazeiFlgMeisai = KAMOKU_KAZEI_KBN_GROUP.KAZEI;
		} else if("1".equals(zeiKbn) || "4".equals(zeiKbn)){
			kazeiFlgMeisai = KAMOKU_KAZEI_KBN_GROUP.HIKAZEI;
		} else if("0".equals(zeiKbn)){
			kazeiFlgMeisai = "1".equals(kaigaiFlgRyohiTmp)? kazeiFlgRyohiKaigaiKamoku: kazeiFlgRyohiKamoku;
		}
		map.put("kazei_flg", kazeiFlgMeisai);
	}
	
	@Override
	protected void setRyohiMeisaiKaigaiKoumoku(int i, GMap map)
	{
		map.put("kaigai_flg", kaigaiFlgRyohi[i]);
		map.put("heishu_cd", heishuCdRyohi[i]);
		map.put("rate", toDecimal(rateRyohi[i]));
		map.put("gaika", toDecimal(gaikaRyohi[i]));
		map.put("currency_unit", taniRyohi[i]);
		map.put("zei_kubun", zeiKubun[i]);
		map.put("kazei_flg", kazeiFlgRyohiMeisai[i]);
	}
	
	@Override
	protected void setKeihiMeisaiKaigaiKoumoku(int i, GMap map)
	{
		map.put("kaigai_flg", kaigaiFlg[i]);
		map.put("heishu_cd", heishuCd[i]);
		map.put("rate", toDecimal(rate[i]));
		map.put("gaika", toDecimal(gaika[i]));
		map.put("currency_unit", tani[i]);
		map.put("kazei_flg", kazeiFlg[i]);
	}
	
	@Override
	protected void extraMeisaiListKaigaiKoumokuDelete()
	{
		List<String> kaigaiFlgMeisaiList = new ArrayList<>();
		List<String> heishuCdMeisaiList = new ArrayList<>();
		List<String> rateMeisaiList = new ArrayList<>();
		List<String> gaikaMeisaiList = new ArrayList<>();
		List<String> taniMeisaiList = new ArrayList<>();
		List<String> zeiKubunMeisaiList = new ArrayList<>();
		List<String> kazeiFlgMeisaiList = new ArrayList<>();
		
		if (shubetsuCd != null) {
			for (int i = 0; i < shubetsuCd.length; i++) {
				if(!shubetsuCd[i].equals(""))
				{
					kaigaiFlgMeisaiList.add(kaigaiFlgRyohi[i]);
					heishuCdMeisaiList.add(heishuCdRyohi[i]);
					rateMeisaiList.add(rateRyohi[i]);
					gaikaMeisaiList.add(gaikaRyohi[i]);
					taniMeisaiList.add(taniRyohi[i]);
					zeiKubunMeisaiList.add(zeiKubun[i]);
					kazeiFlgMeisaiList.add(kazeiFlgRyohiMeisai[i]);
				}
			}
		}
		
		if(kaigaiFlgMeisaiList.size()==0) {
			kaigaiFlgMeisaiList.add("");
			heishuCdMeisaiList.add("");
			rateMeisaiList.add("");
			gaikaMeisaiList.add("");
			taniMeisaiList.add("");
			zeiKubunMeisaiList.add("");
			kazeiFlgMeisaiList.add("");
		}
		
		kaigaiFlgRyohi = kaigaiFlgMeisaiList.toArray(new String[kaigaiFlgMeisaiList.size()]);
		heishuCdRyohi = heishuCdMeisaiList.toArray(new String[heishuCdMeisaiList.size()]);
		rateRyohi = rateMeisaiList.toArray(new String[rateMeisaiList.size()]);
		gaikaRyohi = gaikaMeisaiList.toArray(new String[gaikaMeisaiList.size()]);
		taniRyohi = taniMeisaiList.toArray(new String[taniMeisaiList.size()]);
		zeiKubun = zeiKubunMeisaiList.toArray(new String[zeiKubunMeisaiList.size()]);
		kazeiFlgRyohiMeisai = kazeiFlgMeisaiList.toArray(new String[kazeiFlgMeisaiList.size()]);
	}
	
	@Override
	protected boolean isKaigaiRyohiMeisai(int i)
	{
		return kaigaiFlgRyohi[i].equals("1");
	}

	@Override
	protected boolean isKaigaiKeihiMeisai(int i)
	{
		return kaigaiFlg[i].equals("1");
	}
	
	@Override
	protected void hissuCheckKaigaiUfCd(int eventNum, boolean keihiEntryFlg)
	{
		if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf1CdRyohi, "海外" + hfUfSeigyo.getUf1Name() + "コード" ,keihiEntryFlg ? ks.uf1.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf2CdRyohi, "海外" + hfUfSeigyo.getUf2Name() + "コード" ,keihiEntryFlg ? ks.uf2.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf3CdRyohi, "海外" + hfUfSeigyo.getUf3Name() + "コード" ,keihiEntryFlg ? ks.uf3.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf4CdRyohi, "海外" + hfUfSeigyo.getUf4Name() + "コード" ,keihiEntryFlg ? ks.uf4.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf5CdRyohi, "海外" + hfUfSeigyo.getUf5Name() + "コード" ,keihiEntryFlg ? ks.uf5.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf6CdRyohi, "海外" + hfUfSeigyo.getUf6Name() + "コード" ,keihiEntryFlg ? ks.uf6.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf7CdRyohi, "海外" + hfUfSeigyo.getUf7Name() + "コード" ,keihiEntryFlg ? ks.uf7.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf8CdRyohi, "海外" + hfUfSeigyo.getUf8Name() + "コード" ,keihiEntryFlg ? ks.uf8.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf9CdRyohi, "海外" + hfUfSeigyo.getUf9Name() + "コード" ,keihiEntryFlg ? ks.uf9.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{kaigaiUf10CdRyohi, "海外" + hfUfSeigyo.getUf10Name() + "コード" ,keihiEntryFlg ? ks.uf10.getHissuFlgS() : "0", "0"},}, eventNum);
	}
	
	/**
	 * @return 海外旅費精算Dto
	 */
	protected KaigaiRyohiseisan createDto() {
		KaigaiRyohiseisan kaigaiRyohiseisan = new KaigaiRyohiseisan();
		GMap usrInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		
		kaigaiRyohiseisan.denpyouId = this.denpyouId;
		kaigaiRyohiseisan.karibaraiDenpyouId = this.karibaraiDenpyouId;
		kaigaiRyohiseisan.karibaraiOn = this.karibaraiOn;
		kaigaiRyohiseisan.karibaraiMishiyouFlg = super.isEmpty(karibaraiMishiyouFlg) ? "0" : karibaraiMishiyouFlg;
		kaigaiRyohiseisan.shucchouChuushiFlg = super.isEmpty(shucchouChuushiFlg) ? "0" : shucchouChuushiFlg;
		kaigaiRyohiseisan.dairiflg = this.dairiFlg;
		kaigaiRyohiseisan.userId = this.userIdRyohi;
		kaigaiRyohiseisan.shainNo = (String)usrInfo.get("shain_no");
		kaigaiRyohiseisan.userSei = (String)usrInfo.get("user_sei");
		kaigaiRyohiseisan.userMei = (String)usrInfo.get("user_mei");
		kaigaiRyohiseisan.houmonsaki = this.houmonsaki;
		kaigaiRyohiseisan.mokuteki = this.mokuteki;
		kaigaiRyohiseisan.seisankikanFrom = super.toDate(this.seisankikanFrom);
		kaigaiRyohiseisan.seisankikanFromHour = this.seisankikanFromHour;
		kaigaiRyohiseisan.seisankikanFromMin = this.seisankikanFromMin;
		kaigaiRyohiseisan.seisankikanTo = super.toDate(this.seisankikanTo);
		kaigaiRyohiseisan.seisankikanToHour = this.seisankikanToHour;
		kaigaiRyohiseisan.seisankikanToMin = this.seisankikanToMin;
		kaigaiRyohiseisan.keijoubi = super.toDate(this.keijoubi);
		kaigaiRyohiseisan.shiharaibi = super.toDate(this.shiharaibi);
		kaigaiRyohiseisan.shiharaikiboubi = super.toDate(this.shiharaiKiboubi);
		kaigaiRyohiseisan.shiharaihouhou = this.shiharaihouhou;
		kaigaiRyohiseisan.tekiyou = this.tekiyouRyohi;
		kaigaiRyohiseisan.kaigaiTekiyou = this.kaigaiTekiyouRyohi;
		kaigaiRyohiseisan.zeiritsu = super.toDecimalZeroIfEmpty(this.zeiritsuRyohi);
		kaigaiRyohiseisan.keigenZeiritsuKbn = EteamConst.keigenZeiritsuKbn.NORMAL;
		kaigaiRyohiseisan.goukeiKingaku = super.toDecimal(this.kingaku);
		kaigaiRyohiseisan.houjinCardRiyouKingaku = super.toDecimal(this.houjinCardRiyouGoukei);
		kaigaiRyohiseisan.kaishaTehaiKingaku = super.toDecimal(this.kaishaTehaiGoukei);
		kaigaiRyohiseisan.sashihikiShikyuuKingaku = super.toDecimal(this.sashihikiShikyuuKingaku);
		kaigaiRyohiseisan.sashihikiNum = super.toDecimal(this.sashihikiNum);
		kaigaiRyohiseisan.sashihikiTanka = super.toDecimal(this.sashihikiTanka);
		kaigaiRyohiseisan.sashihikiNumKaigai = super.toDecimal(this.sashihikiNumKaigai);
		kaigaiRyohiseisan.sashihikiTankaKaigai = super.toDecimal(this.sashihikiTankaKaigai);
		kaigaiRyohiseisan.sashihikiHeishuCdKaigai = this.sashihikiHeishuCdKaigai;
		kaigaiRyohiseisan.sashihikiRateKaigai = super.toDecimal(this.sashihikiRateKaigai);
		kaigaiRyohiseisan.sashihikiTankaKaigaiGaika = super.toDecimal(this.sashihikiTankaKaigaiGaika);
		kaigaiRyohiseisan.hf1Cd = this.hf1Cd;
		kaigaiRyohiseisan.hf1NameRyakushiki = this.hf1Name;
		kaigaiRyohiseisan.hf2Cd = this.hf2Cd;
		kaigaiRyohiseisan.hf2NameRyakushiki = this.hf2Name;
		kaigaiRyohiseisan.hf3Cd = this.hf3Cd;
		kaigaiRyohiseisan.hf3NameRyakushiki = this.hf3Name;
		kaigaiRyohiseisan.hf4Cd = this.hf4Cd;
		kaigaiRyohiseisan.hf4NameRyakushiki = this.hf4Name;
		kaigaiRyohiseisan.hf5Cd = this.hf5Cd;
		kaigaiRyohiseisan.hf5NameRyakushiki = this.hf5Name;
		kaigaiRyohiseisan.hf6Cd = this.hf6Cd;
		kaigaiRyohiseisan.hf6NameRyakushiki = this.hf6Name;
		kaigaiRyohiseisan.hf7Cd = this.hf7Cd;
		kaigaiRyohiseisan.hf7NameRyakushiki = this.hf7Name;
		kaigaiRyohiseisan.hf8Cd = this.hf8Cd;
		kaigaiRyohiseisan.hf8NameRyakushiki = this.hf8Name;
		kaigaiRyohiseisan.hf9Cd = this.hf9Cd;
		kaigaiRyohiseisan.hf9NameRyakushiki = this.hf9Name;
		kaigaiRyohiseisan.hf10Cd = this.hf10Cd;
		kaigaiRyohiseisan.hf10NameRyakushiki = this.hf10Name;
		kaigaiRyohiseisan.hosoku = this.hosoku;
		kaigaiRyohiseisan.shiwakeEdano = (isEmpty(shiwakeEdaNoRyohi) ? null : Integer.parseInt(shiwakeEdaNoRyohi));
		kaigaiRyohiseisan.torihikiName = this.torihikiNameRyohi;
		kaigaiRyohiseisan.kariFutanBumonCd = this.futanBumonCdRyohi;
		kaigaiRyohiseisan.kariFutanBumonName = this.futanBumonNameRyohi;
		kaigaiRyohiseisan.torihikisakiCd = this.torihikisakiCdRyohi;
		kaigaiRyohiseisan.torihikisakiNameRyakushiki = this.torihikisakiNameRyohi;
		kaigaiRyohiseisan.kariKamokuCd = this.kamokuCdRyohi;
		kaigaiRyohiseisan.kariKamokuName = this.kamokuNameRyohi;
		kaigaiRyohiseisan.kariKamokuEdabanCd = this.kamokuEdabanCdRyohi;
		kaigaiRyohiseisan.kariKamokuEdabanName = this.kamokuEdabanNameRyohi;
		kaigaiRyohiseisan.kariKazeiKbn = this.kazeiKbnRyohi;
		kaigaiRyohiseisan.ryohiKazeiFlg = this.kazeiFlgRyohi;
		kaigaiRyohiseisan.kashiFutanBumonCd = this.kashiFutanBumonCdRyohi;
		kaigaiRyohiseisan.kashiFutanBumonName = this.kashiFutanBumonNameRyohi;
		kaigaiRyohiseisan.kashiKamokuCd = this.kashiKamokuCdRyohi;
		kaigaiRyohiseisan.kashiKamokuName = this.kashiKamokuNameRyohi;
		kaigaiRyohiseisan.kashiKamokuEdabanCd = this.kashiKamokuEdabanCdRyohi;
		kaigaiRyohiseisan.kashiKamokuEdabanName = this.kashiKamokuEdabanNameRyohi;
		kaigaiRyohiseisan.kashiKazeiKbn = this.kashiKazeiKbnRyohi;
		kaigaiRyohiseisan.uf1Cd = this.uf1CdRyohi;
		kaigaiRyohiseisan.uf1NameRyakushiki = this.uf1NameRyohi;
		kaigaiRyohiseisan.uf2Cd = this.uf2CdRyohi;
		kaigaiRyohiseisan.uf2NameRyakushiki = this.uf2NameRyohi;
		kaigaiRyohiseisan.uf3Cd = this.uf3CdRyohi;
		kaigaiRyohiseisan.uf3NameRyakushiki = this.uf3NameRyohi;
		kaigaiRyohiseisan.uf4Cd = this.uf4CdRyohi;
		kaigaiRyohiseisan.uf4NameRyakushiki = this.uf4NameRyohi;
		kaigaiRyohiseisan.uf5Cd = this.uf5CdRyohi;
		kaigaiRyohiseisan.uf5NameRyakushiki = this.uf5NameRyohi;
		kaigaiRyohiseisan.uf6Cd = this.uf6CdRyohi;
		kaigaiRyohiseisan.uf6NameRyakushiki = this.uf6NameRyohi;
		kaigaiRyohiseisan.uf7Cd = this.uf7CdRyohi;
		kaigaiRyohiseisan.uf7NameRyakushiki = this.uf7NameRyohi;
		kaigaiRyohiseisan.uf8Cd = this.uf8CdRyohi;
		kaigaiRyohiseisan.uf8NameRyakushiki = this.uf8NameRyohi;
		kaigaiRyohiseisan.uf9Cd = this.uf9CdRyohi;
		kaigaiRyohiseisan.uf9NameRyakushiki = this.uf9NameRyohi;
		kaigaiRyohiseisan.uf10Cd = this.uf10CdRyohi;
		kaigaiRyohiseisan.uf10NameRyakushiki = this.uf10NameRyohi;
		kaigaiRyohiseisan.projectCd = this.projectCdRyohi;
		kaigaiRyohiseisan.projectName = this.projectNameRyohi;
		kaigaiRyohiseisan.segmentCd = this.segmentCdRyohi;
		kaigaiRyohiseisan.segmentNameRyakushiki = this.segmentNameRyohi;
		kaigaiRyohiseisan.tekiyouCd = this.tekiyouCdRyohi;
		kaigaiRyohiseisan.kaigaiShiwakeEdano = (isEmpty(kaigaiShiwakeEdaNoRyohi) ? null : Integer.parseInt(kaigaiShiwakeEdaNoRyohi));
		kaigaiRyohiseisan.kaigaiTorihikiName = this.kaigaiTorihikiNameRyohi;
		kaigaiRyohiseisan.kaigaiKariFutanBumonCd = this.kaigaiFutanBumonCdRyohi;
		kaigaiRyohiseisan.kaigaiKariFutanBumonName = this.kaigaiFutanBumonNameRyohi;
		kaigaiRyohiseisan.kaigaiTorihikisakiCd = this.kaigaiTorihikisakiCdRyohi;
		kaigaiRyohiseisan.kaigaiTorihikisakiNameRyakushiki = this.kaigaiTorihikisakiNameRyohi;
		kaigaiRyohiseisan.kaigaiKariKamokuCd = this.kaigaiKamokuCdRyohi;
		kaigaiRyohiseisan.kaigaiKariKamokuName = this.kaigaiKamokuNameRyohi;
		kaigaiRyohiseisan.kaigaiKariKamokuEdabanCd = this.kaigaiKamokuEdabanCdRyohi;
		kaigaiRyohiseisan.kaigaiKariKamokuEdabanName = this.kaigaiKamokuEdabanNameRyohi;
		kaigaiRyohiseisan.kaigaiKariKazeiKbn = this.kaigaiKazeiKbnRyohi;
		kaigaiRyohiseisan.kaigaiKazeiFlg = this.kazeiFlgRyohiKaigai;
		kaigaiRyohiseisan.kaigaiKashiFutanBumonCd = this.kaigaiKashiFutanBumonCdRyohi;
		kaigaiRyohiseisan.kaigaiKashiFutanBumonName = this.kaigaiKashiFutanBumonNameRyohi;
		kaigaiRyohiseisan.kaigaiKashiKamokuCd = this.kaigaiKashiKamokuCdRyohi;
		kaigaiRyohiseisan.kaigaiKashiKamokuName = this.kaigaiKashiKamokuNameRyohi;
		kaigaiRyohiseisan.kaigaiKashiKamokuEdabanCd = this.kaigaiKashiKamokuEdabanCdRyohi;
		kaigaiRyohiseisan.kaigaiKashiKamokuEdabanName = this.kaigaiKashiKamokuEdabanNameRyohi;
		kaigaiRyohiseisan.kaigaiKashiKazeiKbn = this.kaigaiKashiKazeiKbnRyohi;
		kaigaiRyohiseisan.kaigaiUf1Cd = this.kaigaiUf1CdRyohi;
		kaigaiRyohiseisan.kaigaiUf1NameRyakushiki = this.kaigaiUf1NameRyohi;
		kaigaiRyohiseisan.kaigaiUf2Cd = this.kaigaiUf2CdRyohi;
		kaigaiRyohiseisan.kaigaiUf2NameRyakushiki = this.kaigaiUf2NameRyohi;
		kaigaiRyohiseisan.kaigaiUf3Cd = this.kaigaiUf3CdRyohi;
		kaigaiRyohiseisan.kaigaiUf3NameRyakushiki = this.kaigaiUf3NameRyohi;
		kaigaiRyohiseisan.kaigaiUf4Cd = this.kaigaiUf4CdRyohi;
		kaigaiRyohiseisan.kaigaiUf4NameRyakushiki = this.kaigaiUf4NameRyohi;
		kaigaiRyohiseisan.kaigaiUf5Cd = this.kaigaiUf5CdRyohi;
		kaigaiRyohiseisan.kaigaiUf5NameRyakushiki = this.kaigaiUf5NameRyohi;
		kaigaiRyohiseisan.kaigaiUf6Cd = this.kaigaiUf6CdRyohi;
		kaigaiRyohiseisan.kaigaiUf6NameRyakushiki = this.kaigaiUf6NameRyohi;
		kaigaiRyohiseisan.kaigaiUf7Cd = this.kaigaiUf7CdRyohi;
		kaigaiRyohiseisan.kaigaiUf7NameRyakushiki = this.kaigaiUf7NameRyohi;
		kaigaiRyohiseisan.kaigaiUf8Cd = this.kaigaiUf8CdRyohi;
		kaigaiRyohiseisan.kaigaiUf8NameRyakushiki = this.kaigaiUf8NameRyohi;
		kaigaiRyohiseisan.kaigaiUf9Cd = this.kaigaiUf9CdRyohi;
		kaigaiRyohiseisan.kaigaiUf9NameRyakushiki = this.kaigaiUf9NameRyohi;
		kaigaiRyohiseisan.kaigaiUf10Cd = this.kaigaiUf10CdRyohi;
		kaigaiRyohiseisan.kaigaiUf10NameRyakushiki = this.kaigaiUf10NameRyohi;
		kaigaiRyohiseisan.kaigaiProjectCd = this.kaigaiProjectCdRyohi;
		kaigaiRyohiseisan.kaigaiProjectName = this.kaigaiProjectNameRyohi;
		kaigaiRyohiseisan.kaigaiSegmentCd = this.kaigaiSegmentCdRyohi;
		kaigaiRyohiseisan.kaigaiSegmentNameRyakushiki = this.kaigaiSegmentNameRyohi;
		kaigaiRyohiseisan.kaigaiTekiyouCd = this.kaigaiTekiyouCdRyohi;
		kaigaiRyohiseisan.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		kaigaiRyohiseisan.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		kaigaiRyohiseisan.bunriKbn = this.bunriKbn;
		kaigaiRyohiseisan.kariShiireKbn = this.kariShiireKbn;
		kaigaiRyohiseisan.kashiShiireKbn = isEmpty(this.kashiShiireKbn) ? this.kariShiireKbn : this.kashiShiireKbn;
		kaigaiRyohiseisan.kaigaiBunriKbn = this.kaigaiBunriKbn;
		kaigaiRyohiseisan.kaigaiKariShiireKbn = this.kaigaiKariShiireKbn;
		kaigaiRyohiseisan.kaigaiKashiShiireKbn = this.kaigaiKashiShiireKbn;
		kaigaiRyohiseisan.invoiceDenpyou = this.invoiceDenpyou;
		return kaigaiRyohiseisan;
	}
	
	/**
	 * @param i 番号
	 * @return 海外旅費精算明細Dto
	 */
	protected KaigaiRyohiseisanMeisai createkaigaiRyohiseisanMeisai(int i) {
		KaigaiRyohiseisanMeisai kaigaiRyohiseisanMeisai = new KaigaiRyohiseisanMeisai();
		kaigaiRyohiseisanMeisai.denpyouId = this.denpyouId;
		kaigaiRyohiseisanMeisai.kaigaiFlg = this.kaigaiFlgRyohi[i];
		kaigaiRyohiseisanMeisai.denpyouEdano = i + 1;
		kaigaiRyohiseisanMeisai.kikanFrom = super.toDate(kikanFrom[i]);
		kaigaiRyohiseisanMeisai.kikanTo = super.toDate(kikanTo[i]);
		kaigaiRyohiseisanMeisai.kyuujitsuNissuu = super.toDecimal(kyuujitsuNissuu[i]);
		kaigaiRyohiseisanMeisai.shubetsuCd = this.shubetsuCd[i];
		kaigaiRyohiseisanMeisai.shubetsu1 = this.shubetsu1[i];
		kaigaiRyohiseisanMeisai.shubetsu2 = this.shubetsu2[i];
		kaigaiRyohiseisanMeisai.hayaFlg = this.hayaFlg[i];
		kaigaiRyohiseisanMeisai.yasuFlg = this.yasuFlg[i];
		kaigaiRyohiseisanMeisai.rakuFlg = this.rakuFlg[i];
		kaigaiRyohiseisanMeisai.koutsuuShudan = this.koutsuuShudan[i];
		kaigaiRyohiseisanMeisai.shouhyouShoruiHissuFlg = this.shouhyouShoruiHissuFlg[i];
		kaigaiRyohiseisanMeisai.ryoushuushoSeikyuushoTouFlg = this.ryoushuushoSeikyuushoTouFlg[i];
		kaigaiRyohiseisanMeisai.naiyou = this.naiyou[i];
		kaigaiRyohiseisanMeisai.bikou = this.bikou[i];
		kaigaiRyohiseisanMeisai.oufukuFlg = this.oufukuFlg[i];
		kaigaiRyohiseisanMeisai.houjinCardRiyouFlg = this.houjinCardFlgRyohi[i];
		kaigaiRyohiseisanMeisai.kaishaTehaiFlg = this.kaishaTehaiFlgRyohi[i];
		kaigaiRyohiseisanMeisai.jidounyuuryokuFlg = this.jidounyuuryokuFlg[i];
		kaigaiRyohiseisanMeisai.nissuu = super.toDecimal(nissuu[i]);
		kaigaiRyohiseisanMeisai.heishuCd = this.heishuCdRyohi[i];
		kaigaiRyohiseisanMeisai.rate = super.toDecimal(rateRyohi[i]);
		kaigaiRyohiseisanMeisai.gaika = super.toDecimal(gaikaRyohi[i]);
		kaigaiRyohiseisanMeisai.currencyUnit = this.taniRyohi[i];
		kaigaiRyohiseisanMeisai.tanka = super.toDecimal(tanka[i]);
		kaigaiRyohiseisanMeisai.suuryouNyuryokuType = this.suuryouNyuryokuType[i];
		kaigaiRyohiseisanMeisai.suuryou = super.toDecimal(suuryou[i]);
		kaigaiRyohiseisanMeisai.suuryouKigou = this.suuryouKigou[i];
		kaigaiRyohiseisanMeisai.meisaiKingaku = super.toDecimal(meisaiKingaku[i]);
		kaigaiRyohiseisanMeisai.zeiKubun = this.zeiKubun[i];
		kaigaiRyohiseisanMeisai.kazeiFlg = this.kazeiFlgRyohiMeisai[i];
		kaigaiRyohiseisanMeisai.icCardNo = this.icCardNo[i];
		kaigaiRyohiseisanMeisai.icCardSequenceNo = this.icCardSequenceNo[i];
		kaigaiRyohiseisanMeisai.himodukeCardMeisai = this.isEmpty(himodukeCardMeisaiRyohi[i]) ? null : Long.parseLong(himodukeCardMeisaiRyohi[i]);
		kaigaiRyohiseisanMeisai.shiharaisakiName = this.shiharaisakiNameRyohi[i];
		kaigaiRyohiseisanMeisai.jigyoushaKbn = super.getDefaultJigyoushaKbnIfEmpty(this.jigyoushaKbnRyohi[i]);
		kaigaiRyohiseisanMeisai.zeinukiKingaku = super.toDecimal(this.isKaigaiRyohiMeisai(i) ? this.meisaiKingaku[i] : this.zeinukiKingakuRyohi[i]);
		kaigaiRyohiseisanMeisai.shouhizeigaku = this.isKaigaiRyohiMeisai(i) ? BigDecimal.ZERO : super.toDecimal(this.shouhizeigakuRyohi[i]);
		kaigaiRyohiseisanMeisai.zeigakuFixFlg = super.getDefaultZeigakuFixFlgIfEmpty(this.zeigakuFixFlg[i]);
		return kaigaiRyohiseisanMeisai;
	}
	
	/**
	 * @param i 明細番号
	 * @return 海外旅費精算経費明細Dto
	 */
	protected KaigaiRyohiseisanKeihiMeisai createKaigaiRyohiseisanKeihiMeisai(int i) {
		KaigaiRyohiseisanKeihiMeisai kaigaiRyohiseisanKeihiMeisai = new KaigaiRyohiseisanKeihiMeisai();
		kaigaiRyohiseisanKeihiMeisai.denpyouId = this.denpyouId;
		kaigaiRyohiseisanKeihiMeisai.denpyouEdano = i + 1;
		kaigaiRyohiseisanKeihiMeisai.kaigaiFlg = this.kaigaiFlg[i];
		kaigaiRyohiseisanKeihiMeisai.shiwakeEdano = Integer.parseInt(shiwakeEdaNo[i]);
		kaigaiRyohiseisanKeihiMeisai.shiyoubi = super.toDate(shiyoubi[i]);
		kaigaiRyohiseisanKeihiMeisai.shouhyouShoruiFlg = this.shouhyouShorui[i];
		kaigaiRyohiseisanKeihiMeisai.torihikiName = this.torihikiName[i];
		kaigaiRyohiseisanKeihiMeisai.tekiyou = this.tekiyou[i];
		kaigaiRyohiseisanKeihiMeisai.zeiritsu = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]) ? super.toDecimal(zeiritsu[i]) : BigDecimal.ZERO;
		kaigaiRyohiseisanKeihiMeisai.keigenZeiritsuKbn = super.isEmpty(keigenZeiritsuKbn[i]) ? "0" :this.keigenZeiritsuKbn[i];
		kaigaiRyohiseisanKeihiMeisai.kazeiFlg = this.kazeiFlg[i];
		kaigaiRyohiseisanKeihiMeisai.shiharaiKingaku = super.toDecimal(shiharaiKingaku[i]);
		kaigaiRyohiseisanKeihiMeisai.hontaiKingaku = super.toDecimal(hontaiKingaku[i]);
		kaigaiRyohiseisanKeihiMeisai.shouhizeigaku = super.toDecimal(shouhizeigaku[i]);
		kaigaiRyohiseisanKeihiMeisai.houjinCardRiyouFlg = this.houjinCardFlgKeihi[i];
		kaigaiRyohiseisanKeihiMeisai.kaishaTehaiFlg = this.kaishaTehaiFlgKeihi[i];
		kaigaiRyohiseisanKeihiMeisai.kousaihiShousaiHyoujiFlg = this.kousaihiHyoujiFlg[i];
		kaigaiRyohiseisanKeihiMeisai.kousaihiNinzuuRiyouFlg = this.ninzuuRiyouFlg[i];
		kaigaiRyohiseisanKeihiMeisai.kousaihiShousai = this.kousaihiShousai[i];
		kaigaiRyohiseisanKeihiMeisai.kousaihiNinzuu = this.isEmpty(kousaihiNinzuu[i]) ? null : Integer.parseInt(kousaihiNinzuu[i]);
		kaigaiRyohiseisanKeihiMeisai.kousaihiHitoriKingaku = super.toDecimal(kousaihiHitoriKingaku[i]);
		kaigaiRyohiseisanKeihiMeisai.heishuCd = this.heishuCd[i];
		kaigaiRyohiseisanKeihiMeisai.rate = super.toDecimal(rate[i]);
		kaigaiRyohiseisanKeihiMeisai.gaika = super.toDecimal(gaika[i]);
		kaigaiRyohiseisanKeihiMeisai.currencyUnit = this.tani[i];
		kaigaiRyohiseisanKeihiMeisai.kariFutanBumonCd = this.futanBumonCd[i];
		kaigaiRyohiseisanKeihiMeisai.kariFutanBumonName = this.futanBumonName[i];
		kaigaiRyohiseisanKeihiMeisai.torihikisakiCd = this.torihikisakiCd[i];
		kaigaiRyohiseisanKeihiMeisai.torihikisakiNameRyakushiki = this.torihikisakiName[i];
		kaigaiRyohiseisanKeihiMeisai.kariKamokuCd = this.kamokuCd[i];
		kaigaiRyohiseisanKeihiMeisai.kariKamokuName = this.kamokuName[i];
		kaigaiRyohiseisanKeihiMeisai.kariKamokuEdabanCd = this.kamokuEdabanCd[i];
		kaigaiRyohiseisanKeihiMeisai.kariKamokuEdabanName = this.kamokuEdabanName[i];
		kaigaiRyohiseisanKeihiMeisai.kariKazeiKbn = this.kazeiKbn[i];
		kaigaiRyohiseisanKeihiMeisai.kashiFutanBumonCd = this.kashiFutanBumonCd[i];
		kaigaiRyohiseisanKeihiMeisai.kashiFutanBumonName = this.kashiFutanBumonName[i];
		kaigaiRyohiseisanKeihiMeisai.kashiKamokuCd = this.kashiKamokuCd[i];
		kaigaiRyohiseisanKeihiMeisai.kashiKamokuName = this.kashiKamokuName[i];
		kaigaiRyohiseisanKeihiMeisai.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd[i];
		kaigaiRyohiseisanKeihiMeisai.kashiKamokuEdabanName = this.kashiKamokuEdabanName[i];
		kaigaiRyohiseisanKeihiMeisai.kashiKazeiKbn = this.kashiKazeiKbn[i];
		kaigaiRyohiseisanKeihiMeisai.uf1Cd = this.uf1Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf1NameRyakushiki = this.uf1Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf2Cd = this.uf2Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf2NameRyakushiki = this.uf2Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf3Cd = this.uf3Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf3NameRyakushiki = this.uf3Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf4Cd = this.uf4Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf4NameRyakushiki = this.uf4Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf5Cd = this.uf5Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf5NameRyakushiki = this.uf5Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf6Cd = this.uf6Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf6NameRyakushiki = this.uf6Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf7Cd = this.uf7Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf7NameRyakushiki = this.uf7Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf8Cd = this.uf8Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf8NameRyakushiki = this.uf8Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf9Cd = this.uf9Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf9NameRyakushiki = this.uf9Name[i];
		kaigaiRyohiseisanKeihiMeisai.uf10Cd = this.uf10Cd[i];
		kaigaiRyohiseisanKeihiMeisai.uf10NameRyakushiki = this.uf10Name[i];
		kaigaiRyohiseisanKeihiMeisai.projectCd = this.projectCd[i];
		kaigaiRyohiseisanKeihiMeisai.projectName = this.projectName[i];
		kaigaiRyohiseisanKeihiMeisai.segmentCd = this.segmentCd[i];
		kaigaiRyohiseisanKeihiMeisai.segmentNameRyakushiki = this.segmentName[i];
		kaigaiRyohiseisanKeihiMeisai.tekiyouCd = this.tekiyouCd[i];
		kaigaiRyohiseisanKeihiMeisai.himodukeCardMeisai = this.isEmpty(himodukeCardMeisaiKeihi[i]) ? null : Long.parseLong(himodukeCardMeisaiKeihi[i]);
		kaigaiRyohiseisanKeihiMeisai.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		kaigaiRyohiseisanKeihiMeisai.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		kaigaiRyohiseisanKeihiMeisai.shiharaisakiName = this.shiharaisakiNameKeihi[i];
		kaigaiRyohiseisanKeihiMeisai.jigyoushaKbn = super.getDefaultJigyoushaKbnIfEmpty(this.jigyoushaKbnKeihi[i]);
		kaigaiRyohiseisanKeihiMeisai.bunriKbn = this.bunriKbnKeihi[i];
		kaigaiRyohiseisanKeihiMeisai.kariShiireKbn = this.kariShiireKbnKeihi[i];
		kaigaiRyohiseisanKeihiMeisai.kashiShiireKbn = this.kashiShiireKbnKeihi[i];

		return kaigaiRyohiseisanKeihiMeisai;
	}
}
