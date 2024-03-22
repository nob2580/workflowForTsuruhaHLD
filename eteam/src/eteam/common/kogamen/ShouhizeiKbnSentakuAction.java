package eteam.common.kogamen;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.database.abstractdao.KiShouhizeiSettingAbstractDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;

/**
 * 消費税区分取得専用クラス
 * @author j_matsumoto
 */
public class ShouhizeiKbnSentakuAction extends EteamAbstractAction {
	
	/** 伝票ID */
	String denpyouId;

	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}
	
	//＜イベント＞
	/**
	 * 初期表示イベント（取得専用につき使用せず）
	 * @return 処理結果
	 */
	public String init(){
		return "success";
	}
	
	/**
	 * 消費税区分/仕入税額按分取得イベント
	 * @return 処理結果
	 */
	public String getShouhizeiKbn() {

		try(EteamConnection connection = EteamConnection.connect()) {
			KiShouhizeiSettingAbstractDao kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
			KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			var kiShouhizeiSettingDto = kiShouhizeiSettingDao.findByDate(toDate(kaikeiCommonLogic.getKiDate(denpyouId)));
			//DenpyouMeisaiAction.javaではfindByDate(null)としている（常に消費税設定テーブルの最新を取る）

			GMap map = new GMap();
			map.put("shouhizeiKbn", kiShouhizeiSettingDto == null ? null : kiShouhizeiSettingDto.shouhizeiKbn);
			map.put("shiireZeigakuAnbunFlg", kiShouhizeiSettingDto == null ? null : kiShouhizeiSettingDto.shiireZeigakuAnbunFlg);
			
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
