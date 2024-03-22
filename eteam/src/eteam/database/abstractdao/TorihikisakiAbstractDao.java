package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Torihikisaki;

/**
 * 取引先に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class TorihikisakiAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Torihikisaki mapToDto(GMap map){
		return map == null ? null : new Torihikisaki(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Torihikisaki> mapToDto(List<GMap> mapList){
		List<Torihikisaki> dtoList = new ArrayList<Torihikisaki>();
		for (var map : mapList) {
			dtoList.add(new Torihikisaki(map));
		}
		return dtoList;
	}
	
	/**
	 * 取引先のレコード有無を判定
	 * @param torihikisakiCd 取引先コード
	 * @return true:exist false:not exist
	 */
	public boolean exists(String torihikisakiCd) {
		return this.find(torihikisakiCd) == null ? false : true;
	}
	
	/**
	 * 取引先から主キー指定でレコードを取得
	 * @param torihikisakiCd 取引先コード
	 * @return 取引先DTO
	 */
	public Torihikisaki find(String torihikisakiCd) {
		final String sql = "SELECT * FROM torihikisaki WHERE torihikisaki_cd = ?";
		return mapToDto(connection.find(sql, torihikisakiCd));
	}
	
	/**
	 * 取引先からレコードを全件取得 ※大量データ取得に注意
	 * @return List<取引先DTO>
	 */
	public List<Torihikisaki> load() {
		final String sql = "SELECT * FROM torihikisaki ORDER BY torihikisaki_cd";
		return mapToDto(connection.load(sql));
	}

	/**
	* 取引先登録
	* @param dto 取引先
	* @return 件数
	*/
	public int insert(
		Torihikisaki dto
	){
		final String sql =
				"INSERT INTO torihikisaki "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.torihikisakiCd, dto.yuubinBangou, dto.juusho1, dto.juusho2, dto.telno, dto.faxno, dto.kouzaMeiginin, dto.kouzaMeigininFurigana, dto.shiharaiShubetsu, dto.yokinShubetsu, dto.kouzaBangou, dto.tesuuryouFutanKbn, dto.furikomiKbn, dto.furikomiGinkouCd, dto.furikomiGinkouShitenCd, dto.shiharaibi, dto.shiharaiKijitsu, dto.yakujouKingaku, dto.torihikisakiNameHankana, dto.sbusyo, dto.stanto, dto.keicd, dto.nayose, dto.fSetuin, dto.stan, dto.sbcod, dto.skicd, dto.fSoufu, dto.annai, dto.tsokbn, dto.fShitu, dto.cdm2, dto.dm1, dto.dm2, dto.dm3, dto.gendo
					);
	}

	/**
	* 取引先登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto 取引先
	* @return 件数
	*/
	public int upsert(
		Torihikisaki dto
		 ){
		final String sql =
				"INSERT INTO torihikisaki "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT torihikisaki_pkey "
			+ "DO UPDATE SET yuubin_bangou = ?, juusho1 = ?, juusho2 = ?, telno = ?, faxno = ?, kouza_meiginin = ?, kouza_meiginin_furigana = ?, shiharai_shubetsu = ?, yokin_shubetsu = ?, kouza_bangou = ?, tesuuryou_futan_kbn = ?, furikomi_kbn = ?, furikomi_ginkou_cd = ?, furikomi_ginkou_shiten_cd = ?, shiharaibi = ?, shiharai_kijitsu = ?, yakujou_kingaku = ?, torihikisaki_name_hankana = ?, sbusyo = ?, stanto = ?, keicd = ?, nayose = ?, f_setuin = ?, stan = ?, sbcod = ?, skicd = ?, f_soufu = ?, annai = ?, tsokbn = ?, f_shitu = ?, cdm2 = ?, dm1 = ?, dm2 = ?, dm3 = ?, gendo = ? "
			+ "";
			return connection.update(sql,
				dto.torihikisakiCd, dto.yuubinBangou, dto.juusho1, dto.juusho2, dto.telno, dto.faxno, dto.kouzaMeiginin, dto.kouzaMeigininFurigana, dto.shiharaiShubetsu, dto.yokinShubetsu, dto.kouzaBangou, dto.tesuuryouFutanKbn, dto.furikomiKbn, dto.furikomiGinkouCd, dto.furikomiGinkouShitenCd, dto.shiharaibi, dto.shiharaiKijitsu, dto.yakujouKingaku, dto.torihikisakiNameHankana, dto.sbusyo, dto.stanto, dto.keicd, dto.nayose, dto.fSetuin, dto.stan, dto.sbcod, dto.skicd, dto.fSoufu, dto.annai, dto.tsokbn, dto.fShitu, dto.cdm2, dto.dm1, dto.dm2, dto.dm3, dto.gendo
				, dto.yuubinBangou, dto.juusho1, dto.juusho2, dto.telno, dto.faxno, dto.kouzaMeiginin, dto.kouzaMeigininFurigana, dto.shiharaiShubetsu, dto.yokinShubetsu, dto.kouzaBangou, dto.tesuuryouFutanKbn, dto.furikomiKbn, dto.furikomiGinkouCd, dto.furikomiGinkouShitenCd, dto.shiharaibi, dto.shiharaiKijitsu, dto.yakujouKingaku, dto.torihikisakiNameHankana, dto.sbusyo, dto.stanto, dto.keicd, dto.nayose, dto.fSetuin, dto.stan, dto.sbcod, dto.skicd, dto.fSoufu, dto.annai, dto.tsokbn, dto.fShitu, dto.cdm2, dto.dm1, dto.dm2, dto.dm3, dto.gendo
				);
    }
	
	/**
	 * 取引先から主キー指定でレコードを削除
	 * @param torihikisakiCd 取引先コード
	 * @return 削除件数
	 */
	public int delete(String torihikisakiCd){
		final String sql = "DELETE FROM torihikisaki WHERE torihikisaki_cd = ?";
		return connection.update(sql, torihikisakiCd);
	}
}
