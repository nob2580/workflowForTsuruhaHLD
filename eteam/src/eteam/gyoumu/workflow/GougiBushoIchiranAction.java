package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.GougiBushoCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 合議部署一覧画面Action
 */
@Getter @Setter @ToString
public class GougiBushoIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	
//＜画面入力以外＞

	/** 伝票ID */
	protected String denpyouId;
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 合議部署リスト */
	protected List<GMap> gougiList;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザー区分 */
	protected String loginUserKbu;
	/** URLパス */
	protected String urlPath;
	/** 現在地 */
	protected String currentLocation;
	/** 上下フラグ */
	protected boolean upDownFlg;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
	};

//＜イベント＞
	/**
	 * E1:合議部署一覧初期表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		// セッションからログインユーザー情報を取得
		loginUserInfo = getUser();

		try(EteamConnection connection = EteamConnection.connect()){
			GougiBushoCategoryLogic bsril = EteamContainer.getComponent(GougiBushoCategoryLogic.class, connection);
			
			// 有効期限内の合議部署リストを取得
			List<GMap> list = bsril.load();
			// 取得した合議部署リストを画面表示用に作成する
			gougiList = createShowList(list);
			
			// 6.戻り値
			return "success";
		}
	}
	
	/**
	 * E2:合議部署一覧順序替え処理
	 * @return 成功：success, 失敗:error
	 */
	public String junjo() {
		// セッションからログインユーザー情報を取得
		loginUserInfo = getUser();

		try(EteamConnection connection = EteamConnection.connect()){
			GougiBushoCategoryLogic bsril = EteamContainer.getComponent(GougiBushoCategoryLogic.class, connection);
			
			bsril.updateJunjo(toInteger(currentLocation), upDownFlg);
			connection.commit();
			
			// 有効期限内の合議部署リストを取得
			List<GMap> list = bsril.load();
			// 取得した合議部署リストを画面表示用に作成する
			gougiList = createShowList(list);
			
			// 6.戻り値
			return "success";
		}
	}
	
	/**
	 * 画面表示用にレコードをまとめる
	 * @param list 合議部署リスト
	 * @return 画面表示用合議部署リスト
	 */
	protected List<GMap> createShowList(List<GMap> list) {

		List<GMap> retList = new ArrayList<GMap>();

		int index = 0;
		
		for(GMap map : list) {
			
			GMap retMap = new GMap();

			// 合議部署パターンナンバーが一致した場合
			if ((0 < index) && map.get("gougi_pattern_no").equals(list.get(index - 1).get("gougi_pattern_no"))) {
				
				// 所属部門名を<BR>でつなぎ、１レコードとする
				retList.get(retList.size() - 1).put("bumon",
						retList.get(retList.size() - 1).get("bumon")
								+ "\r\n" + map.get("bumon_name"));
				// 役割名を<BR>でつなぎ、１レコードとする
				retList.get(retList.size() - 1).put("yakuwari",
						retList.get(retList.size() - 1).get("yakuwari")
								+ "\r\n" + map.get("bumon_role_name"));
				// 処理権限名を<BR>でつなぎ、１レコードとする
				retList.get(retList.size() - 1).put("shori_kengen",
						retList.get(retList.size() - 1).get("shori_kengen")
								+ "\r\n" + map.get("shounin_shori_kengen_name"));
				// 承認人数比率を<BR>でつなぎ、１レコードとする
				retList.get(retList.size() - 1).put("shounin_ninzuu",
						retList.get(retList.size() - 1).get("shounin_ninzuu")
								+ "\r\n" + map.get("hiritsu"));
				
			} else {
				// 合議No
				retMap.put("gougi_pattern_no", map.get("gougi_pattern_no"));
				// 合議名
				retMap.put("gougi_name", map.get("gougi_name"));
				// 部門
				retMap.put("bumon", map.get("bumon_name"));
				// 役割
				retMap.put("yakuwari", map.get("bumon_role_name"));
				// 処理権限
				retMap.put("shori_kengen", map.get("shounin_shori_kengen_name"));
				// 承認人数
				retMap.put("shounin_ninzuu", map.get("hiritsu"));
				// レコードを追加
				retList.add(retMap);
			}
			index++;
		}

		return retList;
	}
}
