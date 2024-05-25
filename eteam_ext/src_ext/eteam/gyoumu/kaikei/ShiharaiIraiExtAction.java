package eteam.gyoumu.kaikei;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_SHUBETSU;
import eteam.common.EteamSettingInfo;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.database.abstractdao.ShiharaiIraiExtAbstractDao;
import eteam.database.dao.ShiharaiIraiExtDao;
import eteam.database.dto.KamokuEdabanZandaka;
import eteam.database.dto.ShiharaiIrai;
import eteam.database.dto.ShiharaiIraiExt;
import eteam.database.dto.ShiharaiIraiMeisai;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 請求書払いAction
 */
@Getter @Setter @ToString
public class ShiharaiIraiExtAction extends ShiharaiIraiAction {

	/** ファイルオブジェクト */
	protected File csvUploadFile;
	/** ファイル名(uploadFileに付随) */
	protected String csvUploadFileFileName;
	/** 支払依頼Dao */
	ShiharaiIraiExtAbstractDao shiharaiIraiExtDao;
	
	/** 振込銀行ID */
	String furikomiGinkouId = "1";
	
	String yosanShikkouTaishou;
	
	/** URL情報 */
	String[] urlinfo;
	
	int urlcnt = 0;
	
	/** csvインポートフラグ */
	boolean csvImportFlag;
	/** セッション */
	ShiharaiIraiCSVUploadSessionInfo sessionInfo;
	/** 支払依頼csvの情報 */
	List<ShiharaiIraiCSVUploadDenpyouInfo> denpyouList;
	//＜部品＞
	/** 支払依頼ロジック */
	//ShiharaiIraiLogic shiharaiIraiExtLogic;
	
	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum  1:登録/更新
	 */
	protected void denpyouHissuCheck(int eventNum) {
		if(csvImportFlag) {
			for (int i = 0; i < shiwakeEdaNo.length; i++) {
			if(isEmpty(shiwakeEdaNo[i])) errorList.add((i+1) +"行目:取引を登録してください。");
			checkHissu(zeinukiKingaku[i], "税抜金額"+(i+1)+"行目");
			checkHissu(shouhizeigaku[i], "消費税額"+(i+1)+"行目");
			}
			if (0 <errorList.size())
			{
				return;
			}
		}
		super.denpyouHissuCheck(eventNum);
		
	}
	
	//＜伝票共通から呼ばれるイベント処理＞
	//個別伝票について表示処理を行う。
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		//参照フラグ(ture:参照起票である、false:参照起票でない)
		boolean sanshou = false;
		
		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (String)usrInfo.get("shain_no");

		//新規起票時の表示状態作成
		if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {
			makeDefaultMeisai();
			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			hf1Cd = ("HF1".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf2Cd = ("HF2".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf3Cd = ("HF3".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf4Cd = ("HF4".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf5Cd = ("HF5".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf6Cd = ("HF6".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf7Cd = ("HF7".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf8Cd = ("HF8".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf9Cd = ("HF9".equals(shainCdRenkeiArea)) ? initShainCd : "";
			hf10Cd = ("HF10".equals(shainCdRenkeiArea)) ? initShainCd : "";

			// 入力方式の初期値
			nyuryokuHoushiki = setting.zeiDefaultA013();
			//インボイス伝票初期値
			invoiceDenpyou = setInvoiceFlgWhenNew();
			
			if(ks.shouhyouShoruiFlg.getHyoujiFlg()) shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA013();
			
		//登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			ShiharaiIrai shinseiData = this.shiharaiIraiDao.find(denpyouId);
			shinseiData2Gamen(shinseiData);
			//▼カスタマイズ　支払依頼Ext分追加
			ShiharaiIraiExt shiharaiExtData = this.shiharaiIraiExtDao.find(denpyouId);
			shinseiExtData2Gamen(shiharaiExtData);
			List<ShiharaiIraiMeisai> meisaiList = this.shiharaiIraiMeisaiDao.loadByDenpyouId(denpyouId);
			meisaiData2Gamen(meisaiList, sanshou, connection);
			

		//参照起票の表示状態作成
		} else {
			sanshou = true;
			ShiharaiIrai shinseiData = this.shiharaiIraiDao.find(super.sanshouDenpyouId);
			shinseiData2Gamen(shinseiData);
			//▼カスタマイズ　支払依頼Ext分追加
			ShiharaiIraiExt shiharaiExtData = this.shiharaiIraiExtDao.find(super.sanshouDenpyouId);
			shinseiExtData2Gamen(shiharaiExtData);
			List<ShiharaiIraiMeisai> meisaiList = this.shiharaiIraiMeisaiDao.loadByDenpyouId(super.sanshouDenpyouId);
			meisaiData2Gamen(meisaiList, sanshou, connection);
			
			// 社員コード連携オンの場合ヘッダーフィールドに社員コードを設定
			if("HF1".equals(shainCdRenkeiArea)){ hf1Cd = initShainCd; }
			if("HF2".equals(shainCdRenkeiArea)){ hf2Cd = initShainCd; }
			if("HF3".equals(shainCdRenkeiArea)){ hf3Cd = initShainCd; }
			if("HF4".equals(shainCdRenkeiArea)){ hf4Cd = initShainCd; }
			if("HF5".equals(shainCdRenkeiArea)){ hf5Cd = initShainCd; }
			if("HF6".equals(shainCdRenkeiArea)){ hf6Cd = initShainCd; }
			if("HF7".equals(shainCdRenkeiArea)){ hf7Cd = initShainCd; }
			if("HF8".equals(shainCdRenkeiArea)){ hf8Cd = initShainCd; }
			if("HF9".equals(shainCdRenkeiArea)){ hf9Cd = initShainCd; }
			if("HF10".equals(shainCdRenkeiArea)){ hf10Cd = initShainCd; }
			
			// 日付はブランク
			keijoubi = "";
			yoteibi = "";
			shiharaibi = "";
			shiharaiKijitsu = "";
		}
		
		//表示制御（プルダウンとかの取得は表示方法によらず同じ）
		displaySeigyo(connection);
	}
	
	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		super.displaySeigyo(connection);
		if(urlinfo != null)urlcnt = urlinfo.length-1;
				}
	//更新ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {
		
		// 初期値が会社設定に依るため、一括登録のThreadクラスでなくここでセットすることにした
		if(csvUploadFlag) shouhyouShoruiFlg = setting.shouhyouShoruiDefaultA013();

		//申請内容登録
		ShiharaiIrai dto = this.createDto();
		this.shiharaiIraiDao.insert(dto, dto.tourokuUserId);
		//▼カスタマイズ　Ext追加分のデータ登録
		ShiharaiIraiExt extDto = this.createExtDto();
		this.shiharaiIraiExtDao.insert(extDto);
		//明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			ShiharaiIraiMeisai meisaiDto = this.createMeisaiDto(i);
			this.shiharaiIraiMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
		}
		//URL情報追加
		for(int i = 0; i< urlinfo.length; i++) {
			((ShiharaiIraiExtLogic)shiharaiIraiLogic).insertURLInfo(this.denpyouId,i+1,urlinfo[i]);
		}
	}

	//更新ボタン押下時に、個別伝票について以下を行う。
	//・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	//・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		//表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		//必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 <errorList.size())
		{
			return;
		}

		// 仕訳パターン情報読込（相関チェック前に必要）
		reloadShiwakePattern(connection);

		//マスター等から名称は引き直す
		reloadMaster(connection);
		if (0 <errorList.size())
		{
			return;
		}
		
		//相関チェック
		soukanCheck();
		if (0 <errorList.size())
		{
			return;
		}

		//申請内容登録
		ShiharaiIrai dto = this.createDto();
		dto.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		this.shiharaiIraiDao.update(dto, dto.koushinUserId);
		//▼カスタマイズ　Ext追加分のデータ登録
		ShiharaiIraiExt extDto = this.createExtDto();
		this.shiharaiIraiExtDao.update(extDto);
		//明細削除
		this.shiharaiIraiMeisaiDao.deleteByDenpyouId(this.denpyouId);

		//明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			ShiharaiIraiMeisai meisaiDto = this.createMeisaiDto(i);
			meisaiDto.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
			this.shiharaiIraiMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);
		}
		//URL情報削除
		((ShiharaiIraiExtLogic)shiharaiIraiLogic).deleteURLInfo(this.denpyouId);
		//URL情報追加
		for(int i = 0; i< urlinfo.length; i++) {
			((ShiharaiIraiExtLogic)shiharaiIraiLogic).insertURLInfo(this.denpyouId,i+1,urlinfo[i]);
		}
	}

	//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		super.initParts(connection);
		this.shiharaiIraiExtDao = EteamContainer.getComponent(ShiharaiIraiExtDao.class, connection);
	}
	/**
	 * 請求書払いテーブルのレコード情報を画面項目に移す
	 * @param shinseiData 請求書払いレコード
	 */
	protected void shinseiData2Gamen(ShiharaiIrai shinseiData) {
		super.shinseiData2Gamen(shinseiData);
		//▼カスタマイズ画面表示のタイミングでCSVフラグ追加
		this.csvUploadFlag = (shinseiData.csvUploadFlg.equals("1"));
		List<GMap> map =((ShiharaiIraiExtLogic)shiharaiIraiLogic).loadURLinfo(shinseiData.denpyouId);
		if(!map.isEmpty()) {
		urlinfo = new String[map.size()];
		for(int i = 0; i<urlinfo.length; i++) {
		urlinfo[i] = map.get(i).get("url");
		}
		urlcnt = urlinfo.length;
		}else {
			urlcnt = 0;
		}
	}
	/**
	 * 請求書払いテーブルのレコード情報を画面項目に移す
	 * @param shinseiData 請求書払いレコード
	 */
	protected void shinseiExtData2Gamen(ShiharaiIraiExt shinseiExtData) {
		this.furikomiGinkouId = (shinseiExtData == null)?"1":shinseiExtData.furikomiGinkouId.toString();
	}
	/**
	 * DB登録前にマスターから名称を取得する。（支払先関連のみ）
	 * @param connection コネクション
	 */
	protected void reloadMasterShiharai(EteamConnection connection) {
		KihyouNaviCategoryLogic kl = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);

