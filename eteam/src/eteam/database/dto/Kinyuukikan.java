package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 金融機関DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Kinyuukikan implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Kinyuukikan() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Kinyuukikan(GMap m) {
		this.kinyuukikanCd = m.get("kinyuukikan_cd");
		this.kinyuukikanShitenCd = m.get("kinyuukikan_shiten_cd");
		this.kinyuukikanNameHankana = m.get("kinyuukikan_name_hankana");
		this.kinyuukikanNameKana = m.get("kinyuukikan_name_kana");
		this.shitenNameHankana = m.get("shiten_name_hankana");
		this.shitenNameKana = m.get("shiten_name_kana");
		this.map = m;
	}

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