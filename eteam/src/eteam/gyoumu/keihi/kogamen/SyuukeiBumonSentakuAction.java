package eteam.gyoumu.keihi.kogamen;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamConst;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.gyoumu.keihi.KeihiMeisaiLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 負担部門 OR 科目 選択ダイアログ
 * 経費明細一覧からは負担部門と科目の選択用に
 * 執行状況一覧からは負担部門の選択用に 使われている
 */
@Getter @Setter @ToString
public class SyuukeiBumonSentakuAction extends EteamAbstractAction {
//＜定数＞

//＜画面入力(hidden)＞
	/** 選択種別 */
	String type;
	/** 対象月 */
	String targetDate;
	/** 集計部門コード(部門選択の時のみ) */
	String syuukeiBumonCd;
//＜画面入力(ユーザー入力)＞
	/** 検索条件 */
	String keyword;
//＜画面入力以外＞
	/** 種別名称 */
	String typeName;
	/** 集計部門一覧 */
	List<GMap> list;
	/** 業務種類 */
	String parent;

	@Override
	protected void formatCheck() {
		checkString (type ,1, 1, "選択種別"			, true);
		checkDate (targetDate ,		"対象日付"			, true);
		checkString (keyword ,1, 20, typeName + "名称"	, false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] _list = {
				//項目				項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{type, "選択種別", "2"},
				{targetDate, "対象日付", "2"},
		};
		hissuCheckCommon(_list, eventNum);

	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			BumonUserKanriCategoryLogic buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			KeihiMeisaiLogic myLogic = EteamContainer.getComponent(KeihiMeisaiLogic.class, connection);
			boolean isNotKeihi = parent.equals("keihi") ? false : true;
			
			//会社設定に従いユーザまたは部門のセキュリティパターン取得
			String userId = getUser().getSeigyoUserId(); //ログインor代行ユーザID
			String setStr = setting.keihimeisaiSecurityPsettei();
			List<Integer> spList = buLogic.selectSecurityPatternList(userId, null);
			
			//アクセスチェック
			//管理者ユーザでない場合セキュリティコードが無ければエラーとする
			if(!(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId()))) {
				if (spList.isEmpty()) throw new EteamAccessDeniedException("セキュリティパターンの設定がないユーザー");
			}
			
			//引数type(親画面から指定)により表示対象切替
			hissuCheck(1);
			formatCheck();
			if (! errorList.isEmpty()) {
				return "error";
			}


			//決算期の取得
			int kesn = myLogic.findKesn(toDate(targetDate));

			//検索
			switch (type) {
			case "1":		// 集計部門は現在未使用です。
				typeName = "集計部門";
				list = myLogic.loadSyuukeiBumon(kesn, spList);
				break;
			case "2":
				typeName = "科目";
				//科目には部門ORユーザのセキュリティパターンをかける
				list = myLogic.loadKamoku(kesn, spList);
				break;
			case "3":
				if (StringUtils.isBlank(syuukeiBumonCd)) {
					throw new EteamIllegalRequestException("集計部門コードが未設定です。");
				}
				//部門には経費明細一覧でユーザー毎セキュリティ設定の場合のみセキュリティパターンをかける
				//それ以外では集計部門が選択された前提で、その配下も明細部門をすべて出す
				if(isNotKeihi || "2".equals(setStr)) {spList.clear();}
				
				typeName = "部門";
				list = myLogic.loadFutanBumon(kesn, spList, syuukeiBumonCd);
				break;
			default:
				throw new EteamIllegalRequestException("不正な種別です。type = " + type);
			}
			if (list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
		
	}
}
