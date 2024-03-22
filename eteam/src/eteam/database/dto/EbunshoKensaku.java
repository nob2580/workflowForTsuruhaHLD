package eteam.database.dto;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author j_matsumoto
 *
 */
@Getter @Setter @ToString
public class EbunshoKensaku implements IEteamDTO {

	/**
	 * default constructor
	 */
	public EbunshoKensaku() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public EbunshoKensaku(GMap m) {
		this.docType = ((Integer)m.get("doc_type")).shortValue();
		this.itemNo = ((Integer)m.get("item_no")).shortValue();
		this.niniFlg = ((Integer)m.get("nini_flg")).shortValue();
		this.map = m;
	}

	/** 文書種別 */
	public short docType;

	/** 項目 */
	public short itemNo;

	/** 任意フラグ */
	public short niniFlg;

	/** その他項目map */
	public GMap map;
}
