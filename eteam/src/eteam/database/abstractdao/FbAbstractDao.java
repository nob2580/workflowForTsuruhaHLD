package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.database.dto.Fb;

/**
 * FB抽出に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class FbAbstractDao extends EteamAbstractLogic {

	/**
	* @param map GMap
	* @return dto (レコードが存在しなければNull)
	*/
	protected Fb mapToDto(GMap map){
		return map == null ? null : new Fb(map);
	}

	/**
	* @param mapList 検索結果GMap
	* @return dtoList
	*/
	protected List<Fb> mapToDto(List<GMap> mapList){
		List<Fb> dtoList = new ArrayList<Fb>();
		for (var map : mapList) {
			dtoList.add(new Fb(map));
		}
		return dtoList;
	}
	
	/**
	 * FB抽出のレコード有無を判定
	 * @param serialNo シリアル番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(long serialNo) {
		return this.find(serialNo) == null ? false : true;
	}
	
	/**
	 * FB抽出から主キー指定でレコードを取得
	 * @param serialNo シリアル番号
	 * @return FB抽出DTO
	 */
	public Fb find(long serialNo) {
		final String sql = "SELECT * FROM fb WHERE serial_no = ?";
		return mapToDto(connection.find(sql, serialNo));
	}
	
	/**
	 * FB抽出からレコードを全件取得 ※大量データ取得に注意
	 * @return List<FB抽出DTO>
	 */
	public List<Fb> load() {
		final String sql = "SELECT * FROM fb ORDER BY serial_no";
		return mapToDto(connection.load(sql));
	}

	/**
	* FB抽出登録
	* @param dto FB抽出
	* @return 件数
	*/
	public int insert(
		Fb dto
	){
		final String sql =
				"INSERT INTO fb "
			+ "( denpyou_id, user_id, fb_status, touroku_time, koushin_time, shubetsu_cd, cd_kbn, kaisha_cd, kaisha_name_hankana, furikomi_date, moto_kinyuukikan_cd, moto_kinyuukikan_name_hankana, moto_kinyuukikan_shiten_cd, moto_kinyuukikan_shiten_name_hankana, moto_yokin_shumoku_cd, moto_kouza_bangou, saki_kinyuukikan_cd, saki_kinyuukikan_name_hankana, saki_kinyuukikan_shiten_cd, saki_kinyuukikan_shiten_name_hankana, saki_yokin_shumoku_cd, saki_kouza_bangou, saki_kouza_meigi_kana, kingaku, shinki_cd, kokyaku_cd1, furikomi_kbn) "
			+ "VALUES(?, ?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ")";
			return connection.update(sql,
					dto.denpyouId, dto.userId, dto.fbStatus, dto.shubetsuCd, dto.cdKbn, dto.kaishaCd, dto.kaishaNameHankana, dto.furikomiDate, dto.motoKinyuukikanCd, dto.motoKinyuukikanNameHankana, dto.motoKinyuukikanShitenCd, dto.motoKinyuukikanShitenNameHankana, dto.motoYokinShumokuCd, dto.motoKouzaBangou, dto.sakiKinyuukikanCd, dto.sakiKinyuukikanNameHankana, dto.sakiKinyuukikanShitenCd, dto.sakiKinyuukikanShitenNameHankana, dto.sakiYokinShumokuCd, dto.sakiKouzaBangou, dto.sakiKouzaMeigiKana, dto.kingaku, dto.shinkiCd, dto.kokyakuCd1, dto.furikomiKbn
					);
	}

	/**
	* FB抽出の非キー全てと共通列を更新
	* 値設定漏れによるデータ消失防止のためDaoで取得したFbの使用を前提
	* @param dto FB抽出
	* @return 件数
	*/
	public int update(
		Fb dto
		 ){
		final String sql =
				"UPDATE fb "
		    + "SET denpyou_id = ?, user_id = ?, fb_status = ?, koushin_time = current_timestamp, shubetsu_cd = ?, cd_kbn = ?, kaisha_cd = ?, kaisha_name_hankana = ?, furikomi_date = ?, moto_kinyuukikan_cd = ?, moto_kinyuukikan_name_hankana = ?, moto_kinyuukikan_shiten_cd = ?, moto_kinyuukikan_shiten_name_hankana = ?, moto_yokin_shumoku_cd = ?, moto_kouza_bangou = ?, saki_kinyuukikan_cd = ?, saki_kinyuukikan_name_hankana = ?, saki_kinyuukikan_shiten_cd = ?, saki_kinyuukikan_shiten_name_hankana = ?, saki_yokin_shumoku_cd = ?, saki_kouza_bangou = ?, saki_kouza_meigi_kana = ?, kingaku = ?, shinki_cd = ?, kokyaku_cd1 = ?, furikomi_kbn = ? "
	 		+ "WHERE koushin_time = ? AND serial_no = ?";
			return connection.update(sql,
				dto.denpyouId, dto.userId, dto.fbStatus, dto.shubetsuCd, dto.cdKbn, dto.kaishaCd, dto.kaishaNameHankana, dto.furikomiDate, dto.motoKinyuukikanCd, dto.motoKinyuukikanNameHankana, dto.motoKinyuukikanShitenCd, dto.motoKinyuukikanShitenNameHankana, dto.motoYokinShumokuCd, dto.motoKouzaBangou, dto.sakiKinyuukikanCd, dto.sakiKinyuukikanNameHankana, dto.sakiKinyuukikanShitenCd, dto.sakiKinyuukikanShitenNameHankana, dto.sakiYokinShumokuCd, dto.sakiKouzaBangou, dto.sakiKouzaMeigiKana, dto.kingaku, dto.shinkiCd, dto.kokyakuCd1, dto.furikomiKbn
				,dto.koushinTime, dto.serialNo);
    }

	/**
	* FB抽出登録or更新
	* 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	* @param dto FB抽出
	* @return 件数
	*/
	public int upsert(
		Fb dto
		 ){
		final String sql =
				"INSERT INTO fb "
			+ "( denpyou_id, user_id, fb_status, touroku_time, koushin_time, shubetsu_cd, cd_kbn, kaisha_cd, kaisha_name_hankana, furikomi_date, moto_kinyuukikan_cd, moto_kinyuukikan_name_hankana, moto_kinyuukikan_shiten_cd, moto_kinyuukikan_shiten_name_hankana, moto_yokin_shumoku_cd, moto_kouza_bangou, saki_kinyuukikan_cd, saki_kinyuukikan_name_hankana, saki_kinyuukikan_shiten_cd, saki_kinyuukikan_shiten_name_hankana, saki_yokin_shumoku_cd, saki_kouza_bangou, saki_kouza_meigi_kana, kingaku, shinki_cd, kokyaku_cd1, furikomi_kbn) "
			+ "VALUES(?, ?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
			+ ") ON CONFLICT ON CONSTRAINT fb_pkey "
			+ "DO UPDATE SET denpyou_id = ?, user_id = ?, fb_status = ?, koushin_time = current_timestamp, shubetsu_cd = ?, cd_kbn = ?, kaisha_cd = ?, kaisha_name_hankana = ?, furikomi_date = ?, moto_kinyuukikan_cd = ?, moto_kinyuukikan_name_hankana = ?, moto_kinyuukikan_shiten_cd = ?, moto_kinyuukikan_shiten_name_hankana = ?, moto_yokin_shumoku_cd = ?, moto_kouza_bangou = ?, saki_kinyuukikan_cd = ?, saki_kinyuukikan_name_hankana = ?, saki_kinyuukikan_shiten_cd = ?, saki_kinyuukikan_shiten_name_hankana = ?, saki_yokin_shumoku_cd = ?, saki_kouza_bangou = ?, saki_kouza_meigi_kana = ?, kingaku = ?, shinki_cd = ?, kokyaku_cd1 = ?, furikomi_kbn = ? "
			+ "";
			return connection.update(sql,
				dto.denpyouId, dto.userId, dto.fbStatus, dto.shubetsuCd, dto.cdKbn, dto.kaishaCd, dto.kaishaNameHankana, dto.furikomiDate, dto.motoKinyuukikanCd, dto.motoKinyuukikanNameHankana, dto.motoKinyuukikanShitenCd, dto.motoKinyuukikanShitenNameHankana, dto.motoYokinShumokuCd, dto.motoKouzaBangou, dto.sakiKinyuukikanCd, dto.sakiKinyuukikanNameHankana, dto.sakiKinyuukikanShitenCd, dto.sakiKinyuukikanShitenNameHankana, dto.sakiYokinShumokuCd, dto.sakiKouzaBangou, dto.sakiKouzaMeigiKana, dto.kingaku, dto.shinkiCd, dto.kokyakuCd1, dto.furikomiKbn
				, dto.denpyouId, dto.userId, dto.fbStatus, dto.shubetsuCd, dto.cdKbn, dto.kaishaCd, dto.kaishaNameHankana, dto.furikomiDate, dto.motoKinyuukikanCd, dto.motoKinyuukikanNameHankana, dto.motoKinyuukikanShitenCd, dto.motoKinyuukikanShitenNameHankana, dto.motoYokinShumokuCd, dto.motoKouzaBangou, dto.sakiKinyuukikanCd, dto.sakiKinyuukikanNameHankana, dto.sakiKinyuukikanShitenCd, dto.sakiKinyuukikanShitenNameHankana, dto.sakiYokinShumokuCd, dto.sakiKouzaBangou, dto.sakiKouzaMeigiKana, dto.kingaku, dto.shinkiCd, dto.kokyakuCd1, dto.furikomiKbn
				);
    }
	
	/**
	 * FB抽出から主キー指定でレコードを削除
	 * @param serialNo シリアル番号
	 * @return 削除件数
	 */
	public int delete(long serialNo){
		final String sql = "DELETE FROM fb WHERE serial_no = ?";
		return connection.update(sql, serialNo);
	}
}
