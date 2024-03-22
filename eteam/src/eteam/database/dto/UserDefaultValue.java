package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー別デフォルト値DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class UserDefaultValue implements IEteamDTO {

	/**
	 * default constructor
	 */
	public UserDefaultValue() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public UserDefaultValue(GMap m) {
		this.kbn = m.get("kbn");
		this.userId = m.get("user_id");
		this.defaultValue = m.get("default_value");
		this.map = m;
	}

	/** 区分 */
	public String kbn;

	/** ユーザーID */
	public String userId;

	/** デフォルト値 */
	public String defaultValue;

	/** その他項目map */
	public GMap map;
}
