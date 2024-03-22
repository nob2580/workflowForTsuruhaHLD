package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamConst.ShiwakeConstWa;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHAIN_CD_RENKEI;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.BumonMasterAbstractDao;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShiwakePatternSettingAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.BumonMasterDao;
import eteam.database.dao.InvoiceStartDao;
import eteam.database.dao.KamokuEdabanZandakaDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShiwakePatternSettingDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.InvoiceStart;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.TorihikiLogic.ShiwakePatternInputSeigyo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author j_matsumoto
 * 取引追加と変更の共通部
 */
@Getter @Setter @ToString
public class TorihikiCommonAction extends EteamAbstractAction {

	//＜定数＞

	//＜画面入力＞
		//全体
		/** 伝票種別 */
		String denpyouShubetsu;
		/** 有効期限開始日 */
		String yuukouKigenFrom ;
		/** 有効期限終了日 */
		String yuukouKigenTo;
		/** 分類1 */
		String bunrui1;
		/** 分類2 */
		String bunrui2;
		/** 分類3 */
		String bunrui3;
		/** 取引名 */
		String torihikiNm;
		/** 摘要フラグ */
		String tekiyouNyuryokuOn;
		/** 摘要 */
		String tekiyou;
		/** デフォルト表示 */
		String defaultHyoujiOn;
		/** 交際費 */
		String kousaihiOn;
		/** 人数項目 */
		String ninzuuOn;
		/** 交際費基準額 */
		String kousaihiKijungaku;
		/** 交際費チェック方法 */
		String kousaihiCheckHouhou;
		/** 交際費チェック後登録許可 */
		String kousaihiCheckResult;
		/** 掛け */
		String kakeOn;
		/** 表示順 */
		String hyoujijun;
		/** 社員コード連携 */
		String shainCdRenkei;
		/** 財務枝番コード連携 */
		String zaimuEdabanRenkei;

		//以下重複チェック用 会社設定参照
		/** インスタンス作成 */
		EteamSettingInfo duplicateCheck = new EteamSettingInfo();
		/** 社員コードの連携先取得 */
		String shainRenkei = duplicateCheck.shainCdRenkei();
		/** 法人カード識別用番号の連携先 */
		String houjinCardRenkei = duplicateCheck.houjinCardRenkei();
		/** ユニバーサルフィールドマッピング① */
		String uf1Mapping = duplicateCheck.uf1Mapping();
		/** ユニバーサルフィールドマッピング② */
		String uf2Mapping = duplicateCheck.uf2Mapping();
		/** ユニバーサルフィールドマッピング③ */
		String uf3Mapping = duplicateCheck.uf3Mapping();
		/** ユニバーサルフィールドマッピング④ */
		String uf4Mapping = duplicateCheck.uf4Mapping();
		/** ユニバーサルフィールドマッピング⑤ */
		String uf5Mapping = duplicateCheck.uf5Mapping();
		/** ユニバーサルフィールドマッピング⑥ */
		String uf6Mapping = duplicateCheck.uf6Mapping();
		/** ユニバーサルフィールドマッピング⑦ */
		String uf7Mapping = duplicateCheck.uf7Mapping();
		/** ユニバーサルフィールドマッピング⑧ */
		String uf8Mapping = duplicateCheck.uf8Mapping();
		/** ユニバーサルフィールドマッピング⑨ */
		String uf9Mapping = duplicateCheck.uf9Mapping();
		/** ユニバーサルフィールドマッピング⑩ */
		String uf10Mapping = duplicateCheck.uf10Mapping();
		/** ユニバーサルフィールドマッピング（固定値）① */
		String ufKotei1Mapping = duplicateCheck.ufKotei1Mapping();
		/** ユニバーサルフィールドマッピング（固定値）② */
		String ufKotei2Mapping = duplicateCheck.ufKotei2Mapping();
		/** ユニバーサルフィールドマッピング（固定値）③ */
		String ufKotei3Mapping = duplicateCheck.ufKotei3Mapping();
		/** ユニバーサルフィールドマッピング（固定値）④ */
		String ufKotei4Mapping = duplicateCheck.ufKotei4Mapping();
		/** ユニバーサルフィールドマッピング（固定値）⑤ */
		String ufKotei5Mapping = duplicateCheck.ufKotei5Mapping();
		/** ユニバーサルフィールドマッピング（固定値）⑥ */
		String ufKotei6Mapping = duplicateCheck.ufKotei6Mapping();
		/** ユニバーサルフィールドマッピング（固定値）⑦ */
		String ufKotei7Mapping = duplicateCheck.ufKotei7Mapping();
		/** ユニバーサルフィールドマッピング（固定値）⑧ */
		String ufKotei8Mapping = duplicateCheck.ufKotei8Mapping();
		/** ユニバーサルフィールドマッピング（固定値）⑨ */
		String ufKotei9Mapping = duplicateCheck.ufKotei9Mapping();
		/** ユニバーサルフィールドマッピング（固定値）⑩ */
		String ufKotei10Mapping = duplicateCheck.ufKotei10Mapping();

		//借
		/** (借方)負担部門コード */
		String kariFutanBumonCd;
		/** (借方)負担部門名 */
		String kariFutanBumonName;
		/** (借方)勘定科目コード */
		String kariKanjyouKamokuCd;
		/** (借方)勘定科目名 */
		String kariKanjyouKamokuName;
		/** (借方)勘定科目枝番コード */
		String kariKamokuEdabanCd;
		/** (借方)勘定科目枝番名 */
		String kariKamokuEdabanName;
		/** (借方)取引先コード */
		String kariTorihikisakiCd;
		/** (借方)取引先名 */
		String kariTorihikisakiName;
		/** (借方)プロジェクトコード */
		String kariProjectCd;
		/** (借方)プロジェクト名 */
		String kariProjectName;
		/** (借方)セグメントコード */
		String kariSegmentCd;
		/** (借方)セグメント名 */
		String kariSegmentName;
		/** (借方)課税区分 */
		String kariKazeiKbn;
		/** (借方)消費税率 */
		String kariZeiritsu;
		/** (借方)軽減税率区分 */
		String kariKeigenZeiritsuKbn;
		/** (借方)分離区分 */
		String kariBunriKbn;
		/** (借方)仕入区分 */
		String kariShiireKbn;
		/** ユニバーサルフィールド１コード */
		String kariUf1Cd;
		/** ユニバーサルフィールド１名 */
		String kariUf1Name;
		/** ユニバーサルフィールド２コード */
		String kariUf2Cd;
		/** ユニバーサルフィールド２名 */
		String kariUf2Name;
		/** ユニバーサルフィールド３コード */
		String kariUf3Cd;
		/** ユニバーサルフィールド３名 */
		String kariUf3Name;
		/** ユニバーサルフィールド４コード */
		String kariUf4Cd;
		/** ユニバーサルフィールド４名 */
		String kariUf4Name;
		/** ユニバーサルフィールド５コード */
		String kariUf5Cd;
		/** ユニバーサルフィールド５名 */
		String kariUf5Name;
		/** ユニバーサルフィールド６コード */
		String kariUf6Cd;
		/** ユニバーサルフィールド６名 */
		String kariUf6Name;
		/** ユニバーサルフィールド７コード */
		String kariUf7Cd;
		/** ユニバーサルフィールド７名 */
		String kariUf7Name;
		/** ユニバーサルフィールド８コード */
		String kariUf8Cd;
		/** ユニバーサルフィールド８名 */
		String kariUf8Name;
		/** ユニバーサルフィールド９コード */
		String kariUf9Cd;
		/** ユニバーサルフィールド９名 */
		String kariUf9Name;
		/** ユニバーサルフィールド１０コード */
		String kariUf10Cd;
		/** ユニバーサルフィールド１０名 */
		String kariUf10Name;
		/** ユニバーサルフィールド(固定値)１コード */
		String kariUfKotei1Cd;
		/** ユニバーサルフィールド(固定値)１名 */
		String kariUfKotei1Name;
		/** ユニバーサルフィールド(固定値)２コード */
		String kariUfKotei2Cd;
		/** ユニバーサルフィールド(固定値)２名 */
		String kariUfKotei2Name;
		/** ユニバーサルフィールド(固定値)３コード */
		String kariUfKotei3Cd;
		/** ユニバーサルフィールド(固定値)３名 */
		String kariUfKotei3Name;
		/** ユニバーサルフィールド(固定値)４コード */
		String kariUfKotei4Cd;
		/** ユニバーサルフィールド(固定値)４名 */
		String kariUfKotei4Name;
		/** ユニバーサルフィールド(固定値)５コード */
		String kariUfKotei5Cd;
		/** ユニバーサルフィールド(固定値)５名 */
		String kariUfKotei5Name;
		/** ユニバーサルフィールド(固定値)６コード */
		String kariUfKotei6Cd;
		/** ユニバーサルフィールド(固定値)６名 */
		String kariUfKotei6Name;
		/** ユニバーサルフィールド(固定値)７コード */
		String kariUfKotei7Cd;
		/** ユニバーサルフィールド(固定値)７名 */
		String kariUfKotei7Name;
		/** ユニバーサルフィールド(固定値)８コード */
		String kariUfKotei8Cd;
		/** ユニバーサルフィールド(固定値)８名 */
		String kariUfKotei8Name;
		/** ユニバーサルフィールド(固定値)９コード */
		String kariUfKotei9Cd;
		/** ユニバーサルフィールド(固定値)９名 */
		String kariUfKotei9Name;
		/** ユニバーサルフィールド(固定値)１０コード */
		String kariUfKotei10Cd;
		/** ユニバーサルフィールド(固定値)１０名 */
		String kariUfKotei10Name;
		/** (借方)旧課税区分 */
		String oldKariKazeiKbn;
		/** (借方)課税区分 */
		int kariShoriGroup;
		
		//貸1
		/** (貸方1)負担部門コード */
		String kashiFutanBumonCd1;
		/** (貸方1)負担部門名 */
		String kashiFutanBumonName1;
		/** (貸方1)勘定科目コード */
		String kashiKanjyouKamokuCd1;
		/** (貸方1)勘定科目名 */
		String kashiKanjyouKamokuName1;
		/** (貸方1)勘定科目枝番コード */
		String kashiKamokuEdabanCd1;
		/** (貸方1)勘定科目枝番名 */
		String kashiKamokuEdabanName1;
		/** (貸方1)取引先コード */
		String kashiTorihikisakiCd1;
		/** (貸方1)取引先名 */
		String kashiTorihikisakiName1;
		/** (貸方1)プロジェクトコード */
		String kashiProjectCd1;
		/** (貸方1)プロジェクト名 */
		String kashiProjectName1;
		/** (貸方1)セグメントコード */
		String kashiSegmentCd1;
		/** (貸方1)セグメント名 */
		String kashiSegmentName1;
		/** (貸方1)課税区分 */
		String kashiKazeiKbn1;
		/** (貸方1)分離区分 */
		String kashiBunriKbn1;
		/** (貸方1)仕入区分 */
		String kashiShiireKbn1;
		/** ユニバーサルフィールド１コード */
		String kashiUf1Cd1;
		/** ユニバーサルフィールド１名 */
		String kashiUf1Name1;
		/** ユニバーサルフィールド２コード */
		String kashiUf2Cd1;
		/** ユニバーサルフィールド２名 */
		String kashiUf2Name1;
		/** ユニバーサルフィールド３コード */
		String kashiUf3Cd1;
		/** ユニバーサルフィールド３名 */
		String kashiUf3Name1;
		/** ユニバーサルフィールド４コード */
		String kashiUf4Cd1;
		/** ユニバーサルフィールド４名 */
		String kashiUf4Name1;
		/** ユニバーサルフィールド５コード */
		String kashiUf5Cd1;
		/** ユニバーサルフィールド５名 */
		String kashiUf5Name1;
		/** ユニバーサルフィールド６コード */
		String kashiUf6Cd1;
		/** ユニバーサルフィールド６名 */
		String kashiUf6Name1;
		/** ユニバーサルフィールド７コード */
		String kashiUf7Cd1;
		/** ユニバーサルフィールド７名 */
		String kashiUf7Name1;
		/** ユニバーサルフィールド８コード */
		String kashiUf8Cd1;
		/** ユニバーサルフィールド８名 */
		String kashiUf8Name1;
		/** ユニバーサルフィールド９コード */
		String kashiUf9Cd1;
		/** ユニバーサルフィールド９名 */
		String kashiUf9Name1;
		/** ユニバーサルフィールド１０コード */
		String kashiUf10Cd1;
		/** ユニバーサルフィールド１０名 */
		String kashiUf10Name1;
		/** ユニバーサルフィールド(固定値)１コード */
		String kashiUfKotei1Cd1;
		/** ユニバーサルフィールド(固定値)１名 */
		String kashiUfKotei1Name1;
		/** ユニバーサルフィールド(固定値)２コード */
		String kashiUfKotei2Cd1;
		/** ユニバーサルフィールド(固定値)２名 */
		String kashiUfKotei2Name1;
		/** ユニバーサルフィールド(固定値)３コード */
		String kashiUfKotei3Cd1;
		/** ユニバーサルフィールド(固定値)３名 */
		String kashiUfKotei3Name1;
		/** ユニバーサルフィールド(固定値)４コード */
		String kashiUfKotei4Cd1;
		/** ユニバーサルフィールド(固定値)４名 */
		String kashiUfKotei4Name1;
		/** ユニバーサルフィールド(固定値)５コード */
		String kashiUfKotei5Cd1;
		/** ユニバーサルフィールド(固定値)５名 */
		String kashiUfKotei5Name1;
		/** ユニバーサルフィールド(固定値)６コード */
		String kashiUfKotei6Cd1;
		/** ユニバーサルフィールド(固定値)６名 */
		String kashiUfKotei6Name1;
		/** ユニバーサルフィールド(固定値)７コード */
		String kashiUfKotei7Cd1;
		/** ユニバーサルフィールド(固定値)７名 */
		String kashiUfKotei7Name1;
		/** ユニバーサルフィールド(固定値)８コード */
		String kashiUfKotei8Cd1;
		/** ユニバーサルフィールド(固定値)８名 */
		String kashiUfKotei8Name1;
		/** ユニバーサルフィールド(固定値)９コード */
		String kashiUfKotei9Cd1;
		/** ユニバーサルフィールド(固定値)９名 */
		String kashiUfKotei9Name1;
		/** ユニバーサルフィールド(固定値)１０コード */
		String kashiUfKotei10Cd1;
		/** ユニバーサルフィールド(固定値)１０名 */
		String kashiUfKotei10Name1;
		/** (貸方1)旧課税区分 */
		String oldKashiKazeiKbn1;

		//貸2
		/** (貸方2)負担部門コード */
		String kashiFutanBumonCd2;
		/** (貸方2)負担部門名 */
		String kashiFutanBumonName2;
		/** (貸方2)勘定科目コード */
		String kashiKanjyouKamokuCd2;
		/** (貸方2)勘定科目名 */
		String kashiKanjyouKamokuName2;
		/** (貸方2)勘定科目枝番コード */
		String kashiKamokuEdabanCd2;
		/** (貸方2)勘定科目枝番名 */
		String kashiKamokuEdabanName2;
		/** (貸方2)取引先コード */
		String kashiTorihikisakiCd2;
		/** (貸方2)取引先名 */
		String kashiTorihikisakiName2;
		/** (貸方2)プロジェクトコード */
		String kashiProjectCd2;
		/** (貸方2)プロジェクト名 */
		String kashiProjectName2;
		/** (貸方2)セグメントコード */
		String kashiSegmentCd2;
		/** (貸方2)セグメント名 */
		String kashiSegmentName2;
		/** (貸方2)課税区分 */
		String kashiKazeiKbn2;
		/** (貸方2)分離区分 */
		String kashiBunriKbn2;
		/** (貸方2)仕入区分 */
		String kashiShiireKbn2;
		/** ユニバーサルフィールド１コード */
		String kashiUf1Cd2;
		/** ユニバーサルフィールド１名 */
		String kashiUf1Name2;
		/** ユニバーサルフィールド２コード */
		String kashiUf2Cd2;
		/** ユニバーサルフィールド２名 */
		String kashiUf2Name2;
		/** ユニバーサルフィールド３コード */
		String kashiUf3Cd2;
		/** ユニバーサルフィールド３名 */
		String kashiUf3Name2;
		/** ユニバーサルフィールド４コード */
		String kashiUf4Cd2;
		/** ユニバーサルフィールド４名 */
		String kashiUf4Name2;
		/** ユニバーサルフィールド５コード */
		String kashiUf5Cd2;
		/** ユニバーサルフィールド５名 */
		String kashiUf5Name2;
		/** ユニバーサルフィールド６コード */
		String kashiUf6Cd2;
		/** ユニバーサルフィールド６名 */
		String kashiUf6Name2;
		/** ユニバーサルフィールド７コード */
		String kashiUf7Cd2;
		/** ユニバーサルフィールド７名 */
		String kashiUf7Name2;
		/** ユニバーサルフィールド８コード */
		String kashiUf8Cd2;
		/** ユニバーサルフィールド８名 */
		String kashiUf8Name2;
		/** ユニバーサルフィールド９コード */
		String kashiUf9Cd2;
		/** ユニバーサルフィールド９名 */
		String kashiUf9Name2;
		/** ユニバーサルフィールド１０コード */
		String kashiUf10Cd2;
		/** ユニバーサルフィールド１０名 */
		String kashiUf10Name2;
		/** ユニバーサルフィールド(固定値)１コード */
		String kashiUfKotei1Cd2;
		/** ユニバーサルフィールド(固定値)１名 */
		String kashiUfKotei1Name2;
		/** ユニバーサルフィールド(固定値)２コード */
		String kashiUfKotei2Cd2;
		/** ユニバーサルフィールド(固定値)２名 */
		String kashiUfKotei2Name2;
		/** ユニバーサルフィールド(固定値)３コード */
		String kashiUfKotei3Cd2;
		/** ユニバーサルフィールド(固定値)３名 */
		String kashiUfKotei3Name2;
		/** ユニバーサルフィールド(固定値)４コード */
		String kashiUfKotei4Cd2;
		/** ユニバーサルフィールド(固定値)４名 */
		String kashiUfKotei4Name2;
		/** ユニバーサルフィールド(固定値)５コード */
		String kashiUfKotei5Cd2;
		/** ユニバーサルフィールド(固定値)５名 */
		String kashiUfKotei5Name2;
		/** ユニバーサルフィールド(固定値)６コード */
		String kashiUfKotei6Cd2;
		/** ユニバーサルフィールド(固定値)６名 */
		String kashiUfKotei6Name2;
		/** ユニバーサルフィールド(固定値)７コード */
		String kashiUfKotei7Cd2;
		/** ユニバーサルフィールド(固定値)７名 */
		String kashiUfKotei7Name2;
		/** ユニバーサルフィールド(固定値)８コード */
		String kashiUfKotei8Cd2;
		/** ユニバーサルフィールド(固定値)８名 */
		String kashiUfKotei8Name2;
		/** ユニバーサルフィールド(固定値)９コード */
		String kashiUfKotei9Cd2;
		/** ユニバーサルフィールド(固定値)９名 */
		String kashiUfKotei9Name2;
		/** ユニバーサルフィールド(固定値)１０コード */
		String kashiUfKotei10Cd2;
		/** ユニバーサルフィールド(固定値)１０名 */
		String kashiUfKotei10Name2;
		/** (貸方2)旧課税区分 */
		String oldKashiKazeiKbn2;

