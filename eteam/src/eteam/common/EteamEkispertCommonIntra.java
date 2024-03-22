package eteam.common;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import eteam.base.EteamLogger;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_KIKAN_KBN;

/**
 * 駅すぱあとイントラ版共通
 */
public class EteamEkispertCommonIntra {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamEkispertCommonIntra.class);

	/**
	 * 区分によって駅すぱあとから取得する
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @param useCode 使用期間コード
	 * @return 金額
	 */
	public String selectKind(HttpServletRequest request, String useCode) {
		String ret = "";

		if (SHIYOU_KIKAN_KBN.KIKAN1.equals(useCode)) {
			// １ヶ月定期代を返却
			ret = request.getParameter("val_teiki1");
		} else if (SHIYOU_KIKAN_KBN.KIKAN3.equals(useCode)) {
			// ３ヶ月定期代を返却
			ret = request.getParameter("val_teiki3");
		} else if (SHIYOU_KIKAN_KBN.KIKAN6.equals(useCode)) {
			// ６ヶ月定期代を返却
			ret = request.getParameter("val_teiki6");
		} else {

			// 運賃
			String fare = request.getParameter("val_fare");
			// 料金
			String charge = request.getParameter("val_surcharge_fare");

			int iFare = 0;
			int iCharge = 0;

			// 運賃も料金もnullの場合、ブランクを返却
			if (fare == null && charge == null) {
				return ret;
			}

			if (fare != null) {
				iFare = Integer.parseInt(fare);
			}

			if (charge != null) {
				iCharge = Integer.parseInt(charge);
			}

			// 運賃 + 料金を返却
			ret = String.valueOf(iFare + iCharge);

		}
		log.info("■金額:" + ret + "■金額取得区分" + useCode);

		return ret;
	}

	/**
	 * 駅すぱあとにて選択した経路を返却する
	 * 料金が発生した場合は料金発生路線に料金を挿入する
	 *
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @param useCode 使用期間コード
	 * @return 経路文字列
	 */
	public String selectRoute(HttpServletRequest request, String useCode) {
		String ret = "";

		ret = request.getParameter("val_route");

		// nullの場合は空文字を返却
		if (ret == null) {
			return "";
		}

		// 料金区間の数を取得する

		String chargeCnt = request.getParameter("val_csect_cnt");

		int roopCnt = Integer.parseInt(chargeCnt);

		// 料金区間の数分ループ
		if ( (!SHIYOU_KIKAN_KBN.KIKAN1.equals(useCode)) && (!SHIYOU_KIKAN_KBN.KIKAN3.equals(useCode)) && (!SHIYOU_KIKAN_KBN.KIKAN6.equals(useCode))) {
			for (int i = 0; i < roopCnt; i++) {

				// 料金発生路線名
				String rosenNo = request.getParameter("val_csect_from_" + (i + 1));
				String rosenName = request.getParameter("val_line_name_" + rosenNo);

				// 選択した料金名を取得
				String chargeName = request.getParameter("val_csect_name_" + (i + 1));

				// 選択した料金を取得
				String charge = request.getParameter("val_csect_fare_" + (i + 1));
				BigDecimal bdCharge = new BigDecimal(charge);
				String insStr;
				//乗車券情報は経路表示方法が0(詳細表示)のときのみ出力
				if ("0".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_KEIRO_KANIHYOUJI))){
					insStr = rosenName + "(" + chargeName + ":" + formatMoney(bdCharge) + "円)";
				}else {
					insStr = rosenName;
				}

				ret = ret.replaceFirst(rosenName, insStr);

			}
		}

		// ICカード乗車券表示処理
		// ICカード区分が料金を算出で、定期券検索ではない場合
		if ("ic".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_IC_KBN)) &&
				(!SHIYOU_KIKAN_KBN.KIKAN1.equals(useCode)) && (!SHIYOU_KIKAN_KBN.KIKAN3.equals(useCode)) &&
				(!SHIYOU_KIKAN_KBN.KIKAN6.equals(useCode))) {

			// 路線数
			int cnt = Integer.parseInt(request.getParameter("val_line_cnt"));

			String[] strTicket = new String[cnt];
			for (int i = 0; i < cnt; i++) {

				/*
				 * イントラ版は路線名が異なっても直通の場合、運賃を統合しているため、路線ごとにICカードか普通乗車券か
				 * 算出する必要がある。
				 * 以下は、路線数が8で、ICカード乗車券を使用する路線数が5、そのうち2つ直通の路線があるケース
				 *
				 * val_fsect_from_1[1] val_fsect_to_1[2] val_fsect_ticketsysname_1[ICカード乗車券]
				 * val_fsect_from_2[3] val_fsect_to_2[3] val_fsect_ticketsysname_2[ICカード乗車券]
				 * val_fsect_from_3[4] val_fsect_to_3[5] val_fsect_ticketsysname_3[ICカード乗車券]
				 * val_fsect_from_4[7] val_fsect_to_4[7]
				 * val_fsect_from_5[8] val_fsect_to_5[8]
				 *
				 * 以下のロジックで路線ごとに乗車券の名称を持てるように路線数分の配列を生成し、
				 * 各要素に乗車券名称を設定する
				 */
				// 選択乗車券
				String ticketName = request.getParameter("val_fsect_ticketsysname_" + (i + 1));

				// 乗車券適用範囲From
				String from = request.getParameter("val_fsect_from_" + (i + 1));

				// 乗車券適用範囲TO
				String to = request.getParameter("val_fsect_to_" + (i + 1));

				// to - from が1以上の場合、差の分を配列に設定
				if (from != null && to != null) {
					int gapCnt = Integer.parseInt(to) - Integer.parseInt(from);

					if (gapCnt > 0) {
						for (int j = 0; j < gapCnt + 1; j++) {
							strTicket[Integer.parseInt(from) - 1 + j] = ticketName;
						}
					} else {
						strTicket[Integer.parseInt(from) - 1] = ticketName;
					}
				}
			}

			for (int i = 0; i < cnt; i++) {

				String rosenName = request.getParameter("val_line_name_" + (i + 1));

				String insStr = "";
				//乗車券情報は経路表示方法が0(詳細表示)のときのみ出力
				if (("0".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.EKISPERT_KEIRO_KANIHYOUJI))) && strTicket[i] != null) {
					insStr = rosenName + "[" + strTicket[i] + "]";
				} else {
					insStr = rosenName;
				}

				ret = ret.replaceFirst(rosenName, insStr);
			}

		}

		//繋ぎ文字をWeb版と同じものに置き換え
		ret = ret.replaceAll("－", "＝");

		return ret;
	}

	/**
	 * 駅すぱあとにて選択した駅名を返却する
	 *
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @return 駅名文字列
	 */
	public String selectStationName(HttpServletRequest request) {
		String ret = "";

		ret = request.getParameter("val_station_name");

		return ret;
	}

	/**
	 * イントラ版駅すぱあとにて距離文字列を返却
	 *
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @return  距離文字列
	 */
	public String selectDistance(HttpServletRequest request) {
		String ret = "";
		ret = request.getParameter("val_dist");
		// nullの場合は空文字を返却
		if (ret == null) {
			return "";
		}
		//返却される値は100m単位だが、全てkm単位で表示
		BigDecimal dist = new BigDecimal(ret);
		dist = dist.divide(new BigDecimal("10"));
		DecimalFormat fmt = new DecimalFormat("0.0");
		ret = fmt.format(dist) + "km";
		return ret;
	}

	/**
	 * 割引名称を取得する
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @return 割引名称
	 */
	protected String selectDiscountName(HttpServletRequest request) {
		String ret = "";
		// 駅があいまいな状態で経路検索→戻るを押したとき、val_csect_cntがnullになる
		// nullの場合は空文字を返却
		if (request.getParameter("val_csect_cnt") == null) {
			return "";
		}
		int count = Integer.parseInt(request.getParameter("val_csect_cnt"));
		for(int i = 1 ; i < count+1 ; i++) {
			String temp = request.getParameter("val_csect_discountname_" + String.valueOf(i));
			if(temp != null) {
				if(ret.length() == 0) {
					ret = temp;
				}else {
					if(ret.indexOf(temp) < 0) {
						ret = ret+"　"+ temp;
					}
				}
			}
		}

		//ret = request.getParameter("val_fsect_discountname_");

		return ret;
	}

	/**
	 * イントラ版駅すぱあとにて定期区間控除がされたかを確認
	 *
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @return  0：成功
	 * 1：予期せぬエラー
	 * 2：定期経路が不適切
	 * 3：定期区間を含む経路が存在しない
	 * 4：定期を利用しても運賃が高くなってしまう、または変わらない
	 *99：val_teiki_errcode が 0( 正常 ) 以外
	 */
	public String getAssignStatus(HttpServletRequest request) {
		String ret = "";
		ret = request.getParameter("val_tassign_status");
		// nullの場合は空文字を返却
		if (ret == null) {
			return "";
		}

		return ret;
	}

	/**
	 * イントラ版駅すぱあとにて定期検索用の方向性を持った経路文字列を取得
	 *
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @return  定期情報用文字列
	 */
	public String getValRestoreRoute(HttpServletRequest request) {
		String ret = "";
		ret = request.getParameter("val_restoreroute");
		// nullの場合は空文字を返却
		if (ret == null) {
			return "";
		}

		return ret;
	}

	/**
	 * イントラ版駅すぱあとにて指定区間が定期情報として利用できるかを取得
	 *
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @return  0:使用不可 1:使用可能
	 */
	public String getTeikiAvailable(HttpServletRequest request) {
		String ret = "";
		ret = request.getParameter("val_teiki_available");
		// nullの場合は空文字を返却
		if (ret == null) {
			return "";
		}

		return ret;
	}

	/**
	 * イントラ版駅すぱあとにて乗車券区間の消費税率引き上げに伴う改定に対応していない？を取得
	 * @param request 駅すぱあとから戻されたリクエストパラメータ
	 * @param teiki 定期検索である
	 * @return 0:対応済　1：改定前の金額、2：見込の金額
	 */
	public int getTaxNonSupported(HttpServletRequest request, boolean teiki) {
		String teikiKeyWord = "tsect_money_state";//出力パラメータval_routenoは選択したルートNoが入ってくるので、デバッグモードでうまく使え
		String unchiKeyWord = "money_state";
		if(teiki) {
			Enumeration<String> enu = request.getParameterNames();
			while(enu.hasMoreElements()) {
				String key = enu.nextElement();
				if(key.indexOf(teikiKeyWord) != -1 && request.getParameter(key).equals("1")) {
					return 1;
				}
			}
			enu = request.getParameterNames();
			while(enu.hasMoreElements()) {
				String key = enu.nextElement();
				if(key.indexOf(teikiKeyWord) != -1 && request.getParameter(key).equals("2")) {
					return 2;
				}
			}
		}else {
			Enumeration<String> enu = request.getParameterNames();
			while(enu.hasMoreElements()) {
				String key = enu.nextElement();
				if(key.indexOf(teikiKeyWord) == -1 && key.indexOf(unchiKeyWord) != -1 && request.getParameter(key).equals("1")) {
					return 1;
				}
			}
			enu = request.getParameterNames();
			while(enu.hasMoreElements()) {
				String key = enu.nextElement();
				if(key.indexOf(teikiKeyWord) == -1 && key.indexOf(unchiKeyWord) != -1 && request.getParameter(key).equals("2")) {
					return 2;
				}
			}
		}
		return 0;
	}



	/**
	 * BigDecimal型を文字列(カンマ区切り数値)に変換。
	 * @param d 変換前
	 * @return 変換後
	 */
	protected String formatMoney(Object d){
		if (null == d) {
			return "";
		} else if (d instanceof BigDecimal) {
			return new DecimalFormat("#,###").format(d);
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.d:" + d);
		}
	}
}
