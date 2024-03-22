package eteam.gyoumu.shiharai;

import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.gyoumu.kaikei.FBDataCreateBat;

/**
 * FBデータ作成バッチ
 * @author takahashi_ryousuke
 *
 */
public class FBDataCreateShiharaiBat extends FBDataCreateBat {

//＜メンバ変数(実行モード指定)＞
	/** 作成済含める */
	public boolean all;

//＜処理＞
	@Override
	public String getBatchName() {
		return "FBデータ作成（支払依頼）";
	}

	@Override
	public String getCountName() {
		return "FBデータファイル（支払依頼）";
	}

	@Override
	protected void afterMainExt(EteamConnection connection, List<GMap> dataList) {
		for (GMap record : dataList) {
			connection.update("UPDATE shiharai_irai SET fb_made_flg = '1' WHERE denpyou_id = ?", (String)record.get("denpyou_id"));
		}
	}

	/**
	 * 部品初期化
	 * @param connection コネクション
	 */
	@Override
	protected void init(EteamConnection connection) {
		super.init(connection);
		fbdcl = EteamContainer.getComponent(FBDataCreateShiharaiLogic.class, connection);
		((FBDataCreateShiharaiLogic)fbdcl).all = this.all;
	}
}
