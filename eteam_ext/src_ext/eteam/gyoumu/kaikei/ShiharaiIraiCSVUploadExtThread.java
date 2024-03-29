package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eteam.base.GMap;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_IRAI_HOUHOU;
import eteam.gyoumu.user.User;

public class ShiharaiIraiCSVUploadExtThread extends ShiharaiIraiCSVUploadThread {
	/**
	 * コンストラクタ
	 * @param session セッションマップ
	 * @param sessionInfo セッション情報
	 * @param schema      スキーマ名
	 */
	public ShiharaiIraiCSVUploadExtThread(Map<String, Object> session, ShiharaiIraiCSVUploadSessionInfo sessionInfo, String schema){
		super(session,sessionInfo,schema);
		
	}

	/**
	 * 支払依頼申請のアクションクラス（ユーザ入力後）の状態を再現。
	 * @param cmnInfo CSV情報
	 * @return アクションクラス
	 */
	protected ShiharaiIraiAction makeDenpyouAction(ShiharaiIraiCSVUploadDenpyouInfo cmnInfo) {
		//支払依頼Actionをインスタンス化
		//▼カスタマイズ クラスをExt化
		ShiharaiIraiAction action = new ShiharaiIraiExtAction();
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
		String nyuryokuHoushikiFlg = "1".equals(invoiceFlg) ? "0" : setting.zeiDefaultA013();
		
		//仕訳枝番号から課税区分仕入区分分離区分を取得する？
		//取引先コードから事業者区分を取得する？
		boolean enableJigyoushaHenkou = "1".equals(setting.jigyoushaHenkouFlgA013());
		
		Date today = new Date(System.currentTimeMillis());
		var kiShouhizeiSettingDto = kiShouhizeiSettingDao.findByDate(today);

		//--------------------
		//伝票共通部をセット
		//--------------------
		action.setDenpyouKbn(DENPYOU_KBN.SIHARAIIRAI);
		action.setLoginUserInfo((User)session.get(Sessionkey.USER));
		action.init();
		action.setKihyouBumonCd(((User)session.get(Sessionkey.USER)).getBumonCd()[0]);
		action.setKihyouBumonName(((User)session.get(Sessionkey.USER)).getBumonName()[0]);
		action.setBumonRoleId(((User)session.get(Sessionkey.USER)).getBumonRoleId()[0]);
		action.setBumonRoleName(((User)session.get(Sessionkey.USER)).getBumonRoleName()[0]);
		Map<String, Object> daihyouFutanBumonNm = masterLogic.findBumonMasterByCd(((User)session.get(Sessionkey.USER)).getDaihyouFutanCd()[0]);
		action.setDaihyouFutanBumonCd(daihyouFutanBumonNm == null ? "" : ((User)session.get(Sessionkey.USER)).getDaihyouFutanCd()[0]);
		action.setEbunsho_tenpufilename(new String[0]);
		action.setEbunsho_tenpufilename_header(new String[0]);
		action.setEbunshoflg(new String[0]);
		action.setDenshitorihikiFlg(new String[0]);
		action.setTsfuyoFlg(new String[0]);
		
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
		String[] shiwakeEdaNo = new String[len];
		String[] torihikiName = new String[len];
		String[] kamokuCd = new String[len];
		String[] kamokuName = new String[len];
		String[] kamokuEdabanCd = new String[len];
		String[] kamokuEdabanName = new String[len];
		String[] futanBumonCd = new String[len];
		String[] futanBumonName = new String[len];
		String[] projectCd = new String[len];
		String[] projectName = new String[len];
		String[] segmentCd = new String[len];
		String[] segmentName = new String[len];
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
		String[] kazeiKbn = new String[len];
		String[] zeiritsu = new String[len];
		String[] keigenZeiritsuKbn = new String[len];
		String[] shiharaiKingaku = new String[len];
		String[] tekiyou = new String[len];
		String[] tekiyouCd = new String[len];
		String[] bunriKbn = new String[len];
		String[] kariShiireKbn = new String[len];
		String[] zeinukiKingaku = new String[len];
		String[] shouhizeigaku = new String[len];
		
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
			if(action.isEmpty(zeiritsu[j])) {
				GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();
				zeiritsu[j] = initZeiritsu.get("zeiritsu").toString();
				keigenZeiritsuKbn[j] = initZeiritsu.get("keigen_zeiritsu_kbn");
			}
			shiharaiKingaku[j] = detailInfo.getShiharaiKingaku();
			tekiyou[j] = detailInfo.getTekiyou();
			tekiyouCd[j] = "";
			GMap shiwakeP = kaikeiCategoryLogic.findShiwakePattern(DENPYOU_KBN.SIHARAIIRAI, Integer.parseInt(shiwakeEdaNo[j]));
			String kingaku = "0".equals(nyuryokuHoushikiFlg) ? detailInfo.getShiharaiKingaku() : detailInfo.getZeinukiKingaku();
			//仕訳パターンから任意税率か確認する
			int zeiritsuForCalc = StringUtils.isEmpty(detailInfo.getZeiritsu()) ? 0 : Integer.parseInt(detailInfo.getZeiritsu());
			if(!ShiwakeConst.ZEIRITSU.equals(shiwakeP.get("kari_zeiritsu"))) {
				zeiritsuForCalc = Integer.parseInt(shiwakeP.get("kari_zeiritsu"));
			}
			GMap mp = kaikeiCommonLogic.calcKingakuAndTaxForIkkatsu(nyuryokuHoushikiFlg,kingaku, zeiritsuForCalc,jigyoushaKbn,kiShouhizeiSettingDto,detailInfo.getShouhizeigaku(),(String)shiwakeP.get("kari_kazei_kbn"));
			shiharaiKingaku		[j] = mp.get("shiharaiKingaku").toString();
			zeinukiKingaku		[j] = mp.get("hontaiKingaku").toString();
			shouhizeigaku		[j] = mp.get("shouhizeigaku").toString();
			//分離区分と仕入区分は課税区分と同じようにAction.javaのreloadShiwakePattern()でセットするのでここでは空白
			bunriKbn			[j] = "";
			kariShiireKbn		[j] = "";
		}
		action.setShiwakeEdaNo(shiwakeEdaNo);
		action.setTorihikiName(torihikiName);
		action.setKamokuCd(kamokuCd);
		action.setKamokuName(kamokuName);
		action.setKamokuEdabanCd(kamokuEdabanCd);
		action.setKamokuEdabanName(kamokuEdabanName);
		action.setFutanBumonCd(futanBumonCd);
		action.setFutanBumonName(futanBumonName);
		action.setProjectCd(projectCd);
		action.setProjectName(projectName);
		action.setSegmentCd(segmentCd);
		action.setSegmentName(segmentName);
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
		action.setKazeiKbn(kazeiKbn);
		action.setZeiritsu(zeiritsu);
		action.setKeigenZeiritsuKbn(keigenZeiritsuKbn);
		action.setShiharaiKingaku(shiharaiKingaku);
		action.setTekiyou(tekiyou);
		action.setTekiyouCd(tekiyouCd);
		action.bunriKbn = bunriKbn;
		action.kariShiireKbn = kariShiireKbn;
		action.zeinukiKingaku = zeinukiKingaku;
		action.shouhizeigaku = shouhizeigaku;
		
