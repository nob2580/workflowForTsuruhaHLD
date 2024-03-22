package eteam.gyoumu.userjouhoukanri;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.BUMON_CD;
import eteam.common.EteamConst.PasswordValidTerm;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー追加画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class UserJouhouTsuikaAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** ユーザーID */
	String userId;
	/** 社員番号 */
	String shainNo;
	/** 社員番号に紐付く代表負担部門コード */
	String shainDaihyouFutanBumonCd;
	/** 社員番号に紐付く代表負担部門名 */
	String shainDaihyouFutanBumonName;
	/** ユーザー姓 */
	String userSei;
	/** ユーザー名 */
	String userMei;
	/** メールアドレス */
	String mailAddress;
	/** セキュリティパターン **/
	String securityPattern;
	/** セキュリティワークフロー限定フラグ **/
	String securityWfonlyFlg;
	/** パスワード */
	String password;
	/** ユーザー情報の有効期限開始日 */
	String userYuukoukigenFrom;
	/** ユーザー情報の有効期限終了日 */
	String userYuukoukigenTo;
	/** 代理起票可能フラグ **/
	String dairiKihyouFlag;
	/** 法人カード利用フラグ */
	String houjinCardRiyouFlag;
	/** 法人カード識別用番号 */
	String houjinCardShikibetsuyouNum;
	/** 承認ルート変更権限レベル */
	String shouninRouteHenkouLevel;
	/** マル秘設定権限 */
	String maruhiKengenFlag;
	/** マル秘解除権限 */
	String maruhiKaijyoFlag;
	/** 財務拠点のみ使用 */
	String zaimuKyotenNyuryokuOnlyFlg;
	/** 負担部門未入力チェック */
	String userDaihyouFutanBumonMinyuuryokuCheck;

	/** 部門コード */
	String[] bumonCd;
	/** 部門名 */
	String[] bumonName;
	/** 部門ロールデータ */
	String[] bumonRoleId;
	/** 代表負担部門コード*/
	String[] daihyouFutanBumonCd;
	/** 代表負担部門名 */
	String[] daihyouFutanBumonName;
	/** 所属部門の有効期限開始日 */
	String[] bumonYuukoukigenFrom;
	/** 所属部門の有効期限終了日 */
	String[] bumonYuukoukigenTo;

	/** 業務ロールID */
	String[] gyoumuRoleId;
	/** 業務ロール名 */
	String[] gyoumuRoleName;
	/** 業務ロールの有効期限開始日 */
	String[] gyoumuRoleYuukoukigenFrom;
	/** 業務ロールの有効期限終了日 */
	String[] gyoumuRoleYuukoukigenTo;
	/** 業務ロール処理部門コード */
	String[] gyoumuRoleShoriBumonCd;
	/** 業務ロール処理部門名*/
	String[] gyoumuRoleShoriBumonName;

//＜画面入力以外＞
	/** 部門ロールリスト */
	List<GMap> bumonRoleList;
	/** 承認ルート変更権限レベルリスト */
	List<GMap> henkouKengenLevelList;
	/** 承認ルート変更権限レベルドメイン */
	String[] henkouKengenDomain;

	/** 登録正常終了フラグ */
	boolean successful;

	/** ログインユーザー情報 */
	User loginUserInfo;
	/** ログインユーザーID */
	String loginUserId;

	/** 登録済みユーザー数 */
	long tourokuzumiUserCount;
	/** 設定情報の設定値 */
	int settingInfoValue;

	/** 初期表示処理部門コード*/
	String gyoumuRoleShokiShoriShozokuBumonCd;
	/** 初期表示処理部門名 */
	String gyoumuRoleShokiShoriShozokuBumonName;

	/** 法人カード利用表示制御 */
	boolean houjinCardFlag;
	/** セキュリティパターン入力制御 */
	boolean securityPatternFlag;
	/** 承認者による承認ルート変更が可能か*/
	boolean shouninRouteHenkouShouninsha;
	/** 外部呼出し制御 */
	boolean gaibuYobidashiFlag=false;
	/** リクエスト情報 */
	HttpServletRequest request;
	/** 接続元IP */
	String ip;
	/** 接続元IP(X-Forwarded-For) */
	String ipx;
	/** 外部呼出しコネクション */
	EteamConnection gaibuConnection;

//＜部品＞
	/** ユーザー情報ロジック */
	UserJouhouKanriLogic myLogic;
	/** 部門ユーザ管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** マスター管理カテゴリロジック */
	MasterKanriCategoryLogic masterKanriLogic;
	/** システム管理ロジック */
	SystemKanriCategoryLogic systemLogic;
	/** セキュリティログロジック */
	SecurityLogLogic logLogic;


