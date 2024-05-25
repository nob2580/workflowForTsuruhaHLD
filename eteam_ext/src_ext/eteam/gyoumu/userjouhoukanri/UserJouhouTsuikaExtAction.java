package eteam.gyoumu.userjouhoukanri;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.PasswordValidTerm;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UserJouhouTsuikaExtAction extends UserJouhouTsuikaAction {
	//カスタマイズ
	/** 会社切替設定のスキーマコード */
	String[] kaishaKirikaeCd;
	/** 会社切替設定のスキーマ名 */
	String[] kaishaKirikaeName;
	/** 会社切替設定の有効期限開始日 */
	String[] kaishaKirikaeYuukoukigenFrom;
	/** 会社切替設定の有効期限終了日 */
	String[] kaishaKirikaeYuukoukigenTo;
	//カスタマイズ
	/** 会社切替設定表示制御 */
	boolean useKaishaKirikaeSettei = false;
	
	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){

		EteamConnection connection;
		if(!gaibuYobidashiFlag) {
			connection = EteamConnection.connect();
		}
		else {
			connection = getGaibuConnection();
		}
		try{
			initParts(connection);
			displaySeigyo(connection);

			//1.入力チェック
			hissuCheck(2);
			formatCheck();

			//2.データ存在チェック なし

			//3.アクセス可能チェック（権限チェック） なし

			//4.処理
			//所属部門と業務ロールをチェックする
			if (bumonCd == null) {
				errorList.add("所属部門管理が選択されていません。");
			} else {
				for(int i = 0; i < bumonCd.length; i++) {
					if(isEmpty(bumonRoleId[i])){
						errorList.add("所属部門管理：" + (i + 1) + "行目：" + bumonName[i] + "：役割を選択して下さい。");
					}

					if(this.userDaihyouFutanBumonMinyuuryokuCheck.equals("1") && this.isEmpty(this.daihyouFutanBumonCd[i]))
					{
						errorList.add("所属部門管理：" + (i + 1) + "行目：" + this.bumonName[i] + "：代表負担部門を入力して下さい。");
					}

					if(isEmpty(bumonYuukoukigenFrom[i])){
						errorList.add("所属部門管理：" + (i + 1) + "行目：" + bumonName[i] + "：有効期限開始日を入力して下さい。");
					} else {
						checkDate(bumonYuukoukigenFrom[i], "所属部門管理：" + (i + 1) + "行目：" + bumonName[i] + "：有効期限開始日", false);
						checkDate(bumonYuukoukigenTo[i],   "所属部門管理：" + (i + 1) + "行目：" + bumonName[i] + "：有効期限終了日", false);
					}
				}
			}

			//業務ロールの入力チェック
			if (gyoumuRoleId != null) {
				for(int i = 0; i < gyoumuRoleId.length; i++) {
					if(isEmpty(gyoumuRoleYuukoukigenFrom[i])){
						errorList.add("業務ロール管理：" + (i + 1) + "行目：" + gyoumuRoleName[i] +"：有効期限開始日を入力して下さい。");
					} else {
						checkDate(gyoumuRoleYuukoukigenFrom[i], "業務ロール管理：" + (i + 1) + "行目：" + gyoumuRoleName[i] +"：有効期限開始日", false);
						checkDate(gyoumuRoleYuukoukigenTo[i],   "業務ロール管理：" + (i + 1) + "行目：" + gyoumuRoleName[i] +"：有効期限終了日", false);
					}
				}
			}
			
			//カスタマイズここから
			// 会社切替設定をチェックする（会社切替設定セクションを画面表示している場合のみ）
			if (this.useKaishaKirikaeSettei && null != kaishaKirikaeCd){
				for(int i = 0; i < kaishaKirikaeCd.length; i++) {
					if(isEmpty(kaishaKirikaeYuukoukigenFrom[i])){
						errorList.add("会社切替設定：" + (i + 1) + "行目：" + kaishaKirikaeName[i] + "：有効期限開始日を入力して下さい。");
					} else {
						checkDate(kaishaKirikaeYuukoukigenFrom[i], "会社切替設定：" + (i + 1) + "行目：" + kaishaKirikaeName[i] + "：有効期限開始日", false);
						checkDate(kaishaKirikaeYuukoukigenTo[i],   "会社切替設定：" + (i + 1) + "行目：" + kaishaKirikaeName[i] + "：有効期限終了日", false);
					}
				}
			}
			//カスタマイズここまで

			if(0 < errorList.size()){
				successful = false;
				return "error";
			}


			//設定情報のチェック
			//テナントの最大ユーザー数を超えないかチェックする。
			if(tourokuzumiUserCount >= settingInfoValue){
				errorList.add("登録可能なユーザー数を超えています。");
				return "error";
			}

			//ユーザー情報のチェック
			// 入力されたユーザーIDが重複していないかチェックする。
			if(EteamCommon.dataSonzaiCheck(connection, userId, EteamCommon.CheckTable.USER_INFO_KEY_ID) ){
				errorList.add("入力したユーザーIDはすでに登録されています。");
			}
			// 入力された社員番号が重複していないかチェックする。
			if(EteamCommon.dataSonzaiCheck(connection, shainNo, EteamCommon.CheckTable.USER_INFO_KEY_SHAIN_NO) ){
				errorList.add("入力した社員番号はすでに登録されています。");
			}
			if(masterKanriLogic.findShain(shainNo) == null) {
				errorList.add("入力した社員番号は存在しません。");
			}
			// メールアドレスの重複チェック
			if(mailAddress.length() > 0 && EteamCommon.dataSonzaiCheck(connection, mailAddress, EteamCommon.CheckTable.USER_INFO_KEY_MAIL_ADDRESS) ){
				errorList.add("入力したメールアドレスはすでに登録されています。");
			}

			// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
			if (isEmpty(userYuukoukigenTo)) { userYuukoukigenTo = "9999/12/31"; }
			// 有効期限の共通チェックを行います。
			for(Map<String, String> errMap: EteamCommon.kigenCheck(userYuukoukigenFrom, userYuukoukigenTo)) {
				// 開始日と終了日の整合性チェックのみエラーとする。
				if ("2".equals(errMap.get("errorCode"))) {
					errorList.add("ユーザー情報：" + errMap.get("errorMassage"));
				}
			}


			//所属部門のチェック
			LinkedHashSet<Map<String, String>> bumonHashSet = new LinkedHashSet<Map<String, String>>();
			LinkedHashSet<Map<String, String>> bumonSubHashSet = new LinkedHashSet<Map<String, String>>();
			int bumonLine = 1; // 行数
			for(int i = 0; i < bumonCd.length; i++) {
				// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
				if (isEmpty(bumonYuukoukigenTo[i]))
				{
					bumonYuukoukigenTo[i] = "9999/12/31";
				}

				// ユーザー情報の有効期限との相関チェック
				if(userYuukoukigenFrom.compareTo(bumonYuukoukigenFrom[i]) > 0) { // 開始日の比較
					bumonYuukoukigenFrom[i] = userYuukoukigenFrom;
				}
				if(userYuukoukigenTo.compareTo(bumonYuukoukigenTo[i]) < 0) { // 終了日の比較
					bumonYuukoukigenTo[i] =  userYuukoukigenTo;
				}

				// 有効期限の共通チェックを行います。
				for(Map<String, String> errMap: EteamCommon.kigenCheck(bumonYuukoukigenFrom[i], bumonYuukoukigenTo[i])) {
					// 開始日と終了日の整合性チェックのみエラーとする。
					if ("2".equals(errMap.get("errorCode"))) {
						errorList.add("所属部門管理：" + bumonLine + "行目：" + bumonName[i] + "：" + errMap.get("errorMassage"));
					}
				}
				// レコードの重複チェック
				Map<String, String> bumonMap = new HashMap<String, String>();
				bumonMap.put("bumon_cd",        bumonCd[i]);
				Map<String, String> bumonSubMap = new HashMap<String, String>();
				bumonSubMap.put("bumon_cd",                  bumonCd[i]);
				bumonSubMap.put("bumon_yuukoukigen_from",    bumonYuukoukigenFrom[i]);
				bumonSubMap.put("bumon_yuukoukigen_to",      bumonYuukoukigenTo[i]);
				if(bumonHashSet.contains(bumonMap)) {
					for (Map<String,String>map : bumonSubHashSet) {
						if (map.get("bumon_cd").equals(bumonSubMap.get("bumon_cd")) &&
							(EteamCommon.isKigenDuplicate(map.get("bumon_yuukoukigen_from"),map.get("bumon_yuukoukigen_to"),bumonSubMap.get("bumon_yuukoukigen_from"),bumonSubMap.get("bumon_yuukoukigen_to"))) ) {
							errorList.add("所属部門管理：" + bumonLine + "行目：所属部門が重複しています。");
							break;
						}
					}
				}
				bumonHashSet.add(bumonMap);
				bumonSubHashSet.add(bumonSubMap);

				// 部門ロールの存在チェック
				if(!EteamCommon.dataSonzaiCheck(connection, bumonRoleId[i], EteamCommon.CheckTable.BUMON_ROLE_KEY_ID)) {
					errorList.add("所属部門管理：" + bumonLine + "行目：役割が削除されています。");
				}

				// 代表負担部門コードの存在チェック
				if(!daihyouFutanBumonCd[i].equals("")){
					if(masterKanriLogic.findFutanBumonName(daihyouFutanBumonCd[i]).equals("")) {
						errorList.add("代表負担部門:" + bumonLine + "行目：代表負担部門が削除されています。");
					}
				}

				bumonLine++;
			}

			// 所属部門管理の存在チェック
			Iterator<Map<String, String>> shozokuBumonSubIt = bumonSubHashSet.iterator();
			while (shozokuBumonSubIt.hasNext()) {
				Map<String, String> getSubMap = shozokuBumonSubIt.next();
				// 開始日・終了日時点で部門が存在するかチェック
				if(!EteamCommon.dataSonzaiCheck(connection, getSubMap.get("bumon_cd"), EteamCommon.CheckTable.SHOZOKU_BUMON_KEY_CD_ALL_DATE)) {
					errorList.add("所属部門管理：所属部門コード（" + getSubMap.get("bumon_cd") + "）は削除されています。");
				}else{
					if(bumonUserLogic.selectValidShozokuBumon(getSubMap.get("bumon_cd"), toDate(getSubMap.get("bumon_yuukoukigen_from"))) == null){
						errorList.add("所属部門管理：所属部門コード（" + getSubMap.get("bumon_cd") + "）は["+getSubMap.get("bumon_yuukoukigen_from").toString()+"]時点で有効ではありません。");
					}
					if(bumonUserLogic.selectValidShozokuBumon(getSubMap.get("bumon_cd"), toDate(getSubMap.get("bumon_yuukoukigen_to"))) == null){
						errorList.add("所属部門管理：所属部門コード（" + getSubMap.get("bumon_cd") + "）は["+getSubMap.get("bumon_yuukoukigen_to").toString()+"]時点で有効ではありません。");
					}
				}
			}

			//業務ロールのチェック
			if (gyoumuRoleId != null) {
				HashSet<Map<String, String>> gyoumuRole = new HashSet<Map<String, String>>();
				int gyoumuRoleLine = 1; // 行数
				for(int i = 0; i < gyoumuRoleId.length; i++){
					// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
					if (isEmpty(gyoumuRoleYuukoukigenTo[i]))
					{
						gyoumuRoleYuukoukigenTo[i] = "9999/12/31";
					}

					// ユーザー情報の有効期限との相関チェック
					if(userYuukoukigenFrom.compareTo(gyoumuRoleYuukoukigenFrom[i]) > 0) { // 開始日の比較
						gyoumuRoleYuukoukigenFrom[i] = userYuukoukigenFrom;
					}
					if(userYuukoukigenTo.compareTo(gyoumuRoleYuukoukigenTo[i]) < 0) { // 終了日の比較
						gyoumuRoleYuukoukigenTo[i] = userYuukoukigenTo;
					}

					// 有効期限の共通チェックを行います。
					for(Map<String, String> errMap: EteamCommon.kigenCheck(gyoumuRoleYuukoukigenFrom[i], gyoumuRoleYuukoukigenTo[i])) {
						// 開始日と終了日の整合性チェックのみエラーとする。
						if ("2".equals(errMap.get("errorCode"))) {
							errorList.add("業務ロール管理：" + gyoumuRoleLine + "行目：" + gyoumuRoleName[i] + "：" + errMap.get("errorMassage"));
						}
					}

					// レコードの重複チェック
					Map<String, String> hashSetMap = new HashMap<String, String>();
					hashSetMap.put("gyoumu_role_id",   gyoumuRoleId[i]);
					hashSetMap.put("gyoumu_role_bumon_cd", gyoumuRoleShoriBumonCd[i]);
					if (gyoumuRole.contains(hashSetMap)) {
						errorList.add("業務ロール管理：" + gyoumuRoleLine + "行目：所属ロール・処理部門が重複しています。");
					}
					gyoumuRole.add(hashSetMap);

					// 処理部門の存在チェック
					// 開始日・終了日時点で部門が存在するかチェック
					if(!EteamCommon.dataSonzaiCheck(connection, gyoumuRoleShoriBumonCd[i], EteamCommon.CheckTable.SHOZOKU_BUMON_KEY_CD_ALL_DATE)) {
						errorList.add("業務ロール管理：" + gyoumuRoleLine + "行目：処理部門：" + gyoumuRoleShoriBumonName[i] + "は削除されています。");
					}else{
						if(bumonUserLogic.selectValidShozokuBumon(gyoumuRoleShoriBumonCd[i], toDate(gyoumuRoleYuukoukigenFrom[i])) == null){
							errorList.add("業務ロール管理：" + gyoumuRoleLine + "行目：処理部門コード（" + gyoumuRoleShoriBumonCd[i] +"）は["+gyoumuRoleYuukoukigenFrom[i]+"]時点で有効ではありません。");
						}
						if(bumonUserLogic.selectValidShozokuBumon(gyoumuRoleShoriBumonCd[i], toDate(gyoumuRoleYuukoukigenTo[i])) == null){
							errorList.add("業務ロール管理：" + gyoumuRoleLine + "行目：処理部門コード（" + gyoumuRoleShoriBumonCd[i] +"）は["+gyoumuRoleYuukoukigenTo[i]+"]時点で有効ではありません。");
						}
					}
					gyoumuRoleLine++;
				}

				// 業務ロールの存在チェック
				Iterator<Map<String, String>> gyoumuRoleIt = gyoumuRole.iterator();
				while (gyoumuRoleIt.hasNext()) {
					Map<String, String> getMap = gyoumuRoleIt.next();
					if(!EteamCommon.dataSonzaiCheck(connection, getMap.get("gyoumu_role_id"), EteamCommon.CheckTable.GYOUMU_ROLE_KEY_ID)){
						errorList.add("業務ロール管理：所属ロールID（" + getMap.get("gyoumu_role_id") + "）は削除されています。");
					}
				}
			}
			//カスタマイズここから
			// 会社切替設定のチェック（会社切替設定セクションを画面表示している場合のみ）
			if (this.useKaishaKirikaeSettei && null != kaishaKirikaeCd){
				LinkedHashSet<Map<String, String>> kaishaKirikaeHashSet = new LinkedHashSet<Map<String, String>>();
				LinkedHashSet<Map<String, String>> kaishaKirikaeSubHashSet = new LinkedHashSet<Map<String, String>>();
				int kaishaKirikaeLine = 1; // 行数
				for(int i = 0; i < kaishaKirikaeCd.length; i++) {
					// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
					if (isEmpty(kaishaKirikaeYuukoukigenTo[i])){
						kaishaKirikaeYuukoukigenTo[i] = "9999/12/31";
					}

					// ユーザー情報の有効期限との相関チェック

					// 開始日の比較を行い、ユーザの有効期限の方が大きい場合は、その期限を設定する。
					if(userYuukoukigenFrom.compareTo(kaishaKirikaeYuukoukigenFrom[i]) > 0) {
						kaishaKirikaeYuukoukigenFrom[i] = userYuukoukigenFrom;
					}
					// 終了日の比較を行い、ユーザの有効期限の方が小さい場合は、その期限を設定する。
					if(userYuukoukigenTo.compareTo(kaishaKirikaeYuukoukigenTo[i]) < 0) {
						kaishaKirikaeYuukoukigenTo[i] =  userYuukoukigenTo;
					}
					// 有効期限の共通チェックを行います。
					for(Map<String, String> errMap: EteamCommon.kigenCheck(kaishaKirikaeYuukoukigenFrom[i], kaishaKirikaeYuukoukigenTo[i])) {
						// エラーコードが「0」以外の場合にエラーメッセージを追加します。
						if (!"0".equals(errMap.get("errorCode"))) {
							errorList.add("会社切替設定：" + kaishaKirikaeLine + "行目：" + kaishaKirikaeName[i] + "：" + errMap.get("errorMassage"));
						}
					}

					// レコードの重複チェック
					Map<String, String> kaishaKirikaeMap = new HashMap<String, String>();
					kaishaKirikaeMap.put("scheme_cd", kaishaKirikaeCd[i]);
					Map<String, String> kaishaKirikaeSubMap = new HashMap<String, String>();
					kaishaKirikaeSubMap.put("scheme_cd", kaishaKirikaeCd[i]);
					kaishaKirikaeSubMap.put("kaisha_kirikae_yuukoukigen_from", kaishaKirikaeYuukoukigenFrom[i]);
					kaishaKirikaeSubMap.put("kaisha_kirikae_yuukoukigen_to", kaishaKirikaeYuukoukigenTo[i]);
					if(kaishaKirikaeHashSet.contains(kaishaKirikaeMap)) {
						for (Map<String,String>map : kaishaKirikaeSubHashSet) {
							if (map.get("scheme_cd").equals(kaishaKirikaeSubMap.get("scheme_cd")) &&
								!(toDate(map.get("kaisha_kirikae_yuukoukigen_to")).compareTo(toDate(kaishaKirikaeSubMap.get("kaisha_kirikae_yuukoukigen_from"))) < 0 ||
								  toDate(kaishaKirikaeSubMap.get("kaisha_kirikae_yuukoukigen_to")).compareTo(toDate(map.get("kaisha_kirikae_yuukoukigen_from"))) < 0)) {
								errorList.add("会社切替設定：" + kaishaKirikaeLine + "行目：会社切替設定が重複しています。");
								break;
							}
						}
					}
					kaishaKirikaeHashSet.add(kaishaKirikaeMap);
					kaishaKirikaeSubHashSet.add(kaishaKirikaeSubMap);
					kaishaKirikaeLine++;
				}
			}
			//カスタマイズここまで
			// エラーがある場合は表示して終了する。
			if(0 < errorList.size()){
				successful = false;
				return "error";
			}

			//登録処理
			// ユーザー情報へ登録
			myLogic.insertUserInfo(  userId
									,shainNo
									,replaceSpaceDelete(userSei)
									,replaceSpaceDelete(userMei)
									,n2b(mailAddress)
									,toDate(userYuukoukigenFrom)
									,toDate(userYuukoukigenTo)
									,loginUserId
									,"1".equals(dairiKihyouFlag)
									,"1".equals(houjinCardRiyouFlag)
									,n2b(houjinCardShikibetsuyouNum)
									,n2b(securityPattern)
									,("1".equals(securityWfonlyFlg) ? "1":"0")
									,shouninRouteHenkouLevel == null ? "0":shouninRouteHenkouLevel
									,("1".equals(maruhiKengenFlag) ? "1":"0")
									,("1".equals(maruhiKaijyoFlag) ? "1":"0")
									,("1".equals(zaimuKyotenNyuryokuOnlyFlg) ? "1":"0")
									);
			logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_ACCOUNT, LogDetail.ACCOUNT_ADD);

			// パスワードへ登録
			// パスワードを更新(delete ⇒ insert) 画面から登録されたものを正とする
			myLogic.deletePassword(userId);
			myLogic.insertPassword(userId, password);
			if (0 == Integer.parseInt(setting.requirePassChange())) {
				myLogic.updataPassword(loginUserId, userId, password, PasswordValidTerm.NORMAL);
			}

			// 所属部門割り当てへ登録(マスタアップロード対応：画面から登録されたものを正とする)
			// 所属部門割り当てを更新（delete ⇒ insert）
			myLogic.deleteShozokuBumonWariate(userId);
			int hyoujiJun = 0;
			for(int i=0; i <bumonCd.length; i++) {
				hyoujiJun++;
				myLogic.insertShozokuBumonWariate(bumonCd[i], bumonRoleId[i], userId, daihyouFutanBumonCd[i], toDate(bumonYuukoukigenFrom[i]), toDate(bumonYuukoukigenTo[i]), hyoujiJun, loginUserId);
			}

			// 業務ロール割り当てへ登録
			// 業務ロール割り当てを更新（delete ⇒ insert）
			if(!gaibuYobidashiFlag) {
				myLogic.deleteGyoumuRoleWariate(userId);
				if(gyoumuRoleId != null) {
					for(int i = 0; i < gyoumuRoleId.length; i++){
						myLogic.insertGyoumuRoleWariate(userId, gyoumuRoleId[i], toDate(gyoumuRoleYuukoukigenFrom[i]), toDate(gyoumuRoleYuukoukigenTo[i]), gyoumuRoleShoriBumonCd[i], loginUserId);
					}
				}
			}
			//カスタマイズここから
			// 会社切替設定を更新（会社切替設定セクションを画面表示している場合のみ）
			if (this.useKaishaKirikaeSettei){
				((UserJouhouKanriExtLogic)myLogic).deleteKaishaKirikaeSettei(userId);
				if (null != kaishaKirikaeCd){
					hyoujiJun = 0;
					for(int i=0; i <kaishaKirikaeCd.length; i++) {
						hyoujiJun++;
						((UserJouhouKanriExtLogic)myLogic).insertKaishaKirikaeSettei(userId, kaishaKirikaeCd[i], toDate(kaishaKirikaeYuukoukigenFrom[i]), toDate(kaishaKirikaeYuukoukigenTo[i]), hyoujiJun, loginUserId);
					}
				}
			}
			//カスタマイズここまで
			if(!gaibuYobidashiFlag) {
				connection.commit();
			}

			//5.戻り値を返す
			successful = true;
			return "success";
		}finally{
			if(!gaibuYobidashiFlag) {
				connection.close();
			}
		}
	}
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		super.displaySeigyo(connection);
		//カスタマイズ
		// TODO 会社切替設定の有効／無効をどうするか？会社設定で使用する／しないで切り替える？？
		// 会社切替設定
		this.useKaishaKirikaeSettei = true;
	}
}
