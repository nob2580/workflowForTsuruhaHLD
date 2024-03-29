package eteam.gyoumu.masterkanri;

public class MasterTorikomiExtBat extends MasterTorikomiBat {
	/**
	 * テーブルIDが債権債務DBからの取得かどうか
	 * @param etMasterId 諸届側テーブル名
	 * @return 債権債務DBである
	 */
	protected boolean tableIsSiken(String etMasterId) {
		return super.tableIsSiken(etMasterId)
				//▼振込先銀行Extを追加
			|| etMasterId.equals("torihikisaki_furikomisaki_ext");
	}
}
