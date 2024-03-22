package eteam.gyoumu.ic;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.gyoumu.ic.ICHistsAction.ICHistsTrnsJson;

/**
 * ICカードLogic
 */
public class ICLogic extends EteamAbstractLogic {

	/**
	 * insert文定型部（キー込み・ユーザー固定でUpsertしない）
	 */
	protected final String insertSql = "INSERT INTO ic_card_rireki (ic_card_no, ic_card_sequence_no, ic_card_riyoubi, tanmatu_cd, line_cd_from, line_name_from, eki_cd_from, eki_name_from, line_cd_to, line_name_to, eki_cd_to, eki_name_to, kingaku, user_id, jyogai_flg, shori_cd, balance, all_byte) VALUES "
			+ "	(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  ON CONFLICT ON CONSTRAINT ic_card_rireki_pkey DO NOTHING";

	
	/**
	 * ユーザートークン登録
	 * @param cardid カードID
	 * @param token トークン
	 * @param userId ユーザーID
	 */
	public void insertUserToken(String cardid, String token, String userId) {
		final String sql1 = "DELETE FROM ic_card WHERE ic_card_no=?";
		connection.update(sql1, cardid);
		final String sql2 = "INSERT INTO ic_card VALUES(?,?,?)";
		connection.update(sql2, cardid, token, userId);
	}

	/**
	 * ICカード情報を検索する
	 * @param icCardNo ICカード番号
	 * @param token トークン
	 * @param userId ユーザーID
	 * @return あればTRUE
	 */
	public boolean findIcCard(String icCardNo, String token, String userId) {
		final String sql = "SELECT COUNT(*) count FROM ic_card WHERE ic_card_no=? AND token=? AND user_id=?";
		long count = (long)connection.find(sql, icCardNo, token, userId).get("count");
		return (count>=1);
	}

	/**
	 * ICカード利用履歴を登録する(電車用)
	 * @param icCardNo ICカード番号
	 * @param tr 履歴json
	 * @param userId ユーザーID
	 * @param jyogaiFlg 除外フラグ
	 * @return 件数
	 */
	public int insertTrain(String icCardNo, ICHistsTrnsJson tr, String userId, String jyogaiFlg){
		final String sqlMas = "SELECT * FROM eki_master WHERE region_cd=? AND line_cd=? AND eki_cd=?";
		GMap fromMas = connection.find(sqlMas, Integer.toHexString(Integer.parseInt(tr.from.region)), Integer.toHexString(Integer.parseInt(tr.from.linecode)), Integer.toHexString(Integer.parseInt(tr.from.station)));
		GMap toMas = connection.find(sqlMas, Integer.toHexString(Integer.parseInt(tr.to.region)), Integer.toHexString(Integer.parseInt(tr.to.linecode)), Integer.toHexString(Integer.parseInt(tr.to.station))); 
		//22120201 駅マスターに登録がないものはコード16進数表示する
		String lineNameFrom = fromMas != null ? (String)fromMas.get("line_name") : Integer.toHexString(Integer.parseInt(tr.from.region))+"・"+Integer.toHexString(Integer.parseInt(tr.from.linecode));
		String ekiNameFrom = fromMas != null ? (String)fromMas.get("eki_name") : Integer.toHexString(Integer.parseInt(tr.from.station));
		String lineNameTo = toMas   != null ? (String)toMas.get("line_name") : Integer.toHexString(Integer.parseInt(tr.to.region))+"・"+Integer.toHexString(Integer.parseInt(tr.to.linecode));
		String ekiNameTo = toMas   != null ? (String)toMas.get("eki_name") : Integer.toHexString(Integer.parseInt(tr.to.station));
		//22120201 路線名と駅名を置き換える
		if(tr.process != null) {
			switch(tr.process) {
				case "4": /*精算*/
				case "5": /*精算*/
				case "6": /*窓出*/
				case "132": /*精算(他社)*/
				case "133": /*精算(他社)*/
					lineNameTo = "";
					ekiNameTo = "";
					break;
				
				case "21": /*出A*/
				case "8": /*控除*/
				case "17": /*再発*/
				case "19": /*支払（新幹線利用）*/
				case "31": /*入金*/
				case "35": /*券購*/
				case "72": /*特典*/
				case "74": /*物販取消*/
				case "75": /*入物*/
				case "203": /*入物*/
					// 駅マスターと突合した結果駅名取得できていなかったら空文字に
					if(checkStringToHex(ekiNameFrom)) {
						lineNameFrom = "";
						ekiNameFrom = "";
					}
					if(checkStringToHex(ekiNameTo)) {
						lineNameTo = "";
						ekiNameTo = "";
					}
					break;
				
				case "2": /*チャージ*/
				case "3": /*券購*/
				case "7": /*新規*/
				case "20": /*入A*/
				case "70": /*物販*/
				case "73": /*入金*/
				case "198": /*物現*/
					lineNameFrom = "路線名データなし";
					ekiNameFrom = "駅名データなし";
					lineNameTo = "路線名データなし";
					ekiNameTo = "駅名データなし";
					break;
					
				default:
						break;
			}
		}
		
		return connection.update(this.insertSql, icCardNo, tr.seq, toDate(tr.date), tr.ctype,
				tr.from.linecode, lineNameFrom, tr.from.station, ekiNameFrom,
				tr.to.linecode,  lineNameTo, tr.to.station, ekiNameTo,
				toDecimal(tr.fare), userId, jyogaiFlg, this.convertToEmptyIfNull(tr.process), isEmpty(tr.balance) ? new BigDecimal(0) : toDecimal(tr.balance), this.convertToEmptyIfNull(tr.bytecd));
	}
	
