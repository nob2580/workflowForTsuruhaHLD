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
import eteam.gyoumu.kaikei.DaishoMasterCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 振込先選択ダイアログAction
 */
@Getter @Setter @ToString
public class GinkouShitenSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	//初期表示イベントパラメータ
	/** 金融機関コード */
	String kinyuukikanCd;
	/** 金融機関名 */
	String kinyuukikanName;
	//AJAX用
	/** 銀行コード */
	String ginkouCd;
	/** 銀行コード */
	String shitenCd;
	
//＜画面入力以外＞
	/** 支店一覧 */
	List<GMap> list;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] chkList = {
			//項目										EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{kinyuukikanCd, "金融機関コード", "1"},
		};
		hissuCheckCommon(chkList, eventNum);
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
			DaishoMasterCategoryLogic masterLg = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);

			//2.データ存在チェック

			//3.アクセス可能チェック
			//なし

			//4.処理
			list = masterLg.loadOpen21KinyuukikanShiten(kinyuukikanCd);
			if(list.isEmpty()){
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
			DaishoMasterCategoryLogic masterLg = EteamContainer.getComponent(DaishoMasterCategoryLogic.class, connection);

			String name = masterLg.findKinyuukikanShitenName(ginkouCd, shitenCd);

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
