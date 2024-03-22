package eteam.gyoumu.shiharai;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamIO;
import eteam.common.KaishaInfo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * FBデータ作成（支払依頼）画面Action
 */
@Getter @Setter @ToString
public class ShiharaiIraiOutputAction extends EteamAbstractAction {

//＜定数＞
	
//＜画面入力＞
	/** 支払予定日 */
	String kojinSeisanShiharaiBi;
	/** 支払種別 */
	String shiharaiShubetsu;

//＜制御情報＞
	/** 支払日のDropDownList */
	List<GMap> kojinSeisanShiharaiBiList;
	/** 支払種別DropDownList */
	List<GMap> shiharaiShubetsuList;
	/** 支払種別ドメイン */
	String[] shiharaiShubetsuDomain;
	/** 債務使用フラグ */
	String saimuShiyouFlg = KaishaInfo.getKaishaInfo(KaishaInfo.ColumnName.SAIMU_SHIYOU_FLG);
	/** 債務EXE結果コード */
	String exeRetMsg = "";//初期値ブランクでEXE実行後の表示で0～3の値が入るはず。0～3ならEXE実行後の再表示と判定してクライアントでメッセージ表示

//＜ダウンロード用＞
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** ダウンロードファイル用 */
	protected InputStream inputStream;

//＜部品＞
	/** 会計SELECT */
	 KaikeiCategoryLogic kaikeiLogic;
	/** FBデータ作成（支払依頼） */
	ShiharaiIraiOutputLogic myLogic;
	/** システムカテゴリロジック */
	SystemKanriCategoryLogic systemLogic;
	/** 債務支払連携でファイル経由処理があるので、アプリ側で排他かける */
	protected static Object lockObj = new Object();

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDate (kojinSeisanShiharaiBi, "支払予定日", false);
		checkDomain (shiharaiShubetsu, shiharaiShubetsuDomain, "支払種別", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{kojinSeisanShiharaiBi, "支払予定", "1", "1"},
			{shiharaiShubetsu, "支払種別", "0", "0"},
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			//部品new
			init(connection);

			// プルダウン項目データを取得
			displaySeigyo();
			
			// 支払日の検索が0件の場合
			if (kojinSeisanShiharaiBiList.isEmpty()) {
				errorList.add("支払予定日データが存在しません。");
				return "error";
			}
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 債務支払データ作成イベント処理
	 * @return ResultName
	 */
	public String saimuMisakusei() {
		return saimu(false);
	}
	/**
	 * 債務支払データ作成イベント処理
	 * @return ResultName
	 */
	public String saimuAll() {
		return saimu(true);
	}
	/**
	 * 債務支払データ作成イベント処理
	 * @param all 作成済含めて全部
	 * @return ResultName
	 */
	protected String saimu(boolean all) {
		try(EteamConnection connection = EteamConnection.connect()) {
			//部品new
			init(connection);

			// プルダウン項目データを取得
			displaySeigyo();

			// 1.入力チェック
			formatCheck();
			hissuCheck(1);
			if (0 < errorList.size())
			{
				return "error";
			}
		
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理
synchronized(lockObj){
			List<GMap> list = myLogic.loadShiharaiIrai4SaimuPlus(toDate(kojinSeisanShiharaiBi), shiharaiShubetsu, 2, all);
			list.addAll(myLogic.loadShiharaiIrai4SaimuMinus(toDate(kojinSeisanShiharaiBi), shiharaiShubetsu, all));
			if (list.isEmpty()) {
				errorList.add("支払対象のデータがありません。");
				return "error";
			}
			for (GMap record : list) {
				myLogic.updateSaimuMade((String)record.get("denpyou_id"));
			}
			
			//CSVファイル作る
			ByteArrayOutputStream objBos = new ByteArrayOutputStream();
			PrintWriter out = new PrintWriter(objBos);
			String errorMessage = myLogic.makeSaimuShiharai(list, out, toDate(kojinSeisanShiharaiBi));
			if (errorMessage != null) {
				errorList.add(errorMessage);
				return "error";
			}
			out.close();
			
			String filePathStr = "C:/eteam/tmp/shiharaiirai_data/" + EteamCommon.getContextSchemaName();
			String fileNmStr = filePathStr + "/SSESHIP.csv";
			
			File tmpFolder = new File(filePathStr);
			if(!tmpFolder.exists()) {
				tmpFolder.mkdirs();
			}
			
			EteamIO.writeFile(objBos.toByteArray(), fileNmStr);
			
			//作成したファイルは調査のため残しておく
			
			//EXE呼び出し
			int exeRetCd;
			if(Open21Env.getVersion() == Version.SIAS){
				exeRetCd = myLogic.callSaimuExeSIAS(getUser());
			} else {
				exeRetCd = myLogic.callSaimuExeDE3(getUser());
			}
			exeRetMsg = myLogic.saimuExeCd2Msg(exeRetCd);
}

			//5.戻り値を返す
			connection.commit();
			return "success";
		}
	}

	/**
	 * FBデータ作成イベント処理
	 * @return ResultName
	 */
	public String fbMisakusei() {
		return fb(false);
	}
	/**
	 * FBデータ作成イベント処理
	 * @return ResultName
	 */
	public String fbAll() {
		return fb(true);
	}
	/**
	 * FBデータ作成イベント処理
	 * @param all 作成済含める
	 * @return ResultName
	 */
	protected String fb(boolean all) {
		try(EteamConnection connection = EteamConnection.connect()) {
			//部品new
			init(connection);

			// プルダウン項目データを取得
			displaySeigyo();

			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if (0 < errorList.size())
			{
				return "error";
			}
		
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理
			
			//スキーマを起動元のコンテキストから渡されたものに
			Map<String, String> threadMap = EteamThreadMap.get();
			threadMap.put(SYSTEM_PROP.SCHEMA, EteamCommon.getContextSchemaName());

	    	// FBデータ作成バッチを実行
			FBDataCreateShiharaiBat bat = EteamContainer.getComponent(FBDataCreateShiharaiBat.class);
			bat.setKojinSeisanShiharaibi(toDate(kojinSeisanShiharaiBi));
			bat.setGamenResponse(true);
			bat.all = all;
			//mainProc内でエラー発生したらzip作成しない
			if(bat.mainProc() == 1) {
				errorList.add("FBデータ作成時に内部エラーが発生しました。管理者に連絡してください。");
				return "error";
			}

			// FBデータがなければエラー
			List<ByteArrayOutputStream> bstList = bat.getBstList();
			List<String> fnameList = bat.getFnameList();
			if (bstList.isEmpty()) {
				errorList.add("支払対象のデータがありません。");
				return "error";
			}

			// FBデータをZIP化して返す
			this.inputStream = makeFBDataScream(bstList, fnameList);
	
			//5.戻り値を返す
			return "success";
		}
	}

//＜イベント＞
	/**
	 * レスポンス(ZIPファイル)返却用のストリームを作る
	 * @param bstList FBデータのファイルデータのリスト
	 * @param fnameList FBデータのファイル名のリスト
	 * @return ZIPのレスポンス用ストリーム
	 */
	protected InputStream makeFBDataScream(List<ByteArrayOutputStream> bstList, List<String> fnameList) {
		try {
			//ZIP出力ストリームの設定
			ByteArrayOutputStream bArray1 = new ByteArrayOutputStream(); 
			ZipOutputStream objZos = new ZipOutputStream(bArray1); 
			objZos.setEncoding(Encoding.MS932);
				
			//伝票ID毎に添付ファイルを処理する。
			for (int i = 0; i < bstList.size(); i++) {
				ByteArrayOutputStream bst = bstList.get(i);
					
				//ZIPエントリ
				ZipEntry objZe=new ZipEntry(fnameList.get(i));
				objZe.setMethod(ZipOutputStream.DEFLATED);

				//ZIP出力ストリームに追加する。
				objZos.putNextEntry(objZe);
				objZos.write(bst.toByteArray());
						
				//ZIPエントリクローズ
				objZos.closeEntry();
			}

			//ZIP出力ストリームクローズ
			objZos.close();

			contentType = ContentType.ZIP;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, "fbdata.zip");
				
			ByteArrayInputStream bArray2 = new ByteArrayInputStream(bArray1.toByteArray()); 
			return bArray2;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * オブジェクトNEW
	 * @param connection コネクション
	 */
	protected void init(EteamConnection connection) {
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		myLogic = EteamContainer.getComponent(ShiharaiIraiOutputLogic.class, connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}

	/**
	 * プルダウン生成
	 */
	protected void displaySeigyo() {
		// 未来日の支払日（一定数）を取得。
		String limitNum = setting.listNumForShiharaibi();
		kojinSeisanShiharaiBiList = myLogic.loadShiharaiYoteiBi(Integer.parseInt(limitNum));
		for (GMap shiharaiBiMap: kojinSeisanShiharaiBiList) {
			String cd = formatDate(shiharaiBiMap.get("kojinseisan_shiharaibi"));
			String val = cd;
			shiharaiBiMap.put("cd", cd);
			shiharaiBiMap.put("val", val);
		}
		shiharaiShubetsuList = systemLogic.loadNaibuCdSetting("shiharai_irai_shubetsu");
		shiharaiShubetsuDomain = EteamCommon.mapList2Arr(shiharaiShubetsuList, "naibu_cd");
	}
}
