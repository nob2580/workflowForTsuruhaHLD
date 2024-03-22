package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamConst.keigenZeiritsuKbn;
import eteam.common.EteamEkispertCommon;
import eteam.common.EteamEkispertCommonLogic;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_KIKAN_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.KiKesnAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.TsuukinteikiAbstractDao;
import eteam.database.dao.KiKesnDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.TsuukinteikiDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Tsuukinteiki;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 通勤定期申請画面Action
 */
@Getter @Setter @ToString
public class TsuukinTeikiShinseiAction extends EteamEkispertCommon {

//＜定数＞
	/** 無期限延長時の終了日 */
	final String MUKIGEN_DATE = "9999/12/31";

//＜画面入力＞
	/** 仕訳枝番号 */
	String shiwakeEdaNo;
	/** 取引名 */
	String torihikiName;
	/** 共通部分摘要(取引名)注記 */
	String chuuki1;
	/** 伝票摘要(取引名)注記 */
	String chuuki2;
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
	/** 使用期間区分 */
	String shiyouKikanKbn;
	/** 使用開始日 */
	String shiyouKaishiBi;
	/** 使用終了日 */
	String shiyouShuuryouBi;
	/** 乗車区間 */
	String jyoushaKukan;
	/** 定期シリアライズデータ */
	String teikiSerializeData;
	/** 消費税率 */
	String zeiritsu;
	/** 支払日 */
	String shiharaiBi;
	/** 手入力フラグ */
	String tenyuuryokuFlg;
	/** 処理グループ */
	String shoriGroup;

//＜画面入力以外＞

	//プルダウン等
	/** 使用期間区分List */
	List<GMap> shiyouKikanList;
	/** 使用期間チェック用 */
	String[] shiyouKikanKbnDomain;
	/** 消費税率DropDownList */
	List<GMap> zeiritsuList;
	/** 消費税率チェック用 */
	String[] zeiritsuDomain;
	/** 課税区分DropDownList */
	List<GMap> kazeiKbnList;
	/** 課税区分ドメイン */
	String[] kazeiKbnDomain;
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

	//画面制御情報
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks = new GamenKoumokuSeigyo(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI);

	/** 入力モード */
	boolean enableInput;
	String str;
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean kamokuEdabanEnable;
	/** 負担部門選択ボタン押下可否 */
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
	/** 駅すぱあと連携可否 */
	boolean ekispertEnable;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean enableBumonSecurity;

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
	/** 貸方取引先コード */
	String kashiTorihikisakiCd;
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
	/** 摘要コード */
	String tekiyouCd;
	/** 税抜金額 */
	String zeinukiKingaku;
	/** 消費税額 */
	String shouhizeigaku;
	/** 支払先名 */
	String shiharaisakiName;
	/** 事業者区分 */
	String jigyoushaKbn;
	/** 分離区分 */
	String bunriKbn;
	/** 借方仕入区分 */
	String kariShiireKbn;
	/** 貸方仕入区分 */
	String kashiShiireKbn;
	/** インボイス伝票 */
	String invoiceDenpyou;
	
//＜部品＞
	//Logic系面倒なので最初にnewしとく
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** 駅すぱあとロジック */
	EteamEkispertCommonLogic ekiLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** システム管理SELECT */
	SystemKanriCategoryLogic systemKanriLogic;
	/** ワークフローSELECT */
	WorkflowCategoryLogic wfLogic;
	/** ワークフローEVENT */
	WorkflowEventControlLogic wfEventLogic;
	/** 部門・ユーザー管理SELECT */
	BumonUserKanriCategoryLogic bumonUserLc;
	/** 通知ロジック */
	TsuuchiCategoryLogic tsuuchiLogic;
	/** 通勤定期ロジック */
	TsuukinTeikiShinseiLogic gyoumuLogic;
	/** 通勤定期Dao */
	TsuukinteikiAbstractDao tsuukinteikiDao;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 仕訳パターンマスターDao */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** 決算期Dao */
	KiKesnAbstractDao kiKesnDao;

//＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {
		checkString (shiwakeEdaNo, 1 ,10, ks.torihiki.getName() + "コード", false);
		checkString (torihikiName, 1, 20, ks.torihiki.getName() + "名", false);
		checkString (kamokuCd, 1, 6, ks.kamoku.getName() + "コード", false);
		checkString (kamokuName, 1, 22, ks.kamoku.getName() + "名", false);
		checkString (kamokuEdabanCd, 1, 12, ks.kamokuEdaban.getName() + "コード", false);
		checkString (kamokuEdabanName, 1, 20, ks.kamokuEdaban.getName() + "名", false);
		checkString (futanBumonCd, 1, 8, ks.futanBumon.getName() + "コード", false);
		checkString (futanBumonName, 1, 20, ks.futanBumon.getName() + "名", false);
		checkString(torihikisakiCd, 1, 12, ks.torihikisaki.getName() + "コード", false);
		checkString(torihikisakiName, 1, 20, ks.torihikisaki.getName() + "名", false);
		checkString(projectCd, 1, 12, ks.project.getName() + "コード：", false);
		checkString(projectName, 1, 20, ks.project.getName() + "名：", false);
		checkString(segmentCd, 1, 8, ks.segment.getName() + "コード：", false);
		checkString(segmentName, 1, 20, ks.segment.getName() + "名：", false);
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

