package eteam.database.dto;

import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門ロールDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class BumonRole implements IEteamDTO {

	/**
	 * default constructor
	 */
	public BumonRole() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public BumonRole(GMap m) {
		this.bumonRoleId = m.get("bumon_role_id");
		this.bumonRoleName = m.get("bumon_role_name");
		this.hyoujiJun = m.get("hyouji_jun");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 部門ロールID */
	public String bumonRoleId;

	/** 部門ロール名 */
	public String bumonRoleName;

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
