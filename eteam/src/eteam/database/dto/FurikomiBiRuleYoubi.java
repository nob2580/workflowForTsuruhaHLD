package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 振込日ルール(曜日)DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class FurikomiBiRuleYoubi implements IEteamDTO {

	/**
	 * default constructor
	 */
	public FurikomiBiRuleYoubi() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public FurikomiBiRuleYoubi(GMap m) {
		this.kijunWeekday = m.get("kijun_weekday");
		this.furikomiWeekday = m.get("furikomi_weekday");
		this.map = m;
	}

	/** 基準曜日 */
	public int kijunWeekday;

	/** 振込曜日 */
	public int furikomiWeekday;

	/** その他項目map */
	public GMap map;
}
