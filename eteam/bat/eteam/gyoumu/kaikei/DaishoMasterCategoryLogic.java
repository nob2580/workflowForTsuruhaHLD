package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.MasterKanriCategoryLogic;

/**
 * マスターカテゴリー内のSelect文を集約したLogic
 */
public class DaishoMasterCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* 得意先(tokuisaki) */
	/**
	 * 得意先を全て取得します。
	 * @return リスト
	 */
	public List<GMap> selectTokuisakiList() {
		final String sql =
					"SELECT * FROM tokuisaki ORDER BY tokuisaki_cd";
		return connection.load(sql);
	}
	
	/**
	 * 得意先のレコードを取得する。
	 * @param tokuisakiCd 得意先コード
	 * @return 得意先レコード
	 */
	public String findTokuisakiName(String tokuisakiCd) {
		final String sql = "SELECT * FROM tokuisaki WHERE tokuisaki_cd = ?";
		GMap map = connection.find(sql, tokuisakiCd);
		return map == null ? "" : (String)map.get("tokuisaki_name");
	}

/* 取引先(torihikisaki) */
	/**
	 * 振込先を取得する
	 * @param torihikisakiCd 取引先コード
	 * @return 振込先
	 */
	public GMap findFurikomisaki(String torihikisakiCd) {
		final String sql = "SELECT * "
						+ "FROM torihikisaki "
						+ "WHERE "
						+ "  torihikisaki_cd = ? ";
		
		return connection.find(sql, torihikisakiCd);
	}

/* 取引先振込先(torihikisaki_furikomisaki) */
// /**
//  * 振込先を取得する
//  * @param torihikisakiCd 取引先コード
//  * @param ginkouCd 金融機関コード
//  * @param shitenCd 金融機関支店コード
//  * @param kouzaBangou 口座番号
//  */
// public Map<String, Object> findFurikomisaki(String torihikisakiCd, String ginkouCd, String shitenCd, String kouzaBangou) {
// final String sql = "SELECT * FROM torihikisaki_furikomisaki WHERE torihikisaki_cd = ? AND kinyuukikan_cd = ? AND kinyuukikan_shiten_cd = ? AND kouza_bangou = ?";
// return connection.find(sql, torihikisakiCd, ginkouCd, shitenCd, kouzaBangou);
// }

/* 取引先支払方法(torihikisaki_shiharaihouhou) */
	/**
	 * 取引先支払方法を取得
	 * @param torihikisakiCd 取引先コード
	 * @return 取引先支払方法
	 */
	public GMap findTorihikisakiShiharaiHouhou(String torihikisakiCd) {
		final String sql = "SELECT * FROM torihikisaki_shiharaihouhou WHERE torihikisaki_cd = ? AND shiharai_id = 1";
		return connection.find(sql, torihikisakiCd);
	}

/* 取引先補助(torihikisaki_hojo) */
	/**
	 * 取引先補助を取得
	 * @param torihikisakiCd 取引先コード
	 * @return 取引先補助
	 */
	public GMap findTorihikisakiHojoU(String torihikisakiCd) {
		final String sql = "SELECT * FROM torihikisaki_hojo where torihikisaki_cd = ?";
		return connection.find(sql, torihikisakiCd);
	}
	
	/**
	 * 基準日を元に支払日を決定
	 * @param torihikisakiCd 取引先コード
	 * @param s_kijunbi 基準日
	 * @param type 1:支払日、2:支払期日
	 * @param shiharaiHouhou 支払方法
	 * @param shiharaiShubetsu 支払種別
	 * @return 支払予定日
	 */
	public Date judgeShiharaibi(String torihikisakiCd, Date s_kijunbi, int type, String shiharaiHouhou, String shiharaiShubetsu) {
		MasterKanriCategoryLogic mLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);

		//---------------------
		//基準日未決定なので支払日も未決定で終わり
		//---------------------
		if (s_kijunbi == null)
		{
			return null;
		}

		//---------------------
		//基準日の日付(DD)
		//---------------------
		java.util.Date kijunbi = new Date(s_kijunbi.getTime());
		@SuppressWarnings("deprecation")
		int kijunbiDD = kijunbi.getDate();
		
		//---------------------
		//取引先支払方法マスターの各種設定値取得
		//マスターが見つからなければ支払日決定できない
		//---------------------
		GMap shiharai = findTorihikisakiShiharaiHouhou(torihikisakiCd);
		if (shiharai == null)
		{
			return null;
		}
		int shimebi = (int)shiharai.get("shimebi");
		int shiharaibiMM = type == 1 ? (int)shiharai.get("shiharaibi_mm"): (int)shiharai.get("shiharaikijitsu_mm");
		int shiharaibiDD = type == 1 ? (int)shiharai.get("shiharaibi_dd"): (int)shiharai.get("shiharaikijitsu_dd");
		int kyuujitsuHosei = type == 1 ? (int)shiharai.get("harai_h") : (int)shiharai.get("kijitu_h");

		//---------------------
		//基準月初日を作る
		//基準日(DD) <= 締日なら計上月を基準月とする
		//基準日(DD) > 締日なら計上月翌月を基準月とする
		//---------------------
		if (type == 1 && kijunbiDD > shimebi) {
			kijunbi = DateUtils.addMonths(kijunbi, 1);//月末補正あり:1/31→2/28
		}
		kijunbi.setDate(1);

		//---------------------
		//基準月のMMヶ月後のDD日が支払日である
		//※DD=99は末日とする
		//---------------------

		//基準日MM月後の初日を作る
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.addMonths(kijunbi, shiharaibiMM)); 

		//基準日MM月後のDD日を作る
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		if (shiharaibiDD > lastDay) {
			shiharaibiDD = lastDay;
		}
		cal.set(Calendar.DATE, shiharaibiDD);
		Date shiharaibi = new Date(cal.getTimeInMillis());
		
		//振込(その他)は取引先マスタと関係なく前日補正
