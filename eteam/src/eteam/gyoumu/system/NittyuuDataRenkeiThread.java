package eteam.gyoumu.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.RegAccess;
import eteam.gyoumu.kaikei.ShiwakeDataImportBat;

/**
 * 日中データ連携バッチ処理のスレッドクラスです。
 */
public class NittyuuDataRenkeiThread extends Thread {

	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);

    /** 仕訳データ一覧連携バッチ一覧 */
	List<Class<EteamAbstractBat>> swkBatClassList;
    /** マスターデータ一覧連携バッチ一覧 */
	List<Class<EteamAbstractBat>> mstBatClassiList;
	/** スキーマ名 */
	String schema;

    /**
     * コンストラクタ
     * @param swkBatchArr 仕訳データ連携バッチ一覧
     * @param mstBatchArr マスタデータ連携バッチ一覧
     * @param schema      スキーマ名
     */
	public NittyuuDataRenkeiThread(String[] swkBatchArr, String[] mstBatchArr, String schema) {
		swkBatClassList = str2Cls(swkBatchArr);
		mstBatClassiList = str2Cls(mstBatchArr);
		this.schema = schema;
    }

    /**
     * 各バッチ処理を実行する。
     */
    public void run() {
    	try {
    		//スキーマを起動元のコンテキストから渡されたものに
    		Map<String, String> threadMap = EteamThreadMap.get();
    		threadMap.put(SYSTEM_PROP.SCHEMA, schema);
    		
			// 伝票種別単位の伝票抽出バッチ実行
			for (Class<EteamAbstractBat> batClass : swkBatClassList) {
				EteamAbstractBat bat = EteamContainer.getComponent(batClass);
				bat.mainProc();
			}
	
			// 仕訳インポートバッチ実行
			if(RegAccess.checkEnableKeihiSeisan()) {
				ShiwakeDataImportBat impBat = EteamContainer.getComponent(ShiwakeDataImportBat.class);
				impBat.mainProc(); 
			}
	
			// マスターデータ一覧連携処理
			for (Class<EteamAbstractBat> batClass : mstBatClassiList) {
				EteamAbstractBat bat = EteamContainer.getComponent(batClass);
				bat.mainProc(); 
			}
		} catch (Throwable e) {
			log.error("エラー発生", e);
		}
    }

    /**
     * クラス名をクラスに変換
     * @param classNameArr クラス名の配列
     * @return クラスの配列
     */
    @SuppressWarnings("unchecked")
	protected List<Class<EteamAbstractBat>> str2Cls(String[] classNameArr) {
    	List<Class<EteamAbstractBat>> ret = new ArrayList<Class<EteamAbstractBat>>();
    	if (null != classNameArr)
		{
			for (int i = 0; i < classNameArr.length; i++) {
	    		try {
	    			//振替伝票（拠点入力）は最終承認時に仕訳データ抽出済みなので除外します
	    			if(classNameArr[i].indexOf("BumonFurikaeChuushutsuBat") < 0 ) {
	    				ret.add((Class<EteamAbstractBat>)Class.forName(classNameArr[i]));
	    			}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("バッチの型ではない", e);
				}
	    	}
		}
    	return ret;
    }
}
