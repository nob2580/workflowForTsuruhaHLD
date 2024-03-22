package eteam.database.dto;

import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Denpyou implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Denpyou() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Denpyou(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.denpyouKbn = m.get("denpyou_kbn");
		this.denpyouJoutai = m.get("denpyou_joutai");
		this.sanshouDenpyouId = m.get("sanshou_denpyou_id");
		this.daihyouFutanBumonCd = m.get("daihyou_futan_bumon_cd");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.serialNo = m.get("serial_no");
		this.chuushutsuZumiFlg = m.get("chuushutsu_zumi_flg");
		this.shouninRouteHenkouFlg = m.get("shounin_route_henkou_flg");
		this.maruhiFlg = m.get("maruhi_flg");
		this.yosanCheckNengetsu = m.get("yosan_check_nengetsu");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;

	/** 伝票区分 */
	public String denpyouKbn;

	/** 伝票状態 */
	public String denpyouJoutai;

	/** 参照伝票ID */
	public String sanshouDenpyouId;

	/** 代表負担部門コード */
	public String daihyouFutanBumonCd;

	/** 登録ユーザーID */
	public String tourokuUserId;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新ユーザーID */
	public String koushinUserId;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** シリアル番号 */
	public long serialNo;

	/** 抽出済フラグ */
	public String chuushutsuZumiFlg;

	/** 承認ルート変更フラグ */
	public String shouninRouteHenkouFlg;

	/** マル秘文書フラグ */
	public String maruhiFlg;

	/** 予算執行対象月 */
	public String yosanCheckNengetsu;

	/** その他項目map */
	public GMap map;
}