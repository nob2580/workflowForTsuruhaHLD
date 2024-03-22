package eteam.gyoumu.kaikei;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（申請内容明細） */
@Getter @Setter @ToString
public class ShiharaiIraiCSVUploadMeisaiInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;

	/** csv内のnumbering */
	String number;
	/** 伝票枝番号 */
	String denpyouEdaNo;

	/** 摘要 */
	String tekiyou;
	/** 支払金額 */
	String shiharaiKingaku;
	/** 消費税率 */
	String zeiritsu;
	/** 軽減税率区分 */
	String keigenZeiritsuKbn;
	/** 仕訳枝番号 */
	String shiwakeEdaNo;
	/** 勘定科目枝番 */
	String kamokuEdabanCd;
	/** 負担部門コード */
	String futanBumonCd;
	/** UF1コード */
	String uf1Cd;
	/** UF2コード */
	String uf2Cd;
	/** UF3コード */
	String uf3Cd;
	/** UF4コード */
	String uf4Cd;
	/** UF5コード */
	String uf5Cd;
	/** UF6コード */
	String uf6Cd;
	/** UF7コード */
	String uf7Cd;
	/** UF8コード */
	String uf8Cd;
	/** UF9コード */
	String uf9Cd;
	/** UF10コード */
	String uf10Cd;
	/** プロジェクトコード */
	String projectCd;
	/** セグメントコード */
	String segmentCd;
	
	// インボイス系プロパティ、一旦仮置き
	/** 分離区分 */
// String bunriKbn = null;
	/** 借方仕入区分 */
// String kariShiireKbn = "";
	/** 税抜金額 */
	String zeinukiKingaku = "0";
	/** 消費税額 */
	String shouhizeigaku = "0";
}
