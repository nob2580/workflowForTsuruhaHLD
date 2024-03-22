package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamSettingInfo;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataMain;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;

/**
 * 振替伝票抽出バッチ
 */
public class FurikaeDenpyouChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(FurikaeDenpyouChuushutsuBat.class);

	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	FurikaeDenpyouChuushutsuLogic gyoumuLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommonLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;
	/** 科目マスタDao */
	KamokuMasterDao kamokuMasterDao;
	/** ワークフローイベント制御ロジッククラス */
	WorkflowEventControlLogic wfEventLogic;

	@Override
	public String getBatchName() {
		return "振替伝票抽出";
	}

	@Override
	public String getCountName() {
		return "伝票数";
	}

	@Override
	public int mainProc() {
		try {
			connection = EteamConnection.connect();
			initialize();

			// 承認済振替伝票を取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("振替伝票抽出データ件数：" + denpyouList.size());

			return chuushutsuShiwake(denpyouList);
		} catch (Throwable e) {
			log.error("エラー発生", e);
			return 1;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	@Override
	public List<ShiwakeData> makeShiwake(String denpyouId){
		initialize();
		
		GMap denpyou = gyoumuLogic.findDenpyou(denpyouId);
		return denpyouChuushutsu(denpyou);
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A007;
	}

	/**
	 * 伝票単位に仕訳作成
	 * @param denpyou 伝票-通勤定期
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		boolean kariZeiKomi = kaikeiCommonLogic.kazeiKbnIsZeikomiGroup((String) denpyou.get("kari_kazei_kbn"));
		boolean kashiZeiKomi = kaikeiCommonLogic.kazeiKbnIsZeikomiGroup((String) denpyou.get("kashi_kazei_kbn"));
		boolean kariKamokuBunriKbn = common.judgeBunriKbn((String)denpyou.get("kari_kamoku_cd"));
		boolean kashiKamokuBunriKbn = common.judgeBunriKbn((String)denpyou.get("kashi_kamoku_cd"));
		
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		
		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser(denpyouId);
		String shainNo = (String)kihyouUser.get("shain_no");

		//======================================
		//仕訳作成
		//======================================
		List<ShiwakeData> shiwakes = new ArrayList<>();
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);
		ShiwakeDataRS kari = gyoumuLogic.makeKari(denpyou);
		ShiwakeDataRS kashi = gyoumuLogic.makeKashi(denpyou);
		ShiwakeDataRS shokuchi= comChu.makeShokuchi();
		ShiwakeDataCom com = gyoumuLogic.makeCom(denpyou);
		
		String kariBunri = common.getBunriKbnForShiwake(denpyou,(String)denpyou.get("kari_bunri_kbn"),(String)denpyou.get("kari_kazei_kbn"));
		String kashiBunri = common.getBunriKbnForShiwake(denpyou,(String)denpyou.get("kashi_bunri_kbn"),(String)denpyou.get("kashi_kazei_kbn"));
		
		//old
		// 振替.借方課税区分 =「税込」の課税区分グループ　かつ　振替.貸方課税区分 =「税込」の課税区分グループ 
		// かつ 振替.借方科目コード、振替.貸方科目コードに紐づく分離区分のどちらかが0以外
		//new
		// （振替.借方課税区分 =「税込」の課税区分グループ　かつ　振替.貸方課税区分 =「税込」の課税区分グループ　かつ　伝票に登録された分離区分のどちらかが0以外かつ空文字以外）
		if (kariZeiKomi && kashiZeiKomi 
				&& ((!"0".equals(kariBunri) && !"".equals(kariBunri)) || (!"0".equals(kashiBunri)) && (!"".equals(kashiBunri))) ) {
			// 借方／諸口
			ShiwakeData s = new ShiwakeData();
			s.setMain(main);
			s.setKari(kari, denpyouId, null);
			s.setKashi(shokuchi, null, null, null);
			com.setBUNRI(kariBunri);
			s.setCom(com);
			shiwakes.add(s);

			// 諸口／貸方
			s = new ShiwakeData();
			s.setMain(main);
			s.setKari(shokuchi, null, null);
			s.setKashi(kashi, null, null, null);
			com.setBUNRI(kashiBunri);
			s.setCom(com);
			shiwakes.add(s);
			
		} else {
			// 借方／貸方
			
			ShiwakeData s = new ShiwakeData();
			s.setMain(main);
			s.setKari(kari, denpyouId, null);
			s.setKashi(kashi, null, null, null);
//			com.setBUNRI("1".equals(denpyou.get("invoice_denpyou")) ? "" : "0");
			String bnr = "";
			if(!kariZeiKomi && !kashiZeiKomi) {
				bnr = "1".equals(denpyou.get("invoice_denpyou")) ? "" : "0";
				//どちらも税込グループではないので、分離区分は0（旧伝票では空白）
			}else if(kariZeiKomi) {
				bnr = kariBunri;
				//借方が税込グループだから借方の分離区分を
			}else if(kashiZeiKomi) {
				bnr = kashiBunri;
				//貸方が税込グループだから貸方の分離区分を
			}
			com.setBUNRI(bnr);
			s.setCom(com);
			shiwakes.add(s);
			
		}
		
		if (onMemory)
		{
			return shiwakes;
		}
		swkLogic.insert(shiwakes);

		//======================================
		//仕訳作成
		//======================================
		// 承認状況登録処理
		common.insertSyouninJoukyou(denpyouId, false);

		//済
		common.updateChuushutsuZumi(denpyouId);
		return shiwakes;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(FurikaeDenpyouChuushutsuLogic.class, connection);
		kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}
}
