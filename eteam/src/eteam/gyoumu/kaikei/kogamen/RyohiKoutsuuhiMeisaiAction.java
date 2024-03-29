package eteam.gyoumu.kaikei.kogamen;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamEkispertCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.KoutsuuShudanMasterAbstractDao;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.KoutsuuShudanMasterDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 旅費交通費明細追加ダイアログAction
 */
@Getter @Setter @ToString
public class RyohiKoutsuuhiMeisaiAction extends EteamEkispertCommon {

	//＜定数＞

	//＜画面入力＞
	//起動パラメータ
// String denpyouId; ←親にいる
// String denpyouKbn;←親にいる
	/** インデックス */
	String index;
	/** 最大インデックス */
	String maxIndex;
	/** 仮払あり・なし（金額0許容なら1、それ以外は0またはブランク） */
	String zeroEnabled;
	/** 表示モード(1:追加, 2:変更, 3:参照) */
	Integer dispMode;
	/** 使用者ID */
	String userId;
	/** 初期表示：メイン画面からダイアログ起動したタイミングだけtrue、ダイアログ内のリロードではfalse。 */
	boolean init;
	/** 海外フラグ */
	String kaigaiFlgRyohi;
	/** 取引課税フラグ */
	String kazeiFlgRyohi;
	/** 科目課税フラグ */
	String kazeiFlgKamoku;
	
	/** 種別1 */
	String shubetsu1;
	/** 種別2 */
	String shubetsu2;
	/** 自動入力フラグ */
	String jidounyuuryokuFlg;
	/** 期間開始 */
	String kikanFrom;
	/** 交通手段 */
	String koutsuuShudan;
	/** 証憑書類必須フラグ */
	String shouhyouShoruiHissuFlg;
	/** 領収書・請求書等デフォルト値 */
	String ryoushuushoSeikyuushoDefault;
	/** 領収書・請求書等フラグ */
	String ryoushuushoSeikyuushoTouFlg;
	/** 内容(交通費) */
	String naiyou;
	/** 備考(交通費) */
	String bikou;
	/** 幣種 */
	String heishuCdRyohi;
	/** レート */
	String rateRyohi;
	/** 外貨 */
	String gaikaRyohi;
	/** 単価（表示のみ） */
	String tanka;
	/** 数量入力タイプ */
	String suuryouNyuryokuType;
	/** 数量 */
	String suuryou;
	/** 数量記号 */
	String suuryouKigou;
	/** 往復フラグ */
	String oufukuFlg;
	/** 法人カード利用 */
	String houjinCardFlgRyohi;
	/** 会社手配 */
	String kaishaTehaiFlgRyohi;
	/** 税区分 */
	String zeiKubun;
	/** ICカード番号 */
	String icCardNo;
	/** ICカードシーケンス番号 */
	String icCardSequenceNo;
	/** 法人カード履歴紐付番号(旅費) */
	String himodukeCardMeisaiRyohi;
	
	//202210 早安楽
	/** 早フラグ */
	String hayaFlg;
	/** 安フラグ */
	String yasuFlg;
	/** 楽フラグ */
	String rakuFlg;

	/** 支払先名 */
	String shiharaisakiName;
	/** 事業者区分 */
	String jigyoushaKbn;
	/** 税抜金額 */
	String zeinukiKingaku;
	/** 消費税額 */
	String shouhizeigaku;
	/** 税額修正フラグ */
	String zeigakuFixFlg;
	
	//金額はクライアント側だけで持つ
// /** 明細金額 */
// String meisaiKingaku;

//＜画面入力以外＞
	/** 交通手段リスト */
	List<GMap> koutsuuShudanList;
	/** 交通手段ドメイン */
	String[] koutsuuShudanDomain;
	/** 数量入力タイプドメイン */
	String[] suuryouNyuryokuTypeDomain;
	/** 事業者区分DropDownList */
	List<NaibuCdSetting> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;

//画面制御情報
	/** 駅すぱあと連携可否 */
	boolean ekispertEnable = !"9".equals(setting.intraFlg());

	/**  画面項目制御クラス */
	GamenKoumokuSeigyo ks;
			
	/**  法人カード利用表示制御 */
	boolean enableHoujinCard;
	/**  会社手配表示制御 */
	boolean enableKaishaTehai;
	/** 旅費仮払でないかどうか */
	boolean isNotRyohikaribarai;
	/** 海外旅費であるかどうか */
	boolean isKaigaiRyohi;
	/** 単価を固定にする */
	boolean tankaKotei;
	/** 外貨表示制御 */
	boolean enableGaika;
	/** 邦貨換算端数処理方法 */
	String houkaKansanHansuu = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.HOUKA_KANSAN_HASUU);
	
	/** ICカード明細の日付変更制御 */
	boolean icCardDateEnabled = setting.icCardDateEnabled().equals("1");
	/** 法人カード利用明細の日付変更制御 */
	boolean houjinCardDateEnabled = setting.houjinCardDateEnabled().equals("1");
	/** 定期区間の反映先 */
	String teikikukanHaneisaki = setting.ekispertTeikikukanHaneisaki();
	
	
