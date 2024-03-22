package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamEkispertCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.RegAccess;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.BumonMasterAbstractDao;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.BumonMasterDao;
import eteam.database.dao.KaigaiRyohiKaribaraiMeisaiDao;
import eteam.database.dao.KamokuEdabanZandakaDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dto.KaigaiRyohiKaribaraiMeisai;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 海外旅費仮払Action
 */
@Getter @Setter @ToString
public class KaigaiRyohiKaribaraiShinseiAction extends EteamEkispertCommon {

//＜定数＞

//＜画面入力＞

	//画面入力（申請内容）
	/** 使用者ID */
	String userIdRyohi;
	/** 使用者名前 */
	String userNameRyohi;
	/** 社員番号 */
	String shainNoRyohi;
	/** 仮払ありなし */
	String karibaraiOn;
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
	/** 出張先・訪問先 */
	String houmonsaki;
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
	/** 精算期間終了時刻（分）*/
	String seisankikanToMin;
	/** 仕訳枝番号（旅費） */
	String shiwakeEdaNoRyohi;
	/** 取引名（旅費） */
	String torihikiNameRyohi;
	/** 勘定科目コード（旅費） */
	String kamokuCdRyohi;
	/** 勘定科目名（旅費） */
	String kamokuNameRyohi;
	/** 勘定科目枝番コード（旅費） */
	String kamokuEdabanCdRyohi;
	/** 勘定科目枝番名（旅費） */
	String kamokuEdabanNameRyohi;
	/** 負担部門コード（旅費） */
	String futanBumonCdRyohi;
	/** 負担部門名（旅費） */
	String futanBumonNameRyohi;
	/** 取引先コード（旅費） */
	String torihikisakiCdRyohi;
	/** 取引先名（旅費） */
	String torihikisakiNameRyohi;
	/** プロジェクトコード（旅費） */
	String projectCdRyohi;
	/** プロジェクト名（旅費） */
	String projectNameRyohi;
	/** セグメントコード（旅費） */
	String segmentCdRyohi;
	/** セグメント名（旅費） */
	String segmentNameRyohi;
	/** ユニバーサルフィールド１コード（旅費） */
	String uf1CdRyohi;
	/** ユニバーサルフィールド１名（旅費） */
	String uf1NameRyohi;
	/** ユニバーサルフィールド２コード（旅費） */
	String uf2CdRyohi;
	/** ユニバーサルフィールド２名（旅費） */
	String uf2NameRyohi;
	/** ユニバーサルフィールド３コード（旅費） */
	String uf3CdRyohi;
	/** ユニバーサルフィールド３名（旅費） */
	String uf3NameRyohi;
	/** ユニバーサルフィールド４コード（旅費） */
	String uf4CdRyohi;
	/** ユニバーサルフィールド４名（旅費） */
	String uf4NameRyohi;
	/** ユニバーサルフィールド５コード（旅費） */
	String uf5CdRyohi;
	/** ユニバーサルフィールド５名（旅費） */
	String uf5NameRyohi;
	/** ユニバーサルフィールド６コード（旅費） */
	String uf6CdRyohi;
	/** ユニバーサルフィールド６名（旅費） */
	String uf6NameRyohi;
	/** ユニバーサルフィールド７コード（旅費） */
	String uf7CdRyohi;
	/** ユニバーサルフィールド７名（旅費） */
	String uf7NameRyohi;
	/** ユニバーサルフィールド８コード（旅費） */
	String uf8CdRyohi;
	/** ユニバーサルフィールド８名（旅費） */
	String uf8NameRyohi;
	/** ユニバーサルフィールド９コード（旅費） */
	String uf9CdRyohi;
	/** ユニバーサルフィールド９名（旅費） */
	String uf9NameRyohi;
	/** ユニバーサルフィールド１０コード（旅費） */
	String uf10CdRyohi;
	/** ユニバーサルフィールド１０名（旅費） */
	String uf10NameRyohi;
	/** 課税区分（旅費） */
	String kazeiKbnRyohi;
	/** 摘要（旅費） */
	String tekiyouRyohi;
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 共通部分交際費注記 */
	String chuukiKousai1;
	/** 伝票摘要注記（旅費） */
	String chuuki2Ryohi;
	/** 申請期間チェック注記 */
	String shinseiChuuki;
	/** 仮払金額 */
	String karibaraiKingaku;
	/** 支払日 */
	String shiharaibi;
	/** 差引回数 */
	String sashihikiNum;
	/** 差引単価 */
	String sashihikiTanka;
	/** 差引金額 */
	String sashihikiKingaku;
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
	/** 補足 */
	String hosoku;

	//乗換案内エリア
	/** 乗換案内期間 */
	String norikaeKaishiBi;
	/** 乗換案内乗車区間 */
	String jousyaKukan;
	/** 乗換案内金額 */
	String norikaeKingaku;

	//画面入力（明細/旅費）
	/** 海外フラグ */
	String[] kaigaiFlgRyohi;
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
	/** 休日日数 */
	String[] kyuujitsuNissuu;
	/** 証憑書類必須フラグ */
	String[] shouhyouShoruiHissuFlg;
	/** 交通手段 */
	String[] koutsuuShudan;
	/** 内容 */
	String[] naiyou;
	/** 備考 */
	String[] bikou;
	/** 往復フラグ */
	String[] oufukuFlg;
	/** 自動入力フラグ */
	String[] jidounyuuryokuFlg;
	/** 日数 */
	String[] nissuu;
	/** 幣種コード */
	String[] heishuCdRyohi;
	/** レート */
	String[] rateRyohi;
	/** 外貨 */
	String[] gaikaRyohi;
	/** 通貨単位 */
	String[] taniRyohi;
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
	
	//202210
	/** 早フラグ */
	String[] hayaFlg;
	/** 安フラグ */
	String[] yasuFlg;
	/** 楽フラグ */
	String[] rakuFlg;

	//画面入力（明細/経費）
	/** 海外フラグ */
	String[] kaigaiFlg;
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
	/** 課税フラグ */
	String[] kazeiFlg;
	/** 支払金額 */
	String[] shiharaiKingaku;
	/** 本体金額 */
	String[] hontaiKingaku;
	/** 消費税額 */
	String[] shouhizeigaku;
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
	/** 消費税率DropDownList */
	List<GMap> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<GMap> kazeiKbnList;
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
	/** 海外交通手段のDropDownList */
	List<GMap> kaigaiKoutsuuShudanList;
	/** 海外交通手段ドメイン */
	String[] kaigaiKoutsuuShudanDomain;
	/** インボイス対応伝票DropDownList */
	List<NaibuCdSetting> invoiceDenpyouList;
	/** インボイス対応伝票ドメイン */
	String[] invoiceDenpyouDomain;

	/** 借方　UF1コード（旅費） */
	String kariUf1CdRyohi;
	/** 借方　UF2コード（旅費） */
	String kariUf2CdRyohi;
	/** 借方　UF3コード（旅費） */
	String kariUf3CdRyohi;
	/** 借方　UF4コード（旅費） */
	String kariUf4CdRyohi;
	/** 借方　UF5コード（旅費） */
	String kariUf5CdRyohi;
	/** 借方　UF6コード（旅費） */
	String kariUf6CdRyohi;
	/** 借方　UF7コード（旅費） */
	String kariUf7CdRyohi;
	/** 借方　UF8コード（旅費） */
	String kariUf8CdRyohi;
	/** 借方　UF9コード（旅費） */
	String kariUf9CdRyohi;
	/** 借方　UF10コード（旅費） */
	String kariUf10CdRyohi;
	/** 貸方負担部門コード（旅費） */
	String kashiFutanBumonCdRyohi;
	/** 貸方負担部門名（旅費） */
	String kashiFutanBumonNameRyohi;
	/** 貸方科目コード（旅費） */
	String kashiKamokuCdRyohi;
	/** 貸方科目名（旅費） */
	String kashiKamokuNameRyohi;
	/** 貸方科目枝番コード（旅費） */
	String kashiKamokuEdabanCdRyohi;
	/** 貸方科目枝番名（旅費） */
	String kashiKamokuEdabanNameRyohi;
	/** 貸方課税区分（旅費） */
	String kashiKazeiKbnRyohi;
	/** 貸方1　UF1コード（旅費） */
	String kashiUf1Cd1Ryohi;
	/** 貸方1　UF2コード（旅費） */
	String kashiUf2Cd1Ryohi;
	/** 貸方1　UF3コード（旅費） */
	String kashiUf3Cd1Ryohi;
	/** 貸方1　UF4コード（旅費） */
	String kashiUf4Cd1Ryohi;
	/** 貸方1　UF5コード（旅費） */
	String kashiUf5Cd1Ryohi;
	/** 貸方1　UF6コード（旅費） */
	String kashiUf6Cd1Ryohi;
	/** 貸方1　UF7コード（旅費） */
	String kashiUf7Cd1Ryohi;
	/** 貸方1　UF8コード（旅費） */
	String kashiUf8Cd1Ryohi;
	/** 貸方1　UF9コード（旅費） */
	String kashiUf9Cd1Ryohi;
	/** 貸方1　UF10コード（旅費） */
	String kashiUf10Cd1Ryohi;
	/** 貸方2　UF1コード（旅費） */
	String kashiUf1Cd2Ryohi;
	/** 貸方2　UF2コード（旅費） */
	String kashiUf2Cd2Ryohi;
	/** 貸方2　UF3コード（旅費） */
	String kashiUf3Cd2Ryohi;
	/** 貸方2　UF4コード（旅費） */
	String kashiUf4Cd2Ryohi;
	/** 貸方2　UF5コード（旅費） */
	String kashiUf5Cd2Ryohi;
	/** 貸方2　UF6コード（旅費） */
	String kashiUf6Cd2Ryohi;
	/** 貸方2　UF7コード（旅費） */
	String kashiUf7Cd2Ryohi;
	/** 貸方2　UF8コード（旅費） */
	String kashiUf8Cd2Ryohi;
	/** 貸方2　UF9コード（旅費） */
	String kashiUf9Cd2Ryohi;
	/** 貸方2　UF10コード（旅費） */
	String kashiUf10Cd2Ryohi;
	/** 借方取引先コード */
	String[] kariTorihikisakiCd;
	/** 借方　UF1コード（経費） */
	String[] kariUf1Cd;
	/** 借方　UF2コード（経費） */
	String[] kariUf2Cd;
	/** 借方　UF3コード（経費） */
	String[] kariUf3Cd;
	/** 借方　UF4コード（経費） */
	String[] kariUf4Cd;
	/** 借方　UF5コード（経費） */
	String[] kariUf5Cd;
	/** 借方　UF6コード（経費） */
	String[] kariUf6Cd;
	/** 借方　UF7コード（経費） */
	String[] kariUf7Cd;
	/** 借方　UF8コード（経費） */
	String[] kariUf8Cd;
	/** 借方　UF9コード（経費） */
	String[] kariUf9Cd;
	/** 借方　UF10コード（経費） */
	String[] kariUf10Cd;

	/** 貸方取引先コード（経費） */
	String[] kashiTorihikisakiCd;
	/** 貸方負担部門コード（経費） */
	String[] kashiFutanBumonCd;
	/** 貸方負担部門名（経費） */
	String[] kashiFutanBumonName;
	/** 貸方科目コード（経費） */
	String[] kashiKamokuCd;
	/** 貸方科目名（経費） */
	String[] kashiKamokuName;
	/** 貸方科目枝番コード（経費） */
	String[] kashiKamokuEdabanCd;
	/** 貸方科目枝番名（経費） */
	String[] kashiKamokuEdabanName;
	/** 貸方課税区分（経費） */
	String[] kashiKazeiKbn;
	/** 貸方1　UF1コード（経費） */
	String[] kashiUf1Cd1;
	/** 貸方1　UF2コード（経費） */
	String[] kashiUf2Cd1;
	/** 貸方1　UF3コード（経費） */
	String[] kashiUf3Cd1;
	/** 貸方1　UF4コード（経費） */
	String[] kashiUf4Cd1;
	/** 貸方1　UF5コード（経費） */
	String[] kashiUf5Cd1;
	/** 貸方1　UF6コード（経費） */
	String[] kashiUf6Cd1;
	/** 貸方1　UF7コード（経費） */
	String[] kashiUf7Cd1;
	/** 貸方1　UF8コード（経費） */
	String[] kashiUf8Cd1;
	/** 貸方1　UF9コード（経費） */
	String[] kashiUf9Cd1;
	/** 貸方1　UF10コード（経費） */
	String[] kashiUf10Cd1;
	/** 貸方2　UF1コード（経費） */
	String[] kashiUf1Cd2;
	/** 貸方2　UF2コード（経費） */
	String[] kashiUf2Cd2;
	/** 貸方2　UF3コード（経費） */
	String[] kashiUf3Cd2;
	/** 貸方2　UF4コード（経費） */
	String[] kashiUf4Cd2;
	/** 貸方2　UF5コード（経費） */
	String[] kashiUf5Cd2;
	/** 貸方2　UF6コード（経費） */
	String[] kashiUf6Cd2;
	/** 貸方2　UF7コード（経費） */
	String[] kashiUf7Cd2;
	/** 貸方2　UF8コード（経費） */
	String[] kashiUf8Cd2;
	/** 貸方2　UF9コード（経費） */
	String[] kashiUf9Cd2;
	/** 貸方2　UF10コード（経費） */
	String[] kashiUf10Cd2;
	/** 摘要コード（旅費） */
	String tekiyouCdRyohi;
	/** 摘要コード（経費） */
	String[] tekiyouCd;

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
	/**  画面項目制御クラス(経費明細) */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);

	// 会社設定情報
	/** 差引項目名称 */
	String sashihikiName = setting.sashihikiName();
	/** 差引単価 */
	String sashihikiTankaSI = setting.sashihikiTanka();
	/** 差引項目名称（海外） */
	String sashihikiNameKaigai = setting.sashihikiNameKaigai();
	/** 差引単価（海外） */
	String sashihikiTankaKaigaiSI = setting.sashihikiTankaKaigai();

	/** 入力モード */
	boolean enableInput;
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean kamokuEdabanEnableRyohi;
	/** 負担部門選択ボタン押下可否 */
	boolean futanBumonEnableRyohi;
	/** 取引先選択ボタン押下可否 */
	boolean torihikisakiEnableRyohi;
	/** UF1ボタン押下可否 */
	boolean uf1EnableRyohi;
	/** UF2ボタン押下可否 */
	boolean uf2EnableRyohi;
	/** UF3ボタン押下可否 */
	boolean uf3EnableRyohi;
	/** UF4ボタン押下可否 */
	boolean uf4EnableRyohi;
	/** UF5ボタン押下可否 */
	boolean uf5EnableRyohi;
	/** UF6ボタン押下可否 */
	boolean uf6EnableRyohi;
	/** UF7ボタン押下可否 */
	boolean uf7EnableRyohi;
	/** UF8ボタン押下可否 */
	boolean uf8EnableRyohi;
	/** UF9ボタン押下可否 */
	boolean uf9EnableRyohi;
	/** UF10ボタン押下可否 */
	boolean uf10EnableRyohi;
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** プロジェクト選択ボタン */
	boolean projectEnableRyohi;
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** セグメント選択ボタン */
	boolean segmentEnableRyohi;
	/** ユーザーに対する仮払案件有無 */
	String userKaribaraiUmuFlg = "0";
	/** 支払日登録可能。0:非表示、1:入力可、2:表示 */
	int shiharaiBiMode;
	/** 駅すぱあと連携可否 */
	boolean ekispertEnable;
	/** 支払方法モード */
	boolean disableShiharaiHouhou;
	/** 日当等を表示するか */
	boolean enableNittou = true;
	/** 代理起票フラグ（起票者のみ選択=0に固定）*/
	String dairiFlg = "0";
	/** 仮払選択制御 */
	String karibaraiSentakuFlg = setting.kaigaiKaribaraiSentakuSeigyo();
	/** 外貨表示制御 */
	boolean enableGaika;
	/** 予算執行対象フラグ */
	boolean yosanShikkouTaishouFlg;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean enableBumonSecurity;
	/** 差引項目表示フラグ */
	boolean sasihikiHyoujiFlg = isNotEmpty(sashihikiName);
	/** 差引項目表示フラグ（海外） */
	boolean sasihikiHyoujiFlgKaigai = isNotEmpty(sashihikiNameKaigai);
	/** 差引項目外貨表示フラグ（海外） */
	boolean sashihikiHyoujiFlgKaigaiGaika = "1".equals(setting.kaigaiNittouTankaGaikaSettei());

	/** 使用者変更時の社員番号(使用者変更時のAction再呼出時に値が入る) */
	String shiyoushaHenkouShainNo = "";

	//明細単位制御情報
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean[] kamokuEdabanEnable;
	/** 負担部門選択ボタン */
	boolean[] futanBumonEnable;
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
	boolean[] enableMeisaiBumonSecurity;

	//タイトル
	/** A012:出張伺い申請（仮払申請）の仮払あり 伝票種別（タイトル） */
	String denpyouShubetsu;
	/** A012:出張伺い申請（仮払申請）の仮払なし 伝票種別（タイトル） */
	String denpyouKaribaraiNashiShubetsu;

//＜部品＞
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 旅費仮払ロジック */
	KaigaiRyohiKaribaraiShinseiLogic kaigaiRyohiKaribaraiShinseiLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** ワークフローイベント */
	WorkflowEventControlLogic wfEventLogic;
	/** ワークフローカテゴリー */
	WorkflowCategoryLogic wfCategoryLogic;
	/** 起票ナビカテゴリー */
	KihyouNaviCategoryLogic kihyouLogic;
	/** バッチ会計 */
	BatchKaikeiCommonLogic batchKaikeiLogic;
	/** 海外旅費仮払明細DAO */
	KaigaiRyohiKaribaraiMeisaiDao kaigaiRyohiKaribaraiMeisaiDao;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 科目マスタDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** 科目枝番残高Dao */
	KamokuEdabanZandakaAbstractDao kamokuEdabanZandakaDao;
	/** 部門マスタDao */
	BumonMasterAbstractDao bumonMasterDao;

