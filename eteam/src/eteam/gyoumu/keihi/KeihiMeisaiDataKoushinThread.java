package eteam.gyoumu.keihi;

import java.util.Date;
import java.util.Map;

import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.gyoumu.masterkanri.MasterTorikomiTermBat;

/**
 * 経費明細データ更新のスレッドクラスです。
 */
public class KeihiMeisaiDataKoushinThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KeihiMeisaiDataKoushinThread.class);

	/** スキーマ名 */
	String schema;
	/** 対象日付(処理月の初日) */
	Date targetMonth;

    /**
     * コンストラクタ
     * @param schema スキーマ名
     * @param targetMonth 更新対象月
     */
	public KeihiMeisaiDataKoushinThread(String schema, Date targetMonth) {
		this.schema = schema;
		this.targetMonth = targetMonth;
    }

    /**
     * 各バッチ処理を実行する。
     */
    public void run() {
    	try {
    		//スキーマを起動元のコンテキストから渡されたものに
    		Map<String, String> threadMap = EteamThreadMap.get();
    		threadMap.put(SYSTEM_PROP.SCHEMA, schema);
    		
			// 経費明細データ取込バッチを呼び出す
    		MasterTorikomiTermBat bat = EteamContainer.getComponent(MasterTorikomiTermBat.class);
    		bat.setTargetMonth(targetMonth);
    		bat.mainProc();
    		
		} catch (Throwable e) {
			log.error("エラー発生", e);
		}
    }
}
