package eteam.common.kogamen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamEkispertCommon;
import eteam.common.RegAccess;
import eteam.common.RegAccess.REG_KEY_NAME;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 乗換案内検索ダイアログAction
 */
@Getter @Setter @ToString
public class NorikaeAnnaiKensakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(StationSentakuAction.class);

	/** 呼出URL */
	protected String callURL;

	/** イントラ版フラグ(イントラ：true, WebService：false) */
	protected String isIntra = setting.intraFlg();

	//※交通手段のチェック・ラジオはクライアント側だけで持つ。初期表示以外でダイアログをAJAX再表示したりすることがないので、サーバー側で持つ必要なし。
	/** INパラメータ:イントラ版起動URL */
	protected String intraAction;
	/** INパラメータ：戻り先URL */
	protected String val_cgi_url;
	/** 検索モード(1:運賃, 2:定期) */
	protected String val_oneway;
	/** ICカード料金算出区分(0：IC料金算出しない, 1:IC料金算出する) */
	protected String val_icticket;
	/** 駅すぱあと表示件数 */
	protected String val_max_result;
	/** 駅すぱあと経路検索表示順リスト */
	protected List<GMap> ekispertSortList;
	/** 定期区間表示フラグ(0:表示しない, 1:表示する) */
	protected String isShowTeikiKukan;
	//20220224 追加
	/** 駅すぱあと予約サービスリスト */
	protected List<GMap> ekispertJrYoyakuList;

	/** 定期用モード(イントラ版で使用) */
	boolean teikiMode = false;

//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {

		try(EteamConnection connection = EteamConnection.connect()){
			//イントラ版向けの処理を追加
			if("1".equals(isIntra)) {
				EteamEkispertCommon etc = new EteamEkispertCommon();
				etc.ekispertInit(connection, teikiMode ? EteamEkispertCommon.SEARCH_MODE_TEIKI : EteamEkispertCommon.SEARCH_MODE_UNCHIN);
				this.intraAction = etc.getIntraAction();
				this.val_cgi_url = etc.getVal_cgi_url();
				this.val_oneway = etc.getVal_oneway();
				this.val_icticket = etc.getVal_icticket();//初期値は会社設定から→ユーザが変更可能※会社設定により変更不可能
				this.val_max_result = etc.getVal_max_result();
				this.ekispertSortList = etc.getEkispertSortList();
				this.isShowTeikiKukan = etc.getIsShowTeikiKukan();
				this.ekispertJrYoyakuList = etc.getEkispertJrYoyakuList();
			}


			//5.戻り値を返す
			return "success";
		} catch (Exception e) {
			log.error(e);
			errorList.add("WEB APIでエラーが発生しました。");
			return "error";

		}
	}


	/**
	 * 指定されたURLにキー情報を付与し、駅すぱあとAPI呼出
	 * 結果を無加工で返却
	 * @return 処理結果
	 */
	public String callApi() {
		try {

			//URLのキー指定をレジストリから取得、設定
			String parameterKey = RegAccess.readRegistory(REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[0] + EteamCommon.getContextSchemaName(), REG_KEY_NAME.EKISPERT_WEBSERVICE_KEY[1]);
			URL url = new URL(callURL.replaceFirst("key=", parameterKey));

			//駅すぱあとAPIを諸届サーバから呼出
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.setInstanceFollowRedirects(false);
			urlConn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
			urlConn.connect();

			if(urlConn.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) {
				HttpServletResponse response = ServletActionContext.getResponse();
				response.sendError(403);
				throw new RuntimeException("WEB APIからコード403が返却されました。");
			}else if(urlConn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
				HttpServletResponse response = ServletActionContext.getResponse();
				response.sendError(400);
				throw new RuntimeException("WEB APIからコード400が返却されました。");
			}

			//結果レスポンスを返却
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			BufferedReader brStd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"utf-8"));
			String tmpStr;
			PrintWriter out = response.getWriter();
			while((tmpStr = brStd.readLine()) != null) {
				out.print(tmpStr);
			}
			out.flush();
			out.close();

		} catch (Exception e) {
			log.error(e);
		}
		return "success";
	}

}
