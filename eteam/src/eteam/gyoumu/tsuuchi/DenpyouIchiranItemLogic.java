package eteam.gyoumu.tsuuchi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;

/**
 * 伝票検索結果の表示項目に関するロジックです。
 */
public class DenpyouIchiranItemLogic extends EteamAbstractLogic {
 
	/** 全ユーザー共通設定リスト用ユーザーID */
	protected static final String ALL_USER_ID = "";
	/** 項目制御区分 */
	public static class LIST_ITEM_CONTROL_KBN {
		/** 伝票一覧 */
		public static final String ITEM_KBN_DENPYOU_ICHIRAN = "1";
		/** 伝票一覧(全ユーザー共通表示用) */
		public static final String ITEM_KBN_DENPYOU_ICHIRAN_ALLUSER = "2";
	}
	
	/**
	 * ユーザー用画面表示項目を取得します。
	 * @param kbn 区分
	 * @return 表示項目情報
	 */
	public List<DenpyouDisplayItem> getDefaultDisplayItemList(String kbn) {
		List<DenpyouDisplayItem> ret = null;
		WorkflowEventControlLogic wfEventLg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		switch(kbn) {
		case LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN:
		case LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN_ALLUSER:
			ret = DenpyouDisplayItem.getDefaultDisplayItemList();
			if("0".equals(wfEventLg.judgeinvoiceStart())) {
				ret.remove(ret.size()-1);//最後に追加した二つを削除したいため、ret.size()-1を2回繰り替えしている。もっといい方法はある？
				ret.remove(ret.size()-1);
			}
			break;
		default:
			throw new EteamDataNotFoundException();
		}
		
		if (yosanCheckKikan.TO_TAISHOUZUKI.equals(setting.yosanCheckKikan()))
		{
			return ret;
		}
		
		//予算チェック期間が『通年』の場合は予算執行対象月を除去
		Iterator<DenpyouDisplayItem> it = ret.iterator();
		while(it.hasNext()){
			DenpyouDisplayItem item = it.next();
			if("yosan_check_nengetsu".equals(item.getName())){
				it.remove();
				break;
			}
		}

		return ret;
	}
	
	/**
	 * 表示項目を更新します。
	 * @param kbn 区分
	 * @param loginUserId ユーザーID
	 * @param gyoumuRoleId 業務ロールID
	 * @param list 更新するデータ
	 * @return 結果
	 */
	public int replaceDisplayItem(String kbn, String loginUserId, String gyoumuRoleId, List<DenpyouDisplayItem> list) {
		final String delSql = "DELETE FROM list_item_control WHERE kbn = ? AND user_id = ? AND gyoumu_role_id = ? ";
		final String insSql = "INSERT INTO list_item_control (kbn, user_id, gyoumu_role_id, index, name, display_flg) VALUES (?, ?, ?, ?, ?, ?) ";

		String roleId = gyoumuRoleId == null ? "" : gyoumuRoleId;
		connection.update(delSql, kbn, loginUserId, roleId);
		int cnt = 0;
		for (DenpyouDisplayItem item : list) {
			cnt += connection.update(insSql, kbn, loginUserId, roleId, item.getIndex(), item.getName(), item.isDisplay() ? "1" : "0");
		}
		return cnt;
	}
	
	/**
	 * 全ユーザー共通設定表示項目を更新します。
	 * @param kbn 区分
	 * @param list 更新するデータ
	 * @return 結果
	 */
	public int replaceDisplayItemAllUser(String kbn, List<DenpyouDisplayItem> list) {
		return replaceDisplayItem(kbn, ALL_USER_ID, "", list);
	}

	/**
	 * ユーザー用画面表示項目を取得し存在しない項目を埋めて、表示用のリストとします。
	 * @param kbn 区分
	 * @param loginUserId ログインユーザID
	 * @param gyoumuRoleId 業務ロールID
	 * @return 表示項目情報
	 */
	public List<DenpyouDisplayItem> getDisplayItemList(String kbn, String loginUserId, String gyoumuRoleId) {
		List<DenpyouDisplayItem> list = selectDisplayItemList(kbn, loginUserId, gyoumuRoleId);
		List<DenpyouDisplayItem> baseList = getDefaultDisplayItemList(kbn);
		
		if (null == list || list.isEmpty()) {
			// 区分に応じた共通設定を取得
			if (kbn.equals(LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN)) {
				list = selectDisplayItemList(LIST_ITEM_CONTROL_KBN.ITEM_KBN_DENPYOU_ICHIRAN_ALLUSER, ALL_USER_ID, gyoumuRoleId);
			}
			// TODO:elseで良くないか？(5/14 石田)
			if (null == list || list.isEmpty()) {
				//設定未保存かつ全ユーザー共通設定なしなのでデフォルト設定を返す
				return baseList;
			}
		}
		addNameForList(list,baseList);
		return list;
	}
	
