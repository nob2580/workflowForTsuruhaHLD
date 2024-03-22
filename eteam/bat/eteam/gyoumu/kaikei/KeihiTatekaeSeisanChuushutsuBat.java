package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractKaikeiBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 経費立替精算抽出バッチ
 *
 */
public class KeihiTatekaeSeisanChuushutsuBat extends EteamAbstractKaikeiBat {

//＜部品＞
	/** ロガー */
	static EteamLogger log = EteamLogger.getLogger(KeihiTatekaeSeisanChuushutsuBat.class);
	
	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 経費立替ロジック */
	KeihiTatekaeSeisanChuushutsuLogic tatekaeLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;

	@Override
	public String getBatchName() {
		return "経費立替精算抽出";
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

			// 経費精算データと経費精算明細データを取得
			List<GMap> denpyouList = tatekaeLogic.loadDenpyou();
			log.info("経費立替精算抽出明細データ件数：" + denpyouList.size());
			
			return chuushutsuShiwake(denpyouList);
		} catch (Throwable e) {
			log.error("エラー発生", e);
			return(1);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	@Override
	public List<ShiwakeData> makeShiwake(String denpyouId){
		initialize();
		
		GMap denpyou = tatekaeLogic.findDenpyou(denpyouId);
		return denpyouChuushutsu(denpyou);
	}

	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A001;
	}

	/**
	 * 伝票単位に仕訳、FBデータを作る
	 * @param denpyou 伝票単位のレコード
	 */
	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		List<GMap> meisaiList = tatekaeLogic.loadMeisai(denpyouId);

		// 仮払の取得
		GMap karibaraiDenpyou = null;
		BigDecimal karibaraiKingaku = null;
		String kariBaraiDenpyouId = (String)denpyou.get("karibarai_denpyou_id");
		if (StringUtils.isNotEmpty(kariBaraiDenpyouId)) { 
			karibaraiDenpyou = common.findKaribaraiInfo(kariBaraiDenpyouId); 
			if (karibaraiDenpyou != null) {
				karibaraiKingaku = (BigDecimal) karibaraiDenpyou.get("karibarai_kingaku");
			}
		}
		
		// 差引支給金額
		BigDecimal shikyuuKingaku = (BigDecimal) denpyou.get("sashihiki_shikyuu_kingaku");
		boolean isShikyuuKingaku = (shikyuuKingaku != null && shikyuuKingaku.doubleValue() > 0);
		
		// 起票者情報の取得
		GMap kihyouUser = common.findKihyouUser((String) denpyou.get("denpyou_id"));


		//======================================
		// 明細金額を仮払分と立替分に分ける
		//======================================
		tatekaeLogic.calcKariTate(meisaiList, karibaraiKingaku, DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, denpyou);


		//======================================
		//ケースごとに仕訳作成
		//======================================
		List<ShiwakeData> shiwakes = tatekaeLogic.makeSeisanCommon(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, DENPYOU_KBN.KARIBARAI_SHINSEI, denpyou, meisaiList, karibaraiDenpyou);
		if (onMemory)
		{
			return shiwakes;
		}
		swkLogic.insert(shiwakes);


		//======================================
		//FBデータ作成
		//======================================
		// 経費精算.支払方法 =「振込」かつ 経費精算.差引支給金額 > 0 の場合、FBデータの抽出
		// FBデータを作成
		boolean fbDataMade = false;
		
		// 通常起票ならば
		if (! denpyou.get("dairiflg").equals("1")) {
			if ((SHIHARAI_HOUHOU.FURIKOMI.equals(denpyou.get("shiharaihouhou"))) && isShikyuuKingaku) {
				//起票者口座情報
				GMap shainKouza = common.findKihyouShainKouza((String)kihyouUser.get("user_id")); 
				
				// FBデータを作成
				common.makeFBData(denpyouId, (Date)denpyou.get("shiharaibi"), shikyuuKingaku, (String)kihyouUser.get("user_id"), shainKouza);
				fbDataMade = true;
			}
		// 代理起票
		} else {
			if (SHIHARAI_HOUHOU.FURIKOMI.equals(denpyou.get("shiharaihouhou"))) {
				for (GMap meisai : meisaiList) {
					//使用者口座情報
					GMap shainKouza = common.findKihyouShainKouza((String)meisai.get("user_id"));
					//法人カード利用フラグ
					boolean houjinFlag = "1".equals(meisai.get("houjin_card_riyou_flg"));
					//会社手配フラグ
					boolean kaishaTehaiFlag = "1".equals(meisai.get("kaisha_tehai_flg"));
					
					// FBデータを作成
					if ((!houjinFlag) && (!kaishaTehaiFlag)) common.makeFBData(denpyouId, (Date)denpyou.get("shiharaibi"), (BigDecimal)meisai.get("shiharai_kingaku"), (String)meisai.get("user_id"), shainKouza);
				}
				fbDataMade = true;
			}
		}


		//======================================
		//後処理
		//======================================
		// 承認状況登録処理
		common.insertSyouninJoukyou(denpyouId, fbDataMade);
		
		//済
		common.updateChuushutsuZumi(denpyouId);
		return shiwakes;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		tatekaeLogic = EteamContainer.getComponent(KeihiTatekaeSeisanChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
	}
}
