package eteam.base.exception;

/**
 * 仕訳エラー
 */
public class EteamShiwakeErrorException extends RuntimeException {
	
	/**
	 * コンストラクタ
	 */
	public EteamShiwakeErrorException() {
	}

	/**
	 * @param paramString 根本エラー
	 */
	public EteamShiwakeErrorException(String paramString) {
		super(paramString);
	}
}
