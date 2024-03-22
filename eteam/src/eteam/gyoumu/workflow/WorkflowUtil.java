package eteam.gyoumu.workflow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.aspose.cells.PageSetup;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import eteam.common.EteamIO;
import eteam.common.EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;

/**
 * WFのユーティリティ（WF本体と拠点の共通）
 */
public class WorkflowUtil {
	
	/**
	 * メール件名生成
	 * @param mailTsuuchiKbn メール通知区分（内部コード）
	 * @param denpyouShubetsu 伝票種別(区分じゃなくて名称)
	 * @param shoriKengen 処理権限名(承認時のみ
	 * @return メール件名		
	 */
	public static String getMailTitle(String mailTsuuchiKbn, String denpyouShubetsu, String shoriKengen) {
		String systemName = "【" + EteamSettingInfo.getSettingInfo(Key.MAIL_TSUUCHI_SYSTEM_NAME) +  "】";
		denpyouShubetsu = StringUtils.isEmpty(denpyouShubetsu) ? "" : "【" + denpyouShubetsu +  "】";
		shoriKengen = StringUtils.isEmpty(shoriKengen) ? "" : "【" + shoriKengen +  "】";

		String subject = null;
		switch (mailTsuuchiKbn) {
			case MAIL_TSUUCHI_KBN.SHOUNIN_MACHI:           // 承認待ち
				subject = String.format("%s%s%s処理待ちの伝票があります。", systemName, denpyouShubetsu, shoriKengen);
				break;
			case MAIL_TSUUCHI_KBN.JI_DENPYOU_SHOUNIN:      // 自分の伝票が最終承認
			case MAIL_TSUUCHI_KBN.KANREN_DENPYOU_SHOUNIN:  // 自分関連の伝票が最終承認
				subject = String.format("%s%s完了した伝票があります。", systemName, denpyouShubetsu);
				break;
			case MAIL_TSUUCHI_KBN.JI_DENPYOU_SASHIMODOSHI: // 自分の伝票が差戻し
				subject = String.format("%s%s差戻し伝票があります。", systemName, denpyouShubetsu);
				break;
			case MAIL_TSUUCHI_KBN.JI_DENPYOU_HININ:        // 自分の伝票が否認
			case MAIL_TSUUCHI_KBN.KANREN_DENPYOU_HININ:    // 自分関連の伝票が否認
				subject = String.format("%s%s否認された伝票があります。", systemName, denpyouShubetsu);
				break;
			case MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI:      // 合議部署承認待ち
				subject = String.format("%s%s合議対象の伝票があります。", systemName, denpyouShubetsu);
				break;
			default:
				throw new InvalidParameterException("mailTsuuchiKbn:" + mailTsuuchiKbn + "は不正");
		}
		
		return subject;
	}

	/**
	 * メール件名生成
	 * @param mailTsuuchiKbn メール通知区分（内部コード）
	 * @return メール件名
	 */
	public static String getMeilText(String mailTsuuchiKbn) {
		String text = null;

		switch (mailTsuuchiKbn) {
			case MAIL_TSUUCHI_KBN.SHOUNIN_MACHI:           // 承認待ち
				text = "伝票が申請されました。";
				break;
			case MAIL_TSUUCHI_KBN.JI_DENPYOU_SHOUNIN:      // 自分の伝票が最終承認
				text = "申請した伝票が完了しました。";
				break;
			case MAIL_TSUUCHI_KBN.KANREN_DENPYOU_SHOUNIN:  // 自分関連の伝票が最終承認
				text = "自分が承認ルートに含まれる伝票が完了しました。";
				break;
			case MAIL_TSUUCHI_KBN.JI_DENPYOU_SASHIMODOSHI: // 自分の伝票が差戻し
				text = "申請した伝票が差戻しされました。";
				break;
			case MAIL_TSUUCHI_KBN.JI_DENPYOU_HININ:        // 自分の伝票が否認
				text = "申請した伝票が否認されました。";
				break;
			case MAIL_TSUUCHI_KBN.KANREN_DENPYOU_HININ:    // 自分関連の伝票が否認
				text = "自分が承認ルートに含まれる伝票が否認されました。";
				break;
			case MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI:      // 合議部署承認待ち
				text = "伝票が合議状態になりました。";
				break;
			default:
				throw new InvalidParameterException("mailTsuuchiKbn:" + mailTsuuchiKbn + "は不正");
		}
		return text;
	}
	
