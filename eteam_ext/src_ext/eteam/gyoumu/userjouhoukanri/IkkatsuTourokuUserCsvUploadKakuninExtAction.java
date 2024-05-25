package eteam.gyoumu.userjouhoukanri;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.gyoumu.userjouhoukanri.IkkatsuTourokuUserCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー一括登録（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCsvUploadKakuninExtAction extends IkkatsuTourokuUserCsvUploadKakuninAction {
	//＜定数＞

	//＜画面入力＞

	//＜セッション＞
	/** CSVファイル名（会社切替） */
	String csvFileNameKaishaKirikae;
		
	/** CSVファイル情報（会社切替）リスト */
	List<IkkatsuTourokuUserCSVUploadInfoKaishaKirikae> kaishaKirikaeList;
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		
		if (status == null)
		{
			// 初回
			status = UploadStatus.Init;
		}
		
		//▼カスタマイズ　セション情報を取得する。
		IkkatsuTourokuUserCSVUploadSessionInfoExt sessionInfo = (IkkatsuTourokuUserCSVUploadSessionInfoExt)session.get(Sessionkey.USER_CSV);
		if (null == sessionInfo) {
			errorList.add("CSVファイルのアップロードからやり直してください。");
			return "error";
		}
		csvFileNameUserInfo = sessionInfo.getFileNameUserInfo();
		csvFileNameShozokuBumonWariate = sessionInfo.getFileNameShozokuBumonWariate();
		userInfoList = sessionInfo.getUserInfoList();
		shozokuBumonWariateList = sessionInfo.getShozokuBumonWariateList();
		//▼カスタマイズ
		csvFileNameKaishaKirikae = sessionInfo.getFileNameKaishaKirikae();
		kaishaKirikaeList = sessionInfo.getKaishaKirikaeList();
		//▲カスタマイズ
		errorList = sessionInfo.getErrorList();
		if (sessionInfo.getStatus() != null)
		{
			// statusがgetできたら非同期処理終了と判定
			status = sessionInfo.getStatus();
			session.remove(Sessionkey.USER_CSV);
		}
		// 戻り値を返す
		return "success";
	}
	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){
		try(EteamConnection connection = EteamConnection.connect()) {
			//▼カスタマイズ
			IkkatsuTourokuUserCSVUploadSessionInfoExt sessionInfo = (IkkatsuTourokuUserCSVUploadSessionInfoExt)session.get(Sessionkey.USER_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileNameUserInfo = sessionInfo.getFileNameUserInfo();
			userInfoList        = sessionInfo.getUserInfoList();
			csvFileNameShozokuBumonWariate = sessionInfo.getFileNameShozokuBumonWariate();
			shozokuBumonWariateList         = sessionInfo.getShozokuBumonWariateList();
			//▼カスタマイズ
			csvFileNameKaishaKirikae = sessionInfo.getFileNameKaishaKirikae();
			kaishaKirikaeList = sessionInfo.getKaishaKirikaeList();
			//▲カスタマイズ
			errorList = sessionInfo.getErrorList();
			errorList.add("登録処理中です。");
			HttpServletRequest request = ServletActionContext.getRequest();
			String ip = request.getRemoteAddr();
			String ipx = request.getHeader(HEADER_NAME_XFORWARDED);
			
			// 処理
			IkkatsuTourokuUserExtThread myThread = new IkkatsuTourokuUserExtThread(session, sessionInfo, EteamCommon.getContextSchemaName(), request, ip, ipx);
			myThread.start();
			
			status = UploadStatus.Run;
			return "success";
			
		}
	}
}

