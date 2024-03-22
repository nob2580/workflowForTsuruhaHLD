package eteam.gyoumu.houjincard;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.SystemKanriCategoryLogic;

/**
 * 法人カード管理用Logic
 */
public class HoujinCardLogic extends EteamAbstractLogic {

	/** カード番号を紐づける種別 */
	private final String joinOnCardBangou = "             ON  ((h.card_bangou = ui.houjin_card_shikibetsuyou_num AND (h.card_shubetsu = '2' OR h.card_shubetsu = '3') ) ";

	/** 社員番号を紐づける種別 */
	
// Ver22.06.06.00 Bizプリカ追加対応 *-	
// private final String joinOnShainBangou =  "               OR (h.shain_bangou = ui.houjin_card_shikibetsuyou_num AND (h.card_shubetsu = '1' OR h.card_shubetsu = '4') )) ";
	private final String joinOnShainBangou =  "               OR (h.shain_bangou = ui.houjin_card_shikibetsuyou_num AND (h.card_shubetsu = '1' OR h.card_shubetsu = '4' OR h.card_shubetsu = '51') )) ";
// -*	
	
	//TODO 法人カード管理用のロジックを実装

	//TODO insertは共通にして引数のみ追加していく方式とする？
	//     比較はフォーマットごとでよいはず
	//TODO 取込先伝票IDは後で更新させる
	/**
	 * カード履歴情報を追加
	 * @param card_shubetsu カード種別
	 * @param busho_cd  	部署コード
	 * @param shain_bangou  	社員番号
	 * @param shiyousha  	使用者
	 * @param riyoubi  	利用日
	 * @param kingaku  	金額
	 * @param card_bangou  	カード番号
	 * @param kameiten  	加盟店
	 * @param gyoushu_cd  	業種コード
	 * @param jyogai_flg  	除外フラグ
	 * @param jyogai_riyuu  	除外理由
	 * @param user_id  	更新者ID
	 * @return 結果
	 */
	public int insert(
			String card_shubetsu,
			String busho_cd,
			String shain_bangou,
			String shiyousha,
			Date riyoubi,
			BigDecimal kingaku,
			String card_bangou,
			String kameiten,
			String gyoushu_cd,
			String jyogai_flg,
			String jyogai_riyuu,
			String user_id) {
		final String sql = ""
				+ "INSERT INTO houjin_card_jouhou "
				+ "( "
				+ " card_shubetsu, "
				+ " torikomi_denpyou_id, "
				+ " busho_cd, "
				+ " shain_bangou, "
				+ " shiyousha, "
				+ " riyoubi, "
				+ " kingaku, "
				+ " card_bangou, "
				+ " kameiten, "
				+ " gyoushu_cd, "
				+ " jyogai_flg, "
				+ " jyogai_riyuu, "
				+ " touroku_user_id, "
				+ " touroku_time, "
				+ " koushin_user_id, "
				+ " koushin_time "
				+ ") "		//TODO カード種別定数化
				+ " VALUES(?, '', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp) ";
		return connection.update(sql, card_shubetsu, busho_cd, shain_bangou, shiyousha, riyoubi, kingaku, card_bangou, kameiten, gyoushu_cd, jyogai_flg, jyogai_riyuu, user_id, user_id);
	}

	/**
	 * 取込先伝票ID情報を更新
	 * @param card_jouhou_id カード情報ID
	 * @param denpyou_id 取込先伝票ID
	 * @return 結果
	 */
	public int updateDenpyouHimozuke(long card_jouhou_id,String denpyou_id){
		final String sql = "UPDATE houjin_card_jouhou SET torikomi_denpyou_id = ? WHERE card_jouhou_id = ? ";
		return connection.update(sql, denpyou_id, card_jouhou_id);
	}

	/**
	 * 取込先伝票ID情報をリセット
	 * @param denpyou_id 取込先伝票ID
	 * @return 結果
	 */
	public int removeDenpyouHimozuke(String denpyou_id){
		final String sql = "UPDATE houjin_card_jouhou SET torikomi_denpyou_id = '' WHERE torikomi_denpyou_id = ? ";
		return connection.update(sql,denpyou_id);
	}

