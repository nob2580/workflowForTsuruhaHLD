package eteam.base.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 表示対象のデータに対するアクセス権限がない
 */
public class EteamAccessDeniedException extends RuntimeException {
	/** ダイアログ表示 */
	@Getter @Setter
	boolean dialog = false;
	
	/**
	 * new
	 */
	public EteamAccessDeniedException() {
		super();
	}
	
	/**
	 * new
	 * @param message メッセージ
	 */
	public EteamAccessDeniedException(String message) {
		super(message);
	}
	
	/**
	 * new
	 * @param message メッセージ
	 * @param dialog ダイアログ表示とする
	 */
	public EteamAccessDeniedException(String message, boolean dialog) {
		super(message);
		this.dialog = dialog;
	}
}
