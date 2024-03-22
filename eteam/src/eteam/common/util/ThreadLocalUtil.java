package eteam.common.util;

import eteam.base.GMap;

/**
 * スレッドローカルな箱を返す
 */
public class ThreadLocalUtil {

	/** スレッド単位 */
    private static ThreadLocal<GMap> threadLocalHolder = new ThreadLocal<>() {
        @Override
        protected GMap initialValue(){
            return new GMap();
        }
    };
	
	/**
	 * 箱をゲット
	 * @param key キー
	 * @return 箱
	 */
	public static GMap get(String key) {
		GMap map = null;
		//画面 TODO:test
		if (ServletUtil.getRequest() != null) {
			map = (GMap)ServletUtil.getRequest().getAttribute(key);
			if (map == null) {
				map = new GMap();
				ServletUtil.getRequest().setAttribute(key, map);
			}
		//バッチ TODO:test
		} else {
			GMap threadMap = threadLocalHolder.get();
			map = threadMap.get(key);
			if (map == null) {
				map = new GMap();
				threadMap.put(key, map);
			}
		}
		return map;
	}
}
