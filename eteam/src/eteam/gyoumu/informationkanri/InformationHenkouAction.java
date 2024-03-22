package eteam.gyoumu.informationkanri;

import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.select.InformationKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * インフォメーション変更画面Action
 */
@Getter @Setter @ToString
public class InformationHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 掲示開始日 */
	String keijiKikanFrom;
	/** 掲示終了日 */
	String keijiKikanTo;
	/** 通知内容 */
	String tsuuchinaiyou;
	/** インフォメーションID */
	String informationId;
	/** 掲示期間ソート区分（一覧への遷移用） */
	String sortKbn;
	/** ステータス（一覧への遷移用） */
	String status;
	/** ページ番号（一覧への遷移用） */
	String pageNo;
	

//＜画面入力以外＞
	/** 更新ユーザーID */
	String userId;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(informationId, 1, 14, "informationId", true);
		checkString(sortKbn, 0, 1, "sortKbn", true);
		checkString(status, 0, 1, "status", true);
		checkString(pageNo, 0, 10, "pageNo", true);
		checkDate(keijiKikanFrom, "掲示期間開始日", false);
		checkDate(keijiKikanTo,   "掲示期間終了日", false);
		checkString(tsuuchinaiyou, 1, 3000, "掲示内容", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{informationId ,"informationId"		,"2", "2", },
				{sortKbn ,"sortKbn"				,"2", "2", },
				{status ,"status"				,"2", "2", },
				{pageNo ,"pageNo"				,"2", "2", },
				{keijiKikanFrom ,"掲示期間開始日"		,"0", "1", },
				{keijiKikanTo ,"掲示期間終了日"		,"0", "0", },
				{tsuuchinaiyou ,"掲示内容"				,"0", "1", },
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
			InformationKanriCategoryLogic infolc = (InformationKanriCategoryLogic)EteamContainer.getComponent(InformationKanriCategoryLogic.class, connection);
			
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
	
	/**
	 * 変更イベント
	 * @return 処理結果
	 */
	public String henkou() {
		
		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		if(! errorList.isEmpty()){
			return "error";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			var lc = (InformationLogic)EteamContainer.getComponent(InformationLogic.class, connection);
			var infolc = (InformationKanriCategoryLogic)EteamContainer.getComponent(InformationKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			GMap record = infolc.findInfo(informationId);
			if(null == record){
				throw new EteamDataNotFoundException();
			}
			//3.アクセス可能チェック なし
			//4.処理
			/* 掲示期間終了日が入力されていなかった場合、「9999/12/31」を登録。*/
			if(isEmpty(keijiKikanTo)){
				keijiKikanTo = "9999/12/31";
			}
			/* 有効期限の共通チェック */
			List<Map<String, String>> dateCheck = EteamCommon.kigenCheck(keijiKikanFrom, keijiKikanTo);
			for(Map<String, String> map :dateCheck){
				// 開始日と終了日の整合性チェックのみエラーとする。
				if("2".equals(map.get("errorCode"))){
					errorList.add(map.get("errorMassage"));
				}
			}
			/* 通知内容のチェック */
			if(tsuuchinaiyou.replace("\r\n", "").length() == 0){
				errorList.add("改行のみのインフォメーション登録はできません。掲示内容を変更してください。");
			}
			// エラーがあった場合、エラーメッセージを自画面に表示し、後続処理は動きません。
			if(! errorList.isEmpty()){
				return "error";
			}
			/* 更新ユーザーIDをセッションより取得 */
			userId = getUser().getTourokuOrKoushinUserId();
			/* DBの更新処理 */
			lc.update(
					informationId,
					toDate(keijiKikanFrom),
					toDate(keijiKikanTo),
					tsuuchinaiyou,
					userId);

			connection.commit();

			//5.戻り値を返す
			return "success";
		}
	}
}
