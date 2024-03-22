package eteam.gyoumu.yosanshikkoukanri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案番号簿一覧Logic
 */
@Getter @Setter @ToString
public class KianbangouBoIchiranLogic extends EteamAbstractLogic {
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianbangouBoIchiranLogic.class);

	/**
	 * 起案番号簿一覧検索条件クラス
	 */
	public static class SearchCondition {
		/** ユーザー情報 */
		public User loginUser;
		/** 所属部門コード */
		public String bumonCd;
		/** 所属部門名 */
		public String bumonNm;
		/** 年度*/
		public String nendo;
		/** 区分内容 */
		public String kbnNaiyou;
		/** 略号 */
		public String ryakugou;
		/** 採番時表示コード */
		public String[] saibanjiHyouji;
		/** 検索時表示コード */
		public String kensakuHyouji;
		/** 起票部門コード */
		public String kihyouBumonCd;
		/** 呼出元判別区分(ソート順設定用) */
		public int yobidashiHanbetsu;

		/**
		 * クラス変数デバッグ文字列返却
		 * 
		 * @return デバッグ文字列
		 */
		public String toDebugString(){
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append("bumonCd=").append(bumonCd);
			sb.append(" bumonNm=").append(bumonNm);
			sb.append(" nendo=").append(nendo);
			sb.append(" kbnNaiyou=").append(kbnNaiyou);
			sb.append(" ryakugou=").append(ryakugou);
			sb.append(" saibanjiHyouji=").append(Arrays.toString(saibanjiHyouji));
			sb.append(" kensakuHyouji=").append(kensakuHyouji);
			sb.append(" yobidashiHanbetsu=").append(Integer.toString(yobidashiHanbetsu));
			sb.append("]");
			return sb.toString();
		}
		
	};

	/**
	 * 起案番号簿検索<br>
	 * 起案番号簿一覧画面で指定された検索条件を使用して検索を実施する。<br>
	 * 指定された検索条件は {@link SearchCondition} に格納されたものを利用。

	 * @param condSearch 起案番号簿一覧検索条件
	 * @return 検索結果
	 */
	public List<GMap> kensaku(SearchCondition condSearch){

		/*
		 * ログインユーザが所属する部門および配下部門までを表示するための部門リストを取得する。
		 * ただし、業務ロールでログインしている場合は部門リストを利用しない
		 */
		StringBuilder sbBumon = new StringBuilder();
		if (isEmpty(condSearch.loginUser.getGyoumuRoleId())){
			// 業務ロールでログインしていない
			// 取得した部門リストを絞り込みSQLのパラメータに編集する。
			for (String aBumon : this.getShozokuBumonList(condSearch.loginUser)){
				if (0 < sbBumon.length()){
					sbBumon.append(",");
				}
				sbBumon.append("'").append(aBumon).append("'");
			}
		}

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("   A.bumon_cd AS bumonCd")
				 .append("  ,CASE WHEN B.bumon_name IS NULL THEN '(削除)' ELSE B.bumon_name END AS bumonNm")
				 .append("  ,A.nendo AS nendo")
				 .append("  ,A.ryakugou AS ryakugou")
				 .append("  ,A.kian_bangou_from AS kianbangouFrom")
				 .append("  ,A.kian_bangou_to AS kianbangouTo")
				 .append("  ,C.kian_bangou_last AS kianbangouLast")
				 .append("  ,A.kbn_naiyou AS kbnNaiyou")
				 .append("  ,A.kianbangou_bo_sentaku_hyouji_flg AS kianbangou_bo_sentaku_hyouji_flg")
				 .append("  ,D1.name AS saibanjiHyoujiNm")
				 .append("  ,A.denpyou_kensaku_hyouji_flg AS denpyou_kensaku_hyouji_flg")
				 .append("  ,D2.name AS kensakuHyoujiNm")
				 .append(" FROM kian_bangou_bo AS A")
				 .append(" LEFT OUTER JOIN shozoku_bumon AS B")
				 .append("  ON B.bumon_cd = A.bumon_cd")
				 .append("  AND ( A.yuukou_kigen_to BETWEEN B.yuukou_kigen_from AND B.yuukou_kigen_to) ");
		// ログインユーザの所属部門縛りがある場合、自部門と配下部門だけを参照する条件を付加する（業務ロールログインが除外）
		if (0 < sbBumon.length()){
			sbMainSql.append(" AND B.bumon_cd IN (").append(sbBumon.toString()).append(")");
		}
		sbMainSql.append(" LEFT OUTER JOIN kian_bangou_saiban AS C ON (")
				 .append("       C.bumon_cd = A.bumon_cd")
				 .append("   AND C.nendo = A.nendo")
				 .append("   AND C.ryakugou = A.ryakugou")
				 .append("   AND C.kian_bangou_from = A.kian_bangou_from")
				 .append(" )")
				 .append(" LEFT OUTER JOIN (SELECT naibu_cd, name FROM naibu_cd_setting WHERE naibu_cd_name = 'kian_bangou_bo_sentaku_hyouji') AS D1")
				 .append("  ON D1.naibu_cd = A.kianbangou_bo_sentaku_hyouji_flg")
				 .append(" LEFT OUTER JOIN (SELECT naibu_cd, name FROM naibu_cd_setting WHERE naibu_cd_name = 'denpyou_kensaku_hyouji') AS D2")
				 .append("  ON D2.naibu_cd = A.denpyou_kensaku_hyouji_flg")
				 .append(" WHERE")
				 .append("  CURRENT_DATE BETWEEN A.yuukou_kigen_from AND A.yuukou_kigen_to")
				 .append("  AND A.yuukou_kigen_to BETWEEN B.yuukou_kigen_from AND B.yuukou_kigen_to")
				 .append("  @replace")
				 .append("  @order");

		/*
		 * 画面で指定された検索条件を使用して、SQLの置換(@replace)とパラメータの指定を行う。
		 */
		List<Object> params = new ArrayList<Object>();

		// 検索条件を取得する。
		StringBuilder sbReplace = new StringBuilder();
		// 所属部門コード
		if (!isEmpty(condSearch.bumonCd)){
			sbReplace.append(" AND A.bumon_cd = ?");
			params.add(condSearch.bumonCd);
		}
		//※部門コードのみによる検索なのでcondSearch.bumonNmは実質使われていない
		// 所属部門名称
		if (!isEmpty(condSearch.bumonNm)){
			sbReplace.append(" AND B.bumon_name LIKE ");
			sbReplace.append("'%" + condSearch.bumonNm + "%'");
		}
		// 年度
		if (!isEmpty(condSearch.nendo)){
			sbReplace.append(" AND A.nendo = ?");
			params.add(condSearch.nendo);
		}
		// 略号
		if (!isEmpty(condSearch.ryakugou)){
			sbReplace.append(" AND A.ryakugou LIKE ?");
			params.add("%" + condSearch.ryakugou + "%");
		}
		// 区分内容
		if (!isEmpty(condSearch.kbnNaiyou)){
			sbReplace.append(" AND A.kbn_naiyou LIKE ?");
			params.add("%" + condSearch.kbnNaiyou + "%");
		}
		// 採番時表示コード
		if (null != condSearch.saibanjiHyouji && 0 < condSearch.saibanjiHyouji.length){
			StringBuilder sbInItem = new StringBuilder();
			for (int i = 0; i < condSearch.saibanjiHyouji.length; i++){
				if (!isEmpty(condSearch.saibanjiHyouji[i])){
					sbInItem.append("'").append(condSearch.saibanjiHyouji[i]).append("',");
				}
			}
			if (0 < sbInItem.length()){
				sbReplace.append(" AND A.kianbangou_bo_sentaku_hyouji_flg IN ");
				sbReplace.append("(").append(sbInItem.toString().substring(0, sbInItem.length() - 1)).append(")");
			}
		}
		// 検索時表示コード
		if (!isEmpty(condSearch.kensakuHyouji)){
			sbReplace.append(" AND A.denpyou_kensaku_hyouji_flg = ?");
			params.add(condSearch.kensakuHyouji);
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
				sbReplace.append(" AND B.bumon_cd IN (").append(sbKihyouBumon.toString()).append(")");
			}
		}
		
		//呼出元によるソート順設定
		String strOrder = "";
		if(condSearch.yobidashiHanbetsu == 1) {
			//起案番号簿採番ダイアログ・起案追加ダイアログ
			strOrder = " ORDER BY A.nendo ASC, A.ryakugou ASC, A.kbn_naiyou ASC ";
		}else if(condSearch.yobidashiHanbetsu == 2) {
			//起案番号簿一覧画面
			strOrder = " ORDER BY A.hyouji_jun, A.bumon_cd, A.nendo, A.ryakugou, A.kian_bangou_from ";
		}

		// 検索条件で置換部分を置き換える。
		String sql = sbMainSql.toString().replaceAll("@replace", sbReplace.toString());
		sql = sql.replaceAll("@order", strOrder);

		// 検索を実施する。
		List<GMap> lstResult = connection.load(sql, params.toArray());

		// 取得した検索結果を加工する。
		for (GMap aMap : lstResult){
			// 最終起案番号はレコード作成時 null のため、ブランクに置き換える。
			Object kianbangouLast = aMap.get("kianbangouLast");
			if (null == kianbangouLast){
				aMap.put("kianbangouLast", "");
			}
		}

		return lstResult;
	}
	
	
	/**
	 * ログインユーザーに紐づく所属部門をその配下部門を含めて取得する。
	 * @param user ユーザー情報
	 * @return 部門リスト
	 */
	protected List<String> getShozokuBumonList(User user){
		// 業務ロールでログインしていない
		List<String> lstBumon = new ArrayList<String>();

		// ログインユーザに紐づく所属部門の件数分、配下部門のリストを取得する。
		BumonUserKanriCategoryLogic bumonUserKanriCategoryLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, super.connection);
		for (String aBumonCd : user.getBumonCd()){
			// まず、自所属部門をリストに追加
			lstBumon.add(aBumonCd);
			// 自所属部門を親とする子部門を取得して、リストに追加
			List<GMap> lstNestBumon = bumonUserKanriCategoryLogic.selectBumonTreeStructure(aBumonCd, true);
			for (GMap aMap : lstNestBumon){
				lstBumon.add(aMap.get("bumon_cd").toString());
			}
		}
		return lstBumon;
	}
	
	/**
	 * 起案番号簿取得<br>
	 * 指定されたキーの起案番号簿レコードを取得する。
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianBangouFrom 開始起案番号
	 * @return 検索結果
	 */
	public GMap findKianbangouBo(String bumonCd, String nendo, String ryakugou, int kianBangouFrom) {

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT * FROM kian_bangou_bo")
				 .append(" WHERE")
				 .append("  bumon_cd = ?")
				 .append("  AND nendo = ?")
				 .append("  AND ryakugou = ?")
				 .append("  AND kian_bangou_from = ?");

		return connection.find(sbMainSql.toString(), bumonCd, nendo, ryakugou, kianBangouFrom);
	}

	/**
	 * 起案番号簿複製<br>
	 * 指定されたキーの起案番号簿レコードから複製先年度を指定したレコードを新規に登録する。
	 * 
	 * @param bumonCd 所属部門コード
	 * @param nendo 年度
	 * @param ryakugou 略号
	 * @param kianBangouFrom 開始起案番号
	 * @param copyNendo 複製先年度
	 * @return 登録件数
	 */
	public int copyKianbangouBo(String bumonCd, String nendo, String ryakugou, int kianBangouFrom, String copyNendo) {

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("INSERT INTO kian_bangou_bo")
				 .append(" SELECT")
				 .append("   bumon_cd")
				 .append("  ,?")
				 .append("  ,ryakugou")
				 .append("  ,kian_bangou_from")
				 .append("  ,kian_bangou_to")
				 .append("  ,kbn_naiyou")
				 .append("  ,'1'")
				 .append("  ,'1'")
				 .append("  ,yuukou_kigen_from")
				 .append("  ,yuukou_kigen_to")
				 .append("  ,hyouji_jun")
				 .append(" FROM kian_bangou_bo")
				 .append(" WHERE")
				 .append("  bumon_cd = ?")
				 .append("  AND nendo = ?")
				 .append("  AND ryakugou = ?")
				 .append("  AND kian_bangou_from = ?");

		// レコードを追加する。
		int cntResult = connection.update(sbMainSql.toString(), copyNendo, bumonCd, nendo, ryakugou, kianBangouFrom);
		return cntResult;
	}

	/**
	 * 起案番号採番レコードを取得
	 * @param loginUser ログインユーザー
	 * @return 取得結果
	 */
	public List<GMap> loadKianBangouSaiban(User loginUser){

		/*
		 * ログインユーザが所属する部門および配下部門までを表示するための部門リストを取得する。
		 * ただし、業務ロールでログインしている場合は部門リストを利用しない
		 */
		StringBuilder sbBumon = new StringBuilder();
		if (isEmpty(loginUser.getGyoumuRoleId())){
			// 業務ロールでログインしていない
			// 取得した部門リストを絞り込みSQLのパラメータに編集する。
			for (String aBumon : this.getShozokuBumonList(loginUser)){
				if (0 < sbBumon.length()){
					sbBumon.append(",");
				}
				sbBumon.append("'").append(aBumon).append("'");
			}
		}

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT");
		sbMainSql.append(" s.nendo AS nendo,");
		sbMainSql.append(" s.ryakugou AS ryakugou,");
		sbMainSql.append(" b.kbn_naiyou AS kbnNaiyou,");
		sbMainSql.append(" MIN(s.kian_bangou_from) AS kianbangouFrom,");
		sbMainSql.append(" MAX(s.kian_bangou_last) AS kianbangouLast");
		sbMainSql.append(" FROM kian_bangou_saiban s");
		sbMainSql.append(" LEFT OUTER JOIN kian_bangou_bo b");
		sbMainSql.append("   ON s.bumon_cd = b.bumon_cd");
		sbMainSql.append("  AND s.nendo = b.nendo");
		sbMainSql.append("  AND s.ryakugou = b.ryakugou");
		sbMainSql.append("  AND s.kian_bangou_from = b.kian_bangou_from");
		sbMainSql.append(" INNER JOIN shozoku_bumon AS sb");
		sbMainSql.append("   ON sb.bumon_cd = b.bumon_cd");
		// ログインユーザの所属部門縛りがある場合、自部門と配下部門だけを参照する条件を付加する（業務ロールログインが除外）
		if (0 < sbBumon.length()){
			sbMainSql.append(" AND sb.bumon_cd IN (").append(sbBumon.toString()).append(")");
		}
		sbMainSql.append(" WHERE b.denpyou_kensaku_hyouji_flg = '1'");
		sbMainSql.append(" GROUP BY s.nendo, s.ryakugou, b.kbn_naiyou ");
		sbMainSql.append(" ORDER BY s.nendo ASC, s.ryakugou ASC, b.kbn_naiyou ASC");
		return connection.load(sbMainSql.toString());
	}
}
