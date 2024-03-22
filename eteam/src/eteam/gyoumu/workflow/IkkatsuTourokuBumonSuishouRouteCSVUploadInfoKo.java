package eteam.gyoumu.workflow;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（登録内容） */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** csv内のnumbering */
	String number;
	
	/** 伝票区分 */
	String denpyouKbn;
	/** 部門コード */
	String bumonCd;
	/** 枝番号 */
	String edaNo;
	/** 枝々番号 */
	String edaedaNo;
	/** 部門ロールID */
	String bumonRoleId;
	/** 承認処理権限番号 */
	String shouninhoriKengenNo;
	/** 合議パターン番号 */
	String gougiPatternNo;
	/** 合議枝番 */
	String gougiEdano;
	
	
	/** エラーフラグ */
	boolean errorFlg;
}