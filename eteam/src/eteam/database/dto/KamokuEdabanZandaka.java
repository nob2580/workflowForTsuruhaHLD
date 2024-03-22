package eteam.database.dto;

import java.math.BigDecimal;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 科目枝番残高DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KamokuEdabanZandaka implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KamokuEdabanZandaka() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KamokuEdabanZandaka(GMap m) {
		this.kamokuGaibuCd = m.get("kamoku_gaibu_cd");
		this.kamokuEdabanCd = m.get("kamoku_edaban_cd");
		this.kamokuNaibuCd = m.get("kamoku_naibu_cd");
		this.kamokuNameRyakushiki = m.get("kamoku_name_ryakushiki");
		this.kamokuNameSeishiki = m.get("kamoku_name_seishiki");
		this.edabanName = m.get("edaban_name");
		this.kessankiBangou = m.get("kessanki_bangou");
		this.chouhyouShaturyokuNo = m.get("chouhyou_shaturyoku_no");
		this.taishakuZokusei = m.get("taishaku_zokusei");
		this.kishuZandaka = m.get("kishu_zandaka");
		this.kazeiKbn = m.get("kazei_kbn") == null ? null : ((Integer)m.get("kazei_kbn")).shortValue();
		this.bunriKbn = m.get("bunri_kbn") == null ? null : ((Integer)m.get("bunri_kbn")).shortValue();
		this.map = m;
	}

	/** 科目外部コード */
	public String kamokuGaibuCd;

	/** 科目枝番コード */
	public String kamokuEdabanCd;

	/** 科目内部コード */
	public String kamokuNaibuCd;

	/** 科目名（略式） */
	public String kamokuNameRyakushiki;

	/** 科目名（正式） */
	public String kamokuNameSeishiki;

	/** 枝番名 */
	public String edabanName;

	/** 決算期番号 */
	public Integer kessankiBangou;

	/** 帳票出力順番号 */
	public Integer chouhyouShaturyokuNo;

	/** 貸借属性 */
	public Integer taishakuZokusei;

	/** 期首残高 */
	public BigDecimal kishuZandaka;

	/** 課税区分 */
	public Short kazeiKbn;

	/** 分離区分 */
	public Short bunriKbn;

	/** その他項目map */
	public GMap map;
}