		//貸3
		/** (貸方3)負担部門コード */
		String kashiFutanBumonCd3;
		/** (貸方3)負担部門名 */
		String kashiFutanBumonName3;
		/** (貸方3)勘定科目コード */
		String kashiKanjyouKamokuCd3;
		/** (貸方3)勘定科目名 */
		String kashiKanjyouKamokuName3;
		/** (貸方3)勘定科目枝番コード */
		String kashiKamokuEdabanCd3;
		/** (貸方3)勘定科目枝番名 */
		String kashiKamokuEdabanName3;
		/** (貸方3)取引先コード */
		String kashiTorihikisakiCd3;
		/** (貸方3)取引先名 */
		String kashiTorihikisakiName3;
		/** (貸方3)プロジェクトコード */
		String kashiProjectCd3;
		/** (貸方3)プロジェクト名 */
		String kashiProjectName3;
		/** (貸方3)セグメントコード */
		String kashiSegmentCd3;
		/** (貸方3)セグメント名 */
		String kashiSegmentName3;
		/** (貸方3)課税区分 */
		String kashiKazeiKbn3;
		/** (貸方3)分離区分 */
		String kashiBunriKbn3;
		/** (貸方3)仕入区分 */
		String kashiShiireKbn3;
		/** ユニバーサルフィールド１コード */
		String kashiUf1Cd3;
		/** ユニバーサルフィールド１名 */
		String kashiUf1Name3;
		/** ユニバーサルフィールド２コード */
		String kashiUf2Cd3;
		/** ユニバーサルフィールド２名 */
		String kashiUf2Name3;
		/** ユニバーサルフィールド３コード */
		String kashiUf3Cd3;
		/** ユニバーサルフィールド３名 */
		String kashiUf3Name3;
		/** ユニバーサルフィールド４コード */
		String kashiUf4Cd3;
		/** ユニバーサルフィールド４名 */
		String kashiUf4Name3;
		/** ユニバーサルフィールド５コード */
		String kashiUf5Cd3;
		/** ユニバーサルフィールド５名 */
		String kashiUf5Name3;
		/** ユニバーサルフィールド６コード */
		String kashiUf6Cd3;
		/** ユニバーサルフィールド６名 */
		String kashiUf6Name3;
		/** ユニバーサルフィールド７コード */
		String kashiUf7Cd3;
		/** ユニバーサルフィールド７名 */
		String kashiUf7Name3;
		/** ユニバーサルフィールド８コード */
		String kashiUf8Cd3;
		/** ユニバーサルフィールド８名 */
		String kashiUf8Name3;
		/** ユニバーサルフィールド９コード */
		String kashiUf9Cd3;
		/** ユニバーサルフィールド９名 */
		String kashiUf9Name3;
		/** ユニバーサルフィールド１０コード */
		String kashiUf10Cd3;
		/** ユニバーサルフィールド１０名 */
		String kashiUf10Name3;
		/** ユニバーサルフィールド(固定値)１コード */
		String kashiUfKotei1Cd3;
		/** ユニバーサルフィールド(固定値)１名 */
		String kashiUfKotei1Name3;
		/** ユニバーサルフィールド(固定値)２コード */
		String kashiUfKotei2Cd3;
		/** ユニバーサルフィールド(固定値)２名 */
		String kashiUfKotei2Name3;
		/** ユニバーサルフィールド(固定値)３コード */
		String kashiUfKotei3Cd3;
		/** ユニバーサルフィールド(固定値)３名 */
		String kashiUfKotei3Name3;
		/** ユニバーサルフィールド(固定値)４コード */
		String kashiUfKotei4Cd3;
		/** ユニバーサルフィールド(固定値)４名 */
		String kashiUfKotei4Name3;
		/** ユニバーサルフィールド(固定値)５コード */
		String kashiUfKotei5Cd3;
		/** ユニバーサルフィールド(固定値)５名 */
		String kashiUfKotei5Name3;
		/** ユニバーサルフィールド(固定値)６コード */
		String kashiUfKotei6Cd3;
		/** ユニバーサルフィールド(固定値)６名 */
		String kashiUfKotei6Name3;
		/** ユニバーサルフィールド(固定値)７コード */
		String kashiUfKotei7Cd3;
		/** ユニバーサルフィールド(固定値)７名 */
		String kashiUfKotei7Name3;
		/** ユニバーサルフィールド(固定値)８コード */
		String kashiUfKotei8Cd3;
		/** ユニバーサルフィールド(固定値)８名 */
		String kashiUfKotei8Name3;
		/** ユニバーサルフィールド(固定値)９コード */
		String kashiUfKotei9Cd3;
		/** ユニバーサルフィールド(固定値)９名 */
		String kashiUfKotei9Name3;
		/** ユニバーサルフィールド(固定値)１０コード */
		String kashiUfKotei10Cd3;
		/** ユニバーサルフィールド(固定値)１０名 */
		String kashiUfKotei10Name3;
		/** (貸方3)旧課税区分 */
		String oldKashiKazeiKbn3;

		//貸4
		/** (貸方4)負担部門コード */
		String kashiFutanBumonCd4;
		/** (貸方4)負担部門名 */
		String kashiFutanBumonName4;
		/** (貸方4)勘定科目コード */
		String kashiKanjyouKamokuCd4;
		/** (貸方4)勘定科目名 */
		String kashiKanjyouKamokuName4;
		/** (貸方4)勘定科目枝番コード */
		String kashiKamokuEdabanCd4;
		/** (貸方4)勘定科目枝番名 */
		String kashiKamokuEdabanName4;
		/** (貸方4)取引先コード */
		String kashiTorihikisakiCd4;
		/** (貸方4)取引先名 */
		String kashiTorihikisakiName4;
		/** (貸方4)プロジェクトコード */
		String kashiProjectCd4;
		/** (貸方4)プロジェクト名 */
		String kashiProjectName4;
		/** (貸方4)セグメントコード */
		String kashiSegmentCd4;
		/** (貸方4)セグメント名 */
		String kashiSegmentName4;
		/** (貸方4)課税区分 */
		String kashiKazeiKbn4;
		/** (貸方4)分離区分 */
		String kashiBunriKbn4;
		/** (貸方4)仕入区分 */
		String kashiShiireKbn4;
		/** ユニバーサルフィールド１コード */
		String kashiUf1Cd4;
		/** ユニバーサルフィールド１名 */
		String kashiUf1Name4;
		/** ユニバーサルフィールド２コード */
		String kashiUf2Cd4;
		/** ユニバーサルフィールド２名 */
		String kashiUf2Name4;
		/** ユニバーサルフィールド３コード */
		String kashiUf3Cd4;
		/** ユニバーサルフィールド３名 */
		String kashiUf3Name4;
		/** ユニバーサルフィールド４コード */
		String kashiUf4Cd4;
		/** ユニバーサルフィールド４名 */
		String kashiUf4Name4;
		/** ユニバーサルフィールド５コード */
		String kashiUf5Cd4;
		/** ユニバーサルフィールド５名 */
		String kashiUf5Name4;
		/** ユニバーサルフィールド６コード */
		String kashiUf6Cd4;
		/** ユニバーサルフィールド６名 */
		String kashiUf6Name4;
		/** ユニバーサルフィールド７コード */
		String kashiUf7Cd4;
		/** ユニバーサルフィールド７名 */
		String kashiUf7Name4;
		/** ユニバーサルフィールド８コード */
		String kashiUf8Cd4;
		/** ユニバーサルフィールド８名 */
		String kashiUf8Name4;
		/** ユニバーサルフィールド９コード */
		String kashiUf9Cd4;
		/** ユニバーサルフィールド９名 */
		String kashiUf9Name4;
		/** ユニバーサルフィールド１０コード */
		String kashiUf10Cd4;
		/** ユニバーサルフィールド１０名 */
		String kashiUf10Name4;
		/** ユニバーサルフィールド(固定値)１コード */
		String kashiUfKotei1Cd4;
		/** ユニバーサルフィールド(固定値)１名 */
		String kashiUfKotei1Name4;
		/** ユニバーサルフィールド(固定値)２コード */
		String kashiUfKotei2Cd4;
		/** ユニバーサルフィールド(固定値)２名 */
		String kashiUfKotei2Name4;
		/** ユニバーサルフィールド(固定値)３コード */
		String kashiUfKotei3Cd4;
		/** ユニバーサルフィールド(固定値)３名 */
		String kashiUfKotei3Name4;
		/** ユニバーサルフィールド(固定値)４コード */
		String kashiUfKotei4Cd4;
		/** ユニバーサルフィールド(固定値)４名 */
		String kashiUfKotei4Name4;
		/** ユニバーサルフィールド(固定値)５コード */
		String kashiUfKotei5Cd4;
		/** ユニバーサルフィールド(固定値)５名 */
		String kashiUfKotei5Name4;
		/** ユニバーサルフィールド(固定値)６コード */
		String kashiUfKotei6Cd4;
		/** ユニバーサルフィールド(固定値)６名 */
		String kashiUfKotei6Name4;
		/** ユニバーサルフィールド(固定値)７コード */
		String kashiUfKotei7Cd4;
		/** ユニバーサルフィールド(固定値)７名 */
		String kashiUfKotei7Name4;
		/** ユニバーサルフィールド(固定値)８コード */
		String kashiUfKotei8Cd4;
		/** ユニバーサルフィールド(固定値)８名 */
		String kashiUfKotei8Name4;
		/** ユニバーサルフィールド(固定値)９コード */
		String kashiUfKotei9Cd4;
		/** ユニバーサルフィールド(固定値)９名 */
		String kashiUfKotei9Name4;
		/** ユニバーサルフィールド(固定値)１０コード */
		String kashiUfKotei10Cd4;
		/** ユニバーサルフィールド(固定値)１０名 */
		String kashiUfKotei10Name4;
		/** (貸方4)旧課税区分 */
		String oldKashiKazeiKbn4;

		//貸5
		/** (貸方5)負担部門コード */
		String kashiFutanBumonCd5;
		/** (貸方5)負担部門名 */
		String kashiFutanBumonName5;
		/** (貸方5)勘定科目コード */
		String kashiKanjyouKamokuCd5;
		/** (貸方5)勘定科目名 */
		String kashiKanjyouKamokuName5;
		/** (貸方5)勘定科目枝番コード */
		String kashiKamokuEdabanCd5;
		/** (貸方5)勘定科目枝番名 */
		String kashiKamokuEdabanName5;
		/** (貸方5)取引先コード */
		String kashiTorihikisakiCd5;
		/** (貸方5)取引先名 */
		String kashiTorihikisakiName5;
		/** (貸方5)プロジェクトコード */
		String kashiProjectCd5;
		/** (貸方5)プロジェクト名 */
		String kashiProjectName5;
		/** (貸方5)セグメントコード */
		String kashiSegmentCd5;
		/** (貸方5)セグメント名 */
		String kashiSegmentName5;
		/** (貸方5)課税区分 */
		String kashiKazeiKbn5;
		/** (貸方5)分離区分 */
		String kashiBunriKbn5;
		/** (貸方5)仕入区分 */
		String kashiShiireKbn5;
		/** ユニバーサルフィールド１コード */
		String kashiUf1Cd5;
		/** ユニバーサルフィールド１名 */
		String kashiUf1Name5;
		/** ユニバーサルフィールド２コード */
		String kashiUf2Cd5;
		/** ユニバーサルフィールド２名 */
		String kashiUf2Name5;
		/** ユニバーサルフィールド３コード */
		String kashiUf3Cd5;
		/** ユニバーサルフィールド３名 */
		String kashiUf3Name5;
		/** ユニバーサルフィールド４コード */
		String kashiUf4Cd5;
		/** ユニバーサルフィールド４名 */
		String kashiUf4Name5;
		/** ユニバーサルフィールド５コード */
		String kashiUf5Cd5;
		/** ユニバーサルフィールド５名 */
		String kashiUf5Name5;
		/** ユニバーサルフィールド６コード */
		String kashiUf6Cd5;
		/** ユニバーサルフィールド６名 */
		String kashiUf6Name5;
		/** ユニバーサルフィールド７コード */
		String kashiUf7Cd5;
		/** ユニバーサルフィールド７名 */
		String kashiUf7Name5;
		/** ユニバーサルフィールド８コード */
		String kashiUf8Cd5;
		/** ユニバーサルフィールド８名 */
		String kashiUf8Name5;
		/** ユニバーサルフィールド９コード */
		String kashiUf9Cd5;
		/** ユニバーサルフィールド９名 */
		String kashiUf9Name5;
		/** ユニバーサルフィールド１０コード */
		String kashiUf10Cd5;
		/** ユニバーサルフィールド１０名 */
		String kashiUf10Name5;
		/** ユニバーサルフィールド(固定値)１コード */
		String kashiUfKotei1Cd5;
		/** ユニバーサルフィールド(固定値)１名 */
		String kashiUfKotei1Name5;
		/** ユニバーサルフィールド(固定値)２コード */
		String kashiUfKotei2Cd5;
		/** ユニバーサルフィールド(固定値)２名 */
		String kashiUfKotei2Name5;
		/** ユニバーサルフィールド(固定値)３コード */
		String kashiUfKotei3Cd5;
		/** ユニバーサルフィールド(固定値)３名 */
		String kashiUfKotei3Name5;
		/** ユニバーサルフィールド(固定値)４コード */
		String kashiUfKotei4Cd5;
		/** ユニバーサルフィールド(固定値)４名 */
		String kashiUfKotei4Name5;
		/** ユニバーサルフィールド(固定値)５コード */
		String kashiUfKotei5Cd5;
		/** ユニバーサルフィールド(固定値)５名 */
		String kashiUfKotei5Name5;
		/** ユニバーサルフィールド(固定値)６コード */
		String kashiUfKotei6Cd5;
		/** ユニバーサルフィールド(固定値)６名 */
		String kashiUfKotei6Name5;
		/** ユニバーサルフィールド(固定値)７コード */
		String kashiUfKotei7Cd5;
		/** ユニバーサルフィールド(固定値)７名 */
		String kashiUfKotei7Name5;
		/** ユニバーサルフィールド(固定値)８コード */
		String kashiUfKotei8Cd5;
		/** ユニバーサルフィールド(固定値)８名 */
		String kashiUfKotei8Name5;
		/** ユニバーサルフィールド(固定値)９コード */
		String kashiUfKotei9Cd5;
		/** ユニバーサルフィールド(固定値)９名 */
		String kashiUfKotei9Name5;
		/** ユニバーサルフィールド(固定値)１０コード */
		String kashiUfKotei10Cd5;
		/** ユニバーサルフィールド(固定値)１０名 */
		String kashiUfKotei10Name5;
		/** (貸方5)旧課税区分 */
		String oldKashiKazeiKbn5;
		
		//隠し項目（パラメータ）
		/** イベントURL(遷移元画面) */
		String preEventUrl;
		/** 伝票区分 */
		String denpyouKbn;

		//設定により仕訳枝番を入力する
		/** 仕訳枝番号 */
		String shiwakeEdano;
		
		/** 削除フラグ */
		String deleteFlg;

		/** 外部呼出し制御 */
		boolean gaibuYobidashiFlag=false;
		/** 外部呼出しコネクション */
		EteamConnection gaibuConnection;
		/** 外部呼出しユーザーId */
		String gaibuKoushinUserId;

	//＜画面入力以外＞
		/** 課税区分のDropDownList */
		List<NaibuCdSetting> kazeiKbnList;
		/** 消費税率のDropDownList */
		List<GMap> zeiritsuList;
		/** 分離区分DropDownList */
		List<NaibuCdSetting> bunriKbnList;
		/** 仕入区分DropDownList */
		List<NaibuCdSetting> shiireKbnList;
		/** 入力制御 */
		ShiwakePatternInputSeigyo inputSeigyo;
		/** 仕入税按分 */
		int shiireZeiAnbun;
		/** 社員コード連携有無 */
		boolean shainCdRenkeiUmu = !"0".equals(setting.shainCdRenkei());
		/** 社員コード連携エリア */
		String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();


	//画面制御情報
		/** HF・UF制御クラス */
		HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
		/** 画面項目制御クラス*/
		GamenKoumokuSeigyo ks;

	//＜部品＞
		/** コネクション */
		EteamConnection connection;
		/** システム管理　SELECT */
		SystemKanriCategoryLogic systemLogic;
		/** マスター管理　SELECT */
		MasterKanriCategoryLogic kanriCategoryLogic;
		/** 会計　SELECT */
		KaikeiCategoryLogic kaikeiLogic;
		/** 会計共通 */
		KaikeiCommonLogic commonLogic;
		/** 取引系 */
		TorihikiLogic myLogic;
		/** 仕訳パターンマスターDAO */
		ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
		/** 科目マスターDAO */
		KamokuMasterAbstractDao kamokuMasterDao;
		/** 内部コード設定Dao */
		NaibuCdSettingAbstractDao naibuCdSettingDao;
		/** 税率Dao */
		ShouhizeiritsuAbstractDao zeiritsuDao;
		/** 仕訳パターン設定DAO */
		ShiwakePatternSettingAbstractDao shiwakePatternSettingDao;
		/** 科目枝番残高Dao */
		KamokuEdabanZandakaAbstractDao kamokuEdabanZandakaDao;
		/** 部門マスターDao */
		BumonMasterAbstractDao bumonMasterDao;
		/** インボイス処理開始フラグ */
		InvoiceStartAbstractDao invoiceStartDao;
		/** 消費税設定Dao */
		KiShouhizeiSettingDao kiShouhizeiSettingDao;
		
		/** 取引コード(仕訳枝番)表示可否 */
		boolean shiwakeEdanoEnabled;
		
		/** 仕訳枝番編集可否 */
		boolean shiwakeEdanoEditable;
		
