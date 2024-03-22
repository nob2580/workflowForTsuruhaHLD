package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.TSUKEKAE_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataMain;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 総合付替伝票抽出バッチ
 */
public class SougouTsukekaeDenpyouChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(SougouTsukekaeDenpyouChuushutsuBat.class);

	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	SougouTsukekaeDenpyouChuushutsuLogic gyoumuLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommonLogic;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;

	@Override
	public String getBatchName() {
		return "総合付替伝票抽出";
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
			log.info("総合付替伝票抽出データ件数：" + denpyouList.size());

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
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A008;
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
		List<GMap> meisaiList = kaikeiLogic.loadTsukekaeMeisai(denpyouId);
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser(denpyouId);
		String shainNo = (String)kihyouUser.get("shain_no");
		
		
		//======================================
		//仕訳作成
		//======================================
		List<ShiwakeData> shiwakes = new ArrayList<>();
		ShiwakeDataMain main = comChu.makeMain(denpyou, shainNo);
		for (GMap meisai : meisaiList) {
			GMap map = new GMap();
			map.putAll(denpyou);
			map.putAll(meisai);
			boolean motoZeiKomi = kaikeiCommonLogic.kazeiKbnIsZeikomiGroup((String) denpyou.get("moto_kazei_kbn"));
			boolean sakiZeiKomi = kaikeiCommonLogic.kazeiKbnIsZeikomiGroup((String) meisai.get("saki_kazei_kbn"));
//			boolean motoKamokuBunriKbn = common.judgeBunriKbn((String)denpyou.get("moto_kamoku_cd"));
//			boolean sakiKamokuBunriKbn = common.judgeBunriKbn((String)meisai.get("saki_kamoku_cd"));
			
			boolean kariKotei = TSUKEKAE_KBN.KARIKATA_KOUTEI.equals(denpyou.get("tsukekae_kbn"));
			String motoBunri = common.getBunriKbnForShiwake(denpyou,(String)map.get("moto_bunri_kbn"),(String)map.get("moto_kazei_kbn"));
			String sakiBunri = common.getBunriKbnForShiwake(denpyou,(String)map.get("saki_bunri_kbn"),(String)map.get("saki_kazei_kbn"));

			ShiwakeDataRS kari = gyoumuLogic.makeKari(map);
			ShiwakeDataRS kashi = gyoumuLogic.makeKashi(map);
			ShiwakeDataRS shokuchi= comChu.makeShokuchi();
			ShiwakeDataCom com = gyoumuLogic.makeCom(map);

			// moto.借方課税区分 =「税込」の課税区分グループ　かつ　saki.貸方課税区分 =「税込」の課税区分グループ 
			// かつ moto.借方科目コード、saki.貸方科目コードに紐づく分離区分のどちらかが0以外かつ空文字以外
			if (motoZeiKomi && sakiZeiKomi 
					&& ((!"0".equals(motoBunri) && !"".equals(motoBunri)) 
							|| (!"0".equals(sakiBunri)) && (!"".equals(sakiBunri)))) {
				// 借方／諸口
				ShiwakeData s = new ShiwakeData();
				s.setMain(main);
				s.setKari(kari, denpyouId, null);
				s.setKashi(shokuchi, null, null, null);
				com.setBUNRI(kariKotei ? motoBunri : sakiBunri);
				s.setCom(com);
				shiwakes.add(s);

				// 諸口／貸方
				s = new ShiwakeData();
				s.setMain(main);
				s.setKari(shokuchi, null, null);
				s.setKashi(kashi, null, null, null);
				com.setBUNRI(kariKotei ? sakiBunri : motoBunri);
				s.setCom(com);
				shiwakes.add(s);
			} else {
				// 借方／貸方
				ShiwakeData s = new ShiwakeData();
				s.setMain(main);
				s.setKari(kari, denpyouId, null);
				s.setKashi(kashi, null, null, null);
				com.setBUNRI("1".equals(denpyou.get("invoice_denpyou")) ? "" : "0");
				String bnr = "";
				if(!motoZeiKomi && !sakiZeiKomi) {
					bnr = "1".equals(denpyou.get("invoice_denpyou")) ? "" : "0";
					//どちらも税込グループではないので、分離区分は0（旧伝票では空白）
				}else if(motoZeiKomi) {
					bnr = motoBunri;
					//motoが税込グループだからmotoの分離区分を
				}else if(sakiZeiKomi) {
					bnr = sakiBunri;
					//sakiが税込グループだからsakiの分離区分を
				}
				com.setBUNRI(bnr);
				s.setCom(com);
				shiwakes.add(s);
			}
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
		kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(SougouTsukekaeDenpyouChuushutsuLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
	}
}