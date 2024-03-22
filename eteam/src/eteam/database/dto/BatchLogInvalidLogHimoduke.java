package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バッチログ不良ログ紐づけDTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class BatchLogInvalidLogHimoduke implements IEteamDTO {

	/**
	 * default constructor
	 */
	public BatchLogInvalidLogHimoduke() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public BatchLogInvalidLogHimoduke(GMap m) {
		this.serialNo = m.get("serial_no");
		this.edaban = m.get("edaban");
		this.fileName = m.get("file_name");
		this.map = m;
	}

	/** シリアル番号 */
	public long serialNo;

	/** 枝番 */
	public int edaban;

	/** ファイル名 */
	public String fileName;

	/** その他項目map */
	public GMap map;
}