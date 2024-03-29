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
import eteam.common.EteamConst.Domain;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHOUHYOU_SHORUI;
import eteam.common.EteamNaibuCodeSetting.TSUKEKAE_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamUserSettingLogic;
import eteam.common.EteamUserSettingLogic.UserDefaultValueKbn;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.KiKesnAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.abstractdao.TsukekaeAbstractDao;
import eteam.database.abstractdao.TsukekaeMeisaiAbstractDao;
import eteam.database.dao.KiKesnDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dao.TsukekaeDao;
import eteam.database.dao.TsukekaeMeisaiDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.Shouhizeiritsu;
import eteam.database.dto.Tsukekae;
import eteam.database.dto.TsukekaeMeisai;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 総合付替伝票画面Action
 */
@Getter @Setter @ToString
public class SougouTsukekaeDenpyouAction extends WorkflowEventControl {

//＜定数＞
	
//＜画面入力＞

	//申請内容
	/** 伝票日 */
	String denpyouDate; 
	/** 領収書・請求書等 */
	String shouhyouShoruiFlg;
	/** 消費税率 */
	String zeiritsu; 
	/** 軽減税率区分 */
	String keigenZeiritsuKbn;
	/** 付替区分 */
	String tsukekaeKbn;
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
	/** 補足 */
	String hosoku;
	/** 共通部分摘要注記 */
	String chuuki1;

	//付替元
	/** 勘定科目コード */
	String motoKamokuCd; 
	/** 勘定科目名 */
	String motoKamokuName; 
	/** 勘定科目枝番コード */
	String motoKamokuEdabanCd; 
	/** 勘定科目枝番名 */
	String motoKamokuEdabanName;
	/** 負担部門コード */
	String motoFutanBumonCd; 
	/** 負担部門名 */
	String motoFutanBumonName; 
	/** 取引先コード */
	String motoTorihikisakiCd; 
	/** 取引先名 */
	String motoTorihikisakiNameRyakushiki; 
	/** プロジェクトコード */
	String motoProjectCd;
	/** プロジェクト名 */
	String motoProjectName;
	/** セグメントコード */
	String motoSegmentCd;
	/** セグメント名 */
	String motoSegmentName;
	/** ユニバーサルフィールド1コード */
	String motoUf1Cd;
	/** ユニバーサルフィールド1名 */
	String motoUf1Name;
	/** ユニバーサルフィールド2コード */
	String motoUf2Cd;
	/** ユニバーサルフィールド2名 */
	String motoUf2Name;
	/** ユニバーサルフィールド3コード */
	String motoUf3Cd;
	/** ユニバーサルフィールド3名 */
	String motoUf3Name;
	/** ユニバーサルフィールド4コード */
	String motoUf4Cd;
	/** ユニバーサルフィールド4名 */
	String motoUf4Name;
	/** ユニバーサルフィールド5コード */
	String motoUf5Cd;
	/** ユニバーサルフィールド5名 */
	String motoUf5Name;
	/** ユニバーサルフィールド6コード */
	String motoUf6Cd;
	/** ユニバーサルフィールド6名 */
	String motoUf6Name;
	/** ユニバーサルフィールド7コード */
	String motoUf7Cd;
	/** ユニバーサルフィールド7名 */
	String motoUf7Name;
	/** ユニバーサルフィールド8コード */
	String motoUf8Cd;
	/** ユニバーサルフィールド8名 */
	String motoUf8Name;
	/** ユニバーサルフィールド9コード */
	String motoUf9Cd;
	/** ユニバーサルフィールド9名 */
	String motoUf9Name;
	/** ユニバーサルフィールド10コード */
	String motoUf10Cd;
	/** ユニバーサルフィールド10名 */
	String motoUf10Name;
	/** 課税区分 */
	String motoKazeiKbn;
	/** 事業者区分 */
	String motoJigyoushaKbn;
	/** 分離区分 */
	String motoBunriKbn;
	/** 仕入区分 */
	String motoShiireKbn;
	/** 税額計算方式 */
	String motoZeigakuHoushiki;
	/** 処理グループ */
	String motoShoriGroup;
	
	//合計金額は親クラスのkingaku

	//画面入力（明細）
	/** 勘定科目コード */
	String[] sakiKamokuCd;
	/** 勘定科目名 */
	String[] sakiKamokuName;
	/** 勘定科目枝番コード */
	String[] sakiKamokuEdabanCd;
	/** 勘定科目枝番名 */
	String[] sakiKamokuEdabanName;
	/** 負担部門コード */
	String[] sakiFutanBumonCd;
	/** 負担部門名 */
	String[] sakiFutanBumonName;
	/** 取引先コード */
	String[] sakiTorihikisakiCd;
	/** 取引先名 */
	String[] sakiTorihikisakiNameRyakushiki;
	/** プロジェクトコード */
	String[] sakiProjectCd;
	/** プロジェクト名 */
	String[] sakiProjectName;
	/** セグメントコード */
	String[] sakiSegmentCd;
	/** セグメント名 */
	String[] sakiSegmentName;
	/** ユニバーサルフィールド１コード */
	String[] sakiUf1Cd;
	/** ユニバーサルフィールド１名 */
	String[] sakiUf1Name;
	/** ユニバーサルフィールド２コード */
	String[] sakiUf2Cd;
	/** ユニバーサルフィールド２名 */
	String[] sakiUf2Name;
	/** ユニバーサルフィールド３コード */
	String[] sakiUf3Cd;
	/** ユニバーサルフィールド３名 */
	String[] sakiUf3Name;
	/** ユニバーサルフィールド４コード */
	String[] sakiUf4Cd;
	/** ユニバーサルフィールド４名 */
	String[] sakiUf4Name;
	/** ユニバーサルフィールド５コード */
	String[] sakiUf5Cd;
	/** ユニバーサルフィールド５名 */
	String[] sakiUf5Name;
	/** ユニバーサルフィールド６コード */
	String[] sakiUf6Cd;
	/** ユニバーサルフィールド６名 */
	String[] sakiUf6Name;
	/** ユニバーサルフィールド７コード */
	String[] sakiUf7Cd;
	/** ユニバーサルフィールド７名 */
	String[] sakiUf7Name;
	/** ユニバーサルフィールド８コード */
	String[] sakiUf8Cd;
	/** ユニバーサルフィールド８名 */
	String[] sakiUf8Name;
	/** ユニバーサルフィールド９コード */
	String[] sakiUf9Cd;
	/** ユニバーサルフィールド９名 */
	String[] sakiUf9Name;
	/** ユニバーサルフィールド１０コード */
	String[] sakiUf10Cd;
	/** ユニバーサルフィールド１０名 */
	String[] sakiUf10Name;
	/** 課税区分 */
	String[] sakiKazeiKbn;
	/** 金額 */
	String[] sakiKingaku;
	/** 本体金額 */
	String[] sakiHontaiKingaku;
	/** 消費税額 */
	String[] sakiShouhizeigaku;
	/** 摘要 */
	String[] sakiTekiyou;
	/** 摘要注記 */
	String[] chuuki2;
	/** 備考 */
	String[] sakiBikou;
	/** 事業者区分 */
	String[] sakiJigyoushaKbn;
	/** 分離区分 */
	String[] sakiBunriKbn;
	/** 仕入区分 */
	String[] sakiShiireKbn;
	/** 税額方式 */
	String[] sakiZeigakuHoushiki;
	/** 処理グループ */
	String[] sakiShoriGroup;
	
//＜画面入力以外＞
	//プルダウン等の候補値
	/** 領収書・請求書等のDropDownList */
	List<GMap> shouhyouShoruiList;
	
	/** 消費税率のDropDownList */
	List<GMap> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;

