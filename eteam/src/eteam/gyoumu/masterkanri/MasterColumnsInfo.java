package eteam.gyoumu.masterkanri;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import eteam.base.GMap;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamConst;
import lombok.Delegate;
import lombok.Getter;

/**
 * マスタの列情報全部。
 *
 */
public class MasterColumnsInfo {

	/** マスターID */
	@Getter
	protected String masterId;

	/** PKリスト */
	@Getter
	protected List<String> pkList;

	/** マスターカラムリスト */
	@Getter @Delegate
	protected LinkedHashMap<String, MasterColumnInfo> columnMap;
	
	/**
	 * 名前の配列と値の配列からMapを生成します。
	 * @param names 名前のリスト
	 * @param values 値のリスト
	 * @return マップ
	 */
	public static GMap convertNameValueMap(String[] names, String[] values) {
		if (names == null || values == null || names.length != values.length) {
			throw new EteamIllegalRequestException("項目と値の数が一致しません。");
		}
		GMap map = new GMap(names.length);
		for (int ii = 0; ii < names.length; ii++) {
			map.put(names[ii].toLowerCase(), values[ii]);
		}
		return map;
	}

	/**
	 * コンストラクタ
	 * @param _masterId マスターID
	 * @param _pkList PKリスト
	 * @param _columnMap カラムマップ
	 */
	public MasterColumnsInfo(String _masterId, List<String> _pkList, LinkedHashMap<String, MasterColumnInfo> _columnMap) {
		masterId = _masterId;
		pkList = _pkList;
		columnMap = _columnMap;
	}

	/**
	 * CSVヘッダを生成します。
	 * @return １行目～４行目
	 */
	public List<String[]> createCsvDisplayNamesLine() {
		List<String[]> ret = new ArrayList<String[]>(4);
		String[] row1 = new String[getColumnList().size()];
		String[] row2 = new String[getColumnList().size()];
		String[] row3 = new String[getColumnList().size()];
		String[] row4 = new String[getColumnList().size()];
		int ii = 0;
		for (MasterColumnInfo info : getColumnList()) {
			row1[ii] = info.getDisplayName();
			row2[ii] = info.getName();
			row3[ii] = info.getMaxLength() == null ? info.getCsvTypeName() : info.getCsvTypeName() +"(" +info.getMaxLength()+")" ;
			row4[ii] = info.isPk() ? "1" : (info.isNullable() ? "" : "2");
			ii++;
		}
		ret.add(row1);
		ret.add(row2);
		ret.add(row3);
		ret.add(row4);

		return ret;
	}

	/**
	 * CSVデータのバイト列を取得します。
	 * @param dataList 元データ
	 * @return バイト配列
	 */
	public byte[] createCsvByteData(List<String[]> dataList) {
		StringBuilder buf = new StringBuilder();
		for (String[] strary : dataList) {
			int ii = 0;
			for (String str : strary) {
				if (ii > 0) {
					buf.append(',');
				}
				buf.append(str);
				ii++;
			}
			buf.append("\r\n");
		}
		byte [] data = null;
		try {
			data = buf.toString().getBytes(EteamConst.Encoding.MS932);
		}
		catch (UnsupportedEncodingException e) {
			throw new EteamIllegalRequestException();
		}
		return data;
	}

	/**
	 * 行情報からCSV列を生成します。
	 * @param colMap 行情報
	 * @return CSV列
	 */
	public String[] createCsvLine(GMap colMap) {
		String[] row = new String[getColumnList().size()];
		int ii = 0;
		for (MasterColumnInfo info : getColumnList()) {
			row[ii] = colMap.get(info.getName()).toString().replaceAll(",", ";;;");
			ii++;
		}

		return row;
	}

	/**
	 * PKを表す文字列を生成します。
	 * @param value 行を表す配列
	 * @return PK文字列
	 */
	public String createPkString(String value[]) {
		// インデックスの生成
		StringBuffer buf = new StringBuffer();
		for (String pkcol : pkList) {
			MasterColumnInfo colInfo = columnMap.get(pkcol.toLowerCase());
			buf.append("!").append(colInfo.getNormalizeString(value[colInfo.getIndex()]).replaceAll("!", "!!")).append("!,");
		}
		return buf.toString();
	}

	/**
	 * PKを表す文字列を生成します。
	 * @param map 行を表すマップ
	 * @return PK文字列
	 */
	public String createPkString(GMap map) {
		// インデックスの生成
		StringBuffer buf = new StringBuffer();
		for (String pkcol : pkList) {
			MasterColumnInfo colInfo = columnMap.get(pkcol.toLowerCase());
			buf.append("!").append(colInfo.convertObjectToString(map.get(colInfo.getName())).replaceAll("!", "!!")).append("!,");
		}
		return buf.toString();
	}

	/**
	 * 入力チェック
	 * @param map 入力
	 * @return 結果
	 */
	public List<String> checkInput(GMap map) {
		List<String> errorList = new ArrayList<String>();
		for (MasterColumnInfo info : columnMap.values()) {
			if (!map.containsKey(info.getName())) {
				throw new EteamIllegalRequestException("入力列情報が不足しています。");
			}
			String val = map.get(info.getName());
			String errMsg = info.checkInput(val);
			if (null != errMsg) {
				errorList.add(errMsg);
			}
		}
		
		return errorList;
	}

	/**
	 * カラムのリストを取得する。
	 * @return リスト
	 */
	public Collection<MasterColumnInfo> getColumnList() {
		return columnMap.values();
	}
}
