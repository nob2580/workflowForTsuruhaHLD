package eteam.gyoumu.kaikei.common;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仕訳抽出テーブルのレコード
 */
@Getter @Setter @ToString
public class ShiwakeDataCom implements Cloneable {
	/** （オープン２１）伝票日付 */
	Date DYMD;
	/** （オープン２１）伝票番号 */
	String DCNO;
	/** （オープン２１）摘要 */
	String TKY;
	/** （オープン２１）貸方摘要 借方摘要と異なる時だけ設定して */
	String TKY_kashi;
	/** （オープン２１）摘要コード */
	String TNO;
	/** （オープン２１）金額 */
	BigDecimal VALU;
	/** （オープン２１）金額(課税区分：税抜の時に使用) */
	BigDecimal VALU_hontai;
	/** （オープン２１）支払日 */
	Date SYMD;
	/** 分離区分 */ //インボイス税抜対応により、WF側で入力することになったので
	String BUNRI = "";

	@Override
	public ShiwakeDataCom clone() {
		try {
			return (ShiwakeDataCom)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
