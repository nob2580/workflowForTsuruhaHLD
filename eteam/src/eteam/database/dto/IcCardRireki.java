package eteam.database.dto;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ICカード利用履歴DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class IcCardRireki implements IEteamDTO {


	/**
	 * default constructor
	 */
	public IcCardRireki() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public IcCardRireki(GMap m) {
		this.icCardNo = m.get("ic_card_no");
		this.icCardSequenceNo = m.get("ic_card_sequence_no");
		this.icCardRiyoubi = m.get("ic_card_riyoubi");
		this.tanmatuCd = m.get("tanmatu_cd");
		this.lineCdFrom = m.get("line_cd_from");
		this.lineNameFrom = m.get("line_name_from");
		this.ekiCdFrom = m.get("eki_cd_from");
		this.ekiNameFrom = m.get("eki_name_from");
		this.lineCdTo = m.get("line_cd_to");
		this.lineNameTo = m.get("line_name_to");
		this.ekiCdTo = m.get("eki_cd_to");
		this.ekiNameTo = m.get("eki_name_to");
		this.kingaku = m.get("kingaku");
		this.userId = m.get("user_id");
		this.jyogaiFlg = m.get("jyogai_flg");
		this.shoriCd = m.get("shori_cd");
		this.balance = m.get("balance");
		this.allByte = m.get("all_byte");
		this.map = m;
	}

	/** ICカード番号 */
	public String icCardNo;

	/** ICカードシーケンス番号 */
	public String icCardSequenceNo;

	/** ICカード利用日 */
	public Date icCardRiyoubi;

	/** 端末種別 */
	public String tanmatuCd;

	/** 路線コード（FROM） */
	public String lineCdFrom;

	/** 路線名（FROM） */
	public String lineNameFrom;

	/** 駅コード（FROM） */
	public String ekiCdFrom;

	/** 駅名（FROM） */
	public String ekiNameFrom;

	/** 路線コード（TO） */
	public String lineCdTo;

	/** 路線名（TO） */
	public String lineNameTo;

	/** 駅コード（TO） */
	public String ekiCdTo;

	/** 駅名（TO） */
	public String ekiNameTo;

	/** 金額 */
	public BigDecimal kingaku;

	/** ユーザーID */
	public String userId;

	/** 除外フラグ */
	public String jyogaiFlg;

	/** 処理内容 */
	public String shoriCd;

	/** 残高 */
	public BigDecimal balance;

	/** 全バイト配列 */
	public String allByte;

	/** その他項目map */
	public GMap map;
}