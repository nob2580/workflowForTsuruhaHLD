package eteam.gyoumu.kaikei;

import java.sql.Date;

import eteam.base.EteamConnection;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_ITEM;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.database.dto.ShiwakePatternMaster;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引（仕訳）追加画面Action
 */
@Getter @Setter @ToString
public class TorihikiTsuikaAction extends TorihikiCommonAction {

	/** 取引コード(仕訳枝番)表示可否 */
	@Override
	public boolean isShiwakeEdanoEnabled() { return setting.torihikiSettei().equals("1"); }
	
	/** 仕訳枝番編集可否 */
	@Override
	public boolean isShiwakeEdanoEditable() { return true; }

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 追加では仕訳枝番チェックは最初
		checkNumberOver1(shiwakeEdano, 1, 8, "取引コード", false); //設定により仕訳枝番を入力する
		super.formatCheck();
	}

	@Override
	protected void hissuCheck(int eventNum) {

		//設定により仕訳枝番を入力する
		if (shiwakeEdanoEnabled) {
			String[][] listForEdano = {
			//項目  				EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{shiwakeEdano, "取引コード", "0", "1", "2", },
			};
			hissuCheckCommon(listForEdano, eventNum);
		}
		super.hissuCheck(eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){

		//1.入力チェック
		formatCheck();
		hissuCheck(1);

		connection = EteamConnection.connect();
		try{
			initParts();

			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理

			//色々読込
			displaySeigyo();

			//デフォルト値設定
			yuukouKigenFrom = formatDate(new Date(System.currentTimeMillis()));
			yuukouKigenTo = "9999/12/31";
			tekiyouNyuryokuOn = super.shiwakePatternMasterDao.setLatestShiwakePatternMaster();
			defaultHyoujiOn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.DEFAULT_HYOUJI);
			kousaihiOn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.KOUSAIHI);
			kakeOn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.KAKE);
			shainCdRenkei = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.SHAIN);
			zaimuEdabanRenkei = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.ZAIMU_EDABAN);

			kariFutanBumonCd = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			kashiFutanBumonCd1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			kashiFutanBumonCd2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			kashiFutanBumonCd3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			kashiFutanBumonCd4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			kashiFutanBumonCd5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			kariFutanBumonCd = myLogic.ConvertShiwakeVarCd2Nm(kariFutanBumonCd, inputSeigyo.shiwakeVarListKariBumon);
			kashiFutanBumonCd1 = myLogic.ConvertShiwakeVarCd2Nm(kashiFutanBumonCd1, inputSeigyo.shiwakeVarListKashi1Bumon);
			kashiFutanBumonCd2 = myLogic.ConvertShiwakeVarCd2Nm(kashiFutanBumonCd2, inputSeigyo.shiwakeVarListKashi2Bumon);
			kashiFutanBumonCd3 = myLogic.ConvertShiwakeVarCd2Nm(kashiFutanBumonCd3, inputSeigyo.shiwakeVarListKashi3Bumon);
			kashiFutanBumonCd4 = myLogic.ConvertShiwakeVarCd2Nm(kashiFutanBumonCd4, inputSeigyo.shiwakeVarListKashi4Bumon);
			kashiFutanBumonCd5 = myLogic.ConvertShiwakeVarCd2Nm(kashiFutanBumonCd5, inputSeigyo.shiwakeVarListKashi5Bumon);
			kariFutanBumonName = super.bumonMasterDao.findFutanBumonName(kariFutanBumonCd);
			kashiFutanBumonName1 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd1);
			kashiFutanBumonName2 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd2);
			kashiFutanBumonName3 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd3);
			kashiFutanBumonName4 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd4);
			kashiFutanBumonName5 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd5);

			kariKanjyouKamokuCd = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			kashiKanjyouKamokuCd1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			kashiKanjyouKamokuCd2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			kashiKanjyouKamokuCd3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			kashiKanjyouKamokuCd4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			kashiKanjyouKamokuCd5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			kariKanjyouKamokuCd = myLogic.ConvertShiwakeVarCd2Nm(kariKanjyouKamokuCd, inputSeigyo.shiwakeVarListKariKamoku);
			kashiKanjyouKamokuCd1 = myLogic.ConvertShiwakeVarCd2Nm(kashiKanjyouKamokuCd1, inputSeigyo.shiwakeVarListKashi1Kamoku);
			kashiKanjyouKamokuCd2 = myLogic.ConvertShiwakeVarCd2Nm(kashiKanjyouKamokuCd2, inputSeigyo.shiwakeVarListKashi2Kamoku);
			kashiKanjyouKamokuCd3 = myLogic.ConvertShiwakeVarCd2Nm(kashiKanjyouKamokuCd3, inputSeigyo.shiwakeVarListKashi3Kamoku);
			kashiKanjyouKamokuCd4 = myLogic.ConvertShiwakeVarCd2Nm(kashiKanjyouKamokuCd4, inputSeigyo.shiwakeVarListKashi4Kamoku);
			kashiKanjyouKamokuCd5 = myLogic.ConvertShiwakeVarCd2Nm(kashiKanjyouKamokuCd5, inputSeigyo.shiwakeVarListKashi5Kamoku);
			kariKanjyouKamokuName = super.kamokuMasterDao.findKamokuNameStr(kariKanjyouKamokuCd);
			kashiKanjyouKamokuName1 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd1);
			kashiKanjyouKamokuName2 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd2);
			kashiKanjyouKamokuName3 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd3);
			kashiKanjyouKamokuName4 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd4);
			kashiKanjyouKamokuName5 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd5);

			kariKamokuEdabanCd = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			kashiKamokuEdabanCd1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			kashiKamokuEdabanCd2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			kashiKamokuEdabanCd3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			kashiKamokuEdabanCd4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			kashiKamokuEdabanCd5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			kariKamokuEdabanCd = myLogic.ConvertShiwakeVarCd2Nm(kariKamokuEdabanCd, inputSeigyo.shiwakeVarListKariEdano);
			kashiKamokuEdabanCd1 = myLogic.ConvertShiwakeVarCd2Nm(kashiKamokuEdabanCd1, inputSeigyo.shiwakeVarListKashi1Edano);
			kashiKamokuEdabanCd2 = myLogic.ConvertShiwakeVarCd2Nm(kashiKamokuEdabanCd2, inputSeigyo.shiwakeVarListKashi2Edano);
			kashiKamokuEdabanCd3 = myLogic.ConvertShiwakeVarCd2Nm(kashiKamokuEdabanCd3, inputSeigyo.shiwakeVarListKashi3Edano);
			kashiKamokuEdabanCd4 = myLogic.ConvertShiwakeVarCd2Nm(kashiKamokuEdabanCd4, inputSeigyo.shiwakeVarListKashi4Edano);
			kashiKamokuEdabanCd5 = myLogic.ConvertShiwakeVarCd2Nm(kashiKamokuEdabanCd5, inputSeigyo.shiwakeVarListKashi5Edano);
			kariKamokuEdabanName = super.kamokuEdabanZandakaDao.findEdabanName(kariKanjyouKamokuCd, kariKamokuEdabanCd);
			kashiKamokuEdabanName1 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd1, kashiKamokuEdabanCd1);
			kashiKamokuEdabanName2 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd2, kashiKamokuEdabanCd2);
			kashiKamokuEdabanName3 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd3, kashiKamokuEdabanCd3);
			kashiKamokuEdabanName4 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd4, kashiKamokuEdabanCd4);
			kashiKamokuEdabanName5 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd5, kashiKamokuEdabanCd5);

			kariTorihikisakiCd = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			kashiTorihikisakiCd1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			kashiTorihikisakiCd2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			kashiTorihikisakiCd3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			kashiTorihikisakiCd4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			kashiTorihikisakiCd5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			kariTorihikisakiCd = myLogic.ConvertShiwakeVarCd2Nm(kariTorihikisakiCd, inputSeigyo.shiwakeVarListKariTorihiki);
			kashiTorihikisakiCd1 = myLogic.ConvertShiwakeVarCd2Nm(kashiTorihikisakiCd1, inputSeigyo.shiwakeVarListKashi1Torihiki);
			kashiTorihikisakiCd2 = myLogic.ConvertShiwakeVarCd2Nm(kashiTorihikisakiCd2, inputSeigyo.shiwakeVarListKashi2Torihiki);
			kashiTorihikisakiCd3 = myLogic.ConvertShiwakeVarCd2Nm(kashiTorihikisakiCd3, inputSeigyo.shiwakeVarListKashi3Torihiki);
			kashiTorihikisakiCd4 = myLogic.ConvertShiwakeVarCd2Nm(kashiTorihikisakiCd4, inputSeigyo.shiwakeVarListKashi4Torihiki);
			kashiTorihikisakiCd5 = myLogic.ConvertShiwakeVarCd2Nm(kashiTorihikisakiCd5, inputSeigyo.shiwakeVarListKashi5Torihiki);
			kariTorihikisakiName = kanriCategoryLogic.findTorihikisakiName(kariTorihikisakiCd);
			kashiTorihikisakiName1 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd1);
			kashiTorihikisakiName2 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd2);
			kashiTorihikisakiName3 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd3);
			kashiTorihikisakiName4 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd4);
			kashiTorihikisakiName5 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd5);

			kariProjectCd = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			kashiProjectCd1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			kashiProjectCd2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			kashiProjectCd3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			kashiProjectCd4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			kashiProjectCd5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			kariProjectCd = myLogic.ConvertShiwakeVarCd2Nm(kariProjectCd, inputSeigyo.shiwakeVarListKariProject);
			kashiProjectCd1 = myLogic.ConvertShiwakeVarCd2Nm(kashiProjectCd1, inputSeigyo.shiwakeVarListKashi1Project);
			kashiProjectCd2 = myLogic.ConvertShiwakeVarCd2Nm(kashiProjectCd2, inputSeigyo.shiwakeVarListKashi2Project);
			kashiProjectCd3 = myLogic.ConvertShiwakeVarCd2Nm(kashiProjectCd3, inputSeigyo.shiwakeVarListKashi3Project);
			kashiProjectCd4 = myLogic.ConvertShiwakeVarCd2Nm(kashiProjectCd4, inputSeigyo.shiwakeVarListKashi4Project);
			kashiProjectCd5 = myLogic.ConvertShiwakeVarCd2Nm(kashiProjectCd5, inputSeigyo.shiwakeVarListKashi5Project);
			kariProjectName = kanriCategoryLogic.findProjectName(kariProjectCd);
			kashiProjectName1 = kanriCategoryLogic.findProjectName(kashiProjectCd1);
			kashiProjectName2 = kanriCategoryLogic.findProjectName(kashiProjectCd2);
			kashiProjectName3 = kanriCategoryLogic.findProjectName(kashiProjectCd3);
			kashiProjectName4 = kanriCategoryLogic.findProjectName(kashiProjectCd4);
			kashiProjectName5 = kanriCategoryLogic.findProjectName(kashiProjectCd5);

			kariSegmentCd = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			kashiSegmentCd1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			kashiSegmentCd2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			kashiSegmentCd3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			kashiSegmentCd4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			kashiSegmentCd5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			kariSegmentCd = myLogic.ConvertShiwakeVarCd2Nm(kariSegmentCd, inputSeigyo.shiwakeVarListKariSegment);
			kashiSegmentCd1 = myLogic.ConvertShiwakeVarCd2Nm(kashiSegmentCd1, inputSeigyo.shiwakeVarListKashi1Segment);
			kashiSegmentCd2 = myLogic.ConvertShiwakeVarCd2Nm(kashiSegmentCd2, inputSeigyo.shiwakeVarListKashi2Segment);
			kashiSegmentCd3 = myLogic.ConvertShiwakeVarCd2Nm(kashiSegmentCd3, inputSeigyo.shiwakeVarListKashi3Segment);
			kashiSegmentCd4 = myLogic.ConvertShiwakeVarCd2Nm(kashiSegmentCd4, inputSeigyo.shiwakeVarListKashi4Segment);
			kashiSegmentCd5 = myLogic.ConvertShiwakeVarCd2Nm(kashiSegmentCd5, inputSeigyo.shiwakeVarListKashi5Segment);
			kariSegmentName = kanriCategoryLogic.findSegmentName(kariSegmentCd);
			kashiSegmentName1 = kanriCategoryLogic.findSegmentName(kashiSegmentCd1);
			kashiSegmentName2 = kanriCategoryLogic.findSegmentName(kashiSegmentCd2);
			kashiSegmentName3 = kanriCategoryLogic.findSegmentName(kashiSegmentCd3);
			kashiSegmentName4 = kanriCategoryLogic.findSegmentName(kashiSegmentCd4);
			kashiSegmentName5 = kanriCategoryLogic.findSegmentName(kashiSegmentCd5);

			kariKazeiKbn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			kashiKazeiKbn1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			kashiKazeiKbn2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			kashiKazeiKbn3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			kashiKazeiKbn4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			kashiKazeiKbn5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);

			kariZeiritsu = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.ZEIRITSU);
			kariKeigenZeiritsuKbn = "";

			kariBunriKbn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			kashiBunriKbn1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			kashiBunriKbn2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			kashiBunriKbn3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			kashiBunriKbn4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			kashiBunriKbn5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);

			kariShiireKbn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
			kashiShiireKbn1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
			kashiShiireKbn2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
			kashiShiireKbn3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
			kashiShiireKbn4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
			kashiShiireKbn5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
			
			oldKariKazeiKbn = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			oldKashiKazeiKbn1 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			oldKashiKazeiKbn2 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			oldKashiKazeiKbn3 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			oldKashiKazeiKbn4 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			oldKashiKazeiKbn5 = super.shiwakePatternSettingDao.findDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			
			return "success";
		}finally{
			connection.close();
		}
	}

	@Override
	protected void initParts()
	{
		super.initParts();
		this.shiwakeEdanoEnabled = setting.torihikiSettei().equals("1"); // initPartsの共通フローで初期化しておかないとまともに動かないので;
	}
	
	/**
	 * 登録イベント処理
	 * @return ResultName
	 */
	public String touroku() {
		if(!gaibuYobidashiFlag) {
			connection = EteamConnection.connect();
		}
		else {
			connection = getGaibuConnection();
		}
		try{
			initParts();

			//色々読込
			displaySeigyo();

			//1.入力チェック
			formatCheck();
			if(!gaibuYobidashiFlag) {
				hissuCheck(2);
			}else{
				hissuCheck(3);
			}
			if (0 < errorList.size())
			{
				return "error";
			}

			//設定により仕訳枝番を入力する
			if (shiwakeEdanoEnabled) {
				if (this.shiwakePatternMasterDao.exists(denpyouKbn, toInteger(shiwakeEdano))) {
					errorList.add("取引コードは既に登録されています。");
					return "error";
				}
			}

			//非表示状態の項目 null → blank
			setHiHyoujiItemByBlank();

			//有効期限終了日が未設定の場合、9999/12/31を設定
			if(yuukouKigenTo == null || yuukouKigenTo.equals(""))
			{
				yuukouKigenTo = "9999/12/31";
			}

			//項目間チェック
			soukanCheck();
			if (0 < errorList.size())
			{
				return "error";
			}

			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理
			ShiwakePatternMaster dto = this.createDto();
			//インボイス処理開始前後関係なく、旧課税区分に新課税区分をセット
			dto.oldKariKazeiKbn = dto.kariKazeiKbn;
			dto.oldKashiKazeiKbn1 = dto.kashiKazeiKbn1;
			dto.oldKashiKazeiKbn2 = dto.kashiKazeiKbn2;
			dto.oldKashiKazeiKbn3 = dto.kashiKazeiKbn3;
			dto.oldKashiKazeiKbn4 = dto.kashiKazeiKbn4;
			dto.oldKashiKazeiKbn5 = dto.kashiKazeiKbn5;

			putDataMap_Ext(dto.map); // 仮
			this.shiwakePatternMasterDao.insert(dto, dto.tourokuUserId);
			if(!gaibuYobidashiFlag) {
				connection.commit();
			}

			//5.戻り値を返す
			return "success";
		}finally{
			if(!gaibuYobidashiFlag) {
				connection.close();
			}
		}
	}


	
	/**
	 * データマップ更新の拡張
	 * @param dataMap データマップ
	 */
	public void putDataMap_Ext(GMap dataMap) {}
}
