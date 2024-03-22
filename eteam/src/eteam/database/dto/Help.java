package eteam.database.dto;

import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ヘルプDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Help implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Help() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Help(GMap m) {
		this.gamenId = m.get("gamen_id");
		this.helpRichText = m.get("help_rich_text");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 画面ID */
	public String gamenId;

	/** ヘルプリッチテキスト */
	public String helpRichText;

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