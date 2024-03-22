package eteam.gyoumu.bumonkanri;

import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門追加画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonTsuikaAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 部門コード */
	String bumonCd;
	/** 部門名 */
	String bumonName;
	/** 親部門名 */
	String oyaBumonName;
	/** 日付(FROM) */
	String kigenFrom;
	/** 日付(TO) */
	String kigenTo;
	/** セキュリティパターン **/
	String securityPattern;

//＜画面入力以外＞
	/** 親部門コード */
	String oyaBumonCd;
	/** セキュリティパターン入力制御 */
	boolean securityPatternFlag;
	/** 外部呼出し制御 */
	boolean gaibuYobidashiFlag=false;
	/** 外部呼出しコネクション */
	EteamConnection gaibuConnection;


//＜部品＞
	/** 部門管理機能ロジック */
	BumonKanriLogic myLogic;
	/** 部門・ユーザ管理機能ロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;

//＜入力チェック＞
	@Override
	public void formatCheck() {
		int bumonCdMaxLength = Integer.parseInt(setting.bumonCdKetasuu());
		checkBumonCd(bumonCd,      1, bumonCdMaxLength, "部門コード",   false);
		checkString(bumonName,     1, 20,               "部門名",       false);
		checkBumonCd(oyaBumonCd,   1, bumonCdMaxLength, "親部門コード", false);
		checkDate(kigenFrom, "有効期限開始日", false);
		checkDate(kigenTo,   "有効期限終了日", false);
		checkNumber(securityPattern,  1,  4, "セキュリティパターン", false);
	}

	@Override
	public void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{bumonCd ,"部門コード"			,"0", "1", },
				{bumonName ,"部門名"				,"0", "1", },
				{oyaBumonCd ,"親部門コード"			,"0", "1", },
				{kigenFrom ,"有効期限開始日"		,"0", "1", },
				{kigenTo ,"有効期限終了日"		,"0", "0", },
				{securityPattern,"セキュリティパターン"	,"0", "0", },
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
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
		}
		
		//5.戻り値を返す
		return "success";
	}
	
	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku() {
		
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
			if (!errorList.isEmpty())
			{
				return "error";
			}
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			
			// 入力された部門コードが「０」埋めではないかチェックします。
			if (bumonCd.matches("^[0]+$")) {
				errorList.add("入力した部門コードは使用できません。");
			}
			// 入力された部門コードが登録されていないかチェックします。
			if(EteamCommon.dataSonzaiCheck(connection, bumonCd, EteamCommon.CheckTable.SHOZOKU_BUMON_KEY_CD_ALL_DATE)){
				errorList.add("入力した部門コードはすでに登録されています。");
			}
			
			// 有効期限終了日が入力されていない場合、「9999/12/31」を代入します。
			if (isEmpty(kigenTo)) { kigenTo = "9999/12/31"; }
			
			// 入力された親部門コードが期間開始日・終了日時点で存在するかチェックします。
			if(bumonUserLogic.selectValidShozokuBumon(oyaBumonCd, toDate(kigenFrom)) == null){
				errorList.add("入力した親部門は["+kigenFrom+"]時点で有効ではありません。");
			}
			if(bumonUserLogic.selectValidShozokuBumon(oyaBumonCd, toDate(kigenTo)) == null){
				errorList.add("入力した親部門は["+kigenTo+"]時点で有効ではありません。");
			}
			
			// 有効期限の共通チェックを行います。
			for(Map<String, String> map: EteamCommon.kigenCheck(kigenFrom, kigenTo)) {
				// エラーコードが「0」以外の場合にエラーメッセージを追加します。
				if (!"0".equals(map.get("errorCode"))) {
					errorList.add(map.get("errorMassage"));
				}
			}
			
			// エラーが有ればエラーメッセージを表示し処理を終了します。
			if(0 < errorList.size()){
				return "error";
			}
			
			// 入力データを登録します。
			myLogic.insert(bumonCd, bumonName, oyaBumonCd, toDate(kigenFrom), toDate(kigenTo), securityPattern, getUser().getTourokuOrKoushinUserId());
			if(!gaibuYobidashiFlag) {
				connection.commit();
			}
			
			//5.戻り値を返す
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
		myLogic = EteamContainer.getComponent(BumonKanriLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
	}
	
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		// セキュリティパターンを表示するか
		securityPatternFlag = "2".equals(setting.keihimeisaiSecurityPsettei()) ? true : false;
	}
	
}
