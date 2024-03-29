package eteam.gyoumu.kaikei.open21;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamLogger;
import eteam.base.intercepter.EteamActionIntercepter;
import eteam.common.EteamCommon;
import eteam.common.EteamFileLogic;
import eteam.common.EteamSettingInfo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;

/**
 * Open21へのインポートを行うクラス
 */
public class Open21ShiwakeImportLogic {
	
	/** e文書データ格納CSV名前半部 */
	protected static final String IMP_LINKDATA_CSV = "linkData_";
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamActionIntercepter.class);
	
	/** システムエラー */
	protected static final int SYSTEM_ERROR   = -999;
	
	/** 仕訳データ導入の個別エラー番号LIST */
	public static final String[][] SHIWAKE_ERROR_MSG_LIST = {
		//エラー番号		エラー内容
		 { "1",  "以上処理件数（不良データなし）", },
		 { "0",  "不良データあり", },
		 {"-1",  "会社コード不正", },
		 {"-2",  "処理区分不正", },
		 {"-3",  "伝票形式不正", },
		 {"-4",  "ログ出力フラグ不正", },
		 {"-5",  "インポートファイルパス不正", },
		 {"-6",  "インポートファイル名不正", },
		 {"-7",  "ログファイルパス不正", },
		 {"-8",  "ログファイル名不正", },
		 {"-9",  "レイアウト№不正", },
		 {"-10", "入力確定日不正※部署入出力(拡張入出力)への仕訳書き込み時のみ。", },
		 {"-11", "インポートファイルが存在しない", },
		 {"-12", "会社が存在しない", },
		 {"-13", "伝票入力レイアウトパターンが不正", },
		 {"-14", "邦貨換算フラグ不正", },
		 {"-15", "起動ユーザー不正", },
		 {"-16", "起動ユーザーは指定会社を使用できません。", },
		 {"-17", "仕訳区分不正", },
		 {"-21", "インポートファイルオープンエラー", },
		 {"-22", "インポートファイル読込みエラー", },
		 {"-23", "ログファイルオープンエラー", },
		 {"-24", "ログファイル書込みエラー", },
		 
// Ver22.05.31.01 エラー番号抜け追加 *-
		 {"-30", "ロックエラー", },
// -*
		 
		 {"-31", "データベース接続エラー", },
		 {"-32",  "データベース読込みエラー", },
		 {"-33",  "データベース書込みエラー", },
		 {"-34",  "外貨システム整合性エラー", },
		 {"-35",  "データベース書込みエラー(起動履歴)", },
		 {"-41",  "インポートデータは入力不可月に該当している、または存在しない科目コード※データチェックをせずにいきなりデータ取り込みを行った時に発生する可能性があります。データチェックをかければ、不良データとなります。", },
		 {"-42",  "同一月内に同じ伝票番号が存在する。", },
		 {"-43",  "基本財務にインポート処理する場合で自動付番を使用する場合に付番する伝票番号（伝票番号開始番号登録テーブルより開始番号を取得）が重複していた場合のエラーです。", },
		 {"-44",  "未登録の伝票入力レイアウトパターンが指定されています。", },
		 {"-45",  "インポート対象データ又はインポート可能データ（不良仕訳を除いたデータ）で貸借諸口金額がアンマッチです。", },
		 {"-50",  "伝票番号重複チェックエラー　※内部統制強化ﾓｰﾄﾞ＆自動付番を使用する場合のみ", },
		 {"-51",  "伝票番号重複チェックエラー　※内部統制強化ﾓｰﾄﾞ＆自動付番を使用しない場合のみ", },
		 {"-52",  "伝票日付のチェック エラー　", },
		 {"-53",  "受付番号が登録不能な値の場合のエラー　", },
		 {"-54",  "伝票番号が登録不能な値の場合エラー　 ※自動付番を使用する場合のみ", },
		 {"-55",  "受付番号重複エラー", },
		 {"-56",  "リンク情報エラー", },
		 {"-99",  "その他エラー", },
		// 以下、インポータEXEからのエラーコード
		 {"-100",  "処理モードエラー(1：IMP_SUBSIWAKE_M、2：IMP_SUBSIWAKE_K_M以外の数値の場合、エラー)", },
		 {"-101",  "起動引数エラー(起動引数の個数が15個ではない場合、エラー)", },
		 {"-102",  "引数変換エラー(起動引数を数値に変換できない場合、エラー)", },
		 {"-103",  "CSVファイル存在エラー(CSVファイルが指定パスに存在しない場合、エラー)", },
		 {"-104",  "CSVファイル読み込みエラー(CSVファイルの読み込みができない場合、エラー)", },
		 {"-105",  "データなしエラー(CSVファイルのレコード件数が０件の場合、エラー)", },
		 {"-106",  "データ個数エラー(CSVファイルの項目数がOPEN21バージョンによる指定と一致していない場合、エラー)", },
		 {"-107",  "データ変換エラー(CSVファイルデータを数値に変換できない場合、エラー)", },
		 {"-108",  "呼び出しインポータ相違エラー(OPEN21バージョンと相違するインポータexeを呼び出した場合、エラー)", },
		 {"-109",  "伝票添付ファイル存在エラー(伝票添付ファイルが存在しない場合、エラー)", },
		 {"-110",  "リンク情報CSVファイル存在エラー(リンク情報CSVファイルが存在しない場合、エラー)", },
		 {"-999",  "システム例外", },
	};
	
	/**
	 * Imp_Shiwake_Mを呼び出し仕訳データをOpen21へインポートする。
	 * @param data 仕訳データ。詳細はバッチ仕様書（仕訳データインポート）。
	 * @return 仕訳の処理結果 1以上=正常。それ以外は失敗。エラーコードの意味はバッチ仕様書（仕訳データインポート）。
	 */
	public static int importShiwakeM(Open21ShiwakeData data) {
		log.info("OPEN21のメソッド（Imp_Siwake_M）を開始します。");
		
		int mode = 1;
		int result = importerCall(mode, data.CCOD, data.PrcFlg, data.DFUK, data.LogFlg, data.Siwake, 
				data.LogPath, data.LogFname, data.Rno, data.RUCOD, data.SKUBUN, data.IJPT, data.Kanzan, data.Kakutei, data.Keigen, data.getLink());;

		log.info("OPEN21のメソッド（Imp_Siwake_M）の戻りコード：" + result);
		return result;
	}
	
	/**
	 * Imp_Shiwake_K_Mを呼び出し仕訳データをOpen21へインポートする。
	 * @param data 仕訳データ。詳細はバッチ仕様書（仕訳データインポート）。
	 * @return 仕訳の処理結果 1以上=正常。それ以外は失敗。エラーコードの意味はバッチ仕様書（仕訳データインポート）。
	 */
	public static int importShiwakeKM(Open21ShiwakeData data) {
		log.info("OPEN21のメソッド（Imp_Siwake_KM）を開始します。");
		
		int mode = 2;
		int result = importerCall(mode, data.CCOD, data.PrcFlg, data.DFUK, data.LogFlg, data.Siwake, 
				data.LogPath, data.LogFname, data.Rno, data.RUCOD, data.SKUBUN, data.IJPT, data.Kanzan, data.Kakutei,data.Keigen, data.getLink());
		
		log.info("OPEN21のメソッド（Imp_Siwake_KM）の戻りコード：" + result);
		return result;
	}
	
	/**
	 * エラーコードによって、メッセージを取得。
	 * @param errCode エラーコード
	 * @return エラーメッセージ
	 */
	public static String getErrorMessage(int errCode) {
		String errMessage = "";
		for (String[] errArr: SHIWAKE_ERROR_MSG_LIST) {
			if (!errArr[0].equals(String.valueOf(errCode))) {
				continue;
			}
			errMessage = "エラー発生！エラー番号：" + errArr[0] + ",　エラー内容：" + errArr[1];
		}
		if (StringUtils.isEmpty(errMessage)) {
			errMessage = "エラー発生！エラー番号：" + errCode + ",　エラー番号は存在しなかった。";
		}
		return errMessage;
	}
	
	/**
	 * インポート処理
	 * @param mode      外貨対応フラグ [1:非対応(Imp_Siwake_M)][2:対応(Imp_Siwake_K_M)]
	 * @param CCOD      会社コード      
	 * @param PrcFlg    処理区分        
	 * @param DFUK      伝票形式        
	 * @param LogFlg    不良データログ  
	 * @param Siwake    パス            
	 * @param LogPath   ファイル名      
	 * @param LogFname  ログ用パス      
	 * @param RNo       ログファイル名  
	 * @param RUCOD     レイアウトNo    
	 * @param SKUBUN    起動ユーザー    
	 * @param IJPT      伝票入力パターン
	 * @param Kanzan    邦貨換算フラグ  
	 * @param Kakutei   入力確定日      
	 * @param Keigen    税率の扱い
	 * @param linkList  リンク情報リスト
	 * @return 処理結果 
	 */
	protected static int importerCall(int mode, String CCOD, int PrcFlg, int DFUK, int LogFlg, List<Open21CSetData> Siwake, 
			String LogPath, String LogFname, int RNo, int RUCOD, int SKUBUN, int IJPT, int Kanzan, String Kakutei, int Keigen, List<Open21LinkData> linkList) {
		
		String Path  = EteamSettingInfo.getSettingInfo("op21mparam_csv_path") + "\\" + EteamCommon.getContextSchemaName();
		String Fname = EteamCommon.getContextSchemaName() + "_" + EteamSettingInfo.getSettingInfo("op21mparam_csv_file_nm");
		
		File tempFile = new File(Path, Fname);
		try {
			//CSVファイルを生成
			if(Open21Env.getVersion() == Version.DE3){
				fileWriter_de3(tempFile, Siwake,RNo);
			}else if(Open21Env.getVersion() == Version.SIAS){
				fileWriter_SIAS(tempFile, Siwake,RNo);
				fileWriter_Link(linkList);
			}else{
				throw new RuntimeException("OPEN21 ENV NOT FOUND");
			}
			
			StringBuilder command;
			if(Open21Env.getVersion() == Version.DE3){
				command = new StringBuilder("C:\\eteam\\bat\\bin\\de3\\Importer.exe ");  // コマンド（de3版実行文）
			}else if(Open21Env.getVersion() == Version.SIAS){
				command = new StringBuilder("C:\\eteam\\bat\\bin\\sias\\Importer.exe ");  // コマンド（SIAS(SQLServer・PostgreSQL共通)版実行文）
			}else{
				log.error("Importer.exe読み込みエラーが発生した為、処理を終了します。");
				return SYSTEM_ERROR;
			}
			command.append(mode).append(" ");      // mode     外貨対応フラグ [1:非対応(Imp_Siwake_M)][2:対応(Imp_Siwake_K_M)]
			command.append(CCOD).append(" ");      // CCOD     会社コード      
			command.append(PrcFlg).append(" ");    // PrcFlg   処理区分        
			command.append(DFUK).append(" ");      // DFUK     伝票形式        
			command.append(LogFlg).append(" ");    // LogFlg   不良データログ  
			command.append(Path).append(" ");      // Path     パス            
			command.append(Fname).append(" ");     // Fname    ファイル名      
			command.append(LogPath).append(" ");   // LogPath  ログ用パス      
			command.append(LogFname).append(" ");  // LogFname ログファイル名  
			command.append(RNo).append(" ");       // Rno      レイアウトNo    
			command.append(RUCOD).append(" ");     // RUCOD    起動ユーザー    
			command.append(SKUBUN).append(" ");;   // SKUBUN   仕分け区分      
			command.append(IJPT).append(" ");      // IJPT     伝票入力パターン
			command.append(Kanzan).append(" ");    // Kanzan   邦貨換算フラグ  
			command.append(Kakutei).append(" ");   // Kakutei  入力確定日      
			command.append(Keigen).append(" ");    // Keigen   税率の扱い
			
			log.debug("★★★実行コマンド" + command.toString());

			// コマンドを実行しインポートEXEを起動します。
			if (! Open21Env.readIsUt()) {
				Process process = Runtime.getRuntime().exec(command.toString());
				return process.waitFor();
			} else {
				log.info("UTモードなので仕訳インポートしない");
				return 1;
			} 
		} catch (IOException e) {
			log.error("プロセスが実行できませんでした。", e);
			return SYSTEM_ERROR;
		} catch (InterruptedException e) {
			log.error("プロセスが中断されました。", e);
			return SYSTEM_ERROR;
		}
	}
	
	/**
	 * インポート用のCSVファイルへ仕分けデータを書き込みます。
	 * @param file    ファイル
	 * @param Siwake  仕分けデータ
	 */
	protected static void fileWriter_de3(File file, List<Open21CSetData> Siwake, int rNo) {
		StringBuilder sb = new StringBuilder();
		
		for(Open21CSetData data : Siwake) {
			sb.append(formatDate(data.DYMD)).append("\t"); // 伝票日付
			sb.append(n2b(data.SEIRI)).append("\t");       // 整理月フラグ
			sb.append(n2b(data.DCNO)).append("\t");        // 伝票番号
			sb.append(n2b(data.RBMN)).append("\t");        // 借方 部門コード
			sb.append(n2b(data.RTOR)).append("\t");        // 借方 取引先コード
			sb.append(n2b(data.RKMK)).append("\t");        // 借方 科目コード
			sb.append(n2b(data.REDA)).append("\t");        // 借方 枝番コード
			sb.append(n2b(data.RKOJ)).append("\t");        // 借方 工事コード
			sb.append(n2b(data.RKOS)).append("\t");        // 借方 工種コード
			sb.append(n2b(data.RPRJ)).append("\t");        // 借方 プロジェクトコード
			sb.append(n2b(data.RSEG)).append("\t");        // 借方 セグメントコード
			sb.append(n2b(data.RDM1)).append("\t");        // 借方 ユニバーサルフィールド１
			sb.append(n2b(data.RDM2)).append("\t");        // 借方 ユニバーサルフィールド２
			sb.append(n2b(data.RDM3)).append("\t");        // 借方 ユニバーサルフィールド３
			sb.append(n2b(data.TKY)).append("\t");         // 摘要
			sb.append(n2b(data.TNO)).append("\t");         // 摘要コード
			sb.append(n2b(data.SBMN)).append("\t");        // 貸方 部門コード
			sb.append(n2b(data.STOR)).append("\t");        // 貸方 取引先コード
			sb.append(n2b(data.SKMK)).append("\t");        // 貸方 科目コード
			sb.append(n2b(data.SEDA)).append("\t");        // 貸方 枝番コード
			sb.append(n2b(data.SKOJ)).append("\t");        // 貸方 工事コード
			sb.append(n2b(data.SKOS)).append("\t");        // 貸方 工種コード
			sb.append(n2b(data.SPRJ)).append("\t");        // 貸方 プロジェクトコード
			sb.append(n2b(data.SSEG)).append("\t");        // 貸方 セグメントコード
			sb.append(n2b(data.SDM1)).append("\t");        // 貸方 ユニバーサルフィールド１
			sb.append(n2b(data.SDM2)).append("\t");        // 貸方 ユニバーサルフィールド２
			sb.append(n2b(data.SDM3)).append("\t");        // 貸方 ユニバーサルフィールド３
			sb.append(data.EXVL.toString()).append("\t");  // 対価金額
			sb.append(data.VALU.toString()).append("\t");  // 金額
			sb.append(n2b(data.ZKMK)).append("\t");        // 消費税対象科目コード
			sb.append(data.ZRIT.toString()).append("\t");  // 消費税対象科目税率
			sb.append(n2b(data.ZZKB)).append("\t");        // 消費税対象科目　課税区分
			sb.append(n2b(data.ZGYO)).append("\t");        // 消費税対象科目　業種区分
			sb.append(n2b(data.ZSRE)).append("\t");        // 消費税対象科目　仕入区分
			sb.append(data.RRIT.toString()).append("\t");  // 借方 税率
			sb.append(data.SRIT.toString()).append("\t");  // 貸方 税率
			sb.append(n2b(data.RZKB)).append("\t");        // 借方 課税区分
			sb.append(n2b(data.RGYO)).append("\t");        // 借方 業種区分
			sb.append(n2b(data.RSRE)).append("\t");        // 借方 仕入区分
			sb.append(n2b(data.SZKB)).append("\t");        // 貸方 課税区分
			sb.append(n2b(data.SGYO)).append("\t");        // 貸方 業種区分
			sb.append(n2b(data.SSRE)).append("\t");        // 貸方 仕入区分
			sb.append(formatDate(data.SYMD)).append("\t"); // 支払日
			sb.append(n2b(data.SKBN)).append("\t");        // 支払区分
			sb.append(formatDate(data.SKIZ)).append("\t"); // 支払期日
			sb.append(formatDate(data.UYMD)).append("\t"); // 回収日
			sb.append(n2b(data.UKBN)).append("\t");        // 入金区分
			sb.append(formatDate(data.UKIZ)).append("\t"); // 回収期日
			sb.append(n2b(data.STEN)).append("\t");        // 店券フラグ
			sb.append(n2b(data.DKEC)).append("\t");        // 消込コード
			sb.append(formatDate(data.KYMD)).append("\t"); // 起票年月日
			sb.append(n2b(data.KBMN)).append("\t");        // 起票部門コード
			sb.append(n2b(data.KUSR)).append("\t");        // 起票者コード
			sb.append(n2b(data.FUSR)).append("\t");        // 入力者コード
			sb.append(n2b(data.FSEN)).append("\t");        // 付箋番号
			sb.append(n2b(data.SGNO)).append("\t");        // 承認グループNo.
			sb.append(n2b(data.BUNRI)).append("\t");       // 分離区分
			sb.append(n2b(data.RATE)).append("\t");        // レート
			sb.append(n2b(data.GEXVL)).append("\t");       // 外貨対価金額
			sb.append(n2b(data.GVALU)).append("\t");       // 外貨金額
			sb.append(n2b(data.RKEIGEN)).append("\t");     // 借方 軽減税率区分
			sb.append(n2b(data.SKEIGEN)).append("\t");     // 貸方 軽減税率区分
			sb.append(n2b(data.ZKEIGEN)).append("\t");     // 税対象科目 軽減税率区分
			sb.append(n2b(data.GSEP));                     // 行区切り
			if(1 == rNo) {
				sb.append("\t");
				sb.append(n2b(data.RURIZEIKEISAN)).append("\t");       // 借方　併用売上税額計算方式
				sb.append(n2b(data.SURIZEIKEISAN)).append("\t");     // 貸方　併用売上税額計算方式
				sb.append(n2b(data.ZURIZEIKEISAN)).append("\t");     // 税対象科目　併用売上税額計算方式
				sb.append(n2b(data.RMENZEIKEIKA)).append("\t");     // 借方　仕入税額控除経過措置割合
				sb.append(n2b(data.SMENZEIKEIKA)).append("\t");        // 貸方　仕入税額控除経過措置割合
				sb.append(n2b(data.ZMENZEIKEIKA));                      // 税対象科目　仕入税額控除経過措置割合
			}
			sb.append("\r\n");                            // 改行コード
		}
		EteamFileLogic.makeCsvFile(file, sb.toString());
	}
	
	/**
	 * インポート用のCSVファイルへ仕分けデータを書き込みます。
	 * @param file    ファイル
	 * @param Siwake  仕分けデータ
	 */
	protected static void fileWriter_SIAS(File file, List<Open21CSetData> Siwake, int rNo) {
		StringBuilder sb = new StringBuilder();
		
		for(Open21CSetData data : Siwake) {
			sb.append(formatDate(data.DYMD)).append("\t"); // 伝票日付
			sb.append(n2b(data.SEIRI)).append("\t");       // 整理月フラグ
			sb.append(n2b(data.DCNO)).append("\t");        // 伝票番号
			sb.append(formatDate(data.KYMD)).append("\t"); // 起票年月日
			sb.append(n2b(data.KBMN)).append("\t");        // 起票部門コード
			sb.append(n2b(data.KUSR)).append("\t");        // 起票者コード
			sb.append(n2b(data.SGNO)).append("\t");        // 承認グループNo.
			
			sb.append(n2b(data.HF1)).append("\t");         // ヘッダーフィールド１
			sb.append(n2b(data.HF2)).append("\t");         // ヘッダーフィールド２
			sb.append(n2b(data.HF3)).append("\t");         // ヘッダーフィールド３
			sb.append(n2b(data.HF4)).append("\t");         // ヘッダーフィールド４
			sb.append(n2b(data.HF5)).append("\t");         // ヘッダーフィールド５
			sb.append(n2b(data.HF6)).append("\t");         // ヘッダーフィールド６
			sb.append(n2b(data.HF7)).append("\t");         // ヘッダーフィールド７
			sb.append(n2b(data.HF8)).append("\t");         // ヘッダーフィールド８
			sb.append(n2b(data.HF9)).append("\t");         // ヘッダーフィールド９
			sb.append(n2b(data.HF10)).append("\t");        // ヘッダーフィールド１０
			
			sb.append(n2b(data.RBMN)).append("\t");        // 借方 部門コード
			sb.append(n2b(data.RTOR)).append("\t");        // 借方 取引先コード
			sb.append(n2b(data.RKMK)).append("\t");        // 借方 科目コード
			sb.append(n2b(data.REDA)).append("\t");        // 借方 枝番コード
			sb.append(n2b(data.RKOJ)).append("\t");        // 借方 工事コード
			sb.append(n2b(data.RKOS)).append("\t");        // 借方 工種コード
			sb.append(n2b(data.RPRJ)).append("\t");        // 借方 プロジェクトコード
			sb.append(n2b(data.RSEG)).append("\t");        // 借方 セグメントコード
			
			sb.append(n2b(data.RDM1)).append("\t");        // 借方 ユニバーサルフィールド１
			sb.append(n2b(data.RDM2)).append("\t");        // 借方 ユニバーサルフィールド２
			sb.append(n2b(data.RDM3)).append("\t");        // 借方 ユニバーサルフィールド３
			sb.append(n2b(data.RDM4)).append("\t");        // 借方 ユニバーサルフィールド４
			sb.append(n2b(data.RDM5)).append("\t");        // 借方 ユニバーサルフィールド５
			sb.append(n2b(data.RDM6)).append("\t");        // 借方 ユニバーサルフィールド６
			sb.append(n2b(data.RDM7)).append("\t");        // 借方 ユニバーサルフィールド７
			sb.append(n2b(data.RDM8)).append("\t");        // 借方 ユニバーサルフィールド８
			sb.append(n2b(data.RDM9)).append("\t");        // 借方 ユニバーサルフィールド９
			sb.append(n2b(data.RDM10)).append("\t");       // 借方 ユニバーサルフィールド１０
			sb.append(n2b(data.RDM11)).append("\t");       // 借方 ユニバーサルフィールド１１
			sb.append(n2b(data.RDM12)).append("\t");       // 借方 ユニバーサルフィールド１２
			sb.append(n2b(data.RDM13)).append("\t");       // 借方 ユニバーサルフィールド１３
			sb.append(n2b(data.RDM14)).append("\t");       // 借方 ユニバーサルフィールド１４
			sb.append(n2b(data.RDM15)).append("\t");       // 借方 ユニバーサルフィールド１５
			sb.append(n2b(data.RDM16)).append("\t");       // 借方 ユニバーサルフィールド１６
			sb.append(n2b(data.RDM17)).append("\t");       // 借方 ユニバーサルフィールド１７
			sb.append(n2b(data.RDM18)).append("\t");       // 借方 ユニバーサルフィールド１８
			sb.append(n2b(data.RDM19)).append("\t");       // 借方 ユニバーサルフィールド１９
			sb.append(n2b(data.RDM20)).append("\t");       // 借方 ユニバーサルフィールド２０
			
			sb.append(data.RRIT.toString()).append("\t");  // 借方 税率
			sb.append(n2b(data.RZKB)).append("\t");        // 借方 課税区分
			sb.append(n2b(data.RGYO)).append("\t");        // 借方 業種区分
			sb.append(n2b(data.RSRE)).append("\t");        // 借方 仕入区分
			sb.append(n2b(data.RTKY)).append("\t");        // 借方 摘要
			sb.append(n2b(data.RTNO)).append("\t");        // 借方 摘要コード

			sb.append(n2b(data.SBMN)).append("\t");        // 貸方 部門コード
			sb.append(n2b(data.STOR)).append("\t");        // 貸方 取引先コード
			sb.append(n2b(data.SKMK)).append("\t");        // 貸方 科目コード
			sb.append(n2b(data.SEDA)).append("\t");        // 貸方 枝番コード
			sb.append(n2b(data.SKOJ)).append("\t");        // 貸方 工事コード
			sb.append(n2b(data.SKOS)).append("\t");        // 貸方 工種コード
			sb.append(n2b(data.SPRJ)).append("\t");        // 貸方 プロジェクトコード
			sb.append(n2b(data.SSEG)).append("\t");        // 貸方 セグメントコード
			
			sb.append(n2b(data.SDM1)).append("\t");        // 貸方 ユニバーサルフィールド１
			sb.append(n2b(data.SDM2)).append("\t");        // 貸方 ユニバーサルフィールド２
			sb.append(n2b(data.SDM3)).append("\t");        // 貸方 ユニバーサルフィールド３
			sb.append(n2b(data.SDM4)).append("\t");        // 貸方 ユニバーサルフィールド４
			sb.append(n2b(data.SDM5)).append("\t");        // 貸方 ユニバーサルフィールド５
			sb.append(n2b(data.SDM6)).append("\t");        // 貸方 ユニバーサルフィールド６
			sb.append(n2b(data.SDM7)).append("\t");        // 貸方 ユニバーサルフィールド７
			sb.append(n2b(data.SDM8)).append("\t");        // 貸方 ユニバーサルフィールド８
			sb.append(n2b(data.SDM9)).append("\t");        // 貸方 ユニバーサルフィールド９
			sb.append(n2b(data.SDM10)).append("\t");       // 貸方 ユニバーサルフィールド１０
			sb.append(n2b(data.SDM11)).append("\t");       // 貸方 ユニバーサルフィールド１１
			sb.append(n2b(data.SDM12)).append("\t");       // 貸方 ユニバーサルフィールド１２
			sb.append(n2b(data.SDM13)).append("\t");       // 貸方 ユニバーサルフィールド１３
			sb.append(n2b(data.SDM14)).append("\t");       // 貸方 ユニバーサルフィールド１４
			sb.append(n2b(data.SDM15)).append("\t");       // 貸方 ユニバーサルフィールド１５
			sb.append(n2b(data.SDM16)).append("\t");       // 貸方 ユニバーサルフィールド１６
			sb.append(n2b(data.SDM17)).append("\t");       // 貸方 ユニバーサルフィールド１７
			sb.append(n2b(data.SDM18)).append("\t");       // 貸方 ユニバーサルフィールド１８
			sb.append(n2b(data.SDM19)).append("\t");       // 貸方 ユニバーサルフィールド１９
			sb.append(n2b(data.SDM20)).append("\t");       // 貸方 ユニバーサルフィールド２０

			sb.append(data.SRIT.toString()).append("\t");  // 貸方 税率
			sb.append(n2b(data.SZKB)).append("\t");        // 貸方 課税区分
			sb.append(n2b(data.SGYO)).append("\t");        // 貸方 業種区分
			sb.append(n2b(data.SSRE)).append("\t");        // 貸方 仕入区分
			sb.append(n2b(data.STKY)).append("\t");        // 貸方 摘要
			sb.append(n2b(data.STNO)).append("\t");        // 貸方 摘要コード
			
			sb.append(n2b(data.ZKMK)).append("\t");        // 消費税対象科目コード
			sb.append(data.ZRIT.toString()).append("\t");  // 消費税対象科目税率
			sb.append(n2b(data.ZZKB)).append("\t");        // 消費税対象科目　課税区分
			sb.append(n2b(data.ZGYO)).append("\t");        // 消費税対象科目　業種区分
			sb.append(n2b(data.ZSRE)).append("\t");        // 消費税対象科目　仕入区分

			sb.append(data.EXVL.toString()).append("\t");  // 対価金額
			sb.append(data.VALU.toString()).append("\t");  // 金額
			sb.append(formatDate(data.SYMD)).append("\t"); // 支払日
			sb.append(n2b(data.SKBN)).append("\t");        // 支払区分
			sb.append(formatDate(data.SKIZ)).append("\t"); // 支払期日
			sb.append(formatDate(data.UYMD)).append("\t"); // 回収日
			sb.append(n2b(data.UKBN)).append("\t");        // 入金区分
			sb.append(formatDate(data.UKIZ)).append("\t"); // 回収期日
			sb.append(n2b(data.DKEC)).append("\t");        // 消込コード
			sb.append(n2b(data.FUSR)).append("\t");        // 入力者コード
			sb.append(n2b(data.FSEN)).append("\t");        // 付箋番号
			sb.append(n2b(data.TKFLG)).append("\t");       // 貸借別摘要フラグ
			sb.append(n2b(data.BUNRI)).append("\t");       // 分離区分
			sb.append(n2b(data.HEIC)).append("\t");        // 幣種
			sb.append(n2b(data.RATE)).append("\t");        // レート
			sb.append(n2b(data.GEXVL)).append("\t");       // 外貨対価金額
			sb.append(n2b(data.GVALU)).append("\t");       // 外貨金額
			sb.append(n2b(data.RKEIGEN)).append("\t");     // 借方 軽減税率区分
			sb.append(n2b(data.SKEIGEN)).append("\t");     // 貸方 軽減税率区分
			sb.append(n2b(data.ZKEIGEN)).append("\t");     // 税対象科目 軽減税率区分
			sb.append(n2b(data.GSEP)).append("\t");        // 行区切り
			sb.append(n2b(data.LNO));                      // リンクＮｏ
			if(2 == rNo) {
				sb.append("\t");
				sb.append(n2b(data.RURIZEIKEISAN)).append("\t");       // 借方　併用売上税額計算方式
				sb.append(n2b(data.SURIZEIKEISAN)).append("\t");     // 貸方　併用売上税額計算方式
				sb.append(n2b(data.ZURIZEIKEISAN)).append("\t");     // 税対象科目　併用売上税額計算方式
				sb.append(n2b(data.RMENZEIKEIKA)).append("\t");     // 借方　仕入税額控除経過措置割合
				sb.append(n2b(data.SMENZEIKEIKA)).append("\t");        // 貸方　仕入税額控除経過措置割合
				sb.append(n2b(data.ZMENZEIKEIKA));                      // 税対象科目　仕入税額控除経過措置割合
			}
			
			sb.append("\r\n");                             // 改行コード
		}
		EteamFileLogic.makeCsvFile(file, sb.toString());
	}

	/**
	 * インポート用CSVファイル(e文書)へ仕分けデータを書き込みます。
	 * //インポート対象の伝票の添付ファイルにe文書が含まれている場合
	 * [e文書番号][WF起票者姓名][WF最終承認者姓名][書類種別][書類日付][書類金額][書類発行者][書類品名]をタブ区切りで出力したファイル
	 * 「ebData_(リンクNo).csv」を作成してImporter.exe側で読み取らせる
	 * e文書情報はリンク番号1つごとにまとめて作成する
	 * @param linkList リンク情報リスト
	 */
	protected static void fileWriter_Link(List<Open21LinkData> linkList) {
		String path  = EteamSettingInfo.getSettingInfo("op21mparam_csv_path") + "\\" + EteamCommon.getContextSchemaName();
		for (Open21LinkData link : linkList) {
			StringBuilder sb = new StringBuilder();
			File file = new File(path, IMP_LINKDATA_CSV + link.getLinkNo() + ".csv");
			sb.append(link.getLinkName()).append("\t"); // リンク名称
			sb.append(link.getType()).append("\t"); // 2:通常ファイル、3:e文書
			sb.append(link.getFileName()).append("\t"); // ファイル名
			sb.append(link.getEbunshoNo()).append("\t"); // e文書番号
			sb.append(link.getShinseisha()).append("\t"); // ワークフロー申請者名称
			sb.append(link.getShouninsha()).append("\t"); // ワークフロー最終承認者名称
			sb.append(link.getShubetsu()).append("\t"); // 書類種別
			sb.append(link.getDate()).append("\t"); // 書類日付(yyyymmdd)
			sb.append(link.getKingaku()).append("\t"); // 書類金額
			sb.append(link.getHakkousha()).append("\t"); // 書類発行者
			sb.append(link.getHinmei()); // 書類品名
			sb.append("\r\n"); // 改行コード
			EteamFileLogic.appendCsvFile(file, sb.toString());
		}
	}
	
	/**
	 * ヌルはブランクへ、それ以外ならそのまま。
	 * @param s 変換前
	 * @return 変換後
	 */
	protected static String n2b(String s) {
		return (null == s) ? "" : s.replace("\t", "");
	}
	
	/**
	 * 日付型を文字列(yyyyMMdd)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	protected static String formatDate(Date d){
		if (null == d) {
			return "0";
		} else {
			return new SimpleDateFormat("yyyyMMdd").format(d);
		}
	}
}
