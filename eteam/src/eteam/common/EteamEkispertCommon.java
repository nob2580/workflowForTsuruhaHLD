package eteam.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 駅すぱあと共通
 */
@Getter @Setter @ToString
public class EteamEkispertCommon extends WorkflowEventControl {

	/** 検索モード:運賃 */
	public static final String SEARCH_MODE_UNCHIN = "1";
	/** 検索モード:定期 */
	public static final String SEARCH_MODE_TEIKI = "2";
	/** 都道府県コード取得キー */
	public static final String EKISPERT_SORT = "hyouji_jun";

	//20220224 追加
	/** EX予約/スマートEX選択リストキー */
	public static final String EKISPERT_JRYOYAKU = "jr_yoyaku";

	/** INパラメータ:イントラ版起動URL */
	protected String intraAction;
	/** INパラメータ：戻り先URL */
	protected String val_cgi_url;
	/** 検索モード(1:運賃, 2:定期) */
	protected String val_oneway;
	/** 検索区分 */
	protected String searchKbn;
	/** INパラメータ：出発地(イントラ版) */
	protected String val_from;
	/** INパラメータ：到着地(イントラ版) */
	protected String val_to;
	/** 入力データ：出発地(画面保持用) */
	protected String from;
	/** 入力データ：到着地(画面保持用) */
	protected String to01;
	/** 入力データ：到着地(画面保持用) */
	protected String to02;
	/** 入力データ：到着地(画面保持用) */
	protected String to03;
	/** 入力データ：到着地(画面保持用) */
	protected String to04;
	/** イントラ版フラグ(イントラ：true, WebService：false) */
	protected String isIntra;
	/** 定期区間表示フラグ(0:表示しない, 1:表示する) */
	protected String isShowTeikiKukan;
	/** 定期区間駅名(イントラ版) */
	protected String intraTeikiKukan;
	/** 定期区間路線名(イントラ版) */
	protected String intraRestoreroute;
	/** 定期区間情報(WebService版) */
	protected String webServiceTeiki;
	/** 定期区間シリアライズデータ(WebService版) */
	protected String webServiceTeikiSerializeData;
	/** ICカード料金算出区分(0：IC料金算出しない, 1:IC料金算出する) */
	protected String val_icticket;
	/** 駅すぱあと経路検索表示順リスト */
	protected List<GMap> ekispertSortList;
	/** 駅すぱあと表示順 */
	protected String val_sorttype;
	/** 駅すぱあと表示件数 */
	protected String val_max_result;

	/** 定期使用期間区分 */
	protected String kikanKbn;

	//20220224 追加
	/** 駅すぱあと予約サービスリスト */
	protected List<GMap> ekispertJrYoyakuList;

	/**
	 * 駅すぱあと初期設定
	 * @param connection DBコネクション
	 * @param onewayKbn 金額表示区分(1:運賃, 2:定期)
	 * @return 成功:success, 失敗:error
	 */
	public String ekispertInit(EteamConnection connection, String onewayKbn) {

		isIntra = setting.intraFlg();

		// イントラ版のみ戻り先URLを取得する
		if ("1".equals(isIntra)) {
			val_cgi_url = setting.shotodokeUrl() + "eki_intra_back";
			intraAction = setting.ekispertIntraActionUrl();
		}

		// 検索モード設定(1:運賃, 2:定期)
		val_oneway = onewayKbn;

		if ("ic".equals(setting.ekispertIcKbn())) {
			val_icticket = "1";
		} else {
			val_icticket = "0";
		}

		// 表示順を設定
		setEkispertSortList(connection);
		//20220224 追加
		//EX予約/選択リストの設定
		setEkispertJrYoyakuList(connection);

		// 表示件数を設定
		if (val_max_result == null) {
			// デフォルト5件を設定
			val_max_result = "5";
		}

		if (SEARCH_MODE_UNCHIN.equals(onewayKbn)) {
			// 定期区間表示する
			isShowTeikiKukan = "1";
		} else {
			// 定期区間表示しない
			isShowTeikiKukan = "0";
		}

		return "success";
	}

