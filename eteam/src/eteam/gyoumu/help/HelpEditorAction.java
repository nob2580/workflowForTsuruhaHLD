package eteam.gyoumu.help;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.HelpCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ヘルプ編集画面Action
 */
@Getter @Setter @ToString
public class HelpEditorAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

	/** 画面ID(ユーザー定義届書の場合のみ画面ID + 伝票区分) */
	protected String gamenId;
	/** ヘルプテキスト */
	protected String input;

//＜画面入力以外＞
	/** 対象画面名 */
	protected String gamenName;
	/** 削除ボタン表示フラグ(1:表示、0:非表示) */
	protected String delBtnFlg;
	/** イベントコードのマジックナンバー:更新 */
	protected static final int updateEvent = 2;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(input, 1, 50000, "ヘルプ情報", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				{input, "ヘルプ情報", "0", "1" },
				{gamenId, "画面ID", "2", "0"}
		};
		hissuCheckCommon(list, eventNum);
	};

//＜イベント＞
	/**
	 * E1:ヘルプ編集初期表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		try(EteamConnection connection = EteamConnection.connect()) {

			//1.入力チェック
			hissuCheck(1);

			//2.データ存在チェック
			//3.アクセス可能チェック
			// なし

			HelpCategoryLogic hcl = EteamContainer.getComponent(HelpCategoryLogic.class, connection);
			HelpLogic hl = EteamContainer.getComponent(HelpLogic.class, connection);

			// 対象画面名を取得する
			gamenName = hl.judgeGamenName(gamenId);

			// ヘルプ情報を取得する
			GMap map = hcl.find(gamenId);

			// ヘルプ情報があればテキスト表示＆削除ボタン表示
			if (map != null) {
				input = map.getString("help_rich_text");
				delBtnFlg = "1";
			} else {
				delBtnFlg = "0";
			}
			
			// 5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * ヘルプ編集画面更新処理
	 * @return 成功：success, 失敗:error
	 */
	public String koushin() {
		
		try(EteamConnection connection = EteamConnection.connect()) {
			//1.入力チェック
			hissuCheck(updateEvent);
			formatCheck();
			
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				delBtnFlg = "1";
				return "error";
			}
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			// なし
			
			//4.処理
			HelpCategoryLogic hcl = EteamContainer.getComponent(HelpCategoryLogic.class, connection);
			HelpLogic hl = EteamContainer.getComponent(HelpLogic.class, connection);

			// 登録処理
			hl.insertHelp(getUser().getSeigyoUserId(), gamenId, input, hcl.hasHelp(gamenId));
			
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * ヘルプ編集画面削除処理
	 * @return 成功：success, 失敗:error
	 */
	public String sakujo() {
		
		try(EteamConnection connection = EteamConnection.connect()) {
			
			//1.入力チェック
			//2.データ存在チェック
			//3.アクセス可能チェック
			
			HelpLogic hl = EteamContainer.getComponent(HelpLogic.class, connection);
			
			//4.処理
			// 削除処理
			hl.deleteHelp(gamenId);
			
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		
		}
	}
}
