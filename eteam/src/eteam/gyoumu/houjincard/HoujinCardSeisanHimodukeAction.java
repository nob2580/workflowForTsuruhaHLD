package eteam.gyoumu.houjincard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.UserId;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 法人カード精算紐付一覧画面Action
 */
@Getter @Setter @ToString
public class HoujinCardSeisanHimodukeAction extends EteamAbstractAction {

//＜定数＞
	/** 未精算 */
	//protected static final String JOUTAI_MISEISAN = "0";
	/** 精算済 */
	//protected static final String JOUTAI_SEISANZUMI = "1";
	/** 全て */
	protected static final String JOUTAI_SUBETE = "2";
	
	
//＜画面入力＞
	/** ステータス */
	String statusSelect;
	/** ソート区分 */
	String sortKbn = "2";
	/** ページ番号 */
	String pageNo = "1";
	
	/** カード種別 */
	String searchCardShubetsu;
	
	/** 社員No */
	String shainNo;
	/** ユーザーID */
	String userId;
	/** 部署コード */
	String bushoCd;
	/** 日付From */
	String dateFrom;
	/** 日付To */
	String dateTo;
	
	/** イベントURL(遷移元画面) */
	String preEventUrl;
	
	/** 除外フラグ変更対象リスト */ //シーケンス番号のカンマ区切り
	String jyogaiChangeList;
	/** 除外フラグ変更先 */ //1:除外、0:復活
	String jyogaiChangeFlg;
	/** 除外理由 */
	String jyogaiRiyuu;
	
//＜画面入力以外＞
	/** データ件数 */
	long totalCount;
	/** 全ページ数 */
	long totalPage;
	/** ページング処理 link押下時のURL */
	String pagingLink;
	/** カード使用履歴一覧リスト */
	List<GMap> rirekiList;
	/** カード種別のDropDownList */
	List<GMap> cardList;
	/** admin・経理ユーザーフラグ */
	boolean keiriFlg = false;
	
	/** 除外・復活ボタン表示フラグ */
	boolean jyogaiBtnFlg = false;
	
	
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
			
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			HoujinCardLogic hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
			KaikeiCommonLogic commonLc = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			
			// naibu_cd_settingテーブルからデータを取得（カード種別プルダウンリストの値）
			cardList = common.loadNaibuCdSetting("houjin_card_shubetsu");
			// ブランク追加
			GMap mp = new GMap();
			mp.put("naibu_cd", "");
			mp.put("name", "");
			cardList.add(0,mp);
			
			if(UserId.ADMIN.equals(getUser().getSeigyoUserId()) || commonLc.userIsKeiri(getUser()) ) {
				keiriFlg = true;
				//除外機能有効かつ除外者経理処理ならボタン表示
				if(setting.houjinCardRirekiJyogaiEnable().equals("1")
					&& setting.houjinCardRirekiJyogaiUser().equals("2")){
						jyogaiBtnFlg = true;
				}
			}else{
				userId = getUser().getSeigyoUserId();
				shainNo = getUser().getShainNo();
				//除外機能有効かつ除外者申請者ならボタン表示
				if(setting.houjinCardRirekiJyogaiEnable().equals("1")
					&& setting.houjinCardRirekiJyogaiUser().equals("1")){
						jyogaiBtnFlg = true;
				}
			}

			// メニューからの表示であれば（プルダウン変更の再表示でなければ）デフォルトの「全て」を選択
			if (isEmpty(statusSelect)) {
				statusSelect = JOUTAI_SUBETE;
			}

			// カード使用履歴のデータ件数を取得
			if(!keiriFlg && !userId.equals(getUser().getSeigyoUserId())){
				throw new EteamAccessDeniedException("一般ユーザーで該当ユーザー以外のデータは参照不可");
			}
			totalCount = hcLogic.findCardRirekiCount(searchCardShubetsu, userId, bushoCd, toDate(dateFrom), toDate(dateTo), statusSelect, false, keiriFlg);
			
			// 設定情報の取得（１ページ表示件数）
			int pagemax = Integer.parseInt(setting.recordNumPerPage());
			
			// 総ページ数の計算
			totalPage = EteamCommon.calcTotalPageNum(pagemax, totalCount);
			if (totalPage == 0) {
				totalPage = 1;
			}
			
