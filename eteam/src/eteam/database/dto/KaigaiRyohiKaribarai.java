package eteam.database.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import eteam.base.GMap;
import eteam.base.IEteamDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 海外旅費仮払DTO
 * This code was generated by the tool.
 * This code can not be changed.
 * @author CodeGenerator
 */
@Getter @Setter @ToString
public class KaigaiRyohiKaribarai implements IEteamDTO {

	/**
	 * default constructor
	 */
	public KaigaiRyohiKaribarai() {}

	 /**
	 * GMapからDTOを生成
	 * @param m GMap
	 */
	public KaigaiRyohiKaribarai(GMap m) {
		this.denpyouId = m.get("denpyou_id");
		this.karibaraiOn = m.get("karibarai_on");
		this.dairiflg = m.get("dairiflg");
		this.userId = m.get("user_id");
		this.shainNo = m.get("shain_no");
		this.userSei = m.get("user_sei");
		this.userMei = m.get("user_mei");
		this.houmonsaki = m.get("houmonsaki");
		this.mokuteki = m.get("mokuteki");
		this.seisankikanFrom = m.get("seisankikan_from");
		this.seisankikanFromHour = m.get("seisankikan_from_hour");
		this.seisankikanFromMin = m.get("seisankikan_from_min");
		this.seisankikanTo = m.get("seisankikan_to");
		this.seisankikanToHour = m.get("seisankikan_to_hour");
		this.seisankikanToMin = m.get("seisankikan_to_min");
		this.shiharaibi = m.get("shiharaibi");
		this.shiharaikiboubi = m.get("shiharaikiboubi");
		this.shiharaihouhou = m.get("shiharaihouhou");
		this.tekiyou = m.get("tekiyou");
		this.kingaku = m.get("kingaku");
		this.karibaraiKingaku = m.get("karibarai_kingaku");
		this.sashihikiNum = m.get("sashihiki_num");
		this.sashihikiTanka = m.get("sashihiki_tanka");
		this.sashihikiNumKaigai = m.get("sashihiki_num_kaigai");
		this.sashihikiTankaKaigai = m.get("sashihiki_tanka_kaigai");
		this.sashihikiHeishuCdKaigai = m.get("sashihiki_heishu_cd_kaigai");
		this.sashihikiRateKaigai = m.get("sashihiki_rate_kaigai");
		this.sashihikiTankaKaigaiGaika = m.get("sashihiki_tanka_kaigai_gaika");
		this.hf1Cd = m.get("hf1_cd");
		this.hf1NameRyakushiki = m.get("hf1_name_ryakushiki");
		this.hf2Cd = m.get("hf2_cd");
		this.hf2NameRyakushiki = m.get("hf2_name_ryakushiki");
		this.hf3Cd = m.get("hf3_cd");
		this.hf3NameRyakushiki = m.get("hf3_name_ryakushiki");
		this.hf4Cd = m.get("hf4_cd");
		this.hf4NameRyakushiki = m.get("hf4_name_ryakushiki");
		this.hf5Cd = m.get("hf5_cd");
		this.hf5NameRyakushiki = m.get("hf5_name_ryakushiki");
		this.hf6Cd = m.get("hf6_cd");
		this.hf6NameRyakushiki = m.get("hf6_name_ryakushiki");
		this.hf7Cd = m.get("hf7_cd");
		this.hf7NameRyakushiki = m.get("hf7_name_ryakushiki");
		this.hf8Cd = m.get("hf8_cd");
		this.hf8NameRyakushiki = m.get("hf8_name_ryakushiki");
		this.hf9Cd = m.get("hf9_cd");
		this.hf9NameRyakushiki = m.get("hf9_name_ryakushiki");
		this.hf10Cd = m.get("hf10_cd");
		this.hf10NameRyakushiki = m.get("hf10_name_ryakushiki");
		this.hosoku = m.get("hosoku");
		this.shiwakeEdano = m.get("shiwake_edano");
		this.torihikiName = m.get("torihiki_name");
		this.kariFutanBumonCd = m.get("kari_futan_bumon_cd");
		this.kariFutanBumonName = m.get("kari_futan_bumon_name");
		this.torihikisakiCd = m.get("torihikisaki_cd");
		this.torihikisakiNameRyakushiki = m.get("torihikisaki_name_ryakushiki");
		this.kariKamokuCd = m.get("kari_kamoku_cd");
		this.kariKamokuName = m.get("kari_kamoku_name");
		this.kariKamokuEdabanCd = m.get("kari_kamoku_edaban_cd");
		this.kariKamokuEdabanName = m.get("kari_kamoku_edaban_name");
		this.kariKazeiKbn = m.get("kari_kazei_kbn");
		this.kashiFutanBumonCd = m.get("kashi_futan_bumon_cd");
		this.kashiFutanBumonName = m.get("kashi_futan_bumon_name");
		this.kashiKamokuCd = m.get("kashi_kamoku_cd");
		this.kashiKamokuName = m.get("kashi_kamoku_name");
		this.kashiKamokuEdabanCd = m.get("kashi_kamoku_edaban_cd");
		this.kashiKamokuEdabanName = m.get("kashi_kamoku_edaban_name");
		this.kashiKazeiKbn = m.get("kashi_kazei_kbn");
		this.uf1Cd = m.get("uf1_cd");
		this.uf1NameRyakushiki = m.get("uf1_name_ryakushiki");
		this.uf2Cd = m.get("uf2_cd");
		this.uf2NameRyakushiki = m.get("uf2_name_ryakushiki");
		this.uf3Cd = m.get("uf3_cd");
		this.uf3NameRyakushiki = m.get("uf3_name_ryakushiki");
		this.uf4Cd = m.get("uf4_cd");
		this.uf4NameRyakushiki = m.get("uf4_name_ryakushiki");
		this.uf5Cd = m.get("uf5_cd");
		this.uf5NameRyakushiki = m.get("uf5_name_ryakushiki");
		this.uf6Cd = m.get("uf6_cd");
		this.uf6NameRyakushiki = m.get("uf6_name_ryakushiki");
		this.uf7Cd = m.get("uf7_cd");
		this.uf7NameRyakushiki = m.get("uf7_name_ryakushiki");
		this.uf8Cd = m.get("uf8_cd");
		this.uf8NameRyakushiki = m.get("uf8_name_ryakushiki");
		this.uf9Cd = m.get("uf9_cd");
		this.uf9NameRyakushiki = m.get("uf9_name_ryakushiki");
		this.uf10Cd = m.get("uf10_cd");
		this.uf10NameRyakushiki = m.get("uf10_name_ryakushiki");
		this.projectCd = m.get("project_cd");
		this.projectName = m.get("project_name");
		this.segmentCd = m.get("segment_cd");
		this.segmentNameRyakushiki = m.get("segment_name_ryakushiki");
		this.tekiyouCd = m.get("tekiyou_cd");
		this.seisanKanryoubi = m.get("seisan_kanryoubi");
		this.tourokuUserId = m.get("touroku_user_id");
		this.tourokuTime = m.get("touroku_time");
		this.koushinUserId = m.get("koushin_user_id");
		this.koushinTime = m.get("koushin_time");
		this.map = m;
	}

