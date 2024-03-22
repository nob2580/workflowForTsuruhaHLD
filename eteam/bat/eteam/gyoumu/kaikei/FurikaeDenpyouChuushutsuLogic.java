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
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dto.KamokuMaster;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.CommonChuushutsuLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.kaikei.common.ShiwakeDataCom;
import eteam.gyoumu.kaikei.common.ShiwakeDataRS;
import eteam.gyoumu.kaikei.common.ShiwakeLogic;

/**
 * 振替伝票抽出ロジック
 */
public class FurikaeDenpyouChuushutsuLogic extends EteamAbstractLogic  {

	/** 会計共通ロジック */
	protected KaikeiCommonLogic kaikeiCommon;
	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 仕訳ロジック */
	protected ShiwakeLogic shiwakeLogic;
	/** 共通抽出ロジック */
	CommonChuushutsuLogic comChu;
	/** 仕訳ロジック */
	ShiwakeLogic swkLogic;
	/** 科目マスターDao */
	KamokuMasterDao kamokuMasterDao;
	/** 科目マスターDaoから取得したデータ */
	KamokuMaster kmk;
	/** 借方処理グループ */
	String kariShoriGroup;
	/** 貸方処理グループ */
	String kashiShoriGroup;

	/**
	 * 初期化
	 * @param _connection コネクション
	 */
	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		kaikeiCommon = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		shiwakeLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
		comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);
		swkLogic = EteamContainer.getComponent(ShiwakeLogic.class, connection);
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
			+ "INNER JOIN furikae k ON "
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
			+ "INNER JOIN furikae k ON "
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
		
		return connection.load(sql, DENPYOU_KBN.FURIKAE_DENPYOU, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
	}
	
	/**
	 * 借方の作成
	 * @param hontai 伝票+申請本体
	 * @return 借方仕訳
	 */
	public ShiwakeDataRS makeKari(GMap hontai){
		String kariKazeiKbn = (String)hontai.get("kari_kazei_kbn");
		kmk = kamokuMasterDao.find((String)hontai.get("kari_kamoku_cd"));
		if (kmk.shoriGroup != null) {
			kariShoriGroup = kmk.shoriGroup.toString(); //事業者区分の制御で使用
		}
		ShiwakeDataRS r = new ShiwakeDataRS();
		r.setBMN((String)hontai.get("kari_futan_bumon_cd")); //（オープン２１）借方　部門コード
		r.setTOR((String)hontai.get("kari_torihikisaki_cd")); //（オープン２１）借方　取引先コード
		r.setKMK((String)hontai.get("kari_kamoku_cd")); //（オープン２１）借方　科目コード
		r.setEDA((String)hontai.get("kari_kamoku_edaban_cd")); //（オープン２１）借方　枝番コード
		r.setPRJ((String)hontai.get("kari_project_cd")); //（オープン２１）借方　プロジェクトコード
		r.setSEG((String)hontai.get("kari_segment_cd")); //（オープン２１）借方　セグメントコード
		r.setDM1((String)hontai.get("kari_uf1_cd")); //（オープン２１）借方　ユニバーサルフィールド１
		r.setDM2((String)hontai.get("kari_uf2_cd")); //（オープン２１）借方　ユニバーサルフィールド２
		r.setDM3((String)hontai.get("kari_uf3_cd")); //（オープン２１）借方　ユニバーサルフィールド３
		r.setDM4((String)hontai.get("kari_uf4_cd")); //（オープン２１）借方　ユニバーサルフィールド４
		r.setDM5((String)hontai.get("kari_uf5_cd")); //（オープン２１）借方　ユニバーサルフィールド５
		r.setDM6((String)hontai.get("kari_uf6_cd")); //（オープン２１）借方　ユニバーサルフィールド６
		r.setDM7((String)hontai.get("kari_uf7_cd")); //（オープン２１）借方　ユニバーサルフィールド７
		r.setDM8((String)hontai.get("kari_uf8_cd")); //（オープン２１）借方　ユニバーサルフィールド８
		r.setDM9((String)hontai.get("kari_uf9_cd")); //（オープン２１）借方　ユニバーサルフィールド９
		r.setDM10((String)hontai.get("kari_uf10_cd")); //（オープン２１）借方　ユニバーサルフィールド１０
		r.setRIT(common.judgeShiwakeZeiritsu(kariKazeiKbn, (BigDecimal)hontai.get("kari_zeiritsu")));//（オープン２１）借方　税率
		r.setKEIGEN(common.judgeShiwakeZeiritsuKbn(kariKazeiKbn, hontai.get("kari_keigen_zeiritsu_kbn"))); //（オープン２１）借方　軽減税率区分
		r.setZKB(common.convKazekKbn(kariKazeiKbn)); //（オープン２１）借方　課税区分
		r.setSRE("9".equals(hontai.get("kari_shiire_kbn")) ?  "0" : (String)hontai.get("kari_shiire_kbn")); //仕入区分
//		r.setURIZEIKEISAN((String)hontai.get("kari_zeigaku_houshiki")); //（オープン21）借方　併用売上税額計算方式
//		r.setMENZEIKEIKA((String)hontai.get("kari_jigyousha_kbn")); //（オープン21）借方　仕入税額控除経過措置割合
		r.setURIZEIKEISAN(!(List.of("0","1").contains(hontai.get("kari_zeigaku_houshiki"))) ? "0" : (String)hontai.get("kari_zeigaku_houshiki")); //（オープン21）借方　併用売上税額計算方式
		r.setMENZEIKEIKA((!(List.of("0","1","2").contains(hontai.get("kari_jigyousha_kbn"))) || 
				!((List.of("001","011","002","013").contains(hontai.get("kari_kazei_kbn")) && List.of("2","5","6","7","8","10").contains(kariShoriGroup)) || kariShoriGroup.equals("21")))
				? "0" : (String)hontai.get("kari_jigyousha_kbn")); //（オープン21）借方　仕入税額控除経過措置割合
		
		//課税区分が未設定""だったら、仕入区分も""にする
		if("".equals(r.getZKB())) {
			r.setSRE("");
		}
		
		return r;
	}
	
	/**
	 * 貸方の作成
	 * @param hontai 伝票+申請本体
	 * @return 貸方仕訳
	 */
	public ShiwakeDataRS makeKashi(GMap hontai){
		String kashiKazeiKbn = (String)hontai.get("kashi_kazei_kbn");
		kmk = kamokuMasterDao.find((String)hontai.get("kashi_kamoku_cd"));
		if (kmk.shoriGroup != null) {
			kashiShoriGroup = kmk.shoriGroup.toString(); //事業者区分の制御で使用
		}
		ShiwakeDataRS s = new ShiwakeDataRS();
		s.setBMN((String)hontai.get("kashi_futan_bumon_cd")); //（オープン２１）貸方　部門コード
		s.setTOR((String)hontai.get("kashi_torihikisaki_cd")); //（オープン２１）貸方　取引先コード
		s.setKMK((String)hontai.get("kashi_kamoku_cd")); //（オープン２１）貸方　科目コード
		s.setEDA((String)hontai.get("kashi_kamoku_edaban_cd")); //（オープン２１）貸方　枝番コード
		s.setPRJ((String)hontai.get("kashi_project_cd")); //（オープン２１）貸方　プロジェクトコード
		s.setSEG((String)hontai.get("kashi_segment_cd")); //（オープン２１）借方　セグメントコード
		s.setDM1((String)hontai.get("kashi_uf1_cd")); //（オープン２１）貸方　ユニバーサルフィールド１
		s.setDM2((String)hontai.get("kashi_uf2_cd")); //（オープン２１）貸方　ユニバーサルフィールド２
		s.setDM3((String)hontai.get("kashi_uf3_cd")); //（オープン２１）貸方　ユニバーサルフィールド３
		s.setDM4((String)hontai.get("kashi_uf4_cd")); //（オープン２１）貸方　ユニバーサルフィールド４
		s.setDM5((String)hontai.get("kashi_uf5_cd")); //（オープン２１）貸方　ユニバーサルフィールド５
		s.setDM6((String)hontai.get("kashi_uf6_cd")); //（オープン２１）貸方　ユニバーサルフィールド６
		s.setDM7((String)hontai.get("kashi_uf7_cd")); //（オープン２１）貸方　ユニバーサルフィールド７
		s.setDM8((String)hontai.get("kashi_uf8_cd")); //（オープン２１）貸方　ユニバーサルフィールド８
		s.setDM9((String)hontai.get("kashi_uf9_cd")); //（オープン２１）貸方　ユニバーサルフィールド９
		s.setDM10((String)hontai.get("kashi_uf10_cd")); //（オープン２１）貸方　ユニバーサルフィールド１０
		s.setRIT(common.judgeShiwakeZeiritsu(kashiKazeiKbn, (BigDecimal)hontai.get("kashi_zeiritsu")));//（オープン２１）貸方　税率
		s.setKEIGEN(common.judgeShiwakeZeiritsuKbn(kashiKazeiKbn, hontai.get("kashi_keigen_zeiritsu_kbn"))); //（オープン２１）借方　軽減税率区分
		s.setZKB(common.convKazekKbn(kashiKazeiKbn)); //（オープン２１）貸方　課税区分=振替.貸方課税区分（前ゼロ除く）
		s.setSRE("9".equals(hontai.get("kashi_shiire_kbn")) ?  "0" : (String)hontai.get("kashi_shiire_kbn")); //仕入区分
//		s.setURIZEIKEISAN((String)hontai.get("kashi_zeigaku_houshiki")); //（オープン21）貸方　併用売上税額計算方式
//		s.setMENZEIKEIKA((String)hontai.get("kashi_jigyousha_kbn")); //（オープン21）貸方　仕入税額控除経過措置割合
		s.setURIZEIKEISAN(!(List.of("0","1").contains(hontai.get("kashi_zeigaku_houshiki"))) ? "0" : (String)hontai.get("kashi_zeigaku_houshiki")); //（オープン21）貸方　併用売上税額計算方式
		s.setMENZEIKEIKA((!(List.of("0","1","2").contains(hontai.get("kashi_jigyousha_kbn"))) ||
				!((List.of("001","011","002","013").contains(hontai.get("kashi_kazei_kbn")) && List.of("2","5","6","7","8","10").contains(kashiShoriGroup)) || kashiShoriGroup.equals("21")))
				? "0" : (String)hontai.get("kashi_jigyousha_kbn")); //（オープン21）貸方　仕入税額控除経過措置割合
		return s;
	}

	/**
	 * 共通部の作成
	 * @param denpyou 伝票+申請本体
	 * @return 仕訳共通
	 */
	public ShiwakeDataCom makeCom(GMap denpyou) {
		ShiwakeDataCom c = new ShiwakeDataCom();
		c.setDYMD((Date)denpyou.get("denpyou_date")); //（オープン２１）伝票日付
		c.setDCNO(String.format("%1$08d", denpyou.getObject("serial_no"))); //（オープン２１）伝票番号
		c.setTKY(common.cutTekiyou((String)denpyou.get("tekiyou"))); //（オープン２１）摘要
		c.setTNO(""); //（オープン２１）摘要コード
		c.setVALU((BigDecimal)denpyou.get("kingaku")); //（オープン２１）金額 = 振替.金額
		return c;
	}

//※※※日本化学産業用のメソッド・・・バージョンアップすることがあれば下記メソッドの無影響込でテストすべし
	/**
	 * 振替借方諸口仕訳データ作成拡張オーバーライド用
	 * @param hontai 振替伝票
	 * @param swkData 仕訳データ
	 */
	protected void makeFurikaeTuujyouSiwakeExt(GMap hontai, ShiwakeData swkData) {
	}

	/**
	 * 振替貸方諸口仕訳データ作成拡張オーバーライド用
	 * @param hontai 振替伝票
	 * @param swkData 仕訳データ
	 */
	protected void makeFurikaeKashiSiwakeExt(GMap hontai, ShiwakeData swkData) {
	}

	/**
	 * 振替借方諸口仕訳データ作成拡張オーバーライド用
	 * @param hontai 振替伝票
	 * @param swkData 仕訳データ
	 */
	protected void makeFurikaeKariSiwakeExt(GMap hontai, ShiwakeData swkData) {
	}
}
