package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユニバーサルフィールド４一覧DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Uf4Ichiran implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Uf4Ichiran() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Uf4Ichiran(GMap m) {
		this.uf4Cd = m.get("uf4_cd");
		this.uf4NameRyakushiki = m.get("uf4_name_ryakushiki");
		this.kessankiBangou = m.get("kessanki_bangou");
		this.map = m;
	}

	/** UF4コード */
	public String uf4Cd;

	/** UF4名（略式） */
	public String uf4NameRyakushiki;

	/** 決算期番号 */
	public Integer kessankiBangou;

	/** その他項目map */
	public GMap map;
}