	@Override
	protected void formatCheck() {
		//取引（仕訳）内容の入力チェック
		checkDate(yuukouKigenFrom, "有効期限開始日", false);
		checkDate(yuukouKigenTo, "有効期限終了日", false);
		checkString(bunrui1, 1, 20, "分類1", false);
		checkString(bunrui2, 1, 20, "分類2", false);
		checkString(bunrui3, 1, 20, "分類3", false);
		checkString(torihikiNm, 1, 20, "取引名", false);
		checkSJIS(torihikiNm, "取引名");
		checkDomain(tekiyouNyuryokuOn, Domain.FLG, "摘要入力フラグ", false);
		checkString(tekiyou, 1, 20, "摘要", false);
		checkDomain(defaultHyoujiOn, Domain.FLG, "デフォルト表示", false);
		checkDomain(kousaihiOn, Domain.FLG, "交際費", false);
		checkDomain(ninzuuOn, Domain.FLG, "人数項目", false);
		String[] kousaihiCheckHouhouDomain = {"0","1","2"};
		String[] kousaihiCheckResultDomain = {"0","1"};
		checkKingaku(kousaihiKijungaku, 0, 999999, "交際費基準額"	, false);
		checkDomain(kousaihiCheckHouhou, kousaihiCheckHouhouDomain, "交際費チェック方法", false);
		checkDomain(kousaihiCheckResult, kousaihiCheckResultDomain, "交際費チェック時の処理", false);
		checkDomain(kakeOn, Domain.FLG, "掛け", false);
		checkNumber(hyoujijun, 1, 4, "表示順", false);
		checkDomain(shainCdRenkei, Domain.FLG, "社員コード連携", false);
		checkDomain(zaimuEdabanRenkei, Domain.FLG, "財務枝番コード連携", false);
		checkDomain(kariKeigenZeiritsuKbn, Domain.FLG, "軽減税率区分（借方）", false);
		
		// UF
		// 借方
		String[] kariUfCds = {
			    kariUf1Cd, kariUf2Cd, kariUf3Cd, kariUf4Cd, kariUf5Cd, kariUf6Cd, kariUf7Cd, kariUf8Cd, kariUf9Cd, kariUf10Cd,
			    kariUfKotei1Cd, kariUfKotei2Cd, kariUfKotei3Cd, kariUfKotei4Cd, kariUfKotei5Cd, kariUfKotei6Cd, kariUfKotei7Cd, kariUfKotei8Cd, kariUfKotei9Cd, kariUfKotei10Cd
			};
		String[] kariUfNames = {
			    kariUf1Name, kariUf2Name, kariUf3Name, kariUf4Name, kariUf5Name, kariUf6Name, kariUf7Name, kariUf8Name, kariUf9Name, kariUf10Name,
			    kariUfKotei1Name, kariUfKotei2Name, kariUfKotei3Name, kariUfKotei4Name, kariUfKotei5Name, kariUfKotei6Name, kariUfKotei7Name, kariUfKotei8Name, kariUfKotei9Name, kariUfKotei10Name
			};
		this.checkUf(kariUfCds, kariUfNames, "（借方）");
		
		// 貸方1
		String[] kashiUfCds1 = {
			    kashiUf1Cd1, kashiUf2Cd1, kashiUf3Cd1, kashiUf4Cd1, kashiUf5Cd1, kashiUf6Cd1, kashiUf7Cd1, kashiUf8Cd1, kashiUf9Cd1, kashiUf10Cd1,
			    kashiUfKotei1Cd1, kashiUfKotei2Cd1, kashiUfKotei3Cd1, kashiUfKotei4Cd1, kashiUfKotei5Cd1, kashiUfKotei6Cd1, kashiUfKotei7Cd1, kashiUfKotei8Cd1, kashiUfKotei9Cd1, kashiUfKotei10Cd1
			};
		String[] kashiUfNames1 = {
			    kashiUf1Name1, kashiUf2Name1, kashiUf3Name1, kashiUf4Name1, kashiUf5Name1, kashiUf6Name1, kashiUf7Name1, kashiUf8Name1, kashiUf9Name1, kashiUf10Name1,
			    kashiUfKotei1Name1, kashiUfKotei2Name1, kashiUfKotei3Name1, kashiUfKotei4Name1, kashiUfKotei5Name1, kashiUfKotei6Name1, kashiUfKotei7Name1, kashiUfKotei8Name1, kashiUfKotei9Name1, kashiUfKotei10Name1
			};
		this.checkUf(kashiUfCds1, kashiUfNames1, "（貸方1）");
		
		// 貸方2
		String[] kashiUfCds2 = {
			    kashiUf1Cd2, kashiUf2Cd2, kashiUf3Cd2, kashiUf4Cd2, kashiUf5Cd2, kashiUf6Cd2, kashiUf7Cd2, kashiUf8Cd2, kashiUf9Cd2, kashiUf10Cd2,
			    kashiUfKotei1Cd2, kashiUfKotei2Cd2, kashiUfKotei3Cd2, kashiUfKotei4Cd2, kashiUfKotei5Cd2, kashiUfKotei6Cd2, kashiUfKotei7Cd2, kashiUfKotei8Cd2, kashiUfKotei9Cd2, kashiUfKotei10Cd2
			};
		String[] kashiUfNames2 = {
			    kashiUf1Name2, kashiUf2Name2, kashiUf3Name2, kashiUf4Name2, kashiUf5Name2, kashiUf6Name2, kashiUf7Name2, kashiUf8Name2, kashiUf9Name2, kashiUf10Name2,
			    kashiUfKotei1Name2, kashiUfKotei2Name2, kashiUfKotei3Name2, kashiUfKotei4Name2, kashiUfKotei5Name2, kashiUfKotei6Name2, kashiUfKotei7Name2, kashiUfKotei8Name2, kashiUfKotei9Name2, kashiUfKotei10Name2
			};
		this.checkUf(kashiUfCds2, kashiUfNames2, "（貸方2）");

		// 貸方3
		String[] kashiUfCds3 = {
			    kashiUf1Cd3, kashiUf2Cd3, kashiUf3Cd3, kashiUf4Cd3, kashiUf5Cd3, kashiUf6Cd3, kashiUf7Cd3, kashiUf8Cd3, kashiUf9Cd3, kashiUf10Cd3,
			    kashiUfKotei1Cd3, kashiUfKotei2Cd3, kashiUfKotei3Cd3, kashiUfKotei4Cd3, kashiUfKotei5Cd3, kashiUfKotei6Cd3, kashiUfKotei7Cd3, kashiUfKotei8Cd3, kashiUfKotei9Cd3, kashiUfKotei10Cd3
			};
		String[] kashiUfNames3 = {
			    kashiUf1Name3, kashiUf2Name3, kashiUf3Name3, kashiUf4Name3, kashiUf5Name3, kashiUf6Name3, kashiUf7Name3, kashiUf8Name3, kashiUf9Name3, kashiUf10Name3,
			    kashiUfKotei1Name3, kashiUfKotei2Name3, kashiUfKotei3Name3, kashiUfKotei4Name3, kashiUfKotei5Name3, kashiUfKotei6Name3, kashiUfKotei7Name3, kashiUfKotei8Name3, kashiUfKotei9Name3, kashiUfKotei10Name3
			};
		this.checkUf(kashiUfCds3, kashiUfNames3, "（貸方3）");
		
		// 貸方4
		String[] kashiUfCds4 = {
			    kashiUf1Cd4, kashiUf2Cd4, kashiUf3Cd4, kashiUf4Cd4, kashiUf5Cd4, kashiUf6Cd4, kashiUf7Cd4, kashiUf8Cd4, kashiUf9Cd4, kashiUf10Cd4,
			    kashiUfKotei1Cd4, kashiUfKotei2Cd4, kashiUfKotei3Cd4, kashiUfKotei4Cd4, kashiUfKotei5Cd4, kashiUfKotei6Cd4, kashiUfKotei7Cd4, kashiUfKotei8Cd4, kashiUfKotei9Cd4, kashiUfKotei10Cd4
			};
		String[] kashiUfNames4 = {
			    kashiUf1Name4, kashiUf2Name4, kashiUf3Name4, kashiUf4Name4, kashiUf5Name4, kashiUf6Name4, kashiUf7Name4, kashiUf8Name4, kashiUf9Name4, kashiUf10Name4,
			    kashiUfKotei1Name4, kashiUfKotei2Name4, kashiUfKotei3Name4, kashiUfKotei4Name4, kashiUfKotei5Name4, kashiUfKotei6Name4, kashiUfKotei7Name4, kashiUfKotei8Name4, kashiUfKotei9Name4, kashiUfKotei10Name4
			};
		this.checkUf(kashiUfCds4, kashiUfNames4, "（貸方4）");
		
		// 貸方5
		String[] kashiUfCds5 = {
			    kashiUf1Cd5, kashiUf2Cd5, kashiUf3Cd5, kashiUf4Cd5, kashiUf5Cd5, kashiUf6Cd5, kashiUf7Cd5, kashiUf8Cd5, kashiUf9Cd5, kashiUf10Cd5,
			    kashiUfKotei1Cd5, kashiUfKotei2Cd5, kashiUfKotei3Cd5, kashiUfKotei4Cd5, kashiUfKotei5Cd5, kashiUfKotei6Cd5, kashiUfKotei7Cd5, kashiUfKotei8Cd5, kashiUfKotei9Cd5, kashiUfKotei10Cd5
			};
		String[] kashiUfNames5 = {
			    kashiUf1Name5, kashiUf2Name5, kashiUf3Name5, kashiUf4Name5, kashiUf5Name5, kashiUf6Name5, kashiUf7Name5, kashiUf8Name5, kashiUf9Name5, kashiUf10Name5,
			    kashiUfKotei1Name5, kashiUfKotei2Name5, kashiUfKotei3Name5, kashiUfKotei4Name5, kashiUfKotei5Name5, kashiUfKotei6Name5, kashiUfKotei7Name5, kashiUfKotei8Name5, kashiUfKotei9Name5, kashiUfKotei10Name5
			};
		this.checkUf(kashiUfCds5, kashiUfNames5, "（貸方5）");

		//非表示項目(ﾒﾝﾊﾞｰ変数)の入力チェック
		checkString(denpyouKbn, 1, 4, "伝票区分", true);
		checkHankakuEiSuu(denpyouKbn, "伝票区分");
	}
	
	/**
	 * Ufのチェック
	 * @param ufCds ufコード
	 * @param ufNames uf名
	 * @param taishaku 貸借（1～5）
	 */
	protected void checkUf(String[] ufCds, String[] ufNames, String taishaku)
	{
		String[] ufSeigyoName = {
				hfUfSeigyo.getUf1Name(), hfUfSeigyo.getUf2Name(), hfUfSeigyo.getUf3Name(), hfUfSeigyo.getUf4Name(), hfUfSeigyo.getUf5Name(), hfUfSeigyo.getUf6Name(), hfUfSeigyo.getUf7Name(), hfUfSeigyo.getUf8Name(), hfUfSeigyo.getUf9Name(), hfUfSeigyo.getUf10Name(),
				hfUfSeigyo.getUfKotei1Name(), hfUfSeigyo.getUfKotei2Name(), hfUfSeigyo.getUfKotei3Name(), hfUfSeigyo.getUfKotei4Name(), hfUfSeigyo.getUfKotei5Name(), hfUfSeigyo.getUfKotei6Name(), hfUfSeigyo.getUfKotei7Name(), hfUfSeigyo.getUfKotei8Name(), hfUfSeigyo.getUfKotei9Name(), hfUfSeigyo.getUfKotei10Name()
		};
		for(int i = 0; i < ufSeigyoName.length; i++)
		{
			checkString(ufCds[i], 1, 20, ufSeigyoName[i] + "コード" + taishaku, false);
			checkString(ufNames[i], 1, 20, ufSeigyoName[i] + "名" + taishaku, false);
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目 EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{yuukouKigenFrom, "有効期限開始日", "0", "1", "1", },
				{torihikiNm, "取引名", "0", "1", "1", },
				{tekiyouNyuryokuOn, "摘要入力フラグ", "0", "1", "1", },
				{hyoujijun, "表示順", "0", "1", "1", },
				{preEventUrl, "イベントURL(遷移元画面)", "2", "2", "0", },
				{denpyouKbn, "伝票区分", "2", "2", "1", },
			};
			hissuCheckCommon(list, eventNum);
			if (1 == eventNum)
			{
				return;
			}
			//項目 EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			List<String[]> listOpt = new ArrayList<>();
			if (inputSeigyo.defaultHyouji) {
				listOpt.add(new String[]{defaultHyoujiOn, "デフォルト表示", "0", "1", "1"});
			}
			if (inputSeigyo.kousaihiHyouji) {
				listOpt.add(new String[]{kousaihiOn, "交際費", "0", "1", "1"});
			}
			if (inputSeigyo.kousaihiHyouji) {
				listOpt.add(new String[]{ninzuuOn, "人数項目", "0", "1", "1"});
			}
			if (inputSeigyo.kousaihiHyouji) {
				listOpt.add(new String[]{kousaihiKijungaku, "交際費基準額", "0", "1", "1"});
			}
			if (inputSeigyo.kousaihiHyouji) {
				listOpt.add(new String[]{kousaihiCheckHouhou, "交際費チェック方法", "0", "1", "1"});
			}
			if (inputSeigyo.kousaihiHyouji) {
				listOpt.add(new String[]{kousaihiCheckResult, "交際費チェック時の処理", "0", "1", "1"});
			}
			if (inputSeigyo.kakeHyouji) {
				listOpt.add(new String[]{kakeOn, "掛け", "0", "1", "1"});
			}
			if (inputSeigyo.shainCdRenkeiHyouji) {
				listOpt.add(new String[]{shainCdRenkei, "社員コード連携", "0", "1", "1"});
			}
			if (inputSeigyo.zaimuEdabanRenkeiHyouji) {
				listOpt.add(new String[]{zaimuEdabanRenkei, "財務枝番コード連携", "0", "1", "1"});
			}
			if (inputSeigyo.kariKamoku) {
				listOpt.add(new String[]{kariKanjyouKamokuCd, "勘定科目コード（借方）", "0", "1", "1"});
			}
			if (inputSeigyo.kashi1Kamoku) {
				listOpt.add(new String[]{kashiKanjyouKamokuCd1, "勘定科目コード（貸方１）", "0", "1", "1"});
			}

			//例外項目
			//請求書払（掛けなし）は貸方２入力なし※ここだけはshiwake_pattern_settingの制御外
		if (!((DENPYOU_KBN.SEIKYUUSHO_BARAI.equals(denpyouKbn) && "0".equals(kakeOn)))) {
			if (inputSeigyo.kashi2Kamoku) {
				listOpt.add(new String[]{kashiKanjyouKamokuCd2, "勘定科目コード（貸方２）", "0", "1", "1"});
			}
		}
			if (inputSeigyo.kashi3Kamoku) {
				listOpt.add(new String[]{kashiKanjyouKamokuCd3, "勘定科目コード（貸方３）", "0", "1", "1"});
			}
			if (inputSeigyo.kashi4Kamoku) {
				listOpt.add(new String[]{kashiKanjyouKamokuCd4, "勘定科目コード（貸方４）", "0", "1", "1"});
			}
			if (inputSeigyo.kashi5Kamoku) {
				listOpt.add(new String[]{kashiKanjyouKamokuCd5, "勘定科目コード（貸方５）", "0", "1", "1"});
			}

			hissuCheckCommon(listOpt.toArray(new String[0][]), eventNum);
	}

