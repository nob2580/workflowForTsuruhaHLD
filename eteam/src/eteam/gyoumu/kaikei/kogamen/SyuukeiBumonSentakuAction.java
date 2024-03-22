package eteam.gyoumu.kaikei.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 集計部門選択ダイアログ
 * 経費明細一覧、執行状況一覧、負担部門選択から呼ばれる
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
	
	/** 伝票ID */
	String denpyouId;
	/** 起票部門コード */
	String kihyouBumonCd;
	
//＜画面入力(ユーザー入力)＞
	/** 検索条件 */
	String keyword;
//＜画面入力以外＞
	/** 種別名称 */
	String typeName;
	/** 集計部門一覧 */
	List<GMap> syuukeiBumonList;
	/** 業務種類 */
	String parent;
	
	/** 処理モード(1:伝票編集 2:全表示 3:伝票検索 4:拠点（検索系セキュリティ無し)) 
	 */
	String mode;
	
	/** マスターSELECT */
	MasterKanriCategoryLogic masterKanriLogic;
	/** ワークフローカテゴリー */
	WorkflowCategoryLogic wfLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic buLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommonLogic;

//TODO フォーマット・必須チェック必要？
	@Override
	protected void formatCheck() {
// checkString (type ,1, 1, "選択種別"			, true);
// checkDate (targetDate ,		"対象日付"			, true);
// checkString (keyword ,1, 20, typeName + "名称"	, false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
// String[][] _list = {
// //項目				項目名 			必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
// {type, "選択種別", "2"},
// {targetDate, "対象日付", "2"},
// };
// hissuCheckCommon(_list, eventNum);

	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			
			//adminユーザなら集計部門指定時以外は全データ取らせる
			boolean adminFlg = false;
			if(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId())) {
				adminFlg = true;
			}
			if("2".equals(mode)) {
				adminFlg = true;
			}
			
			List<Integer> spList = new ArrayList<Integer>();
			boolean isKeihi = "keihi".equals(parent) ? true : false ;
			String setStr = setting.keihimeisaiSecurityPsettei();
			
			if(!"4".equals(mode)) {
				// 拠点以外は起票者or起票部門のセキュリティパターンリスト取得
				String userId;
				if(denpyouId == null || denpyouId.isEmpty()){
					userId = getUser().getSeigyoUserId();
				}else{
					GMap map = wfLogic.selectKihyoushaData(denpyouId);
					if(map != null && map.get("user_id") != null){
						userId = (String)map.get("user_id");
					}else{
						userId = getUser().getSeigyoUserId();
					}
				}
				boolean syuukeiFlag = "1".equals(setting.futanBumonShukeiFilter()) ? true : false;
				if((isKeihi && setStr.equals("1")) || syuukeiFlag){
					spList = buLogic.selectSecurityPatternList(userId,kihyouBumonCd);
					if (spList.isEmpty() && !adminFlg){
						errorList.add("ユーザーまたは部門にセキュリティパターンが設定されていません。");
						return "error";
					}
				}
			}
			
			//管理者ユーザなら全表示させるためセキュリティパターンリストクリア
			if(adminFlg) spList.clear();
			
			//経費明細一覧、執行状況一覧から
			if( targetDate != null && !(targetDate.isEmpty()) ){
				int kesn = masterKanriLogic.findKesn(toDate(targetDate));
				boolean isMeisaiSptn = (isKeihi && "1".equals(setStr))? true : false;
				syuukeiBumonList = masterKanriLogic.loadSyuukeiBumonForKeihiMeisai(kesn, spList, isMeisaiSptn);
			//部門選択から
			}else{
				if( mode == null || !( ("1".equals(mode)) || ("2".equals(mode)) || ("3".equals(mode)) || ("4".equals(mode))) ){
					throw new EteamAccessDeniedException("モード指定異常");
				}
				
				//決算期番号リスト
				List<Integer> kiList = getKiList(Integer.parseInt(mode));
				
				//集計部門名を翌期優先でそれぞれ1件ずつ取得
				syuukeiBumonList = masterKanriLogic.loadSyuukeiBumonFromSptn(spList, kiList, null);
			}
			
			if(syuukeiBumonList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
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
			initParts(connection);
			
			//adminユーザなら集計部門指定時以外は全データ取らせる
			boolean adminFlg = false;
			if(EteamConst.GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId())) {
				adminFlg = true;
			}
			if("2".equals(mode)) {
				adminFlg = true;
			}

			List<Integer> spList = new ArrayList<Integer>();
			if(!"4".equals(mode)) {
				// 拠点以外は起票者or起票部門のセキュリティパターンリスト取得
				String userId;
				if(denpyouId == null || denpyouId.isEmpty()){
					userId = getUser().getSeigyoUserId();
				}else{
					GMap map = wfLogic.selectKihyoushaData(denpyouId);
					if(map != null && map.get("user_id") != null){
						userId = (String)map.get("user_id");
					}else{
						userId = getUser().getSeigyoUserId();
					}
				}
				boolean syuukeiFlag = "1".equals(setting.futanBumonShukeiFilter()) ? true : false;
				boolean isKeihi = "keihi".equals(parent) ? true : false ;
				String setStr = setting.keihimeisaiSecurityPsettei();
				if((isKeihi && setStr.equals("1")) || syuukeiFlag){
					spList = buLogic.selectSecurityPatternList(userId,kihyouBumonCd);
					if (spList.isEmpty() && !adminFlg){
						errorList.add("ユーザーまたは部門にセキュリティパターンが設定されていません。");
						return "error";
					}
				}
			}
			
			//管理者ユーザなら全表示させるためセキュリティパターンリストクリア
			if(adminFlg) spList.clear();

			if( mode == null || !( ("1".equals(mode)) || ("2".equals(mode)) || ("3".equals(mode)) || ("4".equals(mode)))){
				throw new EteamAccessDeniedException("モード指定異常");
			}
			
			//決算期番号リスト
			List<Integer> kiList = getKiList(Integer.parseInt(mode));
			
			//集計部門名を翌期優先でそれぞれ1件ずつ取得
			syuukeiBumonList = masterKanriLogic.loadSyuukeiBumonFromSptn(spList, kiList, syuukeiBumonCd);

			//名前が取れたら返す
			String name = "";
			if (syuukeiBumonList != null && !syuukeiBumonList.isEmpty()) {
				name = (String) syuukeiBumonList.get(0).get("syuukei_bumon_name");
			}
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
	
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		buLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
	}
	
	/**
	 * 使用する決算期番号のリストを取得
	 * @param smode 処理モード
	 * @return 決算期番号リスト
	 */
	protected List<Integer> getKiList(int smode){
		
		List<Integer> kbList = new ArrayList<Integer>();
		int intKes = masterKanriLogic.findKessankiBangou(EteamCommon.toDate(this.kaikeiCommonLogic.getKiDate(smode == 1 ? "" : denpyouId)));
		
		switch(smode){
			case 1:
				//伝票系
				//(申請前) 当日日付(クライアントPC日付)に該当するkiと、その-1(翌期分)のkiに該当する内部決算期
				//(申請後) 該当伝票の申請日に該当するkiと、その-1(翌期分)のkiに該当する内部決算期
				if(intKes >= 1){kbList.add(intKes - 1);}
				if(intKes >= 0){kbList.add(intKes);}
				break;
			case 2:
				//管理系
				//当日日付(クライアントPC日付)に該当するkiと、その-1(翌期分)のkiに該当する内部決算期
				if(intKes >= 1){kbList.add(intKes - 1);}
				if(intKes >= 0){kbList.add(intKes);}
				break;
			case 3:
			case 4:
				//検索系
				//0～1(翌期～当期)のkiに該当する内部決算期
				kbList.add(0);
				kbList.add(1);
				break;
			default:
				
		}
		return kbList;
	}

}
