package eteam.gyoumu.kaikei;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.ShozokuBumonShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShozokuBumonShiwakePatternMasterDao;
import eteam.database.dto.ShozokuBumonShiwakePatternMaster;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門別取引（仕訳）一覧画面Action
 */
@Getter @Setter @ToString
public class BumonbetsuTorihikiIchiranAction extends EteamAbstractAction {

//＜定数＞
	/** ソートアイテム */
	static final String[] SOURT_ITEM_DOMAIN = {
		"hyouji_jun",
		"bunrui1",
		"bunrui2",
		"bunrui3",
		"torihiki_name",
		"kari_kamoku_cd",
		"default_hyouji_flg"
		};
	/** ソート順 */
	static final String[] SOURT_ORDER_DOMAIN = {"ASC", "DESC"};

//＜画面入力＞
	/** 所属部門コード */
	String searchShozokuBumonCd;
	/** 所属部門名 */
	String searchShozokuBumonNm;
	/** 伝票種別 */
	String searchDenpyouShubetsu;
	/** 取引名 */
	String searchTorihikiNm;
	/** 登録状態 */
	String searchTourokuJyoutai;
	
	/** 外部呼出し制御 */
	boolean gaibuYobidashiFlag=false;
	/** 外部呼出しコネクション */
	EteamConnection gaibuConnection;
	/** 業務ロール処理部門コード */
	String[] gyoumuRoleShozokuBumonCd;
	/** 外部呼出しユーザーId */
	String gaibuKoushinUserId;
	
