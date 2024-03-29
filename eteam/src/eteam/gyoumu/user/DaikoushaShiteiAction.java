package eteam.gyoumu.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.DaikoushaCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 代行者指定画面Action
 */
@Getter @Setter @ToString
public class DaikoushaShiteiAction extends EteamAbstractAction {
	
	//＜定数＞
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(DaikoushaShiteiAction.class);
	
	//＜画面入力＞
	/** 被代行者ユーザーID */
	protected String hiDaikouUserId;
	/** 被代行者ユーザー名 */
	protected String hiDaikouUserName;
	
	/** 選択した部門コード */
	protected String choiceBumonCd;
	/** 選択した部門名 */
	protected String choiceBumonName;
	
	/** ユーザーID(重複なし) */
	protected String daikouUserId[];
	/** ユーザーID(重複あり、退避用) */
	protected String userId[];
	/** 社員番号 */
	protected String shainNo[];
	/** ユーザーフル名 */
	protected String userFullName[];
	/** 部門コード */
	protected String bumonCode[];
	/** 部門フル名 */
	protected String bumonFullName[];
	/** 部門ロールID */
	protected String bumonRoleId[];
	/** 部門ロール名 */
	protected String bumonRoleName[];

	
	//＜画面入力以外＞
	/** 権限レベル */
	protected boolean IsKanri;
	/** 部門リスト */
	protected List<GMap> bumonList;
	/** 代行者リスト */
	protected List<GMap> daikoushaList;
	/** 登録済み代行者リスト */
	protected List<GMap> tourokuzumiDaikoushaList;
	
