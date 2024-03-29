package eteam.gyoumu.workflow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EbunshoLogic;
import eteam.common.EbunshoLogic.StampedPdf;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.ringiChoukaShounintou;
import eteam.common.EteamIO;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KIDOKU_FLG;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
import eteam.common.PdfImageChecker;
import eteam.common.SaibanLogic;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.dao.EbunshoDataDao;
import eteam.database.dao.EbunshoFileDao;
import eteam.database.dao.EbunshoKensakuDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NiniCommentDao;
import eteam.database.dao.TenpuFileDao;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.gyoumu.houjincard.HoujinCardLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.tsuuchi.DenpyouIchiranUpdateLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControlLogic.WfSeigyo;
import eteam.gyoumu.workflow.WorkflowEventControlLogic.kianShuuryou;
import eteam.gyoumu.workflow.WorkflowEventControlLogicBase.Ebunsho;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import eteam.gyoumu.yosanshikkoukanri.kogamen.KianTsuikaLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ワークフローイベント制御クラス
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class WorkflowEventControl extends EteamAbstractAction {

	/** 関連伝票の添付上限値 */
	protected final int KANREN_MAX = 10;

	/** 1mbサイズ */
	protected static int MBSIZE_BYTE = 1048576;

//＜定数＞
	/** イベントコード(このAction内だけの話) */
	public enum Event {
		//申請以降のイベントコードは内部コード設定テーブル（joukyou)のコード値にリンクしている。それ以外のイベントコードは0としている。
		//202207 承認解除追加　内部コードはまだ更新していないので注意
		/** 表示 */ INIT(0),
		/** 登録 */ TOUROKU(0),
		/** 更新 */ KOUSHIN(0),
		/** ファイル削除 */ FILE_DELETE(0),
		/** 申請 */ SHINSEI(1),
		/** 取戻し */ TORIMODOSHI(2),
		/** 取下げ */ TORISAGE(3),
		/** 承認 */ SHOUNIN(4),
		/** 否認 */ HININ(5),
		/** 差戻し */ SASHIMODOSHI(6),
		/** 承認解除 */ SHOUNINKAIJO(7),
		/** マル秘文書フラグ設定 */ MARUHISETTEI(0),
		/** マル秘文書フラグ解除 */ MARUHIKAIJYO(0);


		/** コード値 */
		protected final int code;

		/**
		 * NEW
		 * @param code コード
		 */
		private Event(int code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return Integer.toString(this.code);
		}
	}

//＜画面入力＞

	//画面表示キー（表示イベントのパラメータで、全イベントで必ずhidden入力あり。ただし、sanshouDenpyouIdは参照起票時のみ、versionはユーザー定義届書の場合のみ）
	/** 伝票ID */
	protected String denpyouId;
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 参照伝票ID */
	protected String sanshouDenpyouId;
	/** バージョン */
	protected String version;

	//各種伝票と共有する入力
	/** 金額 */
	protected String kingaku;
	/** 基準日 */
	protected String kijunbi;

	//プルダウンからの入力（起票モードで兼務ユーザーの場合のみプルダウン表示→入力あり。それ以外はイベント内でデータ取得してセット→表示）
	/** 起票部門コード */
	String kihyouBumonCd;
	/** 起票部門 */
	String kihyouBumonName;
	/** 起票部門ロールID */
	String bumonRoleId;
	/** 起票部門ロール名 */
	String bumonRoleName;
	/** 代表負担部門コード */
	protected String daihyouFutanBumonCd;

	//ダイアログによる入力
	/** コメント */
	String comment;
	/** 任意メモ */
	String niniMemo;
	/** 差戻し先枝番号 */
	String sashimodoshiSakiEdano;
	/** 差戻し先合議枝番号 */
	String sashimodoshiSakiGougiEdano;
	/** 差戻し先合議枝枝番号 */
	String sashimodoshiSakiGougiEdaedano;
	/** 差戻しコメント */
	String sashimodoshiComment;
	/** マル秘扱い */
	String maruhiFlg;

	//ファイル添付時の入力
	/** 添付ファイルオブジェクト */
	File[] uploadFile;
	/** 添付ファイルコンテンツタイプ */
	String[] uploadFileContentType;
	/** 添付ファイル名 */
	String[] uploadFileFileName;

	//e文書明細入力時の入力
	/** e文書明細行数(エラー時内容再出力用) */
	String[] ebunsho_meisaicnt;
	/** e文書使用フラグ */
	String[] ebunshoflg;
	/** 電子取引フラグ */
	String[] denshitorihikiFlg;
	/** タイムスタンプ付与フラグ */
	String[] tsfuyoFlg;


	/** e文書種別 */
	String[] ebunsho_shubetsu;
	/** e文書明細年月日 */
	String[] ebunsho_nengappi;
	/** e文書明細金額 */
	String[] ebunsho_kingaku;
	/** e文書明細品名 */
	String[] ebunsho_hinmei;
	/** e文書明細発行者 */
	String[] ebunsho_hakkousha;
	/** e文書添付ファイル名 */
	String[] ebunsho_tenpufilename;

	/** 年月日任意フラグ */
	Short[] nengappi_nini_flg;
	/** 金額任意フラグ */
	Short[] kingaku_nini_flg;
	/** 発行者任意フラグ */
	Short[] hakkousha_nini_flg;

	/** 新規追加添付ファイル用e文書添付ファイル名 */
	String[] ebunsho_tenpufilename_header;

	/** 添付済みファイル用e文書枝番(エラー時内容再出力用) */
	String[] tenpuzumi_ebunsho_edano;
	/** 添付済みファイル用電子取引フラグ */
	String[] tenpuzumi_denshitorihikiFlg;
	/** 添付済みファイル用タイムスタンプ付与フラグ */
	String[] tenpuzumi_tsfuyoFlg;
	/** 添付済みファイル用e文書明細枝番 */
	String[] tenpuzumi_edano;
	/** 添付済みファイル用e文書元添付ファイル名 */
	String[] tenpuzumi_filename;
	/** 添付済みファイル用e文書データ行数 */
	String[] tenpuzumi_ebunshodata_count;

	/** 添付済みファイル用e文書番号 */
	String[] tenpuzumi_ebunsho_no;
	/** 添付済みファイル用e文書種別 */
	String[] tenpuzumi_ebunsho_shubetsu;
	/** 添付済みファイル用e文書明細年月日 */
	String[] tenpuzumi_ebunsho_nengappi;
	/** 添付済みファイル用e文書明細金額 */
	String[] tenpuzumi_ebunsho_kingaku;
	/** 添付済みファイル用e文書明細品名 */
	String[] tenpuzumi_ebunsho_hinmei;
	/** 添付済みファイル用e文書明細発行者 */
	String[] tenpuzumi_ebunsho_hakkousha;
	/** e文書タイムスタンプ登録日時(エラー時内容再出力用) */
	String[] tenpuzumi_ebunsho_tourokutime;
	
	// 添付ファイルのプレビュー用
	/** 添付ファイル名 */
	String[] tenpuFileName;
	/** 添付ファイル拡張子 */
	String[] tenpuFileExtension;
	/** 添付ファイル拡張子（URL用） */
	String[] tenpuFileURLExtension;
	/** 伝票区分・承認者・会社設定を考慮してプレビュー対象伝票であるか */
	boolean isPreviewTargetDenpyou = false;
	
	/**
	 * strutsにおいて、booleanのgetterはisXXXというバグ仕様なのでとりあえず対策
	 * フラグによる方法も一長一短なので、今後どっちを正式にするか検討したいところ
	 * @return isPreviewTargetDenpyou
	 */
	public boolean isIsPreviewTargetDenpyou()
	{
		return this.isPreviewTargetDenpyou;
	}
	
	//ダウンロードイベントのパラメータ
	/** ダウンロードファイルナンバー */
	String downloadFileNo;
	/** ダウンロードe文書番号 */
	String downloadEbunshoNo;

	//e文書タイムスタンプ再付与イベントのパラメータ
	/** タイムスタンプ再付与e文書番号 */
	String addTimestampEbunshoNo;

	/** 添付済みファイルe文書フラグ */
	String[] tenpuzumi_ebunshoflg;
	//予算チェック関連
	/** 予算執行対象月 */
	String yosanCheckNengetsu;

//＜画面入力以外＞
	//表示
	/** タイトル */
	String title;
	/** シリアル番号 */
	String serialNo;
	/** 伝票レコード */
	GMap denpyouMap;
	/** 伝票状態 */
	String denpyouJoutai;
	/** 伝票状態名 */
	String denpyouJoutaiMei;
	/** 起票者 */
	String kihyouUser;
	/** 起票日 */
	String kihyouBi;
	/** 承認者 */
	String shouninSha;

	//制御
	/** リダイレクト用URLパス(denpyou_shubetsu.denpyou_shubetsu_url */
	String urlPath;
	/** 制御情報 */
	protected WfSeigyo wfSeigyo;
	/** e文書情報表示フラグ */
	String ebunshoJouhouHyoujiFlg;
	/** e文書使用フラグ */
	String ebunshoShiyouFlg;
	/** e文書を有効にする(品名項目表示制御用) */
	String ebunshoEnableFlg = setting.ebunshoEnableFlg();

	/** e文書対象チェックの初期値設定保持用 */
	String ebunshoTaishouCheckDefault;
	/** 電子取引のTS付与チェックの初期値設定保持用 */
	String ebunshoDenshitsCheckDefault;

	/** 申請時帳票出力フラグ */
	String printFlg = "FALSE";
	
	/** 消費税額修正ボタン表示フラグ */
	String zeigakuShuseiFlg;
	/** 入力方式初期値フラグ */
	String nyuryokuDefaultFlg;
	/** 入力方式変更フラグ */
	String nyuryokuHenkouFlg;
	/**	事業者区分変更フラグ */
	String jigyoushaKbnHenkouFlg;
	/**	インボイス制度開始フラグ */
	protected String invoiceStartFlg;
	/**	税額端数処理フラグ */
	String hasuuShoriFlg;
	/** インボイス伝票 */
	protected String invoiceDenpyou;

	//インボイス制御
	/** インボイス対応伝票設定項目使用設定フラグ */
	String invoiceSetteiFlg = setting.invoiceSetteiFlg();

	//プルダウン候補
	/** 起票部門リスト */
	List<GMap> kihyouBumonList;
	/** e文書書類種別リスト  */
	List<GMap> ebushoShubetsuList;
	/** 予算執行対象月リスト */
	List<GMap> monthList;

	//表示・制御データ
	/** 承認ルートリスト(ALL) */
	List<GMap> shouninRouteList;
	/** 承認ルート(処理ユーザー) */
	GMap procShouninRoute;
	/** 承認ルートリスト(差戻先候補) */
	List<GMap> sashimodoshiSakiList;
	/** 承認状況リスト */
	List<GMap> shouninJoukyouList;
	/** 操作履歴 */
	List<GMap> sousaList;
	/** 任意メモリスト */
	List<GMap> niniMemoList;
	/** 表示用の添付ファイルリスト */
	List<GMap> tempFileList;
	/** 表示用の添付ファイルリスト(e文書明細追加) */
	List<GMap> tempFileEbunshoList;
	/** 表示用の関連伝票 空白スペース */
	String[] hyoujiKanrenEmbedSpace;
	/** 表示用の関連伝票 伝票区分 */
	String[] hyoujiKanrenDenpyouKbn;
	/** 表示用の関連伝票 伝票種別URL */
	String[] hyoujiKanrenDenpyouShubetsuUrl;
	/** 表示用の関連伝票 伝票URL */
	String[] hyoujiKanrenDenpyouUrl;
	/** 表示用の関連伝票 伝票種別 */
	String[] hyoujiKanrenDenpyouShubetsu;
	/** 表示用の関連伝票 伝票ID */
	String[] hyoujiKanrenDenpyouId;
	/** 表示用の関連伝票 添付 ファイル名*/
	String[] hyoujiKanrenTenpuFileName;
	/** 表示用の関連伝票 添付 ファイルURL*/
	String[] hyoujiKanrenTenpuFileUrl;
	/** 表示用の関連伝票 起票日 */
	String[] hyoujiKanrenKihyouBi;
	/** 表示用の関連伝票 承認日 */
	String[] hyoujiKanrenShouninBi;
	/** 取得用の関連伝票 伝票ID */
	String[] kanrenDenpyouId;
	/** 取得用の関連伝票 伝票区分 */
	String[] kanrenDenpyouKbn;
	/** 取得用の関連伝票 起票日 */
	String[] kanrenTourokuTime;
	/** 取得用の関連伝票 承認日 */
	String[] kanrenShouninTime;
	/** 関連伝票表示フラグ */
	boolean kanrenFlg = false;
	/** 関連伝票データ数 */
	int kanrenCount = 1;
	/** 表示用の稟議金額引継ぎフラグ*/
	protected String[] ringiKingakuHikitsugiFlg;

	/*
	 * 起案番号運用関連（表示制御用）
	 */
	/** 起案番号簿選択ダイアログ表示フラグ（0:表示しない 1:表示する） */
	String isDispKianbangouBoDialog = "0";
	/** 起案番号項目表示フラグ（0:表示しない 1:表示する 2:アンカー表示のみ） */
	protected String isDispKianbangou = "0";
	/** 起案番号項目表示データ（東商財発 ９９９号） */
	String kianBangou = "";
	/** 起案添付セクション表示フラグ（0:表示しない 1:表示する） */
	protected String isDispKiantenpuSection = "0";
	/** 起案伝票紐付けダイアログ表示フラグ（0:表示しない 1:表示する） */
	protected String kianHimodukeDialogFlg = "1";
	/** 起案伝票紐付け確認フラグ（0:紐付け確認しない 1:紐付け確認する） */
	protected String kianHimodukeFlg = "0";
	/** 起案終了表示フラグ（0:表示しない 1:表示する） */
	String isDispKianShuuryou = "0";
	/** 起案終了データ（0:起案終了未済 1:起案終了） */
	String kianShuuryouFlg = "";
	/*
	 * 起案番号簿選択ダイアログ選択値
	 */
	/** 起案番号簿選択ダイアログ選択値（部門コード） */
	String kianbangouBoDialogBumonCd = "";
	/** 起案番号簿選択ダイアログ選択値（年度） */
	protected String kianbangouBoDialogNendo = "";
	/** 起案番号簿選択ダイアログ選択値（略号） */
	String kianbangouBoDialogRyakugou = "";
	/** 起案番号簿選択ダイアログ選択値（開始起案番号） */
	String kianbangouBoDialogKianbangouFrom = "";
	/*
	 * 起案追加
	 */
	/** 表示用の起案伝票 空白スペース */
	String[] hyoujiKianEmbedSpace;
	/** 表示用の起案伝票 伝票区分 */
	String[] hyoujiKianDenpyouKbn;
	/** 表示用の起案伝票 起案番号 */
	String[] hyoujiKianbangou;
	/** 表示用の起案伝票 件名 */
	String[] hyoujiKianKenmei;
	/** 表示用の起案伝票 伝票種別URL */
	String[] hyoujiKianDenpyouShubetsuUrl;
	/** 表示用の起案伝票 伝票URL */
	String[] hyoujiKianDenpyouUrl;
	/** 表示用の起案伝票 伝票種別 */
	String[] hyoujiKianDenpyouShubetsu;
	/** 表示用の起案伝票 伝票ID */
	String[] hyoujiKianDenpyouId;
	/** 表示用の起案伝票 添付 ファイル名*/
	String[] hyoujiKianTenpuFileName;
	/** 表示用の起案伝票 添付 ファイルURL*/
	String[] hyoujiKianTenpuFileUrl;
	/** 取得用の起案伝票 伝票ID */
	String[] kianDenpyouId;
	/** 取得用の起案伝票 伝票区分 */
	String[] kianDenpyouKbn;
	/** 起案伝票表示フラグ */
	boolean kianFlg = false;
	/** 起案伝票データ数 */
	int kianCount = 1;

	/*
	 * 予算執行
	 */
	/** 予算執行対象月表示フラグ（0:表示しない 1:表示する） */
	String isDispYosanCheckSection = "0";
	/** 予算執行対象外判定（部門科目） */
	boolean yosanCheckTaishougaiFlg = false;


	// 稟議金額
	/** 稟議金額ラベル文言 */
	String ringiKingakuName;
	/** 稟議金額 */
	String ringiKingaku;
	/** 稟議金額残高 */
	String ringiKingakuZandaka;
	/** 稟議金額超過コメント */
	protected String ringiKingakuChoukaComment;
	/** 稟議金額超過コメント（表示用） */
	String hyoujiRingiKingakuChoukaComment;
	/** 稟議金額超過コメント必須フラグ */
	String ringiKingakuChoukaCommentHissuFlg;
	/** 稟議金額超過コメント表示フラグ */
	String ringiKingakuChoukaCommentHyoujiFlg;
	/** 関連伝票稟議引継ぎ有無フラグ */
	String kanrenDenpyouRingiHikitsugiUmFlg;
	/** 仮払伝票稟議引継ぎ有無フラグ */
	String karibaraiRingiHikitsugiUmFlg;
	/** 稟議金額超過判定% */
	String ringiChoukaHantei = setting.ringiChoukaHantei();

	//ファイルダウンロードイベント専用
	/** ファイル名 */
	String fileName;
	/** コンテンツタイプ */
	String contentType;
	/** コンテンツディスポジション */
	String contentDisposition;
	/** ダウンロードファイル用 */
	InputStream inputStream;
	/** PDFファイル用*/
	protected FileOutputStream fos;

	/** 画面URL */
	protected String qrText;
	/** 遡及日適用フラグ */
	protected boolean sokyuuFlg;
	/** 遡及日適用会社設定 */
	String sokyuuSettei = setting.sokyuInsatsu();
	
	//インボイス対応
	/** 消費税額修正ボタン押下可否 */
	String shouhizeigakuShuusei;
	/** 消費税区分 */
	int shouhizeikbn;
	/** 消費税額按分法 */
	int shiireZeiAnbun;
	/** 仕入税額経過措置 */
	String shiirezeigakuKeikasothi;
	/** 売上税額計算方式 */
	String uriagezeigakuKeisan;
	
	/** 摘要文字数 */
	String tekiyouMaxLength;

//＜セッション情報＞
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザーID */
	protected String loginUserId;
	/** ログイン業務ロールID */
	protected String loginGyoumuRoleId;
	/** ユーザーフル名 */
	protected String loginUserFullName;

//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** システムカテゴリロジック */
	protected SystemKanriCategoryLogic systemLogic;
	/** ワークフローカテゴリロジック */
	WorkflowCategoryLogic workflowLogic;
	/** ワークフローイベント */
	protected WorkflowEventControlLogic myLogic;
	/** マスターカテゴリー */
	MasterKanriCategoryLogic masterLg;
	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommonLogic;
	/** 部門・ユーザー管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** 採番 */
	SaibanLogic saibanLogic;
	/** E文書 */
	EbunshoLogic eLogic;
	/** ユーザー定義届書カテゴリ SELECT */
	KaniTodokeCategoryLogic kaniTodokeCategoryLogic;
	/** 伝票管理Logic */
	DenpyouKanriLogic denpyouKanriLogic;
	/** ワークフローイベント制御Logic */
	KianTsuikaLogic kianTsuikaLogic;
	/** 会計カテゴリー内のSelect文を集約したLogic */
	KaikeiCategoryLogic kaikeiCategoryLogic;
	/** 予算執行管理共通Logic */
	YosanShikkouKanriCategoryLogic yosanKanriLogic;
	/** 伝票一覧テーブルロジック */
	DenpyouIchiranUpdateLogic diLogic;
	/** 法人カード使用履歴Logic */
	HoujinCardLogic hcLogic;
	/** 起票ナビカテゴリロジック */
	KihyouNaviCategoryLogic kihyouNaviLogic;
	/** DAO */
	TenpuFileDao tenpuFileDao;
	/** DAO */
	EbunshoFileDao ebunshoFileDao;
	/** DAO */
	EbunshoDataDao ebunshoDataDao;
	/** DAO */
	NiniCommentDao niniCommentDao;
	/** DAO */
	EbunshoKensakuDao ebunshoKensakuDao;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouId, 19, 19, "伝票ID", true); // KEY
		checkString(denpyouKbn, 4,  4, "伝票区分", true); // KEY
		checkString(comment, 1,  400, "コメント", true); // 例外的にユーザー入力だけどEteamIllegalRequestExceptionとする。（JSチェックするのでありえないここでエラーはありえない）
		checkString(niniMemo, 1,  400, "任意メモ", true); // 例外的にユーザー入力だけどEteamIllegalRequestExceptionとする。（JSチェックするのでありえないここでエラーはありえない）
		checkNumber(sashimodoshiSakiEdano, 1,  10, "差戻し先枝番号", true); // 例外的にユーザー入力だけどEteamIllegalRequestExceptionとする。（JSチェックするのでありえないここでエラーはありえない）
		checkNumber(sashimodoshiSakiGougiEdano, 1,  10, "差戻し先合議枝番号", false);
		checkNumber(sashimodoshiSakiGougiEdaedano, 1,  10, "差戻し先合議枝枝番号", false);
		checkString(sashimodoshiComment, 1,  400, "差戻しコメント", true); // 例外的にユーザー入力だけどEteamIllegalRequestExceptionとする。（JSチェックするのでありえないここでエラーはありえない）
		checkNumber(downloadFileNo, 1,  2, "ダウンロードファイル番号", true); // ファイルダウンロード・ファイル削除のKEY
		checkString(daihyouFutanBumonCd, 1,  8, "代表負担部門コード", true); // 例外的にユーザー入力だけどEteamIllegalRequestExceptionとする。
		checkNumber(maruhiFlg, 1,  1, "マル秘文書フラグ", true); // 例外的にユーザー入力だけどEteamIllegalRequestExceptionとする。（JSチェックするのでありえないここでエラーはありえない）
		// ユーザー定義届書のバージョンチェック
		if (denpyouKbn != null && denpyouKbn.length() > 0 && denpyouKbn.substring(0, 1).equals("B")) {
			checkNumber(version, 1, 4, "バージョン", true);
		}

		if(isNotEmpty(yosanCheckNengetsu)) {
			checkDomain(yosanCheckNengetsu , getYosanCheckNengetsuDomain(), "予算チェック対象月", false);
		}
		/*202207 #116213　添付ファイルの文字数チェック（仮）
		 * 文字コードに関してはms932でOK　WFもSIASもいっしょ　*/
		if(uploadFile != null) {
			int i = 0;
			//ファイル名（拡張子無し）が160未満or以内
			try {
				for(i = 0;i < uploadFileFileName.length; i++) {
					String fileNameBase = EteamIO.getBase(uploadFileFileName[i]);
					if(160 < fileNameBase.getBytes("MS932").length) {
						errorList.add("[" + uploadFileFileName[i] + "]ファイル名が長すぎます。");
					}
				}
			}catch (UnsupportedEncodingException e1) {
				// TODO 自動生成された catch ブロック
				errorList.add("[" + uploadFileFileName[i] + "]ファイル名に使用されている文字を確認してください。");
			}
		}
			

			//e文書使用フラグが有効になっている場合e文書明細をチェック
			if("1".equals(ebunshoShiyouFlg)){
			
			//添付済みファイル部
			int i = 0;
			int notEcnt = 0;
			
			//タイムスタンプ付与フラグオンの場合、電子取引もオンであるか確認
			//※添付済みe文書の電子取引欄はチェック固定とする前提だが念のためチェック実施
			if(tenpuzumi_ebunsho_edano != null) {
				for(int k = 0; k < tenpuzumi_tsfuyoFlg.length; k++) {
					// 添付済みファイルがe文書でないのならこのチェックは不要
					if(!this.tenpuzumi_ebunshoflg[k].equals("1"))
					{
						continue;
					}
					String fileNm = this.tenpuzumi_filename[k];
					//電子取引オンの場合、拡張子PDFであるか確認
					if("1".equals(tenpuzumi_denshitorihikiFlg[k])) {
						String extension = EteamIO.getExtension(fileNm);
						if (!(EbunshoLogic.EXTENSION_PDF.equalsIgnoreCase(extension))) {
							errorList.add("[" + fileNm + "]電子取引として登録出来るのはPDF形式のファイルだけです。");
						}
					//添付ファイルの拡張子がPDF変換対象、またはPDFか確認
					}else if (! eLogic.isEbunshoOrgFile(fileNm)) {
						errorList.add("e文書用添付ファイル[" + fileNm + "]はPDF変換未対応拡張子のファイルです。"
								+ "e文書用添付ファイルには拡張子"
								+ EbunshoLogic.EXTENSIONS_EBUNSHO_STR
								+ "のファイルを添付してください。");
					}
					
					if("1".equals(tenpuzumi_tsfuyoFlg[k])) {
						if(!("1".equals(tenpuzumi_denshitorihikiFlg[k]))) {
							errorList.add("[" + tenpuzumi_ebunsho_no[k] + "]タイムスタンプ付与をONにする場合は電子取引もONにしてください。");
						}
					}
				}
			}
			if(tenpuzumi_ebunsho_nengappi != null){
				int cnt = 0;
				int lastIndexOfMeisai = 0;
				if(eLogic == null){
					eLogic = EteamContainer.getComponent(EbunshoLogic.class);
				}
				//20220531 添付済みリンクファイルがe文書に変換されることを踏まえ、formatがおかしかったらいけないので
				for(int a = 0; a < tenpuzumi_edano.length; a++) {
// String kakuchoushi = EteamIO.getExtension(tenpuzumi_filename[a]);
					/*if(!"pdf".equals(kakuchoushi) && !"bmp".equals(kakuchoushi) && !"jpeg".equals(kakuchoushi) && !"jpg".equals(kakuchoushi)) {
						notEcnt++;
					}*/
					//e文書対象かチェック
					if(tenpuzumi_ebunshoflg[a] == null || "0".equals(tenpuzumi_ebunshoflg[a])) {
						cnt++;
// if(!"pdf".equals(kakuchoushi) && !"bmp".equals(kakuchoushi) && !"jpeg".equals(kakuchoushi) && !"jpg".equals(kakuchoushi)) {
						if(!eLogic.isEbunshoOrgFile(tenpuzumi_filename[a])) {
							notEcnt++;
						}else {
							lastIndexOfMeisai++;
						}
						continue;
					}
					int lmt = Integer.parseInt(tenpuzumi_ebunshodata_count[a]);
					for( int b = lastIndexOfMeisai ; b < lastIndexOfMeisai + lmt ; b++ ) {
						int ip = b - lastIndexOfMeisai + cnt + 1 - notEcnt;
						//int ip = i + 1;
						checkDomain (tenpuzumi_ebunsho_shubetsu[b], getEbunshoShubetsuDomain(), "e文書明細"+ ip +"行目:種別", false);
						checkDate(tenpuzumi_ebunsho_nengappi[b], "e文書明細"+ ip +"行目:年月日", false);
						if (denpyouKbn.equals("A013")) {
							checkKingakuMinus(tenpuzumi_ebunsho_kingaku[b], "e文書明細"+ ip +"行目:金額", false);
						} else {
							checkKingaku(tenpuzumi_ebunsho_kingaku[b], 0L, 999999999999L, "e文書明細"+ ip +"行目:金額", false);
						}
						if (tenpuzumi_ebunsho_shubetsu[b].equals("3") && ebunshoEnableFlg.equals("1")) {
							checkString(tenpuzumi_ebunsho_hinmei[b], 1,  50, "e文書明細"+ ip +"行目:品名", false);
						}
						checkString(tenpuzumi_ebunsho_hakkousha[b], 1,  20, "e文書明細"+ ip +"行目:発行者", false);
					}
					cnt += lmt;
					lastIndexOfMeisai += lmt;
				}
				i = cnt;
			}
			//新規添付ファイル部
			if(ebunsho_tenpufilename != null){
				for(int j = 0; j < ebunsho_tenpufilename.length; j++) {

					// 添付ファイルが無い、またはe文書化対象のファイルとして指定されていないならスキップ
					String fileNm = ebunsho_tenpufilename[j];
					if ( isEmpty(fileNm) )
					{
						continue;
					}
					fileNm = new File(fileNm).getName();

					//int ip = i + j + 1;
					int ip = i + j + 1 - notEcnt;
					checkDomain (ebunsho_shubetsu[j], getEbunshoShubetsuDomain(), "e文書明細"+ ip +"行目:種別", false);
					checkDate(ebunsho_nengappi[j], "e文書明細"+ ip +"行目:年月日", false);
					if (denpyouKbn.equals("A013")) {
						checkKingakuMinus(ebunsho_kingaku[j], "e文書明細"+ ip +"行目:金額", false);
					} else {
						checkKingaku(ebunsho_kingaku[j], 0L, 999999999999L, "e文書明細"+ ip +"行目:金額", false);
					}
					if (ebunsho_shubetsu[j].equals("3")) {
						checkString(ebunsho_hinmei[j], 1,  50, "e文書明細"+ ip +"行目:品名", false);
					}
					checkString(ebunsho_hakkousha[j], 1,  20, "e文書明細"+ ip +"行目:発行者", false);
				}
			}

			if(ebunshoflg != null) {
				for(int k = 0; k < ebunshoflg.length; k++) {

					//該当添付ファイルのe文書使用フラグがOFFか、「選択されていません」の状態ならスキップ
					if ( !("1".equals(ebunshoflg[k])) )
					{
						continue;
					}
					String fileNm = ebunsho_tenpufilename_header[k];
					if (fileNm == null || fileNm.isEmpty())
					{
						continue;
					}

					if(eLogic == null){
						eLogic = EteamContainer.getComponent(EbunshoLogic.class);
					}

					//タイムスタンプ付与フラグオンの場合、電子取引もオンであるか確認
					if("1".equals(tsfuyoFlg[k])) {
						if(!("1".equals(denshitorihikiFlg[k]))) {
							errorList.add("[" + fileNm + "]タイムスタンプ付与をONにする場合は電子取引もONにしてください。");
						}
					}

					//電子取引オンの場合、拡張子PDFであるか確認
					if("1".equals(denshitorihikiFlg[k])) {
						String extension = EteamIO.getExtension(fileNm);
						if (!(EbunshoLogic.EXTENSION_PDF.equalsIgnoreCase(extension))) {
							errorList.add("[" + fileNm + "]電子取引として登録出来るのはPDF形式のファイルだけです。");
						}
					//添付ファイルの拡張子がPDF変換対象、またはPDFか確認
					}else if (! eLogic.isEbunshoOrgFile(fileNm)) {
						errorList.add("e文書用添付ファイル[" + fileNm + "]はPDF変換未対応拡張子のファイルです。"
								+ "e文書用添付ファイルには拡張子"
								+ EbunshoLogic.EXTENSIONS_EBUNSHO_STR
								+ "のファイルを添付してください。");
					}
				}

			}
		}
	}
	@Override
	protected void hissuCheck(int eventNum) {
		// E1が表示
		// E2が登録
		// E3が更新、各種WF処理、任意コメント
		// E4が任意メモ登録
		// E5がファイルダウンロード・ファイル削除
		// E6が差戻し
		String[][] commonList = {
				//項目														EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{denpyouId ,"伝票ID"					,"0", "0", "2", "2", "2", "2"}, // KEY
				{denpyouKbn ,"伝票区分"					,"2", "2", "2", "2", "2", "2"}, // KEY
				{comment ,"コメント"					,"0", "0", "0", "0", "0", "0"}, // 任意
				{niniMemo ,"任意メモ"					,"0", "0", "0", "2", "0", "0"}, // 任意メモ登録時必須（JSチェックするのでありえないここでエラーはありえない）
				{sashimodoshiSakiEdano ,"差戻し先枝番号"			,"0", "0", "0", "0", "0", "2"}, // 差戻し時必須（JSチェックするのでありえないここでエラーはありえない）
				{sashimodoshiSakiGougiEdano ,"差戻し先合議枝番号"		,"0", "0", "0", "0", "0", "0"}, // 差戻し時必須（JSチェックするのでありえないここでエラーはありえない）
				{sashimodoshiSakiGougiEdaedano ,"差戻し先合議枝枝番号"		,"0", "0", "0", "0", "0", "0"}, // 差戻し時必須（JSチェックするのでありえないここでエラーはありえない）
				{sashimodoshiComment ,"差戻しコメント"			,"0", "0", "0", "0", "0", "0"}, // 任意
				{downloadFileNo ,"ダウンロードファイル番号"	,"0", "0", "0", "0", "2", "0"}, // ファイルダウンロード・ファイル削除のKEY
		};
		hissuCheckCommon(commonList, eventNum);

		// ユーザー定義届書のバージョンチェック
		if (denpyouKbn != null && denpyouKbn.length() > 0 && denpyouKbn.substring(0, 1).equals("B")) {
			String[][] versionList = {{version,"バージョン","2", "2", "2", "2", "0", "2"}};
			hissuCheckCommon(versionList, eventNum);
		}

		if("1".equals(isDispYosanCheckSection) && !"1".equals(isDispKiantenpuSection)) {
			String[][] yosanCheckList = {{yosanCheckNengetsu,"予算チェック対象月","0", "1", "1", "0", "0", "0"}};
			hissuCheckCommon(yosanCheckList, eventNum);
		}

		//e文書使用フラグが有効になっている場合e文書明細をチェック
		if("1".equals(ebunshoShiyouFlg)){
			//添付済みファイル部
			int i = 0;
			int notEcnt = 0;
			if(tenpuzumi_ebunsho_nengappi != null){

				//int notEcnt = 0;
				int cnt = 0;
				int lastIndexOfMeisai = 0;
				if(eLogic == null){
					eLogic = EteamContainer.getComponent(EbunshoLogic.class);
				}
				for(int a = 0; a < tenpuzumi_edano.length; a++) {
// String kakuchoushi = EteamIO.getExtension(tenpuzumi_filename[a]);

					//e文書対象かチェック
					if(tenpuzumi_ebunshoflg[a] == null || "0".equals(tenpuzumi_ebunshoflg[a])) {
						//20220531 コンティニュー時にcntのインクリメント追加　koushinEbunshodata()にも同様の変更あり
						cnt++;
// if(!"pdf".equals(kakuchoushi) && !"bmp".equals(kakuchoushi) && !"jpeg".equals(kakuchoushi) && !"jpg".equals(kakuchoushi)) {
						if(!eLogic.isEbunshoOrgFile(tenpuzumi_filename[a])) {
							notEcnt++;
						}else {
							lastIndexOfMeisai++;
						}
						continue;
					}

					int lmt = Integer.parseInt(tenpuzumi_ebunshodata_count[a]);
					for( int b = lastIndexOfMeisai ; b < lastIndexOfMeisai + lmt ; b++ ) {
						int ip = b - lastIndexOfMeisai + cnt + 1 - notEcnt;
						
						int shoruiShubetsu = Integer.parseInt(this.tenpuzumi_ebunsho_shubetsu[b]);
						String nengappiHissuFlg = this.nengappi_nini_flg[shoruiShubetsu] == 0 ? "1" : "0";
						String kingakuHissuFlg = this.kingaku_nini_flg[shoruiShubetsu] == 0 ? "1" : "0";
						String hakkoushaHissuFlg = this.hakkousha_nini_flg[shoruiShubetsu] == 0 ? "1" : "0";
						hissuCheckCommon(new String[][] {{tenpuzumi_ebunsho_nengappi[b], "e文書明細"+ ip +"行目:年月日", "0", nengappiHissuFlg, nengappiHissuFlg, "0", "0", "0"},}, eventNum);
						
// 2022/06/24 Ver22.05.31.02 納品書の条件を削除	→ 金額のみ外出しに変更 *-					
						if (tenpuzumi_ebunsho_shubetsu[b].equals("3")){
							if(ebunshoEnableFlg.equals("1")) hissuCheckCommon(new String[][] {{tenpuzumi_ebunsho_hinmei[b], "e文書明細"+ ip +"行目:品名", "0", "1", "1", "0", "0", "0"},}, eventNum);
							// 起票途中で「e文書を有効にする」が「1」から「2」へ変わった場合に品名クリア
							if (ebunshoEnableFlg.equals("2"))
							{
								tenpuzumi_ebunsho_hinmei[b] = "";
							}
						}
						else
						{
// hissuCheckCommon(new String[][] {{tenpuzumi_ebunsho_kingaku[b], "e文書明細"+ ip +"行目:金額", "0", kingakuHissuFlg, kingakuHissuFlg, "0", "0", "0"},}, eventNum);
							// 種別3以外は品名クリア
							tenpuzumi_ebunsho_hinmei[b] = "";
						}
						hissuCheckCommon(new String[][] {{tenpuzumi_ebunsho_kingaku[b], "e文書明細"+ ip +"行目:金額", "0", kingakuHissuFlg, kingakuHissuFlg, "0", "0", "0"},}, eventNum);
// -*						
						hissuCheckCommon(new String[][]{{tenpuzumi_ebunsho_hakkousha[b], "e文書明細"+ ip +"行目:発行者", "0", hakkoushaHissuFlg, hakkoushaHissuFlg, "0", "0", "0"},}, eventNum);
						//i = b;
					}
					cnt += lmt;
					lastIndexOfMeisai += lmt;
					
				}
				i = cnt;
			}

			//新規添付ファイル部
			if(ebunsho_tenpufilename != null){
				for(int j = 0; j < ebunsho_tenpufilename.length; j++) {

					// 添付ファイルが無い、またはe文書化対象のファイルとして指定されていないならスキップ
					String fileNm = ebunsho_tenpufilename[j];
					if ( isEmpty(fileNm) )
					{
						continue;
					}

					int ip = i + j + 1 - notEcnt;

					int shoruiShubetsu = Integer.parseInt(this.ebunsho_shubetsu[j]);

					String nengappiHissuFlg = this.nengappi_nini_flg[shoruiShubetsu] == 0 ? "1" : "0";
					String kingakuHissuFlg = this.kingaku_nini_flg[shoruiShubetsu] == 0 ? "1" : "0";
					String hakkoushaHissuFlg = this.hakkousha_nini_flg[shoruiShubetsu] == 0 ? "1" : "0";

					hissuCheckCommon(new String[][] {{ebunsho_nengappi[j], "e文書明細"+ ip +"行目:年月日", "0", nengappiHissuFlg, nengappiHissuFlg, "0", "0", "0"},}, eventNum);
					
// 2022/06/24 Ver22.05.31.02 納品書の条件を削除	→ 金額のみ外出しに変更 *-				
					if (ebunsho_shubetsu[j].equals("3")){
						if(ebunshoEnableFlg.equals("1")) hissuCheckCommon(new String[][] {{ebunsho_hinmei[j], "e文書明細"+ ip +"行目:品名", "0", "1", "1", "0", "0", "0"},}, eventNum);
					}
					else
					{
// hissuCheckCommon(new String[][] {{ebunsho_kingaku[j], "e文書明細"+ ip +"行目:金額", "0", kingakuHissuFlg, kingakuHissuFlg, "0", "0", "0"},}, eventNum);
						// 種別3以外は品名クリア
						ebunsho_hinmei[j] = "";
					}
					hissuCheckCommon(new String[][] {{ebunsho_kingaku[j], "e文書明細"+ ip +"行目:金額", "0", kingakuHissuFlg, kingakuHissuFlg, "0", "0", "0"},}, eventNum);
// -*					
					hissuCheckCommon(new String[][]{{ebunsho_hakkousha[j], "e文書明細"+ ip +"行目:発行者", "0", hakkoushaHissuFlg, hakkoushaHissuFlg, "0", "0", "0"},}, eventNum);
				}
			}
		}

	}
