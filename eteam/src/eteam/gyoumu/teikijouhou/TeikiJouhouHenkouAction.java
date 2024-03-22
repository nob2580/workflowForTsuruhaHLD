package eteam.gyoumu.teikijouhou;

import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamEkispertCommon;
import eteam.common.EteamEkispertCommonLogic;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_KIKAN_KBN;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定期情報変更画面Action
 */
@Getter @Setter @ToString
public class TeikiJouhouHenkouAction extends EteamEkispertCommon {
	
	//＜定数＞

	//＜HTTP REQUEST＞
	/** 社員番号 */
	String shainNo;
	/** ユーザー名 */
	String userName;
	/** ユーザーID(表示キー) */
	String userId;
	/** 使用期間区分 */
	String shiyouKikanKbn;
	/** 使用開始日(表示キー) */
	String shiyouKaishiBi;
	/** 使用終了日(表示キー) */
	String shiyouShuuryouBi;
	/** 乗車区間 */
	String jyoushaKukan;
	/** 定期シリアライズデータ */
	String teikiSerializeData;
	/** 編集前ユーザーID */
	String orgUserId;
	/** 編集前使用開始日 */
	String orgKaishiBi;
	/** 編集前使用終了日 */
	String orgShuuryouBi;
	/** イベントURL(遷移元画面) */
	String preEventUrl;
	
	//＜画面入力以外＞	
	/** 使用期間区分List */
	List<GMap> shiyouKikanList;
	/** 使用期間チェック用 */
	String[] shiyouKikanKbnDomain;
	/** マスターデータ一覧 */
	List<GMap> teikiJouhouList;
	/** 駅すぱあと連携可否 */
	boolean ekispertEnable = !"9".equals(setting.intraFlg());
	
