package eteam.database.dto;

import java.sql.Date;
import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最終承認ルート親DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class SaishuuSyouninRouteOya implements IEteamDTO {

	/**
	 * default constructor
	 */
	public SaishuuSyouninRouteOya() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public SaishuuSyouninRouteOya(GMap m) {
		this.denpyouKbn = m.get("denpyou_kbn");
		this.edano = m.get("edano");
		this.chuukiMongon = m.get("chuuki_mongon");
		this.yuukouKigenFrom = m.get("yuukou_kigen_from");
		this.yuukouKigenTo = m.get("yuukou_kigen_to");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 伝票区分 */
	public String denpyouKbn;

	/** 枝番号 */
	public int edano;

	/** 注記文言 */
	public String chuukiMongon;

	/** 有効期限開始日 */
	public Date yuukouKigenFrom;

	/** 有効期限終了日 */
	public Date yuukouKigenTo;

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