package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.RegAccess;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 仮払申請画面Action
 */
@Getter @Setter @ToString
public class KaribaraiShinseiAction extends WorkflowEventControl {

//＜定数＞

//＜画面入力＞
	/** 仮払ありなし */
	String karibaraiOn;
	/** 支払方法 */
	String shiharaihouhou;
	/** 支払希望日 */
	String shiharaiKiboubi;
	/** 仕訳枝番号 */
	String shiwakeEdaNo;
	/** 取引名 */
	String torihikiName;
	/** 勘定科目コード */
	String kamokuCd;
	/** 勘定科目名 */
	String kamokuName;
	/** 借方科目枝番コード */
	String kamokuEdabanCd;
	/** 借方科目枝番名 */
	String kamokuEdabanName;
	/** 負担部門コード */
	String futanBumonCd;
	/** 負担部門名 */
	String futanBumonName;
	/** 取引先コード */
	String torihikisakiCd;
	/** 取引先名 */
	String torihikisakiName;
	/** プロジェクトコード */
	String projectCd;
	/** プロジェクト名 */
	String projectName;
	/** セグメントコード */
	String segmentCd;
	/** セグメント名 */
	String segmentName;
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
	/** 精算予定日 */
	String seisanYoteiBi;
	/** 摘要 */
	String tekiyou;
	/** 共通部分摘要注記 */
	String chuuki1;
	/** 伝票摘要注記 */
	String chuuki2;
	/** 申請期間チェック注記 */
	String shinseiChuuki;
	/** 申請金額 */
	String shinseiKingaku;
	/** 仮払金額 */
	String karibaraiKingaku;
	/** 支払日 */
	String shiharaiBi;
	/** 使用目的 */
	String shiyouMokuteki;

//＜画面入力以外＞

	//プルダウン等
	/** 支払方法のDropDownList */
	List<GMap> shiharaihouhouList;
	/** 支払方法ドメイン */
	String[] shiharaihouhouDomain;

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();

	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KARIBARAI_SHINSEI);

	/** 入力モード */
	boolean enableInput;
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean kamokuEdabanEnable;
	/** 負担ボタン押下可否 */
	boolean futanBumonEnable;
	/** 取引先選択ボタン押下可否 */
	boolean torihikisakiEnable;
	/** プロジェクト選択ボタン */
	boolean projectEnable;
	/** セグメント選択ボタン */
	boolean segmentEnable;
	/** UF1ボタン押下可否 */
	boolean uf1Enable;
	/** UF2ボタン押下可否 */
	boolean uf2Enable;
	/** UF3ボタン押下可否 */
	boolean uf3Enable;
	/** UF4ボタン選択ボタン */
	boolean uf4Enable;
	/** UF5ボタン選択ボタン */
	boolean uf5Enable;
	/** UF6ボタン選択ボタン */
	boolean uf6Enable;
	/** UF7ボタン選択ボタン */
	boolean uf7Enable;
	/** UF8ボタン選択ボタン */
	boolean uf8Enable;
	/** UF9ボタン選択ボタン */
	boolean uf9Enable;
	/** UF10ボタン選択ボタン */
	boolean uf10Enable;
	/** 支払日登録可能。0:非表示、1:入力可、2:表示 */
	int shiharaiBiMode = 0;
	/** 支払方法モード */
	boolean disableShiharaiHouhou;
	/** 仮払選択制御 */
	String karibaraiSentakuFlg = setting.keihiKaribaraiSentakuSeigyo();
	/** 予算執行対象フラグ */
	boolean yosanShikkouTaishouFlg;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean enableBumonSecurity;

	//タイトル
	/** A001:伺い申請（仮払申請）の仮払あり 伝票種別（タイトル） */
	String denpyouShubetsu;
	/** A001:伺い申請（仮払申請）の仮払なし 伝票種別（タイトル） */
	String denpyouKaribaraiNashiShubetsu;

	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** プロジェクト使用フラグ */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメント使用フラグ */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);

	//仕訳関連情報を一時的にメンバ変数経由で持つ
	/** 借方課税区分 */
	String kazeiKbn;
	/** 借方　UF1コード */
	String kariUf1Cd;
	/** 借方　UF2コード */
	String kariUf2Cd;
	/** 借方　UF3コード */
	String kariUf3Cd;
	/** 借方　UF4コード */
	String kariUf4Cd;
	/** 借方　UF5コード */
	String kariUf5Cd;
	/** 借方　UF6コード */
	String kariUf6Cd;
	/** 借方　UF7コード */
	String kariUf7Cd;
	/** 借方　UF8コード */
	String kariUf8Cd;
	/** 借方　UF9コード */
	String kariUf9Cd;
	/** 借方　UF10コード */
	String kariUf10Cd;
	/** 貸方負担部門コード */
	String kashiFutanBumonCd;
	/** 貸方負担部門名 */
	String kashiFutanBumonName;
	/** 貸方科目コード */
	String kashiKamokuCd;
	/** 貸方科目名 */
	String kashiKamokuName;
	/** 貸方科目枝番コード */
	String kashiKamokuEdabanCd;
	/** 貸方科目枝番名 */
	String kashiKamokuEdabanName;
	/** 貸方課税区分 */
	String kashiKazeiKbn;
	/** 貸方1　UF1コード */
	String kashiUf1Cd1;
	/** 貸方1　UF2コード */
	String kashiUf2Cd1;
	/** 貸方1　UF3コード */
	String kashiUf3Cd1;
	/** 貸方1　UF4コード */
	String kashiUf4Cd1;
	/** 貸方1　UF5コード */
	String kashiUf5Cd1;
	/** 貸方1　UF6コード */
	String kashiUf6Cd1;
	/** 貸方1　UF7コード */
	String kashiUf7Cd1;
	/** 貸方1　UF8コード */
	String kashiUf8Cd1;
	/** 貸方1　UF9コード */
	String kashiUf9Cd1;
	/** 貸方1　UF10コード */
	String kashiUf10Cd1;
	/** 貸方2　UF1コード */
	String kashiUf1Cd2;
	/** 貸方2　UF2コード */
	String kashiUf2Cd2;
	/** 貸方2　UF3コード */
	String kashiUf3Cd2;
	/** 貸方2　UF4コード */
	String kashiUf4Cd2;
	/** 貸方2　UF5コード */
	String kashiUf5Cd2;
	/** 貸方2　UF6コード */
	String kashiUf6Cd2;
	/** 貸方2　UF7コード */
	String kashiUf7Cd2;
	/** 貸方2　UF8コード */
	String kashiUf8Cd2;
	/** 貸方2　UF9コード */
	String kashiUf9Cd2;
	/** 貸方2　UF10コード */
	String kashiUf10Cd2;
	/** 摘要コード */
	String tekiyouCd;

