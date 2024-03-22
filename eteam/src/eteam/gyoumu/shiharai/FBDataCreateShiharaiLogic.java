package eteam.gyoumu.shiharai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.TESUURYOU;
import eteam.gyoumu.kaikei.DaishoMasterCategoryLogic;
import eteam.gyoumu.kaikei.FBDataCreateLogic;
import eteam.gyoumu.kaikei.common.FBData;

/**
 * FBデータ作成ロジック
 * @author takahashi_ryousuke
 *
 */
public class FBDataCreateShiharaiLogic extends FBDataCreateLogic  {

	/** 作成済含める */
	public boolean all;

	@Override
	public List<GMap> loadHeadRecord(Date kojinSeisanShiraibi) {
		java.sql.Date d = new java.sql.Date(kojinSeisanShiraibi.getTime());

		//邪道だが個人精算用の構造がヘッダーレコードの有無で振込データ有無を判定しているので
		FBData p = new FBData();
		p.setFurikomiDate(d);
		if (loadDataRecord(p).size() == 0) return new ArrayList<GMap>();

		//振込元口座はマスターで登録されているのでそれをヘッダーレコードにする
		final String sql =
				  " SELECT "
				+ "   moto_kinyuukikan_cd, "
				+ "   moto_kinyuukikan_shiten_cd, "
				+ "   moto_yokinshubetsu AS moto_yokin_shumoku_cd, "
				+ "   moto_kouza_bangou, "
				+ "   shubetsu_cd, "
				+ "   cd_kbn, "
				+ "   kaisha_cd,"
				+ "   kaisha_name_hankana,"
				+ "   moto_kinyuukikan_name_hankana,"
				+ "   moto_kinyuukikan_shiten_name_hankana "
				+ "FROM moto_kouza_shiharaiirai "
				+ "ORDER BY "
				+ "  moto_kinyuukikan_cd, "
				+ "  moto_kinyuukikan_shiten_cd, "
				+ "  moto_yokinshubetsu, "
				+ "  moto_kouza_bangou ";
		List<GMap> list = connection.load(sql);
		for (GMap record : list) {
			record.put("furikomi_date", d);
		}
		return list;
	}

	@Override
	public List<GMap> loadDataRecord(FBData fbData) {
		DaishoMasterCategoryLogic dmLogic = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);
		
		String sql = 
				  " SELECT "
				+ "   s.furikomi_ginkou_cd        AS saki_kinyuukikan_cd, "
			    + "   s.furikomi_ginkou_shiten_cd AS saki_kinyuukikan_shiten_cd, "
				+ "   s.yokin_shubetsu            AS saki_yokin_shumoku_cd, "
				+ "   s.kouza_bangou              AS saki_kouza_bangou, "
				+ "   s.sashihiki_shiharai        AS kingaku, "
				+ "   k.kinyuukikan_name_hankana  AS saki_kinyuukikan_name_hankana, "
				+ "   k.shiten_name_hankana       AS saki_kinyuukikan_shiten_name_hankana, "
				+ "   s.kouza_meiginin            AS saki_kouza_meigi_kana, "
				+ "   ''                          AS shinki_cd, "
				+ "   s.edi                       AS kokyaku_cd1, "
				+ "   m.furikomi_kbn              AS furikomi_kbn, "
				+ "   s.tesuuryou, "
				+ "   s.denpyou_id "
				+ " FROM shiharai_irai s "
				+ " INNER JOIN denpyou d ON "
				+ "   s.denpyou_id = d.denpyou_id "
				+ " LEFT OUTER JOIN open21_kinyuukikan k ON "
				+ "   (k.kinyuukikan_cd, k.kinyuukikan_shiten_cd) = (s.furikomi_ginkou_cd, furikomi_ginkou_shiten_cd) "
				+ " INNER JOIN (SELECT * FROM moto_kouza_shiharaiirai LIMIT 1) m ON "
				+ "   1 = 1 "
				+ " WHERE "
				+ "   s.yoteibi = ? AND "
				+ "   s.shiharai_houhou = ? AND "
				+ "   s.shiharai_shubetsu = ? AND "
				+ "   s.torihikisaki_cd = ? AND "
				+ "   s.sashihiki_shiharai > 0 AND "
				+ "   d.denpyou_joutai = ? "
				+ "  :FB_MADE "
				+ " ORDER BY "
				+ "   s.furikomi_ginkou_cd, "
			    + "   s.furikomi_ginkou_shiten_cd, "
				+ "   s.yokin_shubetsu, "
				+ "   s.kouza_bangou ";
		if (all) {
			sql = sql.replace(":FB_MADE", "");
		} else {
			sql = sql.replace(":FB_MADE", "AND fb_made_flg = '0' ");
		}
		List<GMap> list = connection.load(sql, fbData.getFurikomiDate(), SHIHARAI_IRAI_HOUHOU.FURIKOMI, SHIHARAI_IRAI_SHUBETSU.SONOTA, setting.ichigenCd(), DENPYOU_JYOUTAI.SYOUNIN_ZUMI);

		for (GMap record : list) {

			//顧客コード１・２：申請画面のEDIを
			String edi = (String)record.get("kokyaku_cd1");
			if("0".equals(setting.zenginFurikomiKeishiki())) {
				//全銀総合振込依頼データ形式0なら固定長にする
				String kokyakuCd12 = edi + "                    ";
				if (kokyakuCd12.length() > 20) kokyakuCd12 = kokyakuCd12.substring(0, 20);
				record.put("kokyaku_cd1", kokyakuCd12);
			}else {
				//全銀総合振込依頼データ形式1ならそのまま登録
				record.put("kokyaku_cd1", edi);
			}

			//識別情報：申請画面のEDIがあるならY、なければブランク
			if (! isEmpty(edi)) record.put("ediFlg", "1");

			//先方負担なら手数料差引
			if (record.get("tesuuryou").equals(TESUURYOU.SENPOU_FUTAN)) {
				BigDecimal kingaku = (BigDecimal)record.get("kingaku");
				BigDecimal tesuuryou = dmLogic.judgeTesuuryou(kingaku, (String)record.get("saki_kinyuukikan_cd"));
				kingaku = kingaku.subtract(tesuuryou);
				record.put("kingaku", kingaku);
			}
		}
		
		return list;
	}

	@Override
	public int insertFBDataSakuseiSyouninJoukyou(Date kojinSeisanShiraibi) {
		return 0;
	}
	
	@Override
	public int updateFBStatus(Date kojinSeisanShiraibi) {
		return 0;
	}
}