//＜イベント＞
	/**
	 * 表示イベント
	 * @return Strutsへ渡すResultName
	 */
	public String init() {
		this.hissuCheck(1);
		connection = EteamConnection.connect();
		try {
			initialize();
			this.formatCheck();

			//アクセス制御 ＆ データ読み
			accessCheck(Event.INIT);
			loadData();

			// 起案番号運用の情報を取得する。
			this.getKianbangouKanrenInfo();

			// 個別伝票の表示処理を行います。
			displayKobetsuDenpyou(connection);
			kijunbi = judgeKijunbi();
			yosanCheckTaishougaiFlg = judgeYosanCheckTaisyougai();

			// 起案伝票の表示処理
			displayHyoujiKianDenpyou();

			// 関連伝票の表示処理
			displayHyoujiKanrenDenpyou();

			// 予算執行対象月の初期化
			yosanCheckNengetsu = this.initYosanCheckNengetsu();

			// エラーが有る場合は処理を終了します。
			if (errorList.size() > 0) { return "error"; }

			// 該当伝票の通知を既読に更新します。
			updateTsuuchi();

			connection.commit();

			return "success";
		}finally{
			connection.close();
		}
	}
	
	/**
	 * initの処理が進む前に、必要なプロパティの初期化を行う。
	 */
	protected void setDefaultKobetsuDenpyouProperties()
	{
	}

	/**
	 * 登録イベント
	 * @return Strutsへ渡すResultName
	 */
	public String touroku() {
		EteamCommon.uploadFileSizeCheck(); //ファイルサイズ超過を先にチェックする（とりあえず一旦）
		hissuCheck(2);
		//EteamCommon.uploadFileSizeCheck();

		connection = EteamConnection.connect();

		Map<String, Ebunsho> filenameEbunshonoMap = new HashMap<String, Ebunsho>();

		try{
			initialize();
			formatCheck();

			accessCheck(Event.TOUROKU);
			loadData();

			// 起案伝票チェック
			myLogic.kianDenpyouCheck(denpyouKbn, denpyouId, kianHimodukeFlg, kianDenpyouId, errorList);

			// 関連伝票チェック
			kanrenDenpyouCheck();

			// 個別伝票の入力チェック＆登録
			tourokuCheckKobetsuDenpyou(connection);
			if (errorList.size() > 0)
			{
				return "error";
			}

			//添付ファイル必須チェック
			checkTenpFileHissu();
			if (errorList.size() > 0)
			{
				return "error";
			}

			//添付ファイル容量チェック
			checkTenpuFileSize();
			if (errorList.size() > 0)
			{
				return "error";
			}

			//ファイルのチェック
			myLogic.checkTenpuFile(denpyouKbn, uploadFileFileName, errorList);
			if (errorList.size() > 0)
			{
				return "error";
			}

			// Shift-JISで対応できないファイル名が含まれているか確認
			checkTenpuFileNameShiftJIS();
			if (errorList.size() > 0)
			{
				// 画面項目情報を取得(2023/12/11時点 オーバーライドされているのは簡易届のみ)
				this.afterFileCheckError();
				return "error";
			}

			// 稟議金額残高チェック
			ringiKingakuZandakaCheck(this.loadRingiKingakuDataBeforeUpdate(this.denpyouId));
			if (errorList.size() > 0)
			{
				return "error";
			}


			// 伝票IDの採番
			String oldDenpyouId = this.denpyouId; // rollbackしたとき用に保存
			denpyouId = saibanLogic.saibanDenpyouId(denpyouKbn, this.connection);

			// 伝票番号（シリアル番号）の採番
			int newSerialNo = saibanLogic.saibanDenpyouSerialNo(this.connection);

			// 個別伝票の登録処理
			tourokuKobetsuDenpyou(connection);

			// 共通側の登録処理
			myLogic.insertDenpyou(denpyouId, denpyouKbn, DENPYOU_JYOUTAI.KIHYOU_CHUU, n2b(sanshouDenpyouId), daihyouFutanBumonCd, loginUserId, newSerialNo);
			myLogic.updateDenpyouYosanCheckNengetsu(denpyouId, super.n2b(yosanCheckNengetsu), loginUserId);
			yosanCheckTaishougaiFlg = judgeYosanCheckTaisyougai();
			myLogic.insertDefaultShouninRoute(denpyouId, denpyouKbn, toDecimal(getKingaku()), sanshouDenpyouId,
											  loginUserInfo.getSeigyoUserId(), loginUserInfo.getDisplayUserNameShort(),
											  kihyouBumonCd, kihyouBumonName,
											  bumonRoleId, bumonRoleName,
											  loginUserInfo.getLoginUserId(), toDate(kijunbi), getDaihyouTorihiki()
											);

			//TODO ebunsho_tenpufilename_headerを削除し、shiftFileNameに統一して処理できるようにする
			//      JSP側も制御変更必要(チェックがあればファイル名を渡し、チェックがなければ空白値を送信してe文書化フラグの代わりとしている)

			// 添付ファイルを登録.ファイル名の重複あれば_01とか付ける
			String[] shiftFileName = myLogic.renameFile(denpyouId, uploadFileFileName);
			tenpuFileDao.tenpuFileEntry(denpyouId, uploadFile, uploadFileContentType, shiftFileName, loginUserId);

			//該当伝票のe文書使用フラグがONであればe文書明細情報の登録
			if("1".equals(ebunshoShiyouFlg)){
				//ebunsho_tenpufilename_headerもリネーム
				ebunshoTenpufileRename(uploadFileFileName, shiftFileName);
				//添付ファイルレコードでに対するe文書番号を取得（登録済と新規採番両方)
				filenameEbunshonoMap = myLogic.createFilenameEbunshonoMap(denpyouId);
				//e文書ファイルレコード登録（↑で新規採番したe文書番号のみ)
				String ebunshoTourokuResult = tourokuEbunshoFile(filenameEbunshonoMap);

				if(!ebunshoTourokuResult.equals("success"))
				{
					// 一部DB処理が既に実行されているので、明示的にrollbackしてIDをもとに戻す
					this.connection.rollback();
					this.denpyouId = oldDenpyouId;
					this.afterFileCheckError();
					return "error";
				}

				//e文書データレコード登録※更新時はe文書番号毎に全データ入れ替えるようにする
				tourokuEbunshoData(filenameEbunshonoMap);
			}

			// 関連伝票の登録処理
			tourokuKanrenDenpyou();

			// 伝票起案紐付の登録処理
			myLogic.tourokuDenpyouKianHimozuke(denpyouId, kianDenpyouId, kianDenpyouKbn, kianHimodukeFlg);

			// 稟議金額を引き継ぐ
			this.ringiKingakuUpdate(this.ringiKingakuHikitsugi());

			//伝票一覧テーブル状態更新
			diLogic.insertDenpyouIchiran(denpyouId);
			diLogic.updateDenpyouIchiran(denpyouId);

			//ユーザー定義届書一覧テーブル登録
			insertKaniTodokeIchiran(connection);

			connection.commit();
			return "success";
		}
		// デバッグ用、エラーメッセージにファイル名配列を強制的に表示させる
// catch(Exception ex)
// {
// return Arrays.toString(this.ebunsho_tenpufilename_header) + ", " + Arrays.toString(this.uploadFileFileName) + ", " + Arrays.toString(filenameEbunshonoMap.keySet().toArray());
// }
		finally{
			connection.close();
		}
	}

	/**
	 * 更新イベント
	 * @return Strutsへ渡すResultName
	 */
	@SuppressWarnings("unchecked")
	public String koushin() {
		EteamCommon.uploadFileSizeCheck();
		hissuCheck(3);
		//EteamCommon.uploadFileSizeCheck();
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.KOUSHIN);
			loadData();

			// 変更前のDB状態
			Object shinseiBf   = myLogic.loadShinsei(denpyouKbn, denpyouId);
			Map<Integer, ?>	meisaiBf   = myLogic.loadMeisai(denpyouKbn, denpyouId);
			List<GMap>		tenpuFileListBf   = workflowLogic.selectTenpuFileForImport(denpyouId); // 添付ファイル比較用（更新前）
			List<GMap>		ebunshoDataListBf = workflowLogic.selectTenpuFile(denpyouId); // e文書データ比較用（更新前）
			GMap ringiBf   = myLogic.loadRingiKingakuData(denpyouId);

			// 起案伝票チェック
			myLogic.kianDenpyouCheck(denpyouKbn, denpyouId, kianHimodukeFlg, kianDenpyouId, errorList);

			// 関連伝票チェック
			kanrenDenpyouCheck();

			// 個別伝票の更新処理
			koushinKobetsuDenpyou(connection);
			if (errorList.size() > 0)
			{
				return "error";
			}
			yosanCheckTaishougaiFlg = judgeYosanCheckTaisyougai();

			//添付ファイル必須チェック
			checkTenpFileHissu();
			if (errorList.size() > 0)
			{
				return "error";
			}

			//添付ファイル容量チェック
			checkTenpuFileSize();
			if (errorList.size() > 0)
			{
				return "error";
			}

			//ファイルのチェック
			myLogic.checkTenpuFile(denpyouKbn, uploadFileFileName, errorList);
			if (errorList.size() > 0)
			{
				return "error";
			}

			// Shift-JISで対応できないファイル名が含まれているか確認
			checkTenpuFileNameShiftJIS();
			if (errorList.size() > 0)
			{
				// 画面項目情報を取得(2023/12/11時点 オーバーライドされているのは簡易届のみ)
				this.afterFileCheckError();
				return "error";
			}

			// 稟議金額残高チェック
			ringiKingakuZandakaCheck(this.loadRingiKingakuDataBeforeUpdate(this.denpyouId));
			if (errorList.size() > 0)
			{
				return "error";
			}

			// 共通側の更新処理
			myLogic.updateDenpyouDaihyouFutanBumon(denpyouId, daihyouFutanBumonCd, loginUserId);
			if(denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) {
				myLogic.updateDenpyouYosanCheckNengetsu(denpyouId, super.n2b(yosanCheckNengetsu), loginUserId);
				myLogic.updateShouninRoute(denpyouId, 1, kihyouBumonCd, kihyouBumonName, bumonRoleId, bumonRoleName, loginUserId);
			}

			// 申請前なら承認ルートリセット
			if (denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) && denpyouMap.get("shounin_route_henkou_flg").equals("0"))
				myLogic.insertDefaultShouninRoute(denpyouId, denpyouKbn, toDecimal(getKingaku()), sanshouDenpyouId,
												  loginUserInfo.getSeigyoUserId(), loginUserInfo.getDisplayUserNameShort(),
												  kihyouBumonCd, kihyouBumonName,
												  bumonRoleId, bumonRoleName,
												  loginUserInfo.getLoginUserId(), toDate(kijunbi), getDaihyouTorihiki()
												);

			//TODO ebunsho_tenpufilename_headerを削除し、shiftFileNameに統一して処理できるようにする
			//      JSP側も制御変更必要(チェックがあればファイル名を渡し、チェックがなければ空白値を送信してe文書化フラグの代わりとしている)

			// 添付ファイルを登録.ファイル名の重複あれば_01とか付ける
			String[] shiftFileName = myLogic.renameFile(denpyouId, uploadFileFileName);
			tenpuFileDao.tenpuFileEntry(denpyouId, uploadFile, uploadFileContentType, shiftFileName, loginUserId);

			//該当伝票のe文書使用フラグがONであればe文書明細情報の更新
			if("1".equals(ebunshoShiyouFlg)){
				//ebunsho_tenpufilename_headerもリネーム
				ebunshoTenpufileRename(uploadFileFileName, shiftFileName);
				//添付ファイルレコードでに対するe文書番号を取得（登録済と新規採番両方)
				Map<String, Ebunsho> filenameEbunshonoMap = myLogic.createFilenameEbunshonoMap(denpyouId);
				//e文書ファイルレコード登録（↑で新規採番したe文書番号のみ)
				String ebunshoTourokuResult = tourokuEbunshoFile(filenameEbunshonoMap);

				if(!ebunshoTourokuResult.equals("success"))
				{
					this.connection.rollback();
					this.afterFileCheckError();
					return "error";
				}

				//e文書データレコード登録※更新時はe文書番号毎に全データ入れ替えるようにする
				tourokuEbunshoData(filenameEbunshonoMap);

				// 既存のe文書ファイルについてe文書データテーブルの情報を更新
				koushinEbunshoData(filenameEbunshonoMap);

			}

			// 関連伝票の登録処理
			tourokuKanrenDenpyou();

			// 伝票起案紐付の更新処理
			myLogic.koushinDenpyouKianHimozuke(denpyouJoutai, denpyouId, denpyouKbn, kianDenpyouId, kianDenpyouKbn, kianHimodukeFlg);

			// 稟議金額を引き継ぐ
			this.ringiKingakuUpdate(this.ringiKingakuHikitsugi());

			// 承認者更新の場合、承認状況にレコード登録（before-after 更新差分情報）
			if (wfSeigyo.curShouninsha) {
				Object shinseiAf   = myLogic.loadShinsei(denpyouKbn, denpyouId);
				Map<Integer, ?>	meisaiAf   = myLogic.loadMeisai(denpyouKbn, denpyouId);
				List<GMap>		tenpuFileListAf   = workflowLogic.selectTenpuFileForImport(denpyouId); // 添付ファイル比較用（更新後）
				List<GMap>		ebunshoDataListAf = workflowLogic.selectTenpuFile(denpyouId); // e文書データ比較用（更新後）
				GMap ringiAf   = myLogic.loadRingiKingakuData(denpyouId);
				//支払日・計上日更新分の差分文字列作成
				String shiharaiDiff = "";
				if(!denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)){
					shiharaiDiff = myLogic.makeDiffShiharaibi(denpyouKbn, shinseiBf, shinseiAf);//支払依頼の日付項目は若干扱いが違う・・
				}
				//通常訂正分の差分文字列作成
				String updateDiff = myLogic.makeDiff(denpyouKbn, shinseiBf, meisaiBf, tenpuFileListBf, ebunshoDataListBf, ringiBf, 
																 shinseiAf, meisaiAf, tenpuFileListAf, ebunshoDataListAf, ringiAf);

				//「更新」として支払日・計上日の差分を承認状況欄に登録
				if (shiharaiDiff.length() > 0) {
					myLogic.insertShouninJoukyou(denpyouId, loginUserInfo, wfSeigyo.procRoute, "", "更新", shiharaiDiff);
				}
				//「訂正」として支払日・計上日以外の差分を承認状況欄に登録
				if (updateDiff.length() > 0) {
					myLogic.insertShouninJoukyou(denpyouId, loginUserInfo, wfSeigyo.procRoute, "", "訂正", updateDiff);
				}

			}

			//申請前の更新の場合、予算・起案金額チェックの保存データを削除
			if (denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) {
				yosanKanriLogic.deleteData(denpyouId);
			}

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			//ユーザー定義届書一覧テーブル登録
			insertKaniTodokeIchiran(connection);

			connection.commit();
			return "success";
		}finally{
			connection.close();
		}
	}

	/**
	 * 伝票を申請します。
	 * @return Strutsへ渡すResultName
	 */
	public String shinsei() {
		//更新処理
		if (koushin().equals("error"))
		{
			return "error";
		}

		//更新処理とは別トランザクションで申請
		connection = EteamConnection.connect();
		try{
			initialize();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御
			accessCheck(Event.SHINSEI);

			//e文書使用がオンの場合、該当伝票に添付されているe文書で登録日時が設定されていないもの
			//(タイムスタンプ付与必要なタイプのPDFに対する処理未実施)をチェック
			if("1".equals(ebunshoShiyouFlg)){
				if(workflowLogic.findEbunshoTimestampNotexistCount(denpyouId, denpyouKbn) > 0){
					errorList.add("タイムスタンプが付与されていないファイルが存在します。確認してください。");
					return "error";
				}
			}

			// 稟議金額残高チェック（直前の更新処理でチェック済みだが念の為）
			ringiKingakuZandakaCheck(myLogic.loadRingiKingakuData(this.denpyouId));
			if (errorList.size() > 0)
			{
				return "error";
			}

			//起案番号に関する処理
			myLogic.registKianbangou(denpyouKbn
									,denpyouId
									,isDispKianbangouBoDialog
									,kianbangouBoDialogBumonCd
									,kianbangouBoDialogNendo
									,kianbangouBoDialogRyakugou
									,kianbangouBoDialogKianbangouFrom);

			//予算・起案金額チェックにレコードを登録
			if((wfSeigyo.yosanCheck && false == yosanCheckTaishougaiFlg)|| wfSeigyo.kianCheck){
				if(false == isKaribaraiMishiyou()){
					myLogic.insertYosanKianKingakuCheck(denpyouId, denpyouKbn, wfSeigyo.yosanCheck, kianbangouBoDialogNendo);
				}
			}

			//個別伝票の申請処理
			shinseiKobetsuDenpyou(connection);

			//申請処理
			myLogic.shinsei(wfSeigyo, loginUserInfo, comment);

			// 申請時帳票出力フラグ取得
			GMap denpyouShubetuMap = workflowLogic.selectDenpyouShubetu(denpyouKbn);
			if(denpyouShubetuMap.get("denpyou_print_flg").toString().equals("1")) {
				printFlg = "TRUE";
			} else {
				printFlg = "FALSE";
			}

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			connection.commit();

			//起案終了
			String karibaraiDenpyouId = getKianShuuryouKaribaraiDenpyouId();
			if(null != karibaraiDenpyouId){
				if (kianShuuryou(karibaraiDenpyouId, "1").equals("error"))
				{
					return "error";
				}
			}

			return "success";
		} finally {
			connection.close();
		}
	}

	/**
	 * 伝票を取戻します。
	 * @return Strutsへ渡すResultName
	 */
	public String torimodoshi() {
		hissuCheck(3);
		connection = EteamConnection.connect();
		try {
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.TORIMODOSHI);
			loadData();

			//伝票状態を更新/
			myLogic.torimodoshi(wfSeigyo, loginUserInfo, comment);

			//予算・起案金額チェックの保存データを削除
			yosanKanriLogic.deleteData(denpyouId);

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			connection.commit();

			//起案終了解除
			String karibaraiDenpyouId = myLogic.getKianShuuryouKaribaraiDenpyouId(denpyouId, denpyouKbn);
			if(null != karibaraiDenpyouId){
				//申請前の起案終了フラグ=0の場合
				if(false == myLogic.isKianShuuryouBeforeShinsei(karibaraiDenpyouId)){
					if (kianShuuryou(karibaraiDenpyouId, "0").equals("error"))
					{
						return "error";
					}
				}
			}

			return returnString();
		} finally {
			connection.close();
		}
	}

	/**
	 * 伝票を取下げます。
	 * @return Strutsへ渡すResultName
	 */
	public String torisage() {
		hissuCheck(3);
		connection = EteamConnection.connect();
		try {
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.TORISAGE);
			loadData();

			// WFデータ更新
			myLogic.torisage(wfSeigyo, loginUserInfo, comment);

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);
			//経費立替精算・国内海外旅費精算・交通費精算で法人カード紐付け明細がある場合、紐付け解除
			hcLogic.removeDenpyouHimozuke(denpyouId);

			connection.commit();
			return returnString();
		} finally {
			connection.close();
		}
	}

	/**
	 * 伝票を承認します。
	 * @return Strutsへ渡すResultName
	 */
	public String shounin() {
		hissuCheck(3);
		
		if(this.errorList.size() > 0)
		{
			return "error";
		}
		
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.SHOUNIN);
			loadData();

			//e文書使用がオンの場合、該当伝票に添付されているe文書で登録日時が設定されていないもの
			//(タイムスタンプ付与必要なタイプのPDFに対する処理未実施)をチェック
			if("1".equals(ebunshoShiyouFlg) && workflowLogic.findEbunshoTimestampNotexistCount(denpyouId, denpyouKbn) > 0)
			{
					errorList.add("タイムスタンプが付与されていないファイルが存在します。確認してください。");
			}

			// 個別伝票の承認前チェック
			shouninCheckKobetsuDenpyou(connection);
			if (errorList.size() > 0)
			{
				return "error";
			}

			// 稟議金額残高チェック
			ringiKingakuZandakaCheck(myLogic.loadRingiKingakuData(this.denpyouId));
			if (errorList.size() > 0)
			{
				return "error";
			}

			// WFデータ更新
			myLogic.shounin(wfSeigyo, loginUserInfo, comment);

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			connection.commit();
			return "success";
		} finally {
			connection.close();
		}
	}

	/**
	 * 伝票の承認前チェック（一括承認用、伝票画面のイベントじゃない）
	 * @param paramConnection DBコネクション
	 *
	 * @exception EteamAccessDeniedException 操作できない状態
	 */
	public void shouninCheck(EteamConnection paramConnection) {
		//伝票一覧から渡されたパラメータをメンバ変数にセットして、伝票画面を開いて処理したのと同じAction状態を作る
		connection = paramConnection;

		initialize();

		// 排他制御：伝票テーブル行ロック
		workflowLogic.selectDenpyouForUpdate(denpyouId);

		//アクセス制御 ＆ データ読み
		accessCheck(Event.SHOUNIN);
		loadData();
		
		//e文書使用がオンの場合、該当伝票に添付されているe文書で登録日時が設定されていないもの
		//(タイムスタンプ付与必要なタイプのPDFに対する処理未実施)をチェック
		if("1".equals(ebunshoShiyouFlg) && workflowLogic.findEbunshoTimestampNotexistCount(denpyouId, denpyouKbn) > 0)
		{
				errorList.add("タイムスタンプが付与されていないファイルが存在します。確認してください。");
				this.connection.rollback(); // とりあえずこれでうまくいく？
				return;
		}
		
		// 個別伝票の承認前チェック
		shouninCheckKobetsuDenpyou(connection);
		if (errorList.size() > 0)
		{
			return;
		}

		// 稟議金額残高チェック
		ringiKingakuZandakaCheck(myLogic.loadRingiKingakuData(this.denpyouId));
		if (errorList.size() > 0)
		{
			return;
		}
	}

	/**
	 * 伝票の承認実行（一括承認用、伝票画面のイベントじゃない）
	 * shouninCheck(伝票1)、shouninCheck(伝票2)、、、と全部クリアしたら、shouninExecute(伝票1)、shouninExecute(伝票2)、、、
	 *
	 * @exception EteamAccessDeniedException 操作できない状態
	 */
	public void shouninExecute() {
		//承認前チェックは一覧側で実装している

		// WFデータ更新
		myLogic.shounin(wfSeigyo, loginUserInfo, comment);

		//伝票一覧テーブル状態更新
		diLogic.updateDenpyouIchiran(wfSeigyo.denpyouId);
	}

	/**
	 * 伝票を否認します。
	 * @return Strutsへ渡すResultName
	 */
	public String hinin() {
		hissuCheck(3);
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.HININ);
			loadData();

			// WFデータ更新
			myLogic.hinin(wfSeigyo, loginUserInfo, comment);

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			//経費立替精算・国内海外旅費精算・交通費精算で法人カード紐付け明細がある場合、紐付け解除
			hcLogic.removeDenpyouHimozuke(denpyouId);

			connection.commit();

			//起案終了解除
			String karibaraiDenpyouId = myLogic.getKianShuuryouKaribaraiDenpyouId(denpyouId, denpyouKbn);
			if(null != karibaraiDenpyouId){
				//申請前の起案終了フラグ=0の場合
				if(false == myLogic.isKianShuuryouBeforeShinsei(karibaraiDenpyouId)){
					if (kianShuuryou(karibaraiDenpyouId, "0").equals("error"))
					{
						return "error";
					}
				}
			}

			return returnString();
		} finally {
			connection.close();
		}
	}

	/**
	 * 伝票を差戻します。
	 * @return Strutsへ渡すResultName
	 */
	public String sashimodoshi() {
		hissuCheck(6);
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.SASHIMODOSHI);
			loadData();

			// WFデータ更新
			myLogic.sashimodoshi(wfSeigyo, loginUserInfo, Integer.parseInt(sashimodoshiSakiEdano), isEmpty(sashimodoshiSakiGougiEdano) ? null : Integer.parseInt(sashimodoshiSakiGougiEdano), isEmpty(sashimodoshiSakiGougiEdaedano) ? null : Integer.parseInt(sashimodoshiSakiGougiEdaedano), sashimodoshiComment);

			//申請者への差戻の場合、予算・起案金額チェックの保存データを削除
			if(1==Integer.parseInt(sashimodoshiSakiEdano)){
				yosanKanriLogic.deleteData(denpyouId);
			}

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			connection.commit();

			//起案終了解除（申請者への差戻の場合）
			if(1==Integer.parseInt(sashimodoshiSakiEdano)){
				String karibaraiDenpyouId = myLogic.getKianShuuryouKaribaraiDenpyouId(denpyouId, denpyouKbn);
				if(null != karibaraiDenpyouId){
					//申請前の起案終了フラグ=0の場合
					if(false == myLogic.isKianShuuryouBeforeShinsei(karibaraiDenpyouId)){
						if (kianShuuryou(karibaraiDenpyouId, "0").equals("error"))
						{
							return "error";
						}
					}
				}
			}

			return returnString();
		} finally {
			connection.close();
		}
	}
	
	/*202207 承認解除のイベント*/
	/**
	 * 最終承認解除イベント
	 * @return Strutsへ渡すResultName
	 */
	public String shouninKaijo() {
		
		connection = EteamConnection.connect();
		try {
			initialize();
			formatCheck();
			
			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);
			
			//アクセス制御 ＆ データ読み
			accessCheck(Event.SHOUNINKAIJO);
			loadData();
			
			//事前チェック4種
			if(workflowLogic.loadTaDenpyouHImoduki(denpyouId)) {
				errorList.add("すでに他の伝票に紐づいているため、承認解除できません。");
			}
			if(workflowLogic.loadKaribaraiSeisanHImoduki(denpyouId)) {
				errorList.add("すでに精算伝票に紐づいているため、承認解除できません。");
			}
			if(workflowLogic.loadKianDenpyouHImoduki(denpyouId)) {
				errorList.add("すでに起案伝票として登録されているため、承認解除できません。");
			}
			if(workflowLogic.loadShiwakezumi(denpyouId)) {
				errorList.add("すでに仕訳抽出の処理が行われているため、承認解除できません。");
			}
			
			if (errorList.size() > 0) {
				displayKobetsuDenpyou(connection);
				return "error";
			}
			
			// WFデータ更新
			myLogic.shouninKaijo(wfSeigyo, loginGyoumuRoleId, loginUserInfo, comment);
			
			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);
			
			connection.commit();
			
			//202208 memo 起案終了解除も必要？
			
			return "success";
		} finally {
			connection.close();
		}
	}
	
	/**
	 * マル秘文書フラグ設定イベント。
	 * @return Strutsへ渡すResultName
	 */
	public String maruhiSettei(){

		//チェック用イベントとして「更新」を使用
		hissuCheck(3);
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			//アクセス制御 ＆ データ読み
			accessCheck(Event.MARUHISETTEI);

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//データ更新
			myLogic.maruhiHenkou(denpyouId, loginUserInfo, "1");

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			connection.commit();
			return returnString();
		}finally{
			connection.close();
		}
	}

	/**
	 * マル秘文書フラグ解除イベント。
	 * @return Strutsへ渡すResultName
	 */
	public String maruhiKaijyo(){

		//チェック用イベントとして「更新」を使用
		hissuCheck(3);
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			//アクセス制御 ＆ データ読み
			accessCheck(Event.MARUHIKAIJYO);

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//データ更新
			myLogic.maruhiHenkou(denpyouId, loginUserInfo, "0");

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			connection.commit();
			return returnString();
		}finally{
			connection.close();
		}
	}



	/**
	 * 任意メモ登録イベント。
	 * @return Strutsへ渡すResultName
	 */
	public String niniMemoTouroku() {
		hissuCheck(4);
		connection = EteamConnection.connect();
		try{
			initialize();
			formatCheck();

			//データ更新
			niniCommentDao.insertNinimemo(denpyouId, loginUserId, loginUserFullName, niniMemo);
			connection.commit();
			return returnString();
		}finally{
			connection.close();
		}
	}

	/**
	 * 参照起票イベント。
	 * @return Strutsへ渡すResultName
	 */
	public String sanshouKihyou() {
		hissuCheck(2);
		formatCheck();
		return returnString();
	}

	/**
	 * ファイルダウンロードイベント。
	 * @return Strutsへ渡すResultName
	 */
	public String download() {
		hissuCheck(5);
		connection = EteamConnection.connect();
		try {
			initialize();
			formatCheck();
			//アクセス制御
			accessCheck(Event.INIT);

			// 添付ファイルデータ取得
			GMap map = workflowLogic.tenpuFileFind(denpyouId, Integer.parseInt(downloadFileNo));
			if (null == map) throw new EteamDataNotFoundException();
			fileName    = map.get("file_name").toString();
			byte[] fileData = (byte[]) map.get("binary_data");

			// コンテンツタイプの設定
			contentType = map.get("content_type").toString();
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);

			// レスポンス返す
			this.inputStream = new ByteArrayInputStream(fileData);
			return "success";
		} finally {
			connection.close();
		}
	}

	/**
	 * e文書ファイルダウンロードイベント。
	 * @return Strutsへ渡すResultName
	 */
	public String downloadEbunshoPdf() {

		connection = EteamConnection.connect();

		try {
			initialize();
			//アクセス制御
			accessCheck(Event.INIT);

			// 添付ファイルデータ取得(ファイル名はe文書番号設定)
			GMap map = workflowLogic.findTenpuEbunshoFile(downloadEbunshoNo);
			if (null == map) throw new EteamDataNotFoundException();
			fileName    = downloadEbunshoNo + ".pdf";
			byte[] fileData = (byte[]) map.get("binary_data");

			// コンテンツタイプの設定(PDF固定)
			contentType = ContentType.PDF;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);

			// レスポンス返す
			this.inputStream = new ByteArrayInputStream(fileData);
			return "success";
		} finally {
			connection.close();
		}
	}

	/**
	 *  添付ファイルのプレビューをロードします
	 */
	protected void loadTenpuFilePreview()
	{
		this.myLogic.loadTenpuFilePreviewInternal(this); // カスタマイズ対策で本体はLogicに移動
	}

	/**
	 * Excel帳票出力後、PDF変換
	 * @return Strutsへ渡すResultName
	 */
	public String pdfOutput() {
		connection = EteamConnection.connect();
		try{
			initialize();

			//アクセス制御 ＆ データ読み
			accessCheck(Event.INIT);
			loadData();
			getKianbangouKanrenInfo();//TODO:これloadData内に入れたい...

			//伝票個別Excelファイル作成
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			makeExcel(connection, excelOut);

			//PDF変換
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			WorkflowUtil.excel2Pdf(excelOut, pdfOut, denpyouId, kianBangou);

			//コンテンツタイプ設定
			fileName = (denpyouId + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".pdf");
			contentType = ContentType.PDF;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);
			inputStream = new ByteArrayInputStream(pdfOut.toByteArray());

			return "success";
		} finally {
			connection.close();
		}
	}

	/**
	 * 添付ファイル削除イベント。
	 * @return Strutsへ渡すResultName
	 */
	public String fileDelete() {
		hissuCheck(5);
		connection = EteamConnection.connect();
		try {
			initialize();
			formatCheck();

			// 排他制御：伝票テーブル行ロック
			workflowLogic.selectDenpyouForUpdate(denpyouId);

			//アクセス制御 ＆ データ読み
			accessCheck(Event.FILE_DELETE);
			loadData();

			//削除前状態
			List<GMap> tenpuFileListBf   = wfSeigyo.curShouninsha ? workflowLogic.selectTenpuFileForImport(denpyouId) : null;
			List<GMap> ebunshoDataListBf = wfSeigyo.curShouninsha ? workflowLogic.selectTenpuFile(denpyouId) : null;
			// 添付ファイルを削除します。
			workflowLogic.tenpuFileDelete(denpyouId, Integer.parseInt(downloadFileNo));
			
			//タイムスタンプ失敗分を削除する 20220602
			//20220607 「downloadEbunshoNo != null」だとバグ情報が出たので変更
			if( !"".equals(downloadEbunshoNo) || downloadEbunshoNo.length() > 1 /*タイムスタンプ付与失敗のファイル*/) {
				workflowLogic.tenpuEbunshoDataDelete(downloadEbunshoNo);
				workflowLogic.tenpuEbunshoFileDelete(denpyouId, downloadEbunshoNo);
			}
			

			// 承認者更新の場合、承認状況にレコード登録（before-after 更新差分情報）
			if (wfSeigyo.curShouninsha) {
				// 削除後状態
				List<GMap> tenpuFileListAf   = workflowLogic.selectTenpuFileForImport(denpyouId);
				List<GMap> ebunshoDataListAf = workflowLogic.selectTenpuFile(denpyouId);
				
				String updateDiff = myLogic.makeDiff(denpyouKbn, null, null, tenpuFileListBf, ebunshoDataListBf, null, 
																 null, null, tenpuFileListAf, ebunshoDataListAf, null);
				if (updateDiff.length() > 0) {
					myLogic.insertShouninJoukyou(denpyouId, loginUserInfo, wfSeigyo.procRoute, "", "訂正", updateDiff);
				}
			}
			connection.commit();
			return returnString();
		} finally {
			connection.close();
		}
	}

	/**
	 * 起案終了／起案終了解除イベント。
	 * @return Strutsへ渡すResultName
	 */
	public String kianShuuryou(){
		connection = EteamConnection.connect();
		try{
			initialize();

			// データ更新
			myLogic.updateDenpyouKianHimozukeForKianSyuryo(this.denpyouId, this.kianShuuryouFlg);

			// 承認状況登録
			String strComment;
			if ("1".equals(this.kianShuuryouFlg)){
				strComment = kianShuuryou.SETTEI;
			}else{
				strComment = kianShuuryou.KAIJO;
			}
			GMap procRoute = workflowLogic.findProcRoute(denpyouId, loginUserInfo);
			if(procRoute != null){
				myLogic.insertShouninJoukyou(denpyouId, loginUserInfo, procRoute, "", kianShuuryou.JOUKYOU, strComment);
			}else{
				myLogic.insertShouninJoukyou(denpyouId, loginUserInfo, "", kianShuuryou.JOUKYOU, strComment);
			}

			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			// コミット
			connection.commit();
			return this.returnString();

		}finally{
			connection.close();
		}
	}

	/**
	 * 起案終了／起案終了解除イベント。（操作対象伝票指定）
	 * @param _denpyouId 対象伝票ID
	 * @param _kianShuuryouFlg 起案終了フラグ
	 * @return Strutsへ渡すResultName
	 */
	protected String kianShuuryou(String _denpyouId, String _kianShuuryouFlg){
		// thisの値を退避
		String tmpDenpyouId = this.denpyouId;
		String tmpKianShuuryouFlg = this.kianShuuryouFlg;

		// 起案終了／起案終了解除を実施
		this.denpyouId = _denpyouId;
		this.kianShuuryouFlg = _kianShuuryouFlg;
		String result = kianShuuryou();

		//退避していた値を戻す
		this.denpyouId = tmpDenpyouId;
		this.kianShuuryouFlg = tmpKianShuuryouFlg;

		return result;
	}

	/**
	 * 関連伝票の登録処理
	 */
	protected void tourokuKanrenDenpyou() {

		myLogic.deleteKanrenDenpyou(denpyouId);

		if (kanrenDenpyouId != null) {
			for (int i = 0; i < kanrenDenpyouId.length; i++) {
				if (!kanrenDenpyouId[i].equals("")) {
					myLogic.insertKanrenDenpyou(denpyouId, kanrenDenpyouId[i], kanrenDenpyouKbn[i], toDate(kanrenTourokuTime[i].substring(0, 10).replace("-", "/")), toDate(kanrenShouninTime[i].substring(0, 10).replace("-", "/")));
				}
			}
		}
	}

	/**
	 * 稟議金額引継<br>
	 * 伝票追加で添付した伝票に稟議金額の設定がある場合、その値を引き継ぐ（伝票起案紐付け）<br>
	 * @return 稟議金額情報
	 */
	protected GMap ringiKingakuHikitsugi(){

		String strRingiKingaku = null;
		String strMotoDenpyouId = null;

		/* ***********************************************************
		 * 引継優先順位：第一位
		 ************************************************************/
		// 経費立替精算、出張旅費精算、海外出張旅費精算で仮払伝票が選択されている場合
		// 仮払伝票に引き継がれている稟議金額を引き継ぐ
		String karibaraiDenpyouId = getKaribaraiDenpyouIdKobetsu();
		if (super.isNotEmpty(karibaraiDenpyouId)){
			GMap mapHimozuke = this.myLogic.selectDenpyouKianHimozuke(karibaraiDenpyouId);
			BigDecimal bdKanrenRingiKingaku = (BigDecimal)mapHimozuke.get("ringi_kingaku");
			if (null != bdKanrenRingiKingaku){
				// 設定された稟議金額を引き継ぎ対象とする。
				strRingiKingaku = bdKanrenRingiKingaku.toPlainString();
				strMotoDenpyouId = karibaraiDenpyouId;
			}
		}

		/* ***********************************************************
		 * 引継優先順位：第二位
		 ************************************************************/

		// 稟議金額が設定されていない場合、添付された伝票に引き継がれた稟議金額を引き継ぐ。
		if (super.isEmpty(strRingiKingaku) && null != ringiKingakuHikitsugiFlg){

			/*
			 * 添付された伝票の稟議金額を取得する（既に稟議金額を引き継いでいるもの）。
			 */

			for (int i=0 ; i < ringiKingakuHikitsugiFlg.length ; i++){
				if("1".equals(ringiKingakuHikitsugiFlg[i])){
					// 関連伝票IDから伝票起案情報を取得する。
					String kDenpyouId = kanrenDenpyouId[i];
					GMap mapHimozuke = this.myLogic.selectDenpyouKianHimozuke(kDenpyouId);
					// 稟議金額が設定されている場合、その値を引き継ぐ。
					BigDecimal bdKanrenRingiKingaku = (BigDecimal)mapHimozuke.get("ringi_kingaku");
					strMotoDenpyouId = kDenpyouId;
					if (null != bdKanrenRingiKingaku){
						// 最初に稟議金額が設定されているものを引き継ぎ対象とする。
						strRingiKingaku = bdKanrenRingiKingaku.toPlainString();
						break;
					}
				}
			}
		}

		/* ***********************************************************
		 * 引継優先順位：第三位
		 ************************************************************/

		// 稟議金額が設定されていない場合、添付されたユーザー定義届書伝票から稟議金額を引き継ぐ。
		if (super.isEmpty(strRingiKingaku) && null != ringiKingakuHikitsugiFlg){
			/*
			 * ユーザー定義届書伝票から稟議金額を取得する。
			 */
			for (int i=0 ; i < ringiKingakuHikitsugiFlg.length ; i++){
				if("1".equals(ringiKingakuHikitsugiFlg[i])){
					// ユーザー定義届書以外は処理しない
					String kDenpyouKbn = kanrenDenpyouKbn[i];
					if (!this.myLogic.isKaniTodoke(kDenpyouKbn)){
						continue;
					}
					String kDenpyouId = kanrenDenpyouId[i];
					String yosanShikkouId = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU;
					strRingiKingaku = this.workflowLogic.getKaniTodokeYosanShikkouColumnData(kDenpyouId, yosanShikkouId);
					strMotoDenpyouId = kDenpyouId;
					if (super.isNotEmpty(strRingiKingaku)){
						// 最初に稟議金額が設定されているものを引き継ぎ対象とする。
						break;
					}
				}
			}
		}

		GMap ret = new GMap();
		ret.put("ringi_kingaku", super.toDecimal(strRingiKingaku));
		ret.put("ringi_kingaku_hikitsugimoto_denpyou", strMotoDenpyouId);
		return ret;
	}

	/**
	 * 稟議金額画面表示<br>
	 * 伝票に稟議金額が引き継がれている場合、画面に稟議金額を表示する。<br>
	 */
	protected void ringiKingakuHyouji(){

		/*
		 * 稟議金額の表示有無を決定する。
		 */
		boolean isCalc = true;
		GMap mapFind;
		switch (this.denpyouKbn){
		case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			// A001 経費精算
			mapFind = this.kaikeiCategoryLogic.findKeihiSeisan(this.denpyouId);
			// 仮払なしの場合は稟議金額を表示しない
			if ("0".equals(mapFind.get("karibarai_on"))){
				isCalc = false;
			}
			break;
		case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
			// A002 伺い申請
			mapFind = this.kaikeiCategoryLogic.findKaribarai(this.denpyouId);
			// 仮払なしの場合は稟議金額を表示しない
			if ("0".equals(mapFind.get("karibarai_on"))){
				isCalc = false;
			}
			break;
		case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
			// A004 出張旅費精算
			mapFind = this.kaikeiCategoryLogic.findRyohiSeisan(this.denpyouId);
			// 仮払なしの場合は稟議金額を表示しない
			if ("0".equals(mapFind.get("karibarai_on"))){
				isCalc = false;
			}
			break;
		case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
			// A005 出張伺い申請
			mapFind = this.kaikeiCategoryLogic.findRyohiKaribarai(this.denpyouId);
			// 仮払なしの場合は稟議金額を表示しない
			if ("0".equals(mapFind.get("karibarai_on"))){
				isCalc = false;
			}
			break;
		case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			// A011 海外出張旅費精算
			mapFind = this.kaikeiCategoryLogic.findKaigaiRyohiSeisan(this.denpyouId);
			// 仮払なしの場合は稟議金額を表示しない
			if ("0".equals(mapFind.get("karibarai_on"))){
				isCalc = false;
			}
			break;
		case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
			// A012 海外出張伺い申請
			mapFind = this.kaikeiCategoryLogic.findKaigaiRyohiKaribarai(this.denpyouId);
			// 仮払なしの場合は稟議金額を表示しない
			if ("0".equals(mapFind.get("karibarai_on"))){
				isCalc = false;
			}
			break;
		default:
			break;
		}

		/*
		 * 画面表示データを構成する。
		 */
		if (isCalc){
			/*
			 * 稟議金額データを取得する。伝票により精算金額合計値を取得する。
			 */
			GMap loadData = this.myLogic.loadRingiKingakuData(this.denpyouId);
			if (loadData == null){
				return;
			}

			BigDecimal bdRingiKingaku = (BigDecimal)loadData.get("ringi_kingaku");
			if (bdRingiKingaku == null){
				// 稟議金額に設定がない時は処理を終了する。
				return;
			}

			// 稟議金額ラベル名称
			this.ringiKingakuName = loadData.get("ringiKingakuName").toString();

			// 稟議金額
			this.ringiKingaku = super.formatMoney(bdRingiKingaku);

			// 稟議金額残高
			BigDecimal lZandaka = (BigDecimal)loadData.get("ringiKingakuZandaka");
			this.ringiKingakuZandaka = super.formatMoney(lZandaka);

			// 稟議金額超過コメント
			this.hyoujiRingiKingakuChoukaComment = loadData.get("ringi_kingaku_chouka_comment").toString();
			this.ringiKingakuChoukaCommentHissuFlg = (-1 == lZandaka.compareTo(BigDecimal.ZERO))? "1" : "";
			// 稟議金額超過コメント表示フラグ
			if(-1 == lZandaka.compareTo(BigDecimal.ZERO) || 0 != hyoujiRingiKingakuChoukaComment.length()){
				this.ringiKingakuChoukaCommentHyoujiFlg = "1";
			}
		}
	}

	/**
	 * 伝票起案紐付テーブルに稟議金額関連項目を保存する。
	 * @param ringiData 稟議金額関連情報マップ
	 */
	protected void ringiKingakuUpdate(GMap ringiData){

		/*
		 * 伝票起案情報に稟議金額を引き継ぐ。
		 * 稟議金額の設定有無に関わらず実施する。（設定された場合／設定解除された場合）
		 */
		BigDecimal bdRingiKingaku = (BigDecimal)ringiData.get("ringi_kingaku");
		String strMotoDenpyouId = (null==ringiData.get("ringi_kingaku_hikitsugimoto_denpyou"))? "" : ringiData.get("ringi_kingaku_hikitsugimoto_denpyou").toString();
		if (null==ringiKingakuChoukaComment)
		{
			ringiKingakuChoukaComment ="";
		}
		this.myLogic.updateDenpyouKianHimozukeForRingiKingaku(this.denpyouId, bdRingiKingaku, strMotoDenpyouId, ringiKingakuChoukaComment);
	}

	/**
	 * 登録／更新前の稟議金額関連情報を取得する。
	 * 引継ぎ元の稟議関連情報はDBから取得するが、操作中の伝票の情報が画面入力から取得する。
	 * @param denpyouIdTmp 伝票ID
	 * @return 結果マップ
	 */
	public  GMap loadRingiKingakuDataBeforeUpdate(String denpyouIdTmp){

		// 稟議金額の引継ぎ
		GMap ret = this.ringiKingakuHikitsugi();
		if (null == ret.get("ringi_kingaku"))
		{
			return null;
		}

		// 操作中の伝票の申請金額を減算した残高を取得する
		calRingiKingakuZandakaKobetsuDenpyou(myLogic.loadRingiKingakuDataBeforeUpdate(denpyouIdTmp, ret));

		return ret;
	}

	/**
	 * 稟議金額残高チェックを行う
	 * @param ringiData 稟議金額関連データマップ
	 */
	protected void ringiKingakuZandakaCheck(GMap ringiData){

		// 稟議金額関連情報が取得できない場合はチェック終了
		if (null == ringiData)
		{
			return;
		}

		// 稟議金額が非表示の場合はチェック終了
		if (false==judgeRingiKingakuHyoujiKobetsu())
		{
			return;
		}

		BigDecimal ringiKingakuWork = (BigDecimal)ringiData.get("ringi_kingaku");
		BigDecimal ringiKingakuZandakaWork = (BigDecimal)ringiData.get("ringiKingakuZandaka");

		// 稟議金額残高 >= 0の場合はチェック終了
		if (-1 != ringiKingakuZandakaWork.compareTo(BigDecimal.ZERO))
		{
			return;
		}

		BigDecimal oneHundred = BigDecimal.valueOf(100);
		int ringiShinseiRate   = ringiKingakuWork.subtract(ringiKingakuZandakaWork).divide(ringiKingakuWork, 2, RoundingMode.UP).multiply(oneHundred).intValue();
		String ringiKingakuNameTmp = ringiData.get("ringiKingakuName").toString();
		//判定に使用する超過コメント>>>申請・承認の場合はDBから取得した内容、登録・更新の場合は画面入力した内容
		String commentTmp = (ringiData.containsKey("ringi_kingaku_chouka_comment"))? (String)ringiData.get("ringi_kingaku_chouka_comment") : this.ringiKingakuChoukaComment;
		if(ringiShinseiRate <= Integer.parseInt(setting.ringiChoukaHantei())){
			if(0 == commentTmp.length()){
				errorList.add("申請金額が" + ringiKingakuNameTmp + "を超過します。\r\n" + ringiKingakuNameTmp + "超過コメントに超過理由を入力してください。");
				this.ringiKingakuChoukaCommentHyoujiFlg = "1";
				this.ringiKingakuName = ringiKingakuNameTmp;
				this.ringiKingakuChoukaCommentHissuFlg = "1";
			}
		}else{
			String shounintouKyokaFlg = setting.ringiChoukaShounintou();
			if(ringiChoukaShounintou.OK.equals(shounintouKyokaFlg)){
				if(0 == commentTmp.length()){
					errorList.add("申請金額が" + ringiKingakuNameTmp + "を大きく超過します。\r\n" + ringiKingakuNameTmp + "超過コメントに超過理由を入力してください。");
					this.ringiKingakuChoukaCommentHyoujiFlg = "1";
					this.ringiKingakuName = ringiKingakuNameTmp;
					this.ringiKingakuChoukaCommentHissuFlg = "1";
				}
			}else{
				errorList.add("申請金額が" + ringiKingakuNameTmp + "を大きく超過しています。");
				this.ringiKingakuChoukaCommentHyoujiFlg = "1";
				this.ringiKingakuName = ringiKingakuNameTmp;
				this.ringiKingakuChoukaCommentHissuFlg = "";
			}
		}

		//超過コメントの長さチェック
		checkString(commentTmp, 1, 240, ringiKingakuNameTmp + "超過コメント", false);
	}

	/**
	 * 表示用の起案伝票作成
	 */
	protected void displayHyoujiKianDenpyou() {

		// 領域を確保
		kianCount = 1;
		hyoujiKianEmbedSpace = new String[kianCount];
		hyoujiKianDenpyouKbn = new String[kianCount];
		hyoujiKianbangou = new String[kianCount];
		hyoujiKianKenmei = new String[kianCount];
		hyoujiKianDenpyouShubetsuUrl = new String[kianCount];
		hyoujiKianDenpyouUrl = new String[kianCount];
		hyoujiKianDenpyouShubetsu = new String[kianCount];
		hyoujiKianDenpyouId = new String[kianCount];
		hyoujiKianTenpuFileName = new String[kianCount];
		hyoujiKianTenpuFileUrl = new String[kianCount];
		kianDenpyouId = new String[kianCount];
		kianDenpyouKbn = new String[kianCount];

		// 伝票の起案追加された伝票IDを取得する。
		Object objKianDenpyou = null;
		GMap mapDenpyou = this.kianTsuikaLogic.findKianDenpyouData(this.denpyouId);
		if (null != mapDenpyou){
			objKianDenpyou = mapDenpyou.get("kian_denpyou");
		}

		// 起案伝票が設定されていない場合は処理終了
		if (isEmpty((String)objKianDenpyou)){
			return;
		}

		// 起案伝票が設定されている場合は起案伝票セクションに表示する情報を取得する。
		GMap mapKian = this.kianTsuikaLogic.findKianDenpyouData(objKianDenpyou.toString());

		// 関連伝票リスト作成
		this.kianTsuikaLogic.formatKianDenpyou(mapKian);

		// 画面表示データを移送する。
		hyoujiKianEmbedSpace[0] = (String)mapKian.get("embed_space");
		hyoujiKianDenpyouKbn[0] = (String)mapKian.get("kbn");
		hyoujiKianbangou[0] = (String)mapKian.get("trKianbangou");
		hyoujiKianKenmei[0] = (String)mapKian.get("trKenmei");
		hyoujiKianDenpyouShubetsuUrl[0] = (String)mapKian.get("denpyou_shubetsu_url");
		hyoujiKianDenpyouUrl[0] = (String)mapKian.get("denpyou_url");
		hyoujiKianDenpyouShubetsu[0] = (String)mapKian.get("shubetsu");
		hyoujiKianDenpyouId[0] = (String)mapKian.get("id");
		hyoujiKianTenpuFileName[0] = (String)mapKian.get("file_name");
		hyoujiKianTenpuFileUrl[0] = (String)mapKian.get("tenpu_url");
		kianDenpyouId[0] = (String)mapKian.get("denpyou_id");
		kianDenpyouKbn[0] = (String)mapKian.get("denpyou_kbn");
	}

	/**
	 * 表示用の関連伝票作成
	 */
	protected void displayHyoujiKanrenDenpyou() {

		/** 取得用の関連伝票リスト */
		List<GMap> kanrenList;

		// 紐づけされている関連伝票を取得
		if (isNotEmpty(denpyouId)) {
			kanrenList = workflowLogic.loadKanrenDenpyou(denpyouId);
		} else {
			kanrenList = new ArrayList<>();
		}

		// 関連伝票リスト作成
		myLogic.formatKanrenDenpyou(kanrenList);

		if (hyoujiKanrenDenpyouKbn == null) {

			hyoujiKanrenEmbedSpace = new String[kanrenList.size()];
			hyoujiKanrenDenpyouKbn = new String[kanrenList.size()];
			hyoujiKanrenDenpyouShubetsuUrl = new String[kanrenList.size()];
			hyoujiKanrenDenpyouUrl = new String[kanrenList.size()];
			hyoujiKanrenDenpyouShubetsu = new String[kanrenList.size()];
			hyoujiKanrenDenpyouId = new String[kanrenList.size()];
			hyoujiKanrenTenpuFileName = new String[kanrenList.size()];
			hyoujiKanrenTenpuFileUrl = new String[kanrenList.size()];
			hyoujiKanrenKihyouBi = new String[kanrenList.size()];
			hyoujiKanrenShouninBi = new String[kanrenList.size()];
			ringiKingakuHikitsugiFlg = new String[kanrenList.size()];
			kanrenDenpyouId = new String[kanrenList.size()];
			kanrenDenpyouKbn = new String[kanrenList.size()];
			kanrenTourokuTime = new String[kanrenList.size()];
			kanrenShouninTime = new String[kanrenList.size()];

			// 関連伝票リスト数を取得
			if (kanrenDenpyouId.length > 0) {
				kanrenCount = kanrenDenpyouId.length;
			}
		}

		int i = 0;
		for (GMap kanrenMap : kanrenList) {
			hyoujiKanrenEmbedSpace[i] = (String)kanrenMap.get("embed_space");
			hyoujiKanrenDenpyouKbn[i] = (String)kanrenMap.get("kbn");
			hyoujiKanrenDenpyouShubetsuUrl[i] = (String)kanrenMap.get("denpyou_shubetsu_url");
			hyoujiKanrenDenpyouUrl[i] = (String)kanrenMap.get("denpyou_url");
			hyoujiKanrenDenpyouShubetsu[i] = (String)kanrenMap.get("shubetsu");
			hyoujiKanrenDenpyouId[i] = (String)kanrenMap.get("id");
			hyoujiKanrenTenpuFileName[i] = (String)kanrenMap.get("file_name");
			hyoujiKanrenTenpuFileUrl[i] = (String)kanrenMap.get("tenpu_url");
			hyoujiKanrenKihyouBi[i] = (String)kanrenMap.get("kihyou_bi");
			hyoujiKanrenShouninBi[i] = (String)kanrenMap.get("shounin_bi");
			ringiKingakuHikitsugiFlg[i] = (String)kanrenMap.get("ringi_kingaku_hikitsugi_flg");
			kanrenDenpyouId[i] = (String)kanrenMap.get("denpyou_id");
			kanrenDenpyouKbn[i] = (String)kanrenMap.get("denpyou_kbn");
			kanrenTourokuTime[i] = kanrenMap.get("touroku_time").toString().substring(0, 10).replaceAll("-", "/");
			kanrenShouninTime[i] = kanrenMap.get("shounin_time").toString().substring(0, 10).replaceAll("-", "/");
			i++;
		}
	}

	/**
	 * 関連伝票に関するチェック
	 */
	protected void kanrenDenpyouCheck () {
		if (kanrenDenpyouId != null) {
			// 関連伝票上限チェック
			if (kanrenDenpyouId.length > KANREN_MAX) {
				errorList.add("添付伝票の上限件数(10)を超えています。");
			}
			// 同一伝票を登録しているか判定
			for (int i = 0; i < kanrenDenpyouId.length; i++) {
				for (int j = 0; j < kanrenDenpyouId.length; j++) {
					 if (i!=j && !kanrenDenpyouId[i].equals("") && kanrenDenpyouId[i].equals(kanrenDenpyouId[j])) {
						 errorList.add("同一伝票を添付することはできません。");
					 }
				}
			}
			// 稟議金額引継ぎフラグの入力チェック
			int work = 0;
			for(int i = 0; i < ringiKingakuHikitsugiFlg.length; i++){
				if(!"".equals(ringiKingakuHikitsugiFlg[i]))work += Integer.parseInt(ringiKingakuHikitsugiFlg[i]);
				if(work > 1){
					errorList.add("2つ以上の添付伝票から稟議金額を引継ぐことはできません。");
				}
			}
			//TODO 同一伝票チェックループの中に入れるべきか？
			for (int i = 0; i < kanrenDenpyouId.length; i++) {
				if(!kanrenDenpyouId[i].equals("")) {
					GMap fordenpyoujoutai = workflowLogic.selectDenpyou(kanrenDenpyouId[i]);
					String joutai = fordenpyoujoutai.get("denpyou_joutai").toString();
					if(!joutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI)) {
						errorList.add(kanrenDenpyouId[i]+"は承認解除されているため、添付伝票として選択できません。");
					}
				}
			}
		}
	}

