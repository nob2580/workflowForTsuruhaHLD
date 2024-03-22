package eteam.gyoumu.kaikei.kogamen;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 負担部門選択ダイアログAction
 */
@Getter @Setter @ToString
public class KaribaraiAnkenSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票区分 */
	String denpyouKbn;
	/** 伝票ID */
	String denpyouID;
	/** 伝票ID(親) */
	String oyaDenpyouId;
	/** ユーザーID **/
	String userId;

//＜画面入力以外＞
	/** 一覧 */
	List<GMap> list;
	/** 仮払案件選択のひも付け可否 */
	boolean tenpuDenpyouJidouFlg = setting.tenpuDenpyouJidouFlg().equals("1") ? true : false;

	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouKbn, 4, 4,"伝票区分",true);
		checkString(denpyouID ,19,19,"伝票ID"  ,false);
		checkString(oyaDenpyouId ,19,19,"伝票ID(親)"  ,false);
// checkString(userId ,1,30,"ユーザーID"  ,false); // 経費精算の場合はブランクのため、チェックしない
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] itemList = {
			//項目					EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouKbn     ,"伝票区分"      ,"2"}, 
			{denpyouID      ,"伝票ID"        ,"0"}, 
			{userId         ,"ユーザーID"    ,"0"}, // 経費精算の場合はブランクのため、チェックしない
};
		hissuCheckCommon(itemList, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		//1.入力チェック
		hissuCheck(1);
		formatCheck();

		if(0 < errorList.size()){
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect())
		{ 
			//3.アクセス可能チェック（権限チェック）
			
			//4.処理（検索）
			
			/* セッションからログインユーザー情報を取得します。 */
			User loginUserInfo = getUser();
			String seigyoUserId = loginUserInfo.getSeigyoUserId();
			
			KaikeiCommonLogic commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			
			// 起票ユーザーID
			if (StringUtils.isNotEmpty(oyaDenpyouId)) {
				GMap kihyouUser = commonLg.findKihyouUser(oyaDenpyouId);
				if(kihyouUser != null && kihyouUser.get("user_id") != null){
					seigyoUserId = kihyouUser.get("user_id").toString();
				}
			}
			
			// 旅費精算・海外旅費精算の場合
			if(denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)){
				seigyoUserId = userId;
			}
			
			//データ取得 
			KaikeiCategoryLogic lg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			WorkflowEventControlLogic worklg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
			
			list = lg.loadKaribaraiAnken(denpyouKbn, denpyouID, seigyoUserId);
			
			if(list == null || list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			for (GMap map : list) {
				map.put("kingaku",formatMoney(map.get("kingaku"))); // 金額
				map.put("ringi_hikitsugi_um_flg",map.get("karibarai_on").equals("1") ? map.get("ringi_hikitsugi_um_flg") : "" ); //稟議金額引継ぎフラグ
				map.put("karibarai_on",map.get("karibarai_on").equals("1") ? "あり" : "なし" ); // 仮払あり・なし
				map.put("shinsei_kingaku",formatMoney(map.get("shinsei_kingaku"))); // 金額
				map.put("karibarai_kingaku",formatMoney(map.get("karibarai_kingaku"))); // 仮払金額
				
				// 旅費精算・海外旅費精算の場合
				if(denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)) {
					map.put("seisankikan_from",formatDate(map.get("seisankikan_from"))); // 期間開始日
					map.put("seisankikan_to",formatDate(map.get("seisankikan_to"))); // 期間終了日
				}
				
				//起案添付済みフラグを設定する
				boolean isKianTempuZumi = commonLg.isKaribaraiKianTenpuZumi(oyaDenpyouId, map.get("denpyou_id").toString());
				map.put("kian_tenpu_zumi_flg", isKianTempuZumi? "1" : "0");
			}
			
			// 関連伝票ひも付け用リスト作成
			if (tenpuDenpyouJidouFlg) {
				worklg.formatKanrenDenpyou(list);
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
}
