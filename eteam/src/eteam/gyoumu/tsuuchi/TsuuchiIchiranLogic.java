package eteam.gyoumu.tsuuchi;

import eteam.base.EteamAbstractLogic;

/**
 * 通知一覧画面Logic
 */
public class TsuuchiIchiranLogic extends EteamAbstractLogic {
		
	/**
	 * 既読フラグを更新する
	 * @param serialNo シリアルNO
	 * @param mode 更新モード(既読 : 1 未読 : 0)
	 * @param userId 登録ユーザーID
	 * @return 更新件数
	 */
	public int koushin(String[] serialNo, String mode, String userId) {

		final String sql = "UPDATE tsuuchi " + 
				 "   SET kidoku_flg = ?, " +
				 "       koushin_user_id = ?, " +
				 "       koushin_time = current_timestamp " +
				 " WHERE serial_no = ? ";
		
		int ret = 0;
		for (String no : serialNo) {
			ret = ret + connection.update(sql, mode, userId, Long.parseLong(no));
		}
		
		return ret;
	}

}
