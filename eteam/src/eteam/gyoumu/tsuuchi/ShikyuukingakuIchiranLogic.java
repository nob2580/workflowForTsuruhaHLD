package eteam.gyoumu.tsuuchi;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 社員別支給金額一覧Logic
 */
public class ShikyuukingakuIchiranLogic extends EteamAbstractLogic {
	
	/** 支給金額情報（伝票単位） */
	@Getter @Setter
	public class ShikyuuData {
		/** 社員番号 */ String shainNo;
		/** ユーザー名 */ String userFullName;
		/** 部門名 */ String bumonFullName; //サマリの場合カンマ区切りで複数部門名
		/** 伝票ID */ String denpyouId; //サマリの場合はなし
		/** 伝票種別 */ String denpyouShubetsu; //サマリの場合はなし
		/** 状態 */ String joutai; //サマリの場合はなし
		/** 起票日 */ String tourokuTime; //通常は単品の起票日(yyyy/mm/dd)形式、サマリでサマリ内MIN!=MAXなら起票日MIN(yyyy/mm/dd)～起票日MAX(yyyy/mm/dd)形式
		/** 使用日 */ String shiyoubi; //通常は単品の使用日(yyyy/mm/dd)形式、サマリでサマリ内MIN!=MAXなら使用日MIN(yyyy/mm/dd)～使用日MAX(yyyy/mm/dd)形式
		/** 支払日 */ String shiharaibi; //通常は単品の支払日(yyyy/mm/dd)形式、サマリでサマリ内MIN!=MAXなら支払日MIN(yyyy/mm/dd)～支払日MAX(yyyy/mm/dd)形式
		/** 支払金額(現金) */ BigDecimal genkinShiharai;
		/** 支払金額(振込) */ BigDecimal furikomiShiharai;
		/** 支払金額(法人カード) */ BigDecimal houjinShiharai;
		/** 支払金額(会社手配) */ BigDecimal tehaiShiharai;
		/** 仮払金額 */ BigDecimal karibarai;
		/** 差引支給金額(現金) */ BigDecimal genkinSashihiki;
		/** 差引支給金額(振込) */ BigDecimal furikomiSashihiki;
		/** 社員別合計 */ ShainGoukeiShikyuuData shainGoukei;
	}
	
	/** 支給金額情報（社員別合計） */
	@Getter @Setter
	public class ShainGoukeiShikyuuData {
		/** 支払金額（現金） */ public BigDecimal sumGenkinShiharai = BigDecimal.valueOf(0);
		/** 支払金額（振込） */ public BigDecimal sumFurikomiShiharai = BigDecimal.valueOf(0);
		/** 支払金額（法人カード） */ public BigDecimal sumHoujinShiharai = BigDecimal.valueOf(0);
		/** 支払金額（会社手配） */ public BigDecimal sumTehaiShiharai = BigDecimal.valueOf(0);
		/** 仮払金額 */ public BigDecimal sumKaribarai = BigDecimal.valueOf(0);
		/** 差引支給金額(現金) */ public BigDecimal sumGenkinSashihiki = BigDecimal.valueOf(0);
		/** 差引支給金額(振込) */ public BigDecimal sumFurikomiSashihiki = BigDecimal.valueOf(0);
	}
	
	/** 支給金額情報（合計） */
	@Getter @Setter
	public class GoukeiShikyuuData {
		/** 支給金額合計(現金) */ public BigDecimal sumGenkinShikyuu = BigDecimal.valueOf(0);
		/** 支給金額合計(振込) */ public BigDecimal sumFurikomiShikyuu = BigDecimal.valueOf(0);
		/** 金額合計(法人カード) */ public BigDecimal sumHoujinKingaku = BigDecimal.valueOf(0);
		/** 金額合計(会社手配) */ public BigDecimal sumTehaiKingaku = BigDecimal.valueOf(0);
		/** 差引マイナス(現金) */ public BigDecimal sumGenkinSashihiki = BigDecimal.valueOf(0);
		/** 差引マイナス(振込) */ public BigDecimal sumFurikomiSashihiki = BigDecimal.valueOf(0);
	}

