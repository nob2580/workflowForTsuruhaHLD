package eteam.gyoumu.menu;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.GAMEN_KENGEN_SEIGYO_KBN;
import eteam.common.EteamNaibuCodeSetting.KIDOKU_FLG;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.RegAccess;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.InformationKanriCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.houjincard.HoujinCardLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * トップアクション
 */
@Getter @Setter @ToString
public class MenuAction extends EteamAbstractAction {

//＜定数＞
//＜画面入力＞

//＜画面入力以外＞
	/**業務ロール機能制御データ */
	List<GMap> gyoumuKinouSeigyoList;
	/** インフォメーションデータ */
	List<GMap> infoList;
	/** 起票中データ件数 */
	long kihyoucyuCount;
	/** 申請中データ件数 */
	long shinseichuuCount;
	/** 承認待ちデータ件数 */
	long shouninmachiCount;
	/** 通知一覧の未読データ件数 */
	long tsuuchiIchiranCount;
	/** 法人カード精算紐付の未精算データ件数 */
	long houjinCardMiseisanCount;
	/** セッションユーザー情報 */
	User userInfo;
	/** (制御)ユーザーID */
	String seigyoUserId;
	/** (本人)ユーザーID */
	String loginUserId;
	/** 業務ロールID */
	String gyoumuRoleId;
	/** 業務ロール所属部門コード**/
	String[] gyoumuRoleShozokuBumonCd;
	/** 部門所属ユーザフラグ */
	boolean bumonShozokuUserFlag = false;
	/** お気に入りデータ */
	List<GMap> okiniiriList;

//新規起票
	/** 新規起票一覧表示フラグ */
	boolean shinkiIchiranHyoujiFlg  = false;
	/** ガイダンス起票表示フラグ */
	boolean guidanceKihyouHyoujiFlg = false;
	/** 請求書払いCSVアップロード表示フラグ */
	boolean seikyuushoBaraiCSVUploadHyoujiFlg = false;
	/** 支払依頼申請CSVアップロード表示フラグ */
	boolean shiharaiIraiCSVUploadHyoujiFlg = false;
	/** 経費立替精算　代理起票 */
	boolean keihiTatekaeSeisanDairikihyouFlg = false;
	/** 旅費精算　代理起票 */
	boolean ryohiSeisanDairikihyouFlg = false;
	/** 旅費仮払申請　代理起票 */
	boolean ryohiKaribaraiSeisanDairikihyouFlg = false;
	/** 海外旅費精算　代理起票 */
	boolean kaigaiRyohiSeisanDairikihyouFlg = false;
	/** 海外旅費仮払申請　代理起票 */
	boolean kaigaiRyohiKaribaraiSeisanDairikihyouFlg = false;
	
//伝票一覧
	/** 起票中一覧表示フラグ */
	boolean kihyoucyuuIchiranHyoujiFlg   = false;
	/** 申請中一覧表示フラグ */
	boolean shinseichuuIchiranHyoujiFlg   = false;
	/** 承認待ち一覧表示フラグ */
	boolean shouninmachiIchiranHyoujiFlg = false;
	/** 伝票検索表示フラグ */
	boolean denpyouKensakuHyoujiFlg      = false;
	/** 支給金額一覧表示フラグ */
	boolean shikyuuKingakuIchiranHyoujiFlg = false;
	/** 経費明細表表示フラグ */
	boolean keihiMeisaihyoHyoujiFlg = false;
	/** 執行状況一覧表示フラグ */
	boolean shikkouJoukyouHyoujiFlg = false;
	/** 法人カード利用明細表示フラグ */
	boolean houjinCardRiyouHyoujiFlg = false;
	/** 法人カードデータインポート表示フラグ */
	boolean houjinCardCSVUploadHyoujiFlg = false;
	/** 支払依頼申請出力 */
	boolean shiharaiIraishoShutsuryokuFlg = false;

//個人設定
	/** 代行者登録表示フラグ */
	boolean daikousyaTourokuHyoujiFlg = false;
	/** メール通知設定表示フラグ */
	boolean mailTuuchiHyoujiFlg       = false;
	/** お気に入りメンテナンス表示フラグ */
	boolean okiniiriMntHyoujiFlg      = false;
	/** ユーザー情報変更表示フラグ */
	boolean userJouhouHenkouHyoujiFlg = false;

//代行
	/** 被代行者選択表示フラグ */
	boolean hidaikousyaSentakuHyoujiFlg = false;

//通知
	/** 通知一覧表示フラグ */
	boolean tsuuchiIchiranHyoujiFlg = false;
	/** 法人カード精算紐付表示フラグ */
	boolean HoujinCardSeisanHimodukeHyoujiFlg = false;

//お気に入り起票
	/** お気に入り起票フラグ */
	boolean okiniiriKihyouFlg = false;

//会社設定
	/** 部門管理表示フラグ */
	boolean bumonKanriHyoujiFlg = false;
	/** 役割管理表示フラグ */
	boolean yakuwariKanriHyoujiFlg = false;
	/** 業務ロール管理表示フラグ */
	boolean gyoumuRoleKanriHyoujiFlg = false;
	/** ユーザー管理表示フラグ */
	boolean userKanriHyoujiFlg = false;
	/** 代行者指定（管理）表示フラグ */
	boolean daikousyaTourokuKanriHyoujiFlg = false;
	/** 部門推奨ルート登録表示フラグ */
	boolean bumonShuisyouRouteHyoujiFlg = false;
	/** 合議部署登録表示フラグ */
	boolean gougiBushoHyoujiFlg = false;
	/** 承認処理権限表示フラグ */
	boolean shouninShoriHyoujiFlg = false;
	/** 最終承認者登録表示フラグ */
	boolean saisyuuShouninsyaHyoujiFlg = false;
	/** マスターデータ管理表示フラグ */
	boolean masterDataHyoujiFlg = false;
	/** 伝票管理表示フラグ */
	boolean denpyouKanriHyoujiFlg = false;
	/** ガイダンス起票メンテナンス表示フラグ */
	boolean guidanceKihyouMntHyoujiFlg = false;
	/** インフォメーション登録表示フラグ */
	boolean infoTourokuHyoujiFlg = false;
	/** 取引登録表示フラグ */
	boolean torihikiTourokuHyoujiFlg = false;
	/** 部門別取引登録表示フラグ */
	boolean bumonTorihikiTourokuHyoujiFlg = false;
	/** ユーザー定義届書作成表示フラグ */
	boolean kanniTodokeSakuseiHyoujiFlg = false;
	/** 定期情報一覧表示フラグ */
	boolean teikiJouhouIchiranHyoujiFlg = false;
	/** 画面項目管理表示フラグ */
	boolean gamenKoumokuKanriHyoujiFlg = false;
	/** 伝票一覧全ユーザー共通表示項目設定表示フラグ */
	boolean denpyouKoumokuKyoutsuSetteiHyoujiFlg = false;
	/** システム管理表示フラグ */
	boolean systemKanriHyoujiFlg = false;
	/** 起案番号管理表示フラグ */
	boolean kianbangouKanriHyoujiFlg = false;
	/** 部門一括登録表示フラグ */
	boolean bumonIkkatsuTourokuHyoujiFlg  = false;
	/** ユーザー一括登録表示フラグ */
	boolean userIkkatsuTourokuHyoujiFlg = false;
	/** 部門推奨ルート一括登録表示フラグ */
	boolean bumonSuishouRouteIkkatsuTourokuHyoujiFlg = false;
	/** 取引（仕訳）一括登録表示フラグ */
	boolean torihikiShiwakeIkkatsuTourokuHyoujiFlg = false;
	/** 部門別取引（仕訳）一括登録表示フラグ */
	boolean bumonbetsuTorihikiShiwakeIkkatsuTourokuHyoujiFlg = false;
	/** インボイス制度開始設定表示フラグ */
	boolean invoiceSeidoKaishiSetteiHyoujiFlg = false;
	
//経理処理
	/** CSV（振込）ダウンロード表示フラグ */
	boolean csvDownloadHyoujiFlg = false;
	/** 日中データ連携表示フラグ */
	boolean nittyuuDataRenkeiHyoujiFlg = false;
	/** FBデータ再作成表示フラグ */
	boolean FBDataSaiSakuseiHyoujiFlg = false;
	/** 請求書払い申請締表示フラグ */
	boolean SeikyuushoShimeHyoujiFlg = false;
	/** 支払依頼申請締表示フラグ */
	boolean ShiharaiShimeHyoujiFlg = false;
	/** 自動引落締表示フラグ*/
	boolean JidouhikiotoshiShimeHyoujiFlg = false;
	/** 自動引落締表示フラグ*/
	boolean KeihitatekaeShimeHyoujiFlg = false;
	/** 出張旅費精算締表示フラグ*/
	boolean RyohiShimeHyoujiFlg = false;
	/** 海外出張旅費精算締表示フラグ*/
	boolean KaigaiRyohiShimeHyoujiFlg = false;
	/** 交通費精算締表示フラグ*/
	boolean KoutsuuhiShimeHyoujiFlg = false;
	/** 経費明細　データ更新表示フラグ */
	boolean keihiMeisaiDataKoushinHyoujiFlg = false;
	/** 支払依頼データ作成 */
	boolean shiharaiIraiOutput = false;
	/** 支払予定総括表 */
	boolean shiharaiYoteiSoukatsuhyoOutput = false;
	/** 予算執行処理年月設定 */
	boolean yosanShikkouShoriNengetsuSettei = false;
	
//運用監視
	/** データアクセス表示フラグ */
	boolean dataAccessHyoujiFlg = false;
	/** バックアップ運用表示フラグ */
	boolean backupUnyouHyoujiFlg = false;
	/** バッチ連携結果表示フラグ */
	boolean batchRenkeiHyoujiFlg = false;

//ツール
	/** ICカード連携ツール ダウンロード */
	boolean smartSeisanDownloadFlg = false;
	
// 財務拠点入力
	/** 財務拠点のみ表示フラグ */
	boolean zaimuKyotenNyuryokuOnlyFlg = false;

//＜部品＞
	/** 拡張用 判定結果格納 */
	protected GMap judgementExt;
	/** システムカテゴリロジック */
	protected SystemKanriCategoryLogic syslc;
	/** 起票ナビカテゴリロジック */
	protected KihyouNaviCategoryLogic kihyouLogic;
	/** 部門ユーザー管理カテゴリロジック */
	protected BumonUserKanriCategoryLogic bumonUserLogic;
	/** メニュー関連ロジック */
	protected MenuLogic myLogic;
	/** 法人カード使用履歴ロジック */
	protected HoujinCardLogic hcLogic;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){

		//1.入力チェック
		//2.データ存在チェック
		//3.アクセス可能チェック
		
		try(EteamConnection connection = EteamConnection.connect()) {
			
			InformationKanriCategoryLogic infolc = EteamContainer.getComponent(InformationKanriCategoryLogic.class, connection);
			TsuuchiCategoryLogic tsuuchilc = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
			syslc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			KihyouNaviCategoryLogic  kihyoulc = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
			bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			myLogic = EteamContainer.getComponent(MenuLogic.class, connection);
			hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
			
			//4.処理
			
			/* セッション情報よりユーザー情報を取得 */
			userInfo = getUser();
			seigyoUserId = userInfo.getSeigyoUserId();
			loginUserId  = userInfo.getLoginUserId(); 
			gyoumuRoleId = userInfo.getGyoumuRoleId();
			gyoumuRoleShozokuBumonCd = userInfo.getGyoumuRoleShozokuBumonCd();
			List<Integer> securityPatternList = bumonUserLogic.selectSecurityPatternList(seigyoUserId,null);
			zaimuKyotenNyuryokuOnlyFlg = bumonUserLogic.selectValidUserInfo(loginUserId).get("zaimu_kyoten_nyuryoku_only_flg").equals(GAMEN_KENGEN_SEIGYO_KBN.YUUKOU);
			
			
			//経費精算機能(WF本体)のフラグをそれぞれレジストリから取得
			boolean keihiSeisanKinouOn = RegAccess.checkEnableKeihiSeisan();
			
			/* メニューごとの表示フラグ設定 */
			// 代行者登録 
			setDaikouHyoujiFlg(connection);
			// 新規起票
			shinkiIchiranHyoujiFlg            = setHyoujiFlg("ShinkiKihyou") && keihiSeisanKinouOn;
			guidanceKihyouHyoujiFlg           = setHyoujiFlg("GuidanceKihyou") && keihiSeisanKinouOn;
			seikyuushoBaraiCSVUploadHyoujiFlg = setHyoujiFlg("SeikyuushoBaraiCSVUpload") && keihiSeisanKinouOn;
			GMap denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.SEIKYUUSHO_BARAI);
			if (null == denpyouShubetsuMap)
			{
				seikyuushoBaraiCSVUploadHyoujiFlg = false;
			}
			shiharaiIraiCSVUploadHyoujiFlg = setHyoujiFlg("ShiharaiIraiCSVUpload") && keihiSeisanKinouOn;
			denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.SIHARAIIRAI);
			if (null == denpyouShubetsuMap)
			{
				shiharaiIraiCSVUploadHyoujiFlg = false;
			}
			keihiTatekaeSeisanDairikihyouFlg  = setDairikihyouFlg(connection) && keihiSeisanKinouOn;
			denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);
			if (null == denpyouShubetsuMap)
			{
				keihiTatekaeSeisanDairikihyouFlg = false;
			}
			ryohiSeisanDairikihyouFlg  = setDairikihyouFlg(connection) && keihiSeisanKinouOn;
			denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.RYOHI_SEISAN);
			if (null == denpyouShubetsuMap)
			{
				ryohiSeisanDairikihyouFlg = false;
			} 
			ryohiKaribaraiSeisanDairikihyouFlg  = setDairikihyouFlg(connection) && keihiSeisanKinouOn;
			denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI);
			if (null == denpyouShubetsuMap)
			{
				ryohiKaribaraiSeisanDairikihyouFlg = false;
			} 
			kaigaiRyohiSeisanDairikihyouFlg  = setDairikihyouFlg(connection) && keihiSeisanKinouOn;
			denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN);
			if (null == denpyouShubetsuMap)
			{
				kaigaiRyohiSeisanDairikihyouFlg = false;
			} 
			kaigaiRyohiKaribaraiSeisanDairikihyouFlg  = setDairikihyouFlg(connection) && keihiSeisanKinouOn;
			denpyouShubetsuMap = kihyouLogic.findValidDenpyouKanri(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
			if (null == denpyouShubetsuMap)
			{
				kaigaiRyohiKaribaraiSeisanDairikihyouFlg = false;
			} 
			
			// 伝票一覧
			kihyoucyuuIchiranHyoujiFlg   = setHyoujiFlg("KihyoucyuuDenpyouIchiran") && keihiSeisanKinouOn;
			shinseichuuIchiranHyoujiFlg  = setHyoujiFlg("ShinseichuuDenpyouIchiran") && keihiSeisanKinouOn;
			shouninmachiIchiranHyoujiFlg = setHyoujiFlg("ShouninmachiDenpyouIchiran") && keihiSeisanKinouOn;
			denpyouKensakuHyoujiFlg      = setHyoujiFlg("DenpyouIchiran") && keihiSeisanKinouOn;
			shikyuuKingakuIchiranHyoujiFlg = setHyoujiFlg("ShikyuukingakuIchiran") && keihiSeisanKinouOn;
			keihiMeisaihyoHyoujiFlg        = setHyoujiFlg("KeihiMeisaihyo") && keihiSeisanKinouOn;
			if(!(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId()))) {
				if (securityPatternList == null || securityPatternList.isEmpty())
				{
					keihiMeisaihyoHyoujiFlg = false;
				}
			}
			shikkouJoukyouHyoujiFlg      = setHyoujiFlg("ShikkouJoukyouIchiran") && keihiSeisanKinouOn;
			if (! RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption()))
			{
				shikkouJoukyouHyoujiFlg = false;
			}
			String sqpt = "";
			try{
				sqpt = setting.yosanSecurityPattern();
			}catch(RuntimeException e){}
			if (sqpt.equals(""))
			{
				shikkouJoukyouHyoujiFlg = false;
			}
			houjinCardRiyouHyoujiFlg     = setHyoujiFlg("HoujinCardRiyouMeisai") && keihiSeisanKinouOn;
			shiharaiIraishoShutsuryokuFlg= setHyoujiFlg("ShiharaiIraishoShutsuryoku") && keihiSeisanKinouOn;
			
			// 個人設定
			mailTuuchiHyoujiFlg       = setHyoujiFlg("MailTsuuchiSettei");
			okiniiriMntHyoujiFlg      = setHyoujiFlg("OkiniiriHensyu") && keihiSeisanKinouOn;
			userJouhouHenkouHyoujiFlg = setHyoujiFlg("UserJouhou");
			
			// 代行
			hidaikousyaSentakuHyoujiFlg = setHyoujiFlg("HiDaikoushaSentaku") && keihiSeisanKinouOn;
			
			// 通知
			tsuuchiIchiranHyoujiFlg = setHyoujiFlg("TsuuchiIchiran") && keihiSeisanKinouOn;
			HoujinCardSeisanHimodukeHyoujiFlg = setHyoujiFlg("HoujinCardSeisanHimoduke") && keihiSeisanKinouOn;
			if (! syslc.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD))
			{
				HoujinCardSeisanHimodukeHyoujiFlg = false;
			}
			boolean keiriFlg = false;
			String keiriChkStr = EteamCommon.getAccessAuthorityLevel(connection, getUser().getGyoumuRoleId(), "KR");
			if (keiriChkStr.equals("SU") || keiriChkStr.equals("KR")) { keiriFlg = true; };
			GMap houjinCardUserInfo  = bumonUserLogic.selectUserInfo(getUser().getSeigyoUserId());
			if (houjinCardUserInfo == null || (!("1".equals(houjinCardUserInfo.get("houjin_card_riyou_flag"))) && !keiriFlg) )
			{
				HoujinCardSeisanHimodukeHyoujiFlg = false;
			}
			
			
			//お気に入り起票
			okiniiriKihyouFlg = setHyoujiFlg("Bookmark") && keihiSeisanKinouOn;
			if(!(syslc.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.OKINIIRIKIHYOU)) ){
				okiniiriKihyouFlg = false;
			}
			
			// 会社設定
			bumonKanriHyoujiFlg                              = setHyoujiFlg("BumonIchiran");
			yakuwariKanriHyoujiFlg                           = setHyoujiFlg("BumonRoleIchiran");
			gyoumuRoleKanriHyoujiFlg                         = setHyoujiFlg("GyoumuRoleIchiran");
			userKanriHyoujiFlg                               = setHyoujiFlg("UserKensaku");
			bumonShuisyouRouteHyoujiFlg                      = setHyoujiFlg("BumonSuishouRouteIchiran") && keihiSeisanKinouOn;
			gougiBushoHyoujiFlg                              = setHyoujiFlg("GougiBushoIchiran") && keihiSeisanKinouOn;
			shouninShoriHyoujiFlg                            = setHyoujiFlg("ShoriKengenTouroku") && keihiSeisanKinouOn;
			saisyuuShouninsyaHyoujiFlg                       = setHyoujiFlg("ChuukiMongonSetteiIchiran") && keihiSeisanKinouOn;
			masterDataHyoujiFlg                              = setHyoujiFlg("MasterDataIchiran");
			denpyouKanriHyoujiFlg                            = setHyoujiFlg("DenpyouKanri") && keihiSeisanKinouOn;
			guidanceKihyouMntHyoujiFlg                       = setHyoujiFlg("GuidanceMaintenance") && keihiSeisanKinouOn;
			infoTourokuHyoujiFlg                             = setHyoujiFlg("InformationIchiran");
			torihikiTourokuHyoujiFlg                         = setHyoujiFlg("TorihikiIchiran") && keihiSeisanKinouOn;
			bumonTorihikiTourokuHyoujiFlg                    = setHyoujiFlg("BumonbetsuTorihikiIchiran") && keihiSeisanKinouOn;
			batchRenkeiHyoujiFlg                             = setHyoujiFlg("BatchRenkeiKekkaKakunin");
			kanniTodokeSakuseiHyoujiFlg                      = setHyoujiFlg("KaniTodokeIchiran") && keihiSeisanKinouOn;
			teikiJouhouIchiranHyoujiFlg                      = setHyoujiFlg("TeikiJouhouIchiran") && keihiSeisanKinouOn;
			systemKanriHyoujiFlg                             = setHyoujiFlg("SystemKanri");
			denpyouKoumokuKyoutsuSetteiHyoujiFlg             = setHyoujiFlg("DenpyouKoumokuKyoutsuSettei") && keihiSeisanKinouOn;
			bumonIkkatsuTourokuHyoujiFlg                     = setHyoujiFlg("IkkatsuTourokuBumon");
			userIkkatsuTourokuHyoujiFlg                      = setHyoujiFlg("IkkatsuTourokuUser");
			bumonSuishouRouteIkkatsuTourokuHyoujiFlg         = setHyoujiFlg("IkkatsuTourokuBumonSuishouRoute") && keihiSeisanKinouOn;
			torihikiShiwakeIkkatsuTourokuHyoujiFlg           = setHyoujiFlg("IkkatsuTourokuTorihiki") && keihiSeisanKinouOn;
			bumonbetsuTorihikiShiwakeIkkatsuTourokuHyoujiFlg = setHyoujiFlg("IkkatsuTourokuBumonbetsuTorihiki") && keihiSeisanKinouOn;
			invoiceSeidoKaishiSetteiHyoujiFlg  = setHyoujiFlg("InvoiceSeidoKaishiSettei"); //画面権限制御テーブルを参照
			
			
			if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())){
				kianbangouKanriHyoujiFlg  = setHyoujiFlg("KianbangouBoIchiran") && keihiSeisanKinouOn;
			}else{
				kianbangouKanriHyoujiFlg  = false;
			}
			
			if (! (setting.intraFlg().equals("0") || setting.intraFlg().equals("1")) ){
				teikiJouhouIchiranHyoujiFlg  = false;
			}

			// 経理処理
			csvDownloadHyoujiFlg          = setHyoujiFlg("CSVDownload") && keihiSeisanKinouOn;
			nittyuuDataRenkeiHyoujiFlg    = setHyoujiFlg("NittyuuDataRenkei");
			FBDataSaiSakuseiHyoujiFlg     = setHyoujiFlg("FBDataSaiSakusei") && keihiSeisanKinouOn;
			SeikyuushoShimeHyoujiFlg      = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.seikyuuKeijouSeigen().equals("1"))
			{
				SeikyuushoShimeHyoujiFlg = false;
			}
			ShiharaiShimeHyoujiFlg      = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.shiharaiiraiKeijouSeigen().equals("1"))
			{
				ShiharaiShimeHyoujiFlg = false;
			}
			JidouhikiotoshiShimeHyoujiFlg = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.jidouhikiKeijouSeigen().equals("1"))
			{
				JidouhikiotoshiShimeHyoujiFlg = false;
			}
			KeihitatekaeShimeHyoujiFlg    = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.keihiseisanKeijouSeigen().equals("1"))
			{
				KeihitatekaeShimeHyoujiFlg = false;
			}
			RyohiShimeHyoujiFlg           = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.ryohiseisanKeijouSeigen().equals("1"))
			{
				RyohiShimeHyoujiFlg = false;
			}
			KaigaiRyohiShimeHyoujiFlg     = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.kaigairyohiseisanKeijouSeigen().equals("1"))
			{
				KaigaiRyohiShimeHyoujiFlg = false;
			}
			KoutsuuhiShimeHyoujiFlg       = setHyoujiFlg("Shime") && keihiSeisanKinouOn;
			if (! setting.koutsuuhiseisanKeijouSeigen().equals("1"))
			{
				KoutsuuhiShimeHyoujiFlg = false;
			}
			keihiMeisaiDataKoushinHyoujiFlg  = setHyoujiFlg("KeihiMeisaiDataKoushin") && keihiSeisanKinouOn;
			houjinCardCSVUploadHyoujiFlg = setHyoujiFlg("HoujinCardCSVUpload") && keihiSeisanKinouOn;
			if (! syslc.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD))
			{
				houjinCardCSVUploadHyoujiFlg = false;
			}
			shiharaiIraiOutput = setHyoujiFlg("ShiharaiIraiOutput") && keihiSeisanKinouOn;
			shiharaiYoteiSoukatsuhyoOutput = setHyoujiFlg("ShiharaiYoteiSoukatsuhyoOutput") && keihiSeisanKinouOn;
			if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())){
				yosanShikkouShoriNengetsuSettei = setHyoujiFlg("YosanShikkouShoriNengetsuSettei") && keihiSeisanKinouOn;
				if (! setting.yosanCheckKikan().equals(yosanCheckKikan.TO_TAISHOUZUKI))
				{
					yosanShikkouShoriNengetsuSettei = false;
				}
			}else {
				kianbangouKanriHyoujiFlg  = false;
			}

			//運用監視
			dataAccessHyoujiFlg = setHyoujiFlg("DataAccessIchiran");
			backupUnyouHyoujiFlg = setHyoujiFlg("BackupUnyou");
			gamenKoumokuKanriHyoujiFlg = setHyoujiFlg("GamenKoumokuKanri") && keihiSeisanKinouOn;
			
			//ツール
			smartSeisanDownloadFlg = setHyoujiFlg("SmartSeisan") && keihiSeisanKinouOn;
			
			/* インフォメーションを取得 */
			infoList = infolc.loadInfoKikannaiData();
			for (GMap infomap : infoList) {
				// 通知内容の改行コードをタグに置換して、3行まで表示。各行が長い場合は、桁数制限（70文字）。
				String tsuuchinaiyou = infomap.get("tsuuchi_naiyou").toString().replaceAll("\r\n", "\n");
				String[] tsuuchisplit = tsuuchinaiyou.split("\n");
				String wkTsuuchiNaiyou = "";
				int line = 0;
				for (int i=0; i < tsuuchisplit.length && line < 3; i++){
					if(tsuuchisplit[i].length() != 0){
						wkTsuuchiNaiyou = wkTsuuchiNaiyou + tsuuchisplit[i] + "\n";
						line++;
					}
				}
				tsuuchinaiyou = wkTsuuchiNaiyou.substring(0,wkTsuuchiNaiyou.length() - 1);
				// 通知内容を設定
				infomap.put("infoNaiyou", tsuuchinaiyou);
				infomap.put("informationId", infomap.get("info_id"));
			}
			
			//部門所属ユーザかチェックする。
			if(gyoumuRoleId == null){
				bumonShozokuUserFlag = true;
			}
			
			/* 伝票一覧の起票中、承認待ち伝票件数を設定 */
			// 起票中
			kihyoucyuCount = tsuuchilc.findDenpyouIchiranKensakuCount(userInfo, EteamNaibuCodeSetting.KANREN_DENPYOU.KIHYOU_CHUU);
			// 申請中
			shinseichuuCount = tsuuchilc.findDenpyouIchiranKensakuCount(userInfo, EteamNaibuCodeSetting.KANREN_DENPYOU.SHINSEI_CHUU);
			// 承認待ち
			shouninmachiCount  = tsuuchilc.findDenpyouIchiranKensakuCount(userInfo, EteamNaibuCodeSetting.KANREN_DENPYOU.SHOUNIN_MACHI);
			
			/* 通知一覧の未読件数を設定 */
			tsuuchiIchiranCount = tsuuchilc.findTsuuchiCount(seigyoUserId, KIDOKU_FLG.MIDOKU);
			/* 法人カード精算紐付の未精算件数を設定 */
			houjinCardMiseisanCount = hcLogic.findCardRirekiCount("", keiriFlg ? "" : seigyoUserId, "", null, null, "0", true, keiriFlg);
			
			/* お気に入りを取得	*/
			okiniiriList = kihyoulc.loadOkiniiriKihyou(userInfo.getTourokuOrKoushinUserId());
			
			for (GMap map : okiniiriList) {
				
				map.put("denpyouShubetsu", map.get("denpyou_shubetsu"));
				map.put("denpyouKbn",map.get("denpyou_kbn"));
				map.put("version", map.get("version"));
				map.put("denpyouShubetsuUrl", map.get("denpyou_shubetsu_url"));
				map.put("gyoumuShubetsu", map.get("gyoumu_shubetsu"));
				map.put("memo", map.get("memo"));
				
			}
			
			// 拡張メニューの表示判定
			judgementExt = myLogic.judgeExt(userInfo, this);
			
			//一部財務拠点のみユーザなら上記判定を無効化
			if(zaimuKyotenNyuryokuOnlyFlg) {
				// 新規起票
				shinkiIchiranHyoujiFlg = false;
				guidanceKihyouHyoujiFlg = false;
				seikyuushoBaraiCSVUploadHyoujiFlg = false;
				shiharaiIraiCSVUploadHyoujiFlg = false;
				keihiTatekaeSeisanDairikihyouFlg = false;
				ryohiSeisanDairikihyouFlg  = false;
				ryohiKaribaraiSeisanDairikihyouFlg = false;
				kaigaiRyohiSeisanDairikihyouFlg  = false;
				kaigaiRyohiKaribaraiSeisanDairikihyouFlg = false;
				
				// 伝票一覧
				kihyoucyuuIchiranHyoujiFlg = false;
				shinseichuuIchiranHyoujiFlg = false;
				shouninmachiIchiranHyoujiFlg = false;
				denpyouKensakuHyoujiFlg = false;
				shikyuuKingakuIchiranHyoujiFlg = false;
				keihiMeisaihyoHyoujiFlg = false;
				shikkouJoukyouHyoujiFlg = false;
				shiharaiIraishoShutsuryokuFlg = false;
				
				// 代行、通知、お気に入り起票、個人設定
				daikousyaTourokuHyoujiFlg = false;
				okiniiriMntHyoujiFlg = false;
				hidaikousyaSentakuHyoujiFlg = false;
				tsuuchiIchiranHyoujiFlg = false;
				HoujinCardSeisanHimodukeHyoujiFlg = false;
				okiniiriKihyouFlg = false;
				
				// 会社設定
				bumonShuisyouRouteHyoujiFlg                      = false;
				gougiBushoHyoujiFlg                              = false;
				shouninShoriHyoujiFlg                            = false;
				saisyuuShouninsyaHyoujiFlg                       = false;
				denpyouKanriHyoujiFlg                            = false;
				guidanceKihyouMntHyoujiFlg                       = false;
				torihikiTourokuHyoujiFlg                         = false;
				bumonTorihikiTourokuHyoujiFlg                    = false;
				kanniTodokeSakuseiHyoujiFlg                      = false;
				teikiJouhouIchiranHyoujiFlg                      = false;
				denpyouKoumokuKyoutsuSetteiHyoujiFlg             = false;
				bumonSuishouRouteIkkatsuTourokuHyoujiFlg         = false;
				torihikiShiwakeIkkatsuTourokuHyoujiFlg           = false;
				bumonbetsuTorihikiShiwakeIkkatsuTourokuHyoujiFlg = false;
				
				// 経理処理
				csvDownloadHyoujiFlg          = false;
				FBDataSaiSakuseiHyoujiFlg     = false;
				SeikyuushoShimeHyoujiFlg      = false;
				ShiharaiShimeHyoujiFlg      = false;
				JidouhikiotoshiShimeHyoujiFlg = false;
				KeihitatekaeShimeHyoujiFlg    = false;
				RyohiShimeHyoujiFlg           = false;
				KaigaiRyohiShimeHyoujiFlg     = false;
				KoutsuuhiShimeHyoujiFlg       = false;
				keihiMeisaiDataKoushinHyoujiFlg  = false;
				houjinCardRiyouHyoujiFlg = false;
				houjinCardCSVUploadHyoujiFlg = false;
				shiharaiIraiOutput = false;
				shiharaiYoteiSoukatsuhyoOutput = false;
				
				// ツール
				smartSeisanDownloadFlg = false;
			}
			
			//5.戻り値を返す。
			return "success";
		}
	}

	/**
	 * メニュー単位で表示フラグを設定する。
	 * @param gamenId        画面ID
	 * @return 表示フラグ
	 */
	protected boolean setHyoujiFlg(String gamenId) {
		return myLogic.setHyoujiFlg(userInfo, gamenId);
	}
	
	/** 
	 * 経費立替精算代理起票のメニュー表示判定
	 * @param connection      コネクション
	 * @return 経費立替精算代理起票フラグ
	 */
	protected boolean setDairikihyouFlg(EteamConnection connection) {
		
		//部門所属ユーザ以外は非表示
		if (gyoumuRoleId != null) return false;
		//代行モードなら非表示
		if (! userInfo.getLoginUserId().equals(userInfo.getSeigyoUserId()))
		{
			return false;
		}
		//新規起票が利用できないなら代理起票も非表示
		if(! shinkiIchiranHyoujiFlg) return false;
		//代理起票権限がなければ非表示
		boolean dairikihyouFlg = false;
		GMap userJouhouMap = bumonUserLogic.selectUserJouhou(loginUserId);
		dairikihyouFlg     = userJouhouMap.get("dairikihyou_flg").equals(GAMEN_KENGEN_SEIGYO_KBN.YUUKOU);
		return dairikihyouFlg;
	}
	
	/**
	 * 代行登録のメニュー表示判定
	 * @param connection      コネクション
	 */
	protected void setDaikouHyoujiFlg(EteamConnection connection) {
		daikousyaTourokuKanriHyoujiFlg= false;
		daikousyaTourokuHyoujiFlg = false;
		
		//経費精算機能オフなら無効
		if (!RegAccess.checkEnableKeihiSeisan())
		{
			return;
		}
		
		// 代行機能が有効かどうか
		// 各利用可否フラグの取得
		GMap accessFlg = syslc.findGamenKengenSeigyoInfoFlg("DaikoushaShitei");
		// 機能制御コードを取得します。
		String kinouSeigyoCd = accessFlg.get("kinou_seigyo_cd").toString();
		// 機能制御を検索し、機能の有効・無効を判定します。
		if (kinouSeigyoCd.length() > 0) {
			String kinouSeigyoKbn = syslc.findKinouSeigyo(kinouSeigyoCd).get("kinou_seigyo_kbn").toString();
			if(KINOU_SEIGYO_KBN.YUUKOU.equals(kinouSeigyoKbn)) {
				// 代行機能が有効である場合、ユーザーの権限を判定
				String accessAuthority = EteamCommon.getAccessAuthorityLevel(connection, gyoumuRoleId, "CO");
				
				// 一般ユーザーでログインしていたら個人設定の代行者登録を表示
				if(isEmpty(gyoumuRoleId) && accessAuthority.length() == 0) {
					if (syslc.judgeKinouSeigyoON(KINOU_SEIGYO_CD.USER_DAIKOU_SHITEI)) {
						daikousyaTourokuHyoujiFlg = true;
					}
				}
				// adminユーザーか会社設定業務ロールでログインしていたら会社設定の代行者登録を表示
				if (accessAuthority.equals("SU") || accessAuthority.equals("CO")) {
					if (syslc.judgeKinouSeigyoON(KINOU_SEIGYO_CD.KANRI_DAIKOU_SHITEI)) {
						daikousyaTourokuKanriHyoujiFlg= true;
					}
				}
			}
		}
	}

}
	