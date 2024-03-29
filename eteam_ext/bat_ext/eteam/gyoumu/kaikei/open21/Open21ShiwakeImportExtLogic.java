package eteam.gyoumu.kaikei.open21;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eteam.common.EteamCommon;
import eteam.common.EteamSettingInfo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;

/**
 * Open21へのインポートを行うクラス
 */
public class Open21ShiwakeImportExtLogic extends Open21ShiwakeImportLogic {
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
			//▼カスタマイズ
			command.append(EteamSettingInfo.getSettingInfo("jidou_bunri_bumon_cd")).append(" ");    // 部門コード
			//▲カスタマイズ
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
}