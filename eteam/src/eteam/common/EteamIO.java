package eteam.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

/**
 * IO処理
 */
public class EteamIO {

	/**
	 * ファイル書込
	 * @param data データ
	 * @param path ファイルパス
	 */
	public static void writeFile(byte[] data, String path) {
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(path);
			IOUtils.write(data, fo);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(fo != null) {
				try {
					fo.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * ファイル読込
	 * @param path ファイルパス
	 * @return 読込データ
	 */
	public static byte[] readFile(String path) {
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(path);
			return IOUtils.toByteArray(fi);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if(fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	/**
	 * ファイル削除
	 * @param path ファイルパス
	 */
	public static void deleteFile(String path) {
		new File(path).delete();
	}

	/**
	 * ストリーム読込
	 * @param is 入力ストリーム
	 * @return 読込データ
	 */
	public static byte[] read(InputStream is) {
		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** ファイル拡張子の取得
	 * @param fileName ファイル名
	 * @return 拡張子
	 */
	public static String getBase(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point == -1) {
	    	return fileName;
	    }
        return fileName.substring(0, point);
	}

	/** ファイル拡張子の取得
	 * @param fileName ファイル名
	 * @return 拡張子
	 */
	public static String getExtension(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point == -1 || point == fileName.length() - 1) {
	    	return "";
	    }
        return fileName.substring(point + 1);
	}
	
	/**
	 * テキスト化
	 * @param reader 読み
	 * @return テキスト
	 * @throws IOException エラー
	 */
	public static String read(Reader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		char[] buffer = new char[512];
		int read;
		while (0 <= (read = reader.read(buffer))) {
			builder.append(buffer, 0, read);
		}
		return builder.toString();
	}
}
