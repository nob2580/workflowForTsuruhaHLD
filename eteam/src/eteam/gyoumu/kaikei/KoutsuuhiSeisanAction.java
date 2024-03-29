package eteam.gyoumu.kaikei;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamConst.keigenZeiritsuKbn;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.abstractdao.KoutsuuhiseisanAbstractDao;
import eteam.database.abstractdao.KoutsuuhiseisanMeisaiAbstractDao;
import eteam.database.dao.KoutsuuhiseisanDao;
import eteam.database.dao.KoutsuuhiseisanMeisaiDao;
import eteam.database.dto.Koutsuuhiseisan;
import eteam.database.dto.KoutsuuhiseisanMeisai;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.ShiwakeCheckData;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 交通費精算Action
 */
@Getter @Setter @ToString
public class KoutsuuhiSeisanAction extends RyohiKoutsuuhiSeisanCommonAction {

	// ＜定数＞

	// ＜画面入力＞

	// 画面入力（申請内容）→RyohiKoutsuuhiSeisanCommonActionに集約

	// 画面入力（明細）→同上

	// 合計
	/** 内法人カード利用合計 */
	String houjinCardRiyouGoukei;
	/** 会社手配合計 */
	String kaishaTehaiGoukei;
	// 差引
	/** 差引支給金額 */
	String sashihikiShikyuuKingaku;

	// ＜画面入力以外＞→同上

	// ＜部品＞
	/** 交通費精算Dao */
	KoutsuuhiseisanAbstractDao koutsuuhiseisanDao;
	/** 交通費精算明細DAO */
	KoutsuuhiseisanMeisaiAbstractDao koutsuuhiseisanMeisaiDao;

