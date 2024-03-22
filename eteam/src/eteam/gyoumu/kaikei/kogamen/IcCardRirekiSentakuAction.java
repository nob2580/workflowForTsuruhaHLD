package eteam.gyoumu.kaikei.kogamen;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.gyoumu.ic.ICLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ICカード使用履歴選択ダイアログAction
 */
@Getter @Setter @ToString
public class IcCardRirekiSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	//パラメータ
	/** 伝票ID */
	String denpyouId;
	/** ユーザーID */
	String userId;
	/** ソート区分 */ //1:昇順、2:降順
	String sortKbn;
	/** 除外データ表示フラグ */ //チェックボックスがONなら1、OFFなら0(hidden)
	String jyogaiDataHyoujiFlg;
	/** 除外フラグ変更対象リスト */ //1-1,1-2,2-1,2-2 カード番号-シーケンス番号のカンマ区切り
	String jyogaiChangeList;
	/** 除外フラグ変更先 */ //1:除外、0:復活
	String jyogaiChangeFlg;
	
	//画面表示項目
	/** 期間開始日（検索条件） */
	String joukenRiyouKikanFrom;
	/** 期間終了日（検索条件） */
	String joukenRiyouKikanTo;
	/** 出発路線名（検索条件） */
	String joukenLineNameFrom;
	/** 出発駅名（検索条件） */
	String joukenEkiNameFrom;
	/** 到着路線名（検索条件） */
	String joukenLineNameTo;
	/** 到着駅名（検索条件） */
	String joukenEkiNameTo;

//＜画面入力以外＞
	/** 一覧 */
	List<GMap> list;
	
	/** 会計カテゴリSELECT */
	KaikeiCategoryLogic kaikeiCategoryLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLg;
	/** ICカードロジック */
	ICLogic icLogic;

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDate(joukenRiyouKikanFrom, "利用日", false);
		checkDate(joukenRiyouKikanTo, "利用日", false);
		//varchar型のため、チェック未実施
		//checkString(joukenLineNameFrom, 1,  20, "出発路線名", false);
		//checkString(joukenEkiNameFrom, 1,  20, "出発駅名", false);
		//checkString(joukenLineNameTo, 1,  20, "到着路線名", false);
		//checkString(joukenEkiNameTo, 1,  20, "到着駅名", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] itemList = {
			//項目									EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目)) 
			{joukenRiyouKikanFrom ,"期間開始日"	,"0"},
			{joukenRiyouKikanTo ,"期間終了日"	,"0"}, 
			{joukenLineNameFrom ,"出発路線名"	,"0"},
			{joukenEkiNameFrom ,"出発駅名"		,"0"},
			{joukenLineNameTo ,"到着路線名"	,"0"},
			{joukenEkiNameTo ,"到着駅名"		,"0"},
		};
		hissuCheckCommon(itemList, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		//1.入力チェック なし
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし
			//4.処理（初期表示）
			return loadData();
			
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
			initParts(connection);
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし
			//4.処理（初期表示）
			List<String[]> jcList = makeJyogaiChangeList();
			
			for(String[] str : jcList) {
				icLogic.setJyogaiFlg(str[0], str[1], jyogaiChangeFlg);
			}
			connection.commit();
			return loadData();
		}
	}
	
	/**
	 * 共通表示処理
	 * @return 処理結果
	 */
	protected String loadData() {
		//期間開始日が入力されていなければ「1900/01/01」を設定。
		//期間終了日が入力されていなければ「9999/12/31」を設定。
		String kikankaishi = joukenRiyouKikanFrom;
		String kikansyuuryou = joukenRiyouKikanTo;
		if(isEmpty(kikankaishi)) {
			kikankaishi = "1900/01/01";
		}
		if(isEmpty(kikansyuuryou)) {
			kikansyuuryou = "9999/12/31";
		}
		
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
		
		list = kaikeiCategoryLogic.loadIcCardRireki(userId, toDate(kikankaishi), toDate(kikansyuuryou), joukenLineNameFrom, joukenEkiNameFrom, joukenLineNameTo, joukenEkiNameTo, sortKbn, jyogaiDataHyoujiFlg);
		if(list.isEmpty()){
			errorList.add("検索結果が0件でした。");
			return "error";
		}
		
		//形式変換
		for (GMap map : list) {
			map.put("ic_card_riyoubi", formatDate(map.get("ic_card_riyoubi")));
			map.put("kingaku", formatMoney(map.get("kingaku")));
		}
				
		return "success";
	}

	
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		kaikeiCategoryLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		icLogic = EteamContainer.getComponent(ICLogic.class, connection);
	}
	
	/**
	 * ICカード番号/シーケンス番号格納用のjyogaiChangeListを分割
	 * @return ICカード番号/シーケンス番号のリスト
	 */
	protected List<String[]> makeJyogaiChangeList() {
		List<String[]> ret = new ArrayList<>();
		String[] listCardNo = jyogaiChangeList.split(",");
		for(int i = 0 ; i < listCardNo.length ; i++ ){
			String[] tmpStr = listCardNo[i].split("-");
			String cardNo = tmpStr[0];
			String cardSeq = tmpStr[1];
			ret.add(new String[]{cardNo, cardSeq});
		}
		return ret;
	}
	
}
