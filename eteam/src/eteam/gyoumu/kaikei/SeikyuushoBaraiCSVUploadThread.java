package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.base.GMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.HfUfSeigyo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.gyoumu.kaikei.SeikyuushoBaraiCSVUploadSessionInfo.UploadStatus;
import eteam.gyoumu.system.NittyuuDataRenkeiThread;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
/**
 * 請求書払い申請一括登録処理のスレッドクラスです。
 */
public class SeikyuushoBaraiCSVUploadThread extends Thread {
	
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(NittyuuDataRenkeiThread.class);

	// 引数
		/** スキーマ名 */
		String schema;
		/** セッション */
		Map<String, Object> session;
		/** セッション */
		SeikyuushoBaraiCSVUploadSessionInfo sessionInfo;
		
	//＜セッション＞
		/** CSVファイル名 */
		String csvFileName;
		/** CSVファイル情報（申請内容）リスト */
		List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList;
		
	//＜部品＞
		/** エラー情報 */
		List<String> errorList = new ArrayList<String>();
		
	//＜画面制御情報＞
		/** HF・UF制御クラス */
		HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
		/** ステータス */
		UploadStatus status;
		/** SIASバージョンか判別 **/
		boolean issias = (Open21Env.getVersion() == Version.SIAS ) ? true : false; // (変数名が小文字でないとjspで読み取れない？)
		/** 会社設定 */
		EteamSettingInfo setting = new EteamSettingInfo();
		/** インボイス開始フラグ */
		String invoiceStartFlg;
		/** インボイス設定フラグ */
		String invoiceSetteiFlg = setting.invoiceSetteiFlg();
		
	//＜部品＞
		/** マスターSELECT */
		MasterKanriCategoryLogic masterLogic;
		/** 会計共通SELECT */
		KaikeiCommonLogic kaikeiCommonLogic;
		/** 会計SELECT */
		KaikeiCategoryLogic kaikeiCategoryLogic;
		/** 仕訳パターンマスターDao */
		ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
		/** 消費税設定Dao */
		KiShouhizeiSettingDao kiShouhizeiSettingDao;
		/** WorkflowEventControlLogic */
		WorkflowEventControlLogic wfEventLogic;
		
	/**
	 * コンストラクタ
	 * @param session セッションマップ
	 * @param sessionInfo セッション情報
	 * @param schema      スキーマ名
	 */
	public SeikyuushoBaraiCSVUploadThread(Map<String, Object> session, SeikyuushoBaraiCSVUploadSessionInfo sessionInfo, String schema){
		this.session = session;
		this.sessionInfo = sessionInfo;
		this.schema = schema;
		}
		
	/**
	 * 各バッチ処理を実行する。
	 */
	public void run(){
		
		try{
			// スキーマを起動元のコンテキストから渡されたものに
			Map<String, String> threadMap = EteamThreadMap.get();
			threadMap.put(SYSTEM_PROP.SCHEMA, schema);
			
			// 登録処理
			this.touroku();
			
		}catch (Throwable e){
			log.error("エラー発生", e);
		}
	}

	/**
	 * 登録イベント
	 */
	public void touroku(){
		try(EteamConnection connection = EteamConnection.connect()) {
			setConnection(connection);
			csvFileName = sessionInfo.getFileName();
			denpyouList = sessionInfo.getDenpyouList();
			invoiceStartFlg = wfEventLogic.judgeinvoiceStart();
			
			//伝票１つずつつくる
			for (SeikyuushoBaraiCSVUploadDenpyouInfo denpyou : denpyouList) {
				SeikyuushoBaraiAction action = makeDenpyouAction(denpyou);
				if ("success".equals(action.touroku())) {
					denpyou.setDenpyouId(action.getDenpyouId());
				} else {
					//SeikyuushoBaraiActionで発生した例外はそのままthrowされる。ここでいうnot successは例外で終わっていないエラー。
					errorList.add("伝票No." + denpyou.getDenpyouNo() + "でエラー。");
					errorList.addAll(action.getErrorList());
				}
			}
			// 処理結果をセッションに格納。呼び出し元のactionクラスで取得する
			sessionInfo.setErrorList(errorList);
			sessionInfo.setFileName(csvFileName);
			sessionInfo.setDenpyouList(denpyouList);
			sessionInfo.setStatus(UploadStatus.End);
			session.put(Sessionkey.SEIKYUUSHO_CSV, sessionInfo);
		}
	}
	
	
	
