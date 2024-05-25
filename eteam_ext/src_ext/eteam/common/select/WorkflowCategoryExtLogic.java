package eteam.common.select;

import java.util.List;

import eteam.base.GMap;

public class WorkflowCategoryExtLogic extends WorkflowCategoryLogic {
	/* e文書ファイル(ebunsho_file) */
	/* e文書データ(ebunsho_data) */
		/**
		 * 伝票IDをキーに「e文書ファイル」から
		 * 添付ファイル枝番、e文書番号を取得する。
		 * @param  denpyouId  String 伝票ID
		 * @return 検索結果 リスト
		 */
		public List<GMap> selectTenpufileListWithEbunshoNo(String denpyouId) {
			final String sql = "select"
							+ " t.denpyou_id,"
							+ " t.edano,"
							+ " t.file_name,"
							+ " eb.ebunsho_no,"
							+ " eb.denshitorihiki_flg,"
							+ " eb.tsfuyo_flg,"
							+ " eb.touroku_time"
							+ " from tenpu_file t "
							+ " LEFT OUTER JOIN "
							+ " (select"
							+ " ef.denpyou_id,"
							+ " ef.ebunsho_no,"
							+ " ef.edano,"
							+ " ef.denshitorihiki_flg,"
							+ " ef.tsfuyo_flg,"
							+ " ef.touroku_time"
							+ " from ebunsho_file ef "
							+ " WHERE ef.denpyou_id = ? ) eb"
							+ " ON  t.denpyou_id = eb.denpyou_id and t.edano = eb.edano"
							+ " WHERE t.denpyou_id = ? "
							//▼カスタマイズ　filesize０は除外
							+ "AND t.file_size <> 0"
							+ " ORDER BY t.denpyou_id,t.edano;";
					return connection.load(sql, denpyouId, denpyouId);
		}
		
}
