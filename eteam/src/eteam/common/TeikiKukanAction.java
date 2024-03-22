package eteam.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamConnection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定期区間取得Action
 */
@Getter @Setter @ToString
public class TeikiKukanAction extends EteamEkispertCommon {

	/** 日付 */
	protected String date;
	/** 旅費精算代理起票時の使用者ID */
	protected String dairiShiyouId;

	/**
	 * 定期区間情報を設定する
	 * @return 処理結果
	 */
	public String searchTeiki() {
		try(EteamConnection connection = EteamConnection.connect()) {
			// 定期区間情報を設定
			super.teikiKukanResultSet(connection, date, dairiShiyouId);
			return "success";
		}
	}
	
	
	/**
	 * 定期区間取得イベント(ajax)
	 * @return 処理結果
	 */
	public String getTeikiKukan() {

		try(EteamConnection connection = EteamConnection.connect()) {
			super.teikiKukanResultSet(connection, date, dairiShiyouId);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			if(setting.intraFlg().equals("1")) {
				out.print(intraTeikiKukan);
				out.print("\t");
				out.print(intraRestoreroute);
			}else {
				out.print(webServiceTeiki);
				out.print("\t");
				out.print(webServiceTeikiSerializeData);
			}
			out.flush();
			out.close();

		} catch (IOException ex) {

		}

		return "success";
	}
	
}