//===以下小画面共用メソッド===============================
	/**
	 * 再表示処理<br>
	 * 伝票共通部分の再取得
	 * 個別のイベントでエラー表示等で画面再描画をする場合、これを呼ぶこと。
	 * 小クラスの各伝票Actionから呼ばれます。
	 * @param paramConnection コネクション
	 */
	protected void reDisplayDataForKobetsuDenpyou(EteamConnection paramConnection) {
		connection = paramConnection;
		initialize();
		accessCheck(Event.INIT);
		loadData();
	}

	/**
	 * 起票ユーザーIDの取得
	 * @return 起票ユーザーID
	 */
	protected String getKihyouUserId() {
		String userId = loginUserInfo.getSeigyoUserId();
		return getKihyouUserId(connection, userId);
	}

	/**
	 * 起票ユーザーIDの取得
	 * @param paramConnection コネクション
	 * @param userId ユーザーID
	 * @return 起票ユーザーID
	 */
	protected String getKihyouUserId(EteamConnection paramConnection, String userId) {
		String userIdTmp = userId;

		if(kaikeiCommonLogic == null) {
			kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, paramConnection);
		}

		//未起票ならログインユーザー、起票済みなら承認ルートのユーザーIID
		GMap map = kaikeiCommonLogic.findKihyouUser(denpyouId);
		if(map != null && map.get("user_id") != null){
			userIdTmp = map.get("user_id").toString();
		}

		return userIdTmp;
	}

	/**
	 * 申請内容を入力可能かどうかを判定する。
	 * 入力可能な条件は以下の通り。
	 * ・未起票である
	 * ・または、（起票中で起票者ユーザーである）
	 * @return 入力可能であればtrue。
	 */
	protected boolean judgeEnableInput() {
		//202208　最終承認者が編集不可かチェック
		if (myLogic.judgeDenpyouHenkouKengen(loginUserInfo, wfSeigyo))
		{
			return false;
		}
		return wfSeigyo.touroku || wfSeigyo.koushin;
	}

