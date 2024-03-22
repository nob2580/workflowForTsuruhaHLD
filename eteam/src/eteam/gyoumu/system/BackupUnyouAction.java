package eteam.gyoumu.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.FileName;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バックアップ運用Actionクラス
 */
@Getter @Setter @ToString
public class BackupUnyouAction extends EteamAbstractAction{
	
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(BackupUnyouAction.class);
	
	//＜定数＞
	
	//＜画面入力＞
	/** モード(0:作成 1:復元) */
	String mode;
	/** チェックボックスの選択リスト */
	String[] sentakuList;
	/** コメント */
	String comment;
	/** 相関チェックフラグ */
	Boolean selectedVersionFlg;
	
	//＜画面入力以外＞
	/** スキーマ名 */
	String schemaName;
	/** ファイルディレクトリ */
	String fileDir;
	/** ファイルリスト */
	List<Dump> DataList;

	//＜部品＞
	/** バックアップ運用Logicクラス */
	BackupUnyouLogic backupLogic;
	/** コネクションクラス */
	EteamConnection connection;
	/** システム管理　SELECT */
	SystemKanriCategoryLogic systemLogic;
	
	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		//sentakuListに不正なファイルが来たら落ちる。
		//commentに長いのが来たら、20文字で切れる
		checkNumber(mode, 1, 1, "処理モード", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目	項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{mode, "処理モード", "2"},
		};
		hissuCheckCommon(list, eventNum);
	}
	
	/**
	 * 作成日時を返すクラス(一覧のソートに用いる)
	 */
	@Getter
	public class Dump{
		
		/** ファイル名 */
		String fileName;
		/** 作成時刻 */
		String tourokuDay;
		/** ファイルID */
		String recordId;
		/** バックアップのコメント */
		String backupComment;
		/** バージョン情報判定 */
		boolean existVersionFlg;
		
	    /**
	     * @return tourokuday 作成時刻
	     */
	    public String getTourokuDay() { return tourokuDay; }
	}
	
	/**
	 * 初期表示
	 * @return ResultName
	 */
	public String init() {
		
		// 共通制御読み込み
		displaySeigyo();

		// ダンプディレクトリが存在しない場合はエラー
		File file = new File(fileDir);
		if(!file.exists()){
			throw new RuntimeException("設定されたディレクトリが存在しません。：" + fileDir);
		}
		
		// ダンプファイルリストを取得
		String files[] = file.list();
		DataList = new ArrayList<Dump>();
		
		// バックアップコメントファイルをMAP化する。「ダンプファイル名 コメント」の形式」
		Map<String,String> backupCommentMap = getBackupCommentInfo();
		
		// ダンプファイルリストを表示用に加工
		for (int i = 0; i < files.length; i++) {
			
			//--------------------
			// backup_comment.txtは無視
			//--------------------
			if (FileName.BACKUP_COMMENT_FILENAME.equals(files[i])) {
				log.info("コメントテーブルはスキップ。ファイル名：" + files[i]);
				continue;
			}
			
			//--------------------
			// 以下、backup_comment.txt以外のファイルが対象。
			// 「スキーマ名_yyyyMMddhhmmss.dump」(yyyyMMddhhmmssが作成時刻)になっているはずなので、該当しないファイルは無視する。
			//--------------------
			
			// ファイル名が想定外ならログだけ出して飛ばす
			int len = files[i].length();
			if(len < 21){
				log.info("ファイル名が不正のため処理をスキップ。ファイル名：" + files[i]);
				continue;
			}

			// 拡張子が"dump"ではない場合、処理を抜ける
			String fileKakuchoushi = files[i].substring(len - 5, len);
			if(!fileKakuchoushi.equals(".dump")){
				log.info("dumpファイルではないため処理をスキップ。ファイル名："+ files[i]);
				continue;
			}

			// ファイルの作成時刻の正当性判定。
			String wkSakuseiTime = files[i].substring(len - 19, len - 5);
			try {
				DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				format.setLenient(false);
				format.parse(wkSakuseiTime);
			} catch (ParseException e) {
				log.warn("ファイル名の作成日付が不正のためスキップ。 ファイル名："+ files[i]);
				continue;
			}

			//--------------------
			// 処理中スキーマのダンプのみを表示対象としてリストに追加
			//--------------------
			
			// ファイル名よりスキーマ名を取得
			String[] wkSchemaName = files[i].split("_");
			
			// ログインユーザーの使用スキーマ名と一致するもののみ取得する。
			if(schemaName.equals(wkSchemaName[0])){
				Dump dump = new Dump();
				// ファイル名
				dump.fileName = files[i];
				// 作成時刻
				dump.tourokuDay = wkSakuseiTime.substring(0, 4) + "/" + wkSakuseiTime.substring(4, 6) + "/" + wkSakuseiTime.substring(6, 8) + " " + wkSakuseiTime.substring(8, 10) + ":" + wkSakuseiTime.substring(10, 12);
				// ファイルID(チェックボックスのvalue値に使用。ファイル名から拡張子を引いたもの)
				dump.recordId = files[i].substring(0 , len - 5);
				// バックアップのコメント
				dump.backupComment = backupCommentMap.get(files[i]);
				// wkSchemaNameの要素数が3の場合…true,バージョン情報あり
				// wkSchemaNameの要素数が2の場合…false,バージョン情報なし
				if(wkSchemaName.length == 3) {
					dump.existVersionFlg = true;
				}else{
					dump.existVersionFlg = false;
				}
				DataList.add(dump);
			}
			DataList = DataList.stream().sorted((Comparator.comparing(x -> x.getTourokuDay())))
		             .collect(Collectors.toList());

		}
		return "success";
	}
	
	/**
	 * 連携イベント処理
	 * @return ResultName
	 */
	public String renkei() {
		displaySeigyo();

		// 1.入力チェック 
		hissuCheck(1);
		formatCheck();
		// 2.データ存在チェック なし
		// 3.アクセス可能チェック なし

		// 復元の場合、相関チェック
		if("1".equals(mode)) {                                                                                      
			backupLogic = (BackupUnyouLogic)EteamContainer.getComponent(BackupUnyouLogic.class, connection);                                                                                           
			// 相関チェックフラグが"true"の時、バージョンチェック
			if(selectedVersionFlg) {
				String[] wkversion = sentakuList[0].split("_");
				String version = wkversion[1];
				if(backupLogic.isGraterThanPgVersion(version)){                                                                           
					errorList.add("指定されたバックアップデータのバージョンが稼働中プログラムのバージョンより新しいため復元できません。");                                                  
					init();                                                                                                               
				// 戻り値を返す                                                                                                                 
				return "error";                                                                                                           
				}
			}
		}

		// 4.処理
		BackupUnyouThread myThread = new BackupUnyouThread(Integer.parseInt(mode), sentakuList, EteamCommon.getContextSchemaName(), comment);
		myThread.start();
		
		//5.戻り値を返す
		return "success";
	}
	
	/**
	 * バックアップファイル削除処理
	 * @return ResultName
	 */
	public String sakujo() {

		// 共通制御読み込み
		displaySeigyo();

		log.debug("削除対象件数：" + sentakuList.length);

		// ファイル削除処理
		for (String bkFile : sentakuList) {
			// ダンプファイル削除
			File file = new File(fileDir + File.separator + bkFile + ".dump");
			if (! file.delete()) {
				throw new RuntimeException("ダンプファイル削除失敗");
			}
		}
		return "success";
	}
	
	/**
	 * 画面の共通制御処理を行う。
	 */
	protected void displaySeigyo(){
		
		// ファイルのディレクトリを、設定情報より取得
		fileDir = setting.dbdumpUrl();
		// ログインユーザの使用しているスキーマ名を取得
		schemaName = EteamCommon.getContextSchemaName();
		
	}
	
	
	/**
	 * バックアップコメントファイルの情報を取得し返却する。
	 * @return backupCommentMap バックアップコメントの情報
	 */
	protected Map<String, String> getBackupCommentInfo() {
		// バックアップコメントを取得する
		Map<String , String> retMap = new HashMap<String , String>();
		BufferedReader br;
		String line;
		try {
            br = new BufferedReader(new InputStreamReader(
            		new FileInputStream (fileDir + File.separator + FileName.BACKUP_COMMENT_FILENAME), Encoding.UTF8)); 
			while((line = br.readLine()) != null){
				int index = line.indexOf(" ");
				// 空白がある場合
				if(index != -1){
					// 先頭から最初の空白より前の文字列をキーとし、空白より後ろの文字列を要素とする
					retMap.put(line.substring(0,index),line.substring(index+1));
				}
			}
			br.close();
		} catch (IOException e) {
			log.error("バックアップコメントファイルの読み込みエラー",e);
		}
		return retMap;
	}

}
