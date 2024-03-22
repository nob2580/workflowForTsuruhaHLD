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
 * セグメント選択ダイアログAction
 */
@Getter @Setter @ToString
public class SegmentSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	
//＜画面入力以外＞
	/** セグメントコード */
	String segmentCd;
	/** 一覧 */
	List<GMap> list;

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
			list = mkc.loadSegment();
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
			String name = masterKanriLogic.findSegmentName(segmentCd);

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
