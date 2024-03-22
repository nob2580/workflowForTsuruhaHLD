package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamConst.Sessionkey;
import eteam.gyoumu.kaikei.IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo.UploadStatus;
import eteam.gyoumu.system.NittyuuDataRenkeiThread;
import eteam.gyoumu.user.User;

/**
 * 部門別取引一括登録処理のスレッドクラスです。
 */
public class IkkatsuTourokuBumonbetsuTorihikiThread extends Thread {
	
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);

	// 引数
		/** スキーマ名 */
		String schema;
		/** セッション */
		Map<String, Object> session;
		/** セッション */
		IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo sessionInfo;
		
	//＜セッション＞
		/** CSVファイル名 */
		String csvFileName;
		/** CSVファイル情報（部門別取引）リスト */
		List<IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo> bumonbetsuTorihikiList;
		
	//＜部品＞
		/** エラー情報 */
		List<String> errorList = new ArrayList<String>();
		
		/**
		 * コンストラクタ
		 * @param session セッションマップ
		 * @param sessionInfo セッション情報
		 * @param schema      スキーマ名
		 */
		public IkkatsuTourokuBumonbetsuTorihikiThread(Map<String, Object> session, IkkatsuTourokuBumonbetsuTorihikiCSVUploadSessionInfo sessionInfo, String schema){
			this.session = session;
			this.sessionInfo = sessionInfo;
			this.schema = schema;
		}
	
		/**
		 * 各バッチ処理を実行する。
		 */
		public void run(){
			
			try{
				// スキーマを起動元のコンテキストから渡されたものに
				Map<String, String> threadMap = EteamThreadMap.get();
				threadMap.put(SYSTEM_PROP.SCHEMA, schema);
				
				// 登録処理
				this.touroku();
				
			}catch (Throwable e){
				log.error("エラー発生", e);
			}
		}
	
	/**
	 * 登録イベント
	 */
	public void touroku(){
		EteamConnection connection = EteamConnection.connect();
		try {
			BumonbetsuTorihikiIchiranAction myBumonTorihikiAction;
			//セション情報を取得する。
			csvFileName = sessionInfo.getFileName();
			bumonbetsuTorihikiList = sessionInfo.getBumonbetsuTorihikiList();
			
			//画面入力と同様のチェックを行う
			int count=0;
			for (IkkatsuTourokuBumonbetsuTorihikiCSVUploadInfo bumonTR : bumonbetsuTorihikiList) {
				
				myBumonTorihikiAction = new BumonbetsuTorihikiIchiranAction();
				myBumonTorihikiAction.setGaibuConnection(connection);
				myBumonTorihikiAction.setShozokuBumonCd(bumonTR.getBumonCd());
				myBumonTorihikiAction.setDenpyouKbn(bumonTR.getDenpyouKbn());
				myBumonTorihikiAction.setShiwakeEdano(bumonTR.getShiwakeEdano());
				myBumonTorihikiAction.setGaibuYobidashiFlag(true);
				myBumonTorihikiAction.setGyoumuRoleShozokuBumonCd(((User)session.get(Sessionkey.USER)).getGyoumuRoleShozokuBumonCd());
				myBumonTorihikiAction.setGaibuKoushinUserId(((User)session.get(Sessionkey.USER)).getTourokuOrKoushinUserId());
				myBumonTorihikiAction.koushin();
				count++;
				bumonTR.setErrorFlg(false);
				for(String errorStr : myBumonTorihikiAction.getErrorList()) {
					errorList.add( "#" + Integer.toString(count) + ":" + errorStr);
					bumonTR.setErrorFlg(true);
				}
			}
			
			if(errorList.isEmpty()) {
				connection.commit();
			}
			else {
				connection.rollback();
			}
			
			// 処理結果をセッションに格納。呼び出し元のactionクラスで取得する
			sessionInfo.setErrorList(errorList);
			sessionInfo.setFileName(csvFileName);
			sessionInfo.setBumonbetsuTorihikiList(bumonbetsuTorihikiList);
			sessionInfo.setStatus(UploadStatus.End);
			session.put(Sessionkey.BUMONBETSUTORIHIKI_CSV, sessionInfo);
			
		}finally{
			connection.close();
		}
	}
}
