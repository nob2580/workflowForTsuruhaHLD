package eteam.gyoumu.userjouhoukanri;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.BUMON_CD;
import eteam.common.EteamConst.PasswordValidTerm;
import eteam.common.EteamConst.UserId;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.RegAccess;
import eteam.common.SecurityLogLogic;
import eteam.common.SecurityLogLogic.LogDetail;
import eteam.common.SecurityLogLogic.LogType;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー情報画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class UserJouhouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** ユーザーID */
	String userId;
	/** 社員番号 */
	String shainNo;
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
	/** パスワードの有効期限 */
	String passwordYuukoukigen;
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
	/** アカウント一時ロック */
	String accountTmpLockFlag;
	/** アカウント永続ロック */
	String accountLockFlag;
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

	/** 操作者がadmin または 会社設定権限 */
	boolean logonIsKanri;
	/** 表示対象がadmin */
	boolean viewIsAdmin;
	/*** 社員番号、ユーザー名の変更可否フラグ */
	boolean userInfoHenkou;

	/** 登録正常終了フラグ */
	boolean successful;

	/** ログインユーザー情報 */
	User loginUserInfo;
	/** ログインユーザーID */
	String loginUserId;

	/** 初期表示代表負担部門コード*/
	String shokiDaihyouFutanBumonCd;
	/** 初期表示代表負担部門名 */
	String shokiDaihyouFutanBumonName;

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
	/** ワークフローロジック */
	WorkflowCategoryLogic wfLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(userId,      1, 30, "ユーザーID",      true); // KEY
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
		
// Ver22.05.31.00 Bizプリカ追加対応(法人カード識別用番号：16 → 20) *-		
// checkString(houjinCardShikibetsuyouNum, 0, 16, "法人カード識別用番号", false);
		checkString(houjinCardShikibetsuyouNum, 0, 20, "法人カード識別用番号", false);
