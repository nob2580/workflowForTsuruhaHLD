package eteam.common;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 祝日定義リスト取得用Logic
 */
public class ShukujitsuListLogic extends EteamAbstractLogic {
	
	/**
	 * 祝日定義リストを取得する。
	 * @return リスト
	 */
	public List<GMap> selectShukujitsuList(){
		final String sql = "SELECT shukujitsu,shukujitsu_name "
				 + " FROM shukujitsu_master "
				 + " ORDER BY shukujitsu ";
		return connection.load(sql);
	}
}
