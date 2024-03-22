package eteam.gyoumu.menu;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.GamenKengenSeigyoLogic;
import eteam.gyoumu.user.User;

/**
 *メニューに関するロジック
 */
public class MenuLogic extends EteamAbstractLogic {

	/** 画面権限制御ロジック */
	protected GamenKengenSeigyoLogic kengenLogic;
	
	/**
	 * 初期化
	 * @param  _connection コネクト
	 */
	public void init(EteamConnection _connection) {
		super.init(_connection);
		kengenLogic = EteamContainer.getComponent(GamenKengenSeigyoLogic.class, _connection);
	}
	
	/**
	 * メニュー単位で表示フラグを設定する。
	 * @param userInfo セッション・ユーザー
	 * @param gamenId 画面ID
	 * @return 表示フラグ
	 */
	public boolean setHyoujiFlg(User userInfo, String gamenId) {
		boolean hyoujiFlg = false;
		if(kengenLogic.judgeAccess(gamenId, userInfo)){
			/* 機能制御が設定なし で 画面権限制御が有効 であれば表示フラグ「有効」 */
			hyoujiFlg = true;
		}
		return hyoujiFlg;
	}

	/**
	 * メニュー表示判定（拡張用） 
	 * @param user セッション・ユーザー
	 * @param action メニューアクション
	 * @return 判定結果
	 */
	public GMap judgeExt(User user, MenuAction action) {
		return null;
	}
}
