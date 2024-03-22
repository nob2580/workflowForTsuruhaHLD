package eteam.gyoumu.tsuuchi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.GMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 法人カード精算紐付画面Action
 */
@Getter @Setter @ToString
public class HoujinCardSeisanHimodukeAction extends EteamAbstractAction {

//＜定数＞
	/** 通知ステータスコードID（コード一覧より）*/
	//protected  static final String STATUS_CD = "kidoku_flg";
	
//＜画面入力＞
	/** シリアルNO配列 */
	String serialNo;
	/** イベントのモード(既読 : 1 未読 : 0) */
	String mode;
	/** ステータス */
	String statusSelect;
	/** ソート区分 */
	String sortKbn = "5";
	/** ページ番号 */
	String pageNo = "1";
	
//＜画面入力以外＞
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** ページング処理 link押下時のURL */
	String pagingLink;
	/** 通知一覧リスト */
	List<GMap> tsuuchiList;
	/** ステータスのDropDownList */
	List<GMap> statusList;
	
//＜入力チェック＞
	@Override protected void formatCheck() {
		checkString(statusSelect, 0, 1, "statusSelect", false);
		checkString(sortKbn, 0, 1, "sortKbn", true);
		checkNumber(pageNo, 0, 10, "pageNo", true);
	}
	@Override protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目								 			EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{statusSelect ,"statusSelect"			,"0", "0", },
			{sortKbn ,"sortKbn"				,"0", "0", },
			{String.valueOf(pageNo) ,"pageNo"				,"0", "0", },
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {

		//1.入力チェック
		formatCheck();
		hissuCheck(1);
		
		//2.データ存在チェック なし
		//3.アクセス可能チェック なし
		
		try(EteamConnection connection = EteamConnection.connect()) {
			
			
			
			
			//TODO 通常ユーザーなら該当ユーザー分のデータのみ表示
			//TODO Admin、経理ロールなら全データ表示で起票はさせない
			
			
			
// // naibu_cd_settingテーブルからデータを取得（状態プルダウンリストの値）
// statusList = common.loadNaibuCdSetting(STATUS_CD);
//
// // メニューからの表示であれば（プルダウン変更の再表示でなければ）デフォルトの「未読」を選択
// if (isEmpty(statusSelect)) {
// statusSelect = KIDOKU_FLG.MIDOKU;
// }
//
// // 通知テーブルのデータ件数を取得
// totalCount = tcl.findTsuuchiCount(getUser().getSeigyoUserId(), statusSelect);
// 
// // 設定情報の取得（１ページ表示件数）
// int pagemax = Integer.parseInt(setting.recordNumPerPage());
// 
// // 総ページ数の計算
// totalPage = EteamCommon.calcTotalPageNum(pagemax, totalCount);
// if (totalPage == 0) {
// totalPage = 1;
// }
// 
// // 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
// if(isEmpty(pageNo) || "0".equals(pageNo)){
// pageNo = "1";
// } else if(Integer.parseInt(pageNo) > totalPage) {
// pageNo = String.valueOf(totalPage);
// }
// 
// // ページングリンクURLを設定
// pagingLink = "tsuuchi_ichiran?sortKbn=" + sortKbn + "&statusSelect=" + statusSelect + "&";
// 
// // 通知一覧の取得
// tsuuchiList = tcl.loadTsuuchiIchiran(getUser().getSeigyoUserId(), statusSelect, sortKbn, Integer.parseInt(pageNo), pagemax);
// 
// // 通知一覧情報が１件も存在しない場合は、エラーメッセージを表示する。
// if (tsuuchiList.isEmpty()) {
// errorList.add("検索結果が0件でした。");
// return "error";
// }
// 
// for (GMap map : tsuuchiList) {
// // 既読('1')時は背景色を変更（グレーに設定）
// if (KIDOKU_FLG.KIDOKU.equals(String.valueOf(map.get("kidoku_flg")))) {
// map.put("bg_color", "disabled-bgcolor");
// }
// // yyyy/MM/dd HH:mmに変換
// map.put("touroku_time", formatTime(map.get("touroku_time")));
// }
			
			// 5.戻り値を返す
			return "success";
		}
	}
	
	//TODO 条件による検索処理も追加
	
	/**
	 * 起票イベント(起票ボタン)
	 * @return 処理結果
	 */
	public String kihyou() {

		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		
		//2.データ存在チェック なし
		//3.アクセス可能チェック なし
		
		try(EteamConnection connection = EteamConnection.connect()) {
			
			//4.処理
			
			//TODO URLを作成して各申請画面呼び出し
			//  GETのパラメータ形式でtenkiSaki,tenkiRiyoubi,tenkiKingakuを渡してそれを各伝票側Action側で受け取らせるようにすればよい？
			//  displayKobetsuDenpyouの新規起票処理にて上記変数があったら明細追加
			//  明細のIDも同時に渡すようにする
			
// TsuuchiIchiranLogic til = EteamContainer.getComponent(TsuuchiIchiranLogic.class, connection);
// 
// String[] sNo = serialNo.split(",");
// 
// // 更新処理 (modeは、既読 : 1 未読 : 0)
// til.koushin(sNo, mode, getUser().getSeigyoUserId());
// 
// connection.commit();
			
			// 5.戻り値を返す
			return "success";
		}
	}

}
