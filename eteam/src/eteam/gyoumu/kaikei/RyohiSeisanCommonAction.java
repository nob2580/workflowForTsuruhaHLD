package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KAZEI_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.dto.KamokuMaster;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 国内・海外旅費共通Action
 * @author j_matsumoto
 */
@Getter @Setter @ToString
public class RyohiSeisanCommonAction extends RyohiKoutsuuhiSeisanCommonAction {
	
	/** 海外出張精算か？ */
	boolean isKaigai;
	
	//＜画面入力＞
	//画面入力（申請内容）
	/** 仮払伝票ID */
	String karibaraiDenpyouId;
	/** 仮払あり・なし */
	String karibaraiOn;
	/** 仮払摘要 */
	String karibaraiTekiyou;
	/** 申請金額 */
	String karibaraiShinseiKingaku;
	/** 仮払金額 */
	String karibaraiKingaku;
	/** 仮払金額差額 */
	String karibaraiKingakuSagaku;
	/** 仮払未使用フラグ */
	String karibaraiMishiyouFlg;
	/** 出張中止フラグ */
	String shucchouChuushiFlg;
	/** 使用者ID */
	String userIdRyohi;
	/** 使用者名前 */
	String userNameRyohi;
	/** 社員番号 */
	String shainNoRyohi;
	/** 出張先・訪問先 */
	String houmonsaki;
	/** 内法人カード利用合計 */
	String houjinCardRiyouGoukei;
	/** 会社手配合計 */
	String kaishaTehaiGoukei;
	/** 差引支給金額 */
	String sashihikiShikyuuKingaku;
	/** 共通部分交際費注記 */
	String chuukiKousai1;
	/** 差引回数 */
	String sashihikiNum;
	/** 差引単価 */
	String sashihikiTanka;
	/** 差引金額 */
	String sashihikiKingaku;
	
	//画面入力（明細/旅費）
	/** 休日日数 */
	String[] kyuujitsuNissuu;
	/** 日当フラグ */
	String[] nittouFlg;
	
	//画面入力（明細/経費）
	/** 仕訳枝番号 */
	String[] shiwakeEdaNo;
	/** 取引名 */
	String[] torihikiName;
	/** 勘定科目コード */
	String[] kamokuCd;
	/** 勘定科目名 */
	String[] kamokuName;
	/** 勘定科目枝番コード */
	String[] kamokuEdabanCd;
	/** 勘定科目枝番名 */
	String[] kamokuEdabanName;
	/** 負担部門コード */
	String[] futanBumonCd;
	/** 負担部門名 */
	String[] futanBumonName;
	/** 取引先コード */
	String[] torihikisakiCd;
	/** 取引先名 */
	String[] torihikisakiName;
	/** プロジェクトコード */
	String[] projectCd;
	/** プロジェクト名 */
	String[] projectName;
	/** セグメントコード */
	String[] segmentCd;
	/** セグメント名 */
	String[] segmentName;
	/** ユニバーサルフィールド１コード */
	String[] uf1Cd;
	/** ユニバーサルフィールド１名 */
	String[] uf1Name;
	/** ユニバーサルフィールド２コード */
	String[] uf2Cd;
	/** ユニバーサルフィールド２名 */
	String[] uf2Name;
	/** ユニバーサルフィールド３コード */
	String[] uf3Cd;
	/** ユニバーサルフィールド３名 */
	String[] uf3Name;
	/** ユニバーサルフィールド４コード */
	String[] uf4Cd;
	/** ユニバーサルフィールド４名 */
	String[] uf4Name;
	/** ユニバーサルフィールド５コード */
	String[] uf5Cd;
	/** ユニバーサルフィールド５名 */
	String[] uf5Name;
	/** ユニバーサルフィールド６コード */
	String[] uf6Cd;
	/** ユニバーサルフィールド６名 */
	String[] uf6Name;
	/** ユニバーサルフィールド７コード */
	String[] uf7Cd;
	/** ユニバーサルフィールド７名 */
	String[] uf7Name;
	/** ユニバーサルフィールド８コード */
	String[] uf8Cd;
	/** ユニバーサルフィールド８名 */
	String[] uf8Name;
	/** ユニバーサルフィールド９コード */
	String[] uf9Cd;
	/** ユニバーサルフィールド９名 */
	String[] uf9Name;
	/** ユニバーサルフィールド１０コード */
	String[] uf10Cd;
	/** ユニバーサルフィールド１０名 */
	String[] uf10Name;

	/** 課税区分 */
	String[] kazeiKbn;
	/** 消費税率 */
	String[] zeiritsu;
	/** 軽減税率区分 */
	String[] keigenZeiritsuKbn;
	/** 使用日 */
	String[] shiyoubi;
	/** 証憑 */
	String[] shouhyouShorui;
	/** 支払金額 */
	String[] shiharaiKingaku;
	/** 本体金額 */
	String[] hontaiKingaku;
	/** 消費税額 */
	String[] shouhizeigaku;
	/** 法人カード利用フラグ */
	String[] houjinCardFlgKeihi;
	/** 会社手配フラグ */
	String[] kaishaTehaiFlgKeihi;
	/** 摘要 */
	String[] tekiyou;
	/** 伝票摘要注記 */
	String[] chuuki2;
	/** 伝票交際費注記 */
	String[] chuukiKousai2;
	/** 交際費表示フラグ */
	String[] kousaihiHyoujiFlg;
	/** 人数項目表示フラグ */
	String[] ninzuuRiyouFlg;
	/** 消交際費詳細 */
	String[] kousaihiShousai;
	/** 交際費人数 */
	String[] kousaihiNinzuu;
	/** 交際費一人あたり金額 */
	String[] kousaihiHitoriKingaku;
	/** 法人カード履歴紐付番号(経費) */
	String[] himodukeCardMeisaiKeihi;
	/** 支払先名（経費） */
	String[] shiharaisakiNameKeihi;
	/** 事業者区分（経費） */
	String[] jigyoushaKbnKeihi;
	/** 分離区分（経費） */
	String[] bunriKbnKeihi;
	/** 借方仕入区分（経費） */
	String[] kariShiireKbnKeihi;
	/** 貸方仕入区分（経費） */
	String[] kashiShiireKbnKeihi;
	
	//＜画面入力以外＞

	//プルダウン等の候補値
	/** 消費税率DropDownList */
	List<Shouhizeiritsu> zeiritsuList;
	/** 消費税率ドメイン */
	String[] zeiritsuDomain;
	/**	税込or税抜ならtrue */
	boolean[] kazeiKbnCheck;
	
	/** 領収書・請求書等デフォルト値（旅費） */
	String ryoushuushoSeikyuushoDefaultRyohi;
	/** 領収書・請求書等デフォルト値（その他経費） */
	String ryoushuushoSeikyuushoDefault = setting.shouhyouShoruiDefaultA001();

	/** 海外フラグ */
	String[] kaigaiFlg;
	/** 借方取引先コード（経費） */
	String[] kariTorihikisakiCd;
	/** 借方　UF1コード（経費） */
	String[] kariUf1Cd;
	/** 借方　UF2コード（経費） */
	String[] kariUf2Cd;
	/** 借方　UF3コード（経費） */
	String[] kariUf3Cd;
	/** 借方　UF4コード（経費） */
	String[] kariUf4Cd;
	/** 借方　UF5コード（経費） */
	String[] kariUf5Cd;
	/** 借方　UF6コード（経費） */
	String[] kariUf6Cd;
	/** 借方　UF7コード（経費） */
	String[] kariUf7Cd;
	/** 借方　UF8コード（経費） */
	String[] kariUf8Cd;
	/** 借方　UF9コード（経費） */
	String[] kariUf9Cd;
	/** 借方　UF10コード（経費） */
	String[] kariUf10Cd;

	/** 貸方取引先コード（経費） */
	String[] kashiTorihikisakiCd;
	/** 貸方負担部門コード（経費） */
	String[] kashiFutanBumonCd;
	/** 貸方負担部門名（経費） */
	String[] kashiFutanBumonName;
	/** 貸方科目コード（経費） */
	String[] kashiKamokuCd;
	/** 貸方科目名（経費） */
	String[] kashiKamokuName;
	/** 貸方科目枝番コード（経費） */
	String[] kashiKamokuEdabanCd;
	/** 貸方科目枝番名（経費） */
	String[] kashiKamokuEdabanName;
	/** 貸方課税区分（経費） */
	String[] kashiKazeiKbn;
	/** 貸方1　UF1コード（経費） */
	String[] kashiUf1Cd1;
	/** 貸方1　UF2コード（経費） */
	String[] kashiUf2Cd1;
	/** 貸方1　UF3コード（経費） */
	String[] kashiUf3Cd1;
	/** 貸方1　UF4コード（経費） */
	String[] kashiUf4Cd1;
	/** 貸方1　UF5コード（経費） */
	String[] kashiUf5Cd1;
	/** 貸方1　UF6コード（経費） */
	String[] kashiUf6Cd1;
	/** 貸方1　UF7コード（経費） */
	String[] kashiUf7Cd1;
	/** 貸方1　UF8コード（経費） */
	String[] kashiUf8Cd1;
	/** 貸方1　UF9コード（経費） */
	String[] kashiUf9Cd1;
	/** 貸方1　UF10コード（経費） */
	String[] kashiUf10Cd1;
	/** 貸方2　UF1コード（経費） */
	String[] kashiUf1Cd2;
	/** 貸方2　UF2コード（経費） */
	String[] kashiUf2Cd2;
	/** 貸方2　UF3コード（経費） */
	String[] kashiUf3Cd2;
	/** 貸方2　UF4コード（経費） */
	String[] kashiUf4Cd2;
	/** 貸方2　UF5コード（経費） */
	String[] kashiUf5Cd2;
	/** 貸方2　UF6コード（経費） */
	String[] kashiUf6Cd2;
	/** 貸方2　UF7コード（経費） */
	String[] kashiUf7Cd2;
	/** 貸方2　UF8コード（経費） */
	String[] kashiUf8Cd2;
	/** 貸方2　UF9コード（経費） */
	String[] kashiUf9Cd2;
	/** 貸方2　UF10コード（経費） */
	String[] kashiUf10Cd2;
	/** 貸方3　UF1コード（経費） */
	String[] kashiUf1Cd3;
	/** 貸方3　UF2コード（経費） */
	String[] kashiUf2Cd3;
	/** 貸方3　UF3コード（経費） */
	String[] kashiUf3Cd3;
	/** 貸方3　UF4コード（経費） */
	String[] kashiUf4Cd3;
	/** 貸方3　UF5コード（経費） */
	String[] kashiUf5Cd3;
	/** 貸方3　UF6コード（経費） */
	String[] kashiUf6Cd3;
	/** 貸方3　UF7コード（経費） */
	String[] kashiUf7Cd3;
	/** 貸方3　UF8コード（経費） */
	String[] kashiUf8Cd3;
	/** 貸方3　UF9コード（経費） */
	String[] kashiUf9Cd3;
	/** 貸方3　UF10コード（経費） */
	String[] kashiUf10Cd3;
	/** 貸方4　UF1コード（経費） */
	String[] kashiUf1Cd4;
	/** 貸方4　UF2コード（経費） */
	String[] kashiUf2Cd4;
	/** 貸方4　UF3コード（経費） */
	String[] kashiUf3Cd4;
	/** 貸方4　UF4コード（経費） */
	String[] kashiUf4Cd4;
	/** 貸方4　UF5コード（経費） */
	String[] kashiUf5Cd4;
	/** 貸方4　UF6コード（経費） */
	String[] kashiUf6Cd4;
	/** 貸方4　UF7コード（経費） */
	String[] kashiUf7Cd4;
	/** 貸方4　UF8コード（経費） */
	String[] kashiUf8Cd4;
	/** 貸方4　UF9コード（経費） */
	String[] kashiUf9Cd4;
	/** 貸方4　UF10コード（経費） */
	String[] kashiUf10Cd4;
	/** 貸方5　UF1コード（経費） */
	String[] kashiUf1Cd5;
	/** 貸方5　UF2コード（経費） */
	String[] kashiUf2Cd5;
	/** 貸方5　UF3コード（経費） */
	String[] kashiUf3Cd5;
	/** 貸方5　UF4コード（経費） */
	String[] kashiUf4Cd5;
	/** 貸方5　UF5コード（経費） */
	String[] kashiUf5Cd5;
	/** 貸方5　UF6コード（経費） */
	String[] kashiUf6Cd5;
	/** 貸方5　UF7コード（経費） */
	String[] kashiUf7Cd5;
	/** 貸方5　UF8コード（経費） */
	String[] kashiUf8Cd5;
	/** 貸方5　UF9コード（経費） */
	String[] kashiUf9Cd5;
	/** 貸方5　UF10コード（経費） */
	String[] kashiUf10Cd5;
	/** 摘要コード（経費） */
	String[] tekiyouCd;
	

	/** 明細追加用伝票ID */
	String meisaiTsuikaDenpyouId;

	//画面制御情報
	/**  画面項目制御クラス（経費明細） */
	GamenKoumokuSeigyo ks1 = new GamenKoumokuSeigyo(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
	/**  画面項目制御クラス(旅費仮払) */
	GamenKoumokuSeigyo ksKari;

	// 会社設定情報
	/** 差引項目名称 */
	String sashihikiName = setting.sashihikiName();
	/** 差引単価 */
	String sashihikiTankaSI = setting.sashihikiTanka();

	/** 仮払画面が有効であるかどうか */
	boolean karibaraiIsEnabled = false;
	/** 代理起票フラグ（起票者のみ選択=0に固定）*/
	String dairiFlg = "0";
	/** 差引項目表示フラグ */
	boolean sasihikiHyoujiFlg = isNotEmpty(sashihikiName);
	/** 仮払申請の起案添付済みフラグ */
	String karibaraiKianTenpuZumiFlg;

	/** 使用者変更時の社員番号(使用者変更時のAction再呼出時に値が入る) */
	String shiyoushaHenkouShainNo = "";

	//明細単位制御情報
	/** 勘定科目枝番選択ボタン押下可否 */
	boolean[] kamokuEdabanEnable;
	/** 負担部門選択ボタン */
	boolean[] futanBumonEnable;
	/** 取引先選択ボタン */
	boolean[] torihikisakiEnable;
	/** プロジェクト選択ボタン */
	boolean[] projectEnable;
	/** セグメント選択ボタン */
	boolean[] segmentEnable;
	/** 交際費表示 */
	boolean[] kousaihiEnable;
	/** 人数項目表示 */
	boolean[] ninzuuEnable;
	/** UF1ボタン選択ボタン */
	boolean[] uf1Enable;
	/** UF2ボタン選択ボタン */
	boolean[] uf2Enable;
	/** UF3ボタン選択ボタン */
	boolean[] uf3Enable;
	/** UF4ボタン選択ボタン */
	boolean[] uf4Enable;
	/** UF5ボタン選択ボタン */
	boolean[] uf5Enable;
	/** UF6ボタン選択ボタン */
	boolean[] uf6Enable;
	/** UF7ボタン選択ボタン */
	boolean[] uf7Enable;
	/** UF8ボタン選択ボタン */
	boolean[] uf8Enable;
	/** UF9ボタン選択ボタン */
	boolean[] uf9Enable;
	/** UF10ボタン選択ボタン */
	boolean[] uf10Enable;
	/** 消費税率入力可否 */
	boolean[] zeiritsuEnable;
	/** セキュリティパターンで使用できる部門かどうか */
	boolean[] enableMeisaiBumonSecurity;

//＜部品＞
	/** 起票ナビカテゴリー */
	KihyouNaviCategoryLogic kihyouLogic;
	/** バッチ会計 */
	BatchKaikeiCommonLogic batchKaikeiLogic;
	/** 科目枝番残高マスタ */
	KamokuEdabanZandakaAbstractDao edabanZandaka;
	
	//＜親子制御＞
	/**
	 * initの処理が進む前に、必要なプロパティの初期化を行う。
	 */
	@Override
	protected void setDefaultKobetsuDenpyouProperties()
	{
		this.isKaigai = denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN);
		this.ryoushuushoSeikyuushoDefaultRyohi = isKaigai ? setting.shouhyouShoruiDefaultA011() : setting.shouhyouShoruiDefaultA004();
		this.hasseiShugi = isKaigai ? !"3".equals(setting.shiwakeSakuseiHouhouA011()) : !"3".equals(setting.shiwakeSakuseiHouhouA004());
		this.keijoubiDefaultSettei = EteamSettingInfo.getSettingInfo(isKaigai ? EteamSettingInfo.Key.KEIJOUBI_DEFAULT_A011 : EteamSettingInfo.Key.KEIJOUBI_DEFAULT_A004);
		this.keijoubiSeigen = isKaigai ? setting.kaigairyohiseisanKeijouSeigen().equals("1") : setting.ryohiseisanKeijouSeigen().equals("1");
		this.ks = new GamenKoumokuSeigyo(denpyouKbn);
		this.ksKari = new GamenKoumokuSeigyo(DENPYOU_KBN.getKaribaraiDenpyouKbn(denpyouKbn));
		this.enableNittou = true;
	}
	
	/**
	 * その他経費の表示有無
	 * @return その他経費
	 */
	public boolean getSonotaKeihiView() {
		return true;
	}
	
