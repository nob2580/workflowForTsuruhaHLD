package eteam.gyoumu.kaikei.kogamen;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author j_matsumoto
 *
 */
@Getter @Setter @ToString
public class ShouhizeiShousaiAction extends EteamAbstractAction {

	/** 伝票区分 */
	protected String denpyouKbn;
	/** 伝票ID */
	protected String denpyouId;
	
	/** 仮払消費税額10% */
	protected String karibaraiZeigaku10Percent = "0";
	/** 仮払消費税額10%（仕入税額控除経過措置割合（80％）） */
	protected String karibaraiZeigaku10PercentMenzei80 = "0";
	/** 仮払消費税額軽減税率8% */
	protected String karibaraiZeigaku8Percent = "0";
	/** 仮払消費税額軽減税率8%（仕入税額控除経過措置割合（80％）） */
	protected String karibaraiZeigaku8PercentMenzei80 = "0";
	/** 仮受消費税額10% */
	protected String kariukeZeigaku10Percent = "0";
	/** 仮受消費税額10%（積上げ計算） */
	protected String kariukeZeigaku10PercentTsumiage = "0";
	/** 仮受消費税額10%（割戻し計算） */
	protected String kariukeZeigaku10PercentWarimodoshi = "0";
	/** 仮受消費税額軽減税率8% */
	protected String kariukeZeigaku8Percent = "0";
	/** 仮受消費税額軽減税率8%（積上げ計算） */
	protected String kariukeZeigaku8PercentTsumiage = "0";
	/** 仮受消費税額軽減税率8%（割戻し計算） */
	protected String kariukeZeigaku8PercentWarimodoshi = "0";
	
	/**
	 * 表示処理
	 * @return 結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			if (!errorList.isEmpty() || errorList.size() > 0) {
				return "error";
			}
		}
		return "success";
	}
	
	// Unnecessary methods
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}
}
