package eteam.gyoumu.kaikei;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.intercepter.EteamActionIntercepter;
import eteam.common.EteamCommon;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 取引(仕訳)一覧画面Action
 */
@Getter @Setter @ToString
public class TorihikiIchiranAction extends EteamAbstractAction {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamActionIntercepter.class);


//＜定数＞
	/** 伝票種別ID（コード一覧より）*/
	static final String INFO_DENPYOU_SHUBETSU_CD_ID = "shiwake_pattern_denpyou_kbn";
	
	/** ソートアイテム */
	static final String[] SOURT_ITEM_DOMAIN = {
		"hyouji_jun",
		"yuukou_kigen_from",
		"bunrui1",
		"bunrui2",
		"bunrui3",
		"torihiki_name",
		"kari_kamoku_cd",
		"kashi_kamoku_cd1"
		};
	
	/** ソート順 */
	static final String[] SOURT_ORDER_DOMAIN = {"ASC", "DESC"};

//＜画面入力＞
	/** 伝票種別 */
	String searchDenpyouShubetsu;
	/** 伝票区分 */
	String denpyouKbn = null;
	/** 仕訳枝番 */
	String shiwakeEdano = "";
	/** ソートアイテム */
	String sortItem = "hyouji_jun";
	/** ソート順 */
	String sortOrder = "ASC";
	/** 一覧ヘッダ初期表示の制御フラグ */
	String initHyoujiFlag;
	
	/** 分類1 */
	String bunrui1Val = "";
	/** 分類2 */
	String bunrui2Val = "";
	/** 分類3 */
	String bunrui3Val = "";
	/** キーワード */
	String keywordVal = "";

//＜画面入力以外＞
	/** 伝票種別のDropDownList */
	List<GMap> infoDenpyouShubetsuList;
	/** 取引一覧 */
	List<GMap> torihikiList;
	/** イベントURL(遷移元画面) */
	String preEventUrl;

//＜入力チェック＞
	@Override 
	protected void formatCheck() {
		checkNumber(shiwakeEdano, 1, 10,            "仕訳枝番",       false);
		checkDomain(sortItem,  SOURT_ITEM_DOMAIN,   "ソートアイテム", false);
		checkDomain(sortOrder, SOURT_ORDER_DOMAIN,  "ソート順",       false);
	}
	
	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouKbn ,"伝票区分"			,"0", "1", "1",},
			{shiwakeEdano ,"仕訳枝番"			,"0", "0", "2",},
			{sortItem ,"ソートアイテム"	,"0", "2", "2",},
			{sortOrder ,"ソート順"			,"0", "2", "2",},
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			SystemKanriCategoryLogic systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			/* 伝票種別DropDownリスト作成 */
			// 内部コード設定からキー・名称を取得(内部コード名 = "shiwake_pattern_denpyou_kbn")
			infoDenpyouShubetsuList = systemLogic.loadNaibuCdSetting(INFO_DENPYOU_SHUBETSU_CD_ID);
			
			// 初期表示の時、一覧のヘッダを非表示する
			initHyoujiFlag = "0";

			// 戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 検索（ソートリンク）イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			SystemKanriCategoryLogic systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			TorihikiLogic myLogic = EteamContainer.getComponent(TorihikiLogic.class, connection);

			// 入力チェック
			hissuCheck(2);
			formatCheck();

			// 伝票区分の存在チェック
			checkCode(connection, denpyouKbn, INFO_DENPYOU_SHUBETSU_CD_ID, "伝票区分", false);
			
			/* 伝票区分DropDownリスト作成 */
			// 内部コード設定からキー・名称を取得(内部コード名 = "shiwake_pattern_denpyou_kbn")
			infoDenpyouShubetsuList = systemLogic.loadNaibuCdSetting(INFO_DENPYOU_SHUBETSU_CD_ID);

			// エラーがある場合
			if(0 < errorList.size()) {
				return "error";
			}

			// 検索条件を初期化する
			// 伝票種別
			searchDenpyouShubetsu = denpyouKbn;
			
			// 仕訳一覧データを取得する
			torihikiList = kaikeiLogic.selectTorihikiIchiran(denpyouKbn, sortItem, sortOrder);
			if(null == torihikiList || torihikiList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// イベントURL(遷移元画面)設定
			preEventUrl = "torihiki_ichiran_kensaku?"  
					+ "denpyouKbn=" + denpyouKbn + "&"
					+ "sortItem=" + sortItem + "&"
					+ "sortOrder=" + sortOrder + "&"
					+ "bunrui1Val=" + URLEncoder.encode(bunrui1Val,"UTF-8") + "&"
					+ "bunrui2Val=" + URLEncoder.encode(bunrui2Val,"UTF-8") + "&"
					+ "bunrui3Val=" + URLEncoder.encode(bunrui3Val,"UTF-8") + "&"
					+ "keywordVal=" + URLEncoder.encode(keywordVal,"UTF-8");
			
			// 表示用にデータを変更します。
			for(GMap map : torihikiList) {
				String strKigenFrom = formatDate(map.get("yuukou_kigen_from"));
				String strKigenTo = formatDate(map.get("yuukou_kigen_to"));
				map.put("yuukou_kigen_from", strKigenFrom);
				map.put("yuukou_kigen_to", strKigenTo);
				map.put("bg_color", EteamCommon.bkColorSettei(strKigenFrom, strKigenTo));
				map.put("kasi_kamoku_disp",myLogic.makeKashiKamokuView(map));
			}

			//5.戻り値を返す
			return "success";
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
		}
	}

	
	/**
	 * 削除イベント
	 * 削除ボタン押下時
	 * 選択した明細を削除する。
	 * @return 処理結果
	 */
	public String sakujo(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			SystemKanriCategoryLogic systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			// 入力チェック
			hissuCheck(3);
			formatCheck();
			
			// 伝票区分の存在チェック
			checkCode(connection, denpyouKbn, INFO_DENPYOU_SHUBETSU_CD_ID, "伝票区分", false);
			
			/* 伝票区分DropDownリスト作成 */
			// 内部コード設定からキー・名称を取得(内部コード名 = "shiwake_pattern_denpyou_kbn")
			infoDenpyouShubetsuList = systemLogic.loadNaibuCdSetting(INFO_DENPYOU_SHUBETSU_CD_ID);
			
			// 該当の取引(仕訳)を削除
			int cnt = kaikeiLogic.deleteTorihikiIchiran(getUser().getTourokuOrKoushinUserId(), denpyouKbn, Integer.parseInt(shiwakeEdano));
			if(cnt != 1) {
				log.debug("■■レコード削除数："+ cnt);
				errorList.add("削除処理に失敗しました。");
				return "error";
			}
			
			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}
}
