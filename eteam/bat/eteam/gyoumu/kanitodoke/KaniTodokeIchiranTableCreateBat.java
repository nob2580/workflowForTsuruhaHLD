package eteam.gyoumu.kanitodoke;

import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;

/**
 * ユーザー定義届書一覧テーブル用データ作成バッチ
 *
 */
public class KaniTodokeIchiranTableCreateBat extends EteamAbstractBat {

	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaniTodokeIchiranTableCreateBat.class);
	
	/**
	 * バッチ処理メイン
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

		//実行
		KaniTodokeIchiranTableCreateBat bat = EteamContainer.getComponent(KaniTodokeIchiranTableCreateBat.class);
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "ユーザー定義届書一覧データ作成";
	}

	@Override
	public String getCountName() {
		return "作成データ数";
	}

	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {
			String outStr = "ユーザー定義届書一覧データ作成 開始";
			log.info(outStr);

			KaniTodokeIchiranUpdateLogic kiLogic = EteamContainer.getComponent(KaniTodokeIchiranUpdateLogic.class, connection);

			//ユーザー定義届書IDリストを取得
			long totalCnt = kiLogic.countAllDenpyouId();
			List<GMap> denpyouIdList = kiLogic.loadAllDenpyouId();

			//ユーザー定義届書ID単位で処理を繰り返す
			int count = 0;
			for(GMap mp : denpyouIdList) {
				count++;
				outStr = "ユーザー定義届書一覧データ作成中 [" + count + "/" + totalCnt + "]";
				System.out.println(outStr);
				log.info(outStr);
				
				String denpyouId = (String) mp.get("denpyou_id");
				try{
					kiLogic.deleteKaniTodokeIchiran(denpyouId);
					kiLogic.insertKaniTodokeIchiran(denpyouId);
					kiLogic.updateKaniTodokeIchiran(denpyouId);
					kiLogic.deleteKaniTodokeMeisaiIchiran(denpyouId);
					kiLogic.insertKaniTodokeMeisaiIchiran(denpyouId);
					kiLogic.updateKaniTodokeMeisaiIchiran(denpyouId);
				}catch(Exception e){
					System.out.println("ユーザー定義届書一覧更新中エラー [" + denpyouId + "]");
					log.error("ユーザー定義届書一覧更新中エラー [" + denpyouId + "]", e);
				}
			}
			
			connection.commit();
			outStr = "ユーザー定義届書一覧データ作成 終了";
			log.info(outStr);
			setCount(count);
			
			return 0;
			
		}catch(Throwable e){
			System.out.println("ユーザー定義届書一覧データ作成中 エラー発生");
			log.error("ユーザー定義届書一覧データ作成中 エラー発生" ,e);
			return 1;
		}
	}
}
