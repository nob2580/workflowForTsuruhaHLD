package eteam.gyoumu.kaikei;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 請求書払いCSVのセッション全部 */
@Getter @Setter @ToString
public class SeikyuushoBaraiCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名 */
	String fileName;
	/** 伝票リスト */
	List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList;
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
    
	/** 処理済 */
	String denpyouId;
	/**
	 * コンストラクタ
	 * @param fileName ファイル名
	 * @param denpyouList 伝票リスト
	 * @param errorList エラーメッセージリスト
	 */
	public SeikyuushoBaraiCSVUploadSessionInfo(String fileName, List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList, List<String> errorList) {
		this.fileName = fileName;
		this.denpyouList = denpyouList;
		this.errorList = errorList;
	}
}