package eteam.gyoumu.userjouhoukanri;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** ユーザー情報CSVと所属部門割り当てCSVのセッション全部 */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCSVUploadSessionInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;
	/** ファイル名（ユーザー情報） */
	String fileNameUserInfo;
	/** ファイル名（所属部門割り当て） */
	String fileNameShozokuBumonWariate;
	/** ユーザー情報リスト */
	List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList;
	/** 所属部門割り当てリスト */
	List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList;
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
	 * @param fileNameUserInfo ファイル名（ユーザ情報）
	 * @param userInfoList ユーザー情報リスト
	 * @param fileNameShozokuBumonWariate ファイル名（所属部門割り当て）
	 * @param shozokuBumonWariateList ユーザー情報リスト
	 * @param errorList エラーメッセージリスト
	 */
	public IkkatsuTourokuUserCSVUploadSessionInfo(String fileNameUserInfo, List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList, String fileNameShozokuBumonWariate, List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList, List<String> errorList) {
		this.fileNameUserInfo = fileNameUserInfo;
		this.fileNameShozokuBumonWariate = fileNameShozokuBumonWariate;
		this.userInfoList = userInfoList;
		this.shozokuBumonWariateList = shozokuBumonWariateList;
		this.errorList = errorList;
	}
}