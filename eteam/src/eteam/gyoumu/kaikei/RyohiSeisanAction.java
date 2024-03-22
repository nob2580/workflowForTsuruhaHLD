package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.database.abstractdao.RyohiseisanAbstractDao;
import eteam.database.abstractdao.RyohiseisanKeihiMeisaiAbstractDao;
import eteam.database.abstractdao.RyohiseisanMeisaiAbstractDao;
import eteam.database.dao.RyohiseisanDao;
import eteam.database.dao.RyohiseisanKeihiMeisaiDao;
import eteam.database.dao.RyohiseisanMeisaiDao;
import eteam.database.dto.Ryohiseisan;
import eteam.database.dto.RyohiseisanKeihiMeisai;
import eteam.database.dto.RyohiseisanMeisai;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 旅費精算Action
 */
@Getter @Setter @ToString
public class RyohiSeisanAction extends RyohiSeisanCommonAction {

//＜定数＞

//＜画面入力＞

	//画面入力（申請内容）→全てCommon

	//画面入力（明細/旅費）→同上

	//画面入力（明細/経費）→同上
	
	//画面制御情報→同上

	// 会社設定情報→同上

//＜部品＞
	/** 旅費精算ロジック */
	RyohiSeisanLogic RyohiSeisanLogic;
	/** 旅費精算DAO */
	RyohiseisanAbstractDao ryohiseisanDao;
	/** 旅費精算明細DAO */
	RyohiseisanMeisaiAbstractDao ryohiseisanMeisaiDao;
	/** 旅費精算経費明細DAO */
	RyohiseisanKeihiMeisaiAbstractDao ryohiseisanKeihiMeisaiDao;


//＜親子制御＞

//＜入力チェック＞
	@Override
	protected void denpyouFormatCheck() {
		// 基本項目のチェック（ID～合計金額）
		super.commonDenpyouFormatCheckBase();
		// 旅費伝票部項目チェック
		super.commonKokunaiRyohiCheck(false);
		// 会社設定に差引項目が入力されている場合
		if( sasihikiHyoujiFlg ){
			checkNumberRange3(sashihikiNum, 1, 99, sashihikiName, false);
			checkKingakuMinus(sashihikiKingaku, ks.sashihikiKingaku.getName(), false);
		}
		checkString (hosoku,1 ,240 , ks.hosoku.getName(), false);

		if (judgeKeihiEntry()) {

			//画面入力（明細）
			for (int i = 0; i < shubetsuCd.length; i++) {
				super.commonRyohiMeisaiFormatCheck(i, false, koutsuuShudanDomain);
			}

			//画面入力（経費明細）
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
				String ip = "その他 経費 " + (i + 1);
				this.commonKeihiMeisaiFormatCheck(i, ip);
			}
		}
	}
	
	@Override
	protected void soukanCheck(String user_Id) {
		// 旅費伝票までの共通部
		super.commonSoukanCheckBase(user_Id, false);
		
		// 明細の共通チェック
		super.soukanCheckMeisai();
	}
	
	@Override
	protected void checkSashihiki()
	{
		// 会社設定に差引項目が入力されている場合
		if( sasihikiHyoujiFlg ){
			double hontaiGoukei = 0;
			double shouhizeigakuTotal = 0;
			if (meisaiKingaku != null)
			{
				for (int i = 0; i < meisaiKingaku.length ; i++) {
					if (isNotEmpty(meisaiKingaku[i]) && ! houjinCardFlgRyohi[i].equals("1") && ! kaishaTehaiFlgRyohi[i].equals("1")) {
						hontaiGoukei += toDecimal(meisaiKingaku[i]).doubleValue();
						shouhizeigakuTotal += toDecimal(shouhizeigakuRyohi[i]).doubleValue();
					}
				}
			}
			if (isNotEmpty(sashihikiKingaku)) {
				hontaiGoukei += toDecimal(sashihikiKingaku).doubleValue();
				var hasuuShoriFlg = this.getHasuuShoriFlg();
				var zeiritsuDecimal = toDecimal(List.of("001", "002", "011", "012", "013", "014").contains(this.kazeiKbnRyohi) ? this.zeiritsuRyohi : "0");
				var sashihikiZeigaku = toDecimal(sashihikiKingaku).multiply(zeiritsuDecimal).divide(new BigDecimal("100").add(zeiritsuDecimal), 3, RoundingMode.HALF_UP);
				sashihikiZeigaku = this.commonLogic.processShouhizeiFraction(hasuuShoriFlg, sashihikiZeigaku, true);
				shouhizeigakuTotal += sashihikiZeigaku.doubleValue();
			}
			if (hontaiGoukei < 0) {
				errorList.add("交通費と日当・宿泊費等の合計がマイナスです。");
			}
			if (shouhizeigakuTotal < 0) {
				errorList.add("交通費と日当・宿泊費等の消費税合計がマイナスです。");
			}
		}
	}

