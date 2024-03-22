package eteam.gyoumu.shiharai;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst.BUMON_CD;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.SHOUNIN_JOUKYOU;
import eteam.common.EteamNaibuCodeSetting.SHUTSURYOKU_JOUKYOU;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.user.User;

/**
 * 支払依頼申請出力画面Logic
 */
public class ShiharaiIraishoShutsuryokuLogic extends EteamAbstractLogic {

	
	/**
	 * 支払依頼申請出力の検索結果
	 * @param userId ユーザーID
	 * @param shutsuryokuJoukyou 出力状況
	 * @param shiharaiShubetsu 支払種別
	 * @param shouninJoukyou 承認状況
	 * @param keijouBiFrom 計上日From
	 * @param keijouBiTo 計上日To
	 * @param shiharaiBiFrom 支払日From
	 * @param shiharaiBiTo 支払日To
	 * @param denpyouNoFrom 伝票番号From
	 * @param denpyouNoTo 伝票番号To
	 * @param kihyouBumonCd 負担部門コード
	 * @param torihikisakiCd 取引先コード
	 * @param user ログインユーザー
	 * @return kensakuListMap 検索結果
	 */
	public List<GMap> loadShiharaiIraishoShutsuryoku(
			String userId,
			String shutsuryokuJoukyou,
			String shiharaiShubetsu,
			String shouninJoukyou,
			Date   keijouBiFrom,
			Date   keijouBiTo,
			Date   shiharaiBiFrom,
			Date   shiharaiBiTo,
			String denpyouNoFrom,
			String denpyouNoTo,
			String kihyouBumonCd,
			String torihikisakiCd,
			User user) {
		BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

		//------------------------------
		//SQL固定部分
		//------------------------------
		String sql = 
			"SELECT "
		+ "  d.denpyou_id "
		+ "  , d.denpyou_kbn"
		+ "  , sr1.user_full_name "
		+ "  , irai.keijoubi  "
		+ "  , irai.yoteibi "
		+ "  , irai.shiharaibi "
		+ "  , irai.shutsuryoku_flg "
		+ "  , irai.shiharai_houhou "
		+ "  , cd1.name shiharai_houhou_name "
		+ "  , irai.shiharai_shubetsu "
		+ "  , cd2.name shiharai_shubetsu_name "
		+ "  , (CASE WHEN irai.torihikisaki_cd = ? THEN irai.ichigensaki_torihikisaki_name ELSE irai.torihikisaki_name_ryakushiki END) torihikisaki_name "

		//支払依頼
		+ "FROM shiharai_irai irai "
		+ "LEFT OUTER JOIN naibu_cd_setting cd1 ON "
		+ "  cd1.naibu_cd_name = 'shiharai_irai_houhou' AND "
		+ "  cd1.naibu_cd = irai.shiharai_houhou "
		+ "LEFT OUTER JOIN naibu_cd_setting cd2 ON "
		+ "  cd2.naibu_cd_name = 'shiharai_irai_shubetsu' AND "
		+ "  cd2.naibu_cd = irai.shiharai_shubetsu "
		
		//伝票
		+ "JOIN denpyou d ON "
		+ "  d.denpyou_id = irai.denpyou_id "
		//承認ルート（枝番1）
		+ "JOIN shounin_route sr1 ON "
		+ "  sr1.denpyou_id = d.denpyou_id AND "
		+ "  sr1.edano = 1 "
		//承認ルート（現在）
		+ "LEFT OUTER JOIN shounin_route cur ON "
		+ "  cur.denpyou_id = d.denpyou_id AND "
		+ "  cur.genzai_flg = '1' "
		
		+ "WHERE "
		+ "  1 = 1 :WHERE "
		+ "ORDER BY "
		+ "  sr1.bumon_cd"
		+ "  , irai.shiharai_houhou DESC"
		+ "  , irai.shiharai_shubetsu "
		+   "  , irai.yoteibi"
		+ "  , shiharai_kijitsu "			//ここまでがヘッダー部項目（グルーピングキー）
		+ "  , irai.keijoubi "
		+ "  , d.denpyou_id ";
		List<Object> params = new ArrayList<Object>();
		StringBuffer where = new StringBuffer();
		params.add(setting.ichigenCd());
		
		//------------------------------
		//条件により動的WHER区
		//------------------------------
		// 出力状況による検索
		if (! isEmpty(shutsuryokuJoukyou)) {
			where.append("  AND irai.shutsuryoku_flg = ? ");
			params.add(shutsuryokuJoukyou.equals(SHUTSURYOKU_JOUKYOU.MI_INSATSU) ? "0" : "1");
		} 
		
		// 支払種別による検索
		if (! isEmpty(shiharaiShubetsu)) {
			where.append("  AND irai.shiharai_shubetsu = ? ");
			params.add(shiharaiShubetsu);
		}
		
		// 承認状況による検索
		switch(shouninJoukyou) {
			case SHOUNIN_JOUKYOU.MI_SHOUNIN:
				where.append("  AND d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SHINSEI_CHUU  + "' ");
				where.append("  AND cur.edano = 2 ");
				break;
			case SHOUNIN_JOUKYOU.JOUCHOU_SHOUNIN:
				where.append("  AND d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SHINSEI_CHUU  + "' ");
				where.append("  AND cur.edano >= 3 ");
				break;
			case SHOUNIN_JOUKYOU.SAISHU_SHOUNIN:
				where.append("  AND d.denpyou_joutai = '" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "' ");
				break;
			default:
				where.append("  AND d.denpyou_joutai IN ('" + DENPYOU_JYOUTAI.SHINSEI_CHUU  + "','" + DENPYOU_JYOUTAI.SYOUNIN_ZUMI + "') ");
				break;
		}
		
		// 計上日による検索
		if (keijouBiFrom != null) {
			where.append("  AND irai.keijoubi >= ? ");
			params.add(keijouBiFrom);
		}
		if (keijouBiTo != null) {
			where.append("  AND irai.keijoubi <= ? ");
			params.add(keijouBiTo);
		}
		// 支払日による検索
		if (shiharaiBiFrom != null) {
			where.append("  AND irai.shiharaibi >= ? ");
			params.add(shiharaiBiFrom);
		}
		if (shiharaiBiTo != null) {
			where.append("  AND irai.shiharaibi <= ? ");
			params.add(shiharaiBiTo);
		}
		// ユーザーIDによる検索
		if (!userId.isEmpty()) {
			where.append("  AND sr1.user_id = ? ");
			params.add(userId);
		}
		// 伝票番号による検索
		if (!denpyouNoFrom.isEmpty()) {
			where.append("  AND d.serial_no >= ? ");
			params.add(Integer.parseInt(denpyouNoFrom));
		}
		if (!denpyouNoTo.isEmpty()) {
			where.append("  AND d.serial_no <= ? ");
			params.add(Integer.parseInt(denpyouNoTo));
		}
		// 起票部門による検索
		if (!kihyouBumonCd.isEmpty()) {
			where.append("  AND sr1.bumon_cd = ? ");
			params.add(kihyouBumonCd);
		}
		// 取引先による検索
		if (!torihikisakiCd.isEmpty()) {
			where.append("  AND torihikisaki_cd = ? ");
			params.add(torihikisakiCd);
		}
		//アクセス制御
		//部門所属ユーザの場合の条件
		if(user.getGyoumuRoleId() == null){
			where.append("  AND d.denpyou_id IN (SELECT denpyou_id FROM shounin_route WHERE user_id = ?) ");
			params.add(user.getSeigyoUserId());
		}
		//業務ロールユーザの場合の条件
		if (user.getGyoumuRoleId() != null && ! ArrayUtils.contains(user.getGyoumuRoleShozokuBumonCd(), BUMON_CD.ZENSHA)){
			String searchGyoumuRoleShozokuBumonCd = bumonUserLogic.getMergeGyoumuRoleShozokuBumonCd(user.getGyoumuRoleShozokuBumonCd(), true);
			where.append("  AND sr1.bumon_cd IN( " + searchGyoumuRoleShozokuBumonCd + " )");
		}
		sql = sql.replace(":WHERE", where.toString());
		
		return connection.load(sql.toString(), params.toArray());
	}
	
	/**
	 * 支払依頼出力を済にする
	 * @param denpyouId 伝票ID
	 * @param userId ログインユーザーID
	 */
	public void updateShutsuryokuFlg(String denpyouId, String userId) {
		final String sql = "UPDATE shiharai_irai SET shutsuryoku_flg='1', koushin_user_id=?, koushin_time=current_timestamp WHERE denpyou_id=? ";
		connection.update(sql, userId, denpyouId);
	}
}
