package eteam.gyoumu.workflow;

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
import eteam.gyoumu.tsuuchi.DenpyouIchiranUpdateLogic;

/**
 * 伝票一覧テーブル用データ作成バッチ
 */


public class DenpyouIchiranTableCreateBat extends EteamAbstractBat {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(DenpyouIchiranTableCreateBat.class);
	
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
		DenpyouIchiranTableCreateBat bat = EteamContainer.getComponent(DenpyouIchiranTableCreateBat.class);
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "伝票一覧データ作成";
	}

	@Override
	public String getCountName() {
		return "作成データ数";
	}

	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {
			log.info("伝票一覧データ作成 開始");

			DenpyouIchiranUpdateLogic diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);

			//伝票IDリストを取得
			long totalCnt = diLogic.countAllDenpyouId();
			List<GMap> denpyouIdList = diLogic.loadAllDenpyouId();
			connection.rollback();

			//伝票ID単位で処理を繰り返す
			int count = 0;
			for(GMap mp : denpyouIdList) {
				count++;
				log.info("伝票一覧データ作成中 [" + count + "/" + totalCnt + "]");
				System.out.println("伝票一覧データ作成中 [" + count + "/" + totalCnt + "]");
				String denpyouId = (String) mp.get("denpyou_id");
				try{
					connection.beginTransaction();
					diLogic.deleteDenpyouIchiran(denpyouId);
					diLogic.insertDenpyouIchiran(denpyouId);
					diLogic.updateDenpyouIchiran(denpyouId);
					connection.commit();
				}catch(Exception e){
					connection.rollback();
					System.out.println("伝票一覧更新中エラー [" + denpyouId + "]");
					log.error("伝票一覧更新中エラー [" + denpyouId + "]", e);
				}
			}
			
			log.info("伝票一覧データ作成 終了");
			setCount(count);
			
			return 0;
			
		}catch(Throwable e){
			System.out.println("伝票一覧データ作成中 エラー発生");
			log.error("伝票一覧データ作成中 エラー発生" ,e);
			return 1;
		}
	}
}
