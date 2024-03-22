package eteam.gyoumu.kaikei.common;

import eteam.base.EteamAbstractLogic;


/**
 * FB抽出共通ロジック
 */
public class FBLogic extends EteamAbstractLogic {

	/**
	 * FB抽出テーブルへのインサート
	 * シリアル番号、登録日時、更新日時は自動セットなので指定不要。その他の更新項目の値を指定する。
	 * @param d FBデータ
	 */
	public void insert(FBData d) {
		final String sql = "INSERT INTO fb(denpyou_id, user_id, fb_status, touroku_time, koushin_time, shubetsu_cd, cd_kbn, kaisha_cd, kaisha_name_hankana, furikomi_date, moto_kinyuukikan_cd, moto_kinyuukikan_name_hankana, moto_kinyuukikan_shiten_cd, moto_kinyuukikan_shiten_name_hankana, moto_yokin_shumoku_cd, moto_kouza_bangou, saki_kinyuukikan_cd, saki_kinyuukikan_name_hankana, saki_kinyuukikan_shiten_cd, saki_kinyuukikan_shiten_name_hankana, saki_yokin_shumoku_cd, saki_kouza_bangou, saki_kouza_meigi_kana, kingaku, shinki_cd, kokyaku_cd1, furikomi_kbn) VALUES(?, ?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
		connection.update(sql,
			d.getDenpyouId(),
			d.getUserId(),
			d.getFbStatus(),
			d.getShubetsuCd(),
			d.getCdKbn(),
			d.getKaishaCd(),
			d.getKaishaNameHankana(),
			d.getFurikomiDate(),
			d.getMotoKinyuukikanCd(),
			d.getMotoKinyuukikanNameHankana(),
			d.getMotoKinyuukikanShitenCd(),
			d.getMotoKinyuukikanShitenNameHankana(),
			d.getMotoYokinShumokuCd(),
			d.getMotoKouzaBangou(),
			d.getSakiKinyuukikanCd(),
			d.getSakiKinyuukikanNameHankana(),
			d.getSakiKinyuukikanShitenCd(),
			d.getSakiKinyuukikanShitenNameHankana(),
			d.getSakiYokinShumokuCd(),
			d.getSakiKouzaBangou(),
			d.getSakiKouzaMeigiKana(),
			d.getKingaku(),
			d.getShinkiCd(),
			d.getKokyakuCd1(),
			d.getFurikomiKbn()
		);
	}
}
