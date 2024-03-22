package eteam.gyoumu.kanitodoke;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamIO;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import lombok.Getter;
import lombok.Setter;

/**
 * 届出定義インポートバッチ
 */
public class KaniTodokeImportBat extends EteamAbstractBat {
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaniTodokeImportBat.class);
	
	/** インポート対象ファイル */
	protected String targetFilePath;
	/** kaniTodokeVersionテーブル情報 */
	protected List<GMap> kaniTodokeVersion;
	/** kaniTodokeKoumokuテーブル情報 */
	protected List<GMap> kaniTodokeKoumoku;
	/** kaniTodokeMasterテーブル情報 */
	protected List<GMap> kaniTodokeMaster;
	/** kaniTodokeCheckboxテーブル情報 */
	protected List<GMap> kaniTodokeCheckbox;
	/** kaniTodokeListKoテーブル情報 */
	protected List<GMap> kaniTodokeListKo;
	/** kaniTodokeListOyaテーブル情報 */
	protected List<GMap> kaniTodokeListOya;
	/** kaniTodokeTextテーブル情報 */
	protected List<GMap> kaniTodokeText;
	/** kaniTodokeTextareaテーブル情報 */
	protected List<GMap> kaniTodokeTextarea;
	/** denpyouShubetsuIchiranテーブル情報 */
	protected List<GMap> denpyouShubetsuIchiran;
	/** EteamConnection */
	protected EteamConnection connection;
	/** KaniTodokeLogic */
	protected KaniTodokeLogic kaniTodokeLogic;
	/** KaniTodokeCategoryLogic */
	protected KaniTodokeCategoryLogic kaniTodokeCategoryLogic;
	/** KaikeiCommonLogic */
	protected KaikeiCommonLogic commonLogic;
	
	
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic systemLogic;
	/** インポート失敗時表示用メッセージ */
	protected String errMsg = "";
	
	/**
	 * バッチ処理メイン
	 * @param argv 0:スキーマ名 argv 1:インポート対象ファイル(完全パス)
	 */
	public static void main(String[] argv) {
		// バッチ専用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		//スキーマ指定
		if (2 != argv.length) {
			System.err.println("パラメータにスキーマ名及びインポート対象ファイル(フォルダ)が指定されていません。");
			throw new IllegalArgumentException("パラメータにスキーマ名及びインポート対象ファイル(フォルダ)が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, argv[0]);

		//実行
		KaniTodokeImportBat bat = EteamContainer.getComponent(KaniTodokeImportBat.class);
		bat.targetFilePath = argv[1];
		System.exit(bat.mainProc());
	}

	@Override
	public String getBatchName() {
		return "届出定義インポート";
	}

	@Override
	public String getCountName() {
		return "届出定義インポート数";
	}
	
	
	/**
	 * エラー項目情報
	 * */
	@Getter @Setter
	protected class errorKoumoku
	{
		/**
		 * @param areaKbn エリア区分
		 * @param itemName 項目名
		 */
		errorKoumoku(String areaKbn, String itemName)
		{
			this.areaKbn = areaKbn;
			this.itemName = itemName;
		}
		/**
		 * エリア区分
		 */
		String areaKbn;
		/**
		 * 項目名
		 */
		String itemName;
	}
	
	@Override
	public int mainProc() {
		try {
			connection = EteamConnection.connect();
			kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, connection); 
			kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection); 
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection); 
			systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

			File fileOrFolder = new File(targetFilePath);
			if (!fileOrFolder.exists()) {
				throw new Exception("指定ファイル(フォルダ)「" + targetFilePath + "」は存在しません。");
	        }

			//処理対象ファイルをリストに入れる
			List<File> fileList = new ArrayList<>();
			if(fileOrFolder.isFile()) {
				if(!EteamIO.getExtension(fileOrFolder.getName()).toLowerCase().equals("json")) { //bat,bvsでJSON以外のファイル指定はエラーにしているはずだが一応
					throw new Exception("指定ファイル「" + targetFilePath + "」が定義ファイル(.json)ではありません。");
				}
				fileList.add(fileOrFolder);
			}else {
				for(File subFile : fileOrFolder.listFiles()) {
					if(EteamIO.getExtension(subFile.getName()).toLowerCase().equals("json")) {
						fileList.add(subFile);
					}
				}
				if(fileList.isEmpty()) {
					throw new Exception("指定フォルダ「" + targetFilePath + "」配下に定義ファイル(.json)が存在しません。");
				}
			}

			//処理対象ファイル単位
			for(File file : fileList) {
				// JSONデシリアライズ			
				String str = FileUtils.readFileToString(file, "utf-8");
				Gson gson = new Gson();
				HashMap<String, List<GMap>> importData = gson.fromJson(str, new TypeToken<HashMap<String, List<GMap>>>(){}.getType());
				// importDataの中身はKaniTodokeImportBat参照
				List<GMap> exportInfo = importData.get("export_info");
				kaniTodokeVersion = importData.get("kani_todoke_version");
				kaniTodokeKoumoku = importData.get("kani_todoke_koumoku");
				kaniTodokeMaster = importData.get("kani_todoke_master");
				kaniTodokeCheckbox = importData.get("kani_todoke_checkbox");
				kaniTodokeListKo = importData.get("kani_todoke_list_ko");
				kaniTodokeListOya = importData.get("kani_todoke_list_oya");
				kaniTodokeText = importData.get("kani_todoke_text");
				kaniTodokeTextarea = importData.get("kani_todoke_textarea");
				denpyouShubetsuIchiran = importData.get("denpyou_shubetsu_ichiran");
				
				//表示用エラーメッセージを作成
				//「定義ファイルとDBで届出ジェネレータのテーブルレイアウトが変更されている為、インポートできません。定義ファイル(yy.mm.dd.##)　DB(yy.mm.dd.##)」
				String pgVersionFrom = exportInfo.get(0).get("pg_version");
				pgVersionFrom = pgVersionFrom.substring(0, 2) + "." + pgVersionFrom.substring(2, 4) + "." + pgVersionFrom.substring(4, 6) + "." + pgVersionFrom.substring(6, 8);
				String dbVersion = systemLogic.findVersionYYMMDDXX();
				errMsg = "定義ファイルとDBで届出ジェネレータのテーブルレイアウトが変更されている為、「" + file.getName() + "」をインポートできません。定義ファイル(" + pgVersionFrom + ")　DB(" + dbVersion + ")";
	
				
				List<errorKoumoku> defaultClearList = masterCheck();
				if (defaultClearList.size() > 0)
				{
					defaultClear(defaultClearList);
				}
	
				touroku();
				setCount(super.getCount()+1);
			}

			connection.commit();
			System.out.println("インポートファイル件数：" + getCount());
			return 0;
		}catch(Throwable e){
			log.error("エラー発生" ,e);
			if(!StringUtils.isEmpty(e.getMessage())) {
				System.err.println(e.getMessage());
			}
			return 1;
		}finally{
			if (connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * リストに該当するkaniTodokeKoumokuのデフォルト値をクリアする
	 * @param defaultClearList デフォルト値クリア対象項目リスト
	 */
	protected void defaultClear(List<errorKoumoku> defaultClearList)
	{
		defaultClearList.forEach(d -> {
			kaniTodokeKoumoku.stream()
			.filter(k -> k.get("area_kbn").equals(d.areaKbn)
					&& k.get("item_name").equals(d.itemName))
			.map(x -> x.put("default_value1","")).toArray();
			
			// mapを連続で書けませんでした。。
			kaniTodokeKoumoku.stream()
			.filter(k -> k.get("area_kbn").equals(d.areaKbn)
					&& k.get("item_name").equals(d.itemName))
			.map(x -> x.put("default_value2","")).toArray();
		});
	}
	
	/**
	 * マスタチェック
	 * デフォルト値がマスタに存在しない場合defaultClearListに追加
	 * @return デフォルト値クリア対象項目リスト
	 */
	protected List<errorKoumoku> masterCheck()
	{
		List<GMap> masterCheckList = kaniTodokeKoumoku.stream()
				.filter(k -> k.get("buhin_type").equals("master")
						&& StringUtils.isNotEmpty(k.get("default_value1"))).collect(Collectors.toList());
		
		List<errorKoumoku> defaultClearList = new ArrayList<errorKoumoku>();
		
		// 項目定義のデフォルト値がマスタに存在するか確認
		for(GMap targetKoumoku :masterCheckList){
			
			ShiwakeCheckData shiwakeCheckData = commonLogic.new ShiwakeCheckData();
			List<String> errorList = new ArrayList<String>();
			errorKoumoku edaErrorKoumoku = null;
			
			GMap targetMaster = kaniTodokeMaster.stream()
			.filter(m -> m.get("area_kbn").equals(targetKoumoku.get("area_kbn"))
					&& m.get("item_name").equals(targetKoumoku.get("item_name")))
			.findFirst().orElse(null);
			switch (targetMaster.getString("master_kbn")) {
			case KaniTodokeConst.MasterKbn.BUMON:
				shiwakeCheckData.futanBumonNm = targetMaster.get("label_name");
				shiwakeCheckData.futanBumonCd = targetKoumoku.get("default_value1");
				commonLogic.checkShiwake(shiwakeCheckData, errorList);
				break;
			case KaniTodokeConst.MasterKbn.KAMOKU:
				shiwakeCheckData.kamokuNm = targetMaster.get("label_name");
				shiwakeCheckData.kamokuCd = targetKoumoku.get("default_value1");
				
				String edaLabelName = "";
				GMap edaKoumoku;
				if (targetKoumoku.get("yosan_shikkou_koumoku_id").equals(YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KAMOKU)) {

					// 収入科目の場合は対応する収入枝番を取得してチェック
					edaKoumoku = masterCheckList.stream()
							.filter(m -> m.get("area_kbn").equals(targetKoumoku.get("area_kbn"))
									&& m.get("yosan_shikkou_koumoku_id")
											.equals(YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_EDA))
							.findFirst().orElse(null);
					
					if (edaKoumoku != null)
					{
						shiwakeCheckData.kamokuEdabanCd = edaKoumoku.get("default_value1");
						
						edaLabelName = kaniTodokeMaster.stream()
								.filter(m -> m.get("area_kbn").equals(edaKoumoku.get("area_kbn"))
										&& m.get("item_name").equals(edaKoumoku.get("item_name")))
								.findFirst().orElse(null).get("label_name");
						// エラー用に情報を別だし
						edaErrorKoumoku = new errorKoumoku(edaKoumoku.get("area_kbn"), edaKoumoku.get("item_name"));
					}
				} else if (targetKoumoku.get("yosan_shikkou_koumoku_id").equals(YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU)) {
					// 支出科目の場合は対応する支出枝番を取得してチェック
					edaKoumoku = masterCheckList.stream()
							.filter(m -> m.get("area_kbn").equals(targetKoumoku.get("area_kbn"))
									&& m.get("yosan_shikkou_koumoku_id")
											.equals(YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA))
							.findFirst().orElse(null);
					
					if (edaKoumoku != null)
					{
						shiwakeCheckData.kamokuEdabanCd = edaKoumoku.get("default_value1");
						
						edaLabelName = kaniTodokeMaster.stream()
								.filter(m -> m.get("area_kbn").equals(edaKoumoku.get("area_kbn"))
										&& m.get("item_name").equals(edaKoumoku.get("item_name")))
								.findFirst().orElse(null).get("label_name");
						// エラー用に情報を別だし
						edaErrorKoumoku = new errorKoumoku(edaKoumoku.get("area_kbn"), edaKoumoku.get("item_name"));
					}
				}
				
				if (StringUtils.isNotEmpty(edaLabelName))
				{
					switch (targetKoumoku.getString("area_kbn")) {
					case AREA_KBN.SHINSEI:
						shiwakeCheckData.kamokuEdabanNm = "申請内容." + edaLabelName;
						break;
					case AREA_KBN.MEISAI:
						shiwakeCheckData.kamokuEdabanNm = "明細." + edaLabelName;
						break;
					}
				}
				
				commonLogic.checkShiwake(shiwakeCheckData, errorList);
				break;
			case KaniTodokeConst.MasterKbn.TORIHIKISAKI:
				shiwakeCheckData.torihikisakiNm = targetMaster.get("label_name");
				shiwakeCheckData.torihikisakiCd = targetKoumoku.get("default_value1");
				commonLogic.checkShiwake(shiwakeCheckData, errorList);
				break;
			case KaniTodokeConst.MasterKbn.UF1:
				shiwakeCheckData.uf1Nm = targetMaster.get("label_name");
				shiwakeCheckData.uf1Cd = targetKoumoku.get("default_value1");
				commonLogic.checkShiwake(shiwakeCheckData, errorList);
				break;
			case KaniTodokeConst.MasterKbn.UF2:
				shiwakeCheckData.uf2Nm = targetMaster.get("label_name");
				shiwakeCheckData.uf2Cd = targetKoumoku.get("default_value1");
				commonLogic.checkShiwake(shiwakeCheckData, errorList);
				break;
			case KaniTodokeConst.MasterKbn.UF3:
				shiwakeCheckData.uf3Nm = targetMaster.get("label_name");
				shiwakeCheckData.uf3Cd = targetKoumoku.get("default_value1");
				commonLogic.checkShiwake(shiwakeCheckData, errorList);
				break;
			default:
				break;
			}
			
			if (errorList.size() > 0)
			{
				// エラー有（マスタに該当コードなし)の場合、後処理用にリストに情報を設定する
				defaultClearList.add(new errorKoumoku(targetKoumoku.get("area_kbn"), targetKoumoku.get("item_name")));
				if (edaErrorKoumoku != null)
				{
					defaultClearList.add(edaErrorKoumoku);
				}
			}
		}
		
		return defaultClearList;
	}
	
	/**
	 * 登録処理
	 * 画面とはオブジェクトの構造が異なるため項目定義系は共通化していない
	 */
	protected void touroku()
	{
		String denpyouShubetsu = kaniTodokeVersion.get(0).get("denpyou_shubetsu");
		String naiyou = kaniTodokeVersion.get(0).get("naiyou");
		String gyoumuShubetsu = denpyouShubetsuIchiran.get(0).get("gyoumu_shubetsu");

		// 伝票区分
		String denpyouKbn = kaniTodokeCategoryLogic.getNewDenpyouKbn();

		// ユーザーID
		String userId = "batch";
		
		try {
			

			// ユーザー定義届書メタテーブルに登録
			kaniTodokeLogic.insertKaniTodokeMetaData(denpyouKbn, userId);
	
			// ユーザー定義届書バージョンテーブルに登録
			kaniTodokeLogic.insertKaniTodokeVersion(denpyouKbn, 1, denpyouShubetsu, naiyou, userId);
			
			for(GMap row :kaniTodokeKoumoku){
				// ユーザー定義届書項目定義登録
				connection.update("INSERT INTO kani_todoke_koumoku VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("hyouji_jun"), 
						row.get("buhin_type"),
						row.get("default_value1"),
						row.get("default_value2"),
						row.get("yosan_shikkou_koumoku_id"),
						userId, 
						userId); 
			}
	
			for(GMap row :kaniTodokeText){
				// ユーザー定義届書項目テキストに登録
				connection.update("INSERT INTO kani_todoke_text VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("label_name"), 
						row.get("hissu_flg"), 
						row.get("buhin_format"),
						row.get("buhin_width"), 
						row.get("max_length"), 
						row.get("decimal_point"), 
						row.get("kotei_hyouji"), 
						row.get("min_value"), 
						row.get("max_value"), 
						userId, 
						userId);
			}
			
			for(GMap row :kaniTodokeTextarea){
				// ユーザー定義届書項目テキストエリアに登録
				connection.update("INSERT INTO kani_todoke_textarea VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("label_name"), 
						row.get("hissu_flg"), 
						row.get("buhin_width"), 
						row.get("buhin_height"), 
						row.get("max_length"), 
						row.get("kotei_hyouji"), 
						userId, 
						userId);
			}
			
			for(GMap row : kaniTodokeCheckbox) {
				// ユーザー定義届書項目チェックボックスに登録
				connection.update("INSERT INTO kani_todoke_checkbox VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("label_name"), 
						row.get("hissu_flg"), 
						row.get("checkbox_label_name"), 
						userId, 
						userId);
			}
			
			for(GMap row: kaniTodokeMaster) {
				// ユーザー定義届書項目マスターに登録
				connection.update("INSERT INTO kani_todoke_master VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("label_name"), 
						row.get("hissu_flg"), 
						row.get("master_kbn"), 
						userId, 
						userId);
			}
			
			for(GMap row: kaniTodokeListOya) {
				// ユーザー定義届書項目リスト親(ラジオボタン／プルダウン)に登録
				connection.update("INSERT INTO kani_todoke_list_oya VALUES (?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("label_name"), 
						row.get("hissu_flg"), 
						userId, 
						userId);
			}
			
			for(GMap row: kaniTodokeListKo) {
				// ユーザー定義届書項目リスト子に登録
				connection.update("INSERT INTO kani_todoke_list_ko VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						1, 
						row.get("area_kbn"), 
						row.get("item_name"),
						row.get("hyouji_jun"),
						row.get("text"), 
						row.get("value"), 
						row.get("select_item"),
						userId, 
						userId);
			}
			
			// 伝票種別一覧に登録
			kaniTodokeLogic.insertDenpyouShubetsuIchiran(denpyouKbn, denpyouShubetsu, naiyou, userId);
			connection.update("UPDATE denpyou_shubetsu_ichiran SET gyoumu_shubetsu=? WHERE denpyou_kbn=?", gyoumuShubetsu, denpyouKbn);
		}catch(Exception e) {
			//DBバージョンに相違があるものとみなしテーブルレイアウト相違エラーメッセージ出力
			throw new RuntimeException(errMsg, e);
		}
	}
}
