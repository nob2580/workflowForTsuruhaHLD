package eteam.common.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.gyoumu.yosanshikkoukanri.KianbangouBoIchiranLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門検索ダイアログAction
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class KianBangouSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** 一覧 */
	List<GMap> list;
	
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

		try(EteamConnection connection = EteamConnection.connect()){
			KianbangouBoIchiranLogic KianbangouBoIchiranLogic = EteamContainer.getComponent(KianbangouBoIchiranLogic.class, connection);

			//2.データ存在チェック
			//3.アクセス可能チェック

			//4.処理（初期表示）
			
			//所属部門のすべてのデータを取得します。
			list = KianbangouBoIchiranLogic.loadKianBangouSaiban(super.getUser());
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
}
