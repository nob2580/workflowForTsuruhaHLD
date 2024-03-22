package eteam.gyoumu.kaikei;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（申請内容） */
@Getter @Setter @ToString
public class ShiharaiIraiCSVUploadDenpyouInfo implements Serializable {
	/** UID */
	protected static final long serialVersionUID = 1L;

	/** csv内のnumbering */
	String number;
	
	/** 伝票No. */
	String denpyouNo;
	/** HF1コード */
	String hf1Cd;
	/** HF2コード */
	String hf2Cd;
	/** HF3コード */
	String hf3Cd;
	/** HF4コード */
	String hf4Cd;
	/** HF5コード */
	String hf5Cd;
	/** HF6コード */
	String hf6Cd;
	/** HF7コード */
	String hf7Cd;
	/** HF8コード */
	String hf8Cd;
	/** HF9コード */
	String hf9Cd;
	/** HF10コード */
	String hf10Cd;
	/** 取引先コード */
	String torihikisakiCd;
	/** (一見先の場合のみ表示)支払先名 */
	String ichigensakiTorihikisakiName = "";
	/** 支払種別 */
	String shihiaraiShubetsu;
	/** 計上日 */
	String keijoubi;
	/** 支払予定日 */
	String yoteibi;
	/** EDI */
	String edi;
	/** 振込先銀行コード */
	String furikomiGinkouCd;
	/** 振込先支店コード */
	String furikomiGinkouShitenCd;
	/** 口座種別 */
	String yokinShubetsu;
	/** 口座番号 */
	String kouzaBangou;
	/** 口座名義人 */
	String kouzaMeiginin;
	/** 手数料 */
	String tesuuryou;
	/** 控除金額 */
	String manekinGensen;
	/** 発生種別 */
	String hasseiShubetsu;

	// インボイス系プロパティ、一旦仮置き
	/** 入力方式 */
	//String nyuryokuHoushiki = "0";
	/** 事業者区分 */
	String jigyoushaKbn = "0";
	/** 事業者登録番号 */
	String jigyoushaNo = "";
	/** インボイス対応伝票 */
	String invoiceDenpyou = "1";
	
	/** CSVファイル情報（申請内容明細）リスト */
	ArrayList<ShiharaiIraiCSVUploadMeisaiInfo> meisaiList = new ArrayList<>();
	
	/** 起案伝票ID */
	String kianDenpyouId;

	/** 伝票ID(処理済になってから) */
	String denpyouId;
}