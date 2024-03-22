package eteam.gyoumu.kaikei.common;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仕訳抽出テーブルのレコード
 */
@Getter @Setter @ToString
public class ShiwakeDataRS implements Cloneable {
	/** 諸口である */ boolean shokuchi = false;

	/** 部門コード */ String BMN = "";
	/** 取引先コード */ String TOR = "";
	/** 科目コード */ String KMK = "";
	/** 枝番コード */ String EDA = "";
	/** プロジェクトコード */ String PRJ = "";
	/** セグメントコード */ String SEG = "";
	/** UF① */ String DM1 = "";//ユニバーサルフィールド１～２０にマッピングされるWF側の番号
	/** UF② */ String DM2 = "";
	/** UF③ */ String DM3 = "";
	/** UF④ */ String DM4 = "";
	/** UF⑤ */ String DM5 = "";
	/** UF⑥ */ String DM6 = "";
	/** UF⑦ */ String DM7 = "";
	/** UF⑧ */ String DM8 = "";
	/** UF⑨ */ String DM9 = "";
	/** UF⑩ */ String DM10 = "";
	/** 固定UF① */ String K_DM1 = "";//ユニバーサルフィールド１～２０にマッピングされるWF側の番号
	/** 固定UF② */ String K_DM2 = "";
	/** 固定UF③ */ String K_DM3 = "";
	/** 固定UF④ */ String K_DM4 = "";
	/** 固定UF⑤ */ String K_DM5 = "";
	/** 固定UF⑥ */ String K_DM6 = "";
	/** 固定UF⑦ */ String K_DM7 = "";
	/** 固定UF⑧ */ String K_DM8 = "";
	/** 固定UF⑨ */ String K_DM9 = "";
	/** 固定UF⑩ */ String K_DM10 = "";
	/** 税率 */ BigDecimal RIT;
	/** 軽減税率区分 */ String KEIGEN = "";
	/** 課税区分 */ String ZKB = "";
	/** 仕入区分 */ String SRE = "";
	/** 摘要(共通でない場合) */ String TKY = "";
	/** 摘要コード(共通でない場合) */ String TNO = "";
	/** 分離区分(連携対象外) */ String BUNRI = "";
	/** 金額 */ BigDecimal VALU;
	/** 金額 */ BigDecimal VALU_hontai;
	/** 併用売上税額計算方式 */ String URIZEIKEISAN = "";
	/** 仕入税額控除経過措置割合 */ String MENZEIKEIKA = "";

	/** 伝票ID/社員NOを貸借UFに反映する仕訳である */ boolean didFlg;
	/** 借UFに反映する伝票ID */ String did;
	/** 社員NOを連携する貸借UFに反映する仕訳である */ boolean shainCdFlg;
	/** 社員NO */ String shainNo;
	/** 法人カード識別用番号を貸UFに反映する仕訳である */ boolean houjinFlg;
	/** 貸UFに反映する法人カード識別用番号 */ String houjin;
	
	/** 伝票区分 */ String denpyouKbn;

	@Override
	public ShiwakeDataRS clone() {
		try {
			return (ShiwakeDataRS)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
		{
			return false;
		}
		if (! (o instanceof ShiwakeDataRS))
		{
			return false;
		}
		
		ShiwakeDataRS s1 = this;
		ShiwakeDataRS s2 = (ShiwakeDataRS)o;
		
		return
				   equals(s1.BMN , s2.BMN)
				&& equals(s1.TOR , s2.TOR)
				&& equals(s1.KMK , s2.KMK)
				&& equals(s1.EDA , s2.EDA)
				&& equals(s1.PRJ , s2.PRJ)
				&& equals(s1.SEG , s2.SEG)
				&& equals(s1.DM1 , s2.DM1)
				&& equals(s1.DM2 , s2.DM2)
				&& equals(s1.DM3 , s2.DM3)
				&& equals(s1.DM4 , s2.DM4)
				&& equals(s1.DM5 , s2.DM5)
				&& equals(s1.DM6 , s2.DM6)
				&& equals(s1.DM7 , s2.DM7)
				&& equals(s1.DM8 , s2.DM8)
				&& equals(s1.DM9 , s2.DM9)
				&& equals(s1.DM10 , s2.DM10)
				&& equals(s1.K_DM1 , s2.K_DM1)
				&& equals(s1.K_DM2 , s2.K_DM2)
				&& equals(s1.K_DM3 , s2.K_DM3)
				&& equals(s1.K_DM4 , s2.K_DM4)
				&& equals(s1.K_DM5 , s2.K_DM5)
				&& equals(s1.K_DM6 , s2.K_DM6)
				&& equals(s1.K_DM7 , s2.K_DM7)
				&& equals(s1.K_DM8 , s2.K_DM8)
				&& equals(s1.K_DM9 , s2.K_DM9)
				&& equals(s1.K_DM10 , s2.K_DM10)
				&& equals(s1.RIT , s2.RIT)
				&& equals(s1.KEIGEN , s2.KEIGEN)
				&& equals(s1.ZKB , s2.ZKB)
				&& equals(s1.SRE , s2.SRE)
				&& equals(s1.URIZEIKEISAN , s2.URIZEIKEISAN)
				&& equals(s1.MENZEIKEIKA , s2.MENZEIKEIKA)
				&& equals(s1.TKY , s2.TKY);
	}
	/**
	 * 比較
	 * @param o1 1
	 * @param o2 2
	 * @return 1と2が一致したらtrue
	 */
	protected boolean equals(Object o1, Object o2){
		if(o1 == null){
			return o2 == null;
		}else{
			return o1.equals(o2);
		}
	}
}