	/**
	 * 指定したカード情報IDの履歴データについて、除外済でないかと別伝票に紐付済でないかをチェック
	 * @param card_jouhou_id カード情報ID
	 * @param denpyou_id 編集中伝票ID
	 * @return 結果(0:使用OK(未紐付) -1:除外済 -2:別伝票登録済 -99:カード情報履歴なし)
	 */
	public int checkHimodukeUsed(long card_jouhou_id,String denpyou_id){
		final String sql = "SELECT * FROM houjin_card_jouhou WHERE card_jouhou_id = ?";
		GMap mp = connection.find(sql, card_jouhou_id);

		if(mp == null) {
			//カード情報履歴なし
			return -99;
		}else{
			String jyogaiFlg = mp.get("jyogai_flg");
			if(jyogaiFlg.equals("1")) {
				//該当履歴データ除外済み
				return -1;
			}
			String torikomiId = mp.get("torikomi_denpyou_id");
			if( !isEmpty(torikomiId) && !(torikomiId.equals(denpyou_id)) ) {
				//別伝票登録済
				return -2;
			}
		}
		//使用OK(未紐付)
		return 0;
	}

	/**
	 * 重複データがあるかチェック
	 * @param card_shubetsu カード種別
	 * @param busho_cd 部署コード
	 * @param shain_no 社員No
	 * @param shiyousha 使用者
	 * @param riyoubi 利用日
	 * @param kingaku 金額
	 * @param card_bangou カード番号
	 * @param kameiten 加盟店
	 * @param gyoushu_cd 業種コード
	 * @return 結果
	 */
	public boolean isDuplicate(
			String card_shubetsu,
			String busho_cd,
			String shain_no,
			String shiyousha,
			Date riyoubi,
			BigDecimal kingaku,
			String card_bangou,
			String kameiten,
			String gyoushu_cd) {
		final String sql = "SELECT COUNT(*) AS cnt FROM houjin_card_jouhou"
				+ " WHERE card_shubetsu = ? "		//TODO 定数化
				+ " AND busho_cd = ? "
				+ " AND shain_bangou = ? "
				+ " AND shiyousha = ? "
				+ " AND riyoubi = ? "
				+ " AND kingaku = ? "
				+ " AND card_bangou = ? "
				+ " AND kameiten = ? "
				+ " AND gyoushu_cd = ? ";
		GMap mp = connection.find(sql, card_shubetsu, busho_cd, shain_no, shiyousha, riyoubi, kingaku, card_bangou, kameiten, gyoushu_cd);
		long cnt = (long)mp.get("cnt");
		return cnt > 0 ? true:false;
	}

