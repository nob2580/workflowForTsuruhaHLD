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
import eteam.common.EteamSettingInfo;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 通勤定期抽出バッチ
 */
public class TsuukinTeikiChuushutsuBat extends EteamAbstractKaikeiBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(TsuukinTeikiChuushutsuBat.class);

	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic common;
	/** 抽出ロジック */
	TsuukinTeikiChuushutsuLogic gyoumuLogic;
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

	@Override
	public String getBatchName() {
		return "通勤定期申請抽出";
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

			//通勤定期申請から仕訳を抽出する
			if (! "1".equals(setting.tsuukinteikiShiwakeSakuseiUmu())) {
				log.info("通勤定期　仕訳作成有無の値が1でないので仕訳は作成しません。");
			}
			
			//承認済通勤定期伝票の取得
			List<GMap> denpyouList = gyoumuLogic.loadDenpyou();
			log.info("通勤定期申請抽出データ件数：" + denpyouList.size());

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
		return _denpyouChuushutsu(denpyou);
	}
	
	@Override
	protected String getIdLevelRenkeiKey() {
		return EteamSettingInfo.Key.DENPYOU_SAKUSEI_TANI_A006;
	}

	@Override
	protected List<ShiwakeData> denpyouChuushutsu(GMap denpyou) {
		List<ShiwakeData> shiwakes = null;
		
		if ("1".equals(setting.tsuukinteikiShiwakeSakuseiUmu())) {
			shiwakes = _denpyouChuushutsu(denpyou);
		}

		//済：通勤定期申請から仕訳を作らない設定だとしても、作成済にする。今後設定を変更した直後に大量作成されないように。
		common.updateChuushutsuZumi((String)denpyou.get("denpyou_id"));
		return shiwakes;
	}

	/**
	 * 伝票単位に仕訳作成
	 * @param denpyou 伝票-通勤定期
	 * @return 仕訳リスト
	 */
	protected List<ShiwakeData> _denpyouChuushutsu(GMap denpyou) {

		//======================================
		//初期計算
		//======================================
		String denpyouId = (String)denpyou.get("denpyou_id");
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		
		// 起票者情報
		GMap kihyouUser = common.findKihyouUser(denpyouId);
		String shainNo = (String)kihyouUser.get("shain_no");

		//分割用リスト
		List<ShiwakeData> shiwakes = new ArrayList<>();

		//======================================
		//仕訳作成
		//======================================
		
		//課税区分取得
		//課税区分：税抜なら、税抜で明細作って最後に消費税額合計した行作成する
		boolean isZeinuki = ("002".equals(denpyou.get("kari_kazei_kbn")) || "013".equals(denpyou.get("kari_kazei_kbn")) ||"014".equals(denpyou.get("kari_kazei_kbn")));
		String kingakuStr = "kingaku";
		if (isZeinuki)
		{
			kingakuStr = "zeinuki_kingaku";
		}
		
		//支払日 仮払金/現預金 仮払金額
		ShiwakeData shiwake = new ShiwakeData();
		shiwake.setMain(comChu.makeMain(denpyou, shainNo));
		shiwake.setKari(comChu.makeKarikata(denpyou), denpyouId, comChu.getShainCdRenkeiFlg(denpyou) ? shainNo : null);
		shiwake.setKashi(comChu.makeKashikata(denpyou, 1), null, null, null);

		ShiwakeDataCom sc = shiwake.getCom();
		sc.setDYMD((Date)denpyou.get("shiharaibi"));
		sc.setDCNO(common.serialNo(denpyou.get("serial_no")));
		sc.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, denpyou, null, "0")));
		sc.setTNO((String)denpyou.get("tekiyou_cd"));
		sc.setVALU((BigDecimal)denpyou.get(kingakuStr));
		sc.setBUNRI(common.getBunriKbnForShiwake(denpyou));
		shiwakes.add(shiwake);
		
		if(isZeinuki) {
			ShiwakeData shiwakeZei = new ShiwakeData();
			shiwakeZei.setMain(comChu.makeMain(denpyou, shainNo));
			shiwakeZei.setKari(comChu.makeKarikata(denpyou), denpyouId, comChu.getShainCdRenkeiFlg(denpyou) ? shainNo : null);
			shiwakeZei.setKashi(comChu.makeKashikata(denpyou, 1), null, null, null);
			
			ShiwakeDataRS r = shiwakeZei.getR();
			r.setKMK(common.getZeiKamokuGaibuCd(denpyou, dto));
			
			r.setZKB(KAZEI_KBN_SHOUHIZEI_SHIWAKE);
			r.setSRE("");
			//消費税仕訳　消費税設定により空にする
			common.setZeitaishouKoumoku(r, dto);
			
			//消費税対象科目
			shiwakeZei.setZKMK(shiwake.getR().getKMK());
			shiwakeZei.setZRIT(r.getRIT());
			shiwakeZei.setZKEIGEN(r.getKEIGEN());
			shiwakeZei.setZZKB(shiwake.getR().getZKB());
			shiwakeZei.setZGYO(shiwakeZei.getRGYO());
			shiwakeZei.setZSRE(r.getSRE());
			shiwakeZei.setZURIZEIKEISAN(r.getURIZEIKEISAN());
			shiwakeZei.setZMENZEIKEIKA(r.getMENZEIKEIKA());
			
			ShiwakeDataCom scZei = shiwakeZei.getCom();
			scZei.setDYMD((Date)denpyou.get("shiharaibi"));
			scZei.setDCNO(common.serialNo(denpyou.get("serial_no")));
			scZei.setTKY(common.cutTekiyou(common.shiwakeTekiyou(DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI, denpyou, null, "0")));
			scZei.setTNO((String)denpyou.get("tekiyou_cd"));
			scZei.setVALU((BigDecimal)denpyou.get("shouhizeigaku"));
//			scZei.setBUNRI(common.getBunriKbnForShiwake(denpyou));
			
			shiwakes.add(shiwakeZei);
		}

		//複合の場合分割
		if(!setting.op21MparamDenpyouKeishikiA006().equals("0")){
			shiwakes = common.mergeShiwake(shiwakes,false,true);
		}
		if (onMemory)
		{
			return shiwakes;
		}
		swkLogic.insert(shiwakes);


		//======================================
		//FBデータを作成
		//======================================
		// 起票者口座情報
		GMap shainKouza = common.findKihyouShainKouza((String)kihyouUser.get("user_id")); 

		// FBデータを作成
		common.makeFBData((String)denpyou.get("denpyou_id"), (Date)denpyou.get("shiharaibi"), (BigDecimal)denpyou.get("kingaku"), (String)kihyouUser.get("user_id"), shainKouza);

		
		//======================================
		//後処理
		//======================================
		// 承認状況登録処理
		common.insertSyouninJoukyou(denpyouId, true);
		return shiwakes;
	}

	/**
	 * 初期化
	 */
	protected void initialize() {
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		gyoumuLogic = EteamContainer.getComponent(TsuukinTeikiChuushutsuLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}
}
