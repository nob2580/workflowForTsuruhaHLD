package eteam.gyoumu.houjincard;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class HoujinCardCSVUploadInfoBase implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	//TODO 使用項目検討
	
	/** csv内のnumbering */
	String number;
	
	/** カード会社 */
	String cardShubetsu;
	
	/** 部署コード */
	String bushoCd;
	/** 社員No */
	String shainNo;
	/** 使用者 */
	String shiyousha;
	/** 利用日 */
	String riyoubi;
	/** 金額 */
	String kingaku;
	/** カード番号 */
	String cardBangou;
	/** 加盟店 */
	String kameiten;
	/** 業種コード */
	String gyoushuCd;
	
	/** エラーフラグ */
	boolean errorFlg;
	/** 未取込フラグ */
	boolean mitorikomiFlg;
	/** 重複フラグ */
	boolean duplicateFlg;
	
}