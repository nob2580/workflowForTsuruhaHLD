package eteam.gyoumu.userjouhoukanri;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.gyoumu.userjouhoukanri.IkkatsuTourokuUserCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門一括登録（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuUserCsvUploadKakuninAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜セッション＞
	/** CSVファイル名（ユーザー情報） */
	String csvFileNameUserInfo;
	/** CSVファイル名（所属部門割り当て） */
	String csvFileNameShozokuBumonWariate;
	
	/** CSVファイル情報（ユーザー情報）リスト */
	List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList;
	/** CSVファイル情報（所属部門割り当て）リスト */
	List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList;
	
//＜画面制御情報＞
	/** ステータス */
	UploadStatus status;

//＜部品＞
	/**
	 * Proxy経由での接続元IPアドレスが設定されたヘッダ名。
	 */
	protected static final String HEADER_NAME_XFORWARDED = "X-Forwarded-For";

//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

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
		
		//セション情報を取得する。
		IkkatsuTourokuUserCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuUserCSVUploadSessionInfo)session.get(Sessionkey.USER_CSV);
		if (null == sessionInfo) {
			errorList.add("CSVファイルのアップロードからやり直してください。");
			return "error";
		}
		csvFileNameUserInfo = sessionInfo.getFileNameUserInfo();
		csvFileNameShozokuBumonWariate = sessionInfo.getFileNameShozokuBumonWariate();
		userInfoList = sessionInfo.getUserInfoList();
		shozokuBumonWariateList = sessionInfo.getShozokuBumonWariateList();
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
			IkkatsuTourokuUserCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuUserCSVUploadSessionInfo)session.get(Sessionkey.USER_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileNameUserInfo = sessionInfo.getFileNameUserInfo();
			userInfoList        = sessionInfo.getUserInfoList();
			csvFileNameShozokuBumonWariate = sessionInfo.getFileNameShozokuBumonWariate();
			shozokuBumonWariateList         = sessionInfo.getShozokuBumonWariateList();
			errorList = sessionInfo.getErrorList();
			errorList.add("登録処理中です。");
			HttpServletRequest request = ServletActionContext.getRequest();
			String ip = request.getRemoteAddr();
			String ipx = request.getHeader(HEADER_NAME_XFORWARDED);
			
			// 処理
			IkkatsuTourokuUserThread myThread = new IkkatsuTourokuUserThread(session, sessionInfo, EteamCommon.getContextSchemaName(), request, ip, ipx);
			myThread.start();
			
			status = UploadStatus.Run;
			return "success";
			
		}
	}
}