// if (shiharaiHouhou.equals(SHIHARAI_IRAI_HOUHOU.FURIKOMI) && shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.SONOTA)) {
// kyuujitsuHosei = 0;
// }
		
		//休日補正で前倒し
		switch(kyuujitsuHosei) {
			//0:前営業日
			case 0:
				shiharaibi = mLogic.findEigyoubiBefore(shiharaibi);
				break;
			//1:当日は何もせいず
			//2:後営業日
			case 2:
				shiharaibi = mLogic.findEigyoubiAfter(shiharaibi);
				break;
		}
		return shiharaibi;
	}

/* 金融機関(open21_kinyuukikan, kinyuukikan) */
	/**
	 * 金融機関の名称を取得(Open21)
	 * @param kinyuuKikanCd 金融機関コード
	 * @return 金融機関名称
	 */
	public String findKinyuukikanName(String kinyuuKikanCd) {
		final String sql = "SELECT kinyuukikan_name_kana FROM open21_kinyuukikan WHERE kinyuukikan_cd = ?";
		GMap record = connection.find(sql, kinyuuKikanCd);
		return (record == null) ? "" : (String)record.get("kinyuukikan_name_kana");
	}
	
	/**
	 * 金融機関の名称を取得(WF)
	 * @param kinyuuKikanCd 金融機関コード
	 * @return 金融機関名称
	 */
	public String findWFKinyuukikanName(String kinyuuKikanCd) {
		final String sql = "SELECT kinyuukikan_name_kana FROM kinyuukikan WHERE kinyuukikan_cd = ? ORDER BY kinyuukikan_shiten_cd LIMIT 1";
		GMap record = connection.find(sql, kinyuuKikanCd);
		return record == null ? null : (String)record.get("kinyuukikan_name_kana");
	}
	/**
	 * 金融機関支店の名称を取得
	 * @param kinyuuKikanCd 金融機関コード
	 * @param kinyuuKikanShitenCd 金融機関支店コード
	 * @return 金融機関支店名称
	 */
	public String findKinyuukikanShitenName(String kinyuuKikanCd, String kinyuuKikanShitenCd) {
		final String sql = "SELECT shiten_name_kana FROM open21_kinyuukikan WHERE kinyuukikan_cd = ? AND kinyuukikan_shiten_cd = ?";
		GMap record = connection.find(sql, kinyuuKikanCd, kinyuuKikanShitenCd);
		return (record == null) ? "" : (String)record.get("shiten_name_kana");
	}
	/**
	 * 金融機関を全て取得します。
	 * @return リスト
	 */
	public List<GMap> loadOpen21Kinyuukikan() {
		final String sql = "SELECT distinct kinyuukikan_cd, kinyuukikan_name_kana FROM open21_kinyuukikan ORDER BY kinyuukikan_cd";
		return connection.load(sql);
	}
	/**
	 * WFの金融機関について、支店コードが最小のものを取得します。
	 * @return リスト
	 */
	public List<GMap> loadWFKinyuukikan() {
		final String sql = "SELECT k1.kinyuukikan_cd kinyuukikan_cd, kinyuukikan_name_kana, k1.kinyuukikan_shiten_cd FROM kinyuukikan k1 INNER JOIN\r\n"
				+ "(SELECT k2.kinyuukikan_cd, MIN(kinyuukikan_shiten_cd) shiten_cd FROM kinyuukikan k2 GROUP BY kinyuukikan_cd ORDER BY kinyuukikan_cd) k2\r\n"
				+ "ON k1.kinyuukikan_cd = k2.kinyuukikan_cd AND k1.kinyuukikan_shiten_cd = k2.shiten_cd ORDER BY k1.kinyuukikan_cd";
		return connection.load(sql);
	}
	/**
	 * 金融機関支店を全て取得します。
	 * @param kinyuukikanCd 金融機関コード
	 * @return リスト
	 */
	public List<GMap> loadOpen21KinyuukikanShiten(String kinyuukikanCd) {
		final String sql = "SELECT distinct kinyuukikan_shiten_cd, shiten_name_kana FROM open21_kinyuukikan WHERE kinyuukikan_cd = ? ORDER BY kinyuukikan_shiten_cd";
		return connection.load(sql, kinyuukikanCd);
	}
	
	/**
	 * 一見先振込手数料を決定
	 * @param shiharai 支払金額（手数料込）
	 * @param ginkouCd 銀行コード
	 * @return 手数料
	 */
	public BigDecimal judgeTesuuryou(BigDecimal shiharai, String ginkouCd) {
		final String doukouSql = "SELECT COUNT(*) count FROM moto_kouza_shiharaiirai WHERE moto_kinyuukikan_cd = ?";
		boolean doukou = ((long)connection.find(doukouSql, ginkouCd).get("count")) >= 1;
		String tesuuryouSet[] = setting.ichigenTesuuryou().split(",");
		BigDecimal tesuryou1 = new BigDecimal(doukou ? tesuuryouSet[0] : tesuuryouSet[2]);
		BigDecimal tesuryou2 = new BigDecimal(doukou ? tesuuryouSet[1] : tesuuryouSet[3]);
		return (shiharai.doubleValue() - tesuryou1.doubleValue() < 30000) ? tesuryou1 : tesuryou2;
	}
}
