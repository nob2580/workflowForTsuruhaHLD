package eteam.gyoumu.masterkanri;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * マスターデータ。アップロード時にセッションに保持する。
 */
@Getter @Setter @ToString
public class MasterData implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;

	/** ファイル名 */
	String uploadFileFileName;
	/** ファイルサイズ */
	long uploadFileSize;
	/** ファイルコンテンツタイプ */
	String uploadFileContentType;
	/** ファイルデータ */
	byte[] uploadFileData;
}
