package eteam.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * シリアライズ関連
 */
public class SerializeUtil {
	
	/**
	 * シリアライズ
	 * @param obj オブジェクト
	 * @return シリアライズしたデータ
	 */
	public static byte[] serialize(Serializable obj) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream (b);
			out.writeObject(obj);
			out.close();
			return b.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
	 * デシリアライズ
	 * @param data シリアライズしたデータ
	 * @return オブジェクト
	 */
	public static <T> T deserialize(byte[] data) {
		try {
			ByteArrayInputStream b = new ByteArrayInputStream(data);
			ObjectInputStream in;
			in = new ObjectInputStream(b);
			Object obj = in.readObject(); 
			return (T)obj; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