	/**
	 * カード履歴情報テーブルのデータ件数を取得する。データがなければサイズ0。
	 * @param searchCardShubetsu カード種別
	 * @param userId ユーザーID
	 * @param bushoCd 部署コード
	 * @param dateFrom 利用日From
	 * @param dateTo 利用日To
	 * @param seisanFlg 精算済フラグ  0:未精算 1:精算済 2:全て
	 * @param jogaiHidden 除外済非表示
	 * @param keiriFlg admin・経理ユーザーか否か
	 * @return 検索結果件数
	 */
	public long findCardRirekiCount(String searchCardShubetsu, String userId, String bushoCd, Date dateFrom, Date dateTo, String seisanFlg, boolean jogaiHidden, boolean keiriFlg){
		StringBuffer sql = new StringBuffer();
		sql.append( "         SELECT COUNT(DISTINCT h.card_jouhou_id) AS datacount ");
		sql.append( "           FROM houjin_card_jouhou h ");
		sql.append( "             LEFT OUTER JOIN user_info ui ");
		sql.append( this.joinOnCardBangou);
		sql.append( this.joinOnShainBangou);
		sql.append( "	       WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();

		//カード種別での絞り込み
		if ( !isEmpty(searchCardShubetsu) ) {
			sql.append(" AND card_shubetsu = ? ");
			params.add(searchCardShubetsu);
		}

		// ユーザーでの絞込み
		if ( !keiriFlg || !isEmpty(userId) ) {
			sql.append(" AND ui.user_id = ? ");
			params.add(userId);
		}

		//部署コードでの絞り込み
		if ( !isEmpty(bushoCd) ) {
			sql.append(" AND busho_cd like ? ");
			params.add(bushoCd);
		}

		//利用日での絞込み
		if(dateFrom != null && dateTo != null){
			sql.append(" AND riyoubi BETWEEN ? AND ? ");
			params.add(dateFrom);
			params.add(dateTo);
		}else if(dateFrom != null){
			sql.append(" AND ? <= riyoubi ");
			params.add(dateFrom);

		}else if(dateTo != null){
			sql.append(" AND riyoubi <= ? ");
			params.add(dateTo);
		}

		// 精算済フラグ
		if ("0".equals(seisanFlg)) {
			sql.append(" AND torikomi_denpyou_id = '' ");
		}
		if ("1".equals(seisanFlg)) {
			sql.append(" AND torikomi_denpyou_id <> '' ");
		}

		//除外フラグ非表示
		if (jogaiHidden) {
			sql.append(" AND jyogai_flg = '0' ");
		}

		GMap datacount = connection.find(sql.toString(), params.toArray());
		long count = (long)datacount.get("datacount");
		return count;
	}

	/**
	 * カード履歴情報一覧を取得する
	 * @param searchCardShubetsu カード種別
	 * @param userId       ユーザーID
	 * @param bushoCd      部署コード
	 * @param dateFrom     利用日From
	 * @param dateTo       利用日To
	 * @param seisanFlg    精算済フラグ  0:未精算 1:精算済 2:全て
	 * @param jogaiHidden 除外済非表示
	 * @param sortKbn      ソート区分
	 * @param keiriFlg     admin・経理ユーザーか否か
	 * @param pageNo       ページ番号
	 * @param pageMax      １ページ最大表示件数
	 * @return 通知一覧リスト
	 */
	public List<GMap> loadCardRireki(String searchCardShubetsu, String userId, String bushoCd, Date dateFrom, Date dateTo, String seisanFlg, boolean jogaiHidden, String sortKbn, boolean keiriFlg, int pageNo, int pageMax) {

		StringBuffer sql = new StringBuffer();
		sql.append( "         SELECT DISTINCT h.*, ");
		sql.append( "               dsi.denpyou_kbn, ");
		sql.append( "               dsi.denpyou_shubetsu_url ");
		sql.append( "           FROM houjin_card_jouhou h ");
		sql.append( "             LEFT OUTER JOIN user_info ui ");
		sql.append( this.joinOnCardBangou);
		sql.append(this.joinOnShainBangou);
		sql.append( "             LEFT OUTER JOIN denpyou_shubetsu_ichiran dsi ");
		sql.append( "             ON SUBSTR(h.torikomi_denpyou_id, 8, 4) = dsi.denpyou_kbn ");
		sql.append( "	       WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();

		//カード種別での絞り込み
		if ( !isEmpty(searchCardShubetsu) ) {
			sql.append(" AND card_shubetsu = ? ");
			params.add(searchCardShubetsu);
		}

		// ユーザーでの絞込み
		if ( !keiriFlg || !isEmpty(userId) ) {
			sql.append(" AND ui.user_id = ? ");
			params.add(userId);
		}

		//部署コードでの絞り込み
		if ( !isEmpty(bushoCd) ) {
			sql.append(" AND busho_cd like ? ");
			params.add(bushoCd);
		}

		//利用日での絞込み
		if(dateFrom != null && dateTo != null){
			sql.append(" AND riyoubi BETWEEN ? AND ? ");
			params.add(dateFrom);
			params.add(dateTo);
		}else if(dateFrom != null){
			sql.append(" AND ? <= riyoubi ");
			params.add(dateFrom);

		}else if(dateTo != null){
			sql.append(" AND riyoubi <= ? ");
			params.add(dateTo);
		}

		// 精算済フラグ
		if ("0".equals(seisanFlg)) {
			sql.append(" AND torikomi_denpyou_id = '' ");
		}
		if ("1".equals(seisanFlg)) {
			sql.append(" AND torikomi_denpyou_id <> '' ");
		}

		//除外フラグ非表示
		if (jogaiHidden) {
			sql.append(" AND jyogai_flg = '0' ");
		}

		/* 条件①：ソート区分 */
		if (sortKbn == null || sortKbn.isEmpty()) {
			// デフォルトは利用日降順・社員No昇順・部署昇順でソート
			sql.append(" ORDER BY h.riyoubi DESC, h.shain_bangou ASC, h.busho_cd ASC ");
		} else if ("1".equals(sortKbn)) {
			// ソート区分"1":利用日昇順
			sql.append(" ORDER BY h.riyoubi ASC, h.shain_bangou ASC, h.busho_cd ASC ");
		} else if ("2".equals(sortKbn)) {
			// ソート区分"2":利用日降順
			sql.append(" ORDER BY h.riyoubi DESC, h.shain_bangou ASC, h.busho_cd ASC ");
		} else if ("3".equals(sortKbn)) {
			// ソート区分"3":部署昇順
			sql.append(" ORDER BY h.busho_cd ASC, h.riyoubi DESC, h.shain_bangou ASC ");
		} else if ("4".equals(sortKbn)) {
			// ソート区分"4":部署降順
			sql.append(" ORDER BY h.busho_cd DESC, h.riyoubi DESC, h.shain_bangou ASC ");
		} else if ("5".equals(sortKbn)) {
			// ソート区分"5":社員No昇順
			sql.append(" ORDER BY h.shain_bangou ASC, h.riyoubi DESC, h.busho_cd ASC ");
		} else if ("6".equals(sortKbn)) {
			// ソート区分"6":社員No降順
			sql.append(" ORDER BY h.shain_bangou DESC, h.riyoubi DESC, h.busho_cd ASC ");
		}

		/* 条件②：ページ番号（取得件数）*/
		sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));

		return connection.load(sql.toString(), params.toArray());
	}


