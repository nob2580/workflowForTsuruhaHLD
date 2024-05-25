package eteam.gyoumu.userjouhoukanri;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** ユーザー情報CSVと所属部門割り当てCSVのセッション全部 */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCSVUploadSessionInfoExt extends IkkatsuTourokuUserCSVUploadSessionInfo {
	/** ファイル名（会社切替） */
	String fileNameKaishaKirikae;
	/** 所属部門割り当てリスト */
	List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> kaishaKirikaeList;
	
	/**
	 * コンストラクタ
	 * @param fileNameUserInfo ファイル名（ユーザ情報）
	 * @param userInfoList ユーザー情報リスト
	 * @param fileNameShozokuBumonWariate ファイル名（所属部門割り当て）
	 * @param shozokuBumonWariateList ユーザー情報リスト
	 * @param fileNameKaishaKirikae ファイル名(会社切替）
	 * @param kaishaKirikaeList 会社切替リスト
	 * @param errorList エラーメッセージリスト
	 */
	public IkkatsuTourokuUserCSVUploadSessionInfoExt(
			String fileNameUserInfo, 
			List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList, 
			String fileNameShozokuBumonWariate, 
			List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList, 
			String fileNameKaishaKirikae, 
			List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> kaishaKirikaeList, 
			List<String> errorList) {
		super(fileNameUserInfo,userInfoList,fileNameShozokuBumonWariate, shozokuBumonWariateList,errorList);
		
		this.fileNameUserInfo = fileNameUserInfo;
		this.fileNameShozokuBumonWariate = fileNameShozokuBumonWariate;
		this.userInfoList = userInfoList;
		this.shozokuBumonWariateList = shozokuBumonWariateList;
		this.fileNameKaishaKirikae = fileNameKaishaKirikae;
		this.kaishaKirikaeList = kaishaKirikaeList;
		this.errorList = errorList;
	}

}
