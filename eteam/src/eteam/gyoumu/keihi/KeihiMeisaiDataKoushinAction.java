package eteam.gyoumu.keihi;

import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 経費明細データ更新Action
 */
@Getter @Setter @ToString
public class KeihiMeisaiDataKoushinAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 対象日付 */
	String targetDate;

//＜画面入力以外＞
	/** 選択月リスト */
	@Getter
	protected List<Map<String, String>> monthList;


//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDate(targetDate, "対象日付", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{targetDate, "対象日付", "0", "1"},
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			monthList = EteamCommon.createMonthList();
			//1.入力チェック なし
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし
			//4.処理 なし
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 連携イベント処理
	 * @return ResultName
	 */
	public String renkei() {
		try(EteamConnection connection = EteamConnection.connect()) {
			// 1.入力チェック なし
			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理
			KeihiMeisaiDataKoushinThread myThread = new KeihiMeisaiDataKoushinThread(EteamCommon.getContextSchemaName(), toDate(targetDate));
			myThread.start();

			//5.戻り値を返す
			return "success";
		}
	}
}
