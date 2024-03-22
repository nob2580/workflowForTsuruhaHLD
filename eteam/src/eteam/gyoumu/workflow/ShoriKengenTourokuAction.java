package eteam.gyoumu.workflow;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 処理権限登録画面Action
 */
@Getter @Setter @ToString
public class ShoriKengenTourokuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 承認処理権限番号 */
	protected String[] shouninShoriKengenNo;
	/** 承認処理権限名 */
	protected String[] shouninShoriKengenName;
	/** 基本モデル */
	protected String[] kihonModelCd;
	/** 承認必須 */
	protected String[] shouninHissuFlg;
	/** 承認権 */
	protected String[] shouninKenFlg;
	/** 変更 */
	protected String[] henkouFlg;
	/** 説明 */
	protected String[] setsumei;
	/** 承認文言 */
	protected String[] shouninMongon;
	/** 凡例表示 */
	protected String[] hanreiHyoujiCd;
	
//＜画面入力以外＞
	/** 処理権限リスト */
	protected List<GMap> shoriKengenList;
	/** 基本モデルリスト */
	protected List<GMap> kihonModeList;
	/** 基本モデルドメイン */
	String[] kihonModeDomain;
	/** 凡例表示リスト */
	protected List<GMap> hanreiHyoujiList;
	/** 凡例表示ドメイン */
	String[] hanreiHyoujiDomain;
	/** ログインユーザー情報 */
	protected User loginUserInfo;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		for (int i = 0; i < shouninShoriKengenNo.length; i++) {
			checkNumber(shouninShoriKengenNo[i], 1, 999999999, "承認処理権限No" + (i+1) + "行目", false);
			checkString(shouninShoriKengenName[i], 1, 6, "承認処理権限名" + (i+1) + "行目", false);
			checkDomain(kihonModelCd[i], kihonModeDomain, "基本モデル" + (i+1) + "行目", false);
			checkDomain(shouninHissuFlg[i], EteamConst.Domain.FLG, "承認必須フラグ" + (i+1) + "行目", false);
			checkDomain(shouninKenFlg[i], EteamConst.Domain.FLG, "承認権フラグ" + (i+1) + "行目", false);
			checkDomain(henkouFlg[i], EteamConst.Domain.FLG, "申請書変更フラグ" + (i+1) + "行目",false);
			checkString(setsumei[i], 1, 100, "説明" + (i+1) + "行目", false);
			checkString(shouninMongon[i], 1, 6, "承認文言" + (i+1) + "行目", false);
			checkDomain(hanreiHyoujiCd[i], hanreiHyoujiDomain, "凡例表示" + (i+1) + "行目", false);
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		for (int i = 0; i < shouninShoriKengenNo.length; i++) {
			hissuCheckCommon(new String[][]{
				{shouninShoriKengenName[i], "承認処理権限名：" + (i+1) + "行目", "1"},
				{kihonModelCd[i], "基本モデル：" + (i+1) + "行目", "1"},
				{shouninHissuFlg[i], "承認必須フラグ：" + (i+1) + "行目", "1"},
				{shouninKenFlg[i], "承認権フラグ：" + (i+1) + "行目", "1"},
				{henkouFlg[i], "申請書変更フラグ：" + (i+1) + "行目", "1"},
				{setsumei[i], "説明：" + (i+1) + "行目", "1"},
				{shouninMongon[i], "承認文言：" + (i+1) + "行目", "1"},
				{hanreiHyoujiCd[i], "凡例表示：" + (i+1) + "行目", "1"},
			}, eventNum);
		}
	};

