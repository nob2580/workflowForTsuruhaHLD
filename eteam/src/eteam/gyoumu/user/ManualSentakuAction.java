package eteam.gyoumu.user;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.common.EteamCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ロール選択画面Action
 */
@Getter @Setter @ToString
public class ManualSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 業務ロールID */
	String selectedManual;

//＜画面入力以外＞
	/** ログイン前のURL */
	String originalURL;
	/** 業務ロールID */
	String gyoumuRoleId;
	/** 業務ロール 機能制御コード */
	String gyoumuRoleKinouSeigyoCd;
	/** 業務ロール 機能制御区分 */
	String gyoumuRoleKinouSeigyoKbn;
	/**	経理処理の業務ロールを持つかどうか */
	boolean keiriShoriRole;
	/**	管理者の業務ロールを持つかどうか */
	boolean kanriRole;
	/**	WFの業務ロールを持つかどうか */
	boolean wfRole;

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}

	/**
	 * E1:初期表示イベント
	 * @return 結果
	 */
	public String init(){

		//1.入力チェック
		//2.データ存在チェック
		//3.アクセス可能チェック
		//なし

		//4.処理
		User user = getUser();
		gyoumuRoleId = user.gyoumuRoleId;
		
		try(EteamConnection connection = EteamConnection.connect()){
			// 業務ロール機能制御から機能制御区分を取得
			keiriShoriRole =  EteamCommon.getAccessAuthorityLevel(connection, gyoumuRoleId, "KR").equals("KR");
			kanriRole = List.of("CO","SU").contains(EteamCommon.getAccessAuthorityLevel(connection, gyoumuRoleId, "CO"));
			wfRole =  EteamCommon.getAccessAuthorityLevel(connection, gyoumuRoleId, "WF").equals("WF");
		}
		//5.戻り値を返す
		return "success";
	}
}
