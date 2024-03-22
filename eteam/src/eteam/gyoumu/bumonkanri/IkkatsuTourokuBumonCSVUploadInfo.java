package eteam.gyoumu.bumonkanri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonCSVUploadInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	/** 部門コード */
	String bumonCd;
	/** 部門名 */
	String bumonName;
	/** 親部門コード */
	String oyaBumonCd;
	/** 有効期限From */
	String yuukouKigenFrom;
	/** 有効期限To */
	String yuukouKigenTo;
	/** セキュリティパターン */
	String securityPattern;
	/** エラーフラグ */
	boolean errorFlg;
}