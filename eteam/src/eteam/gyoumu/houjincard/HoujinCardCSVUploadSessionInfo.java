package eteam.gyoumu.houjincard;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 法人カード使用履歴CSVのセッション全部 */
@Getter @Setter @ToString
public class HoujinCardCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名 */
	String fileName;
	/** カード種別 */
	String cardShubetsu;
	/** エラーリスト */
	List<String> errorList;
	/** 法人カード使用履歴リスト ※登録用 */
	List<HoujinCardCSVUploadInfoBase> rirekiList;
	
	/** CSVファイル情報（法人カード使用履歴）表示用ヘッダリスト */
	List<String> hyoujiHeaderList;
	/** CSVファイル情報（法人カード使用履歴）表示用データリスト */
	List<String[]> hyoujiDataList;
	
	/**
	 * コンストラクタ
	 * @param fileName ファイル名
	 * @param rirekiList カード使用履歴リスト
	 * @param errorList エラーメッセージリスト
	 * @param cardShubetsu カード種別
	 * @param hyoujiHeaderList 表示用ヘッダリスト
	 * @param hyoujiDataList 表示用データリスト
	 */
	public HoujinCardCSVUploadSessionInfo(String fileName, List<HoujinCardCSVUploadInfoBase> rirekiList,  List<String> errorList, String cardShubetsu,  List<String> hyoujiHeaderList,  List<String[]> hyoujiDataList) {
		this.fileName = fileName;
		this.rirekiList = rirekiList;
		this.errorList = errorList;
		this.cardShubetsu = cardShubetsu;
		this.hyoujiHeaderList = hyoujiHeaderList;
		this.hyoujiDataList = hyoujiDataList;
		
	}
}