package eteam.gyoumu.yosanshikkoukanri.kogamen;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.yosanshikkoukanri.KianbangouBoIchiranLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案追加Logic
 */
@Getter @Setter @ToString
public class KianTsuikaLogic extends EteamAbstractLogic {

	/** ワークフローカテゴリー内のSelect文を集約したLogic */
	protected WorkflowCategoryLogic wfCategoryLogic;

	/**
	 * 起案番号簿プルダウンデータ取得<br>
	 * 取得したデータは下記の通り編集する。<br>
	 * <ul>
	 * <li>[マップキー]"key"		[データ]部門コード+"@"+年度+"@"+略号+"@"+開始起案番号
	 * <li>[マップキー]"dispNm"		[データ]年度+" "+略号+" "+区分内容+" "+開始起案番号+"-"+終了起案番号
	 * </ul>
	 * 
	 * @param usr ユーザ情報
	 * @param kensakuBumonCd 起票部門
	 * @return Mapリスト
	 */
	public List<GMap> loadKianbangouBoList(User usr, String kensakuBumonCd){
		List<GMap> lstResult = new ArrayList<GMap>();

		/*
		 * 起案番号簿リスト(内容は起案番号簿一覧と同じだが、ソート順のみ年度、略号、区分内容)を取得する。
		 */
		KianbangouBoIchiranLogic kianbangouBoIchiranLogic = EteamContainer.getComponent(KianbangouBoIchiranLogic.class, super.connection);
		// 検索条件を格納する。
		KianbangouBoIchiranLogic.SearchCondition cond = new KianbangouBoIchiranLogic.SearchCondition();
		// ログインユーザ情報
		cond.loginUser = usr;
		// 起票部門
		cond.kihyouBumonCd = kensakuBumonCd;
		
		// 呼出元判別区分(ソート順設定用)
		cond.yobidashiHanbetsu = 1;

		// 検索を実施する。
		List<GMap> lstKianbangouBo = kianbangouBoIchiranLogic.kensaku(cond);

		for (GMap aMap : lstKianbangouBo){

			/*
			 * キー情報を生成する。
			 */
			StringBuilder sbKey = new StringBuilder();
			sbKey.append(aMap.get("bumonCd").toString()).append("@");
			sbKey.append(aMap.get("nendo").toString()).append("@");
			sbKey.append(aMap.get("ryakugou").toString()).append("@");
			sbKey.append(aMap.get("kianbangouFrom").toString());

			/*
			 * データ情報を生成する。
			 */
			// 略号を全角固定長に編集
			String strRyakugou = aMap.get("ryakugou").toString();
			int iStrCnt = strRyakugou.length();
			int iMaxCnt = 6;
			for (int i = 0; i < (iMaxCnt - iStrCnt); i++){
				strRyakugou += "　";
			}
			// 区分内容を全角固定長に編集
			String strKbnNaiyou = aMap.get("kbnNaiyou").toString();
			iStrCnt = strKbnNaiyou.length();
			iMaxCnt = 10;
			for (int i = 0; i < (iMaxCnt - iStrCnt); i++){
				strKbnNaiyou += "　";
			}
			StringBuilder sbDispNm = new StringBuilder();
			sbDispNm.append(aMap.get("nendo").toString()).append(" ");
			sbDispNm.append(strRyakugou).append(" ");
			sbDispNm.append(strKbnNaiyou).append(" ");
			sbDispNm.append(aMap.get("kianbangouFrom").toString()).append("-").append(aMap.get("kianbangouTo").toString());

			// マップ情報を設定する。
			GMap mapNew = new GMap();
			mapNew.put("key", sbKey.toString());
			mapNew.put("dispNm", sbDispNm.toString());
			lstResult.add(mapNew);
		}

		return lstResult;
	}

