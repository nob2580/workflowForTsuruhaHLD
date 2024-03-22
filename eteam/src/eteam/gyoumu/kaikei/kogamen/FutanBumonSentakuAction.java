package eteam.gyoumu.kaikei.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.GyoumuRoleId;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.BumonMasterAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.KiKesnAbstractDao;
import eteam.database.abstractdao.KiShouhizeiSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouninJoukyouAbstractDao;
import eteam.database.abstractdao.ShouninRouteAbstractDao;
import eteam.database.dao.BumonMasterDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiKesnDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouninJoukyouDao;
import eteam.database.dao.ShouninRouteDao;
import eteam.database.dto.BumonMaster;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 負担部門選択ダイアログAction
 * 経費明細一覧と執行状況一覧を除く画面から呼ばれる
 */
@Getter @Setter @ToString
public class FutanBumonSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** 勘定科目コード */
	String kamokuCd;
	/** 伝票ID */
	String denpyouId;
	/** 仕訳枝番 */
	Integer shiwakeEdano;
	/** 伝票区分 */
	String denpyouKbn;
	/** 起票部門コード */
	String kihyouBumonCd;
	/** 集計部門コード */
	String syuukeiBumonCd;
	
	/** 集計部門名 */
	String syuukeiBumonName;
	
	