		//--------------------
		//申請内容本体部分をセット
		//--------------------
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

		action.setTorihikisakiCd(cmnInfo.getTorihikisakiCd());
		action.setIchigensakiTorihikisakiName(cmnInfo.getIchigensakiTorihikisakiName());
		action.setShiharaiHouhou(SHIHARAI_IRAI_HOUHOU.FURIKOMI);
		action.setShiharaiShubetsu(cmnInfo.getShihiaraiShubetsu());
		action.setKeijoubi(cmnInfo.getKeijoubi());
		action.setYoteibi(cmnInfo.getYoteibi());
		action.setEdi(cmnInfo.getEdi());
		action.setFurikomiGinkouCd(cmnInfo.getFurikomiGinkouCd());
		action.setFurikomiGinkouName("");
		action.setFurikomiGinkouShitenCd(cmnInfo.getFurikomiGinkouShitenCd());
		action.setFurikomiGinkouShitenName("");
		action.setKouzaShubetsu(cmnInfo.getYokinShubetsu());
		action.setKouzaBangou(cmnInfo.getKouzaBangou());
		action.setKouzaMeiginin(cmnInfo.getKouzaMeiginin());
		action.setTesuuryou(cmnInfo.getTesuuryou());
		action.setManekinGensen(cmnInfo.getManekinGensen());
		action.setHosoku("");
		action.setGaibuKoushinUserId(((User)session.get(Sessionkey.USER)).getTourokuOrKoushinUserId());
		action.nyuryokuHoushiki = nyuryokuHoushikiFlg;
		action.jigyoushaKbn = jigyoushaKbn;
		action.jigyoushaNo = cmnInfo.jigyoushaNo;
		//action.invoiceDenpyou = cmnInfo.invoiceDenpyou;
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
		
		//--------------------
		//金額の合計計算
		//--------------------
		double shiharaiGoukei = 0;
		double sousaiGoukei = 0;
		double manekin = action.isNotEmpty(cmnInfo.getManekinGensen()) ? action.toDecimal(cmnInfo.getManekinGensen()).doubleValue() : 0;
		for(var shiharaiMeisaiKingaku : shiharaiKingaku){
			if (action.isNotEmpty(shiharaiMeisaiKingaku)) {
				double shiharai = action.toDecimal(shiharaiMeisaiKingaku).doubleValue();
				if (shiharai >= 0) {
					shiharaiGoukei += shiharai;
				} else {
					sousaiGoukei -= shiharai;
				}
			}
		}
		double kingaku = shiharaiGoukei - sousaiGoukei - manekin;
		action.setShiharaiGoukei(action.formatMoney(new BigDecimal(shiharaiGoukei)));
		action.setSousaiGoukei(action.formatMoney(new BigDecimal(sousaiGoukei)));
		action.setKingaku(action.formatMoney(new BigDecimal(kingaku)));
		
		return action;
	}
}
