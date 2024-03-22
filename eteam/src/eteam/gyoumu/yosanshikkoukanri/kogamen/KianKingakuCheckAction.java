package eteam.gyoumu.yosanshikkoukanri.kogamen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamFileLogic;
import eteam.common.EteamSettingInfo;
import eteam.common.RegAccess;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案金額チェックダイアログAction
 */
@Getter @Setter @ToString
public class KianKingakuCheckAction extends EteamAbstractAction {
	
//＜定数＞
	/** CSVファイル名 */
	static final String CSV_FILENAME = "kianKingakuCheck.csv";

//＜画面入力＞
	/** 金額チェックコメント */
	protected String comment;

//＜画面入力以外＞
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 伝票ID */
	protected String denpyouId;
	/** 処理モード（0:通常、1:申請） */
	protected String mode; 
	/** 起案番号 */
	protected String kianBangou;
	/** 表示データ */
	protected List<GMap> dataList;
	/** 集計部門コード*/
	protected String[] syuukeiBumonCd;
	/** 集計部門名称*/
	protected String[] syuukeiBumonName;
	/** 明細部門コード*/
	protected String[] meisaiBumonCd;
	/** 明細部門名称*/
	protected String[] meisaiBumonName;
	/** 科目コード*/
	protected String[] kamokuCd;
	/** 科目*/
	protected String[] kamokuName;
	/** 科目枝番コード*/
	protected String[] kamokuEdabanCd;
	/** 科目枝番*/
	protected String[] kamokuEdabanName;
	/** 起案額*/
	protected String[] kiangaku;
	/** 支出依頼済み額*/
	protected String[] jissekigaku;
	/** 申請額*/
	protected String[] shinseigaku;
	/** 起案残高*/
	protected String[] zandaka;
	/** 判定コメント */
	protected String[] judgeComment;
	/** 判定コメント（合計） */
	protected String judgeCommentGoukei;
	/** 予算執行オプションA 使用可否 */
	protected boolean yosannShikkouOPOn = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption());
	/** 予算チェック粒度 */
	protected String checkTani = setting.yosanCheckTani();
	/** 起案チェック:超過判定％ */
	public final String choukaKijun = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.KIAN_CHOUKA_HANTEI);
	/** 起案番号採番ダイアログ選択キー（部門コード） */
	protected String sentakuBumonCd;
	/** 起案番号採番ダイアログ選択キー（年度） */
	protected String sentakuNendo;
	/** 起案番号採番ダイアログ選択キー（略号） */
	protected String sentakuRyakugou;
	/** 起案番号採番ダイアログ選択キー（開始起案番号） */
	protected String sentakuKianbangouFrom;

	//CSV出力用
	/** ダウンロードファイル用 */
	InputStream inputStream;
	/** コンテンツタイプ */
	String contentType;
	/** コンテンツディスポジション */
	String contentDisposition;
	/** EXCELファイルの長さ */
	long contentLength;

	//＜部品＞
	/** 起案金額チェックロジック */
	protected KianKingakuCheckLogic myLogic;
	/** 予算執行管理の共通ロジック */
	protected YosanShikkouKanriCategoryLogic commonLogic;
	/** 部門ユーザロジック */
	protected BumonUserKanriCategoryLogic buLogic;

	@Override
	protected void formatCheck() {
		checkString(denpyouId, 19, 19, "伝票ID", true);
		checkString(denpyouKbn, 4, 4, "伝票区分", true);
		checkString(mode, 1, 1, "モード", true);
		checkString(comment, 0, 220,"金額チェックコメント", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		// 金額チェックコメントの必須フラグを取得する。
		String commentHissuFlg = "0";
		if (null != judgeComment){
			//合計行の判定が超過している場合、入力エラーチェックを実施
			if (this.myLogic.cmntChouka.equals(judgeCommentGoukei)){
				commentHissuFlg = "1";
			}
		}

		String[][] itemList = {
				//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目)) 
				{denpyouKbn ,"伝票区分", "2", "2", "2"},
				{denpyouId ,"伝票ID", "2", "2", "2"},
				{mode ,"モード", "2", "2", "0"},
				{comment ,"金額チェックコメント", "0", commentHissuFlg, "0"}
			};
		hissuCheckCommon(itemList, eventNum);
	}
	
	
	/**
	 * 初期化処理です。
	 * @param connection コネクション
	 */
	public void initialize(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(KianKingakuCheckLogic.class, connection);
		commonLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
	}
	
	/**
	 * 初期表示処理<br>
	 * 
	 * @return 結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);

			hissuCheck(1);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}

			// データを取得する。
			this.loadData();
			if (null == dataList){
				errorList.add("部門を入力してください。");
				return "error";
			}else if (this.dataList.isEmpty()) {
				errorList.add("データが存在しません。");
				return "error";
			}

		}
		return "success";
	}
	
	/**
	 * 更新ボタン押下イベント<br>
	 * 
	 * @return 結果
	 */
	public String confirm(){
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);

			// loadData することにより入力されたコメントが書き換わってしまうため保存する。
			String saveComment = this.comment;

			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				// 画面再描画用にデータを取得する。
				this.loadData();
				this.comment = saveComment;
				return "error";
			}

			// データを取得する。
			// ※申請ボタン押下時にデータが存在しない場合でも、ダイアログ内の申請ボタンが押下出来てしまうので
			//   再度、データの有無を確認する。
			// データを取得する。
			this.loadData();
			if (null == dataList){
				errorList.add("部門を入力してください。");
				return "error";
			}else if (this.dataList.isEmpty()) {
				errorList.add("データが存在しません。");
				return "error";
			}
			// データチェックのために load した dataList をクリアする。
			this.dataList.clear();
			this.comment = saveComment;

			// 保存済みデータを削除する。
			commonLogic.deleteData(denpyouId);
			commonLogic.deleteComment(denpyouId);

			// 予算・起案金額チェックテーブルにデータを登録する。
			for(int i = 0 ; i < syuukeiBumonCd.length ; i++ ){

				if("0".equals(mode)){
					// 更新の場合は合計以外のすべてを保存する。
					commonLogic.insertData(
							denpyouId, 
							syuukeiBumonCd[i], 
							kamokuCd[i], 
							kamokuEdabanCd[i], 
							meisaiBumonCd[i], 
							syuukeiBumonName[i], 
							kamokuName[i], 
							kamokuEdabanName[i], 
							meisaiBumonName[i], 
							toDecimal(kiangaku[i]), 
							toDecimal(jissekigaku[i]), 
							toDecimal(shinseigaku[i]));
				}
			}

			// 予算・起案金額チェックコメントテーブルにデータを登録する。
			commonLogic.insertComment(denpyouId, comment);

			// コミットする。
			connection.commit();

			// 画面再描画用にデータを取得する。
			this.loadData();

			return "success";

		}
	}

	/**
	 * CSV出力前チェックイベント<br>
	 * ここではデータの存在確認だけを実施し、CSV出力は行わない。<br>
	 * CSV出力は DenpyouCommon.jsp 内に定義したコールバック関数内で呼び出される。<br>
	 * 
	 * @return 結果
	 */
	public String csvOutputCheck() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);

			hissuCheck(1);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}

			// データを取得する。
			this.loadData();
			if (null == dataList){
				errorList.add("部門を入力してください。");
				return "error";
			}else if (this.dataList.isEmpty()) {
				errorList.add("データが存在しません。");
				return "error";
			}

		}

		return "success";
	}

	/**
	 * CSV出力イベント<br>
	 * 
	 * @return 結果
	 */
	public String csvOutput() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);

			hissuCheck(3);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}

			// データを取得する。
			this.loadData();

			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// ヘッダ部のカラム名を設定する。
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("集計部門");
			if (EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani)) {
				// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
				colRecord.add("科目");
				colRecord.add("科目枝番");
			}
			colRecord.add("明細部門");
			colRecord.add("起案額");
			colRecord.add("支出依頼済み額");
			colRecord.add("申請額");
			colRecord.add("起案残高");
			if (!"0".equals(this.choukaKijun)) {
				colRecord.add("判定コメント");
			}
			csvRecords.add(colRecord);

			// 明細部のデータを設定する。
			for (GMap map : this.dataList) {
				List<Object> dataRecord = new ArrayList<Object>();

				if(super.isNotEmpty((String)map.get(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_CD))){
					// 集計部門名称
					dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_NAME));
					// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
					if(EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani)){
						// 勘定科目名称
						dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_NAME));
						// 勘定科目枝番名称
						dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_NAME));
					}
					// 明細部門名称
					dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_NAME));
					// 起案額
					dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU).toString());
					// 支出依頼済み額
					dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU).toString());
					// 申請額
					dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU).toString());
					// 起案残高
					dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA).toString());
					if (!"0".equals(this.choukaKijun)) {
						// 判定コメント
						dataRecord.add(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE_COMMENT));
					}

					csvRecords.add(dataRecord);
				}
			}

			// CSVファイルデータを作成する。
			try {
				// CSVデータ作成
				ByteArrayOutputStream objBos = new ByteArrayOutputStream();
				EteamFileLogic.outputCsv(objBos, csvRecords);
				// コンテンツタイプ設定
				contentType = ContentType.EXCEL;
				// コンテンツディスポジション設定
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME);
				// ダウンロード用ファイル作成
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();

			} catch (IOException e) {
			    throw new RuntimeException(e);
			}

		}

		return "success";
	}

	/**
	 * 画面表示データ取得<br>
	 * 画面表示データとして以下の項目を取得する。<br>
	 * <ul>
	 * <li>起案番号</li>
	 * <li>データリスト</li>
	 * <li>金額チェックコメント</li>
	 * </ul>
	 */
	protected void loadData(){

		// 起案番号の設定する。
		this.kianBangou = this.myLogic.getKianbangou(this.denpyouKbn, this.denpyouId);

		// 予算・起案金額チェックテーブルからデータを取得する。
		if ("0".equals(mode)){
			// 起案金額チェックボタンが押下された場合、まずテーブルから取得
			this.dataList = this.commonLogic.loadData(this.denpyouId);
		}

		// データが存在しない場合、データを集めて取得する。
		if (null == this.dataList || 0 == this.dataList.size()){
			this.dataList = this.myLogic.loadData(this.denpyouId, this.denpyouKbn, this.sentakuNendo);
		}

		// データリストが存在する場合
		if (null != this.dataList && 0 < this.dataList.size()){
			// 合計行を算出してデータリストに追加する。
			this.dataList.add(commonLogic.getGoukei(this.dataList));

			// 超過判定結果を設定する。
			this.commonLogic.setJudgeCumment(this.dataList, Integer.parseInt(this.choukaKijun));

			// 存在するデータリストの金額部分に表示書式を設定する。
			// また、超過判定結果により判定コメントを設定する。
			for (GMap map : this.dataList) {
				map.put(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU, formatMoney(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU)));
				map.put(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU, formatMoney(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU)));
				map.put(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU, formatMoney(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU)));
				map.put(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA, formatMoney(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA)));
				if ("0".equals(map.get(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE))){
					map.put(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE_COMMENT, this.myLogic.cmntYosanNai);
				}else{
					map.put(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE_COMMENT, this.myLogic.cmntChouka);
				}
			}
		}

		// 金額チェックコメントを設定する。
		this.comment = this.commonLogic.findComment(this.denpyouId);
	}
}