	/**
	 * 追加ファイル名の変更。
	 * 既存ファイル・追加ファイルの中に同名がいたら、hoge.jpg → hoge_##.jpgの用に変更
	 * UTF-8のノーブレークスペース(&nbsp)が存在した場合、通常半角スペースに変換
	 * 『,(半角カンマ)』が存在した場合、『、(読点)』に変換
	 * UTF-8の波ダッシュ(～)がある場合Shift-JISの波形に変換
	 * Windowsのファイル名使用不可能文字『\/:*?"<>|』がある場合大文字に変換
	 * @param denpyouId 伝票ID
	 * @param addFileName 追加ファイル名
	 * @param curs 今のファイル名
	 * @return 追加ファイル名(改)
	 */
	public static String[] renameFile(String denpyouId, String[] addFileName, String[] curs) {
		if (addFileName == null || addFileName.length == 0 )
		{
			return addFileName;
		}
		String[] news = ArrayUtils.clone(addFileName);
		
		for(int i = 0; i < news.length; i++) {
			news[i] = news[i]
				.replace("\u00a0", " ") // UTF-8のノーブレークスペース(&nbsp)を半角スペースに変換
				.replace(",", "、") // 半角カンマを読点に変換
				.replace("\u301c", "～") // UTF-8の波ダッシュ(～)をShift-JISの波形に変換
				.replace("\u00a5", "￥") // UTF-8の円記号を変換
				.replace("\uffe5", "￥") // UTF-8のバックスラッシュを変換
				.replace("\\", "￥") // （以下Windowsファイル名使用不可能文字の全角化）バックスラッシュを変換・全角化
				.replace("/", "／") // スラッシュを全角化
				.replace(":", "：") // コロンを全角化
				.replace("*", "＊") // アスタリスクを全角化
				.replace("?", "？") // クエスチョンマークを全角化
				.replace("\"", "”") // ダブルクオーテーションを全角化
				.replace("%22", "”") // URLエンコードされたダブルクオーテーションを全角化
				.replace("<", "＜") // 不等号(小)を全角化
				.replace(">", "＞") // 不等号(大)を全角化
				.replace("|", "｜") // パイプを全角化
				.replace("%", "％") // （以下、URL禁則文字）パーセントを全角化
				.replace("#", "＃"); // ハッシュを全角化
		}
		
		//追加ファイルが既存・追加両方の中に重複しているか調査
		Map<String, Integer> fileNameCount = new HashMap<String, Integer>() {
			@Override
			public Integer get(Object key) {
				return super.get(((String)key).toLowerCase());
			}
			@Override
			public Integer put(String key, Integer value) {
				return super.put(key.toLowerCase(), value);
			}
		};
		for(int i = 0; i < news.length; i++) {
			Integer c = fileNameCount.get(news[i]);
			fileNameCount.put(news[i], (c == null) ? 1 : c+1);
		}
		for(int i = 0; i < curs.length; i++) {
			Integer c = fileNameCount.get(curs[i]);
			fileNameCount.put(curs[i], (c == null) ? 1 : c+1);
		}
		
		//追加ファイルの内、重複があるやつだけリネーム
		for(int i = 0; i < news.length; i++) {
			if (fileNameCount.get(news[i]) >= 2) {
				String base = EteamIO.getBase(news[i]);
				String ext = EteamIO.getExtension(news[i]);
				for (int edano = 1; true; edano++) {
					String shiftName = base + "_" + String.format("%02d", edano);
					if (! StringUtils.isEmpty(ext))
					{
						shiftName = shiftName + "." + ext;
					}
					boolean found = false;
					for (String s : curs) {
						if (shiftName.equalsIgnoreCase(s))
						{
							found = true;
						}
					}
					for (int j = 0; j < i; j++) {
						if (shiftName.equalsIgnoreCase(news[j]))
						{
							found = true;
						}
					}
					if (! found) {
						news[i] = shiftName;
						break;
					}
				}
			}
		}
		
		return news;
	}
	
	/**
	 * 帳票のEXCELからPDFの変換
	 * @param excelOut EXCEL
	 * @param pdfOut PDF
	 * @param denpyouId 伝票ID
	 * @param kianBangou 起案番号
	 */
	public static void excel2Pdf(ByteArrayOutputStream excelOut, ByteArrayOutputStream pdfOut, String denpyouId, String kianBangou) {
		try {
			if (kianBangou == null)
			{
				kianBangou = "";
			}
			Workbook workbook = new Workbook(new ByteArrayInputStream(excelOut.toByteArray()));
			WorksheetCollection sheets = workbook.getWorksheets();
			for(int i = 0; i < sheets.getCount(); i++){
				Worksheet sheet = sheets.get(i);
				PageSetup pageSetup = sheet.getPageSetup();
				pageSetup.setFooter(0, denpyouId); //0=Left
				pageSetup.setFooter(1, "&P / &N ページ"); //1=Center Section　&P=Current page number　&N=Page count　
				pageSetup.setAutoFirstPageNumber(true); //2=Right
				pageSetup.setFooter(2, kianBangou); //3=Right
			}
			PdfSaveOptions saveOptions = new PdfSaveOptions(SaveFormat.PDF);
			saveOptions.setAllColumnsInOnePagePerSheet(true);
			workbook.save(pdfOut, saveOptions);
		} catch (Exception e) {
			throw new RuntimeException("EXCEL-PDF変換でエラー", e);
		}
	}
}
