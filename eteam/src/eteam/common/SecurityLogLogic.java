package eteam.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;

/**
 * セキュリティに関する業務ログを明示的に取得するロジック。
 */
public class SecurityLogLogic extends EteamAbstractLogic {

	/**
	 * ログ種別の列挙
	 */
	public enum LogType {
		/** ログイン */
		TYPE_LOGIN("ログイン"),
		/** アカウント */
		TYPE_ACCOUNT("アカウント"),
		/** パスワード変更 */
		TYPE_PASSCHG("パスワード変更"),
		;
		/** 表示用の値 */
		protected String value;
		/**
		 * コンストラクタ
		 * @param value 表示用の値
		 */
		private LogType(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * ログ詳細の列挙
	 */
	public enum LogDetail {
		/** ログイン成功 */
		LOGIN_SUCCESS("成功"),
		/** ログイン失敗(パスワード誤り) */
		LOGIN_FAIL_PASS("失敗(パスワード誤り)"),
		/** ログイン失敗(一時ロックアカウント) */
		LOGIN_FAIL_TMP_LOCK("失敗(一時ロックアカウント)"),
		/** ログイン失敗(永続ロックアカウント) */
		LOGIN_FAIL_LOCK("失敗(永続ロックアカウント)"),
		/** ログイン失敗(存在しないユーザ) */
		LOGIN_FAIL_USERID("失敗(存在しないユーザ)"),
		/** ロール変更 */
		LOGIN_ROLE_CHG("ロール変更"),
		/** アカウント一時ロック */
		ACCOUNT_TMP_LOCK("一時ロック"),
		/** アカウント一時ロック解除 */
		ACCOUNT_TMP_UNLOCK("一時ロック解除"),
		/** アカウント永続ロック */
		ACCOUNT_LOCK("永続ロック"),
		/** アカウント永続ロック解除 */
		ACCOUNT_UNLOCK("永続ロック解除"),
		/** アカウント追加 */
		ACCOUNT_ADD("ユーザー追加"),
		/** アカウント削除 */
		ACCOUNT_DELETE("ユーザー削除"),
		/** 自分のパスワード変更 */
		PASSCHG_OWN("ユーザー情報変更画面(本人)"),
		/** 自分のパスワード変更(強制) */
		PASSCHG_FORCE_OWN("強制パスワード変更画面"),
		/** 管理者のパスワード変更 */
		PASSCHG_ADMIN("ユーザー情報変更画面(管理者)"),
		;
		/** 表示用の値 */
		protected String value;
		/**
		 * コンストラクタ
		 * @param value 表示用の値
		 */
		private LogDetail(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * Proxy経由での接続元IPアドレスが設定されたヘッダ名。
	 */
	protected static final String HEADER_NAME_XFORWARDED = "X-Forwarded-For";
	/**
	 * Proxy経由での接続元IPアドレスの最大長
	 */
	protected static final int HEADER_XFORWARDED_SIZE = 255;
	
	/** リクエスト情報 */
	@Getter @Setter
	HttpServletRequest request;
	/** 接続元IP */
	@Getter @Setter
	String ip;
	/** 接続元IP(X-Forwarded-For) */
	@Getter @Setter
	String ipx;
	

	/**
	 * セキュリティログを追加します。
	 * 
	 * @param user ログインユーザー情報(未ログイン時null)
	 * @param target 操作対象
	 * @param type ログ種別
	 * @param detail ログ詳細
	 */
	public void insertLog(User user, String target, LogType type, LogDetail detail) {
		insertLog((user != null ? user.getLoginUserId() : ""), (user != null ? user.getGyoumuRoleId() : ""), target, type, detail);
	}

	/**
	 * セキュリティログを追加します。
	 * 
	 * @param loginUserId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @param target 操作対象
	 * @param type ログ種別
	 * @param detail ログ詳細
	 */
	public void insertLog(String loginUserId, String gyoumuRoleId, String target, LogType type, LogDetail detail) {
		if(request == null) {
			request =  ServletActionContext.getRequest();
		}
		insertLog(loginUserId, gyoumuRoleId, request, target, type.toString(), detail.toString());
	}

	/**
	 * セキュリティログを追加します。
	 * 
	 * @param loginUserId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @param request HttpRequest
	 * @param target 操作対象
	 * @param logType ログ種別
	 * @param logDetail ログ詳細
	 * @return インクリメントされたシリアル番号
	 */
	protected long insertLog(String loginUserId, String gyoumuRoleId, HttpServletRequest request, String target, String logType, String logDetail) {
		if(ip == null) {
			ip = request.getRemoteAddr();
		}
		if(ipx == null) {
			ipx = request.getHeader(HEADER_NAME_XFORWARDED);
		}
		if (null != ipx && HEADER_XFORWARDED_SIZE < ipx.length()) {
			ipx = ipx.substring(0, HEADER_XFORWARDED_SIZE);
		}
		return insertLog(
				ip,
				ipx,
				loginUserId,
				gyoumuRoleId,
				target != null ? target : "",
				logType,
				logDetail
		);
	}

	/**
	 * セキュリティログを追加します。
	 * 
	 * @param ip 接続元IP
	 * @param ipXforwarded 接続元IP(X-Forwarded-For)
	 * @param userId ログインユーザーのユーザーID(未ログインならブランク)
	 * @param gyoumuRoleId 業務ロールID
	 * @param target 操作対象
	 * @param logType ログ種別
	 * @param logDetail ログ詳細
	 * @return インクリメントされたシリアル番号
	 */
	protected long insertLog(String ip, String ipXforwarded, String userId, String gyoumuRoleId, String target, String logType, String logDetail) {
		final String sql = ""
				+  "INSERT INTO security_log ( "
				+  "	event_time, "
				+  "	ip, "
				+  "	ip_xforwarded, "
				+  "	user_id, "
				+  "	gyoumu_role_id, "
				+  "	target, "
				+  "	type, "
				+  "	detail "
				+  ") values ( "
				+  "	current_timestamp, "
				+  "	?, "
				+  "	?, "
				+  "	?, "
				+  "	?, "
				+  "	?, "
				+  "	?, "
				+  "	? "
				+  ") RETURNING serial_no;";
		GMap ret = connection.find(sql, ip, ipXforwarded, userId, gyoumuRoleId, target, logType, logDetail);
		return (long)ret.get("serial_no");
	}
}
