package eteam.common.select;

import java.util.List;

import eteam.base.GMap;

/**
 * マスターカテゴリー内のSelect文を集約したLogic
 */
public class MasterKanriCategoryExtLogic extends MasterKanriCategoryLogic {

	/**
	 * 振込先情報の一覧を取得
	 * @param torihikisakiCd
	 * @return
	 */
	public List<GMap> selectFurikomisakiJouhou(String torihikisakiCd){
		final String sql = " SELECT tf.ginkou_id, ok.kinyuukikan_cd AS ginkou_cd, ok.kinyuukikan_name_kana AS ginkou_name, "
				         + " ok.kinyuukikan_shiten_cd AS shiten_cd, ok.shiten_name_kana AS shiten_name, tf.yokin_shubetsu, "
						 + " tf.kouza_bangou, tf.kouza_meiginin, tfe.tesuuryou_futan_kbn AS tesuuryou"
						 + " FROM torihikisaki_furikomisaki tf "
						 + " LEFT OUTER JOIN torihikisaki_furikomisaki_ext tfe "
						 + " ON tf.torihikisaki_cd = tfe.torihikisaki_cd AND tf.ginkou_id = tfe.ginkou_id "
						 + " LEFT OUTER JOIN open21_kinyuukikan ok ON tf.kinyuukikan_cd = ok.kinyuukikan_cd "
						 + " AND tf.kinyuukikan_shiten_cd = ok.kinyuukikan_shiten_cd WHERE tf.torihikisaki_cd = ?";
	return connection.load(sql, torihikisakiCd);
	}
	
}
