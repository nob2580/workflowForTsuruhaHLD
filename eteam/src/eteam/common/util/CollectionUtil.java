package eteam.common.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import eteam.base.GMap;

/**
 * コレクション系の処理まとめておく（業務ロジック途中でこういうのが間に入ると見通し悪いので）
 */
public class CollectionUtil {

	/**
	 * List<GMap>から配列　※nullはブランクに、String以外はtoString()を書ける
	 * @param list リスト
	 * @param key キー
	 * @return 配列
	 */
	public static String[] toArrayStr(List<GMap> list, String key) {
		String[] ret = new String[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i).get(key) == null ? "" : list.get(i).get(key).toString();
		}
		return ret;
	}
	/**
	 * List<GMap>から配列　※nullはブランクに、String以外は指定Functionによるフォーマット
	 * @param list リスト
	 * @param key キー
	 * @param f 変換オブジェクト
	 * @return 配列
	 */
	public static String[] toArrayStr(List<GMap> list, String key, Function<? extends Object, String> f) {
		String[] ret = new String[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = f.apply(list.get(i).get(key));
		}
		return ret;
	}
	/**
	 * List<GMap>から配列
	 * @param list リスト
	 * @param key キー
	 * @return 配列
	 */
	public static Integer[] toArrayInt(List<GMap> list, String key) {
		var ret = new Integer[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i).get(key);
		}
		return ret;
	}
	/**
	 * List<GMap>から配列
	 * @param list リスト
	 * @param key キー
	 * @return 配列
	 */
	public static BigDecimal[] toArrayDec(List<GMap> list, String key) {
		BigDecimal[] ret = new BigDecimal[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i).get(key);
		}
		return ret;
	}
	/**
	 * List<GMap>から配列
	 * @param list リスト
	 * @param key キー
	 * @return 配列
	 */
	public static Date[] toArrayDate(List<GMap> list, String key) {
		Date[] ret = new Date[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i).get(key);
		}
		return ret;
	}

	/**
	 * 固定値だけの配列を作る
	 * @param val 固定値
	 * @param size キー
	 * @return 配列
	 */
	public static String[] makeArrayStr(String val, int size) {
		String[] ret = new String[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = val;
		}
		return ret;
	}
	/**
	 * 固定値だけの配列を作る
	 * @param val 固定値
	 * @param size キー
	 * @return 配列
	 */
	public static Integer[] makeArrayInt(Integer val, int size) {
		Integer[] ret = new Integer[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = val;
		}
		return ret;
	}
	/**
	 * 固定値だけの配列を作る
	 * @param val 固定値
	 * @param size キー
	 * @return 配列
	 */
	public static BigDecimal[] makeArrayDec(BigDecimal val, int size) {
		BigDecimal[] ret = new BigDecimal[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = val;
		}
		return ret;
	}
	/**
	 * 固定値だけの配列を作る
	 * @param val 固定値
	 * @param size キー
	 * @return 配列
	 */
	public static Date[] makeArrayDate(Date val, int size) {
		Date[] ret = new Date[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = val;
		}
		return ret;
	}

	/**
	  * List<Map>からMap<String, Map>へ
	  * @param list リスト<Map>
	  * @param key Mapのキー項目
	  * @return Map<String,Map>・・・param「key」が戻り値Mapのキーになる
	  */
	 public static Map<String, GMap> list2Map(List<GMap> list, String key){
		 Map<String, GMap> ret = new HashMap<>();
		 for(GMap map : list){
			 ret.put((String)map.get(key), map);
		 }
		 return ret;
	 }
	
	/**
	 * UT
	 * @param argv 未使用
	 */
	public static void main(String[] argv) {
		List<Object[]> data = Arrays.asList(
			new Object[]{"s", Integer.valueOf(1), BigDecimal.valueOf(2)},
			new Object[]{null, null, null}
		);
		List<GMap> list = new ArrayList<>();
		data.stream().forEach(d -> {
			GMap m = new GMap();
			m.put("s", d[0]);
			m.put("i", d[1]);
			m.put("b", d[2]);
			list.add(m);
		});

		String[] strList = toArrayStr(list, "s");
		for(Object o : strList) System.out.print("," + o);
		System.out.println();

		Integer[] intList = toArrayInt(list, "i");
		for(Object o : intList) System.out.print("," + o);
		System.out.println();

		BigDecimal[] decList = toArrayDec(list, "b");
		for(Object o : decList) System.out.print("," + o);
		System.out.println();
		
// String[] strList = makeArrayStr("0", 10);
// for(Object o : strList) System.out.print("," + o);
// System.out.println();
// 
// Integer[] intList = makeArrayInt(2, 10);
// for(Object o : intList) System.out.print("," + o);
// System.out.println();
// 
// BigDecimal[] decList = makeArrayDec(BigDecimal.ONE, 10);
// for(Object o : decList) System.out.print("," + o);
// System.out.println();
// 
// Date[] dateList = makeArrayDate(new Date(System.currentTimeMillis()), 10);
// for(Object o : dateList) System.out.print("," + o);
// System.out.println();
	}
}