	//＜非表示項目＞
	/** 所属部門コード */
	String shozokuBumonCd;
	/** 基準日 */
	String kijunBi;
	/** 伝票区分 */
	String denpyouKbn;
	/** 仕訳枝番号※複数項目をカンマ区切りでPOST */
	String shiwakeEdano;
	/** 取引名 */
	String torihikiNm;
	/** 登録状態 */
	String tourokuStatus;
	/** ソート項目 */
	String sortItem;
	/** ソート順 */
	String sortOrder;
	/** イベントURL(遷移元画面) */
	String preEventUrl;

//＜画面入力以外＞
	/** 初期表示は非表示Flag ("0"の場合、非表示。以外は表示) */
	String initHyoujiFlag = "0";
	/** 部門別取引（仕訳）一覧 */
	List<GMap> kensakuKekkaList;
	/** 伝票種別のDropDownList */
	List<GMap> denpyouShubetsuList;
	/** 登録状態のDropDownList */
	List<GMap> tourokuJyoutaiList;
	
//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** システム管理　SELECT */
	SystemKanriCategoryLogic systemLogic;
	/** マスター管理　SELECT */
	MasterKanriCategoryLogic kanriCategoryLogic;
	/** 部門・ユーザー管理　SELECT */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** 会計　SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 取引 */
	TorihikiLogic myLogic;
	/** 所属部門仕訳パターンマスターDAO */
	ShozokuBumonShiwakePatternMasterAbstractDao shozokuBumonShiwakePatternMasterDao;

//＜入力チェック＞
	@Override 
	protected void formatCheck() {
		checkBumonCd(searchShozokuBumonCd, 1, 8, "所属部門コード", false);
		checkString(searchTorihikiNm, 1, 20, "取引名", false);
		checkBumonCd(shozokuBumonCd, 1, 8, "所属部門コード", false);
		checkString(denpyouKbn, 1,  4, "伝票区分", false);
		checkCode(connection, denpyouKbn, "shiwake_pattern_denpyou_kbn", "伝票区分", false);
		checkHankakuEiSuu(denpyouKbn, "伝票区分");
		checkString(torihikiNm, 1, 20, "取引名", false);
		checkNumber(tourokuStatus, 1, 1, "登録状態", false);
		checkDomain(sortItem, SOURT_ITEM_DOMAIN, "ソートアイテム", false);
		checkDomain(sortOrder, SOURT_ORDER_DOMAIN, "ソート順", false);
	}

	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shozokuBumonCd, "所属部門コード", "0", "1", "2", "1",},
				{denpyouKbn, "伝票種別", "0", "1", "2", "1",},
				{shiwakeEdano, "仕訳枝番号", "0", "0", "0", "1",},
				{tourokuStatus, "登録状態", "0", "1", "2", "0",},
				{sortItem, "ソート項目", "0", "2", "2", "0",},
				{sortOrder, "ソート順", "0", "2", "2", "0",},
			};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		connection = EteamConnection.connect();
		try{
			initParts();
			displaySeigyo();

			//1.入力チェック　なし

			//2.データ存在チェック なし

			//4.処理

			//表示イベントではソート指定がないので、デフォルト値セット
			sortItem = "hyouji_jun";
			sortOrder = "ASC";
			
			//5.戻り値を返す
			return "success";
		}finally{
			connection.close();
		}
	}

	/**
	 * 検索イベント
	 * 検索結果の表示/各種項目でのソート
	 * @return 処理結果
	 */
	public String kensaku(){
		connection = EteamConnection.connect();
		try{
			initParts();
			displaySeigyo();

			//1.入力チェック
			formatCheck();
			hissuCheck(2);
			if (0 < errorList.size())
			{
				return "error";
			}

			//2.データ存在チェック なし
			
			//3.アクセス可能チェック
			// 業務ロールユーザのアクセス権をチェック → Ver22.12.02.12　仕様変更により部門制限排除
// String searchGyoumuRoleShozokuBumonCd = bumonUserLogic.getMergeGyoumuRoleShozokuBumonCd(getUser().getGyoumuRoleShozokuBumonCd(), false);
// if(!bumonUserLogic.checkGyoumuRoleShozokuBumonCd(searchGyoumuRoleShozokuBumonCd, shozokuBumonCd)) {
// errorList.add("アクセス権がありません。");
// return "error";
// }

			//4.処理
			// 検索条件を初期化する　※search○○が画面表示用で、searchがつかないのが入力受け取り用。
			// 画面検索時でも、他画面から戻ってきた時でも、処理系統を合わせる為、上記の方針。
			searchShozokuBumonCd = shozokuBumonCd;
			if(StringUtils.isEmpty(kijunBi)) {
				searchShozokuBumonNm = (String)bumonUserLogic.selectValidShozokuBumon(shozokuBumonCd).get("bumon_name");
			}else {
				searchShozokuBumonNm = (String)bumonUserLogic.selectValidShozokuBumon(shozokuBumonCd, toDate(kijunBi)).get("bumon_name");
			}

			searchDenpyouShubetsu = denpyouKbn;
			searchTorihikiNm = torihikiNm;
			searchTourokuJyoutai = tourokuStatus;
			preEventUrl = "bumonbetsu_torihiki_ichiran_kensaku?"  
										+ "shozokuBumonCd=" + URLEncoder.encode(shozokuBumonCd,"UTF-8") + "&"
										+ "kijunBi=" + URLEncoder.encode(kijunBi,"UTF-8") + "&"
										+ "denpyouKbn=" + denpyouKbn + "&"
										+ "tourokuStatus=" + tourokuStatus + "&"
										+ "sortItem=" + sortItem + "&"
										+ "sortOrder=" + sortOrder + "&"
										+ "torihikiNm=" + URLEncoder.encode(torihikiNm,"UTF-8");
			
			// 一覧データを取得する
			
			// 検索条件の設定
			String status = tourokuStatus;
			if ("9".equals(status)) {
				status = "0, 1";
			}
			
			
			// 部門別取引仕訳一覧情報の全データを取得します。 
			kensakuKekkaList = kaikeiLogic.selectBumonbetsuTorihikiIchiran(denpyouKbn, shozokuBumonCd, torihikiNm, status, sortItem, sortOrder);
			if(kensakuKekkaList.size() == 0){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 表示用にデータを変更します。
			for(GMap map : kensakuKekkaList) {
				String strKigenFrom = formatDate(map.get("yuukou_kigen_from"));
				String strKigenTo = formatDate(map.get("yuukou_kigen_to"));
				map.put("yuukou_kigen_from", strKigenFrom);
				map.put("yuukou_kigen_to", strKigenTo);
				map.put("bg_color", EteamCommon.bkColorSettei(strKigenFrom, strKigenTo));
			}

			//初期一覧ヘッダの表示設定
			initHyoujiFlag = "1";

			//5.戻り値を返す
			return "success";
			
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
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
		try{
			initParts();
			displaySeigyo();

			
			
			
			//1.入力チェック
			formatCheck();
			if(!gaibuYobidashiFlag) {
				hissuCheck(3);
			}else{
				hissuCheck(4);
			}
			if (0 < errorList.size())
			{
				return "error";
			}

			//2.データ存在チェック なし

			//3.アクセス可能チェック
			// 業務ロールユーザのアクセス権をチェック → Ver22.12.02.12　仕様変更により部門制限排除
// String searchGyoumuRoleShozokuBumonCd = bumonUserLogic.getMergeGyoumuRoleShozokuBumonCd(gyoumuRoleShozokuBumonCd != null ? gyoumuRoleShozokuBumonCd : getUser().getGyoumuRoleShozokuBumonCd(), false);
// if(!bumonUserLogic.checkGyoumuRoleShozokuBumonCd(searchGyoumuRoleShozokuBumonCd, shozokuBumonCd)) {
// errorList.add("アクセス権がありません。");
// return "error";
// }

			
			//4.処理

			//レコードを削除
			if(!gaibuYobidashiFlag) {
				switch (tourokuStatus) {
				case "0":
					// 未登録を表示している時は、削除対象が存在しない
					break;
				case "1":
				case "9":
					// 登録済みを表示している時は、登録を消してからinsert
					myLogic.deleteShozokuBumonShiwakeInfo(shozokuBumonCd, denpyouKbn, torihikiNm);
					break;
				default:
					throw new EteamIllegalRequestException("不正な登録状態です。tourokuStatus = " + tourokuStatus);
				}
			}else{
				this.shozokuBumonShiwakePatternMasterDao.delete(shozokuBumonCd, denpyouKbn, Integer.parseInt(shiwakeEdano));
			}

			//部門別仕訳パターンマスターの登録処理
			if(!gaibuYobidashiFlag) {
				String [] edanoArr = shiwakeEdano.split(",");
				for (String edano: edanoArr) {
					if (isNotEmpty(edano)) {
						ShozokuBumonShiwakePatternMaster dto = this.createDto(edano);
						this.shozokuBumonShiwakePatternMasterDao.insert(dto, dto.tourokuUserId);
					}
				}
				connection.commit();
			}else{
				ShozokuBumonShiwakePatternMaster dto = this.createDto(this.shiwakeEdano);
				this.shozokuBumonShiwakePatternMasterDao.insert(dto, dto.tourokuUserId);
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
	 * 部品初期化
	 */
	protected void initParts() {
		systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		kanriCategoryLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		myLogic = EteamContainer.getComponent(TorihikiLogic.class, connection);
		this.shozokuBumonShiwakePatternMasterDao = EteamContainer.getComponent(ShozokuBumonShiwakePatternMasterDao.class, connection);
	}

	/**
	 * コンボボックスなどの読込
	 */
	protected void displaySeigyo() {
		denpyouShubetsuList = systemLogic.loadNaibuCdSetting("shiwake_pattern_denpyou_kbn");
		tourokuJyoutaiList = systemLogic.loadNaibuCdSetting("touroku_status");
	}
	
	/**
	 * @param edano 仕訳枝番
	 * @return 所属部門仕訳パターンマスターDto
	 */
	protected ShozokuBumonShiwakePatternMaster createDto(String edano)
	{
		ShozokuBumonShiwakePatternMaster shozokuBumonShiwakePatternMaster = new ShozokuBumonShiwakePatternMaster();
		shozokuBumonShiwakePatternMaster.bumonCd = this.shozokuBumonCd;
		shozokuBumonShiwakePatternMaster.denpyouKbn = this.denpyouKbn;
		shozokuBumonShiwakePatternMaster.shiwakeEdano = Integer.parseInt(edano);
		shozokuBumonShiwakePatternMaster.tourokuUserId = gaibuKoushinUserId != null ? gaibuKoushinUserId : getUser().getTourokuOrKoushinUserId();
		shozokuBumonShiwakePatternMaster.koushinUserId = gaibuKoushinUserId != null ? gaibuKoushinUserId : getUser().getTourokuOrKoushinUserId();
		return shozokuBumonShiwakePatternMaster;
	}
}
