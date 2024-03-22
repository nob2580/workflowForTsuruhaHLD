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
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.KiKesnAbstractDao;
import eteam.database.abstractdao.KiShouhizeiSettingAbstractDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiKesnDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.KamokuMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 勘定科目選択ダイアログAction
 */
@Getter @Setter @ToString
public class KanjyouKamokuSentakuAction extends EteamAbstractAction {

//＜定数＞
//＜画面入力＞
	
//＜画面入力以外＞
	
	/** 一覧 */
	List<KamokuMaster> list;
	/** 勘定科目コード */
	String kamokuCd;
	/** 伝票ID */
	String denpyouId;
	/** 現預金だけ */
	boolean onlyGenyokin;
	/** 科目マスターDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** （期別）消費税設定Dao */
	KiShouhizeiSettingAbstractDao kiShouhizeiSettingDao;
	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommonLogic;
	/** 決算期Dao */
	KiKesnAbstractDao kiKesnDao;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		//1.入力チェック なし
		try(EteamConnection connection = EteamConnection.connect()){
			this.kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
					
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理（初期表示）
			list = onlyGenyokin? this.kamokuMasterDao.loadGenyokin() : this.kamokuMasterDao.load();
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
			this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
			this.kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			this.kiKesnDao = EteamContainer.getComponent(KiKesnDao.class, connection);

			var kamokuMaster = onlyGenyokin ? this.kamokuMasterDao.findGenyokin(kamokuCd) : this.kamokuMasterDao.find(kamokuCd);
			boolean isNull = kamokuMaster == null;
			
			GMap map = new GMap();
			map.put("kamokuNameRyakushiki", isNull ? "" : kamokuMaster.kamokuNameRyakushiki);
			map.put("kazeiKbn", (isNull || kamokuMaster.kazeiKbn == null) ? "" : kamokuMaster.kazeiKbn);
			map.put("bunriKbn", (isNull || kamokuMaster.bunriKbn == null) ? "" : kamokuMaster.bunriKbn);
			
			// 仕入区分は期別消費税の仕入税額按分フラグを考慮
			var kesnDto = this.kiKesnDao.findBySingleDate(toDate(this.kaikeiCommonLogic.getKiDate(denpyouId)));
			Integer kesn = kesnDto == null ? null : kesnDto.kesn;
			var kiShouhizeiDto = kesn == null ? null : this.kiShouhizeiSettingDao.find(kesn);
			var shiireZeigakuAnbunFlg = kiShouhizeiDto == null ? 0 : kiShouhizeiDto.shiireZeigakuAnbunFlg;
			map.put("shiireKbn", (isNull || kamokuMaster.shiireKbn == null || shiireZeigakuAnbunFlg == 0) ? "" : kamokuMaster.shiireKbn);
			
			// 科目確定時に処理グループも取得
			map.put("shoriGroup", (isNull || kamokuMaster.shoriGroup == null) ? "" : kamokuMaster.shoriGroup);
			
			// 消費税率
			map.put("zeiritsuKbn", (isNull || kamokuMaster.zeiritsuKbn == null) ? "" : kamokuMaster.zeiritsuKbn);

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
	
	
	/**
	 * 処理グループ取得イベント
	 * @return 処理結果
	 */
	public String getShoriGroup() {

		try(EteamConnection connection = EteamConnection.connect()) {
			this.kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
			var shoriGroup = kamokuMasterDao.find(kamokuCd).shoriGroup;

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.print(shoriGroup);
			out.flush();
			out.close();

		} catch (IOException ex) {

		}

		return "success";
	}
}
