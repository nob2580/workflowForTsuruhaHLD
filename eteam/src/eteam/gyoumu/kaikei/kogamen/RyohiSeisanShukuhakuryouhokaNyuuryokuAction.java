package eteam.gyoumu.kaikei.kogamen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControl;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 旅費精算(宿泊料他入力）ダイアログAction
 */
@Getter @Setter @ToString
public class RyohiSeisanShukuhakuryouhokaNyuuryokuAction extends WorkflowEventControl {

//＜定数＞

//＜画面入力＞
	//起動パラメータ
	/** インデックス */
	String index;
	/** 最大インデックス */
	String maxIndex;
	/** 仮払あり・なし（金額0許容なら1、それ以外は0またはブランク） */
	String zeroEnabled;
	/** 表示モード(1:追加,2:変更,3:参照) */
	Integer dispMode;
	/** 海外フラグ */
	String kaigaiFlgRyohi;
	/** 使用者ID */
	String userId;
	/** 初期表示：メイン画面からダイアログ起動したタイミングだけtrue、ダイアログ内のリロードではfalse。 */
	boolean init;
	/** 明細追加：新規追加の起動及びリロードで"true"、変更ではnull。 */
	String requestKbn;
	/** 取引課税フラグ */
	String kazeiFlgRyohi;
	/** 科目課税フラグ */
	String kazeiFlgKamoku;
	
	//ダイアログ入力
	/** 種別１ */
	String shubetsu1 = "";
	/** 種別２ */
	String shubetsu2 = "";
	/** 期間開始 */
	String kikanFrom;
	/** 期間終了 */
	String kikanTo;
	/** 休日日数 */
	String kyuujitsuNissuu;
	/** 証憑書類必須フラグ */
	String shouhyouShoruiHissuFlg;
	/** 領収書・請求書等デフォルト値 */
	String ryoushuushoSeikyuushoDefault;
	/** 領収書・請求書等フラグ */
	String ryoushuushoSeikyuushoTouFlg;
	/** 日当・宿泊費フラグ */
	String nittouShukuhakuhiFlg;
	/** 法人カード利用 */
	String houjinCardFlgRyohi;
	/** 会社手配 */
	String kaishaTehaiFlgRyohi;
	/** 内容 */
	String naiyou;
	/** 備考 */
	String bikou;
	/** 幣種 */
	String heishuCdRyohi;
	/** レート */
	String rateRyohi;
	/** 外貨 */
	String gaikaRyohi;
	/** 単位 */
	String taniRyohi;
	/** 単価（表示のみ） */
	String tanka;
	/** 日数 */
	String nissuu;
	/** 人数 */
	String ninzuu;
	//金額はクライアント側だけで持つ
// /** 明細金額 */
// String meisaiKingaku;
	/** 税区分 */
	String zeiKubun;
	/** 法人カード履歴紐付番号(旅費) */
	String himodukeCardMeisaiRyohi;

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

//＜画面入力以外＞
	/** 幣種を固定にする */
	boolean heishuKotei;
	/** 外貨を固定にする */
	boolean gaikaKotei;
	/** 単価を固定にする */
	boolean tankaKotei;
	/** 種別１リストプルダウン値 */
	List<String> shubetsu1List;
	/** 種別２リストプルダウン値 */
	List<Map<String, String>> shubetsu2List;
	/**  法人カード利用表示制御 */
	boolean enableHoujinCard;
	/**  会社手配表示制御 */
	boolean enableKaishaTehai;
	/** 海外旅費であるかどうか */
	boolean isKaigaiRyohi;
	/** 外貨表示制御 */
	boolean enableGaika;
	/** 邦貨換算端数処理方法 */
	String houkaKansanHansuu = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.HOUKA_KANSAN_HASUU);
	/** 海外用日当外貨入力可否*/
	boolean isKaigaiNittouGaikaOn = "1".equals(setting.kaigaiNittouTankaGaikaSettei());
	/** 事業者区分DropDownList */
	List<NaibuCdSetting> jigyoushaKbnList;
	/** 事業者区分ドメイン */
	String[] jigyoushaKbnDomain;
	
	/** 法人カード利用明細の日付変更制御 */
	boolean houjinCardDateEnabled = setting.houjinCardDateEnabled().equals("1");
	/** 画面項目制御クラス */
	GamenKoumokuSeigyo ks; 

//＜部品＞
	/** マスターSELECT */
	MasterKanriCategoryLogic masterSelLg;
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLg;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic bumonUsrLogic;
	/** 内部コード設定Dao */
	NaibuCdSettingAbstractDao naibuCdSettingDao;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkNumber (index, 1, 3, "インデックス"									,false);
		checkNumber (maxIndex, 1, 3, "最大インデックス"								,false);
		checkString (denpyouId, 19, 19, "伝票ID"										,false);
		checkString (denpyouKbn, 4, 4, "伝票区分"										,true);
		checkString (shubetsu1, 1, 20, ks.shubetsu1.getName() ,false);
		checkString (shubetsu2, 1, 20, ks.shubetsu2.getName() ,false);
		checkDate (kikanFrom, ks.kikan.getName() + "開始日"			,false);
		checkDate (kikanTo, ks.kikan.getName() + "終了日"			,false);
		if (ks.kyuujitsuNissuu != null) {
			checkNissuu0 (kyuujitsuNissuu, ks.kyuujitsuNissuu.getName() ,false);
		}
		if (!(denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) || denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI))) {
			checkDomain (ryoushuushoSeikyuushoTouFlg, Domain.FLG, ks.ryoushuushoSeikyuushoTouFlg.getName()  	,false);
		}
		checkString (naiyou, 1, 512, ks.naiyouNittou.getName() ,false);
		checkString (bikou, 1, 40, ks.bikouNittou.getName() ,false);
		if (isKaigaiRyohi && kaigaiFlgRyohi.equals("1")) {
			checkString (heishuCdRyohi,1 ,4 , ks.heishu.getName() ,false);
			checkKingakuDecimalMoreThan0(rateRyohi, ks.rate.getName() ,false);
			checkKingakuDecimalOver1 (gaikaRyohi, ks.gaika.getName() ,false);
		}
		checkKingakuOver1 (tanka, ks.tanka.getName() ,false);
		if (zeroEnabled != null && zeroEnabled.equals("1")) {
			checkNissuu0 (nissuu, ks.nissuu.getName() ,false);
		} else {
			checkNissuu (nissuu, ks.nissuu.getName() ,false);
		}
		if (denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)) {
			checkDomain (houjinCardFlgRyohi, Domain.FLG, ks.houjinCardRiyou.getName() ,false);
			checkDomain (kaishaTehaiFlgRyohi, Domain.FLG, ks.kaishaTehai.getName() ,false);
			checkString(this.shiharaisakiName, 1, 60, ks.shiharaisaki.getName(), false);
			checkDomain(this.jigyoushaKbn, this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName(), false);
			checkKingakuMinus (this.shouhizeigaku, ks.shouhizeigaku.getName(), false);
		}
		//カスタマイズ用の処理だけどパッケージ側に実装
		if (ks.ninzuu != null) {
			checkNumber (ninzuu, 1, 2, ks.ninzuu.getName() ,false);
		}
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = new String[][]{
			//項目						項目名 											必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouKbn, "伝票区分"									, "2","2"},
			{shubetsu1, ks.shubetsu1.getName() ,	"0",ks.shubetsu1.getHissuFlgS()},
			{kikanFrom, ks.kikan.getName() + "開始日"		, "0",ks.kikan.getHissuFlgS()},
			{kikanTo, ks.kikan.getName() + "終了日"		, "0",ks.kikan.getHissuFlgS()},
			{kyuujitsuNissuu, ks.kyuujitsuNissuu.getName() ,	"0",ks.kyuujitsuNissuu.getHissuFlgS()},
			{naiyou, ks.naiyouNittou.getName() ,	"0",ks.naiyouNittou.getHissuFlgS()},
			{bikou, ks.bikouNittou.getName() ,	"0",ks.bikouNittou.getHissuFlgS()},
		};
		hissuCheckCommon(list, eventNum);
		if (denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN))
		{
			// JSP間でやり取りするのは面倒なのでこっちで一度取得する。本当はロジックレイヤーで直接受け渡しできればベストだが…。
			this.invoiceStartFlg = super.myLogic.judgeinvoiceStart();
			list = new String[][]{
				{this.shiharaisakiName, ks.shiharaisaki.getName(), "0", ks.shiharaisaki.getInvoiceHissuFlgS(invoiceDenpyou, invoiceStartFlg)},
				{this.jigyoushaKbn, ks.jigyoushaKbn.getName(), "0", ks.jigyoushaKbn.getInvoiceHissuFlgS(invoiceDenpyou, invoiceStartFlg)},
				{this.shouhizeigaku, ks.shouhizeigaku.getName(), "0", ks.shouhizeigaku.getHissuFlgS()},
			};
			hissuCheckCommon(list, eventNum);
		}
		if (isKaigaiRyohi && kaigaiFlgRyohi.equals("1")) {
			list = new String[][]{
				//項目			項目名 								必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{heishuCdRyohi, ks.heishu.getName() ,"0",ks.heishu.getHissuFlgS()},
				{rateRyohi, ks.rate.getName() ,"0",ks.rate.getHissuFlgS()},
				{gaikaRyohi, ks.gaika.getName() ,"0",ks.gaika.getHissuFlgS()},
			};
			hissuCheckCommon(list, eventNum);
		}
		
		list = new String[][]{
			//項目				項目名 								必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{tanka, ks.tanka.getName() ,"0",ks.tanka.getHissuFlgS()},
			{nissuu, ks.nissuu.getName() ,"0",ks.nissuu.getHissuFlgS()},
		};
		hissuCheckCommon(list, eventNum);
		
		//カスタマイズ用の処理だけどパッケージ側に実装
		if (ks.ninzuu != null) {
			list = new String[][]{
				//項目			項目名 								必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{ninzuu, ks.ninzuu.getName() ,"0",ks.ninzuu.getHissuFlgS()},
			};
			hissuCheckCommon(list, eventNum);
		}
	}
	
	/**
	 * 業務関連の関連チェック処理
	 */
	protected void soukanCheck() {
		if(! kikanTo.isEmpty()) {
			// 期間開始日と期間終了日の整合性チェック
			for(Map<String, String> errMap: EteamCommon.kigenCheck(kikanFrom, kikanTo)) {
				// 開始日と終了日の整合性チェックのみエラーとする。
				if("2".equals(errMap.get("errorCode"))){
					errorList.add(ks.kikan.getName() + errMap.get("errorMassage"));
				}
			}
		}
		
		// 領収書・請求書等チェックが必須（会社設定）かつ証憑書類必須フラグが1、かつ旅費仮払以外の場合
		if(EteamCommon.funcControlJudgment(sysLogic, KINOU_SEIGYO_CD.RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU) 
			&& shouhyouShoruiHissuFlg.equals("1")
			&& !(denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) || denpyouKbn.equals(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI))){
			// 領収書・請求書等が未チェックであればエラー
			if(ryoushuushoSeikyuushoTouFlg.equals("0")){
				errorList.add(ks.ryoushuushoSeikyuushoTouFlg.getName() + "をチェックしてください。");
			}
		}
	}
	