	/**
	 * 社員別支給金額一覧を取得する。
	 * @param user ログインユーザ
	 * @param kihyouBiFrom 起票日付From
	 * @param kihyouBiTo 起票日付To
	 * @param kensakuShainNo 起票者社員番号
	 * @param kihyouShozokuBumonCd 起票者所属部門コード
	 * @param kihyouShozokuBumonName 起票者所属部門名
	 * @param kihyouShozokuUserName 起票者名
	 * @param shiharaiBiFrom 支払日From
	 * @param shiharaiBiTo 支払日To
	 * @param shiyouBiFrom 使用日From
	 * @param shiyouBiTo 使用日To
	 * @param kihyouchuu 伝票状態：起票中
	 * @param shinseichuu 伝票状態：申請中
	 * @param syouninzumi 伝票状態：承認済
	 * @param hininzumi   伝票状態：否認済
	 * @param torisagezumi 伝票状態：取下済
	 * @param sortColumn1 ソートカラム①
	 * @param sort1       ソート順①
	 * @param sortColumn2 ソートカラム②
	 * @param sort2       ソート順②
	 * @param sortColumn3 ソートカラム③
	 * @param sort3       ソート順③
	 * @param isShainBetsuSummary 社員別サマリ
	 * @return 検索結果
	 */
	public List<ShikyuuData> loadIchiran(
			  User user
			, Date kihyouBiFrom
			, Date kihyouBiTo
			, String kensakuShainNo
			, String kihyouShozokuBumonCd
			, String kihyouShozokuBumonName
			, String kihyouShozokuUserName
			, Date shiharaiBiFrom
			, Date shiharaiBiTo
			, Date shiyouBiFrom
			, Date shiyouBiTo
			, String kihyouchuu
			, String shinseichuu
			, String syouninzumi
			, String hininzumi
			, String torisagezumi
			, String sortColumn1
			, String sort1
			, String sortColumn2
			, String sort2
			, String sortColumn3
			, String sort3
			, Boolean isShainBetsuSummary){
		TsuuchiCategoryLogic lg = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		List<GMap> records = lg.loadShikyuuKingakuIchiranKensaku(
					  user
					, kihyouBiFrom
					, kihyouBiTo
					, kensakuShainNo
					, kihyouShozokuBumonCd
					, kihyouShozokuBumonName
					, kihyouShozokuUserName
					, shiharaiBiFrom
					, shiharaiBiTo
					, shiyouBiFrom
					, shiyouBiTo
					, kihyouchuu
					, shinseichuu
					, syouninzumi
					, hininzumi
					, torisagezumi
					, sortColumn1
					, sort1
					, sortColumn2
					, sort2
					, sortColumn3
					, sort3
					, isShainBetsuSummary);
		List<ShikyuuData> ret = new ArrayList<>();
		
		//加工
		for (GMap record : records) {
			ShikyuuData d = new ShikyuuData();
			d.setShainNo ((String)record.get("shain_no"));
			d.setUserFullName ((String)record.get("user_full_name"));
			d.setBumonFullName ((String)record.get("bumon_full_name"));
			d.setGenkinShiharai ((BigDecimal)record.get("genkin_shiharai"));
			d.setFurikomiShiharai ((BigDecimal)record.get("furikomi_shiharai"));
			d.setHoujinShiharai ((BigDecimal)record.get("houjin_shiharai"));
			d.setTehaiShiharai ((BigDecimal)record.get("tehai_shiharai"));
			d.setKaribarai ((BigDecimal)record.get("karibarai_kingaku"));
			d.setGenkinSashihiki ((BigDecimal)record.get("genkin_sashihiki"));
			d.setFurikomiSashihiki ((BigDecimal)record.get("furikomi_sashihiki"));

			if (isShainBetsuSummary) {
				d.setShiharaibi ((String)record.get("shiharaibi"));
				d.setTourokuTime ((String)record.get("touroku_time"));
				d.setShiyoubi ((String)record.get("shiyoubi"));
			} else {
				d.setShiharaibi (EteamXlsFmt.formatDate(record.get("shiharaibi")));
				d.setTourokuTime (EteamXlsFmt.formatDate(record.get("touroku_time")));
				d.setShiyoubi (EteamXlsFmt.formatDate(record.get("shiyoubi")));
				d.setDenpyouId ((String)record.get("denpyou_id"));
				d.setDenpyouShubetsu((String)record.get("denpyou_shubetsu"));
				d.setJoutai (record.get("name") + "(" + record.get("cur_cnt") + "/" + record.get("all_cnt") + ")");
			}
			ret.add(d);
		}

		//社員単位合計を付ける
		if (! isShainBetsuSummary) {
			ShainGoukeiShikyuuData shainGoukei = new ShainGoukeiShikyuuData();
			for (int i = 0; i < ret.size(); i++) {
				//社員別合計にプラス
				ShikyuuData d = ret.get(i);
					shainGoukei.sumGenkinShiharai = BigDecimal.valueOf(shainGoukei.sumGenkinShiharai.doubleValue() + d.genkinShiharai.doubleValue());
					shainGoukei.sumFurikomiShiharai = BigDecimal.valueOf(shainGoukei.sumFurikomiShiharai.doubleValue() + d.furikomiShiharai.doubleValue());
				if(d.houjinShiharai != null){
					shainGoukei.sumHoujinShiharai = BigDecimal.valueOf(shainGoukei.sumHoujinShiharai.doubleValue() + d.houjinShiharai.doubleValue());
				}
				if(d.tehaiShiharai != null){
					shainGoukei.sumTehaiShiharai = BigDecimal.valueOf(shainGoukei.sumTehaiShiharai.doubleValue() + d.tehaiShiharai.doubleValue());
				}
				if(d.karibarai != null){
					shainGoukei.sumKaribarai = BigDecimal.valueOf(shainGoukei.sumKaribarai.doubleValue() + d.karibarai.doubleValue());
				}
					shainGoukei.sumGenkinSashihiki = BigDecimal.valueOf(shainGoukei.sumGenkinSashihiki.doubleValue() + d.genkinSashihiki.doubleValue());
					shainGoukei.sumFurikomiSashihiki= BigDecimal.valueOf(shainGoukei.sumFurikomiSashihiki.doubleValue() + d.furikomiSashihiki.doubleValue());
				
				//最終行である　または次の行でユーザーが変わる場合に、社員別合計を〆る
				boolean goukeiFlg = false;
				if (i == ret.size()-1) {
					goukeiFlg = true;
				} else if (! d.getShainNo().equals(ret.get(i+1).getShainNo())) {
					goukeiFlg = true;
				}
				if (goukeiFlg) {
					d.setShainGoukei(shainGoukei);
					shainGoukei = new ShainGoukeiShikyuuData();
				}
			}
		}
		return ret;
	}

