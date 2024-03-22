package eteam.database.dto;

import java.sql.Date;
import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * インフォメーションDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Information implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Information() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Information(GMap m) {
		this.infoId = m.get("info_id");
		this.tsuuchiKikanFrom = m.get("tsuuchi_kikan_from");
		this.tsuuchiKikanTo = m.get("tsuuchi_kikan_to");
		this.tsuuchiNaiyou = m.get("tsuuchi_naiyou");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** インフォメーションID */
	public String infoId;

	/** 通知期間開始日 */
	public Date tsuuchiKikanFrom;

	/** 通知期間終了日 */
	public Date tsuuchiKikanTo;

	/** 通知内容 */
	public String tsuuchiNaiyou;

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
