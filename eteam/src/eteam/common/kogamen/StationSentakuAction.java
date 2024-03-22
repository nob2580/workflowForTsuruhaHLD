package eteam.common.kogamen;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import eteam.base.EteamAbstractAction;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.EteamEkispertCommonWeb;

/**
 * 駅名選択ダイアログAction
 */
@Getter @Setter @ToString
public class StationSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(StationSentakuAction.class);
	
	/** 駅名 */
	String searchName;
	
	/** 部門ロール一覧 */
	List<String> list;
	
//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {

		try{
			EteamEkispertCommonWeb eecw = EteamContainer.getComponent(EteamEkispertCommonWeb.class);
			
			//4.処理（初期表示）
			list = eecw.searchStationList(searchName);

			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
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