	/**
	 * カード履歴情報一覧を取得する(ダイアログ向け)
	 * @param searchCardShubetsu カード種別
	 * @param userId       ユーザーID
	 * @param dateFrom     利用日From
	 * @param dateTo       利用日To
	 * @param sortKbn      ソート区分
	 * @param anyFlg       全取得フラグ(経費立替精算の代理起票用)
	 * @param denpyouId    編集中伝票ID
	 * @return 通知一覧リスト
	 */
	public List<GMap> loadCardRirekiForSentakuDialog(String searchCardShubetsu, String userId, Date dateFrom, Date dateTo, String sortKbn, boolean anyFlg, String denpyouId) {

		StringBuffer sql = new StringBuffer();
		sql.append( "         SELECT DISTINCT h.* ");
		sql.append( "           FROM houjin_card_jouhou h ");
		sql.append( "             LEFT OUTER JOIN user_info ui ");
		sql.append(this.joinOnCardBangou);
		sql.append(this.joinOnShainBangou);
		sql.append( "	       WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();

		//別伝票取込済みか、除外フラグ1のデータは取得しない
		sql.append( " AND ( (h.torikomi_denpyou_id = '' ");
		if ( !isEmpty(denpyouId) ) {
			//ただし編集中伝票=取込伝票の場合はデータは取得しておく
			sql.append( " OR h.torikomi_denpyou_id = ? ");
			params.add(denpyouId);
		}
		sql.append( " ) AND h.jyogai_flg <> '1') ");

		//カード種別での絞り込み
		if ( !isEmpty(searchCardShubetsu) ) {
			sql.append(" AND card_shubetsu = ? ");
			params.add(searchCardShubetsu);
		}

		// ユーザーでの絞込み
		if(!anyFlg){
			sql.append(" AND ui.user_id = ? ");
			params.add(userId);
		}

		//利用日での絞込み
		if(dateFrom != null && dateTo != null){
			sql.append(" AND riyoubi BETWEEN ? AND ? ");
			params.add(dateFrom);
			params.add(dateTo);
		}else if(dateFrom != null){
			sql.append(" AND ? <= riyoubi ");
			params.add(dateFrom);

		}else if(dateTo != null){
			sql.append(" AND riyoubi <= ? ");
			params.add(dateTo);
		}

		/* 条件①：ソート区分 */
		if (sortKbn == null || sortKbn.isEmpty()) {
			// デフォルトは利用日降順・社員No昇順・部署昇順でソート
			sql.append(" ORDER BY h.riyoubi DESC, h.shain_bangou ASC, h.busho_cd ASC ");
		} else if ("1".equals(sortKbn)) {
			// ソート区分"1":利用日昇順
			sql.append(" ORDER BY h.riyoubi ASC, h.shain_bangou ASC, h.busho_cd ASC ");
		} else if ("2".equals(sortKbn)) {
			// ソート区分"2":利用日降順
			sql.append(" ORDER BY h.riyoubi DESC, h.shain_bangou ASC, h.busho_cd ASC ");
		}

		return connection.load(sql.toString(), params.toArray());
	}

	/**
	 * 入力されたyyyyMMddをDate型に変換して返却
	 * @param yyyyMMdd 日付文字列(yyyyMMdd形式)
	 * @return Date型yyyyMMdd
	 */
	public Date toDateyyyyMMdd(String yyyyMMdd){
		Date retDate;
		try {
			retDate = new Date(new SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return retDate;
	}

	/**
	 * 入力されたyyyy/M/dをDate型に変換して返却
	 * @param yyyyMd 日付文字列(yyyy/M/d形式)
	 * @return Date型yyyyMMdd
	 */
	public Date toDateyyyyMd(String yyyyMd){
		Date retDate;
		try {
			retDate = new Date(new SimpleDateFormat("yyyy/M/d").parse(yyyyMd).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return retDate;
	}

	/**
	 * ユーザーIDに紐付く対象カード種別を取得する
	 * @param userId ユーザーID
	 * @return カード種別
	 */
	public String getCardShubtsu(String userId){
		final String sql = "SELECT * FROM houjin_card_import WHERE user_id = ?";
		GMap mp = connection.find(sql, userId);
		if(null == mp){
			// カード種別が未保存の場合、法人カード種別の表示順先頭のコード値を返却
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			return common.loadNaibuCdSetting("houjin_card_shubetsu").get(0).get("naibu_cd").toString();
		}else{
			return mp.get("houjin_card_shubetsu").toString();
		}
	}

	/**
	 * ユーザーIDに紐付く対象カード種別を更新する
	 * @param userId ユーザーID
	 * @param cardShubetsu 対象カード種別
	 */
	public void insertCardShubetsu(String userId, String cardShubetsu) {
		connection.update("DELETE FROM houjin_card_import WHERE user_id = ?", userId);
		connection.update("INSERT INTO houjin_card_import VALUES (?, ?)", userId, cardShubetsu);
	}

	/**
	 * 指定法人カード履歴のカード番号と指定ユーザーの法人カード識別用番号が一致するかチェック
	 * @param houjinRirekiNo 法人カード履歴番号ID
	 * @param userId ユーザーID
	 * @return 一致するならtrue
	 */
	public boolean isValidUserForCardMeisai(String houjinRirekiNo, String userId) {
		final String sql = "SELECT COUNT(*) AS cnt FROM houjin_card_jouhou h "
						 + " INNER JOIN user_info ui "
						 + "    ON h.card_jouhou_id = ? "
						 + this.joinOnCardBangou.replace("ON", "AND")
						 + this.joinOnShainBangou
						 + "   AND ui.user_id = ?"
						 + "   AND ui.houjin_card_riyou_flag = '1' ";
		GMap map = connection.find(sql.toString(), Integer.parseInt(houjinRirekiNo), userId);
		return ((long)map.get("cnt")) > 0;
	}

	/**
	 * 法人カード履歴選択除外対象フラグを変更する
	 * @param cardJouhouId カード情報ID
	 * @param jyogaiChangeFlg 除外データフラグ　1:除外、0:復活
	 * @param jyogaiRiyuu 除外理由
	 */
	public void setJyogaiFlg(String cardJouhouId, String jyogaiChangeFlg, String jyogaiRiyuu) {
		String sql;
		if(jyogaiChangeFlg.equals("1")) {
			//除外設定(もし既に伝票紐付されていた場合は処理しない)※同時に処理実行されたパターン等
			sql = "UPDATE houjin_card_jouhou SET jyogai_flg = ?, jyogai_riyuu = ? WHERE card_jouhou_id = ? AND torikomi_denpyou_id = ''";
			connection.update(sql, jyogaiChangeFlg, jyogaiRiyuu, Integer.parseInt(cardJouhouId));
		}else if(jyogaiChangeFlg.equals("0")) {
			//除外解除設定
			sql = "UPDATE houjin_card_jouhou SET jyogai_flg = ?, jyogai_riyuu = ? WHERE card_jouhou_id = ?";
			connection.update(sql, jyogaiChangeFlg, jyogaiRiyuu, Integer.parseInt(cardJouhouId));
		}
	}
}