			// 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
			if(isEmpty(pageNo) || "0".equals(pageNo)){
				pageNo = "1";
			} else if(Integer.parseInt(pageNo) > totalPage) {
				pageNo = String.valueOf(totalPage);
			}
			
			// ページングリンクURLを設定
			pagingLink = "houjin_card_seisan_himoduke"
						+ "?sortKbn=" + sortKbn
						+ "&searchCardShubetsu=" + URLEncoder.encode((searchCardShubetsu != null ? searchCardShubetsu : "") ,"UTF-8")
						+ "&shainNo=" + URLEncoder.encode((shainNo != null ? shainNo : "") ,"UTF-8")
						+ "&userId=" + URLEncoder.encode((userId != null ? userId : "") ,"UTF-8")
						+ "&bushoCd=" + URLEncoder.encode((bushoCd != null ? bushoCd : "") ,"UTF-8")
						+ "&dateFrom=" + URLEncoder.encode((dateFrom != null ? dateFrom : "") ,"UTF-8")
						+ "&dateTo=" + URLEncoder.encode((dateTo != null ? dateTo : "") ,"UTF-8")
						+ "&statusSelect=" + URLEncoder.encode((statusSelect != null ? statusSelect : "") ,"UTF-8") + "&";

			preEventUrl = "houjin_card_seisan_himoduke"
					+ "?searchCardShubetsu=" + URLEncoder.encode((searchCardShubetsu != null ? searchCardShubetsu : "") ,"UTF-8")
					+ "&shainNo=" + URLEncoder.encode((shainNo != null ? shainNo : "") ,"UTF-8")
					+ "&userId=" + URLEncoder.encode((userId != null ? userId : "") ,"UTF-8")
					+ "&bushoCd=" + URLEncoder.encode((bushoCd != null ? bushoCd : "") ,"UTF-8")
					+ "&dateFrom=" + URLEncoder.encode((dateFrom != null ? dateFrom : "") ,"UTF-8")
					+ "&dateTo=" + URLEncoder.encode((dateTo != null ? dateTo : "") ,"UTF-8")
					+ "&statusSelect=" + URLEncoder.encode((statusSelect != null ? statusSelect : "") ,"UTF-8") + "&";

			
			// 法人カード精算紐付一覧の取得
			rirekiList = hcLogic.loadCardRireki(searchCardShubetsu, userId, bushoCd, toDate(dateFrom), toDate(dateTo), statusSelect, false, sortKbn, keiriFlg, Integer.parseInt(pageNo), pagemax);
			
			// 法人カード精算紐付一覧情報が１件も存在しない場合は、エラーメッセージを表示する。
			if (rirekiList.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			for (GMap map : rirekiList) {
				//カード会社名称設定
				for (GMap cdmp : cardList){
					if (map.get("card_shubetsu").equals(cdmp.get("naibu_cd"))){
						map.put("cardName",cdmp.get("name").toString());
						break;
					}
				}
				//利用日変換
				map.put("riyoubi", formatDate(map.get("riyoubi")));
				map.put("kingaku", formatMoney(map.get("kingaku")));
			}
			
			// 5.戻り値を返す
			return "success";
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * ICカード履歴の除外・再表示処理
	 * @return 処理結果
	 */
	public String jyogaiChange(){
		//1.入力チェック なし
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			HoujinCardLogic hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
			KaikeiCommonLogic commonLc = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック
			if(!(setting.houjinCardRirekiJyogaiEnable().equals("1"))){
				throw new EteamAccessDeniedException("法人カード履歴除外機能無効");
			}else {
				if( (UserId.ADMIN.equals(getUser().getSeigyoUserId()) || commonLc.userIsKeiri(getUser()) ) ){
					if (!(setting.houjinCardRirekiJyogaiUser().equals("2"))) {
						throw new EteamAccessDeniedException("申請者以外は除外操作不可");
					}
				}else{
					if (!(setting.houjinCardRirekiJyogaiUser().equals("1"))){
						throw new EteamAccessDeniedException("経理ロール以外は除外操作不可");
					}
				}
			}
			
			//4.処理（初期表示）
			String[] jcList = jyogaiChangeList.split(",");
			
			for(String str : jcList) {
				hcLogic.setJyogaiFlg(str, jyogaiChangeFlg, jyogaiRiyuu);
			}
			connection.commit();
			return init();
		}
	}

	
}
