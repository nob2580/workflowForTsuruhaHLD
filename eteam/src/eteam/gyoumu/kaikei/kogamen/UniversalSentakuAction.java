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
 * ユニバーサルフィールド選択ダイアログAction
 */
@Getter @Setter @ToString
public class UniversalSentakuAction extends EteamAbstractAction {

//＜定数＞
	/** noのドメイン */
	static final String[] UF_NO_DOMAIN = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

//＜画面入力＞
	/** 勘定科目コード */
	String kamokuCd;
	/** UF NO(1-10) */
	String no;
	
//＜画面入力以外＞
	/** ユニバーサルフィールドコード */
	String ufCd;
	/** 勘定科目名 */
	String kamokuName;
	/** UF固定フラグ 0:可変UF,1:固定UF */
	String isKotei;
	/** UF一覧 */
	List<GMap> ufList;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(kamokuCd,         1,  8,   "勘定科目コード",   false);
		checkDomain(no, UF_NO_DOMAIN, "UF NO", true);
		checkDomain(isKotei, new String[]{"0", "1"}, "UF固定フラグ", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
			//項目							EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{no ,"UF NO"			,"2"},
			{isKotei ,"UF固定フラグ"		,"2"},
		};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		
		if (isKotei.equals("1") && Integer.parseInt(no) > 10)
		{
			// マッピング処理で11-20が渡される場合はUF固定の1-10として処理する
			no = String.valueOf(Integer.parseInt(no) - 10);
		}
				
		//1.入力チェック
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			MasterKanriCategoryLogic masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			if (isNotEmpty(kamokuCd)) {
				GMap map = masterLg.findKamokuName(kamokuCd);
				if(map != null) {
					kamokuName = (String)map.get("kamoku_name_ryakushiki");
				}
			}

			//3.アクセス可能チェック
			//なし

			//4.処理
			if (isKotei.equals("0"))
			{
				ufList = masterLg.loadUF(no);
			}
			else if(isKotei.equals("1"))
			{
				ufList = masterLg.loadUFKotei(no);
			}
			if(ufList.isEmpty()){
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
			String name = "";
			if (isKotei.equals("0"))
			{
				name = masterKanriLogic.findUfName(no, ufCd);
			}
			else if(isKotei.equals("1"))
			{
				name = masterKanriLogic.findUfKoteiName(no, ufCd);
			}
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
