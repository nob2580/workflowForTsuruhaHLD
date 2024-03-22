package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 最終承認者・注記文言設定変更画面Action
 */
@Getter @Setter @ToString
public class ChuukiMongonSetteiHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 伝票区分 */
	protected String denpyouKbn;
	/** 伝票名 */
	protected String denpyouName;
	/** 枝番号 */
	protected String edaNo;
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
	/** 承認者リスト */
	protected List<GMap> gyoumuRoleList;
	/** 最終承認ルートリスト */
	protected List<GMap> saishuRouteList;
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
			ChuukiMongonSetteiHenkouLogic cmshl = EteamContainer.getComponent(ChuukiMongonSetteiHenkouLogic.class, connection);

			// 最終承認ルート情報の取得
			saishuRouteList = cmshl.saishuShouninRouteList(denpyouKbn, Integer.parseInt(edaNo));
			
			if (saishuRouteList.isEmpty()) {
				throw new EteamDataNotFoundException();
			}
			
			gyoumuRoleList = new ArrayList<GMap>();
			
			shorikengen = new String[saishuRouteList.size()];
			gyoumuRole = new String[saishuRouteList.size()];
			gyoumuRoleId = new String[saishuRouteList.size()];
			
			// 画面項目に設定
			for (GMap map : saishuRouteList) {
				
				GMap gyoumuRoleMap = new GMap();
				
				denpyouKbn = map.getString("denpyou_kbn");
				denpyouName = map.getString("denpyou_shubetsu");
				edaNo = map.getString("edano");

				if (map.get("gyoumu_role_name") != null) {
					gyoumuRoleMap.put("shorikengen", map.getString("saishuu_shounin_shori_kengen_name"));
					gyoumuRoleMap.put("gyoumuRole", map.getString("gyoumu_role_name"));
					gyoumuRoleMap.put("gyoumuRoleId", map.getString("gyoumu_role_id"));
				}
				chuukiMongon = map.getString("chuuki_mongon");
				yuukouKigenFrom = formatDate(map.get("yuukou_kigen_from"));
				yuukouKigenTo = formatDate(map.get("yuukou_kigen_to"));

				gyoumuRoleList.add(gyoumuRoleMap);
			}

			return "success";
		}
	}

	/**
	 * E2:最終承認者更新処理
	 * @return 成功：success, 失敗:error
	 */
	public String koushin() {
		
		try(EteamConnection connection = EteamConnection.connect()) {
		
			// セッションからログインユーザー情報を取得する
			loginUserInfo = getUser();
			loginUserId = loginUserInfo.getSeigyoUserId();
	
			//1.入力チェック
			hissuCheck(updateEvent);
			formatCheck();
	
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				
				createGyoumuRoleList();
				return "error";
			}
	
			// 有効期限終了日が入力されていない場合、「9999/12/31」を代入する
			if (isEmpty(yuukouKigenTo)) {
				yuukouKigenTo = "9999/12/31";
			}
			
			// 有効期限の共通チェック
			for (Map<String, String> errMap: EteamCommon.kigenCheck(yuukouKigenFrom, yuukouKigenTo)) {
				// 開始日と終了日の整合性チェックのみエラーとする。
				if ("2".equals(errMap.get("errorCode"))) {
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
		
			ChuukiMongonSetteiHenkouLogic cmshl = EteamContainer.getComponent(ChuukiMongonSetteiHenkouLogic.class, connection);
			
			// 2.データ存在チェック(重複チェック)
			List<GMap> deplicateRec = cmshl.checkDuplicate(denpyouKbn, toDate(yuukouKigenFrom), toDate(yuukouKigenTo));
			
			// 重複レコード数
			int cnt = deplicateRec.size();

			if (cnt == 1 && !edaNo.equals(deplicateRec.get(0).getString("edano"))) {
				errorList.add("重複レコードが存在します。");

			} else if (cnt > 1) {
				errorList.add("重複レコードが存在します。");
			}
			
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				createGyoumuRoleList();
				return "error";
			}
			
			// 3.アクセス可能チェック
			
			// 4.処理
			// 更新処理
			cmshl.updateSaishuShouninRoute(denpyouKbn, Integer.parseInt(edaNo), gyoumuRoleId, shorikengen, chuukiMongon, toDate(yuukouKigenFrom),
					toDate(yuukouKigenTo), getUser().getTourokuOrKoushinUserId());
			
			
			connection.commit();
			// 5.戻り値
			return "success";
		}
	}
	
	/**
	 * E3:最終承認者削除処理
	 * @return 成功：success, 失敗:error
	 */
	public String sakujo() {

		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		loginUserId = loginUserInfo.getSeigyoUserId();

		try(EteamConnection connection = EteamConnection.connect()) {
			ChuukiMongonSetteiHenkouLogic cmshl = EteamContainer.getComponent(ChuukiMongonSetteiHenkouLogic.class, connection);

			
			// 4.処理
			// 削除処理
			cmshl.deleteSaishuShouninRoute(denpyouKbn, Integer.parseInt(edaNo));

			connection.commit();
			
			// 5.戻り値
			return "success";
		}
	}
	
	/**
	 * 業務ロールリストを設定する
	 */
	protected void createGyoumuRoleList() {
		
		gyoumuRoleList = new ArrayList<GMap>();
		
		for (int i = 0; i <gyoumuRole.length; i++) {
			GMap gyoumuRoleMap = new GMap();
			gyoumuRoleMap.put("shorikengen", shorikengen[i]);
			gyoumuRoleMap.put("gyoumuRole", gyoumuRole[i]);
			gyoumuRoleMap.put("gyoumuRoleId", gyoumuRoleId[i]);
	
			gyoumuRoleList.add(gyoumuRoleMap);
		}
		
	}
}
