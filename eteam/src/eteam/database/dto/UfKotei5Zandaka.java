package eteam.database.dto;

import java.math.BigDecimal;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユニバーサルフィールド５残高（固定値）DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class UfKotei5Zandaka implements IEteamDTO {

	/**
	 * default constructor
	 */
	public UfKotei5Zandaka() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public UfKotei5Zandaka(GMap m) {
		this.ufKotei5Cd = m.get("uf_kotei5_cd");
		this.kamokuGaibuCd = m.get("kamoku_gaibu_cd");
		this.kessankiBangou = m.get("kessanki_bangou");
		this.kamokuNaibuCd = m.get("kamoku_naibu_cd");
		this.ufKotei5NameRyakushiki = m.get("uf_kotei5_name_ryakushiki");
		this.chouhyouShaturyokuNo = m.get("chouhyou_shaturyoku_no");
		this.kamokuNameRyakushiki = m.get("kamoku_name_ryakushiki");
		this.kamokuNameSeishiki = m.get("kamoku_name_seishiki");
		this.taishakuZokusei = m.get("taishaku_zokusei");
		this.kishuZandaka = m.get("kishu_zandaka");
		this.map = m;
	}

	/** UF5コード */
	public String ufKotei5Cd;

	/** 科目外部コード */
	public String kamokuGaibuCd;

	/** 決算期番号 */
	public Integer kessankiBangou;

	/** 科目内部コード */
	public String kamokuNaibuCd;

	/** UF5名（略式） */
	public String ufKotei5NameRyakushiki;

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