package eteam.gyoumu.workflow;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.tsuuchi.DenpyouIchiranUpdateLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic.WfSeigyo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 承認ルート登録画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class ShouninRouteTourokuAction extends EteamAbstractAction {

//＜定数＞
	/** 起票者 */
	protected static final int KIHYOUSHA         = 1;
	/** 承認者 */
	protected static final int SHOUNINSHA        = 2;
	/** 最終承認者 */
	protected static final int SAISHU_SHOUNINSHA = 3;
	
//＜画面入力＞
//表示キー
	/** 伝票ID */
	String denpyouId;

//hidden持ち回り
	/** 伝票区分 */
	String denpyouKbn;
	/** バージョン */
	String version;
	/** 申請金額 */
	String shinseiKingaku;
	/** 基準日 */
	String kijunbi;
	
//画面選択内容
	/** 選択した部門コード */
	String choiceBumonCd;
	/** 追加指定の合議部署 */
	String addGougiBushoList;
	
	/** ユーザーID */
	String userId[];
	/** ユーザーフル名 */
	String userFullName[];
	/** 部門コード */
	String bumonCode[];
	/** 部門フル名 */
	String bumonFullName[];
	/** 部門ロールID */
	String bumonRoleId[];
	/** 部門ロール名 */
	String bumonRoleName[];
	
	/** 業務ロールID */
	String gyoumuRoleId[];
	/** 業務ロール名 */
	String gyoumuRoleName[];
	
	/** 追加可否フラグ */
	String addEnabled[];
	
	/** 承認ルート枝々番号 */
	String routeEdaedano[];
	/** 合議パターン番号 */
	String gougiPatternNo[];
	/** 合議枝番号 */
	String gougiEdano[];
	/** 合議名称 */
	String gougiName[];
	/** 承認処理権限番号 */
	String shoriKengenNo[];
	/** 承認処理権限名 */
	String shoriKengenName[];
	/** 合議承認状況枝番 */
	String gougiJoukyouEdano[];
	
	
	/** 承認人数コード */
	String shouninNinzuuCd[];
	/** 承認人数比率 */
	String shouninNinzuuHiritsu[];
	/** 合議部門内グループ */
	String gouginaiGroup[];
	/** 画面遷移時点での現在承認権所持ユーザの承認権限No */
	String genzaiUsrShouninKengen[];
	
	/** 追加合議フラグ */
	String addedGougi[];
	/** ルート内新規追加ユーザーフラグ */
	String addedUser[];
	/** 現在承認者フラグ */
	String isGenzaiShouninsha[];
	/** 合議内権限編集可否フラグ*/
	String kengenDisableFlg[];

//＜画面入力以外＞
	/** 伝票状態 */
	String denpyouJoutai;
	/** 参照伝票ID */
	String sanshouDenpyouId;
	/** ユーザー承認ルート変更権限 */
	String shouninRouteHenkouLevel;
	
	/** 部門リスト */
	List<GMap> bumonList;
	/** 承認者リスト */
	List<GMap> shouninshaList;
	
	/** 承認者ルート */
	List<GMap> shouninRouteList;
	/** 承認者ルート登録リスト */
	List<GMap> shouninRouteTourokuList;
	/** 最終承認者リスト */
	List<GMap> saishuuShouninshaList;
	
	// プルダウン等の候補値
	/** 承認処理権限DropDownList */
	List<GMap> shoriKengenList;
	/** 承認処理権限DropDownList(合議) */
	List<GMap> shoriKengenGougiList;
	/** 承認処理権限ドメイン */
	String[] shoriKengenDomain;
	/** 承認処理権限ドメイン(合議) */
	String[] shoriKengenGougiDomain;
	
	/** 注記文言 */
	String chuukiMongon;
	
	/** URLパス */
	String urlPath;
	
	/** ログインユーザー区分 */
	int loginUserKbu;

//＜部品＞	
	/** 承認ルート登録ロジック */
	ShouninRouteTourokuLogic myLogic;
	/** システムカテゴリロジック */
	SystemKanriCategoryLogic systemLogic;
	/** ワークフローカテゴリロジック */
	WorkflowCategoryLogic workflowLogic;
	/** 部門・ユーザー管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** ワークフローイベントロジック */
	WorkflowEventControlLogic wfLogic;
	/** 伝票一覧テーブルロジック */
	DenpyouIchiranUpdateLogic diLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouId,   19, 19,  "伝票ID",    true); // KEY
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{denpyouId ,"伝票ID"			,"2", "2", "2", "2", "2", }, // KEY
			};
		hissuCheckCommon(list, eventNum);
	};

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init() {
		//1.入力チェック
		formatCheck();
		hissuCheck(1);
		
		try(EteamConnection connection = EteamConnection.connect()){
			// ロジッククラスへコネクションを設定します。
			setConnection(connection);

			//2.データ存在チェック
			dataNotFoundCheck();
			//3.アクセス可能チェック
			accessDeniedCheck();
			
			//4.処理
			// 初期データの取得を行います。
			initDate(connection);
			
			// 承認ルートの取得
			shouninRouteList = workflowLogic.selectShouninRouteGougiList(denpyouId);
			
			// 承認ルート登録リストの作成
			shouninRouteTourokuList = new ArrayList<GMap>();
			boolean isEnabled = false;
			for(GMap map : shouninRouteList) {
				
				//画面表示用の枝枝番号を設定
				map.put("route_edaedano",  Integer.toString(Integer.parseInt(map.get("edano").toString()) - 1) );
				
				// 最終承認フラグが[1:最終承認者]の場合は追加しない。
				if("0".equals(map.get("saishu_shounin_flg").toString())) {
					if(loginUserKbu == KIHYOUSHA) { // 起票者の場合
						map.put("addEnabled", "1".equals(map.get("edano").toString()) ? false : true);
					}
					if(loginUserKbu == SHOUNINSHA) { // 承認者の場合
						if("1".equals(map.get("genzai_flg").toString())) {
							isEnabled = true;
						}
						map.put("addEnabled", isEnabled);
					}
					shouninRouteTourokuList.add(map);
				}
			}
			
			//画面表示・制御用にリストを作成
			createShouninRouteForGamen(shouninRouteTourokuList);
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 承認者検索イベント
	 * @return ResultName
	 */
	public String shouninshaKensaku() {
		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		
		try(EteamConnection connection = EteamConnection.connect()){
			// ロジッククラスへコネクションを設定します。
			setConnection(connection);

			//2.データ存在チェック
			dataNotFoundCheck();
			//3.アクセス可能チェック
			accessDeniedCheck();
			
			//4.処理
			// 初期データの取得を行います。
			initDate(connection);
			// 承認ルート登録リストの作成（再表示のデータ保持）
			shouninRouteTourokuList = createShouninRouteTourokuList();
			
			//画面表示・制御用にリストを作成
			createShouninRouteForGamen(shouninRouteTourokuList);
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku() {
		
		
		//1.入力チェック
		formatCheck();
		hissuCheck(3);
		
		try(EteamConnection connection = EteamConnection.connect()){
			// ロジッククラスへコネクションを設定します。
			setConnection(connection);
			List<GMap> bfRouteList = workflowLogic.selectShouninRoute(denpyouId);

			// 排他制御：承認ルートテーブルを行ロックします。
			workflowLogic.selectShouninRouteForUpdate(denpyouId);
			
			//2.データ存在チェック
			dataNotFoundCheck();
			//3.アクセス可能チェック
			accessDeniedCheck();
			
			//4.処理
			// 初期データの取得を行います。(再表示の保持と最終承認者取得のため)
			initDate(connection);
			
			//承認権限ドメインチェック
			if(shoriKengenNo != null){
				for (int i = 0; i < shoriKengenNo.length; i++) {
					if(gougiPatternNo[i].isEmpty()){
						checkDomain (shoriKengenNo[i], shoriKengenDomain, "承認権限：" + (i+1) + "行目", false);
					}else{
						checkDomain (shoriKengenNo[i], shoriKengenGougiDomain, "承認権限：" + (i+1) + "行目", false);
					}
				}
			}
			
			// 承認ルート登録リストの作成
			shouninRouteTourokuList = createShouninRouteTourokuList();
			//画面表示・制御用にリストを作成
			createShouninRouteForGamen(shouninRouteTourokuList);
			
			// 承認ルート登録リストが１件の場合、且つ最終承認者リストが０件の場合、エラーメッセージを追加する。
			if(shouninRouteTourokuList.size() == 1 && saishuuShouninshaList.size() == 0) {
				errorList.add("承認者が選択されていません。");
			}
			
			// 承認ルート連続チェック
			// ユーザーID、部門コード、部門ロールIDもしくは業務ロールIDが連続していないかチェックする。

			HashSet<GMap> lastTimeData = new HashSet<GMap>();
			String gougiptnBef = "";
			String edaedanoBef = "";
			
			String gougiedanoBef = "";
			String bumoncdBef = "";
			String bumonroleidBef = "";
			String shouninshorikengenBef = "";
			
			for(GMap shouninRouteTourokuMap : shouninRouteTourokuList) {
				String gyoumu_role_id = shouninRouteTourokuMap.get("gyoumu_role_id").toString();
				String gougi_pattern_no = shouninRouteTourokuMap.get("gougi_pattern_no").toString();
				String route_edaedano = shouninRouteTourokuMap.get("route_edaedano").toString();
				
				GMap thisTimeMap = new GMap();
				String errorMassge;
				//合議内ユーザの連続はチェックしない、ただし同一合議が連続している場合はエラー
				if(!isEmpty(gougi_pattern_no)) {
					errorMassge = "合議（" + shouninRouteTourokuMap.get("gougi_name").toString() + "）が連続しています。";
					if(gougiptnBef.equals(gougi_pattern_no) && !(edaedanoBef.equals(route_edaedano))){
						errorList.add(errorMassge);
					}
					
					//同一合議・部門・役割内で承認処理権限が一致していない場合はエラー
					String mapGougiEdaNo = shouninRouteTourokuMap.get("gougi_edano").toString();
					String mapBumonCd = shouninRouteTourokuMap.get("bumon_cd").toString();
					String mapBumonRoleId = shouninRouteTourokuMap.get("bumon_role_id").toString();
					String mapShouninShoriKengen = shouninRouteTourokuMap.get("shounin_shori_kengen_no").toString();
					
					if ( edaedanoBef.equals(route_edaedano) &&
						 gougiedanoBef.equals(mapGougiEdaNo) &&
						 bumoncdBef.equals(mapBumonCd) &&
						 bumonroleidBef.equals(mapBumonRoleId) ){
						
						if( !(shouninshorikengenBef.equals(mapShouninShoriKengen)) ){
							errorMassge = "合議[" + shouninRouteTourokuMap.get("gougi_name").toString() + "]"
										+ "[" + EteamCommon.connectBumonName(connection, mapBumonCd) + "]"
										+ "[" + shouninRouteTourokuMap.get("bumon_role_name").toString() + "]"
										+ "の処理権限が一致していません。";
							errorList.add(errorMassge);
						}
					}else{
						shouninshorikengenBef = mapShouninShoriKengen;
					}
					
					gougiedanoBef = mapGougiEdaNo;
					bumoncdBef = mapBumonCd;
					bumonroleidBef = mapBumonRoleId;
					
				}else if (isEmpty(gyoumu_role_id)) {
					thisTimeMap.put("a", shouninRouteTourokuMap.get("user_id").toString());
					thisTimeMap.put("b", shouninRouteTourokuMap.get("bumon_cd").toString());
					thisTimeMap.put("c", shouninRouteTourokuMap.get("bumon_role_id").toString());
					errorMassge = shouninRouteTourokuMap.get("user_full_name").toString() + "（"
								+ shouninRouteTourokuMap.get("bumon_full_name").toString() + "："
								+ shouninRouteTourokuMap.get("bumon_role_name").toString() + "）が連続しています。";
				} else {
					thisTimeMap.put("d", gyoumu_role_id);
					errorMassge = shouninRouteTourokuMap.get("gyoumu_role_name").toString() + "が連続しています。";
				}
				
				// 前回と同じデータかチェックする。
				if(lastTimeData.contains(thisTimeMap) && isEmpty(gougi_pattern_no)) {
					errorList.add(errorMassge);
			    }
				lastTimeData.clear();          // 中身をクリア
				lastTimeData.add(thisTimeMap); // 今回のデータを設定
				gougiptnBef = gougi_pattern_no;
				edaedanoBef = route_edaedano;
			}
			
			// 承認ルートの最後と最終承認者の最初が連続していないかチェックする。
			if (saishuuShouninshaList.size() > 0) {
				GMap thisTimeMap = new GMap();
				// 最終承認者の１件目を取得する。
				GMap saishuuShouninshaFirstMap = saishuuShouninshaList.get(0);
				thisTimeMap.put("d", saishuuShouninshaFirstMap.get("gyoumu_role_id").toString());
				
				if(lastTimeData.contains(thisTimeMap)) {
					errorList.add(saishuuShouninshaFirstMap.get("gyoumu_role_name").toString() + "は最終承認者と連続しています。");
				}
			}
			
			// チェックエラーの場合、エラーメッセージを表示して終了
			if(0 < errorList.size()){ return "error"; }
			
			// 承認ルートを削除 ※現在承認者以降が対象(起票者や処理済承認者は変更することがないので残す)
			int edano = (int)workflowLogic.findShouninRouteCur(denpyouId).get("edano");
			int firstEdano = edano;
			if (edano == 1)
			{
				edano = 2;
			}
			myLogic.deleteShouninRoute(denpyouId, edano);
			myLogic.deleteShouninRouteGougiOya(denpyouId, edano);
			myLogic.deleteShouninRouteGougiKo(denpyouId, edano);
			
			/* 最終承認者以外を承認ルートへ登録します。 */
			boolean isGenzaiSagyousha = false;
			String befRouteEdaedano = "";
			String befGougiedano = "";
			int gougiEdaedano = 0;
			int edanoForGougi = 0;
			
			
			List<GMap> userList = new ArrayList<GMap>();
			
			for(GMap map : shouninRouteTourokuList) {
				
				
				boolean isAddEnabled = Boolean.valueOf(map.get("addEnabled").toString());
				
				String genzai_flg = "0";
				if (!isGenzaiSagyousha) {
					boolean isGougi = !(n2b(map.get("gougi_pattern_no").toString()).isEmpty());
					
					if (loginUserKbu == KIHYOUSHA && !isAddEnabled && !isGougi) {
						genzai_flg= "1";
						isGenzaiSagyousha = true;
					}
					if (loginUserKbu == SHOUNINSHA && isAddEnabled && !isGougi) {
						genzai_flg= "1";
						isGenzaiSagyousha = true;
						
						
						//現在承認者ユーザの承認権限が後伺いに変えられていたら状況を記録
						if(genzai_flg.equals("1")){
							long kengenNo = Long.parseLong(n2b(map.get("shounin_shori_kengen_no").toString()));
							GMap kengenMap =  workflowLogic.findShouninShoriKengen(kengenNo);
							if  (kengenMap.get("kihon_model_cd") != null && "3".equals(kengenMap.get("kihon_model_cd")) ){
								GMap addMap = new GMap();
								addMap.put("userId",n2b(map.get("user_id").toString()));
								addMap.put("gyoumuRoleId",n2b(map.get("gyoumu_role_id").toString()));
								addMap.put("edano", (Integer.parseInt(map.get("route_edaedano").toString()) + 1) );
								addMap.put("gougi_edano", null );
								addMap.put("gougi_edaedano", null );
								userList.add(addMap);
							}
						}

					}
					
					//合議に現在承認者がいる場合
					if( Integer.parseInt(map.get("route_edaedano").toString()) == firstEdano - 1 && isGougi ){
						genzai_flg= "1";
						isGenzaiSagyousha = true;
					}
				}
				
				
				String route_edaedano = n2b(map.get("route_edaedano").toString());
				String gougi_pattern_no   = n2b(map.get("gougi_pattern_no").toString());
				String gougi_edano        = n2b(map.get("gougi_edano").toString());
				String gougi_name         = n2b(map.get("gougi_name").toString());
				String shounin_shori_kengen_no = n2b(map.get("shounin_shori_kengen_no").toString());
				
				String user_id            = n2b(map.get("user_id").toString());
				String user_full_name     = n2b(map.get("user_full_name").toString());
				String bumon_cd           = n2b(map.get("bumon_cd").toString());
				String bumon_full_name    = n2b(map.get("bumon_full_name").toString());
				String bumon_role_id      = n2b(map.get("bumon_role_id").toString());
				String bumon_role_name    = n2b(map.get("bumon_role_name").toString());
				String gyoumu_role_id     = n2b(map.get("gyoumu_role_id").toString());
				String gyoumu_role_name   = n2b(map.get("gyoumu_role_name").toString());
				
				String shounin_ninzuu_cd      = n2b(map.get("shounin_ninzuu_cd").toString());
				String shounin_ninzuu_hiritsu = n2b(map.get("shounin_ninzuu_hiritsu").toString());
				
				String gougi_genzai_flg = "1".equals( n2b(map.get("isGenzaiShouninsha").toString()) )? "1": "0";
				String gougi_joukyou_edano =  ( map.get("gougiJoukyouEdano") == null || map.get("gougiJoukyouEdano").toString().isEmpty() ) ? "" : map.get("gougiJoukyouEdano").toString();
				
				int igedaBef = befGougiedano.isEmpty() ? -99 : Integer.parseInt(befGougiedano);
				int igeda =  gougi_edano.isEmpty() ? -1 : Integer.parseInt(gougi_edano);
				Integer gougiJoukyouEdaInteger = gougi_joukyou_edano.isEmpty() ? null : Integer.parseInt(gougi_joukyou_edano);
				
				
				if (isAddEnabled || !(gougi_pattern_no.isEmpty())) {
					if( !(gougi_pattern_no.isEmpty()) ){
						
						//承認済みの合議は登録しない
						if ( Integer.parseInt(route_edaedano) < (firstEdano - 1) )
						{
							continue;
						}
						
						boolean gougiFlg = false;
						//承認ルートテーブル登録(合議枝番が連番の時は同一合議内とみなしスキップ)
						if( !(befRouteEdaedano.equals(route_edaedano)) && (igeda != (igedaBef + 1)) ){
							edanoForGougi = edano;
							myLogic.insertShouninRoute(denpyouId, edano++, "", "", bumon_cd, bumon_full_name, "", "", "", "", genzai_flg, "0", Long.parseLong(shounin_shori_kengen_no), getUser().getTourokuOrKoushinUserId());
							gougiFlg = true;
						}
						
						//合議枝番が変わっていたら承認ルート合議親テーブル登録
						if( !(befGougiedano.equals(gougi_edano)) || gougiFlg ){
							myLogic.insertShouninRouteGougiOya(denpyouId, edanoForGougi, Integer.parseInt(gougi_edano), Long.parseLong(gougi_pattern_no), gougi_name,"0");
							befGougiedano = gougi_edano;
							gougiEdaedano = 1;
							befRouteEdaedano = route_edaedano;
						}
						
						//合議内データとみなして子登録
						myLogic.insertShouninRouteGougiKo(denpyouId, 
								 edanoForGougi,
								 Integer.parseInt(gougi_edano),
								 gougiEdaedano++,
								 user_id,
								 user_full_name,
								 bumon_cd,
								 bumon_full_name,
								 bumon_role_id,
								 bumon_role_name,
								 gougi_genzai_flg,
								 Long.parseLong(shounin_shori_kengen_no),
								 shounin_ninzuu_cd,
								 shounin_ninzuu_hiritsu.isEmpty() ? null : Integer.parseInt(shounin_ninzuu_hiritsu),
								 "0",
								 0,//gouginai_group
								 gougiJoukyouEdaInteger
								 );
						
						
						//現在承認者ユーザの承認権限が後伺いに変えられていたら状況を記録
						if(gougi_genzai_flg.equals("1")){
							long kengenNo = Long.parseLong(n2b(map.get("shounin_shori_kengen_no").toString()));
							GMap kengenMap =  workflowLogic.findShouninShoriKengen(kengenNo);
							if  (kengenMap.get("kihon_model_cd") != null && "3".equals(kengenMap.get("kihon_model_cd")) ){
								GMap addMap = new GMap();
								addMap.put("userId",n2b(map.get("user_id").toString()));
								addMap.put("gyoumuRoleId",n2b(map.get("gyoumu_role_id").toString()));
								addMap.put("edano", (Integer.parseInt(route_edaedano) + 1) );
								addMap.put("gougi_edano", Integer.parseInt(gougi_edano) );
								addMap.put("gougi_edaedano", (gougiEdaedano - 1) );
								userList.add(addMap);
							}
						}

						
						continue;
					}else{
						myLogic.insertShouninRoute(denpyouId, edano++, user_id, user_full_name, bumon_cd, bumon_full_name, bumon_role_id, bumon_role_name, gyoumu_role_id, gyoumu_role_name, genzai_flg, "0", Long.parseLong(shounin_shori_kengen_no), getUser().getTourokuOrKoushinUserId());
					}
				}
			}
			
			/* 最終承認者を承認ルートへ登録します。 */
			for(GMap map : saishuuShouninshaList) {
				String genzai_flg = "0";
				if (!isGenzaiSagyousha) {
					genzai_flg= "1";
					isGenzaiSagyousha = true;
				}
				String gyoumu_role_id     = n2b(map.get("gyoumu_role_id").toString());
				String gyoumu_role_name   = n2b(map.get("gyoumu_role_name").toString());
				myLogic.insertShouninRoute(denpyouId, edano, "", "", "", "", "", "", gyoumu_role_id, gyoumu_role_name, genzai_flg, "1", null, getUser().getTourokuOrKoushinUserId());
				String saishuuShouninShorikengen = map.get("saishuu_shounin_shori_kengen_name").toString();
				if(saishuuShouninShorikengen.isEmpty() == false){
					myLogic.updateShouninRouteShorikengenName(denpyouId, edano, saishuuShouninShorikengen);
				}
				edano++;
			}
			
			//デフォルト承認ルートと今登録した結果を比較して差異があれば承認ルート変更済フラグを立てる
			boolean shouninRouteIsDefault = workflowLogic.shouninRouteIsDefault(denpyouId, denpyouKbn, toDecimal(shinseiKingaku), toDate(kijunbi), getDaihyouTorihiki(connection));
			myLogic.updateShouninRouteHenkouFlg(denpyouId, ! shouninRouteIsDefault);

			//差分履歴
			List<GMap> afRouteList = workflowLogic.selectShouninRoute(denpyouId);
			String diff = workflowLogic.diffRouteList(bfRouteList, afRouteList);
			if(diff != null){
				WorkflowEventControlLogic wf = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
				GMap procRoute = workflowLogic.findProcRoute(bfRouteList, getUser());
				if(procRoute == null){
					procRoute = workflowLogic.findJouiRoute(bfRouteList, getUser());
				}
				wf.insertShouninJoukyou(denpyouId, getUser(), procRoute, "", "承認ルート変更", diff);
			}
			
			//後伺い分ユーザについて内部承認処理を行う
			//対象ユーザ分だけ繰り返す
			for(GMap atoukagaiUser : userList) {
				
				//処理前の承認ルート
				List<GMap> routeList = workflowLogic.selectShouninRoute(denpyouId);
				GMap procRoute = null;
				
				if( atoukagaiUser.get("gougi_edano") == null || atoukagaiUser.get("gougi_edano").toString().isEmpty() ){
					//非合議ユーザの現在ルート情報取得
					int tmpRouteEdano = (int)atoukagaiUser.get("edano");
					for(int i = 0; i < routeList.size(); i++){
						GMap route = routeList.get(i);
						if ( (int)route.get("edano") != tmpRouteEdano )
						{
							continue;
						}
						procRoute = route;
						break;
					}
					
				}else{
					//合議ユーザの現在ルート情報取得
					int tmpRouteEdano = (int)atoukagaiUser.get("edano");
					int tmpGougiEdano = (int)atoukagaiUser.get("gougi_edano");
					int tmpGougiEdaedano = (int)atoukagaiUser.get("gougi_edaedano");
					
					for(int i = 0; i < routeList.size(); i++){
						GMap route = routeList.get(i);
						if ( (int)route.get("edano") != tmpRouteEdano )
						{
							continue;
						}
						@SuppressWarnings("unchecked")
						List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya"); 
						for(int j = 0; j < gougiOyaList.size(); j++){
							GMap gougiOya = gougiOyaList.get(j);
							if ( (int)gougiOya.get("gougi_edano") != tmpGougiEdano )
							{
								continue;
							}
							@SuppressWarnings("unchecked")
							List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
							for(int z = 0; z < gougiKoList.size(); z++){
								GMap gougiKo = gougiKoList.get(z);
								if ( (int)gougiKo.get("gougi_edaedano") != tmpGougiEdaedano )
								{
									continue;
								}
								procRoute = gougiKo;
								break;
							}
							if (procRoute != null)
							{
								break;
							}
						}
						if (procRoute != null)
						{
							break;
						}
					}
				}
				
				//現在フラグを進める
				wfLogic.nextRoute(getUser(), denpyouId, routeList, procRoute);
				
			}
			
			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);
			
			connection.commit();

			//5.戻り値を返す
			if (Integer.parseInt(version) >= 1) {
				return "success_version";
			} else {
				return "success";
			}
			
		}
	}

	/**
	 * デフォルトに戻すイベント
	 * @return ResultName
	 */
	public String defaultBack() {
		//1.入力チェック
		formatCheck();
		hissuCheck(4);
		
		try(EteamConnection connection = EteamConnection.connect()){
			// ロジッククラスへコネクションを設定します。
			setConnection(connection);

			//2.データ存在チェック
			dataNotFoundCheck();
			//3.アクセス可能チェック
			accessDeniedCheck();
			
			//4.処理
			// 初期データの取得を行います。
			initDate(connection);
			
			// 承認ルートの取得
			shouninRouteList = workflowLogic.selectShouninRouteGougiList(denpyouId);
			// デフォルト承認ルートを作成します。
			shouninRouteTourokuList = createDefaultShouninRoute(connection);
			
			//画面表示・制御用にリストを作成
			createShouninRouteForGamen(shouninRouteTourokuList);
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 前回ルートに戻すイベント
	 * @return ResultName
	 */
	public String zenkaiRouteBack() {
		//1.入力チェック
		formatCheck();
		hissuCheck(5);
		
		try(EteamConnection connection = EteamConnection.connect()){
			// ロジッククラスへコネクションを設定します。
			setConnection(connection);

			//2.データ存在チェック
			dataNotFoundCheck();
			//3.アクセス可能チェック
			accessDeniedCheck();
			
			//4.処理
			// 初期データの取得を行います。
			initDate(connection);
			// 承認ルートの取得
			shouninRouteList = workflowLogic.selectShouninRouteGougiList(sanshouDenpyouId);
			
			// 前回ルートを作成します。
			shouninRouteTourokuList = new ArrayList<GMap>();
			for(GMap map : shouninRouteList) {
				
				// 部門フル名の再設定 現在日時点の名称データを取得(業務ロールの場合はそのまま)
				if( isEmpty((String)map.get("gyoumu_role_id")) ){
					String bFull = EteamCommon.connectBumonName(connection, (String)(map.get("bumon_cd")) );
					if(bFull.length() == 0) {bFull = "(削除)";}
					map.put("bumon_full_name", bFull );
				}
				
				//現在フラグを起票者に設定
				if("1".equals(map.get("edano").toString())) {
					map.put("genzai_flg", "1");
				}else{
					map.put("genzai_flg", "0");
				}
				
				//画面表示用の枝枝番号を設定
				map.put("route_edaedano",  Integer.toString(Integer.parseInt(map.get("edano").toString()) - 1) );
				//合議現在フラグ・状況枝番をリセット
				map.put("gougi_genzai_flg", "0");
				map.put("gougi_joukyou_edano", null);
				map.put("joukyou_edano", null);
				
				// 最終承認フラグが[1:最終承認者]の場合は追加しない。
				if("0".equals(map.get("saishu_shounin_flg").toString())) {
					map.put("addEnabled", "1".equals(map.get("edano").toString()) ? false : true);
					shouninRouteTourokuList.add(map);
				}
			}
			
			//画面表示・制御用にリストを作成
			createShouninRouteForGamen(shouninRouteTourokuList);
			//5.戻り値を返す
			return "success";
		}
	}
	
	
	/**
	 * 画面で指定された合議部署を承認ルートに追加する。
	 * @return ResultName
	 */
	public String addGougiBusho(){
		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		
		try(EteamConnection connection = EteamConnection.connect()){
			// ロジッククラスへコネクションを設定します。
			setConnection(connection);

			//2.データ存在チェック
			dataNotFoundCheck();
			//3.アクセス可能チェック
			accessDeniedCheck();
			
			//4.処理
			// 初期データの取得を行います。
			initDate(connection);
			// 承認ルート登録リストの作成（再表示のデータ保持）
			shouninRouteTourokuList = createShouninRouteTourokuList();
			
			// 指定された合議部署を追加
			addGougiToRoute(shouninRouteTourokuList, connection);
			
			//画面表示・制御用にリストを作成
			createShouninRouteForGamen(shouninRouteTourokuList);
			
			//5.戻り値を返す
			return "success";
		}
	}

//===以下内部メソッド===============================
	
	/**
	 * ロジッククラスへコネクションを設定します。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(ShouninRouteTourokuLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
	}

	/**
	 * データ存在チェックを行います。
	 * データが有る場合は必要なデータを取得します。
	 */
	protected void dataNotFoundCheck() {
		// 伝票の存在チェック、伝票IDをキーに伝票を検索します。
		GMap denpyouMap = workflowLogic.selectDenpyou(denpyouId);
		if(denpyouMap == null) {
			throw new EteamDataNotFoundException();
		} else {
			denpyouKbn       = denpyouMap.get("denpyou_kbn").toString();
			version          = denpyouMap.get("version").toString();
			sanshouDenpyouId = denpyouMap.get("sanshou_denpyou_id").toString();
			denpyouJoutai    = denpyouMap.get("denpyou_joutai").toString();
		}
	}

	/**
	 * アクセス可能チェックを行います。
	 * アクセス可能の場合はログインユーザー区分([1:起票者][2:承認者])を設定します。
	 */
	protected void accessDeniedCheck() {
		//更新ボタン押せる時だけ当画面利用可能
		WfSeigyo wfSeigyo = wfLogic.judgeWfSeigyo(getUser(), denpyouId, null);
		if (! wfSeigyo.shouninRouteTouroku) {
			throw new EteamAccessDeniedException();
		}
		
		//承認ルートの現在処理ユーザーレコード
		List<GMap> routeList = workflowLogic.selectShouninRoute(denpyouId);
		GMap procShouninRoute = workflowLogic.findProcRoute(routeList, getUser());
		
		// 伝票IDと現在フラグ「1:現在作業者」をキーに承認ルートからデータを取得します。
		if(procShouninRoute == null){
			loginUserKbu = SHOUNINSHA; //上位者と思われる
		}else{
			if((int)procShouninRoute.get("edano") == 1){
				loginUserKbu = KIHYOUSHA;
			}else{
				if(procShouninRoute.get("saishu_shounin_flg") != null && procShouninRoute.get("saishu_shounin_flg").equals("1")){
					loginUserKbu = SAISHU_SHOUNINSHA;
				}else{
					loginUserKbu = SHOUNINSHA;
				}
			}
		}
	}

	/**
	 * 初期データの取得を行います。
	 * @param connection DBコネクション
	 */
	protected void initDate(EteamConnection connection) {
		
		Date tmpDate = null;
		//基準日設定がなければシステム日付とする
		if(kijunbi != null && !(kijunbi.isEmpty())){
			tmpDate = toDate(kijunbi);
		}else{
			tmpDate = new Date(System.currentTimeMillis());
		}
		
		shoriKengenList = myLogic.loadShouninShoriKengen();
		shoriKengenGougiList = myLogic.loadShouninShoriKengenGougi();
		shoriKengenDomain = EteamCommon.mapList2Arr(shoriKengenList, "shounin_shori_kengen_no");
		shoriKengenGougiDomain = EteamCommon.mapList2Arr(shoriKengenGougiList, "shounin_shori_kengen_no");
		
		// 伝票種別一覧からリダイレクト用のURLの取得
		urlPath = workflowLogic.selectDenpyouShubetu(denpyouKbn).get("denpyou_shubetsu_url").toString();

		// 基準日時点の所属部門リストを取得し編集します。
		bumonList = EteamCommon.bumonListHenshuu(connection, bumonUserLogic.selectBumonTreeWithKijunbi("", tmpDate));
		
		// 承認者選択リスト作成
		if(isNotEmpty(choiceBumonCd)) {
			if("gyoumuRole".equals(choiceBumonCd)) {
				// 業務ロール機能制御ID：WF（ワークフロー）且つ、業務ロール機能制御区分：1（有効）の業務ロールを取得します。
				shouninshaList = bumonUserLogic.selectValidGyoumuRoleKinou("WF");
			}else if("gougiBusho".equals(choiceBumonCd)) {
				// 合議部署名リストを取得します。
				shouninshaList = bumonUserLogic.selectGougiBusho();
			} else {
				// 選択された部門コード・基準日をキーに指定基準日時点のユーザー・役職を取得します。
				shouninshaList = bumonUserLogic.userSerach("", "", "", choiceBumonCd, "", "", tmpDate, false, false, false, "");
				for(GMap map : shouninshaList) {
					map.put("user_full_name", map.get("user_sei").toString() + "　" + map.get("user_mei").toString());
					map.put("bumon_full_name", EteamCommon.connectBumonName(connection, choiceBumonCd));
				}
			}
		}
		
		// 注記文言・最終承認ルートの取得
		saishuuShouninshaList = new ArrayList<GMap>();
		GMap chuukiMongonMap = workflowLogic.selectChuukiMongon(denpyouKbn);
		if(chuukiMongonMap != null) {
			chuukiMongon = chuukiMongonMap.get("chuuki_mongon").toString();
			
			// 最終承認者リストを取得します。
			for(GMap map : workflowLogic.selectSaishuuSyouninRoute(denpyouKbn, (int) chuukiMongonMap.get("edano"))){
				GMap tempMap = new GMap();
				tempMap.put("gyoumu_role_id",   map.get("gyoumu_role_id"));
				tempMap.put("gyoumu_role_name", map.get("gyoumu_role_name"));
				tempMap.put("saishuu_shounin_shori_kengen_name", map.get("saishuu_shounin_shori_kengen_name"));
				saishuuShouninshaList.add(tempMap);
			}
		}
	}

	/**
	 * 承認ルート登録リスト作成
	 * 画面から受け取ったデータをListに格納し返却します。
	 * @return 承認ルート登録リスト
	 */
	protected ArrayList<GMap> createShouninRouteTourokuList() {
		ArrayList<GMap> returnList = new ArrayList<GMap>();
		
		int curEdaedaNo = -1;
		String edaTmp = "";
		
		if (addEnabled != null) {
			for(int i = 0; i < addEnabled.length; i++) {
				GMap map = new GMap();
				map.put("addEnabled",           addEnabled[i]);
				if (userId != null) {
					map.put("user_id",          userId[i]);
					map.put("user_full_name",   userFullName[i]);
					map.put("bumon_cd",         bumonCode[i]);
					map.put("bumon_full_name",  bumonFullName[i]);
					map.put("bumon_role_id",    bumonRoleId[i]);
					map.put("bumon_role_name",  bumonRoleName[i]);
				}
				if (gyoumuRoleId != null) {
					map.put("gyoumu_role_id",   gyoumuRoleId[i]);
					map.put("gyoumu_role_name", gyoumuRoleName[i]);
				}
				
				//ルート枝々番号の振り直し
				if( !(edaTmp.equals(routeEdaedano[i])) || routeEdaedano[i].isEmpty()){
					curEdaedaNo++;
				}
				edaTmp = routeEdaedano[i];
				
				map.put("route_edaedano",curEdaedaNo);
				map.put("gougi_pattern_no",gougiPatternNo[i]);
				map.put("gougi_edano",gougiEdano[i]);
				map.put("gougi_name",gougiName[i]);
				map.put("shounin_shori_kengen_no",shoriKengenNo[i]);
				map.put("shounin_shori_kengen_name",shoriKengenName[i]);
				
				map.put("shounin_ninzuu_cd",shouninNinzuuCd[i]);
				map.put("shounin_ninzuu_hiritsu",shouninNinzuuHiritsu[i]);
				map.put("gouginai_group",gouginaiGroup[i]);
				
				map.put("genzaiUsrShouninKengen", genzaiUsrShouninKengen[i]);
				
				map.put("addedGougi", addedGougi[i]);
				map.put("addedUser", addedUser[i]);
				map.put("isGenzaiShouninsha", isGenzaiShouninsha[i]);
				map.put("gougiJoukyouEdano", gougiJoukyouEdano[i]);
				map.put("kengenDisableFlg", kengenDisableFlg[i]);
				
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	/**
	 * デフォルト承認ルート作成
	 * デフォルトの承認ルートをListに格納し返却します。
	 * @param connection DBコネクション
	 * @return デフォルト承認ルートリスト
	 */
	protected List<GMap> createDefaultShouninRoute(EteamConnection connection) {
		ArrayList<GMap> returnList = new ArrayList<GMap>();
		// 承認ルートから起票者の情報を取得します。
		GMap kihyoushaMap = shouninRouteList.get(0);
		kihyoushaMap.put("addEnabled", false);
		kihyoushaMap.put("route_edaedano", 0); //起票者は枝枝番号0としておく
		returnList.add(kihyoushaMap);
		
		// 部門推奨ルートを取得します。
		String kihyouBumonCd = kihyoushaMap.get("bumon_cd").toString();
		List<GMap> bumonSuishouRouteList = workflowLogic.loadBumonSuishouRouteUser(denpyouKbn, kihyouBumonCd, toDecimal(shinseiKingaku), (String)kihyoushaMap.get("user_id"), toDate(kijunbi), getDaihyouTorihiki(connection));

		//この画面用の必要情報を付加
		for(GMap map : bumonSuishouRouteList) {
			if(map.get("user_id") != null) {
				map.put("user_full_name",  map.get("user_sei").toString() + "　" + map.get("user_mei").toString());
				map.put("bumon_cd",        map.get("bumon_cd"));
				map.put("bumon_full_name", EteamCommon.connectBumonName(connection, map.get("bumon_cd").toString()));
				//合議部署内のユーザは変更不可とする
				if(map.get("gougi_pattern_no") == null){
					map.put("addEnabled", true);
				}else{
					map.put("addEnabled", false);
				}
				
				if( map.get("addedGougi") == null || !("1".equals(map.get("addedGougi"))) ){
					map.put("addedGougi", "0");
				}
				if( map.get("addedUser") == null || !("1".equals(map.get("addedUser"))) ){
					map.put("addedUser", "0");
				}
				if( map.get("gougiJoukyouEdano") == null || ((String)map.get("gougiJoukyouEdano")).isEmpty() ){
					map.put("gougiJoukyouEdano", null);
				}
				
				if( map.get("kengenDisableFlg") == null || ((String)map.get("kengenDisableFlg")).isEmpty() ){
					map.put("kengenDisableFlg", "0");
				}
				
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	/**
	 * 承認ルートのレコード情報を画面項目に移す
	 * @param orgList 承認ルートリスト
	 */
	protected void createShouninRouteForGamen(List<GMap> orgList){
		
		//ログイン中ユーザーの承認ルート変更権限レベルの取得
		shouninRouteHenkouLevel = getUser().getShouninRouteHenkouLevel();
		
		Map<Integer,String> gougiLineMap = new HashMap<Integer,String>(); //合議内テーブル行数保持用
		Map<Integer,String> linePropMap = new HashMap<Integer,String>(); //テーブル行内制御情報保持用
		
		int index = -1;
		int orgListMax = orgList.size();
		int lastIndex = -1; //ルート内最終承認者判別用
		
		String gedaBef = "";
		String reenBef = "";
		int chkIndex = -1;
		//合議ライン数算出 //ライン開始・部門変更・終了フラグ設定
		for(GMap orgMap : orgList){
			index++;
			//起票者該当行・合議内チェック済み行はスキップ
			if (index == 0 || index < chkIndex)
			{
				continue;
			}
			if(orgMap.get("gougi_pattern_no") == null){
				linePropMap.put(index, "fe");
				lastIndex = index;
				continue;
			}
			
			String gptn = orgMap.get("gougi_pattern_no").toString();
			if( !gptn.isEmpty() ){
				//合議内データ
				gedaBef = orgMap.get("gougi_edano").toString();
				reenBef = orgMap.get("route_edaedano").toString();
				linePropMap.put(index , "fci");
				lastIndex = index;
				int listCnt = 2;
				
				//合議が承認ルート上の最終承認者かつ、合議内ユーザーが1人しかいないパターン
				if(index + 1 == orgListMax){
					String tmpStr = linePropMap.containsKey(index) ? linePropMap.get(index) : "";
					linePropMap.put(index , tmpStr + "ei");
				}
				
				//合議内テーブル使用行数カウント(人数+部署数) 同一合議パターン・同一ルート枝枝番内の情報を参照
				for( chkIndex = index + 1; chkIndex < orgListMax ; chkIndex++ ){
					GMap tmpMap = orgList.get(chkIndex);
					String geda = tmpMap.get("gougi_edano") == null ? "" : tmpMap.get("gougi_edano").toString();
					String reen = tmpMap.get("route_edaedano") == null ? "" : tmpMap.get("route_edaedano").toString();
					boolean isSameReen = reenBef.equals(reen);
					reenBef = reen;
					
					int igedaBef = Integer.parseInt(gedaBef);
					int igeda =  geda.isEmpty() ? -1 : Integer.parseInt(geda);
					
					if(tmpMap.get("gougi_pattern_no") != null
					   && (isSameReen || (!isSameReen && igeda == (igedaBef + 1) ) ) ){
						//テーブル使用行数追加
						linePropMap.put(chkIndex , "i");
						listCnt++;
						//同一合議内で部署が変わっていれば部署表示行追加
						if(!gedaBef.equals(tmpMap.get("gougi_edano").toString())){
							linePropMap.put(chkIndex , "ci");
							listCnt++;
							gedaBef = tmpMap.get("gougi_edano").toString();
						}
					}else{
						//1つ前のデータに終了フラグ立てる
						String tmpStr = linePropMap.containsKey(chkIndex - 1) ? linePropMap.get(chkIndex - 1) : "";
						linePropMap.put(chkIndex - 1 , tmpStr + "ei");
						break;
					}
					if(chkIndex + 1 == orgListMax){
						//最終行なら終了フラグ立てる
						String tmpStr = linePropMap.containsKey(chkIndex) ? linePropMap.get(chkIndex) : "";
						linePropMap.put(chkIndex , tmpStr + "ei");
					}
					
				}
				
				gougiLineMap.put(index, Integer.toString(listCnt));
			}else{
				//非合議データ
				linePropMap.put(index, "fe");
				lastIndex = index;
			}
			gedaBef = "";
		}

		//算出結果をもとに画面表示用フラグ設定
		index = -1;
		
		
		for(GMap orgMap : orgList){
			
			index++;
			orgMap.put("gougiLine", "0"); //合議内テーブル行数
			orgMap.put("gougiStartFlg", ""); //合議データ開始行フラグ(非合議データは常に1)
			orgMap.put("gougiChangeFlg", ""); //合議内データ部署変更フラグ
			orgMap.put("gougiEndFlg", ""); //合議データ終了行フラグ(非合議データは常に1)
			orgMap.put("gougiInnerFlg", ""); //合議内データフラグ
			orgMap.put("saishuuShouninFlg", ""); //ルート内最終承認者フラグ
			
			if(gougiLineMap.containsKey(index)){orgMap.put("gougiLine", gougiLineMap.get(index));}
			if(linePropMap.containsKey(index)){
				String tmpStr = linePropMap.get(index);
				if(tmpStr.contains("f")){orgMap.put("gougiStartFlg", "1");}
				if(tmpStr.contains("c")){orgMap.put("gougiChangeFlg", "1");}
				if(tmpStr.contains("e")){orgMap.put("gougiEndFlg", "1");}
				if(tmpStr.contains("i")){
					orgMap.put("gougiInnerFlg", "1");
					orgMap.put("addEnabled", false);
					}
			}
			if (index == lastIndex){
				if( orgMap.get("addedUser") == null || !("1".equals(orgMap.get("addedUser"))) ){
					orgMap.put("saishuuShouninFlg", "1");
				}
			}
			
			if( (orgMap.get("genzaiUsrShouninKengen") != null && ( !(orgMap.get("genzaiUsrShouninKengen").toString().isEmpty()) ) ) ) {
				//genzaiUsrShouninKengen設定済みなので何もしない
			}else if (   ( (orgMap.get("gougi_pattern_no") == null || orgMap.get("gougi_pattern_no").toString().isEmpty()) 
					     && orgMap.get("genzai_flg") != null && orgMap.get("genzai_flg").toString().equals("1") ) 
					|| (orgMap.get("gougi_genzai_flg") != null && orgMap.get("gougi_genzai_flg").toString().equals("1")) ){
				//現在の承認権限Noを取得
				orgMap.put("genzaiUsrShouninKengen", orgMap.get("shounin_shori_kengen_no"));
			}else{
				orgMap.put("genzaiUsrShouninKengen", "");
			}
			
			//既にgougiJoukyouEdano設定されているならその値を使用
			if( (orgMap.get("gougiJoukyouEdano") == null || (orgMap.get("gougiJoukyouEdano").toString().isEmpty()) ) )  {
				orgMap.put("gougiJoukyouEdano", orgMap.get("gougi_joukyou_edano"));
			}
			
		}
		
		//「現在フラグが0」 「該当部門の合議現在フラグがそれまでに出ていない」なら該当承認権限は変更させない
		Map<String,String> genzaiAppearBumonCd = new HashMap<String,String>();
		Map<String,String> genzaiAppearRole = new HashMap<String,String>();
		String curReen = "";
		for(GMap orgMap : orgList){
			
			String mapRouteEdaedaNo = orgMap.get("route_edaedano") == null ? "" : orgMap.get("route_edaedano").toString();
			String mapGougiPatternNo = orgMap.get("gougi_pattern_no") == null ? "" : orgMap.get("gougi_pattern_no").toString();
			String mapGougiEdaNo = orgMap.get("gougi_edano") == null ? "" : orgMap.get("gougi_edano").toString();
			String mapGenzaiflg = orgMap.get("genzai_flg") == null ? "" : orgMap.get("genzai_flg").toString();
			String mapGougiGenzaiflg = orgMap.get("gougi_genzai_flg") == null ? "" : orgMap.get("gougi_genzai_flg").toString();
			String mapBumonCd = orgMap.get("bumon_cd") == null ? "" : orgMap.get("bumon_cd").toString();
			String mapBumonRoleId = orgMap.get("bumon_role_id") == null ? "" : orgMap.get("bumon_role_id").toString();
			String mapGougiJoukyouEdano = orgMap.get("gougiJoukyouEdano") == null ? "" : orgMap.get("gougiJoukyouEdano").toString();
			
			
			if(mapGenzaiflg.equals("1")){
				if ( mapGougiPatternNo.isEmpty() ){
					//単一ユーザが現在承認者
					orgMap.put("isGenzaiShouninsha", "1");
					genzaiAppearRole.put("notGougi","dmy");
				}else if( mapGougiGenzaiflg.equals("1") ){
					//合議内に現在承認者
					orgMap.put("isGenzaiShouninsha", "1");
					curReen = mapRouteEdaedaNo;
					genzaiAppearBumonCd.put(mapGougiEdaNo, mapBumonCd);
					genzaiAppearRole.put(mapGougiEdaNo,mapBumonRoleId);
				}
			}
			
			//状況枝番が入っていれば承認済みとみなす
			if( !(mapGougiJoukyouEdano.isEmpty()) )  {
				orgMap.put("kengenDisableFlg", "1" );
			}
			
			//状況枝番がなくても合議内で現在フラグ出現前のデータは承認済とみなし、承認権限変更させない
			if( (orgMap.get("kengenDisableFlg") == null || (orgMap.get("kengenDisableFlg").toString().isEmpty()) ) )  {
				orgMap.put("kengenDisableFlg", "0");
				if ( !(mapGougiPatternNo.isEmpty()) ){
					if( !(mapGougiGenzaiflg.equals("1")) ){
						if( !(genzaiAppearRole.containsKey(mapGougiEdaNo) || genzaiAppearRole.containsKey("notGougi")) ){
							orgMap.put("kengenDisableFlg", "1" );
						}
					}
				}
			}
			
			//現在承認者と同一の合議・部門・役割内で現在フラグ0であるなら承認権限編集不可とする
			if(curReen.equals(mapRouteEdaedaNo)){
				if ( !(mapGougiPatternNo.isEmpty()) ){
					if( genzaiAppearBumonCd.containsKey(mapGougiEdaNo) && genzaiAppearRole.containsKey(mapGougiEdaNo) ){
						if(genzaiAppearBumonCd.get(mapGougiEdaNo).toString().equals(mapBumonCd) && genzaiAppearRole.get(mapGougiEdaNo).toString().equals(mapBumonRoleId)){
							if( !(mapGougiGenzaiflg.equals("1")) ){
								orgMap.put("kengenDisableFlg", "1" );
							}
						}
					}
				}
			}
			// 現在承認者以降の合議なら全て承認権限編集可
			if(!curReen.isEmpty()){
				int curReenInt = Integer.parseInt(curReen);
				String reenMapStr = orgMap.get("route_edaedano") == null ? "-1" : orgMap.get("route_edaedano").toString();
				int reenMapInt = Integer.parseInt(reenMapStr);
				if(curReenInt < reenMapInt){
					orgMap.put("kengenDisableFlg", "0" );
				}
				
			}
		}
		
	}
	
	/**
	 * 合議部署のデータを取得し、最終承認者手前に追加
	 * ただし承認ルート最終承認者(合議含む)に現在フラグがある場合のみ最終承認者の後に追加
	 * @param orgList 元リスト
	 * @param connection DBコネクション
	 */
	protected void addGougiToRoute(List<GMap> orgList, EteamConnection connection){
		
		int edaedano = 1;
		String kihyouUserId = "";
		
		Date tmpDate = null;
		//基準日設定がなければシステム日付とする
		if(kijunbi != null && !(kijunbi.isEmpty())){
			tmpDate = toDate(kijunbi);
		}else{
			tmpDate = new Date(System.currentTimeMillis());
		}
		
		//合議挿入先インデックスを取得
		int sounyuIndex = -1;
		if(orgList.size() > 1){
			GMap tmpMap = orgList.get(orgList.size() - 1);
			edaedano = (int)tmpMap.get("route_edaedano");
			int index = 0;
			
			//承認ルート最終承認者に現在フラグがあるかチェック
			boolean genzaiFlg = false;
			for(GMap map : orgList){
				if((int)map.get("route_edaedano") == edaedano){
					if( map.get("isGenzaiShouninsha") != null && map.get("isGenzaiShouninsha").toString().equals("1") ){
						genzaiFlg = true;
						break;
					}
				}
			}
			
			if(genzaiFlg == false){
				//リスト内最終承認者(合議含む)のroute_edaedanoを取得し、該当のroute_edaedano を + 1
				for(GMap map : orgList){
					if((int)map.get("route_edaedano") == edaedano){
						map.put("route_edaedano", edaedano + 1);
						if (sounyuIndex == -1)
						{
							sounyuIndex = index;
						}
					}
					index++;
				}
			}else{
				//承認ルート最終承認者に現在フラグがある場合のみ最終承認者の後に追加
				sounyuIndex = orgList.size();
				edaedano = edaedano + 1;
			}
			
			tmpMap = orgList.get(0);
			kihyouUserId = (String)tmpMap.get("user_id"); 
		}else{
			//起票者しかルートに無い場合は特に操作しない
			sounyuIndex = 1;
		}
		
		
		int addGougiEdano = 1;
		String[] gougiPtnNo = addGougiBushoList.split(",", 0);

		//指定された合議部署のデータを取得
		for(int i = 0; i < gougiPtnNo.length; i++){
			
			
			if (gougiPtnNo[i].isEmpty())
			{
				continue;
			}
			
			GMap setMap = new GMap();
			
			setMap.put("bumon_cd", "");
			setMap.put("route_edaedano", edaedano);
			setMap.put("shounin_shori_kengen_no", "0");
			setMap.put("gougi_pattern_no", gougiPtnNo[i]);
			setMap.put("gougi_edano", Integer.toString(addGougiEdano));
			setMap.put("kengenDisableFlg", "0" );

			
			//部門推奨ルート子(合議)に紐付くユーザー全員
			List<GMap> gougiPtnList = workflowLogic.selectGougiPattern(Long.parseLong(gougiPtnNo[i]));
			for(GMap gMap : gougiPtnList) {
				List<GMap> suishouRouteUserGougiList = bumonUserLogic.shouninUserSerach(gMap.get("bumon_cd").toString(), gMap.get("bumon_role_id").toString(), kihyouUserId, tmpDate);
				for(GMap listMap : suishouRouteUserGougiList) {
					GMap routeUser = new GMap();
					listMap.put("user_full_name", listMap.get("user_sei").toString() + "　" + listMap.get("user_mei").toString());
					listMap.put("bumon_full_name", EteamCommon.connectBumonName(connection, listMap.get("bumon_cd").toString()));
					listMap.put("addedGougi", "1");
					listMap.put("addedUser", "1");
					listMap.put("gougiJoukyouEdano", null);
					routeUser.putAll(setMap);
					routeUser.putAll(gMap);
					routeUser.putAll(listMap);
					
					//承認ルートリストの指定位置にユーザを挿入
					orgList.add( sounyuIndex , routeUser );
					sounyuIndex++;
				}
			}
			addGougiEdano++;
		}
	}

	/**
	 * 各伝票テーブルから仕訳枝番号を取得
	 * @param connection DBコネクション
	 * @return 仕訳枝番号
	 */
	protected String getDaihyouTorihiki(EteamConnection connection){
		
		KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		String ret = null;
		
		if(navi.findDenpyouKanri(denpyouKbn).get("route_torihiki_flg").toString().equals(EteamConst.routeTorihiki.OK)){
			switch(denpyouKbn){
			case(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN):
				ret = myLogic.getMeisaiShiwakeEdaNo("keihiseisan_meisai", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.SEIKYUUSHO_BARAI):
				ret = myLogic.getMeisaiShiwakeEdaNo("seikyuushobarai_meisai", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU):
				ret = myLogic.getMeisaiShiwakeEdaNo("jidouhikiotoshi_meisai", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.KARIBARAI_SHINSEI):
				ret = myLogic.getShiwakeEdaNo("karibarai", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.RYOHI_SEISAN):
				ret = myLogic.getShiwakeEdaNo("ryohiseisan", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI):
				ret = myLogic.getShiwakeEdaNo("ryohi_karibarai", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.KOUTSUUHI_SEISAN):
				ret = myLogic.getShiwakeEdaNo("koutsuuhiseisan", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI):
				ret = myLogic.getShiwakeEdaNo("tsuukinteiki", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN):
				ret = myLogic.getShiwakeEdaNo("kaigai_ryohiseisan", "kaigai_shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI):
				ret = myLogic.getShiwakeEdaNo("kaigai_ryohi_karibarai", "shiwake_edano", denpyouId);
				break;
			case(DENPYOU_KBN.SIHARAIIRAI):
				ret = myLogic.getMeisaiShiwakeEdaNo("shiharai_irai_meisai", "shiwake_edano", denpyouId);
				break;
				
			default:
			}
		}
		
		return ret;
	}
}