	//＜部品＞
	/** システム管理SELECT */
	SystemKanriCategoryLogic systemKanriLogic;
	/** 部門ユーザ管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** 駅すぱあとロジック */
	EteamEkispertCommonLogic ekiLogic;
	/** 定期情報管理機能ロジック */
	TeikiJouhouLogic teikiLogic;
		
	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(userId, 1, 30, "ユーザーID", false);
		checkNumber(shiyouKikanKbn, 2, 2, "使用期間区分", false);
		checkDate(shiyouKaishiBi, "使用開始日", false);
		checkDate(shiyouShuuryouBi, "使用終了日", false);
	}
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目				項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shainNo, "社員番号", "0","0","0"},
				{userId, "ユーザーID", "2","1","0"},
				{preEventUrl, "イベントURL(遷移元画面)", "2","2","2"},
				{shiyouKikanKbn, "使用期間区分", "0","1","0"},
				{shiyouKaishiBi, "使用開始日", "2","1","0"},
				{shiyouShuuryouBi, "使用終了日", "2","1","0"},
				{jyoushaKukan, "乗車区間", "0","1","0"}
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
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			
			//該当定期券情報のデータを取得して画面に表示
			GMap map = teikiLogic.findTeikiJouhou(userId, toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi));
			if(map == null) throw new EteamDataNotFoundException();
			
			//画面呼出時点でのユーザーID・使用開始終了日を保持(更新・削除処理のため)
			orgUserId   = userId;
			orgKaishiBi   = shiyouKaishiBi;
			orgShuuryouBi = shiyouShuuryouBi;
			
			//イントラ版かWeb版かで分岐
			if("1".equals(setting.intraFlg())){
				jyoushaKukan = (String)map.get("intra_teiki_kukan");
				teikiSerializeData = (String)map.get("intra_restoreroute");
			}else {
				jyoushaKukan = (String)map.get("web_teiki_kukan");
				teikiSerializeData = (String)map.get("web_teiki_serialize_data");
			}
			
			//ユーザーIDから社員番号・氏名取得
			GMap userInfo = bumonUserLogic.selectUserInfo(userId);
			if(userInfo != null){
				shainNo  = (String)userInfo.get("shain_no");
				userName = (String)userInfo.get("user_sei") + "　" + (String)userInfo.get("user_mei");
			}else{
				shainNo  = "";
				userName = "※該当ユーザーは削除されています";
			}
			
			
		}
		
		//5.戻り値を返す
		return "success";
	}
	

	/**
	 * 変更イベント
	 * @return ResultName
	 */
	public String henkou(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			displaySeigyo(connection);
			
			//1.入力チェック
			hissuCheck(2);
			formatCheck();
			if(!errorList.isEmpty()){
				return "error";
			}
			
			//2.データ存在チェック
			//3.アクセス可能チェック（権限チェック）
			//4.処理
			
			//指定ユーザーが存在するかチェック
			GMap userInfo = bumonUserLogic.selectUserInfo(userId);
			if(userInfo == null){
				errorList.add("指定されたユーザーは存在しません。");
			}
			
			// 有効期限の共通チェックを行います。
			for(Map<String, String> errMap: EteamCommon.kigenCheck(shiyouKaishiBi, shiyouShuuryouBi)) {
				// 開始日と終了日の整合性チェックのみエラーとする。
				if ("2".equals(errMap.get("errorCode"))) {
					errorList.add(errMap.get("errorMassage"));
				}
			}
			
			//該当ユーザー分の既存定期情報データを参照し、重複があるならエラー
			//まだDBからデータ削除をしていないため編集中のデータが重複データとして取得される
			long dupCnt = teikiLogic.countTeikiJouhouDuplicate(userId, toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi));
			if(dupCnt >= 2){
				errorList.add("登録対象ユーザーの、使用期間が重複する定期情報が存在します。");
			}else if(dupCnt == 1){
				//重複データが編集元のデータかチェック
				GMap tmpMap = teikiLogic.findTeikiJouhouDuplicate(userId, toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi));
				if(!( orgUserId.equals(tmpMap.get("user_id")) && orgKaishiBi.equals(formatDate(tmpMap.get("shiyou_kaishibi"))) && orgShuuryouBi.equals(formatDate(tmpMap.get("shiyou_shuuryoubi"))) ) ) {
					errorList.add("登録対象ユーザーの、使用期間が重複する定期情報が存在します。");
				}
			}
			
			if(!errorList.isEmpty()){
				return "error";
			}
			
			//更新処理
			//元データは先に削除しておく
			teikiLogic.deleteTeikiJouhou(orgUserId, toDate(orgKaishiBi), toDate(orgShuuryouBi));
			
			ekiLogic.updateTeikiJouhou(jyoushaKukan,teikiSerializeData,toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi), userId, getUser());
			connection.commit();
			
			//5.戻り値を返す
			return "success";
			
		}
	}

	/**
	 * 削除イベント
	 * @return ResultName
	 */
	public String sakujo(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			displaySeigyo(connection);
			
			//1.入力チェック
			hissuCheck(3);
			formatCheck();
			if(!errorList.isEmpty()){
				return "error";
			}
			
			//2.データ存在チェック
			//3.アクセス可能チェック（権限チェック）
			//4.処理
			
			//削除処理
			teikiLogic.deleteTeikiJouhou(orgUserId, toDate(orgKaishiBi), toDate(orgShuuryouBi));
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
		systemKanriLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		ekiLogic = EteamContainer.getComponent(EteamEkispertCommonLogic.class, connection);
		teikiLogic = EteamContainer.getComponent(TeikiJouhouLogic.class, connection);
	}
	
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		shiyouKikanList = systemKanriLogic.loadNaibuCdSetting("shiyou_kikan_kbn");
		shiyouKikanKbn = SHIYOU_KIKAN_KBN.KIKAN1;
		
		//「無期限」用仮設定追加(定期金額は1ヶ月として出力)
		GMap map = new GMap();
		map.put("naibu_cd", SHIYOU_KIKAN_KBN.KIKAN1);
		map.put("name", "無期限");
		map.put("option1", "");
		map.put("option2", "");
		map.put("option3", "");
		shiyouKikanList.add(0,map);
		
		shiyouKikanKbnDomain = EteamCommon.mapList2Arr(shiyouKikanList, "naibu_cd");
		// 駅すぱあと連携可否判定
		ekispertEnable = !"9".equals(setting.intraFlg());
		if(ekispertEnable) {
			// 駅すぱあと呼出 start
			super.ekispertInit(connection, SEARCH_MODE_TEIKI);
		}else{
			throw new EteamAccessDeniedException(); // アクセスエラー
		}
	}
}
