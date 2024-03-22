package eteam.gyoumu.kanitodoke;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.Gson;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamFileLogic;
import eteam.common.select.KaniTodokeCategoryLogic;

/**
 * 届出定義エクスポートバッチ
 */
public class KaniTodokeExportBat extends EteamAbstractBat {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaniTodokeExportBat.class);
	
	/** 伝票区分 */
	String denpyouKbn;
	
	/**
	 * バッチ処理メイン
	 * @param argv 0:スキーマ名 1:伝票区分
	 */
	public static void main(String[] argv) {
		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		//スキーマ指定
		if (2 != argv.length) {
			throw new IllegalArgumentException("パラメータにスキーマ名及び伝票区分が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);

		//実行
		KaniTodokeExportBat bat = EteamContainer.getComponent(KaniTodokeExportBat.class);
		bat.denpyouKbn = argv[1];
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "届出定義エクスポート";
	}

	@Override
	public String getCountName() {
		return "届出定義エクスポート数";
	}

	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {
			KaniTodokeCategoryLogic kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection); 
			
			// エクスポート情報を設定
	        List<GMap> exportInfo = new ArrayList<GMap>();

			Properties prop = new Properties();
			prop.load(EteamConnection.class.getResourceAsStream("/eteam.properties"));
			String pgVersion = prop.getProperty("version");
			
			// ファイルに出力時の情報を待たせる
	        GMap map = new GMap();
	        map.put("pg_version", pgVersion);
	        exportInfo.add(map);
	        
	        // フォルダ作成
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestr = fmt.format(new Date(System.currentTimeMillis())); 
			String folder = setting.op21MparamCsvPath() + "/" + getBatchName() + timestr; //c:\eteam\tmp\届出定義エクスポート_yyyyMMddHHmmss";
			new File(folder).mkdirs();

			// c:\eteam\tmpは仕訳連携周りで消される？
			
			List<GMap> kaniTodokeList = kaniTodokeCategoryLogic.loadKaniTodokeIchiran();
			if (!this.denpyouKbn.equals("ALL")) {
				kaniTodokeList  = kaniTodokeList.stream().filter(s -> s.get("denpyou_kbn").equals(this.denpyouKbn)).collect(Collectors.toList());
				
				if (kaniTodokeList.isEmpty())
				{
					throw new Exception(this.denpyouKbn + "に該当する届出定義が存在しません");
				}
			}
			
			// 伝票種別単位で出力
			for(GMap kaniTodoke :kaniTodokeList){
				String targetKbn = kaniTodoke.get("denpyou_kbn");
				Integer version = kaniTodoke.get("version");

				var OutData = new HashMap<String, List<GMap>>();
		        List<GMap> kaniTodokeMeta = connection.load("select * from kani_todoke_meta where denpyou_kbn = ?", targetKbn);
				List<GMap> kaniTodokeVersion = connection.load("select * from kani_todoke_version where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeKoumoku = connection.load("select * from kani_todoke_koumoku where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeMaster = connection.load("select * from kani_todoke_master where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeCheckbox = connection.load("select * from kani_todoke_checkbox where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeListKo = connection.load("select * from kani_todoke_list_ko where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeListOya = connection.load("select * from kani_todoke_list_oya where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeText = connection.load("select * from kani_todoke_text where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> kaniTodokeTextarea = connection.load("select * from kani_todoke_textarea where denpyou_kbn = ? and version = ?", targetKbn, version);
				List<GMap> denpyouShubetsuIchiran = connection.load("select * from denpyou_shubetsu_ichiran where denpyou_kbn = ? and version = ?", targetKbn, version);

				OutData.put("export_info", exportInfo);
				OutData.put("kani_todoke_meta", kaniTodokeMeta);
				OutData.put("kani_todoke_version", kaniTodokeVersion);
				OutData.put("kani_todoke_koumoku", kaniTodokeKoumoku);
				OutData.put("kani_todoke_master", kaniTodokeMaster);
				OutData.put("kani_todoke_checkbox", kaniTodokeCheckbox);
				OutData.put("kani_todoke_list_ko", kaniTodokeListKo);
				OutData.put("kani_todoke_list_oya", kaniTodokeListOya);
				OutData.put("kani_todoke_text", kaniTodokeText);
				OutData.put("kani_todoke_textarea", kaniTodokeTextarea);
				OutData.put("denpyou_shubetsu_ichiran", denpyouShubetsuIchiran);
				
				// 出力
				Gson gson = new Gson();
			    String json = gson.toJson(OutData);  
			    
				String denpyouShubetsu = EteamFileLogic.trimNGFileNamePattern(kaniTodoke.get("denpyou_shubetsu"));  
			    String filePath = folder + "/" + targetKbn + "_" + denpyouShubetsu + ".json";
				FileUtils.writeStringToFile(new File(filePath), json , "utf-8", false);
			}
			
			setCount((int)kaniTodokeList.stream().count());
			return 0;
		}catch(Throwable e){
			log.error("エラー発生" ,e);
			return 1;
		}
	}
}
