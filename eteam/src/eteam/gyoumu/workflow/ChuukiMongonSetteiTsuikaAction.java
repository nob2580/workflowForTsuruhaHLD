package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最終承認者・注記文言設定一覧画面Action
 */
@Getter @Setter @ToString
public class ChuukiMongonSetteiTsuikaAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 処理権限名 */
	protected String[] shorikengen;
	/** 承認者 */
	protected String[] gyoumuRole;
	/** 業務ロールID */
	protected String[] gyoumuRoleId;
	/** 注記文言 */
	protected String chuukiMongon;
	/** 有効期限開始日 */
	protected String yuukouKigenFrom;
	/** 有効期限終了日 */
	protected String yuukouKigenTo;
//＜画面入力以外＞

	/** 伝票リスト */
	protected List<GMap> denpyouList;
	/** 伝票プルダウンHTML文字列 */
	protected String denpyouPull;
	/** 承認者リスト */
	protected List<GMap> gyoumuRoleList;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザーID */
	protected String loginUserId;
	/** ログインユーザー区分 */
	protected String loginUserKbu;
	/** URLパス */
	protected String urlPath;
	/** イベントコードのマジックナンバー:更新 */
	protected static final int updateEvent = 1;
	/** エラーフラグ */
	protected String isError = "0";

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouKbn, 4, 4, "伝票区分", true);
		for(int i = 0 ; i < shorikengen.length ; i++){
			checkString(shorikengen[i], 1, 6, "処理権限名:" + i + "行目", true);
		}
		checkString(chuukiMongon, 1, 400, "注記文言", false);
		checkDate(yuukouKigenFrom, "有効期限開始日", false);
		checkDate(yuukouKigenTo, "有効期限終了日", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				{denpyouKbn, "伝票", "1" }, // 値、和名、EVENT1のフラグ
				{chuukiMongon, "注記文言", "1" }, // 値、和名、EVENT1のフラグ
				{yuukouKigenFrom, "有効期限開始", "1"}
			};
		hissuCheckCommon(list, eventNum);
	};

