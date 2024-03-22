package eteam.common;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.dao.GamenKengenSeigyoDao;
import eteam.database.dao.KinouSeigyoDao;
import eteam.database.dto.GamenKengenSeigyo;
import eteam.gyoumu.user.User;


/**
 * 画面権限制御Logic
 */
public class GamenKengenSeigyoLogic extends EteamAbstractLogic {
	
	/** システムカテゴリロジック */
	SystemKanriCategoryLogic systemLogic;
	/** 部門・ユーザー管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** 画面項目制御Dao */
	GamenKengenSeigyoDao gamenKengenSeigyo;
	/** 機能制御Dao */
	KinouSeigyoDao kinouSeigyoDao;
	
	@Override
	public void init(@SuppressWarnings("hiding") EteamConnection connection) {
		super.init(connection);
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		gamenKengenSeigyo =  EteamContainer.getComponent(GamenKengenSeigyoDao.class, connection);
		kinouSeigyoDao =  EteamContainer.getComponent(KinouSeigyoDao.class, connection);
	}
	
	/**
	 * 画面に対する利用権限有無を判定する。
	 * @param gamenId 画面ID
	 * @param user セッション・ユーザー情報
	 * @return 利用可能ならばtrue
	 */
	public boolean judgeAccess(String gamenId, User user) {
		final String extSuffix = "Ext";
		if (gamenId.endsWith(extSuffix)) gamenId = gamenId.substring(0, gamenId.length() - extSuffix.length());
		
		// 各利用可否フラグの取得
// GMap accessFlg = systemLogic.findGamenKengenSeigyoInfoFlg(gamenId);
		GamenKengenSeigyo kengen = gamenKengenSeigyo.find(gamenId);
		
		// 機能制御コードを取得します。
// String kinouSeigyoCd = accessFlg.get("kinou_seigyo_cd").toString();
		String kinouSeigyoCdDao = kengen.kinouSeigyoCd;
		
		// 機能制御を検索し、機能の有効・無効を判定します。
// if (kinouSeigyoCd.length() > 0) {
		if (kinouSeigyoCdDao.length() > 0) {
// String kinouSeigyoKbn = systemLogic.findKinouSeigyo(kinouSeigyoCd).get("kinou_seigyo_kbn").toString();
			String kinouSeigyoKbnDao = kinouSeigyoDao.find(kinouSeigyoCdDao).kinouSeigyoKbn;
// if(KINOU_SEIGYO_KBN.MUKOU.equals(kinouSeigyoKbn)) {
			if(KINOU_SEIGYO_KBN.MUKOU.equals(kinouSeigyoKbnDao)) {
				return false;
			}
		}
		
		// 部門所属ユーザーとしてログインしている場合、以下のように判定する。
		if(null != user.getBumonCd()) {
			// 部門所属ユーザー利用可能フラグ判定：「1:利用可能」「0:利用不可能」
// if("1".equals(accessFlg.get("bumon_shozoku_riyoukanou_flg"))) {
			if("1".equals(kengen.bumonShozokuRiyoukanouFlg)) {
				return true;
			} else {
				return false;
			}
		}
		
		// 「システム管理」の業務ロールでログインしている場合、以下のように判定
		if(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(user.getGyoumuRoleId())) {
			// システム管理利用可能フラグ判定：「1:利用可能」「0:利用不可能」
// if("1".equals(accessFlg.get("system_kanri_riyoukanou_flg"))) {
			if("1".equals(kengen.systemKanriRiyoukanouFlg)) {
				return true;
			} else {
				return false;
			}
			
		// 「システム管理」以外の業務ロールでログインしている場合
		} else {
			// 業務ロール機能制御区分の取得
			List<GMap> roleKbn = bumonUserLogic.selectGyoumuRoleKinouSeigyoKbn(user.getGyoumuRoleId());
			
			for (int i = 0; i < roleKbn.size(); i++) {
				// ワークフローの機能制御区分：「1:有効」かつ ワークフロー利用可能フラグ：「1:利用可能」
				if((EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD.WORKFLOW.equals(roleKbn.get(i).get("gyoumu_role_kinou_seigyo_cd")) &&
					EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_KBN.YUUKOU.equals(roleKbn.get(i).get("gyoumu_role_kinou_seigyo_kbn"))) &&
// "1".equals(accessFlg.get("workflow_riyoukanou_flg"))) {
					"1".equals(kengen.workflowRiyoukanouFlg)) {
					return true;
				}
				// 経理処理の機能制御区分：「1:有効」 かつ 経理処理利用可能フラグ：「1:利用可能」
				if((EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI.equals(roleKbn.get(i).get("gyoumu_role_kinou_seigyo_cd")) &&
					EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_KBN.YUUKOU.equals(roleKbn.get(i).get("gyoumu_role_kinou_seigyo_kbn"))) &&
// "1".equals(accessFlg.get("keirishori_riyoukanou_flg"))) {
					"1".equals(kengen.keirishoriRiyoukanouFlg)) {
					return true;
				}
				// 会社設定の機能制御区分：「1:有効」 かつ 会社設定利用可能フラグ：「1:利用可能」
				if((EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD.KAISHA_SETTEI.equals(roleKbn.get(i).get("gyoumu_role_kinou_seigyo_cd")) &&
					EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_KBN.YUUKOU.equals(roleKbn.get(i).get("gyoumu_role_kinou_seigyo_kbn"))) &&
// "1".equals(accessFlg.get("kaishasettei_riyoukanou_flg"))) {
					"1".equals(kengen.kaishasetteiRiyoukanouFlg)) {
					return true;
				}
			}
			// 上記いずれも該当しない場合
			return false;
		}
	}
}
