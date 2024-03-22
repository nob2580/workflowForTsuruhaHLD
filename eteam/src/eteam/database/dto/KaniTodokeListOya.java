package eteam.database.dto;

import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 届出ジェネレータ項目リスト親DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KaniTodokeListOya implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KaniTodokeListOya() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KaniTodokeListOya(GMap m) {
		this.denpyouKbn = m.get("denpyou_kbn");
		this.version = m.get("version");
		this.areaKbn = m.get("area_kbn");
		this.itemName = m.get("item_name");
		this.labelName = m.get("label_name");
		this.hissuFlg = m.get("hissu_flg");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 伝票区分 */
	public String denpyouKbn;

	/** バージョン */
	public int version;

	/** エリア区分 */
	public String areaKbn;

	/** 項目名 */
	public String itemName;

	/** ラベル名 */
	public String labelName;

	/** 必須フラグ */
	public String hissuFlg;

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