//＜イベント＞
	/**
	 * E1:初期表示表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {
		
		// セッションからログインユーザー情報を取得する/
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()) {
			ChuukiMongonSetteiTsuikaLogic cmstl = EteamContainer.getComponent(ChuukiMongonSetteiTsuikaLogic.class, connection);
			
			// 伝票リストの取得
			createDenpyouList(cmstl);
			// 伝票プルダウン文字列生成
			createDenpyouPullDown();

			if (denpyouList.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			// 業務ロールHTML文字列生成

			return "success";
		}
	}

	/**
	 * E2:最終承認者登録処理
	 * @return 成功：success, 失敗:error
	 */
	public String touroku() {
		
		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		loginUserId = loginUserInfo.getSeigyoUserId();
		
		try(EteamConnection connection = EteamConnection.connect()) {
			
			ChuukiMongonSetteiTsuikaLogic cmstl = EteamContainer.getComponent(ChuukiMongonSetteiTsuikaLogic.class, connection);
			
			// 伝票リストの再取得
			createDenpyouList(cmstl);
			// 伝票プルダウン文字列生成
			createDenpyouPullDown();
			
			// 1.入力チェック
			hissuCheck(updateEvent);
			formatCheck();
	
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				isError = "1";
				createGyoumuRoleList();
				return "error";
			}
			
			// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
			if (isEmpty(yuukouKigenTo)) {
				yuukouKigenTo = "9999/12/31";
			}
			
			// 有効期限の共通チェック
			for (Map<String, String> errMap : EteamCommon.kigenCheck(yuukouKigenFrom, yuukouKigenTo)) {
				// エラーコードが「0」以外の場合にエラーメッセージを追加する
				if (!"0".equals(errMap.get("errorCode"))) {
					errorList.add("有効期限" + errMap.get("errorMassage"));
				}
			}
			
			// 承認者の連続チェック
			for (int i = 0; i < gyoumuRoleId.length; i++) {
				if (i > 0 && gyoumuRoleId[i].equals(gyoumuRoleId[i - 1])) {
					errorList.add("最終承認者が重複しています。");
					break;
				}
			}
			
			// 2.データ存在チェック(重複チェック)
			List<GMap> deplicateRec = cmstl.checkDuplicate(denpyouKbn, toDate(yuukouKigenFrom), toDate(yuukouKigenTo));
			
			// 重複レコード数
			int cnt = Integer.parseInt(deplicateRec.get(0).getString("cnt"));

			if (cnt > 0) {
				errorList.add("重複レコードが存在します。");
			}

			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				isError = "1";
				createGyoumuRoleList();
				return "error";
			}
			
			// 3.アクセス可能チェック
			
			// 4.登録処理
			cmstl.insertSaishuuShouninRoute(denpyouKbn, gyoumuRoleId, shorikengen, chuukiMongon,
					toDate(yuukouKigenFrom), toDate(yuukouKigenTo), getUser().getTourokuOrKoushinUserId());
			
			
			connection.commit();
			
			// 5.戻り値
			return "success";
		}
	}
	
	/**
	 * 伝票リスト作成処理
	 * @param cmstl 最終承認者・注記文言設定追加機能Logic
	 */
	protected void createDenpyouList(ChuukiMongonSetteiTsuikaLogic cmstl) {
		
		// 有効期限内の伝票種別リストを取得する
		List<GMap> list = cmstl.load();
		
		if (list.isEmpty()) {
			return;
		}

		denpyouList = new ArrayList<GMap>();
		
		Object gyoumu_shubetsu = null;
		
		for (GMap map : list) {

			Object gyoumu_shubetsu_wk = map.get("gyoumu_shubetsu");
			
			if (gyoumu_shubetsu == null) {
				gyoumu_shubetsu = gyoumu_shubetsu_wk;
			} else {
				
				if (gyoumu_shubetsu.equals(gyoumu_shubetsu_wk)) {
					map.put("gyoumu_shubetsu", null);
				} else {
					gyoumu_shubetsu = gyoumu_shubetsu_wk;
				}
			}
			
			denpyouList.add(map);
		}
	}
	
	/**
	 * 業務ロールリストを設定する
	 */
	protected void createGyoumuRoleList() {
		
		gyoumuRoleList = new ArrayList<GMap>();
		
		for(int i = 0; i <gyoumuRole.length; i++) {
			GMap gyoumuRoleMap = new GMap();
			gyoumuRoleMap.put("shorikengen", shorikengen[i]);
			gyoumuRoleMap.put("gyoumuRole", gyoumuRole[i]);
			gyoumuRoleMap.put("gyoumuRoleId", gyoumuRoleId[i]);
	
			gyoumuRoleList.add(gyoumuRoleMap);
		}
		
	}
	
	/**
	 * 伝票プルダウン文字列を生成する
	 */
	protected void createDenpyouPullDown() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<select name='denpyouKbn' class='input-large'>");
		sb.append("<option></option>");

		for (int i = 0; i < denpyouList.size(); i++) {
			
			if (i == 0) {
				sb.append(" <optgroup label='" + denpyouList.get(i).get("gyoumu_shubetsu") + "'> ");
				sb.append(" <option value='" + denpyouList.get(i).get("denpyou_kbn") + "' ");
				
				if (denpyouList.get(i).get("denpyou_kbn").equals(denpyouKbn)) {
					sb.append(" selected ");
				}
				sb.append("> " + denpyouList.get(i).get("denpyou_shubetsu") + "</option>");
			} else if (i > 0 && denpyouList.get(i).get("gyoumu_shubetsu") != null) {
				sb.append("</optgroup>");
				sb.append(" <optgroup label='" + denpyouList.get(i).get("gyoumu_shubetsu") + "'> ");
				sb.append(" <option value='" + denpyouList.get(i).get("denpyou_kbn") + "' ");
				
				if (denpyouList.get(i).get("denpyou_kbn").equals(denpyouKbn)) {
					sb.append(" selected ");
				}
				sb.append("> " + denpyouList.get(i).get("denpyou_shubetsu") + "</option>");
			} else if (denpyouList.get(i).get("gyoumu_shubetsu") == null) {
				sb.append(" <option value='" + denpyouList.get(i).get("denpyou_kbn") + "' ");
				
				if (denpyouList.get(i).get("denpyou_kbn").equals(denpyouKbn)) {
					sb.append(" selected ");
				}
				sb.append("> " + denpyouList.get(i).get("denpyou_shubetsu") + "</option>");
			}
			if (i == denpyouList.size() - 1) {
				sb.append("</optgroup>");
			}
		}
		sb.append("</select>");
		denpyouPull = sb.toString();
	}
	
}