	/** 伝票ID */
	public String denpyouId;

	/** 仮払申請フラグ */
	public String karibaraiOn;

	/** 代理フラグ */
	public String dairiflg;

	/** ユーザーID */
	public String userId;

	/** 社員番号 */
	public String shainNo;

	/** ユーザー姓 */
	public String userSei;

	/** ユーザー名 */
	public String userMei;

	/** 訪問先 */
	public String houmonsaki;

	/** 目的 */
	public String mokuteki;

	/** 精算期間開始日 */
	public Date seisankikanFrom;

	/** 精算期間開始時刻（時） */
	public String seisankikanFromHour;

	/** 精算期間開始時刻（分） */
	public String seisankikanFromMin;

	/** 精算期間終了日 */
	public Date seisankikanTo;

	/** 精算期間終了時刻（時） */
	public String seisankikanToHour;

	/** 精算期間終了時刻（分） */
	public String seisankikanToMin;

	/** 支払日 */
	public Date shiharaibi;

	/** 支払希望日 */
	public Date shiharaikiboubi;

	/** 支払方法 */
	public String shiharaihouhou;

	/** 摘要 */
	public String tekiyou;

	/** 金額 */
	public BigDecimal kingaku;

	/** 仮払金額 */
	public BigDecimal karibaraiKingaku;

	/** 差引回数 */
	public BigDecimal sashihikiNum;

	/** 差引単価 */
	public BigDecimal sashihikiTanka;

	/** 差引回数（海外） */
	public BigDecimal sashihikiNumKaigai;

	/** 差引単価（海外） */
	public BigDecimal sashihikiTankaKaigai;

	/** 差引幣種コード（海外） */
	public String sashihikiHeishuCdKaigai;

	/** 差引レート（海外） */
	public BigDecimal sashihikiRateKaigai;

	/** 差引単価（海外）外貨 */
	public BigDecimal sashihikiTankaKaigaiGaika;

