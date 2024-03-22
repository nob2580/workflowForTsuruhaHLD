package eteam.gyoumu.kaikei.kogamen;

import java.util.List;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.kaikei.DaishoMasterCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票明細追加ダイアログAction
 */
@Getter @Setter @ToString
public class ShiharaiIraiMeisaiAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** インデックス */
	String index;
	/** 最大インデックス */
	String maxIndex;

	/** 仕訳枝番号 */
	String shiwakeEdaNo;
	/** 取引名 */
	String torihikiName;
	/** 勘定科目コード */
	String kamokuCd;
	/** 勘定科目名 */
	String kamokuName;
	/** 勘定科目枝番コード */
	String kamokuEdabanCd;
	/** 勘定科目枝番名 */
	String kamokuEdabanName;
	/** 処理グループ */
	String shoriGroup;
	/** 負担部門コード */
	String futanBumonCd;
	/** 負担部門名 */
	String futanBumonName;
	/** 得意先コード */
	String tokuisakiCd;
	/** 得意先名 */
	String tokuisakiName;
	/** プロジェクトコード */
	String projectCd;
	/** プロジェクト名 */
	String projectName;
	/** セグメントコード */
	String segmentCd;
	/** セグメント名 */
	String segmentName;
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
	/** 課税区分 */
	String kazeiKbn;
	/** 消費税率区分 */
	String zeiritsu;
	/** 軽減税率区分 */
	String keigenZeiritsuKbn;
	/** 支払金額 */
	String shiharaiKingaku;
	/** 摘要 */
	String tekiyou;
	/** 分離区分 */
	String bunriKbn;
	/** 借方仕入区分 */
	String kariShiireKbn;
	/** 税抜金額 */
	String zeinukiKingaku;
	/** 消費税額 */
	String shouhizeigaku;

