package eteam.gyoumu.kaikei;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	
	/** 部門コード */
	String bumonCd;
	/** 伝票区分 */
	String denpyouKbn;
	/** 仕訳枝番号 */
	String shiwakeEdano;
	
	/** エラーフラグ */
	boolean errorFlg;
}