	/** HF1コード */
	public String hf1Cd;

	/** HF1名（略式） */
	public String hf1NameRyakushiki;

	/** HF2コード */
	public String hf2Cd;

	/** HF2名（略式） */
	public String hf2NameRyakushiki;

	/** HF3コード */
	public String hf3Cd;

	/** HF3名（略式） */
	public String hf3NameRyakushiki;

	/** HF4コード */
	public String hf4Cd;

	/** HF4名（略式） */
	public String hf4NameRyakushiki;

	/** HF5コード */
	public String hf5Cd;

	/** HF5名（略式） */
	public String hf5NameRyakushiki;

	/** HF6コード */
	public String hf6Cd;

	/** HF6名（略式） */
	public String hf6NameRyakushiki;

	/** HF7コード */
	public String hf7Cd;

	/** HF7名（略式） */
	public String hf7NameRyakushiki;

	/** HF8コード */
	public String hf8Cd;

	/** HF8名（略式） */
	public String hf8NameRyakushiki;

	/** HF9コード */
	public String hf9Cd;

	/** HF9名（略式） */
	public String hf9NameRyakushiki;

	/** HF10コード */
	public String hf10Cd;

	/** HF10名（略式） */
	public String hf10NameRyakushiki;

	/** 補足 */
	public String hosoku;

	/** 仕訳枝番号 */
	public Integer shiwakeEdano;

	/** 取引名 */
	public String torihikiName;

	/** 借方負担部門コード */
	public String kariFutanBumonCd;

	/** 借方負担部門名 */
	public String kariFutanBumonName;

	/** 取引先コード */
	public String torihikisakiCd;

	/** 取引先名（略式） */
	public String torihikisakiNameRyakushiki;

	/** 借方科目コード */
	public String kariKamokuCd;

	/** 借方科目名 */
	public String kariKamokuName;

	/** 借方科目枝番コード */
	public String kariKamokuEdabanCd;

	/** 借方科目枝番名 */
	public String kariKamokuEdabanName;

	/** 借方課税区分 */
	public String kariKazeiKbn;

	/** 貸方負担部門コード */
	public String kashiFutanBumonCd;

	/** 貸方負担部門名 */
	public String kashiFutanBumonName;

	/** 貸方科目コード */
	public String kashiKamokuCd;

	/** 貸方科目名 */
	public String kashiKamokuName;

	/** 貸方科目枝番コード */
	public String kashiKamokuEdabanCd;

	/** 貸方科目枝番名 */
	public String kashiKamokuEdabanName;

	/** 貸方課税区分 */
	public String kashiKazeiKbn;

	/** UF1コード */
	public String uf1Cd;

	/** UF1名（略式） */
	public String uf1NameRyakushiki;

	/** UF2コード */
	public String uf2Cd;

	/** UF2名（略式） */
	public String uf2NameRyakushiki;

	/** UF3コード */
	public String uf3Cd;

	/** UF3名（略式） */
	public String uf3NameRyakushiki;

	/** UF4コード */
	public String uf4Cd;

	/** UF4名（略式） */
	public String uf4NameRyakushiki;

	/** UF5コード */
	public String uf5Cd;

	/** UF5名（略式） */
	public String uf5NameRyakushiki;

	/** UF6コード */
	public String uf6Cd;

	/** UF6名（略式） */
	public String uf6NameRyakushiki;

	/** UF7コード */
	public String uf7Cd;

	/** UF7名（略式） */
	public String uf7NameRyakushiki;

	/** UF8コード */
	public String uf8Cd;

	/** UF8名（略式） */
	public String uf8NameRyakushiki;

	/** UF9コード */
	public String uf9Cd;

	/** UF9名（略式） */
	public String uf9NameRyakushiki;

	/** UF10コード */
	public String uf10Cd;

	/** UF10名（略式） */
	public String uf10NameRyakushiki;

	/** プロジェクトコード */
	public String projectCd;

	/** プロジェクト名 */
	public String projectName;

	/** セグメントコード */
	public String segmentCd;

	/** セグメント名（略式） */
	public String segmentNameRyakushiki;

	/** 摘要コード */
	public String tekiyouCd;

	/** 精算完了日 */
	public Date seisanKanryoubi;

	/** 登録ユーザーID */
	public String tourokuUserId;

	/** 登録日時 */
	public Timestamp tourokuTime;

	/** 更新ユーザーID */
	public String koushinUserId;

	/** 更新日時 */
	public Timestamp koushinTime;

	/** その他項目map */
	public GMap map;
}
