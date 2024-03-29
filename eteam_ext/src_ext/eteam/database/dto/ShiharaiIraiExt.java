package eteam.database.dto;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 支払依頼DTO
 * 今後のカスタマイズ含め作成しておく
 */
@Getter @Setter @ToString
public class ShiharaiIraiExt implements IEteamDTO{

	public ShiharaiIraiExt() {
		
	}
	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public ShiharaiIraiExt(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.furikomiGinkouId = m.get("furikomi_ginkou_id");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;
	
	/** 振込銀行ID*\ */
	public Integer furikomiGinkouId;
	
	/** その他項目map */
	public GMap map;
}
