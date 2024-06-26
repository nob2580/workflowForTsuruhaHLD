package eteam.database.abstractdao;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.database.dto.RyohiseisanMeisai;

/**
 * 旅費精算明細に対する標準データ操作クラス
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
public abstract class RyohiseisanMeisaiAbstractDao extends EteamAbstractLogic {

	/**
	 * insert文定型部
	 */
	protected final String insertSql = "INSERT INTO ryohiseisan_meisai (denpyou_id, denpyou_edano, kikan_from, kikan_to, kyuujitsu_nissuu, shubetsu_cd, shubetsu1, shubetsu2, haya_flg, yasu_flg, raku_flg, koutsuu_shudan, shouhyou_shorui_hissu_flg, ryoushuusho_seikyuusho_tou_flg, naiyou, bikou, oufuku_flg, houjin_card_riyou_flg, kaisha_tehai_flg, jidounyuuryoku_flg, nissuu, tanka, suuryou_nyuryoku_type, suuryou, suuryou_kigou, meisai_kingaku, ic_card_no, ic_card_sequence_no, himoduke_card_meisai, touroku_user_id, touroku_time, koushin_user_id, koushin_time, shiharaisaki_name, jigyousha_kbn, zeinuki_kingaku, shouhizeigaku, zeigaku_fix_flg) VALUES "
			+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp, ?, ?, ?, ?, ?)";

	/**
	 * 主キー条件定型部
	 */
	protected final String whereSql = " WHERE denpyou_id = ? AND denpyou_edano = ?";

	/**
	 * 否認済
	 */
	protected final String denpyouHinin = EteamNaibuCodeSetting.DENPYOU_JYOUTAI.HININ_ZUMI;
	/**
	 * 取下済
	 */
	protected final String denpyouTorikeshi = EteamNaibuCodeSetting.DENPYOU_JYOUTAI.TORISAGE_ZUMI;
	/**
	 * INSERT~SELECT文定型部
	 */
	protected final String insertSelectSql = this.insertSql.replace("VALUES (", "SELECT ").replace("timestamp)", "timestamp ");
	/**
	 * ICカード重複回避where文ベース
	 */
	protected final String icInsertWhereBase = " NOT EXISTS "
			+ "  (SELECT ic_card_no,ic_card_sequence_no "
			+ "   FROM TARGET_meisai meisai "
			+ "   INNER JOIN denpyou "
			+ "   ON meisai.denpyou_id = denpyou.denpyou_id "
			+ "   WHERE "
			+ "   meisai.ic_card_no=? AND meisai.ic_card_sequence_no=? AND "
			+ "   '" + denpyouHinin + "' != denpyou.denpyou_joutai "
			+ "   AND '" + denpyouTorikeshi + "' != denpyou.denpyou_joutai) ";
	
	/**
	 * ICカード重複回避where文
	 */
	protected final String icInsertWhere = " WHERE " + this.icInsertWhereBase.replace("TARGET", "koutsuuhiseisan")
	+ " AND " + this.icInsertWhereBase.replace("TARGET", "ryohiseisan")
	+ " AND " + this.icInsertWhereBase.replace("TARGET", "kaigai_ryohiseisan");
	
	/**
	 * @param map GMap
	 * @return dto (レコードが存在しなければNull)
	 */
	protected RyohiseisanMeisai mapToDto(GMap map) {
		return map == null ? null : new RyohiseisanMeisai(map);
	}

	/**
	 * @param mapList 検索結果GMap
	 * @return dtoList
	 */
	protected List<RyohiseisanMeisai> mapToDto(List<GMap> mapList) {
		List<RyohiseisanMeisai> dtoList = new ArrayList<RyohiseisanMeisai>();
		for (var map : mapList) {
			dtoList.add(new RyohiseisanMeisai(map));
		}
		return dtoList;
	}

	/**
	 * 旅費精算明細のレコード有無を判定
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return true:exist false:not exist
	 */
	public boolean exists(String denpyouId, int denpyouEdano) {
		return this.find(denpyouId, denpyouEdano) != null;
	}

	/**
	 * 旅費精算明細から主キー指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return 旅費精算明細DTO
	 */
	public RyohiseisanMeisai find(String denpyouId, int denpyouEdano) {
		final String sql = "SELECT * FROM ryohiseisan_meisai " + this.whereSql;
		return mapToDto(connection.find(sql, denpyouId, denpyouEdano));
	}

	/**
	 * 旅費精算明細からレコードを全件取得 ※大量データ取得に注意
	 * @return List<旅費精算明細DTO>
	 */
	public List<RyohiseisanMeisai> load() {
		final String sql = "SELECT * FROM ryohiseisan_meisai  ORDER BY denpyou_id, denpyou_edano";
		return mapToDto(connection.load(sql));
	}

	/**
	 * 旅費精算明細から一部キー（伝票ID）指定でレコードを取得
	 * @param denpyouId 伝票ID
	 * @return List<旅費精算明細>DTO
	 */
	public List<RyohiseisanMeisai> loadByDenpyouId(String denpyouId) {
		final String sql = "SELECT * FROM ryohiseisan_meisai WHERE denpyou_id = ? "
							+ "ORDER BY denpyou_id, denpyou_edano";
		return mapToDto(connection.load(sql, denpyouId));
	}

	/**
	 * 旅費精算明細登録
	 * @param dto 旅費精算明細
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int insert(RyohiseisanMeisai dto, String koushinUserId) {
		return connection.update(this.insertSql, dto.denpyouId, dto.denpyouEdano, dto.kikanFrom, dto.kikanTo, dto.kyuujitsuNissuu, dto.shubetsuCd, dto.shubetsu1, dto.shubetsu2, dto.hayaFlg, dto.yasuFlg, dto.rakuFlg, dto.koutsuuShudan, dto.shouhyouShoruiHissuFlg, dto.ryoushuushoSeikyuushoTouFlg, dto.naiyou, dto.bikou, dto.oufukuFlg, dto.houjinCardRiyouFlg, dto.kaishaTehaiFlg, dto.jidounyuuryokuFlg, dto.nissuu, dto.tanka, dto.suuryouNyuryokuType, dto.suuryou, dto.suuryouKigou, dto.meisaiKingaku, dto.icCardNo, dto.icCardSequenceNo, dto.himodukeCardMeisai, koushinUserId, koushinUserId, dto.shiharaisakiName, dto.jigyoushaKbn, dto.zeinukiKingaku, dto.shouhizeigaku, dto.zeigakuFixFlg);
	}

	/**
	 * 旅費精算明細更新
	 * @param dto 旅費精算明細
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int update(RyohiseisanMeisai dto, String koushinUserId) {
		final String sql = "UPDATE ryohiseisan_meisai SET  kikan_from = ?, kikan_to = ?, kyuujitsu_nissuu = ?, shubetsu_cd = ?, shubetsu1 = ?, shubetsu2 = ?, haya_flg = ?, yasu_flg = ?, raku_flg = ?, koutsuu_shudan = ?, shouhyou_shorui_hissu_flg = ?, ryoushuusho_seikyuusho_tou_flg = ?, naiyou = ?, bikou = ?, oufuku_flg = ?, houjin_card_riyou_flg = ?, kaisha_tehai_flg = ?, jidounyuuryoku_flg = ?, nissuu = ?, tanka = ?, suuryou_nyuryoku_type = ?, suuryou = ?, suuryou_kigou = ?, meisai_kingaku = ?, ic_card_no = ?, ic_card_sequence_no = ?, himoduke_card_meisai = ?, koushin_user_id = ?, koushin_time = current_timestamp, shiharaisaki_name = ?, jigyousha_kbn = ?, zeinuki_kingaku = ?, shouhizeigaku = ?, zeigaku_fix_flg = ? " + this.whereSql;
		return connection.update(sql, dto.kikanFrom, dto.kikanTo, dto.kyuujitsuNissuu, dto.shubetsuCd, dto.shubetsu1, dto.shubetsu2, dto.hayaFlg, dto.yasuFlg, dto.rakuFlg, dto.koutsuuShudan, dto.shouhyouShoruiHissuFlg, dto.ryoushuushoSeikyuushoTouFlg, dto.naiyou, dto.bikou, dto.oufukuFlg, dto.houjinCardRiyouFlg, dto.kaishaTehaiFlg, dto.jidounyuuryokuFlg, dto.nissuu, dto.tanka, dto.suuryouNyuryokuType, dto.suuryou, dto.suuryouKigou, dto.meisaiKingaku, dto.icCardNo, dto.icCardSequenceNo, dto.himodukeCardMeisai, koushinUserId, dto.shiharaisakiName, dto.jigyoushaKbn, dto.zeinukiKingaku, dto.shouhizeigaku, dto.zeigakuFixFlg, dto.denpyouId, dto.denpyouEdano);
	}

	/**
	 * 旅費精算明細登録or更新
	 * 仕様上Insert/Updateの一方のみ処理される場合は使用不可
	 * @param dto 旅費精算明細
	 * @param koushinUserId 更新ユーザーID
	 * @return 件数
	 */
	public int upsert(RyohiseisanMeisai dto, String koushinUserId) {
		final String sql = this.insertSql
			+ " ON CONFLICT ON CONSTRAINT ryohiseisan_meisai_pkey "
			+ "DO UPDATE SET  kikan_from = ?, kikan_to = ?, kyuujitsu_nissuu = ?, shubetsu_cd = ?, shubetsu1 = ?, shubetsu2 = ?, haya_flg = ?, yasu_flg = ?, raku_flg = ?, koutsuu_shudan = ?, shouhyou_shorui_hissu_flg = ?, ryoushuusho_seikyuusho_tou_flg = ?, naiyou = ?, bikou = ?, oufuku_flg = ?, houjin_card_riyou_flg = ?, kaisha_tehai_flg = ?, jidounyuuryoku_flg = ?, nissuu = ?, tanka = ?, suuryou_nyuryoku_type = ?, suuryou = ?, suuryou_kigou = ?, meisai_kingaku = ?, ic_card_no = ?, ic_card_sequence_no = ?, himoduke_card_meisai = ?, koushin_user_id = ?, koushin_time = current_timestamp, shiharaisaki_name = ?, jigyousha_kbn = ?, zeinuki_kingaku = ?, shouhizeigaku = ?, zeigaku_fix_flg = ? ";
		return connection.update(sql, dto.denpyouId, dto.denpyouEdano, dto.kikanFrom, dto.kikanTo, dto.kyuujitsuNissuu, dto.shubetsuCd, dto.shubetsu1, dto.shubetsu2, dto.hayaFlg, dto.yasuFlg, dto.rakuFlg, dto.koutsuuShudan, dto.shouhyouShoruiHissuFlg, dto.ryoushuushoSeikyuushoTouFlg, dto.naiyou, dto.bikou, dto.oufukuFlg, dto.houjinCardRiyouFlg, dto.kaishaTehaiFlg, dto.jidounyuuryokuFlg, dto.nissuu, dto.tanka, dto.suuryouNyuryokuType, dto.suuryou, dto.suuryouKigou, dto.meisaiKingaku, dto.icCardNo, dto.icCardSequenceNo, dto.himodukeCardMeisai, koushinUserId, koushinUserId, dto.shiharaisakiName, dto.jigyoushaKbn, dto.zeinukiKingaku, dto.shouhizeigaku, dto.zeigakuFixFlg, dto.kikanFrom, dto.kikanTo, dto.kyuujitsuNissuu, dto.shubetsuCd, dto.shubetsu1, dto.shubetsu2, dto.hayaFlg, dto.yasuFlg, dto.rakuFlg, dto.koutsuuShudan, dto.shouhyouShoruiHissuFlg, dto.ryoushuushoSeikyuushoTouFlg, dto.naiyou, dto.bikou, dto.oufukuFlg, dto.houjinCardRiyouFlg, dto.kaishaTehaiFlg, dto.jidounyuuryokuFlg, dto.nissuu, dto.tanka, dto.suuryouNyuryokuType, dto.suuryou, dto.suuryouKigou, dto.meisaiKingaku, dto.icCardNo, dto.icCardSequenceNo, dto.himodukeCardMeisai, koushinUserId, dto.shiharaisakiName, dto.jigyoushaKbn, dto.zeinukiKingaku, dto.shouhizeigaku, dto.zeigakuFixFlg);
	}

	/**
	 * 旅費精算明細から主キー指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @param denpyouEdano 伝票枝番号
	 * @return 削除件数
	 */
	public int delete(String denpyouId, int denpyouEdano) {
		final String sql = "DELETE FROM ryohiseisan_meisai " + this.whereSql;
		return connection.update(sql, denpyouId, denpyouEdano);
	}
	
	/**
	 * 旅費精算明細から一部キー（伝票ID）指定でレコードを削除
	 * @param denpyouId 伝票ID
	 * @return 削除件数
	 */
	public int deleteByDenpyouId(String denpyouId) {
		final String sql = "DELETE FROM ryohiseisan_meisai WHERE denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}
}
