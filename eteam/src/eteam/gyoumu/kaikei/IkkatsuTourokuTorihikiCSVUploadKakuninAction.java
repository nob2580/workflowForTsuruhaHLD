package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.gyoumu.kaikei.IkkatsuTourokuTorihikiCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引一括登録（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuTorihikiCSVUploadKakuninAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜セッション＞
	/** CSVファイル名 */
	String csvFileName;
	/** CSVファイル情報（部門）リスト */
	List<IkkatsuTourokuTorihikiCSVUploadInfo> torihikiList;

//＜画面制御情報＞
	/** SIASバージョンか判別 **/
	boolean issias = (Open21Env.getVersion() == Version.SIAS ) ? true : false; // (変数名が小文字でないとjspで読み取れない？)

	/** ステータス */
	UploadStatus status;
	
//＜部品＞
	/** 取引系 */
	TorihikiLogic trLogic;

	
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
		
		//セッション情報を取得する。
		IkkatsuTourokuTorihikiCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuTorihikiCSVUploadSessionInfo)session.get(Sessionkey.TORIHIKI_CSV);
		if (null == sessionInfo) {
			errorList.add("CSVファイルのアップロードからやり直してください。");
			return "error";
		}
		csvFileName = sessionInfo.getFileName();
		torihikiList = sessionInfo.getTorihikiList();
		errorList = sessionInfo.getErrorList();
		if (sessionInfo.getStatus() != null)
		{
			// statusがgetできたら非同期処理終了と判定
			status = sessionInfo.getStatus();
			session.remove(Sessionkey.TORIHIKI_CSV);
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
			
			//セッション情報を取得する。
			IkkatsuTourokuTorihikiCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuTorihikiCSVUploadSessionInfo)session.get(Sessionkey.TORIHIKI_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileName = sessionInfo.getFileName();
			torihikiList = sessionInfo.getTorihikiList();
			errorList = sessionInfo.getErrorList();
			errorList.add("登録処理中です。");
			
			// 処理
			IkkatsuTourokuTorihikiThread myThread = new IkkatsuTourokuTorihikiThread(session, sessionInfo, EteamCommon.getContextSchemaName());
			myThread.start();
			
			status = UploadStatus.Run;
			return "success";
			
		}
	}
}
