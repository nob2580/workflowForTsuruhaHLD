package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.BumonSuishouRouteCategoryLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門推奨ルート追加画面Action
 */
@Getter @Setter @ToString
public class BumonSuishouRouteTsuikaAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票区分 */
	protected String denpyouKbn;
	/** デフォルト設定 */
	protected String defaultFlg;
	/** 仕訳枝番号 */
	protected String[] shiwakeEdaNo;
	/** 取引名称 */
	protected String[] torihikiName;
	/** 起票部門 */
	protected String bumon;
	/** 部門コード */
	protected String bumonCd;
	/** 金額開始 */
	protected String kingakuFrom;
	/** 金額終了 */
	protected String kingakuTo;
	/** 承認者 */
	protected String[] bumonRole;
	/** 部門ロールID */
	protected String[] bumonRoleCd;
	/** 処理権限 */
	protected String[] shoriKengen;
	/** 合議部署 */
	protected String[] gougiName;
	/** 合議部署ID */
	protected String[] gougiCd;
	/** 部門表示 */
	protected boolean[] bumonRoleHyouji;
	/** 合議枝番 */
	protected String[] gougiEdano;
	/** 有効期限開始日 */
	protected String yuukouKigenFrom;
	/** 有効期限終了日 */
	protected String yuukouKigenTo;
	
