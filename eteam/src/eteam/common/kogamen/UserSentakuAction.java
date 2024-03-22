package eteam.common.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
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
 * ユーザー選択ダイアログAction
 */
@Getter @Setter @ToString
public class UserSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 部門コード */
	String d_bumonCd;
	/** 部門名 */
	String d_bumonName;
	/** 社員番号 */
	String d_shainNo;
	/** ユーザー名 */
	String d_userName;
	//非表示
	/** 表示パラメータ（全件取得） */
	String isAllDate;
	/** 法人カード履歴番号 */
	String houjinRirekiNo;
	//AJAX用
	/** 社員番号 */
	String shainNo;

//＜画面入力以外＞
	/** 部門一覧 */
	List<GMap> bumonList;
	/** ユーザー一覧 */
	List<GMap> userList;
	
//＜入力チェック＞
	@Override protected void formatCheck() {
	}
	@Override protected void hissuCheck(int eventNum) {
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		return kensaku();
	}
	
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic buklc = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(! errorList.isEmpty()){
				return "error";
			}
			
			// 2.データ存在チェック なし
			// 3.アクセス可能チェック なし
			
			// 4.処理
			boolean isExpirationDate = (!"true".equals(isAllDate));
			/* 所属部門のすべてのデータを取得します。 
			 * 親画面からのパラメータ（isAllDate）がtrueだったら部門全検索・falseだったら有効なものを取得
			 */
			bumonList = buklc.selectBumonTreeStructure("", isExpirationDate);
			if(bumonList.isEmpty()){
				errorList.add("部門が存在しません。");
				return "error";
			}
			
			// 共通機能の部門リスト編集を呼びます。
			for (GMap map : bumonList) {
				//日付を表示用フォーマットに変換する(共通機能用)
				map.put("yuukou_kigen_from", formatDate(map.get("yuukou_kigen_from")));
				map.put("yuukou_kigen_to", formatDate(map.get("yuukou_kigen_to")));
			}
			bumonList = EteamCommon.bumonListHenshuu(connection, bumonList);
			
			/* 選択された部門コードをキーにユーザーを取得します。
			 * 親画面で法人カード履歴番号(houjinRirekiNo)が指定されていた場合は
			 * 該当法人カード履歴のカード番号とユーザーの法人カード識別用番号が一致するデータのみ表示
			 * 親画面からのパラメータ（isAllDate）がtrueだったらユーザー全検索・falseだったら有効なものを取得
			 */
			
			if(!isEmpty(houjinRirekiNo)){
				userList = buklc.userSerachHoujinCardRireki(houjinRirekiNo);
			}else{
				userList = buklc.userSerach("", "", "", "", isExpirationDate);
			}
			
			if(userList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			for(GMap map : userList) {
				// 背景色の設定
				map.put("user_bg_color", EteamCommon.bkColorSettei(formatDate(map.get("yuukou_kigen_from")), formatDate(map.get("yuukou_kigen_to"))));
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 社員名称取得イベント
	 * @return 処理結果
	 */
	public String getShainName() {

		try(EteamConnection connection = EteamConnection.connect()) {
			BumonUserKanriCategoryLogic bumonUserKanriCategoryLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			GMap record = bumonUserKanriCategoryLogic.selectShainNo(shainNo);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
			PrintWriter out = response.getWriter();
			
			if (record != null) {
				String name = (String) record.get("user_sei") + "　" + (String) record.get("user_mei");
				String id = (String) record.get("user_id");
				String cardFlg = (String) record.get("houjin_card_riyou_flag");
				String cardNum = (String) record.get("houjin_card_shikibetsuyou_num");
				out.print(name);
				out.print("\t");
				out.print(id);
				out.print("\t");
				out.print(cardFlg);
				out.print("\t");
				out.print(cardNum);
			}
			
			out.flush();
			out.close();
		} catch (IOException ex) {

		}

		return "success";
	}
}
