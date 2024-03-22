package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;

/**
 * 会計バッチ
 * @author takahashi_ryousuke
 *
 */
public class KaikeiMainBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaikeiMainBat.class);

	/**
	 * バッチ処理メイン
	 * 終了コード 0:正常、1:エラー
	 * @param argv 0:スキーマ名
	 */
	public static void main(String[] argv) {

		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));
	
		//スキーマ指定
		if (1 != argv.length) {
			throw new IllegalArgumentException("パラメータにスキーマ名が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);
		
		KaikeiMainLogic lg = EteamContainer.getComponent(KaikeiMainLogic.class);

		//実行バッチリスト
		@SuppressWarnings("rawtypes")
		List<Class> batClassList = new ArrayList<>();
		batClassList.addAll(lg.getChuushutsuBatClassList());
		batClassList.add(ShiwakeDataImportBat.class);
		batClassList.add(FBDataCreateBat.class);
		batClassList.add(HizukeControlUpdateBat.class);
		
		//1つずつ実行、エラーが出ても続ける
		int ret = 0;
		for (@SuppressWarnings("rawtypes") Class batClass : batClassList) {
			EteamAbstractBat bat = EteamContainer.getComponent(batClass);
			int retTmp = bat.mainProc();
			if (0 != retTmp)
			{
				ret = 1;
			}
		}
	
		System.exit(ret);
	}
}
