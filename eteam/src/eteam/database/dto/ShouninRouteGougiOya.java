package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 承認ルート合議親DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class ShouninRouteGougiOya implements IEteamDTO {

	/**
	 * default constructor
	 */
	public ShouninRouteGougiOya() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public ShouninRouteGougiOya(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.edano = m.get("edano");
		this.gougiEdano = m.get("gougi_edano");
		this.gougiPatternNo = m.get("gougi_pattern_no");
		this.gougiName = m.get("gougi_name");
		this.syoriCd = m.get("syori_cd");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;

	/** 枝番号 */
	public int edano;

	/** 合議枝番号 */
	public int gougiEdano;

	/** 合議パターン番号 */
	public long gougiPatternNo;

	/** 合議名 */
	public String gougiName;

	/** 処理コード */
	public String syoriCd;

	/** その他項目map */
	public GMap map;
}
