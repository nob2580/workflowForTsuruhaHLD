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
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引先選択ダイアログAction
 */
@Getter @Setter @ToString
public class TorihikisakiSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 取引先コード */
	String torihikisakiCd;
	/** 取引先名 */
	String torihikisakiName;
	/** 伝票区分(呼びもとから任意で指定される、仕入先フラグを紐つける為のもの) */
	String denpyouKbn;
	
//＜画面入力以外＞
	/** 取引先リスト */
	List<GMap> torihikisakiList;
	
	/** システムカテゴリーロジック */
	SystemKanriCategoryLogic systemLogic;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(torihikisakiCd,    1, 12,   "取引先コード",    false);
		checkString(torihikisakiName,  1, 20,   "取引先名",        false);
	}

	@Override 
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目										EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{torihikisakiCd ,"取引先コード"		,"0", "0"},
				{torihikisakiName ,"取引先名"			,"0", "0"},
			};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		//初期表示は入力なしの検索である
		return kensaku();
	}
	
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		//1.入力チェック
		hissuCheck(2);
		formatCheck();
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			MasterKanriCategoryLogic masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			KaikeiCommonLogic commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			int numPerPage = Integer.parseInt(setting.recordNumPerPageTorihikisaki());

			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理

			torihikisakiList = masterKanriLogic.selectTorihikisakiJouhou(torihikisakiCd, torihikisakiName, findShiiresakiFlg(connection), isEnableIchigensaki());
			if(torihikisakiList.size() == 0) {
				errorList.add("検索結果は０件です。");
			}
			if(!errorList.isEmpty()){ return "error"; }

			//口座情報が紐ついていたら表示させる
			for(GMap map : torihikisakiList) {
				map.put("furikomisaki", commonLg.furikomisakiSakusei(map));
			}

			//件数オーバーならメッセージ表示
			if (numPerPage + 1 == torihikisakiList.size()) {
				errorList.add("検索結果が" + numPerPage + "件を超えています。取引先が見つからない場合、検索条件を入力して検索してください。");
				torihikisakiList.remove(numPerPage);
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
			KaikeiCommonLogic commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			WorkflowEventControlLogic WfLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);

			String name = masterKanriLogic.findTorihikisakiName(torihikisakiCd, findShiiresakiFlg(connection));
			if(isEmpty(name) && setting.ichigenCd().equals(torihikisakiCd)) {
				// 一見先の選択を許可する伝票区分なら名称を再検索
				if(isEnableIchigensaki()) name = masterKanriLogic.findTorihikisakiName(torihikisakiCd);
			}
			String invoiceStartFlg = WfLogic.judgeinvoiceStart();
			String furikomisaki = "";
			String jigyoushaKbn = "0";
			if(!isEmpty(name)){
				GMap master = masterKanriLogic.findTorihikisakiJouhou(torihikisakiCd, false);
				if(master != null) {
					furikomisaki = commonLg.furikomisakiSakusei(master);
					jigyoushaKbn = invoiceStartFlg.equals("1") ? (String)master.get("menzei_jigyousha_flg") : "0";
				}
			}

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.print(name);
			out.print("\t");
			out.print(furikomisaki);
			out.print("\t");
			out.print(jigyoushaKbn);
			out.flush();
			out.close();

		} catch (IOException ex) {

		}

		return "success";
	}

	/**
	 * 伝票区分から仕入先フラグを取得
	 * @param conn コネクション
	 * @return 仕入先フラグ
	 */
	protected boolean findShiiresakiFlg(EteamConnection conn) {
		if (isEmpty(denpyouKbn))
		{
			return false;
		}
		KihyouNaviCategoryLogic l = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, conn);
		GMap denpyouKanri = l.findDenpyouKanri(denpyouKbn);
		return denpyouKanri.get("shiiresaki_flg").equals("1");
	}
	
	/**
	 * 伝票区分から一見先の入力が可能か判断
	 * @return 一見先の選択可否
	 */
	protected boolean isEnableIchigensaki() {
		if (DENPYOU_KBN.SIHARAIIRAI.equals(denpyouKbn))
		{
			return true;
		}
		return false;
	}
}