	/**
	 * 共通項目フォーマットチェック（ID～合計金額）
	 */
	protected void commonDenpyouFormatCheckBase()
	{
		// 明細必須フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		//画面入力（申請内容）
		// 項目										//項目名											//キー項目？
		checkString (karibaraiDenpyouId, 1, 19, ks.karibaraiDenpyouId.getName(), false);
		checkDomain (karibaraiOn, Domain.FLG, ks.karibaraiOn.getName(), false);
		checkString (karibaraiTekiyou, 1, this.getIntTekiyouMaxLength(), ks.karibaraiTekiyou.getName(), false);
		checkKingakuOver0 (karibaraiShinseiKingaku, ks.shinseiKingaku.getName(), false);
		//姓名の間に空白が入るので21文字
		checkString (userNameRyohi, 1, 21, ks.userName.getName() + "名", false);
		checkString (shainNoRyohi, 1, 15, ks.userName.getName() + "社員番号", false);
		checkString(hf1Cd, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString(hf1Name, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString(hf2Cd, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString(hf2Name, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString(hf3Cd, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString(hf3Name, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString(hf4Cd, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString(hf4Name, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString(hf5Cd, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString(hf5Name, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString(hf6Cd, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString(hf6Name, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString(hf7Cd, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString(hf7Name, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString(hf8Cd, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString(hf8Name, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString(hf9Cd, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString(hf9Name, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString(hf10Cd, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkString(hf10Name, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkString (houmonsaki,1 ,200 , ks.houmonsaki.getName(), false);
		checkString (mokuteki,1 ,240 , ks.mokuteki.getName(), false);
		checkDomain (shiharaihouhou, shiharaihouhouDomain, ks.shiharaiHouhou.getName(), false);
		checkDate (shiharaiKiboubi, ks.shiharaiKiboubi.getName() , false);
		checkDate (seisankikanFrom, ks.seisankikan.getName() + "開始日", false);
		checkHour (seisankikanFromHour, ks.seisankikanJikoku.getName() + "開始（時）",false);
		checkMin (seisankikanFromMin, ks.seisankikanJikoku.getName() + "開始（分）",false);
		checkDate (seisankikanTo, ks.seisankikan.getName() + "終了日", false);
		checkHour (seisankikanToHour, ks.seisankikanJikoku.getName() + "終了（時）",false);
		checkMin (seisankikanToMin, ks.seisankikanJikoku.getName() + "終了（分）",false);
		checkDomain (zeiritsuRyohi, zeiritsuRyohiDomain, ks.zeiritsu.getName(), false);

		if (keihiEntryFlg) {
			checkKingakuOver1 (kingaku, ks.goukeiKingaku.getName(), false);
		} else {
			checkKingakuOver0 (kingaku, ks.goukeiKingaku.getName(), false);
		}

		checkKingakuOver0 (houjinCardRiyouGoukei, ks.uchiHoujinCardRiyouGoukei.getName(), false);
		checkKingakuOver0 (kaishaTehaiGoukei, ks.kaishaTehaiGoukei.getName(), false);
		checkDomain(this.invoiceDenpyou, this.invoiceDenpyouDomain, "インボイス対応伝票", false);
	}
	
	/**
	 * 国内旅費分伝票共通部フォーマットチェック
	 * @param isKaigaiRyohiSeisan 海外旅費精算である
	 */
	protected void commonKokunaiRyohiCheck(boolean isKaigaiRyohiSeisan)
	{
		String prefix = isKaigaiRyohiSeisan ? "国内" : "";
		checkString(shiwakeEdaNoRyohi, 1, 10, prefix + ks.torihiki.getName() + "コード", false);
		checkString(torihikiNameRyohi, 1, 20, prefix + ks.torihiki.getName() + "名", false);
		checkString(kamokuCdRyohi, 1, 6, prefix + ks.kamoku.getName() + "コード", false);
		checkString(kamokuNameRyohi, 1, 22, prefix + ks.kamoku.getName() + "名", false);
		checkString(kamokuEdabanCdRyohi, 1, 12, prefix + ks.kamokuEdaban.getName() + "コード", false);
		checkString(kamokuEdabanNameRyohi, 1, 20, prefix + ks.kamokuEdaban.getName() + "名", false);
		checkString(futanBumonCdRyohi, 1, 8, prefix + ks.futanBumon.getName() + "コード", false);
		checkString(futanBumonNameRyohi, 1, 20, prefix + ks.futanBumon.getName() + "名", false);
		checkString(torihikisakiCdRyohi, 1, 12, prefix + ks.torihikisaki.getName() + "コード", false);
		checkString(torihikisakiNameRyohi, 1, 20, prefix + ks.torihikisaki.getName() + "名", false);
		checkString(projectCdRyohi, 1, 12, prefix + ks.project.getName() + "コード", false);
		checkString(projectNameRyohi, 1, 20, prefix + ks.project.getName() + "名", false);
		checkString(segmentCdRyohi, 1, 8, prefix + ks.segment.getName() + "コード", false);
		checkString(segmentNameRyohi, 1, 20, prefix + ks.segment.getName() + "名", false);
		checkString(uf1CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf1Name(), false);
		checkString(uf1NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf1Name(), false);
		checkString(uf2CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf2Name(), false);
		checkString(uf2NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf2Name(), false);
		checkString(uf3CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf3Name(), false);
		checkString(uf3NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf3Name(), false);
		checkString(uf4CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf4Name(), false);
		checkString(uf4NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf4Name(), false);
		checkString(uf5CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf5Name(), false);
		checkString(uf5NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf5Name(), false);
		checkString(uf6CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf6Name(), false);
		checkString(uf6NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf6Name(), false);
		checkString(uf7CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf7Name(), false);
		checkString(uf7NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf7Name(), false);
		checkString(uf8CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf8Name(), false);
		checkString(uf8NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf8Name(), false);
		checkString(uf9CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf9Name(), false);
		checkString(uf9NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf9Name(), false);
		checkString(uf10CdRyohi, 1, 20, prefix + hfUfSeigyo.getUf10Name(), false);
		checkString(uf10NameRyohi, 1, 20, prefix + hfUfSeigyo.getUf10Name(), false);
		checkString(tekiyouRyohi, 1, this.getIntTekiyouMaxLength(), prefix + ks.tekiyou.getName(), false);
		checkSJIS(tekiyouRyohi, prefix + ks.tekiyou.getName());
		checkDate (shiharaibi, "支払日", false);
		checkDate (keijoubi, "計上日", false);
		checkDomain(this.bunriKbn, this.bunriKbnDomain, prefix + ks.bunriKbn.getName(), false);
		checkDomain(this.kariShiireKbn, this.shiireKbnDomain, prefix + ks.shiireKbn.getName(), false);
	}
	
	/**
	 * 旅費明細フォーマットチェック共通部
	 * @param i 番号
	 * @param isKaigaiMeisai 海外旅費の海外明細か
	 * @param koutsuuShudanDomainForCheck 交通手段ドメイン
	 */
	protected void commonRyohiMeisaiFormatCheck(int i, boolean isKaigaiMeisai, String[] koutsuuShudanDomainForCheck)
	{
		String ip = makeMeisaiHeader(i);
		checkString (shubetsuCd[i],1 , 1, "種別コード:" + ip + "行目", false);
		checkString (shubetsu1[i], 1, 20, ks.shubetsu1.getName()  + ":" + ip + "行目", false);
		checkString (shubetsu2[i], 1, 20, ks.shubetsu2.getName() + ":" + ip + "行目", false);
		checkDate (kikanFrom[i], ks.kikan.getName() + "開始日" + ":" + ip + "行目", false);
		checkDate (kikanTo[i], ks.kikan.getName() + "終了日" + ":" + ip + "行目", false);
		if (shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.SONOTA)) {
			checkNissuu0 (kyuujitsuNissuu[i], ks.kyuujitsuNissuu.getName()  + ":" + ip + "行目", false);
		}
		checkDomain (koutsuuShudan[i], koutsuuShudanDomainForCheck, ks.koutsuuShudan.getName()  + ":" + ip + "行目", false);
		checkDomain (ryoushuushoSeikyuushoTouFlg[i], Domain.FLG, ks.ryoushuushoSeikyuushoTouFlg.getName()  + ":" + ip + "行目", false);
		if (shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)) {
			checkString (naiyou[i],1 ,512 , ks.naiyouKoutsuuhi.getName()  + ":" + ip + "行目", false);
		}else {
			checkString (naiyou[i],1 ,512 , ks.naiyouNittou.getName()  + ":" + ip + "行目", false);
			checkString (bikou[i],1 ,200 , ks.bikouNittou.getName()  + ":" + ip + "行目", false);
		}
		checkDomain (oufukuFlg[i], Domain.FLG, ks.oufukuFlg.getName()  + ":" + ip + "行目", false);
		checkDomain (houjinCardFlgRyohi[i], Domain.FLG, ks.houjinCardRiyou.getName()  + "フラグ:" + ip + "行目", false);
		checkDomain (kaishaTehaiFlgRyohi[i], Domain.FLG, ks.kaishaTehai.getName()  + "フラグ:" + ip + "行目", false);
		checkDomain (jidounyuuryokuFlg[i], Domain.FLG, "自動入力フラグ:" + ip + "行目", false);
		//早安楽
		checkDomain (hayaFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(早フラグ):" + ip + "行目", false);
		checkDomain (yasuFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(安フラグ):" + ip + "行目", false);
		checkDomain (rakuFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(楽フラグ):" + ip + "行目", false);
		checkNissuu (nissuu[i], ks.nissuu.getName()  + ":" + ip + "行目", false);
		if (isKaigaiMeisai) {
			this.checkKaigaiMeisaiKoumokuFormat(i, ip);
		}
		if(shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)){
			//交通費明細の0円入力に対応 仮払有無に関わらず0円を許可
			if (!isKaigaiMeisai) {
				if(StringUtils.isNotEmpty(suuryouNyuryokuType[i]) && !suuryouNyuryokuType[i].equals("0")) {
					checkKingaku3thDecimalPlaceOver0 (tanka[i], ks.tanka.getName()  + ":" + ip + "行目", false);
					checkDomain (suuryouNyuryokuType[i], suuryouNyuryokuTypeDomain, "数量入力タイプ:" + ip + "行目", false);
					checkKingakuDecimal (suuryou[i], "0.01", "999999999999.99", "数量:" + ip + "行目", false);
					checkString (suuryouKigou[i], 1, 5, "数量記号:" + ip + "行目", false);
				}else {
					checkKingakuOver0 (tanka[i], ks.tanka.getName()  + ":" + ip + "行目", false);
					checkDomain (suuryouNyuryokuType[i], suuryouNyuryokuTypeDomain, "数量入力タイプ:" + ip + "行目", false);
				}
			}else {
				checkKingakuOver0 (tanka[i], ks.tanka.getName()  + ":" + ip + "行目", false);
			}
		}else{
			checkKingakuOver1 (tanka[i], ks.tanka.getName()  + ":" + ip + "行目", false);
			checkKingakuOver1 (meisaiKingaku[i], ks.meisaiKingaku.getName()  + ":" + ip + "行目", false);
		}
		checkString(this.shiharaisakiNameRyohi[i], 1, 60, ks.shiharaisaki.getName() +  "：" + ip + "行目",false);
		checkDomain(this.jigyoushaKbnRyohi[i], this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName() +  "：" + ip + "行目",false);
		checkKingakuMinus (this.shouhizeigakuRyohi[i], ks.shouhizeigaku.getName()  +  "："  + ip + "行目",false);
	}
	
	/**
	 * 経費明細共通部チェック
	 * @param i 配列番号
	 * @param ip 名称
	 */
	protected void commonKeihiMeisaiFormatCheck(int i, String ip)
	{
		checkDate (shiyoubi[i], ks1.shiyoubi.getName() + "：" + ip + "行目", false);
		checkDomain (shouhyouShorui[i],EteamConst.Domain.FLG,ks1.shouhyouShoruiFlg.getName() + "：" + ip + "行目", false);
		checkNumber (shiwakeEdaNo[i], 1, 10, ks1.torihiki.getName() + "コード：" + ip + "行目", false);
		checkString (torihikiName[i], 1, 20, ks1.torihiki.getName() + "名：" + ip + "行目", false);
		checkString (kamokuCd[i], 1, 6, ks1.kamoku.getName() + "コード：" + ip + "行目", false);
		checkString (kamokuName[i], 1, 22, ks1.kamoku.getName() + "名：" + ip + "行目", false);
		checkString (kamokuEdabanCd[i], 1, 12, ks1.kamokuEdaban.getName() + "コード：" + ip + "行目", false);
		checkString (kamokuEdabanName[i], 1, 20, ks1.kamokuEdaban.getName() + "名：" + ip + "行目", false);
		checkString (futanBumonCd[i], 1, 8, ks1.futanBumon.getName() + "コード：" + ip + "行目", false);
		checkString (futanBumonName[i], 1, 20, ks1.futanBumon.getName() + "名：" + ip + "行目", false);
		checkString (torihikisakiCd[i], 1, 12, ks1.torihikisaki.getName() + "コード：" + ip + "行目", false);
		checkString (torihikisakiName[i], 1, 20, ks1.torihikisaki.getName() + "名：" + ip + "行目", false);
		checkString (projectCd[i], 1, 12, ks1.project.getName() + "コード：" + ip + "行目", false);
		checkString (projectName[i], 1, 20, ks1.project.getName() + "名：" + ip + "行目", false);
		checkString (segmentCd[i], 1, 8, ks1.segment.getName() + "コード：" + ip + "行目", false);
		checkString (segmentName[i], 1, 20, ks1.segment.getName() + "名：" + ip + "行目", false);
		checkString (uf1Cd[i], 1, 20, hfUfSeigyo.getUf1Name() + "：" + ip + "行目", false);
		checkString (uf1Name[i], 1, 20, hfUfSeigyo.getUf1Name() + "：" + ip + "行目", false);
		checkString (uf2Cd[i], 1, 20, hfUfSeigyo.getUf2Name() + "：" + ip + "行目", false);
		checkString (uf2Name[i], 1, 20, hfUfSeigyo.getUf2Name() + "：" + ip + "行目", false);
		checkString (uf3Cd[i], 1, 20, hfUfSeigyo.getUf3Name() + "：" + ip + "行目", false);
		checkString (uf3Name[i], 1, 20, hfUfSeigyo.getUf3Name() + "：" + ip + "行目", false);
		checkString (uf4Cd[i], 1, 20, hfUfSeigyo.getUf4Name() + "：" + ip + "行目", false);
		checkString (uf4Name[i], 1, 20, hfUfSeigyo.getUf4Name() + "：" + ip + "行目", false);
		checkString (uf5Cd[i], 1, 20, hfUfSeigyo.getUf5Name() + "：" + ip + "行目", false);
		checkString (uf5Name[i], 1, 20, hfUfSeigyo.getUf5Name() + "：" + ip + "行目", false);
		checkString (uf6Cd[i], 1, 20, hfUfSeigyo.getUf6Name() + "：" + ip + "行目", false);
		checkString (uf6Name[i], 1, 20, hfUfSeigyo.getUf6Name() + "：" + ip + "行目", false);
		checkString (uf7Cd[i], 1, 20, hfUfSeigyo.getUf7Name() + "：" + ip + "行目", false);
		checkString (uf7Name[i], 1, 20, hfUfSeigyo.getUf7Name() + "：" + ip + "行目", false);
		checkString (uf8Cd[i], 1, 20, hfUfSeigyo.getUf8Name() + "：" + ip + "行目", false);
		checkString (uf8Name[i], 1, 20, hfUfSeigyo.getUf8Name() + "：" + ip + "行目", false);
		checkString (uf9Cd[i], 1, 20, hfUfSeigyo.getUf9Name() + "：" + ip + "行目", false);
		checkString (uf9Name[i], 1, 20, hfUfSeigyo.getUf9Name() + "：" + ip + "行目", false);
		checkString (uf10Cd[i], 1, 20, hfUfSeigyo.getUf10Name() + "：" + ip + "行目", false);
		checkString (uf10Name[i], 1, 20, hfUfSeigyo.getUf10Name() + "：" + ip + "行目", false);
		checkDomain (kazeiKbn[i], kazeiKbnDomain, ks1.kazeiKbn.getName() + "：" + ip + "行目", false);
		//課税区分が税込or税抜系の場合のみチェック
		if(isNotEmpty(kazeiKbn[i]) && kazeiKbnCheck != null && kazeiKbnCheck[i]) {
			checkDomain (zeiritsu[i], zeiritsuDomain, ks1.zeiritsu.getName() + "：" + ip + "行目", false);
			checkDomain (keigenZeiritsuKbn[i], Domain.FLG, "軽減税率区分" + "：" + ip + "行目", false);
		}		
		checkKingakuOver1 (shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "：" + ip + "行目", false);
		checkKingakuOver0 (hontaiKingaku[i], "本体金額合計" + "：" + ip + "行目", false);
		checkKingakuOver0 (shouhizeigaku[i], "消費税額合計" + "：" + ip + "行目", false);
		checkDomain (houjinCardFlgKeihi[i], Domain.FLG, ks.houjinCardRiyou.getName() + "フラグ:" + ip + "行目", false);
		checkDomain (kaishaTehaiFlgKeihi[i], Domain.FLG, ks.kaishaTehai.getName() + "フラグ:" + ip + "行目", false);
		checkString (tekiyou[i], 1, this.getIntTekiyouMaxLength(), ks1.tekiyou.getName() + "：" + ip + "行目", false);
		checkSJIS (tekiyou[i], ks1.tekiyou.getName() + "：" + ip + "行目");
		checkString (kousaihiShousai[i], 1, 240, ks1.kousaihiShousai.getName() + "：" + ip + "行目", false);
		checkNumberOver1 (kousaihiNinzuu[i], 1, 6, "交際費人数：" + ip + "行目", false);
		checkKingakuOver0 (kousaihiHitoriKingaku[i], "交際費一人当たりの金額：" + ip + "行目", false);
		checkString(this.shiharaisakiNameKeihi[i], 1, 60, ks1.shiharaisaki.getName() +  "：" + ip + "行目",false);
		checkDomain(this.jigyoushaKbnKeihi[i], this.jigyoushaKbnDomain, ks1.jigyoushaKbn.getName() +  "：" + ip + "行目",false);
		checkDomain(this.bunriKbnKeihi[i], this.bunriKbnDomain, ks1.bunriKbn.getName() +  "：" + ip + "行目",false);
		checkDomain(this.kariShiireKbnKeihi[i], this.shiireKbnDomain, ks1.shiireKbn.getName() +  "：" + ip + "行目",false);
	}
	
	/**
	 * @param i 配列番号
	 * @param ip 行の種類+番号
	 */
	protected void checkKaigaiMeisaiKoumokuFormat(int i, String ip) {}
	
	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum 1:登録/更新、2:支払日更新
	 */
	protected void denpyouHissuCheck(int eventNum) {

		// 明細必須フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();
		
		List<String[]> listArrays = new ArrayList<String[]>();
		// 基本項目
		listArrays.add(new String[] {karibaraiDenpyouId, ks.karibaraiDenpyouId.getName(), karibaraiIsEnabled ? ks.karibaraiDenpyouId.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {karibaraiOn, ks.karibaraiOn.getName(), karibaraiIsEnabled ? ks.karibaraiOn.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {karibaraiTekiyou, ks.karibaraiTekiyou.getName(), karibaraiIsEnabled ? ks.karibaraiTekiyou.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {karibaraiShinseiKingaku, ks.shinseiKingaku.getName(), karibaraiIsEnabled ? ks.shinseiKingaku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {karibaraiKingakuSagaku, ks.karibaraiKingakuSagaku.getName(), karibaraiIsEnabled ? ks.karibaraiKingakuSagaku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {userIdRyohi, ks.userName.getName() + "ID", ks.userName.getHissuFlgS(), "0"});
		listArrays.add(new String[] {userNameRyohi, ks.userName.getName() + "名", ks.userName.getHissuFlgS(), "0"});
		listArrays.add(new String[] {shainNoRyohi, ks.userName.getName() + "社員番号", ks.userName.getHissuFlgS(), "0"});
		listArrays.add(new String[] {houmonsaki, ks.houmonsaki.getName(), keihiEntryFlg ? ks.houmonsaki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {mokuteki, ks.mokuteki.getName(), keihiEntryFlg ? ks.mokuteki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {shiharaihouhou, ks.shiharaiHouhou.getName(), keihiEntryFlg ? ks.shiharaiHouhou.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {shiharaiKiboubi, ks.shiharaiKiboubi.getName(), keihiEntryFlg ? ks.shiharaiKiboubi.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {seisankikanFrom, ks.seisankikan.getName() + "開始日", keihiEntryFlg ? ks.seisankikan.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {seisankikanFromHour, ks.seisankikanJikoku.getName() + "開始（時）",keihiEntryFlg ? ks.seisankikanJikoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {seisankikanFromMin, ks.seisankikanJikoku.getName() + "開始（分）",keihiEntryFlg ? ks.seisankikanJikoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {seisankikanTo, ks.seisankikan.getName() + "終了日", keihiEntryFlg ? ks.seisankikan.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {seisankikanToHour, ks.seisankikanJikoku.getName() + "終了（時）",keihiEntryFlg ? ks.seisankikanJikoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {seisankikanToMin, ks.seisankikanJikoku.getName() + "終了（分）",keihiEntryFlg ? ks.seisankikanJikoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {zeiritsuRyohi, ks.zeiritsu.getName(), keihiEntryFlg && List.of("001", "002", "011", "012", "013", "014").contains(this.kazeiKbnRyohi) ? ks.zeiritsu.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kingaku, ks.goukeiKingaku.getName(), keihiEntryFlg ? ks.goukeiKingaku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {this.invoiceDenpyou, "インボイス対応伝票", keihiEntryFlg ? "1" : "0", "0"});
		
		// 海外仕訳分追加
		this.addKaigaiShiwakeKoumokuForHissuCheck(listArrays, keihiEntryFlg);
		
		// 国内仕訳分追加
		String prefix = isKaigai ? "国内" : "";
		listArrays.add(new String[] {shiwakeEdaNoRyohi, prefix + ks.torihiki.getName() + "コード",  keihiEntryFlg && denpyouKbn.equals("A004") ? ks.torihiki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {torihikiNameRyohi, prefix + ks.torihiki.getName() + "名", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.torihiki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kamokuCdRyohi, prefix + ks.kamoku.getName() + "コード", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.kamoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kamokuNameRyohi, prefix + ks.kamoku.getName() + "名", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.kamoku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kamokuEdabanCdRyohi, prefix + ks.kamokuEdaban.getName() + "コード", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.kamokuEdaban.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {kamokuEdabanNameRyohi, prefix + ks.kamokuEdaban.getName() + "名", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.kamokuEdaban.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {futanBumonCdRyohi, prefix + ks.futanBumon.getName() + "コード", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.futanBumon.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {futanBumonNameRyohi, prefix + ks.futanBumon.getName() + "名", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.futanBumon.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {torihikisakiCdRyohi, prefix + ks.torihikisaki.getName() + "コード", keihiEntryFlg ? ks.torihikisaki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {torihikisakiNameRyohi, prefix + ks.torihikisaki.getName() + "名", keihiEntryFlg ? ks.torihikisaki.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {projectCdRyohi, prefix + ks.project.getName() + "コード", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) && ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {projectNameRyohi, prefix + ks.project.getName() + "名", keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) && ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {segmentCdRyohi, prefix + ks.segment.getName() + "コード", keihiEntryFlg && ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {segmentNameRyohi, prefix + ks.segment.getName() + "名", keihiEntryFlg && ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {tekiyouRyohi, ks.tekiyou.getName(), keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi) ? ks.tekiyou.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {houjinCardRiyouGoukei, ks.uchiHoujinCardRiyouGoukei.getName(), keihiEntryFlg ? ks.uchiHoujinCardRiyouGoukei.getHissuFlgS() : "0", "0"});
		listArrays.add(new String[] {kaishaTehaiGoukei, ks.kaishaTehaiGoukei.getName(), keihiEntryFlg ? ks.kaishaTehaiGoukei.getHissuFlgS() : "0", "0"});
		listArrays.add(new String[] {sashihikiShikyuuKingaku, ks.sashihikiShikyuuKingaku.getName(), keihiEntryFlg ? ks.sashihikiShikyuuKingaku.getHissuFlgS() : "0","0"});
		listArrays.add(new String[] {shiharaibi, "支払日", "0","1"});
		listArrays.add(new String[] {keijoubi, "計上日", keihiEntryFlg && (keijouBiMode == 1 || keijouBiMode == 3) ? "1" : "0", keihiEntryFlg && hasseiShugi ? "1" : "0"});
		listArrays.add(new String[] {hosoku, ks.hosoku.getName(), ks.hosoku.getHissuFlgS(),"0"});
		listArrays.add(new String[] {this.bunriKbn, prefix + ks.bunriKbn.getName(), keihiEntryFlg ? ks.bunriKbn.getHissuFlgS() : "0", "0"});
		listArrays.add(new String[] {this.kariShiireKbn, prefix + ks.shiireKbn.getName(), keihiEntryFlg ? ks.shiireKbn.getHissuFlgS() : "0", "0"});
		hissuCheckCommon(listArrays, eventNum);

		// 会社設定に差引項目が入力されている場合
		this.hissuCheckSashihikiKingakuKaigai(eventNum, keihiEntryFlg);
		if( sasihikiHyoujiFlg ){
			var list = new String[][] {
					{sashihikiNum, ks.sashihikiNum.getName(), keihiEntryFlg ? ks.sashihikiNum.getHissuFlgS() : "0", "0"},
					{sashihikiKingaku, ks.sashihikiKingaku.getName(), keihiEntryFlg ? ks.sashihikiKingaku.getHissuFlgS() : "0", "0"},
				};
			hissuCheckCommon(list, eventNum);
		}

		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード" ,keihiEntryFlg ? ks.hf1.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード" ,keihiEntryFlg ? ks.hf2.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード" ,keihiEntryFlg ? ks.hf3.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード" ,keihiEntryFlg ? ks.hf4.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード" ,keihiEntryFlg ? ks.hf5.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード" ,keihiEntryFlg ? ks.hf6.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード" ,keihiEntryFlg ? ks.hf7.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード" ,keihiEntryFlg ? ks.hf8.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード" ,keihiEntryFlg ? ks.hf9.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード" ,keihiEntryFlg ? ks.hf10.getHissuFlgS() : "0", "0"},}, eventNum);

		this.hissuCheckKaigaiUfCd(eventNum, keihiEntryFlg);
		
		if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1CdRyohi, prefix + hfUfSeigyo.getUf1Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf1.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2CdRyohi, prefix + hfUfSeigyo.getUf2Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf2.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3CdRyohi, prefix + hfUfSeigyo.getUf3Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf3.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4CdRyohi, prefix + hfUfSeigyo.getUf4Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf4.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5CdRyohi, prefix + hfUfSeigyo.getUf5Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf5.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6CdRyohi, prefix + hfUfSeigyo.getUf6Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf6.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7CdRyohi, prefix + hfUfSeigyo.getUf7Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf7.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8CdRyohi, prefix + hfUfSeigyo.getUf8Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf8.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9CdRyohi, prefix + hfUfSeigyo.getUf9Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf9.getHissuFlgS() : "0", "0"},}, eventNum);
		if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10CdRyohi, prefix + hfUfSeigyo.getUf10Name() + "コード" ,keihiEntryFlg && isNotEmpty(shiwakeEdaNoRyohi)? ks.uf10.getHissuFlgS() : "0", "0"},}, eventNum);

		if (keihiEntryFlg) {
			//旅費明細・経費明細どちらかが入力されていればOK
			if (shubetsuCd[0].isEmpty() && shiwakeEdaNo[0].isEmpty()) {
				var list = new String[][]{
					//項目							項目名  必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{koutsuuShudan[0], ks.koutsuuShudan.getName()  + ":" + "1行目", ks.koutsuuShudan.getHissuFlgS(),"0"},
					{oufukuFlg[0], ks.oufukuFlg.getName()  + ":" + "1行目", ks.oufukuFlg.getHissuFlgS(),"0"},
					{jidounyuuryokuFlg[0], "自動入力フラグ:" + "1行目", "1","0"},
					{kikanTo[0], ks.kikan.getName() + "終了日" + ":" + "1行目", ks.kikan.getHissuFlgS(),"0"},
					{kyuujitsuNissuu[0], ks.kyuujitsuNissuu.getName() + ":" + "1行目", ks.kyuujitsuNissuu.getHissuFlgS(),"0"},
					{nissuu[0], ks.nissuu.getName() + ":" + "1行目", ks.nissuu.getHissuFlgS(),"0"},
					{shiwakeEdaNo[0], ks.torihiki.getName() + "コード：" + "1行目", ks.torihiki.getHissuFlgS(), "0"},
					{torihikiName[0], ks.torihiki.getName() + "名：" + "1行目", ks.torihiki.getHissuFlgS(), "0"},
					{kamokuCd[0], ks.kamoku.getName() + "コード：" + "1行目", ks.kamoku.getHissuFlgS(), "0"},
					{kamokuName[0], ks.kamoku.getName() + "名：" + "1行目", ks.kamoku.getHissuFlgS(), "0"},
					{kamokuEdabanCd[0], ks.kamokuEdaban.getName() + "コード：" + "1行目", ks.kamokuEdaban.getHissuFlgS(), "0"},
					{kamokuEdabanName[0], ks.kamokuEdaban.getName() + "名：" + "1行目", ks.kamokuEdaban.getHissuFlgS(), "0"},
					{futanBumonCd[0], ks.futanBumon.getName() + "コード：" + "1行目", ks.futanBumon.getHissuFlgS(), "0"},
					{futanBumonName[0], ks.futanBumon.getName() + "名：" + "1行目", ks.futanBumon.getHissuFlgS(), "0"},
					{projectCd[0], ks.project.getName() + "コード：" + "1行目", ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
					{projectName[0], ks.project.getName() + "名：" + "1行目", ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
					{segmentCd[0], ks.segment.getName() + "コード：" + "1行目", ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
					{segmentName[0], ks.segment.getName() + "名：" + "1行目", ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
					{torihikisakiCd[0], ks1.torihikisaki.getName()+ "コード：" + "1行目", ks1.torihikisaki.getHissuFlgS(), "0"},
					{torihikisakiName[0], ks1.torihikisaki.getName()+ "名：" + "1行目", ks1.torihikisaki.getHissuFlgS(), "0"},
					{zeiritsu[0], ks.zeiritsu.getName() + "：" + "1行目", ks.zeiritsu.getHissuFlgS(), "0"},
					{keigenZeiritsuKbn[0], "軽減税率区分" + "：" + "1行目", ks.zeiritsu.getHissuFlgS(), "0"},
					{shiharaiKingaku[0], ks1.shiharaiKingaku.getName() + "：" + "1行目", ks1.shiharaiKingaku.getHissuFlgS(), "0"},
					{tekiyou[0], ks.tekiyou.getName() + "：" + "1行目", ks.tekiyou.getHissuFlgS(), "0"},
					{kousaihiShousai[0], ks1.kousaihiShousai.getName() + "：" + "1行目", kousaihiEnable[0] ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
					{kousaihiNinzuu[0], "交際費人数：" + "1行目", (kousaihiEnable[0] && ninzuuEnable[0]) ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
					{kousaihiHitoriKingaku[0], "交際費一人当たりの金額：" + "1行目", (kousaihiEnable[0] && ninzuuEnable[0]) ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
					{hayaFlg[0], ks.hayaYasuRaku.getName() + "(早フラグ):" + "1行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
					{yasuFlg[0], ks.hayaYasuRaku.getName() + "(安フラグ):" + "1行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
					{rakuFlg[0], ks.hayaYasuRaku.getName() + "(楽フラグ):" + "1行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
					{this.shiharaisakiNameRyohi[0],ks.shiharaisaki.getName() +  "：" + "1行目", ks.shiharaisaki.getHissuFlgS(), "0"},
					{this.jigyoushaKbnRyohi[0],ks.jigyoushaKbn.getName() +  "：" + "1行目", ks.jigyoushaKbn.getHissuFlgS(), "0"},
					{this.shouhizeigakuRyohi[0],ks.shouhizeigaku.getName()  +  "："  + "1行目", ks.shouhizeigaku.getHissuFlgS(), "0"},
				};
				hissuCheckCommon(list, eventNum);
			}

			if (!shubetsuCd[0].isEmpty()) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					String ip = makeMeisaiHeader(i);
					var ryohiMeisaiListArrays = new ArrayList<String[]>();
					ryohiMeisaiListArrays.add(new String[] {shubetsuCd[i], "種別コード" + ":" + ip + "行目", "1","0"});
					ryohiMeisaiListArrays.add(new String[] {shubetsu1[i], ks.shubetsu1.getName() + ":" + ip + "行目", ks.shubetsu1.getHissuFlgS(),"0"});
					ryohiMeisaiListArrays.add(new String[] {kikanFrom[i], ks.kikan.getName() + "開始日" + ":" + ip + "行目", ks.kikan.getHissuFlgS(),"0"});
					ryohiMeisaiListArrays.add(new String[] {ryoushuushoSeikyuushoTouFlg[i],ks.ryoushuushoSeikyuushoTouFlg.getName()  + ":" + ip + "行目", ks.ryoushuushoSeikyuushoTouFlg.getHissuFlgS(),"0"});
					ryohiMeisaiListArrays.add(new String[] {tanka[i], ks.tanka.getName()  + ":" + ip + "行目", ks.tanka.getHissuFlgS(),"0"});
					ryohiMeisaiListArrays.add(new String[] {houjinCardFlgRyohi[i], ks.houjinCardRiyou.getName()  + ":" + ip + "行目", ks.houjinCardRiyou.getHissuFlgS(),"0"});
					ryohiMeisaiListArrays.add(new String[] {kaishaTehaiFlgRyohi[i], ks.kaishaTehai.getName()  + ":" + ip + "行目", ks.kaishaTehai.getHissuFlgS(),"0"});
					ryohiMeisaiListArrays.add(new String[] {meisaiKingaku[i], ks.meisaiKingaku.getName()  + ":" + ip + "行目", ks.meisaiKingaku.getHissuFlgS(),"0"});
					
					// 海外項目の追加
					if (isKaigaiRyohiMeisai(i))
					{
						this.addKaigaiRyohiMeisaiKoumokuForHissuCheck(listArrays, i, ip);
					}
					
					hissuCheckCommon(ryohiMeisaiListArrays, eventNum);
					
					String[][] list;
					switch(shubetsuCd[i]) {
						case RYOHISEISAN_SYUBETSU.KOUTSUUHI:
							list = new String[][]{
								//項目							項目名  必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
								{shubetsu2[i], ks.shubetsu2.getName() + ":" + ip + "行目", ks.shubetsu1.getHissuFlgS(),"0"},
								{koutsuuShudan[i], ks.koutsuuShudan.getName()  + ":" + ip + "行目", ks.koutsuuShudan.getHissuFlgS(),"0"},
								{naiyou[i], ks.naiyouKoutsuuhi.getName()  + ":" + ip + "行目", ks.naiyouKoutsuuhi.getHissuFlgS(),"0"},
								{bikou[i], ks.bikouKoutsuuhi.getName() + ":" + ip + "行目", ks.bikouKoutsuuhi.getHissuFlgS(),"0"},
								{jidounyuuryokuFlg[i], "自動入力フラグ" + ":" + ip + "行目", "1","0"},
								{hayaFlg[i], ks.hayaYasuRaku.getName() + "(早フラグ):" + ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
								{yasuFlg[i], ks.hayaYasuRaku.getName() + "(安フラグ):" + ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
								{rakuFlg[i], ks.hayaYasuRaku.getName() + "(楽フラグ):" + ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
							};
							hissuCheckCommon(list, eventNum);

							if (!isKaigaiRyohiMeisai(i) && (StringUtils.isNotEmpty(suuryouNyuryokuType[i]) && !suuryouNyuryokuType[i].equals("0"))) {
								list = new String[][]{
									{suuryou[i], "数量:" + ip + "行目", "1","0"},
									{suuryouKigou[i], "数量記号:" + ip + "行目", "1","0"},
								};
							}else {
								list = new String[][]{
									{oufukuFlg[i], ks.oufukuFlg.getName()  + ":" + ip + "行目", ks.oufukuFlg.getHissuFlgS(),"0"},
								};
							}
							hissuCheckCommon(list, eventNum);
							break;
						case RYOHISEISAN_SYUBETSU.SONOTA:
							list = new String[][]{
								//項目							項目名  必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
								{kikanTo[i], ks.kikan.getName() + "終了日" + ":" + ip + "行目", ks.kikan.getHissuFlgS(),"0"},
								{kyuujitsuNissuu[i], ks.kyuujitsuNissuu.getName() + ":" + ip + "行目", ks.kyuujitsuNissuu.getHissuFlgS(),"0"},
								{naiyou[i], ks.naiyouNittou.getName()  + ":" + ip + "行目", ks.naiyouNittou.getHissuFlgS(),"0"},
								{bikou[i], ks.bikouNittou.getName() + ":" + ip + "行目", ks.bikouNittou.getHissuFlgS(),"0"},
								{nissuu[i], ks.nissuu.getName() + ":" + ip + "行目", ks.nissuu.getHissuFlgS(),"0"},
							};
							hissuCheckCommon(list, eventNum);
							break;
					}
					
					if(this.invoiceDenpyou.equals("0"))
					{
						list = new String[][]{
							{this.shiharaisakiNameRyohi[i],ks.shiharaisaki.getName() +  "：" + ip + "行目", ks.shiharaisaki.getHissuFlgS(), "0"},
							{this.jigyoushaKbnRyohi[i],ks.jigyoushaKbn.getName() +  "：" + ip + "行目", ks.jigyoushaKbn.getHissuFlgS(), "0"},
						};
						hissuCheckCommon(list, eventNum);
					}
					
					hissuCheckCommon(new String[][] {
								{this.shouhizeigakuRyohi[i],ks.shouhizeigaku.getName()  +  "："  + ip + "行目", ks.shouhizeigaku.getHissuFlgS(), "0"}
					}, eventNum);
				}

				if (!shiwakeEdaNo[0].isEmpty()) {
					for (int i = 0; i < shiwakeEdaNo.length; i++) {
						String ip = "その他 経費 " + (i + 1);
						var keihiMeisaiListArrays = new ArrayList<String[]>();
						
						keihiMeisaiListArrays.add(new String[] {shiyoubi[i], ks1.shiyoubi.getName() + "：" + ip + "行目", ks1.shiyoubi.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {shouhyouShorui[i], ks1.shouhyouShoruiFlg.getName() + "：" + ip + "行目", ks1.shouhyouShoruiFlg.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {shiwakeEdaNo[i], ks1.torihiki.getName() + "コード：" + ip + "行目", ks1.torihiki.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {torihikiName[i], ks1.torihiki.getName() + "名：" + ip + "行目", ks1.torihiki.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {kamokuCd[i], ks1.kamoku.getName() + "コード：" + ip + "行目", ks1.kamoku.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {kamokuName[i], ks1.kamoku.getName() + "名：" + ip + "行目", ks1.kamoku.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {kamokuEdabanCd[i], ks1.kamokuEdaban.getName() + "コード：" + ip + "行目", ks1.kamokuEdaban.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {kamokuEdabanName[i], ks1.kamokuEdaban.getName() + "名：" + ip + "行目", ks1.kamokuEdaban.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {futanBumonCd[i], ks1.futanBumon.getName() + "コード：" + ip + "行目", ks1.futanBumon.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {futanBumonName[i], ks1.futanBumon.getName() + "名：" + ip + "行目", ks1.futanBumon.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {projectCd[i], ks1.project.getName() + "コード：" + ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {projectName[i], ks1.project.getName() + "名：" + ip + "行目", ( !"0".equals(pjShiyouFlg) && ks1.project.getHyoujiFlg() ) ? ks1.project.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {segmentCd[i], ks1.segment.getName() + "コード：" + ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {segmentName[i], ks1.segment.getName() + "名：" + ip + "行目", ( !"0".equals(segmentShiyouFlg) && ks1.segment.getHyoujiFlg() ) ? ks1.segment.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {torihikisakiCd[i], ks1.torihikisaki.getName()+ "コード：" + ip + "行目", ks1.torihikisaki.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {torihikisakiName[i], ks1.torihikisaki.getName()+ "名：" + ip + "行目", ks1.torihikisaki.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {kazeiKbnCheck[i] ? zeiritsu[i]: "0", ks1.zeiritsu.getName() + "：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {kazeiKbnCheck[i] ? keigenZeiritsuKbn[i]: "0", "軽減税率区分：" + ip + "行目", kazeiKbnCheck[i] ? ks.zeiritsu.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {shiharaiKingaku[i], ks1.shiharaiKingaku.getName() + "：" + ip + "行目", ks1.shiharaiKingaku.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {houjinCardFlgKeihi[i], ks.houjinCardRiyou.getName() + ":" + ip + "行目", ks.houjinCardRiyou.getHissuFlgS(),"0"});
						keihiMeisaiListArrays.add(new String[] {kaishaTehaiFlgKeihi[i], ks.kaishaTehai.getName() + ":" + ip + "行目", ks.kaishaTehai.getHissuFlgS(),"0"});
						keihiMeisaiListArrays.add(new String[] {tekiyou[i], ks1.tekiyou.getName() + "：" + ip + "行目", ks1.tekiyou.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {kousaihiShousai[i], ks1.kousaihiShousai.getName() + "：" + ip + "行目", kousaihiEnable[i] ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {kousaihiNinzuu[i], "交際費人数：" + ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0"});
						keihiMeisaiListArrays.add(new String[] {kousaihiHitoriKingaku[i], "交際費一人当たりの金額：" + ip + "行目", (kousaihiEnable[i] && ninzuuEnable[i]) ? "1" : "0", "0"});
						if(this.invoiceDenpyou.equals("0")){
							keihiMeisaiListArrays.add(new String[] {this.shiharaisakiNameKeihi[i],ks1.shiharaisaki.getName() +  "：" + ip + "行目", ks1.shiharaisaki.getHissuFlgS(), "0"});
							keihiMeisaiListArrays.add(new String[] {this.jigyoushaKbnKeihi[i],ks1.jigyoushaKbn.getName() +  "：" + ip + "行目", ks1.jigyoushaKbn.getHissuFlgS(), "0"});
						}
						keihiMeisaiListArrays.add(new String[] {this.bunriKbnKeihi[i],ks1.bunriKbn.getName() +  "：" + ip + "行目", ks1.bunriKbn.getHissuFlgS(), "0"});
						keihiMeisaiListArrays.add(new String[] {this.kariShiireKbnKeihi[i],ks1.shiireKbn.getName() +  "：" + ip + "行目", ks1.shiireKbn.getHissuFlgS(), "0"});
						
						// 海外項目の追加
						this.addKaigaiKeihiMeisaiKoumokuForHissuCheck(keihiMeisaiListArrays, i, ip);
						
						hissuCheckCommon(keihiMeisaiListArrays, eventNum);

						if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1Cd[i], hfUfSeigyo.getUf1Name() + "コード" + "："+ ip + "行目", ks1.uf1.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2Cd[i], hfUfSeigyo.getUf2Name() + "コード" + "："+ ip + "行目",ks1.uf2.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3Cd[i], hfUfSeigyo.getUf3Name() + "コード" + "："+ ip + "行目", ks1.uf3.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4Cd[i], hfUfSeigyo.getUf4Name() + "コード" + "："+ ip + "行目", ks1.uf4.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5Cd[i], hfUfSeigyo.getUf5Name() + "コード" + "："+ ip + "行目", ks1.uf5.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6Cd[i], hfUfSeigyo.getUf6Name() + "コード" + "："+ ip + "行目", ks1.uf6.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7Cd[i], hfUfSeigyo.getUf7Name() + "コード" + "："+ ip + "行目", ks1.uf7.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8Cd[i], hfUfSeigyo.getUf8Name() + "コード" + "："+ ip + "行目", ks1.uf8.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9Cd[i], hfUfSeigyo.getUf9Name() + "コード" + "："+ ip + "行目", ks1.uf9.getHissuFlgS()},}, 1);
						if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10Cd[i], hfUfSeigyo.getUf10Name() + "コード" + "："+ ip + "行目", ks1.uf10.getHissuFlgS()},}, 1);
					}
				}
			}
		}
	}
	
	/**
	 * 必須チェック用リストに海外仕訳項目を追加
	 * @param listArrays リスト
	 * @param keihiEntryFlg 経費必須フラグ
	 */
	protected void addKaigaiShiwakeKoumokuForHissuCheck(List<String[]> listArrays, boolean keihiEntryFlg)
	{
	}
	
	/**
	 * 海外差し引き金額の必須チェック
	 * @param eventNum イベント番号
	 * @param keihiEntryFlg 経費必須フラグ
	 */
	protected void hissuCheckSashihikiKingakuKaigai(int eventNum, boolean keihiEntryFlg)
	{
	}
	
	/**
	 * 海外UFコードの必須チェック
	 * @param eventNum イベント番号
	 * @param keihiEntryFlg 経費必須フラグ
	 */
	protected void hissuCheckKaigaiUfCd(int eventNum, boolean keihiEntryFlg)
	{
	}
	
	/**
	 * 海外旅費明細項目の必須チェック
	 * @param ryohiMeisaiListArrays 旅費明細項目リスト
	 * @param i 明細番号
	 * @param ip 明細行番号
	 */
	protected void addKaigaiRyohiMeisaiKoumokuForHissuCheck(List<String[]> ryohiMeisaiListArrays, int i, String ip)
	{
	}
	
	/**
	 * 経費旅費明細項目の必須チェック
	 * @param keihiMeisaiListArrays 経費明細項目リスト
	 * @param i 明細番号
	 * @param ip 明細行番号
	 */
	protected void addKaigaiKeihiMeisaiKoumokuForHissuCheck(List<String[]> keihiMeisaiListArrays, int i, String ip)
	{
	}
	
	/**
	 * 業務関連の関連チェック処理共通部
	 * @param user_Id ユーザーID
	 * @param isKaigaiRyohi 海外旅費であるか
	 */
	protected void commonSoukanCheckBase(String user_Id, boolean isKaigaiRyohi) {

		// 精算期間開始日と精算期間終了日の整合性チェック
		for(Map<String, String> errMap: EteamCommon.kigenCheckWithJikoku(seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin)) {
			// 開始日と終了日の整合性チェック、分だけ入力されている場合入力エラーとする。
			if("2".equals(errMap.get("errorCode")) || "3".equals(errMap.get("errorCode")) || "4".equals(errMap.get("errorCode")) && !shubetsuCd[0].isEmpty()){
				errorList.add(ks.seisankikan.getName() + errMap.get("errorMassage"));
			}
		}

		// 登録時のみ仮払伝票IDが登録できるものかチェック
		if(isEmpty(super.denpyouId) && !karibaraiDenpyouId.isEmpty()) {
			List<GMap> karibaraiChk = kaikeiLogic.loadKaribaraiAnken(isKaigaiRyohi ? DENPYOU_KBN.KAIGAI_RYOHI_SEISAN : DENPYOU_KBN.RYOHI_SEISAN, karibaraiDenpyouId, userIdRyohi);
			if(karibaraiChk.isEmpty()){
				errorList.add(ks.karibaraiDenpyouId.getName() + "[" + karibaraiDenpyouId + "]の仮払は精算済です。");
			}
		}
		
		//202208 tuika 仮払伝票の最終承認確認
		if(!karibaraiDenpyouId.isEmpty()) {
			GMap forDenpyouJoutai = wfLogic.selectDenpyou(karibaraiDenpyouId); 
			String joutai = forDenpyouJoutai.get("denpyou_joutai").toString(); 
			if(!joutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI)) { 
				errorList.add(karibaraiDenpyouId+"は承認解除されているため、仮払案件として選択できません。"); 
			}
		}

		//社員口座チェック
		commonLogic.checkShainKouza(user_Id, shiharaihouhou, errorList);

		//消費税率チェック
		commonLogic.checkZeiritsu(toDecimal(zeiritsuRyohi), EteamConst.keigenZeiritsuKbn.NORMAL, toDate(seisankikanFrom), errorList); // 精算期間開始日
		commonLogic.checkZeiritsu(toDecimal(zeiritsuRyohi), EteamConst.keigenZeiritsuKbn.NORMAL, toDate(seisankikanTo), errorList); // 精算期間終了日

		// 伝票HF部チェック
		ShiwakeCheckData shiwakeCheckDataNaiyou = commonLogic.new ShiwakeCheckData() ;
		shiwakeCheckDataNaiyou.hf1Nm = hfUfSeigyo.getHf1Name();
		shiwakeCheckDataNaiyou.hf1Cd = hf1Cd;
		shiwakeCheckDataNaiyou.hf2Nm = hfUfSeigyo.getHf2Name();
		shiwakeCheckDataNaiyou.hf2Cd = hf2Cd;
		shiwakeCheckDataNaiyou.hf3Nm = hfUfSeigyo.getHf3Name();
		shiwakeCheckDataNaiyou.hf3Cd = hf3Cd;
		shiwakeCheckDataNaiyou.hf4Nm = hfUfSeigyo.getHf4Name();
		shiwakeCheckDataNaiyou.hf4Cd = hf4Cd;
		shiwakeCheckDataNaiyou.hf5Nm = hfUfSeigyo.getHf5Name();
		shiwakeCheckDataNaiyou.hf5Cd = hf5Cd;
		shiwakeCheckDataNaiyou.hf6Nm = hfUfSeigyo.getHf6Name();
		shiwakeCheckDataNaiyou.hf6Cd = hf6Cd;
		shiwakeCheckDataNaiyou.hf7Nm = hfUfSeigyo.getHf7Name();
		shiwakeCheckDataNaiyou.hf7Cd = hf7Cd;
		shiwakeCheckDataNaiyou.hf8Nm = hfUfSeigyo.getHf8Name();
		shiwakeCheckDataNaiyou.hf8Cd = hf8Cd;
		shiwakeCheckDataNaiyou.hf9Nm = hfUfSeigyo.getHf9Name();
		shiwakeCheckDataNaiyou.hf9Cd = hf9Cd;
		shiwakeCheckDataNaiyou.hf10Nm = hfUfSeigyo.getHf10Name();
		shiwakeCheckDataNaiyou.hf10Cd = hf10Cd;
		commonLogic.checkShiwake(shiwakeCheckDataNaiyou, errorList);

		// 共通機能（会計共通）．会計項目入力チェック
		if (isNotEmpty(shiwakeEdaNoRyohi)) {

			String denpyouKbnTmp = DENPYOU_KBN.RYOHI_SEISAN;
			//旅費精算の仕訳パターン取得
			ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(denpyouKbnTmp, Integer.parseInt(shiwakeEdaNoRyohi));
			// 使用者の代表負担部門コード
			String daihyouBumonCd = super.daihyouFutanBumonCd;
			// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
			if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
				daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
			}
			
			// 借方
			checkShiwakeKashi(0, shiwakePattern, denpyouKbnTmp, daihyouBumonCd);
			
			// 貸方
			// 貸方1：振込、貸方2：現金
			checkShiwakeKashi(shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, shiwakePattern, denpyouKbnTmp, daihyouBumonCd);

			// 貸方3
			if (hasseiShugi) {
				checkShiwakeKashi(3, shiwakePattern, denpyouKbnTmp, daihyouBumonCd);
			}

			boolean houjinCardFlg = false;
			boolean kaishaTehaiFlg = false;
			for (int i = 0; i < houjinCardFlgRyohi.length; i++) {
				if (houjinCardFlgRyohi[i].equals("1") && !this.isKaigaiRyohiMeisai(i))
				{
					houjinCardFlg = true;
				}
				if (kaishaTehaiFlgRyohi[i].equals("1") && !this.isKaigaiRyohiMeisai(i))
				{
					kaishaTehaiFlg = true;
				}
			}

			// 貸方4
			if (houjinCardFlg) {
				checkShiwakeKashi(4, shiwakePattern, denpyouKbnTmp, daihyouBumonCd);
			}

			// 貸方5
			if (kaishaTehaiFlg) {
				checkShiwakeKashi(5, shiwakePattern, denpyouKbnTmp, daihyouBumonCd);
			}

			// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
			if (isNotEmpty(torihikisakiCdRyohi)) {
				ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
				torihikisaki.torihikisakiNm = ks.torihikisaki.getName() + "コード";
				torihikisaki.torihikisakiCd =torihikisakiCdRyohi;
				commonLogic.checkTorihikisaki(torihikisaki, errorList);

				// 取引先仕入先チェック
				super.checkShiiresaki(ks.torihikisaki.getName() + "コード", torihikisakiCdRyohi, DENPYOU_KBN.RYOHI_SEISAN);
			}
		}
	}
	
	/**
	 * 明細の共通soukanCheck
	 */
	protected void soukanCheckMeisai()
	{
		// 明細必須フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {
			// 明細部
			if (!shubetsuCd[0].isEmpty()) {
				for (int i = 0; i < kikanFrom.length; i++) {
					String ip = makeMeisaiHeader(i);

					//法人カード履歴使用明細のチェック(旅費交通費)
					if(!isEmpty(himodukeCardMeisaiRyohi[i])){
						int chkCd = hcLogic.checkHimodukeUsed(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
						switch(chkCd) {
						case -1:	errorList.add(ip + "行目：指定した法人カード履歴は除外済です。");break;
						case -2:	errorList.add(ip + "行目：指定した法人カード履歴は別伝票に紐付済みです。");break;
						case -99:	errorList.add(ip + "行目：指定した法人カード履歴データが存在しません。");break;
						}
					}

					// 精算期間開始日 ≦ 期間開始日 ≦ 精算期間終了日 でなければエラー
					if(kikanFrom[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(kikanFrom[i]) < 0) {
						errorList.add(ip + "行目：" + ks.kikan.getName() + "開始日は" + ks.seisankikan.getName() + "内の日付を入力してください。");
					}
					if(! kikanTo[i].isEmpty()) {

						// 精算期間開始日 ≦ 期間終了日 ≦ 精算期間終了日 でなければエラー
						if(kikanTo[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(kikanTo[i]) < 0) {
							errorList.add(ip + "行目：" + ks.kikan.getName() + "終了日は" + ks.seisankikan.getName() + "内の日付を入力してください。");
						}

						// 期間開始日と期間終了日の整合性チェック
						for(Map<String, String> errMap: EteamCommon.kigenCheck(kikanFrom[i], kikanTo[i])) {
							// 開始日と終了日の整合性チェックのみエラーとする。
							if("2".equals(errMap.get("errorCode"))){
								errorList.add(ip + "行目：" + ks.kikan.getName() + errMap.get("errorMassage"));
							}
						}
					}
				}
			}

			for (int i = 0; i < ryoushuushoSeikyuushoTouFlg.length; i++){
				/* comment out ▼ shouhyouShoruiHissuFlgは明細テーブルに持たせるようにしたのでマスターからのリロードなくす
				//証憑書類必須フラグを再取得する
				switch(shubetsuCd[i]) {
				//種別が交通費の場合
				case RYOHISEISAN_SYUBETSU.KOUTSUUHI:
					shouhyouShoruiHissuFlg[i] = masterLogic.findShouhyouShoruiHissuFlg(koutsuuShudan[i]);
					break;
				//種別が交通費以外の場合
				case RYOHISEISAN_SYUBETSU.SONOTA:
					String shubetsu2Tmp = !"".equals(shubetsu2[i])? shubetsu2[i] : "-" ;
					GMap nittou = masterLogic.loadNittouKingaku(shubetsu1[i], shubetsu2Tmp, commonLogic.getKihyouYakushokuCd(super.getKihyouUserId(), super.denpyouId));
					shouhyouShoruiHissuFlg[i] = (nittou == null)? "" : (String)nittou.get("shouhyou_shorui_hissu_flg");
					break;
				}
				comment out ▲ */
				String ip = makeMeisaiHeader(i);
				// 領収書・請求書等チェックが必須（会社設定）かつ証憑書類必須フラグが1の場合
				if(EteamCommon.funcControlJudgment(sysLogic, KINOU_SEIGYO_CD.RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU) && shouhyouShoruiHissuFlg[i].equals("1")){
					// 領収書・請求書等が未チェックであればエラー
					if(ryoushuushoSeikyuushoTouFlg[i].equals("0")){
						errorList.add(ip + "行目：" + ks.ryoushuushoSeikyuushoTouFlg.getName() + "をチェックしてください。");
					}
				}
			}

			//税率チェック
			for (int i = 0; i < kazeiKbn.length; i++) {
				String ip = "その他 経費 " + (i + 1);
				if(isNotEmpty(kazeiKbn[i]) && kazeiKbnCheck != null && kazeiKbnCheck[i]) {
					if(!this.isKaigaiKeihiMeisai(i) && isNotEmpty(shiwakeEdaNo[i])) commonLogic.checkZeiritsu(kazeiKbn[i], toDecimal(zeiritsu[i]), keigenZeiritsuKbn[i], toDate((shiyoubi[i])), errorList, ip + "行目：");
				}
				
				// 旅費系明細は科目コードがないケースがあるので
				if(isNotEmpty(this.kamokuCd[i])) {
					var shoriGroup = this.kamokuMasterDao.find(this.kamokuCd[i]).shoriGroup;
					this.commonLogic.checkKbnConsistency(errorList, ks1, shoriGroup.toString(), this.getShouhizeikbn(), this.getShiireZeiAnbun(), kazeiKbn[i], bunriKbnKeihi[i], kariShiireKbnKeihi[i], i);
				}
			}

			if (!shiwakeEdaNo[0].isEmpty()) {
				//明細単位の仕訳チェック
				for (int i = 0; i < shiwakeEdaNo.length; i++) {
					String ip = "その他 経費 " + (i + 1);

					//法人カード履歴使用明細のチェック(経費)
					if(!isEmpty(himodukeCardMeisaiKeihi[i])){
						int chkCd = hcLogic.checkHimodukeUsed(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
						switch(chkCd) {
						case -1:	errorList.add(ip + "行目：指定した法人カード履歴は除外済です。");break;
						case -2:	errorList.add(ip + "行目：指定した法人カード履歴は別伝票に紐付済みです。");break;
						case -99:	errorList.add(ip + "行目：指定した法人カード履歴データが存在しません。");break;
						}
					}

					// 使用者の代表負担部門コード
					String daihyouBumonCd = super.daihyouFutanBumonCd;
					// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
					if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
						daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
					}
					String denpyouKbnKeihi = this.isKaigaiKeihiMeisai(i) ? "A901" : DENPYOU_KBN.KEIHI_TATEKAE_SEISAN;

					//各経費明細の仕訳パターン
					ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(denpyouKbnKeihi, Integer.parseInt(shiwakeEdaNo[i]));
					
					// 借方
					checkShiwakeKeihiMeisaiKashi(i, 0, shiwakePattern, denpyouKbnKeihi, daihyouBumonCd);
					// 貸方1, 2
					checkShiwakeKeihiMeisaiKashi(i, shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, shiwakePattern, denpyouKbnKeihi, daihyouBumonCd);

					// 貸方3
					if (hasseiShugi) {
						checkShiwakeKeihiMeisaiKashi(i, 3, shiwakePattern, denpyouKbnKeihi, daihyouBumonCd);
					}

					// 貸方4
					if (houjinCardFlgKeihi[i].equals("1")) {
						checkShiwakeKeihiMeisaiKashi(i, 4, shiwakePattern, denpyouKbnKeihi, daihyouBumonCd);
					}

					// 貸方5
					if (kaishaTehaiFlgKeihi[i].equals("1")) {
						checkShiwakeKeihiMeisaiKashi(i, 5, shiwakePattern, denpyouKbnKeihi, daihyouBumonCd);
					}

					// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
					if (isNotEmpty(torihikisakiCd[i])) {
						ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
						torihikisaki.torihikisakiNm = ip + "行目：" + ks1.torihikisaki.getName() + "コード";
						torihikisaki.torihikisakiCd = torihikisakiCd[i];
						commonLogic.checkTorihikisaki(torihikisaki, errorList);

						// 取引先仕入先チェック
						super.checkShiiresaki(ip + "行目：" + ks1.torihikisaki.getName() +"コード", torihikisakiCd[i], DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
					}

					// 表示フラグONかつ使用日入力ありのとき、精算期間開始日 ≦ 使用日 ≦ 精算期間終了日 でなければエラー
					if(ks1.shiyoubi.getHyoujiFlg() && !isEmpty(shiyoubi[i]) &&  (shiyoubi[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(shiyoubi[i]) < 0)) {
						errorList.add(ip + "行目：" + ks1.shiyoubi.getName() + "は" + ks.seisankikan.getName() + "内の日付を入力してください。");
					}

				}
				//交際費が入力されている場合基準値を満たしているかチェック
				List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,shiwakeEdaNo,kousaihiHitoriKingaku,kaigaiFlg,commonLogic.KOUSAI_ERROR_CD_ERROR);
				for(GMap res : resultList) {if(res.get("errCd") != null && (int)res.get("errCd") == commonLogic.KOUSAI_ERROR_CD_ERROR ) errorList.add((String)res.get("errMsg"));}
			}
		}
		//計上日のチェック
		if ((getWfSeigyo().isTouroku() || getWfSeigyo().isShinsei()) && keijoubiSeigen && isNotEmpty(keijoubi)) {
			Date shimebi = this.keijoubiShimebiDao.findMaxShimebiForDenpyouKbn(denpyouKbn);
			if (shimebi != null && ! toDate(keijoubi).after(shimebi)) {
				errorList.add("計上日には締日(" + formatDate(shimebi) + ")より後を入力してください。");
			}

		}

		// 稟議金額引継ぎフラグの入力チェック
		int work = 0;
		if(!karibaraiDenpyouId.isEmpty()){
			if (super.ringiKingakuHikitsugiFlg != null)
			{
				for(int i = 0; i < super.ringiKingakuHikitsugiFlg.length; i++){
					if(!"".equals(ringiKingakuHikitsugiFlg[i])) {
						work += Integer.parseInt(ringiKingakuHikitsugiFlg[i]);
					}
				}
			}
			if(Domain.FLG[0].equals(karibaraiOn)){
				// 仮払ありの場合
				GMap mapKianHimozuke = wfEventLogic.selectDenpyouKianHimozuke(karibaraiDenpyouId);
				if(null != (BigDecimal)mapKianHimozuke.get("ringi_kingaku")){
					if(work > 0){
						errorList.add("稟議金額を引継いでいる仮払申請が仮払選択されているため、添付伝票から稟議金額を引継ぐことはできません。");
					}
				}
			}else{
				// 仮払なしの場合
				if(work > 0){
					errorList.add("仮払なしの仮払申請が仮払選択されているため、添付伝票から稟議金額を引継ぐことはできません。");
				}
			}
		}

		checkSashihiki();

		//仮払未使用フラグの入力チェック
		if("1".equals(karibaraiMishiyouFlg)){
			if(commonLogic.isKaribaraiKianTenpuZumi(denpyouId, karibaraiDenpyouId)){
				errorList.add("仮払選択された伝票は起案添付済のため、仮払未使用で登録することはできません。");
				karibaraiMishiyouFlg = "0";
			}
		}

		//出張中止フラグの入力チェック
		if("1".equals(shucchouChuushiFlg)){
			if(commonLogic.isKaribaraiKianTenpuZumi(denpyouId, karibaraiDenpyouId)){
				errorList.add("仮払選択された伝票は起案添付済のため、出張中止で登録することはできません。");
				shucchouChuushiFlg = "0";
			}
		}
	}
	
	/**
	 * 差し引き金額のチェック
	 */
	protected void checkSashihiki()
	{
	}
	
	/**
	 * 経費入力が必須か判定する。
	 * @return true:必須／false:不要
	 */
	protected boolean judgeKeihiEntry() {
		return ( ( isNotEmpty(karibaraiMishiyouFlg) && karibaraiMishiyouFlg.equals("1") )
				||( isNotEmpty(shucchouChuushiFlg) && shucchouChuushiFlg.equals("1") )) ? false : true;
	}
	
	/**
	 * 交通費、宿泊費等別の明細ヘッダーを返す。
	 * @param index 明細インデックス
	 * @return 交通費、宿泊費等別の明細ヘッダー
	 */
	protected String makeMeisaiHeader(int index) {
		String header = "";
		if(shubetsuCd[index].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI)){
			header = "交通費 ";
		}
		else if(shubetsuCd[index].equals(RYOHISEISAN_SYUBETSU.SONOTA)){
			header = "日当・宿泊費等 ";
			}
			int ip = 0;
			String sCd = shubetsuCd[index];
			for (int i = 0; i < index+1 ; i++) {
				if (shubetsuCd[i].equals(sCd))
				{
					ip++;
				}
			}
			return header + String.valueOf(ip);
	}
	
	/**
	 * 国内経費明細の仕訳をチェックする。
	 * @param index 明細番号
	 * @param i 貸方いくつ？（0なら借方）
	 * @param shiwakePattern 仕訳パターン
	 * @param denpyouKbnTmp 伝票区分
	 * @param daihyouBumonCd 代表部門コード
	 */
	protected void checkShiwakeKeihiMeisaiKashi(int index, int i, ShiwakePatternMaster shiwakePattern, String denpyouKbnTmp, String daihyouBumonCd)
	{
		this.checkShiwakeKeihiMeisaiKashi(index, i, shiwakePattern.map, denpyouKbnTmp, daihyouBumonCd);
	}
	
	/**
	 * 国内経費明細の仕訳をチェックする。
	 * @param index 明細番号
	 * @param i 貸方いくつ？（0なら借方）
	 * @param shiwakePattern 仕訳パターン
	 * @param denpyouKbnTmp 伝票区分
	 * @param daihyouBumonCd 代表部門コード
	 */
	protected void checkShiwakeKeihiMeisaiKashi(int index, int i, GMap shiwakePattern, String denpyouKbnTmp, String daihyouBumonCd)
	{
		boolean isKari = i == 0;
		String ip = "その他 経費 " + (index + 1) + "行目：";
		String prefixJa = ip + (isKari ? "" : "貸方");
		String prefixEn = isKari ? "kari_" : "kashi_";
		String suffix = isKari ? "" : String.valueOf(i);
		ShiwakeCheckData shiwakeCheckDataKashi = commonLogic.new ShiwakeCheckData();
		shiwakeCheckDataKashi.shiwakeEdaNoNm = ks.torihiki.getName() + "コード";
		shiwakeCheckDataKashi.shiwakeEdaNo = shiwakeEdaNo[index];
		// 原因不明だが、貸方3～5では科目・枝番・部門・課税区分はチェックされていない
		if(i <= 2)
		{
			shiwakeCheckDataKashi.kamokuNm = prefixJa + ks.kamoku.getName() + "コード";
			shiwakeCheckDataKashi.kamokuCd = isKari ? kamokuCd[index] : kashiKamokuCd[index];
			shiwakeCheckDataKashi.kamokuEdabanNm = prefixJa + ks.kamokuEdaban.getName() + "コード";
			shiwakeCheckDataKashi.kamokuEdabanCd = isKari ? kamokuEdabanCd[index] : kashiKamokuEdabanCd[index];
			shiwakeCheckDataKashi.futanBumonNm = prefixJa + ks.futanBumon.getName() + "コード";
			shiwakeCheckDataKashi.futanBumonCd = isKari ? futanBumonCd[index] : kashiFutanBumonCd[index];
			shiwakeCheckDataKashi.kazeiKbnNm = prefixJa + "課税区分";
			shiwakeCheckDataKashi.kazeiKbn = isKari ? kazeiKbn[index] : kashiKazeiKbn[index];
		}
		shiwakeCheckDataKashi.torihikisakiNm = prefixJa + ks.torihikisaki.getName() + "コード";
		shiwakeCheckDataKashi.torihikisakiCd = ShiwakeConst.TORIHIKI.equals(shiwakePattern.get(prefixEn + "torihikisaki_cd" + suffix)) ? torihikisakiCd[index] : (String)shiwakePattern.get(prefixEn + "torihikisaki_cd" + suffix);
		shiwakeCheckDataKashi.projectNm =  prefixJa + ks.project.getName() + "コード";
		if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get(prefixEn + "project_cd" + suffix)))
		{
			shiwakeCheckDataKashi.projectCd = projectCd[index];
		}
		shiwakeCheckDataKashi.segmentNm =  prefixJa + ks.segment.getName() + "コード";
		if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get(prefixEn + "segment_cd" + suffix)))
		{
			shiwakeCheckDataKashi.segmentCd = segmentCd[index];
		}
		shiwakeCheckDataKashi.uf1Nm = prefixJa + hfUfSeigyo.getUf1Name();
		shiwakeCheckDataKashi.uf2Nm = prefixJa + hfUfSeigyo.getUf2Name();
		shiwakeCheckDataKashi.uf3Nm = prefixJa + hfUfSeigyo.getUf3Name();
		shiwakeCheckDataKashi.uf4Nm = prefixJa + hfUfSeigyo.getUf4Name();
		shiwakeCheckDataKashi.uf5Nm = prefixJa + hfUfSeigyo.getUf5Name();
		shiwakeCheckDataKashi.uf6Nm = prefixJa + hfUfSeigyo.getUf6Name();
		shiwakeCheckDataKashi.uf7Nm = prefixJa + hfUfSeigyo.getUf7Name();
		shiwakeCheckDataKashi.uf8Nm = prefixJa + hfUfSeigyo.getUf8Name();
		shiwakeCheckDataKashi.uf9Nm = prefixJa + hfUfSeigyo.getUf9Name();
		shiwakeCheckDataKashi.uf10Nm = prefixJa + hfUfSeigyo.getUf10Name();
		String[][] ufCdRyohiArrays =
			{
				{ kariUf1Cd[index], kariUf2Cd[index], kariUf3Cd[index], kariUf4Cd[index], kariUf5Cd[index], kariUf6Cd[index], kariUf7Cd[index], kariUf8Cd[index], kariUf9Cd[index], kariUf10Cd[index] },
				{ kashiUf1Cd1[index], kashiUf2Cd1[index], kashiUf3Cd1[index], kashiUf4Cd1[index], kashiUf5Cd1[index], kashiUf6Cd1[index], kashiUf7Cd1[index], kashiUf8Cd1[index], kashiUf9Cd1[index], kashiUf10Cd1[index] },
				{ kashiUf1Cd2[index], kashiUf2Cd2[index], kashiUf3Cd2[index], kashiUf4Cd2[index], kashiUf5Cd2[index], kashiUf6Cd2[index], kashiUf7Cd2[index], kashiUf8Cd2[index], kashiUf9Cd2[index], kashiUf10Cd2[index] },
				{ kashiUf1Cd3[index], kashiUf2Cd3[index], kashiUf3Cd3[index], kashiUf4Cd3[index], kashiUf5Cd3[index], kashiUf6Cd3[index], kashiUf7Cd3[index], kashiUf8Cd3[index], kashiUf9Cd3[index], kashiUf10Cd3[index] },
				{ kashiUf1Cd4[index], kashiUf2Cd4[index], kashiUf3Cd4[index], kashiUf4Cd4[index], kashiUf5Cd4[index], kashiUf6Cd4[index], kashiUf7Cd4[index], kashiUf8Cd4[index], kashiUf9Cd4[index], kashiUf10Cd4[index] },
				{ kashiUf1Cd5[index], kashiUf2Cd5[index], kashiUf3Cd5[index], kashiUf4Cd5[index], kashiUf5Cd5[index], kashiUf6Cd5[index], kashiUf7Cd5[index], kashiUf8Cd5[index], kashiUf9Cd5[index], kashiUf10Cd5[index] }
			};
		
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf1_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf1Cd = ufCdRyohiArrays[i][0];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf2_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf2Cd = ufCdRyohiArrays[i][1];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf3_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf3Cd = ufCdRyohiArrays[i][2];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf4_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf4Cd = ufCdRyohiArrays[i][3];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf5_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf5Cd = ufCdRyohiArrays[i][4];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf6_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf6Cd = ufCdRyohiArrays[i][5];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf7_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf7Cd = ufCdRyohiArrays[i][6];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf8_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf8Cd = ufCdRyohiArrays[i][7];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf9_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf9Cd = ufCdRyohiArrays[i][8];
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get(prefixEn + "uf10_cd" + suffix)))
		{
			shiwakeCheckDataKashi.uf10Cd = ufCdRyohiArrays[i][9];
		}
		String[] kbnArray = { SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5 };
		commonLogic.checkShiwake(denpyouKbnTmp, kbnArray[i], shiwakeCheckDataKashi, daihyouBumonCd, errorList);
	}
	
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		//参照フラグ(true:参照起票である、false:参照起票でない)
		boolean sanshou = false;
		
		//新規起票時の表示状態作成
		//代理起票時の使用者変更時も新規起票と同様の状態とさせる
		if ( (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) || isNotEmpty(shiyoushaHenkouShainNo)) {
			makeDefaultShinsei(connection);
			makeDefaultMeisai();
			makeDefaultMeisaiKeihi();

			//代理起票できるかチェック
			dairiCheck();

			if(isEmpty(shiyoushaHenkouShainNo)) {
				if (!(dairiFlg.equals("1"))) {
					//使用者=起票者をセット
					userIdRyohi = getUser().getSeigyoUserId();
					userNameRyohi = getUser().getDisplayUserNameShort();//userInfo.getDisplayUserName();
					shainNoRyohi = getUser().getShainNo();
				}else {
					//代理起票(参照起票ではない)ならクリア
					userIdRyohi = "";
					userNameRyohi = "";
					shainNoRyohi = "";
				}
			}else {
				//使用者=変更後の使用者をセット
				GMap usrMp = bumonUsrLogic.selectShainNo(shiyoushaHenkouShainNo);
				userIdRyohi = usrMp.getString("user_id");
				userNameRyohi = usrMp.getString("user_sei") +  "　" + usrMp.getString("user_mei");
				shainNoRyohi = shiyoushaHenkouShainNo;
			}

			// 申請と取引が1:1で決まる場合
			List<ShiwakePatternMaster> list = this.shiwakePatternMasterDao.loadByDenpyouKbn(DENPYOU_KBN.RYOHI_SEISAN,this.futanBumonCdRyohi);
			if(!list.isEmpty() && list.size() == 1) {
				shiwakeEdaNoRyohi = Integer.toString(list.get(0).shiwakeEdano);
				torihikiNameRyohi =  list.get(0).torihikiName;
				tekiyouRyohi = torihikiNameRyohi;
				if(list.get(0).tekiyouFlg.equals("1") ) {
					tekiyouRyohi = list.get(0).tekiyou;
				}
				shiharaihouhou = SHIHARAI_HOUHOU.FURIKOMI;
				this.kazeiKbnRyohi = list.get(0).kariKazeiKbn;
				this.zeiritsuRyohi = list.get(0).kariZeiritsu;
				this.keigenZeiritsuKbnRyohi = list.get(0).kariKeigenZeiritsuKbn;
				this.bunriKbn = list.get(0).kariBunriKbn;
				this.kariShiireKbn = list.get(0).kariShiireKbn;
				if(!List.of("<FUTAN>","<DAIHYOUBUMON>","<SYOKIDAIHYOU>").contains(list.get(0).kariFutanBumonCd)) {
					this.futanBumonCdRyohi = list.get(0).kariFutanBumonCd;
				}
				if(!list.get(0).kariTorihikisakiCd.equals("<TORIHIKI>")) {
					this.torihikisakiCdRyohi = list.get(0).kariTorihikisakiCd;
				}
				if(!list.get(0).kariSegmentCd.equals("<SG>")) {
					this.segmentCdRyohi = list.get(0).kariSegmentCd;
				}
				if(!list.get(0).kariProjectCd.equals("<PROJECT>")) {
					this.projectCdRyohi = list.get(0).kariProjectCd;
				}
				this.displayKazeiFlgRyohi(list.get(0));
			}
			List<ShiwakePatternMaster> kaigaiList = isKaigai ?this.shiwakePatternMasterDao.loadByDenpyouKbn(denpyouKbn,this.futanBumonCdRyohi) : new ArrayList<ShiwakePatternMaster>();
			if(isKaigai)
			{
				this.displayKaigaiShiwakeRyohi(kaigaiList);
			}
			if((!list.isEmpty() && list.size() == 1) || (!kaigaiList.isEmpty() && kaigaiList.size() == 1)){
				// 仕訳パターン情報読込（相関チェック前に必要）
				reloadShiwakePattern(connection);
				//マスター等から名称は引き直す
				reloadMaster(connection);
				shiharaihouhou = null;
			}
			invoiceDenpyou = setInvoiceFlgWhenNew();

		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			GMap shinseiData = isKaigai ? kaikeiLogic.findKaigaiRyohiSeisan(denpyouId) : kaikeiLogic.findRyohiSeisan(denpyouId);
			shinseiData2Gamen(shinseiData, sanshou, connection);

			// 明細必須フラグ
			boolean keihiEntryFlg = judgeKeihiEntry();

			if (keihiEntryFlg) {
				List<GMap> meisaiList = isKaigai ? kaikeiLogic.loadKaigaiRyohiSeisanMeisai(denpyouId) : kaikeiLogic.loadRyohiSeisanMeisai(denpyouId);
				if (!meisaiList.isEmpty()) {
					meisaiData2Gamen(meisaiList);
				} else {
					makeDefaultMeisai();
				}
				List<GMap> meisaiKeiriList = isKaigai ? kaikeiLogic.loadKaigaiRyohiKeihiseisanMeisai(denpyouId) : kaikeiLogic.loadRyohiKeihiseisanMeisai(denpyouId);
				if (!meisaiKeiriList.isEmpty()) {
					meisaiData2GamenKeihi(meisaiKeiriList, sanshou, shinseiData);
				} else {
					makeDefaultMeisaiKeihi();
				}
			} else {
				makeDefaultMeisai();
				makeDefaultMeisaiKeihi();
			}

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			GMap shinseiData = isKaigai ? kaikeiLogic.findKaigaiRyohiSeisan(sanshouDenpyouId) : kaikeiLogic.findRyohiSeisan(sanshouDenpyouId);
			dairiFlg = shinseiData.get("dairiflg");
			if(getWfSeigyo().isOthersSanshouKihyou()) {
				commonLogic.adjustmentTashaCopy(dairiFlg, getUser(), denpyouKbn, shinseiData);
			}
			shinseiData2Gamen(shinseiData, sanshou, connection);

			//代理起票できるかチェック
			dairiCheck();

			// 明細必須フラグ
			boolean keihiEntryFlg = judgeKeihiEntry();

			if (keihiEntryFlg) {
				List<GMap> meisaiList = isKaigai ? kaikeiLogic.loadKaigaiRyohiSeisanMeisaiNOIC(sanshouDenpyouId) : kaikeiLogic.loadRyohiSeisanMeisaiNOIC(sanshouDenpyouId);
				if (!meisaiList.isEmpty()) {
					if(getWfSeigyo().isOthersSanshouKihyou()) {
						commonLogic.adjustmentTashaCopy(dairiFlg, getUser(), denpyouKbn, meisaiList);
					}
					meisaiData2Gamen(meisaiList);
				} else {
					makeDefaultMeisai();
				}
				List<GMap> meisaiKeihiList = isKaigai ? kaikeiLogic.loadKaigaiRyohiKeihiseisanMeisaiNoHoujinHimoduke(sanshouDenpyouId) : kaikeiLogic.loadRyohiKeihiseisanMeisaiNoHoujinHimoduke(sanshouDenpyouId);
				if (!meisaiKeihiList.isEmpty()) {
					meisaiData2GamenKeihi(meisaiKeihiList, sanshou, shinseiData);
				} else {
					makeDefaultMeisaiKeihi();
				}
			} else {
				makeDefaultMeisai();
				makeDefaultMeisaiKeihi();
			}

			// 仮払案件がある場合はクリア
			if(!isEmpty(karibaraiDenpyouId)) {
				karibaraiDenpyouId ="";
				karibaraiOn = "";
				karibaraiTekiyou = "";
				karibaraiShinseiKingaku ="";
				karibaraiKingakuSagaku = "";
				karibaraiMishiyouFlg = "";
				shucchouChuushiFlg = "";
			}

			//差引支給金額は登録時に決定
			sashihikiShikyuuKingaku = "";

			// 支払方法は会社設定で設定されている中になければクリア
			String shiharaiHouhouSetting = setting.shiharaiHouhouA004();
			String[] shiharaiHouhouArray = shiharaiHouhouSetting.split(",");
			if (!Arrays.asList(shiharaiHouhouArray).contains(shiharaihouhou))
			{
				shiharaihouhou = "";
			}

			// 日付はブランク
			seisankikanFrom = "";
			seisankikanTo = "";
			shiharaiKiboubi = "";
			for (int i = 0; i < kikanFrom.length; i++) {
				kikanFrom[i] = "";
				kikanTo[i] = "";
			}
			for (int i = 0; i < shiyoubi.length; i++) {
				shiyoubi[i] = "";
			}
			keijoubi = "";
			shiharaibi = "";

		}
		
		//課税区分の制御
		if (isNotEmpty(kamokuCdRyohi)) {
			var kamoku = this.kamokuMasterDao.find(this.kamokuCdRyohi);
			this.shoriGroupRyohi = kamoku == null ? null : kamoku.shoriGroup;
		}

		//表示制御（プルダウンとかの取得は表示方法によらず同じ）
		displaySeigyo(connection);


		// 更新モードの場合は差引単価に会社設定から取得した値を設定
		if(enableInput){
			sashihikiTanka = sashihikiTankaSI;
			this.displaySashihikiTankaKaigai();
		}
	}
	
	/**
	 * 海外分の差引単価を表示
	 */
	protected void displaySashihikiTankaKaigai()
	{
	}
	
	/**
	 * 旅費用課税フラグのセット
	 * @param record 明細レコード
	 */
	protected void displayKazeiFlgRyohi(ShiwakePatternMaster record)
	{
	}
	
	/**
	 * 海外分仕訳項目を表示する
	 * @param kaigaiList 海外明細リスト
	 */
	protected void displayKaigaiShiwakeRyohi(List<ShiwakePatternMaster> kaigaiList)
	{
	}
	
	/**
	 * 個別伝票チェック・共通部
	 * @param connection コネクション
	 * @param isTouroku 登録か更新か
	 */
	protected void commonCheckKobetsuDenpyou(EteamConnection connection, boolean isTouroku)
	{
		initParts(connection);

		//表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		// 経費入力が不要の場合、申請内容をリセットする
		if (!keihiEntryFlg) {
			resetShinsei(connection);
		}

		//余分な明細リストの削除
		extraMeisaiListDelete();

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);

		// 各種コード設定と差引支給金額の計算（相関チェック前に必要）
		if (0 <errorList.size())
		{
			return;
		}

		//支払日チェック
		if (!isTouroku && commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			denpyouHissuCheck(2);
			if (0 <errorList.size())
			{
				return;
			}
			checkShiharaiBi(shiharaibi, hasseiShugi ? keijoubi : seisankikanTo, hasseiShugi ? "計上日" : ks.seisankikan.getName() + "終了日");
			if (0 <errorList.size())
			{
				return;
			}
		}

		// 各種コード設定と差引支給金額の計算
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
		reloadMaster(connection);

		//相関チェック
		soukanCheck(userIdRyohi);
		if (0 <errorList.size())
		{
			return;
		}
		
		//計上日の値を設定に沿ってセット
		keijoubi = commonLogic.setKeijoubi(hasseiShugi, keijouBiMode, keijoubi, keijoubiDefaultSettei, setting.keijouNyuuryoku(), denpyouKbn, seisankikanTo, errorList,loginUserInfo);
	}

	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {}
	
	/**
	 * 業務関連の関連チェック処理
	 * @param user_Id ユーザーID
	 */
	protected void soukanCheck(String user_Id) {}
	
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	@Override
	protected void initParts(EteamConnection connection) {
		super.initParts(connection);
		batchKaikeiLogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		edabanZandaka =  EteamContainer.getComponent(KamokuEdabanZandakaAbstractDao.class, connection);
	}

	/**
	 * 申請内容をリセットする。
	 * @param connection コネクション
	 */
	protected void resetShinsei(EteamConnection connection) {

		hf1Cd = "";
		hf1Name = "";
		hf2Cd = "";
		hf2Name = "";
		hf3Cd = "";
		hf3Name = "";
		hf4Cd = "";
		hf4Name = "";
		hf5Cd = "";
		hf5Name = "";
		hf6Cd = "";
		hf6Name = "";
		hf7Cd = "";
		hf7Name = "";
		hf8Cd = "";
		hf8Name = "";
		hf9Cd = "";
		hf9Name = "";
		hf10Cd = "";
		hf10Name = "";
		houmonsaki = "";
		mokuteki = "";
		shiharaihouhou = "";
		shiharaiKiboubi = "";
		seisankikanFrom = "";
		seisankikanFromHour = "";
		seisankikanFromMin = "";
		seisankikanTo = "";
		seisankikanToHour = "";
		seisankikanToMin = "";
		zeiritsuRyohi = "";
		shiwakeEdaNoRyohi = "";
		torihikiNameRyohi = "";
		kamokuCdRyohi = "";
		kamokuNameRyohi = "";
		kamokuEdabanCdRyohi = "";
		kamokuEdabanNameRyohi = "";
		futanBumonCdRyohi = "";
		futanBumonNameRyohi = "";
		torihikisakiCdRyohi = "";
		torihikisakiNameRyohi = "";
		projectCdRyohi = "";
		projectNameRyohi = "";
		segmentCdRyohi = "";
		segmentNameRyohi = "";
		kazeiKbnRyohi = "";
		kashiFutanBumonCdRyohi = "";
		kashiFutanBumonNameRyohi = "";
		kashiKamokuCdRyohi = "";
		kashiKamokuNameRyohi = "";
		kashiKamokuEdabanCdRyohi = "";
		kashiKamokuEdabanNameRyohi = "";
		kashiKazeiKbnRyohi = "";
		uf1CdRyohi = "";
		uf1NameRyohi = "";
		uf2CdRyohi = "";
		uf2NameRyohi = "";
		uf3CdRyohi = "";
		uf3NameRyohi = "";
		uf4CdRyohi = "";
		uf4NameRyohi = "";
		uf5CdRyohi = "";
		uf5NameRyohi = "";
		uf6CdRyohi = "";
		uf6NameRyohi = "";
		uf7CdRyohi = "";
		uf7NameRyohi = "";
		uf8CdRyohi = "";
		uf8NameRyohi = "";
		uf9CdRyohi = "";
		uf9NameRyohi = "";
		uf10CdRyohi = "";
		uf10NameRyohi = "";
		tekiyouRyohi = "";
		tekiyouCdRyohi = "";
		sashihikiNum = "";
		sashihikiKingaku = "";
		this.bunriKbn = "";
		this.kariShiireKbn = "";
		this.kashiShiireKbn = "";
		
		// 海外項目のリセット
		resetKaigaiKoumoku();
		
		makeDefaultShinsei(connection);
	}
	
	/**
	 * 海外項目のリセット
	 */
	protected void resetKaigaiKoumoku()
	{
	}
	
	/**
	 * 旅費精算テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 旅費精算レコード
	 * @param sanshou 参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection コネクション
	 */
	protected void shinseiData2Gamen(GMap shinseiData, boolean sanshou, EteamConnection connection) {
		dairiFlg = (String)shinseiData.get("dairiflg");
		karibaraiDenpyouId = (String)shinseiData.get("karibarai_denpyou_id");
		karibaraiOn = (String)shinseiData.get("karibarai_on");
		karibaraiTekiyou = (String)shinseiData.get("karibarai_tekiyou");
		karibaraiShinseiKingaku = formatMoney(shinseiData.get("kingaku"));
		karibaraiKingaku = formatMoney(shinseiData.get("karibarai_kingaku"));
		karibaraiMishiyouFlg = (String)shinseiData.get("karibarai_mishiyou_flg");
		shucchouChuushiFlg = (String)shinseiData.get("shucchou_chuushi_flg");
		userIdRyohi = (String)shinseiData.get("user_id");
		userNameRyohi = (String)shinseiData.get("user_sei") + "　" + (String)shinseiData.get("user_mei");
		shainNoRyohi = (String)shinseiData.get("shain_no");
		hf1Cd = (String)shinseiData.get("hf1_cd");
		hf1Name = (String)shinseiData.get("hf1_name_ryakushiki");
		hf2Cd = (String)shinseiData.get("hf2_cd");
		hf2Name = (String)shinseiData.get("hf2_name_ryakushiki");
		hf3Cd = (String)shinseiData.get("hf3_cd");
		hf3Name = (String)shinseiData.get("hf3_name_ryakushiki");
		hf4Cd = (String) shinseiData.get("hf4_cd");
		hf4Name = (String) shinseiData.get("hf4_name_ryakushiki");
		hf5Cd = (String) shinseiData.get("hf5_cd");
		hf5Name = (String) shinseiData.get("hf5_name_ryakushiki");
		hf6Cd = (String) shinseiData.get("hf6_cd");
		hf6Name = (String) shinseiData.get("hf6_name_ryakushiki");
		hf7Cd = (String) shinseiData.get("hf7_cd");
		hf7Name = (String) shinseiData.get("hf7_name_ryakushiki");
		hf8Cd = (String) shinseiData.get("hf8_cd");
		hf8Name = (String) shinseiData.get("hf8_name_ryakushiki");
		hf9Cd = (String) shinseiData.get("hf9_cd");
		hf9Name = (String) shinseiData.get("hf9_name_ryakushiki");
		hf10Cd = (String) shinseiData.get("hf10_cd");
		hf10Name = (String) shinseiData.get("hf10_name_ryakushiki");
		houmonsaki = (String)shinseiData.get("houmonsaki");
		mokuteki = (String)shinseiData.get("mokuteki");
		shiharaihouhou = (String)shinseiData.get("shiharaihouhou");
		shiharaiKiboubi = formatDate(shinseiData.get("shiharaikiboubi"));
		seisankikanFrom = formatDate(shinseiData.get("seisankikan_from"));
		seisankikanFromHour = (String)shinseiData.get("seisankikan_from_hour");
		seisankikanFromMin = (String)shinseiData.get("seisankikan_from_min");
		seisankikanTo = formatDate(shinseiData.get("seisankikan_to"));
		seisankikanToHour = (String)shinseiData.get("seisankikan_to_hour");
		seisankikanToMin = (String)shinseiData.get("seisankikan_to_min");
		keijoubi = formatDate(shinseiData.get("keijoubi"));
		zeiritsuRyohi = formatMoney(shinseiData.get("zeiritsu"));
		kingaku = formatMoney(shinseiData.get("goukei_kingaku"));
		houjinCardRiyouGoukei = formatMoney(shinseiData.get("houjin_card_riyou_kingaku"));
		kaishaTehaiGoukei = formatMoney(shinseiData.get("kaisha_tehai_kingaku"));
		sashihikiShikyuuKingaku = formatMoney(shinseiData.get("sashihiki_shikyuu_kingaku"));
		shiwakeEdaNoRyohi = ((shinseiData.get("shiwake_edano") == null) ? "" : ((Integer)shinseiData.get("shiwake_edano")).toString());
		torihikiNameRyohi = (String)shinseiData.get("torihiki_name");
		kamokuCdRyohi = (String)shinseiData.get("kari_kamoku_cd");
		kamokuNameRyohi = (String)shinseiData.get("kari_kamoku_name");
		kamokuEdabanCdRyohi = (String)shinseiData.get("kari_kamoku_edaban_cd");
		kamokuEdabanNameRyohi = (String)shinseiData.get("kari_kamoku_edaban_name");
		futanBumonCdRyohi = (String)shinseiData.get("kari_futan_bumon_cd");
		futanBumonNameRyohi = (String)shinseiData.get("kari_futan_bumon_name");
		torihikisakiCdRyohi = (String)shinseiData.get("torihikisaki_cd");
		torihikisakiNameRyohi = (String)shinseiData.get("torihikisaki_name_ryakushiki");
		projectCdRyohi = (String)shinseiData.get("project_cd");
		projectNameRyohi = (String)shinseiData.get("project_name");
		segmentCdRyohi = (String)shinseiData.get("segment_cd");
		segmentNameRyohi = (String)shinseiData.get("segment_name_ryakushiki");
		uf1CdRyohi = (String) shinseiData.get("uf1_cd");
		uf1NameRyohi = (String) shinseiData.get("uf1_name_ryakushiki");
		uf2CdRyohi = (String) shinseiData.get("uf2_cd");
		uf2NameRyohi = (String) shinseiData.get("uf2_name_ryakushiki");
		uf3CdRyohi = (String) shinseiData.get("uf3_cd");
		uf3NameRyohi = (String) shinseiData.get("uf3_name_ryakushiki");
		uf4CdRyohi = (String) shinseiData.get("uf4_cd");
		uf4NameRyohi = (String) shinseiData.get("uf4_name_ryakushiki");
		uf5CdRyohi = (String) shinseiData.get("uf5_cd");
		uf5NameRyohi = (String) shinseiData.get("uf5_name_ryakushiki");
		uf6CdRyohi = (String) shinseiData.get("uf6_cd");
		uf6NameRyohi = (String) shinseiData.get("uf6_name_ryakushiki");
		uf7CdRyohi = (String) shinseiData.get("uf7_cd");
		uf7NameRyohi = (String) shinseiData.get("uf7_name_ryakushiki");
		uf8CdRyohi = (String) shinseiData.get("uf8_cd");
		uf8NameRyohi = (String) shinseiData.get("uf8_name_ryakushiki");
		uf9CdRyohi = (String) shinseiData.get("uf9_cd");
		uf9NameRyohi = (String) shinseiData.get("uf9_name_ryakushiki");
		uf10CdRyohi = (String) shinseiData.get("uf10_cd");
		uf10NameRyohi = (String) shinseiData.get("uf10_name_ryakushiki");
		tekiyouRyohi = (String)shinseiData.get("tekiyou");
		shiharaibi = formatDate(shinseiData.get("shiharaibi"));
		if(sasihikiHyoujiFlg){
			sashihikiNum = (shinseiData.get("sashihiki_num") == null)? "" : shinseiData.get("sashihiki_num").toString();
			sashihikiTanka = (shinseiData.get("sashihiki_tanka") == null)? "": formatMoney(shinseiData.get("sashihiki_tanka"));
		} else {
			sashihikiNum = "";
			sashihikiTanka = "";
		}
		hosoku = (String)shinseiData.get("hosoku");
		this.kazeiKbnRyohi = (String) shinseiData.get("kari_kazei_kbn");
		this.bunriKbn = (String) shinseiData.get("bunri_kbn");
		this.kariShiireKbn = (String) shinseiData.get("kari_shiire_kbn");
		this.kashiShiireKbn = (String) shinseiData.get("kashi_shiire_kbn");
		this.invoiceDenpyou = (String) shinseiData.get("invoice_denpyou");
	}
	
	/**
	 * 明細のデフォルト状態に項目セットする。
	 */
	protected void makeDefaultMeisai() {
		setCommonMeisaiArray(-1);
		makeDefaultMeisaiKaigaiKoumoku();
	}
	
	/**
	 * デフォルト明細・海外項目分
	 */
	protected void makeDefaultMeisaiKaigaiKoumoku()
	{
	}

	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList 明細レコードのリスト
	 */
	protected void meisaiData2Gamen(List<GMap> meisaiList) {
		int length = meisaiList.size();
		this.setCommonMeisaiArray(length);
		this.setKaigaiMeisaiArray(length);
		for (int i = 0; i < length ; i++) {
			GMap meisai = meisaiList.get(i);
			this.setKaigaiMeisaiValues(i, meisai); // 海外判定に際して、先にセットする必要がある
			this.setCommonMeisaiValues(i, meisai);
		}
	}
	
	/**
	 * 明細共通部のセット。-1はmakeDefaultMeisai。
	 * @param length 配列長
	 */
	protected void setCommonMeisaiArray(int length)
	{
		// 明細の初期化(デフォルト値設定)
		boolean isDefault = length == -1;
		shubetsuCd = isDefault ? new String[] { "" } : new String[length];
		shubetsu1 = isDefault ? new String[] { "" } : new String[length];
		shubetsu2 = isDefault ? new String[] { "" } : new String[length];
		hayaFlg = isDefault ? new String[] { "" } : new String[length];
		yasuFlg = isDefault ? new String[] { "" } : new String[length];
		rakuFlg = isDefault ? new String[] { "" } : new String[length];
		kikanFrom = isDefault ? new String[] { "" } : new String[length];
		kikanTo = isDefault ? new String[] { "" } : new String[length];
		kyuujitsuNissuu = isDefault ? new String[] { "" } : new String[length];
		shouhyouShoruiHissuFlg = isDefault ? new String[] { "" } : new String[length];
		koutsuuShudan = isDefault ? new String[] { "" } : new String[length];
		ryoushuushoSeikyuushoTouFlg = isDefault ? new String[] { "" } : new String[length];
		naiyou = isDefault ? new String[] { "" } : new String[length];
		bikou = isDefault ? new String[] { "" } : new String[length];
		oufukuFlg = isDefault ? new String[] { "" } : new String[length];
		houjinCardFlgRyohi = isDefault ? new String[] { "" } : new String[length];
		kaishaTehaiFlgRyohi = isDefault ? new String[] { "" } : new String[length];
		jidounyuuryokuFlg = isDefault ? new String[] { "" } : new String[length];
		nissuu = isDefault ? new String[] { "" } : new String[length];
		suuryouNyuryokuType = isDefault ? new String[] { "" } : new String[length];
		tanka = isDefault ? new String[] { "" } : new String[length];
		suuryou = isDefault ? new String[] { "" } : new String[length];
		suuryouKigou = isDefault ? new String[] { "" } : new String[length];
		meisaiKingaku = isDefault ? new String[] { "" } : new String[length];
		icCardNo = isDefault ? new String[] { "" } : new String[length];
		icCardSequenceNo = isDefault ? new String[] { "" } : new String[length];
		himodukeCardMeisaiRyohi = isDefault ? new String[] { "" } : new String[length];
		shiharaisakiNameRyohi = isDefault ? new String[] { "" } : new String[length];
		jigyoushaKbnRyohi = isDefault ? new String[] { "" } : new String[length];
		zeinukiKingakuRyohi = isDefault ? new String[] { "" } : new String[length];
		shouhizeigakuRyohi = isDefault ? new String[] { "" } : new String[length];
		zeigakuFixFlg = isDefault ? new String[] { "" } : new String[length];
		nittouFlg = isDefault ? new String[] { "" } : new String[length];
	}
	
	/**
	 * 海外分明細の配列のセット
	 * @param length 配列長
	 */
	protected void setKaigaiMeisaiArray(int length)
	{
	}
	
	/**
	 * 明細共通部の値のセット
	 * @param i 配列番号
	 * @param meisai 明細マップ
	 */
	protected void setCommonMeisaiValues(int i, GMap meisai) 
	{
		suuryouNyuryokuType[i] = meisai.get("suuryou_nyuryoku_type");
		shubetsuCd[i] = (String)meisai.get("shubetsu_cd");
		shubetsu1[i] = (String)meisai.get("shubetsu1");
		shubetsu2[i] = (String)meisai.get("shubetsu2");
		hayaFlg[i] = (String)meisai.get("haya_flg");
		yasuFlg[i] = (String)meisai.get("yasu_flg");
		rakuFlg[i] = (String)meisai.get("raku_flg");
		kikanFrom[i] = formatDate(meisai.get("kikan_from"));
		kikanTo[i] = formatDate(meisai.get("kikan_to"));
		kyuujitsuNissuu[i] = null == meisai.get("kyuujitsu_nissuu") ? "" : meisai.get("kyuujitsu_nissuu").toString();
		shouhyouShoruiHissuFlg[i] = (String)meisai.get("shouhyou_shorui_hissu_flg");
		koutsuuShudan[i] = (String)meisai.get("koutsuu_shudan");
		ryoushuushoSeikyuushoTouFlg[i] = (String)meisai.get("ryoushuusho_seikyuusho_tou_flg");
		naiyou[i] = (String)meisai.get("naiyou");
		bikou[i] = (String)meisai.get("bikou");
		oufukuFlg[i] = (String)meisai.get("oufuku_flg");
		houjinCardFlgRyohi[i] = (String)meisai.get("houjin_card_riyou_flg");
		kaishaTehaiFlgRyohi[i] = (String)meisai.get("kaisha_tehai_flg");
		jidounyuuryokuFlg[i] = (String)meisai.get("jidounyuuryoku_flg");
		nissuu[i] = null == meisai.get("nissuu") ? "" : meisai.get("nissuu").toString();
		tanka[i] = shubetsuCd[i].equals(RYOHISEISAN_SYUBETSU.KOUTSUUHI) && !isKaigaiRyohiMeisai(i) && !suuryouNyuryokuType[i].equals("0")
				? formatMoneyDecimalWithNoPadding(meisai.get("tanka"))
				: formatMoney(meisai.get("tanka"));
		suuryou[i] = formatMoneyDecimalWithNoPadding(meisai.get("suuryou"));
		suuryouKigou[i] = meisai.get("suuryou_kigou");
		meisaiKingaku[i] = formatMoney(meisai.get("meisai_kingaku"));

		icCardNo[i] = (String)meisai.get("ic_card_no");
		icCardSequenceNo[i] = (String)meisai.get("ic_card_sequence_no");
		himodukeCardMeisaiRyohi[i] = null == meisai.get("himoduke_card_meisai") ? "" : meisai.get("himoduke_card_meisai").toString();
		shiharaisakiNameRyohi[i] = (String)meisai.get("shiharaisaki_name");
		jigyoushaKbnRyohi[i] = (String)meisai.get("jigyousha_kbn");
		zeinukiKingakuRyohi[i] = formatMoney(meisai.get("zeinuki_kingaku"));
		shouhizeigakuRyohi[i] = formatMoney(meisai.get("shouhizeigaku"));
		zeigakuFixFlg[i] = (String)meisai.get("zeigaku_fix_flg");
		// 海外の場合は後で海外ロジックで上書きされるので、一旦両者共通で問題ない
		nittouFlg[i] = commonLogic.isNittou(shubetsu1[i], shubetsu2[i], commonLogic.getYakushokuCd(userIdRyohi)) ? "1" : "";
	}
	
	/**
	 * 明細海外分の値のセット
	 * @param i 配列番号
	 * @param meisai 明細マップ
	 */
	protected void setKaigaiMeisaiValues(int i, GMap meisai)
	{
	}

	/**
	 * 明細レコードがない時、空の明細表示用に項目作成する。
	 */
	protected void makeDefaultMeisaiKeihi() {
		// 共通処理
		this.setCommonMeisaiArrayKeihi(-1);
		
		//消費税率マップだけ個別代入
		GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();
		zeiritsu[0] = initZeiritsu.getString("zeiritsu");
		keigenZeiritsuKbn[0] = initZeiritsu.get("keigen_zeiritsu_kbn");

		//制御情報
		kamokuEdabanEnable = new boolean[1];
		futanBumonEnable = new boolean[1];
		torihikisakiEnable = new boolean[1];
		projectEnable = new boolean[1];
		segmentEnable = new boolean[1];
		kousaihiEnable = new boolean[1];
		ninzuuEnable = new boolean[1];
		uf1Enable = new boolean[1];
		uf2Enable = new boolean[1];
		uf3Enable = new boolean[1];
		uf4Enable = new boolean[1];
		uf5Enable = new boolean[1];
		uf6Enable = new boolean[1];
		uf7Enable = new boolean[1];
		uf8Enable = new boolean[1];
		uf9Enable = new boolean[1];
		uf10Enable = new boolean[1];
		zeiritsuEnable = new boolean[1];
		
		this.makeDefaultMeisaiKeihiKaigaiKoumoku();
	}
	
	/**
	 * 経費明細・海外分項目のデフォルト作成
	 */
	protected void makeDefaultMeisaiKeihiKaigaiKoumoku()
	{
	}

	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList 明細レコードのリスト
	 * @param sanshou 参照起票か
	 * @param shinseiData 申請データ
	 */
	protected void meisaiData2GamenKeihi(List<GMap> meisaiList, boolean sanshou, GMap shinseiData) {
		int length = meisaiList.size();
		this.setCommonMeisaiArrayKeihi(length);
		this.setKaigaiMeisaiArrayKeihi(length);
		for (int i = 0; i < length ; i++) {
			GMap meisai = meisaiList.get(i);
			//参照起票の海外明細分と、伺い伝票を使用している場合で、分離区分がない場合は、値をセットする前に取引の最新の登録状況をチェック
			if((sanshou && "1".equals(meisai.get("kaigai_flg")))
				|| isEmpty(meisai.get("bunri_kbn"))) {
				//仕訳パターン取得
				ShiwakePatternMaster shiwakeP = shiwakePatternMasterDao.find("1".equals(meisai.get("kaigai_flg")) ?"A901" : "A001", meisai.get("shiwake_edano"));
				String kazeiKbnKeihi = shiwakeP.kariKazeiKbn;
				boolean isKazei = List.of("001", "011", "012", "002", "013", "014").contains(kazeiKbnKeihi);
				String bunriKeihi = shiwakeP.kariBunriKbn;
				var shoriGroup = this.kamokuMasterDao.find(meisai.get("kari_kamoku_cd")).shoriGroup;
				if(!isEmpty(meisai.get("kari_kamoku_edaban_cd"))) {
					var edaban = edabanZandaka.find(meisai.get("kari_kamoku_cd"), meisai.get("kari_kamoku_edaban_cd"));
					var hasKazeiKbn = edaban != null && edaban.kazeiKbn != null;
					//枝番課税区分・分離区分が存在したら課税区分・分離区分をそれぞれ塗り替え
					// リストにあるものはそのまま採用。自動系は仕入に置き換え、課税区分がない場合や、未対応課税区分は取引の設定を維持
					// 参照起票ではdisplaySeigyo本処理よりも下流にあり、このためドメインが初期化されていないので作っておく
					if(this.kazeiKbnDomain == null) {
						kazeiKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn");
						kazeiKbnDomain = kazeiKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
					}
					kazeiKbnKeihi = (hasKazeiKbn && List.of(kazeiKbnDomain).contains(String.format("%3s", edaban.kazeiKbn.toString()).replace(" ","0")))
								? String.format("%3s", edaban.getKazeiKbn().toString()).replace(" ","0")
								: (hasKazeiKbn && edaban.kazeiKbn == 18)
									? "0" + KAZEI_KBN.KAKOMI_SHIWAKE
									: (hasKazeiKbn && edaban.kazeiKbn == 19)
										? "0" + KAZEI_KBN.KANUKI_SHIIRE
										: (hasKazeiKbn && edaban.kazeiKbn == 49)
											? "0" + KAZEI_KBN.HIKAZEI_SHIIRE
											: kazeiKbnKeihi;
					isKazei = List.of("001", "011", "012", "002", "013", "014").contains(kazeiKbnKeihi);
					bunriKeihi = (edaban != null && edaban.bunriKbn != null)
						? edaban.bunriKbn != 3 // 税作成のときは「無し」（枝番選択ダイアログとそろえる）
							?edaban.bunriKbn.toString()
							: "0"
						: bunriKeihi;
		
					// 処理グループ&課税区分、消費税区分を考慮し、使用不可なら有効な区分に設定
					var shouhizeiKbn = this.getShouhizeikbn();
					if(!this.commonLogic.isValidBunriKbn(shoriGroup.toString(), kazeiKbnKeihi, bunriKeihi, shouhizeiKbn)) {
						bunriKeihi = (!List.of(1, 2).contains(shouhizeiKbn) || !isKazei)
							? "9"
							: (List.of("001", "011", "012").contains(kazeiKbnKeihi) && shouhizeiKbn == 1)
								? "1"
								: "0";
					}
				}
				meisai.put("kari_kazei_kbn", kazeiKbnKeihi);
				meisai.put("bunri_kbn", bunriKeihi);
				// 負担部門が存在する場合、部門仕入を確認
				String shiireKbnKeihi = commonLogic.bumonShiireCheck(shiwakeP.kariKamokuCd, meisai.get("kari_futan_bumon_cd"), kazeiKbnKeihi, shiwakeP.kariShiireKbn, this.getShiireZeiAnbun());
				meisai.put("kari_shiire_kbn", shiireKbnKeihi);
				// 課税区分・処理グループの組み合わせにより、事業者区分が変更可能ならtrue
				boolean jigyoushaKbnChange = (List.of("001", "002", "011", "013").contains(kazeiKbnKeihi) && List.of("2","5","6","7","8","10").contains(shoriGroup.toString())) || shoriGroup.toString().equals("21");
				// 伺いの場合（事業者区分 = 空の場合）、国内明細で課税区分、処理グループの組み合わせにより事業者区分が変更可能な場合、取引先マスタを参照して事業者区分をセット
				// 伺い、または通常起票の場合で事業者区分の変更が不可の場合、0(通常課税)としてセット
				String jigyoushaKbnSet  = meisai.get("jigyousha_kbn");
				if(isEmpty(meisai.get("jigyousha_kbn")) && !("1".equals(meisai.get("kaigai_flg"))) && jigyoushaKbnChange && !isEmpty(meisai.get("torihikisaki_cd"))) {
					GMap master = masterLogic.findTorihikisakiJouhou(meisai.get("torihikisaki_cd"), false);
					jigyoushaKbnSet = master == null ? "0" : (String)master.get("menzei_jigyousha_flg");
				}else if(isEmpty(meisai.get("jigyousha_kbn")) || !jigyoushaKbnChange) {
					jigyoushaKbnSet = "0";
				}
				meisai.put("jigyousha_kbn", jigyoushaKbnSet);
				meisai.put("kari_kamoku_cd", shiwakeP.kariKamokuCd);
				if(isKazei) {
					var zeikomiKingaku = (BigDecimal)meisai.get("shiharai_kingaku");
					var zeiritsuDecimal = (BigDecimal)meisai.get("zeiritsu");
					var jigyoushaKbnDecimal = jigyoushaKbnSet.equals("0") ?  BigDecimal.valueOf(1) :  BigDecimal.valueOf(0.8);
					//税率が0の場合、リストの一番最初の税率で上書き
					if(zeiritsu != null && meisai.get("zeiritsu").toString().equals("0")) {
						zeiritsuRyohiList = this.zeiritsuDao.loadNormalZeiritsu();
						zeiritsuDecimal = zeiritsuRyohiList.get(0).zeiritsu;
					}
					// 税額再計算 ※仮払いでの再計算ソース部分を一旦そのままコピペ
					var shouhizeigakuDecimal = zeikomiKingaku.multiply(zeiritsuDecimal).divide(new BigDecimal("100").add(zeiritsuDecimal), 3, RoundingMode.HALF_UP).multiply(jigyoushaKbnDecimal);
					var hasuuShoriFlg = this.getHasuuShoriFlg();
					shouhizeigakuDecimal = this.commonLogic.processShouhizeiFraction(hasuuShoriFlg, shouhizeigakuDecimal, true);
					var zeinukiKingaku = zeikomiKingaku.subtract(shouhizeigakuDecimal);

					meisai.put("hontai_kingaku", zeinukiKingaku);
					meisai.put("shouhizeigaku", shouhizeigakuDecimal);
				}else {
					//課税系でない場合は税抜き金額、消費税額ともに0固定
					meisai.put("hontai_kingaku", BigDecimal.ZERO);
					meisai.put("shouhizeigaku", BigDecimal.ZERO);
				}
			}
			this.setCommonMeisaiValuesKeihi(i, meisai, sanshou, shinseiData);
			this.setKaigaiMeisaiValuesKeihi(i, meisai);
		}
		if(!sanshou){
			//交際費関連の警告メッセージ取得
			chuukiKousai1 = "";
			List<GMap> resultList = commonLogic.checkKousaihiKijun(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN,shiwakeEdaNo,kousaihiHitoriKingaku, kaigaiFlg, commonLogic.KOUSAI_ERROR_CD_KEIKOKU);
			for(GMap res : resultList) {
				if(res.get("index") != null) {
					chuukiKousai2[(Integer)res.get("index")] = (String)res.get("errMsg");
				}else if(res.get("keikokuMsg") != null) {
					chuukiKousai1 += (isEmpty(chuukiKousai1) ? "" : "\r\n") + (String)res.get("keikokuMsg");
				}
			}
		}
	}
	
	/**
	 * 経費明細共通部のセット。-1はデフォルト用。
	 * @param length 配列長
	 */
	protected void setCommonMeisaiArrayKeihi(int length)
	{
		boolean isDefault = length == -1;
		shiwakeEdaNo = isDefault ? new String[] { "" } : new String[length];
		shiyoubi = isDefault ? new String[] { "" } : new String[length];
		shouhyouShorui = isDefault ? new String[] { "" } : new String[length];
		torihikiName = isDefault ? new String[] { "" } : new String[length];
		kamokuCd = isDefault ? new String[] { "" } : new String[length];
		kamokuName = isDefault ? new String[] { "" } : new String[length];
		kamokuEdabanCd = isDefault ? new String[] { "" } : new String[length];
		kamokuEdabanName = isDefault ? new String[] { "" } : new String[length];
		futanBumonCd = isDefault ? new String[] { "" } : new String[length];
		futanBumonName = isDefault ? new String[] { "" } : new String[length];
		torihikisakiCd = isDefault ? new String[] { "" } : new String[length];
		torihikisakiName = isDefault ? new String[] { "" } : new String[length];
		projectCd = isDefault ? new String[] { "" } : new String[length];
		projectName = isDefault ? new String[] { "" } : new String[length];
		segmentCd = isDefault ? new String[] { "" } : new String[length];
		segmentName = isDefault ? new String[] { "" } : new String[length];
		uf1Cd = isDefault ? new String[] { "" } : new String[length];
		uf1Name = isDefault ? new String[] { "" } : new String[length];
		uf2Cd = isDefault ? new String[] { "" } : new String[length];
		uf2Name = isDefault ? new String[] { "" } : new String[length];
		uf3Cd = isDefault ? new String[] { "" } : new String[length];
		uf3Name = isDefault ? new String[] { "" } : new String[length];
		uf4Cd = isDefault ? new String[] { "" } : new String[length];
		uf4Name = isDefault ? new String[] { "" } : new String[length];
		uf5Cd = isDefault ? new String[] { "" } : new String[length];
		uf5Name = isDefault ? new String[] { "" } : new String[length];
		uf6Cd = isDefault ? new String[] { "" } : new String[length];
		uf6Name = isDefault ? new String[] { "" } : new String[length];
		uf7Cd = isDefault ? new String[] { "" } : new String[length];
		uf7Name = isDefault ? new String[] { "" } : new String[length];
		uf8Cd = isDefault ? new String[] { "" } : new String[length];
		uf8Name = isDefault ? new String[] { "" } : new String[length];
		uf9Cd = isDefault ? new String[] { "" } : new String[length];
		uf9Name = isDefault ? new String[] { "" } : new String[length];
		uf10Cd = isDefault ? new String[] { "" } : new String[length];
		uf10Name = isDefault ? new String[] { "" } : new String[length];
		kazeiKbn = isDefault ? new String[] { "" } : new String[length];
		zeiritsu = isDefault ? new String[] { "" } : new String[length];
		keigenZeiritsuKbn = isDefault ? new String[] { "" } : new String[length];
		shiharaiKingaku = isDefault ? new String[] { "" } : new String[length];
		hontaiKingaku = isDefault ? new String[] { "" } : new String[length];
		shouhizeigaku = isDefault ? new String[] { "" } : new String[length];
		houjinCardFlgKeihi = isDefault ? new String[] { "" } : new String[length];
		kaishaTehaiFlgKeihi = isDefault ? new String[] { "" } : new String[length];
		tekiyou = isDefault ? new String[] { "" } : new String[length];
		chuuki2 = isDefault ? new String[] { "" } : new String[length];
		chuukiKousai2 = isDefault ? new String[] { "" } : new String[length];
		kousaihiShousai = isDefault ? new String[] { "" } : new String[length];
		kousaihiNinzuu = isDefault ? new String[] { "" } : new String[length];
		kousaihiHitoriKingaku = isDefault ? new String[] { "" } : new String[length];
		himodukeCardMeisaiKeihi = isDefault ? new String[] { "" } : new String[length];
		// デフォでは定義しない項目
		if(!isDefault)
		{
			kousaihiHyoujiFlg = new String[length];
			ninzuuRiyouFlg = new String[length];
		}
		shiharaisakiNameKeihi = isDefault ? new String[] { "" } : new String[length];
		jigyoushaKbnKeihi = isDefault ? new String[] { "" } : new String[length];
		bunriKbnKeihi = isDefault ? new String[] { "" } : new String[length];
		kariShiireKbnKeihi = isDefault ? new String[] { "" } : new String[length];
		kashiShiireKbnKeihi = isDefault ? new String[] { "" } : new String[length];
	}
	
	/**
	 * 経費明細海外部分の配列のセット
	 * @param length 配列長
	 */
	protected void setKaigaiMeisaiArrayKeihi(int length)
	{
	}
	
	/**
	 * 経費明細共通部のセット
	 * @param i 配列番号
	 * @param meisai 明細マップ
	 * @param sanshou 参照起票か
	 * @param shinseiData 申請データ
	 */
	protected void setCommonMeisaiValuesKeihi(int i, GMap meisai, boolean sanshou, GMap shinseiData)
	{
		shiharaiKingaku[i] = formatMoney(meisai.get("shiharai_kingaku"));
		shiwakeEdaNo[i] = ((Integer)meisai.get("shiwake_edano")).toString();
		shiyoubi[i] = formatDate(meisai.get("shiyoubi"));
		shouhyouShorui[i] = (String)meisai.get("shouhyou_shorui_flg");
		torihikiName[i] = (String)meisai.get("torihiki_name");
		kamokuCd[i] = (String)meisai.get("kari_kamoku_cd");
		kamokuName[i] = (String)meisai.get("kari_kamoku_name");
		kamokuEdabanCd[i] = (String)meisai.get("kari_kamoku_edaban_cd");
		kamokuEdabanName[i] = (String)meisai.get("kari_kamoku_edaban_name");
		futanBumonCd[i] = (String) meisai.get("kari_futan_bumon_cd");
		futanBumonName[i] = (String) meisai.get("kari_futan_bumon_name");
		torihikisakiCd[i] = (String) meisai.get("torihikisaki_cd");
		torihikisakiName[i] = (String) meisai.get("torihikisaki_name_ryakushiki");
		projectCd[i] = (String) meisai.get("project_cd");
		projectName[i] = (String) meisai.get("project_name");
		segmentCd[i] = (String) meisai.get("segment_cd");
		segmentName[i] = (String) meisai.get("segment_name_ryakushiki");
		uf1Cd[i] = (String) meisai.get("uf1_cd");
		uf1Name[i] = (String) meisai.get("uf1_name_ryakushiki");
		uf2Cd[i] = (String) meisai.get("uf2_cd");
		uf2Name[i] = (String) meisai.get("uf2_name_ryakushiki");
		uf3Cd[i] = (String) meisai.get("uf3_cd");
		uf3Name[i] = (String) meisai.get("uf3_name_ryakushiki");
		uf4Cd[i] = (String) meisai.get("uf4_cd");
		uf4Name[i] = (String) meisai.get("uf4_name_ryakushiki");
		uf5Cd[i] = (String) meisai.get("uf5_cd");
		uf5Name[i] = (String) meisai.get("uf5_name_ryakushiki");
		uf6Cd[i] = (String) meisai.get("uf6_cd");
		uf6Name[i] = (String) meisai.get("uf6_name_ryakushiki");
		uf7Cd[i] = (String) meisai.get("uf7_cd");
		uf7Name[i] = (String) meisai.get("uf7_name_ryakushiki");
		uf8Cd[i] = (String) meisai.get("uf8_cd");
		uf8Name[i] = (String) meisai.get("uf8_name_ryakushiki");
		uf9Cd[i] = (String) meisai.get("uf9_cd");
		uf9Name[i] = (String) meisai.get("uf9_name_ryakushiki");
		uf10Cd[i] = (String) meisai.get("uf10_cd");
		uf10Name[i] = (String) meisai.get("uf10_name_ryakushiki");
		kazeiKbn[i] = (String)meisai.get("kari_kazei_kbn");
		zeiritsu[i] = formatMoney(meisai.get("zeiritsu"));
		keigenZeiritsuKbn[i]= meisai.get("keigen_zeiritsu_kbn");
		hontaiKingaku[i] = formatMoney(meisai.get("hontai_kingaku"));
		shouhizeigaku[i] = formatMoney(meisai.get("shouhizeigaku"));
		houjinCardFlgKeihi[i]= (String)meisai.get("houjin_card_riyou_flg");
		kaishaTehaiFlgKeihi[i]= (String)meisai.get("kaisha_tehai_flg");
		tekiyou[i] = (String)meisai.get("tekiyou");
		kousaihiShousai[i] = (String)meisai.get("kousaihi_shousai");
		kousaihiHyoujiFlg[i]= (String)meisai.get("kousaihi_shousai_hyouji_flg");
		ninzuuRiyouFlg[i]= (String)meisai.get("kousaihi_ninzuu_riyou_flg");
		kousaihiNinzuu[i] = meisai.get("kousaihi_ninzuu") == null ? "" : ((Integer)meisai.get("kousaihi_ninzuu")).toString();
		kousaihiHitoriKingaku[i] = formatMoney(meisai.get("kousaihi_hitori_kingaku"));
		// 注記は明細ごとに個別に計算（DBには格納していない値）
		var chuuki = super.setChuuki(sanshou, shinseiData, meisai, DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, batchKaikeiLogic, commonLogic);
		this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : chuuki[0];
		this.chuuki2[i] = chuuki[1];
		himodukeCardMeisaiKeihi[i] = null == meisai.get("himoduke_card_meisai") ? "" : meisai.get("himoduke_card_meisai").toString();
		shiharaisakiNameKeihi[i] = (String)meisai.get("shiharaisaki_name");
		jigyoushaKbnKeihi[i] = (String)meisai.get("jigyousha_kbn");
		bunriKbnKeihi[i] = (String)meisai.get("bunri_kbn");
		kariShiireKbnKeihi[i] = (String)meisai.get("kari_shiire_kbn");
		kashiShiireKbnKeihi[i] = (String)meisai.get("kashi_shiire_kbn");
	}
	
	/**
	 * 経費明細海外項目分の値のセット
	 * @param i 明細番号
	 * @param meisai 明細マップ
	 */
	protected void setKaigaiMeisaiValuesKeihi(int i, GMap meisai)
	{
	}
	

	@Override
	protected void displaySeigyo(EteamConnection connection) {
		//サーバーからの送信値が半角スペース→&nbsp;になっていてマスターと不一致になる為
		EteamCommon.trimNoBlank(shubetsu1);
		EteamCommon.trimNoBlank(shubetsu2);
		EteamCommon.trimNoBlank(koutsuuShudan);

		//入力（登録、更新）可能かどうか判定
		enableInput = super.judgeEnableInput();

		// リスト作成
		zeiritsuRyohiList = this.zeiritsuDao.loadNormalZeiritsu();
		zeiritsuRyohiDomain = this.zeiritsuRyohiList.stream().map(item -> item.zeiritsu.toString()).collect(Collectors.toList()).toArray(new String[0]);
		zeiritsuList = this.zeiritsuDao.load();
		zeiritsuDomain = zeiritsuList.stream().map(item -> item.zeiritsu.toString()).collect(Collectors.toList()).toArray(new String[0]);
		kazeiKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn");
		kazeiKbnDomain = kazeiKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		bunriKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn");
		bunriKbnDomain = bunriKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		shiireKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn");
		shiireKbnDomain = shiireKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		koutsuuShudanList = masterLogic.loadKoutsuuShudan();
		koutsuuShudanDomain =  EteamCommon.mapList2Arr(koutsuuShudanList, "koutsuu_shudan");
		suuryouNyuryokuTypeDomain = EteamCommon.mapList2Arr(koutsuuShudanList, "suuryou_nyuryoku_type");
		shiharaihouhouList = commonLogic.makeShiharaiHouhouList(EteamSettingInfo.Key.SHIHARAI_HOUHOU_A004, shiharaihouhou);
		shiharaihouhouDomain = EteamCommon.mapList2Arr(shiharaihouhouList, "naibu_cd");
		jigyoushaKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn");
		jigyoushaKbnDomain = jigyoushaKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);

		//支払方法を入力可能にするかどうか判定。
		disableShiharaiHouhou = (shiharaihouhouList.size() <= 1);

		//ICカード履歴ボタンの表示判定（起票者のみ）
		if(enableInput) {
			if(sysLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.IC_CARD)){
				icCardEnable = true;
			}
		}

		//仮払案件ボタンの表示判定
		if(enableInput) {
			judgeKaribaraiAnken(connection);
		} else {
			userKaribaraiUmuFlg = "0";
		}

		//支払日の表示・入力判定。
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		//入力可能時の制御
		if (enableInput) {
			// 仕訳パターンによる制御
			if (isNotEmpty(shiwakeEdaNoRyohi)) {
				//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				String denpyouKbnTmp = DENPYOU_KBN.RYOHI_SEISAN;
				ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(denpyouKbnTmp, Integer.parseInt(shiwakeEdaNoRyohi));
				InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
				kamokuEdabanEnableRyohi = info.kamokuEdabanEnable;
				futanBumonEnableRyohi = info.futanBumonEnable;
				torihikisakiEnableRyohi = info.torihikisakiEnable;
				projectEnableRyohi = info.projectEnable;
				segmentEnableRyohi = info.segmentEnable;
				uf1EnableRyohi = info.uf1Enable;
				uf2EnableRyohi = info.uf2Enable;
				uf3EnableRyohi = info.uf3Enable;
				uf4EnableRyohi = info.uf4Enable;
				uf5EnableRyohi = info.uf5Enable;
				uf6EnableRyohi = info.uf6Enable;
				uf7EnableRyohi = info.uf7Enable;
				uf8EnableRyohi = info.uf8Enable;
				uf9EnableRyohi = info.uf9Enable;
				uf10EnableRyohi = info.uf10Enable;
			}
			// 駅すぱあと連携可否判定
			ekispertEnable = !"9".equals(setting.intraFlg());
			if(ekispertEnable) {
				// 駅すぱあと呼出 start
				super.ekispertInit(connection, SEARCH_MODE_UNCHIN);
			}
			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
			String initShainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
			if("HF1".equals(shainCdRenkeiArea)){ hf1Cd = initShainCd; }
			if("HF2".equals(shainCdRenkeiArea)){ hf2Cd = initShainCd; }
			if("HF3".equals(shainCdRenkeiArea)){ hf3Cd = initShainCd; }
			if("HF4".equals(shainCdRenkeiArea)){ hf4Cd = initShainCd; }
			if("HF5".equals(shainCdRenkeiArea)){ hf5Cd = initShainCd; }
			if("HF6".equals(shainCdRenkeiArea)){ hf6Cd = initShainCd; }
			if("HF7".equals(shainCdRenkeiArea)){ hf7Cd = initShainCd; }
			if("HF8".equals(shainCdRenkeiArea)){ hf8Cd = initShainCd; }
			if("HF9".equals(shainCdRenkeiArea)){ hf9Cd = initShainCd; }
		}

		//計上日の入力状態・モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン))を判定
		keijouBiMode = commonLogic.judgeKeijouBiMode(hasseiShugi, keijoubiDefaultSettei.equals("3"), denpyouId, loginUserInfo, enableInput, setting.keijouNyuuryoku());
		//計上日プルダウンのリスト取得
		if(keijouBiMode == 3) keijoubiList = masterLogic.loadKeijoubiList(denpyouKbn);

		//仮払選択表示設定
		GMap denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.getKaribaraiDenpyouKbn(denpyouKbn));
		if (denpyouShubetsuMap != null) {
			karibaraiIsEnabled = true;
		} else {
			karibaraiIsEnabled = false;
		}

		//経費明細の表示設定
		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg && !shiwakeEdaNo[0].isEmpty()) {

			//明細単位に仕訳パターンによる制御
			int length = shiwakeEdaNo.length;
			kamokuEdabanEnable = new boolean[length];
			futanBumonEnable = new boolean[length];
			torihikisakiEnable = new boolean[length];
			projectEnable = new boolean[length];
			segmentEnable = new boolean[length];
			kousaihiEnable = new boolean[length];
			ninzuuEnable = new boolean[length];
			uf1Enable = new boolean[length];
			uf2Enable = new boolean[length];
			uf3Enable = new boolean[length];
			uf4Enable = new boolean[length];
			uf5Enable = new boolean[length];
			uf6Enable = new boolean[length];
			uf7Enable = new boolean[length];
			uf8Enable = new boolean[length];
			uf9Enable = new boolean[length];
			uf10Enable = new boolean[length];
			zeiritsuEnable = new boolean[length];
			kazeiKbnCheck = new boolean[length];
			for (int i = 0; i < length; i++) {
				if (enableInput) {
					if (isNotEmpty(shiwakeEdaNo[i])) {

						//※以下はDenpyouMeisaiAction側で再定義しているため無効な制御変数

						//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
						//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
						//海外旅費・海外旅費仮払かつ海外フラグが１のとき、海外用の経費仕訳を取得する
						ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(this.isKaigaiKeihiMeisai(i) ? "A901" : DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, Integer.parseInt(shiwakeEdaNo[i]));
						InputEnableInfo info = commonLogic.judgeInputEnable(shiwakePattern);
						kamokuEdabanEnable[i] = info.kamokuEdabanEnable;
						futanBumonEnable[i] = info.futanBumonEnable;
						torihikisakiEnable[i] = info.torihikisakiEnable;
						projectEnable[i] = info.projectEnable;
						segmentEnable[i] = info.segmentEnable;
						uf1Enable[i] = info.uf1Enable;
						uf2Enable[i] = info.uf2Enable;
						uf3Enable[i] = info.uf3Enable;
						uf4Enable[i] = info.uf4Enable;
						uf5Enable[i] = info.uf5Enable;
						uf6Enable[i] = info.uf6Enable;
						uf7Enable[i] = info.uf7Enable;
						uf8Enable[i] = info.uf8Enable;
						uf9Enable[i] = info.uf9Enable;
						uf10Enable[i] = info.uf10Enable;
						zeiritsuEnable[i] = info.zeiritsuEnable;
						kousaihiEnable[i] = info.kousaihiEnable;
						ninzuuEnable[i] = info.ninzuuEnable;
					}
				} else {
					kousaihiEnable[i] = "1".equals(kousaihiHyoujiFlg[i]);
					ninzuuEnable[i] = "1".equals(ninzuuRiyouFlg[i]);
				}
				kazeiKbnCheck[i] = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]);
			}
		}

		// 代理起票フラグ（パラメータ）
		if(isNotEmpty(denpyouId)) {
			GMap data = denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) ? kaikeiLogic.findRyohiSeisan(denpyouId)  : kaikeiLogic.findKaigaiRyohiSeisan(denpyouId);
			dairiFlg = data.get("dairiflg").toString();
		}
		else {
			HttpServletRequest request = null;
			try {
				request =  ServletActionContext.getRequest();
			} catch(NullPointerException e) {
				//UTテスト用（ServletActionContextを使用しない）
			}
			if (null != request && null != request.getQueryString()) {
				String[] param = request.getQueryString().split("&", 0);
				for (int i = 0 ; i < param.length ; i++){
					if(param[i].equals("dairiFlg=1")) {
						dairiFlg = "1";
						break;
					}
				}
			}
		}

		// 法人カードの表示可否
		houjinCardFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		GMap houjinCardUserInfo = bumonUsrLogic.selectUserInfo(getUser().getSeigyoUserId());
		if( ((houjinCardUserInfo != null && "1".equals(houjinCardUserInfo.get("houjin_card_riyou_flag"))) || "1".equals(dairiFlg) ) && houjinCardFlag == true ){
			houjinCardRirekiEnable = true;
		}else{
			houjinCardRirekiEnable = false;
		}

		// 会社手配の表示可否
		kaishaTehaiFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);

		//仮払申請の起案添付済みフラグを設定（新規起票以外）
		if(isNotEmpty(denpyouId) && isEmpty(shiyoushaHenkouShainNo)){
			if(commonLogic.isKaribaraiKianTenpuZumi(denpyouId, karibaraiDenpyouId)){
				karibaraiKianTenpuZumiFlg = "1";
			}else{
				karibaraiKianTenpuZumiFlg = "0";
			}
		}
		
		//課税区分の制御
		if (isNotEmpty(kamokuCdRyohi)) {
			var kamoku = this.kamokuMasterDao.find(this.kamokuCdRyohi);
			this.shoriGroupRyohi = kamoku == null ? null : kamoku.shoriGroup;
		}
	}
	
	//登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){
		BigDecimal zandaka = (BigDecimal)ringiData.get("ringiKingakuZandaka");

		//仮払未選択または（仮払選択済みで）仮払ありの場合
		if("".equals(karibaraiDenpyouId) || Domain.FLG[0].equals(karibaraiOn)){
			zandaka = zandaka.subtract(toDecimal(kingaku));

			//仮払が稟議金額を引継いでいる場合は仮払金額残高に加算する。
			if(0 != karibaraiKingaku.length()){
				// 稟議金額の引継ぎ元を特定
				GMap motoDenpyouInfo = new GMap();
				motoDenpyouInfo = wfLogic.findRingiMotoDenpyouId(motoDenpyouInfo, karibaraiDenpyouId);
				String motoDenpyouId = motoDenpyouInfo.get("ringi_kingaku_hikitsugimoto_denpyou_base").toString();
				if(!"".equals(motoDenpyouId)){
					zandaka = zandaka.add(toDecimal(karibaraiKingaku));
				}
			}
		}
		ringiData.put("ringiKingakuZandaka", zandaka);
	}

	//稟議金額の表示有無を判定する。
	@Override
	protected boolean judgeRingiKingakuHyoujiKobetsu(){
		boolean isCalc = true;
		// 仮払なしの場合は稟議金額を表示しない
		if (Domain.FLG[1].equals(karibaraiOn)){
			isCalc = false;
		}
		return isCalc;
	}

	//画面に入力された仮払伝票IDを呼び出し元に返却する。
	@Override
	protected String getKaribaraiDenpyouIdKobetsu(){
		return this.karibaraiDenpyouId;
	}

	// 申請内容が仮払金未使用・出張中止かどうか。
	@Override
	protected boolean isKaribaraiMishiyou() {
		boolean result = false;
		if("1".equals(this.karibaraiMishiyouFlg) || "1".equals(this.shucchouChuushiFlg)){
			result =  true;
		}
		return result;
	}
	/**
	 * ユーザーに対して仮払案件があるか判定する。
	 * @param connection コネクション
	 */
	protected void judgeKaribaraiAnken(EteamConnection connection) {
		// 通常起票の場合
		if(dairiFlg.equals("0")){
			userKaribaraiUmuFlg = "0";
			// 子画面の初期表示イベントに合わせて渡す。
			List<GMap> karibaraiUmuList = kaikeiLogic.loadKaribaraiAnken(denpyouKbn , "", userIdRyohi);

			if(! karibaraiUmuList.isEmpty()){
				userKaribaraiUmuFlg = "1";
			}
		//代理起票の場合
		}else{
			userKaribaraiUmuFlg = "1";
		}
	}

	/**
	 * 支払日のチェックを行う。エラーがあれば、エラーリストにメッセージが詰まる。
	 * @param shiharai 支払日
	 * @param keijou 計上日
	 * @param keijouName 計上日項目名
	 */
	protected void checkShiharaiBi(String shiharai, String keijou, String keijouName) {
		//出張中止ONなら支払日チェックしない
		if ( isNotEmpty(shucchouChuushiFlg) && shucchouChuushiFlg.equals("1") )
		{
			return;
		}

		String pShiharaihouhou = (0 < toDecimal(sashihikiShikyuuKingaku).compareTo(BigDecimal.ZERO)) ? shiharaihouhou : SHIHARAI_HOUHOU.GENKIN;//差引支給金額が0以下なら（つまり本当に振込があるなら）支払方法と関係なく当日支払日許容とする。
		commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), toDate(keijou), keijouName, pShiharaihouhou, loginUserInfo, errorList);
	}

	/**
	 * 領収書が必要かのチェックを行う。
	 * 出張旅費精算（仮払精算）追加した明細の中に「領収書・請求書等」のチェックがオンの明細が1件以上ある場合
	 * @return 領収書が必要か
	 */
	protected boolean isUseShouhyouShorui(){
		for (int i = 0; i < shubetsuCd.length; i++) {
			if("1".equals(ryoushuushoSeikyuushoTouFlg[i])){
				return true;
			}
		}
		for (int i = 0; i < shouhyouShorui.length; i++) {
			if("1".equals(shouhyouShorui[i])){
				return true;
			}
		}
		return false;
	}

	/**
	 * 代理起票できるかのチェック
	 */
	protected void dairiCheck() {
		//代理起票できるかチェック
		if (dairiFlg.equals("1")) {
			//代理権限ないユーザーはできない（メニューで非表示だがURL直接来られた時制御が破綻するし）
			GMap userJouhouMap = bumonUsrLogic.selectUserJouhou(getUser().getSeigyoUserId());
			String userDairiKihyouFlg = (String)userJouhouMap.get("dairikihyou_flg");
			if (userDairiKihyouFlg.equals("0")) {
				throw new EteamAccessDeniedException("代理起票権限ないユーザーでの代理起票は不可能");
			}
			//代行モードで代理起票させない（メニューで非表示だがURL直接来られた時制御が破綻するし）
			if (! getUser().getLoginUserId().equals(getUser().getSeigyoUserId())) {
				throw new EteamAccessDeniedException("代行モードでの代理起票は不可能");
			}
		}
	}
	
	/**
	 * DB登録前にマスターから名称を取得する。（クライアントから送られた名称は破棄）
	 * @param connection コネクション
	 */
	protected void reloadMaster(EteamConnection connection) {
		//画面表示項目
		kamokuNameRyohi = this.kamokuMasterDao.findKamokuNameStr(kamokuCdRyohi);
		kamokuEdabanNameRyohi = this.kamokuEdabanZandakaDao.findEdabanName(kamokuCdRyohi, kamokuEdabanCdRyohi);
		futanBumonNameRyohi = enableBumonSecurity ? this.bumonMasterDao.findFutanBumonName(futanBumonCdRyohi) : "";
		torihikisakiNameRyohi = masterLogic.findTorihikisakiName(torihikisakiCdRyohi);
		projectNameRyohi = masterLogic.findProjectName(projectCdRyohi);
		segmentNameRyohi = masterLogic.findSegmentName(segmentCdRyohi);

		hf1Name = masterLogic.findHfName("1", hf1Cd);
		hf2Name = masterLogic.findHfName("2", hf2Cd);
		hf3Name = masterLogic.findHfName("3", hf3Cd);
		hf4Name = masterLogic.findHfName("4", hf4Cd);
		hf5Name = masterLogic.findHfName("5", hf5Cd);
		hf6Name = masterLogic.findHfName("6", hf6Cd);
		hf7Name = masterLogic.findHfName("7", hf7Cd);
		hf8Name = masterLogic.findHfName("8", hf8Cd);
		hf9Name = masterLogic.findHfName("9", hf9Cd);
		hf10Name = masterLogic.findHfName("10", hf10Cd);

		uf1NameRyohi = masterLogic.findUfName("1", uf1CdRyohi);
		uf2NameRyohi = masterLogic.findUfName("2", uf2CdRyohi);
		uf3NameRyohi = masterLogic.findUfName("3", uf3CdRyohi);
		uf4NameRyohi = masterLogic.findUfName("4", uf4CdRyohi);
		uf5NameRyohi = masterLogic.findUfName("5", uf5CdRyohi);
		uf6NameRyohi = masterLogic.findUfName("6", uf6CdRyohi);
		uf7NameRyohi = masterLogic.findUfName("7", uf7CdRyohi);
		uf8NameRyohi = masterLogic.findUfName("8", uf8CdRyohi);
		uf9NameRyohi = masterLogic.findUfName("9", uf9CdRyohi);
		uf10NameRyohi = masterLogic.findUfName("10", uf10CdRyohi);

		//貸方
		kashiKamokuNameRyohi = this.kamokuMasterDao.findKamokuNameStr(kashiKamokuCdRyohi);
		kashiKamokuEdabanNameRyohi = this.kamokuEdabanZandakaDao.findEdabanName(kashiKamokuCdRyohi, kashiKamokuEdabanCdRyohi);
		kashiFutanBumonNameRyohi = this.bumonMasterDao.findFutanBumonName(kashiFutanBumonCdRyohi);

		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//とりあえず画面表示のない明細項目について、領域確保
			int length = shiwakeEdaNo.length;
			kashiFutanBumonName = new String[length];
			kashiKamokuName = new String[length];
			kashiKamokuEdabanName = new String[length];

			//明細項目を１件ずつ変換
			for (int i = 0; i < length; i++) {
				futanBumonName[i] = enableMeisaiBumonSecurity[i] ? this.bumonMasterDao.findFutanBumonName(futanBumonCd[i]) : "";
				torihikisakiName[i] = masterLogic.findTorihikisakiName(torihikisakiCd[i]);
				kamokuName[i] = this.kamokuMasterDao.findKamokuNameStr(kamokuCd[i]);
				kamokuEdabanName[i] = this.kamokuEdabanZandakaDao.findEdabanName(kamokuCd[i], kamokuEdabanCd[i]);
				segmentName[i] = masterLogic.findSegmentName(segmentCd[i]);
				projectName[i] = masterLogic.findProjectName(projectCd[i]);
				uf1Name[i] = masterLogic.findUfName("1", uf1Cd[i]);
				uf2Name[i] = masterLogic.findUfName("2", uf2Cd[i]);
				uf3Name[i] = masterLogic.findUfName("3", uf3Cd[i]);
				uf4Name[i] = masterLogic.findUfName("4", uf4Cd[i]);
				uf5Name[i] = masterLogic.findUfName("5", uf5Cd[i]);
				uf6Name[i] = masterLogic.findUfName("6", uf6Cd[i]);
				uf7Name[i] = masterLogic.findUfName("7", uf7Cd[i]);
				uf8Name[i] = masterLogic.findUfName("8", uf8Cd[i]);
				uf9Name[i] = masterLogic.findUfName("9", uf9Cd[i]);
				uf10Name[i] = masterLogic.findUfName("10", uf10Cd[i]);
				kashiFutanBumonName[i] = this.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd[i]);
				kashiKamokuName[i] = this.kamokuMasterDao.findKamokuNameStr(kashiKamokuCd[i]);
				kashiKamokuEdabanName[i] = this.kamokuEdabanZandakaDao.findEdabanName(kashiKamokuCd[i], kashiKamokuEdabanCd[i]);
			}
		}
		
		reloadKaigaiMaster();
	}
	
	/**
	 * 海外分マスター名称のリロード
	 */
	protected void reloadKaigaiMaster()
	{
	}
	
	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {}

	/**
	 * 国内仕訳分までのリロード
	 * @param connection コネクション
	 */
	protected void reloadShiwakePatternKokunai(EteamConnection connection)
	{

		// 社員コード取得
		GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
		// 法人カード識別用番号取得
		String houjinCard = (userInfo == null) ? "" : (String)userInfo.get("houjin_card_shikibetsuyou_num");
		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(userIdRyohi);
		
		// 仮払情報と仮払金額
		GMap karibaraiResult = super.isEmpty(karibaraiDenpyouId)
			? null
			: isKaigai
				? kaikeiLogic.findKaigaiRyohiKaribarai(karibaraiDenpyouId)
				: kaikeiLogic.findRyohiKaribarai(karibaraiDenpyouId);
		BigDecimal tmpKaribaraiKingaku = (GMap.isNotNull(karibaraiResult, "karibarai_kingaku") && karibaraiResult.get("karibarai_on").equals("1"))
				? karibaraiResult.get("karibarai_kingaku")
				: BigDecimal.ZERO;

		//差引支給金額の計算
		if(super.isNotEmpty(kingaku)) {
			BigDecimal tmpShiharaiKingaku = toDecimal(kingaku);
			BigDecimal tmpHoujinKingaku = toDecimal(houjinCardRiyouGoukei);
			BigDecimal tmpKaishaTehaiKingaku = toDecimal(kaishaTehaiGoukei);
			sashihikiShikyuuKingaku = formatMoney(((tmpShiharaiKingaku.subtract(tmpHoujinKingaku)).subtract(tmpKaishaTehaiKingaku)).subtract(tmpKaribaraiKingaku));
		}
		//国内の仕訳
		if (isNotEmpty(shiwakeEdaNoRyohi)) {

			//仕訳パターン取得
			String denpyouKbnTmp = DENPYOU_KBN.RYOHI_SEISAN;
			ShiwakePatternMaster shiwakeP = shiwakePatternMasterDao.find(denpyouKbnTmp, Integer.parseInt(shiwakeEdaNoRyohi));
			// 使用者の代表負担部門コード
			String daihyouBumonCd = super.daihyouFutanBumonCd;
			// 代理起票時、起票者=使用者でない場合はユーザーに紐付く代表負担部門コード（先頭）を取得する。
			if (dairiFlg.equals("1") && !(super.getKihyouUserId().equals(userIdRyohi))) {
				daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
			}

			//取引名
			torihikiNameRyohi = shiwakeP.torihikiName;

			//借方　科目
			kamokuCdRyohi = shiwakeP.kariKamokuCd;

			//借方　科目枝番
			String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
			switch (pKariKamokuEdabanCd) {
				//何もしない(画面入力のまま)
				case EteamConst.ShiwakeConst.EDABAN:
					break;
				//固定コード値 or ブランク
				default:
					kamokuEdabanCdRyohi = pKariKamokuEdabanCd;
					break;
			}

			//借方　UF1-10
			if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.shainCdRenkeiFlg.equals(("1"))){
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
				{
					 uf1CdRyohi = shainCd;
				}
				if (ufno == 2)
				{
					 uf2CdRyohi = shainCd;
				}
				if (ufno == 3)
				{
					 uf3CdRyohi = shainCd;
				}
				if (ufno == 4)
				{
					 uf4CdRyohi = shainCd;
				}
				if (ufno == 5)
				{
					 uf5CdRyohi = shainCd;
				}
				if (ufno == 6)
				{
					 uf6CdRyohi = shainCd;
				}
				if (ufno == 7)
				{
					 uf7CdRyohi = shainCd;
				}
				if (ufno == 8)
				{
					 uf8CdRyohi = shainCd;
				}
				if (ufno == 9)
				{
					 uf9CdRyohi = shainCd;
				}
				if (ufno == 10)
				{
					uf10CdRyohi = shainCd;
				}
			}
			kariUf1CdRyohi = commonLogic.convUf(uf1CdRyohi, shiwakeP.kariUf1Cd);
			kariUf2CdRyohi = commonLogic.convUf(uf2CdRyohi, shiwakeP.kariUf2Cd);
			kariUf3CdRyohi = commonLogic.convUf(uf3CdRyohi, shiwakeP.kariUf3Cd);
			kariUf4CdRyohi = commonLogic.convUf(uf4CdRyohi, shiwakeP.kariUf4Cd);
			kariUf5CdRyohi = commonLogic.convUf(uf5CdRyohi, shiwakeP.kariUf5Cd);
			kariUf6CdRyohi = commonLogic.convUf(uf6CdRyohi, shiwakeP.kariUf6Cd);
			kariUf7CdRyohi = commonLogic.convUf(uf7CdRyohi, shiwakeP.kariUf7Cd);
			kariUf8CdRyohi = commonLogic.convUf(uf8CdRyohi, shiwakeP.kariUf8Cd);
			kariUf9CdRyohi = commonLogic.convUf(uf9CdRyohi, shiwakeP.kariUf9Cd);
			kariUf10CdRyohi = commonLogic.convUf(uf10CdRyohi, shiwakeP.kariUf10Cd);

			//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
			enableBumonSecurity = true;
			//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
			if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) ){
				enableBumonSecurity = commonLogic.checkFutanBumonWithSecurity(futanBumonCdRyohi, ks.futanBumon.getName() + "コード：" , super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
			}
			// 借方 負担部門
			futanBumonCdRyohi = commonLogic.convFutanBumon(futanBumonCdRyohi, shiwakeP.kariFutanBumonCd, daihyouBumonCd);
			
			// 処理グループ&課税区分、仕入税額按分を考慮し、使用不可なら0に戻す
			if(shoriGroupRyohi == null) {
				shoriGroupRyohi = this.kamokuMasterDao.find(this.kamokuCdRyohi).shoriGroup;
			}
			
			// 代表部門の時の仕入区分
			if(List.of(EteamConst.ShiwakeConst.DAIHYOUBUMON, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) && this.futanBumonCdRyohi.equals(daihyouBumonCd)) {
				// 一旦代表部門の仕入区分をセット
				var shiireKbn = this.bumonMasterDao.find(this.futanBumonCdRyohi).shiireKbn;
				this.kariShiireKbn = shiireKbn == null ? this.kariShiireKbn : shiireKbn.toString();
				if(!this.commonLogic.isValidShiireKbn(this.shoriGroupRyohi.toString(), this.kazeiKbnRyohi, this.kariShiireKbn, this.getShiireZeiAnbun())) {
					this.kariShiireKbn = "0";
				}
			}
			
			//申請内容の処理グループ、課税区分の組み合わせが事業者区分の変更不可の場合、明細の事業者区分は通常課税固定にする
			if (!((List.of("001", "002", "011", "013").contains(kazeiKbnRyohi) && List.of("2","5","6","7","8","10").contains(this.shoriGroupRyohi.toString()))
					|| this.shoriGroupRyohi.toString().equals("21"))) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					jigyoushaKbnRyohi[i] = "0";
				}
			}

			// 借方　課税区分
			//kazeiKbnRyohi = shiwakeP.kariKazeiKbn;

			//支払方法により貸方1 or 貸方2を切替する
			switch (shiharaihouhou) {

				case SHIHARAI_HOUHOU.FURIKOMI:
					//振込
					kashiKamokuCdRyohi = shiwakeP.kashiKamokuCd1; //貸方1　科目コード
					//貸方1　科目枝番コード
					String pKashiKamokuEdabanCd = shiwakeP.kashiKamokuEdabanCd1;
					this.kashiKamokuEdabanCdRyohi = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
							? shainShiwakeEdaNo
							: pKashiKamokuEdabanCd;
					kashiFutanBumonCdRyohi = shiwakeP.kashiFutanBumonCd1; //貸方1　負担部門コード
					kashiKazeiKbnRyohi = shiwakeP.kashiKazeiKbn1; //貸方1　課税区分
					kashiShiireKbn = (shiwakeP.kashiShiireKbn1 != null) ? shiwakeP.kashiShiireKbn1 : "" ;
					break;

				case SHIHARAI_HOUHOU.GENKIN:
					//現金
					kashiKamokuCdRyohi = shiwakeP.kashiKamokuCd2; //貸方2　科目コード
					kashiKamokuEdabanCdRyohi = shiwakeP.kashiKamokuEdabanCd2; //貸方2　科目枝番コード
					kashiFutanBumonCdRyohi = shiwakeP.kashiFutanBumonCd2; //貸方2　負担部門コード
					kashiKazeiKbnRyohi = shiwakeP.kashiKazeiKbn2; //貸方2　課税区分
					kashiShiireKbn = (shiwakeP.kashiShiireKbn2 != null) ? shiwakeP.kashiShiireKbn2 : "" ;
					break;
			}

			//代表部門
			kashiFutanBumonCdRyohi = commonLogic.convFutanBumon(futanBumonCdRyohi, kashiFutanBumonCdRyohi, daihyouBumonCd);

			kashiUf1Cd1Ryohi = commonLogic.convUf(uf1CdRyohi, shiwakeP.kashiUf1Cd1);
			kashiUf2Cd1Ryohi = commonLogic.convUf(uf2CdRyohi, shiwakeP.kashiUf2Cd1);
			kashiUf3Cd1Ryohi = commonLogic.convUf(uf3CdRyohi, shiwakeP.kashiUf3Cd1);
			kashiUf4Cd1Ryohi = commonLogic.convUf(uf4CdRyohi, shiwakeP.kashiUf4Cd1);
			kashiUf5Cd1Ryohi = commonLogic.convUf(uf5CdRyohi, shiwakeP.kashiUf5Cd1);
			kashiUf6Cd1Ryohi = commonLogic.convUf(uf6CdRyohi, shiwakeP.kashiUf6Cd1);
			kashiUf7Cd1Ryohi = commonLogic.convUf(uf7CdRyohi, shiwakeP.kashiUf7Cd1);
			kashiUf8Cd1Ryohi = commonLogic.convUf(uf8CdRyohi, shiwakeP.kashiUf8Cd1);
			kashiUf9Cd1Ryohi = commonLogic.convUf(uf9CdRyohi, shiwakeP.kashiUf9Cd1);
			kashiUf10Cd1Ryohi = commonLogic.convUf(uf10CdRyohi, shiwakeP.kashiUf10Cd1);

			kashiUf1Cd2Ryohi = commonLogic.convUf(uf1CdRyohi, shiwakeP.kashiUf1Cd2);
			kashiUf2Cd2Ryohi = commonLogic.convUf(uf2CdRyohi, shiwakeP.kashiUf2Cd2);
			kashiUf3Cd2Ryohi = commonLogic.convUf(uf3CdRyohi, shiwakeP.kashiUf3Cd2);
			kashiUf4Cd2Ryohi = commonLogic.convUf(uf4CdRyohi, shiwakeP.kashiUf4Cd2);
			kashiUf5Cd2Ryohi = commonLogic.convUf(uf5CdRyohi, shiwakeP.kashiUf5Cd2);
			kashiUf6Cd2Ryohi = commonLogic.convUf(uf6CdRyohi, shiwakeP.kashiUf6Cd2);
			kashiUf7Cd2Ryohi = commonLogic.convUf(uf7CdRyohi, shiwakeP.kashiUf7Cd2);
			kashiUf8Cd2Ryohi = commonLogic.convUf(uf8CdRyohi, shiwakeP.kashiUf8Cd2);
			kashiUf9Cd2Ryohi = commonLogic.convUf(uf9CdRyohi, shiwakeP.kashiUf9Cd2);
			kashiUf10Cd2Ryohi = commonLogic.convUf(uf10CdRyohi, shiwakeP.kashiUf10Cd2);

			kashiUf1Cd3Ryohi = commonLogic.convUf(uf1CdRyohi, shiwakeP.kashiUf1Cd3);
			kashiUf2Cd3Ryohi = commonLogic.convUf(uf2CdRyohi, shiwakeP.kashiUf2Cd3);
			kashiUf3Cd3Ryohi = commonLogic.convUf(uf3CdRyohi, shiwakeP.kashiUf3Cd3);
			kashiUf4Cd3Ryohi = commonLogic.convUf(uf4CdRyohi, shiwakeP.kashiUf4Cd3);
			kashiUf5Cd3Ryohi = commonLogic.convUf(uf5CdRyohi, shiwakeP.kashiUf5Cd3);
			kashiUf6Cd3Ryohi = commonLogic.convUf(uf6CdRyohi, shiwakeP.kashiUf6Cd3);
			kashiUf7Cd3Ryohi = commonLogic.convUf(uf7CdRyohi, shiwakeP.kashiUf7Cd3);
			kashiUf8Cd3Ryohi = commonLogic.convUf(uf8CdRyohi, shiwakeP.kashiUf8Cd3);
			kashiUf9Cd3Ryohi = commonLogic.convUf(uf9CdRyohi, shiwakeP.kashiUf9Cd3);
			kashiUf10Cd3Ryohi = commonLogic.convUf(uf10CdRyohi, shiwakeP.kashiUf10Cd3);

			kashiUf1Cd4Ryohi = commonLogic.convUf(uf1CdRyohi, shiwakeP.kashiUf1Cd4);
			kashiUf2Cd4Ryohi = commonLogic.convUf(uf2CdRyohi, shiwakeP.kashiUf2Cd4);
			kashiUf3Cd4Ryohi = commonLogic.convUf(uf3CdRyohi, shiwakeP.kashiUf3Cd4);
			kashiUf4Cd4Ryohi = commonLogic.convUf(uf4CdRyohi, shiwakeP.kashiUf4Cd4);
			kashiUf5Cd4Ryohi = commonLogic.convUf(uf5CdRyohi, shiwakeP.kashiUf5Cd4);
			kashiUf6Cd4Ryohi = commonLogic.convUf(uf6CdRyohi, shiwakeP.kashiUf6Cd4);
			kashiUf7Cd4Ryohi = commonLogic.convUf(uf7CdRyohi, shiwakeP.kashiUf7Cd4);
			kashiUf8Cd4Ryohi = commonLogic.convUf(uf8CdRyohi, shiwakeP.kashiUf8Cd4);
			kashiUf9Cd4Ryohi = commonLogic.convUf(uf9CdRyohi, shiwakeP.kashiUf9Cd4);
			kashiUf10Cd4Ryohi = commonLogic.convUf(uf10CdRyohi, shiwakeP.kashiUf10Cd4);

			kashiUf1Cd5Ryohi = commonLogic.convUf(uf1CdRyohi, shiwakeP.kashiUf1Cd5);
			kashiUf2Cd5Ryohi = commonLogic.convUf(uf2CdRyohi, shiwakeP.kashiUf2Cd5);
			kashiUf3Cd5Ryohi = commonLogic.convUf(uf3CdRyohi, shiwakeP.kashiUf3Cd5);
			kashiUf4Cd5Ryohi = commonLogic.convUf(uf4CdRyohi, shiwakeP.kashiUf4Cd5);
			kashiUf5Cd5Ryohi = commonLogic.convUf(uf5CdRyohi, shiwakeP.kashiUf5Cd5);
			kashiUf6Cd5Ryohi = commonLogic.convUf(uf6CdRyohi, shiwakeP.kashiUf6Cd5);
			kashiUf7Cd5Ryohi = commonLogic.convUf(uf7CdRyohi, shiwakeP.kashiUf7Cd5);
			kashiUf8Cd5Ryohi = commonLogic.convUf(uf8CdRyohi, shiwakeP.kashiUf8Cd5);
			kashiUf9Cd5Ryohi = commonLogic.convUf(uf9CdRyohi, shiwakeP.kashiUf9Cd5);
			kashiUf10Cd5Ryohi = commonLogic.convUf(uf10CdRyohi, shiwakeP.kashiUf10Cd5);

			//画面に反映
			if (!isEmpty(kariUf1CdRyohi))
			{
				uf1CdRyohi = kariUf1CdRyohi;
			}
			if (!isEmpty(kariUf2CdRyohi))
			{
				uf2CdRyohi = kariUf2CdRyohi;
			}
			if (!isEmpty(kariUf3CdRyohi))
			{
				uf3CdRyohi = kariUf3CdRyohi;
			}
			if (!isEmpty(kariUf4CdRyohi))
			{
				uf4CdRyohi = kariUf4CdRyohi;
			}
			if (!isEmpty(kariUf5CdRyohi))
			{
				uf5CdRyohi = kariUf5CdRyohi;
			}
			if (!isEmpty(kariUf6CdRyohi))
			{
				uf6CdRyohi = kariUf6CdRyohi;
			}
			if (!isEmpty(kariUf7CdRyohi))
			{
				uf7CdRyohi = kariUf7CdRyohi;
			}
			if (!isEmpty(kariUf8CdRyohi))
			{
				uf8CdRyohi = kariUf8CdRyohi;
			}
			if (!isEmpty(kariUf9CdRyohi))
			{
				uf9CdRyohi = kariUf9CdRyohi;
			}
			if (!isEmpty(kariUf10CdRyohi))
			{
				uf10CdRyohi = kariUf10CdRyohi;
			}

			//社員コードを摘要コードに反映する場合
			if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
				if(shainCd.length() > 4) {
					tekiyouCdRyohi = shainCd.substring(shainCd.length()-4);
				} else {
					tekiyouCdRyohi = shainCd;
				}
			//法人カード識別用番号を摘要コードに反映する場合
			} else if("T".equals(setting.houjinCardRenkei())) {
				if(houjinCard.length() > 4) {
					tekiyouCdRyohi = houjinCard.substring(houjinCard.length()-4);
				} else {
					tekiyouCdRyohi = houjinCard;
				}
			}  else {
				tekiyouCdRyohi = "";
			}
		}else if (isKaigai){
			// 海外旅費で仕訳枝番号がブランクの場合は初期化
			kazeiKbnRyohi = "";
			kashiKamokuCdRyohi = "";
			kashiKamokuEdabanCdRyohi = "";
			kashiFutanBumonCdRyohi="";
			kashiKazeiKbnRyohi = "";
			tekiyouCdRyohi = "";
		}
	}
	
	/**
	 * 仕訳パターン・経費部分のリロード
	 */
	protected void reloadShiwakePatternKeihi()
	{
		// 社員コード取得
		GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
		// 法人カード識別用番号取得
		String houjinCard = (userInfo == null) ? "" : (String)userInfo.get("houjin_card_shikibetsuyou_num");
		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(userIdRyohi);
		
		// 経費入力フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細行数分の領域確保
			int length = shiwakeEdaNo.length;

			kousaihiHyoujiFlg = new String[length];
			ninzuuRiyouFlg = new String[length];
			kariTorihikisakiCd = new String[length];

			kariUf1Cd = new String[length];
			kariUf2Cd = new String[length];
			kariUf3Cd = new String[length];
			kariUf4Cd = new String[length];
			kariUf5Cd = new String[length];
			kariUf6Cd = new String[length];
			kariUf7Cd = new String[length];
			kariUf8Cd = new String[length];
			kariUf9Cd = new String[length];
			kariUf10Cd = new String[length];
			kashiTorihikisakiCd = new String[length];
			kashiFutanBumonCd = new String[length];
			kashiKamokuCd = new String[length];
			kashiKamokuEdabanCd = new String[length];
			kashiKazeiKbn = new String[length];
			kashiShiireKbnKeihi = new String[length];
			tekiyouCd = new String[length];

			kashiUf1Cd1 = new String[length];
			kashiUf2Cd1 = new String[length];
			kashiUf3Cd1 = new String[length];
			kashiUf4Cd1 = new String[length];
			kashiUf5Cd1 = new String[length];
			kashiUf6Cd1 = new String[length];
			kashiUf7Cd1 = new String[length];
			kashiUf8Cd1 = new String[length];
			kashiUf9Cd1 = new String[length];
			kashiUf10Cd1 = new String[length];

			kashiUf1Cd2 = new String[length];
			kashiUf2Cd2 = new String[length];
			kashiUf3Cd2 = new String[length];
			kashiUf4Cd2 = new String[length];
			kashiUf5Cd2 = new String[length];
			kashiUf6Cd2 = new String[length];
			kashiUf7Cd2 = new String[length];
			kashiUf8Cd2 = new String[length];
			kashiUf9Cd2 = new String[length];
			kashiUf10Cd2 = new String[length];

			kashiUf1Cd3 = new String[length];
			kashiUf2Cd3 = new String[length];
			kashiUf3Cd3 = new String[length];
			kashiUf4Cd3 = new String[length];
			kashiUf5Cd3 = new String[length];
			kashiUf6Cd3 = new String[length];
			kashiUf7Cd3 = new String[length];
			kashiUf8Cd3 = new String[length];
			kashiUf9Cd3 = new String[length];
			kashiUf10Cd3 = new String[length];

			kashiUf1Cd4 = new String[length];
			kashiUf2Cd4 = new String[length];
			kashiUf3Cd4 = new String[length];
			kashiUf4Cd4 = new String[length];
			kashiUf5Cd4 = new String[length];
			kashiUf6Cd4 = new String[length];
			kashiUf7Cd4 = new String[length];
			kashiUf8Cd4 = new String[length];
			kashiUf9Cd4 = new String[length];
			kashiUf10Cd4 = new String[length];

			kashiUf1Cd5 = new String[length];
			kashiUf2Cd5 = new String[length];
			kashiUf3Cd5 = new String[length];
			kashiUf4Cd5 = new String[length];
			kashiUf5Cd5 = new String[length];
			kashiUf6Cd5 = new String[length];
			kashiUf7Cd5 = new String[length];
			kashiUf8Cd5 = new String[length];
			kashiUf9Cd5 = new String[length];
			kashiUf10Cd5 = new String[length];

			enableMeisaiBumonSecurity = new boolean[length];

			//--------------------
			//経理明細情報の設定
			//--------------------
			if (!shiwakeEdaNo[0].isEmpty()) {
				for (int i = 0; i < length; i++) {

					// 使用者の法人カード識別用番号取得
					String houjinCardKeiri = (String)userInfo.get("houjin_card_shikibetsuyou_num");

					// 使用者の代表負担部門コード
					String daihyouBumonCd = super.daihyouFutanBumonCd;
					if (dairiFlg.equals("1")) {
						daihyouBumonCd = bumonUsrLogic.findFirstDaihyouFutanBumonCd(userIdRyohi);
					}

					// 取引
					// 海外フラグによって伝票区分を変更する
					ShiwakePatternMaster shiwakeP = shiwakePatternMasterDao.find(isKaigaiKeihiMeisai(i) ? "A901" : DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, Integer.parseInt(shiwakeEdaNo[i]));;

					//交際費
					kousaihiHyoujiFlg[i] = shiwakeP.kousaihiHyoujiFlg;
					ninzuuRiyouFlg[i] = shiwakeP.kousaihiNinzuuRiyouFlg;
					if (! "1".equals(kousaihiHyoujiFlg[i])) {
						kousaihiShousai[i] = "";
						kousaihiNinzuu[i] = "";
						kousaihiHitoriKingaku[i] = "";
					}else if (! "1".equals(ninzuuRiyouFlg[i])) {
						kousaihiNinzuu[i] = "";
						kousaihiHitoriKingaku[i] = "";
					}

					//借方　取引先 ※仕訳チェック用、DB登録には関係ない
					kariTorihikisakiCd[i] = "".equals(shiwakeP.kariTorihikisakiCd) ? "" : torihikisakiCd[i];

					//代表部門指定の場合や特定部門指定の場合でも部門名称が表示されるよう初期化
					Arrays.fill(enableMeisaiBumonSecurity, true);
					//負担部門任意入力or初期代表の場合、起票ユーザで使用できるかチェック
					if( List.of(EteamConst.ShiwakeConst.FUTAN, EteamConst.ShiwakeConst.SYOKIDAIHYOU).contains(shiwakeP.kariFutanBumonCd) ){
						enableMeisaiBumonSecurity[i] = commonLogic.checkFutanBumonWithSecurity(futanBumonCd[i], ks1.futanBumon.getName() + "コード："+ (i+1) + "行目", super.getUser(), denpyouId, getKihyouBumonCd(), errorList);
					}
					//借方　負担部門
					futanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kariFutanBumonCd, daihyouBumonCd);

					//借方　科目
					kamokuCd[i] = shiwakeP.kariKamokuCd;

					//借方　科目枝番
					String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
					switch (pKariKamokuEdabanCd) {
					//何もしない(画面入力のまま)
					case EteamConst.ShiwakeConst.EDABAN:
						break;
						//固定コード値 or ブランク
					default:
						kamokuEdabanCd[i] = pKariKamokuEdabanCd;
						break;
					}

					//借方　UF1-10
					if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.shainCdRenkeiFlg.equals(("1"))){
						int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
						if (ufno == 1)
						{
							 uf1Cd[i] = shainCd;
						}
						if (ufno == 2)
						{
							 uf2Cd[i] = shainCd;
						}
						if (ufno == 3)
						{
							 uf3Cd[i] = shainCd;
						}
						if (ufno == 4)
						{
							 uf4Cd[i] = shainCd;
						}
						if (ufno == 5)
						{
							 uf5Cd[i] = shainCd;
						}
						if (ufno == 6)
						{
							 uf6Cd[i] = shainCd;
						}
						if (ufno == 7)
						{
							 uf7Cd[i] = shainCd;
						}
						if (ufno == 8)
						{
							 uf8Cd[i] = shainCd;
						}
						if (ufno == 9)
						{
							 uf9Cd[i] = shainCd;
						}
						if (ufno == 10)
						{
							uf10Cd[i] = shainCd;
						}
					}
					kariUf1Cd[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kariUf1Cd);
					kariUf2Cd[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kariUf2Cd);
					kariUf3Cd[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kariUf3Cd);
					kariUf4Cd[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kariUf4Cd);
					kariUf5Cd[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kariUf5Cd);
					kariUf6Cd[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kariUf6Cd);
					kariUf7Cd[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kariUf7Cd);
					kariUf8Cd[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kariUf8Cd);
					kariUf9Cd[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kariUf9Cd);
					kariUf10Cd[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kariUf10Cd);

					// 借方　消費税率
					if(!kazeiKbn[i].equals("")) {
						if(kazeiKbnCheck[i]) {
							String[] convZeiritsu = commonLogic.convZeiritsu(zeiritsu[i], keigenZeiritsuKbn[i], shiwakeP.kariZeiritsu, shiwakeP.kariKeigenZeiritsuKbn);
							zeiritsu[i] = convZeiritsu[0];
							keigenZeiritsuKbn[i] = convZeiritsu[1];
						}
					}
					String shoriGroup = "";
					KamokuMaster kmk = kamokuMasterDao.find(shiwakeP.kariKamokuCd);
					if (kmk.shoriGroup != null) {
						shoriGroup = kmk.shoriGroup.toString(); //事業者区分の制御で使用
					}
					//税込・税抜系以外の場合、事業者区分は通常課税固定にする
					if (!((List.of("001", "002", "011", "013").contains(kazeiKbn[i]) && List.of("2","5","6","7","8","10").contains(shoriGroup))
							|| shoriGroup.equals("21"))) {
						jigyoushaKbnKeihi[i] = "0";
					}


					//支払方法により貸方1 or 貸方2を切替する
					switch (shiharaihouhou) {

					case SHIHARAI_HOUHOU.FURIKOMI:
						//振込
						kashiTorihikisakiCd[i] = "".equals(shiwakeP.kashiTorihikisakiCd1) ? "" : torihikisakiCd[i];//貸方1　取引先コード
						kashiFutanBumonCd[i] = shiwakeP.kashiFutanBumonCd1; //貸方1　負担部門コード
						kashiKamokuCd[i] = shiwakeP.kashiKamokuCd1; //貸方1　科目コード
						//貸方1　科目枝番コード
						String pKashiKamokuEdabanCd = shiwakeP.kashiKamokuEdabanCd1;
						this.kashiKamokuEdabanCd[i] = pKashiKamokuEdabanCd.equals(MasterKanriCategoryLogic.ZAIMU)
								? shainShiwakeEdaNo
								: pKashiKamokuEdabanCd;
						kashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn1; //貸方1　課税区分
						kashiUf1Cd1[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd1);
						kashiUf2Cd1[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd1);
						kashiUf3Cd1[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd1);
						kashiUf4Cd1[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd1);
						kashiUf5Cd1[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd1);
						kashiUf6Cd1[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd1);
						kashiUf7Cd1[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd1);
						kashiUf8Cd1[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd1);
						kashiUf9Cd1[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd1);
						kashiUf10Cd1[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd1);
						kashiShiireKbnKeihi[i] = (shiwakeP.kashiShiireKbn1 != null) ? shiwakeP.kashiShiireKbn1 : "" ;
						break;

					case SHIHARAI_HOUHOU.GENKIN:
						//現金
						kashiTorihikisakiCd[i] = "".equals(shiwakeP.kashiTorihikisakiCd2) ? "" : torihikisakiCd[i];//貸方2　取引先コード
						kashiFutanBumonCd[i] = shiwakeP.kashiFutanBumonCd2; //貸方2　負担部門コード
						kashiKamokuCd[i] = shiwakeP.kashiKamokuCd2; //貸方2　科目コード
						kashiKamokuEdabanCd[i] = shiwakeP.kashiKamokuEdabanCd2; //貸方2　科目枝番コード
						kashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn2; //貸方2　課税区分
						kashiUf1Cd2[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd2);
						kashiUf2Cd2[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd2);
						kashiUf3Cd2[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd2);
						kashiUf4Cd2[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd2);
						kashiUf5Cd2[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd2);
						kashiUf6Cd2[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd2);
						kashiUf7Cd2[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd2);
						kashiUf8Cd2[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd2);
						kashiUf9Cd2[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd2);
						kashiUf10Cd2[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd2);
						kashiShiireKbnKeihi[i] = (shiwakeP.kashiShiireKbn2 != null) ? shiwakeP.kashiShiireKbn2 : "" ;
						break;
					}

					kashiUf1Cd3[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd3);
					kashiUf2Cd3[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd3);
					kashiUf3Cd3[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd3);
					kashiUf4Cd3[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd3);
					kashiUf5Cd3[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd3);
					kashiUf6Cd3[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd3);
					kashiUf7Cd3[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd3);
					kashiUf8Cd3[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd3);
					kashiUf9Cd3[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd3);
					kashiUf10Cd3[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd3);

					kashiUf1Cd4[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd4);
					kashiUf2Cd4[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd4);
					kashiUf3Cd4[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd4);
					kashiUf4Cd4[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd4);
					kashiUf5Cd4[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd4);
					kashiUf6Cd4[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd4);
					kashiUf7Cd4[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd4);
					kashiUf8Cd4[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd4);
					kashiUf9Cd4[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd4);
					kashiUf10Cd4[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd4);

					kashiUf1Cd5[i] = commonLogic.convUf(uf1Cd[i], shiwakeP.kashiUf1Cd5);
					kashiUf2Cd5[i] = commonLogic.convUf(uf2Cd[i], shiwakeP.kashiUf2Cd5);
					kashiUf3Cd5[i] = commonLogic.convUf(uf3Cd[i], shiwakeP.kashiUf3Cd5);
					kashiUf4Cd5[i] = commonLogic.convUf(uf4Cd[i], shiwakeP.kashiUf4Cd5);
					kashiUf5Cd5[i] = commonLogic.convUf(uf5Cd[i], shiwakeP.kashiUf5Cd5);
					kashiUf6Cd5[i] = commonLogic.convUf(uf6Cd[i], shiwakeP.kashiUf6Cd5);
					kashiUf7Cd5[i] = commonLogic.convUf(uf7Cd[i], shiwakeP.kashiUf7Cd5);
					kashiUf8Cd5[i] = commonLogic.convUf(uf8Cd[i], shiwakeP.kashiUf8Cd5);
					kashiUf9Cd5[i] = commonLogic.convUf(uf9Cd[i], shiwakeP.kashiUf9Cd5);
					kashiUf10Cd5[i] = commonLogic.convUf(uf10Cd[i], shiwakeP.kashiUf10Cd5);

					//画面に反映
					if (!isEmpty(kariUf1Cd[i]))
					{
						uf1Cd[i] = kariUf1Cd[i];
					}
					if (!isEmpty(kariUf2Cd[i]))
					{
						uf2Cd[i] = kariUf2Cd[i];
					}
					if (!isEmpty(kariUf3Cd[i]))
					{
						uf3Cd[i] = kariUf3Cd[i];
					}
					if (!isEmpty(kariUf4Cd[i]))
					{
						uf4Cd[i] = kariUf4Cd[i];
					}
					if (!isEmpty(kariUf5Cd[i]))
					{
						uf5Cd[i] = kariUf5Cd[i];
					}
					if (!isEmpty(kariUf6Cd[i]))
					{
						uf6Cd[i] = kariUf6Cd[i];
					}
					if (!isEmpty(kariUf7Cd[i]))
					{
						uf7Cd[i] = kariUf7Cd[i];
					}
					if (!isEmpty(kariUf8Cd[i]))
					{
						uf8Cd[i] = kariUf8Cd[i];
					}
					if (!isEmpty(kariUf9Cd[i]))
					{
						uf9Cd[i] = kariUf9Cd[i];
					}
					if (!isEmpty(kariUf10Cd[i]))
					{
						uf10Cd[i] = kariUf10Cd[i];
					}

					//代表部門
					kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], kashiFutanBumonCd[i], daihyouBumonCd);

					//社員コードを摘要コードに反映する場合
					if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
						if(shainCd.length() > 4) {
							tekiyouCd[i] = shainCd.substring(shainCd.length()-4);
						} else {
							tekiyouCd[i] = shainCd;
						}
					//法人カード識別用番号を摘要コードに反映する場合
					} else if("T".equals(setting.houjinCardRenkei())) {
						if(houjinCard.length() > 4) {
							tekiyouCd[i] = houjinCardKeiri.substring(houjinCardKeiri.length()-4);
						} else {
							tekiyouCd[i] = houjinCardKeiri;
						}
					} else {
						tekiyouCd[i] = "";
					}
				}
			}
		}
	}
	
	/**
	 * 仮払明細追加イベント（仮払からの引継ぎ処理専用）。
	 * @return Strutsへ渡すResultName
	 */
	public String addKaribaraiMeisai() {
		super.hissuCheck(2);
		EteamCommon.uploadFileSizeCheck();

		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);

			initialize();
			super.formatCheck();

			//アクセス制御 ＆ データ読み
			accessCheck(Event.INIT);
			loadData();

			//画面の制御
			initParts(connection);
			displaySeigyo(connection);

			//本体の引き継ぎ
			String[] tsuikaDenpyouId = meisaiTsuikaDenpyouId.split(",");
			GMap shinsei = isKaigai ? kaikeiLogic.findKaigaiRyohiKaribarai(tsuikaDenpyouId[0]) : kaikeiLogic.findRyohiKaribarai(tsuikaDenpyouId[0]);
			houmonsaki = (String)shinsei.get("houmonsaki");
			mokuteki = (String)shinsei.get("mokuteki");
			seisankikanFrom = formatDate(shinsei.get("seisankikan_from"));
			seisankikanFromHour = (String)shinsei.get("seisankikan_from_hour");
			seisankikanFromMin = (String)shinsei.get("seisankikan_from_min");
			seisankikanTo = formatDate(shinsei.get("seisankikan_to"));
			seisankikanToHour = (String)shinsei.get("seisankikan_to_hour");
			seisankikanToMin = (String)shinsei.get("seisankikan_to_min");
			if (shinsei.get("sashihiki_num") != null) this.sashihikiNum = ((BigDecimal)shinsei.get("sashihiki_num")).toString();
			if (shinsei.get("sashihiki_num_kaigai") != null) this.setSashihikiNumKaigai(((BigDecimal)shinsei.get("sashihiki_num_kaigai")).toString());
			hosoku = (String)shinsei.get("hosoku");
			String yakushokuCd = commonLogic.getYakushokuCd((String)shinsei.get("user_id"));

			//明細の追加処理
			for (int num = 0; num < tsuikaDenpyouId.length; num++) {
				//旅費明細
				List<GMap> ryohiList = isKaigai ? kaikeiLogic.loadKaigaiRyohiKaribaraiMeisai(tsuikaDenpyouId[num]) : kaikeiLogic.loadRyohiKaribaraiMeisai(tsuikaDenpyouId[num]);
				if (!ryohiList.isEmpty()) {
					//証憑の初期化、税区分、課税フラグをセット
					for (GMap map : ryohiList) {
						map.put("ryoushuusho_seikyuusho_tou_flg", "0");
						map.put("houjin_card_riyou_flg", "0");
						map.put("kaisha_tehai_flg", "0");

						setZeiKbnAndKazeiFlg(map, yakushokuCd);
					}
					// 既存明細の補充。国内旅費精算の添付伝票出の引継ぎの時のみ関係する。
					// このため、海外明細の項目については、欠落が残っている可能性があるので、必要であれば別途明示的に補充すること。
					for (int i = 0; i < shubetsu1.length; i++) {
						if (!shubetsu1[i].isEmpty()) {
							GMap map = new GMap();
							map.put("suuryou_nyuryoku_type", suuryouNyuryokuType[i]);
							map.put("shubetsu_cd", shubetsuCd[i]);
							map.put("shubetsu1", shubetsu1[i]);
							map.put("shubetsu2", shubetsu2[i]);
							map.put("haya_flg", hayaFlg[i]);
							map.put("yasu_flg", yasuFlg[i]);
							map.put("raku_flg", rakuFlg[i]);
							map.put("kikan_from", toDate(kikanFrom[i]));
							map.put("kikan_to", toDate(kikanTo[i]));
							map.put("kyuujitsu_nissuu", kyuujitsuNissuu[i]);
							map.put("shouhyou_shorui_hissu_flg", shouhyouShoruiHissuFlg[i]);
							map.put("koutsuu_shudan", koutsuuShudan[i]);
							map.put("ryoushuusho_seikyuusho_tou_flg", ryoushuushoSeikyuushoTouFlg[i]);
							map.put("naiyou", naiyou[i]);
							map.put("bikou", bikou[i]);
							map.put("oufuku_flg", oufukuFlg[i]);
							map.put("houjin_card_riyou_flg", houjinCardFlgRyohi[i]);
							map.put("kaisha_tehai_flg", kaishaTehaiFlgRyohi[i]);
							map.put("jidounyuuryoku_flg", jidounyuuryokuFlg[i]);
							map.put("nissuu", nissuu[i]);
							map.put("tanka", toDecimal(tanka[i]));
							map.put("suuryou", toDecimal(suuryou[i]));
							map.put("suuryou_kigou", suuryouKigou[i]);
							map.put("meisai_kingaku", toDecimal(meisaiKingaku[i]));
							map.put("ic_card_no", icCardNo[i]);
							map.put("ic_card_sequence_no", icCardSequenceNo[i]);
							map.put("himoduke_card_meisai", himodukeCardMeisaiRyohi[i]);
							map.put("shiharaisaki_name", shiharaisakiNameRyohi[i]);
							map.put("jigyousha_kbn", jigyoushaKbnRyohi[i]);
							map.put("zeinuki_kingaku", zeinukiKingakuRyohi[i]);
							map.put("shouhizeigaku", shouhizeigakuRyohi[i]);
							map.put("zeigaku_fix_flg", "0");
							nittouFlg[i] = commonLogic.isNittou(shubetsu1[i], shubetsu2[i], commonLogic.getYakushokuCd(userIdRyohi)) ? "1" : "";
							map.put("nittouFlg", nittouFlg[i]);
							this.setRyohiMeisaiKaigaiKoumoku(i, map);
							ryohiList.add(i, map);
						}
					}
					meisaiData2Gamen(ryohiList);
				}
				//経費明細
				List<GMap> keihiList = isKaigai ? kaikeiLogic.loadKaigaiRyohiKeihiKaribaraiMeisai(tsuikaDenpyouId[num]) : kaikeiLogic.loadRyohiKeihiKaribaraiMeisai(tsuikaDenpyouId[num]);
				if (!keihiList.isEmpty()) {
					//証憑の初期化
					for (GMap map : keihiList) {
						map.put("shouhyou_shorui_flg", "0");
						map.put("houjin_card_riyou_flg", "0");
						map.put("kaisha_tehai_flg", "0");
					}
					// 既存明細の補充。国内旅費精算の添付伝票出の引継ぎの時のみ関係する。
					// このため、海外明細の項目については、欠落が残っている可能性があるので、必要であれば別途明示的に補充すること。
					for (int i = 0; i < shiwakeEdaNo.length; i++) {
						if (!shiwakeEdaNo[i].isEmpty()) {
							GMap map = new GMap();
							map.put("shiwake_edano", Integer.parseInt(shiwakeEdaNo[i]));
							map.put("shiyoubi", toDate(shiyoubi[i]));
							map.put("shouhyou_shorui_flg", shouhyouShorui[i]);
							map.put("torihiki_name", torihikiName[i]);
							map.put("kari_kamoku_cd", kamokuCd[i]);
							map.put("kari_kamoku_name", kamokuName[i]);
							map.put("kari_kamoku_edaban_cd", kamokuEdabanCd[i]);
							map.put("kari_kamoku_edaban_name", kamokuEdabanName[i]);
							map.put("kari_futan_bumon_cd", futanBumonCd[i]);
							map.put("kari_futan_bumon_name", futanBumonName[i]);
							map.put("torihikisaki_cd", torihikisakiCd[i]);
							map.put("torihikisaki_name_ryakushiki", torihikisakiName[i]);
							map.put("project_cd", projectCd[i]);
							map.put("project_name", projectName[i]);
							map.put("segment_cd", segmentCd[i]);
							map.put("segment_name_ryakushiki", segmentName[i]);
							map.put("uf1_cd", uf1Cd[i]);
							map.put("uf1_name_ryakushiki", uf1Name[i]);
							map.put("uf2_cd", uf2Cd[i]);
							map.put("uf2_name_ryakushiki", uf2Name[i]);
							map.put("uf3_cd", uf3Cd[i]);
							map.put("uf3_name_ryakushiki", uf3Name[i]);
							map.put("uf4_cd", uf4Cd[i]);
							map.put("uf4_name_ryakushiki", uf4Name[i]);
							map.put("uf5_cd", uf5Cd[i]);
							map.put("uf5_name_ryakushiki", uf5Name[i]);
							map.put("uf6_cd", uf6Cd[i]);
							map.put("uf6_name_ryakushiki", uf6Name[i]);
							map.put("uf7_cd", uf7Cd[i]);
							map.put("uf7_name_ryakushiki", uf7Name[i]);
							map.put("uf8_cd", uf8Cd[i]);
							map.put("uf8_name_ryakushiki", uf8Name[i]);
							map.put("uf9_cd", uf9Cd[i]);
							map.put("uf9_name_ryakushiki", uf9Name[i]);
							map.put("uf10_cd", uf10Cd[i]);
							map.put("uf10_name_ryakushiki", uf10Name[i]);
							map.put("kari_kazei_kbn", kazeiKbn[i]);
							map.put("zeiritsu", kazeiKbnCheck[i] ? super.toDecimal(zeiritsu[i]) : BigDecimal.ZERO);
							map.put("keigen_zeiritsu_kbn", super.isEmpty(keigenZeiritsuKbn[i]) ? "0" :this.keigenZeiritsuKbn[i]);
							map.put("shiharai_kingaku", toDecimal(shiharaiKingaku[i]));
							map.put("hontai_kingaku", toDecimal(hontaiKingaku[i]));
							map.put("shouhizeigaku", toDecimal(shouhizeigaku[i]));
							map.put("houjin_card_riyou_flg", houjinCardFlgKeihi[i]);
							map.put("kaisha_tehai_flg", kaishaTehaiFlgKeihi[i]);
							map.put("tekiyou", tekiyou[i]);
							map.put("kousaihi_shousai", kousaihiShousai[i]);
							map.put("kousaihi_shousai_hyouji_flg", kousaihiHyoujiFlg[i]);
							map.put("kousaihi_ninzuu_riyou_flg", ninzuuRiyouFlg[i]);
							map.put("kousaihi_ninzuu", kousaihiNinzuu[i]);
							map.put("kousaihi_hitori_kingaku", kousaihiHitoriKingaku[i]);
							map.put("himoduke_card_meisai", himodukeCardMeisaiKeihi[i]);
							map.put("shiharaisaki_name", shiharaisakiNameKeihi[i]);
							map.put("jigyousha_kbn", jigyoushaKbnKeihi[i]);
							map.put("kari_kazei_kbn", kazeiKbn[i]);
							map.put("bunri_kbn", bunriKbnKeihi[i]);
							map.put("kari_shiire_kbn", kariShiireKbnKeihi[i]);
							map.put("kashi_shiire_kbn", kashiShiireKbnKeihi[i]);
							this.setKeihiMeisaiKaigaiKoumoku(i, map);
							keihiList.add(i, map);
						}
					}
					meisaiData2GamenKeihi(keihiList, true, null);
				}
			}

			//再制御
			displaySeigyo(connection);

			// エラーが有る場合は処理を終了します。
			if (errorList.size() > 0) { return "error"; }

			return "success";
		}
	}
	
	/**
	 * @param value 海外差し引き金額
	 */
	protected void setSashihikiNumKaigai(String value)
	{
	}
	
	/**
	 * @param map 明細マップ
	 * @param yakushokuCd 役職コード
	 */
	protected void setZeiKbnAndKazeiFlg(GMap map, String yakushokuCd)
	{
	}
	
	/**
	 * @param i 配列番号
	 * @param map 旅費明細マップ
	 */
	protected void setRyohiMeisaiKaigaiKoumoku(int i, GMap map)
	{
	}
	
	/**
	 * @param i 配列番号
	 * @param map 経費明細マップ
	 */
	protected void setKeihiMeisaiKaigaiKoumoku(int i, GMap map)
	{
	}
	
	/**
	 * 余分な明細リストを削除する
	 */
	protected void extraMeisaiListDelete() {
		extraMeisaiListKaigaiKoumokuDelete();
		List<String> shubetsuCdMeisaiList = new ArrayList<>();
		List<String> shubetsu1MeisaiList = new ArrayList<>();
		List<String> shubetsu2MeisaiList = new ArrayList<>();
		List<String> hayaFlgMeisaiList = new ArrayList<>();
		List<String> yasuFlgMeisaiList = new ArrayList<>();
		List<String> rakuFlgMeisaiList = new ArrayList<>();
		List<String> koutsuuShudanMeisaiList = new ArrayList<>();
		List<String> kikanFromMeisaiList = new ArrayList<>();
		List<String> kikanToMeisaiList = new ArrayList<>();
		List<String> kyuujitsuNissuuMeisaiList = new ArrayList<>();
		List<String> shouhyouShoruiHissuFlgMeisaiList = new ArrayList<>();
		List<String> ryoushuushoSeikyuushoTouFlgMeisaiList = new ArrayList<>();
		List<String> naiyouMeisaiList = new ArrayList<>();
		List<String> bikouMeisaiList = new ArrayList<>();
		List<String> oufukuFlgMeisaiList = new ArrayList<>();
		List<String> houjinCardFlgMeisaiList = new ArrayList<>();
		List<String> kaishaTehaiFlgMeisaiList = new ArrayList<>();
		List<String> jidounyuuryokuFlgMeisaiList = new ArrayList<>();
		List<String> nissuuMeisaiList = new ArrayList<>();
		List<String> tankaMeisaiList = new ArrayList<>();
		List<String> suuryouNyuryokuTypeMeisaiList = new ArrayList<>();
		List<String> suuryouMeisaiList = new ArrayList<>();
		List<String> suuryouKigouMeisaiList = new ArrayList<>();
		List<String> meisaiKingakuMeisaiList = new ArrayList<>();
		List<String> icCardNoMeisaiList = new ArrayList<>();
		List<String> icCardSequenceNoMeisaiList = new ArrayList<>();
		List<String> himodukeCardMeisaiRyohiList = new ArrayList<>();
		List<String> shiharaisakiNameRyohiList = new ArrayList<>();
		List<String> jigyoushaKbnRyohiList = new ArrayList<>();
		List<String> zeinukiKingakuRyohiList = new ArrayList<>();
		List<String> shouhizeigakuRyohiList = new ArrayList<>();
		List<String> zeigakuFixFlgList = new ArrayList<>();
		List<String> nittouFlgMeisaiList = new ArrayList<>();

		if (shubetsuCd != null) {
			for (int i = 0; i < shubetsuCd.length; i++) {
				if(!shubetsuCd[i].equals(""))
				{
					shubetsuCdMeisaiList.add(shubetsuCd[i]);
					shubetsu1MeisaiList.add(shubetsu1[i]);
					shubetsu2MeisaiList.add(shubetsu2[i]);
					hayaFlgMeisaiList.add(hayaFlg[i]);
					yasuFlgMeisaiList.add(yasuFlg[i]);
					rakuFlgMeisaiList.add(rakuFlg[i]);
					koutsuuShudanMeisaiList.add(koutsuuShudan[i]);
					kikanFromMeisaiList.add(kikanFrom[i]);
					kikanToMeisaiList.add(kikanTo[i]);
					kyuujitsuNissuuMeisaiList.add(kyuujitsuNissuu[i]);
					shouhyouShoruiHissuFlgMeisaiList.add(shouhyouShoruiHissuFlg[i]);
					ryoushuushoSeikyuushoTouFlgMeisaiList.add(ryoushuushoSeikyuushoTouFlg[i]);
					naiyouMeisaiList.add(naiyou[i]);
					bikouMeisaiList.add(bikou[i]);
					oufukuFlgMeisaiList.add(oufukuFlg[i]);
					houjinCardFlgMeisaiList.add(houjinCardFlgRyohi[i]);
					kaishaTehaiFlgMeisaiList.add(kaishaTehaiFlgRyohi[i]);
					jidounyuuryokuFlgMeisaiList.add(jidounyuuryokuFlg[i]);
					nissuuMeisaiList.add(nissuu[i]);
					tankaMeisaiList.add(tanka[i]);
					suuryouNyuryokuTypeMeisaiList.add(suuryouNyuryokuType[i]);
					suuryouMeisaiList.add(suuryou[i]);
					suuryouKigouMeisaiList.add(suuryouKigou[i]);
					meisaiKingakuMeisaiList.add(meisaiKingaku[i]);
					icCardNoMeisaiList.add(icCardNo[i]);
					icCardSequenceNoMeisaiList.add(icCardSequenceNo[i]);
					himodukeCardMeisaiRyohiList.add(himodukeCardMeisaiRyohi[i]);
					shiharaisakiNameRyohiList.add(shiharaisakiNameRyohi[i]);
					jigyoushaKbnRyohiList.add(jigyoushaKbnRyohi[i]);
					zeinukiKingakuRyohiList.add(zeinukiKingakuRyohi[i]);
					shouhizeigakuRyohiList.add(shouhizeigakuRyohi[i]);
					zeigakuFixFlgList.add(zeigakuFixFlg[i]);
					nittouFlgMeisaiList.add(nittouFlg[i]);
				}
			}
		}

		if(shubetsuCdMeisaiList.size()==0) {
			shubetsuCdMeisaiList.add("");
			shubetsu1MeisaiList.add("");
			shubetsu2MeisaiList.add("");
			hayaFlgMeisaiList.add("");
			yasuFlgMeisaiList.add("");
			rakuFlgMeisaiList.add("");
			koutsuuShudanMeisaiList.add("");
			kikanFromMeisaiList.add("");
			kikanToMeisaiList.add("");
			kyuujitsuNissuuMeisaiList.add("");
			shouhyouShoruiHissuFlgMeisaiList.add("");
			ryoushuushoSeikyuushoTouFlgMeisaiList.add("");
			naiyouMeisaiList.add("");
			bikouMeisaiList.add("");
			oufukuFlgMeisaiList.add("");
			houjinCardFlgMeisaiList.add("");
			kaishaTehaiFlgMeisaiList.add("");
			jidounyuuryokuFlgMeisaiList.add("");
			nissuuMeisaiList.add("");
			tankaMeisaiList.add("");
			suuryouNyuryokuTypeMeisaiList.add("");
			suuryouMeisaiList.add("");
			suuryouKigouMeisaiList.add("");
			meisaiKingakuMeisaiList.add("");
			icCardNoMeisaiList.add("");
			icCardSequenceNoMeisaiList.add("");
			himodukeCardMeisaiRyohiList.add("");
			shiharaisakiNameRyohiList.add("");
			jigyoushaKbnRyohiList.add("");
			zeinukiKingakuRyohiList.add("");
			shouhizeigakuRyohiList.add("");
			zeigakuFixFlgList.add("");
			nittouFlgMeisaiList.add("");
		}

		shubetsuCd = shubetsuCdMeisaiList.toArray(new String[shubetsuCdMeisaiList.size()]);
		shubetsu1 = shubetsu1MeisaiList.toArray(new String[shubetsu1MeisaiList.size()]);
		shubetsu2 = shubetsu2MeisaiList.toArray(new String[shubetsu2MeisaiList.size()]);
		hayaFlg = hayaFlgMeisaiList.toArray(new String[hayaFlgMeisaiList.size()]);
		yasuFlg = yasuFlgMeisaiList.toArray(new String[yasuFlgMeisaiList.size()]);
		rakuFlg = rakuFlgMeisaiList.toArray(new String[rakuFlgMeisaiList.size()]);
		koutsuuShudan = koutsuuShudanMeisaiList.toArray(new String[koutsuuShudanMeisaiList.size()]);
		kikanFrom = kikanFromMeisaiList.toArray(new String[kikanFromMeisaiList.size()]);
		kikanTo = kikanToMeisaiList.toArray(new String[kikanToMeisaiList.size()]);
		kyuujitsuNissuu = kyuujitsuNissuuMeisaiList.toArray(new String[kyuujitsuNissuuMeisaiList.size()]);
		shouhyouShoruiHissuFlg = shouhyouShoruiHissuFlgMeisaiList.toArray(new String[shouhyouShoruiHissuFlgMeisaiList.size()]);
		ryoushuushoSeikyuushoTouFlg = ryoushuushoSeikyuushoTouFlgMeisaiList.toArray(new String[ryoushuushoSeikyuushoTouFlgMeisaiList.size()]);
		naiyou = naiyouMeisaiList.toArray(new String[naiyouMeisaiList.size()]);
		bikou = bikouMeisaiList.toArray(new String[bikouMeisaiList.size()]);
		oufukuFlg = oufukuFlgMeisaiList.toArray(new String[oufukuFlgMeisaiList.size()]);
		houjinCardFlgRyohi = houjinCardFlgMeisaiList.toArray(new String[houjinCardFlgMeisaiList.size()]);
		kaishaTehaiFlgRyohi = kaishaTehaiFlgMeisaiList.toArray(new String[kaishaTehaiFlgMeisaiList.size()]);
		jidounyuuryokuFlg = jidounyuuryokuFlgMeisaiList.toArray(new String[jidounyuuryokuFlgMeisaiList.size()]);
		nissuu = nissuuMeisaiList.toArray(new String[nissuuMeisaiList.size()]);
		tanka = tankaMeisaiList.toArray(new String[tankaMeisaiList.size()]);
		suuryouNyuryokuType = suuryouNyuryokuTypeMeisaiList.toArray(new String[suuryouNyuryokuTypeMeisaiList.size()]);
		suuryou = suuryouMeisaiList.toArray(new String[suuryouMeisaiList.size()]);
		suuryouKigou = suuryouKigouMeisaiList.toArray(new String[suuryouKigouMeisaiList.size()]);
		meisaiKingaku = meisaiKingakuMeisaiList.toArray(new String[meisaiKingakuMeisaiList.size()]);
		icCardNo = icCardNoMeisaiList.toArray(new String[icCardNoMeisaiList.size()]);
		icCardSequenceNo = icCardSequenceNoMeisaiList.toArray(new String[icCardSequenceNoMeisaiList.size()]);
		himodukeCardMeisaiRyohi = himodukeCardMeisaiRyohiList.toArray(new String[himodukeCardMeisaiRyohiList.size()]);
		shiharaisakiNameRyohi = shiharaisakiNameRyohiList.toArray(new String[shiharaisakiNameRyohiList.size()]);
		jigyoushaKbnRyohi = jigyoushaKbnRyohiList.toArray(new String[jigyoushaKbnRyohiList.size()]);
		zeinukiKingakuRyohi = zeinukiKingakuRyohiList.toArray(new String[zeinukiKingakuRyohiList.size()]);
		shouhizeigakuRyohi = shouhizeigakuRyohiList.toArray(new String[shouhizeigakuRyohiList.size()]);
		zeigakuFixFlg = zeigakuFixFlgList.toArray(new String[zeigakuFixFlgList.size()]);
		nittouFlg = nittouFlgMeisaiList.toArray(new String[nittouFlgMeisaiList.size()]);
	}
	
	/**
	 * 余分明細の海外項目削除
	 */
	protected void extraMeisaiListKaigaiKoumokuDelete()
	{
	}
	
	/**
	 * @param i 配列番号
	 * @return 海外旅費明細であるか
	 */
	protected boolean isKaigaiRyohiMeisai(int i)
	{
		return false;
	}
	
	/**
	 * @param i 配列番号
	 * @return 海外経費明細であるか
	 */
	protected boolean isKaigaiKeihiMeisai(int i)
	{
		return false;
	}
}