//===以下小画面実装メソッド===============================

	/**
	 * 個別業務の表示処理を実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 * @param out EXCELファイル出力先
	 */
	protected void makeExcel(EteamConnection _connection, OutputStream out) {}

	/**
	 * 個別業務の表示処理を実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 */
	protected void displayKobetsuDenpyou(EteamConnection _connection){}

	/**
	 * 個別業務の登録前チェックを実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 */
	protected void tourokuCheckKobetsuDenpyou(EteamConnection _connection){}

	/**
	 * 個別業務の登録処理を実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 */
	protected void tourokuKobetsuDenpyou(EteamConnection _connection){}

	/**
	 * 個別業務の更新処理を実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 */
	protected void koushinKobetsuDenpyou(EteamConnection _connection){}

	/**
	 * 個別業務の承認前チェックを実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 */
	protected void shouninCheckKobetsuDenpyou(EteamConnection _connection){}

	/**
	 * 証憑書類が必要であるかのチェックを実装。<br>
	 * @return 証憑書類が必要ならtrue
	 */
	protected boolean isUseShouhyouShorui(){
		return false;
	}

	/**
	 * 個別業務の申請時の処理を実装。<br>
	 * エラーが有る場合は「errorList」にエラーメッセージを追加する。
	 * @param _connection DBコネクション
	 */
	protected void shinseiKobetsuDenpyou(EteamConnection _connection){}

	/**
	 * 個別業務の稟議金額残高計算処理を実装。
	 * @param ringiData 稟議金額関連情報マップ
	 */
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){}

	/**
	 * 稟議金額を表示するかどうかの判定処理を実装
	 * @return 稟議金額を表示する場合はtrue
	 */
	protected boolean judgeRingiKingakuHyoujiKobetsu(){
		return true;
	}

	/**
	 * 画面に入力された仮払伝票IDを取得する処理を実装。
	 * @return 仮払伝票ID（仮払伝票IDを画面に持たない伝票の場合はnull）
	 */
	protected String getKaribaraiDenpyouIdKobetsu(){
		return null;
	}

	/**
	 * デフォルト承認ルートの決定に使用する取引の取得処理を実装。
	 * @return 仕訳枝番号（仕訳枝番号を持たない伝票の場合はnull)
	 */
	protected String getDaihyouTorihiki(){
		return null;
	}

	/**
	 * 仮払金未使用で精算済みかどうかの判定処理を実装
	 * @param _connection DBコネクション
	 * @return 仮払金未使用で精算済みならtrue
	 */
	protected boolean isKaribaraiMishiyouSeisanZumi(EteamConnection _connection) {
		return false;
	}

	/**
	 * 仮払金未使用の申請かどうかの判定処理を実装
	 * @return 申請内容が仮払未使用ならtrue
	 */
	protected boolean isKaribaraiMishiyou() {
		return false;
	}

	/**
	 * 起案終了／起案終了解除の対象とする仮払伝票IDを取得する処理を実装。
	 * @return 仮払伝票ID（処理対象がない場合はnull）
	 */
	protected String getKianShuuryouKaribaraiDenpyouId() {
		return null;
	}

	/**
	 * 予算執行対象部門・科目が入力された申請かどうかの判定処理を実装
	 * @param _connection DBコネクション
	 * @return 申請内容（集計部門・科目）が予算執行対象外ならtrue
	 */
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection _connection) {
		return false;
	}

	/**
	 * ユーザー定義届書一覧の登録処理を実装。<br>
	 * @param _connection DBコネクション
	 */
	protected void insertKaniTodokeIchiran(EteamConnection _connection){
		//ユーザー定義届書以外は処理なし
	}
