package eteam.gyoumu.kaikei;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;

/**
 * 日付コントロール更新バッチ
 */
public class HizukeControlUpdateBat extends EteamAbstractBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(HizukeControlUpdateBat.class);

	@Override
	public String getBatchName() {
		return "日付コントロール更新";
	}

	@Override
	public String getCountName() {
		return "日付コントロール";
	}

	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {

			HizukeControlUpdateLogic lg = EteamContainer.getComponent(HizukeControlUpdateLogic.class, connection);
			lg.updateToujitsuKbnFlgOff();
			int count = lg.updateToujitsuKbnFlgOn();

			connection.commit();
			setCount(count);
			return 0;
		}catch (Throwable e) {
			log.error("エラー発生", e);
			return 1;
		}
	}
}