//＜イベント＞
	/**
	 * E1:処理権限初期表示処理
	 * @return 成功：success, 失敗:error
	 */
	public String init() {

		// セッションからログインユーザー情報を取得
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()){
			ShoriKengenTourokuLogic bsril = EteamContainer.getComponent(ShoriKengenTourokuLogic.class, connection);
			SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			kihonModeList = sysLogic.loadNaibuCdSetting("kihon_model_cd");
			kihonModeDomain = EteamCommon.mapList2Arr(kihonModeList, "naibu_cd");
			hanreiHyoujiList = sysLogic.loadNaibuCdSetting("hanrei_hyouji_cd");
			hanreiHyoujiDomain = EteamCommon.mapList2Arr(hanreiHyoujiList, "naibu_cd");
			
			// 処理権限リストを取得
			shoriKengenList = bsril.getShoriKengenList();
			
			if (shoriKengenList.isEmpty()) {
				makeDefaultList();
			}
			
			// 6.戻り値
			return "success";
		}
	}
	
	/**
	 * E2:処理権限登録処理
	 * @return 成功：success, 失敗:error
	 */
	public String touroku() {
		// セッションからログインユーザー情報を取得
		loginUserInfo = getUser();
		
		try(EteamConnection connection = EteamConnection.connect()){
			ShoriKengenTourokuLogic bsril = EteamContainer.getComponent(ShoriKengenTourokuLogic.class, connection);
			SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			
			kihonModeList = sysLogic.loadNaibuCdSetting("kihon_model_cd");
			kihonModeDomain = EteamCommon.mapList2Arr(kihonModeList, "naibu_cd");
			hanreiHyoujiList = sysLogic.loadNaibuCdSetting("hanrei_hyouji_cd");
			hanreiHyoujiDomain = EteamCommon.mapList2Arr(hanreiHyoujiList, "naibu_cd");
			
			formatCheck();
			hissuCheck(1);
			
			if (!errorList.isEmpty()) {
				makeList();
				return "error";
			}
			
			// データ存在チェック
			List<GMap> prevShoriList = bsril.getShoriKengenList();
			
			for (int i = 0; i<prevShoriList.size(); i++) {
				prevShoriList.get(i).put("delete", false);
				for (int j = 0; j < shouninShoriKengenNo.length; j++) {
					if (prevShoriList.get(i).getString("shounin_shori_kengen_no").equals(shouninShoriKengenNo[j])) {
						prevShoriList.get(i).put("delete", true);
						break;
					}
				}
				if (prevShoriList.get(i).get("delete").equals(false)) {
					//削除前の確認処理追加
					boolean useShounin = bsril.loadBumonSuishouRoute(prevShoriList.get(i).getString("shounin_shori_kengen_no"));
					boolean useGougi = bsril.loadGougiPattern(prevShoriList.get(i).getString("shounin_shori_kengen_no"));
					boolean useShouninRouteKo = bsril.loadShouninRouteGougiKo(prevShoriList.get(i).getString("shounin_shori_kengen_no"));
					boolean useShouninRoute = bsril.loadShouninRoute(prevShoriList.get(i).getString("shounin_shori_kengen_no"));
					if (useShounin) {
						errorList.add(prevShoriList.get(i).get("shounin_shori_kengen_name") + "：部門推奨ルートに存在するため、削除できません。");
					}
					if (useGougi) {
						errorList.add(prevShoriList.get(i).get("shounin_shori_kengen_name") + "：合議部署一覧に存在するため、削除できません。");
					}
					if (useShouninRouteKo) {
						errorList.add(prevShoriList.get(i).get("shounin_shori_kengen_name") + "：未完了の承認ルート（合議）に存在するため、削除できません。");
					}
					if (useShouninRoute) {
						errorList.add(prevShoriList.get(i).get("shounin_shori_kengen_name") + "：未完了の承認ルートに存在するため、削除できません。");
					}
				}
			}
			
			if (!errorList.isEmpty()) {
				shoriKengenList = prevShoriList;
				return "error";
			}
			
			// 閲覧者のとき承認権・申請書変更が1のときエラー、後伺いのとき承認処理必須が1以外・承認権が1のときエラー
			for (int i = 0; i < kihonModelCd.length; i++) {
				switch (kihonModelCd[i]) {
					case EteamNaibuCodeSetting.KIHON_MODEL_CD.SHOUNI:
						if (!shouninHissuFlg[i].equals("1") && shouninKenFlg[i].equals("1")) {
							errorList.add((i+1) + "行目：承認必須にチェックがないとき、承認権にチェックは出来ません。");
						}
						if (!shouninHissuFlg[i].equals("1") && henkouFlg[i].equals("1")) {
							errorList.add((i+1) + "行目：承認必須にチェックがないとき、変更にチェックは出来ません。");
						}
						break;
					case EteamNaibuCodeSetting.KIHON_MODEL_CD.ETSURAN:
						if (shouninKenFlg[i].equals("1")) {
							errorList.add((i+1) + "行目：承認権にチェックは出来ません。");
						}
						if (henkouFlg[i].equals("1")) {
							errorList.add((i+1) + "行目：変更にチェックは出来ません。");
						}
						break;
					case EteamNaibuCodeSetting.KIHON_MODEL_CD.ATOUKAGAI:
						if (!shouninHissuFlg[i].equals("1")) {
							errorList.add((i+1) + "行目：承認必須にチェックをしてください。");
						}
						if (shouninKenFlg[i].equals("1")) {
							errorList.add((i+1) + "行目：承認権にチェックは出来ません。");
						}
						break;
				}
			}
			
			if (!errorList.isEmpty()) {
				makeList();
				return "error";
			}
			
			List<Integer> list = new ArrayList<Integer>();
			
			// lengthが1で承認処理権限名がない時、空と判定
			if (!(shouninShoriKengenNo.length == 1 && shouninShoriKengenName[0].equals(""))) {
				for (int i = 0; i < shouninShoriKengenNo.length; i++) {
					if (shouninShoriKengenNo[i].equals("")) {
						// 承認処理権限Noがない時、新規登録
						int ret = bsril.insertShoriKengen(
										shouninShoriKengenName[i], 
										kihonModelCd[i], 
										shouninHissuFlg[i].equals("") ? "0" : shouninHissuFlg[i], 
										shouninKenFlg[i].equals("") ? "0" : shouninKenFlg[i], 
										henkouFlg[i].equals("") ? "0" : henkouFlg[i], 
										setsumei[i], 
										shouninMongon[i], 
										hanreiHyoujiCd[i], 
										i+1,
										loginUserInfo.getTourokuOrKoushinUserId());
						list.add(ret);
					} else {
						// 承認処理権限Noがある時、アップデート
						int ret = bsril.updateShoriKengen(
										toInteger(shouninShoriKengenNo[i]), 
										shouninShoriKengenName[i], 
										kihonModelCd[i], 
										shouninHissuFlg[i].equals("") ? "0" : shouninHissuFlg[i], 
										shouninKenFlg[i].equals("") ? "0" : shouninKenFlg[i], 
										henkouFlg[i].equals("") ? "0" : henkouFlg[i], 
										setsumei[i], 
										shouninMongon[i], 
										hanreiHyoujiCd[i], 
										i+1,
										loginUserInfo.getTourokuOrKoushinUserId());
						list.add(ret);
					}
				}
			}
			
			// 削除処理
			bsril.deleteShoriKengen(list);
			
			connection.commit();
			
			// 処理権限リストを取得
			shoriKengenList = bsril.getShoriKengenList();
			
			if (shoriKengenList.isEmpty()) {
				makeDefaultList();
			}
			
			// 6.戻り値
			return "success";
		}
	}
	
	/**
	 * 処理権限レコードがない時、空の処理権限表示用に項目作成する。
	 */
	protected void makeDefaultList() {
		shoriKengenList = new ArrayList<GMap>();
		GMap map = new GMap();
		
		//表示項目
		shouninShoriKengenNo = new String[1]; shouninShoriKengenNo[0] = "";
		shouninShoriKengenName = new String[1]; shouninShoriKengenName[0] = "";
		kihonModelCd = new String[1]; kihonModelCd[0] = "";
		shouninHissuFlg = new String[1]; shouninHissuFlg[0] = "";
		shouninKenFlg = new String[1]; shouninKenFlg[0] = "";
		henkouFlg = new String[1]; henkouFlg[0] = "";
		setsumei = new String[1]; setsumei[0] = "";
		shouninMongon = new String[1]; shouninMongon[0] = "";
		hanreiHyoujiCd = new String[1]; hanreiHyoujiCd[0] = "";
		
		map.put("shounin_shori_kengen_no", shouninShoriKengenNo[0]);
		map.put("shounin_shori_kengen_name", shouninShoriKengenName[0]);
		map.put("kihon_model_cd", kihonModelCd[0]);
		map.put("shounin_hissu_flg", shouninHissuFlg[0]);
		map.put("shounin_ken_flg", shouninKenFlg[0]);
		map.put("henkou_flg", henkouFlg[0]);
		map.put("setsumei", setsumei[0]);
		map.put("shounin_mongon", shouninMongon[0]);
		map.put("hanrei_hyouji_cd", hanreiHyoujiCd[0]);
		
		shoriKengenList.add(map);
		
	}
	
	/**
	 * レコード再登録
	 */
	protected void makeList() {
		shoriKengenList = new ArrayList<GMap>();
		for (int i = 0; i < shouninShoriKengenNo.length; i++) {
			GMap map = new GMap();
			map.put("shounin_shori_kengen_no", shouninShoriKengenNo[i]);
			map.put("shounin_shori_kengen_name", shouninShoriKengenName[i]);
			map.put("kihon_model_cd", kihonModelCd[i]);
			map.put("shounin_hissu_flg", shouninHissuFlg[i]);
			map.put("shounin_ken_flg", shouninKenFlg[i]);
			map.put("henkou_flg", henkouFlg[i]);
			map.put("setsumei", setsumei[i]);
			map.put("shounin_mongon", shouninMongon[i]);
			map.put("hanrei_hyouji_cd", hanreiHyoujiCd[i]);
			shoriKengenList.add(map);
		}
	}
}
