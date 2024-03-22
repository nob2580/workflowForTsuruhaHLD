package eteam.gyoumu.kanitodoke;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KaniTodokeCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書一覧画面Action
 */
@Getter @Setter @ToString
public class KaniTodokeIchiranAction extends EteamAbstractAction {
	
	/** 一覧 */
	List<GMap> list;

	//＜入力チェック＞
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

	//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){

		
		try(EteamConnection connection = EteamConnection.connect()){
			
			KaniTodokeCategoryLogic ktclc = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);

			list = ktclc.loadKaniTodokeIchiran();
			
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			return "success";
			
		}
	}
}
