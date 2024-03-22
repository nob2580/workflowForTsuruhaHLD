package eteam.gyoumu.kaikei.common;

import java.math.BigDecimal;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dto.KamokuMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;


/**
 * 会計共通処理（バッチ）
 */
public class CommonChuushutsuLogic extends EteamAbstractLogic {

	/** 会計共通ロジック */
	protected KaikeiCommonLogic kaikei;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 仕訳ロジック */
	protected ShiwakeLogic shiwakeLogic;
	/** FBロジック */
	protected FBLogic fbLogic;
	/** 会計カテゴリSELECT */
	protected KaikeiCategoryLogic kaikeiLg;
	/** 部門ユーザーカテゴリSELECT */
	BumonUserKanriCategoryLogic bumonUserLg;
	/** ワークフローカテゴリーロジック */
	WorkflowCategoryLogic wfLg;
	/** マスターカテゴリ */
	MasterKanriCategoryLogic masterLogic;
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;
	
	/** 処理グループ	 */
	String shoriGroup;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		kaikei = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		shiwakeLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		fbLogic = EteamContainer.getComponent(FBLogic.class, connection);
		kaikeiLg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		bumonUserLg = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfLg = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		this.masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}

	/**
	 * 仕訳のメイン部分を作成する
	 * @param denpyou 伝票テーブル + 申請本体テーブル
	 * @param shainNo 本体社員NO
	 * @return 仕訳(メイン)
	 */
	public ShiwakeDataMain makeMain(GMap denpyou, String shainNo){
		ShiwakeDataMain ret = new ShiwakeDataMain();

		ret.setDenpyou_id((String)denpyou.get("denpyou_id")); // 伝票ID

		ret.setHF1((String)denpyou.get("hf1_cd")); //ヘッダフィールド１
		ret.setHF2((String)denpyou.get("hf2_cd")); //ヘッダフィールド２
		ret.setHF3((String)denpyou.get("hf3_cd")); //ヘッダフィールド３
		ret.setHF4((String)denpyou.get("hf4_cd")); //ヘッダフィールド４
		ret.setHF5((String)denpyou.get("hf5_cd")); //ヘッダフィールド５
		ret.setHF6((String)denpyou.get("hf6_cd")); //ヘッダフィールド６
		ret.setHF7((String)denpyou.get("hf7_cd")); //ヘッダフィールド７
		ret.setHF8((String)denpyou.get("hf8_cd")); //ヘッダフィールド８
		ret.setHF9((String)denpyou.get("hf9_cd")); //ヘッダフィールド９
		ret.setHF10((String)denpyou.get("hf10_cd")); //ヘッダフィールド１０

		ret.shainNo = shainNo;
		return ret;
	}

	/**
	 * 仕訳の借方を作る
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @return 仕訳の借方
	 */
	public ShiwakeDataRS makeKarikata(GMap meisai) {

		//使いそうな項目取り出しておいて
		String denpyouId = (String)meisai.get("denpyou_id");
		String denpyouKbn = denpyouId.substring(7, 11);

		return makeKarikata(meisai, denpyouKbn);
	}

	/**
	 * 仕訳の借方を作る
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @param denpyouKbn 伝票区分
	 * @return 仕訳の借方
	 */
	public ShiwakeDataRS makeKarikata(GMap meisai, String denpyouKbn) {
		ShiwakeDataRS ret = new ShiwakeDataRS();

		//使いそうな項目取り出しておいて
		int shiwakeEdano = (int)meisai.get("shiwake_edano");

		//仕訳パターン
		GMap shiwakeP = kaikeiLg.findShiwakePattern(denpyouKbn, shiwakeEdano);

		//仕訳項目マッピング
		ret.setBMN((String)meisai.get("kari_futan_bumon_cd")); //部門コード
		ret.setTOR(kaikei.convTorihikisaki((String)meisai.get("torihikisaki_cd"), (String)shiwakeP.get("kari_torihikisaki_cd"))); //取引先コード
		ret.setKMK((String)meisai.get("kari_kamoku_cd")); //科目コード
		ret.setEDA((String)meisai.get("kari_kamoku_edaban_cd")); //枝番コード
		ret.setPRJ(kaikei.convProject((String)meisai.get("project_cd"), (String)shiwakeP.get("kari_project_cd"))); //プロジェクトコード
		ret.setSEG(kaikei.convSegment((String)meisai.get("segment_cd"), (String)shiwakeP.get("kari_segment_cd"))); //セグメントコード
		ret.setDM1(kaikei.convUf((String)meisai.get("uf1_cd"), (String)shiwakeP.get("kari_uf1_cd"))); //ユニバーサルフィールド１
		ret.setDM2(kaikei.convUf((String)meisai.get("uf2_cd"), (String)shiwakeP.get("kari_uf2_cd"))); //ユニバーサルフィールド２
		ret.setDM3(kaikei.convUf((String)meisai.get("uf3_cd"), (String)shiwakeP.get("kari_uf3_cd"))); //ユニバーサルフィールド３
		ret.setDM4(kaikei.convUf((String)meisai.get("uf4_cd"), (String)shiwakeP.get("kari_uf4_cd"))); //ユニバーサルフィールド４
		ret.setDM5(kaikei.convUf((String)meisai.get("uf5_cd"), (String)shiwakeP.get("kari_uf5_cd"))); //ユニバーサルフィールド５
		ret.setDM6(kaikei.convUf((String)meisai.get("uf6_cd"), (String)shiwakeP.get("kari_uf6_cd"))); //ユニバーサルフィールド６
		ret.setDM7(kaikei.convUf((String)meisai.get("uf7_cd"), (String)shiwakeP.get("kari_uf7_cd"))); //ユニバーサルフィールド７
		ret.setDM8(kaikei.convUf((String)meisai.get("uf8_cd"), (String)shiwakeP.get("kari_uf8_cd"))); //ユニバーサルフィールド８
		ret.setDM9(kaikei.convUf((String)meisai.get("uf9_cd"), (String)shiwakeP.get("kari_uf9_cd"))); //ユニバーサルフィールド９
		ret.setDM10(kaikei.convUf((String)meisai.get("uf10_cd"), (String)shiwakeP.get("kari_uf10_cd"))); //ユニバーサルフィールド１０
		ret.setK_DM1((String)shiwakeP.get("kari_uf_kotei1_cd")); //固定ユニバーサルフィールド１
		ret.setK_DM2((String)shiwakeP.get("kari_uf_kotei2_cd")); //固定ユニバーサルフィールド２
		ret.setK_DM3((String)shiwakeP.get("kari_uf_kotei3_cd")); //固定ユニバーサルフィールド３
		ret.setK_DM4((String)shiwakeP.get("kari_uf_kotei4_cd")); //固定ユニバーサルフィールド４
		ret.setK_DM5((String)shiwakeP.get("kari_uf_kotei5_cd")); //固定ユニバーサルフィールド５
		ret.setK_DM6((String)shiwakeP.get("kari_uf_kotei6_cd")); //固定ユニバーサルフィールド６
		ret.setK_DM7((String)shiwakeP.get("kari_uf_kotei7_cd")); //固定ユニバーサルフィールド７
		ret.setK_DM8((String)shiwakeP.get("kari_uf_kotei8_cd")); //固定ユニバーサルフィールド８
		ret.setK_DM9((String)shiwakeP.get("kari_uf_kotei9_cd")); //固定ユニバーサルフィールド９
		ret.setK_DM10((String)shiwakeP.get("kari_uf_kotei10_cd")); //固定ユニバーサルフィールド１０

		String[] convZeiritsu = kaikei.convZeiritsu(n2b(meisai.get("zeiritsu")), n2b(meisai.get("keigen_zeiritsu_kbn")), shiwakeP.get("kari_zeiritsu"), shiwakeP.get("kari_keigen_zeiritsu_kbn"));
		ret.setRIT(common.judgeShiwakeZeiritsu((String)meisai.get("kari_kazei_kbn"), toDecimal(convZeiritsu[0]))); //税率
		ret.setKEIGEN(common.judgeShiwakeZeiritsuKbn((String)meisai.get("kari_kazei_kbn"), convZeiritsu[1])); //軽減税率区分

		ret.setZKB(common.convKazekKbn((String)meisai.get("kari_kazei_kbn"))); //課税区分

		//ShiwakeDataRS はnewした時点で仕入区分に空文字が入っているため、nullの時は空文字からnullに置き換えたりしない
		if(meisai.get("kari_shiire_kbn") != null) {
			ret.setSRE("9".equals(meisai.get("kari_shiire_kbn")) ? "0" : (String)meisai.get("kari_shiire_kbn"));//仕入区分 インボイス開始前でも表示するため
		}
		if("A002".equals(denpyouKbn) || "A005".equals(denpyouKbn) || "A012".equals(denpyouKbn)) {
			//伺い申請は強制的に仕入区分空文字
			ret.setSRE("");
		}
		//インボイス対応伝票の場合のみセット
		if("0".equals(meisai.get("invoice_denpyou"))) {
			ret.setURIZEIKEISAN("0");//併用売上税額計算方式
			//仕入税額控除経過措置割合
			//仮払い以外の場合、課税区分が非課税等の場合は値を「0」に書き換え
			KamokuMaster kmk = kamokuMasterDao.find(meisai.get("kari_kamoku_cd"));
			if (kmk.shoriGroup != null) {
				shoriGroup = kmk.shoriGroup.toString(); //事業者区分の制御で使用
			}
			if(!((List.of("001","011","002","013").contains(meisai.get("kari_kazei_kbn")) && List.of("2","5","6","7","8","10").contains(shoriGroup))
							|| shoriGroup.equals("21"))) {
				ret.setMENZEIKEIKA("0");
			}else {
				ret.setMENZEIKEIKA((String)meisai.get("jigyousha_kbn"));
			}
			ret.setVALU((BigDecimal)meisai.get("shiharai_kingaku"));
		}else {
			ret.setURIZEIKEISAN("0");//併用売上税額計算方式
			ret.setMENZEIKEIKA("0");//仕入税額控除経過措置割合
		}
		
		//課税区分が未設定""だったら、仕入区分も""にする
		if("".equals(ret.getZKB())) {
			ret.setSRE("");
		}

		ret.setDenpyouKbn(denpyouKbn);
		return ret;
	}

	/**
	 * 仕訳の貸方を作る
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @param kashiNo 貸方番号（取引の貸方１～）
	 * @return 仕訳の貸方
	 */
	public ShiwakeDataRS makeKashikata(GMap meisai, int kashiNo) {

		//使いそうな項目取り出しておいて
		String denpyouId = (String)meisai.get("denpyou_id");
		String denpyouKbn = denpyouId.substring(7, 11);

		return makeKashikata(meisai, kashiNo, denpyouKbn);
	}

	/**
	 * 仕訳の貸方を作る
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @param kashiNo 貸方番号（取引の貸方１～）
	 * @param denpyouKbn 伝票区分
	 * @return 仕訳の貸方
	 */
	public ShiwakeDataRS makeKashikata(GMap meisai, int kashiNo, String denpyouKbn) {
		ShiwakeDataRS ret = new ShiwakeDataRS();

		//使いそうな項目取り出しておいて
		int shiwakeEdano = (int)meisai.get("shiwake_edano");
		String daihyouFutanBumonCd = meisai.get("daihyou_futan_bumon_cd");
		String zaimuEdabanCd = this.masterLogic.getShainShiwakeEdano(wfLg.selectKihyoushaData(meisai.get("denpyou_id")).get("user_id"));

		if(meisai.get("user_id") != null
				&& !wfLg.selectKihyoushaData(meisai.get("denpyou_id")).get("user_id").equals(meisai.get("user_id"))){
			//使用者があってそれが起票者と違う（代理起票）の場合は特殊
			daihyouFutanBumonCd = bumonUserLg.findFirstDaihyouFutanBumonCd((String)meisai.get("user_id"));
			zaimuEdabanCd = this.masterLogic.getShainShiwakeEdano((String)meisai.get("user_id"));
		}

		//仕訳パターン
		GMap shiwakeP = kaikeiLg.findShiwakePattern(denpyouKbn, shiwakeEdano);

		ret.setBMN(kaikei.convFutanBumon((String)meisai.get("kari_futan_bumon_cd"), (String)shiwakeP.get("kashi_futan_bumon_cd" + kashiNo), daihyouFutanBumonCd)); //部門コード
		ret.setTOR(kaikei.convTorihikisaki((String)meisai.get("torihikisaki_cd"), (String)shiwakeP.get("kashi_torihikisaki_cd" + kashiNo))); //取引先コード
		ret.setKMK((String)shiwakeP.get("kashi_kamoku_cd" + kashiNo)); //科目コード
		ret.setEDA(this.convKamokuEdaban((String)shiwakeP.get("kashi_kamoku_edaban_cd" + kashiNo), zaimuEdabanCd)); //枝番コード
		ret.setPRJ(kaikei.convProject((String)meisai.get("project_cd"), (String)shiwakeP.get("kashi_project_cd" + kashiNo))); //プロジェクトコード
		ret.setSEG(kaikei.convSegment((String)meisai.get("segment_cd"), (String)shiwakeP.get("kashi_segment_cd" + kashiNo))); //セグメントコード
		ret.setDM1(kaikei.convUf((String)meisai.get("uf1_cd"), (String)shiwakeP.get("kashi_uf1_cd" + kashiNo))); //ユニバーサルフィールド１
		ret.setDM2(kaikei.convUf((String)meisai.get("uf2_cd"), (String)shiwakeP.get("kashi_uf2_cd" + kashiNo))); //ユニバーサルフィールド２
		ret.setDM3(kaikei.convUf((String)meisai.get("uf3_cd"), (String)shiwakeP.get("kashi_uf3_cd" + kashiNo))); //ユニバーサルフィールド３
		ret.setDM4(kaikei.convUf((String)meisai.get("uf4_cd"), (String)shiwakeP.get("kashi_uf4_cd" + kashiNo))); //ユニバーサルフィールド４
		ret.setDM5(kaikei.convUf((String)meisai.get("uf5_cd"), (String)shiwakeP.get("kashi_uf5_cd" + kashiNo))); //ユニバーサルフィールド５
		ret.setDM6(kaikei.convUf((String)meisai.get("uf6_cd"), (String)shiwakeP.get("kashi_uf6_cd" + kashiNo))); //ユニバーサルフィールド６
		ret.setDM7(kaikei.convUf((String)meisai.get("uf7_cd"), (String)shiwakeP.get("kashi_uf7_cd" + kashiNo))); //ユニバーサルフィールド７
		ret.setDM8(kaikei.convUf((String)meisai.get("uf8_cd"), (String)shiwakeP.get("kashi_uf8_cd" + kashiNo))); //ユニバーサルフィールド８
		ret.setDM9(kaikei.convUf((String)meisai.get("uf9_cd"), (String)shiwakeP.get("kashi_uf9_cd" + kashiNo))); //ユニバーサルフィールド９
		ret.setDM10(kaikei.convUf((String)meisai.get("uf10_cd"), (String)shiwakeP.get("kashi_uf10_cd" + kashiNo))); //ユニバーサルフィールド１０
		ret.setK_DM1((String)shiwakeP.get("kashi_uf_kotei1_cd" + kashiNo)); //固定ユニバーサルフィールド１
		ret.setK_DM2((String)shiwakeP.get("kashi_uf_kotei2_cd" + kashiNo)); //固定ユニバーサルフィールド２
		ret.setK_DM3((String)shiwakeP.get("kashi_uf_kotei3_cd" + kashiNo)); //固定ユニバーサルフィールド３
		ret.setK_DM4((String)shiwakeP.get("kashi_uf_kotei4_cd" + kashiNo)); //固定ユニバーサルフィールド４
		ret.setK_DM5((String)shiwakeP.get("kashi_uf_kotei5_cd" + kashiNo)); //固定ユニバーサルフィールド５
		ret.setK_DM6((String)shiwakeP.get("kashi_uf_kotei6_cd" + kashiNo)); //固定ユニバーサルフィールド６
		ret.setK_DM7((String)shiwakeP.get("kashi_uf_kotei7_cd" + kashiNo)); //固定ユニバーサルフィールド７
		ret.setK_DM8((String)shiwakeP.get("kashi_uf_kotei8_cd" + kashiNo)); //固定ユニバーサルフィールド８
		ret.setK_DM9((String)shiwakeP.get("kashi_uf_kotei9_cd" + kashiNo)); //固定ユニバーサルフィールド９
		ret.setK_DM10((String)shiwakeP.get("kashi_uf_kotei10_cd" + kashiNo)); //固定ユニバーサルフィールド１０
		ret.setRIT(common.judgeShiwakeZeiritsu((String)shiwakeP.get("kashi_kazei_kbn" + kashiNo), meisai.get("zeiritsu"))); //税率
		ret.setKEIGEN(common.judgeShiwakeZeiritsuKbn((String)shiwakeP.get("kashi_kazei_kbn" + kashiNo), n2b(meisai.get("keigen_zeiritsu_kbn")))); //軽減税率区
		ret.setZKB(common.convKazekKbn((String)shiwakeP.get("kashi_kazei_kbn" + kashiNo))); //課税区分
		//ShiwakeDataRS はnewした時点で仕入区分に空文字が入っているため、nullの時は空文字からnullに置き換えたりしない
		if(shiwakeP.get("kashi_shiire_kbn" + kashiNo) != null) {
			ret.setSRE("9".equals(shiwakeP.get("kashi_shiire_kbn" + kashiNo)) ? "0" : (String)shiwakeP.get("kashi_shiire_kbn" + kashiNo));//仕入区分インボイス開始前でも表示するため
		}
		if("A002".equals(denpyouKbn) || "A005".equals(denpyouKbn) || "A012".equals(denpyouKbn)) {
			//伺い申請は強制的に仕入区分空文字
			ret.setSRE("");
		}
		//レイアウトがインボイス対応SIASの時だけセットする（？）
		if("0".equals(meisai.get("invoice_denpyou"))) {
			ret.setURIZEIKEISAN("0");//併用売上税額計算方式
			ret.setMENZEIKEIKA("0");//仕入税額控除経過措置割合
			if(ret.getZKB().equals("2")) ret.setVALU_hontai((BigDecimal)meisai.get("shiharai_kingaku"));
		}else {
			ret.setURIZEIKEISAN("0"); //併用売上税額計算方式
			ret.setMENZEIKEIKA("0"); //仕入税額控除経過措置割合
		}
		
		//課税区分が未設定""だったら、仕入区分も""にする
		if("".equals(ret.getZKB())) {
			ret.setSRE("");
		}

		ret.setDenpyouKbn(denpyouKbn);
		return ret;
	}

	/**
	 * 仕訳の貸借-諸口を作る
	 * @return 仕訳の貸借-諸口
	 */
	public ShiwakeDataRS makeShokuchi() {
		ShiwakeDataRS ret = new ShiwakeDataRS();
		ret.setShokuchi(true);

		ret.setBMN(""); //部門コード
		ret.setTOR(""); //取引先コード
		ret.setKMK(setting.shokuchiCd()); //科目コード
		ret.setEDA(""); //枝番コード
		ret.setPRJ(""); //プロジェクトコード
		ret.setDM1(""); //ユニバーサルフィールド１
		ret.setDM2(""); //ユニバーサルフィールド２
		ret.setDM3(""); //ユニバーサルフィールド３
		ret.setRIT(null); //税率
		ret.setKEIGEN("");
		ret.setZKB(""); //課税区分
		return ret;
	}

	/**
	 * 取引の社員コード連携フラグを取得
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @return 社員コード連携フラグ=ありの場合はtrue
	 */
	public boolean getShainCdRenkeiFlg(GMap meisai){
		//使いそうな項目取り出しておいて
		String denpyouId = (String)meisai.get("denpyou_id");
		String denpyouKbn = denpyouId.substring(7, 11);
		return getShainCdRenkeiFlg(meisai, denpyouKbn);
	}

	/**
	 * 取引の社員コード連携フラグを取得
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @param denpyouKbn 伝票区分
	 * @return 社員コード連携フラグ=ありの場合はtrue
	 */
	public boolean getShainCdRenkeiFlg(GMap meisai, String denpyouKbn) {
		//仕訳パターン
		GMap shiwakeP = kaikeiLg.findShiwakePattern(denpyouKbn, (int)meisai.get("shiwake_edano"));
		return "1".equals(shiwakeP.get("shain_cd_renkei_flg"));
	}

	/**
	 * 取引の財務枝番コード連携フラグを取得
	 * @param meisai 伝票テーブル＋本体テーブル＋明細テーブルをマージしたやつを渡す。伝票種別によっては明細はない。
	 * @param denpyouKbn 伝票区分
	 * @return 社員コード連携フラグ=ありの場合はtrue
	 */
	public boolean getEdabanRenkeiFlg(GMap meisai, String denpyouKbn) {
		//仕訳パターン
		GMap shiwakeP = kaikeiLg.findShiwakePattern(denpyouKbn, (int)meisai.get("shiwake_edano"));
		return "1".equals(shiwakeP.get("edaban_renkei_flg"));
	}

	/**
	 * 仕訳パターンに照らして科目枝番コードの変換を行う
	 * @param shiwakePattern 仕訳パターンの科目枝番コード(空か固定コード、<ZAIMU>)
	 * @param zaimuEdabanCd 財務枝番コード
	 * @return 変換後科目枝番コード
	 */
	public String convKamokuEdaban(String shiwakePattern, String zaimuEdabanCd) {
		switch (shiwakePattern) {
			case MasterKanriCategoryLogic.ZAIMU:
				//社員口座テーブル指定の財務枝番コード
				return zaimuEdabanCd;
			default:
				//固定コード値 or Blank
				return shiwakePattern;
		}
	}

	/**
	 * マップをマージする（キーバッティングなら後ガチ）
	 * @param list マップ(バラ)
	 * @return マップ(マージ後)
	 */
	public GMap merge(GMap...list) {
		GMap ret = new GMap();
		for(GMap m : list)ret.putAll(m);
		return ret;
	}
}
