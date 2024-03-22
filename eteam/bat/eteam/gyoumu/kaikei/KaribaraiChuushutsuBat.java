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
 * 仮払抽出バッチ
 * @author tsukamoto
 *
 */
public class KaribaraiChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KaribaraiChuushutsuBat.class);

	/** 抽出バッチのロジック */
	protected KaribaraiChuushutsuLogic gyoumuLogic;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;

	@Override
	public String getBatchName() {
		return "経費伺い申請（仮払申請）抽出";
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

			//承認済仮払伝票の取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("経費伺い申請（仮払申請）データ件数：" + denpyouList.size());

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
		
		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser((String)denpyou.get("denpyou_id"));
		String shainNo = (String)kihyouUser.get("shain_no");

		//分割用リスト
		List<ShiwakeData> shiwakes = new ArrayList<>();

		//仮払金額があれば ※仮払金なし(伺い)の場合は不要
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
			sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.KARIBARAI_SHINSEI, denpyou, null, "0")));
			sc.setTNO((String)denpyou.get("tekiyou_cd"));
			sc.setVALU((BigDecimal)denpyou.get("karibarai_kingaku"));
			shiwakes.add(shiwake);

			//複合の場合分割
			if(!EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A002).equals("0")){
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
			if (SHIHARAI_HOUHOU.FURIKOMI.equals(denpyou.get("shiharaihouhou")) == true) {
				//起票者口座情報
				GMap shainKouza = common.findKihyouShainKouza((String)kihyouUser.get("user_id")); 

				// FBデータを作成
				common.makeFBData((String)denpyou.get("denpyou_id"), (Date)denpyou.get("shiharaibi"), kingaku, (String)kihyouUser.get("user_id"), shainKouza);
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
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A002;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		gyoumuLogic = EteamContainer.getComponent(KaribaraiChuushutsuLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
	}
}