	/**
	 * イントラ版検索結果
	 */
	public class EkispertResultSet{
		/** ルート */ String route;
		/** 金額 */ String kind; //なんで金額がkindやねん・・・
		/** 距離 */ String distance;
		/** 定期控除したか */ String assignStatus; //0：成功、1：予期せぬエラー、2：定期経路が不適切、3：定期区間を含む経路が存在しない、4：定期を利用しても運賃が高くなってしまう、または変わらない、99：val_teiki_errcode が 0( 正常 ) 以外
		/** 定期利用可否 */ String teikiAvailable; //0:使用不可 1:使用可能
		/** 定期区間文字列 */ String restoreRoute; //該当ルートの定期検索用の方向性を持った経路文字列
		/** 増税非対応 */ int taxNonSupported; //0:対応済　1：改定前の金額、2：見込の金額
		//20220218 追加
		/** 割引名称 */ String discountName;
	}
	/**
	 * 経路探索結果を設定する
	 * @param shiyouKikanKbn 使用期間区分(運賃:00, １ヶ月:01, ３ヶ月:03, ６ヶ月:06) 無期限で検索した時は便宜上01でくる
	 * @return 経路探索結果
	 */
	public EkispertResultSet searchResultSet(String shiyouKikanKbn) {
		HttpServletRequest request = ServletActionContext.getRequest();

		EteamEkispertCommonIntra eecI = EteamContainer.getComponent(EteamEkispertCommonIntra.class);

		// 経路検索時
		if ("searchRoute".equals(searchKbn)) {
			EkispertResultSet ret = new EkispertResultSet();
			ret.route = eecI.selectRoute(request, shiyouKikanKbn);
			ret.kind = eecI.selectKind(request, shiyouKikanKbn);
			ret.distance = eecI.selectDistance(request);
			ret.assignStatus = eecI.getAssignStatus(request);
			ret.teikiAvailable = eecI.getTeikiAvailable(request);
			if(!("00".equals(shiyouKikanKbn))) {//この判定無効な気がする・・・運賃のときshiyouKikanKbn=ブランクできてるし・・・
				ret.restoreRoute = eecI.getValRestoreRoute(request);
			}else {
				ret.restoreRoute = "";
			}
			ret.taxNonSupported = eecI.getTaxNonSupported(request, ArrayUtils.contains(new String[] {"01","03","06"}, shiyouKikanKbn));
			ret.discountName = eecI.selectDiscountName(request);
			return ret;
		}

		// 駅名検索時
		if ("searchFrom".equals(searchKbn)) {
			from = eecI.selectStationName(request);
		} else if ("searchTo01".equals(searchKbn)) {
			to01 = eecI.selectStationName(request);
		} else if ("searchTo02".equals(searchKbn)) {
			to02 = eecI.selectStationName(request);
		} else if ("searchTo03".equals(searchKbn)) {
			to03 = eecI.selectStationName(request);
		} else if ("searchTo04".equals(searchKbn)) {
			to04 = eecI.selectStationName(request);
		}

		return null;
	}

	/**
	 * 定期区間を設定する
	 * @param connection コネクション
	 * @param date 検索日付
	 * @param dairiShiyouId 代理起票時の使用者ID(通常起票時はブランク)
	 * @return 成功:success, 失敗:error
	 */
	public String teikiKukanResultSet(EteamConnection connection, String date, String dairiShiyouId) {

		try {
			// 定期区間を取得する
			EteamEkispertCommonLogic eecl = EteamContainer.getComponent(EteamEkispertCommonLogic.class, connection);

			//代理起票時は使用者として指定したユーザーIDを参照
			String userId = super.getKihyouUserId(connection, getUser().getSeigyoUserId());
			if(!isEmpty(dairiShiyouId)) {
				userId = dairiShiyouId;
			}

			GMap teikiJohoMap = eecl.loadTeikiJouhou(userId, toDate(date));

			if (teikiJohoMap == null || teikiJohoMap.isEmpty()) {
				throw new Exception();
			}
			// イントラ版
			if(setting.intraFlg().equals("1")) {
				intraTeikiKukan = teikiJohoMap.getString("intra_teiki_kukan");
				intraRestoreroute = teikiJohoMap.getString("intra_restoreroute");

			// WebService版
			}else {
				webServiceTeiki = teikiJohoMap.getString("web_teiki_kukan");
				webServiceTeikiSerializeData = teikiJohoMap.get("web_teiki_serialize_data");
			}

			return "success";

		} catch (Exception e) {

			// エラーが発生した場合、定期区間を空に設定し、エラーが起きないように制御
			// イントラ版
			intraTeikiKukan = "";
			intraRestoreroute = "";

			// WebService版
			webServiceTeiki = "";
			webServiceTeikiSerializeData = "";

			return "success";
		}
	}

	/**
	 * 駅すぱあとの検索表示順リストを設定する
	 * @param connection DBコネクション
	 */
	protected void setEkispertSortList(EteamConnection connection) {

		SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

		ekispertSortList = common.loadNaibuCdSetting(EKISPERT_SORT);
	}

	//20220224 追加
	/**
	 * 駅すぱあとの予約サービス名リストを設定する
	 * @param connection DBコネクション
	 */
	protected void setEkispertJrYoyakuList(EteamConnection connection) {

		SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

		ekispertJrYoyakuList = common.loadNaibuCdSetting(EKISPERT_JRYOYAKU);
	}
}
