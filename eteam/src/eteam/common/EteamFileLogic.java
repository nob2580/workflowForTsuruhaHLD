package eteam.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import eteam.common.EteamConst.Encoding;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ファイル入出力共通
 */
@Getter @Setter @ToString
public class EteamFileLogic {

	/** &nbsp;のバイナリ値 */
	protected static final String NBSP = "\u00A0";

	/**
	 * CSVファイルを出力する。
	 * ダブルコートで各カラムを囲む。
	 * 
	 * @param out 出力
	 * @param records CSVの複数レコード
	 * @throws IOException IOエラー
	 */
	@Deprecated
	public static void outputCsv(OutputStream out, List<List<Object>> records) throws IOException { 
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out, Encoding.MS932);
			outputCsv(writer, records);
		} finally {
			if (null != writer) {
				writer.close();
			} else {
				out.close();
			}
		}
	}

	/**
	 * CSVファイルを出力する。
	 * ダブルコートで各カラムを囲む。
	 * 
	 * @param writer 出力
	 * @param records CSVの複数レコード
	 * @throws IOException IOエラー
	 */
	public static void outputCsv(Writer writer, List<List<Object>> records) throws IOException {
		for (int i = 0; i < records.size(); i++) {
			List<Object> record = records.get(i);
			outputCsvRecord(writer, record);
		}
	}

	/**
	 * CSVファイルを出力する。
	 * ダブルコートで各カラムを囲む。
	 * 
	 * @param writer 出力
	 * @param record CSVの1レコード
	 * @throws IOException IOエラー
	 */
	public static void outputCsvRecord(Writer writer, List<Object> record) throws IOException {
		for (int j = 0; j < record.size(); j++) {
			if (0 < j) writer.write(",");
			writer.write("\"");
			writer.write(formatColumn(record.get(j)));
			writer.write("\"");
		}
		writer.write("\r\n");
	}
	
	/**
	 * CSVファイルを出力する
	 * @param file ファイル
	 * @param text テキスト
	 */
	public static void makeCsvFile(File file, String text) {
		outCsvFile(file, text, false);
	}
	
	/**
	 * CSVファイルにtextを追記する
	 * @param file ファイル
	 * @param text テキスト
	 */
	public static void appendCsvFile(File file, String text) {
		outCsvFile(file, text, true);
	}
	
	/**
	 * CSVファイルにtextを追記する
	 * @param file ファイル
	 * @param text テキスト
	 * @param append 追記である
	 */
	public static void outCsvFile(File file, String text, boolean append) {
		OutputStream out = null;
		try {
			// &nbsp;を空白に置換
			String s = text.replace(NBSP, " ");
			out = new FileOutputStream(file, append);
			out.write(s.getBytes(Encoding.MS932));
		} catch (IOException e) {
			throw new RuntimeException("ファイル書き込みエラーが発生しました。", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException("ファイル書き込みエラーが発生しました。", e);
				}
			}
		}
	}

	/**
	 * CSV出力用のフォーマット処理
	 * @param o オブジェクト
	 * @return 変換用
	 */
	protected static String formatColumn(Object o) {
		if (null == o)
		{
			return "";
		}
		if (o instanceof String) {
			return ((String)o).replace(NBSP, " ").replace("\"", "\"\"").replace("\r", "").replace("\n", " ").replace("\t", " ");
		} else if (o instanceof Date) {
			return new SimpleDateFormat("yyyy/MM/dd").format((Date)o);
		} else if (o instanceof Timestamp) {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format((Timestamp)o);
		} else if (o instanceof BigDecimal) {
			return new DecimalFormat("#,###").format(o);
		} else if (o instanceof byte[]) {
			return "-";
		}
		return o.toString();
	}

	/**
	 * 指定ディレクトリ配下のサブディレクトリ一覧を返す。（ディレクトリ名ソート）
	 * @param path 指定ディレクトリ
	 * @return サブディレクトリ
	 */
	public static List<File> getDirs(String path) {

		// ディレクトリリストを作る
	    File[] children = new File(path).listFiles();
	    List<File> dirList = new ArrayList<>();
	    for (File f : children) {
	    	if (f.isDirectory()) dirList.add(f);
	    }
	    //ディレクトリ名でソート
	    Collections.sort(dirList);
	    return dirList;
	}
	
	/**
	 * ファイル名として使用できない文字 ¥/:*?"<>| を空白に置換
	 * @param fileName ファイル名
	 * @return 置換後の文字列
	 */
	public static String trimNGFileNamePattern(String fileName)
	{
		Pattern NGFileNamePattern = Pattern.compile("[(\\|/|:|\\*|?|\"|<|>|\\\\|)]");
		return NGFileNamePattern.matcher(fileName).replaceAll("");
	}
}
