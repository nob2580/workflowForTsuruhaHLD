package eteam.database.dto;

import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 見出しDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Midashi implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Midashi() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Midashi(GMap m) {
		this.midashiId = m.get("midashi_id");
		this.midashiName = m.get("midashi_name");
		this.hyoujiJun = m.get("hyouji_jun");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 見出しID */
	public long midashiId;

	/** 見出し名 */
	public String midashiName;

	/** 表示順 */
	public int hyoujiJun;

	/** 登録ユーザーID */
	public String tourokuUserId;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新ユーザーID */
	public String koushinUserId;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** その他項目map */
	public GMap map;
}
