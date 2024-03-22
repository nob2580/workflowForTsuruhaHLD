package eteam.gyoumu.kanitodoke.kogamen;

import eteam.base.EteamAbstractAction;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書チェックボックス画面Action
 */
@Getter @Setter @ToString
public class KaniTodokeCheckBoxAddAction extends EteamAbstractAction {

	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

	//＜定数＞
	/** 表示モード(追加) */
	protected static final int DISP_MODE_ADD = 1;
	/** 表示モード(変更) */
	protected static final int DISP_MODE_CHANGE = 2;
	
	//＜画面入力＞
	/** 表示モード */
	Integer dispMode;
	/** ラベル名 */
	String labelName;
	/** 必須フラグ */
	Integer hissuFlg;
	/** チェックボックスラベル名 */
	String checkBoxName;
	
	//＜画面入力以外＞
	/** データ */
	String data;
	
	//＜イベント＞
	/**
	 * 初期表示イベント
	 * 
	 * @return 処理結果
	 */
	public String init() {
		
		if (isEmpty(data)) {
			dispMode = DISP_MODE_ADD; // 追加
			labelName = ""; // ラベル名
			hissuFlg = 1; // 必須フラグ
			checkBoxName = ""; // チェックボックスラベル名
		} else {

			String[] dataArray = data.split("\r\n");
			String hissu_flg   = dataArray[DataListIndex.HISSU_FLG];  		// 必須フラグ
			String label_name  = dataArray[DataListIndex.LABEL_NAME];  		// ラベル名
			String checkbox_label_name = "";  
			
			// チェックボックスラベル名
			if (dataArray.length >= 14) {
				checkbox_label_name = dataArray[DataListIndex.CHECKBOX_LABEL_NAME];  
			}
			
			dispMode = DISP_MODE_CHANGE; // 変更
			labelName = label_name; // ラベル名
			hissuFlg = Integer.parseInt(hissu_flg); // 必須フラグ
			checkBoxName = checkbox_label_name; // チェックボックスラベル名
		}
		
		return "success";
	}
}
