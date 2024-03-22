package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.BumonKamokuYosan;

/**
 * 負担部門科目予算に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class BumonKamokuYosanAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected BumonKamokuYosan mapToDto(GMap map){
		return map == null ? null : new BumonKamokuYosan(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<BumonKamokuYosan> mapToDto(List<GMap> mapList){
		List<BumonKamokuYosan> dtoList = new ArrayList<BumonKamokuYosan>();
		for (var map : mapList) {
			dtoList.add(new BumonKamokuYosan(map));
		}
		return dtoList;
	}
	
	/**
	 * 負担部門科目予算のレコード有無を判定
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kessankiBangou 決算期番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String futanBumonCd, String kamokuGaibuCd, int kessankiBangou) {
		return this.find(futanBumonCd, kamokuGaibuCd, kessankiBangou) == null ? false : true;
	}
	
	/**
	 * 負担部門科目予算から主キー指定でレコードを取得
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kessankiBangou 決算期番号
	 * @return 負担部門科目予算DTO
	 */
	public BumonKamokuYosan find(String futanBumonCd, String kamokuGaibuCd, int kessankiBangou) {
		final String sql = "SELECT * FROM bumon_kamoku_yosan WHERE futan_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kessanki_bangou = ?";
		return mapToDto(connection.find(sql, futanBumonCd, kamokuGaibuCd, kessankiBangou));
	}
	
	/**
	 * 負担部門科目予算からレコードを全件取得 ※大量データ取得に注意
	 * @return List<負担部門科目予算DTO>
	 */
	public List<BumonKamokuYosan> load() {
		final String sql = "SELECT * FROM bumon_kamoku_yosan ORDER BY futan_bumon_cd, kamoku_gaibu_cd, kessanki_bangou";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 負担部門科目予算から一部キー指定でレコードを取得
	 * @param futanBumonCd 負担部門コード
	 * @return List<負担部門科目予算>DTO
	 */
	public List<BumonKamokuYosan> load(String futanBumonCd) {
		final String sql = "SELECT * FROM bumon_kamoku_yosan WHERE futan_bumon_cd = ? "
							+ "ORDER BY futan_bumon_cd, kamoku_gaibu_cd, kessanki_bangou";
		return mapToDto(connection.load(sql, futanBumonCd));
	}
	
	/**
	 * 負担部門科目予算から一部キー指定でレコードを取得
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return List<負担部門科目予算>DTO
	 */
	public List<BumonKamokuYosan> load(String futanBumonCd, String kamokuGaibuCd) {
		final String sql = "SELECT * FROM bumon_kamoku_yosan WHERE futan_bumon_cd = ?  AND kamoku_gaibu_cd = ? "
							+ "ORDER BY futan_bumon_cd, kamoku_gaibu_cd, kessanki_bangou";
		return mapToDto(connection.load(sql, futanBumonCd, kamokuGaibuCd));
	}

	/**
	* 負担部門科目予算登録
	* @param dto 負担部門科目予算
	* @return 件数
	*/
	public int insert(
		BumonKamokuYosan dto
	){
		final String sql =
				"INSERT INTO bumon_kamoku_yosan "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.futanBumonCd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.futanBumonName, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.yosan01, dto.yosan02, dto.yosan03, dto.yosan03Shu, dto.yosan04, dto.yosan05, dto.yosan06, dto.yosan06Shu, dto.yosan07, dto.yosan08, dto.yosan09, dto.yosan09Shu, dto.yosan10, dto.yosan11, dto.yosan12, dto.yosan12Shu
					);
	}

	/**
	* 負担部門科目予算登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 負担部門科目予算
	* @return 件数
	*/
	public int upsert(
		BumonKamokuYosan dto
		 ){
		final String sql =
				"INSERT INTO bumon_kamoku_yosan "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT bumon_kamoku_yosan_pkey "
			+ "DO UPDATE SET kamoku_naibu_cd = ?, futan_bumon_name = ?, chouhyou_shaturyoku_no = ?, kamoku_name_ryakushiki = ?, kamoku_name_seishiki = ?, taishaku_zokusei = ?, yosan_01 = ?, yosan_02 = ?, yosan_03 = ?, yosan_03_shu = ?, yosan_04 = ?, yosan_05 = ?, yosan_06 = ?, yosan_06_shu = ?, yosan_07 = ?, yosan_08 = ?, yosan_09 = ?, yosan_09_shu = ?, yosan_10 = ?, yosan_11 = ?, yosan_12 = ?, yosan_12_shu = ? "
			+ "";
			return connection.update(sql,
				dto.futanBumonCd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.futanBumonName, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.yosan01, dto.yosan02, dto.yosan03, dto.yosan03Shu, dto.yosan04, dto.yosan05, dto.yosan06, dto.yosan06Shu, dto.yosan07, dto.yosan08, dto.yosan09, dto.yosan09Shu, dto.yosan10, dto.yosan11, dto.yosan12, dto.yosan12Shu
				, dto.kamokuNaibuCd, dto.futanBumonName, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.yosan01, dto.yosan02, dto.yosan03, dto.yosan03Shu, dto.yosan04, dto.yosan05, dto.yosan06, dto.yosan06Shu, dto.yosan07, dto.yosan08, dto.yosan09, dto.yosan09Shu, dto.yosan10, dto.yosan11, dto.yosan12, dto.yosan12Shu
				);
    }
	
	/**
	 * 負担部門科目予算から主キー指定でレコードを削除
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kessankiBangou 決算期番号
	 * @return 削除件数
	 */
	public int delete(String futanBumonCd, String kamokuGaibuCd, int kessankiBangou){
		final String sql = "DELETE FROM bumon_kamoku_yosan WHERE futan_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kessanki_bangou = ?";
		return connection.update(sql, futanBumonCd, kamokuGaibuCd, kessankiBangou);
	}
	
	/**
	 * 負担部門科目予算から一部キー指定でレコードを削除
	 * @param futanBumonCd 負担部門コード
	 * @return 削除件数
	 */
	public int delete(String futanBumonCd) {
		final String sql = "DELETE FROM bumon_kamoku_yosan WHERE futan_bumon_cd = ? ";
		return connection.update(sql, futanBumonCd);
	}
	
	/**
	 * 負担部門科目予算から一部キー指定でレコードを削除
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return 削除件数
	 */
	public int delete(String futanBumonCd, String kamokuGaibuCd) {
		final String sql = "DELETE FROM bumon_kamoku_yosan WHERE futan_bumon_cd = ?  AND kamoku_gaibu_cd = ? ";
		return connection.update(sql, futanBumonCd, kamokuGaibuCd);
	}
}
