package eteam.gyoumu.user;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamSettingInfo;
import eteam.common.util.SerializeUtil;
import eteam.common.util.ServletUtil;

/**
 * ユーザセッション処理
 */
public class UserSessionLogic extends EteamAbstractLogic {

	/**
	 * ユーザセッション更新
	 * @param user ユーザ
	 */
	public void updateUserSession(User user) {
		String jsessionId = ServletUtil.getJSessionId();
		String schema = EteamCommon.getContextSchemaName();
		byte[] data = SerializeUtil.serialize(user);
		String sql = "INSERT INTO public.user_session "
					+ "VALUES(?, ?, ?, current_timestamp) "
					+ "ON CONFLICT(jsession_id, schema) "
					+ "DO UPDATE SET data = ?, time = CURRENT_TIMESTAMP ";
		connection.update(sql ,jsessionId, schema, data, data);
		
	}
	
	/**
	 * ユーザセッションチェック。有効ならユーザを返すし無効ならnull。
	 * @return ユーザ
	 */
	public User userSessionCheck() {
		String loginIjiJikan = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.LOGIN_IJI_JIKAN);
		String jsessionId = ServletUtil.getJSessionId();
		String schema = EteamCommon.getContextSchemaName();
		String sql = "UPDATE public.user_session "
					+ "SET time = CURRENT_TIMESTAMP "
					+ "WHERE jsession_id = ? AND schema = ? AND time + INTERVAL '" + loginIjiJikan + " HOURS' > CURRENT_TIMESTAMP "
					+ "RETURNING data ";
		GMap record = connection.find(sql, jsessionId, schema);
		return record == null ? null : SerializeUtil.deserialize(record.get("data"));
	}
	
	/**
	 * ユーザセッション削除
	 */
	public void deleteUserSession() {
		String jsessionId = ServletUtil.getJSessionId();
		String schema = EteamCommon.getContextSchemaName();
		String sql = "DELETE FROM public.user_session "
					+ "WHERE jsession_id = ? AND schema = ? ";
		connection.update(sql, jsessionId, schema);
	}
}