//＜画面入力以外＞
	/** 伝票区分 */
	String denpyouKbn;
	
	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス(明細） */
	GamenKoumokuSeigyo ks1;
	
	/** 社員コード連携エリア */
	String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
	/** 入力モード */
	boolean enableInput;
	/** 表示モード */
	Integer dispMode;
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean kamokuEdabanEnable;
	/** 負担部門選択ボタン */
	boolean futanBumonEnable;
	/** プロジェクトコード表示 */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** プロジェクト選択ボタン */
	boolean projectEnable;
	/** セグメントコード表示 */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	/** セグメント選択ボタン */
	boolean segmentEnable;
	/** UF1ボタン押下可否 */
	boolean uf1Enable;
	/** UF2ボタン押下可否 */
	boolean uf2Enable;
	/** UF3ボタン押下可否 */
	boolean uf3Enable;
	/** UF4ボタン押下可否 */
	boolean uf4Enable;
	/** UF5ボタン押下可否 */
	boolean uf5Enable;
	/** UF6ボタン押下可否 */
	boolean uf6Enable;
	/** UF7ボタン押下可否 */
	boolean uf7Enable;
	/** UF8ボタン押下可否 */
	boolean uf8Enable;
	/** UF9ボタン押下可否 */
	boolean uf9Enable;
	/** UF10ボタン押下可否 */
	boolean uf10Enable;
	/** 消費税率変更可否 */
	boolean zeiritsuEnable;
	
	//プルダウン等の候補値
	/** 消費税率DropDownList */
	List<Shouhizeiritsu> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<NaibuCdSetting> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
	/** 分離区分DropDownList */
	List<NaibuCdSetting> bunriKbnList;
	/** 分離区分ドメイン */
	String[] bunriKbnDomain;
	/** 仕入区分DropDownList */
	List<NaibuCdSetting> shiireKbnList;
	/** 仕入区分ドメイン */
	String[] shiireKbnDomain;

//＜部品＞
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** マスターSELECT */
	DaishoMasterCategoryLogic dMasterLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 税率Dao */
	ShouhizeiritsuAbstractDao zeiritsuDao;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	
	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 項目									//項目名										//キー項目？
		checkString(denpyouKbn, 4, 4, "伝票区分", true);
		checkNumber(index, 1, 3, "インデックス", false);
		checkNumber(maxIndex, 1, 3, "最大インデックス", false);
		checkNumber(shiwakeEdaNo, 1, 10, ks1.torihiki.getName() + "コード", false);
		checkString(torihikiName, 1, 20, ks1.torihiki.getName() + "名", false);
		checkString(kamokuCd, 1, 6, ks1.kamoku.getName() + "コード", false);
		checkString(kamokuName, 1, 22, ks1.kamoku.getName() + "名", false);
		checkString(kamokuEdabanCd, 1, 12, ks1.kamokuEdaban.getName() + "コード", false);
		checkString(kamokuEdabanName, 1, 20, ks1.kamokuEdaban.getName() + "名", false);
		checkString(futanBumonCd, 1, 8, ks1.futanBumon.getName() + "コード", false);
		checkString(futanBumonName, 1, 20, ks1.futanBumon.getName() + "名", false);
		checkString(projectCd, 1, 12, ks1.project.getName() + "コード", false);
		checkString(projectName, 1, 20, ks1.project.getName() + "名", false);
		checkString(segmentCd, 1, 8, ks1.segment.getName() + "コード", false);
		checkString(segmentName, 1, 20, ks1.segment.getName() + "名", false);
		checkString(uf1Cd, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString(uf1Name, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString(uf2Cd, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString(uf2Name, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString(uf3Cd, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString(uf3Name, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString(uf4Cd, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString(uf4Name, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString(uf5Cd, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString(uf5Name, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString(uf6Cd, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString(uf6Name, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString(uf7Cd, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString(uf7Name, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString(uf8Cd, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString(uf8Name, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString(uf9Cd, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString(uf9Name, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString(uf10Cd, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkString(uf10Name, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkDomain(kazeiKbn, kazeiKbnDomain, ks1.kazeiKbn.getName(), false);
		checkDomain(zeiritsu, zeiritsuDomain, ks1.zeiritsu.getName(), false);
		checkDomain(keigenZeiritsuKbn, Domain.FLG, "軽減税率区分", false);
		checkKingakuMinus(shiharaiKingaku, ks1.shiharaiKingaku.getName(), false);
		checkString(tekiyou, 1, commonLogic.tekiyouCheck(Open21Env.getVersion().toString()), ks1.tekiyou.getName(), false);
		checkSJIS(tekiyou, ks1.tekiyou.getName());
		checkKingakuMinus(shiharaiKingaku, ks1.shiharaiKingaku.getName(), false);
		checkDomain(this.bunriKbn, this.bunriKbnDomain, ks1.bunriKbn.getName(), false);
		checkDomain(this.kariShiireKbn, this.shiireKbnDomain, ks1.shiireKbn.getName(), false);
		checkKingakuMinus (this.zeinukiKingaku, ks1.zeinukiKingaku.getName(), false);
		checkKingakuMinus (this.shouhizeigaku, ks1.shouhizeigaku.getName(), false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		hissuCheckCommon(new String[][]{
			//項目						項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouKbn, "伝票区分", "2", "0"},
			{shiwakeEdaNo, ks1.torihiki.getName() + "コード", ks1.torihiki.getHissuFlgS(), "0"},
			{torihikiName, ks1.torihiki.getName() + "名", ks1.torihiki.getHissuFlgS(), "0"},
			{kamokuCd, ks1.kamoku.getName() + "コード", ks1.kamoku.getHissuFlgS(), "0"},
			{kamokuName, ks1.kamoku.getName() + "名", ks1.kamoku.getHissuFlgS(), "0"},
			{kamokuEdabanCd, ks1.kamokuEdaban.getName() + "名", ks1.kamokuEdaban.getHissuFlgS(), "0"},
			{kamokuEdabanName, ks1.kamokuEdaban.getName() + "名", ks1.kamokuEdaban.getHissuFlgS(), "0"},
			{futanBumonCd, ks1.futanBumon.getName() + "名", ks1.futanBumon.getHissuFlgS(), "0"},
			{futanBumonName, ks1.futanBumon.getName() + "名", ks1.futanBumon.getHissuFlgS(), "0"},
			{projectCd, ks1.project.getName() + "コード", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
			{projectName, ks1.project.getName() + "名", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
			{segmentCd, ks1.segment.getName() + "コード", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
			{segmentName, ks1.segment.getName() + "名", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
			{shiharaiKingaku, ks1.shiharaiKingaku.getName(), ks1.shiharaiKingaku.getHissuFlgS(), "0"},
			{tekiyou, ks1.tekiyou.getName(), ks1.tekiyou.getHissuFlgS(), "0"}, 
			{bunriKbn, ks1.bunriKbn.getName(), ks1.bunriKbn.getHissuFlgS(), "0"}, 
			{kariShiireKbn, ks1.shiireKbn.getName(), ks1.shiireKbn.getHissuFlgS(), "0"}, 
			{zeinukiKingaku, ks1.zeinukiKingaku.getName(), ks1.zeinukiKingaku.getHissuFlgS(), "0"}, 
			{shouhizeigaku, ks1.shouhizeigaku.getName(), ks1.shouhizeigaku.getHissuFlgS(), "0"}, 
		}, eventNum);

		if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf1Cd, hfUfSeigyo.getUf1Name() + "コード", ks1.uf1.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf2Cd, hfUfSeigyo.getUf2Name() + "コード", ks1.uf2.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf3Cd, hfUfSeigyo.getUf3Name() + "コード", ks1.uf3.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf4Cd, hfUfSeigyo.getUf4Name() + "コード", ks1.uf4.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf5Cd, hfUfSeigyo.getUf5Name() + "コード", ks1.uf5.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf6Cd, hfUfSeigyo.getUf6Name() + "コード", ks1.uf6.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf7Cd, hfUfSeigyo.getUf7Name() + "コード", ks1.uf7.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf8Cd, hfUfSeigyo.getUf8Name() + "コード", ks1.uf8.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf9Cd, hfUfSeigyo.getUf9Name() + "コード", ks1.uf9.getHissuFlgS()},
			}, eventNum);
		}
		
		if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf10Cd, hfUfSeigyo.getUf10Name() + "コード", ks1.uf10.getHissuFlgS()},
			}, eventNum);
		}
		
		hissuCheckCommon(new String[][]{
			{zeiritsu, ks1.zeiritsu.getName(), ks1.zeiritsu.getHissuFlgS(), "0"},
			{keigenZeiritsuKbn, "軽減税率区分", ks1.zeiritsu.getHissuFlgS(), "0"},
		}, eventNum);

	}
	
	//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		dMasterLogic = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		//プルダウンのリストを取得
		zeiritsuList = this.zeiritsuDao.load();
		zeiritsuDomain = zeiritsuList.stream().map(item -> item.zeiritsu.toString()).collect(Collectors.toList()).toArray(new String[0]);
		kazeiKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn"); // 支払依頼専用区分はただの重複で、今となっては考慮不要
		kazeiKbnDomain = kazeiKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		bunriKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn");
		bunriKbnDomain = bunriKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		shiireKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn");
		shiireKbnDomain = shiireKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		
		//税率の初期設定
		if (zeiritsu == null) {
			GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();
			zeiritsu = initZeiritsu.get("zeiritsu").toString();
			keigenZeiritsuKbn = initZeiritsu.get("keigen_zeiritsu_kbn");
		}
		
		kamokuEdabanEnable = false;
		futanBumonEnable = false;
		projectEnable = false;
		segmentEnable = false;
		uf1Enable = false;
		uf2Enable = false;
		uf3Enable = false;
		uf4Enable = false;
		uf5Enable = false;
		uf6Enable = false;
		uf7Enable = false;
		uf8Enable = false;
		uf9Enable = false;
		uf10Enable = false;
		zeiritsuEnable = false;
		
		ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.SIHARAIIRAI);

		// 画面項目制御クラス(支払金額) */
		ks1.shiharaiKingaku = new GamenKoumokuSeigyo(DENPYOU_KBN.SIHARAIIRAI).shiharaiKingaku;
		
		if (enableInput) {
			//明細単位に仕訳パターンによる制御
			if (isNotEmpty(shiwakeEdaNo)) {
				//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				ShiwakePatternMaster shiwakePattern = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo));
				InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern.map);
				kamokuEdabanEnable = info.kamokuEdabanEnable;
				futanBumonEnable = info.futanBumonEnable;
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
				zeiritsuEnable = info.zeiritsuEnable;
				
				//課税区分の制御
				if (isNotEmpty(kamokuCd)) {
					GMap kmk = masterLogic.findKamokuMaster(kamokuCd);
					if (kmk != null) shoriGroup = kmk.get("shori_group").toString();
				}
			}
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			displaySeigyo(connection);
		}

		return "success";
	}

	/**
	 * ダイアログ確定
	 * @return 結果
	 */
	public String confirm() {

		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			displaySeigyo(connection);

			formatCheck();
			hissuCheck(1);

			if (!errorList.isEmpty()) {
				return "error";
			}
		}

		return "success";
	}
}