	/** 課税区分のDropDownList */
	List<GMap> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
	/** 分離区分DropDownList */
	List<GMap> bunriKbnList;
	/** 分離区分ドメイン */
	String[] bunriKbnDomain;
	/** 仕入区分DropDownList */
	List<GMap> shiireKbnList;
	/** 仕入区分ドメイン */
	String[] shiireKbnDomain;
	/** インボイス対応伝票DropDownList */
	List<NaibuCdSetting> invoiceDenpyouList;
	/** インボイス対応伝票ドメイン */
	String[] invoiceDenpyouDomain;
	/** 事業者区分DropDownList */
	List<GMap> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;
	/** 税額計算方式DropDownList */
	List<GMap> zeigakuKeisanList;
	/** 税額計算方式ドメイン */
	String[] zeigakuKeisanDomain;

	/** 付替区分のラジオList */
	List<GMap> tsukekaeKbnList;
	
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	
	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU);

	/** プロジェクトコード表示 */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	
	/** 入力モード */
	boolean enableInput;
	
//＜部品＞
	/** 総合付替伝票ロジック */
	SougouTsukekaeDenpyouLogic gyoumuLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** バッチ会計ロジック */
	BatchKaikeiCommonLogic batchkaikeilogic; 
	/** ユーザー単位設定ロジック */
	EteamUserSettingLogic userSettingLogic;
	/** 総合付替伝票Dao */
	TsukekaeAbstractDao tsukekaeDao;
	/** 総合付替伝票Dao */
	TsukekaeMeisaiAbstractDao tsukekaeMeisaiDao;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 税率Dao */
	ShouhizeiritsuAbstractDao zeiritsuDao;
	/** 決算期Dao */
	KiKesnAbstractDao kiKesnDao;
		
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
		//申請内容
			checkDate (denpyouDate, ks.denpyouDate.getName(), false);
			checkDomain (shouhyouShoruiFlg, Domain.FLG, ks.shouhyouShoruiFlg.getName(), false);
			checkDomain (zeiritsu, zeiritsuDomain, ks.zeiritsu.getName(), false);
			checkDomain (keigenZeiritsuKbn, EteamConst.Domain.FLG, "軽減税率区分", false);
			checkDomain (tsukekaeKbn, new String[]{"1", "2"}, "付替区分", false);
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
			checkString (hosoku, 1, 240, ks.hosoku.getName(), false);
			checkDomain(this.invoiceDenpyou, this.invoiceDenpyouDomain, "インボイス対応伝票", false);
		
		//付替元
			checkString (motoKamokuCd, 1, 6, "（付替元）" + ks.kamoku.getName() + "コード", false);
			checkString (motoKamokuName, 1, 22, "（付替元）" + ks.kamoku.getName() + "名", false);
			checkString (motoKamokuEdabanCd, 1, 12, "（付替元）" + ks.kamokuEdaban.getName() + "コード", false);
			checkString (motoKamokuEdabanName, 1, 20, "（付替元）" + ks.kamokuEdaban.getName() + "名", false);
			checkString (motoFutanBumonCd, 1, 8, "（付替元）" + ks.futanBumon.getName() + "コード", false);
			checkString (motoFutanBumonName, 1, 20, "（付替元）" + ks.futanBumon.getName() + "名", false);
			checkString (motoTorihikisakiCd, 1, 12, "（付替元）" + ks.torihikisaki.getName() + "コード", false);
			checkString (motoTorihikisakiNameRyakushiki,1, 20, "（付替元）" + ks.torihikisaki.getName() + "名", false);
			checkString (motoProjectCd, 1, 12, "（付替元）" + ks.project.getName() + "コード", false);
			checkString (motoProjectName, 1, 20, "（付替元）" + ks.project.getName() + "名", false);
			checkString (motoSegmentCd, 1, 8, "（付替元）" + ks.segment.getName() + "コード", false);
			checkString (motoSegmentName, 1, 20, "（付替元）" + ks.segment.getName() + "名", false);
			checkString(motoUf1Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf1Name(), false);
			checkString(motoUf1Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf1Name(), false);
			checkString(motoUf2Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf2Name(), false);
			checkString(motoUf2Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf2Name(), false);
			checkString(motoUf3Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf3Name(), false);
			checkString(motoUf3Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf3Name(), false);
			checkString(motoUf4Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf4Name(), false);
			checkString(motoUf4Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf4Name(), false);
			checkString(motoUf5Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf5Name(), false);
			checkString(motoUf5Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf5Name(), false);
			checkString(motoUf6Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf6Name(), false);
			checkString(motoUf6Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf6Name(), false);
			checkString(motoUf7Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf7Name(), false);
			checkString(motoUf7Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf7Name(), false);
			checkString(motoUf8Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf8Name(), false);
			checkString(motoUf8Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf8Name(), false);
			checkString(motoUf9Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf9Name(), false);
			checkString(motoUf9Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf9Name(), false);
			checkString(motoUf10Cd, 1, 20, "（付替元）" + hfUfSeigyo.getUf10Name(), false);
			checkString(motoUf10Name, 1, 20, "（付替元）" + hfUfSeigyo.getUf10Name(), false);
			if(motoKazeiKbn != null && motoKazeiKbn.length() != 0) {
				checkDomain (motoKazeiKbn, kazeiKbnDomain, "（付替元）" + ks.kazeiKbn.getName(), false);
			}else {
				errorList.add("（付替元）課税区分を入力してください");
			}
			checkKingakuOver1 (kingaku, "（付替元）" + ks.kingakuGoukei.getName() , false);
			checkDomain(motoJigyoushaKbn, this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
			checkDomain(motoZeigakuHoushiki , this.zeigakuKeisanDomain, ks.uriagezeigakuKeisan.getName(), false);
			checkDomain(motoBunriKbn, this.bunriKbnDomain, ks.bunriKbn.getName(), false);
			checkDomain(motoShiireKbn, this.shiireKbnDomain, ks.shiireKbn.getName(),false);
			

		//付替先
		for (int i = 0; i < sakiKamokuCd.length; i++) {
			int ip = i + 1;
			checkString (sakiKamokuCd[i], 1,  6, "（付替先）" + ks.kamoku.getName() + "コード:" + ip + "行目", false);
			checkString (sakiKamokuName[i] ,				1, 22, "（付替先）" + ks.kamoku.getName() + "名:"		+ ip + "行目", false);
			checkString (sakiKamokuEdabanCd[i], 1, 12, "（付替先）" + ks.kamokuEdaban.getName() + "コード:"	+ ip + "行目", false);
			checkString (sakiKamokuEdabanName[i], 1, 20, "（付替先）" + ks.kamokuEdaban.getName() + "名："	+ ip + "行目", false);
			checkString (sakiFutanBumonCd[i], 1,  8, "（付替先）" + ks.futanBumon.getName() + "コード："+ ip + "行目", false);
			checkString (sakiFutanBumonName[i], 1, 20, "（付替先）" + ks.futanBumon.getName() + "名："	+ ip + "行目", false);
			checkString (sakiTorihikisakiCd[i], 1, 12, "（付替先）" + ks.torihikisaki.getName() + "コード："+ ip + "行目", false);
			checkString (sakiTorihikisakiNameRyakushiki[i], 1, 20, "（付替先）" + ks.torihikisaki.getName() + "名："	+ ip + "行目", false);
			checkString(sakiProjectCd[i], 1, 12, "（付替先）" + ks.project.getName() + "コード：" + ip + "行目", false);
			checkString(sakiProjectName[i], 1, 20, "（付替先）" + ks.project.getName() + "名：" + ip + "行目", false);
			checkString(sakiSegmentCd[i], 1, 8, "（付替先）" + ks.segment.getName() + "コード：" + ip + "行目", false);
			checkString(sakiSegmentName[i], 1, 20, "（付替先）" + ks.segment.getName() + "名：" + ip + "行目", false);
			checkString(sakiUf1Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf1Name() + ip + "行目", false);
			checkString(sakiUf1Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf1Name() + ip + "行目", false);
			checkString(sakiUf2Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf2Name() + ip + "行目", false);
			checkString(sakiUf2Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf2Name() + ip + "行目", false);
			checkString(sakiUf3Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf3Name() + ip + "行目", false);
			checkString(sakiUf3Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf3Name() + ip + "行目", false);
			checkString(sakiUf4Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf4Name() + ip + "行目", false);
			checkString(sakiUf4Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf4Name() + ip + "行目", false);
			checkString(sakiUf5Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf5Name() + ip + "行目", false);
			checkString(sakiUf5Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf5Name() + ip + "行目", false);
			checkString(sakiUf6Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf6Name() + ip + "行目", false);
			checkString(sakiUf6Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf6Name() + ip + "行目", false);
			checkString(sakiUf7Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf7Name() + ip + "行目", false);
			checkString(sakiUf7Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf7Name() + ip + "行目", false);
			checkString(sakiUf8Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf8Name() + ip + "行目", false);
			checkString(sakiUf8Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf8Name() + ip + "行目", false);
			checkString(sakiUf9Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf9Name() + ip + "行目", false);
			checkString(sakiUf9Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf9Name() + ip + "行目", false);
			checkString(sakiUf10Cd[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf10Name() + ip + "行目", false);
			checkString(sakiUf10Name[i], 1, 20, "（付替先）" + hfUfSeigyo.getUf10Name() + ip + "行目", false);
			if(sakiKazeiKbn != null && sakiKazeiKbn.length == sakiKamokuCd.length && sakiKazeiKbn[i].length() != 0) {
				checkString (sakiKazeiKbn[i], 1,  3, "（付替先）" + ks.kazeiKbn.getName() + ip + "行目", false);
			}else {
				errorList.add("（付替先）課税区分を入力してください");
			}
			checkKingakuOver1 (sakiKingaku[i], "（付替先）" + ks.kingaku.getName() + ip + "行目", false);
			checkString(sakiTekiyou[i], 1, this.getIntTekiyouMaxLength(), "（付替先）" + ks.tekiyou.getName() + ip + "行目", false);
			checkSJIS (sakiTekiyou[i], "（付替先）" + ks.tekiyou.getName() + ip + "行目");
			checkString (sakiBikou[i], 1, 40, "（付替先）" + ks.bikou.getName() + ip + "行目", false);
			checkDomain(sakiJigyoushaKbn[i] , this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
			checkDomain(sakiZeigakuHoushiki[i] , this.zeigakuKeisanDomain, ks.uriagezeigakuKeisan.getName(), false);
			checkDomain(sakiBunriKbn[i], this.bunriKbnDomain, ks.bunriKbn.getName(), false);
			checkDomain(sakiShiireKbn[i], this.shiireKbnDomain, ks.shiireKbn.getName(),false);
		}
		
	}

	/**
	 * 伝票内部項目の必須チェック
	 */
	protected void denpyouHissuCheck() {

		String[][] list = {
			//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			//申請内容
			{denpyouDate, ks.denpyouDate.getName(), ks.denpyouDate.getHissuFlgS()},
			{shouhyouShoruiFlg, ks.shouhyouShoruiFlg.getName(), ks.shouhyouShoruiFlg.getHissuFlgS()},
			{zeiritsu, ks.zeiritsu.getName(), ks.zeiritsu.getHissuFlgS()},
			{keigenZeiritsuKbn, "軽減税率区分", ks.zeiritsu.getHissuFlgS()},
			{tsukekaeKbn, "付替区分", "1"},
			{hosoku, ks.hosoku.getName(), ks.hosoku.getHissuFlgS()},
			//付替元
			{motoKamokuCd, "（付替元）" + ks.kamoku.getName() + "コード", ks.kamoku.getHissuFlgS()},
			{motoKamokuName, "（付替元）" + ks.kamoku.getName() + "名", ks.kamoku.getHissuFlgS()},
			{motoKamokuEdabanCd, "（付替元）" + ks.kamokuEdaban.getName() + "コード", ks.kamokuEdaban.getHissuFlgS()},
			{motoKamokuEdabanName, "（付替元）" + ks.kamokuEdaban.getName() + "名", ks.kamokuEdaban.getHissuFlgS()},
			{motoFutanBumonCd, "（付替元）" + ks.futanBumon.getName() + "コード", ks.futanBumon.getHissuFlgS()},
			{motoFutanBumonName, "（付替元）" + ks.futanBumon.getName() + "名", ks.futanBumon.getHissuFlgS()},
			{motoTorihikisakiCd, "（付替元）" + ks.torihikisaki.getName() + "コード", ks.torihikisaki.getHissuFlgS()},
			{motoTorihikisakiNameRyakushiki, "（付替元）" + ks.torihikisaki.getName() + "名", ks.torihikisaki.getHissuFlgS()},
			{kingaku ,			"（付替元）" + ks.kingakuGoukei.getName(), ks.kingakuGoukei.getHissuFlgS()},
		};
		hissuCheckCommon(list, 1);
		
		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード", ks.hf1.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード", ks.hf2.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード", ks.hf3.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード", ks.hf4.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード", ks.hf5.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード", ks.hf6.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード", ks.hf7.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード", ks.hf8.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード", ks.hf9.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード"	, ks.hf10.getHissuFlgS(), "0"},}, 1);

		if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf1Cd, "（付替元）" + hfUfSeigyo.getUf1Name() + "コード", ks.uf1.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf2Cd, "（付替元）" + hfUfSeigyo.getUf2Name() + "コード", ks.uf2.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf3Cd, "（付替元）" + hfUfSeigyo.getUf3Name() + "コード", ks.uf3.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf4Cd, "（付替元）" + hfUfSeigyo.getUf4Name() + "コード",  ks.uf4.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf5Cd, "（付替元）" + hfUfSeigyo.getUf5Name() + "コード",  ks.uf5.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf6Cd, "（付替元）" + hfUfSeigyo.getUf6Name() + "コード",  ks.uf6.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf7Cd, "（付替元）" + hfUfSeigyo.getUf7Name() + "コード",  ks.uf7.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf8Cd, "（付替元）" + hfUfSeigyo.getUf8Name() + "コード",  ks.uf8.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf9Cd, "（付替元）" + hfUfSeigyo.getUf9Name() + "コード",  ks.uf9.getHissuFlgS(), "0"},}, 1);
		if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{motoUf10Cd, "（付替元）" + hfUfSeigyo.getUf10Name() + "コード",  ks.uf10.getHissuFlgS(), "0"},}, 1);

		if( !pjShiyouFlg.equals("0") && ks.project.getHyoujiFlg() ) {
			list = new String[][]{
					{motoProjectCd, "（付替元）" + ks.project.getName() + "コード", ks.project.getHissuFlgS()},
					{motoProjectName, "（付替元）" + ks.project.getName() + "名", ks.project.getHissuFlgS()},
		};
			hissuCheckCommon(list, 1);
		}
		
		if( !segmentShiyouFlg.equals("0") && ks.segment.getHyoujiFlg() ) {
			list = new String[][]{
					{motoSegmentCd, "（付替元）" + ks.segment.getName() + "コード", ks.segment.getHissuFlgS()},
					{motoSegmentName, "（付替元）" + ks.segment.getName() + "名", ks.segment.getHissuFlgS()},
		};
			hissuCheckCommon(list, 1);
		}
		
		for (int i = 0; i < sakiKamokuCd.length; i++) {
			int ip = i + 1;
			list = new String[][]{
			//項目									項目名 																		必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{sakiKamokuCd[i], "（付替先）" + ks.kamoku.getName() + "コード" 	+ ip + "行目", ks.kamoku.getHissuFlgS()},
			{sakiKamokuName[i], "（付替先）" + ks.kamoku.getName() + "名"		+ ip + "行目", ks.kamoku.getHissuFlgS()},
			{sakiKamokuEdabanCd[i], "（付替先）" + ks.kamokuEdaban.getName() + "コード" 	+ ip + "行目", ks.kamokuEdaban.getHissuFlgS()},
			{sakiKamokuEdabanName[i], "（付替先）" + ks.kamokuEdaban.getName() + "名" 		+ ip + "行目", ks.kamokuEdaban.getHissuFlgS()},
			{sakiFutanBumonCd[i], "（付替先）" + ks.futanBumon.getName() + "コード" 	+ ip + "行目", ks.futanBumon.getHissuFlgS()},
			{sakiFutanBumonName[i], "（付替先）" + ks.futanBumon.getName() + "名" 		+ ip + "行目", ks.futanBumon.getHissuFlgS()},
			{sakiTorihikisakiCd[i], "（付替先）" + ks.torihikisaki.getName() + "コード" 	+ ip + "行目", ks.torihikisaki.getHissuFlgS()},
			{sakiTorihikisakiNameRyakushiki[i], "（付替先）" + ks.torihikisaki.getName() + "名" 		+ ip + "行目", ks.torihikisaki.getHissuFlgS()},
			{sakiKingaku[i], "（付替先）" + ks.kingaku.getName() + ip + "行目", ks.kingaku.getHissuFlgS()},
			{sakiTekiyou[i], "（付替先）" + ks.tekiyou.getName() + ip + "行目", ks.tekiyou.getHissuFlgS()},
			{sakiBikou[i], "（付替先）" + ks.bikou.getName() + ip + "行目", ks.bikou.getHissuFlgS()},
			};
			hissuCheckCommon(list, 1);
			
			if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf1Cd[i], "（付替先）" + hfUfSeigyo.getUf1Name() + "コード" + ip + "行目",  ks.uf1.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf2Cd[i], "（付替先）" + hfUfSeigyo.getUf2Name() + "コード" + ip + "行目",  ks.uf2.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf3Cd[i], "（付替先）" + hfUfSeigyo.getUf3Name() + "コード" + ip + "行目",  ks.uf3.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf4Cd[i], "（付替先）" + hfUfSeigyo.getUf4Name() + "コード" + ip + "行目",  ks.uf4.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf5Cd[i], "（付替先）" + hfUfSeigyo.getUf5Name() + "コード" + ip + "行目",  ks.uf5.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf6Cd[i], "（付替先）" + hfUfSeigyo.getUf6Name() + "コード" + ip + "行目",  ks.uf6.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf7Cd[i], "（付替先）" + hfUfSeigyo.getUf7Name() + "コード" + ip + "行目",  ks.uf7.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf8Cd[i], "（付替先）" + hfUfSeigyo.getUf8Name() + "コード" + ip + "行目",  ks.uf8.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf9Cd[i], "（付替先）" + hfUfSeigyo.getUf9Name() + "コード" + ip + "行目",  ks.uf9.getHissuFlgS(), "0"},}, 1);
			if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{sakiUf10Cd[i], "（付替先）" + hfUfSeigyo.getUf10Name() + "コード" + ip + "行目",  ks.uf10.getHissuFlgS(), "0"},}, 1);

			if( !pjShiyouFlg.equals("0") && ks.project.getHyoujiFlg() ) {
				list = new String[][]{
					{sakiProjectCd[i] ,"（付替先）" + ks.project.getName() + "コード"					,ks.project.getHissuFlgS()},
					{sakiProjectName[i] ,"（付替先）" + ks.project.getName() + "名"						,ks.project.getHissuFlgS()},
			};
				hissuCheckCommon(list, 1);
			}
			
			if( !segmentShiyouFlg.equals("0") && ks.segment.getHyoujiFlg() ) {
				list = new String[][]{
					{sakiSegmentCd[i] ,"（付替先）" + ks.segment.getName() + "コード"					,ks.segment.getHissuFlgS()},
					{sakiSegmentName[i] ,"（付替先）" + ks.segment.getName() + "名"						,ks.segment.getHissuFlgS()},
			};
				hissuCheckCommon(list, 1);
			}
		}
	};

	/**
	 * 業務関連の関連チェック処理
	 */
	protected void soukanCheck() {

		//税率チェック
		commonLogic.checkZeiritsu(toDecimal(zeiritsu), keigenZeiritsuKbn, toDate(denpyouDate), errorList);
		
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
		commonLogic.checkShiwake(denpyouKbn, shiwakeCheckDataNaiyou, errorList);

		// 借方の共通チェック
		ShiwakeCheckData motoCheckData = commonLogic.new ShiwakeCheckData() ;
		motoCheckData.kamokuNm = "（付替元）" + ks.kamoku.getName() +"コード";
		motoCheckData.kamokuCd = motoKamokuCd;
		motoCheckData.kamokuEdabanNm = "（付替元）" + ks.kamokuEdaban.getName() +"コード";
		motoCheckData.kamokuEdabanCd = motoKamokuEdabanCd;
		motoCheckData.futanBumonNm = "（付替元）" + ks.futanBumon.getName() +"コード";
		motoCheckData.futanBumonCd = motoFutanBumonCd;
		motoCheckData.torihikisakiNm = "（付替元）" + ks.torihikisaki.getName() +"コード";
		motoCheckData.torihikisakiCd = motoTorihikisakiCd;
		motoCheckData.projectNm = "（付替元）" + ks.project.getName() +"コード";
		motoCheckData.projectCd = motoProjectCd;
		motoCheckData.segmentNm = "（付替元）" + ks.segment.getName() +"コード";
		motoCheckData.segmentCd = motoSegmentCd;
		motoCheckData.uf1Nm = "（付替元）" + hfUfSeigyo.getUf1Name();
		motoCheckData.uf1Cd = motoUf1Cd;
		motoCheckData.uf2Nm = "（付替元）" + hfUfSeigyo.getUf2Name();
		motoCheckData.uf2Cd = motoUf2Cd;
		motoCheckData.uf3Nm = "（付替元）" + hfUfSeigyo.getUf3Name();
		motoCheckData.uf3Cd = motoUf3Cd;
		motoCheckData.uf4Nm = "（付替元）" + hfUfSeigyo.getUf4Name();
		motoCheckData.uf4Cd = motoUf4Cd;
		motoCheckData.uf5Nm = "（付替元）" + hfUfSeigyo.getUf5Name();
		motoCheckData.uf5Cd = motoUf5Cd;
		motoCheckData.uf6Nm = "（付替元）" + hfUfSeigyo.getUf6Name();
		motoCheckData.uf6Cd = motoUf6Cd;
		motoCheckData.uf7Nm = "（付替元）" + hfUfSeigyo.getUf7Name();
		motoCheckData.uf7Cd = motoUf7Cd;
		motoCheckData.uf8Nm = "（付替元）" + hfUfSeigyo.getUf8Name();
		motoCheckData.uf8Cd = motoUf8Cd;
		motoCheckData.uf9Nm = "（付替元）" + hfUfSeigyo.getUf9Name();
		motoCheckData.uf9Cd = motoUf9Cd;
		motoCheckData.uf10Nm = "（付替元）" + hfUfSeigyo.getUf10Name();
		motoCheckData.uf10Cd = motoUf10Cd;
		motoCheckData.kazeiKbnNm = "（付替元）" + ks.kazeiKbn.getName();
		motoCheckData.kazeiKbn = motoKazeiKbn;
		commonLogic.checkShiwake(denpyouKbn, motoCheckData, errorList);
		
		// 取引先仕入先チェック
		super.checkShiiresaki("（付替元）" + ks.torihikisaki.getName() +"コード", motoTorihikisakiCd, denpyouKbn);

		// 貸方の共通チェック
		for (int i = 0; i < sakiKamokuCd.length; i++) {
			int ip = i + 1;
			ShiwakeCheckData sakiCheckData = commonLogic.new ShiwakeCheckData() ;
			sakiCheckData.kamokuNm = ip + "行目：（付替先）" + ks.kamoku.getName() +"コード";
			sakiCheckData.kamokuCd = sakiKamokuCd[i];
			sakiCheckData.kamokuEdabanNm = ip + "行目：（付替先）" + ks.kamokuEdaban.getName() +"コード";
			sakiCheckData.kamokuEdabanCd = sakiKamokuEdabanCd[i];
			sakiCheckData.futanBumonNm  	= ip + "行目：（付替先）" + ks.futanBumon.getName() +"コード";
			sakiCheckData.futanBumonCd = sakiFutanBumonCd[i];
			sakiCheckData.torihikisakiNm = ip + "行目：（付替先）" + ks.torihikisaki.getName() +"コード";
			sakiCheckData.torihikisakiCd = sakiTorihikisakiCd[i];
			sakiCheckData.projectNm = ip + "行目：（付替先）" + ks.project.getName() +"コード";
			sakiCheckData.projectCd = sakiProjectCd[i];
			sakiCheckData.segmentNm = ip + "行目：（付替先）" + ks.segment.getName() +"コード";
			sakiCheckData.segmentCd = sakiSegmentCd[i];
			sakiCheckData.uf1Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf1Name();
			sakiCheckData.uf1Cd = sakiUf1Cd[i];
			sakiCheckData.uf2Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf2Name();
			sakiCheckData.uf2Cd = sakiUf2Cd[i];
			sakiCheckData.uf3Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf3Name();
			sakiCheckData.uf3Cd = sakiUf3Cd[i];
			sakiCheckData.uf4Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf4Name();
			sakiCheckData.uf4Cd = sakiUf4Cd[i];
			sakiCheckData.uf5Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf5Name();
			sakiCheckData.uf5Cd = sakiUf5Cd[i];
			sakiCheckData.uf6Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf6Name();
			sakiCheckData.uf6Cd = sakiUf6Cd[i];
			sakiCheckData.uf7Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf7Name();
			sakiCheckData.uf7Cd = sakiUf7Cd[i];
			sakiCheckData.uf8Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf8Name();
			sakiCheckData.uf8Cd = sakiUf8Cd[i];
			sakiCheckData.uf9Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf9Name();
			sakiCheckData.uf9Cd = sakiUf9Cd[i];
			sakiCheckData.uf10Nm = ip + "行目：（付替先）" + hfUfSeigyo.getUf10Name();
			sakiCheckData.uf10Cd = sakiUf10Cd[i];
			sakiCheckData.kazeiKbnNm = ip + "行目：（付替先）" + ks.kazeiKbn.getName();
			sakiCheckData.kazeiKbn = sakiKazeiKbn[i];
			commonLogic.checkShiwake(denpyouKbn, sakiCheckData, errorList);
			
			// 取引先仕入先チェック
			super.checkShiiresaki(ip + "行目：（付替先）" + ks.torihikisaki.getName() + "コード", sakiTorihikisakiCd[i], denpyouKbn);
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
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (usrInfo == null) ? "" : (String)usrInfo.get("shain_no");
		
		//新規起票時の表示状態作成
		if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {
			makeDefaultShinsei();
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

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			Tsukekae shinseiDataMap = this.tsukekaeDao.find(denpyouId);
			shinseiData2Gamen(shinseiDataMap);

			List<TsukekaeMeisai> meisaiList = this.tsukekaeMeisaiDao.loadByDenpyouId(denpyouId);
			meisaiData2Gamen(meisaiList, sanshou, shinseiDataMap.map, connection);

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			Tsukekae shinseiDataMap = this.tsukekaeDao.find(sanshouDenpyouId);
			shinseiData2Gamen(shinseiDataMap);

			List<TsukekaeMeisai> meisaiList = this.tsukekaeMeisaiDao.loadByDenpyouId(sanshouDenpyouId);
			meisaiData2Gamen(meisaiList, sanshou, shinseiDataMap.map, connection);
			
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
		//処理グループの取得
		if (isNotEmpty(motoKamokuCd)) {
			GMap motokmk = masterLogic.findKamokuMaster(motoKamokuCd);
			if (motokmk != null) {
				motoShoriGroup = motokmk.get("shori_group").toString();
			}
		}
		sakiShoriGroup = new String[sakiKamokuCd.length];
		for (int i = 0; i < sakiKamokuCd.length; i++) {
			if (isNotEmpty(sakiKamokuCd[i])) {
				GMap sakikmk = masterLogic.findKamokuMaster(sakiKamokuCd[i]);
				if (sakikmk != null) {
					sakiShoriGroup[i] = sakikmk.get("shori_group").toString();
				}
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
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}
	}

	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		//マスター等から名称は引き直す
		reloadMaster(connection);

		//付替元　登録
		Tsukekae dto = this.createDto();
		this.tsukekaeDao.insert(dto, dto.tourokuUserId);

		//付替先　登録
		for (int i = 0; i < sakiTekiyou.length; i++) {
			TsukekaeMeisai meisaiDto = this.createMeisaiDto(i);
			this.tsukekaeMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
		}
		
		// 登録時のみ設定を保持
		userSettingLogic.upsertUserDefaultValue(UserDefaultValueKbn.tsukekaeKbn, loginUserId, tsukekaeKbn);
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
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}

		//マスター等から名称は引き直す
		reloadMaster(connection);

		//付替元　登録
		Tsukekae dto = this.createDto();
		this.tsukekaeDao.update(dto, dto.koushinUserId);

		//明細削除
		this.tsukekaeMeisaiDao.deleteByDenpyouId(this.denpyouId);

		//付替先　登録
		for (int i = 0; i < sakiTekiyou.length; i++) {
			TsukekaeMeisai meisaiDto = this.createMeisaiDto(i);
			this.tsukekaeMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
		}
		
		// 起票ユーザーの更新時のみ設定を更新
		if (loginUserId.equals(super.getKihyouUserId())) {
			userSettingLogic.upsertUserDefaultValue(UserDefaultValueKbn.tsukekaeKbn, loginUserId, tsukekaeKbn);
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
		return denpyouDate;
	}
	//＜内部処理＞
	/**
	 * 各Logic対象の初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		gyoumuLogic = EteamContainer.getComponent(SougouTsukekaeDenpyouLogic.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		userSettingLogic = EteamContainer.getComponent(EteamUserSettingLogic.class, connection);
		this.tsukekaeDao = EteamContainer.getComponent(TsukekaeDao.class, connection);
		this.tsukekaeMeisaiDao = EteamContainer.getComponent(TsukekaeMeisaiDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.kiKesnDao = EteamContainer.getComponent(KiKesnDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
	}
	
	/**
	 * 未登録の時、デフォルトの申請内容表示状態に項目セットする。
	 */
	protected void makeDefaultShinsei() {
		shouhyouShoruiFlg = SHOUHYOU_SHORUI.NASI;
		zeiritsu = masterLogic.findValidShouhizeiritsuMap().getString("zeiritsu");
		keigenZeiritsuKbn = masterLogic.findValidShouhizeiritsuMap().getString("keigen_zeiritsu_kbn");
		GMap map = userSettingLogic.selectUserDefaultValue(UserDefaultValueKbn.tsukekaeKbn, super.loginUserId); 
		tsukekaeKbn =  (null != map) ? map.getString("default_value") : TSUKEKAE_KBN.KARIKATA_KOUTEI;
	}

	/**
	 * 付替テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 付替申請レコード
	 */
	protected void shinseiData2Gamen(Tsukekae shinseiData) {
		//申請内容
		denpyouDate = formatDate(shinseiData.denpyouDate);
		shouhyouShoruiFlg = shinseiData.shouhyouShoruiFlg;
		zeiritsu = formatMoney(shinseiData.zeiritsu);
		keigenZeiritsuKbn = shinseiData.keigenZeiritsuKbn;
		tsukekaeKbn = shinseiData.tsukekaeKbn;
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
		invoiceDenpyou = shinseiData.invoiceDenpyou;
		hosoku = shinseiData.hosoku;
		//付替元
		motoKamokuCd = shinseiData.motoKamokuCd;
		motoKamokuName = shinseiData.motoKamokuName;
		motoKamokuEdabanCd = shinseiData.motoKamokuEdabanCd;
		motoKamokuEdabanName = shinseiData.motoKamokuEdabanName;
		motoFutanBumonCd = shinseiData.motoFutanBumonCd;
		motoFutanBumonName = shinseiData.motoFutanBumonName;
		motoTorihikisakiCd = shinseiData.motoTorihikisakiCd;
		motoTorihikisakiNameRyakushiki = shinseiData.motoTorihikisakiNameRyakushiki;
		motoUf1Cd = shinseiData.motoUf1Cd;
		motoUf1Name = shinseiData.motoUf1NameRyakushiki;
		motoUf2Cd = shinseiData.motoUf2Cd;
		motoUf2Name = shinseiData.motoUf2NameRyakushiki;
		motoUf3Cd = shinseiData.motoUf3Cd;
		motoUf3Name = shinseiData.motoUf3NameRyakushiki;
		motoUf4Cd = shinseiData.motoUf4Cd;
		motoUf4Name = shinseiData.motoUf4NameRyakushiki;
		motoUf5Cd = shinseiData.motoUf5Cd;
		motoUf5Name = shinseiData.motoUf5NameRyakushiki;
		motoUf6Cd = shinseiData.motoUf6Cd;
		motoUf6Name = shinseiData.motoUf6NameRyakushiki;
		motoUf7Cd = shinseiData.motoUf7Cd;
		motoUf7Name = shinseiData.motoUf7NameRyakushiki;
		motoUf8Cd = shinseiData.motoUf8Cd;
		motoUf8Name = shinseiData.motoUf8NameRyakushiki;
		motoUf9Cd = shinseiData.motoUf9Cd;
		motoUf9Name = shinseiData.motoUf9NameRyakushiki;
		motoUf10Cd = shinseiData.motoUf10Cd;
		motoUf10Name = shinseiData.motoUf10NameRyakushiki;
		motoKazeiKbn = shinseiData.motoKazeiKbn;
		motoProjectCd = "0".equals(pjShiyouFlg) || !ks.project.getHyoujiFlg() ? "": shinseiData.motoProjectCd;
		motoProjectName = "0".equals(pjShiyouFlg) || !ks.project.getHyoujiFlg() ? "": shinseiData.motoProjectName;
		motoSegmentCd = "0".equals(segmentShiyouFlg) || !ks.segment.getHyoujiFlg() ? "": shinseiData.motoSegmentCd;
		motoSegmentName = "0".equals(segmentShiyouFlg) || !ks.segment.getHyoujiFlg() ? "": shinseiData.motoSegmentNameRyakushiki;
		kingaku = formatMoney(shinseiData.kingakuGoukei);
		motoFutanBumonCd = shinseiData.motoFutanBumonCd;
		motoBunriKbn = shinseiData.motoBunriKbn;
		motoShiireKbn = shinseiData.motoShiireKbn;
		motoJigyoushaKbn = shinseiData.motoJigyoushaKbn;
		motoZeigakuHoushiki = shinseiData.motoZeigakuHoushiki;
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisai() {
		String[] empty = {""};
		sakiKamokuCd = empty;
		sakiKamokuName = empty;
		sakiKamokuEdabanCd = empty;
		sakiKamokuEdabanName = empty;
		sakiFutanBumonCd = empty;
		sakiFutanBumonName = empty;
		sakiTorihikisakiCd = empty;
		sakiTorihikisakiNameRyakushiki = empty;
		sakiUf1Cd = empty;
		sakiUf1Name = empty;
		sakiUf2Cd = empty;
		sakiUf2Name = empty;
		sakiUf3Cd = empty;
		sakiUf3Name = empty;
		sakiUf4Cd = empty;
		sakiUf4Name = empty;
		sakiUf5Cd = empty;
		sakiUf5Name = empty;
		sakiUf6Cd = empty;
		sakiUf6Name = empty;
		sakiUf7Cd = empty;
		sakiUf7Name = empty;
		sakiUf8Cd = empty;
		sakiUf8Name = empty;
		sakiUf9Cd = empty;
		sakiUf9Name = empty;
		sakiUf10Cd = empty;
		sakiUf10Name = empty;
		sakiProjectCd = empty;
		sakiProjectName = empty;
		sakiSegmentCd = empty;
		sakiSegmentName = empty;
		sakiKazeiKbn = empty;
		sakiKingaku = empty;
		sakiHontaiKingaku = empty;
		sakiShouhizeigaku = empty;
		sakiTekiyou = empty;
		chuuki2 = empty;
		sakiBikou = empty;
		sakiBunriKbn = empty;
		sakiShiireKbn = empty;
		sakiJigyoushaKbn = empty;
		sakiZeigakuHoushiki = empty;
	}

	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList     明細レコードのリスト
	 * @param sanshou        参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param shinseiDataMap 伝票データ
	 * @param connection     コネクション
	 */
	protected void meisaiData2Gamen(List<TsukekaeMeisai> meisaiList, boolean sanshou, GMap shinseiDataMap, EteamConnection connection) {
		int length = meisaiList.size();
		sakiKamokuCd = new String[length];
		sakiKamokuName = new String[length];
		sakiKamokuEdabanCd = new String[length];
		sakiKamokuEdabanName = new String[length];
		sakiFutanBumonCd = new String[length];
		sakiFutanBumonName = new String[length];
		sakiTorihikisakiCd = new String[length];
		sakiTorihikisakiNameRyakushiki = new String[length];
		sakiUf1Cd = new String[length];
		sakiUf1Name = new String[length];
		sakiUf2Cd = new String[length];
		sakiUf2Name = new String[length];
		sakiUf3Cd = new String[length];
		sakiUf3Name = new String[length];
		sakiUf4Cd = new String[length];
		sakiUf4Name = new String[length];
		sakiUf5Cd = new String[length];
		sakiUf5Name = new String[length];
		sakiUf6Cd = new String[length];
		sakiUf6Name = new String[length];
		sakiUf7Cd = new String[length];
		sakiUf7Name = new String[length];
		sakiUf8Cd = new String[length];
		sakiUf8Name = new String[length];
		sakiUf9Cd = new String[length];
		sakiUf9Name = new String[length];
		sakiUf10Cd = new String[length];
		sakiUf10Name = new String[length];
		sakiProjectCd = new String[length];
		sakiProjectName = new String[length];
		sakiSegmentCd = new String[length];
		sakiSegmentName = new String[length];
		sakiKazeiKbn = new String[length];
		sakiKingaku = new String[length];
		sakiHontaiKingaku = new String[length];
		sakiShouhizeigaku = new String[length];
		sakiTekiyou = new String[length];
		chuuki2 = new String[length];
		sakiBikou = new String[length];
		sakiJigyoushaKbn = new String[length];
		sakiBunriKbn = new String[length];
		sakiShiireKbn = new String[length];
		sakiZeigakuHoushiki = new String[length];
		for (int i = 0; i < length; i++) {
			TsukekaeMeisai meisai = meisaiList.get(i);
			sakiKamokuCd[i] = meisai.sakiKamokuCd;
			sakiKamokuName[i] = meisai.sakiKamokuName;
			sakiKamokuEdabanCd[i] = meisai.sakiKamokuEdabanCd;
			sakiKamokuEdabanName[i] = meisai.sakiKamokuEdabanName;
			sakiFutanBumonCd[i] = meisai.sakiFutanBumonCd;
			sakiFutanBumonName[i] = meisai.sakiFutanBumonName;
			sakiTorihikisakiCd[i] = meisai.sakiTorihikisakiCd;
			sakiTorihikisakiNameRyakushiki[i] = meisai.sakiTorihikisakiNameRyakushiki;
			sakiUf1Cd[i] = meisai.sakiUf1Cd;
			sakiUf1Name[i] = meisai.sakiUf1NameRyakushiki;
			sakiUf2Cd[i] = meisai.sakiUf2Cd;
			sakiUf2Name[i] = meisai.sakiUf2NameRyakushiki;
			sakiUf3Cd[i] = meisai.sakiUf3Cd;
			sakiUf3Name[i] = meisai.sakiUf3NameRyakushiki;
			sakiUf4Cd[i] = meisai.sakiUf4Cd;
			sakiUf4Name[i] = meisai.sakiUf4NameRyakushiki;
			sakiUf5Cd[i] = meisai.sakiUf5Cd;
			sakiUf5Name[i] = meisai.sakiUf5NameRyakushiki;
			sakiUf6Cd[i] = meisai.sakiUf6Cd;
			sakiUf6Name[i] = meisai.sakiUf6NameRyakushiki;
			sakiUf7Cd[i] = meisai.sakiUf7Cd;
			sakiUf7Name[i] = meisai.sakiUf7NameRyakushiki;
			sakiUf8Cd[i] = meisai.sakiUf8Cd;
			sakiUf8Name[i] = meisai.sakiUf8NameRyakushiki;
			sakiUf9Cd[i] = meisai.sakiUf9Cd;
			sakiUf9Name[i] = meisai.sakiUf9NameRyakushiki;
			sakiUf10Cd[i] = meisai.sakiUf10Cd;
			sakiUf10Name[i] = meisai.sakiUf10NameRyakushiki;
			sakiProjectCd[i] = "0".equals(pjShiyouFlg) || !ks.project.getHyoujiFlg() ? "": meisai.sakiProjectCd;
			sakiProjectName[i] = "0".equals(pjShiyouFlg) || !ks.project.getHyoujiFlg() ? "": meisai.sakiProjectName;
			sakiSegmentCd[i] = "0".equals(segmentShiyouFlg) || !ks.segment.getHyoujiFlg() ? "": meisai.sakiSegmentCd;
			sakiSegmentName[i] = "0".equals(segmentShiyouFlg) || !ks.segment.getHyoujiFlg() ? "": meisai.sakiSegmentNameRyakushiki;
			sakiKazeiKbn[i] = meisai.sakiKazeiKbn;
			sakiKingaku[i] = formatMoney(meisai.kingaku);
			sakiHontaiKingaku[i] = formatMoney(meisai.hontaiKingaku);
			sakiShouhizeigaku[i] = formatMoney(meisai.shouhizeigaku);
			sakiJigyoushaKbn[i] = meisai.sakiJigyoushaKbn;
			sakiBunriKbn[i] = meisai.sakiBunriKbn;
			sakiShiireKbn[i] = meisai.sakiShiireKbn;
			sakiTekiyou[i] = meisai.tekiyou;
			sakiZeigakuHoushiki[i] = meisai.sakiZeigakuHoushiki;
			if(!sanshou){
				String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU, shinseiDataMap, meisai.map, "0");
				String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
				if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
					chuuki1 = commonLogic.getTekiyouChuuki();
					chuuki2[i] = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
				}
			}
			sakiBikou[i] = meisai.bikou;
		}
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		//プルダウンのリストを取得
		shouhyouShoruiList = systemLogic.loadNaibuCdSetting("shouhyou_shorui");
		tsukekaeKbnList = systemLogic.loadNaibuCdSetting("tsukekae_kbn");
		zeiritsuList = zeiritsuDao.load().stream().map(Shouhizeiritsu::getMap).collect(Collectors.toList());
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
		boolean enableMotoBumonSecurity = commonLogic.checkFutanBumonWithSecurity(motoFutanBumonCd, "（付替元）" + ks.futanBumon.getName() + "コード" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
		
		//付替元
		motoKamokuName = masterLogic.findKamokuNameStr(motoKamokuCd);
		motoKamokuEdabanName = masterLogic.findKamokuEdabanName(motoKamokuCd, motoKamokuEdabanCd);
		motoFutanBumonName = enableMotoBumonSecurity ? masterLogic.findFutanBumonName(motoFutanBumonCd) : "";
		motoTorihikisakiNameRyakushiki = masterLogic.findTorihikisakiName(motoTorihikisakiCd);
		motoUf1Name = masterLogic.findUfName("1", motoUf1Cd);
		motoUf2Name = masterLogic.findUfName("2", motoUf2Cd);
		motoUf3Name = masterLogic.findUfName("3", motoUf3Cd);
		motoUf4Name = masterLogic.findUfName("4", motoUf4Cd);
		motoUf5Name = masterLogic.findUfName("5", motoUf5Cd);
		motoUf6Name = masterLogic.findUfName("6", motoUf6Cd);
		motoUf7Name = masterLogic.findUfName("7", motoUf7Cd);
		motoUf8Name = masterLogic.findUfName("8", motoUf8Cd);
		motoUf9Name = masterLogic.findUfName("9", motoUf9Cd);
		motoUf10Name = masterLogic.findUfName("10", motoUf10Cd);
		motoProjectName = masterLogic.findProjectName(motoProjectCd);
		motoSegmentName = masterLogic.findSegmentName(motoSegmentCd);

		//付替先
		for (int i = 0; i < sakiKamokuCd.length; i++) {
			//負担部門任意入力の場合、起票ユーザで使用できるかチェック
			boolean enableSakiBumonSecurity = commonLogic.checkFutanBumonWithSecurity(sakiFutanBumonCd[i], "（付替先）" + ks.futanBumon.getName() + "コード" 	+ (i+1) + "行目" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
			
			sakiKamokuName[i] = masterLogic.findKamokuNameStr(sakiKamokuCd[i]);
			sakiKamokuEdabanName[i] = masterLogic.findKamokuEdabanName(sakiKamokuCd[i], sakiKamokuEdabanCd[i]);
			sakiFutanBumonName[i] = enableSakiBumonSecurity ? masterLogic.findFutanBumonName(sakiFutanBumonCd[i]) : "";
			sakiTorihikisakiNameRyakushiki[i] = masterLogic.findTorihikisakiName(sakiTorihikisakiCd[i]);
			sakiUf1Name[i] = masterLogic.findUfName("1", sakiUf1Cd[i]);
			sakiUf2Name[i] = masterLogic.findUfName("2", sakiUf2Cd[i]);
			sakiUf3Name[i] = masterLogic.findUfName("3", sakiUf3Cd[i]);
			sakiUf4Name[i] = masterLogic.findUfName("4", sakiUf4Cd[i]);
			sakiUf5Name[i] = masterLogic.findUfName("5", sakiUf5Cd[i]);
			sakiUf6Name[i] = masterLogic.findUfName("6", sakiUf6Cd[i]);
			sakiUf7Name[i] = masterLogic.findUfName("7", sakiUf7Cd[i]);
			sakiUf8Name[i] = masterLogic.findUfName("8", sakiUf8Cd[i]);
			sakiUf9Name[i] = masterLogic.findUfName("9", sakiUf9Cd[i]);
			sakiUf10Name[i] = masterLogic.findUfName("10", sakiUf10Cd[i]);
			sakiProjectName[i] = masterLogic.findProjectName(sakiProjectCd[i]);
			sakiSegmentName[i] = masterLogic.findSegmentName(sakiSegmentCd[i]);
		}
	}
	/**
	 * 総合付替伝票EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */ 
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		SougouTsukekaeDenpyouXlsLogic sougoutsukekaedenpyouxlsLogic = EteamContainer.getComponent(SougouTsukekaeDenpyouXlsLogic.class, connection);
		sougoutsukekaedenpyouxlsLogic.makeExcel(denpyouId, out);
	}
	
	/**
	 * @return 付替Dto
	 */
	protected Tsukekae createDto()
	{
		Tsukekae tsukekae = new Tsukekae();
		tsukekae.denpyouId = this.denpyouId;
		tsukekae.denpyouDate = super.toDate(denpyouDate);
		tsukekae.shouhyouShoruiFlg = this.shouhyouShoruiFlg;
		tsukekae.zeiritsu = super.toDecimal(zeiritsu);
		tsukekae.keigenZeiritsuKbn = this.keigenZeiritsuKbn;
		tsukekae.kingakuGoukei = super.toDecimal(kingaku);
		tsukekae.hf1Cd = this.hf1Cd;
		tsukekae.hf1NameRyakushiki = this.hf1Name;
		tsukekae.hf2Cd = this.hf2Cd;
		tsukekae.hf2NameRyakushiki = this.hf2Name;
		tsukekae.hf3Cd = this.hf3Cd;
		tsukekae.hf3NameRyakushiki = this.hf3Name;
		tsukekae.hf4Cd = this.hf4Cd;
		tsukekae.hf4NameRyakushiki = this.hf4Name;
		tsukekae.hf5Cd = this.hf5Cd;
		tsukekae.hf5NameRyakushiki = this.hf5Name;
		tsukekae.hf6Cd = this.hf6Cd;
		tsukekae.hf6NameRyakushiki = this.hf6Name;
		tsukekae.hf7Cd = this.hf7Cd;
		tsukekae.hf7NameRyakushiki = this.hf7Name;
		tsukekae.hf8Cd = this.hf8Cd;
		tsukekae.hf8NameRyakushiki = this.hf8Name;
		tsukekae.hf9Cd = this.hf9Cd;
		tsukekae.hf9NameRyakushiki = this.hf9Name;
		tsukekae.hf10Cd = this.hf10Cd;
		tsukekae.hf10NameRyakushiki = this.hf10Name;
		tsukekae.hosoku = this.hosoku;
		tsukekae.tsukekaeKbn = this.tsukekaeKbn;
		tsukekae.motoKamokuCd = this.motoKamokuCd;
		tsukekae.motoKamokuName = this.motoKamokuName;
		tsukekae.motoKamokuEdabanCd = this.motoKamokuEdabanCd;
		tsukekae.motoKamokuEdabanName = this.motoKamokuEdabanName;
		tsukekae.motoFutanBumonCd = this.motoFutanBumonCd;
		tsukekae.motoFutanBumonName = this.motoFutanBumonName;
		tsukekae.motoTorihikisakiCd = this.motoTorihikisakiCd;
		tsukekae.motoTorihikisakiNameRyakushiki = this.motoTorihikisakiNameRyakushiki;
		tsukekae.motoKazeiKbn = this.motoKazeiKbn;
		tsukekae.motoUf1Cd = this.motoUf1Cd;
		tsukekae.motoUf1NameRyakushiki = this.motoUf1Name;
		tsukekae.motoUf2Cd = this.motoUf2Cd;
		tsukekae.motoUf2NameRyakushiki = this.motoUf2Name;
		tsukekae.motoUf3Cd = this.motoUf3Cd;
		tsukekae.motoUf3NameRyakushiki = this.motoUf3Name;
		tsukekae.motoUf4Cd = this.motoUf4Cd;
		tsukekae.motoUf4NameRyakushiki = this.motoUf4Name;
		tsukekae.motoUf5Cd = this.motoUf5Cd;
		tsukekae.motoUf5NameRyakushiki = this.motoUf5Name;
		tsukekae.motoUf6Cd = this.motoUf6Cd;
		tsukekae.motoUf6NameRyakushiki = this.motoUf6Name;
		tsukekae.motoUf7Cd = this.motoUf7Cd;
		tsukekae.motoUf7NameRyakushiki = this.motoUf7Name;
		tsukekae.motoUf8Cd = this.motoUf8Cd;
		tsukekae.motoUf8NameRyakushiki = this.motoUf8Name;
		tsukekae.motoUf9Cd = this.motoUf9Cd;
		tsukekae.motoUf9NameRyakushiki = this.motoUf9Name;
		tsukekae.motoUf10Cd = this.motoUf10Cd;
		tsukekae.motoUf10NameRyakushiki = this.motoUf10Name;
		tsukekae.motoProjectCd = this.motoProjectCd;
		tsukekae.motoProjectName = this.motoProjectName;
		tsukekae.motoSegmentCd = this.motoSegmentCd;
		tsukekae.motoSegmentNameRyakushiki = this.motoSegmentName;
		tsukekae.tourokuUserId = this.loginUserId;
		tsukekae.koushinUserId = this.loginUserId;
		tsukekae.motoJigyoushaKbn = this.motoJigyoushaKbn == null ? "0" : this.motoJigyoushaKbn;
		tsukekae.motoBunriKbn = this.motoBunriKbn;
		tsukekae.motoShiireKbn = this.motoShiireKbn;
		tsukekae.motoZeigakuHoushiki = this.motoZeigakuHoushiki == null ? "0" : this.motoZeigakuHoushiki;
		tsukekae.invoiceDenpyou = this.invoiceDenpyou;
		return tsukekae;
	} 
	/**
	 * @return 付替Dto
	 */
	protected TsukekaeMeisai createMeisaiDto(int i)
	{
		TsukekaeMeisai tsukekaeMeisai = new TsukekaeMeisai();
		tsukekaeMeisai.denpyouId = this.denpyouId;
		tsukekaeMeisai.denpyouEdano = i + 1;
		tsukekaeMeisai.tekiyou = this.sakiTekiyou[i];
		tsukekaeMeisai.kingaku = this.toDecimal(sakiKingaku[i]);
		tsukekaeMeisai.hontaiKingaku = this.toDecimal(sakiHontaiKingaku[i]);
		tsukekaeMeisai.shouhizeigaku = this.toDecimal(sakiShouhizeigaku[i]);
		tsukekaeMeisai.bikou = this.sakiBikou[i];
		tsukekaeMeisai.sakiKamokuCd = this.sakiKamokuCd[i];
		tsukekaeMeisai.sakiKamokuName = this.sakiKamokuName[i];
		tsukekaeMeisai.sakiKamokuEdabanCd = this.sakiKamokuEdabanCd[i];
		tsukekaeMeisai.sakiKamokuEdabanName = this.sakiKamokuEdabanName[i];
		tsukekaeMeisai.sakiFutanBumonCd = this.sakiFutanBumonCd[i];
		tsukekaeMeisai.sakiFutanBumonName = this.sakiFutanBumonName[i];
		tsukekaeMeisai.sakiTorihikisakiCd = this.sakiTorihikisakiCd[i];
		tsukekaeMeisai.sakiTorihikisakiNameRyakushiki = this.sakiTorihikisakiNameRyakushiki[i];
		tsukekaeMeisai.sakiKazeiKbn = this.sakiKazeiKbn[i];
		tsukekaeMeisai.sakiUf1Cd = this.sakiUf1Cd[i];
		tsukekaeMeisai.sakiUf1NameRyakushiki = this.sakiUf1Name[i];
		tsukekaeMeisai.sakiUf2Cd = this.sakiUf2Cd[i];
		tsukekaeMeisai.sakiUf2NameRyakushiki = this.sakiUf2Name[i];
		tsukekaeMeisai.sakiUf3Cd = this.sakiUf3Cd[i];
		tsukekaeMeisai.sakiUf3NameRyakushiki = this.sakiUf3Name[i];
		tsukekaeMeisai.sakiUf4Cd = this.sakiUf4Cd[i];
		tsukekaeMeisai.sakiUf4NameRyakushiki = this.sakiUf4Name[i];
		tsukekaeMeisai.sakiUf5Cd = this.sakiUf5Cd[i];
		tsukekaeMeisai.sakiUf5NameRyakushiki = this.sakiUf5Name[i];
		tsukekaeMeisai.sakiUf6Cd = this.sakiUf6Cd[i];
		tsukekaeMeisai.sakiUf6NameRyakushiki = this.sakiUf6Name[i];
		tsukekaeMeisai.sakiUf7Cd = this.sakiUf7Cd[i];
		tsukekaeMeisai.sakiUf7NameRyakushiki = this.sakiUf7Name[i];
		tsukekaeMeisai.sakiUf8Cd = this.sakiUf8Cd[i];
		tsukekaeMeisai.sakiUf8NameRyakushiki = this.sakiUf8Name[i];
		tsukekaeMeisai.sakiUf9Cd = this.sakiUf9Cd[i];
		tsukekaeMeisai.sakiUf9NameRyakushiki = this.sakiUf9Name[i];
		tsukekaeMeisai.sakiUf10Cd = this.sakiUf10Cd[i];
		tsukekaeMeisai.sakiUf10NameRyakushiki = this.sakiUf10Name[i];
		tsukekaeMeisai.sakiProjectCd = this.sakiProjectCd[i];
		tsukekaeMeisai.sakiProjectName = this.sakiProjectName[i];
		tsukekaeMeisai.sakiSegmentCd = this.sakiSegmentCd[i];
		tsukekaeMeisai.sakiSegmentNameRyakushiki = this.sakiSegmentName[i];
		tsukekaeMeisai.tourokuUserId = this.loginUserId;
		tsukekaeMeisai.koushinUserId = this.loginUserId;
		tsukekaeMeisai.sakiJigyoushaKbn = this.sakiJigyoushaKbn[i];
		tsukekaeMeisai.sakiBunriKbn = this.sakiBunriKbn[i];
		tsukekaeMeisai.sakiShiireKbn = this.sakiShiireKbn[i];
		tsukekaeMeisai.sakiZeigakuHoushiki = this.sakiZeigakuHoushiki[i];

		return tsukekaeMeisai;
	}
}
