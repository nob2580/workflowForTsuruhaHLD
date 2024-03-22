package eteam.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 駅すぱあとイントラ版Action
 */
@Getter @Setter @ToString
public class EteamEkispertIntraAction extends EteamEkispertCommon {

	/** 使用期間区分 */
	protected String kikanKbn;
	/** 経路 */
	protected String route;
	/** 金額 */
	protected String money;
	/** 距離 */
	protected String distance;
	/** 定期控除フラグ */
	protected String val_tassign_status;
	/** 定期情報使用可否 */
	protected String val_teiki_available;
	/** 方向性をもった経路文字列(定期用情報) */
	protected String val_restoreroute;
	/** 増税非対応 */
	protected String taxNonSupported; //0:対応済　1：改定前の金額、2：見込の金額

	//20220218 追加
	/** 割引名称  */
	protected String discountName;

	/**
	 * 駅すぱあとイントラ版から渡された値をダミー画面に設定する
	 * @return 処理結果
	 */
	public String dummyInit() {

		// 経路探索処理
		EkispertResultSet eRet = super.searchResultSet(kikanKbn);

		//親クラスと子クラスの分け方が微妙・・・
		//駅名検索とかもここを通るのだけど、super.searchResultSetの中でsuper.fromとか、super.to01とかに値がセットされるのだが、
		//ルート情報とかだけは、なぜかこっちでセットしたりする。
		if (eRet != null) {
			route = eRet.route;
			money = eRet.kind;
			distance = eRet.distance;
			val_tassign_status = eRet.assignStatus;
			val_teiki_available = eRet.teikiAvailable;
			val_restoreroute = eRet.restoreRoute;
			taxNonSupported = Integer.toString(eRet.taxNonSupported);
			//20220224 追加
			discountName = eRet.discountName;
		}
		//5.戻り値を返す
		return "success";
	}
}