//＜画面入力以外＞
	/** 負担部門コード */
	String futanBumonCd;
	/** 勘定科目名 */
	String kamokuName;
	/** 負担部門一覧 */
	List<GMap> futanBumonList;
	/** 集計部門コード使用フラグ */
	boolean syuukeiFlag;
	
	/** 処理モード(1:伝票編集 2:全表示 3:伝票検索 4:拠点（検索系セキュリティ無し) */
	String mode;
	
	/** マスターSELECT */
	MasterKanriCategoryLogic masterKanriLogic;
	/** ワークフローカテゴリー */
	WorkflowCategoryLogic wfLogic;
	/** 部門ユーザー管理ロジック */
	BumonUserKanriCategoryLogic buLogic;
	/** 会計共通ロジック */
	KaikeiCommonLogic kaikeiCommonLogic;
	/** 科目マスターDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** 部門マスターDao */
	BumonMasterAbstractDao bumonMasterDao;
	/** 承認ルートDao */
	ShouninRouteAbstractDao shouninRouteDao;
	/** 承認状況Dao */
	ShouninJoukyouAbstractDao shouninJoukyouDao;
	/** （期別）消費税設定Dao */
	KiShouhizeiSettingAbstractDao kiShouhizeiSettingDao;
	/** 決算期Dao */
	KiKesnAbstractDao kiKesnDao;
	/** 仕訳パターンマスターDAO */
	ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;
	
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(kamokuCd, 1, 8, "勘定科目コード", false);
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目										EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{kamokuCd, "勘定科目コード", "0"},
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
		hissuCheck(1);
		formatCheck();
		if(0 < errorList.size()){ return "error"; }

		if (isEmpty(mode))
		{
			return "success";
		}
		
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);
			
			// 集計部門使用設定をチェック
			syuukeiFlag = "1".equals(setting.futanBumonShukeiFilter());

			//2.データ存在チェック
			var kamoku = this.kamokuMasterDao.find(kamokuCd);
			if(kamoku != null) {
				kamokuName = kamoku.kamokuNameRyakushiki;
			}
			//3.アクセス可能チェック
			//4.処理

			if ("4".equals(mode))
			{
				Search4Kyoten();
			}
			else if(!syuukeiFlag){
				//集計部門使用しない場合
				futanBumonList = this.bumonMasterDao.load().stream().map(BumonMaster::getMap).collect(Collectors.toList()); // 集計部門がややこしいので、一旦Gmapのままで
			}else{
				//集計部門使用する
				//決算期番号リスト
				List<Integer> kiList = getKiList(Integer.parseInt(mode));
				
				//集計部門名取得
				if( isNotEmpty(syuukeiBumonCd) ){
					List<String> sbList = new ArrayList<String>();
					sbList.add(syuukeiBumonCd);
					List<GMap> list = masterKanriLogic.loadSyuukeiBumonList(sbList,kiList);
					if(list != null && !(list.isEmpty()) ) {
						//TODO 取得集計部門名の調整必要？
						syuukeiBumonName = list.get(0).get("syuukei_bumon_name").toString();
					}else{
						errorList.add("集計部門コードが存在しません。");
						return "error";
					}
				}
				
				//呼び出された元画面により表示する負担部門の制御変更
				
				//adminユーザなら集計部門指定時以外は全データ取らせる
				boolean adminFlg = GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId());
				
				//mode1,3の非管理者はセキュリティあり
				List<Integer> spList = null;
				List<GMap> sbList = null;
				if( ("1".equals(mode) || "3".equals(mode)) && !adminFlg){
					
					spList = buLogic.selectSecurityPatternList(this.getUserId(), kihyouBumonCd);
					if (spList == null || spList.isEmpty()){
						errorList.add("ユーザーまたは部門にセキュリティパターンが設定されていません。");
						return "error";
					}
					
					if( !isEmpty(syuukeiBumonCd) ){
						//集計部門が指定されている場合はそれを用いて負担部門リストを絞り込み
						//指定された集計部門が該当起票者or起票部門で使用できる部門かチェック
						sbList = masterKanriLogic.loadSyuukeiBumonFromSptn(spList, kiList, syuukeiBumonCd);
						if( sbList == null || sbList.isEmpty() ){
							errorList.add("使用できない集計部門コードが指定されています。");
							return "error";
						}
					}
				}
				
				// sbで取ったものがあるなら再取得しない
				futanBumonList = sbList == null ? masterKanriLogic.loadFutanBumonFromSptn(spList,kiList,syuukeiBumonCd) : sbList;
			}
			if(futanBumonList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//5.戻り値を返す
			return "success";
		}
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
		this.kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		this.bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);
		this.shouninRouteDao = EteamContainer.getComponent(ShouninRouteDao.class, connection);
		this.shouninJoukyouDao = EteamContainer.getComponent(ShouninJoukyouDao.class, connection);
		this.kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		this.kiKesnDao = EteamContainer.getComponent(KiKesnDao.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
	}
	
	/**
	 * 拠点用検索
	 */
	protected void Search4Kyoten() {
		// 拠点
		if (isEmpty(syuukeiBumonCd)){
			futanBumonList = this.bumonMasterDao.load().stream().map(BumonMaster::getMap).collect(Collectors.toList());
		}else {
			List<Integer> kiList = getKiList(Integer.parseInt(mode));
			futanBumonList = masterKanriLogic.loadFutanBumonFromNoSptn(kiList, syuukeiBumonCd);
		}
	}
	
	//↑↓対の構造保て
	/**
	 * 名称取得イベント
	 * @return 処理結果
	 */
	public String getNameAndKbn() {
		
		//モード等を参照して、ユーザで使用できる名前でなければreturn
		if (isEmpty(mode))
		{
			return "success";
		}

		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			GMap record = null;
			
			// 集計部門使用設定をチェック
			syuukeiFlag = "1".equals(setting.futanBumonShukeiFilter());
			
			//adminユーザなら集計部門指定時以外は全データ取らせる
			boolean adminFlg = GyoumuRoleId.SYSTEM_KANRI.equals(getUser().getGyoumuRoleId());
			
			//集計部門使用しない場合、mode2, 4, admin→セキュリティなし。集計部門指定時以外は全データ取得させる
			if(!syuukeiFlag || "2".equals(mode) || "4".equals(mode) || adminFlg){
				var dto = this.bumonMasterDao.find(futanBumonCd);
				record = dto == null ? null : dto.map; // note: 集計部門は別テーブルにあるので、普通にfindするだけでOK
			}else{
				//決算期番号リスト
				List<Integer> kiList = getKiList(Integer.parseInt(mode));
				
				List<Integer> spList = buLogic.selectSecurityPatternList(this.getUserId(), kihyouBumonCd);
				record = masterKanriLogic.findBumonMasterByCdAndSptn(futanBumonCd, kiList, spList);
				// DISTINCT沼に突っ込みたくないので、上記条件で部門が取得できた場合だけ、仕入区分取得用に再取得
				// 動作優先の仮対応で、パフォーマンスから見てもよくはないので、いずれは要修正。
				if(record != null)
				{
					var dto = this.bumonMasterDao.find(futanBumonCd);
					record = dto == null ? null : dto.map; // note: 集計部門は別テーブルにあるので、普通にfindするだけでOK
				}
			}

			//名前が取れたら返す
			boolean isNull = record == null;
			var kamokuMaster = this.kamokuMasterDao.find(kamokuCd);
			var shiwakePatternMaster = (this.denpyouKbn ==null || this.shiwakeEdano == null) ? null : this.shiwakePatternMasterDao.find(denpyouKbn, this.shiwakeEdano);
			var kesnDto = this.kiKesnDao.findBySingleDate(toDate(this.kaikeiCommonLogic.getKiDate(denpyouKbn)));
			Integer kesn = kesnDto == null ? null : kesnDto.kesn;
			var kiShouhizeiDto = kesn == null ? null : this.kiShouhizeiSettingDao.find(kesn);
			var shiireZeigakuAnbunFlg = kiShouhizeiDto == null ? 0 : kiShouhizeiDto.shiireZeigakuAnbunFlg;
			GMap map = new GMap();
			// 仕入区分は、部門の値があればそれ、なければ取引→科目の、それもなければ空（クリア）
			// 仕入税額按分も考慮
			String shiirekbn = shiireZeigakuAnbunFlg == 0
					? ""
					: (!isNull && record.get("shiire_kbn") != null)
						? record.get("shiire_kbn").toString()
						: (shiwakePatternMaster != null && !isEmpty(shiwakePatternMaster.kariShiireKbn))
							? shiwakePatternMaster.kariShiireKbn
							: (kamokuMaster != null && kamokuMaster.shiireKbn != null)
								? kamokuMaster.shiireKbn.toString()
								: "";
			map.put("futanBumonName", isNull ? "" : record.getString("futan_bumon_name"));
			map.put("shiireKbn", shiirekbn);
			
			HttpServletResponse response = ServletActionContext.getResponse();
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();
			out.print(new Gson().toJson(map));
			out.flush();
			out.close();
		} catch (IOException ex) {

		}

		return "success";
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
			case 2:
				//伝票系・管理系
				// 当日日付(クライアントPC日付)もしくは該当伝票の申請日（申請後伝票のみ）に該当するkiと、その-1(翌期分)のkiに該当する内部決算期
				if(intKes >= 1){kbList.add(intKes - 1);}
				if(intKes >= 0){kbList.add(intKes);}
				break;
			case 3:
			case 4:
				//検索系・拠点
				//0～1(翌期～当期)のkiに該当する内部決算期
				kbList.add(0);
				kbList.add(1);
				break;
			default:
		}
		
		return kbList;
		
	}

	/**
	 * ユーザーID取得
	 * @return ユーザーID
	 */
	protected String getUserId()
	{
		//起票者or起票部門のセキュリティパターンリスト取得
		var dto = isEmpty(denpyouId) ? null : this.shouninRouteDao.find(denpyouId, 1);
		// 何かがnullか空なら制御ユーザー、そうでないなら起票者データのユーザー
		return (dto == null || dto.userId == null)
			? getUser().getSeigyoUserId()
			: dto.userId;
	}
}
