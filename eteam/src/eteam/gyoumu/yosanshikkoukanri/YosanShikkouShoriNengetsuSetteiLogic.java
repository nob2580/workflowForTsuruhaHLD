package eteam.gyoumu.yosanshikkoukanri;

import eteam.base.EteamAbstractLogic;

/**
 * 速報PL処理年月設定Logic（予算執行管理オプションＢ）
 */
public class YosanShikkouShoriNengetsuSetteiLogic extends EteamAbstractLogic {

	/**
	 * 予算執行処理年月を追加する。
	 * 
	 * @param fromNengetsu 開始処理年月
	 * @param toNengetsu   終了処理年月
	 */
	public void insert(String fromNengetsu, String toNengetsu){
		super.connection.update("INSERT INTO yosan_shikkou_shori_nengetsu(from_nengetsu, to_nengetsu) VALUES(?, ?)", fromNengetsu, toNengetsu);
	}
	
	/**
	 * 予算執行処理年月を更新する。
	 * 
	 * @param fromNengetsu 開始処理年月
	 * @param toNengetsu   終了処理年月
	 */
	public void update(String fromNengetsu, String toNengetsu){
		super.connection.update("UPDATE yosan_shikkou_shori_nengetsu SET from_nengetsu = ?, to_nengetsu = ?", fromNengetsu, toNengetsu);
	}
}
