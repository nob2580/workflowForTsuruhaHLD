package eteam.gyoumu.kaikei;

import eteam.base.EteamConnection;
import eteam.base.GMap;
import eteam.database.dto.ShiwakePatternMaster;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引（仕訳）変更画面Action
 */
@Getter @Setter @ToString
public class TorihikiHenkouAction extends TorihikiCommonAction {

	/** 取引コード(仕訳枝番)表示可否 */
	@Override
	public boolean isShiwakeEdanoEnabled() { return true; }
	
	/** 仕訳枝番編集可否 */
	@Override
	public boolean isShiwakeEdanoEditable() { return false; }
	
	//boolean isInvoiceStart = "1".equals(invoiceStart.invoiceFlg);)
			
	@Override
	protected void formatCheck() {
		super.formatCheck();
		// 変更では仕訳枝番チェックは最後
		checkNumberOver1(shiwakeEdano, 1, 8, "仕訳枝番号", true);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		super.hissuCheck(eventNum);
		String[][] listForEdano = {
			//項目 EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{shiwakeEdano, "仕訳枝番号", "2", "2", "1", },
		};
		// 変更では最後に仕訳枝番をチェックする
		hissuCheckCommon(listForEdano, eventNum);
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

			//仕訳パターンマスターの値
			var shiwakePatternMaster = super.shiwakePatternMasterDao.find(denpyouKbn, Integer.parseInt(shiwakeEdano));

			yuukouKigenFrom = formatDate(shiwakePatternMaster.yuukouKigenFrom);
			yuukouKigenTo = formatDate(shiwakePatternMaster.yuukouKigenTo);
			bunrui1 = shiwakePatternMaster.bunrui1;
			bunrui2 = shiwakePatternMaster.bunrui2;
			bunrui3 = shiwakePatternMaster.bunrui3;
			torihikiNm = shiwakePatternMaster.torihikiName;
			tekiyouNyuryokuOn = shiwakePatternMaster.tekiyouFlg;
			tekiyou = shiwakePatternMaster.tekiyou;
			defaultHyoujiOn = shiwakePatternMaster.defaultHyoujiFlg;
			kousaihiOn = shiwakePatternMaster.kousaihiHyoujiFlg;
			ninzuuOn = shiwakePatternMaster.kousaihiNinzuuRiyouFlg;
			kousaihiKijungaku = shiwakePatternMaster.kousaihiKijunGaku == null ? "" : shiwakePatternMaster.kousaihiKijunGaku.toString();
			kousaihiCheckHouhou = shiwakePatternMaster.kousaihiCheckHouhou;
			kousaihiCheckResult = shiwakePatternMaster.kousaihiCheckResult;
			kakeOn = shiwakePatternMaster.kakeFlg;
			hyoujijun = String.valueOf(shiwakePatternMaster.hyoujiJun);
			shainCdRenkei = shiwakePatternMaster.shainCdRenkeiFlg;
			zaimuEdabanRenkei = shiwakePatternMaster.edabanRenkeiFlg;


			kariFutanBumonCd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariFutanBumonCd, inputSeigyo.shiwakeVarListKariBumon);
			kashiFutanBumonCd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiFutanBumonCd1, inputSeigyo.shiwakeVarListKashi1Bumon);
			kashiFutanBumonCd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiFutanBumonCd2, inputSeigyo.shiwakeVarListKashi2Bumon);
			kashiFutanBumonCd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiFutanBumonCd3, inputSeigyo.shiwakeVarListKashi3Bumon);
			kashiFutanBumonCd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiFutanBumonCd4, inputSeigyo.shiwakeVarListKashi4Bumon);
			kashiFutanBumonCd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiFutanBumonCd5, inputSeigyo.shiwakeVarListKashi5Bumon);
			kariFutanBumonName = super.bumonMasterDao.findFutanBumonName(kariFutanBumonCd);
			kashiFutanBumonName1 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd1);
			kashiFutanBumonName2 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd2);
			kashiFutanBumonName3 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd3);
			kashiFutanBumonName4 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd4);
			kashiFutanBumonName5 = super.bumonMasterDao.findFutanBumonName(kashiFutanBumonCd5);

			kariKanjyouKamokuCd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariKamokuCd, inputSeigyo.shiwakeVarListKariKamoku);
			kashiKanjyouKamokuCd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuCd1, inputSeigyo.shiwakeVarListKashi1Kamoku);
			kashiKanjyouKamokuCd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuCd2, inputSeigyo.shiwakeVarListKashi2Kamoku);
			kashiKanjyouKamokuCd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuCd3, inputSeigyo.shiwakeVarListKashi3Kamoku);
			kashiKanjyouKamokuCd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuCd4, inputSeigyo.shiwakeVarListKashi4Kamoku);
			kashiKanjyouKamokuCd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuCd5, inputSeigyo.shiwakeVarListKashi5Kamoku);
			kariKanjyouKamokuName = super.kamokuMasterDao.findKamokuNameStr(kariKanjyouKamokuCd);
			kashiKanjyouKamokuName1 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd1);
			kashiKanjyouKamokuName2 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd2);
			kashiKanjyouKamokuName3 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd3);
			kashiKanjyouKamokuName4 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd4);
			kashiKanjyouKamokuName5 = super.kamokuMasterDao.findKamokuNameStr(kashiKanjyouKamokuCd5);


			kariKamokuEdabanCd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariKamokuEdabanCd, inputSeigyo.shiwakeVarListKariEdano);
			kashiKamokuEdabanCd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuEdabanCd1, inputSeigyo.shiwakeVarListKashi1Edano);
			kashiKamokuEdabanCd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuEdabanCd2, inputSeigyo.shiwakeVarListKashi2Edano);
			kashiKamokuEdabanCd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuEdabanCd3, inputSeigyo.shiwakeVarListKashi3Edano);
			kashiKamokuEdabanCd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuEdabanCd4, inputSeigyo.shiwakeVarListKashi4Edano);
			kashiKamokuEdabanCd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiKamokuEdabanCd5, inputSeigyo.shiwakeVarListKashi5Edano);
			kariKamokuEdabanName = super.kamokuEdabanZandakaDao.findEdabanName(kariKanjyouKamokuCd, kariKamokuEdabanCd);
			kashiKamokuEdabanName1 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd1, kashiKamokuEdabanCd1);
			kashiKamokuEdabanName2 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd2, kashiKamokuEdabanCd2);
			kashiKamokuEdabanName3 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd3, kashiKamokuEdabanCd3);
			kashiKamokuEdabanName4 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd4, kashiKamokuEdabanCd4);
			kashiKamokuEdabanName5 = super.kamokuEdabanZandakaDao.findEdabanName(kashiKanjyouKamokuCd5, kashiKamokuEdabanCd5);

			kariTorihikisakiCd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariTorihikisakiCd, inputSeigyo.shiwakeVarListKariTorihiki);
			kashiTorihikisakiCd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiTorihikisakiCd1, inputSeigyo.shiwakeVarListKashi1Torihiki);
			kashiTorihikisakiCd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiTorihikisakiCd2, inputSeigyo.shiwakeVarListKashi2Torihiki);
			kashiTorihikisakiCd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiTorihikisakiCd3, inputSeigyo.shiwakeVarListKashi3Torihiki);
			kashiTorihikisakiCd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiTorihikisakiCd4, inputSeigyo.shiwakeVarListKashi4Torihiki);
			kashiTorihikisakiCd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiTorihikisakiCd5, inputSeigyo.shiwakeVarListKashi5Torihiki);
			kariTorihikisakiName = kanriCategoryLogic.findTorihikisakiName(kariTorihikisakiCd);
			kashiTorihikisakiName1 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd1);
			kashiTorihikisakiName2 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd2);
			kashiTorihikisakiName3 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd3);
			kashiTorihikisakiName4 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd4);
			kashiTorihikisakiName5 = kanriCategoryLogic.findTorihikisakiName(kashiTorihikisakiCd5);

			kariProjectCd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariProjectCd, inputSeigyo.shiwakeVarListKariProject);
			kashiProjectCd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiProjectCd1, inputSeigyo.shiwakeVarListKashi1Project);
			kashiProjectCd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiProjectCd2, inputSeigyo.shiwakeVarListKashi2Project);
			kashiProjectCd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiProjectCd3, inputSeigyo.shiwakeVarListKashi3Project);
			kashiProjectCd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiProjectCd4, inputSeigyo.shiwakeVarListKashi4Project);
			kashiProjectCd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiProjectCd5, inputSeigyo.shiwakeVarListKashi5Project);
			kariProjectName = kanriCategoryLogic.findProjectName(kariProjectCd);
			kashiProjectName1 = kanriCategoryLogic.findProjectName(kashiProjectCd1);
			kashiProjectName2 = kanriCategoryLogic.findProjectName(kashiProjectCd2);
			kashiProjectName3 = kanriCategoryLogic.findProjectName(kashiProjectCd3);
			kashiProjectName4 = kanriCategoryLogic.findProjectName(kashiProjectCd4);
			kashiProjectName5 = kanriCategoryLogic.findProjectName(kashiProjectCd5);

			kariSegmentCd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariSegmentCd, inputSeigyo.shiwakeVarListKariSegment);
			kashiSegmentCd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiSegmentCd1, inputSeigyo.shiwakeVarListKashi1Segment);
			kashiSegmentCd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiSegmentCd2, inputSeigyo.shiwakeVarListKashi2Segment);
			kashiSegmentCd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiSegmentCd3, inputSeigyo.shiwakeVarListKashi3Segment);
			kashiSegmentCd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiSegmentCd4, inputSeigyo.shiwakeVarListKashi4Segment);
			kashiSegmentCd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiSegmentCd5, inputSeigyo.shiwakeVarListKashi5Segment);
			kariSegmentName = kanriCategoryLogic.findSegmentName(kariSegmentCd);
			kashiSegmentName1 = kanriCategoryLogic.findSegmentName(kashiSegmentCd1);
			kashiSegmentName2 = kanriCategoryLogic.findSegmentName(kashiSegmentCd2);
			kashiSegmentName3 = kanriCategoryLogic.findSegmentName(kashiSegmentCd3);
			kashiSegmentName4 = kanriCategoryLogic.findSegmentName(kashiSegmentCd4);
			kashiSegmentName5 = kanriCategoryLogic.findSegmentName(kashiSegmentCd5);

			// できればこの辺のUFも何とかしたいが、一旦保留
			kariUf1Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf1Cd, inputSeigyo.shiwakeVarListKariUf1);
			kashiUf1Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf1Cd1, inputSeigyo.shiwakeVarListKashi1Uf1);
			kashiUf1Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf1Cd2, inputSeigyo.shiwakeVarListKashi2Uf1);
			kashiUf1Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf1Cd3, inputSeigyo.shiwakeVarListKashi3Uf1);
			kashiUf1Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf1Cd4, inputSeigyo.shiwakeVarListKashi4Uf1);
			kashiUf1Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf1Cd5, inputSeigyo.shiwakeVarListKashi5Uf1);
			kariUf1Name = kanriCategoryLogic.findUfName("1", kariUf1Cd);
			kashiUf1Name1 = kanriCategoryLogic.findUfName("1", kashiUf1Cd1);
			kashiUf1Name2 = kanriCategoryLogic.findUfName("1", kashiUf1Cd2);
			kashiUf1Name3 = kanriCategoryLogic.findUfName("1", kashiUf1Cd3);
			kashiUf1Name4 = kanriCategoryLogic.findUfName("1", kashiUf1Cd4);
			kashiUf1Name5 = kanriCategoryLogic.findUfName("1", kashiUf1Cd5);

			kariUf2Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf2Cd, inputSeigyo.shiwakeVarListKariUf2);
			kashiUf2Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf2Cd1, inputSeigyo.shiwakeVarListKashi1Uf2);
			kashiUf2Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf2Cd2, inputSeigyo.shiwakeVarListKashi2Uf2);
			kashiUf2Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf2Cd3, inputSeigyo.shiwakeVarListKashi3Uf2);
			kashiUf2Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf2Cd4, inputSeigyo.shiwakeVarListKashi4Uf2);
			kashiUf2Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf2Cd5, inputSeigyo.shiwakeVarListKashi5Uf2);
			kariUf2Name = kanriCategoryLogic.findUfName("2", kariUf2Cd);
			kashiUf2Name1 = kanriCategoryLogic.findUfName("2", kashiUf2Cd1);
			kashiUf2Name2 = kanriCategoryLogic.findUfName("2", kashiUf2Cd2);
			kashiUf2Name3 = kanriCategoryLogic.findUfName("2", kashiUf2Cd3);
			kashiUf2Name4 = kanriCategoryLogic.findUfName("2", kashiUf2Cd4);
			kashiUf2Name5 = kanriCategoryLogic.findUfName("2", kashiUf2Cd5);

			kariUf3Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf3Cd, inputSeigyo.shiwakeVarListKariUf3);
			kashiUf3Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf3Cd1, inputSeigyo.shiwakeVarListKashi1Uf3);
			kashiUf3Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf3Cd2, inputSeigyo.shiwakeVarListKashi2Uf3);
			kashiUf3Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf3Cd3, inputSeigyo.shiwakeVarListKashi3Uf3);
			kashiUf3Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf3Cd4, inputSeigyo.shiwakeVarListKashi4Uf3);
			kashiUf3Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf3Cd5, inputSeigyo.shiwakeVarListKashi5Uf3);
			kariUf3Name = kanriCategoryLogic.findUfName("3", kariUf3Cd);
			kashiUf3Name1 = kanriCategoryLogic.findUfName("3", kashiUf3Cd1);
			kashiUf3Name2 = kanriCategoryLogic.findUfName("3", kashiUf3Cd2);
			kashiUf3Name3 = kanriCategoryLogic.findUfName("3", kashiUf3Cd3);
			kashiUf3Name4 = kanriCategoryLogic.findUfName("3", kashiUf3Cd4);
			kashiUf3Name5 = kanriCategoryLogic.findUfName("3", kashiUf3Cd5);

			kariUf4Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf4Cd, inputSeigyo.shiwakeVarListKariUf4);
			kashiUf4Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf4Cd1, inputSeigyo.shiwakeVarListKashi1Uf4);
			kashiUf4Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf4Cd2, inputSeigyo.shiwakeVarListKashi2Uf4);
			kashiUf4Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf4Cd3, inputSeigyo.shiwakeVarListKashi3Uf4);
			kashiUf4Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf4Cd4, inputSeigyo.shiwakeVarListKashi4Uf4);
			kashiUf4Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf4Cd5, inputSeigyo.shiwakeVarListKashi5Uf4);
			kariUf4Name = kanriCategoryLogic.findUfName("4", kariUf4Cd);
			kashiUf4Name1 = kanriCategoryLogic.findUfName("4", kashiUf4Cd1);
			kashiUf4Name2 = kanriCategoryLogic.findUfName("4", kashiUf4Cd2);
			kashiUf4Name3 = kanriCategoryLogic.findUfName("4", kashiUf4Cd3);
			kashiUf4Name4 = kanriCategoryLogic.findUfName("4", kashiUf4Cd4);
			kashiUf4Name5 = kanriCategoryLogic.findUfName("4", kashiUf4Cd5);

			kariUf5Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf5Cd, inputSeigyo.shiwakeVarListKariUf5);
			kashiUf5Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf5Cd1, inputSeigyo.shiwakeVarListKashi1Uf5);
			kashiUf5Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf5Cd2, inputSeigyo.shiwakeVarListKashi2Uf5);
			kashiUf5Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf5Cd3, inputSeigyo.shiwakeVarListKashi3Uf5);
			kashiUf5Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf5Cd4, inputSeigyo.shiwakeVarListKashi4Uf5);
			kashiUf5Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf5Cd5, inputSeigyo.shiwakeVarListKashi5Uf5);
			kariUf5Name = kanriCategoryLogic.findUfName("5", kariUf5Cd);
			kashiUf5Name1 = kanriCategoryLogic.findUfName("5", kashiUf5Cd1);
			kashiUf5Name2 = kanriCategoryLogic.findUfName("5", kashiUf5Cd2);
			kashiUf5Name3 = kanriCategoryLogic.findUfName("5", kashiUf5Cd3);
			kashiUf5Name4 = kanriCategoryLogic.findUfName("5", kashiUf5Cd4);
			kashiUf5Name5 = kanriCategoryLogic.findUfName("5", kashiUf5Cd5);

			kariUf6Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf6Cd, inputSeigyo.shiwakeVarListKariUf6);
			kashiUf6Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf6Cd1, inputSeigyo.shiwakeVarListKashi1Uf6);
			kashiUf6Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf6Cd2, inputSeigyo.shiwakeVarListKashi2Uf6);
			kashiUf6Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf6Cd3, inputSeigyo.shiwakeVarListKashi3Uf6);
			kashiUf6Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf6Cd4, inputSeigyo.shiwakeVarListKashi4Uf6);
			kashiUf6Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf6Cd5, inputSeigyo.shiwakeVarListKashi5Uf6);
			kariUf6Name = kanriCategoryLogic.findUfName("6", kariUf6Cd);
			kashiUf6Name1 = kanriCategoryLogic.findUfName("6", kashiUf6Cd1);
			kashiUf6Name2 = kanriCategoryLogic.findUfName("6", kashiUf6Cd2);
			kashiUf6Name3 = kanriCategoryLogic.findUfName("6", kashiUf6Cd3);
			kashiUf6Name4 = kanriCategoryLogic.findUfName("6", kashiUf6Cd4);
			kashiUf6Name5 = kanriCategoryLogic.findUfName("6", kashiUf6Cd5);

			kariUf7Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf7Cd, inputSeigyo.shiwakeVarListKariUf7);
			kashiUf7Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf7Cd1, inputSeigyo.shiwakeVarListKashi1Uf7);
			kashiUf7Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf7Cd2, inputSeigyo.shiwakeVarListKashi2Uf7);
			kashiUf7Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf7Cd3, inputSeigyo.shiwakeVarListKashi3Uf7);
			kashiUf7Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf7Cd4, inputSeigyo.shiwakeVarListKashi4Uf7);
			kashiUf7Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf7Cd5, inputSeigyo.shiwakeVarListKashi5Uf7);
			kariUf7Name = kanriCategoryLogic.findUfName("7", kariUf7Cd);
			kashiUf7Name1 = kanriCategoryLogic.findUfName("7", kashiUf7Cd1);
			kashiUf7Name2 = kanriCategoryLogic.findUfName("7", kashiUf7Cd2);
			kashiUf7Name3 = kanriCategoryLogic.findUfName("7", kashiUf7Cd3);
			kashiUf7Name4 = kanriCategoryLogic.findUfName("7", kashiUf7Cd4);
			kashiUf7Name5 = kanriCategoryLogic.findUfName("7", kashiUf7Cd5);

			kariUf8Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf8Cd, inputSeigyo.shiwakeVarListKariUf8);
			kashiUf8Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf8Cd1, inputSeigyo.shiwakeVarListKashi1Uf8);
			kashiUf8Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf8Cd2, inputSeigyo.shiwakeVarListKashi2Uf8);
			kashiUf8Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf8Cd3, inputSeigyo.shiwakeVarListKashi3Uf8);
			kashiUf8Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf8Cd4, inputSeigyo.shiwakeVarListKashi4Uf8);
			kashiUf8Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf8Cd5, inputSeigyo.shiwakeVarListKashi5Uf8);
			kariUf8Name = kanriCategoryLogic.findUfName("8", kariUf8Cd);
			kashiUf8Name1 = kanriCategoryLogic.findUfName("8", kashiUf8Cd1);
			kashiUf8Name2 = kanriCategoryLogic.findUfName("8", kashiUf8Cd2);
			kashiUf8Name3 = kanriCategoryLogic.findUfName("8", kashiUf8Cd3);
			kashiUf8Name4 = kanriCategoryLogic.findUfName("8", kashiUf8Cd4);
			kashiUf8Name5 = kanriCategoryLogic.findUfName("8", kashiUf8Cd5);

			kariUf9Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf9Cd, inputSeigyo.shiwakeVarListKariUf9);
			kashiUf9Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf9Cd1, inputSeigyo.shiwakeVarListKashi1Uf9);
			kashiUf9Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf9Cd2, inputSeigyo.shiwakeVarListKashi2Uf9);
			kashiUf9Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf9Cd3, inputSeigyo.shiwakeVarListKashi3Uf9);
			kashiUf9Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf9Cd4, inputSeigyo.shiwakeVarListKashi4Uf9);
			kashiUf9Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf9Cd5, inputSeigyo.shiwakeVarListKashi5Uf9);
			kariUf9Name = kanriCategoryLogic.findUfName("9", kariUf9Cd);
			kashiUf9Name1 = kanriCategoryLogic.findUfName("9", kashiUf9Cd1);
			kashiUf9Name2 = kanriCategoryLogic.findUfName("9", kashiUf9Cd2);
			kashiUf9Name3 = kanriCategoryLogic.findUfName("9", kashiUf9Cd3);
			kashiUf9Name4 = kanriCategoryLogic.findUfName("9", kashiUf9Cd4);
			kashiUf9Name5 = kanriCategoryLogic.findUfName("9", kashiUf9Cd5);

			kariUf10Cd = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kariUf10Cd, inputSeigyo.shiwakeVarListKariUf10);
			kashiUf10Cd1 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf10Cd1, inputSeigyo.shiwakeVarListKashi1Uf10);
			kashiUf10Cd2 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf10Cd2, inputSeigyo.shiwakeVarListKashi2Uf10);
			kashiUf10Cd3 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf10Cd3, inputSeigyo.shiwakeVarListKashi3Uf10);
			kashiUf10Cd4 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf10Cd4, inputSeigyo.shiwakeVarListKashi4Uf10);
			kashiUf10Cd5 = myLogic.ConvertShiwakeVarCd2Nm(shiwakePatternMaster.kashiUf10Cd5, inputSeigyo.shiwakeVarListKashi5Uf10);
			kariUf10Name = kanriCategoryLogic.findUfName("10", kariUf10Cd);
			kashiUf10Name1 = kanriCategoryLogic.findUfName("10", kashiUf10Cd1);
			kashiUf10Name2 = kanriCategoryLogic.findUfName("10", kashiUf10Cd2);
			kashiUf10Name3 = kanriCategoryLogic.findUfName("10", kashiUf10Cd3);
			kashiUf10Name4 = kanriCategoryLogic.findUfName("10", kashiUf10Cd4);
			kashiUf10Name5 = kanriCategoryLogic.findUfName("10", kashiUf10Cd5);

			kariUfKotei1Cd = shiwakePatternMaster.kariUfKotei1Cd;
			kashiUfKotei1Cd1 = shiwakePatternMaster.kashiUfKotei1Cd1;
			kashiUfKotei1Cd2 = shiwakePatternMaster.kashiUfKotei1Cd2;
			kashiUfKotei1Cd3 = shiwakePatternMaster.kashiUfKotei1Cd3;
			kashiUfKotei1Cd4 = shiwakePatternMaster.kashiUfKotei1Cd4;
			kashiUfKotei1Cd5 = shiwakePatternMaster.kashiUfKotei1Cd5;
			kariUfKotei1Name = kanriCategoryLogic.findUfKoteiName("1", kariUfKotei1Cd);
			kashiUfKotei1Name1 = kanriCategoryLogic.findUfKoteiName("1", kashiUfKotei1Cd1);
			kashiUfKotei1Name2 = kanriCategoryLogic.findUfKoteiName("1", kashiUfKotei1Cd2);
			kashiUfKotei1Name3 = kanriCategoryLogic.findUfKoteiName("1", kashiUfKotei1Cd3);
			kashiUfKotei1Name4 = kanriCategoryLogic.findUfKoteiName("1", kashiUfKotei1Cd4);
			kashiUfKotei1Name5 = kanriCategoryLogic.findUfKoteiName("1", kashiUfKotei1Cd5);
			
			kariUfKotei2Cd = shiwakePatternMaster.kariUfKotei2Cd;
			kashiUfKotei2Cd1 = shiwakePatternMaster.kashiUfKotei2Cd1;
			kashiUfKotei2Cd2 = shiwakePatternMaster.kashiUfKotei2Cd2;
			kashiUfKotei2Cd3 = shiwakePatternMaster.kashiUfKotei2Cd3;
			kashiUfKotei2Cd4 = shiwakePatternMaster.kashiUfKotei2Cd4;
			kashiUfKotei2Cd5 = shiwakePatternMaster.kashiUfKotei2Cd5;
			kariUfKotei2Name = kanriCategoryLogic.findUfKoteiName("2", kariUfKotei2Cd);
			kashiUfKotei2Name1 = kanriCategoryLogic.findUfKoteiName("2", kashiUfKotei2Cd1);
			kashiUfKotei2Name2 = kanriCategoryLogic.findUfKoteiName("2", kashiUfKotei2Cd2);
			kashiUfKotei2Name3 = kanriCategoryLogic.findUfKoteiName("2", kashiUfKotei2Cd3);
			kashiUfKotei2Name4 = kanriCategoryLogic.findUfKoteiName("2", kashiUfKotei2Cd4);
			kashiUfKotei2Name5 = kanriCategoryLogic.findUfKoteiName("2", kashiUfKotei2Cd5);

			kariUfKotei3Cd = shiwakePatternMaster.kariUfKotei3Cd;
			kashiUfKotei3Cd1 = shiwakePatternMaster.kashiUfKotei3Cd1;
			kashiUfKotei3Cd2 = shiwakePatternMaster.kashiUfKotei3Cd2;
			kashiUfKotei3Cd3 = shiwakePatternMaster.kashiUfKotei3Cd3;
			kashiUfKotei3Cd4 = shiwakePatternMaster.kashiUfKotei3Cd4;
			kashiUfKotei3Cd5 = shiwakePatternMaster.kashiUfKotei3Cd5;
			kariUfKotei3Name = kanriCategoryLogic.findUfKoteiName("3", kariUfKotei3Cd);
			kashiUfKotei3Name1 = kanriCategoryLogic.findUfKoteiName("3", kashiUfKotei3Cd1);
			kashiUfKotei3Name2 = kanriCategoryLogic.findUfKoteiName("3", kashiUfKotei3Cd2);
			kashiUfKotei3Name3 = kanriCategoryLogic.findUfKoteiName("3", kashiUfKotei3Cd3);
			kashiUfKotei3Name4 = kanriCategoryLogic.findUfKoteiName("3", kashiUfKotei3Cd4);
			kashiUfKotei3Name5 = kanriCategoryLogic.findUfKoteiName("3", kashiUfKotei3Cd5);

			kariUfKotei4Cd = shiwakePatternMaster.kariUfKotei4Cd;
			kashiUfKotei4Cd1 = shiwakePatternMaster.kashiUfKotei4Cd1;
			kashiUfKotei4Cd2 = shiwakePatternMaster.kashiUfKotei4Cd2;
			kashiUfKotei4Cd3 = shiwakePatternMaster.kashiUfKotei4Cd3;
			kashiUfKotei4Cd4 = shiwakePatternMaster.kashiUfKotei4Cd4;
			kashiUfKotei4Cd5 = shiwakePatternMaster.kashiUfKotei4Cd5;
			kariUfKotei4Name = kanriCategoryLogic.findUfKoteiName("4", kariUfKotei4Cd);
			kashiUfKotei4Name1 = kanriCategoryLogic.findUfKoteiName("4", kashiUfKotei4Cd1);
			kashiUfKotei4Name2 = kanriCategoryLogic.findUfKoteiName("4", kashiUfKotei4Cd2);
			kashiUfKotei4Name3 = kanriCategoryLogic.findUfKoteiName("4", kashiUfKotei4Cd3);
			kashiUfKotei4Name4 = kanriCategoryLogic.findUfKoteiName("4", kashiUfKotei4Cd4);
			kashiUfKotei4Name5 = kanriCategoryLogic.findUfKoteiName("4", kashiUfKotei4Cd5);

			kariUfKotei5Cd = shiwakePatternMaster.kariUfKotei5Cd;
			kashiUfKotei5Cd1 = shiwakePatternMaster.kashiUfKotei5Cd1;
			kashiUfKotei5Cd2 = shiwakePatternMaster.kashiUfKotei5Cd2;
			kashiUfKotei5Cd3 = shiwakePatternMaster.kashiUfKotei5Cd3;
			kashiUfKotei5Cd4 = shiwakePatternMaster.kashiUfKotei5Cd4;
			kashiUfKotei5Cd5 = shiwakePatternMaster.kashiUfKotei5Cd5;
			kariUfKotei5Name = kanriCategoryLogic.findUfKoteiName("5", kariUfKotei5Cd);
			kashiUfKotei5Name1 = kanriCategoryLogic.findUfKoteiName("5", kashiUfKotei5Cd1);
			kashiUfKotei5Name2 = kanriCategoryLogic.findUfKoteiName("5", kashiUfKotei5Cd2);
			kashiUfKotei5Name3 = kanriCategoryLogic.findUfKoteiName("5", kashiUfKotei5Cd3);
			kashiUfKotei5Name4 = kanriCategoryLogic.findUfKoteiName("5", kashiUfKotei5Cd4);
			kashiUfKotei5Name5 = kanriCategoryLogic.findUfKoteiName("5", kashiUfKotei5Cd5);

			kariUfKotei6Cd = shiwakePatternMaster.kariUfKotei6Cd;
			kashiUfKotei6Cd1 = shiwakePatternMaster.kashiUfKotei6Cd1;
			kashiUfKotei6Cd2 = shiwakePatternMaster.kashiUfKotei6Cd2;
			kashiUfKotei6Cd3 = shiwakePatternMaster.kashiUfKotei6Cd3;
			kashiUfKotei6Cd4 = shiwakePatternMaster.kashiUfKotei6Cd4;
			kashiUfKotei6Cd5 = shiwakePatternMaster.kashiUfKotei6Cd5;
			kariUfKotei6Name = kanriCategoryLogic.findUfKoteiName("6", kariUfKotei6Cd);
			kashiUfKotei6Name1 = kanriCategoryLogic.findUfKoteiName("6", kashiUfKotei6Cd1);
			kashiUfKotei6Name2 = kanriCategoryLogic.findUfKoteiName("6", kashiUfKotei6Cd2);
			kashiUfKotei6Name3 = kanriCategoryLogic.findUfKoteiName("6", kashiUfKotei6Cd3);
			kashiUfKotei6Name4 = kanriCategoryLogic.findUfKoteiName("6", kashiUfKotei6Cd4);
			kashiUfKotei6Name5 = kanriCategoryLogic.findUfKoteiName("6", kashiUfKotei6Cd5);

			kariUfKotei7Cd = shiwakePatternMaster.kariUfKotei7Cd;
			kashiUfKotei7Cd1 = shiwakePatternMaster.kashiUfKotei7Cd1;
			kashiUfKotei7Cd2 = shiwakePatternMaster.kashiUfKotei7Cd2;
			kashiUfKotei7Cd3 = shiwakePatternMaster.kashiUfKotei7Cd3;
			kashiUfKotei7Cd4 = shiwakePatternMaster.kashiUfKotei7Cd4;
			kashiUfKotei7Cd5 = shiwakePatternMaster.kashiUfKotei7Cd5;
			kariUfKotei7Name = kanriCategoryLogic.findUfKoteiName("7", kariUfKotei7Cd);
			kashiUfKotei7Name1 = kanriCategoryLogic.findUfKoteiName("7", kashiUfKotei7Cd1);
			kashiUfKotei7Name2 = kanriCategoryLogic.findUfKoteiName("7", kashiUfKotei7Cd2);
			kashiUfKotei7Name3 = kanriCategoryLogic.findUfKoteiName("7", kashiUfKotei7Cd3);
			kashiUfKotei7Name4 = kanriCategoryLogic.findUfKoteiName("7", kashiUfKotei7Cd4);
			kashiUfKotei7Name5 = kanriCategoryLogic.findUfKoteiName("7", kashiUfKotei7Cd5);

			kariUfKotei8Cd = shiwakePatternMaster.kariUfKotei8Cd;
			kashiUfKotei8Cd1 = shiwakePatternMaster.kashiUfKotei8Cd1;
			kashiUfKotei8Cd2 = shiwakePatternMaster.kashiUfKotei8Cd2;
			kashiUfKotei8Cd3 = shiwakePatternMaster.kashiUfKotei8Cd3;
			kashiUfKotei8Cd4 = shiwakePatternMaster.kashiUfKotei8Cd4;
			kashiUfKotei8Cd5 = shiwakePatternMaster.kashiUfKotei8Cd5;
			kariUfKotei8Name = kanriCategoryLogic.findUfKoteiName("8", kariUfKotei8Cd);
			kashiUfKotei8Name1 = kanriCategoryLogic.findUfKoteiName("8", kashiUfKotei8Cd1);
			kashiUfKotei8Name2 = kanriCategoryLogic.findUfKoteiName("8", kashiUfKotei8Cd2);
			kashiUfKotei8Name3 = kanriCategoryLogic.findUfKoteiName("8", kashiUfKotei8Cd3);
			kashiUfKotei8Name4 = kanriCategoryLogic.findUfKoteiName("8", kashiUfKotei8Cd4);
			kashiUfKotei8Name5 = kanriCategoryLogic.findUfKoteiName("8", kashiUfKotei8Cd5);

			kariUfKotei9Cd = shiwakePatternMaster.kariUfKotei9Cd;
			kashiUfKotei9Cd1 = shiwakePatternMaster.kashiUfKotei9Cd1;
			kashiUfKotei9Cd2 = shiwakePatternMaster.kashiUfKotei9Cd2;
			kashiUfKotei9Cd3 = shiwakePatternMaster.kashiUfKotei9Cd3;
			kashiUfKotei9Cd4 = shiwakePatternMaster.kashiUfKotei9Cd4;
			kashiUfKotei9Cd5 = shiwakePatternMaster.kashiUfKotei9Cd5;
			kariUfKotei9Name = kanriCategoryLogic.findUfKoteiName("9", kariUfKotei9Cd);
			kashiUfKotei9Name1 = kanriCategoryLogic.findUfKoteiName("9", kashiUfKotei9Cd1);
			kashiUfKotei9Name2 = kanriCategoryLogic.findUfKoteiName("9", kashiUfKotei9Cd2);
			kashiUfKotei9Name3 = kanriCategoryLogic.findUfKoteiName("9", kashiUfKotei9Cd3);
			kashiUfKotei9Name4 = kanriCategoryLogic.findUfKoteiName("9", kashiUfKotei9Cd4);
			kashiUfKotei9Name5 = kanriCategoryLogic.findUfKoteiName("9", kashiUfKotei9Cd5);

			kariUfKotei10Cd = shiwakePatternMaster.kariUfKotei10Cd;
			kashiUfKotei10Cd1 = shiwakePatternMaster.kashiUfKotei10Cd1;
			kashiUfKotei10Cd2 = shiwakePatternMaster.kashiUfKotei10Cd2;
			kashiUfKotei10Cd3 = shiwakePatternMaster.kashiUfKotei10Cd3;
			kashiUfKotei10Cd4 = shiwakePatternMaster.kashiUfKotei10Cd4;
			kashiUfKotei10Cd5 = shiwakePatternMaster.kashiUfKotei10Cd5;
			kariUfKotei10Name = kanriCategoryLogic.findUfKoteiName("10", kariUfKotei10Cd);
			kashiUfKotei10Name1 = kanriCategoryLogic.findUfKoteiName("10", kashiUfKotei10Cd1);
			kashiUfKotei10Name2 = kanriCategoryLogic.findUfKoteiName("10", kashiUfKotei10Cd2);
			kashiUfKotei10Name3 = kanriCategoryLogic.findUfKoteiName("10", kashiUfKotei10Cd3);
			kashiUfKotei10Name4 = kanriCategoryLogic.findUfKoteiName("10", kashiUfKotei10Cd4);
			kashiUfKotei10Name5 = kanriCategoryLogic.findUfKoteiName("10", kashiUfKotei10Cd5);

			kariKazeiKbn = shiwakePatternMaster.kariKazeiKbn;
			kashiKazeiKbn1 = shiwakePatternMaster.kashiKazeiKbn1;
			kashiKazeiKbn2 = shiwakePatternMaster.kashiKazeiKbn2;
			kashiKazeiKbn3 = shiwakePatternMaster.kashiKazeiKbn3;
			kashiKazeiKbn4 = shiwakePatternMaster.kashiKazeiKbn4;
			kashiKazeiKbn5 = shiwakePatternMaster.kashiKazeiKbn5;

			kariZeiritsu = shiwakePatternMaster.kariZeiritsu;
			kariKeigenZeiritsuKbn = shiwakePatternMaster.kariKeigenZeiritsuKbn;

			kariBunriKbn = shiwakePatternMaster.kariBunriKbn;
			kashiBunriKbn1 = shiwakePatternMaster.kashiBunriKbn1;
			kashiBunriKbn2 = shiwakePatternMaster.kashiBunriKbn2;
			kashiBunriKbn3 = shiwakePatternMaster.kashiBunriKbn3;
			kashiBunriKbn4 = shiwakePatternMaster.kashiBunriKbn4;
			kashiBunriKbn5 = shiwakePatternMaster.kashiBunriKbn5;

			kariShiireKbn = shiwakePatternMaster.kariShiireKbn;
			kashiShiireKbn1 = shiwakePatternMaster.kashiShiireKbn1;
			kashiShiireKbn2 = shiwakePatternMaster.kashiShiireKbn2;
			kashiShiireKbn3 = shiwakePatternMaster.kashiShiireKbn3;
			kashiShiireKbn4 = shiwakePatternMaster.kashiShiireKbn4;
			kashiShiireKbn5 = shiwakePatternMaster.kashiShiireKbn5;
			
			oldKariKazeiKbn = shiwakePatternMaster.oldKariKazeiKbn;
			oldKashiKazeiKbn1 = shiwakePatternMaster.oldKashiKazeiKbn1;
			oldKashiKazeiKbn2 = shiwakePatternMaster.oldKashiKazeiKbn2;
			oldKashiKazeiKbn3 = shiwakePatternMaster.oldKashiKazeiKbn3;
			oldKashiKazeiKbn4 = shiwakePatternMaster.oldKashiKazeiKbn4;
			oldKashiKazeiKbn5 = shiwakePatternMaster.oldKashiKazeiKbn5;

			// 型変更は後で
			init_Ext(shiwakePatternMaster.map);

			//5.戻り値を返す
			return "success";
		}finally{
			connection.close();
		}
	}

	/**
	 * 更新イベント処理
	 * @return ResultName
	 */
	public String koushin() {
		if(!gaibuYobidashiFlag) {
			connection = EteamConnection.connect();
		}
		else {
			connection = getGaibuConnection();
		}
		try {
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
			ShiwakePatternMaster dto = super.createDto();
			boolean isInvoiceStarted = super.isInvoiceStarted();
			//20231030 旧課税区分にも常に新課税区分が格納されるよう変更
			var currentDto = this.shiwakePatternMasterDao.find(denpyouKbn, dto.shiwakeEdano);
			dto.oldKariKazeiKbn = dto.kariKazeiKbn;
			dto.oldKashiKazeiKbn1 = dto.kashiKazeiKbn1;
			dto.oldKashiKazeiKbn2 = dto.kashiKazeiKbn2;
			dto.oldKashiKazeiKbn3 = dto.kashiKazeiKbn3;
			dto.oldKashiKazeiKbn4 = dto.kashiKazeiKbn4;
			dto.oldKashiKazeiKbn5 = dto.kashiKazeiKbn5;
			
			putDataMap_Ext(dto.map); // 仮
			this.shiwakePatternMasterDao.update(dto, dto.koushinUserId);
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
	 * 初期化の拡張
	 * @param shiwakePatternMaster 仕訳パターンマスターの値
	 */
	public void init_Ext(GMap shiwakePatternMaster) {
	}

	/**
	 * データマップ更新の拡張
	 * @param dataMap データマップ
	 */
	public void putDataMap_Ext(GMap dataMap) {}
}
