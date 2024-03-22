package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案番号採番DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KianBangouSaiban implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KianBangouSaiban() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KianBangouSaiban(GMap m) {
		this.bumonCd = m.get("bumon_cd");
		this.nendo = m.get("nendo");
		this.ryakugou = m.get("ryakugou");
		this.kianBangouFrom = m.get("kian_bangou_from");
		this.kianBangouLast = m.get("kian_bangou_last");
		this.map = m;
	}

	/** 部門コード */
	public String bumonCd;

	/** 年度 */
	public String nendo;

	/** 略号 */
	public String ryakugou;

	/** 開始起案番号 */
	public int kianBangouFrom;

	/** 最終起案番号 */
	public int kianBangouLast;

	/** その他項目map */
	public GMap map;
}
