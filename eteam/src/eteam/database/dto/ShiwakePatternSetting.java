package eteam.database.dto;


import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仕訳パターン設定DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class ShiwakePatternSetting implements IEteamDTO {

	/**
	 * default constructor
	 */
	public ShiwakePatternSetting() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public ShiwakePatternSetting(GMap m) {
		this.denpyouKbn = m.get("denpyou_kbn");
		this.settingKbn = m.get("setting_kbn");
		this.settingItem = m.get("setting_item");
		this.defaultValue = m.get("default_value");
		this.hyoujiFlg = m.get("hyouji_flg");
		this.shiwakePatternVar1 = m.get("shiwake_pattern_var1");
		this.shiwakePatternVar2 = m.get("shiwake_pattern_var2");
		this.shiwakePatternVar3 = m.get("shiwake_pattern_var3");
		this.shiwakePatternVar4 = m.get("shiwake_pattern_var4");
		this.shiwakePatternVar5 = m.get("shiwake_pattern_var5");
		this.map = m;
	}

	/** 伝票区分 */
	public String denpyouKbn;

	/** 設定区分 */
	public String settingKbn;

	/** 設定項目 */
	public String settingItem;

	/** デフォルト値 */
	public String defaultValue;

	/** 表示フラグ */
	public String hyoujiFlg;

	/** 仕訳パターン変数1 */
	public String shiwakePatternVar1;

	/** 仕訳パターン変数2 */
	public String shiwakePatternVar2;

	/** 仕訳パターン変数3 */
	public String shiwakePatternVar3;

	/** 仕訳パターン変数4 */
	public String shiwakePatternVar4;

	/** 仕訳パターン変数5 */
	public String shiwakePatternVar5;

	/** その他項目map */
	public GMap map;
}
