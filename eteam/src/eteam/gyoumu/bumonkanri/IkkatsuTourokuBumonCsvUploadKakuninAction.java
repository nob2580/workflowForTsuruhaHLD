package eteam.gyoumu.bumonkanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.gyoumu.bumonkanri.IkkatsuTourokuBumonCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門一括登録（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonCsvUploadKakuninAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜セッション＞
	/** CSVファイル名 */
	String csvFileName;
	/** CSVファイル情報（部門）リスト */
	List<IkkatsuTourokuBumonCSVUploadInfo> bumonList;

//＜画面制御情報＞
	/** ステータス */
	UploadStatus status;

//＜部品＞

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
		IkkatsuTourokuBumonCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuBumonCSVUploadSessionInfo)session.get(Sessionkey.BUMON_CSV);
		if (null == sessionInfo) {
			errorList.add("CSVファイルのアップロードからやり直してください。");
			return "error";
		}
		csvFileName = sessionInfo.getFileName();
		bumonList = sessionInfo.getBumonList();
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
		try(EteamConnection connection = EteamConnection.connect();) {
			//セション情報を取得する。
			IkkatsuTourokuBumonCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuBumonCSVUploadSessionInfo)session.get(Sessionkey.BUMON_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileName = sessionInfo.getFileName();
			bumonList = sessionInfo.getBumonList();
			errorList = sessionInfo.getErrorList();
			errorList.add("登録処理中です。");
			
			// 処理
			IkkatsuTourokuBumonThread myThread = new IkkatsuTourokuBumonThread(session, sessionInfo, EteamCommon.getContextSchemaName());
			myThread.start();
						
			status = UploadStatus.Run;
			return "success";
			
		}
	}
}
