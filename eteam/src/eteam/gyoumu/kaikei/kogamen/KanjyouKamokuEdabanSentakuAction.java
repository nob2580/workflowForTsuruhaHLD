package eteam.gyoumu.kaikei.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.KamokuEdabanZandakaDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dto.KamokuEdabanZandaka;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 勘定科目枝番選択ダイアログAction
 */
@Getter @Setter @ToString
public class KanjyouKamokuEdabanSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 勘定科目コード */
	String kamokuCd;
	
//＜画面入力以外＞
	/** 勘定科目枝番コード */
	String kamokuEdabanCd;
	/** 勘定科目名 */
	String kamokuName;
	/** 伝票区分 */
	String denpyouKbn;
	/** 仕訳枝番 */
	Integer shiwakeEdano;
	/** 一覧 */
	List<KamokuEdabanZandaka> list;
	/** 全件取得フラグ */
	String isAllDate;
	/** 科目枝番残高Dao */
	KamokuEdabanZandakaAbstractDao kamokuEdabanZandakaDao;
	/** 科目マスターDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** 仕訳パターンマスターDAO */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(kamokuCd, 1, 8, "勘定科目コード",   true);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] itemList = {
				//項目	EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{kamokuCd, "勘定科目コード", "2"},
		};
		hissuCheckCommon(itemList, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		//1.入力チェック
		formatCheck();
		hissuCheck(1);
		if (! errorList.isEmpty())
		{
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			this.kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
			this.kamokuEdabanZandakaDao = EteamContainer.getComponent(KamokuEdabanZandakaDao.class, connection);
			
			//2.データ存在チェック
			var kamoku = this.kamokuMasterDao.find(kamokuCd);
			if(kamoku != null) {
				kamokuName = kamoku.kamokuNameRyakushiki;
			}
			
			//3.アクセス可能チェック なし

			//4.処理（初期表示） なし
			list = this.kamokuEdabanZandakaDao.loadByKamokuCd(kamokuCd);
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			return "success";
		}
	}
	
	/**
	 * 名称・区分取得イベント
	 * @return 処理結果
	 */
	public String getNameAndKbn() {

		try(EteamConnection connection = EteamConnection.connect()) {
			this.kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
			this.kamokuEdabanZandakaDao = EteamContainer.getComponent(KamokuEdabanZandakaDao.class, connection);
			this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
			var kamoku = this.kamokuMasterDao.find(kamokuCd);
			var shiwakePattern = (this.denpyouKbn ==null || this.shiwakeEdano == null) ? null : this.shiwakePatternMasterDao.find(denpyouKbn, this.shiwakeEdano);
			KamokuEdabanZandaka kamokuEdabanZandaka = this.kamokuEdabanZandakaDao.find(kamokuCd, kamokuEdabanCd);
			boolean isNull = kamokuEdabanZandaka == null;
			// 選択肢（空値以外）+自動系のリスト
			var kazeiKbnList = List.of("000", "001", "002", "003", "004", "011", "012", "013", "014", "018", "019", "041", "042", "049", "099", "100");
			// 枝番の区分各種がnullもしくはWF未対応の時、仕訳パターンがあれば取引の値をセット（なければ科目）、
			// それ以外なら科目の値。それすらも空の時は空
			String kazeiKbn = (!isNull && kamokuEdabanZandaka.kazeiKbn != null && kazeiKbnList.contains(String.format("%03d",kamokuEdabanZandaka.kazeiKbn)))
					? String.format("%03d",kamokuEdabanZandaka.kazeiKbn)
					: (shiwakePattern != null && !isEmpty(shiwakePattern.kariKazeiKbn) && kazeiKbnList.contains(shiwakePattern.kariKazeiKbn))
						? shiwakePattern.kariKazeiKbn
						: (kamoku.kazeiKbn != null && kazeiKbnList.contains(String.format("%03d",kamoku.kazeiKbn)))
							? String.format("%03d",kamoku.kazeiKbn)
							: "";
			String bunriKbn = (!isNull && kamokuEdabanZandaka.bunriKbn != null) 
					//消費税区分による振替作成の使用有無はjsp側で制御する
					? kamokuEdabanZandaka.bunriKbn.toString()
					: (shiwakePattern != null && !isEmpty(shiwakePattern.kariBunriKbn))
						? shiwakePattern.kariBunriKbn
						: kamoku.bunriKbn != null
							? kamoku.bunriKbn.toString()
							: "";
			
			//マスタから取得した分離区分の値が3の場合、0に変換
			if(bunriKbn.equals("3"))
			{
				bunriKbn = "0";
			}
			
			GMap map = new GMap();
			map.put("edabanName", isNull ? "" : kamokuEdabanZandaka.edabanName);
			map.put("kazeiKbn", kazeiKbn);
			map.put("bunriKbn", bunriKbn);
			
			HttpServletResponse response = ServletActionContext.getResponse();
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();
			out.print(new Gson().toJson(map));
			out.flush();
			out.close();

		} catch (IOException ex) {

		}

		return "success";
	}
}
