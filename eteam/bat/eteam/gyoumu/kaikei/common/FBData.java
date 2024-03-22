package eteam.gyoumu.kaikei.common;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * FB抽出テーブルのレコード
 */
@Getter @Setter @ToString
public class FBData {
	/** シリアル番号 */
	Long serialNo;
	/** 伝票ID */
	String denpyouId;
	/** ユーザーID */
	String userId;
	/** FB抽出状態 */
	String fbStatus;
	/** 種別コード */
	String shubetsuCd;
	/** コード区分 */
	String cdKbn;
	/** 会社コード */
	String kaishaCd;
	/** 会社名（半角カナ） */
	String kaishaNameHankana;
	/** 振込日 */
	Date furikomiDate;
	/** 振込元金融機関コード */
	String motoKinyuukikanCd;
	/** 振込元金融機関名（半角カナ） */
	String motoKinyuukikanNameHankana;
	/** 振込元金融機関支店コード */
	String motoKinyuukikanShitenCd;
	/** 振込元金融機関支店名（半角カナ） */
	String motoKinyuukikanShitenNameHankana;
	/** 振込元預金種目コード */
	String motoYokinShumokuCd;
	/** 振込元口座番号 */
	String motoKouzaBangou;
	/** 振込先金融機関銀行コード */
	String sakiKinyuukikanCd;
	/** 振込先金融機関名（半角カナ） */
	String sakiKinyuukikanNameHankana;
	/** 振込先金融機関支店コード */
	String sakiKinyuukikanShitenCd;
	/** 振込先金融機関支店名（半角カナ） */
	String sakiKinyuukikanShitenNameHankana;
	/** 振込先預金種目コード */
	String sakiYokinShumokuCd;
	/** 振込先口座番号 */
	String sakiKouzaBangou;
	/** 振込先口座名義（カナ） */
	String sakiKouzaMeigiKana;
	/** 金額 */
	BigDecimal kingaku;
	/** 新規コード */
	String shinkiCd;
	/** 顧客コード１ */
	String kokyakuCd1;
	/** 振込区分 */
	String furikomiKbn;
}
