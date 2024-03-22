package eteam.gyoumu.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 画像アップロードActionクラス
 */
@Getter @Setter @ToString
public class GazouUploadAction extends EteamAbstractAction {
	
	//＜画面入力＞
	/** ファイルオブジェクト */
	protected File gazouFile;
	/** 添付ファイルコンテンツタイプ */
	protected String gazouFileContentType;
	/** ファイル名 */
	protected String gazouFileFileName;
	/** シリアル番号 */
	protected String downloadSerialNo;
	/** 画像ファイルデータリスト */
	protected List<GMap> dataList;
	/** チェックボックスの選択リスト */
	protected String[] sentakuList;
	
	//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** 画像アップロードLogicクラス */
	GazouUploadLogic gazouUploadLogic;

	@Override
	protected void formatCheck() {}
	// 入力チェック
	@Override
	protected void hissuCheck(int eventNum) {
		super.checkHissu(gazouFile, "画像ファイル");
	}
	
	/**
	 * 初期表示
	 * @return ResultName
	 */
	public String init() {

		connection = EteamConnection.connect();

		try {
			// 一覧読み込み
			displaySeigyo();
		} finally {
			connection.close();
		}
		return "success";
	}
	
	/**
	 * アップロード処理
	 * @return ResultName
	 */
	public String upload() {
		
		connection = EteamConnection.connect();
		
		try {
			// 一覧読み込み
			displaySeigyo();
			// 入力チェック
			hissuCheck(2);
			// エラーがあった場合は処理終了
			if (0 < errorList.size())
			{
				return "error";
			}

			// ファイルサイズエラーチェック
			EteamCommon.uploadFileSizeCheck();
			// ファイルサイズ取得
			long fileSize = gazouFile.length();
			// ファイルデータをbyte[]で取得
			byte[] data = new byte[(int) fileSize];
			FileInputStream fileInputStream = new FileInputStream(gazouFile);
			fileInputStream.read(data);
			// 画像ファイルをアップロード
			gazouUploadLogic.insertGazouFile(gazouFileFileName, fileSize, gazouFileContentType, data, getUser().getTourokuOrKoushinUserId());
			fileInputStream.close();
			connection.commit();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}

		return "success";
	}

	/**
	 * 差し替え処理
	 * @return ResultName
	 */
	public String sashikae(){
		
		connection = EteamConnection.connect();
		
		try {
			// 一覧読み込み
			displaySeigyo();
			// 入力チェック
			hissuCheck(2);
			// エラーがあった場合は処理終了
			if (0 < errorList.size())
			{
				return "error";
			}

			// ファイルサイズエラーチェック
			EteamCommon.uploadFileSizeCheck();
			// ファイルサイズ取得
			long fileSize = gazouFile.length();
			// ファイルデータをbyte[]で取得
			byte[] data = new byte[(int) fileSize];
			FileInputStream fileInputStream = new FileInputStream(gazouFile);
			fileInputStream.read(data);
			
			
			for (String serialNo : sentakuList) {
				// 画像ファイルを差し替え
				gazouUploadLogic.updateGazouFile(gazouFileFileName, fileSize, gazouFileContentType, data, getUser().getTourokuOrKoushinUserId(), Integer.parseInt(serialNo));
			}
			
			fileInputStream.close();
			connection.commit();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			connection.close();
		}
		
		return "success";
	}
	
	/**
	 * 削除処理
	 * @return ResultName
	 */
	public String sakujo() {

		connection = EteamConnection.connect();
		try {
			// 一覧読み込み
			displaySeigyo();
			// チェックボックスで選択されたファイルを削除する。
			for (String serialNo : sentakuList) {
				gazouUploadLogic.deleteGazouFile(Integer.parseInt(serialNo));
			}
			connection.commit();
		} finally {
			connection.close();
		}
		return "success";
	}
	
	/**
	 * 画面表示イベントやイベントエラー表示時用に、画面の共通制御処理を行う。
	 */
	protected void displaySeigyo() {

		gazouUploadLogic = EteamContainer.getComponent(GazouUploadLogic.class, connection);
		// 画像ファイルテーブル検索処理
		dataList = gazouUploadLogic.selectGazouFile();
		// 画面表示用にデータを格納する
		for (GMap map : dataList) {
			map.put("serialNo", map.get("serial_no"));
			map.put("fileName", map.get("file_name"));
			map.put("tourokuDate", formatTime(map.get("touroku_time")));
			map.put("koushinDate", formatTime(map.get("koushin_time")));
		}
	}
}