		//--------------------
		//振込(定期)の場合、取引先の支払先区分が「1：約定A」、「6：銀行振込」、「7：期日振込」以外だめ
		//--------------------
		if(shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)){
			if(!masterLogic.shiharaiTaishou(torihikisakiCd)){
				errorList.add(kl.findDenpyouKanri(DENPYOU_KBN.SIHARAIIRAI).get("denpyou_shubetsu") + "対象の" + ks.torihikisaki.getName() + "ではありません。");
			}
		}
		
		//--------------------
		//振込の場合振込先をマスターから取得。
		//--------------------
		boolean furikomisakiClear = false;
		
		//定期なら必ず、定期以外（その他か未入力）の場合は銀行コードが未入力なら取得する。定期以外で銀行コードが入力されていたらそれを活かす。
		if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI) || isEmpty(furikomiGinkouCd)) {
			if (isNotEmpty(torihikisakiCd)) {
				//▼カスタマイズ　振込先を銀行IDごとに変更
				GMap furisaki = ((DaishoMasterCategoryExtLogic)dMasterLogic).findFurikomisaki(torihikisakiCd,furikomiGinkouId);
				//▲カスタマイズ
				if (furisaki != null) {
					furikomiGinkouCd = (String)furisaki.get("furikomi_ginkou_cd");
					furikomiGinkouShitenCd = (String)furisaki.get("furikomi_ginkou_shiten_cd");
					kouzaShubetsu = (String)furisaki.get("yokin_shubetsu");
					kouzaBangou = (String)furisaki.get("kouza_bangou");
					kouzaMeiginin = (String)furisaki.get("kouza_meiginin_furigana");
					tesuuryou = furisaki.get("tesuuryou_futan_kbn").equals(2) ? "1" : "0";
				} else {
					if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) {
						errorList.add("振込先がマスターから取得できません。");
						furikomisakiClear = true;
					}
				}
			}
		}
		
		if (furikomisakiClear) {
			furikomiGinkouCd = "";
			furikomiGinkouShitenCd = "";
			kouzaShubetsu = "";
			kouzaBangou = "";
			kouzaMeiginin = "";
			tesuuryou = "";
		}

		//--------------------		
		//銀行・支店の名称
		//--------------------
		if (isNotEmpty(furikomiGinkouCd)) {
			String tmp = dMasterLogic.findKinyuukikanName(furikomiGinkouCd);
			if (isEmpty(tmp)) {
				errorList.add("銀行コードが不正です。");
			}
			furikomiGinkouName = tmp;
		} else {
			furikomiGinkouName = "";
		}
		if (isNotEmpty(furikomiGinkouCd) && isNotEmpty(furikomiGinkouShitenCd)) {
			String tmp = dMasterLogic.findKinyuukikanShitenName(furikomiGinkouCd, furikomiGinkouShitenCd);
			if (isEmpty(tmp)) {
				errorList.add("銀行コード、支店コードが不正です。");
			}
			furikomiGinkouShitenName = tmp;
		} else {
			furikomiGinkouShitenName = "";
		}

		//--------------------
		//支払日、支払期日をマスターから取る
		//支払予定日が未入力なら支払日にする
		//--------------------
		//▼カスタマイズ　経理ロールはリロードしない
		if(!wfSeigyo.isKeiri()) {
		shiharaibi = "";
		shiharaiKijitsu = "";
		if (isNotEmpty(torihikisakiCd)) {
			//▼カスタマイズ　振込先を銀行IDごとに変更
			GMap furisaki = ((DaishoMasterCategoryExtLogic)dMasterLogic).findFurikomisaki(torihikisakiCd,furikomiGinkouId);
			//▲カスタマイズ
			Date shiharaiKijunbi = null;
			try {
				shiharaiKijunbi = toDate(keijoubi);
			} catch(Exception e){
				errorList.add("計上日が不正です。");
				return;
			}
			if (shiharaiKijunbi != null) {
				Date masterShiharaibi = dMasterLogic.judgeShiharaibi(torihikisakiCd, shiharaiKijunbi, 1, shiharaiHouhou, shiharaiShubetsu);
				if (masterShiharaibi != null) {
					shiharaibi = formatDate(masterShiharaibi);
					if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI) && furisaki != null && (furisaki.get("shiharai_shubetsu").toString().equals(SHIHARAI_SHUBETSU.YAKUTEI_A) || furisaki.get("shiharai_shubetsu").toString().equals(SHIHARAI_SHUBETSU.KIJITSU_FURIKOMI))) {
						Date masterShiharaikijitsu = dMasterLogic.judgeShiharaibi(torihikisakiCd, toDate(shiharaibi), 2, shiharaiHouhou, shiharaiShubetsu);
						if (masterShiharaikijitsu != null) {
							shiharaiKijitsu = formatDate(masterShiharaikijitsu);
						} else {
							errorList.add("支払期日がマスターから取得できません。");
						}
					}
				} else {
					if (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI))
						errorList.add("支払日がマスターから取得できません。");
				}
			}
		}
		
		//振込(定期)の場合、支払予定日＝マスター支払日にする→クライアントで変更不可能
		//振込(その他)、自動引落でも、支払予定日未入力で顧客マスター取れていたら支払予定日=マスター支払日にする→クライアントで変更可能
		//▼▼カスタマイズ CSVフラグfalseなら通す
		if (isEmpty(yoteibi) || shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI) && !csvUploadFlag) {
		//▲▲カスタマイズ
			yoteibi = shiharaibi;
		}
	}
		//▲カスタマイズ
	}
	
	/**
	 * 登録時に仕訳パターンマスターより借方貸方の値を設定する。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {
		String daihyouBumonCd = super.daihyouFutanBumonCd;
		
		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String shainCd = (String)usrInfo.get("shain_no");
		ShiwakePatternMaster shiwakeP;
		//掛けは全明細で統一されている前提なので、1つ目の明細でチェック
		//▼カスタマイズ
		if(isNotEmpty(shiwakeEdaNo[0])) {
		 shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[0]));
		}
		//明細行数分の領域確保
		int length = shiwakeEdaNo.length;
		kariTorihikisakiCd = new String[length];

		kashiTorihikisakiCd = new String[length];
		kashiFutanBumonCd = new String[length];
		kashiKamokuCd = new String[length];
		kashiKamokuEdabanCd = new String[length];
		kashiKazeiKbn = new String[length];
// kariUf1Cd = new String[length];
// kariUf2Cd = new String[length];
// kariUf3Cd = new String[length];
// kariUf4Cd = new String[length];
// kariUf5Cd = new String[length];
// kariUf6Cd = new String[length];
// kariUf7Cd = new String[length];
// kariUf8Cd = new String[length];
// kariUf9Cd = new String[length];
// kariUf10Cd = new String[length];

		kakeKashiTorihikisakiCd = new String[length];
		kakeKashiFutanBumonCd = new String[length];
		kakeKashiKamokuCd = new String[length];
		kakeKashiKamokuEdabanCd = new String[length];
		kakeKashiKazeiKbn = new String[length];
		tekiyouCd = new String[length];
		//this.bunriKbn = new String[length];
		//this.kariShiireKbn = new String[length];

		//明細１行ずつ
		for (int i = 0; i < length; i++) {
			//▼カスタマイズ
			if(isEmpty(shiwakeEdaNo[i])) continue;
			shiwakeP = this.shiwakePatternMasterDao.find(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[i]));
			
			if(csvUploadFlag) {
				//取引で貸借どちらかが任意入力でなければ取引の値で上書き
				String[] colNameCore = new String[]{ "project", "segment", "uf" };
				String[] constName = new String[]{ ShiwakeConst.PROJECT, ShiwakeConst.SEGMENT, ShiwakeConst.UF };
				var arrayList = new String[][] { this.projectCd, this.segmentCd, this.uf1Cd, this.uf2Cd, this.uf3Cd, this.uf4Cd, this.uf5Cd, this.uf6Cd, this.uf7Cd, this.uf8Cd, this.uf9Cd, this.uf10Cd };
				for(int j = 0; j < colNameCore.length + 9; j++)
				{
					int index = Math.min(colNameCore.length - 1, j);
					int ufNo = j - 1;
					String ufNoString = ufNo <= 0 ? "" : Integer.toString(ufNo);
					String codeCore = colNameCore[index] + ufNoString;
					GMap map = shiwakeP.map;
					var valueList = List.of( map.get( "kari_" + codeCore +"_cd"), map.get( "kashi_" + codeCore + "_cd1"), map.get( "kashi_" + codeCore + "_cd2"), map.get( "kashi_" + codeCore + "_cd3"), map.get( "kashi_" + codeCore + "_cd4"), map.get( "kashi_" + codeCore + "_cd5") );
					if(!valueList.contains(constName[index]))
					{
						arrayList[j][i] = (String)valueList.get(0);
					}
				}
			}

			//取引名
			torihikiName[i] = shiwakeP.torihikiName;

			//借方--------------------
			//借方　取引先 ※仕訳チェック用、DB登録には関係ない
			kariTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kariTorihikisakiCd) ? torihikisakiCd : shiwakeP.kariTorihikisakiCd;

			//借方　負担部門
			futanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kariFutanBumonCd, daihyouBumonCd);

			//借方　科目
			kamokuCd[i] = shiwakeP.kariKamokuCd;

			//借方　科目枝番
			String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
			switch (pKariKamokuEdabanCd) {
				case EteamConst.ShiwakeConst.EDABAN:
					//何もしない(画面入力のまま)
					break;
				default:
					//固定コード値 or ブランク
					kamokuEdabanCd[i] = pKariKamokuEdabanCd;
					break;
			}
			
			//借方　UF1-10
			if(shainCdRenkeiArea.startsWith("UF") && shainCdRenkeiArea.indexOf("KOTEI") == -1 && shiwakeP.shainCdRenkeiFlg.equals(("1"))){
				int ufno = Integer.valueOf(shainCdRenkeiArea.substring(2));
				if (ufno == 1)
				{
					 uf1Cd[i]  = shainCd;
				}
				if (ufno == 2)
				{
					 uf2Cd[i]  = shainCd;
				}
				if (ufno == 3)
				{
					 uf3Cd[i]  = shainCd;
				}
				if (ufno == 4)
				{
					 uf4Cd[i]  = shainCd;
				}
				if (ufno == 5)
				{
					 uf5Cd[i]  = shainCd;
				}
				if (ufno == 6)
				{
					 uf6Cd[i]  = shainCd;
				}
				if (ufno == 7)
				{
					 uf7Cd[i]  = shainCd;
				}
				if (ufno == 8)
				{
					 uf8Cd[i]  = shainCd;
				}
				if (ufno == 9)
				{
					 uf9Cd[i]  = shainCd;
				}
				if (ufno == 10)
				{
					uf10Cd[i] = shainCd;
				}
			}
			
			// 借方　課税区分 支払い依頼CSVアップロード対応
			if (isEmpty(kazeiKbn[i]))
			{
				if(kamokuEdabanCd[i] == null || kamokuEdabanCd[i].equals("")) {
					kazeiKbn[i] = shiwakeP.kariKazeiKbn;
				}else {
					KamokuEdabanZandaka edaban = edabanZandaka.find(kamokuCd[i], kamokuEdabanCd[i]);
					kazeiKbn[i] = edaban.getKazeiKbn() == null ? shiwakeP.kariKazeiKbn : String.format("%3s", edaban.getKazeiKbn().toString()).replace(" ","0");
				}
			}
			
			// 借方　消費税率
			if (List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn[i])) {
				String[] convZeiritsu = commonLogic.convZeiritsu(zeiritsu[i], keigenZeiritsuKbn[i], shiwakeP.kariZeiritsu, shiwakeP.kariKeigenZeiritsuKbn);
				zeiritsu[i] = convZeiritsu[0];
				keigenZeiritsuKbn[i] = convZeiritsu[1];
			//CSV対応 税込系or税抜系以外の取引仕訳と選択した場合、CSVの内容に関係なく税率・消費税額を「0」セット
			}else if(csvUploadFlag){
				zeiritsu[i] = "0";
				keigenZeiritsuKbn[i] = "0";
				shouhizeigaku[i] = "0";
				//入力方式のデフォルト値に従って税抜金額・支払金額もセットし直し
				if(EteamSettingInfo.getSettingInfo("zei_default_A013").equals("0")) {
					zeinukiKingaku[i] = shiharaiKingaku[i];
				}else {
					shiharaiKingaku[i] = zeinukiKingaku[i];
				}
			}
			// 借方 分離区分・仕入区分　csv一括の場合は仕訳パターンマスタから取得する
			if (csvUploadFlag) {
				bunriKbn[i] = commonLogic.edabanBunriCheck(kamokuCd[i], kamokuEdabanCd[i], shiwakeP.kariBunriKbn, kazeiKbn[i]);
				kariShiireKbn[i] = shiwakeP.kariShiireKbn == null ? "": commonLogic.bumonShiireCheck(kamokuCd[i], futanBumonCd[i], kazeiKbn[i],shiwakeP.kariShiireKbn, this.getShiireZeiAnbun());
			}

			//貸方（未払）--------------------
			String kashiNo = (shiharaiShubetsu.equals(SHIHARAI_IRAI_SHUBETSU.TEIKI)) ? "2" : "3";

			//貸方　取引先 ※仕訳チェック用、DB登録には関係ない
			kashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.map.get("kashi_torihikisaki_cd" + kashiNo)) ? torihikisakiCd : (String)shiwakeP.map.get("kashi_torihikisaki_cd" + kashiNo);

			//貸方　負担部門コード
			kashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], (String)shiwakeP.map.get("kashi_futan_bumon_cd" + kashiNo), daihyouBumonCd);

			//貸方　科目コード
			kashiKamokuCd[i] = (String)shiwakeP.map.get("kashi_kamoku_cd" + kashiNo);

			//貸方　科目枝番コード（ブランク or コード値）
			kashiKamokuEdabanCd[i] = (String)shiwakeP.map.get("kashi_kamoku_edaban_cd" + kashiNo);

			//貸方　課税区分
			kashiKazeiKbn[i] = (String)shiwakeP.map.get("kashi_kazei_kbn" + kashiNo);

			//貸方（支払）--------------------
			//掛け　貸方　取引先
			kakeKashiTorihikisakiCd[i] = ShiwakeConst.TORIHIKI.equals(shiwakeP.kashiTorihikisakiCd1) ? torihikisakiCd : (String)shiwakeP.kashiTorihikisakiCd1;

			//掛け　貸方　負担部門コード
			kakeKashiFutanBumonCd[i] = commonLogic.convFutanBumon(futanBumonCd[i], shiwakeP.kashiFutanBumonCd1, daihyouFutanBumonCd);

			//掛け　貸方　科目コード
			kakeKashiKamokuCd[i] = shiwakeP.kashiKamokuCd1;

			//掛け　貸方　科目枝番コード（ブランク or コード値）
			kakeKashiKamokuEdabanCd[i] = shiwakeP.kashiKamokuEdabanCd1;

			//掛け　貸方　課税区分
			kakeKashiKazeiKbn[i] = shiwakeP.kashiKazeiKbn1;
			

			//社員コードを摘要コードに反映する場合--------------------
			if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
				if(shainCd.length() > 4) {
					tekiyouCd[i] = shainCd.substring(shainCd.length()-4);
				} else {
					tekiyouCd[i] = shainCd;
				}
			} else {
				tekiyouCd[i] = "";
			}
		}
	}

	/**
	 * 必須チェック・形式チェック以外の入力チェック=相関チェック
	 * @param keijoubiTourokuFlg 0:エラーメッセージとして支払日が先、1:エラーメッセージとして計上日が先
	 *								※実態として、個別伝票から呼び出す場合は0、伝票一覧から支払日一括登録の場合は0、伝票一覧から計上日一括登録の場合は1
	 */
	@Override
	protected void soukanCheck(String keijoubiTourokuFlg) {
		super.soukanCheck(keijoubiTourokuFlg);
		//▼負担部門Cd空じゃなかったら（パッケージで必須にしている可能性もあるが念のため）
		if(isNotEmpty(futanBumonCd[0])) {
		Date keijoubiD = Date.valueOf(keijoubi.replaceAll("/", "-"));
		GMap map =((ShiharaiIraiExtLogic)shiharaiIraiLogic).getKaitenbiAndHeitenbi(keijoubiD,futanBumonCd[0]);
		if(map != null && !map.isEmpty()) {
			Date kaitenbi = map.get("stymd");
			Date heitenbi = map.get("edymd");
			if(kaitenbi!=null && keijoubiD.before(kaitenbi)) errorList.add("計上日が開店日より前に設定されています。");
			if(heitenbi!=null && keijoubiD.after(heitenbi)) errorList.add("計上日が閉店日より後に設定されています。");
			}
		}
		//▼URL情報のチェック
		 for(int i=0; i<urlinfo.length; i++) {
			 //複数行あって1行目が空白の時
			 if(urlinfo[0].isEmpty() && urlinfo.length == 1) {
				 continue;
			 }
			 if(urlinfo[0].isEmpty() && urlinfo.length>1) {
				 errorList.add("1行目:URL情報を入力してください");
				 
			 //空白かつ1行目じゃない時
			 }else if(urlinfo[i].isEmpty() && urlinfo[i] != urlinfo[0]){
				 errorList.add(i+1+"行目:URL情報を入力してください");
				 
			 } else if ( !(urlinfo[i].startsWith( "http://" ) || urlinfo[i].startsWith( "https://" ))){
					
				 errorList.add(i+1+"行目:URL情報を正しく入力してください");
			 }
		 }
	}
	
	/**
	 * @return 支払依頼Dto
	 */
	@Override
	protected ShiharaiIrai createDto(){
		ShiharaiIrai shiharaiIrai = super.createDto();
		//▼カスタマイズ登録時にCSVフラグ追加
		shiharaiIrai.csvUploadFlg = (this.csvUploadFlag) ? "1" : "0";
		return shiharaiIrai;
	}
	
	/**
	 * @return 支払依頼ExtDto
	 */
	protected ShiharaiIraiExt createExtDto() {
		ShiharaiIraiExt shiharaiIraiExt = new ShiharaiIraiExt();
		shiharaiIraiExt.denpyouId = this.denpyouId;
		shiharaiIraiExt.furikomiGinkouId = isEmpty(this.furikomiGinkouId) ? 1 : Integer.parseInt(this.furikomiGinkouId);
		return shiharaiIraiExt;
	}
	
	//▼▼以下csv処理
	/**
	 * ファイルのフォーマット、伝票IDの昇順チェックを行います。
	 * 予算執行対象が支出依頼の場合、CSVのデータ列数が1列追加されます。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheck(List<String[]> dataList) {
		int datacolumnNum;
		
		//ShiharaiIraiCSVUploadActionの定数に＋１
		datacolumnNum = 50;
		if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
			datacolumnNum = datacolumnNum + 1;
		}
		
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：データの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}
	
	/**
	 * CSVファイル情報をチェックします。
	 * @param denpyouList CSVファイル情報
	 */
	protected void fileContentCheck(List<ShiharaiIraiCSVUploadDenpyouExtInfo> denpyouList) {
		
		//必須項目チェック
		for (int i = 0; i < denpyouList.size(); i++) {
			ShiharaiIraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
			String ip = "#" + denpyou.getNumber();
			checkHissu(denpyou.getDenpyouNo(), "伝票No."		+ ip);
			checkHissu(denpyou.getTorihikisakiCd(), "取引先コード"	+ ip);
			checkHissu(denpyou.getShihiaraiShubetsu(), "支払種別"		+ ip);
			checkHissu(denpyou.getKeijoubi(), "計上日"		+ ip);
			checkHissu(denpyou.getKianDenpyouId(), "起案伝票ID" 	+ ip);

			List<ShiharaiIraiCSVUploadMeisaiInfo> meisaiList = denpyou.getMeisaiList();
			for (int j = 0; j < meisaiList.size(); j++) {
				ShiharaiIraiCSVUploadMeisaiInfo meisai = meisaiList.get(j);
				ip = "#" + meisai.getNumber();
				checkHissu(meisai.getTekiyou(), "摘要"			+ ip);
				if("0".equals(getNyuryokuHoushiki())) {
					checkHissu(meisai.getShiharaiKingaku(),	"支払金額"+ ip);
				}else {
					checkHissu(meisai.getZeinukiKingaku(), "税抜金額"+ ip);
					checkHissu(meisai.getShouhizeigaku(), "消費税額"+ ip);
				}
				checkHissu(meisai.getShiwakeEdaNo(), "仕訳枝番号"	+ ip);
				boolean isKazei = true;
				if(meisai.getShiwakeEdaNo().matches("^[0-9]{1,10}$")){
					GMap mp = kaikeiLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(meisai.getShiwakeEdaNo()));
					if(mp != null){
						isKazei = List.of("001", "002", "011", "012", "013", "014").contains(mp.get("kari_kazei_kbn")); 
					}
				}
				if(isKazei) {
					checkHissu(meisai.getZeiritsu(), "消費税率"		+ ip);
					checkHissu(meisai.getKeigenZeiritsuKbn(), "軽減税率区分"	+ ip);
				}
			}
			//入力がなかった場合は0とみなすので必須チェックの必要はなくなった
			//ただし仕様変更など可能性あるので残しておく
//			if("1".equals(invoiceStartFlg) && "1".equals(invoiceSetteiFlg)) {
//				checkHissu(denpyou.getInvoiceDenpyou(),"インボイス対応伝票"+ip);
//			}
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < denpyouList.size(); i++) {
			ShiharaiIraiCSVUploadDenpyouExtInfo denpyou = denpyouList.get(i);
			String ip = "#" + denpyou.getNumber();
			checkNumber(denpyou.getDenpyouNo(), 1, 3, "伝票No." + ip, false);
			if(!(denpyou.getKianDenpyouId().equals("0"))) {
				checkString(denpyou.getKianDenpyouId(), 19, 19, "起案伝票ID" + ip , false);
			}
			checkDomain(denpyou.getJigyoushaKbn(), jigyoushaKbnDomain,"事業者区分" + ip, false); //インボイス対応
			checkJigyoushaNo (denpyou.getJigyoushaNo(), "適格請求書発行事業者登録番号" + ip); //インボイス対応
			checkDomain(denpyou.getInvoiceDenpyou(), invoiceDenpyouDomain, "インボイス伝票" + ip , false); //インボイス対応

			List<ShiharaiIraiCSVUploadMeisaiInfo> meisaiList = denpyou.getMeisaiList();
			for (int j = 0; j < meisaiList.size(); j++) {
				ShiharaiIraiCSVUploadMeisaiInfo meisai = meisaiList.get(j);
				ip = "#" + meisai.getNumber();
				checkNumber(meisai.getShiwakeEdaNo(), 1, 10, "仕訳枝番号" + ip, false);
				boolean isKazei = true;
				if(meisai.getShiwakeEdaNo().matches("^[0-9]{1,10}$")){
					GMap mp = kaikeiLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(meisai.getShiwakeEdaNo()));
					if(mp == null){
						errorList.add("仕訳枝番号" + ip + "は存在しません。");
					}else {
						isKazei = List.of("001", "002", "011", "012", "013", "014").contains(mp.get("kari_kazei_kbn")); 
					}
					
				}
				if(isKazei) {
					checkDomain(meisai.getZeiritsu(), zeiritsuDomain, "消費税率" + ip , false);
					checkDomain(meisai.getKeigenZeiritsuKbn(), Domain.FLG, "軽減税率区分" + ip , false);
				}
				
			}
			
		}
		if (0 < errorList.size())
		{
			return;
		}

		//伝票No.の昇順チェック
        int beforeNo = -1; 
        int currentNo = -1;
		for(int i = 0 ; i < denpyouList.size() ; i++){
			ShiharaiIraiCSVUploadDenpyouInfo denpyou = denpyouList.get(i);
			String ip = "#" + denpyou.getNumber();
			currentNo = Integer.parseInt(denpyou.getDenpyouNo());
			if(currentNo < beforeNo){
				errorList.add("伝票No." + ip + "が昇順にソートされていません。");
				return;
			}
			beforeNo = currentNo;
		}
	}
	
	protected void checkURL(String url) {
		if(!(url.startsWith( "http://" ) || url.startsWith( "https://" ))){
			errorList.add("url情報を正しく入力してください。");
		}
	}
	/**
	 * ファイルデータを読み込みます。
	 * @return CSVの１行ずつのテキスト
	 */
	protected List<String[]> fileDataRead() {
		List<String[]> dataList = new ArrayList<>();
		BufferedReader inFile = null;
		try {
			// ファイルデータを読み込みます。
			inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(csvUploadFile)), Encoding.MS932));
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray = line.split(",",-1);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("支払依頼申請CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("支払依頼申請CSVファイルクローズでエラー", e);
				}
			}
		}
		return dataList;
	}
	
	/**
	 * 支払依頼申請画面でcsvをアップロードする処理
	 * @return
	 */
	public String csvUpload() {
		EteamConnection connection = EteamConnection.connect();
		try{
			// ファイルサイズエラーチェック
			EteamCommon.uploadFileSizeCheck();
			super.setConnection(connection);
			//再表示用(親)
			super.initialize();
			super.accessCheck(Event.INIT);
			super.loadData();
			//再表示用(子)
			this.initParts(connection);
			this.displaySeigyo(connection);

			//支払依頼申請の予算執行対象を取得
			GMap denpyouShubetsuMap = getKihyouNaviLogic().findDenpyouKanri(DENPYOU_KBN.SIHARAIIRAI);
			yosanShikkouTaishou = denpyouShubetsuMap.get("yosan_shikkou_taishou").toString();
			invoiceStartFlg = myLogic.judgeinvoiceStart();

			//ファイルをテキストに分解
			List<String[]> dataList = fileDataRead();
			
			//ファイル全体のチェック。列数が規定数であるか、キーとなる伝票No.が昇順であるかのみ。
			fileFormatCheck(dataList);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			
			//CSVファイル情報を作成します。
			List<ShiharaiIraiCSVUploadDenpyouExtInfo> denpyouList = makeCsvFileInfo(dataList);

			//CSVファイル情報をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			//チェックいらない
		//	fileContentCheck(denpyouList);

			//伝票１つずつつくる
			for (ShiharaiIraiCSVUploadDenpyouExtInfo denpyou : denpyouList) {
				makeDenpyouAction(denpyou);
			}
			reloadMasterShiharai(connection);
			setCsvUploadFlag(true);
			reloadShiwakePattern(connection);
			setCsvUploadFlag(false);
			//必要な値をセット
			setCsvDefaultValue(connection);
			// 戻り値を返す
			//仕訳枝番がないための対策
			setCsvImportFlag(true);
			return "success";
		}finally{
			connection.close();
		}
	}
	
	/**
	 * csvアップロード時に必要な値を登録する
	 * @param connection
	 */
	protected void setCsvDefaultValue(EteamConnection connection) {
		//取引先名称セット
		if(isNotEmpty(torihikisakiCd)){
			setTorihikisakiNameRyakushiki(masterLogic.findTorihikisakiName(torihikisakiCd));
		}else {
			setTorihikisakiNameRyakushiki("");
		}
		for(int i =0;i<shiwakeEdaNo.length; i++) {
		kamokuName[i] = masterLogic.findKamokuNameStr(kamokuCd[i]);
		futanBumonName[i] = masterLogic.findFutanBumonName(futanBumonCd[i]);
		kamokuEdabanName[i]= masterLogic.findKamokuEdabanName(kamokuCd[i],kamokuEdabanCd[i]);
		}
		
		
	}
	/**
	 * CSVファイル情報を作成します。
	 * @param dataList CSV１行ずつのテキスト
	 * @return 伝票リスト
	 */
	protected List<ShiharaiIraiCSVUploadDenpyouExtInfo> makeCsvFileInfo(List<String[]> dataList) {
		List<ShiharaiIraiCSVUploadDenpyouExtInfo> denpyouList = new ArrayList<>();
        String beforeNo = null; 
		int currentEdaNo = -1;
		ShiharaiIraiCSVUploadDenpyouExtInfo denpyou = null;
		
		for(int i = 0 ; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			String denpyouNo = rowData[0];
			
				//SIAS版の場合HFにそのままCSVの値を指定
				//伝票No.は昇順前提。同じ伝票No.の先頭行のみを処理→申請本体情報の読込
				//起案伝票IDは予算執行対象が支出依頼でない場合、0を内部的にセットする
				if(! denpyouNo.equals(beforeNo)) {
					//CSVファイル情報（申請内容）の設定
					denpyou = new ShiharaiIraiCSVUploadDenpyouExtInfo();
					denpyouList.add(denpyou);
					denpyou.setNumber (Integer.toString(i + 1));
					denpyou.setDenpyouNo (denpyouNo);
					denpyou.setHf1Cd (rowData[1]);
					denpyou.setHf2Cd (rowData[2]);
					denpyou.setHf3Cd (rowData[3]);
					denpyou.setHf4Cd (rowData[4]);
					denpyou.setHf5Cd (rowData[5]);
					denpyou.setHf6Cd (rowData[6]);
					denpyou.setHf7Cd (rowData[7]);
					denpyou.setHf8Cd (rowData[8]);
					denpyou.setHf9Cd (rowData[9]);
					denpyou.setHf10Cd (rowData[10]);
					denpyou.setTorihikisakiCd (rowData[11]);
					denpyou.setIchigensakiTorihikisakiName (rowData[12]);
					denpyou.setShihiaraiShubetsu (rowData[13]);
					denpyou.setKeijoubi (rowData[14]);
					denpyou.setYoteibi (rowData[15]);
					denpyou.setEdi (rowData[16]);
					denpyou.setFurikomiGinkouCd (rowData[17]);
					denpyou.setFurikomiGinkouShitenCd (rowData[18]);
					denpyou.setYokinShubetsu (rowData[19]);
					denpyou.setKouzaBangou (rowData[20]);
					denpyou.setKouzaMeiginin (rowData[21]);
					denpyou.setTesuuryou (rowData[22]);
					denpyou.setManekinGensen (rowData[23]);
					denpyou.setHasseiShubetsu (rowData[24]); if(isEmpty(denpyou.getHasseiShubetsu())) denpyou.setHasseiShubetsu("経費");
					denpyou.setJigyoushaKbn(rowData[46]);
					denpyou.setJigyoushaNo(rowData[47]);
					denpyou.setInvoiceDenpyou(rowData[48]);
					if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
						denpyou.setKianDenpyouId(rowData[49]);
					}else {
						denpyou.setKianDenpyouId("0");
					}
					denpyou.url = new String[dataList.size()];
					currentEdaNo = 0;
				}
				
				//全行を処理→申請明細情報の読込
				currentEdaNo++;
				ShiharaiIraiCSVUploadMeisaiInfo meisai = new ShiharaiIraiCSVUploadMeisaiInfo();
				denpyou.meisaiList.add(meisai);
				meisai.setNumber(Integer.toString(i + 1));
				meisai.setDenpyouEdaNo(Integer.toString(currentEdaNo));
				meisai.setTekiyou (rowData[25]);
				meisai.setShiharaiKingaku (rowData[26]);
				meisai.setZeiritsu (rowData[27]);
				meisai.setKeigenZeiritsuKbn (rowData[28]);
				meisai.setShiwakeEdaNo (rowData[29]);
				meisai.setKamokuEdabanCd (rowData[30]);
				meisai.setFutanBumonCd (rowData[31]);
				meisai.setUf1Cd (rowData[32]);
				meisai.setUf2Cd (rowData[33]);
				meisai.setUf3Cd (rowData[34]);
				meisai.setUf4Cd (rowData[35]);
				meisai.setUf5Cd (rowData[36]);
				meisai.setUf6Cd (rowData[37]);
				meisai.setUf7Cd (rowData[38]);
				meisai.setUf8Cd (rowData[39]);
				meisai.setUf9Cd (rowData[40]);
				meisai.setUf10Cd (rowData[41]);
				meisai.setProjectCd (rowData[42]);
				meisai.setSegmentCd (rowData[43]);
				meisai.setZeinukiKingaku(rowData[44]);
				meisai.setShouhizeigaku(rowData[45]);
				
				if(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) {
					denpyou.url[i]=rowData[50];
				}else {
					denpyou.url[i]=rowData[49];
				}

			beforeNo = denpyouNo;
		}
		return denpyouList;
	}
	/**
	 * 支払依頼申請のアクションクラス（ユーザ入力後）の状態を再現。
	 * @param cmnInfo CSV情報
	 * @return アクションクラス
	 */
	protected void makeDenpyouAction(ShiharaiIraiCSVUploadDenpyouExtInfo cmnInfo) {
		
		//入力方式からの金額計算必要
		String invoiceFlg = cmnInfo.invoiceDenpyou;
		if("1".equals(invoiceStartFlg)) {
			//インボイス開始後は、会社設定の「インボイス対応伝票 設定項目」を使用しないなら強制的にインボイス伝票
			// 使用する なのにカラム空だったらインボイス伝票とみなす
			if("0".equals(getInvoiceSetteiFlg()) || isEmpty(invoiceFlg)) {
				invoiceFlg = "0";
			}
		}else {
			//インボイス開始前は強制的に旧伝票
			invoiceFlg = "1";
		}
		String nyuryokuHoushikiFlg = "1".equals(invoiceFlg) ? "0" : setting.zeiDefaultA013();
		
		//仕訳枝番号から課税区分仕入区分分離区分を取得する？
		//取引先コードから事業者区分を取得する？
		boolean enableJigyoushaHenkou = "1".equals(setting.jigyoushaHenkouFlgA013());
		
		Date today = new Date(System.currentTimeMillis());
		var kiShouhizeiSettingDto = getKiShouhizeiSettingDao().findByDate(today);

		//--------------------
		//伝票共通部をセット
		//--------------------
		this.setDenpyouKbn(DENPYOU_KBN.SIHARAIIRAI);
		this.setLoginUserInfo((User)session.get(Sessionkey.USER));
	
		this.setKihyouBumonCd(((User)session.get(Sessionkey.USER)).getBumonCd()[0]);
		this.setKihyouBumonName(((User)session.get(Sessionkey.USER)).getBumonName()[0]);
		this.setBumonRoleId(((User)session.get(Sessionkey.USER)).getBumonRoleId()[0]);
		this.setBumonRoleName(((User)session.get(Sessionkey.USER)).getBumonRoleName()[0]);
		Map<String, Object> daihyouFutanBumonNm = masterLogic.findBumonMasterByCd(((User)session.get(Sessionkey.USER)).getDaihyouFutanCd()[0]);
		this.setDaihyouFutanBumonCd(daihyouFutanBumonNm == null ? "" : ((User)session.get(Sessionkey.USER)).getDaihyouFutanCd()[0]);
		this.setEbunsho_tenpufilename(new String[0]);
		this.setEbunsho_tenpufilename_header(new String[0]);
		this.setEbunshoflg(new String[0]);
		this.setDenshitorihikiFlg(new String[0]);
		this.setTsfuyoFlg(new String[0]);
		
		//金額自動計算に必要なので先に事業者区分を出す
		GMap master = masterLogic.findTorihikisakiJouhou(cmnInfo.getTorihikisakiCd(), false);
		//インボイス開始前+旧伝票は強制的に0通常課税　取引先マスタの事業者区分取得、事業者区分変更可能かつcsvに入力があったら上書き
		String jigyoushaKbn = ("1".equals(invoiceFlg) || master == null) ? "0" : (String)master.get("menzei_jigyousha_flg");
		if (enableJigyoushaHenkou && !"".equals(cmnInfo.jigyoushaKbn))
		{
			jigyoushaKbn = cmnInfo.jigyoushaKbn;
		}
		//取引先マスタの適格請求書発行事業者登録番号を取得、csvに入力があったら上書き
		//→20230719確認画面にはそのまま出していい、Actionクラス側の一見先名の置き換えの時に適格事業者番号も合わせて置き換えればいい
//		String jigyoushaNo = master == null ? "" : (String)master.get("tekikaku_no");
//		if (StringUtils.isEmpty(jigyoushaNo))
//		{
//			jigyoushaNo = cmnInfo.jigyoushaNo;
//		}

		//--------------------
		//伝票明細部分をセット
		//仕訳パターンの読込やマスター名称変換による上書登録の項目はブランクにしておいてよい
		//--------------------
		int len = cmnInfo.getMeisaiList().size();
		 shiwakeEdaNo = new String[len];
		 torihikiName = new String[len];
		 kamokuCd = new String[len];
		 kamokuName = new String[len];
		 kamokuEdabanCd = new String[len];
		 kamokuEdabanName = new String[len];
		 futanBumonCd = new String[len];
		 futanBumonName = new String[len];
		 projectCd = new String[len];
		 projectName = new String[len];
		 segmentCd = new String[len];
		 segmentName = new String[len];
		 uf1Cd = new String[len];
		 uf1Name = new String[len];
		 uf2Cd = new String[len];
		 uf2Name = new String[len];
		 uf3Cd = new String[len];
		 uf3Name = new String[len];
		 uf4Cd = new String[len];
		 uf4Name = new String[len];
		 uf5Cd = new String[len];
		 uf5Name = new String[len];
		 uf6Cd = new String[len];
		 uf6Name = new String[len];
		 uf7Cd = new String[len];
		 uf7Name = new String[len];
		 uf8Cd = new String[len];
		 uf8Name = new String[len];
		 uf9Cd = new String[len];
		 uf9Name = new String[len];
		 uf10Cd = new String[len];
		 uf10Name = new String[len];
		 kazeiKbn = new String[len];
		 zeiritsu = new String[len];
		 keigenZeiritsuKbn = new String[len];
		 shiharaiKingaku = new String[len];
		 tekiyou = new String[len];
		 tekiyouCd = new String[len];
		 bunriKbn = new String[len];
		 kariShiireKbn = new String[len];
		 zeinukiKingaku = new String[len];
		 shouhizeigaku = new String[len];
		 urlinfo = new String[len];
		 urlcnt = len-1;
		//CSVファイル情報（申請内容明細）
		for(int j = 0; j < cmnInfo.getMeisaiList().size(); j++){
			ShiharaiIraiCSVUploadMeisaiInfo detailInfo = cmnInfo.getMeisaiList().get(j);
			shiwakeEdaNo[j] = detailInfo.getShiwakeEdaNo();
			torihikiName[j] = "";

			kamokuCd[j] = "";
			kamokuName[j] = "";
			kamokuEdabanCd[j] = detailInfo.getKamokuEdabanCd();
			kamokuEdabanName[j] = "";
			futanBumonCd[j] = detailInfo.getFutanBumonCd();
			futanBumonName[j] = "";
			projectCd[j] = detailInfo.getProjectCd();
			projectName[j] = "";
			segmentCd[j] = detailInfo.getSegmentCd();
			segmentName[j] = "";

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

			kazeiKbn[j] = "";
			zeiritsu[j] = detailInfo.getZeiritsu(); 
			keigenZeiritsuKbn[j] = detailInfo.getKeigenZeiritsuKbn();
			if(isEmpty(zeiritsu[j])) {
				GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();
				zeiritsu[j] = initZeiritsu.get("zeiritsu").toString();
				keigenZeiritsuKbn[j] = initZeiritsu.get("keigen_zeiritsu_kbn");
			}
			shiharaiKingaku[j] = detailInfo.getShiharaiKingaku();
			tekiyou[j] = detailInfo.getTekiyou();
			tekiyouCd[j] = "";
			//仕訳からじゃなければ（取引）
//			if(isNotEmpty(shiwakeEdaNo[j])) {
//			GMap shiwakeP = getKaikeiCategoryLogic().findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[j]));
			String kingaku = "0".equals(nyuryokuHoushikiFlg) ? detailInfo.getShiharaiKingaku() : detailInfo.getZeinukiKingaku();
			//仕訳パターンから任意税率か確認する
//			int zeiritsuForCalc = StringUtils.isEmpty(detailInfo.getZeiritsu()) ? 0 : Integer.parseInt(detailInfo.getZeiritsu());
//			if(!ShiwakeConst.ZEIRITSU.equals(shiwakeP.get("kari_zeiritsu"))) {
//				zeiritsuForCalc = Integer.parseInt(shiwakeP.get("kari_zeiritsu"));
//			}
			//金額からじゃなければ
			if(isNotEmpty(kingaku)){
		//	GMap mp = getKaikeiCommonLogic().calcKingakuAndTaxForIkkatsu(nyuryokuHoushikiFlg,kingaku, zeiritsuForCalc,jigyoushaKbn,kiShouhizeiSettingDto,detailInfo.getShouhizeigaku(),(String)shiwakeP.get("kari_kazei_kbn"));
			shiharaiKingaku		[j] = detailInfo.getShiharaiKingaku();//mp.get("shiharaiKingaku").toString();
			zeinukiKingaku		[j] = detailInfo.getZeinukiKingaku();//mp.get("hontaiKingaku").toString();
			shouhizeigaku		[j] = detailInfo.getShouhizeigaku();//mp.get("shouhizeigaku").toString();
			}//金額からじゃなければ
			
			//分離区分と仕入区分は課税区分と同じようにAction.javaのreloadShiwakePattern()でセットするのでここでは空白
			bunriKbn			[j] = "";
			kariShiireKbn		[j] = "";
		}
		urlinfo = cmnInfo.getUrl();
		this.setShiwakeEdaNo(shiwakeEdaNo);
		this.setTorihikiName(torihikiName);
		this.setKamokuCd(kamokuCd);
		this.setKamokuName(kamokuName);
		this.setKamokuEdabanCd(kamokuEdabanCd);
		this.setKamokuEdabanName(kamokuEdabanName);
		this.setFutanBumonCd(futanBumonCd);
		this.setFutanBumonName(futanBumonName);
		this.setProjectCd(projectCd);
		this.setProjectName(projectName);
		this.setSegmentCd(segmentCd);
		this.setSegmentName(segmentName);
		this.setUf1Cd(uf1Cd);
		this.setUf1Name(uf1Name);
		this.setUf2Cd(uf2Cd);
		this.setUf2Name(uf2Name);
		this.setUf3Cd(uf3Cd);
		this.setUf3Name(uf3Name);
		this.setUf4Cd(uf4Cd);
		this.setUf4Name(uf4Name);
		this.setUf5Cd(uf5Cd);
		this.setUf5Name(uf5Name);
		this.setUf6Cd(uf6Cd);
		this.setUf6Name(uf6Name);
		this.setUf7Cd(uf7Cd);
		this.setUf7Name(uf7Name);
		this.setUf8Cd(uf8Cd);
		this.setUf8Name(uf8Name);
		this.setUf9Cd(uf9Cd);
		this.setUf9Name(uf9Name);
		this.setUf10Cd(uf10Cd);
		this.setUf10Name(uf10Name);
		this.setKazeiKbn(kazeiKbn);
		this.setZeiritsu(zeiritsu);
		this.setKeigenZeiritsuKbn(keigenZeiritsuKbn);
		this.setShiharaiKingaku(shiharaiKingaku);
		this.setTekiyou(tekiyou);
		this.setTekiyouCd(tekiyouCd);
		this.setUrlinfo(urlinfo);
		
		//--------------------
		//申請内容本体部分をセット
		//--------------------
		this.setHf1Cd(cmnInfo.getHf1Cd());
		this.setHf2Cd(cmnInfo.getHf2Cd());
		this.setHf3Cd(cmnInfo.getHf3Cd());
		this.setHf4Cd(cmnInfo.getHf4Cd());
		this.setHf5Cd(cmnInfo.getHf5Cd());
		this.setHf6Cd(cmnInfo.getHf6Cd());
		this.setHf7Cd(cmnInfo.getHf7Cd());
		this.setHf8Cd(cmnInfo.getHf8Cd());
		this.setHf9Cd(cmnInfo.getHf9Cd());
		this.setHf10Cd(cmnInfo.getHf10Cd());

		this.setTorihikisakiCd(cmnInfo.getTorihikisakiCd());
		this.setIchigensakiTorihikisakiName(cmnInfo.getIchigensakiTorihikisakiName());
		this.setShiharaiHouhou(SHIHARAI_IRAI_HOUHOU.FURIKOMI);
		this.setShiharaiShubetsu(cmnInfo.getShihiaraiShubetsu());
		this.setKeijoubi(cmnInfo.getKeijoubi());
		this.setYoteibi(cmnInfo.getYoteibi());
		this.setEdi(cmnInfo.getEdi());
		this.setFurikomiGinkouCd(cmnInfo.getFurikomiGinkouCd());
		this.setFurikomiGinkouName("");
		this.setFurikomiGinkouShitenCd(cmnInfo.getFurikomiGinkouShitenCd());
		this.setFurikomiGinkouShitenName("");
		this.setKouzaShubetsu(cmnInfo.getYokinShubetsu());
		this.setKouzaBangou(cmnInfo.getKouzaBangou());
		this.setKouzaMeiginin(cmnInfo.getKouzaMeiginin());
		this.setTesuuryou(cmnInfo.getTesuuryou());
		this.setManekinGensen(cmnInfo.getManekinGensen());
		this.setHosoku("");
		this.setGaibuKoushinUserId(((User)session.get(Sessionkey.USER)).getTourokuOrKoushinUserId());
		this.nyuryokuHoushiki = nyuryokuHoushikiFlg;
		this.jigyoushaKbn = jigyoushaKbn;
		this.jigyoushaNo = cmnInfo.jigyoushaNo;
		//this.invoiceDenpyou = cmnInfo.invoiceDenpyou;
		this.setInvoiceDenpyou(invoiceFlg);
	
		
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
			this.setKianDenpyouId(KianDenpyouId);
			String KiandenpyouKbnStr = cmnInfo.getKianDenpyouId().substring(7, 11);
			KianDenpyouKbn[0] = KiandenpyouKbnStr;
			this.setKianDenpyouKbn(KianDenpyouKbn);
			this.setKianHimodukeFlg("1");
		}else {
			KianDenpyouId[0] = "";
			this.setKianDenpyouId(KianDenpyouId);
			KianDenpyouKbn[0] = "";
			this.setKianDenpyouKbn(KianDenpyouKbn);
			this.setKianHimodukeFlg("0");
		}
		
		//--------------------
		//金額の合計計算
		//--------------------
		double shiharaiGoukei = 0;
		double sousaiGoukei = 0;
		double manekin = this.isNotEmpty(cmnInfo.getManekinGensen()) ? this.toDecimal(cmnInfo.getManekinGensen()).doubleValue() : 0;
		for(var shiharaiMeisaiKingaku : shiharaiKingaku){
			if (this.isNotEmpty(shiharaiMeisaiKingaku)) {
				double shiharai = this.toDecimal(shiharaiMeisaiKingaku).doubleValue();
				if (shiharai >= 0) {
					shiharaiGoukei += shiharai;
				} else {
					sousaiGoukei -= shiharai;
				}
			}
		}
		double kingaku = shiharaiGoukei - sousaiGoukei - manekin;
		this.setShiharaiGoukei(this.formatMoney(new BigDecimal(shiharaiGoukei)));
		this.setSousaiGoukei(this.formatMoney(new BigDecimal(sousaiGoukei)));
		this.setKingaku(this.formatMoney(new BigDecimal(kingaku)));
			
	}

}
