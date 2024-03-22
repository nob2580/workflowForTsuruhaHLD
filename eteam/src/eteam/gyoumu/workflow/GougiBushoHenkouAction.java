package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 合議部署変更画面Action
 */
@Getter @Setter @ToString
public class GougiBushoHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 合議部署名 */
	protected String gougiBushoName;
	/** 起票部門 */
	protected String[] bumon;
	/** 部門コード */
	protected String[] bumonCd;
	/** 承認者 */
	protected String[] bumonRole;
	/** 部門ロールID */
	protected String[] bumonRoleCd;
	/** 処理権限No */
	protected String[] shoriKengen;
	/** 承認必要人数 */
	protected String[] hiritsu;
	/** 承認必要人数(パーセント) */
	protected String[] hiritsuPer;
	
//＜画面入力以外＞
	/** 合議部署No */
	protected String gougiNo;
	/** 処理権限リスト */
	protected List<GMap> shoriKengenList;
	/** 承認者リスト */
	protected List<GMap> shouninshaList;
	/** 承認人数リスト */
	protected List<GMap> shouninNinzuuList;
	/** 承認人数ドメイン */
	String[] shouninNinzuuDomain;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	/** ログインユーザーID */
	protected String loginUserId;
	/** ログインユーザー区分 */
	protected String loginUserKbu;
	/** URLパス */
	protected String urlPath;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(gougiBushoName, 1, 20, "合議部署名", false);
		for (int i = 0; i < bumon.length; i++) {
			checkDomain(hiritsu[i], shouninNinzuuDomain, "承認人数：" + (i+1) + "行目", false);
			if (hiritsu[i].equals("3")) {
				checkNumberOver1(hiritsuPer[i], 1, 2, "承認必要人数(パーセント)" + "：" + (i+1) + "行目", false);
			}
		}
		
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		hissuCheckCommon(new String[][]{
			{gougiNo, "合議部署No", "1"},
			{gougiBushoName, "合議部署名", "1"},
		}, eventNum);
		for (int i = 0; i < bumon.length; i++) {
			hissuCheckCommon(new String[][]{
				{bumon[i], "部門名" + "：" + (i+1) + "行目", "1"},
				{bumonCd[i], "部門ID" + "：" + (i+1) + "行目", "1"},
				{bumonRole[i], "部門ロール名" + "：" + (i+1) + "行目", "1"},
				{bumonRoleCd[i], "部門ロールID" + "：" + (i+1) + "行目", "1"},
				{shoriKengen[i], "処理権限No" + "：" + (i+1) + "行目", "1"},
				{hiritsu[i], "承認必要人数" + "：" + (i+1) + "行目", "1"},
			}, eventNum);
			if (hiritsu[i].equals("3")) {
				hissuCheckCommon(new String[][]{
					{hiritsuPer[i], "承認必要人数(パーセント)" + "：" + (i+1) + "行目", "1"},
				}, eventNum);
			}
		}
	};


	/**
	 * E1:初期表示表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()) {
			GougiBushoHenkouLogic bsrtl = EteamContainer.getComponent(GougiBushoHenkouLogic.class, connection);
			SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			shouninNinzuuList = sysLogic.loadNaibuCdSetting("shounin_ninzuu_cd");
			shouninNinzuuDomain = EteamCommon.mapList2Arr(shouninNinzuuList, "naibu_cd");
			
			// 処理権限リストの取得
			shoriKengenList = bsrtl.loadShouninShoriKengen();

			// 合議部署情報の取得
			List<GMap> list = bsrtl.getGougiBushoList(toInteger(gougiNo));
			if (list.isEmpty()) {
				throw new EteamDataNotFoundException();
			}
			
			shouninshaList = new ArrayList<GMap>();
			
			bumon = new String[list.size()];
			bumonCd = new String[list.size()];
			bumonRole = new String[list.size()];
			bumonRoleCd = new String[list.size()];
			shoriKengen = new String[list.size()];
			hiritsuPer = new String[list.size()];
			
			// 画面項目に設定
			gougiBushoName = (String)list.get(0).get("gougi_name");
			for(GMap map : list) {
				GMap shouninshaMap = new GMap();
				shouninshaMap.put("bumonCd", map.getString("bumon_cd"));
				shouninshaMap.put("bumon", map.getString("bumon_name"));
				shouninshaMap.put("bumonRoleCd", map.getString("bumon_role_id"));
				shouninshaMap.put("bumonRole", map.getString("bumon_role_name"));
				shouninshaMap.put("shoriKengenCd", map.getString("shounin_shori_kengen_no"));
				shouninshaMap.put("hiritsu", map.getString("shounin_ninzuu_cd"));
				shouninshaMap.put("hiritsuPer", map.get("shounin_ninzuu_hiritsu")== null ? "" : map.getString("shounin_ninzuu_hiritsu"));
				shouninshaList.add(shouninshaMap);
			}

			return "success";
		}
	}
	
	/**
	 * E2:合議部署更新処理
	 * @return 成功：success, 失敗:error
	 */
	public String koushin() {
		
		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		loginUserId = loginUserInfo.getSeigyoUserId();

		try(EteamConnection connection = EteamConnection.connect()){
			GougiBushoHenkouLogic bsrtl = EteamContainer.getComponent(GougiBushoHenkouLogic.class, connection);
			BumonUserKanriCategoryLogic bukcl = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			shouninNinzuuList = sysLogic.loadNaibuCdSetting("shounin_ninzuu_cd");
			shouninNinzuuDomain = EteamCommon.mapList2Arr(shouninNinzuuList, "naibu_cd");
			
			//1.入力チェック
			hissuCheck(1);
			formatCheck();
			
			shoriKengenList = bsrtl.loadShouninShoriKengen();

			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				createshouninshaList();
				return "error";
			}
			
			// 2.データ存在チェック
			for (int i = 0; i < bumonCd.length; i++) {
				GMap bumonMap = bukcl.selectValidShozokuBumon(bumonCd[i]);
				GMap bumonRoleMap = bukcl.selectBumonRole(bumonRoleCd[i]);
				GMap shoriKengenMap = bsrtl.loadShouninShoriKengen(toInteger(shoriKengen[i]));
				if (bumonMap == null) {
					errorList.add((i+1) + "行目：部門が見つかりません。");
				}
				if (bumonRoleMap == null) {
					errorList.add((i+1) + "行目：部門ロールが見つかりません。");
				}
				if (shoriKengenMap == null) {
					errorList.add((i+1) + "行目：承認処理権限が見つかりません。");
				}
			}
			// 役割・所属部門の連続チェック
			for (int i = 0; i < bumonRoleCd.length; i++) {
				if (i > 0 && bumonRoleCd[i].equals(bumonRoleCd[i - 1]) && bumonCd[i].equals(bumonCd[i - 1])) {
					errorList.add("承認者が重複しています。");
					break;
				}
			}
			
			// エラーがあれば処理を終了する
			if (!errorList.isEmpty()) {
				createshouninshaList();
				return "error";
			}
			
			// 3.アクセス可能チェック
			
			// 4.処理
			// 更新処理
			bsrtl.updateGougiBusho(toInteger(gougiNo), gougiBushoName, bumonCd, bumonRoleCd, shoriKengen, hiritsu, hiritsuPer, getUser().getTourokuOrKoushinUserId());
			
			connection.commit();
			// 5.戻り値
			return "success";
		}
	}
	
	/**
	 * E3:合議部署削除処理
	 * @return 成功：success, 失敗:error
	 */
	public String sakujo() {

		// セッションからログインユーザー情報を取得する
		loginUserInfo = getUser();
		loginUserId = loginUserInfo.getSeigyoUserId();

		try(EteamConnection connection = EteamConnection.connect()){
			GougiBushoHenkouLogic bsrtl = EteamContainer.getComponent(GougiBushoHenkouLogic.class, connection);
			SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			shouninNinzuuList = sysLogic.loadNaibuCdSetting("shounin_ninzuu_cd");
			shouninNinzuuDomain = EteamCommon.mapList2Arr(shouninNinzuuList, "naibu_cd");
			shoriKengenList = bsrtl.loadShouninShoriKengen();
			
			//削除前の確認処理追加（部門推奨ルートに使用していない、かつ使用された伝票が全て処理済）
			List<GMap> shouninList = bsrtl.loadBumonSuishouRoute(gougiNo);
			List<GMap> joutaiList = bsrtl.loadGougiDenpyouJoutai(gougiNo);
			
			// エラーがあれば処理を終了する
			if (!shouninList.isEmpty() || !joutaiList.isEmpty()) {
				errorList.add("承認ルート・部門推奨ルートにデータが存在するため、削除できません。");
				createshouninshaList();
				return "error";
			}
			
			// 4.処理
			// 更新処理
			int hyoujiJun = bsrtl.deleteGougiBusho(toInteger(gougiNo));
			// 表示順の整理
			bsrtl.updateHyoujiJun(hyoujiJun);

			connection.commit();
			
			// 5.戻り値
			return "success";
		}
	}
	
	/**
	 * 承認者リストを設定する
	 */
	protected void createshouninshaList() {
		shouninshaList = new ArrayList<GMap>();
		for(int i = 0; i <bumon.length; i++) {
			GMap shouninshaMap = new GMap();
			shouninshaMap.put("bumonCd", bumonCd[i]);
			shouninshaMap.put("bumon", bumon[i]);
			shouninshaMap.put("bumonRoleCd", bumonRoleCd[i]);
			shouninshaMap.put("bumonRole", bumonRole[i]);
			shouninshaMap.put("shoriKengenCd", shoriKengen[i]);
			shouninshaMap.put("hiritsu", hiritsu[i]);
			shouninshaMap.put("hiritsuPer", hiritsuPer[i]);

			shouninshaList.add(shouninshaMap);
		}
	}

}
