package eteam.database.dto;

import java.sql.Date;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * （期別）部門DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KiBumon implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KiBumon() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KiBumon(GMap m) {
		this.kesn = m.get("kesn");
		this.futanBumonCd = m.get("futan_bumon_cd");
		this.futanBumonName = m.get("futan_bumon_name");
		this.oyaSyuukeiBumonCd = m.get("oya_syuukei_bumon_cd");
		this.shiireKbn = ((Integer)m.get("shiire_kbn")).shortValue();
		this.nyuryokuFromDate = m.get("nyuryoku_from_date");
		this.nyuryokuToDate = m.get("nyuryoku_to_date");
		this.map = m;
	}

	/** 内部決算期 */
	public int kesn;

	/** 負担部門コード */
	public String futanBumonCd;

	/** 負担部門名 */
	public String futanBumonName;

	/** 親集計部門コード */
	public String oyaSyuukeiBumonCd;

	/** 仕入区分 */
	public short shiireKbn;

	/** 入力開始日 */
	public Date nyuryokuFromDate;

	/** 入力終了日 */
	public Date nyuryokuToDate;

	/** その他項目map */
	public GMap map;
}