package eteam.gyoumu.kaikei.open21;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Open21のImp_Siwake_Mを呼び出し、仕訳のインポートを行う。
 */
@Getter @Setter @ToString
public class Open21LinkData {
	/** リンク番号 */
	int linkNo;
	/** リンク名 */
	String linkName;
	/** ファイル名 */
	String fileName;
	
	/** 2:通常ファイル、3:e文書 */
	String type;
	
	//以下e文書のみ必要
	/** e文書番号 */
	String ebunshoNo = "";
	/** 申請者名称 */
	String shinseisha = "";
	/** 最終承認者名称 */
	String shouninsha = "";
	/** 書類種別 */
	String shubetsu = "";
	/** 書類日付 */
	String date = "";
	/** 書類金額 */
	String kingaku = "";
	/** 書類発行者名称 */
	String hakkousha = "";
	/** 書類品名 */
	String hinmei = "";
	
	/**
	 * new
	 * @param linkNo リンク番号
	 * @param linkName リンク名称
	 * @param fileName ファイル名
	 * @param ebunshoNo e文書番号
	 * @param denshitorihikiFlg 電子取引フラグ
	 * @param tsfuyoFlg タイムスタンプ付与フラグ
	 * @param shinseisha 申請者名称
	 * @param shouninsha 最終承認者名称
	 * @param shubetsu 書類種別
	 * @param date 書類日付
	 * @param kingaku 書類金額
	 * @param hakkousha 書類発行者名称
	 * @param hinmei 書類品名
	 */
	public Open21LinkData(int linkNo, String linkName, String fileName, String ebunshoNo,  String denshitorihikiFlg, String tsfuyoFlg, String shinseisha, String shouninsha, String shubetsu, String date, String kingaku, String hakkousha, String hinmei) {
		this(linkNo, linkName, fileName);
		
		if(!("1".equals(denshitorihikiFlg))) {
			//e文書(スキャナ)
			//電子取引オフ(TS付与オフ)
			setType("3");
		}else if(!("1".equals(tsfuyoFlg))) {
			//e文書(電子取引・タイムスタンプ無し)
			//電子取引オン・TS付与オフ
			setType("5");
		}else {
			//e文書(電子取引・タイムスタンプ有り)
			//電子取引オン・TS付与オン
			setType("4");
		}
		
		
		setEbunshoNo(ebunshoNo);
		setShinseisha(shinseisha);
		setShouninsha(shouninsha);
		setShubetsu(shubetsu);
		setDate(date);
		setKingaku(kingaku);
		setHakkousha(hakkousha);
		setHinmei(hinmei);
	}

	/**
	 * new
	 * @param linkNo リンク番号
	 * @param linkName リンク名称
	 * @param fileName ファイル名
	 */
	public Open21LinkData(int linkNo, String linkName, String fileName) {
		setLinkNo(linkNo);
		setLinkName(linkName);
		setFileName(fileName);
		
		setType("2");
	}
}
