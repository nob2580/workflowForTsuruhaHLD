package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 支払依頼抽出ロジック
 */
public class ShiharaiIraiChuushutsuLogic extends EteamAbstractLogic  {

	/** 会計共通ロジック */
	protected KaikeiCommonLogic kaikeiCommonLg;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 仕訳ロジック */
	protected ShiwakeLogic shiwakeLg;
	/** システムカテゴリSELECT */
	protected SystemKanriCategoryLogic systemLg;
	/** 会計カテゴリSELECT */
	protected KaikeiCategoryLogic kaikeiLg;
	/** マスターロジック */
	protected DaishoMasterCategoryLogic dmLogic;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		kaikeiCommonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		shiwakeLg = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		systemLg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		kaikeiLg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		dmLogic = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);
	}

	/**
	 * 抽出対象の伝票を取得
	 * @param denpyouId 伝票ID
	 * @return 抽出対象の伝票
	 */
	public GMap findDenpyou(String denpyouId) {
		String sql =
				"SELECT * "
			+ "FROM denpyou d "
			+ "INNER JOIN shiharai_irai k ON "
			+ "  d.denpyou_id = k.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 抽出対象の伝票を取得
	 * @return 仕訳対象のデータ
	 */
	public List<GMap> loadDenpyou() {
		String sql =
				"SELECT * "
			+ "FROM denpyou d "
			+ "INNER JOIN shiharai_irai k ON "
			+ "  d.denpyou_id = k.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? "
			+ "  AND d.denpyou_joutai = ? "
			+ "  AND d.chuushutsu_zumi_flg = '0' "
// +	"  AND d.denpyou_id='180823-A013-0000002' " //UT終わったらけす
// +	"  AND k.hassei_shubetsu <> '購買' " //daishoのみ？
			+ "ORDER BY "
			+ "  d.denpyou_id ASC";
		return connection.load(sql, DENPYOU_KBN.SIHARAIIRAI, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}

	/**
	 * 明細金額を支払分と控除分に分ける
	 * @param meisaiList 明細
	 * @param manekinKingaku マネキン控除額
	 * @return 分けた後リスト
	 */
	public List<GMap> calcHaraiManekin(List<GMap> meisaiList, BigDecimal manekinKingaku) {
		boolean tanitsu = EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A013).equals("0");
		List<GMap> orgList = meisaiList;
		meisaiList = new ArrayList<>();
		for(GMap org : orgList) meisaiList.add(org);

		double manezan = manekinKingaku == null ? 0 : manekinKingaku.doubleValue();
		for(GMap meisai : meisaiList){
			boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
			double meisaiKingaku = meisai.get("shiharai_kingaku") == null ? 0 : ((BigDecimal)meisai.get("shiharai_kingaku")).doubleValue();
			double meisaiKingakuZeinuki = meisai.get("zeinuki_kingaku") == null ? 0 : ((BigDecimal)meisai.get("zeinuki_kingaku")).doubleValue();
			if(meisaiKingaku < 0){
				meisai.put("manekin2", new BigDecimal(0));
				meisai.put("mibarai2", new BigDecimal((tanitsu && isZeinuki) ? meisaiKingakuZeinuki : meisaiKingaku));
				//税抜の単一 か それ以外
			}else{
				//TODO 経費立替精算のような、税込＞控除＞税抜　の場合の対応が必要
				if(manezan >= meisaiKingaku){
					meisai.put("manekin2", new BigDecimal(meisaiKingaku));
					meisai.put("mibarai2", new BigDecimal(0));
					manezan -= meisaiKingaku;
					if(isZeinuki && manezan == meisaiKingaku) {
						meisai.put("manekin2", new BigDecimal(meisaiKingakuZeinuki));
					}
				}else if(manezan > 0){
					meisai.put("manekin2", new BigDecimal(manezan));
					meisai.put("mibarai2", new BigDecimal(meisaiKingaku - manezan));
					double mibarai2;
					if(tanitsu && isZeinuki) {
						//税抜の単一
						if(manezan == meisaiKingakuZeinuki) {
							meisai.put("manekin2", new BigDecimal(meisaiKingakuZeinuki));
							mibarai2 = 0;
						}else {
							mibarai2 = meisaiKingakuZeinuki - manezan;
						}
					}else {
						//税込の単一 と 複合すべて
						mibarai2 = meisaiKingaku - manezan;
					}
					meisai.put("mibarai2", new BigDecimal(mibarai2));
					double zeimanekin = manezan - meisaiKingakuZeinuki;
					double shouhizeigaku = meisai.get("shouhizeigaku") == null ? 0 : ((BigDecimal)meisai.get("shouhizeigaku")).doubleValue();
					meisai.put("zeimanekin", new BigDecimal(zeimanekin));
					meisai.put("zeimiabarai", new BigDecimal(shouhizeigaku - zeimanekin));
					manezan = 0;
				}else{
					meisai.put("manekin2", new BigDecimal(0));
					meisai.put("mibarai2", new BigDecimal((isZeinuki && tanitsu) ? meisaiKingakuZeinuki : meisaiKingaku));
				}
			}
		}
		return meisaiList;
	}

	/**
	 * 未払金を支払分と相手負担分に分ける
	 * @param meisaiList 明細
	 * @param tesuuryouB 相手先負担の手数料
	 * @return 分けた後リスト
	 */
	public List<GMap> calcTesuuryou(List<GMap> meisaiList, BigDecimal tesuuryouB) {
		List<GMap> orgList = meisaiList;
		meisaiList = new ArrayList<>();
		boolean tanitsu = EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A013).equals("0");
		if(tesuuryouB == null){
			for(GMap meisai : orgList){
				boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
				double mibaraiKingaku = meisai.get("mibarai2") == null ? 0 : ((BigDecimal)meisai.get("mibarai2")).doubleValue();
				if(mibaraiKingaku >= 0 && isZeinuki && tanitsu) {
					//マイナス金額の明細じゃないとき
					if(mibaraiKingaku != 0) {
						//税抜金額＞控除金額
						mibaraiKingaku += meisai.get("shouhizeigaku") == null ? 0 : ((BigDecimal)meisai.get("shouhizeigaku")).doubleValue();
					}else {
						//控除金額＞＝税抜金額
						mibaraiKingaku += meisai.get("zeimiabarai") == null ? 0 : ((BigDecimal)meisai.get("zeimiabarai")).doubleValue();
					}
				}
				meisai.put("tesuuryou2", new BigDecimal(0));
				meisai.put("yokin2", new BigDecimal(mibaraiKingaku));
				meisaiList.add(meisai);
			}
			
		}else{
			double tesuuryou = tesuuryouB.doubleValue();
			for(int i = orgList.size() - 1; i >= 0; i--){
				GMap meisai = orgList.get(i);
				boolean isZeinuki = ("002".equals(meisai.get("kari_kazei_kbn")) || "013".equals(meisai.get("kari_kazei_kbn")) ||"014".equals(meisai.get("kari_kazei_kbn")));
				double mibaraiKingaku = meisai.get("mibarai2") == null ? 0 : ((BigDecimal)meisai.get("mibarai2")).doubleValue();
				if(mibaraiKingaku >= 0 && isZeinuki && tanitsu) {
					if(mibaraiKingaku != 0) {
						mibaraiKingaku += meisai.get("shouhizeigaku") == null ? 0 : ((BigDecimal)meisai.get("shouhizeigaku")).doubleValue();
					}else {
						mibaraiKingaku += meisai.get("zeimiabarai") == null ? 0 : ((BigDecimal)meisai.get("zeimiabarai")).doubleValue();
					}
				}
				if(mibaraiKingaku > 0){
					if(tesuuryou >= mibaraiKingaku){
						meisai.put("tesuuryou2", new BigDecimal(mibaraiKingaku));
						meisai.put("yokin2", new BigDecimal(0));
						tesuuryou -= mibaraiKingaku;
					}else if(tesuuryou > 0){
						meisai.put("tesuuryou2", new BigDecimal(tesuuryou));
						meisai.put("yokin2", new BigDecimal(mibaraiKingaku - tesuuryou));
						tesuuryou = 0;
					}else{
						meisai.put("tesuuryou2", new BigDecimal(0));
						meisai.put("yokin2", new BigDecimal(mibaraiKingaku));
					}
					meisaiList.add(meisai);
				}
			}
		}
		return meisaiList;
	}

	/**
	 * 貸を同じくする仕訳を諸口マージする
	 * @param shiwakeList 仕訳リスト
	 */
	public void mergeKashi(List<ShiwakeData> shiwakeList) {
		//貸方を同じくする仕訳をグループ化して、
		//グループ毎の先頭に金額合計して
		//グループ毎の先頭以外は消す
		for(int i = 0; i < shiwakeList.size(); i++){
			ShiwakeData s = shiwakeList.get(i);
			for(int j = shiwakeList.size()-1; i < j; j--){
				ShiwakeData s2 = shiwakeList.get(j);
				if(s.getS().equals(s2.getS())){
					s.getCom().setVALU(s.getCom().getVALU().add(s2.getCom().getVALU()));
					shiwakeList.remove(j);
				}
			}
		}
	}

	/**
	 * 貸を同じくする仕訳を諸口マージする
	 * @param shiwakeList 仕訳リスト
	 */
	public void mergeKari(List<ShiwakeData> shiwakeList) {
		//借方を同じくする仕訳をグループ化して、
		//グループ毎の先頭に金額合計して
		//グループ毎の先頭以外は消す
		for(int i = 0; i < shiwakeList.size(); i++){
			ShiwakeData s = shiwakeList.get(i);
			for(int j = shiwakeList.size()-1; i < j; j--){
				ShiwakeData s2 = shiwakeList.get(j);
				if(s.getR().equals(s2.getR())){
					s.getCom().setVALU(s.getCom().getVALU().add(s2.getCom().getVALU()));
					shiwakeList.remove(j);
				}
			}
		}
	}
	
	/**
	 * 手数料仕訳にセットする負担部門コードを取得する
	 * @param meisaiList 明細データ
	 * @return 負担部門コード
	 */
	public String getTesuuryouFutanBumonCd(List<GMap> meisaiList){
		GMap meisaiTop = meisaiList.get(0);
		String ret = meisaiTop.get("kari_futan_bumon_cd"); 
		BigDecimal maxKingaku = meisaiTop.get("shiharai_kingaku");
		
		//先頭の明細の負担部門を採用する設定ならここで終わり。
		if (EteamSettingInfo.getSettingInfo(Key.ICHIGEN_TESUURYOU_BUMON_CD_NINI_BUMON_HANTEI).equals("2"))
		{
			return ret;
		}
		
		//金額最大の明細の負担部門を採用する設定なら先頭から支払金額最大の明細を探す。
		//なお、金額最大の明細が複数あった場合は先勝ち。
		for(int i = 1 ; i < meisaiList.size() ; i++){
			GMap meisai = meisaiList.get(i);
			if(maxKingaku.compareTo(meisai.get("shiharai_kingaku")) < 0){
				maxKingaku = meisai.get("shiharai_kingaku");
				ret = meisai.get("kari_futan_bumon_cd");
			}
		}
		return ret;
	}

}
