package eteam.gyoumu.teikijouhou;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定期情報一覧画面Action
 */
@Getter @Setter @ToString
public class TeikiJouhouIchiranAction extends EteamAbstractAction {
	
	//＜定数＞
	/** 無期限延長時の終了日 */
	final String MUKIGEN_DATE = "9999/12/31";

	//＜画面入力＞
	/** ユーザーID */
	String userId;
	/** ユーザー名 */
	String userName;
	/** 社員番号 */
	String shainNo;
	/** (検索部)使用開始日 */
	String shiyouKaishiBi;
	/** (検索部)使用終了日 */
	String shiyouShuuryouBi;
	
	/** 選択チェックボックス */
	String[] sentaku;

	//＜画面入力以外＞
	/** (無期限延長対象取得用)ユーザーID */
	String[] rowUserId;
	/** (リスト保持用)ユーザーフル名 */
	String[] rowUserFullName;
	/** (無期限延長対象取得用)使用開始日 */
	String[] rowKaishiBi;
	/** (無期限延長対象取得用)使用終了日 */
	String[] rowShuuryouBi;
	/** (リスト保持用)定期区間 */
	String[] rowKukan;
	/** 定期情報リスト */
	List<GMap> teikiJouhouList;
	/** チェックボックス選択フラグ */
	String[] sentakuFlg;
	/** イベントURL(遷移元画面) */
	String preEventUrl;
	
	/** (検索部保持用)ユーザーID */
	String kensakuUserId;
	/** (検索部保持用)使用開始日 */
	String kensakuKaishiBi;
	/** (検索部保持用)使用終了日 */
	String kensakuShuuryouBi;

	//＜入力チェック＞
	/** 検索条件の形式チェック */
	@Override protected void formatCheck() {
		checkDate (shiyouKaishiBi, "使用開始日", false);
		checkDate (shiyouShuuryouBi, "使用終了日", false);
	}
	@Override protected void hissuCheck(int eventNum) {}

