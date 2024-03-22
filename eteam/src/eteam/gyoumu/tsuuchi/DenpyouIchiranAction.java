package eteam.gyoumu.tsuuchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamConst.UserId;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.EteamFileLogic;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KANREN_DENPYOU;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamUserSettingLogic;
import eteam.common.EteamUserSettingLogic.UserDefaultValueKbn;
import eteam.common.RegAccess;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.ShiharaiIraiAction;
import eteam.gyoumu.tsuuchi.DenpyouIchiranItemLogic.LIST_ITEM_CONTROL_KBN;
import eteam.gyoumu.tsuuchi.DenpyouIchiranLogic.KeijouShiharai;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControl;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic.WfSeigyo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧画面Action
 */
@Getter @Setter @ToString
public class DenpyouIchiranAction extends EteamAbstractAction {

//＜定数＞
	/** 関連伝票ID（コード一覧より）*/
	static final String KANREN_DENPYOU_ID = "kanren_denpyou";
	/** 伝票状態ID（コード一覧より）*/
	static final String DENPYOU_JOUTAI_ID = "denpyou_jyoutai";
	/** 証憑書類ID（コード一覧より）*/
	static final String SHOUHYOU_SYORUI_ID = "shouhyou_shorui";
	/** 起案番号入力（コード一覧より）*/
	static final String KIANBANGOU_INPUT = "kian_bangou_input";
	/** 起案番号終了（コード一覧より）*/
	static final String KIANBANGOU_END = "kian_bangou_end";
	/** 起案番号運用（コード一覧より）*/
	static final String KIANBANGOU_UNYOU = "kian_bangou_unyou";
	/** 起案番号あり且つ支出依頼なし分抽出（コード一覧より）*/
	static final String KIANBANGOU_NO_SHISHUTSUIRAI = "kian_bangou_no_shishutsuirai";
	/** 予算執行対象（コード一覧より）*/
	static final String YOSAN_SHIKKOU_TAISHOU = "yosan_shikkou_taishou";
	/** e文書種別（コード一覧より）*/
	static final String EBUNSHO_SHUBETSU = "ebunsho_shubetsu";
	/** CSVファイル名 */
	static final String CSV_FILENAME = "denpyouIchiran.csv";
	/** 起案一覧CSVファイル名 */
	static final String KIAN_CSV_FILENAME = "kianIchiran.csv";
	/** ZIPファイル名 */
	static final String ZIP_FILENAME = "attachedFile.zip";
	/** 仕訳抽出状況ドメイン */
	static final String[] SHIWAKE_STATUS_DOMAIN = {"N", "0", "1"};
	/** FBデータ作成状況ドメイン */
	static final String[] FB_STATUS_DOMAIN = {"N", "1"};
	/** 仮払伝票URL(仮払) */
	static final String KARIBARAI_URL = "karibarai_shinsei";
	/** 仮払伝票URL(出張旅費仮払) */
	static final String RYOHI_KARIBARAI_URL = "ryohi_karibarai_shinsei";
	/** 仮払伝票URL(海外出張旅費仮払) */
	static final String KAIGAI_RYOHI_KARIBARAI_URL = "kaigai_ryohi_karibarai_shinsei";

//＜画面入力＞
	/** 詳細検索表示フラグ */
	String shousaiKensakuHyoujiFlg="0";
	/** 検索条件表示フラグ */
	String kensakuJoukenHyoujiFlg="1";
	/** 関連伝票 */
	String kanrenDenpyou="";
	/** 伝票ID */
	String kensakuDenpyouId="";
	/** シリアル番号 */
	String kensakuSerialNo="";
	/** 状態 */
	String kensakuJoutai="";
	/** 業務種別 */
	String kensakuGyoumuShubetsu="";
	/** 伝票種別 */
	String kensakuDenpyouShubetsu="";
	/** 伝票区分絞り込み */
	String dpkbSibori="";
	/** 起案番号年度 */
	String kianBangouNendo="";
	/** 起案番号略号 */
	String kianBangouRyakugou="";
	/** 起案番号 */
	String kianBangou="";
	/** 起案番号略号From */
	String kianBangouRyakugouFromTo="";
	/** 起案番号From */
	String kianBangouFrom="";
	/** 起案番号To */
	String kianBangouTo="";
	/** 起案番号入力 */
	String kianBangouInput="";
	/** 起案番号終了 */
	String kianBangouEnd="";
	/** 起案番号運用 */
	String kianBangouUnyou="";
	/** 起案番号あり且つ支出依頼なし分抽出 */
	String kianBangouNoShishutsuIrai="";
	/** 予算執行伝票種別 */
	String[] yosanshikkouShubetsu={};
	/** 予算執行対象月From */
	String yosanCheckNengetsuFrom = "";
	/** 予算執行対象月To */
	String yosanCheckNengetsuTo = "";
	/** 起票日付From */
	String kihyouBiFrom="";
	/** 起票日付To */
	String kihyouBiTo="";
	/** 起票者所属部門コード */
	String kensakuKihyouShozokuBumonCd="";
	/** 起票者所属部門名 */
	String kensakuKihyouShozokuBumonName="";
	/** 起票者社員番号 */
	String kensakuKihyouShozokuUserShainNo="";
	/** 起票者名 */
	String kensakuKihyouShozokuUserName="";
	/** 承認日付From */
	String shouninBiFrom="";
	/** 承認日付To */
	String shouninBiTo="";
	/** 承認者所属部門コード */
	String shouninsyaShozokuBumonCd="";
	/** 承認者所属部門名 */
	String shouninsyaShozokuBumonName="";
	/** 承認者社員番号 */
	String shouninsyaShozokuUserShainNo="";
	/** 承認者名 */
	String shouninsyaShozokuUserName="";
	/** 所有者所属部門コード */
	String shoyuushaShozokuBumonCd="";
	/** 所有者所属部門名 */
	String shoyuushaShozokuBumonName="";
	/** 所有者社員番号 */
	String shoyuushaShozokuUserShainNo="";
	/** 所有者名 */
	String shoyuushaShozokuUserName="";
	/** 摘要 */
	String tekiyou="";
	/** 金額From */
	String kingakuFrom="";
	/** 金額To */
	String kingakuTo="";
	/** 明細金額From */
	String meisaiKingakuFrom="";
	/** 明細金額To */
	String meisaiKingakuTo="";
	/** クエリ用税率 */
	String queryZeiritsu="";
	/** 軽減税率区分 */
	String keigenZeiritsuKbn="";
	/** 法人カード利用 */
	String houjinCardRiyou = "";
	/** 会社手配 */
	String kaishaTehai = "";
	/** マル秘 */
	String maruhi = "";
	/** 支払日From */
	String shiharaiBiFrom="";
	/** 支払日To */
	String shiharaiBiTo="";
	/** 支払希望日From */
	String shiharaiKiboubiFrom="";
	/** 支払希望日To */
	String shiharaiKiboubiTo="";
	/** 計上日From */
	String keijoubiFrom="";
	/** 計上日To */
	String keijoubiTo="";
	/** 仕訳計上日From */
	String shiwakeKeijoubiFrom="";
	/** 仕訳計上日To */
	String shiwakeKeijoubiTo="";
	/** 借方部門コードFrom */
	String karikataBumonCdFrom="";
	/** 借方部門名From */
	String karikataBumonNameFrom="";
	/** 借方部門コードTo */
	String karikataBumonCdTo="";
	/** 借方部門名To */
	String karikataBumonNameTo="";
	/** 貸方部門コードFrom */
	String kashikataBumonCdFrom="";
	/** 貸方部門名From */
	String kashikataBumonNameFrom="";
	/** 貸方部門コードTo */
	String kashikataBumonCdTo="";
	/** 貸方部門名To */
	String kashikataBumonNameTo="";
	/** 借方科目コードFrom */
	String karikataKamokuCdFrom="";
	/** 借方科目名From */
	String karikataKamokuNameFrom="";
	/** 借方科目コードTo */
	String karikataKamokuCdTo="";
	/** 借方科目名To */
	String karikataKamokuNameTo="";
	/** 貸方科目コードFrom */
	String kashikataKamokuCdFrom="";
	/** 貸方科目名From */
	String kashikataKamokuNameFrom="";
	/** 貸方科目コードTo */
	String kashikataKamokuCdTo="";
	/** 貸方科目名To */
	String kashikataKamokuNameTo="";
	/** 借方科目枝番コードFrom */
	String karikataKamokuEdanoCdFrom="";
	/** 借方科目枝番名From */
	String karikataKamokuEdanoNameFrom="";
	/** 借方科目枝番コードTo */
	String karikataKamokuEdanoCdTo="";
	/** 借方科目枝番名To */
	String karikataKamokuEdanoNameTo="";
	/** 貸方科目枝番コードFrom */
	String kashikataKamokuEdanoCdFrom="";
	/** 貸方科目枝番名From */
	String kashikataKamokuEdanoNameFrom="";
	/** 貸方科目枝番コードTo */
	String kashikataKamokuEdanoCdTo="";
	/** 貸方科目枝番名To */
	String kashikataKamokuEdanoNameTo="";
	/** 借方取引先コードFrom */
	String karikataTorihikisakiCdFrom="";
	/** 借方取引先名From */
	String karikataTorihikisakiNameFrom="";
	/** 借方取引先コードTo */
	String karikataTorihikisakiCdTo="";
	/** 借方取引先名To */
	String karikataTorihikisakiNameTo="";
	/** 貸方取引先コードFrom */
	String kashikataTorihikisakiCdFrom="";
	/** 貸方取引先名From */
	String kashikataTorihikisakiNameFrom="";
	/** 貸方取引先コードTo */
	String kashikataTorihikisakiCdTo="";
	/** 貸方取引先名To */
	String kashikataTorihikisakiNameTo="";
	/** 領収書・請求書等 */
	String ryoushuushoSeikyuushoTou="";
	/** 未精算仮払 */
	String miseisanKaribarai = "";
	/** 未精算伺い */
	String miseisanUkagai = "";
	/** 添付ファイルフラグ */
	String tenpuFileFlg = "";
	/** 添付ファイル種別 */
	String[] tenpuFileShubetsu = {"","","",""};
	/** e文書種別 */
	String ebunshoShubetsu = "";
	/** e文書年月日From */
	String ebunshoNengappiFrom = "";
	/** e文書年月日To */
	String ebunshoNengappiTo = "";
	/** e文書年月日未入力フラグ */
	String ebunshoNengappiMinyuuryokuFlg = "";
	/** e文書金額From */
	String ebunshoKingakuFrom = "";
	/** e文書金額To */
	String ebunshoKingakuTo = "";
	/** e文書金額未入力フラグ */
	String ebunshoKingakuMinyuuryokuFlg = "";
	/** e文書発行者 */
	String ebunshoHakkousha = "";
	/** e文書発行者未入力フラグ */
	String ebunshoHakkoushaMinyuuryokuFlg = "";
	/** 部門From */
	String bumonCdFrom = "";
	/** 部門名称From */
	String bumonNameFrom = "";
	/** 部門To */
	String bumonCdTo = "";
	/** 部門名称To */
	String bumonNameTo = "";
	/** ユーザー定義届書の件名 */
	String kanitodokeKenmei = "";
	/** ユーザー定義届書の内容 */
	String kanitodokeNaiyou = "";
	/** 仕訳抽出状況 */
	String shiwakeStatus="";
	/** FBデータ作成状況 */
	String fbStatus="";
	/** 集計部門選択フラグ(0:部門 1:集計部門) */
	String bumonSentakuFlag="0";

	/** 前回検索条件 */
	String beforeSelect;
	/** 承認チェックボックス */
	String sentaku;
	/** ソート区分 */
	String sortKbn ="0";
	/** ページ番号 */
	String pageNo = "1";

	/** 支払日 */
	String updateShiharaibi;
	/** 計上日 */
	String updateKeijoubi;

	/** 検索結果の合計金額 */
	String kensakuGoukeiKingaku="";

	/** 一括起案終了処理種別（0:起案終了解除 1:起案終了）*/
	String kianSyuryoType;
	
	/** 税抜金額From */
	String zeinukiKingakuFrom = "";
	/** 税抜金額To */
	String zeinukiKingakuTo = "";
	/** 税抜明細金額From */
	String zeinukiMeisaiKingakuFrom = "";
	/** 税抜明細金額To */
	String zeinukiMeisaiKingakuTo = "";
	/** 事業者区分 */
	String jigyoushaKbn = "";
	/** 支払先名 */
	String shiharaiName = "";

//＜画面入力以外＞
	//ドロップダウン等
	/** 関連伝票のDropDownList */
	List<GMap> kanrenDenpyouList;
	/** 伝票状態のDropDownList */
	List<GMap> denpyouJoutaiList;
	/** 伝票種別のDropDownList */
	List<GMap> denpyouShubetsuList;
	/** 業務種別のDropDownList */
	List<GMap> gyoumuShubetsuList;
	/** 証憑書類のDropDownList */
	List<GMap> shouhyouShoruiList;
	/** 起案番号入力 */
	List<GMap> kianBangouInputList;
	/** 起案番号終了 */
	List<GMap> kianBangouEndList;
	/** 起案番号運用 */
	List<GMap> kianBangouUnyouList;
	/** 起案番号あり且つ支出依頼なし分抽出 */
	List<GMap> kianBangouNoShishutsuIraiList;
	/** 予算執行伝票種別 */
	List<GMap> yosanshikkouShubetsuList;
	/** 予算執行対象月 */
	List<GMap> yosanCheckNengetsuList;
	/** e文書種別 */
	List<GMap>ebunshoShubetsuList;
	/** 消費税率リスト */
	List<GMap> queryZeiritsuList;
	/** 事業者区分リスト */
	List<GMap> jigyoushaKbnList;
	/** 起案番号入力ドメイン */
	String[] kianBangouInputDomain;
	/** 起案番号終了ドメイン */
	String[] kianBangouEndDomain;
	/** 起案番号運用ドメイン */
	String[] kianBangouUnyouDomain;
	/** 予算執行伝票種別ドメイン */
	String[] yosanshikkouShubetsuDomain;
	/** 予算執行対象月ドメイン */
	String[] yosanCheckNengetsuDomain;
	/** 消費税率ドメイン */
	String[] queryZeiritsuDomain;
	/** e文書種別ドメイン */
	String[]ebunshoShubetsuDomain;
	/** 事業者区分ドメイン */
	String[]jigyoushaKbnDomain;