//＜画面入力以外＞

	/** 伝票リスト */
	protected List<GMap> denpyouList;
	/** 伝票プルダウンHTML文字列 */
	protected String denpyouPull;
	/** 承認者リスト */
	protected List<GMap> bumonRoleList;
	/** 部門推奨ルートリスト */
	List<GMap> bumonRouteList;
	/** 処理権限リスト */
	protected List<GMap> shoriKengenList;
	
	/** 外部呼出し制御 */
	boolean gaibuYobidashiFlag=false;
	/** 外部呼出しコネクション */
	EteamConnection gaibuConnection;
	/** 外部呼出しユーザーId */
	String gaibuKoushinUserId;
	/** csv登録時の部門推奨ルート(親)枝番 */
	String gaibuEdaNo;
	
	/** 凡例表示コード */
	protected String[] hanreiHyoujiCd;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザーID */
	protected String loginUserId;
	/** ログインユーザー区分 */
	protected String loginUserKbu;
	/** URLパス */
	protected String urlPath;
	/** 会計伝票フラグ */
	protected boolean isKaikeiDenpyou;
	/** イベントコードのマジックナンバー:会計伝票時の更新 */
	protected static final int updateEventKaikei = 1;
	/** イベントコードのマジックナンバー:会計伝票ではない場合の更新 */
	protected static final int updateEvent = 2;
	/** 非デフォルト設定*/
	protected static final String NOT_DEFAULT = "0";

	/** エラーフラグ */
	protected String isError = "0";

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDomain(defaultFlg, Domain.FLG, "デフォルト設定フラグ", false);
		if(NOT_DEFAULT.equals(defaultFlg)){
			for(int i = 0; i < shiwakeEdaNo.length ; i++){
				int ip = i + 1;
				checkString(shiwakeEdaNo[i], 1 ,10, "取引コード：" + ip + "行目", false);
			}
		}
		checkKingakuOver0(kingakuFrom, "金額開始", false);
		checkKingakuOver0(kingakuTo, "金額終了", false);
		checkDate(yuukouKigenFrom, "有効期限開始日", false);
		checkDate(yuukouKigenTo, "有効期限終了日", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		hissuCheckCommon(new String[][] {
				{denpyouKbn, "伝票", "1", "1" }, // 値、和名、EVENT1のフラグ
				{bumon, "起票部門", "1", "1"},
				{yuukouKigenFrom, "有効期限開始", "1", "1"},
				{kingakuFrom, "金額開始", "1", "0"},
				{kingakuTo, "金額終了", "1", "0"}
		}, eventNum);
		for (int i = 0; i < bumonRole.length; i++) {
			if (bumonRoleHyouji[i]) {
				hissuCheckCommon(new String[][] {
					{bumonRoleCd[i], "役割", "1", "1"},
					{shoriKengen[i], "承認処理権限", "1", "1"},
				}, eventNum);
			} else {
				hissuCheckCommon(new String[][] {
					{gougiCd[i], "合議部署", "1", "1"},
				}, eventNum);
			}
		}
	};


	/**
	 * E1:初期表示表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {
		
		// セッションからログインユーザー情報を取得する/
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()) {
			BumonSuishouRouteTsuikaLogic bsrtl = EteamContainer.getComponent(BumonSuishouRouteTsuikaLogic.class, connection);
			
			// 処理権限リストの取得
			shoriKengenList = bsrtl.loadShouninShoriKengen();
			// 伝票リストの取得
			denpyouList = createDenpyouList(bsrtl);
			// 伝票プルダウン文字列生成
			createDenpyouPullDown(connection);

			if (denpyouList.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			// 金額のデフォルト値を設定
			kingakuFrom = "1";
			kingakuTo = "999,999,999,999";
			
			return "success";
		}
	}
	
	/**
	 * E2:部門推奨ルート登録処理
	 * @return 成功：success, 失敗:error
	 */
	public String touroku() {
		
		// セッションからログインユーザー情報を取得する
		// 通常の変更処理の際はこちらで取得
		// 一括登録処理の際は既に設定済み
		if(loginUserInfo == null) {
			loginUserInfo = getUser();
		}
		loginUserId = loginUserInfo.getSeigyoUserId();

		EteamConnection connection;
		if(!gaibuYobidashiFlag) {
			connection = EteamConnection.connect();
		}
		else {
			connection = getGaibuConnection();
		}

		try {
			BumonSuishouRouteCategoryLogic bsrcl = EteamContainer.getComponent(BumonSuishouRouteCategoryLogic.class, connection);
			BumonSuishouRouteTsuikaLogic bsrtl = EteamContainer.getComponent(BumonSuishouRouteTsuikaLogic.class, connection);
			KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			BumonUserKanriCategoryLogic bukcl = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			
			//取引毎に設定可能かどうかを判定
			boolean torihikiFlg = bsrtl.judgeTorihikiInput(navi.findDenpyouKanri(denpyouKbn).get("route_torihiki_flg").toString());
			
			// 処理権限リストの取得
			shoriKengenList = bsrtl.loadShouninShoriKengen();
			// 伝票リストの再取得
			denpyouList = createDenpyouList(bsrtl);
			// 伝票プルダウン文字列生成
			createDenpyouPullDown(connection);
			
			// 伝票区分から会計伝票か判断する
			isKaikeiDenpyou = navi.routeBunkiAri(denpyouKbn);
			
			// 金額が0円の場合、ブランクに変換する
			if( !denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) && 
				!denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN) && 
				!denpyouKbn.equals(DENPYOU_KBN.KARIBARAI_SHINSEI) &&
				!denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) &&
				!denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI) &&
				!denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) &&
				!(denpyouKbn.length() > 1 && denpyouKbn.substring(0, 1).equals("B") && isKaikeiDenpyou)){
				if ("0".equals(kingakuFrom)) {
					kingakuFrom = "";
				}
				if ("0".equals(kingakuTo)) {
					kingakuTo = "";
				}
			}
			
			// 1.入力チェック
			if (isKaikeiDenpyou) {
				hissuCheck(updateEventKaikei);
			} else {
				hissuCheck(updateEvent);
			}
			formatCheck();
			
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				isError = "1";
				// カンマを外した状態で画面に返す
				delComma();
				createTorihikiList();
				createBumonRoleList();
				return "error";
			}
			
			// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
			if (isEmpty(yuukouKigenTo)) {
				yuukouKigenTo = "9999/12/31";
			}
			
			// 有効期限の共通チェック
			for(Map<String, String> errMap: EteamCommon.kigenCheck(yuukouKigenFrom, yuukouKigenTo)) {
				// エラーコードが「0」以外の場合にエラーメッセージを追加する
				if (!"0".equals(errMap.get("errorCode"))) {
					errorList.add("有効期限" + errMap.get("errorMassage"));
				}
			}
			
			// 金額チェック
			// From<=TOになっていない場合はエラー
			if (isKaikeiDenpyou && toDecimal(kingakuFrom).compareTo(toDecimal(kingakuTo)) > 0) {
				errorList.add("金額の指定範囲が不正です。");
			}
			
			for (int i = 0; i < bumonRoleCd.length; i++) {
				// 役割の連続チェック
				if (i > 0 &&  (bumonRoleCd[i].equals(bumonRoleCd[i - 1]) && shoriKengen[i].equals(shoriKengen[i - 1])) && (gougiEdano[i].equals("") && gougiEdano[i - 1].equals(""))) {
					errorList.add("承認者が重複しています。");
					break;
				}
				// 合議の連続チェック
				if (i > 0 &&  gougiCd[i].equals(gougiCd[i - 1]) && (!gougiEdano[i].equals("") && !gougiEdano[i - 1].equals(""))) {
					errorList.add("合議が重複しています。");
					break;
				}
			}
			
			//相関チェック
			if(NOT_DEFAULT.equals(defaultFlg)){
				if(isShiwakeEdaNoInput()){
					if(!torihikiFlg){
						errorList.add("取引毎ルート登録が認められない伝票種別です。");
					}
				}else{
					errorList.add("取引を入力してください。");
				}
			}else{
				if(isShiwakeEdaNoInput()){
					if(torihikiFlg){
						errorList.add("デフォルト設定の場合、取引は入力できません。");
					}
				}
			}
			
			// 仕訳枝番号の重複チェック
			if(isShiwakeEdaNoInput()){
				for(int i=0 ; i < shiwakeEdaNo.length ; i++){
					for(int j = i+1 ; j < shiwakeEdaNo.length ; j++){
						if(!"".equals(shiwakeEdaNo[i])){
							if(shiwakeEdaNo[i].equals(shiwakeEdaNo[j])){
								errorList.add((i+1) + "行目：仕訳枝番号が重複しています。(" + (j+1) + "行目)");
							}
						}
					}
				}
			}
			
			// 2.データ存在チェック(重複チェック)
			if(NOT_DEFAULT.equals(defaultFlg)){
				for(int i=0 ; i < shiwakeEdaNo.length ; i++){
					if(!"".equals(shiwakeEdaNo[i])){
						if(null == kaikeiLogic.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdaNo[i]))){
							errorList.add((i+1) + "行目：仕訳枝番号が見つかりません。");
						} else if(! kaikeiLogic.bumonTorihikiCheck(denpyouKbn, Integer.parseInt(shiwakeEdaNo[i]), bumonCd)) {
							errorList.add((i+1) + "行目：該当起票部門では使用できません。");
						}
					}
				}
			}
			for (int i = 0; i < bumonRoleCd.length; i++) {
				if (bumonRoleHyouji[i]) {
					GMap role = bukcl.selectBumonRole(bumonRoleCd[i]);
					GMap shori = bsrtl.findShouninShoriKengen(shoriKengen[i]);
					if (role == null) {
						errorList.add((i+1) + "行目：役割が見つかりません。");
					}
					if (shori == null) {
						errorList.add((i+1) + "行目：承認処理権限が見つかりません。");
					}
				} else {
					GMap gougi = bukcl.findGougiBusho(gougiCd[i]);
					if (gougi == null) {
						errorList.add((i+1) + "行目：合議部署が見つかりません。");
					}
				}
			}
			
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				isError = "1";
				// カンマを外した状態で画面に返す
				delComma();
				createTorihikiList();
				createBumonRoleList();
				return "error";
			}
			
			if (!NOT_DEFAULT.equals(defaultFlg))
			{
				shiwakeEdaNo = null;
			}
			List<GMap> deplicateRec = bsrcl.checkDuplicate(denpyouKbn, bumonCd, defaultFlg, shiwakeEdaNo, toDecimal(kingakuFrom),
					toDecimal(kingakuTo), toDate(yuukouKigenFrom), toDate(yuukouKigenTo));
			
			// 重複レコード数
			int cnt = deplicateRec.size();

			if (cnt > 0) {
				errorList.add("重複レコードが存在します。");
			}

			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				isError = "1";
				// カンマを外した状態で画面に返す
				delComma();
				createTorihikiList();
				createBumonRoleList();
				return "error";
			}
			
			// 3.アクセス可能チェック
			
			// 4.登録処理
			List<String> shiwakeEdaNoArray = new ArrayList<String>();
			if(NOT_DEFAULT.equals(defaultFlg)){
				for(int i = 0 ; i < shiwakeEdaNo.length ; i++){
					if(!"".equals(shiwakeEdaNo[i])){
						shiwakeEdaNoArray.add(shiwakeEdaNo[i]);
					}
				}
			}
			if(!gaibuYobidashiFlag) {
				bsrtl.insertBumonSuishoRoute(denpyouKbn, bumonCd, defaultFlg, (0==shiwakeEdaNoArray.size())? null: shiwakeEdaNoArray.toArray() ,toDecimal(kingakuFrom),
						toDecimal(kingakuTo), bumonRoleCd, shoriKengen, gougiCd, gougiEdano, toDate(yuukouKigenFrom), toDate(yuukouKigenTo), loginUserInfo.getTourokuOrKoushinUserId());
				connection.commit();
			}else{
				bsrtl.insertBumonSuishoRouteWithEdano(denpyouKbn, bumonCd, Integer.parseInt(gaibuEdaNo), defaultFlg, (0==shiwakeEdaNoArray.size())? null: shiwakeEdaNoArray.toArray() ,toDecimal(kingakuFrom),
						toDecimal(kingakuTo), bumonRoleCd, shoriKengen, gougiCd, gougiEdano, toDate(yuukouKigenFrom), toDate(yuukouKigenTo), loginUserInfo.getTourokuOrKoushinUserId());
			}
			
			// 5.戻り値
			return "success";
		} finally {
			if(!gaibuYobidashiFlag) {
				connection.close();
			}
		}
	}
	
	/**
	 * 伝票リスト作成処理
	 * @param bsrtl 部門推奨ルート追加機能Logic
	 * @return 伝票リスト
	 */
	protected List<GMap> createDenpyouList(BumonSuishouRouteTsuikaLogic bsrtl) {
		
		// 有効期限内の伝票種別リストを取得する
		List<GMap> list = bsrtl.load();
		
		if (list.isEmpty()) {
			return list;
		}

		denpyouList = new ArrayList<GMap>();
		
		Object gyoumu_shubetsu = null;
		
		for (GMap map : list) {

			Object gyoumu_shubetsu_wk = map.get("gyoumu_shubetsu");
			
			if (gyoumu_shubetsu == null) {
				gyoumu_shubetsu = gyoumu_shubetsu_wk;
			} else {
				
				if (gyoumu_shubetsu.equals(gyoumu_shubetsu_wk)) {
					map.put("gyoumu_shubetsu", null);
				} else {
					gyoumu_shubetsu = gyoumu_shubetsu_wk;
				}
			}
			
			denpyouList.add(map);
		}
		
		return denpyouList;
	}
	
	/**
	 * 金額項目のカンマを除去する
	 */
	protected void delComma() {
		
		//カンマなしで、画面に戻す
		if (!kingakuFrom.isEmpty()) {
			kingakuFrom = String.valueOf(toDecimal(kingakuFrom));
		}
		
		if (!kingakuTo.isEmpty()) {
			kingakuTo = String.valueOf(toDecimal(kingakuTo));
		}
	}
	
	/**
	 * 取引リストを設定する
	 */
	protected void createTorihikiList() {
		if(null == shiwakeEdaNo){
			shiwakeEdaNo = new String[]{""};
			torihikiName = new String[]{""};
		}
	}
	
	/**
	 * 承認者リストを設定する
	 */
	protected void createBumonRoleList() {
		
		bumonRoleList = new ArrayList<GMap>();
		
		for(int i = 0; i <bumonRole.length; i++) {
			GMap bumonRoleMap = new GMap();
			bumonRoleMap.put("bumonRole", bumonRole[i]);
			bumonRoleMap.put("bumonRoleCd", bumonRoleCd[i]);
			bumonRoleMap.put("gougiName", gougiName[i]);
			bumonRoleMap.put("gougiCd", gougiCd[i]);
			bumonRoleMap.put("gougiEdano", gougiEdano[i]);
			bumonRoleMap.put("bumonRoleHyouji", bumonRoleHyouji[i]);
			bumonRoleMap.put("shoriKengen", shoriKengen[i]);

			bumonRoleList.add(bumonRoleMap);
		}
		
	}
	
	/**
	 * 伝票プルダウン文字列を生成する
	 * @param connection コネクション
	 */
	protected void createDenpyouPullDown(EteamConnection connection) {
		KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<select name='denpyouKbn' class='input-large'>");
		sb.append("<option></option>");

		for (int i = 0; i < denpyouList.size(); i++) {
			
			int cnt = navi.routeBunkiAri((String)denpyouList.get(i).get("denpyou_kbn")) ? 1 : 0;
			
			if (i == 0) {
				sb.append(" <optgroup label='" + denpyouList.get(i).get("gyoumu_shubetsu") + "'> ");
				sb.append(" <option value='" + denpyouList.get(i).get("denpyou_kbn") + "' data-version='" + denpyouList.get(i).get("version")  + "' data-cnt='" + cnt + "' data-routeTorihikiFlg='" + denpyouList.get(i).get("route_torihiki_flg") + "'" );
				
				if (denpyouList.get(i).get("denpyou_kbn").equals(denpyouKbn)) {
					sb.append(" selected ");
				}
				sb.append("> " + denpyouList.get(i).get("denpyou_shubetsu") + "</option>");
			} else if (i > 0 && denpyouList.get(i).get("gyoumu_shubetsu") != null) {
				sb.append("</optgroup>");
				sb.append(" <optgroup label='" + denpyouList.get(i).get("gyoumu_shubetsu") + "'> ");
				sb.append(" <option value='" + denpyouList.get(i).get("denpyou_kbn") + "' data-version='" + denpyouList.get(i).get("version")  + "' data-cnt='" + cnt + "' data-routeTorihikiFlg='" + denpyouList.get(i).get("route_torihiki_flg") + "'" );
				
				if (denpyouList.get(i).get("denpyou_kbn").equals(denpyouKbn)) {
					sb.append(" selected ");
				}
				sb.append("> " + denpyouList.get(i).get("denpyou_shubetsu") + "</option>");
			} else if (denpyouList.get(i).get("gyoumu_shubetsu") == null) {
				sb.append(" <option value='" + denpyouList.get(i).get("denpyou_kbn") + "' data-version='" + denpyouList.get(i).get("version")  + "' data-cnt='" + cnt + "' data-routeTorihikiFlg='" + denpyouList.get(i).get("route_torihiki_flg") + "'" );
				
				if (denpyouList.get(i).get("denpyou_kbn").equals(denpyouKbn)) {
					sb.append(" selected ");
				}
				sb.append("> " + denpyouList.get(i).get("denpyou_shubetsu") + "</option>");
			}
			if (i == denpyouList.size() - 1) {
				sb.append("</optgroup>");
			}
		}
		sb.append("</select>");
		denpyouPull = sb.toString();
	}
	
	/**
	 * 仕訳枝番号の入力状況を確認する。
	 * @return true:入力あり、false：未入力
	 */
	protected boolean isShiwakeEdaNoInput(){
		if(null == shiwakeEdaNo){
			return false;
		}
		boolean ret = false;
		for(int i = 0 ; i < shiwakeEdaNo.length ; i++){
			if(!"".equals(shiwakeEdaNo[i])){
				ret = true;
			}
		}
		return ret;
		
	}
}
