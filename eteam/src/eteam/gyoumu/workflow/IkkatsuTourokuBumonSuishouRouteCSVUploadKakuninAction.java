package eteam.gyoumu.workflow;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.gyoumu.workflow.IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門推奨ルート一括登録（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuBumonSuishouRouteCSVUploadKakuninAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜セッション＞
	/** CSVファイル名（部門推奨ルート(親)） */
	String csvFileNameOyaInfo;
	/** CSVファイル名（部門推奨ルート(子)） */
	String csvFileNameKoInfo;
	
	/** CSVファイル情報（部門推奨ルート(親)）リスト */
	List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> oyaInfoList;
	/** CSVファイル情報（部門推奨ルート(子)）リスト */
	List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> koInfoList;
	
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
		IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo)session.get(Sessionkey.ROUTE_CSV);
		if (null == sessionInfo) {
			errorList.add("CSVファイルのアップロードからやり直してください。");
			return "error";
		}
		csvFileNameOyaInfo = sessionInfo.getFileNameOyaInfo();
		csvFileNameKoInfo = sessionInfo.getFileNameKoInfo();
		oyaInfoList = sessionInfo.getOyaList();
		koInfoList = sessionInfo.getKoList();
		errorList = sessionInfo.getErrorList();
		
		if (sessionInfo.getStatus() != null)
		{
			// statusがgetできたら非同期処理終了と判定
			status = sessionInfo.getStatus();
			session.remove(Sessionkey.ROUTE_CSV);
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
			IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo sessionInfo = (IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo)session.get(Sessionkey.ROUTE_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileNameOyaInfo = sessionInfo.getFileNameOyaInfo();
			oyaInfoList        = sessionInfo.getOyaList();
			csvFileNameKoInfo = sessionInfo.getFileNameKoInfo();
			koInfoList         = sessionInfo.getKoList();
			errorList = sessionInfo.getErrorList();
			errorList.add("登録処理中です。");
			
			// 処理
			IkkatsuTourokuBumonSuishouRouteThread myThread = makeThread(sessionInfo, EteamCommon.getContextSchemaName());
			myThread.start();
			
			status = UploadStatus.Run;
			return "success";
			
		}
	}

	/**
	 * スレッド作成。デフォルトはnewインスタンス。パッケージカスタマイズ用。
	 * @param sessionInfo	セッション情報
	 * @param schema スキーマ名
	 * @return スレッド
	 */
	protected IkkatsuTourokuBumonSuishouRouteThread makeThread(IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo sessionInfo, String schema){
		return new IkkatsuTourokuBumonSuishouRouteThread(session, sessionInfo, schema);
	}
}
