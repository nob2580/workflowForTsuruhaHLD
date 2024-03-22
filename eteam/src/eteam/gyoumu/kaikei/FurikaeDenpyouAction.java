package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHOUHYOU_SHORUI;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.FurikaeAbstractDao;
import eteam.database.abstractdao.KiKesnAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.FurikaeDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiKesnDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.Furikae;
import eteam.database.dto.KamokuMaster;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 振替伝票Action
 */
@Getter @Setter @ToString
public class FurikaeDenpyouAction extends WorkflowEventControl {

//＜定数＞

//＜画面入力＞

	//画面入力（申請内容）
	/** 伝票日 */
	String denpyouDate;
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
	/**本体金額 */
	String hontaiKingaku;
	/** 消費税額 */
	String shouhizeigaku;
	/** ユニバーサルフィールド１コード */
	String uf1Cd;
	/** ユニバーサルフィールド１名 */
	String uf1Name;
	/** ユニバーサルフィールド２コード */
	String uf2Cd;
	/** ユニバーサルフィールド２名 */
	String uf2Name;
	/** ユニバーサルフィールド３コード */
	String uf3Cd;
	/** ユニバーサルフィールド３名 */
	String uf3Name;
	/** ユニバーサルフィールド４コード */
	String uf4Cd;
	/** ユニバーサルフィールド４名 */
	String uf4Name;
	/** ユニバーサルフィールド５コード */
	String uf5Cd;
	/** ユニバーサルフィールド５名 */
	String uf5Name;
	/** ユニバーサルフィールド６コード */
	String uf6Cd;
	/** ユニバーサルフィールド６名 */
	String uf6Name;
	/** ユニバーサルフィールド７コード */
	String uf7Cd;
	/** ユニバーサルフィールド７名 */
	String uf7Name;
	/** ユニバーサルフィールド８コード */
	String uf8Cd;
	/** ユニバーサルフィールド８名 */
	String uf8Name;
	/** ユニバーサルフィールド９コード */
	String uf9Cd;
	/** ユニバーサルフィールド９名 */
	String uf9Name;
	/** ユニバーサルフィールド１０コード */
	String uf10Cd;
	/** ユニバーサルフィールド１０名 */
	String uf10Name;
	/** 摘要 */
	String tekiyou;
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 伝票摘要注記 */
	String chuuki2;
	/** 備考 */
	String bikou;

	//借方
	/** 勘定科目コード */
	String kariKamokuCd;
	/** 勘定科目名 */
	String kariKamokuName;
	/** 勘定科目枝番コード */
	String kariKamokuEdabanCd;
	/** 勘定科目枝番名 */
	String kariKamokuEdabanName;
	/** 負担部門選択コード */
	String kariFutanBumonCd;
	/** 負担部門選択名 */
	String kariFutanBumonName;
	/** 取引先コード */
	String kariTorihikisakiCd;
	/** 取引先名 */
	String kariTorihikisakiName;
	/** プロジェクトコード */
	String kariProjectCd;
	/** プロジェクト名 */
	String kariProjectName;
	/** セグメントコード */
	String kariSegmentCd;
	/** セグメント名 */
	String kariSegmentName;
	/** ユニバーサルフィールド1コード */
	String kariUf1Cd;
	/** ユニバーサルフィールド1名 */
	String kariUf1Name;
	/** ユニバーサルフィールド2コード */
	String kariUf2Cd;
	/** ユニバーサルフィールド2名 */
	String kariUf2Name;
	/** ユニバーサルフィールド3コード */
	String kariUf3Cd;
	/** ユニバーサルフィールド3名 */
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
	/** 課税区分 */
	String kariKazeiKbn;
	/** 消費税率 */
	String kariZeiritsu;
	/** 軽減税率区分 */
	String kariKeigenZeiritsuKbn;
	/** 事業者区分 */
	String kariJigyoushaKbn;
	/** 分離区分 */
	String kariBunriKbn;
	/** 課税区分 */
	String kariShiireKbn;
	/** 処理グループ */
	String kariShoriGroup;

	//貸方
	/** 勘定科目コード */
	String kashiKamokuCd;
	/** 勘定科目名 */
	String kashiKamokuName;
	/** 勘定科目枝番コード */
	String kashiKamokuEdabanCd;
	/** 勘定科目枝番名 */
	String kashiKamokuEdabanName;
	/** 負担部門選択コード */
	String kashiFutanBumonCd;
	/** 負担部門選択名 */
	String kashiFutanBumonName;
	/** 取引先コード */
	String kashiTorihikisakiCd;
	/** 取引先名 */
	String kashiTorihikisakiName;
	/** プロジェクトコード */
	String kashiProjectCd;
	/** プロジェクト名 */
	String kashiProjectName;
	/** セグメントコード */
	String kashiSegmentCd;
	/** セグメント名 */
	String kashiSegmentName;
	/** ユニバーサルフィールド1コード */
	String kashiUf1Cd;
	/** ユニバーサルフィールド2名 */
	String kashiUf1Name;
	/** ユニバーサルフィールド2コード */
	String kashiUf2Cd;
	/** ユニバーサルフィールド2名 */
	String kashiUf2Name;
	/** ユニバーサルフィールド3コード */
	String kashiUf3Cd;
	/** ユニバーサルフィールド3名 */
	String kashiUf3Name;
	/** ユニバーサルフィールド４コード */
	String kashiUf4Cd;
	/** ユニバーサルフィールド４名 */
	String kashiUf4Name;
	/** ユニバーサルフィールド５コード */
	String kashiUf5Cd;
	/** ユニバーサルフィールド５名 */
	String kashiUf5Name;
	/** ユニバーサルフィールド６コード */
	String kashiUf6Cd;
	/** ユニバーサルフィールド６名 */
	String kashiUf6Name;
	/** ユニバーサルフィールド７コード */
	String kashiUf7Cd;
	/** ユニバーサルフィールド７名 */
	String kashiUf7Name;
	/** ユニバーサルフィールド８コード */
	String kashiUf8Cd;
	/** ユニバーサルフィールド８名 */
	String kashiUf8Name;
	/** ユニバーサルフィールド９コード */
	String kashiUf9Cd;
	/** ユニバーサルフィールド９名 */
	String kashiUf9Name;
	/** ユニバーサルフィールド１０コード */
	String kashiUf10Cd;
	/** ユニバーサルフィールド１０名 */
	String kashiUf10Name;
	/** 課税区分 */
	String kashiKazeiKbn;
	/** 消費税率 */
	String kashiZeiritsu;
	/** 軽減税率区分 */
	String kashiKeigenZeiritsuKbn;
	/** 事業者区分 */
	String kashiJigyoushaKbn;
	/** 分離区分 */
	String kashiBunriKbn;
	/** 仕入区分 */
	String kashiShiireKbn;
	/**	借方税額計算方式 */
	String kariZeigakuHoushiki;
	/**	貸方税額計算方式 */
	String kashiZeigakuHoushiki;
	/** 処理グループ */
	String kashiShoriGroup;

//＜画面入力以外＞

	//プルダウン等の候補値
	/** 消費税率DropDownList */
	List<GMap> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<GMap> kazeiKbnList;
	/** 課税区分(借方)ドメイン */
	String[] kazeiKbnDomain;
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
	/** 税額計算方式DropDownList */
	List<GMap> zeigakuKeisanList;
	/** 税額計算方式ドメイン */
	String[] zeigakuKeisanDomain;

	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.FURIKAE_DENPYOU);
	/** 決算期Dao */
	KiKesnAbstractDao kiKesnDao;
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;
	/** 税率Dao */
	ShouhizeiritsuAbstractDao zeiritsuDao;

	/** 入力モード */
	boolean enableInput;

