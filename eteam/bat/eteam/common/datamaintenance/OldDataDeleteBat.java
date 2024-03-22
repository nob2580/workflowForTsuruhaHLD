package eteam.common.datamaintenance;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamSettingInfo.Key;

/**
 * 過去データ消去
 *
 */

public class OldDataDeleteBat extends EteamAbstractBat {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(OldDataDeleteBat.class);
	/** 保存日数(WF一般) */
	protected String hozon;
	/** 保存日数(ログ) */
	protected String hozonLog;
	
	/**
	 * バッチ処理メイン
	 * @param argv 0:スキーマ名(なければデフォルトスキーマ)、1:保存日数(なければ会社設定の値、会社設定の値がブランクなら削除処理せず終わる)
	 */
	public static void main(String[] argv) {
		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		//スキーマ指定
		if (argv.length == 0) {
			throw new IllegalArgumentException("パラメータにスキーマ名が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);
		
		//日数指定
		String hozon = "";
		String hozonLog = "";
		if (argv.length >= 2) {
			hozon = argv[1];
			hozonLog = argv[1];
		}
		
		//実行
		OldDataDeleteBat bat = EteamContainer.getComponent(OldDataDeleteBat.class);
		bat.hozon = hozon;
		bat.hozonLog = hozonLog;
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "過去データ消去";
	}

	@Override
	public String getCountName() {
		return "削除レコード数";
	}

	@Override
	public int mainProc() {
		
		//保存日数指定が無ければ会社設定から読み取る
		if (StringUtils.isEmpty(hozon)) {
			hozon = setting.dataHozonNissuu();
		}
		if (StringUtils.isEmpty(hozonLog)) {
			hozonLog = setting.dataHozonNissuuLog();
		}
		
		//会社設定が空なので何もせず終わる
		if (StringUtils.isEmpty(hozon) && StringUtils.isEmpty(hozonLog)) {
			log.info("データ保存日数が空なので何もせず終了します。");
			setCount(0);
			return 0; 
		}
		
		//以下保存日数ありの時
		try(EteamConnection connection = EteamConnection.connect()) {
			OldDataDeleteLogic oddl = EteamContainer.getComponent(OldDataDeleteLogic.class, connection);

			Date kijunDate;
			List<Map<String, String>> deleteList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> tmpList;
			int deletedRecordCount = 0;
			
			//テーブル単位にDELETE文流す(WF一般)
			if (!StringUtils.isEmpty(hozon)) {
				kijunDate = nissuuKeisan(Integer.parseInt(hozon));
				tmpList = oddl.updateOldDelete(kijunDate,Key.DATA_HOZON_NISSUU);
				for(Map<String,String> mp : tmpList) {deleteList.add(mp);};
			}
			
			//テーブル単位にDELETE文流す(ログ)
			if (!StringUtils.isEmpty(hozonLog)) {
				kijunDate = nissuuKeisan(Integer.parseInt(hozonLog));
				tmpList = oddl.updateOldDelete(kijunDate,Key.DATA_HOZON_NISSUU_LOG);
				for(Map<String,String> mp : tmpList) {deleteList.add(mp);};
			}
			
			//削除結果のログ出力＆総削除件数カウント
			for(Map<String, String> map : deleteList){
				String masterId = String.valueOf(map.get("master_id"));
				int count = Integer.parseInt(map.get("count"));
				log.info(masterId + "の処理件数は" + count + "件です。");
				deletedRecordCount += count;
			}
			
			connection.commit();
			setCount(deletedRecordCount);
			return 0; 
		}catch(Throwable e){
			log.error("エラー発生", e);
			return 1;
		}
	}
	
	/**
	 * システム日付から保存日数分を減算した日付を取得
	 * @param hozonNissuu 保存日数
	 * @return 計算結果
	 */
	public Date nissuuKeisan(int hozonNissuu) {
		//システム日付取得
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		
		//システム日付から保存日数を引く
		cal.add(Calendar.DATE, - hozonNissuu);
		
		//yyyy-mm-ddの形でシステム日付を取得
		return new Date(cal.getTimeInMillis());
	}
}
