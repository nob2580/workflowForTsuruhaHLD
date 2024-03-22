package eteam.gyoumu.system;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.WorkflowCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日中データ連携画面Action
 */
@Getter @Setter @ToString
public class NittyuuDataRenkeiAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 仕訳データ連携リスト */
	String[] swkRenkeiArr;
	/** マスターデータ連携リスト */
	String[] mstRenkeiArr;

//＜画面入力以外＞
	/** 請求書払い有無(契約オプション) */
	boolean seikyuushoBaraiOn;
	/** 支払依頼有無(オプション) */
	boolean shiharaiIraiOn;


//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

	/**
	 * 項目間チェック
	 */
	protected void soukanCheck() {
		//連携対象機能の必須チェック
		if ((swkRenkeiArr == null || swkRenkeiArr.length == 0)
				&& (mstRenkeiArr == null || mstRenkeiArr.length == 0)) {
			errorList.add("連携対象機能をチェックしてください。");
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			//1.入力チェック
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理
			displaySeigyo(connection);

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 連携イベント処理
	 * @return ResultName
	 */
	public String renkei() {
		try(EteamConnection connection = EteamConnection.connect()) {
			displaySeigyo(connection);

			// 1.入力チェック
			soukanCheck();
			if (0 < errorList.size())
			{
				return "error";
			}

			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理
			NittyuuDataRenkeiThread myThread = new NittyuuDataRenkeiThread(swkRenkeiArr, mstRenkeiArr, EteamCommon.getContextSchemaName());
			myThread.start();
			
			//5.戻り値を返す
			return "success";
		}
	}

//＜内部処理＞
	/**
	 * 画面表示共通
	 * @param connection コネクション
	 */
	public void displaySeigyo(EteamConnection connection) {
		WorkflowCategoryLogic lc = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		seikyuushoBaraiOn = (null != lc.selectDenpyouShubetu(DENPYOU_KBN.SEIKYUUSHO_BARAI));
		shiharaiIraiOn = (null != lc.selectDenpyouShubetu(DENPYOU_KBN.SIHARAIIRAI));
	}
}
