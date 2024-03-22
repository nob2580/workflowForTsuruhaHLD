package eteam.database.dao;

import java.sql.Date;

import eteam.base.GMap;
import eteam.database.abstractdao.KiKesnAbstractDao;
import eteam.database.dto.KiKesn;

/**
 * （期別）決算期に対するデータ操作クラス
 */
public class KiKesnDao extends KiKesnAbstractDao {
	
	/**
	 * 「(期別)決算期」から指定日が開始日～終了日の範囲内のレコードを取得
	 * @param date 日付
	 * @return 内部決算期
	 */
	public KiKesn findKiKesn(Date date) {
		final String sql = "SELECT * FROM ki_kesn WHERE ? BETWEEN from_date AND to_date";
		return mapToDto(connection.find(sql, date));
	}
	
    /**
     * 指定日が財務の月開始日であるかチェック
	 * @param hizuke 指定日
     * @return 指定日が財務の月開始日ならtrue
     */
    public boolean isTsukiKaishi(Date hizuke){
        GMap mp = connection.find("SELECT count(*) AS cnt FROM ki_kesn WHERE from_date = ? ", hizuke);
        return (long)mp.get("cnt") > 0 ? true : false;
    }
    
    /**
     * 指定日が財務の月締日であるかチェック
	 * @param hizuke 指定日
     * @return 指定日が財務の月締日ならtrue
     */
    public boolean isTsukiShime(Date hizuke){
        GMap mp = connection.find("SELECT count(*) AS cnt FROM ki_kesn WHERE to_date = ? ", hizuke);
        return (long)mp.get("cnt") > 0 ? true : false;
    }
}