//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		masterSelLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		super.myLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		isKaigaiRyohi = denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);

		ks = new GamenKoumokuSeigyo(denpyouKbn);
		
		//サーバーからの送信値が半角スペース→&nbsp;になっていてマスターと不一致になる為
		if (isNotEmpty(shubetsu1)) shubetsu1 = shubetsu1.replace(' ', ' ');
		if (isNotEmpty(shubetsu2)) shubetsu2 = shubetsu2.replace(' ', ' ');
		
		boolean isAll = isEmpty(himodukeCardMeisaiRyohi)?  true : false ;
		//プルダウンの取得
		if (kaigaiFlgRyohi.equals("1")) {
			//種別１取得
			shubetsu1List = trimDup(masterSelLg.loadKaigaiNittouShubetsu1(isAll));
			if (shubetsu1List.isEmpty()) {
				errorList.add("選択対象がありません");
				return;
			}
			//初期状態は先頭、参照ならスクリプト側で当時のマスターが消えていても渡されたshubetsu1を強引に追加して表示する
			if (dispMode != 3)
				if (isEmpty(shubetsu1) || ! shubetsu1List.contains(shubetsu1)) shubetsu1 = shubetsu1List.get(0);
			
			//種別１に紐付く種別２のリストを取得
			shubetsu2List = trimMapDup(masterSelLg.loadKaigaiNittouShubetsu2(shubetsu1, isAll));
			//初期状態は先頭、参照ならスクリプト側で当時のマスターが消えていても渡されたshubetsu2を強引に追加して表示する
			if (dispMode != 3)
				if (isEmpty(shubetsu2) || notContains(shubetsu2List, shubetsu2)) shubetsu2 = shubetsu2List.get(0).get("shubetsu2");
			
			
		} else {
			//種別１取得
			shubetsu1List = trimDup(masterSelLg.loadNittouShubetsu1(isAll));
			if (shubetsu1List.isEmpty()) {
				errorList.add("選択対象がありません");
				return;
			}
			//初期状態は先頭、参照ならスクリプト側で当時のマスターが消えていても渡されたshubetsu1を強引に追加して表示する
			if (dispMode != 3)
				if (isEmpty(shubetsu1) || ! shubetsu1List.contains(shubetsu1)) shubetsu1 = shubetsu1List.get(0);
			
			//種別１に紐付く種別２のリストを取得
			shubetsu2List = trimMapDup(masterSelLg.loadNittouShubetsu2(shubetsu1, isAll));
			//初期状態は先頭、参照ならスクリプト側で当時のマスターが消えていても渡されたshubetsu2を強引に追加して表示する
			if (dispMode != 3)
				if (isEmpty(shubetsu2) || notContains(shubetsu2List, shubetsu2)) shubetsu2 = shubetsu2List.get(0).get("shubetsu2");
		}
		
		// 法人カードの表示可否
		GMap userInfo = bumonUsrLogic.selectUserInfo(userId);
		if (userInfo == null) {
			enableHoujinCard = false;
		} else {
			enableHoujinCard = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD) && 
							   userInfo.get("houjin_card_riyou_flag").equals("1") &&
							   (denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN));
		}
		
		jigyoushaKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn");
		jigyoushaKbnDomain = jigyoushaKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		
		// 会社手配の表示可否
		enableKaishaTehai = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI) &&
						    (denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN));
		
		//種別１、２、役職に一致する日当・宿泊費フラグを取得
		//※日当単価と証憑書類必須フラグはマスターからトランザクションレコードにコピーしているが、日当・宿泊費フラグはマスターにしかもっていないので取得方法が異なる。もしマスターが消えたらnittouShukuhakuhiFlg=nullで進む。
		GMap nittou = loadNittouKingaku();
		if (nittou != null) {
			nittouShukuhakuhiFlg = (String)nittou.get("nittou_shukuhakuhi_flg");
		}
		
		// 外貨等の表示可否
		if (isKaigaiRyohi && kaigaiFlgRyohi.equals("1")) {
			//①会社設定のオプション機能による判定
			enableGaika = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
			
			//②日当宿泊費フラグによる判定（外貨入力機能がONの場合のみ）
			if(enableGaika) enableGaika = isKaigaiNittouGaikaHyouji();
		}
		
		//日当単価と証憑書類必須フラグをマスターから取得し直す。ただし参照時はマスターを参照せずにトランザクションデータを参照。
		//userIdが空の時、単価等が指定できなくなるのでJSP側でアラート制御
		if (dispMode != 3 && !userId.isEmpty()) {
			//種別１、２、役職に一致する日当等マスターを取得
// GMap nittou = loadNittouKingaku();
			if (nittou != null) {
				
				//マスターの単価→固定値登録なら単価に表示し変更不可。
				String tmpTanka = formatMoney(nittou.get("tanka"));
				if(isNotEmpty(tmpTanka)) {
					tanka = tmpTanka;
					tankaKotei = true; //単価の入力可否
					heishuKotei = tankaKotei; //幣種とレートの入力可否。単価固定と同期
					gaikaKotei = tankaKotei; //外貨の入力可否。単価固定と同期
					if(isEmpty(gaikaRyohi)) {
						heishuCdRyohi = "";
						rateRyohi = "";
						taniRyohi = "";
					}
				}
				
				//外貨入力ONなら基本的に単価固定
				if(enableGaika) {
					tankaKotei = true;
					//幣種固定と外貨固定はデフォルトfalseなので設定不要
					
					//日当の場合は例外的な挙動なので制御を上書き
					//あと外貨関連項目の値のマスター登録値も。
					if(isNittou(nittou.get("nittou_shukuhakuhi_flg")) && isKaigaiNittouGaikaOn) {
						boolean existHeishuCd = isNotEmpty(nittou.get("heishu_cd"));
						heishuKotei = true;
						gaikaKotei =(existHeishuCd && (null==nittou.get("tanka_gaika")))? false : true;
						if (!existHeishuCd && isEmpty(tmpTanka))
						{
							tankaKotei = false;
						} //幣種コードの登録があれば外貨入力を優先
						
						heishuCdRyohi = nittou.get("heishu_cd"); //国内ならNULL
						String tmpGaika = formatMoneyDecimal(nittou.get("tanka_gaika")); //国内ならブランク
						if (isNotEmpty(tmpGaika))
						{
							gaikaRyohi = tmpGaika;
						}
						GMap rateMasterMap = masterSelLg.findHeishuCd(heishuCdRyohi);
						if(null!=rateMasterMap) {
							rateRyohi = formatMoneyDecimalNoComma(rateMasterMap.get("rate"));
							taniRyohi = rateMasterMap.get("currency_unit");
						}
						//幣種コードの登録があれば単価（邦貨）より優先する
						if(isNotEmpty(heishuCdRyohi)) {
							tanka = formatMoney(commonLg.calSashihikiTankaKaigai(toDecimal(gaikaRyohi), toDecimal(rateRyohi)));
						}
					}
				}
				
				
				//種別１、２、役職に一致する証憑書類必須フラグを取得
				shouhyouShoruiHissuFlg = (String)nittou.get("shouhyou_shorui_hissu_flg");
				//種別１、２、役職に一致する税区分を取得
				zeiKubun = (String)nittou.get("zei_kubun");
			}
		}
		
		displaySeigyoExt(connection);
	}
	
	/**
	 * 日当等マスター情報取得
	 * @return 日当Map
	 */
	protected GMap loadNittouKingaku() {
		//海外と国内で日当テーブルが異なるた呼び出しメソッド分岐
		if (kaigaiFlgRyohi.equals("1")) return masterSelLg.loadKaigaiNittouKingaku(shubetsu1, shubetsu2, commonLg.getKihyouYakushokuCd(userId, denpyouId));
		return masterSelLg.loadNittouKingakuAndFlgAndKbn(shubetsu1, shubetsu2, commonLg.getKihyouYakushokuCd(userId, denpyouId));
	}
	
	/**
	 * 外貨表示するかどうかの判定処理
	 * @return 判定結果
	 */
	protected boolean isKaigaiNittouGaikaHyouji() {
		if(isNittou(nittouShukuhakuhiFlg)) {
			if(!isKaigaiNittouGaikaOn)  return false; // 外貨使用しないなら非表示
		}
		return true;
	}
	
	/**
	 * 種別が日当かどうの判定処理
	 * @param _nittouShukuhakuhiFlg 日当宿泊費フラグ
	 * @return 判定結果
	 */
	protected boolean isNittou(String _nittouShukuhakuhiFlg) {
		if ("2".equals(_nittouShukuhakuhiFlg))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * リストから重複を除く
	 * @param param 重複ありリスト
	 * @return 重複なしリスト
	 */
	protected List<String> trimDup(List<String> param) {
		List<String> ret = new ArrayList<>();
		for (int i = 0; i < param.size() ; i++) {
			if(! ret.contains(param.get(i))) {
				ret.add(param.get(i));
			}
		}
		return ret;
	}
	
	/**
	 * リストから重複を除く
	 * @param param 重複ありリスト
	 * @return 重複なしリスト
	 */
	protected List<Map<String, String>> trimMapDup(List<Map<String, String>> param){
		List<Map<String, String>> ret = new ArrayList<>();
		for (int i = 0; i < param.size() ; i++) {
			if(! ret.contains(param.get(i))) {
				ret.add(param.get(i));
			}
		}
		return ret;
	}
	
	/**
	 * リスト内のMapに要素が含まれるか判定する
	 * @param param リスト
	 * @param str 要素
	 * @return 判定結果（要素が含まれない場合はtrue）
	 */
	protected boolean notContains(List<Map<String, String>> param, String str){
		boolean ret = true;
		for(Map<String, String> map : param) {
			if(map.containsValue(str)) {
				ret = false; // 処理終了
			}
		}
		return ret;
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {
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
	 * カスタマイズ用のdisplaySeigyo
	 * @param connection コネクション
	 */
	protected void displaySeigyoExt(EteamConnection connection){
		
	}
	
	/**
	 * 証憑書類フラグのデフォルト制御
	 */
	protected void setShouhyouShoruiDefault() {
		if (requestKbn != null) {
			// 既に明細が作成済の場合（変更・複写）RyohiKoutsuuhiMeisaiList.jspで"notNew"を指定。それ以外の場合（新規・クリア・過去明細）のみデフォルト値を設定。
			return;
		}

		switch(denpyouKbn){
		case DENPYOU_KBN.RYOHI_SEISAN:
			ryoushuushoSeikyuushoTouFlg = setting.shouhyouShoruiDefaultA004();
			break;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			ryoushuushoSeikyuushoTouFlg = setting.shouhyouShoruiDefaultA011();
			break;
		default:
			break;
		}
	}
}
