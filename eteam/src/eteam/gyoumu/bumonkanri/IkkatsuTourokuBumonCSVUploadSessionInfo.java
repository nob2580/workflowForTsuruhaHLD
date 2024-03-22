package eteam.gyoumu.bumonkanri;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 部門CSVのセッション全部 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名 */
	String fileName;
	/** 部門リスト */
	List<IkkatsuTourokuBumonCSVUploadInfo> bumonList;
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
	 * @param bumonList 部門リスト
	 * @param errorList エラーメッセージリスト
	 */
	public IkkatsuTourokuBumonCSVUploadSessionInfo(String fileName, List<IkkatsuTourokuBumonCSVUploadInfo> bumonList, List<String> errorList) {
		this.fileName = fileName;
		this.bumonList     = bumonList;
		this.errorList = errorList;
	}
}