package eteam.gyoumu.kaikei.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.MasterKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ヘッダーフィールド選択ダイアログAction
 */
@Getter @Setter @ToString
public class HeaderSentakuAction extends EteamAbstractAction {

//＜定数＞
	/** noのドメイン */
	static final String[] HF_NO_DOMAIN = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

//＜画面入力＞
	/** HF NO(1-10) */
	String no;
	
//＜画面入力以外＞
	/** ヘッダーフィールドコード */
	String hfCd;
	/** HF一覧 */
	List<GMap> hfList;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkDomain(no, HF_NO_DOMAIN, "HF NO", true);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目							EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{no ,"HF NO"			,"2"},
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		//1.入力チェック
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			MasterKanriCategoryLogic masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			//なし

			//3.アクセス可能チェック
			//なし

			//4.処理
			hfList = masterLg.loadHF(no);
			if(hfList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 名称取得イベント
	 * @return 処理結果
	 */
	public String getName() {

		try(EteamConnection connection = EteamConnection.connect()) {
			MasterKanriCategoryLogic masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			String name = masterKanriLogic.findHfName(no, hfCd);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.print(name);
			out.flush();
			out.close();

		} catch (IOException ex) {

		}

		return "success";
	}
}