	//表示
	/** 検索結果リスト */
	List<GMap> list;
	/** ファイル添付機能使用フラグ */
	boolean fileTenpuFlg;
	/** 起票可能 */
	boolean kihyouFlg = false;
	/** 支払日一括登録フラグ */
	boolean shiharaibiTourokuFlg;
	/** 表示項目リスト */
	List<DenpyouDisplayItem> itemList;
	/** 法人カード利用表示制御 */
	boolean houjinCardFlag;
	/** 会社手配表示制御 */
	boolean kaishaTehaiFlag;
	/** マル秘フラグ表示制御 */
	boolean maruhiFlag;
	/** 予算管理項目の表示制御 */
	boolean yosanKannriFlg;
	/** 予算執行対象月の表示制御 */
	boolean yosanCheckNengetsuFlg;
	/** e文書表示制御 */
	boolean ebunshoHyoujiFlg;
	/** 一括起案終了・起案終了解除の表示制御 */
	boolean ikkatsuKianSyuryoFlg;

	/** フォーカス時画面自動更新フラグ制御 */
	String jidouKoushinFlg;
	/** 取下済・否認済　表示 */
	String torisageHininHyoujiFlg = "1";
	
	/** インボイス開始処理フラグ */
	boolean invoiceStart;

	//ページング
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** ページング処理 link押下時のURL */
	String pagingLink;

	//ダウンロード用の
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** ダウンロードファイル用 */
	protected InputStream inputStream;

	//部品
	/** 通知ロジック */
	protected TsuuchiCategoryLogic tsuuchiLogic;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysLogic;
	/** 会計共通ロジック */
	protected KaikeiCommonLogic commonLc;
	/** ワークフローロジック */
	WorkflowCategoryLogic wfLogic;
	/** 伝票一覧表示ロジック */
	protected DenpyouIchiranItemLogic denpyouItemLogic;
	/** 伝票一覧ロジック */
	protected DenpyouIchiranLogic denpyouIchiranLg;
	/** WFイベントロジック */
	protected WorkflowEventControlLogic wfEventLg;
	/** 伝票一覧テーブルロジック */
	protected DenpyouIchiranUpdateLogic diLogic;
	/** マスターSELECT */
	protected MasterKanriCategoryLogic masterLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(DenpyouIchiranAction.class);
	
	/** カスタマイズ用Strategyクラス */
	protected AbstractDenpyouIchiranActionStrategy denpyouIchiranActionStrategy;
	
	//＜入力チェック＞
	@Override protected void formatCheck() {
		checkDomain(shousaiKensakuHyoujiFlg, EteamConst.Domain.FLG, "詳細検索表示フラグ", false);
		checkDomain(kensakuJoukenHyoujiFlg, EteamConst.Domain.FLG, "検索条件表示フラグ", false);
		checkString(kensakuDenpyouId,19,19,"伝票ID",false);
		checkNumber(kensakuSerialNo,1,8,"伝票番号",false);
		if(yosanKannriFlg){
			checkNumber(kianBangouNendo,4,4,"起案番号年度",false);
			checkString(kianBangouRyakugou,1,7,"略号",false);
			checkNumber(kianBangou,1,EteamConst.kianBangou.MAX_LENGTH,"起案番号",false);
			checkString(kianBangouRyakugouFromTo,1,7,"起案番号範囲指定 略号",false);
			checkNumber(kianBangouFrom,1,EteamConst.kianBangou.MAX_LENGTH,"起案番号範囲指定 起案番号From",false);
			checkNumber(kianBangouTo,1,EteamConst.kianBangou.MAX_LENGTH,"起案番号範囲指定 起案番号To",false);
			checkDomain(kianBangouInput,kianBangouInputDomain,"起案番号入力",false);
			checkDomain(kianBangouEnd,kianBangouEndDomain,"起案番号終了",false);
			checkDomain(kianBangouUnyou,kianBangouUnyouDomain,"起案番号運用",false);
			checkCheckbox(kianBangouNoShishutsuIrai,"起案番号あり且つ支出依頼なし分抽出",false);
			for(int i = 0; i < yosanshikkouShubetsu.length ; i++){
				checkDomain(yosanshikkouShubetsu[i],yosanshikkouShubetsuDomain,"予算執行伝票種別",false);
			}
			if(yosanCheckNengetsuFlg) {
				checkDomain(yosanCheckNengetsuFrom,yosanCheckNengetsuDomain,"予算執行対象月From",false);
				checkDomain(yosanCheckNengetsuTo  ,yosanCheckNengetsuDomain,"予算執行対象月To"  ,false);
			}
		}
		checkDate(kihyouBiFrom,  "起票日付From", false);
		checkDate(kihyouBiTo,  "起票日付To", false);
		checkString(kensakuKihyouShozokuBumonName,1,20,"起票者所属部門名",false);
		checkString(kensakuKihyouShozokuUserShainNo,1,15,"起票者社員番号",false);
		checkString(kensakuKihyouShozokuUserName,1,21,"起票者名",false);
		checkDate(shouninBiFrom,  "承認日付From", false);
		checkDate(shouninBiTo,  "承認日付To", false);
		checkString(shouninsyaShozokuBumonName,1,20,"承認者所属部門名",false);
		checkString(shouninsyaShozokuUserShainNo,1,15,"承認者社員番号",false);
		checkString(shouninsyaShozokuUserName,1,21,"承認者名",false);
		checkString(shoyuushaShozokuBumonName,1,20,"所有者所属部門名",false);
		checkString(shoyuushaShozokuUserShainNo,1,15,"所有者社員番号",false);
		checkString(shoyuushaShozokuUserName,1,21,"所有者名",false);
		checkString(tekiyou,1,60,"摘要",false);
		checkKingaku(kingakuFrom, 1L, 999999999999L, "金額From", false);
		checkKingaku(kingakuTo, 1L, 999999999999L, "金額To", false);
		checkKingaku(meisaiKingakuFrom, 1L, 999999999999L, "明細金額From", false);
		checkKingaku(meisaiKingakuTo, 1L, 999999999999L, "明細金額To", false);
		checkCheckbox(houjinCardRiyou, "法人カード利用", false);
		checkCheckbox(kaishaTehai, "会社手配", false);
		checkCheckbox(maruhi, "マル秘", false);
		checkDomain(queryZeiritsu, queryZeiritsuDomain, "消費税率", false);
		checkDomain(keigenZeiritsuKbn, Domain.FLG, "軽減税率区分", false);
		checkDate(shiharaiBiFrom,  "支払日From", false);
		checkDate(shiharaiBiTo,  "支払日To", false);
		checkDate(shiharaiKiboubiFrom,  "支払希望日From", false);
		checkDate(shiharaiKiboubiTo,  "支払希望日To", false);
		checkDate(keijoubiFrom,  "使用日From", false);
		checkDate(keijoubiTo,  "使用日To", false);
		checkDate(shiwakeKeijoubiFrom,  "計上日From", false);
		checkDate(shiwakeKeijoubiTo,  "計上日To", false);
		checkString(karikataBumonCdFrom,1,8,"借方部門コードFrom",false);
		checkString(karikataBumonNameFrom,1,20,"借方部門名From",false);
		checkString(karikataBumonCdTo,1,8,"借方部門コードTo",false);
		checkString(karikataBumonNameTo,1,20,"借方部門名To",false);
		checkString(kashikataBumonCdFrom,1,8,"貸方部門コードFrom",false);
		checkString(kashikataBumonNameFrom,1,20,"貸方部門名From",false);
		checkString(kashikataBumonCdTo,1,8,"貸方部門コードTo",false);
		checkString(kashikataBumonNameTo,1,20,"貸方部門名To",false);
		checkString(karikataKamokuCdFrom,1,6,"借方科目コードFrom",false);
		checkString(karikataKamokuNameFrom,1,22,"借方科目名From",false);
		checkString(karikataKamokuCdTo,1,6,"借方科目コードTo",false);
		checkString(karikataKamokuNameTo,1,22,"借方科目名To",false);
		checkString(kashikataKamokuCdFrom,1,6,"貸方科目コードFrom",false);
		checkString(kashikataKamokuNameFrom,1,22,"貸方科目名From",false);
		checkString(kashikataKamokuCdTo,1,6,"貸方科目コードTo",false);
		checkString(kashikataKamokuNameTo,1,22,"貸方科目名To",false);
		checkString(karikataKamokuEdanoCdFrom,1,12,"借方科目枝番コードFrom",false);
		checkString(karikataKamokuEdanoNameFrom,1,20,"借方科目枝番名From",false);
		checkString(karikataKamokuEdanoCdTo,1,12,"借方科目枝番コードTo",false);
		checkString(karikataKamokuEdanoNameTo,1,20,"借方科目枝番名To",false);
		checkString(kashikataKamokuEdanoCdFrom,1,12,"貸方科目枝番コードFrom",false);
		checkString(kashikataKamokuEdanoNameFrom,1,20,"貸方科目枝番名From",false);
		checkString(kashikataKamokuEdanoCdTo,1,12,"貸方科目枝番コードTo",false);
		checkString(kashikataKamokuEdanoNameTo,1,20,"貸方科目枝番名To",false);
		checkString(karikataTorihikisakiCdFrom,1,12,"借方取引先コードFrom",false);
		checkString(karikataTorihikisakiNameFrom,1,44,"借方取引先名From",false);
		checkString(karikataTorihikisakiCdTo,1,12,"借方取引先コードTo",false);
		checkString(karikataTorihikisakiNameTo,1,44,"借方取引先名To",false);
		checkString(kashikataTorihikisakiCdFrom,1,12,"貸方取引先コードFrom",false);
		checkString(kashikataTorihikisakiNameFrom,1,44,"貸方取引先名From",false);
		checkString(kashikataTorihikisakiCdTo,1,12,"貸方取引先コードTo",false);
		checkString(kashikataTorihikisakiNameTo,1,44,"貸方取引先名To",false);
		checkDomain(ryoushuushoSeikyuushoTou, Domain.FLG, "領収書・請求書等", false);
		checkCheckbox(miseisanKaribarai, "未精算伺い・仮払(仮払あり)", false);
		checkCheckbox(miseisanUkagai, "未精算伺い・仮払(仮払なし)", false);
		checkDomain(this.tenpuFileFlg, Domain.FLG, "添付ファイル", false);
		// 配列コントロールはvalue設定で調整する必要があるのでいったんチェックを外す
// for(int i = 0; i < this.tenpuFileShubetsu.length ; i++){
// checkDomain(this.tenpuFileShubetsu[i],Domain.FLG,"添付ファイル種別",false);
// }
		checkDomain(this.ebunshoShubetsu, this.ebunshoShubetsuDomain, "書類種別", false);
		checkDate(this.ebunshoNengappiFrom,  "e文書年月日From", false);
		checkDate(this.ebunshoNengappiTo,  "e文書年月日To", false);
		checkCheckbox(this.ebunshoNengappiMinyuuryokuFlg, "e文書年月日未入力フラグ", false);
		checkKingaku(this.ebunshoKingakuFrom, 1L, 999999999999L, "e文書金額From", false);
		checkKingaku(this.ebunshoKingakuTo, 1L, 999999999999L, "e文書金額To", false);
		checkCheckbox(this.ebunshoKingakuMinyuuryokuFlg, "e文書金額未入力フラグ", false);
		checkString(this.ebunshoHakkousha,1,20,"e文書発行者",false);
		checkCheckbox(this.ebunshoHakkoushaMinyuuryokuFlg, "e文書発行者未入力フラグ", false);
		checkString(bumonCdFrom,1,8,"部門コードFrom",false);
		checkString(bumonCdTo,1,8,"部門コードTo",false);
		checkString(kanitodokeKenmei,1,20,"ユーザー定義届書の件名",false);
		checkString(kanitodokeNaiyou,1,20,"ユーザー定義届書の内容",false);
		checkDomain(shiwakeStatus, SHIWAKE_STATUS_DOMAIN, "仕訳抽出状況", false);
		checkDomain(fbStatus, FB_STATUS_DOMAIN, "FBデータ作成状況", false);
		checkDomain(bumonSentakuFlag, EteamConst.Domain.FLG, "集計部門選択フラグ", false);
		
		checkKingaku(zeinukiKingakuFrom, 1L, 999999999999L, "税抜金額From", false);
		checkKingaku(zeinukiKingakuTo, 1L, 999999999999L, "税抜金額To", false);
		checkKingaku(zeinukiMeisaiKingakuFrom, 1L, 999999999999L, "税抜明細金額From", false);
		checkKingaku(zeinukiMeisaiKingakuTo, 1L, 999999999999L, "税抜明細金額To", false);
		checkDomain(jigyoushaKbn, this.jigyoushaKbnDomain, "事業者区分", false);
		checkString(shiharaiName,1,60,"支払先名",false);

		checkNumber(pageNo, 0, 10, "pageNo", true);
	}

	@Override protected void hissuCheck(int eventNum) {}

	/**
	 * 相関チェック<br>
	 */
	protected void soukanCheck(){
		if(yosanKannriFlg){
			// 起案番号 範囲指定
			if (super.isNotEmpty(kianBangouFrom) && super.isNotEmpty(kianBangouTo)){
				// 起案番号のFROMとTOの両方に入力ある場合、大小チェックを行う。
				if (Integer.valueOf(kianBangouFrom) > Integer.valueOf(kianBangouTo)){
					errorList.add("起案番号 範囲指定の起案番号FROM-TOは FROM <= TO の形式で入力してください。");
				}
			}
		}
	}

//＜初期化＞
	/**
	 * DB接続を初期化する。
	 * @param connection コネクション
	 */
	protected void initConnection(EteamConnection connection){

		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		commonLc = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		denpyouIchiranLg = EteamContainer.getComponent(DenpyouIchiranLogic.class, connection);
		wfEventLg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		this.masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		// 項目リスト
		denpyouItemLogic = EteamContainer.getComponent(DenpyouIchiranItemLogic.class, connection);

		diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
	}

