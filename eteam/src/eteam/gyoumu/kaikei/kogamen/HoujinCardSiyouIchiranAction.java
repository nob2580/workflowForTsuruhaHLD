package eteam.gyoumu.kaikei.kogamen;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.houjincard.HoujinCardLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 法人カード使用履歴選択ダイアログAction
 */
@Getter @Setter @ToString
public class HoujinCardSiyouIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	//パラメータ
	/** 伝票ID */
	protected String denpyouId;
	/** ユーザーID */
	protected String userId;
	/** 伝票区分 */
	protected String denpyouKbn;
	/** ソート区分 */
	String sortKbn;
	/** 経費立替精算の代理起票フラグ */
	String dairiFlg; 
	
	//画面表示項目
	/** カード種別 */
	String searchCardShubetsu;
	/** 期間開始日（検索条件） */
	String joukenRiyouKikanFrom;
	/** 期間終了日（検索条件） */
	String joukenRiyouKikanTo;

//＜画面入力以外＞
	/** 一覧 */
	List<GMap> list;
	/** カード種別のDropDownList */
	List<GMap> cardList;
	/** 転記先明細のDropDownList */
	List<GMap> meisaiShuList;
	/** イベントURL(遷移元画面) */
	String preEventUrl;

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDate(joukenRiyouKikanFrom, "日付From", false);
		checkDate(joukenRiyouKikanTo, "日付To", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] itemList = {
			//項目									EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目)) 
			{searchCardShubetsu ,"対象カード","0"},
			{joukenRiyouKikanFrom ,"日付From"	,"0"},
			{joukenRiyouKikanTo ,"日付To"	,"0"}, 
		};
		hissuCheckCommon(itemList, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			KaikeiCommonLogic commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			HoujinCardLogic hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			BumonUserKanriCategoryLogic buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			// naibu_cd_settingテーブルからデータを取得（カード種別プルダウンリストの値）
			cardList = common.loadNaibuCdSetting("houjin_card_shubetsu");
			//ブランク用設定追加
			GMap mp = new GMap();
			mp.put("naibu_cd", "");
			mp.put("name", "");
			mp.put("option1", "");
			mp.put("option2", "");
			mp.put("option3", "");
			cardList.add(0,mp);
			
			//1.入力チェック
			hissuCheck(1);
			formatCheck();
			if(0 < errorList.size()){ return "error"; }
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック
			
			//全表示指定時、ログイン中ユーザーに代理起票権限が無ければアクセスエラー
			boolean anyFlg = false;
			if("1".equals(dairiFlg)){
				//代理権限ないユーザーはできない（メニューで非表示だがURL直接来られた時制御が破綻するし）
				GMap userJouhouMap = buLogic.selectUserJouhou(getUser().getSeigyoUserId());
				String userDairiKihyouFlg = (String)userJouhouMap.get("dairikihyou_flg");
				if (userDairiKihyouFlg.equals("0")) {
					throw new EteamAccessDeniedException("代理起票権限ないユーザーでの全参照は不可能");
				}
				//代行モードで代理起票させない（メニューで非表示だがURL直接来られた時制御が破綻するし）
				if (! getUser().getLoginUserId().equals(getUser().getSeigyoUserId())) {
					throw new EteamAccessDeniedException("代行モードでの全参照は不可能");
				}
				anyFlg = true;
			}

			//4.処理（初期表示）
			
			
			preEventUrl = "houjin_card_siyou_ichiran"
					+ "?searchCardShubetsu=" + searchCardShubetsu
					+ "&denpyouId=" + denpyouId
					+ "&userId=" + userId
					+ "&denpyouKbn=" + denpyouKbn
					+ "&joukenRiyouKikanFrom=" + URLEncoder.encode((joukenRiyouKikanFrom != null ? joukenRiyouKikanFrom : "") ,"UTF-8")
					+ "&joukenRiyouKikanTo=" + URLEncoder.encode((joukenRiyouKikanTo != null ? joukenRiyouKikanTo : "") ,"UTF-8")
					+ "&dairiFlg=" + dairiFlg + "&";
			
			
			setMeisaiShubetsuList();
			
			//ログインユーザーID・・・パラメータに指定があればそれ(出張や交通費の場合の使用者) > パラメータに伝票IDがあれば起票者 > ログインユーザID
			if(isEmpty(userId)){
				if (isNotEmpty(denpyouId)) {
					GMap kihyouUser = commonLg.findKihyouUser(denpyouId);
					if(kihyouUser != null && kihyouUser.get("user_id") != null){
						userId = kihyouUser.get("user_id").toString();
					}
				} else{
					userId =getUser().getSeigyoUserId();
				}
			}
			
			list = hcLogic.loadCardRirekiForSentakuDialog(searchCardShubetsu, userId, toDate(joukenRiyouKikanFrom), toDate(joukenRiyouKikanTo), sortKbn, anyFlg, denpyouId);
			
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//形式変換
			for (GMap map : list) {
				
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
				
				//別伝票で使用済みかチェック
				if( !("".equals(map.get("torikomi_denpyou_id"))) ){
					//取込先伝票が開いた伝票と一致する場合は使用済みとしない(グレーアウトは画面側で制御させる)
					if( isEmpty(denpyouId) || !(denpyouId.equals(map.get("torikomi_denpyou_id"))) ){
						map.put("isUsed", true);
						map.put("bg_color", "disabled-bgcolor");
					}else{
						map.put("isUsed", false);
					}
				}else{
					map.put("isUsed", false);
				}
			}
			
					
			return "success";
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 伝票種別ごとの明細種別リストを作成
	 */
	protected void setMeisaiShubetsuList(){
		//TODO 余裕があれば構造整理
		meisaiShuList = new ArrayList<GMap>();
		switch(denpyouKbn){
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN :
			GMap mapA = new GMap();
			mapA.put("naibu_cd", "1");
			mapA.put("name", "経費明細");
			meisaiShuList.add(mapA);
			break;
		case DENPYOU_KBN.RYOHI_SEISAN :
			GMap mapB1 = new GMap();
			mapB1.put("naibu_cd", "1");
			mapB1.put("name", "旅費・交通費");
			meisaiShuList.add(mapB1);
			GMap mapB2 = new GMap();
			mapB2.put("naibu_cd", "2");
			mapB2.put("name", "旅費・宿泊費");
			meisaiShuList.add(mapB2);
			GMap mapB3 = new GMap();
			mapB3.put("naibu_cd", "3");
			mapB3.put("name", "その他経費");
			meisaiShuList.add(mapB3);
			break;
		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			GMap mapC = new GMap();
			mapC.put("naibu_cd", "1");
			mapC.put("name", "交通費明細");
			meisaiShuList.add(mapC);
			break;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			GMap mapD1 = new GMap();
			mapD1.put("naibu_cd", "1");
			mapD1.put("name", "(国内)旅費・交通費");
			meisaiShuList.add(mapD1);
			GMap mapD2 = new GMap();
			mapD2.put("naibu_cd", "2");
			mapD2.put("name", "(国内)旅費・宿泊費");
			meisaiShuList.add(mapD2);
			GMap mapD3 = new GMap();
			mapD3.put("naibu_cd", "3");
			mapD3.put("name", "(国内)その他経費");
			meisaiShuList.add(mapD3);
			GMap mapD4 = new GMap();
			mapD4.put("naibu_cd", "4");
			mapD4.put("name", "(海外)旅費・交通費");
			meisaiShuList.add(mapD4);
			GMap mapD5 = new GMap();
			mapD5.put("naibu_cd", "5");
			mapD5.put("name", "(海外)旅費・宿泊費");
			meisaiShuList.add(mapD5);
			GMap mapD6 = new GMap();
			mapD6.put("naibu_cd", "6");
			mapD6.put("name", "(海外)その他経費");
			meisaiShuList.add(mapD6);
			break;
		default:
			break;
		}
	}
	
}
