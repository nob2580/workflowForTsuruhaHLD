package eteam.gyoumu.kaikei;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（申請内容） */
@Getter @Setter @ToString
public class ShiharaiIraiCSVUploadDenpyouExtInfo extends ShiharaiIraiCSVUploadDenpyouInfo {

	/** URL */
	String[] url;
}
