package eteam.gyoumu.kaikei;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 部門別取引CSVのセッション全部 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名 */
	String fileName;
	/** 部門別取引リスト */
	List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> bumonbetsuTorihikiList;
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
	 * @param bumonTRList 部門別取引リスト
	 * @param errorList エラーメッセージリスト
	 */
	public IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo(String fileName, List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> bumonTRList, List<String> errorList) {
		this.fileName = fileName;
		this.bumonbetsuTorihikiList     = bumonTRList;
		this.errorList = errorList;
	}
}