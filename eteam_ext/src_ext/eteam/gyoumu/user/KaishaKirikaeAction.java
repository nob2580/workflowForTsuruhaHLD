package eteam.gyoumu.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamExtConst.Sessionkey;
import eteam.common.select.BumonUserKanriCategoryExtLogic;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 会社切替Action
 */
@Getter @Setter @ToString
public class KaishaKirikaeAction extends EteamAbstractAction {

//＜定数＞
//＜画面入力＞

//＜画面入力以外＞
	/** ログインユーザID */
	public String userId;
	/** 業務ロール */
	public String gyoumuRole;
	/** 移動前スキーマ */
	public String motoSchema;
	/** 移動先スキーマ */
	public String schema;
	/** 切替先URL */
	public String redirectURL;

//＜部品＞
	/** ロガー */
	private EteamLogger log = EteamLogger.getLogger(KaishaKirikaeAction.class);

//＜入力チェック＞
	@Override
	protected void formatCheck() {}
	@Override
	protected void hissuCheck(int eventNum) {}

	/**
	 * リダイレクトイベント
	 *
	 * @return 結果
	 */
	public String redirect(){

		//1.入力チェック
		this.formatCheck();
		this.hissuCheck(1);
		if(0 < super.errorList.size()){
			return "error";
		}
this.log.debug("[From]ログインユーザID=[" + this.userId + "]");
this.log.debug("[From]切替先URL=[" + this.redirectURL + "]");

		//2.データ存在チェック
		//3.アクセス可能チェック

		//4.処理
		EteamConnection connection = EteamConnection.connect();
		try {
			UserLogic lc = EteamContainer.getComponent(UserLogic.class);
			lc.init(connection);
			BumonUserKanriCategoryLogic bukcl = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class);
			bukcl.init(connection);

			//セッションにユーザー情報 + テナントID(スキーマ名)を入れる
			User user = lc.makeSessionUser(this.userId);
			List<GMap> kinouList = ((BumonUserKanriCategoryExtLogic)bukcl).selectGyoumuRoleKinouSeigyoKbnOtherSchema(gyoumuRole, motoSchema);
			Map<String, Object> kinouMap = new HashMap<String, Object>();
			for (Map<String, Object>map : kinouList) {
				kinouMap.put((String)map.get("gyoumu_role_kinou_seigyo_cd"), (String)map.get("gyoumu_role_kinou_seigyo_kbn"));
			}
			Map<String, Object> gyoumuRoleMap =  ((BumonUserKanriCategoryExtLogic)bukcl).selectGyoumuRoleOtherSchema((String)kinouMap.get("WF"), (String)kinouMap.get("KR"), (String)kinouMap.get("CO"), userId, schema);
			if (gyoumuRoleMap != null && !gyoumuRoleMap.isEmpty()) {
				lc.changeGyoumuRole(user, (String)gyoumuRoleMap.get("gyoumu_role_id"));
			}
			session.put(Sessionkey.USER, user);
			session.put(Sessionkey.SCHEMA_NAME, schema);

this.log.debug("[From]スキーマ=[" + super.getSchemaName() + "]");
		}finally{
			connection.close();
		}

		//5.戻り値を返す
		return "success";
	}
}
