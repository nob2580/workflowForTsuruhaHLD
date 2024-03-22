package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.BumonKamokuZandaka;

/**
 * 負担部門科目残高に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class BumonKamokuZandakaAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected BumonKamokuZandaka mapToDto(GMap map){
		return map == null ? null : new BumonKamokuZandaka(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<BumonKamokuZandaka> mapToDto(List<GMap> mapList){
		List<BumonKamokuZandaka> dtoList = new ArrayList<BumonKamokuZandaka>();
		for (var map : mapList) {
			dtoList.add(new BumonKamokuZandaka(map));
		}
		return dtoList;
	}
	
	/**
	 * 負担部門科目残高のレコード有無を判定
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kessankiBangou 決算期番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String futanBumonCd, String kamokuGaibuCd, int kessankiBangou) {
		return this.find(futanBumonCd, kamokuGaibuCd, kessankiBangou) == null ? false : true;
	}
	
	/**
	 * 負担部門科目残高から主キー指定でレコードを取得
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kessankiBangou 決算期番号
	 * @return 負担部門科目残高DTO
	 */
	public BumonKamokuZandaka find(String futanBumonCd, String kamokuGaibuCd, int kessankiBangou) {
		final String sql = "SELECT * FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kessanki_bangou = ?";
		return mapToDto(connection.find(sql, futanBumonCd, kamokuGaibuCd, kessankiBangou));
	}
	
	/**
	 * 負担部門科目残高からレコードを全件取得 ※大量データ取得に注意
	 * @return List<負担部門科目残高DTO>
	 */
	public List<BumonKamokuZandaka> load() {
		final String sql = "SELECT * FROM bumon_kamoku_zandaka ORDER BY futan_bumon_cd, kamoku_gaibu_cd, kessanki_bangou";
		return mapToDto(connection.load(sql));
	}
	
	/**
	 * 負担部門科目残高から一部キー指定でレコードを取得
	 * @param futanBumonCd 負担部門コード
	 * @return List<負担部門科目残高>DTO
	 */
	public List<BumonKamokuZandaka> load(String futanBumonCd) {
		final String sql = "SELECT * FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ? "
							+ "ORDER BY futan_bumon_cd, kamoku_gaibu_cd, kessanki_bangou";
		return mapToDto(connection.load(sql, futanBumonCd));
	}
	
	/**
	 * 負担部門科目残高から一部キー指定でレコードを取得
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return List<負担部門科目残高>DTO
	 */
	public List<BumonKamokuZandaka> load(String futanBumonCd, String kamokuGaibuCd) {
		final String sql = "SELECT * FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ?  AND kamoku_gaibu_cd = ? "
							+ "ORDER BY futan_bumon_cd, kamoku_gaibu_cd, kessanki_bangou";
		return mapToDto(connection.load(sql, futanBumonCd, kamokuGaibuCd));
	}

	/**
	* 負担部門科目残高登録
	* @param dto 負担部門科目残高
	* @return 件数
	*/
	public int insert(
		BumonKamokuZandaka dto
	){
		final String sql =
				"INSERT INTO bumon_kamoku_zandaka "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.futanBumonCd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.futanBumonName, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.zandakaKari00, dto.zandakaKashi00, dto.zandakaKari01, dto.zandakaKashi01, dto.zandakaKari02, dto.zandakaKashi02, dto.zandakaKari03, dto.zandakaKashi03, dto.zandakaKari03Shu, dto.zandakaKashi03Shu, dto.zandakaKari04, dto.zandakaKashi04, dto.zandakaKari05, dto.zandakaKashi05, dto.zandakaKari06, dto.zandakaKashi06, dto.zandakaKari06Shu, dto.zandakaKashi06Shu, dto.zandakaKari07, dto.zandakaKashi07, dto.zandakaKari08, dto.zandakaKashi08, dto.zandakaKari09, dto.zandakaKashi09, dto.zandakaKari09Shu, dto.zandakaKashi09Shu, dto.zandakaKari10, dto.zandakaKashi10, dto.zandakaKari11, dto.zandakaKashi11, dto.zandakaKari12, dto.zandakaKashi12, dto.zandakaKari12Shu, dto.zandakaKashi12Shu
					);
	}

	/**
	* 負担部門科目残高登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 負担部門科目残高
	* @return 件数
	*/
	public int upsert(
		BumonKamokuZandaka dto
		 ){
		final String sql =
				"INSERT INTO bumon_kamoku_zandaka "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT bumon_kamoku_zandaka_pkey "
			+ "DO UPDATE SET kamoku_naibu_cd = ?, futan_bumon_name = ?, chouhyou_shaturyoku_no = ?, kamoku_name_ryakushiki = ?, kamoku_name_seishiki = ?, taishaku_zokusei = ?, zandaka_kari00 = ?, zandaka_kashi00 = ?, zandaka_kari01 = ?, zandaka_kashi01 = ?, zandaka_kari02 = ?, zandaka_kashi02 = ?, zandaka_kari03 = ?, zandaka_kashi03 = ?, zandaka_kari03_shu = ?, zandaka_kashi03_shu = ?, zandaka_kari04 = ?, zandaka_kashi04 = ?, zandaka_kari05 = ?, zandaka_kashi05 = ?, zandaka_kari06 = ?, zandaka_kashi06 = ?, zandaka_kari06_shu = ?, zandaka_kashi06_shu = ?, zandaka_kari07 = ?, zandaka_kashi07 = ?, zandaka_kari08 = ?, zandaka_kashi08 = ?, zandaka_kari09 = ?, zandaka_kashi09 = ?, zandaka_kari09_shu = ?, zandaka_kashi09_shu = ?, zandaka_kari10 = ?, zandaka_kashi10 = ?, zandaka_kari11 = ?, zandaka_kashi11 = ?, zandaka_kari12 = ?, zandaka_kashi12 = ?, zandaka_kari12_shu = ?, zandaka_kashi12_shu = ? "
			+ "";
			return connection.update(sql,
				dto.futanBumonCd, dto.kamokuGaibuCd, dto.kessankiBangou, dto.kamokuNaibuCd, dto.futanBumonName, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.zandakaKari00, dto.zandakaKashi00, dto.zandakaKari01, dto.zandakaKashi01, dto.zandakaKari02, dto.zandakaKashi02, dto.zandakaKari03, dto.zandakaKashi03, dto.zandakaKari03Shu, dto.zandakaKashi03Shu, dto.zandakaKari04, dto.zandakaKashi04, dto.zandakaKari05, dto.zandakaKashi05, dto.zandakaKari06, dto.zandakaKashi06, dto.zandakaKari06Shu, dto.zandakaKashi06Shu, dto.zandakaKari07, dto.zandakaKashi07, dto.zandakaKari08, dto.zandakaKashi08, dto.zandakaKari09, dto.zandakaKashi09, dto.zandakaKari09Shu, dto.zandakaKashi09Shu, dto.zandakaKari10, dto.zandakaKashi10, dto.zandakaKari11, dto.zandakaKashi11, dto.zandakaKari12, dto.zandakaKashi12, dto.zandakaKari12Shu, dto.zandakaKashi12Shu
				, dto.kamokuNaibuCd, dto.futanBumonName, dto.chouhyouShaturyokuNo, dto.kamokuNameRyakushiki, dto.kamokuNameSeishiki, dto.taishakuZokusei, dto.zandakaKari00, dto.zandakaKashi00, dto.zandakaKari01, dto.zandakaKashi01, dto.zandakaKari02, dto.zandakaKashi02, dto.zandakaKari03, dto.zandakaKashi03, dto.zandakaKari03Shu, dto.zandakaKashi03Shu, dto.zandakaKari04, dto.zandakaKashi04, dto.zandakaKari05, dto.zandakaKashi05, dto.zandakaKari06, dto.zandakaKashi06, dto.zandakaKari06Shu, dto.zandakaKashi06Shu, dto.zandakaKari07, dto.zandakaKashi07, dto.zandakaKari08, dto.zandakaKashi08, dto.zandakaKari09, dto.zandakaKashi09, dto.zandakaKari09Shu, dto.zandakaKashi09Shu, dto.zandakaKari10, dto.zandakaKashi10, dto.zandakaKari11, dto.zandakaKashi11, dto.zandakaKari12, dto.zandakaKashi12, dto.zandakaKari12Shu, dto.zandakaKashi12Shu
				);
    }
	
	/**
	 * 負担部門科目残高から主キー指定でレコードを削除
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @param kessankiBangou 決算期番号
	 * @return 削除件数
	 */
	public int delete(String futanBumonCd, String kamokuGaibuCd, int kessankiBangou){
		final String sql = "DELETE FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ? AND kamoku_gaibu_cd = ? AND kessanki_bangou = ?";
		return connection.update(sql, futanBumonCd, kamokuGaibuCd, kessankiBangou);
	}
	
	/**
	 * 負担部門科目残高から一部キー指定でレコードを削除
	 * @param futanBumonCd 負担部門コード
	 * @return 削除件数
	 */
	public int delete(String futanBumonCd) {
		final String sql = "DELETE FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ? ";
		return connection.update(sql, futanBumonCd);
	}
	
	/**
	 * 負担部門科目残高から一部キー指定でレコードを削除
	 * @param futanBumonCd 負担部門コード
	 * @param kamokuGaibuCd 科目外部コード
	 * @return 削除件数
	 */
	public int delete(String futanBumonCd, String kamokuGaibuCd) {
		final String sql = "DELETE FROM bumon_kamoku_zandaka WHERE futan_bumon_cd = ?  AND kamoku_gaibu_cd = ? ";
		return connection.update(sql, futanBumonCd, kamokuGaibuCd);
	}
}