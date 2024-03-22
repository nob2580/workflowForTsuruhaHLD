package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バッチ排他制御DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class BatchHaitaSeigyo implements IEteamDTO {

	/**
	 * default constructor
	 */
	public BatchHaitaSeigyo() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public BatchHaitaSeigyo(GMap m) {
		this.dummy = m.get("dummy");
		this.map = m;
	}

	/** ダミー */
	public String dummy;

	/** その他項目map */
	public GMap map;
}