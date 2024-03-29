package eteam.gyoumu.kaikei;

import java.sql.Date;

import eteam.base.GMap;

public class ShiharaiIraiExtLogic extends ShiharaiIraiLogic {

	public GMap getKaitenbiAndHeitenbi(Date keijoubi, String futanBumonCd) {
		final String sql= " SELECT * FROM kaitenbi_heitenbi_master "
					 	+ " WHERE kesn = (SELECT kesn FROM ki_kesn WHERE ? BETWEEN from_date AND to_date) "
					 	+ " AND futan_bumon_cd = ? ";
		return connection.find(sql, keijoubi,futanBumonCd);
	}
}
