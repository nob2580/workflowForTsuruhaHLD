package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.TSUKEKAE_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dto.KamokuMaster;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 総合付替伝票抽出ジック
 */
public class SougouTsukekaeDenpyouChuushutsuLogic extends EteamAbstractLogic  {

	/** 会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 仕訳ロジック */
	protected ShiwakeLogic shiwakeLogic;
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;
	
	/** 処理グループ */
	String shoriGroup;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		shiwakeLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
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
			+ "INNER JOIN tsukekae k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 抽出対象の伝票を取得
	 * @return 抽出対象の伝票
	 */
	public List<GMap> loadDenpyou() {
		String sql =
				"SELECT * "
			+ "FROM denpyou d "
			+ "INNER JOIN tsukekae k ON "
			+ "  k.denpyou_id = d.denpyou_id "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? "
			+ "  AND d.denpyou_joutai = ? "
			+ "  AND d.chuushutsu_zumi_flg = '0' "
			+ "  AND NOT EXISTS (SELECT * FROM :SHIWAKE s WHERE d.denpyou_id = s.denpyou_id) "
			+ "ORDER BY "
			+ "  d.denpyou_id ASC";
		
		if (Open21Env.getVersion() == Version.DE3 ) {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_de3");
		} else {
			sql = sql.replaceAll(":SHIWAKE", "shiwake_sias");
		}
		
		return connection.load(sql, DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}
	
	/**
	 * 借方の作成
	 * @param meisai 伝票+申請本体+明細
	 * @return 借方仕訳
	 */
	public ShiwakeDataRS makeKari(GMap meisai){
		return this.makeTaishaku(meisai, true);
	}
	
	/**
	 * 貸方の作成
	 * @param meisai 伝票+申請本体+明細
	 * @return 貸方仕訳
	 */
	public ShiwakeDataRS makeKashi(GMap meisai){
		return this.makeTaishaku(meisai, false);
	}
	
	/**
	 * 貸借ほぼ同じなので共通化
	 * @param meisai 明細
	 * @param isKari 借方か
	 * @return 貸借どちらかの仕訳
	 */
	private ShiwakeDataRS makeTaishaku(GMap meisai, boolean isKari)
	{
		boolean kariKotei = TSUKEKAE_KBN.KARIKATA_KOUTEI.equals(meisai.get("tsukekae_kbn"));
		String prefix = (kariKotei == isKari) ? "moto_" : "saki_";
		String kazeiKbn = (String)meisai.get(prefix + "kazei_kbn");
		KamokuMaster kmk = kamokuMasterDao.find((String)meisai.get(prefix + "kamoku_cd"));
		if (kmk.shoriGroup != null) {
			shoriGroup = kmk.shoriGroup.toString(); //事業者区分の制御で使用
		}
		ShiwakeDataRS shiwakeDataRS = new ShiwakeDataRS();
		shiwakeDataRS.setBMN((String)meisai.get(prefix + "futan_bumon_cd")); //（オープン２１）部門コード
		shiwakeDataRS.setTOR((String)meisai.get(prefix + "torihikisaki_cd")); //（オープン２１）取引先コード
		shiwakeDataRS.setKMK((String)meisai.get(prefix + "kamoku_cd")); //（オープン２１）科目コード
		shiwakeDataRS.setEDA((String)meisai.get(prefix + "kamoku_edaban_cd")); //（オープン２１）枝番コード
		shiwakeDataRS.setPRJ((String)meisai.get(prefix + "project_cd")); //（オープン２１）プロジェクトコード
		shiwakeDataRS.setSEG((String)meisai.get(prefix + "segment_cd")); //（オープン２１）セグメントコード
		shiwakeDataRS.setDM1((String)meisai.get(prefix + "uf1_cd")); //（オープン２１）ユニバーサルフィールド１
		shiwakeDataRS.setDM2((String)meisai.get(prefix + "uf2_cd")); //（オープン２１）ユニバーサルフィールド２
		shiwakeDataRS.setDM3((String)meisai.get(prefix + "uf3_cd")); //（オープン２１）ユニバーサルフィールド３
		shiwakeDataRS.setDM4((String)meisai.get(prefix + "uf4_cd")); //（オープン２１）ユニバーサルフィールド４
		shiwakeDataRS.setDM5((String)meisai.get(prefix + "uf5_cd")); //（オープン２１）ユニバーサルフィールド５
		shiwakeDataRS.setDM6((String)meisai.get(prefix + "uf6_cd")); //（オープン２１）ユニバーサルフィールド６
		shiwakeDataRS.setDM7((String)meisai.get(prefix + "uf7_cd")); //（オープン２１）ユニバーサルフィールド７
		shiwakeDataRS.setDM8((String)meisai.get(prefix + "uf8_cd")); //（オープン２１）ユニバーサルフィールド８
		shiwakeDataRS.setDM9((String)meisai.get(prefix + "uf9_cd")); //（オープン２１）ユニバーサルフィールド９
		shiwakeDataRS.setDM10((String)meisai.get(prefix + "uf10_cd")); //（オープン２１）ユニバーサルフィールド１０
		shiwakeDataRS.setRIT(common.judgeShiwakeZeiritsu(kazeiKbn, (BigDecimal)meisai.get("zeiritsu")));//（オープン２１）税率
		shiwakeDataRS.setKEIGEN(common.judgeShiwakeZeiritsuKbn(kazeiKbn, meisai.get("keigen_zeiritsu_kbn"))); //（オープン２１）軽減税率区分
		shiwakeDataRS.setZKB(common.convKazekKbn(kazeiKbn)); //（オープン２１）課税区分
		shiwakeDataRS.setSRE("9".equals(meisai.get(prefix + "shiire_kbn")) ?  "0" : (String)meisai.get(prefix + "shiire_kbn")); //仕入区分
		shiwakeDataRS.setURIZEIKEISAN(!(List.of("0","1").contains(meisai.get(prefix + "zeigaku_houshiki"))) ? "0" : (String)meisai.get(prefix + "zeigaku_houshiki")); //（オープン21）借方　併用売上税額計算方式
		shiwakeDataRS.setMENZEIKEIKA((!(List.of("0","1","2").contains(meisai.get(prefix + "jigyousha_kbn"))) || 
				!((List.of("001","011","002","013").contains(meisai.get(prefix + "kazei_kbn")) && List.of("2","5","6","7","8","10").contains(shoriGroup)) || shoriGroup.equals("21")))
				? "0" : (String)meisai.get(prefix + "jigyousha_kbn")); //（オープン21）借方　仕入税額控除経過措置割合
		
		//課税区分が未設定""だったら、仕入区分も""にする
		if("".equals(shiwakeDataRS.getZKB())) {
			shiwakeDataRS.setSRE("");
		}
		
		return shiwakeDataRS;
	}

	/**
	 * 共通部の作成
	 * @param meisai 伝票+申請本体+明細
	 * @return 仕訳共通
	 */
	public ShiwakeDataCom makeCom(GMap meisai) {
		ShiwakeDataCom c = new ShiwakeDataCom();
		c.setDYMD((Date)meisai.get("denpyou_date")); //（オープン２１）伝票日付
		c.setDCNO(String.format("%1$08d", meisai.getObject("serial_no"))); //（オープン２１）伝票番号
		c.setTKY(common.cutTekiyou((String)meisai.get("tekiyou"))); //（オープン２１）摘要
		c.setTNO(""); //（オープン２１）摘要コード
		c.setVALU((BigDecimal)meisai.get("kingaku")); //（オープン２１）金額 = 振替.金額
		return c;
	}

//※※※日本化学産業用のメソッド・・・バージョンアップすることがあれば下記メソッドの無影響込でテストすべし
	/**
	 * 総合付替通常仕訳データ出力処理拡張オーバーライド用
	 * @param map 伝票-付替-付替明細
	 * @param swkData 仕訳データ
	 */
	protected void makeTsukekaeFutuuSiwakeExt(GMap map, ShiwakeData swkData) {
	}
	
	/**
	 * 総合付替貸方諸口仕訳データ出力処理拡張オーバーライド用
	 * @param map 伝票-付替-付替明細
	 * @param swkData 仕訳データ
	 */
	protected void makeTsukekaeKashiSiwakeExt(GMap map, ShiwakeData swkData) {
	}

	/**
	 * 総合付替借方諸口仕訳データ出力処理拡張オーバーライド用
	 * @param map 伝票-付替-付替明細
	 * @param swkData 仕訳データ
	 */
	protected void makeTsukekaeKariSiwakeExt(GMap map, ShiwakeData swkData) {
	}
	
}
