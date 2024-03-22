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
 * 幣種選択ダイアログAction
 */
@Getter @Setter @ToString
public class HeishuSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	
//＜画面入力以外＞
	
	/** 一覧 */
	List<GMap> list;
	/** 幣種コード */
	String heishuCd;
	
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
			MasterKanriCategoryLogic mkc = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
					
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理（初期表示）
			list = mkc.loadheishuCd();
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
					
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
			GMap ret = masterKanriLogic.findHeishuCd(heishuCd);
			
			String[] dataAry = new String[3];
			
			for (int i = 0; i < dataAry.length; i++) {
				dataAry[i] = "";
			}
			
			if (ret != null) {
				dataAry[0] = o2s(ret.get("heishu_cd")); // 幣種コード（retが帰ってきたかどうか調べる用）
				dataAry[1] = o2s(ret.get("currency_unit")); // 通貨単位
				dataAry[2] = o2s(ret.get("rate")); // レート
			} 
			
			// 配列を改行区切りで文字列化
			String data = String.join("\t", dataAry);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
	
			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();
	
		} catch (IOException ex) {
		}

		return "success";
	}
	
	/**
	 * ヌルはブランクへ、それ以外ならそのまま。
	 * @param s 変換前
	 * @return 変換後
	 */
	protected String o2s(Object s) {
		return (null == s) ? "" : s.toString();
	}
}
