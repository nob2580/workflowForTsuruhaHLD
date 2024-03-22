package eteam.gyoumu.kaikei.kogamen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 旅費精算(過去明細選択）ダイアログAction
 */
@Getter @Setter @ToString
public class RyohiSeisanKakoMeisaiSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	//起動パラメータ
	/** 伝票ID */
	String denpyouId;
	/** 伝票区分 */
	String denpyouKbn;
	/** 種別コード(1:交通費、2:日当・宿泊費等) */
	String shubetsuCd;
	/** 海外フラグ */
	String kaigaiFlgRyohi;
	/** ユーザーID */
	String userId;
	/** 取引課税フラグ */
	String kazeiFlgRyohi;
	/** 科目課税フラグ */
	String kazeiFlgKamoku;

	//画面表示項目
	/** 期間開始日（検索条件） */
	String joukenKikanFrom;
	/** 期間終了日（検索条件） */
	String jokenKikanTo;
	/** 内容（検索条件） */
	String jokenNaiyou;

//＜画面入力以外＞
	/** 過去明細リスト */
	List<GMap> kakoMeisaiList;

	/** 会計カテゴリ */
	KaikeiCategoryLogic kaikeilc;
	/** 会計共通 */
	KaikeiCommonLogic commonLg;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouId, 19, 19, "伝票ID", false);
		checkString(denpyouKbn, 4, 4, "伝票区分", true);
		checkString(shubetsuCd, 1, 1, "種別コード", true);
		checkDate(joukenKikanFrom, "期間開始日", false);
		checkDate(jokenKikanTo, "期間終了日", false);
		checkString(jokenNaiyou, 1,  512, "内容", false);
	}
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] itemList = {
			//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目)) 
			{denpyouKbn ,"伝票区分"		,"2"},
			{shubetsuCd ,"種別コード"	,"2"},
			{joukenKikanFrom ,"期間開始日"	,"0"},
			{jokenKikanTo ,"期間終了日"	,"0"}, 
			{jokenNaiyou ,"内容"			,"0"},
		};
		hissuCheckCommon(itemList, eventNum);
	}

//＜イベント＞
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		//1.入力チェック
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }

		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理
			if(joukenKikanFrom == null) {
				// 表示イベントの場合、期間開始日をシステム日付の６ヶ月前に指定する
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -6);
			    cal.set(Calendar.DAY_OF_MONTH,1);
			    Date date = cal.getTime();
			    joukenKikanFrom = new SimpleDateFormat("yyyy/MM/dd").format(date);
			}

			//過去明細SELECT
			//期間開始日が入力されていなければ「1900/01/01」を設定。
			//期間終了日が入力されていなければ「9999/12/31」を設定。
			String kikankaishi = joukenKikanFrom;
			String kikansyuuryou = jokenKikanTo;
			if(isEmpty(kikankaishi)) {
				kikankaishi = "1900/01/01";
			}
			if(isEmpty(kikansyuuryou)) {
				kikansyuuryou = "9999/12/31";
			}
			
			// 起票ユーザーID
			String userIdTmp = getUser().getSeigyoUserId();
			if (StringUtils.isNotEmpty(denpyouId)) {
				GMap kihyouUser = commonLg.findKihyouUser(denpyouId);
				if(kihyouUser != null && kihyouUser.get("user_id") != null){
					userIdTmp = kihyouUser.get("user_id").toString();
				}
			}
			
			// リクエストのユーザーIDがブランクでなければ起票ユーザーIDを上書き
			if (!denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN))
			{
				userIdTmp = this.userId;
			}
			
			kakoMeisaiList = findKakoMeisai(kikankaishi, kikansyuuryou, userIdTmp);
			
			if(kakoMeisaiList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//形式変換
			for (GMap map : kakoMeisaiList) {
				map.put("kikan_from", formatDate(map.get("kikan_from")));
				map.put("kikan_to", formatDate(map.get("kikan_to")));
				map.put("tanka", formatMoneyDecimalWithNoPadding(map.get("tanka")));
				map.put("suuryou", formatMoneyDecimalWithNoPadding(map.get("suuryou")));
				
				// 期間の編集
				if("".equals(map.get("kikan_to"))) {
					map.put("kikan", map.get("kikan_from"));
				} else {
					map.put("kikan", map.get("kikan_from") + "～" + map.get("kikan_to"));
				}
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		kaikeilc = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
	}
	
	/**
	 * 過去明細を取得
	 * @param kikankaishi 期間開始
	 * @param kikansyuuryou 期間終了
	 * @param userIdTmp ユーザーID
	 * @return 過去明細リスト
	 */
	protected List<GMap> findKakoMeisai(String kikankaishi, String kikansyuuryou, String userIdTmp) {
		List<GMap> list = new ArrayList<GMap>();
		
		//旅費精算、出張伺いの場合
		if(denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI)){
			list = kaikeilc.loadRyohiSeisanMeisaiForUser(toDate(kikankaishi), toDate(kikansyuuryou), jokenNaiyou, userIdTmp, shubetsuCd);
		//海外旅費精算、海外出張伺いの場合
		}else if(denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI)){
			list = kaikeilc.loadKaigaiRyohiSeisanMeisaiForUser(toDate(kikankaishi), toDate(kikansyuuryou), jokenNaiyou, userIdTmp, shubetsuCd, kaigaiFlgRyohi);
		//交通費精算の場合
		}else if(denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN)){
			list = kaikeilc.loadKoutsuuhiSeisanMeisaiForUser(toDate(kikankaishi), toDate(kikansyuuryou), jokenNaiyou, userIdTmp);
		}
		return list;
	}
}
