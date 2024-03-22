package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユニバーサルフィールド２一覧（固定値）DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class UfKotei2Ichiran implements IEteamDTO {

	/**
	 * default constructor
	 */
	public UfKotei2Ichiran() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public UfKotei2Ichiran(GMap m) {
		this.ufKotei2Cd = m.get("uf_kotei2_cd");
		this.ufKotei2NameRyakushiki = m.get("uf_kotei2_name_ryakushiki");
		this.kessankiBangou = m.get("kessanki_bangou");
		this.map = m;
	}

	/** UF2コード */
	public String ufKotei2Cd;

	/** UF2名（略式） */
	public String ufKotei2NameRyakushiki;

	/** 決算期番号 */
	public Integer kessankiBangou;

	/** その他項目map */
	public GMap map;
}