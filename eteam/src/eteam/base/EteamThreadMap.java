package eteam.base;

import java.util.HashMap;
import java.util.Map;

/**
 * スレッド内でのデータ共有：オンラインバッチ想定
 */
public class EteamThreadMap{

	/**
	 * スレッドローカル
	 */
    protected static ThreadLocal<Map<String, String>> tl = new ThreadLocal<Map<String, String>>() {
        protected synchronized Map<String, String> initialValue() {
            return new HashMap<String, String>();
        }
    };

    /**
     * スレッドローカルなマップを返す。
     * @return スレッドローカルなマップ
     */
    public static Map<String, String> get() {
        return tl.get();
    }
}
