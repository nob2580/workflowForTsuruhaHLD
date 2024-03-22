package eteam.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.common.EteamConst.ContentType;

/**
 * QRコード作成Action
 */
@Getter @Setter @ToString
public class QrCodeSakuseiAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** QRテキスト */
	String qrText;

//＜画面入力以外＞
	/** ダウンロードファイル用 */
	protected InputStream inputStream;
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** コンテンツのサイズ */
	protected long contentLength;

//＜入力チェック＞
	@Override protected void formatCheck() {
		checkString(qrText, 1, 255, "QRテキスト", true);
	}
	@Override protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目							EVENT1～の必須フラグ
			{qrText ,"QRテキスト"		,"2"}
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){

		//1.入力チェック
		formatCheck();
		hissuCheck(1); // EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))

		//2.データ存在チェック なし
		//3.アクセス可能チェック なし

		//4.処理（初期表示）
		// パラメータ「QRテキスト」を表すQRコード画像PNGを作成
		ByteArrayOutputStream out = QRCode.from(qrText).to(ImageType.PNG).stream();
		contentType = ContentType.PNG;
		int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
		contentDisposition = EteamCommon.contentDisposition(browserCode, true, "qrcode.png");
		contentLength = out.size();
		inputStream = new ByteArrayInputStream(out.toByteArray());

		//5.戻り値を返す
		return "success";
	}
}
