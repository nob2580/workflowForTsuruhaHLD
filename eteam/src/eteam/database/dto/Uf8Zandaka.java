package eteam.database.dto;

import java.math.BigDecimal;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユニバーサルフィールド８残高DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Uf8Zandaka implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Uf8Zandaka() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Uf8Zandaka(GMap m) {
		this.uf8Cd = m.get("uf8_cd");
		this.kamokuGaibuCd = m.get("kamoku_gaibu_cd");
		this.kessankiBangou = m.get("kessanki_bangou");
		this.kamokuNaibuCd = m.get("kamoku_naibu_cd");
		this.uf8NameRyakushiki = m.get("uf8_name_ryakushiki");
		this.chouhyouShaturyokuNo = m.get("chouhyou_shaturyoku_no");
		this.kamokuNameRyakushiki = m.get("kamoku_name_ryakushiki");
		this.kamokuNameSeishiki = m.get("kamoku_name_seishiki");
		this.taishakuZokusei = m.get("taishaku_zokusei");
		this.kishuZandaka = m.get("kishu_zandaka");
		this.map = m;
	}

	/** UF8コード */
	public String uf8Cd;

	/** 科目外部コード */
	public String kamokuGaibuCd;

	/** 決算期番号 */
	public Integer kessankiBangou;

	/** 科目内部コード */
	public String kamokuNaibuCd;

	/** UF8名（略式） */
	public String uf8NameRyakushiki;

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