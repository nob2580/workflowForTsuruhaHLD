package eteam.gyoumu.ic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import eteam.base.EteamAbstractAction;
import eteam.common.Env;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamIO;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import lombok.Getter;

/**
 * スマート精算のEXE一式のダウンロード
 */
public class SmartSeisanAction extends EteamAbstractAction{

//ダウンロード用の
	/** コンテンツタイプ */
	@Getter
	protected String contentType;
	/** コンテンツディスポジション */
	@Getter
	protected String contentDisposition;
	/** ダウンロードファイル用 */
	@Getter
	protected InputStream inputStream;
	/** ファイルサイズ */
	@Getter
	protected long contentLength;

//初期化
	/**
	 * ダウンロードイベント
	 * @return 処理結果
	 */
	public String download(){
		//ZIP作る
		ByteArrayOutputStream by = new ByteArrayOutputStream();
		ZipOutputStream zout = new ZipOutputStream(by);
		makeZip(zout);
		
		//ダウンロード用変数にセット
		int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
		contentType = ContentType.ZIP;
		contentDisposition = EteamCommon.contentDisposition(browserCode, true, "SmartSeisan.zip");
		inputStream = new ByteArrayInputStream(by.toByteArray());
		contentLength = by.size();
		
		//おしまい
		return "success";
	}
	
	/**
	 * ZIPにc:\eteam\web\files\smart_seisan 配下のファイル書き込む
	 * @param zout ZIP
	 */
	protected void makeZip(ZipOutputStream zout){
		try {
			//ZIP内の動的可変部分取得
			String url = EteamSettingInfo.getSettingInfo(Key.SHOTODOKE_URL);
			
			//c:\eteam\web\files\smart_seisanのファイル一式
			File dir = new File("c:/eteam/web/files/smart_seisan");
			File[] files = dir.listFiles();
			if(files == null){
				throw new RuntimeException("ZIP用のファイルがない");
			}
			
			//文字コードセット
			zout.setEncoding(Encoding.MS932);
			
			//吐き出し設定値
			String isSpit = Env.smartSeisanSpitOut() ? "true" : "false";
			
			//ファイル１つずつ書き込み
			for(File file : files){
				if(file.isFile()){
					ZipEntry target = new ZipEntry(file.getName());
					zout.putNextEntry(target);
					
					//configファイルはURLとか入っているので動的に作る
					if(file.getName().equals("SmartSeisanGui.exe.config")){
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
						PrintWriter w = new PrintWriter(new OutputStreamWriter(bout, Encoding.MS932));
						w.println("<?xml version=\"1.0\"?>");
						w.println("<configuration>");
						w.println("<startup><supportedRuntime version=\"v4.0\" sku=\".NETFramework,Version=v4.5\"/></startup>");
						w.println("  <appSettings>");
						w.println("    <add key=\"Base Url\" value=\"<URL>\"/>".replace("<URL>", url));
						w.println("    <add key=\"Login Url\" value=\"ic_login\" />");
						w.println("    <add key=\"HistsPost Url\" value=\"ic_hists\" />");
						w.println("    <add key=\"Spit Out\" value=\"<HAKIDASHI>\" />".replace("<HAKIDASHI>", isSpit));
						w.println("  </appSettings>");
						w.println("</configuration>");
						w.close();
						zout.write(bout.toByteArray());
						zout.closeEntry();
					//その他のファイルはバイナリコピー
					}else{
						zout.write(EteamIO.readFile(file.getAbsolutePath()));
						zout.closeEntry();
					}
				}
			}
		}catch(IOException e){
			throw new RuntimeException("ZIP作成中にエラー発生", e);
		}finally{
			try{
				zout.close();
			}catch(IOException e){
				throw new RuntimeException("ZIP閉中にエラー発生", e);
			}
		}
	}

	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}
}
