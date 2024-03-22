package eteam.database.dao;

import java.util.List;

import eteam.base.GMap;
import eteam.database.abstractdao.ShouninJoukyouAbstractDao;

/**
 * 承認状況に対するデータ操作クラス
 */
public class ShouninJoukyouDao extends ShouninJoukyouAbstractDao {

	/**
	 * 承認状況を取得
	 * @param denpyouId 伝票ID
	 * @return 承認状況
	 */
	public List<GMap> loadShouninJoukyou(String denpyouId){
		final String sql = "SELECT j.*, r.user_id route_user_id, r.user_full_name route_user_full_name FROM shounin_joukyou_kyoten j LEFT OUTER JOIN shounin_route_kyoten r ON (j.denpyou_id,j.shounin_route_edano)=(r.denpyou_id,r.edano) WHERE j.denpyou_id=? ORDER BY edano";
		return connection.load(sql, denpyouId);
	}
}
