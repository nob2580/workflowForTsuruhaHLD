package eteam.gyoumu.kaikei;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** CSVファイル情報（申請内容） */
@Getter @Setter @ToString
public class SeikyuushoBaraiCSVUploadDenpyouInfo implements Serializable {
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
	/** 支払期限 */
	String shiharaiKigen;
	/** 伝票ID(処理済になってから) */
	String denpyouId;
	/** CSVファイル情報（申請内容明細）リスト */
	ArrayList<SeikyuushoBaraiCSVUploadMeisaiInfo> meisaiList = new ArrayList<>();
	/** 起案伝票ID */
	String kianDenpyouId;
	/** 入力方式 */
	String nyuryokuHoushiki;
	/** インボイス対応伝票 */
	String invoiceDenpyou;
}