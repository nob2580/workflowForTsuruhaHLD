package eteam.gyoumu.shiharai;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamNaibuCodeSetting.SHUTSURYOKU_JOUKYOU;
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
public class ShiharaiIraishoShutsuryokuAction extends EteamAbstractAction {

//＜定数＞
	/** 出力状況 */
// static final String SHUTSURYOKU_JOUKYOU = "shutsuryoku_joukyou";
	/** 支払種別 */
	static final String SHIHARAI_SHUBETSU = "shiharai_irai_shubetsu";
	/** 承認状況 */
	static final String SHOUNIN_JOUKYOU = "shounin_joukyou";
	/** ZIPファイル名 */
	static final String PDF_FILENAME = "shiharai_irai";
	
//＜画面入力＞
	/** 出力状況 */
	String shutsuryokuJoukyou="";
	/** 支払種別 */
	String shiharaiShubetsu="";
	/** 承認状況 */
	String shouninJoukyou="";
	/** 計上日From */
	String keijouBiFrom="";
	/** 計上日To */
	String keijouBiTo="";
	/** 支払日From */
	String shiharaiBiFrom="";
	/** 支払日To */
	String shiharaiBiTo="";
	/** 伝票番号From */
	String denpyouNoFrom="";
	/** 伝票番号To */
	String denpyouNoTo="";
	/** 起票部門コード */
	String kihyouBumonCd="";
	/** 起票部門名 */
	String kihyouBumonName="";
	/** ユーザーID */
	String userId="";
	/** 社員コード */
	String shainNo="";
	/** ユーザー姓名 */
	String userName="";
	/** 取引先コード */
	String torihikisakiCd="";
	/** 取引先名 */
	String torihikisakiName="";
	/** 帳票チェックボックス */
	String sentaku;
	
//＜出力＞
	/** 出力状況 */
	List<GMap> shutsuryokuJoukyouList;
	/** 支払種別 */
	List<GMap> shiharaiShubetsuList;
	/** 承認状況 */
	List<GMap> shouninJoukyouList;
	/** 検索結果リスト */
	List<GMap> list = new ArrayList<GMap>();
	/** 全選択させる */
	boolean allCheck;
	
//＜ファイルダウンロードイベント専用＞
	/** ファイル名 */
	String fileName;
	/** コンテンツタイプ */
	String contentType;
	/** コンテンツディスポジション */
	String contentDisposition;
	/** ダウンロードファイル用 */
	InputStream inputStream;
	/** PDFファイル用*/
	protected FileOutputStream fos;
	
//＜部品＞
	/** 通知ロジック */
	protected TsuuchiCategoryLogic tsuuchiLogic;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysLogic;
	/** 部門ユーザー管理カテゴリロジック */
	protected BumonUserKanriCategoryLogic bumonUserLogic;
	/** 社員別支給一覧ロジック */
	protected ShiharaiIraishoShutsuryokuLogic myLogic;
	/** 支払依頼一覧Xlsロジック */
	ShiharaiIraiIchiranXlsLogic xlsLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(ShiharaiIraishoShutsuryokuAction.class);

	//＜入力チェック＞
	@Override protected void formatCheck() {
		checkDate(keijouBiFrom, "計上日From", false);
		checkDate(keijouBiTo, "計上日To", false);
		checkDate(shiharaiBiFrom, "支払日From", false);
		checkDate(shiharaiBiTo, "支払日To", false);
		checkNumber(denpyouNoFrom, 8,8, "伝票番号From", false);
		checkNumber(denpyouNoTo, 8,8, "伝票番号To", false);
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
			
			//初期表示状態
			setDefault(connection);
			kensaku();
			
			//戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku() {
		formatCheck();
		if (errorList.size() > 0) {
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()) {
			makeParts(connection);
			
			list = myLogic.loadShiharaiIraishoShutsuryoku(
					userId, 
					shutsuryokuJoukyou, 
					shiharaiShubetsu, 
					shouninJoukyou, 
					toDate(keijouBiFrom), 
					toDate(keijouBiTo), 
					toDate(shiharaiBiFrom), 
					toDate(shiharaiBiTo), 
					denpyouNoFrom, 
					denpyouNoTo, 
					kihyouBumonCd, 
					torihikisakiCd,
					getUser());
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
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
			
			//出力伝票がない場合エラー
			if (sentaku == null) {
				errorList.add("伝票を選択してください。");
				return "error";
			}
			
			//出力伝票
			String[] denpyouIdArr =  sentaku.split(", ");
			
			//伝票個別Excelファイル作成
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			xlsLogic.makeExcel(denpyouIdArr, excelOut);
			
			//PDF変換
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			Workbook workbook = new Workbook(new ByteArrayInputStream(excelOut.toByteArray()));
			workbook.save(pdfOut, SaveFormat.PDF);
			
			contentType = ContentType.PDF;
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, PDF_FILENAME  + DateFormatUtils.format(new Date(), "yyyyMMddhhmmss") + ".pdf");

			ByteArrayInputStream bArray2 = new ByteArrayInputStream(pdfOut.toByteArray()); 
			inputStream = bArray2;

			//伝票ID毎にPDFを処理する。
			for (int i = 0; i < denpyouIdArr.length; i++) {
				myLogic.updateShutsuryokuFlg(denpyouIdArr[i], getUser().getLoginUserId());
			}

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
		myLogic = EteamContainer.getComponent(ShiharaiIraishoShutsuryokuLogic.class, connection);
		xlsLogic = EteamContainer.getComponent(ShiharaiIraiIchiranXlsLogic.class, connection);
		
		shutsuryokuJoukyouList = sysLogic.loadNaibuCdSetting("shutsuryoku_joukyou");
		shiharaiShubetsuList = sysLogic.loadNaibuCdSetting(SHIHARAI_SHUBETSU);
		shouninJoukyouList = sysLogic.loadNaibuCdSetting(SHOUNIN_JOUKYOU);
	}

	/**
	 * 権限による検索条件領域の表示設定
	 * @param connection      コネクション
	 */
	protected void setDefault(EteamConnection connection) {
		// 一般ユーザーの場合
		if(getUser().getGyoumuRoleId() == null) {
			userId = getUser().getSeigyoUserId();
			shainNo = getUser().getShainNo();
			userName = getUser().getDisplayUserName();
			
			//出力状況の初期表示は未印刷にセット
			if (shutsuryokuJoukyou.equals("")) {
				shutsuryokuJoukyou = SHUTSURYOKU_JOUKYOU.MI_INSATSU;
			}
			
			//全選択
			allCheck = true;
		}
	}
}
