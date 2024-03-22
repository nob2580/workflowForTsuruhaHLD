package eteam.gyoumu.shiharai;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支払依頼申請出力画面Action
 */
@Getter @Setter @ToString
public class ShiharaiYoteiSoukatsuhyoOutputAction extends EteamAbstractAction {

//＜定数＞
	/** ZIPファイル名 */
	static final String PDF_FILENAME = "shiharai_yotei_soukatsu";
	
//＜画面入力＞
	/** 計上日From */
	String keijouBiFrom ="";
	/** 計上日To */
	String keijouBiTo ="";
	/** 支払予定日From */
	String shiharaiYoteiBiFrom ="";
	/** 支払予定日To */
	String shiharaiYoteiBiTo ="";
	/** 最終承認済チェックボックス */
	String saisyuShouninZumiCheck;
	
//＜ファイルダウンロードイベント専用＞
	/** コンテンツタイプ */
	String contentType;
	/** コンテンツディスポジション */
	String contentDisposition;
	/** ダウンロードファイル用 */
	InputStream inputStream;
	
//＜部品＞
	/** 通知ロジック */
	protected TsuuchiCategoryLogic tsuuchiLogic;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysLogic;
	/** 部門ユーザー管理カテゴリロジック */
	protected BumonUserKanriCategoryLogic bumonUserLogic;
	
	/** 支払予定総括表ロジック */
	ShiharaiYoteiSoukatsuhyoXlsLogic xlsLogic;
	
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(ShiharaiYoteiSoukatsuhyoOutputAction.class);

	//＜入力チェック＞
	@Override protected void formatCheck() {
		checkDate(keijouBiFrom, "計上日From", false);
		checkDate(keijouBiTo, "計上日To", false);
		checkDate(shiharaiYoteiBiFrom, "支払予定日From", false);
		checkDate(shiharaiYoteiBiTo, "支払予定日To", false);
	}

	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			makeParts(connection);
			//戻り値を返す
			return "success";
		}
	}
	
	/**
	 * PDF帳票出力
	 * @return 結果
	 */
	public String pdfOutput() {
		
		formatCheck();
		if (errorList.size() > 0) {
			return "error";
		}
		try(EteamConnection connection = EteamConnection.connect()){
			makeParts(connection);
			
			//該当するデータがない場合
			long shiharaiIraiCountJidouHikiotoshi = xlsLogic.countShiharaiIrai(toDate(shiharaiYoteiBiFrom), toDate(shiharaiYoteiBiTo),
																				toDate(keijouBiFrom),toDate(keijouBiTo),
																				saisyuShouninZumiCheck, SHIHARAI_IRAI_HOUHOU.JIDOUHIKIOTOSHI);
			long shiharaiIraiCountFurikomi = xlsLogic.countShiharaiIrai(toDate(shiharaiYoteiBiFrom), toDate(shiharaiYoteiBiTo),
																				toDate(keijouBiFrom),toDate(keijouBiTo),
																				saisyuShouninZumiCheck, SHIHARAI_IRAI_HOUHOU.FURIKOMI);
			if(shiharaiIraiCountJidouHikiotoshi+shiharaiIraiCountFurikomi == 0){
				errorList.add("該当する期間のデータは存在しません。");
				return "error";
			}
			
			//伝票個別Excelファイル作成
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			xlsLogic.makeExcel(toDate(shiharaiYoteiBiFrom), toDate(shiharaiYoteiBiTo), toDate(keijouBiFrom), toDate(keijouBiTo), saisyuShouninZumiCheck, excelOut);
			
			//PDF変換
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			Workbook workbook = new Workbook(new ByteArrayInputStream(excelOut.toByteArray()));
			workbook.save(pdfOut, SaveFormat.PDF);
			
			contentType = ContentType.PDF;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, PDF_FILENAME  + DateFormatUtils.format(new Date(), "yyyyMMddhhmmss") + ".pdf");
			ByteArrayInputStream bArray2 = new ByteArrayInputStream(pdfOut.toByteArray()); 
			
			inputStream = bArray2;
			connection.commit();
			return "success";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * DB接続を初期化する。
	 * @param connection コネクション
	 */
	protected void makeParts(EteamConnection connection){
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		
		xlsLogic = EteamContainer.getComponent(ShiharaiYoteiSoukatsuhyoXlsLogic.class, connection);
	}
}
