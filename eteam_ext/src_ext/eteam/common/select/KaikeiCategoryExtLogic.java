package eteam.common.select;

import eteam.base.GMap;

public class KaikeiCategoryExtLogic extends KaikeiCategoryLogic {
	/* 取引(仕訳)一覧(torihiki_ichiran) */
	/**
	 * 伝票種別をキーに取引(仕訳)一覧を取得する。（取引選択画面用）
	 * @param denpyouKbn 伝票種別
	 * @param bumonCd （所属）部門コード
	 * @param shiwakeEdano 仕訳枝番号 ◀カスタマイズ
	 * @return 取引のリスト
	 */
	public GMap findTorihiki(String denpyouKbn, String bumonCd,int shiwakeEdano) {
		String sql =
			"SELECT "
		+ "  sp.*, "
		+ "  km.karikanjou_keshikomi_no_flg, "
		+ "  km.kamoku_name_ryakushiki, "
		+ "  km.shori_group, "
		+ "  bm.futan_bumon_name, "
		+ "  ke.edaban_name "
		+ "FROM shiwake_pattern_master sp "
		+ "LEFT OUTER JOIN kamoku_master km ON "
		+ "  sp.kari_kamoku_cd = km.kamoku_gaibu_cd "
		+ "LEFT OUTER JOIN kamoku_edaban_zandaka ke ON "
		+ "  sp.kari_kamoku_cd = ke.kamoku_gaibu_cd "
		+ "  AND sp.kari_kamoku_edaban_cd = ke.kamoku_edaban_cd "
		+ "LEFT OUTER JOIN bumon_master bm ON "
		+ "  sp.kari_futan_bumon_cd = bm.futan_bumon_cd "
		+ "WHERE "
		+ "  sp.denpyou_kbn = ? "
		+ "  AND sp.shiwake_edano =?"
		+ "  AND current_date BETWEEN sp.yuukou_kigen_from AND sp.yuukou_kigen_to "
		+ "  AND sp.delete_flg = '0' "
		+ "  AND ( "
		+ "    (sp.default_hyouji_flg = '1') "
		+ "    OR (exists (SELECT * FROM shozoku_bumon_shiwake_pattern_master tmp WHERE (tmp.denpyou_kbn, tmp.shiwake_edano, tmp.bumon_cd)=(sp.denpyou_kbn, sp.shiwake_edano, ?)))) "
		+ "ORDER BY "
		+ "  sp.hyouji_jun ";
		return connection.find(sql, denpyouKbn, shiwakeEdano,bumonCd);
	}
}