//＜伝票共通から呼ばれるイベント処理＞
	//登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {
		super.commonCheckKobetsuDenpyou(connection, true);
	}

	//登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		//申請内容登録
		Ryohiseisan dto = this.createDto();
		this.ryohiseisanDao.insert(dto, dto.tourokuUserId);

		// 明細必須フラグ
		boolean keihiEntryFlg = judgeKeihiEntry();

		if (keihiEntryFlg) {

			//明細登録（旅費）
			if (!shubetsuCd[0].isEmpty()) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					RyohiseisanMeisai meisaiDto = this.createMeisaiDto(i);
					ryohiseisanMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);

					if(!isEmpty(himodukeCardMeisaiRyohi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
					}
				}
			}

			//明細登録（経費）
			if (!shiwakeEdaNo[0].isEmpty()) {
				for (int i = 0; i < shiwakeEdaNo.length; i++) {
					RyohiseisanKeihiMeisai keihiMeisaiDto = this.createKeihiMeisaiDto(i);
					this.ryohiseisanKeihiMeisaiDao.insert(keihiMeisaiDto, keihiMeisaiDto.tourokuUserId);
					if(!isEmpty(himodukeCardMeisaiKeihi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
					}
				}
			}
		}
	}

	//更新ボタン押下時に、個別伝票について以下を行う。
	//・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	//・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		super.commonCheckKobetsuDenpyou(connection, false);
		if (0 <errorList.size())
		{
			return;
		}

		//計上日の値を設定に沿ってセット
		keijoubi = commonLogic.setKeijoubi(hasseiShugi, keijouBiMode, keijoubi, keijoubiDefaultSettei, setting.keijouNyuuryoku(), denpyouKbn, seisankikanTo, errorList,loginUserInfo);
		if (0 <errorList.size())
		{
			return;
		}

		//申請内容更新
		Ryohiseisan dto = this.createDto();
		this.ryohiseisanDao.update(dto, dto.koushinUserId);

		//明細削除＆登録
		ryohiseisanMeisaiDao.deleteByDenpyouId(denpyouId);
		this.ryohiseisanKeihiMeisaiDao.deleteByDenpyouId(this.denpyouId);
		hcLogic.removeDenpyouHimozuke(denpyouId);

		if (judgeKeihiEntry()) {
			//明細登録（旅費）
			if (!shubetsuCd[0].isEmpty()) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					RyohiseisanMeisai meisaiDto = this.createMeisaiDto(i);
					ryohiseisanMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
					if(!isEmpty(himodukeCardMeisaiRyohi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
					}
				}
			}
			//明細登録（経費）
			if (!shiwakeEdaNo[0].isEmpty()) {
				for (int i = 0; i < shiwakeEdaNo.length; i++) {
					RyohiseisanKeihiMeisai keihiMeisaiDto = this.createKeihiMeisaiDto(i);
					this.ryohiseisanKeihiMeisaiDao.insert(keihiMeisaiDto, keihiMeisaiDto.koushinUserId);
					if(!isEmpty(himodukeCardMeisaiKeihi[i])){
						hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiKeihi[i]),denpyouId);
					}
				}
			}
		}
	}

	//承認ボタン押下時に、個別伝票について以下を行う。
	//・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 再表示用
		displaySeigyo(connection);

		//支払日・計上日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			GMap data = kaikeiLogic.findRyohiSeisan(denpyouId);
			String shiharai = formatDate(data.get("shiharaibi"));
			String keijou = hasseiShugi ? formatDate(data.get("keijoubi"))  : formatDate(data.get("seisankikan_to"));
			String keijouName = hasseiShugi ? "計上日" : ks.seisankikan.getName() + "終了日";
			if(judgeKeihiEntry() && hasseiShugi && isEmpty(keijou)) errorList.add(keijouName + "が未登録です。" + keijouName +"を登録してください。");
			checkShiharaiBi(shiharai, keijou, keijouName);
			// エラーの場合の表示用に現実の値を設定
			shiharaibi = shiharai;
			if (hasseiShugi)
			{
				keijoubi = keijou;
			} else seisankikanTo = keijou;
		}

		//仮払指定時かつ最終承認時は仮払伝票の精算完了日を更新
		if( (!isEmpty(karibaraiDenpyouId)) && wfEventLogic.isLastShounin(denpyouId,loginUserInfo)){
			this.ryohiseisanDao.updateKaribaraiSeisanbi(karibaraiDenpyouId,loginUserId);
		}
	}

	//代表仕訳枝番号を設定
	@Override
	protected String getDaihyouTorihiki(){
		return shiwakeEdaNoRyohi;
	}

	// 起案終了／起案終了解除の対象とする仮払伝票IDを取得する。
	@Override
	protected String getKianShuuryouKaribaraiDenpyouId(){
		String result = null;

		// 仮払申請が予算執行対象
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI);
		if(YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(denpyouShubetsuMap.get("yosan_shikkou_taishou").toString())){

			// 仮払金未使用
			if("1".equals(this.karibaraiMishiyouFlg) || "1".equals(this.shucchouChuushiFlg)){
				// 仮払伝票IDを返却
				result = this.karibaraiDenpyouId;
			}
		}
		return result;
	}

	//科目が予算執行対象かどうか判定する。
	@Override
	protected boolean isCheckTaishougaiBumonKamoku(EteamConnection connection) {

		HashSet<String> kamokuCdSet = new HashSet<String>();
		if(isNotEmpty(kamokuCdRyohi)) kamokuCdSet.add(kamokuCdRyohi);
		for(int i = 0 ; i < kamokuCd.length ; i++){
			kamokuCdSet.add(kamokuCd[i]);
		}
		if(0 == kamokuCdSet.size()) { return false; }

		if(null == commonLogic){
			commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		}
		return commonLogic.isYosanShikkouKamoku(denpyouId, kamokuCdSet, kianbangouBoDialogNendo);
	}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	@Override
	protected void initParts(EteamConnection connection) {
		super.initParts(connection);
		RyohiSeisanLogic = EteamContainer.getComponent(RyohiSeisanLogic.class, connection);
		ryohiseisanDao = EteamContainer.getComponent(RyohiseisanDao.class, connection);
		ryohiseisanMeisaiDao = EteamContainer.getComponent(RyohiseisanMeisaiDao.class, connection);
		ryohiseisanKeihiMeisaiDao = EteamContainer.getComponent(RyohiseisanKeihiMeisaiDao.class, connection);
	}

	/**
	 * 旅費精算テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 旅費精算レコード
	 * @param sanshou 参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection コネクション
	 */
	@Override
	protected void shinseiData2Gamen(GMap shinseiData, boolean sanshou, EteamConnection connection) {
		super.shinseiData2Gamen(shinseiData, sanshou, connection);
		var chuuki = super.setChuuki(sanshou, shinseiData, null, batchKaikeiLogic, commonLogic);
		this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : chuuki[0];
		this.chuuki2Ryohi = chuuki[1];
	}


	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		super.displaySeigyo(connection);

		//経理担当が初めて伝票を開き、その伝票が振込のとき、支払日に自動入力
		if (shiharaiBiMode == 1 && isEmpty(shiharaibi) && shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI)) {
			java.sql.Date shiharai = null;
			if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A004).equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(keijoubi));
			} else if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A004).equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(keijoubi));
			}
			if (shiharai != null) {
				shiharaibi = formatDate(shiharai);
				// マスターから基準日(計上日)に対する振込日が見つかった場合、支払日の更新
				this.ryohiseisanDao.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}
	}

	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		// 国内仕訳までの共通部のリロード
		super.reloadShiwakePatternKokunai(connection);
		// 経費部分のリロード
		super.reloadShiwakePatternKeihi();
	}

	/**
	 * 出張旅費精算申請EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		RyohiSeisanXlsLogic ryohiseisanxlsLogic = EteamContainer.getComponent(RyohiSeisanXlsLogic.class, connection);
		ryohiseisanxlsLogic.makeExcel(denpyouId, out);
	}
	
	/**
	 * @return 旅費精算Dto
	 */
	protected Ryohiseisan createDto()
	{
		GMap userInfo = bumonUsrLogic.selectUserInfo(userIdRyohi);
		String shainCd = (userInfo == null) ? "" : (String)userInfo.get("shain_no");
		String userSei = (userInfo == null) ? "" : (String)userInfo.get("user_sei");
		String userMei = (userInfo == null) ? "" : (String)userInfo.get("user_mei");
		
		Ryohiseisan ryohiseisan = new Ryohiseisan();
		ryohiseisan.denpyouId = this.denpyouId;
		ryohiseisan.karibaraiDenpyouId = this.karibaraiDenpyouId;
		ryohiseisan.karibaraiOn = this.karibaraiOn;
		ryohiseisan.karibaraiMishiyouFlg = super.isEmpty(karibaraiMishiyouFlg) ? "0" : karibaraiMishiyouFlg;
		ryohiseisan.shucchouChuushiFlg = super.isEmpty(shucchouChuushiFlg) ? "0" : shucchouChuushiFlg;
		ryohiseisan.dairiflg = this.dairiFlg;
		ryohiseisan.userId = this.userIdRyohi;
		ryohiseisan.shainNo = shainCd;
		ryohiseisan.userSei = userSei;
		ryohiseisan.userMei = userMei;
		ryohiseisan.houmonsaki = this.houmonsaki;
		ryohiseisan.mokuteki = this.mokuteki;
		ryohiseisan.seisankikanFrom = super.toDate(seisankikanFrom);
		ryohiseisan.seisankikanFromHour = this.seisankikanFromHour;
		ryohiseisan.seisankikanFromMin = this.seisankikanFromMin;
		ryohiseisan.seisankikanTo = super.toDate(seisankikanTo);
		ryohiseisan.seisankikanToHour = this.seisankikanToHour;
		ryohiseisan.seisankikanToMin = this.seisankikanToMin;
		ryohiseisan.keijoubi = super.toDate(keijoubi);
		ryohiseisan.shiharaibi = super.toDate(shiharaibi);
		ryohiseisan.shiharaikiboubi = super.toDate(shiharaiKiboubi);
		ryohiseisan.shiharaihouhou = this.shiharaihouhou;
		ryohiseisan.tekiyou = this.tekiyouRyohi;
		ryohiseisan.zeiritsu = super.toDecimalZeroIfEmpty(zeiritsuRyohi);
		ryohiseisan.keigenZeiritsuKbn = EteamConst.keigenZeiritsuKbn.NORMAL;//0固定
		ryohiseisan.goukeiKingaku = super.toDecimal(kingaku);
		ryohiseisan.houjinCardRiyouKingaku = super.toDecimal(houjinCardRiyouGoukei);
		ryohiseisan.kaishaTehaiKingaku = super.toDecimal(kaishaTehaiGoukei);
		ryohiseisan.sashihikiShikyuuKingaku = super.toDecimal(sashihikiShikyuuKingaku);
		ryohiseisan.sashihikiNum = super.toDecimal(sashihikiNum);
		ryohiseisan.sashihikiTanka = super.toDecimal(sashihikiTanka);
		ryohiseisan.hf1Cd = this.hf1Cd;
		ryohiseisan.hf1NameRyakushiki = this.hf1Name;
		ryohiseisan.hf2Cd = this.hf2Cd;
		ryohiseisan.hf2NameRyakushiki = this.hf2Name;
		ryohiseisan.hf3Cd = this.hf3Cd;
		ryohiseisan.hf3NameRyakushiki = this.hf3Name;
		ryohiseisan.hf4Cd = this.hf4Cd;
		ryohiseisan.hf4NameRyakushiki = this.hf4Name;
		ryohiseisan.hf5Cd = this.hf5Cd;
		ryohiseisan.hf5NameRyakushiki = this.hf5Name;
		ryohiseisan.hf6Cd = this.hf6Cd;
		ryohiseisan.hf6NameRyakushiki = this.hf6Name;
		ryohiseisan.hf7Cd = this.hf7Cd;
		ryohiseisan.hf7NameRyakushiki = this.hf7Name;
		ryohiseisan.hf8Cd = this.hf8Cd;
		ryohiseisan.hf8NameRyakushiki = this.hf8Name;
		ryohiseisan.hf9Cd = this.hf9Cd;
		ryohiseisan.hf9NameRyakushiki = this.hf9Name;
		ryohiseisan.hf10Cd = this.hf10Cd;
		ryohiseisan.hf10NameRyakushiki = this.hf10Name;
		ryohiseisan.hosoku = this.hosoku;
		ryohiseisan.shiwakeEdano = super.isEmpty(shiwakeEdaNoRyohi) ? null : Integer.parseInt(shiwakeEdaNoRyohi);
		ryohiseisan.torihikiName = this.torihikiNameRyohi;
		ryohiseisan.kariFutanBumonCd = this.futanBumonCdRyohi;
		ryohiseisan.kariFutanBumonName = this.futanBumonNameRyohi;
		ryohiseisan.torihikisakiCd = this.torihikisakiCdRyohi;
		ryohiseisan.torihikisakiNameRyakushiki = this.torihikisakiNameRyohi;
		ryohiseisan.kariKamokuCd = this.kamokuCdRyohi;
		ryohiseisan.kariKamokuName = this.kamokuNameRyohi;
		ryohiseisan.kariKamokuEdabanCd = this.kamokuEdabanCdRyohi;
		ryohiseisan.kariKamokuEdabanName = this.kamokuEdabanNameRyohi;
		ryohiseisan.kariKazeiKbn = this.kazeiKbnRyohi;
		ryohiseisan.kashiFutanBumonCd = this.kashiFutanBumonCdRyohi;
		ryohiseisan.kashiFutanBumonName = this.kashiFutanBumonNameRyohi;
		ryohiseisan.kashiKamokuCd = this.kashiKamokuCdRyohi;
		ryohiseisan.kashiKamokuName = this.kashiKamokuNameRyohi;
		ryohiseisan.kashiKamokuEdabanCd = this.kashiKamokuEdabanCdRyohi;
		ryohiseisan.kashiKamokuEdabanName = this.kashiKamokuEdabanNameRyohi;
		ryohiseisan.kashiKazeiKbn = this.kashiKazeiKbnRyohi;
		ryohiseisan.uf1Cd = this.uf1CdRyohi;
		ryohiseisan.uf1NameRyakushiki = this.uf1NameRyohi;
		ryohiseisan.uf2Cd = this.uf2CdRyohi;
		ryohiseisan.uf2NameRyakushiki = this.uf2NameRyohi;
		ryohiseisan.uf3Cd = this.uf3CdRyohi;
		ryohiseisan.uf3NameRyakushiki = this.uf3NameRyohi;
		ryohiseisan.uf4Cd = this.uf4CdRyohi;
		ryohiseisan.uf4NameRyakushiki = this.uf4NameRyohi;
		ryohiseisan.uf5Cd = this.uf5CdRyohi;
		ryohiseisan.uf5NameRyakushiki = this.uf5NameRyohi;
		ryohiseisan.uf6Cd = this.uf6CdRyohi;
		ryohiseisan.uf6NameRyakushiki = this.uf6NameRyohi;
		ryohiseisan.uf7Cd = this.uf7CdRyohi;
		ryohiseisan.uf7NameRyakushiki = this.uf7NameRyohi;
		ryohiseisan.uf8Cd = this.uf8CdRyohi;
		ryohiseisan.uf8NameRyakushiki = this.uf8NameRyohi;
		ryohiseisan.uf9Cd = this.uf9CdRyohi;
		ryohiseisan.uf9NameRyakushiki = this.uf9NameRyohi;
		ryohiseisan.uf10Cd = this.uf10CdRyohi;
		ryohiseisan.uf10NameRyakushiki = this.uf10NameRyohi;
		ryohiseisan.projectCd = this.projectCdRyohi;
		ryohiseisan.projectName = this.projectNameRyohi;
		ryohiseisan.segmentCd = this.segmentCdRyohi;
		ryohiseisan.segmentNameRyakushiki = this.segmentNameRyohi;
		ryohiseisan.tekiyouCd = this.tekiyouCdRyohi;
		ryohiseisan.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		ryohiseisan.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		ryohiseisan.bunriKbn = this.bunriKbn;
		ryohiseisan.kariShiireKbn = this.kariShiireKbn;
		ryohiseisan.kashiShiireKbn = this.kashiShiireKbn;
		ryohiseisan.invoiceDenpyou = this.invoiceDenpyou;
		return ryohiseisan;
	}
	
	/**
	 * @param i 明細番号
	 * @return 明細Dto
	 */
	protected RyohiseisanMeisai createMeisaiDto(int i)
	{
		RyohiseisanMeisai ryohiseisanMeisai = new RyohiseisanMeisai();
		ryohiseisanMeisai.denpyouId = this.denpyouId;
		ryohiseisanMeisai.denpyouEdano = i + 1;
		ryohiseisanMeisai.kikanFrom = super.toDate(kikanFrom[i]);
		ryohiseisanMeisai.kikanTo = super.toDate(kikanTo[i]);
		ryohiseisanMeisai.kyuujitsuNissuu = super.toDecimal(kyuujitsuNissuu[i]);
		ryohiseisanMeisai.shubetsuCd = this.shubetsuCd[i];
		ryohiseisanMeisai.shubetsu1 = this.shubetsu1[i];
		ryohiseisanMeisai.shubetsu2 = this.shubetsu2[i];
		ryohiseisanMeisai.hayaFlg = this.hayaFlg[i];
		ryohiseisanMeisai.yasuFlg = this.yasuFlg[i];
		ryohiseisanMeisai.rakuFlg = this.rakuFlg[i];
		ryohiseisanMeisai.koutsuuShudan = this.koutsuuShudan[i];
		ryohiseisanMeisai.shouhyouShoruiHissuFlg = this.shouhyouShoruiHissuFlg[i];
		ryohiseisanMeisai.ryoushuushoSeikyuushoTouFlg = this.ryoushuushoSeikyuushoTouFlg[i];
		ryohiseisanMeisai.naiyou = this.naiyou[i];
		ryohiseisanMeisai.bikou = this.bikou[i];
		ryohiseisanMeisai.oufukuFlg = this.oufukuFlg[i];
		ryohiseisanMeisai.houjinCardRiyouFlg = this.houjinCardFlgRyohi[i];
		ryohiseisanMeisai.kaishaTehaiFlg = this.kaishaTehaiFlgRyohi[i];
		ryohiseisanMeisai.jidounyuuryokuFlg = this.jidounyuuryokuFlg[i];
		ryohiseisanMeisai.nissuu = super.toDecimal(nissuu[i]);
		ryohiseisanMeisai.tanka = super.toDecimal(tanka[i]);
		ryohiseisanMeisai.suuryouNyuryokuType = this.suuryouNyuryokuType[i];
		ryohiseisanMeisai.suuryou = super.toDecimal(suuryou[i]);
		ryohiseisanMeisai.suuryouKigou = this.suuryouKigou[i];
		ryohiseisanMeisai.meisaiKingaku = super.toDecimal(meisaiKingaku[i]);
		ryohiseisanMeisai.icCardNo = this.icCardNo[i];
		ryohiseisanMeisai.icCardSequenceNo = this.icCardSequenceNo[i];
		ryohiseisanMeisai.himodukeCardMeisai = super.isEmpty(himodukeCardMeisaiRyohi[i]) ? null : Long.parseLong(himodukeCardMeisaiRyohi[i]);
		ryohiseisanMeisai.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		ryohiseisanMeisai.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		ryohiseisanMeisai.shiharaisakiName = this.shiharaisakiNameRyohi[i];
		ryohiseisanMeisai.jigyoushaKbn = super.getDefaultJigyoushaKbnIfEmpty(this.jigyoushaKbnRyohi[i]);
		ryohiseisanMeisai.zeinukiKingaku = super.toDecimal(this.zeinukiKingakuRyohi[i]);
		ryohiseisanMeisai.shouhizeigaku = super.toDecimal(this.shouhizeigakuRyohi[i]);
		ryohiseisanMeisai.zeigakuFixFlg = super.getDefaultZeigakuFixFlgIfEmpty(this.zeigakuFixFlg[i]);
		return ryohiseisanMeisai;
	}
	
	/**
	 * @param i 明細番号
	 * @return 経費明細Dto
	 */
	protected RyohiseisanKeihiMeisai createKeihiMeisaiDto(int i)
	{
		RyohiseisanKeihiMeisai ryohiseisanKeihiMeisai = new RyohiseisanKeihiMeisai();
		ryohiseisanKeihiMeisai.denpyouId = this.denpyouId;
		ryohiseisanKeihiMeisai.denpyouEdano = i + 1;
		ryohiseisanKeihiMeisai.shiwakeEdano = Integer.parseInt(shiwakeEdaNo[i]);
		ryohiseisanKeihiMeisai.shiyoubi = super.toDate(shiyoubi[i]);
		ryohiseisanKeihiMeisai.shouhyouShoruiFlg = this.shouhyouShorui[i];
		ryohiseisanKeihiMeisai.torihikiName = this.torihikiName[i];
		ryohiseisanKeihiMeisai.tekiyou = this.tekiyou[i];
		ryohiseisanKeihiMeisai.zeiritsu = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i]) ? super.toDecimal(zeiritsu[i]) : BigDecimal.ZERO;
		ryohiseisanKeihiMeisai.keigenZeiritsuKbn = super.isEmpty(keigenZeiritsuKbn[i]) ? "0" :this.keigenZeiritsuKbn[i];
		ryohiseisanKeihiMeisai.shiharaiKingaku = super.toDecimal(shiharaiKingaku[i]);
		ryohiseisanKeihiMeisai.hontaiKingaku = super.toDecimal(hontaiKingaku[i]);
		ryohiseisanKeihiMeisai.shouhizeigaku = super.toDecimal(shouhizeigaku[i]);
		ryohiseisanKeihiMeisai.houjinCardRiyouFlg = this.houjinCardFlgKeihi[i];
		ryohiseisanKeihiMeisai.kaishaTehaiFlg = this.kaishaTehaiFlgKeihi[i];
		ryohiseisanKeihiMeisai.kousaihiShousaiHyoujiFlg = this.kousaihiHyoujiFlg[i];
		ryohiseisanKeihiMeisai.kousaihiNinzuuRiyouFlg = this.ninzuuRiyouFlg[i];
		ryohiseisanKeihiMeisai.kousaihiShousai = this.kousaihiShousai[i];
		ryohiseisanKeihiMeisai.kousaihiNinzuu = super.isEmpty(kousaihiNinzuu[i]) ? null : Integer.parseInt(kousaihiNinzuu[i]);
		ryohiseisanKeihiMeisai.kousaihiHitoriKingaku = super.toDecimal(kousaihiHitoriKingaku[i]);
		ryohiseisanKeihiMeisai.kariFutanBumonCd = this.futanBumonCd[i];
		ryohiseisanKeihiMeisai.kariFutanBumonName = this.futanBumonName[i];
		ryohiseisanKeihiMeisai.torihikisakiCd = this.torihikisakiCd[i];
		ryohiseisanKeihiMeisai.torihikisakiNameRyakushiki = this.torihikisakiName[i];
		ryohiseisanKeihiMeisai.kariKamokuCd = this.kamokuCd[i];
		ryohiseisanKeihiMeisai.kariKamokuName = this.kamokuName[i];
		ryohiseisanKeihiMeisai.kariKamokuEdabanCd = this.kamokuEdabanCd[i];
		ryohiseisanKeihiMeisai.kariKamokuEdabanName = this.kamokuEdabanName[i];
		ryohiseisanKeihiMeisai.kariKazeiKbn = this.kazeiKbn[i];
		ryohiseisanKeihiMeisai.kashiFutanBumonCd = this.kashiFutanBumonCd[i];
		ryohiseisanKeihiMeisai.kashiFutanBumonName = this.kashiFutanBumonName[i];
		ryohiseisanKeihiMeisai.kashiKamokuCd = this.kashiKamokuCd[i];
		ryohiseisanKeihiMeisai.kashiKamokuName = this.kashiKamokuName[i];
		ryohiseisanKeihiMeisai.kashiKamokuEdabanCd = this.kashiKamokuEdabanCd[i];
		ryohiseisanKeihiMeisai.kashiKamokuEdabanName = this.kashiKamokuEdabanName[i];
		ryohiseisanKeihiMeisai.kashiKazeiKbn = this.kashiKazeiKbn[i];
		ryohiseisanKeihiMeisai.uf1Cd = this.uf1Cd[i];
		ryohiseisanKeihiMeisai.uf1NameRyakushiki = this.uf1Name[i];
		ryohiseisanKeihiMeisai.uf2Cd = this.uf2Cd[i];
		ryohiseisanKeihiMeisai.uf2NameRyakushiki = this.uf2Name[i];
		ryohiseisanKeihiMeisai.uf3Cd = this.uf3Cd[i];
		ryohiseisanKeihiMeisai.uf3NameRyakushiki = this.uf3Name[i];
		ryohiseisanKeihiMeisai.uf4Cd = this.uf4Cd[i];
		ryohiseisanKeihiMeisai.uf4NameRyakushiki = this.uf4Name[i];
		ryohiseisanKeihiMeisai.uf5Cd = this.uf5Cd[i];
		ryohiseisanKeihiMeisai.uf5NameRyakushiki = this.uf5Name[i];
		ryohiseisanKeihiMeisai.uf6Cd = this.uf6Cd[i];
		ryohiseisanKeihiMeisai.uf6NameRyakushiki = this.uf6Name[i];
		ryohiseisanKeihiMeisai.uf7Cd = this.uf7Cd[i];
		ryohiseisanKeihiMeisai.uf7NameRyakushiki = this.uf7Name[i];
		ryohiseisanKeihiMeisai.uf8Cd = this.uf8Cd[i];
		ryohiseisanKeihiMeisai.uf8NameRyakushiki = this.uf8Name[i];
		ryohiseisanKeihiMeisai.uf9Cd = this.uf9Cd[i];
		ryohiseisanKeihiMeisai.uf9NameRyakushiki = this.uf9Name[i];
		ryohiseisanKeihiMeisai.uf10Cd = this.uf10Cd[i];
		ryohiseisanKeihiMeisai.uf10NameRyakushiki = this.uf10Name[i];
		ryohiseisanKeihiMeisai.projectCd = this.projectCd[i];
		ryohiseisanKeihiMeisai.projectName = this.projectName[i];
		ryohiseisanKeihiMeisai.segmentCd = this.segmentCd[i];
		ryohiseisanKeihiMeisai.segmentNameRyakushiki = this.segmentName[i];
		ryohiseisanKeihiMeisai.tekiyouCd = this.tekiyouCd[i];
		ryohiseisanKeihiMeisai.himodukeCardMeisai = super.isEmpty(himodukeCardMeisaiKeihi[i]) ? null : Long.parseLong(himodukeCardMeisaiKeihi[i]);
		ryohiseisanKeihiMeisai.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		ryohiseisanKeihiMeisai.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		ryohiseisanKeihiMeisai.shiharaisakiName = this.shiharaisakiNameKeihi[i];
		ryohiseisanKeihiMeisai.jigyoushaKbn = super.getDefaultJigyoushaKbnIfEmpty(this.jigyoushaKbnKeihi[i]);
		ryohiseisanKeihiMeisai.bunriKbn = this.bunriKbnKeihi[i];
		ryohiseisanKeihiMeisai.kariShiireKbn = this.kariShiireKbnKeihi[i];
		ryohiseisanKeihiMeisai.kashiShiireKbn = this.kashiShiireKbnKeihi[i];
		return ryohiseisanKeihiMeisai;
	}
}
