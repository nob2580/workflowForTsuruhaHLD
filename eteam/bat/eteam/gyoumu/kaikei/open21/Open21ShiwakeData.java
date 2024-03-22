package eteam.gyoumu.kaikei.open21;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Open21のImp_Siwake_Mを呼び出し、仕訳のインポートを行う。
 */
@Getter @Setter @ToString
public class Open21ShiwakeData {
	/** 会社コード */
	String CCOD;
	/** 処理区分 */
	int PrcFlg;
	/** 伝票形式 */
	int DFUK;
	/** 不良データログ */
	int LogFlg;
	/** ログ用パス */
	String LogPath;
	/** ログファイル名 */
	String LogFname;
	/** レイアウトNo */
	int Rno;
	/** 起動ユーザー */
	int RUCOD;
	/** 仕訳区分 */
	int SKUBUN;
	/** 伝票入力パターン */
	int IJPT;
	/** 邦貨換算フラグ */
	int Kanzan;
	/** 入力確定日 */
	String Kakutei;
	/** 税率の扱い */
	int Keigen;

	/** メモリークラス */
	List<Open21CSetData> Siwake;
	/** e文書情報 */
	List<Open21LinkData> link;
}