	/**
	 * 画面パーツを作成する。
	 */
	protected void makeParts(){
		// 関連伝票
		kanrenDenpyouList = sysLogic.loadNaibuCdSetting(KANREN_DENPYOU_ID);
		for (GMap kanren : kanrenDenpyouList) {
			// 「下位未承認伝票のみ」は上位承認しない設定なら表示しない
			if(KANREN_DENPYOU.KAI_MISHOUNIN.equals(kanren.get("naibu_cd").toString()) && ! sysLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.JOUI_SENKETSU_SHOUNIN)) {
				kanrenDenpyouList.remove(kanren);
				break;
			}
		}
		// 伝票状態
		denpyouJoutaiList = sysLogic.loadNaibuCdSetting(DENPYOU_JOUTAI_ID);
		for (GMap joutai : denpyouJoutaiList) {
			// 「未起票」は表示しない
			if(DENPYOU_JYOUTAI.MI_KIHYOU.equals(joutai.get("naibu_cd").toString())) {
				denpyouJoutaiList.remove(joutai);
				break;
			}
		}
		GMap kihyouShinsei = new GMap(); kihyouShinsei.put("naibu_cd", "10_20"); kihyouShinsei.put("name", "起票中 or 申請中");
		denpyouJoutaiList.add(2, kihyouShinsei);
		// 証憑書類
		shouhyouShoruiList = sysLogic.loadNaibuCdSetting(SHOUHYOU_SYORUI_ID);
		// 伝票種別
		denpyouShubetsuList = tsuuchiLogic.loadDenpyouShubetsu();
		// 業務種別
		gyoumuShubetsuList = tsuuchiLogic.loadGyoumuShubetsu();
		// 起案番号入力
		kianBangouInputList = sysLogic.loadNaibuCdSetting(KIANBANGOU_INPUT);
		kianBangouInputDomain = EteamCommon.mapList2Arr(kianBangouInputList, "naibu_cd");
		// 起案番号終了
		kianBangouEndList = sysLogic.loadNaibuCdSetting(KIANBANGOU_END);
		kianBangouEndDomain = EteamCommon.mapList2Arr(kianBangouEndList, "naibu_cd");
		// 起案番号運用
		kianBangouUnyouList = sysLogic.loadNaibuCdSetting(KIANBANGOU_UNYOU);
		kianBangouUnyouDomain = EteamCommon.mapList2Arr(kianBangouUnyouList, "naibu_cd");
		// 予算執行伝票種別
		yosanshikkouShubetsuList = sysLogic.loadNaibuCdSetting(YOSAN_SHIKKOU_TAISHOU);
		yosanshikkouShubetsuDomain = EteamCommon.mapList2Arr(yosanshikkouShubetsuList, "naibu_cd");
		// 予算執行対象月
		yosanCheckNengetsuList = wfEventLg.createKiKesnNengetsuList();
		yosanCheckNengetsuDomain = EteamCommon.mapList2Arr(yosanCheckNengetsuList, "key");
		// 消費税率
		queryZeiritsuList = masterLogic.loadshouhizeiritsu();
		queryZeiritsuDomain = EteamCommon.mapList2Arr(queryZeiritsuList, "zeiritsu");
		//事業者区分
		jigyoushaKbnList = sysLogic.loadNaibuCdSetting("jigyousha_kbn");
		jigyoushaKbnList.get(0).put("name","通常");
		jigyoushaKbnList.get(1).put("name","免税"); //内部コード登録nameと伝票一覧画面での表示文字が違うため
		jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");
		// e文書種別
		ebunshoShubetsuList = sysLogic.loadNaibuCdSetting(EBUNSHO_SHUBETSU);
		ebunshoShubetsuDomain = EteamCommon.mapList2Arr(ebunshoShubetsuList, "naibu_cd");
		// ファイル添付使用フラグ
		fileTenpuFlg = EteamCommon.funcControlJudgment(sysLogic, KINOU_SEIGYO_CD.FILE_TENPU);
		// 支払日一括登録フラグ
		shiharaibiTourokuFlg = commonLc.userIsKeiri(getUser());
		// 表示項目取得
		itemList = denpyouItemLogic.getDisplayItemList(LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN,  getUser().getLoginUserId(), "");
		// 法人カードの表示可否
		houjinCardFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		// 会社手配の表示可否
		kaishaTehaiFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		// マル秘フラグの表示可否
		maruhiFlag = getUser().isMaruhiKengenFlg();
		// 予算管理項目の表示制御
		yosanKannriFlg = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption());
		// 予算執行対象月の表示制御
		yosanCheckNengetsuFlg = yosanCheckKikan.TO_TAISHOUZUKI.equals(setting.yosanCheckKikan());
		// e文書表示フラグ
		this.ebunshoHyoujiFlg = this.wfEventLg.isUseEbunsho("search");
		// 一括起案終了・起案終了解除の表示制御
		ikkatsuKianSyuryoFlg = yosanKannriFlg && ( UserId.ADMIN.equals(getUser().getSeigyoUserId()) || commonLc.userIsKeiri(getUser()) );
		//インボイス開始処理
		invoiceStart = "1".equals(wfEventLg.judgeinvoiceStart());

		//再処理用のURLを作る
		String urlParam = makeUrlParam();
		beforeSelect= "pageNo=" + pageNo + "&sortKbn=" + sortKbn + urlParam;
	}