	/**
	 * 全ユーザー共通設定用画面表示項目を取得します。
	 * 画面表示のため、編集項目名情報を追加します。
	 * @param kbn 区分
	 * @return 表示項目情報
	 */
	public List<DenpyouDisplayItem> getDisplayItemList(String kbn){
		return getDisplayItemList(kbn, "", "");
	}

	/**
	 * ユーザー用画面表示項目を取得します。
	 * @param kbn 区分
	 * @param loginUserId ログインユーザID
	 * @param gyoumuRoleId 業務ロールID
	 * @return 表示項目情報
	 */
	protected List<DenpyouDisplayItem> selectDisplayItemList(String kbn, String loginUserId, String gyoumuRoleId) {
		
		String sql = "SELECT index, name, display_flg FROM list_item_control WHERE kbn = ? AND user_id = ? AND gyoumu_role_id = ?";
		if(yosanCheckKikan.YEAR.equals(setting.yosanCheckKikan())) sql = sql.concat(" AND name <> 'yosan_check_nengetsu'");
		sql = sql.concat(" ORDER BY index");

		List<GMap> list = connection.load(sql, kbn, loginUserId, gyoumuRoleId == null ? "" : gyoumuRoleId);
		if (null == list || list.isEmpty()) {
			return null;
		}
		List<DenpyouDisplayItem> retList = new ArrayList<DenpyouDisplayItem>(list.size());
		for (GMap map : list) {
			retList.add(new DenpyouDisplayItem((Integer)map.get("index"), (String)map.get("name"), "1".equals(map.get("display_flg")), ""));
		}

		return retList;
	}

	/**
	 * 指定したユーザーの表示項目個人設定を削除します。
	 * @param kbn 区分
	 * @param loginUserId ログインユーザID
	 * @param gyoumuRoleId 業務ロールID
	 * @return 結果
	 */
	public int deleteDisplayItem(String kbn, String loginUserId, String gyoumuRoleId){
		final String delSql = "DELETE FROM list_item_control WHERE kbn = ? AND user_id = ? AND gyoumu_role_id = ? ";
		String roleId = gyoumuRoleId == null ? "" : gyoumuRoleId;
		int cnt = connection.update(delSql, kbn, loginUserId, roleId);
		return cnt;
	}
	
	/**
	 * 対象リストに、画面表示用の項目名とVUPによる追加項目を追加します。
	 * @param addList  項目名称追加前リスト
	 * @param baseList デフォルト画面表示用リスト(名称付き)
	 */
	protected void addNameForList(List<DenpyouDisplayItem> addList, List<DenpyouDisplayItem> baseList){
		//設定保存済なので保存済のをベースに、足りていないデフォルト設定（VUPによる追加想定）を追加
		List<DenpyouDisplayItem> tmpList = new ArrayList<DenpyouDisplayItem>();
		for (DenpyouDisplayItem bItem : baseList) {
			boolean isFind = false;
			for (DenpyouDisplayItem item : addList) {
				if (item.getName().equals(bItem.getName())) {
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				tmpList.add(bItem);
			}
		}
		addList.addAll(tmpList);
		
		//表示項目名はDBに保存されないのでここで補う
		for (DenpyouDisplayItem bItem : baseList) {
			for (DenpyouDisplayItem item : addList) {
				if (item.getName().equals(bItem.getName())) {
					item.setLabel(bItem.getLabel());
				}
			}
		}
		
		// 名称がない場合(=オプション項目が途中でOn→Offとなった場合)リストから削除
		List<DenpyouDisplayItem> removeList = addList.stream().filter(x -> x.label.isBlank()).collect(Collectors.toList());
		addList.removeAll(removeList);
	}
}
