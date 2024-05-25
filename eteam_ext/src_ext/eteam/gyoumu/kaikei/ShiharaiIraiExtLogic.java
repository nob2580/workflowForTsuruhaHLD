package eteam.gyoumu.kaikei;

import java.sql.Date;
import java.util.List;

import eteam.base.GMap;

public class ShiharaiIraiExtLogic extends ShiharaiIraiLogic {

	/**
	 * 開店日と閉店日を取得
	 * @param keijoubi
	 * @param futanBumonCd
	 * @return
	 */
	public GMap getKaitenbiAndHeitenbi(Date keijoubi, String futanBumonCd) {
		final String sql= " SELECT * FROM kaitenbi_heitenbi_master "
					 	+ " WHERE kesn = (SELECT kesn FROM ki_kesn WHERE ? BETWEEN from_date AND to_date) "
					 	+ " AND futan_bumon_cd = ? ";
		return connection.find(sql, keijoubi,futanBumonCd);
	}
	
	/**
	 * URL情報を登録
	 * @param denpyouId
	 * @param edano
	 * @param url
	 * @return
	 */
	public int insertURLInfo(String denpyouId, int edano,String url) {
		if(url.isEmpty()) {
			return 0;
		}
		final String sql = "INSERT INTO url_info VALUES (?,?,?)"; 
		return connection.update(sql, denpyouId, edano, url);
	}
	
	/**
	 * 伝票IDをキーにURL情報を取得
	 * @param denpyouId
	 * @return
	 */
	public List<GMap> loadURLinfo(String denpyouId){
		final String sql = "SELECT * FROM url_info WHERE denpyou_id = ? ORDER BY edano ASC";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 伝票IDをキーにURL情報を削除
	 * @param denpyouId
	 * @return
	 */
	public int deleteURLInfo(String denpyouId) {
		final String sql ="DELETE FROM url_info WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}
	
}
