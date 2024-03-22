package eteam.gyoumu.bumonkanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.select.BumonUserKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部門一覧画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class BumonIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 基準日 */
	String kijunBi;
	/** 部門表示フラグ */
	String hyoujiFlg;

//＜画面入力以外＞
	/** 部門一覧 */
	List<GMap> bumonList;

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
		
		//kijunBiが日付型に変換できるかチェック
		checkDate(kijunBi,"基準日",false);
		if(! errorList.isEmpty()){
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			bumonList = bumonUserLogic.selectBumonTreeWithKijunbi("", new java.sql.Date(System.currentTimeMillis()));
			if(bumonList.isEmpty()){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			//4.処理（初期表示）
			// 所属部門の全データを取得します。
			// 日付を表示用フォーマットに変換します。
			for (GMap map : bumonList) {
				map.put("yuukou_kigen_from", formatDate(map.get("yuukou_kigen_from")));
				map.put("yuukou_kigen_to", formatDate(map.get("yuukou_kigen_to")));
			}
			
			// 共通機能の部門リスト編集を呼びます。
			bumonList = EteamCommon.bumonListHenshuu(connection, bumonList);
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 基準日入力
	 * @return ResultName
	 */
	public String kijun(){
		//1.入力チェック
		
		//kijunBiが日付型に変換できるかチェック
		checkDate(kijunBi,"基準日",false);
		if(! errorList.isEmpty()){
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			bumonList = bumonUserLogic.selectBumonTreeWithKijunbi("", toDate(kijunBi) == null ? new java.sql.Date(System.currentTimeMillis()) : toDate(kijunBi));
			if(bumonList.isEmpty()){
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			//4.処理（初期表示）
			// 所属部門の全データを取得します。
			// 日付を表示用フォーマットに変換します。
			for (GMap map : bumonList) {
				map.put("yuukou_kigen_from", formatDate(map.get("yuukou_kigen_from")));
				map.put("yuukou_kigen_to", formatDate(map.get("yuukou_kigen_to")));
			}
			
			// 共通機能の部門リスト編集を呼びます。
			bumonList = EteamCommon.bumonListHenshuu(connection, bumonList);
			
			//5.戻り値を返す
			return "success";
		}
	}
}
