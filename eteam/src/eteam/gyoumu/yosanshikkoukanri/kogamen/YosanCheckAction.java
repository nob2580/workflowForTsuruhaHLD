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
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.EteamFileLogic;
import eteam.common.RegAccess;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 予算チェックダイアログAction
 *
 */
@Getter @Setter @ToString
public class YosanCheckAction extends EteamAbstractAction {

//＜定数＞
	/** CSVファイル名 */
	static final String CSV_FILENAME = "yosanCheck.csv";

//＜画面入力＞
	/** 金額チェックコメント */
	protected String comment;

//＜画面入力以外＞
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 伝票ID */
	protected String denpyouId;
	/** 処理モード */
	protected String mode; // 0:通常、1:申請
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
	/** 予算額*/
	protected String[] kiangaku;
	/** 支出依頼済み額*/
	protected String[] jissekigaku;
	/** 申請額*/
	protected String[] shinseigaku;
	/** 予算残高*/
	protected String[] zandaka;
	/** 判定コメント */
	protected String[] judgeComment;
	/** 判定コメント（合計） */
	protected String judgeCommentGoukei;
	/** 予算執行オプションA 使用可否 */
	protected boolean yosannShikkouOPOn = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption());
	/** 予算チェック粒度 */
	protected String checkTani = setting.yosanCheckTani();
	/** 予算チェック:超過判定％ */
	public final String choukaKijun = setting.yosanChoukaHantei();
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
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** EXCELファイルの長さ */
	long contentLength;

	//＜部品＞
	/**起案金額チェックロジック */
	protected YosanCheckLogic myLogic;
	/** 部門ユーザロジック */
	protected BumonUserKanriCategoryLogic buLogic;
	/** 予算執行管理カテゴリーロジック */
	protected YosanShikkouKanriCategoryLogic commonLogic;
	/** 通知カテゴリーロジック */
	protected TsuuchiCategoryLogic tsuuchiLogic;

	@Override
	protected void formatCheck() {
		checkString(denpyouId, 19, 19, "伝票ID", true);
		checkString(denpyouKbn, 4, 4, "伝票区分", true);
		checkString(mode, 1, 1, "モード", true);
		checkString(comment, 0, 220, "予算チェックコメント", false);
		//以下は画面入力項目ではないのでエラーにはならないはず。
		if(null != syuukeiBumonCd){
			for(int i = 0 ; i < syuukeiBumonCd.length ; i++){
				checkString(syuukeiBumonCd[i], 0, 8, "集計部門コード:" + (i+1) + "行目", false);
				checkString(syuukeiBumonName[i], 0, 20, "集計部門名称:" + (i+1) + "行目", false);
				checkString(meisaiBumonCd[i], 0, 8, "明細部門コード:" + (i+1) + "行目", false);
				checkString(meisaiBumonName[i], 0, 20, "明細部門名称:" + (i+1) + "行目", false);
				checkString(kamokuCd[i], 0, 8, "科目コード:" + (i+1) + "行目", false);
				checkString(kamokuName[i], 0, 22, "科目:" + (i+1) + "行目", false);
				checkString(kamokuEdabanCd[i], 0, 12, "科目枝番コード:" + (i+1) + "行目", false);
				checkString(kamokuEdabanName[i], 0, 20, "科目枝番:" + (i+1) + "行目", false);
				checkKingakuOver0(kiangaku[i], "予算額:" + (i+1) + "行目", false);
				//貸方発生の金額が大きく累計実績がマイナス金額になる場合を考慮
				checkKingakuMinus(jissekigaku[i], "支出依頼済み額:" + (i+1) + "行目", false);
				checkKingakuOver0(shinseigaku[i], "申請額:" + (i+1) + "行目", false);
				checkKingakuMinus(zandaka[i], "残高:" + (i+1) + "行目", false);
				checkString(judgeComment[i], 0, 15, "判定コメント:" + (i+1) + "行目", false);
			}
		}
	}

	@Override
	protected void hissuCheck(int eventNum) {
		// 金額チェックコメントの必須フラグを取得する。
		String commentHissuFlg = "0";
		if (null != judgeCommentGoukei){
			//合計行の判定が超過している場合、入力エラーチェックを実施
			if (this.myLogic.cmntChouka.equals(judgeCommentGoukei)){
				commentHissuFlg = "1";
			}
		}
		String[][] itemList = {
				//項目											EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目)) 
				{denpyouKbn ,"伝票区分", "2", "2"},
				{denpyouId ,"伝票ID", "2", "2"},
				{mode ,"モード", "2", "2"},
				{comment ,"予算チェックコメント", "0", commentHissuFlg}
			};
		hissuCheckCommon(itemList, eventNum);
				
	}
	
	
	/**
	 * 初期化処理です。
	 * @param connection コネクション
	 */
	public void initialize(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(YosanCheckLogic.class, connection);
		buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		commonLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
	}
	
	/**
	 * 表示処理
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
			
			// データを取得
			this.loadData();
			if (errorList.size() > 0) {
				return "error";
			}else
			if (null == dataList){
				errorList.add("部門を入力してください。");
				return "error";
			}else if (dataList.isEmpty()) {
				errorList.add("データが存在しません。");
				return "error";
			}
		}
		return "success";
	}
	
	/**
	 * 更新イベント
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
				this.loadData(); //表示用データの再取得
				this.comment = saveComment;
				return "error";
			}
	
			// データを取得する。
			// ※申請ボタン押下時にデータが存在しない場合でも、ダイアログ内の申請ボタンが押下出来てしまうので
			//   再度、データの有無を確認する。
			this.loadData();
			if (errorList.size() > 0) {
				return "error";
			}else
			if (null == dataList){
				errorList.add("部門を入力してください。");
				return "error";
			}else if (dataList.isEmpty()) {
				errorList.add("データが存在しません。");
				return "error";
			}
			// データチェックのために load した dataList をクリアする。
			this.dataList.clear();
			this.comment = saveComment;

			//保存済みデータを削除
			commonLogic.deleteData(denpyouId);
			commonLogic.deleteComment(denpyouId);

			//データの登録
			for(int i = 0 ; i < syuukeiBumonCd.length ; i++ ){
				//更新の場合→合計以外すべて保存
				if("0".equals(mode)){
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
				
				//申請ボタン押下時の保存処理はWorkflowEventControl.shinsei()へ移動
				}
			}
			commonLogic.insertComment(denpyouId, comment);
			
			connection.commit();
			
			//表示用データの再取得
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
			if (errorList.size() > 0) {
				return "error";
			}else
			if (null == dataList){
				errorList.add("部門を入力してください。");
				return "error";
			}else if (dataList.isEmpty()) {
				errorList.add("データが存在しません。");
				return "error";
			}
		}
		
		return "success";
	}
	
	
	/**
	 * CSV出力イベント
	 * @return 結果
	 */
	public String csvOutput() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			
			hissuCheck(1);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}
			
			//データの取得
			this.loadData();
			
			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("集計部門");
			if(EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani)){
				colRecord.add("科目");
				colRecord.add("科目枝番");
			}
			colRecord.add("明細部門");
			colRecord.add("予算");
			colRecord.add("累計実績額");
			colRecord.add("申請額");
			colRecord.add("予算残高");
			if (!"0".equals(this.choukaKijun)) {
				colRecord.add("判定コメント");
			}
			csvRecords.add(colRecord);
			
			// 明細部のデータを設定する。
			for (GMap map : this.dataList) {
				List<Object> dataRecord = new ArrayList<Object>();

				if(super.isNotEmpty((String)map.get("meisaiBumonCd"))){
					// 集計部門名称
					dataRecord.add(map.get("syuukeiBumonName"));
					// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
					if(EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani)){
						// 勘定科目名称
						dataRecord.add(map.get("kamokuName"));
						// 勘定科目枝番名称
						dataRecord.add(map.get("kamokuEdabanName"));
					}
					// 明細部門名称
					dataRecord.add(map.get("meisaiBumonName"));
					// 予算
					dataRecord.add(map.get("kiangaku").toString());
					// 累計実績額
					dataRecord.add(map.get("jissekigaku").toString());
					// 申請額
					dataRecord.add(map.get("shinseigaku").toString());
					// 予算残高
					dataRecord.add(map.get("zandaka").toString());
					if (!"0".equals(this.choukaKijun)) {
						// 判定コメント
						dataRecord.add(map.get("judgeComment"));
					}
					
					csvRecords.add(dataRecord);
				}
			}
			
			// CSVファイルデータを作る
			try {
				//CSVデータ作成
				ByteArrayOutputStream objBos = new ByteArrayOutputStream();
				EteamFileLogic.outputCsv(objBos, csvRecords);

				//コンテンツタイプ設定
				contentType = ContentType.EXCEL;
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME);
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();
			} catch (IOException e) {
			    throw new RuntimeException(e);
			}
			
			return "success";
		}
	}

	/**
	 * 画面表示データ取得<br>
	 * 画面表示データとして以下の項目を取得する。<br>
	 * <ul>
	 * <li>データリスト</li>
	 * <li>予算チェックコメント</li>
	 * </ul>
	 */
	protected void loadData(){
		
		// 予算・起案金額チェックテーブルからデータを取得する。
		if("0".equals(mode)) dataList = commonLogic.loadData(denpyouId);
		
		// データが存在しない場合、データを集めて取得する。
		if(null==dataList || dataList.isEmpty()){
			if(yosanCheckKikan.TO_TAISHOUZUKI.equals(setting.yosanCheckKikan())) {
				if(isEmpty(tsuuchiLogic.findDenpyou(denpyouId).getString("yosan_check_nengetsu"))) {
					errorList.add("予算執行対象月を入力してから予算確認してください。");
					return;
				}
			}
			dataList = myLogic.loadData(denpyouId, denpyouKbn, sentakuNendo);
		}
		
		if(null != dataList && 0 < this.dataList.size()){
		
			//合計を計算してdataListにputする
			dataList.add(commonLogic.getGoukei(dataList));
			
			//判定のセット
			commonLogic.setJudgeCumment(dataList, Integer.parseInt(setting.yosanChoukaHantei()));
			
			//表示書式セット
			for (GMap map : dataList) {
				map.put("kiangaku", formatMoney(map.get("kiangaku")));
				map.put("jissekigaku", formatMoney(map.get("jissekigaku")));
				map.put("shinseigaku", formatMoney(map.get("shinseigaku")));
				map.put("zandaka", formatMoney(map.get("zandaka")));
				map.put("judgeComment", "0".equals(map.get("judge"))? 
												setting.yosanCommentYosannai()
												: setting.yosanCommentChouka());
			}
		}
		
		//コメントの取得
		comment = commonLogic.findComment(denpyouId);
	}

}
