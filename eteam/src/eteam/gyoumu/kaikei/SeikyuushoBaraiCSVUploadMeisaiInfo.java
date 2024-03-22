package eteam.gyoumu.kaikei;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（申請内容明細） */
@Getter @Setter @ToString
public class SeikyuushoBaraiCSVUploadMeisaiInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;

	/** csv内のnumbering */
	String number;
	/** 伝票枝番号 */
	String denpyouEdaNo;
	/** 仕訳枝番号 */
	String shiwakeEdaNo;
	/** 勘定科目枝番コード */
	String kamokuEdabanCd;
	/** 負担部門コード */
	String futanBumonCd;
	/** 取引先コード */
	String torihikisakiCd;
	/** 摘要 */
	String tekiyou;
	/** 支払金額(金額) */
	String shiharaiKingaku;
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
	/** 消費税率 */
	String zeiritsu;
	/** 軽減税率区分 */
	String keigenZeiritsuKbn;
	/** 交際費人数 */
	String kousaihiNinzuu;
	/** 事業者区分 */
	String jigyoushaKbn;
	/** 税抜金額 */
	String zeinukiKingaku = "0";
	/** 消費税額 */
	String shouhizeigaku = "0";
}