	/**
	 * ICカード利用履歴を登録する(バス用)
	 * @param icCardNo ICカード番号
	 * @param tr 履歴json
	 * @param userId ユーザーID
	 * @param jyogaiFlg 除外フラグ
	 * @return 件数
	 */
	public int insertBus(String icCardNo, ICHistsTrnsJson tr, String userId, String jyogaiFlg){
		final String sqlMas = "SELECT * FROM bus_line_master WHERE line_cd=?";
		GMap fromMas = connection.find(sqlMas, Integer.toHexString(Integer.parseInt(tr.from.linecode)) ); 
		
		String lineName = fromMas != null ? (String)fromMas.get("line_name") : "バス路線名データなし";
		
		return connection.update(this.insertSql, icCardNo, tr.seq, toDate(tr.date), tr.ctype,
				 tr.from.linecode, lineName, "", "",
				""  , ""  , ""  , "",
				toDecimal(tr.fare), userId, jyogaiFlg, this.convertToEmptyIfNull(tr.process), isEmpty(tr.balance) ? new BigDecimal(0) : toDecimal(tr.balance), this.convertToEmptyIfNull(tr.bytecd));
	}
	
	/**
	 * ICカード履歴選択除外対象フラグを変更する
	 * @param icCardNo ICカード番号
	 * @param seq ICカードシーケンス
	 * @param jyogaiFlg 除外データフラグ
	 */
	public void setJyogaiFlg(String icCardNo, String seq, String jyogaiFlg) {
		final String sql = "UPDATE ic_card_rireki SET jyogai_flg = ? WHERE ic_card_no = ? AND ic_card_sequence_no = ? ";
		connection.update(sql, jyogaiFlg, icCardNo, seq);
	}
	
	/**
	 * Nullの時空文字列を返す
	 * @param string 文字列
	 * @return 結果
	 */
	protected String convertToEmptyIfNull(String string) 
	{
		return isEmpty(string) ? "" : string;
	}
	
	/**
	 * 文字列が16進数かチェックする
	 * @param str 文字列
	 * @return 数値ならtrue
	 */
	protected boolean checkStringToHex(String str) {
		String reg = "[0-9a-fA-F]+";
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}
}
