package eteam.gyoumu.kanitodoke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ユーザー定義届書画面情報
 */
public class KaniTodokeGamenInfo {

	/** レイアウト */
	protected String[][] layout = new String[0][0];
	
	/** オプション  */
	protected String[][] option = new String[0][0];
	
	/** データ値１ */
	protected Map<Integer, Map<String, String>> value1 = new HashMap<Integer, Map<String, String>>();
	
	/** データ値２ */
	protected Map<Integer, Map<String, String>> value2 = new HashMap<Integer, Map<String, String>>();
	
	/** 項目名 */
	protected String[] itemNames = new String[0];
	
	/** レイアウト */
	protected Map<String, String[]> itemLayout = new HashMap<String, String[]>();
	
	/** オプション  */
	protected Map<String, List<String[]>> itemOption = new HashMap<String, List<String[]>>();
	
	/**
	 * @return オプション
	 */
	public String[][] getOption() {
		return option;
	}
	/**
	 * @param Option オプション
	 */
	public void setOption(String[][] Option) {
		this.option = Option;
	}
	/**
	 * @return データ値１
	 */
	public Map<Integer, Map<String, String>> getValue1() {
		return value1;
	}
	/**
	 * @param Value1 データ値１
	 */
	public void setValue1(Map<Integer, Map<String, String>> Value1) {
		this.value1 = Value1;
	}
	/**
	 * @return データ値２
	 */
	public Map<Integer, Map<String, String>> getValue2() {
		return value2;
	}
	/**
	 * @param Value2 データ値２
	 */
	public void setValue2(Map<Integer, Map<String, String>> Value2) {
		this.value2 = Value2;
	}
	/**
	 * @return 項目名
	 */
	public String[] getItemNames() {
		return itemNames;
	}
	/**
	 * @param itemNames 項目名
	 */
	public void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}
	/**
	 * @return レイアウト
	 */
	public String[][] getLayout() {
		return layout;
	}
	/**
	 * @param Layout レイアウト
	 */
	public void setLayout(String[][] Layout) {
		this.layout = Layout;
	}
	/**
	 * @return 項目別レイアウト
	 */
	public Map<String, String[]> getItemLayout() {
		return itemLayout;
	}
	/**
	 * @param itemLayout 項目別レイアウト
	 */
	public void setItemLayout(Map<String, String[]> itemLayout) {
		this.itemLayout = itemLayout;
	}
	
	/**
	 * @return 項目別オプション
	 */
	public Map<String, List<String[]>> getItemOption() {
		return itemOption;
	}
	/**
	 * @param itemOption 項目別オプション
	 */
	public void setItemOption(Map<String, List<String[]>> itemOption) {
		this.itemOption = itemOption;
	}
	
	/**
	 * @return 項目数
	 */
	public Integer getItemCount() {
		return itemNames.length;
	}
	
	/**
	 * ソート済み項目名リストを取得する
	 * @param mapValue 値マップ
	 * @return ソート済み項目名リスト
	 */
	public List<String> getItemSortList(Map<String, String> mapValue) {
	
		List<String> listSort = new ArrayList<String>();

		for (String item_name : mapValue.keySet()) {
			listSort.add(item_name);
		}

		Collections.sort(listSort);
		
		return listSort;
	}
	
	/**
	 * ソート済み項目名リストを取得する
	 * @param value1Tmp 値１マップ
	 * @return ソート済み項目名リスト
	 */
	public Integer[] getSortDenpyouEdano(Map<Integer, Map<String, String>> value1Tmp) {
	
		Integer[] keys = value1Tmp.keySet().toArray(new Integer[0]);
		
		Arrays.sort(keys);
		
		return keys;
	}
}
