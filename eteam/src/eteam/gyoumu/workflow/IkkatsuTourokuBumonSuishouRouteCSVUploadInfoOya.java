package eteam.gyoumu.workflow;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	
	/** 伝票区分 */
	String denpyouKbn;
	/** デフォルト設定フラグ */
	String defaultFlg;
	/** 仕訳枝番号 */
	String shiwakeEdaNo[];
	/** 仕訳枝番号表示用 */
	String shiwakeEdaNoHyouji;
	/** 部門コード */
	String bumonCd;
	/** 枝番号 */
	String edaNo;
	/** 金額開始 */
	String kingakuFrom;
	/** 金額終了 */
	String kingakuTo;
	/** 有効期限From */
	String yuukouKigenFrom;
	/** 有効期限To */
	String yuukouKigenTo;
	
	/** エラーフラグ */
	boolean errorFlg;
}