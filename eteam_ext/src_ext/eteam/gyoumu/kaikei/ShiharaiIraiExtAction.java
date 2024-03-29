package eteam.gyoumu.kaikei;

import java.sql.Date;
import java.util.List;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_SHUBETSU;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_SHUBETSU;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.database.abstractdao.ShiharaiIraiExtAbstractDao;
import eteam.database.dao.ShiharaiIraiExtDao;
import eteam.database.dto.ShiharaiIrai;
import eteam.database.dto.ShiharaiIraiExt;
import eteam.database.dto.ShiharaiIraiMeisai;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 請求書払いAction
 */
@Getter @Setter @ToString
public class ShiharaiIraiExtAction extends ShiharaiIraiAction {

	/** 支払依頼Dao */
	ShiharaiIraiExtAbstractDao shiharaiIraiExtDao;
	
	/** 振込銀行ID */
	String furikomiGinkouId = "1";
	//＜部品＞
	/** 支払依頼ロジック */
	//ShiharaiIraiLogic shiharaiIraiExtLogic;
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
;		//明細登録
		for (int i = 0; i < shiwakeEdaNo.length; i++) {
			ShiharaiIraiMeisai meisaiDto = this.createMeisaiDto(i);
			this.shiharaiIraiMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);
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
		//▼カスタマイズ　振込先を銀行IDごとに変更
		GMap furisaki = ((DaishoMasterCategoryExtLogic)dMasterLogic).findFurikomisaki(torihikisakiCd,furikomiGinkouId);
		//▲カスタマイズ
		shiharaibi = "";
		shiharaiKijitsu = "";
		if (isNotEmpty(torihikisakiCd)) {
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
		if(!map.isEmpty()) {
			Date kaitenbi = map.get("stymd");
			Date heitenbi = map.get("edymd");
			if(kaitenbi!=null && keijoubiD.before(kaitenbi)) errorList.add("計上日が開店日より前に設定されています。");
			if(heitenbi!=null && keijoubiD.after(heitenbi)) errorList.add("計上日が閉店日より後に設定されています。");
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
}