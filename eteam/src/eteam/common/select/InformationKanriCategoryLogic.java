package eteam.common.select;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.INFO_STATUS;

/**
 * インフォメーション登録カテゴリー内のSelect文を集約したLogic
 */
public class InformationKanriCategoryLogic extends EteamAbstractLogic {

/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/* インフォメーション(information) */
	/**
	 * インフォメーションを検索する。データがなければnull。
	 * @param infoId インフォメーションID
	 * @return 検索結果
	 */
	public GMap findInfo(String infoId){
		final String sql = "SELECT * FROM information WHERE info_id=?";
		return connection.find(sql, infoId);
	}
	
	/**
	 * インフォメーションの通知期間内のデータを取得する。
	 * @return 検索結果
	 */
	public List<GMap> loadInfoKikannaiData(){
		final String sql = "SELECT i.* FROM information i LEFT OUTER JOIN information_sort s ON s.info_id = i.info_id WHERE current_date BETWEEN i.tsuuchi_kikan_from AND i.tsuuchi_kikan_to ORDER BY s.hyouji_jun, i.tsuuchi_kikan_from desc, i.tsuuchi_kikan_to desc, i.info_id;";
		return connection.load(sql);
	}
	
	/**
	 * 指定した条件のインフォメーションデータ件数を取得する。データがなければサイズ0。
	 * @param status ステータス  9:全て 1:掲載中 2:未掲載 3:掲載終了
	 * @return 検索結果件数
	 */
	public long findInfoCount(String status){
		final StringBuffer sql = new StringBuffer("SELECT COUNT(info_id) AS datacount FROM information ");

		if (! status.isEmpty()){
			/* 
			 * 条件①：ステータス 
			 * 件数の取得だけなので、条件はステータスのみ
			 */
			// ステータス:掲載中
			if(INFO_STATUS.KEISAI_CHUU.equals(status)){
				sql.append("WHERE ");
				sql.append(" current_date BETWEEN tsuuchi_kikan_from AND tsuuchi_kikan_to");
			}
			// ステータス:未掲載
			else if(INFO_STATUS.MI_KEISAI.equals(status)){
				sql.append("WHERE ");
				sql.append(" tsuuchi_kikan_from > current_date ");
			}
			// ステータス:掲載終了
			else if(INFO_STATUS.KEISAI_SHUURYOU.equals(status)){
				sql.append("WHERE ");
				sql.append(" tsuuchi_kikan_to < current_date ");
			}
		}
		
		GMap datacount = connection.find(sql.toString());
		long count = datacount.get("datacount");
		return count;
	
	}
	/**
	 * 指定した条件のインフォメーションを検索する。データがなければサイズ0。
	 * @param sortKbn ソート区分 1:昇順 2:降順
	 * @param status  ステータス 9:全て 1:掲載中 2:未掲載 3:掲載終了
	 * @param pageNo  ページ番号
	 * @param pageMax 1ページ最大表示件数
	 * @return 検索結果
	 */
	public List<GMap> loadInfo(String sortKbn, String status, int pageNo, int pageMax){
		final StringBuffer sql = new StringBuffer("SELECT i.* FROM information i LEFT OUTER JOIN information_sort s ON s.info_id = i.info_id ");
		
		/* 条件①：ステータス */
		if (! status.isEmpty()){
			// ステータス:掲載中
			if(INFO_STATUS.KEISAI_CHUU.equals(status)){
				sql.append("WHERE ");
				sql.append(" current_date BETWEEN i.tsuuchi_kikan_from AND i.tsuuchi_kikan_to");
			}
			// ステータス:未掲載
			else if(INFO_STATUS.MI_KEISAI.equals(status)){
				sql.append("WHERE ");
				sql.append(" i.tsuuchi_kikan_from > current_date ");
			}
			// ステータス:掲載終了
			else if(INFO_STATUS.KEISAI_SHUURYOU.equals(status)){
				sql.append("WHERE ");
				sql.append(" i.tsuuchi_kikan_to < current_date ");
			}
		}
		
		/* 条件②：ソート区分 */
		// ソート区分"1":昇順
		if ("1".equals(sortKbn)){
			sql.append(" ORDER BY i.tsuuchi_kikan_from , i.tsuuchi_kikan_to, s.hyouji_jun, i.info_id");
		}
		else if("2".equals(sortKbn)){
			// 開始日、終了日の降順でソート
			sql.append(" ORDER BY i.tsuuchi_kikan_from desc, i.tsuuchi_kikan_to desc, s.hyouji_jun, i.info_id");
		}else {
			// 表示順でソート
			sql.append(" ORDER BY s.hyouji_jun, i.tsuuchi_kikan_from desc, i.tsuuchi_kikan_to desc, i.info_id");
		}
		
		/* 条件③：ページ番号（取得件数）*/
		sql.append(EteamCommon.makeSqlForTableNumCtl(pageNo, pageMax));
		
		return connection.load(sql.toString());
	}
	
/* インフォメーションID採番(info_id_saiban) */
	
	
	

}
