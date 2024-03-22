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
 * FB抽出DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class Fb implements IEteamDTO {

	/**
	 * default constructor
	 */
	public Fb() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public Fb(GMap m) {
		this.serialNo = m.get("serial_no");
		this.denpyouId = m.get("denpyou_id");
		this.userId = m.get("user_id");
		this.fbStatus = m.get("fb_status");
		this.tourokuTime = m.get("touroku_time");
		this.koushinTime = m.get("koushin_time");
		this.shubetsuCd = m.get("shubetsu_cd");
		this.cdKbn = m.get("cd_kbn");
		this.kaishaCd = m.get("kaisha_cd");
		this.kaishaNameHankana = m.get("kaisha_name_hankana");
		this.furikomiDate = m.get("furikomi_date");
		this.motoKinyuukikanCd = m.get("moto_kinyuukikan_cd");
		this.motoKinyuukikanNameHankana = m.get("moto_kinyuukikan_name_hankana");
		this.motoKinyuukikanShitenCd = m.get("moto_kinyuukikan_shiten_cd");
		this.motoKinyuukikanShitenNameHankana = m.get("moto_kinyuukikan_shiten_name_hankana");
		this.motoYokinShumokuCd = m.get("moto_yokin_shumoku_cd");
		this.motoKouzaBangou = m.get("moto_kouza_bangou");
		this.sakiKinyuukikanCd = m.get("saki_kinyuukikan_cd");
		this.sakiKinyuukikanNameHankana = m.get("saki_kinyuukikan_name_hankana");
		this.sakiKinyuukikanShitenCd = m.get("saki_kinyuukikan_shiten_cd");
		this.sakiKinyuukikanShitenNameHankana = m.get("saki_kinyuukikan_shiten_name_hankana");
		this.sakiYokinShumokuCd = m.get("saki_yokin_shumoku_cd");
		this.sakiKouzaBangou = m.get("saki_kouza_bangou");
		this.sakiKouzaMeigiKana = m.get("saki_kouza_meigi_kana");
		this.kingaku = m.get("kingaku");
		this.shinkiCd = m.get("shinki_cd");
		this.kokyakuCd1 = m.get("kokyaku_cd1");
		this.furikomiKbn = m.get("furikomi_kbn");
		this.map = m;
	}

	/** シリアル番号 */
	public long serialNo;

	/** 伝票ID */
	public String denpyouId;

	/** ユーザーID */
	public String userId;

	/** FB抽出状態 */
	public String fbStatus;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** 種別コード */
	public String shubetsuCd;

	/** コード区分 */
	public String cdKbn;

	/** 会社コード */
	public String kaishaCd;

	/** 会社名（半角カナ） */
	public String kaishaNameHankana;

	/** 振込日 */
	public Date furikomiDate;

	/** 振込元金融機関コード */
	public String motoKinyuukikanCd;

	/** 振込元金融機関名（半角カナ） */
	public String motoKinyuukikanNameHankana;

	/** 振込元金融機関支店コード */
	public String motoKinyuukikanShitenCd;

	/** 振込元金融機関支店名（半角カナ） */
	public String motoKinyuukikanShitenNameHankana;

	/** 振込元預金種目コード */
	public String motoYokinShumokuCd;

	/** 振込元口座番号 */
	public String motoKouzaBangou;

	/** 振込先金融機関銀行コード */
	public String sakiKinyuukikanCd;

	/** 振込先金融機関名（半角カナ） */
	public String sakiKinyuukikanNameHankana;

	/** 振込先金融機関支店コード */
	public String sakiKinyuukikanShitenCd;

	/** 振込先金融機関支店名（半角カナ） */
	public String sakiKinyuukikanShitenNameHankana;

	/** 振込先預金種目コード */
	public String sakiYokinShumokuCd;

	/** 振込先口座番号 */
	public String sakiKouzaBangou;

	/** 振込先口座名義（半角カナ） */
	public String sakiKouzaMeigiKana;

	/** 金額 */
	public BigDecimal kingaku;

	/** 新規コード */
	public String shinkiCd;

	/** 顧客コード１ */
	public String kokyakuCd1;

	/** 振込区分 */
	public String furikomiKbn;

	/** その他項目map */
	public GMap map;
}