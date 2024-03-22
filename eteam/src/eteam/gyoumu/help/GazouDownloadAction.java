package eteam.gyoumu.help;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 画像アップロードのダウンロードクラス
 *
 */
@Getter @Setter @ToString
public class GazouDownloadAction extends EteamAbstractAction{
	
	// 画面入力
	/** 画像ファイルデータリスト */
	protected List<GMap> dataList;
	/** シリアル番号 */
	protected String downloadSerialNo;
	
	//＜画面入力以外＞
	/** ダウンロードファイル用 */
	protected InputStream inputStream;
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** コンテンツのサイズ */
	protected long contentLength;
	
	//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** 画像アップロードLogicクラス */
	GazouUploadLogic gazouUploadLogic;
	
	
	@Override
	protected void formatCheck() {}
	
	// 入力チェック
	@Override
	protected void hissuCheck(int eventNum) {}
	
	/**
	 * ダウンロード処理
	 * @return ResultName
	 */
	public String download() {
		
		connection = EteamConnection.connect();
		gazouUploadLogic = EteamContainer.getComponent(GazouUploadLogic.class, connection);
		// 画像ファイルテーブル検索処理
		dataList = gazouUploadLogic.selectGazouFile();
		// 画面表示用にデータを格納する
		for (GMap map : dataList) {
			map.put("serialNo", map.get("serial_no"));
			map.put("fileName", map.get("file_name"));
			map.put("tourokuDate", formatTime(map.get("touroku_time")));
		}
		
		
		try {
			// クリックされたファイルの情報を取得
			GMap map = gazouUploadLogic.selectDownloadFile(Integer.parseInt(downloadSerialNo));
			String fileName = map.get("file_name").toString();
			byte[] fileData = (byte[]) map.get("binary_data");
			// コンテンツタイプの設定
			contentType = map.get("content_type").toString();
			// コンテンツディスポジションの設定を行います。
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode,true, fileName);
			// ファイルサイズを設定
			contentLength = (long) map.get("file_size");
			// ダウンロード用ファイルを作成します。
			// InputStreamを渡しておけば、後はStrutsがやってくれます。
			this.inputStream = new ByteArrayInputStream(fileData);
			return "success";
		} finally {
			connection.close();
		}
	}

}
