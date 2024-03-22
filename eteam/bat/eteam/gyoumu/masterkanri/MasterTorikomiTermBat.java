package eteam.gyoumu.masterkanri;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.SYSTEM_PROP;

/**
 * マスターの期間取込バッチ
 */
public class MasterTorikomiTermBat extends MasterTorikomiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(MasterTorikomiTermBat.class);

	@Override
	public String getBatchName() {
		return (getTargetMonth() == null) ? "経費明細データ更新" : "経費明細データ更新(" + DateFormatUtils.format(getTargetMonth(), "yyyy/MM") + ")";
	}

	@Override
	public String getCountName() {
		return "経費明細レコード数";
	}
	
	@Override
	protected int getProcType() {
		
		return 2;
	}

	/**
	 * バッチ処理メイン
	 * argv[1]があれば、argv[1]の指定月～当月までを取り込む。
	 * argv[1]がなければ、当月のみを取り込む。
	 * 
	 * argv[0]: スキーマ名
	 * argv[1]: 指定月(yyyyMM形式)
	 * @param argv バッチ実行引数
	 */
	public static void main(String[] argv) {
		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		//スキーマ指定
		if (1 > argv.length) {
			throw new IllegalArgumentException("パラメータにスキーマ名が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);

		MasterTorikomiTermBat bat = EteamContainer.getComponent(MasterTorikomiTermBat.class);
		
		//処理月範囲の決定 ※2016/04/01～2016/10/01みたいな月初日の範囲指定
		List<Date> monthList;
		try {
			//指定月以降
			if (argv.length >= 2) {
				Date fromMonth = DateUtils.parseDate(argv[1] + "01", "yyyyMMdd");
				monthList = EteamCommon.createMonthList(fromMonth);
				//現在日付が月締日以降の場合を考慮
				monthList.add(new Date(System.currentTimeMillis()));
			//引数なければ当月
			} else {
				monthList = new ArrayList<>();
				monthList.add(new Date(System.currentTimeMillis()));
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		//月単位に処理.mainProc内で日単位に処理して月単位コミットする。
		for (Date month : monthList) {
			bat.setTargetMonth(month);
			int ret = bat.mainProc();
			if (0 != ret) {
				System.exit(ret);
			}
		}
		System.exit(0);
	}
}
