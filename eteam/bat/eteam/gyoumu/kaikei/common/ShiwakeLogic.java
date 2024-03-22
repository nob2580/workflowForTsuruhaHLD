package eteam.gyoumu.kaikei.common;

import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamSettingInfo;
import eteam.common.RegAccess;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;


/**
 * 仕訳抽出共通ロジック
 */
public class ShiwakeLogic extends EteamAbstractLogic {

	/** バッチ会計共通ロジック */
	BatchKaikeiCommonLogic cl;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		cl = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, _connection);
	}

	/**
	 * 仕訳抽出テーブルへのインサート
	 * シリアル番号、登録日時、更新日時は自動セットなので指定不要。その他の更新項目の値を指定する。
	 * @param d 仕訳抽出データ
	 */
	protected void insert(ShiwakeData d) {
		beforeInsertExt(d);
		if (Open21Env.getVersion() == Version.DE3) {
			insert_de3(d);
		} else {
			insert_sias(d);
		}
	}

	/**
	 * 仕訳抽出テーブルへのインサート
	 * シリアル番号、登録日時、更新日時は自動セットなので指定不要。その他の更新項目の値を指定する。
	 * @param dList 仕訳抽出データ
	 */
	public void insert(List<ShiwakeData> dList) {
		for(ShiwakeData d : dList){
			//軽減税率スペースreplaceAll
			d.r.KEIGEN = d.r.KEIGEN.replaceAll(" ", "").replaceAll("　", "");
			d.s.KEIGEN = d.s.KEIGEN.replaceAll(" ", "").replaceAll("　", "");
			d.ZKEIGEN = d.ZKEIGEN.replaceAll(" ", "").replaceAll("　", "");
			insert(d);
		}
	}

	/**
	 * 拡張用メソッド insert前処理
	 * @param d 仕訳
	 */
	protected void beforeInsertExt(ShiwakeData d) {}

	/**
	 * 仕訳抽出テーブル(de3)へのインサート
	 * シリアル番号、登録日時、更新日時は自動セットなので指定不要。その他の更新項目の値を指定する。
	 * @param d 仕訳抽出データ
	 */
	protected void insert_de3(ShiwakeData d) {
		final String sql = "INSERT INTO shiwake_de3(denpyou_id, shiwake_status, touroku_time, koushin_time, DYMD, SEIRI, DCNO, "
				+ "RBMN, RTOR, RKMK, REDA, RKOJ, RKOS, RPRJ, RSEG, RDM1, RDM2, RDM3, TKY, TNO, "
				+ "SBMN, STOR, SKMK, SEDA, SKOJ, SKOS, SPRJ, SSEG, SDM1, SDM2, SDM3, EXVL, VALU, "
				+ "ZKMK, ZRIT, ZKEIGEN, ZZKB, ZGYO, ZSRE, RRIT, RKEIGEN, SRIT, SKEIGEN, "
				+ "RZKB, RGYO, RSRE, SZKB, SGYO, SSRE, SYMD, SKBN, SKIZ, UYMD, UKBN, UKIZ, STEN, DKEC, KYMD, KBMN, KUSR, FUSR, FSEN, SGNO, BUNRI, RATE, GEXVL, GVALU, GSEP, "
				+ "RURIZEIKEISAN, RMENZEIKEIKA, SURIZEIKEISAN, SMENZEIKEIKA, ZURIZEIKEISAN, ZMENZEIKEIKA)"
				+ " VALUES(?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '', ?, ?, ?, ?, ?, ?)"; 
		connection.update(sql,
			//WF
			d.main.getDenpyou_id(),
			d.main.getShiwake_status(),
			//伝票情報
			d.com.getDYMD(),
			d.getSEIRI(),
			d.com.getDCNO(),
			//借方情報
			d.r.getBMN(),
			d.r.getTOR(),
			d.r.getKMK(),
			d.r.getEDA(),
			d.getRKOJ(),
			d.getRKOS(),
			d.r.getPRJ(),
			d.r.getSEG(),
			kariUfWf2De3(d, 1),
			kariUfWf2De3(d, 2),
			kariUfWf2De3(d, 3),
			d.com.getTKY(),
			d.com.getTNO(),
			//貸方情報
			d.s.getBMN(),
			d.s.getTOR(),
			d.s.getKMK(),
			d.s.getEDA(),
			d.getSKOJ(),
			d.getSKOS(),
			d.s.getPRJ(),
			d.s.getSEG(),
			kashiUfWf2De3(d, 1),
			kashiUfWf2De3(d, 2),
			kashiUfWf2De3(d, 3),
			d.getEXVL(),
			d.com.getVALU(),
			d.getZKMK(),
			d.getZRIT(),
			d.getZKEIGEN(),
			d.getZZKB(),
			d.getZGYO(),
			d.getZSRE(),
			d.r.getRIT(),
			d.r.getKEIGEN(),
			d.s.getRIT(),
			d.s.getKEIGEN(),
			d.r.getZKB(),
			d.getRGYO(),
			d.r.getSRE(),
			d.s.getZKB(),
			d.getSGYO(),
			d.s.getSRE(),
			d.com.getSYMD(),
			d.getSKBN(),
			d.getSKIZ(),
			d.getUYMD(),
			d.getUKBN(),
			d.getUKIZ(),
			d.getSTEN(),
			d.getDKEC(),
			d.getKYMD(),
			d.getKBMN(),
			d.getKUSR(),
			d.getFUSR(),
			d.getFSEN(),
			d.getSGNO(),
			d.com.getBUNRI(),
			d.getRATE(),
			d.getGEXVL(),
			d.getGVALU(),
			d.r.getURIZEIKEISAN(),
			d.r.getMENZEIKEIKA(),
			d.s.getURIZEIKEISAN(),
			d.s.getMENZEIKEIKA(),
			d.getZURIZEIKEISAN(),
			d.getZMENZEIKEIKA()
		);
	}

	/**
	 * 仕訳抽出テーブル(SIAS)へのインサート
	 * シリアル番号、登録日時、更新日時は自動セットなので指定不要。その他の更新項目の値を指定する。
	 * @param d 仕訳抽出データ
	 */
	protected void insert_sias(ShiwakeData d) {
		String shokuchiKamokuCd = setting.shokuchiCd();
		
		final String sql = "INSERT INTO shiwake_sias(denpyou_id, shiwake_status, touroku_time, koushin_time, DYMD, SEIRI, DCNO, KYMD, KBMN, KUSR, SGNO, HF1, HF2, HF3, HF4, HF5, HF6, HF7, HF8, HF9, HF10, "
				+ "RBMN, RTOR, RKMK, REDA, RKOJ, RKOS, RPRJ, RSEG, RDM1, RDM2, RDM3, RDM4, RDM5, RDM6, RDM7, RDM8, RDM9, RDM10, RDM11, RDM12, RDM13, RDM14, RDM15, RDM16, RDM17, RDM18, RDM19, RDM20, RRIT, RKEIGEN, RZKB, RGYO, RSRE, RTKY, RTNO, RURIZEIKEISAN, RMENZEIKEIKA, "
				+ "SBMN, STOR, SKMK, SEDA, SKOJ, SKOS, SPRJ, SSEG, SDM1, SDM2, SDM3, SDM4, SDM5, SDM6, SDM7, SDM8, SDM9, SDM10, SDM11, SDM12, SDM13, SDM14, SDM15, SDM16, SDM17, SDM18, SDM19, SDM20, SRIT, SKEIGEN, SZKB, SURIZEIKEISAN, SMENZEIKEIKA, SGYO, SSRE, STKY, STNO, "
				+ "ZKMK, ZRIT, ZKEIGEN, ZZKB, ZGYO, ZSRE, ZURIZEIKEISAN, ZMENZEIKEIKA, EXVL, VALU, SYMD, SKBN, SKIZ, UYMD, UKBN, UKIZ, DKEC, FUSR, FSEN, TKFLG, BUNRI, HEIC, RATE, GEXVL, GVALU, GSEP)"
				+ " VALUES(?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '')"; 
		connection.update(sql,
			//WF
			d.main.getDenpyou_id(),
			d.main.getShiwake_status(),
			//伝票情報
			d.com.getDYMD(),
			d.getSEIRI(),
			d.com.getDCNO(),
			d.getKYMD(),
			d.getKBMN(),
			d.getKUSR(),
			d.getSGNO(),
			hfWf2Sias(d, 1),
			hfWf2Sias(d, 2),
			hfWf2Sias(d, 3),
			hfWf2Sias(d, 4),
			hfWf2Sias(d, 5),
			hfWf2Sias(d, 6),
			hfWf2Sias(d, 7),
			hfWf2Sias(d, 8),
			hfWf2Sias(d, 9),
			hfWf2Sias(d, 10),

			//借方情報
			d.r.getBMN(),
			d.r.getTOR(),
			d.r.getKMK(),
			d.r.getEDA(),
			d.getRKOJ(),
			d.getRKOS(),
			d.r.getPRJ(),
			d.r.getSEG(),
			kariUfWf2Sias(d, 1),
			kariUfWf2Sias(d, 2),
			kariUfWf2Sias(d, 3),
			kariUfWf2Sias(d, 4),
			kariUfWf2Sias(d, 5),
			kariUfWf2Sias(d, 6),
			kariUfWf2Sias(d, 7),
			kariUfWf2Sias(d, 8),
			kariUfWf2Sias(d, 9),
			kariUfWf2Sias(d, 10),
			kariUfWf2Sias(d, 11),
			kariUfWf2Sias(d, 12),
			kariUfWf2Sias(d, 13),
			kariUfWf2Sias(d, 14),
			kariUfWf2Sias(d, 15),
			kariUfWf2Sias(d, 16),
			kariUfWf2Sias(d, 17),
			kariUfWf2Sias(d, 18),
			kariUfWf2Sias(d, 19),
			kariUfWf2Sias(d, 20),
			d.r.getRIT(),
			d.r.getKEIGEN(),
			d.r.getZKB(),
			d.getRGYO(),
			d.r.getSRE(),
			d.com.getTKY(), //RTKY
			(d.r.getKMK() != null && d.r.getKMK().equals(shokuchiKamokuCd)) ? "" :  d.com.getTNO(),//RTNO
			d.r.getURIZEIKEISAN(),
			d.r.getMENZEIKEIKA(),
			//貸方情報
			d.s.getBMN(),
			d.s.getTOR(),
			d.s.getKMK(),
			d.s.getEDA(),
			d.getSKOJ(),
			d.getSKOS(),
			d.s.getPRJ(),
			d.s.getSEG(),
			kashiUfWf2Sias(d, 1),
			kashiUfWf2Sias(d, 2),
			kashiUfWf2Sias(d, 3),
			kashiUfWf2Sias(d, 4),
			kashiUfWf2Sias(d, 5),
			kashiUfWf2Sias(d, 6),
			kashiUfWf2Sias(d, 7),
			kashiUfWf2Sias(d, 8),
			kashiUfWf2Sias(d, 9),
			kashiUfWf2Sias(d, 10),
			kashiUfWf2Sias(d, 11),
			kashiUfWf2Sias(d, 12),
			kashiUfWf2Sias(d, 13),
			kashiUfWf2Sias(d, 14),
			kashiUfWf2Sias(d, 15),
			kashiUfWf2Sias(d, 16),
			kashiUfWf2Sias(d, 17),
			kashiUfWf2Sias(d, 18),
			kashiUfWf2Sias(d, 19),
			kashiUfWf2Sias(d, 20),
			d.s.getRIT(),
			d.s.getKEIGEN(),
			d.s.getZKB(),
			d.s.getURIZEIKEISAN(),
			d.s.getMENZEIKEIKA(),
			d.getSGYO(),
			d.s.getSRE(),
			"", //STKY
			(d.s.getKMK() != null && d.s.getKMK().equals(shokuchiKamokuCd)) ? "" :  d.com.getTNO(),//STNO（RTNOと同じもの）
			//消費税対象
			d.getZKMK(),
			d.getZRIT(),
			d.getZKEIGEN(),
			d.getZZKB(),
			d.getZGYO(),
			d.getZSRE(),
			d.getZURIZEIKEISAN(),
			d.getZMENZEIKEIKA(),
			//仕訳共通
			d.getEXVL(),
			d.com.getVALU(),
			d.com.getSYMD(),
			d.getSKBN(),
			d.getSKIZ(),
			d.getUYMD(),
			d.getUKBN(),
			d.getUKIZ(),
			d.getDKEC(),
			d.getFUSR(),
			d.getFSEN(),
			d.getTKFLG(),
			d.com.getBUNRI(),
			d.getHEIC(),
			d.getRATE(),
			d.getGEXVL(),
			d.getGVALU()
		);
	}

	/**
	 * WFのHF①～③をSIASの借HF１～１０にマッピングして返す。
	 * 伝票IDを該当HFに反映する設定になっている場合、伝票IDを採用
	 * 社員コードをHFに反映する設定になっている場合、社員コードを採用
	 * 
	 * @param d 仕訳データ(各伝票抽出から渡された状態）
	 * @param no １～１０
	 * @return HF１～１０(引数noで指定した番号)
	 */
	protected String hfWf2Sias(ShiwakeData d, int no) {
		String dIdMapping = setting.uf1DenpyouIdHanei();
		String sCdMapping = setting.shainCdRenkei();
		String kNoRyakuMapping = setting.kianNoHaneiRyakugou();
		String kNoBanMapping = setting.kianNoHaneiBangou();

		//伝票IDを該当HFに反映する設定になっている場合、伝票IDを採用
		if (dIdMapping.startsWith("HF")) {
			int dIdMappingNo = Integer.parseInt(dIdMapping.substring(2));
			if (dIdMappingNo == no) {
				return d.main.getDenpyou_id();
			}
		}

		//社員コードをHFに反映する設定になっている場合、社員コードを採用
		if (sCdMapping.startsWith("HF")) {
			int sCdMappingNo = Integer.parseInt(sCdMapping.substring(2));
			if (sCdMappingNo == no) {
				return trimShainNo(d, d.main.shainNo);
			}
		}
		
		//予算執行管理オプションが「予算執行管理Aあり」の場合
		String yosanShikkouOption = RegAccess.checkEnableYosanShikkouOption();
		if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
			//起案番号をHFに転記する設定かつ
			//起案番号を運用する設定の伝票種別である場合、起案番号を採用
			int kNoRyakuMappingNo = Integer.parseInt(kNoRyakuMapping);
			if( kNoRyakuMappingNo == no ){
				String denpyouKbn = d.main.getDenpyou_id().substring(7, 11);
				if(cl.judgeKianbangouUnyouFlg(denpyouKbn) == true){
					return getKianNoRyakugou4HF(d);
				}
			}
			int kNoBanMappingNo = Integer.parseInt(kNoBanMapping);
			if( kNoBanMappingNo == no ){
				String denpyouKbn = d.main.getDenpyou_id().substring(7, 11);
				if(cl.judgeKianbangouUnyouFlg(denpyouKbn) == true){
					return getKianNoBangou4HF(d);
				}
			}
			
		}

		//WFのHF①～⑩をSIASの借HF１～１０にマッピングして返す
		for (int i = 1; i <= 10; i++) {
			int mapping = Integer.parseInt(EteamSettingInfo.getSettingInfo("hf" + i + "_mapping"));
			if (no == mapping) {
				switch(i) {
				case 1:
					if(!isEmpty(d.main.getHF1())) return d.main.getHF1();
					break;
				case 2:
					if(!isEmpty(d.main.getHF2())) return d.main.getHF2();
					break;
				case 3:
					if(!isEmpty(d.main.getHF3())) return d.main.getHF3();
					break;
				case 4:
					if(!isEmpty(d.main.getHF4())) return d.main.getHF4();
					break;
				case 5:
					if(!isEmpty(d.main.getHF5())) return d.main.getHF5();
					break;
				case 6:
					if(!isEmpty(d.main.getHF6())) return d.main.getHF6();
					break;
				case 7:
					if(!isEmpty(d.main.getHF7())) return d.main.getHF7();
					break;
				case 8:
					if(!isEmpty(d.main.getHF8())) return d.main.getHF8();
					break;
				case 9:
					if(!isEmpty(d.main.getHF9())) return d.main.getHF9();
					break;
				case 10:
					if(!isEmpty(d.main.getHF10())) return d.main.getHF10();
					break;
				}
			}
		}

		return "";
	}

	/**
	 * WFの借UF①～③をDE3の借UF１～３用に返す。
	 * 伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コードを借UFに反映するものである場合、伝票IDを採用
	 * 社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コードを借UFに反映するものである場合、社員コードを採用
	 * 
	 * @param d 仕訳データ(各伝票抽出から渡された状態）
	 * @param no １～３
	 * @return 借UF１～３(引数noで指定した番号)
	 */
	protected String kariUfWf2De3(ShiwakeData d, int no) {
		String dIdMapping = setting.uf1DenpyouIdHanei();
		String sCdMapping = setting.shainCdRenkei();

		//伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で反映するものである場合、伝票IDを採用
		if (dIdMapping.startsWith("UF") && d.r.didFlg) {
			int dIdMappingNo = Integer.parseInt(dIdMapping.substring(2));
			if (dIdMappingNo == no) {
				return d.r.did;
			}
		}

		//社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で仕訳パターンで社員コード連携あり担っている場合、社員NOを採用
		if (sCdMapping.startsWith("UF") && d.r.didFlg && d.r.shainCdFlg) {
			int sCdMappingNo = Integer.parseInt(sCdMapping.substring(2));
			if (sCdMappingNo == no) {
				return trimShainNo(d, d.r.shainNo);
			}
		}

		//WFの借UF①～③をde3の借UF１～3にマッピングして返す
		switch(no) {
		case 1:
			if(!isEmpty(d.r.getDM1())) return d.r.getDM1();
			break;
		case 2:
			if(!isEmpty(d.r.getDM2())) return d.r.getDM2();
			break;
		case 3:
			if(!isEmpty(d.r.getDM3())) return d.r.getDM3();
			break;
		}

		return "";
	}

	/**
	 * WFの(可変)借UF①～⑩、(固定)借UF①～⑩をSIASの借UF１～２０にマッピングして返す。
	 * 伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コードを借UFに反映するものである場合、伝票IDを採用
	 * 社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コードを借UFに反映するものである場合、社員コードを採用
	 * 
	 * @param d 仕訳データ(各伝票抽出から渡された状態）
	 * @param no １～２０
	 * @return 借UF１～２０(引数noで指定した番号)
	 */
	protected String kariUfWf2Sias(ShiwakeData d, int no) {
		String dIdMapping = setting.uf1DenpyouIdHanei();
		String sCdMapping = setting.shainCdRenkei();

		//伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で反映するものである場合、伝票IDを採用
		if (dIdMapping.startsWith("UF") && d.r.didFlg) {
			int dIdMappingNo = Integer.parseInt(dIdMapping.substring(2));
			if (dIdMappingNo == no) {
				return d.r.did;
			}
		}

		//社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で仕訳パターンで社員コード連携あり担っている場合、社員NOを採用
		if (sCdMapping.startsWith("UF") && d.r.didFlg && d.r.shainCdFlg) {
			int sCdMappingNo = Integer.parseInt(sCdMapping.substring(2));
			if (sCdMappingNo == no) {
				return trimShainNo(d, d.r.shainNo);
			}
		}

		//WFの借UF①～⑩をSIASの借UF１～２０にマッピングして返す
		for (int i = 1; i <= 10; i++) {
			int mapping = Integer.parseInt(EteamSettingInfo.getSettingInfo("uf" + i + "_mapping"));
			if (no == mapping) {
				switch(i) {
				case 1:
					if(!isEmpty(d.r.getDM1())) return d.r.getDM1();
					break;
				case 2:
					if(!isEmpty(d.r.getDM2())) return d.r.getDM2();
					break;
				case 3:
					if(!isEmpty(d.r.getDM3())) return d.r.getDM3();
					break;
				case 4:
					if(!isEmpty(d.r.getDM4())) return d.r.getDM4();
					break;
				case 5:
					if(!isEmpty(d.r.getDM5())) return d.r.getDM5();
					break;
				case 6:
					if(!isEmpty(d.r.getDM6())) return d.r.getDM6();
					break;
				case 7:
					if(!isEmpty(d.r.getDM7())) return d.r.getDM7();
					break;
				case 8:
					if(!isEmpty(d.r.getDM8())) return d.r.getDM8();
					break;
				case 9:
					if(!isEmpty(d.r.getDM9())) return d.r.getDM9();
					break;
				case 10:
					if(!isEmpty(d.r.getDM10())) return d.r.getDM10();
					break;
				}
			}
		}
		
		//WFの固定借UF①～⑩をSIASの借UF１～２０にマッピングして返す
		for (int i = 1; i <= 10; i++) {
			int mapping = Integer.parseInt(EteamSettingInfo.getSettingInfo("uf_kotei" + i + "_mapping"));
			if (no == mapping) {
				switch(i) {
				case 1:
					if(!isEmpty(d.r.getK_DM1())) return d.r.getK_DM1();
					break;
				case 2:
					if(!isEmpty(d.r.getK_DM2())) return d.r.getK_DM2();
					break;
				case 3:
					if(!isEmpty(d.r.getK_DM3())) return d.r.getK_DM3();
					break;
				case 4:
					if(!isEmpty(d.r.getK_DM4())) return d.r.getK_DM4();
					break;
				case 5:
					if(!isEmpty(d.r.getK_DM5())) return d.r.getK_DM5();
					break;
				case 6:
					if(!isEmpty(d.r.getK_DM6())) return d.r.getK_DM6();
					break;
				case 7:
					if(!isEmpty(d.r.getK_DM7())) return d.r.getK_DM7();
					break;
				case 8:
					if(!isEmpty(d.r.getK_DM8())) return d.r.getK_DM8();
					break;
				case 9:
					if(!isEmpty(d.r.getK_DM9())) return d.r.getK_DM9();
					break;
				case 10:
					if(!isEmpty(d.r.getK_DM10())) return d.r.getK_DM10();
					break;
				}
			}
		}

		return "";
	}

	/**
	 * WFの貸UF①～③をDE3の貸UF１～３用に返す。
	 * 伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コードを貸UFに反映するものである場合、伝票IDを採用
	 * 社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コードを貸UFに反映するものである場合、社員コードを採用
	 * 
	 * @param d 仕訳データ(各伝票抽出から渡された状態）
	 * @param no １～３
	 * @return 貸UF１～３(引数noで指定した番号)
	 */
	protected String kashiUfWf2De3(ShiwakeData d, int no) {
		String dIdMapping = setting.uf1DenpyouIdHanei();
		String sCdMapping = setting.shainCdRenkei();
		String hCdMapping = setting.houjinCardRenkei();

		//伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で反映するものである場合、伝票IDを採用
		if (dIdMapping.startsWith("UF") && d.s.didFlg) {
			int dIdMappingNo = Integer.parseInt(dIdMapping.substring(2));
			if (dIdMappingNo == no) {
				return d.s.did;
			}
		}

		//社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で仕訳パターンで社員コード連携あり担っている場合、社員NOを採用
		if (sCdMapping.startsWith("UF") && d.s.didFlg && d.s.shainCdFlg) {
			int sCdMappingNo = Integer.parseInt(sCdMapping.substring(2));
			if (sCdMappingNo == no) {
				return trimShainNo(d, d.s.shainNo);
			}
		}
		
		//法人カード識別用番号をUFに反映する設定になっていて、この仕訳が法人カード識別用番号を貸UFに反映するものである場合、法人カード識別用番号を採用
		if (hCdMapping.startsWith("UF") && d.s.houjinFlg) {
			int hCdMappingNo = Integer.parseInt(hCdMapping.substring(2));
			if (hCdMappingNo == no) {
				return d.s.houjin;
			}
		}

		//WFの貸UF①～③をde3の貸UF１～3にマッピングして返す
		switch(no) {
		case 1:
			if(!isEmpty(d.s.getDM1())) return d.s.getDM1();
			break;
		case 2:
			if(!isEmpty(d.s.getDM2())) return d.s.getDM2();
			break;
		case 3:
			if(!isEmpty(d.s.getDM3())) return d.s.getDM3();
			break;
		}

		return "";
	}

	/**
	 * WFの(可変)貸UF①～⑩、(固定)貸UF①～⑩をSIASの貸UF１～２０にマッピングして返す。
	 * 伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コードを貸UFに反映するものである場合、伝票IDを採用
	 * 社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コードを貸UFに反映するものである場合、社員コードを採用
	 * 
	 * @param d 仕訳データ(各伝票抽出から渡された状態）
	 * @param no １～２０
	 * @return 貸UF１～２０(引数noで指定した番号)
	 */
	protected String kashiUfWf2Sias(ShiwakeData d, int no) {
		String dIdMapping = setting.uf1DenpyouIdHanei();
		String sCdMapping = setting.shainCdRenkei();
		String hCdMapping = setting.houjinCardRenkei();

		//伝票IDを該当UFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で反映するものである場合、伝票IDを採用
		if (dIdMapping.startsWith("UF") && d.s.didFlg) {
			int dIdMappingNo = Integer.parseInt(dIdMapping.substring(2));
			if (dIdMappingNo == no) {
				return d.s.did;
			}
		}

		//社員コードをUFに反映する設定になっていて、この仕訳が伝票ID/社員コード反映対象で仕訳パターンで社員コード連携あり担っている場合、社員NOを採用
		if (sCdMapping.startsWith("UF") && d.s.didFlg && d.s.shainCdFlg) {
			int sCdMappingNo = Integer.parseInt(sCdMapping.substring(2));
			if (sCdMappingNo == no) {
				return trimShainNo(d, d.s.shainNo);
			}
		}

		//法人カード識別用番号をUFに反映する設定になっていて、この仕訳が法人カード識別用番号を貸UFに反映するものである場合、法人カード識別用番号を採用
		if (hCdMapping.startsWith("UF") && d.s.houjinFlg) {
			int hCdMappingNo = Integer.parseInt(hCdMapping.substring(2));
			if (hCdMappingNo == no) {
				return d.s.houjin;
			}
		}

		//WFの借UF①～⑩をSIASの借UF１～２０にマッピングして返す
		for (int i = 1; i <= 10; i++) {
			int mapping = Integer.parseInt(EteamSettingInfo.getSettingInfo("uf" + i + "_mapping"));
			if (no == mapping) {
				switch(i) {
				case 1:
					if(!isEmpty(d.s.getDM1())) return d.s.getDM1();
					break;
				case 2:
					if(!isEmpty(d.s.getDM2())) return d.s.getDM2();
					break;
				case 3:
					if(!isEmpty(d.s.getDM3())) return d.s.getDM3();
					break;
				case 4:
					if(!isEmpty(d.s.getDM4())) return d.s.getDM4();
					break;
				case 5:
					if(!isEmpty(d.s.getDM5())) return d.s.getDM5();
					break;
				case 6:
					if(!isEmpty(d.s.getDM6())) return d.s.getDM6();
					break;
				case 7:
					if(!isEmpty(d.s.getDM7())) return d.s.getDM7();
					break;
				case 8:
					if(!isEmpty(d.s.getDM8())) return d.s.getDM8();
					break;
				case 9:
					if(!isEmpty(d.s.getDM9())) return d.s.getDM9();
					break;
				case 10:
					if(!isEmpty(d.s.getDM10())) return d.s.getDM10();
					break;
				}
			}
		}
		
		//WFの固定借UF①～⑩をSIASの借UF１～２０にマッピングして返す
		for (int i = 1; i <= 10; i++) {
			int mapping = Integer.parseInt(EteamSettingInfo.getSettingInfo("uf_kotei" + i + "_mapping"));
			if (no == mapping) {
				switch(i) {
				case 1:
					if(!isEmpty(d.s.getK_DM1())) return d.s.getK_DM1();
					break;
				case 2:
					if(!isEmpty(d.s.getK_DM2())) return d.s.getK_DM2();
					break;
				case 3:
					if(!isEmpty(d.s.getK_DM3())) return d.s.getK_DM3();
					break;
				case 4:
					if(!isEmpty(d.s.getK_DM4())) return d.s.getK_DM4();
					break;
				case 5:
					if(!isEmpty(d.s.getK_DM5())) return d.s.getK_DM5();
					break;
				case 6:
					if(!isEmpty(d.s.getK_DM6())) return d.s.getK_DM6();
					break;
				case 7:
					if(!isEmpty(d.s.getK_DM7())) return d.s.getK_DM7();
					break;
				case 8:
					if(!isEmpty(d.s.getK_DM8())) return d.s.getK_DM8();
					break;
				case 9:
					if(!isEmpty(d.s.getK_DM9())) return d.s.getK_DM9();
					break;
				case 10:
					if(!isEmpty(d.s.getK_DM10())) return d.s.getK_DM10();
					break;
				}
			}
		}

		return "";
	}
	
	/**
	 * 伝票に紐づけられた起案番号(略号部)を返す。
	 * 略号部を全角スペース含めた5文字に調整後、実施・支出間には区切りスペースを含めずに連結する。
	 * 片方のみ起案番号がある場合は、全角スペース含めた5文字に調整した文字列を返却する。
	 * @param d 仕訳データ
	 * @return 起案番号略号(『○○○○　×××××』の形式)
	 */
	protected String getKianNoRyakugou4HF(ShiwakeData d) {
		
		String retStr = "";
		String[] tmpJissi = {"",""};
		String[] tmpShishutsu = {"",""};
		String denpyouId = d.main.getDenpyou_id();
		
		//実施起案番号・支出起案番号をHF転記用形式に調整して取得
		GMap tmpMap = cl.findKianHimozuke(denpyouId);
		if(tmpMap != null && tmpMap.get("jisshi_kian_bangou") != null){
			tmpJissi = adjustKianNo((String)tmpMap.get("jisshi_kian_bangou"));
		}
		if(tmpMap != null && tmpMap.get("shishutsu_kian_bangou") != null){
			tmpShishutsu = adjustKianNo((String)tmpMap.get("shishutsu_kian_bangou"));
		}
		
		//略号部は実施・支出間の区切りスペースは追加せずそのまま連結
		retStr = tmpJissi[0];
		if(retStr.length() > 0){
			retStr = retStr + tmpShishutsu[0];
		}else{
			retStr = tmpShishutsu[0];
		}
		
		return retStr;
	}
	
	
	/**
	 * 伝票に紐づけられた起案番号(番号部)を返す。
	 * 半角数字の形式で取得し、実施・支出間に半角の区切りスペースを含めて連結する。
	 * 片方のみ起案番号がある場合は、その数値のみを返却する。
	 * @param d 仕訳データ
	 * @return 起案番号(『xxxx yyyy』の形式)
	 */
	protected String getKianNoBangou4HF(ShiwakeData d) {
		
		String retStr = "";
		String[] tmpJissi = {"",""};
		String[] tmpShishutsu = {"",""};
		String denpyouId = d.main.getDenpyou_id();
		
		//実施起案番号・支出起案番号をHF転記用形式に調整して取得
		GMap tmpMap = cl.findKianHimozuke(denpyouId);
		if(tmpMap != null && tmpMap.get("jisshi_kian_bangou") != null){
			tmpJissi = adjustKianNo((String)tmpMap.get("jisshi_kian_bangou"));
		}
		if(tmpMap != null && tmpMap.get("shishutsu_kian_bangou") != null){
			tmpShishutsu = adjustKianNo((String)tmpMap.get("shishutsu_kian_bangou"));
		}
		
		//番号部は実施・支出間に半角区切りスペースを追加して連結
		retStr = tmpJissi[1];
		if(retStr.length() > 0){
			retStr = retStr + " " + tmpShishutsu[1];
		}else{
			retStr = tmpShishutsu[1];
		}
		
		return retStr;
	}
	
	
	/**
	 * 『○○○○ ××××号』の形式の文字列をHF転記用の形式に変換する。
	 * 略号部は5文字以下なら全角スペースで5文字まで埋め、6文字以上なら5文字まででカット。
	 * 番号部は『××××号』の『××××』を半角数字に変換。
	 * @param orgKianNo DB登録された起案番号(『○○○○ ××××号』の形式の文字列)
	 * @return 形式変換した起案番号 [0]略号部 [1]番号部
	 */
	protected String[] adjustKianNo(String orgKianNo){
		
		//データが空なら空文字配列返却
		String[] retStr = {"",""};
		if (orgKianNo.isEmpty())
		{
			return retStr;
		}
		
		//伝票起案紐付テーブル登録時形式の文字列を略号部・番号部に分割
		String tmpRyaku = "";
		String tmpBan = "";
		int index = orgKianNo.lastIndexOf(" ");
		tmpRyaku = orgKianNo.substring(0,index);
		tmpBan = orgKianNo.substring(index + 1, orgKianNo.length());
		
		//略号の文字数調整
		//5文字以下なら全角スペースで5文字まで埋め、6文字以上なら5文字まででカット
		while(tmpRyaku.length() < 5){
			tmpRyaku = tmpRyaku + "　";
		}
		if(tmpRyaku.length() > 5){
			tmpRyaku = tmpRyaku.substring(0,5);
		}
		retStr[0] = tmpRyaku;
		
		//番号部(全角)を半角数字に変換
		for(int i = 0; i < tmpBan.length(); i++){
			char tmpChr = tmpBan.charAt(i);
			switch(tmpChr){
			case '０':retStr[1] = retStr[1] + "0"; break;
			case '１':retStr[1] = retStr[1] + "1"; break;
			case '２':retStr[1] = retStr[1] + "2"; break;
			case '３':retStr[1] = retStr[1] + "3"; break;
			case '４':retStr[1] = retStr[1] + "4"; break;
			case '５':retStr[1] = retStr[1] + "5"; break;
			case '６':retStr[1] = retStr[1] + "6"; break;
			case '７':retStr[1] = retStr[1] + "7"; break;
			case '８':retStr[1] = retStr[1] + "8"; break;
			case '９':retStr[1] = retStr[1] + "9"; break;
			default:break;
			}
		}
		
		return retStr;
	}

	/**
	 * 社員番号を返す。
	 * @param s ShiwakeData
	 * @param shainNo 社員番号
	 * @return 社員番号
	 */
	protected String trimShainNo(ShiwakeData s, String shainNo) {
		return !isEmpty(shainNo) ? shainNo : (String)cl.findKihyouUser(s.main.getDenpyou_id()).get("shain_no"); 
	}
}
