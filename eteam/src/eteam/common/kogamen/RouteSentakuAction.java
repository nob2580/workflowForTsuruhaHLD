package eteam.common.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamEkispertCommonWeb;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 経路選択ダイアログAction
 */
@Getter @Setter @ToString
public class RouteSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 日付 */
	protected String date;
	/** 使用期間区分 */
	protected String shiyouKikanKbn;
	/** 出発地 */
	protected String from;
	/** 経由/到着地1 */
	protected String to01;
	/** 経由/到着地2 */
	protected String to02;
	/** 経由/到着地3 */
	protected String to03;
	/** 到着地4 */
	protected String to04;
	/** 表示件数 */
	protected String hyoujiCnt;
	/** ソート区分 */
	protected String sortKbn;
	/** 定期区間 */
	protected String teiki;
//＜画面入力以外＞
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(RouteSentakuAction.class);
	
	/** 経路リスト(WebService) */
	protected List<GMap> keiroList;
	
//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {

		try(EteamConnection connection = EteamConnection.connect()){

			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			
			// 駅すぱあとWebサービスから経路を取得
			keiroList = eecw.createRouteList(from, 
					to01, to02, to03, to04, date, shiyouKikanKbn, hyoujiCnt, sortKbn, teiki);
			
			//4.処理（初期表示）
			if(keiroList.isEmpty()){
				errorList.add("検索結果が0件でした。条件を見直してください。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		} catch (Exception e) {
			log.error(e);
			errorList.add("WEB APIでエラーが発生しました。");
			return "error";
			

		}
	}
}