//＜部品＞
	//Logic系面倒なので最初にnewしとく
	/** 振替伝票ロジック */
	FurikaeAbstractDao furikaeDao;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	

//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
		// 項目										//項目名							 					//キー項目？
		checkDate (denpyouDate, ks.denpyouDate.getName(), false);
		checkDomain (shouhyouShoruiFlg, EteamConst.Domain.FLG, ks.shouhyouShoruiFlg.getName(), false);
		checkKingakuOver1 (kingaku, ks.kingaku.getName(), false);
		checkString (hf1Cd,1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf1Name,1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf2Cd,1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf2Name,1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf3Cd,1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString (hf3Name,1, 20, hfUfSeigyo.getHf3Name(), false);
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

		//借方
		// 項目										//項目名												//キー項目
		checkString (kariKamokuCd,1,6, "借方" + ks.kamoku.getName() + "コード", false);
		checkString (kariKamokuName,1,22, "借方" + ks.kamoku.getName() + "名", false);
		checkString (kariKamokuEdabanCd,1,12, "借方" + ks.kamokuEdaban.getName() + "コード", false);
		checkString (kariKamokuEdabanName,1,20, "借方" + ks.kamokuEdaban.getName() + "名", false);
		checkString (kariFutanBumonCd,1,8, "借方" + ks.futanBumon.getName() + "コード", false);
		checkString (kariFutanBumonName,1,20, "借方" + ks.futanBumon.getName() + "名", false);
		checkString (kariTorihikisakiCd,1,12, "借方" + ks.torihikisaki.getName() + "コード", false);
		checkString (kariTorihikisakiName,1,44, "借方" + ks.torihikisaki.getName() + "名", false);
		checkString (kariProjectCd, 1, 12, "借方" + ks.project.getName() + "コード", false);
		checkString (kariProjectName, 1, 20, "借方" + ks.project.getName() + "名", false);
		checkString (kariSegmentCd, 1, 8, "借方" + ks.segment.getName() + "コード", false);
		checkString (kariSegmentName, 1, 20, "借方" + ks.segment.getName() + "名", false);
		checkString(kariUf1Cd, 1, 20, "借方" + hfUfSeigyo.getUf1Name(), false);
		checkString(kariUf1Name, 1, 20, "借方" + hfUfSeigyo.getUf1Name(), false);
		checkString(kariUf2Cd, 1, 20, "借方" + hfUfSeigyo.getUf2Name(), false);
		checkString(kariUf2Name, 1, 20, "借方" + hfUfSeigyo.getUf2Name(), false);
		checkString(kariUf3Cd, 1, 20, "借方" + hfUfSeigyo.getUf3Name(), false);
		checkString(kariUf3Name, 1, 20, "借方" + hfUfSeigyo.getUf3Name(), false);
		checkString(kariUf4Cd, 1, 20, "借方" + hfUfSeigyo.getUf4Name(), false);
		checkString(kariUf4Name, 1, 20, "借方" + hfUfSeigyo.getUf4Name(), false);
		checkString(kariUf5Cd, 1, 20, "借方" + hfUfSeigyo.getUf5Name(), false);
		checkString(kariUf5Name, 1, 20, "借方" + hfUfSeigyo.getUf5Name(), false);
		checkString(kariUf6Cd, 1, 20, "借方" + hfUfSeigyo.getUf6Name(), false);
		checkString(kariUf6Name, 1, 20, "借方" + hfUfSeigyo.getUf6Name(), false);
		checkString(kariUf7Cd, 1, 20, "借方" + hfUfSeigyo.getUf7Name(), false);
		checkString(kariUf7Name, 1, 20, "借方" + hfUfSeigyo.getUf7Name(), false);
		checkString(kariUf8Cd, 1, 20, "借方" + hfUfSeigyo.getUf8Name(), false);
		checkString(kariUf8Name, 1, 20, "借方" + hfUfSeigyo.getUf8Name(), false);
		checkString(kariUf9Cd, 1, 20, "借方" + hfUfSeigyo.getUf9Name(), false);
		checkString(kariUf9Name, 1, 20, "借方" + hfUfSeigyo.getUf9Name(), false);
		checkString(kariUf10Cd, 1, 20, "借方" + hfUfSeigyo.getUf10Name(), false);
		checkString(kariUf10Name, 1, 20, "借方" + hfUfSeigyo.getUf10Name(), false);
		checkDomain(kariKazeiKbn,kazeiKbnDomain, "借方" + ks.kazeiKbn.getName(), false);
		checkDomain(kariJigyoushaKbn, jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
		checkDomain(kariZeigakuHoushiki, zeigakuKeisanDomain, ks.uriagezeigakuKeisan.getName(), false);
		checkDomain(kariBunriKbn, bunriKbnDomain, ks.bunriKbn.getName(), false);
		checkDomain(kariShiireKbn, shiireKbnDomain, ks.shiireKbn.getName(),false);

		//貸方
		// 項目										//項目名												//キー項目
		checkString (kashiKamokuCd,1,6, "貸方" + ks.kamoku.getName() + "コード", false);
		checkString (kashiKamokuName,1,22, "貸方" + ks.kamoku.getName() + "名", false);
		checkString (kashiKamokuEdabanCd,1,12, "貸方" + ks.kamokuEdaban.getName() + "コード", false);
		checkString (kashiKamokuEdabanName,1,20, "貸方" + ks.kamokuEdaban.getName() + "名", false);
		checkString (kashiFutanBumonCd,1,8, "貸方" + ks.futanBumon.getName() + "コード", false);
		checkString (kashiFutanBumonName,1,20, "貸方" + ks.futanBumon.getName() + "名", false);
		checkString (kashiTorihikisakiCd,1,12, "貸方" + ks.torihikisaki.getName() + "コード", false);
		checkString (kashiTorihikisakiName,1,44, "貸方" + ks.torihikisaki.getName() + "名", false);
		checkString (kashiProjectCd, 1, 12, "貸方" + ks.project.getName() + "コード", false);
		checkString (kashiProjectName, 1, 20, "貸方" + ks.project.getName() + "名", false);
		checkString (kashiSegmentCd, 1, 12, "貸方" + ks.segment.getName() + "コード", false);
		checkString (kashiSegmentName, 1, 20, "貸方" + ks.segment.getName() + "名", false);
		checkString(kashiUf1Cd, 1, 20, "貸方" + hfUfSeigyo.getUf1Name(), false);
		checkString(kashiUf1Name, 1, 20, "貸方" + hfUfSeigyo.getUf1Name(), false);
		checkString(kashiUf2Cd, 1, 20, "貸方" + hfUfSeigyo.getUf2Name(), false);
		checkString(kashiUf2Name, 1, 20, "貸方" + hfUfSeigyo.getUf2Name(), false);
		checkString(kashiUf3Cd, 1, 20, "貸方" + hfUfSeigyo.getUf3Name(), false);
		checkString(kashiUf3Name, 1, 20, "貸方" + hfUfSeigyo.getUf3Name(), false);
		checkString(kashiUf4Cd, 1, 20, "貸方" + hfUfSeigyo.getUf4Name(), false);
		checkString(kashiUf4Name, 1, 20, "貸方" + hfUfSeigyo.getUf4Name(), false);
		checkString(kashiUf5Cd, 1, 20, "貸方" + hfUfSeigyo.getUf5Name(), false);
		checkString(kashiUf5Name, 1, 20, "貸方" + hfUfSeigyo.getUf5Name(), false);
		checkString(kashiUf6Cd, 1, 20, "貸方" + hfUfSeigyo.getUf6Name(), false);
		checkString(kashiUf6Name, 1, 20, "貸方" + hfUfSeigyo.getUf6Name(), false);
		checkString(kashiUf7Cd, 1, 20, "貸方" + hfUfSeigyo.getUf7Name(), false);
		checkString(kashiUf7Name, 1, 20, "貸方" + hfUfSeigyo.getUf7Name(), false);
		checkString(kashiUf8Cd, 1, 20, "貸方" + hfUfSeigyo.getUf8Name(), false);
		checkString(kashiUf8Name, 1, 20, "貸方" + hfUfSeigyo.getUf8Name(), false);
		checkString(kashiUf9Cd, 1, 20, "貸方" + hfUfSeigyo.getUf9Name(), false);
		checkString(kashiUf9Name, 1, 20, "貸方" + hfUfSeigyo.getUf9Name(), false);
		checkString(kashiUf10Cd, 1, 20, "貸方" + hfUfSeigyo.getUf10Name(), false);
		checkString(kashiUf10Name, 1, 20, "貸方" + hfUfSeigyo.getUf10Name(), false);
		checkDomain (kashiKazeiKbn,kazeiKbnDomain, "貸方" + ks.kazeiKbn.getName(), false);
		checkDomain(kashiJigyoushaKbn, jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
		checkDomain(kashiZeigakuHoushiki, zeigakuKeisanDomain, ks.uriagezeigakuKeisan.getName(), false);
		checkDomain(kashiBunriKbn, bunriKbnDomain, ks.bunriKbn.getName(), false);
		checkDomain(kashiShiireKbn, shiireKbnDomain, ks.shiireKbn.getName(),false);
		checkString(tekiyou,1,this.getIntTekiyouMaxLength(), ks.tekiyou.getName(), false);
		checkSJIS (tekiyou, ks.tekiyou.getName());
		checkString (bikou,1,40, ks.bikou.getName(), false);
	}

	/**
	 * 伝票内部項目の必須チェック
	 */
	protected void denpyouHissuCheck() {
		// E1=登録・更新
		String[][] list = {
			//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouDate, ks.denpyouDate.getName(), ks.denpyouDate.getHissuFlgS()},
			{shouhyouShoruiFlg, ks.shouhyouShoruiFlg.getName(), ks.shouhyouShoruiFlg.getHissuFlgS()},
			{kingaku, ks.kingaku.getName(), ks.kingaku.getHissuFlgS()},
			{kariKamokuCd, "借方" + ks.kamoku.getName() + "コード", ks.kamoku.getHissuFlgS()},
			{kariKamokuName, "借方" + ks.kamoku.getName() + "名", ks.kamoku.getHissuFlgS()},
			{kariKazeiKbn, "借方" + ks.kazeiKbn.getName() + "コード", ks.kazeiKbn.getHissuFlgS()},
			{kariKamokuEdabanCd, "借方" + ks.kamokuEdaban.getName() + "コード", ks.kamokuEdaban.getHissuFlgS()},
			{kariKamokuEdabanName, "借方" + ks.kamokuEdaban.getName() + "名", ks.kamokuEdaban.getHissuFlgS()},
			{kariFutanBumonCd, "借方" + ks.futanBumon.getName() + "コード", ks.futanBumon.getHissuFlgS()},
			{kariFutanBumonName, "借方" + ks.futanBumon.getName() + "名", ks.futanBumon.getHissuFlgS()},
			{kariTorihikisakiCd, "借方" + ks.torihikisaki.getName() + "コード", ks.torihikisaki.getHissuFlgS()},
			{kariTorihikisakiName, "借方" + ks.torihikisaki.getName() + "名", ks.torihikisaki.getHissuFlgS()},
			{kashiKamokuCd, "貸方" + ks.kamoku.getName() + "コード", ks.kamoku.getHissuFlgS()},
			{kashiKamokuName, "貸方" + ks.kamoku.getName() + "名", ks.kamoku.getHissuFlgS()},
			{kashiKazeiKbn, "貸方" + ks.kazeiKbn.getName() + "コード", ks.kazeiKbn.getHissuFlgS()},
			{kashiKamokuEdabanCd, "貸方" + ks.kamokuEdaban.getName() + "コード", ks.kamokuEdaban.getHissuFlgS()},
			{kashiKamokuEdabanName, "貸方" + ks.kamokuEdaban.getName() + "名", ks.kamokuEdaban.getHissuFlgS()},
			{kashiFutanBumonCd, "貸方" + ks.futanBumon.getName() + "コード", ks.futanBumon.getHissuFlgS()},
			{kashiFutanBumonName, "貸方" + ks.futanBumon.getName() + "名", ks.futanBumon.getHissuFlgS()},
			{kashiTorihikisakiCd, "貸方" + ks.torihikisaki.getName() + "コード", ks.torihikisaki.getHissuFlgS()},
			{kashiTorihikisakiName, "貸方" + ks.torihikisaki.getName() + "名", ks.torihikisaki.getHissuFlgS()},
			{tekiyou, ks.tekiyou.getName(), ks.tekiyou.getHissuFlgS()},
			{bikou, ks.bikou.getName(), ks.bikou.getHissuFlgS()},
		};
		hissuCheckCommon(list,1);

		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード"	, ks.hf1.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード"	, ks.hf2.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード"	, ks.hf3.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード" , ks.hf4.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード" , ks.hf5.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード" , ks.hf6.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード" , ks.hf7.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード" , ks.hf8.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード" , ks.hf9.getHissuFlgS(), "0"}}, 1);
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード" , ks.hf10.getHissuFlgS(), "0"}}, 1);

		if (!hfUfSeigyo.getUf1ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf1Cd, "借方" + hfUfSeigyo.getUf1Name() + "コード", ks.uf1.getHissuFlgS(), "0" },
					{ kashiUf1Cd, "貸方" + hfUfSeigyo.getUf1Name() + "コード", ks.uf1.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf2ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf2Cd, "借方" + hfUfSeigyo.getUf2Name() + "コード", ks.uf2.getHissuFlgS(), "0" },
					{ kashiUf2Cd, "貸方" + hfUfSeigyo.getUf2Name() + "コード", ks.uf2.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf3ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf3Cd, "借方" + hfUfSeigyo.getUf3Name() + "コード", ks.uf3.getHissuFlgS(), "0" },
					{ kashiUf3Cd, "貸方" + hfUfSeigyo.getUf3Name() + "コード", ks.uf3.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf4ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf4Cd, "借方" + hfUfSeigyo.getUf4Name() + "コード", ks.uf4.getHissuFlgS(), "0" },
					{ kashiUf4Cd, "貸方" + hfUfSeigyo.getUf4Name() + "コード", ks.uf4.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf5ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf5Cd, "借方" + hfUfSeigyo.getUf5Name() + "コード", ks.uf5.getHissuFlgS(), "0" },
					{ kashiUf5Cd, "貸方" + hfUfSeigyo.getUf5Name() + "コード", ks.uf5.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf6ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf6Cd, "借方" + hfUfSeigyo.getUf6Name() + "コード", ks.uf6.getHissuFlgS(), "0" },
					{ kashiUf6Cd, "貸方" + hfUfSeigyo.getUf6Name() + "コード", ks.uf6.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf7ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf7Cd, "借方" + hfUfSeigyo.getUf7Name() + "コード", ks.uf7.getHissuFlgS(), "0" },
					{ kashiUf7Cd, "貸方" + hfUfSeigyo.getUf7Name() + "コード", ks.uf7.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf8ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf8Cd, "借方" + hfUfSeigyo.getUf8Name() + "コード", ks.uf8.getHissuFlgS(), "0" },
					{ kashiUf8Cd, "貸方" + hfUfSeigyo.getUf8Name() + "コード", ks.uf8.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf9ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf9Cd, "借方" + hfUfSeigyo.getUf9Name() + "コード", ks.uf9.getHissuFlgS(), "0" },
					{ kashiUf9Cd, "貸方" + hfUfSeigyo.getUf9Name() + "コード", ks.uf9.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if (!hfUfSeigyo.getUf10ShiyouFlg().equals("0")) {
			list = new String[][] {
					{ kariUf10Cd, "借方" + hfUfSeigyo.getUf10Name() + "コード", ks.uf10.getHissuFlgS(), "0" },
					{ kashiUf10Cd, "貸方" + hfUfSeigyo.getUf10Name() + "コード", ks.uf10.getHissuFlgS(), "0" }, };
			hissuCheckCommon(list, 1);
		}

		if( !pjShiyouFlg.equals("0") && ks.project.getHyoujiFlg() ) {
			list = new String[][]{
				{kariProjectCd, "借方" + ks.project.getName() + "コード", ks.project.getHissuFlgS()},
				{kariProjectName, "借方" + ks.project.getName() + "名", ks.project.getHissuFlgS()},
				{kashiProjectCd, "貸方" + ks.project.getName() + "コード", ks.project.getHissuFlgS()},
				{kashiProjectName, "貸方" + ks.project.getName() + "名", ks.project.getHissuFlgS()},
			};
			hissuCheckCommon(list, 1);
		}

		if( !segmentShiyouFlg.equals("0") && ks.segment.getHyoujiFlg() ) {
			list = new String[][]{
				{kariSegmentCd, "借方" + ks.segment.getName() + "コード", ks.segment.getHissuFlgS()},
				{kariSegmentName, "借方" + ks.segment.getName() + "名", ks.segment.getHissuFlgS()},
				{kashiSegmentCd, "貸方" + ks.segment.getName() + "コード", ks.segment.getHissuFlgS()},
				{kashiSegmentName, "貸方" + ks.segment.getName() + "名", ks.segment.getHissuFlgS()},
			};
			hissuCheckCommon(list, 1);
		}
		
		// 貸借課税区分で、nullになるケース（貸倒などの未対応課税区分）ついて対策する
		if(isEmpty(this.kariKazeiKbn)) {
			this.kariKazeiKbn = "";
		}
		if(isEmpty(this.kashiKazeiKbn)) {
			this.kashiKazeiKbn = "";
		}
		
		if( getUriagezeigakuKeisan().equals("2") && invoiceDenpyou.equals("0")) {
			if(List.of("4", "9").contains(kariShoriGroup) && List.of("001", "002").contains(kariKazeiKbn)) {
				list = new String[][]{
					{kariZeigakuHoushiki, "借方" + ks.uriagezeigakuKeisan.getName() + "コード", "1"},
				};
				hissuCheckCommon(list, 1);
			}
			if(List.of("4", "9").contains(kashiShoriGroup) && List.of("001", "002").contains(kashiKazeiKbn)) {
				list = new String[][]{
					{kashiZeigakuHoushiki, "借方" + ks.uriagezeigakuKeisan.getName() + "コード", "1"},
				};
				hissuCheckCommon(list, 1);
			}
		}
		if(invoiceDenpyou.equals("0")) {
			if(List.of("2", "5","6","7","8","10").contains(kariShoriGroup) && List.of("001", "002","011","013").contains(kariKazeiKbn)) {
				list = new String[][]{
					{kariJigyoushaKbn, "借方" + ks.jigyoushaKbn.getName() + "コード", "1"},
				};
				hissuCheckCommon(list, 1);
			}
			if(List.of("2", "5","6","7","8","10").contains(kashiShoriGroup) && List.of("001", "002","011","013").contains(kashiKazeiKbn)) {
				list = new String[][]{
					{kashiJigyoushaKbn, "借方" + ks.jigyoushaKbn.getName() + "コード", "1"},
				};
				hissuCheckCommon(list, 1);
			}
		}
	}

	/**
	 * 必須チェック・形式チェック以外の入力チェック=相関チェック
	 * @param connection コネクション
	 */
	protected void soukanCheck(EteamConnection connection) {

		// 伝票HF部チェック
		ShiwakeCheckData shiwakeCheckDataNaiyou = commonLogic.new ShiwakeCheckData() ;
		shiwakeCheckDataNaiyou.hf1Nm = hfUfSeigyo.getHf1Name();
		shiwakeCheckDataNaiyou.hf1Cd = hf1Cd;
		shiwakeCheckDataNaiyou.hf2Nm = hfUfSeigyo.getHf2Name();
		shiwakeCheckDataNaiyou.hf2Cd = hf2Cd;
		shiwakeCheckDataNaiyou.hf3Nm = hfUfSeigyo.getHf3Name();
		shiwakeCheckDataNaiyou.hf3Cd = hf3Cd;
		shiwakeCheckDataNaiyou.hf4Nm   = hfUfSeigyo.getHf4Name();
		shiwakeCheckDataNaiyou.hf4Cd   = hf4Cd;
		shiwakeCheckDataNaiyou.hf5Nm   = hfUfSeigyo.getHf5Name();
		shiwakeCheckDataNaiyou.hf5Cd   = hf5Cd;
		shiwakeCheckDataNaiyou.hf6Nm   = hfUfSeigyo.getHf6Name();
		shiwakeCheckDataNaiyou.hf6Cd   = hf6Cd;
		shiwakeCheckDataNaiyou.hf7Nm   = hfUfSeigyo.getHf7Name();
		shiwakeCheckDataNaiyou.hf7Cd   = hf7Cd;
		shiwakeCheckDataNaiyou.hf8Nm   = hfUfSeigyo.getHf8Name();
		shiwakeCheckDataNaiyou.hf8Cd   = hf8Cd;
		shiwakeCheckDataNaiyou.hf9Nm   = hfUfSeigyo.getHf9Name();
		shiwakeCheckDataNaiyou.hf9Cd   = hf9Cd;
		shiwakeCheckDataNaiyou.hf10Nm   = hfUfSeigyo.getHf10Name();
		shiwakeCheckDataNaiyou.hf10Cd   = hf10Cd;
		commonLogic.checkShiwake(denpyouKbn, shiwakeCheckDataNaiyou, errorList);

		// 共通機能（会計共通）．会計項目入力チェック
		// 借方
		KaikeiCommonLogic.ShiwakeCheckData shiwakeCheckData1 = new KaikeiCommonLogic().new ShiwakeCheckData() ;
		shiwakeCheckData1.shiwakeEdaNoNm  = null;
		shiwakeCheckData1.shiwakeEdaNo  = null;
		shiwakeCheckData1.torihikisakiNm  = "借方" + ks.torihikisaki.getName() +"コード";
		shiwakeCheckData1.torihikisakiCd  = kariTorihikisakiCd;
		shiwakeCheckData1.kamokuNm  = "借方" + ks.kamoku.getName() +"コード";
		shiwakeCheckData1.kamokuCd  = kariKamokuCd;
		shiwakeCheckData1.kamokuEdabanNm  = "借方" + ks.kamokuEdaban.getName() +"コード";
		shiwakeCheckData1.kamokuEdabanCd  = kariKamokuEdabanCd;
		shiwakeCheckData1.futanBumonNm  = "借方" + ks.futanBumon.getName() +"コード";
		shiwakeCheckData1.futanBumonCd  = kariFutanBumonCd;
		shiwakeCheckData1.kazeiKbnNm  = "借方" + ks.kazeiKbn.getName()+"コード";
		shiwakeCheckData1.kazeiKbn  = kariKazeiKbn;
		shiwakeCheckData1.bunriKbnNm  = "借方" + ks.bunriKbn.getName()+"コード";
		shiwakeCheckData1.bunriKbn  = kariBunriKbn;
		shiwakeCheckData1.shiireKbnNm  = "借方" + ks.shiireKbn.getName()+"コード";
		shiwakeCheckData1.shiireKbn  = kariShiireKbn;
		shiwakeCheckData1.projectNm  = "借方" + ks.project.getName() +"コード";
		shiwakeCheckData1.projectCd  = kariProjectCd;
		shiwakeCheckData1.segmentNm  = "借方" + ks.segment.getName() +"コード";
		shiwakeCheckData1.segmentCd  = kariSegmentCd;
		shiwakeCheckData1.uf1Nm = "借方" + hfUfSeigyo.getUf1Name() + "コード";
		shiwakeCheckData1.uf1Cd = kariUf1Cd;
		shiwakeCheckData1.uf2Nm = "借方" + hfUfSeigyo.getUf2Name() + "コード";
		shiwakeCheckData1.uf2Cd = kariUf2Cd;
		shiwakeCheckData1.uf3Nm = "借方" + hfUfSeigyo.getUf3Name() + "コード";
		shiwakeCheckData1.uf3Cd = kariUf3Cd;
		shiwakeCheckData1.uf4Nm = "借方" + hfUfSeigyo.getUf4Name() + "コード";
		shiwakeCheckData1.uf4Cd = kariUf4Cd;
		shiwakeCheckData1.uf5Nm = "借方" + hfUfSeigyo.getUf5Name() + "コード";
		shiwakeCheckData1.uf5Cd = kariUf5Cd;
		shiwakeCheckData1.uf6Nm = "借方" + hfUfSeigyo.getUf6Name() + "コード";
		shiwakeCheckData1.uf6Cd = kariUf6Cd;
		shiwakeCheckData1.uf7Nm = "借方" + hfUfSeigyo.getUf7Name() + "コード";
		shiwakeCheckData1.uf7Cd = kariUf7Cd;
		shiwakeCheckData1.uf8Nm = "借方" + hfUfSeigyo.getUf8Name() + "コード";
		shiwakeCheckData1.uf8Cd = kariUf8Cd;
		shiwakeCheckData1.uf9Nm = "借方" + hfUfSeigyo.getUf9Name() + "コード";
		shiwakeCheckData1.uf9Cd = kariUf9Cd;
		shiwakeCheckData1.uf10Nm = "借方" + hfUfSeigyo.getUf10Name() + "コード";
		shiwakeCheckData1.uf10Cd = kariUf10Cd;
		commonLogic.checkShiwake(denpyouKbn, shiwakeCheckData1, errorList);

		// 貸方
		KaikeiCommonLogic.ShiwakeCheckData shiwakeCheckData2 = new KaikeiCommonLogic().new ShiwakeCheckData() ;
		shiwakeCheckData2.shiwakeEdaNoNm  = null;
		shiwakeCheckData2.shiwakeEdaNo  = null;
		shiwakeCheckData2.torihikisakiNm  = "貸方" + ks.torihikisaki.getName() +"コード";
		shiwakeCheckData2.torihikisakiCd  = kashiTorihikisakiCd;
		shiwakeCheckData2.kamokuNm  = "貸方" + ks.kamoku.getName() +"コード";
		shiwakeCheckData2.kamokuCd  = kashiKamokuCd;
		shiwakeCheckData2.kamokuEdabanNm  = "貸方" + ks.kamokuEdaban.getName() +"コード";
		shiwakeCheckData2.kamokuEdabanCd  = kashiKamokuEdabanCd;
		shiwakeCheckData2.futanBumonNm  = "貸方" + ks.futanBumon.getName() +"コード";
		shiwakeCheckData2.futanBumonCd  = kashiFutanBumonCd;
		shiwakeCheckData2.kazeiKbnNm  = "貸方" + ks.kazeiKbn.getName()+"コード";
		shiwakeCheckData2.kazeiKbn  = kashiKazeiKbn;
		shiwakeCheckData1.bunriKbnNm  = "貸方" + ks.bunriKbn.getName()+"コード";
		shiwakeCheckData1.bunriKbn  = kashiBunriKbn;
		shiwakeCheckData1.shiireKbnNm  = "貸方" + ks.shiireKbn.getName()+"コード";
		shiwakeCheckData1.shiireKbn  = kariShiireKbn;
		shiwakeCheckData2.projectNm  = "貸方" + ks.project.getName() +"コード";
		shiwakeCheckData2.projectCd  = kashiProjectCd;
		shiwakeCheckData2.segmentNm  = "貸方" + ks.segment.getName() +"コード";
		shiwakeCheckData2.segmentCd  = kashiSegmentCd;
		shiwakeCheckData2.uf1Nm = "貸方" + hfUfSeigyo.getUf1Name() + "コード";
		shiwakeCheckData2.uf1Cd = kashiUf1Cd;
		shiwakeCheckData2.uf2Nm = "貸方" + hfUfSeigyo.getUf2Name() + "コード";
		shiwakeCheckData2.uf2Cd = kashiUf2Cd;
		shiwakeCheckData2.uf3Nm = "貸方" + hfUfSeigyo.getUf3Name() + "コード";
		shiwakeCheckData2.uf3Cd = kashiUf3Cd;
		shiwakeCheckData2.uf4Nm = "貸方" + hfUfSeigyo.getUf4Name() + "コード";
		shiwakeCheckData2.uf4Cd = kashiUf4Cd;
		shiwakeCheckData2.uf5Nm = "貸方" + hfUfSeigyo.getUf5Name() + "コード";
		shiwakeCheckData2.uf5Cd = kashiUf5Cd;
		shiwakeCheckData2.uf6Nm = "貸方" + hfUfSeigyo.getUf6Name() + "コード";
		shiwakeCheckData2.uf6Cd = kashiUf6Cd;
		shiwakeCheckData2.uf7Nm = "貸方" + hfUfSeigyo.getUf7Name() + "コード";
		shiwakeCheckData2.uf7Cd = kashiUf7Cd;
		shiwakeCheckData2.uf8Nm = "貸方" + hfUfSeigyo.getUf8Name() + "コード";
		shiwakeCheckData2.uf8Cd = kashiUf8Cd;
		shiwakeCheckData2.uf9Nm = "貸方" + hfUfSeigyo.getUf9Name() + "コード";
		shiwakeCheckData2.uf9Cd = kashiUf9Cd;
		shiwakeCheckData2.uf10Nm = "貸方" + hfUfSeigyo.getUf10Name() + "コード";
		shiwakeCheckData2.uf10Cd = kashiUf10Cd;
		commonLogic.checkShiwake(denpyouKbn, shiwakeCheckData2, errorList);

		// 取引先仕入先チェック
		super.checkShiiresaki("借方" + ks.torihikisaki.getName() +"コード", kariTorihikisakiCd, denpyouKbn);
		super.checkShiiresaki("貸方" + ks.torihikisaki.getName() +"コード", kashiTorihikisakiCd, denpyouKbn);
		
		//消費税率入力チェック
		if(isNotEmpty(kariKamokuCd)) {
			if(List.of("001", "002", "011","012", "013","014").contains(kariKazeiKbn) && kariZeiritsu == null){
				errorList.add("借方消費税率を入力してください。");
			}
		}
		if(isNotEmpty(kashiKamokuCd)) {
			if(List.of("001", "002", "011","012", "013","014").contains(kashiKazeiKbn) && kashiZeiritsu == null){
				errorList.add("貸方消費税率を入力してください。");
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
		BumonUserKanriCategoryLogic bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		GMap userInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");

		//新規起票時の表示状態作成
		if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {
			makeDefaultShinsei(connection);
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

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			Furikae shinseiData = this.furikaeDao.find(denpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			Furikae shinseiData = this.furikaeDao.find(sanshouDenpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);
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
			denpyouDate = "";
		}
		//課税区分の制御
		if (isNotEmpty(kariKamokuCd) || isNotEmpty(kashiKamokuCd)) {
			KamokuMaster karikmk = kamokuMasterDao.find(kariKamokuCd);
			KamokuMaster kashikmk = kamokuMasterDao.find(kashiKamokuCd);
			if (karikmk != null) {
				kariShoriGroup = karikmk.shoriGroup.toString();
			}
			if (kashikmk != null) {
				kashiShoriGroup = kashikmk.shoriGroup.toString();
			}
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

		//相関チェック
		soukanCheck(connection);
		if (0 <errorList.size())
		{
			return;
		}
	}

	//更新ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		//マスター等から名称は引き直す
		reloadMaster(connection);

		//登録
		Furikae furikae = this.createFurikae();
		this.furikaeDao.insert(furikae, furikae.koushinUserId); // 本当はユーザーIDもdtaに入れられるので一引数で良いのだけど、一応
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

		//相関チェック
		soukanCheck(connection);
		if (0 <errorList.size())
		{
			return;
		}

		//マスター等から名称は引き直す
		reloadMaster(connection);

		// 更新
		Furikae furikae = this.createFurikae();
		this.furikaeDao.update(furikae, furikae.tourokuUserId); // 本当はユーザーIDもdtaに入れられるので一引数で良いのだけど、一応
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
		return denpyouDate;
	}
//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		this.furikaeDao = EteamContainer.getComponent(FurikaeDao.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.kiKesnDao = EteamContainer.getComponent(KiKesnDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
	}
	/**
	 * 未登録の時、デフォルトの申請内容表示状態に項目セットする。
	 * @param connection コネクション
	 */
	protected void makeDefaultShinsei(EteamConnection connection) {
		kariZeiritsu = masterLogic.findValidShouhizeiritsuMap().getString("zeiritsu");
		kariKeigenZeiritsuKbn = masterLogic.findValidShouhizeiritsuMap().getString("keigen_zeiritsu_kbn");
		kashiZeiritsu = masterLogic.findValidShouhizeiritsuMap().getString("zeiritsu");
		kashiKeigenZeiritsuKbn = masterLogic.findValidShouhizeiritsuMap().getString("keigen_zeiritsu_kbn");
		shouhyouShoruiFlg = SHOUHYOU_SHORUI.NASI;
	}

	/**
	 * 振替伝票テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 振替伝票レコード
	 * @param sanshou     参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection  コネクション
	 */
	protected void shinseiData2Gamen(Furikae shinseiData, boolean sanshou, EteamConnection connection) {
		this.denpyouDate = formatDate(shinseiData.denpyouDate);
		this.shouhyouShoruiFlg = shinseiData.shouhyouShoruiFlg;
		this.kariZeiritsu = formatMoney(shinseiData.kariZeiritsu);
		this.kariKeigenZeiritsuKbn = shinseiData.kariKeigenZeiritsuKbn;
		this.kingaku = formatMoney(shinseiData.kingaku);
		this.hontaiKingaku = "0"; //計算しないため「0」格納;
		this.shouhizeigaku = "0";
		this.hf1Cd = shinseiData.hf1Cd;
		this.hf1Name = shinseiData.hf1NameRyakushiki;
		this.hf2Cd =shinseiData.hf2Cd;
		this.hf2Name = shinseiData.hf2NameRyakushiki;
		this.hf3Cd =  shinseiData.hf3Cd;
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
		this.kariKamokuCd = shinseiData.kariKamokuCd;
		this.kariKamokuName = shinseiData.kariKamokuName;
		this.kariKamokuEdabanCd = shinseiData.kariKamokuEdabanCd;
		this.kariKamokuEdabanName = shinseiData.kariKamokuEdabanName;
		this.kariFutanBumonCd = shinseiData.kariFutanBumonCd;
		this.kariFutanBumonName = shinseiData.kariFutanBumonName;
		this.kariTorihikisakiCd = shinseiData.kariTorihikisakiCd;
		this.kariTorihikisakiName = shinseiData.kariTorihikisakiNameRyakushiki;
		this.kariUf1Cd = shinseiData.kariUf1Cd;
		this.kariUf1Name = shinseiData.kariUf1NameRyakushiki;
		this.kariUf2Cd = shinseiData.kariUf2Cd;
		this.kariUf2Name = shinseiData.kariUf2NameRyakushiki;
		this.kariUf3Cd = shinseiData.kariUf3Cd;
		this.kariUf3Name = shinseiData.kariUf3NameRyakushiki;
		this.kariUf4Cd = shinseiData.kariUf4Cd;
		this.kariUf4Name = shinseiData.kariUf4NameRyakushiki;
		this.kariUf5Cd = shinseiData.kariUf5Cd;
		this.kariUf5Name = shinseiData.kariUf5NameRyakushiki;
		this.kariUf6Cd = shinseiData.kariUf6Cd;
		this.kariUf6Name = shinseiData.kariUf6NameRyakushiki;
		this.kariUf7Cd = shinseiData.kariUf7Cd;
		this.kariUf7Name = shinseiData.kariUf7NameRyakushiki;
		this.kariUf8Cd = shinseiData.kariUf8Cd;
		this.kariUf8Name = shinseiData.kariUf8NameRyakushiki;
		this.kariUf9Cd = shinseiData.kariUf9Cd;
		this.kariUf9Name = shinseiData.kariUf9NameRyakushiki;
		this.kariUf10Cd = shinseiData.kariUf10Cd;
		this.kariUf10Name = shinseiData.kariUf10NameRyakushiki;
		this.kariJigyoushaKbn = shinseiData.kariJigyoushaKbn;
		this.kariKazeiKbn  	= shinseiData.kariKazeiKbn;
		this.kariBunriKbn  	= shinseiData.kariBunriKbn;
		this.kariShiireKbn  	= shinseiData.kariShiireKbn;
		this.kashiKamokuCd = shinseiData.kashiKamokuCd;
		this.kashiKamokuName = shinseiData.kashiKamokuName;
		this.kashiKamokuEdabanCd = shinseiData.kashiKamokuEdabanCd;
		this.kashiKamokuEdabanName = shinseiData.kashiKamokuEdabanName;
		this.kashiFutanBumonCd = shinseiData.kashiFutanBumonCd;
		this.kashiFutanBumonName = shinseiData.kashiFutanBumonName;
		this.kashiTorihikisakiCd = shinseiData.kashiTorihikisakiCd;
		this.kashiTorihikisakiName = shinseiData.kashiTorihikisakiNameRyakushiki;
		this.kashiUf1Cd = shinseiData.kashiUf1Cd;
		this.kashiUf1Name = shinseiData.kashiUf1NameRyakushiki;
		this.kashiUf2Cd = shinseiData.kashiUf2Cd;
		this.kashiUf2Name = shinseiData.kashiUf2NameRyakushiki;
		this.kashiUf3Cd =  shinseiData.kashiUf3Cd;
		this.kashiUf3Name = shinseiData.kashiUf3NameRyakushiki;
		this.kashiUf4Cd = shinseiData.kashiUf4Cd;
		this.kashiUf4Name = shinseiData.kashiUf4NameRyakushiki;
		this.kashiUf5Cd = shinseiData.kashiUf5Cd;
		this.kashiUf5Name = shinseiData.kashiUf5NameRyakushiki;
		this.kashiUf6Cd = shinseiData.kashiUf6Cd;
		this.kashiUf6Name = shinseiData.kashiUf6NameRyakushiki;
		this.kashiUf7Cd = shinseiData.kashiUf7Cd;
		this.kashiUf7Name = shinseiData.kashiUf7NameRyakushiki;
		this.kashiUf8Cd = shinseiData.kashiUf8Cd;
		this.kashiUf8Name = shinseiData.kashiUf8NameRyakushiki;
		this.kashiUf9Cd = shinseiData.kashiUf9Cd;
		this.kashiUf9Name = shinseiData.kashiUf9NameRyakushiki;
		this.kashiUf10Cd = shinseiData.kashiUf10Cd;
		this.kashiUf10Name = shinseiData.kashiUf10NameRyakushiki;
		this.kashiKazeiKbn = shinseiData.kashiKazeiKbn;
		this.kashiJigyoushaKbn = shinseiData.kashiJigyoushaKbn;
		this.kashiBunriKbn  	= shinseiData.kashiBunriKbn;
		this.kashiShiireKbn  	= shinseiData.kashiShiireKbn;
		this.kariProjectCd = shinseiData.kariProjectCd;
		this.kariProjectName = shinseiData.kariProjectName;
		this.kashiProjectCd = shinseiData.kashiProjectCd;
		this.kashiProjectName = shinseiData.kashiProjectName;
		this.kariSegmentCd = shinseiData.kariSegmentCd;
		this.kariSegmentName = shinseiData.kariSegmentNameRyakushiki;
		this.kashiSegmentCd = shinseiData.kashiSegmentCd;
		this.kashiSegmentName = shinseiData.kashiSegmentNameRyakushiki;
		this.tekiyou = shinseiData.tekiyou;
		this.invoiceDenpyou = shinseiData.invoiceDenpyou;
		this.kashiZeigakuHoushiki = shinseiData.kashiZeigakuHoushiki;
		this.kariZeigakuHoushiki = shinseiData.kariZeigakuHoushiki;
		this.kashiZeiritsu = formatMoney(shinseiData.kashiZeiritsu);
		this.kashiKeigenZeiritsuKbn = shinseiData.kashiKeigenZeiritsuKbn;
		if(!sanshou){
			GMap meisai = null;
			BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.FURIKAE_DENPYOU, shinseiData.map, meisai, "0");
			String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
			if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
				chuuki1 = commonLogic.getTekiyouChuuki();
				chuuki2 = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
			}
		}
		bikou = shinseiData.bikou;
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		//プルダウンのリストを取得
		zeiritsuList =this.zeiritsuDao.load().stream().map(Shouhizeiritsu::getMap).collect(Collectors.toList());
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
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		jigyoushaKbnList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");
		zeigakuKeisanList = this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiirezeigaku_keisan").stream()
				.map(NaibuCdSetting::getMap).collect(Collectors.toList());
		zeigakuKeisanDomain = EteamCommon.mapList2Arr(zeigakuKeisanList, "naibu_cd");
		
		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		// プロジェクトコードを使用しない設定の場合クリア
		if("0".equals(pjShiyouFlg)  || !ks.project.getHyoujiFlg() ) {
			kariProjectCd = "";
			kariProjectName = "";
			kashiProjectCd = "";
			kashiProjectName = "";
		}

		// セグメントコードを使用しない設定の場合クリア
		if("0".equals(pjShiyouFlg)  || !ks.segment.getHyoujiFlg() ) {
			kariSegmentCd = "";
			kariSegmentName = "";
			kashiSegmentCd = "";
			kashiSegmentName = "";
		}
		//課税区分の制御
		if (isNotEmpty(kariKamokuCd)) {
			KamokuMaster kmk = kamokuMasterDao.find(kariKamokuCd);
			if (kmk != null) {
				kariShoriGroup = kmk.shoriGroup.toString();
			}
		}
		if (isNotEmpty(kashiKamokuCd)) {
			KamokuMaster kmk = kamokuMasterDao.find(kashiKamokuCd);
			if (kmk != null) {
				kashiShoriGroup = kmk.shoriGroup.toString();
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

		//負担部門任意入力の場合、起票ユーザで使用できるかチェック
		boolean enableKariBumonSecurity = commonLogic.checkFutanBumonWithSecurity(kariFutanBumonCd, "借方" + ks.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
		boolean enableKashiBumonSecurity = commonLogic.checkFutanBumonWithSecurity(kashiFutanBumonCd, "貸方" + ks.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);

		//借方
		kariKamokuName =  kamokuMasterDao.find(kariKamokuCd).kamokuNameRyakushiki;
		kariKamokuEdabanName = masterLogic.findKamokuEdabanName(kariKamokuCd,kariKamokuEdabanCd) ;
		kariFutanBumonName = enableKariBumonSecurity ? masterLogic.findFutanBumonName(kariFutanBumonCd) : "";
		kariTorihikisakiName = masterLogic.findTorihikisakiName(kariTorihikisakiCd);
		kariUf1Name = masterLogic.findUfName("1", kariUf1Cd);
		kariUf2Name = masterLogic.findUfName("2", kariUf2Cd);
		kariUf3Name = masterLogic.findUfName("3", kariUf3Cd);
		kariUf4Name = masterLogic.findUfName("4", kariUf4Cd);
		kariUf5Name = masterLogic.findUfName("5", kariUf5Cd);
		kariUf6Name = masterLogic.findUfName("6", kariUf6Cd);
		kariUf7Name = masterLogic.findUfName("7", kariUf7Cd);
		kariUf8Name = masterLogic.findUfName("8", kariUf8Cd);
		kariUf9Name = masterLogic.findUfName("9", kariUf9Cd);
		kariUf10Name = masterLogic.findUfName("10", kariUf10Cd);
		kariProjectName = masterLogic.findProjectName(kariProjectCd);
		kariSegmentName = masterLogic.findSegmentName(kariSegmentCd);

		//貸方
		kashiKamokuName = kamokuMasterDao.find(kashiKamokuCd).kamokuNameRyakushiki;
		kashiKamokuEdabanName = masterLogic.findKamokuEdabanName(kashiKamokuCd,kashiKamokuEdabanCd) ;
		kashiFutanBumonName = enableKashiBumonSecurity ? masterLogic.findFutanBumonName(kashiFutanBumonCd) : "";
		kashiTorihikisakiName = masterLogic.findTorihikisakiName(kashiTorihikisakiCd);
		kashiUf1Name = masterLogic.findUfName("1", kashiUf1Cd);
		kashiUf2Name = masterLogic.findUfName("2", kashiUf2Cd);
		kashiUf3Name = masterLogic.findUfName("3", kashiUf3Cd);
		kashiUf4Name = masterLogic.findUfName("4", kashiUf4Cd);
		kashiUf5Name = masterLogic.findUfName("5", kashiUf5Cd);
		kashiUf6Name = masterLogic.findUfName("6", kashiUf6Cd);
		kashiUf7Name = masterLogic.findUfName("7", kashiUf7Cd);
		kashiUf8Name = masterLogic.findUfName("8", kashiUf8Cd);
		kashiUf9Name = masterLogic.findUfName("9", kashiUf9Cd);
		kashiUf10Name = masterLogic.findUfName("10", kashiUf10Cd);
		kashiProjectName = masterLogic.findProjectName(kashiProjectCd);
		kashiSegmentName = masterLogic.findSegmentName(kashiSegmentCd);
	}
	
	/**
	 * @return 振替dto
	 */
	protected Furikae createFurikae()
	{
		Furikae furikae = new Furikae();
		furikae.denpyouId = this.denpyouId;
		furikae.denpyouDate = super.toDate(this.denpyouDate);
		furikae.shouhyouShoruiFlg = this.shouhyouShoruiFlg;
		furikae.kingaku = super.toDecimal(this.kingaku);
		furikae.hontaiKingaku = BigDecimal.ZERO; //計算しないため「0」格納
		furikae.shouhizeigaku = BigDecimal.ZERO; //計算しないため「0」格納
		furikae.kariZeiritsu = super.toDecimalZeroIfEmpty(this.kariZeiritsu);
		furikae.kariKeigenZeiritsuKbn = this.kariKeigenZeiritsuKbn;
		furikae.tekiyou = this.tekiyou;
		furikae.hf1Cd = this.hf1Cd;
		furikae.hf1NameRyakushiki = this.hf1Name;
		furikae.hf2Cd = this.hf2Cd;
		furikae.hf2NameRyakushiki = this.hf2Name;
		furikae.hf3Cd = this.hf3Cd;
		furikae.hf3NameRyakushiki = this.hf3Name;
		furikae.hf4Cd = this.hf4Cd;
		furikae.hf4NameRyakushiki = this.hf4Name;
		furikae.hf5Cd = this.hf5Cd;
		furikae.hf5NameRyakushiki = this.hf5Name;
		furikae.hf6Cd = this.hf6Cd;
		furikae.hf6NameRyakushiki = this.hf6Name;
		furikae.hf7Cd = this.hf7Cd;
		furikae.hf7NameRyakushiki = this.hf7Name;
		furikae.hf8Cd = this.hf8Cd;
		furikae.hf8NameRyakushiki = this.hf8Name;
		furikae.hf9Cd = this.hf9Cd;
		furikae.hf9NameRyakushiki = this.hf9Name;
		furikae.hf10Cd = this.hf10Cd;
		furikae.hf10NameRyakushiki = this.hf10Name;
		furikae.bikou = this.bikou;
		furikae.kariFutanBumonCd = this.kariFutanBumonCd;
		furikae.kariFutanBumonName = this.kariFutanBumonName;
		furikae.kariKamokuCd = this.kariKamokuCd;
		furikae.kariKamokuName = this.kariKamokuName;
		furikae.kariKamokuEdabanCd = this.kariKamokuEdabanCd;
		furikae.kariKamokuEdabanName = this.kariKamokuEdabanName;
		furikae.kariKazeiKbn = this.kariKazeiKbn;
		furikae.kariTorihikisakiCd = this.kariTorihikisakiCd;
		furikae.kariTorihikisakiNameRyakushiki = this.kariTorihikisakiName;
		furikae.kashiFutanBumonCd = this.kashiFutanBumonCd;
		furikae.kashiFutanBumonName = this.kashiFutanBumonName;
		furikae.kashiKamokuCd = this.kashiKamokuCd;
		furikae.kashiKamokuName = this.kashiKamokuName;
		furikae.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd;
		furikae.kashiKamokuEdabanName = this.kashiKamokuEdabanName;
		furikae.kashiKazeiKbn = this.kashiKazeiKbn;
		furikae.kashiTorihikisakiCd = this.kashiTorihikisakiCd;
		furikae.kashiTorihikisakiNameRyakushiki = this.kashiTorihikisakiName;
		furikae.kariUf1Cd = this.kariUf1Cd;
		furikae.kariUf1NameRyakushiki = this.kariUf1Name;
		furikae.kariUf2Cd = this.kariUf2Cd;
		furikae.kariUf2NameRyakushiki = this.kariUf2Name;
		furikae.kariUf3Cd = this.kariUf3Cd;
		furikae.kariUf3NameRyakushiki = this.kariUf3Name;
		furikae.kariUf4Cd = this.kariUf4Cd;
		furikae.kariUf4NameRyakushiki = this.kariUf4Name;
		furikae.kariUf5Cd = this.kariUf5Cd;
		furikae.kariUf5NameRyakushiki = this.kariUf5Name;
		furikae.kariUf6Cd = this.kariUf6Cd;
		furikae.kariUf6NameRyakushiki = this.kariUf6Name;
		furikae.kariUf7Cd = this.kariUf7Cd;
		furikae.kariUf7NameRyakushiki = this.kariUf7Name;
		furikae.kariUf8Cd = this.kariUf8Cd;
		furikae.kariUf8NameRyakushiki = this.kariUf8Name;
		furikae.kariUf9Cd = this.kariUf9Cd;
		furikae.kariUf9NameRyakushiki = this.kariUf9Name;
		furikae.kariUf10Cd = this.kariUf10Cd;
		furikae.kariUf10NameRyakushiki = this.kariUf10Name;
		furikae.kashiUf1Cd = this.kashiUf1Cd;
		furikae.kashiUf1NameRyakushiki = this.kashiUf1Name;
		furikae.kashiUf2Cd = this.kashiUf2Cd;
		furikae.kashiUf2NameRyakushiki = this.kashiUf2Name;
		furikae.kashiUf3Cd = this.kashiUf3Cd;
		furikae.kashiUf3NameRyakushiki = this.kashiUf3Name;
		furikae.kashiUf4Cd = this.kashiUf4Cd;
		furikae.kashiUf4NameRyakushiki = this.kashiUf4Name;
		furikae.kashiUf5Cd = this.kashiUf5Cd;
		furikae.kashiUf5NameRyakushiki = this.kashiUf5Name;
		furikae.kashiUf6Cd = this.kashiUf6Cd;
		furikae.kashiUf6NameRyakushiki = this.kashiUf6Name;
		furikae.kashiUf7Cd = this.kashiUf7Cd;
		furikae.kashiUf7NameRyakushiki = this.kashiUf7Name;
		furikae.kashiUf8Cd = this.kashiUf8Cd;
		furikae.kashiUf8NameRyakushiki = this.kashiUf8Name;
		furikae.kashiUf9Cd = this.kashiUf9Cd;
		furikae.kashiUf9NameRyakushiki = this.kashiUf9Name;
		furikae.kashiUf10Cd = this.kashiUf10Cd;
		furikae.kashiUf10NameRyakushiki = this.kashiUf10Name;
		furikae.kariProjectCd = this.kariProjectCd;
		furikae.kariProjectName = this.kariProjectName;
		furikae.kariSegmentCd = this.kariSegmentCd;
		furikae.kariSegmentNameRyakushiki = this.kariSegmentName;
		furikae.kashiProjectCd = this.kashiProjectCd;
		furikae.kashiProjectName = this.kashiProjectName;
		furikae.kashiSegmentCd = this.kashiSegmentCd;
		furikae.kashiSegmentNameRyakushiki = this.kashiSegmentName;
		furikae.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		furikae.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		furikae.kariBunriKbn = this.kariBunriKbn;
		furikae.kashiBunriKbn = this.kashiBunriKbn;
		furikae.kariZeigakuHoushiki = this.kariZeigakuHoushiki == null ? "0" : this.kariZeigakuHoushiki;
		furikae.kariJigyoushaKbn = this.kariJigyoushaKbn == null ? "0" : this.kariJigyoushaKbn;
		furikae.kariShiireKbn = this.kariShiireKbn;
		furikae.kashiZeigakuHoushiki = this.kashiZeigakuHoushiki == null ? "0" : this.kashiZeigakuHoushiki;
		furikae.kashiJigyoushaKbn = this.kashiJigyoushaKbn == null ? "0" : this.kashiJigyoushaKbn;
		furikae.kashiShiireKbn = this.kashiShiireKbn;
		furikae.invoiceDenpyou = this.invoiceDenpyou;
		furikae.kashiZeiritsu = super.toDecimalZeroIfEmpty(this.kashiZeiritsu);
		furikae.kashiKeigenZeiritsuKbn = this.kashiKeigenZeiritsuKbn;
		return furikae;
	}

	/**
	 * 振替伝票EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		FurikaeDenpyouXlsLogic furikaedenpyouxlsLogic = EteamContainer.getComponent(FurikaeDenpyouXlsLogic.class, connection);
		furikaedenpyouxlsLogic.makeExcel(denpyouId, out);
	}
}
