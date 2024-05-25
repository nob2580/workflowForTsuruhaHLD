package eteam.common;

import java.util.List;

import eteam.base.GMap;

public class SecurityLogExtLogic extends SecurityLogLogic {
	//▼▼カスタマイズ
	/**
	 * 直近の２日前まで遡り、ログイン情報を取得する
	 * @param ip 接続元IP
	 * @param ipx 接続元IP(X-Forwarded-For)
	 * @param userId ユーザーID
	 * @param schema スキーマ
	 * @return 設定値
	 */
	public List<GMap> loadSecurityLog(String ip, String ipx, String userId, String schema){
		String sql =  "SELECT * FROM  " + schema + ".security_log"
					+ " WHERE ip = '" + ip + "'";
				if (ipx != null) {
					sql += " AND ip_xforwarded = '" + ipx + "'";
				}
				sql += " AND user_id = '" + userId + "'";
				sql += " AND type = '" + LogType.TYPE_LOGIN + "'";
				sql += " AND detail = '" + LogDetail.LOGIN_SUCCESS + "'";
				sql += " AND event_time > current_timestamp + '-2 days';";
		return connection.load(sql);
	}
	//▲▲カスタマイズ
}