//＜部品＞
	/** マスターSELECT */
	MasterKanriCategoryLogic masterLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLg;
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** 交通手段マスターDAO */
	KoutsuuShudanMasterAbstractDao koutsuuShudanMasterDao;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;

	//＜入力チェック＞
		@Override
		protected void formatCheck() {
			checkNumber (index, 1, 3, "インデックス", false);
			checkNumber (maxIndex, 1, 3, "最大インデックス", false);
			checkString (denpyouId, 19, 19, "伝票ID", false);
			checkString (denpyouKbn, 4, 4, "伝票区分", true);
			
			checkString (shubetsu1, 1, 20, ks.shubetsu1.getName(), false);
			checkString (shubetsu2, 1, 20, ks.shubetsu2.getName(), false);
			checkDomain (jidounyuuryokuFlg, Domain.FLG, "自動入力フラグ", false);
			checkDomain (hayaFlg, Domain.FLG, ks.hayaYasuRaku.getName() + "(早フラグ)" , false);
			checkDomain (yasuFlg, Domain.FLG, ks.hayaYasuRaku.getName() + "(安フラグ)" , false);
			checkDomain (rakuFlg, Domain.FLG, ks.hayaYasuRaku.getName() + "(楽フラグ)" , false);
			checkDate (kikanFrom, ks.kikan.getName() + "開始日", false);
			if (! init)
			checkDomain (koutsuuShudan, koutsuuShudanDomain, ks.koutsuuShudan.getName(), false);
			checkDomain (shouhyouShoruiHissuFlg, Domain.FLG, "証憑書類必須フラグ", false);
			if (isNotRyohikaribarai) 
				checkDomain (ryoushuushoSeikyuushoTouFlg, Domain.FLG, ks.ryoushuushoSeikyuushoTouFlg.getName(), false);
			checkString (naiyou, 1, 512,  ks.naiyouKoutsuuhi.getName(), false);
			
			if (isKaigaiRyohi && kaigaiFlgRyohi.equals("1")) {
				checkString (heishuCdRyohi, 1, 4, ks.heishu.getName(), false);
				checkKingakuDecimalMoreThan0(rateRyohi, ks.rate.getName(), false);
				checkKingakuDecimalOver1 (gaikaRyohi, ks.gaika.getName(), false);
			}
			
			//単価は交通費明細の0円入力に対応 仮払有無に関わらず0円を許可
			if(kaigaiFlgRyohi.equals("0")) {
				if(StringUtils.isNotEmpty(suuryouNyuryokuType) && !suuryouNyuryokuType.equals("0")) {
					checkKingaku3thDecimalPlaceOver0(tanka, ks.tanka.getName(), false);
					checkDomain (suuryouNyuryokuType, suuryouNyuryokuTypeDomain, "数量入力タイプ", false);
					checkKingakuDecimal (suuryou, "0.01", "999999999999.99", "数量", false);
					checkString (suuryouKigou, 1, 5, "数量記号", false);
				}else{
					checkKingakuOver0 (tanka, ks.tanka.getName(), false);
					checkDomain (suuryouNyuryokuType, suuryouNyuryokuTypeDomain, "数量入力タイプ", false);
				}
			}else{
				checkKingakuOver0 (tanka, ks.tanka.getName(), false);
			}
			checkDomain (oufukuFlg, Domain.FLG, ks.oufukuFlg.getName(), false);
			if (isNotRyohikaribarai) {
				checkDomain (houjinCardFlgRyohi, Domain.FLG, ks.houjinCardRiyou.getName(), false);
				checkDomain (kaishaTehaiFlgRyohi, Domain.FLG, ks.kaishaTehai.getName(), false);
				checkString(this.shiharaisakiName, 1, 60, ks.shiharaisaki.getName(), false);
				checkDomain(this.jigyoushaKbn, this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
				checkKingakuMinus (this.shouhizeigaku, ks.shouhizeigaku.getName(), false);
			}
		}

		@Override
		protected void hissuCheck(int eventNum) {
			String[][] list = new String[][]{
				//項目						項目名  必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{denpyouKbn, "伝票区分", "2", "2"},
				{shubetsu1, ks.shubetsu1.getName(), "0",ks.shubetsu1.getHissuFlgS()},
				{shubetsu2, ks.shubetsu2.getName(), "0",ks.shubetsu2.getHissuFlgS()},
				{jidounyuuryokuFlg, "自動入力フラグ", "0","1"},
				{kikanFrom, ks.kikan.getName() + "開始日", "0",ks.kikan.getHissuFlgS()},
				{naiyou, ks.naiyouKoutsuuhi.getName(), "0",ks.naiyouKoutsuuhi.getHissuFlgS()},
				{tanka, ks.tanka.getName(), "0",ks.tanka.getHissuFlgS()},
				{koutsuuShudan, ks.koutsuuShudan.getName(), "0",ks.koutsuuShudan.getHissuFlgS()},
				{oufukuFlg, ks.oufukuFlg.getName(), "0",ks.oufukuFlg.getHissuFlgS()},
				{bikou, ks.bikouKoutsuuhi.getName(), "0",ks.bikouKoutsuuhi.getHissuFlgS()},
			};
			hissuCheckCommon(list, eventNum);
			
			if (isNotRyohikaribarai) {
				if(StringUtils.isNotEmpty(suuryouNyuryokuType) && suuryouNyuryokuType.equals("0")) {
					// JSP間でやり取りするのは面倒なのでこっちで一度取得する。本当はロジックレイヤーで直接受け渡しできればベストだが…。
					this.invoiceStartFlg = super.myLogic.judgeinvoiceStart();
					list = new String[][]{
						{ryoushuushoSeikyuushoTouFlg,ks.ryoushuushoSeikyuushoTouFlg.getName(),"0",ks.ryoushuushoSeikyuushoTouFlg.getHissuFlgS()},
						{houjinCardFlgRyohi, ks.houjinCardRiyou.getName(), "0",ks.houjinCardRiyou.getHissuFlgS()},
						{kaishaTehaiFlgRyohi, ks.kaishaTehai.getName(), "0",ks.kaishaTehai.getHissuFlgS()},
						{this.shiharaisakiName, ks.shiharaisaki.getName(), "0", ks.shiharaisaki.getInvoiceHissuFlgS(invoiceDenpyou, invoiceStartFlg)},
						{this.jigyoushaKbn, ks.jigyoushaKbn.getName(), "0", ks.jigyoushaKbn.getInvoiceHissuFlgS(invoiceDenpyou, invoiceStartFlg)},
						{this.shouhizeigaku, ks.shouhizeigaku.getName(), "0", ks.shouhizeigaku.getHissuFlgS()},
					};
				}else {
					list = new String[][]{
						{ryoushuushoSeikyuushoTouFlg,ks.ryoushuushoSeikyuushoTouFlg.getName(),"0",ks.ryoushuushoSeikyuushoTouFlg.getHissuFlgS()},
					};
				}
				hissuCheckCommon(list, eventNum);
			}
			
			if (isKaigaiRyohi && kaigaiFlgRyohi.equals("1")) {
				list = new String[][]{
					{heishuCdRyohi, ks.heishu.getName(),"0",ks.heishu.getHissuFlgS()},
					{rateRyohi, ks.rate.getName(),"0",ks.rate.getHissuFlgS()},
					{gaikaRyohi, ks.gaika.getName(),"0",ks.gaika.getHissuFlgS()},
				};
				hissuCheckCommon(list, eventNum);
			}else {
				//国内旅費交通費明細の場合
				list = new String[][]{
					{suuryouNyuryokuType, "数量入力タイプ", "0","1"},
					
				};
				hissuCheckCommon(list, eventNum);
				
				if(StringUtils.isNotEmpty(suuryouNyuryokuType) && !suuryouNyuryokuType.equals("0")) {
					list = new String[][]{
						{suuryou, "数量", "0","1"},
						{suuryouKigou, "数量記号", "0","1"},
					};
					hissuCheckCommon(list, eventNum);
				};
			}
		}
		
		/**
		 * 業務関連の関連チェック処理
		 */
		protected void soukanCheck() {
			// 領収書・請求書等チェックが必須（会社設定）かつ証憑書類必須フラグが1、かつ旅費仮払申請でない時場合
			if(EteamCommon.funcControlJudgment(sysLogic, KINOU_SEIGYO_CD.RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU) 
				&& shouhyouShoruiHissuFlg.equals("1") 
				&& isNotRyohikaribarai){
				// 領収書・請求書等が未チェックであればエラー
				if(ryoushuushoSeikyuushoTouFlg.equals("0")){
					errorList.add(ks.ryoushuushoSeikyuushoTouFlg.getName() + "をチェックしてください。");
				}
			}
			
			// 往復が必須の場合
			if(ks.oufukuFlg.getHissuFlgS().equals("1")){
				// 往復が未チェックであればエラー
				if(oufukuFlg.equals("0")){
					errorList.add(ks.oufukuFlg.getName() + "をチェックしてください。");
				}
			}
		}
			
//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		koutsuuShudanMasterDao = EteamContainer.getComponent(KoutsuuShudanMasterDao.class, connection);
		naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		super.myLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		//サーバーからの送信値が半角スペース→&nbsp;になっていてマスターと不一致になる為
		if (isNotEmpty(koutsuuShudan)) koutsuuShudan = koutsuuShudan.replace(' ', ' ');

		isNotRyohikaribarai = !(denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI));
		isKaigaiRyohi = denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
		
		//プルダウンのリストを取得
		koutsuuShudanList = (isKaigaiRyohi && kaigaiFlgRyohi.equals("1"))? masterLogic.loadKaigaiKoutsuuShudan() : masterLogic.loadKoutsuuShudan();
		koutsuuShudanDomain =  EteamCommon.mapList2Arr(koutsuuShudanList, "koutsuu_shudan");
		jigyoushaKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn");
		jigyoushaKbnDomain = jigyoushaKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		if(!(isKaigaiRyohi && kaigaiFlgRyohi.equals("1"))) {
			suuryouNyuryokuTypeDomain = EteamCommon.mapList2Arr(koutsuuShudanList, "suuryou_nyuryoku_type");
		}

		// 項目制御クラス生成
		ks = new GamenKoumokuSeigyo(denpyouKbn);

		//交通手段マスターが削除済であってもちゃんと表示されるように
		if (StringUtils.isNotEmpty(koutsuuShudan) && init) {
			boolean foundFlg = false;
			for (String k : koutsuuShudanDomain) if (k.equals(koutsuuShudan)) foundFlg = true;
			if (! foundFlg) {
				GMap m = new GMap();
				m.put("koutsuu_shudan", koutsuuShudan);
				m.put("shouhyou_shorui_hissu_flg", shouhyouShoruiHissuFlg);
				m.put("zei_kubun", zeiKubun);
				m.put("suuryou_nyuryoku_type", suuryouNyuryokuType);
				m.put("tanka", tanka);
				m.put("suuryou_kigou", suuryouKigou);
				koutsuuShudanList.add(m);
			}
		}
		
		// 駅すぱあと連携可否判定
		if(ekispertEnable) {
			// 駅すぱあと呼出 start
			super.ekispertInit(connection, EteamEkispertCommon.SEARCH_MODE_UNCHIN);
		}
		
		// 起票ユーザーID
		if (denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN)) {
			userId = getUser().getSeigyoUserId();
			if (StringUtils.isNotEmpty(denpyouId)) {
				GMap kihyouUser = commonLg.findKihyouUser(denpyouId);
				if(kihyouUser != null && kihyouUser.get("user_id") != null){
					userId = kihyouUser.get("user_id").toString();
				}
			}
		}
		
		// 法人カードの表示可否
		GMap usrInfo = bumonUsrLogic.selectUserInfo(userId);
		if (usrInfo == null) {
			enableHoujinCard = false;
		} else {
			enableHoujinCard = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD) && 
								usrInfo.get("houjin_card_riyou_flag").equals("1") &&
								(denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN));
		}
		
		// 会社手配の表示可否
		enableKaishaTehai = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI)&&
							(denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KOUTSUUHI_SEISAN));

		// 外貨等の表示可否
		if (isKaigaiRyohi && kaigaiFlgRyohi.equals("1")) {
			enableGaika = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
		}
		
		// 海外旅費の場合、外貨を入力するなら単価は入力不可にする
		// 国内旅費交通費明細でも数量入力タイプが「1」の場合は単価を入力不可にする。	
		if ((isKaigaiRyohi && kaigaiFlgRyohi.equals("1") && enableGaika)
			|| (!isKaigaiRyohi && !kaigaiFlgRyohi.equals("1") && StringUtils.isNotEmpty(suuryouNyuryokuType) && suuryouNyuryokuType.equals("1"))) {
			tankaKotei = true;
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			displaySeigyo(connection);
			setShouhyouShoruiDefault();

			formatCheck();
			hissuCheck(1);
		}

		return "success";
	}

	/**
	 * ダイアログ確定
	 * @return 結果
	 */
	public String confirm() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			displaySeigyo(connection);

			formatCheck();
			hissuCheck(2);
			soukanCheck();
			if (!errorList.isEmpty()) {
				return "error";
			}
		}

		return "success";
	}
	
	/**
	 * 証憑書類フラグのデフォルト制御
	 */
	protected void setShouhyouShoruiDefault(){
		switch(denpyouKbn){
		case DENPYOU_KBN.RYOHI_SEISAN:
			ryoushuushoSeikyuushoDefault = setting.shouhyouShoruiDefaultA004();
			break;
		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			ryoushuushoSeikyuushoDefault = setting.shouhyouShoruiDefaultA010();
			break;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			ryoushuushoSeikyuushoDefault = setting.shouhyouShoruiDefaultA011();
			break;
		default:
			break;
		}
	}
}
