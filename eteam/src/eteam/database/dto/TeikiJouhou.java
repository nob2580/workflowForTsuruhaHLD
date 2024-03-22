package eteam.database.dto;

import java.sql.Date;
import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定期券情報DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class TeikiJouhou implements IEteamDTO {

	/**
	 * default constructor
	 */
	public TeikiJouhou() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public TeikiJouhou(GMap m) {
		this.userId = m.get("user_id");
		this.shiyouKaishibi = m.get("shiyou_kaishibi");
		this.shiyouShuuryoubi = m.get("shiyou_shuuryoubi");
		this.intraTeikiKukan = m.get("intra_teiki_kukan");
		this.intraRestoreroute = m.get("intra_restoreroute");
		this.webTeikiKukan = m.get("web_teiki_kukan");
		this.webTeikiSerializeData = m.get("web_teiki_serialize_data");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** ユーザーID */
	public String userId;

	/** 使用開始日 */
	public Date shiyouKaishibi;

	/** 使用終了日 */
	public Date shiyouShuuryoubi;

	/** イントラ版定期区間 */
	public String intraTeikiKukan;

	/** イントラ版方向性付き定期経路文字列 */
	public String intraRestoreroute;

	/** 定期区間情報 */
	public String webTeikiKukan;

	/** 定期区間シリアライズデータ */
	public String webTeikiSerializeData;

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
