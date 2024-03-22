package eteam.gyoumu.yosanshikkoukanri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.EteamFileLogic;
import eteam.common.RegAccess;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 執行状況一覧（みなし実績）Actionｓ
 */
@Getter @Setter @ToString
public class ShikkouJoukyouIchiranAction extends EteamAbstractAction {

//＜定数＞
	/** CSVファイル名 */
	static final String CSV_FILENAME = "shikkoujoukyouIchiran.csv";

//＜画面入力＞
	/** 対象月 */
	protected String targetDate;
	/** 集計部門コード */
	protected String syuukeiBumonCd;
	/** 集計部門名 */
	protected String syuukeiBumonName;
	/** 部門コードfrom */
	protected String fromBumonCd;
	/** 部門名from */
	protected String fromBumonName;
	/** 部門コードto */
	protected String toBumonCd;
	/** 部門名to */
	protected String toBumonName;
	/** 科目コードfrom */
	protected String fromKamokuCd;
	/** 科目名from */
	protected String fromKamokuName;
	/** 科目コードto */
	protected String toKamokuCd;
	/** 科目名to */
	protected String toKamokuName;
	/** 予算単位 */
	protected String yosanTani;

//＜画面入力以外＞
	/** 表示データ */
	protected List<GMap> ichiranList;
	/** 選択月リスト */
	protected List<Map<String, String>> monthList;
	/** 予算執行オプションA 使用可否 */
	boolean yosannShikkouOPOn = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption());
	/** 予算チェック粒度 */
	protected String checkTani = setting.yosanCheckTani();
	/** 予算単位ドメイン */
	protected static String[] yosanTaniDomain = {yosanCheckKikan.YEAR, yosanCheckKikan.TO_TAISHOUZUKI};
	
	//ページング用
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** ページ番号 */
	String pageNo = "1";
	/** ページング処理 link押下時のURL */
	String pagingLink;

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
	/**執行状況一覧ロジック */
	protected ShikkouJoukyouIchiranLogic myLogic;
	/** 部門ユーザロジック */
	protected BumonUserKanriCategoryLogic buLogic;
	
	@Override
	protected void formatCheck() {
		checkDate(targetDate, "対象月", false);
		checkString(syuukeiBumonCd, 1, 8, "集計部門コード", false);
		checkString(fromBumonCd, 1, 8, "部門コードFrom", false);
		checkString(toBumonCd, 1, 8, "部門コードTo", false);
		checkDomain(yosanTani, yosanTaniDomain, "予算単位", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目				項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{targetDate, "対象月", "0", "2"},
				{syuukeiBumonCd, "集計部門", "0", "1"},
				{yosanTani, "予算単位", "0", "1"},
		};
		hissuCheckCommon(list, eventNum);
	}
	/**
	 * 予算執行管理オプションが未設定の場合エラー
	 * セキュリティパターンがないならエラー
	 * ※メニューで非表示にするけどURLで来るかもしれないので
	 */
	protected void accessCheck() {
		if(!yosannShikkouOPOn){
			throw new EteamAccessDeniedException("予算執行管理オプションが未設定のため執行状況一覧は利用できません。");
		}
	}

	/**
	 * 初期化処理です。
	 * @param connection コネクション
	 */
	public void initialize(EteamConnection connection) {
		monthList = EteamCommon.createMonthList();
		myLogic = EteamContainer.getComponent(ShikkouJoukyouIchiranLogic.class ,connection);
		buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class ,connection);
		
	}

	/**
	 * 表示処理
	 * @return 結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			accessCheck();
			
			totalCount = 0;
			pageNo = "0";
			pagingLink = "";
			User user = super.getUser();
			String userId = user.getSeigyoUserId();
			GMap map = myLogic.selectShikkouJoukyouIchiranJouhou(userId);
			yosanTani = (null != map) ? map.getString("yosan_tani") : yosanCheckKikan.YEAR;
		}
		return "success";
	}

	/**
	 * 検索処理
	 * @return 結果
	 */
	public String search() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			accessCheck();
			
			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}
			
			// 予算単位を保存
			updateShikkouJoukyouIchiranJouhou();
			// 検索結果に係わらず予算単位は保存したいので一旦コミット
			connection.commit();
			
			//総件数取得
			totalCount = countMeisai();
			
			//1ページ最大表示件数をテーブルから取得
			int pageMax = Integer.parseInt(setting.recordNumPerPage());

			// 総ページ数の計算
			totalPage = EteamCommon.calcTotalPageNum(pageMax, totalCount);
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
			pagingLink = EteamCommon.getParmeterString(
					this,
					"shikkou_joukyou_search",
					new HashSet<String>(Arrays.asList(new String[] {
							"targetDate",
							"syuukeiBumonCd",
							"syuukeiBumonName",
							"fromBumonCd",
							"fromBumonName",
							"toBumonCd",
							"toBumonName",
							"fromKamokuCd",
							"fromKamokuName",
							"toKamokuCd",
							"toKamokuName",
							"yosanTani",
					}))
				).toString();
			
			// データを取得
			ichiranList = loadMeisai(pageMax);
			if (ichiranList.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//表示書式セット
			for (GMap map : ichiranList) {
				map.put("yosan",              formatMoney(map.get("yosan")));
				map.put("ruikei_shikkoudaka", formatMoney(map.get("ruikei_shikkoudaka")));
				map.put("minasi_shikkoudaka", formatMoney(map.get("minasi_shikkoudaka")));
				map.put("shikkoudaka_goukei", formatMoney(map.get("shikkoudaka_goukei")));
				map.put("yosanzan",           formatMoney(map.get("yosanzan")));
				map.put("rate",               map.get("rate").toString().concat("％"));
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
			accessCheck();
			
			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}

			// 予算単位を保存
			updateShikkouJoukyouIchiranJouhou();
			// 検索結果に係わらず予算単位は保存したいので一旦コミット
			connection.commit();
			
			// データ取得
			ichiranList = loadMeisai(0);
			if (ichiranList.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			boolean isKamokuTani = EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani);
			boolean isRuikei = EteamConst.yosanCheckKikan.TO_TAISHOUZUKI.equals(yosanTani);

			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// ヘッダー部(集計部門と対象月)を出力
			List<Object> headerRecord = new ArrayList<Object>();
			headerRecord.add(syuukeiBumonName);
			headerRecord.add(targetDate.substring(0, 4) + "年" + targetDate.substring(5, 7) + "月度");
			csvRecords.add(headerRecord);

			// データ部を出力
			
			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("集計部門コード");
			colRecord.add("集計部門名称");
			colRecord.add("明細部門コード");
			colRecord.add("明細部門名称");
			if(isKamokuTani) {
				colRecord.add("科目コード");
				colRecord.add("科目名称");
			}
			if(isRuikei) {
				colRecord.add("累計予算");
			}else {
				colRecord.add("当年度予算");
			}
			colRecord.add("累計執行高（会計）");
			colRecord.add("WF内みなし執行高");
			colRecord.add("執行高合計");
			colRecord.add("差引予算残");
			colRecord.add("執行率");
			csvRecords.add(colRecord);

			for (GMap map : ichiranList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("syuukei_bumon_cd"));
				dataRecord.add(map.get("syuukei_bumon_name"));
				dataRecord.add(map.get("meisai_bumon_cd"));
				dataRecord.add(map.get("meisai_bumon_name"));
				if(isKamokuTani) {
					dataRecord.add(map.get("kamoku_gaibu_cd"));
					dataRecord.add(map.get("kamoku_name_ryakushiki"));
				}
				dataRecord.add(formatMoney(map.get("yosan")));
				dataRecord.add(formatMoney(map.get("ruikei_shikkoudaka")));
				dataRecord.add(formatMoney(map.get("minasi_shikkoudaka")));
				dataRecord.add(formatMoney(map.get("shikkoudaka_goukei")));
				dataRecord.add(formatMoney(map.get("yosanzan")));
				dataRecord.add(map.get("rate").toString().concat("％"));
				csvRecords.add(dataRecord);
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
		}
		return "success";
	}

	/**
	 * 執行状況一覧情報テーブルの更新
	 */
	protected void updateShikkouJoukyouIchiranJouhou() {
		User user = super.getUser();
		String userId = user.getSeigyoUserId();
		if(myLogic.existShikkouJoukyouIchiranJouhou(userId)) {
			myLogic.updateShikkouJoukyouIchiranJouhou(userId, yosanTani);
		}else {
			myLogic.insertShikkouJoukyouIchiranJouhou(userId, yosanTani);
		}
	}
	
	/**
	 * 執行状況データ件数
	 * @return 件数
	 */
	protected long countMeisai() {
		int sptn = Integer.parseInt(setting.yosanSecurityPattern());
		return myLogic.countShikkouJoukyou(
				EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani),
				EteamConst.yosanCheckKikan.TO_TAISHOUZUKI.equals(yosanTani),
				sptn,
				syuukeiBumonCd,
				fromBumonCd,
				toBumonCd,
				fromKamokuCd,
				toKamokuCd,
				yosanTani,
				toDate(targetDate));
	}

	/**
	 * 執行状況データ読み込み
	 * @param pageMax 1ページ最大表示件数
	 * @return 執行状況データ
	 */
	protected List<GMap> loadMeisai(int pageMax) {
		int sptn = Integer.parseInt(setting.yosanSecurityPattern());
		return myLogic.loadShikkouJoukyou(
				EteamConst.yosanCheckTani.BUMON_KAMOKU.equals(checkTani),
				EteamConst.yosanCheckKikan.TO_TAISHOUZUKI.equals(yosanTani),
				sptn,
				syuukeiBumonCd,
				fromBumonCd,
				toBumonCd,
				fromKamokuCd,
				toKamokuCd,
				yosanTani,
				toDate(targetDate),
				Integer.parseInt(pageNo),
				pageMax);
	}
}