	/**
	 * 起案番号簿一覧検索条件クラス
	 */
	public static class SearchCondition {
		/** 予算執行対象 */
		public String yosanShikkouTaishou;
		/** 起案番号簿キー（部門コード） */
		public String boBumonCd;
		/** 起案番号簿キー（年度） */
		public String boNendo;
		/** 起案番号簿キー（略号） */
		public String boRyakugou;
		/** 起案番号簿キー（開始起案番号） */
		public Integer boKianbangouFrom;
		/** 起案番号 */
		public Integer kianbangou;
		/** 件名 */
		public String kenmei;
		/** 伝票種別 */
		public String denpyouShubetsu;
		/** 起案日(from) */
		public Date kianBiFrom;
		/** 起案日(to) */
		public Date kianBiTo;
		/** 最終承認日(from) */
		public Date saishuuShouninBiFrom;
		/** 最終承認日(to) */
		public Date saishuuShouninBiTo;
		/** 起票部門コード */
		public String kihyouBumonCd;

		/**
		 * クラス変数デバッグ文字列返却
		 * 
		 * @return デバッグ文字列
		 */
		public String toDebugString(){
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append("yosanShikkouTaishou=").append(yosanShikkouTaishou);
			sb.append(" boBumonCd=").append(boBumonCd);
			sb.append(" boNendo=").append(boNendo);
			sb.append(" boRyakugou=").append(boRyakugou);
			sb.append(" boKianbangouFrom=").append(boKianbangouFrom);
			sb.append(" kianbangou=").append(kianbangou);
			sb.append(" kenmei=").append(kenmei);
			sb.append(" denpyouShubetsu=").append(denpyouShubetsu);
			sb.append(" kianBiFrom=").append(kianBiFrom);
			sb.append(" kianBiTo=").append(kianBiTo);
			sb.append(" saishuuShouninBiFrom=").append(saishuuShouninBiFrom);
			sb.append(" saishuuShouninBiTo=").append(saishuuShouninBiTo);
			sb.append("]");
			return sb.toString();
		}
		
	};

