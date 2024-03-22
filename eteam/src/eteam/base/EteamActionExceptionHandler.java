package eteam.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * アクションクラスで例外が発生した時にCHAINするリアクション。
 * ログのダンプが役割。
 */
@Getter @Setter @ToString
public class EteamActionExceptionHandler {

	/** ロガー */
	EteamLogger log = EteamLogger.getLogger(EteamActionExceptionHandler.class);

	/** 投げられた例外が入る。エラー画面にも引き継ぐ。 */
    protected Throwable exception;

    /**
     * CHAINされる処理
     * @return 特に意味は内が"success"を返す。実際はstruts.xmlで例外型別のエラー画面にハンドリングしている。例外としてメモリ不足の場合は"outOfMemoryError"を返す。
     */
    public String execute() {
		log.error("アクションクラスで例外発生。", exception);
		if(exception != null && exception.getCause() != null && exception.getCause() instanceof OutOfMemoryError){
			return "outOfMemoryError";
		}
		return "success";
    }
}
