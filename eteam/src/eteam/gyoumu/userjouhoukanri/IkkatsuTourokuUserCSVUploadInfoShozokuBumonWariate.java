package eteam.gyoumu.userjouhoukanri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	/** 部門コード */
	String bumonCd;
	/** 部門ロールID */
	String bumonRoleId;
	/** ユーザーID */
	String userId;
	/** 代表負担部門コード */
	String daihyouFutanBumonCd;
	/** 有効期限From */
	String yuukouKigenFrom;
	/** 有効期限To */
	String yuukouKigenTo;
	/** 表示順 */
	String hyoujiJun;
	
	/** エラーフラグ */
	boolean errorFlg;
}