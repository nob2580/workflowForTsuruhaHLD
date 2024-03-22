package eteam.gyoumu.workflow.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.dao.TenpuDenpyouJyogaiDao;
import eteam.database.dto.TenpuDenpyouJyogai;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 関連伝票選択ダイアログAction
 */
@Getter @Setter @ToString
public class KanrenDenpyouSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票ID */
	String denpyouId;
	/** 伝票種別 */
	String denpyouShubetsu;
	/** 承認日From */
	String shouninFrom;
	/** 承認日To */
	String shouninTo;
	/** 伝票選択チェックボックス */
	String sentaku;
	
	/** 除外データ表示フラグ */ //チェックボックスがONなら1、OFFなら0(hidden)
	String jyogaiDataHyoujiFlg;
	/** 除外フラグ変更対象リスト */ //伝票IDのカンマ区切り
	String jyogaiChangeList;
	/** 除外フラグ変更先 */ //1:除外、0:復活
	String jyogaiChangeFlg;

	
//＜画面入力以外＞
	/** 伝票種別リスト */
	List<GMap> shubetsuList;
	/** 関連伝票リスト */
	List<GMap> kanrenList;
	/** 伝票ID */
	String gamenDenpyouId;
	
//＜制御＞
	/** ワークフロー */
	WorkflowEventControlLogic workflowLg;
	/** カテゴリー */
	WorkflowCategoryLogic workCateLg;
	/** 起票ナビ */
	KihyouNaviCategoryLogic kihyouLg;
	/** 会計 */
	KaikeiCommonLogic kaikeiLg;
	/** 添付伝票除外対象テーブルDAO */
	TenpuDenpyouJyogaiDao jyogaiDao;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouId, 19,  19, "伝票ID", false);
		checkString(denpyouShubetsu,  4,   4, "伝票種別", false);
		checkDate(shouninFrom, "承認日From", false);
		checkDate(shouninTo, "承認日To", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目									EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouId ,"伝票ID"			,"0"}, 
			{denpyouShubetsu ,"伝票種別"			,"0"}, 
			{shouninFrom ,"精算予定日(開始)"	,"0"}, 
			{shouninTo ,"精算予定日(終了)"	,"0"}, 
		};
		hissuCheckCommon(list, eventNum);
	}

	/**
	 * 部品の初期化
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		workflowLg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		workCateLg = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		kihyouLg = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		kaikeiLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		jyogaiDao = EteamContainer.getComponent(TenpuDenpyouJyogaiDao.class, connection);
		
		// 有効な種別リストを取得
		shubetsuList = kihyouLg.loadKanrenDenpyouKanri();
		
	}
	
	/**
	 * 画面表示イベントやイベントエラー表示時用に、画面の共通制御処理を行う。
	 */
	protected void displaySeigyo() {
		// 現伝票の起票ユーザーが起票した伝票を取得
		GMap kihyouUser = kaikeiLg.findKihyouUser(gamenDenpyouId);
		String kensakuUser = kihyouUser == null ? getUser().getSeigyoUserId() : (String)kihyouUser.get("user_id");
		kanrenList = workCateLg.loadEnableKanrenDenpyou(kensakuUser, denpyouId, denpyouShubetsu, shouninFrom, shouninTo, jyogaiDataHyoujiFlg);
		
		if (kanrenList.isEmpty()) {
			errorList.add("検索結果が0件でした。");
		}
		
		// 関連伝票リスト作成
		workflowLg.formatKanrenDenpyou(kanrenList);
		
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			
			initParts(connection);
			displaySeigyo();
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			hissuCheck(1);
			formatCheck();
			if(0 < errorList.size()){ return "error"; }
			
			displaySeigyo();
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 確定イベント
	 * @return 処理結果
	 */
	public String confirm(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			hissuCheck(1);
			formatCheck();
			if(0 < errorList.size()){ return "error"; }
			
			displaySeigyo();
			
			if (sentaku == null || sentaku.isEmpty()) {
				errorList.add("添付伝票を1つ以上選択してください。");
				
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 関連伝票の除外・再表示処理
	 * @return 処理結果
	 */
	public String jyogaiChange(){
		//1.入力チェック なし
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし
			//4.処理（初期表示）
			String[] jyogaiIdList = jyogaiChangeList.split(",");
			for(String jyogaiId : jyogaiIdList) {
				if("1".equals(jyogaiChangeFlg)) {
					//除外
					TenpuDenpyouJyogai dto = new TenpuDenpyouJyogai();
					dto.denpyouId = jyogaiId;
					jyogaiDao.upsert(dto);
				}else if("0".equals(jyogaiChangeFlg)) {
					//復活
					jyogaiDao.delete(jyogaiId);
				}
			}
			connection.commit();
			
			return init();
		}
	}

}
