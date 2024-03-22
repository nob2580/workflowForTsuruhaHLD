package eteam.gyoumu.help;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.GamenKengenSeigyoLogic;
import eteam.common.select.HelpCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ヘルプ情報画面Action
 */
@Getter @Setter @ToString
public class HelpViewerAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

	/** 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分) */
	protected String gamenId;

	//＜画面入力以外＞
	/** ヘルプ情報 */
	protected String helpInfo;
	/** 対象画面名 */
	protected String gamenName;
	/** 編集ボタン表示フラグ */
	protected String editBtnFlg;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				{gamenId, "画面ID", "2"},
		};
		hissuCheckCommon(list, eventNum);
	};

//＜イベント＞
	/**
	 * E1:ヘルプ情報初期表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		try(EteamConnection connection = EteamConnection.connect()) {
			
			//1.入力チェック
			hissuCheck(1);

			//2.データ存在チェック
			//3.アクセス可能チェック
			//なし

			//4.処理
			GamenKengenSeigyoLogic kengenLogic = EteamContainer.getComponent(GamenKengenSeigyoLogic.class, connection);
			HelpCategoryLogic hcl = EteamContainer.getComponent(HelpCategoryLogic.class, connection);
			HelpLogic hl = EteamContainer.getComponent(HelpLogic.class, connection);

			// 対象画面名を取得する
			gamenName = hl.judgeGamenName(gamenId);

			// ヘルプ情報を取得する
			GMap map = hcl.find(gamenId);
			if (map != null) {
				helpInfo = map.getString("help_rich_text");
			}
			
			// 編集ボタンの表示判定
			boolean editable = kengenLogic.judgeAccess(EteamCommon.makeGamenId(HelpEditorAction.class), getUser());
			if (editable) {
				editBtnFlg = "1";
			} else {
				editBtnFlg = "0";
			}
			
			// 5.戻り値を返す
			return "success";
		}
	}
}