	//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		//1.入力チェック
		try(EteamConnection connection = EteamConnection.connect()){
			TeikiJouhouLogic teikiLogic = EteamContainer.getComponent(TeikiJouhouLogic.class, connection);
			//2.データ存在チェック
			//3.アクセス可能チェック
			
			// 駅すぱあと連携可否判定
			boolean ekispertEnable = !"9".equals(setting.intraFlg());
			if(!ekispertEnable) {
				throw new EteamAccessDeniedException(); // アクセスエラー
			}
			
			//4.処理
			teikiJouhouList = teikiLogic.selectTeikiJouhouList("", null, null);
			preEventUrl = "teiki_jouhou_ichiran";
			
			// 全定期券情報のリストを取得			
			displaySeigyo(connection);
			changeListHyouji(teikiJouhouList);
			if(teikiJouhouList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 検索イベント
	 * @return ResultName
	 */
	public String kensaku(){
		
		//1.入力チェック
		formatCheck();
		
		
		if(0 < errorList.size()) {
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			TeikiJouhouLogic teikiLogic = EteamContainer.getComponent(TeikiJouhouLogic.class, connection);
			//2.データ存在チェック
			//3.アクセス可能チェック
			
			// 駅すぱあと連携可否判定
			boolean ekispertEnable = !"9".equals(setting.intraFlg());
			if(!ekispertEnable) {
				throw new EteamAccessDeniedException(); // アクセスエラー
			}
			
			//4.処理
			
			try{
				// イベントURL(遷移元画面)設定
				preEventUrl = "teiki_jouhou_ichiran_kensaku?"  
							+ "userId=" + userId + "&"
							+ "shiyouKaishiBi=" + URLEncoder.encode(shiyouKaishiBi,"UTF-8") + "&"
							+ "shiyouShuuryouBi=" + URLEncoder.encode(shiyouShuuryouBi,"UTF-8");
			} catch (UnsupportedEncodingException e) {
			    throw new RuntimeException(e);
			}
			
			// 指定検索条件に該当する定期券情報のリストを取得
			teikiJouhouList = teikiLogic.selectTeikiJouhouList(userId, toDate(shiyouKaishiBi), toDate(shiyouShuuryouBi));
			displaySeigyo(connection);
			changeListHyouji(teikiJouhouList);
			if(teikiJouhouList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	
	/**
	 * 検索イベント
	 * @return ResultName
	 */
	public String enchou(){
		
		//1.入力チェック
		if(sentaku == null){
			errorList.add("無期限延長対象を選択してください。");
			return "error";
		}
		try(EteamConnection connection = EteamConnection.connect()){

			TeikiJouhouLogic teikiLogic = EteamContainer.getComponent(TeikiJouhouLogic.class, connection);
			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理
			
			// イベントURL(遷移元画面)設定
			preEventUrl = "teiki_jouhou_ichiran_kensaku?"  
						+ "userId=" + kensakuUserId + "&"
						+ "shiyouKaishiBi=" + URLEncoder.encode(kensakuKaishiBi,"UTF-8") + "&"
						+ "shiyouShuuryouBi=" + URLEncoder.encode(kensakuShuuryouBi,"UTF-8");
		
			userId = kensakuUserId;
			shiyouKaishiBi = kensakuKaishiBi;
			shiyouShuuryouBi = kensakuShuuryouBi;
			
			
			//エラー発生時の直前画面の再表示用処理
			List<GMap> redrawList = new ArrayList<GMap>();
			for(int index = 0; index < sentakuFlg.length ; index++){
				GMap map = new GMap();
				map.put("bg_color", EteamCommon.bkColorSettei(rowKaishiBi[index], rowShuuryouBi[index]) );
				map.put("user_id", rowUserId[index]);
				map.put("shiyou_kaishibi", rowKaishiBi[index]);
				map.put("shiyou_shuuryoubi", rowShuuryouBi[index]);
				map.put("user_full_name", rowUserFullName[index]);
				map.put("jyoushakukan",  rowKukan[index]);
				map.put("isChecked", "1".equals(sentakuFlg[index]));
				redrawList.add(map);
			}
			teikiJouhouList = redrawList;
			displaySeigyo(connection);
			
			
			//まず同一ユーザーの複数データが対象とされていないかチェック
			List<String> usrIdLst = new ArrayList<String>();
			List<Integer> chkLst = new ArrayList<Integer>();
			for(int index = 0; index < sentakuFlg.length ; index++){
				if ("0".equals(sentakuFlg[index]))
				{
					continue;
				}
				if (usrIdLst.contains(rowUserId[index])){
					errorList.add("同一ユーザーの複数の定期期間終了日を無期限延長することはできません。");
				}
				usrIdLst.add(rowUserId[index]);
				chkLst.add(index);
			}
			if(0 < errorList.size()) {
				return "error";
			}
			
			//選択データを延長した場合に期間が重複しないかチェック
			for(Integer i : chkLst){
				long dupCnt = teikiLogic.countTeikiJouhouDuplicate(rowUserId[i], toDate(rowKaishiBi[i]), toDate(MUKIGEN_DATE));
				if(dupCnt >= 2){
					errorList.add((i+1) + "行目：無期限延長によりユーザーID[" + rowUserId[i] + "]の定期有効期間が重複します。");
				}
			}
			if(0 < errorList.size()) {
				return "error";
			}
			
			//選択データの使用終了日を9999/12/31に変更
			for(Integer i : chkLst){
				teikiLogic.mukigenEnchou(rowUserId[i], toDate(rowKaishiBi[i]), toDate(rowShuuryouBi[i]));
			}
			
			connection.commit();
			//5.戻り値を返す
			return "success";
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		
		//ユーザーIDから社員番号・氏名取得
		GMap userInfo = bumonUserLogic.selectUserInfo(userId);
		if(userInfo != null){
			shainNo  = (String)userInfo.get("shain_no");
			userName = (String)userInfo.get("user_sei") + "　" + (String)userInfo.get("user_mei");
		}else{
			shainNo  = "";
			userName = "";
		}
		
	}
	
	/**
	 * 画面表示用にリスト内容を編集。
	 * @param chgList 編集対象リスト
	 */
	protected void changeListHyouji(List<GMap> chgList){
		// 表示形式変換
		int index = 0;
		for(GMap map : chgList) {
			map.put("index",             index);
			map.put("bg_color", EteamCommon.bkColorSettei(formatDate(map.get("shiyou_kaishibi")), formatDate(map.get("shiyou_shuuryoubi"))));
			map.put("shiyou_kaishibi",   formatDate(map.get("shiyou_kaishibi")));
			map.put("shiyou_shuuryoubi", formatDate(map.get("shiyou_shuuryoubi")));
			
			if(map.get("user_full_name") == null){
				map.put("user_full_name", "※ユーザー削除済");
			}
			
			//イントラ版かWeb版かで分岐
			if("1".equals(setting.intraFlg())){
				map.put("jyoushakukan",   map.get("intra_teiki_kukan"));
			}else {
				map.put("jyoushakukan",   map.get("web_teiki_kukan"));
			}
			index++;
		}
	}
	
}
