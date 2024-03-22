package eteam.gyoumu.kaikei;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 取引CSVのセッション全部 */
@Getter @Setter @ToString
public class IkkatsuTourokuTorihikiCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名 */
	String fileName;
	/** 取引リスト */
	List<IkkatsuTourokuTorihikiCSVUploadInfo> torihikiList;
	/** エラーリスト */
	List<String> errorList;

	/** アップロードステータス */
	enum UploadStatus {
		/** 初期表示 */
		Init, 
		/** 処理中 */
		Run,
		/** 処理終了 */
		End
    };
    
    /** 現在のアップロードステータス */
    UploadStatus status;
    
    /**
	 * コンストラクタ
	 * @param fileName ファイル名
	 * @param trList 取引リスト
	 * @param errorList エラーメッセージリスト
	 */
	public IkkatsuTourokuTorihikiCSVUploadSessionInfo(String fileName, List<IkkatsuTourokuTorihikiCSVUploadInfo> trList, List<String> errorList) {
		this.fileName = fileName;
		this.torihikiList     = trList;
		this.errorList = errorList;
	}
}