package eteam.gyoumu.userjouhoukanri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCSVUploadInfoKaishaKirikae implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	/** ユーザーID */
	String userId;
	/** スキーマコード */
	String schemeCd;
	/** 有効期限From */
	String yuukouKigenFrom;
	/** 有効期限To */
	String yuukouKigenTo;
	/** 表示順 */
	String hyoujiJun;
	
	/** エラーフラグ */
	boolean errorFlg;
}
