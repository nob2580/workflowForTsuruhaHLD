package eteam.database.dto;

import java.math.BigDecimal;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユニバーサルフィールド７残高（固定値）DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class UfKotei7Zandaka implements IEteamDTO {

	/**
	 * default constructor
	 */
	public UfKotei7Zandaka() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public UfKotei7Zandaka(GMap m) {
		this.ufKotei7Cd = m.get("uf_kotei7_cd");
		this.kamokuGaibuCd = m.get("kamoku_gaibu_cd");
		this.kessankiBangou = m.get("kessanki_bangou");
		this.kamokuNaibuCd = m.get("kamoku_naibu_cd");
		this.ufKotei7NameRyakushiki = m.get("uf_kotei7_name_ryakushiki");
		this.chouhyouShaturyokuNo = m.get("chouhyou_shaturyoku_no");
		this.kamokuNameRyakushiki = m.get("kamoku_name_ryakushiki");
		this.kamokuNameSeishiki = m.get("kamoku_name_seishiki");
		this.taishakuZokusei = m.get("taishaku_zokusei");
		this.kishuZandaka = m.get("kishu_zandaka");
		this.map = m;
	}

	/** UF7コード */
	public String ufKotei7Cd;

	/** 科目外部コード */
	public String kamokuGaibuCd;

	/** 決算期番号 */
	public Integer kessankiBangou;

	/** 科目内部コード */
	public String kamokuNaibuCd;

	/** UF7名（略式） */
	public String ufKotei7NameRyakushiki;

	/** 帳票出力順番号 */
	public Integer chouhyouShaturyokuNo;

	/** 科目名（略式） */
	public String kamokuNameRyakushiki;

	/** 科目名（正式） */
	public String kamokuNameSeishiki;

	/** 貸借属性 */
	public int taishakuZokusei;

	/** 期首残高 */
	public BigDecimal kishuZandaka;

	/** その他項目map */
	public GMap map;
}