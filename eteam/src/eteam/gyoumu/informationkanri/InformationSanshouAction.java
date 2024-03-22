package eteam.gyoumu.informationkanri;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.select.InformationKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * インフォメーション参照画面Action
 */
@Getter @Setter @ToString
public class InformationSanshouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** インフォメーションID */
	String informationId;

//＜画面入力以外＞
	/** 掲示開始日 */
	String keijiKikanFrom;
	/** 掲示終了日 */
	String keijiKikanTo;
	/** 通知内容 */
	String tsuuchinaiyou;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(informationId, 1, 14, "informationId", true);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{informationId ,"informationId"		,"2" },
			};
		hissuCheckCommon(list, eventNum);
	}
//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){

		//1.入力チェック
		formatCheck();
		hissuCheck(1);
		
		try(EteamConnection connection = EteamConnection.connect()){
			InformationKanriCategoryLogic infolc = EteamContainer.getComponent(InformationKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			GMap record = infolc.findInfo(informationId);
			if(null == record){
				throw new EteamDataNotFoundException();
			}
			
			//4.処理
			//取得済みデータをメンバ変数に詰める。
			keijiKikanFrom = formatDate(record.get("tsuuchi_kikan_from")); //通知期間開始日
			keijiKikanTo = formatDate(record.get("tsuuchi_kikan_to")); //通知期間終了日
			tsuuchinaiyou = (String)record.get("tsuuchi_naiyou"); //通知内容

			//5.戻り値を返す
			return "success";
		}
	}
	
}
