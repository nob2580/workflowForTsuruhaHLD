package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.BumonSuishouRouteCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門推奨ルート一覧画面Action
 */
@Getter @Setter @ToString
public class BumonSuishouRouteIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	
//＜画面入力以外＞

	/** 伝票ID */
	protected String denpyouId;
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 部門推奨ルートリスト */
	protected List<GMap> bumonRouteList;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザー区分 */
	protected String loginUserKbu;
	/** URLパス */
	protected String urlPath;
	/** 取引項目表示フラグ */
	boolean torihikiHyoujiFlg;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
	};

//＜イベント＞
	/**
	 * E1:部門推奨ルート一覧初期表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		// セッションからログインユーザー情報を取得
		loginUserInfo = getUser();

		try(EteamConnection connection = EteamConnection.connect()){
			BumonSuishouRouteCategoryLogic bsril = EteamContainer.getComponent(BumonSuishouRouteCategoryLogic.class, connection);
			DenpyouShubetsuIchiranDao dao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);
			
			// 取引項目表示判定(伝票管理で「取引毎にルート設定する/しない」が全伝票種別OFFの場合は項目を表示しない)
			torihikiHyoujiFlg = dao.countRouteTorihikiFlg();
			
			// 有効期限内の部門推奨ルートリストを取得
			List<GMap> list = bsril.load();
			
			// 取得した部門推奨ルートリストを画面表示用に作成する
			bumonRouteList = createShowList(list);
			
			// 6.戻り値
			return "success";
		}
	}
	
	/**
	 * 画面表示用にレコードをまとめる
	 * @param list 部門推奨ルートリスト
	 * @return 画面表示用部門推奨ルートリスト
	 */
	protected List<GMap> createShowList(List<GMap> list) {
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonSuishouRouteHenkouLogic bsrtl = EteamContainer.getComponent(BumonSuishouRouteHenkouLogic.class, connection);
			KaikeiCategoryLogic kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			List<GMap> retList = new ArrayList<GMap>();

			int index = 0;
			
			for(GMap map : list) {
				
				GMap retMap = new GMap();

				// 伝票種別、伝票区分、部門名、部門コード、枝番号、金額、有効期限がすべて一致した場合
				if ((0 < index) &&
					map.get("denpyou_shubetsu").equals(list.get(index - 1).get("denpyou_shubetsu")) &&
					map.get("denpyou_kbn").equals(list.get(index - 1).get("denpyou_kbn")) &&
					map.get("bumon_name").equals(list.get(index - 1).get("bumon_name")) &&
					map.get("bumon_cd").equals(list.get(index - 1).get("bumon_cd")) &&
					map.get("edano").equals(list.get(index - 1).get("edano")) &&
					((map.get("kingaku_from") == null) ? 0 : Long.parseLong(map.getString("kingaku_from"))) == 
							((list.get(index - 1).get("kingaku_from") == null) ? 0 : Long.parseLong(list.get(index - 1).getString("kingaku_from"))) &&
					((map.get("kingaku_to") == null) ? 0 : Long.parseLong(map.getString("kingaku_to"))) == 
							((list.get(index - 1).get("kingaku_to") == null) ? 0 : Long.parseLong(list.get(index - 1).getString("kingaku_to"))) &&
					map.get("yuukou_kigen_from").equals(list.get(index - 1).get("yuukou_kigen_from")) &&
					map.get("yuukou_kigen_to").equals(list.get(index - 1).get("yuukou_kigen_to"))) {
					
					// 部門名を<BR>でつなぎ、１レコードとする
					String gougiTopStr = "";
					if (map.get("gougi_edano") != null && map.get("gougi_edano").equals(1)) {
						gougiTopStr = "*";
					}
					retList.get(retList.size() - 1).put("shouninsha",
							retList.get(retList.size() - 1).get("shouninsha")
									+ "\r\n" + (map.get("gougi_pattern_no") == null ? map.get("bumon_role_name") + "　" + map.get("shounin_shori_kengen_name") : gougiTopStr + map.get("gougi_name")));
					
				} else {
					// 取引情報取得→加工(bsril.load()では取得していないのでこのタイミングで取得)
					List<GMap> shiwakeEdaNoList = bsrtl.getBumonSuishouRouteTorihikiList(map.get("denpyou_kbn"), map.get("bumon_cd"), map.get("edano"));
					List<String> shiwakeEdano = new ArrayList<String>();
					List<String> torihikiName = new ArrayList<String>();
					if (shiwakeEdaNoList.size() != 0) {
							// bumonRouteList:取引情報 = 1:n(n>=1)の関係になるため、mapにはリスト型で格納する
							shiwakeEdaNoList.forEach(s -> shiwakeEdano.add(Integer.toString(s.get("shiwake_edano"))));
							shiwakeEdaNoList.forEach(s -> torihikiName.add(kaikeiLogic.findShiwakePattern(map.get("denpyou_kbn"), s.get("shiwake_edano")).get("torihiki_name")));
					}else {
						// そもそも取引設定がされてなかったり設定できない場合は「なし」と表示する
						shiwakeEdano.add("なし");
						torihikiName.add("なし");
					}
					retMap.put("shiwakeEdano", shiwakeEdano);
					retMap.put("torihikiName", torihikiName);
					retMap.put("countTorihiki", torihikiHyoujiFlg && shiwakeEdaNoList.size() != 0 ? shiwakeEdaNoList.size() : "1"); // セル結合に用いる
					
					// 伝票名
					retMap.put("denpyouName", map.get("denpyou_shubetsu"));
					// 伝票区分
					retMap.put("denpyouKbn", map.get("denpyou_kbn"));
					// 部門名
					retMap.put("kihyouBumon", map.get("bumon_name"));
					// 部門コード
					retMap.put("bumonCd", map.get("bumon_cd"));
					// 枝番号
					retMap.put("edano", map.get("edano"));
					// 承認者
					String gougiTopStr = "";
					if (map.get("gougi_edano") != null && map.get("gougi_edano").equals(1)) {
						gougiTopStr = "*";
					}
					retMap.put("shouninsha", map.get("gougi_pattern_no") == null ? map.get("bumon_role_name") + "　" + map.get("shounin_shori_kengen_name") : gougiTopStr + map.get("gougi_name"));
					
					// 金額開始、金額終了がどちらもnullではない場合
					if (map.get("kingaku_from") != null && map.get("kingaku_to") != null) {
						retMap.put("kingaku", "申請金額「" + formatMoney(map.get("kingaku_from")) + "円～" +  formatMoney(map.get("kingaku_to")) + "円」");
					
					// 金額開始がnull、金額終了がnullではない場合
					} else if (map.get("kingaku_from") == null && map.get("kingaku_to") != null) {
						retMap.put("kingaku",  "申請金額「" + formatMoney(map.get("kingaku_to")) + "円」以下");
					
					// 金額開始がnullではなく、金額終了がnullの場合
					} else if (map.get("kingaku_from") != null && map.get("kingaku_to") == null) {
						retMap.put("kingaku",  "申請金額「" + formatMoney(map.get("kingaku_from")) + "円」以上");
					} else {
						retMap.put("kingaku",  "なし");
					}

					// 有効期限
					retMap.put("yuukouKigen", formatDate(map.get("yuukou_kigen_from")) + "～" + formatDate(map.get("yuukou_kigen_to")));
					
					// 背景色の設定
					retMap.put("bg_color", EteamCommon.bkColorSettei(formatDate(map.get("yuukou_kigen_from")),
							formatDate(map.get("yuukou_kigen_to"))));
					
					// レコードを追加
					retList.add(retMap);
				}
				index++;
			}

			return retList;
		}
	}
}
