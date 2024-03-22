package eteam.gyoumu.bumonkanri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.bumonkanri.IkkatsuTourokuBumonCSVUploadSessionInfo.UploadStatus;
import eteam.gyoumu.system.NittyuuDataRenkeiThread;
import eteam.gyoumu.user.User;

/**
 * 部門一括登録処理のスレッドクラスです。
 */
public class IkkatsuTourokuBumonThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);
	
	// 引数
		/** スキーマ名 */
		String schema;
		/** セッション */
		Map<String, Object> session;
		/** セッション */
		IkkatsuTourokuBumonCSVUploadSessionInfo sessionInfo;
		
	//＜セッション＞
		/** CSVファイル名 */
		String csvFileName;
		/** CSVファイル情報（部門）リスト */
		List<IkkatsuTourokuBumonCSVUploadInfo> bumonList;
		
	//＜部品＞
		/** 取引系 */
		BumonUserKanriCategoryLogic bumonUserLogic;
		/** エラー情報 */
		List<String> errorList = new ArrayList<String>(); 
	
	/**
	 * コンストラクタ
	 * @param session セッションマップ
	 * @param sessionInfo セッション情報
	 * @param schema      スキーマ名
	 */
	public IkkatsuTourokuBumonThread(Map<String, Object> session, IkkatsuTourokuBumonCSVUploadSessionInfo sessionInfo, String schema){
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
			BumonHenkouAction myBumonHenkouAction;
			setConnection(connection);
			bumonList = sessionInfo.getBumonList();
			csvFileName = sessionInfo.getFileName();
			
			//先にCSVの同一部門コードのデータをまとめておく
			Map<String, List<IkkatsuTourokuBumonCSVUploadInfo>> bumonCdMap = new HashMap<String, List<IkkatsuTourokuBumonCSVUploadInfo>>(); //部門データ格納用
			Map<String, List<Integer>> bumonGyobanMap = new HashMap<String, List<Integer>>(); //部門コード毎行番号リスト格納用
			
			List<String> oyabumonList = new ArrayList<String>();
			List<String> addbumonCdList = new ArrayList<String>();
			
			//部門コード毎の部門データ・行番号リストの作成
			Integer gyoCnt = 0;
			for (IkkatsuTourokuBumonCSVUploadInfo bumon : bumonList) {
				bumon.setErrorFlg(false);
				String bumonCd = bumon.getBumonCd();
				List<IkkatsuTourokuBumonCSVUploadInfo> bumonDatList;
				List<Integer> bumonGyobanList;
				if(bumonCdMap.containsKey(bumonCd)){
					bumonDatList = bumonCdMap.get(bumonCd);
					bumonDatList.add(bumon);
					bumonGyobanList = bumonGyobanMap.get(bumonCd);
					bumonGyobanList.add(gyoCnt);
				}else{
					bumonDatList = new ArrayList<IkkatsuTourokuBumonCSVUploadInfo>();
					bumonDatList.add(bumon);
					bumonCdMap.put(bumonCd,bumonDatList);
					bumonGyobanList = new ArrayList<Integer>();
					bumonGyobanList.add(gyoCnt);
					bumonGyobanMap.put(bumonCd,bumonGyobanList);
				}
				//CSV内で親部門コードとして指定されているコードのリスト作成
				String oyabumonCd = bumon.getOyaBumonCd();
				if(!oyabumonList.contains(oyabumonCd)){
					oyabumonList.add(oyabumonCd);
				}
				if(!addbumonCdList.contains(bumonCd)){
					addbumonCdList.add(bumonCd);
				}
				gyoCnt++;
			}
			
			//既存の全部門コードリストを取得
			List<String> allbumonCdList = bumonUserLogic.selectAllBumonCd();
			for(String str : addbumonCdList){
				if(!allbumonCdList.contains(str)){
					allbumonCdList.add(str);
				}
			}
			
			//親部門として指定したコードが既存の親部門かCSVのどちらにも存在しなければエラーとする
			for(String str : oyabumonList){
				if(!allbumonCdList.contains(str)){
					errorList.add( "親部門コードとして指定されている[" + str + "]の部門が既存部門・追加部門どちらにも存在しません。");   
				}
				if(!errorList.isEmpty()) {
					session.put(Sessionkey.BUMON_CSV, sessionInfo);
					session.remove(Sessionkey.BUMON_CSV);
					connection.rollback();
					// 処理結果をセッションに格納。呼び出し元のactionクラスで取得する
					setResult();
					return;
				}
			}
			
			//変更は部門コード分データの全削除+新規追加により実施しているので、追加も同様に実施する
			//(画面からの追加は単一期限分の登録しか対応していないため)
			// 部門更新アクション側で親部門の存在チェックを無効とする(自身を親部門としていないかはチェックする)
			
			//画面入力と同様のチェックを行う
			for ( String bumonCd : bumonCdMap.keySet() ){
				List<IkkatsuTourokuBumonCSVUploadInfo> bumonDatList = bumonCdMap.get(bumonCd);

				myBumonHenkouAction = new BumonHenkouAction();
				myBumonHenkouAction.setGaibuConnection(connection);
				myBumonHenkouAction.setBumonCd(bumonCd);
				myBumonHenkouAction.setGaibuYobidashiFlag(true);
				
				int length = bumonDatList.size();
				String[] bumonName = new String[length];
				String[] oyaBumonCd = new String[length];
				String[] kigenFrom = new String[length];
				String[] kigenTo = new String[length];
				String[] securityPattern = new String[length];
				
				String[] oyaBumonName = new String[length];
				
				int i = 0;
				for (IkkatsuTourokuBumonCSVUploadInfo bumon : bumonDatList) {
					bumonName[i] = bumon.getBumonName();
					oyaBumonCd[i] = bumon.getOyaBumonCd();
					kigenFrom[i] = bumon.getYuukouKigenFrom();
					kigenTo[i] = bumon.getYuukouKigenTo();
					securityPattern[i] = bumon.getSecurityPattern();
					
					oyaBumonName[i] = ""; //画面表示用なのでダミー
					
					i++;
				}
				
				myBumonHenkouAction.setBumonCd(bumonCd);
				myBumonHenkouAction.setBumonName(bumonName);
				myBumonHenkouAction.setOyaBumonCd(oyaBumonCd);
				myBumonHenkouAction.setKigenFrom(kigenFrom);
				myBumonHenkouAction.setKigenTo(kigenTo);
				myBumonHenkouAction.setSecurityPattern(securityPattern);
				myBumonHenkouAction.setGaibuKoushinUserId(((User)session.get(Sessionkey.USER)).getTourokuOrKoushinUserId());
				
				myBumonHenkouAction.setOyaBumonName(oyaBumonName);
				
				myBumonHenkouAction.henkou();
				
				//エラーメッセージ・エラー該当行番号登録
				if(!myBumonHenkouAction.getErrorList().isEmpty()){
					for(String errorStr : myBumonHenkouAction.getErrorList()) {
						errorList.add( "部門コード[" + bumonCd + "]:" + errorStr);
						//エラーフラグ設定
						Integer cnt = 0;
						List<Integer> gyoList = bumonGyobanMap.get(bumonCd);
						for (IkkatsuTourokuBumonCSVUploadInfo bumon : bumonList) {
							if(gyoList.contains(cnt)){
								bumon.setErrorFlg(true);
							}
							cnt++;
						}
					}
				}
			}
			
			if(errorList.isEmpty()) {
				connection.commit();
			}
			else {
				connection.rollback();
			}
			
			// 処理結果をセッションに格納。呼び出し元のactionクラスで取得する
			setResult();
			
		}
	}
	
	/**
	 * 処理結果をセッションに格納。呼び出し元のactionクラスで取得する
	 */
	protected void setResult() {
		sessionInfo.setErrorList(errorList);
		sessionInfo.setFileName(csvFileName);
		sessionInfo.setBumonList(bumonList);
		sessionInfo.setStatus(UploadStatus.End);
		session.put(Sessionkey.BUMON_CSV, sessionInfo);
	}
	
	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		bumonUserLogic = (BumonUserKanriCategoryLogic)EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
	}
}
