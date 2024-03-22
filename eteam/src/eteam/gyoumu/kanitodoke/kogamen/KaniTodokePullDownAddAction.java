package eteam.gyoumu.kanitodoke.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.OptionListIndex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザー定義届書プルダウン追加画面Action
 */
@Getter @Setter @ToString
public class KaniTodokePullDownAddAction extends EteamAbstractAction {

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
	
	//＜画面入力以外＞
	/** リスト */
	List<GMap> pulldownList;
	/** リストオプション */
	List<GMap> pulldownOptionList;
	/** データ */
	String data;
	/** オプションリスト */
	String[] optionList;
	
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

	//＜イベント＞
	/**
	 * 初期表示イベント
	 * 
	 * @return 処理結果
	 */
	public String init() {

		// コネクション生成
		
		try(EteamConnection connection = EteamConnection.connect())
		{
			KaniTodokeCategoryLogic kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
			
			// プルダウンリスト
			pulldownList = kaniTodokeCategoryLogic.loadKaniTodokeSelectItem();
			
			// プルダウンオプションリスト
			pulldownOptionList = kaniTodokeCategoryLogic.loadKaniTodokeSelectItemOption();

			if (isEmpty(data)) {
				dispMode = DISP_MODE_ADD; // 追加
				labelName = ""; // ラベル名
				hissuFlg = 1; // 必須フラグ
				GMap mp = new GMap();
				mp.put("select_item","");
				mp.put("selected",true);
				pulldownList.add(0,mp);
			} else {

				String[] dataArray = data.split("\r\n");
				String hissu_flg   = dataArray[DataListIndex.HISSU_FLG];  		// 必須フラグ
				String label_name  = dataArray[DataListIndex.LABEL_NAME];  		// ラベル名

				dispMode = DISP_MODE_CHANGE; // 変更
				labelName = label_name; // ラベル名
				hissuFlg = Integer.parseInt(hissu_flg); // 必須フラグ
				
				//kani_todoke_list.select_itemが設定されている場合は該当リストを表示させる
				boolean existFlg = false;
				for (String option : optionList) {
					if (isNotEmpty(option)) {
						String[] optionArray = option.split("\r\n");
						if (optionArray.length == 6) {
							String selItem = optionArray[OptionListIndex.SELECT_ITEM];
							if(isEmpty(selItem)) {continue;}
							for(GMap mp : pulldownList) {
								if(selItem.equals(mp.get("select_item"))) {
									mp.put("selected",true);
									existFlg = true;
									break;
								}
							}
						}
					}
				}
				//kani_todoke_list.select_itemがブランクか、対応マスタが無かった場合は空のリストを表示(そのまま登録しようとした場合はエラー)
				if(!existFlg) {
					GMap mp = new GMap();
					mp.put("select_item","");
					mp.put("selected",true);
					pulldownList.add(0,mp);
				}
			}
			
			// 戻り値を返す
			return "success";
			
		}
	}
}
