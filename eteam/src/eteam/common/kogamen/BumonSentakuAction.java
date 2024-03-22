package eteam.common.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門検索ダイアログAction
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** 一覧 */
	List<GMap> list;
	/** 全件取得フラグ */
	String isAllDate;
	/** 部門コード */
	String bumonCd;
	/** 基準日(ダイアログ側) */
	String d_kijunBi;
	
//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){

		//1.入力チェック
		
		//d_kijunBiが日付型に変換できるかチェック
		checkDate(d_kijunBi,"基準日",false);
		if(! errorList.isEmpty()){
			return "error";
		}

		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			//3.アクセス可能チェック

			//4.処理（初期表示）
			
			//所属部門のすべてのデータを取得します。
			boolean isExpirationDate = (! "true".equals(isAllDate));
			
			//基準日がある前提で統一(設定無ければ現在日とする)
			
			if(!isEmpty(d_kijunBi)){
				Date kDt = toDate(d_kijunBi);
				list = bukc.selectBumonTreeWithKijunbi("",kDt);
			}else{
				list = bukc.selectBumonTreeStructure("", isExpirationDate);
			}
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//共通機能の部門リスト編集を呼び出す
			for (GMap map : list) {
				//日付を表示用フォーマットに変換する(共通機能用)
				map.put("yuukou_kigen_from", formatDate(map.get("yuukou_kigen_from")));
				map.put("yuukou_kigen_to", formatDate(map.get("yuukou_kigen_to")));
			}
			if(!isEmpty(d_kijunBi)){
				list = EteamCommon.bumonListHenshuu(connection, list, toDate(d_kijunBi));
			}else{
				list = EteamCommon.bumonListHenshuu(connection, list);
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
			BumonUserKanriCategoryLogic bukc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			GMap map;
			if("true".equals(isAllDate)){
				map = bukc.selectValidShozokuBumon(bumonCd);
				if(map == null){
					map = bukc.findShozokuBumonLatest(bumonCd);
				}
			}else{
				map = bukc.selectValidShozokuBumon(bumonCd);
			}

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.print(map == null ? "" : map.get("bumon_name"));
			out.flush();
			out.close();

		} catch (IOException ex) {

		}

		return "success";
	}
}
