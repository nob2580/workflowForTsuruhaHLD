package eteam.gyoumu.bumonkanri;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門変更画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	
	/** 部門名 */
	String[] bumonName;
	/** 親部門コード */
	String[] oyaBumonCd;
	/** 親部門名 */
	String[] oyaBumonName;
	/** 日付(FROM) */
	String[] kigenFrom;
	/** 日付(TO) */
	String[] kigenTo;
	/** セキュリティパターン **/
	String[] securityPattern;

//＜画面入力以外＞
	/** 部門コード */
	String bumonCd;
	/** セキュリティパターン入力制御 */
	boolean securityPatternFlag;
	/** 外部呼出し制御 */
	boolean gaibuYobidashiFlag=false;
	/** 外部呼出しコネクション */
	EteamConnection gaibuConnection;
	/** 外部呼出しユーザーId */
	String gaibuKoushinUserId;

	/** タブ名称 */
	String tabMeisyo[];
	/** 基準日 */
	String kijunDate;
	/** 表示タブインデックス */
	int tabIndex;
	
//＜部品＞
	/** 部門管理機能ロジック */
	BumonKanriLogic myLogic;
	/** 部門・ユーザ管理機能ロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		int bumonCdMaxLength = Integer.parseInt(setting.bumonCdKetasuu());
		checkString(bumonCd,       1, bumonCdMaxLength,  "部門コード",   true); // KEY
		if(bumonName != null){
			for (int i = 0; i < bumonName.length; i++) {
				checkString(bumonName[i],     1, 20,                "部門名",       false);
				checkString(oyaBumonCd[i],    1, bumonCdMaxLength,  "親部門コード", false);
				checkDate(kigenFrom[i], "有効期限開始日", false);
				checkDate(kigenTo[i],   "有効期限終了日", false);
				checkNumber(securityPattern[i],  1,  4, "セキュリティパターン", false);
			}
		}
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			{bumonCd ,"部門コード"			,"2", "2", "2", }, // KEY
		};
		hissuCheckCommon(list, eventNum);
		if(bumonName != null){
			for (int i = 0; i < bumonName.length; i++) {
				int ip = i + 1;
				list = new String[][]{
						//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
						{bumonName[i] ,"部門名["+ ip + "]"				,"0", "1", "0", },
						{oyaBumonCd[i] ,"親部門コード["+ ip + "]"			,"0", "1", "0", },
						{kigenFrom[i] ,"有効期限開始日["+ ip + "]"		,"0", "1", "0", },
						{kigenTo[i] ,"有効期限終了日["+ ip + "]"		,"0", "0", "0", },
						{securityPattern[i],"セキュリティパターン["+ ip + "]"	,"0", "0", "0", },
					};
				hissuCheckCommon(list, eventNum);
			}
		}
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
			formatCheck();
			hissuCheck(1);
			if (!errorList.isEmpty())
			{
				return "error";
			}
			
			
			//2.データ存在チェック
			List<GMap> bumonList = bumonUserLogic.selectShozokuBumonAll(bumonCd);
			
			if(bumonList.isEmpty()){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			// 部門コードが全社の場合、アクセスエラーとする
			if (bumonCd.matches("^[0]+$")) {
				throw new EteamAccessDeniedException(); // アクセスエラー
			}
			
			
			//4.処理
			// 表示データ取得
			int length = bumonList.size();
			bumonName = new String[length];
			oyaBumonCd = new String[length];
			oyaBumonName = new String[length];
			securityPattern = new String[length];
			kigenFrom = new String[length];
			kigenTo = new String[length];
			tabMeisyo = new String[length];
			
			
			for (int i = 0; i < length; i++) {
				GMap bumonMap = bumonList.get(i);
				bumonName[i]       = bumonMap.get("bumon_name").toString();
				oyaBumonCd[i]      = bumonMap.get("oya_bumon_cd").toString();
				securityPattern[i] = bumonMap.get("security_pattern").toString();
				
				// 日付を表示用フォーマットに変換します。
				kigenFrom[i]       = formatDate(bumonMap.get("yuukou_kigen_from"));
				kigenTo[i]         = formatDate(bumonMap.get("yuukou_kigen_to"));
				
				//基準日が指定されていたら初期表示のタブを制御
				Date chkDate;
				if(isEmpty(kijunDate)){
					chkDate = new Date(System.currentTimeMillis());
				}else{
					chkDate = toDate(kijunDate);
				}
				if( (chkDate.after(toDate(kigenFrom[i])) && chkDate.before(toDate(kigenTo[i]))) || chkDate.equals(toDate(kigenFrom[i])) || chkDate.equals(toDate(kigenTo[i])) ){
					tabIndex = i;
				}
				
				// 親部門コードを使用し、部門名を取得します。
				oyaBumonName[i] = bumonUserLogic.selectValidShozokuBumon(oyaBumonCd[i], toDate(kigenFrom[i])).get("bumon_name").toString();
				tabMeisyo[i] = kigenFrom[i] + "～";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 変更イベント
	 * @return ResultName
	 */
	public String henkou() {
		
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
			formatCheck();
			hissuCheck(2);
			if (!errorList.isEmpty())
			{
				return "error";
			}
			
			//2.データ存在チェック
			// 所属部門が存在するかチェックする。
			//※外部呼出し時(一括登録からの呼出時)は子部門が先に登録される場合があるため、チェックさせない
			//  既存データかCSVに親部門があるかのチェックは一括登録側で実施させる
			List<GMap> bumonLst = bumonUserLogic.selectShozokuBumonAll(bumonCd);
			if(bumonLst.isEmpty() && !gaibuYobidashiFlag){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			//4.処理
			
			//タブごとのデータを取得して重複チェック・連続チェック
			// 有効期限重複チェック
			LinkedHashSet<Map<String, String>> bumonHashSet = new LinkedHashSet<Map<String, String>>();
			for(int i = 0; i < bumonName.length; i++) {
				
				// 有効期限終了日が入力されていない場合、「9999/12/31」を設定します。
				if (isEmpty(kigenTo[i])) { kigenTo[i] = "9999/12/31"; }
				
				// 有効期限の共通チェックを行います。
				for(Map<String, String> errMap: EteamCommon.kigenCheck(kigenFrom[i], kigenTo[i])) {
					// 開始日と終了日の整合性チェックのみエラーとする。
					if ("2".equals(errMap.get("errorCode"))) {
						errorList.add("部門：" + (i+1) + "ページ目：" + bumonName[i] + "：" + errMap.get("errorMassage"));
					}
				}
				// レコードの重複チェック
				Map<String, String> bumonMap = new HashMap<String, String>();
				bumonMap.put("bumon_yuukoukigen_from",    kigenFrom[i]);
				bumonMap.put("bumon_yuukoukigen_to",      kigenTo[i]);
				for (Map<String,String>map : bumonHashSet) {
					if(EteamCommon.isKigenDuplicate(map.get("bumon_yuukoukigen_from"),map.get("bumon_yuukoukigen_to"),bumonMap.get("bumon_yuukoukigen_from"),bumonMap.get("bumon_yuukoukigen_to"))){
						errorList.add("部門：" + (i+1) + "ページ目：有効期間が重複しています。");
						break;
						
					}
				}
				bumonHashSet.add(bumonMap);
			}
			
			
			//有効期限連続チェック
			for(int i = 0; i < bumonName.length; i++) {
				Date endDate = toDate(kigenTo[i]);
				Boolean lastDayFlg = true;
				Boolean hasNextFlg = false;
				//開始日が終了日+1のデータが別データ群にあるかチェック
		        Calendar nextStDt = Calendar.getInstance();
		        nextStDt.setTime(endDate);
		        nextStDt.add(Calendar.DATE, 1);
		        endDate = nextStDt.getTime();
				for(int j = 0; j < bumonName.length; j++) {
					if (i == j)
					{
						continue;
					}
					if( endDate.compareTo(toDate(kigenTo[j])) < 0){
						lastDayFlg = false;
					}
					if( endDate.compareTo(toDate(kigenFrom[j])) == 0){
						hasNextFlg = true;
						break;
					}
				}
				//開始日が終了日+1のデータが別データ群に無く、自身が一番未来のデータでなければエラー
				if(!hasNextFlg && !lastDayFlg){
					errorList.add("有効期限は全データで連続している必要があります。");
					break;
				}
			}
			
			//親部門チェックの処理は外部呼出しでは実行させないようにする
			//(親部門が後に登録される場合があるため)
			
			// 入力された親部門コードが存在するかチェックする。
			if(!gaibuYobidashiFlag){
				for(int i = 0; i < bumonName.length; i++) {
					if(bumonUserLogic.selectValidShozokuBumon(oyaBumonCd[i], toDate(kigenFrom[i])) == null){
						errorList.add("部門：" + (i+1) + "ページ目：入力した親部門は["+kigenFrom[i]+"]時点で有効ではありません。");
					}
					if(bumonUserLogic.selectValidShozokuBumon(oyaBumonCd[i], toDate(kigenTo[i])) == null){
						errorList.add("部門：" + (i+1) + "ページ目：入力した親部門は["+kigenTo[i]+"]時点で有効ではありません。");
					}
				}
			}
			
			// 選択した部門と親部門が同じコードでは無いことをチェックします。
			for(int i = 0; i < bumonName.length; i++) {
				if(bumonCd.equals(oyaBumonCd[i])){
					errorList.add("部門：" + (i+1) + "ページ目：選択した部門と同じ部門が親部門に選択されています。");
				}
			}
			
			// 選択した部門の子部門が選択されていないことをチェックします。
			for(int i = 0; i < bumonName.length; i++) {
				for(GMap map: bumonUserLogic.selectBumonTreeWithKijunbi(bumonCd, toDate(kigenFrom[i]))) {
					if(map.get("bumon_cd").toString().equals(oyaBumonCd[i])){
						errorList.add("部門：" + (i+1) + "ページ目：選択した部門の["+kigenFrom[i]+"]時点の子部門が親部門に選択されています。");
					}
				}
				for(GMap map: bumonUserLogic.selectBumonTreeWithKijunbi(bumonCd, toDate(kigenTo[i]))) {
					if(map.get("bumon_cd").toString().equals(oyaBumonCd[i])){
						errorList.add("部門：" + (i+1) + "ページ目：選択した部門の["+kigenTo[i]+"]時点の子部門が親部門に選択されています。");
					}
				}
			}
			
			// エラーが有ればエラーメッセージを表示し処理を終了します。
			if(0 < errorList.size()){
				return "error";
			}

			//更新対象分のデータを削除します。
			myLogic.delete(bumonCd);
			// 入力データを登録します。
			for(int i = 0; i < bumonName.length; i++) {
				myLogic.insert(bumonCd, bumonName[i], oyaBumonCd[i], toDate(kigenFrom[i]), toDate(kigenTo[i]), securityPattern[i], gaibuKoushinUserId != null ? gaibuKoushinUserId : getUser().getTourokuOrKoushinUserId());
			}
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
	 * 削除イベント(全期間のデータ削除)
	 * @return ResultName
	 */
	public String sakujo() {
		
		try(EteamConnection connection = EteamConnection.connect()){
			
			initParts(connection);
			displaySeigyo(connection);
			
			//1.入力チェック
			hissuCheck(3);
			//formatCheck(); //bumonCdがあるかだけチェックすればよい
			if(!errorList.isEmpty()) throw new EteamIllegalRequestException();
			
			//2.データ存在チェック
			// 所属部門が存在するかチェックします。
			if(!EteamCommon.dataSonzaiCheck(connection, bumonCd, EteamCommon.CheckTable.SHOZOKU_BUMON_KEY_CD_ALL_DATE)){
				throw new EteamDataNotFoundException();
			}
			
			//いずれかの期間で該当部門を親部門とするコードが存在する場合は削除させない
			if(myLogic.isKobumonExist(bumonCd)){
				errorList.add("本部門を親部門とするデータが存在するため、部門を削除できません。");
				return "error";
			}
			

			//3.アクセス可能チェック
			//4.処理

			
			// 対象の所属部門を削除します。
			myLogic.delete(bumonCd);
			connection.commit();
			
			//5.戻り値を返す
			return "success";
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
		securityPatternFlag = setting.keihimeisaiSecurityPsettei().equals("2");
		if (bumonName != null) {
			tabMeisyo = new String[bumonName.length];
			for(int i = 0; i < bumonName.length; i++) {
				String tmpStr = isEmpty(kigenFrom[i]) ? "(開始日未入力)" : kigenFrom[i]+"～";
				tabMeisyo[i] = tmpStr;
			}
		}

	}

}
