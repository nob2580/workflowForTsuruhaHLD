package eteam.database.dto;

import java.math.BigDecimal;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 国内用日当等マスターDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class NittouNadoMaster implements IEteamDTO {

	/**
	 * default constructor
	 */
	public NittouNadoMaster() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public NittouNadoMaster(GMap m) {
		this.shubetsu1 = m.get("shubetsu1");
		this.shubetsu2 = m.get("shubetsu2");
		this.yakushokuCd = m.get("yakushoku_cd");
		this.tanka = m.get("tanka");
		this.shouhyouShoruiHissuFlg = m.get("shouhyou_shorui_hissu_flg");
		this.nittouShukuhakuhiFlg = m.get("nittou_shukuhakuhi_flg");
		this.zeiKubun = m.get("zei_kubun");
		this.edaban = m.get("edaban");
		this.map = m;
	}

	/** 種別１ */
	public String shubetsu1;

	/** 種別２ */
	public String shubetsu2;

	/** 役職コード */
	public String yakushokuCd;

	/** 単価 */
	public BigDecimal tanka;

	/** 証憑書類必須フラグ */
	public String shouhyouShoruiHissuFlg;

	/** 日当・宿泊費フラグ */
	public String nittouShukuhakuhiFlg;

	/** 税区分 */
	public String zeiKubun;

	/** 枝番コード */
	public String edaban;

	/** その他項目map */
	public GMap map;
}