	/**
	 * 金額の合計処理
	 * @param list 明細リスト（サマリ）
	 * @return 合計
	 */
	public GoukeiShikyuuData calcAllKingaku(List<ShikyuuData> list) {
		GoukeiShikyuuData ret = new GoukeiShikyuuData() ;
		for (ShikyuuData d : list) {
			if (d.genkinSashihiki.doubleValue() > 0) {
				ret.sumGenkinShikyuu = BigDecimal.valueOf(ret.sumGenkinShikyuu.doubleValue() + d.genkinSashihiki.doubleValue());
			} else {
				ret.sumGenkinSashihiki = BigDecimal.valueOf(ret.sumGenkinSashihiki.doubleValue() + d.genkinSashihiki.doubleValue());
			}
			if (d.furikomiSashihiki.doubleValue() > 0) {
				ret.sumFurikomiShikyuu = BigDecimal.valueOf(ret.sumFurikomiShikyuu.doubleValue() + d.furikomiSashihiki.doubleValue());
			} else {
				ret.sumFurikomiSashihiki= BigDecimal.valueOf(ret.sumFurikomiSashihiki.doubleValue() + d.furikomiSashihiki.doubleValue());
			}
			if (d.houjinShiharai != null) {
				ret.sumHoujinKingaku = BigDecimal.valueOf(ret.sumHoujinKingaku.doubleValue() + d.houjinShiharai.doubleValue());
			}
			if (d.tehaiShiharai != null) {
				ret.sumTehaiKingaku = BigDecimal.valueOf(ret.sumTehaiKingaku.doubleValue() + d.tehaiShiharai.doubleValue());
			}
		}
		return ret;
	}
}
