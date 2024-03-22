package eteam.gyoumu.yosanshikkoukanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.RegAccess;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 速報PL処理年月設定Action
 */
@Getter @Setter @ToString
public class YosanShikkouShoriNengetsuSetteiAction extends EteamAbstractAction {

//＜画面入力＞
	/** 開始月 */
	String fromNengetsu;
	/** 終了月 */
	String toNengetsu;

//＜画面入力以外＞
	/** 選択月リスト */
	@Getter
	protected List<GMap> monthList;

//＜部品＞
	/** マスターカテゴリSELECT */
	MasterKanriCategoryLogic masterCommonLg;
	/** ワークフローカテゴリーロジック */
	WorkflowEventControlLogic wfEventLg;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDateYYYYMM(fromNengetsu , "開始月", false);
		checkDateYYYYMM(toNengetsu, "終了月", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目							項目名 												必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{fromNengetsu, "開始月", "0", "1"},
				{toNengetsu, "終了月", "0", "1"},
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			masterCommonLg = (MasterKanriCategoryLogic)EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			wfEventLg = (WorkflowEventControlLogic)EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
			// 月リストの取得
			monthList = wfEventLg.createKiKesnNengetsuList();
			//1.入力チェック なし
			//2.データ存在チェック なし
			
			//3.アクセス可能チェック
			// 会社設定-予算チェック期間が『対象月まで』のとき、アクセスエラーとする
			if (false == RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())){
				throw new EteamAccessDeniedException();
			}else if(! setting.yosanCheckKikan().equals(yosanCheckKikan.TO_TAISHOUZUKI)) {
				throw new EteamAccessDeniedException();
			}
			
			//4.処理
			GMap dateMap = masterCommonLg.findYosanShikkouShoriNengetsu();
			if (dateMap != null) {
				// 開始年月を設定
				fromNengetsu    = dateMap.get("from_nengetsu");
				// 終了年月を設定
				toNengetsu    = dateMap.get("to_nengetsu");
			} else {
				fromNengetsu = monthList.get(monthList.size()-1).get("key");
				toNengetsu = monthList.get(0).get("key");
			}
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 変更処理
	 * @return ResultName
	 */
	public String henkou() {
		try(EteamConnection connection = EteamConnection.connect()) {
			masterCommonLg = (MasterKanriCategoryLogic)EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			wfEventLg = (WorkflowEventControlLogic)EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
			
			// 月リストの取得
			monthList = wfEventLg.createKiKesnNengetsuList();
			// 1.入力チェック
			hissuCheck(2);
			formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}
			// 2.相関チェック
			soukanCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}

			// 3.処理
			YosanShikkouShoriNengetsuSetteiLogic myLogic = (YosanShikkouShoriNengetsuSetteiLogic)EteamContainer.getComponent(YosanShikkouShoriNengetsuSetteiLogic.class, connection);
			GMap dateMap = masterCommonLg.findYosanShikkouShoriNengetsu();
			if (dateMap != null) {
				myLogic.update(fromNengetsu, toNengetsu);
			} else {
				myLogic.insert(fromNengetsu, toNengetsu);
			}
			connection.commit();
			//4.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 相関チェックを実施する。
	 */
	protected void soukanCheck() {
		if(fromNengetsu.compareTo(toNengetsu) > 0) {
			errorList.add("終了月は開始月以降にしてください。");
		}
	}
}
