package eteam.gyoumu.tsuuchi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.gyoumu.tsuuchi.DenpyouIchiranItemLogic.LIST_ITEM_CONTROL_KBN;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧の表示項目カスタマイズを行うアクションです。
 */
@Getter @Setter @ToString
public class DenpyouKoumokuKyoutsuSetteiAction extends EteamAbstractAction {

//＜画面入力＞
	/** 表示フラグ */
	String displayItem[];
	/** 項目名 */
	String itemName[];

//＜画面入力以外＞
	//表示
	/** 表示項目リスト */
	List<DenpyouDisplayItem> itemList;


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
			DenpyouIchiranItemLogic logic = EteamContainer.getComponent(DenpyouIchiranItemLogic.class, connection);
			
			//全ユーザー共通用リスト取得
			itemList = logic.getDisplayItemList(LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN_ALLUSER);
			
		}

		return "success";
	}

	/**
	 * 登録イベント。
	 * @return 処理結果
	 */
	public String touroku() {

		try(EteamConnection connection = EteamConnection.connect()) {
			DenpyouIchiranItemLogic logic = EteamContainer.getComponent(DenpyouIchiranItemLogic.class, connection);
			
			//全ユーザー共通用リスト取得
			itemList = logic.getDisplayItemList(LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN_ALLUSER);

			// 入力チェック
			if (null == itemName || itemName.length != itemList.size()) {
				errorList.add("不正アクセスです。");
				return "error";
			}

			// 更新処理
			LinkedHashMap<String, DenpyouDisplayItem> linkedMap = new LinkedHashMap<String, DenpyouDisplayItem>();
			for (DenpyouDisplayItem item : itemList) {
				if (!item.isEditable()) {
					item.setDisplay(true);
				} else {
					item.setDisplay(false);
				}
				linkedMap.put(item.getName(), item);
			}
			// 表示対象のフラグを立てる
			if (null != displayItem) {
				for (int ii = 0; ii < displayItem.length; ii++) {
					DenpyouDisplayItem item = linkedMap.get(displayItem[ii]);
					if (null != item) {
						item.setDisplay(true);
					}
				}
			}
			// 渡された順に並べる
			List<DenpyouDisplayItem> tmpList = new ArrayList<DenpyouDisplayItem>(itemList.size());
			for (int ii = 0; ii < itemName.length; ii++) {
				DenpyouDisplayItem item = linkedMap.get(itemName[ii]);
				if (null != item) {
					item.setIndex(ii);
					tmpList.add(item);
				}
			}
			logic.replaceDisplayItemAllUser(LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN_ALLUSER, tmpList);
			connection.commit();
			itemList = tmpList;
		}
		
		return "success";
	}
}
