package eteam.gyoumu.kaikei.kogamen;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.TESUURYOU_FUTAN_KBN;
import eteam.common.EteamNaibuCodeSetting.YOKIN_SHUBETSU;
import eteam.common.select.MasterKanriCategoryExtLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引先選択ダイアログAction
 */
@Getter @Setter @ToString
public class FurikomisakiSentakuAction extends EteamAbstractAction {

	//<画面入力>
	/** 取引先コード */
	String torihikisakiCd;
	
	//＜画面入力以外＞
	/** 振込先リスト */
	List<GMap> furikomisakiList;

	/** システムカテゴリーロジック */
	SystemKanriCategoryLogic systemLogic;
	
	@Override
	protected void formatCheck() {
		checkString(torihikisakiCd,    1, 12,   "取引先コード",    false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目										EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{torihikisakiCd ,"取引先コード"		,"0", "0"},
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
	
	public String kensaku() {
		//1.入力チェック
		hissuCheck(2);
		formatCheck();
		if(!errorList.isEmpty()){ return "error"; }
		try(EteamConnection connection = EteamConnection.connect()){
			MasterKanriCategoryLogic masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

			//2.データ存在チェック
			//3.アクセス可能チェック
			//4.処理

			furikomisakiList = ((MasterKanriCategoryExtLogic)masterKanriLogic).selectFurikomisakiJouhou(torihikisakiCd);
			if(furikomisakiList.size() == 0) {
				errorList.add("検索結果は０件です。");
			}
			if(!errorList.isEmpty()){ return "error"; }

			//各項目に名前を持たせる
			for(GMap map : furikomisakiList) {
				//預金種別
				switch(map.get("yokin_shubetsu").toString()){
					case YOKIN_SHUBETSU.FUTSUU:
						map.put("kouza_shubetsu","普通");
						break;
					case YOKIN_SHUBETSU.TOUZA:
						map.put("kouza_shubetsu","当座");
						break;
					case YOKIN_SHUBETSU.CHOCHIKU:
						map.put("kouza_shubetsu","貯蓄");
						break;
					case YOKIN_SHUBETSU.SONOTA:
						map.put("kouza_shubetsu","その他");
						break;
				}
				//手数料負担
				switch(map.get("tesuuryou").toString()) {
					case TESUURYOU_FUTAN_KBN.JISHA:
						map.put("tesuuryou_futan","自社");
						break;
					case TESUURYOU_FUTAN_KBN.KYAKUSAKI:
						map.put("tesuuryou_futan","客先");
						break;
					case TESUURYOU_FUTAN_KBN.SEPPAN:
						map.put("tesuuryou_futan","折半");
						break;
				}
			}
			//5.戻り値を返す
			return "success";
	}
		
	}
}