// -*		
		
		checkDomain(shouninRouteHenkouLevel, henkouKengenDomain, "承認ルート変更権限", false);
		checkCheckbox(maruhiKengenFlag, "マル秘設定権限", false);
		checkCheckbox(maruhiKaijyoFlag, "マル秘解除権限", false);
		checkCheckbox(accountTmpLockFlag, "アカウント一時ロック", false);
		checkCheckbox(accountLockFlag, "アカウント永続ロック", false);
	}
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{userId,              "ユーザーID"			,"2", "2", "2", }, // KEY
				{shainNo,             "社員番号"			,"0", "1", "0", },
				{userSei,             "ユーザー姓"			,"0", "1", "0", },
				{userMei,             "ユーザー名"			,"0", "1", "0", },
				{userYuukoukigenFrom, "有効期限開始日"		,"0", "1", "0", },
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

			//2.データ存在チェック
			GMap userJouhouMap = bumonUserLogic.selectUserJouhou(userId);
			if(userJouhouMap == null){
				throw new EteamDataNotFoundException();
			}

			//3.アクセス可能チェック（権限チェック）
			checkAccess(connection, false, false);

			//4.処理（初期表示）
			shainNo = userJouhouMap.get("shain_no").toString();
			userSei = userJouhouMap.get("user_sei").toString();
			userMei = userJouhouMap.get("user_mei").toString();
			mailAddress = userJouhouMap.get("mail_address").toString();
			securityPattern = userJouhouMap.get("security_pattern").toString();
			securityWfonlyFlg = "1".equals(userJouhouMap.get("security_wfonly_flg")) ? "1" : null;
			password = userJouhouMap.get("password").toString();
			userYuukoukigenFrom = formatDate(userJouhouMap.get("yuukou_kigen_from"));
			userYuukoukigenTo = formatDate(userJouhouMap.get("yuukou_kigen_to"));
			dairiKihyouFlag = "1".equals(userJouhouMap.get("dairikihyou_flg")) ? "1" : null;
			houjinCardRiyouFlag = "1".equals(userJouhouMap.get("houjin_card_riyou_flag")) ? "1" : null;
			houjinCardShikibetsuyouNum = userJouhouMap.get("houjin_card_shikibetsuyou_num").toString();
			shouninRouteHenkouLevel = userJouhouMap.get("shounin_route_henkou_level").toString();
			maruhiKengenFlag =  "1".equals(userJouhouMap.get("maruhi_kengen_flg")) ? "1" : null;
			maruhiKaijyoFlag =  "1".equals(userJouhouMap.get("maruhi_kaijyo_flg")) ? "1" : null;
			accountTmpLockFlag = "1".equals(userJouhouMap.get("tmp_lock_flg")) ? "1" : null;
			accountLockFlag = "1".equals(userJouhouMap.get("lock_flg")) ? "1" : null;
			zaimuKyotenNyuryokuOnlyFlg = "1".equals(userJouhouMap.get("zaimu_kyoten_nyuryoku_only_flg")) ? "1" : null;

			// 有効期限取得
			int passwordValidTerm = Integer.parseInt(setting.passValidTerm());
			Date passKoushinDate = (Date)userJouhouMap.get("pass_koushin_date");
			Date passKoushinDate2 = (Date)userJouhouMap.get("pass_koushin_date2");
			if (null == passKoushinDate2) {
				passwordYuukoukigen = "失効済";
			} else if (0 >= passwordValidTerm) {
				passwordYuukoukigen = "無期限";
			} else if (null == passKoushinDate) {
				passwordYuukoukigen = formatDate(passKoushinDate2) + "(失効済)";
			} else {
				passwordYuukoukigen = formatDate(passKoushinDate);
			}

			// 管理者モードで表示がadmin以外なら所属部門と業務ロール表示
			if(logonIsKanri && !viewIsAdmin) {
				List<GMap> shozokuBumonList = bumonUserLogic.selectShozokuBumonKanri(userId);
				bumonCd = new String[shozokuBumonList.size()];
				bumonName = new String[shozokuBumonList.size()];
				bumonRoleId = new String[shozokuBumonList.size()];
				daihyouFutanBumonCd = new String[shozokuBumonList.size()];
				daihyouFutanBumonName = new String[shozokuBumonList.size()];
				bumonYuukoukigenFrom = new String[shozokuBumonList.size()];
				bumonYuukoukigenTo = new String[shozokuBumonList.size()];
				int i=0;
				for(GMap shozokuBumon : shozokuBumonList){
					bumonCd[i] = (String)shozokuBumon.get("bumon_cd");
					bumonName[i] = (String)shozokuBumon.get("bumon_name");
					bumonRoleId[i] = (String)shozokuBumon.get("bumon_role_id");
					daihyouFutanBumonCd[i] = (String)shozokuBumon.get("daihyou_futan_bumon_cd");
					daihyouFutanBumonName[i] = (String)shozokuBumon.get("futan_bumon_name");
					bumonYuukoukigenFrom[i] = formatDate(shozokuBumon.get("yuukou_kigen_from"));
					bumonYuukoukigenTo[i] = formatDate(shozokuBumon.get("yuukou_kigen_to"));
					i++;
				}

				List<GMap> gyoumuRoleList = bumonUserLogic.selectGyoumuRoleKanri(userId);
				gyoumuRoleId = new String[gyoumuRoleList.size()];
				gyoumuRoleName = new String[gyoumuRoleList.size()];
				gyoumuRoleYuukoukigenFrom = new String[gyoumuRoleList.size()];
				gyoumuRoleYuukoukigenTo = new String[gyoumuRoleList.size()];
				gyoumuRoleShoriBumonCd = new String[gyoumuRoleList.size()];
				gyoumuRoleShoriBumonName = new String[gyoumuRoleList.size()];
				i=0;
				for(GMap gyoumuRole : gyoumuRoleList) {
					gyoumuRoleId[i] = (String)gyoumuRole.get("gyoumu_role_id");
					gyoumuRoleName[i] = (String)gyoumuRole.get("gyoumu_role_name");
					gyoumuRoleYuukoukigenFrom[i] = formatDate(gyoumuRole.get("yuukou_kigen_from"));
					gyoumuRoleYuukoukigenTo[i] = formatDate(gyoumuRole.get("yuukou_kigen_to"));
					gyoumuRoleShoriBumonCd[i] = (String)gyoumuRole.get("shori_bumon_cd");
					gyoumuRoleShoriBumonName[i] = (String)gyoumuRole.get("bumon_name");
					i++;
				}
			}

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 更新イベント
	 * @return ResultName
	 */
	public String koushin(){

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

			//2.データ存在チェック
			GMap userJouhouMap = bumonUserLogic.selectUserJouhou(userId);
			if(userJouhouMap == null){
				throw new EteamDataNotFoundException();
			}

			//3.アクセス可能チェック（権限チェック）
			checkAccess(connection, false, false);

			//4.処理
			//管理者モードで表示がadmin以外なら所属部門と業務ロールを表示しているのでチェックする
			if(logonIsKanri && !viewIsAdmin) {
				//所属部門のチェック
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
				//業務ロールのチェック
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
			}
			if(0 < errorList.size()){
				successful = false;
				return "error";
			}

			//ユーザー情報のチェック
			// 入力された社員番号に変更が有る場合、重複チェックを行います。
			if(!shainNo.equals(userJouhouMap.get("shain_no").toString())) {
				if(EteamCommon.dataSonzaiCheck(connection, shainNo, EteamCommon.CheckTable.USER_INFO_KEY_SHAIN_NO) ){
					errorList.add("入力した社員番号はすでに登録されています。");
				}
				if(masterKanriLogic.findShain(shainNo) == null) {
					errorList.add("入力した社員番号は存在しません。");
				}
			}
			// メールアドレスが入力されている、且つ変更が有る場合、重複チェックを行います。
			if(mailAddress.length() > 0 && !mailAddress.equals(userJouhouMap.get("mail_address").toString())) {
				if(EteamCommon.dataSonzaiCheck(connection, mailAddress, EteamCommon.CheckTable.USER_INFO_KEY_MAIL_ADDRESS) ){
					errorList.add("入力したメールアドレスはすでに登録されています。");
				}
			}

			// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
			if (isEmpty(userYuukoukigenTo)) { userYuukoukigenTo = "9999/12/31"; }
			// 有効期限の共通チェックを行います。
			for(Map<String, String> errMap: EteamCommon.kigenCheck(userYuukoukigenFrom, userYuukoukigenTo)) {
				// エラーコードが「1」の場合、ユーザーIDが自分自身ならエラー。
				if ("1".equals(errMap.get("errorCode")) && loginUserInfo.getTourokuOrKoushinUserId().equals(userId)){
					errorList.add("ユーザー情報：" + errMap.get("errorMassage"));
				// 開始日と終了日の整合性チェックはエラーとする。
				}else if ("2".equals(errMap.get("errorCode"))) {
					errorList.add("ユーザー情報：" + errMap.get("errorMassage"));
				}
			}

			// 管理者モードで表示がadmin以外なら所属部門と業務ロール表示しているのでチェック
			if(logonIsKanri && !viewIsAdmin) {
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
					bumonMap.put("bumon_cd",                  bumonCd[i]);
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
			}

			// エラーがある場合は表示して終了する。
			if(0 < errorList.size()){
				successful = false;
				return "error";
			}

			//更新処理
			//ユーザー情報へ更新
			myLogic.updataUserInfoMail(userId, n2b(mailAddress), loginUserId);
			if (userInfoHenkou) {
				myLogic.updataUserInfoNoMei(userId, shainNo, replaceSpaceDelete(userSei), replaceSpaceDelete(userMei), loginUserId);
			}
			// パスワードへ更新
			if (isNotEmpty(password)) {
				int type = PasswordValidTerm.NORMAL;
				if (!userId.equals(loginUserId) &&
						"1".equals(setting.requirePassChange())) {
					// パスワード変更必須で、自分のパスワード更新でないなら、失効登録
					type = PasswordValidTerm.NONE;
				}
				myLogic.updataPassword(loginUserId, userId, password, type);
				if (userId.equals(loginUserId)) {
					logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_PASSCHG, LogDetail.PASSCHG_OWN);
				} else {
					logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_PASSCHG, LogDetail.PASSCHG_ADMIN);
				}
			}

			//管理者モードなら更新
			if(logonIsKanri) {
				// アカウント一時ロック状態を変更します。
				boolean curTmpLockFlg = "1".equals(userJouhouMap.get("tmp_lock_flg"));
				boolean newTmpLockFlg = "1".equals(accountTmpLockFlag);
				if (!newTmpLockFlg && (curTmpLockFlg != newTmpLockFlg)) {
					// 一時ロックを解除する
					myLogic.resetPasswordFailureCount(userId, true);
					logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_ACCOUNT, LogDetail.ACCOUNT_TMP_UNLOCK);
				}

				if(RegAccess.checkEnableKeihiSeisan()) {
					//セキュリティコードを更新
					String curSecurityPattern = (String)userJouhouMap.get("security_pattern");
					boolean curWfonlyFlg = "1".equals(userJouhouMap.get("security_wfonly_flg"));
					boolean newWfonlyFlg = "1".equals(securityWfonlyFlg);
					if (! ( curSecurityPattern.equals(securityPattern) && (curWfonlyFlg == newWfonlyFlg) ) ) {
						myLogic.updateSecurityPatterm(loginUserId, userId, securityPattern, newWfonlyFlg);
					}
				}
			}

			//管理者モードで表示対象がadmin以外なら更新
			if(logonIsKanri && !viewIsAdmin) {
				//有効期限
				myLogic.updataUserInfoKigen(userId, toDate(userYuukoukigenFrom), toDate(userYuukoukigenTo), loginUserId);

				if(RegAccess.checkEnableKeihiSeisan()) {
					//代理起票可能フラグを更新
					boolean curDairiKihyouFlg = "1".equals(userJouhouMap.get("dairikihyou_flg"));
					boolean newDairiKihyouFlg = "1".equals(dairiKihyouFlag);
					if (curDairiKihyouFlg != newDairiKihyouFlg) {
						myLogic.updateDairiKihyouFlag(loginUserId, userId, newDairiKihyouFlg);
					}

					//法人カード利用フラグを更新
					boolean curHoujinCardRiyouFlag = "1".equals(userJouhouMap.get("houjin_card_riyou_flag"));
					boolean newHoujinCardRiyouFlag = "1".equals(houjinCardRiyouFlag);
					if (curHoujinCardRiyouFlag != newHoujinCardRiyouFlag) {
						myLogic.updateHoujinCardRiyouFlag(loginUserId, userId, newHoujinCardRiyouFlag);
					}

					//法人カード識別用番号を更新
					String curHoujinCardShikibetsuyouNum = (String)userJouhouMap.get("houjin_card_shikibetsuyou_num");
					String newHoujinCardShikibetsuyouNum = houjinCardShikibetsuyouNum;
					if (!curHoujinCardShikibetsuyouNum.equals(newHoujinCardShikibetsuyouNum)) {
						myLogic.updateHoujinCardShikibetsuyouNum(loginUserId, userId, newHoujinCardShikibetsuyouNum);
					}

					//承認ルート変更権限レベルを更新
					String curShouninRouteHenkouLevel = (String)userJouhouMap.get("shounin_route_henkou_level");
					String newShouninRouteHenkouLevel = shouninRouteHenkouLevel;
					if (!curShouninRouteHenkouLevel.equals(newShouninRouteHenkouLevel)) {
						myLogic.updateShouninRouteHenkouLevel(loginUserId, userId, newShouninRouteHenkouLevel);
					}

					//マル秘設定権限を更新
					boolean curMaruhiKengenFlag = "1".equals(userJouhouMap.get("maruhi_kengen_flg"));
					boolean newMaruhiKengenFlag = "1".equals(maruhiKengenFlag);
					if (curMaruhiKengenFlag != newMaruhiKengenFlag) {
						myLogic.updateMaruhiKengenFlag(loginUserId, userId, newMaruhiKengenFlag);
					}

					//マル秘解除権限を更新
					boolean curMaruhiKaijyoFlag = "1".equals(userJouhouMap.get("maruhi_kaijyo_flg"));
					boolean newMaruhiKaijyoFlag = "1".equals(maruhiKaijyoFlag);
					if (curMaruhiKaijyoFlag != newMaruhiKaijyoFlag) {
						myLogic.updateMaruhiKaijyoFlag(loginUserId, userId, newMaruhiKaijyoFlag);
					}
				}

				// アカウント永続ロック状態を変更します。
				boolean curLockFlg = "1".equals(userJouhouMap.get("lock_flg"));
				boolean newLockFlg = "1".equals(accountLockFlag);
				if (curLockFlg != newLockFlg) {
					// 状態を変更する
					myLogic.updateUserLockFlag(loginUserId, userId, newLockFlg);
					if (newLockFlg) {
						logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_ACCOUNT, LogDetail.ACCOUNT_LOCK);
					} else {
						logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_ACCOUNT, LogDetail.ACCOUNT_UNLOCK);
					}
				}

				// 拠点入力のみ使用かどうかを変更(「経費精算オプションON且つ拠点入力オプションOFF」「経費精算オプションOFF且つ拠点入力オプションON」の時は一律0で保存)
				if (RegAccess.checkEnableKeihiSeisan() && RegAccess.checkEnableZaimuKyotenOption()) {
					boolean curZaimuKyotenNyuryokuOnlyFlg = "1".equals(userJouhouMap.get("zaimu_kyoten_nyuryoku_only_flg"));
					boolean newZaimuKyotenNyuryokuOnlyFlg = "1".equals(zaimuKyotenNyuryokuOnlyFlg);
					if (curZaimuKyotenNyuryokuOnlyFlg != newZaimuKyotenNyuryokuOnlyFlg) {
						myLogic.updatenewzaimuKyotenNyuryokuOnlyFlg(loginUserId, userId, newZaimuKyotenNyuryokuOnlyFlg);
					}
				}else {
					myLogic.updatenewzaimuKyotenNyuryokuOnlyFlg(loginUserId, userId, false);
				}

				// 所属部門割り当てを更新（delete ⇒ insert）
				myLogic.deleteShozokuBumonWariate(userId);
				int hyoujiJun = 0;
				for(int i=0; i <bumonCd.length; i++) {
					hyoujiJun++;
					myLogic.insertShozokuBumonWariate(bumonCd[i], bumonRoleId[i], userId, daihyouFutanBumonCd[i], toDate(bumonYuukoukigenFrom[i]), toDate(bumonYuukoukigenTo[i]), hyoujiJun, loginUserId);
				}

				// 業務ロール割り当てを更新（delete ⇒ insert）
				if(!gaibuYobidashiFlag) {
					myLogic.deleteGyoumuRoleWariate(userId);
					if(gyoumuRoleId != null) {
						for(int i = 0; i < gyoumuRoleId.length; i++){
							myLogic.insertGyoumuRoleWariate(userId, gyoumuRoleId[i], toDate(gyoumuRoleYuukoukigenFrom[i]), toDate(gyoumuRoleYuukoukigenTo[i]), gyoumuRoleShoriBumonCd[i], loginUserId);
						}
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
	 * 削除イベント
	 * @return ResultName
	 */
	public String sakujo(){

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
			hissuCheck(3);
			checkString(userId,      1, 30, "ユーザーID",      true); // KEY

			//2.データ存在チェック
			// ユーザーIDが存在するかチェックする。
			if(!EteamCommon.dataSonzaiCheck(connection, userId, EteamCommon.CheckTable.USER_INFO_KEY_ID) ){
				throw new EteamDataNotFoundException();
			}

			//3.アクセス可能チェック（権限チェック）
			checkAccess(connection, true, true);

			//未承認伝票の起票者は消しちゃだめ
			if (wfLogic.existsMikanryouDenpyou(userId)) {
				errorList.add("当ユーザーが起票した、未完了の申請が残っている為、ユーザーを削除することができません。");
				return "error";
			}

			//4.処理
			myLogic.deleteUserInfo(userId);            // ユーザー情報を削除
			myLogic.deletePassword(userId);            // パスワードを削除
			myLogic.deleteShozokuBumonWariate(userId); // 所属部門割り当てを削除
			myLogic.deleteGyoumuRoleWariate(userId);   // 業務ロール割り当てを削除
			logLogic.insertLog(loginUserInfo, userId, LogType.TYPE_ACCOUNT, LogDetail.ACCOUNT_DELETE);

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
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		logLogic = EteamContainer.getComponent(SecurityLogLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
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

		//初期表示用の代表負担部門コードと代表負担名を取得（所属部門管理）
		GMap shokiDaihyouFutanBumonList = bumonUserLogic.shokiDaihyouFutanBumon(userId);
		if(shokiDaihyouFutanBumonList != null){
			shokiDaihyouFutanBumonCd = shokiDaihyouFutanBumonList.get("daihyou_futan_bumon_cd").toString();
			shokiDaihyouFutanBumonName = shokiDaihyouFutanBumonList.get("futan_bumon_name").toString();
		}else{
			shokiDaihyouFutanBumonCd="";
			shokiDaihyouFutanBumonName="";
		}
		//初期表示用の処理部門コードと処理部門名を取得（業務ロール管理）
		GMap shokiShoriBumonList = bumonUserLogic.selectValidShozokuBumon(BUMON_CD.ZENSHA);
		gyoumuRoleShokiShoriShozokuBumonCd =shokiShoriBumonList.get("bumon_cd").toString();
		gyoumuRoleShokiShoriShozokuBumonName = shokiShoriBumonList.get("bumon_name").toString();

		// 承認ルート変更権限レベルリストの取得
		henkouKengenLevelList = systemLogic.loadNaibuCdSetting("shounin_route_henkou_level");
		henkouKengenDomain = EteamCommon.mapList2Arr(henkouKengenLevelList, "naibu_cd");

		// 部門ロールのデータ取得。（エラー時のデータ保持の為）
		bumonRoleList = bumonUserLogic.selectBumonRole();

		// ユーザー代表負担部門未入力チェック取得
		this.userDaihyouFutanBumonMinyuuryokuCheck = this.setting.userDaihyoufutanbumonMinyuuryokuCheck();
	}

	/**
	 * 当画面共通のアクセス制御チェックを行う。
	 * 一般ユーザーは自分自身の変更しかできないように。
	 * @param connection コネクション
	 * @param kanriOnly 管理者専用機能である(一般ユーザーが使ったらエラー)
	 * @param adminNg adminユーザーに対しては操作不可能である
	 */
	protected void checkAccess(EteamConnection connection, boolean kanriOnly, boolean adminNg) {
		//管理権限で入っている？
		String accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, loginUserInfo.getGyoumuRoleId(), "CO");
		if (accessAuthority.equals("SU") || accessAuthority.equals("CO")) {
			logonIsKanri = true;
		}

		//adminユーザーの変更である？
		if(UserId.ADMIN.equals(userId)) {
			viewIsAdmin = true;
		}

		//ユーザー情報の変更可否 = 業務ロールで入っている OR 一般ユーザーで入っていて変更できる設定である
		userInfoHenkou = logonIsKanri || setting.userInfoHenkou().equals("1");

		// 一般ユーザーの場合、ログインユーザーとユーザーIDが同じかチェックする。
		if(accessAuthority.length() == 0 && !loginUserInfo.getTourokuOrKoushinUserId().equals(userId)) {
			throw new EteamAccessDeniedException(); // 不正アクセス
		}

		// 削除機能の場合、一般ユーザーからは使えない
		if (kanriOnly && accessAuthority.length() == 0) {
			throw new EteamAccessDeniedException(); // 不正アクセス
		}

		// 削除機能はadminユーザーに対しては不可能
		if (adminNg && viewIsAdmin) {
			throw new EteamAccessDeniedException(); // 不正アクセス
		}
	}
}