	/** 部門ユーザー管理カテゴリロジック */
	protected BumonUserKanriCategoryLogic bumonUserLogic;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(hiDaikouUserId,      1, 30, "被代行者",      true);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{hiDaikouUserId,              "被代行者"			,"0","0", "1", },
			};
		hissuCheckCommon(list, eventNum);
	}
	
	/**
	 * 代行者指定機能へのアクセスが可能かチェック
	 * (代行機能自体の有効無効は画面権限制御でチェック)
	 * @param connection コネクション
	 */
	protected void accessCheck(EteamConnection connection){
		SystemKanriCategoryLogic syslc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		String accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, getUser().getGyoumuRoleId(), "CO");
		
		//一般ユーザーログインで、一般ユーザーによる代行者指定無効ならエラー
		if(accessAuthority.length() == 0 && !(syslc.judgeKinouSeigyoON(KINOU_SEIGYO_CD.USER_DAIKOU_SHITEI)) ) {
			throw new EteamAccessDeniedException(); // 不正アクセス
		}
		//adminユーザーか会社設定業務ロールログインで、管理者による代行者指定無効ならエラー
		if ( (accessAuthority.equals("SU") || accessAuthority.equals("CO")) && !(syslc.judgeKinouSeigyoON(KINOU_SEIGYO_CD.KANRI_DAIKOU_SHITEI)) ) {
			throw new EteamAccessDeniedException(); // 不正アクセス
		}
	}

	/**
	 * E1:初期表示イベント(初期表示と被代行者選択時)
	 * @return 結果
	 */
	public String init() {
		//1.入力チェック
		hissuCheck(1);
		formatCheck();
		if(!errorList.isEmpty()){
			return "error";
		}
		//2.データ存在チェック
		//なし

		try(EteamConnection connection = EteamConnection.connect()) {
			//3.アクセス可能チェック
			accessCheck(connection);
			
			//4.処理
			bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			DaikoushaCategoryLogic dcLogic = EteamContainer.getComponent(DaikoushaCategoryLogic.class, connection);
			
			// 所属部門リストを取得
			bumonList = EteamCommon.bumonListHenshuu(connection, bumonUserLogic.selectBumonTreeStructure("", true));
			
			// 被代行者領域の設定
			setHidaikouSha(connection);
			
			if(isNotEmpty(choiceBumonCd)) {
				// 選択された部門コードから部門名を取得
				choiceBumonName = EteamCommon.connectBumonName(connection, choiceBumonCd);
				// 選択された部門コードをキーに有効期限内のユーザーを取得
				daikoushaList = bumonUserLogic.userSerach("", choiceBumonCd, "", "", true);
				for (GMap map : daikoushaList) {
					map.put("user_full_name", map.getString("user_sei") + "　" + map.getString("user_mei"));
					map.put("bumon_full_name", EteamCommon.connectBumonName(connection, choiceBumonCd));
				}
			}
			
			// 登録済代行者リストを取得する(管理者の初期表示時は被代行者がまだなので飛ばす）
			if (isNotEmpty(hiDaikouUserId)) {
				tourokuzumiDaikoushaList = dcLogic.daikoushaSerach(hiDaikouUserId);
				for (GMap map : tourokuzumiDaikoushaList) {
					map.put("user_full_name", map.getString("user_sei") + "　" + map.getString("user_mei"));
					//現在日時点の名称データを取得
					String bFull = EteamCommon.connectBumonName(connection, map.getString("bumon_cd"), new Date(System.currentTimeMillis()) );
					if(bFull.length() == 0) {bFull = "(削除)";}

					map.put("bumon_full_name", bFull);
				}
			}

			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * E2:代行者検索イベント
	 * @return 結果
	 */
	public String kensaku() {
		//1.入力チェック
		hissuCheck(2);
		formatCheck();
		if(!errorList.isEmpty()){
			return "error";
		}
		//2.データ存在チェック
		//なし
		
		try(EteamConnection connection = EteamConnection.connect()) {
			//3.アクセス可能チェック
			accessCheck(connection);
			
			//4.処理
			restoreData(connection);
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * E3:代行者登録イベント
	 * @return 結果
	 */
	public String touroku() {
		//1.入力チェック
		//2.データ存在チェック
		//なし
		try(EteamConnection connection = EteamConnection.connect()) {
			//3.アクセス可能チェック
			accessCheck(connection);
			
			// 未選択チェック→なし：代行者０人にする必要があるのでチェックせず
// if (daikouUserId == null || daikouUserId.length == 0) {
// errorList.add("代行者が選択されていません。");
// }
			
			hissuCheck(3);
			formatCheck();
			if(!errorList.isEmpty()){
				// 画面再表示処理
				restoreData(connection);
				return "error";
			}
			
			// 一般ユーザーの場合、被代行ユーザーIDとログインユーザーIDが一致するかチェックする
			// 権限判定
			String accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, getUser().getGyoumuRoleId(), "CO");
			if(accessAuthority.length() == 0 && !getUser().getTourokuOrKoushinUserId().equals(hiDaikouUserId)) {
				throw new EteamAccessDeniedException(); // 不正アクセス
			}
			
			// ユーザーIDでソート
			String[] tmpUserId = daikouUserId;
			
			if (tmpUserId != null) {
				Arrays.sort(tmpUserId);
				log.debug("代行者数:"+ tmpUserId.length);
				for (int i = 0; i < tmpUserId.length; i++) {
					log.debug("代行者ID:" + tmpUserId[i]);
					//本人チェック
					if (tmpUserId[i].equals(hiDaikouUserId)) {
						errorList.add("本人を代行者に指定することはできません。");
						break;
					}
					// 重複チェック
					if (i > 0 && tmpUserId[i - 1].equals(tmpUserId[i])) {
						errorList.add("代行者が重複しています。");
						break;
					}
				}
			}
			// チェックエラーの場合、エラーメッセージを表示して終了
			if (errorList.size() > 0) {
				// 画面再表示処理
				restoreData(connection);
				return "error";
			}
			
			// 代行者登録処理
			DaikoushaShiteiLogic dtLogic = EteamContainer.getComponent(DaikoushaShiteiLogic.class, connection);
			
			dtLogic.insertDaikousha(daikouUserId, hiDaikouUserId, getUser().getTourokuOrKoushinUserId());
			connection.commit();
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 画面再表示用データの取得
	 * @param connection DBコネクション
	 */
	protected void restoreData(EteamConnection connection) {
		
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		// 所属部門リストを取得
		bumonList = EteamCommon.bumonListHenshuu(connection, bumonUserLogic.selectBumonTreeStructure("", true));
		
		// 被代行者領域の設定
		setHidaikouSha(connection);
		
		if(isNotEmpty(choiceBumonCd)) {
			// 選択された部門コードから部門名を取得
			choiceBumonName = EteamCommon.connectBumonName(connection, choiceBumonCd);
			// 選択された部門コードをキーに有効期限内のユーザーを取得
			daikoushaList = bumonUserLogic.userSerach("", choiceBumonCd, "", "", true);
			for (GMap map : daikoushaList) {
				map.put("user_full_name", map.getString("user_sei") + "　" + map.getString("user_mei"));
				map.put("bumon_full_name", EteamCommon.connectBumonName(connection, choiceBumonCd));
			}
		}
		// 代行者登録リストの作成（再表示のデータ保持）
		tourokuzumiDaikoushaList = createDaikoushaList();
	}
	
	/**
	 * 代行者リスト作成
	 * 画面から受け取ったデータを返却する
	 * @return 代行者リスト
	 */
	protected ArrayList<GMap> createDaikoushaList() {
		ArrayList<GMap> retList = new ArrayList<GMap>();
		if (userId != null) {
			for (int i = 0; i < userId.length; i++) {
				GMap map = new GMap();
				map.put("daikou_user_id", userId[i]);
				map.put("user_id", userId[i]);
				map.put("shain_no", shainNo[i]);
				map.put("user_full_name", userFullName[i]);
				map.put("bumon_cd", bumonCode[i]);
				map.put("bumon_full_name", bumonFullName[i]);
				map.put("bumon_role_id", bumonRoleId[i]);
				map.put("bumon_role_name", bumonRoleName[i]);
				retList.add(map);
			}
		}
		
		return retList;
	}
	
	/**
	 * 被代行者領域の表示設定
	 * @param connection      コネクション
	 */
	protected void setHidaikouSha(EteamConnection connection) {

		// 権限判定
		String accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, getUser().getGyoumuRoleId(), "CO");
		
		// 一般ユーザーの場合
		if(accessAuthority.length() == 0) {
			IsKanri = false;
			hiDaikouUserId = getUser().getTourokuOrKoushinUserId();
			GMap userInfo = bumonUserLogic.selectUserInfo(hiDaikouUserId);
			hiDaikouUserName = userInfo.getString("user_sei") + "　" + userInfo.getString("user_mei");
		}
		// adminユーザーか会社設定業務ロールの場合
		if (accessAuthority.equals("SU") || accessAuthority.equals("CO")) {
			IsKanri = true;
			if(isNotEmpty(hiDaikouUserId)) {
				GMap userInfo = bumonUserLogic.selectUserInfo(hiDaikouUserId);
				hiDaikouUserName = userInfo.getString("user_sei") + "　" + userInfo.getString("user_mei");
			}
		}
	}
}