	/**
	 * 部品初期化
	 */
	protected void initParts() {
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		kanriCategoryLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		myLogic = EteamContainer.getComponent(TorihikiLogic.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		this.shiwakePatternSettingDao = EteamContainer.getComponent(ShiwakePatternSettingDao.class, connection);
		this.kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		this.kamokuEdabanZandakaDao = EteamContainer.getComponent(KamokuEdabanZandakaDao.class, connection);
		this.bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);
		this.invoiceStartDao = EteamContainer.getComponent(InvoiceStartDao.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
	}
		
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 */
	protected void displaySeigyo() {
		//伝票種別
		NaibuCdSetting naibuCdMap = this.naibuCdSettingDao.find("shiwake_pattern_denpyou_kbn", denpyouKbn);
		denpyouShubetsu = naibuCdMap.name;

		//リスト
		kazeiKbnList = this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn"); // 支払依頼専用区分は重複に付き不要
		zeiritsuList = this.zeiritsuDao.load().stream().map(Shouhizeiritsu::getMap).collect(Collectors.toList());
		zeiritsuList.stream().forEach(m -> {
			String tmp = m.get("zeiritsu").toString();
			m.put("zeiritsu", tmp);
			});
		bunriKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn");
		shiireKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn");

		//入力可否制御
		inputSeigyo = myLogic.inputSeigyo(denpyouKbn);
		
		//TODO 期別消費税設定の取得 基準とする日がどれかわからないので、とりあえず最新を取れるようにnull
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		shiireZeiAnbun = dto.shiireZeigakuAnbunFlg;//仕入区分のenable/非表示の按分法

		String denpyouKbnTmp = denpyouKbn.equals("A901") ? DENPYOU_KBN.KEIHI_TATEKAE_SEISAN :denpyouKbn;
		ks = new GamenKoumokuSeigyo(denpyouKbnTmp);
	}

	/**
	 * 借方、貸方の各非表示項目にディフォール値を設定
	 */
	protected void setHiHyoujiItemByBlank() {
		if (! inputSeigyo.defaultHyouji)
		{
			defaultHyoujiOn = "1";
		}
		if (! inputSeigyo.kousaihiHyouji)
		{
			kousaihiOn = "";
		}
		if (! inputSeigyo.kousaihiHyouji)
		{
			ninzuuOn = "";
		}
		if (! inputSeigyo.kousaihiHyouji)
		{
			kousaihiKijungaku = "";
		}
		if (! inputSeigyo.kousaihiHyouji)
		{
			kousaihiCheckHouhou = "";
		}
		if (! inputSeigyo.kousaihiHyouji)
		{
			kousaihiCheckResult = "";
		}
		if (! inputSeigyo.kakeHyouji)
		{
			kakeOn = "";
		}
		if (! inputSeigyo.shainCdRenkeiHyouji)
		{
			shainCdRenkei = SHAIN_CD_RENKEI.NASHI;
		}
		if (! inputSeigyo.zaimuEdabanRenkeiHyouji)
		{
			zaimuEdabanRenkei = "";
		}

		// 借方
		if (! inputSeigyo.kariBumon)
		{
			kariFutanBumonCd = "";
		}
		if (! inputSeigyo.kariKamoku)
		{
			kariKanjyouKamokuCd = "";
		}
		if (! inputSeigyo.kariKamokuEda)
		{
			kariKamokuEdabanCd = "";
		}
		if (! inputSeigyo.kariTorihiki)
		{
			kariTorihikisakiCd = "";
		}
		if (! inputSeigyo.kariProject)
		{
			kariProjectCd = "";
		}
		if (! inputSeigyo.kariSegment)
		{
			kariSegmentCd = "";
		}
		if (! inputSeigyo.kariUf1)
		{
			kariUf1Cd = "";
		}
		if (! inputSeigyo.kariUf2)
		{
			kariUf2Cd = "";
		}
		if (! inputSeigyo.kariUf3)
		{
			kariUf3Cd = "";
		}
		if (! inputSeigyo.kariUf4)
		{
			kariUf4Cd = "";
		}
		if (! inputSeigyo.kariUf5)
		{
			kariUf5Cd = "";
		}
		if (! inputSeigyo.kariUf6)
		{
			kariUf6Cd = "";
		}
		if (! inputSeigyo.kariUf7)
		{
			kariUf7Cd = "";
		}
		if (! inputSeigyo.kariUf8)
		{
			kariUf8Cd = "";
		}
		if (! inputSeigyo.kariUf9)
		{
			kariUf9Cd = "";
		}
		if (! inputSeigyo.kariUf10)
		{
			kariUf10Cd = "";
		}
		if (! inputSeigyo.kariUfKotei1)
		{
			kariUfKotei1Cd = "";
		}
		if (! inputSeigyo.kariUfKotei2)
		{
			kariUfKotei2Cd = "";
		}
		if (! inputSeigyo.kariUfKotei3)
		{
			kariUfKotei3Cd = "";
		}
		if (! inputSeigyo.kariUfKotei4)
		{
			kariUfKotei4Cd = "";
		}
		if (! inputSeigyo.kariUfKotei5)
		{
			kariUfKotei5Cd = "";
		}
		if (! inputSeigyo.kariUfKotei6)
		{
			kariUfKotei6Cd = "";
		}
		if (! inputSeigyo.kariUfKotei7)
		{
			kariUfKotei7Cd = "";
		}
		if (! inputSeigyo.kariUfKotei8)
		{
			kariUfKotei8Cd = "";
		}
		if (! inputSeigyo.kariUfKotei9)
		{
			kariUfKotei9Cd = "";
		}
		if (! inputSeigyo.kariUfKotei10)
		{
			kariUfKotei10Cd = "";
		}
		if (! inputSeigyo.kariKazeiKbn)
		{
			kariKazeiKbn = "";
		}
		if (! inputSeigyo.kariZeiritsu) {
											kariZeiritsu = "";
											kariKeigenZeiritsuKbn = "";
		}
		if (! inputSeigyo.kariBunriKbn)
		{
			kariBunriKbn = "";
		}
		if (! inputSeigyo.kariShiireKbn)
		{
			kariShiireKbn = "";
		}

		// 貸方１
		if (! inputSeigyo.kashi1Bumon)
		{
			kashiFutanBumonCd1 = "";
		}
		if (! inputSeigyo.kashi1Kamoku)
		{
			kashiKanjyouKamokuCd1 = "";
		}
		if (! inputSeigyo.kashi1KamokuEda)
		{
			kashiKamokuEdabanCd1 = "";
		}
		if (! inputSeigyo.kashi1Torihiki)
		{
			kashiTorihikisakiCd1 = "";
		}
		if (! inputSeigyo.kashi1Project)
		{
			kashiProjectCd1 = "";
		}
		if (! inputSeigyo.kashi1Segment)
		{
			kashiSegmentCd1 = "";
		}
		if (! inputSeigyo.kashi1Uf1)
		{
			kashiUf1Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf2)
		{
			kashiUf2Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf3)
		{
			kashiUf3Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf4)
		{
			kashiUf4Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf5)
		{
			kashiUf5Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf6)
		{
			kashiUf6Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf7)
		{
			kashiUf7Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf8)
		{
			kashiUf8Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf9)
		{
			kashiUf9Cd1 = "";
		}
		if (! inputSeigyo.kashi1Uf10)
		{
			kashiUf10Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei1)
		{
			kashiUfKotei1Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei2)
		{
			kashiUfKotei2Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei3)
		{
			kashiUfKotei3Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei4)
		{
			kashiUfKotei4Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei5)
		{
			kashiUfKotei5Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei6)
		{
			kashiUfKotei6Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei7)
		{
			kashiUfKotei7Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei8)
		{
			kashiUfKotei8Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei9)
		{
			kashiUfKotei9Cd1 = "";
		}
		if (! inputSeigyo.kashi1UfKotei10)
		{
			kashiUfKotei10Cd1 = "";
		}
		if (! inputSeigyo.kashi1KazeiKbn)
		{
			kashiKazeiKbn1 = "";
		}
		if (! inputSeigyo.kashi1BunriKbn)
		{
			kashiBunriKbn1 = "";
		}
		if (! inputSeigyo.kashi1ShiireKbn)
		{
			kashiShiireKbn1 = "";
		}

		// 貸方２
		if (! inputSeigyo.kashi2Bumon)
		{
			kashiFutanBumonCd2 = "";
		}
		if (! inputSeigyo.kashi2Kamoku)
		{
			kashiKanjyouKamokuCd2 = "";
		}
		if (! inputSeigyo.kashi2KamokuEda)
		{
			kashiKamokuEdabanCd2 = "";
		}
		if (! inputSeigyo.kashi2Torihiki)
		{
			kashiTorihikisakiCd2 = "";
		}
		if (! inputSeigyo.kashi2Project)
		{
			kashiProjectCd2 = "";
		}
		if (! inputSeigyo.kashi2Segment)
		{
			kashiSegmentCd2 = "";
		}
		if (! inputSeigyo.kashi2Uf1)
		{
			kashiUf1Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf2)
		{
			kashiUf2Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf3)
		{
			kashiUf3Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf4)
		{
			kashiUf4Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf5)
		{
			kashiUf5Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf6)
		{
			kashiUf6Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf7)
		{
			kashiUf7Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf8)
		{
			kashiUf8Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf9)
		{
			kashiUf9Cd2 = "";
		}
		if (! inputSeigyo.kashi2Uf10)
		{
			kashiUf10Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei1)
		{
			kashiUfKotei1Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei2)
		{
			kashiUfKotei2Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei3)
		{
			kashiUfKotei3Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei4)
		{
			kashiUfKotei4Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei5)
		{
			kashiUfKotei5Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei6)
		{
			kashiUfKotei6Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei7)
		{
			kashiUfKotei7Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei8)
		{
			kashiUfKotei8Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei9)
		{
			kashiUfKotei9Cd2 = "";
		}
		if (! inputSeigyo.kashi2UfKotei10)
		{
			kashiUfKotei10Cd2 = "";
		}
		if (! inputSeigyo.kashi2KazeiKbn)
		{
			kashiKazeiKbn2 = "";
		}
		if (! inputSeigyo.kashi2BunriKbn)
		{
			kashiBunriKbn2 = "";
		}
		if (! inputSeigyo.kashi2ShiireKbn)
		{
			kashiShiireKbn2 = "";
		}

		// 貸方３
		if (! inputSeigyo.kashi3Bumon)
		{
			kashiFutanBumonCd3 = "";
		}
		if (! inputSeigyo.kashi3Kamoku)
		{
			kashiKanjyouKamokuCd3 = "";
		}
		if (! inputSeigyo.kashi3KamokuEda)
		{
			kashiKamokuEdabanCd3 = "";
		}
		if (! inputSeigyo.kashi3Torihiki)
		{
			kashiTorihikisakiCd3 = "";
		}
		if (! inputSeigyo.kashi3Project)
		{
			kashiProjectCd3 = "";
		}
		if (! inputSeigyo.kashi3Segment)
		{
			kashiSegmentCd3 = "";
		}
		if (! inputSeigyo.kashi3Uf1)
		{
			kashiUf1Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf2)
		{
			kashiUf2Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf3)
		{
			kashiUf3Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf4)
		{
			kashiUf4Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf5)
		{
			kashiUf5Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf6)
		{
			kashiUf6Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf7)
		{
			kashiUf7Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf8)
		{
			kashiUf8Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf9)
		{
			kashiUf9Cd3 = "";
		}
		if (! inputSeigyo.kashi3Uf10)
		{
			kashiUf10Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei1)
		{
			kashiUfKotei1Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei2)
		{
			kashiUfKotei2Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei3)
		{
			kashiUfKotei3Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei4)
		{
			kashiUfKotei4Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei5)
		{
			kashiUfKotei5Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei6)
		{
			kashiUfKotei6Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei7)
		{
			kashiUfKotei7Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei8)
		{
			kashiUfKotei8Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei9)
		{
			kashiUfKotei9Cd3 = "";
		}
		if (! inputSeigyo.kashi3UfKotei10)
		{
			kashiUfKotei10Cd3 = "";
		}
		if (! inputSeigyo.kashi3KazeiKbn)
		{
			kashiKazeiKbn3 = "";
		}
		if (! inputSeigyo.kashi3BunriKbn)
		{
			kashiBunriKbn3 = "";
		}
		if (! inputSeigyo.kashi3ShiireKbn)
		{
			kashiShiireKbn3 = "";
		}

		// 貸方４
		if (! inputSeigyo.kashi4Bumon)
		{
			kashiFutanBumonCd4 = "";
		}
		if (! inputSeigyo.kashi4Kamoku)
		{
			kashiKanjyouKamokuCd4 = "";
		}
		if (! inputSeigyo.kashi4KamokuEda)
		{
			kashiKamokuEdabanCd4 = "";
		}
		if (! inputSeigyo.kashi4Torihiki)
		{
			kashiTorihikisakiCd4 = "";
		}
		if (! inputSeigyo.kashi4Project)
		{
			kashiProjectCd4 = "";
		}
		if (! inputSeigyo.kashi4Segment)
		{
			kashiSegmentCd4 = "";
		}
		if (! inputSeigyo.kashi4Uf1)
		{
			kashiUf1Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf2)
		{
			kashiUf2Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf3)
		{
			kashiUf3Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf4)
		{
			kashiUf4Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf5)
		{
			kashiUf5Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf6)
		{
			kashiUf6Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf7)
		{
			kashiUf7Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf8)
		{
			kashiUf8Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf9)
		{
			kashiUf9Cd4 = "";
		}
		if (! inputSeigyo.kashi4Uf10)
		{
			kashiUf10Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei1)
		{
			kashiUfKotei1Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei2)
		{
			kashiUfKotei2Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei3)
		{
			kashiUfKotei3Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei4)
		{
			kashiUfKotei4Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei5)
		{
			kashiUfKotei5Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei6)
		{
			kashiUfKotei6Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei7)
		{
			kashiUfKotei7Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei8)
		{
			kashiUfKotei8Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei9)
		{
			kashiUfKotei9Cd4 = "";
		}
		if (! inputSeigyo.kashi4UfKotei10)
		{
			kashiUfKotei10Cd4 = "";
		}
		if (! inputSeigyo.kashi4KazeiKbn)
		{
			kashiKazeiKbn4 = "";
		}
		if (! inputSeigyo.kashi4BunriKbn)
		{
			kashiBunriKbn4 = "";
		}
		if (! inputSeigyo.kashi4ShiireKbn)
		{
			kashiShiireKbn4 = "";
		}

		// 貸方５
		if (! inputSeigyo.kashi5Bumon)
		{
			kashiFutanBumonCd5 = "";
		}
		if (! inputSeigyo.kashi5Kamoku)
		{
			kashiKanjyouKamokuCd5 = "";
		}
		if (! inputSeigyo.kashi5KamokuEda)
		{
			kashiKamokuEdabanCd5 = "";
		}
		if (! inputSeigyo.kashi5Torihiki)
		{
			kashiTorihikisakiCd5 = "";
		}
		if (! inputSeigyo.kashi5Project)
		{
			kashiProjectCd5 = "";
		}
		if (! inputSeigyo.kashi5Segment)
		{
			kashiSegmentCd5 = "";
		}
		if (! inputSeigyo.kashi5Uf1)
		{
			kashiUf1Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf2)
		{
			kashiUf2Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf3)
		{
			kashiUf3Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf4)
		{
			kashiUf4Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf5)
		{
			kashiUf5Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf6)
		{
			kashiUf6Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf7)
		{
			kashiUf7Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf8)
		{
			kashiUf8Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf9)
		{
			kashiUf9Cd5 = "";
		}
		if (! inputSeigyo.kashi5Uf10)
		{
			kashiUf10Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei1)
		{
			kashiUfKotei1Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei2)
		{
			kashiUfKotei2Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei3)
		{
			kashiUfKotei3Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei4)
		{
			kashiUfKotei4Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei5)
		{
			kashiUfKotei5Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei6)
		{
			kashiUfKotei6Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei7)
		{
			kashiUfKotei7Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei8)
		{
			kashiUfKotei8Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei9)
		{
			kashiUfKotei9Cd5 = "";
		}
		if (! inputSeigyo.kashi5UfKotei10)
		{
			kashiUfKotei10Cd5 = "";
		}
		if (! inputSeigyo.kashi5KazeiKbn)
		{
			kashiKazeiKbn5 = "";
		}
		if (! inputSeigyo.kashi5BunriKbn)
		{
			kashiBunriKbn5 = "";
		}
		if (! inputSeigyo.kashi5ShiireKbn)
		{
			kashiShiireKbn5 = "";
		}

		// 請求書払（掛けなし）は貸方２不要※ここだけはshiwake_pattern_settingの制御外
		if (DENPYOU_KBN.SEIKYUUSHO_BARAI.equals(denpyouKbn) && "0".equals(kakeOn)) {
			kashiFutanBumonCd2 = "";
			kashiKanjyouKamokuCd2 = "";
			kashiKamokuEdabanCd2 = "";
			kashiTorihikisakiCd2 = "";
			kashiProjectCd2 = "";
			kashiSegmentCd2 = "";
			kashiKazeiKbn2 = "";
			kashiBunriKbn2 = "";
			kashiShiireKbn2 = "";
		}
	}

	/**
	 * 項目間チェック
	 */
	protected void soukanCheck() {

		//----------
		//有効期限の期限チェック（参照：共通機能（期限チェック））
		//----------
		for(Map<String, String> errMap: EteamCommon.kigenCheck(yuukouKigenFrom, yuukouKigenTo)) {
			// エラーコードが「2」の場合にエラーメッセージを追加します。
			if ("2".equals(errMap.get("errorCode"))) {
				errorList.add(errMap.get("errorMassage"));
			}
		}

		//----------
		//分類の飛び入力チェック
		//----------
		if(
			(isEmpty(bunrui1) && (isNotEmpty(bunrui2) || isNotEmpty(bunrui3))) ||
			(isEmpty(bunrui2) && isNotEmpty(bunrui3))
		){
			errorList.add("分類は飛び入力できません。");
		}

		// 借方区分各種・消費税率
		// この業務ではsoukanCheckはhissuCheckより後でしか呼ばれないので、科目コードが空かのチェックは無意味
		var kamoku = kamokuMasterDao.find(kariKanjyouKamokuCd);
		
		// 科目dtoがnullの場合はありうるので、その場合の対策
		// 科目dtoがnullの場合、処理グループなどを持てないので区分チェック自体が無意味
		if(kamoku != null) {
			// 処理グループ
			this.kariShoriGroup = kamoku.shoriGroup;
			var kamokuName = kamoku.kamokuNameRyakushiki;

			// 消費税設定
			KiShouhizeiSetting kiShouhizeiSetting = kiShouhizeiSettingDao.findByDate(null);
			var shiireZeiAnbun = kiShouhizeiSetting.shiireZeigakuAnbunFlg;
			var shouhizeiKbn = kiShouhizeiSetting.shouhizeiKbn;
			boolean canUseZeinuki = shouhizeiKbn >= 1; // 税抜・一括税抜は大部分が共通なので変数化
	
			// 課税区分
			String kazeiKbnName = "";
			// 未設定
			if (isEmpty(this.kariKazeiKbn)) {
				this.errorList.add("借方課税区分を設定してください。");
				this.kariKazeiKbn = ""; // nullは空文字にしておく（貸倒等の対策）
			}
			// 設定ありの場合
			else {
				var kazeiKbnDto = this.naibuCdSettingDao.find("kazei_kbn", this.kariKazeiKbn);
				kazeiKbnName = kazeiKbnDto == null ? "" : kazeiKbnDto.name;
			}
			
			// 実在する課税区分の場合のみ
			if(isNotEmpty(kazeiKbnName)) {
				var normalShoriGroupList = List.of(3, 4, 5, 6, 9, 10);
				var shiireShoriGroupList = List.of(2, 7, 8);
				
				var normalZeiList = new ArrayList<String>(List.of("000", "001", "003", "004"));
				var shiireZeiList = new ArrayList<String>(List.of("000", "003", "011", "012", "041", "042"));
				
				// 税抜系なら税抜区分追加
				if(canUseZeinuki) {
					normalZeiList.add("002");
					shiireZeiList.addAll(List.of("013", "014"));
				}
				
				// 正常系のいずれでもない場合エラー
				if(!((normalShoriGroupList.contains(this.kariShoriGroup) && normalZeiList.contains(this.kariKazeiKbn)) // 無印の税込が使えるグループ
					|| (shiireShoriGroupList.contains(this.kariShoriGroup) && shiireZeiList.contains(this.kariKazeiKbn)) // 課込・抜を使うグループ
					|| (this.kariShoriGroup == 7 && this.kariKazeiKbn.equals("004")) // 混在だけ特殊
					|| (!normalShoriGroupList.contains(this.kariShoriGroup) && !shiireShoriGroupList.contains(this.kariShoriGroup) && this.kariKazeiKbn.equals("100")))) { // 上記以外のグループ
					errorList.add("借方課税区分「" + kazeiKbnName + "」は科目「" + kamokuName + "」に対して設定できません。");
				}
			}
			//--------------------------------
			//消費税率マスターのチェック
			//--------------------------------
			if(inputSeigyo.kariZeiritsu) {
				try {
					if(!myLogic.isHensuuVar(kariZeiritsu) && !kariZeiritsu.equals("任意消費税率")){
						//マスター存在チェック
						commonLogic.checkZeiritsu(toDecimal(kariZeiritsu), kariKeigenZeiritsuKbn, toDate(yuukouKigenTo), errorList, "（借方）");
						commonLogic.checkZeiritsu(toDecimal(kariZeiritsu), kariKeigenZeiritsuKbn, toDate(yuukouKigenFrom), errorList, "（借方）");
		
						//課税区分と消費税率の相関チェック
						if(kariShoriGroup < 21 && myLogic.isNotKazeiGroup(kariKazeiKbn)) {
							errorList.add("消費税率を登録できない課税区分です。");
						}
					}else {
						//任意消費税率のとき軽減税率区分が入力されていればエラー
						if(!isEmpty(kariKeigenZeiritsuKbn)){
							errorList.add("任意消費税率のとき軽減税率区分は指定できません。");
						}
					}
				}catch (Exception e) {
					errorList.add("「"+ kariZeiritsu +"」は消費税率に対して設定できません。");
				}
			}
			
			// 分離区分
			boolean isEmptyBunriKbn = isEmpty(kariBunriKbn) || kariBunriKbn.equals("9");
			var bunriKbnDto = this.naibuCdSettingDao.find("bunri_kbn", this.kariBunriKbn);
			var bunriKbnName = bunriKbnDto == null ? "" : bunriKbnDto.name;
			var invalidBunriKbnMessage = "分離区分「" + bunriKbnName + "」は課税区分「" + kazeiKbnName + "」に対して設定できません。";
			if(bunriKbnDto == null && !isEmpty(kariBunriKbn)) {
				errorList.add("「"+ kariBunriKbn +"」は分離区分に対して設定できません。");
			}else {
				// 区分が存在すべき条件
				if (canUseZeinuki 
					&& List.of("001", "011", "012", "002", "013", "014").contains(this.kariKazeiKbn)) {
					if(isEmptyBunriKbn) {
						errorList.add("分離区分を設定してください。");
					}
					else if ((List.of("002", "013", "014").contains(this.kariKazeiKbn) && !kariBunriKbn.equals("0"))
							|| (List.of("001", "011", "012").contains(this.kariKazeiKbn)
								&& !kariBunriKbn.equals("1")
								&& !(shouhizeiKbn == 2 && List.of("0","2").contains(this.kariBunriKbn)))) {
						errorList.add(invalidBunriKbnMessage);
				    }
				}
				// 存在してはならない条件
				// 課税区分が実在しないのなら意味をなさないので、これが存在する場合のみ
				else if(!isEmptyBunriKbn && isNotEmpty(kazeiKbnName) && isNotEmpty(bunriKbnName)) // 存在しない区分は別枠
				{
					errorList.add(invalidBunriKbnMessage);
				}
			}

			//仕入区分の登録が必要な条件（課税区分と科目が仕入系で仕入税額按分が個別）の場合、仕入区分の空白チェック
			boolean isEmptyShiireKbn = isEmpty(kariShiireKbn) || kariShiireKbn.equals("0");
			var shiireKbnDto = this.naibuCdSettingDao.find("shiire_kbn", this.kariShiireKbn);
			var shiireKbnName = shiireKbnDto == null ? "" : shiireKbnDto.name;
			if(shiireKbnDto == null && !isEmpty(kariShiireKbn)) {
				errorList.add("「"+ kariShiireKbn +"」は仕入区分に対して設定できません。");
			}else {
				// 区分が存在すべき条件
				if(shiireZeiAnbun == 1
						&& List.of("001", "002", "011", "013").contains(kariKazeiKbn)
						&& List.of(2, 5, 6, 7, 8, 10).contains(kariShoriGroup))
				{
					if(isEmptyShiireKbn) {
						errorList.add("仕入区分を設定してください。");
					}
				}
				// 存在してはならない条件
				// 課税区分が実在しないのなら意味をなさないので、これが存在する場合のみ
				else if(!isEmptyShiireKbn && isNotEmpty(kazeiKbnName) && isNotEmpty(shiireKbnName)) // 存在しない区分は別枠
				{
					errorList.add("仕入区分「" + shiireKbnName + "」は科目「" + kamokuName + "」・課税区分「" + kazeiKbnName + "」に対して設定できません。");
				}
			}
		}

		// プロパティ配列
		// 借方
		String[] kariVariables = {
			    kariFutanBumonCd, kariKanjyouKamokuCd, kariKamokuEdabanCd, kariTorihikisakiCd, kariProjectCd, kariSegmentCd,
			    kariUf1Cd, kariUf2Cd, kariUf3Cd, kariUf4Cd, kariUf5Cd, kariUf6Cd, kariUf7Cd, kariUf8Cd, kariUf9Cd, kariUf10Cd,
			    kariUfKotei1Cd, kariUfKotei2Cd, kariUfKotei3Cd, kariUfKotei4Cd, kariUfKotei5Cd, kariUfKotei6Cd, kariUfKotei7Cd, kariUfKotei8Cd, kariUfKotei9Cd, kariUfKotei10Cd,
			    kariKazeiKbn, kariBunriKbn, kariShiireKbn
			};
		
		// 貸方1
		String[] kashiVariables1 = {
			    kashiFutanBumonCd1, kashiKanjyouKamokuCd1, kashiKamokuEdabanCd1, kashiTorihikisakiCd1, kashiProjectCd1, kashiSegmentCd1,
			    kashiUf1Cd1, kashiUf2Cd1, kashiUf3Cd1, kashiUf4Cd1, kashiUf5Cd1, kashiUf6Cd1, kashiUf7Cd1, kashiUf8Cd1, kashiUf9Cd1, kashiUf10Cd1,
			    kashiUfKotei1Cd1, kashiUfKotei2Cd1, kashiUfKotei3Cd1, kashiUfKotei4Cd1, kashiUfKotei5Cd1, kashiUfKotei6Cd1, kashiUfKotei7Cd1, kashiUfKotei8Cd1, kashiUfKotei9Cd1, kashiUfKotei10Cd1,
			    kashiKazeiKbn1, kashiBunriKbn1, kashiShiireKbn1
			};
		
		// 貸方2
		String[] kashiVariables2 = {
			    kashiFutanBumonCd2, kashiKanjyouKamokuCd2, kashiKamokuEdabanCd2, kashiTorihikisakiCd2, kashiProjectCd2, kashiSegmentCd2,
			    kashiUf1Cd2, kashiUf2Cd2, kashiUf3Cd2, kashiUf4Cd2, kashiUf5Cd2, kashiUf6Cd2, kashiUf7Cd2, kashiUf8Cd2, kashiUf9Cd2, kashiUf10Cd2,
			    kashiUfKotei1Cd2, kashiUfKotei2Cd2, kashiUfKotei3Cd2, kashiUfKotei4Cd2, kashiUfKotei5Cd2, kashiUfKotei6Cd2, kashiUfKotei7Cd2, kashiUfKotei8Cd2, kashiUfKotei9Cd2, kashiUfKotei10Cd2,
			    kashiKazeiKbn2, kashiBunriKbn2, kashiShiireKbn2
			};

		// 貸方3
		String[] kashiVariables3 = {
			    kashiFutanBumonCd3, kashiKanjyouKamokuCd3, kashiKamokuEdabanCd3, kashiTorihikisakiCd3, kashiProjectCd3, kashiSegmentCd3,
			    kashiUf1Cd3, kashiUf2Cd3, kashiUf3Cd3, kashiUf4Cd3, kashiUf5Cd3, kashiUf6Cd3, kashiUf7Cd3, kashiUf8Cd3, kashiUf9Cd3, kashiUf10Cd3,
			    kashiUfKotei1Cd3, kashiUfKotei2Cd3, kashiUfKotei3Cd3, kashiUfKotei4Cd3, kashiUfKotei5Cd3, kashiUfKotei6Cd3, kashiUfKotei7Cd3, kashiUfKotei8Cd3, kashiUfKotei9Cd3, kashiUfKotei10Cd3,
			    kashiKazeiKbn3, kashiBunriKbn3, kashiShiireKbn3
			};
		
		// 貸方4
		String[] kashiVariables4 = {
			    kashiFutanBumonCd4, kashiKanjyouKamokuCd4, kashiKamokuEdabanCd4, kashiTorihikisakiCd4, kashiProjectCd4, kashiSegmentCd4,
			    kashiUf1Cd4, kashiUf2Cd4, kashiUf3Cd4, kashiUf4Cd4, kashiUf5Cd4, kashiUf6Cd4, kashiUf7Cd4, kashiUf8Cd4, kashiUf9Cd4, kashiUf10Cd4,
			    kashiUfKotei1Cd4, kashiUfKotei2Cd4, kashiUfKotei3Cd4, kashiUfKotei4Cd4, kashiUfKotei5Cd4, kashiUfKotei6Cd4, kashiUfKotei7Cd4, kashiUfKotei8Cd4, kashiUfKotei9Cd4, kashiUfKotei10Cd4,
			    kashiKazeiKbn4, kashiBunriKbn4, kashiShiireKbn4
			};
		
		// 貸方5
		String[] kashiVariables5 = {
			    kashiFutanBumonCd5, kashiKanjyouKamokuCd5, kashiKamokuEdabanCd5, kashiTorihikisakiCd5, kashiProjectCd5, kashiSegmentCd5,
			    kashiUf1Cd5, kashiUf2Cd5, kashiUf3Cd5, kashiUf4Cd5, kashiUf5Cd5, kashiUf6Cd5, kashiUf7Cd5, kashiUf8Cd5, kashiUf9Cd5, kashiUf10Cd5,
			    kashiUfKotei1Cd5, kashiUfKotei2Cd5, kashiUfKotei3Cd5, kashiUfKotei4Cd5, kashiUfKotei5Cd5, kashiUfKotei6Cd5, kashiUfKotei7Cd5, kashiUfKotei8Cd5, kashiUfKotei9Cd5, kashiUfKotei10Cd5,
			    kashiKazeiKbn5, kashiBunriKbn5, kashiShiireKbn5
			};
		
		//----------
		//変数チェック
		//----------
		var shiwakeVarListKariArray = new Object[]{
			    inputSeigyo.shiwakeVarListKariBumon, inputSeigyo.shiwakeVarListKariKamoku, inputSeigyo.shiwakeVarListKariEdano, inputSeigyo.shiwakeVarListKariTorihiki, inputSeigyo.shiwakeVarListKariProject, inputSeigyo.shiwakeVarListKariSegment,
			    inputSeigyo.shiwakeVarListKariUf1, inputSeigyo.shiwakeVarListKariUf2, inputSeigyo.shiwakeVarListKariUf3, inputSeigyo.shiwakeVarListKariUf4, inputSeigyo.shiwakeVarListKariUf5,
			    inputSeigyo.shiwakeVarListKariUf6, inputSeigyo.shiwakeVarListKariUf7, inputSeigyo.shiwakeVarListKariUf8, inputSeigyo.shiwakeVarListKariUf9, inputSeigyo.shiwakeVarListKariUf10
			};
		this.shiwakePatternVarCheck(kariVariables, shiwakeVarListKariArray, "（借方）");
		
		var shiwakeVarListKashi1Array = new Object[]{
			    inputSeigyo.shiwakeVarListKashi1Bumon, inputSeigyo.shiwakeVarListKashi1Kamoku, inputSeigyo.shiwakeVarListKashi1Edano, inputSeigyo.shiwakeVarListKashi1Torihiki, inputSeigyo.shiwakeVarListKashi1Project, inputSeigyo.shiwakeVarListKashi1Segment,
			    inputSeigyo.shiwakeVarListKashi1Uf1, inputSeigyo.shiwakeVarListKashi1Uf2, inputSeigyo.shiwakeVarListKashi1Uf3, inputSeigyo.shiwakeVarListKashi1Uf4, inputSeigyo.shiwakeVarListKashi1Uf5,
			    inputSeigyo.shiwakeVarListKashi1Uf6, inputSeigyo.shiwakeVarListKashi1Uf7, inputSeigyo.shiwakeVarListKashi1Uf8, inputSeigyo.shiwakeVarListKashi1Uf9, inputSeigyo.shiwakeVarListKashi1Uf10
			};
		this.shiwakePatternVarCheck(kashiVariables1, shiwakeVarListKashi1Array, "（貸方1）");
		
		var shiwakeVarListKashi2Array = new Object[]{
			    inputSeigyo.shiwakeVarListKashi2Bumon, inputSeigyo.shiwakeVarListKashi2Kamoku, inputSeigyo.shiwakeVarListKashi2Edano, inputSeigyo.shiwakeVarListKashi2Torihiki, inputSeigyo.shiwakeVarListKashi2Project, inputSeigyo.shiwakeVarListKashi2Segment,
			    inputSeigyo.shiwakeVarListKashi2Uf1, inputSeigyo.shiwakeVarListKashi2Uf2, inputSeigyo.shiwakeVarListKashi2Uf3, inputSeigyo.shiwakeVarListKashi2Uf4, inputSeigyo.shiwakeVarListKashi2Uf5,
			    inputSeigyo.shiwakeVarListKashi2Uf6, inputSeigyo.shiwakeVarListKashi2Uf7, inputSeigyo.shiwakeVarListKashi2Uf8, inputSeigyo.shiwakeVarListKashi2Uf9, inputSeigyo.shiwakeVarListKashi2Uf10
			};
		this.shiwakePatternVarCheck(kashiVariables2, shiwakeVarListKashi2Array, "（貸方2）");
		
		var shiwakeVarListKashi3Array = new Object[]{
			    inputSeigyo.shiwakeVarListKashi3Bumon, inputSeigyo.shiwakeVarListKashi3Kamoku, inputSeigyo.shiwakeVarListKashi3Edano, inputSeigyo.shiwakeVarListKashi3Torihiki, inputSeigyo.shiwakeVarListKashi3Project, inputSeigyo.shiwakeVarListKashi3Segment,
			    inputSeigyo.shiwakeVarListKashi3Uf1, inputSeigyo.shiwakeVarListKashi3Uf2, inputSeigyo.shiwakeVarListKashi3Uf3, inputSeigyo.shiwakeVarListKashi3Uf4, inputSeigyo.shiwakeVarListKashi3Uf5,
			    inputSeigyo.shiwakeVarListKashi3Uf6, inputSeigyo.shiwakeVarListKashi3Uf7, inputSeigyo.shiwakeVarListKashi3Uf8, inputSeigyo.shiwakeVarListKashi3Uf9, inputSeigyo.shiwakeVarListKashi3Uf10
			};
		this.shiwakePatternVarCheck(kashiVariables3, shiwakeVarListKashi3Array, "（貸方3）");
		
		var shiwakeVarListKashi4Array = new Object[]{
			    inputSeigyo.shiwakeVarListKashi4Bumon, inputSeigyo.shiwakeVarListKashi4Kamoku, inputSeigyo.shiwakeVarListKashi4Edano, inputSeigyo.shiwakeVarListKashi4Torihiki, inputSeigyo.shiwakeVarListKashi4Project, inputSeigyo.shiwakeVarListKashi4Segment,
			    inputSeigyo.shiwakeVarListKashi4Uf1, inputSeigyo.shiwakeVarListKashi4Uf2, inputSeigyo.shiwakeVarListKashi4Uf3, inputSeigyo.shiwakeVarListKashi4Uf4, inputSeigyo.shiwakeVarListKashi4Uf5,
			    inputSeigyo.shiwakeVarListKashi4Uf6, inputSeigyo.shiwakeVarListKashi4Uf7, inputSeigyo.shiwakeVarListKashi4Uf8, inputSeigyo.shiwakeVarListKashi4Uf9, inputSeigyo.shiwakeVarListKashi4Uf10
			};
		this.shiwakePatternVarCheck(kashiVariables4, shiwakeVarListKashi4Array, "（貸方4）");
		
		var shiwakeVarListKashi5Array = new Object[]{
			    inputSeigyo.shiwakeVarListKashi5Bumon, inputSeigyo.shiwakeVarListKashi5Kamoku, inputSeigyo.shiwakeVarListKashi5Edano, inputSeigyo.shiwakeVarListKashi5Torihiki, inputSeigyo.shiwakeVarListKashi5Project, inputSeigyo.shiwakeVarListKashi5Segment,
			    inputSeigyo.shiwakeVarListKashi5Uf1, inputSeigyo.shiwakeVarListKashi5Uf2, inputSeigyo.shiwakeVarListKashi5Uf3, inputSeigyo.shiwakeVarListKashi5Uf4, inputSeigyo.shiwakeVarListKashi5Uf5,
			    inputSeigyo.shiwakeVarListKashi5Uf6, inputSeigyo.shiwakeVarListKashi5Uf7, inputSeigyo.shiwakeVarListKashi5Uf8, inputSeigyo.shiwakeVarListKashi5Uf9, inputSeigyo.shiwakeVarListKashi5Uf10
			};
		this.shiwakePatternVarCheck(kashiVariables5, shiwakeVarListKashi5Array, "（貸方5）");

		//----------
		//一見先　振込手数料負担部門コード設定によるチェック
		//----------
		if( EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI.equals(denpyouKbn)) {
			if(EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD).equals(EteamConst.ShiwakeConst.FUTAN)) {
				if(! kariFutanBumonCd.equals(ShiwakeConstWa.FUTAN)) {
					errorList.add("会社設定「一見先　振込手数料負担部門コード」が任意部門に設定されているため、借方負担部門は[" + ShiwakeConstWa.FUTAN + "]を設定してください。");
				}
			}
		}

		//----------
		//借・貸間整合性チェック
		//----------
		//借・貸で取引先「任意取引先」と固定コードの混在がないこと
		this.konzaiCheck(new String[] { kariTorihikisakiCd, kashiTorihikisakiCd1, kashiTorihikisakiCd2, kashiTorihikisakiCd3, kashiTorihikisakiCd4, kashiTorihikisakiCd5 }, "取引先", EteamConst.ShiwakeConstWa.TORIHIKI, false);

		//貸で「任意部門」「初期代表」なら借も「任意部門」「初期代表」であること
		List<String> kashiFutanBumonCdList = new ArrayList<>(
				Arrays.asList(kashiFutanBumonCd1, kashiFutanBumonCd2, kashiFutanBumonCd3, kashiFutanBumonCd4, kashiFutanBumonCd5));

		List.of(ShiwakeConstWa.FUTAN, ShiwakeConstWa.SYOKIDAIHYOU)
			.forEach(item -> {
				if (
					! kariFutanBumonCd.equals(item)
					&& kashiFutanBumonCdList.contains(item)) {
					errorList.add("貸方の負担部門のみに「" + item + "」を設定することはできません。");
				}});

		//借・貸でプロジェクトに「任意プロジェクト」と固定コードの混在がないこと
		this.konzaiCheck(new String[] { kariProjectCd, kashiProjectCd1, kashiProjectCd2, kashiProjectCd3, kashiProjectCd4, kashiProjectCd5 }, "プロジェクト", EteamConst.ShiwakeConstWa.PROJECT, false);

		//借・貸でセグメントに「任意セグメント」と固定コードの混在がないこと
		this.konzaiCheck(new String[] { kariSegmentCd, kashiSegmentCd1, kashiSegmentCd2, kashiSegmentCd3, kashiSegmentCd4, kashiSegmentCd5 }, "セグメント", EteamConst.ShiwakeConstWa.SEGMENT, false);

		//借・貸でUF1～10に「任意UF」と固定コードの混在がないこと
		this.konzaiCheck(new String[] { kariUf1Cd, kashiUf1Cd1, kashiUf1Cd2, kashiUf1Cd3, kashiUf1Cd4, kashiUf1Cd5 }, hfUfSeigyo.getUf1Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf2Cd, kashiUf2Cd1, kashiUf2Cd2, kashiUf2Cd3, kashiUf2Cd4, kashiUf2Cd5 }, hfUfSeigyo.getUf2Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf3Cd, kashiUf3Cd1, kashiUf3Cd2, kashiUf3Cd3, kashiUf3Cd4, kashiUf3Cd5 }, hfUfSeigyo.getUf3Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf4Cd, kashiUf4Cd1, kashiUf4Cd2, kashiUf4Cd3, kashiUf4Cd4, kashiUf4Cd5 }, hfUfSeigyo.getUf4Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf5Cd, kashiUf5Cd1, kashiUf5Cd2, kashiUf5Cd3, kashiUf5Cd4, kashiUf5Cd5 }, hfUfSeigyo.getUf5Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf6Cd, kashiUf6Cd1, kashiUf6Cd2, kashiUf6Cd3, kashiUf6Cd4, kashiUf6Cd5 }, hfUfSeigyo.getUf6Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf7Cd, kashiUf7Cd1, kashiUf7Cd2, kashiUf7Cd3, kashiUf7Cd4, kashiUf7Cd5 }, hfUfSeigyo.getUf7Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf8Cd, kashiUf8Cd1, kashiUf8Cd2, kashiUf8Cd3, kashiUf8Cd4, kashiUf8Cd5 }, hfUfSeigyo.getUf8Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf9Cd, kashiUf9Cd1, kashiUf9Cd2, kashiUf9Cd3, kashiUf9Cd4, kashiUf9Cd5 }, hfUfSeigyo.getUf9Name(), EteamConst.ShiwakeConstWa.UF, true);
		this.konzaiCheck(new String[] { kariUf10Cd, kashiUf10Cd1, kashiUf10Cd2, kashiUf10Cd3, kashiUf10Cd4, kashiUf10Cd5 }, hfUfSeigyo.getUf10Name(), EteamConst.ShiwakeConstWa.UF, true);

		//----------
		//マスターチェック
		//----------

		this.codeSoukanCheck(kariVariables, "（借方）");
		this.codeSoukanCheck(kashiVariables1, "（貸方1）");
		this.codeSoukanCheck(kashiVariables2, "（貸方2）");
		this.codeSoukanCheck(kashiVariables3, "（貸方3）");
		this.codeSoukanCheck(kashiVariables4, "（貸方4）");
		this.codeSoukanCheck(kashiVariables5, "（貸方5）");

		// 取引先仕入先チェック
		if (!myLogic.isHensuu(kariTorihikisakiCd))   myLogic.checkShiiresaki(errorList, "取引先コード（借方）" , kariTorihikisakiCd  , denpyouKbn);
		if (!myLogic.isHensuu(kashiTorihikisakiCd1)) myLogic.checkShiiresaki(errorList, "取引先コード（貸方1）", kashiTorihikisakiCd1, denpyouKbn);
		if (!myLogic.isHensuu(kashiTorihikisakiCd2)) myLogic.checkShiiresaki(errorList, "取引先コード（貸方2）", kashiTorihikisakiCd2, denpyouKbn);
		if (!myLogic.isHensuu(kashiTorihikisakiCd3)) myLogic.checkShiiresaki(errorList, "取引先コード（貸方3）", kashiTorihikisakiCd3, denpyouKbn);
		if (!myLogic.isHensuu(kashiTorihikisakiCd4)) myLogic.checkShiiresaki(errorList, "取引先コード（貸方4）", kashiTorihikisakiCd4, denpyouKbn);
		if (!myLogic.isHensuu(kashiTorihikisakiCd5)) myLogic.checkShiiresaki(errorList, "取引先コード（貸方5）", kashiTorihikisakiCd5, denpyouKbn);

		//重複チェック処理の前準備
		//とりあえずユニバーサルフィールド1から10までをListにしてマッチ
		// UFマッピングの重複チェックが必要なのはSIASだけ
		if(Open21Env.getVersion() == Version.SIAS) {
			List<String> shainUfMatching = new ArrayList<String>(Arrays.asList(uf1Mapping, uf2Mapping, uf3Mapping, uf4Mapping, uf5Mapping, uf6Mapping, uf7Mapping, uf8Mapping, uf9Mapping, uf10Mapping));
			List<String> shainUfKoteiMatching = new ArrayList<String>(Arrays.asList(ufKotei1Mapping, ufKotei2Mapping, ufKotei3Mapping, ufKotei4Mapping, ufKotei5Mapping, ufKotei6Mapping, ufKotei7Mapping, ufKotei8Mapping, ufKotei9Mapping, ufKotei10Mapping));
			
			//場所特定のためのMAP。エラーメッセージが１ではなく①なので変換
			HashMap<Integer, String> matchingMap = new HashMap<>();
			matchingMap.put(1,"①");
			matchingMap.put(2,"②");
			matchingMap.put(3,"③");
			matchingMap.put(4,"④");
			matchingMap.put(5,"⑤");
			matchingMap.put(6,"⑥");
			matchingMap.put(7,"⑦");
			matchingMap.put(8,"⑧");
			matchingMap.put(9,"⑨");
			matchingMap.put(10,"⑩");
	
			//会社設定から社員コード連携の値を取りに行く。UF1と1でマッチさせるためにUF除外。さらに0のまま引っ張るとマッチするので回避
			String shainCdRenkeiMatching = "";
			if(shainRenkei.equals("0")) {
				//利用なしなので特に何もなしそのまま""でOK
			}else {
				//何かしら設定されているのでUFだけ削除
				shainCdRenkeiMatching = shainRenkei.replace("UF", "");
			}
	
			//会社設定から法人カード連携の値を取りに行く。UF1と1でマッチさせるためにUF除外
			String houjinCardRenkeiMatching = "";
			if(houjinCardRenkei.equals("0")) {
				//利用なしなので特に何もなしそのまま""でOK
			}else {
				//何かしら設定されているのでUFだけ削除
				houjinCardRenkeiMatching = houjinCardRenkei.replace("UF", "");
			}
	
			//場所の特定とそれが含むかの確認
			int shainCdAndUfRenkeiCounter = 1;
			for(String s : shainUfMatching) {
				if(s.equals(shainCdRenkeiMatching)) {
					errorList.add("社員コードの連携先がユニバーサルフィールドマッピング" + matchingMap.get(shainCdAndUfRenkeiCounter) + "と重複しています。");
				}
				shainCdAndUfRenkeiCounter++;
			}
	
			int shainCdAndUfKoteiRenkeiCounter = 1;
			for(String s : shainUfKoteiMatching) {
				if(s.equals(shainCdRenkeiMatching)) {
					errorList.add("社員コードの連携先がユニバーサルフィールドマッピング（固定値）" + matchingMap.get(shainCdAndUfKoteiRenkeiCounter) + "と重複しています。");
				}
				shainCdAndUfKoteiRenkeiCounter++;
			}
	
			int houjinCardAndUfRenkeiCounter = 1;
			for(String s : shainUfMatching) {
				if(s.equals(houjinCardRenkeiMatching)) {
					errorList.add("法人カード識別用番号の連携先がユニバーサルフィールド" + matchingMap.get(houjinCardAndUfRenkeiCounter) + "と重複しています。");
				}
				houjinCardAndUfRenkeiCounter++;
			}
	
			int houjinCardAndUfKoteiRenkeiCounter = 1;
			for(String s : shainUfKoteiMatching) {
				if(s.equals(houjinCardRenkeiMatching)) {
					errorList.add("法人カード識別用番号の連携先がユニバーサルフィールドマッピング（固定値）" + matchingMap.get(houjinCardAndUfKoteiRenkeiCounter) + "と重複しています。");
				}
				houjinCardAndUfKoteiRenkeiCounter++;
			}
		}
	}
	
	/**
	 * 仕訳変数チェック
	 * @param properties プロパティ配列
	 * @param settingArray 入力制御はいっ烈
	 * @param taishaku 貸借（1～5）
	 */
	protected void shiwakePatternVarCheck(String[] properties, Object[] settingArray, String taishaku)
	{
		int i = 0;
		myLogic.shiwakeVarCheck("負担部門コード" + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck("勘定科目コード" + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck("勘定科目枝番コード" + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck("取引先コード" + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck("プロジェクトコード" + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck("セグメントコード" + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf1Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf2Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf3Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf4Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf5Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf6Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf7Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf8Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
		myLogic.shiwakeVarCheck(hfUfSeigyo.getUf9Name() + taishaku, properties[i], (List<GMap>)settingArray[i++], errorList);
	}
	
	/**
	 * 混在チェックの一般化
	 * @param cdArray コード配列
	 * @param cdName コード名
	 * @param constWa 和名定数
	 * @param isUF UFか否か
	 */
	protected void konzaiCheck(String[] cdArray, String cdName, String constWa, boolean isUF)
	{
		Set<String> cdSet = new HashSet<>();
		for(var cd : cdArray)
		{
			if (isNotEmpty(cd)) cdSet.add(cd);
		}
		if (1 < cdSet.size() && cdSet.contains(constWa)) {
			String keyword = isUF ? "UF" : cdName;
			errorList.add(cdName + "に任意" + keyword + "と特定" + keyword + "（コード値入力）の混在指定はできません。");
		}
	}
	
	/**
	 * コードの相関チェック
	 * @param properties プロパティ各種
	 * @param taishaku 貸借（1～5）
	 */
	protected void codeSoukanCheck(String[] properties, String taishaku)
	{
		ShiwakeCheckData checkD = commonLogic.new ShiwakeCheckData();

		int i = 0;
		
		// UF以外
		this.checkAndSetHensuu(properties[i++], "負担部門コード" + taishaku, checkD::setFutanBumonNm, checkD::setFutanBumonCd);
		this.checkAndSetHensuu(properties[i++], "勘定科目コード" + taishaku, checkD::setKamokuNm, checkD::setKamokuCd);
		this.checkAndSetHensuu(properties[i++], "勘定科目枝番コード" + taishaku, checkD::setKamokuEdabanNm, checkD::setKamokuEdabanCd);
		this.checkAndSetHensuu(properties[i++], "取引先コード" + taishaku, checkD::setTorihikisakiNm, checkD::setTorihikisakiCd);
		this.checkAndSetHensuu(properties[i++], "プロジェクトコード" + taishaku, checkD::setProjectNm, checkD::setProjectCd);
		this.checkAndSetHensuu(properties[i++], "セグメントコード" + taishaku, checkD::setSegmentNm, checkD::setSegmentCd);

		// UF1～10
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf1Name() + taishaku, checkD::setUf1Nm, checkD::setUf1Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf2Name() + taishaku, checkD::setUf2Nm, checkD::setUf2Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf3Name() + taishaku, checkD::setUf3Nm, checkD::setUf3Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf4Name() + taishaku, checkD::setUf4Nm, checkD::setUf4Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf5Name() + taishaku, checkD::setUf5Nm, checkD::setUf5Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf6Name() + taishaku, checkD::setUf6Nm, checkD::setUf6Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf7Name() + taishaku, checkD::setUf7Nm, checkD::setUf7Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf8Name() + taishaku, checkD::setUf8Nm, checkD::setUf8Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf9Name() + taishaku, checkD::setUf9Nm, checkD::setUf9Cd);
		this.checkAndSetHensuu(properties[i++], hfUfSeigyo.getUf10Name() + taishaku, checkD::setUf10Nm, checkD::setUf10Cd);

		// UF固定1～10
		checkD.ufKotei1Nm = hfUfSeigyo.getUfKotei1Name() + taishaku;
		checkD.ufKotei1Cd = properties[i++];
		checkD.ufKotei2Nm = hfUfSeigyo.getUfKotei2Name() + taishaku;
		checkD.ufKotei2Cd = properties[i++];
		checkD.ufKotei3Nm = hfUfSeigyo.getUfKotei3Name() + taishaku;
		checkD.ufKotei3Cd = properties[i++];
		checkD.ufKotei4Nm = hfUfSeigyo.getUfKotei4Name() + taishaku;
		checkD.ufKotei4Cd = properties[i++];
		checkD.ufKotei5Nm = hfUfSeigyo.getUfKotei5Name() + taishaku;
		checkD.ufKotei5Cd = properties[i++];
		checkD.ufKotei6Nm = hfUfSeigyo.getUfKotei6Name() + taishaku;
		checkD.ufKotei6Cd = properties[i++];
		checkD.ufKotei7Nm = hfUfSeigyo.getUfKotei7Name() + taishaku;
		checkD.ufKotei7Cd = properties[i++];
		checkD.ufKotei8Nm = hfUfSeigyo.getUfKotei8Name() + taishaku;
		checkD.ufKotei8Cd = properties[i++];
		checkD.ufKotei9Nm = hfUfSeigyo.getUfKotei9Name() + taishaku;
		checkD.ufKotei9Cd = properties[i++];
		checkD.ufKotei10Nm = hfUfSeigyo.getUfKotei10Name() + taishaku;
		checkD.ufKotei10Cd = properties[i++];

		// 課税区分
		checkD.kazeiKbnNm = "課税区分" + taishaku;
		checkD.kazeiKbn = properties[i++];
		
		// 分離区分
		checkD.bunriKbnNm = "分離区分" + taishaku;
		checkD.bunriKbn = properties[i++];
		
		// 仕入区分
		checkD.shiireKbnNm = "仕入区分" + taishaku;
		checkD.shiireKbn = properties[i++];

		//仕訳チェック用に科目枝番連携フラグ設定（借方のみ）
		if(taishaku.contains("借方")){
			checkD.zaimuEdabanRenkei = zaimuEdabanRenkei;
		}

		commonLogic.checkShiwake(denpyouKbn, checkD, errorList);
	}
	
	/**
	 * 一般化した変数セット用メソッド
	 * @param value コード
	 * @param name コード名称
	 * @param setName 名称セッター
	 * @param setCode コードセッター
	 */
	protected void checkAndSetHensuu(String value, String name, Consumer<String> setName, Consumer<String> setCode) {
	    if (!myLogic.isHensuu(value)) {
	        setName.accept(name);
	        setCode.accept(value);
	    }
	}
	
	/**
	 * @return 仕訳パターンマスターDto
	 */
	protected ShiwakePatternMaster createDto()
	{
		ShiwakePatternMaster shiwakePatternMaster = new ShiwakePatternMaster();
		shiwakePatternMaster.denpyouKbn = this.denpyouKbn;
		shiwakePatternMaster.shiwakeEdano = isEmpty(shiwakeEdano) ? this.shiwakePatternMasterDao.getSaiban(denpyouKbn) : toInteger(shiwakeEdano);
		shiwakePatternMaster.deleteFlg = isEmpty(deleteFlg) ? "0": deleteFlg;
		shiwakePatternMaster.yuukouKigenFrom = super.toDate(yuukouKigenFrom);
		shiwakePatternMaster.yuukouKigenTo = super.toDate(yuukouKigenTo);
		shiwakePatternMaster.bunrui1 = this.bunrui1;
		shiwakePatternMaster.bunrui2 = this.bunrui2;
		shiwakePatternMaster.bunrui3 = this.bunrui3;
		shiwakePatternMaster.torihikiName = this.torihikiNm;
		shiwakePatternMaster.tekiyouFlg = this.tekiyouNyuryokuOn;
		shiwakePatternMaster.tekiyou = this.tekiyou;
		shiwakePatternMaster.defaultHyoujiFlg = this.defaultHyoujiOn;
		shiwakePatternMaster.kousaihiHyoujiFlg = this.kousaihiOn;
		shiwakePatternMaster.kousaihiNinzuuRiyouFlg = super.isEmpty(kousaihiOn) ? "" : !("1".equals(kousaihiOn)) ? "0" : ninzuuOn;
		shiwakePatternMaster.kousaihiKijunGaku = super.isEmpty(kousaihiKijungaku) ? null : ( !("1".equals(kousaihiOn) && "1".equals(ninzuuOn)) ? new BigDecimal(0) : super.toDecimal(kousaihiKijungaku));
		shiwakePatternMaster.kousaihiCheckHouhou = super.isEmpty(kousaihiOn) ? "" : !("1".equals(kousaihiOn) && "1".equals(ninzuuOn)) ? "0" : kousaihiCheckHouhou;
		shiwakePatternMaster.kousaihiCheckResult = super.isEmpty(kousaihiOn) ? "" : ( !("1".equals(kousaihiOn) && "1".equals(ninzuuOn)) || ("0".equals(kousaihiCheckHouhou)) ) ? "1" : kousaihiCheckResult;
		shiwakePatternMaster.kakeFlg = this.kakeOn;
		shiwakePatternMaster.hyoujiJun = Integer.parseInt(hyoujijun);
		shiwakePatternMaster.shainCdRenkeiFlg = this.shainCdRenkei;
		shiwakePatternMaster.edabanRenkeiFlg = this.zaimuEdabanRenkei;
		shiwakePatternMaster.kariFutanBumonCd = this.myLogic.ConvertShiwakeVarNm2Cd(kariFutanBumonCd, inputSeigyo.shiwakeVarListKariBumon);
		shiwakePatternMaster.kariKamokuCd = this.myLogic.ConvertShiwakeVarNm2Cd(kariKanjyouKamokuCd, inputSeigyo.shiwakeVarListKariKamoku);
		shiwakePatternMaster.kariKamokuEdabanCd = this.myLogic.ConvertShiwakeVarNm2Cd(kariKamokuEdabanCd, inputSeigyo.shiwakeVarListKariEdano);
		shiwakePatternMaster.kariTorihikisakiCd = this.myLogic.ConvertShiwakeVarNm2Cd(kariTorihikisakiCd, inputSeigyo.shiwakeVarListKariTorihiki);
		shiwakePatternMaster.kariProjectCd = this.myLogic.ConvertShiwakeVarNm2Cd(kariProjectCd, inputSeigyo.shiwakeVarListKariProject);
		shiwakePatternMaster.kariSegmentCd = this.myLogic.ConvertShiwakeVarNm2Cd(kariSegmentCd, inputSeigyo.shiwakeVarListKariSegment);
		shiwakePatternMaster.kariUf1Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf1Cd, inputSeigyo.shiwakeVarListKariUf1);
		shiwakePatternMaster.kariUf2Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf2Cd, inputSeigyo.shiwakeVarListKariUf2);
		shiwakePatternMaster.kariUf3Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf3Cd, inputSeigyo.shiwakeVarListKariUf3);
		shiwakePatternMaster.kariUf4Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf4Cd, inputSeigyo.shiwakeVarListKariUf4);
		shiwakePatternMaster.kariUf5Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf5Cd, inputSeigyo.shiwakeVarListKariUf5);
		shiwakePatternMaster.kariUf6Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf6Cd, inputSeigyo.shiwakeVarListKariUf6);
		shiwakePatternMaster.kariUf7Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf7Cd, inputSeigyo.shiwakeVarListKariUf7);
		shiwakePatternMaster.kariUf8Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf8Cd, inputSeigyo.shiwakeVarListKariUf8);
		shiwakePatternMaster.kariUf9Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf9Cd, inputSeigyo.shiwakeVarListKariUf9);
		shiwakePatternMaster.kariUf10Cd = this.myLogic.ConvertShiwakeVarNm2Cd(kariUf10Cd, inputSeigyo.shiwakeVarListKariUf10);
		shiwakePatternMaster.kariUfKotei1Cd = this.kariUfKotei1Cd;
		shiwakePatternMaster.kariUfKotei2Cd = this.kariUfKotei2Cd;
		shiwakePatternMaster.kariUfKotei3Cd = this.kariUfKotei3Cd;
		shiwakePatternMaster.kariUfKotei4Cd = this.kariUfKotei4Cd;
		shiwakePatternMaster.kariUfKotei5Cd = this.kariUfKotei5Cd;
		shiwakePatternMaster.kariUfKotei6Cd = this.kariUfKotei6Cd;
		shiwakePatternMaster.kariUfKotei7Cd = this.kariUfKotei7Cd;
		shiwakePatternMaster.kariUfKotei8Cd = this.kariUfKotei8Cd;
		shiwakePatternMaster.kariUfKotei9Cd = this.kariUfKotei9Cd;
		shiwakePatternMaster.kariUfKotei10Cd = this.kariUfKotei10Cd;
		shiwakePatternMaster.kariKazeiKbn = this.kariKazeiKbn;
		shiwakePatternMaster.kariZeiritsu = this.kariZeiritsu.equals("任意消費税率") ? ShiwakeConst.ZEIRITSU: this.kariZeiritsu;
		shiwakePatternMaster.kariKeigenZeiritsuKbn = this.kariKeigenZeiritsuKbn;
		shiwakePatternMaster.kashiFutanBumonCd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiFutanBumonCd1, inputSeigyo.shiwakeVarListKashi1Bumon);
		shiwakePatternMaster.kashiKamokuCd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKanjyouKamokuCd1, inputSeigyo.shiwakeVarListKashi1Kamoku);
		shiwakePatternMaster.kashiKamokuEdabanCd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKamokuEdabanCd1, inputSeigyo.shiwakeVarListKashi1Edano);
		shiwakePatternMaster.kashiTorihikisakiCd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiTorihikisakiCd1, inputSeigyo.shiwakeVarListKashi1Torihiki);
		shiwakePatternMaster.kashiProjectCd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiProjectCd1, inputSeigyo.shiwakeVarListKashi1Project);
		shiwakePatternMaster.kashiSegmentCd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiSegmentCd1, inputSeigyo.shiwakeVarListKashi1Segment);
		shiwakePatternMaster.kashiUf1Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf1Cd1, inputSeigyo.shiwakeVarListKashi1Uf1);
		shiwakePatternMaster.kashiUf2Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf2Cd1, inputSeigyo.shiwakeVarListKashi1Uf2);
		shiwakePatternMaster.kashiUf3Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf3Cd1, inputSeigyo.shiwakeVarListKashi1Uf3);
		shiwakePatternMaster.kashiUf4Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf4Cd1, inputSeigyo.shiwakeVarListKashi1Uf4);
		shiwakePatternMaster.kashiUf5Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf5Cd1, inputSeigyo.shiwakeVarListKashi1Uf5);
		shiwakePatternMaster.kashiUf6Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf6Cd1, inputSeigyo.shiwakeVarListKashi1Uf6);
		shiwakePatternMaster.kashiUf7Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf7Cd1, inputSeigyo.shiwakeVarListKashi1Uf7);
		shiwakePatternMaster.kashiUf8Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf8Cd1, inputSeigyo.shiwakeVarListKashi1Uf8);
		shiwakePatternMaster.kashiUf9Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf9Cd1, inputSeigyo.shiwakeVarListKashi1Uf9);
		shiwakePatternMaster.kashiUf10Cd1 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf10Cd1, inputSeigyo.shiwakeVarListKashi1Uf10);
		shiwakePatternMaster.kashiUfKotei1Cd1 = this.kashiUfKotei1Cd1;
		shiwakePatternMaster.kashiUfKotei2Cd1 = this.kashiUfKotei2Cd1;
		shiwakePatternMaster.kashiUfKotei3Cd1 = this.kashiUfKotei3Cd1;
		shiwakePatternMaster.kashiUfKotei4Cd1 = this.kashiUfKotei4Cd1;
		shiwakePatternMaster.kashiUfKotei5Cd1 = this.kashiUfKotei5Cd1;
		shiwakePatternMaster.kashiUfKotei6Cd1 = this.kashiUfKotei6Cd1;
		shiwakePatternMaster.kashiUfKotei7Cd1 = this.kashiUfKotei7Cd1;
		shiwakePatternMaster.kashiUfKotei8Cd1 = this.kashiUfKotei8Cd1;
		shiwakePatternMaster.kashiUfKotei9Cd1 = this.kashiUfKotei9Cd1;
		shiwakePatternMaster.kashiUfKotei10Cd1 = this.kashiUfKotei10Cd1;
		shiwakePatternMaster.kashiKazeiKbn1 = ""; //貸方課税区分は未設定固定
		shiwakePatternMaster.kashiFutanBumonCd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiFutanBumonCd2, inputSeigyo.shiwakeVarListKashi2Bumon);
		shiwakePatternMaster.kashiKamokuCd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKanjyouKamokuCd2, inputSeigyo.shiwakeVarListKashi2Kamoku);
		shiwakePatternMaster.kashiKamokuEdabanCd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKamokuEdabanCd2, inputSeigyo.shiwakeVarListKashi2Edano);
		shiwakePatternMaster.kashiTorihikisakiCd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiTorihikisakiCd2, inputSeigyo.shiwakeVarListKashi2Torihiki);
		shiwakePatternMaster.kashiProjectCd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiProjectCd2, inputSeigyo.shiwakeVarListKashi2Project);
		shiwakePatternMaster.kashiSegmentCd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiSegmentCd2, inputSeigyo.shiwakeVarListKashi2Segment);
		shiwakePatternMaster.kashiUf1Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf1Cd2, inputSeigyo.shiwakeVarListKashi2Uf1);
		shiwakePatternMaster.kashiUf2Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf2Cd2, inputSeigyo.shiwakeVarListKashi2Uf2);
		shiwakePatternMaster.kashiUf3Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf3Cd2, inputSeigyo.shiwakeVarListKashi2Uf3);
		shiwakePatternMaster.kashiUf4Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf4Cd2, inputSeigyo.shiwakeVarListKashi1Uf4);
		shiwakePatternMaster.kashiUf5Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf5Cd2, inputSeigyo.shiwakeVarListKashi1Uf5);
		shiwakePatternMaster.kashiUf6Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf6Cd2, inputSeigyo.shiwakeVarListKashi1Uf6);
		shiwakePatternMaster.kashiUf7Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf7Cd2, inputSeigyo.shiwakeVarListKashi1Uf7);
		shiwakePatternMaster.kashiUf8Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf8Cd2, inputSeigyo.shiwakeVarListKashi1Uf8);
		shiwakePatternMaster.kashiUf9Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf9Cd2, inputSeigyo.shiwakeVarListKashi1Uf9);
		shiwakePatternMaster.kashiUf10Cd2 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf10Cd2, inputSeigyo.shiwakeVarListKashi1Uf10);
		shiwakePatternMaster.kashiUfKotei1Cd2 = this.kashiUfKotei1Cd2;
		shiwakePatternMaster.kashiUfKotei2Cd2 = this.kashiUfKotei2Cd2;
		shiwakePatternMaster.kashiUfKotei3Cd2 = this.kashiUfKotei3Cd2;
		shiwakePatternMaster.kashiUfKotei4Cd2 = this.kashiUfKotei4Cd2;
		shiwakePatternMaster.kashiUfKotei5Cd2 = this.kashiUfKotei5Cd2;
		shiwakePatternMaster.kashiUfKotei6Cd2 = this.kashiUfKotei6Cd2;
		shiwakePatternMaster.kashiUfKotei7Cd2 = this.kashiUfKotei7Cd2;
		shiwakePatternMaster.kashiUfKotei8Cd2 = this.kashiUfKotei8Cd2;
		shiwakePatternMaster.kashiUfKotei9Cd2 = this.kashiUfKotei9Cd2;
		shiwakePatternMaster.kashiUfKotei10Cd2 = this.kashiUfKotei10Cd2;
		shiwakePatternMaster.kashiKazeiKbn2 = ""; //貸方課税区分は未設定固定
		shiwakePatternMaster.kashiFutanBumonCd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiFutanBumonCd3, inputSeigyo.shiwakeVarListKashi3Bumon);
		shiwakePatternMaster.kashiKamokuCd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKanjyouKamokuCd3, inputSeigyo.shiwakeVarListKashi3Kamoku);
		shiwakePatternMaster.kashiKamokuEdabanCd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKamokuEdabanCd3, inputSeigyo.shiwakeVarListKashi3Edano);
		shiwakePatternMaster.kashiTorihikisakiCd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiTorihikisakiCd3, inputSeigyo.shiwakeVarListKashi3Torihiki);
		shiwakePatternMaster.kashiProjectCd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiProjectCd3, inputSeigyo.shiwakeVarListKashi3Project);
		shiwakePatternMaster.kashiSegmentCd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiSegmentCd3, inputSeigyo.shiwakeVarListKashi3Segment);
		shiwakePatternMaster.kashiUf1Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf1Cd3, inputSeigyo.shiwakeVarListKashi3Uf1);
		shiwakePatternMaster.kashiUf2Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf2Cd3, inputSeigyo.shiwakeVarListKashi3Uf2);
		shiwakePatternMaster.kashiUf3Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf3Cd3, inputSeigyo.shiwakeVarListKashi3Uf3);
		shiwakePatternMaster.kashiUf4Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf4Cd3, inputSeigyo.shiwakeVarListKashi3Uf4);
		shiwakePatternMaster.kashiUf5Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf5Cd3, inputSeigyo.shiwakeVarListKashi3Uf5);
		shiwakePatternMaster.kashiUf6Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf6Cd3, inputSeigyo.shiwakeVarListKashi3Uf6);
		shiwakePatternMaster.kashiUf7Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf7Cd3, inputSeigyo.shiwakeVarListKashi3Uf7);
		shiwakePatternMaster.kashiUf8Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf8Cd3, inputSeigyo.shiwakeVarListKashi3Uf8);
		shiwakePatternMaster.kashiUf9Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf9Cd3, inputSeigyo.shiwakeVarListKashi3Uf9);
		shiwakePatternMaster.kashiUf10Cd3 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf10Cd3, inputSeigyo.shiwakeVarListKashi3Uf10);
		shiwakePatternMaster.kashiUfKotei1Cd3 = this.kashiUfKotei1Cd3;
		shiwakePatternMaster.kashiUfKotei2Cd3 = this.kashiUfKotei2Cd3;
		shiwakePatternMaster.kashiUfKotei3Cd3 = this.kashiUfKotei3Cd3;
		shiwakePatternMaster.kashiUfKotei4Cd3 = this.kashiUfKotei4Cd3;
		shiwakePatternMaster.kashiUfKotei5Cd3 = this.kashiUfKotei5Cd3;
		shiwakePatternMaster.kashiUfKotei6Cd3 = this.kashiUfKotei6Cd3;
		shiwakePatternMaster.kashiUfKotei7Cd3 = this.kashiUfKotei7Cd3;
		shiwakePatternMaster.kashiUfKotei8Cd3 = this.kashiUfKotei8Cd3;
		shiwakePatternMaster.kashiUfKotei9Cd3 = this.kashiUfKotei9Cd3;
		shiwakePatternMaster.kashiUfKotei10Cd3 = this.kashiUfKotei10Cd3;
		shiwakePatternMaster.kashiKazeiKbn3 = ""; //貸方課税区分は未設定固定
		shiwakePatternMaster.kashiFutanBumonCd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiFutanBumonCd4, inputSeigyo.shiwakeVarListKashi4Bumon);
		shiwakePatternMaster.kashiKamokuCd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKanjyouKamokuCd4, inputSeigyo.shiwakeVarListKashi4Kamoku);
		shiwakePatternMaster.kashiKamokuEdabanCd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKamokuEdabanCd4, inputSeigyo.shiwakeVarListKashi4Edano);
		shiwakePatternMaster.kashiTorihikisakiCd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiTorihikisakiCd4, inputSeigyo.shiwakeVarListKashi4Torihiki);
		shiwakePatternMaster.kashiProjectCd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiProjectCd4, inputSeigyo.shiwakeVarListKashi4Project);
		shiwakePatternMaster.kashiSegmentCd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiSegmentCd4, inputSeigyo.shiwakeVarListKashi4Segment);
		shiwakePatternMaster.kashiUf1Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf1Cd4, inputSeigyo.shiwakeVarListKashi4Uf1);
		shiwakePatternMaster.kashiUf2Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf2Cd4, inputSeigyo.shiwakeVarListKashi4Uf2);
		shiwakePatternMaster.kashiUf3Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf3Cd4, inputSeigyo.shiwakeVarListKashi4Uf3);
		shiwakePatternMaster.kashiUf4Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf4Cd4, inputSeigyo.shiwakeVarListKashi4Uf4);
		shiwakePatternMaster.kashiUf5Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf5Cd4, inputSeigyo.shiwakeVarListKashi4Uf5);
		shiwakePatternMaster.kashiUf6Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf6Cd4, inputSeigyo.shiwakeVarListKashi4Uf6);
		shiwakePatternMaster.kashiUf7Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf7Cd4, inputSeigyo.shiwakeVarListKashi4Uf7);
		shiwakePatternMaster.kashiUf8Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf8Cd4, inputSeigyo.shiwakeVarListKashi4Uf8);
		shiwakePatternMaster.kashiUf9Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf9Cd4, inputSeigyo.shiwakeVarListKashi4Uf9);
		shiwakePatternMaster.kashiUf10Cd4 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf10Cd4, inputSeigyo.shiwakeVarListKashi4Uf10);
		shiwakePatternMaster.kashiUfKotei1Cd4 = this.kashiUfKotei1Cd4;
		shiwakePatternMaster.kashiUfKotei2Cd4 = this.kashiUfKotei2Cd4;
		shiwakePatternMaster.kashiUfKotei3Cd4 = this.kashiUfKotei3Cd4;
		shiwakePatternMaster.kashiUfKotei4Cd4 = this.kashiUfKotei4Cd4;
		shiwakePatternMaster.kashiUfKotei5Cd4 = this.kashiUfKotei5Cd4;
		shiwakePatternMaster.kashiUfKotei6Cd4 = this.kashiUfKotei6Cd4;
		shiwakePatternMaster.kashiUfKotei7Cd4 = this.kashiUfKotei7Cd4;
		shiwakePatternMaster.kashiUfKotei8Cd4 = this.kashiUfKotei8Cd4;
		shiwakePatternMaster.kashiUfKotei9Cd4 = this.kashiUfKotei9Cd4;
		shiwakePatternMaster.kashiUfKotei10Cd4 = this.kashiUfKotei10Cd4;
		shiwakePatternMaster.kashiKazeiKbn4 = ""; //貸方課税区分は未設定固定
		shiwakePatternMaster.kashiFutanBumonCd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiFutanBumonCd5, inputSeigyo.shiwakeVarListKashi5Bumon);
		shiwakePatternMaster.kashiKamokuCd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKanjyouKamokuCd5, inputSeigyo.shiwakeVarListKashi5Kamoku);
		shiwakePatternMaster.kashiKamokuEdabanCd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiKamokuEdabanCd5, inputSeigyo.shiwakeVarListKashi5Edano);
		shiwakePatternMaster.kashiTorihikisakiCd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiTorihikisakiCd5, inputSeigyo.shiwakeVarListKashi5Torihiki);
		shiwakePatternMaster.kashiProjectCd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiProjectCd5, inputSeigyo.shiwakeVarListKashi5Project);
		shiwakePatternMaster.kashiSegmentCd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiSegmentCd5, inputSeigyo.shiwakeVarListKashi5Segment);
		shiwakePatternMaster.kashiUf1Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf1Cd5, inputSeigyo.shiwakeVarListKashi5Uf1);
		shiwakePatternMaster.kashiUf2Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf2Cd5, inputSeigyo.shiwakeVarListKashi5Uf2);
		shiwakePatternMaster.kashiUf3Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf3Cd5, inputSeigyo.shiwakeVarListKashi5Uf3);
		shiwakePatternMaster.kashiUf4Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf4Cd5, inputSeigyo.shiwakeVarListKashi5Uf4);
		shiwakePatternMaster.kashiUf5Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf5Cd5, inputSeigyo.shiwakeVarListKashi5Uf5);
		shiwakePatternMaster.kashiUf6Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf6Cd5, inputSeigyo.shiwakeVarListKashi5Uf6);
		shiwakePatternMaster.kashiUf7Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf7Cd5, inputSeigyo.shiwakeVarListKashi5Uf7);
		shiwakePatternMaster.kashiUf8Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf8Cd5, inputSeigyo.shiwakeVarListKashi5Uf8);
		shiwakePatternMaster.kashiUf9Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf9Cd5, inputSeigyo.shiwakeVarListKashi5Uf9);
		shiwakePatternMaster.kashiUf10Cd5 = this.myLogic.ConvertShiwakeVarNm2Cd(kashiUf10Cd5, inputSeigyo.shiwakeVarListKashi5Uf10);
		shiwakePatternMaster.kashiUfKotei1Cd5 = this.kashiUfKotei1Cd5;
		shiwakePatternMaster.kashiUfKotei2Cd5 = this.kashiUfKotei2Cd5;
		shiwakePatternMaster.kashiUfKotei3Cd5 = this.kashiUfKotei3Cd5;
		shiwakePatternMaster.kashiUfKotei4Cd5 = this.kashiUfKotei4Cd5;
		shiwakePatternMaster.kashiUfKotei5Cd5 = this.kashiUfKotei5Cd5;
		shiwakePatternMaster.kashiUfKotei6Cd5 = this.kashiUfKotei6Cd5;
		shiwakePatternMaster.kashiUfKotei7Cd5 = this.kashiUfKotei7Cd5;
		shiwakePatternMaster.kashiUfKotei8Cd5 = this.kashiUfKotei8Cd5;
		shiwakePatternMaster.kashiUfKotei9Cd5 = this.kashiUfKotei9Cd5;
		shiwakePatternMaster.kashiUfKotei10Cd5 = this.kashiUfKotei10Cd5;
		shiwakePatternMaster.kashiKazeiKbn5 =  ""; //貸方課税区分は未設定固定
		shiwakePatternMaster.tourokuUserId = this.gaibuKoushinUserId != null ? this.gaibuKoushinUserId : super.getUser().getTourokuOrKoushinUserId();
		shiwakePatternMaster.koushinUserId = this.gaibuKoushinUserId != null ? this.gaibuKoushinUserId : super.getUser().getTourokuOrKoushinUserId();
		shiwakePatternMaster.oldKariKazeiKbn =  this.oldKariKazeiKbn;
		shiwakePatternMaster.oldKashiKazeiKbn1 = this.oldKashiKazeiKbn1;
		shiwakePatternMaster.oldKashiKazeiKbn2 = this.oldKashiKazeiKbn2;
		shiwakePatternMaster.oldKashiKazeiKbn3 = this.oldKashiKazeiKbn3;
		shiwakePatternMaster.oldKashiKazeiKbn4 = this.oldKashiKazeiKbn4;
		shiwakePatternMaster.oldKashiKazeiKbn5 = this.oldKashiKazeiKbn5;
		shiwakePatternMaster.kariBunriKbn = this.kariBunriKbn;
		// 貸方分離区分 長さ0の場合は空白、そうでない場合は空白を示す値固定(登録画面からの登録は元々その通り、CSVから入力される値は無視する)
		shiwakePatternMaster.kashiBunriKbn1 = this.kashiBunriKbn1.length() == 0 ? this.kashiBunriKbn1: "9";
		shiwakePatternMaster.kashiBunriKbn2 = this.kashiBunriKbn2.length() == 0 ? this.kashiBunriKbn2: "9";
		shiwakePatternMaster.kashiBunriKbn3 = this.kashiBunriKbn3.length() == 0 ? this.kashiBunriKbn3: "9";
		shiwakePatternMaster.kashiBunriKbn4 = this.kashiBunriKbn4.length() == 0 ? this.kashiBunriKbn4: "9";
		shiwakePatternMaster.kashiBunriKbn5 = this.kashiBunriKbn5.length() == 0 ? this.kashiBunriKbn5: "9";
		shiwakePatternMaster.kariShiireKbn = this.kariShiireKbn;
		shiwakePatternMaster.kashiShiireKbn1 = this.kashiShiireKbn1.length() == 0 ? this.kashiShiireKbn1: "0";
		shiwakePatternMaster.kashiShiireKbn2 = this.kashiShiireKbn2.length() == 0 ? this.kashiShiireKbn2: "0";
		shiwakePatternMaster.kashiShiireKbn3 = this.kashiShiireKbn3.length() == 0 ? this.kashiShiireKbn3: "0";
		shiwakePatternMaster.kashiShiireKbn4 = this.kashiShiireKbn4.length() == 0 ? this.kashiShiireKbn4: "0";
		shiwakePatternMaster.kashiShiireKbn5 = this.kashiShiireKbn5.length() == 0 ? this.kashiShiireKbn5: "0";

		return shiwakePatternMaster;
	}
	
	/**
	 * @param torihiki 取引CSVアップロード情報
	 * @param userId ユーザーID
	 */
	public void setPropertiesFromInfo(IkkatsuTourokuTorihikiCSVUploadInfo torihiki, String userId)
	{
		this.setDenpyouKbn(torihiki.getDenpyouKbn());
		this.setShiwakeEdano(torihiki.getShiwakeEdano());
		this.setDeleteFlg(torihiki.getDeleteFlg());
		this.setYuukouKigenFrom(torihiki.getYuukouKigenFrom());
		this.setYuukouKigenTo(torihiki.getYuukouKigenTo());
		this.setBunrui1(torihiki.getBunrui1());
		this.setBunrui2(torihiki.getBunrui2());
		this.setBunrui3(torihiki.getBunrui3());
		this.setTorihikiNm(torihiki.getTorihikiName());
		this.setTekiyouNyuryokuOn(torihiki.getTekiyouFlg());
		this.setTekiyou(torihiki.getTekiyou());
		this.setDefaultHyoujiOn(torihiki.getDefaultHyoujiFlg());
		this.setKousaihiOn(torihiki.getKousaihiHyoujiFlg());
		this.setNinzuuOn(torihiki.getKousaihiNinzuuRiyouFlg());
		this.setKousaihiKijungaku(torihiki.getKousaihiKijungaku());
		this.setKousaihiCheckHouhou(torihiki.getKousaihiCheckHouhou());
		this.setKousaihiCheckResult(torihiki.getKousaihiCheckResult());
		this.setKakeOn(torihiki.getKakeFlg());
		this.setHyoujijun(torihiki.getHyoujiJun());
		this.setShainCdRenkei(torihiki.getShainCdRenkeiFlg());
		this.setZaimuEdabanRenkei(torihiki.getZaimuEdabanRenkeiFlg());
		this.setKariFutanBumonCd(torihiki.getKariFutanBumonCd());
		this.setKariKanjyouKamokuCd(torihiki.getKariKamokuCd());
		this.setKariKamokuEdabanCd(torihiki.getKariKamokuEdabanCd());
		this.setKariTorihikisakiCd(torihiki.getKariTorihikisakiCd());
		this.setKariProjectCd(torihiki.getKariProjectCd());
		this.setKariSegmentCd(torihiki.getKariSegmentCd());
		this.setKariUf1Cd(torihiki.getKariUf1Cd());
		this.setKariUf2Cd(torihiki.getKariUf2Cd());
		this.setKariUf3Cd(torihiki.getKariUf3Cd());
		this.setKariUf4Cd(torihiki.getKariUf4Cd());
		this.setKariUf5Cd(torihiki.getKariUf5Cd());
		this.setKariUf6Cd(torihiki.getKariUf6Cd());
		this.setKariUf7Cd(torihiki.getKariUf7Cd());
		this.setKariUf8Cd(torihiki.getKariUf8Cd());
		this.setKariUf9Cd(torihiki.getKariUf9Cd());
		this.setKariUf10Cd(torihiki.getKariUf10Cd());
		this.setKariUfKotei1Cd(torihiki.getKariUfKotei1Cd());
		this.setKariUfKotei2Cd(torihiki.getKariUfKotei2Cd());
		this.setKariUfKotei3Cd(torihiki.getKariUfKotei3Cd());
		this.setKariUfKotei4Cd(torihiki.getKariUfKotei4Cd());
		this.setKariUfKotei5Cd(torihiki.getKariUfKotei5Cd());
		this.setKariUfKotei6Cd(torihiki.getKariUfKotei6Cd());
		this.setKariUfKotei7Cd(torihiki.getKariUfKotei7Cd());
		this.setKariUfKotei8Cd(torihiki.getKariUfKotei8Cd());
		this.setKariUfKotei9Cd(torihiki.getKariUfKotei9Cd());
		this.setKariUfKotei10Cd(torihiki.getKariUfKotei10Cd());
		this.setKariKazeiKbn(torihiki.getKariKazeiKbn());
		this.setKariZeiritsu(torihiki.getKariZeiritsu());
		this.setKariBunriKbn(torihiki.getKariBunriKbn());
		this.setKariShiireKbn(torihiki.getKariShiireKbn());
		this.setKariKeigenZeiritsuKbn(torihiki.getKariKeigenZeiritsuKbn());
		this.setKashiFutanBumonCd1(torihiki.getKashiFutanBumonCd1());
		this.setKashiKanjyouKamokuCd1(torihiki.getKashiKamokuCd1());
		this.setKashiKamokuEdabanCd1(torihiki.getKashiKamokuEdabanCd1());
		this.setKashiTorihikisakiCd1(torihiki.getKashiTorihikisakiCd1());
		this.setKashiProjectCd1(torihiki.getKashiProjectCd1());
		this.setKashiSegmentCd1(torihiki.getKashiSegmentCd1());
		this.setKashiUf1Cd1(torihiki.getKashiUf1Cd1());
		this.setKashiUf2Cd1(torihiki.getKashiUf2Cd1());
		this.setKashiUf3Cd1(torihiki.getKashiUf3Cd1());
		this.setKashiUf4Cd1(torihiki.getKashiUf4Cd1());
		this.setKashiUf5Cd1(torihiki.getKashiUf5Cd1());
		this.setKashiUf6Cd1(torihiki.getKashiUf6Cd1());
		this.setKashiUf7Cd1(torihiki.getKashiUf7Cd1());
		this.setKashiUf8Cd1(torihiki.getKashiUf8Cd1());
		this.setKashiUf9Cd1(torihiki.getKashiUf9Cd1());
		this.setKashiUf10Cd1(torihiki.getKashiUf10Cd1());
		this.setKashiUfKotei1Cd1(torihiki.getKashiUfKotei1Cd1());
		this.setKashiUfKotei2Cd1(torihiki.getKashiUfKotei2Cd1());
		this.setKashiUfKotei3Cd1(torihiki.getKashiUfKotei3Cd1());
		this.setKashiUfKotei4Cd1(torihiki.getKashiUfKotei4Cd1());
		this.setKashiUfKotei5Cd1(torihiki.getKashiUfKotei5Cd1());
		this.setKashiUfKotei6Cd1(torihiki.getKashiUfKotei6Cd1());
		this.setKashiUfKotei7Cd1(torihiki.getKashiUfKotei7Cd1());
		this.setKashiUfKotei8Cd1(torihiki.getKashiUfKotei8Cd1());
		this.setKashiUfKotei9Cd1(torihiki.getKashiUfKotei9Cd1());
		this.setKashiUfKotei10Cd1(torihiki.getKashiUfKotei10Cd1());
		this.setKashiKazeiKbn1(""); //貸方課税区分は未設定固定
		this.setKashiBunriKbn1(torihiki.getKashiBunriKbn1());
		this.setKashiShiireKbn1(torihiki.getKashiShiireKbn1());
		this.setKashiFutanBumonCd2(torihiki.getKashiFutanBumonCd2());
		this.setKashiKanjyouKamokuCd2(torihiki.getKashiKamokuCd2());
		this.setKashiKamokuEdabanCd2(torihiki.getKashiKamokuEdabanCd2());
		this.setKashiTorihikisakiCd2(torihiki.getKashiTorihikisakiCd2());
		this.setKashiProjectCd2(torihiki.getKashiProjectCd2());
		this.setKashiSegmentCd2(torihiki.getKashiSegmentCd2());
		this.setKashiUf1Cd2(torihiki.getKashiUf1Cd2());
		this.setKashiUf2Cd2(torihiki.getKashiUf2Cd2());
		this.setKashiUf3Cd2(torihiki.getKashiUf3Cd2());
		this.setKashiUf4Cd2(torihiki.getKashiUf4Cd2());
		this.setKashiUf5Cd2(torihiki.getKashiUf5Cd2());
		this.setKashiUf6Cd2(torihiki.getKashiUf6Cd2());
		this.setKashiUf7Cd2(torihiki.getKashiUf7Cd2());
		this.setKashiUf8Cd2(torihiki.getKashiUf8Cd2());
		this.setKashiUf9Cd2(torihiki.getKashiUf9Cd2());
		this.setKashiUf10Cd2(torihiki.getKashiUf10Cd2());
		this.setKashiUfKotei1Cd2(torihiki.getKashiUfKotei1Cd2());
		this.setKashiUfKotei2Cd2(torihiki.getKashiUfKotei2Cd2());
		this.setKashiUfKotei3Cd2(torihiki.getKashiUfKotei3Cd2());
		this.setKashiUfKotei4Cd2(torihiki.getKashiUfKotei4Cd2());
		this.setKashiUfKotei5Cd2(torihiki.getKashiUfKotei5Cd2());
		this.setKashiUfKotei6Cd2(torihiki.getKashiUfKotei6Cd2());
		this.setKashiUfKotei7Cd2(torihiki.getKashiUfKotei7Cd2());
		this.setKashiUfKotei8Cd2(torihiki.getKashiUfKotei8Cd2());
		this.setKashiUfKotei9Cd2(torihiki.getKashiUfKotei9Cd2());
		this.setKashiUfKotei10Cd2(torihiki.getKashiUfKotei10Cd2());
		this.setKashiKazeiKbn2(""); //貸方課税区分は未設定固定
		this.setKashiBunriKbn2(torihiki.getKashiBunriKbn2());
		this.setKashiShiireKbn2(torihiki.getKashiShiireKbn2());
		this.setKashiFutanBumonCd3(torihiki.getKashiFutanBumonCd3());
		this.setKashiKanjyouKamokuCd3(torihiki.getKashiKamokuCd3());
		this.setKashiKamokuEdabanCd3(torihiki.getKashiKamokuEdabanCd3());
		this.setKashiTorihikisakiCd3(torihiki.getKashiTorihikisakiCd3());
		this.setKashiProjectCd3(torihiki.getKashiProjectCd3());
		this.setKashiSegmentCd3(torihiki.getKashiSegmentCd3());
		this.setKashiUf1Cd3(torihiki.getKashiUf1Cd3());
		this.setKashiUf2Cd3(torihiki.getKashiUf2Cd3());
		this.setKashiUf3Cd3(torihiki.getKashiUf3Cd3());
		this.setKashiUf4Cd3(torihiki.getKashiUf4Cd3());
		this.setKashiUf5Cd3(torihiki.getKashiUf5Cd3());
		this.setKashiUf6Cd3(torihiki.getKashiUf6Cd3());
		this.setKashiUf7Cd3(torihiki.getKashiUf7Cd3());
		this.setKashiUf8Cd3(torihiki.getKashiUf8Cd3());
		this.setKashiUf9Cd3(torihiki.getKashiUf9Cd3());
		this.setKashiUf10Cd3(torihiki.getKashiUf10Cd3());
		this.setKashiUfKotei1Cd3(torihiki.getKashiUfKotei1Cd3());
		this.setKashiUfKotei2Cd3(torihiki.getKashiUfKotei2Cd3());
		this.setKashiUfKotei3Cd3(torihiki.getKashiUfKotei3Cd3());
		this.setKashiUfKotei4Cd3(torihiki.getKashiUfKotei4Cd3());
		this.setKashiUfKotei5Cd3(torihiki.getKashiUfKotei5Cd3());
		this.setKashiUfKotei6Cd3(torihiki.getKashiUfKotei6Cd3());
		this.setKashiUfKotei7Cd3(torihiki.getKashiUfKotei7Cd3());
		this.setKashiUfKotei8Cd3(torihiki.getKashiUfKotei8Cd3());
		this.setKashiUfKotei9Cd3(torihiki.getKashiUfKotei9Cd3());
		this.setKashiUfKotei10Cd3(torihiki.getKashiUfKotei10Cd3());
		this.setKashiKazeiKbn3(""); //貸方課税区分は未設定固定
		this.setKashiBunriKbn3(torihiki.getKashiBunriKbn3());
		this.setKashiShiireKbn3(torihiki.getKashiShiireKbn3());
		this.setKashiFutanBumonCd4(torihiki.getKashiFutanBumonCd4());
		this.setKashiKanjyouKamokuCd4(torihiki.getKashiKamokuCd4());
		this.setKashiKamokuEdabanCd4(torihiki.getKashiKamokuEdabanCd4());
		this.setKashiTorihikisakiCd4(torihiki.getKashiTorihikisakiCd4());
		this.setKashiProjectCd4(torihiki.getKashiProjectCd4());
		this.setKashiSegmentCd4(torihiki.getKashiSegmentCd4());
		this.setKashiUf1Cd4(torihiki.getKashiUf1Cd4());
		this.setKashiUf2Cd4(torihiki.getKashiUf2Cd4());
		this.setKashiUf3Cd4(torihiki.getKashiUf3Cd4());
		this.setKashiUf4Cd4(torihiki.getKashiUf4Cd4());
		this.setKashiUf5Cd4(torihiki.getKashiUf5Cd4());
		this.setKashiUf6Cd4(torihiki.getKashiUf6Cd4());
		this.setKashiUf7Cd4(torihiki.getKashiUf7Cd4());
		this.setKashiUf8Cd4(torihiki.getKashiUf8Cd4());
		this.setKashiUf9Cd4(torihiki.getKashiUf9Cd4());
		this.setKashiUf10Cd4(torihiki.getKashiUf10Cd4());
		this.setKashiUfKotei1Cd4(torihiki.getKashiUfKotei1Cd4());
		this.setKashiUfKotei2Cd4(torihiki.getKashiUfKotei2Cd4());
		this.setKashiUfKotei3Cd4(torihiki.getKashiUfKotei3Cd4());
		this.setKashiUfKotei4Cd4(torihiki.getKashiUfKotei4Cd4());
		this.setKashiUfKotei5Cd4(torihiki.getKashiUfKotei5Cd4());
		this.setKashiUfKotei6Cd4(torihiki.getKashiUfKotei6Cd4());
		this.setKashiUfKotei7Cd4(torihiki.getKashiUfKotei7Cd4());
		this.setKashiUfKotei8Cd4(torihiki.getKashiUfKotei8Cd4());
		this.setKashiUfKotei9Cd4(torihiki.getKashiUfKotei9Cd4());
		this.setKashiUfKotei10Cd4(torihiki.getKashiUfKotei10Cd4());
		this.setKashiKazeiKbn4(""); //貸方課税区分は未設定固定
		this.setKashiBunriKbn4(torihiki.getKashiBunriKbn4());
		this.setKashiShiireKbn4(torihiki.getKashiShiireKbn4());
		this.setKashiFutanBumonCd5(torihiki.getKashiFutanBumonCd5());
		this.setKashiKanjyouKamokuCd5(torihiki.getKashiKamokuCd5());
		this.setKashiKamokuEdabanCd5(torihiki.getKashiKamokuEdabanCd5());
		this.setKashiTorihikisakiCd5(torihiki.getKashiTorihikisakiCd5());
		this.setKashiProjectCd5(torihiki.getKashiProjectCd5());
		this.setKashiSegmentCd5(torihiki.getKashiSegmentCd5());
		this.setKashiUf1Cd5(torihiki.getKashiUf1Cd5());
		this.setKashiUf2Cd5(torihiki.getKashiUf2Cd5());
		this.setKashiUf3Cd5(torihiki.getKashiUf3Cd5());
		this.setKashiUf4Cd5(torihiki.getKashiUf4Cd5());
		this.setKashiUf5Cd5(torihiki.getKashiUf5Cd5());
		this.setKashiUf6Cd5(torihiki.getKashiUf6Cd5());
		this.setKashiUf7Cd5(torihiki.getKashiUf7Cd5());
		this.setKashiUf8Cd5(torihiki.getKashiUf8Cd5());
		this.setKashiUf9Cd5(torihiki.getKashiUf9Cd5());
		this.setKashiUf10Cd5(torihiki.getKashiUf10Cd5());
		this.setKashiUfKotei1Cd5(torihiki.getKashiUfKotei1Cd5());
		this.setKashiUfKotei2Cd5(torihiki.getKashiUfKotei2Cd5());
		this.setKashiUfKotei3Cd5(torihiki.getKashiUfKotei3Cd5());
		this.setKashiUfKotei4Cd5(torihiki.getKashiUfKotei4Cd5());
		this.setKashiUfKotei5Cd5(torihiki.getKashiUfKotei5Cd5());
		this.setKashiUfKotei6Cd5(torihiki.getKashiUfKotei6Cd5());
		this.setKashiUfKotei7Cd5(torihiki.getKashiUfKotei7Cd5());
		this.setKashiUfKotei8Cd5(torihiki.getKashiUfKotei8Cd5());
		this.setKashiUfKotei9Cd5(torihiki.getKashiUfKotei9Cd5());
		this.setKashiUfKotei10Cd5(torihiki.getKashiUfKotei10Cd5());
		this.setKashiKazeiKbn5(""); //貸方課税区分は未設定固定
		this.setKashiBunriKbn5(torihiki.getKashiBunriKbn5());
		this.setKashiShiireKbn5(torihiki.getKashiShiireKbn5());
		this.setGaibuKoushinUserId(userId);
		this.setGaibuYobidashiFlag(true);
	}
	
	/**
	 * インボイス処理開始判定。ここでしか使わないのでbooleanで
	 * @return invoiceStartFlg 0:開始前・1:開始後
	 */
	protected boolean isInvoiceStarted() {
		InvoiceStart invoiceStart = invoiceStartDao.find();
		String invoiceStartFlg = invoiceStart != null ? invoiceStart.invoiceFlg : "0";
		return invoiceStartFlg.equals("1");
	}
}