//＜イベント＞
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		//メニューの件数リンクからとんだ時に予算関連の入力を無効化するように
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request.getParameter("kensakuDenpyouId") == null){
			kianBangouInput = "2";
			kianBangouEnd = "2";
			kianBangouUnyou = "";
			kianBangouNoShishutsuIrai = "";
		}
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			//user_default_valueから該当ユーザー設定値呼び出し
			EteamUserSettingLogic userSettingLogic = EteamContainer.getComponent(EteamUserSettingLogic.class, connection);
			GMap koushinMap = userSettingLogic.selectUserDefaultValue(UserDefaultValueKbn.ichiranReloadKbn, getUser().getLoginUserId());
			jidouKoushinFlg =  (null != koushinMap) ? koushinMap.getString("default_value") : "";
			GMap torisageMap = userSettingLogic.selectUserDefaultValue(UserDefaultValueKbn.torisageHininHyoujiKbn, getUser().getLoginUserId());
			this.torisageHininHyoujiFlg=  (null != torisageMap) ? torisageMap.getString("default_value") : "1";

			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(! errorList.isEmpty()){
				return "error";
			}
			this.soukanCheck();
			if(! errorList.isEmpty()){
				return "error";
			}

			// 2.データ存在チェック

			// 3.アクセス可能チェック なし
			// 4.処理

			// クエリパラメーターの作成
			var queryParameter = this.createQueryParameter();

			// データの全件数取得
			totalCount = tsuuchiLogic.findDenpyouIchiranKensakuCount(queryParameter);

			// 1ページ最大表示件数を取得
			int pagemax = Integer.parseInt(setting.recordNumPerPage());

			// 総ページ数の計算
			totalPage = EteamCommon.calcTotalPageNum(pagemax, totalCount);
			if(totalPage == 0){
				totalPage = 1;
			}

			// 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
			if(isEmpty(pageNo) || "0".equals(pageNo)){
				pageNo = "1";
			} else if(Integer.parseInt(pageNo) > totalPage) {
				pageNo = String.valueOf(totalPage);
			}

			// ページングリンクURLを設定
			String urlParam = makeUrlParam();
			pagingLink = "denpyou_ichiran_kensaku?sortKbn="+sortKbn+ urlParam+"&";

			list = tsuuchiLogic.loadDenpyouIchiranKensaku(
					queryParameter
					, Integer.parseInt(pageNo)
					, pagemax
					, sortKbn
					); // ページやソートは検索本体固有なので分けておく

			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 検索結果全件のユーザー定義届書を除く合計金額を取得
			BigDecimal bigKensaku = tsuuchiLogic.findDenpyouIchiranKensakuGoukeiKingaku(queryParameter);
			kensakuGoukeiKingaku=formatMoney(bigKensaku);

			String[] kanikoumokuItemname = {"","",""};
			//フォーマット
			formatList4JSP(kanikoumokuItemname);

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 承認イベント
	 * @return 処理結果
	 */
	public String shounin(){
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			hissuCheck(3);
			if(sentaku==null || sentaku.isEmpty()){
				errorList.add("伝票が選択されていません。");
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理

			//--------------------
			//引数のカンマ区切り伝票番号を分解
			//--------------------
			List<String[]> denpyouList = makeDList();

			//--------------------
			//伝票１件ずつ承認チェック
			//--------------------
			List<WorkflowEventControl> wfActionList = new ArrayList<>();
			for (String[] d : denpyouList) {
				String denpyouId = d[0];
				String denpyouKbn = d[1];
				WorkflowEventControl wfAction = denpyouIchiranLg.makeWfAction(denpyouId, denpyouKbn);
				wfActionList.add(wfAction);
				try {
					wfAction.setUser((User)session.get(Sessionkey.USER));
					wfAction.init();
					wfAction.shouninCheck(connection);
					//通常承認で画面上部に出るエラーメッセージ返してたらそれを伝票一覧でも出す
					for (String msg : wfAction.getErrorList()) {
						errorList.add("伝票ID[" + denpyouId + "]：" + msg);
					}
				} catch(EteamAccessDeniedException e) {
					//通常承認で例外出すようなら（アクセス制御）
					log.warn("承認チェックでエラー", e);
					errorList.add("伝票ID[" + denpyouId + "]：承認できる状態ではありません。");
				} catch(RuntimeException e) {
					//通常承認で例外出すようなら（システムエラー？）
					log.warn("承認チェックでエラー", e);
					errorList.add("伝票ID[" + denpyouId + "]：承認できない伝票です。");
				}
			}
			//承認チェックで１件でもエラーあるなら終わり
			if(! errorList.isEmpty()) {
				connection.rollback();
				reKensaku();
				return "error";
			}

			//--------------------
			//全部承認できそうなので承認実行
			//--------------------
			for (WorkflowEventControl wfAction : wfActionList) {
				try {
					wfAction.shouninExecute();
				} catch(Exception e) {
					log.warn("承認実行でエラー", e);
					errorList.add("伝票ID[" + wfAction.getDenpyouId() + "]：承認できない伝票です。");
				}
			}
			//承認実行で１件でもエラーあるなら終わり
			if(! errorList.isEmpty()) {
				connection.rollback();
				reKensaku();
				return "error";
			}
			//伝票一覧テーブル状態更新
			for (WorkflowEventControl wfAction : wfActionList) {
				diLogic.updateDenpyouIchiran(wfAction.getDenpyouId());
			}

			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * CSV出力イベント
	 * @return 処理結果
	 */
	public String csvOutput(){
		PrintWriter writer = null;
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			hissuCheck(4);
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}
			this.soukanCheck();
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理
			// クエリパラメーターの作成
			var queryParameter = this.createQueryParameter();
			
			// データの全件数取得
			totalCount = tsuuchiLogic.findDenpyouIchiranKensakuCount(queryParameter);
			if(totalCount == 0) {
				errorList.add("検索結果が0件でした。");
				reKensaku();
				return "error";
			}

			int tmpReadSize = EteamConnection.DB_FETCH_SIZE;
			int tmpMaxPage = (int)(totalCount / tmpReadSize + (totalCount % tmpReadSize == 0 ? 0 : 1));

			//ContentType判定
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());

			// CSVファイルデータを作る
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(ContentType.EXCEL);
		    response.setHeader("Content-Disposition", EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME));
		    response.setCharacterEncoding(Encoding.MS932);
		    writer = response.getWriter();

			// 1行目：カラム名(和名)のリストを作る
			List<Object> colName = new ArrayList<Object>();
			colName.add("状態");
			colName.add("伝票ID");
			if(yosanKannriFlg){
				colName.add("実施起案番号");
				colName.add("支出起案番号");
				colName.add("予算執行伝票種別");
				if(yosanCheckNengetsuFlg) {
					colName.add("予算執行対象月");
				}
			}
			colName.add("伝票番号");
			colName.add("伝票種別");
			colName.add(setting.userTeigiTodokeKensakuKenmei());
			colName.add(setting.userTeigiTodokeKensakuNaiyou());
			colName.add("使用者");
			colName.add("精算期間");
			colName.add("出張先・訪問先");
			colName.add("目的");
			colName.add("金額");
			if(invoiceStart) {
				colName.add("税抜金額"); //インボイス対応
			}
			if (houjinCardFlag) colName.add("法人カード利用金額");
			if (kaishaTehaiFlag) colName.add("会社手配金額");
			colName.add("差引支給金額");
			colName.add("支払希望日\n支払期限");
			colName.add("最終承認日");
			colName.add("支払日");
			colName.add("支払方法");
			colName.add("計上日");
			colName.add("使用日");
			colName.add("取引先");
			colName.add("精算予定日");
			colName.add("仮払伝票ID");
			colName.add("起票日");
			colName.add("起票者部門");
			colName.add("起票者社員番号");
			colName.add("起票者名");
			colName.add("所有者部門");
			colName.add("所有者名");
			colName.add("補足");
			colName.add("消費税率");
			if(invoiceStart) {
				colName.add("事業者区分"); //インボイス対応 TODO カラムの順序 事業者区分と税抜金額の場所
			}

			// カスタマイズ用カラムヘッダー追加処理
			if(this.denpyouIchiranActionStrategy != null)
			{
				for(String columnName : this.denpyouIchiranActionStrategy.getProcessedCsvColumnNameList())
				{
					colName.add(columnName);
				}
			}

			EteamFileLogic.outputCsvRecord(writer, colName);

			for(int tmpPageNo = 1; tmpPageNo <= tmpMaxPage; tmpPageNo++) {
				list = tsuuchiLogic.loadDenpyouIchiranKensaku(
						queryParameter
						, tmpPageNo
						, tmpReadSize
						, sortKbn);

				// 支払区分表示用の文言取得
				Map<String, String> shiharaiHouhouMap = new HashMap<String, String>();
				shiharaiHouhouMap.put(null, "");
				for (GMap map : sysLogic.loadNaibuCdSetting("shiharai_houhou")) {
					shiharaiHouhouMap.put((String)map.get("naibu_cd"), (String)map.get("name"));
				}

				// 2行目以降：データ部のリストを作る
				String maruhiHyoujiFlg = setting.maruhiHyoujiSeigyoFlg();
				for (GMap map : list) {
					//支払方法
					List<Object> csvRecord = new ArrayList<Object>();
					csvRecord.add(map.get("name") + "(" + map.get("cur_cnt") + "/" + map.get("all_cnt") + ")");
					csvRecord.add(map.get("denpyou_id"));
					if(yosanKannriFlg){
						csvRecord.add(map.get("jisshi_kian_bangou"));
						csvRecord.add(map.get("shishutsu_kian_bangou"));
						csvRecord.add(map.get("yosan_shikkou_taishou"));
						if(yosanCheckNengetsuFlg) {
							String yosanCheckNengetsu = "";
							if(isNotEmpty(map.get("yosan_check_nengetsu"))) {
								yosanCheckNengetsu = map.get("yosan_check_nengetsu").toString().substring(0, 4) + "年" + map.get("yosan_check_nengetsu").toString().substring(4, 6) + "月";
							}
							csvRecord.add(yosanCheckNengetsu);
						}
					}
					csvRecord.add(String.format("%1$08d", map.getObject("serial_no"))); // 伝票番号
					csvRecord.add(map.get("denpyou_shubetsu"));
					if("1".equals(map.get("maruhi_flg")) && "0".equals(maruhiHyoujiFlg)) {
						csvRecord.add("");
					}else {
						csvRecord.add(map.get("kenmei"));
					}
// csvRecord.add(map.get("kenmei")); // ユーザー定義届書／件名

					//ユーザー定義届書最初の内容データのみ取得
					if(map.get("naiyou") != null){
						String str = (String)map.get("naiyou");
						if(str.indexOf("╂") >= 0){str = str.substring(0, str.indexOf("╂"));};
						map.put("naiyou", str);
					}
					if("1".equals(map.get("maruhi_flg")) && "0".equals(maruhiHyoujiFlg)) {
						csvRecord.add("");
					}else {
						csvRecord.add(map.get("naiyou"));
					}
// csvRecord.add(map.get("naiyou")); // ユーザー定義届書／内容

					String userName = "";
					if(null != map.get("user_sei") && !(isEmpty((String)map.get("user_sei"))) ) {
						userName = map.get("user_sei").toString() + "　" + map.get("user_mei").toString();
					}
					csvRecord.add(userName); // 使用者

					String seisankikan = "";
					if(null != map.get("seisankikan_from")){
						seisankikan = formatDate(map.get("seisankikan_from")) + "～" + formatDate(map.get("seisankikan_to"));
					}
					csvRecord.add(seisankikan); // 精算期間

					// 出張先・訪問先は改行コードを「\n」に統一した上でスラッシュに置換して入力
					String houmonsaki = map.get("houmonsaki");
					if(StringUtils.isNotEmpty(houmonsaki)) houmonsaki = houmonsaki.replace("\r\n", "\n").replace("\n", "/");
					csvRecord.add(houmonsaki);

					csvRecord.add(map.get("mokuteki")); // 目的
					csvRecord.add(formatMoney(map.get("kingaku")) + ((map.get("gaika") == null || ((String)map.get("gaika")).isEmpty()) ? "" : "\r\n("+map.get("gaika").toString()+")"));
					if(invoiceStart) {
						csvRecord.add((map.get("invoice_denpyou") == null || map.get("invoice_denpyou").equals("1")) ? "" :map.get("zeinuki_kingaku")); // 税抜金額
					}
					if (houjinCardFlag) csvRecord.add(map.get("houjin_kingaku")); //法人カード利用金額
					if (kaishaTehaiFlag) csvRecord.add(map.get("tehai_kingaku")); //会社手配金額
					csvRecord.add(map.get("sashihiki_shikyuu_kingaku")); //差引支給金額
					csvRecord.add(map.get("shiharaikiboubi"));
					csvRecord.add(map.get("shouninbi"));
					csvRecord.add(map.get("shiharaibi"));
					csvRecord.add(shiharaiHouhouMap.get(map.get("shiharaihouhou"))); //支払方法

					//仕訳計上日(画面表示は計上日)、仕訳作成方法を設定できる場合、設定により表示非表示を制御する。（発生：表示 現金：非表示）
					//仕訳作成方法を設定できる且つそれが"現金主義"の場合はcsvに表示させないようにする。
					trimShiwakeKeijoubi(map);
					csvRecord.add(map.get("shiwakeKeijoubi"));

					csvRecord.add(map.get("keijoubi")); //計上日(使用日)

					//取引先はスラッシュ区切りで入力
					String torihikisaki1 = (String)map.get("torihikisaki1");
					if(!torihikisaki1.isEmpty()){
						torihikisaki1 = torihikisaki1.replaceAll("╂", "/");
					}
					if (isNotEmpty(torihikisaki1)){
						csvRecord.add(torihikisaki1);
					} else {
						csvRecord.add("");
					}
					csvRecord.add(map.get("seisan_yoteibi")); //精算予定日
					csvRecord.add(map.get("karibarai_denpyou_id")); //仮払伝票ID
					csvRecord.add(map.get("touroku_time")); //起票日
					csvRecord.add(map.get("bumon_full_name")); //起票者部門
					csvRecord.add(map.get("uiShainNo")); //起票者社員番号
					csvRecord.add(map.get("user_full_name")); //起票者名
					csvRecord.add(map.get("gen_bumon_full_name")); //所有者名
					csvRecord.add(map.get("gen_name")); //所有者部門
					csvRecord.add(map.get("hosoku")); //補足
					csvRecord.add(map.get("zeiritsu")); //消費税率
					if(invoiceStart && map.get("invoice_denpyou") != null) {
						if(null != map.get("jigyousha_kbn") && map.get("invoice_denpyou").equals("0")) {
							csvRecord.add(map.get("jigyousha_kbn").toString().replace("{", "").replace("}", "").replace("1", "免税").replace("0", "通常"));
						}else {
							csvRecord.add(map.get("")); //事業者区分
						}
					}

					// カスタマイズ用データ追加実行
					if(this.denpyouIchiranActionStrategy != null)
					{
						this.denpyouIchiranActionStrategy.mapForCsv = map;
						csvRecord.addAll(this.denpyouIchiranActionStrategy.getProcessedCsvMap());
					}

					EteamFileLogic.outputCsvRecord(writer, csvRecord);
				}
			}

			//5.戻り値を返す
			return "success";
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}finally{
			if(writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * ファイルダウンロードイベント
	 * @return 処理結果
	 */
	public String fileDownload(){
		try(EteamConnection connection = EteamConnection.connect()){

			initConnection(connection);

			//画面部品の作成
			makeParts();

			//設定情報テーブルから最大ダウンロードファイル数を取得
			int maxDownloadNum = Integer.parseInt(setting.fileNumForDownload());

			// 1.入力チェック
			formatCheck();
			hissuCheck(5);
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}
			this.soukanCheck();
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理
			// クエリパラメーターの作成
			var queryParameter = this.createQueryParameter();

			//検索条件から伝票を検索する。
			list = tsuuchiLogic.loadDenpyouIchiranKensaku(
					queryParameter
					, 0 //ページ指定なし
					, 0
					, ""); //標準ソート

			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				reKensaku();
				return "error";
			}

			//カンマ区切りの検索用伝票IDリストを作成する。
			StringBuffer denpyouIdList = new StringBuffer();
			int i=0;
			for (GMap map : list) {
				if(i != 0){
					denpyouIdList.append(",");
				}
				denpyouIdList.append("'" + map.get("denpyou_id") +"'"); //伝票ID
				i++;
			}

			//添付ファイル数を取得する。
			long fileNum = wfLogic.selectTenpuFileNum(denpyouIdList.toString());

			if(fileNum < 1){ //添付ファイルなし
				errorList.add("添付ファイルがありません。");
				reKensaku();
				return "error";
			}

			if(fileNum > maxDownloadNum){ //最大ダウンロードファイル数より大きい。
				errorList.add("添付ファイル数が最大ダウンロード数（" + maxDownloadNum + "）を超えています。");
				reKensaku();
				return "error";
			}

			//重複した添付ファイル名リストを取得する。
			ArrayList<String> duplicateFileList = wfLogic.selectDuplicateTenpuFileName(denpyouIdList.toString());

			//ZIPの作成
			try {

				//ZIP出力ストリームの設定
				ByteArrayOutputStream bArray1 = new ByteArrayOutputStream();
				ZipOutputStream objZos = new ZipOutputStream(bArray1);
				objZos.setEncoding(Encoding.MS932);

				//伝票ID毎に添付ファイルを処理する。
				for (GMap map : list) {

					String wkDenpyouId = (String)map.get("denpyou_id"); //伝票ID

					//指定した伝票IDの添付ファイルを取得する。
					List<GMap> tenpuList = wfLogic.selectTenpuFileBD(wkDenpyouId);
					for (GMap tenpu : tenpuList) {

						//ファイル名を作成する。（重複リストにある場合は名前を変える）
						String tenpuFileName = (String)tenpu.get("file_name");

						if(duplicateFileList.indexOf(tenpuFileName) != -1){
							//ファイル拡張子を取得
							int point = tenpuFileName.lastIndexOf(".");
						    if (point != -1) { //拡張子あり
						        tenpuFileName = tenpuFileName.substring(0, point) + "_" + wkDenpyouId + "-" + tenpu.get("edano") + "." + tenpuFileName.substring(point + 1);
						    }else{//拡張子なし
						        tenpuFileName = tenpuFileName + "_" + wkDenpyouId + "-" + tenpu.get("edano");
						    }
						}

						//ZIPエントリ
						ZipEntry objZe=new ZipEntry(tenpuFileName);
						objZe.setMethod(ZipOutputStream.DEFLATED);

						//ZIP出力ストリームに追加する。
						objZos.putNextEntry(objZe);
						objZos.write((byte[]) tenpu.get("binary_data"));

						//ZIPエントリクローズ
						objZos.closeEntry();
					}
				}

				//ZIP出力ストリームクローズ
				objZos.close();

				contentType = ContentType.ZIP;
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, ZIP_FILENAME);

				ByteArrayInputStream bArray2 = new ByteArrayInputStream(bArray1.toByteArray());
				this.inputStream = bArray2;

			} catch (IOException e) {
			    throw new RuntimeException(e);
			}

			//5.戻り値を返す
			return "success";

		}
	}

	/**
	 * 支払日登録イベント
	 * @return 処理結果
	 */
	public String shiharaibiTouroku(){
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			checkDate(updateShiharaibi,  "支払日", false); // formatCheckに入れてしまうと再検索できなくなってしまうため。
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}
			if(sentaku==null || sentaku.isEmpty()){
				errorList.add("伝票が選択されていません。");
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理
			Date shiharaibi = toDate(updateShiharaibi); // 入力された支払日

			//--------------------
			//引数のカンマ区切り伝票番号を分解
			//--------------------
			List<String[]> denpyouList = makeDList();

			//--------------------
			//伝票１件ずつチェック
			//--------------------
			for (String[] d : denpyouList) {
				String denpyouId = d[0];
				String denpyouKbn = d[1];

				// 計上更新対象の伝票種別か
				if (denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI) && setting.seikyuuShiharaibiIkkatsu().equals("0")
						|| denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI) && setting.shiharaiiraiShiharaibiIkkatsu().equals("0")) {
					errorList.add("伝票ID[" + denpyouId +"]：一括支払日登録できない伝票種別です。");
					continue;
				}
				//カスタマイズ用
				if(shiharaibiTourokuExt(denpyouId, denpyouKbn)){
				}else if (!(
						denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN) //経費立替精算(A001)
					||  denpyouKbn.equals(DENPYOU_KBN.KARIBARAI_SHINSEI) //仮払申請(A002)
					||	denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) //出張旅費精算(A004)
					||	denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) //海外出張旅費精算(A011)
					||	denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) //出張旅費仮払申請(A005)
					||	denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI) //海外出張旅費仮払申請(A012)
					||	denpyouKbn.equals(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI) //通勤定期申請(A006)
					||	denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN) //交通費精算(A010)
					||	denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI) && !(setting.seikyuuShiharaibiIkkatsu().equals("0")) //請求書払い申請(A009)
					||	denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI) && setting.shiharaiiraiShiharaibiIkkatsu().equals("1") //支払依頼申請(A013)
				)){
					errorList.add("伝票ID[" + denpyouId +"]：支払日登録対象外の伝票種別です。");
					continue;
				}

				// ユーザーが更新可能な常態か
				WfSeigyo wfSeigyo = wfEventLg.judgeWfSeigyo(getUser(), denpyouId, null);
				if (!(wfSeigyo.isShounin() && wfSeigyo.isKoushin())) {
					errorList.add("伝票ID[" + denpyouId +"]：支払日登録できる状態ではありません。");
					continue;
				}
				int shiharaiBiMode = commonLc.judgeShiharaiBiMode(denpyouId, getUser());
				if(shiharaiBiMode != 1) {
					errorList.add("伝票ID[" + denpyouId +"]：支払日登録できる状態ではありません。");
					continue;
				}

				//会社設定-計上日・支払タブ-請求書払い申請（支払日一括登録）の設定が"2"の際、マスター参照フラグのチェック
				if(denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI)) {
					String masrefFlg = tsuuchiLogic.findSeikyuushoBarai(denpyouId).get("masref_flg");
					if(setting.seikyuuShiharaibiIkkatsu().equals("2") && masrefFlg.equals("1")) {
						errorList.add("伝票ID[" + denpyouId +"]：取引先マスター参照がONの場合は支払日登録ができません。");
					}
				}

				//支払依頼申請の支払種別チェック（定期なら支払日登録できない）
				if(denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)) {
					GMap shinseiData = tsuuchiLogic.findShiharaiirai(denpyouId);
					String shiharaiShubetsu = (String)shinseiData.get("shiharai_shubetsu");
					if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) {
						errorList.add("伝票ID[" + denpyouId +"]：マスタ取引先は支払日登録ができません。");
					}
				}

				//計上日/支払日のチェック
				KeijouShiharai ks = denpyouIchiranLg.findKeijouShiharai(denpyouKbn, denpyouId);
				List<String> wkErrorList = new ArrayList<>();
					commonLc.checkShiharaiBi(denpyouId, shiharaibi, ks.getKeijoubi(), ks.getKeijoubiName(), ks.getShiharaiHouhou(), getUser(), wkErrorList);
					if(wkErrorList.size() != 0){
						for(int j=0 ; j < wkErrorList.size() ; j++) {
							errorList.add("伝票ID[" + denpyouId +"]：" + wkErrorList.get(j));
						}
					}
			}

			//--------------------
			// 不正伝票が１つでもあった場合、処理終了。
			//--------------------
			if(! errorList.isEmpty()) {
				reKensaku();
				return "error";
			}

			//--------------------
			// 不正伝票が1つもない場合、支払日を更新する。
			//--------------------
			for (String[] d : denpyouList) {
				String denpyouId = d[0];
				String denpyouKbn = d[1];

				if(denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI) && setting.seikyuuShiharaibiIkkatsu().equals("1")) {
					//マスター参照フラグ取得
					String masrefFlg = tsuuchiLogic.findSeikyuushoBarai(denpyouId).get("masref_flg");
					// 請求書払いテーブルの支払日更新
					denpyouIchiranLg.updateShiharaibi(denpyouId, shiharaibi, "0", getUser().getTourokuOrKoushinUserId());
					// 更新前の取引先マスター参照がONの場合はOFFになった旨を記載
					if(masrefFlg.equals("1")) {
						updateShiharaibi = updateShiharaibi + "（マスター参照取消）";
					}
					}else if(denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)){
						// 支払依頼テーブルの支払予定日更新
						denpyouIchiranLg.updateYoteibi(denpyouId, shiharaibi, getUser().getTourokuOrKoushinUserId());
					}else {
						// 各種伝票テーブルの支払日更新
						denpyouIchiranLg.updateShiharaibi(denpyouKbn, denpyouId, shiharaibi, getUser().getTourokuOrKoushinUserId());
					}
				// 承認状況に登録
				commonLc.insertShiharaiBiTourokuRireki(denpyouId, getUser(), updateShiharaibi);
				//伝票一覧テーブル状態更新
				diLogic.updateDenpyouIchiran(denpyouId);
			}

			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 支払日登録対象の伝票種別か（カスタマイズ用）
	 * @param denpyouId  伝票ID
	 * @param denpyouKbn  伝票区分
	 * @return  false
	 */
	protected boolean shiharaibiTourokuExt(String denpyouId, String denpyouKbn){
		return false;
	}

	/**
	 * 計上日更新イベント
	 * @return 処理結果
	 */
	public String keijoubiKoushin(){
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			checkDate(updateKeijoubi,  "計上日", false); // formatCheckに入れてしまうと再検索できなくなってしまうため。
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}
			//選択した伝票IDの取得
			if(sentaku==null || sentaku.isEmpty()){
				errorList.add("伝票が選択されていません。");
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理
			Date keijoubi = toDate(updateKeijoubi); // 入力された計上日

			//--------------------
			//引数のカンマ区切り伝票番号を分解
			//--------------------
			List<String[]> denpyouList = makeDList();

			Map<String,String[]> shiharaiIraiMap = new HashMap<String,String[]>(); //支払依頼データ保持用

			//--------------------
			//伝票１件ずつチェック
			//--------------------
			for (String[] d : denpyouList) {
				String denpyouId = d[0];
				String denpyouKbn = d[1];

				// 計上更新対象の伝票種別か
				if (denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI)) {
					GMap denpyouRecord = tsuuchiLogic.findSeikyuushoBarai(denpyouId);
					if (denpyouRecord.get("kake_flg").equals("0")) {
						errorList.add("伝票ID[" + denpyouId +"]：掛けなしのため、計上日更新対象外の伝票種別です。");
						continue;
					}
				}
				//カスタマイズ用
				if (keijoubiKoushinExt(denpyouId, denpyouKbn)){

				}else if(!(
						(denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN) && ! setting.shiwakeSakuseiHouhouA001().equals("3")) //経費立替精算(A001)
					||	(denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) && ! setting.shiwakeSakuseiHouhouA004().equals("3")) //出張旅費精算(A004)
					||	(denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) && ! setting.shiwakeSakuseiHouhouA011().equals("3")) //海外出張旅費精算(A011)
					||	(denpyouKbn.equals(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU)&& ! setting.shiwakeSakuseiHouhouA009().equals("3")) //自動引落(A009)
					||	(denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN) && ! setting.shiwakeSakuseiHouhouA010().equals("3")) //交通費精算(A010)
					||	(denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI)) //請求書払い申請(A003)
					||	(denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)) //支払依頼申請(A013)
				)){
					errorList.add("伝票ID[" + denpyouId +"]：計上日更新対象外の伝票種別です。");
					continue;
				}

				// ユーザーが更新可能な常態か
				WfSeigyo wfSeigyo = wfEventLg.judgeWfSeigyo(getUser(), denpyouId, null);
				if (!(wfSeigyo.isShounin() && wfSeigyo.isKoushin())) {
					errorList.add("伝票ID[" + denpyouId +"]：計上日更新できる状態ではありません。");
					continue;
				}
				if (! denpyouKbn.equals(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU) && ! denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI) ) {
					int shiharaiBiMode = commonLc.judgeShiharaiBiMode(denpyouId, getUser());
					if(shiharaiBiMode != 1) {
						errorList.add("伝票ID[" + denpyouId +"]：計上日更新できる状態ではありません。");
						continue;
					}
				}

				if(!wfEventLg.judgeEnableDenpyouHenkouKengenKeijou(getUser(),wfSeigyo)) {
					errorList.add("伝票ID[" + denpyouId +"]：計上日更新できる状態ではありません。");
				}

				//計上日/支払日のチェック
				KeijouShiharai ks = denpyouIchiranLg.findKeijouShiharai(denpyouKbn, denpyouId);
				List<String> wkErrorList = new ArrayList<>();
				if(denpyouKbn.equals(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU)) {
					//自動引落
					commonLc.checkHikiotoshiBiForJidouHikiotoshi(ks.getShiharaibi(), keijoubi, wkErrorList); //メッセージ文言のみ変更したチェック処理呼出
					if(wkErrorList.size() != 0){
						for(int j=0 ; j < wkErrorList.size() ; j++) {
							errorList.add("伝票ID[" + denpyouId +"]：" + wkErrorList.get(j));
						}
					}
				} else if(denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)){
					//支払依頼
					ShiharaiIraiAction wfAction = (ShiharaiIraiAction)denpyouIchiranLg.makeWfAction(denpyouId, denpyouKbn);
					wfAction.setUser((User)session.get(Sessionkey.USER));
					wfAction.init();
					wfAction.soukanCheckFromDenpyouIchiran(connection, updateKeijoubi);
					wkErrorList = wfAction.getErrorList();
					if(wkErrorList.size() != 0){
						for(int j=0 ; j < wkErrorList.size() ; j++) {
							errorList.add("伝票ID[" + denpyouId +"]：" + wkErrorList.get(j));
						}
					}else {
						String[] dateStr = new String[3];
						dateStr[0] = wfAction.getShiharaibi();
						dateStr[1] = wfAction.getShiharaiKijitsu();
						dateStr[2] = wfAction.getYoteibi();
						shiharaiIraiMap.put(denpyouId,dateStr);
					}
				} else {
					//精算
					if(ks.getShiharaibi() != null) {
						commonLc.checkShiharaiBi(denpyouId, ks.getShiharaibi(), keijoubi, ks.getKeijoubiName(), ks.getShiharaiHouhou(), getUser(), wkErrorList, "1");
						if(wkErrorList.size() != 0){
							for(int j=0 ; j < wkErrorList.size() ; j++) {
								errorList.add("伝票ID[" + denpyouId +"]：" + wkErrorList.get(j));
							}
						}
					}
				}
			}

			//--------------------
			// 不正伝票が１つでもあった場合、処理終了。
			//--------------------
			if(! errorList.isEmpty()) {
				reKensaku();
				return "error";
			}

			//--------------------
			// 不正伝票が1つもない場合、計上日を更新する
			//--------------------
			for (String[] d : denpyouList) {
				String denpyouId = d[0];
				String denpyouKbn = d[1];
				// 各種伝票の計上日更新
				if(denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)){
					//支払依頼の場合、支払日・支払期限をセットで更新
					String[] dateStr = shiharaiIraiMap.get(denpyouId);
					Date shiharaibi = toDate(dateStr[0]);
					Date shiharaiKijitsu = toDate(dateStr[1]);
					Date yoteibi = toDate(dateStr[2]);
					denpyouIchiranLg.updateShiharaiiraiDate(denpyouId, keijoubi, shiharaibi, shiharaiKijitsu, yoteibi, getUser().getTourokuOrKoushinUserId());
				}else {
					denpyouIchiranLg.updateKeijoubi(denpyouKbn, denpyouId, keijoubi, getUser().getTourokuOrKoushinUserId());
				}
				// 承認状況に登録
				commonLc.insertKeijouBiKoushinRireki(denpyouId, getUser(), updateKeijoubi);

				//伝票一覧テーブル状態更新
				diLogic.updateDenpyouIchiran(denpyouId);
			}

			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 計上日更新対象の伝票か（カスタマイズ版）
	 * @param denpyouId  伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return  false
	 */
	protected boolean keijoubiKoushinExt(String denpyouId, String denpyouKbn) {
		return false;
	}

	/**
	 * 一括起案終了・起案終了解除イベント
	 * kianSyuryoTypeが 1:起案終了 0:起案終了解除
	 * @return 処理結果
	 */
	public String kianSyuryou(){
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			hissuCheck(3);
			if(sentaku==null || sentaku.isEmpty()){
				errorList.add("伝票が選択されていません。");
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック

			// 予算執行オプション無効ならエラー
			if( !RegAccess.checkEnableYosanShikkouOption().equals(RegAccess.YOSAN_SHIKKOU_OP.A_OPTION) ) {
				throw new EteamAccessDeniedException("予算執行オプションが無効");
			}
			// 必須値渡されていなければエラー
			if( isEmpty(kianSyuryoType) || ( !"0".equals(kianSyuryoType) && !"1".equals(kianSyuryoType) ) ) {
				throw new EteamAccessDeniedException("一括起案終了指定が不正");
			}
			// 操作実行者が経理ロールかadminでなければエラー
			if( ! ( UserId.ADMIN.equals(getUser().getSeigyoUserId()) || commonLc.userIsKeiri(getUser()) ) ){
				throw new EteamAccessDeniedException("経理ロール・admin以外は一括起案終了・起案終了解除不可");
			}

			// 4.処理

			//--------------------
			//引数のカンマ区切り伝票番号を分解
			//--------------------
			List<String[]> denpyouList = makeDList();

			//--------------------
			//伝票１件ずつ起案終了操作チェック
			//--------------------
			List<WorkflowEventControl> wfActionList = new ArrayList<>();
			for (String[] d : denpyouList) {
				String denpyouId = d[0];
				String denpyouKbn = d[1];
				WorkflowEventControl wfAction = denpyouIchiranLg.makeWfAction(denpyouId, denpyouKbn);
				wfActionList.add(wfAction);
				try {
					wfAction.setUser((User)session.get(Sessionkey.USER));
					wfAction.init();
					if("".equals(wfAction.getKianShuuryouFlg())){
						errorList.add("伝票ID[" + denpyouId + "]：起案終了操作できない種別の伝票か、起案終了操作できる状態ではありません。");
					}else if("1".equals(kianSyuryoType) && "1".equals(wfAction.getKianShuuryouFlg())){
						errorList.add("伝票ID[" + denpyouId + "]：既に起案終了されている伝票です。");
					}else if("0".equals(kianSyuryoType) && "0".equals(wfAction.getKianShuuryouFlg())){
						errorList.add("伝票ID[" + denpyouId + "]：既に起案終了解除されているか、起案終了未済の伝票です。");
					}
				} catch(EteamAccessDeniedException e) {
					//起案終了で例外出すようなら（アクセス制御）
					log.warn("起案終了操作チェックでエラー", e);
					errorList.add("伝票ID[" + denpyouId + "]：起案終了操作できる状態ではありません。");
				} catch(RuntimeException e) {
					//起案終了で例外出すようなら（システムエラー？）
					log.warn("起案終了操作チェックでエラー", e);
					errorList.add("伝票ID[" + denpyouId + "]：起案終了操作できない伝票です。");
				}
			}
			//起案終了操作チェックで１件でもエラーあるなら終わり
			if(! errorList.isEmpty()) {
				connection.rollback();
				reKensaku();
				return "error";
			}

			//--------------------
			//全部起案終了操作できそうなので処理実行
			//--------------------
			for (WorkflowEventControl wfAction : wfActionList) {
				try {
					//起案終了・終了解除フラグだけは伝票一覧側で設定
					wfAction.setKianShuuryouFlg(kianSyuryoType);
					wfAction.kianShuuryou();
				} catch(Exception e) {
					log.warn("起案終了操作実行でエラー", e);
					errorList.add("伝票ID[" + wfAction.getDenpyouId() + "]：起案終了操作できない伝票です。");
				}
			}
			//承認実行で１件でもエラーあるなら終わり
			if(! errorList.isEmpty()) {
				connection.rollback();
				reKensaku();
				return "error";
			}
			//伝票一覧テーブル状態更新
			for (WorkflowEventControl wfAction : wfActionList) {
				diLogic.updateDenpyouIchiran(wfAction.getDenpyouId());
			}

			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 更新処理エラー発生時、再検索用
	 */
	protected void reKensaku() {
		List<String> tmpErrorList = errorList;
		errorList = new ArrayList<>();
		kensaku();
		errorList = tmpErrorList;
	}

	/**
	 * パラメータ(sentaku)はカンマ区切りの伝票ID→これををバラして、伝票ID/伝票区分のリスト作る
	 * @return 伝票ID/伝票区分のリスト
	 */
	protected List<String[]> makeDList() {
		List<String[]> ret = new ArrayList<>();
		String[] listDenpyouId = sentaku.split(",");
		for(int i=0 ; i < listDenpyouId.length ; i++){
			String denpyouId = listDenpyouId[i].trim();
			String denpyouKbn = denpyouId.substring(7, 11);
			ret.add(new String[]{denpyouId, denpyouKbn});
		}
		return ret;
	}

	/**
	 * 起案一覧CSV出力イベント
	 * @return 処理結果
	 */
	public String kianCsvOutput(){

		PrintWriter writer = null;
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理

			//ContentType判定
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());

			// CSVファイルデータを作る
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(ContentType.EXCEL);
		    response.setHeader("Content-Disposition", EteamCommon.contentDisposition(browserCode, true, KIAN_CSV_FILENAME));
		    response.setCharacterEncoding(Encoding.MS932);
		    writer = response.getWriter();


			// 検索条件：起案番号の号部分に入力があれば全角文字に変換する
			String cnvKianBangou = kianBangou;
			if (super.isNotEmpty(kianBangou)){
				cnvKianBangou = WorkflowEventControlLogic.convKianbangouToZenkaku(Integer.valueOf(kianBangou));
			}

			// 一覧の先頭となる起案伝票の件数を取得
			long kianListCont = tsuuchiLogic.findKianListCount(
					super.getUser()
					, kensakuGyoumuShubetsu
					, kensakuDenpyouShubetsu
					, kianBangouNendo
					, kianBangouRyakugou
					, cnvKianBangou
					, kianBangouRyakugouFromTo
					, kianBangouFrom
					, kianBangouTo
					, kianBangouEnd
					, kianBangouNoShishutsuIrai
					, yosanshikkouShubetsu
					, yosanCheckNengetsuFrom
					, yosanCheckNengetsuTo
					);

			// 処理の分割回数の決定
			int tmpReadSize = EteamConnection.DB_FETCH_SIZE / 2;
			int tmpMaxPage = (int)(kianListCont / tmpReadSize + (kianListCont % tmpReadSize == 0 ? 0 : 1));

			// 一覧の先頭となる起案伝票の一覧を取得する。
			for(int tmpPageNo = 1; tmpPageNo <= tmpMaxPage; tmpPageNo++) {
				List<GMap> headlst = tsuuchiLogic.loadKianList(
						super.getUser()
						, kensakuGyoumuShubetsu
						, kensakuDenpyouShubetsu
						, kianBangouNendo
						, kianBangouRyakugou
						, cnvKianBangou
						, kianBangouRyakugouFromTo
						, kianBangouFrom
						, kianBangouTo
						, kianBangouEnd
						, kianBangouNoShishutsuIrai
						, yosanshikkouShubetsu
						, yosanCheckNengetsuFrom
						, yosanCheckNengetsuTo
						, tmpPageNo
						, tmpReadSize
						);

				if(headlst.isEmpty()){
					errorList.add("検索結果が0件でした。");
					reKensaku();
					return "error";
				}

				boolean isFirstRow = (tmpPageNo==1)? true : false;
				for(GMap headMap : headlst) {
					list = tsuuchiLogic.loadKianIchiranCsvDataWithMap(
							  headMap
							, toDate(kihyouBiFrom)
							, toDate(kihyouBiTo)
							, toDate(shouninBiFrom)
							, toDate(shouninBiTo)
							, toDecimal(kingakuFrom)
							, toDecimal(kingakuTo)
							, tekiyou
							, bumonCdFrom
							, bumonCdTo
							, kanitodokeKenmei
							);
					if(!list.isEmpty()){
						if(isFirstRow) {
							// 1行目：カラム名(和名)のリストを作る
							List<Object> colName = new ArrayList<Object>();
							colName.add("起案番号");
							colName.add("件名");
							colName.add("伝票種別");
							colName.add("予算執行伝票種別");
							colName.add("関連起案番号");
							colName.add("起案・申請日");
							colName.add("起案・決済日");
							colName.add("支出依頼・申請日");
							colName.add("支出依頼・決済日");
							colName.add("支出依頼申請書数");
							colName.add("起案金額");
							colName.add("支出依頼・金額合計");
							colName.add("起案－支出依頼");
							colName.add("起案終了日");
							EteamFileLogic.outputCsvRecord(writer, colName);
							isFirstRow = false;
						}
						// 2行目以降：データ部のリストを作る
						for (GMap map : list) {
							List<Object> csvRecord = new ArrayList<Object>();
							csvRecord.add(map.get("kianbangou"));
							csvRecord.add(map.get("kenmei"));
							csvRecord.add(map.get("denpyou_shubetsu"));
							csvRecord.add(map.get("yosan_shikkou_taishou"));
							csvRecord.add(map.get("kanren_kian_bangou"));
							csvRecord.add(map.get("kian_shinseibi"));
							csvRecord.add(map.get("kian_kessaibi"));
							csvRecord.add(map.get("irai_shinseibi"));
							csvRecord.add(map.get("irai_kessaibi"));
							csvRecord.add(map.get("irai_kensuu"));
							csvRecord.add(map.get("kian_shishutsu_goukei"));
							csvRecord.add(map.get("shishutsu_irai_goukei"));
							csvRecord.add(map.get("sagaku"));
							csvRecord.add(map.get("kian_shuuryoubi"));
							EteamFileLogic.outputCsvRecord(writer, csvRecord);
						}
					}
				}

				if(isFirstRow){
					errorList.add("検索結果が0件でした。");
					reKensaku();
					return "error";
				}
			}


			//5.戻り値を返す
			return "success";

		} catch (IOException e) {
		    throw new RuntimeException(e);
		}finally{
			if(writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * フォーカス時自動更新フラグ設定イベント
	 * @return 処理結果
	 */
	public String jidouKoushinSet() {
		return this.setUserDefaultValueKbn(UserDefaultValueKbn.ichiranReloadKbn, this.jidouKoushinFlg);
	}
	
	/**
	 * 取下済・否認済　表示フラグ設定イベント
	 * @return 処理結果
	 */
	public String torisageHininHyoujiSet() {
		return this.setUserDefaultValueKbn(UserDefaultValueKbn.torisageHininHyoujiKbn, this.torisageHininHyoujiFlg);
	}

	/**
	 * @param kbn ユーザーデフォルト設定区分
	 * @param property 呼応するプロパティ
	 * @return 結果
	 */
	protected String setUserDefaultValueKbn(UserDefaultValueKbn kbn, String property)
	{
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();
			// 1.入力チェック なし
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理
			//user_default_valueに値登録
			EteamUserSettingLogic userSettingLogic = EteamContainer.getComponent(EteamUserSettingLogic.class, connection);
			userSettingLogic.upsertUserDefaultValue(kbn, getUser().getLoginUserId(), property);
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * URLに対するGETパラメータ文字列を作る
	 * @return GETパラメータ文字列
	 */
	protected String makeUrlParam() {
		try {
			String urlParam = "&kanrenDenpyou=" + URLEncoder.encode(kanrenDenpyou,"UTF-8")
					+ "&kensakuDenpyouId=" + URLEncoder.encode(kensakuDenpyouId,"UTF-8")
					+ "&kensakuSerialNo=" + URLEncoder.encode(kensakuSerialNo,"UTF-8")
					+ "&kensakuJoutai=" + URLEncoder.encode(kensakuJoutai,"UTF-8")
					+ "&kianBangouNendo=" + URLEncoder.encode(kianBangouNendo,"UTF-8")
					+ "&kianBangouRyakugou=" + URLEncoder.encode(kianBangouRyakugou,"UTF-8")
					+ "&kianBangou=" + URLEncoder.encode(kianBangou,"UTF-8")
					+ "&kianBangouRyakugouFromTo=" + URLEncoder.encode(kianBangouRyakugouFromTo,"UTF-8")
					+ "&kianBangouFrom=" + URLEncoder.encode(kianBangouFrom,"UTF-8")
					+ "&kianBangouTo=" + URLEncoder.encode(kianBangouTo,"UTF-8")
					+ "&kianBangouInput=" + URLEncoder.encode(kianBangouInput,"UTF-8")
					+ "&kianBangouEnd=" + URLEncoder.encode(kianBangouEnd,"UTF-8")
					+ "&kianBangouUnyou=" + URLEncoder.encode(kianBangouUnyou,"UTF-8")
					+ "&kianBangouNoShishutsuIrai=" + URLEncoder.encode(kianBangouNoShishutsuIrai,"UTF-8");
			if(0==yosanshikkouShubetsu.length){
				urlParam = urlParam.concat("&yosanshikkouShubetsu=" + URLEncoder.encode("","UTF-8"));
			}else{
				for(int i = 0 ; i < yosanshikkouShubetsu.length ; i++){
					urlParam = urlParam.concat("&yosanshikkouShubetsu=" + URLEncoder.encode(yosanshikkouShubetsu[i],"UTF-8"));
				}
			}
			urlParam = urlParam.concat("&yosanCheckNengetsuFrom=" + URLEncoder.encode(yosanCheckNengetsuFrom,"UTF-8")
					+ "&yosanCheckNengetsuTo=" + URLEncoder.encode(yosanCheckNengetsuTo,"UTF-8")
					+ "&kensakuGyoumuShubetsu=" + URLEncoder.encode(kensakuGyoumuShubetsu,"UTF-8")
					+ "&kensakuDenpyouShubetsu=" + URLEncoder.encode(kensakuDenpyouShubetsu,"UTF-8")
					+ "&dpkbSibori=" + URLEncoder.encode(dpkbSibori,"UTF-8")
					+ "&kihyouBiFrom=" + URLEncoder.encode(kihyouBiFrom,"UTF-8")
					+ "&kihyouBiTo=" + URLEncoder.encode(kihyouBiTo,"UTF-8")
					+ "&kensakuKihyouShozokuBumonCd=" + URLEncoder.encode(kensakuKihyouShozokuBumonCd,"UTF-8")
					+ "&kensakuKihyouShozokuBumonName=" + URLEncoder.encode(kensakuKihyouShozokuBumonName,"UTF-8")
					+ "&kensakuKihyouShozokuUserShainNo=" + URLEncoder.encode(kensakuKihyouShozokuUserShainNo,"UTF-8")
					+ "&kensakuKihyouShozokuUserName=" + URLEncoder.encode(kensakuKihyouShozokuUserName,"UTF-8")
					+ "&shouninBiFrom=" + URLEncoder.encode(shouninBiFrom,"UTF-8")
					+ "&shouninBiTo=" + URLEncoder.encode(shouninBiTo,"UTF-8")
					+ "&shouninsyaShozokuBumonCd=" + URLEncoder.encode(shouninsyaShozokuBumonCd,"UTF-8")
					+ "&shouninsyaShozokuBumonName=" + URLEncoder.encode(shouninsyaShozokuBumonName,"UTF-8")
					+ "&shouninsyaShozokuUserShainNo=" + URLEncoder.encode(shouninsyaShozokuUserShainNo,"UTF-8")
					+ "&shouninsyaShozokuUserName=" + URLEncoder.encode(shouninsyaShozokuUserName,"UTF-8")
					+ "&shoyuushaShozokuBumonCd=" + URLEncoder.encode(shoyuushaShozokuBumonCd,"UTF-8")
					+ "&shoyuushaShozokuBumonName=" + URLEncoder.encode(shoyuushaShozokuBumonName,"UTF-8")
					+ "&shoyuushaShozokuUserShainNo=" + URLEncoder.encode(shoyuushaShozokuUserShainNo,"UTF-8")
					+ "&shoyuushaShozokuUserName=" + URLEncoder.encode(shoyuushaShozokuUserName,"UTF-8")
					+ "&tekiyou=" + URLEncoder.encode(tekiyou,"UTF-8")
					+ "&kingakuFrom=" + URLEncoder.encode(kingakuFrom,"UTF-8")
					+ "&kingakuTo=" + URLEncoder.encode(kingakuTo,"UTF-8")
					+ "&meisaiKingakuFrom=" + URLEncoder.encode(meisaiKingakuFrom,"UTF-8")
					+ "&meisaiKingakuTo=" + URLEncoder.encode(meisaiKingakuTo,"UTF-8")
					+ "&queryZeiritsu=" + URLEncoder.encode(queryZeiritsu,"UTF-8")
					+ "&keigenZeiritsuKbn=" + URLEncoder.encode(keigenZeiritsuKbn,"UTF-8")
					+ "&houjinCardRiyou=" + URLEncoder.encode(houjinCardRiyou,"UTF-8")
					+ "&kaishaTehai=" + URLEncoder.encode(kaishaTehai,"UTF-8")
					+ "&maruhi=" + URLEncoder.encode(maruhi,"UTF-8")
					+ "&shiharaiBiFrom=" + URLEncoder.encode(shiharaiBiFrom,"UTF-8")
					+ "&shiharaiBiTo=" + URLEncoder.encode(shiharaiBiTo,"UTF-8")
					+ "&shiharaiKiboubiFrom=" + URLEncoder.encode(shiharaiKiboubiFrom,"UTF-8")
					+ "&shiharaiKiboubiTo=" + URLEncoder.encode(shiharaiKiboubiTo,"UTF-8")
					+ "&keijoubiFrom=" + URLEncoder.encode(keijoubiFrom,"UTF-8")
					+ "&keijoubiTo=" + URLEncoder.encode(keijoubiTo,"UTF-8")
					+ "&shiwakeKeijoubiFrom=" + URLEncoder.encode(shiwakeKeijoubiFrom,"UTF-8")
					+ "&shiwakeKeijoubiTo=" + URLEncoder.encode(shiwakeKeijoubiTo,"UTF-8")
					+ "&karikataBumonCdFrom=" + URLEncoder.encode(karikataBumonCdFrom,"UTF-8")
					+ "&karikataBumonNameFrom=" + URLEncoder.encode(karikataBumonNameFrom,"UTF-8")
					+ "&karikataBumonCdTo=" + URLEncoder.encode(karikataBumonCdTo,"UTF-8")
					+ "&karikataBumonNameTo=" + URLEncoder.encode(karikataBumonNameTo,"UTF-8")
					+ "&kashikataBumonCdFrom=" + URLEncoder.encode(kashikataBumonCdFrom,"UTF-8")
					+ "&kashikataBumonNameFrom=" + URLEncoder.encode(kashikataBumonNameFrom,"UTF-8")
					+ "&kashikataBumonCdTo=" + URLEncoder.encode(kashikataBumonCdTo,"UTF-8")
					+ "&kashikataBumonNameTo=" + URLEncoder.encode(kashikataBumonNameTo,"UTF-8")
					+ "&karikataKamokuCdFrom=" + URLEncoder.encode(karikataKamokuCdFrom,"UTF-8")
					+ "&karikataKamokuNameFrom=" + URLEncoder.encode(karikataKamokuNameFrom,"UTF-8")
					+ "&karikataKamokuCdTo=" + URLEncoder.encode(karikataKamokuCdTo,"UTF-8")
					+ "&karikataKamokuNameTo=" + URLEncoder.encode(karikataKamokuNameTo,"UTF-8")
					+ "&kashikataKamokuCdFrom=" + URLEncoder.encode(kashikataKamokuCdFrom,"UTF-8")
					+ "&kashikataKamokuNameFrom=" + URLEncoder.encode(kashikataKamokuNameFrom,"UTF-8")
					+ "&kashikataKamokuCdTo=" + URLEncoder.encode(kashikataKamokuCdTo,"UTF-8")
					+ "&kashikataKamokuNameTo=" + URLEncoder.encode(kashikataKamokuNameTo,"UTF-8")
					+ "&karikataKamokuEdanoCdFrom=" + URLEncoder.encode(karikataKamokuEdanoCdFrom,"UTF-8")
					+ "&karikataKamokuEdanoNameFrom=" + URLEncoder.encode(karikataKamokuEdanoNameFrom,"UTF-8")
					+ "&karikataKamokuEdanoCdTo=" + URLEncoder.encode(karikataKamokuEdanoCdTo,"UTF-8")
					+ "&karikataKamokuEdanoNameTo=" + URLEncoder.encode(karikataKamokuEdanoNameTo,"UTF-8")
					+ "&kashikataKamokuEdanoCdFrom=" + URLEncoder.encode(kashikataKamokuEdanoCdFrom,"UTF-8")
					+ "&kashikataKamokuEdanoNameFrom=" + URLEncoder.encode(kashikataKamokuEdanoNameFrom,"UTF-8")
					+ "&kashikataKamokuEdanoCdTo=" + URLEncoder.encode(kashikataKamokuEdanoCdTo,"UTF-8")
					+ "&kashikataKamokuEdanoNameTo=" + URLEncoder.encode(kashikataKamokuEdanoNameTo,"UTF-8")
					+ "&karikataTorihikisakiCdFrom=" + URLEncoder.encode(karikataTorihikisakiCdFrom,"UTF-8")
					+ "&karikataTorihikisakiNameFrom=" + URLEncoder.encode(karikataTorihikisakiNameFrom,"UTF-8")
					+ "&karikataTorihikisakiCdTo=" + URLEncoder.encode(karikataTorihikisakiCdTo,"UTF-8")
					+ "&karikataTorihikisakiNameTo=" + URLEncoder.encode(karikataTorihikisakiNameTo,"UTF-8")
					+ "&kashikataTorihikisakiCdFrom=" + URLEncoder.encode(kashikataTorihikisakiCdFrom,"UTF-8")
					+ "&kashikataTorihikisakiNameFrom=" + URLEncoder.encode(kashikataTorihikisakiNameFrom,"UTF-8")
					+ "&kashikataTorihikisakiCdTo=" + URLEncoder.encode(kashikataTorihikisakiCdTo,"UTF-8")
					+ "&kashikataTorihikisakiNameTo=" + URLEncoder.encode(kashikataTorihikisakiNameTo,"UTF-8")
					+ "&ryoushuushoSeikyuushoTou=" + URLEncoder.encode(ryoushuushoSeikyuushoTou,"UTF-8")
					+ "&shousaiKensakuHyoujiFlg=" + URLEncoder.encode(shousaiKensakuHyoujiFlg,"UTF-8")
					+ "&kensakuJoukenHyoujiFlg=" + URLEncoder.encode(kensakuJoukenHyoujiFlg,"UTF-8")
					+ "&miseisanKaribarai=" + URLEncoder.encode(miseisanKaribarai,"UTF-8")
					+ "&miseisanUkagai=" + URLEncoder.encode(miseisanUkagai,"UTF-8")
					+ "&tenpuFileFlg=" + URLEncoder.encode(tenpuFileFlg,"UTF-8")
					+ "&jigyoushaKbn=" + URLEncoder.encode(jigyoushaKbn,"UTF-8")
					+ "&shiharaiName=" + URLEncoder.encode(shiharaiName,"UTF-8")
					+ "&zeinukiKingakuFrom=" + URLEncoder.encode(zeinukiKingakuFrom,"UTF-8")
					+ "&zeinukiKingakuTo=" + URLEncoder.encode(zeinukiKingakuTo,"UTF-8")
					+ "&zeinukiMeisaiKingakuFrom=" + URLEncoder.encode(zeinukiMeisaiKingakuFrom,"UTF-8")
					+ "&zeinukiMeisaiKingakuTo=" + URLEncoder.encode(zeinukiMeisaiKingakuTo,"UTF-8")
				);
			if(0==tenpuFileShubetsu.length){
				urlParam = urlParam.concat("&tenpuFileShubetsu=");
			}else{
				for(int i = 0 ; i < tenpuFileShubetsu.length ; i++){
					urlParam = urlParam.concat("&tenpuFileShubetsu=" + URLEncoder.encode(tenpuFileShubetsu[i],"UTF-8"));
				}
			}
			urlParam = urlParam.concat("&ebunshoShubetsu=" + URLEncoder.encode(ebunshoShubetsu,"UTF-8")
					+ "&ebunshoNengappiFrom=" + URLEncoder.encode(ebunshoNengappiFrom,"UTF-8")
					+ "&ebunshoNengappiTo=" + URLEncoder.encode(ebunshoNengappiTo,"UTF-8")
					+ "&ebunshoNengappiMinyuuryokuFlg=" + URLEncoder.encode(ebunshoNengappiMinyuuryokuFlg,"UTF-8")
					+ "&ebunshoKingakuFrom=" + URLEncoder.encode(ebunshoKingakuFrom,"UTF-8")
					+ "&ebunshoKingakuTo=" + URLEncoder.encode(ebunshoKingakuTo,"UTF-8")
					+ "&ebunshoKingakuMinyuuryokuFlg=" + URLEncoder.encode(ebunshoKingakuMinyuuryokuFlg,"UTF-8")
					+ "&ebunshoHakkousha=" + URLEncoder.encode(ebunshoHakkousha,"UTF-8")
					+ "&ebunshoHakkoushaMinyuuryokuFlg=" + URLEncoder.encode(ebunshoHakkoushaMinyuuryokuFlg,"UTF-8")
					+ "&shiwakeStatus=" + URLEncoder.encode(shiwakeStatus,"UTF-8")
					+ "&fbStatus=" + URLEncoder.encode(fbStatus,"UTF-8")
					+ "&bumonSentakuFlag=" + URLEncoder.encode(bumonSentakuFlag,"UTF-8")
					+ "&bumonCdFrom=" + URLEncoder.encode(bumonCdFrom,"UTF-8")
					+ "&bumonCdTo=" + URLEncoder.encode(bumonCdTo,"UTF-8")
					+ "&bumonNameFrom=" + URLEncoder.encode(bumonNameFrom,"UTF-8")
					+ "&bumonNameTo=" + URLEncoder.encode(bumonNameTo,"UTF-8")
					+ "&kanitodokeKenmei=" + URLEncoder.encode(kanitodokeKenmei,"UTF-8")
					+ "&kanitodokeNaiyou=" + URLEncoder.encode(kanitodokeNaiyou,"UTF-8")
					+ "&torisageHininHyoujiFlg=" + URLEncoder.encode(this.torisageHininHyoujiFlg,"UTF-8")
					);
			return urlParam;
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
		}
	}

	/**
	 * データリストのフォーマット(画面)
	 * @param kaniItem アイテムカラム名（簡易届用）
	 */
	protected void formatList4JSP(String[] kaniItem) {
		// 支払区分表示用の文言取得
		Map<String, String> shiharaiHouhouMap = new HashMap<String, String>();
		shiharaiHouhouMap.put(null, "");
		for (GMap map : sysLogic.loadNaibuCdSetting("shiharai_houhou")) {
			shiharaiHouhouMap.put((String)map.get("naibu_cd"), (String)map.get("name"));
		}

		String maruhiHyoujiFlg = setting.maruhiHyoujiSeigyoFlg();
		// レコードの初期化
		for (GMap map : list) {
			String maruhiFlg = map.get("maruhi_flg");
			map.put("joutai",map.get("name")); //状態
			map.put("denpyouId",map.get("denpyou_id")); //伝票ID
			map.put("denpyouShubetsu",map.get("denpyou_shubetsu")); //伝票種別

			//伝票URL
			Integer version = Integer.parseInt(map.get("version").toString());

			if(version >= 1) {
				map.put("denpyouUrl", map.get("denpyou_shubetsu_url")+"?denpyouId="+map.get("denpyou_id")+"&denpyouKbn="+map.get("denpyou_kbn")+"&version="+version.toString());
			} else {
				map.put("denpyouUrl", map.get("denpyou_shubetsu_url")+"?denpyouId="+map.get("denpyou_id")+"&denpyouKbn="+map.get("denpyou_kbn"));
			}

			map.put("jisshiKianBangou",map.get("jisshi_kian_bangou")); //実施起案番号
			map.put("shishutsuKianBangou",map.get("shishutsu_kian_bangou")); //支出起案番号
			map.put("yosanShikkouTaishou",map.get("yosan_shikkou_taishou")); //予算執行対象

			String yosanCheckNengetsu = "";
			if(isNotEmpty(map.get("yosan_check_nengetsu"))) {
				yosanCheckNengetsu = map.get("yosan_check_nengetsu").toString().substring(0, 4) + "年" + map.get("yosan_check_nengetsu").toString().substring(4, 6) + "月";
			}
			map.put("yosanCheckNengetsu", yosanCheckNengetsu); //予算執行対象月
			map.put("kingaku",formatMoney(map.get("kingaku"))); //金額
			map.put("zeinukiKingaku", (map.get("invoice_denpyou") == null || map.get("invoice_denpyou").equals("1")) ? "" :formatMoney(map.get("zeinuki_kingaku"))); //税抜金額(インボイス対応前伝票なら空白固定)

			if(map.get("gaika") == null || ((String)map.get("gaika")).isEmpty()){ map.remove("gaika"); } //一覧テーブルにはgaikaはブランクで登録される場合があるため消しておく

			//取引先は先頭の２０文字までを出力
			int len = 20;
			String torihikisaki1 = (String)map.get("torihikisaki1");
			if(!torihikisaki1.isEmpty()){
				torihikisaki1 = torihikisaki1.replaceAll("╂", "\r\n");
			}
			if(torihikisaki1 != null && torihikisaki1.length() > len ){
				torihikisaki1= torihikisaki1.substring(0,len);
			}
			map.put("torihikisaki",torihikisaki1); //取引先
			map.put("kihyouBi",formatTime(map.get("touroku_time"))); //起票日
			map.put("kihyouShozokuBumonName",((String)map.get("bumon_full_name")).replaceAll("｜", "\r\n")); //起票者所属部門
			map.put("KihyouShozokuUserName",map.get("user_full_name")); //起票者名
			map.put("KihyouShozokuUserShainNo",map.get("uiShainNo")); //社員番号（起票者）
			map.put("gen_bumon_full_name",((String)map.get("gen_bumon_full_name")).replaceAll("｜", "\r\n")); //所有所属部門
			map.put("gen_name",((String)map.get("gen_name")).replaceAll("｜", "\r\n")); //所有者名
			map.put("shiharaiBi",formatDate(map.get("shiharaiBi"))); //支払日
			map.put("shiharaiKiboubi",formatDate(map.get("shiharaiKiboubi"))); //支払希望日
			map.put("shouninbi",formatTime(map.get("shouninbi"))); //承認日

			//支払方法
			map.put("shiharaihouhou", shiharaiHouhouMap.get(map.get("shiharaihouhou")));
			map.put("houjin_kingaku", formatMoney(map.get("houjin_kingaku"))); //法人カード利用金額
			map.put("sashihiki_shikyuu_kingaku", formatMoney(map.get("sashihiki_shikyuu_kingaku"))); //差引支給金額
			map.put("keijoubi",formatDate(map.get("keijoubi"))); //計上日(画面表示は使用日)

			//仕訳計上日(画面表示は計上日)、仕訳作成方法を設定できる場合、設定により表示非表示を制御する。（発生：表示 現金：非表示）
			//仕訳作成方法を設定できる且つそれが"現金主義"の場合は画面に表示しないため削除する。
			trimShiwakeKeijoubi(map);
			map.put("shiwakeKeijoubi",formatDate(map.get("shiwakeKeijoubi")));

			map.put("seisan_yoteibi",formatDate(map.get("seisan_yoteibi"))); //精算予定日
			// 仮払伝票用の伝票区分を生成
			String karibaraiDenpyouId = (String)map.get("karibarai_denpyou_id");
			if (!StringUtils.isEmpty(karibaraiDenpyouId) && 11 <= karibaraiDenpyouId.length()) {
				String karibaraiKbn = karibaraiDenpyouId.substring(7, 11);
				map.put("karibarai_denpyou_kbn", karibaraiKbn);
				if(karibaraiKbn.equals(DENPYOU_KBN.KARIBARAI_SHINSEI)){
					map.put("karibarai_Url", KARIBARAI_URL);
				} else if (karibaraiKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI)) {
					map.put("karibarai_Url", RYOHI_KARIBARAI_URL);
				} else if (karibaraiKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI)) {
					map.put("karibarai_Url", KAIGAI_RYOHI_KARIBARAI_URL);
				}
			}

			map.put("serialNo", String.format("%1$08d", map.getObject("serial_no"))); // 伝票番号

			//出張先・訪問先は先頭の２０文字までを出力
			int lenH = 20;
			String houmonsaki = (String)map.get("houmonsaki");
			//改行コードが存在する場合、コードを「\n」に統一した上で半角スぺースに置換する。
			houmonsaki = houmonsaki.replace("\r\n", "\n").replace("\n", " ");
			if( houmonsaki != null && houmonsaki.length() > lenH ){
				houmonsaki = houmonsaki.substring(0, lenH);
			}

			//目的は先頭の２０文字までを出力
			int lenM = 20;
			String mokuteki = (String)map.get("mokuteki");
			if( mokuteki != null && mokuteki.length() > lenM ){
				mokuteki = mokuteki.substring(0, lenM);
			}

			map.put("houmonsaki", houmonsaki); // 出張先・訪問先
			map.put("mokuteki", mokuteki); // 目的
			map.put("kanitodoke_kenmei", map.get("kenmei")); //ユーザー定義届書の件名

			//ユーザー定義届書最初の内容データのみ取得
			if(map.get("naiyou") != null){
				String str = (String)map.get("naiyou");
				if(str.indexOf("╂") >= 0){str = str.substring(0, str.indexOf("╂"));};
				map.put("naiyou", str);
			}
			map.put("kanitodoke_naiyou", map.get("naiyou")); //ユーザー定義届書の内容
			//B伝票かつマル秘かつ表示しない→金額のみ  B伝票かつマル秘かつ表示する　→naiyou kenmei kingaku
			if(map.get("denpyou_kbn").toString().contains("B") && maruhiFlg.equals("1")) {
				//明細の非表示処理
				String shinsei = "";
				String meisai = "";
				for(int i = 1;i< 31;i++) {
					shinsei = "shinsei"+String.format("%02d",i);
					meisai  = "meisai" +String.format("%02d",i);
					map.put(meisai,"");
					if(kaniItem[0].equals(shinsei)){
						continue;
					}
					if(maruhiHyoujiFlg.equals("1") && (kaniItem[1].equals(shinsei) || kaniItem[2].equals(shinsei))) {
						continue;
					}
					map.put(shinsei,"");
				}
				if(maruhiHyoujiFlg.equals("0")) {
					//kanitodoke_naiyouとkanitodoke_kenmeiも空白に
					map.put("kanitodoke_kenmei", "");
					map.put("kanitodoke_naiyou", "");
				}
			}

			//使用者
			if(null == map.get("user_sei") || isEmpty((String)map.get("user_sei")) ) {
				map.put("user_name", "");
			}else{
				map.put("user_name", map.get("user_sei") + "　" + map.get("user_mei"));
			}
			//精算期間
			if(null == map.get("seisankikan_from")){
				map.put("seisankikan", "");
			}else{
				map.put("seisankikan", formatDate(map.get("seisankikan_from")) + "～" + formatDate(map.get("seisankikan_to")));
			}
			
			// 消費税率
			map.put("formattedZeiritsu", isEmpty(map.get("zeiritsu")) ? "" : map.getString("zeiritsu").replace(",", "\r\n"));
			//事業者区分がnull、もしくはインボイス対応前伝票の場合は事業者区分空白
			if(null == map.get("jigyousha_kbn") || map.get("invoice_denpyou") == null || map.get("invoice_denpyou").equals("1")) {
				map.put("jigyoushaKbn", "");
			}else {
				//事業者区分 Jdbc4Arrayは文字列ではないためisEmpty()が使えない　冗長だけど一旦　インボイス対応後、時間があればやり直し
				var ara = map.get("jigyousha_kbn").toString().replace("{", "").replace("}", "").replace(",", "");
				map.put("jigyoushaKbn", isEmpty(ara) ? "" : map.get("jigyousha_kbn").toString().replace("{", "").replace("}", "").replace("1", "免税").replace("0", "通常").replace(",", "\r\n"));
			}

			// カスタマイズ用追加部分
			// カスタマイズ用データ追加実行
			if(this.denpyouIchiranActionStrategy != null)
			{
				this.denpyouIchiranActionStrategy.mapForJsp = map;
				this.denpyouIchiranActionStrategy.getProcessedJspMap();
			}
		}
	}

	/**
	 * 仕訳作成方法の設定値で計上日の表示非表示を判断する。
	 * 発生主義…表示　現金主義…非表示
	 * 現金主義の場合はこちらでmapから計上日情報を削除する。
	 * @param map レコード情報
	 */
	protected void trimShiwakeKeijoubi(GMap map) {
		String shiwakeInfo = null;
		String denpyouKbn = map.getString("denpyou_kbn");
		switch(denpyouKbn){
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			shiwakeInfo = setting.shiwakeSakuseiHouhouA001();
			break;

		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			shiwakeInfo = setting.shiwakeSakuseiHouhouA009();
			break;

		case DENPYOU_KBN.RYOHI_SEISAN:
			shiwakeInfo = setting.shiwakeSakuseiHouhouA004();
			break;

		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			shiwakeInfo = setting.shiwakeSakuseiHouhouA011();
			break;

		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			shiwakeInfo = setting.shiwakeSakuseiHouhouA010();
			break;
		}
		if("3".equals(shiwakeInfo))  {
			map.remove("shiwakeKeijoubi");
		}
	}
	
	/**
	 * 検索用パラメーターを集約したクラスを作成する
	 * @return パラメータークラス
	 */
	protected DenpyouKensakuQueryParameter createQueryParameter()
	{
		String cnvKianBangou = kianBangou;
		if (super.isNotEmpty(kianBangou)){
			cnvKianBangou = WorkflowEventControlLogic.convKianbangouToZenkaku(Integer.valueOf(kianBangou));
		}
		
		DenpyouKensakuQueryParameter denpyouKensakuQueryParameter = new DenpyouKensakuQueryParameter();
		denpyouKensakuQueryParameter.user = getUser();
		denpyouKensakuQueryParameter.kanrenDenpyou = kanrenDenpyou;
		denpyouKensakuQueryParameter.denpyouId = kensakuDenpyouId;
		denpyouKensakuQueryParameter.serialNo = toLong(kensakuSerialNo);
		denpyouKensakuQueryParameter.denpyouJoutai = kensakuJoutai;
		denpyouKensakuQueryParameter.gyoumuShubetsu = kensakuGyoumuShubetsu;
		denpyouKensakuQueryParameter.denpyouShubetsu = kensakuDenpyouShubetsu;
		denpyouKensakuQueryParameter.kianBangouNendo = kianBangouNendo;
		denpyouKensakuQueryParameter.kianBangouRyakugou = kianBangouRyakugou;
		denpyouKensakuQueryParameter.kianBangou = cnvKianBangou;
		denpyouKensakuQueryParameter.kianBangouRyakugouFromTo = kianBangouRyakugouFromTo;
		denpyouKensakuQueryParameter.kianBangouFrom = kianBangouFrom;
		denpyouKensakuQueryParameter.kianBangouTo = kianBangouTo;
		denpyouKensakuQueryParameter.kianBangouInput = kianBangouInput;
		denpyouKensakuQueryParameter.kianBangouEnd = kianBangouEnd;
		denpyouKensakuQueryParameter.kianBangouUnyou = kianBangouUnyou;
		denpyouKensakuQueryParameter.kianBangouNoShishutsuIrai = kianBangouNoShishutsuIrai;
		denpyouKensakuQueryParameter.yosanshikkouShubetsu = yosanshikkouShubetsu;
		denpyouKensakuQueryParameter.yosanCheckNengetsuFrom = yosanCheckNengetsuFrom;
		denpyouKensakuQueryParameter.yosanCheckNengetsuTo = yosanCheckNengetsuTo;
		denpyouKensakuQueryParameter.kihyouBiFrom = toDate(kihyouBiFrom);
		denpyouKensakuQueryParameter.kihyouBiTo = toDate(kihyouBiTo);
		denpyouKensakuQueryParameter.kihyouShozokuBumonCd = kensakuKihyouShozokuBumonCd;
		denpyouKensakuQueryParameter.kihyouShozokuBumonName = kensakuKihyouShozokuBumonName;
		denpyouKensakuQueryParameter.kihyouShozokuUserShainNo = kensakuKihyouShozokuUserShainNo;
		denpyouKensakuQueryParameter.kihyouShozokuUserName = kensakuKihyouShozokuUserName;
		denpyouKensakuQueryParameter.shouninBiFrom = toDate(shouninBiFrom);
		denpyouKensakuQueryParameter.shouninBiTo = toDate(shouninBiTo);
		denpyouKensakuQueryParameter.shouninsyaShozokuBumonCd = shouninsyaShozokuBumonCd;
		denpyouKensakuQueryParameter.shouninsyaShozokuBumonName = shouninsyaShozokuBumonName;
		denpyouKensakuQueryParameter.shouninsyaShozokuUserShainNo = shouninsyaShozokuUserShainNo;
		denpyouKensakuQueryParameter.shouninsyaShozokuUserName = shouninsyaShozokuUserName;
		denpyouKensakuQueryParameter.shoyuushaShozokuBumonCd = shoyuushaShozokuBumonCd;
		denpyouKensakuQueryParameter.shoyuushaShozokuBumonName = shoyuushaShozokuBumonName;
		denpyouKensakuQueryParameter.shoyuushaShozokuUserShainNo = shoyuushaShozokuUserShainNo;
		denpyouKensakuQueryParameter.shoyuushaShozokuUserName = shoyuushaShozokuUserName;
		denpyouKensakuQueryParameter.tekiyou = tekiyou;
		denpyouKensakuQueryParameter.kingakuFrom = toDecimal(kingakuFrom);
		denpyouKensakuQueryParameter.kingakuTo = toDecimal(kingakuTo);
		denpyouKensakuQueryParameter.meisaiKingakuFrom = toDecimal(meisaiKingakuFrom);
		denpyouKensakuQueryParameter.meisaiKingakuTo = toDecimal(meisaiKingakuTo);
		denpyouKensakuQueryParameter.queryZeiritsu = this.queryZeiritsu;
		denpyouKensakuQueryParameter.keigenZeiritsuKbn = this.keigenZeiritsuKbn;
		denpyouKensakuQueryParameter.houjinCardRiyou = houjinCardRiyou;
		denpyouKensakuQueryParameter.kaishaTehai = kaishaTehai;
		denpyouKensakuQueryParameter.maruhi = maruhi;
		denpyouKensakuQueryParameter.shiharaiBiFrom = toDate(shiharaiBiFrom);
		denpyouKensakuQueryParameter.shiharaiBiTo = toDate(shiharaiBiTo);
		denpyouKensakuQueryParameter.shiharaiKiboubiFrom = toDate(shiharaiKiboubiFrom);
		denpyouKensakuQueryParameter.shiharaiKiboubiTo = toDate(shiharaiKiboubiTo);
		denpyouKensakuQueryParameter.keijoubiFrom = toDate(keijoubiFrom);
		denpyouKensakuQueryParameter.keijoubiTo = toDate(keijoubiTo);
		denpyouKensakuQueryParameter.shiwakeKeijoubiFrom = toDate(shiwakeKeijoubiFrom);
		denpyouKensakuQueryParameter.shiwakeKeijoubiTo = toDate(shiwakeKeijoubiTo);
		denpyouKensakuQueryParameter.karikataBumonCdFrom = karikataBumonCdFrom;
		denpyouKensakuQueryParameter.karikataBumonCdTo = karikataBumonCdTo;
		denpyouKensakuQueryParameter.kashikataBumonCdFrom = kashikataBumonCdFrom;
		denpyouKensakuQueryParameter.kashikataBumonCdTo = kashikataBumonCdTo;
		denpyouKensakuQueryParameter.karikataKamokuCdFrom = karikataKamokuCdFrom;
		denpyouKensakuQueryParameter.karikataKamokuCdTo = karikataKamokuCdTo;
		denpyouKensakuQueryParameter.kashikataKamokuCdFrom = kashikataKamokuCdFrom;
		denpyouKensakuQueryParameter.kashikataKamokuCdTo = kashikataKamokuCdTo;
		denpyouKensakuQueryParameter.karikataKamokuEdanoCdFrom = karikataKamokuEdanoCdFrom;
		denpyouKensakuQueryParameter.karikataKamokuEdanoCdTo = karikataKamokuEdanoCdTo;
		denpyouKensakuQueryParameter.kashikataKamokuEdanoCdFrom = kashikataKamokuEdanoCdFrom;
		denpyouKensakuQueryParameter.kashikataKamokuEdanoCdTo = kashikataKamokuEdanoCdTo;
		denpyouKensakuQueryParameter.karikataTorihikisakiCdFrom = karikataTorihikisakiCdFrom;
		denpyouKensakuQueryParameter.karikataTorihikisakiCdTo = karikataTorihikisakiCdTo;
		denpyouKensakuQueryParameter.kashikataTorihikisakiCdFrom = kashikataTorihikisakiCdFrom;
		denpyouKensakuQueryParameter.kashikataTorihikisakiCdTo = kashikataTorihikisakiCdTo;
		denpyouKensakuQueryParameter.bumonSentakuFlag = bumonSentakuFlag;
		denpyouKensakuQueryParameter.ryoushuushoSeikyuushoTou = ryoushuushoSeikyuushoTou;
		denpyouKensakuQueryParameter.miseisanKaribarai = miseisanKaribarai;
		denpyouKensakuQueryParameter.miseisanUkagai = miseisanUkagai;
		denpyouKensakuQueryParameter.tenpuFileFlg = tenpuFileFlg;
		denpyouKensakuQueryParameter.tenpuFileShubetsu = tenpuFileShubetsu;
		denpyouKensakuQueryParameter.ebunshoShubetsu = ebunshoShubetsu;
		denpyouKensakuQueryParameter.ebunshoNengappiFrom = toDate(ebunshoNengappiFrom);
		denpyouKensakuQueryParameter.ebunshoNengappiTo = toDate(ebunshoNengappiTo);
		denpyouKensakuQueryParameter.ebunshoNengappiMinyuuryokuFlg = ebunshoNengappiMinyuuryokuFlg;
		denpyouKensakuQueryParameter.ebunshoKingakuFrom = toDecimal(ebunshoKingakuFrom);
		denpyouKensakuQueryParameter.ebunshoKingakuTo = toDecimal(ebunshoKingakuTo);
		denpyouKensakuQueryParameter.ebunshoKingakuMinyuuryokuFlg = ebunshoKingakuMinyuuryokuFlg;
		denpyouKensakuQueryParameter.ebunshoHakkousha = ebunshoHakkousha;
		denpyouKensakuQueryParameter.ebunshoHakkoushaMinyuuryokuFlg = ebunshoHakkoushaMinyuuryokuFlg;
		denpyouKensakuQueryParameter.shiwakeStatus = shiwakeStatus;
		denpyouKensakuQueryParameter.fbStatus = fbStatus;
		denpyouKensakuQueryParameter.bumonCdFrom = bumonCdFrom;
		denpyouKensakuQueryParameter.bumonCdTo = bumonCdTo;
		denpyouKensakuQueryParameter.kanitodokeKenmei = kanitodokeKenmei;
		denpyouKensakuQueryParameter.kanitodokeNaiyou = kanitodokeNaiyou;
		denpyouKensakuQueryParameter.torisageHininHyoujiFlg = torisageHininHyoujiFlg;
		denpyouKensakuQueryParameter.jigyoushaKbn = jigyoushaKbn;
		denpyouKensakuQueryParameter.shiharaiName = shiharaiName;
		denpyouKensakuQueryParameter.zeinukiKingakuFrom = toDecimal(zeinukiKingakuFrom);
		denpyouKensakuQueryParameter.zeinukiKingakuTo = toDecimal(zeinukiKingakuTo);
		denpyouKensakuQueryParameter.zeinukiMeisaiKingakuFrom = toDecimal(zeinukiMeisaiKingakuFrom);
		denpyouKensakuQueryParameter.zeinukiMeisaiKingakuTo = toDecimal(zeinukiMeisaiKingakuTo);
		return denpyouKensakuQueryParameter;
	}
}
