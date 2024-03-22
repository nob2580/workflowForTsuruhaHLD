package eteam.gyoumu.kaikei.open21;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Open21のImp_Siwake_Mを呼び出し、仕訳のインポートを行う。
 */
@Getter @Setter @ToString
public class Open21CSetData {
	/** 伝票日付 */
	Date DYMD;
	/** 整理月フラグ */
	String SEIRI;
	/** 伝票番号 */
	String DCNO;
	/** 借方　部門コード */
	String RBMN;
	/** 借方　取引先コード */
	String RTOR;
	/** 借方　科目コード */
	String RKMK;
	/** 借方　枝番コード */
	String REDA;
	/** 借方　工事コード */
	String RKOJ;
	/** 借方　工種コード */
	String RKOS;
	/** 借方　プロジェクトコード */
	String RPRJ;
	/** 借方　セグメントコード */
	String RSEG;
	/** 借方　ユニバーサルフィールド１ */
	String RDM1;
	/** 借方　ユニバーサルフィールド２ */
	String RDM2;
	/** 借方　ユニバーサルフィールド３ */
	String RDM3;
	/** 摘要 */
	String TKY;
	/** 摘要コード */
	String TNO;
	/** 貸方　部門コード */
	String SBMN;
	/** 貸方　取引先コード */
	String STOR;
	/** 貸方　科目コード */
	String SKMK;
	/** 貸方　枝番コード */
	String SEDA;
	/** 貸方　工事コード */
	String SKOJ;
	/** 貸方　工種コード */
	String SKOS;
	/** 貸方　プロジェクトコード */
	String SPRJ;
	/** 貸方　セグメントコード */
	String SSEG;
	/** 貸方　ユニバーサルフィールド１ */
	String SDM1;
	/** 貸方　ユニバーサルフィールド２ */
	String SDM2;
	/** 貸方　ユニバーサルフィールド３ */
	String SDM3;
	/** 対価金額 */
	BigDecimal EXVL;
	/** 金額 */
	BigDecimal VALU;
	/** 消費税対象科目コード */
	String ZKMK;
	/** 消費税対象科目税率 */
	String ZRIT;
	/** 消費税対象科目　課税区分 */
	String ZZKB;
	/** 消費税対象科目　業種区分 */
	String ZGYO;
	/** 消費税対象科目　仕入区分 */
	String ZSRE;
	/** 消費税対象科目　軽減税率区分 */
	String ZKEIGEN;
	/** 借方　税率 */
	String RRIT;
	/** 借方　軽減税率区分*/
	String RKEIGEN;
	/** 貸方　税率 */
	String SRIT;
	/** 貸方　軽減税率区分*/
	String SKEIGEN;
	/** 借方　課税区分 */
	String RZKB;
	/** 借方　業種区分 */
	String RGYO;
	/** 借方　仕入区分 */
	String RSRE;
	/** 貸方　課税区分 */
	String SZKB;
	/** 貸方　業種区分 */
	String SGYO;
	/** 貸方　仕入区分 */
	String SSRE;
	/** 支払日 */
	Date SYMD;
	/** 支払区分 */
	String SKBN;
	/** 支払期日 */
	Date SKIZ;
	/** 回収日 */
	Date UYMD;
	/** 入金区分 */
	String UKBN;
	/** 回収期日 */
	Date UKIZ;
	/** 店券フラグ */
	String STEN;
	/** 消込コード */
	String DKEC;
	/** 起票年月日 */
	Date KYMD;
	/** 起票部門コード */
	String KBMN;
	/** 起票者コード */
	String KUSR;
	/** 入力者コード */
	String FUSR;
	/** 付箋番号 */
	String FSEN;
	/** 承認グループNo. */
	String SGNO;
	/** 分離区分 */
	String BUNRI;
	/** レート */
	String RATE;
	/** 外貨対価金額 */
	String GEXVL;
	/** 外貨金額 */
	String GVALU;
	/** 行区切り */
	String GSEP;
	
	
	// 以下、SIAS向け追加フィールド
	
