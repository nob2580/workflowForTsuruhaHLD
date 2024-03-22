package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 海外出張仮払抽出バッチ
 */
public class KaigaiRyohiKaribaraiChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaigaiRyohiKaribaraiChuushutsuBat.class);

	/** 抽出バッチのロジック */
	protected KaigaiRyohiKaribaraiChuushutsuLogic gyoumuLogic;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;

	@Override
	public String getBatchName() {
		return "海外出張伺い申請（仮払申請）抽出";
	}

	@Override
	public String getCountName() {
		return "伝票数";
	}

	@Override
	public int mainProc() {
		//return -1;
		try {
			connection = EteamConnection.connect();
			initialize();

			//承認済旅費仮払伝票を取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("海外出張伺い申請（仮払申請）抽出データ件数：" + denpyouList.size());

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

	/**
	 * 伝票単位に仕訳作成
	 * @param denpyou 伝票-仮払
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {

		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		BigDecimal kingaku = (BigDecimal)denpyou.get("karibarai_kingaku");
		String shainNo = (String)denpyou.get("shain_no");
		
		//分割用リスト
		List<ShiwakeData> shiwakes = new ArrayList<>();

		//仮払金額があれば仕訳とFBレコードを作る。※仮払金なし(伺い)の場合は不要
		if (kingaku != null && kingaku.doubleValue() > 0) {

			//======================================
			//仕訳作成
			//======================================
			//支払日 仮払金/現預金 仮払金額
			ShiwakeData shiwake = new ShiwakeData();
			shiwake.setMain(comChu.makeMain(denpyou, shainNo));
			shiwake.setKari(comChu.makeKarikata(denpyou), denpyouId, comChu.getShainCdRenkeiFlg(denpyou) ? shainNo : null);
			shiwake.setKashi(comChu.makeKashikata(denpyou, denpyou.get("shiharaihouhou").equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2), null, null, null);

			ShiwakeDataCom sc = shiwake.getCom();
			sc.setDYMD((Date)denpyou.get("shiharaibi"));
			sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
			sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, denpyou, null, "0")));
			sc.setTNO((String)denpyou.get("tekiyou_cd"));
			sc.setVALU((BigDecimal)denpyou.get("karibarai_kingaku"));
			shiwakes.add(shiwake);
			
			//複合の場合分割
			if(!EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A012).equals("0")){
				shiwakes = common.mergeShiwake(shiwakes,false,false);
			}
			if (onMemory)
			{
				return shiwakes;
			}
			swkLogic.insert(shiwakes);

			//======================================
			//FBデータを作成
			//======================================
			boolean fbDataMade = false;
			
			if (EteamNaibuCodeSetting.SHIHARAI_HOUHOU.FURIKOMI.equals(denpyou.get("shiharaihouhou"))) {
				// 通常起票ならば起票者口座情報、代理起票ならば使用者口座情報を取得
				GMap shainKouza = common.findKihyouShainKouza((String)denpyou.get("user_id"));
				// FBデータを作成
				common.makeFBData(denpyouId, (Date)denpyou.get("shiharaibi"), kingaku, (String)denpyou.get("user_id"), shainKouza);
				fbDataMade = true;
			}

			
			//======================================
			//後処理
			//======================================
			// 承認状況登録処理
			common.insertSyouninJoukyou(denpyouId, fbDataMade);
		}
		if (onMemory)
		{
			return shiwakes;
		}

		//済※仕訳、FBレコードを作っていなくてもフラグだけは立てる
		common.updateChuushutsuZumi(denpyouId);
		return shiwakes;
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A012;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(KaigaiRyohiKaribaraiChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
	}
}
