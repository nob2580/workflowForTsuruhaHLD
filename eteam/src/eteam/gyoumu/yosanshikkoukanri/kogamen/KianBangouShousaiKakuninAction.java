package eteam.gyoumu.yosanshikkoukanri.kogamen;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案番号詳細確認ダイアログAction
 */
@Getter @Setter @ToString
public class KianBangouShousaiKakuninAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** 伝票区分 */
	String denpyouKbn;
	/** 伝票ID */
	String denpyouId;

//表示
	/** 年度（実施起案） */
	String jisshiNendo = "";
	/** 起案番号（実施起案） */
	String jisshiKianBangou = "";
	/** 年度（支出起案） */
	String shishutsuNendo = "";
	/** 起案番号（支出起案） */
	String shishutsuKianBangou = "";

//＜制御＞
	/** 伝票管理Logic */
	protected DenpyouKanriLogic denpyouKanriLogic;
	/** ワークフローイベント制御Logic */
	protected WorkflowEventControlLogic wfEventControlLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianBangouShousaiKakuninAction.class);

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 処理なし
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		// 処理なし
	}

	/**
	 * 部品の初期化
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		// 伝票管理Logic
		this.denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		// ワークフローイベント制御Logic
		this.wfEventControlLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
	}
	
	/**
	 * 共通制御処理<br>
	 * 検索イベントやエラー表示時用に画面の共通制御を行う。<br>
	 */
	protected void displaySeigyo() {

		/*
		 * 伝票起案紐付から実施起案と支出起案の年度と起案番号を取得する。
		 */
		GMap aMap = this.wfEventControlLogic.selectDenpyouKianHimozuke(this.denpyouId);
		if (super.isNotEmpty((String)aMap.get("jisshi_nendo"))){
			this.jisshiNendo = aMap.get("jisshi_nendo").toString();
		}
		if (super.isNotEmpty((String)aMap.get("jisshi_kian_bangou"))){
			this.jisshiKianBangou = aMap.get("jisshi_kian_bangou").toString();
		}
		if (super.isNotEmpty((String)aMap.get("shishutsu_nendo"))){
			this.shishutsuNendo = aMap.get("shishutsu_nendo").toString();
		}
		if (super.isNotEmpty((String)aMap.get("shishutsu_kian_bangou"))){
			this.shishutsuKianBangou = aMap.get("shishutsu_kian_bangou").toString();
		}

		/*
		 * 	伝票の「予算執行対象」から発番側の起案番号に印を追加する。
		 */
		String yosanShikkouTaishou = denpyouKanriLogic.getYosanShikkouTaishou(this.denpyouKbn);
		switch (yosanShikkouTaishou){
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN:
			this.jisshiKianBangou += "  *";
			break;

		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN:
			this.shishutsuKianBangou += "  *";
			break;

		default:
			break;
		}
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			// 共通制御処理
			displaySeigyo();

			/*
			 * 1.入力チェック
			 */
			// なし

			/*
			 * 2.データ存在チェック
			 */
			// なし

			/*
			 * 3.アクセス可能チェック なし
			 */
			// なし

			/*
			 * 4.処理 
			 */
			// なし

			// 5.戻り値を返す
			return "success";

		}
	}
}
