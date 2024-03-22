package eteam.database.dao;

import java.math.BigDecimal;
import java.sql.Date;

import eteam.database.abstractdao.EbunshoDataAbstractDao;
import eteam.database.dto.EbunshoData;

/**
 * e文書データに対するデータ操作クラス
 */
public class EbunshoDataDao extends EbunshoDataAbstractDao {

	/**
	* e文書データの非キー全てを更新
	* @param dto e文書データ
	* @return 件数
	*/
	public int update(
		EbunshoData dto
		 ){
		final String sql =
				"UPDATE ebunsho_data "
		    + "SET ebunsho_shubetsu = ?, ebunsho_nengappi = ?, ebunsho_kingaku = ?, ebunsho_hakkousha = ?, ebunsho_hinmei = ? "
	 		+ "WHERE ebunsho_no = ? AND ebunsho_edano = ?";
			return connection.update(sql,
				dto.ebunshoShubetsu, dto.ebunshoNengappi, dto.ebunshoKingaku, dto.ebunshoHakkousha, dto.ebunshoHinmei
				,dto.ebunshoNo, dto.ebunshoEdano);
    }
	
	/**
	 * e文書データ保持用テーブルebunsho_dataへの登録を行う。
	 * @param ebunshoNo e文書番号
	 * @param ebunshoShubetsu 種別
	 * @param ebunshoNengappi 年月日
	 * @param ebunshoKingaku 金額
	 * @param ebunshoHakkousha 発行者
	 * @param ebunshoHinmei 品名
	 */
	public void insertEbunshoData(String ebunshoNo, Integer ebunshoShubetsu, Date ebunshoNengappi, BigDecimal ebunshoKingaku, String ebunshoHakkousha,String ebunshoHinmei){
		
		final String sql = "INSERT INTO ebunsho_data VALUES(?, (SELECT COALESCE(MAX(ebunsho_edano + 1), 1) FROM ebunsho_data WHERE ebunsho_no = ?), ?, ?, ?, ?, ?)";
		connection.update(sql, ebunshoNo, ebunshoNo, ebunshoShubetsu, ebunshoNengappi, ebunshoKingaku, ebunshoHakkousha, ebunshoHinmei);
	}
}
