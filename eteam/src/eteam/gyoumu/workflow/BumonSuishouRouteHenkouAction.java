package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
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
 * 部門推奨ルート変更画面Action
 */
@Getter @Setter @ToString
public class BumonSuishouRouteHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票区分 */
	protected String denpyouKbn;
	/** バージョン */
	protected String version;
	/** 伝票名 */
	protected String denpyouName;
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
	/** 枝番号 */
	protected String edano;
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
	/** 取引毎設定フラグ */
	protected boolean torihikiFlg;
	/** イベントコードのマジックナンバー:会計伝票時の更新 */
	protected static final int updateEventKaikei = 1;
	/** イベントコードのマジックナンバー:会計伝票ではない場合の更新 */
	protected static final int updateEvent = 2;
	/** 稟議カウント */
	protected int ringiCnt=0;
	/** 非デフォルト設定*/
	protected static final String NOT_DEFAULT = "0";

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
		
		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()) {
			BumonSuishouRouteHenkouLogic bsrtl = EteamContainer.getComponent(BumonSuishouRouteHenkouLogic.class, connection);
			KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			
			//取引毎に設定可能かどうかを判定
			torihikiFlg = bsrtl.judgeTorihikiInput(navi.findDenpyouKanri(denpyouKbn).get("route_torihiki_flg").toString());
			
			// 部門推奨ルート情報の取得
			bumonRouteList = bsrtl.getBumonSuishouRouteList(denpyouKbn, bumonCd, Integer.parseInt(edano));
			
			if (bumonRouteList.isEmpty()) {
				throw new EteamDataNotFoundException();
			}
			
			bumonRoleList = new ArrayList<GMap>();
			// 処理権限リストの取得
			shoriKengenList = bsrtl.loadShouninShoriKengen();
			
			bumonRole = new String[bumonRouteList.size()];
			bumonRoleCd = new String[bumonRouteList.size()];
			gougiName = new String[bumonRouteList.size()];
			gougiCd = new String[bumonRouteList.size()];
			
			// 画面項目に設定
			for(GMap map : bumonRouteList) {
				
				GMap bumonRoleMap = new GMap();
				
				denpyouKbn = map.getString("denpyou_kbn");
				version = map.getString("version");
				denpyouName = map.getString("denpyou_shubetsu");
				bumon = map.getString("bumon_name");
				bumonCd = map.getString("bumon_cd");
				
				edano = map.getString("edano");
				defaultFlg =  map.getString("default_flg");
				kingakuFrom = (map.get("kingaku_from") == null) ? "" : map.getString("kingaku_from");
				kingakuTo =(map.get("kingaku_to") == null) ? "" : map.getString("kingaku_to");
				
				if (map.get("gougi_pattern_no") == null ) {
					bumonRoleMap.put("bumonRole", map.getString("bumon_role_name"));
					bumonRoleMap.put("bumonRoleCd", map.getString("bumon_role_id"));
					bumonRoleMap.put("bumonRoleHyouji", true);
				} else {
					bumonRoleMap.put("gougiName", map.getString("gougi_name"));
					bumonRoleMap.put("gougiCd", map.getString("gougi_pattern_no"));
					bumonRoleMap.put("gougiEdano", map.getString("gougi_edano"));
					bumonRoleMap.put("bumonRoleHyouji", false);
				}
				bumonRoleMap.put("shoriKengen", map.getString("shounin_shori_kengen_no"));
				
				yuukouKigenFrom = formatDate(map.get("yuukou_kigen_from"));
				yuukouKigenTo = formatDate(map.get("yuukou_kigen_to"));
				
				bumonRoleList.add(bumonRoleMap);
				
			}

			ringiCnt = navi.routeBunkiAri(denpyouKbn) ? 1 : 0;
			
			List<GMap> shiwakeEdaNoList = bsrtl.getBumonSuishouRouteTorihikiList(denpyouKbn, bumonCd, Integer.parseInt(edano));
			
			if(0 != shiwakeEdaNoList.size()){
				torihikiName = new String[shiwakeEdaNoList.size()];
				shiwakeEdaNo = new String[shiwakeEdaNoList.size()];
				for(int i = 0 ; i < shiwakeEdaNoList.size() ; i++){
					GMap map = shiwakeEdaNoList.get(i);
					int shiwakeEdaNoTmp = Integer.parseInt(map.get("shiwake_edano").toString());
					GMap shiwakeP = kaikeiLogic.findShiwakePattern(denpyouKbn, shiwakeEdaNoTmp);
					torihikiName[i] = shiwakeP.get("torihiki_name").toString();
					shiwakeEdaNo[i] = map.get("shiwake_edano").toString();
				}
			}else{
				torihikiName = new String[]{""};
				shiwakeEdaNo = new String[]{""};
			}
			
			return "success";
		}
	}
	
	/**
	 * E2:部門推奨ルート更新処理
	 * @return 成功：success, 失敗:error
	 */
	public String koushin() {
		
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
		try{
			BumonSuishouRouteCategoryLogic bsrcl = EteamContainer.getComponent(BumonSuishouRouteCategoryLogic.class, connection);
			BumonSuishouRouteHenkouLogic bsrhl = EteamContainer.getComponent(BumonSuishouRouteHenkouLogic.class, connection);
			KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			BumonUserKanriCategoryLogic bukcl = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			
			//取引毎に設定可能かどうかを判定
			torihikiFlg = bsrhl.judgeTorihikiInput(navi.findDenpyouKanri(denpyouKbn).get("route_torihiki_flg").toString());
			
			// 処理権限リストの取得
			shoriKengenList = bsrhl.loadShouninShoriKengen();
			
			ringiCnt = navi.routeBunkiAri(denpyouKbn) ? 1 : 0;
			
			// 伝票区分から会計伝票か判断する
			isKaikeiDenpyou = navi.routeBunkiAri(denpyouKbn);
			
			// 金額が0円の場合、ブランクに変換する
			if(!(denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN)
				|| denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) 
				|| denpyouKbn.equals(DENPYOU_KBN.KARIBARAI_SHINSEI)
				|| denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN)
				|| denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI)
				|| denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)
				|| (denpyouKbn.length() > 1 && denpyouKbn.substring(0, 1).equals("B") && isKaikeiDenpyou))){
				if ("0".equals(kingakuFrom)) {
					kingakuFrom = "";
				}
				if ("0".equals(kingakuTo)) {
					kingakuTo = "";
				}
			}
			
			//1.入力チェック
			if (isKaikeiDenpyou) {
				hissuCheck(updateEventKaikei);
			} else {
				hissuCheck(updateEvent);
			}
			formatCheck();

			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				
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
				// 開始日と終了日の整合性チェックのみエラーとする。
				if ("2".equals(errMap.get("errorCode"))) {
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
			
			// 2.データ存在チェック
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
					GMap shori = bsrhl.findShouninShoriKengen(shoriKengen[i]);
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

			if (cnt == 1 && !edano.equals(deplicateRec.get(0).getString("edano"))) {
				// 枝番号が一致しなかった場合は重複レコード
				errorList.add("重複レコードが存在します。");

			} else if (cnt > 1) {
				errorList.add("重複レコードが存在します。");
			}

			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				
				// カンマを外した状態で画面に返す
				delComma();
				createTorihikiList();
				createBumonRoleList();
				return "error";
			}
			
			// 3.アクセス可能チェック
			
			// 4.処理
			// 更新処理
			List<String> shiwakeEdaNoArray = new ArrayList<String>();
			if(NOT_DEFAULT.equals(defaultFlg)){
				for(int i = 0 ; i < shiwakeEdaNo.length ; i++){
					if(!"".equals(shiwakeEdaNo[i])){
						shiwakeEdaNoArray.add(shiwakeEdaNo[i]);
					}
				}
			}
			bsrhl.updateBumonSuishoRoute(denpyouKbn, bumonCd, Integer.parseInt(edano), defaultFlg, (0==shiwakeEdaNoArray.size())? null: shiwakeEdaNoArray.toArray(), toDecimal(kingakuFrom),
					toDecimal(kingakuTo), bumonRoleCd, shoriKengen, gougiCd, gougiEdano, toDate(yuukouKigenFrom), toDate(yuukouKigenTo), loginUserInfo.getTourokuOrKoushinUserId());
			
			if(!gaibuYobidashiFlag) {
				connection.commit();
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
	 * E3:部門推奨ルート削除処理
	 * @return 成功：success, 失敗:error
	 */
	public String sakujo() {

		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		loginUserId = loginUserInfo.getSeigyoUserId();

		EteamConnection connection;
		if(!gaibuYobidashiFlag) {
			connection = EteamConnection.connect();
		}
		else {
			connection = getGaibuConnection();
		}
		try{
			BumonSuishouRouteHenkouLogic bsrtl = EteamContainer.getComponent(BumonSuishouRouteHenkouLogic.class, connection);
			
			// 4.処理
			// 更新処理
			bsrtl.deleteBumonSuishoRoute(denpyouKbn, bumonCd, Integer.parseInt(edano));

			if(!gaibuYobidashiFlag) {
				connection.commit();
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
	 * 部門ロールリストを設定する
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
