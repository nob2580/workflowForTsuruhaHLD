package eteam.base.exception;

/**
 * (URL自体は正しいが)メソッド名やパラメータが不正（本来ありえない）
 */
public class EteamIllegalRequestException extends RuntimeException {

	/**
	 * コンストラクタ
	 */
	public EteamIllegalRequestException() {
		super();
	}

	/**
	 * コンストラクタ
	 * @param msg エラーメッセージ
	 */
	public EteamIllegalRequestException(String msg) {
		super(msg);
	}

	/**
	 * コンストラクタ
	 * @param msg エラーメッセージ
	 * @param e 例外
	 */
	public EteamIllegalRequestException(String msg, Throwable e) {
		super(msg, e);
	}
}
