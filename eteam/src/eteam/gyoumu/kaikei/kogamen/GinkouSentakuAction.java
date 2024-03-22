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
public class GinkouSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	//AJAX用
	/** 銀行コード */
	String ginkouCd;
//＜画面入力以外＞
	/** 銀行一覧 */
	List<GMap> list;
	/** マスター検索か */
	String isMasterSearch;
	
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
			boolean isMaster = isNotEmpty(this.isMasterSearch) && this.isMasterSearch.equals("1");
			list = isMaster
					? masterLg.loadWFKinyuukikan()
					: masterLg.loadOpen21Kinyuukikan();
			
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

			boolean isMaster = isNotEmpty(this.isMasterSearch) && this.isMasterSearch.equals("1");
			String name = isMaster ? masterLg.findWFKinyuukikanName(ginkouCd) : masterLg.findKinyuukikanName(ginkouCd);
			name = !isMaster
					? name
					: name == null
						? ""
						: name + "exists"; // マスターの時、銀行が存在するが空文字の場合は存在を示す文字を仕込む

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