	//＜親子制御＞
	/**
	 * initの処理が進む前に、必要なプロパティの初期化を行う。
	 */
	@Override
	protected void setDefaultKobetsuDenpyouProperties()
	{
		this.hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA010());
		this.keijoubiDefaultSettei = setting.keijoubiDefaultA010();
		this.keijoubiSeigen = setting.koutsuuhiseisanKeijouSeigen().equals("1");
		this.ks = new GamenKoumokuSeigyo(DENPYOU_KBN.KOUTSUUHI_SEISAN);
		this.enableNittou = false;
	}
	
	// ＜入力チェック＞
	/**
	 * 伝票内部項目の形式チェック
	 */
	protected void denpyouFormatCheck() {

		//画面入力（申請内容）
		// 項目 //項目名 //キー項目？
		checkString (mokuteki,1 ,240 , ks.mokuteki.getName(), false);
		checkDomain (shiharaihouhou, shiharaihouhouDomain, ks.shiharaiHouhou.getName(), false);
		checkDate (shiharaiKiboubi, ks.shiharaiKiboubi.getName(), false);
		checkDate (seisankikanFrom, ks.seisankikan.getName() + "開始日", false);
		checkHour (seisankikanFromHour, ks.seisankikanJikoku.getName() + "開始（時）",false);
		checkMin (seisankikanFromMin, ks.seisankikanJikoku.getName() + "開始（分）",false);
		checkDate (seisankikanTo, ks.seisankikan.getName() + "終了日", false);
		checkHour (seisankikanToHour, ks.seisankikanJikoku.getName() + "終了（時）",false);
		checkMin (seisankikanToMin, ks.seisankikanJikoku.getName() + "終了（分）",false);
		checkString (hf1Cd, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf1Name, 1, 20, hfUfSeigyo.getHf1Name(), false);
		checkString (hf2Cd, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf2Name, 1, 20, hfUfSeigyo.getHf2Name(), false);
		checkString (hf3Cd, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString (hf3Name, 1, 20, hfUfSeigyo.getHf3Name(), false);
		checkString (hf4Cd, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString (hf4Name, 1, 20, hfUfSeigyo.getHf4Name(), false);
		checkString (hf5Cd, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString (hf5Name, 1, 20, hfUfSeigyo.getHf5Name(), false);
		checkString (hf6Cd, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString (hf6Name, 1, 20, hfUfSeigyo.getHf6Name(), false);
		checkString (hf7Cd, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString (hf7Name, 1, 20, hfUfSeigyo.getHf7Name(), false);
		checkString (hf8Cd, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString (hf8Name, 1, 20, hfUfSeigyo.getHf8Name(), false);
		checkString (hf9Cd, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString (hf9Name, 1, 20, hfUfSeigyo.getHf9Name(), false);
		checkString (hf10Cd, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkString (hf10Name, 1, 20, hfUfSeigyo.getHf10Name(), false);
		checkDomain (zeiritsuRyohi, zeiritsuRyohiDomain, ks.zeiritsu.getName(), false);
		checkKingakuOver1 (kingaku, ks.goukeiKingaku.getName(), false);
		checkKingakuOver0 (houjinCardRiyouGoukei, ks.uchiHoujinCardRiyouGoukei.getName(), false);
		checkKingakuOver0 (kaishaTehaiGoukei, ks.kaishaTehaiGoukei.getName(), false);
		checkKingakuOver0 (sashihikiShikyuuKingaku, ks.sashihikiShikyuuKingaku.getName(), false);
		checkString (shiwakeEdaNoRyohi,1 ,10, ks.torihiki.getName() + "コード", false);
		checkString (torihikiNameRyohi,1 ,20 , ks.torihiki.getName() + "名", false);
		checkString (kamokuCdRyohi,1 ,6 , ks.kamoku.getName() + "コード", false);
		checkString (kamokuNameRyohi,1 ,22 , ks.kamoku.getName() + "名", false);
		checkString (kamokuEdabanCdRyohi,1 ,12 , ks.kamokuEdaban.getName() + "コード", false);
		checkString (kamokuEdabanNameRyohi,1 ,20 , ks.kamokuEdaban.getName() + "名", false);
		checkString (futanBumonCdRyohi,1 ,8 , ks.futanBumon.getName() + "コード", false);
		checkString (futanBumonNameRyohi,1 ,20 , ks.futanBumon.getName() + "名", false);
		checkString (torihikisakiCdRyohi, 1, 12, ks.torihikisaki.getName() + "コード", false);
		checkString (torihikisakiNameRyohi, 1, 20, ks.torihikisaki.getName() + "名", false);
		checkString (projectCdRyohi, 1, 12, ks.project.getName() + "コード：" , false);
		checkString (projectNameRyohi, 1, 20, ks.project.getName() + "名：" , false);
		checkString (segmentCdRyohi, 1, 8, ks.segment.getName() + "コード：" , false);
		checkString (segmentNameRyohi, 1, 20, ks.segment.getName() + "名：" , false);
		checkString (uf1CdRyohi, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString (uf1NameRyohi, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString (uf2CdRyohi, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString (uf2NameRyohi, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString (uf3CdRyohi, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString (uf3NameRyohi, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString (uf4CdRyohi, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString (uf4NameRyohi, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString (uf5CdRyohi, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString (uf5NameRyohi, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString (uf6CdRyohi, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString (uf6NameRyohi, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString (uf7CdRyohi, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString (uf7NameRyohi, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString (uf8CdRyohi, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString (uf8NameRyohi, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString (uf9CdRyohi, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString (uf9NameRyohi, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString (uf10CdRyohi, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkString (uf10NameRyohi, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkString (tekiyouRyohi,1 ,this.getIntTekiyouMaxLength(), ks.tekiyou.getName(), false);
		checkSJIS (tekiyouRyohi, ks.tekiyou.getName());
		checkDate (shiharaibi, "支払日", false);
		checkDate (keijoubi, "計上日", false);
		checkString (hosoku,1 ,240 , ks.hosoku.getName(), false);
		checkDomain(this.bunriKbn, this.bunriKbnDomain, ks.bunriKbn.getName(), false);
		checkDomain(this.kariShiireKbn, this.shiireKbnDomain, ks.shiireKbn.getName(), false);
		checkDomain(this.invoiceDenpyou, this.invoiceDenpyouDomain, "インボイス対応伝票", false);

		//画面入力（明細）
		for (int i = 0; i < shubetsuCd.length; i++) {
			int ip = i + 1;
			checkString (shubetsuCd[i],1 , 1, "種別コード:" + ip + "行目", false);
			checkString (shubetsu1[i], 1, 20, ks.shubetsu1.getName() + ":" + ip + "行目", false);
			checkString (shubetsu2[i], 1, 20, ks.shubetsu2.getName() + ":" + ip + "行目", false);
			checkDate (kikanFrom[i], ks.kikan.getName() + "開始日:" + ip + "行目", false);
			checkDomain (koutsuuShudan[i], koutsuuShudanDomain, ks.koutsuuShudan.getName() + ":" + ip + "行目", false);
			checkDomain (ryoushuushoSeikyuushoTouFlg[i], Domain.FLG, ks.ryoushuushoSeikyuushoTouFlg.getName() + "フラグ:"+ ip + "行目", false);
			checkString (naiyou[i],1 ,512 , ks.naiyouKoutsuuhi.getName() + ":" + ip + "行目", false);
			checkDomain (oufukuFlg[i], Domain.FLG, ks.oufukuFlg.getName() + "フラグ:" + ip + "行目", false);
			checkDomain (houjinCardFlgRyohi[i], Domain.FLG, ks.houjinCardRiyou.getName() + "フラグ:" + ip + "行目", false);
			checkDomain (kaishaTehaiFlgRyohi[i], Domain.FLG, ks.kaishaTehai.getName() + "フラグ:" + ip + "行目", false);
			checkDomain (jidounyuuryokuFlg[i], Domain.FLG, "自動入力フラグ:" + ip + "行目", false);
			//早安楽
			checkDomain (hayaFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(早フラグ):" + ip + "行目", false);
			checkDomain (yasuFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(安フラグ):" + ip + "行目", false);
			checkDomain (rakuFlg[i], Domain.FLG, ks.hayaYasuRaku.getName() + "(楽フラグ):" + ip + "行目", false);
			//交通費明細の0円入力に対応 仮払有無に関わらず0円を許可
			if(StringUtils.isNotEmpty(suuryouNyuryokuType[i]) && !suuryouNyuryokuType[i].equals("0")) {
				checkKingaku3thDecimalPlaceOver0 (tanka[i], ks.tanka.getName() + ":" + ip + "行目", false);
				checkDomain (suuryouNyuryokuType[i], suuryouNyuryokuTypeDomain, "数量入力タイプ:" + ip + "行目", false);
				checkKingakuDecimal (suuryou[i], "0.01", "999999999999.99", "数量:" + ip + "行目", false);
				checkString (suuryouKigou[i], 1, 5, "数量記号:" + ip + "行目", false);
			}else {
				checkKingakuOver0 (tanka[i], ks.tanka.getName() + ":" + ip + "行目", false);
				checkDomain (suuryouNyuryokuType[i], suuryouNyuryokuTypeDomain, "数量入力タイプ:" + ip + "行目", false);
			}
			checkKingakuOver0 (meisaiKingaku[i], ks.meisaiKingaku.getName() + ":" + ip + "行目", false);
			checkString(this.shiharaisakiNameRyohi[i], 1, 60, ks.shiharaisaki.getName() +  "：" + ip + "行目",false);
			checkDomain(this.jigyoushaKbnRyohi[i], this.jigyoushaKbnDomain, ks.jigyoushaKbn.getName() +  "：" + ip + "行目",false);
			checkKingakuMinus (this.shouhizeigakuRyohi[i], ks.shouhizeigaku.getName()  +  "："  + ip + "行目",false);
		}
	}


	/**
	 * 伝票内部項目の必須チェック
	 * @param eventNum 1:登録/更新、2:支払日更新
	 */
	protected void denpyouHissuCheck(int eventNum) {

		// 画面入力（申請内容）
		String[][] list = {
				{mokuteki, ks.mokuteki.getName(), ks.mokuteki.getHissuFlgS(),"0"},
				{shiharaihouhou, ks.shiharaiHouhou.getName(), ks.shiharaiHouhou.getHissuFlgS(),"0"},
				{shiharaiKiboubi, ks.shiharaiKiboubi.getName(), ks.shiharaiKiboubi.getHissuFlgS(),"0"},
				{seisankikanFrom, ks.seisankikan.getName() + "開始日", ks.seisankikan.getHissuFlgS(),"0"},
				{seisankikanFromHour, ks.seisankikanJikoku.getName() + "開始（時）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
				{seisankikanFromMin, ks.seisankikanJikoku.getName() + "開始（分）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
				{seisankikanTo, ks.seisankikan.getName() + "終了日", ks.seisankikan.getHissuFlgS(),"0"},
				{seisankikanToHour, ks.seisankikanJikoku.getName() + "終了（時）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
				{seisankikanToMin, ks.seisankikanJikoku.getName() + "終了（分）",ks.seisankikanJikoku.getHissuFlgS(),"0"},
				{zeiritsuRyohi, ks.zeiritsu.getName(), List.of("001", "002", "011", "012", "013", "014").contains(this.kazeiKbnRyohi) ? ks.zeiritsu.getHissuFlgS() : "0","0"},
				{kingaku, ks.goukeiKingaku.getName(), ks.goukeiKingaku.getHissuFlgS(),"0"},
				{houjinCardRiyouGoukei, ks.uchiHoujinCardRiyouGoukei.getName(), ks.uchiHoujinCardRiyouGoukei.getHissuFlgS(),"0"},
				{kaishaTehaiGoukei, ks.kaishaTehaiGoukei.getName(), ks.kaishaTehaiGoukei.getHissuFlgS(),"0"},
				{sashihikiShikyuuKingaku, ks.sashihikiShikyuuKingaku.getName(), ks.sashihikiShikyuuKingaku.getHissuFlgS(), "0" },
				{shiwakeEdaNoRyohi, ks.torihiki.getName() + "コード", ks.torihiki.getHissuFlgS(),"0"},
				{torihikiNameRyohi, ks.torihiki.getName() + "名", ks.torihiki.getHissuFlgS(),"0"},
				{kamokuCdRyohi, ks.kamoku.getName() + "コード", ks.kamoku.getHissuFlgS(),"0"},
				{kamokuNameRyohi, ks.kamoku.getName() + "名", ks.kamoku.getHissuFlgS(),"0"},
				{kamokuEdabanCdRyohi, ks.kamokuEdaban.getName() + "コード", ks.kamokuEdaban.getHissuFlgS(),"0"},
				{kamokuEdabanNameRyohi, ks.kamokuEdaban.getName() + "名" , ks.kamokuEdaban.getHissuFlgS(),"0"},
				{futanBumonCdRyohi, ks.futanBumon.getName() + "コード", ks.futanBumon.getHissuFlgS(),"0"},
				{futanBumonNameRyohi, ks.futanBumon.getName() + "名", ks.futanBumon.getHissuFlgS(),"0"},
				{torihikisakiCdRyohi, ks.torihikisaki.getName()+ "コード：", ks.torihikisaki.getHissuFlgS(), "0"},
				{torihikisakiNameRyohi, ks.torihikisaki.getName()+ "名：" , ks.torihikisaki.getHissuFlgS(), "0"},
				{projectCdRyohi, ks.project.getName() + "コード：" , ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
				{projectNameRyohi, ks.project.getName() + "名：" , ( !"0".equals(pjShiyouFlg) && ks.project.getHyoujiFlg() ) ? ks.project.getHissuFlgS() : "0", "0"},
				{segmentCdRyohi, ks.segment.getName() + "コード：" , ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
				{segmentNameRyohi, ks.segment.getName() + "名：" , ( !"0".equals(segmentShiyouFlg) && ks.segment.getHyoujiFlg() ) ? ks.segment.getHissuFlgS() : "0", "0"},
				{tekiyouRyohi, ks.tekiyou.getName(), ks.tekiyou.getHissuFlgS(),"0"},
				{shiharaibi, "支払日", "0","1"},
				{keijoubi, "計上日", keijouBiMode == 1 || keijouBiMode == 3 ? "1" : "0", hasseiShugi ? "1" : "0"},
				{hosoku, ks.hosoku.getName(), ks.hosoku.getHissuFlgS(), "0"},
				{this.bunriKbn, ks.bunriKbn.getName(), ks.bunriKbn.getHissuFlgS(), "0"},
				{this.kariShiireKbn, ks.shiireKbn.getName(), ks.shiireKbn.getHissuFlgS(), "0"},
				{this.invoiceDenpyou, "インボイス対応伝票", "1", "0"},
		};
		hissuCheckCommon(list, eventNum);

		if ( !hfUfSeigyo.getHf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf1Cd, hfUfSeigyo.getHf1Name() + "コード" , ks.hf1.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf2Cd, hfUfSeigyo.getHf2Name() + "コード" , ks.hf2.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf3Cd, hfUfSeigyo.getHf3Name() + "コード" , ks.hf3.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf4Cd, hfUfSeigyo.getHf4Name() + "コード" , ks.hf4.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf5Cd, hfUfSeigyo.getHf5Name() + "コード" , ks.hf5.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf6Cd, hfUfSeigyo.getHf6Name() + "コード" , ks.hf6.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf7Cd, hfUfSeigyo.getHf7Name() + "コード" , ks.hf7.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf8Cd, hfUfSeigyo.getHf8Name() + "コード" , ks.hf8.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf9Cd, hfUfSeigyo.getHf9Name() + "コード" , ks.hf9.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getHf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{hf10Cd, hfUfSeigyo.getHf10Name() + "コード" , ks.hf10.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf1CdRyohi, hfUfSeigyo.getUf1Name() + "コード" , ks.uf1.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf2CdRyohi, hfUfSeigyo.getUf2Name() + "コード" , ks.uf2.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf3CdRyohi, hfUfSeigyo.getUf3Name() + "コード" , ks.uf3.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf4CdRyohi, hfUfSeigyo.getUf4Name() + "コード" , ks.uf4.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf5CdRyohi, hfUfSeigyo.getUf5Name() + "コード" , ks.uf5.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf6CdRyohi, hfUfSeigyo.getUf6Name() + "コード" , ks.uf6.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf7CdRyohi, hfUfSeigyo.getUf7Name() + "コード" , ks.uf7.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf8CdRyohi, hfUfSeigyo.getUf8Name() + "コード" , ks.uf8.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf9CdRyohi, hfUfSeigyo.getUf9Name() + "コード" , ks.uf9.getHissuFlgS(), "0"}}, eventNum);
		if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) hissuCheckCommon(new String[][] {{uf10CdRyohi, hfUfSeigyo.getUf10Name() + "コード" , ks.uf10.getHissuFlgS(), "0"}}, eventNum);

		for (int i = 0; i < shubetsuCd.length; i++) {
			int ip = i + 1;
			list = new String[][] {
				//項目							項目名 必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shubetsuCd[i], "種別コード:" + ip + "行目", "1","0"},
				{shubetsu1[i], ks.shubetsu1.getName() + ":" + ip + "行目", ks.shubetsu1.getHissuFlgS(),"0"},
				{kikanFrom[i], ks.kikan.getName() +"開始日:" + ip + "行目", ks.kikan.getHissuFlgS(),"0"},
				{ryoushuushoSeikyuushoTouFlg[i],ks.ryoushuushoSeikyuushoTouFlg.getName() + ":" + ip + "行目", ks.ryoushuushoSeikyuushoTouFlg.getHissuFlgS(),"0"},
				{naiyou[i], ks.naiyouKoutsuuhi.getName() + ":" + ip + "行目", ks.naiyouKoutsuuhi.getHissuFlgS(),"0"},
				{bikou[i], ks.bikouKoutsuuhi.getName() + ":" + ip + "行目", ks.bikouKoutsuuhi.getHissuFlgS(),"0"},
				{tanka[i], ks.tanka.getName() + ":" + ip + "行目", ks.tanka.getHissuFlgS(),"0"},
				{meisaiKingaku[i], ks.meisaiKingaku.getName() + ":" + ip + "行目", ks.meisaiKingaku.getHissuFlgS(),"0"},
				{koutsuuShudan[i], ks.koutsuuShudan.getName() + ":" + ip + "行目", ks.koutsuuShudan.getHissuFlgS(),"0"},
				{jidounyuuryokuFlg[i], "自動入力フラグ:" + ip + "行目", "1","0"},
				{suuryouNyuryokuType[i], "数量入力タイプ:" + ip + "行目", "1","0"},
				{hayaFlg[i], ks.hayaYasuRaku.getName() + "(早フラグ):" + ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
				{yasuFlg[i], ks.hayaYasuRaku.getName() + "(安フラグ):" + ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
				{rakuFlg[i], ks.hayaYasuRaku.getName() + "(楽フラグ):" + ip + "行目", ks.hayaYasuRaku.getHissuFlgS(),"0"},
			};
			hissuCheckCommon(list, eventNum);
			
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

			if(StringUtils.isNotEmpty(suuryouNyuryokuType[i]) && !suuryouNyuryokuType[i].equals("0")) {
				list = new String[][]{
					{suuryou[i], "数量:" + ip + "行目", "1","0"},
					{suuryouKigou[i], "数量記号:" + ip + "行目", "1","0"},
				};
			}else {
				list = new String[][]{
					{oufukuFlg[i], ks.oufukuFlg.getName() + ":" + ip + "行目", ks.oufukuFlg.getHissuFlgS(),"0"},
					{houjinCardFlgRyohi[i], ks.houjinCardRiyou.getName() + ":" + ip + "行目", ks.houjinCardRiyou.getHissuFlgS(),"0"},
					{kaishaTehaiFlgRyohi[i], ks.kaishaTehai.getName() + ":" + ip + "行目", ks.kaishaTehai.getHissuFlgS(),"0"},
				};
			}
			hissuCheckCommon(list, eventNum);
		}
	}

	/**
	 * 業務関連の関連チェック処理
	 * @param userId ユーザーID
	 */
	protected void soukanCheck(String userId) {

		// 精算期間開始日と精算期間終了日の整合性チェック
		for(Map<String, String> errMap: EteamCommon.kigenCheckWithJikoku(seisankikanFrom, seisankikanFromHour, seisankikanFromMin, seisankikanTo, seisankikanToHour, seisankikanToMin)) {
			// 開始日と終了日の整合性チェック、分だけ入力されている場合入力エラーとする。
			if("2".equals(errMap.get("errorCode")) || "3".equals(errMap.get("errorCode")) || "4".equals(errMap.get("errorCode"))){
				errorList.add(ks.seisankikan.getName() + errMap.get("errorMassage"));
			}
		}

		// 社員口座チェック
		commonLogic.checkShainKouza(userId, shiharaihouhou, errorList);

		// 消費税率チェック
		commonLogic.checkZeiritsu(toDecimal(zeiritsuRyohi), keigenZeiritsuKbn.NORMAL, toDate(seisankikanFrom), errorList); // 精算期間開始日
		commonLogic.checkZeiritsu(toDecimal(zeiritsuRyohi), keigenZeiritsuKbn.NORMAL, toDate(seisankikanTo), errorList); // 精算期間終了日

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

			//仕訳パターン
			ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(super.denpyouKbn, Integer.parseInt(shiwakeEdaNoRyohi));

			// 借方
			checkShiwakeKashi(0, shiwakePattern, this.denpyouKbn, super.daihyouFutanBumonCd);
			
			// 貸方
			// 貸方1：振込、貸方2：現金
			checkShiwakeKashi(shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI) ? 1 : 2, shiwakePattern, this.denpyouKbn, super.daihyouFutanBumonCd);

			// 貸方3
			if (hasseiShugi) {
				checkShiwakeKashi(3, shiwakePattern, this.denpyouKbn, super.daihyouFutanBumonCd);
			}

			boolean houjinCardFlg = false;
			boolean kaishaTehaiFlg = false;
			for (int i = 0; i < houjinCardFlgRyohi.length; i++) {
				if (houjinCardFlgRyohi[i].equals("1"))
				{
					houjinCardFlg = true;
				}
				if (kaishaTehaiFlgRyohi[i].equals("1"))
				{
					kaishaTehaiFlg = true;
				}
			}

			// 貸方4
			if (houjinCardFlg) {
				checkShiwakeKashi(4, shiwakePattern, this.denpyouKbn, super.daihyouFutanBumonCd);
			}

			// 貸方5
			if (kaishaTehaiFlg) {
				checkShiwakeKashi(5, shiwakePattern, this.denpyouKbn, super.daihyouFutanBumonCd);
			}

			// 貸借仕訳に取引先が反映されないことがありえるので、単体のマスターチェックをしておく
			if (isNotEmpty(torihikisakiCdRyohi)) {
				ShiwakeCheckData torihikisaki = commonLogic.new ShiwakeCheckData();
				torihikisaki.torihikisakiNm = ks.torihikisaki.getName() + "コード";
				torihikisaki.torihikisakiCd =torihikisakiCdRyohi;
				commonLogic.checkTorihikisaki(torihikisaki, errorList);

				// 取引先仕入先チェック
				super.checkShiiresaki(ks.torihikisaki.getName() + "コード", torihikisakiCdRyohi, denpyouKbn);
			}
		}

		// 明細部
		for (int i = 0; i < kikanFrom.length; i++) {
			int ip = i + 1;

			//法人カード履歴使用明細のチェック(旅費交通費)
			if(!isEmpty(himodukeCardMeisaiRyohi[i])){
				int chkCd = hcLogic.checkHimodukeUsed(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
				switch(chkCd) {
					case -1:
						errorList.add(ip + "行目：指定した法人カード履歴は除外済です。");
						break;
					case -2:
						errorList.add(ip + "行目：指定した法人カード履歴は別伝票に紐付済みです。");
						break;
					case -99:
						errorList.add(ip + "行目：指定した法人カード履歴データが存在しません。");
						break;
				}
			}

			// 精算期間開始日 ≦ 期間開始日 ≦ 精算期間終了日 でなければエラー
			if(kikanFrom[i].compareTo(seisankikanFrom) < 0 || seisankikanTo.compareTo(kikanFrom[i]) < 0) {
				errorList.add(ip + "行目：" + ks.kikan.getName() + "開始日は" + ks.seisankikan.getName() + "内の日付を入力してください。");
			}
		}
		for (int i = 0; i < ryoushuushoSeikyuushoTouFlg.length; i++){
			/* comment out ▼ shouhyouShoruiHissuFlgは明細テーブルに持たせるようにしたのでマスターからのリロードなくす
			//証憑書類必須フラグを再取得する
			shouhyouShoruiHissuFlg[i] = masterLogic.findShouhyouShoruiHissuFlg(koutsuuShudan[i]);
			comment out ▲ */
			int ip = i + 1;
			// 領収書・請求書等チェックが必須（会社設定）かつ証憑書類必須フラグが1の場合
			if(EteamCommon.funcControlJudgment(sysLogic, KINOU_SEIGYO_CD.RYOUSYUUSYO_SEIKYUUSHO_TOU_CHK_HISSU) && shouhyouShoruiHissuFlg[i].equals("1")){
				// 領収書・請求書等が未チェックであればエラー
				if(ryoushuushoSeikyuushoTouFlg[i].equals("0")){
					errorList.add(ip + "行目：" + ks.ryoushuushoSeikyuushoTouFlg.getName() + "をチェックしてください。");
				}
			}
		}

		//計上日のチェック
		if ((getWfSeigyo().isTouroku() || getWfSeigyo().isShinsei()) && keijoubiSeigen && isNotEmpty(keijoubi)) {
			Date shimebi = this.keijoubiShimebiDao.findMaxShimebiForDenpyouKbn(denpyouKbn);
			if (shimebi != null && ! toDate(keijoubi).after(shimebi)) {
				errorList.add("計上日には締日(" + formatDate(shimebi) + ")より後を入力してください。");
			}

		}
	}

	// ＜伝票共通から呼ばれるイベント処理＞
	// 個別伝票について表示処理を行う。
	@Override
	protected void displayKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);
		// 参照フラグ(ture:参照起票である、false:参照起票でない)
		boolean sanshou = false;

		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String initShainCd = (usrInfo == null) ? "" : (String) usrInfo.get("shain_no");

		// 新規起票時の表示状態作成
		if (isEmpty(super.denpyouId) && isEmpty(super.sanshouDenpyouId)) {
			makeDefaultShinsei(connection);
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

			// 申請と取引が1:1で決まる場合
			List<ShiwakePatternMaster> list = this.shiwakePatternMasterDao.loadByDenpyouKbn(DENPYOU_KBN.KOUTSUUHI_SEISAN ,this.futanBumonCdRyohi);
			if (!list.isEmpty()) {
				if (list.size() == 1) {
					shiwakeEdaNoRyohi = Integer.toString(list.get(0).shiwakeEdano);
					torihikiNameRyohi = list.get(0).torihikiName;
					tekiyouRyohi = torihikiNameRyohi;
					if (list.get(0).tekiyouFlg.equals("1")) {
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
					// 仕訳パターン情報読込（相関チェック前に必要）
					reloadShiwakePattern(connection);
					// マスター等から名称は引き直す
					reloadMaster(connection);
					shiharaihouhou = null;
				}
			}
			invoiceDenpyou = setInvoiceFlgWhenNew();

		// 登録済伝票の表示状態作成
		} else if (isNotEmpty(super.denpyouId)) {
			// 申請部
			GMap shinseiData = this.koutsuuhiseisanDao.find(denpyouId).map;
			shinseiData2Gamen(shinseiData, sanshou, connection);
			// 明細部
			List<KoutsuuhiseisanMeisai> meisaiList = this.koutsuuhiseisanMeisaiDao.loadByDenpyouId(denpyouId);
			meisaiData2Gamen(meisaiList);

		// 参照起票の表示状態作成
		} else {
			sanshou = true;
			// 申請部
			GMap shinseiData = this.koutsuuhiseisanDao.find(sanshouDenpyouId).map;
			shinseiData2Gamen(shinseiData, sanshou, connection);
			// 明細部
			List<KoutsuuhiseisanMeisai> meisaiList = this.koutsuuhiseisanMeisaiDao.loadByDenpyouIdNOIC(sanshouDenpyouId);
			if(getWfSeigyo().isOthersSanshouKihyou()) {
				commonLogic.adjustmentTashaCopy(null, getUser(), denpyouKbn, meisaiList.stream().map(KoutsuuhiseisanMeisai::getMap).collect(Collectors.toList()));
			}

			if(!meisaiList.isEmpty()){
				meisaiData2Gamen(meisaiList);
			}else{
				makeDefaultMeisai();
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
			seisankikanFrom = "";
			seisankikanTo = "";
			shiharaiKiboubi = "";
			for (int i = 0; i < kikanFrom.length; i++) {
				kikanFrom[i] = "";
				kikanTo[i] = "";
			}
			keijoubi = "";
			shiharaibi = "";
		}
		
		//課税区分の制御
		if (isNotEmpty(kamokuCdRyohi)) {
			var kamoku = this.kamokuMasterDao.find(this.kamokuCdRyohi);
			this.shoriGroupRyohi = kamoku == null ? null : kamoku.shoriGroup;
		}

		// 表示制御（プルダウンとかの取得は表示方法によらず同じ）
		displaySeigyo(connection);
	}

	// 登録ボタン押下時に、個別伝票について入力チェックを行う：入力エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void tourokuCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		// 必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);
		if (0 <errorList.size())
		{
			return;
		}

		// 各種コード設定と差引支給金額の計算（相関チェック前に必要）
		reloadShiwakePattern(connection);

		// マスター等から名称は引き直す
		reloadMaster(connection);

		// 相関チェック
		soukanCheck(super.getKihyouUserId());
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
	}

	// 登録ボタン押下時に、個別伝票について登録処理を行う。
	@Override
	protected void tourokuKobetsuDenpyou(EteamConnection connection) {

		// 申請内容登録
		Koutsuuhiseisan dto = this.createDto();
		this.koutsuuhiseisanDao.insert(dto, dto.tourokuUserId);
		
		// 明細登録
		for (int i = 0; i < shubetsuCd.length; i++) {
			KoutsuuhiseisanMeisai meisaiDto = this.createMeisaiDto(i);
			koutsuuhiseisanMeisaiDao.insert(meisaiDto, meisaiDto.tourokuUserId);

			if(!isEmpty(himodukeCardMeisaiRyohi[i])){
				hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
			}
		}
	}

	// 更新ボタン押下時に、個別伝票について以下を行う。
	// ・入力チェック：入力エラーがあれば、errorListにエラーを詰める。
	// ・更新処理
	@Override
	protected void koushinKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 表示制御（ドメインチェック、エラー発生時用）
		displaySeigyo(connection);

		// 必須チェック・形式チェック
		denpyouFormatCheck();
		denpyouHissuCheck(1);

		// 各種コード設定と差引支給金額の計算（相関チェック前に必要）
		if (0 <errorList.size())
		{
			return;
		}

		//支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId,loginUserInfo)) {
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

		// マスター等から名称は引き直す
		reloadMaster(connection);

		// 相関チェック
		soukanCheck(super.getKihyouUserId());
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

		// 申請内容更新
		Koutsuuhiseisan dto = this.createDto();
		this.koutsuuhiseisanDao.update(dto, dto.koushinUserId);

		// 明細削除＆登録
		koutsuuhiseisanMeisaiDao.deleteByDenpyouId(denpyouId);
		hcLogic.removeDenpyouHimozuke(denpyouId);

		for (int i = 0; i < shubetsuCd.length; i++) {
			KoutsuuhiseisanMeisai meisaiDto = this.createMeisaiDto(i);
			koutsuuhiseisanMeisaiDao.insert(meisaiDto, meisaiDto.koushinUserId);

			if(!isEmpty(himodukeCardMeisaiRyohi[i])){
				hcLogic.updateDenpyouHimozuke(Integer.parseInt(himodukeCardMeisaiRyohi[i]),denpyouId);
			}
		}
	}

	// 承認ボタン押下時に、個別伝票について以下を行う。
	// ・承認チェック：エラーがあれば、errorListにエラーを詰める。
	@Override
	protected void shouninCheckKobetsuDenpyou(EteamConnection connection) {
		initParts(connection);

		// 再表示用
		displaySeigyo(connection);

		// 支払日チェック
		if (commonLogic.isKeiriOrLastShounin(denpyouId, loginUserInfo)) {
			// 承認時の支払日チェックは最終承認時に値なしのままにならないように、DBの値でチェック
			var data = this.koutsuuhiseisanDao.find(denpyouId);
			String shiharai = formatDate(data.shiharaibi);
			String keijou = hasseiShugi ? formatDate(data.keijoubi) : formatDate(data.seisankikanTo);
			String keijouName = hasseiShugi ? "計上日" : ks.seisankikan.getName() + "終了日";
			if(hasseiShugi && isEmpty(keijou)) errorList.add(keijouName + "が未登録です。" + keijouName +"を登録してください。");
			checkShiharaiBi(shiharai, keijou, keijouName);
			// エラーの場合の表示用に現実の値を設定
			shiharaibi = shiharai;
			if (hasseiShugi)
			{
				keijoubi = keijou;
			} else seisankikanTo = keijou;
		}
	}

	//登録・更新時に稟議金額残高から画面入力した申請金額を減算する。
	@Override
	protected void calRingiKingakuZandakaKobetsuDenpyou(GMap ringiData){
		BigDecimal zandaka = (BigDecimal)ringiData.get("ringiKingakuZandaka");
		ringiData.put("ringiKingakuZandaka", zandaka.subtract(toDecimal(kingaku)));
	}

	/**
	 * 支払日更新イベント
	 * @return 処理結果
	 */
	public String shiharaibiKoushin() {
		// 1.入力チェック
		// 親のパラメータなければエラー
		super.formatCheck();
		super.hissuCheck(3);

		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);

			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし

			// 4.処理
			// 伝票共通の再表示データ取得
			reDisplayDataForKobetsuDenpyou(connection);

			// 表示制御（エラー発生時用）
			displaySeigyo(connection);

			// 個別伝票の入力チェック
			denpyouFormatCheck();
			denpyouHissuCheck(2);
			if (errorList.size() > 0) {
				return "error";
			}

			// 支払日チェック
			checkShiharaiBi(shiharaibi, hasseiShugi ? keijoubi : seisankikanTo, hasseiShugi ? "計上日" : ks.seisankikan.getName() + "終了日");
			if (0 <errorList.size())
			{
				return "error";
			}

			// エラーがなかった場合、支払日の更新
			this.koutsuuhiseisanDao.updateShiharaibi(denpyouId, toDate(shiharaibi), loginUserId);
			commonLogic.insertShiharaiBiTourokuRireki(denpyouId, loginUserInfo, shiharaibi);
			connection.commit();

			// 5.戻り値を返す
			return "success";
		}
	}

	//代表仕訳枝番号を設定
	@Override
	protected String getDaihyouTorihiki(){
		return shiwakeEdaNoRyohi;
	}
	// ＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	@Override
	protected void initParts(EteamConnection connection) {
		super.initParts(connection);
		koutsuuhiseisanDao = EteamContainer.getComponent(KoutsuuhiseisanDao.class, connection);
		koutsuuhiseisanMeisaiDao = EteamContainer.getComponent(KoutsuuhiseisanMeisaiDao.class, connection);
	}

	/**
	 * 交通費精算テーブルのレコード情報を画面項目に移す
	 * @param shinseiData 交通費精算レコード
	 * @param sanshou 参照フラグ(ture:参照起票である、false:参照起票でない)
	 * @param connection コネクション
	 */
	protected void shinseiData2Gamen(GMap shinseiData, boolean sanshou, EteamConnection connection) {
		mokuteki = (String)shinseiData.get("mokuteki");
		shiharaihouhou = (String)shinseiData.get("shiharaihouhou");
		shiharaiKiboubi = formatDate(shinseiData.get("shiharaikiboubi"));
		seisankikanFrom = formatDate(shinseiData.get("seisankikan_from"));
		seisankikanFromHour = (String)shinseiData.get("seisankikan_from_hour");
		seisankikanFromMin = (String)shinseiData.get("seisankikan_from_min");
		seisankikanTo = formatDate(shinseiData.get("seisankikan_to"));
		seisankikanToHour = (String)shinseiData.get("seisankikan_to_hour");
		seisankikanToMin = (String)shinseiData.get("seisankikan_to_min");
		hf1Cd = (String) shinseiData.get("hf1_cd");
		hf1Name = (String) shinseiData.get("hf1_name_ryakushiki");
		hf2Cd = (String) shinseiData.get("hf2_cd");
		hf2Name = (String) shinseiData.get("hf2_name_ryakushiki");
		hf3Cd = (String) shinseiData.get("hf3_cd");
		hf3Name = (String) shinseiData.get("hf3_name_ryakushiki");
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
		keijoubi = formatDate(shinseiData.get("keijoubi"));
		zeiritsuRyohi = formatMoney(shinseiData.get("zeiritsu"));
		this.keigenZeiritsuKbnRyohi = shinseiData.get("keigen_zeiritsu_kbn");
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
		torihikisakiCdRyohi = (String) shinseiData.get("torihikisaki_cd");
		torihikisakiNameRyohi = (String) shinseiData.get("torihikisaki_name_ryakushiki");
		projectCdRyohi = (String) shinseiData.get("project_cd");
		projectNameRyohi = (String) shinseiData.get("project_name");
		segmentCdRyohi = (String) shinseiData.get("segment_cd");
		segmentNameRyohi = (String) shinseiData.get("segment_name_ryakushiki");
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
		// 交通費だけここで初期化するのはちょっと気持ち悪いけど、一旦そのまま
		BatchKaikeiCommonLogic batchKaikeiLogic = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		var chuuki = super.setChuuki(sanshou, shinseiData, null, batchKaikeiLogic, commonLogic);
		this.chuuki1 = isNotEmpty(this.chuuki1) ? this.chuuki1 : chuuki[0];
		this.chuuki2Ryohi = chuuki[1];
		shiharaibi = formatDate(shinseiData.get("shiharaibi"));
		hosoku = (String) shinseiData.get("hosoku");
		this.kazeiKbnRyohi = (String) shinseiData.get("kari_kazei_kbn");
		this.bunriKbn = (String) shinseiData.get("bunri_kbn");
		this.kariShiireKbn = (String) shinseiData.get("kari_shiire_kbn");
		this.kashiShiireKbn = isEmpty((String) shinseiData.get("kashi_shiire_kbn")) ? "" : (String) shinseiData.get("kashi_shiire_kbn");
		this.invoiceDenpyou = (String) shinseiData.get("invoice_denpyou");
	}

	/**
	 * 明細のデフォルト状態に項目セットする。
	 */
	protected void makeDefaultMeisai() {
		setMeisaiArray(-1);
	}

	/**
	 * 明細共通部のセット。-1はmakeDefaultMeisai。いずれ旅費精算系と共通化したいところ。
	 * @param length 配列長
	 */
	protected void setMeisaiArray(int length)
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
		tanka = isDefault ? new String[] { "" } : new String[length];
		suuryouNyuryokuType = isDefault ? new String[] { "" } : new String[length];
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
	}
	
	/**
	 * 明細レコードのリストを画面表示項目に詰め替え
	 * @param meisaiList 明細レコードのリスト
	 */
	protected void meisaiData2Gamen(List<KoutsuuhiseisanMeisai> meisaiList) {
		int length = meisaiList.size();
		setMeisaiArray(length);
		for (int i = 0; i < length ; i++) {
			GMap meisai = meisaiList.get(i).map;
			shubetsuCd[i] = (String)meisai.get("shubetsu_cd");
			shubetsu1[i] = (String)meisai.get("shubetsu1");
			shubetsu2[i] = (String)meisai.get("shubetsu2");
			hayaFlg[i] = (String)meisai.get("haya_flg");
			yasuFlg[i] = (String)meisai.get("yasu_flg");
			rakuFlg[i] = (String)meisai.get("raku_flg");
			kikanFrom[i] = formatDate(meisai.get("kikan_from"));
			kikanTo[i] = "";
			shouhyouShoruiHissuFlg[i] = (String)meisai.get("shouhyou_shorui_hissu_flg");
			koutsuuShudan[i] = (String)meisai.get("koutsuu_shudan");
			ryoushuushoSeikyuushoTouFlg[i] = (String)meisai.get("ryoushuusho_seikyuusho_tou_flg");
			naiyou[i] = (String)meisai.get("naiyou");
			bikou[i] = (String)meisai.get("bikou");
			oufukuFlg[i] = (String)meisai.get("oufuku_flg");
			houjinCardFlgRyohi[i] = (String)meisai.get("houjin_card_riyou_flg");
			kaishaTehaiFlgRyohi[i] = (String)meisai.get("kaisha_tehai_flg");
			jidounyuuryokuFlg[i] = (String)meisai.get("jidounyuuryoku_flg");
			nissuu[i] = "";
			tanka[i] = formatMoneyDecimalWithNoPadding(meisai.get("tanka"));
			suuryouNyuryokuType[i] = meisai.get("suuryou_nyuryoku_type");
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
		}
	}


	@Override
	protected void displaySeigyo(EteamConnection connection) {
		// サーバーからの送信値が半角スペース→&nbsp;になっていてマスターと不一致になる為
		EteamCommon.trimNoBlank(shubetsu1);
		EteamCommon.trimNoBlank(shubetsu2);
		EteamCommon.trimNoBlank(koutsuuShudan);

		// 入力（登録、更新）可能かどうか判定
		enableInput = super.judgeEnableInput();

		// リスト作成
		zeiritsuRyohiList = this.zeiritsuDao.loadNormalZeiritsu();
		zeiritsuRyohiDomain = this.zeiritsuRyohiList.stream().map(item -> item.zeiritsu.toString()).collect(Collectors.toList()).toArray(new String[0]);
		kazeiKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn");
		kazeiKbnDomain = kazeiKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		koutsuuShudanList = masterLogic.loadKoutsuuShudan();
		bunriKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn");
		bunriKbnDomain = bunriKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		shiireKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn");
		shiireKbnDomain = shiireKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		koutsuuShudanDomain = EteamCommon.mapList2Arr(koutsuuShudanList, "koutsuu_shudan");
		suuryouNyuryokuTypeDomain = EteamCommon.mapList2Arr(koutsuuShudanList, "suuryou_nyuryoku_type");
		shiharaihouhouList = commonLogic.makeShiharaiHouhouList(EteamSettingInfo.Key.SHIHARAI_HOUHOU_A004, shiharaihouhou);
		shiharaihouhouDomain = EteamCommon.mapList2Arr(shiharaihouhouList, "naibu_cd");
		jigyoushaKbnList =  this.naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn");
		jigyoushaKbnDomain = jigyoushaKbnList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);

		// 支払方法を入力可能にするかどうか判定。
		disableShiharaiHouhou = (shiharaihouhouList.size() <= 1);

		//ICカード履歴ボタンの表示判定（起票者のみ）
		if(enableInput) {
			if(sysLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.IC_CARD)){
				icCardEnable = true;
			}
		}

		// 支払日の表示・入力判定。
		shiharaiBiMode = commonLogic.judgeShiharaiBiMode(denpyouId, loginUserInfo);

		// 入力可能時の制御
		if (enableInput) {
			// 仕訳パターンによる制御
			if (isNotEmpty(shiwakeEdaNoRyohi)) {
				// 初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				// 仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				ShiwakePatternMaster shiwakePattern = shiwakePatternMasterDao.find(super.denpyouKbn, Integer.parseInt(shiwakeEdaNoRyohi));
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
			if (ekispertEnable) {
				// 駅すぱあと呼出 start
				super.ekispertInit(connection, SEARCH_MODE_UNCHIN);
			}
		}

		// 経理担当が初めて伝票を開き、その伝票が振込のとき、支払日に自動入力
		if (shiharaiBiMode == 1 && isEmpty(shiharaibi) && shiharaihouhou.equals(SHIHARAI_HOUHOU.FURIKOMI)) {
			Date shiharai = null;
			if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A010).equals("1")) {
				shiharai = commonLogic.decideFurikomiDateHi(toDate(keijoubi));
			} else if (EteamSettingInfo.getSettingInfo(Key.FURIKOMI_RULE_A010).equals("2")) {
				shiharai = commonLogic.decideFurikomiDateYoubi(toDate(keijoubi));
			}
			if (shiharai != null) {
				shiharaibi = formatDate(shiharai);
				// マスターから基準日(計上日)に対する振込日が見つかった場合、支払日の更新
				this.koutsuuhiseisanDao.updateShiharaibi(denpyouId, shiharai, loginUserId);
			}
		}

		//計上日の入力状態・モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン))を判定
		keijouBiMode = commonLogic.judgeKeijouBiMode(hasseiShugi, keijoubiDefaultSettei.equals("3"), denpyouId, loginUserInfo, enableInput, setting.keijouNyuuryoku());
		//計上日プルダウンのリスト取得
		if(keijouBiMode == 3) keijoubiList = masterLogic.loadKeijoubiList(denpyouKbn);

		// 法人カードの表示可否
		houjinCardFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		GMap chkMap = bumonUsrLogic.selectUserInfo(getUser().getSeigyoUserId());
		if("1".equals(chkMap.get("houjin_card_riyou_flag")) && houjinCardFlag == true ){
			houjinCardRirekiEnable = true;
		}else{
			houjinCardRirekiEnable = false;
		}

		// 会社手配の表示可否
		kaishaTehaiFlag = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
	}

	/**
	 * 仕訳パターンから仕訳に必要な情報を読み込む。一部画面入力を無視するが、基本は同じ値のはず。
	 * @param connection コネクション
	 */
	protected void reloadShiwakePattern(EteamConnection connection) {

		// 社員コード取得
		GMap usrInfo = bumonUsrLogic.selectUserInfo(super.getKihyouUserId());
		String shainCd = (String) usrInfo.get("shain_no");
		// 法人カード識別用番号取得
		String houjinCard = (String) usrInfo.get("houjin_card_shikibetsuyou_num");
		//社員財務枝番コード取得
		String shainShiwakeEdaNo = this.masterLogic.getShainShiwakeEdano(super.getKihyouUserId());

		//差引支給金額の計算
		if(kingaku != null && ! kingaku.isEmpty()) {
		BigDecimal tmpShiharaiKingaku = toDecimal(kingaku);
		BigDecimal tmpHoujinKingaku = toDecimal(houjinCardRiyouGoukei);
		BigDecimal tmpKaishaTehaiKingaku = toDecimal(kaishaTehaiGoukei);
		sashihikiShikyuuKingaku = formatMoney((tmpShiharaiKingaku.subtract(tmpHoujinKingaku)).subtract(tmpKaishaTehaiKingaku));
		}

		if (isNotEmpty(shiwakeEdaNoRyohi)) {

			// 仕訳パターン取得
			ShiwakePatternMaster shiwakeP = shiwakePatternMasterDao.find(super.denpyouKbn, Integer.parseInt(shiwakeEdaNoRyohi));
			String daihyouBumonCd = super.daihyouFutanBumonCd;

			// 取引名
			torihikiNameRyohi = shiwakeP.torihikiName;

			// 借方 科目
			kamokuCdRyohi = shiwakeP.kariKamokuCd;

			// 借方 科目枝番
			String pKariKamokuEdabanCd = shiwakeP.kariKamokuEdabanCd;
			switch (pKariKamokuEdabanCd) {
			// 何もしない(画面入力のまま)
			case EteamConst.ShiwakeConst.EDABAN:
				break;
			// 固定コード値 or ブランク
			default:
				kamokuEdabanCdRyohi = pKariKamokuEdabanCd;
				break;
			}

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
			
			//申請内容の課税区分が税込・税抜系以外の場合、明細の事業者区分は通常課税固定にする
			if (!((List.of("001", "002", "011", "013").contains(kazeiKbnRyohi) && List.of("2","5","6","7","8","10").contains(this.shoriGroupRyohi.toString()))
					|| this.shoriGroupRyohi.toString().equals("21"))) {
				for (int i = 0; i < shubetsuCd.length; i++) {
					jigyoushaKbnRyohi[i] = "0";
				}
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

			// 借方 課税区分  画面上の値をつかうのでCO
			//kazeiKbnRyohi = shiwakeP.kariKazeiKbn;

			// 支払方法により貸方1 or 貸方2を切替する
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

			// 代表部門
			kashiFutanBumonCdRyohi = commonLogic.convFutanBumon(futanBumonCdRyohi, kashiFutanBumonCdRyohi, daihyouBumonCd);

			//貸方1　UF1-10
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

			//貸方2　UF1-10
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

			//貸方3　UF1-10
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

			//貸方4　UF1-10
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

			//貸方5　UF1-10
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

			// 社員コードを摘要コードに反映する場合
			if("1".equals(shiwakeP.shainCdRenkeiFlg) && "T".equals(setting.shainCdRenkei())) {
				if (shainCd.length() > 4) {
					tekiyouCdRyohi = shainCd.substring(shainCd.length() - 4);
				} else {
					tekiyouCdRyohi = shainCd;
				}
				// 法人カード識別用番号を摘要コードに反映する場合
			} else if ("T".equals(setting.houjinCardRenkei())) {
				if (houjinCard.length() > 4) {
					tekiyouCdRyohi = houjinCard.substring(houjinCard.length() - 4);
				} else {
					tekiyouCdRyohi = houjinCard;
				}
			} else {
				tekiyouCdRyohi = "";
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
	}


	/**
	 * 支払日のチェックを行う。エラーがあれば、エラーリストにメッセージが詰まる。
	 * @param shiharai 支払日
	 * @param keijou 計上日
	 * @param keijouName 計上日項目名
	 */
	protected void checkShiharaiBi(String shiharai, String keijou, String keijouName) {
		String pShiharaihouhou = shiharaihouhou;
		commonLogic.checkShiharaiBi(denpyouId, toDate(shiharai), toDate(keijou), keijouName, pShiharaihouhou, loginUserInfo, errorList);
	}
	/**
	 * 領収書が必要かのチェックを行う。
	 * 交通費精算 追加した明細の中に「領収書・請求書等」のチェックがオンの明細が1件以上ある場合
	 * @return 領収書が必要か
	 */
	protected boolean isUseShouhyouShorui() {
		for (int i = 0; i < shubetsuCd.length; i++) {
			if ("1".equals(ryoushuushoSeikyuushoTouFlg[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 交通費精算EXCEL出力
	 * @param connection コネクション
	 * @param out 出力先
	 */
	@Override
	public void makeExcel(EteamConnection connection, OutputStream out) {
		KoutsuuhiSeisanXlsLogic koutsuuhiseisanxlsLogic = EteamContainer.getComponent(KoutsuuhiSeisanXlsLogic.class, connection);
		koutsuuhiseisanxlsLogic.makeExcel(denpyouId, out);
	}
	
	/**
	 * @return 交通費精算Dto
	 */
	protected Koutsuuhiseisan createDto()
	{
		Koutsuuhiseisan koutsuuhiseisan = new Koutsuuhiseisan();
		koutsuuhiseisan.denpyouId = this.denpyouId;
		koutsuuhiseisan.mokuteki = this.mokuteki;
		koutsuuhiseisan.seisankikanFrom = super.toDate(seisankikanFrom);
		koutsuuhiseisan.seisankikanFromHour = this.seisankikanFromHour;
		koutsuuhiseisan.seisankikanFromMin = this.seisankikanFromMin;
		koutsuuhiseisan.seisankikanTo = super.toDate(seisankikanTo);
		koutsuuhiseisan.seisankikanToHour = this.seisankikanToHour;
		koutsuuhiseisan.seisankikanToMin = this.seisankikanToMin;
		koutsuuhiseisan.keijoubi = super.toDate(keijoubi);
		koutsuuhiseisan.shiharaibi = super.toDate(shiharaibi);
		koutsuuhiseisan.shiharaikiboubi = super.toDate(shiharaiKiboubi);
		koutsuuhiseisan.shiharaihouhou = this.shiharaihouhou;
		koutsuuhiseisan.tekiyou = this.tekiyouRyohi;
		koutsuuhiseisan.zeiritsu = super.toDecimalZeroIfEmpty(zeiritsuRyohi);
		koutsuuhiseisan.keigenZeiritsuKbn = keigenZeiritsuKbn.NORMAL;//0固定
		koutsuuhiseisan.goukeiKingaku = super.toDecimal(kingaku);
		koutsuuhiseisan.houjinCardRiyouKingaku = super.toDecimal(houjinCardRiyouGoukei);
		koutsuuhiseisan.kaishaTehaiKingaku = super.toDecimal(kaishaTehaiGoukei);
		koutsuuhiseisan.sashihikiShikyuuKingaku = super.toDecimal(sashihikiShikyuuKingaku);
		koutsuuhiseisan.hf1Cd = this.hf1Cd;
		koutsuuhiseisan.hf1NameRyakushiki = this.hf1Name;
		koutsuuhiseisan.hf2Cd = this.hf2Cd;
		koutsuuhiseisan.hf2NameRyakushiki = this.hf2Name;
		koutsuuhiseisan.hf3Cd = this.hf3Cd;
		koutsuuhiseisan.hf3NameRyakushiki = this.hf3Name;
		koutsuuhiseisan.hf4Cd = this.hf4Cd;
		koutsuuhiseisan.hf4NameRyakushiki = this.hf4Name;
		koutsuuhiseisan.hf5Cd = this.hf5Cd;
		koutsuuhiseisan.hf5NameRyakushiki = this.hf5Name;
		koutsuuhiseisan.hf6Cd = this.hf6Cd;
		koutsuuhiseisan.hf6NameRyakushiki = this.hf6Name;
		koutsuuhiseisan.hf7Cd = this.hf7Cd;
		koutsuuhiseisan.hf7NameRyakushiki = this.hf7Name;
		koutsuuhiseisan.hf8Cd = this.hf8Cd;
		koutsuuhiseisan.hf8NameRyakushiki = this.hf8Name;
		koutsuuhiseisan.hf9Cd = this.hf9Cd;
		koutsuuhiseisan.hf9NameRyakushiki = this.hf9Name;
		koutsuuhiseisan.hf10Cd = this.hf10Cd;
		koutsuuhiseisan.hf10NameRyakushiki = this.hf10Name;
		koutsuuhiseisan.hosoku = this.hosoku;
		koutsuuhiseisan.shiwakeEdano = super.isEmpty(shiwakeEdaNoRyohi) ? null : Integer.parseInt(shiwakeEdaNoRyohi);
		koutsuuhiseisan.torihikiName = this.torihikiNameRyohi;
		koutsuuhiseisan.kariFutanBumonCd = this.futanBumonCdRyohi;
		koutsuuhiseisan.kariFutanBumonName = this.futanBumonNameRyohi;
		koutsuuhiseisan.torihikisakiCd = this.torihikisakiCdRyohi;
		koutsuuhiseisan.torihikisakiNameRyakushiki = this.torihikisakiNameRyohi;
		koutsuuhiseisan.kariKamokuCd = this.kamokuCdRyohi;
		koutsuuhiseisan.kariKamokuName = this.kamokuNameRyohi;
		koutsuuhiseisan.kariKamokuEdabanCd = this.kamokuEdabanCdRyohi;
		koutsuuhiseisan.kariKamokuEdabanName = this.kamokuEdabanNameRyohi;
		koutsuuhiseisan.kariKazeiKbn = this.kazeiKbnRyohi;
		koutsuuhiseisan.kashiFutanBumonCd = this.kashiFutanBumonCdRyohi;
		koutsuuhiseisan.kashiFutanBumonName = this.kashiFutanBumonNameRyohi;
		koutsuuhiseisan.kashiKamokuCd = this.kashiKamokuCdRyohi;
		koutsuuhiseisan.kashiKamokuName = this.kashiKamokuNameRyohi;
		koutsuuhiseisan.kashiKamokuEdabanCd = this.kashiKamokuEdabanCdRyohi;
		koutsuuhiseisan.kashiKamokuEdabanName = this.kashiKamokuEdabanNameRyohi;
		koutsuuhiseisan.kashiKazeiKbn = this.kashiKazeiKbnRyohi;
		koutsuuhiseisan.uf1Cd = this.uf1CdRyohi;
		koutsuuhiseisan.uf1NameRyakushiki = this.uf1NameRyohi;
		koutsuuhiseisan.uf2Cd = this.uf2CdRyohi;
		koutsuuhiseisan.uf2NameRyakushiki = this.uf2NameRyohi;
		koutsuuhiseisan.uf3Cd = this.uf3CdRyohi;
		koutsuuhiseisan.uf3NameRyakushiki = this.uf3NameRyohi;
		koutsuuhiseisan.uf4Cd = this.uf4CdRyohi;
		koutsuuhiseisan.uf4NameRyakushiki = this.uf4NameRyohi;
		koutsuuhiseisan.uf5Cd = this.uf5CdRyohi;
		koutsuuhiseisan.uf5NameRyakushiki = this.uf5NameRyohi;
		koutsuuhiseisan.uf6Cd = this.uf6CdRyohi;
		koutsuuhiseisan.uf6NameRyakushiki = this.uf6NameRyohi;
		koutsuuhiseisan.uf7Cd = this.uf7CdRyohi;
		koutsuuhiseisan.uf7NameRyakushiki = this.uf7NameRyohi;
		koutsuuhiseisan.uf8Cd = this.uf8CdRyohi;
		koutsuuhiseisan.uf8NameRyakushiki = this.uf8NameRyohi;
		koutsuuhiseisan.uf9Cd = this.uf9CdRyohi;
		koutsuuhiseisan.uf9NameRyakushiki = this.uf9NameRyohi;
		koutsuuhiseisan.uf10Cd = this.uf10CdRyohi;
		koutsuuhiseisan.uf10NameRyakushiki = this.uf10NameRyohi;
		koutsuuhiseisan.projectCd = this.projectCdRyohi;
		koutsuuhiseisan.projectName = this.projectNameRyohi;
		koutsuuhiseisan.segmentCd = this.segmentCdRyohi;
		koutsuuhiseisan.segmentNameRyakushiki = this.segmentNameRyohi;
		koutsuuhiseisan.tekiyouCd = this.tekiyouCdRyohi;
		koutsuuhiseisan.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		koutsuuhiseisan.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		koutsuuhiseisan.bunriKbn = this.bunriKbn;
		koutsuuhiseisan.kariShiireKbn = this.kariShiireKbn;
		koutsuuhiseisan.kashiShiireKbn = this.kashiShiireKbn;
		koutsuuhiseisan.invoiceDenpyou = this.invoiceDenpyou;
		return koutsuuhiseisan;
	}
	
	/**
	 * @param i 明細番号
	 * @return 明細Dto
	 */
	protected KoutsuuhiseisanMeisai createMeisaiDto(int i)
	{
		KoutsuuhiseisanMeisai koutsuuhiseisanMeisai = new KoutsuuhiseisanMeisai();
		koutsuuhiseisanMeisai.denpyouId = this.denpyouId;
		koutsuuhiseisanMeisai.denpyouEdano = i + 1;
		koutsuuhiseisanMeisai.kikanFrom = super.toDate(kikanFrom[i]);
		koutsuuhiseisanMeisai.shubetsuCd = this.shubetsuCd[i];
		koutsuuhiseisanMeisai.shubetsu1 = this.shubetsu1[i];
		koutsuuhiseisanMeisai.shubetsu2 = this.shubetsu2[i];
		koutsuuhiseisanMeisai.hayaFlg = this.hayaFlg[i];
		koutsuuhiseisanMeisai.yasuFlg = this.yasuFlg[i];
		koutsuuhiseisanMeisai.rakuFlg = this.rakuFlg[i];
		koutsuuhiseisanMeisai.koutsuuShudan = this.koutsuuShudan[i];
		koutsuuhiseisanMeisai.shouhyouShoruiHissuFlg = this.shouhyouShoruiHissuFlg[i];
		koutsuuhiseisanMeisai.ryoushuushoSeikyuushoTouFlg = this.ryoushuushoSeikyuushoTouFlg[i];
		koutsuuhiseisanMeisai.naiyou = this.naiyou[i];
		koutsuuhiseisanMeisai.bikou = this.bikou[i];
		koutsuuhiseisanMeisai.oufukuFlg = this.oufukuFlg[i];
		koutsuuhiseisanMeisai.houjinCardRiyouFlg = this.houjinCardFlgRyohi[i];
		koutsuuhiseisanMeisai.kaishaTehaiFlg = this.kaishaTehaiFlgRyohi[i];
		koutsuuhiseisanMeisai.jidounyuuryokuFlg = this.jidounyuuryokuFlg[i];
		koutsuuhiseisanMeisai.tanka = super.toDecimal(tanka[i]);
		koutsuuhiseisanMeisai.suuryouNyuryokuType = this.suuryouNyuryokuType[i];
		koutsuuhiseisanMeisai.suuryou = super.toDecimal(suuryou[i]);
		koutsuuhiseisanMeisai.suuryouKigou = this.suuryouKigou[i];
		koutsuuhiseisanMeisai.meisaiKingaku = super.toDecimal(meisaiKingaku[i]);
		koutsuuhiseisanMeisai.icCardNo = this.icCardNo[i];
		koutsuuhiseisanMeisai.icCardSequenceNo = this.icCardSequenceNo[i];
		koutsuuhiseisanMeisai.himodukeCardMeisai = super.isEmpty(himodukeCardMeisaiRyohi[i]) ? null : Long.parseLong(himodukeCardMeisaiRyohi[i]);
		koutsuuhiseisanMeisai.tourokuUserId = super.getUser().getTourokuOrKoushinUserId();
		koutsuuhiseisanMeisai.koushinUserId = super.getUser().getTourokuOrKoushinUserId();
		koutsuuhiseisanMeisai.shiharaisakiName = this.shiharaisakiNameRyohi[i];
		koutsuuhiseisanMeisai.jigyoushaKbn = super.getDefaultJigyoushaKbnIfEmpty(this.jigyoushaKbnRyohi[i]);
		koutsuuhiseisanMeisai.zeinukiKingaku = super.toDecimal(this.zeinukiKingakuRyohi[i]);
		koutsuuhiseisanMeisai.shouhizeigaku = super.toDecimal(this.shouhizeigakuRyohi[i]);
		koutsuuhiseisanMeisai.zeigakuFixFlg = super.getDefaultZeigakuFixFlgIfEmpty(this.zeigakuFixFlg[i]);
		return koutsuuhiseisanMeisai;
	}
}