//＜部品＞
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 通知ロジック */
	TsuuchiCategoryLogic tsuuchiLogic;
	/** 仮払申請ロジック */
	KaribaraiShinseiLogic gyoumuLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** ワークフローカテゴリー */
	WorkflowCategoryLogic wfCategoryLogic;
	/** 起票ナビカテゴリー */
	KihyouNaviCategoryLogic kihyouLogic;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;

	//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
		checkDomain (karibaraiOn, Domain.FLG, ks.karibaraiOn.getName(), false);
		checkDate (shiharaiKiboubi, ks.shiharaiKiboubi.getName(), false);
		checkDomain (shiharaihouhou, shiharaihouhouDomain, ks.shiharaiHouhou.getName(), false);
		checkDate (seisanYoteiBi, ks.seisanYoteibi.getName(), false);
		checkDate (shiharaiBi, "支払日",  					false);
		if (karibaraiOn != null && karibaraiOn.equals("1")) {
			checkKingakuOver0 (kingaku, ks.shinseiKingaku.getName(), false);
			checkKingakuOver1 (karibaraiKingaku, ks.kingaku.getName(), false);
		} else {
			checkKingakuOver1 (kingaku, ks.shinseiKingaku.getName(), false);
			checkKingakuOver0 (karibaraiKingaku, ks.kingaku.getName(), false);
		}
		checkString (shiwakeEdaNo, 1 ,10, ks.torihiki.getName() + "コード", false);
		checkString (torihikiName, 1, 20, ks.torihiki.getName() + "名", false);
		checkString (kamokuCd, 1, 6, ks.kamoku.getName() + "コード", false);
		checkString (kamokuName, 1, 22, ks.kamoku.getName() + "名", false);
		checkString (kamokuEdabanCd, 1, 12, ks.kamokuEdaban.getName() + "コード", false);
		checkString (kamokuEdabanName, 1, 20, ks.kamokuEdaban.getName() + "名", false);
		checkString (futanBumonCd, 1, 8, ks.futanBumon.getName() + "コード", false);
		checkString (futanBumonName, 1, 20, ks.futanBumon.getName() + "名", false);
		checkString (torihikisakiCd, 1, 12, ks.torihikisaki.getName() + "コード", false);
		checkString (torihikisakiName, 1, 20, ks.torihikisaki.getName() + "名", false);
		checkString (projectCd, 1, 12, ks.project.getName() + "コード："		, false);
		checkString (projectName, 1, 20, ks.project.getName() + "名："			, false);
		checkString (segmentCd, 1, 8, ks.segment.getName() + "コード："		, false);
		checkString (segmentName, 1, 20, ks.segment.getName() + "名："			, false);
		checkString (hf1Cd, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf1Name, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf2Cd, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf2Name, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf3Cd, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString (hf3Name, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString   (hf4Cd,     1, 20,    hfUfSeigyo.getHf4Name(), false);
		checkString   (hf4Name,    1, 20,    hfUfSeigyo.getHf4Name(), false);
		checkString   (hf5Cd,     1, 20,    hfUfSeigyo.getHf5Name(), false);
		checkString   (hf5Name,    1, 20,    hfUfSeigyo.getHf5Name(), false);
		checkString   (hf6Cd,     1, 20,    hfUfSeigyo.getHf6Name(), false);
		checkString   (hf6Name,    1, 20,    hfUfSeigyo.getHf6Name(), false);
		checkString   (hf7Cd,     1, 20,    hfUfSeigyo.getHf7Name(), false);
		checkString   (hf7Name,    1, 20,    hfUfSeigyo.getHf7Name(), false);
		checkString   (hf8Cd,     1, 20,    hfUfSeigyo.getHf8Name(), false);
		checkString   (hf8Name,    1, 20,    hfUfSeigyo.getHf8Name(), false);
		checkString   (hf9Cd,     1, 20,    hfUfSeigyo.getHf9Name(), false);
		checkString   (hf9Name,    1, 20,    hfUfSeigyo.getHf9Name(), false);
		checkString   (hf10Cd,     1, 20,    hfUfSeigyo.getHf10Name(), false);
		checkString   (hf10Name,    1, 20,    hfUfSeigyo.getHf10Name(), false);
		checkString (uf1Cd, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString (uf1Name, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString (uf2Cd, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString (uf2Name, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString (uf3Cd, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString (uf3Name, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString   (uf4Cd,     1, 20,    hfUfSeigyo.getUf4Name(), false);
		checkString   (uf4Name,    1, 20,    hfUfSeigyo.getUf4Name(), false);
		checkString   (uf5Cd,     1, 20,    hfUfSeigyo.getUf5Name(), false);
		checkString   (uf5Name,    1, 20,    hfUfSeigyo.getUf5Name(), false);
		checkString   (uf6Cd,     1, 20,    hfUfSeigyo.getUf6Name(), false);
		checkString   (uf6Name,    1, 20,    hfUfSeigyo.getUf6Name(), false);
		checkString   (uf7Cd,     1, 20,    hfUfSeigyo.getUf7Name(), false);
		checkString   (uf7Name,    1, 20,    hfUfSeigyo.getUf7Name(), false);
		checkString   (uf8Cd,     1, 20,    hfUfSeigyo.getUf8Name(), false);
		checkString   (uf8Name,    1, 20,    hfUfSeigyo.getUf8Name(), false);
		checkString   (uf9Cd,     1, 20,    hfUfSeigyo.getUf9Name(), false);
		checkString   (uf9Name,    1, 20,    hfUfSeigyo.getUf9Name(), false);
		checkString   (uf10Cd,     1, 20,    hfUfSeigyo.getUf10Name(), false);
		checkString   (uf10Name,    1, 20,    hfUfSeigyo.getUf10Name(), false);
		checkString (tekiyou, 1, this.getIntTekiyouMaxLength(), ks.tekiyou.getName(), false);
		checkSJIS (tekiyou, ks.tekiyou.getName());
		checkString (shiyouMokuteki, 1, 240, ks.hosoku.getName(), false);
	}

	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum 1:登録/更新/申請、2:支払日更新
	 */
	protected void denpyouHissuCheck(int eventNum) {
		String[][] list = {
			//項目									 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{karibaraiOn ,ks.karibaraiOn.getName() ,ks.karibaraiOn.getHissuFlgS(),"0"},
			{shiharaiKiboubi ,ks.shiharaiKiboubi.getName() ,ks.shiharaiKiboubi.getHissuFlgS(), "0"},
			{shiharaihouhou ,ks.shiharaiHouhou.getName() ,ks.shiharaiHouhou.getHissuFlgS(), "0"},
			{tekiyou ,ks.tekiyou.getName() ,ks.tekiyou.getHissuFlgS(), "0"},
		};
		hissuCheckCommon(list, eventNum);

		if(karibaraiOn != null && karibaraiOn.equals("1")){
			list = new String[][]{
				{shiwakeEdaNo ,ks.torihiki.getName() + "コード"	,ks.torihiki.getHissuFlgS(), "0"},
				{torihikiName ,ks.torihiki.getName() + "名"		,ks.torihiki.getHissuFlgS(), "0"},
				{kamokuCd ,ks.kamoku.getName() + "コード"		,ks.kamoku.getHissuFlgS(), "0"},
				{kamokuName ,ks.kamoku.getName() + "名"			,ks.kamoku.getHissuFlgS(), "0"},
				{kamokuEdabanCd ,ks.kamokuEdaban.getName() + "コード",ks.kamokuEdaban.getHissuFlgS(), "0"},
				{kamokuEdabanName ,ks.kamokuEdaban.getName() + "名"	,ks.kamokuEdaban.getHissuFlgS(), "0"},
				{futanBumonCd ,ks.futanBumon.getName() + "コード"	,ks.futanBumon.getHissuFlgS(), "0"},
				{futanBumonName ,ks.futanBumon.getName() + "名"		,ks.futanBumon.getHissuFlgS(), "0"},
				{torihikisakiCd, ks.torihikisaki.getName()+ "コード：", ks.torihikisaki.getHissuFlgS(), "0"},
				{torihikisakiName, ks.torihikisaki.getName()+ "名："	, ks.torihikisaki.getHissuFlgS(), "0"},
				{projectCd, ks.project.getName() + "コード："	, ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
				{projectName, ks.project.getName() + "名："		, ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
				{segmentCd, ks.segment.getName() + "コード："	, ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
				{segmentName, ks.segment.getName() + "名："		, ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
			};
			hissuCheckCommon(list, eventNum);

			list = new String[][]{
				{karibaraiKingaku ,ks.kingaku.getName() ,ks.kingaku.getHissuFlgS(), "0"},
				{shiharaiBi ,"支払日"								,"0", "1"},
			};
			hissuCheckCommon(list, eventNum);

			if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード"	, ks.hf1.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード"	, ks.hf2.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード"	, ks.hf3.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード" , ks.hf4.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード" , ks.hf5.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード" , ks.hf6.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード" , ks.hf7.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード" , ks.hf8.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード" , ks.hf9.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード" , ks.hf10.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1Cd, hfUfSeigyo.getUf1Name() + "コード"	, ks.uf1.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2Cd, hfUfSeigyo.getUf2Name() + "コード"	, ks.uf2.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3Cd, hfUfSeigyo.getUf3Name() + "コード"	, ks.uf3.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4Cd, hfUfSeigyo.getUf4Name() + "コード" , ks.uf4.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5Cd, hfUfSeigyo.getUf5Name() + "コード" , ks.uf5.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6Cd, hfUfSeigyo.getUf6Name() + "コード" , ks.uf6.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7Cd, hfUfSeigyo.getUf7Name() + "コード" , ks.uf7.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8Cd, hfUfSeigyo.getUf8Name() + "コード" , ks.uf8.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9Cd, hfUfSeigyo.getUf9Name() + "コード" , ks.uf9.getHissuFlgS(), "0"}}, eventNum);
			if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10Cd, hfUfSeigyo.getUf10Name() + "コード" , ks.uf10.getHissuFlgS(), "0"}}, eventNum);
		}

		list = new String[][]{
			{seisanYoteiBi ,ks.seisanYoteibi.getName() ,ks.seisanYoteibi.getHissuFlgS(), "0"},
		};
		hissuCheckCommon(list, eventNum);

		if (karibaraiOn != null && karibaraiOn.equals("0")) {
			list = new String[][]{
				{kingaku ,ks.shinseiKingaku.getName() ,	ks.shinseiKingaku.getHissuFlgS(), "0"},
			};
			hissuCheckCommon(list, eventNum);
		}

		list = new String[][]{
			{shiyouMokuteki ,ks.hosoku.getName() ,ks.hosoku.getHissuFlgS(), "0"},
		};
		hissuCheckCommon(list, eventNum);
	};

	/**
	 * 業務関連の関連チェック処理
	 */
	protected void soukanCheck() {

		// 社員口座存在チェック
		commonLogic.checkShainKouza(super.getKihyouUserId(), shiharaihouhou, errorList);

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
		commonLogic.checkShiwake(shiwakeCheckDataNaiyou, errorList);

		// 共通機能（会計共通）．会計項目入力チェック
		if (isNotEmpty(shiwakeEdaNo)) {

			//仕訳パターン
			GMap shiwakePattern = kaikeiLogic.findShiwakePattern(super.denpyouKbn, Integer.parseInt(shiwakeEdaNo));
			// 借方
			ShiwakeCheckData shiwakeCheckDataKari = commonLogic.new ShiwakeCheckData() ;
			shiwakeCheckDataKari.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
			shiwakeCheckDataKari.shiwakeEdaNo = shiwakeEdaNo;
			shiwakeCheckDataKari.kamokuNm = ks.kamoku.getName() + "コード";
			shiwakeCheckDataKari.kamokuCd = kamokuCd;
			shiwakeCheckDataKari.kamokuEdabanNm = ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKari.kamokuEdabanCd = kamokuEdabanCd;
			shiwakeCheckDataKari.futanBumonNm = ks.futanBumon.getName() + "コード";
			shiwakeCheckDataKari.futanBumonCd = futanBumonCd;
			shiwakeCheckDataKari.torihikisakiNm = ks.torihikisaki.getName() + "コード";
			shiwakeCheckDataKari.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kari_torihikisaki_cd")) ? torihikisakiCd : (String)shiwakePattern.get("kari_torihikisaki_cd");
			shiwakeCheckDataKari.projectNm = ks.project.getName() + "コード";
			shiwakeCheckDataKari.segmentNm = ks.segment.getName() + "コード";
			if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kari_project_cd")))
			{
				shiwakeCheckDataKari.projectCd = projectCd;
			}
			if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kari_segment_cd")))
			{
				shiwakeCheckDataKari.segmentCd = segmentCd;
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
			// 以下、借方の画面上表示のないもの
			shiwakeCheckDataKari.kazeiKbnNm = "借方課税区分";
			shiwakeCheckDataKari.kazeiKbn = kazeiKbn;
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf1_cd")))
			{
				shiwakeCheckDataKari.uf1Cd = kariUf1Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf2_cd")))
			{
				shiwakeCheckDataKari.uf2Cd = kariUf2Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf3_cd")))
			{
				shiwakeCheckDataKari.uf3Cd = kariUf3Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf4_cd")))
			{
				shiwakeCheckDataKari.uf4Cd = kariUf4Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf5_cd")))
			{
				shiwakeCheckDataKari.uf5Cd = kariUf5Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf6_cd")))
			{
				shiwakeCheckDataKari.uf6Cd = kariUf6Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf7_cd")))
			{
				shiwakeCheckDataKari.uf7Cd = kariUf7Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf8_cd")))
			{
				shiwakeCheckDataKari.uf8Cd = kariUf8Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf9_cd")))
			{
				shiwakeCheckDataKari.uf9Cd = kariUf9Cd;
			}
			if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf10_cd")))
			{
				shiwakeCheckDataKari.uf10Cd = kariUf10Cd;
			}
			commonLogic.checkShiwake(denpyouKbn, EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, shiwakeCheckDataKari, super.daihyouFutanBumonCd, errorList);

			// 貸方
			ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData() ;
			shiwakeCheckDataKashi.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
			shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo;
			shiwakeCheckDataKashi.kamokuNm = "貸方" + ks.kamoku.getName() + "コード";
			shiwakeCheckDataKashi.kamokuCd = kashiKamokuCd;
			shiwakeCheckDataKashi.kamokuEdabanNm = "貸方" + ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKashi.kamokuEdabanCd = kashiKamokuEdabanCd;
			shiwakeCheckDataKashi.futanBumonNm = "貸方" + ks.futanBumon.getName() + "コード";
			shiwakeCheckDataKashi.futanBumonCd = kashiFutanBumonCd;
			shiwakeCheckDataKashi.torihikisakiNm =  "貸方" + ks.torihikisaki.getName() + "コード";
			shiwakeCheckDataKashi.projectNm =  "貸方" + ks.project.getName() + "コード";
			shiwakeCheckDataKashi.segmentNm =  "貸方" + ks.segment.getName() + "コード";
			shiwakeCheckDataKashi.kazeiKbnNm = "貸方課税区分";
			shiwakeCheckDataKashi.kazeiKbn = kashiKazeiKbn;
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
					shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd1")) ? torihikisakiCd : (String)shiwakePattern.get("kashi_torihikisaki_cd1");
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd1")))
					{
						shiwakeCheckDataKashi.projectCd = projectCd;
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd1")))
					{
						shiwakeCheckDataKashi.segmentCd = segmentCd;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd1")))
					{
						shiwakeCheckDataKashi.uf1Cd = kashiUf1Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd1")))
					{
						shiwakeCheckDataKashi.uf2Cd = kashiUf2Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd1")))
					{
						shiwakeCheckDataKashi.uf3Cd = kashiUf3Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd1")))
					{
						shiwakeCheckDataKashi.uf4Cd = kashiUf4Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd1")))
					{
						shiwakeCheckDataKashi.uf5Cd = kashiUf5Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd1")))
					{
						shiwakeCheckDataKashi.uf6Cd = kashiUf6Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd1")))
					{
						shiwakeCheckDataKashi.uf7Cd = kashiUf7Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd1")))
					{
						shiwakeCheckDataKashi.uf8Cd = kashiUf8Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd1")))
					{
						shiwakeCheckDataKashi.uf9Cd = kashiUf9Cd1;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd1")))
					{
						shiwakeCheckDataKashi.uf10Cd = kashiUf10Cd1;
					}
					commonLogic.checkShiwake(denpyouKbn, EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, shiwakeCheckDataKashi, super.daihyouFutanBumonCd, errorList);
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					// 現金
					shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd2")) ? torihikisakiCd : (String)shiwakePattern.get("kashi_torihikisaki_cd2");
					if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd2")))
					{
						shiwakeCheckDataKashi.projectCd = projectCd;
					}
					if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd2")))
					{
						shiwakeCheckDataKashi.segmentCd = segmentCd;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd2")))
					{
						shiwakeCheckDataKashi.uf1Cd = kashiUf1Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd2")))
					{
						shiwakeCheckDataKashi.uf2Cd = kashiUf2Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd2")))
					{
						shiwakeCheckDataKashi.uf3Cd = kashiUf3Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd2")))
					{
						shiwakeCheckDataKashi.uf4Cd = kashiUf4Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd2")))
					{
						shiwakeCheckDataKashi.uf5Cd = kashiUf5Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd2")))
					{
						shiwakeCheckDataKashi.uf6Cd = kashiUf6Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd2")))
					{
						shiwakeCheckDataKashi.uf7Cd = kashiUf7Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd2")))
					{
						shiwakeCheckDataKashi.uf8Cd = kashiUf8Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd2")))
					{
						shiwakeCheckDataKashi.uf9Cd = kashiUf9Cd2;
					}
					if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd2")))
					{
						shiwakeCheckDataKashi.uf10Cd = kashiUf10Cd2;
					}
					commonLogic.checkShiwake(denpyouKbn, EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, shiwakeCheckDataKashi, super.daihyouFutanBumonCd, errorList);
					break;
			}

			// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
			if (isNotEmpty(torihikisakiCd)) {
				ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
				torihikisaki.torihikisakiNm = ks.torihikisaki.getName() + "コード";
				torihikisaki.torihikisakiCd =torihikisakiCd;
				commonLogic.checkTorihikisaki(torihikisaki, errorList);

				// 取引先仕入先チェック
				super.checkShiiresaki(ks.torihikisaki.getName() + "コード", torihikisakiCd, denpyouKbn);
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

			// 申請と取引が1:1で決まる場合
			List<ShiwakePatternMaster> list = this.shiwakePatternMasterDao.loadByDenpyouKbn(DENPYOU_KBN.KARIBARAI_SHINSEI,this.futanBumonCd);
			if(!list.isEmpty() && setting.keihiKaribaraiSentakuSeigyo().equals("1")) {
				if(list.size() == 1) {
					shiwakeEdaNo = Integer.toString(list.get(0).shiwakeEdano);
					torihikiName =  list.get(0).torihikiName;
					tekiyou = torihikiName;
					if(list.get(0).tekiyouFlg.equals("1") ) {
						tekiyou = list.get(0).tekiyou;
					}
					if(!List.of("<FUTAN>","<DAIHYOUBUMON>","<SYOKIDAIHYOU>").contains(list.get(0).kariFutanBumonCd)) {
						this.futanBumonCd = list.get(0).kariFutanBumonCd;
					}
					if(!list.get(0).kariTorihikisakiCd.equals("<TORIHIKI>")) {
						this.torihikisakiCd = list.get(0).kariTorihikisakiCd;
					}
					if(!list.get(0).kariSegmentCd.equals("<SG>")) {
						this.segmentCd = list.get(0).kariSegmentCd;
					}
					if(!list.get(0).kariProjectCd.equals("<PROJECT>")) {
						this.projectCd = list.get(0).kariProjectCd;
					}
					shiharaihouhou = SHIHARAI_HOUHOU.FURIKOMI;
					// 仕訳パターン情報読込（相関チェック前に必要）
					reloadShiwakePattern(connection);
					//マスター等から名称は引き直す
					reloadMaster(connection);
					shiharaihouhou = null;
				}
			}

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			GMap shinseiData = kaikeiLogic.findKaribarai(denpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			GMap shinseiData = kaikeiLogic.findKaribarai(sanshouDenpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);
			// 支払方法は会社設定で設定されている中になければクリア
			String shiharaiHouhouSetting = setting.shiharaiHouhouA002();
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
			seisanYoteiBi = "";
			shiharaiBi = "";
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

		//相関チェック
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}
	}

	//登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		// 登録
		gyoumuLogic.insert(
			denpyouId,
			toDate(seisanYoteiBi),
			null,
			null,
			karibaraiOn,
			toDate(shiharaiKiboubi),
			shiharaihouhou,
			tekiyou,
			toDecimal(kingaku),
			toDecimal(karibaraiKingaku),
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
			shiyouMokuteki,
			(isEmpty(shiwakeEdaNo) ? null : Integer.parseInt(shiwakeEdaNo)),
			torihikiName,
			futanBumonCd,
			futanBumonName,
			torihikisakiCd,
			torihikisakiName,
			kamokuCd == null ? "" : kamokuCd,
			kamokuName == null ? "" : kamokuName,
			kamokuEdabanCd == null ? "" : kamokuEdabanCd,
			kamokuEdabanName == null ? "" : kamokuEdabanName,
			kazeiKbn == null ? "" : kazeiKbn,
			kashiFutanBumonCd == null ? "" : kashiFutanBumonCd,
			kashiFutanBumonName == null ? "" : kashiFutanBumonName,
			kashiKamokuCd == null ? "" : kashiKamokuCd,
			kashiKamokuName == null ? "" : kashiKamokuName,
			kashiKamokuEdabanCd == null ? "" : kashiKamokuEdabanCd,
			kashiKamokuEdabanName == null ? "" : kashiKamokuEdabanName,
			kashiKazeiKbn == null ? "" : kashiKazeiKbn,
			uf1Cd,
			uf1Name,
			uf2Cd,
			uf2Name,
			uf3Cd,
			uf3Name,
			uf4Cd,
			uf4Name,
			uf5Cd,
			uf5Name,
			uf6Cd,
			uf6Name,
			uf7Cd,
			uf7Name,
			uf8Cd,
			uf8Name,
			uf9Cd,
			uf9Name,
			uf10Cd,
			uf10Name,
			projectCd,
			projectName,
			segmentCd,
			segmentName,
			tekiyouCd == null ? "" : tekiyouCd,
			super.getUser().getTourokuOrKoushinUserId());
	}

	//更新ボタン押下時に、個別伝票について以下を行う。
	//・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	//・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		//必須チェック・形式チェック・相関チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
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
			commonLogic.checkShiharaiBi(denpyouId, toDate(shiharaiBi), null, null, shiharaihouhou, loginUserInfo, errorList);
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

		// 更新
		gyoumuLogic.update(
			denpyouId,
			toDate(seisanYoteiBi),
			null,
			toDate(shiharaiBi),
			karibaraiOn,
			toDate(shiharaiKiboubi),
			shiharaihouhou,
			tekiyou,
			toDecimal(kingaku),
			toDecimal(karibaraiKingaku),
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
			shiyouMokuteki,
			(isEmpty(shiwakeEdaNo) ? null : Integer.parseInt(shiwakeEdaNo)),
			torihikiName,
			futanBumonCd,
			futanBumonName,
			torihikisakiCd,
			torihikisakiName,
			kamokuCd == null ? "" : kamokuCd,
			kamokuName == null ? "" : kamokuName,
			kamokuEdabanCd == null ? "" : kamokuEdabanCd,
			kamokuEdabanName == null ? "" : kamokuEdabanName,
			kazeiKbn == null ? "" : kazeiKbn,
			kashiFutanBumonCd == null ? "" : kashiFutanBumonCd,
			kashiFutanBumonName == null ? "" : kashiFutanBumonName,
			kashiKamokuCd == null ? "" : kashiKamokuCd,
			kashiKamokuName == null ? "" : kashiKamokuName,
			kashiKamokuEdabanCd == null ? "" : kashiKamokuEdabanCd,
			kashiKamokuEdabanName == null ? "" : kashiKamokuEdabanName,
			kashiKazeiKbn == null ? "" : kashiKazeiKbn,
			uf1Cd,
			uf1Name,
			uf2Cd,
			uf2Name,
			uf3Cd,
			uf3Name,
			uf4Cd,
			uf4Name,
			uf5Cd,
			uf5Name,
			uf6Cd,
			uf6Name,
			uf7Cd,
			uf7Name,
			uf8Cd,
			uf8Name,
			uf9Cd,
			uf9Name,
			uf10Cd,
			uf10Name,
			projectCd,
			projectName,
			segmentCd,
			segmentName,
			tekiyouCd == null ? "" : tekiyouCd,
			super.getUser().getTourokuOrKoushinUserId());
	}

	//承認ボタン押下時に、個別伝票について以下を行う。
	//・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（エラー発生時用）
		displaySeigyo(connection);

		//支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo) && karibaraiOn.equals("1")) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			String shiharai = formatDate(kaikeiLogic.findKaribarai(denpyouId).get("shiharaibi"));
			commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), null, null, shiharaihouhou, loginUserInfo, errorList);
			// エラーの場合の表示用に現実の値を設定
			shiharaiBi = shiharai;
		}
	}

	//部門推奨ルートの基準金額を取得。
	//デフォルトでは親子共有するメンバ変数kingakuを返すので、子で特殊な制御をする場合はオーバーライド。
	@Override
	public String getKingaku() {
		if("0".equals(karibaraiOn)) {
			return this.shinseiKingaku;
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

	//代表仕訳枝番号を設定
	@Override
	protected String getDaihyouTorihiki(){
		return shiwakeEdaNo;
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
		GMap seisanWithKaribarai = kaikeiLogic.findKeihiSeisanWithKaribarai(denpyouId);
		if(null != seisanWithKaribarai){
			String seisanDenpyouId = seisanWithKaribarai.get("denpyou_id").toString();
			GMap seisan = kaikeiLogic.findKeihiSeisan(seisanDenpyouId);
			if("1".equals(seisan.get("karibarai_mishiyou_flg")) && commonLogic.isAfterShinsei(seisanDenpyouId)){
				ret = true;
			}
		}
		return ret;
	}

	//科目が予算執行対象かどうか判定する。
	@Override
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection connection) {

		HashSet<String> kamokuCdSet = new HashSet<String>();
		kamokuCdSet.add(kamokuCd);

		if(null == commonLogic){
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		}
		return commonLogic.isYosanShikkouKamoku(denpyouId, kamokuCdSet, kianbangouBoDialogNendo);
	}


//＜内部処理＞
	/**
	 * 各Logic対象の初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(KaribaraiShinseiLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfCategoryLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
	}

	/**
	 * 仮払申請テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 仮払申請レコード
	 * @param sanshou     参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection  コネクション
	 */
	protected void shinseiData2Gamen(GMap shinseiData, boolean sanshou, EteamConnection connection) {
		karibaraiOn = (String)shinseiData.get("karibarai_on");
		shiharaihouhou = (String) shinseiData.get("shiharaihouhou");
		shiharaiKiboubi = formatDate(shinseiData.get("shiharaiKiboubi"));
		shiwakeEdaNo = ((shinseiData.get("shiwake_edano") == null) ? "" : ((Integer)shinseiData.get("shiwake_edano")).toString());
		torihikiName = (String) shinseiData.get("torihiki_name");
		kamokuCd = (String) shinseiData.get("kari_kamoku_cd");
		kamokuName = (String) shinseiData.get("kari_kamoku_name");
		kamokuEdabanCd = (String) shinseiData.get("kari_kamoku_edaban_cd");
		kamokuEdabanName = (String) shinseiData.get("kari_kamoku_edaban_name");
		futanBumonCd = (String) shinseiData.get("kari_futan_bumon_cd");
		futanBumonName = (String) shinseiData.get("kari_futan_bumon_name");
		torihikisakiCd = (String)shinseiData.get("torihikisaki_cd");
		torihikisakiName = (String)shinseiData.get("torihikisaki_name_ryakushiki");
		projectCd = (String)shinseiData.get("project_cd");
		projectName = (String)shinseiData.get("project_name");
		segmentCd = (String)shinseiData.get("segment_cd");
		segmentName = (String)shinseiData.get("segment_name_ryakushiki");
		hf1Cd = (String) shinseiData.get("hf1_cd");
		hf1Name = (String) shinseiData.get("hf1_name_ryakushiki");
		hf2Cd = (String) shinseiData.get("hf2_cd");
		hf2Name = (String) shinseiData.get("hf2_name_ryakushiki");
		hf3Cd = (String) shinseiData.get("hf3_cd");
		hf3Name = (String) shinseiData.get("hf3_name_ryakushiki");
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
		uf1Cd = (String) shinseiData.get("uf1_cd");
		uf1Name = (String) shinseiData.get("uf1_name_ryakushiki");
		uf2Cd = (String) shinseiData.get("uf2_cd");
		uf2Name = (String) shinseiData.get("uf2_name_ryakushiki");
		uf3Cd = (String) shinseiData.get("uf3_cd");
		uf3Name = (String) shinseiData.get("uf3_name_ryakushiki");
		uf4Cd = (String)shinseiData.get("uf4_cd");
		uf4Name = (String)shinseiData.get("uf4_name_ryakushiki");
		uf5Cd = (String)shinseiData.get("uf5_cd");
		uf5Name = (String)shinseiData.get("uf5_name_ryakushiki");
		uf6Cd = (String)shinseiData.get("uf6_cd");
		uf6Name = (String)shinseiData.get("uf6_name_ryakushiki");
		uf7Cd = (String)shinseiData.get("uf7_cd");
		uf7Name = (String)shinseiData.get("uf7_name_ryakushiki");
		uf8Cd = (String)shinseiData.get("uf8_cd");
		uf8Name = (String)shinseiData.get("uf8_name_ryakushiki");
		uf9Cd = (String)shinseiData.get("uf9_cd");
		uf9Name = (String)shinseiData.get("uf9_name_ryakushiki");
		uf10Cd = (String)shinseiData.get("uf10_cd");
		uf10Name = (String)shinseiData.get("uf10_name_ryakushiki");
		seisanYoteiBi = formatDate(shinseiData.get("seisan_yoteibi"));
		shinseiKingaku = formatMoney(shinseiData.get("kingaku"));
		karibaraiKingaku = formatMoney(shinseiData.get("karibarai_kingaku"));
		tekiyou = (String) shinseiData.get("tekiyou");
		if(!sanshou){
			GMap meisai = null;
			BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.KARIBARAI_SHINSEI, shinseiData, meisai, "0");
			String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
			if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
				chuuki1 = commonLogic.getTekiyouChuuki();
				chuuki2 = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
			}
		}
		shiharaiBi = formatDate(shinseiData.get("shiharaibi"));
		shiyouMokuteki = (String) shinseiData.get("hosoku");
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		shiharaihouhouList = commonLogic.makeShiharaiHouhouList(EteamSettingInfo.Key.SHIHARAI_HOUHOU_A002, shiharaihouhou);
		shiharaihouhouDomain = EteamCommon.mapList2Arr(shiharaihouhouList, "naibu_cd");

		//支払方法を入力可能にするかどうか判定。
		disableShiharaiHouhou = (shiharaihouhouList.size() <= 1);

		// 支払日登録可能かどうか制御 (0:非表示、1:入力可、2:表示)
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		//起票モードで取引選択済ならば仕訳パターンの設定によりボタン押下判定
		if (enableInput) {
			if (isNotEmpty(shiwakeEdaNo)) {
				GMap shiwakePattern = kaikeiLogic.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdaNo));
				InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
				kamokuEdabanEnable = info.kamokuEdabanEnable;
				futanBumonEnable = info.futanBumonEnable;
				torihikisakiEnable = info.torihikisakiEnable;
				projectEnable = info.projectEnable;
				segmentEnable = info.segmentEnable;
				uf1Enable = info.uf1Enable;
				uf2Enable = info.uf2Enable;
				uf3Enable = info.uf3Enable;
				uf4Enable = info.uf4Enable;
				uf5Enable = info.uf5Enable;
				uf6Enable = info.uf6Enable;
				uf7Enable = info.uf7Enable;
				uf8Enable = info.uf8Enable;
				uf9Enable = info.uf9Enable;
				uf10Enable = info.uf10Enable;
			}

			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
			String initShainCd = (usrInfo == null) ? "" : (String)usrInfo.get("shain_no");
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
		if (shiharaiBiMode == 1 && isEmpty(shiharaiBi) && shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI)) {
			Date shiharai = null;
			if (setting.furikomiRuleA002().equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(super.getKihyouBi()));
			} else if (setting.furikomiRuleA002().equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(super.getKihyouBi()));
			}
			if (shiharai != null) {
				shiharaiBi = formatDate(shiharai);
				// マスターから基準日(起票日)に対する振込日が見つかった場合、支払日の更新
				gyoumuLogic.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}

		//申請画面チェック日数の表示制御
		//・申請期間チェック日数が有効のとき
		//・承認後、否認後、取下後でないとき
		//・精算予定日 + 1 - システム日付 <= 申請期間チェック日数
		if (!"0".equals(setting.keihiShinseiKikanCheckNissuu())
			&& isNotEmpty(seisanYoteiBi)
			&& !(DENPYOU_JYOUTAI.SYOUNIN_ZUMI.equals(super.getDenpyouJoutai())
				|| DENPYOU_JYOUTAI.HININ_ZUMI.equals(super.getDenpyouJoutai())
				|| DENPYOU_JYOUTAI.TORISAGE_ZUMI.equals(super.getDenpyouJoutai()))
			&& (DateUtils.addDays(toDate(seisanYoteiBi), 1).compareTo(DateUtils.addDays(new Date(System.currentTimeMillis()), Integer.parseInt(setting.keihiShinseiKikanCheckNissuu()))) <= 0)
		) {
			shinseiChuuki = setting.keihiShinseiKikanCheckMongon();
			shinseiChuuki = shinseiChuuki.replace("<DAY>", setting.keihiShinseiKikanCheckNissuu());
		}

		//タイトル切替用
		GMap denpyouShubetuMap = wfCategoryLogic.selectDenpyouShubetu(denpyouKbn);
		denpyouShubetsu = denpyouShubetuMap.get("denpyou_shubetsu").toString();
		denpyouKaribaraiNashiShubetsu = denpyouShubetuMap.get("denpyou_karibarai_nashi_shubetsu").toString();

		// 予算執行対象フラグの設定
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.KARIBARAI_SHINSEI);
		yosanShikkouTaishouFlg = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())
				                 && EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(denpyouShubetsuMap.get("yosan_shikkou_taishou").toString());
	}

	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String shainCd = (String)usrInfo.get("shain_no");
		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(super.getKihyouUserId());

		if (isNotEmpty(shiwakeEdaNo)) {
			//仕訳パターン取得
			GMap shiwakeP = kaikeiLogic.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdaNo));
			String daihyouBumonCd = super.daihyouFutanBumonCd;

			//取引名
			torihikiName = (String)shiwakeP.get("torihiki_name");

			//借方　科目
			kamokuCd = (String)shiwakeP.get("kari_kamoku_cd");

			//借方　科目枝番
			String pKariKamokuEdabanCd = (String)shiwakeP.get("kari_kamoku_edaban_cd");
			switch (pKariKamokuEdabanCd) {
				case EteamConst.ShiwakeConst.EDABAN:
					//何もしない(画面入力のまま)
					break;
				default:
					//固定コード値 or ブランク
					kamokuEdabanCd = pKariKamokuEdabanCd;
					break;
			}

			//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
			enableBumonSecurity = true;
			//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
			if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.get("kari_futan_bumon_cd")) ){
				enableBumonSecurity = commonLogic.checkFutanBumonWithSecurity(futanBumonCd, ks.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
			}
			// 借方負担部門コード
			futanBumonCd = commonLogic.convFutanBumon(futanBumonCd, (String)shiwakeP.get("kari_futan_bumon_cd"), daihyouBumonCd);

			//借方　課税区分
			kazeiKbn = (String)shiwakeP.get("kari_kazei_kbn");

			//借方　UF1-10
			if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.get("shain_cd_renkei_flg").equals(("1"))){
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
				{
					 uf1Cd  = shainCd;
				}
				if (ufno == 2)
				{
					 uf2Cd  = shainCd;
				}
				if (ufno == 3)
				{
					 uf3Cd  = shainCd;
				}
				if (ufno == 4)
				{
					 uf4Cd  = shainCd;
				}
				if (ufno == 5)
				{
					 uf5Cd  = shainCd;
				}
				if (ufno == 6)
				{
					 uf6Cd  = shainCd;
				}
				if (ufno == 7)
				{
					 uf7Cd  = shainCd;
				}
				if (ufno == 8)
				{
					 uf8Cd  = shainCd;
				}
				if (ufno == 9)
				{
					 uf9Cd  = shainCd;
				}
				if (ufno == 10)
				{
					uf10Cd = shainCd;
				}
			}
			kariUf1Cd = commonLogic.convUf(uf1Cd, (String)shiwakeP.get("kari_uf1_cd"));
			kariUf2Cd = commonLogic.convUf(uf2Cd, (String)shiwakeP.get("kari_uf2_cd"));
			kariUf3Cd = commonLogic.convUf(uf3Cd, (String)shiwakeP.get("kari_uf3_cd"));
			kariUf4Cd = commonLogic.convUf(uf4Cd, (String)shiwakeP.get("kari_uf4_cd"));
			kariUf5Cd = commonLogic.convUf(uf5Cd, (String)shiwakeP.get("kari_uf5_cd"));
			kariUf6Cd = commonLogic.convUf(uf6Cd, (String)shiwakeP.get("kari_uf6_cd"));
			kariUf7Cd = commonLogic.convUf(uf7Cd, (String)shiwakeP.get("kari_uf7_cd"));
			kariUf8Cd = commonLogic.convUf(uf8Cd, (String)shiwakeP.get("kari_uf8_cd"));
			kariUf9Cd = commonLogic.convUf(uf9Cd, (String)shiwakeP.get("kari_uf9_cd"));
			kariUf10Cd = commonLogic.convUf(uf10Cd, (String)shiwakeP.get("kari_uf10_cd"));

			//支払方法により貸方1 or 貸方2を切替する
			switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					//振込
					kashiKamokuCd = (String)shiwakeP.get("kashi_kamoku_cd1"); //貸方1　科目コード
					//貸方1　科目枝番コード
					String pKashiKamokuEdabanCd = (String)shiwakeP.get("kashi_kamoku_edaban_cd1");
					this.kashiKamokuEdabanCd = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
							? shainShiwakeEdaNo
							: pKashiKamokuEdabanCd;
					kashiFutanBumonCd = (String)shiwakeP.get("kashi_futan_bumon_cd1"); //貸方1　負担部門コード
					kashiKazeiKbn = (String)shiwakeP.get("kashi_kazei_kbn1"); //貸方1　課税区分
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					//現金
					kashiKamokuCd = (String)shiwakeP.get("kashi_kamoku_cd2"); //貸方2　科目コード
					kashiKamokuEdabanCd = (String)shiwakeP.get("kashi_kamoku_edaban_cd2"); //貸方2　科目枝番コード
					kashiFutanBumonCd = (String)shiwakeP.get("kashi_futan_bumon_cd2"); //貸方2　負担部門コード
					kashiKazeiKbn = (String)shiwakeP.get("kashi_kazei_kbn2"); //貸方2　課税区分
					break;
			}

			//貸方1　UF1-10
			kashiUf1Cd1 = commonLogic.convUf(uf1Cd, (String)shiwakeP.get("kashi_uf1_cd1"));
			kashiUf2Cd1 = commonLogic.convUf(uf2Cd, (String)shiwakeP.get("kashi_uf2_cd1"));
			kashiUf3Cd1 = commonLogic.convUf(uf3Cd, (String)shiwakeP.get("kashi_uf3_cd1"));
			kashiUf4Cd1 = commonLogic.convUf(uf4Cd, (String)shiwakeP.get("kashi_uf4_cd1"));
			kashiUf5Cd1 = commonLogic.convUf(uf5Cd, (String)shiwakeP.get("kashi_uf5_cd1"));
			kashiUf6Cd1 = commonLogic.convUf(uf6Cd, (String)shiwakeP.get("kashi_uf6_cd1"));
			kashiUf7Cd1 = commonLogic.convUf(uf7Cd, (String)shiwakeP.get("kashi_uf7_cd1"));
			kashiUf8Cd1 = commonLogic.convUf(uf8Cd, (String)shiwakeP.get("kashi_uf8_cd1"));
			kashiUf9Cd1 = commonLogic.convUf(uf9Cd, (String)shiwakeP.get("kashi_uf9_cd1"));
			kashiUf10Cd1 = commonLogic.convUf(uf10Cd, (String)shiwakeP.get("kashi_uf10_cd1"));

			//貸方2　UF1-10
			kashiUf1Cd2 = commonLogic.convUf(uf1Cd, (String)shiwakeP.get("kashi_uf1_cd2"));
			kashiUf2Cd2 = commonLogic.convUf(uf2Cd, (String)shiwakeP.get("kashi_uf2_cd2"));
			kashiUf3Cd2 = commonLogic.convUf(uf3Cd, (String)shiwakeP.get("kashi_uf3_cd2"));
			kashiUf4Cd2 = commonLogic.convUf(uf4Cd, (String)shiwakeP.get("kashi_uf4_cd2"));
			kashiUf5Cd2 = commonLogic.convUf(uf5Cd, (String)shiwakeP.get("kashi_uf5_cd2"));
			kashiUf6Cd2 = commonLogic.convUf(uf6Cd, (String)shiwakeP.get("kashi_uf6_cd2"));
			kashiUf7Cd2 = commonLogic.convUf(uf7Cd, (String)shiwakeP.get("kashi_uf7_cd2"));
			kashiUf8Cd2 = commonLogic.convUf(uf8Cd, (String)shiwakeP.get("kashi_uf8_cd2"));
			kashiUf9Cd2 = commonLogic.convUf(uf9Cd, (String)shiwakeP.get("kashi_uf9_cd2"));
			kashiUf10Cd2 = commonLogic.convUf(uf10Cd, (String)shiwakeP.get("kashi_uf10_cd2"));

			//画面に反映
			if (!isEmpty(kariUf1Cd))
			{
				uf1Cd = kariUf1Cd;
			}
			if (!isEmpty(kariUf2Cd))
			{
				uf2Cd = kariUf2Cd;
			}
			if (!isEmpty(kariUf3Cd))
			{
				uf3Cd = kariUf3Cd;
			}
			if (!isEmpty(kariUf4Cd))
			{
				uf4Cd = kariUf4Cd;
			}
			if (!isEmpty(kariUf5Cd))
			{
				uf5Cd = kariUf5Cd;
			}
			if (!isEmpty(kariUf6Cd))
			{
				uf6Cd = kariUf6Cd;
			}
			if (!isEmpty(kariUf7Cd))
			{
				uf7Cd = kariUf7Cd;
			}
			if (!isEmpty(kariUf8Cd))
			{
				uf8Cd = kariUf8Cd;
			}
			if (!isEmpty(kariUf9Cd))
			{
				uf9Cd = kariUf9Cd;
			}
			if (!isEmpty(kariUf10Cd))
			{
				uf10Cd = kariUf10Cd;
			}

			//代表部門
			kashiFutanBumonCd = commonLogic.convFutanBumon(futanBumonCd, kashiFutanBumonCd, daihyouBumonCd);

			//社員コードを摘要コードに反映する場合
			if("1".equals(shiwakeP.get("shain_cd_renkei_flg")) && "T".equals(setting.shainCdRenkei())) {
				if(shainCd.length() > 4) {
					tekiyouCd = shainCd.substring(shainCd.length()-4);
				} else {
					tekiyouCd = shainCd;
				}

			} else {
				tekiyouCd = "";
			}
		}
	}

	/**
	 * DB登録前にマスターから名称を取得する。（クライアントから送られた名称は破棄）
	 * @param connection コネクション
	 */
	protected void reloadMaster(EteamConnection connection) {
		//画面表示項目
		kamokuName = masterLogic.findKamokuNameStr(kamokuCd);
		kamokuEdabanName = masterLogic.findKamokuEdabanName(kamokuCd, kamokuEdabanCd);
		futanBumonName = enableBumonSecurity ? masterLogic.findFutanBumonName(futanBumonCd) : "";
		torihikisakiName = masterLogic.findTorihikisakiName(torihikisakiCd);
		projectName = masterLogic.findProjectName(projectCd);
		segmentName = masterLogic.findSegmentName(segmentCd);

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

		uf1Name = masterLogic.findUfName("1", uf1Cd);
		uf2Name = masterLogic.findUfName("2", uf2Cd);
		uf3Name = masterLogic.findUfName("3", uf3Cd);
		uf4Name = masterLogic.findUfName("4", uf4Cd);
		uf5Name = masterLogic.findUfName("5", uf5Cd);
		uf6Name = masterLogic.findUfName("6", uf6Cd);
		uf7Name = masterLogic.findUfName("7", uf7Cd);
		uf8Name = masterLogic.findUfName("8", uf8Cd);
		uf9Name = masterLogic.findUfName("9", uf9Cd);
		uf10Name = masterLogic.findUfName("10", uf10Cd);

		//貸方
		kashiKamokuName = masterLogic.findKamokuNameStr(kashiKamokuCd);
		kashiKamokuEdabanName = masterLogic.findKamokuEdabanName(kashiKamokuCd, kashiKamokuEdabanCd);
		kashiFutanBumonName = masterLogic.findFutanBumonName(kashiFutanBumonCd);
	}

	/**
	 * 仮払申請EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		KaribaraiShinseiXlsLogic karibaraishinseixlsLogic = EteamContainer.getComponent(KaribaraiShinseiXlsLogic.class, connection);
		karibaraishinseixlsLogic.makeExcel(denpyouId, out);
	}
}