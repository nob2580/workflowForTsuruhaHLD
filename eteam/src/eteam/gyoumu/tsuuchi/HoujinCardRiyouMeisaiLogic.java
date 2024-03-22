package eteam.gyoumu.tsuuchi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.GMap;

/**
 * 法人カード利用明細Logic
 */
public class HoujinCardRiyouMeisaiLogic extends EteamAbstractLogic {
	
	/** 法人カード利用（合計） */
	public BigDecimal sumHoujinCardRiyou = BigDecimal.valueOf(0);
	/** 法人カード利用（総合計） */
	public BigDecimal sumAllHoujinCardRiyou = BigDecimal.valueOf(0);
	
	/**
	 * 社員別データ出力情報作成
	 * @param kensakuList 一覧情報
	 * @param pdfFlg  			帳票出力ならtrue/CSV出力ならfalse
	 * @return kensakuListMap 編集後一覧情報
	 */
	public List<GMap> makeIchiranData(List<GMap> kensakuList, boolean pdfFlg) {
		String tmpShainNo = "";
		String tmpKihyouCardNum = "";
		String tmpKihyouUsrNm = "";
		
		List<GMap> ichiranList = new ArrayList<GMap>();
		
		for (int i = 0; i < kensakuList.size(); i++) {
			//1行ずつ書き込み
			GMap mapList = kensakuList.get(i);
			
			// 社員番号が前段と一致していたら同一ユーザーとみなす
			if(!tmpShainNo.equals(mapList.get("shain_no"))) {
				if(!"".equals(tmpShainNo)) {
					// 社員別合計行の挿入
					ichiranList.add(makeGoukeiRow(sumHoujinCardRiyou));
					// 金額(社員別)のクリア
					sumHoujinCardRiyou = BigDecimal.valueOf(0);
				}
			}
			
			/* データ編集 */
			GMap map = makeIchiranData(mapList, tmpShainNo, tmpKihyouUsrNm, tmpKihyouCardNum, pdfFlg);
			ichiranList.add(map);
			
			BigDecimal kingaku = (BigDecimal)map.get("riyou_kingaku");
			// 社員別合計金額集計
			sumHoujinCardRiyou = BigDecimal.valueOf(sumHoujinCardRiyou.doubleValue() + kingaku.doubleValue());
			// 総合計金額集計
			sumAllHoujinCardRiyou = BigDecimal.valueOf(sumAllHoujinCardRiyou.doubleValue() + kingaku.doubleValue());
			
			tmpShainNo = (String) mapList.get("shain_no"); // 社員番号を保存
			tmpKihyouCardNum = (String) mapList.get("houjin_card_shikibetsuyou_num"); // ｶｰﾄﾞ番号を保存
			tmpKihyouUsrNm = (String) mapList.get("user_full_name"); // 起票者名を保存
		}
		
		// 社員別合計行の挿入
		ichiranList.add(makeGoukeiRow(sumHoujinCardRiyou));
		
		return ichiranList;
	}
	
	/**
	 * 社員別合計行の編集
	 * @param kingakuData 金額情報
	 * @return map 社員別合計行のデータ
	 */
	protected GMap makeGoukeiRow(BigDecimal kingakuData) {
		
		GMap map = new GMap();
		map.put("naiyou_bikou_tekiyou", "合計");
		map.put("riyou_kingaku", kingakuData);
		
		return map;
	}
	
	/**
	 * 社員別データ出力情報作成
	 * @param kensakuListMap 一覧情報
	 * @param tmpShainNo ひとつ前の社員番号
	 * @param tmpKihyouUsrNm ひとつ前のユーザー名
	 * @param tmpKihyouCardNum ひとつ前のカード番号
	 * @param pdfFlg  			帳票出力ならtrue/CSV出力ならfalse
	 * @return kensakuListMap 編集後一覧情報
	 */
	protected GMap makeIchiranData(GMap kensakuListMap, String tmpShainNo, String tmpKihyouUsrNm, String tmpKihyouCardNum, boolean pdfFlg) {
		
		GMap map = new GMap();
		map.putAll(kensakuListMap);
		
		if(pdfFlg) { // 社員別サマリなしで、PDF帳票の場合、社員番号・起票者・起票部門は上段と同じ場合は表示しない
			// 社員番号が前段と一致していたら同一ユーザーとみなす
			if(tmpShainNo.equals(map.get("shain_no"))) {
				map.put("shain_no", "");
				/* 上段と同じ起票者だった場合 */
				if(tmpKihyouUsrNm.equals(map.get("user_full_name"))) {
					map.put("user_full_name", "");
				}
				/* 上段と同じ起票者カード番号だった場合*/
				if(tmpKihyouCardNum.equals(map.get("houjin_card_shikibetsuyou_num"))) {
					map.put("houjin_card_shikibetsuyou_num", "");
				}
			}
		}
		
		return map;
	}
}
