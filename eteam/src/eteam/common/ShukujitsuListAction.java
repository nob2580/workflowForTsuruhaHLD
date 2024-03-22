package eteam.common;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.HttpHeaderName;
import eteam.common.EteamConst.HttpHeaderValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 祝日定義リスト(JavaScript)取得Action
 */
@Getter @Setter @ToString
public class ShukujitsuListAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(ShukujitsuListAction.class);
	
	
//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 祝日リストを取得してshukujitsuListJsに格納
	 * @return 処理結果
	 */
	public String loadShukujitsuList() {

		//祝日リストのスクリプト作る・・・処理本体でエラーが出てもなるべくブランクを返す
		String shStr = "";
		try{
			shStr = makeScript();
		} catch(Throwable e){
			log.error("祝日リストのスクリプト作成でエラー", e);
		}

		//レスポンス
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(ContentType.JS);
			response.setHeader(HttpHeaderName.CACHE, HttpHeaderValue.CACHE);
			PrintWriter out = response.getWriter();
			out.write(shStr);
			out.close();
			return "success";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 返却文字列を作る 例）shukujitsuList = [19801231,19810101];
	 * @return 返却文字列
	 */
	protected String makeScript(){
		try(EteamConnection connection = EteamConnection.connect()){
			ShukujitsuListLogic myLogic = EteamContainer.getComponent(ShukujitsuListLogic.class, connection);
			
			String shStr = "shukujitsuList = [";
			List<GMap> shList = myLogic.selectShukujitsuList();
			for(GMap mp : shList){
				String shDt = new SimpleDateFormat("yyyyMMdd").format(mp.get("shukujitsu"));
				//未使用
				//String shDtName = mp.get("shukujitsu_name").toString();
				shStr += "\"" + shDt + "\",";
			}
			
			if(",".equals(shStr.substring( shStr.length() - 1, shStr.length()) ) ){
				shStr = shStr.substring( 0, shStr.length() - 1 );
			}
			
			shStr +="];";
			return shStr;
		}
	}
}
