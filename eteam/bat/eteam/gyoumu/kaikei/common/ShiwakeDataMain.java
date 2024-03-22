package eteam.gyoumu.kaikei.common;

import eteam.common.EteamNaibuCodeSetting.SHIWAKE_STATUS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仕訳抽出テーブルのレコード
 */
@Getter @Setter @ToString
public class ShiwakeDataMain implements Cloneable {
	//WF側の項目
	/** 伝票ID */ String denpyou_id;
	/** 仕訳抽出状態 */ String shiwake_status = SHIWAKE_STATUS.CHUUSHUTSU;
	
	//OPEN21
	/** HF① */ String HF1 = "";//ヘッダーフィールド１～１０にマッピングされるWF側の番号
	/** HF② */ String HF2 = "";
	/** HF③ */ String HF3 = "";
	/** HF④ */ String HF4 = "";
	/** HF⑤ */ String HF5 = "";
	/** HF⑥ */ String HF6 = "";
	/** HF⑦ */ String HF7 = "";
	/** HF⑧ */ String HF8 = "";
	/** HF⑨ */ String HF9 = "";
	/** HF⑩ */ String HF10 = "";

	/** 本体(HF)用社員番号 */ String shainNo;

	@Override
	public ShiwakeDataMain clone() {
		try {
			return (ShiwakeDataMain)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