	/**
	 * 請求書払い申請のアクションクラス（ユーザ入力後）の状態を再現。
	 * @param cmnInfo CSV情報
	 * @return アクションクラス
	 */
	protected SeikyuushoBaraiAction makeDenpyouAction(SeikyuushoBaraiCSVUploadDenpyouInfo cmnInfo) {
	
		//請求書払いActionをインスタンス化
		SeikyuushoBaraiAction action = new SeikyuushoBaraiAction();
		action.setCsvUploadFlag(true);
		
		//入力方式からの金額計算必要
		String invoiceFlg = cmnInfo.invoiceDenpyou;
		if("1".equals(invoiceStartFlg)) {
			//インボイス開始後は、会社設定の「インボイス対応伝票 設定項目」を使用しないなら強制的にインボイス伝票
			// 使用する なのにカラム空だったらインボイス伝票とみなす
			if("0".equals(invoiceSetteiFlg) || action.isEmpty(invoiceFlg)) {
				invoiceFlg = "0";
			}
		}else {
			//インボイス開始前は強制的に旧伝票
			invoiceFlg = "1";
		}
		String nyuryokuHoushikiFlg = "1".equals(invoiceFlg) ? "0" : setting.zeiDefaultA003();
		
		//仕訳枝番号から課税区分仕入区分分離区分を取得する？
		//取引先コードから事業者区分を取得する？
		boolean enableJigyoushaHenkou = "1".equals(setting.jigyoushaHenkouFlgA003());
		
		Date today = new Date(System.currentTimeMillis());
		var kiShouhizeiSettingDto = kiShouhizeiSettingDao.findByDate(today);
		String hasuuShoriFlg = Integer.toString(kiShouhizeiSettingDto.hasuuShoriFlg);

		//--------------------
		//伝票共通部をセット
		//--------------------
		action.setDenpyouKbn(EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI);
		action.setLoginUserInfo((User)session.get(Sessionkey.USER));
		action.init();
		action.setKihyouBumonCd(((User)session.get(Sessionkey.USER)).getBumonCd()[0]);
		action.setKihyouBumonName(((User)session.get(Sessionkey.USER)).getBumonName()[0]);
		action.setBumonRoleId(((User)session.get(Sessionkey.USER)).getBumonRoleId()[0]);
		action.setBumonRoleName(((User)session.get(Sessionkey.USER)).getBumonRoleName()[0]);
		GMap daihyouFutanBumonNm = masterLogic.findBumonMasterByCd(((User)session.get(Sessionkey.USER)).getDaihyouFutanCd()[0]);
		action.setDaihyouFutanBumonCd(daihyouFutanBumonNm == null ? "" : ((User)session.get(Sessionkey.USER)).getDaihyouFutanCd()[0]);
		action.setEbunsho_tenpufilename(new String[0]);
		action.setEbunsho_tenpufilename_header(new String[0]);
		action.setEbunshoflg(new String[0]);
		action.setDenshitorihikiFlg(new String[0]);
		action.setTsfuyoFlg(new String[0]);
		
		//--------------------
		//伝票明細部分をセット
		//仕訳パターンの読込やマスター名称変換による上書登録の項目はブランクにしておいてよい
		//--------------------
		int len = cmnInfo.getMeisaiList().size();
		String[] shiwakeEdaNo = new String[len];
		String[] torihikiName = new String[len];
		String[] kamokuCd = new String[len];
		String[] kamokuName = new String[len];
		String[] kamokuEdabanCd = new String[len];
		String[] kamokuEdabanName = new String[len];
		String[] futanBumonCd = new String[len];
		String[] futanBumonName = new String[len];
		String[] torihikisakiCd = new String[len];
		String[] torihikisakiName = new String[len];
		String[] furikomisakiJouhou = new String[len];
		String[] uf1Cd = new String[len];
		String[] uf1Name = new String[len];
		String[] uf2Cd = new String[len];
		String[] uf2Name = new String[len];
		String[] uf3Cd = new String[len];
		String[] uf3Name = new String[len];
		String[] uf4Cd = new String[len];
		String[] uf4Name = new String[len];
		String[] uf5Cd = new String[len];
		String[] uf5Name = new String[len];
		String[] uf6Cd = new String[len];
		String[] uf6Name = new String[len];
		String[] uf7Cd = new String[len];
		String[] uf7Name = new String[len];
		String[] uf8Cd = new String[len];
		String[] uf8Name = new String[len];
		String[] uf9Cd = new String[len];
		String[] uf9Name = new String[len];
		String[] uf10Cd = new String[len];
		String[] uf10Name = new String[len];
		String[] projectCd = new String[len];
		String[] projectName = new String[len];
		String[] segmentCd = new String[len];
		String[] segmentName = new String[len];
		String[] kazeiKbn = new String[len];
		String[] zei = new String[len];
		String[] keigenZeiritsuKbn = new String[len];
		String[] shiharaiKingaku = new String[len];
		String[] hontaiKingaku = new String[len];
		String[] shouhizeigaku = new String[len];
		String[] tekiyou = new String[len];
		String[] kousaihiShousai = new String[len];
		String[] kousaihiHyoujiFlg = new String[len];
		String[] ninzuuRiyouFlg = new String[len];
		String[] kousaihiNinzuu = new String[len];
		String[] kousaihiHitoriKingaku = new String[len];
		String[] tekiyouCd = new String[len];
		String[] jigyoushaKbn = new String[len];
		String[] bunriKbn = new String[len];
		String[] kariShiireKbn = new String[len];
		//CSVファイル情報（申請内容明細）
		for(int j = 0; j < cmnInfo.getMeisaiList().size(); j++){
			SeikyuushoBaraiCSVUploadMeisaiInfo detailInfo = cmnInfo.getMeisaiList().get(j);
			shiwakeEdaNo [j] = detailInfo.getShiwakeEdaNo();
			torihikiName [j] = "";
			kamokuCd [j] = "";
			kamokuName [j] = "";
			kamokuEdabanCd [j] = detailInfo.getKamokuEdabanCd();
			kamokuEdabanName [j] = "";
			futanBumonCd [j] = detailInfo.getFutanBumonCd();
			futanBumonName [j] = "";
			torihikisakiCd [j] = detailInfo.getTorihikisakiCd();
			torihikisakiName [j] = "";
			furikomisakiJouhou [j] = kaikeiCommonLogic.furikomisakiSakusei(detailInfo.getTorihikisakiCd());
			uf1Cd[j] = detailInfo.getUf1Cd();
			uf1Name[j] = "";
			uf2Cd[j] = detailInfo.getUf2Cd();
			uf2Name[j] = "";
			uf3Cd[j] = detailInfo.getUf3Cd();
			uf3Name[j] = "";
			uf4Cd[j] = detailInfo.getUf4Cd();
			uf4Name[j] = "";
			uf5Cd[j] = detailInfo.getUf5Cd();
			uf5Name[j] = "";
			uf6Cd[j] = detailInfo.getUf6Cd();
			uf6Name[j] = "";
			uf7Cd[j] = detailInfo.getUf7Cd();
			uf7Name[j] = "";
			uf8Cd[j] = detailInfo.getUf8Cd();
			uf8Name[j] = "";
			uf9Cd[j] = detailInfo.getUf9Cd();
			uf9Name[j] = "";
			uf10Cd[j] = detailInfo.getUf10Cd();
			uf10Name[j] = "";
			projectCd[j] = detailInfo.getProjectCd();
			projectName[j] = "";
			segmentCd[j] = detailInfo.getSegmentCd();
			segmentName[j] = "";
			kazeiKbn [j] = "";
			zei [j] = detailInfo.getZeiritsu();
			keigenZeiritsuKbn [j] = detailInfo.getKeigenZeiritsuKbn();
			//金額計算のため、金額より先に値を確定させる
			if("1".equals(invoiceFlg)) {
				//インボイス開始前+旧伝票は強制的に0通常課税
				jigyoushaKbn[j] = "0";
			}else {
				//取引先マスタから取得→変更可能かつcsv入力ありならcsvを優先
				GMap master = masterLogic.findTorihikisakiJouhou(torihikisakiCd[j], false);
				jigyoushaKbn[j] = master == null ? "0" :(String)master.get("menzei_jigyousha_flg");
				if(enableJigyoushaHenkou && !"".equals(detailInfo.jigyoushaKbn)) {
					jigyoushaKbn[j] = detailInfo.getJigyoushaKbn();
				}
			}
			String kingaku ="0".equals(nyuryokuHoushikiFlg) ? detailInfo.getShiharaiKingaku() : detailInfo.getZeinukiKingaku();
			tekiyou				[j] = detailInfo.getTekiyou();
			kousaihiShousai		[j] = "";
			GMap shiwakeP = kaikeiCategoryLogic.findShiwakePattern(DENPYOU_KBN.SEIKYUUSHO_BARAI, Integer.parseInt(shiwakeEdaNo[j]));
			//仕訳パターンから任意税率か確認する
			int zeiritsuForCalc = StringUtils.isEmpty(detailInfo.getZeiritsu()) ? 0 : Integer.parseInt(detailInfo.getZeiritsu());
			if(!ShiwakeConst.ZEIRITSU.equals(shiwakeP.get("kari_zeiritsu"))) {
				zeiritsuForCalc = Integer.parseInt(shiwakeP.get("kari_zeiritsu"));
			}
			GMap mp = kaikeiCommonLogic.calcKingakuAndTaxForIkkatsu(nyuryokuHoushikiFlg,kingaku, zeiritsuForCalc,jigyoushaKbn[j],kiShouhizeiSettingDto,detailInfo.getShouhizeigaku(),(String)shiwakeP.get("kari_kazei_kbn"));
			shiharaiKingaku		[j] = mp.get("shiharaiKingaku").toString();
			hontaiKingaku		[j] = mp.get("hontaiKingaku").toString();
			shouhizeigaku		[j] = mp.get("shouhizeigaku").toString();
// GMap shiwakeP = shiwakePatternMasterDao.find(DENPYOU_KBN.SEIKYUUSHO_BARAI,Integer.parseInt(shiwakeEdaNo[j])).map;
			kousaihiHyoujiFlg [j] = (shiwakeP != null ? (String)shiwakeP.get("kousaihi_hyouji_flg") : "0");
			ninzuuRiyouFlg [j] = (shiwakeP != null ? (String)shiwakeP.get("kousaihi_ninzuu_riyou_flg") : "0");
			//交際費人数から一人当たり金額を算出して代入するが、取引の交際費使用がオフならreloadShiwakePatternでブランクに戻る
			kousaihiNinzuu [j] = detailInfo.getKousaihiNinzuu();
			if( (!("1".equals(kousaihiHyoujiFlg[j]))) || (!("1".equals(ninzuuRiyouFlg[j]))) ) {
				//交際費使用・人数項目使用どちらかがオフなら人数ブランク
				kousaihiNinzuu[j] = "";
			}
			if( (null != kousaihiNinzuu[j]) && !(kousaihiNinzuu[j].toString().isEmpty()) ) {
				BigDecimal hitoriKingaku = new BigDecimal(shiharaiKingaku[j]); //shiharaiKingakuに入る値が必ず税込ではなくなったため
				BigDecimal ninzuu = new BigDecimal(kousaihiNinzuu[j]);
				hitoriKingaku = hitoriKingaku.divide(ninzuu, 0, RoundingMode.DOWN);
				kousaihiHitoriKingaku[j] = hitoriKingaku.toPlainString();
			}else {
				//人数ブランクなら金額もブランク
				kousaihiHitoriKingaku[j] = "";
			}
			tekiyouCd [j] = "";
			//分離区分と仕入区分は課税区分と同じようにAction.javaのreloadShiwakePattern()でセットするのでここでは空白
			bunriKbn [j] = "";
			kariShiireKbn [j] = "";
		}
		action.setShiwakeEdaNo (shiwakeEdaNo);
		action.setTorihikiName (torihikiName);
		action.setKamokuCd (kamokuCd);
		action.setKamokuName (kamokuName);
		action.setKamokuEdabanCd (kamokuEdabanCd);
		action.setKamokuEdabanName (kamokuEdabanName);
		action.setFutanBumonCd (futanBumonCd);
		action.setFutanBumonName (futanBumonName);
		action.setTorihikisakiCd (torihikisakiCd);
		action.setTorihikisakiName (torihikisakiName);
		action.setFurikomisakiJouhou(furikomisakiJouhou);
		action.setKazeiKbn (kazeiKbn);
		action.setUf1Cd(uf1Cd);
		action.setUf1Name(uf1Name);
		action.setUf2Cd(uf2Cd);
		action.setUf2Name(uf2Name);
		action.setUf3Cd(uf3Cd);
		action.setUf3Name(uf3Name);
		action.setUf4Cd(uf4Cd);
		action.setUf4Name(uf4Name);
		action.setUf5Cd(uf5Cd);
		action.setUf5Name(uf5Name);
		action.setUf6Cd(uf6Cd);
		action.setUf6Name(uf6Name);
		action.setUf7Cd(uf7Cd);
		action.setUf7Name(uf7Name);
		action.setUf8Cd(uf8Cd);
		action.setUf8Name(uf8Name);
		action.setUf9Cd(uf9Cd);
		action.setUf9Name(uf9Name);
		action.setUf10Cd(uf10Cd);
		action.setUf10Name(uf10Name);
		action.setProjectCd(projectCd);
		action.setProjectName(projectName);
		action.setSegmentCd(segmentCd);
		action.setSegmentName(segmentName);
		action.setZeiritsu (zei);
		action.setKeigenZeiritsuKbn (keigenZeiritsuKbn);
		action.setShiharaiKingaku (shiharaiKingaku);
		action.setHontaiKingaku (hontaiKingaku);
		action.setShouhizeigaku (shouhizeigaku);
		action.setTekiyou (tekiyou);
		action.setKousaihiShousai (kousaihiShousai);
		action.setKousaihiHyoujiFlg (kousaihiHyoujiFlg);
		action.setNinzuuRiyouFlg (ninzuuRiyouFlg);
		action.setKousaihiNinzuu (kousaihiNinzuu);
		action.setKousaihiHitoriKingaku (kousaihiHitoriKingaku);
		action.setTekiyouCd (tekiyouCd);
		action.jigyoushaKbn = jigyoushaKbn;
		action.bunriKbn = bunriKbn;
		action.kariShiireKbn = kariShiireKbn;
		//--------------------
		//申請内容本体部分をセット
		//--------------------
		action.setShiharaiKigen(cmnInfo.getShiharaiKigen());
		action.setHontaiKakeFlg("");
		action.setHf1Cd(cmnInfo.getHf1Cd());
		action.setHf2Cd(cmnInfo.getHf2Cd());
		action.setHf3Cd(cmnInfo.getHf3Cd());
		action.setHf4Cd(cmnInfo.getHf4Cd());
		action.setHf5Cd(cmnInfo.getHf5Cd());
		action.setHf6Cd(cmnInfo.getHf6Cd());
		action.setHf7Cd(cmnInfo.getHf7Cd());
		action.setHf8Cd(cmnInfo.getHf8Cd());
		action.setHf9Cd(cmnInfo.getHf9Cd());
		action.setHf10Cd(cmnInfo.getHf10Cd());
		action.setHontaiKingakuGoukei(sum(hontaiKingaku));
		action.setShouhizeigakuGoukei(sum(shouhizeigaku));
		action.setKingaku(sum(shiharaiKingaku));
		action.setHosoku("");
		action.setGaibuKoushinUserId(((User)session.get(Sessionkey.USER)).getTourokuOrKoushinUserId());
		action.nyuryokuHoushiki = nyuryokuHoushikiFlg;
		action.setInvoiceDenpyou(invoiceFlg);
		//------------------------------------------------------
		//セッションから取得した起案伝票IDが
		//・19文字の文字列の場合（「"0"」のでない場合）
		//　起案伝票IDと起案伝票区分にそれぞれ値を格納し、
		//　起案紐付フラグを「"1"」とする。
		//
		//・それ以外の場合
		//　起案伝票IDと起案伝票区分にそれぞれブランクを格納し、
		//　起案紐付フラグを「"0"」とする。
		//------------------------------------------------------
		String[] KianDenpyouId = new String[1];
		String[] KianDenpyouKbn = new String[1];
		if(!cmnInfo.getKianDenpyouId().equals("0")) {
			KianDenpyouId[0] = cmnInfo.getKianDenpyouId();
			action.setKianDenpyouId(KianDenpyouId);
			String KiandenpyouKbnStr = cmnInfo.getKianDenpyouId().substring(7, 11);
			KianDenpyouKbn[0] = KiandenpyouKbnStr;
			action.setKianDenpyouKbn(KianDenpyouKbn);
			action.setKianHimodukeFlg("1");
		}else {
			KianDenpyouId[0] = "";
			action.setKianDenpyouId(KianDenpyouId);
			KianDenpyouKbn[0] = "";
			action.setKianDenpyouKbn(KianDenpyouKbn);
			action.setKianHimodukeFlg("0");
			
		}
		
		return action;
	}

	/**
	 * 金額の合計を返す。
	 * @param arr 明細金額
	 * @return 合計金額
	 */
	public String sum(String[] arr) {
		BigDecimal ret = new BigDecimal(0);
		for (String g : arr) ret = ret.add(new BigDecimal(g));
		return ret.toPlainString();
	}

	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		masterLogic = (MasterKanriCategoryLogic)EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiCommonLogic = (KaikeiCommonLogic)EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		kaikeiCategoryLogic = (KaikeiCategoryLogic)EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
	}
}
