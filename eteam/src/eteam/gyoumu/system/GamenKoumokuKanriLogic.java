package eteam.gyoumu.system;

import eteam.base.EteamAbstractLogic;

/**
 * 画面項目管理機能Logic
 */
public class GamenKoumokuKanriLogic extends EteamAbstractLogic {
	
	/**
	 * 画面項目制御を更新する
	 * @param denpyouKbn        伝票区分
	 * @param koumokuId         項目ID
	 * @param koumokuName       項目名
	 * @param hyoujiFlg         表示フラグ
	 * @param hissuFlg          必須フラグ
	 * @param pdfHyoujiFlg      帳票表示フラグ
	 * @param codeOutputFlg     帳票コード印字フラグ
	 * @return 結果
	 */
	public int updateGamenKoumokuSeigyo(String denpyouKbn, String koumokuId, String koumokuName, String hyoujiFlg, String hissuFlg, String pdfHyoujiFlg, String codeOutputFlg) {
		final String sql = "UPDATE gamen_koumoku_seigyo SET koumoku_name = ? , hyouji_flg = ? ,hissu_flg = ? , pdf_hyouji_flg = ? , code_output_flg = ? WHERE denpyou_kbn = ? AND koumoku_id = ? ";
		return connection.update(sql, koumokuName, hyoujiFlg, hissuFlg, pdfHyoujiFlg, codeOutputFlg, denpyouKbn, koumokuId);
	}
}
