package eteam.common.select;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * ヘルプカテゴリー内のSelect文を集約したLogic
 */
public class HelpCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* ヘルプ */
	/**
	 * 画面に対するヘルプレコードが作成されているかどうか調べる。
	 * @param gamenId 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分)
	 * @return 存在するならtrue
	 */
	public boolean hasHelp(String gamenId) {
		final String sql = "SELECT COUNT(gamen_id) count FROM help WHERE gamen_id = ?";
		long count = (long)connection.find(sql, gamenId).get("count");
		return (1 == count);
	}
	
	
	/**
	 * 画面に対するヘルプレコードを取得する
	 * @param gamenId 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分)
	 * @return ヘルプレコード
	 */
	public GMap find(String gamenId) {
		final String sql = "SELECT gamen_id, help_rich_text FROM help WHERE gamen_id = ?";
		
		return connection.find(sql, gamenId);
		
	}
}
