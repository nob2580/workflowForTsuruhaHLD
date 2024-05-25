package eteam.gyoumu.kaikei;

import eteam.common.EteamIO;

public class ShiwakeDataImportExtLogic extends ShiwakeDataImportLogic {
	/**
	 * 指定したファイル名がインポート対象のファイルとなるか判別します。(拡張子なしのファイルは非取込対象)
	 * @param fileName 対象のファイル名
	 * @return true:取込対象 false:取込対象でない
	 */
	@Override
	protected boolean isEnableAppendFile(String fileName) {
		boolean result = false;
		String extStr = EteamIO.getExtension(fileName);
		// 設定不問で取得対象となる拡張子(bmp/jpeg/jpg/png)ならtrue
		if( (EXTENSION_BMP.equalsIgnoreCase(extStr))
				||	(EXTENSION_JPEG.equalsIgnoreCase(extStr))
				||	(EXTENSION_JPG.equalsIgnoreCase(extStr))
				||	(EXTENSION_PNG.equalsIgnoreCase(extStr)) ){
			result = true;
			
		// 拡張子が「doc」「docx」なら「DOCDOCX」としてフラグ取得
		}else if( (EXTENSION_DOC.equalsIgnoreCase(extStr))
				||	(EXTENSION_DOCX.equalsIgnoreCase(extStr)) ){
			result = isEnableExtension(EXTENSION_SETTING_DOCDOCX);
			
		// 拡張子が「xls」「xlsx」なら「XLSXLSX」としてフラグ取得
		}else if( (EXTENSION_XLS.equalsIgnoreCase(extStr))
				||	(EXTENSION_XLSX.equalsIgnoreCase(extStr)) ){
			result = isEnableExtension(EXTENSION_SETTING_XLSXLSX);
		// ▼カスタマイズ
		// 先頭がURL情報ならresult	
		}else if(fileName.startsWith( "http://" ) || fileName.startsWith( "https://" )){
			result = true;
		// ▲ここまで
		// 上記以外なら直接拡張子を用いて拡張子使用フラグ取得
		}else{
			result = isEnableExtension(extStr);
		}
		return result;
	}
	
}