//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(userId,      1, 30, "ユーザーID",      false);
		checkString(shainNo,     1, 15, "社員番号",        false);
		checkString(userSei,     1, 10, "ユーザー姓",      false);
		checkString(userMei,     1, 10, "ユーザー名",      false);
		checkString(mailAddress, 6, 50, "メールアドレス",  false);
		checkNumber(securityPattern,  1,  4, "セキュリティパターン", false);
		checkCheckbox(securityWfonlyFlg, "ワークフロー入力データのみ", false);
		if(daihyouFutanBumonCd != null){
			for(int i = 0; i<daihyouFutanBumonCd.length; i++){
				checkString(daihyouFutanBumonCd[i], 1, 8, "代表負担部門コード",  false);
			}
		}
		errorList.addAll(EteamCommon.mailAddressCheck(mailAddress));
		if(!gaibuYobidashiFlag) {
			int passLenMin = Integer.parseInt(setting.passLenMin());
			checkString(password, passLenMin, 32, "パスワード", false);
			errorList.addAll(EteamCommon.passwordCheck(password));
		}
		checkDate(userYuukoukigenFrom,  "有効期限開始日",  false);
		checkDate(userYuukoukigenTo,    "有効期限終了日",  false);
		checkCheckbox(dairiKihyouFlag, "代理起票可能", false);
		checkCheckbox(houjinCardRiyouFlag, "法人カード利用フラグ", false);
		checkHankakuEiSuu(houjinCardShikibetsuyouNum, "法人カード識別用番号");
		checkString(houjinCardShikibetsuyouNum, 0, 16, "法人カード識別用番号", false);
		checkDomain(shouninRouteHenkouLevel, henkouKengenDomain, "承認ルート変更権限", false);
		checkCheckbox(maruhiKengenFlag, "マル秘設定権限", false);
		checkCheckbox(maruhiKaijyoFlag, "マル秘解除権限", false);
		checkCheckbox(zaimuKyotenNyuryokuOnlyFlg, "財務拠点のみ使用", false);
		checkHankakuEiSuuUnderbar(userId,  "ユーザーID");
		checkHankakuEiSuu(shainNo, "社員番号");
	}
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{userId,              "ユーザーID"			,"0", "1", },
			{shainNo,             "社員番号"			,"0", "1", },
			{userSei,             "ユーザー姓"			,"0", "1", },
			{userMei,             "ユーザー名"			,"0", "1", },
			{password,            "パスワード"			,"0", "1", },
			{userYuukoukigenFrom, "有効期限開始日"		,"0", "1", },
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			displaySeigyo(connection);

			//1.入力チェック
			hissuCheck(1);
			formatCheck();
			if(!errorList.isEmpty()){
				return "error";
			}
			//2.データ存在チェック なし
			//3.アクセス可能チェック（権限チェック） なし
			//4.処理（初期表示） なし

			//5.戻り値を返す
			return "success";
		}
	}

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
	 * 社員番号変更時、社員マスターから社員番号に紐付く代表負担部門を取得
	 * @return 結果
	 */
	public String getDaihyouBumonName() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);

			String responseText = "";

			//初期表示用の代表負担部門コードと代表負担名を取得（所属部門管理）
			GMap shokiDaihyouFutanBumon = bumonUserLogic.findShainDaihyouFutanBumonName(shainNo);
			if(shokiDaihyouFutanBumon != null){
				String cd = shokiDaihyouFutanBumon.get("daihyou_futan_bumon_cd").toString();
				String name = shokiDaihyouFutanBumon.get("futan_bumon_name").toString();
				if (name == null)
				{
					name = "";
				}
				responseText = cd + "\t" + name;
			}

			//AJAXレスポンス返す
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.print(responseText);
			out.flush();
			out.close();
		} catch (IOException ex) {
		}
		return "success";
	}


	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		logLogic = EteamContainer.getComponent(SecurityLogLogic.class, connection);
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		// セキュリティパターンを表示するか
		securityPatternFlag = "1".equals(setting.keihimeisaiSecurityPsettei()) ? true : false;

		// 法人カードの表示可否
		houjinCardFlag = systemLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);

		// 承認者による承認ルート変更が可能か
		shouninRouteHenkouShouninsha = systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHOUNINSHA);

		//社員番号とユーザー名変更可否
		// 通常の変更処理の際はこちらで取得
		// 一括登録処理の際は既に設定済み
		if(loginUserInfo == null) {
			loginUserInfo = getUser();
		}else {
			logLogic.setRequest(request);
			logLogic.setIp(ip);
			logLogic.setIpx(ipx);
		}
		loginUserId = loginUserInfo.getTourokuOrKoushinUserId();

		//初期表示用の処理部門コードと処理部門名を取得（業務ロール管理）
		GMap shokiShoriBumonList = bumonUserLogic.selectValidShozokuBumon(BUMON_CD.ZENSHA);
		gyoumuRoleShokiShoriShozokuBumonCd =shokiShoriBumonList.get("bumon_cd").toString();
		gyoumuRoleShokiShoriShozokuBumonName = shokiShoriBumonList.get("bumon_name").toString();

		// 承認ルート変更権限レベルリストの取得
		henkouKengenLevelList = systemLogic.loadNaibuCdSetting("shounin_route_henkou_level");
		henkouKengenDomain = EteamCommon.mapList2Arr(henkouKengenLevelList, "naibu_cd");

		// 部門ロールのデータ取得
		bumonRoleList = bumonUserLogic.selectBumonRole();

		// 登録済みユーザ数、テナント最大ユーザ数の取得
		tourokuzumiUserCount = bumonUserLogic.findUserCount();
		settingInfoValue = Integer.parseInt(setting.tenantMaxUsersNum());

		// ユーザー代表負担部門未入力チェック取得
		this.userDaihyouFutanBumonMinyuuryokuCheck = this.setting.userDaihyoufutanbumonMinyuuryokuCheck();
	}
}