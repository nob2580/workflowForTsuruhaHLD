package eteam.gyoumu.system;

import eteam.base.EteamAbstractAction;

/**
 * バックアップ運用受理画面Action
 */
public class BackupUnyouJyuriAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞

	
//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		
		//1.入力チェック なし
		//2.データ存在チェック なし
		//3.アクセス可能チェック なし
		//4.処理

		//5.戻り値を返す
		return "success";
	}

}