		checkDomain (shiyouKikanKbn, shiyouKikanKbnDomain, ks.shiyouKikanKbn.getName(), false);
		checkDate (shiyouKaishiBi, ks.shiyouKaishibi.getName(), false);
		checkDate (shiyouShuuryouBi, ks.shiyouShuuryoubi.getName(), false);
		checkDomain (zeiritsu, zeiritsuDomain, ks.zeiritsu.getName(), false);
		checkKingakuOver1 (kingaku, ks.kingaku.getName(), false);
		checkDate (shiharaiBi, "支払日", false);
		checkDomain (tenyuuryokuFlg, Domain.FLG, "手入力フラグ", false);
		checkString (shiharaisakiName, 1, 20, ks.shiharaisaki.getName() + "名", false);
		checkDomain(jigyoushaKbn, jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
		checkDomain(bunriKbn, bunriKbnDomain, ks.bunriKbn.getName(), false);
		checkDomain(kariShiireKbn, this.shiireKbnDomain, ks.shiireKbn.getName(),false);
		checkKingakuMinus(zeinukiKingaku, ks.zeinukiKingaku10Percent.getName(), false);
		checkKingakuMinus(shouhizeigaku, ks.shouhizeigaku.getName(), false);
		checkDomain(this.invoiceDenpyou, this.invoiceDenpyouDomain, "インボイス対応伝票", false);
	}

	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum  1:登録/更新、2:支払日更新
	 */
	protected void denpyouHissuCheck(int eventNum) {
		String[][] list = {
			//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{ shiwakeEdaNo, ks.torihiki.getName() + "コード", ks.torihiki.getHissuFlgS(), "0" },
				{ torihikiName, ks.torihiki.getName() + "名", ks.torihiki.getHissuFlgS(), "0" },
				{ kamokuCd, ks.kamoku.getName() + "コード", ks.kamoku.getHissuFlgS(), "0" },
				{ kamokuName, ks.kamoku.getName() + "名", ks.kamoku.getHissuFlgS(), "0" },
				{ kamokuEdabanCd, ks.kamokuEdaban.getName() + "コード", ks.kamokuEdaban.getHissuFlgS(), "0" },
				{ kamokuEdabanName, ks.kamokuEdaban.getName() + "名", ks.kamokuEdaban.getHissuFlgS(), "0" },
				{ futanBumonCd, ks.futanBumon.getName() + "コード", ks.futanBumon.getHissuFlgS(), "0" },
				{ futanBumonName, ks.futanBumon.getName() + "名", ks.futanBumon.getHissuFlgS(), "0" },
				{torihikisakiCd, ks.torihikisaki.getName()+ "コード：", ks.torihikisaki.getHissuFlgS(), "0"},
				{torihikisakiName, ks.torihikisaki.getName()+ "名："	, ks.torihikisaki.getHissuFlgS(), "0"},
				{projectCd, ks.project.getName() + "コード："	, ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
				{projectName, ks.project.getName() + "名："		, ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
				{segmentCd, ks.segment.getName() + "コード："	, ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
				{segmentName, ks.segment.getName() + "名："		, ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
				{ shiyouKikanKbn, ks.shiyouKikanKbn.getName(), ks.shiyouKikanKbn.getHissuFlgS(), "0" },
				{ shiyouKaishiBi, ks.shiyouKaishibi.getName(), ks.shiyouKaishibi.getHissuFlgS(), "0" },
				{ shiyouShuuryouBi, ks.shiyouShuuryoubi.getName(), ks.shiyouShuuryoubi.getHissuFlgS(), "0" },
				{ jyoushaKukan, ks.jyoushaKukan.getName(), ks.jyoushaKukan.getHissuFlgS(), "0" },
				{ zeiritsu, ks.zeiritsu.getName(), List.of("001", "002", "011", "012", "013", "014").contains(this.kazeiKbn) ? ks.zeiritsu.getHissuFlgS() : "0", "0" },
				{ kingaku, ks.kingaku.getName(), ks.kingaku.getHissuFlgS(), "0" },
				{ shiharaiBi, "支払日", "0", "1" }, { tenyuuryokuFlg, "手入力フラグ", "1", "0" },
				 { invoiceDenpyou, "インボイス伝票", "1" },
				{ jigyoushaKbn, ks.jigyoushaKbn.getName(), ks.jigyoushaKbn.getHissuFlgS(), "0"},
				{ bunriKbn, ks.bunriKbn.getName(), ks.bunriKbn.getHissuFlgS(), "0"},
				{ kariShiireKbn, ks.shiireKbn.getName(), ks.shiireKbn.getHissuFlgS(), "0"},
				{ shiharaisakiName, ks.shiharaisaki.getName(), ks.shiharaisaki.getHissuFlgS(), "0"}
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
	};

	/**
	 * 業務関連の関連チェック処理
	 */
	protected void soukanCheck() {

		// 社員口座存在チェック
		commonLogic.checkShainKouza(super.getKihyouUserId(), null, errorList);

		// 消費税率チェック
		commonLogic.checkZeiritsu(toDecimal(zeiritsu), keigenZeiritsuKbn.NORMAL, null, errorList);

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

		//仕訳パターン
		GMap shiwakePattern = kaikeiLogic.findShiwakePattern(super.denpyouKbn, Integer.parseInt(shiwakeEdaNo));

		// 借方
		ShiwakeCheckData shiwakeCheckDataKari = commonLogic.new ShiwakeCheckData() ;
		shiwakeCheckDataKari.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
		shiwakeCheckDataKari.shiwakeEdaNo = shiwakeEdaNo;
		shiwakeCheckDataKari.kamokuNm = ks.kamoku.getName() + "コード";
		shiwakeCheckDataKari.kamokuCd = kamokuCd;
		shiwakeCheckDataKari.kamokuEdabanNm = "借方" + ks.kamokuEdaban.getName() + "コード";
		shiwakeCheckDataKari.kamokuEdabanCd = kamokuEdabanCd;
		shiwakeCheckDataKari.futanBumonNm = ks.futanBumon.getName() + "コード";
		shiwakeCheckDataKari.futanBumonCd = futanBumonCd;
		shiwakeCheckDataKari.torihikisakiNm = ks.torihikisaki.getName() + "コード";
		shiwakeCheckDataKari.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kari_torihikisaki_cd")) ? torihikisakiCd : (String)shiwakePattern.get("kari_torihikisaki_cd");
		shiwakeCheckDataKari.projectNm = ks.project.getName() + "コード";
		if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kari_project_cd")))
		{
			shiwakeCheckDataKari.projectCd = projectCd;
		}
		shiwakeCheckDataKari.segmentNm = ks.segment.getName() + "コード";
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
		shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd1")) ? torihikisakiCd : (String)shiwakePattern.get("kashi_torihikisaki_cd1");
		shiwakeCheckDataKashi.projectNm =  "貸方" + ks.project.getName() + "コード";
		if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd1")))
		{
			shiwakeCheckDataKashi.projectCd = projectCd;
		}
		shiwakeCheckDataKashi.segmentNm =  "貸方" + ks.segment.getName() + "コード";
		if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd1")))
		{
			shiwakeCheckDataKashi.segmentCd = segmentCd;
		}
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

//＜伝票共通から呼ばれるイベント処理＞
	//伝票個別の表示処理(JSPに渡すデータ取得)
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		//参照フラグ(ture:参照起票である、false:参照起票でない)
		boolean sanshou = false;

		// 社員コード取得
		GMap userInfo = bumonUserLc.selectUserInfo(super.getKihyouUserId());
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

			// 申請と取引が1:1で決まる場合
			List<ShiwakePatternMaster> list = this.shiwakePatternMasterDao.loadByDenpyouKbn(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, this.futanBumonCd);
			if(!list.isEmpty()) {
				if(list.size() == 1) {
					shiwakeEdaNo = Integer.toString(list.get(0).shiwakeEdano);
					torihikiName =  list.get(0).torihikiName;
					this.kazeiKbn = list.get(0).kariKazeiKbn;
					this.bunriKbn = list.get(0).kariBunriKbn;
					this.kariShiireKbn = list.get(0).kariShiireKbn;
					this.zeiritsu = list.get(0).kariZeiritsu;
					if(!list.get(0).kariKamokuEdabanCd.equals("<EDABAN>")) {
						this.kamokuEdabanCd = list.get(0).kariFutanBumonCd;
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
					// 仕訳パターン情報読込（相関チェック前に必要）
					reloadShiwakePattern(connection);
					//マスター等から名称は引き直す
					reloadMaster(connection);
				}
			}

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			Tsuukinteiki shinseiData = this.tsuukinteikiDao.find(denpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			Tsuukinteiki shinseiData = this.tsuukinteikiDao.find(sanshouDenpyouId);
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
			shiyouKaishiBi = "";
			shiyouShuuryouBi = "";
			shiharaiBi = "";
		}
		
		//課税区分の制御
		if (isNotEmpty(kamokuCd)) {
			GMap kmk = masterLogic.findKamokuMaster(kamokuCd);
			if (kmk != null) shoriGroup = kmk.get("shori_group").toString();
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

	//更新ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		//登録処理
		Tsuukinteiki dto = this.createDto();
		this.tsuukinteikiDao.insert(dto, dto.tourokuUserId);
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
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			commonLogic.checkShiharaiBi(denpyouId, toDate(shiharaiBi), null, null, SHIHARAI_HOUHOU.FURIKOMI, loginUserInfo, errorList);
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
		Tsuukinteiki dto = this.createDto();
		this.tsuukinteikiDao.update(dto, dto.tourokuUserId);
	}

	//承認ボタン押下時に、個別伝票について以下を行う。
	//・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（エラー発生時用）
		displaySeigyo(connection);

		//支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			String shiharai = formatDate(kaikeiLogic.findTsuukinnteiki(denpyouId).get("shiharaibi"));
			commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), null, null, SHIHARAI_HOUHOU.FURIKOMI, loginUserInfo, errorList);
			// エラーの場合の表示用に現実の値を設定
			shiharaiBi = shiharai;
		}
		if (0 <errorList.size())
		{
			return;
		}

		//定期情報の更新
		if (wfEventLogic.isLastShounin(denpyouId, loginUserInfo) && ! "1".equals(tenyuuryokuFlg)) {

			//会社設定「通勤定期仕訳作成有無」：0(作成しない)
			//「仕訳作成しない場合に保持する定期情報の有効期限」：1(申請内容に関わらず無期限)
			// の場合、定期情報テーブルに登録する期間終了日付を9999/12/31とする(通勤定期申請伝票本体の使用終了日はそのまま)
			String teikiJouhouShuuryouBi = shiyouShuuryouBi;
			if ( "0".equals(setting.tsuukinteikiShiwakeSakuseiUmu()) && "1".equals(setting.teikiJouhouYuukouKigen()) ){
				teikiJouhouShuuryouBi = MUKIGEN_DATE;
			}
			String kihyouUserId = (String)wfLogic.selectKihyoushaData(denpyouId).get("user_id");
			//定期情報テーブルで重複する期間がある場合、ロジック内部で上書きされる
			ekiLogic.updateTeikiJouhou(jyoushaKukan, teikiSerializeData, toDate(shiyouKaishiBi), toDate(teikiJouhouShuuryouBi), kihyouUserId, loginUserInfo);
		}
	}

	//個別業務の支払日の格納を実装。
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
		ringiData.put("ringiKingakuZandaka", zandaka.subtract(toDecimal(kingaku)));
	}

	//代表仕訳枝番号を設定
	@Override
	protected String getDaihyouTorihiki(){
		return shiwakeEdaNo;
	}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		gyoumuLogic = EteamContainer.getComponent(TsuukinTeikiShinseiLogic.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		systemKanriLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		bumonUserLc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		ekiLogic = EteamContainer.getComponent(EteamEkispertCommonLogic.class, connection);
		this.tsuukinteikiDao = EteamContainer.getComponent(TsuukinteikiDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		this.kiKesnDao = EteamContainer.getComponent(KiKesnDao.class, connection);
	}

	/**
	 * 未登録の時、デフォルトの申請内容表示状態に項目セットする。
	 * @param connection コネクション
	 */
	protected void makeDefaultShinsei(EteamConnection connection) {
		shiyouKikanKbn = SHIYOU_KIKAN_KBN.KIKAN1;
		zeiritsu = masterLogic.findValidShouhizeiritsuExclusionKeigen().toPlainString();
		tenyuuryokuFlg = "1"; //初期値は手入力とする
	}

	/**
	 * 通勤定期テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 仮払申請レコード
	 * @param sanshou     参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection  コネクション
	 */
	protected void shinseiData2Gamen(Tsuukinteiki shinseiData, boolean sanshou, EteamConnection connection) {
		this.shiwakeEdaNo = String.valueOf(shinseiData.shiwakeEdano);
		this.torihikiName = shinseiData.torihikiName;
		this.kamokuCd = shinseiData.kariKamokuCd;
		this.kamokuName = shinseiData.kariKamokuName;
		this.kamokuEdabanCd = shinseiData.kariKamokuEdabanCd;
		this.kamokuEdabanName = shinseiData.kariKamokuEdabanName;
		this.futanBumonCd = shinseiData.kariFutanBumonCd;
		this.futanBumonName = shinseiData.kariFutanBumonName;
		this.torihikisakiCd = shinseiData.torihikisakiCd;
		this.torihikisakiName = shinseiData.torihikisakiNameRyakushiki;
		this.projectCd = shinseiData.projectCd;
		this.projectName = shinseiData.projectName;
		this.segmentCd = shinseiData.segmentCd;
		this.segmentName = shinseiData.segmentNameRyakushiki;
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
		this.uf1Cd = shinseiData.uf1Cd;
		this.uf1Name = shinseiData.uf1NameRyakushiki;
		this.uf2Cd = shinseiData.uf2Cd;
		this.uf2Name = shinseiData.uf2NameRyakushiki;
		this.uf3Cd = shinseiData.uf3Cd;
		this.uf3Name = shinseiData.uf3NameRyakushiki;
		this.uf4Cd = shinseiData.uf4Cd;
		this.uf4Name = shinseiData.uf4NameRyakushiki;
		this.uf5Cd = shinseiData.uf5Cd;
		this.uf5Name = shinseiData.uf5NameRyakushiki;
		this.uf6Cd = shinseiData.uf6Cd;
		this.uf6Name = shinseiData.uf6NameRyakushiki;
		this.uf7Cd = shinseiData.uf7Cd;
		this.uf7Name = shinseiData.uf7NameRyakushiki;
		this.uf8Cd = shinseiData.uf8Cd;
		this.uf8Name = shinseiData.uf8NameRyakushiki;
		this.uf9Cd = shinseiData.uf9Cd;
		this.uf9Name = shinseiData.uf9NameRyakushiki;
		this.uf10Cd = shinseiData.uf10Cd;
		this.uf10Name = shinseiData.uf10NameRyakushiki;
		this.shiyouKikanKbn = shinseiData.shiyouKikanKbn;
		this.shiyouKaishiBi = formatDate(shinseiData.shiyouKaishibi);
		this.shiyouShuuryouBi = formatDate(shinseiData.shiyouShuuryoubi);
		this.jyoushaKukan = shinseiData.jyoushaKukan;
		this.teikiSerializeData = shinseiData.teikiSerializeData;
		this.zeiritsu = shinseiData.zeiritsu.toString();
		this.kingaku = formatMoney(shinseiData.kingaku);
		this.shiharaiBi = formatDate(shinseiData.shiharaibi);
		this.tenyuuryokuFlg = shinseiData.tenyuuryokuFlg;
		this.zeinukiKingaku = formatMoney(shinseiData.zeinukiKingaku);
		this.shouhizeigaku = formatMoney(shinseiData.shouhizeigaku);
		this.shiharaisakiName = shinseiData.shiharaisakiName;
		this.invoiceDenpyou = shinseiData.invoiceDenpyou;
		this.kazeiKbn  	= shinseiData.kariKazeiKbn;
		this.jigyoushaKbn = shinseiData.jigyoushaKbn;
		this.bunriKbn  	= shinseiData.bunriKbn;
		this.kariShiireKbn = shinseiData.kariShiireKbn;
		this.kashiShiireKbn = shinseiData.kashiShiireKbn;
		if(!sanshou){
			GMap meisai = null;
			BatchKaikeiCommonLogic batchkaikeilogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
			String shiwakeTekiyouNoCut = batchkaikeilogic.shiwakeTekiyou(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, shinseiData.map, meisai, "0");
			String shiwakeTekiyou = batchkaikeilogic.cutTekiyou(shiwakeTekiyouNoCut);
			if(commonLogic.tekiyouCheck(Open21Env.getVersion().toString()) < EteamCommon.getByteLength(shiwakeTekiyouNoCut)){
				chuuki1 = commonLogic.getTekiyouChuuki();
				chuuki2 = commonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
			}
		}
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		// リスト作成
		shiyouKikanList = systemKanriLogic.loadNaibuCdSetting("shiyou_kikan_kbn");
		shiyouKikanKbnDomain = EteamCommon.mapList2Arr(shiyouKikanList, "naibu_cd");
		zeiritsuList = masterLogic.loadShouhizeiritsuExclusionKeigen();
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
		

		//各種入力チェックドメイン

		//入力（登録、更新）可能かどうか判定。
		enableInput = super.judgeEnableInput();

		// 支払日登録可能かどうか制御 (0:非表示、1:入力可、2:表示)
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		//入力可能時の制御
		if (enableInput) {
			// 仕訳パターンによる制御
			if (isNotEmpty(shiwakeEdaNo)) {
				//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				GMap shiwakePattern = kaikeiLogic.findShiwakePattern(super.denpyouKbn, Integer.parseInt(shiwakeEdaNo));
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

			// 駅すぱあと連携可否判定
			ekispertEnable = !"9".equals(setting.intraFlg());
			if(ekispertEnable) {
				// 駅すぱあと呼出 start
				super.ekispertInit(connection, SEARCH_MODE_TEIKI);
			}
		}

		//経理担当が初めて伝票を開いたとき、支払日に自動入力
		if (shiharaiBiMode == 1  && isEmpty(shiharaiBi)) {
			Date shiharai = null;
			if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A006).equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(super.getKihyouBi()));
			} else if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A006).equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(super.getKihyouBi()));
			}
			if (shiharai != null) {
				shiharaiBi = formatDate(shiharai);
				// マスターから基準日(起票日)に対する振込日が見つかった場合、支払日の更新
				this.tsuukinteikiDao.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}
	}

	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		String daihyouBumonCd = super.daihyouFutanBumonCd;
		ShiwakePatternMaster shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, Integer.parseInt(shiwakeEdaNo));

		// 社員コード取得
		GMap userInfo = bumonUserLc.selectUserInfo(super.getKihyouUserId());
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(super.getKihyouUserId());

		//取引名
		torihikiName = shiwakeP.torihikiName;

		//借方　科目
		kamokuCd = shiwakeP.kariKamokuCd;

		//借方　科目枝番
		String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
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
		if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) ){
			enableBumonSecurity = commonLogic.checkFutanBumonWithSecurity(futanBumonCd, ks.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
		}
		// 借方負担部門コード
		futanBumonCd = commonLogic.convFutanBumon(futanBumonCd, shiwakeP.kariFutanBumonCd, daihyouBumonCd);

		//借方　UF1-10
		if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.shainCdRenkeiFlg.equals(("1"))){
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
		kariUf1Cd = commonLogic.convUf(uf1Cd, shiwakeP.kariUf1Cd);
		kariUf2Cd = commonLogic.convUf(uf2Cd, shiwakeP.kariUf2Cd);
		kariUf3Cd = commonLogic.convUf(uf3Cd, shiwakeP.kariUf3Cd);
		kariUf4Cd = commonLogic.convUf(uf4Cd, shiwakeP.kariUf4Cd);
		kariUf5Cd = commonLogic.convUf(uf5Cd, shiwakeP.kariUf5Cd);
		kariUf6Cd = commonLogic.convUf(uf6Cd, shiwakeP.kariUf6Cd);
		kariUf7Cd = commonLogic.convUf(uf7Cd, shiwakeP.kariUf7Cd);
		kariUf8Cd = commonLogic.convUf(uf8Cd, shiwakeP.kariUf8Cd);
		kariUf9Cd = commonLogic.convUf(uf9Cd, shiwakeP.kariUf9Cd);
		kariUf10Cd = commonLogic.convUf(uf10Cd, shiwakeP.kariUf10Cd);

		//貸方　科目コード
		kashiKamokuCd = shiwakeP.kashiKamokuCd1;

		//貸方　科目枝番コード
		String pKashiKamokuEdabanCd = shiwakeP.kashiKamokuEdabanCd1;
		this.kashiKamokuEdabanCd = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
				? shainShiwakeEdaNo
				: pKashiKamokuEdabanCd;

		//貸方　負担部門コード
		kashiFutanBumonCd = commonLogic.convFutanBumon(futanBumonCd, shiwakeP.kashiFutanBumonCd1, daihyouBumonCd);

		//貸方　課税区分
		kashiKazeiKbn = shiwakeP.kashiKazeiKbn1;
		// 貸方 仕入区分
		kashiShiireKbn = (shiwakeP.kashiShiireKbn1 != null) ? shiwakeP.kashiShiireKbn1 : "" ;

		//貸方　UF1-10
		kashiUf1Cd1 = commonLogic.convUf(uf1Cd, shiwakeP.kashiUf1Cd1);
		kashiUf2Cd1 = commonLogic.convUf(uf2Cd, shiwakeP.kashiUf2Cd1);
		kashiUf3Cd1 = commonLogic.convUf(uf3Cd, shiwakeP.kashiUf3Cd1);
		kashiUf4Cd1 = commonLogic.convUf(uf4Cd, shiwakeP.kashiUf4Cd1);
		kashiUf5Cd1 = commonLogic.convUf(uf5Cd, shiwakeP.kashiUf5Cd1);
		kashiUf6Cd1 = commonLogic.convUf(uf6Cd, shiwakeP.kashiUf6Cd1);
		kashiUf7Cd1 = commonLogic.convUf(uf7Cd, shiwakeP.kashiUf7Cd1);
		kashiUf8Cd1 = commonLogic.convUf(uf8Cd, shiwakeP.kashiUf8Cd1);
		kashiUf9Cd1 = commonLogic.convUf(uf9Cd, shiwakeP.kashiUf9Cd1);
		kashiUf10Cd1 = commonLogic.convUf(uf10Cd, shiwakeP.kashiUf10Cd1);

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
		//社員コードを摘要コードに反映する場合
		if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
			if(shainCd.length() > 4) {
				tekiyouCd = shainCd.substring(shainCd.length()-4);
			} else {
				tekiyouCd = shainCd;
			}
		} else {
			tekiyouCd = "";
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
	 * 通勤定期申請EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		TsuukinteikiShinseiXlsLogic tsuukinteikishinseixlsLogic = EteamContainer.getComponent(TsuukinteikiShinseiXlsLogic.class, connection);
		tsuukinteikishinseixlsLogic.makeExcel(denpyouId, out);
	}
	
	/**
	 * @return 通勤定期Dto
	 */
	protected Tsuukinteiki createDto()
	{
		Tsuukinteiki tsuukinteiki = new Tsuukinteiki();
		tsuukinteiki.denpyouId = this.denpyouId;
		tsuukinteiki.shiyouKikanKbn = this.shiyouKikanKbn;
		tsuukinteiki.shiyouKaishibi = super.toDate(shiyouKaishiBi);
		tsuukinteiki.shiyouShuuryoubi = super.toDate(shiyouShuuryouBi);
		tsuukinteiki.jyoushaKukan = this.jyoushaKukan;
		tsuukinteiki.teikiSerializeData = this.teikiSerializeData;
		tsuukinteiki.shiharaibi = super.toDate(shiharaiBi);
		tsuukinteiki.tekiyou = this.torihikiName;
		tsuukinteiki.zeiritsu = super.toDecimalZeroIfEmpty(zeiritsu);
		tsuukinteiki.keigenZeiritsuKbn = keigenZeiritsuKbn.NORMAL;//0固定
		tsuukinteiki.kingaku = super.toDecimal(kingaku);
		tsuukinteiki.tenyuuryokuFlg = this.tenyuuryokuFlg;
		tsuukinteiki.hf1Cd = this.hf1Cd;
		tsuukinteiki.hf1NameRyakushiki = this.hf1Name;
		tsuukinteiki.hf2Cd = this.hf2Cd;
		tsuukinteiki.hf2NameRyakushiki = this.hf2Name;
		tsuukinteiki.hf3Cd = this.hf3Cd;
		tsuukinteiki.hf3NameRyakushiki = this.hf3Name;
		tsuukinteiki.hf4Cd = this.hf4Cd;
		tsuukinteiki.hf4NameRyakushiki = this.hf4Name;
		tsuukinteiki.hf5Cd = this.hf5Cd;
		tsuukinteiki.hf5NameRyakushiki = this.hf5Name;
		tsuukinteiki.hf6Cd = this.hf6Cd;
		tsuukinteiki.hf6NameRyakushiki = this.hf6Name;
		tsuukinteiki.hf7Cd = this.hf7Cd;
		tsuukinteiki.hf7NameRyakushiki = this.hf7Name;
		tsuukinteiki.hf8Cd = this.hf8Cd;
		tsuukinteiki.hf8NameRyakushiki = this.hf8Name;
		tsuukinteiki.hf9Cd = this.hf9Cd;
		tsuukinteiki.hf9NameRyakushiki = this.hf9Name;
		tsuukinteiki.hf10Cd = this.hf10Cd;
		tsuukinteiki.hf10NameRyakushiki = this.hf10Name;
		tsuukinteiki.shiwakeEdano = Integer.parseInt(shiwakeEdaNo);
		tsuukinteiki.torihikiName = this.torihikiName;
		tsuukinteiki.kariFutanBumonCd = this.futanBumonCd;
		tsuukinteiki.kariFutanBumonName = this.futanBumonName;
		tsuukinteiki.torihikisakiCd = this.torihikisakiCd;
		tsuukinteiki.torihikisakiNameRyakushiki = this.torihikisakiName;
		tsuukinteiki.kariKamokuCd = this.kamokuCd;
		tsuukinteiki.kariKamokuName = this.kamokuName;
		tsuukinteiki.kariKamokuEdabanCd = this.kamokuEdabanCd;
		tsuukinteiki.kariKamokuEdabanName = this.kamokuEdabanName;
		tsuukinteiki.kariKazeiKbn = this.kazeiKbn;
		tsuukinteiki.kashiFutanBumonCd = this.kashiFutanBumonCd;
		tsuukinteiki.kashiFutanBumonName = this.kashiFutanBumonName;
		tsuukinteiki.kashiKamokuCd = this.kashiKamokuCd;
		tsuukinteiki.kashiKamokuName = this.kashiKamokuName;
		tsuukinteiki.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd;
		tsuukinteiki.kashiKamokuEdabanName = this.kashiKamokuEdabanName;
		tsuukinteiki.kashiKazeiKbn = this.kashiKazeiKbn;
		tsuukinteiki.uf1Cd = this.uf1Cd;
		tsuukinteiki.uf1NameRyakushiki = this.uf1Name;
		tsuukinteiki.uf2Cd = this.uf2Cd;
		tsuukinteiki.uf2NameRyakushiki = this.uf2Name;
		tsuukinteiki.uf3Cd = this.uf3Cd;
		tsuukinteiki.uf3NameRyakushiki = this.uf3Name;
		tsuukinteiki.uf4Cd = this.uf4Cd;
		tsuukinteiki.uf4NameRyakushiki = this.uf4Name;
		tsuukinteiki.uf5Cd = this.uf5Cd;
		tsuukinteiki.uf5NameRyakushiki = this.uf5Name;
		tsuukinteiki.uf6Cd = this.uf6Cd;
		tsuukinteiki.uf6NameRyakushiki = this.uf6Name;
		tsuukinteiki.uf7Cd = this.uf7Cd;
		tsuukinteiki.uf7NameRyakushiki = this.uf7Name;
		tsuukinteiki.uf8Cd = this.uf8Cd;
		tsuukinteiki.uf8NameRyakushiki = this.uf8Name;
		tsuukinteiki.uf9Cd = this.uf9Cd;
		tsuukinteiki.uf9NameRyakushiki = this.uf9Name;
		tsuukinteiki.uf10Cd = this.uf10Cd;
		tsuukinteiki.uf10NameRyakushiki = this.uf10Name;
		tsuukinteiki.projectCd = this.projectCd;
		tsuukinteiki.projectName = this.projectName;
		tsuukinteiki.segmentCd = this.segmentCd;
		tsuukinteiki.segmentNameRyakushiki = this.segmentName;
		tsuukinteiki.tekiyouCd = this.tekiyouCd;
		tsuukinteiki.tourokuUserId = this.loginUserId;
		tsuukinteiki.koushinUserId = this.loginUserId;
		tsuukinteiki.zeinukiKingaku = super.toDecimal(this.zeinukiKingaku);
		tsuukinteiki.shouhizeigaku = super.toDecimal(this.shouhizeigaku);
		tsuukinteiki.shiharaisakiName = this.shiharaisakiName;
		tsuukinteiki.jigyoushaKbn = this.jigyoushaKbn;
		tsuukinteiki.bunriKbn = this.bunriKbn;
		tsuukinteiki.kariShiireKbn = this.kariShiireKbn;
		tsuukinteiki.kashiShiireKbn = this.kashiShiireKbn;
		tsuukinteiki.invoiceDenpyou = this.invoiceDenpyou;
		return tsuukinteiki;
	}
}
