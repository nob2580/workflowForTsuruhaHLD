package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 社員口座DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class ShainKouza implements IEteamDTO {

	/**
	 * default constructor
	 */
	public ShainKouza() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public ShainKouza(GMap m) {
		this.shainNo = m.get("shain_no");
		this.sakiKinyuukikanCd = m.get("saki_kinyuukikan_cd");
		this.sakiGinkouShitenCd = m.get("saki_ginkou_shiten_cd");
		this.sakiYokinShabetsu = m.get("saki_yokin_shabetsu");
		this.sakiKouzaBangou = m.get("saki_kouza_bangou");
		this.sakiKouzaMeigiKanji = m.get("saki_kouza_meigi_kanji");
		this.sakiKouzaMeigiKana = m.get("saki_kouza_meigi_kana");
		this.motoKinyuukikanCd = m.get("moto_kinyuukikan_cd");
		this.motoKinyuukikanShitenCd = m.get("moto_kinyuukikan_shiten_cd");
		this.motoYokinshubetsu = m.get("moto_yokinshubetsu");
		this.motoKouzaBangou = m.get("moto_kouza_bangou");
		this.zaimuEdabanCd = m.get("zaimu_edaban_cd");
		this.map = m;
	}

	/** 社員番号 */
	public String shainNo;

	/** 振込先金融機関銀行コード */
	public String sakiKinyuukikanCd;

	/** 振込先銀行支店コード */
	public String sakiGinkouShitenCd;

	/** 振込先預金種別 */
	public String sakiYokinShabetsu;

	/** 振込先口座番号 */
	public String sakiKouzaBangou;

	/** 振込先口座名義漢字 */
	public String sakiKouzaMeigiKanji;

	/** 振込先口座名義（半角カナ） */
	public String sakiKouzaMeigiKana;

	/** 振込元金融機関コード */
	public String motoKinyuukikanCd;

	/** 振込元金融機関支店コード */
	public String motoKinyuukikanShitenCd;

	/** 振込元預金種別 */
	public String motoYokinshubetsu;

	/** 振込元口座番号 */
	public String motoKouzaBangou;

	/** 財務枝番コード */
	public String zaimuEdabanCd;

	/** その他項目map */
	public GMap map;
}