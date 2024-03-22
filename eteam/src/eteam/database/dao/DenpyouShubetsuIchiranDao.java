package eteam.database.dao;

import eteam.database.abstractdao.DenpyouShubetsuIchiranAbstractDao;

/**
 * 伝票種別一覧に対するデータ操作クラス
 */
public class DenpyouShubetsuIchiranDao extends DenpyouShubetsuIchiranAbstractDao {

	/**
	 *「取引毎にルート判定する」がONの伝票種別をカウントする
	 * @return 「取引毎にルート判定する」がONの伝票種別数
	 */
	public boolean countRouteTorihikiFlg() {
		final String sql = "SELECT COUNT(*) count FROM denpyou_shubetsu_ichiran WHERE route_torihiki_flg = '1'";
		return ((long)connection.find(sql).get("count") > 0);
	}
}