//===以下内部メソッド===============================

	/**
	 * 全イベント共通初期処理
	 */
	protected void initialize() {
		//Logic New
		myLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		saibanLogic = EteamContainer.getComponent(SaibanLogic.class, connection);
		eLogic = EteamContainer.getComponent(EbunshoLogic.class);
		kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		// ワークフローイベント制御Logic
		this.kianTsuikaLogic = EteamContainer.getComponent(KianTsuikaLogic.class, connection);
		// 会計カテゴリー内のSelect文を集約したLogic
		this.kaikeiCategoryLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		yosanKanriLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
		kihyouNaviLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		// DAO
		tenpuFileDao = EteamContainer.getComponent(TenpuFileDao.class, connection);
		ebunshoFileDao = EteamContainer.getComponent(EbunshoFileDao.class, connection);
		ebunshoDataDao = EteamContainer.getComponent(EbunshoDataDao.class, connection);
		niniCommentDao = EteamContainer.getComponent(NiniCommentDao.class, connection);
		this.ebunshoKensakuDao = EteamContainer.getComponent(EbunshoKensakuDao.class, connection);

		//セッション情報をメンバーに移す
		loginUserInfo     	= loginUserInfo != null ? loginUserInfo : getUser();
		loginUserId       	= loginUserInfo.getTourokuOrKoushinUserId();
		loginGyoumuRoleId = loginUserInfo.getGyoumuRoleId();
		loginUserFullName = loginUserInfo.getDisplayUserName();

		// e文書検索設定情報を取得
		var ebunshoKensakuList = this.ebunshoKensakuDao.load();
		this.nengappi_nini_flg = ebunshoKensakuList.stream().filter(item -> item.itemNo == 4).map(item -> item.niniFlg).toArray(Short[]::new);
		this.kingaku_nini_flg = ebunshoKensakuList.stream().filter(item -> item.itemNo == 5).map(item -> item.niniFlg).toArray(Short[]::new);
		this.hakkousha_nini_flg = ebunshoKensakuList.stream().filter(item -> item.itemNo == 6).map(item -> item.niniFlg).toArray(Short[]::new);

		this.setDefaultKobetsuDenpyouProperties();
	}

	/**
	 * アクセス制御を行う。データなし or 不正アクセスならエラー。
	 * @param eventCode イベントコード。単に参照のみチェックなら0
	 * @throws EteamAccessDeniedException アクセス不可能
	 * @throws EteamDataNotFoundException データなし（伝票 or 参照起票伝票）
	 */
	protected void accessCheck(Event eventCode) {
		//各種機能が利用できる状態か判定。アクセス不可ならアクセスエラー投げる。
		wfSeigyo = myLogic.judgeWfSeigyo(loginUserInfo, denpyouKbn, denpyouId, sanshouDenpyouId);

		//押せないはずのボタンが押された！？不正リクエストかちょっとした時間差か
		if (
				(eventCode == Event.TOUROKU && ! wfSeigyo.touroku)
			||	(eventCode == Event.KOUSHIN && ! wfSeigyo.koushin)
			||	(eventCode == Event.FILE_DELETE && ! wfSeigyo.fileTenpuUpd)
			||	(eventCode == Event.SHINSEI && ! wfSeigyo.shinsei)
			||	(eventCode == Event.TORISAGE && ! wfSeigyo.torisage)
			||	(eventCode == Event.TORIMODOSHI && ! wfSeigyo.torimodoshi)
			||	(eventCode == Event.SHOUNIN && ! wfSeigyo.shounin)
			||	(eventCode == Event.HININ && ! wfSeigyo.hinin)
			||	(eventCode == Event.SASHIMODOSHI && ! wfSeigyo.sashimodoshi)
			||	(eventCode == Event.MARUHISETTEI && ! wfSeigyo.maruhiSettei)
			||	(eventCode == Event.MARUHIKAIJYO && ! wfSeigyo.maruhiKaijyo)
			||	(eventCode == Event.SHOUNINKAIJO && ! wfSeigyo.shouninKaijo) /*202207 承認解除追加*/
		) {
			throw new EteamAccessDeniedException();
		}
	}

	/**
	 * データ読み込み（全イベントで処理用 + エラー時の再表示用に）
	 */
	@SuppressWarnings("unchecked")
	protected void loadData() {

		//--------------------
		//伝票種別、伝票レコードの取得
		//--------------------
		GMap denpyouShubetuMap = workflowLogic.selectDenpyouShubetu(denpyouKbn);
		if(denpyouShubetuMap == null) {
			throw new EteamAccessDeniedException(); // アクセス権限なし
		}
		if (! isEmpty(denpyouId)){
			denpyouMap = workflowLogic.selectDenpyou(denpyouId);
			if (denpyouMap == null) throw new EteamDataNotFoundException();
			if (!denpyouKbn.equals(denpyouMap.get("denpyou_kbn"))) throw new EteamDataNotFoundException();
		}

		//--------------------
		//伝票種別、リダイレクト用のPATH取得
		//--------------------
		title = denpyouShubetuMap.get("denpyou_shubetsu").toString();
		// ユーザー定義届書の場合該当バージョン時点の伝票種別を取得
		if(isNotEmpty(denpyouId) && myLogic.isKaniTodoke(denpyouKbn)){
			GMap kaniTodokeNameMap = kaniTodokeCategoryLogic.findKaniTodokeName(denpyouKbn, denpyouMap.get("version").toString());
			title = kaniTodokeNameMap.get("denpyou_shubetsu").toString();
		}
		urlPath = workflowLogic.selectDenpyouShubetu(denpyouKbn).get("denpyou_shubetsu_url").toString();

		//--------------------
		//伝票状態
		//--------------------
		denpyouJoutai = isEmpty(denpyouId) ? DENPYOU_JYOUTAI.MI_KIHYOU : denpyouMap.get("denpyou_joutai").toString();
		denpyouJoutaiMei = systemLogic.findNaibuCdSetting("denpyou_jyoutai", denpyouJoutai).get("name").toString();

		//--------------------
		//伝票共通属性
		//--------------------
		if (isNotEmpty(denpyouId)) {
			Map<String, Boolean> isWfLevelRenkeiMap = EteamCommon.getWfLevelRenkeiMap();
			Boolean flag = isWfLevelRenkeiMap.get(denpyouKbn);
			if (flag == null || flag.booleanValue()) {
				serialNo = !denpyouMap.get("dcno").equals("") ? denpyouMap.get("dcno") : (String.format("%1$08d", denpyouMap.getObject("serial_no")));
			} else {
				// 仕訳後の伝票番号を出力
				serialNo = denpyouMap.get("dcno");
			}
			// 代表負担部門コード
			if (daihyouFutanBumonCd == null) {
				// 初期表示
				daihyouFutanBumonCd = denpyouMap.get("daihyou_futan_bumon_cd").toString();
			} else {
				// 伝票状態が起票中以外の場合
				if (!denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) {
					daihyouFutanBumonCd = denpyouMap.get("daihyou_futan_bumon_cd").toString();
				}
			}

			// マル秘
			maruhiFlg = denpyouMap.get("maruhi_flg").toString();

			// 稟議金額の表示
			this.ringiKingakuHyouji();
		}
		//稟議金額超過コメントの入力内容を表示用項目にセット
		if (null != ringiKingakuChoukaComment)
		{
			this.hyoujiRingiKingakuChoukaComment = this.ringiKingakuChoukaComment;
		}


		//--------------------
		//承認ルート・承認状況
		//起票済なら登録済データを取得
		//参照起票（未登録）の場合、参照元データを取得→参照起票時、元データ承認者表示させる
		//--------------------
		if (isNotEmpty(denpyouId)) {
			shouninRouteList   = workflowLogic.selectShouninRoute(denpyouId);
			procShouninRoute = workflowLogic.findProcRoute(shouninRouteList, getUser());
		} else if(isNotEmpty(sanshouDenpyouId)) {
			shouninRouteList   = workflowLogic.selectShouninRoute(sanshouDenpyouId);
			//起票者と最終承認者の処理権限名を取得→リストにセット
			myLogic.resetShorikengen(denpyouKbn, shouninRouteList);

		}

		//--------------------a
		//差戻し先の承認ルート
		//--------------------
		if (wfSeigyo.sashimodoshi) {
			sashimodoshiSakiList = myLogic.makeSashimodosiSakiList(wfSeigyo);
		}

		//--------------------
		//承認者（画面上部）：起票者除く承認者を「→」区切りで繋げる
		//--------------------
		String delimiter = " → ";
		if (shouninRouteList != null && ! shouninRouteList.isEmpty()) {
			StringBuilder strb = new StringBuilder();
			for(GMap route : shouninRouteList) {
				if(((int) route.get("edano")) > 1) {
					if((int)route.get("edano") > 2){
						strb.append(delimiter);
					}
					if(!(boolean)route.get("isGougi")){
						// 承認者へ表示する値を作成します。
						String gyoumuRoleName = route.get("gyoumu_role_name").toString();
						if(isEmpty(gyoumuRoleName)) {
							strb.append(route.get("bumon_full_name").toString()).append("：");
							strb.append(route.get("user_full_name").toString()).append("（");
							strb.append(route.get("bumon_role_name").toString()).append("）");
						} else {
							strb.append(gyoumuRoleName);
						}
					}else{
						// 合議
						strb.append("【合議】");
						List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
						for(GMap gougiOya : gougiOyaList){
							if((int)gougiOya.get("gougi_edano") > 1){
								strb.append("：");
							}
							strb.append(gougiOya.get("gougi_name").toString());
						}
					}
				}
			}
			shouninSha = strb.toString();
		}

		//--------------------
		//承認状況
		//--------------------
		//起票済なら登録済データを取得、更に現在作業者以降の承認ルートを追加
		if (isNotEmpty(denpyouId)) {
			shouninJoukyouList = workflowLogic.selectShouninJoukyou(denpyouId);
			sousaList = workflowLogic.loadSousaRireki(denpyouId);
		//参照起票なら参照元の承認ルートを全て表示
		} else if(isNotEmpty(sanshouDenpyouId)) {
			shouninJoukyouList = (List<GMap>)((ArrayList<GMap>)shouninRouteList).clone();
		}
		//表示用編集
		if (shouninJoukyouList != null) for(GMap joukyou : shouninJoukyouList) {
			if (isEmpty(joukyou.get("bumon_full_name").toString())) { joukyou.put("bumon_full_name", "-"); }
			if (isEmpty(joukyou.get("user_full_name").toString()))  { joukyou.put("user_full_name",  "-"); }
			if (isEmpty(joukyou.get("bumon_role_name").toString())) { joukyou.put("bumon_role_name",  "-"); }
			joukyou.put("koushin_time", formatTime(joukyou.get("koushin_time")));

			List<GMap> oyaList = (List<GMap>)joukyou.get("gougiOya");
			if (oyaList != null) for(GMap oya : oyaList){
				List<GMap> koList = (List<GMap>)oya.get("gougiKo");
				for(GMap ko : koList){
					if (isEmpty(ko.get("bumon_full_name").toString())) { ko.put("bumon_full_name", "-"); }
					if (isEmpty(ko.get("user_full_name").toString()))  { ko.put("user_full_name",  "-"); }
					if (isEmpty(ko.get("bumon_role_name").toString())) { ko.put("bumon_role_name",  "-"); }
					ko.put("koushin_time", formatTime(ko.get("koushin_time")));
				}
			}
		}
		//表示用編集
		if (sousaList != null) for(GMap joukyou : sousaList) {
			if (isEmpty(joukyou.get("bumon_full_name").toString())) { joukyou.put("bumon_full_name", "-"); }
			if (isEmpty(joukyou.get("user_full_name").toString()))  { joukyou.put("user_full_name",  "-"); }
			if (isEmpty(joukyou.get("bumon_role_name").toString())) { joukyou.put("bumon_role_name",  "-"); }
			joukyou.put("koushin_time", formatTime(joukyou.get("koushin_time")));
		}

		//--------------------
		//起票部門のプルダウン：起票モードの時のみ必要
		//--------------------
		if (wfSeigyo.touroku || (wfSeigyo.koushin && denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU))) {
			if (2 <=loginUserInfo.getBumonCd().length) {
				kihyouBumonList = new ArrayList<GMap>();
				for(int i = 0; loginUserInfo.getBumonCd().length > i; i++) {
					GMap map = new GMap();
					map = new GMap();
					map.put("bumon_cd",        loginUserInfo.getBumonCd()[i]);
					map.put("bumon_full_name", loginUserInfo.getBumonFullName()[i]);
					map.put("bumon_role_id",   loginUserInfo.getBumonRoleId()[i]);
					map.put("bumon_role_name", loginUserInfo.getBumonRoleName()[i]);
					GMap daihyouFutanBumonNm = masterLg.findBumonMasterByCd(loginUserInfo.getDaihyouFutanCd()[i]);
					map.put("daihyou_futan_bumon_cd", daihyouFutanBumonNm == null ? "" : loginUserInfo.getDaihyouFutanCd()[i]);
					kihyouBumonList.add(map);
				}
			}
		}

		//--------------------
		//起票者情報（プルダウン選択 or 文言表示）
		//下記でセットしているのは表示用 or プルダウン選択用のメンバ変数
		//--------------------
		//登録済ならば自伝票の承認ルート（枝番１）をコピー
		if (isNotEmpty(denpyouId)) {
			GMap shouninRoute = shouninRouteList.get(0);
			kihyouUser = shouninRoute.get("user_full_name").toString();
			if (isEmpty(kihyouBumonCd)) { //プルダウンの値をクリアしないように
				setKihyouBumon(shouninRoute);
			}
			// 起票日で設定する（現在日）
			kihyouBi = new SimpleDateFormat("yyyy/MM/dd").format(shouninRoute.get("touroku_time"));
		//参照起票でも現在日時点の部門名を参照させる
		} else if (isNotEmpty(sanshouDenpyouId)) {
			kihyouUser = loginUserInfo.getDisplayUserName();
			if (isEmpty(kihyouBumonCd)) { //プルダウンの値をクリアしないように
				setKihyoushaDefault();
			}
			// 起票日で設定する（現在日）
			Calendar cal = Calendar.getInstance();
		    Date date = cal.getTime();
			kihyouBi = new SimpleDateFormat("yyyy/MM/dd").format(date);

			// 代表負担部門コード
			if (daihyouFutanBumonCd == null) {
				// 初期表示
				GMap daihyouFutanBumonNm = masterLg.findBumonMasterByCd(loginUserInfo.getDaihyouFutanCd()[0]);
				daihyouFutanBumonCd = daihyouFutanBumonNm == null ? "" : loginUserInfo.getDaihyouFutanCd()[0];
			}

		//新規起票ならログインユーザーの第一所属
		} else {
			kihyouUser = loginUserInfo.getDisplayUserName();
			if (isEmpty(kihyouBumonCd)) { //プルダウンの値をクリアしないように
				setKihyoushaDefault();
			}
			// 起票日で設定する（現在日）
			Calendar cal = Calendar.getInstance();
		    Date date = cal.getTime();
			kihyouBi = new SimpleDateFormat("yyyy/MM/dd").format(date);
		}
		//起票モードで登録時点の所属が存在しなければ初期化する
		if (wfSeigyo.touroku || (wfSeigyo.koushin && denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU))) {
			if (!(ArrayUtils.contains(loginUserInfo.getBumonCd(), kihyouBumonCd) && ArrayUtils.contains(loginUserInfo.getBumonRoleId(), bumonRoleId))) {
				setKihyoushaDefault();
			}
		}
		
		//税額修正可否の取得
		zeigakuShuseiFlg = wfSeigyo.zeigakuShusei;
		//入力方式デフォルト表示の取得
		nyuryokuDefaultFlg = wfSeigyo.nyuryokuDefault ? "1" : "0";
		//入力方式変更可否の取得
		nyuryokuHenkouFlg = wfSeigyo.nyuryokuhenkou ? "1" : "0";
		//事業者区分変更可否の取得
		jigyoushaKbnHenkouFlg = wfSeigyo.jigyoushaKbnHenkou ? "1" : "0";
		//インボイス制度開始設定
		invoiceStartFlg = wfSeigyo.invoiceStartCheck;
		//税額計算端数処理
		hasuuShoriFlg = wfSeigyo.ZeigakuHasuuShoriFlg;
		
		//TODO 期別消費税設定の取得 基準とする日がどれかわからないので、とりあえず最新を取れるようにnull
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		shouhizeikbn = dto.shouhizeiKbn; //分離区分のdisable対応の消費税区分
		shiireZeiAnbun = dto.shiireZeigakuAnbunFlg;//仕入区分のenable/非表示の按分法
		shiirezeigakuKeikasothi = Integer.toString(dto.shiirezeigakuKeikasothi); //仕入れ税額控除経過措置適用
		uriagezeigakuKeisan = Integer.toString(dto.uriagezeigakuKeisan);//売上税額計算方式の値取得
		
		tekiyouMaxLength = String.valueOf(kaikeiCommonLogic.tekiyouCheck(Open21Env.getVersion().toString()));
		
		//--------------------
		//ファイル添付
		//--------------------

		//伝票種別と会社設定情報を参照してe文書使用フラグを設定
		ebunshoShiyouFlg = wfSeigyo.ebunsho ? "1" : "0";
		if(ebunshoShiyouFlg.equals("1"))
		{
			setEbushoShubetsuList();
		}
		ebunshoTaishouCheckDefault = setting.ebunshoTaishouCheckDefault();
		ebunshoDenshitsCheckDefault = setting.ebunshoDenshitsCheckDefault();

		ebunshoJouhouHyoujiFlg = "0";
		if (isNotEmpty(denpyouId)) {
			tempFileList = workflowLogic.selectTenpuFile(denpyouId);

			//該当伝票にe文書情報があるなら表示
			//e文書使用設定がオフでも作成時点でe文書情報が設定されていたら表示は行う
			//if(workflowLogic.findEbunshoCount(denpyouId) > 0){

				//エラー等によりページが再表示される場合、編集中のe文書情報があるならそれを代わりにtempFileEbunshoListに設定
				if(tenpuzumi_ebunsho_no != null){
					tempFileEbunshoList = new ArrayList<GMap>();

					int cnt = 0;
					for(int i = 0; i < tenpuzumi_edano.length; i++) {

						GMap tempFileEbunshoMap = new GMap();
						tempFileEbunshoMap.put("edano", tenpuzumi_edano[i]);
						tempFileEbunshoMap.put("file_name", tenpuzumi_filename[i]);
						tempFileEbunshoMap.put("ebunsho_no", tenpuzumi_ebunsho_no[i]);
						tempFileEbunshoMap.put("denshitorihiki_flg", tenpuzumi_denshitorihikiFlg[i]);
						tempFileEbunshoMap.put("tsfuyo_flg", tenpuzumi_tsfuyoFlg[i]);
						tempFileEbunshoMap.put("touroku_time", tenpuzumi_ebunsho_tourokutime[i]);
						List<GMap> ebunshoDatalist = new ArrayList<GMap>();
						int lmt = cnt + Integer.parseInt(tenpuzumi_ebunshodata_count[i]);
						for( int j = cnt ; j < lmt ; j++ ){
							GMap ebunshoDataMap = new GMap();
							ebunshoDataMap.put("ebunsho_shubetsu", tenpuzumi_ebunsho_shubetsu[j]);
							ebunshoDataMap.put("ebunsho_edano", tenpuzumi_ebunsho_edano[j]);
							ebunshoDataMap.put("ebunsho_nengappi", tenpuzumi_ebunsho_nengappi[j]);
							ebunshoDataMap.put("ebunsho_kingaku", tenpuzumi_ebunsho_kingaku[j]);
							ebunshoDataMap.put("ebunsho_hinmei", tenpuzumi_ebunsho_hinmei[j]);
							ebunshoDataMap.put("ebunsho_hakkousha", tenpuzumi_ebunsho_hakkousha[j]);
							ebunshoDatalist.add(ebunshoDataMap);
						}
						cnt = lmt;
						tempFileEbunshoMap.put("ebunshoDatalist",ebunshoDatalist);

						tempFileEbunshoList.add(tempFileEbunshoMap);
					}

				}else{
					//該当伝票のe文書情報をDBから取得
					tempFileEbunshoList = workflowLogic.selectTenpufileListWithEbunshoNo(denpyouId);
					for(GMap tempFileEbunshoMap : tempFileEbunshoList) {

						//e文書情報があれば取得して格納
						if( (tempFileEbunshoMap.get("ebunsho_no")) != null){
							String ebunshoNo = (String) tempFileEbunshoMap.get("ebunsho_no");
							List<GMap> ebunshoDatalist = workflowLogic.selectEbunshoDatalist(ebunshoNo);
							for(GMap EbunshoDataMap : ebunshoDatalist) {
								EbunshoDataMap.put("ebunsho_nengappi", formatDate(EbunshoDataMap.get("ebunsho_nengappi")));
							}
							tempFileEbunshoMap.put("ebunshoDatalist",ebunshoDatalist);
						}
					}
				}
				ebunshoJouhouHyoujiFlg = "1";
			//}
		}

		//添付されていたら設定によらず参照のみは可能にする。
		if (tempFileList != null && tempFileList.size() > 0)
		{
			wfSeigyo.fileTenpuRef = true;
		}

		//--------------------
		// 任意メモの表示用編集
		//--------------------
		if (isNotEmpty(denpyouId)) {
			niniMemoList = workflowLogic.selectNiniComment(denpyouId);
			for(GMap niniMemoMap : niniMemoList) {
				niniMemoMap.put("koushin_time", formatTime(niniMemoMap.get("koushin_time")));
			}
		}

		//---------------------
		// 関連伝票
		//---------------------
		// 入力欄表示フラグが有効であるか判定
		kanrenFlg = workflowLogic.isUsableKanrenDenpyou(denpyouKbn);

		if (kanrenDenpyouId != null) {
			// 関連伝票リスト数を取得
			kanrenCount = kanrenDenpyouId.length;
		}

		//---------------------
		// 基準日
		//---------------------
		kijunbi = judgeKijunbi();

		//---------------------
		// 予算チェック
		//---------------------
		if(myLogic.isDispYosanCheckSection(denpyouKbn)) {
			isDispYosanCheckSection = "1";
			monthList = createYosanCheckNengetsuList();
		} else {
			isDispYosanCheckSection = "0";
			monthList = null;
			yosanCheckNengetsu = null;
		}

		// 添付ファイルプレビュー表示有無
		// プレビュー機能関係初期情報（承認者 && 種別OK && 添付ファイルのプレビューON）
		this.isPreviewTargetDenpyou = this.wfSeigyo.shounin
				&& !List.of("A007", "A008").contains(this.denpyouKbn) && !this.denpyouKbn.contains("B")
				&& this.systemLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.PREVIEW_TENPU_FILE);

		// プレビュー対象の時の一時ファイル作成処理
		if(this.isPreviewTargetDenpyou)
		{
			this.loadTenpuFilePreview();
		}
	}

	/**
	 * 予算執行対象月を初期化
	 * @return 予算執行対象月
	 */
	protected String initYosanCheckNengetsu() {
		if("1".equals(isDispYosanCheckSection)) {
			if(isNotEmpty(denpyouId)){
				//保存された予算チェック年月
				return denpyouMap.get("yosan_check_nengetsu");
			}else {
				//操作日が属する財務年月
				SimpleDateFormat formatKey = new SimpleDateFormat("yyyyMM");
				return formatKey.format(masterLg.findKiKesn(new java.sql.Date(System.currentTimeMillis())).get("to_date"));
			}
		}
		//予算執行対象月が非表示の場合
		return null;
	}

	/**
	 * e文書種別リストを会社設定に基づいて設定する
	 */
	protected void setEbushoShubetsuList()
	{
		List<GMap> NaibuCdSetting = systemLogic.loadNaibuCdSetting("ebunsho_shubetsu");

		String[] ebunshoShubetsuSetting = {};

		if (myLogic.isKaniTodoke(denpyouKbn))
		{
			ebunshoShubetsuSetting = setting.ebunshoShubetsuB000().split(",");
		}
		else
		{
			switch (denpyouKbn) {
			// 伝票区分応じて設定項目を取得
			case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA001().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA002().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA003().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA004().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA005().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA006().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA009().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.KOUTSUUHI_SEISAN:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA010().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA011().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA012().split(",");
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
				ebunshoShubetsuSetting = setting.ebunshoShubetsuA013().split(",");
				break;
			default:
				break;
			}
		}

		List<String> ebunshoShubetsuList = Arrays.asList(ebunshoShubetsuSetting);

		ebushoShubetsuList = NaibuCdSetting.stream()
				.filter(s -> ebunshoShubetsuList.contains(s.get("naibu_cd")))
				.collect(Collectors.toList());
	}

	/**
	 * e文書種別のドメインを取得する
	 * @return 会社設定により制御された指定可能な種別リスト
	 */
	protected String[] getEbunshoShubetsuDomain()
	{
		if (ebushoShubetsuList == null)
		{
			setEbushoShubetsuList();
		}
		return ebushoShubetsuList.stream().map(e -> e.get("naibu_cd")).toArray(s -> new String[s]);
	}

	/**
	 * 起票者情報に承認ルート（枝番１）をセット（参照起票時、起票済の場合）
	 * @param shouninRoute 承認ルート（枝番１）
	 */
	protected void setKihyouBumon(GMap shouninRoute) {
		kihyouBumonCd = shouninRoute.get("bumon_cd").toString();
		kihyouBumonName = shouninRoute.get("bumon_full_name").toString();
		bumonRoleId = shouninRoute.get("bumon_role_id").toString();
		bumonRoleName = shouninRoute.get("bumon_role_name").toString();
	}
	/**
	 * 起票者情報にログインユーザーをセット（新規起票時のみ）
	 */
	protected void setKihyoushaDefault() {
		if (0 < loginUserInfo.getBumonCd().length) {
			kihyouBumonCd = loginUserInfo.getBumonCd()[0];
			kihyouBumonName = loginUserInfo.getBumonFullName()[0];
			bumonRoleId = loginUserInfo.getBumonRoleId()[0];
			bumonRoleName = loginUserInfo.getBumonRoleName()[0];
			GMap daihyouFutanBumonNm = masterLg.findBumonMasterByCd(loginUserInfo.getDaihyouFutanCd()[0]);
			daihyouFutanBumonCd = daihyouFutanBumonNm == null ? "" : loginUserInfo.getDaihyouFutanCd()[0];
		}
	}

	/**
	 * 添付ファイルからe文書ファイルを作成し、e文書ファイルテーブルに登録します。
	 * @param filenameEbunshoMap ファイル名と添付ファイル枝番号の紐づけリスト
	 * @return result
	 */
	protected String tourokuEbunshoFile(Map<String , Ebunsho> filenameEbunshoMap){
		if (ebunshoflg == null)
		{
			return "success";
		}

		int originalErrorListCount = this.errorList.size();
		boolean isErrorAdded = false;
		if(tenpuzumi_ebunshoflg != null && tenpuzumi_ebunshoflg.length > 0) {
			for(int i = 0; i < tenpuzumi_ebunshoflg.length; i++) {
				if ( !("1".equals(tenpuzumi_ebunshoflg[i])) )
				{
					continue;
				}
				String tmpfileNm = tenpuzumi_filename[i];
				if (isEmpty(tmpfileNm))
				{
					continue;
				}
				Ebunsho e = filenameEbunshoMap.get(tmpfileNm);
				if (e.now) {
					String ebunshoNo = e.getEbunshoNo();
					Integer edano = e.getEdano();

					//添付ファイルをPDFに変換(元からPDFの場合はファイル名のみ変更)
					byte[] pdf = myLogic.createPdfData(denpyouId, edano);

					//電子取引・TS付与チェックボックスがそれぞれ両方オフか両方オンの時には
					//PDFにタイムスタンプ付与、成功時は登録日時指定
					if(tenpuzumi_denshitorihikiFlg[i] != null && tenpuzumi_denshitorihikiFlg[i].equals(tenpuzumi_tsfuyoFlg[i])){
						
						// 電子取引OFFの時のみ解像度チェックする
						if(tenpuzumi_denshitorihikiFlg[i].equals("0"))
						{
							this.checkEbunshoImages(pdf, i);
						}
	
						if(!isErrorAdded && this.errorList.size() > originalErrorListCount)
						{
							isErrorAdded = true;
							continue;
						}
	
						if(!isErrorAdded)
						{
// Ver22.09.30.00 e文書同時実行対応（3次版）*-							
// StampedPdf stamped = eLogic.addTimeStamp(pdf);
							StampedPdf stamped = eLogic.addTimeStamp(pdf, denpyouId, edano);
// -*							
							
							ebunshoFileDao.insertEbunshoFile(denpyouId, edano, ebunshoNo, stamped.getPdf(), tenpuzumi_denshitorihikiFlg[i], tenpuzumi_tsfuyoFlg[i], loginUserId, stamped.getTimeStamp());
						}
					//電子取引オン・TS付与オフの場合は
					//PDFをそのまま登録、日付は処理実行時点日付をダミーで登録
					}else{
						ebunshoFileDao.insertEbunshoFile(denpyouId, edano, ebunshoNo, pdf, tenpuzumi_denshitorihikiFlg[i], tenpuzumi_tsfuyoFlg[i], loginUserId, new java.util.Date(System.currentTimeMillis()));
					}
				}
			}
		}

		//新規添付ファイルの数だけ繰り返す
		for(int i = 0; i < ebunshoflg.length; i++) {

			//該当添付ファイルのe文書使用フラグがOFFか、「選択されていません」の状態ならスキップ
			if ( !("1".equals(ebunshoflg[i])) )
			{
				continue;
			}
			String fileNm = ebunsho_tenpufilename_header[i];
			if (isEmpty(fileNm))
			{
				continue;
			}
			//ファイル名に紐づく添付ファイル枝番・e文書番号を取得
			//紐づけリストにデータが無ければe文書ファイル作成済みとみなしスキップ
			//e文書新規採番した添付ファイルに対して、タイムスタンプを付けてe文書ファイルレコードに入れる
			Ebunsho e = filenameEbunshoMap.get(fileNm);
			if (e.now) {
				String ebunshoNo = e.getEbunshoNo();
				Integer edano = e.getEdano();

				//添付ファイルをPDFに変換(元からPDFの場合はファイル名のみ変更)
				byte[] pdf = myLogic.createPdfData(denpyouId, edano);

				//電子取引・TS付与チェックボックスがそれぞれ両方オフか両方オンの時には
				//PDFにタイムスタンプ付与、成功時は登録日時指定
				if(denshitorihikiFlg[i] != null && denshitorihikiFlg[i].equals(tsfuyoFlg[i])){
					
					// 電子取引OFFの時のみ解像度チェックする
					if(denshitorihikiFlg[i].equals("0"))
					{
						this.checkEbunshoImages(pdf, i + (this.tenpuzumi_ebunshoflg == null ? 0 : this.tenpuzumi_ebunshoflg.length));
					}

					if(!isErrorAdded && this.errorList.size() > originalErrorListCount)
					{
						isErrorAdded = true;
						continue;
					}

					if(!isErrorAdded)
					{
// Ver22.09.30.00 e文書同時実行対応（3次版）*-							
// StampedPdf stamped = eLogic.addTimeStamp(pdf);
						StampedPdf stamped = eLogic.addTimeStamp(pdf, denpyouId, edano);
//-*
						
						ebunshoFileDao.insertEbunshoFile(denpyouId, edano, ebunshoNo, stamped.getPdf(), denshitorihikiFlg[i], tsfuyoFlg[i], loginUserId, stamped.getTimeStamp());
					}
				//電子取引オン・TS付与オフの場合は
				//PDFをそのまま登録、日付は処理実行時点日付をダミーで登録
				}else{
					ebunshoFileDao.insertEbunshoFile(denpyouId, edano, ebunshoNo, pdf, denshitorihikiFlg[i], tsfuyoFlg[i], loginUserId, new java.util.Date(System.currentTimeMillis()));
				}
			}
		}

		return isErrorAdded ? "error" : "success";
	}

	/**
	 * @param pdf pdfバイト配列
	 * @param i インデックス
	 */
	protected void checkEbunshoImages(byte[] pdf, int i)
	{
		PDDocument pdfDoc = null; // いきなりロードしたいが、それをするとエラー時にcloseを強制できないのでnullで初期化
		// PDFのチェック処理
		try
		{
			try
			{
				pdfDoc = PDDocument.load(pdf);
			}
			catch(Exception e)
			{
				this.errorList.add(String.format("添付ファイル：%d行目のファイルは、正常なPDFファイルとして読み込めません。", i + 1));
				return;
			}

			var pdfPages = pdfDoc.getPages();

		    for (PDPage page : pdfPages) {
		    	String imageCheckResult = PdfImageChecker.CheckImage(page);

		    	if(!imageCheckResult.equals("success"))
		    	{
		    		this.errorList.add(String.format("添付ファイル：%d行目のファイルで、%s", i + 1, imageCheckResult));
		    	}
			}
		}
		catch (Exception e1)
		{
			String errorMessage = "error: " + Arrays.toString(e1.getStackTrace());
			this.errorList.add(errorMessage);
		}
		finally
		{
		    try
		    {
				pdfDoc.close();
			}
		    catch (Exception e1)
		    {
			}
		}
	}

	/**
	 * e文書チェック、Shift-JISで対応できないファイル名チェックでエラーになったときの後処理。今のところ簡易届専用。
	 */
	protected void afterFileCheckError()
	{
	}
	
	/**
	 * JSP新規添付ファイル情報部からe文書明細情報を取得し、e文書データテーブルに登録します。
	 * @param filenameEbunshoMap ファイル名とe文書番号の紐づけリスト
	 */
	protected void tourokuEbunshoData(Map<String , Ebunsho> filenameEbunshoMap){
		//idx1がe文書単位、idx2がe文書配下の明細単位
		int idx1 = 0, idx2 = 0;

		//ebunsho_tenpufilenameに格納されているデータだけテーブルに情報登録を繰り返す
		 for(int i = 0; i < ebunsho_tenpufilename.length; i++) {
			if(! ebunsho_tenpufilename[i].isEmpty()) {
				String fileNm = ebunsho_tenpufilename_header[idx1];

				ebunshoDataDao.insertEbunshoData(
						filenameEbunshoMap.get(fileNm).ebunshoNo
						, Integer.parseInt(ebunsho_shubetsu[i])
						, toDate(ebunsho_nengappi[i])
						, toDecimal(ebunsho_kingaku[i])
					    // Ver22.03.09.00 タブ文字削除
						, ebunsho_hakkousha[i].replace("\t", "")
					    // Ver22.03.09.01 タブ文字削除
						, ebunsho_hinmei[i].replace("\t", "")
						);
			}
			idx2++;
			if (idx2 == Integer.parseInt(ebunsho_meisaicnt[idx1])) {
				idx1++;
				idx2 = 0;
			}
		}

	}

	/**
	 * JSP添付済ファイル情報部からe文書明細情報を取得し、e文書データテーブルを更新します。
	 * @param filenameEbunshoMap ファイル名とe文書番号の紐づけリスト
	 */
	protected void koushinEbunshoData(Map<String , Ebunsho> filenameEbunshoMap){

		if (tenpuzumi_ebunsho_no == null)
		{
			return;
		}
		//添付ファイルの区切り毎にe文書番号に設定されている旧データを削除
		String ebunshoNoOld = "";
		for(int i = 0; i < tenpuzumi_ebunsho_no.length; i++) {
			String ebunshoNo = tenpuzumi_ebunsho_no[i];
			if(!(ebunshoNoOld.equals(ebunshoNo))){
				ebunshoDataDao.delete(ebunshoNo);
				ebunshoNoOld = ebunshoNo;
			}
		}

		int lastIndexOfMeisai = 0;
		if(eLogic == null){
			eLogic = EteamContainer.getComponent(EbunshoLogic.class);
		}
		for(int i = 0; i < tenpuzumi_filename.length; i++) {
			//if(tenpuzumi_ebunsho_no[i].isEmpty()) continue;
// String kakuchoushi = EteamIO.getExtension(tenpuzumi_filename[i]);
			//20220509 ファイル名からe文書番号が取得できた場合のみ　とする
			String ebunshoNo = "";
			if(tenpuzumi_ebunshoflg[i] == null || "0".equals(tenpuzumi_ebunshoflg[i])) {
				// if(!"pdf".equals(kakuchoushi) && !"bmp".equals(kakuchoushi) && !"jpeg".equals(kakuchoushi) && !"jpg".equals(kakuchoushi)) {
				if(!eLogic.isEbunshoOrgFile(tenpuzumi_filename[i])) {
					//notEcnt++;
				}else {
					lastIndexOfMeisai++;
				}
				continue;
			}
			String fileNm = tenpuzumi_filename[i];
			if(filenameEbunshoMap.get(fileNm) == null) {
				continue;
			}
			ebunshoNo = filenameEbunshoMap.get(fileNm).ebunshoNo;
			if(ebunshoNo == null || ebunshoNo.isEmpty()) {
				continue;
			}

			int lmt = Integer.parseInt(tenpuzumi_ebunshodata_count[i]);
			//String ebunshoNo = tenpuzumi_ebunsho_no[i];
			for( int j = lastIndexOfMeisai ; j < lastIndexOfMeisai + lmt ; j++ ){
				ebunshoDataDao.insertEbunshoData(
						ebunshoNo
						, Integer.parseInt(tenpuzumi_ebunsho_shubetsu[j])
						, toDate(tenpuzumi_ebunsho_nengappi[j])
						, toDecimal(tenpuzumi_ebunsho_kingaku[j])
						// Ver22.03.09.00 タブ文字削除
						, tenpuzumi_ebunsho_hakkousha[j].replace("\t", "")
						// Ver22.03.09.01 タブ文字削除
						, tenpuzumi_ebunsho_hinmei[j].replace("\t", "")
						);
			}
			lastIndexOfMeisai += lmt;

			//電子取引オン・TS付与オンでタイムスタンプ付与に失敗したデータについて
			//電子取引オン・TS付与オフのデータに変更されている場合はファイルテーブル情報更新
			if("1".equals(tenpuzumi_denshitorihikiFlg[i]) && "0".equals(tenpuzumi_tsfuyoFlg[i]) && isEmpty(tenpuzumi_ebunsho_tourokutime[i])) {
				ebunshoFileDao.updateEbunshoTSfuyoFlg(ebunshoNo, "0", new java.util.Date(System.currentTimeMillis()));
			}
		}

	}

	/**
	 * 伝票に添付ファイルが必要であるかのチェックを行う。
	 */
	protected void checkTenpFileHissu() {

		if(myLogic.isUseEbunsho(denpyouKbn) == true){
			if(isUseShouhyouShorui() == true){
				//e文書添付チェック
				isEbunshoAppended();
			}
		}else{
			if(myLogic.isCheckTenpFile(denpyouKbn) == true){
				if(isUseShouhyouShorui() == true){
					//通常ファイル添付チェック
					isTenpuFileAppended();
				}
			}
		}
	}

	/**
	 * 伝票にe文書ファイルが最低1つ以上添付されているか確認する。
	 * 添付されていない場合はerrorListにエラーメッセージを格納する。
	 * @return e文書が添付されていればtrue
	 */
	protected boolean isEbunshoAppended() {

		boolean result = false;

		//添付済みe文書の確認
		if(!isEmpty(denpyouId)){
			if(workflowLogic.findEbunshoCount(denpyouId) > 0){
				result = true;
				return result;
			}
		}

		//添付済みファイルの確認
		if (!isTenpuFileAppended())
		{
			return false;
		}

		String ebunshoShubetsu = "";
		//添付ファイルに1件以上e文書が設定されているか確認
		if(ebunshoflg != null){
			for(int i = 0; i < ebunshoflg.length; i++) {
				if( ("1".equals(ebunshoflg[i])) &&  !((isEmpty(ebunsho_tenpufilename_header[i]))) ){
					result = true;
					break;
				}
				else
				{
					ebunshoShubetsu = ebunsho_shubetsu[i];
				}
			}
		}
		if(result == false){
			// 複数行あっても1行分だけ出力
			String name = getEbushoShubetsuName(ebunshoShubetsu);
			errorList.add(name + "の検索項目が入力されていないか、又は" + name + "が添付されていません。");
		}

		return result;
	}

	/**
	 * e文書種別の名称を取得
	 * @param ebunshoShubetsu e文書種別(コード)
	 * @return e文書種別(名称)
	 */
	protected String getEbushoShubetsuName(String ebunshoShubetsu)
	{
		if (ebushoShubetsuList == null)
		{
			setEbushoShubetsuList();
		}
		return ebushoShubetsuList.stream().filter(x -> x.get("naibu_cd").equals(ebunshoShubetsu)).map(y -> y.getString("name")).findFirst().orElse(null);
	}

	/**
	 * 伝票に添付ファイルが最低1つ以上添付されているか確認する。
	 * 添付されていない場合はerrorListにエラーメッセージを格納する。
	 * @return 添付ファイルがあればtrue
	 */
	protected boolean isTenpuFileAppended(){
		boolean result = true;
		boolean tenpuzumiFlg = false;

		//添付済みファイルの確認
		if(!isEmpty(denpyouId)){

			List<GMap> tpBf = workflowLogic.selectTenpuFile(denpyouId);
			if(!(tpBf.isEmpty())){
				tenpuzumiFlg = true;
			}
		}

		//添付ファイルが存在しない場合
		if(uploadFile == null && tenpuzumiFlg == false){
			errorList.add("領収書・請求書等のファイルを添付してください。");
			return false;
		}
		return result;
	}

	/** 指定したe文書番号のPDFファイルに対してタイムスタンプ付与処理・ebunsho_fileテーブル更新を実施します。
	 * @return 1:タイムスタンプ付与成功 -1:タイムスタンプ付与失敗
	 */
	public String addPDFTimeStampForEbunshoNo(){
		connection = EteamConnection.connect();
		try {
			initialize();
			//アクセス制御
			accessCheck(Event.KOUSHIN);

			// 添付ファイルデータ取得(ファイル名はe文書番号設定)
			GMap map = workflowLogic.findTenpuEbunshoFile(addTimestampEbunshoNo);
			if (null == map) throw new EteamDataNotFoundException();

			// 取得したPDFファイルにタイムスタンプ付与
// Ver22.09.30.00 e文書同時実行対応（3次版）*-
// StampedPdf stamped = eLogic.addTimeStamp((byte[])map.get("binary_data")) 
			StampedPdf stamped = eLogic.addTimeStamp((byte[])map.get("binary_data"), denpyouId, map.get("edano"));
//-*
			if (stamped.getTimeStamp() != null) {
				ebunshoFileDao.updateEbunshoTimestamp(addTimestampEbunshoNo, stamped.getPdf(), stamped.getTimeStamp());
			}
			connection.commit();
			return returnString();
		} finally {
			connection.close();
		}
	}

	/** ActionPathの戻り値を返す
	 * @return ActionPathの戻り値
	 */
	protected String returnString() {
		return isEmpty(version) ? "success" : "success_version";
	}

	/**
	 * ebunsho_tenpufilename_headerのファイル名を変更後(重複で_01とか付けた方)に合わせる
	 *
	 * @param orgFileNames アップロード時点ファイル名
	 * @param newFileNames リネーム後ファイル名
	 */
	protected void ebunshoTenpufileRename (String[] orgFileNames, String[] newFileNames) {
		int jNext = 0;
		for (int i = 0; i < ebunsho_tenpufilename_header.length; i++) {
			if (isNotEmpty(ebunsho_tenpufilename_header[i])) {
				for (int j = jNext; orgFileNames != null && j < orgFileNames.length; j++) {
					if (new File(ebunsho_tenpufilename_header[i]).getName().replace("\u00a0"," ").equals(orgFileNames[j].replace("\u00a0"," "))) { // どちらかもしくは両方のNBSPが勝手に置き換わることがあり、equals比較が壊れるので必要
						ebunsho_tenpufilename_header[i] = newFileNames[j];
						jNext = j + 1;
						break;
					}
				}

				this.ebunsho_tenpufilename_header[i] = this.ebunsho_tenpufilename_header[i].replace("C:\\fakepath\\", ""); // 原因不明だが、無理やり中身を比較すると特殊文字の時に出てきたので消してみる
			}
		}
	}

	/**
	 * 部門推奨ルートの基準金額を取得。
	 * デフォルトでは親子共有するメンバ変数kingakuを返すので、子で特殊な制御をする場合はオーバーライド。
	 * @return 金額
	 */
	public String getKingaku() {
		return kingaku;
	}

	/**
	 * 基準日の取得
	 * @return 基準日
	 */
	public String judgeKijunbi() {
		if(denpyouJoutai.equals(DENPYOU_JYOUTAI.MI_KIHYOU) || denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) {
			return formatDate(new Date(System.currentTimeMillis()));
		}else{
			GMap tmpMap = workflowLogic.selectShinseiDate(denpyouId);
			return (null != tmpMap)? formatDate(tmpMap.get("koushin_time")) : formatDate(new Date(System.currentTimeMillis()));
		}
	}

	/**
	 * ログイン中ユーザの該当伝票IDに関する通知を既読に変更する。
	 * @return 更新件数
	 */
	protected int updateTsuuchi(){

		final String sql = "UPDATE tsuuchi " +
				 "   SET kidoku_flg = '" + KIDOKU_FLG.KIDOKU + "', " +
				 "       koushin_user_id = ?, " +
				 "       koushin_time = current_timestamp " +
				 " WHERE denpyou_id = ? " +
				 "   AND user_id = ? " +
				 "   AND kidoku_flg = '" + KIDOKU_FLG.MIDOKU + "' ";
		String userId = loginUserInfo.getSeigyoUserId();
		int ret = connection.update(sql, userId, denpyouId, userId );
		return ret;
	}

	/**
	 * 起案番号運用関連情報取得<br>
	 * 起案番号番号運用に関連する制御情報を取得する。<br>
	 * ただし、起案番号運用をしないと判断された場合、制御情報は初期値（起案番号運用しない設定）となる。
	 * <ul>
	 * <li>起案番号簿選択ダイアログ表示フラグ
	 * <br>- 起案運用するときに、「実施起案」「支出起案」「対象外」であれば表示する。</li>
	 * <li>起案番号項目表示フラグ／起案番号項目表示データ</li>
	 * <li>起案添付セクション表示フラグ／／</li>
	 * <li>起案終了表示フラグ／起案終了データ</li>
	 * </ul>
	 */
	protected void getKianbangouKanrenInfo(){

		/*
		 * 起案番号運用対象を確認する。
		 */
		if (!myLogic.isUsableKianBangou(denpyouKbn)){
			// 対象外の場合は処理しない
			return;
		}

		/*
		 * 伝票の「予算執行対象」を取得する。
		 */
		String yosanShikkouTaishouCd = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);

		/*
		 * 伝票起案情報を取得する。
		 */
		GMap mapDenpyouKianHimozuke = myLogic.selectDenpyouKianHimozuke(denpyouId);

		/*
		 * 起案番号簿選択ダイアログ表示フラグの設定（0:表示しない 1:表示する）
		 * （申請ボタン押下時のダイアログ表示制御）
		 */
		this.isDispKianbangouBoDialog = myLogic.judgeIsDispKianbangouBoDialog(yosanShikkouTaishouCd);

		/*
		 * 起案番号項目表示フラグの設定（0:表示しない 1:表示する 2:アンカー表示のみ）
		 */
		this.isDispKianbangou = myLogic.judgeIsDispKianbangou(
											yosanShikkouTaishouCd,
											null != mapDenpyouKianHimozuke && null == mapDenpyouKianHimozuke.get("jisshi_kian_bangou"));


		// 伝票起案情報から起案番号簿文字列を取得する。
		// 取得データが null（起案番号未選択）の場合はブランク表示
		this.kianBangou = null == mapDenpyouKianHimozuke? "" : myLogic.getHyoujikianBangou(yosanShikkouTaishouCd, mapDenpyouKianHimozuke);

		/*
		 * 起案添付セクション表示フラグ（0:表示しない 1:表示する）
		 */
		this.isDispKiantenpuSection = myLogic.judgeIsDispKiantenpuSection(
													yosanShikkouTaishouCd, denpyouId,
													null != mapDenpyouKianHimozuke && null != mapDenpyouKianHimozuke.get("jisshi_kian_bangou"));

		/*
		 * 起案伝票紐付け確認フラグ（0:紐付け確認しない 1:紐付け確認する）
		 */
		this.kianHimodukeFlg = myLogic.judgeKianHimozukeFlg(yosanShikkouTaishouCd, denpyouId);


		/*
		 * 起案終了表示フラグ（0:表示しない 1:表示する）
		 */

		// 伝票状態により表示要否を決定する。
		switch (denpyouJoutai) {
		case DENPYOU_JYOUTAI.SHINSEI_CHUU:
		case DENPYOU_JYOUTAI.SYOUNIN_ZUMI:
			this.kianShuuryouFlg = myLogic.judgeKianShuuryou(yosanShikkouTaishouCd, super.getUser(), this.getKihyouUserId(), mapDenpyouKianHimozuke);
			this.isDispKianShuuryou = myLogic.judgeDispKianShuuryou(yosanShikkouTaishouCd, super.getUser(), this.getKihyouUserId(), isKaribaraiMishiyouSeisanZumi(connection), this.kianShuuryouFlg);
			break;
		default:
			// 表示しない系
			break;
		}
	}


	/**
	 * CSVアップロード時の起案伝票IDの整合性をチェックします。
	 */
	protected void checkKianDenpyouId() {

		// 検索を実施する。
		List<GMap> list =  myLogic.loadKianTsuikaDenpyou(denpyouKbn, kihyouBumonCd);
		if (list.isEmpty()) {
			errorList.add("起案添付可能な起案伝票が存在しません。");
			return;
		}

		//起案伝票IDの整合性チェック
		String matchFlg = "0";
		for (GMap aMap : list) {
			if((kianDenpyouId[0].equals(aMap.get("denpyou_id")))) {
				matchFlg = "1";
				break;
			}
		};
		if(matchFlg.equals("0")) {
			errorList.add("起案添付可能な起案伝票IDを入力してください。");
		}
	}


	/**
	 * 添付ファイル合計容量のチェックを行う。
	 */
	protected void checkTenpuFileSize() {
		if (uploadFile == null || uploadFile.length == 0){
			//追加添付がなければチェックしない
			return;
		}

		//会社設定「添付ファイル容量」の値を取得(0ならチェックしない)
		int setMB = Integer.parseInt(setting.tenpuSizeSeigen());
		if (setMB == 0)
		{
			return;
		}

		long maxSizeByte = MBSIZE_BYTE * setMB;
		long currentSize = 0;

		//既に添付されているファイルの合計サイズを追加
		if(denpyouId != null && !denpyouId.isEmpty()){
			long tenpuzumiSize = workflowLogic.findTenpuFileSize(denpyouId);
			currentSize += tenpuzumiSize;
		}

		//今回添付分の合計サイズを算出
		if (uploadFile != null && uploadFile.length != 0){
			for(int i = 0; i < uploadFile.length; i++) {
				long fileSize = uploadFile[i].length();
				currentSize += fileSize;
			}
		}

		if(currentSize > maxSizeByte){
			errorList.add("添付ファイルの合計サイズが制限容量("+ setMB + "MB)を超過しています。");
			return;
		}
	}

	/**
	 * Shift-JISで対応できないファイル名が含まれているか確認。
	 */
	protected void checkTenpuFileNameShiftJIS() {
		if (uploadFile == null || uploadFile.length == 0){
			//追加添付がなければチェックしない
			return;
		}

		//変換対象の文字(nbsp、半角カンマ、波ダッシュ)は先に変換してからチェック
		String[] chkFileName = myLogic.renameFile("dmyID", uploadFileFileName);
		for(int i = 0; i < chkFileName.length; i++) {
			checkSJIS(chkFileName[i], "添付ファイル名[" + chkFileName[i] + "]");
		}
		return;
	}

	/**
	 * 入力内容が予算チェック対象外かどうか判定する。
	 * @return 入力内容が予算チェック対象外の場合true;
	 */
	protected boolean judgeYosanCheckTaisyougai() {
		if(isEmpty(denpyouId)){
			return false;
		}

		String yosanShikkouTaishouCd = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);
		if(YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishouCd)){
			return false;
		}

		return isCheckTaishougaiBumonKamoku(connection);
	}

	/**
	 * 予算チェック対象外かどうかの判定を再取得。起案番号採番時に実施。
	 * @return 結果
	 */
	public String resetIsYosanCheckTaishougai(){

		connection = EteamConnection.connect();
		try{
			boolean yosanCehckTaishougaiFlg = isCheckTaishougaiBumonKamoku(connection);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.print(yosanCehckTaishougaiFlg);
			out.flush();
			out.close();

		} catch (IOException ex) {
		} finally {
			connection.close();
		}

		return "success";
	}

	/**
	 * 予算執行対象月の選択リストを取得
	 * @return 予算執行対象月の選択肢リスト
	 */
	protected List<GMap> createYosanCheckNengetsuList(){
		GMap shoriNengetsu = masterLg.findYosanShikkouShoriNengetsu();
		List<GMap> ret = null;
		if( shoriNengetsu == null) {
			ret = myLogic.createKiKesnNengetsuList();
		}else{
			SimpleDateFormat formatKey = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat formatVal = new SimpleDateFormat("yyyy年MM月");

			ret = masterLg.loadKesnToDateAll();
			Iterator<GMap> it = ret.listIterator();
			while(it.hasNext()) {
				Map<String, Object> month = it.next();
				if( formatKey.format(month.get("to_date")).compareTo(shoriNengetsu.get("from_nengetsu")) < 0
						|| formatKey.format(month.get("to_date")).compareTo(shoriNengetsu.get("to_nengetsu")) > 0 ) {
					it.remove();
				}else {
					month.put("key", formatKey.format(month.get("to_date")));
					month.put("val", formatVal.format(month.get("to_date")));
				}
			}
		}
		return ret;
	}

	/**
	 * 予算執行対象月のチェック用ドメインを取得
	 * @return 予算執行対象月のフォーマットチェック用ドメイン
	 */
	protected String[] getYosanCheckNengetsuDomain(){
		List<GMap> List = createYosanCheckNengetsuList();
		return List.stream().map(e -> e.get("key")).toArray(s -> new String[s]);
	}

	/**
	 * denpyou_shubetsu_ichiran.shiiresaki_flgが1の場合、取引先が仕入先であるかチェック
	 * @param label 取引先コードのエラー文言用名称
	 * @param torihikisakiCd 取引先コード
	 * @param denpyouKbn4DenpyouKanri 伝票区分。申請の伝票区分ではなく仕入先フラグを紐つける伝票区分。
	 */
	protected void checkShiiresaki(String label, String torihikisakiCd, String denpyouKbn4DenpyouKanri) {
		if(isNotEmpty(torihikisakiCd)) {
			GMap denpyouKanri = kihyouNaviLogic.findDenpyouKanri(denpyouKbn4DenpyouKanri);
			boolean shiiresakiFlg = denpyouKanri.get("shiiresaki_flg").equals("1");
			if(shiiresakiFlg) {
				if(masterLg.findTorihikisakiJouhou(torihikisakiCd, true) == null) {
					errorList.add(label + "に仕入先ではない取引先を入力することはできません。");
				}
			}
		}
	}
	/**
	 * インボイスフラグの初期値を返却
	 * <br>新規起票時の読み込み用
	 * @return インボイスフラグの初期値
	 * 
	 */
	protected String setInvoiceFlgWhenNew() {
		return "1".equals(invoiceStartFlg)
			? (!"A002".equals(denpyouKbn) && !"A005".equals(denpyouKbn)&& !"A012".equals(denpyouKbn) && !denpyouKbn.contains("B"))
				? "0"
				:"1"
			:"1";
	}
	
	/**
	 * 注記取得共通処理。最後のに引数は、プロパティの共通化が進めば不要になるはず。
	 * @param sanshou 参照
	 * @param shinseiData 申請データ
	 * @param meisai 対象となる明細
	 * @param denpyouKbn 伝票区分
	 * @param batchKaikeiCommonLogic バッチ会計共通ロジック
	 * @param kaikeiCommonLogic 会計共通ロジック
	 * @return 注記1, 2
	 */
	protected String[] setChuuki(boolean sanshou, GMap shinseiData, GMap meisai, String denpyouKbn, BatchKaikeiCommonLogic batchKaikeiCommonLogic, KaikeiCommonLogic kaikeiCommonLogic)
	{
		String[] chuuki = new String[] { "", "" };
		if(!sanshou){
			String shiwakeTekiyouNoCut = batchKaikeiCommonLogic.shiwakeTekiyou(denpyouKbn, shinseiData, meisai, "0"); // 0以外の場合の出現箇所があるならここの引数化が必要になる
			String shiwakeTekiyou = batchKaikeiCommonLogic.cutTekiyou(shiwakeTekiyouNoCut);
			
			if(EteamCommon.getByteLength(shiwakeTekiyouNoCut) > this.getIntTekiyouMaxLength()){
				chuuki[0] = kaikeiCommonLogic.getTekiyouChuuki();
				chuuki[1] = kaikeiCommonLogic.getTekiyouChuukiMeisai(shiwakeTekiyou);
			}
		}
		return chuuki;
	}
	
	/**
	 * 注記取得共通処理。最後のに引数は、プロパティの共通化が進めば不要になるはず。伝票区分は各伝票のデフォ。
	 * @param sanshou 参照
	 * @param shinseiData 申請データ
	 * @param meisai 対象となる明細
	 * @param batchKaikeiCommonLogic バッチ会計共通ロジック
	 * @return 注記1, 2
	 */
	protected String[] setChuuki(boolean sanshou, GMap shinseiData, GMap meisai, BatchKaikeiCommonLogic batchKaikeiCommonLogic, KaikeiCommonLogic kaikeiCommonLogic)
	{
		return this.setChuuki(sanshou, shinseiData, meisai, this.denpyouKbn, batchKaikeiCommonLogic, kaikeiCommonLogic);
	}
	
	/**
	 * 注記最大長をintとして取得。
	 * @return 摘要最大長さ、親クラスにあるなら毎回再取得なんてことはしない。
	 */
	protected int getIntTekiyouMaxLength()
	{
		int maxLength = 0;
		
		// キャッシュすることで、呼び出し回数を減らす。Flyweightパターンの中核部でも使われている技法。
		try
		{
			maxLength = Integer.parseInt(this.tekiyouMaxLength);
		}
		catch(Exception ex)
		{
			maxLength = kaikeiCommonLogic.tekiyouCheck(Open21Env.getVersion().toString());
			this.tekiyouMaxLength = String.valueOf(maxLength);
		}
		
		return maxLength;
	}
	
	/**
	 * @param jigyoushaKbn 事業者区分
	 * @return 空なら初期値0、空でないのなら元の値
	 */
	protected String getDefaultJigyoushaKbnIfEmpty(String jigyoushaKbn) {
		return isEmpty(jigyoushaKbn) ? "0" : jigyoushaKbn;
	}
	
	/**
	 * @param zeigakuFixFlg 税額手動修正フラグ
	 * @return 空なら初期値0、空でないのなら元の値
	 */
	protected String getDefaultZeigakuFixFlgIfEmpty(String zeigakuFixFlg) {
		return isEmpty(zeigakuFixFlg) ? "0" : zeigakuFixFlg;
	}
}