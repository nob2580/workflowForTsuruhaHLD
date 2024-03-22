package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.ChuukiMongonSetteiCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最終承認者・注記文言設定一覧画面Action
 */
@Getter @Setter @ToString
public class ChuukiMongonSetteiIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

	/** 伝票区分 */
	protected String denpyouKbn;
	/** 枝番号 */
	protected String edaNo;
	
//＜画面入力以外＞

	/** 最終承認リスト */
	protected List<GMap> saishuuShouninList;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザー区分 */
	protected String loginUserKbu; 
	/** URLパス */
	protected String urlPath;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
	};

//＜イベント＞
	/**
	 * E1:最終承認ルート一覧初期表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		// セッションからログインユーザー情報を取得
		loginUserInfo = getUser();

		try(EteamConnection connection = EteamConnection.connect()){
			ChuukiMongonSetteiCategoryLogic cmscl = EteamContainer.getComponent(ChuukiMongonSetteiCategoryLogic.class, connection);
			
			// 最終承認ルートリストを取得
			List<GMap> list = cmscl.load();

			// 取得した最終承認ルートリストを画面表示用に作成する
			saishuuShouninList = createShowList(list);

			
			// 6.戻り値
			return "success";
		}
	}
	
	/**
	 * 画面表示用にレコードをまとめる
	 * @param list 最終承認リスト
	 * @return 画面表示用最終承認リスト
	 */
	protected List<GMap> createShowList(List<GMap> list) {

		List<GMap> retList = new ArrayList<GMap>();

		int index = 0;
		
		for (GMap map : list) {
			
			GMap retMap = new GMap();

			// 伝票種別、伝票区分、枝番号が一致した場合
			if ((0 < index) &&
				map.get("denpyou_shubetsu").equals(list.get(index - 1).get("denpyou_shubetsu")) &&
				map.get("denpyou_kbn").equals(list.get(index - 1).get("denpyou_kbn")) && 
				map.get("edano").equals(list.get(index - 1).get("edano"))) {
				
				
				// 業務ロール名を<BR>でつなぎ、１レコードとする
				retList.get(retList.size() - 1).put("shouninsha",
						retList.get(retList.size() - 1).get("shouninsha")
								+ "\r\n" + map.get("gyoumu_role_name"));
				
			} else {
				
				// 伝票名
				retMap.put("denpyouName", map.get("denpyou_shubetsu"));
				// 伝票区分
				retMap.put("denpyouKbn", map.get("denpyou_kbn"));
				// 枝番号
				retMap.put("edaNo", map.get("edano"));
				// 承認者
				retMap.put("shouninsha", map.get("gyoumu_role_name"));
				// 注記文言
				retMap.put("chuukiMongon", map.get("chuuki_mongon"));

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
