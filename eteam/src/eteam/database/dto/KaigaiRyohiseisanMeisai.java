package eteam.database.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 海外旅費精算明細DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KaigaiRyohiseisanMeisai implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KaigaiRyohiseisanMeisai() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KaigaiRyohiseisanMeisai(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.kaigaiFlg = m.get("kaigai_flg");
		this.denpyouEdano = m.get("denpyou_edano");
		this.kikanFrom = m.get("kikan_from");
		this.kikanTo = m.get("kikan_to");
		this.kyuujitsuNissuu = m.get("kyuujitsu_nissuu");
		this.shubetsuCd = m.get("shubetsu_cd");
		this.shubetsu1 = m.get("shubetsu1");
		this.shubetsu2 = m.get("shubetsu2");
		this.hayaFlg = m.get("haya_flg");
		this.yasuFlg = m.get("yasu_flg");
		this.rakuFlg = m.get("raku_flg");
		this.koutsuuShudan = m.get("koutsuu_shudan");
		this.shouhyouShoruiHissuFlg = m.get("shouhyou_shorui_hissu_flg");
		this.ryoushuushoSeikyuushoTouFlg = m.get("ryoushuusho_seikyuusho_tou_flg");
		this.naiyou = m.get("naiyou");
		this.bikou = m.get("bikou");
		this.oufukuFlg = m.get("oufuku_flg");
		this.houjinCardRiyouFlg = m.get("houjin_card_riyou_flg");
		this.kaishaTehaiFlg = m.get("kaisha_tehai_flg");
		this.jidounyuuryokuFlg = m.get("jidounyuuryoku_flg");
		this.nissuu = m.get("nissuu");
		this.heishuCd = m.get("heishu_cd");
		this.rate = m.get("rate");
		this.gaika = m.get("gaika");
		this.currencyUnit = m.get("currency_unit");
		this.tanka = m.get("tanka");
		this.suuryouNyuryokuType = m.get("suuryou_nyuryoku_type");
		this.suuryou = m.get("suuryou");
		this.suuryouKigou = m.get("suuryou_kigou");
		this.meisaiKingaku = m.get("meisai_kingaku");
		this.zeiKubun = m.get("zei_kubun");
		this.kazeiFlg = m.get("kazei_flg");
		this.icCardNo = m.get("ic_card_no");
		this.icCardSequenceNo = m.get("ic_card_sequence_no");
		this.himodukeCardMeisai = m.get("himoduke_card_meisai");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.shiharaisakiName = m.get("shiharaisaki_name");
		this.jigyoushaKbn = m.get("jigyousha_kbn");
		this.zeinukiKingaku = m.get("zeinuki_kingaku");
		this.shouhizeigaku = m.get("shouhizeigaku");
		this.zeigakuFixFlg = m.get("zeigaku_fix_flg");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;

	/** 海外フラグ */
	public String kaigaiFlg;

	/** 伝票枝番号 */
	public int denpyouEdano;

	/** 期間開始日 */
	public Date kikanFrom;

	/** 期間終了日 */
	public Date kikanTo;

	/** 休日日数 */
	public BigDecimal kyuujitsuNissuu;

	/** 種別コード */
	public String shubetsuCd;

	/** 種別１ */
	public String shubetsu1;

	/** 種別２ */
	public String shubetsu2;

	/** 早フラグ */
	public String hayaFlg;

	/** 安フラグ */
	public String yasuFlg;

	/** 楽フラグ */
	public String rakuFlg;

	/** 交通手段 */
	public String koutsuuShudan;

	/** 証憑書類必須フラグ */
	public String shouhyouShoruiHissuFlg;

	/** 領収書・請求書等フラグ */
	public String ryoushuushoSeikyuushoTouFlg;

	/** 内容（旅費精算） */
	public String naiyou;

	/** 備考（旅費精算） */
	public String bikou;

	/** 往復フラグ */
	public String oufukuFlg;

	/** 法人カード利用フラグ */
	public String houjinCardRiyouFlg;

	/** 会社手配フラグ */
	public String kaishaTehaiFlg;

	/** 自動入力フラグ */
	public String jidounyuuryokuFlg;

	/** 日数 */
	public BigDecimal nissuu;

	/** 幣種コード */
	public String heishuCd;

	/** レート */
	public BigDecimal rate;

	/** 外貨金額 */
	public BigDecimal gaika;

	/** 通貨単位 */
	public String currencyUnit;

	/** 単価 */
	public BigDecimal tanka;

	/** 数量入力タイプ */
	public String suuryouNyuryokuType;

	/** 数量 */
	public BigDecimal suuryou;

	/** 数量記号 */
	public String suuryouKigou;

	/** 明細金額 */
	public BigDecimal meisaiKingaku;

	/** 税区分 */
	public String zeiKubun;

	/** 課税フラグ */
	public String kazeiFlg;

	/** ICカード番号 */
	public String icCardNo;

	/** ICカードシーケンス番号 */
	public String icCardSequenceNo;

	/** 紐付元カード明細 */
	public Long himodukeCardMeisai;

	/** 登録ユーザーID */
	public String tourokuUserId;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新ユーザーID */
	public String koushinUserId;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** 支払先名 */
	public String shiharaisakiName;

	/** 事業者区分 */
	public String jigyoushaKbn;

	/** 税抜金額 */
	public BigDecimal zeinukiKingaku;

	/** 消費税額 */
	public BigDecimal shouhizeigaku;
	
	/** 税額修正フラグ */
	public String zeigakuFixFlg;

	/** その他項目map */
	public GMap map;
}