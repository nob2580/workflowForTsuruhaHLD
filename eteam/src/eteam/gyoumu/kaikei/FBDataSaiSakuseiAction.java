package eteam.gyoumu.kaikei;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import eteam.common.select.KaikeiCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * FBデータ作成画面Action
 */
@Getter @Setter @ToString
public class FBDataSaiSakuseiAction extends EteamAbstractAction {

//＜定数＞
	
//＜画面入力＞
	/** 個人精算支払日 */
	String kojinSeisanShiharaiBi;

//＜DROP DOWN＞
	/** 個人精算支払日のDropDownList */
	List<GMap> kojinSeisanShiharaiBiList;

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

//＜入力チェック＞
		@Override
		protected void formatCheck() {
			checkDate(kojinSeisanShiharaiBi, "個人精算支払日", false);
		}

		@Override
		protected void hissuCheck(int eventNum) {
			String[][] list = {
				//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{kojinSeisanShiharaiBi, "個人精算支払日", "0", "1" },
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
			
			// 個人精算支払日の検索が0件の場合
			if (kojinSeisanShiharaiBiList.isEmpty()) {
				errorList.add("個人精算支払日データが存在しません。");
				return "error";
			}
			
			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 作成イベント処理
	 * @return ResultName
	 */
	public String saisakusei() {
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
			
			//スキーマを起動元のコンテキストから渡されたものに
			Map<String, String> threadMap = EteamThreadMap.get();
			threadMap.put(SYSTEM_PROP.SCHEMA, EteamCommon.getContextSchemaName());

	    	// FBデータ作成バッチを実行
			FBDataCreateBat bat = EteamContainer.getComponent(FBDataCreateBat.class);
			bat.setKojinSeisanShiharaibi(toDate(kojinSeisanShiharaiBi));
			bat.setGamenResponse(true);
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
	}

	/**
	 * プルダウン生成
	 */
	protected void displaySeigyo() {
		// 未来日の個人精算支払日（一定数）を取得。
		String limitNum = setting.listNumForShiharaibi();
		kojinSeisanShiharaiBiList = kaikeiLogic.loadKojinSeisanShiharaiBi(Integer.parseInt(limitNum));
		for (GMap shiharaiBiMap: kojinSeisanShiharaiBiList) {
			String cd = formatDate(shiharaiBiMap.get("kojinseisan_shiharaibi"));
			String val = cd;
			if ((int)shiharaiBiMap.get("sakuseizumi") == 1) {
				val = val + " (作成済)";
			}
			shiharaiBiMap.put("cd", cd);
			shiharaiBiMap.put("val", val);
		}
	}
}