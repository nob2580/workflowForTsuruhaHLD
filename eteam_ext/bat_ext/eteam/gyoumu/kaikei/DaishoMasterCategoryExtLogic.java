package eteam.gyoumu.kaikei;

import eteam.base.GMap;

/**
* マスターカテゴリー内のSelect文を集約したLogic
*/
public class DaishoMasterCategoryExtLogic extends DaishoMasterCategoryLogic {
	
	/**
	 * 振込先を取得する
	 * @param torihikisakiCd 取引先コード
	 * @param furikomiGinkouId
	 * @return 振込先
	 */
	public GMap findFurikomisaki(String torihikisakiCd, String furikomiGinkouId) {
		//パッケージ番の振込先リスト
		GMap torihikisaki = super.findFurikomisaki(torihikisakiCd);
		//銀行IDをint型に変換
		int ginkouId = (isEmpty(furikomiGinkouId))	? 1:Integer.parseInt(furikomiGinkouId);
		if(ginkouId== 1) {
			//銀行ID１なら銀行IDをマップに追加してもともとの処理を返せばいい
			torihikisaki.put("ginkou_id",1);
			return torihikisaki;
		}else {
			//それ以外の場合は銀行IDを指定して振込先から情報を取得
			final String sql =" SELECT  tf.*, tfe.kouza_meiginin_seishiki, tfe.furikomi_kbn, tfe.tesuuryou_futan_kbn "
					+ " FROM  torihikisaki_furikomisaki tf LEFT OUTER JOIN torihikisaki_furikomisaki_ext tfe"
					+ " ON tf.torihikisaki_cd = tfe.torihikisaki_cd AND tf.ginkou_id = tfe.ginkou_id "
					+ " WHERE tf.torihikisaki_cd = ? AND tf.ginkou_id = ? ";
				
		GMap reList = connection.find(sql, torihikisakiCd,ginkouId);
		//銀行IDをセット
		torihikisaki.put("ginkou_id", reList.get("ginkou_id"));
		//取引先マスターの振込先情報に該当する部分を書き換える
		torihikisaki.replace("furikomi_ginkou_cd",reList.get("kinyuukikan_cd")); //銀行コード
		torihikisaki.replace("furikomi_ginkou_shiten_cd",reList.get("kinyuukikan_shiten_cd")); //銀行支店コード
		torihikisaki.replace("yokin_shubetsu", reList.get("yokin_shubetsu")); //預金種別
		torihikisaki.replace("kouza_bangou", reList.get("kouza_bangou")); //口座番号
		torihikisaki.replace("kouza_meiginin_furigana", reList.get("kouza_meiginin"));	//口座名義人(半ｶﾅ）
		torihikisaki.replace("tesuuryou_futan_kbn", reList.get("tesuuryou_futan_kbn")); //手数料負担区分
		//おそらく使用しないがデータがおかしくならないよう書き換え
		torihikisaki.replace("kouza_meiginin", reList.get("kouza_meiginin_seishiki")); //口座名義人正式
		torihikisaki.replace("furikomi_kbn", reList.get("furikomi_kbn")); //振込区分
		return torihikisaki;
		}
	}
}
