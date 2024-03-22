package eteam.gyoumu.tsuuchi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧財務拠点テーブルの項目マッピング
 */
@Getter @Setter @ToString
public class DenpyouIchiranDataZK {

	/** WF共通部分 */
	DenpyouIchiranDataWFCom com = new DenpyouIchiranDataWFCom();
	/** 財務拠点入力パターンNo */
	String zaimuKyotenNyuryokuPatternNo = "";
}
