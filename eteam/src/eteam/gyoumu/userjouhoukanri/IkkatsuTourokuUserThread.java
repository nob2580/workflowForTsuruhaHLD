package eteam.gyoumu.userjouhoukanri;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.SecurityLogLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.system.NittyuuDataRenkeiThread;
import eteam.gyoumu.user.User;
import eteam.gyoumu.userjouhoukanri.IkkatsuTourokuUserCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザー一括登録処理のスレッドクラスです。
 */
public class IkkatsuTourokuUserThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);

// 引数
	/** スキーマ名 */
	String schema;
	/** セッション */
	Map<String, Object> session;
	/** セッション */
	IkkatsuTourokuUserCSVUploadSessionInfo sessionInfo;
	
//＜セッション＞
	/** CSVファイル名（ユーザー情報） */
	String csvFileNameUserInfo;
	/** CSVファイル名（所属部門割り当て） */
	String csvFileNameShozokuBumonWariate;
	/** CSVファイル情報（ユーザー情報）リスト */
	List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList;
	/** CSVファイル情報（所属部門割り当て）リスト */
	List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList;
	
//＜部品＞
	/** 部門・ユーザ管理機能ロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** セキュリティログロジック */
	SecurityLogLogic logLogic;
	/** リクエスト情報 */
	@Getter @Setter
	HttpServletRequest request;
	/** 接続元IP */
	@Getter @Setter
	String ip;
	/** 接続元IP(X-Forwarded-For) */
	@Getter @Setter
	String ipx;
	/** エラー情報 */
	List<String> errorList = new ArrayList<String>();
	
	/**
	 * コンストラクタ
	 * @param session セッションマップ
	 * @param sessionInfo セッション情報
	 * @param schema      スキーマ名
	 * @param request セッション情報
	 * @param ip 接続元IP
	 * @param ipx 接続元IP(X-Forwarded-For)
	 */
	public IkkatsuTourokuUserThread(Map<String, Object> session, IkkatsuTourokuUserCSVUploadSessionInfo sessionInfo, String schema, HttpServletRequest request, String ip, String ipx){
		this.session = session;
		this.sessionInfo = sessionInfo;
		this.schema = schema;
		this.request = request;
		this.ip = ip;
		this.ipx = ipx;
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
			UserJouhouTsuikaAction myUserTsuikaAction;
			UserJouhouAction myUserHenkouAction;
			setConnection(connection);
			csvFileNameUserInfo = sessionInfo.getFileNameUserInfo();
			userInfoList        = sessionInfo.getUserInfoList();
			csvFileNameShozokuBumonWariate = sessionInfo.getFileNameShozokuBumonWariate();
			shozokuBumonWariateList         = sessionInfo.getShozokuBumonWariateList();
			
			//画面入力と同様のチェックを行う
			int count=0;
			for (IkkatsuTourokuUserCSVUploadInfoUserInfo userInfo : userInfoList) {
				GMap usrInfoMap = bumonUserLogic.selectUserJouhou(userInfo.getUserId());
				if(usrInfoMap != null){
					myUserHenkouAction = new UserJouhouAction();
					myUserHenkouAction.setGaibuConnection(connection);
					myUserHenkouAction.setUserId(userInfo.getUserId());
					myUserHenkouAction.setShainNo(userInfo.getShainNo());
					myUserHenkouAction.setUserSei(userInfo.getUserSei());
					myUserHenkouAction.setUserMei(userInfo.getUserMei());
					myUserHenkouAction.setMailAddress(userInfo.getMailAddress());
					myUserHenkouAction.setSecurityPattern(userInfo.getSecurityPattern());
					myUserHenkouAction.setSecurityWfonlyFlg(("1".equals(userInfo.getSecurityWfonlyFlg()) ? "1":null));
					myUserHenkouAction.setUserYuukoukigenFrom(userInfo.getYuukouKigenFrom());
					myUserHenkouAction.setUserYuukoukigenTo(userInfo.getYuukouKigenTo());
					myUserHenkouAction.setDairiKihyouFlag(("1".equals(userInfo.getDairikihyouFlg()) ? "1":null));
					myUserHenkouAction.setHoujinCardRiyouFlag(("1".equals(userInfo.getHoujinCardRiyouFlag()) ? "1":null));
					myUserHenkouAction.setHoujinCardShikibetsuyouNum(userInfo.getHoujinCardShikibetsuyouNum());
					myUserHenkouAction.setShouninRouteHenkouLevel(userInfo.getShouninRouteHenkouLevel());
					myUserHenkouAction.setMaruhiKengenFlag(("1".equals(userInfo.getMaruhiKengenFlg()) ? "1":null));
					myUserHenkouAction.setMaruhiKaijyoFlag(("1".equals(userInfo.getMaruhiKaijyoFlg()) ? "1":null));
					myUserHenkouAction.setZaimuKyotenNyuryokuOnlyFlg(("1".equals(userInfo.getZaimuKyotenNyuryokuOnlyFlg()) ? "1":null));
					int dataSize=0;
					for (IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate : shozokuBumonWariateList) {
						String userId  = shozokuBumonWariate.getUserId();
						if(userId.equals(userInfo.getUserId())) {
							dataSize++;
						}
					}
					if(dataSize>0) {
						String[] bumonCd               = new String[dataSize];
						String[] bumonName             = new String[dataSize];
						String[] bumonRoleId           = new String[dataSize];
						String[] daihyouFutanBumonCd   = new String[dataSize];
						String[] daihyouFutanBumonName = new String[dataSize];
						String[] bumonYuukoukigenFrom  = new String[dataSize];
						String[] bumonYuukoukigenTo    = new String[dataSize];
						
						int countData =0;
						for (IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate : shozokuBumonWariateList) {
							String userId  = shozokuBumonWariate.getUserId();
							if(userId.equals(userInfo.getUserId())) {
								bumonCd[countData]               = shozokuBumonWariate.getBumonCd();
								if(bumonUserLogic.selectValidShozokuBumon(shozokuBumonWariate.getBumonCd(),myUserHenkouAction.toDate(shozokuBumonWariate.getYuukouKigenFrom())) != null) {
									bumonName[countData]         = (String)bumonUserLogic.selectValidShozokuBumon(shozokuBumonWariate.getBumonCd(),myUserHenkouAction.toDate(shozokuBumonWariate.getYuukouKigenFrom())).get("bumon_name");
								}
								bumonRoleId[countData]           = shozokuBumonWariate.getBumonRoleId();
								daihyouFutanBumonCd[countData]   = shozokuBumonWariate.getDaihyouFutanBumonCd();
								daihyouFutanBumonName[countData] = ""; //チェックとしては不要
								bumonYuukoukigenFrom[countData]  = shozokuBumonWariate.getYuukouKigenFrom();
								bumonYuukoukigenTo[countData]    = shozokuBumonWariate.getYuukouKigenTo();
								countData++;
							}
						}
						myUserHenkouAction.setBumonCd(bumonCd);
						myUserHenkouAction.setBumonName(bumonName);
						myUserHenkouAction.setBumonRoleId(bumonRoleId);
						myUserHenkouAction.setDaihyouFutanBumonCd(daihyouFutanBumonCd);
						myUserHenkouAction.setDaihyouFutanBumonName(daihyouFutanBumonName);
						myUserHenkouAction.setBumonYuukoukigenFrom(bumonYuukoukigenFrom);
						myUserHenkouAction.setBumonYuukoukigenTo(bumonYuukoukigenTo);
					}
					
					myUserHenkouAction.setAccountTmpLockFlag("1".equals(usrInfoMap.get("tmp_lock_flg")) ? "1" : null);
					myUserHenkouAction.setAccountLockFlag("1".equals(usrInfoMap.get("lock_flg")) ? "1" : null);
					
					myUserHenkouAction.setGaibuYobidashiFlag(true);
					myUserHenkouAction.setLoginUserInfo((User)session.get(Sessionkey.USER));
					myUserHenkouAction.setRequest(request);
					myUserHenkouAction.setIp(ip);
					myUserHenkouAction.setIpx(ipx);
					myUserHenkouAction.koushin();
					count++;
					for(String errorStr : myUserHenkouAction.getErrorList()) {
						errorList.add( "ユーザー情報 #" + Integer.toString(count) + ":" + errorStr);
						userInfo.setErrorFlg(true);
					}
				}
				else
				{
					myUserTsuikaAction = new UserJouhouTsuikaAction();
					myUserTsuikaAction.setGaibuConnection(connection);
					myUserTsuikaAction.setUserId(userInfo.getUserId());
					myUserTsuikaAction.setShainNo(userInfo.getShainNo());
					myUserTsuikaAction.setUserSei(userInfo.getUserSei());
					myUserTsuikaAction.setUserMei(userInfo.getUserMei());
					myUserTsuikaAction.setMailAddress(userInfo.getMailAddress());
					myUserTsuikaAction.setSecurityPattern(userInfo.getSecurityPattern());
					myUserTsuikaAction.setSecurityWfonlyFlg(("1".equals(userInfo.getSecurityWfonlyFlg()) ? "1":null));
					myUserTsuikaAction.setUserYuukoukigenFrom(userInfo.getYuukouKigenFrom());
					myUserTsuikaAction.setUserYuukoukigenTo(userInfo.getYuukouKigenTo());
					myUserTsuikaAction.setDairiKihyouFlag(("1".equals(userInfo.getDairikihyouFlg()) ? "1":null));
					myUserTsuikaAction.setHoujinCardRiyouFlag(("1".equals(userInfo.getHoujinCardRiyouFlag()) ? "1":null));
					myUserTsuikaAction.setHoujinCardShikibetsuyouNum(userInfo.getHoujinCardShikibetsuyouNum());
					myUserTsuikaAction.setShouninRouteHenkouLevel(userInfo.getShouninRouteHenkouLevel());
					myUserTsuikaAction.setMaruhiKengenFlag(("1".equals(userInfo.getMaruhiKengenFlg()) ? "1":null));
					myUserTsuikaAction.setMaruhiKaijyoFlag(("1".equals(userInfo.getMaruhiKaijyoFlg()) ? "1":null));
					myUserTsuikaAction.setZaimuKyotenNyuryokuOnlyFlg(("1".equals(userInfo.getZaimuKyotenNyuryokuOnlyFlg()) ? "1":null));
					myUserTsuikaAction.setPassword("password");
					
					int dataSize=0;
					for (IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate : shozokuBumonWariateList) {
						String userId  = shozokuBumonWariate.getUserId();
						if(userId.equals(userInfo.getUserId())) {
							dataSize++;
						}
					}
					if(dataSize>0) {
						String[] bumonCd               = new String[dataSize];
						String[] bumonName             = new String[dataSize];
						String[] bumonRoleId           = new String[dataSize];
						String[] daihyouFutanBumonCd   = new String[dataSize];
						String[] daihyouFutanBumonName = new String[dataSize];
						String[] bumonYuukoukigenFrom  = new String[dataSize];
						String[] bumonYuukoukigenTo    = new String[dataSize];
						String[] bumonWariateGyobangou = new String[dataSize];
						
						int countData =0;
						int countBumon=0;
						for (IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate : shozokuBumonWariateList) {
							countBumon++;
							String userId  = shozokuBumonWariate.getUserId();
							if(userId.equals(userInfo.getUserId())) {
								bumonCd[countData]               = shozokuBumonWariate.getBumonCd();
								if(bumonUserLogic.selectValidShozokuBumon(shozokuBumonWariate.getBumonCd(),myUserTsuikaAction.toDate(shozokuBumonWariate.getYuukouKigenFrom())) != null) {
									bumonName[countData]         = (String)bumonUserLogic.selectValidShozokuBumon(shozokuBumonWariate.getBumonCd(),myUserTsuikaAction.toDate(shozokuBumonWariate.getYuukouKigenFrom())).get("bumon_name");
								}
								bumonRoleId[countData]           = shozokuBumonWariate.getBumonRoleId();
								daihyouFutanBumonCd[countData]   = shozokuBumonWariate.getDaihyouFutanBumonCd();
								daihyouFutanBumonName[countData] = ""; //チェックとしては不要
								bumonYuukoukigenFrom[countData]  = shozokuBumonWariate.getYuukouKigenFrom();
								bumonYuukoukigenTo[countData]    = shozokuBumonWariate.getYuukouKigenTo();
								bumonWariateGyobangou[countData] = String.valueOf(countBumon);
								countData++;
							}
						}
						myUserTsuikaAction.setBumonCd(bumonCd);
						myUserTsuikaAction.setBumonName(bumonName);
						myUserTsuikaAction.setBumonRoleId(bumonRoleId);
						myUserTsuikaAction.setDaihyouFutanBumonCd(daihyouFutanBumonCd);
						myUserTsuikaAction.setDaihyouFutanBumonName(daihyouFutanBumonName);
						myUserTsuikaAction.setBumonYuukoukigenFrom(bumonYuukoukigenFrom);
						myUserTsuikaAction.setBumonYuukoukigenTo(bumonYuukoukigenTo);
					}
					myUserTsuikaAction.setGaibuYobidashiFlag(true);
					myUserTsuikaAction.setLoginUserInfo((User)session.get(Sessionkey.USER));
					myUserTsuikaAction.setRequest(request);
					myUserTsuikaAction.setIp(ip);
					myUserTsuikaAction.setIpx(ipx);
					myUserTsuikaAction.touroku();
					count++;
					for(String errorStr : myUserTsuikaAction.getErrorList()) {
						errorList.add( "ユーザー情報 #" + Integer.toString(count) + ":" + errorStr);
						userInfo.setErrorFlg(true);
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
			sessionInfo.setFileNameUserInfo(csvFileNameUserInfo);
			sessionInfo.setUserInfoList(userInfoList);
			sessionInfo.setFileNameShozokuBumonWariate(csvFileNameShozokuBumonWariate);
			sessionInfo.setShozokuBumonWariateList(shozokuBumonWariateList);
			sessionInfo.setStatus(UploadStatus.End);
			session.put(Sessionkey.USER_CSV, sessionInfo);
			
		}
	}
	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		bumonUserLogic = (BumonUserKanriCategoryLogic)EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
	}
}

