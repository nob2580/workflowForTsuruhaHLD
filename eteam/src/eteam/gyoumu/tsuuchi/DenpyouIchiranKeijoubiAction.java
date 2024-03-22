package eteam.gyoumu.tsuuchi;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.select.MasterKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧の表示項目カスタマイズを行うアクションです。
 */
@Getter @Setter @ToString
public class DenpyouIchiranKeijoubiAction extends EteamAbstractAction {

//＜画面入力＞
	/** 伝票区分(一覧画面の検索条件を引き継ぐ) */
	String denpyouKbn;

//＜画面入力以外＞
	/** 計上日の選択候補 */
	List<String> keijoubiList;


//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}

//＜イベント＞
	/**
	 * 初期表示イベント。
	 * @return 処理結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			keijoubiList = masterLogic.loadKeijoubiList(denpyouKbn);
		}
		return "success";
	}
}
