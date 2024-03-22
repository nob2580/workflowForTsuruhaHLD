package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.system.NittyuuDataRenkeiThread;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo.UploadStatus;

/**
 * 部門推奨ルート一括登録処理のスレッドクラスです。
 */
public class IkkatsuTourokuBumonSuishouRouteThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);

	// 引数
		/** スキーマ名 */
		String schema;
		/** セッション */
		Map<String, Object> session;
		/** セッション */
		IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo sessionInfo;
		
	//＜セッション＞
		/** CSVファイル名(部門推奨ルート(親)) */
		String csvFileNameOyaInfo;
		/** CSVファイル名(部門推奨ルート(子)) */
		String csvFileNameKoInfo;
		/** CSVファイル情報（部門推奨ルート(親)）リスト */
		List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya> oyaInfoList;
		/** CSVファイル情報（部門推奨ルート(子)）リスト */
		List<IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo> koInfoList;
		
	//＜部品＞
		/** 部門・ユーザ管理機能ロジック */
		BumonUserKanriCategoryLogic bumonUserLogic;
		/** ワークフローSELECTロジック */
		WorkflowCategoryLogic wfLogic;
		/** エラー情報 */
		List<String> errorList = new ArrayList<String>(); 
	
	/**
	 * コンストラクタ
	 * @param session セッションマップ
	 * @param sessionInfo セッション情報
	 * @param schema      スキーマ名
	 */
	public IkkatsuTourokuBumonSuishouRouteThread(Map<String, Object> session, IkkatsuTourokuBumonSuishouRouteCSVUploadSessionInfo sessionInfo, String schema){
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
			BumonSuishouRouteTsuikaAction myRouteTsuikaAction;
			BumonSuishouRouteHenkouAction myRouteHenkouAction;
			setConnection(connection);
			csvFileNameOyaInfo = sessionInfo.getFileNameOyaInfo();
			oyaInfoList        = sessionInfo.getOyaList();
			csvFileNameKoInfo = sessionInfo.getFileNameKoInfo();
			koInfoList         = sessionInfo.getKoList();
			
			//画面入力と同様のチェックを行う
			int count=0;
			for (IkkatsuTourokuBumonSuishouRouteCSVUploadInfoOya oyaInfo : oyaInfoList) {
				
				GMap routeMap = wfLogic.selectBumonSuishouRouteEdano(oyaInfo.getDenpyouKbn(),oyaInfo.getBumonCd(),oyaInfo.getEdaNo());
				if(routeMap != null){
					//既存データ更新
					myRouteHenkouAction = this.makeHenkouAction();
					myRouteHenkouAction.setGaibuConnection(connection);
					myRouteHenkouAction.setDenpyouKbn(oyaInfo.getDenpyouKbn());
					myRouteHenkouAction.setDefaultFlg(oyaInfo.getDefaultFlg());
					myRouteHenkouAction.setShiwakeEdaNo(oyaInfo.getShiwakeEdaNo());
					myRouteHenkouAction.setTorihikiName(new String[oyaInfo.getShiwakeEdaNo().length]);
					myRouteHenkouAction.setBumonCd(oyaInfo.getBumonCd());
					if(bumonUserLogic.selectValidShozokuBumon(oyaInfo.getBumonCd(),myRouteHenkouAction.toDate(oyaInfo.getYuukouKigenFrom())) != null){
						myRouteHenkouAction.setBumon((String)bumonUserLogic.selectValidShozokuBumon(oyaInfo.getBumonCd(),myRouteHenkouAction.toDate(oyaInfo.getYuukouKigenFrom())).get("bumon_name"));
					}
					myRouteHenkouAction.setEdano(oyaInfo.getEdaNo());
					myRouteHenkouAction.setKingakuFrom(oyaInfo.getKingakuFrom());
					myRouteHenkouAction.setKingakuTo(oyaInfo.getKingakuTo());
					myRouteHenkouAction.setYuukouKigenFrom(oyaInfo.getYuukouKigenFrom());
					myRouteHenkouAction.setYuukouKigenTo(oyaInfo.getYuukouKigenTo());
					
					int dataSize=0;
					for (IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo : koInfoList) {
						String denpyouKbn  = koInfo.getDenpyouKbn();
						String bumonCd  = koInfo.getBumonCd();
						String edaNo  = koInfo.getEdaNo();
						if(    denpyouKbn.equals(oyaInfo.getDenpyouKbn())
								&& bumonCd.equals(oyaInfo.getBumonCd())
								&& edaNo.equals(oyaInfo.getEdaNo()) ) {
							dataSize++;
						}
					}
					if(dataSize>0) {
						String[] bumonRole               = new String[dataSize];
						String[] bumonRoleCd             = new String[dataSize];
						String[] shoriKengen             = new String[dataSize];
						String[] gougiCd                 = new String[dataSize];
						String[] gougiEdano              = new String[dataSize];
						
						//こちらは後で入れる
						String[] gougiName = new String[dataSize];
						boolean[] bumonRoleHyouji = new boolean[dataSize];
						
						int countData =0;
						for (IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo : koInfoList) {
							String denpyouKbn  = koInfo.getDenpyouKbn();
							String bumonCd  = koInfo.getBumonCd();
							String edaNo  = koInfo.getEdaNo();
							if(    denpyouKbn.equals(oyaInfo.getDenpyouKbn())
									&& bumonCd.equals(oyaInfo.getBumonCd())
									&& edaNo.equals(oyaInfo.getEdaNo()) ) {
								if(!myRouteHenkouAction.isEmpty(koInfo.getBumonCd())){
									
									GMap bmMap =  bumonUserLogic.selectValidShozokuBumon(koInfo.getBumonCd(),myRouteHenkouAction.toDate(oyaInfo.getYuukouKigenFrom()));
									if(bmMap != null && bmMap.get("bumon_name") != null){
										bumonRole[countData]        =  (String)bmMap.get("bumon_name");
									}else{
										bumonRole[countData]        = "";
									}
									bumonRoleCd[countData]      =  koInfo.getBumonRoleId();
								}else{
									bumonRole[countData]        = "";
									bumonRoleCd[countData]      = "";
								}
								shoriKengen[countData]      =  koInfo.getShouninhoriKengenNo();
								if(!myRouteHenkouAction.isEmpty(koInfo.getGougiPatternNo())){
									gougiCd[countData]      = koInfo.getGougiPatternNo();
									gougiEdano[countData]   = koInfo.getGougiEdano();
									GMap ggMap =  bumonUserLogic.findGougiBusho(koInfo.getGougiPatternNo());
									if(ggMap != null && ggMap.get("gougi_name") != null){
										gougiName[countData]    = (String)ggMap.get("gougi_name");
									}else{
										gougiName[countData]    = "";
									}
								}else{
									gougiCd[countData]      = "";
									gougiEdano[countData]   = "";
									gougiName[countData]    = "";
									
								}
								bumonRoleHyouji[countData] = !(myRouteHenkouAction.isEmpty(bumonRoleCd[countData]));

								countData++;
							}
						}
						myRouteHenkouAction.setBumonRole(bumonRole);
						myRouteHenkouAction.setBumonRoleCd(bumonRoleCd);
						myRouteHenkouAction.setShoriKengen(shoriKengen);
						myRouteHenkouAction.setGougiCd(gougiCd);
						myRouteHenkouAction.setGougiEdano(gougiEdano);
						myRouteHenkouAction.setGougiName(gougiName);
						myRouteHenkouAction.setBumonRoleHyouji(bumonRoleHyouji);
					}else{
						//対応する子データが存在しない場合はエラー
						count++;
						errorList.add( "部門推奨ルート#" + Integer.toString(count) + ":CSVファイル(部門推奨ルート子)に該当データがありません。");
						oyaInfo.setErrorFlg(true);
						continue;
					}
					
					myRouteHenkouAction.setGaibuYobidashiFlag(true);
					myRouteHenkouAction.setLoginUserInfo((User)session.get(Sessionkey.USER));
					myRouteHenkouAction.koushin();
					count++;
					for(String errorStr : myRouteHenkouAction.getErrorList()) {
						errorList.add( "部門推奨ルート#" + Integer.toString(count) + ":" + errorStr);
						oyaInfo.setErrorFlg(true);
					}
				}
				else
				{
					//新規データ登録
					myRouteTsuikaAction =this.makeTsuikaAction();
					myRouteTsuikaAction.setGaibuConnection(connection);
					myRouteTsuikaAction.setDenpyouKbn(oyaInfo.getDenpyouKbn());
					myRouteTsuikaAction.setDefaultFlg(oyaInfo.getDefaultFlg());
					myRouteTsuikaAction.setShiwakeEdaNo(oyaInfo.getShiwakeEdaNo());
					myRouteTsuikaAction.setTorihikiName(new String[oyaInfo.getShiwakeEdaNo().length]);
					myRouteTsuikaAction.setBumonCd(oyaInfo.getBumonCd());
					if(bumonUserLogic.selectValidShozokuBumon(oyaInfo.getBumonCd(),myRouteTsuikaAction.toDate(oyaInfo.getYuukouKigenFrom())) != null){
						myRouteTsuikaAction.setBumon((String)bumonUserLogic.selectValidShozokuBumon(oyaInfo.getBumonCd(),myRouteTsuikaAction.toDate(oyaInfo.getYuukouKigenFrom())).get("bumon_name"));
					}
					myRouteTsuikaAction.setGaibuEdaNo(oyaInfo.getEdaNo());
					myRouteTsuikaAction.setKingakuFrom(oyaInfo.getKingakuFrom());
					myRouteTsuikaAction.setKingakuTo(oyaInfo.getKingakuTo());
					myRouteTsuikaAction.setYuukouKigenFrom(oyaInfo.getYuukouKigenFrom());
					myRouteTsuikaAction.setYuukouKigenTo(oyaInfo.getYuukouKigenTo());
					
					int dataSize=0;
					for (IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo : koInfoList) {
						String denpyouKbn  = koInfo.getDenpyouKbn();
						String bumonCd  = koInfo.getBumonCd();
						String edaNo  = koInfo.getEdaNo();
						if(    denpyouKbn.equals(oyaInfo.getDenpyouKbn())
								&& bumonCd.equals(oyaInfo.getBumonCd())
								&& edaNo.equals(oyaInfo.getEdaNo()) ) {
							dataSize++;
						}
					}
					if(dataSize>0) {
						String[] bumonRole               = new String[dataSize];
						String[] bumonRoleCd             = new String[dataSize];
						String[] shoriKengen             = new String[dataSize];
						String[] gougiCd                 = new String[dataSize];
						String[] gougiEdano              = new String[dataSize];
						
						//こちらは後で入れる
						String[] gougiName = new String[dataSize];
						boolean[] bumonRoleHyouji = new boolean[dataSize];
						
						int countData =0;
						for (IkkatsuTourokuBumonSuishouRouteCSVUploadInfoKo koInfo : koInfoList) {
							String denpyouKbn  = koInfo.getDenpyouKbn();
							String bumonCd  = koInfo.getBumonCd();
							String edaNo  = koInfo.getEdaNo();
							if(    denpyouKbn.equals(oyaInfo.getDenpyouKbn())
									&& bumonCd.equals(oyaInfo.getBumonCd())
									&& edaNo.equals(oyaInfo.getEdaNo()) ) {
								if(!myRouteTsuikaAction.isEmpty(koInfo.getBumonCd())){
									
									GMap bmMap =  bumonUserLogic.selectValidShozokuBumon(koInfo.getBumonCd());
									if(bmMap != null && bmMap.get("bumon_name") != null){
										bumonRole[countData]        =  (String)bmMap.get("bumon_name");
									}else{
										bumonRole[countData]        = "";
									}
									bumonRoleCd[countData]      =  koInfo.getBumonRoleId();
								}else{
									bumonRole[countData]        = "";
									bumonRoleCd[countData]      = "";
								}
								shoriKengen[countData]      =  koInfo.getShouninhoriKengenNo();
								if(!myRouteTsuikaAction.isEmpty(koInfo.getGougiPatternNo())){
									gougiCd[countData]      = koInfo.getGougiPatternNo();
									gougiEdano[countData]   = koInfo.getGougiEdano();
									GMap ggMap =  bumonUserLogic.findGougiBusho(koInfo.getGougiPatternNo());
									if(ggMap != null && ggMap.get("gougi_name") != null){
										gougiName[countData]    = (String)ggMap.get("gougi_name");
									}else{
										gougiName[countData]    = "";
									}
								}else{
									gougiCd[countData]      = "";
									gougiEdano[countData]   = "";
									gougiName[countData]    = "";
									
								}
								bumonRoleHyouji[countData] = !(myRouteTsuikaAction.isEmpty(bumonRoleCd[countData]));
								countData++;
							}
						}
						myRouteTsuikaAction.setBumonRole(bumonRole);
						myRouteTsuikaAction.setBumonRoleCd(bumonRoleCd);
						myRouteTsuikaAction.setShoriKengen(shoriKengen);
						myRouteTsuikaAction.setGougiCd(gougiCd);
						myRouteTsuikaAction.setGougiEdano(gougiEdano);
						myRouteTsuikaAction.setGougiName(gougiName);
						myRouteTsuikaAction.setBumonRoleHyouji(bumonRoleHyouji);

					}else{
						//対応する子データが存在しない場合はエラー
						count++;
						errorList.add( "部門推奨ルート#" + Integer.toString(count) + ":CSVファイル(部門推奨ルート子)に該当データがありません。");
						oyaInfo.setErrorFlg(true);
						continue;
					}
					myRouteTsuikaAction.setGaibuYobidashiFlag(true);
					myRouteTsuikaAction.setLoginUserInfo((User)session.get(Sessionkey.USER));
					myRouteTsuikaAction.touroku();
					count++;
					oyaInfo.setErrorFlg(false);
					for(String errorStr : myRouteTsuikaAction.getErrorList()) {
						errorList.add( "部門推奨ルート#" + Integer.toString(count) + ":" + errorStr);
						oyaInfo.setErrorFlg(true);
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
			sessionInfo.setErrorList(errorList);
			sessionInfo.setFileNameOyaInfo(csvFileNameOyaInfo);
			sessionInfo.setFileNameKoInfo(csvFileNameKoInfo);
			sessionInfo.setOyaList(oyaInfoList);
			sessionInfo.setKoList(koInfoList);
			sessionInfo.setStatus(UploadStatus.End);
			session.put(Sessionkey.ROUTE_CSV, sessionInfo);
						
		}
	}
	
	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		bumonUserLogic = (BumonUserKanriCategoryLogic)EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfLogic = (WorkflowCategoryLogic)EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
	}

	/**
	 * 個別追加のAction new。カスタマイズ用にメソッド化
	 * @return	追加のAction
	 */
	protected BumonSuishouRouteTsuikaAction makeTsuikaAction() {
		return new BumonSuishouRouteTsuikaAction();
	}

	/**
	 * 個別変更のAction new。カスタマイズ用にメソッド化
	 * @return	変更のAction
	 */
	protected BumonSuishouRouteHenkouAction makeHenkouAction() {
		return new BumonSuishouRouteHenkouAction();
	}
}
