package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * open21金融機関DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Open21Kinyuukikan implements IEteamDTO {

	/** 金融機関コード */
	public String kinyuukikanCd;

	/** 金融機関支店コード */
	public String kinyuukikanShitenCd;

	/** 金融機関名（半角カナ） */
	public String kinyuukikanNameHankana;

	/** 金融機関名 */
	public String kinyuukikanNameKana;

	/** 支店名（半角カナ） */
	public String shitenNameHankana;

	/** 支店名 */
	public String shitenNameKana;

	/** その他項目map */
	public GMap map;
}
