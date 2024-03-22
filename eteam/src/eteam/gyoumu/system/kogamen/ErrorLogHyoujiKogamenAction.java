package eteam.gyoumu.system.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バッチ連携結果確認画面Action
 */
@Getter @Setter @ToString
public class ErrorLogHyoujiKogamenAction extends EteamAbstractAction {

//＜非表示項目＞
	/** シリアル番号 */
	int serialNo;
	/** インデックス */
	int index;
	/** 最大インデックス */
	int maxIndex;
	
//＜画面入力以外＞
	
	/** 紐づけList */
	List<GMap> himodukeList;
	/** 不良データList */
	List<GMap> invalidFileList;
	/** 不良伝票List */
	List<GMap> invalidDenpyouList;

//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** システム管理　SELECT */
	SystemKanriCategoryLogic systemLogic;

//＜入力チェック＞
	@Override 
	protected void formatCheck() {
	}

	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目								 	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		connection = EteamConnection.connect();
		try{
			initParts();
			maxIndex = 1;
			
			himodukeList = systemLogic.loadBatchErrorLogHimoduke(serialNo);
			if (himodukeList.isEmpty()) {
				errorList.add("ログが0件でした。");
				return "error";
			}
			maxIndex = himodukeList.size();
			String fileName = (String)himodukeList.get(index-1).get("file_name");
			invalidFileList = systemLogic.loadInvalidFileLog(fileName);
			invalidDenpyouList = systemLogic.loadInvalidDenpyouLog(fileName);
			if (!invalidDenpyouList.isEmpty()) {
				for (GMap invalidDenpyou : invalidDenpyouList) {
					invalidDenpyou.put("denpyou_date", formatDate(invalidDenpyou.get("denpyou_date")));
				}
			}
			return "success";
		}finally{
			connection.close();
		}
	}
	

	/**
	 * 部品初期化
	 */
	protected void initParts() {
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}
}
