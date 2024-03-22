package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 締画面Action
 */
@Getter @Setter @ToString
public class ShimeAction extends EteamAbstractAction{

	//＜定数＞

	//＜画面入力＞
	/** 締日 */
	String shimebi;
	/** 解除日(hidden) */
	String kaijobi;
	/** タイトル */
	String pageTitle;
	/** 伝票区分 */
	String denpyouKbn;

	//＜画面入力以外＞
	/** 締日リスト */
	List<GMap> shimebiList;

	/** Logic */
	ShimeLogic lg;
	/** 会計 SELECT */
	KaikeiCategoryLogic kLg;
	/** システム管理ロジック */
	SystemKanriCategoryLogic sLg;

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDate(shimebi, "締日", false);
		checkDate(kaijobi, "解除日", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		//項目				 			EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
		String[][] list = {
				{shimebi, "締日", "0", "1", "0"},
				{kaijobi, "解除日", "0", "0", "1"},
		};
		hissuCheckCommon(list, eventNum);
	}

	//＜イベント＞
	/**
	 * 初期表示
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			init(connection);

			// 入力チェック
			formatCheck();
			hissuCheck(1);

			// 表示用締日 取得
			displaySeigyo(connection);
			return "success";
		}
	}

	/**
	 * 締処理
	 * @return ResultName
	 */
	public String shime(){
		try(EteamConnection connection = EteamConnection.connect()){
			init(connection);

			// 入力チェック
			formatCheck();
			hissuCheck(2);

			// エラーなら表示用締日 取得してエラー表示
			if (! errorList.isEmpty()) {
				displaySeigyo(connection);
				return "error";
			}

			// 締日登録
			lg.insertShinsei(denpyouKbn, toDate(shimebi), getUser().getTourokuOrKoushinUserId());
			connection.commit();
			return "success";
		}
	}

	/**
	 * 解除
	 * @return ResultName
	 */
	public String kaijo(){
		try(EteamConnection connection = EteamConnection.connect()){
			init(connection);

			// 入力チェック
			formatCheck();
			hissuCheck(3);

			// エラーなら表示用締日 取得してエラー表示
			if (! errorList.isEmpty()) {
				displaySeigyo(connection);
				return "error";
			}

			// 締日登録
			lg.deleteShinsei(denpyouKbn, toDate(kaijobi));
			connection.commit();
			return "success";
		}
	}

	//＜内部メソッド＞
	/**
	 * 部品初期化
	 * @param connection コネクション
	 */
	protected void init(EteamConnection connection) {
		lg = EteamContainer.getComponent(ShimeLogic.class, connection);
		kLg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		sLg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}

	/**
	 * 画面表示共通
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		shimebiList = kLg.loadKeijoubiShimebi(denpyouKbn);
		for (GMap shimebiMap : shimebiList) {
			shimebiMap.put("shimebi", formatDate(shimebiMap.get("shimebi")));
		}

		pageTitle = sLg.findNaibuCdName("denpyou_kbn", denpyouKbn);
		if(pageTitle.indexOf("（") >= 0)
		{
			pageTitle = pageTitle.substring(0, pageTitle.indexOf("（"));
		}
	}
}