package eteam.database.dto;

import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 届出ジェネレータDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KaniTodoke implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KaniTodoke() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KaniTodoke(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.denpyouKbn = m.get("denpyou_kbn");
		this.version = m.get("version");
		this.itemName = m.get("item_name");
		this.value1 = m.get("value1");
		this.value2 = m.get("value2");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;

	/** 伝票区分 */
	public String denpyouKbn;

	/** バージョン */
	public int version;

	/** 項目名 */
	public String itemName;

	/** 値１ */
	public String value1;

	/** 値２ */
	public String value2;

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