package eteam.gyoumu.help;

import java.util.regex.Pattern;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.common.EteamCommon;
import eteam.common.GamenKengenSeigyoLogic;
import eteam.common.select.HelpCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeAction;
import eteam.gyoumu.user.User;

/**
 * ヘルプLogic
 */
public class HelpLogic extends EteamAbstractLogic {

	/**
	 * 画面IDからヘルプ表示有無を判定して、表示設定を行う。
	 * 表示の必要があるのは、ログインしている前提で、以下のいずれかが成立する時。
	 * ・画面IDに対するヘルプレコードがある。
	 * ・ヘルプ編集権限がある。（編集権限があるなら未登録のヘルプ画面も表示する）
	 * ただし、未ログインの場合は無条件に表示しない。
	 * @param user セッションユーザー。未ログインならnull。
	 * @param gamenId 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分)
	 * @return 表示の必要があるならtrue
	 */
	public boolean judgeHelpDisplay(User user, String gamenId) {

		boolean ret = false;
		
		if (null == user)
		{
			return ret;
		}

		HelpCategoryLogic helpLogic = EteamContainer.getComponent(HelpCategoryLogic.class, connection);
		GamenKengenSeigyoLogic kengenLogic = EteamContainer.getComponent(GamenKengenSeigyoLogic.class, connection);

		return helpLogic.hasHelp(gamenId) || kengenLogic.judgeAccess(EteamCommon.makeGamenId(HelpEditorAction.class), user);
	}

	/**
	 * 画面名を取得する
	 * 通常は画面権限制御テーブルから、ユーザー定義届書なら伝票種別一覧から。
	 * @param gamenId 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分)
	 * @return 画面名
	 */
	public String judgeGamenName(String gamenId) {
		SystemKanriCategoryLogic skcl = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowCategoryLogic wLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		String ret;

		String kaniTodokeGamenId = EteamCommon.makeGamenId(KaniTodokeAction.class);
		
		if (Pattern.compile(kaniTodokeGamenId + "B[0-9]{3}").matcher(gamenId).find()) {
			String denpyoKbn = gamenId.substring(kaniTodokeGamenId.length());
			ret = (String)wLogic.selectDenpyouShubetu(denpyoKbn).get("denpyou_shubetsu");
		} else {
			ret = (String)skcl.findGamenName(gamenId).get("gamen_name");
		}
		return ret;
	}
	
	/**
	 * ヘルプ編集内容を登録する。
	 * @param userId ユーザーID
	 * @param gamenId 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分)
	 * @param text ヘルプ入力テキスト
	 * @param hasHelp 画面IDに該当するレコード有無
	 * @return 登録件数
	 */
	public int insertHelp(String userId, String gamenId, String text, boolean hasHelp) {
	
		int ret = 0;
		
		if (hasHelp) {

			final String updSql = "UPDATE help " + 
					 "   SET help_rich_text = ?, " +
					 "       koushin_user_id = ?, " +
					 "       koushin_time = current_timestamp " +
					 " WHERE gamen_id = ? ";

			ret = connection.update(updSql, text, userId, gamenId);
		} else {

			final String insSql = "INSERT INTO help " + 
				  	 "VALUES (?, ?, ?, current_timestamp, ?, current_timestamp)";
			
			ret = connection.update(insSql, gamenId, text, userId, userId);
		
		}

		return ret;
	}
	
	/**
	 * ヘルプ編集内容を削除する。
	 * @param gamenId 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分)
	 * @return 削除件数
	 */
	public int deleteHelp(String gamenId) {
		
		final String sql = "DELETE FROM help WHERE gamen_id = ?";
		
		return connection.update(sql, gamenId);
		
	}
}
