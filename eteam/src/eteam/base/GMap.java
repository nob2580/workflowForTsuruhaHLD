package eteam.base;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 拡張LinkedHashMap
 * Key判定がCaseInsensitive
 * Get時に以下の型に自動キャスト
 *  
 * postgres →	Java
 * 
 * character →	java.lang.String
 * integer →	java.lang.Integer
 * bigint →	java.lang.Long
 * numeric →	java.math.BigDecimal
 * date →	java.sql.Date (java.util.Dateに代入可） 
 * timestamp →	java.sql.Timestamp (java.util.Dateに代入可） 
 * integer[] →	org.postgresql.jdbc4.Jdbc4Array
 * bytea →	Byte[]
 * 
 */
public class GMap extends LinkedHashMap<String, Object> {

	/**
     * The internal mapping from lowercase keys to the real keys.
     *
     * <p>
     * Any query operation using the key
     * ({@link #get(Object)}, {@link #containsKey(Object)})
     * is done in three steps:
     * <ul>
     * <li>convert the parameter key to lower case</li>
     * <li>get the actual key that corresponds to the lower case key</li>
     * <li>query the map with the actual key</li>
     * </ul>
     * </p>
     * 
     * commons-dbutils-1.6.jar BasicRowProcessor.CaseInsensitiveHashMapから流用
     */
    protected final Map<String, String> lowerCaseMap = new HashMap<String, String>();

    /**
     * commons-dbutils-1.6.jar BasicRowProcessor.CaseInsensitiveHashMapから流用
     */
    @Override
    public boolean containsKey(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.containsKey(realKey);
    }

    /**
     * commons-dbutils-1.6.jar BasicRowProcessor.CaseInsensitiveHashMapから流用
     */
    @Override
    public Object get(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.get(realKey);
    }

    /**
     * commons-dbutils-1.6.jar BasicRowProcessor.CaseInsensitiveHashMapから流用
     */
    @Override
    public Object put(String key, Object value) {

        Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
        super.remove(oldKey);
        return super.put(key, value);
    }

    /**
     * commons-dbutils-1.6.jar BasicRowProcessor.CaseInsensitiveHashMapから流用
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {
        for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            this.put(key, value);
        }
    }

    /**
     * commons-dbutils-1.6.jar BasicRowProcessor.CaseInsensitiveHashMapから流用
     */
    @Override
    public Object remove(Object key) {
        Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
        return super.remove(realKey);
    }

    /**
	 * コンストラクタ
	 */
    public GMap() {
    	super();
    }
    
	/**
	 * コンストラクタ
	 * @param size initialCapacity
	 */
	public GMap(int size){
		super(size);
	}

	/**
	 * keyに対する値を返す
	 * @param key  キー
	 * @return 値 ※型はクラスコメントを参照	<br>
	 * character →	java.lang.String <br>
	 * integer →	java.lang.Integer <br>
	 * bigint →	java.lang.Long <br>
	 * numeric →	java.math.BigDecimal <br>
	 * date →	java.sql.Date (java.util.Dateに代入可） 	<br>
	 * timestamp →	java.sql.Timestamp (java.util.Dateに代入可） 	<br>
	 * 
	 */
	public <T> T get(String key) {
		Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
		return (T) super.get(realKey);
	}

	/**
	 * keyに対する値をObjectで返す
	 * @param key        キー
	 * @return                値
	 */
	public Object getObject(String key) {
		Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
		return super.get(realKey);
	}
    
	/**
	 * keyに対する値をStringで返す
	 * @param key        キー
	 * @return                値
	 */
	public String getString(String key) {
		Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
		Object tmp = super.get(realKey);
		return (tmp == null) ? "null" : tmp.toString(); //TODO: "null"はおかしい・・・どうしよこれ
	}
	
	@Override
	public boolean isEmpty() {
		return this.lowerCaseMap.isEmpty();
	}
	
	/**
	 * 毎回mapとキーの両方のnullチェックするのがうっとうしいので集約
	 * @param map マップ
	 * @param key キー
	 * @return nullか
	 */
	public static boolean isNull(GMap map, String key) {
		return map == null || map.get(key) == null;
	}
	
	/**
	 * 毎回mapとキーの両方のnullチェックするのがうっとうしいので集約。isNullの逆。
	 * @param map マップ
	 * @param key キー
	 * @return nullでないか
	 */
	public static boolean isNotNull(GMap map, String key)
	{
		return !isNull(map, key);
	}
}