	/** ヘッダーフィールド１ */
	String HF1;
	/** ヘッダーフィールド２ */
	String HF2;
	/** ヘッダーフィールド３ */
	String HF3;
	/** ヘッダーフィールド４ */
	String HF4;
	/** ヘッダーフィールド５ */
	String HF5;
	/** ヘッダーフィールド６ */
	String HF6;
	/** ヘッダーフィールド７ */
	String HF7;
	/** ヘッダーフィールド８ */
	String HF8;
	/** ヘッダーフィールド９ */
	String HF9;
	/** ヘッダーフィールド１０ */
	String HF10;
	
	/** 借方　ユニバーサルフィールド４ */
	String RDM4;
	/** 借方　ユニバーサルフィールド５ */
	String RDM5;
	/** 借方　ユニバーサルフィールド６ */
	String RDM6;
	/** 借方　ユニバーサルフィールド７ */
	String RDM7;
	/** 借方　ユニバーサルフィールド８ */
	String RDM8;
	/** 借方　ユニバーサルフィールド９ */
	String RDM9;
	/** 借方　ユニバーサルフィールド１０ */
	String RDM10;
	/** 借方　ユニバーサルフィールド１１ */
	String RDM11;
	/** 借方　ユニバーサルフィールド１２ */
	String RDM12;
	/** 借方　ユニバーサルフィールド１３ */
	String RDM13;
	/** 借方　ユニバーサルフィールド１４ */
	String RDM14;
	/** 借方　ユニバーサルフィールド１５ */
	String RDM15;
	/** 借方　ユニバーサルフィールド１６ */
	String RDM16;
	/** 借方　ユニバーサルフィールド１７ */
	String RDM17;
	/** 借方　ユニバーサルフィールド１８ */
	String RDM18;
	/** 借方　ユニバーサルフィールド１９ */
	String RDM19;
	/** 借方　ユニバーサルフィールド２０ */
	String RDM20;
	
	/** 借方　摘要 */
	String RTKY;
	/** 借方　摘要コード */
	String RTNO;
	
	/** 貸方　ユニバーサルフィールド４ */
	String SDM4;
	/** 貸方　ユニバーサルフィールド５ */
	String SDM5;
	/** 貸方　ユニバーサルフィールド６ */
	String SDM6;
	/** 貸方　ユニバーサルフィールド７ */
	String SDM7;
	/** 貸方　ユニバーサルフィールド８ */
	String SDM8;
	/** 貸方　ユニバーサルフィールド９ */
	String SDM9;
	/** 貸方　ユニバーサルフィールド１０ */
	String SDM10;
	/** 貸方　ユニバーサルフィールド１１ */
	String SDM11;
	/** 貸方　ユニバーサルフィールド１２ */
	String SDM12;
	/** 貸方　ユニバーサルフィールド１３ */
	String SDM13;
	/** 貸方　ユニバーサルフィールド１４ */
	String SDM14;
	/** 貸方　ユニバーサルフィールド１５ */
	String SDM15;
	/** 貸方　ユニバーサルフィールド１６ */
	String SDM16;
	/** 貸方　ユニバーサルフィールド１７ */
	String SDM17;
	/** 貸方　ユニバーサルフィールド１８ */
	String SDM18;
	/** 貸方　ユニバーサルフィールド１９ */
	String SDM19;
	/** 貸方　ユニバーサルフィールド２０ */
	String SDM20;
	
	/** 貸方　摘要 */
	String STKY;
	/** 貸方　摘要コード */
	String STNO;
	
	/** 貸借別摘要フラグ */
	String TKFLG;
	/** 幣種 */
	String HEIC;
	/** リンクＮｏ */
	String LNO;
	
	//以下　SIASインボイス対応レイアウト
	/** 借方　併用売上税額計算方式 */
	String RURIZEIKEISAN;
	/** 貸方　併用売上税額計算方式 */
	String SURIZEIKEISAN;
	/** 税対象科目　併用売上税額計算方式 */
	String ZURIZEIKEISAN;
	/** 借方　仕入税額控除経過措置割合 */
	String RMENZEIKEIKA;
	/** 貸方　仕入税額控除経過措置割合 */
	String SMENZEIKEIKA;
	/** 税対象科目　仕入税額控除経過措置割合 */
	String ZMENZEIKEIKA;
}
