package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 画面項目制御DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class GamenKoumokuSeigyo implements IEteamDTO {

	/**
	 * default constructor
	 */
	public GamenKoumokuSeigyo() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public GamenKoumokuSeigyo(GMap m) {
		this.denpyouKbn = m.get("denpyou_kbn");
		this.koumokuId = m.get("koumoku_id");
		this.defaultKoumokuName = m.get("default_koumoku_name");
		this.koumokuName = m.get("koumoku_name");
		this.hyoujiFlg = m.get("hyouji_flg");
		this.hissuFlg = m.get("hissu_flg");
		this.hyoujiSeigyoFlg = m.get("hyouji_seigyo_flg");
		this.pdfHyoujiFlg = m.get("pdf_hyouji_flg");
		this.pdfHyoujiSeigyoFlg = m.get("pdf_hyouji_seigyo_flg");
		this.codeOutputFlg = m.get("code_output_flg");
		this.codeOutputSeigyoFlg = m.get("code_output_seigyo_flg");
		this.hyoujiJun = m.get("hyouji_jun");
		this.map = m;
	}

	/** 伝票区分 */
	public String denpyouKbn;

	/** 項目ID */
	public String koumokuId;

	/** デフォルト項目名 */
	public String defaultKoumokuName;

	/** 項目名 */
	public String koumokuName;

	/** 表示フラグ */
	public String hyoujiFlg;

	/** 必須フラグ */
	public String hissuFlg;

	/** 表示制御フラグ */
	public String hyoujiSeigyoFlg;

	/** 帳票表示フラグ */
	public String pdfHyoujiFlg;

	/** 帳票表示制御フラグ */
	public String pdfHyoujiSeigyoFlg;

	/** 帳票コード印字フラグ */
	public String codeOutputFlg;

	/** 帳票コード印字制御フラグ */
	public String codeOutputSeigyoFlg;

	/** 表示順 */
	public int hyoujiJun;

	/** その他項目map */
	public GMap map;
}