	/**
	 * 起案追加検索<br>
	 * 起案追加画面で指定された検索条件を使用して検索を実施する。<br>
	 * 指定された検索条件は {@link SearchCondition} に格納されたものを利用。

	 * @param condSearch 起案追加検索条件
	 * @return 検索結果リスト
	 */
	public List<GMap> kensaku(SearchCondition condSearch){

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("   A.denpyou_id")
				 .append("  ,A.denpyou_joutai")
				 .append("  ,B.denpyou_kbn")
				 .append("  ,B.denpyou_shubetsu")
				 .append("  ,B.denpyou_shubetsu_url")
				 .append("  ,B.kianbangou_unyou_flg")
				 .append("  ,B.yosan_shikkou_taishou")
				 .append("  ,C.touroku_time AS kihyouBi")
				 .append("  ,D.touroku_time AS saishuuShouninBi")
				 .append("  ,E.bumon_cd")
				 .append("  ,E.nendo")
				 .append("  ,E.ryakugou")
				 .append("  ,E.kian_bangou_from")
				 .append("  ,E.kian_denpyou")
				 .append("  ,E.kian_denpyou_kbn")
				 .append("  ,E.jisshi_nendo")
				 .append("  ,E.jisshi_kian_bangou")
				 .append("  ,E.shishutsu_nendo")
				 .append("  ,E.shishutsu_kian_bangou")
				 .append("  ,E.ringi_kingaku")
				 .append("  ,COALESCE(F.value1, '') AS kenmei")
				 .append("  ,COALESCE(G.version, 0) AS version")
				 .append(" FROM denpyou AS A")
				 .append(" INNER JOIN denpyou_shubetsu_ichiran AS B ON B.denpyou_kbn = A.denpyou_kbn")
				 .append(" INNER JOIN shounin_route AS C ON C.denpyou_id = A.denpyou_id")
				 .append(" INNER JOIN shounin_joukyou AS D ON D.denpyou_id = A.denpyou_id AND D.edano = (")
				 .append("  SELECT MAX(edano) AS edano")
				 .append("  FROM shounin_joukyou")
				 .append("  WHERE denpyou_id = A.denpyou_id")
				 .append("   AND joukyou_cd = '").append(EteamNaibuCodeSetting.JOUKYOU.SHOUNIN).append("'")
				 .append(" )")
				 .append(" INNER JOIN denpyou_kian_himozuke AS E ON E.denpyou_id = A.denpyou_id")
				 .append(" LEFT OUTER JOIN (")
				 .append("  SELECT F2.denpyou_id, F2.value1")
				 .append("  FROM kani_todoke_koumoku AS F1")
				 .append("  INNER JOIN kani_todoke AS F2 ON F2.denpyou_kbn = F1.denpyou_kbn AND F2.version = F1.version AND F2.item_name = F1.item_name")
				 .append("  WHERE F1.yosan_shikkou_koumoku_id = 'kenmei'")
				 .append(" ) AS F ON F.denpyou_id = A.denpyou_id")
				 .append(" LEFT OUTER JOIN (")
				 .append("  SELECT DISTINCT denpyou_id, version")
				 .append("  FROM kani_todoke")
				 .append(" ) AS G ON G.denpyou_id = A.denpyou_id")
				 .append(" WHERE")
				 .append("   A.denpyou_joutai = '").append(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI).append("'")
				 .append("  AND B.yosan_shikkou_taishou != '").append(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI).append("'")
				 .append("  AND B.yosan_shikkou_taishou != '").append(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI).append("'")
				 .append("  AND C.edano = 1")
				 .append("  AND E.kian_syuryo_flg = '0'");
		// 現在の伝票が支出起案の場合は実施起案番号に設定が有るものだけを対象とする。
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(condSearch.yosanShikkouTaishou)){
			sbMainSql.append(" AND (LENGTH(COALESCE(E.jisshi_kian_bangou, '')) > 0 AND LENGTH(COALESCE(E.shishutsu_kian_bangou, '')) = 0)");
		}
		// 現在の伝票が支出依頼の場合は実施起案番号、支出起案番号のどちらかに設定が有るものを対象とする
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(condSearch.yosanShikkouTaishou)){
			sbMainSql.append(" AND (LENGTH(COALESCE(E.jisshi_kian_bangou, '')) > 0 OR LENGTH(COALESCE(E.shishutsu_kian_bangou, '')) > 0)");
		}
		sbMainSql.append("  @replace")
				 .append(" ORDER BY")
				 .append("  C.touroku_time, D.touroku_time");

		/*
		 * 画面で指定された検索条件を使用して、SQLの置換(@replace)とパラメータの指定を行う。
		 */
		List<Object> params = new ArrayList<Object>();

		// 検索条件を取得する。
		StringBuilder sbReplace = new StringBuilder();

		// 起案番号簿
		if (null != condSearch.boBumonCd){
			sbReplace.append(" AND E.bumon_cd = ? AND E.nendo = ? AND E.ryakugou = ? AND E.kian_bangou_from = ?");
			params.add(condSearch.boBumonCd);
			params.add(condSearch.boNendo);
			params.add(condSearch.boRyakugou);
			params.add(condSearch.boKianbangouFrom);
		}
		// 起案番号
		if (null != condSearch.kianbangou){
			sbReplace.append(" AND E.kian_bangou = ?");
			params.add(condSearch.kianbangou);
		}
		// 件名
		if (!isEmpty(condSearch.kenmei)){
			sbReplace.append(" AND F.value1 LIKE ?");
			params.add("%" + condSearch.kenmei + "%");
		}
		// 伝票種別コード
		if (!isEmpty(condSearch.denpyouShubetsu)){
			sbReplace.append(" AND A.denpyou_kbn = ?");
			params.add(condSearch.denpyouShubetsu);
		}
		// 起案日
		if (null != condSearch.kianBiFrom){
			sbReplace.append(" AND C.touroku_time >= ?");
			params.add(condSearch.kianBiFrom);
		}
		if (null != condSearch.kianBiTo){
			sbReplace.append(" AND C.touroku_time < (CAST(? AS DATE) + 1)");
			params.add(condSearch.kianBiTo);
		}
		// 最終承認日
		if (null != condSearch.saishuuShouninBiFrom){
			sbReplace.append(" AND D.touroku_time >= ?");
			params.add(condSearch.saishuuShouninBiFrom);
		}
		if (null != condSearch.saishuuShouninBiTo){
			sbReplace.append(" AND D.touroku_time < (CAST(? AS DATE) + 1)");
			params.add(condSearch.saishuuShouninBiTo);
		}
		// 起票部門コード
		if (!isEmpty(condSearch.kihyouBumonCd)){
			
			List<String> kihyouBumonList = new ArrayList<String>();
			BumonUserKanriCategoryLogic bumonUserKanriCategoryLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, super.connection);
			
			// まず、自所属部門をリストに追加
			kihyouBumonList.add(condSearch.kihyouBumonCd);
			// 自所属部門を親とする子部門を取得して、リストに追加
			List<GMap> lstNestBumon = bumonUserKanriCategoryLogic.selectBumonTreeStructure(condSearch.kihyouBumonCd, true);
			for (GMap aMap : lstNestBumon){
				kihyouBumonList.add(aMap.get("bumon_cd").toString());
			}
			
			if (0 < kihyouBumonList.size()){
				StringBuilder sbKihyouBumon = new StringBuilder();
				for (String aKihyouBumon : kihyouBumonList){
					if (0 < sbKihyouBumon.length()){
						sbKihyouBumon.append(",");
					}
					sbKihyouBumon.append("'").append(aKihyouBumon).append("'");
				}
				sbReplace.append(" AND E.bumon_cd IN (").append(sbKihyouBumon.toString()).append(")");
			}
		}

		// 検索条件で置換部分を置き換える。
		String sql = sbMainSql.toString().replaceAll("@replace", sbReplace.toString());

		// 検索を実施する。
		List<GMap> lstResult = connection.load(sql, params.toArray());

		return lstResult;
	}

	/**
	 * 起案伝票データ取得<br>
	 * 呼び元へ引き渡すための起案伝票データを取得する。<br>
	 * 
	 * @param denpyouId 伝票ID
	 * @return 検索結果Map
	 */
	public GMap findKianDenpyouData(String denpyouId){

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("   A.denpyou_id")
				 .append("  ,A.denpyou_kbn")
				 .append("  ,B.version")
				 .append("  ,B.denpyou_shubetsu")
				 .append("  ,B.denpyou_shubetsu_url")
				 .append("  ,C.kian_denpyou")
				 .append("  ,C.kian_denpyou_kbn")
				 .append("  ,C.jisshi_nendo")
				 .append("  ,C.jisshi_kian_bangou")
				 .append("  ,C.shishutsu_nendo")
				 .append("  ,C.shishutsu_kian_bangou")
				 .append("  ,C.ringi_kingaku")
				 .append("  ,COALESCE(D.value1, '') AS kenmei")
				 .append("  ,COALESCE(E.version, 0) AS version")
				 .append(" FROM denpyou AS A")
				 .append(" INNER JOIN denpyou_shubetsu_ichiran AS B ON B.denpyou_kbn = A.denpyou_kbn")
				 .append(" LEFT OUTER JOIN denpyou_kian_himozuke AS C ON C.denpyou_id = A.denpyou_id")
				 .append(" LEFT OUTER JOIN (")
				 .append("  SELECT D2.denpyou_id, D2.value1")
				 .append("  FROM kani_todoke_koumoku AS D1")
				 .append("  INNER JOIN kani_todoke AS D2 ON D2.denpyou_kbn = D1.denpyou_kbn AND D2.version = D1.version AND D2.item_name = D1.item_name")
				 .append("  WHERE D1.yosan_shikkou_koumoku_id = 'kenmei'")
				 .append(" ) AS D ON D.denpyou_id = A.denpyou_id")
				 .append(" LEFT OUTER JOIN (")
				 .append("  SELECT DISTINCT denpyou_id, version")
				 .append("  FROM kani_todoke")
				 .append(" ) AS E ON E.denpyou_id = A.denpyou_id")
				 .append(" WHERE")
				 .append("  A.denpyou_id = ?");

		// 検索を実施する。
		GMap mapResult = connection.find(sbMainSql.toString(), denpyouId);

		return mapResult;
	}

	/**
	 * 取得した起案追加伝票を整形する。<br>
	 * 
	 * @param mapKian 起案追加する起案Map
	 */
	public void formatKianDenpyou(GMap mapKian){
		this.wfCategoryLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, super.connection);

		/*
		 * 実施起案番号／支出起案番号の表示切り替えを行う。
		 */
		String tmpKianbangou;
		String jisshiKianbangou = (String)mapKian.get("jisshi_kian_bangou");
		String shishutsuKianbangou = (String)mapKian.get("shishutsu_kian_bangou");
		if (super.isEmpty(shishutsuKianbangou)){
			tmpKianbangou = jisshiKianbangou;
		}else{
			tmpKianbangou = shishutsuKianbangou;
		}

		/*
		 * 選択された伝票について処理する。
		 */
		// 表示用 起案伝票
		String embedSpaceStr  = "";
		String putDenpyouKbn  = mapKian.get("denpyou_kbn").toString();
		String putKianbangou  = tmpKianbangou;
		String putKenmei  		 = mapKian.get("kenmei").toString();
		String putDenpyouId  = mapKian.get("denpyou_id").toString();
		String putDenpyouShubetsu  = mapKian.get("denpyou_shubetsu").toString();
		String putDenpyouShubetsuUrl = mapKian.get("denpyou_shubetsu_url").toString();
		String putDenpyouUrl  = this.formatKanrenDenpyouUrl(putDenpyouShubetsuUrl, putDenpyouId, putDenpyouKbn, mapKian.get("version").toString());
		// 表示用 起案伝票の添付ファイル
		String putTenpuName  = "";
		String putTenpuUrl  = "";
		// 起案追加されている伝票
		String nestDenpyouId = mapKian.get("kian_denpyou") == null ? "" : mapKian.get("kian_denpyou").toString();

		// 起案伝票に添付されたファイル
		List<GMap> tenpuFileList = this.wfCategoryLogic.selectTenpuFile(putDenpyouId);
		this.formatTenpuUrl(tenpuFileList, putDenpyouId, putDenpyouKbn);

		/*
		 * 起案伝票に追加された起案伝票を処理する。
		 */
		int nestLvl = 0;
		while (!super.isEmpty(nestDenpyouId)){
			// 追加された起案伝票から起案添付セクションに表示するデータを検索する。
			GMap mapNestDenpyou = this.findKianDenpyouData(nestDenpyouId);

			// 実施起案番号／支出起案番号の表示切り替えを行う。
			String wktmpKianbangou;
			String wkJisshiKianbangou = (String)mapNestDenpyou.get("jisshi_kian_bangou");
			String wkShishutsuKianbangou = (String)mapNestDenpyou.get("shishutsu_kian_bangou");
			if (super.isEmpty(wkShishutsuKianbangou)){
				wktmpKianbangou = wkJisshiKianbangou;
			}else{
				wktmpKianbangou = wkShishutsuKianbangou;
			}

			String wkDenpyouKbn = mapNestDenpyou.get("denpyou_kbn").toString();
			String wkKianbangou = wktmpKianbangou;
			String wkKenmei = mapNestDenpyou.get("kenmei").toString();
			String wkDenpyouId = mapNestDenpyou.get("denpyou_id").toString();
			String wkDenpyouShubetsu = mapNestDenpyou.get("denpyou_shubetsu").toString();
			String wkDenpyouShubetsuUrl = mapNestDenpyou.get("denpyou_shubetsu_url").toString();

			// 表示用 起案伝票に追加する。
			nestLvl++;
			embedSpaceStr += "," 		+ this.fetchEmbedSpace(nestLvl);
			putDenpyouKbn += "," 		+ wkDenpyouKbn;
			putDenpyouShubetsu += "<br>"	+ wkDenpyouShubetsu;
			putKianbangou += "<br>"	+ wkKianbangou;
			putKenmei += "<br>"	+ wkKenmei;
			putDenpyouId += "<br>"	+ this.fetchEmbedSpace(nestLvl) + wkDenpyouId;
			putDenpyouShubetsuUrl += ","		+ mapNestDenpyou.get("denpyou_shubetsu_url");
			putDenpyouUrl += ","		+ this.formatKanrenDenpyouUrl(wkDenpyouShubetsuUrl, wkDenpyouId, wkDenpyouKbn, mapNestDenpyou.get("version").toString());

			// 起案伝票に添付されたファイル
			List<GMap> tenpu = this.wfCategoryLogic.selectTenpuFile(wkDenpyouId);
			formatTenpuUrl(tenpu, wkDenpyouId, wkDenpyouKbn);
			tenpuFileList.addAll(tenpu);

			// さらに起案伝票に追加されている伝票を取得する。
			nestDenpyouId = mapNestDenpyou.get("kian_denpyou") == null ? "" : mapNestDenpyou.get("kian_denpyou").toString();
		}

		// 全ての添付ファイルを結合する。
		for (GMap tenpuMap : tenpuFileList) {
			putTenpuName += tenpuMap.get("file_name") + ",";
			putTenpuUrl  += tenpuMap.get("tenpu_url") + ",";
		}

		// マップに整形した情報を追加する。
		mapKian.put("embed_space",  	embedSpaceStr);
		mapKian.put("kbn", putDenpyouKbn);
		mapKian.put("trKianbangou", putKianbangou);
		mapKian.put("trKenmei", putKenmei);
		mapKian.put("shubetsu", putDenpyouShubetsu);
		mapKian.put("id", putDenpyouId);
		mapKian.put("denpyou_shubetsu_url", putDenpyouShubetsuUrl);
		mapKian.put("denpyou_url", putDenpyouUrl);
		mapKian.put("file_name", putTenpuName);
		mapKian.put("tenpu_url", putTenpuUrl);
	}

	/**
	 * 起案伝票のURLを作成する。
	 * 
	 * @param url 伝票種別URL
	 * @param id 伝票ID
	 * @param kbn 伝票区分
	 * @param version バージョン
	 * @return 起案伝票URL
	 */
	protected String formatKanrenDenpyouUrl (String url, String id, String kbn, String version) {
		String ret = url + "?denpyouId=" + id + "&denpyouKbn=" + kbn;
		if (Integer.valueOf(version) > 0) {
			ret += "&version=" + version;
		}
		return ret;
	}

	/**
	 * 添付ファイルリストにURLをセットする。<br>
	 * 
	 * @param tenpu 添付ファイルリスト
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 */
	protected void formatTenpuUrl (List<GMap> tenpu, String denpyouId, String denpyouKbn){
		for (GMap map : tenpu) {
			map.put("tenpu_url", "denpyou_file_download?denpyouId=" + denpyouId + "&denpyouKbn=" + denpyouKbn + "&downloadFileNo=" + map.get("edano"));
		}
	}

	/**
	 * 枝番による空白領域を取得する。
	 * 
	 * @param listEdano ループ回数
	 * @return 結果
	 */
	protected String fetchEmbedSpace (int listEdano) {
		String ret = "";
		for (int i = 0; i < listEdano; i++) {
			ret += "　";
		}
		return ret;
	}
}
