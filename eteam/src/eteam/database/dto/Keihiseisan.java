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
 * 経費精算DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Keihiseisan implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Keihiseisan() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Keihiseisan(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.karibaraiDenpyouId = m.get("karibarai_denpyou_id");
		this.karibaraiOn = m.get("karibarai_on");
		this.karibaraiMishiyouFlg = m.get("karibarai_mishiyou_flg");
		this.dairiflg = m.get("dairiflg");
		this.keijoubi = m.get("keijoubi");
		this.shiharaibi = m.get("shiharaibi");
		this.shiharaikiboubi = m.get("shiharaikiboubi");
		this.shiharaihouhou = m.get("shiharaihouhou");
		this.hontaiKingakuGoukei = m.get("hontai_kingaku_goukei");
		this.shouhizeigakuGoukei = m.get("shouhizeigaku_goukei");
		this.shiharaiKingakuGoukei = m.get("shiharai_kingaku_goukei");
		this.houjinCardRiyouKingaku = m.get("houjin_card_riyou_kingaku");
		this.kaishaTehaiKingaku = m.get("kaisha_tehai_kingaku");
		this.sashihikiShikyuuKingaku = m.get("sashihiki_shikyuu_kingaku");
		this.hf1Cd = m.get("hf1_cd");
		this.hf1NameRyakushiki = m.get("hf1_name_ryakushiki");
		this.hf2Cd = m.get("hf2_cd");
		this.hf2NameRyakushiki = m.get("hf2_name_ryakushiki");
		this.hf3Cd = m.get("hf3_cd");
		this.hf3NameRyakushiki = m.get("hf3_name_ryakushiki");
		this.hf4Cd = m.get("hf4_cd");
		this.hf4NameRyakushiki = m.get("hf4_name_ryakushiki");
		this.hf5Cd = m.get("hf5_cd");
		this.hf5NameRyakushiki = m.get("hf5_name_ryakushiki");
		this.hf6Cd = m.get("hf6_cd");
		this.hf6NameRyakushiki = m.get("hf6_name_ryakushiki");
		this.hf7Cd = m.get("hf7_cd");
		this.hf7NameRyakushiki = m.get("hf7_name_ryakushiki");
		this.hf8Cd = m.get("hf8_cd");
		this.hf8NameRyakushiki = m.get("hf8_name_ryakushiki");
		this.hf9Cd = m.get("hf9_cd");
		this.hf9NameRyakushiki = m.get("hf9_name_ryakushiki");
		this.hf10Cd = m.get("hf10_cd");
		this.hf10NameRyakushiki = m.get("hf10_name_ryakushiki");
		this.hosoku = m.get("hosoku");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.invoiceDenpyou = m.get("invoice_denpyou");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;

	/** 仮払伝票ID */
	public String karibaraiDenpyouId;

	/** 仮払申請フラグ */
	public String karibaraiOn;

	/** 仮払金未使用フラグ */
	public String karibaraiMishiyouFlg;

	/** 代理フラグ */
	public String dairiflg;

	/** 計上日 */
	public Date keijoubi;

	/** 支払日 */
	public Date shiharaibi;

	/** 支払希望日 */
	public Date shiharaikiboubi;

	/** 支払方法 */
	public String shiharaihouhou;

	/** 本体金額合計 */
	public BigDecimal hontaiKingakuGoukei;

	/** 消費税額合計 */
	public BigDecimal shouhizeigakuGoukei;

	/** 支払金額合計 */
	public BigDecimal shiharaiKingakuGoukei;

	/** 内法人カード利用合計 */
	public BigDecimal houjinCardRiyouKingaku;

	/** 会社手配合計 */
	public BigDecimal kaishaTehaiKingaku;

	/** 差引支給金額 */
	public BigDecimal sashihikiShikyuuKingaku;

	/** HF1コード */
	public String hf1Cd;

	/** HF1名（略式） */
	public String hf1NameRyakushiki;

	/** HF2コード */
	public String hf2Cd;

	/** HF2名（略式） */
	public String hf2NameRyakushiki;

	/** HF3コード */
	public String hf3Cd;

	/** HF3名（略式） */
	public String hf3NameRyakushiki;

	/** HF4コード */
	public String hf4Cd;

	/** HF4名（略式） */
	public String hf4NameRyakushiki;

	/** HF5コード */
	public String hf5Cd;

	/** HF5名（略式） */
	public String hf5NameRyakushiki;

	/** HF6コード */
	public String hf6Cd;

	/** HF6名（略式） */
	public String hf6NameRyakushiki;

	/** HF7コード */
	public String hf7Cd;

	/** HF7名（略式） */
	public String hf7NameRyakushiki;

	/** HF8コード */
	public String hf8Cd;

	/** HF8名（略式） */
	public String hf8NameRyakushiki;

	/** HF9コード */
	public String hf9Cd;

	/** HF9名（略式） */
	public String hf9NameRyakushiki;

	/** HF10コード */
	public String hf10Cd;

	/** HF10名（略式） */
	public String hf10NameRyakushiki;

	/** 補足 */
	public String hosoku;

	/** 登録ユーザーID */
	public String tourokuUserId;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新ユーザーID */
	public String koushinUserId;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** インボイス対応伝票 */
	public String invoiceDenpyou;

	/** その他項目map */
	public GMap map;
}