//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {

		//画面入力（申請内容）
		// 項目										//項目名											//キー項目？
		//姓名の間に空白が入るので21文字
		checkString (userNameRyohi, 1, 21, ks.userName.getName() + "名", false);
		checkString (shainNoRyohi, 1, 15, ks.userName.getName() + "社員番号", false);
		checkDomain (karibaraiOn, Domain.FLG, ks.karibaraiOn.getName(), false);
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
		checkString (houmonsaki,1 ,200 , ks.houmonsaki.getName(), false);
		checkString (mokuteki,1 ,240 , ks.mokuteki.getName(), false);
		checkDomain (shiharaihouhou, shiharaihouhouDomain, ks.shiharaiHouhou.getName(), false);
		checkDate (shiharaiKiboubi, ks.shiharaiKiboubi.getName() ,				false);
		checkDate (seisankikanFrom, ks.seisankikan.getName() + "開始日", false);
		checkHour (seisankikanFromHour, ks.seisankikanJikoku.getName() + "開始（時）",false);
		checkMin (seisankikanFromMin, ks.seisankikanJikoku.getName() + "開始（分）",false);
		checkDate (seisankikanTo, ks.seisankikan.getName() + "終了日", false);
		checkHour (seisankikanToHour, ks.seisankikanJikoku.getName() + "終了（時）",false);
		checkMin (seisankikanToMin, ks.seisankikanJikoku.getName() + "終了（分）",false);
		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			checkKingakuOver0 (kingaku, ks.shinseiKingaku.getName(), false);
			checkKingakuOver1 (karibaraiKingaku, ks.karibaraiKingaku.getName(), false);
		} else {
			checkKingakuOver1 (kingaku, ks.shinseiKingaku.getName(), false);
			checkKingakuOver0 (karibaraiKingaku, ks.karibaraiKingaku.getName(), false);
		}
		checkString (shiwakeEdaNoRyohi,1 ,10, ks.torihiki.getName() + "コード", false);
		checkString (torihikiNameRyohi,1 ,20 , ks.torihiki.getName() + "名", false);
		checkString (kamokuCdRyohi,1 ,6 , ks.kamoku.getName() + "コード", false);
		checkString (kamokuNameRyohi,1 ,22 , ks.kamoku.getName() + "名", false);
		checkString (kamokuEdabanCdRyohi,1 ,12 , ks.kamokuEdaban.getName() + "コード", false);
		checkString (kamokuEdabanNameRyohi,1 ,20 , ks.kamokuEdaban.getName() + "名", false);
		checkString (futanBumonCdRyohi,1 ,8 , ks.futanBumon.getName() + "コード", false);
		checkString (futanBumonNameRyohi,1 ,20 , ks.futanBumon.getName() + "名", false);
		checkString(torihikisakiCdRyohi, 1, 12, ks.torihikisaki.getName() + "コード", false);
		checkString(torihikisakiNameRyohi, 1, 20, ks.torihikisaki.getName() + "名", false);
		checkString(projectCdRyohi, 1, 12, ks.project.getName() + "コード", false);
		checkString(projectNameRyohi, 1, 20, ks.project.getName() + "名", false);
		checkString(segmentCdRyohi, 1, 8, ks.segment.getName() + "コード", false);
		checkString(segmentNameRyohi, 1, 20, ks.segment.getName() + "名", false);
		checkString(uf1CdRyohi, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString(uf1NameRyohi, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString(uf2CdRyohi, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString(uf2NameRyohi, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString(uf3CdRyohi, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString(uf3NameRyohi, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString(uf4CdRyohi, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString(uf4NameRyohi, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString(uf5CdRyohi, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString(uf5NameRyohi, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString(uf6CdRyohi, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString(uf6NameRyohi, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString(uf7CdRyohi, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString(uf7NameRyohi, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString(uf8CdRyohi, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString(uf8NameRyohi, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString(uf9CdRyohi, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString(uf9NameRyohi, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString(uf10CdRyohi, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkString(uf10NameRyohi, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkString (tekiyouRyohi, 1, this.getIntTekiyouMaxLength(), ks.tekiyou.getName(), false);
		checkSJIS (tekiyouRyohi, ks.tekiyou.getName());
		checkDate (shiharaibi, "支払日", false);
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

		//画面入力（明細）
		for (int i = 0; i < shubetsuCd.length; i++) {
			String ip = makeMeisaiHeader(i);
			checkString (shubetsuCd[i],1 , 1, "種別コード:"											+ ip + "行目", false);
			checkString (shubetsu1[i], 1, 20, ks.shubetsu1.getName() + ":"	+ ip + "行目", false);
			checkString (shubetsu2[i], 1, 20, ks.shubetsu2.getName() + ":"	+ ip + "行目", false);
			checkDate (kikanFrom[i], ks.kikan.getName() + "開始日"			+ ":"	+ ip + "行目", false);
			checkDate (kikanTo[i], ks.kikan.getName() + "終了日"			+ ":"	+ ip + "行目", false);
			if (shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.SONOTA)) {
				checkNissuu0 (kyuujitsuNissuu[i], ks.kyuujitsuNissuu.getName() + ":"	+ ip + "行目", false);
			}
			checkDomain (koutsuuShudan[i], (kaigaiFlgRyohi[i].equals("1"))? kaigaiKoutsuuShudanDomain : koutsuuShudanDomain,
																				ks.koutsuuShudan.getName() + ":"	+ ip + "行目", false);
			if (shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)) {
				checkString (naiyou[i],1 ,512 , ks.naiyouKoutsuuhi.getName() + ":"	+ ip + "行目", false);
			}else {
				checkString (naiyou[i],1 ,512 , ks.naiyouNittou.getName() + ":"	+ ip + "行目", false);
				checkString (bikou[i],1 ,200 , ks.bikouNittou.getName() + ":"	+ ip + "行目", false);
			}
			checkDomain (oufukuFlg[i], Domain.FLG, ks.oufukuFlg.getName() + ":"	+ ip + "行目", false);
			checkDomain (jidounyuuryokuFlg[i], Domain.FLG, "自動入力フラグ:"										+ ip + "行目", false);
			//早安楽
			checkDomain (hayaFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(早フラグ):" 				+ ip + "行目", false);
			checkDomain (yasuFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(安フラグ):" 				+ ip + "行目", false);
			checkDomain (rakuFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(楽フラグ):" 				+ ip + "行目", false);
			if (kaigaiFlgRyohi[i].equals("1")) {
				checkString (heishuCdRyohi[i], 1, 4, ks.heishu.getName() + ":"					+ ip + "行目", false);
				checkKingakuDecimalMoreThan0(rateRyohi[i], ks.rate.getName() + ":"						+ ip + "行目", false);
				checkKingakuDecimalOver1 (gaikaRyohi[i], ks.gaika.getName() + ":"						+ ip + "行目", false);
				checkString (taniRyohi[i], 1, 20, "通貨単位" + ":"									+ ip + "行目", false);
			}
			if ( (karibaraiOn != null && karibaraiOn.equals("1")) || shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI) ) {
				checkNissuu0 (nissuu[i], ks.nissuu.getName() + ":"	+ ip + "行目", false);
				if (kaigaiFlgRyohi[i].equals("0")) {
					if(StringUtils.isNotEmpty(suuryouNyuryokuType[i]) && !suuryouNyuryokuType[i].equals("0")) {
						checkKingaku3thDecimalPlaceOver0 (tanka[i], ks.tanka.getName() + ":"	+ ip + "行目", false);
						checkDomain (suuryouNyuryokuType[i], suuryouNyuryokuTypeDomain, "数量入力タイプ:"						+ ip + "行目", false);
						checkKingakuDecimal (suuryou[i], "0.01", "999999999999.99", "数量:"									+ ip + "行目", false);
						checkString (suuryouKigou[i], 1, 5, "数量記号:"										+ ip + "行目", false);
					}else {
						checkKingakuOver0 (tanka[i], ks.tanka.getName() + ":"	+ ip + "行目", false);
						checkDomain (suuryouNyuryokuType[i], suuryouNyuryokuTypeDomain, "数量入力タイプ:"						+ ip + "行目", false);
					}
				}else {
					checkKingakuOver0 (tanka[i], ks.tanka.getName() + ":"	+ ip + "行目", false);
				}
				checkKingakuOver0 (meisaiKingaku[i], ks.meisaiKingaku.getName() + ":"	+ ip + "行目", false);
			} else {
				checkNissuu (nissuu[i], ks.nissuu.getName() + ":"	+ ip + "行目", false);
				checkKingakuOver1 (tanka[i], ks.tanka.getName() + ":"	+ ip + "行目", false);
				checkKingakuOver1 (meisaiKingaku[i], ks.meisaiKingaku.getName() + ":"	+ ip + "行目", false);
			}
		}
		//画面入力（経費明細）
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			String ip = "その他 経費 " + (i + 1);
			checkDomain (kaigaiFlg[i],EteamConst.Domain.FLG, ks.shucchouKbn.getName() + "："				+ ip + "行目", false);
			checkDate (shiyoubi[i], ks1.shiyoubi.getName() + "："					+ ip + "行目", false);
			checkDomain (shouhyouShorui[i],EteamConst.Domain.FLG, ks1.shouhyouShoruiFlg.getName() + "："			+ ip + "行目", false);
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
			checkDomain (zeiritsu[i], zeiritsuDomain, ks1.zeiritsu.getName() + "："					+ ip + "行目", false);
			checkDomain (keigenZeiritsuKbn[i], Domain.FLG, "軽減税率区分" + "："							+ ip + "行目", false);

			if (kaigaiFlg[i].equals("1")) {
				checkString (heishuCd[i], 1, 4, ks.heishu.getName() + "："					+ ip + "行目", false);
				checkKingakuDecimalMoreThan0(rate[i], ks.rate.getName() + "："						+ ip + "行目", false);
				checkKingakuDecimalOver1 (gaika[i], ks.gaika.getName() + "："					+ ip + "行目", false);
				checkString (tani[i], 1, 20, "通貨単位" + "："									+ ip + "行目", false);
			}

			if (karibaraiOn != null && karibaraiOn.equals("1")) {
				checkKingakuOver0 (shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："			+ ip + "行目", false);
			} else {
				checkKingakuOver1 (shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："			+ ip + "行目", false);
			}
			checkKingakuOver0 (hontaiKingaku[i], "本体金額合計" + "："								+ ip + "行目", false);
			checkKingakuOver0 (shouhizeigaku[i], "消費税額合計" + "："								+ ip + "行目", false);
			checkString (tekiyou[i], 1, this.getIntTekiyouMaxLength(), ks1.tekiyou.getName() + "："					+ ip + "行目", false);
			checkSJIS (tekiyou[i], ks1.tekiyou.getName() + "："					+ ip + "行目");
			checkString (kousaihiShousai[i], 1, 240, ks1.kousaihiShousai.getName() + "："			+ ip + "行目", false);
			checkNumberOver1 (kousaihiNinzuu[i], 1, 6, "交際費人数："			+ ip + "行目", false);
			checkKingakuOver0 (kousaihiHitoriKingaku[i], "交際費一人当たりの金額："	+ ip + "行目", false);
		}
	}

	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum  1:登録/更新、2:支払日更新
	 */
	protected void denpyouHissuCheck(int eventNum) {

		String[][] list;

		list = new String[][] {
			{userIdRyohi, ks.userName.getName() + "ID", ks.userName.getHissuFlgS(), "0"},
			{userNameRyohi, ks.userName.getName() + "名", ks.userName.getHissuFlgS(), "0"},
			{shainNoRyohi, ks.userName.getName() + "社員番号", ks.userName.getHissuFlgS(), "0"},
			{karibaraiOn, ks.karibaraiOn.getName(), ks.karibaraiOn.getHissuFlgS(),"0"},
		};
		hissuCheckCommon(list, eventNum);

		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード"	,ks.hf1.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード"	,ks.hf2.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード"	,ks.hf3.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード"	,ks.hf4.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード"	,ks.hf5.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード"	,ks.hf6.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード"	,ks.hf7.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード"	,ks.hf8.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード"	,ks.hf9.getHissuFlgS(), "0"},}, eventNum);
			if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード"	,ks.hf10.getHissuFlgS(), "0"},}, eventNum);
		}

		//画面入力（申請内容）
		list = new String[][] {
			{houmonsaki, ks.houmonsaki.getName(), ks.houmonsaki.getHissuFlgS(),"0"},
			{mokuteki, ks.mokuteki.getName(), ks.mokuteki.getHissuFlgS(),"0"},
			{seisankikanFrom, ks.seisankikan.getName() + "開始日", ks.seisankikan.getHissuFlgS(),"0"},
			{seisankikanFromHour, ks.seisankikanJikoku.getName() + "開始（時）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
			{seisankikanFromMin, ks.seisankikanJikoku.getName() + "開始（分）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
			{seisankikanTo, ks.seisankikan.getName() + "終了日", ks.seisankikan.getHissuFlgS(),"0"},
			{seisankikanToHour, ks.seisankikanJikoku.getName() + "終了（時）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
			{seisankikanToMin, ks.seisankikanJikoku.getName() + "終了（分）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
			{tekiyouRyohi, ks.tekiyou.getName(), ks.tekiyou.getHissuFlgS(),"0"},
		};
		hissuCheckCommon(list, eventNum);

		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			list = new String[][]{
				{shiwakeEdaNoRyohi, ks.torihiki.getName() + "コード", ks.torihiki.getHissuFlgS(),"0"},
				{torihikiNameRyohi, ks.torihiki.getName() + "名", ks.torihiki.getHissuFlgS(),"0"},
				{kamokuCdRyohi, ks.kamoku.getName() + "コード", ks.kamoku.getHissuFlgS(),"0"},
				{kamokuNameRyohi, ks.kamoku.getName() + "名", ks.kamoku.getHissuFlgS(),"0"},
				{kamokuEdabanCdRyohi, ks.kamokuEdaban.getName() + "コード", ks.kamokuEdaban.getHissuFlgS(),"0"},
				{kamokuEdabanNameRyohi, ks.kamokuEdaban.getName() + "名", ks.kamokuEdaban.getHissuFlgS(),"0"},
				{futanBumonCdRyohi, ks.futanBumon.getName() + "コード", ks.futanBumon.getHissuFlgS(),"0"},
				{futanBumonNameRyohi, ks.futanBumon.getName() + "名", ks.futanBumon.getHissuFlgS(),"0"},
				{torihikisakiCdRyohi, ks.torihikisaki.getName() + "コード", ks.torihikisaki.getHissuFlgS() ,"0"},
				{torihikisakiNameRyohi, ks.torihikisaki.getName() + "名", ks.torihikisaki.getHissuFlgS() ,"0"},
				{projectCdRyohi, ks.project.getName() + "コード",  ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0","0"},
				{projectNameRyohi, ks.project.getName() + "名",  ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0","0"},
				{segmentCdRyohi, ks.segment.getName() + "コード",  ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0","0"},
				{segmentNameRyohi, ks.segment.getName() + "名", ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0","0"},

			};
			hissuCheckCommon(list, eventNum);
		}

		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1CdRyohi, hfUfSeigyo.getUf1Name() + "コード"	, ks.uf1.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2CdRyohi, hfUfSeigyo.getUf2Name() + "コード"	, ks.uf2.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3CdRyohi, hfUfSeigyo.getUf3Name() + "コード"	, ks.uf3.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4CdRyohi, hfUfSeigyo.getUf4Name() + "コード"	, ks.uf4.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5CdRyohi, hfUfSeigyo.getUf5Name() + "コード"	, ks.uf5.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6CdRyohi, hfUfSeigyo.getUf6Name() + "コード"	, ks.uf6.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7CdRyohi, hfUfSeigyo.getUf7Name() + "コード"	, ks.uf7.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8CdRyohi, hfUfSeigyo.getUf8Name() + "コード"	, ks.uf8.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9CdRyohi, hfUfSeigyo.getUf9Name() + "コード"	, ks.uf9.getHissuFlgS() , "0"},}, eventNum);
			if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10CdRyohi, hfUfSeigyo.getUf10Name() + "コード"	, ks.uf10.getHissuFlgS() , "0"},}, eventNum);
		}

		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			list = new String[][]{
				{shiharaiKiboubi, ks.shiharaiKiboubi.getName(), ks.shiharaiKiboubi.getHissuFlgS(),"0"},
				{shiharaihouhou, ks.shiharaiHouhou.getName(), ks.shiharaiHouhou.getHissuFlgS(),"0"},
				{shiharaibi, "支払日", "0","1"},
			};
			hissuCheckCommon(list, eventNum);
		}

		list = new String[][]{
			{kingaku, ks.shinseiKingaku.getName(), ks.shinseiKingaku.getHissuFlgS(),"0"},
		};
		hissuCheckCommon(list, eventNum);

		//仮払ありのとき仮払金額の必須チェック
		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			list = new String[][] {
				{karibaraiKingaku, ks.karibaraiKingaku.getName(), ks.karibaraiKingaku.getHissuFlgS(),"0"},
			};
			hissuCheckCommon(list, eventNum);
		}

		//「仮払なし」のときは明細を１件は入力する
		if (karibaraiOn != null && karibaraiOn.equals("0")) {
			if (shubetsuCd[0].isEmpty() && shiwakeEdaNo[0].isEmpty()) {
				errorList.add("明細が1件も入力されていません。");
			}
		}

		//交通費明細
		if (!shubetsuCd[0].isEmpty()) {
			for (int i = 0; i < shubetsuCd.length; i++) {
				String ip = makeMeisaiHeader(i);
				list = new String[][]{
					//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{shubetsuCd[i], "種別コード" 									+ ":" + ip + "行目", "1","0"},
					{shubetsu1[i], ks.shubetsu1.getName() + ":" + ip + "行目", ks.shubetsu1.getHissuFlgS(),"0"},
					{kikanFrom[i], ks.kikan.getName() + "開始日"			+ ":" + ip + "行目", ks.kikan.getHissuFlgS(),"0"},
					{tanka[i], ks.tanka.getName() + ":" + ip + "行目", ks.tanka.getHissuFlgS(),"0"},
					{meisaiKingaku[i], ks.meisaiKingaku.getName() + ":" + ip + "行目", ks.meisaiKingaku.getHissuFlgS(),"0"},
				};
				hissuCheckCommon(list, eventNum);
				if (kaigaiFlgRyohi[i].equals("1")) {
					list = new String[][]{
					//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{heishuCdRyohi[i], ks.heishu.getName() + ":" 	+ ip + "行目", ks.heishu.getHissuFlgS(),"0"},
					{rateRyohi[i], ks.rate.getName() + ":" 	+ ip + "行目", ks.rate.getHissuFlgS(),"0"},
					{gaikaRyohi[i], ks.gaika.getName() + ":" 	+ ip + "行目", ks.gaika.getHissuFlgS(),"0"},
					{taniRyohi[i], "通貨単位"									+ ":" 	+ ip + "行目", "0","0"},
					};
					hissuCheckCommon(list, eventNum);
				}
				switch(shubetsuCd[i]) {
				case RYOHISEISAN_SYUBETSU.KOUTSUUHI:
					list = new String[][]{
					//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{shubetsu2[i], ks.shubetsu2.getName() + ":" 	+ ip + "行目", ks.shubetsu1.getHissuFlgS(),"0"},
					{koutsuuShudan[i], ks.koutsuuShudan.getName() + ":"	+ ip + "行目", ks.koutsuuShudan.getHissuFlgS(),"0"},
					{naiyou[i], ks.naiyouKoutsuuhi.getName() + ":"	+ ip + "行目", ks.naiyouKoutsuuhi.getHissuFlgS(),"0"},
					{bikou[i], ks.bikouKoutsuuhi.getName() + ":"	+ ip + "行目", ks.bikouKoutsuuhi.getHissuFlgS(),"0"},
					{jidounyuuryokuFlg[i], "自動入力フラグ"								+ ":"	+ ip + "行目", "1","0"},
					{hayaFlg[i], ks.hayaYasuRaku.getName() + "(早フラグ):"				+ ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
					{yasuFlg[i], ks.hayaYasuRaku.getName() + "(安フラグ):"				+ ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
					{rakuFlg[i], ks.hayaYasuRaku.getName() + "(楽フラグ):"				+ ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
					};
					hissuCheckCommon(list, eventNum);

					if(kaigaiFlgRyohi[i].equals("0") && (StringUtils.isNotEmpty(suuryouNyuryokuType[i]) && !suuryouNyuryokuType[i].equals("0"))) {
						list = new String[][]{
							{suuryou[i], "数量:"												+ ip + "行目", "1","0"},
							{suuryouKigou[i], "数量記号:"											+ ip + "行目", "1","0"},
						};
					}else {
						list = new String[][]{
							{oufukuFlg[i], ks.oufukuFlg.getName() + ":"	+ ip + "行目", ks.oufukuFlg.getHissuFlgS(),"0"},
						};
					}
					hissuCheckCommon(list, eventNum);
					break;
				case RYOHISEISAN_SYUBETSU.SONOTA:
					list = new String[][]{
					//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{kikanTo[i], ks.kikan.getName() + "終了日"		 	+ ":"	+ ip + "行目", ks.kikan.getHissuFlgS(),"0"},
					{kyuujitsuNissuu[i], ks.kyuujitsuNissuu.getName() + ":"	+ ip + "行目", ks.kyuujitsuNissuu.getHissuFlgS(),"0"},
					{naiyou[i], ks.naiyouNittou.getName() + ":"	+ ip + "行目", ks.naiyouNittou.getHissuFlgS(),"0"},
					{bikou[i], ks.bikouNittou.getName() + ":"	+ ip + "行目", ks.bikouNittou.getHissuFlgS(),"0"},
					{nissuu[i], ks.nissuu.getName() + ":"	+ ip + "行目", ks.nissuu.getHissuFlgS(),"0"},
					};
					hissuCheckCommon(list, eventNum);
					break;
				}
			}
		}

		//その他明細
		if (!shiwakeEdaNo[0].isEmpty()) {
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				String ip = "その他 経費 " + (i + 1);
				list = new String[][]{
					//項目							項目名 														必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{kaigaiFlg[i], ks.shucchouKbn.getName() + "："			+ ip + "行目", ks.shucchouKbn.getHissuFlgS(), "0"},
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
					{zeiritsu[i], ks1.zeiritsu.getName() + "："		+ ip + "行目", ks1.zeiritsu.getHissuFlgS(), "0"},
					{keigenZeiritsuKbn[i], "軽減税率区分"				+ "："		+ ip + "行目", ks1.zeiritsu.getHissuFlgS(), "0"},
					{shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "："		+ ip + "行目", ks1.shiharaiKingaku.getHissuFlgS(), "0"},
					{tekiyou[i], ks1.tekiyou.getName() + "："				+ ip + "行目", ks1.tekiyou.getHissuFlgS(), "0"},
					{kousaihiShousai[i], ks1.kousaihiShousai.getName() + "："		+ ip + "行目", kousaihiEnable[i] ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
					{kousaihiNinzuu[i], "交際費人数："	+ ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0"},
					{kousaihiHitoriKingaku[i], "交際費一人当たりの金額：" + ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0"},
				};
				hissuCheckCommon(list, eventNum);

				if (kaigaiFlg[i].equals("1")) {
					list = new String[][]{
					//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{heishuCd[i], ks.heishu.getName() + ":" 	+ ip + "行目", ks.heishu.getHissuFlgS(),"0"},
					{rate[i], ks.rate.getName() + ":" 	+ ip + "行目", ks.rate.getHissuFlgS(),"0"},
					{gaika[i], ks.gaika.getName() + ":" 	+ ip + "行目", ks.gaika.getHissuFlgS(),"0"},
					{tani[i], "通貨単位"									+ ":" 	+ ip + "行目", "0","0"},
					};
					hissuCheckCommon(list, eventNum);
				}

				if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1Cd[i], hfUfSeigyo.getUf1Name() + "コード", ks1.uf1.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2Cd[i], hfUfSeigyo.getUf2Name() + "コード", ks1.uf2.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3Cd[i], hfUfSeigyo.getUf3Name() + "コード", ks1.uf3.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4Cd[i], hfUfSeigyo.getUf4Name() + "コード", ks1.uf4.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5Cd[i], hfUfSeigyo.getUf5Name() + "コード", ks1.uf5.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6Cd[i], hfUfSeigyo.getUf6Name() + "コード", ks1.uf6.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7Cd[i], hfUfSeigyo.getUf7Name() + "コード", ks1.uf7.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8Cd[i], hfUfSeigyo.getUf8Name() + "コード", ks1.uf8.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9Cd[i], hfUfSeigyo.getUf9Name() + "コード", ks1.uf9.getHissuFlgS()},}, 1);
				if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10Cd[i], hfUfSeigyo.getUf10Name() + "コード", ks1.uf10.getHissuFlgS()},}, 1);
			}
		}

		list = new String[][]{
			{hosoku, ks.hosoku.getName(), ks.hosoku.getHissuFlgS(),"0"},
		};

		// 会社設定に差引項目が入力されている場合
		if( sasihikiHyoujiFlgKaigai ){
			list = new String[][]{
					{sashihikiNumKaigai, ks.sashihikiNum.getName(), ks.sashihikiNum.getHissuFlgS(), "0"},
					{sashihikiKingakuKaigai, ks.sashihikiKingaku.getName(), ks.sashihikiKingaku.getHissuFlgS(), "0"},
				};
			hissuCheckCommon(list, eventNum);
		}
		if( sasihikiHyoujiFlg ){
			list = new String[][]{
					{sashihikiNum, ks.sashihikiNum.getName(), ks.sashihikiNum.getHissuFlgS(), "0"},
					{sashihikiKingaku, ks.sashihikiKingaku.getName(), ks.sashihikiKingaku.getHissuFlgS(), "0"},
				};
			hissuCheckCommon(list, eventNum);
		}
		hissuCheckCommon(list, eventNum);
	}

	/**
	 * 業務関連の関連チェック処理
	 * @param user_Id ユーザーID
	 */
	protected void soukanCheck(String user_Id) {

		// 精算期間開始日と精算期間終了日の整合性チェック
		for(Map<String, String> errMap: EteamCommon.kigenCheckWithJikoku(seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin)) {
			// 開始日と終了日の整合性チェック、分だけ入力されている場合入力エラーとする。
			if("2".equals(errMap.get("errorCode")) || "3".equals(errMap.get("errorCode")) || "4".equals(errMap.get("errorCode")) && !shubetsuCd[0].isEmpty()){
				errorList.add(ks.seisankikan.getName() + errMap.get("errorMassage"));
			}
		}

		//社員口座チェック
		commonLogic.checkShainKouza(user_Id, shiharaihouhou, errorList);

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

		// 共通機能（会計共通）．会計項目入力チェック
		if (isNotEmpty(shiwakeEdaNoRyohi)) {
			//旅費精算の仕訳パターン取得
			GMap shiwakePattern = kaikeiLogic.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdaNoRyohi));

			// 借方
			ShiwakeCheckData shiwakeCheckDataKari = commonLogic.new ShiwakeCheckData() ;
			shiwakeCheckDataKari.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
			shiwakeCheckDataKari.shiwakeEdaNo = shiwakeEdaNoRyohi;
			shiwakeCheckDataKari.kamokuNm = ks.kamoku.getName() + "コード";
			shiwakeCheckDataKari.kamokuCd = kamokuCdRyohi;
			shiwakeCheckDataKari.kamokuEdabanNm = ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKari.kamokuEdabanCd = kamokuEdabanCdRyohi;
			shiwakeCheckDataKari.futanBumonNm = ks.futanBumon.getName() + "コード";
			shiwakeCheckDataKari.futanBumonCd = futanBumonCdRyohi;
			shiwakeCheckDataKari.torihikisakiNm = ks.torihikisaki.getName() + "コード";
			shiwakeCheckDataKari.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kari_torihikisaki_cd")) ? torihikisakiCdRyohi : (String)shiwakePattern.get("kari_torihikisaki_cd");
			shiwakeCheckDataKari.projectNm = ks.project.getName() + "コード";
			if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kari_project_cd")))
			{
				shiwakeCheckDataKari.projectCd = projectCdRyohi;
			}
			shiwakeCheckDataKari.segmentNm = ks.segment.getName() + "コード";
			if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kari_segment_cd")))
			{
				shiwakeCheckDataKari.segmentCd = segmentCdRyohi;
			}
			shiwakeCheckDataKari.uf1Nm = hfUfSeigyo.getUf1Name();
			shiwakeCheckDataKari.uf2Nm = hfUfSeigyo.getUf2Name();
			shiwakeCheckDataKari.uf3Nm = hfUfSeigyo.getUf3Name();
			shiwakeCheckDataKari.uf4Nm = hfUfSeigyo.getUf4Name();
			shiwakeCheckDataKari.uf5Nm = hfUfSeigyo.getUf5Name();
			shiwakeCheckDataKari.uf6Nm = hfUfSeigyo.getUf6Name();
			shiwakeCheckDataKari.uf7Nm = hfUfSeigyo.getUf7Name();
			shiwakeCheckDataKari.uf8Nm = hfUfSeigyo.getUf8Name();
			shiwakeCheckDataKari.uf9Nm = hfUfSeigyo.getUf9Name();
			shiwakeCheckDataKari.uf10Nm = hfUfSeigyo.getUf10Name();
			// 以下、画面にはない項目
			shiwakeCheckDataKari.kazeiKbnNm = "借方課税区分";
			shiwakeCheckDataKari.kazeiKbn = kazeiKbnRyohi;
			// 使用者の代表負担部門コード
			String daihyouBumonCd = super.daihyouFutanBumonCd;
			// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
			if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
				daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf1_cd")))
			{
				shiwakeCheckDataKari.uf1Cd = kariUf1CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf2_cd")))
			{
				shiwakeCheckDataKari.uf2Cd = kariUf2CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf3_cd")))
			{
				shiwakeCheckDataKari.uf3Cd = kariUf3CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf4_cd")))
			{
				shiwakeCheckDataKari.uf4Cd = kariUf4CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf5_cd")))
			{
				shiwakeCheckDataKari.uf5Cd = kariUf5CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf6_cd")))
			{
				shiwakeCheckDataKari.uf6Cd = kariUf6CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf7_cd")))
			{
				shiwakeCheckDataKari.uf7Cd = kariUf7CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf8_cd")))
			{
				shiwakeCheckDataKari.uf8Cd = kariUf8CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf9_cd")))
			{
				shiwakeCheckDataKari.uf9Cd = kariUf9CdRyohi;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf10_cd")))
			{
				shiwakeCheckDataKari.uf10Cd = kariUf10CdRyohi;
			}
			commonLogic.checkShiwake(denpyouKbn, EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckDataKari, daihyouBumonCd, errorList);

			// 貸方
			ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData();
			shiwakeCheckDataKashi.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
			shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNoRyohi;
			shiwakeCheckDataKashi.kamokuNm = "貸方" + ks.kamoku.getName() + "コード";
			shiwakeCheckDataKashi.kamokuCd = kashiKamokuCdRyohi;
			shiwakeCheckDataKashi.kamokuEdabanNm = "貸方" + ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKashi.kamokuEdabanCd = kashiKamokuEdabanCdRyohi;
			shiwakeCheckDataKashi.futanBumonNm = "貸方" + ks.futanBumon.getName() + "コード";
			shiwakeCheckDataKashi.futanBumonCd = kashiFutanBumonCdRyohi;
			shiwakeCheckDataKashi.torihikisakiNm = "貸方" + ks.torihikisaki.getName() + "コード";
			shiwakeCheckDataKashi.kazeiKbnNm = "貸方課税区分";
			shiwakeCheckDataKashi.kazeiKbn = kashiKazeiKbnRyohi;
			shiwakeCheckDataKashi.projectNm = "貸方" + ks.project.getName() + "コード";
			shiwakeCheckDataKashi.segmentNm = "貸方" + ks.segment.getName() + "コード";
			shiwakeCheckDataKashi.uf1Nm = "貸方" + hfUfSeigyo.getUf1Name();
			shiwakeCheckDataKashi.uf2Nm = "貸方" + hfUfSeigyo.getUf2Name();
			shiwakeCheckDataKashi.uf3Nm = "貸方" + hfUfSeigyo.getUf3Name();
			shiwakeCheckDataKashi.uf4Nm = "貸方" + hfUfSeigyo.getUf4Name();
			shiwakeCheckDataKashi.uf5Nm = "貸方" + hfUfSeigyo.getUf5Name();
			shiwakeCheckDataKashi.uf6Nm = "貸方" + hfUfSeigyo.getUf6Name();
			shiwakeCheckDataKashi.uf7Nm = "貸方" + hfUfSeigyo.getUf7Name();
			shiwakeCheckDataKashi.uf8Nm = "貸方" + hfUfSeigyo.getUf8Name();
			shiwakeCheckDataKashi.uf9Nm = "貸方" + hfUfSeigyo.getUf9Name();
			shiwakeCheckDataKashi.uf10Nm = "貸方" + hfUfSeigyo.getUf10Name();

			switch (shiharaihouhou) {

			case SHIHARAI_HOUHOU.FURIKOMI:
				// 振込
				shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd1")) ? torihikisakiCdRyohi : (String)shiwakePattern.get("kashi_torihikisaki_cd1");
				if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd1")))
				{
					shiwakeCheckDataKashi.projectCd = projectCdRyohi;
				}
				if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd1")))
				{
					shiwakeCheckDataKashi.segmentCd = segmentCdRyohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd1")))
				{
					shiwakeCheckDataKashi.uf1Cd = kashiUf1Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd1")))
				{
					shiwakeCheckDataKashi.uf2Cd = kashiUf2Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd1")))
				{
					shiwakeCheckDataKashi.uf3Cd = kashiUf3Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd1")))
				{
					shiwakeCheckDataKashi.uf4Cd = kashiUf4Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd1")))
				{
					shiwakeCheckDataKashi.uf5Cd = kashiUf5Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd1")))
				{
					shiwakeCheckDataKashi.uf6Cd = kashiUf6Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd1")))
				{
					shiwakeCheckDataKashi.uf7Cd = kashiUf7Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd1")))
				{
					shiwakeCheckDataKashi.uf8Cd = kashiUf8Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd1")))
				{
					shiwakeCheckDataKashi.uf9Cd = kashiUf9Cd1Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd1")))
				{
					shiwakeCheckDataKashi.uf10Cd = kashiUf10Cd1Ryohi;
				}
				commonLogic.checkShiwake(denpyouKbn, EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi, daihyouBumonCd, errorList);
				break;

			case SHIHARAI_HOUHOU.GENKIN:
				// 現金
				shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd2")) ? torihikisakiCdRyohi : (String)shiwakePattern.get("kashi_torihikisaki_cd2");
				if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd2")))
				{
					shiwakeCheckDataKashi.projectCd = projectCdRyohi;
				}
				if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd2")))
				{
					shiwakeCheckDataKashi.segmentCd = segmentCdRyohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd2")))
				{
					shiwakeCheckDataKashi.uf1Cd = kashiUf1Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd2")))
				{
					shiwakeCheckDataKashi.uf2Cd = kashiUf2Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd2")))
				{
					shiwakeCheckDataKashi.uf3Cd = kashiUf3Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd2")))
				{
					shiwakeCheckDataKashi.uf4Cd = kashiUf4Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd2")))
				{
					shiwakeCheckDataKashi.uf5Cd = kashiUf5Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd2")))
				{
					shiwakeCheckDataKashi.uf6Cd = kashiUf6Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd2")))
				{
					shiwakeCheckDataKashi.uf7Cd = kashiUf7Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd2")))
				{
					shiwakeCheckDataKashi.uf8Cd = kashiUf8Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd2")))
				{
					shiwakeCheckDataKashi.uf9Cd = kashiUf9Cd2Ryohi;
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd2")))
				{
					shiwakeCheckDataKashi.uf10Cd = kashiUf10Cd2Ryohi;
				}
				commonLogic.checkShiwake(denpyouKbn, EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, shiwakeCheckDataKashi, daihyouBumonCd, errorList);
				break;
			}

			// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
			if (isNotEmpty(torihikisakiCdRyohi)) {
				ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
				torihikisaki.torihikisakiNm = ks.torihikisaki.getName() + "コード";
				torihikisaki.torihikisakiCd =torihikisakiCdRyohi;
				commonLogic.checkTorihikisaki(torihikisaki, errorList);

				// 取引先仕入先チェック
				super.checkShiiresaki(ks.torihikisaki.getName() + "コード", torihikisakiCdRyohi, denpyouKbn);
			}
		}

		// 明細部
		if (!shubetsuCd[0].isEmpty()) {
			for (int i = 0; i < kikanFrom.length; i++) {
				String ip = makeMeisaiHeader(i);
				// 精算期間開始日 ≦ 期間開始日 ≦ 精算期間終了日 でなければエラー
				if(kikanFrom[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(kikanFrom[i]) < 0) {
					errorList.add(ip + "行目：" + ks.kikan.getName() + "開始日は" + ks.seisankikan.getName() + "内の日付を入力してください。");
				}
				if(! kikanTo[i].isEmpty()) {

					// 精算期間開始日 ≦ 期間終了日 ≦ 精算期間終了日 でなければエラー
					if(kikanTo[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(kikanTo[i]) < 0) {
						errorList.add(ip + "行目：" + ks.kikan.getName() + "終了日は" + ks.seisankikan.getName() + "内の日付を入力してください。");
					}

					// 期間開始日と期間終了日の整合性チェック
					for(Map<String, String> errMap: EteamCommon.kigenCheck(kikanFrom[i], kikanTo[i])) {
						// 開始日と終了日の整合性チェックのみエラーとする。
						if("2".equals(errMap.get("errorCode"))){
							errorList.add(ip + "行目：" + ks.kikan.getName() + errMap.get("errorMassage"));
						}
					}
				}
			}
		}

		//税率チェック
		for (int i = 0; i < zeiritsu.length; i++) {
			String ip = "その他 経費 " + (i + 1);
			if(kaigaiFlg[i].equals("0") && isNotEmpty(shiwakeEdaNo[i])) commonLogic.checkZeiritsu(kazeiKbn[i], toDecimal(zeiritsu[i]), keigenZeiritsuKbn[i], toDate((shiyoubi[i])), errorList, ip + "行目：");
		}

		if (!shiwakeEdaNo[0].isEmpty()) {
			//明細単位の仕訳チェック
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				String ip = "その他 経費 " + (i + 1);
				ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData() ;
				ShiwakeCheckData shiwakeCheckDataKashi1 = commonLogic.new ShiwakeCheckData() ;
				// 使用者の代表負担部門コード
				String daihyouBumonCd = super.daihyouFutanBumonCd;
				// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
				if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
					daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
				}
				String denpyouKbnKeihi = kaigaiFlg[i].equals("1") ? "A901" : DENPYOU_KBN.KEIHI_TATEKAE_SEISAN;

				//各経費明細の仕訳パターン
				GMap shiwakePattern = kaikeiLogic.findShiwakePattern(denpyouKbnKeihi, Integer.parseInt(shiwakeEdaNo[i]));

				// 借方
				shiwakeCheckData.shiwakeEdaNoNm = ip + "行目：" + ks1.torihiki.getName() + "コード";
				shiwakeCheckData.shiwakeEdaNo = shiwakeEdaNo[i];
				shiwakeCheckData.kamokuNm = ip + "行目：" + ks1.kamoku.getName() + "コード";
				shiwakeCheckData.kamokuCd = kamokuCd[i];
				shiwakeCheckData.kamokuEdabanNm = ip + "行目：" + ks1.kamokuEdaban.getName() + "コード";
				shiwakeCheckData.kamokuEdabanCd = kamokuEdabanCd[i];
				shiwakeCheckData.futanBumonNm = ip + "行目：" + ks1.futanBumon.getName() + "コード";
				shiwakeCheckData.futanBumonCd = futanBumonCd[i];
				shiwakeCheckData.torihikisakiNm = ip + "行目：" + ks1.torihikisaki.getName() + "コード";
				shiwakeCheckData.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kari_torihikisaki_cd")) ? torihikisakiCd[i] : (String)shiwakePattern.get("kari_torihikisaki_cd");
				shiwakeCheckData.projectNm = ip + "行目：" + ks1.project.getName() + "コード";
				if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kari_project_cd")))
				{
					shiwakeCheckData.projectCd = projectCd[i];
				}
				shiwakeCheckData.segmentNm = ip + "行目：" + ks1.segment.getName() + "コード";
				if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kari_segment_cd")))
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
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf1_cd")))
				{
					shiwakeCheckData.uf1Cd = kariUf1Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf2_cd")))
				{
					shiwakeCheckData.uf2Cd = kariUf2Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf3_cd")))
				{
					shiwakeCheckData.uf3Cd = kariUf3Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf4_cd")))
				{
					shiwakeCheckData.uf4Cd = kariUf4Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf5_cd")))
				{
					shiwakeCheckData.uf5Cd = kariUf5Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf6_cd")))
				{
					shiwakeCheckData.uf6Cd = kariUf6Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf7_cd")))
				{
					shiwakeCheckData.uf7Cd = kariUf7Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf8_cd")))
				{
					shiwakeCheckData.uf8Cd = kariUf8Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf9_cd")))
				{
					shiwakeCheckData.uf9Cd = kariUf9Cd[i];
				}
				if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf10_cd")))
				{
					shiwakeCheckData.uf10Cd = kariUf10Cd[i];
				}
				commonLogic.checkShiwake(denpyouKbnKeihi, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckData, daihyouBumonCd, errorList);

				// 貸方
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
				shiwakeCheckDataKashi1.torihikisakiNm = ip + "行目：貸方" + ks1.torihikisaki.getName() + "コード";
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
					shiwakeCheckDataKashi1.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd1")) ? torihikisakiCd[i] : (String)shiwakePattern.get("kashi_torihikisaki_cd1");
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd1")))
					{
						shiwakeCheckDataKashi1.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd1")))
					{
						shiwakeCheckDataKashi1.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd1")))
					{
						shiwakeCheckDataKashi1.uf1Cd = kashiUf1Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd1")))
					{
						shiwakeCheckDataKashi1.uf2Cd = kashiUf2Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd1")))
					{
						shiwakeCheckDataKashi1.uf3Cd = kashiUf3Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd1")))
					{
						shiwakeCheckDataKashi1.uf4Cd = kashiUf4Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd1")))
					{
						shiwakeCheckDataKashi1.uf5Cd = kashiUf5Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd1")))
					{
						shiwakeCheckDataKashi1.uf6Cd = kashiUf6Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd1")))
					{
						shiwakeCheckDataKashi1.uf7Cd = kashiUf7Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd1")))
					{
						shiwakeCheckDataKashi1.uf8Cd = kashiUf8Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd1")))
					{
						shiwakeCheckDataKashi1.uf9Cd = kashiUf9Cd1[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd1")))
					{
						shiwakeCheckDataKashi1.uf10Cd = kashiUf10Cd1[i];
					}
					commonLogic.checkShiwake(denpyouKbnKeihi, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi1, daihyouBumonCd, errorList);
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					// 現金
					shiwakeCheckDataKashi1.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd2")) ? torihikisakiCd[i] : (String)shiwakePattern.get("kashi_torihikisaki_cd2");
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd2")))
					{
						shiwakeCheckDataKashi1.projectCd = projectCd[i];
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd2")))
					{
						shiwakeCheckDataKashi1.segmentCd = segmentCd[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd2")))
					{
						shiwakeCheckDataKashi1.uf1Cd = kashiUf1Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd2")))
					{
						shiwakeCheckDataKashi1.uf2Cd = kashiUf2Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd2")))
					{
						shiwakeCheckDataKashi1.uf3Cd = kashiUf3Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd2")))
					{
						shiwakeCheckDataKashi1.uf4Cd = kashiUf4Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd2")))
					{
						shiwakeCheckDataKashi1.uf5Cd = kashiUf5Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd2")))
					{
						shiwakeCheckDataKashi1.uf6Cd = kashiUf6Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd2")))
					{
						shiwakeCheckDataKashi1.uf7Cd = kashiUf7Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd2")))
					{
						shiwakeCheckDataKashi1.uf8Cd = kashiUf8Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd2")))
					{
						shiwakeCheckDataKashi1.uf9Cd = kashiUf9Cd2[i];
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd2")))
					{
						shiwakeCheckDataKashi1.uf10Cd = kashiUf10Cd2[i];
					}
					commonLogic.checkShiwake(denpyouKbnKeihi, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, shiwakeCheckDataKashi1, daihyouBumonCd, errorList);
					break;
				}

				// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
				if (isNotEmpty(torihikisakiCd[i])) {
					ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
					torihikisaki.torihikisakiNm = ip + "行目：" + ks1.torihikisaki.getName() + "コード";
					torihikisaki.torihikisakiCd = torihikisakiCd[i];
					commonLogic.checkTorihikisaki(torihikisaki, errorList);

					// 取引先仕入先チェック
					super.checkShiiresaki(ip + "行目：" + ks1.torihikisaki.getName() + "コード", torihikisakiCd[i], DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
				}

				// 表示フラグONかつ使用日入力ありのとき、精算期間開始日 ≦ 使用日 ≦ 精算期間終了日 でなければエラー
				if(ks1.shiyoubi.getHyoujiFlg() && !isEmpty(shiyoubi[i]) && ( shiyoubi[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(shiyoubi[i]) < 0)) {
					errorList.add(ip + "行目：" + ks1.shiyoubi.getName() + "は" + ks.seisankikan.getName() + "内の日付を入力してください。");
				}
			}
			//交際費が入力されている場合基準値を満たしているかチェック
			List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,shiwakeEdaNo,kousaihiHitoriKingaku,kaigaiFlg,commonLogic.KOUSAI_ERROR_CD_ERROR);
			for(GMap res : resultList) {if(res.get("errCd") != null && (int)res.get("errCd") == commonLogic.KOUSAI_ERROR_CD_ERROR ) errorList.add((String)res.get("errMsg"));}

		}

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

//＜伝票共通から呼ばれるイベント処理＞
	//個別伝票について表示処理を行う。
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		//参照フラグ(ture:参照起票である、false:参照起票でない)
		boolean sanshou = false;
		invoiceDenpyou = setInvoiceFlgWhenNew();

		//新規起票時の表示状態作成
		//代理起票時の使用者変更時も新規起票と同様の状態とさせる
		if ( (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) || isNotEmpty(shiyoushaHenkouShainNo)) {
			makeDefaultMeisai();
			makeDefaultMeisaiKeihi();

			//代理起票できるかチェック
			dairiCheck();

			if(isEmpty(shiyoushaHenkouShainNo)) {
				if (!(dairiFlg.equals("1"))) {
					//使用者=起票者をセット
					userIdRyohi   = getUser().getSeigyoUserId();
					userNameRyohi = getUser().getDisplayUserNameShort();//userInfo.getDisplayUserName();
					shainNoRyohi  = getUser().getShainNo();
				}else {
					//代理起票(参照起票ではない)ならクリア
					userIdRyohi   = "";
					userNameRyohi = "";
					shainNoRyohi  = "";
				}
			}else {
				//使用者=変更後の使用者をセット
				GMap usrMp = bumonUsrLogic.selectShainNo(shiyoushaHenkouShainNo);
				userIdRyohi   = usrMp.getString("user_id");
				userNameRyohi = usrMp.getString("user_sei") +  "　" + usrMp.getString("user_mei");
				shainNoRyohi  = shiyoushaHenkouShainNo;
			}

			// 申請と取引が1:1で決まる場合
			List<ShiwakePatternMaster> list = this.shiwakePatternMasterDao.loadByDenpyouKbn(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, this.futanBumonCdRyohi);
			if(!list.isEmpty() && setting.kaigaiKaribaraiSentakuSeigyo().equals("1")) {
				if(list.size() == 1) {
					shiwakeEdaNoRyohi = Integer.toString(list.get(0).shiwakeEdano);
					torihikiNameRyohi =  list.get(0).torihikiName;
					tekiyouRyohi = torihikiNameRyohi;
					if(list.get(0).tekiyouFlg.equals("1") ) {
						tekiyouRyohi = list.get(0).tekiyou;
					}
					shiharaihouhou = SHIHARAI_HOUHOU.FURIKOMI;
					if(!List.of("<FUTAN>","<DAIHYOUBUMON>","<SYOKIDAIHYOU>").contains(list.get(0).kariFutanBumonCd)) {
						this.futanBumonCdRyohi = list.get(0).kariFutanBumonCd;
					}
					if(!list.get(0).kariTorihikisakiCd.equals("<TORIHIKI>")) {
						this.torihikisakiCdRyohi = list.get(0).kariTorihikisakiCd;
					}
					if(!list.get(0).kariSegmentCd.equals("<SG>")) {
						this.segmentCdRyohi = list.get(0).kariSegmentCd;
					}
					if(!list.get(0).kariProjectCd.equals("<PROJECT>")) {
						this.projectCdRyohi = list.get(0).kariProjectCd;
					}
					// 仕訳パターン情報読込（相関チェック前に必要）
					reloadShiwakePattern(connection);
					//マスター等から名称は引き直す
					reloadMaster(connection);
					shiharaihouhou = null;
				}
			}

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			GMap shinseiData = kaikeiLogic.findKaigaiRyohiKaribarai(denpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);
			List<GMap> meisaiList = kaikeiLogic.loadKaigaiRyohiKaribaraiMeisai(denpyouId);
			if (!meisaiList.isEmpty()) {
				meisaiData2Gamen(meisaiList);
			} else {
				makeDefaultMeisai();
			}
			List<GMap> meisaiKeiriList = kaikeiLogic.loadKaigaiRyohiKeihiKaribaraiMeisai(denpyouId);
			if (!meisaiKeiriList.isEmpty()) {
				meisaiData2GamenKeihi(meisaiKeiriList, sanshou, shinseiData, connection);
			} else {
				makeDefaultMeisaiKeihi();
			}

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			GMap shinseiData = kaikeiLogic.findKaigaiRyohiKaribarai(sanshouDenpyouId);
			dairiFlg = shinseiData.get("dairiflg");
			if(getWfSeigyo().isOthersSanshouKihyou()) {
				commonLogic.adjustmentTashaCopy(dairiFlg, getUser(), denpyouKbn, shinseiData);
			}
			shinseiData2Gamen(shinseiData, sanshou, connection);
			List<GMap> meisaiList = kaikeiLogic.loadKaigaiRyohiKaribaraiMeisai(sanshouDenpyouId);
			if (!meisaiList.isEmpty()) {
				if(getWfSeigyo().isOthersSanshouKihyou()) {
					commonLogic.adjustmentTashaCopy(dairiFlg, getUser(), denpyouKbn, meisaiList);
				}
				meisaiData2Gamen(meisaiList);
			} else {
				makeDefaultMeisai();
			}
			List<GMap> meisaiKeiriList = kaikeiLogic.loadKaigaiRyohiKeihiKaribaraiMeisai(sanshouDenpyouId);
			if (!meisaiKeiriList.isEmpty()) {
				meisaiData2GamenKeihi(meisaiKeiriList, sanshou, shinseiData, connection);
			} else {
				makeDefaultMeisaiKeihi();
			}

			//代理起票できるかチェック
			dairiCheck();

			// 支払方法は会社設定で設定されている中になければクリア
			String shiharaiHouhouSetting = setting.shiharaiHouhouA005();
			String[] shiharaiHouhouArray = shiharaiHouhouSetting.split(",");
			if (!Arrays.asList(shiharaiHouhouArray).contains(shiharaihouhou))
			{
				shiharaihouhou = "";
			}

			// 日付はブランク
			seisankikanFrom = "";
			seisankikanTo = "";
			shiharaiKiboubi = "";
			for (int i = 0; i < kikanFrom.length; i++) {
				kikanFrom[i] = "";
				kikanTo[i] = "";
			}
			for (int i = 0; i < shiyoubi.length; i++) {
				shiyoubi[i] = "";
			}
			shiharaibi = "";
		}

		//表示制御（プルダウンとかの取得は表示方法によらず同じ）
		displaySeigyo(connection);

		// 更新モードの場合は差引単価に会社設定から取得した値を設定
		if(enableInput){
			sashihikiTanka = sashihikiTankaSI;
			sashihikiTankaKaigai = sashihikiTankaKaigaiSI;

			// 差引単価（海外）を差引単価外貨×差引レートで上書き
			if(sashihikiHyoujiFlgKaigaiGaika) {
				sashihikiHeishuCdKaigai = setting.sashihikiTankaKaigaiGaikaHeishu();
				GMap rateMasterMap = masterLogic.findHeishuCd(sashihikiHeishuCdKaigai);
				if(null != rateMasterMap) sashihikiRateKaigai = formatMoneyDecimalNoComma(rateMasterMap.get("rate"));
				sashihikiTankaKaigaiGaika = formatMoneyDecimal(toDecimal(setting.sashihikiTankaKaigaiGaika()));
				sashihikiTankaKaigai = formatMoney(commonLogic.calSashihikiTankaKaigai(toDecimal(sashihikiTankaKaigaiGaika), toDecimal(sashihikiRateKaigai)));
			}
		}
	}

	//登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		//余分な明細リストの削除
		extraMeisaiListDelete();

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 <errorList.size())
		{
			return;
		}

		// 各種コード設定と差引支給金額の計算（相関チェック前に必要）
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
		reloadMaster(connection);

		//相関チェック
		soukanCheck(userIdRyohi);
		if (0 <errorList.size())
		{
			return;
		}
	}

	//登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		// ないはずだが、全てふさいでおく
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
		String userSei = (userInfo == null) ? "" : (String)userInfo.get("user_sei");
		String userMei = (userInfo == null) ? "" : (String)userInfo.get("user_mei");

		//申請内容登録
		kaigaiRyohiKaribaraiShinseiLogic.insertShinsei(
			denpyouId,
			userIdRyohi,
			shainCd,
			userSei,
			userMei,
			karibaraiOn,
			dairiFlg,
			houmonsaki,
			mokuteki,
			toDate(seisankikanFrom),
			seisankikanFromHour,
			seisankikanFromMin,
			toDate(seisankikanTo),
			seisankikanToHour,
			seisankikanToMin,
			null,
			toDate(shiharaiKiboubi),
			shiharaihouhou,
			tekiyouRyohi,
			toDecimal(kingaku),
			toDecimal(karibaraiKingaku),
			toDecimal(sashihikiNum),
			toDecimal(sashihikiTanka),
			toDecimal(sashihikiNumKaigai),
			toDecimal(sashihikiTankaKaigai),
			sashihikiHeishuCdKaigai,
			toDecimal(sashihikiRateKaigai),
			toDecimal(sashihikiTankaKaigaiGaika),
			hf1Cd,
			hf1Name,
			hf2Cd,
			hf2Name,
			hf3Cd,
			hf3Name,
			hf4Cd,
			hf4Name,
			hf5Cd,
			hf5Name,
			hf6Cd,
			hf6Name,
			hf7Cd,
			hf7Name,
			hf8Cd,
			hf8Name,
			hf9Cd,
			hf9Name,
			hf10Cd,
			hf10Name,
			hosoku,
			(isEmpty(shiwakeEdaNoRyohi) ? null : Integer.parseInt(shiwakeEdaNoRyohi)),
			torihikiNameRyohi,
			futanBumonCdRyohi,
			futanBumonNameRyohi,
			torihikisakiCdRyohi,
			torihikisakiNameRyohi,
			kamokuCdRyohi == null ? "" : kamokuCdRyohi,
			kamokuNameRyohi == null ? "" : kamokuNameRyohi,
			kamokuEdabanCdRyohi == null ? "" : kamokuEdabanCdRyohi,
			kamokuEdabanNameRyohi == null ? "" : kamokuEdabanNameRyohi,
			kazeiKbnRyohi == null ? "" : kazeiKbnRyohi,
			kashiFutanBumonCdRyohi == null ? "" : kashiFutanBumonCdRyohi,
			kashiFutanBumonNameRyohi == null ? "" : kashiFutanBumonNameRyohi,
			kashiKamokuCdRyohi == null ? "" : kashiKamokuCdRyohi,
			kashiKamokuNameRyohi == null ? "" : kashiKamokuNameRyohi,
			kashiKamokuEdabanCdRyohi == null ? "" : kashiKamokuEdabanCdRyohi,
			kashiKamokuEdabanNameRyohi == null ? "" : kashiKamokuEdabanNameRyohi,
			kashiKazeiKbnRyohi == null ? "" : kashiKazeiKbnRyohi,
			uf1CdRyohi,
			uf1NameRyohi,
			uf2CdRyohi,
			uf2NameRyohi,
			uf3CdRyohi,
			uf3NameRyohi,
			uf4CdRyohi,
			uf4NameRyohi,
			uf5CdRyohi,
			uf5NameRyohi,
			uf6CdRyohi,
			uf6NameRyohi,
			uf7CdRyohi,
			uf7NameRyohi,
			uf8CdRyohi,
			uf8NameRyohi,
			uf9CdRyohi,
			uf9NameRyohi,
			uf10CdRyohi,
			uf10NameRyohi,
			projectCdRyohi,
			projectNameRyohi,
			segmentCdRyohi,
			segmentNameRyohi,
			tekiyouCdRyohi == null ? "" : tekiyouCdRyohi,
			null,
			super.getUser().getTourokuOrKoushinUserId());

		//明細登録（旅費）
		if (!shubetsuCd[0].isEmpty()) {
			for (int i = 0; i < shubetsuCd.length; i++) {
				KaigaiRyohiKaribaraiMeisai dto = new KaigaiRyohiKaribaraiMeisai();
				dto.denpyouId = denpyouId;
				dto.kaigaiFlg = kaigaiFlgRyohi[i];
				dto.denpyouEdano = i + 1;
				dto.kikanFrom = toDate(kikanFrom[i]);
				dto.kikanTo = toDate(kikanTo[i]);
				dto.kyuujitsuNissuu = toDecimal(kyuujitsuNissuu[i]);
				dto.shubetsuCd = shubetsuCd[i];
				dto.shubetsu1 = shubetsu1[i];
				dto.shubetsu2 = shubetsu2[i];
				dto.hayaFlg = hayaFlg[i];
				dto.yasuFlg = yasuFlg[i];
				dto.rakuFlg = rakuFlg[i];
				dto.koutsuuShudan = koutsuuShudan[i];
				dto.shouhyouShoruiHissuFlg = shouhyouShoruiHissuFlg[i];
				dto.naiyou = naiyou[i];
				dto.bikou = bikou[i];
				dto.oufukuFlg = oufukuFlg[i];
				dto.jidounyuuryokuFlg = jidounyuuryokuFlg[i];
				dto.nissuu = toDecimal(nissuu[i]);
				dto.heishuCd = heishuCdRyohi[i];
				dto.rate = toDecimal(rateRyohi[i]);
				dto.gaika = toDecimal(gaikaRyohi[i]);
				dto.currencyUnit = taniRyohi[i];
				dto.tanka = toDecimal(tanka[i]);
				dto.suuryouNyuryokuType = suuryouNyuryokuType[i];
				dto.suuryou = toDecimal(suuryou[i]);
				dto.suuryouKigou = suuryouKigou[i];
				dto.meisaiKingaku = toDecimal(meisaiKingaku[i]);
				kaigaiRyohiKaribaraiMeisaiDao.insert(dto, super.getUser().getTourokuOrKoushinUserId());
			}
		}

		//明細登録（経費）
		if (!shiwakeEdaNo[0].isEmpty()) {
			for (int i = 0; i < shiwakeEdaNo.length; i++) {

				kaigaiRyohiKaribaraiShinseiLogic.insertKeihiMeisai(
					denpyouId,
					i + 1,
					kaigaiFlg[i],
					Integer.parseInt(shiwakeEdaNo[i]),
					toDate(shiyoubi[i]),
					shouhyouShorui[i],
					torihikiName[i],
					tekiyou[i],
					toDecimal(zeiritsu[i]),
					keigenZeiritsuKbn[i],
					kazeiFlg[i],
					toDecimal(shiharaiKingaku[i]),
					toDecimal(hontaiKingaku[i]),
					toDecimal(shouhizeigaku[i]),
					kousaihiHyoujiFlg[i],
					ninzuuRiyouFlg[i],
					kousaihiShousai[i],
					isEmpty(kousaihiNinzuu[i]) ? null : Integer.parseInt(kousaihiNinzuu[i]),
					toDecimal(kousaihiHitoriKingaku[i]),
					heishuCd[i],
					toDecimal(rate[i]),
					toDecimal(gaika[i]),
					tani[i],
					futanBumonCd[i],
					futanBumonName[i],
					torihikisakiCd[i],
					torihikisakiName[i],
					kamokuCd[i],
					kamokuName[i],
					kamokuEdabanCd[i],
					kamokuEdabanName[i],
					kazeiKbn[i],
					kashiFutanBumonCd[i],
					kashiFutanBumonName[i],
					kashiKamokuCd[i],
					kashiKamokuName[i],
					kashiKamokuEdabanCd[i],
					kashiKamokuEdabanName[i],
					kashiKazeiKbn[i],
					uf1Cd[i],
					uf1Name[i],
					uf2Cd[i],
					uf2Name[i],
					uf3Cd[i],
					uf3Name[i],
					uf4Cd[i],
					uf4Name[i],
					uf5Cd[i],
					uf5Name[i],
					uf6Cd[i],
					uf6Name[i],
					uf7Cd[i],
					uf7Name[i],
					uf8Cd[i],
					uf8Name[i],
					uf9Cd[i],
					uf9Name[i],
					uf10Cd[i],
					uf10Name[i],
					projectCd[i],
					projectName[i],
					segmentCd[i],
					segmentName[i],
					tekiyouCd[i],
					super.getUser().getTourokuOrKoushinUserId()
				);
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

		//余分な明細リストの削除
		extraMeisaiListDelete();

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);

		// 各種コード設定と差引支給金額の計算（相関チェック前に必要）
		if (0 <errorList.size())
		{
			return;
		}

		//支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo) && karibaraiOn.equals("1")) {
			denpyouHissuCheck(2);
			if (0 <errorList.size())
			{
				return;
			}
			commonLogic.checkShiharaiBi(denpyouId, toDate(shiharaibi), null, null, shiharaihouhou, loginUserInfo, errorList);
			if (0 <errorList.size())
			{
				return;
			}
		}

		// 各種コード設定と差引支給金額の計算
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
		reloadMaster(connection);

		//相関チェック
		soukanCheck(userIdRyohi);
		if (0 <errorList.size())
		{
			return;
		}

		GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		// ここはないはずだけど、全てふさいでおく
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
		String userSei = (userInfo == null) ? "" : (String)userInfo.get("user_sei");
		String userMei = (userInfo == null) ? "" : (String)userInfo.get("user_mei");

		//申請内容更新
		kaigaiRyohiKaribaraiShinseiLogic.updateShinsei(
			denpyouId,
			userIdRyohi,
			shainCd,
			userSei,
			userMei,
			karibaraiOn,
			houmonsaki,
			mokuteki,
			toDate(seisankikanFrom),
			seisankikanFromHour,
			seisankikanFromMin,
			toDate(seisankikanTo),
			seisankikanToHour,
			seisankikanToMin,
			toDate(shiharaibi),
			toDate(shiharaiKiboubi),
			shiharaihouhou,
			tekiyouRyohi,
			toDecimal(kingaku),
			toDecimal(karibaraiKingaku),
			toDecimal(sashihikiNum),
			toDecimal(sashihikiTanka),
			toDecimal(sashihikiNumKaigai),
			toDecimal(sashihikiTankaKaigai),
			sashihikiHeishuCdKaigai,
			toDecimal(sashihikiRateKaigai),
			toDecimal(sashihikiTankaKaigaiGaika),
			hf1Cd,
			hf1Name,
			hf2Cd,
			hf2Name,
			hf3Cd,
			hf3Name,
			hf4Cd,
			hf4Name,
			hf5Cd,
			hf5Name,
			hf6Cd,
			hf6Name,
			hf7Cd,
			hf7Name,
			hf8Cd,
			hf8Name,
			hf9Cd,
			hf9Name,
			hf10Cd,
			hf10Name,
			hosoku,
			(isEmpty(shiwakeEdaNoRyohi) ? null : Integer.parseInt(shiwakeEdaNoRyohi)),
			torihikiNameRyohi,
			futanBumonCdRyohi,
			futanBumonNameRyohi,
			torihikisakiCdRyohi,
			torihikisakiNameRyohi,
			kamokuCdRyohi == null ? "" : kamokuCdRyohi,
			kamokuNameRyohi == null ? "" : kamokuNameRyohi,
			kamokuEdabanCdRyohi == null ? "" : kamokuEdabanCdRyohi,
			kamokuEdabanNameRyohi == null ? "" : kamokuEdabanNameRyohi,
			kazeiKbnRyohi == null ? "" : kazeiKbnRyohi,
			kashiFutanBumonCdRyohi == null ? "" : kashiFutanBumonCdRyohi,
			kashiFutanBumonNameRyohi == null ? "" : kashiFutanBumonNameRyohi,
			kashiKamokuCdRyohi == null ? "" : kashiKamokuCdRyohi,
			kashiKamokuNameRyohi == null ? "" : kashiKamokuNameRyohi,
			kashiKamokuEdabanCdRyohi == null ? "" : kashiKamokuEdabanCdRyohi,
			kashiKamokuEdabanNameRyohi == null ? "" : kashiKamokuEdabanNameRyohi,
			kashiKazeiKbnRyohi == null ? "" : kashiKazeiKbnRyohi,
			uf1CdRyohi,
			uf1NameRyohi,
			uf2CdRyohi,
			uf2NameRyohi,
			uf3CdRyohi,
			uf3NameRyohi,
			uf4CdRyohi,
			uf4NameRyohi,
			uf5CdRyohi,
			uf5NameRyohi,
			uf6CdRyohi,
			uf6NameRyohi,
			uf7CdRyohi,
			uf7NameRyohi,
			uf8CdRyohi,
			uf8NameRyohi,
			uf9CdRyohi,
			uf9NameRyohi,
			uf10CdRyohi,
			uf10NameRyohi,
			projectCdRyohi,
			projectNameRyohi,
			segmentCdRyohi,
			segmentNameRyohi,
			tekiyouCdRyohi == null ? "" : tekiyouCdRyohi,
			null,
			super.getUser().getTourokuOrKoushinUserId());

		//明細削除＆登録
		kaigaiRyohiKaribaraiMeisaiDao.delete(denpyouId);
		kaigaiRyohiKaribaraiShinseiLogic.deleteKeihiMeisai(denpyouId);

		//明細登録（旅費）
		if (!shubetsuCd[0].isEmpty()) {
			for (int i = 0; i < shubetsuCd.length; i++) {
				KaigaiRyohiKaribaraiMeisai dto = new KaigaiRyohiKaribaraiMeisai();
				dto.denpyouId = denpyouId;
				dto.kaigaiFlg = kaigaiFlgRyohi[i];
				dto.denpyouEdano = i + 1;
				dto.kikanFrom = toDate(kikanFrom[i]);
				dto.kikanTo = toDate(kikanTo[i]);
				dto.kyuujitsuNissuu = toDecimal(kyuujitsuNissuu[i]);
				dto.shubetsuCd = shubetsuCd[i];
				dto.shubetsu1 = shubetsu1[i];
				dto.shubetsu2 = shubetsu2[i];
				dto.hayaFlg = hayaFlg[i];
				dto.yasuFlg = yasuFlg[i];
				dto.rakuFlg = rakuFlg[i];
				dto.koutsuuShudan = koutsuuShudan[i];
				dto.shouhyouShoruiHissuFlg = shouhyouShoruiHissuFlg[i];
				dto.naiyou = naiyou[i];
				dto.bikou = bikou[i];
				dto.oufukuFlg = oufukuFlg[i];
				dto.jidounyuuryokuFlg = jidounyuuryokuFlg[i];
				dto.nissuu = toDecimal(nissuu[i]);
				dto.heishuCd = heishuCdRyohi[i];
				dto.rate = toDecimal(rateRyohi[i]);
				dto.gaika = toDecimal(gaikaRyohi[i]);
				dto.currencyUnit = taniRyohi[i];
				dto.tanka = toDecimal(tanka[i]);
				dto.suuryouNyuryokuType = suuryouNyuryokuType[i];
				dto.suuryou = toDecimal(suuryou[i]);
				dto.suuryouKigou = suuryouKigou[i];
				dto.meisaiKingaku = toDecimal(meisaiKingaku[i]);
				kaigaiRyohiKaribaraiMeisaiDao.insert(dto, super.getUser().getTourokuOrKoushinUserId());
			}
		}
		//明細登録（経費）
		if (!shiwakeEdaNo[0].isEmpty()) {
			for (int i = 0; i < shiwakeEdaNo.length; i++) {

				kaigaiRyohiKaribaraiShinseiLogic.insertKeihiMeisai(
					denpyouId,
					i + 1,
					kaigaiFlg[i],
					Integer.parseInt(shiwakeEdaNo[i]),
					toDate(shiyoubi[i]),
					shouhyouShorui[i],
					torihikiName[i],
					tekiyou[i],
					toDecimal(zeiritsu[i]),
					keigenZeiritsuKbn[i],
					kazeiFlg[i],
					toDecimal(shiharaiKingaku[i]),
					toDecimal(hontaiKingaku[i]),
					toDecimal(shouhizeigaku[i]),
					kousaihiHyoujiFlg[i],
					ninzuuRiyouFlg[i],
					kousaihiShousai[i],
					isEmpty(kousaihiNinzuu[i]) ? null : Integer.parseInt(kousaihiNinzuu[i]),
					toDecimal(kousaihiHitoriKingaku[i]),
					heishuCd[i],
					toDecimal(rate[i]),
					toDecimal(gaika[i]),
					tani[i],
					futanBumonCd[i],
					futanBumonName[i],
					torihikisakiCd[i],
					torihikisakiName[i],
					kamokuCd[i],
					kamokuName[i],
					kamokuEdabanCd[i],
					kamokuEdabanName[i],
					kazeiKbn[i],
					kashiFutanBumonCd[i],
					kashiFutanBumonName[i],
					kashiKamokuCd[i],
					kashiKamokuName[i],
					kashiKamokuEdabanCd[i],
					kashiKamokuEdabanName[i],
					kashiKazeiKbn[i],
					uf1Cd[i],
					uf1Name[i],
					uf2Cd[i],
					uf2Name[i],
					uf3Cd[i],
					uf3Name[i],
					uf4Cd[i],
					uf4Name[i],
					uf5Cd[i],
					uf5Name[i],
					uf6Cd[i],
					uf6Name[i],
					uf7Cd[i],
					uf7Name[i],
					uf8Cd[i],
					uf8Name[i],
					uf9Cd[i],
					uf9Name[i],
					uf10Cd[i],
					uf10Name[i],
					projectCd[i],
					projectName[i],
					segmentCd[i],
					segmentName[i],
					tekiyouCd[i],
					super.getUser().getTourokuOrKoushinUserId()
				);
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

		//支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo) && karibaraiOn.equals("1")) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			String shiharai = formatDate(kaikeiLogic.findKaigaiRyohiKaribarai(denpyouId).get("shiharaibi"));
			commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), null, null, shiharaihouhou, loginUserInfo, errorList);
			// エラーの場合の表示用に現実の値を設定
			shiharaibi = shiharai;
		}
	}

	//部門推奨ルートの基準金額を取得。
	//デフォルトでは親子共有するメンバ変数kingakuを返すので、子で特殊な制御をする場合はオーバーライド。
	@Override
	public String getKingaku() {
		if("0".equals(karibaraiOn)) {
			return this.kingaku;
		}
		return karibaraiKingaku;
	}

	//登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){
		BigDecimal zandaka = (BigDecimal)ringiData.get("ringiKingakuZandaka");
		if(null != toDecimal(karibaraiKingaku)){
			ringiData.put("ringiKingakuZandaka", zandaka.subtract(toDecimal(karibaraiKingaku)));
		}
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

	//代表仕訳枝番号を設定
	@Override
	protected String getDaihyouTorihiki(){
		return shiwakeEdaNoRyohi;
	}

	//仮払金未使用で精算済みかどうかを判定する。
	@Override
	protected boolean isKaribaraiMishiyouSeisanZumi(EteamConnection connection) {
		if(null == kaikeiLogic){
			kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		}
		if(null == commonLogic){
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		}

		boolean ret = false;
		GMap seisanWithKaribarai = kaikeiLogic.findKaigaiRyohiSeisanWithKaribarai(denpyouId);
		if(null != seisanWithKaribarai){
			String seisanDenpyouId = seisanWithKaribarai.get("denpyou_id").toString();
			GMap seisan = kaikeiLogic.findKaigaiRyohiSeisan(seisanDenpyouId);
			if( ("1".equals(seisan.get("karibarai_mishiyou_flg")) || "1".equals(seisan.get("shucchou_chuushi_flg")) )
					&& commonLogic.isAfterShinsei(seisanDenpyouId)){
				ret = true;
			}
		}
		return ret;
	}

	//科目が予算執行対象かどうか判定する。
	@Override
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection connection) {

		HashSet<String> kamokuCdSet = new HashSet<String>();
		kamokuCdSet.add(kamokuCdRyohi);
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
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		kaigaiRyohiKaribaraiShinseiLogic = EteamContainer.getComponent(KaigaiRyohiKaribaraiShinseiLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		wfCategoryLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		kaigaiRyohiKaribaraiMeisaiDao = EteamContainer.getComponent(KaigaiRyohiKaribaraiMeisaiDao.class, connection);
		naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		batchKaikeiLogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		kamokuEdabanZandakaDao = EteamContainer.getComponent(KamokuEdabanZandakaDao.class, connection);
		bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);
	}

	/**
	 * 旅費精算テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 旅費精算レコード
	 * @param sanshou     参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection  コネクション
	 */
	protected void shinseiData2Gamen(GMap shinseiData, boolean sanshou, EteamConnection connection) {
		karibaraiOn = (String)shinseiData.get("karibarai_on");
		dairiFlg = (String)shinseiData.get("dairiflg");
		userIdRyohi = (String)shinseiData.get("user_id");
		userNameRyohi = (String)shinseiData.get("user_sei") + "　" + (String)shinseiData.get("user_mei");
		shainNoRyohi = (String)shinseiData.get("shain_no");
		hf1Cd = (String)shinseiData.get("hf1_cd");
		hf1Name = (String)shinseiData.get("hf1_name_ryakushiki");
		hf2Cd = (String)shinseiData.get("hf2_cd");
		hf2Name = (String)shinseiData.get("hf2_name_ryakushiki");
		hf3Cd = (String)shinseiData.get("hf3_cd");
		hf3Name = (String)shinseiData.get("hf3_name_ryakushiki");
		hf4Cd = (String) shinseiData.get("hf4_cd");
		hf4Name = (String) shinseiData.get("hf4_name_ryakushiki");
		hf5Cd = (String) shinseiData.get("hf5_cd");
		hf5Name = (String) shinseiData.get("hf5_name_ryakushiki");
		hf6Cd = (String) shinseiData.get("hf6_cd");
		hf6Name = (String) shinseiData.get("hf6_name_ryakushiki");
		hf7Cd = (String) shinseiData.get("hf7_cd");
		hf7Name = (String) shinseiData.get("hf7_name_ryakushiki");
		hf8Cd = (String) shinseiData.get("hf8_cd");
		hf8Name = (String) shinseiData.get("hf8_name_ryakushiki");
		hf9Cd = (String) shinseiData.get("hf9_cd");
		hf9Name = (String) shinseiData.get("hf9_name_ryakushiki");
		hf10Cd = (String) shinseiData.get("hf10_cd");
		hf10Name = (String) shinseiData.get("hf10_name_ryakushiki");
		houmonsaki = (String)shinseiData.get("houmonsaki");
		mokuteki = (String)shinseiData.get("mokuteki");
		shiharaihouhou = (String)shinseiData.get("shiharaihouhou");
		shiharaiKiboubi = formatDate(shinseiData.get("shiharaikiboubi"));
		seisankikanFrom = formatDate(shinseiData.get("seisankikan_from"));
		seisankikanFromHour = (String)shinseiData.get("seisankikan_from_hour");
		seisankikanFromMin = (String)shinseiData.get("seisankikan_from_min");
		seisankikanTo = formatDate(shinseiData.get("seisankikan_to"));
		seisankikanToHour = (String)shinseiData.get("seisankikan_to_hour");
		seisankikanToMin = (String)shinseiData.get("seisankikan_to_min");
		kingaku = formatMoney(shinseiData.get("shinsei_kingaku"));
		karibaraiKingaku = formatMoney(shinseiData.get("karibarai_kingaku"));
		shiwakeEdaNoRyohi = ((shinseiData.get("shiwake_edano") == null) ? "" : ((Integer)shinseiData.get("shiwake_edano")).toString());
		torihikiNameRyohi = (String)shinseiData.get("torihiki_name");
		kamokuCdRyohi = (String)shinseiData.get("kari_kamoku_cd");
		kamokuNameRyohi = (String)shinseiData.get("kari_kamoku_name");
		kamokuEdabanCdRyohi = (String)shinseiData.get("kari_kamoku_edaban_cd");
		kamokuEdabanNameRyohi = (String)shinseiData.get("kari_kamoku_edaban_name");
		futanBumonCdRyohi = (String)shinseiData.get("kari_futan_bumon_cd");
		futanBumonNameRyohi = (String)shinseiData.get("kari_futan_bumon_name");
		torihikisakiCdRyohi = (String) shinseiData.get("torihikisaki_cd");
		torihikisakiNameRyohi = (String) shinseiData.get("torihikisaki_name_ryakushiki");
		projectCdRyohi = (String) shinseiData.get("project_cd");
		projectNameRyohi = (String) shinseiData.get("project_name");
		segmentCdRyohi = (String) shinseiData.get("segment_cd");
		segmentNameRyohi = (String) shinseiData.get("segment_name_ryakushiki");
		uf1CdRyohi = (String) shinseiData.get("uf1_cd");
		uf1NameRyohi = (String) shinseiData.get("uf1_name_ryakushiki");
		uf2CdRyohi = (String) shinseiData.get("uf2_cd");
		uf2NameRyohi = (String) shinseiData.get("uf2_name_ryakushiki");
		uf3CdRyohi = (String) shinseiData.get("uf3_cd");
		uf3NameRyohi = (String) shinseiData.get("uf3_name_ryakushiki");
		uf4CdRyohi = (String) shinseiData.get("uf4_cd");
		uf4NameRyohi = (String) shinseiData.get("uf4_name_ryakushiki");
		uf5CdRyohi = (String) shinseiData.get("uf5_cd");
		uf5NameRyohi = (String) shinseiData.get("uf5_name_ryakushiki");
		uf6CdRyohi = (String) shinseiData.get("uf6_cd");
		uf6NameRyohi = (String) shinseiData.get("uf6_name_ryakushiki");
		uf7CdRyohi = (String) shinseiData.get("uf7_cd");
		uf7NameRyohi = (String) shinseiData.get("uf7_name_ryakushiki");
		uf8CdRyohi = (String) shinseiData.get("uf8_cd");
		uf8NameRyohi = (String) shinseiData.get("uf8_name_ryakushiki");
		uf9CdRyohi = (String) shinseiData.get("uf9_cd");
		uf9NameRyohi = (String) shinseiData.get("uf9_name_ryakushiki");
		uf10CdRyohi = (String) shinseiData.get("uf10_cd");
		uf10NameRyohi = (String) shinseiData.get("uf10_name_ryakushiki");
		tekiyouRyohi = (String)shinseiData.get("tekiyou");
		var chuuki = super.setChuuki(sanshou, shinseiData, null, batchKaikeiLogic, commonLogic);
		this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : chuuki[0];
		this.chuuki2Ryohi = chuuki[1];
		shiharaibi = formatDate(shinseiData.get("shiharaibi"));
		if(sasihikiHyoujiFlg){
			sashihikiNum = (shinseiData.get("sashihiki_num") == null)? "" : shinseiData.get("sashihiki_num").toString();
			sashihikiTanka = (shinseiData.get("sashihiki_tanka") == null)? "": formatMoney(shinseiData.get("sashihiki_tanka"));
		} else {
			sashihikiNum = "";
			sashihikiTanka = "";
		}
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
		hosoku = (String)shinseiData.get("hosoku");
	}

	/**
	 * 明細のデフォルト状態に項目セットする。
	 */
	protected void makeDefaultMeisai() {

		// 明細の初期化(デフォルト値設定)
		String[] emptyArr = {""};
		kaigaiFlgRyohi = emptyArr;
		shubetsuCd = emptyArr;
		shubetsu1 = emptyArr;
		shubetsu2 = emptyArr;
		hayaFlg = emptyArr;
		yasuFlg = emptyArr;
		rakuFlg = emptyArr;
		kikanFrom = emptyArr;
		kikanTo = emptyArr;
		kyuujitsuNissuu = emptyArr;
		shouhyouShoruiHissuFlg = emptyArr;
		koutsuuShudan = emptyArr;
		naiyou = emptyArr;
		bikou = emptyArr;
		oufukuFlg = emptyArr;
		jidounyuuryokuFlg = emptyArr;
		nissuu = emptyArr;
		heishuCdRyohi = emptyArr;
		rateRyohi = emptyArr;
		gaikaRyohi = emptyArr;
		taniRyohi = emptyArr;
		tanka = emptyArr;
		suuryouNyuryokuType = emptyArr;
		suuryou = emptyArr;
		suuryouKigou = emptyArr;
		meisaiKingaku = emptyArr;
	}

	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList 明細レコードのリスト
	 */
	protected void meisaiData2Gamen(List<GMap> meisaiList) {
		int length = meisaiList.size();
		kaigaiFlgRyohi = new String[length];
		shubetsuCd = new String[length];
		shubetsu1 = new String[length];
		shubetsu2 = new String[length];
		hayaFlg = new String[length];
		yasuFlg = new String[length];
		rakuFlg = new String[length];
		kikanFrom = new String[length];
		kikanTo = new String[length];
		kyuujitsuNissuu = new String[length];
		shouhyouShoruiHissuFlg
							= new String[length];
		koutsuuShudan = new String[length];
		naiyou = new String[length];
		bikou = new String[length];
		oufukuFlg = new String[length];
		jidounyuuryokuFlg = new String[length];
		nissuu = new String[length];
		heishuCdRyohi = new String[length];
		rateRyohi = new String[length];
		gaikaRyohi = new String[length];
		taniRyohi = new String[length];
		suuryouNyuryokuType = new String[length];
		tanka = new String[length];
		suuryou = new String[length];
		suuryouKigou = new String[length];
		meisaiKingaku = new String[length];
		for (int i = 0; i < length ; i++) {
			GMap meisai = meisaiList.get(i);
			kaigaiFlgRyohi[i] = (String)meisai.get("kaigai_flg");
			shubetsuCd[i] = (String)meisai.get("shubetsu_cd");
			shubetsu1[i] = (String)meisai.get("shubetsu1");
			shubetsu2[i] = (String)meisai.get("shubetsu2");
			hayaFlg[i] = (String)meisai.get("haya_flg");
			yasuFlg[i] = (String)meisai.get("yasu_flg");
			rakuFlg[i] = (String)meisai.get("raku_flg");
			kikanFrom[i] = formatDate(meisai.get("kikan_from"));
			kikanTo[i] = formatDate(meisai.get("kikan_to"));
			kyuujitsuNissuu[i] = null == meisai.get("kyuujitsu_nissuu") ? "" : meisai.get("kyuujitsu_nissuu").toString();
			shouhyouShoruiHissuFlg[i]
									= (String)meisai.get("shouhyou_shorui_hissu_flg");
			koutsuuShudan[i] = (String)meisai.get("koutsuu_shudan");
			naiyou[i] = (String)meisai.get("naiyou");
			bikou[i] = (String)meisai.get("bikou");
			oufukuFlg[i] = (String)meisai.get("oufuku_flg");
			jidounyuuryokuFlg[i] = (String)meisai.get("jidounyuuryoku_flg");
			nissuu[i] = null == meisai.get("nissuu") ? "" : meisai.get("nissuu").toString();
			heishuCdRyohi[i] = (String)meisai.get("heishu_cd");
			rateRyohi[i] = formatMoneyDecimalNoComma(meisai.get("rate"));
			gaikaRyohi[i] = formatMoneyDecimal(meisai.get("gaika"));
			taniRyohi[i] = (String)meisai.get("currency_unit");
			suuryouNyuryokuType[i] = meisai.get("suuryou_nyuryoku_type");
			if(shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI) && kaigaiFlgRyohi[i].equals("0")) {
				if(!suuryouNyuryokuType[i].equals("0")) {
					tanka[i] = formatMoneyDecimalWithNoPadding(meisai.get("tanka"));
				}else {
					tanka[i] = formatMoney(meisai.get("tanka"));
				}
			}else {
				tanka[i] = formatMoney(meisai.get("tanka"));
			}
			suuryou[i] = formatMoneyDecimalWithNoPadding(meisai.get("suuryou"));
			suuryouKigou[i] = meisai.get("suuryou_kigou");
			meisaiKingaku[i] = formatMoney(meisai.get("meisai_kingaku"));
		}
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisaiKeihi() {
		//消費税率マップ
		GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();

		//表示項目
		shiwakeEdaNo = new String[1]; shiwakeEdaNo[0] = "";
		kaigaiFlg = new String[1]; kaigaiFlg[0] = "";
		shiyoubi = new String[1]; shiyoubi[0] = "";
		shouhyouShorui = new String[1]; shouhyouShorui[0] = "";
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
		keigenZeiritsuKbn = new String[1]; keigenZeiritsuKbn[0] = initZeiritsu.get("keigen_zeiritsu_kbn");
		heishuCd = new String[1]; heishuCd[0] = "";
		rate = new String[1]; rate[0] = "";
		gaika = new String[1]; gaika[0] = "";
		tani = new String[1]; tani[0] = "";
		kazeiFlg = new String[1]; kazeiFlg[0] = "";
		shiharaiKingaku = new String[1]; shiharaiKingaku[0] = "";
		hontaiKingaku = new String[1]; hontaiKingaku[0] = "";
		shouhizeigaku = new String[1]; shouhizeigaku[0] = "";
		tekiyou = new String[1]; tekiyou[0] = "";
		chuuki2 = new String[1]; chuuki2[0] = "";
		chuukiKousai2 = new String[2]; chuukiKousai2[0] = ""; chuukiKousai2[1] = "";
		kousaihiShousai = new String[1]; kousaihiShousai[0] = "";
		kousaihiNinzuu = new String[1]; kousaihiNinzuu[0] = "";
		kousaihiHitoriKingaku = new String[1]; kousaihiHitoriKingaku[0] = "";

		//制御情報
		kamokuEdabanEnable = new boolean[1];
		futanBumonEnable = new boolean[1];
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
	protected void meisaiData2GamenKeihi(List<GMap> meisaiList, boolean sanshou, GMap shinseiData, EteamConnection connection) {
		int length = meisaiList.size();
		shiwakeEdaNo = new String[length];
		kaigaiFlg = new String[length];
		shiyoubi = new String[length];
		shouhyouShorui = new String[length];
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
		heishuCd = new String[length];
		rate = new String[length];
		gaika = new String[length];
		tani = new String[length];
		kazeiFlg = new String[length];
		shiharaiKingaku = new String[length];
		hontaiKingaku = new String[length];
		shouhizeigaku = new String[length];
		tekiyou = new String[length];
		chuuki2 = new String[length];
		chuukiKousai2 = new String[length];
		kousaihiShousai = new String[length];
		kousaihiHyoujiFlg = new String[length];
		ninzuuRiyouFlg = new String[length];
		kousaihiNinzuu = new String[length];
		kousaihiHitoriKingaku = new String[length];
		for (int i = 0; i < length; i++) {
			GMap meisai = meisaiList.get(i);
			shiwakeEdaNo[i] = ((Integer)meisai.get("shiwake_edano")).toString();
			kaigaiFlg[i] = (String)meisai.get("kaigai_flg");
			shiyoubi[i] = formatDate(meisai.get("shiyoubi"));
			shouhyouShorui[i] = (String)meisai.get("shouhyou_shorui_flg");
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
			segmentCd[i] = (String) meisai.get("segment_cd");
			segmentName[i] = (String) meisai.get("segment_name_ryakushiki");
			uf1Cd[i] = (String) meisai.get("uf1_cd");
			uf1Name[i] = (String) meisai.get("uf1_name_ryakushiki");
			uf2Cd[i] = (String) meisai.get("uf2_cd");
			uf2Name[i] = (String) meisai.get("uf2_name_ryakushiki");
			uf3Cd[i] = (String) meisai.get("uf3_cd");
			uf3Name[i] = (String) meisai.get("uf3_name_ryakushiki");
			uf4Cd[i] = (String) meisai.get("uf4_cd");
			uf4Name[i] = (String) meisai.get("uf4_name_ryakushiki");
			uf5Cd[i] = (String) meisai.get("uf5_cd");
			uf5Name[i] = (String) meisai.get("uf5_name_ryakushiki");
			uf6Cd[i] = (String) meisai.get("uf6_cd");
			uf6Name[i] = (String) meisai.get("uf6_name_ryakushiki");
			uf7Cd[i] = (String) meisai.get("uf7_cd");
			uf7Name[i] = (String) meisai.get("uf7_name_ryakushiki");
			uf8Cd[i] = (String) meisai.get("uf8_cd");
			uf8Name[i] = (String) meisai.get("uf8_name_ryakushiki");
			uf9Cd[i] = (String) meisai.get("uf9_cd");
			uf9Name[i] = (String) meisai.get("uf9_name_ryakushiki");
			uf10Cd[i] = (String) meisai.get("uf10_cd");
			uf10Name[i] = (String) meisai.get("uf10_name_ryakushiki");
			kazeiKbn[i] = (String)meisai.get("kari_kazei_kbn");
			zeiritsu[i] = formatMoney(meisai.get("zeiritsu"));
			keigenZeiritsuKbn[i]= meisai.get("keigen_zeiritsu_kbn");
			heishuCd[i] = (String)meisai.get("heishu_cd");
			rate[i] = formatMoneyDecimalNoComma(meisai.get("rate"));
			gaika[i] = formatMoneyDecimal(meisai.get("gaika"));
			tani[i] = (String)meisai.get("currency_unit");
			kazeiFlg[i] = (String)meisai.get("kazei_flg");
			shiharaiKingaku[i] = formatMoney(meisai.get("shiharai_kingaku"));
			hontaiKingaku[i] = formatMoney(meisai.get("hontai_kingaku"));
			shouhizeigaku[i] = formatMoney(meisai.get("shouhizeigaku"));
			tekiyou[i] = (String)meisai.get("tekiyou");
			kousaihiShousai[i] = (String)meisai.get("kousaihi_shousai");
			kousaihiHyoujiFlg[i]= (String)meisai.get("kousaihi_shousai_hyouji_flg");
			ninzuuRiyouFlg[i]= (String)meisai.get("kousaihi_ninzuu_riyou_flg");
			kousaihiNinzuu[i] = meisai.get("kousaihi_ninzuu") == null ? "" : ((Integer)meisai.get("kousaihi_ninzuu")).toString();
			kousaihiHitoriKingaku[i] = formatMoney(meisai.get("kousaihi_hitori_kingaku"));
			var chuuki = super.setChuuki(sanshou, shinseiData, meisai, DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, batchKaikeiLogic, commonLogic);
			this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : chuuki[0];
			this.chuuki2[i] = chuuki[1];
		}
		if(!sanshou){
			//交際費関連の警告メッセージ取得
			chuukiKousai1 = "";
			List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,shiwakeEdaNo,kousaihiHitoriKingaku,kaigaiFlg,commonLogic.KOUSAI_ERROR_CD_KEIKOKU);
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
		//サーバーからの送信値が半角スペース→&nbsp;になっていてマスターと不一致になる為
		EteamCommon.trimNoBlank(shubetsu1);
		EteamCommon.trimNoBlank(shubetsu2);
		EteamCommon.trimNoBlank(koutsuuShudan);

		//入力（登録、更新）可能かどうか判定
		enableInput = super.judgeEnableInput();

		// リスト作成
		zeiritsuList = masterLogic.loadshouhizeiritsu();
		zeiritsuDomain = EteamCommon.mapList2Arr(zeiritsuList, "zeiritsu");
		kazeiKbnList = sysLogic.loadNaibuCdSetting("kazei_kbn");
		kazeiKbnDomain = EteamCommon.mapList2Arr(kazeiKbnList, "naibu_cd");
		koutsuuShudanList = masterLogic.loadKoutsuuShudan();
		koutsuuShudanDomain =  EteamCommon.mapList2Arr(koutsuuShudanList, "koutsuu_shudan");
		suuryouNyuryokuTypeDomain = EteamCommon.mapList2Arr(koutsuuShudanList, "suuryou_nyuryoku_type");
		kaigaiKoutsuuShudanList = masterLogic.loadKaigaiKoutsuuShudan();
		kaigaiKoutsuuShudanDomain =  EteamCommon.mapList2Arr(kaigaiKoutsuuShudanList, "koutsuu_shudan");
		shiharaihouhouList = commonLogic.makeShiharaiHouhouList(EteamSettingInfo.Key.SHIHARAI_HOUHOU_A005, shiharaihouhou);
		shiharaihouhouDomain = EteamCommon.mapList2Arr(shiharaihouhouList, "naibu_cd");
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);

		//支払方法を入力可能にするかどうか判定。
		disableShiharaiHouhou = (shiharaihouhouList.size() <= 1);

		//支払日の表示・入力判定。
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		//入力可能時の制御
		if (enableInput) {
			// 仕訳パターンによる制御
			if (isNotEmpty(shiwakeEdaNoRyohi)) {
				//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				GMap shiwakePattern = kaikeiLogic.findShiwakePattern(super.denpyouKbn, Integer.parseInt(shiwakeEdaNoRyohi));
				InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
				kamokuEdabanEnableRyohi = info.kamokuEdabanEnable;
				futanBumonEnableRyohi = info.futanBumonEnable;
				torihikisakiEnableRyohi = info.torihikisakiEnable;
				projectEnableRyohi = info.projectEnable;
				segmentEnableRyohi = info.segmentEnable;
				uf1EnableRyohi = info.uf1Enable;
				uf2EnableRyohi = info.uf2Enable;
				uf3EnableRyohi = info.uf3Enable;
				uf4EnableRyohi = info.uf4Enable;
				uf5EnableRyohi = info.uf5Enable;
				uf6EnableRyohi = info.uf6Enable;
				uf7EnableRyohi = info.uf7Enable;
				uf8EnableRyohi = info.uf8Enable;
				uf9EnableRyohi = info.uf9Enable;
				uf10EnableRyohi = info.uf10Enable;
			}
			// 駅すぱあと連携可否判定
			ekispertEnable = !"9".equals(setting.intraFlg());
			if(ekispertEnable) {
				// 駅すぱあと呼出 start
				super.ekispertInit(connection, SEARCH_MODE_UNCHIN);
			}
			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
			String initShainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
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
		}

		//経理担当が初めて伝票を開き、その伝票が振込のとき、支払日に自動入力
		if (shiharaiBiMode == 1  && isEmpty(shiharaibi) && shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI)) {
			Date shiharai = null;
			if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A012).equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(super.getKihyouBi()));
			} else if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A012).equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(super.getKihyouBi()));
			}
			if (shiharai != null) {
				shiharaibi = formatDate(shiharai);
				// マスターから基準日(起票日)に対する振込日が見つかった場合、支払日の更新
				kaigaiRyohiKaribaraiShinseiLogic.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}

		//申請画面チェック日数の表示制御
		//・申請期間チェック日数が有効のとき
		//・承認後、否認後、取下後でないとき
		//・精算期間開始日 + 1 - システム日付 <= 申請期間チェック日数
		if (!"0".equals(setting.kaigaiShinseiKikanCheckNissuu())
			&& isNotEmpty(seisankikanFrom)
			&& !(DENPYOU_JYOUTAI.SYOUNIN_ZUMI.equals(super.getDenpyouJoutai())
				|| DENPYOU_JYOUTAI.HININ_ZUMI.equals(super.getDenpyouJoutai())
				|| DENPYOU_JYOUTAI.TORISAGE_ZUMI.equals(super.getDenpyouJoutai()))
			&& (DateUtils.addDays(toDate(seisankikanFrom), 1).compareTo(DateUtils.addDays(new Date(System.currentTimeMillis()), Integer.parseInt(setting.shinseiKikanCheckNissuu()))) <= 0)
		) {
			shinseiChuuki = setting.kaigaiShinseiKikanCheckMongon();
			shinseiChuuki = shinseiChuuki.replace("<DAY>", setting.kaigaiShinseiKikanCheckNissuu());
		}

		//経費明細の表示設定
		if (!shiwakeEdaNo[0].isEmpty()) {
			//明細単位に仕訳パターンによる制御
			int length = shiwakeEdaNo.length;
			kamokuEdabanEnable = new boolean[length];
			futanBumonEnable = new boolean[length];
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
			for (int i = 0; i < length; i++) {
				if (enableInput) {
					if (isNotEmpty(shiwakeEdaNo[i])) {

						//※以下はDenpyouMeisaiAction側で再定義しているため無効な制御変数

						//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
						//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
						GMap shiwakePattern = kaikeiLogic.findShiwakePattern(kaigaiFlg[i].equals("1") ? "A901" : DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, Integer.parseInt(shiwakeEdaNo[i]));
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
						kousaihiEnable[i] = info.kousaihiEnable;
						ninzuuEnable[i] = info.ninzuuEnable;
					}
				} else {
					kousaihiEnable[i] = "1".equals(kousaihiHyoujiFlg[i]);
					ninzuuEnable[i] = "1".equals(ninzuuRiyouFlg[i]);
				}
			}
		}

		//タイトル切替用
		GMap denpyouShubetuMap = wfCategoryLogic.selectDenpyouShubetu(denpyouKbn);
		denpyouShubetsu = denpyouShubetuMap.get("denpyou_shubetsu").toString();
		denpyouKaribaraiNashiShubetsu = denpyouShubetuMap.get("denpyou_karibarai_nashi_shubetsu").toString();

		// 代理起票フラグ（パラメータ）
		if(isNotEmpty(denpyouId)) {
			GMap data = kaikeiLogic.findKaigaiRyohiKaribarai(denpyouId);
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

		// 外貨の表示可否
		enableGaika = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
		// 外貨非表示なら差引項目外貨表示フラグ（海外）は無効化
		if (!enableGaika)
		{
			sashihikiHyoujiFlgKaigaiGaika = false;
		}

		// 予算執行対象フラグの設定
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
		yosanShikkouTaishouFlg = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())
				                 && EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(denpyouShubetsuMap.get("yosan_shikkou_taishou").toString());
	}

	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {

		// 社員コード取得
		GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");

		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(userIdRyohi);

		if (isNotEmpty(shiwakeEdaNoRyohi)) {

			//仕訳パターン取得
			GMap shiwakeP = kaikeiLogic.findShiwakePattern(super.denpyouKbn, Integer.parseInt(shiwakeEdaNoRyohi));
			// 使用者の代表負担部門コード
			String daihyouBumonCd = super.daihyouFutanBumonCd;
			// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
			if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
				daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
			}

			//取引名
			torihikiNameRyohi = (String)shiwakeP.get("torihiki_name");

			//借方　科目
			kamokuCdRyohi = (String)shiwakeP.get("kari_kamoku_cd");

			//借方　科目枝番
			String pKariKamokuEdabanCd = (String)shiwakeP.get("kari_kamoku_edaban_cd");
			switch (pKariKamokuEdabanCd) {
				//何もしない(画面入力のまま)
				case EteamConst.ShiwakeConst.EDABAN:
					break;
				//固定コード値 or ブランク
				default:
					kamokuEdabanCdRyohi = pKariKamokuEdabanCd;
					break;
			}

			//借方　UF1-10
			if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.get("shain_cd_renkei_flg").equals(("1"))){
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
				{
					 uf1CdRyohi  = shainCd;
				}
				if (ufno == 2)
				{
					 uf2CdRyohi  = shainCd;
				}
				if (ufno == 3)
				{
					 uf3CdRyohi  = shainCd;
				}
				if (ufno == 4)
				{
					 uf4CdRyohi  = shainCd;
				}
				if (ufno == 5)
				{
					 uf5CdRyohi  = shainCd;
				}
				if (ufno == 6)
				{
					 uf6CdRyohi  = shainCd;
				}
				if (ufno == 7)
				{
					 uf7CdRyohi  = shainCd;
				}
				if (ufno == 8)
				{
					 uf8CdRyohi  = shainCd;
				}
				if (ufno == 9)
				{
					 uf9CdRyohi  = shainCd;
				}
				if (ufno == 10)
				{
					uf10CdRyohi = shainCd;
				}
			}
			kariUf1CdRyohi = commonLogic.convUf(uf1CdRyohi, (String)shiwakeP.get("kari_uf1_cd"));
			kariUf2CdRyohi = commonLogic.convUf(uf2CdRyohi, (String)shiwakeP.get("kari_uf2_cd"));
			kariUf3CdRyohi = commonLogic.convUf(uf3CdRyohi, (String)shiwakeP.get("kari_uf3_cd"));
			kariUf4CdRyohi = commonLogic.convUf(uf4CdRyohi, (String)shiwakeP.get("kari_uf4_cd"));
			kariUf5CdRyohi = commonLogic.convUf(uf5CdRyohi, (String)shiwakeP.get("kari_uf5_cd"));
			kariUf6CdRyohi = commonLogic.convUf(uf6CdRyohi, (String)shiwakeP.get("kari_uf6_cd"));
			kariUf7CdRyohi = commonLogic.convUf(uf7CdRyohi, (String)shiwakeP.get("kari_uf7_cd"));
			kariUf8CdRyohi = commonLogic.convUf(uf8CdRyohi, (String)shiwakeP.get("kari_uf8_cd"));
			kariUf9CdRyohi = commonLogic.convUf(uf9CdRyohi, (String)shiwakeP.get("kari_uf9_cd"));
			kariUf10CdRyohi = commonLogic.convUf(uf10CdRyohi, (String)shiwakeP.get("kari_uf10_cd"));

			//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
			enableBumonSecurity = true;
			//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
			if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.get("kari_futan_bumon_cd")) ){
				enableBumonSecurity = commonLogic.checkFutanBumonWithSecurity(futanBumonCdRyohi, ks1.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
			}
			//借方　負担部門
			futanBumonCdRyohi = commonLogic.convFutanBumon(futanBumonCdRyohi, (String)shiwakeP.get("kari_futan_bumon_cd"), daihyouBumonCd);

			// 借方　課税区分
			kazeiKbnRyohi = (String)shiwakeP.get("kari_kazei_kbn");

			//支払方法により貸方1 or 貸方2を切替する
			switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					//振込
					kashiKamokuCdRyohi = (String)shiwakeP.get("kashi_kamoku_cd1"); //貸方1　科目コード
					//貸方1　科目枝番コード
					String pKashiKamokuEdabanCd = (String)shiwakeP.get("kashi_kamoku_edaban_cd1");
					this.kashiKamokuEdabanCdRyohi = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
							? shainShiwakeEdaNo
							: pKashiKamokuEdabanCd;
					kashiFutanBumonCdRyohi = (String)shiwakeP.get("kashi_futan_bumon_cd1"); //貸方1　負担部門コード
					kashiKazeiKbnRyohi = (String)shiwakeP.get("kashi_kazei_kbn1"); //貸方1　課税区分
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					//現金
					kashiKamokuCdRyohi = (String)shiwakeP.get("kashi_kamoku_cd2"); //貸方2　科目コード
					kashiKamokuEdabanCdRyohi = (String)shiwakeP.get("kashi_kamoku_edaban_cd2"); //貸方2　科目枝番コード
					kashiFutanBumonCdRyohi = (String)shiwakeP.get("kashi_futan_bumon_cd2"); //貸方2　負担部門コード
					kashiKazeiKbnRyohi = (String)shiwakeP.get("kashi_kazei_kbn2"); //貸方2　課税区分
					break;
			}

			kashiUf1Cd1Ryohi = commonLogic.convUf(uf1CdRyohi, (String)shiwakeP.get("kashi_uf1_cd1"));
			kashiUf2Cd1Ryohi = commonLogic.convUf(uf2CdRyohi, (String)shiwakeP.get("kashi_uf2_cd1"));
			kashiUf3Cd1Ryohi = commonLogic.convUf(uf3CdRyohi, (String)shiwakeP.get("kashi_uf3_cd1"));
			kashiUf4Cd1Ryohi = commonLogic.convUf(uf4CdRyohi, (String)shiwakeP.get("kashi_uf4_cd1"));
			kashiUf5Cd1Ryohi = commonLogic.convUf(uf5CdRyohi, (String)shiwakeP.get("kashi_uf5_cd1"));
			kashiUf6Cd1Ryohi = commonLogic.convUf(uf6CdRyohi, (String)shiwakeP.get("kashi_uf6_cd1"));
			kashiUf7Cd1Ryohi = commonLogic.convUf(uf7CdRyohi, (String)shiwakeP.get("kashi_uf7_cd1"));
			kashiUf8Cd1Ryohi = commonLogic.convUf(uf8CdRyohi, (String)shiwakeP.get("kashi_uf8_cd1"));
			kashiUf9Cd1Ryohi = commonLogic.convUf(uf9CdRyohi, (String)shiwakeP.get("kashi_uf9_cd1"));
			kashiUf10Cd1Ryohi = commonLogic.convUf(uf10CdRyohi, (String)shiwakeP.get("kashi_uf10_cd1"));

			kashiUf1Cd2Ryohi = commonLogic.convUf(uf1CdRyohi, (String)shiwakeP.get("kashi_uf1_cd2"));
			kashiUf2Cd2Ryohi = commonLogic.convUf(uf2CdRyohi, (String)shiwakeP.get("kashi_uf2_cd2"));
			kashiUf3Cd2Ryohi = commonLogic.convUf(uf3CdRyohi, (String)shiwakeP.get("kashi_uf3_cd2"));
			kashiUf4Cd2Ryohi = commonLogic.convUf(uf4CdRyohi, (String)shiwakeP.get("kashi_uf4_cd2"));
			kashiUf5Cd2Ryohi = commonLogic.convUf(uf5CdRyohi, (String)shiwakeP.get("kashi_uf5_cd2"));
			kashiUf6Cd2Ryohi = commonLogic.convUf(uf6CdRyohi, (String)shiwakeP.get("kashi_uf6_cd2"));
			kashiUf7Cd2Ryohi = commonLogic.convUf(uf7CdRyohi, (String)shiwakeP.get("kashi_uf7_cd2"));
			kashiUf8Cd2Ryohi = commonLogic.convUf(uf8CdRyohi, (String)shiwakeP.get("kashi_uf8_cd2"));
			kashiUf9Cd2Ryohi = commonLogic.convUf(uf9CdRyohi, (String)shiwakeP.get("kashi_uf9_cd2"));
			kashiUf10Cd2Ryohi = commonLogic.convUf(uf10CdRyohi, (String)shiwakeP.get("kashi_uf10_cd2"));

			//画面に反映
			if (!isEmpty(kariUf1CdRyohi))
			{
				uf1CdRyohi = kariUf1CdRyohi;
			}
			if (!isEmpty(kariUf2CdRyohi))
			{
				uf2CdRyohi = kariUf2CdRyohi;
			}
			if (!isEmpty(kariUf3CdRyohi))
			{
				uf3CdRyohi = kariUf3CdRyohi;
			}
			if (!isEmpty(kariUf4CdRyohi))
			{
				uf4CdRyohi = kariUf4CdRyohi;
			}
			if (!isEmpty(kariUf5CdRyohi))
			{
				uf5CdRyohi = kariUf5CdRyohi;
			}
			if (!isEmpty(kariUf6CdRyohi))
			{
				uf6CdRyohi = kariUf6CdRyohi;
			}
			if (!isEmpty(kariUf7CdRyohi))
			{
				uf7CdRyohi = kariUf7CdRyohi;
			}
			if (!isEmpty(kariUf8CdRyohi))
			{
				uf8CdRyohi = kariUf8CdRyohi;
			}
			if (!isEmpty(kariUf9CdRyohi))
			{
				uf9CdRyohi = kariUf9CdRyohi;
			}
			if (!isEmpty(kariUf10CdRyohi))
			{
				uf10CdRyohi = kariUf10CdRyohi;
			}

			//代表部門
			kashiFutanBumonCdRyohi = commonLogic.convFutanBumon(futanBumonCdRyohi, kashiFutanBumonCdRyohi, daihyouBumonCd);

			//社員コードを摘要コードに反映する場合
			if("1".equals(shiwakeP.get("shain_cd_renkei_flg")) && "T".equals(setting.shainCdRenkei())) {
				if(shainCd.length() > 4) {
					tekiyouCdRyohi = shainCd.substring(shainCd.length()-4);
				} else {
					tekiyouCdRyohi = shainCd;
				}
			} else {
				tekiyouCdRyohi = "";
			}
		}

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

		enableMeisaiBumonSecurity = new boolean[length];

		//--------------------
		//経理明細情報の設定
		//--------------------
		if (!shiwakeEdaNo[0].isEmpty()) {
			for (int i = 0; i < length; i++) {

				// 使用者の代表負担部門コード
				String daihyouBumonCd = super.daihyouFutanBumonCd;
				// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
				if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
					daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
				}

				// 取引
				GMap shiwakeP = null;
				if (kaigaiFlg[i].equals("1")) {
					shiwakeP = kaikeiLogic.findShiwakePattern("A901", Integer.parseInt(shiwakeEdaNo[i]));
				} else {
					shiwakeP = kaikeiLogic.findShiwakePattern(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, Integer.parseInt(shiwakeEdaNo[i]));
				}

				//交際費
				kousaihiHyoujiFlg[i] = (String)shiwakeP.get("kousaihi_hyouji_flg");
				ninzuuRiyouFlg[i] = (String)shiwakeP.get("kousaihi_ninzuu_riyou_flg");
				if (! "1".equals(kousaihiHyoujiFlg[i])) {
					kousaihiShousai[i] = "";
					kousaihiNinzuu[i] = "";
					kousaihiHitoriKingaku[i] = "";
				}else if (! "1".equals(ninzuuRiyouFlg[i])) {
					kousaihiNinzuu[i] = "";
					kousaihiHitoriKingaku[i] = "";
				}

				//借方　取引先 ※仕訳チェック用、DB登録には関係ない
				kariTorihikisakiCd[i] = "".equals(shiwakeP.get("kari_torihikisaki_cd")) ? "" : torihikisakiCd[i];

				//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
				Arrays.fill(enableMeisaiBumonSecurity, true);
				//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
				if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.get("kari_futan_bumon_cd")) ){
					enableMeisaiBumonSecurity[i] = commonLogic.checkFutanBumonWithSecurity(futanBumonCd[i], ks1.futanBumon.getName() + "コード："+ (i+1) + "行目", super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
				}
				//借方　負担部門
				futanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], (String)shiwakeP.get("kari_futan_bumon_cd"), daihyouBumonCd);

				//借方　科目
				kamokuCd[i] = (String)shiwakeP.get("kari_kamoku_cd");

				//借方　科目枝番
				String pKariKamokuEdabanCd = (String)shiwakeP.get("kari_kamoku_edaban_cd");
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
				if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.get("shain_cd_renkei_flg").equals(("1"))){
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
				kariUf1Cd[i] = commonLogic.convUf(uf1Cd[i], (String)shiwakeP.get("kari_uf1_cd"));
				kariUf2Cd[i] = commonLogic.convUf(uf2Cd[i], (String)shiwakeP.get("kari_uf2_cd"));
				kariUf3Cd[i] = commonLogic.convUf(uf3Cd[i], (String)shiwakeP.get("kari_uf3_cd"));
				kariUf4Cd[i] = commonLogic.convUf(uf4Cd[i], (String)shiwakeP.get("kari_uf4_cd"));
				kariUf5Cd[i] = commonLogic.convUf(uf5Cd[i], (String)shiwakeP.get("kari_uf5_cd"));
				kariUf6Cd[i] = commonLogic.convUf(uf6Cd[i], (String)shiwakeP.get("kari_uf6_cd"));
				kariUf7Cd[i] = commonLogic.convUf(uf7Cd[i], (String)shiwakeP.get("kari_uf7_cd"));
				kariUf8Cd[i] = commonLogic.convUf(uf8Cd[i], (String)shiwakeP.get("kari_uf8_cd"));
				kariUf9Cd[i] = commonLogic.convUf(uf9Cd[i], (String)shiwakeP.get("kari_uf9_cd"));
				kariUf10Cd[i] = commonLogic.convUf(uf10Cd[i], (String)shiwakeP.get("kari_uf10_cd"));

				// 借方　消費税率
				String[] convZeiritsu = commonLogic.convZeiritsu(zeiritsu[i], keigenZeiritsuKbn[i], shiwakeP.get("kari_zeiritsu"), shiwakeP.get("kari_keigen_zeiritsu_kbn"));
				zeiritsu[i] = convZeiritsu[0];
				keigenZeiritsuKbn[i] = convZeiritsu[1];

				//支払方法により貸方1 or 貸方2を切替する
				switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					//振込
					kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.get("kashi_torihikisaki_cd1")) ? torihikisakiCd[i] : (String)shiwakeP.get("kashi_torihikisaki_cd1");//貸方1　取引先コード
					kashiFutanBumonCd[i] = (String)shiwakeP.get("kashi_futan_bumon_cd1"); //貸方1　負担部門コード
					kashiKamokuCd[i] = (String)shiwakeP.get("kashi_kamoku_cd1"); //貸方1　科目コード
					String pKashiKamokuEdabanCd = (String)shiwakeP.get("kashi_kamoku_edaban_cd1");
					this.kashiKamokuEdabanCd[i] = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
							? shainShiwakeEdaNo
							: pKashiKamokuEdabanCd; //貸方1　科目枝番コード
					kashiKazeiKbn[i] = (String)shiwakeP.get("kashi_kazei_kbn1"); //貸方1　課税区分
					kashiUf1Cd1[i] = commonLogic.convUf(uf1Cd[i], (String)shiwakeP.get("kashi_uf1_cd1")); //貸方1　UF1
					kashiUf2Cd1[i] = commonLogic.convUf(uf2Cd[i], (String)shiwakeP.get("kashi_uf2_cd1")); //貸方1　UF2
					kashiUf3Cd1[i] = commonLogic.convUf(uf3Cd[i], (String)shiwakeP.get("kashi_uf3_cd1")); //貸方1　UF3
					kashiUf4Cd1[i] = commonLogic.convUf(uf4Cd[i], (String)shiwakeP.get("kashi_uf4_cd1"));
					kashiUf5Cd1[i] = commonLogic.convUf(uf5Cd[i], (String)shiwakeP.get("kashi_uf5_cd1"));
					kashiUf6Cd1[i] = commonLogic.convUf(uf6Cd[i], (String)shiwakeP.get("kashi_uf6_cd1"));
					kashiUf7Cd1[i] = commonLogic.convUf(uf7Cd[i], (String)shiwakeP.get("kashi_uf7_cd1"));
					kashiUf8Cd1[i] = commonLogic.convUf(uf8Cd[i], (String)shiwakeP.get("kashi_uf8_cd1"));
					kashiUf9Cd1[i] = commonLogic.convUf(uf9Cd[i], (String)shiwakeP.get("kashi_uf9_cd1"));
					kashiUf10Cd1[i] = commonLogic.convUf(uf10Cd[i], (String)shiwakeP.get("kashi_uf10_cd1"));
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					//現金
					kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.get("kashi_torihikisaki_cd2")) ? torihikisakiCd[i] : (String)shiwakeP.get("kashi_torihikisaki_cd2");//貸方2　取引先コード
					kashiFutanBumonCd[i] = (String)shiwakeP.get("kashi_futan_bumon_cd2"); //貸方2　負担部門コード
					kashiKamokuCd[i] = (String)shiwakeP.get("kashi_kamoku_cd2"); //貸方2　科目コード
					kashiKamokuEdabanCd[i] = (String)shiwakeP.get("kashi_kamoku_edaban_cd2"); //貸方2　科目枝番コード
					kashiKazeiKbn[i] = (String)shiwakeP.get("kashi_kazei_kbn2"); //貸方2　課税区分
					kashiUf1Cd2[i] = commonLogic.convUf(uf1Cd[i], (String)shiwakeP.get("kashi_uf1_cd2")); //貸方2　UF1
					kashiUf2Cd2[i] = commonLogic.convUf(uf2Cd[i], (String)shiwakeP.get("kashi_uf2_cd2")); //貸方2　UF2
					kashiUf3Cd2[i] = commonLogic.convUf(uf3Cd[i], (String)shiwakeP.get("kashi_uf3_cd2")); //貸方2　UF3
					kashiUf4Cd2[i] = commonLogic.convUf(uf4Cd[i], (String)shiwakeP.get("kashi_uf4_cd2"));
					kashiUf5Cd2[i] = commonLogic.convUf(uf5Cd[i], (String)shiwakeP.get("kashi_uf5_cd2"));
					kashiUf6Cd2[i] = commonLogic.convUf(uf6Cd[i], (String)shiwakeP.get("kashi_uf6_cd2"));
					kashiUf7Cd2[i] = commonLogic.convUf(uf7Cd[i], (String)shiwakeP.get("kashi_uf7_cd2"));
					kashiUf8Cd2[i] = commonLogic.convUf(uf8Cd[i], (String)shiwakeP.get("kashi_uf8_cd2"));
					kashiUf9Cd2[i] = commonLogic.convUf(uf9Cd[i], (String)shiwakeP.get("kashi_uf9_cd2"));
					kashiUf10Cd2[i] = commonLogic.convUf(uf10Cd[i], (String)shiwakeP.get("kashi_uf10_cd2"));
					break;
				}

				//代表部門
				kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], kashiFutanBumonCd[i], daihyouBumonCd);

				//社員コードを摘要コードに反映する場合
				if("1".equals(shiwakeP.get("shain_cd_renkei_flg")) && "T".equals(setting.shainCdRenkei())) {
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
	}

	/**
	 * DB登録前にマスターから名称を取得する。（クライアントから送られた名称は破棄）
	 * @param connection コネクション
	 */
	protected void reloadMaster(EteamConnection connection) {
		//画面表示項目
		kamokuNameRyohi = this.kamokuMasterDao.findKamokuNameStr(kamokuCdRyohi);
		kamokuEdabanNameRyohi = this.kamokuEdabanZandakaDao.findEdabanName(kamokuCdRyohi, kamokuEdabanCdRyohi);
		futanBumonNameRyohi = enableBumonSecurity ? this.bumonMasterDao.findFutanBumonName(futanBumonCdRyohi) : "";
		torihikisakiNameRyohi = masterLogic.findTorihikisakiName(torihikisakiCdRyohi);
		projectNameRyohi = masterLogic.findProjectName(projectCdRyohi);
		segmentNameRyohi = masterLogic.findSegmentName(segmentCdRyohi);

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

		uf1NameRyohi = masterLogic.findUfName("1", uf1CdRyohi);
		uf2NameRyohi = masterLogic.findUfName("2", uf2CdRyohi);
		uf3NameRyohi = masterLogic.findUfName("3", uf3CdRyohi);
		uf4NameRyohi = masterLogic.findUfName("4", uf4CdRyohi);
		uf5NameRyohi = masterLogic.findUfName("5", uf5CdRyohi);
		uf6NameRyohi = masterLogic.findUfName("6", uf6CdRyohi);
		uf7NameRyohi = masterLogic.findUfName("7", uf7CdRyohi);
		uf8NameRyohi = masterLogic.findUfName("8", uf8CdRyohi);
		uf9NameRyohi = masterLogic.findUfName("9", uf9CdRyohi);
		uf10NameRyohi = masterLogic.findUfName("10", uf10CdRyohi);

		//貸方
		kashiKamokuNameRyohi = masterLogic.findKamokuNameStr(kashiKamokuCdRyohi);
		kashiKamokuEdabanNameRyohi = masterLogic.findKamokuEdabanName(kashiKamokuCdRyohi, kashiKamokuEdabanCdRyohi);
		kashiFutanBumonNameRyohi = masterLogic.findFutanBumonName(kashiFutanBumonCdRyohi);

		//とりあえず画面表示のない明細項目について、領域確保
		int length = shiwakeEdaNo.length;
		kashiFutanBumonName = new String[length];
		kashiKamokuName = new String[length];
		kashiKamokuEdabanName = new String[length];

		//明細項目を１件ずつ変換
		for (int i = 0; i < length; i++) {
			futanBumonName[i] = enableMeisaiBumonSecurity[i] ? masterLogic.findFutanBumonName(futanBumonCd[i]) : "";
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
	 * 領収書が必要かのチェックを行う。
	 * 出張旅費精算（仮払精算）追加した明細の中に「領収書・請求書等」のチェックがオンの明細が1件以上ある場合
	 * @return  領収書が必要か
	 */
	protected boolean isUseShouhyouShorui(){
		return false;
	}

	/**
	 * 海外出張旅費精算申請EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		KaigaiRyohikaribaraiShinseiXlsLogic kaigairyohikaribaraiseisanxlsLogic = EteamContainer.getComponent(KaigaiRyohikaribaraiShinseiXlsLogic.class, connection);
		kaigairyohikaribaraiseisanxlsLogic.makeExcel(denpyouId, out);
	}

	/**
	 * 余分な明細リストを削除する
	 */
	protected void extraMeisaiListDelete() {

		List<String> kaigaiFlgMeisaiList = new ArrayList<>();
		List<String> shubetsuCdMeisaiList = new ArrayList<>();
		List<String> shubetsu1MeisaiList = new ArrayList<>();
		List<String> shubetsu2MeisaiList = new ArrayList<>();
		List<String> hayaflgMeisaiList = new ArrayList<>();
		List<String> yasuflgMeisaiList = new ArrayList<>();
		List<String> rakuflgMeisaiList = new ArrayList<>();
		List<String> koutsuuShudanMeisaiList = new ArrayList<>();
		List<String> kikanFromMeisaiList = new ArrayList<>();
		List<String> kikanToMeisaiList = new ArrayList<>();
		List<String> kyuujitsuNissuuMeisaiList = new ArrayList<>();
		List<String> shouhyouShoruiHissuFlgMeisaiList = new ArrayList<>();
		List<String> naiyouMeisaiList = new ArrayList<>();
		List<String> bikouMeisaiList = new ArrayList<>();
		List<String> oufukuFlgMeisaiList = new ArrayList<>();
		List<String> jidounyuuryokuFlgMeisaiList = new ArrayList<>();
		List<String> nissuuMeisaiList = new ArrayList<>();
		List<String> heishuMeisaiList = new ArrayList<>();
		List<String> rateMeisaiList = new ArrayList<>();
		List<String> gaikaMeisaiList = new ArrayList<>();
		List<String> taniMeisaiList = new ArrayList<>();
		List<String> tankaMeisaiList = new ArrayList<>();
		List<String> suuryouNyuryokuTypeList = new ArrayList<>();
		List<String> suuryouList = new ArrayList<>();
		List<String> suuryouKigouList = new ArrayList<>();
		List<String> meisaiKingakuMeisaiList = new ArrayList<>();

		if (shubetsuCd != null) {
			for (int i = 0; i < shubetsuCd.length; i++) {
				kaigaiFlgMeisaiList.add(kaigaiFlgRyohi[i]);
				shubetsuCdMeisaiList.add(shubetsuCd[i]);
				shubetsu1MeisaiList.add(shubetsu1[i]);
				shubetsu2MeisaiList.add(shubetsu2[i]);
				hayaflgMeisaiList.add(hayaFlg[i]);
				yasuflgMeisaiList.add(yasuFlg[i]);
				rakuflgMeisaiList.add(rakuFlg[i]);
				koutsuuShudanMeisaiList.add(koutsuuShudan[i]);
				kikanFromMeisaiList.add(kikanFrom[i]);
				kikanToMeisaiList.add(kikanTo[i]);
				kyuujitsuNissuuMeisaiList.add(kyuujitsuNissuu[i]);
				shouhyouShoruiHissuFlgMeisaiList.add(shouhyouShoruiHissuFlg[i]);
				naiyouMeisaiList.add(naiyou[i]);
				bikouMeisaiList.add(bikou[i]);
				oufukuFlgMeisaiList.add(oufukuFlg[i]);
				jidounyuuryokuFlgMeisaiList.add(jidounyuuryokuFlg[i]);
				nissuuMeisaiList.add(nissuu[i]);
				heishuMeisaiList.add(heishuCdRyohi[i]);
				rateMeisaiList.add(rateRyohi[i]);
				gaikaMeisaiList.add(gaikaRyohi[i]);
				taniMeisaiList.add(taniRyohi[i]);
				tankaMeisaiList.add(tanka[i]);
				suuryouNyuryokuTypeList.add(suuryouNyuryokuType[i]);
				suuryouList.add(suuryou[i]);
				suuryouKigouList.add(suuryouKigou[i]);
				meisaiKingakuMeisaiList.add(meisaiKingaku[i]);
			}
		}

		int arraySize = shubetsuCdMeisaiList.size();
		for(int i = arraySize-1; i > -1; i--) {
			String shubetsuCdTmp = shubetsuCdMeisaiList.get(i);
			if(shubetsuCdTmp.equals("")) {
				kaigaiFlgMeisaiList.remove(i);
				shubetsuCdMeisaiList.remove(i);
				shubetsu1MeisaiList.remove(i);
				shubetsu2MeisaiList.remove(i);
				hayaflgMeisaiList.remove(i);
				yasuflgMeisaiList.remove(i);
				rakuflgMeisaiList.remove(i);
				koutsuuShudanMeisaiList.remove(i);
				kikanFromMeisaiList.remove(i);
				kikanToMeisaiList.remove(i);
				kyuujitsuNissuuMeisaiList.remove(i);
				shouhyouShoruiHissuFlgMeisaiList.remove(i);
				naiyouMeisaiList.remove(i);
				bikouMeisaiList.remove(i);
				oufukuFlgMeisaiList.remove(i);
				jidounyuuryokuFlgMeisaiList.remove(i);
				nissuuMeisaiList.remove(i);
				heishuMeisaiList.remove(i);
				rateMeisaiList.remove(i);
				gaikaMeisaiList.remove(i);
				taniMeisaiList.remove(i);
				tankaMeisaiList.remove(i);
				suuryouNyuryokuTypeList.remove(i);
				suuryouList.remove(i);
				suuryouKigouList.remove(i);
				meisaiKingakuMeisaiList.remove(i);
			}
		}

		if(shubetsuCdMeisaiList.size()==0) {
			kaigaiFlgMeisaiList.add("");
			shubetsuCdMeisaiList.add("");
			shubetsu1MeisaiList.add("");
			shubetsu2MeisaiList.add("");
			hayaflgMeisaiList.add("");
			yasuflgMeisaiList.add("");
			rakuflgMeisaiList.add("");
			koutsuuShudanMeisaiList.add("");
			kikanFromMeisaiList.add("");
			kikanToMeisaiList.add("");
			kyuujitsuNissuuMeisaiList.add("");
			shouhyouShoruiHissuFlgMeisaiList.add("");
			naiyouMeisaiList.add("");
			bikouMeisaiList.add("");
			oufukuFlgMeisaiList.add("");
			jidounyuuryokuFlgMeisaiList.add("");
			nissuuMeisaiList.add("");
			heishuMeisaiList.add("");
			rateMeisaiList.add("");
			gaikaMeisaiList.add("");
			taniMeisaiList.add("");
			tankaMeisaiList.add("");
			suuryouNyuryokuTypeList.add("");
			suuryouList.add("");
			suuryouKigouList.add("");
			meisaiKingakuMeisaiList.add("");
		}

		int length = shubetsuCdMeisaiList.size();
		kaigaiFlgRyohi              = new String[length];
		shubetsuCd                  = new String[length];
		shubetsu1                   = new String[length];
		shubetsu2                   = new String[length];
		hayaFlg                     = new String[length];
		yasuFlg                     = new String[length];
		rakuFlg                     = new String[length];
		koutsuuShudan               = new String[length];
		kikanFrom                   = new String[length];
		kikanTo                     = new String[length];
		kyuujitsuNissuu             = new String[length];
		shouhyouShoruiHissuFlg      = new String[length];
		naiyou                      = new String[length];
		bikou                       = new String[length];
		oufukuFlg                   = new String[length];
		jidounyuuryokuFlg           = new String[length];
		nissuu                      = new String[length];
		heishuCdRyohi               = new String[length];
		rateRyohi                   = new String[length];
		gaikaRyohi                  = new String[length];
		taniRyohi                   = new String[length];
		tanka                       = new String[length];
		suuryouNyuryokuType = new String[length];
		suuryou = new String[length];
		suuryouKigou = new String[length];
		meisaiKingaku               = new String[length];

		for (int i = 0; i < length ; i++) {
			kaigaiFlgRyohi[i]              = kaigaiFlgMeisaiList.get(i);
			shubetsuCd[i]                  = shubetsuCdMeisaiList.get(i);
			shubetsu1[i]                   = shubetsu1MeisaiList.get(i);
			shubetsu2[i]                   = shubetsu2MeisaiList.get(i);
			hayaFlg[i]                     = hayaflgMeisaiList.get(i);
			yasuFlg[i]                     = yasuflgMeisaiList.get(i);
			rakuFlg[i]                     = rakuflgMeisaiList.get(i);
			koutsuuShudan[i]               = koutsuuShudanMeisaiList.get(i);
			kikanFrom[i]                   = kikanFromMeisaiList.get(i);
			kikanTo[i]                     = kikanToMeisaiList.get(i);
			kyuujitsuNissuu[i]             = kyuujitsuNissuuMeisaiList.get(i);
			shouhyouShoruiHissuFlg[i]      = shouhyouShoruiHissuFlgMeisaiList.get(i);
			naiyou[i]                      = naiyouMeisaiList.get(i);
			bikou[i]                       = bikouMeisaiList.get(i);
			oufukuFlg[i]                   = oufukuFlgMeisaiList.get(i);
			jidounyuuryokuFlg[i]           = jidounyuuryokuFlgMeisaiList.get(i);
			nissuu[i]                      = nissuuMeisaiList.get(i);
			heishuCdRyohi[i]               = heishuMeisaiList.get(i);
			rateRyohi[i]                   = rateMeisaiList.get(i);
			gaikaRyohi[i]                  = gaikaMeisaiList.get(i);
			taniRyohi[i]                   = taniMeisaiList.get(i);
			tanka[i]                       = tankaMeisaiList.get(i);
			suuryouNyuryokuType[i]    = suuryouNyuryokuTypeList.get(i);
			suuryou[i]    = suuryouList.get(i);
			suuryouKigou[i]    = suuryouKigouList.get(i);
			meisaiKingaku[i]               = meisaiKingakuMeisaiList.get(i);
		}
	}

	/**
	 * 交通費、宿泊費等別の明細ヘッダーを返す。
	 * @param index 明細インデックス
	 * @return  交通費、宿泊費等別の明細ヘッダー
	 */
	protected String makeMeisaiHeader(int index) {
		String header = "";
		if(shubetsuCd[index].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)){
			header = "交通費 ";
		}
		else if(shubetsuCd[index].equals(RYOHISEISAN_SYUBETSU.SONOTA)){
			header = "日当・宿泊費等 ";
		}
		int ip = 0;
		String sCd = shubetsuCd[index];
		for (int i = 0; i < index+1 ; i++) {
			if (shubetsuCd[i].equals(sCd))
			{
				ip++;
			}
		}
		return header + String.valueOf(ip);
	}

	/**
	 * 代理起票できるかのチェック
	 */
	protected void dairiCheck() {
		//代理起票できるかチェック
		if (dairiFlg.equals("1")) {
			//代理権限ないユーザーはできない（メニューで非表示だがURL直接来られた時制御が破綻するし）
			GMap userJouhouMap = bumonUsrLogic.selectUserJouhou(getUser().getSeigyoUserId());
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
}
