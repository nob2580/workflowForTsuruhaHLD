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
 * 定期情報追加画面Action
 */
@Getter @Setter @ToString
public class TeikiJouhouTsuikaAction extends EteamEkispertCommon {
	
	//＜定数＞

	//＜HTTP REQUEST＞
	/** 社員番号 */
	String shainNo;
	/** ユーザー名 */
	String userName;
	/** ユーザーID */
	String userId;
	/** 使用期間区分 */
	String shiyouKikanKbn;
	/** 使用開始日 */
	String shiyouKaishiBi;
	/** 使用終了日 */
	String shiyouShuuryouBi;
	/** 乗車区間 */
	String jyoushaKukan;
	/** 定期シリアライズデータ */
	String teikiSerializeData;
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
				{shainNo, "社員番号", "0"},
				{userId, "ユーザー", "1"},
				{preEventUrl, "イベントURL(遷移元画面)", "2"},
				{shiyouKikanKbn, "使用期間区分", "1"},
				{shiyouKaishiBi, "使用開始日", "1"},
				{shiyouShuuryouBi, "使用終了日", "1"},
				{jyoushaKukan, "乗車区間", "1"}
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
			//リクエストで渡されたパラメータをリセット
			userId = "";
			shiyouKaishiBi = "";
			shiyouShuuryouBi = "";
			
		}
		
		//5.戻り値を返す
		return "success";
	}
	

	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){
		
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
			GMap userJouhouMap = bumonUserLogic.selectUserInfo(userId);
			if(userJouhouMap == null){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック（権限チェック）
			//4.処理
			// 有効期限の共通チェックを行います。
			for(Map<String, String> errMap: EteamCommon.kigenCheck(shiyouKaishiBi, shiyouShuuryouBi)) {
				// 開始日と終了日の整合性チェックのみエラーとする。
				if ("2".equals(errMap.get("errorCode"))) {
					errorList.add(errMap.get("errorMassage"));
				}
			}
			//該当ユーザー分の既存定期情報データを参照し、重複があるならエラー
			if(teikiLogic.countTeikiJouhouDuplicate(userId, toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi)) >= 1){
				errorList.add("登録対象ユーザーの、使用期間が重複する定期情報が存在します。");
			}
			
			if(!errorList.isEmpty()){
				return "error";
			}
			
			//登録処理
			ekiLogic.updateTeikiJouhou(jyoushaKukan,teikiSerializeData,toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi), userId, getUser());
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
