package eteam.common.kogamen;

import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 会社切替選択ダイアログAction
 */
@Getter @Setter @ToString
public class KaishaKirikaeSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** 会社切替一覧 */
	List<Map<String, Object>> list;

//部品
	/** 会社切替選択ダイアログLogic */
	private KaishaKirikaeSentakuLogic myLogic;

//＜入力チェック＞
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	private void initParts(EteamConnection connection) {
		// 会社切替選択ダイアログLogic
		myLogic = EteamContainer.getComponent(KaishaKirikaeSentakuLogic.class);
		myLogic.init(connection);
	}

	/**
	 * 共通制御処理<br>
	 * 検索イベントやエラー表示時用に画面の共通制御を行う。<br>
	 */
	private void displaySeigyo() {
		// 会社切替一覧を取得する。
		list = myLogic.loadKaishaKirikaeList();
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * 
	 * @return 処理結果
	 */
	public String init(){
		EteamConnection connection = EteamConnection.connect();
		try{
			// 使用する部品の初期化を行う。
			initParts(connection);
			// 画面共通制御を行う。
			displaySeigyo();
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 1.アクセス可能チェック なし
			// 2.入力チェック なし
			// 3.アクセス可能チェック なし
			// 4.処理 なし

			// 5.戻り値を返す
			return "success";
		}finally{
			connection.close();
		}
	}
}
