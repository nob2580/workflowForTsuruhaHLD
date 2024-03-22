package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamConst.Sessionkey;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.gyoumu.kaikei.IkkatsuTourokuTorihikiCSVUploadSessionInfo.UploadStatus;
import eteam.gyoumu.system.NittyuuDataRenkeiThread;
import eteam.gyoumu.user.User;

/**
 * 取引一括登録処理のスレッドクラスです。
 */
public class IkkatsuTourokuTorihikiThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);

// 引数
	/** スキーマ名 */
	String schema;
	/** セッション */
	Map<String, Object> session;
	/** セッション */
	IkkatsuTourokuTorihikiCSVUploadSessionInfo sessionInfo;
	
//＜セッション＞
	/** CSVファイル名 */
	String csvFileName;
	/** CSVファイル情報（部門）リスト */
	List<IkkatsuTourokuTorihikiCSVUploadInfo> torihikiList;
	
//＜部品＞
	/** 仕訳パターンマスターDAO */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	/** エラー情報 */
	List<String> errorList = new ArrayList<String>();
	
	/**
	 * コンストラクタ
	 * @param session セッションマップ
	 * @param sessionInfo セッション情報
	 * @param schema      スキーマ名
	 */
	public IkkatsuTourokuTorihikiThread(Map<String, Object> session, IkkatsuTourokuTorihikiCSVUploadSessionInfo sessionInfo, String schema){
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
		try(EteamConnection connection = EteamConnection.connect()) {
			setConnection(connection);
			csvFileName = sessionInfo.getFileName();
			torihikiList = sessionInfo.getTorihikiList();
			
			//画面入力と同様のチェックを行う
			int count = 0;
			var userId = ((User)session.get(Sessionkey.USER)).getTourokuOrKoushinUserId(); // こっちで取らないとnullになるっぽい？
			for (IkkatsuTourokuTorihikiCSVUploadInfo torihiki : torihikiList) {
				boolean isKoushin = this.shiwakePatternMasterDao.exists(torihiki.getDenpyouKbn(), Integer.parseInt(torihiki.getShiwakeEdano()));
				TorihikiCommonAction torihikiCommonAction = isKoushin ? new TorihikiHenkouAction() : new TorihikiTsuikaAction();
				torihikiCommonAction.setPropertiesFromInfo(torihiki, userId);
				torihikiCommonAction.setGaibuConnection(connection); // 外からセットしているのが分かりやすいように
				if(isKoushin) {
					((TorihikiHenkouAction)torihikiCommonAction).koushin();
				}else {
					((TorihikiTsuikaAction)torihikiCommonAction).touroku();
				}
				count++;
				torihiki.setErrorFlg(false);
				for(String errorStr : torihikiCommonAction.getErrorList()) {
					errorList.add( "#" + Integer.toString(count) + ":" + errorStr);
					torihiki.setErrorFlg(true);
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
			sessionInfo.setTorihikiList(torihikiList);
			sessionInfo.setStatus(UploadStatus.End);
			session.put(Sessionkey.TORIHIKI_CSV, sessionInfo);
			
		}
	}
	
	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
	}
}
